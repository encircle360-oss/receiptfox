package com.encircle360.oss.receiptfox.client.docsrabbit.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TemplateDTO {

    private String id;

    private String name;

    private String content;

    private String locale;

    private List<String> tags;

    private LocalDateTime lastUpdate;
}
