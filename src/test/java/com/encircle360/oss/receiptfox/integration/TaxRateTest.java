package com.encircle360.oss.receiptfox.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.receiptfox.AbstractTest;
import com.encircle360.oss.receiptfox.dto.TaxRateDTO;
import com.encircle360.oss.receiptfox.dto.api.CreateUpdateTaxRateDTO;

@SpringBootTest
public class TaxRateTest extends AbstractTest {

    private final static String URL = "/tax-rates";

    @Test
    public void test_crud() throws Exception {
        CreateUpdateTaxRateDTO createUpdateTaxRateDTO = CreateUpdateTaxRateDTO.builder().build();
        post(URL, createUpdateTaxRateDTO, status().isBadRequest());

        createUpdateTaxRateDTO = CreateUpdateTaxRateDTO
            .builder()
            .rate(BigDecimal.valueOf(0.19))
            .name("German tax")
            .build();

        MvcResult createdResult = post(URL, createUpdateTaxRateDTO, status().isCreated());
        TaxRateDTO taxRateDTO = mapResultToObject(createdResult, TaxRateDTO.class);

        Assertions.assertEquals(createUpdateTaxRateDTO.getRate(), taxRateDTO.getRate());
        Assertions.assertEquals(createUpdateTaxRateDTO.getName(), taxRateDTO.getName());

        CreateUpdateTaxRateDTO updateTaxRateDTO = CreateUpdateTaxRateDTO.builder()
            .name("asdf")
            .rate(BigDecimal.ZERO)
            .build();
        put(URL + "/" + taxRateDTO.getId(), updateTaxRateDTO, status().isMethodNotAllowed());

        MvcResult getTaxRateResult = get(URL + "/" + taxRateDTO.getId(), status().isOk());
        TaxRateDTO getTaxRateDTO = mapResultToObject(getTaxRateResult, TaxRateDTO.class);

        Assertions.assertEquals(getTaxRateDTO.getRate(), taxRateDTO.getRate());
        Assertions.assertEquals(getTaxRateDTO.getName(), taxRateDTO.getName());

        delete(URL + "/" + taxRateDTO.getId(), status().isNoContent());
        delete(URL + "/" + taxRateDTO.getId(), status().isNotFound());
        get(URL + "/" + taxRateDTO.getId(), status().isNotFound());
    }
}
