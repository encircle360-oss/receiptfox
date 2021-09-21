package com.encircle360.oss.receiptfox.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import com.encircle360.oss.receiptfox.AbstractTest;
import com.encircle360.oss.receiptfox.dto.contact.AddressDTO;
import com.encircle360.oss.receiptfox.dto.contact.ContactDTO;
import com.encircle360.oss.receiptfox.dto.contact.ContactTypeDTO;
import com.encircle360.oss.receiptfox.dto.contact.SalutationDTO;
import com.encircle360.oss.receiptfox.dto.contact.api.CreateUpdateContactDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.OrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.organizationunit.api.CreateUpdateOrganizationUnitDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;

@SpringBootTest
public class ContactTest extends AbstractTest {

    @Test
    public void test_crud() throws Exception {
        OrganizationUnitDTO organizationUnitDTO = createOrganizationUnit();

        CreateUpdateContactDTO createUpdateContactDTO = CreateUpdateContactDTO.builder().build();
        post("/contacts", createUpdateContactDTO, status().isBadRequest());

        AddressDTO addressDTO = AddressDTO.builder().build();
        createUpdateContactDTO = CreateUpdateContactDTO
            .builder()
            .salutation(SalutationDTO.MR)
            .contactType(ContactTypeDTO.CUSTOMER)
            .organizationUnitId(999L)
            .address(addressDTO)
            .build();

        post("/contacts", createUpdateContactDTO, status().isFailedDependency());
        createUpdateContactDTO.setOrganizationUnitId(organizationUnitDTO.getId());

        MvcResult createResult = post("/contacts", createUpdateContactDTO, status().isCreated());
        ContactDTO contactDTO = mapResultToObject(createResult, ContactDTO.class);

        Assertions.assertNotNull(contactDTO.getId());
        Assertions.assertEquals(organizationUnitDTO.getId(), contactDTO.getOrganizationUnitId());
        Assertions.assertEquals(SalutationDTO.MR, contactDTO.getSalutation());
        Assertions.assertEquals(ContactTypeDTO.CUSTOMER, contactDTO.getContactType());

        MvcResult getResult = get("/contacts/" + contactDTO.getId(), status().isOk());
        ContactDTO getContactDto = mapResultToObject(getResult, ContactDTO.class);
        Assertions.assertEquals(contactDTO.getId(), getContactDto.getId());
        Assertions.assertEquals(contactDTO.getContactType(), getContactDto.getContactType());
        Assertions.assertEquals(contactDTO.getSalutation(), getContactDto.getSalutation());
        Assertions.assertEquals(contactDTO.getOrganizationUnitId(), getContactDto.getOrganizationUnitId());

        MvcResult listResult = get("/contacts", status().isOk());
        PageContainer<ContactDTO> pageContainer = mapResultToPageContainer(listResult, ContactDTO.class);

        Assertions.assertNotNull(pageContainer);
        Assertions.assertNotNull(pageContainer.getPagination());
        Assertions.assertTrue(pageContainer.getPagination().getTotalElements() > 0);

        createUpdateContactDTO.setOrganizationUnitId(999L);
        put("/contacts/" + contactDTO.getId(), createUpdateContactDTO, status().isFailedDependency());

        createUpdateContactDTO.setOrganizationUnitId(organizationUnitDTO.getId());
        createUpdateContactDTO.setSalutation(SalutationDTO.MRS);
        MvcResult putResult = put("/contacts/" + contactDTO.getId(), createUpdateContactDTO, status().isOk());
        ContactDTO putContact = mapResultToObject(putResult, ContactDTO.class);

        getResult = get("/contacts/" + contactDTO.getId(), status().isOk());
        getContactDto = mapResultToObject(getResult, ContactDTO.class);

        Assertions.assertEquals(createUpdateContactDTO.getSalutation(), putContact.getSalutation());
        Assertions.assertEquals(createUpdateContactDTO.getSalutation(), getContactDto.getSalutation());

        delete("/contacts/" + contactDTO.getId(), status().isNoContent());
        delete("/contacts/" + contactDTO.getId(), status().isNotFound());
        put("/contacts/" + contactDTO.getId(), createUpdateContactDTO, status().isNotFound());
        get("/contacts/" + contactDTO.getId(), status().isNotFound());
        get("/contacts", status().isOk());
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
