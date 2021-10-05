package com.encircle360.oss.receiptfox.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.receiptfox.AbstractTest;
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.OrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.api.CreateUpdateOrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;

@SpringBootTest
public class OrganizationUnitTest extends AbstractTest {

    @Test
    public void test_crud() throws Exception {
        CreateUpdateOrganizationUnitDTO createUpdateOrganizationUnitDTO = CreateUpdateOrganizationUnitDTO.builder().build();

        post("/organization-units", createUpdateOrganizationUnitDTO, status().isBadRequest());

        AddressDTO addressDTO = AddressDTO.builder().build();
        createUpdateOrganizationUnitDTO = CreateUpdateOrganizationUnitDTO
            .builder()
            .name("test")
            .address(addressDTO)
            .build();

        MvcResult createResult = post("/organization-units", createUpdateOrganizationUnitDTO, status().isCreated());
        OrganizationUnitDTO organizationUnitDTO = mapResultToObject(createResult, OrganizationUnitDTO.class);

        Assertions.assertNotNull(organizationUnitDTO.getId());
        Assertions.assertNotNull(organizationUnitDTO.getCreated());
        Assertions.assertNotNull(organizationUnitDTO.getUpdated());
        Assertions.assertEquals("test", organizationUnitDTO.getName());

        MvcResult listResult = get("/organization-units/", status().isOk());
        PageContainer<OrganizationUnitDTO> pageContainer = mapResultToPageContainer(listResult, OrganizationUnitDTO.class);

        Assertions.assertNotNull(pageContainer);
        Assertions.assertNotNull(pageContainer.getContent());
        Assertions.assertFalse(pageContainer.getContent().isEmpty());
        Assertions.assertTrue(pageContainer.getPagination().getTotalElements() > 0);

        MvcResult getResult = get("/organization-units/" + organizationUnitDTO.getId(), status().isOk());
        OrganizationUnitDTO getUnit = mapResultToObject(getResult, OrganizationUnitDTO.class);

        Assertions.assertEquals(organizationUnitDTO.getId(), getUnit.getId());
        Assertions.assertEquals(organizationUnitDTO.getCreated(), getUnit.getCreated());
        Assertions.assertEquals(organizationUnitDTO.getUpdated(), getUnit.getUpdated());
        Assertions.assertEquals(organizationUnitDTO.getName(), getUnit.getName());

        CreateUpdateOrganizationUnitDTO updateOrganizationUnitDTO = CreateUpdateOrganizationUnitDTO
            .builder()
            .address(addressDTO)
            .name("changed")
            .build();
        MvcResult putResult = put("/organization-units/" + organizationUnitDTO.getId(), updateOrganizationUnitDTO, status().isOk());
        OrganizationUnitDTO putUnit = mapResultToObject(putResult, OrganizationUnitDTO.class);

        Assertions.assertEquals(organizationUnitDTO.getId(), putUnit.getId());
        Assertions.assertEquals(organizationUnitDTO.getCreated(), putUnit.getCreated());
        Assertions.assertNotEquals(organizationUnitDTO.getUpdated(), putUnit.getUpdated());
        Assertions.assertEquals(updateOrganizationUnitDTO.getName(), putUnit.getName());

        getResult = get("/organization-units/" + organizationUnitDTO.getId(), status().isOk());
        getUnit = mapResultToObject(getResult, OrganizationUnitDTO.class);

        Assertions.assertEquals(putUnit.getId(), getUnit.getId());
        Assertions.assertEquals(putUnit.getCreated(), getUnit.getCreated());
        Assertions.assertEquals(putUnit.getUpdated(), getUnit.getUpdated());
        Assertions.assertEquals(putUnit.getName(), getUnit.getName());

        delete("/organization-units/" + organizationUnitDTO.getId(), status().isNoContent());
        delete("/organization-units/" + organizationUnitDTO.getId(), status().isNotFound());
        get("/organization-units/" + organizationUnitDTO.getId(), status().isNotFound());

        get("/organization-units/", status().isOk());
    }
}
