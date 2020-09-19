package com.elankeeran.demo.resolver;


import com.elankeeran.demo.model.Author;
import com.elankeeran.demo.model.Book;
import com.elankeeran.demo.repository.AuthorRepository;
import graphql.kickstart.tools.GraphQLResolver;


public class BookResolver implements GraphQLResolver<Book> {
    private AuthorRepository authorRepository;

    public BookResolver(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Author getAuthor(Book book) {
        return authorRepository.findById(book.getAuthor().getId()).get();
    }
}