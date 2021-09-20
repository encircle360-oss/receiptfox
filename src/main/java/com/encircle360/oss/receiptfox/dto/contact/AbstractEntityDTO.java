package com.encircle360.oss.receiptfox.dto.contact;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntityDTO {

    private Long id;

    private LocalDateTime created;

    private LocalDateTime updated;
}
