package com.freeperch.fond_analyzer.api;

import com.freeperch.fond_analyzer.dto.request.FundRequestDto;
import com.freeperch.fond_analyzer.dto.response.ClientData;
import com.freeperch.fond_analyzer.dto.response.ClientStatus;
import com.freeperch.fond_analyzer.dto.response.TianTianFundDetailDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
@RequestMapping("/fund")
public interface FundAnalyzeToolServiceApi {

    @PostMapping("/bond/analyze")
    ClientStatus bondAnalyze(@RequestBody FundRequestDto fundRequestDto);

    @PostMapping("/tiantian/hybridIndexStock/analyze")
    ClientData<List<TianTianFundDetailDto>> tiantianHybridIndexStockAnalyze(@RequestBody FundRequestDto fundRequestDto);

}
