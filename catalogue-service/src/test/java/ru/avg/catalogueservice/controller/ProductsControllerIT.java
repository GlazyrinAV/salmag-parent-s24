package ru.avg.catalogueservice.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerIT {

    @Autowired
    private MockMvc mvc;

    @Test
    @Sql("/sql/products.sql")
    void findProducts_returnsProductsList() throws Exception {
        //given
        var request = MockMvcRequestBuilders.get("/catalogue-api/products")
                .param("filter", "product")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        //when
        this.mvc.perform(request)

                //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {"id": 1, "title":  "Product 1", "details": "Detail 1"},
                                {"id": 3, "title":  "Product 3", "details": "Detail 3"}
                                ] 
                                """)
                );
    }

    @Test
    void createProduct_requestValid_returnsNewPoduct() throws Exception {
        //given
        var request = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "Product 5", "details": "Details 5"}
                        """)
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        //when
        this.mvc.perform(request)

                //then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost/catalogue-api/products/1"),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                {"id":  1, "title":  "Product 5", "details": "Details 5"}
                                """)
                );
    }

    @Test
    void createProduct_requestInvalid_returnsProblemDetail() throws Exception {
        //given
        var request = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "   ", "details": null}
                        """)
                .locale(Locale.of("ru", "RU"))
                .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        //when
        this.mvc.perform(request)

                //then
                .andDo(print())
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON_VALUE),
                        content().json("""
                                {"errors": ["Название товара не должно быть пустым или состоять из пробелов"] }
                                """)
                );
    }

    @Test
    void createProduct_UserIsNotAuthorizes_returnsForbidden() throws Exception {
        //given
        var request = MockMvcRequestBuilders.post("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"title": "   ", "details": null}
                        """)
                .locale(Locale.of("ru", "RU"))
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        //when
        this.mvc.perform(request)

                //then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden()
                );
    }
}