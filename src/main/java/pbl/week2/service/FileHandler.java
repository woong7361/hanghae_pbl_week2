package pbl.week2.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import pbl.week2.config.exception.PblException;
import pbl.week2.entity.entityDto.BoardDto;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static pbl.week2.config.exception.ErrorConstant.FILE_ERROR;

@Component
@Slf4j
public class FileHandler {

    public String getFilePath(MultipartFile multipartFile) {
        // 전달되어 온 파일이 존재하지 않을 경우
        if (multipartFile == null) return null;

        // 파일 디렉토리 주소 생성
        String path = getFileDirectoryPath();
        File file = new File(path);

        // 파일 디렉토리 생성
        makeDirectory(file);
        
        // 파일의 확장자 추출
        String originalFileExtension = getFileExtension(multipartFile);

        // 파일명 중복 피하고자 나노초까지 얻어와 지정
        String new_file_name = System.nanoTime() + originalFileExtension;

        return path + File.separator + new_file_name;
    }
    

    public void saveFile(MultipartFile multipartFile, String filePath) {
        // 전달되어 온 파일이 존재할 경우
        if (multipartFile == null || filePath == null) {
            return;
        }
        saveMultipartFile(multipartFile, filePath);
        log.info("파일 하드에 저장 완료");
    }


    public byte[] getFileToByte(String imagePath) {
        InputStream imageStream = null;
        try {
            imageStream = new FileInputStream(BoardDto.ABSOLUTE_PATH + imagePath);
        } catch (FileNotFoundException e) {
            log.info("파일을 찾을 수 없습니다");
            return null;
        }

        byte[] imageByteArray = new byte[0];
        try {
            imageByteArray = IOUtils.toByteArray(imageStream);
            imageStream.close();
        } catch (IOException e) {
            throw new PblException("IOEception 파일을 byte로 바꾸다 에러 발생", FILE_ERROR);
        }

        return imageByteArray;
    }

    public void removeFile(String filePath) {
        File file = new File(BoardDto.ABSOLUTE_PATH + filePath);
        if (file.exists()) { //파일이 존재하면 지운다
            log.info("파일을 지우려는 시도중 .....");
            file.delete();
        }else{
            log.info("{} 인 경로에 지우고자 하는 파일이 존재하지 않습니다.", BoardDto.ABSOLUTE_PATH + filePath);
        }
    }

    
    
    //======================================편의 메소드=======================================//
    
    
    private void makeDirectory(File file) {
        if (!file.exists()) {
            boolean wasSuccessful = file.mkdirs();
            // 디렉터리 생성에 실패했을 경우
            if (!wasSuccessful){
                throw new PblException("디렉토리 생성 실패", FILE_ERROR);
            }
        }
    }

    private String getFileExtension(MultipartFile multipartFile) {
        String originalFileExtension;
        String contentType = multipartFile.getContentType();

        // 확장자명이 존재하지 않을 경우 처리 x
        if (ObjectUtils.isEmpty(contentType)) {
            throw new PblException("확장자명이 존재하지 않습니다", FILE_ERROR);
        } else {  // 확장자가 jpeg, png인 파일들만 받아서 처리
            if (contentType.contains("image/jpeg"))
                originalFileExtension = ".jpg";
            else if (contentType.contains("image/png"))
                originalFileExtension = ".png";
            else { // 다른 확장자일 경우 처리 x
                log.info("jpeg, png이외의 확장자가 들어왔습니다. {}", contentType);
                throw new PblException("jpeg, png이외의 확장자가 들어왔습니다.", FILE_ERROR);
            }
        }
        return originalFileExtension;
    }

    private String getFileDirectoryPath() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyyMMdd");
        String current_date = now.format(dateTimeFormatter);

        // 파일을 저장할 세부 경로 지정 -> 날짜로 디렉토리 만들기
        String path = "images" + File.separator + current_date;
        return path;
    }

    private void saveMultipartFile(MultipartFile multipartFile, String filePath) {
        // 파일 데이터를 지정한 경로에 저장
        File file = new File(BoardDto.ABSOLUTE_PATH + filePath);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new PblException("파일 저장 에러", FILE_ERROR);
        }

        // 파일 권한 설정(쓰기, 읽기)
        file.setWritable(true);
        file.setReadable(true);
    }
    
}
