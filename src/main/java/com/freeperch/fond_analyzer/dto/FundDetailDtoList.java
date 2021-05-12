package com.freeperch.fond_analyzer.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
@Data
public class FundDetailDtoList {

    private List<FundDetailDto> list;

    private Long total;

}
