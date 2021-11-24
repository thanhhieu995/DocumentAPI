package com.example.documentsapi.model;

import java.io.Serializable;

public class Issues implements Serializable {
    public String url;
    public String repository_url;
    public int number;
    public String node_id;
    public String title;
    public UserIssues user;
}
