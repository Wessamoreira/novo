package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface FormaPagamentoNegociacaoRecebimentoCartaoCreditoInterfaceFacade {

    public void persistir(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception;

    public void persistir(List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> listaFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO, UsuarioVO usuario) throws Exception;

    public void excluir(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception;

    public void validarDados(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws ConsistirException;

    public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarFormaPagamentoNegociacaoRecebimentoCartaoCredito(Integer configuracaoFinanceiro, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorTipoOperadoraCartao(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

    public void excluirPelaFormaPagamentoNegociacaoRecebimento(Integer codigoFormaPagamentoNegociacaoRecebimento, UsuarioVO usuario) throws Exception;

    public void alterarDataRecebimentoSituacaoResponsavelPelaBaixa(final FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);
    
    public List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> consultarPorFormaPagamentoNegociacaoRecebimento(Integer codigoFormaPagamentoNegociacaoRecebimento) throws Exception;

	void alterar(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 16 de mai de 2016 
	 * @return
	 * @throws Exception 
	 */
	List<FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO> consultarFormaPagamentoNegociacaoRecebimentoAReceberDCC() throws Exception;

	void alterarDataVencimento(FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception;
	
	public void alterarAjusteValorLiquido(final FormaPagamentoNegociacaoRecebimentoCartaoCreditoVO obj, UsuarioVO usuario) throws Exception;
}
