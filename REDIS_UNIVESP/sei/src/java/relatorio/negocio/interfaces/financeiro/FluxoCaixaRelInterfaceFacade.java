package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.FluxoCaixaRelVO;

public interface FluxoCaixaRelInterfaceFacade {

    public Vector getOrdenacoesRelatorio();

    List<FluxoCaixaRelVO> criarObjeto(Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer contaCaixa, Integer usuario, String modeloRel, String tipoLayout) throws Exception;

    List<FluxoCaixaRelVO> criarObjetoListaVazia(Date dataInicio, Date dataFim, Integer unidadeEnsino, Integer contaCaixa, Integer usuario) throws Exception;

    public String designIReportRelatorio(String modeloRel, String tipoLayout);

    String caminhoBaseIReportRelatorio();

    void validarDados(Date dataInicio, Date dataFim) throws ConsistirException;

}