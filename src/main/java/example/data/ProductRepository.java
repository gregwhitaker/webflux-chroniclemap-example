package example.data;

import example.data.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Repository for interacting with {@link Product} objects in Redis.
 */
@Component
public class ProductRepository {
  private static final Logger LOG = LoggerFactory.getLogger(ProductRepository.class);

  private final ReactiveRedisOperations<String, Product> productOps;

  @Autowired
  public ProductRepository(final ReactiveRedisOperations<String, Product> productOps) {
    this.productOps = productOps;
  }

  /**
   * Finds a product by id.
   *
   * @param id product identifier
   * @return a {@link Product} if found; otherwise empty
   */
  public Mono<Product> findOne(final String id) {
    return productOps.opsForValue().get(id);
  }
}
