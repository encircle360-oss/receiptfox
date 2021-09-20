package com.encircle360.oss.receiptfox.model.contact;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import com.encircle360.oss.receiptfox.model.AbstractEntity;
import com.encircle360.oss.receiptfox.model.OrganizationUnit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Contact extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private Salutation salutation;

    private String company;

    private String firstName;

    private String lastName;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address address;

    private String contactNumber;

    private String phone;

    private String mobile;

    private String email;

    @Enumerated(EnumType.STRING)
    private ContactType contactType;

    @ManyToOne
    private OrganizationUnit organizationUnit;

}
