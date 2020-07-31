package nefrapp.rest.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nefrapp.rest.model.Amministratore;

@Repository
public interface AmministratoreRepository extends CrudRepository<Amministratore, Integer> {
    boolean existsByCodiceFiscale(String cf);
    Amministratore findByCodiceFiscale(String cf);
}