package cz.upce.eshop.datafactory

import cz.upce.eshop.entity.Product
import cz.upce.eshop.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.stereotype.Component

@Component
@ComponentScan
class ProductTestDataFactory {
	@Autowired
	private val productRepository: ProductRepository? = null

	@Autowired
	private val supplierTestDataFactory: SupplierTestDataFactory? = null
	fun saveProduct(productName: String?) {
		val product = Product()
		product.productName = productName
		productRepository!!.save(product)
	}

	fun saveProduct(product: Product) {
		if (product.productName == null) product.productName = "Test product"
		if (product.description == null) product.description = "Test description"
		saveProductWithDefaultSpplier(product)
	}

	fun saveProductWithDefaultSpplier(product: Product) {
		val testSupplier = supplierTestDataFactory!!.saveSupplier()
		product.supplier = testSupplier
		productRepository!!.save(product)
	}
}