package com.freeperch.fond_analyzer.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
@Data
@NoArgsConstructor
public class TianTianFundDetailDto {

    private String fundCode;
    private String fundName;
    private String fundType;

    private Double netValueChangeMonth1;
    private Double netValueChangeMonth3;
    private Double netValueChangeMonth6;
    private Double netValueChangeYear1;
    private Double netValueChangeYear2;
    private Double netValueChangeYear3;

    private Date fundDate;

    public TianTianFundDetailDto(String fundCode, String fundName, Double netValueChangeMonth1, Double netValueChangeMonth3, Double netValueChangeMonth6, Double netValueChangeYear1, Double netValueChangeYear2, Double netValueChangeYear3, Date fundDate) {
        this.fundCode = fundCode;
        this.fundName = fundName;
        this.netValueChangeMonth1 = netValueChangeMonth1;
        this.netValueChangeMonth3 = netValueChangeMonth3;
        this.netValueChangeMonth6 = netValueChangeMonth6;
        this.netValueChangeYear1 = netValueChangeYear1;
        this.netValueChangeYear2 = netValueChangeYear2;
        this.netValueChangeYear3 = netValueChangeYear3;
        this.fundDate = fundDate;
    }
}
