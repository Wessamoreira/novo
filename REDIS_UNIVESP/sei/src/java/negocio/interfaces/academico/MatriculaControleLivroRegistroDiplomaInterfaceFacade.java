package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaControleLivroRegistroDiplomaVO;
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
public interface MatriculaControleLivroRegistroDiplomaInterfaceFacade {

	public MatriculaControleLivroRegistroDiplomaVO novo() throws Exception;

	public void incluir(MatriculaControleLivroRegistroDiplomaVO obj) throws Exception;

	public void alterar(MatriculaControleLivroRegistroDiplomaVO obj) throws Exception;

	public void excluir(MatriculaControleLivroRegistroDiplomaVO obj) throws Exception;

	public MatriculaControleLivroRegistroDiplomaVO consultarPorChavePrimaria(String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public MatriculaControleLivroRegistroDiplomaVO consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorDataEntregaRecibo(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoControleLivroFolhaRecibo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void gravarMatriculaRegistroLivro(List<MatriculaControleLivroRegistroDiplomaVO> matriculaControleLivroRegistroDiploma, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	void excluirPorControleLivroFolhaRecibo(Integer controleLivroFolhaRecibo) throws Exception;
}