package cz.upce.eshop.entity

import javax.persistence.*

@Entity
class Supplier(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,
	@Column
	var name: String? = null
)