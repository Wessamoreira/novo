package relatorio.negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.financeiro.ComprovanteRecebimentoRelVO;

public interface ComprovanteRecebimentoRelInterfaceFacade {

	public List<ComprovanteRecebimentoRelVO> criarObjeto(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception;

	public String getDesignIReportRelatorio();

	public String getCaminhoBaseRelatorio();

	public void validaLogoComprovanteRecibo(String caminhoFisicoLogo, String nomeLogo, SuperParametroRelVO superParametro, String urlExternoDownloadArquivo) throws Exception;
	
	public String consultarIdentificadorTurmaContaReceber(List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs) throws Exception;

	List<ComprovanteRecebimentoRelVO> montarObjetoComprovanteReciboCartaoCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 4 de jul de 2016 
	 * @param negociacaoRecebimentoVO
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<ComprovanteRecebimentoRelVO> criarObjetoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception;
}
