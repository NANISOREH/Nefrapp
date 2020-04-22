package team.nefrapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import team.nefrapp.model.Medico;
import team.nefrapp.model.Paziente;
import team.nefrapp.model.Utente;

@Repository
public interface MedicoRepository extends CrudRepository<Medico, Integer> {
    boolean existsByCodiceFiscale(String cf);
    Medico findByCodiceFiscale(String cf);
}