package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO;

public interface RecebimentoPorUnidadeCursoTurmaRelInterfaceFacade {

	public List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<TipoRequerimentoVO> listaTipoReq, Boolean dataCompetencia, Date dataInicio, Date dataFim, TurmaVO turma, CursoVO curso, String situacao, String matriculaAluno, Map<String, ContaReceberVO> hashMapCodigoContaCorrente, String filtro, Integer contaCorrente, Boolean utilizarValorCompensado, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception;

	public String getDesignIReportRelatorio(String tipoLayout);

	public String getDesignIReportRelatorioExcel(String tipoLayout);

	public String caminhoBaseIReportRelatorio();

	public void validarDados(Integer codigoTurma, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws ConsistirException;

	public void validarDadosPeriodoRelatorioUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer turma, Date dataInicio, Date dataFim) throws Exception;
}
