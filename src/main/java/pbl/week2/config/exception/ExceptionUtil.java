package pbl.week2.config.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import pbl.week2.entity.dto.ResultMsg;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public final class ExceptionUtil {

    public static void makeResponseInFilter(HttpServletResponse response, ResultMsg error) throws IOException {
        try (OutputStream os = response.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, error);
            os.flush();
        }
    }
}
