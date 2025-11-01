package co.edu.umanizales.bookverse.service;

import co.edu.umanizales.bookverse.model.Author;
import co.edu.umanizales.bookverse.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthorService {

    private final IAuthorRepository authorRepository;

    @Autowired
    public AuthorService(IAuthorRepository authorRepository) {
        log.info("Initializing AuthorService with repository: {}", authorRepository.getClass().getName());
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorById(String id) {
        return authorRepository.findById(id);
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public boolean deleteAuthor(String id) {
        if (authorRepository.existsById(id)) {
            authorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsById(String id) {
        return authorRepository.existsById(id);
    }
}
