package com.example.demo.Model;

public class AdultDTO {
    private String name;
    private String address;
    public AdultDTO(String name, String address)
    {
        this.name=name;
        this.address=address;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
