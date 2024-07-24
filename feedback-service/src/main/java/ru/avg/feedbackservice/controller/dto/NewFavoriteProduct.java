package ru.avg.feedbackservice.controller.dto;

import jakarta.validation.constraints.NotNull;

public record NewFavoriteProduct(

        @NotNull(message = "errors.feedback.products.favorites.create.productId_null")
        Integer productId) {
}
