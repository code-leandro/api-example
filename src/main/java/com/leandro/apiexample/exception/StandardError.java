package com.leandro.apiexample.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StandardError {

    private LocalDateTime time;
    private Integer status;
    private String error;
    private String path;
}
