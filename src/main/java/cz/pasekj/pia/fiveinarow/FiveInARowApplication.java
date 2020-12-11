package cz.pasekj.pia.fiveinarow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class FiveInARowApplication {

	public static void main(String[] args) {
		SpringApplication.run(FiveInARowApplication.class, args);
	}

}
