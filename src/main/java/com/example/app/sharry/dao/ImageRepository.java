package com.example.app.sharry.dao;


import com.example.app.sharry.model.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
    Page<Image> findAllByUser_Username(String username, Pageable pageable);

}
