package com.example.demo.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="adults")
public class Adult implements Person{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long adultID;
    @Column(name="aname")
    public String aname;
    @Column(name="eyecolor")
    String eyeColor;
    @Column(name="address")
    String address;
    @Column(name="birthdate")
    String birthdate;
    @Column(name="age")
    public Integer age;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")//, nullable = true)
    private User user;

    public String getName() {
        return aname;
    }
    public String getAddress()
    {
        return address;
    }
    public String getEyeColor()
    {
        return eyeColor;
    }
    public String getBirthdate(){
        return birthdate;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age){this.age=age;}
    public void setAddress(String address){this.address=address;}
    public String toString()
    {
        return adultID+"\n"+aname+"\n"+age+"\n"+address+"\n"+birthdate+"\n"+eyeColor;
    }
}
