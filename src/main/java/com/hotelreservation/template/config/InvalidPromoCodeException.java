package com.hotelreservation.template.config;

public class InvalidPromoCodeException extends RuntimeException {
  public InvalidPromoCodeException(String message) {
    super(message);
  }
}
