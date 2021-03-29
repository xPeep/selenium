package cz.upce.eshop.repository

import cz.upce.eshop.entity.OrderHasProduct
import org.springframework.data.jpa.repository.JpaRepository

interface OrderHasProductRepository : JpaRepository<OrderHasProduct?, Long?>