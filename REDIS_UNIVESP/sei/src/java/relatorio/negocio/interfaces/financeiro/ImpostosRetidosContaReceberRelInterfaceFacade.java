package relatorio.negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.financeiro.ParceiroVO;
import relatorio.negocio.comuns.financeiro.ImpostosRetidosContaReceberRelVO;

public interface ImpostosRetidosContaReceberRelInterfaceFacade {


	public List<ImpostosRetidosContaReceberRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, String layout) throws Exception;

	public ImpostosRetidosContaReceberRelVO montarDados(SqlRowSet dadosSQL, String layout) throws Exception;

	String caminhoBaseRelatorio();

	String designIReportRelatorio(String layout);
	
	String designIReportRelatorioExcel(String layout);

	void validarDados(Date dataInicio, Date dataFim) throws Exception;

	List<ImpostosRetidosContaReceberRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, ParceiroVO parceiro, EstadoVO estado,
			CidadeVO cidade, List<ImpostoVO> listaImpostos, Date dataInicio, Date dataFim, String situacaoContaReceber,
			String layout, UsuarioVO usuario) throws Exception;
	
	List<ImpostoVO> criarListaImposto(List<UnidadeEnsinoVO> listaUnidadeEnsino, ParceiroVO parceiro, EstadoVO estado,
			CidadeVO cidade, List<ImpostoVO> listaImpostos, Date dataInicio, Date dataFim, String situacaoContaReceber
			) throws Exception;
	
}