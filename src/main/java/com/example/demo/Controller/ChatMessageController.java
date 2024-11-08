package com.example.demo.Controller;

import com.example.demo.Service.ChatMessageService;
import com.example.demo.Service.ChatRoomService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class ChatMessageController {
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;

    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/sub/chat/{chatRoomId}")
    public ChatMessage sendMessage(@Payload Map<String,Object> payload, @DestinationVariable Long chatRoomId) {
        String content = (String) payload.get("content");
        String senderIdStr = (String) payload.get("senderId");
        Long senderId = Long.parseLong(senderIdStr);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(content);
        chatMessage.setSender(userService.getUserById(senderId));
        chatMessage.setChatRoom(chatRoomService.getChatRoomById(chatRoomId));

        chatMessageService.saveMessage(chatMessage);
        return chatMessage;
    }
}
