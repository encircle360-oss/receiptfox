package com.encircle360.oss.receiptfox.controller.receipt;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.encircle360.oss.receiptfox.dto.receipt.ReceiptEventDTO;
import com.encircle360.oss.receiptfox.mapping.receipt.ReceiptEventMapper;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptEvent;
import com.encircle360.oss.receiptfox.model.receipt.ReceiptStatus;
import com.encircle360.oss.receiptfox.service.receipt.ReceiptService;
import com.encircle360.oss.receiptfox.statemachine.receipt.ReceiptStateMachineService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/process-receipts")
public class ReceiptProcessController {

    private final ReceiptService receiptService;

    private final ReceiptStateMachineService receiptStateMachineService;

    private final ReceiptEventMapper receiptEventMapper = ReceiptEventMapper.INSTANCE;

    @PutMapping(value = "/{id}/{event}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> process(@PathVariable final Long id, @PathVariable(name = "event") final ReceiptEventDTO receiptEventDTO) {
        Receipt receipt = receiptService.get(id);
        if (receipt == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        ReceiptEvent event = receiptEventMapper.fromDto(receiptEventDTO);
        ReceiptStatus oldStatus = receipt.getStatus();

        StateMachine<ReceiptStatus, ReceiptEvent> stateMachine = receiptStateMachineService.sendStateMachineEvent(receipt, event);

        if (oldStatus.equals(stateMachine.getState().getId())) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).build();
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
