package com.example.sinitto.dto;

import java.util.Date;

public class ReviewResponse {

    String name;
    int starCount;
    Date postDate;
    String content;

    public ReviewResponse() {
    }

    public String getName() {
        return name;
    }

    public int getStarCount() {
        return starCount;
    }

    public Date getPostDate() {
        return postDate;
    }

    public String getContent() {
        return content;
    }
}
