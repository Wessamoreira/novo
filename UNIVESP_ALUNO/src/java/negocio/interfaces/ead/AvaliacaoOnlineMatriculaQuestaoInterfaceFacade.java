package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;

/**
 * @author Victor Hugo 10/10/2014
 */
public interface AvaliacaoOnlineMatriculaQuestaoInterfaceFacade {

	void incluir(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO) throws Exception;

	void persistir(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineMatriculaQuestaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AvaliacaoOnlineMatriculaQuestaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	AvaliacaoOnlineMatriculaQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void persistirQuestoesAvaliacaoOnlineMatricula(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO, UsuarioVO usuarioVO) throws Exception;

	void realizarCorrecaoQuestao(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO);

	List<AvaliacaoOnlineMatriculaQuestaoVO> consultarPorAvaliacaoOnlineMatricula(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AvaliacaoOnlineMatriculaQuestaoVO> consultarQuestoesQueAcertouOuErrou(Integer codigoAvaliacaoOnlineMatricula, Integer codigoTemaAssunto, SituacaoAtividadeRespostaEnum situacaoAtividadeRespostaEnum, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void realizarCorrecaoNotaAvaliacaoOnlineQuestaoAnulada(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, AvaliacaoOnlineMatriculaVO obj);

	void alterarSituacaoPorCodigo(Integer avaliacaoOnlineMatriculaQuestao, SituacaoAtividadeRespostaEnum situacao, UsuarioVO usuario) throws Exception;

	List<AvaliacaoOnlineMatriculaQuestaoVO> consultarPorAvaliacaoOnlineMatriculaEQuestao(Integer avaliacaoOnlineMatricula, Integer questao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

}
