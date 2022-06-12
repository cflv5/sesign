package tr.edu.yildiz.ce.sesign;

import java.security.Security;

import javax.annotation.PostConstruct;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "tr.edu.yildiz.ce.se.base", "tr.edu.yildiz.ce.sesign" })
public class SeSignApplication {
	public static void main(String[] args) {
		SpringApplication.run(SeSignApplication.class, args);
	}

	@PostConstruct
	void addSecurityProvider() {
		Security.addProvider(new BouncyCastleProvider());
	}
}
