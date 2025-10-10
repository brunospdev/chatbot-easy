package com.wpp.wppbotmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wpp.wppbotmanager.service.UserService;

@RestController
@RequestMapping("/usuario/luser")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public String getUser() {
    return userService.getUser();
  }
}
