package com.freeperch.fond_analyzer.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.freeperch.fond_analyzer.api.FundAnalyzeToolServiceApi;
import com.freeperch.fond_analyzer.dto.FundDetailDto;
import com.freeperch.fond_analyzer.dto.FundDetailDtoList;
import com.freeperch.fond_analyzer.dto.enums.RiskEnum;
import com.freeperch.fond_analyzer.dto.request.FundRequestDto;
import com.freeperch.fond_analyzer.dto.response.ClientData;
import com.freeperch.fond_analyzer.dto.response.ClientStatus;
import com.freeperch.fond_analyzer.dto.response.TianTianFundDetailDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Miles
 * @date 2020-06-12 14:37
 */
@RestController
@Slf4j
public class FundAnalyzeToolController implements FundAnalyzeToolServiceApi {

    @Autowired
    private RestTemplate restTemplate;

    private String dataUrl = "https://www.lixinger.com/api/analyt/fund/funds/list";

    private Integer pageSize = 100;

    String formatStr = "%-2d: stockCode: %-7s  m6: %-2.4f  y1: %-2.4f  asset-scale: %-15.4f  fundType: %-15s  name: %-8s\n";

    String tiantianFormatStr = "%-2d: stockCode: %-7s  m3: %-2.4f  m6: %-2.4f  y1: %-2.4f  y2: %-2.4f y3: %-2.4f  fundDate: %-10s  fundType: %-3s  name: %-8s\n";

    private String tiantianFundUrl = "http://fund.eastmoney.com/data/rankhandler.aspx?op=ph&dt=kf&ft={fundType}&sc={orderBy}&st=desc&pi={page}&pn={pageSize}&dx=1";

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Override
    public ClientStatus bondAnalyze(@RequestBody FundRequestDto fundRequestDto) {
        printTianTian(getTianTianTopData(fundRequestDto));
        return ClientStatus.ok();
    }

