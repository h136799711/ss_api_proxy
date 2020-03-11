package cn.hebidu.ss.apiproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiProxyApplication.class, args);
	}


	@Bean
	public SSProxyService proxyService() {
		return  new SSProxyService();
	}


	@Bean
	public TransportCryptor transportCryptor() {
		return  new Md5V3Cryptor();
	}

}
