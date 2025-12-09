package relatorio.negocio.interfaces.financeiro;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.ContaPagarCategoriaDespesaRelVO;
import relatorio.negocio.comuns.financeiro.ContaPagarRelVO;

public interface ContaPagarRelInterfaceFacade {

    String designIReportRelatorio();
    
    String designIReportRelatorioExcel();

    String caminhoBaseRelatorio();

    List<ContaPagarRelVO> criarObjeto(ContaPagarRelVO contaPagarRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroNegociada, String filtroCancelada, Integer numero, Boolean apresentaContaCorrente, UsuarioVO usuarioVO) throws Exception;

    void validarDados(ContaPagarRelVO contaPagarRelVO) throws ConsistirException;

    public Vector getOrdenacoesRelatorio();

	String designIReportRelatorioEspecifica();

	List<ContaPagarCategoriaDespesaRelVO> realizarCriacaoResumoPorCategoriaDespesa(Map<String, ContaPagarCategoriaDespesaRelVO> mapaTemp) throws Exception;

}