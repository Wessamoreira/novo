package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.RankingTurmaVO;
import negocio.comuns.crm.RankingVO;

/**
 *
 * @author Carlos
 */
public interface RankingInterfaceFacade {
    public List<UnidadeEnsinoVO> montarListaSelectItemUnidadeEnsino(Integer unidadeEnsinoLogado, UsuarioVO usuarioVO) throws Exception;
    public List consultarCurso(String valorConsultaCurso, String campoConsultaCurso, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
    public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer unidadeEnsino, Integer curso, UsuarioVO usuarioVO) throws Exception;
    public List<RankingTurmaVO> consultar(Integer unidadeEnsino, Integer curso, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception;
    public List<RankingVO> consultarRanking(RankingTurmaVO obj, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception;
    public List<RankingVO> consultarRankingConsultorPorConsultorMes(Integer consultor, String valorConsultaMes, String valorOrdenarPor, UsuarioVO usuarioVO);
	List<RankingTurmaVO> consultarRankingTurma(Integer unidadeEnsino, Integer curso, Integer turma, String valorConsultaMes, UsuarioVO usuarioVO) throws Exception;

}
