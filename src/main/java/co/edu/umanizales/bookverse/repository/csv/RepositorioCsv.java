package co.edu.umanizales.bookverse.repository.csv;

import co.edu.umanizales.bookverse.model.Autor;
import co.edu.umanizales.bookverse.repository.IAutorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Repository
public class RepositorioCsv implements IAutorRepository {
    
    private final String filePath;
    
    public RepositorioCsv(@Value("${bookverse.data.path:./data}") String dataPath) {
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
            throw new RuntimeException("Error al inicializar el archivo de datos", e);
        }
    }
    
    @Override
    public List<Autor> findAll() {
        List<Autor> autores = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    autores.add(parseLine(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo de autores", e);
        }
        return autores;
    }
    
    @Override
    public Optional<Autor> findById(String id) {
        return findAll().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public Autor save(Autor autor) {
        List<Autor> autores = findAll();
        
        if (autor.getId() == null || autor.getId().isEmpty()) {
            // Nuevo autor
            autor.setId(UUID.randomUUID().toString());
            autores.add(autor);
        } else {
            // Actualizar autor existente
            for (int i = 0; i < autores.size(); i++) {
                if (autores.get(i).getId().equals(autor.getId())) {
                    autores.set(i, autor);
                    break;
                }
            }
        }
        
        saveAll(autores);
        return autor;
    }
    
    @Override
    public void deleteById(String id) {
        List<Autor> autores = findAll();
        autores.removeIf(a -> a.getId().equals(id));
        saveAll(autores);
    }
    
    @Override
    public boolean existsById(String id) {
        return findAll().stream().anyMatch(a -> a.getId().equals(id));
    }
    
    private void saveAll(List<Autor> autores) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Autor autor : autores) {
                writer.println(toCSV(autor));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar los autores", e);
        }
    }
    
    private Autor parseLine(String line) {
        String[] parts = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        Autor autor = new Autor();
        autor.setId(parts[0]);
        autor.setNombre(parts[1]);
        autor.setApellido(parts[2]);
        autor.setNacionalidad(parts[3]);
        if (parts.length > 4 && !parts[4].isEmpty()) {
            autor.setFechaNacimiento(LocalDate.parse(parts[4]));
        }
        if (parts.length > 5) {
            String biografia = parts[5].replaceAll("^\"|\"$", "");
            autor.setBiografia(biografia);
        }
        if (parts.length > 6) {
            autor.setEmail(parts[6]);
        }
        return autor;
    }
    
    private String toCSV(Autor autor) {
        String biografia = autor.getBiografia() != null ? 
            "\"" + autor.getBiografia().replace("\"", "\"\"") + "\"" : "";
            
        return String.join(",",
            escapeCsv(autor.getId()),
            escapeCsv(autor.getNombre()),
            escapeCsv(autor.getApellido()),
            escapeCsv(autor.getNacionalidad()),
            autor.getFechaNacimiento() != null ? autor.getFechaNacimiento().toString() : "",
            biografia,
            escapeCsv(autor.getEmail())
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
