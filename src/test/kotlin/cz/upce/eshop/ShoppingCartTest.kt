package cz.upce.eshop

import cz.upce.eshop.entity.Product
import cz.upce.eshop.repository.ProductRepository
import cz.upce.eshop.service.ShoppingCartService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class ShoppingCartTest
@Autowired constructor(
	private val productRepository: ProductRepository,
	private val shoppingCartService: ShoppingCartService
) {

	@Test
	fun addOneToShoppingCart() {
		val product = Product()
		product.productName = "MyProduct"
		productRepository.save(product)
		val all = productRepository.findAll()
		val productId = all.first()?.id ?: -1
		shoppingCartService.add(productId)

		// počet prvků v košíku = 1
		Assertions.assertThat(shoppingCartService.cart.size).isEqualTo(1)
		// obsahuje právě vložený produkt
		Assertions.assertThat(shoppingCartService.cart.containsKey(all[0])).isTrue
		// obsahuje vložený produkt v počtu = 1
		Assertions.assertThat(shoppingCartService.cart[all[0]]).isEqualTo(1)
		shoppingCartService.add(productId)
		Assertions.assertThat(shoppingCartService.cart[all[0]]).isEqualTo(2)
		shoppingCartService.add(productId)
		Assertions.assertThat(shoppingCartService.cart[all[0]]).isEqualTo(3)
		shoppingCartService.remove(productId)
		Assertions.assertThat(shoppingCartService.cart[all[0]]).isEqualTo(2)
		shoppingCartService.remove(productId)
		Assertions.assertThat(shoppingCartService.cart[all[0]]).isEqualTo(1)
		shoppingCartService.remove(productId)
		Assertions.assertThat(shoppingCartService.cart.containsKey(all[0])).isFalse
	}
}