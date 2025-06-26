package com.example.module.entity;

import lombok.Data;

@Data
public class DeepSeekRequest {
    private String model;
    private Object[] messages;
    private boolean stream;
}
