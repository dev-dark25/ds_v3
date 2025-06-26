package com.example.module.service;

import java.util.Map;

public interface DeepSeekService {

    Map callDeepSeekApi(Map req);

    Map callLocalDeepSeekApi(Map req);
}
