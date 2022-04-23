package pbl.week2.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pbl.week2.config.exception.ErrorConstant;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.service.BoardService;
import pbl.week2.service.FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class testController {

    private final FileHandler fileHandler;
    private final ApplicationEventPublisher eventPublisher;
    private final BoardService boardService;

    @PostMapping("/test")
    @ResponseBody
    public void multipart(@RequestPart(value = "picture") MultipartFile file) {
        System.out.println("file = " + file);
    }

//    @GetMapping("/form")
    public void form() {
        Optional<String> abc = Optional.empty();
        abc.orElseThrow(() -> {
            System.out.println("abc = " + abc);
            return new IllegalArgumentException(ErrorConstant.DEFAULT_ERROR);
        });

    }

//    @PostMapping("/multipart2")
    @ResponseBody
    public ResponseEntity multipart2(
            BoardDto.FileReq req
    ) throws Exception {
        String filePath = fileHandler.getFilePath(req.getPicture());
        fileHandler.saveFile(req.getPicture(), filePath);
        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

        InputStream imageStream = new FileInputStream(absolutePath + filePath);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return new ResponseEntity<>(imageByteArray, HttpStatus.OK);

    }

    @ResponseBody
    @GetMapping(value = "/image", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] form(BoardDto.FileReq req) throws Exception {
        String filePath = fileHandler.getFilePath(req.getPicture());
        fileHandler.saveFile(req.getPicture(), filePath);

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

        InputStream imageStream = new FileInputStream(absolutePath + filePath);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return imageByteArray;
    }

    @ResponseBody
    @GetMapping(value = "/api/posts/{id}/paging")
    public Slice<BoardDto.PostRes> form(@PathVariable("id") String id, Pageable pageable) {
        return boardService.getpaging(pageable);
    }
}
