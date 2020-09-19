package com.elankeeran.demo.repository;

import com.elankeeran.demo.model.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long> { }