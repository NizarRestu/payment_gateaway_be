package com.payment.gateaway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {
    public static <T> CommonResponse<T> ok(T data) {
        CommonResponse<T> response = new CommonResponse<T>();
        response.setMessage("success");
        response.setStatus("200 OK");
        response.setCode(200);
        response.setData(data);
        return response;
    }

    public static <T> ResponseEntity<CommonResponse<T>> error(String error, HttpStatus httpStatus) {
        String code = String.valueOf(httpStatus.value());
        CommonResponse<T> response = new CommonResponse<>();
        response.setStatus(code + " ERROR");
        response.setCode(httpStatus.value());
        response.setMessage(httpStatus.name());
        response.setData((T) error);
        return new ResponseEntity<>(response, httpStatus);
    }

}
