package ru.academits.orlov.phonebookspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeneralResponse {
    private boolean success;
    private String message;

    public static GeneralResponse getSuccessResponse() {
        return new GeneralResponse(true, null);
    }

    public static GeneralResponse getErrorResponse(String message) {
        return new GeneralResponse(false, message);
    }
}
