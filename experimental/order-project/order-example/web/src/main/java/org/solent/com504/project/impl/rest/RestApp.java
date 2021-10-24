/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.solent.com504.project.impl.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;

@OpenAPIDefinition(
    tags = {
            @Tag(name="widget", description="Widget operations."),
            @Tag(name="gasket", description="Operations related to gaskets")
    },
    info = @Info(
        title="Example API",
        version = "1.0.1",
        contact = @Contact(
            name = "Example API Support",
            url = "http://exampleurl.com/contact",
            email = "techsupport@example.com"),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"))
)
@ApplicationPath("/rest")
public class RestApp extends ResourceConfig {

    // produces http://localhost:8080/project-web/rest/openapi.json 
    // see https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Getting-started
    public RestApp() {
        packages("org.solent.com504.project.impl.rest",
                "io.swagger.v3.jaxrs2.integration.resources"
        );
        // configureSwagger();
    }

    // swagger 1.5
    // see https://stackoverflow.com/questions/40480131/how-to-use-swagger-with-resourceconfig-in-jersey
//    private void configureSwagger() {
//        this.register(ApiListingResource.class);
//        this.register(SwaggerSerializers.class);
//        BeanConfig config = new BeanConfig();
//        config.setConfigId("spring-jaxrs-swagger");
//        config.setTitle("Spring Jersey jaxrs swagger integration");
//        config.setVersion("v1.0");
//        config.setBasePath("/swagger");
//        config.setResourcePackage("org.solent.com504.project.impl.rest");
//        config.setPrettyPrint(true);
//        config.setScan(true);
    // http://localhost:8080/project-web/rest/swagger/v1.0/swagger.json
    // http://localhost:8080/swagger/v1.0/swagger.json
    // }
}

// alternatve if using jaxrs directly
//public class RestApp extends Application {
//  public Set<Class<?>> getClasses() {
//    return new HashSet<Class<?>>(Arrays.asList(RestService.class));
//  }
//}
