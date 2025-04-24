package com.example.app.sharry.dao;



import com.example.app.sharry.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String username);
    List<User> findByUsernameNot(String username);
}