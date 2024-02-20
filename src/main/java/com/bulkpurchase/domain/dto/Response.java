package com.bulkpurchase.domain.dto;

public class Response {

    private boolean success;
    private String message;
    private Object isFavorited;

    // 찜하기 토글 결과 전용 생성자
    public Response(boolean isFavorited) {
        this.success = true; // 이 경우 항상 성공으로 간주
        this.message = isFavorited ? "상품을 찜했습니다." : "찜하기를 취소했습니다.";
        this.isFavorited = isFavorited; // 찜하기 상태를 나타내는 불리언 값
    }

    // Getter 및 Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getIsFavorited() {
        return isFavorited;
    }

    public void setIsFavorited(Object isFavorited) {
        this.isFavorited = isFavorited;
    }
}
