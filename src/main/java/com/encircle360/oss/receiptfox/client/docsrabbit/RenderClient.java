package com.encircle360.oss.receiptfox.client.docsrabbit;

import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderRequestDTO;
import com.encircle360.oss.receiptfox.client.docsrabbit.dto.render.RenderResultDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "renderClient", url = "http://docsrabbit-service:50005/render")
public interface RenderClient {

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RenderResultDTO> render(@RequestBody @Valid RenderRequestDTO renderRequestDTO);
}
