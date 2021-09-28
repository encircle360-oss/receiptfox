package com.encircle360.oss.receiptfox.event;

import org.springframework.context.ApplicationEvent;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateReceiptDocumentEvent extends ApplicationEvent {

    private Long receiptId;

    @Builder
    public CreateReceiptDocumentEvent(final Long receiptId, final Object source) {
        super(source);
        this.receiptId = receiptId;
    }
}
