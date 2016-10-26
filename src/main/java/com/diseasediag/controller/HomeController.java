package com.diseasediag.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author HARSHBHAVSAR (hhb140330@utdallas.edu)
 */

@Controller
public class HomeController {

	
	@RequestMapping(value = { "/", "" })
	public String index() {
		return "index";
	}
	
	
	@RequestMapping(value = { "/login", "" })
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = { "/home", "" })
	public String secured() {
		return "home";
	}


	/**
	 * If something went wrong during the entire process.
	 * 
	 * @param e
	 * @return
	 */
	@RequestMapping(value = { "/error", "" })
	@ExceptionHandler(Exception.class)
	public String hadnleException(Exception e) {
		e.printStackTrace();
		return "index";
	}
}
