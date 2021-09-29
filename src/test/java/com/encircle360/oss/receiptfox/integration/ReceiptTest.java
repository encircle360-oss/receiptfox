package com.encircle360.oss.receiptfox.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.OrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.api.CreateUpdateOrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;
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
        AddressDTO emptyAddress = AddressDTO.builder().build();
        List<CreateUpdateReceiptPositionDTO> positions = new ArrayList<>();

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
}
