package io.money.client;

import java.time.LocalDate;

public interface HttpCurrencyDateRateClient {
    String requestByDate(LocalDate date);

    String buildUrlRequest(String baseUrl, LocalDate date);
}
