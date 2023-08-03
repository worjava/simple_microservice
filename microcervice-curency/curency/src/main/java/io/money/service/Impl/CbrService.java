package io.money.service.Impl;


import com.google.common.cache.CacheBuilder;
import io.money.client.Impl.HttpCurrencyDateRateClientImpl;
import io.money.schema.ValCurs;
import com.google.common.cache.Cache;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toMap;

@Service
public class CbrService implements io.money.service.CbrService {

    private final HttpCurrencyDateRateClientImpl client;
    private final Cache<LocalDate, Map<String, BigDecimal>> cache; // кеширование данных.
    //ключ код валюты, и его котировки

    public CbrService(HttpCurrencyDateRateClientImpl client) {
        this.client = client;
        this.cache = CacheBuilder.newBuilder().build(); //иницилизация кеша в конструкторе
    }

    @Override
    public BigDecimal requestByCurrentCode(String code) { // запрос к серверу сохранение в кеш
        try {
            return cache.get(LocalDate.now(), this::callCurrentDate).get(code); // если данные в кеше свежие на сегодняшний день то вернет,
        } catch (
                ExecutionException e) {                                                //если нет то сделает снова запрос
            throw new RuntimeException("Ошибка при получении значения из кеша ", e);
        }
    }

    @Override
    public Map<String, BigDecimal> callCurrentDate() {  // получение данных текущих валют
        var xml = client.requestByDate(LocalDate.now()); // все котировки за эту дату
        ValCurs response = unmarshall(xml);
        return response.getValute().stream().collect(toMap(ValCurs.Valute::getCharCode, item ->
                parseWithLocale(item.getValue())));// получаем весь список валют и получаем карту с ключом значением
        //например USD это ключ а в нем хранится значение стоймость валюты


    }

    @Override
    public ValCurs unmarshall(String xml) {
        try (StringReader reader = new StringReader(xml)) {
            JAXBContext context = JAXBContext.newInstance(ValCurs.class);
            return (ValCurs) context.createUnmarshaller().unmarshal(reader);
        } catch (JAXBException e) {
            throw new RuntimeException(e); // Десериализация берем данные в формате JSON или XML и преобразуем их обратно в объекты Java
        }

    }

    @Override
    public BigDecimal parseWithLocale(String currency) { // преобразуем значение объекта для удобства валютном приложении чтобы можно было выполнять арифметические операции или другие расчеты итд
        try {
            double v = NumberFormat.getNumberInstance(Locale.getDefault()).parse(currency).
                    doubleValue();
            return BigDecimal.valueOf(v);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


    }


}
