package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface TipoDocumentoInterfaceFacade {

	public TipoDocumentoVO novo() throws Exception;

	public void incluir(TipoDocumentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void incluir(final TipoDocumentoVO obj) throws Exception;

	public void alterar(TipoDocumentoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(TipoDocumentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public TipoDocumentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TipoDocumentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TipoDocumentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<TipoDocumentoVO> consultarPorSituacaoMatricula(String matricula, int nivelmontardadosTodos) throws Exception;

	public List<TipoDocumentoVO> consultarUtilizadosPorFuncionarios(Integer idPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public TipoDocumentoVO consultarPorCategoriaGedIdentificadorGED(String categoriaGed, String identificadorGED, String matricula) throws Exception;

	@SuppressWarnings("rawtypes")
	public List consultarPorCategoriaGED(Integer codigo) throws Exception;
	
	List<TipoDocumentoVO> consultarPorIdentificadorGED(String valorConsulta, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<TipoDocumentoVO> consultarPorCategoriaGED(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;
	
	List<TipoDocumentoVO> consultarPorIdentificadorGEDCategoriaGED(String campoConsulta, String valorConsulta, DocumentacaoGEDVO documentacaoGED, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<TipoDocumentoVO> consultarPorTipoExigenciaDocumento(TipoExigenciaDocumentoEnum tipoExigenciaDocumentoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<TipoDocumentoVO> consultarTipoDocumentoUtilizadosPorFuncionarios(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<TipoDocumentoVO> consultarTipoDocumentoFuncionarioManual(Integer codigo, boolean controlarAcesso, int nivelmontardadosCombobox, UsuarioVO usuarioLogado) throws Exception;

	TipoDocumentoVO consultarPorChavePrimariaUnico(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario)
			throws Exception;
}