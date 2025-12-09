package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RenegociacaoRelVO;

/**
 *
 * @author Carlos
 */
public interface RenegociacaoRelInterfaceFacade {

	public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;

	public List<MatriculaVO> consultarAluno(String campoConsultaAluno, String valorConsultaAluno, Integer turma, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public List<RenegociacaoRelVO> realizarCriacaoObjRel(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer turma, String matricula, String tipoContaNegociada, String tipo, String tipoRelatorio, Date dataInicio, Date dataFim, String situacaoContaReceber, String ordenarPor, String tipoPeriodo, UsuarioVO usuarioVO, Integer responsavelFinanceiro, Integer funcionario, Integer parceiro, Integer fornecedor, Integer responsavelRenegociacao) throws Exception;
}
