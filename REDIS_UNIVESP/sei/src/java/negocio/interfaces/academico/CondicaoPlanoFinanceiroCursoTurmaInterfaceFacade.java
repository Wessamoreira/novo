package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CondicaoPlanoFinanceiroCursoTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;


public interface CondicaoPlanoFinanceiroCursoTurmaInterfaceFacade {
    
    public void persistir(CondicaoPlanoFinanceiroCursoTurmaVO obj) throws Exception;

    public void incluirCondicaoPlanoFinanceiroCursoTurma(CondicaoPagamentoPlanoFinanceiroCursoVO obj) throws Exception;
    
    public void alterarCondicaoPlanoFinanceiroCursoTurma(CondicaoPagamentoPlanoFinanceiroCursoVO obj) throws Exception;
    
    List<CondicaoPlanoFinanceiroCursoTurmaVO> consultarPorCondicaoPlanoFinanceiroCurso(Integer condicaoPlanoFinanceiroCurso) throws Exception;

    List<CondicaoPlanoFinanceiroCursoTurmaVO> consultarDadosParaCriacaoCondicaoPlanoFinanceiroCursoTurma(Integer turma) throws Exception;

    Double consultarValorCondicaoPlanoFinanceiroCursoTurma(Integer condicaoPlanoFinanceiroCurso, Integer disciplina, UsuarioVO usuario) throws Exception;

}
