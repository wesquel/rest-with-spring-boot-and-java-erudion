package br.com.wesquel.restwithspringbootandjavaerudion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class RestWithSpringBootAndJavaErudionApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestWithSpringBootAndJavaErudionApplication.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String result = encoder.encode("admin123");
        
        System.out.println("My hash: " + result);
	}

}
