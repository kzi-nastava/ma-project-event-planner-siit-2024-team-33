package com.example.myapplication.dto;

import java.util.List;

public class PageResponse<T> {
    private List<T> content;
    private int number;
    private int size;
    private int totalPages;
    private long totalElements;

    public List<T> getContent() {
        return content;
    }


    public long getTotalElements() {
        return totalElements;
    }

    public int getNumber() {
        return this.number;
    }

    public int getSize() {
        return this.size;
    }
    public int getTotalPages(){
        return this.totalPages;
    }
}

