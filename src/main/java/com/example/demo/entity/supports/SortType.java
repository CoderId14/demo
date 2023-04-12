package com.example.demo.entity.supports;

public enum SortType {
    createdDate("createdDate"),
    modifiedDate("modifiedDate");
    private String sortType;

    SortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
