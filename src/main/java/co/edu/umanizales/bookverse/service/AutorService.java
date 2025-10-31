package co.edu.umanizales.bookverse.service;

import co.edu.umanizales.bookverse.model.Autor;
import co.edu.umanizales.bookverse.repository.IAutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AutorService {

    private final IAutorRepository autorRepository;

    @Autowired
    public AutorService(IAutorRepository autorRepository) {
        log.info("Inicializando AutorService con repositorio: {}", autorRepository.getClass().getName());
        this.autorRepository = autorRepository;
    }

    public List<Autor> getAllAutores() {
        return autorRepository.findAll();
    }

    public Optional<Autor> getAutorById(String id) {
        return autorRepository.findById(id);
    }

    public Autor saveAutor(Autor autor) {
        return autorRepository.save(autor);
    }

    public boolean deleteAutor(String id) {
        if (autorRepository.existsById(id)) {
            autorRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean existsById(String id) {
        return autorRepository.existsById(id);
    }
}
