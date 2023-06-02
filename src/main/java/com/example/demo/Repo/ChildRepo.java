package com.example.demo.Repo;

import com.example.demo.Model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChildRepo extends JpaRepository<Child,Long> {
    List<Child> findByUserId(Long id);
    @Query("SELECT AVG(o.age) FROM Child o")
    Double averageChildAge();
}
