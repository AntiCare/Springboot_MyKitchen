package com.example.mykitchen.repositories;

import com.example.mykitchen.model.Dish;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends CrudRepository<Dish,Long> {
    List<Dish> findAll();
}