    @Override
    public ClientData<List<TianTianFundDetailDto>> tiantianHybridIndexStockAnalyze(FundRequestDto fundRequestDto) {
        fundRequestDto.setFundType("gp");
        List<TianTianFundDetailDto> gpTopData = getTianTianTopData(fundRequestDto);
        fundRequestDto.setFundType("hh");
        List<TianTianFundDetailDto> hhTopData = getTianTianTopData(fundRequestDto);
        fundRequestDto.setFundType("zs");
        List<TianTianFundDetailDto> zsTopData = getTianTianTopData(fundRequestDto);
        List<TianTianFundDetailDto> fundDetailDtos = new ArrayList<>();
        fundDetailDtos.addAll(gpTopData);
        fundDetailDtos.addAll(hhTopData);
        fundDetailDtos.addAll(zsTopData);
        fundDetailDtos = fundDetailDtos.stream().sorted((i1, i2) -> {
            double value = i2.getNetValueChangeMonth6() - i1.getNetValueChangeMonth6();
            if (value < 0) {
                return -1;
            } else if (value == 0) {
                return 0;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
        printTianTian(fundDetailDtos);
        return ClientData.ok(fundDetailDtos);
    }

    private Map<String, FundDetailDto> getData(FundRequestDto fundRequestDto, Double topPercent, String sortName) {
        Map<String, FundDetailDto> dataMap = new HashMap<>(16);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("cookie", fundRequestDto.getCookie());
        int maxPage = 1;
        for (int page = 0; page <= maxPage; page++) {
            List<String> exchanges = Arrays.asList("sh", "sz", "jj");
            Map<String, Object> requestBody = new HashMap<>(16);
            requestBody.put("fundSecondLevel", fundRequestDto.getFundType());
            requestBody.put("exchanges", exchanges);
            requestBody.put("pageIndex", page);
            requestBody.put("pageSize", pageSize);
            requestBody.put("sortName", sortName);
            requestBody.put("sortOrder", "desc");

            if (fundRequestDto.getFundType().equals("stock")) {
                requestBody.put("fundFlags", fundRequestDto.getFundFlags());
            }

            HttpEntity<Map> requestEntity = new HttpEntity<>(requestBody, requestHeaders);

            ResponseEntity<String> fundResponse = restTemplate.postForEntity(dataUrl, requestEntity, String.class);
            FundDetailDtoList fundDetailDtoList = JSONObject.parseObject(fundResponse.getBody(), FundDetailDtoList.class);
            Map<String, FundDetailDto> pageM1Map = fundDetailDtoList.getList()
                .stream()
                .collect(Collectors.toMap(FundDetailDto::getStockCode, item -> item));
            dataMap.putAll(pageM1Map);
            if (page == 0) {
                maxPage = (int) ((fundDetailDtoList.getTotal() * topPercent) / pageSize);
            }
        }
        return dataMap;
    }


    private List<TianTianFundDetailDto> getTianTianTopData(FundRequestDto fundRequestDto) {
        Map<String, TianTianFundDetailDto> m1Map = getTianTianData(fundRequestDto, fundRequestDto.getSixMonthTopPercent(), "1yzf");
        Map<String, TianTianFundDetailDto> m3Map = getTianTianData(fundRequestDto, fundRequestDto.getSixMonthTopPercent(), "3yzf");
        Map<String, TianTianFundDetailDto> m6Map = getTianTianData(fundRequestDto, fundRequestDto.getSixMonthTopPercent(), "6yzf");
        Map<String, TianTianFundDetailDto> y1Map = getTianTianData(fundRequestDto, fundRequestDto.getOneYearTopPercent(), "1nzf");
        Map<String, TianTianFundDetailDto> y2Map = getTianTianData(fundRequestDto, fundRequestDto.getOneYearTopPercent(), "2nzf");
        Map<String, TianTianFundDetailDto> y3Map = getTianTianData(fundRequestDto, fundRequestDto.getThreeYearTopPercent(), "3nzf");

        Set<String> m1Keys = m1Map.keySet();
        List<TianTianFundDetailDto> finalData = new ArrayList<>(10);
        for (String m1Key : m1Keys) {
            boolean isQualified = m3Map.containsKey(m1Key)
                && m6Map.containsKey(m1Key)
                && y1Map.containsKey(m1Key)
                && y2Map.containsKey(m1Key)
                && y3Map.containsKey(m1Key);
            if (isQualified) {
                finalData.add(m1Map.get(m1Key));
            }
        }
        List<TianTianFundDetailDto> orderByM3 = finalData.stream().sorted((i1, i2) -> {
            double value = i2.getNetValueChangeMonth3() - i1.getNetValueChangeMonth3();
            if (value < 0) {
                return -1;
            } else if (value == 0) {
                return 0;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
        List<TianTianFundDetailDto> orderByM6 = finalData.stream().sorted((i1, i2) -> {
            double value = i2.getNetValueChangeMonth6() - i1.getNetValueChangeMonth6();
            if (value < 0) {
                return -1;
            } else if (value == 0) {
                return 0;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
        List<TianTianFundDetailDto> orderByY1 = finalData.stream().sorted((i1, i2) -> {
            double value = i2.getNetValueChangeYear1() - i1.getNetValueChangeYear1();
            if (value < 0) {
                return -1;
            } else if (value == 0) {
                return 0;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());
        orderByM3 = orderByM3.subList(0, orderByM3.size() / 2);
        orderByM6 = orderByM6.subList(0, orderByM6.size() / 2);
        orderByY1 = orderByY1.subList(0, orderByY1.size() / 2);

        List<TianTianFundDetailDto> fundDetailDtos = new ArrayList<>();

        for (TianTianFundDetailDto tianTianFundDetailDto : orderByM3) {
            if (orderByM6.contains(tianTianFundDetailDto) && orderByY1.contains(tianTianFundDetailDto)) {
                tianTianFundDetailDto.setFundType(fundRequestDto.getFundType());
                boolean isQualified = tianTianFundDetailDto.getNetValueChangeYear1() > tianTianFundDetailDto.getNetValueChangeMonth6()
                    && tianTianFundDetailDto.getNetValueChangeMonth6() > tianTianFundDetailDto.getNetValueChangeMonth3()
                    && tianTianFundDetailDto.getNetValueChangeMonth3() > tianTianFundDetailDto.getNetValueChangeMonth1();
                if (RiskEnum.LOW.equals(fundRequestDto.getRisk())) {
                    Calendar fiveYearsAgo = Calendar.getInstance();
                    fiveYearsAgo.add(Calendar.YEAR, -5);
                    Date fiveYearsAgoTime = fiveYearsAgo.getTime();
                    isQualified = isQualified
                        && (tianTianFundDetailDto.getNetValueChangeYear3() != null && tianTianFundDetailDto.getNetValueChangeYear2() != null)
                        && tianTianFundDetailDto.getNetValueChangeYear3() > tianTianFundDetailDto.getNetValueChangeYear2()
                        && tianTianFundDetailDto.getNetValueChangeYear2() > tianTianFundDetailDto.getNetValueChangeYear1()
                        && tianTianFundDetailDto.getFundDate().before(fiveYearsAgoTime);
                } else if (RiskEnum.HIGH.equals(fundRequestDto.getRisk())) {
                    Calendar threeYearsAgo = Calendar.getInstance();
                    threeYearsAgo.add(Calendar.YEAR, -3);
                    Date threeYearsAgoTime = threeYearsAgo.getTime();
                    isQualified = isQualified && tianTianFundDetailDto.getFundDate().before(threeYearsAgoTime);
                }
                if (isQualified) {
                    fundDetailDtos.add(tianTianFundDetailDto);
                }
            }
        }
        fundDetailDtos = fundDetailDtos.stream().sorted((i1, i2) -> {
            double value = i2.getNetValueChangeMonth6() - i1.getNetValueChangeMonth6();
            if (value < 0) {
                return -1;
            } else if (value == 0) {
                return 0;
            } else {
                return 1;
            }
        }).collect(Collectors.toList());

        return fundDetailDtos;
    }

    private Map<String, TianTianFundDetailDto> getTianTianData(FundRequestDto fundRequestDto, Double topPercent, String orderBy) {
        Map<String, TianTianFundDetailDto> dataMap = new HashMap<>(16);
        //设置请求头参数
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Referer", "http://fund.eastmoney.com/data/fundranking.html");
        HttpEntity request = new HttpEntity(requestHeaders);

        int maxPage = 1;
        for (int page = 0; page <= maxPage; page++) {
            //设置get请求参数
            Map<String, String> params = new HashMap<>(16);
            params.put("fundType", fundRequestDto.getFundType());
            params.put("orderBy", orderBy);
            params.put("page", String.valueOf(page));
            params.put("pageSize", String.valueOf(pageSize));

            ResponseEntity<String> exchange = restTemplate.exchange(tiantianFundUrl, HttpMethod.GET, request, String.class, params);
            String body = exchange.getBody();
            body = body.substring(15);
            body = ((body.substring(0, (body.indexOf(",allNum:")))) + "}")
                .replace("datas", "\"datas\"")
                .replace("allRecords", "\"allRecords\"")
                .replace("pageIndex", "\"pageIndex\"")
                .replace("pageNum", "\"pageNum\"")
                .replace("allPages", "\"allPages\"");
            JSONObject jsonObject = JSON.parseObject(body);
            JSONArray dataArray = jsonObject.getJSONArray("datas");
            Integer allPages = (Integer) jsonObject.get("allPages");
            for (int i = 0; i < dataArray.size(); i++) {
                String oneFund = (String) dataArray.get(i);
                String[] oneFundElements = oneFund.split(",");
                String fundCode = oneFundElements[0];

                TianTianFundDetailDto tianTianFundDetailDto = null;
                try {
                    tianTianFundDetailDto = new TianTianFundDetailDto(fundCode,
                        oneFundElements[1],
                        StringUtils.isBlank(oneFundElements[8]) ? null : Double.valueOf(oneFundElements[8]),
                        StringUtils.isBlank(oneFundElements[9]) ? null : Double.valueOf(oneFundElements[9]),
                        StringUtils.isBlank(oneFundElements[10]) ? null : Double.valueOf(oneFundElements[10]),
                        StringUtils.isBlank(oneFundElements[11]) ? null : Double.valueOf(oneFundElements[11]),
                        StringUtils.isBlank(oneFundElements[12]) ? null : Double.valueOf(oneFundElements[12]),
                        StringUtils.isBlank(oneFundElements[13]) ? null : Double.valueOf(oneFundElements[13]),
                        dateFormat.parse(oneFundElements[16]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dataMap.put(fundCode, tianTianFundDetailDto);
            }
            if (page == 0) {
                maxPage = (int) (allPages * topPercent);
            }
        }
        return dataMap;
    }

    void printTianTian(List<TianTianFundDetailDto> fundDetailDtos) {
        System.out.println("\n-------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < fundDetailDtos.size(); i++) {
            TianTianFundDetailDto fundDetailDto = fundDetailDtos.get(i);
            String dataStr = dateFormat.format(fundDetailDto.getFundDate());
            System.out.printf(tiantianFormatStr,
                i,
                fundDetailDto.getFundCode(),
                fundDetailDto.getNetValueChangeMonth3(),
                fundDetailDto.getNetValueChangeMonth6(),
                fundDetailDto.getNetValueChangeYear1(),
                fundDetailDto.getNetValueChangeYear2(),
                fundDetailDto.getNetValueChangeYear3(),
                dataStr,
                fundDetailDto.getFundType(),
                fundDetailDto.getFundName());
        }
        System.out.println("-------------------------------------------------------------------------------------------------------");
    }
}
