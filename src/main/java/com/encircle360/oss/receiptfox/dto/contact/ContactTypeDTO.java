package com.encircle360.oss.receiptfox.dto.contact;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ContactType")
public enum ContactTypeDTO {
    CUSTOMER, SUPPLIER, BOTH

}
