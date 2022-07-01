package com.example.mykitchen.controllers;


import com.example.mykitchen.model.Dish;
import com.example.mykitchen.model.Ingredient;
import com.example.mykitchen.model.Review;
import com.example.mykitchen.services.DishService;
import com.example.mykitchen.services.IngredientsService;
import com.example.mykitchen.services.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/dishes")
@CrossOrigin(origins = "*",allowedHeaders = "*")
public class DishController {
    private final DishService fs;
    private final ReviewService rs;

    private final IngredientsService is;

    public DishController(DishService fs, ReviewService rs, IngredientsService is){
        this.fs = fs;
        this.rs = rs;
        this.is = is;
    }

    /**
     * GET -get all dishes.
     * @return List<Dish>
     */
    @GetMapping
    public List<Dish> getAll(){
        return fs.getAllDishes();
    }

    /**
     * GET -get one dish by id.
     * @param id the id of dish.
     * @return dish or HTTP status 404.
     */
    @GetMapping("/{id}")
    public Dish getDishByID(@PathVariable Long id){
        Optional<Dish> f = fs.getOneFoodById(id);
        if(f.isPresent()){
            return f.orElse(null);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"can't find this dish!");
        }
    }

    /**
     * GET -Search for dishes with matching name .
     * @param search the name of dish
     * @return List<Dish>
     */
    @GetMapping("/search")
    public List<Dish> find(@RequestParam String search){
        return fs.SearchDishByNameTime(search);
    }

    /**
     * POST -add new dish
     * @param f -new dish
     * @return HTTP status 201 or 500.
     */
    @PostMapping(consumes = {"application/json"})
    public Object addFoodJson(@RequestBody Dish f){
        return fs.saveDish(f);
    }

    /**
     * PUT -update exist dish
     * @param id  find dish by id
     * @param fo new dish
     * @return HTTP status 201 or 404.
     */
    @PutMapping("/{id}")
    public Object updateFood(@PathVariable Long id, @RequestBody Dish fo){
        return this.fs.updateDish(id,fo);
    }

    /**
     * DELETE -delete all dishes.
     */
    @DeleteMapping
    public void deleteAll(){
        fs.deleteAll();
    }

    /**
     * DELETE -delete dish by id.
     * @param id id
     * @return 1. 404 2. HTTP status 200 404 500.
     */
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable Long id){
        if(!this.fs.dishExists(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id " + id + " not found!");
        }
        return fs.deleteDishById(id);
    }


    /**
     *Review
     */

    /**
     * GET -get all reviews in dish by id.
     * @param id dish id
     * @return List<Review>
     */
    @GetMapping("/{id}/reviews")
    public List<Review> getReviewsForDish(@PathVariable Long id){
        List<Review> reviews = getDishByID(id).getReviewList();
        return reviews;
    }

    /**
     * GET -get one review by id.
     * @param id dish id
     * @param rid the id of review.
     * @return review or HttpStatus.NOT_FOUND.
     */
    @GetMapping("/{id}/reviews/{rid}")
    public Optional<Review> getReviewByID(@PathVariable Long id, @PathVariable Long rid){
        Dish dish = getDishByID(id);
        if(dish==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id " + id + " not found!");
        }
        List<Review> reviews = getDishByID(id).getReviewList();
        Optional<Review> r = rs.getOneReviewById(reviews,rid);
        if(r.isPresent()){
            return r;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"can't find this review!");
        }
    }

    /**
     * GET -Search for reviews with matching score .
     * @param search the score of review
     * @return List<Review>
     */
    @GetMapping("/{id}/reviews/search")
    public Object find(@PathVariable Long id, @RequestParam(defaultValue = "0") int search){
        List<Review> reviews = getDishByID(id).getReviewList();
        return rs.SearchReviewByScore(reviews,search);
    }

    /**
     * POST -Add a review for dish.
     * @param id dish id
     * @param review new review
     * @return HTTP status 201 or 500.
     */
    @PostMapping("/{id}/reviews")
    public Object addReviewForDish(@PathVariable Long id, @RequestBody Review review){
        Dish dish = getDishByID(id);
        if(dish==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id " + id + " not found!");
        }
        return rs.addReviewToDish(dish,review);
    }

    /**
     * PUT -update exist review.
     * @param id  find review by id
     * @param r new review
     * @return HTTP status 201 400 404.
     */
    @PutMapping("/{id}/reviews/{rid}")
    public Object updateReview(@PathVariable Long id,@PathVariable Long rid, @RequestBody Review r){
        if(!this.rs.reviewExists(rid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review with id " + rid + " not found!");
        }
        List<Review> reviews = getDishByID(id).getReviewList();
        return this.rs.updateReview(reviews,rid,r);
    }


    /**
     * DELETE -delete all reviews in dish.
     * @param id dish id
     */
    @DeleteMapping("/{id}/reviews")
    public void deleteAllReviews(@PathVariable Long id){
        rs.deleteAllReviews(id);
    }


    /**
     * DELETE -delete one review in dish by id.
     * @param id dish id
     * @param rid review id
     * @return 404 or 200 or 500
     */
    @DeleteMapping("/{id}/reviews/{rid}")
    public Object deleteReviewById(@PathVariable Long id, @PathVariable Long rid){
        if(!this.rs.reviewExists(rid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review with id " + rid + " not found!");
        }
        if(!this.fs.dishExists(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id " + id + " not found!");
        }
        return rs.deleteReviewById(id,rid);
    }


    /**
     * Ingredient
     */

    /**
     * GET -get all ingredients in dish by id.
     * @param id dish id
     * @return List<Review>
     */
    @GetMapping("/{id}/ingredients")
    public List<Ingredient> getIngredientsForDish(@PathVariable Long id){
        List<Ingredient> ingredients = getDishByID(id).getIngredientList();
        return ingredients;
    }


    /**
     * GET -get one ingredient by id.
     * @param id dish id
     * @param iid the id of ingredient.
     * @return ingredient or HttpStatus.NOT_FOUND.
     */
    @GetMapping("/{id}/ingredients/{iid}")
    public Optional<Ingredient> getIngredientByID(@PathVariable Long id, @PathVariable Long iid){
        Dish dish = getDishByID(id);
        if(dish==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id " + id + " not found!");
        }
        List<Ingredient> ingredients = getDishByID(id).getIngredientList();
        Optional<Ingredient> i = is.getOneIngredientById(ingredients,iid);
        if(i.isPresent()){
            return i;
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"can't find this ingredient!");
        }
    }

    /**
     * GET -Search for ingredients with matching score .
     * @param search the name of ingredient
     * @return List<Ingredient>
     */
    @GetMapping("/{id}/ingredients/search")
    public Object find(@PathVariable Long id, @RequestParam(defaultValue = "0") String search){
        List<Ingredient> ingredients = getDishByID(id).getIngredientList();
        return is.SearchIngredientByName(ingredients,search);
    }

    /**
     * POST -Add an ingredient for dish.
     * @param id dish id
     * @param ingredient new ingredient
     * @return HTTP status 201 or 500.
     */
    @PostMapping("/{id}/ingredients")
    public Object addIngredientForDish(@PathVariable Long id, @RequestBody Ingredient ingredient){
        Dish dish = getDishByID(id);
        if(dish==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id " + id + " not found!");
        }
        return is.addIngredientToDish(dish,ingredient);
    }

    /**
     * PUT -update exist ingredient.
     * @param id dish id
     * @param iid ingredient id
     * @param i new ingredient
     * @return HTTP status 201 400 404.
     */
    @PutMapping("/{id}/ingredients/{iid}")
    public Object updateIngredient(@PathVariable Long id,@PathVariable Long iid, @RequestBody Ingredient i){
        if(!this.is.ingredientExists(iid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient with id " + iid + " not found!");
        }
        List<Ingredient> ingredients = getDishByID(id).getIngredientList();
        return this.is.updateIngredient(ingredients,iid,i);
    }

    /**
     * DELETE -delete all ingredients in dish.
     * @param id dish id
     */
    @DeleteMapping("/{id}/ingredients")
    public void deleteIngredients(@PathVariable Long id){
        is.deleteAllIngredients(id);
    }

    /**
     * DELETE -delete one ingredient in dish by id.
     * @param id dish id
     * @param iid ingredient id
     * @return HTTP status 404 or 200 or 500
     */
    @DeleteMapping("/{id}/ingredients/{iid}")
    public Object deleteIngredientById(@PathVariable Long id, @PathVariable Long iid){
        if(!this.is.ingredientExists(iid)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient with id " + iid + " not found!");
        }
        if(!this.fs.dishExists(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish with id " + id + " not found!");
        }
        return is.deleteIngredientById(id,iid);
    }




}
