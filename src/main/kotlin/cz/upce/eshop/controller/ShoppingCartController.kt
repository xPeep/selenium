package cz.upce.eshop.controller

import cz.upce.eshop.service.ShoppingCartService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ShoppingCartController(private val shoppingCartService: ShoppingCartService) {
	@GetMapping("/shopping-cart-add/{id}")
	fun shoppingCartAdd(@PathVariable id: Long, model: Model?): String {
		shoppingCartService.add(id)
		return "redirect:/shopping-cart"
	}

	@GetMapping("/shopping-cart-remove/{id}")
	fun shoppingCartRemove(@PathVariable id: Long, model: Model?): String {
		shoppingCartService.remove(id)
		return "redirect:/shopping-cart"
	}

	@GetMapping("/shopping-cart")
	fun showShoppingCart(model: Model): String {
		model.addAttribute("shoppingCart", shoppingCartService.cart)
		return "shopping-cart"
	}
}