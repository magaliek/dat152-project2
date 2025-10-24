/**
 *
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.BookService;

/**
 * @author tdoy
 */
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/elibrary/api/v1")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<Object> getAllBooks(){

        List<Book> books = bookService.findAll();

        if(books.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<Object> getBook(@PathVariable String isbn) throws BookNotFoundException{

        Book book = bookService.findByISBN(isbn);

        return new ResponseEntity<>(book, HttpStatus.OK);

    }

    @PostMapping("/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book){

        Book nbook = bookService.saveBook(book);

        return new ResponseEntity<>(nbook, HttpStatus.CREATED);
    }

    @GetMapping("/books/{isbn}/authors")
    public ResponseEntity<Set<Author>> getAuthorsOfBookByISBN(@PathVariable String isbn) throws BookNotFoundException {
        Set<Author> auth = bookService.findAuthorsOfBookByISBN(isbn);
        return new ResponseEntity<>(auth, HttpStatus.OK);
    }

    @PutMapping("/books/{isbn}")
    public ResponseEntity<Book> updateBookByISBN(@PathVariable String isbn, @RequestBody Book book) throws BookNotFoundException{
        Book updatedBook = bookService.updateBook(book, isbn);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("/books/{isbn}")
    public ResponseEntity<Void> deleteBookByISBN(@PathVariable String isbn) throws BookNotFoundException{
        bookService.deleteByISBN(isbn);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
