package com.example.demo.Model;

import java.util.ArrayList;
import java.util.List;

public class FriendDTO {
    private Child c1;
    private List<Child> friends=new ArrayList<>();
    public FriendDTO(Child c)
    {
        c1=c;
    }
    public Child getC1()
    {
        return c1;
    }

    public List<Child> getFriends() {
        return friends;
    }

    public void addFriends(Child c)
    {
        friends.add(c);
    }
    public void addFriends(List<Child> c)
    {
        friends.addAll(c);}
}
