package com.encircle360.oss.receiptfox.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptEventDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptFileDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptPositionDTO;
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
            .unitGrossAmount(BigDecimal.valueOf(119))
            .build();

        CreateUpdateReceiptPositionDTO createUpdateReceiptPositionDTO2 = CreateUpdateReceiptPositionDTO
            .builder()
            .title("test")
            .taxRateId(taxRateDTO.getId())
            .quantity(5)
            .unit(UnitDTO.PIECES)
            .unitNetAmount(BigDecimal.valueOf(100))
            .build();

        CreateUpdateReceiptPositionDTO createUpdateReceiptPositionDTO3 = CreateUpdateReceiptPositionDTO
            .builder()
            .title("test")
            .taxRateId(taxRateDTO.getId())
            .quantity(5)
            .unit(UnitDTO.PIECES)
            .unitNetAmount(BigDecimal.valueOf(145.23))
            .build();

        // always use a prime number after decimal point to test calculation
        CreateUpdateReceiptPositionDTO createUpdateReceiptPositionDTO4 = CreateUpdateReceiptPositionDTO
            .builder()
            .title("test")
            .taxRateId(taxRateDTO.getId())
            .quantity(5)
            .unit(UnitDTO.PIECES)
            .unitGrossAmount(BigDecimal.valueOf(145.17))
            .build();

        List<CreateUpdateReceiptPositionDTO> positions = List.of(
            createUpdateReceiptPositionDTO,
            createUpdateReceiptPositionDTO2,
            createUpdateReceiptPositionDTO3,
            createUpdateReceiptPositionDTO4
        );

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

        Assertions.assertEquals(createUpdateReceiptDTO.getReceiptType(), receiptDTO.getReceiptType());
        Assertions.assertEquals(createUpdateReceiptDTO.getReceiptDate(), receiptDTO.getReceiptDate());
        Assertions.assertEquals(createUpdateReceiptDTO.getOrganizationUnitId(), receiptDTO.getOrganizationUnitId());
        Assertions.assertEquals(createUpdateReceiptDTO.getTemplateId(), receiptDTO.getTemplateId());
        Assertions.assertEquals(createUpdateReceiptDTO.getReceiverAddress(), receiptDTO.getReceiverAddress());
        Assertions.assertEquals(createUpdateReceiptDTO.getSenderAddress(), receiptDTO.getSenderAddress());
        Assertions.assertEquals(createUpdateReceiptDTO.getReceiptNumber(), receiptDTO.getReceiptNumber());
        Assertions.assertEquals(createUpdateReceiptDTO.getDeliveryDate(), receiptDTO.getDeliveryDate());
        Assertions.assertNotNull(receiptDTO.getPositions());
        Assertions.assertEquals(4, receiptDTO.getPositions().size());

        BigDecimal totalNetAmount = BigDecimal.ZERO;
        BigDecimal totalGrossAmount = BigDecimal.ZERO;
        BigDecimal totalTaxAmount = BigDecimal.ZERO;

        for (ReceiptPositionDTO position : receiptDTO.getPositions()) {
            BigDecimal quantity = BigDecimal.valueOf(position.getQuantity());
            BigDecimal calculatedGross = position.getUnitNetAmount().multiply(position.getTaxRatePercent().add(BigDecimal.ONE));

            Assertions.assertEquals(scaled(position.getUnitGrossAmount()), scaled(calculatedGross));
            Assertions.assertEquals(scaled(position.getUnitGrossAmount().multiply(quantity)), scaled(position.getTotalGrossAmount()));
            Assertions.assertEquals(scaled(position.getUnitNetAmount().multiply(quantity)), scaled(position.getTotalNetAmount()));
            Assertions.assertEquals(scaled(position.getUnitGrossAmount().subtract(position.getUnitNetAmount())), scaled(position.getUnitTaxAmount()));
            Assertions.assertEquals(scaled(position.getUnitTaxAmount().multiply(quantity)), scaled(position.getTotalTaxAmount()));
            Assertions.assertEquals(scaled(position.getTaxRatePercent()), scaled(taxRateDTO.getRate()));
            Assertions.assertEquals(position.getTaxRateId(), taxRateDTO.getId());

            totalNetAmount = totalNetAmount.add(position.getTotalNetAmount());
            totalGrossAmount = totalGrossAmount.add(position.getTotalGrossAmount());
            totalTaxAmount = totalTaxAmount.add(position.getTotalTaxAmount());
        }

        Assertions.assertEquals(scaled(totalNetAmount), scaled(receiptDTO.getNetAmount()));
        Assertions.assertEquals(scaled(totalGrossAmount), scaled(receiptDTO.getGrossAmount()));
        Assertions.assertEquals(scaled(totalTaxAmount), scaled(receiptDTO.getTaxAmount()));

        // remove one position and recheck calculations
        positions = List.of(createUpdateReceiptPositionDTO, createUpdateReceiptPositionDTO2, createUpdateReceiptPositionDTO3);
        createUpdateReceiptDTO.setPositions(positions);

        MvcResult putResult = put(URL + "/" + receiptDTO.getId(), createUpdateReceiptDTO, status().isOk());
        receiptDTO = mapResultToObject(putResult, ReceiptDTO.class);

        Assertions.assertEquals(3, receiptDTO.getPositions().size());

        totalNetAmount = BigDecimal.ZERO;
        totalGrossAmount = BigDecimal.ZERO;
        totalTaxAmount = BigDecimal.ZERO;

        for (ReceiptPositionDTO position : receiptDTO.getPositions()) {
            BigDecimal quantity = BigDecimal.valueOf(position.getQuantity());
            Assertions.assertEquals(scaled(position.getUnitGrossAmount().multiply(quantity)), scaled(position.getTotalGrossAmount()));
            Assertions.assertEquals(scaled(position.getUnitNetAmount().multiply(quantity)), scaled(position.getTotalNetAmount()));
            Assertions.assertEquals(scaled(position.getUnitGrossAmount().subtract(position.getUnitNetAmount())), scaled(position.getUnitTaxAmount()));
            Assertions.assertEquals(scaled(position.getUnitTaxAmount().multiply(quantity)), scaled(position.getTotalTaxAmount()));
            Assertions.assertEquals(scaled(position.getTaxRatePercent()), scaled(taxRateDTO.getRate()));
            Assertions.assertEquals(position.getTaxRateId(), taxRateDTO.getId());

            totalNetAmount = totalNetAmount.add(position.getTotalNetAmount());
            totalGrossAmount = totalGrossAmount.add(position.getTotalGrossAmount());
            totalTaxAmount = totalTaxAmount.add(position.getTotalTaxAmount());
        }

        Assertions.assertEquals(scaled(totalNetAmount), scaled(receiptDTO.getNetAmount()));
        Assertions.assertEquals(scaled(totalGrossAmount), scaled(receiptDTO.getGrossAmount()));
        Assertions.assertEquals(scaled(totalTaxAmount), scaled(receiptDTO.getTaxAmount()));

        MvcResult listResult = get(URL, status().isOk());
        PageContainer<ReceiptDTO> pageContainer = mapResultToPageContainer(listResult, ReceiptDTO.class);

        Assertions.assertEquals(1, pageContainer.getPagination().getTotalElements());
        Assertions.assertNotNull(pageContainer.getContent());
        Assertions.assertFalse(pageContainer.getContent().isEmpty());

        delete(URL + "/" + receiptDTO.getId(), status().isNoContent());
        delete(URL + "/" + receiptDTO.getId(), status().isNotFound());
        get(URL + "/" + receiptDTO.getId(), status().isNotFound());
        put(URL + "/" + receiptDTO.getId(), createUpdateReceiptDTO, status().isNotFound());

        listResult = get(URL, status().isOk());
        pageContainer = mapResultToPageContainer(listResult, ReceiptDTO.class);

        Assertions.assertEquals(0, pageContainer.getPagination().getTotalElements());
        Assertions.assertNotNull(pageContainer.getContent());
        Assertions.assertTrue(pageContainer.getContent().isEmpty());
    }

    @Test
    public void test_processing_and_download() throws Exception {
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
            .unitGrossAmount(BigDecimal.valueOf(119))
            .build();

        CreateUpdateReceiptDTO createUpdateReceiptDTO = CreateUpdateReceiptDTO
            .builder()
            .receiptType(ReceiptTypeDTO.INVOICE)
            .receiptDate(LocalDate.now())
            .organizationUnitId(organizationUnitDTO.getId())
            .templateId(templateDTO.getId())
            .receiverAddress(emptyAddress)
            .senderAddress(emptyAddress)
            .receiptNumber(UUID.randomUUID().toString())
            .deliveryDate(LocalDate.now())
            .positions(List.of(createUpdateReceiptPositionDTO))
            .build();

        MvcResult createdResult = post(URL, createUpdateReceiptDTO, status().isCreated());
        ReceiptDTO receiptDTO = mapResultToObject(createdResult, ReceiptDTO.class);

        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_OPEN, status().isNoContent());

        MvcResult receiptWithDocumentResult = get(URL + "/" + receiptDTO.getId(), status().isOk());
        ReceiptDTO receiptWithDocumentDto = mapResultToObject(receiptWithDocumentResult, ReceiptDTO.class);

        Assertions.assertNotNull(receiptWithDocumentDto.getReceiptFileId());

        MvcResult receiptFileResult = get("/receipt-files/" + receiptWithDocumentDto.getReceiptFileId() + "/download", status().isOk());
        byte[] receiptFileBytes = receiptFileResult.getResponse().getContentAsByteArray();

        Assertions.assertEquals(receiptFileResult.getResponse().getContentType(), MediaType.APPLICATION_PDF_VALUE);
        Assertions.assertNotNull(receiptFileBytes);
        Assertions.assertTrue(receiptFileBytes.length > 0);

        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_OPEN, status().isPreconditionFailed());
        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_CANCELED, status().isNoContent());
        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_PAID, status().isPreconditionFailed());
    }

    @Test
    public void test_processing_and_ocr() throws Exception {
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
            .unitGrossAmount(BigDecimal.valueOf(119))
            .build();

        CreateUpdateReceiptDTO createUpdateReceiptDTO = CreateUpdateReceiptDTO
            .builder()
            .receiptType(ReceiptTypeDTO.INVOICE)
            .receiptDate(LocalDate.now())
            .organizationUnitId(organizationUnitDTO.getId())
            .templateId(templateDTO.getId())
            .receiverAddress(emptyAddress)
            .senderAddress(emptyAddress)
            .receiptNumber(UUID.randomUUID().toString())
            .deliveryDate(LocalDate.now())
            .positions(List.of(createUpdateReceiptPositionDTO))
            .build();

        MvcResult createdResult = post(URL, createUpdateReceiptDTO, status().isCreated());
        ReceiptDTO receiptDTO = mapResultToObject(createdResult, ReceiptDTO.class);

        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_OPEN, status().isNoContent());

        MvcResult receiptWithDocumentResult = get(URL + "/" + receiptDTO.getId(), status().isOk());
        ReceiptDTO receiptWithDocumentDto = mapResultToObject(receiptWithDocumentResult, ReceiptDTO.class);

        Assertions.assertNotNull(receiptWithDocumentDto.getReceiptFileId());

        MvcResult receiptFileResult = get("/receipt-files/" + receiptWithDocumentDto.getReceiptFileId(), status().isOk());
        ReceiptFileDTO receiptFileDTO = mapResultToObject(receiptFileResult, ReceiptFileDTO.class);

        Assertions.assertNull(receiptFileDTO.getOcr());
        Assertions.assertNotNull(receiptFileDTO.getName());
        Assertions.assertNotNull(receiptFileDTO.getId());

        Thread.sleep(2000);
        receiptFileResult = get("/receipt-files/" + receiptWithDocumentDto.getReceiptFileId(), status().isOk());
        receiptFileDTO = mapResultToObject(receiptFileResult, ReceiptFileDTO.class);

        Assertions.assertNotNull(receiptFileDTO.getOcr());

        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_OPEN, status().isPreconditionFailed());
        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_CANCELED, status().isNoContent());
        emptyPut("/process-receipts/" + receiptDTO.getId() + "/" + ReceiptEventDTO.SET_PAID, status().isPreconditionFailed());
    }

    private BigDecimal scaled(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    private TemplateDTO createTestTemplate() throws Exception {
        String testHtml = "<h1>I'm a test</h1><br/>${id} - ${receiptNumber!\"\"}";

        CreateUpdateTemplateDTO createUpdateTemplateDTO = CreateUpdateTemplateDTO
            .builder()
            .name("test")
            .content(testHtml)
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
