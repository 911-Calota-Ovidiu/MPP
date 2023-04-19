package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Children")
public class Child  implements  Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long childID;
    @Column(name="name")
    public String name;
    @Column(name="eyecolor")
    String eyeColor;
    @Column(name="address")
    String address;
    @Column(name="birthdate")
    String birthdate;
    @Column(name="age")
    public Integer age;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "famID")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    Family family;

}
