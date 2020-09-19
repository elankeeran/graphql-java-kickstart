package com.elankeeran.demo.resolver;


import com.elankeeran.demo.model.Author;
import com.elankeeran.demo.model.Book;
import com.elankeeran.demo.repository.AuthorRepository;
import com.elankeeran.demo.repository.BookRepository;
import graphql.kickstart.tools.GraphQLQueryResolver;


public class Query implements GraphQLQueryResolver {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    public Query(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Iterable<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Iterable<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public long countBooks() {
        return bookRepository.count();
    }
    public long countAuthors() {
        return authorRepository.count();
    }
}