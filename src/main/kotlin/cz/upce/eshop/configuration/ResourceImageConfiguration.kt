package cz.upce.eshop.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class ResourceImageConfiguration : WebMvcConfigurer {
	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		registry.addResourceHandler("/images/**")
			.addResourceLocations("file:/home/pfilip/Projects/upce/eshop/images/")
	}
}