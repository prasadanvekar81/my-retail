package com.target.myretail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProductController.class)
@WebMvcTest(value = ProductController.class, secure = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    @Qualifier(value = "productDetailService")
    private ProductService productDetailsService;

    Price prodPrice = new Price();
    Product prodDetails = new Product();

    @Before
    public void setup() {

        prodPrice.setCurrency_code("USD");
        prodPrice.setValue(2000);
        prodDetails.setName("The Big Lebowski (Blu-ray)");
        prodDetails.setProductId(13860428);
        prodDetails.setPrice(prodPrice);
    }


    @Test
    public void getProductDetailsTest() throws Exception {
        Mockito.when(productDetailsService.getProduct(13860428)).thenReturn(prodDetails);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/product/" + 13860428).accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":13860428,\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":2000,\"currency_code\":\"USD\"}}";
        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test(expected = MethodArgumentTypeMismatchException.class)
    public void getProductDetailsInvalidRequestTest() throws Exception, MethodArgumentTypeMismatchException {
        String var = "XYZ";
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/product/" + var).accept(
                MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        throw result.getResolvedException();
    }

    @Test
    public void putProductDetailsTest() throws Exception {
        Price price = new Price();
        price.setCurrency_code("USD");
        price.setValue(new Double(300));
        Product product = new Product();
        product.setPrice(price);
        product.setProductId(13860428);
        product.setName("The Big Lebowski (Blu-ray)");


        Mockito.when(productDetailsService.save(product, 13860428)).thenReturn(product);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
                "/product/" + 13860428)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + 13860428 + ",\"name\":\"The Big Lebowski (Blu-ray)\",\"current_price\":{\"value\":300,\"currency_code\":\"USD\"}}")
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }
}

