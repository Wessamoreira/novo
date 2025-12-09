package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import relatorio.negocio.comuns.financeiro.ExtratoContaPagarRelVO;

public interface ExtratoContaPagarRelInterfaceFacade {

    public String caminhoBaseIReportRelatorio();

    public List<ExtratoContaPagarRelVO> criarObjeto(Integer codigoFavorecido, String nomeFavorecido, String filtroTipoFavorecido, String filtroTipoData, Date dataInicio, Date dataFim, UnidadeEnsinoVO unidadeEnsino) throws Exception;

    public void calcularTotalPagarTotalPagoExtratoContaPagar(ExtratoContaPagarRelVO extratoContaPagarRelVO, List<ContaPagarVO> listaContaPagar);

    public String getDesignIReportRelatorioPdf();

    public String getDesignIReportRelatorioExcel();

    public void validarDados(Integer codigoFavorecido, Date dataInicio, Date dataFim) throws Exception;

    public void montarDadosBancoRecebimento(ExtratoContaPagarRelVO extratoContaPagarRelVO);
}
