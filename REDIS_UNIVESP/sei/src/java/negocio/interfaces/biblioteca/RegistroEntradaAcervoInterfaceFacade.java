package negocio.interfaces.biblioteca;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.RegistroEntradaAcervoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface RegistroEntradaAcervoInterfaceFacade {

	public void excluirExemplarItemRegistroEntradaAcervo(Integer exemplar, UsuarioVO usuario) throws Exception;
	
	public RegistroEntradaAcervoVO novo() throws Exception;

	public void incluir(RegistroEntradaAcervoVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(RegistroEntradaAcervoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(RegistroEntradaAcervoVO obj, UsuarioVO usuarioVO) throws Exception;

	public RegistroEntradaAcervoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroEntradaAcervoVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroEntradaAcervoVO> consultarPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroEntradaAcervoVO> consultarPorTipoEntrada(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroEntradaAcervoVO> consultarPorNomeUsuario(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<RegistroEntradaAcervoVO> consultarPorNomeBiblioteca(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void inicializarDadosRegistroEntradaAcervoNovo(RegistroEntradaAcervoVO registroEntradaAcervoVO, UsuarioVO usuario) throws Exception;

	public void registrarEntradaAcervoExemplar(ExemplarVO obj, UsuarioVO usuario) throws Exception;

        public void excluirRegistrarEntradaAcervoExemplar(CatalogoVO catalogo) throws Exception;

		List<RegistroEntradaAcervoVO> consultarPorCodigoBarra(String valorConsulta, Integer unidadeEnsino,
				boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

		List<RegistroEntradaAcervoVO> consultarPorCatalogo(String valorConsulta, Integer unidadeEnsino,
				boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
}