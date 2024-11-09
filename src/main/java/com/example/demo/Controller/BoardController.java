package com.example.demo.Controller;

import com.example.demo.repository.BoardRepository;
import com.example.demo.Service.BoardService;
import com.example.demo.Service.UserService;
import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.SiteUser;
import com.example.demo.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    private SiteUserRepository siteUserRepository;

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
    public String createBoard(@ModelAttribute BoardDto dto, @RequestParam("image") MultipartFile image, Principal principal, RedirectAttributes rttr)  throws IOException {
        SiteUser user = userService.getUserByUserName(principal.getName());
        if(!boardService.createBoard(dto,user,image)){
            rttr.addFlashAttribute("msg","위험 물질 발견됨! 등록 불가합니다. 경고 누적 1회!");
            user.setPenalty(user.getPenalty()+1);
            siteUserRepository.save(user);
            if(user.getPenalty()>=2){
                userService.deleteUser(user);
                return "redirect:/user/logout";
            }
        };
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

        model.addAttribute("isSelling", "Selling".equals(board.getStatus()));
        model.addAttribute("isSoldOut", "SoldOut".equals(board.getStatus()));
        model.addAttribute("isReserved", "Reserved".equals(board.getStatus()));
        return "board/board_detail";
    }

    @GetMapping("/board/{id}/delete")
    public String deleteBoard(@PathVariable("id") Long id, RedirectAttributes rttr) {
        boardService.deleteBoardById(id);
        rttr.addFlashAttribute("msg","게시글을 삭제했습니다!");
        return "redirect:/board/list";
    }

    @PostMapping("/board/{id}/status")
    public String updateBoardStatus(@PathVariable("id") Long id, @RequestParam String status) {
        boardService.updateBoardStatus(id,status);
        return "redirect:/board/detail/"+id;
    }
}
