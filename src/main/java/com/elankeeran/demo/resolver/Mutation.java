package com.elankeeran.demo.resolver;


import com.elankeeran.demo.exception.BookNotFoundException;
import com.elankeeran.demo.model.Author;
import com.elankeeran.demo.model.Book;
import com.elankeeran.demo.repository.AuthorRepository;
import com.elankeeran.demo.repository.BookRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Mutation implements GraphQLMutationResolver {
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;

    private  ResourceLoader resourceLoader;

    public Mutation(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    public Author newAuthor(String firstName, String lastName) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);

        authorRepository.save(author);

        return author;
    }

    public Author updateAvatar(Part avatar, DataFetchingEnvironment environment) throws IOException {
        Author author = new Author();
        Part actualAvatar = environment.getArgument("avatar");
        BufferedImage actualImage = ImageIO.read(actualAvatar.getInputStream());
        BufferedImage scaledImage = scale(actualImage);
        String type = getType(actualAvatar.getContentType());
        File location = getLocation("foo." + type);
        ImageIO.write(scaledImage, type, location);
        //return "http://localhost:8080/avatar/foo." + type;
        return author;
    }

    public Book newBook(String title, String isbn, Integer pageCount, Long authorId) {
        Book book = new Book();
        book.setAuthor(new Author(authorId));
        book.setTitle(title);
        book.setIsbn(isbn);
        book.setPageCount(pageCount != null ? pageCount : 0);

        bookRepository.save(book);

        return book;
    }

    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }

    public Book updateBookPageCount(Integer pageCount, Long id) {
        Book book = bookRepository.findById(id).get();
        if(book == null) {
            throw new BookNotFoundException("The book to be updated was not found", id);
        }
        book.setPageCount(pageCount);

        bookRepository.save(book);

        return book;
    }


    private BufferedImage scale(BufferedImage image) {
        int maxWidth = 200;
        int maxHeight = 200;
        if (image.getWidth() >= image.getHeight() && image.getWidth() > maxWidth) {
            int newHeight = (int) (image.getHeight() * ((float) maxWidth / image.getWidth()));
            return getBufferered(image.getScaledInstance(maxWidth, newHeight, BufferedImage.SCALE_SMOOTH), maxWidth, newHeight);
        } else if (image.getHeight() > image.getWidth() && image.getHeight() > maxHeight) {
            int newWidth = (int) (image.getWidth() * ((float) maxHeight / image.getHeight()));
            return getBufferered(image.getScaledInstance(newWidth, maxHeight, BufferedImage.SCALE_SMOOTH), newWidth, maxHeight);
        } else {
            return image;
        }
    }

    private BufferedImage getBufferered(Image image, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.createGraphics().drawImage(image, 0, 0, null);
        return bufferedImage;
    }

    private File getLocation(String filename) throws IOException {
        File directory = resourceLoader.getResource("file:./filestorage/").getFile();
        return new File(directory, filename);
    }

    private String getType(String mimetype) {
        MediaType mediaType = MediaType.parseMediaType(mimetype);
        if (!isImage(mediaType)) throw new RuntimeException("Invalid content-type");
        else if (isJpeg(mediaType)) return "jpg";
        else return mediaType.getSubtype();
    }

    private boolean isJpeg(MediaType mediaType) {
        return "jpeg".equalsIgnoreCase(mediaType.getSubtype());
    }

    private boolean isImage(MediaType mediaType) {
        return "image".equalsIgnoreCase(mediaType.getType());
    }
}