package relatorio.negocio.interfaces.avaliacaoInst;

import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalPorSinteticoPorCursoVO;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;

import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalAnaliticoRelVO;
import relatorio.negocio.comuns.avaliacaoInst.AvaliacaoInstitucionalPorTurmaSinteticoVO;

public interface AvaliacaoInstitucionalAnaliticoRelInterfaceFacade {

	List<AvaliacaoInstitucionalAnaliticoRelVO> realizarGeracaoRelatorioAnalitico(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, String ordenarPor, Date dataInicio, Date dataFinal, Boolean utilizarListagemRespondente, UsuarioVO usuarioVO, Boolean considerarTurmaAvaliacaoInstitucional) throws Exception;

	void realizarEnvioEmail(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, List<AvaliacaoInstitucionalAnaliticoRelVO> avaliacaoInstitucionalAnaliticoRelVOs, PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuarioVO) throws Exception;

	List<AvaliacaoInstitucionalPorTurmaSinteticoVO> consultarRelatorioSintetico(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, Date dataInicio, Date dataFinal, UsuarioVO usuarioVO) throws Exception;
	
	public List<AvaliacaoInstitucionalPorSinteticoPorCursoVO> consultarRelatorioSinteticoPorCurso(Integer unidadeEnsino, AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, Integer curso, Integer turno, Integer turma, String situacaoResposta, Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception;

	StringBuilder getSqlCondicaoWhereCurso(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String campoComparacao, String andOr);

	StringBuilder getSqlCondicaoWhereUnidadeEnsino(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO, String campoComparacao, String andOr);
}
