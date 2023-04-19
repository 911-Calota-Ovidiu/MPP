package com.example.demo.Model;

public class ChildDTO {
    private String name;
    private String address;
    private Long famid;
    public ChildDTO(String name, String address,Long famid)
    {
        this.name=name;
        this.address=address;
        this.famid=famid;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
    public Long getFamid()
    {
        return famid;
    }
    public void setFamid(Long id){
        famid=id;
    }
}
