package com.example.mykitchen.services;

import com.example.mykitchen.model.Dish;
import com.example.mykitchen.repositories.DishRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DishService {
    private DishRepository dishRepository;

    public DishService(DishRepository dishRepository){
        this.dishRepository = dishRepository;
    }

    /**
     * Check if dish exist by id
     * @param id dish id
     * @return bool
     */
    public boolean dishExists(Long id) {
        List<Dish> dishes = dishRepository.findAll();
        if (id != null) {
            for (Dish f: dishes) {
                if(Objects.equals(f.getId(), id)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * GET - get all dishes
     * @return List<Dish>
     */
    public List<Dish> getAllDishes(){
        return dishRepository.findAll();
    }


    /**
     * GET - find dish by id
     * @param id dish id
     * @return Optional<Dish>
     */
    public Optional<Dish> getOneFoodById(Long id){
        return dishRepository.findById(id);
    }

    public Dish getDishByName(String name){
        List<Dish> dishes = dishRepository.findAll();
        if (name != null) {
            for (Dish f: dishes) {
                if(f.getName().equalsIgnoreCase(name)){
                   return f;
                }
            }
        }
        return null;
    }

    /**
     * GET - search dishes by name
     * @param name dish name
     * @return List<Dish> or null
     */
    public List<Dish> SearchDishByNameTime(String name){
        List<Dish> dishes = dishRepository.findAll();
        if (name != null) {
            List<Dish> searchDishName = new ArrayList<>();
            for (Dish f: dishes) {
                if(f.name.toLowerCase().contains(name.toLowerCase()) ){
                    searchDishName.add(f);
                }
            }
            return searchDishName;
        }
      return null;
    }


    /**
     * POST - add new dish.
     * @param f new dish
     * @return HTTP status 201 or 500
     */

    public Object saveDish(Dish f){
        try {
            dishRepository.save(f);
            return HttpServletResponse.SC_CREATED;
        }catch (Exception e){
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

    }

    /**
     * PUT - modify dish by id
     * @param id dish id
     * @param i new dish
     * @return HTTP status 201 or 404
     */
    public Object updateDish(Long id, Dish i){
        List<Dish> dishes = dishRepository.findAll();
        if (id != null && i !=null) {
            for (Dish dish:dishes) {
                if(Objects.equals(dish.getId(), id)){
                    dish.setName(i.getName());
                    dish.setCategory(i.getCategory());
                    dish.setTime(i.getTime());
                    dish.setDescription(i.getDescription());
                    dishRepository.save(dish);
                    return HttpServletResponse.SC_CREATED;
                }
            }
        }

        return HttpServletResponse.SC_NOT_FOUND;
    }

    /**
     * DELETE - delete dish by id.
     * @param id dish id
     * @return HTTP status 200 500 404
     */
    public Object deleteDishById(Long id){
        try {
            dishRepository.deleteById(id);
        }catch (Exception e){
            return  HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        return  HttpServletResponse.SC_OK;
    }

    /**
     * DELETE - delete all dishes
     */
    public void deleteAll(){
       dishRepository.deleteAll();
    }

    public Object deleteDishByName(String name){
        List<Dish> dishes = dishRepository.findAll();
        if (name != null) {
            for (Dish f: dishes) {
                if(f.getName().equalsIgnoreCase(name)){
                    dishRepository.deleteById(f.getId());
                    return  HttpServletResponse.SC_OK;
                }
            }
        }
        return  HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }




}

