package negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;

public interface ContaPagarControleRemessaContaPagarInterfaceFacade {

	public void incluir(final ContaPagarControleRemessaContaPagarVO obj, final Integer controleRemessa, UsuarioVO usuario) throws Exception ;
	
	public void realizarEstorno(ContaPagarControleRemessaContaPagarVO controleRemessaContaPagarVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<ContaPagarControleRemessaContaPagarVO> consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception ;
	
	public ContaPagarControleRemessaContaPagarVO consultarPorCodigo(Integer codigo) throws Exception;

	Integer verificaContaPagarExistenteEmControleRemessa(ContaPagarVO obj) throws Exception;

	void atualizarSituacaoContaPagarControleRemessaContaPagarPorContaPagar(Integer contapagar, SituacaoControleRemessaContaReceberEnum situacao, UsuarioVO usuarioVO) throws Exception;
	
	public ContaPagarControleRemessaContaPagarVO consultarControleRemessaPorContaPagar(ContaPagarVO contaPagarVO) throws Exception;
	

	public List<ContaPagarControleRemessaContaPagarVO> consultarPorCodigoAgrupamento(String codigoAgrupamento,	UsuarioVO usuario) throws Exception;

	public List<ContaPagarControleRemessaContaPagarVO> consultarPorCodigoTransmissaoNossoNumero(String codigoAgrupamento,UsuarioVO usuario) throws Exception;

	public List<ContaPagarControleRemessaContaPagarVO> consultarPorNossoNumero(String nossonumero, UsuarioVO usuario)throws Exception;
			
}