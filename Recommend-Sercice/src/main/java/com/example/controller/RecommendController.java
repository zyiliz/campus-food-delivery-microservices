package com.example.controller;

import com.example.Service.RecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/recommend")
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping(value = "/recommend/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> recommend(@RequestParam("prompt")String prompt
                                    , @RequestHeader("auth")String auth){
        return recommendService.getStreamingRecommendation(prompt,auth);
    }
}
