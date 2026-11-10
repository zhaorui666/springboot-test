package com.zr.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class StockBaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code = "123";

    private String name;

    private String marketvalue;

    private String pe;

    @JsonFormat(pattern = "yyyyMMdd")
    private Date currentDate;
}