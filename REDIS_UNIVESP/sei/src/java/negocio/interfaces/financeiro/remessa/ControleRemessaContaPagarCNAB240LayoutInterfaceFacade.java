package negocio.interfaces.financeiro.remessa;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.RegistroDetalhePagarVO;
import negocio.comuns.utilitarias.EditorOC;

/**
 *
 * @author Pedro Andrade
 */
public interface ControleRemessaContaPagarCNAB240LayoutInterfaceFacade {
    
	public EditorOC executarGeracaoDadosArquivoRemessa(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, ControleRemessaContaPagarVO controleRemessaContaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
	
	public void processarArquivoRetornoPagar(ControleCobrancaPagarVO controleCobrancaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
	
	public void criarNegociacaoPagamentoBaixandoContasPagas(ControleCobrancaPagarVO controleCobrancaVO, List<ContaPagarRegistroArquivoVO> listaContaPagarRegistroDetalhe, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
}
