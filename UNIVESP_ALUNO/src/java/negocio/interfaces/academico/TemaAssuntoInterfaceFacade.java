package negocio.interfaces.academico;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.TemaAssuntoDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;

public interface TemaAssuntoInterfaceFacade {

	void incluir(TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	TemaAssuntoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void persistir(TemaAssuntoVO temaAssuntoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<TemaAssuntoVO> consultarPorNome(String nomeTemaAssunto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	TemaAssuntoVO consultarPorChavePrimaria(Integer codigoTemaAssunto, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoPorNome(String nomeTemaAssunto, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoPorAbreviatura(String abreviaturaAssunto, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<TemaAssuntoDisciplinaVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<TemaAssuntoVO> consultarTemaAssuntoPorCodigoDisciplina(Integer codigoDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<TemaAssuntoVO> consultarTemaAssuntoPorNomeECodigoDisciplina(String nomeTemaAssunto, Integer codigoDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	Integer consultarQuantidadeAssuntoPorConteudo(Integer codigoConteudo, UsuarioVO usuarioVO) throws Exception;

	List<Integer> consultarTemasAssuntosPorConteudo(Integer codigoUnidadeConteudo, Integer codigoConteudo, Integer codigoMatriculaPeriodoTurmaDisciplina, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, UsuarioVO usuarioVO) throws Exception;

	List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoDaDisciplinaPorCodigoDisciplina(Integer codigoDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	List<TemaAssuntoDisciplinaVO> consultarTemaAssuntoPorNomeDisciplina(String nomeDisciplina, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

}
