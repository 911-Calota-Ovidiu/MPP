
package com.example.demo.Model;

public class AdultDTO {
    private final String name;
    private final String address;
    private final int age;
    public AdultDTO(String name, String address,int age)
    {
        this.name=name;
        this.address=address;
        this.age=age;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

