package relatorio.negocio.interfaces.processosel;

import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public interface ProcSeletivoRelInterfaceFacade {

	public void inicializarOrdenacoesRelatorio();

	public void inicializarParametros();

	public SqlRowSet executarConsultaParametrizada() throws Exception;

	public Integer getCurso();

	public void setCurso(Integer curso);

	public Date getDataFim();

	public void setDataFim(Date dataFim);

	public Date getDataInicio();

	public void setDataInicio(Date dataInicio);

	public Integer getDescricao();

	public void setDescricao(Integer descricao);

	public Integer getUnidadeEnsino();

	public void setUnidadeEnsino(Integer unidadeEnsino);

	String caminhoBaseRelatorio();
	

	public String designIReportRelatorio();

	

}