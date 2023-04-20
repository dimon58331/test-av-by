package by.av.test.testavby.payload.response;

import lombok.Getter;

@Getter
public class JWTSuccessResponse {
    private boolean success;
    private String token;

    public JWTSuccessResponse(boolean success, String token) {
        this.token = token;
        this.success = success;
    }
}
