package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;

/*
 * @author Victor Hugo 10/10/2014
 */
public interface AvaliacaoOnlineMatriculaInterfaceFacade {

	void incluir(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO) throws Exception;

	void alterar(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineMatriculaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AvaliacaoOnlineMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	AvaliacaoOnlineMatriculaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void realizarVerificacaoQuestaoUnicaEscolha(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, UsuarioVO usuarioVO) throws Exception;

	void realizarCalculoQuantidadePerguntasRespondidas(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO);

	void executarCorrecaoAvaliacaoOnline(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean validarPerguntasRespondidas, UsuarioVO usuarioVO, boolean simulandoAcesso) throws Exception;

	AvaliacaoOnlineMatriculaVO realizarVisualizacaoGabarito(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarGeracaoAvaliacaoOnline(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, String matricula, Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, boolean simulandoAcesso, Boolean simulandoAvaliacao) throws Exception;

	void realizarGeracaoQuestoesRandomicamente(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, boolean simulandoAvaliacao) throws Exception;

	Integer consultarUltimaAvalicaoOnlineRealizadaPorMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplina) throws Exception;

	List<AvaliacaoOnlineMatriculaVO> consultarAvaliacoesOnlineMatriculaRealizadas(Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuarioVO) throws Exception;

	void realizarGeracaoAvaliacaoOnlineRea(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineMatriculaVO consultarUltimaAvalicaoOnlinePorAvaliacaoOnlinePorMatriculaPeriodoTurmaDisciplina(Integer avaliacaoOnline, Integer codigoMatriculaPeriodoTurmaDisciplina, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;	

	void excluirPorCalendarioAtividadeMatricula(List<CalendarioAtividadeMatriculaVO> listaCalendario,
			UsuarioVO usuarioVO) throws Exception;

	void executarCorrecaoQuestaoAnuladaAvaliacaoOnline(AvaliacaoOnlineMatriculaVO obj, Boolean simularAnulacao, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineMatriculaVO consultarPorMatriculaEQuestaoAvaliacaoOnline(String matricula, Integer questao, Integer avaliacaoOnline, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
}
