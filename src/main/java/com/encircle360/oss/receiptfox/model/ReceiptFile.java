package com.encircle360.oss.receiptfox.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

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
public class ReceiptFile extends AbstractEntity {

    private String name;

    private String ocr;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> meta;

    private String s3Path;

    private String s3Bucket;
}
