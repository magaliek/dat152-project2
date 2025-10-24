/**
 *
 */
package no.hvl.dat152.rest.ws.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.service.AuthorService;

/**
 *
 */
@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/elibrary/api/v1")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public ResponseEntity<List<Author>> getAllAuthor() {
        List<Author> authors = authorService.findAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/authors/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable int id) throws AuthorNotFoundException {
        Author author = authorService.findById(id);
        return ResponseEntity.ok(author);
    }

    @GetMapping("/authors/{id}/books")
    public ResponseEntity<Set<Book>> getBooksByAuthorId(@PathVariable int id) throws AuthorNotFoundException {
        Set<Book> books = authorService.findBooksByAuthorId(id);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/authors")
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author createdAuthor = authorService.saveAuthor(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuthor);
    }

    @PutMapping("/authors/{id}")
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author, @PathVariable int id) throws AuthorNotFoundException {
        Author updatedAuthor = authorService.updateAuthor(author, id);
        return ResponseEntity.ok(updatedAuthor);
    }

}
