package com.appjishu.fpush.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class HomeController {
	@RequestMapping("")
	public String index() {
		return "Welcome to fpush application!";
	}
}
