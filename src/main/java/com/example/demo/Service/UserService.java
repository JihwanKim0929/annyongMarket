package com.example.demo.Service;

import com.example.demo.repository.SiteUserRepository;
import com.example.demo.dto.SiteUserDto;
import com.example.demo.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private SiteUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BoardService boardService;
    @Autowired
    private ChatRoomService chatRoomService;

    public SiteUserDto createUser(SiteUserDto userDto){
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userDto.setPenalty(0);
        SiteUser created = userRepository.save(userDto.get_SiteUser());
        return created.get_SiteUserDto();
    }

    public SiteUser getUserByUserName(String username) {
        Optional<SiteUser> siteUser = this.userRepository.findByusername(username);
        return siteUser.orElse(null);
    }

    public SiteUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(SiteUser user) {
        boardService.deleteBoardByUser(user);
        chatRoomService.deleteChatRoomByUser(user);
        userRepository.delete(user);
    }
}
