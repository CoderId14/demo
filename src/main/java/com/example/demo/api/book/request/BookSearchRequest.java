package com.example.demo.api.book.request;

import com.example.demo.criteria.SearchCriteria;
import lombok.Data;

import java.util.List;

@Data
public class BookSearchRequest {
    private List<SearchCriteria> searchCriteriaList;
    private String dataOption;
}
