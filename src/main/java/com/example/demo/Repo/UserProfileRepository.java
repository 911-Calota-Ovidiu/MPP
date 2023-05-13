package com.example.demo.Repo;

import com.example.demo.Model.UserJwt;
import com.example.demo.Model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {
}
