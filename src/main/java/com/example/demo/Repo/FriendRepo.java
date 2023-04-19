package com.example.demo.Repo;

import com.example.demo.Model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepo extends JpaRepository<Friend,Long> {
}
