package com.example.banquemisr.challenge05.enums;

import java.util.HashMap;
import java.util.Map;

public enum ErrorCode {
  BAD_REQUEST(400),
  INTERNAL_SERVER_ERROR(500),
  CONFLICT(409),
  UNAUTHORIZED(401),
  NOT_FOUND(404);

  private static final Map<Integer, ErrorCode> MY_MAP = new HashMap<>();

  static {
    for (ErrorCode myEnum : values()) {
      MY_MAP.put(myEnum.getValue(), myEnum);
    }
  }

  private int value;

  private ErrorCode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static ErrorCode getByValue(int value) {
    return MY_MAP.get(value);
  }
}