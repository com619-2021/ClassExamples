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
import java.util.ArrayList;
import java.util.List;
import solent.ac.uk.devops.traffic.client.swagger.model.Characteristic;
import solent.ac.uk.devops.traffic.client.swagger.model.Party;
/**
 * Resource
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-11-19T14:34:17.894448800Z[Europe/London]")
public class Resource {
  @JsonProperty("characteristics")
  private List<Characteristic> characteristics = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("href")
  private String href = null;

  @JsonProperty("id")
  private Long id = null;

  @JsonProperty("name")
  private String name = null;

  /**
   * Gets or Sets resourceController
   */
  public enum ResourceControllerEnum {
    EXTERNAL("EXTERNAL"),
    INTERNAL("INTERNAL");

    private String value;

    ResourceControllerEnum(String value) {
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
    public static ResourceControllerEnum fromValue(String text) {
      for (ResourceControllerEnum b : ResourceControllerEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

  }  @JsonProperty("resourceController")
  private ResourceControllerEnum resourceController = null;

  @JsonProperty("resourceOwner")
  private Party resourceOwner = null;

  @JsonProperty("resourceTypeName")
  private String resourceTypeName = null;

  @JsonProperty("uuid")
  private String uuid = null;

  public Resource characteristics(List<Characteristic> characteristics) {
    this.characteristics = characteristics;
    return this;
  }

  public Resource addCharacteristicsItem(Characteristic characteristicsItem) {
    if (this.characteristics == null) {
      this.characteristics = new ArrayList<>();
    }
    this.characteristics.add(characteristicsItem);
    return this;
  }

   /**
   * Get characteristics
   * @return characteristics
  **/
  @Schema(description = "")
  public List<Characteristic> getCharacteristics() {
    return characteristics;
  }

  public void setCharacteristics(List<Characteristic> characteristics) {
    this.characteristics = characteristics;
  }

  public Resource description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @Schema(description = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Resource href(String href) {
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

  public Resource id(Long id) {
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

  public Resource name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @Schema(description = "")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Resource resourceController(ResourceControllerEnum resourceController) {
    this.resourceController = resourceController;
    return this;
  }

   /**
   * Get resourceController
   * @return resourceController
  **/
  @Schema(description = "")
  public ResourceControllerEnum getResourceController() {
    return resourceController;
  }

  public void setResourceController(ResourceControllerEnum resourceController) {
    this.resourceController = resourceController;
  }

  public Resource resourceOwner(Party resourceOwner) {
    this.resourceOwner = resourceOwner;
    return this;
  }

   /**
   * Get resourceOwner
   * @return resourceOwner
  **/
  @Schema(description = "")
  public Party getResourceOwner() {
    return resourceOwner;
  }

  public void setResourceOwner(Party resourceOwner) {
    this.resourceOwner = resourceOwner;
  }

  public Resource resourceTypeName(String resourceTypeName) {
    this.resourceTypeName = resourceTypeName;
    return this;
  }

   /**
   * Get resourceTypeName
   * @return resourceTypeName
  **/
  @Schema(description = "")
  public String getResourceTypeName() {
    return resourceTypeName;
  }

  public void setResourceTypeName(String resourceTypeName) {
    this.resourceTypeName = resourceTypeName;
  }

  public Resource uuid(String uuid) {
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
    Resource resource = (Resource) o;
    return Objects.equals(this.characteristics, resource.characteristics) &&
        Objects.equals(this.description, resource.description) &&
        Objects.equals(this.href, resource.href) &&
        Objects.equals(this.id, resource.id) &&
        Objects.equals(this.name, resource.name) &&
        Objects.equals(this.resourceController, resource.resourceController) &&
        Objects.equals(this.resourceOwner, resource.resourceOwner) &&
        Objects.equals(this.resourceTypeName, resource.resourceTypeName) &&
        Objects.equals(this.uuid, resource.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characteristics, description, href, id, name, resourceController, resourceOwner, resourceTypeName, uuid);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Resource {\n");
    
    sb.append("    characteristics: ").append(toIndentedString(characteristics)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    resourceController: ").append(toIndentedString(resourceController)).append("\n");
    sb.append("    resourceOwner: ").append(toIndentedString(resourceOwner)).append("\n");
    sb.append("    resourceTypeName: ").append(toIndentedString(resourceTypeName)).append("\n");
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