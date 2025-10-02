package com.ecom.CommonEntity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class pageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String status;
    private Object data;
    private int pageNumber;
    private int pageSize;
    private String massage;
    private long getTotalElements;
    private int getTotalPages;

    public pageModel(HttpStatus status, Object data, String massage) {
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.data = data;
        this.massage = massage;

    }

    public pageModel(HttpStatus status, Object data, String massage, int pageNumber, int pageSize,long getTotalElements,int getTotalPages) {
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.data = data;
        this.massage = massage;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.getTotalElements = getTotalElements;
        this.getTotalPages = getTotalPages;

    }

}
