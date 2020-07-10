package team.nefrapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.nefrapp.model.Paziente;

@Repository
public interface PazienteRepository extends CrudRepository<Paziente, Integer> {
    boolean existsByCodiceFiscale(String cf);
    Paziente findByCodiceFiscale(String cf);
    void deleteByCodiceFiscale(String cf);
}
