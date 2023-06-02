package com.example.demo.Repo;

import com.example.demo.Model.Adult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdultRepo extends JpaRepository<Adult,Long> {

    List<Adult> findByAge(Integer age, Pageable pageable);
    List<Adult> findByUserId(Long id);
    @Query("Select a from Adult a where a.age=:age")
    List<Adult> getAdultsByAge(@Param("age")int age);
}
