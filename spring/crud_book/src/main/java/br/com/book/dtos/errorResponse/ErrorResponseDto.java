package br.com.book.dtos.errorResponse;

import org.springframework.stereotype.Component;

@Component
public class ErrorResponseDto {
    private String messageError;
    private Integer codeError;

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public Integer getCodeError() {
        return codeError;
    }

    public void setCodeError(Integer codeError) {
        this.codeError = codeError;
    }
}
