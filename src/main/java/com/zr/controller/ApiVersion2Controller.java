package com.zr.controller;

import com.zr.config.mvc.ApiVersion;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiVersion2Controller {

	@RequestMapping(value = "/apiVersion", method = {RequestMethod.POST})
	@ApiVersion(2)
	public String apiVersion() {
		return "I am v2!";
	}
}
