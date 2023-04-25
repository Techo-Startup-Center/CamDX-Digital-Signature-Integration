package kh.gov.camdx.disig.wrapperservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"kh.gov.camdx.disig.wrapperservice",
		"kh.gov.camdx.disig.lib"
})
public class WrapperServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WrapperServiceApplication.class, args);
	}

}
