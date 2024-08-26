package com.beachUpSkilling.bookStore.repository;

import com.beachUpSkilling.bookStore.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends CrudRepository<Book, UUID> {
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByTitleIgnoreCase(String title);
}
