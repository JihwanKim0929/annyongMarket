package com.example.demo.Service;

import com.example.demo.entity.ChatRoom;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageService chatMessageService;

    public ChatRoom findOrCreateChatRoom(SiteUser siteUser1, SiteUser siteUser2) {

        if(siteUser1.getId()>siteUser2.getId()) {
            SiteUser tmp = siteUser1;
            siteUser1 = siteUser2;
            siteUser2 = tmp;
        }

        ChatRoom chatRoom = chatRoomRepository.findBySiteUser1AndSiteUser2(siteUser1,siteUser2).orElse(null);
        if(chatRoom==null) {
            chatRoom = new ChatRoom();
            chatRoom.setSiteUser1(siteUser1);
            chatRoom.setSiteUser2(siteUser2);
            chatRoomRepository.save(chatRoom);
        }
        return chatRoom;
    }

    public List<ChatRoom> getChatRoomOfUser(SiteUser user) {
        return chatRoomRepository.findBySiteUser1OrSiteUser2(user,user);
    }

    public ChatRoom getChatRoomById(Long id){
        return chatRoomRepository.findById(id).orElse(null);
    }

    public void deleteChatRoomByUser(SiteUser user) {
        List<ChatRoom> chatRooms = getChatRoomOfUser(user);
        for(ChatRoom chatRoom : chatRooms){
            chatMessageService.deleteMessageByChatRoom(chatRoom);
            chatRoomRepository.delete(chatRoom);
        }
    }
}
