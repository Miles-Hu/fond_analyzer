package com.freeperch.fond_analyzer;


import com.freeperch.fond_analyzer.dto.enums.RiskEnum;
import com.freeperch.fond_analyzer.dto.request.FundRequestDto;
import org.junit.Test;


/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
public class FundAnalyzeToolTests extends BaseTests {

    @Test
    /**
     *
     */
    public void bondFundLowRisk() throws Exception {
        long start = System.currentTimeMillis();
        FundRequestDto lowRiskFundRequestDto = getLowRiskFundRequestDto(false);
        lowRiskFundRequestDto.setFundType("zq");
        executeTest("/fund/bond/analyze", lowRiskFundRequestDto);
        System.out.printf("\nTime elapse: %d(s)\n", (System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void tiantianStockHybridStockFundLowRisk() throws Exception {
        long start = System.currentTimeMillis();
        FundRequestDto lowRiskFundRequestDto = getLowRiskFundRequestDto(false);
        executeTestWithResponse("/fund/tiantian/hybridIndexStock/analyze", lowRiskFundRequestDto);
        System.out.printf("\nTime elapse: %d(s)\n", (System.currentTimeMillis() - start) / 1000);
    }

    @Test
    public void tiantianStockHybridStockFundHighRisk() throws Exception {
        long start = System.currentTimeMillis();
        FundRequestDto highRiskRequestDto = getHighRiskRequestDto(false);
        executeTestWithResponse("/fund/tiantian/hybridIndexStock/analyze", highRiskRequestDto);
        System.out.printf("\nTime elapse: %d(s)\n", (System.currentTimeMillis() - start) / 1000);
    }


    private FundRequestDto getLowRiskFundRequestDto(boolean isCheck) {
        FundRequestDto fundRequestDto = new FundRequestDto();
        fundRequestDto.setRisk(RiskEnum.LOW);
        if (isCheck) {
            fundRequestDto.setSixMonthTopPercent(0.4);
            fundRequestDto.setOneYearTopPercent(0.4);
            fundRequestDto.setThreeYearTopPercent(0.4);
            fundRequestDto.setFiveYearTopPercent(0.4);
        } else {
            fundRequestDto.setSixMonthTopPercent(0.4);
            fundRequestDto.setOneYearTopPercent(0.4);
            fundRequestDto.setThreeYearTopPercent(0.8);
            fundRequestDto.setFiveYearTopPercent(0.8);
        }
        return fundRequestDto;
    }


    private FundRequestDto getHighRiskRequestDto(boolean isCheck) {
        FundRequestDto fundRequestDto = new FundRequestDto();
        fundRequestDto.setRisk(RiskEnum.HIGH);
        if (isCheck) {
            fundRequestDto.setSixMonthTopPercent(0.2);
            fundRequestDto.setOneYearTopPercent(0.2);
            fundRequestDto.setThreeYearTopPercent(0.2);
        } else {
            fundRequestDto.setSixMonthTopPercent(0.1);
            fundRequestDto.setOneYearTopPercent(0.1);
            fundRequestDto.setThreeYearTopPercent(0.2);
        }
        return fundRequestDto;
    }
}
