package com.example.documentsapi.model;

import java.io.Serializable;

public class IssuesComment implements Serializable {
    public String url;
    public int number;
    public String issue_url;
    public String node_id;
    public User owner;
}
