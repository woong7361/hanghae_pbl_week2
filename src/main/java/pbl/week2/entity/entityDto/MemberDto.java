package pbl.week2.entity.entityDto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberDto {

    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class Register {
        //추후 보안 강화 => " fsd" 이런 문자열 들어올 수 있음
        @Pattern(regexp = "[A-Za-z0-9]+")
        @Size(min = 3)
        private String username;
        @NotBlank
        @Size(min = 4)
        private String pw;
        @Pattern(regexp = "[A-Za-z0-9]+")
        @Size(min = 3)
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
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Session {
        private Long id;
        private String username;
        private String password;

        public Session(Login loginMember) {
            this.username = loginMember.username;
            this.password = loginMember.getPw();
        }
    }

}
