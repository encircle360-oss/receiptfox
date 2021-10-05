package com.encircle360.oss.receiptfox.dto.contact;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AbstractEntity", description = "Superclass for entities.")
public abstract class AbstractEntityDTO {

    @Schema(description = "The database id of the entity.")
    private Long id;

    @Schema(description = "The time this entry was created.")
    private LocalDateTime created;

    @Schema(description = "The time this entry was updated, last time.")
    private LocalDateTime updated;
}
