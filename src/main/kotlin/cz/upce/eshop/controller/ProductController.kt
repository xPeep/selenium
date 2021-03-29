package cz.upce.eshop.controller

import cz.upce.eshop.dto.AddOrEditProductDto
import cz.upce.eshop.entity.Product
import cz.upce.eshop.repository.ProductRepository
import cz.upce.eshop.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ProductController {
	@Autowired
	private val productRepository: ProductRepository? = null

	@Autowired
	private val fileService: FileService? = null

	@ExceptionHandler(RuntimeException::class)
	fun handleException(): String {
		return "error"
	}

	@GetMapping("/")
	fun showAllProducts(model: Model): String {
		model.addAttribute("productList", productRepository!!.findAll())
		return "product-list"
	}

	@GetMapping("/product-detail/{id}")
	fun showProductDetail(@PathVariable(required = false) id: Long, model: Model): String {
		model.addAttribute("product", productRepository!!.findById(id).get())
		return "product-detail"
	}

	@GetMapping(value = ["/product-form", "/product-form/{id}"])
	fun showProductForm(@PathVariable(required = false) id: Long?, model: Model): String {
		if (id != null) {
			val byId = productRepository!!.findById(id).orElse(Product())!!
			val dto = AddOrEditProductDto()
			dto.id = byId.id
			dto.productName = byId.productName
			dto.description = byId.description
			model.addAttribute("product", dto)
		} else {
			model.addAttribute("product", AddOrEditProductDto())
		}
		return "product-form"
	}

	@PostMapping("/product-form-process")
	fun productFormProcess(addOrEditProductDto: AddOrEditProductDto): String {
		val product = Product()
		product.id = addOrEditProductDto.id
		product.productName = addOrEditProductDto.productName
		product.description = addOrEditProductDto.description
		val fileName = fileService!!.upload(addOrEditProductDto.image)
		product.pathToImage = fileName
		productRepository!!.save(product)
		return "redirect:/"
	}
}