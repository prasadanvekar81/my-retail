package com.target.myretail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping(value = "/{id}")
    public Product getProduct(@PathVariable("id") Integer id) {
        return productService.getProduct(id);
    }

    @PutMapping(value = "/{id}")
    public Product putProducts(@RequestBody Product req, @PathVariable("id") Integer id) {

        if(id.equals(req.productId)) {
            return productService.save(req, id);
        }
        throw new InputMismatchException("Id in url and object are not matching");
    }
}