package co.edu.umanizales.bookverse.repository.csv;

import co.edu.umanizales.bookverse.model.Author;
import co.edu.umanizales.bookverse.model.Book;
import co.edu.umanizales.bookverse.repository.IAuthorRepository;
import co.edu.umanizales.bookverse.repository.IBookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class BookCsvRepository implements IBookRepository {
    
    private final String filePath;
    private final IAuthorRepository authorRepository;
    
    @Autowired
    public BookCsvRepository(
            @Value("${bookverse.data.path:./data}") String dataPath,
            IAuthorRepository authorRepository) {
        this.filePath = dataPath + "/libros.csv";
        this.authorRepository = authorRepository;
        ensureDirectoryExists();
    }
    
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            log.error("Error initializing books data file", e);
            throw new RuntimeException("Error initializing data file", e);
        }
    }
    
    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    try {
                        books.add(parseLine(line));
                    } catch (Exception e) {
                        log.error("Error parsing book line: {}", line, e);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Error reading books file", e);
            throw new RuntimeException("Error reading books file", e);
        }
        return books;
    }
    
    @Override
    public Optional<Book> findById(String id) {
        return findAll().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Book save(Book book) {
        List<Book> books = findAll();
        
        if (book.getId() == null || book.getId().isEmpty()) {
            book.setId(UUID.randomUUID().toString());
            books.add(book);
            log.info("Creating new book with ID: {}", book.getId());
        } else {
            boolean found = false;
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getId().equals(book.getId())) {
                    books.set(i, book);
                    found = true;
                    log.info("Updating book with ID: {}", book.getId());
                    break;
                }
            }
            if (!found) {
                books.add(book);
                log.info("Adding book with existing ID: {}", book.getId());
            }
        }
        
        saveAll(books);
        return book;
    }
    
    @Override
    public void deleteById(String id) {
        List<Book> books = findAll();
        boolean removed = books.removeIf(b -> b.getId().equals(id));
        if (removed) {
            log.info("Deleted book with ID: {}", id);
            saveAll(books);
        } else {
            log.warn("Book with ID {} not found for deletion", id);
        }
    }
    
    @Override
    public boolean existsById(String id) {
        return findAll().stream().anyMatch(b -> b.getId().equals(id));
    }
    
    @Override
    public List<Book> findByAuthorId(String authorId) {
        return findAll().stream()
                .filter(b -> b.getAuthorId() != null && b.getAuthorId().equals(authorId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> findByGenre(String genre) {
        return findAll().stream()
                .filter(b -> b.getGenre() != null && b.getGenre().equalsIgnoreCase(genre))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> findAvailableBooks() {
        return findAll().stream()
                .filter(Book::isAvailable)
                .collect(Collectors.toList());
    }
    
    private void saveAll(List<Book> books) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.println(toCSV(book));
            }
        } catch (IOException e) {
            log.error("Error saving books", e);
            throw new RuntimeException("Error saving books", e);
        }
    }
    
    private Book parseLine(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        
        Book book = new Book();
        book.setId(parts[0]);
        book.setIsbn(parts.length > 1 ? parts[1] : "");
        book.setTitle(parts.length > 2 ? parts[2] : "");
        
        if (parts.length > 3 && !parts[3].isEmpty()) {
            Optional<Author> author = authorRepository.findById(parts[3]);
            book.setAuthor(author.orElseGet(() -> {
                Author a = new Author();
                a.setId(parts[3]);
                return a;
            }));
        }
        
        book.setPublisher(parts.length > 4 ? parts[4] : "");
        
        if (parts.length > 5 && !parts[5].isEmpty()) {
            try {
                book.setPublicationDate(LocalDate.parse(parts[5]));
            } catch (Exception e) {
                log.warn("Error parsing publication date: {}", parts[5]);
            }
        }
        
        book.setGenre(parts.length > 6 ? parts[6] : "");
        
        if (parts.length > 7 && !parts[7].isEmpty()) {
            try {
                book.setPageCount(Integer.parseInt(parts[7]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing page count: {}", parts[7]);
            }
        }
        
        if (parts.length > 8 && !parts[8].isEmpty()) {
            try {
                book.setPrice(Double.parseDouble(parts[8]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing price: {}", parts[8]);
            }
        }
        
        if (parts.length > 9 && !parts[9].isEmpty()) {
            try {
                book.setStock(Integer.parseInt(parts[9]));
            } catch (NumberFormatException e) {
                log.warn("Error parsing stock: {}", parts[9]);
            }
        }
        
        if (parts.length > 10) {
            String description = parts[10].replaceAll("^\"|\"$", "").replace("\"\"", "\"");
            book.setDescription(description);
        }
        
        book.setLanguage(parts.length > 11 ? parts[11] : "");
        
        return book;
    }
    
    private String toCSV(Book book) {
        String description = book.getDescription() != null ? 
            "\"" + book.getDescription().replace("\"", "\"\"") + "\"" : "";
            
        return String.join(",",
            escapeCsv(book.getId()),
            escapeCsv(book.getIsbn()),
            escapeCsv(book.getTitle()),
            escapeCsv(book.getAuthorId()),
            escapeCsv(book.getPublisher()),
            book.getPublicationDate() != null ? book.getPublicationDate().toString() : "",
            escapeCsv(book.getGenre()),
            String.valueOf(book.getPageCount()),
            String.format(java.util.Locale.US, "%.2f", book.getPrice()),
            String.valueOf(book.getStock()),
            description,
            escapeCsv(book.getLanguage())
        );
    }
    
    private String escapeCsv(String input) {
        if (input == null) {
            return "";
        }
        if (input.contains(",") || input.contains("\"") || input.contains("\n")) {
            return "\"" + input.replace("\"", "\"\"") + "\"";
        }
        return input;
    }
}
