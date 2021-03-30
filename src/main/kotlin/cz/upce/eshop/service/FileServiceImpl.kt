package cz.upce.eshop.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileServiceImpl : FileService {
	override fun upload(image: MultipartFile?): String? {
		try {
			val path = Paths.get("C:\\Users\\Pepe\\IdeaProjects\\nnpia-eshop-full-kotlin\\images\\" + image!!.originalFilename)
			Files.copy(image.inputStream, path, StandardCopyOption.REPLACE_EXISTING)
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return image!!.originalFilename
	}
}