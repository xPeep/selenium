package cz.upce.eshop.entity

import java.util.*
import javax.persistence.*

@Entity
class Product(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column
	var productName: String? = null,

	@Column(columnDefinition = "text")
	var description: String? = null,

	@Column
	var pathToImage: String? = null,

	@OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
	var productInOrders: MutableSet<OrderHasProduct>? = null,

	@ManyToOne()
	var supplier: Supplier? = null
) {
	override fun equals(o: Any?): Boolean {
		if (this === o) {
			return true
		}
		if (o == null || javaClass != o.javaClass) {
			return false
		}
		val product = o as Product
		return Objects.equals(id, product.id) && Objects.equals(productName, product.productName)
	}

	override fun hashCode(): Int {
		return Objects.hash(id, productName)
	}

}