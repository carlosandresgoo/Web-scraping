package Web.scraping.Web.scraping.repositories;

import Web.scraping.Web.scraping.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Long> {
}
