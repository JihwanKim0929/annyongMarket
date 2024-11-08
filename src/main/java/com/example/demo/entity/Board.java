package com.example.demo.entity;

import com.example.demo.dto.BoardDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne
    private SiteUser author;

    private String image_url;

    public BoardDto getBoardDto() {
        return BoardDto.builder()
                .id(id)
                .title(title)
                .content(content)
                .author(author)
                .image_url(image_url)
                .build();
    }



}
