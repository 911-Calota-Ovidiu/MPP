package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="Family")
public class Family{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long famID;
    @Column(name="nrofmembers")
    int nrOfMembers;
    @Column(name="mom")
    Long mom;
    @Column(name="dad")
    Long dad;
    @JsonSerialize
    @OneToMany(mappedBy = "family", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    List<Child> children=new ArrayList<>();
    @Column(name="address")
    String homeAddress;

    public Long getId() {
        return famID;
    }

    public int getNrOfMembers(){return nrOfMembers;}
    public Long getMom(){return mom;}
    public Long getDad(){return dad;}
    public List<Child> getChildren(){return children;}
    public String getHomeAddress(){return homeAddress;}
    public void addChild(Child c){children.add(c);
        c.setFamily(this);
    }
    public String toString()
    {
        return nrOfMembers+"\n"+mom.toString()+"\n"+dad.toString()+"\n"+homeAddress+"\n";
    }
}
