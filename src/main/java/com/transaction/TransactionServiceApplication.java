package com.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;

import com.commons.security.DefaultSecurityConfig;
import com.commons.security.FeignTokenRelayConfig;

@SpringBootApplication(scanBasePackages = {"com.transaction", "com.transaction.mapper"})
@EnableFeignClients(basePackages = "com.transaction.client")
@Import({DefaultSecurityConfig.class, FeignTokenRelayConfig.class})
public class TransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

}
