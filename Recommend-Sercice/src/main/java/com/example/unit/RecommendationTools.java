package com.example.unit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cache.DishCache;
import com.example.client.DishClient;
import com.example.mapper.PreferenceMapper;
import com.example.pojo.VO.DishPageVO;
import com.example.pojo.entity.Preference;
import com.example.result.Result;
import com.example.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendationTools {

    private final DishCache dishCache;

    private final DishClient dishClient;

    private final PreferenceMapper preferenceMapper;



    @Tool(description = "当用户提到特定分类（如川菜、火锅）或预算限制时调用。参数 categoryName 映射为分类名，price 为最高单价上限。")
    public List<DishPageVO> getDishListByCategoryOrName(String categoryName, BigDecimal price){
            String auth = UserContext.getAuth();
        Result<List<Long>> recommend = dishClient.getRecommend(auth, categoryName, price);
        if (recommend == null || recommend.getData() == null){return Collections.emptyList();};
        return dishCache.getCommendList(recommend.getData());
    }

    @Tool(description = "当发现用户提到长期的口味偏好、忌口或特定预算习惯时调用，我会永远记住这些细节。")
    public String saveUserHabit(String habitSummary){
        Long userId = UserContext.getId();
        new Preference();
        Preference preference = Preference.builder()
                .userId(userId)
                .preferenceSummary(habitSummary)
                .updatedAt(LocalDateTime.now())
                .build();
        boolean insert = preferenceMapper.insertOrUpdate(preference);
        return "已记下偏好" +habitSummary;
    }

    

}
