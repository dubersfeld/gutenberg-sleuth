package com.dub.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserFirstNameController {
	
	@RequestMapping("/firstname")
	public String getUserName() {
		
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return "Albert";
	}

}
