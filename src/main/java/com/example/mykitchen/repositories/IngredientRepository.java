package com.example.mykitchen.repositories;


import com.example.mykitchen.model.Ingredient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends CrudRepository<Ingredient,Long> {
    List<Ingredient> findAll();
}
