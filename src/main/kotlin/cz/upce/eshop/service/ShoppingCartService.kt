package cz.upce.eshop.service

import cz.upce.eshop.entity.Product

interface ShoppingCartService {
	fun add(id: Long)
	fun remove(id: Long)
	val cart: Map<Product, Int>
	fun checkout()
}