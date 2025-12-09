package relatorio.negocio.jdbc.faturamento.nfe;

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
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.faturamento.nfe.ImpostosRetidosNotaFiscalEntradaRelVO;
import relatorio.negocio.interfaces.faturamento.nfe.ImpostosRetidosNotaFiscalEntradaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings({"serial","deprecation"})
@Scope("singleton")
@Repository
@Lazy
public class ImpostosRetidosNotaFiscalEntradaRel extends SuperRelatorio implements ImpostosRetidosNotaFiscalEntradaRelInterfaceFacade {

	private static String idEntidade;

	public ImpostosRetidosNotaFiscalEntradaRel() {
		setIdEntidade("ImpostosRetidosNotaFiscalEntradaRel");
	}

	@Override
	public List<ImpostosRetidosNotaFiscalEntradaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado, CidadeVO cidade, List<ImpostoVO>listaImpostos, Date dataInicio, Date dataFim,Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim, String layout, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT unidadeensino.nome as unidadeensino, ");
		sqlStr.append("fornecedor.nome as fornecedor, "); 
		sqlStr.append("imposto.nome as imposto, ");
		if(layout.equals("analitico")) {
			sqlStr.append("contapagar.datavencimento as dataVencimentoNota, ");
			sqlStr.append("notafiscalentrada.numero as numeroNota, ");
			sqlStr.append("notafiscalentrada.totalnotaentrada as valorNota, ");
			sqlStr.append("notafiscalentrada.dataemissao as dataEmissaoNota, ");
			sqlStr.append("notafiscalentrada.dataEntrada as dataEntradaNota, ");
			sqlStr.append("notafiscalentradaimposto.porcentagem as porcentagemImposto, ");
			sqlStr.append("notafiscalentradaimposto.valor as valorImposto ");
		}else {
		sqlStr.append("sum(notafiscalentradaimposto.valor) as valorImposto ");
		}
		sqlStr.append("FROM notafiscalentrada ");
		sqlStr.append("INNER JOIN fornecedor on (notafiscalentrada.fornecedor=fornecedor.codigo) ");
		sqlStr.append("INNER JOIN unidadeensino on (notafiscalentrada.unidadeensino=unidadeensino.codigo) ");
		sqlStr.append("INNER JOIN cidade on (fornecedor.cidade=cidade.codigo) ");
		sqlStr.append("INNER JOIN estado on (cidade.estado=estado.codigo) ");
		sqlStr.append("INNER JOIN notafiscalentradaimposto on (notafiscalentrada.codigo=notafiscalentradaimposto.notafiscalentrada) ");
		sqlStr.append("INNER JOIN imposto on (notafiscalentradaimposto.imposto=imposto.codigo) ");
		sqlStr.append("INNER JOIN contapagar on notafiscalentrada.codigo = any(string_to_array(contapagar.codigonotafiscalentrada, ',')::int[]) ");
		sqlStr.append("and contapagar.codigo = (select cp.codigo from contapagar cp where notafiscalentrada.codigo = any(string_to_array(cp.codigonotafiscalentrada, ',')::int[])  order by cp.datavencimento,cp.codigo limit 1) ");
		sqlStr.append("WHERE notafiscalentradaimposto.retido is true ");

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

