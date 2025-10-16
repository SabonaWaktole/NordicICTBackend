package com.nordic.backend.company.Common.excetions;

public class ExpiredJWTException extends RuntimeException {

  public ExpiredJWTException(String message) {
    super(message);
  }
  
}
