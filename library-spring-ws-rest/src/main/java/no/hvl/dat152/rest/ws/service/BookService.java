/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.List;
import java.util.Set;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.BookNotFoundException;
import no.hvl.dat152.rest.ws.exceptions.UpdateBookFailedException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.BookRepository;

/**
 * @author tdoy
 */
@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;
	
	
	public Book saveBook(Book book) {
		
		return bookRepository.save(book);
		
	}
	
	public List<Book> findAll(){
		
		return (List<Book>) bookRepository.findAll();
		
	}
	
	
	public Book findByISBN(String isbn) throws BookNotFoundException {
		
		Book book = bookRepository.findByIsbn(isbn)
				.orElseThrow(() -> new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
		
		return book;
	}
	
	// TODO public Book updateBook(Book book, String isbn)
    public Book updateBook(Book book, String isbn) throws BookNotFoundException {
        return bookRepository.findByIsbn(isbn).map(existingBook -> {
            existingBook.setAuthors(book.getAuthors());
            existingBook.setTitle(book.getTitle());
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
    }
	
	// TODO public List<Book> findAllPaginate(Pageable page)
    public List<Book> findAllPaginate(Pageable page) {
        return bookRepository.findAllPaginate(page.getPageSize(), (int) page.getOffset());
    }
	
	// TODO public Set<Author> findAuthorsOfBookByISBN(String isbn)
    public Set<Author> findAuthorsOfBookByISBN(String isbn) throws BookNotFoundException {
        bookRepository.findByIsbn(isbn).orElseThrow(()->new BookNotFoundException("Book with isbn = "+isbn+" not found!"));
        return bookRepository.findByIsbn(isbn).get().getAuthors();
    }
	
	// TODO public void deleteById(long id)
    public void deleteById(long id) throws BookNotFoundException {
        bookRepository.deleteById(id); //maybe should have checked with ifpresent but then i cant use deletebyid
    }
	
	// TODO public void deleteByISBN(String isbn)
    public void deleteByISBN(String isbn) {
        bookRepository.findByIsbn(isbn).ifPresent(bookRepository::delete);
    }

}
