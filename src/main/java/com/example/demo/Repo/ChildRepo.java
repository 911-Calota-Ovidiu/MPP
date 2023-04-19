package com.example.demo.Repo;

import com.example.demo.Model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepo extends JpaRepository<Child,Long> {
}
