package io.money.service;

import io.money.schema.ValCurs;

import java.math.BigDecimal;
import java.util.Map;

public interface CbrService {
     BigDecimal requestByCurrentCode(String code);

     Map<String,BigDecimal> callCurrentDate();

     ValCurs unmarshall(String xml);


     BigDecimal parseWithLocale(String currency);
}
