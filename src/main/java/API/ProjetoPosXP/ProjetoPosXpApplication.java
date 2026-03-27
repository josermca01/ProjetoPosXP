package API.ProjetoPosXP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProjetoPosXpApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoPosXpApplication.class, args);
	}

}
