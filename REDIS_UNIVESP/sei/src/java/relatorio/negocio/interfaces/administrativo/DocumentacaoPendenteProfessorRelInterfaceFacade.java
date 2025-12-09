package relatorio.negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.utilitarias.dominios.Escolaridade;
import relatorio.negocio.comuns.administrativo.ProfessorRelVO;


public interface DocumentacaoPendenteProfessorRelInterfaceFacade {

    List<ProfessorRelVO> consultarDadosGeracaoRelatorio(Integer unidadeEnsino, Integer professor, Integer turma, Integer curso, String escolaridade) throws Exception;
    
}
