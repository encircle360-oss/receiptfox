package com.encircle360.oss.receiptfox.model.receipt;

import com.encircle360.oss.receiptfox.model.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Map;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReceiptFile extends AbstractEntity {

    private String name;

    @Column(columnDefinition="text")
    private String ocr;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Map<String, String> meta;

    private String s3Path;

    private String s3Bucket;
}
