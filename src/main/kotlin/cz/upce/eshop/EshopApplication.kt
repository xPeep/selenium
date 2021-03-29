package cz.upce.eshop

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EshopApplication

fun main(args: Array<String>) {
	runApplication<EshopApplication>(*args)
}
