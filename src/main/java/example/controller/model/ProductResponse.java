package example.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import example.data.model.Product;

import java.time.Instant;
import java.time.ZoneOffset;

/**
 * Product response DTO
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "active",
        "startDate",
        "endDate"
})
public class ProductResponse {

  /**
   * Creates an instance of {@link ProductResponse} from {@link Product}.
   *
   * @param product product to convert
   * @return a {@link ProductResponse}
   */
  public static ProductResponse from(final Product product) {
    final ProductResponse response = new ProductResponse();
    response.setId(product.getId());
    response.setName(product.getName());
    response.setDescription(product.getDescription());
    response.setActive(product.isActive());
    response.setStartDate(Instant.ofEpochSecond(product.getStartTime()).atOffset(ZoneOffset.UTC).toString());
    response.setEndDate(Instant.ofEpochSecond(product.getEndTime()).atOffset(ZoneOffset.UTC).toString());

    return response;
  }

  private String id;
  private String name;
  private String description;
  private boolean active;
  @JsonProperty("start_date") private String startDate;
  @JsonProperty("end_date") private String endDate;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }
}
