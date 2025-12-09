package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RecebimentoPorTurmaRelVO;

public interface RecebimentoPorTurmaRelInterfaceFacade {

	public List<RecebimentoPorTurmaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnisno, List<TipoRequerimentoVO> listaTipoReq, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Date dataInicio, Date dataFim, TurmaVO turma, Integer ordenacao, String tipoLayout, boolean naoTrazerContasIsentas, String parcela, String filtrarPor, UsuarioVO usuarioVO) throws Exception;

	public String getDesignIReportRelatorio(String layout);

	public String caminhoBaseIReportRelatorio();

	public void validarDados(Integer codigoTurma, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws ConsistirException;

	public String getDesignIReportRelatorioExcel();

	public void validarDadosPeriodoRelatorioUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer turma, Date dataInicio, Date dataFim) throws Exception;
	
}