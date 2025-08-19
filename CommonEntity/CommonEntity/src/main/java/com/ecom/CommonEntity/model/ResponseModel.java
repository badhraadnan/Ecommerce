package com.ecom.CommonEntity.model;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class ResponseModel implements Serializable {

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
