package com.example.mykitchen.services;

import com.example.mykitchen.model.Dish;
import com.example.mykitchen.model.Ingredient;
import com.example.mykitchen.model.Review;
import com.example.mykitchen.repositories.IngredientRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class IngredientsService {
    private IngredientRepository ir ;

    public IngredientsService(IngredientRepository ir){
        this.ir = ir;
    }
    /**
     * Check if ingredient exist by id
     * @param id ingredient id
     * @return bool
     */
    public boolean ingredientExists(Long id) {
        List<Ingredient> ingredients = ir.findAll();
        if (id != null) {
            for (Ingredient i: ingredients) {
                if(Objects.equals(i.getId(), id)){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * GET - find ingredient by id
     * @param id ingredient id
     * @return Optional<Ingredient>
     */
    public Optional<Ingredient> getOneIngredientById(List<Ingredient> ingredients, Long id){
        for (Ingredient i:ingredients) {
            if(Objects.equals(i.getId(),id)){
                return ir.findById(id);
            }
        }
        return null;
    }


    /**
     * GET - search ingredient by name
     * @param name ingredient name
     * @return List<Ingredient> or null
     */
    public List<Ingredient> SearchIngredientByName(List<Ingredient> ingredients, String name){
        if (name != null) {
            List<Ingredient> searchIngredientName = new ArrayList<>();
            for (Ingredient i:ingredients) {
                if(i.name.toLowerCase().contains(name.toLowerCase())){
                    searchIngredientName.add(i);
                }
            }
            return searchIngredientName;
        }
        return null;
    }


    /**
     * POST - add new ingredient.
     * @param dish dish
     * @param ingredient new review
     * @return HTTP status 201 or 500
     */
    public Object addIngredientToDish(Dish dish, Ingredient ingredient){
        try{
            ingredient.setDish(dish);
            ir.save(ingredient);
            return HttpServletResponse.SC_CREATED;
        }catch (Exception e){
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

    }

    /**
     * PUT - modify ingredient by id
     * @param id ingredient id
     * @param i new ingredient
     * @return HTTP status 201 400 404
     */
    public Object updateIngredient(List<Ingredient> ingredients, Long id, Ingredient i){
        if (id != null && i !=null) {
            for (Ingredient in:ingredients) {
                if(Objects.equals(in.getId(), id)){
                   in.setName(i.getName());
                   in.setWeight(i.getWeight());
                   in.setDescription(i.getDescription());
                   ir.save(in);
                    return HttpServletResponse.SC_CREATED;
                }
            }
        }
        return HttpServletResponse.SC_BAD_REQUEST;
    }


    /**
     * DELETE - delete review by id.
     * @param id dish id
     * @param iid ingredient id
     * @return  HTTP status 200 404 500
     */
    public Object deleteIngredientById(Long id,Long iid){
        try {
            List<Ingredient> ingredients = ir.findAll();
            for (Ingredient i:ingredients) {
                if(Objects.equals(i.getDish().getId(), id) && Objects.equals(i.getId(), iid)){
                    ir.deleteById(iid);
                    return  HttpServletResponse.SC_OK;
                }
            }
        }catch (Exception e){
            return  HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        return null;
    }
    /**
     * DELETE - delete all ingredients.
     */
    public void deleteAllIngredients(Long id){
        List<Ingredient> ingredients = ir.findAll();
        for (Ingredient i:ingredients) {
            if(Objects.equals(i.getDish().getId(), id)){
                ir.deleteById(i.getId());
            }
        }

    }




}
