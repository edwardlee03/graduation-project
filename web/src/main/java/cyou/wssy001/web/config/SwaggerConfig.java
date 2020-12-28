package cyou.wssy001.web.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @ProjectName: graduation-project
 * @ClassName: SwaggerConfig
 * @Description:
 * @Author: alexpetertyler
 * @Date: 2020/9/4
 * @Version v1.0
 */
@Configuration
@EnableSwagger2WebMvc
@RequiredArgsConstructor
public class SwaggerConfig implements WebMvcConfigurer {
    private final Environment environment;
//    private boolean b = StrUtil.containsAnyIgnoreCase(environment.getActiveProfiles()[0], "dev", "test");

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
//                .enable(b)
                .groupName("测试")
                .select()
                .apis(RequestHandlerSelectors.basePackage("cyou.wssy001.web.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("Tyler", "", "");
        return new ApiInfoBuilder()
                .contact(contact)
                .title("宜搜 API Doc")
                .description("用了宜搜没有不说好的🤣")
                .version("v1.0")
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
