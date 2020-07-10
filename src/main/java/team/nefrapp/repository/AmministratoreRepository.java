package team.nefrapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.nefrapp.model.Amministratore;

@Repository
public interface AmministratoreRepository extends CrudRepository<Amministratore, Integer> {
    boolean existsByCodiceFiscale(String cf);
    Amministratore findByCodiceFiscale(String cf);
}