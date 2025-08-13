package com.ecom.CommonEntity.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ResponseModel {

    private int code;
    private String status;
    private Object data;
    private String massage;

    public ResponseModel(HttpStatus status,Object data,String massage){
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.data = data;
        this.massage = massage;
    }

}
