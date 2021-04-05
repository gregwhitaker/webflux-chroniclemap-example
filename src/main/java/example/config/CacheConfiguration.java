package example.config;

import example.data.model.Product;
import net.openhft.chronicle.map.ChronicleMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Chronicle off-heap cache configuration
 */
@Configuration
public class CacheConfiguration {

  @Bean
  public ChronicleMap<String, Product> productCache() {
    final Product averageProduct = new Product();
    averageProduct.setId("123456789");
    averageProduct.setName("Product123456789");
    averageProduct.setDescription("Product123456789 Description");
    averageProduct.setActive(true);
    averageProduct.setStartTime(System.currentTimeMillis());
    averageProduct.setEndTime(System.currentTimeMillis());


    return ChronicleMap.of(String.class, Product.class)
            .name("product-cache")
            .entries(30_000_000L)
            .averageKey("123456789")
            .averageValue(averageProduct)
            .create();
  }
}
