package example.controller;

import example.controller.model.ProductResponse;
import example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Endpoint for querying products in the datastore.
 */
@RestController
public class ProductController {
  private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

  private final ProductService productService;

  @Autowired
  public ProductController(final ProductService productService) {
    this.productService = productService;
  }

  /**
   * Gets the product data for a specific product.
   *
   * @param id product identifier
   * @return a {@link ProductResponse} if found
   */
  @GetMapping(value = "/products/{id}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<ProductResponse>> getProduct(@PathVariable("id") String id) {
    return productService.getProduct(id)
            .map(product -> ResponseEntity.ok(ProductResponse.from(product)))
            .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
  }
}

