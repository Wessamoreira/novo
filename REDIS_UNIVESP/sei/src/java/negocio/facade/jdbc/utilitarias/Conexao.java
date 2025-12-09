package negocio.facade.jdbc.utilitarias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service 
@Scope("singleton") 
public class Conexao {
	
    private  JdbcTemplate jdbcTemplate;

    public  JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplates) {
        this.jdbcTemplate = jdbcTemplates;
    }
    
}
