package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinTable(name="firstFriend")
    @JoinColumn(name = "childID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Child c1;

    @ManyToOne
    @JoinTable(name="secondFriend")
    @JoinColumn(name = "childID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Child c2;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")//, nullable = true), nullable = false)
    private User user;
    public Child getFriends() {
        return c2;
    }

    public Child getPerson() {
        return c1;
    }
}
