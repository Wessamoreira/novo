package relatorio.negocio.interfaces.processosel;

import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import relatorio.negocio.comuns.processosel.PerfilSocioEconomicoRelVO;

public interface PerfilSocioEconomicoRelInterfaceFacade {

	public List emitirRelatorio() throws Exception;

	public void inicializarOrdenacoesRelatorio();

	public void inicializarParametros();

	public List montarDadosConsulta(SqlRowSet rs) throws Exception;

	public void executarConsultaParametrizadaSexo() throws Exception;

	public void executarConsultaParametrizadaRendaMensalFamiliar() throws Exception;

	public void executarConsultaParametrizadaRendaMensalCandidato() throws Exception;

	public void executarConsultaParametrizadaComoPretendeManterCurso() throws Exception;

	public void executarConsultaParametrizadaGrauEscolaridadePai() throws Exception;

	public void executarConsultaParametrizadaGrauEscolaridadeMae() throws Exception;

	public void executarConsultaParametrizadaEscolaCursouEnsinoMedio() throws Exception;

	public void executarConsultaParametrizadaQuantidadeFaculdadePrestVestibular() throws Exception;

	public void executarConsultaParametrizadaConhecimentoProcSeletivo() throws Exception;

	/**
	 * Operação reponsável por retornar o arquivo (caminho e nome) correspondente ao
	 * design do relatório criado pelo IReport.
	 */
	public String getDesignIReportRelatorio();

	public String getCaminhoBaseRelatorio();

	public Integer getUnidadeEnsino();

	public void setUnidadeEnsino(Integer unidadeEnsino);

	public void setIdEntidade(String idEntidade);

	public PerfilSocioEconomicoRelVO getPerfilSocioEconomicoRelVO();

	public void setPerfilSocioEconomicoRelVO(PerfilSocioEconomicoRelVO perfilSocioEconomicoRelVO);

}