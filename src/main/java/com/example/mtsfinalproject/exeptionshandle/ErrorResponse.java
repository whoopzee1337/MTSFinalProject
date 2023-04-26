package com.example.mtsfinalproject.exeptionshandle;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@RequiredArgsConstructor
@Accessors(chain = true)
public class ErrorResponse {

    private String code;

    private String message;
}
