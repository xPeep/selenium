package cz.upce.eshop.datafactory

import cz.upce.eshop.entity.Supplier
import cz.upce.eshop.repository.SupplierRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SupplierTestDataFactory {
	@Autowired
	private val supplierRepository: SupplierRepository? = null
	fun saveSupplier(): Supplier {
		val testSupplier = Supplier()
		testSupplier.name = "Test supplier"
		supplierRepository!!.save(testSupplier)
		return testSupplier
	}
}