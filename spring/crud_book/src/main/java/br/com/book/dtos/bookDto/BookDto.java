package br.com.book.dtos.bookDto;

import jakarta.validation.constraints.NotBlank;

public class BookDto {
    @NotBlank
    private String bookName;
    @NotBlank
    private String price;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
