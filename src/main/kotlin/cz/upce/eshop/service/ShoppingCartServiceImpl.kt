package cz.upce.eshop.service

import cz.upce.eshop.entity.Order
import cz.upce.eshop.entity.OrderHasProduct
import cz.upce.eshop.entity.Product
import cz.upce.eshop.entity.StateEnum
import cz.upce.eshop.repository.OrderHasProductRepository
import cz.upce.eshop.repository.OrderRepository
import cz.upce.eshop.repository.ProductRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ShoppingCartServiceImpl(
	private val productRepository: ProductRepository,
	private val orderRepository: OrderRepository,
	private val orderHasProductRepository: OrderHasProductRepository
) : ShoppingCartService {

	override val cart = mutableMapOf<Product, Int>()

	override fun add(id: Long) {
		val product = productRepository.findById(id).orElseThrow { NoSuchElementException() }!!
		if (cart.containsKey(product)) {
			cart.replace(product, cart[product]!! + 1)
		} else {
			cart[product] = 1
		}
	}

	override fun remove(id: Long) {
		val product = productRepository.findById(id).orElseThrow { NoSuchElementException() }!!
		if (cart.containsKey(product)) {
			if (cart[product]!! > 1) {
				cart.replace(product, cart[product]!! - 1)
			} else {
				cart.remove(product)
			}
		}
	}

	override fun checkout() {
		val order = Order()
		order.state = StateEnum.NEW
		orderRepository.save(order)
		for ((key, value) in cart) {
			val orderHasProduct = OrderHasProduct()
			orderHasProduct.order = order
			orderHasProduct.product = key
			orderHasProduct.amount = value
			orderHasProductRepository.save(orderHasProduct)
		}
		cart.clear()
	}

}