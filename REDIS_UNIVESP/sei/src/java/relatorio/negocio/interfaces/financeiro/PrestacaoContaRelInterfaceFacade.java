package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.ConsistirException;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import relatorio.negocio.comuns.financeiro.PrestacaoContaRelVO;

public interface PrestacaoContaRelInterfaceFacade {

    public SqlRowSet executarConsultaParametrizada(Date dataInicio, Date dataFim, TurmaVO turma, boolean trazerContasConvenio, Integer ordenacao, String descricaoFiltros) throws Exception;

    List<PrestacaoContaRelVO> criarObjeto(Date dataInicio, Date dataFim, TurmaVO turma, boolean trazerContasConvenio, Integer ordenacao, String descricaoFiltros) throws Exception;

    public String getDesignIReportRelatorio(String layout);

    public String caminhoBaseIReportRelatorio();

    public Vector getOrdenacoesRelatorio();

    void validarDados(Integer codigoTurma) throws ConsistirException;

}