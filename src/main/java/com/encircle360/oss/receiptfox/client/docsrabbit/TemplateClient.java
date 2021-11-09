package com.encircle360.oss.receiptfox.client.docsrabbit;

import com.encircle360.oss.receiptfox.client.docsrabbit.dto.template.CreateUpdateTemplateDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.template.TemplateDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "templateClient", url = "http://docsrabbit-service:50005/templates", decode404 = true)
public interface TemplateClient {

    @GetMapping("")
    ResponseEntity<PageContainer<TemplateDTO>> list(@RequestParam(required = false) String sort,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam(required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(required = false) List<String> tag);

    @GetMapping("/{id}")
    ResponseEntity<TemplateDTO> get(@PathVariable final String id);

    @PostMapping("")
    ResponseEntity<TemplateDTO> create(@RequestBody @Valid CreateUpdateTemplateDTO createUpdateTemplateDTO);

    @PutMapping(value = "/{id}")
    ResponseEntity<TemplateDTO> update(@PathVariable final String id, @RequestBody @Valid final CreateUpdateTemplateDTO createUpdateTemplateDTO);

    @DeleteMapping(value = "/{id}")
    ResponseEntity<Void> delete(@PathVariable final String id);
}
