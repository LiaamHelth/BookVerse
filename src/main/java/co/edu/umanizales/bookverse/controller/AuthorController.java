package co.edu.umanizales.bookverse.controller;

import co.edu.umanizales.bookverse.model.Author;
import co.edu.umanizales.bookverse.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@Tag(name = "Authors", description = "API para gestión de autores")
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los autores", description = "Retorna la lista completa de autores registrados")
    @ApiResponse(responseCode = "200", description = "Lista de autores retornada exitosamente")
    public ResponseEntity<List<Author>> getAllAuthors() {
        return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener autor por ID", description = "Retorna un autor específico basado en su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor encontrado"),
        @ApiResponse(responseCode = "404", description = "Autor no encontrado")
    })
    public ResponseEntity<Author> getAuthorById(
            @Parameter(description = "ID único del autor", required = true) @PathVariable String id) {
        return authorService.getAuthorById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        try {
            Author savedAuthor = authorService.saveAuthor(author);
            return new ResponseEntity<>(savedAuthor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable String id, @RequestBody Author author) {
        if (!authorService.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        author.setId(id);
        return ResponseEntity.ok(authorService.saveAuthor(author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteAuthor(@PathVariable String id) {
        try {
            if (authorService.deleteAuthor(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
