package pbl.week2.event;

import lombok.Getter;

@Getter
public class FileDeleteEvent {

    private String filePath;

    public FileDeleteEvent(String filePath) {
        this.filePath = filePath;
    }
}
