package com.elankeeran.demo.repository;

import com.elankeeran.demo.model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> { }