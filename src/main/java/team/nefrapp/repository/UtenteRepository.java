package team.nefrapp.repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.nefrapp.model.Utente;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Integer> {
    boolean existsByCodiceFiscale(String cf);
    Utente findByCodiceFiscale(String cf);
    void deleteByCodiceFiscale(String cf);
}