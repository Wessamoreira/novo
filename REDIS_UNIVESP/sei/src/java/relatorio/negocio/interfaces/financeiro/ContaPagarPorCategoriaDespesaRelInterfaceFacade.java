package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import relatorio.negocio.comuns.financeiro.ContaPagarPorCategoriaDespesaRelVO;

/**
 *
 * @author Carlos
 */
public interface ContaPagarPorCategoriaDespesaRelInterfaceFacade {
    public String caminhoBaseRelatorio();
    public String designIReportRelatorio();
	String designIReportRelatorioSintetico();
	List<ContaPagarPorCategoriaDespesaRelVO> criarObjeto(Integer categoriaDespesa, Integer unidadeEnsino, Date dataInicio, Date dataFim, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente, String filtroContaCancelada, String possuiConta, Integer codigoContaCorrente , Boolean trazerContasSubcategoria) throws Exception;
}
