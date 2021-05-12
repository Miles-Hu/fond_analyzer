package com.freeperch.fond_analyzer.dto;

import lombok.Data;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
@Data
public class FundDetailDto {

    private String groupId;
    private String latest_nv_date;
    private String latest_price_date;
    private String inception_date;
    private String asset_scale_date;
    private String name;
    private String shortName;
    private String manager;
    private String custody;
    private String areaCode;
    private String stockType;
    private String exchange;
    private String stockCode;
    private String fundSecondLevel;
    private String status;
    private String fundType;

    private Long stockId;
    private Double nv;
    private Double total_nv;
    private Double latest_price;
    private Double price_change_r;
    private Double asset_scale;
    private Double nv_change_r;
    private Double nv_change_r_m1;
    private Double nv_change_r_m3;
    private Double nv_change_r_m6;
    private Double nv_change_r_y1;
    private Double nv_change_r_y3;
    private Double nv_change_r_y5;
    private Double discount_r;
    private Double estimated_nv;

    private Long _id;
    private Long tickerId;
    private Long turnover;
    private Long followedNum;
    private Long companyId;
    private Long indexId;
    private Long indexFundFlag;
    private Long classificationFlag;
    private CompanyFund company;
}
