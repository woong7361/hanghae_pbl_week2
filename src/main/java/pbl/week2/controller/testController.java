package pbl.week2.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.service.FileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class testController {

    private final FileHandler fileHandler;
    private final ApplicationEventPublisher eventPublisher;

    @ResponseBody
    @PostMapping("/multipart")
    public void multipart(
            @RequestPart(value = "file") MultipartFile file,
            @RequestPart(value = "title") String title,
            @RequestPart(value = "name") String name
    ) {
        return;
    }

    @GetMapping("/form")
    public String form() {
        return "form.html";
    }

    @ResponseBody
    @PostMapping("/multipart2")
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
}
