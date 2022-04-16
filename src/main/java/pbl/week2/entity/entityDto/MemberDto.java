package pbl.week2.entity.entityDto;

import lombok.*;

import javax.validation.constraints.NotBlank;

public class MemberDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Register {
        //추후 보안 강화 => " fsd" 이런 문자열 들어올 수 있음
        @NotBlank
        private String username;
        @NotBlank
        private String pw;
        @NotBlank
        private String nickname;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Login {
        //추후 보안 강화 => " fsd" 이런 문자열 들어올 수 있음
        @NotBlank
        private String username;
        @NotBlank
        private String pw;
    }

    @Data
    public static class Session {
        private Long id;
        private String username;
        private String password;
    }

}
