package toyProject.snow.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        HttpStatus status,
        String message,
        T data

){
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, null, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(HttpStatus.CREATED, null, data);
    }

    public static <T> ApiResponse<T> accepted(T data) {
        return new ApiResponse<>(HttpStatus.ACCEPTED, null, data);
    }

    public static <T> ApiResponse<T> error(String message){
        return new ApiResponse<>(HttpStatus.BAD_REQUEST, message, null);
    }

}

