package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.financeiro.ImpostosRetidosContaReceberRelVO;
import relatorio.negocio.interfaces.financeiro.ImpostosRetidosContaReceberRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings({"serial","deprecation"})
@Scope("singleton")
@Repository
@Lazy
public class ImpostosRetidosContaReceberRel extends SuperRelatorio implements ImpostosRetidosContaReceberRelInterfaceFacade {

	private static String idEntidade;

	public ImpostosRetidosContaReceberRel() {
		setIdEntidade("ImpostosRetidosContaReceberRel");
	}

	@Override
	public List<ImpostosRetidosContaReceberRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, ParceiroVO parceiro, EstadoVO estado, CidadeVO cidade, List<ImpostoVO>listaImpostos, Date dataInicio, Date dataFim, String situacaoContaReceber, String layout, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT cidade.nome as cidade, ");
		sqlStr.append("estado.sigla as estado, ");	    
		if(layout.equals("analitico")) {
			sqlStr.append("parceiro.nome as parceiro, ");
		}
		sqlStr.append("imposto.nome as imposto, ");
		sqlStr.append("sum(planodescontocontareceber.valorutilizadorecebimento) as valorImposto ");
		sqlStr.append("FROM contareceber ");
		sqlStr.append("INNER JOIN parceiro on (contareceber.parceiro=parceiro.codigo) ");
		sqlStr.append("INNER JOIN unidadeensino on (contareceber.unidadeensinofinanceira=unidadeensino.codigo) ");
		sqlStr.append("INNER JOIN cidade on (unidadeensino.cidade=cidade.codigo) ");
		sqlStr.append("INNER JOIN estado on (cidade.estado=estado.codigo) ");
		sqlStr.append("INNER JOIN planodescontocontareceber on (contareceber.codigo=planodescontocontareceber.contareceber) ");
		sqlStr.append("INNER JOIN imposto on (planodescontocontareceber.imposto=imposto.codigo) ");
		sqlStr.append("WHERE contareceber.tipoorigem = 'BCC' ");

