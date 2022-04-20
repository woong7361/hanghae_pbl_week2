package pbl.week2.event;

import lombok.Getter;

@Getter
public class FileRecoverEvent {
    private String filePath;
    private byte[] imageByteArray;

    public FileRecoverEvent(String filePath, byte[] imageByteArray) {
        this.filePath = filePath;
        this.imageByteArray = imageByteArray;
    }
}
