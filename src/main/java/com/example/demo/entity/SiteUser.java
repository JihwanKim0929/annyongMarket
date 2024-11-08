package com.example.demo.entity;

import com.example.demo.dto.SiteUserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    public SiteUserDto get_SiteUserDto(){
        return SiteUserDto.builder()
                .id(id)
                .username(username)
                .password(password)
                .build();
    }
}