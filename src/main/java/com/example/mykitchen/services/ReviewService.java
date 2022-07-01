package com.example.mykitchen.services;

import com.example.mykitchen.model.Dish;
import com.example.mykitchen.model.Review;
import com.example.mykitchen.repositories.DishRepository;
import com.example.mykitchen.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReviewService {
    private ReviewRepository reviewRepository;
    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository=reviewRepository;
    }

    /**
     * Check if review exist by id
     * @param id review id
     * @return bool
     */
    public boolean reviewExists(Long id) {
        List<Review> reviews = reviewRepository.findAll();
        if (id != null) {
            for (Review r: reviews) {
                if(Objects.equals(r.getId(), id)){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * GET - find review by id
     * @param id review id
     * @return Optional<Review>
     */
    public Optional<Review> getOneReviewById(List<Review> reviews, Long id){
        for (Review r:reviews) {
            if(Objects.equals(r.getId(), id)){
                return reviewRepository.findById(id);
            }
        }
        return  null;
    }

    /**
     * GET - search review by score
     * @param score review score
     * @return List<Review>
     */
    public List<Review> SearchReviewByScore(List<Review> reviews,int score){
            List<Review> searchReviewScore = new ArrayList<>();
            for (Review r: reviews) {
                if(r.score==score){
                    searchReviewScore.add(r);
                }
            }
                return searchReviewScore;
    }



    /**
     * POST - add new review.
     * @param dish dish
     * @param review new review
     * @return HTTP status 201 or 500
     */
    public Object addReviewToDish(Dish dish, Review review){
        try{
            review.setDish(dish);
             reviewRepository.save(review);
            return HttpServletResponse.SC_CREATED;
        }catch (Exception e){
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }

    }


    /**
     * PUT - modify review by id
     * @param id ingredient id
     * @param i new review
     * @return HTTP status 201 400 404
     */
    public Object updateReview(List<Review> reviews,Long id, Review i){
        if (id != null && i !=null) {
            for (Review review:reviews) {
                if(Objects.equals(review.getId(), id)){
                    review.setScore(i.getScore());
                    review.setDescription(i.getDescription());
                    reviewRepository.save(review);
                    return HttpServletResponse.SC_CREATED;
                }
            }
        }
        return HttpServletResponse.SC_BAD_REQUEST;
    }

    /**
     * DELETE - delete review by id.
     * @return HTTP status 200 404 500
     */
    public Object deleteReviewById(Long id,Long rid){
        try {
            List<Review> reviews = reviewRepository.findAll();
            for (Review r:reviews) {
                if(Objects.equals(r.getDish().getId(), id) && Objects.equals(r.getId(), rid)){
                  reviewRepository.deleteById(rid);
                  return  HttpServletResponse.SC_OK;
                }
            }
        }catch (Exception e){
            return  HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        return null;
    }
    /**
     * DELETE - delete all reviews.
     */
    public void deleteAllReviews(Long id){
        List<Review> reviews = reviewRepository.findAll();
        for (Review r:reviews) {
            if(Objects.equals(r.getDish().getId(), id)){
                reviewRepository.deleteById(r.getId());
            }
        }

    }

}
