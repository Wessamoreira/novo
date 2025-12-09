package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import relatorio.negocio.comuns.financeiro.AdiantamentoRelVO;

public interface AdiantamentoRelInterfaceFacade {


	public List<AdiantamentoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception;

	public AdiantamentoRelVO montarDados(SqlRowSet dadosSQL) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio();
	
	String designIReportRelatorioExcel();

	void validarDados(Date dataInicio, Date dataFim) throws Exception;

	List<AdiantamentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim, String tipoSacado, String situacaoContaPagar, String situacaoAdiantamento, UsuarioVO usuario) throws Exception;
		
}