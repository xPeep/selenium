package cz.upce.eshop.ui

import cz.upce.eshop.datafactory.Creator
import cz.upce.eshop.entity.Product
import cz.upce.eshop.repository.ProductRepository
import org.junit.Assert
import org.junit.jupiter.api.*
import org.junit.runner.RunWith
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExperimentalStdlibApi
class ProductUITest
@Autowired constructor(
	val productRepository: ProductRepository,
	val creator: Creator
) {

	private var driver: WebDriver? = null

	@BeforeAll
	fun setupWebdriverChromeDriver() {
		val chromeDriverPath = ProductUITest::class.java.getResource("/chromedriver.exe").file
		System.setProperty(
			"webdriver.chrome.driver",
			chromeDriverPath
		)
	}

	@BeforeEach
	fun setup() {
		driver = ChromeDriver()
		productRepository.deleteAll()
	}

	@AfterEach
	fun teardown() {
		if (driver != null) {
			driver?.quit()
		}
	}

	@Test
	fun addProductTest() {
		singleProductAddTest()
	}

	@Test
	fun addProductTest2() {
		singleProductAddTest2()
	}

	@Test
	fun productList() {
		creator.saveEntities(
			Product(productName = "product1"),
			Product(productName = "product2"),
			Product(productName = "product3")
		)

		driver?.get("http://localhost:8080/")

		Assert.assertEquals(1, driver?.findElements(By.xpath("//h3[text()='product1']"))?.size)
		Assert.assertEquals(1, driver?.findElements(By.xpath("//h3[text()='product2']"))?.size)
		Assert.assertEquals(1, driver?.findElements(By.xpath("//h3[text()='product3']"))?.size)
	}

	fun singleProductAddTest2() {
		driver?.get("http://localhost:8080/product-form")
		driver?.findElement(By.id("productName"))?.sendKeys("Naruto")
		driver?.findElement(By.id("image"))
			?.sendKeys("C:\\Users\\Pepe\\IdeaProjects\\nnpia-eshop-full-kotlin\\images\\img.png")
		driver?.findElement(By.xpath("//input[@type='submit']"))?.click()

		Assert.assertEquals(1, driver?.findElements(By.xpath("//h2[text()='Product list']"))?.size)
		Assert.assertEquals(1, driver?.findElements(By.xpath("//h3[text()='Naruto']"))?.size)
	}

	fun singleProductAddTest() {
		driver?.get("http://localhost:8080/product-form")
		driver?.findElement(By.id("productName"))?.sendKeys("Samara")
		driver?.findElement(By.id("image"))
			?.sendKeys("C:\\Users\\Pepe\\IdeaProjects\\nnpia-eshop-full-kotlin\\images\\instambul.jpg")
		driver?.findElement(By.xpath("//input[@type='submit']"))?.click()

		Assert.assertEquals(1, driver?.findElements(By.xpath("//h2[text()='Product list']"))?.size)
		Assert.assertEquals(1, driver?.findElements(By.xpath("//h3[text()='Samara']"))?.size)
	}
}