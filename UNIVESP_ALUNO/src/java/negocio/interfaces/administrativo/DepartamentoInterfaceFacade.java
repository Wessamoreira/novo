package negocio.interfaces.administrativo;

import java.util.List;

import negocio.comuns.administrativo.DepartamentoVO;
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
public interface DepartamentoInterfaceFacade {

	
	public void incluir(DepartamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(DepartamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(DepartamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public DepartamentoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<DepartamentoVO> consultarPorCodigoPorUnidadeEnsino(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception ;

	public List<DepartamentoVO> consultarPorDepartamentoSuperior(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	
	public List<DepartamentoVO> consultarPorNomePorUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorCodigoUnidadeEnsinoESemUE(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<DepartamentoVO> consultarPorNomeFaleConosco(String valorConsulta, boolean controlarAcesso, int nivelMontarDado, UsuarioVO usuarios) throws Exception;

	public List<DepartamentoVO> consultarPorDiferenteDepartamentoSuperior(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorNivelZero(int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorNivelUm(Integer codDepartamentoPai, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorGerenteDpto(Integer codigoGerente, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DepartamentoVO> consultarDepartamentoContaPagar(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DepartamentoVO> consultarDepartamentoRequerimento(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<DepartamentoVO> consultarPorCodigoPessoaUnidadeEnsino(Integer pessoa, Integer unidadeEnsino, UsuarioVO usuarioVO);

	public List<DepartamentoVO> consultarPorUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidade, int nivelMontarDados, UsuarioVO usuario, Boolean visualizarTodasUnidades, Boolean todosDepartamentosMesmaUnidade, Boolean todosDepartamentosMesmoTramite, Boolean mesmoDepartamentosMesmaUnidade, Boolean realizarTramiteRequerimentoOutroDepartamento, Boolean consultarRequerimentoOutroDepartamentoMesmoTramiteTodasUnidades, Boolean consultarRequerimentoOutrosResponsaveisMesmoDepartamentoTodasUnidades) throws Exception;

	DepartamentoVO consultarPorCodigoFuncionario(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DepartamentoVO> consultarDepartamentoPorDepartamentoTramiteExistente(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<DepartamentoVO> consultarPorCodigoPessoaFuncionario(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	boolean consultarSePessoaTrabalhaNoDepartamento(Integer codigoPessoa, Integer departamento, Integer centroResultado);

	boolean consultarSeGerenteTrabalhaNoDepartamento(Integer codigoPessoa, Integer departamento, Integer centroResultado);

	public DepartamentoVO consultarPorCodigoFuncionarioCargo(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	DepartamentoVO consultarDepartamentoControlaEstoquePorUnidadeEnsino(Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	DepartamentoVO consultarDepartamentoControlaEstoquePorCentroResultado(Integer centroResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	DepartamentoVO consultarDepartamentoPorContratoReceitas(Integer contratoReceitas, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<DepartamentoVO> consultarDepartamentoObrigatoriamenteComResponsavelPorCoodigoParaEnvioComunicadoInterno(List<DepartamentoVO> departamentoVOs, UsuarioVO usuario) throws Exception;

}