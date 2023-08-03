package io.money.webapp.App.rest;

import io.money.service.Impl.CbrService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/currency")
public class CurrencyController {

    private final CbrService cbrService;

    public CurrencyController(CbrService cbrService) {
        this.cbrService = cbrService;
    }


    @GetMapping("/rate/{code}")
    public BigDecimal currencyRate(@PathVariable("code") String code) {

        return cbrService.requestByCurrentCode(code);

    }

}
