package cz.upce.eshop.entity

import javax.persistence.*

@Entity
class OrderHasProduct(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,
	@ManyToOne
	var order: Order? = null,
	@ManyToOne
	var product: Product? = null,
	var amount: Int? = null
)