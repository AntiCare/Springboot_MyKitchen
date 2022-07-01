package com.example.mykitchen.repositories;

import com.example.mykitchen.model.UserLogin;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<UserLogin,Long> {
    List<UserLogin> findAll();
}
