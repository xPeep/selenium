package cz.upce.eshop.datafactory

import org.apache.commons.beanutils.PropertyUtils
import org.apache.commons.lang3.reflect.FieldUtils
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.sql.Date
import javax.persistence.Entity
import javax.persistence.Id
import kotlin.reflect.KClass

@Component
class Creator(
	private val applicationContext: ApplicationContext
) {

	private val log: Log = LogFactory.getLog(Creator::class.java)

	fun saveMore(vararg entities: Any) = entities.forEach { entity ->
		saveEntity(entity)
	}

	fun save(entity: Any) = saveEntity(entity)

	@JvmOverloads
	fun saveEntity(entity: Any, deleteOthers: Boolean = false): Any {
		try {
			val props = PropertyUtils.describe(entity)
			initializeListOfFields(entity)

			for (propName in props.keys) {
				props[propName]?.let { saveChildEntity(it) }
			}

			val dao = getDao(entity)

			if (deleteOthers) {
				dao.deleteAllInBatch()
			}

			dao.save(cast(entity, entity::class))

		} catch (e: Exception) {
			throw IllegalStateException("Problem", e)
		}
		return entity
	}

	private fun initializeListOfFields(entity: Any) {
		val allFields = FieldUtils.getAllFieldsList(entity.javaClass)

		allFields.forEach { field ->
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
	
	private fun getDao(entity: Any): JpaRepository<Any, *> {
		val repoClassName = entity::class.simpleName
		val repositoryBeanName =
			"""${repoClassName?.substring(0, 1)?.toLowerCase()}${repoClassName?.substring(1)}Repository"""
		return applicationContext.getBean(repositoryBeanName) as JpaRepository<Any, *>
	}

	private fun saveChildEntity(propValue: Any) {
		val valueClass = propValue.javaClass
		val isEntity: Boolean = isEntity(valueClass)
		if (isEntity) {
			saveEntity(propValue)
			val daoName = """${
				valueClass.simpleName.substring(0, 1).toLowerCase()
			}${valueClass.simpleName.substring(1)}Repository"""
			(applicationContext.getBeansOfType(JpaRepository::class.java)[daoName] as JpaRepository<Any, *>).apply {
				save(cast(propValue, propValue::class))
			}
		}
	}

	private fun <T : Any> cast(any: Any, clazz: KClass<out T>): T = clazz.javaObjectType.cast(any)

	private fun isEntity(valueClass: Class<*>): Boolean {
		return valueClass.getDeclaredAnnotationsByType(Entity::class.java).isNotEmpty()
	}
}