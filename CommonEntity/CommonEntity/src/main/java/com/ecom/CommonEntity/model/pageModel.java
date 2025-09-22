package com.ecom.CommonEntity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class pageModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String status;
    private Object data;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int pageNumber;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int pageSize;
    private String massage;

    public pageModel(HttpStatus status, Object data, String massage) {
        this(status, data, massage, 0, 0);
    }

    // With pagination
    public pageModel(HttpStatus status, Object data, String massage, int pageNumber, int pageSize) {
        this.code = status.value();
        this.status = status.getReasonPhrase();
        this.data = data;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.massage = massage;
    }

}
