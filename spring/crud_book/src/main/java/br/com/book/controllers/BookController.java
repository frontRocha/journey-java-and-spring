package br.com.book.controllers;

import br.com.book.dtos.BookDto;
import br.com.book.models.BookModel;
import br.com.book.services.BookService;
import businessException.BusinessException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/library")
public class BookController {

    final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookModel>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneBook(@PathVariable(value = "id") UUID id) {
        Optional<BookModel> bookModelOptional = bookService.findById(id);

        try {
            bookService.validationExistsbook(bookModelOptional);

            return ResponseEntity.status(HttpStatus.OK).body(bookModelOptional.get());
        } catch(BusinessException err) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveNewBook(@RequestBody @Valid BookDto bookDto) {
        try {
            bookService.existsByBookName(bookDto.getBookName());

            var bookModel = new BookModel();
            BeanUtils.copyProperties(bookDto, bookModel);
            bookModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));

            return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(bookModel));
        } catch(BusinessException err) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable(value = "id") UUID id) {
        Optional<BookModel> bookModelOptional = bookService.findById(id);

        try {
            bookService.validationExistsbook(bookModelOptional);
            bookService.delete(bookModelOptional.get());

            return ResponseEntity.status(HttpStatus.CREATED).body("successfully deleted book");
        } catch(BusinessException err) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editBook(@PathVariable(value = "id") UUID id, @RequestBody @Valid BookDto bookDto) {
        Optional<BookModel> bookModelOptional = bookService.findById(id);

        try {
            bookService.validationExistsbook(bookModelOptional);
            bookService.existsByBookName(bookDto.getBookName());

            var bookModel = new BookModel();
            BeanUtils.copyProperties(bookDto, bookModel);
            bookModel.setId(bookModelOptional.get().getId());
            bookModel.setRegistrationDate(bookModelOptional.get().getRegistrationDate());

            return ResponseEntity.status(HttpStatus.OK).body(bookService.save(bookModel));
        } catch(BusinessException err) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err.getMessage());
        }
    }
}
