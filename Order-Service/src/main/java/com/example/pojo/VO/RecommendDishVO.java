package com.example.pojo.VO;

import com.example.pojo.DTO.DishFrequencyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendDishVO {
    private List<DishFrequencyDTO> dishFrequencyDTOS;
    private List<Long> recommendDishId;
}
