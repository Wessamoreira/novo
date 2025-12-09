package negocio.interfaces.compras;

import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface FormaPagamentoInterfaceFacade {

	public FormaPagamentoVO novo() throws Exception;

	public void incluir(FormaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(FormaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(FormaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception;

	public FormaPagamentoVO consultarPorChavePrimaria(Integer codigo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FormaPagamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FormaPagamentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public FormaPagamentoVO consultarPorTipoDinheiro(int nivelmontardadosDadosminimos) throws Exception;

	public FormaPagamentoVO consultarPorTipoDebitoEmContaCorrente(int nivelMontarDados) throws Exception;

	public FormaPagamentoVO consultarPorTipo(String tipo, int nivelMontarDados) throws Exception;
	
	public List<FormaPagamentoVO> consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<FormaPagamentoVO> consultarPorNomeUsaNoRecebimento(String valorConsulta, Boolean usaNoRecebimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public FormaPagamentoVO consultarFormaPagamentoCheque(UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 30/04/2015
	 * @param formaPagamento
	 * @return
	 * @throws Exception
	 */
	boolean executarVerificacaoFormaPagamentoVinculadoNegociacaoRecebimento(Integer formaPagamento) throws Exception;
	
	public List<FormaPagamentoVO> consultarFormaPagamentoCartoes(boolean controlarAcesso, Boolean usaNoRecebimento, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<FormaPagamentoVO> consultarFormaPagamentoCartoesCredito(boolean controlarAcesso, Boolean usaNoRecebimento, int nivelMontarDados, UsuarioVO usuario) throws Exception; 
	
	public List<FormaPagamentoVO> consultarFormaPagamentoDaMovimentacaoFinanceira(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public List<FormaPagamentoVO> consultarFormaPagamentoFaltandoLista(List<FormaPagamentoVO> formaPagamentoVOs, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void consultarPorEnumCampoConsulta(DataModelo consultaFormaPagamento) throws Exception;
	
	List<FormaPagamentoVO> consultarPorTipoCartaoOnline(PermitirCartaoEnum tipo, Boolean usaNoRecebimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	FormaPagamentoVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception;
}