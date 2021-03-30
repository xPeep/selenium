package cz.upce.eshop.datafactory

import cz.upce.eshop.entity.Product
import cz.upce.eshop.repository.ProductRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@ExperimentalStdlibApi
@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(Creator::class)
class ProductRepositoryTest
@ExperimentalStdlibApi
@Autowired constructor(
	private val creator: Creator,
	private val productRepository: ProductRepository
) {

	@Test
	fun saveProductTest() {
		val testProduct = Product().apply { productName = "MyProduct" }

		creator.save(testProduct)

		val all = productRepository.findAll()
		Assertions.assertThat(all.size).isEqualTo(1)

		val readFromDb = productRepository.findById(testProduct.id ?: -1).get()
		Assertions.assertThat(readFromDb.productName).isEqualTo("MyProduct")
		Assertions.assertThat(readFromDb.description).isEqualTo("Test description")
		Assertions.assertThat(readFromDb.supplier?.name).isEqualTo("Test name")
	}
}

