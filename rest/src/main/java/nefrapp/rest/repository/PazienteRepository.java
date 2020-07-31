package nefrapp.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nefrapp.rest.model.Paziente;

@Repository
public interface PazienteRepository extends CrudRepository<Paziente, Integer> {
    boolean existsByCodiceFiscale(String cf);
    Paziente findByCodiceFiscale(String cf);
    void deleteByCodiceFiscale(String cf);
}
