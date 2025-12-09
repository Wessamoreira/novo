package relatorio.negocio.interfaces.academico;

import java.util.Date;
import java.util.List;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.academico.AberturaTurmaRelVO;

/**
 *
 * @author Carlos
 */
public interface AberturaTurmaRelInterfaceFacade {

    public List montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception;
    public List consultarTurma(String campoConsultaTurma, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaTurma, UsuarioVO usuarioLogado) throws Exception;
    public List consultarCurso(String campoConsultaCurso, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaCurso, UsuarioVO usuarioLogado) throws Exception;
    public List<AberturaTurmaRelVO> realizarCriacaoOjbRel(Integer turma, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, String situacao, Date dataInicio, Date dataFim) throws Exception;
    public void validarDados(Date dataInicio, Date dataFim, Boolean telaMapaAbertura) throws Exception;
    public String validarDadosSituacaoApresentar(String situacao);
}
