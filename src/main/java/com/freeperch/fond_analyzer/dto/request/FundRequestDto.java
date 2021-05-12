package com.freeperch.fond_analyzer.dto.request;

import com.freeperch.fond_analyzer.dto.enums.RiskEnum;
import lombok.Data;

import java.util.List;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
@Data
public class FundRequestDto {

    private String cookie;

    private Double sixMonthTopPercent;

    private Double oneYearTopPercent;

    private Double twoYearTopPercent;

    private Double threeYearTopPercent;

    private Double fiveYearTopPercent;

    private String fundType;

    private String stockCode;

    private RiskEnum risk;

    private List<String> fundFlags;

}
