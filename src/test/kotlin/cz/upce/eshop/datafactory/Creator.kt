package cz.upce.eshop.datafactory

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.ApplicationContext
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import java.sql.Date
import javax.persistence.Entity
import javax.persistence.Id
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.typeOf

@ExperimentalStdlibApi
@Component
@Suppress("UNCHECKED_CAST")
class Creator(
	private val applicationContext: ApplicationContext
) {
	private val log: Log = LogFactory.getLog(Creator::class.java)

	fun saveEntities(vararg entities: Any) = entities.forEach { entity -> saveEntity(entity) }

	fun save(entity: Any) : Any = saveEntity(entity)

	@JvmOverloads
	fun saveEntity(entity: Any, deleteOthers: Boolean = false): Any {
		entity::class.memberProperties
			.map { Pair(it.name, (it as KProperty1<Any, *>).get(entity)) }
			.toMap()
			.also { initializeListOfFields(entity) }
			.entries
			.filter { element -> element.value != null }
			.forEach {
				saveChildEntity(it)
			}

		(applicationContext.getBean(getInstanceName(entity::class)) as JpaRepository<Any, *>)
			.also {
				if (deleteOthers) {
					it.deleteAllInBatch()
				}
			}.also { it.save(entity) }

		return entity
	}

	private fun initializeListOfFields(entity: Any) {
		entity::class.memberProperties
			.forEach { field ->
				var propValue = (field as KProperty1<Any, *>).get(entity)
				if (propValue == null && !field.hasAnnotation<Id>()) {
					propValue = when {
						field.returnType.isSubtypeOf(typeOf<String?>()) -> """Test ${field.name}"""
						field.returnType.isSubtypeOf(typeOf<Date?>()) -> Date(System.currentTimeMillis())
						field.returnType.isSubtypeOf(typeOf<Long?>()) -> null
						field.returnType.isSubtypeOf(typeOf<Set<*>?>()) -> mutableSetOf<Any>()
						else -> (field.returnType.classifier as KClass<*>).createInstance()
					}
					(field as KMutableProperty1<*, *>).setter.call(entity, propValue)
				}
				saveChildEntity(propValue ?: log.info("Skipping " + field.name + ", as it is probably private"))
			}
	}

	private fun saveChildEntity(propValue: Any) {
		if (propValue::class.hasAnnotation<Entity>()) {
			saveEntity(propValue)
			(applicationContext.getBeansOfType(JpaRepository::class.java)[getInstanceName(propValue::class)] as JpaRepository<Any, *>)
				.also {
					it.save(propValue)
				}
		}
	}

	private fun getInstanceName(valueClass: KClass<out Any>): String {
		return """${
			valueClass.simpleName?.substring(0, 1)?.toLowerCase()
		}${valueClass.simpleName?.substring(1)}Repository"""
	}
}