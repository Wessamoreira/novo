package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.QuestaoAssuntoVO;
import negocio.comuns.ead.QuestaoVO;

public interface QuestaoAssuntoInterfaceFacade {

	void incluir(QuestaoAssuntoVO questaoAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(QuestaoAssuntoVO questaoAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(Integer codigoQuestao, Integer codigoTemaAssunto, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void removerTemaAssunto(QuestaoAssuntoVO questaoAssuntoVO, List<QuestaoAssuntoVO> questaoAssuntoVOs, UsuarioVO usuarioVO) throws Exception;

	void persistir(QuestaoAssuntoVO questaoAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void adicionarTemaAssunto(TemaAssuntoVO temaAssuntoVO, QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception;

	QuestaoAssuntoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<QuestaoAssuntoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<QuestaoAssuntoVO> consultarPorCodigoQuestaoVO(Integer codigoQuestao, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void persistirQuestaoAssuntoVOs(QuestaoVO questaoVO, UsuarioVO usuarioVO) throws Exception;

	void excluirQuestaoAssuntoVOs(Integer codigoQuestao, List<QuestaoAssuntoVO> questaoAssuntoVOs,
			boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
}
