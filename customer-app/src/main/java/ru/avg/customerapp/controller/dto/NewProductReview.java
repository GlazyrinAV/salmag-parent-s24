package ru.avg.customerapp.controller.dto;

import jakarta.validation.constraints.*;

public record NewProductReview(

        @NotNull(message = "{errors.customer.products.reviews.create.rating_is_null}")
        @Max(value = 5, message = "{errors.customer.products.reviews.create.rating_is_above_max}")
        @Min(value = 1, message = "{errors.customer.products.reviews.create.rating_is_below_min}")
        Integer rating,

        @NotBlank(message = "{errors.customer.products.reviews.create.review_is_blank}")
        @Size(max = 1000, message = "{errors.customer.products.reviews.create.review_size_is_invalid}")
        String review) {
}
