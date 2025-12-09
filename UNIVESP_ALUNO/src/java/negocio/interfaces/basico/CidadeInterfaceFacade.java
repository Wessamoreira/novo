package negocio.interfaces.basico;
import java.util.List;

import controle.arquitetura.ControleConsultaCidade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import webservice.servicos.CidadeObject;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface CidadeInterfaceFacade {
	

    public CidadeVO novo() throws Exception;
    public void incluir(CidadeVO obj,  UsuarioVO usuarioVO) throws Exception;
    public void alterar(CidadeVO obj,  UsuarioVO usuarioVO) throws Exception;
    public void alterarCodigoIBGECidade(CidadeVO obj) throws Exception;
    public void excluir(CidadeVO obj,  UsuarioVO usuarioVO) throws Exception;
    public CidadeVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public CidadeVO consultaCidadeRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<CidadeVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<CidadeVO> consultarPorNome(String valorConsulta,  boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List consultarPorNomeCodigoEstado(String valorConsulta, boolean controlarAcesso, Integer estado, UsuarioVO usuario) throws Exception;
    public List<CidadeVO> consultarPorSiglaEstado(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorCodigoEstado(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List consultarPorCep(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario ) throws Exception;
    public List consultarPorEstado(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<CidadeVO> consultaRapidaPorNomeAutoComplete(String valorConsulta, int limit,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public List<CidadeVO> consultaRapidaPorCodigo(Integer codCidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<CidadeVO> consultaRapidaPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public List<CidadeObject> consultarPorNomeRS(String nome) throws Exception;
    public CidadeVO consultarPorMatriculaAluno(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public CidadeVO consultarPorUnidadeEnsinoMatriz(boolean controlarAcesso, UsuarioVO usuario) throws Exception;
    public CidadeVO consultarCidadePorUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public CidadeVO consultarCidadeUnidadeEnsinoPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    public CidadeVO consultarDadosComboBoxPorBiblioteca(Integer biblioteca, UsuarioVO usuarioVO);
	List<CidadeObject> consultarPorCodigoEstadoRS(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	public Integer consultarCodigoDMS(CidadeVO cidade, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public CidadeVO consultarCidadePorCodigoIBGENomeCidade(String codigoIBGE, String nomeCidade, UsuarioVO usuario) throws Exception;
	CidadeVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario)
			throws Exception;
	public List<CidadeVO> consultarPorNomeSiglaEstado(String nomeCidade, String siglaEstado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	CidadeVO consultarPorNomeCidadeSiglaEstado(String nomeCidade, String siglaEstado, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	public void consultarCidade(ControleConsultaCidade controleConsultaCidade, Boolean controleAcesso, UsuarioVO usuario) throws Exception;
}