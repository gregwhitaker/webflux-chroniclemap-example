package example.actuator;

import example.controller.model.ProductResponse;
import example.data.ProductRepository;
import example.data.model.Product;
import net.openhft.chronicle.map.ChronicleMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

/**
 * Cache management actuator endpoint.
 */
@Component
@RestControllerEndpoint(id = "cache")
public class CacheManagementEndpoint {
  private static final Logger LOG = LoggerFactory.getLogger(CacheManagementEndpoint.class);

  private final ProductRepository repo;
  private final ChronicleMap<String, Product> productCache;

  @Autowired
  public CacheManagementEndpoint(final ProductRepository repo,
                                 final ChronicleMap<String, Product> productCache) {
    this.repo = repo;
    this.productCache = productCache;
  }

  /**
   * Get the currently cached value for the key.
   *
   * @param cacheKey cache key
   * @return a {@link ProductResponse} if the value exists in the cache
   */
  @GetMapping(value = "/values/{key}")
  public Mono<ResponseEntity<ProductResponse>> getValue(@PathVariable("key") String cacheKey) {
    return Mono.fromSupplier(() -> productCache.get(cacheKey))
            .map(product -> ResponseEntity.ok(ProductResponse.from(product)))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  /**
   * Invalidate the currently cached value for the key.
   *
   * @param cacheKey cache key
   * @return an HTTP 204 if successful
   */
  @PostMapping(value = "/values/{key}/invalidate")
  public Mono<ResponseEntity<Object>> invalidateValue(@PathVariable("key") String cacheKey) {
    return Mono.fromSupplier(() -> productCache.get(cacheKey))
            .map(product -> {
              productCache.remove(cacheKey);
              return ResponseEntity.noContent().build();
            })
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }

  /**
   * Invalidate the currently cached value for the key and refresh it with the value stored in the database.
   *
   * @param cacheKey cache key
   * @return a {@link ProductResponse} if successful
   */
  @PostMapping(value = "/values/{key}/refresh")
  public Mono<ResponseEntity<ProductResponse>> refreshValue(@PathVariable("key") String cacheKey) {
    final Mono<Product> cacheMono = Mono.defer(() -> repo.findOne(cacheKey))
            .map(product -> {
              productCache.put(cacheKey, product);
              return product;
            });

    return Mono.fromSupplier(() -> productCache.get(cacheKey))
            .flatMap(product -> cacheMono)
            .map(product -> ResponseEntity.ok(ProductResponse.from(product)))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }
}
