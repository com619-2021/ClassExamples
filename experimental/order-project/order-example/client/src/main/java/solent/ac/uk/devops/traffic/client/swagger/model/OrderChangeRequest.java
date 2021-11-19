/*
 * Smart Port API
 * Solent university devops https://github.com/com619-2021
 *
 * OpenAPI spec version: v1
 * Contact: craig.gallen@solent.ac.uk
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package solent.ac.uk.devops.traffic.client.swagger.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import solent.ac.uk.devops.traffic.client.swagger.model.Order;
import solent.ac.uk.devops.traffic.client.swagger.model.Party;
/**
 * OrderChangeRequest
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-11-19T14:34:17.894448800Z[Europe/London]")
public class OrderChangeRequest {
  @JsonProperty("approvedDate")
  private OffsetDateTime approvedDate = null;

  @JsonProperty("changeReason")
  private String changeReason = null;

  @JsonProperty("changeRequest")
  private Order changeRequest = null;

  @JsonProperty("changeRequestor")
  private Party changeRequestor = null;

  @JsonProperty("href")
  private String href = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("requestDate")
  private OffsetDateTime requestDate = null;

  @JsonProperty("responseDescription")
  private String responseDescription = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    REQUESTED("REQUESTED"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }
    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("status")
  private StatusEnum status = null;

  @JsonProperty("uuid")
  private String uuid = null;

  public OrderChangeRequest approvedDate(OffsetDateTime approvedDate) {
    this.approvedDate = approvedDate;
    return this;
  }

   /**
   * Get approvedDate
   * @return approvedDate
  **/
  @Schema(description = "")
  public OffsetDateTime getApprovedDate() {
    return approvedDate;
  }

  public void setApprovedDate(OffsetDateTime approvedDate) {
    this.approvedDate = approvedDate;
  }

  public OrderChangeRequest changeReason(String changeReason) {
    this.changeReason = changeReason;
    return this;
  }

   /**
   * Get changeReason
   * @return changeReason
  **/
  @Schema(description = "")
  public String getChangeReason() {
    return changeReason;
  }

  public void setChangeReason(String changeReason) {
    this.changeReason = changeReason;
  }

  public OrderChangeRequest changeRequest(Order changeRequest) {
    this.changeRequest = changeRequest;
    return this;
  }

   /**
   * Get changeRequest
   * @return changeRequest
  **/
  @Schema(description = "")
  public Order getChangeRequest() {
    return changeRequest;
  }

  public void setChangeRequest(Order changeRequest) {
    this.changeRequest = changeRequest;
  }

  public OrderChangeRequest changeRequestor(Party changeRequestor) {
    this.changeRequestor = changeRequestor;
    return this;
  }

   /**
   * Get changeRequestor
   * @return changeRequestor
  **/
  @Schema(description = "")
  public Party getChangeRequestor() {
    return changeRequestor;
  }

  public void setChangeRequestor(Party changeRequestor) {
    this.changeRequestor = changeRequestor;
  }

  public OrderChangeRequest href(String href) {
    this.href = href;
    return this;
  }

   /**
   * Get href
   * @return href
  **/
  @Schema(description = "")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public OrderChangeRequest id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @Schema(description = "")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OrderChangeRequest requestDate(OffsetDateTime requestDate) {
    this.requestDate = requestDate;
    return this;
  }

   /**
   * Get requestDate
   * @return requestDate
  **/
  @Schema(description = "")
  public OffsetDateTime getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(OffsetDateTime requestDate) {
    this.requestDate = requestDate;
  }

  public OrderChangeRequest responseDescription(String responseDescription) {
    this.responseDescription = responseDescription;
    return this;
  }

   /**
   * Get responseDescription
   * @return responseDescription
  **/
  @Schema(description = "")
  public String getResponseDescription() {
    return responseDescription;
  }

  public void setResponseDescription(String responseDescription) {
    this.responseDescription = responseDescription;
  }

  public OrderChangeRequest status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @Schema(description = "")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public OrderChangeRequest uuid(String uuid) {
    this.uuid = uuid;
    return this;
  }

   /**
   * Get uuid
   * @return uuid
  **/
  @Schema(description = "")
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OrderChangeRequest orderChangeRequest = (OrderChangeRequest) o;
    return Objects.equals(this.approvedDate, orderChangeRequest.approvedDate) &&
        Objects.equals(this.changeReason, orderChangeRequest.changeReason) &&
        Objects.equals(this.changeRequest, orderChangeRequest.changeRequest) &&
        Objects.equals(this.changeRequestor, orderChangeRequest.changeRequestor) &&
        Objects.equals(this.href, orderChangeRequest.href) &&
        Objects.equals(this.id, orderChangeRequest.id) &&
        Objects.equals(this.requestDate, orderChangeRequest.requestDate) &&
        Objects.equals(this.responseDescription, orderChangeRequest.responseDescription) &&
        Objects.equals(this.status, orderChangeRequest.status) &&
        Objects.equals(this.uuid, orderChangeRequest.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(approvedDate, changeReason, changeRequest, changeRequestor, href, id, requestDate, responseDescription, status, uuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OrderChangeRequest {\n");
    
    sb.append("    approvedDate: ").append(toIndentedString(approvedDate)).append("\n");
    sb.append("    changeReason: ").append(toIndentedString(changeReason)).append("\n");
    sb.append("    changeRequest: ").append(toIndentedString(changeRequest)).append("\n");
    sb.append("    changeRequestor: ").append(toIndentedString(changeRequestor)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    requestDate: ").append(toIndentedString(requestDate)).append("\n");
    sb.append("    responseDescription: ").append(toIndentedString(responseDescription)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    uuid: ").append(toIndentedString(uuid)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
