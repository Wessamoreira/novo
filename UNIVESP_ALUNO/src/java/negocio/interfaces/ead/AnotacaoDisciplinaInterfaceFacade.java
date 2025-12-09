package negocio.interfaces.ead;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AnotacaoDisciplinaVO;


/*
 * @author Victor Hugo 08/09/2014
 */
public interface AnotacaoDisciplinaInterfaceFacade {

	void incluir(AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void persistir(AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(AnotacaoDisciplinaVO anotacaoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void validarDados(AnotacaoDisciplinaVO anotacaoDisciplinaVO) throws Exception;

	AnotacaoDisciplinaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado);
	
	List consultarPorChavePrimaria (Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List consultarPorPalavraChave (String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	List<AnotacaoDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	List <AnotacaoDisciplinaVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	boolean consultarExistenciaAnotacaoDisciplinaPorUnidadeConteudoUnidadeConteudoPagina(Integer unidadeConteudo, Integer conteudoUnidadePagina, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	AnotacaoDisciplinaVO realizarCriarAnotacaoUnidadeConteudo(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, UsuarioVO usuarioLogado);

	AnotacaoDisciplinaVO realizarCriarAnotacaoConteudoUnidadePagina(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuarioLogado);

	AnotacaoDisciplinaVO consultarAnotacaoDisciplinaPorDisciplinaMatriculaConteudoConteudoUnidadePagina(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaVO conteudoUnidadePaginaVO, UsuarioVO usuarioLogado) throws Exception;

	AnotacaoDisciplinaVO consultarAnotacaoDisciplinaPorDisciplinaMatriculaUnidadeConteudo(String matriculaVO, DisciplinaVO disciplinaVO, ConteudoVO conteudoVO, UnidadeConteudoVO unidadeConteudoVO, UsuarioVO usuarioLogado) throws Exception;

	boolean consultarExistenciaAnotacaoDisciplinaPorUnidadeConteudo(Integer unidadeConteudo, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<AnotacaoDisciplinaVO> consultaAnotacoesPorTemaAssuntoEConteudo(String matricula,Integer codigoTemaAssunto, Integer codigoConteudo, UsuarioVO usuarioVO) throws Exception;
}
