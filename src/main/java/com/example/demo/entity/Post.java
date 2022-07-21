package com.example.demo.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name ="tbl_post")
@Data
public class Post extends BaseEntity{
    private String title;
    private String content;
}
