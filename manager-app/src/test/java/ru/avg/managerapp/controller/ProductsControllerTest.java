package ru.avg.managerapp.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import ru.avg.managerapp.client.ProductRestClient;
import ru.avg.managerapp.controller.dto.NewProductDto;
import ru.avg.managerapp.entity.Product;
import ru.avg.managerapp.exception.BadRequestException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты ProductController")
class ProductsControllerTest {

    @Mock
    ProductRestClient productRestClient;

    @InjectMocks
    ProductsController controller;

    @Test
    @DisplayName("createProduct создать новый товар и перенаправит на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        //given
        var dto = new NewProductDto("Новый товар", "Описание нового товара");
        var model = new ConcurrentModel();

        doReturn(new Product(1, "Новый товар", "Описание нового товара"))
                .when(this.productRestClient)
                .createNewProduct("Новый товар", "Описание нового товара");

        //when
        var result = this.controller.createProduct(dto, model);

        //then
        assertEquals("redirect:/catalogue/products/1", result);
        verify(this.productRestClient).createNewProduct("Новый товар", "Описание нового товара");
        verifyNoMoreInteractions(this.productRestClient);
    }

    @Test
    @DisplayName("createProduct вернет страницу с ошибками если запрос не валиден")
    void createProduct_RequestIsInvalid_ReturnsProductFormWithErrors() {
        //given
        var dto = new NewProductDto("   ", null);
        var model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Ошибка 1", "Ошибка 2")))
                .when(this.productRestClient)
                .createNewProduct("   ", null);

        //when
        var result = this.controller.createProduct(dto, model);

        //then
        assertEquals("catalogue/products/new_product", result);
        assertEquals(dto, model.getAttribute("dto"));
        assertEquals(List.of("Ошибка 1", "Ошибка 2"), model.getAttribute("errors"));
        verify(this.productRestClient).createNewProduct("   ", null);
        verifyNoMoreInteractions(this.productRestClient);
    }

}