<<<<<<< HEAD
package com.example.demo.Model;

public class AdultDTO {
    private String name;
    private String address;
    private int age;
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
=======
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
>>>>>>> 118a3a82d5056d24973de98f517c694ff76ac1b8
