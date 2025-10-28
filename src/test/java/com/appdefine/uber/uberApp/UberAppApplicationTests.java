package com.appdefine.uber.uberApp;

import com.appdefine.uber.uberApp.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberAppApplicationTests {

	@Autowired
	private EmailSenderService emailSenderService;

	@Test
	void contextLoads() {
		emailSenderService.sendEmail(
				"gimok31307@ametitas.com",
				"Subject Email",
				"Body Email");
	}

	@Test
	void sendEmailMultiple(){
		String email[] = {
				"gimok31307@ametitas.com",
				"flexuswriter@gmail.com"
		};

		emailSenderService.sendEmail(
				email,
				"Subject Email 22",
				"Body Email 22");
	}

}
