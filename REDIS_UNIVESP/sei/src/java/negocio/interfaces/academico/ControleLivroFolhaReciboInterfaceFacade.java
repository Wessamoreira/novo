package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface ControleLivroFolhaReciboInterfaceFacade {

	public ControleLivroFolhaReciboVO novo() throws Exception;

	public void incluir(ControleLivroFolhaReciboVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(ControleLivroFolhaReciboVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(ControleLivroFolhaReciboVO obj, UsuarioVO usuario) throws Exception;

	public ControleLivroFolhaReciboVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<ControleLivroFolhaReciboVO> consultarDadosControleLivroFolhaRecibo(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

	public ControleLivroFolhaReciboVO consultarPorMatriculaMatriculaMaiorVia(String valorConsulta, TipoLivroRegistroDiplomaEnum tipoLivro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public ControleLivroFolhaReciboVO montarDadosListaLivroFolhaReciboMatricula(MatriculaVO matricula, ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO, int folhaRecibo, Boolean filtroPorProgramacaoFormatura, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List consultarPorMatriculaCursoTurmaSituacaoControleLivroRegistroDiploma(String matricula, Integer curso, Integer turma, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer nrLivro) throws Exception;

	public void alterarListaControleLivroFolhaRecibo(Integer controleLivroRegistroDiploma, List<ControleLivroFolhaReciboVO> listaconControleLivroFolhaReciboVOs, UsuarioVO usuarioVO) throws Exception;

	String adicionarListaLivroFolhaReciboMatricula(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO,
			UnidadeEnsinoVO unidadeEnsinoVO, CursoVO cursoVO, List<MatriculaVO> matriculas,
			List<ControleLivroFolhaReciboVO> controleLivroFolhaReciboVOs, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, Boolean filtroPorProgramacaoFormatura, UsuarioVO usuarioVO) throws Exception;
	
	public String verificaSituacaoDiploma(String matricula, UsuarioVO usuarioVO);
	
	public List<ControleLivroFolhaReciboVO> consultarLivroFolhaReciboDiploma(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por realizar uma consulta de <code>ControleLivroFolhaRecibo</code> através do valor do atributo 
	 * <code>matricula</code> da classe <code>Matricula</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * @return  List Contendo vários objetos da classe <code>ControleLivroFolhaReciboVO</code> resultantes da consulta.
	 * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
	*/
	public ControleLivroFolhaReciboVO consultarPorMatriculaMatriculaPrimeiraEUltimaVia(String valorConsulta, Integer curso, TipoLivroRegistroDiplomaEnum tipoLivro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}