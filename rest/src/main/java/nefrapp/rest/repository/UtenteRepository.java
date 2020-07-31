package nefrapp.rest.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import nefrapp.rest.model.Utente;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, String> {
    boolean existsByCodiceFiscale(String cf);
    Utente findByCodiceFiscale(String cf);
    void deleteByCodiceFiscale(String cf);
}