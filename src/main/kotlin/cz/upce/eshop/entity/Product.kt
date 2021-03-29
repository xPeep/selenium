package cz.upce.eshop.entity

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

	@ManyToOne(optional = false)
	var supplier: Supplier = Supplier()

)