package ru.avg.customerapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpResponse;
import org.springframework.ui.ConcurrentModel;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.avg.customerapp.client.FavoriteProductsClient;
import ru.avg.customerapp.client.ProductsClient;
import ru.avg.customerapp.client.ProductsReviewClient;
import ru.avg.customerapp.entity.Product;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    ProductsClient productsClient;

    @Mock
    FavoriteProductsClient favoriteProductsClient;

    @Mock
    ProductsReviewClient productsReviewClient;

    @InjectMocks
    ProductController controller;

    @Test
    @DisplayName("Исключение должно быть транслировано в страницу 404")
    void handleException_returnsError404() {

        //given
        var model = new ConcurrentModel();
        var exception = new NoSuchElementException("Товар не найден");
        var response = new MockServerHttpResponse();

        //when
        var result = this.controller.handleException(exception, model, response);

        //then
        assertEquals("customer/errors/404", result);
        assertEquals("Товар не найден", model.getAttribute("error"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void loadProduct_ProductExists_returnsProductNotEmptyMono() {
        //given
        var product = new Product(1, "title 1", "details 1");

        doReturn(Mono.just(product))
                .when(this.productsClient).findProduct(1);
        //when
        StepVerifier.create(this.controller.loadProduct(1))

                //then
                .expectNext(new Product(1, "title 1", "details 1"))
                .expectComplete()
                .verify();

        verify(this.productsClient).findProduct(1);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoInteractions(this.favoriteProductsClient);
        verifyNoInteractions(this.productsReviewClient);
    }

    @Test
    void loadProduct_ProductNotExists_returnsMonoWithException() {
        //given

        doReturn(Mono.empty())
                .when(this.productsClient).findProduct(1);
        //when
        StepVerifier.create(this.controller.loadProduct(1))

                //then
                .expectErrorMatches(exception -> {
                    return exception instanceof NoSuchElementException e &&
                            e.getMessage().equals("customer.errors.product.not_found");
                })
                .verify();

        verify(this.productsClient).findProduct(1);
        verifyNoMoreInteractions(this.productsClient);
        verifyNoInteractions(this.favoriteProductsClient);
        verifyNoInteractions(this.productsReviewClient);
    }
}