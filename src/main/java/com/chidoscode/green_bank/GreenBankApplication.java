package com.chidoscode.green_bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
		title = "Green Chidozie's Bank App",
		description = "Backend APIs for Green's Bank",
		version = "1.0",
		contact = @Contact(
				name = "Chidozie Hamilton Green",
				email = "Chidogreen2003@gmail.com",
				url = "https://github.com/HeisGreen/Green_bank.git"
		),
		license = @License(
				name = "Chidoscode",
				url = "https://github.com/HeisGreen/Green_bank.git"
		)
),
externalDocs = @ExternalDocumentation(
		description = "Green Chidozie's Bank App Documentation",
		url = "Green Chidozie's Bank App"
))
public class GreenBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreenBankApplication.class, args);
	}

}
