package nefrapp.restapi.repository;

import nefrapp.restapi.model.Utente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface UtenteRepository extends CrudRepository<Utente, String> {
    boolean existsByCodiceFiscale(String cf);

    Utente findByCodiceFiscale(String cf);

    void deleteByCodiceFiscale(String cf);
}