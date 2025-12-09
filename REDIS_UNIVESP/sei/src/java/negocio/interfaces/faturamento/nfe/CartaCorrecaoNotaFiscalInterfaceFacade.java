package negocio.interfaces.faturamento.nfe;

import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.CartaCorrecaoVO;



/**
 *
 */
public interface CartaCorrecaoNotaFiscalInterfaceFacade {

	void transmitirCartaCorrecao(CartaCorrecaoVO cartaCorrecao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception;

	List<CartaCorrecaoVO> consultarPorNotaFiscal(Integer nota, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception ;
	public String getDesignIReportRelatorio();
	
}
