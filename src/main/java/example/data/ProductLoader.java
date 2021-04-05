package example.data;

import example.data.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * Loads test data into Redis on application startup.
 */
@Component
public class ProductLoader {
  private static final Logger LOG = LoggerFactory.getLogger(ProductLoader.class);

  private final ReactiveRedisConnectionFactory connFactory;
  private final ReactiveRedisOperations<String, Product> productOps;

  @Autowired
  public ProductLoader(final ReactiveRedisConnectionFactory connFactory,
                       final ReactiveRedisOperations<String, Product> productOps) {
    this.connFactory = connFactory;
    this.productOps = productOps;
  }

  @PostConstruct
  public void loadData() {
    connFactory.getReactiveConnection()
            .serverCommands()
            .flushAll()
            .thenMany(Flux.range(1, 10)
                    .map(this::generateProduct)
                    .flatMap(product -> productOps.opsForValue().set(product.getId(), product))
            )
            .doOnSubscribe(subscription -> LOG.info("Loading test data..."))
            .doOnComplete(() -> LOG.info("Test data load complete!"))
            .subscribe();
  }

  /**
   * Generates a random product for testing.
   *
   * @param id product identifier
   * @return a {@link Product}
   */
  private Product generateProduct(final int id) {
    final Product product = new Product();
    product.setId(String.valueOf(id));
    product.setName("Product" + id);
    product.setDescription("Product" + id + " Description");
    product.setActive(id % 2 == 1);
    product.setStartTime(OffsetDateTime.of(2021, 1, 1, 0, 0,0, 0, ZoneOffset.UTC).toEpochSecond());
    product.setEndTime(OffsetDateTime.of(2021, 1, 1, 0, 0,0, 0, ZoneOffset.UTC).plusDays(30).toEpochSecond());

    return product;
  }
}
