package nefrapp.restapi.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

//implementazione concreta delle custom queries per l'entity Utente
public class UtenteRepositoryImpl implements UtenteRepositoryCustom{

    public UtenteRepositoryImpl(){}

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public void deleteAllExceptAdmin() {
        String sql = "DELETE FROM utente WHERE authorities != 'ADMIN'";
        jdbcTemplate.execute(sql);
    }
}
