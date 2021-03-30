package cz.upce.eshop.datafactory

import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.logging.*
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.sql.Date
import javax.persistence.*
import kotlin.reflect.*
import kotlin.reflect.full.hasAnnotation

@Component
class Creator(
	private val applicationContext: ApplicationContext
) {

	private val log: Log = LogFactory.getLog(Creator::class.java)

	fun saveMore(vararg entities: Any) = entities.forEach { entity -> saveEntity(entity) }

	fun save(entity: Any) = saveEntity(entity)

	@JvmOverloads
	fun saveEntity(entity: Any, deleteOthers: Boolean = false): Any {
		try {
			val props = PropertyUtils.describe(entity)
			initializeListOfFields(entity)
			props.keys.forEach { propName -> props[propName]?.let { saveChildEntity(it) } }
			val dao = getDao(entity)
			if (deleteOthers) {
				dao.deleteAllInBatch()
			}
			dao.apply {
				this.save(entity)
			}
		} catch (e: Exception) {
			throw IllegalStateException("Problem", e)
		}
		return entity
	}

	private fun initializeListOfFields(entity: Any) {
		FieldUtils.getAllFieldsList(entity.javaClass).forEach { field ->
			try {
				field.isAccessible = true
				var propValue = FieldUtils.readField(field, entity)
				if (propValue == null) {
					if (field.getDeclaredAnnotationsByType(Id::class.java).isEmpty()) {
						val fieldClass = field.type
						propValue = if (fieldClass.isAssignableFrom(String::class.java)) {
							"""Test ${field.name}"""
						} else {
							when {
								Date::class.java == fieldClass -> Date(System.currentTimeMillis())
								Long::class.java == fieldClass -> 1L
								MutableSet::class.java == fieldClass -> HashSet<Any>()
								else -> fieldClass.getDeclaredConstructor().newInstance()
							}
						}
						PropertyUtils.setProperty(entity, field.name, propValue)
					}
				}

				if (propValue != null) {
					saveChildEntity(propValue)
				}

			} catch (e: IllegalAccessException) {
				log.info("""Skipping ${field.name}, as it is probably private""")
			}
		}
	}

	private fun saveChildEntity(propValue: Any) {
		val valueClass = propValue::class
		if (valueClass.hasAnnotation<Entity>()) {
			saveEntity(propValue)
			val daoName = getInstanceName(valueClass)
			(applicationContext.getBeansOfType(JpaRepository::class.java)[daoName] as JpaRepository<Any, *>).apply {
				this.save(propValue)
			}
		}
	}

	private fun getDao(entity: Any): JpaRepository<Any, *> {
		val repoClassName = entity::class
		val repositoryBeanName = getInstanceName(repoClassName)
		return applicationContext.getBean(repositoryBeanName) as JpaRepository<Any, *>
	}

	private fun getInstanceName(valueClass: KClass<out Any>): String {
		return """${
			valueClass.simpleName?.substring(0, 1)?.toLowerCase()
		}${valueClass.simpleName?.substring(1)}Repository"""
	}
}