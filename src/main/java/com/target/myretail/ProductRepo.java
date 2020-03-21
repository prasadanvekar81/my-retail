package com.target.myretail;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

//@Component
interface ProductRepo extends MongoRepository<Product, Integer> {
    Product findOneByProductId(Integer Id);
}
