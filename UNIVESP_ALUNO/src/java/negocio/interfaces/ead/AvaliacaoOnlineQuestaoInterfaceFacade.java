package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;

/**
 * @author Victor Hugo 10/10/2014
 */
public interface AvaliacaoOnlineQuestaoInterfaceFacade {

	void incluir(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO) throws Exception;

	void persistir(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AvaliacaoOnlineQuestaoVO avaliacaoOnlineQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineQuestaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AvaliacaoOnlineQuestaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	AvaliacaoOnlineQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void validarQuandoSelecionarQuestoesFixasRadomicamente(AvaliacaoOnlineVO avaliacaoOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception;

	List<AvaliacaoOnlineQuestaoVO> consultarPorAvaliacaoOnline(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	void exluirQuestaoAvaliacaoOnline(List<AvaliacaoOnlineQuestaoVO> avaliacaoOnlineQuestaoVOs, UsuarioVO usuarioVO) throws Exception;

	List<AvaliacaoOnlineQuestaoVO> gerarQuestoesRandomicamente(AvaliacaoOnlineVO avaliacaoOnlineVO, Integer qtdeNivelFacil, Integer qtdeNivelMedio, Integer qtdeNivelDificil, Integer qtdeQualquerNivel, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception;

	void persistirQuestoesAvaliacaoOnline(AvaliacaoOnlineVO avaliacaoOnlineVO, UsuarioVO usuarioVO) throws Exception;

	void excluirAvaliacaoOnlineQuestao(Integer codigoAvaliacaoOnline, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<AvaliacaoOnlineQuestaoVO> consultarAvaliacaoOnlineQuestaoComQuestaoPorAvaliacaoOnline(Integer codigo, UsuarioVO usuarioLogado) throws Exception;

}