		if(fornecedor.getCodigo() != null && fornecedor.getCodigo() != 0) {
			sqlStr.append("AND fornecedor.codigo = ").append(fornecedor.getCodigo());

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
			sqlStr.append("AND notafiscalentrada.dataEntrada BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("AND notafiscalentrada.dataEntrada < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}
		
		sqlStr.append(getSqlWhereDataEmissaoDataVencimento(dataEmissaoInicio,dataEmissaoFim,dataVencimentoInicio,dataVencimentoFim));

		if(layout.equals("sintetico")) {
			sqlStr.append("GROUP BY unidadeensino.nome, ");	
			sqlStr.append("fornecedor.nome, ");
			sqlStr.append("imposto.nome ");
		}
		
		sqlStr.append("ORDER BY unidadeensino.nome, fornecedor.nome");
		if(layout.equals("analitico")) {
			sqlStr.append(", notafiscalentrada.numero");
		}
		sqlStr.append(", imposto.nome");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, layout);

	}

	@Override
	public List<ImpostosRetidosNotaFiscalEntradaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, String layout) throws Exception {
		List<ImpostosRetidosNotaFiscalEntradaRelVO> vetResultado = new ArrayList<ImpostosRetidosNotaFiscalEntradaRelVO>(0);
		while (tabelaResultado.next()) {
			ImpostosRetidosNotaFiscalEntradaRelVO impostosRetidosNotaFiscalEntradaRelVO = montarDados(tabelaResultado, layout);
			vetResultado.add(impostosRetidosNotaFiscalEntradaRelVO);
		}
		return vetResultado;
	}

	@Override
	public ImpostosRetidosNotaFiscalEntradaRelVO montarDados(SqlRowSet dadosSQL, String layout) throws Exception {
		ImpostosRetidosNotaFiscalEntradaRelVO obj = new ImpostosRetidosNotaFiscalEntradaRelVO();
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeensino"));
		obj.setFornecedor(dadosSQL.getString("fornecedor"));
		obj.setImposto(dadosSQL.getString("imposto"));
		obj.setValorImposto(dadosSQL.getDouble("valorImposto"));
		if(layout.equals("analitico")) { 
			obj.setNumeroNota(dadosSQL.getInt("numeroNota"));
			obj.setDataEmissaoNota(dadosSQL.getDate("dataEmissaoNota"));
			obj.setDataEntradaNota(dadosSQL.getDate("dataEntradaNota"));
			obj.setDataVencimentoNota(dadosSQL.getDate("dataVencimentoNota"));
			obj.setValorNota(dadosSQL.getDouble("valorNota"));
			obj.setPorcentagemImposto(dadosSQL.getDouble("porcentagemImposto"));
		}
		return obj;
	}

	public List<ImpostoVO> criarListaImposto(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado, CidadeVO cidade, List<ImpostoVO>listaImpostos, Date dataInicio, Date dataFim,Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception {		

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT ");
		sqlStr.append("imposto.nome as imposto, ");
		sqlStr.append("sum(notafiscalentradaimposto.valor) as valorImposto ");
		sqlStr.append("FROM notafiscalentrada ");
		sqlStr.append("INNER JOIN fornecedor on (notafiscalentrada.fornecedor=fornecedor.codigo) ");
		sqlStr.append("INNER JOIN unidadeensino on (notafiscalentrada.unidadeensino=unidadeensino.codigo) ");
		sqlStr.append("INNER JOIN cidade on (fornecedor.cidade=cidade.codigo) ");
		sqlStr.append("INNER JOIN estado on (cidade.estado=estado.codigo) ");
		sqlStr.append("INNER JOIN notafiscalentradaimposto on (notafiscalentrada.codigo=notafiscalentradaimposto.notafiscalentrada) ");
		sqlStr.append("INNER JOIN imposto on (notafiscalentradaimposto.imposto=imposto.codigo) ");
		sqlStr.append("INNER JOIN contapagar on notafiscalentrada.codigo = any(string_to_array(contapagar.codigonotafiscalentrada, ',')::int[]) ");
		sqlStr.append("and contapagar.codigo = (select cp.codigo from contapagar cp where notafiscalentrada.codigo = any(string_to_array(cp.codigonotafiscalentrada, ',')::int[])  order by cp.datavencimento,cp.codigo limit 1) ");
		sqlStr.append("WHERE notafiscalentradaimposto.retido is true ");

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

		if(fornecedor.getCodigo() != null && fornecedor.getCodigo() != 0) {
			sqlStr.append("AND fornecedor.codigo = ").append(fornecedor.getCodigo());

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
			sqlStr.append("AND notafiscalentrada.dataEntrada BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("AND notafiscalentrada.dataEntrada < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}
		
		sqlStr.append(getSqlWhereDataEmissaoDataVencimento(dataEmissaoInicio,dataEmissaoFim,dataVencimentoInicio,dataVencimentoFim));
		
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

	public List<ImpostosRetidosNotaFiscalEntradaRelVO> criarListaImpostoPorUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado, CidadeVO cidade, List<ImpostoVO>listaImpostos, Date dataInicio, Date dataFim,Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception {		

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT ");
		sqlStr.append("unidadeensino.nome as unidadeensino, ");
		sqlStr.append("imposto.nome as imposto, ");
		sqlStr.append("sum(notafiscalentradaimposto.valor) as valorImposto ");
		sqlStr.append("FROM notafiscalentrada ");
		sqlStr.append("INNER JOIN fornecedor on (notafiscalentrada.fornecedor=fornecedor.codigo) ");
		sqlStr.append("INNER JOIN unidadeensino on (notafiscalentrada.unidadeensino=unidadeensino.codigo) ");
		sqlStr.append("INNER JOIN cidade on (fornecedor.cidade=cidade.codigo) ");
		sqlStr.append("INNER JOIN estado on (cidade.estado=estado.codigo) ");
		sqlStr.append("INNER JOIN notafiscalentradaimposto on (notafiscalentrada.codigo=notafiscalentradaimposto.notafiscalentrada) ");
		sqlStr.append("INNER JOIN imposto on (notafiscalentradaimposto.imposto=imposto.codigo) ");
		sqlStr.append("INNER JOIN contapagar on notafiscalentrada.codigo = any(string_to_array(contapagar.codigonotafiscalentrada, ',')::int[]) ");
		sqlStr.append("and contapagar.codigo = (select cp.codigo from contapagar cp where notafiscalentrada.codigo = any(string_to_array(cp.codigonotafiscalentrada, ',')::int[])  order by cp.datavencimento,cp.codigo limit 1) ");
		sqlStr.append("WHERE notafiscalentradaimposto.retido is true ");

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

		if(fornecedor.getCodigo() != null && fornecedor.getCodigo() != 0) {
			sqlStr.append("AND fornecedor.codigo = ").append(fornecedor.getCodigo());

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
			sqlStr.append("AND notafiscalentrada.dataEntrada BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("AND notafiscalentrada.dataEntrada < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}
		
		sqlStr.append(getSqlWhereDataEmissaoDataVencimento(dataEmissaoInicio,dataEmissaoFim,dataVencimentoInicio,dataVencimentoFim));

		sqlStr.append("GROUP BY unidadeensino.nome, imposto.nome ");		
		sqlStr.append("ORDER BY unidadeensino.nome, imposto.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<ImpostosRetidosNotaFiscalEntradaRelVO> listaImpostosPorUnidadeEnsino = new ArrayList<ImpostosRetidosNotaFiscalEntradaRelVO>(0);		

		while (tabelaResultado.next()) {
			ImpostosRetidosNotaFiscalEntradaRelVO impostosPorUnidadeEnsino =  new ImpostosRetidosNotaFiscalEntradaRelVO();
			impostosPorUnidadeEnsino.setUnidadeEnsino(tabelaResultado.getString("unidadeensino"));
			impostosPorUnidadeEnsino.setImposto(tabelaResultado.getString("imposto"));
			impostosPorUnidadeEnsino.setValorImposto(tabelaResultado.getDouble("valorImposto"));
			listaImpostosPorUnidadeEnsino.add(impostosPorUnidadeEnsino);	
		}
		return listaImpostosPorUnidadeEnsino;
	}
	
	private StringBuilder getSqlWhereDataEmissaoDataVencimento(Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) {
		StringBuilder sqlStr = new StringBuilder("");
		if(dataEmissaoInicio != null) {
			sqlStr.append("AND notafiscalentrada.dataemissao >= '").append(Uteis.getDataBD0000(dataEmissaoInicio)).append("'");
		}
		if(dataEmissaoFim != null) {
			sqlStr.append("AND notafiscalentrada.dataemissao <= '").append(Uteis.getDataBD2359(dataEmissaoFim)).append("'");
		}
		
		if(Uteis.isAtributoPreenchido(dataVencimentoInicio) && Uteis.isAtributoPreenchido(dataVencimentoFim)){
			sqlStr.append("and ( select cp.datavencimento from contapagar cp  ");
			sqlStr.append(" where ");
			sqlStr.append(" cp.codigo = contapagar.codigo ");
			sqlStr.append(" order by datavencimento limit 1 ");
			sqlStr.append(" ) ");
			sqlStr.append(" between '");
			sqlStr.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataVencimentoInicio)));
			sqlStr.append("' and '");
			sqlStr.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataVencimentoFim)));
			sqlStr.append("' ");
		}else if(Uteis.isAtributoPreenchido(dataVencimentoInicio)) {
			sqlStr.append("and ( select cp.datavencimento from contapagar cp ");
			sqlStr.append(" where ");
			sqlStr.append(" cp.codigo = contapagar.codigo ");
			sqlStr.append(" order by datavencimento limit 1 ");
			sqlStr.append(" ) ");
			sqlStr.append(" >= '");
			sqlStr.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataVencimentoInicio)));
			sqlStr.append("' ");
		}else if(Uteis.isAtributoPreenchido(dataVencimentoFim)) {
			sqlStr.append("and ( select cp.datavencimento from contapagar cp  ");
			sqlStr.append(" where ");
			sqlStr.append(" cp.codigo = contapagar.codigo ");
			sqlStr.append(" order by datavencimento limit 1 ");
			sqlStr.append(" ) ");
			sqlStr.append(" <= '");
			sqlStr.append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataVencimentoFim)));
			sqlStr.append("' ");
		}
		return sqlStr;
	}

	public List<ImpostosRetidosNotaFiscalEntradaRelVO> criarListaImpostoPorFornecedor(List<UnidadeEnsinoVO> listaUnidadeEnsino, FornecedorVO fornecedor, EstadoVO estado, CidadeVO cidade, List<ImpostoVO>listaImpostos, Date dataInicio, Date dataFim,Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception {		

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT ");
		sqlStr.append("unidadeensino.nome as unidadeensino, ");
		sqlStr.append("fornecedor.nome as fornecedor, ");
		sqlStr.append("imposto.nome as imposto, ");
		sqlStr.append("sum(notafiscalentradaimposto.valor) as valorImposto ");
		sqlStr.append("FROM notafiscalentrada ");
		sqlStr.append("INNER JOIN fornecedor on (notafiscalentrada.fornecedor=fornecedor.codigo) ");
		sqlStr.append("INNER JOIN unidadeensino on (notafiscalentrada.unidadeensino=unidadeensino.codigo) ");
		sqlStr.append("INNER JOIN cidade on (fornecedor.cidade=cidade.codigo) ");
		sqlStr.append("INNER JOIN estado on (cidade.estado=estado.codigo) ");
		sqlStr.append("INNER JOIN notafiscalentradaimposto on (notafiscalentrada.codigo=notafiscalentradaimposto.notafiscalentrada) ");
		sqlStr.append("INNER JOIN imposto on (notafiscalentradaimposto.imposto=imposto.codigo) ");
		sqlStr.append("INNER JOIN contapagar on notafiscalentrada.codigo = any(string_to_array(contapagar.codigonotafiscalentrada, ',')::int[]) ");
		sqlStr.append("and contapagar.codigo = (select cp.codigo from contapagar cp where notafiscalentrada.codigo = any(string_to_array(cp.codigonotafiscalentrada, ',')::int[])  order by cp.datavencimento,cp.codigo limit 1) ");
		sqlStr.append("WHERE notafiscalentradaimposto.retido is true ");

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

		if(fornecedor.getCodigo() != null && fornecedor.getCodigo() != 0) {
			sqlStr.append("AND fornecedor.codigo = ").append(fornecedor.getCodigo());

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
			sqlStr.append("AND notafiscalentrada.dataEntrada BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("AND notafiscalentrada.dataEntrada < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}
		
		sqlStr.append(getSqlWhereDataEmissaoDataVencimento(dataEmissaoInicio,dataEmissaoFim,dataVencimentoInicio,dataVencimentoFim));

		sqlStr.append("GROUP BY unidadeensino.nome, fornecedor.nome, imposto.nome ");		
		sqlStr.append("ORDER BY unidadeensino.nome, fornecedor.nome, imposto.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<ImpostosRetidosNotaFiscalEntradaRelVO> listaImpostosPorFornecedor = new ArrayList<ImpostosRetidosNotaFiscalEntradaRelVO>(0);		

		while (tabelaResultado.next()) {
			ImpostosRetidosNotaFiscalEntradaRelVO impostosPorFornecedor =  new ImpostosRetidosNotaFiscalEntradaRelVO();
			impostosPorFornecedor.setFornecedor(tabelaResultado.getString("fornecedor"));
			impostosPorFornecedor.setUnidadeEnsino(tabelaResultado.getString("unidadeEnsino"));
			impostosPorFornecedor.setImposto(tabelaResultado.getString("imposto"));
			impostosPorFornecedor.setValorImposto(tabelaResultado.getDouble("valorImposto"));
			listaImpostosPorFornecedor.add(impostosPorFornecedor);	
		}
		return listaImpostosPorFornecedor;
	}
	
	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator + "nfe");
	}

	@Override
	public String designIReportRelatorio(String layout) {
		if(layout.equals("analitico")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator + "nfe" + File.separator + "ImpostosRetidosNotaFiscalEntradaRelAnalitico" + ".jrxml");
		}
		else {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator + "nfe" + File.separator + getIdEntidade() + ".jrxml");
		}
	}

	@Override
	public String designIReportRelatorioExcel(String layout) {
		if(layout.equals("analitico")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator + "nfe" + File.separator + "ImpostosRetidosNotaFiscalEntradaRelAnaliticoExcel" + ".jrxml");
		}
		else {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator + "nfe" + File.separator + getIdEntidade() + ".jrxml");
		}
	}


	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "faturamento" + File.separator + "nfe" + File.separator);
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
		ImpostosRetidosNotaFiscalEntradaRel.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	@Override
	public void validarData(Date dataEmissaoInicio, Date dataEmissaoFim, Date dataVencimentoInicio,Date dataVencimentoFim) throws Exception {
		ZoneId defaultZoneId = ZoneId.systemDefault();	
		if(dataEmissaoInicio != null && dataEmissaoFim!= null) {
			LocalDate ldDataEmissaoInicio = dataEmissaoInicio.toInstant().atZone(defaultZoneId).toLocalDate();
			LocalDate ldDataEmissaoFim = dataEmissaoFim.toInstant().atZone(defaultZoneId).toLocalDate();
			if (ldDataEmissaoInicio.isAfter(ldDataEmissaoFim)) {
				throw new Exception("A Data de Emissão Inicial  não pode ser maior que a data Final de Emissão.");
			}
		}else if(dataVencimentoInicio != null && dataVencimentoFim!= null) {
			LocalDate ldDataVencimentoInicio = dataEmissaoInicio.toInstant().atZone(defaultZoneId).toLocalDate();
			LocalDate ldDataVencimentoFim = dataEmissaoFim.toInstant().atZone(defaultZoneId).toLocalDate();
			if (ldDataVencimentoInicio.isAfter(ldDataVencimentoFim)) {
				throw new Exception("A Data de Vencimento Inicial  não pode ser maior que a data Final de Vencimento.");
			}
		}
		
	}

}
