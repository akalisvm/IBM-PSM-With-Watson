package ucl.ac.uk.ibmpsmwithwatson.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("ucl.ac.uk.ibmpsmwithwatson.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * Defining information of API webpage, claiming title and description for all APIs.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("IBM PSM With Watson")
                .description("IBM PSM With Watson Swagger API management")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
