package cz.upce.eshop.dto

import org.springframework.web.multipart.MultipartFile

class AddOrEditProductDto {
	var id: Long? = null
	var productName: String? = null
	var description: String? = null
	var image: MultipartFile? = null
}