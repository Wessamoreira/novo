package negocio.interfaces.financeiro;

import java.util.List;

import javax.faces.model.SelectItem;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaRateioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface CategoriaDespesaInterfaceFacade {	

	/*public void incluir(CategoriaDespesaVO obj, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	public void alterar(CategoriaDespesaVO obj, UsuarioVO usuarioVO) throws Exception;*/

	public void excluir(CategoriaDespesaVO obj, UsuarioVO usuarioVO) throws Exception;

	public CategoriaDespesaVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorCategoriaDespesaPrincipal(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorCategoriaDespesaPrincipalFilho(Integer codigo, boolean controlarAcesso, int NIVELMONTARDADOS_DADOSBASICOS, boolean limitarUmRegistro, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorDescricaoPlanoOrcamentario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorIdentificadorCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorIdentificadorCategoriaDespesaPlanoOrcamentario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorNivelCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	// public List consultarPorNomeDepartamento(String valorConsulta, int
	// nivelMontarDados) throws Exception;
	public void setIdEntidade(String aIdEntidade);

	public List<CategoriaDespesaVO> consultarUltimaMascaraGerada(String mascaraConsulta, String mascaraPlanoContaPrincipal, int codigo, int nivelMontarDados) throws Exception;

	public boolean consultarSeExisteCategoriaDespesa() throws Exception;

	List<CategoriaDespesaVO> consultarPorNomeCategoriaDespesaPrincipal(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	CategoriaDespesaVO consultarPorIdentificadorCategoriaDespesaUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorDescricaoPassandoCodCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, int codCategoriaDespesa) throws Exception;

	public List<CategoriaDespesaVO> consultarPorIdentificadorCategoriaDespesaPassandoCodCategoriaDespesa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, int codCategoriaDespesa) throws Exception;

	void addCategoriaDespesaRateio(CategoriaDespesaVO obj, CategoriaDespesaRateioVO categoriaRateio, UsuarioVO usuario) throws Exception;

	void removeCategoriaDespesaRateio(CategoriaDespesaVO obj, CategoriaDespesaRateioVO categoriaRateio, UsuarioVO usuario) throws Exception;

	void persistir(CategoriaDespesaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<CategoriaDespesaVO> consultaRapidaCategoriaDespDRE() throws Exception;

	CategoriaDespesaVO consultaCategoriaDespesaPadraoConfiguracaoFinanceiroPorUnidadeEnsinoTipoCategoria(Integer unidadeEnsino, Integer operadoraCartao, String campoCategoriaDespesa) throws Exception;

	void montarListaSelectItemTipoNivelCentroResultadoEnum(CategoriaDespesaVO obj, List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) throws Exception;

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception;

	CategoriaDespesaVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;

	public List<CategoriaDespesaVO> consultarPorDescricaoExigeCentroCustoRequisitante(String valorConsultaCentroDespesa, boolean b, int nivelmontardadosDadosconsulta, UsuarioVO usuarioLogado) throws Exception;

	public List<CategoriaDespesaVO>consultarPorIdentificadorExigeCentroCustoRequisitante(String valorConsultaCentroDespesa, boolean b, int nivelmontardadosDadosconsulta, UsuarioVO usuarioLogado) throws Exception;

}