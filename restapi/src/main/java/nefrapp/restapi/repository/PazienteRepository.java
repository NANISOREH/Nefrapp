package nefrapp.restapi.repository;

import nefrapp.restapi.model.Paziente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PazienteRepository extends CrudRepository<Paziente, Integer> {
    boolean existsByCodiceFiscale(String cf);

    Paziente findByCodiceFiscale(String cf);

    void deleteByCodiceFiscale(String cf);
}
