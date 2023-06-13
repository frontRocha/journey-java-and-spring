package br.com.book.repository.book;

import br.com.book.models.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {
    @Query(value = "SELECT COUNT(*) FROM tb_book WHERE book_name = :bookName AND user_id = :userId LIMIT 1", nativeQuery = true)
    int countByBookNameAndUserId(String bookName, Integer userId);

    @Query(value = "SELECT * FROM tb_book WHERE user_id = :user_id", nativeQuery = true)
    List<Book> findAll(Integer user_id);
}
