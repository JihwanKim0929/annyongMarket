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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
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

    @Value("${fastapi.url}") // FastAPI 서버 URL
    private String fastApiUrl;
    private RestTemplate restTemplate = new RestTemplate();

    @MessageMapping("/chat/send/{chatRoomId}")
    @SendTo("/sub/chat/{chatRoomId}")
    public ChatMessage sendMessage(@Payload Map<String,Object> payload, @DestinationVariable Long chatRoomId) throws Exception {
        String content = (String) payload.get("content");
        String senderIdStr = (String) payload.get("senderId");
        Long senderId = Long.parseLong(senderIdStr);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(content);

        SiteUser sender = userService.getUserById(senderId);
        chatMessage.setSender(sender);

        ChatRoom chatRoom = chatRoomService.getChatRoomById(chatRoomId);
        chatMessage.setChatRoom(chatRoom);
        try {
            SiteUser opponent;
            if (senderId.equals(chatRoom.getSiteUser1().getId()))
                opponent = chatRoom.getSiteUser2();
            else
                opponent = chatRoom.getSiteUser1();

            String senderLang = sender.getLang();
            String opponentLang = opponent.getLang();
            //System.out.println("Sender Language: " + senderLang);
            //System.out.println("Opponent Language: " + opponentLang);
            if (!senderLang.equals(opponentLang)) {
                String apiEndpoint = senderLang.equals("KO") ? "/ko-en/" : "/en-ko/";
                //System.out.println("content 내용: " + content);
                String translation = callTranslationApi(content, apiEndpoint);
                //System.out.println("번역 결과: " + translation);
                if (translation != null) {
                    chatMessage.setTranslation(translation); // 번역 결과 저장
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        chatMessageService.saveMessage(chatMessage);
        return chatMessage;
    }

    private String callTranslationApi(String content, String apiEndpoint) {
        try {
            String url = fastApiUrl + apiEndpoint;
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("text", content);

            // FastAPI 번역 API 호출
            Map<String, String> response = restTemplate.postForObject(url, requestBody, Map.class);

            //System.out.println("번역 response: " + response);

            // 응답 확인 및 반환
            if (response != null) {
                if (response.containsKey("english")) {
                    return response.get("english");
                } else if (response.containsKey("korean")) {
                    return response.get("korean");
                }
            }
        } catch (Exception e) {
            System.err.println("FastAPI 번역 호출 실패: " + e.getMessage());
        }
        return null; // 번역 실패 시 null 반환
    }

}
