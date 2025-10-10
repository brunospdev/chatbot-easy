package com.wpp.wppbotmanager.service;

import org.springframework.stereotype.Service;

import com.wpp.wppbotmanager.client.UserBd;

@Service
public class UserService {

  private final UserBd userBd;

  public UserService(UserBd userBd) {
    this.userBd = userBd;
  }

  public String getUser() {
    return userBd.getUser();
  }
}
