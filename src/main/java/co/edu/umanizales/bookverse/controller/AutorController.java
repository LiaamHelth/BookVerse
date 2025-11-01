package co.edu.umanizales.bookverse.controller;

import co.edu.umanizales.bookverse.model.Autor;
import co.edu.umanizales.bookverse.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {

    private final AutorService autorService;

    @Autowired
    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @GetMapping
    public ResponseEntity<List<Autor>> getAllAutores() {
        return new ResponseEntity<>(autorService.getAllAutores(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> getAutorById(@PathVariable String id) {
        return autorService.getAutorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Autor> createAutor(@RequestBody Autor autor) {
        try {
            Autor savedAutor = autorService.saveAutor(autor);
            return new ResponseEntity<>(savedAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> updateAutor(@PathVariable String id, @RequestBody Autor autor) {
        if (!autorService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        autor.setId(id);
        return ResponseEntity.ok(autorService.saveAutor(autor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAutor(@PathVariable String id) {
        try {
            if (autorService.deleteAutor(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
