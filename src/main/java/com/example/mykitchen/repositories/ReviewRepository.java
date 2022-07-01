package com.example.mykitchen.repositories;

import com.example.mykitchen.model.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ReviewRepository extends CrudRepository<Review,Long> {
    List<Review> findAll();
}
