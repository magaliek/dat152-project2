/**
 * 
 */
package no.hvl.dat152.rest.ws.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import no.hvl.dat152.rest.ws.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import no.hvl.dat152.rest.ws.exceptions.AuthorNotFoundException;
import no.hvl.dat152.rest.ws.model.Author;
import no.hvl.dat152.rest.ws.model.Book;
import no.hvl.dat152.rest.ws.repository.AuthorRepository;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author tdoy
 */
@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;  //only did this because it seemed from the bookRepository that there
                                            // was a method perfect for this and meant for this. I thought maybe the teacher wanted me to do this
		
	
	public Author findById(int id) throws AuthorNotFoundException {
		
		Author author = authorRepository.findById(id)
				.orElseThrow(()-> new AuthorNotFoundException("Author with the id: "+id+ "not found!"));
		
		return author;
	}
	
	// TODO public saveAuthor(Author author)
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }
		
	
	// TODO public Author updateAuthor(Author author, int id)
    public Author updateAuthor(Author author, int id) throws AuthorNotFoundException {
        return authorRepository.findById(id).map(existingAuthor -> {
            existingAuthor.setFirstname(author.getFirstname());
            existingAuthor.setLastname(author.getLastname());
            existingAuthor.setBooks(author.getBooks());

            return authorRepository.save(existingAuthor);
        }).orElseThrow(()->new AuthorNotFoundException("Author not found"));
    }
		
	
	// TODO public List<Author> findAll()
    public List<Author> findAllAuthors() {
        List<Author> authors = new ArrayList<Author>();
        authorRepository.findAll().forEach(authors::add);
        return authors;
    }
	
	
	// TODO public void deleteById(int id) throws AuthorNotFoundException
    public void deleteById(int id) throws AuthorNotFoundException {
        authorRepository.findById(id).orElseThrow(() -> new AuthorNotFoundException("author not found"));
        authorRepository.deleteById(id);
    }

	
	// TODO public Set<Book> findBooksByAuthorId(int id)
    public Set<Book> findBooksByAuthorId(int id) {
        return new HashSet<>(bookRepository.findBooksByAuthorId(id));
    }
}
