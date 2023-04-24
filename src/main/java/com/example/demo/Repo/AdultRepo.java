package com.example.demo.Repo;

import com.example.demo.Model.Adult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AdultRepo extends JpaRepository<Adult,Long> {

    @Query(value= "select a from adults a", nativeQuery = true)
    List<Object[]> getListAdults();
}
