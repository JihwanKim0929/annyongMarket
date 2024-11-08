package com.example.demo.Service;

import com.example.demo.repository.BoardRepository;
import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public BoardDto createBoard(BoardDto dto, SiteUser siteUser, MultipartFile image)throws IOException {
        if(!image.isEmpty()){
            String fileName = UUID.randomUUID().toString().replace("-", "")+"_"+image.getOriginalFilename();
            String uploadDir = "C:\\demo_image\\";
            String fullPathName = uploadDir+fileName;
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            image.transferTo(new File(fullPathName));
            dto.setImage_url(fileName);
        }
        dto.setAuthor(siteUser);
        Board board = boardRepository.save(dto.getBoard());
        return board.getBoardDto();
    }

    public List<Board> getBoardList(){
        return boardRepository.findAll();
    }

    public Board getBoardById(Long id){
        return boardRepository.findById(id).orElse(null);
    }
}
