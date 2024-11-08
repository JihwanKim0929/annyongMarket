package com.example.demo.repository;

import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findBySiteUser1AndSiteUser2(SiteUser siteUser1, SiteUser siteUser2);

    List<ChatRoom> findBySiteUser1OrSiteUser2(SiteUser siteUser1, SiteUser siteUser2);
}
