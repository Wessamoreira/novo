package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;

/*
 * @author Victor Hugo 10/10/2014
 */
public interface AvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade {

	void incluir(AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO) throws Exception;

	void persistir(AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AvaliacaoOnlineMatriculaRespostaQuestaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<AvaliacaoOnlineMatriculaRespostaQuestaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	AvaliacaoOnlineMatriculaRespostaQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void persistirRepostasQuestoesAvaliacaoOnlineMatricula(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, UsuarioVO usuarioVO) throws Exception;

	List consultarPorAvaliacaoOnlineMatriculaQuestao(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

}
