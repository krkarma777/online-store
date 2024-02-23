package com.bulkpurchase.domain.enums;

public enum ErrorPage {
    UNAUTHORIZED(401, "error/401"),
    FORBIDDEN(403, "error/403"),
    NOT_FOUND(404, "error/404"),
    BAD_REQUEST(400, "error/400"),
    INTERNAL_SERVER_ERROR(500, "error/500");

    private final int statusCode;
    private final String viewName;

    ErrorPage(int statusCode, String viewName) {
        this.statusCode = statusCode;
        this.viewName = viewName;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getViewName() {
        return viewName;
    }

    public static String viewByStatusCode(int statusCode) {
        for (ErrorPage page : values()) {
            if (page.getStatusCode() == statusCode) {
                return page.getViewName();
            }
        }
        return INTERNAL_SERVER_ERROR.getViewName();
    }
}
