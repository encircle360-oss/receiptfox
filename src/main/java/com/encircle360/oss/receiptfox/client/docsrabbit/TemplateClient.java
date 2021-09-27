package com.encircle360.oss.receiptfox.client.docsrabbit;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.encircle360.oss.receiptfox.client.docsrabbit.dto.TemplateDTO;
import com.encircle360.oss.receiptfox.dto.pagination.PageContainer;

@FeignClient(name = "templateClient", url = "http://docsrabbit-service:50005")
public interface TemplateClient {

    @GetMapping("")
    ResponseEntity<PageContainer<TemplateDTO>> list(@RequestParam(required = false) String sort,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size,
                                                    @RequestParam(required = false, defaultValue = "0") Integer page,
                                                    @RequestParam(required = false) List<String> tag);

    @GetMapping("/{id}")
    ResponseEntity<TemplateDTO> get(@PathVariable final String id);
}
