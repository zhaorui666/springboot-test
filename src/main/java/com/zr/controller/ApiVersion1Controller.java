package com.zr.controller;

import com.zr.config.mvc.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiVersion1Controller {

	@ApiVersion(1)
	@RequestMapping("/apiVersion")
	public String apiVersion() {
		return "I am v1!";
	}
}