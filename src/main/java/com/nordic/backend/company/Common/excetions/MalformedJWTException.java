package com.nordic.backend.company.Common.excetions;

public class MalformedJWTException extends RuntimeException {

  public MalformedJWTException(String message) {
    super(message);
  }
}
