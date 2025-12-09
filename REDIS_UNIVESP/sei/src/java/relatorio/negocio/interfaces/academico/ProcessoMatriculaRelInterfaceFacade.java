package relatorio.negocio.interfaces.academico;

import java.util.List;
import java.util.Vector;
import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ProcessoMatriculaRelInterfaceFacade {

    public void inicializarOrdenacoesRelatorio();

    public List criarObjeto(String unidadeEnsino, String curso, String situacao) throws Exception;

    public void setOrdenarPor(int intValue);
    
    public Vector getOrdenacoesRelatorio();

    
}
