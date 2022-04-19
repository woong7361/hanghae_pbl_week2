package pbl.week2.event.eventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pbl.week2.entity.entityDto.BoardDto;
import pbl.week2.event.FileDeleteEvent;
import pbl.week2.event.FileRecoverEvent;
import pbl.week2.repository.BoardRepository;
import pbl.week2.service.FileHandler;

import java.io.File;
import java.io.FileOutputStream;

import static java.lang.Thread.sleep;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileEventHandler {

    private final FileHandler fileHandler;

    @EventListener
    public void deleteFile(FileDeleteEvent event) {
        String filePath = event.getFilePath();

        for(int i=0; i<5; i++){  //5번 시도
            try{
                fileHandler.removeFile(filePath);
                break;
            }catch(Exception e){
//                sleep(1000*i); // 조금 쉬었다가
                continue;
            }
        }
    }

    @EventListener
    public void recoverFile(FileRecoverEvent event) {
        String filePath = event.getFilePath();
        byte[] recoveryData = event.getImageByteArray();

        File file = new File(BoardDto.ABSOLUTE_PATH + filePath);
        if(!file.exists()) {
            log.info("file is deleted recovery file for url: {}", BoardDto.ABSOLUTE_PATH + filePath);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(recoveryData);
                fileOutputStream.close();
            } catch (Exception e) {
                throw new IllegalArgumentException("event error");
            }
        }
    }

}
