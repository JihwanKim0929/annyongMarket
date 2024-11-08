package com.example.demo.dto;


import com.example.demo.entity.Board;
import com.example.demo.entity.SiteUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private Long id;

    private String title;

    private String content;

    private SiteUser author;

    private String image_url;

    public Board getBoard() {
        return Board.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .image_url(image_url)
                .build();
    }
}
