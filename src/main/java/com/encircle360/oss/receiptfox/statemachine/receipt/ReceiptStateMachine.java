package com.encircle360.oss.receiptfox.statemachine.receipt;

import java.util.EnumSet;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.encircle360.oss.receiptfox.event.CreateReceiptDocumentEvent;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptEvent;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptStatus;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableStateMachineFactory(name = "receiptStateMachineFactory")
public class ReceiptStateMachine extends EnumStateMachineConfigurerAdapter<ReceiptStatus, ReceiptEvent> {

    public static String RECEIPT_HEADER = "RECEIPT";

    private final ApplicationEventPublisher eventPublisher;
    private final ReceiptService receiptService;

    @Override
    public void configure(final StateMachineTransitionConfigurer<ReceiptStatus, ReceiptEvent> transitions) throws Exception {
        transitions
            .withExternal()
            .source(ReceiptStatus.DRAFT)
            .target(ReceiptStatus.OPEN)
            .event(ReceiptEvent.SET_OPEN)
            .action(switchState(ReceiptStatus.OPEN))
            .and()
            .withExternal()
            .source(ReceiptStatus.OPEN)
            .target(ReceiptStatus.PAID)
            .event(ReceiptEvent.SET_PAID)
            .action(switchState(ReceiptStatus.PAID))
            .and()
            .withExternal()
            .source(ReceiptStatus.OPEN)
            .target(ReceiptStatus.CANCELED)
            .event(ReceiptEvent.SET_CANCELED)
            .action(switchState(ReceiptStatus.CANCELED));
    }

    @Override
    public void configure(final StateMachineStateConfigurer<ReceiptStatus, ReceiptEvent> stateConfigurer) throws Exception {
        stateConfigurer
            .withStates()
            .states(EnumSet.allOf(ReceiptStatus.class))
            .initial(ReceiptStatus.DRAFT)
            .end(ReceiptStatus.PAID)
            .end(ReceiptStatus.CANCELED);
    }

    private Action<ReceiptStatus, ReceiptEvent> switchState(final ReceiptStatus status) {
        return context -> {
            Receipt receipt = (Receipt) context.getMessageHeader(RECEIPT_HEADER);
            receipt.setStatus(status);
            receipt = receiptService.save(receipt);

            if (status.equals(ReceiptStatus.OPEN) || status.equals(ReceiptStatus.CANCELED)) {
                CreateReceiptDocumentEvent receiptDocumentEvent = CreateReceiptDocumentEvent
                    .builder()
                    .receiptId(receipt.getId())
                    .source(this)
                    .build();
                eventPublisher.publishEvent(receiptDocumentEvent);
            }
        };
    }
}
