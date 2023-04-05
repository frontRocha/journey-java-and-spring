package br.com.book.services;

import br.com.book.models.BookModel;
import br.com.book.repositories.BookRepository;
import businessException.BusinessException;
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

    public List<BookModel> findAll() { return bookRepository.findAll(); }

    public Optional<BookModel> findById(UUID id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public BookModel save(BookModel bookModel) {
        return bookRepository.save(bookModel);
    }

    public void delete(BookModel bookModel) {
        bookRepository.delete(bookModel);
    }

    public void existsByBookName(String bookName) throws BusinessException {
        validationBookName(bookName);
    }

    public void validationBookName(String bookName) throws BusinessException {
        if(bookRepository.existsByBookName(bookName)) {
            throw new BusinessException("The book: " + bookName + " is already in use:");
        }
    }

    public void validationExistsbook(Optional<BookModel> bookModelOptional) throws BusinessException {
        if(!bookModelOptional.isPresent()) {
            throw new BusinessException("This book does not exist");
        }
    }
}
