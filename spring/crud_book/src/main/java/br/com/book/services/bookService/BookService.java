package br.com.book.services.bookService;

import br.com.book.models.book.Book;
import br.com.book.repository.book.BookRepository;
import br.com.book.businessException.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {
    final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(Integer user_id) { return bookRepository.findAllByUserId(user_id); }

    public Optional<Book> findById(UUID id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public void existsByBookName(String bookName, Integer userId) throws BusinessException {
        validationBookName(bookName, userId);
    }

    private void validationBookName(String bookName, Integer userId) throws BusinessException {
        int count = bookRepository.countByBookNameAndUserId(bookName, userId);

        if (count > 0) {
            throw new BusinessException("The book: " + bookName + " is already registered for the user");
        }
    }

    public void validationExistsbook(Optional<Book> bookModelOptional) throws BusinessException {
        if(!bookModelOptional.isPresent()) {
            throw new BusinessException("This book does not exist");
        }
    }
}
