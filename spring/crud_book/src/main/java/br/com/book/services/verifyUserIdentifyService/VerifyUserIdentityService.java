package br.com.book.services.verifyUserIdentifyService;

import br.com.book.businessException.BusinessException;
import br.com.book.models.book.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerifyUserIdentityService {

    public void verifyUser(Optional<Book> bookModel, Integer userId) throws BusinessException {
        if(bookModel.orElse(null).getUser().getId() != userId) {
            throw new BusinessException("This book does not exist");
        }
    }
}
