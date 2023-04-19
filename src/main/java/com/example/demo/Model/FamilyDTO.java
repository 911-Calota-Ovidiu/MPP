package com.example.demo.Model;

public class FamilyDTO {
    private Long mom,dad;
    private String address;
    public FamilyDTO(Long mom, Long dad, String address)
    {
        this.mom=mom;
        this.dad=dad;
        this.address=address;
    }
    public Long getMom(){return mom;}
    public Long getDad(){return dad;}
    public String getHomeAddress(){return address;}
}
