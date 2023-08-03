package io.money.client.Impl;

import io.money.client.HttpCurrencyDateRateClient;
import io.money.config.CurrencyClientCfg;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@AllArgsConstructor
public class HttpCurrencyDateRateClientImpl implements HttpCurrencyDateRateClient { // выполнение Http Запросы со стороны клиента

    public static final String DATE_PATTERN = "dd/MM/yyyy";
    private final DateTimeFormatter DATA_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private final CurrencyClientCfg clientCfg;


    @Override
    public String requestByDate(LocalDate date) {
        var baseUrl = clientCfg.getUrl();
        var httpClient = HttpClient.newHttpClient(); // создаем клиента
        var url = buildUrlRequest(baseUrl, date); // указываем юрл и дату на которую хотим получить курс
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();// данные запроса на сервер
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());// данные о курсах
            return response.body();// получаем тело в ввиде строки ответ с сервера
        } catch (IOException e) {
            throw new RuntimeException("Ошибка запроса", e);
        } catch (InterruptedException e) {
            throw new RuntimeException("Ошибка запроса", e);
        }

    }

    public String buildUrlRequest(String baseUrl, LocalDate date) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl).
                queryParam("data_req", DATA_TIME_FORMATTER.format(date)).
                build().toString();
    }

}
