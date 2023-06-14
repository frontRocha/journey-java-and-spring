package br.com.book.repository.book;

import br.com.book.models.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    int countByBookNameAndUserId(String bookName, Integer userId);

    List<Book> findAllByUserId(Integer userId);
}
