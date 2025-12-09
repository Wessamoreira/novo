package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.QuestaoConteudoVO;
import negocio.comuns.ead.QuestaoVO;

/**
 * @author Victor Hugo 08/01/2014
 */
public interface QuestaoConteudoFacade {

	void incluir(final QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistirQuestaoConteudoVOs(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception;
	
	List<QuestaoConteudoVO> consultarPorCodigoQuestaoVO(Integer codigoQuestao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<QuestaoConteudoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	QuestaoConteudoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void excluir(QuestaoConteudoVO questaoConteudoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void adicionarConteudo(QuestaoVO questaoVO, QuestaoConteudoVO questaoConteudoVO, UsuarioVO usuarioVO) throws Exception;

	void excluirQuestaoConteudoVOs(Integer codigoQuestao, List<QuestaoConteudoVO> questaoConteudoVOs,
			boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	public void consultarClonarQuestaoConteudo(Integer codigoNovoConteudo, Integer conteudoAntigo, UsuarioVO usuarioVO) throws Exception;
}
