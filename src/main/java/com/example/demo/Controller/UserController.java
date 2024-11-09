package com.example.demo.Controller;

import com.example.demo.Service.UserService;
import com.example.demo.dto.SiteUserDto;
import com.example.demo.entity.SiteUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/new")
    public String newUser() {
        return "userNew";
    }

    @PostMapping("/user/create")
    public String createUser(SiteUserDto dto){
        System.out.println(dto.toString());
        userService.createUser(dto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginP() {
        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/user")
    @ResponseBody
    public String user(Principal principal){
        return principal.getName();
    }

    @GetMapping("/user/delete")
    public String deleteUser(Principal principal){
        SiteUser user = userService.getUserByUserName(principal.getName());
        userService.deleteUser(user);
        return "redirect:/user/logout";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/board/list";
    }

}
