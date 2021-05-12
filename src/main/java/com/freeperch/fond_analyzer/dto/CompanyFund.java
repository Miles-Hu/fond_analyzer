package com.freeperch.fond_analyzer.dto;

import lombok.Data;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
@Data
public class CompanyFund {

    private String name;
    private String stockCode;
    private String exchange;

    private Long tickerId;
}
