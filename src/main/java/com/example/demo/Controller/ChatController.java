package com.example.demo.Controller;

import com.example.demo.Service.ChatMessageService;
import com.example.demo.Service.ChatRoomService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private UserService userService;


    @GetMapping("/chat/startByOpponentId/{opponentId}")
    public String startChatByOpponentId(@PathVariable("opponentId") Long opponentId, Model model, Principal principal) {
        SiteUser currentUser = userService.getUserByUserName(principal.getName());
        SiteUser opponent = userService.getUserById(opponentId);
        ChatRoom chatRoom = chatRoomService.findOrCreateChatRoom(currentUser, opponent);
        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("user",currentUser);
        model.addAttribute("messages",chatMessageService.getMessagesByChatRoom(chatRoom));
        model.addAttribute("opponent",opponent);
        return "chat/chat";
    }

    @GetMapping("/chat/startByChatRoomId/{chatRoomId}")
    public String startChatByChatRoomId(@PathVariable("chatRoomId") Long chatRoomId, Model model, Principal principal) {
        SiteUser currentUser = userService.getUserByUserName(principal.getName());
        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        SiteUser opponent;
        if(currentUser.getId().equals(chatRoom.getSiteUser1().getId()))
            opponent = chatRoom.getSiteUser2();
        else
            opponent = chatRoom.getSiteUser1();
        model.addAttribute("chatRoom", chatRoom);
        model.addAttribute("user",currentUser);
        model.addAttribute("messages",chatMessageService.getMessagesByChatRoom(chatRoom));
        model.addAttribute("opponent",opponent);
        return "chat/chat";
    }
}
