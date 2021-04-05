package example.service;

import example.data.ProductRepository;
import example.data.model.Product;
import net.openhft.chronicle.map.ChronicleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Service for interacting with product data.
 */
@Component
public class ProductService {
  private static final Logger LOG = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository repo;
  private final ChronicleMap<String, Product> productCache;

  @Autowired
  public ProductService(final ProductRepository repo,
                        final ChronicleMap<String, Product> productCache) {
    this.repo = repo;
    this.productCache = productCache;
  }

  /**
   * Gets a product by id.
   *
   * @param id product identifier
   * @return a {@link Product} if found; otherwise empty
   */
  public Mono<Product> getProduct(final String id) {
    // Find the product in redis and add to cache if it is not cached
    final Mono<Product> cacheMono = Mono.defer(() -> repo.findOne(id))
            .map(product -> {
              productCache.put(id, product);
              return product;
            });

    return Mono.fromSupplier(() -> productCache.get(id))
            .switchIfEmpty(cacheMono);
  }
}
