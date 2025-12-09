package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.RegistroSaidaAcervoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface RegistroSaidaAcervoInterfaceFacade {

	public RegistroSaidaAcervoVO novo() throws Exception;

	public void incluir(RegistroSaidaAcervoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(RegistroSaidaAcervoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(RegistroSaidaAcervoVO obj, UsuarioVO usuarioVO) throws Exception;

	public RegistroSaidaAcervoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroSaidaAcervoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroSaidaAcervoVO> consultarPorNomeUsuario(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroSaidaAcervoVO> consultarPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroSaidaAcervoVO> consultarPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void inicializarDadosRegistroSaidaAcervoNovo(RegistroSaidaAcervoVO registroSaidaAcervoVO, UsuarioVO usuario) throws Exception;

	List<RegistroSaidaAcervoVO> consultarPorTombo(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;
}