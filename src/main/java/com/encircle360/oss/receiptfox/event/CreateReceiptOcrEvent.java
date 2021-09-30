package com.encircle360.oss.receiptfox.event;

import org.springframework.context.ApplicationEvent;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CreateReceiptOcrEvent extends ApplicationEvent {

    private final Long receiptId;

    @Builder
    public CreateReceiptOcrEvent(final Long receiptId, final Object source) {
        super(source);
        this.receiptId = receiptId;
    }
}
