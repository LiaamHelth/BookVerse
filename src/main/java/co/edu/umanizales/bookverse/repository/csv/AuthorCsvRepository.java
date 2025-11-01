package co.edu.umanizales.bookverse.repository.csv;

import co.edu.umanizales.bookverse.model.Author;
import co.edu.umanizales.bookverse.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Repository
public class AuthorCsvRepository implements IAuthorRepository {
    
    private final String filePath;
    
    public AuthorCsvRepository(@Value("${bookverse.data.path:./data}") String dataPath) {
        this.filePath = dataPath + "/autores.csv";
        ensureDirectoryExists();
    }
    
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(filePath).getParent());
            if (!Files.exists(Paths.get(filePath))) {
                Files.createFile(Paths.get(filePath));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error initializing data file", e);
        }
    }
    
    @Override
    public List<Author> findAll() {
        List<Author> authors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    authors.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading authors file", e);
        }
        return authors;
    }
    
    @Override
    public Optional<Author> findById(String id) {
        return findAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Author save(Author author) {
        List<Author> authors = findAll();
        
        if (author.getId() == null || author.getId().isEmpty()) {
            // New author
            author.setId(UUID.randomUUID().toString());
            authors.add(author);
        } else {
            // Update existing author
            for (int i = 0; i < authors.size(); i++) {
                if (authors.get(i).getId().equals(author.getId())) {
                    authors.set(i, author);
                    break;
                }
            }
        }
        
        saveAll(authors);
        return author;
    }
    
    @Override
    public void deleteById(String id) {
        List<Author> authors = findAll();
        authors.removeIf(a -> a.getId().equals(id));
        saveAll(authors);
    }
    
    @Override
    public boolean existsById(String id) {
        return findAll().stream().anyMatch(a -> a.getId().equals(id));
    }
    
    private void saveAll(List<Author> authors) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Author author : authors) {
                writer.println(toCSV(author));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error saving authors", e);
        }
    }
    
    private Author parseLine(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        Author author = new Author();
        author.setId(parts[0]);
        author.setName(parts[1]);
        author.setLastName(parts[2]);
        author.setNationality(parts[3]);
        if (parts.length > 4 && !parts[4].isEmpty()) {
            author.setBirthDate(LocalDate.parse(parts[4]));
        }
        if (parts.length > 5) {
            String biography = parts[5].replaceAll("^\"|\"$", "");
            author.setBiography(biography);
        }
        if (parts.length > 6) {
            author.setEmail(parts[6]);
        }
        return author;
    }
    
    private String toCSV(Author author) {
        String biography = author.getBiography() != null ? 
            "\"" + author.getBiography().replace("\"", "\"\"") + "\"" : "";
            
        return String.join(",",
            escapeCsv(author.getId()),
            escapeCsv(author.getName()),
            escapeCsv(author.getLastName()),
            escapeCsv(author.getNationality()),
            author.getBirthDate() != null ? author.getBirthDate().toString() : "",
            biography,
            escapeCsv(author.getEmail())
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
