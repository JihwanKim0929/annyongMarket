package com.example.demo.Controller;

import com.example.demo.Service.ChatRoomService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;

    @GetMapping("/chat/rooms")
    public String getUserRelatedChatRooms(Model model, Principal principal) {
        SiteUser currentUser = userService.getUserByUserName(principal.getName());
        List<ChatRoom> chatRooms = chatRoomService.getChatRoomOfUser(currentUser);
        List<Boolean> isFirst = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            if (currentUser.getId().equals(chatRoom.getSiteUser1().getId()))
                isFirst.add(true);
            else
                isFirst.add(false);
        }
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("isFirst", isFirst);
        return "chat/chatRooms";
    }
}
