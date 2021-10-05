package com.encircle360.oss.receiptfox.dto.pagination;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageContainer<T> {

    private List<T> content;

    private PaginationContainer pagination;
}
