package cz.upce.eshop.repository

import cz.upce.eshop.entity.Product
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product?, Long?> {
	@EntityGraph(attributePaths = ["productInOrders"])
	fun findProductByProductNameContains(contains: String?): Product?

	@EntityGraph(attributePaths = ["productInOrders"])
	@Query(" select p from Product p where p.id between 1 and 2")
	fun findProductByIdBetween(start: Long?, finish: Long?): List<Product?>?
}