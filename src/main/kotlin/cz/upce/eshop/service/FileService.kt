package cz.upce.eshop.service

import org.springframework.web.multipart.MultipartFile

interface FileService {
	fun upload(image: MultipartFile?): String?
}