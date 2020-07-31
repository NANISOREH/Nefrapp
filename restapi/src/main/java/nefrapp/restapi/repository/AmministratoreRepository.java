package nefrapp.restapi.repository;

import nefrapp.restapi.model.Amministratore;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmministratoreRepository extends CrudRepository<Amministratore, Integer> {
    boolean existsByCodiceFiscale(String cf);

    Amministratore findByCodiceFiscale(String cf);
}