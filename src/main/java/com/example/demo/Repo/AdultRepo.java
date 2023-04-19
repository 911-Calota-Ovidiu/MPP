package com.example.demo.Repo;

import com.example.demo.Model.Adult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdultRepo extends JpaRepository<Adult,Long> {
}
