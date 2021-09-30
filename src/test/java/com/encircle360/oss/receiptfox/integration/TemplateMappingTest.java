package com.encircle360.oss.receiptfox.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.receiptfox.AbstractTest;
import com.encircle360.oss.receiptfox.dto.TemplateMappingDTO;
import com.encircle360.oss.receiptfox.dto.api.CreateUpdateTemplateMappingDTO;
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.OrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.api.CreateUpdateOrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.receipt.ReceiptTypeDTO;

@SpringBootTest
public class TemplateMappingTest extends AbstractTest {

    private static String URL = "/template-mappings";

    @Test
    public void test_crud() throws Exception {
        CreateUpdateTemplateMappingDTO createUpdateTemplateMappingDTO = CreateUpdateTemplateMappingDTO.builder().build();
        post(URL, createUpdateTemplateMappingDTO, status().isBadRequest());

        OrganizationUnitDTO organizationUnitDTO = createOrganizationUnit();
        createUpdateTemplateMappingDTO = CreateUpdateTemplateMappingDTO
            .builder()
            .templateId("test")
            .organizationUnitId(organizationUnitDTO.getId())
            .type(ReceiptTypeDTO.INVOICE)
            .build();

        MvcResult postResult = post(URL, createUpdateTemplateMappingDTO, status().isCreated());
        TemplateMappingDTO templateMappingDTO = mapResultToObject(postResult, TemplateMappingDTO.class);

        Assertions.assertEquals(createUpdateTemplateMappingDTO.getTemplateId(), templateMappingDTO.getTemplateId());
        Assertions.assertEquals(createUpdateTemplateMappingDTO.getOrganizationUnitId(), templateMappingDTO.getOrganizationUnitId());
        Assertions.assertEquals(createUpdateTemplateMappingDTO.getType(), templateMappingDTO.getType());
        Assertions.assertNotNull(templateMappingDTO.getCreated());
        Assertions.assertNotNull(templateMappingDTO.getUpdated());
        Assertions.assertNotNull(templateMappingDTO.getId());

        MvcResult getResult = get(URL + "/" + templateMappingDTO.getId(), status().isOk());
        TemplateMappingDTO getTemplateMappingDTO = mapResultToObject(getResult, TemplateMappingDTO.class);

        Assertions.assertEquals(getTemplateMappingDTO.getTemplateId(), templateMappingDTO.getTemplateId());
        Assertions.assertEquals(getTemplateMappingDTO.getOrganizationUnitId(), templateMappingDTO.getOrganizationUnitId());
        Assertions.assertEquals(getTemplateMappingDTO.getType(), templateMappingDTO.getType());
        Assertions.assertEquals(getTemplateMappingDTO.getCreated(), templateMappingDTO.getCreated());
        Assertions.assertEquals(getTemplateMappingDTO.getUpdated(), templateMappingDTO.getUpdated());
        Assertions.assertEquals(getTemplateMappingDTO.getId(), templateMappingDTO.getId());

        // test failed dependency
        createUpdateTemplateMappingDTO.setOrganizationUnitId(324L);
        put(URL + "/" + templateMappingDTO.getId(), createUpdateTemplateMappingDTO, status().isFailedDependency());
        post(URL, createUpdateTemplateMappingDTO, status().isFailedDependency());

        createUpdateTemplateMappingDTO.setOrganizationUnitId(organizationUnitDTO.getId());
        createUpdateTemplateMappingDTO.setType(ReceiptTypeDTO.INTERIM_BILL);
        createUpdateTemplateMappingDTO.setStandart(true);

        MvcResult putResult = put(URL + "/" + templateMappingDTO.getId(), createUpdateTemplateMappingDTO, status().isOk());
        TemplateMappingDTO putTemplateMapping = mapResultToObject(putResult, TemplateMappingDTO.class);

        Assertions.assertEquals(templateMappingDTO.getId(), putTemplateMapping.getId());
        Assertions.assertEquals(templateMappingDTO.getTemplateId(), putTemplateMapping.getTemplateId());
        Assertions.assertNotEquals(templateMappingDTO.getType(), putTemplateMapping.getType());
        Assertions.assertNotEquals(templateMappingDTO.isStandart(), putTemplateMapping.isStandart());
        Assertions.assertNotEquals(templateMappingDTO.getUpdated(), putTemplateMapping.getUpdated());
        Assertions.assertEquals(templateMappingDTO.getCreated(), putTemplateMapping.getCreated());

        getResult = get(URL + "/" + templateMappingDTO.getId(), status().isOk());
        getTemplateMappingDTO = mapResultToObject(getResult, TemplateMappingDTO.class);

        Assertions.assertEquals(getTemplateMappingDTO.getId(), putTemplateMapping.getId());
        Assertions.assertEquals(getTemplateMappingDTO.getUpdated(), putTemplateMapping.getUpdated());

        delete(URL + "/" + templateMappingDTO.getId(), status().isNoContent());
        delete(URL + "/" + templateMappingDTO.getId(), status().isNotFound());
        get(URL + "/" + templateMappingDTO.getId(), status().isNotFound());
        put(URL + "/" + templateMappingDTO.getId(), createUpdateTemplateMappingDTO, status().isNotFound());
    }

    private OrganizationUnitDTO createOrganizationUnit() throws Exception {
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
