package by.av.test.testavby.payload.response;

import lombok.Getter;

@Getter
public class JWTSuccessResponse {
    private String token;

    public JWTSuccessResponse(String token) {
        this.token = "Bearer " + token;
    }
}
