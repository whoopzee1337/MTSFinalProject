package com.example.mtsfinalproject.wrappers;

import com.example.mtsfinalproject.exeptionshandle.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorWrap {
    private ErrorResponse error;
}
