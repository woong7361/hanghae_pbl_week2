package pbl.week2.config.exception;

public class MyException extends Exception{
    private Long aLong;

    public MyException(Long aLong) {
        this.aLong = aLong;
    }

}



