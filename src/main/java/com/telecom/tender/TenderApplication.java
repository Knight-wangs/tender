package com.telecom.tender;

import com.telecom.tender.config.GlobalCorsConfig;
import com.thetransactioncompany.cors.CORSFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableScheduling
public class TenderApplication {

	public static void main(String[] args) {
		SpringApplication.run(TenderApplication.class, args);
	}

}
