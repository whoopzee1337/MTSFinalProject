package com.example.mtsfinalproject.wrappers;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderIdWrap {

    private UUID orderId;
}
