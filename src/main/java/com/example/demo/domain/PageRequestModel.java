package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageRequestModel {
    private int page;
    private int size;
    private String sortColumn;
    private String sortDirection;
}
