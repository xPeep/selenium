package cz.upce.eshop

import cz.upce.eshop.datafactory.ProductTestDataFactory
import cz.upce.eshop.repository.ProductRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(
	ProductTestDataFactory::class
)
internal class ProductRepositoryTest
@Autowired constructor(
	private val productRepository: ProductRepository,
	private val productTestDataFactory: ProductTestDataFactory
) {

	@Test
	fun saveProductTest() {
		productTestDataFactory.saveProduct("My product")
		val all = productRepository.findAll()
		Assertions.assertThat(all.size).isEqualTo(1)
	}
}