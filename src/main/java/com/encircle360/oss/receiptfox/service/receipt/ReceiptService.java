package com.encircle360.oss.receiptfox.service.receipt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.encircle360.oss.receiptfox.client.docsrabbit.RenderClient;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderFormatDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderResultDTO;
import com.encircle360.oss.receiptfox.model.TemplateMapping;
import com.encircle360.oss.receiptfox.model.contact.Contact;
import com.encircle360.oss.receiptfox.model.receipt.Receipt;
import com.encircle360.oss.receiptfox.repository.receipt.ReceiptRepository;
import com.encircle360.oss.receiptfox.service.TemplateMappingService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final TemplateMappingService templateMappingService;
    private final ReceiptRepository receiptRepository;
    private final RenderClient renderClient;

    public Page<Receipt> filter(Pageable pageable, Long organizationUnitId) {
        if (organizationUnitId == null) {
            return receiptRepository.findAll(pageable);
        }
        return receiptRepository.findAllByOrganizationUnitId(organizationUnitId, pageable);
    }

    public Receipt get(Long id) {
        return receiptRepository.findById(id).orElse(null);
    }

    public Receipt save(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    public void delete(Receipt receipt) {
        receiptRepository.delete(receipt);
    }

    public RenderResultDTO render(Receipt receipt) {
        String templateId = getTemplateId(receipt);

        Map<String, Object> model = new HashMap<>();
        Contact contact = receipt.getContact();

        model.put("receipt", receipt);
        model.put("contact", contact);

        RenderRequestDTO renderRequestDTO = RenderRequestDTO
            .builder()
            .templateId(templateId)
            .format(RenderFormatDTO.PDF)
            .model(model)
            .build();

        ResponseEntity<RenderResultDTO> resultResponseEntity = renderClient.render(renderRequestDTO);
        RenderResultDTO result = resultResponseEntity.getBody();

        if (!resultResponseEntity.getStatusCode().is2xxSuccessful() || result == null || result.getBase64() == null) {
            throw new IllegalStateException("Rendering was not successful.");
        }
        return result;
    }

    private String getTemplateId(Receipt receipt) {
        String templateId = receipt.getTemplateId();
        if (templateId == null) {
            TemplateMapping templateMapping = templateMappingService
                .getDefaultForOrganizationUnitAndType(receipt.getOrganizationUnit(), receipt.getReceiptType());

            if (templateMapping != null) {
                templateId = templateMapping.getTemplateId();
            }
        }

        if (templateId == null) {
            return receipt.getOrganizationUnit().getDefaultTemplateId();
        }
        return templateId;
    }
}
