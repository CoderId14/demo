package com.example.demo.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TagDTO{
    private String title;
    private String content;
}
