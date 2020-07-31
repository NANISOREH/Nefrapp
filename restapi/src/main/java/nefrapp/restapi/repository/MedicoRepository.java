package nefrapp.restapi.repository;

import nefrapp.restapi.model.Medico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends CrudRepository<Medico, Integer> {
    boolean existsByCodiceFiscale(String cf);

    Medico findByCodiceFiscale(String cf);

}