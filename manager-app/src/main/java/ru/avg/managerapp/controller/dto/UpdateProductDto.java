package ru.avg.managerapp.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProductDto(

        @NotBlank(message = "{errors.catalogue.products.update.title_is_blank}")
        @Size(min = 3, max = 50, message = "{errors.catalogue.products.update.title_size_is_invalid}")
        String title,

        @Size(max = 1000, message = "{errors.catalogue.products.update.details_size_is_invalid}")
        String details) {
}
