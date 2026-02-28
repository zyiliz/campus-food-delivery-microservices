package com.example.Service;

import com.example.pojo.VO.DishPageVO;
import com.example.result.Result;
import reactor.core.publisher.Flux;

import java.util.List;

public interface RecommendService {
    Flux<String> getStreamingRecommendation(String query,String auth);

}