		if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlStr.append("AND unidadeensino.codigo IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlStr.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlStr.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlStr.append(") ");
		}

		if(parceiro.getCodigo() != null && parceiro.getCodigo() != 0) {
			sqlStr.append("AND parceiro.codigo = ").append(parceiro.getCodigo());

		}

		if(estado.getCodigo() != null && estado.getCodigo() != 0) {
			sqlStr.append(" AND estado.codigo = ").append(estado.getCodigo());

		}

		if(cidade.getCodigo() != null && cidade.getCodigo() != 0) {
			sqlStr.append(" AND cidade.codigo = ").append(cidade.getCodigo());

		}

		if (!listaImpostos.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND imposto.codigo IN(");
			for (ImpostoVO impostoVo : listaImpostos) {
				if (impostoVo.getFiltrarImposto()) {
					if (!virgula) {
						sqlStr.append(impostoVo.getCodigo());
					} else {
						sqlStr.append(", ").append(impostoVo.getCodigo());
					}
					virgula = true;
				}
			}
			sqlStr.append(") ");
		}

		if (dataInicio != null) {
			sqlStr.append("AND contareceber.datavencimento BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("AND contareceber.datavencimento < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}

		if(situacaoContaReceber.equals("aReceber")) {
			sqlStr.append("AND contareceber.situacao = 'AR' ");

		}
		else if(situacaoContaReceber.equals("recebido")) {
			sqlStr.append("AND contareceber.situacao = 'RE' ");			
		}
		else {
			sqlStr.append("AND contareceber.situacao IN ('AR', 'RE') ");	
		}

		sqlStr.append("GROUP BY cidade.nome, ");	
		sqlStr.append("estado.sigla, ");
		if(layout.equals("analitico")) {
			sqlStr.append("parceiro.nome, ");
		}
		sqlStr.append("imposto.nome ");

		sqlStr.append("ORDER BY cidade.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, layout);

	}

	@Override
	public List<ImpostosRetidosContaReceberRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, String layout) throws Exception {
		List<ImpostosRetidosContaReceberRelVO> vetResultado = new ArrayList<ImpostosRetidosContaReceberRelVO>(0);
		while (tabelaResultado.next()) {
			ImpostosRetidosContaReceberRelVO impostosRetidosContaReceberRelVO = montarDados(tabelaResultado, layout);
			vetResultado.add(impostosRetidosContaReceberRelVO);
		}
		return vetResultado;
	}

	@Override
	public ImpostosRetidosContaReceberRelVO montarDados(SqlRowSet dadosSQL, String layout) throws Exception {
		ImpostosRetidosContaReceberRelVO obj = new ImpostosRetidosContaReceberRelVO();
		obj.setCidade(dadosSQL.getString("cidade"));
		obj.setEstado(dadosSQL.getString("estado"));
		obj.setImposto(dadosSQL.getString("imposto"));
		obj.setValor(dadosSQL.getDouble("valorImposto"));
		if(layout.equals("analitico")) { 
			obj.setParceiro(dadosSQL.getString("parceiro"));
		}
		return obj;
	}

	public List<ImpostoVO> criarListaImposto(List<UnidadeEnsinoVO> listaUnidadeEnsino, ParceiroVO parceiro, EstadoVO estado, CidadeVO cidade, List<ImpostoVO>listaImpostos, Date dataInicio, Date dataFim, String situacaoContaReceber) throws Exception {		

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT ");
		sqlStr.append("imposto.nome as imposto, ");
		sqlStr.append("sum(planodescontocontareceber.valorutilizadorecebimento) as valorImposto ");
		sqlStr.append("FROM contareceber ");
		sqlStr.append("INNER JOIN parceiro on (contareceber.parceiro=parceiro.codigo) ");
		sqlStr.append("INNER JOIN unidadeensino on (contareceber.unidadeensinofinanceira=unidadeensino.codigo) ");
		sqlStr.append("INNER JOIN cidade on (unidadeensino.cidade=cidade.codigo) ");
		sqlStr.append("INNER JOIN estado on (cidade.estado=estado.codigo) ");
		sqlStr.append("INNER JOIN planodescontocontareceber on (contareceber.codigo=planodescontocontareceber.contareceber) ");
		sqlStr.append("INNER JOIN imposto on (planodescontocontareceber.imposto=imposto.codigo) ");
		sqlStr.append("WHERE contareceber.tipoorigem = 'BCC' ");

		if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlStr.append("AND unidadeensino.codigo IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlStr.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlStr.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlStr.append(") ");
		}

		if(parceiro.getCodigo() != null && parceiro.getCodigo() != 0) {
			sqlStr.append("AND parceiro.codigo = ").append(parceiro.getCodigo());

		}

		if(estado.getCodigo() != null && estado.getCodigo() != 0) {
			sqlStr.append(" AND estado.codigo = ").append(estado.getCodigo());

		}

		if(cidade.getCodigo() != null && cidade.getCodigo() != 0) {
			sqlStr.append(" AND cidade.codigo = ").append(cidade.getCodigo());

		}

		if (!listaImpostos.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND imposto.codigo IN(");
			for (ImpostoVO impostoVo : listaImpostos) {
				if (impostoVo.getFiltrarImposto()) {
					if (!virgula) {
						sqlStr.append(impostoVo.getCodigo());
					} else {
						sqlStr.append(", ").append(impostoVo.getCodigo());
					}
					virgula = true;
				}
			}
			sqlStr.append(") ");
		}

		if (dataInicio != null) {
			sqlStr.append("AND contareceber.datavencimento BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("AND contareceber.datavencimento < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}

		if(situacaoContaReceber.equals("aReceber")) {
			sqlStr.append("AND contareceber.situacao = 'AR' ");

		}
		else if(situacaoContaReceber.equals("recebido")) {
			sqlStr.append("AND contareceber.situacao = 'RE' ");			
		}
		else {
			sqlStr.append("AND contareceber.situacao IN ('AR', 'RE') ");	
		}

		sqlStr.append("GROUP BY imposto.nome ");		
		sqlStr.append("ORDER BY imposto.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<ImpostoVO> impostoVOs = new ArrayList<ImpostoVO>(0);		

		while (tabelaResultado.next()) {
			ImpostoVO impostoVO =  new ImpostoVO();
			impostoVO.setNome(tabelaResultado.getString("imposto"));
			impostoVO.setValorTotalImposto(tabelaResultado.getDouble("valorImposto"));
			impostoVOs.add(impostoVO);	
		}
		return impostoVOs;
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro");
	}

	@Override
	public String designIReportRelatorio(String layout) {
		if(layout.equals("analitico")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "ImpostosRetidosContaReceberRelAnalitico" + ".jrxml");
		}
		else {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
		}
	}

	@Override
	public String designIReportRelatorioExcel(String layout) {
		if(layout.equals("analitico")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "ImpostosRetidosContaReceberRelAnalitico" + ".jrxml");
		}
		else {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
		}
	}


	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	@Override
	public void validarDados(Date dataInicio, Date dataFim) throws Exception {

		if(dataInicio == null) {
			throw new Exception("A Data Inicial deve ser informada para a geração do relatório.");
		}
		else if (dataFim == null) {
			throw new Exception("A Data Final deve ser informada para a geração do relatório.");
		}else { 

			ZoneId defaultZoneId = ZoneId.systemDefault();				

			LocalDate ldDataInicio = dataInicio.toInstant().atZone(defaultZoneId).toLocalDate();
			LocalDate ldDataFim = dataFim.toInstant().atZone(defaultZoneId).toLocalDate();

			Period periodo = Period.between(ldDataInicio, ldDataFim);

			if((periodo.getYears() > 1) || (periodo.getYears() == 1 && (periodo.getMonths() > 0 || periodo.getDays() > 0))) {
				throw new Exception("O intervalo deve ser menor que um ano para a geração do relatório.");
			}

			if (ldDataInicio.isAfter(ldDataFim)) {
				throw new Exception("A Data Inicial não pode ser maior que a data Final.");
			}
		}
	}

	public static void setIdEntidade(String idEntidade) {
		ImpostosRetidosContaReceberRel.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

}
