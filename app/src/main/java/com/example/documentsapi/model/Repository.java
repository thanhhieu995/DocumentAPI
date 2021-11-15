package com.example.documentsapi.model;

import java.io.Serializable;

public class Repository implements Serializable {
    public int id;
    public String name;
    public String full_name;
    public String description;
    public String url;
    public User owner;
}
