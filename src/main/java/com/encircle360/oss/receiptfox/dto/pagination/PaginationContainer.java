package com.encircle360.oss.receiptfox.dto.pagination;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationContainer {

    @Schema(example = "status.StatusCode,DESC")
    private String sort;
    @Schema(example = "20")
    private Integer size;
    @Schema(example = "0")
    private Integer page;

    @Schema(example = "143")
    private Integer totalElements;
    @Schema(example = "8")
    private Integer totalPages;

    @Schema(example = "true")
    private Boolean first;
    @Schema(example = "false")
    private Boolean last;
    @Schema(example = "false")
    private Boolean empty;
}
