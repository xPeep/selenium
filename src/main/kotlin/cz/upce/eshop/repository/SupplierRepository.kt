package cz.upce.eshop.repository

import cz.upce.eshop.entity.Supplier
import org.springframework.data.jpa.repository.JpaRepository

interface SupplierRepository : JpaRepository<Supplier?, Long?>