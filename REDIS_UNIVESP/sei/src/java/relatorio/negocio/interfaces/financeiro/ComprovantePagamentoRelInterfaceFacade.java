package relatorio.negocio.interfaces.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import relatorio.negocio.comuns.financeiro.ComprovantePagamentoRelVO;

public interface ComprovantePagamentoRelInterfaceFacade {

	String getDesignIReportRelatorio();

	List<ComprovantePagamentoRelVO> criarObjeto(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception;

	String getCaminhoBaseRelatorio();

}
