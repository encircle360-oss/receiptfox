package com.encircle360.oss.receiptfox.statemachine.receipt;

import static com.encircle360.oss.receiptfox.statemachine.receipt.ReceiptStateMachine.RECEIPT_HEADER;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptEvent;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptStatus;
import com.encircle360.oss.receiptfox.statemachine.StateMachineContextBuilder;
import com.encircle360.oss.receiptfox.util.StateMachineUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReceiptStateMachineService {

    private final StateMachineFactory<ReceiptStatus, ReceiptEvent> stateMachineFactory;

    public StateMachine<ReceiptStatus, ReceiptEvent> buildStateMachine(final Receipt receipt) {
        if (receipt == null) {
            return null;
        }

        String stateMachineId = StateMachineUtils.id(receipt);
        StateMachine<ReceiptStatus, ReceiptEvent> stateMachine = stateMachineFactory.getStateMachine(stateMachineId);
        stateMachine.stopReactively().subscribe();
        stateMachine.getStateMachineAccessor().doWithAllRegions(accessor -> {
            accessor.resetStateMachineReactively(StateMachineContextBuilder
                    .create(ReceiptStatus.class, ReceiptEvent.class)
                    .state(receipt.getStatus())
                    .id(stateMachineId)
                    .build())
                .subscribe();
        });
        stateMachine.startReactively().subscribe();

        return stateMachine;
    }

    public StateMachine<ReceiptStatus, ReceiptEvent> sendStateMachineEvent(final Receipt receipt, final ReceiptEvent receiptEvent) {
        StateMachine<ReceiptStatus, ReceiptEvent> stateMachine = buildStateMachine(receipt);
        Message<ReceiptEvent> message = MessageBuilder.withPayload(receiptEvent)
            .setHeader(RECEIPT_HEADER, receipt)
            .build();

        stateMachine.sendEvent(Mono.just(message)).subscribe();

        return stateMachine;
    }
}
