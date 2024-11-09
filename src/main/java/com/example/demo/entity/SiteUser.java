package com.example.demo.entity;

import com.example.demo.dto.SiteUserDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SiteUser {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;

    private String password;

    private String lang;

    private Integer penalty;

    public SiteUserDto get_SiteUserDto(){
        return SiteUserDto.builder()
                .id(id)
                .username(username)
                .password(password)
                .lang(lang)
                .penalty(penalty)
                .build();
    }
}