package com.target.myretail;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductService {


    @Autowired
    ProductRepo productRepo;


    public Product getProduct(Integer id) {
        RestTemplate restTemplate = new RestTemplate();
        Product productFromDb = productRepo.findOneByProductId(id);

        try {
            ResponseEntity<JsonNode> response =
                    restTemplate.exchange("https://redsky.target.com/v2/pdp/tcin/" + id + "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics",
                            HttpMethod.GET, null, JsonNode.class);

            JsonNode map = response.getBody();
            String itemTitle = map.get("product").get("item").get("product_description").get("title").asText();

            productFromDb.name = itemTitle;
        } catch (Exception e) {
            System.out.println(e);
        }

        return productFromDb;

    }

    public Product save(Product req, Integer id) {

        productRepo.delete(productRepo.findOneByProductId(id));
        productRepo.save(req);
        System.out.println(req);
        return req;
    }


}
