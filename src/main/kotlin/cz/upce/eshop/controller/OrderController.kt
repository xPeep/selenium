package cz.upce.eshop.controller

import cz.upce.eshop.service.ShoppingCartService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class OrderController(private val shoppingCartService: ShoppingCartService) {
	@GetMapping("/checkout")
	fun checkout(model: Model?): String {
		shoppingCartService.checkout()
		return "checkout"
	}
}