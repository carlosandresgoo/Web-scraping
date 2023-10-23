package Web.scraping.Web.scraping;

import Web.scraping.Web.scraping.models.Product;
import Web.scraping.Web.scraping.repositories.ProductRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class WebScrapingApplication implements CommandLineRunner {

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(WebScrapingApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--disable-extensions");
		options.addArguments("--no-sandbox");
		options.addArguments("--remote-allow-origins=*");

		// Definir la URL del sitio web
		String website = "https://www.ktronix.com/computadores-tablet/computadores-portatiles/c/BI_104_KTRON";

		// Configurar el sistema para usar el controlador de Chrome
		System.setProperty("webdriver.chrome.driver", "D:\\chromedriver\\chromedriver-win64\\chromedriver.exe");
		WebDriver driver = new ChromeDriver(options);

		// Inicializar el controlador de Chrome
		try  {
			// Abrir la página web en el navegador
			driver.get(website);

			// Esperar un tiempo para que la página se cargue completamente (puedes ajustar este tiempo según sea necesario)
			Thread.sleep(5000);

			// Obtener el contenido de la página como HTML
			String htmlContent = driver.getPageSource();

			// Parsear el HTML usando Jsoup
			Document doc = Jsoup.parse(htmlContent);

			// Encontrar elementos con una clase CSS específica y extraer su texto
			List<WebElement> productTitles = driver.findElements(By.className("product__item__top__title"));
			List<WebElement> productPrices = driver.findElements(By.className("product__price--discounts__price"));

			// Iterar a través de los elementos y guardarlos en la base de datos
			int numberOfProductsToDisplay = Math.min(5, Math.min(productTitles.size(), productPrices.size()));
			for (int i = 0; i < numberOfProductsToDisplay; i++) {
				String productName = productTitles.get(i).getText();
				String productPrice = productPrices.get(i).getText();

				// Crear un objeto Producto y guardarlo en la base de datos
				Product product = new Product();
				product.setName(productName);
				product.setPrice(productPrice);
				productRepository.save(product);
			}

		} catch (Exception e) {
			System.err.println("Ocurrió un error: " + e.getMessage());
		}
	}

}




