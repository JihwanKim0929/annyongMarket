package com.example.demo.Service;

import com.example.demo.repository.BoardRepository;
import com.example.demo.dto.BoardDto;
import com.example.demo.entity.Board;
import com.example.demo.entity.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BoardService {
    @Autowired
    private BoardRepository boardRepository;

    public boolean createBoard(BoardDto dto, SiteUser siteUser, MultipartFile image)throws IOException {
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
        dto.setStatus("Selling");
        Board board = boardRepository.save(dto.getBoard());
        if(validateImage(board.getImage_url())){
            boardRepository.delete(board);
            return false;
        }
        return true;
    }

    public List<Board> getBoardList(){
        return boardRepository.findAll();
    }

    public Board getBoardById(Long id){
        return boardRepository.findById(id).orElse(null);
    }

    public void deleteBoardById(Long id){
        boardRepository.deleteById(id);
    }

    public void deleteBoardByUser(SiteUser user){
        List<Board> boards = boardRepository.findByAuthor(user);
        for(Board board : boards){
            boardRepository.delete(board);
        }
    }

    public Boolean validateImage(String image_url){
        URI uri = UriComponentsBuilder
                .fromUriString("http://localhost:8000")
                .path("/image_validate")
                .encode()
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("path", "C:\\demo_image\\"+image_url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(uri, requestEntity, Map.class);

        System.out.println(response.getBody());

        return (Boolean) response.getBody().get("detected");
    }

    public void updateBoardStatus(Long id, String status){
        Board board = boardRepository.findById(id).orElse(null);
        if(board != null){
            board.setStatus(status);
            boardRepository.save(board);
        }
    }
}
