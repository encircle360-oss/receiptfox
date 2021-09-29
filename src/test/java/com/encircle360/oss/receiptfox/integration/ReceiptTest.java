package com.encircle360.oss.receiptfox.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.receiptfox.AbstractTest;
import com.encircle360.oss.receiptfox.client.docsrabbit.TemplateClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.template.TemplateDTO;
import com.encircle360.oss.receiptfox.dto.TaxRateDTO;
import com.encircle360.oss.receiptfox.dto.api.CreateUpdateTaxRateDTO;
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.OrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.api.CreateUpdateOrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;
import com.encircle360.oss.receiptfox.dto.receipt.UnitDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptDTO;
import com.encircle360.oss.receiptfox.dto.receipt.api.CreateUpdateReceiptPositionDTO;

@SpringBootTest
public class ReceiptTest extends AbstractTest {

    private final static String URL = "/receipts";

    @Autowired
    private TemplateClient templateClient;

    @Test
    public void test_crud() throws Exception {
        CreateUpdateReceiptDTO createUpdateReceiptDTO = CreateUpdateReceiptDTO.builder().build();
        post(URL, createUpdateReceiptDTO, status().isBadRequest());

        TemplateDTO templateDTO = createTestTemplate();
        OrganizationUnitDTO organizationUnitDTO = createTestOrganizationUnit();
        TaxRateDTO taxRateDTO = createTestTaxRate();

        AddressDTO emptyAddress = AddressDTO.builder().build();

        CreateUpdateReceiptPositionDTO createUpdateReceiptPositionDTO = CreateUpdateReceiptPositionDTO
            .builder()
            .title("test")
            .taxRateId(taxRateDTO.getId())
            .quantity(2)
            .unit(UnitDTO.PIECES)
            .singleGrossAmount(BigDecimal.valueOf(119))
            .build();

        List<CreateUpdateReceiptPositionDTO> positions = List.of(createUpdateReceiptPositionDTO);

        createUpdateReceiptDTO = CreateUpdateReceiptDTO
            .builder()
            .receiptType(ReceiptTypeDTO.INVOICE)
            .receiptDate(LocalDate.now())
            .organizationUnitId(organizationUnitDTO.getId())
            .templateId(templateDTO.getId())
            .receiverAddress(emptyAddress)
            .senderAddress(emptyAddress)
            .receiptNumber("test")
            .deliveryDate(LocalDate.now())
            .positions(positions)
            .build();

        MvcResult createdResult = post(URL, createUpdateReceiptDTO, status().isCreated());
        ReceiptDTO receiptDTO = mapResultToObject(createdResult, ReceiptDTO.class);

    }

    private TemplateDTO createTestTemplate() throws Exception {
        CreateUpdateTemplateDTO createUpdateTemplateDTO = CreateUpdateTemplateDTO
            .builder()
            .name("test")
            .content("<h1>I'm a test</h1>")
            .build();

        ResponseEntity<TemplateDTO> createdTemplate = templateClient.create(createUpdateTemplateDTO);
        Assertions.assertTrue(createdTemplate.getStatusCode().is2xxSuccessful());
        return createdTemplate.getBody();
    }

    private OrganizationUnitDTO createTestOrganizationUnit() throws Exception {
        AddressDTO addressDTO = AddressDTO.builder().build();
        CreateUpdateOrganizationUnitDTO createUpdateOrganizationUnitDTO = CreateUpdateOrganizationUnitDTO
            .builder()
            .name("test")
            .address(addressDTO)
            .build();

        MvcResult createResult = post("/organization-units", createUpdateOrganizationUnitDTO, status().isCreated());
        return mapResultToObject(createResult, OrganizationUnitDTO.class);
    }

    private TaxRateDTO createTestTaxRate() throws Exception {
        CreateUpdateTaxRateDTO createUpdateTaxRateDTO = CreateUpdateTaxRateDTO
            .builder()
            .name("test")
            .rate(BigDecimal.valueOf(0.19))
            .build();
        MvcResult createResult = post("/tax-rates", createUpdateTaxRateDTO, status().isCreated());
        return mapResultToObject(createResult, TaxRateDTO.class);
    }
}
