package com.example.demo.dto;

import com.example.demo.entity.SiteUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteUserDto {

    private Long id;

    private String username;

    private String password;

    @JsonIgnore
    public SiteUser get_SiteUser(){
        return SiteUser.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
    }
}

