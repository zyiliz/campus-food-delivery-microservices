package com.example.Service.Impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.Service.RecommendService;
import com.example.cache.DishCache;
import com.example.client.OrderClient;
import com.example.mapper.PreferenceMapper;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.entity.Preference;
import com.example.unit.RecommendationTools;
import com.example.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import org.springframework.core.io.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendServiceImpl implements RecommendService {
    private final DishCache dishCache;
    private final OrderClient orderClient;
    private final ChatClient chatClient;

    private final PreferenceMapper preferenceMapper;

    private final RecommendationTools recommendationTools;

    @Value("classpath:/prompts/recommend.st")
    private Resource recommendTemplate;


    @Override
    public Flux<String> getStreamingRecommendation(String query,String auth) {
        Long userId = UserContext.getId();
        List<Long> recommendList = orderClient.getRecommendList(auth, userId);
        String dishContext = "";
        if (recommendList != null && !recommendList.isEmpty()){
            List<DishPageVO> list = dishCache.getCommendList(recommendList);
            dishContext = list.stream()
                    .map(d->String.format("菜名：%s，描述：%s，价格：%.2f",d.getName(),d.getDescription(),d.getPrice()))
                    .collect(Collectors.joining("\n"));
        }
        LambdaQueryWrapper<Preference> lqw = new LambdaQueryWrapper<Preference>()
                .eq(Preference::getUserId,userId);
        Preference preference = preferenceMapper.selectOne(lqw);

        PromptTemplate promptTemplate = new PromptTemplate(recommendTemplate);
        String renderedPrompt = promptTemplate.render(Map.of(
                "userId", userId,
                "dishContext", dishContext,
                "userPreference",preference,
                "query", query
        ));
        Flux<String> statusFlux = Flux.just("[AI_STATUS]:正在为您翻阅今日菜单...\n\n");
        Flux<String> content = chatClient.prompt()
                .user(renderedPrompt)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, userId.toString()))
                .tools(recommendationTools)
                .stream()
                .content();
        return Flux.concat(statusFlux,content);
    }
}
