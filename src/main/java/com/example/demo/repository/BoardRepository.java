package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.example.demo.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByAuthor(SiteUser user);
}
