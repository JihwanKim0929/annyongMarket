package com.example.demo.Controller;

import com.example.demo.repository.BoardRepository;
import com.example.demo.Service.BoardService;
import com.example.demo.Service.UserService;
import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class BoardController {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;
    @Autowired
    private UserService userService;

    @GetMapping("/board/list")
    public String list(Model model, Principal principal) {
        List<Board> boardList = boardService.getBoardList();
        model.addAttribute("boardList", boardList);
        return "board/board_list";
    }

    @GetMapping("/board/new")
    public String newBoard() {
        return "board/board_new";
    }

    @PostMapping("/board/create")
    public String createBoard(@ModelAttribute BoardDto dto, @RequestParam("image") MultipartFile image, Principal principal)  throws IOException {
        System.out.println(dto.toString());
        SiteUser user = userService.getUserByUserName(principal.getName());
        boardService.createBoard(dto,user,image);
        return "redirect:/board/list";
    }

    @GetMapping("/board/detail/{boardId}")
    public String detail(@PathVariable("boardId") Long boardId, Model model, Principal principal) {
        Board board = boardService.getBoardById(boardId);
        model.addAttribute("board", board);

        boolean isLoggedIn = (principal != null);

        boolean isNotBoardAuthor = true;
        if(isLoggedIn){
            SiteUser currentUser = userService.getUserByUserName(principal.getName());
            isNotBoardAuthor = !currentUser.getId().equals(board.getAuthor().getId());
        }
        model.addAttribute("isNotBoardAuthor", isNotBoardAuthor);
        return "board/board_detail";
    }
}
