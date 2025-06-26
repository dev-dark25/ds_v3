package com.example.module.controller;

import com.example.module.service.DeepSeekService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class WebController {

    @Resource
    private DeepSeekService deepSeekService;

    @PostMapping("/call")
    public Map callApi(@RequestBody Map<String, Object> req) {
        System.out.println("input: " + req.get("input"));
        System.out.println("model: " + req.get("model"));
        Map result = deepSeekService.callDeepSeekApi(req);
        System.out.println(result);
        return result;
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Map test(@RequestBody String body) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "200");
        return map;
    }

}
