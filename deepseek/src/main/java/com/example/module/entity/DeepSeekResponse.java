package com.example.module.entity;

import lombok.Data;

import java.util.Map;

@Data
public class DeepSeekResponse {
    private String result;
    private String model;
    private String created_at;
    private Map message;
}
