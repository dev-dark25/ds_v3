package com.example.module.service.impl;

import com.example.module.config.DeepSeekConfig;
import com.example.module.entity.DeepSeekRequest;
import com.example.module.entity.DeepSeekResponse;
import com.example.module.service.DeepSeekService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    @Value("${deepseek.api.url}")
    public String apiUrl;

    @Value("${deepseek.api.key}")
    public String apiKey;

    private final RestTemplate restTemplate;

    private HttpHeaders headers;

    public DeepSeekServiceImpl() {
        this.restTemplate = new RestTemplate();
        this.headers = new HttpHeaders();
        this.headers.set("Content-Type", "application/json");
    }

    public Map callDeepSeekApi(Map req) {

        String model = req.get("model").toString();
        if (DeepSeekConfig.LOCAL.equals(model)) {
            return callLocalDeepSeekApi(req);
        }

        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel(model);

        Object[] oa = null;
        if (DeepSeekConfig.V3.equals(model)) {
            oa = new Object[]{
                    new HashMap<String, String>() {{
                        put("role", "user");
                        put("content", req.get("input").toString());
                    }},
                    new HashMap<String, String>() {{
                        put("role", "system");
                        put("content", req.get("systemInput") != null ? req.get("systemInput").toString() : "");
                    }}};
        } else if (DeepSeekConfig.R1.equals(model)) {
            oa = new Object[]{
                    new HashMap<String, String>() {{
                        put("role", "user");
                        put("content", req.get("input").toString());
                    }}
//                    new HashMap<String, String>() {{    // r1只支持role为user的请求，使用r1注释
//                        put("role", "system");
//                        put("content", input);
            };
        } else {
            oa = new Object[]{};
        }

        request.setMessages(oa);
        headers.set("Authorization", "Bearer " + apiKey);
        HttpEntity<DeepSeekRequest> entity = new HttpEntity<>(request, headers);

        Map result = new HashMap();
        try {
            ResponseEntity<DeepSeekResponse> response = restTemplate.postForEntity(apiUrl, entity, DeepSeekResponse.class);
            result.put("code", "000");
            result.put("output", response.getBody().getResult());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
        }
        return result;
    }

    public Map callLocalDeepSeekApi(Map req) {

        DeepSeekRequest request = new DeepSeekRequest();
        request.setModel(DeepSeekConfig.MODEL);
        request.setStream(false);

        Object[] oa = new Object[]{
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", req.get("input").toString());
                }}
        };
        request.setMessages(oa);
        HttpEntity<DeepSeekRequest> entity = new HttpEntity<>(request, headers);

        Map result = new HashMap();
        try {
            ResponseEntity<DeepSeekResponse> response = restTemplate.postForEntity(DeepSeekConfig.LOCAL_URL, entity, DeepSeekResponse.class);
            result.put("code", "000");
            result.put("output", response.getBody().getMessage().get("content"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            result.put("code", "400");
            result.put("output", e.getMessage());
        }
        return result;
    }

}
