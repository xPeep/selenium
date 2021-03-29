package cz.upce.eshop.entity

import javax.persistence.*

@Entity(name = "order_form")
class Order(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,
	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	var state: StateEnum? = null,
	@OneToMany(mappedBy = "id")
	var orderHasProducts: MutableSet<OrderHasProduct>? = mutableSetOf()
)