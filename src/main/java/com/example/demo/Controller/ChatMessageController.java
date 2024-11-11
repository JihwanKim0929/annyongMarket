package com.example.demo.Controller;

import com.deepl.api.DeepLException;
import com.deepl.api.TextResult;
import com.deepl.api.Translator;
import com.example.demo.Service.ChatMessageService;
import com.example.demo.Service.ChatRoomService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.ChatMessage;
import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatMessageController {
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private UserService userService;

    @Value("${deepl-api-key}")
    String authKey;

    Translator translator;

    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/sub/chat/{chatRoomId}")
    public ChatMessage sendMessage(@Payload Map<String,Object> payload, @DestinationVariable Long chatRoomId) throws Exception {
        String content = (String) payload.get("content");
        String senderIdStr = (String) payload.get("senderId");
        Long senderId = Long.parseLong(senderIdStr);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(content);

        SiteUser sender = userService.getUserById(senderId);
        chatMessage.setSender(userService.getUserById(senderId));

        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        chatMessage.setChatRoom(chatRoom);
        try {
            SiteUser opponent;
            if (senderId.equals(chatRoom.getSiteUser1().getId()))
                opponent = chatRoom.getSiteUser2();
            else
                opponent = chatRoom.getSiteUser1();
            String opponentLang = opponent.getLang();
            if (!opponentLang.equals(sender.getLang())) {
                translator = new Translator(authKey);
                TextResult result = translator.translateText(content, null, opponentLang);
                chatMessage.setTranslation(result.getText());
            }
        } catch (Exception e) {}
        chatMessageService.saveMessage(chatMessage);
        return chatMessage;
    }
}
