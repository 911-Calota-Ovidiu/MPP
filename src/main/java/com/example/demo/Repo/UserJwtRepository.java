package com.example.demo.Repo;

import com.example.demo.Model.UserJwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserJwtRepository extends JpaRepository<UserJwt,Long> {
    Boolean existsByJwtToken(String jwtToken);

    Boolean existsByUsername(String username);

    UserJwt findByJwtToken(String jwtToken);

    void deleteByUsername(String username);
}
