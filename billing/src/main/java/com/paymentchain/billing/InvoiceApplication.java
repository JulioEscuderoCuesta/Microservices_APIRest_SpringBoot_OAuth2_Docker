package com.paymentchain.billing;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class InvoiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoiceApplication.class, args);
	}
        
        // El @Bean sirve para inyectar este método en otro punto de la aplicación. No se necesitará instanciación.
        @Bean
        public OpenAPI customerOpenAPI() {
            return new OpenAPI()
                    .components(new Components())
                    .info(new Info()
                        .title("Billing API")
                        .version("1.0.0"));
        }
}
