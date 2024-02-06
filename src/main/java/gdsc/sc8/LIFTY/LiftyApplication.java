package gdsc.sc8.LIFTY;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LiftyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiftyApplication.class, args);
	}

}
