package cz.upce.eshop.ui

import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.*
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestImplementation {
	private var driver: WebDriver? = null

	@BeforeAll
	fun setupWebdriverChromeDriver() {
		val chromeDriverPath = TestImplementation::class.java.getResource("/chromedriver.exe").file
		System.setProperty(
			"webdriver.chrome.driver",
			chromeDriverPath
		)
	}

	@BeforeEach
	fun setup() {
		driver = ChromeDriver()
	}

	@AfterEach
	fun teardown() {
		if (driver != null) {
			driver?.quit()
		}
	}

	@Test
	fun verifyGitHubTitle() {
		driver?.get("https://www.github.com")
		assertThat(driver?.title, containsString("GitHub"))
	}
}