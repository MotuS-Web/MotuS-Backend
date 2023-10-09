package website.motus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MotusApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotusApplication.class, args);
	}

}
