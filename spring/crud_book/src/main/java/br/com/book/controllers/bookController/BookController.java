package br.com.book.controllers.bookController;

import br.com.book.dtos.bookDto.BookDto;
import br.com.book.dtos.errorResponseDto.ErrorResponseDto;
import br.com.book.models.book.Book;
import br.com.book.models.user.User;
import br.com.book.repository.user.UserRepository;
import br.com.book.services.bookService.BookService;
import br.com.book.businessException.BusinessException;
import br.com.book.services.verifyUserIdentifyService.VerifyUserIdentityService;
import br.com.book.utils.jwtDecode.JWTDecode;
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

    private final BookService bookService;
    private final UserRepository userRepository;
    private final ErrorResponseDto errorResponse;
    private final JWTDecode jwtDecode;
    private final VerifyUserIdentityService verifyUserIdentityService;

    public BookController(BookService bookService,
                          UserRepository userRepository,
                          ErrorResponseDto errorResponse,
                          JWTDecode jwtDecode,
                          VerifyUserIdentityService verifyUserIdentityService) {
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.errorResponse = errorResponse;
        this.jwtDecode = jwtDecode;
        this.verifyUserIdentityService = verifyUserIdentityService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(@RequestHeader("Authorization") String authorizationHeader) {
        String token = jwtDecode.extractTokenFromAuthorizationHeader(authorizationHeader);
        Integer userId = jwtDecode.extractUserIdFromToken(token);

        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneBook(@PathVariable(value = "id") UUID id,
                                             @RequestHeader("Authorization") String authorizationHeader) {
        Optional<Book> bookModel = bookService.findById(id);

        try {
            bookService.validationExistsbook(bookModel);
            String token = jwtDecode.extractTokenFromAuthorizationHeader(authorizationHeader);
            Integer userId = jwtDecode.extractUserIdFromToken(token);

            verifyUserIdentityService.verifyUser(bookModel, userId);

            return ResponseEntity.status(HttpStatus.OK).body(bookModel.get());
        } catch(BusinessException err) {
            setMessageError(err.getMessage());
            setCodeError(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveNewBook(@RequestBody @Valid BookDto bookDto,
                                              @RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = jwtDecode.extractTokenFromAuthorizationHeader(authorizationHeader);
            Integer userId = jwtDecode.extractUserIdFromToken(token);

            bookService.existsByBookName(bookDto.getBookName(), userId);

            Book book = createBookModel(bookDto);
            User user = getUserModel(userId);
            book.setUser(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(bookService.save(book));
        } catch(BusinessException err) {
            setMessageError(err.getMessage());
            setCodeError(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable(value = "id") UUID id,
                                             @RequestHeader("Authorization") String authorizationHeader) {
        Optional<Book> bookModelOptional = bookService.findById(id);

        try {
            String token = jwtDecode.extractTokenFromAuthorizationHeader(authorizationHeader);
            Integer userId = jwtDecode.extractUserIdFromToken(token);

            bookService.validationExistsbook(bookModelOptional);
            verifyUserIdentityService.verifyUser(bookModelOptional, userId);
            bookService.delete(bookModelOptional.get());

            return ResponseEntity.status(HttpStatus.CREATED).body("successfully deleted book");
        } catch(BusinessException err) {
            setMessageError(err.getMessage());
            setCodeError(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editBook(@PathVariable(value = "id") UUID id,
                                           @RequestBody @Valid BookDto bookDto,
                                           @RequestHeader("Authorization") String authorizationHeader) {
        try {
            Optional<Book> bookModelOptional = bookService.findById(id);

            String token = jwtDecode.extractTokenFromAuthorizationHeader(authorizationHeader);
            Integer userId = jwtDecode.extractUserIdFromToken(token);

            bookService.validationExistsbook(bookModelOptional);
            verifyUserIdentityService.verifyUser(bookModelOptional, userId);
            bookService.existsByBookName(bookDto.getBookName(), userId);

            Book book = createBookModel(bookDto);
            setIdBook(bookModelOptional, book);

            return ResponseEntity.status(HttpStatus.OK).body(bookService.save(book));
        } catch(BusinessException err) {
            setMessageError(err.getMessage());
            setCodeError(HttpStatus.CONFLICT.value());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    private User getUserModel(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Book createBookModel(BookDto bookDto) {
        var bookModel = new Book();
        BeanUtils.copyProperties(bookDto, bookModel);
        bookModel.setRegistrationDate(LocalDateTime.now(ZoneId.of("UTC")));
        return bookModel;
    }

    private void setMessageError(String message) {
        errorResponse.setMessageError(message);
    }

    private void setCodeError(Integer code) {
        errorResponse.setCodeError(code);
    }

    private void setIdBook(Optional<Book> bookModelOptional, Book book) {
        book.setId(bookModelOptional.get().getId());
    }
}
