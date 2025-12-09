/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.facade.jdbc.administrativo;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.ajax4jsf.util.HtmlColor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.ContaReceberPainelGestorVO;
import negocio.comuns.administrativo.PainelGestorContaPagarMesAnoVO;
import negocio.comuns.administrativo.PainelGestorContaReceberMesAnoVO;
import negocio.comuns.administrativo.PainelGestorDetalhamentoDescontoVO;
import negocio.comuns.administrativo.PainelGestorFinanceiroAcademicoMesAnoVO;
import negocio.comuns.administrativo.PainelGestorFinanceiroAcademicoNivelEducacionalVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoConsultorDetalhamentoVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoConsultorVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDescontoConvenioVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDescontoInstituicaoVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDescontoMesAnoVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDescontoNivelEducacionalVO;
import negocio.comuns.administrativo.PainelGestorMonitoramentoDescontoProgressivoVO;
import negocio.comuns.administrativo.PainelGestorVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.enumerador.ReceitaDespesaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemMensalDetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.PlanoOrcamentarioVO;
import negocio.comuns.sad.LegendaGraficoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.PainelGestorInterfaceFacade;

/**
 * 
 * @author Rodrigo
 */
@Service
@Lazy
public class PainelGestor extends ControleAcesso implements PainelGestorInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8037016160462465003L;

	/**
	 * Método responsável por inicializar as planílhas de Mapa de Receitas por
	 * Competência, Mapa de Receitas por Fluxo caixa, Mapa de Despesas e Gráfico
	 * de ReceitaXDespesa.
	 * 
	 * @author CarlosEugenio 24/01/2014
	 * 
	 */
	public void executarInicializacaoDadoFinanceiroPainelGestor(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String filtrarPeriodoPor) throws Exception {
		try {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
			validarDadosFiltroBusca(dataInicio, dataTermino);
			validarDadosUnidadeEnsino(unidadeEnsinoVOs);
			// executarCriacaoGraficoRecebimentoPagamento(painelGestorVO,
			// unidadeEnsinoVOs, dataInicio, dataTermino);
			executarCriacaoGraficoLinhaRecebimentoPagamento(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, false);
			consultarMapaReceitaPorCompetenciaPainelGestorFinanceiroPorPeriodo(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, filtrarPeriodoPor);
			consultarMapaReceitaPorFluxoCaixaPainelGestorFinanceiroPorPeriodo(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino);
			consultarMapaDespesaPainelGestorFinanceiro(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino);
			executarCriacaoGraficoCategoriaDespesa(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, "", null, true, 5, 0);
			// consultarDadosPainelGestorFinanceiroAcademico(painelGestorVO,
			// unidadeEnsinoVOs, dataInicio, dataTermino);
		} catch (Exception e) {
			painelGestorVO.getLegendaGraficoReceitaDespesaVOs().clear();
			painelGestorVO.getPainelGestorContaPagarMesAnoVOs().clear();
			painelGestorVO.getPainelGestorContaReceberMesAnoVOs().clear();
			painelGestorVO.getPainelGestorContaReceberFluxoCaixaMesAnoVOs().clear();
			painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
			throw e;
		}
	}

	/**
	 * Método responsável por detalhar os valores expostos no mapa de receita do
	 * painel gestor financeiro, mostrando conta por conta.
	 * 
	 * @author CarlosEugenio
	 */
	public void executarDetalhamentoReceitaDoMesPorMesAnoETipoOrigemSituacaoTipoMapareceitaTipoDesconto(PainelGestorVO painelGestorVO, PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO, String tipoOrigem, String situacao, String tipoMapaReceita, String tipoDesconto, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, Integer codigoTurma, String filtarPeriodoPor) throws Exception {
		int mes = Integer.parseInt(MesAnoEnum.getEnum(painelGestorContaReceberMesAnoVO.getMesAno().substring(0, painelGestorContaReceberMesAnoVO.getMesAno().indexOf("/"))).getKey());
		int ano = Integer.parseInt(painelGestorContaReceberMesAnoVO.getMesAno().substring(painelGestorContaReceberMesAnoVO.getMesAno().length() - 4, painelGestorContaReceberMesAnoVO.getMesAno().length()));
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		consultarDetalhamentoContaReceberDoMesPorMesAnoTipoOrigem(painelGestorContaReceberMesAnoVO, mes, ano, tipoOrigem, situacao, tipoMapaReceita, tipoDesconto, unidadeEnsinoVOs, dataInicio, dataTermino, configuracaoFinanceiroVO, codigoCurso, codigoTurma, filtarPeriodoPor);
		realizarCalculoTotalValoresContaReceber(painelGestorContaReceberMesAnoVO);
		painelGestorVO.getPainelGestorDetalhamentoDescontoVOs().clear();
		if (tipoDesconto.equals("CONVENIO") || tipoDesconto.equals("INSTITUICAO") || tipoDesconto.equals("PROGRESSIVO")) {
			consultarDadosFinanceiroDetalhamentoDesconto(painelGestorVO, painelGestorContaReceberMesAnoVO, mes, ano, tipoOrigem, situacao, tipoMapaReceita, tipoDesconto, unidadeEnsinoVOs, dataInicio, dataTermino, configuracaoFinanceiroVO, filtarPeriodoPor);
		}
	}

	public void consultarDetalhamentoContaReceberDoMesPorMesAnoTipoOrigem(PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO, int mes, int ano, String tipoOrigem, String situacao, String tipoMapaReceita, String tipoDesconto, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, Integer codigoTurma, String filtarPeriodoPor) throws Exception {
		if (tipoMapaReceita.equals("FLUXO_CAIXA")) {
			filtarPeriodoPor = "datavencimento";
		}
			
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct contareceber.codigo, nossonumero, datavencimento as datavencimento, datacompetencia as datacompetencia, parcela, contareceber.situacao, ");
		sb.append(" descontoConvenio, descontoinstituicao, valordescontoprogressivo as descontoprogressivo, valorcalculadodescontolancadorecebimento as descontorecebimento, ");
		sb.append(" valordescontoalunojacalculado as descontoAluno, valor, juro, multa, acrescimo, contaReceber.valorRecebido, contaReceber.valorDescontoRateio, contareceber.valorReceberCalculado, ");
		// Dados do Aluno
		sb.append(" pessoa.nome AS \"pessoa.nome\", pessoa.codigo AS \"pessoa.codigo\", matricula.matricula, ");
		// Dados do Parceiro
		sb.append(" parceiro.nome AS \"parceiro.nome\", parceiro.codigo AS \"parceiro.codigo\", ");
		// Dados do Responsável Financeiro
		sb.append(" responsavelfinanceiro.nome AS \"responsavelfinanceiro.nome\", responsavelfinanceiro.codigo AS \"responsavelfinanceiro.codigo\", ");
		// Dados do Candidato
		sb.append(" candidato.nome AS \"candidato.nome\", candidato.codigo AS \"candidato.codigo\",  ");
		// Dados do Funcionário
		sb.append(" pessoafuncionario.nome AS \"pessoafuncionario.nome\", pessoafuncionario.codigo AS \"pessoafuncionario.codigo\", ");
		// Dados do Fornecedor
		sb.append(" fornecedor.nome AS \"fornecedor.nome\", fornecedor.codigo AS \"fornecedor.codigo\", contareceber.tipopessoa ");
		sb.append(" from contareceber ");
		if (tipoMapaReceita.equals("FLUXO_CAIXA")) {
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
			sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		}
		sb.append(" left join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sb.append(" left join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sb.append(" left join pessoa responsavelfinanceiro on responsavelfinanceiro.codigo = contareceber.responsavelfinanceiro ");
		sb.append(" left join pessoa candidato on candidato.codigo = contareceber.candidato ");
		sb.append(" left join funcionario on funcionario.codigo = contareceber.funcionario ");
		sb.append(" left join pessoa pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sb.append(" left join fornecedor on fornecedor.codigo = contareceber.fornecedor ");
		if (!tipoOrigem.equals("")) {
			sb.append(" where tipoorigem in('").append(tipoOrigem).append("') ");
		} else if (tipoOrigem.equals("OUT")) {
			sb.append(" where tipoorigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') ");
		} else {
			sb.append(" where 1=1 ");
		}
		if (!codigoCurso.equals(0)) {
			sb.append(" and matricula.curso = ").append(codigoCurso);
		}
		if (!codigoTurma.equals(0)) {
			sb.append(" and matriculaPeriodo.Turma = ").append(codigoTurma);
		}
		if (!situacao.equals("")) {
			sb.append(" and contaReceber.situacao in('").append(situacao).append("') ");
		} else {
			sb.append(" and contareceber.situacao not in('NE', 'CF', 'RM') ");
		}
		if (tipoMapaReceita.equals("FLUXO_CAIXA")) {
			sb.append(" and extract(month from negociacaorecebimento.data)::INT = ").append(mes);
			sb.append(" and extract(year from negociacaorecebimento.data)::INT = ").append(ano);
		} else {
			sb.append(" and extract(month from "+filtarPeriodoPor+")::INT = ").append(mes);
			sb.append(" and extract(year from "+filtarPeriodoPor+")::INT = ").append(ano);
		}
		// Define o tipo de desconto que deverá ser mostrado no detalhamento
		if (tipoDesconto.equals("CONVENIO")) {
			sb.append(" and contareceber.descontoConvenio > 0 ");
		}else	if (tipoDesconto.equals("RATEIO")) {
				sb.append(" and contareceber.valorDescontoRateio > 0 ");
		} else if (tipoDesconto.equals("INSTITUICAO")) {
			sb.append(" and contareceber.descontoInstituicao > 0 ");
		} else if (tipoDesconto.equals("PROGRESSIVO")) {
			sb.append(" and contareceber.valorDescontoProgressivo > 0 ");
		} else if (tipoDesconto.equals("ALUNO")) {
			sb.append(" and contareceber.valordescontoalunojacalculado > 0 ");
		} else if (tipoDesconto.equals("RECEBIMENTO")) {
			sb.append(" and contareceber.valorcalculadodescontolancadorecebimento > 0 ");
		} else if (tipoDesconto.equals("TODOS")) {
			sb.append(" and (contareceber.descontoConvenio > 0 or contareceber.descontoInstituicao > 0 or contareceber.valorDescontoProgressivo > 0 or contareceber.valordescontoalunojacalculado > 0 or contareceber.valorcalculadodescontolancadorecebimento > 0 or contareceber.valordescontorateio > 0) ");
		}
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(" order by "+filtarPeriodoPor+", parcela, situacao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorContaReceberMesAnoVO.getContaReceberPainelGestorVOs().clear();
		while (tabelaResultado.next()) {
			ContaReceberPainelGestorVO obj = new ContaReceberPainelGestorVO();
			obj.getContaReceberVO().setCodigo(tabelaResultado.getInt("codigo"));
			obj.getContaReceberVO().setNossoNumero(tabelaResultado.getString("nossoNumero"));
			obj.getContaReceberVO().setDataVencimento(tabelaResultado.getDate("dataVencimento"));
			obj.getContaReceberVO().setDataCompetencia(tabelaResultado.getDate("datacompetencia"));
			obj.getContaReceberVO().setParcela(tabelaResultado.getString("parcela"));
			obj.getContaReceberVO().setSituacao(tabelaResultado.getString("situacao"));
			obj.getContaReceberVO().setValorDescontoConvenio(tabelaResultado.getDouble("descontoConvenio"));
			obj.getContaReceberVO().setValorDescontoInstituicao(tabelaResultado.getDouble("descontoInstituicao"));
			obj.getContaReceberVO().setValorDescontoProgressivo(tabelaResultado.getDouble("descontoProgressivo"));
			obj.getContaReceberVO().setValorDescontoRateio(tabelaResultado.getDouble("valorDescontoRateio"));
			obj.getContaReceberVO().setValorCalculadoDescontoLancadoRecebimento(tabelaResultado.getDouble("descontoRecebimento"));
			obj.getContaReceberVO().setValorDescontoAlunoJaCalculado(tabelaResultado.getDouble("descontoAluno"));
			obj.getContaReceberVO().setValor(tabelaResultado.getDouble("valor"));
			obj.getContaReceberVO().setValorRecebido(tabelaResultado.getDouble("valorRecebido"));

			obj.getContaReceberVO().setJuro(tabelaResultado.getDouble("juro"));
			obj.getContaReceberVO().setMulta(tabelaResultado.getDouble("multa"));
			obj.getContaReceberVO().setAcrescimo(tabelaResultado.getDouble("acrescimo"));
			obj.getContaReceberVO().setTipoPessoa(tabelaResultado.getString("tipoPessoa"));
			// Dados do Aluno
			obj.getContaReceberVO().getMatriculaAluno().setMatricula(tabelaResultado.getString("matricula"));
			obj.getContaReceberVO().getPessoa().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getContaReceberVO().getPessoa().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			// Dados do Parceiro
			obj.getContaReceberVO().getParceiroVO().setCodigo(tabelaResultado.getInt("parceiro.codigo"));
			obj.getContaReceberVO().getParceiroVO().setNome(tabelaResultado.getString("parceiro.nome"));
			// Dados do Responsável Financeiro
			obj.getContaReceberVO().getResponsavelFinanceiro().setNome(tabelaResultado.getString("responsavelfinanceiro.nome"));
			obj.getContaReceberVO().getResponsavelFinanceiro().setCodigo(tabelaResultado.getInt("responsavelfinanceiro.codigo"));
			// Dados do Candidato
			obj.getContaReceberVO().getCandidato().setNome(tabelaResultado.getString("candidato.nome"));
			obj.getContaReceberVO().getCandidato().setCodigo(tabelaResultado.getInt("candidato.codigo"));
			// Dados do Funcionário
			obj.getContaReceberVO().getFuncionario().getPessoa().setNome(tabelaResultado.getString("pessoafuncionario.nome"));
			obj.getContaReceberVO().getFuncionario().getPessoa().setCodigo(tabelaResultado.getInt("pessoafuncionario.codigo"));
			// Dados do Fornecedor
			obj.getContaReceberVO().getFornecedor().setNome(tabelaResultado.getString("fornecedor.nome"));
			obj.getContaReceberVO().getFornecedor().setCodigo(tabelaResultado.getInt("fornecedor.codigo"));

			painelGestorContaReceberMesAnoVO.getContaReceberPainelGestorVOs().add(obj);
		}
	}

	public void consultarDadosFinanceiroDetalhamentoDesconto(PainelGestorVO painelGestorVO, PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO, int mes, int ano, String tipoOrigem, String situacao, String tipoMapaReceita, String tipoDesconto, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String filtrarPeriodoPor) throws Exception {
		if (tipoMapaReceita.equals("FLUXO_CAIXA")) {
			filtrarPeriodoPor = "datavencimento";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(" select nomeCategoriaDesconto, nomeTipoDesconto, sum(totalDesconto::NUMERIC(20,2)) as totalDesconto, count(distinct matricula)::INT as qtdePessoa ");
		sb.append(" from (  ");
		if (tipoDesconto.equals("CONVENIO")) {
			sb.append(" select parceiro.nome AS nomeCategoriaDesconto, convenio.descricao as nomeTipoDesconto,  matriculaaluno as matricula, contareceber.descontoConvenio::NUMERIC(20,2) as totalDesconto ");
		}
		if (tipoDesconto.equals("INSTITUICAO")) {
			sb.append(" select categoriaDesconto.nome AS nomeCategoriaDesconto, planodesconto.nome as nomeTipoDesconto,  matriculaaluno as matricula, planodescontocontareceber.valorUtilizadoRecebimento::NUMERIC(20,2) as totalDesconto ");
		}
		if (tipoDesconto.equals("PROGRESSIVO")) {
			sb.append(" select ''::varchar as nomeCategoriaDesconto, descontoProgressivo.nome as nomeTipoDesconto,  matriculaaluno as matricula, contareceber.valorDescontoProgressivo::NUMERIC(20,2) as totalDesconto ");
		}
		sb.append(" from contareceber ");
		if (tipoMapaReceita.equals("FLUXO_CAIXA")) {
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
			sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		}
		if (tipoDesconto.equals("CONVENIO")) {
			sb.append(" inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo ");
			sb.append(" inner join convenio on convenio.codigo = planodescontocontareceber.convenio ");
			sb.append(" left join parceiro on parceiro.codigo = convenio.parceiro ");
		}
		if (tipoDesconto.equals("INSTITUICAO")) {
			sb.append(" inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo ");
			sb.append(" inner join planoDesconto on planoDesconto.codigo = planodescontocontareceber.planodesconto ");
			sb.append(" left join categoriaDesconto on categoriaDesconto.codigo = planoDesconto.categoriaDesconto ");
		}
		if (tipoDesconto.equals("PROGRESSIVO")) {
			sb.append(" inner join descontoProgressivo on descontoProgressivo.codigo = contaReceber.descontoProgressivo ");
		}
		sb.append(" inner join matricula on contareceber.matriculaaluno = matricula.matricula");
		sb.append(" inner join curso on curso.codigo = matricula.curso");

		if (!tipoOrigem.equals("")) {
			sb.append(" where tipoorigem in('").append(tipoOrigem).append("') ");
		} else if (tipoOrigem.equals("OUT")) {
			sb.append(" where tipoorigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') ");
		} else {
			sb.append(" where 1=1 ");
		}
		if (!situacao.equals("")) {
			sb.append(" and contaReceber.situacao in('").append(situacao).append("') ");
		} else {
			sb.append(" and contareceber.situacao not in('NE', 'CF', 'RM') ");
		}
		if (tipoMapaReceita.equals("FLUXO_CAIXA")) {
			sb.append(" and extract(month from negociacaorecebimento.data)::INT = ").append(mes);
			sb.append(" and extract(year from negociacaorecebimento.data)::INT = ").append(ano);
		} else {
			sb.append(" and extract(month from "+filtrarPeriodoPor+")::INT = ").append(mes);
			sb.append(" and extract(year from "+filtrarPeriodoPor+")::INT = ").append(ano);
		}
		if (tipoDesconto.equals("CONVENIO")) {
			sb.append(" and contareceber.descontoConvenio > 0 ");
		}
		if (tipoDesconto.equals("INSTITUICAO")) {
			sb.append(" and contareceber.descontoInstituicao > 0 ");
		}
		if (tipoDesconto.equals("PROGRESSIVO")) {
			sb.append(" and contareceber.valorDescontoProgressivo > 0 ");
		}
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(" order by "+filtrarPeriodoPor+", parcela, contareceber.situacao");
		sb.append(" ) as t group by nomeCategoriaDesconto, nomeTipoDesconto order by nomeCategoriaDesconto, totalDesconto desc, nomeTipoDesconto");

		painelGestorVO.getPainelGestorDetalhamentoDescontoVOs().clear();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		painelGestorVO.setTotalDescontoDetalhamento(BigDecimal.ZERO);
		painelGestorVO.setTotalNumeroAlunoComDesconto(0);
		HashMap<String, PainelGestorDetalhamentoDescontoVO> mapDetalhamentoDescontoVOs = new HashMap<String, PainelGestorDetalhamentoDescontoVO>(0);
		while (rs.next()) {
			if (!mapDetalhamentoDescontoVOs.containsKey(rs.getString("nomeCategoriaDesconto"))) {
				PainelGestorDetalhamentoDescontoVO painelGestorDetalhamentoDescontoVO = new PainelGestorDetalhamentoDescontoVO();
				painelGestorDetalhamentoDescontoVO.setNomeCategoriaDesconto(rs.getString("nomeCategoriaDesconto"));
				montarDadosPainelGestorFinanceiroDetalhamentoDesconto(painelGestorVO, mapDetalhamentoDescontoVOs, painelGestorDetalhamentoDescontoVO, rs);
			} else {
				PainelGestorDetalhamentoDescontoVO painelGestorDetalhamentoDescontoVO = mapDetalhamentoDescontoVOs.get(rs.getString("nomeCategoriaDesconto"));
				montarDadosPainelGestorFinanceiroDetalhamentoDesconto(painelGestorVO, mapDetalhamentoDescontoVOs, painelGestorDetalhamentoDescontoVO, rs);
			}
		}

		for (PainelGestorDetalhamentoDescontoVO painelGestorDetalhamentoDescontoVO : mapDetalhamentoDescontoVOs.values()) {
			painelGestorVO.getPainelGestorDetalhamentoDescontoVOs().add(painelGestorDetalhamentoDescontoVO);
		}
		Ordenacao.ordenarListaDecrescente(painelGestorVO.getPainelGestorDetalhamentoDescontoVOs(), "nomeCategoriaDesconto");

	}

	public void montarDadosPainelGestorFinanceiroDetalhamentoDesconto(PainelGestorVO painelGestorVO, HashMap<String, PainelGestorDetalhamentoDescontoVO> mapDetalhamentoDescontoVOs, PainelGestorDetalhamentoDescontoVO painelGestorDetalhamentoDescontoCategoriaDescontoVO, SqlRowSet rs) {
		PainelGestorDetalhamentoDescontoVO painelGestorDetalhamentoDescontoVO = new PainelGestorDetalhamentoDescontoVO();
		painelGestorDetalhamentoDescontoVO.setNomeTipoDesconto(rs.getString("nomeTipoDesconto"));
		painelGestorDetalhamentoDescontoVO.setNumeroAlunoComDesconto(rs.getInt("qtdePessoa"));
		painelGestorDetalhamentoDescontoVO.setTotalDesconto(rs.getDouble("totalDesconto"));
		painelGestorDetalhamentoDescontoCategoriaDescontoVO.setTotalDescontoPorCategoria(painelGestorDetalhamentoDescontoCategoriaDescontoVO.getTotalDescontoPorCategoria().add(BigDecimal.valueOf(painelGestorDetalhamentoDescontoVO.getTotalDesconto())));
		painelGestorDetalhamentoDescontoCategoriaDescontoVO.setTotalNumeroAlunoPorCategoria(painelGestorDetalhamentoDescontoCategoriaDescontoVO.getTotalNumeroAlunoPorCategoria() + painelGestorDetalhamentoDescontoVO.getNumeroAlunoComDesconto());
		painelGestorDetalhamentoDescontoCategoriaDescontoVO.getPainelGestorDetalhamentoDescontoVOs().add(painelGestorDetalhamentoDescontoVO);

		painelGestorVO.setTotalDescontoDetalhamento(painelGestorVO.getTotalDescontoDetalhamento().add(BigDecimal.valueOf(painelGestorDetalhamentoDescontoVO.getTotalDesconto())));
		painelGestorVO.setTotalNumeroAlunoComDesconto(painelGestorVO.getTotalNumeroAlunoComDesconto() + painelGestorDetalhamentoDescontoVO.getNumeroAlunoComDesconto());
		mapDetalhamentoDescontoVOs.put(rs.getString("nomeCategoriaDesconto"), painelGestorDetalhamentoDescontoCategoriaDescontoVO);
	}

	public void realizarCalculoTotalValoresContaReceber(PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO) {
		painelGestorContaReceberMesAnoVO.setTotalValorContareceber(BigDecimal.ZERO);
		painelGestorContaReceberMesAnoVO.setTotalValorRecebidoContareceber(BigDecimal.ZERO);
		painelGestorContaReceberMesAnoVO.setTotalValorReceberContareceber(BigDecimal.ZERO);
		painelGestorContaReceberMesAnoVO.setTotalDescontoConvenioContareceber(BigDecimal.ZERO);
		painelGestorContaReceberMesAnoVO.setTotalDescontoInstituicaoContareceber(BigDecimal.ZERO);
		painelGestorContaReceberMesAnoVO.setTotalDescontoProgressivoContareceber(BigDecimal.ZERO);
		painelGestorContaReceberMesAnoVO.setTotalDescontoAlunoContareceber(BigDecimal.ZERO);		
		painelGestorContaReceberMesAnoVO.setTotalDescontoRateioContareceber(BigDecimal.ZERO);		
		painelGestorContaReceberMesAnoVO.setTotalDescontoRecebimentoContareceber(BigDecimal.ZERO);
		for (ContaReceberPainelGestorVO contaReceberPainelVO : painelGestorContaReceberMesAnoVO.getContaReceberPainelGestorVOs()) {
			if(!contaReceberPainelVO.getContaReceberVO().getSituacaoEQuitada()) {
				painelGestorContaReceberMesAnoVO.setTotalValorReceberContareceber(painelGestorContaReceberMesAnoVO.getTotalValorReceberContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorReceberCalculado())));
			}
			painelGestorContaReceberMesAnoVO.setTotalValorContareceber(painelGestorContaReceberMesAnoVO.getTotalValorContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValor())));
			painelGestorContaReceberMesAnoVO.setTotalValorRecebidoContareceber(painelGestorContaReceberMesAnoVO.getTotalValorRecebidoContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorRecebido())));
			painelGestorContaReceberMesAnoVO.setTotalDescontoConvenioContareceber(painelGestorContaReceberMesAnoVO.getTotalDescontoConvenioContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorDescontoConvenio())));
			painelGestorContaReceberMesAnoVO.setTotalDescontoInstituicaoContareceber(painelGestorContaReceberMesAnoVO.getTotalDescontoInstituicaoContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorDescontoInstituicao())));
			painelGestorContaReceberMesAnoVO.setTotalDescontoProgressivoContareceber(painelGestorContaReceberMesAnoVO.getTotalDescontoProgressivoContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorDescontoProgressivo())));
			painelGestorContaReceberMesAnoVO.setTotalDescontoAlunoContareceber(painelGestorContaReceberMesAnoVO.getTotalDescontoAlunoContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorDescontoAlunoJaCalculado())));
			painelGestorContaReceberMesAnoVO.setTotalDescontoRecebimentoContareceber(painelGestorContaReceberMesAnoVO.getTotalDescontoRecebimentoContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorCalculadoDescontoLancadoRecebimento())));
			painelGestorContaReceberMesAnoVO.setTotalDescontoRateioContareceber(painelGestorContaReceberMesAnoVO.getTotalDescontoRateioContareceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getValorDescontoRateio())));
			painelGestorContaReceberMesAnoVO.setTotalAcrescimoContaReceber(painelGestorContaReceberMesAnoVO.getTotalAcrescimoContaReceber().add(BigDecimal.valueOf(contaReceberPainelVO.getContaReceberVO().getTotalAcrescimoContaReceber())));
		}
	}

	public void executarInicializacaoDadosPlanoOrcamentario(PlanoOrcamentarioVO planoOrcamentarioVO, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getPlanoOrcamentarioFacade().carregarDados(planoOrcamentarioVO, usuario);
		planoOrcamentarioVO.setSaldo(planoOrcamentarioVO.getOrcamentoTotalPrevisto() - planoOrcamentarioVO.getOrcamentoTotalRealizado());
		executarMontagemGraficoPorDepartamento(planoOrcamentarioVO);
	}

	//TODO REFAZER REGRA MONTAGEM DOS GRAFICOS
	public void executarMontagemGraficoPorDepartamento(PlanoOrcamentarioVO planoOrcamentarioVO) {
		DefaultCategoryDataset dataSet = new DefaultCategoryDataset();
		Double totalOrcamento = 0.00;
		Double totalValorConsumido = 0.00;
		planoOrcamentarioVO.getLegendaGraficoVOs().clear();
		
		/*for (DetalhamentoPlanoOrcamentarioVO detalhamento : planoOrcamentarioVO.getDetalhamentoPlanoOrcamentario()) {
			int index = 1;
			dataSet.addValue(detalhamento.getOrcamentoTotalDepartamento(), "" + index + "", detalhamento.getDepartamento().getNome());
			totalOrcamento = totalOrcamento + detalhamento.getOrcamentoTotalDepartamento();
			index++;
			dataSet.addValue(detalhamento.getValorConsumido(), "" + index + "", detalhamento.getDepartamento().getNome());
			totalValorConsumido = totalValorConsumido + detalhamento.getValorConsumido();
		}*/

		LegendaGraficoVO legendaTotalOrcamento = new LegendaGraficoVO("Total Planejado", totalOrcamento, "#7777FF");
		LegendaGraficoVO legendaValorConsumido = new LegendaGraficoVO("Total Realizado", totalValorConsumido, "#B85959");
		planoOrcamentarioVO.getLegendaGraficoVOs().add(legendaTotalOrcamento);
		planoOrcamentarioVO.getLegendaGraficoVOs().add(legendaValorConsumido);
		planoOrcamentarioVO.setGraficoPorDepartamento(dataSet);
	}

	public void executarMontagemGraficoDetalhamentoPorMes(DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO) {
		TimeSeries planejado = new TimeSeries("PLANEJADO", Month.class);
		TimeSeries realizado = new TimeSeries("REALIZADO", Month.class);
		Double totalOrcamento = 0.00;
		Double totalValorConsumido = 0.00;
		detalhamentoPlanoOrcamentarioVO.getLegendaPlanejadoRealizadoDespesaVOs().clear();
		for (ItemMensalDetalhamentoPlanoOrcamentarioVO itemMensal : detalhamentoPlanoOrcamentarioVO.getListaItemMensalDetalhamentoPlanoOrcamentarioVOs()) {
			planejado.add(new Month(Uteis.getMesReferencia(itemMensal.getMes().substring(0, itemMensal.getMes().indexOf(" -")).toString()), Uteis.getAnoPlanoOrcamentario(itemMensal.getMes())), itemMensal.getValor());
			realizado.add(new Month(Uteis.getMesReferencia(itemMensal.getMes().substring(0, itemMensal.getMes().indexOf(" -")).toString()), Uteis.getAnoPlanoOrcamentario(itemMensal.getMes())), itemMensal.getValorConsumidoMes());
			totalOrcamento = totalOrcamento + itemMensal.getValor();
			totalValorConsumido = totalValorConsumido + itemMensal.getValorConsumidoMes();
		}
		LegendaGraficoVO legendaTotalOrcamento = new LegendaGraficoVO("Total Planejado", totalOrcamento, "#3399FF");
		LegendaGraficoVO legendaValorConsumido = new LegendaGraficoVO("Total Realizado", totalValorConsumido, "#D20000");
		detalhamentoPlanoOrcamentarioVO.getLegendaPlanejadoRealizadoDespesaVOs().add(legendaTotalOrcamento);
		detalhamentoPlanoOrcamentarioVO.getLegendaPlanejadoRealizadoDespesaVOs().add(legendaValorConsumido);
		detalhamentoPlanoOrcamentarioVO.getGraficoPlanejadoRealizadoLinhaTempo().addSeries(planejado);
		detalhamentoPlanoOrcamentarioVO.getGraficoPlanejadoRealizadoLinhaTempo().addSeries(realizado);
	}

	public void validarDadosUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {

		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				return;
			}
		}
		throw new Exception("Nenhuma UNIDADE DE ENSINO foi selecionada, clique no botão ao lado para adicionar uma Unidade de Ensino");

	}

	public void validarDadosFiltroBusca(Date dataInicio, Date dataTermino) throws ConsistirException {
		if (dataInicio == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PainelGestorFinanceiro_dataInicio"));
		}
		if (dataTermino == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PainelGestorFinanceiro_dataTermino"));
		}
		if (dataInicio.compareTo(dataTermino) > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_PainelGestorFinanceiro_dataInicioMaioSataTermino"));
		}
	}

	public void executarCriacaoGraficoReceitaDespesaSaldo(Map<String, Double> saldos, PainelGestorVO painelGestorVO) {
		painelGestorVO.setListaValoresReceitaDespesasSaldo("");
		TreeMap<String, Double> sorted_map = new TreeMap<String, Double>();
		sorted_map.putAll(saldos);
		for (Entry<String, Double> saldo : sorted_map.entrySet()) {
			saldo.getKey();
			saldo.getValue();
			if (painelGestorVO.getListaValoresReceitaDespesasSaldo().isEmpty()) {
				painelGestorVO.setListaValoresReceitaDespesasSaldo(painelGestorVO.getListaValoresReceitaDespesasSaldo() + "[Date.UTC(" + saldo.getKey() + ",1), " + Uteis.arrendondarForcando2CadasDecimais(saldo.getValue()) + "]");
			} else {
				painelGestorVO.setListaValoresReceitaDespesasSaldo(painelGestorVO.getListaValoresReceitaDespesasSaldo() + ",[Date.UTC(" + saldo.getKey() + ",1), " + Uteis.arrendondarForcando2CadasDecimais(saldo.getValue()) + "]");
			}
		}

	}

	/**
	 * Método responsável por criar o gráfico de Recebimento X Pagamento
	 * 
	 * @author CarlosEugenio 24/01/2014
	 */
	public void executarCriacaoGraficoLinhaRecebimentoPagamento(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Boolean mesmoMesAno) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setLegendaGraficoReceitaDespesaVOs(null);
		painelGestorVO.setListaValoresReceitas("");
		painelGestorVO.setListaValoresDespesas("");
		painelGestorVO.setListaValoresReceitaDespesasSaldo("");
		SqlRowSet rs = null;
		String mesAno = "";
		// if (mesmoMesAno) {
		// rs = consultarRecebimentoPagamentoMesmoMesAno(unidadeEnsinoVOs,
		// dataInicio, dataTermino);
		// } else {
		rs = consultarRecebimentoPagamento(unidadeEnsinoVOs, dataInicio, dataTermino);
		// }
		LegendaGraficoVO receita = new LegendaGraficoVO(ReceitaDespesaEnum.RECEITA.toString(), 0.0, "#3399FF");
		LegendaGraficoVO despesa = new LegendaGraficoVO(ReceitaDespesaEnum.DESPESA.toString(), 0.0, "#D20000");
		LegendaGraficoVO saldoLegenda = new LegendaGraficoVO(ReceitaDespesaEnum.SALDO.toString(), 0.0, "#FDFD00");
		Map<String, Double> saldo = new HashMap<String, Double>(0);
		while (rs.next()) {

			mesAno = rs.getInt("mes") + "/" + rs.getInt("ano");
			mesAno = MesAnoEnum.getEnum(mesAno.substring(0, mesAno.indexOf("/"))).getMesAbreviado() + "/" + mesAno.substring(mesAno.length() - 2, mesAno.length());
			if (rs.getString("tipo").equals(ReceitaDespesaEnum.RECEITA.toString())) {

				if (mesmoMesAno) {
					if (painelGestorVO.getListaValoresReceitas().isEmpty()) {
						painelGestorVO.setListaValoresReceitas(painelGestorVO.getListaValoresReceitas() + "[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + "," + rs.getInt("dia") + "), " + rs.getDouble("valor") + "]");
					} else {
						painelGestorVO.setListaValoresReceitas(painelGestorVO.getListaValoresReceitas() + ",[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + "," + rs.getInt("dia") + "), " + rs.getDouble("valor") + "]");
					}

				} else {
					if (painelGestorVO.getListaValoresReceitas().isEmpty()) {
						painelGestorVO.setListaValoresReceitas(painelGestorVO.getListaValoresReceitas() + "[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + ",1), " + rs.getDouble("valor") + "]");
					} else {
						painelGestorVO.setListaValoresReceitas(painelGestorVO.getListaValoresReceitas() + ",[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + ",1), " + rs.getDouble("valor") + "]");
					}
				}
				if (saldo.containsKey(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1))) {
					saldo.put(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1), saldo.remove(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1)) + rs.getDouble("valor"));
				} else {
					saldo.put(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1), rs.getDouble("valor"));
				}
				receita.setValor(receita.getValor() + rs.getDouble("valor"));
			} else if (rs.getString("tipo").equals(ReceitaDespesaEnum.DESPESA.toString())) {
				if (mesmoMesAno) {
					if (painelGestorVO.getListaValoresDespesas().isEmpty()) {
						painelGestorVO.setListaValoresDespesas(painelGestorVO.getListaValoresDespesas() + "[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + "," + rs.getInt("dia") + "), " + rs.getDouble("valor") + "]");
					} else {
						painelGestorVO.setListaValoresDespesas(painelGestorVO.getListaValoresDespesas() + ",[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + "," + rs.getInt("dia") + "), " + rs.getDouble("valor") + "]");
					}
				} else {
					if (painelGestorVO.getListaValoresDespesas().isEmpty()) {
						painelGestorVO.setListaValoresDespesas(painelGestorVO.getListaValoresDespesas() + "[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + ", 1), " + rs.getDouble("valor") + "]");
					} else {
						painelGestorVO.setListaValoresDespesas(painelGestorVO.getListaValoresDespesas() + ",[Date.UTC(" + rs.getInt("ano") + ", " + (rs.getInt("mes") - 1) + ",1), " + rs.getDouble("valor") + "]");
					}
				}
				if (saldo.containsKey(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1))) {
					saldo.put(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1), saldo.remove(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1)) - rs.getDouble("valor"));
				} else {
					saldo.put(rs.getInt("ano") + ", " + (rs.getInt("mes") - 1), -1 * rs.getDouble("valor"));
				}
				despesa.setValor(despesa.getValor() + rs.getDouble("valor"));
			}
		}

		receita.setValor(Uteis.arrendondarForcando2CadasDecimais(receita.getValor()));
		despesa.setValor(Uteis.arrendondarForcando2CadasDecimais(despesa.getValor()));
		if (receita.getValor() > 0) {
			receita.setLegenda(receita.getNome() + " - R$ " + Uteis.formatarDecimal(Uteis.arrendondarForcando2CadasDecimais(receita.getValor()), "0,000.00#") + "");
		} else {
			painelGestorVO.setListaValoresReceitas("[Date.UTC(0,0,0),0]");
			receita.setLegenda(receita.getNome() + " - R$ 0,00");
		}
		if (despesa.getValor() > 0) {
			despesa.setLegenda(despesa.getNome() + " - R$ " + Uteis.formatarDecimal(Uteis.arrendondarForcando2CadasDecimais(despesa.getValor()), "0,000.00#") + " ");
		} else {
			painelGestorVO.setListaValoresDespesas("[Date.UTC(0,0,0),0]");
			despesa.setLegenda(despesa.getNome() + " - R$ 0,00");
		}
		if (receita.getValor() > 0 || despesa.getValor() > 0) {
			executarCriacaoGraficoReceitaDespesaSaldo(saldo, painelGestorVO);
			saldoLegenda.setValor(Uteis.arrendondarForcando2CadasDecimais(receita.getValor() - despesa.getValor()));
			saldoLegenda.setLegenda(saldoLegenda.getNome() + " - R$ " + Uteis.formatarDecimal(Uteis.arrendondarForcando2CadasDecimais(receita.getValor() - despesa.getValor()), "0,000.00#") + " ");
		} else {
			painelGestorVO.setListaValoresDespesas("[Date.UTC(0,0,0),0]");
			saldoLegenda.setLegenda(saldoLegenda.getNome() + " - R$ 0,00");
		}

		painelGestorVO.getLegendaGraficoReceitaDespesaVOs().add(receita);
		painelGestorVO.getLegendaGraficoReceitaDespesaVOs().add(despesa);
		painelGestorVO.getLegendaGraficoReceitaDespesaVOs().add(saldoLegenda);

	}

	private String getFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String campo) throws Exception {
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		StringBuilder sb = new StringBuilder("");
		sb.append(campo).append(" in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sb.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sb.append(") ");
		return sb.toString();
	}

	public SqlRowSet consultarRecebimentoPagamento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {
		StringBuilder sb = new StringBuilder(" select sum(valor::NUMERIC(20,2)) as valor, tipo, ano, mes from (");
		sb.append(" select distinct contareceber.codigo, " + "valorRecebido as valor, "
		/**
		 * Foi comentado pelo Rodrigo no dia 05/08/2013 pois este limitava para
		 * trazer só as contas recebidas que venciam no mês correspondente
		 */
		// +
		// "case when ( extract(month from negociacaorecebimento.data) = extract(month from dataVencimento) and extract(year from negociacaorecebimento.data) = extract(year from dataVencimento)) then trunc(contareceber.valorRecebido::NUMERIC,2) else 0.0 end as valor, "
				+ "'").append(ReceitaDespesaEnum.RECEITA.toString()).append("'::VARCHAR as tipo, extract(year from max(negociacaorecebimento.data))::INT as ano,");
		sb.append(" extract(month from max(negociacaorecebimento.data))::INT as mes");
		sb.append(" from contareceber ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo");

		sb.append(" where  situacao = 'RE' and valorRecebido > 0  and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));

		sb.append(" group by contareceber.codigo, valorRecebido, negociacaorecebimento.data, contareceber.datavencimento ) as t group by ano, mes, tipo");
		sb.append(" union all");
		sb.append(" select sum(valor::NUMERIC(20,2)) as valor, tipo, ano, mes from (");
		sb.append(" select distinct contapagar.codigo, " + "valorPago as valor, "
		/**
		 * Foi comentado pelo Rodrigo no dia 05/08/2013 pois este limitava para
		 * trazer só as contas pagas que venciam no mês correspondente
		 */
		// +
		// "case when ( extract(month from negociacaopagamento.data) = extract(month from dataVencimento) and extract(year from negociacaopagamento.data) = extract(year from dataVencimento))  then contapagar.valorPago else 0.0 end as valor,  "
				+ "'").append(ReceitaDespesaEnum.DESPESA.toString()).append("'::VARCHAR as tipo, extract(year from max(negociacaopagamento.data))::INT as ano,");
		sb.append(" extract(month from max(negociacaopagamento.data))::INT as mes");
		sb.append(" from contapagar ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo  ");
		sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
		sb.append(" where  situacao = 'PA' and valorPago > 0 and negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" group by contapagar.codigo, valorPago, negociacaopagamento.data, dataVencimento ) as t group by ano, mes, tipo  order by tipo desc, ano, mes");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	public SqlRowSet consultarRecebimentoPagamentoMesmoMesAno(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {
		StringBuilder sb = new StringBuilder(" select sum(valor::NUMERIC(20,2)) as valor, tipo, ano, mes, dia from (");
		sb.append(" select distinct contareceber.codigo, " + "valorRecebido as valor, "
		/**
		 * Foi comentado pelo Rodrigo no dia 05/08/2013 pois este limitava para
		 * trazer só as contas recebidas que venciam no mês correspondente
		 */
		// +
		// " case when ( extract(month from negociacaorecebimento.data) = extract(month from dataVencimento) and extract(year from negociacaorecebimento.data) = extract(year from dataVencimento)) then trunc(contareceber.valorRecebido::NUMERIC,2) else 0.0 end as valor, "
				+ "'").append(ReceitaDespesaEnum.RECEITA.toString()).append("'::VARCHAR as tipo, " + "extract(year from max(negociacaorecebimento.data))::INT as ano,");
		sb.append(" extract(month from max(negociacaorecebimento.data))::INT as mes, extract(day from max(negociacaorecebimento.data))::INT as dia");
		sb.append(" from contareceber ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo");
		sb.append(" where  situacao = 'RE' and valorRecebido > 0 and negociacaorecebimento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaorecebimento.data::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(" group by contareceber.codigo, valorRecebido, negociacaorecebimento.data, contareceber.datavencimento order by ano, mes, dia) as t group by ano, mes, dia, tipo");
		sb.append(" union all");
		sb.append(" select sum(valor::NUMERIC(20,2)) as valor, tipo, ano, mes, dia from (");
		sb.append(" select distinct contapagar.codigo, " + "valorPago as valor, "
		/**
		 * Foi comentado pelo Rodrigo no dia 05/08/2013 pois este limitava para
		 * trazer só as contas pagas que venciam no mês
		 */
		// +
		// "case when ( extract(month from negociacaopagamento.data) = extract(month from dataVencimento) and extract(year from negociacaopagamento.data) = extract(year from dataVencimento))  then contapagar.valorPago else 0.0 end as valor, "
				+ "'").append(ReceitaDespesaEnum.DESPESA.toString()).append("'::VARCHAR as tipo, extract(year from max(negociacaopagamento.data))::INT as ano,");
		sb.append(" extract(month from max(negociacaopagamento.data))::INT as mes, extract(day from max(negociacaopagamento.data))::INT as dia");
		sb.append(" from contapagar ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo  ");
		sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
		sb.append(" where  situacao = 'PA' and valorPago > 0 and negociacaopagamento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaopagamento.data::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" group by contapagar.codigo, valorPago, negociacaopagamento.data, contapagar.datavencimento order by ano, mes, dia) as t group by ano, mes, dia, tipo  order by tipo desc, ano, mes, dia");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	public void consultarMapaReceitaPorFluxoCaixaPainelGestorFinanceiroPorPeriodo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {
		painelGestorVO.getPainelGestorContaReceberFluxoCaixaMesAnoVOs().clear();
		StringBuilder sb = new StringBuilder();
		sb.append(" select receitaNoMes, descontoNoMes, acrescimo, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, ");
		sb.append(" receitaNoMesMatricula, receitaNoMesMaterialDidatico, receitaNoMesMensalidade, receitaNoMesRequerimento, receitaNoMesBiblioteca, receitaNoMesDevolucaoCheque, ");
		sb.append(" receitaNoMesNegociacao, receitaNoMesBolsaCusteada, receitaNoMesInscricao,  receitaNoMesContratoReceita,	receitaNoMesInclusaoReposicao, receitaNoMesOutros, ");
		sb.append(" descontoNoMesMatricula, descontoNoMesMaterialDidatico, descontoNoMesMensalidade, descontoNoMesRequerimento, descontoNoMesBiblioteca, descontoNoMesDevolucaoCheque, ");
		sb.append(" descontoNoMesNegociacao, descontoNoMesBolsaCusteada, descontoNoMesInscricao, descontoNoMesContratoReceita, descontoNoMesInclusaoReposicao, descontoNoMesOutros, ");
		sb.append(" acrescimoNoMesMatricula, acrescimoNoMesMaterialDidatico, acrescimoNoMesMensalidade, acrescimoNoMesRequerimento, acrescimoNoMesBiblioteca, acrescimoNoMesDevolucaoCheque, ");
		sb.append(" acrescimoNoMesNegociacao, acrescimoNoMesBolsaCusteada, acrescimoNoMesInscricao, acrescimoNoMesContratoReceita, acrescimoNoMesInclusaoReposicao, acrescimoNoMesOutros, valorInadimplencia, ");
		sb.append(" case when valorInadimplencia > 0 and receitaNoMes > 0 then ((valorInadimplencia * 100) / receitaNoMes)::NUMERIC(20,2) else 0.0 end as taxaInadimplenciaNoMes,  ");
		sb.append(" case when valorInadimplencia > 0 and receitaNoMes > 0 then (((valorInadimplencia - acrescimo) * 100) / receitaNoMes)::NUMERIC(20,2) else 0.0 end as taxaInadimplenciaNoMesSemAcrescimo,  ");
		sb.append(" ano, mes from (");
		sb.append(" select sum(receitaNoMes::NUMERIC(20,2)) as receitaNoMes, sum(descontoNoMes::NUMERIC(20,2)) as descontoNoMes, sum(acrescimo::NUMERIC(20,2)) as acrescimo, ");
		sb.append(" sum(descontoconvenio) as descontoconvenio, sum(descontoinstituicao) as descontoinstituicao, sum(valorDescontoprogressivo) as valorDescontoprogressivo, ");
		sb.append(" sum(descontoaluno) as descontoaluno, sum(descontorecebimento) as descontorecebimento, sum(descontorateio) as descontorateio, ");
		// Receita No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMensalidade,");
		sb.append(" sum(case when tipoOrigem = 'REQ' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesOutros, ");
		// Desconto No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMensalidade, ");
		sb.append(" sum(case when tipoOrigem = 'REQ' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesOutros,  ");
		// Acrescimo No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMensalidade, ");
		sb.append(" sum(case when tipoOrigem = 'REQ' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesOutros, ");
		sb.append(" sum(valorInadimplencia) as valorInadimplencia, ano, mes from (");
		sb.append(" select codigo, nossonumero, situacao, datavencimento, tipoorigem, valorRecebido::NUMERIC(20,2) as receitaNoMes, ");
		sb.append(" (sum(descontoconvenio) + sum(descontoinstituicao) + sum(valorDescontoprogressivo) + sum(descontoaluno) + sum(descontorecebimento) + sum(descontorateio))::numeric(20,2) as descontoNoMes,");
		sb.append(" sum(acrescimo + juro + multa)::numeric(20,2) as acrescimo, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, valorInadimplencia, ano, mes ");
		sb.append(" from (");

		sb.append(" select distinct contareceber.tipoOrigem, contareceber.codigo, nossonumero, datavencimento, contareceber.situacao, ");
		sb.append(" case when (contareceber.acrescimo > 0) then contareceber.acrescimo::numeric(20,2) else 0.0 end as acrescimo, ");
		sb.append(" case when (contareceber.juro > 0) then contareceber.juro::numeric(20,2) else 0.0 end as juro, ");
		sb.append(" case when (contareceber.multa > 0) then contareceber.multa::numeric(20,2) else 0.0 end as multa, ");
		sb.append(" contareceber.valorRecebido::NUMERIC(20,2) as valorRecebido, ");
		sb.append(" case when (descontoconvenio > 0) then descontoconvenio::numeric(20,2) else 0.0 end as descontoconvenio, ");
		sb.append(" case when (descontoinstituicao > 0) then descontoinstituicao::numeric(20,2) else 0.0 end as descontoinstituicao, ");
		sb.append(" case when (valordescontoprogressivo > 0) then valordescontoprogressivo::numeric(20,2) else 0.0 end as valordescontoprogressivo, ");
		sb.append(" case when valordescontoalunojacalculado > 0 then valordescontoalunojacalculado::numeric(20,2) else 0.0 end as descontoaluno, ");
		sb.append(" case when (valorcalculadodescontolancadorecebimento > 0) then valorcalculadodescontolancadorecebimento::numeric(20,2) else 0.0 end as descontorecebimento, ");
		sb.append(" valordescontorateio as descontorateio, ");
		sb.append(" 0.0 AS valorInadimplencia, extract(year from negociacaorecebimento.data)::INT as ano, extract(month from negociacaorecebimento.data)::INT as mes ");
		sb.append(" from contaReceber  ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" where contaReceber.situacao in ('RE') ");
		sb.append(" and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' ");
		sb.append(" and negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira "));

		// Consulta o valor de Inadimplência
		sb.append(" union all ");

		sb.append(" select cast('' as varchar ) as tipoorigem, 0 AS codigo, cast('' as varchar ) as nossoNumero, null, cast('' as varchar) as situacao, ");
		sb.append(" 0.0 as acrescimo, 0.0 as juro, 0.0 as multa, 0.0 as valorrecebido, 0.0 as descontoconvenio, 0.0 as descontoinstituicao, 0.0 as valordescontoprogressivo, ");
		sb.append(" 0.0 as descontoaluno, 0.0 as descontoRecebimento, 0.0 as descontorateio, ");
		sb.append(" sum(valorInadimplencia)::numeric(20,2) AS valorInadimplencia, ano, mes from (");
		sb.append(" select case when (situacao = 'RE' and datavencimento < negociacaorecebimento.data ");
		sb.append(" and extract(month from negociacaorecebimento.data)::INT != extract(month from datavencimento)::INT) then contaReceber.valorRecebido else ");
		sb.append(" case when (situacao = 'AR' and contareceber.datavencimento < current_date ) then valorReceberCalculado else 0.0 end end AS valorInadimplencia, ");
		sb.append(" extract(year from datavencimento)::INT as ano, extract(month from datavencimento)::INT as mes ");
		sb.append(" from contaReceber ");
		sb.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sb.append(" left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" where 1=1 ");
		sb.append(" and dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' ");
		sb.append(" and dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira "));
		sb.append(" order by datavencimento ");
		sb.append(" ) as t ");
		sb.append(" group by ano, mes ");

		sb.append(") as t ");
		sb.append(" group by ano, mes, valorRecebido, situacao, tipoorigem, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, ");
		sb.append(" descontoaluno, descontorecebimento, descontorateio, nossonumero, datavencimento, codigo, valorInadimplencia ");
		sb.append(") as t1 ");
		sb.append(" group by ano, mes ");
		sb.append(" order by ano, mes ");
		sb.append(" ) as t2 ");

		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		painelGestorVO.setTotalReceitaNoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalAcrescimoNoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalDescontoNoMes(BigDecimal.ZERO);

		painelGestorVO.setReceitaNoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesOutros(BigDecimal.ZERO);
		

		painelGestorVO.setAcrescimoNoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoNoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setDescontoNoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesOutros(BigDecimal.ZERO);
		painelGestorVO.setMediaInadimplenciaNoPeriodo(0.0);
		painelGestorVO.setMediaInadimplenciaNoPeriodoSemAcrescimo(0.0);
		
		while (dadosSQL.next()) {
			montarDadosMapaReceitaPorFluxoCaixaPainelGestorFinanceiroContaReceberMesAno(painelGestorVO, false, dadosSQL);
		}
		executarCalculoMediaInadimplenciaMapaReceitaPorFluxoCaixaPeridoPainelGestorFinanceiro(painelGestorVO);
	}

	public void montarDadosMapaReceitaPorFluxoCaixaPainelGestorFinanceiroContaReceberMesAno(PainelGestorVO painelGestorVO, Boolean filtarPorNivelEducacional, SqlRowSet dadosSQL) {
		PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = new PainelGestorContaReceberMesAnoVO();
		painelGestorContaReceberMesAnoVO.setReceitaNoMes(dadosSQL.getBigDecimal("receitaNoMes"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMes(dadosSQL.getBigDecimal("descontoNoMes"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMes(dadosSQL.getBigDecimal("acrescimo"));

		painelGestorContaReceberMesAnoVO.setDescontoConvenio(dadosSQL.getBigDecimal("descontoConvenio"));
		painelGestorContaReceberMesAnoVO.setDescontoInstituicao(dadosSQL.getBigDecimal("descontoInstituicao"));
		painelGestorContaReceberMesAnoVO.setValorDescontoProgressivo(dadosSQL.getBigDecimal("valorDescontoProgressivo"));
		painelGestorContaReceberMesAnoVO.setDescontoAluno(dadosSQL.getBigDecimal("descontoAluno"));
		painelGestorContaReceberMesAnoVO.setDescontoRecebimento(dadosSQL.getBigDecimal("descontoRecebimento"));
		painelGestorContaReceberMesAnoVO.setDescontoRateio(dadosSQL.getBigDecimal("descontoRateio"));

		painelGestorContaReceberMesAnoVO.setValorInadimplenciaNoMes(dadosSQL.getBigDecimal("taxaInadimplenciaNoMes"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesMatricula(dadosSQL.getBigDecimal("receitaNoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesMaterialDidatico(dadosSQL.getBigDecimal("receitaNoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesMensalidade(dadosSQL.getBigDecimal("receitaNoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesRequerimento(dadosSQL.getBigDecimal("receitaNoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesBiblioteca(dadosSQL.getBigDecimal("receitaNoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesDevolucaoCheque(dadosSQL.getBigDecimal("receitaNoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesNegociacao(dadosSQL.getBigDecimal("receitaNoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesBolsaCusteada(dadosSQL.getBigDecimal("receitaNoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesInscricao(dadosSQL.getBigDecimal("receitaNoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesContratoReceita(dadosSQL.getBigDecimal("receitaNoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesInclusaoReposicao(dadosSQL.getBigDecimal("receitaNoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesOutros(dadosSQL.getBigDecimal("receitaNoMesOutros"));

		painelGestorContaReceberMesAnoVO.setDescontoNoMesMatricula(dadosSQL.getBigDecimal("descontoNoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesMaterialDidatico(dadosSQL.getBigDecimal("descontoNoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesMensalidade(dadosSQL.getBigDecimal("descontoNoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesRequerimento(dadosSQL.getBigDecimal("descontoNoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesBiblioteca(dadosSQL.getBigDecimal("descontoNoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesDevolucaoCheque(dadosSQL.getBigDecimal("descontoNoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesNegociacao(dadosSQL.getBigDecimal("descontoNoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesBolsaCusteada(dadosSQL.getBigDecimal("descontoNoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesInscricao(dadosSQL.getBigDecimal("descontoNoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesContratoReceita(dadosSQL.getBigDecimal("descontoNoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesInclusaoReposicao(dadosSQL.getBigDecimal("descontoNoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesOutros(dadosSQL.getBigDecimal("descontoNoMesOutros"));

		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesMatricula(dadosSQL.getBigDecimal("acrescimoNoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesMaterialDidatico(dadosSQL.getBigDecimal("acrescimoNoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesMensalidade(dadosSQL.getBigDecimal("acrescimoNoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesRequerimento(dadosSQL.getBigDecimal("acrescimoNoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesBiblioteca(dadosSQL.getBigDecimal("acrescimoNoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesDevolucaoCheque(dadosSQL.getBigDecimal("acrescimoNoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesNegociacao(dadosSQL.getBigDecimal("acrescimoNoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesBolsaCusteada(dadosSQL.getBigDecimal("acrescimoNoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesInscricao(dadosSQL.getBigDecimal("acrescimoNoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesContratoReceita(dadosSQL.getBigDecimal("acrescimoNoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesInclusaoReposicao(dadosSQL.getBigDecimal("acrescimoNoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesOutros(dadosSQL.getBigDecimal("acrescimoNoMesOutros"));
                
		if(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMes().doubleValue() > 0 && painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMes().doubleValue() > 0) {
			painelGestorContaReceberMesAnoVO.setTaxaInadimplenciaNoMes(new BigDecimal((painelGestorContaReceberMesAnoVO.getTotalVencidoDoMes().doubleValue() *100) /painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMes().doubleValue()));	
		}
		painelGestorContaReceberMesAnoVO.setTaxaInadimplenciaNoMesSemAcrescimo(dadosSQL.getBigDecimal("taxaInadimplenciaNoMesSemAcrescimo"));

		if (filtarPorNivelEducacional) {
			painelGestorContaReceberMesAnoVO.setCurso(dadosSQL.getString("curso_nome"));
			painelGestorContaReceberMesAnoVO.setCodigoCurso(dadosSQL.getInt("codigoCurso"));
			painelGestorContaReceberMesAnoVO.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		} else {
			painelGestorContaReceberMesAnoVO.setMesAno(dadosSQL.getInt("mes") + "/" + dadosSQL.getInt("ano"));
		}
		painelGestorVO.getPainelGestorContaReceberFluxoCaixaMesAnoVOs().add(painelGestorContaReceberMesAnoVO);
	}

	public void montarDadosMapaReceitaPorFluxoCaixaPainelGestorFinanceiroContaReceberPorTurma(PainelGestorVO painelGestorVO, Boolean filtarPorNivelEducacional, SqlRowSet dadosSQL) {
		PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = new PainelGestorContaReceberMesAnoVO();
		painelGestorContaReceberMesAnoVO.setReceitaNoMes(dadosSQL.getBigDecimal("receitaNoMes"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMes(dadosSQL.getBigDecimal("descontoNoMes"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMes(dadosSQL.getBigDecimal("acrescimo"));
		
		painelGestorContaReceberMesAnoVO.setDescontoConvenio(dadosSQL.getBigDecimal("descontoConvenio"));
		painelGestorContaReceberMesAnoVO.setDescontoInstituicao(dadosSQL.getBigDecimal("descontoInstituicao"));
		painelGestorContaReceberMesAnoVO.setValorDescontoProgressivo(dadosSQL.getBigDecimal("valorDescontoProgressivo"));
		painelGestorContaReceberMesAnoVO.setDescontoAluno(dadosSQL.getBigDecimal("descontoAluno"));
		painelGestorContaReceberMesAnoVO.setDescontoRecebimento(dadosSQL.getBigDecimal("descontoRecebimento"));
		painelGestorContaReceberMesAnoVO.setDescontoRateio(dadosSQL.getBigDecimal("descontoRateio"));
		
		painelGestorContaReceberMesAnoVO.setReceitaNoMesMatricula(dadosSQL.getBigDecimal("receitaNoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesMaterialDidatico(dadosSQL.getBigDecimal("receitaNoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesMensalidade(dadosSQL.getBigDecimal("receitaNoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesRequerimento(dadosSQL.getBigDecimal("receitaNoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesBiblioteca(dadosSQL.getBigDecimal("receitaNoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesDevolucaoCheque(dadosSQL.getBigDecimal("receitaNoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesNegociacao(dadosSQL.getBigDecimal("receitaNoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesBolsaCusteada(dadosSQL.getBigDecimal("receitaNoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesInscricao(dadosSQL.getBigDecimal("receitaNoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesContratoReceita(dadosSQL.getBigDecimal("receitaNoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesInclusaoReposicao(dadosSQL.getBigDecimal("receitaNoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setReceitaNoMesOutros(dadosSQL.getBigDecimal("receitaNoMesOutros"));
		
		painelGestorContaReceberMesAnoVO.setDescontoNoMesMatricula(dadosSQL.getBigDecimal("descontoNoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesMaterialDidatico(dadosSQL.getBigDecimal("descontoNoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesMensalidade(dadosSQL.getBigDecimal("descontoNoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesRequerimento(dadosSQL.getBigDecimal("descontoNoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesBiblioteca(dadosSQL.getBigDecimal("descontoNoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesDevolucaoCheque(dadosSQL.getBigDecimal("descontoNoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesNegociacao(dadosSQL.getBigDecimal("descontoNoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesBolsaCusteada(dadosSQL.getBigDecimal("descontoNoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesInscricao(dadosSQL.getBigDecimal("descontoNoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesContratoReceita(dadosSQL.getBigDecimal("descontoNoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesInclusaoReposicao(dadosSQL.getBigDecimal("descontoNoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setDescontoNoMesOutros(dadosSQL.getBigDecimal("descontoNoMesOutros"));
		
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesMatricula(dadosSQL.getBigDecimal("acrescimoNoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesMaterialDidatico(dadosSQL.getBigDecimal("acrescimoNoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesMensalidade(dadosSQL.getBigDecimal("acrescimoNoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesRequerimento(dadosSQL.getBigDecimal("acrescimoNoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesBiblioteca(dadosSQL.getBigDecimal("acrescimoNoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesDevolucaoCheque(dadosSQL.getBigDecimal("acrescimoNoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesNegociacao(dadosSQL.getBigDecimal("acrescimoNoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesBolsaCusteada(dadosSQL.getBigDecimal("acrescimoNoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesInscricao(dadosSQL.getBigDecimal("acrescimoNoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesContratoReceita(dadosSQL.getBigDecimal("acrescimoNoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesInclusaoReposicao(dadosSQL.getBigDecimal("acrescimoNoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoNoMesOutros(dadosSQL.getBigDecimal("acrescimoNoMesOutros"));
		if(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMes().doubleValue() > 0 && painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMes().doubleValue() > 0) {
			painelGestorContaReceberMesAnoVO.setTaxaInadimplenciaNoMes(new BigDecimal((painelGestorContaReceberMesAnoVO.getTotalVencidoDoMes().doubleValue() *100) /painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMes().doubleValue()));	
		}
		
		
		if (filtarPorNivelEducacional) {
			painelGestorContaReceberMesAnoVO.setTurma(dadosSQL.getString("turma"));
			painelGestorContaReceberMesAnoVO.setCodigoTurma(dadosSQL.getInt("codigoTurma"));
			painelGestorContaReceberMesAnoVO.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		} else {
			painelGestorContaReceberMesAnoVO.setMesAno(dadosSQL.getInt("mes") + "/" + dadosSQL.getInt("ano"));
		}
		painelGestorVO.getPainelGestorContaReceberMesAnoTurmaVOs().add(painelGestorContaReceberMesAnoVO);
	}
	
	public void executarCalculoMediaInadimplenciaMapaReceitaPorFluxoCaixaPeridoPainelGestorFinanceiro(PainelGestorVO painelGestorVO) {
		Double totalInadimplenciaNoMes = 0.0;
		Double totalInadimplenciaNoMesSemAcrescimo = 0.0;
		for (PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO : painelGestorVO.getPainelGestorContaReceberFluxoCaixaMesAnoVOs()) {
			painelGestorContaReceberMesAnoVO.getContaReceberPainelGestorVOs().clear();
			
			totalInadimplenciaNoMes = totalInadimplenciaNoMes + painelGestorContaReceberMesAnoVO.getValorInadimplenciaNoMes().doubleValue(); 
			
			painelGestorVO.setTotalReceitaNoMes(painelGestorVO.getTotalReceitaNoMes().add(painelGestorContaReceberMesAnoVO.getReceitaNoMes()));
			painelGestorVO.setTotalAcrescimoNoMes(painelGestorVO.getTotalAcrescimoNoMes().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMes()));
			painelGestorVO.setTotalDescontoNoMes(painelGestorVO.getTotalDescontoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoNoMes()));

			painelGestorVO.setTotalDescontoConvenioNoMes(painelGestorVO.getTotalDescontoConvenioNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoConvenio()));
			painelGestorVO.setTotalDescontoInstituicaoNoMes(painelGestorVO.getTotalDescontoInstituicaoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoInstituicao()));
			painelGestorVO.setTotalDescontoProgressivoNoMes(painelGestorVO.getTotalDescontoProgressivoNoMes().add(painelGestorContaReceberMesAnoVO.getValorDescontoProgressivo()));
			painelGestorVO.setTotalDescontoRateioNoMes(painelGestorVO.getTotalDescontoRateioNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoRateio()));
			painelGestorVO.setTotalDescontoAlunoNoMes(painelGestorVO.getTotalDescontoAlunoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoAluno()));
			painelGestorVO.setTotalDescontoRecebimentoNoMes(painelGestorVO.getTotalDescontoRecebimentoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoRecebimento()));

			painelGestorVO.setReceitaNoMesMatricula(painelGestorVO.getReceitaNoMesMatricula().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesMatricula()));
			painelGestorVO.setReceitaNoMesMaterialDidatico(painelGestorVO.getReceitaNoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesMaterialDidatico()));
			painelGestorVO.setReceitaNoMesMensalidade(painelGestorVO.getReceitaNoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesMensalidade()));
			painelGestorVO.setReceitaNoMesRequerimento(painelGestorVO.getReceitaNoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesRequerimento()));
			painelGestorVO.setReceitaNoMesBiblioteca(painelGestorVO.getReceitaNoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesBiblioteca()));
			painelGestorVO.setReceitaNoMesDevolucaoCheque(painelGestorVO.getReceitaNoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesDevolucaoCheque()));
			painelGestorVO.setReceitaNoMesNegociacao(painelGestorVO.getReceitaNoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesNegociacao()));
			painelGestorVO.setReceitaNoMesBolsaCusteada(painelGestorVO.getReceitaNoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesBolsaCusteada()));
			painelGestorVO.setReceitaNoMesInscricao(painelGestorVO.getReceitaNoMesInscricao().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesInscricao()));
			painelGestorVO.setReceitaNoMesContratoReceita(painelGestorVO.getReceitaNoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesContratoReceita()));
			painelGestorVO.setReceitaNoMesInclusaoReposicao(painelGestorVO.getReceitaNoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesInclusaoReposicao()));
			painelGestorVO.setReceitaNoMesOutros(painelGestorVO.getReceitaNoMesOutros().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesOutros()));

			painelGestorVO.setDescontoNoMesMatricula(painelGestorVO.getDescontoNoMesMatricula().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesMatricula()));
			painelGestorVO.setDescontoNoMesMaterialDidatico(painelGestorVO.getDescontoNoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesMaterialDidatico()));
			painelGestorVO.setDescontoNoMesMensalidade(painelGestorVO.getDescontoNoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesMensalidade()));
			painelGestorVO.setDescontoNoMesRequerimento(painelGestorVO.getDescontoNoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesRequerimento()));
			painelGestorVO.setDescontoNoMesBiblioteca(painelGestorVO.getDescontoNoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesBiblioteca()));
			painelGestorVO.setDescontoNoMesDevolucaoCheque(painelGestorVO.getDescontoNoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesDevolucaoCheque()));
			painelGestorVO.setDescontoNoMesNegociacao(painelGestorVO.getDescontoNoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesNegociacao()));
			painelGestorVO.setDescontoNoMesBolsaCusteada(painelGestorVO.getDescontoNoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesBolsaCusteada()));
			painelGestorVO.setDescontoNoMesInscricao(painelGestorVO.getDescontoNoMesInscricao().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesInscricao()));
			painelGestorVO.setDescontoNoMesContratoReceita(painelGestorVO.getDescontoNoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesContratoReceita()));
			painelGestorVO.setDescontoNoMesInclusaoReposicao(painelGestorVO.getDescontoNoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesInclusaoReposicao()));
			painelGestorVO.setDescontoNoMesOutros(painelGestorVO.getDescontoNoMesOutros().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesOutros()));

			painelGestorVO.setAcrescimoNoMesMatricula(painelGestorVO.getAcrescimoNoMesMatricula().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesMatricula()));
			painelGestorVO.setAcrescimoNoMesMaterialDidatico(painelGestorVO.getAcrescimoNoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesMaterialDidatico()));
			painelGestorVO.setAcrescimoNoMesMensalidade(painelGestorVO.getAcrescimoNoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesMensalidade()));
			painelGestorVO.setAcrescimoNoMesRequerimento(painelGestorVO.getAcrescimoNoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesRequerimento()));
			painelGestorVO.setAcrescimoNoMesBiblioteca(painelGestorVO.getAcrescimoNoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesBiblioteca()));
			painelGestorVO.setAcrescimoNoMesDevolucaoCheque(painelGestorVO.getAcrescimoNoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesDevolucaoCheque()));
			painelGestorVO.setAcrescimoNoMesNegociacao(painelGestorVO.getAcrescimoNoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesNegociacao()));
			painelGestorVO.setAcrescimoNoMesBolsaCusteada(painelGestorVO.getAcrescimoNoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesBolsaCusteada()));
			painelGestorVO.setAcrescimoNoMesInscricao(painelGestorVO.getAcrescimoNoMesInscricao().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesInscricao()));
			painelGestorVO.setAcrescimoNoMesContratoReceita(painelGestorVO.getAcrescimoNoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesContratoReceita()));
			painelGestorVO.setAcrescimoNoMesInclusaoReposicao(painelGestorVO.getAcrescimoNoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesInclusaoReposicao()));
			painelGestorVO.setAcrescimoNoMesOutros(painelGestorVO.getAcrescimoNoMesOutros().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesOutros()));

		}
		
		if (totalInadimplenciaNoMes.doubleValue() > 0) {
			painelGestorVO.setMediaInadimplenciaNoPeriodo((totalInadimplenciaNoMes.doubleValue() * 100) / painelGestorVO.getTotalReceitaNoMes().doubleValue());
		} else {
			painelGestorVO.setMediaInadimplenciaNoPeriodo(0.0);
		}
		if (totalInadimplenciaNoMes.doubleValue() > 0) {
			painelGestorVO.setMediaInadimplenciaNoPeriodoSemAcrescimo(((totalInadimplenciaNoMes.doubleValue() - painelGestorVO.getTotalAcrescimoNoMes().doubleValue()) * 100) / painelGestorVO.getTotalReceitaNoMes().doubleValue());
		} else {
			painelGestorVO.setMediaInadimplenciaNoPeriodoSemAcrescimo(0.0);
		}
		
	}

	public void executarCalculoMediaInadimplenciaMapaReceitaPorFluxoCaixaPeridoPainelGestorFinanceiroPorTurma(PainelGestorVO painelGestorVO) {
		Double taxaNoMes = 0.0;
		for (PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO : painelGestorVO.getPainelGestorContaReceberMesAnoTurmaVOs()) {
			painelGestorContaReceberMesAnoVO.getContaReceberPainelGestorVOs().clear();
			painelGestorVO.setTotalReceitaNoMes(painelGestorVO.getTotalReceitaNoMes().add(painelGestorContaReceberMesAnoVO.getReceitaNoMes()));
			painelGestorVO.setTotalAcrescimoNoMes(painelGestorVO.getTotalAcrescimoNoMes().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMes()));
			painelGestorVO.setTotalDescontoNoMes(painelGestorVO.getTotalDescontoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoNoMes()));
			
			painelGestorVO.setTotalDescontoConvenioNoMes(painelGestorVO.getTotalDescontoConvenioNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoConvenio()));
			painelGestorVO.setTotalDescontoInstituicaoNoMes(painelGestorVO.getTotalDescontoInstituicaoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoInstituicao()));
			painelGestorVO.setTotalDescontoProgressivoNoMes(painelGestorVO.getTotalDescontoProgressivoNoMes().add(painelGestorContaReceberMesAnoVO.getValorDescontoProgressivo()));
			painelGestorVO.setTotalDescontoRateioNoMes(painelGestorVO.getTotalDescontoRateioNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoRateio()));
			painelGestorVO.setTotalDescontoAlunoNoMes(painelGestorVO.getTotalDescontoAlunoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoAluno()));
			painelGestorVO.setTotalDescontoRecebimentoNoMes(painelGestorVO.getTotalDescontoRecebimentoNoMes().add(painelGestorContaReceberMesAnoVO.getDescontoRecebimento()));
			
			painelGestorVO.setReceitaNoMesMatricula(painelGestorVO.getReceitaNoMesMatricula().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesMatricula()));			
			painelGestorVO.setReceitaNoMesMaterialDidatico(painelGestorVO.getReceitaNoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesMaterialDidatico()));			
			painelGestorVO.setReceitaNoMesMensalidade(painelGestorVO.getReceitaNoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesMensalidade()));
			painelGestorVO.setReceitaNoMesRequerimento(painelGestorVO.getReceitaNoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesRequerimento()));
			painelGestorVO.setReceitaNoMesBiblioteca(painelGestorVO.getReceitaNoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesBiblioteca()));
			painelGestorVO.setReceitaNoMesDevolucaoCheque(painelGestorVO.getReceitaNoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesDevolucaoCheque()));
			painelGestorVO.setReceitaNoMesNegociacao(painelGestorVO.getReceitaNoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesNegociacao()));
			painelGestorVO.setReceitaNoMesBolsaCusteada(painelGestorVO.getReceitaNoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesBolsaCusteada()));
			painelGestorVO.setReceitaNoMesInscricao(painelGestorVO.getReceitaNoMesInscricao().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesInscricao()));
			painelGestorVO.setReceitaNoMesContratoReceita(painelGestorVO.getReceitaNoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesContratoReceita()));
			painelGestorVO.setReceitaNoMesInclusaoReposicao(painelGestorVO.getReceitaNoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesInclusaoReposicao()));
			painelGestorVO.setReceitaNoMesOutros(painelGestorVO.getReceitaNoMesOutros().add(painelGestorContaReceberMesAnoVO.getReceitaNoMesOutros()));
			
			painelGestorVO.setDescontoNoMesMatricula(painelGestorVO.getDescontoNoMesMatricula().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesMatricula()));			
			painelGestorVO.setDescontoNoMesMaterialDidatico(painelGestorVO.getDescontoNoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesMaterialDidatico()));			
			painelGestorVO.setDescontoNoMesMensalidade(painelGestorVO.getDescontoNoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesMensalidade()));
			painelGestorVO.setDescontoNoMesRequerimento(painelGestorVO.getDescontoNoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesRequerimento()));
			painelGestorVO.setDescontoNoMesBiblioteca(painelGestorVO.getDescontoNoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesBiblioteca()));
			painelGestorVO.setDescontoNoMesDevolucaoCheque(painelGestorVO.getDescontoNoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesDevolucaoCheque()));
			painelGestorVO.setDescontoNoMesNegociacao(painelGestorVO.getDescontoNoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesNegociacao()));
			painelGestorVO.setDescontoNoMesBolsaCusteada(painelGestorVO.getDescontoNoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesBolsaCusteada()));
			painelGestorVO.setDescontoNoMesInscricao(painelGestorVO.getDescontoNoMesInscricao().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesInscricao()));
			painelGestorVO.setDescontoNoMesContratoReceita(painelGestorVO.getDescontoNoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesContratoReceita()));
			painelGestorVO.setDescontoNoMesInclusaoReposicao(painelGestorVO.getDescontoNoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesInclusaoReposicao()));
			painelGestorVO.setDescontoNoMesOutros(painelGestorVO.getDescontoNoMesOutros().add(painelGestorContaReceberMesAnoVO.getDescontoNoMesOutros()));
			
			painelGestorVO.setAcrescimoNoMesMatricula(painelGestorVO.getAcrescimoNoMesMatricula().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesMatricula()));
			painelGestorVO.setAcrescimoNoMesMaterialDidatico(painelGestorVO.getAcrescimoNoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesMaterialDidatico()));
			painelGestorVO.setAcrescimoNoMesMensalidade(painelGestorVO.getAcrescimoNoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesMensalidade()));
			painelGestorVO.setAcrescimoNoMesRequerimento(painelGestorVO.getAcrescimoNoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesRequerimento()));
			painelGestorVO.setAcrescimoNoMesBiblioteca(painelGestorVO.getAcrescimoNoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesBiblioteca()));
			painelGestorVO.setAcrescimoNoMesDevolucaoCheque(painelGestorVO.getAcrescimoNoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesDevolucaoCheque()));
			painelGestorVO.setAcrescimoNoMesNegociacao(painelGestorVO.getAcrescimoNoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesNegociacao()));
			painelGestorVO.setAcrescimoNoMesBolsaCusteada(painelGestorVO.getAcrescimoNoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesBolsaCusteada()));
			painelGestorVO.setAcrescimoNoMesInscricao(painelGestorVO.getAcrescimoNoMesInscricao().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesInscricao()));
			painelGestorVO.setAcrescimoNoMesContratoReceita(painelGestorVO.getAcrescimoNoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesContratoReceita()));
			painelGestorVO.setAcrescimoNoMesInclusaoReposicao(painelGestorVO.getAcrescimoNoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesInclusaoReposicao()));
			painelGestorVO.setAcrescimoNoMesOutros(painelGestorVO.getAcrescimoNoMesOutros().add(painelGestorContaReceberMesAnoVO.getAcrescimoNoMesOutros()));
			
		}
		if (painelGestorVO.getPainelGestorContaReceberMesAnoTurmaVOs().size() > 0) {
			painelGestorVO.setMediaInadimplenciaNoPeriodo(taxaNoMes / painelGestorVO.getPainelGestorContaReceberMesAnoVOs().size());
		} else {
			painelGestorVO.setMediaInadimplenciaNoPeriodo(0.0);
		}
		taxaNoMes = null;
	}
	
	/**
	 * Método responsável por criar o Mapa de Receita por Competência Mês/Ano
	 * 
	 * @param painelGestorVO
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @throws Exception
	 * @author CarlosEugenio
	 */
	@Override
	public void consultarMapaReceitaPorCompetenciaPainelGestorFinanceiroPorPeriodo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String filtrarPeriodoPor) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		painelGestorVO.getPainelGestorContaReceberMesAnoVOs().clear();
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from ( ");
		sb.append("  select receitaDoMes, ");
		sb.append("  case when (acrescimo > 0 and receitaDoMes = receitaComDescontoAcrescimoDoMes) then (acrescimo::NUMERIC(20,2) + descontoDoMes::NUMERIC(20,2)) else descontoDoMes::NUMERIC(20,2) end as descontoDoMes, ");
		sb.append("  case when (descontoDoMes > 0 and receitaDoMes = receitaComDescontoAcrescimoDoMes) then (descontoDoMes + acrescimo) else acrescimo end as acrescimo, juroMulta, ");
		sb.append("  receitaComDescontoAcrescimoDoMes, valorRecebidoDoMes, saldoReceberDoMes, ");

		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, ");

		sb.append("  receitaDoMesMatricula, receitaDoMesMaterialDidatico, receitaDoMesMensalidade, receitaDoMesRequerimento, receitaDoMesBiblioteca, receitaDoMesDevolucaoCheque, ");
		sb.append("  receitaDoMesNegociacao, receitaDoMesBolsaCusteada, receitaDoMesInscricao,  receitaDoMesContratoReceita, receitaDoMesInclusaoReposicao, receitaDoMesOutros, ");

		sb.append("  descontoDoMesMatricula, descontoDoMesMaterialDidatico, descontoDoMesMensalidade, descontoDoMesRequerimento, descontoDoMesBiblioteca, descontoDoMesDevolucaoCheque, ");
		sb.append("  descontoDoMesNegociacao, descontoDoMesBolsaCusteada, descontoDoMesInscricao, descontoDoMesContratoReceita, descontoDoMesInclusaoReposicao, descontoDoMesOutros, ");

		sb.append("  acrescimoDoMesMatricula, acrescimoDoMesMaterialDidatico, acrescimoDoMesMensalidade, acrescimoDoMesRequerimento, acrescimoDoMesBiblioteca, acrescimoDoMesDevolucaoCheque, ");
		sb.append("  acrescimoDoMesNegociacao, acrescimoDoMesBolsaCusteada, acrescimoDoMesInscricao, acrescimoDoMesContratoReceita, acrescimoDoMesInclusaoReposicao, acrescimoDoMesOutros, ");

		sb.append("  receitaComDescontoAcrescimoDoMesMatricula, receitaComDescontoAcrescimoDoMesMaterialDidatico, receitaComDescontoAcrescimoDoMesMensalidade, receitaComDescontoAcrescimoDoMesRequerimento, receitaComDescontoAcrescimoDoMesBiblioteca, ");
		sb.append("  receitaComDescontoAcrescimoDoMesDevolucaoCheque, receitaComDescontoAcrescimoDoMesNegociacao, receitaComDescontoAcrescimoDoMesBolsaCusteada, receitaComDescontoAcrescimoDoMesInscricao, ");
		sb.append("  receitaComDescontoAcrescimoDoMesContratoReceita, receitaComDescontoAcrescimoDoMesInclusaoReposicao, receitaComDescontoAcrescimoDoMesOutros, ");

		sb.append("  valorRecebidoDoMesMatricula, valorRecebidoDoMesMaterialDidatico, valorRecebidoDoMesMensalidade, valorRecebidoDoMesRequerimento, valorRecebidoDoMesBiblioteca, valorRecebidoDoMesDevolucaoCheque, ");
		sb.append("  valorRecebidoDoMesNegociacao, valorRecebidoDoMesBolsaCusteada, valorRecebidoDoMesInscricao,  valorRecebidoDoMesContratoReceita, valorRecebidoDoMesInclusaoReposicao, valorRecebidoDoMesOutros, ");

		sb.append("  saldoReceberDoMesMatricula,  saldoReceberDoMesMaterialDidatico, saldoReceberDoMesMensalidade, saldoReceberDoMesRequerimento, saldoReceberDoMesBiblioteca, saldoReceberDoMesDevolucaoCheque, ");
		sb.append("  saldoReceberDoMesNegociacao, saldoReceberDoMesBolsaCusteada, saldoReceberDoMesInscricao, saldoReceberDoMesContratoReceita, saldoReceberDoMesInclusaoReposicao, saldoReceberDoMesOutros, ");

		sb.append("  totalVencidoDoMes, totalVencidoDoMesMatricula, totalVencidoDoMesMaterialDidatico, totalVencidoDoMesMensalidade, totalVencidoDoMesRequerimento, totalVencidoDoMesBiblioteca, totalVencidoDoMesDevolucaoCheque, ");
		sb.append("  totalVencidoDoMesNegociacao, totalVencidoDoMesBolsaCusteada, totalVencidoDoMesInscricao, totalVencidoDoMesContratoReceita, totalVencidoDoMesInclusaoReposicao, totalVencidoDoMesOutros, ");

		sb.append("  ano, mes ");
		sb.append("  from (");
		sb.append("  select sum(receitaDoMes::NUMERIC(20,2)) as receitaDoMes, ");
		sb.append("  sum(descontoDoMes::NUMERIC(20,2)) as descontoDoMes, sum(acrescimo::NUMERIC(20,2)) as acrescimo, sum(juroMulta::NUMERIC(20,2)) as juroMulta, sum(receitaComDescontoAcrescimoDoMes) as receitaComDescontoAcrescimoDoMes, ");
		sb.append("  sum(valorRecebidoDoMes::NUMERIC(20,2)) as valorRecebidoDoMes, sum(saldoReceberDoMes::NUMERIC(20,2)) as saldoReceberDoMes, ");

		sb.append("  sum(descontoconvenio) as descontoconvenio, sum(descontoinstituicao) as descontoinstituicao, sum(valorDescontoprogressivo) as valorDescontoprogressivo, sum(descontorateio) as descontoRateio, ");
		sb.append("  sum(descontoaluno) as descontoaluno, sum(descontorecebimento) as descontorecebimento, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesOutros,  ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesOutros, ");

		sb.append("  sum(case when (datavencimento < current_date and situacao = 'AR') then saldoReceberDoMes else 0.0 end) as totalVencidoDoMes, ");
		sb.append("  sum(case when tipoOrigem = 'MAT' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE')  and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end)  as totalVencidoDoMesOutros, ");

		sb.append("  ano, mes ");

		sb.append("  from (");
		sb.append("  select codigo, nossonumero, situacao,  datavencimento, tipoorigem, valor::NUMERIC(20,2) as receitaDoMes, ");
		sb.append("  case when (situacao = 'AR') then sum(valorDescontoCalculado) else ");
		sb.append("  (sum(descontoconvenio) + sum(descontoinstituicao) + sum(valorDescontoprogressivo) + sum(descontoaluno) + sum(descontorecebimento)+ sum(descontorateio))::numeric(20,2) end as descontoDoMes, ");
		sb.append("  case when (situacao = 'AR') then sum(acrescimo + juroCalculado + multaCalculado)::numeric(20,2) else sum(acrescimo + juro + multa)::numeric(20,2) end as acrescimo, ");
		sb.append("  case when (situacao = 'AR') then sum(juro + multa)::numeric(20,2) else sum(juro + multa)::numeric(20,2) end as juroMulta, ");
		sb.append("  (sum(valorRecebidoDoMes::NUMERIC(20,2)) + sum(saldoReceberDoMes::NUMERIC(20,2))) as receitaComDescontoAcrescimoDoMes, ");
		sb.append("  valorRecebidoDoMes::NUMERIC(20,2), saldoReceberDoMes::NUMERIC(20,2), descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento,  descontorateio,  ano, mes ");
		sb.append("  from (");
		sb.append("  select distinct contareceber.tipoOrigem, contareceber.codigo, nossonumero, "+filtrarPeriodoPor+" as datavencimento, situacao, ");
		sb.append("  case when (contareceber.acrescimo > 0) then contareceber.acrescimo::numeric(20,2) else 0.0 end as acrescimo, ");
		sb.append("  case when (contareceber.juro > 0) then contareceber.juro::numeric(20,2) else 0.0 end as juro, ");
		sb.append("  case when (contareceber.multa > 0) then contareceber.multa::numeric(20,2) else 0.0 end as multa, ");
		sb.append("  case when (contareceber.valorJuroCalculado > 0) then contareceber.valorJuroCalculado::numeric(20,2) else 0.0 end as juroCalculado, ");
		sb.append("  case when (contareceber.valorMultaCalculado > 0) then contareceber.valorMultaCalculado::numeric(20,2) else 0.0 end as multaCalculado, ");
		sb.append("  contareceber.valor::NUMERIC(20,2) as valor, ");
		sb.append("  case when (descontoconvenio > 0) then descontoconvenio::numeric(20,2) else 0.0 end as descontoconvenio, ");
		sb.append("  case when (descontoinstituicao > 0) then descontoinstituicao::numeric(20,2) else 0.0 end as descontoinstituicao, ");
		sb.append("  case when (valordescontoprogressivo > 0) then valordescontoprogressivo::numeric(20,2) else 0.0 end as valordescontoprogressivo, ");
		sb.append("  case when valordescontoalunojacalculado > 0 then valordescontoalunojacalculado::numeric(20,2) else 0.0 end as descontoaluno, ");
		sb.append("  case when (valorcalculadodescontolancadorecebimento > 0) then valorcalculadodescontolancadorecebimento::numeric(20,2) else 0.0 end as descontorecebimento, ");
		sb.append("  case when (situacao = 'AR' and valorDescontoCalculado > 0) then valorDescontoCalculado::NUMERIC(20,2) else 0.0	end valorDescontoCalculado, ");
		sb.append("  valorDescontoRateio as descontorateio, ");
		sb.append("  case when  ( valorRecebido is not null and situacao = 'RE')  then valorRecebido::NUMERIC(20,2) else 0.0 end  as valorRecebidoDoMes,  ");
		sb.append("  case when (situacao = 'AR') then contareceber.valorrecebercalculado::NUMERIC(20,2) else 0.0 end as saldoReceberDoMes, ");
		sb.append("  extract(year from "+filtrarPeriodoPor+")::INT as ano, extract(month from "+filtrarPeriodoPor+")::INT as mes  ");
		sb.append("  from contaReceber ");
		sb.append(" where  ");
		sb.append(" contaReceber.situacao not in ('NE', 'CF', 'RM', 'CA') and "+filtrarPeriodoPor+" >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and "+filtrarPeriodoPor+" <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(") as t group by ano, mes, valor, situacao, valorRecebidoDoMes, tipoorigem, saldoReceberDoMes, descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontorateio, ");
		sb.append(" descontoaluno, descontorecebimento, descontorateio, nossonumero, datavencimento, codigo ");
		sb.append(" ) as t1 group by ano, mes order by ano, mes ");
		sb.append(" ) as t2 ");
		sb.append(" ) as t3 ");
		// //System.out.println(sb.toString());
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		painelGestorVO.setTotalReceitaDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalAcrescimoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalJuroMultaDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalDescontoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalReceitaComDescontoAcrescimoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalSaldoAReceberDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalRecebidoDoMes(BigDecimal.ZERO);

		painelGestorVO.setReceitaDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setDescontoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setAcrescimoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setValorRecebidoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setSaldoReceberDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setTotalVencidoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setMediaInadimplenciaDoPeriodo(0.0);
		painelGestorVO.setMediaInadimplenciaDoPeriodoSemAcrescimo(0.0);

		while (dadosSQL.next()) {
			montarDadosMapaReceitaPorCompetênciaPainelGestorFinanceiroContaReceberMesAno(painelGestorVO, false, dadosSQL);
		}
		executarCalculoMediaInadimplenciaMapaReceitaPorCompetenciaPeridoPainelGestorFinanceiro(painelGestorVO);
	}

	/**
	 * Método responsável por criar o Mapa de Receita por Competência Mês/Ano
	 * 
	 * @param painelGestorVO
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @throws Exception
	 * @author CarlosEugenio
	 */
	public void consultarMapaReceitaPorCompetenciaPainelGestorFinanceiroPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String filtrarPeriodoPor) throws Exception {

		painelGestorVO.getPainelGestorContaReceberMesAnoVOs().clear();
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from ( ");
		sb.append("  select receitaDoMes, ");
		sb.append("  case when (acrescimo > 0 and receitaDoMes = receitaComDescontoAcrescimoDoMes) then (acrescimo::NUMERIC(20,2) + descontoDoMes::NUMERIC(20,2)) else descontoDoMes::NUMERIC(20,2) end as descontoDoMes, ");
		sb.append("  case when (descontoDoMes > 0 and receitaDoMes = receitaComDescontoAcrescimoDoMes) then (descontoDoMes + acrescimo) else acrescimo end as acrescimo, juroMulta, ");
		sb.append("  receitaComDescontoAcrescimoDoMes, valorRecebidoDoMes, saldoReceberDoMes, ");

		sb.append("  descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, ");

		sb.append("  receitaDoMesMatricula, receitaDoMesMaterialDidatico, receitaDoMesMensalidade, receitaDoMesRequerimento, receitaDoMesBiblioteca, receitaDoMesDevolucaoCheque, ");
		sb.append("  receitaDoMesNegociacao, receitaDoMesBolsaCusteada, receitaDoMesInscricao,  receitaDoMesContratoReceita, receitaDoMesInclusaoReposicao, receitaDoMesOutros, ");

		sb.append("  descontoDoMesMatricula, descontoDoMesMaterialDidatico, descontoDoMesMensalidade, descontoDoMesRequerimento, descontoDoMesBiblioteca, descontoDoMesDevolucaoCheque, ");
		sb.append("  descontoDoMesNegociacao, descontoDoMesBolsaCusteada, descontoDoMesInscricao, descontoDoMesContratoReceita, descontoDoMesInclusaoReposicao, descontoDoMesOutros, ");

		sb.append("  acrescimoDoMesMatricula, acrescimoDoMesMaterialDidatico, acrescimoDoMesMensalidade, acrescimoDoMesRequerimento, acrescimoDoMesBiblioteca, acrescimoDoMesDevolucaoCheque, ");
		sb.append("  acrescimoDoMesNegociacao, acrescimoDoMesBolsaCusteada, acrescimoDoMesInscricao, acrescimoDoMesContratoReceita, acrescimoDoMesInclusaoReposicao, acrescimoDoMesOutros, ");

		sb.append("  receitaComDescontoAcrescimoDoMesMatricula, receitaComDescontoAcrescimoDoMesMaterialDidatico, receitaComDescontoAcrescimoDoMesMensalidade, receitaComDescontoAcrescimoDoMesRequerimento, receitaComDescontoAcrescimoDoMesBiblioteca, ");
		sb.append("  receitaComDescontoAcrescimoDoMesDevolucaoCheque, receitaComDescontoAcrescimoDoMesNegociacao, receitaComDescontoAcrescimoDoMesBolsaCusteada, receitaComDescontoAcrescimoDoMesInscricao, ");
		sb.append("  receitaComDescontoAcrescimoDoMesContratoReceita, receitaComDescontoAcrescimoDoMesInclusaoReposicao, receitaComDescontoAcrescimoDoMesOutros, ");

		sb.append("  valorRecebidoDoMesMatricula, valorRecebidoDoMesMaterialDidatico, valorRecebidoDoMesMensalidade, valorRecebidoDoMesRequerimento, valorRecebidoDoMesBiblioteca, valorRecebidoDoMesDevolucaoCheque, ");
		sb.append("  valorRecebidoDoMesNegociacao, valorRecebidoDoMesBolsaCusteada, valorRecebidoDoMesInscricao,  valorRecebidoDoMesContratoReceita, valorRecebidoDoMesInclusaoReposicao, valorRecebidoDoMesOutros, ");

		sb.append("  saldoReceberDoMesMatricula, saldoReceberDoMesMaterialDidatico, saldoReceberDoMesMensalidade, saldoReceberDoMesRequerimento, saldoReceberDoMesBiblioteca, saldoReceberDoMesDevolucaoCheque, ");
		sb.append("  saldoReceberDoMesNegociacao, saldoReceberDoMesBolsaCusteada, saldoReceberDoMesInscricao, saldoReceberDoMesContratoReceita, saldoReceberDoMesInclusaoReposicao, saldoReceberDoMesOutros, ");

		sb.append("  totalVencidoDoMes, totalVencidoDoMesMatricula, totalVencidoDoMesMaterialDidatico, totalVencidoDoMesMensalidade, totalVencidoDoMesRequerimento, totalVencidoDoMesBiblioteca, totalVencidoDoMesDevolucaoCheque, ");
		sb.append("  totalVencidoDoMesNegociacao, totalVencidoDoMesBolsaCusteada, totalVencidoDoMesInscricao, totalVencidoDoMesContratoReceita, totalVencidoDoMesInclusaoReposicao, totalVencidoDoMesOutros, ");

		sb.append("  codigoCurso, curso_nome, niveleducacional ");
		sb.append("  from (");
		sb.append("  select sum(receitaDoMes::NUMERIC(20,2)) as receitaDoMes, ");
		sb.append("  sum(descontoDoMes::NUMERIC(20,2)) as descontoDoMes, sum(acrescimo::NUMERIC(20,2)) as acrescimo, sum(juroMulta::NUMERIC(20,2)) as juroMulta, sum(receitaComDescontoAcrescimoDoMes) as receitaComDescontoAcrescimoDoMes, ");
		sb.append("  sum(valorRecebidoDoMes::NUMERIC(20,2)) as valorRecebidoDoMes, sum(saldoReceberDoMes::NUMERIC(20,2)) as saldoReceberDoMes, ");

		sb.append("  sum(descontoconvenio) as descontoconvenio, sum(descontoinstituicao) as descontoinstituicao, sum(valorDescontoprogressivo) as valorDescontoprogressivo, sum(descontorateio) as descontoRateio, ");
		sb.append("  sum(descontoaluno) as descontoaluno, sum(descontorecebimento) as descontorecebimento, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesOutros,  ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesOutros, ");

		sb.append("  sum(case when (datavencimento < current_date and situacao = 'AR') then saldoReceberDoMes else 0.0 end) as totalVencidoDoMes, ");
		sb.append("  sum(case when tipoOrigem = 'MAT' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE')  and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end)  as totalVencidoDoMesOutros, ");

		sb.append("  codigoCurso, curso_nome, niveleducacional ");

		sb.append("  from (");
		sb.append("  select codigo, nossonumero, situacao, datavencimento, tipoorigem, valor::NUMERIC(20,2) as receitaDoMes, ");
		sb.append("  case when (situacao = 'AR') then sum(valorDescontoCalculado) else ");
		sb.append("  (sum(descontoconvenio) + sum(descontoinstituicao) + sum(valorDescontoprogressivo) + sum(descontoaluno) + sum(descontorecebimento) + sum(descontorateio))::numeric(20,2) end as descontoDoMes, ");
		sb.append("  case when (situacao = 'AR') then sum(acrescimo + juroCalculado + multaCalculado)::numeric(20,2) else sum(acrescimo + juro + multa)::numeric(20,2) end as acrescimo, ");
		sb.append("  case when (situacao = 'AR') then sum(juro + multa)::numeric(20,2) else sum(juro + multa)::numeric(20,2) end as juroMulta, ");
		sb.append("  (sum(valorRecebidoDoMes::NUMERIC(20,2)) + sum(saldoReceberDoMes::NUMERIC(20,2))) as receitaComDescontoAcrescimoDoMes, ");
		sb.append("  valorRecebidoDoMes::NUMERIC(20,2), saldoReceberDoMes::NUMERIC(20,2), descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontorateio, descontoaluno, descontorecebimento, codigoCurso, curso_nome, niveleducacional ");
		sb.append("  from (");
		sb.append("  select distinct contareceber.tipoOrigem, contareceber.codigo, nossonumero, "+filtrarPeriodoPor+" as  datavencimento, contareceber.situacao, ");
		sb.append("  case when (contareceber.acrescimo > 0) then contareceber.acrescimo::numeric(20,2) else 0.0 end as acrescimo, ");
		sb.append("  case when (contareceber.juro > 0) then contareceber.juro::numeric(20,2) else 0.0 end as juro, ");
		sb.append("  case when (contareceber.multa > 0) then contareceber.multa::numeric(20,2) else 0.0 end as multa, ");
		sb.append("  case when (contareceber.valorJuroCalculado > 0) then contareceber.valorJuroCalculado::numeric(20,2) else 0.0 end as juroCalculado, ");
		sb.append("  case when (contareceber.valorMultaCalculado > 0) then contareceber.valorMultaCalculado::numeric(20,2) else 0.0 end as multaCalculado, ");
		sb.append("  contareceber.valor::NUMERIC(20,2) as valor, ");
		sb.append("  case when (descontoconvenio > 0) then descontoconvenio::numeric(20,2) else 0.0 end as descontoconvenio, ");
		sb.append("  case when (descontoinstituicao > 0) then descontoinstituicao::numeric(20,2) else 0.0 end as descontoinstituicao, ");
		sb.append("  case when (valordescontoprogressivo > 0) then valordescontoprogressivo::numeric(20,2) else 0.0 end as valordescontoprogressivo, ");
		sb.append("  case when valordescontoalunojacalculado > 0 then valordescontoalunojacalculado::numeric(20,2) else 0.0 end as descontoaluno, ");
		sb.append("  case when (valorcalculadodescontolancadorecebimento > 0) then valorcalculadodescontolancadorecebimento::numeric(20,2) else 0.0 end as descontorecebimento, ");
		sb.append("  contareceber.valorDescontoRateio as descontorateio, ");
		sb.append("  case when (contareceber.situacao = 'AR' and valorDescontoCalculado > 0) then valorDescontoCalculado::NUMERIC(20,2) else 0.0	end valorDescontoCalculado, ");
		sb.append("  case when  ( valorRecebido is not null and contareceber.situacao = 'RE')  then valorRecebido::NUMERIC(20,2) else 0.0 end  as valorRecebidoDoMes,  ");
		sb.append("  case when (contareceber.situacao = 'AR') then contareceber.valorrecebercalculado::NUMERIC(20,2) else 0.0 end as saldoReceberDoMes, ");		
		sb.append("  curso.codigo AS codigoCurso, curso.nome AS curso_nome, curso.niveleducacional ");
		sb.append("  from contaReceber ");
		sb.append("  inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append("  inner join curso on curso.codigo = matricula.curso ");
		sb.append(" and curso.niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" where  ");
		sb.append(" contaReceber.situacao not in ('NE', 'CF', 'RM') and "+filtrarPeriodoPor+" >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and "+filtrarPeriodoPor+" <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));				
		sb.append(") as t group by codigoCurso, curso_nome, niveleducacional, valor, situacao, valorRecebidoDoMes, tipoorigem, saldoReceberDoMes, descontoconvenio, descontoinstituicao, valorDescontoprogressivo, ");
		sb.append(" descontoaluno, descontorecebimento, descontoRateio, nossonumero, datavencimento, codigo ");
		sb.append(" ) as t1 group by codigoCurso, curso_nome, niveleducacional order by curso_nome ");
		sb.append(" ) as t2 ");
		sb.append(" ) as t3 ");
		//System.out.println(sb.toString());
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		painelGestorVO.setTotalReceitaDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalAcrescimoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalDescontoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalReceitaComDescontoAcrescimoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalSaldoAReceberDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalRecebidoDoMes(BigDecimal.ZERO);

		painelGestorVO.setReceitaDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setDescontoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setAcrescimoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setValorRecebidoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setSaldoReceberDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setTotalVencidoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setMediaInadimplenciaDoPeriodo(0.0);
		painelGestorVO.setMediaInadimplenciaNoPeriodo(0.0);

		while (dadosSQL.next()) {
			montarDadosMapaReceitaPorCompetênciaPainelGestorFinanceiroContaReceberMesAno(painelGestorVO, true, dadosSQL);
		}
		executarCalculoMediaInadimplenciaMapaReceitaPorCompetenciaPeridoPainelGestorFinanceiro(painelGestorVO);
	}

	public void consultarMapaReceitaPorFluxoCaixaPainelGestorFinanceiroPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		painelGestorVO.getPainelGestorContaReceberFluxoCaixaMesAnoVOs().clear();
		StringBuilder sb = new StringBuilder();
		sb.append(" select receitaNoMes, descontoNoMes, acrescimo, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, ");
		sb.append(" receitaNoMesMatricula, receitaNoMesMaterialDidatico, receitaNoMesMensalidade, receitaNoMesRequerimento, receitaNoMesBiblioteca, receitaNoMesDevolucaoCheque, ");
		sb.append(" receitaNoMesNegociacao, receitaNoMesBolsaCusteada, receitaNoMesInscricao,  receitaNoMesContratoReceita,	receitaNoMesInclusaoReposicao, receitaNoMesOutros, ");
		sb.append(" descontoNoMesMatricula,descontoNoMesMaterialDidatico, descontoNoMesMensalidade, descontoNoMesRequerimento, descontoNoMesBiblioteca, descontoNoMesDevolucaoCheque, ");
		sb.append(" descontoNoMesNegociacao, descontoNoMesBolsaCusteada, descontoNoMesInscricao, descontoNoMesContratoReceita, descontoNoMesInclusaoReposicao, descontoNoMesOutros, ");
		sb.append(" acrescimoNoMesMatricula, acrescimoNoMesMaterialDidatico, acrescimoNoMesMensalidade, acrescimoNoMesRequerimento, acrescimoNoMesBiblioteca, acrescimoNoMesDevolucaoCheque, ");
		sb.append(" acrescimoNoMesNegociacao, acrescimoNoMesBolsaCusteada, acrescimoNoMesInscricao, acrescimoNoMesContratoReceita, acrescimoNoMesInclusaoReposicao, acrescimoNoMesOutros, ");
		sb.append(" case when valorInadimplencia > 0 then ((valorInadimplencia * 100) / receitaNoMes)::NUMERIC(20,2) else 0.0 end as taxaInadimplenciaNoMes, ");
		sb.append(" case when valorInadimplencia > 0 and receitaNoMes > 0 then (((valorInadimplencia - acrescimo) * 100) / receitaNoMes)::NUMERIC(20,2) else 0.0 end as taxaInadimplenciaNoMesSemAcrescimo,  ");
		sb.append(" codigoCurso, curso_nome, nivelEducacional from (");
		sb.append(" select sum(receitaNoMes::NUMERIC(20,2)) as receitaNoMes, sum(descontoNoMes::NUMERIC(20,2)) as descontoNoMes, sum(acrescimo::NUMERIC(20,2)) as acrescimo, ");
		sb.append(" sum(descontoconvenio) as descontoconvenio, sum(descontoinstituicao) as descontoinstituicao, sum(valorDescontoprogressivo) as valorDescontoprogressivo, ");
		sb.append(" sum(descontoaluno) as descontoaluno, sum(descontorecebimento) as descontorecebimento, sum(descontorateio) as descontorateio, ");
		// Receita No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMensalidade,");
		sb.append(" sum(case when tipoOrigem = 'REQ' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesOutros, ");
		// Desconto No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMensalidade, ");
		sb.append(" sum(case when tipoOrigem = 'REQ' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesOutros,  ");
		// Acrescimo No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMensalidade, ");
		sb.append(" sum(case when tipoOrigem = 'REQ' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesOutros, ");
		sb.append(" sum(valorInadimplencia) as valorInadimplencia, codigoCurso, curso_nome, nivelEducacional from (");
		sb.append(" select codigo, nossonumero, situacao, datavencimento, tipoorigem, valorRecebido::NUMERIC(20,2) as receitaNoMes, ");
		sb.append(" (sum(descontoconvenio) + sum(descontoinstituicao) + sum(valorDescontoprogressivo) + sum(descontoaluno) + sum(descontorecebimento) + sum(descontorateio))::numeric(20,2) as descontoNoMes,");
		sb.append(" sum(acrescimo + juro + multa)::numeric(20,2) as acrescimo, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, valorInadimplencia, codigoCurso, curso_nome, nivelEducacional ");
		sb.append(" from (");

		sb.append(" select distinct contareceber.tipoOrigem, contareceber.codigo, nossonumero, datavencimento, contareceber.situacao, ");
		sb.append(" case when (contareceber.acrescimo > 0) then contareceber.acrescimo::numeric(20,2) else 0.0 end as acrescimo, ");
		sb.append(" case when (contareceber.juro > 0) then contareceber.juro::numeric(20,2) else 0.0 end as juro, ");
		sb.append(" case when (contareceber.multa > 0) then contareceber.multa::numeric(20,2) else 0.0 end as multa, ");
		sb.append(" contareceber.valorRecebido::NUMERIC(20,2) as valorRecebido, ");
		sb.append(" case when (descontoconvenio > 0) then descontoconvenio::numeric(20,2) else 0.0 end as descontoconvenio, ");
		sb.append(" case when (descontoinstituicao > 0) then descontoinstituicao::numeric(20,2) else 0.0 end as descontoinstituicao, ");
		sb.append(" case when (valordescontoprogressivo > 0) then valordescontoprogressivo::numeric(20,2) else 0.0 end as valordescontoprogressivo, ");
		sb.append(" case when valordescontoalunojacalculado > 0 then valordescontoalunojacalculado::numeric(20,2) else 0.0 end as descontoaluno, ");
		sb.append(" case when (valorcalculadodescontolancadorecebimento > 0) then valorcalculadodescontolancadorecebimento::numeric(20,2) else 0.0 end as descontorecebimento, ");
		sb.append(" valorDescontoRateio as descontorateio, ");
		sb.append(" 0.0 AS valorInadimplencia, curso.codigo AS codigoCurso, curso.nome AS curso_nome, curso.nivelEducacional ");
		sb.append(" from contaReceber  ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append("  left join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append("  inner join curso on curso.codigo = matricula.curso ");
		sb.append(" where contaReceber.situacao in ('RE') ");
		sb.append("  and curso.niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' ");
		sb.append(" and negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira "));

		// Consulta o valor de Inadimplência
		sb.append(" union all ");

		sb.append(" select cast('' as varchar ) as tipoorigem, 0 AS codigo, cast('' as varchar ) as nossoNumero, null, cast('' as varchar) as situacao, ");
		sb.append(" 0.0 as acrescimo, 0.0 as juro, 0.0 as multa, 0.0 as valorrecebido, 0.0 as descontoconvenio, 0.0 as descontoinstituicao, 0.0 as valordescontoprogressivo, ");
		sb.append(" 0.0 as descontoaluno, 0.0 as descontoRecebimento, 0.0 as descontoRateio, ");
		sb.append(" sum(valorInadimplencia)::numeric(20,2) AS valorInadimplencia, codigoCurso, curso_nome, nivelEducacional from (");
		sb.append(" select case when (contareceber.situacao = 'RE' and datavencimento < negociacaorecebimento.data ");
		sb.append(" and extract(month from negociacaorecebimento.data)::INT != extract(month from datavencimento)::INT) then contaReceber.valorRecebido else ");
		sb.append(" case when (contareceber.situacao = 'AR' and contareceber.datavencimento < current_date ) then valorReceberCalculado else 0.0 end end AS valorInadimplencia, ");
		sb.append(" curso.codigo AS codigoCurso, curso.nome AS curso_nome, curso.nivelEducacional ");
		sb.append(" from contaReceber ");
		sb.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sb.append(" left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append("  left join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append("  inner join curso on curso.codigo = matricula.curso ");
		sb.append(" where 1=1 ");
		sb.append("  and curso.niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' ");
		sb.append(" and dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira "));
		sb.append(" order by datavencimento ");
		sb.append(" ) as t ");
		sb.append(" group by codigoCurso, curso_nome, nivelEducacional ");

		sb.append(") as t ");
		sb.append(" group by codigoCurso, curso_nome, nivelEducacional, valorRecebido, situacao, tipoorigem, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoRateio, ");
		sb.append(" descontoaluno, descontorecebimento, nossonumero, datavencimento, codigo, valorInadimplencia ");
		sb.append(") as t1 ");
		sb.append(" group by codigoCurso, curso_nome, nivelEducacional ");
		sb.append(" order by codigoCurso, curso_nome, nivelEducacional ");
		sb.append(" ) as t2 ");

		//System.out.println(sb.toString());
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.setTotalReceitaNoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalAcrescimoNoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalDescontoNoMes(BigDecimal.ZERO);

		painelGestorVO.setReceitaNoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setDescontoNoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesOutros(BigDecimal.ZERO);

		while (dadosSQL.next()) {
			montarDadosMapaReceitaPorFluxoCaixaPainelGestorFinanceiroContaReceberMesAno(painelGestorVO, true, dadosSQL);
		}
		executarCalculoMediaInadimplenciaMapaReceitaPorFluxoCaixaPeridoPainelGestorFinanceiro(painelGestorVO);
	}

	public void consultarMapaReceitaPorFluxoCaixaPainelGestorFinanceiroPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso) throws Exception {
		painelGestorVO.getPainelGestorContaReceberFluxoCaixaMesAnoVOs().clear();
		StringBuilder sb = new StringBuilder();
		sb.append(" select receitaNoMes, descontoNoMes, acrescimo, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, ");
		sb.append(" receitaNoMesMatricula, receitaNoMesMaterialDidatico,	receitaNoMesMensalidade, receitaNoMesRequerimento, receitaNoMesBiblioteca, receitaNoMesDevolucaoCheque, ");
		sb.append(" receitaNoMesNegociacao, receitaNoMesBolsaCusteada, receitaNoMesInscricao,  receitaNoMesContratoReceita,	receitaNoMesInclusaoReposicao, receitaNoMesOutros, ");
		sb.append(" descontoNoMesMatricula, descontoNoMesMaterialDidatico, descontoNoMesMensalidade, descontoNoMesRequerimento, descontoNoMesBiblioteca, descontoNoMesDevolucaoCheque, ");
		sb.append(" descontoNoMesNegociacao, descontoNoMesBolsaCusteada, descontoNoMesInscricao, descontoNoMesContratoReceita, descontoNoMesInclusaoReposicao, descontoNoMesOutros, ");
		sb.append(" acrescimoNoMesMatricula, acrescimoNoMesMaterialDidatico, acrescimoNoMesMensalidade, acrescimoNoMesRequerimento, acrescimoNoMesBiblioteca, acrescimoNoMesDevolucaoCheque, ");
		sb.append(" acrescimoNoMesNegociacao, acrescimoNoMesBolsaCusteada, acrescimoNoMesInscricao, acrescimoNoMesContratoReceita, acrescimoNoMesInclusaoReposicao, acrescimoNoMesOutros, ");
		sb.append(" case when (receitaNoMes > 0 AND valorInadimplencia > 0) then ((valorInadimplencia * 100) / receitaNoMes)::NUMERIC(20,2) else 0.0 end as taxaInadimplenciaNoMes,  codigoTurma, turma, nivelEducacional from (");
		sb.append(" select sum(receitaNoMes::NUMERIC(20,2)) as receitaNoMes, sum(descontoNoMes::NUMERIC(20,2)) as descontoNoMes, sum(acrescimo::NUMERIC(20,2)) as acrescimo, ");
		sb.append(" sum(descontoconvenio) as descontoconvenio, sum(descontoinstituicao) as descontoinstituicao, sum(valorDescontoprogressivo) as valorDescontoprogressivo, ");
		sb.append(" sum(descontoaluno) as descontoaluno, sum(descontorecebimento) as descontorecebimento, sum(descontorateio) as descontorateio, ");
		// Receita No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMaterialDidatico,");
		sb.append(" sum(case when tipoOrigem = 'MEN' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesMensalidade,");
		sb.append(" sum(case when tipoOrigem = 'REQ' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaNoMes::NUMERIC(20,2) else 0.0 end) as receitaNoMesOutros, ");
		// Desconto No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesMensalidade, ");
		sb.append(" sum(case when tipoOrigem = 'REQ' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT','MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then descontoNoMes::NUMERIC(20,2) else 0.0 end) as descontoNoMesOutros,  ");
		// Acrescimo No Mês por Tipo Origem
		sb.append(" sum(case when tipoOrigem = 'MAT' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesMensalidade, ");
		sb.append(" sum(case when tipoOrigem = 'REQ' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoNoMesOutros, ");
		sb.append(" sum(valorInadimplencia) as valorInadimplencia, codigoTurma, turma, nivelEducacional from (");
		sb.append(" select codigo, nossonumero, situacao, datavencimento, tipoorigem, valorRecebido::NUMERIC(20,2) as receitaNoMes, ");
		sb.append(" (sum(descontoconvenio) + sum(descontoinstituicao) + sum(valorDescontoprogressivo) + sum(descontoaluno) + sum(descontorecebimento) + sum(descontorateio))::numeric(20,2) as descontoNoMes,");
		sb.append(" sum(acrescimo + juro + multa)::numeric(20,2) as acrescimo, ");
		sb.append(" descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, valorInadimplencia, codigoTurma, turma, nivelEducacional ");
		sb.append(" from (");

		sb.append(" select distinct contareceber.tipoOrigem, contareceber.codigo, nossonumero, datavencimento, contareceber.situacao, ");
		sb.append(" case when (contareceber.acrescimo > 0) then contareceber.acrescimo::numeric(20,2) else 0.0 end as acrescimo, ");
		sb.append(" case when (contareceber.juro > 0) then contareceber.juro::numeric(20,2) else 0.0 end as juro, ");
		sb.append(" case when (contareceber.multa > 0) then contareceber.multa::numeric(20,2) else 0.0 end as multa, ");
		sb.append(" contareceber.valorRecebido::NUMERIC(20,2) as valorRecebido, ");
		sb.append(" case when (descontoconvenio > 0) then descontoconvenio::numeric(20,2) else 0.0 end as descontoconvenio, ");
		sb.append(" case when (descontoinstituicao > 0) then descontoinstituicao::numeric(20,2) else 0.0 end as descontoinstituicao, ");
		sb.append(" case when (valordescontoprogressivo > 0) then valordescontoprogressivo::numeric(20,2) else 0.0 end as valordescontoprogressivo, ");
		sb.append(" case when valordescontoalunojacalculado > 0 then valordescontoalunojacalculado::numeric(20,2) else 0.0 end as descontoaluno, ");
		sb.append(" case when (valorcalculadodescontolancadorecebimento > 0) then valorcalculadodescontolancadorecebimento::numeric(20,2) else 0.0 end as descontorecebimento, ");
		sb.append(" valordescontorateio as descontorateio, ");
		sb.append(" 0.0 AS valorInadimplencia, turma.codigo AS codigoTurma, turma.identificadorTurma AS turma, curso.nivelEducacional ");
		sb.append(" from contaReceber  ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append("  left join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  and matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sb.append("  inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append("  inner join curso on curso.codigo = matricula.curso ");
		sb.append(" where contaReceber.situacao in ('RE') ");
		sb.append("  and curso.niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and curso.codigo = ").append(codigoCurso);
		sb.append(" and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' ");
		sb.append(" and negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira "));

		// Consulta o valor de Inadimplência
		sb.append(" union all ");

		sb.append(" select cast('' as varchar ) as tipoorigem, 0 AS codigo, cast('' as varchar ) as nossoNumero, null, cast('' as varchar) as situacao, ");
		sb.append(" 0.0 as acrescimo, 0.0 as juro, 0.0 as multa, 0.0 as valorrecebido, 0.0 as descontoconvenio, 0.0 as descontoinstituicao, 0.0 as valordescontoprogressivo, ");
		sb.append(" 0.0 as descontoaluno, 0.0 as descontoRecebimento, 0.0 as descontorateio, ");
		sb.append(" sum(valorInadimplencia)::numeric(20,2) AS valorInadimplencia, codigoTurma, turma, nivelEducacional from (");
		sb.append(" select case when (contareceber.situacao = 'RE' and datavencimento < negociacaorecebimento.data ");
		sb.append(" and extract(month from negociacaorecebimento.data)::INT != extract(month from datavencimento)::INT) then contaReceber.valorRecebido else ");
		sb.append(" case when (contareceber.situacao = 'AR' and contareceber.datavencimento < current_date ) then valorReceberCalculado else 0.0 end end AS valorInadimplencia, ");
		sb.append(" turma.codigo AS codigoTurma, turma.identificadorTurma AS turma, curso.nivelEducacional ");
		sb.append(" from contaReceber ");
		sb.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sb.append(" left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append("  left join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  and matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sb.append("  inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append("  inner join curso on curso.codigo = matricula.curso ");
		sb.append(" where 1=1 ");
		sb.append("  and curso.niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and curso.codigo = ").append(codigoCurso);
		sb.append(" and dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' ");
		sb.append(" and dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira "));
		sb.append(" order by datavencimento ");
		sb.append(" ) as t ");
		sb.append(" group by codigoTurma, turma, nivelEducacional ");

		sb.append(") as t ");
		sb.append(" group by codigoTurma, turma, nivelEducacional, valorRecebido, situacao, tipoorigem, ");
		sb.append(" descontoconvenio, descontoinstituicao, descontorateio, valorDescontoprogressivo, ");
		sb.append(" descontoaluno, descontorecebimento, nossonumero, datavencimento, codigo, valorInadimplencia ");
		sb.append(") as t1 ");
		sb.append(" group by codigoTurma, turma, nivelEducacional ");
		sb.append(" order by codigoTurma, turma, nivelEducacional ");
		sb.append(" ) as t2 ");

		//System.out.println(sb.toString());
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		painelGestorVO.setTotalReceitaNoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalAcrescimoNoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalDescontoNoMes(BigDecimal.ZERO);

		painelGestorVO.setReceitaNoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaNoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setDescontoNoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setDescontoNoMesOutros(BigDecimal.ZERO);

		while (dadosSQL.next()) {
			montarDadosMapaReceitaPorFluxoCaixaPainelGestorFinanceiroContaReceberPorTurma(painelGestorVO, true, dadosSQL);
		}
		executarCalculoMediaInadimplenciaMapaReceitaPorFluxoCaixaPeridoPainelGestorFinanceiroPorTurma(painelGestorVO);
	}

	public void montarDadosMapaReceitaPorCompetênciaPainelGestorFinanceiroContaReceberMesAno(PainelGestorVO painelGestorVO, Boolean filtarPorNivelEducacional, SqlRowSet dadosSQL) {
		PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = new PainelGestorContaReceberMesAnoVO();
		painelGestorContaReceberMesAnoVO.setReceitaDoMes(dadosSQL.getBigDecimal("receitaDoMes"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMes(dadosSQL.getBigDecimal("descontoDoMes"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMes(dadosSQL.getBigDecimal("acrescimo"));
		painelGestorContaReceberMesAnoVO.setJuroMultaDoMes(dadosSQL.getBigDecimal("juroMulta"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMes(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMes"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMes(dadosSQL.getBigDecimal("valorRecebidoDoMes"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMes(dadosSQL.getBigDecimal("saldoReceberDoMes"));

		painelGestorContaReceberMesAnoVO.setDescontoConvenio(dadosSQL.getBigDecimal("descontoConvenio"));
		painelGestorContaReceberMesAnoVO.setDescontoInstituicao(dadosSQL.getBigDecimal("descontoInstituicao"));
		painelGestorContaReceberMesAnoVO.setValorDescontoProgressivo(dadosSQL.getBigDecimal("valorDescontoProgressivo"));
		painelGestorContaReceberMesAnoVO.setDescontoAluno(dadosSQL.getBigDecimal("descontoAluno"));
		painelGestorContaReceberMesAnoVO.setDescontoRateio(dadosSQL.getBigDecimal("descontoRateio"));
		painelGestorContaReceberMesAnoVO.setDescontoRecebimento(dadosSQL.getBigDecimal("descontoRecebimento"));

		painelGestorContaReceberMesAnoVO.setReceitaDoMesMatricula(dadosSQL.getBigDecimal("receitaDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesMaterialDidatico(dadosSQL.getBigDecimal("receitaDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesMensalidade(dadosSQL.getBigDecimal("receitaDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesRequerimento(dadosSQL.getBigDecimal("receitaDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesBiblioteca(dadosSQL.getBigDecimal("receitaDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesDevolucaoCheque(dadosSQL.getBigDecimal("receitaDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesNegociacao(dadosSQL.getBigDecimal("receitaDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesBolsaCusteada(dadosSQL.getBigDecimal("receitaDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesInscricao(dadosSQL.getBigDecimal("receitaDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesContratoReceita(dadosSQL.getBigDecimal("receitaDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesInclusaoReposicao(dadosSQL.getBigDecimal("receitaDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesOutros(dadosSQL.getBigDecimal("receitaDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setDescontoDoMesMatricula(dadosSQL.getBigDecimal("descontoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesMaterialDidatico(dadosSQL.getBigDecimal("descontoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesMensalidade(dadosSQL.getBigDecimal("descontoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesRequerimento(dadosSQL.getBigDecimal("descontoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesBiblioteca(dadosSQL.getBigDecimal("descontoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("descontoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesNegociacao(dadosSQL.getBigDecimal("descontoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesBolsaCusteada(dadosSQL.getBigDecimal("descontoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesInscricao(dadosSQL.getBigDecimal("descontoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesContratoReceita(dadosSQL.getBigDecimal("descontoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("descontoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesOutros(dadosSQL.getBigDecimal("descontoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesMatricula(dadosSQL.getBigDecimal("acrescimoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesMaterialDidatico(dadosSQL.getBigDecimal("acrescimoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesMensalidade(dadosSQL.getBigDecimal("acrescimoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesRequerimento(dadosSQL.getBigDecimal("acrescimoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesBiblioteca(dadosSQL.getBigDecimal("acrescimoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("acrescimoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesNegociacao(dadosSQL.getBigDecimal("acrescimoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesBolsaCusteada(dadosSQL.getBigDecimal("acrescimoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesInscricao(dadosSQL.getBigDecimal("acrescimoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesContratoReceita(dadosSQL.getBigDecimal("acrescimoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("acrescimoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesOutros(dadosSQL.getBigDecimal("acrescimoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesMatricula(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesMaterialDidatico(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesMensalidade(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesRequerimento(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesBiblioteca(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesNegociacao(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesBolsaCusteada(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesInscricao(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesContratoReceita(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesOutros(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesMatricula(dadosSQL.getBigDecimal("valorRecebidoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesMaterialDidatico(dadosSQL.getBigDecimal("valorRecebidoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesMensalidade(dadosSQL.getBigDecimal("valorRecebidoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesRequerimento(dadosSQL.getBigDecimal("valorRecebidoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesBiblioteca(dadosSQL.getBigDecimal("valorRecebidoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("valorRecebidoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesNegociacao(dadosSQL.getBigDecimal("valorRecebidoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesBolsaCusteada(dadosSQL.getBigDecimal("valorRecebidoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesInscricao(dadosSQL.getBigDecimal("valorRecebidoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesContratoReceita(dadosSQL.getBigDecimal("valorRecebidoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("valorRecebidoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesOutros(dadosSQL.getBigDecimal("valorRecebidoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesMatricula(dadosSQL.getBigDecimal("saldoReceberDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesMaterialDidatico(dadosSQL.getBigDecimal("saldoReceberDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesMensalidade(dadosSQL.getBigDecimal("saldoReceberDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesRequerimento(dadosSQL.getBigDecimal("saldoReceberDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesBiblioteca(dadosSQL.getBigDecimal("saldoReceberDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesDevolucaoCheque(dadosSQL.getBigDecimal("saldoReceberDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesNegociacao(dadosSQL.getBigDecimal("saldoReceberDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesBolsaCusteada(dadosSQL.getBigDecimal("saldoReceberDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesInscricao(dadosSQL.getBigDecimal("saldoReceberDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesContratoReceita(dadosSQL.getBigDecimal("saldoReceberDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesInclusaoReposicao(dadosSQL.getBigDecimal("saldoReceberDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesOutros(dadosSQL.getBigDecimal("saldoReceberDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMes(dadosSQL.getBigDecimal("totalVencidoDoMes"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesMatricula(dadosSQL.getBigDecimal("totalVencidoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesMaterialDidatico(dadosSQL.getBigDecimal("totalVencidoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesMensalidade(dadosSQL.getBigDecimal("totalVencidoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesRequerimento(dadosSQL.getBigDecimal("totalVencidoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesBiblioteca(dadosSQL.getBigDecimal("totalVencidoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("totalVencidoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesNegociacao(dadosSQL.getBigDecimal("totalVencidoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesBolsaCusteada(dadosSQL.getBigDecimal("totalVencidoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesInscricao(dadosSQL.getBigDecimal("totalVencidoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesContratoReceita(dadosSQL.getBigDecimal("totalVencidoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("totalVencidoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesOutros(dadosSQL.getBigDecimal("totalVencidoDoMesOutros"));

		if (filtarPorNivelEducacional) {
			painelGestorContaReceberMesAnoVO.setCurso(dadosSQL.getString("curso_nome"));
			painelGestorContaReceberMesAnoVO.setCodigoCurso(dadosSQL.getInt("codigoCurso"));
			painelGestorContaReceberMesAnoVO.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		} else {
			painelGestorContaReceberMesAnoVO.setMesAno(dadosSQL.getInt("mes") + "/" + dadosSQL.getInt("ano"));
		}
		painelGestorVO.getPainelGestorContaReceberMesAnoVOs().add(painelGestorContaReceberMesAnoVO);
	}

	public void montarDadosPainelGestorFinanceiroContaReceberMesAnoTurma(PainelGestorVO painelGestorVO, Boolean filtarPorNivelEducacional, SqlRowSet dadosSQL) {
		PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO = new PainelGestorContaReceberMesAnoVO();
		painelGestorContaReceberMesAnoVO.setReceitaDoMes(dadosSQL.getBigDecimal("receitaDoMes"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMes(dadosSQL.getBigDecimal("descontoDoMes"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMes(dadosSQL.getBigDecimal("acrescimo"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMes(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMes"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMes(dadosSQL.getBigDecimal("valorRecebidoDoMes"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMes(dadosSQL.getBigDecimal("saldoReceberDoMes"));
		
		painelGestorContaReceberMesAnoVO.setDescontoConvenio(dadosSQL.getBigDecimal("descontoConvenio"));
		painelGestorContaReceberMesAnoVO.setDescontoInstituicao(dadosSQL.getBigDecimal("descontoInstituicao"));
		painelGestorContaReceberMesAnoVO.setValorDescontoProgressivo(dadosSQL.getBigDecimal("valorDescontoProgressivo"));
		painelGestorContaReceberMesAnoVO.setDescontoAluno(dadosSQL.getBigDecimal("descontoAluno"));
		painelGestorContaReceberMesAnoVO.setDescontoRateio(dadosSQL.getBigDecimal("descontoRateio"));
		painelGestorContaReceberMesAnoVO.setDescontoRecebimento(dadosSQL.getBigDecimal("descontoRecebimento"));

		painelGestorContaReceberMesAnoVO.setReceitaDoMesMatricula(dadosSQL.getBigDecimal("receitaDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesMaterialDidatico(dadosSQL.getBigDecimal("receitaDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesMensalidade(dadosSQL.getBigDecimal("receitaDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesRequerimento(dadosSQL.getBigDecimal("receitaDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesBiblioteca(dadosSQL.getBigDecimal("receitaDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesDevolucaoCheque(dadosSQL.getBigDecimal("receitaDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesNegociacao(dadosSQL.getBigDecimal("receitaDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesBolsaCusteada(dadosSQL.getBigDecimal("receitaDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesInscricao(dadosSQL.getBigDecimal("receitaDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesContratoReceita(dadosSQL.getBigDecimal("receitaDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesInclusaoReposicao(dadosSQL.getBigDecimal("receitaDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setReceitaDoMesOutros(dadosSQL.getBigDecimal("receitaDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setDescontoDoMesMatricula(dadosSQL.getBigDecimal("descontoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesMaterialDidatico(dadosSQL.getBigDecimal("descontoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesMensalidade(dadosSQL.getBigDecimal("descontoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesRequerimento(dadosSQL.getBigDecimal("descontoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesBiblioteca(dadosSQL.getBigDecimal("descontoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("descontoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesNegociacao(dadosSQL.getBigDecimal("descontoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesBolsaCusteada(dadosSQL.getBigDecimal("descontoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesInscricao(dadosSQL.getBigDecimal("descontoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesContratoReceita(dadosSQL.getBigDecimal("descontoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("descontoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setDescontoDoMesOutros(dadosSQL.getBigDecimal("descontoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesMatricula(dadosSQL.getBigDecimal("acrescimoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesMaterialDidatico(dadosSQL.getBigDecimal("acrescimoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesMensalidade(dadosSQL.getBigDecimal("acrescimoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesRequerimento(dadosSQL.getBigDecimal("acrescimoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesBiblioteca(dadosSQL.getBigDecimal("acrescimoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("acrescimoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesNegociacao(dadosSQL.getBigDecimal("acrescimoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesBolsaCusteada(dadosSQL.getBigDecimal("acrescimoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesInscricao(dadosSQL.getBigDecimal("acrescimoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesContratoReceita(dadosSQL.getBigDecimal("acrescimoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("acrescimoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setAcrescimoDoMesOutros(dadosSQL.getBigDecimal("acrescimoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesMatricula(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesMaterialDidatico(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesMensalidade(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesRequerimento(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesBiblioteca(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesNegociacao(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesBolsaCusteada(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesInscricao(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesContratoReceita(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setReceitaComDescontoAcrescimoDoMesOutros(dadosSQL.getBigDecimal("receitaComDescontoAcrescimoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesMatricula(dadosSQL.getBigDecimal("valorRecebidoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesMaterialDidatico(dadosSQL.getBigDecimal("valorRecebidoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesMensalidade(dadosSQL.getBigDecimal("valorRecebidoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesRequerimento(dadosSQL.getBigDecimal("valorRecebidoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesBiblioteca(dadosSQL.getBigDecimal("valorRecebidoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("valorRecebidoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesNegociacao(dadosSQL.getBigDecimal("valorRecebidoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesBolsaCusteada(dadosSQL.getBigDecimal("valorRecebidoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesInscricao(dadosSQL.getBigDecimal("valorRecebidoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesContratoReceita(dadosSQL.getBigDecimal("valorRecebidoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("valorRecebidoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setValorRecebidoDoMesOutros(dadosSQL.getBigDecimal("valorRecebidoDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesMatricula(dadosSQL.getBigDecimal("saldoReceberDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesMaterialDidatico(dadosSQL.getBigDecimal("saldoReceberDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesMensalidade(dadosSQL.getBigDecimal("saldoReceberDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesRequerimento(dadosSQL.getBigDecimal("saldoReceberDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesBiblioteca(dadosSQL.getBigDecimal("saldoReceberDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesDevolucaoCheque(dadosSQL.getBigDecimal("saldoReceberDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesNegociacao(dadosSQL.getBigDecimal("saldoReceberDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesBolsaCusteada(dadosSQL.getBigDecimal("saldoReceberDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesInscricao(dadosSQL.getBigDecimal("saldoReceberDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesContratoReceita(dadosSQL.getBigDecimal("saldoReceberDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesInclusaoReposicao(dadosSQL.getBigDecimal("saldoReceberDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setSaldoReceberDoMesOutros(dadosSQL.getBigDecimal("saldoReceberDoMesOutros"));

		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMes(dadosSQL.getBigDecimal("totalVencidoDoMes"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesMatricula(dadosSQL.getBigDecimal("totalVencidoDoMesMatricula"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesMaterialDidatico(dadosSQL.getBigDecimal("totalVencidoDoMesMaterialDidatico"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesMensalidade(dadosSQL.getBigDecimal("totalVencidoDoMesMensalidade"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesRequerimento(dadosSQL.getBigDecimal("totalVencidoDoMesRequerimento"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesBiblioteca(dadosSQL.getBigDecimal("totalVencidoDoMesBiblioteca"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesDevolucaoCheque(dadosSQL.getBigDecimal("totalVencidoDoMesDevolucaoCheque"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesNegociacao(dadosSQL.getBigDecimal("totalVencidoDoMesNegociacao"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesBolsaCusteada(dadosSQL.getBigDecimal("totalVencidoDoMesBolsaCusteada"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesInscricao(dadosSQL.getBigDecimal("totalVencidoDoMesInscricao"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesContratoReceita(dadosSQL.getBigDecimal("totalVencidoDoMesContratoReceita"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesInclusaoReposicao(dadosSQL.getBigDecimal("totalVencidoDoMesInclusaoReposicao"));
		painelGestorContaReceberMesAnoVO.setTotalVencidoDoMesOutros(dadosSQL.getBigDecimal("totalVencidoDoMesOutros"));

		if (filtarPorNivelEducacional) {
			painelGestorContaReceberMesAnoVO.setTurma(dadosSQL.getString("turma"));
			painelGestorContaReceberMesAnoVO.setCodigoTurma(dadosSQL.getInt("codigoTurma"));
			painelGestorContaReceberMesAnoVO.setNivelEducacional(dadosSQL.getString("nivelEducacional"));
		} else {
			painelGestorContaReceberMesAnoVO.setMesAno(dadosSQL.getInt("mes") + "/" + dadosSQL.getInt("ano"));
		}
		painelGestorVO.getPainelGestorContaReceberMesAnoTurmaVOs().add(painelGestorContaReceberMesAnoVO);
	}

	public void executarCalculoMediaInadimplenciaMapaReceitaPorCompetenciaPeridoPainelGestorFinanceiro(PainelGestorVO painelGestorVO) {
		for (PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO : painelGestorVO.getPainelGestorContaReceberMesAnoVOs()) {
			painelGestorContaReceberMesAnoVO.getContaReceberPainelGestorVOs().clear();
			
			painelGestorVO.setTotalReceitaDoMes(painelGestorVO.getTotalReceitaDoMes().add(painelGestorContaReceberMesAnoVO.getReceitaDoMes()));
			painelGestorVO.setTotalAcrescimoDoMes(painelGestorVO.getTotalAcrescimoDoMes().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMes()));
			painelGestorVO.setTotalJuroMultaDoMes(painelGestorVO.getTotalJuroMultaDoMes().add(painelGestorContaReceberMesAnoVO.getJuroMultaDoMes()));
			painelGestorVO.setTotalDescontoDoMes(painelGestorVO.getTotalDescontoDoMes().add(painelGestorContaReceberMesAnoVO.getDescontoDoMes()));
			painelGestorVO.setTotalReceitaComDescontoAcrescimoDoMes(painelGestorVO.getTotalReceitaComDescontoAcrescimoDoMes().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMes()));
			painelGestorVO.setTotalSaldoAReceberDoMes(painelGestorVO.getTotalSaldoAReceberDoMes().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMes()));
			painelGestorVO.setTotalRecebidoDoMes(painelGestorVO.getTotalRecebidoDoMes().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMes()));

			painelGestorVO.setTotalDescontoConvenioDoMes(painelGestorVO.getTotalDescontoConvenioDoMes().add(painelGestorContaReceberMesAnoVO.getDescontoConvenio()));
			painelGestorVO.setTotalDescontoInstituicaoDoMes(painelGestorVO.getTotalDescontoInstituicaoDoMes().add(painelGestorContaReceberMesAnoVO.getDescontoInstituicao()));
			painelGestorVO.setTotalDescontoProgressivoDoMes(painelGestorVO.getTotalDescontoProgressivoDoMes().add(painelGestorContaReceberMesAnoVO.getValorDescontoProgressivo()));
			painelGestorVO.setTotalDescontoAlunoDoMes(painelGestorVO.getTotalDescontoAlunoDoMes().add(painelGestorContaReceberMesAnoVO.getDescontoAluno()));
			painelGestorVO.setTotalDescontoRateioDoMes(painelGestorVO.getTotalDescontoRateioDoMes().add(painelGestorContaReceberMesAnoVO.getDescontoRateio()));
			painelGestorVO.setTotalDescontoRecebimentoDoMes(painelGestorVO.getTotalDescontoRecebimentoDoMes().add(painelGestorContaReceberMesAnoVO.getDescontoRecebimento()));

			painelGestorVO.setReceitaDoMesMatricula(painelGestorVO.getReceitaDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesMatricula()));
			painelGestorVO.setReceitaDoMesMaterialDidatico(painelGestorVO.getReceitaDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesMaterialDidatico()));
			painelGestorVO.setReceitaDoMesMensalidade(painelGestorVO.getReceitaDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesMensalidade()));
			painelGestorVO.setReceitaDoMesRequerimento(painelGestorVO.getReceitaDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesRequerimento()));
			painelGestorVO.setReceitaDoMesBiblioteca(painelGestorVO.getReceitaDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesBiblioteca()));
			painelGestorVO.setReceitaDoMesDevolucaoCheque(painelGestorVO.getReceitaDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesDevolucaoCheque()));
			painelGestorVO.setReceitaDoMesNegociacao(painelGestorVO.getReceitaDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesNegociacao()));
			painelGestorVO.setReceitaDoMesBolsaCusteada(painelGestorVO.getReceitaDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesBolsaCusteada()));
			painelGestorVO.setReceitaDoMesInscricao(painelGestorVO.getReceitaDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesInscricao()));
			painelGestorVO.setReceitaDoMesContratoReceita(painelGestorVO.getReceitaDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesContratoReceita()));
			painelGestorVO.setReceitaDoMesInclusaoReposicao(painelGestorVO.getReceitaDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesInclusaoReposicao()));
			painelGestorVO.setReceitaDoMesOutros(painelGestorVO.getReceitaDoMesOutros().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesOutros()));

			painelGestorVO.setDescontoDoMesMatricula(painelGestorVO.getDescontoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesMatricula()));
			painelGestorVO.setDescontoDoMesMaterialDidatico(painelGestorVO.getDescontoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesMaterialDidatico()));
			painelGestorVO.setDescontoDoMesMensalidade(painelGestorVO.getDescontoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesMensalidade()));
			painelGestorVO.setDescontoDoMesRequerimento(painelGestorVO.getDescontoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesRequerimento()));
			painelGestorVO.setDescontoDoMesBiblioteca(painelGestorVO.getDescontoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesBiblioteca()));
			painelGestorVO.setDescontoDoMesDevolucaoCheque(painelGestorVO.getDescontoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesDevolucaoCheque()));
			painelGestorVO.setDescontoDoMesNegociacao(painelGestorVO.getDescontoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesNegociacao()));
			painelGestorVO.setDescontoDoMesBolsaCusteada(painelGestorVO.getDescontoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesBolsaCusteada()));
			painelGestorVO.setDescontoDoMesInscricao(painelGestorVO.getDescontoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesInscricao()));
			painelGestorVO.setDescontoDoMesContratoReceita(painelGestorVO.getDescontoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesContratoReceita()));
			painelGestorVO.setDescontoDoMesInclusaoReposicao(painelGestorVO.getDescontoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesInclusaoReposicao()));
			painelGestorVO.setDescontoDoMesOutros(painelGestorVO.getDescontoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesOutros()));

			painelGestorVO.setAcrescimoDoMesMatricula(painelGestorVO.getAcrescimoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesMatricula()));
			painelGestorVO.setAcrescimoDoMesMaterialDidatico(painelGestorVO.getAcrescimoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesMaterialDidatico()));
			painelGestorVO.setAcrescimoDoMesMensalidade(painelGestorVO.getAcrescimoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesMensalidade()));
			painelGestorVO.setAcrescimoDoMesRequerimento(painelGestorVO.getAcrescimoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesRequerimento()));
			painelGestorVO.setAcrescimoDoMesBiblioteca(painelGestorVO.getAcrescimoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesBiblioteca()));
			painelGestorVO.setAcrescimoDoMesDevolucaoCheque(painelGestorVO.getAcrescimoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesDevolucaoCheque()));
			painelGestorVO.setAcrescimoDoMesNegociacao(painelGestorVO.getAcrescimoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesNegociacao()));
			painelGestorVO.setAcrescimoDoMesBolsaCusteada(painelGestorVO.getAcrescimoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesBolsaCusteada()));
			painelGestorVO.setAcrescimoDoMesInscricao(painelGestorVO.getAcrescimoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesInscricao()));
			painelGestorVO.setAcrescimoDoMesContratoReceita(painelGestorVO.getAcrescimoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesContratoReceita()));
			painelGestorVO.setAcrescimoDoMesInclusaoReposicao(painelGestorVO.getAcrescimoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesInclusaoReposicao()));
			painelGestorVO.setAcrescimoDoMesOutros(painelGestorVO.getAcrescimoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesOutros()));

			painelGestorVO.setReceitaComDescontoAcrescimoDoMesMatricula(painelGestorVO.getReceitaComDescontoAcrescimoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesMatricula()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesMaterialDidatico(painelGestorVO.getReceitaComDescontoAcrescimoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesMaterialDidatico()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesMensalidade(painelGestorVO.getReceitaComDescontoAcrescimoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesMensalidade()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesRequerimento(painelGestorVO.getReceitaComDescontoAcrescimoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesRequerimento()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesBiblioteca(painelGestorVO.getReceitaComDescontoAcrescimoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesBiblioteca()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(painelGestorVO.getReceitaComDescontoAcrescimoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesDevolucaoCheque()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesNegociacao(painelGestorVO.getReceitaComDescontoAcrescimoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesNegociacao()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesBolsaCusteada(painelGestorVO.getReceitaComDescontoAcrescimoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesBolsaCusteada()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesInscricao(painelGestorVO.getReceitaComDescontoAcrescimoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesInscricao()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesContratoReceita(painelGestorVO.getReceitaComDescontoAcrescimoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesContratoReceita()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(painelGestorVO.getReceitaComDescontoAcrescimoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesInclusaoReposicao()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesOutros(painelGestorVO.getReceitaComDescontoAcrescimoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesOutros()));

			painelGestorVO.setValorRecebidoDoMesMatricula(painelGestorVO.getValorRecebidoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesMatricula()));
			painelGestorVO.setValorRecebidoDoMesMaterialDidatico(painelGestorVO.getValorRecebidoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesMaterialDidatico()));
			painelGestorVO.setValorRecebidoDoMesMensalidade(painelGestorVO.getValorRecebidoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesMensalidade()));
			painelGestorVO.setValorRecebidoDoMesRequerimento(painelGestorVO.getValorRecebidoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesRequerimento()));
			painelGestorVO.setValorRecebidoDoMesBiblioteca(painelGestorVO.getValorRecebidoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesBiblioteca()));
			painelGestorVO.setValorRecebidoDoMesDevolucaoCheque(painelGestorVO.getValorRecebidoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesDevolucaoCheque()));
			painelGestorVO.setValorRecebidoDoMesNegociacao(painelGestorVO.getValorRecebidoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesNegociacao()));
			painelGestorVO.setValorRecebidoDoMesBolsaCusteada(painelGestorVO.getValorRecebidoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesBolsaCusteada()));
			painelGestorVO.setValorRecebidoDoMesInscricao(painelGestorVO.getValorRecebidoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesInscricao()));
			painelGestorVO.setValorRecebidoDoMesContratoReceita(painelGestorVO.getValorRecebidoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesContratoReceita()));
			painelGestorVO.setValorRecebidoDoMesInclusaoReposicao(painelGestorVO.getValorRecebidoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesInclusaoReposicao()));
			painelGestorVO.setValorRecebidoDoMesOutros(painelGestorVO.getValorRecebidoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesOutros()));

			painelGestorVO.setSaldoReceberDoMesMatricula(painelGestorVO.getSaldoReceberDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesMatricula()));
			painelGestorVO.setSaldoReceberDoMesMaterialDidatico(painelGestorVO.getSaldoReceberDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesMaterialDidatico()));
			painelGestorVO.setSaldoReceberDoMesMensalidade(painelGestorVO.getSaldoReceberDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesMensalidade()));
			painelGestorVO.setSaldoReceberDoMesRequerimento(painelGestorVO.getSaldoReceberDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesRequerimento()));
			painelGestorVO.setSaldoReceberDoMesBiblioteca(painelGestorVO.getSaldoReceberDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesBiblioteca()));
			painelGestorVO.setSaldoReceberDoMesDevolucaoCheque(painelGestorVO.getSaldoReceberDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesDevolucaoCheque()));
			painelGestorVO.setSaldoReceberDoMesNegociacao(painelGestorVO.getSaldoReceberDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesNegociacao()));
			painelGestorVO.setSaldoReceberDoMesBolsaCusteada(painelGestorVO.getSaldoReceberDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesBolsaCusteada()));
			painelGestorVO.setSaldoReceberDoMesInscricao(painelGestorVO.getSaldoReceberDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesInscricao()));
			painelGestorVO.setSaldoReceberDoMesContratoReceita(painelGestorVO.getSaldoReceberDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesContratoReceita()));
			painelGestorVO.setSaldoReceberDoMesInclusaoReposicao(painelGestorVO.getSaldoReceberDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesInclusaoReposicao()));
			painelGestorVO.setSaldoReceberDoMesOutros(painelGestorVO.getSaldoReceberDoMesOutros().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesOutros()));

			painelGestorVO.setTotalVencidoDoMes(painelGestorVO.getTotalVencidoDoMes().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMes()));
			painelGestorVO.setTotalVencidoDoMesMatricula(painelGestorVO.getTotalVencidoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesMatricula()));
			painelGestorVO.setTotalVencidoDoMesMaterialDidatico(painelGestorVO.getTotalVencidoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesMaterialDidatico()));
			painelGestorVO.setTotalVencidoDoMesMensalidade(painelGestorVO.getTotalVencidoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesMensalidade()));
			painelGestorVO.setTotalVencidoDoMesRequerimento(painelGestorVO.getTotalVencidoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesRequerimento()));
			painelGestorVO.setTotalVencidoDoMesBiblioteca(painelGestorVO.getTotalVencidoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesBiblioteca()));
			painelGestorVO.setTotalVencidoDoMesDevolucaoCheque(painelGestorVO.getTotalVencidoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesDevolucaoCheque()));
			painelGestorVO.setTotalVencidoDoMesNegociacao(painelGestorVO.getTotalVencidoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesNegociacao()));
			painelGestorVO.setTotalVencidoDoMesBolsaCusteada(painelGestorVO.getTotalVencidoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesBolsaCusteada()));
			painelGestorVO.setTotalVencidoDoMesInscricao(painelGestorVO.getTotalVencidoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesInscricao()));
			painelGestorVO.setTotalVencidoDoMesContratoReceita(painelGestorVO.getTotalVencidoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesContratoReceita()));
			painelGestorVO.setTotalVencidoDoMesInclusaoReposicao(painelGestorVO.getTotalVencidoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesInclusaoReposicao()));
			painelGestorVO.setTotalVencidoDoMesOutros(painelGestorVO.getTotalVencidoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesOutros()));

		}
		if (painelGestorVO.getTotalVencidoDoMes().doubleValue() > 0) {
			painelGestorVO.setMediaInadimplenciaDoPeriodo((painelGestorVO.getTotalVencidoDoMes().doubleValue() * 100) / painelGestorVO.getTotalReceitaComDescontoAcrescimoDoMes().doubleValue());
		} else {
			painelGestorVO.setMediaInadimplenciaDoPeriodo(0.0);
		}
		if (painelGestorVO.getTotalVencidoDoMes().doubleValue() > 0) {
			painelGestorVO.setMediaInadimplenciaDoPeriodoSemAcrescimo(((painelGestorVO.getTotalVencidoDoMes().doubleValue() - painelGestorVO.getTotalJuroMultaDoMes().doubleValue()) * 100) / painelGestorVO.getTotalReceitaComDescontoAcrescimoDoMes().doubleValue());
		} else {
			painelGestorVO.setMediaInadimplenciaDoPeriodoSemAcrescimo(0.0);
		}
	}

	public void executarCalculoMediaInadimplenciaPeridoPainelGestorFinanceiroTurma(PainelGestorVO painelGestorVO) {
		Double taxaDoMes = 0.0;
		for (PainelGestorContaReceberMesAnoVO painelGestorContaReceberMesAnoVO : painelGestorVO.getPainelGestorContaReceberMesAnoTurmaVOs()) {
			painelGestorContaReceberMesAnoVO.getContaReceberPainelGestorVOs().clear();
			if (painelGestorContaReceberMesAnoVO.getTaxaInadimplenciaDoMes() > 0) {
				taxaDoMes = taxaDoMes + painelGestorContaReceberMesAnoVO.getTaxaInadimplenciaDoMes();
			}
			painelGestorVO.setTotalReceitaDoMes(painelGestorVO.getTotalReceitaDoMes().add(painelGestorContaReceberMesAnoVO.getReceitaDoMes()));
			painelGestorVO.setTotalAcrescimoDoMes(painelGestorVO.getTotalAcrescimoDoMes().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMes()));
			painelGestorVO.setTotalDescontoDoMes(painelGestorVO.getTotalDescontoDoMes().add(painelGestorContaReceberMesAnoVO.getDescontoDoMes()));
			painelGestorVO.setTotalReceitaComDescontoAcrescimoDoMes(painelGestorVO.getTotalReceitaComDescontoAcrescimoDoMes().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMes()));
			painelGestorVO.setTotalSaldoAReceberDoMes(painelGestorVO.getTotalSaldoAReceberDoMes().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMes()));
			painelGestorVO.setTotalRecebidoDoMes(painelGestorVO.getTotalRecebidoDoMes().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMes()));

			painelGestorVO.setReceitaDoMesMatricula(painelGestorVO.getReceitaDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesMatricula()));
			painelGestorVO.setReceitaDoMesMaterialDidatico(painelGestorVO.getReceitaDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesMaterialDidatico()));
			painelGestorVO.setReceitaDoMesMensalidade(painelGestorVO.getReceitaDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesMensalidade()));
			painelGestorVO.setReceitaDoMesRequerimento(painelGestorVO.getReceitaDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesRequerimento()));
			painelGestorVO.setReceitaDoMesBiblioteca(painelGestorVO.getReceitaDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesBiblioteca()));
			painelGestorVO.setReceitaDoMesDevolucaoCheque(painelGestorVO.getReceitaDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesDevolucaoCheque()));
			painelGestorVO.setReceitaDoMesNegociacao(painelGestorVO.getReceitaDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesNegociacao()));
			painelGestorVO.setReceitaDoMesBolsaCusteada(painelGestorVO.getReceitaDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesBolsaCusteada()));
			painelGestorVO.setReceitaDoMesInscricao(painelGestorVO.getReceitaDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesInscricao()));
			painelGestorVO.setReceitaDoMesContratoReceita(painelGestorVO.getReceitaDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesContratoReceita()));
			painelGestorVO.setReceitaDoMesInclusaoReposicao(painelGestorVO.getReceitaDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesInclusaoReposicao()));
			painelGestorVO.setReceitaDoMesOutros(painelGestorVO.getReceitaDoMesOutros().add(painelGestorContaReceberMesAnoVO.getReceitaDoMesOutros()));

			painelGestorVO.setDescontoDoMesMatricula(painelGestorVO.getDescontoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesMatricula()));
			painelGestorVO.setDescontoDoMesMaterialDidatico(painelGestorVO.getDescontoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesMatricula()));
			painelGestorVO.setDescontoDoMesMensalidade(painelGestorVO.getDescontoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesMensalidade()));
			painelGestorVO.setDescontoDoMesRequerimento(painelGestorVO.getDescontoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesRequerimento()));
			painelGestorVO.setDescontoDoMesBiblioteca(painelGestorVO.getDescontoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesBiblioteca()));
			painelGestorVO.setDescontoDoMesDevolucaoCheque(painelGestorVO.getDescontoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesDevolucaoCheque()));
			painelGestorVO.setDescontoDoMesNegociacao(painelGestorVO.getDescontoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesNegociacao()));
			painelGestorVO.setDescontoDoMesBolsaCusteada(painelGestorVO.getDescontoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesBolsaCusteada()));
			painelGestorVO.setDescontoDoMesInscricao(painelGestorVO.getDescontoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesInscricao()));
			painelGestorVO.setDescontoDoMesContratoReceita(painelGestorVO.getDescontoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesContratoReceita()));
			painelGestorVO.setDescontoDoMesInclusaoReposicao(painelGestorVO.getDescontoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesInclusaoReposicao()));
			painelGestorVO.setDescontoDoMesOutros(painelGestorVO.getDescontoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getDescontoDoMesOutros()));

			painelGestorVO.setAcrescimoDoMesMatricula(painelGestorVO.getAcrescimoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesMatricula()));
			painelGestorVO.setAcrescimoDoMesMaterialDidatico(painelGestorVO.getAcrescimoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesMaterialDidatico()));
			painelGestorVO.setAcrescimoDoMesMensalidade(painelGestorVO.getAcrescimoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesMensalidade()));
			painelGestorVO.setAcrescimoDoMesRequerimento(painelGestorVO.getAcrescimoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesRequerimento()));
			painelGestorVO.setAcrescimoDoMesBiblioteca(painelGestorVO.getAcrescimoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesBiblioteca()));
			painelGestorVO.setAcrescimoDoMesDevolucaoCheque(painelGestorVO.getAcrescimoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesDevolucaoCheque()));
			painelGestorVO.setAcrescimoDoMesNegociacao(painelGestorVO.getAcrescimoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesNegociacao()));
			painelGestorVO.setAcrescimoDoMesBolsaCusteada(painelGestorVO.getAcrescimoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesBolsaCusteada()));
			painelGestorVO.setAcrescimoDoMesInscricao(painelGestorVO.getAcrescimoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesInscricao()));
			painelGestorVO.setAcrescimoDoMesContratoReceita(painelGestorVO.getAcrescimoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesContratoReceita()));
			painelGestorVO.setAcrescimoDoMesInclusaoReposicao(painelGestorVO.getAcrescimoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesInclusaoReposicao()));
			painelGestorVO.setAcrescimoDoMesOutros(painelGestorVO.getAcrescimoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getAcrescimoDoMesOutros()));

			painelGestorVO.setReceitaComDescontoAcrescimoDoMesMatricula(painelGestorVO.getReceitaComDescontoAcrescimoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesMatricula()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesMaterialDidatico(painelGestorVO.getReceitaComDescontoAcrescimoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesMaterialDidatico()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesMensalidade(painelGestorVO.getReceitaComDescontoAcrescimoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesMensalidade()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesRequerimento(painelGestorVO.getReceitaComDescontoAcrescimoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesRequerimento()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesBiblioteca(painelGestorVO.getReceitaComDescontoAcrescimoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesBiblioteca()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(painelGestorVO.getReceitaComDescontoAcrescimoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesDevolucaoCheque()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesNegociacao(painelGestorVO.getReceitaComDescontoAcrescimoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesNegociacao()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesBolsaCusteada(painelGestorVO.getReceitaComDescontoAcrescimoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesBolsaCusteada()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesInscricao(painelGestorVO.getReceitaComDescontoAcrescimoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesInscricao()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesContratoReceita(painelGestorVO.getReceitaComDescontoAcrescimoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesContratoReceita()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(painelGestorVO.getReceitaComDescontoAcrescimoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesInclusaoReposicao()));
			painelGestorVO.setReceitaComDescontoAcrescimoDoMesOutros(painelGestorVO.getReceitaComDescontoAcrescimoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getReceitaComDescontoAcrescimoDoMesOutros()));

			painelGestorVO.setValorRecebidoDoMesMatricula(painelGestorVO.getValorRecebidoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesMatricula()));
			painelGestorVO.setValorRecebidoDoMesMaterialDidatico(painelGestorVO.getValorRecebidoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesMaterialDidatico()));
			painelGestorVO.setValorRecebidoDoMesMensalidade(painelGestorVO.getValorRecebidoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesMensalidade()));
			painelGestorVO.setValorRecebidoDoMesRequerimento(painelGestorVO.getValorRecebidoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesRequerimento()));
			painelGestorVO.setValorRecebidoDoMesBiblioteca(painelGestorVO.getValorRecebidoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesBiblioteca()));
			painelGestorVO.setValorRecebidoDoMesDevolucaoCheque(painelGestorVO.getValorRecebidoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesDevolucaoCheque()));
			painelGestorVO.setValorRecebidoDoMesNegociacao(painelGestorVO.getValorRecebidoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesNegociacao()));
			painelGestorVO.setValorRecebidoDoMesBolsaCusteada(painelGestorVO.getValorRecebidoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesBolsaCusteada()));
			painelGestorVO.setValorRecebidoDoMesInscricao(painelGestorVO.getValorRecebidoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesInscricao()));
			painelGestorVO.setValorRecebidoDoMesContratoReceita(painelGestorVO.getValorRecebidoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesContratoReceita()));
			painelGestorVO.setValorRecebidoDoMesInclusaoReposicao(painelGestorVO.getValorRecebidoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesInclusaoReposicao()));
			painelGestorVO.setValorRecebidoDoMesOutros(painelGestorVO.getValorRecebidoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getValorRecebidoDoMesOutros()));

			painelGestorVO.setSaldoReceberDoMesMatricula(painelGestorVO.getSaldoReceberDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesMatricula()));
			painelGestorVO.setSaldoReceberDoMesMaterialDidatico(painelGestorVO.getSaldoReceberDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesMaterialDidatico()));
			painelGestorVO.setSaldoReceberDoMesMensalidade(painelGestorVO.getSaldoReceberDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesMensalidade()));
			painelGestorVO.setSaldoReceberDoMesRequerimento(painelGestorVO.getSaldoReceberDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesRequerimento()));
			painelGestorVO.setSaldoReceberDoMesBiblioteca(painelGestorVO.getSaldoReceberDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesBiblioteca()));
			painelGestorVO.setSaldoReceberDoMesDevolucaoCheque(painelGestorVO.getSaldoReceberDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesDevolucaoCheque()));
			painelGestorVO.setSaldoReceberDoMesNegociacao(painelGestorVO.getSaldoReceberDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesNegociacao()));
			painelGestorVO.setSaldoReceberDoMesBolsaCusteada(painelGestorVO.getSaldoReceberDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesBolsaCusteada()));
			painelGestorVO.setSaldoReceberDoMesInscricao(painelGestorVO.getSaldoReceberDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesInscricao()));
			painelGestorVO.setSaldoReceberDoMesContratoReceita(painelGestorVO.getSaldoReceberDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesContratoReceita()));
			painelGestorVO.setSaldoReceberDoMesInclusaoReposicao(painelGestorVO.getSaldoReceberDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesInclusaoReposicao()));
			painelGestorVO.setSaldoReceberDoMesOutros(painelGestorVO.getSaldoReceberDoMesOutros().add(painelGestorContaReceberMesAnoVO.getSaldoReceberDoMesOutros()));

			painelGestorVO.setTotalVencidoDoMes(painelGestorVO.getTotalVencidoDoMes().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMes()));
			painelGestorVO.setTotalVencidoDoMesMatricula(painelGestorVO.getTotalVencidoDoMesMatricula().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesMatricula()));
			painelGestorVO.setTotalVencidoDoMesMaterialDidatico(painelGestorVO.getTotalVencidoDoMesMaterialDidatico().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesMaterialDidatico()));
			painelGestorVO.setTotalVencidoDoMesMensalidade(painelGestorVO.getTotalVencidoDoMesMensalidade().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesMensalidade()));
			painelGestorVO.setTotalVencidoDoMesRequerimento(painelGestorVO.getTotalVencidoDoMesRequerimento().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesRequerimento()));
			painelGestorVO.setTotalVencidoDoMesBiblioteca(painelGestorVO.getTotalVencidoDoMesBiblioteca().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesBiblioteca()));
			painelGestorVO.setTotalVencidoDoMesDevolucaoCheque(painelGestorVO.getTotalVencidoDoMesDevolucaoCheque().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesDevolucaoCheque()));
			painelGestorVO.setTotalVencidoDoMesNegociacao(painelGestorVO.getTotalVencidoDoMesNegociacao().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesNegociacao()));
			painelGestorVO.setTotalVencidoDoMesBolsaCusteada(painelGestorVO.getTotalVencidoDoMesBolsaCusteada().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesBolsaCusteada()));
			painelGestorVO.setTotalVencidoDoMesInscricao(painelGestorVO.getTotalVencidoDoMesInscricao().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesInscricao()));
			painelGestorVO.setTotalVencidoDoMesContratoReceita(painelGestorVO.getTotalVencidoDoMesContratoReceita().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesContratoReceita()));
			painelGestorVO.setTotalVencidoDoMesInclusaoReposicao(painelGestorVO.getTotalVencidoDoMesInclusaoReposicao().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesInclusaoReposicao()));
			painelGestorVO.setTotalVencidoDoMesOutros(painelGestorVO.getTotalVencidoDoMesOutros().add(painelGestorContaReceberMesAnoVO.getTotalVencidoDoMesOutros()));

		}
		if (painelGestorVO.getPainelGestorContaReceberMesAnoVOs().size() > 0) {
			painelGestorVO.setMediaInadimplenciaDoPeriodo(taxaDoMes / painelGestorVO.getPainelGestorContaReceberMesAnoVOs().size());
		} else {
			painelGestorVO.setMediaInadimplenciaDoPeriodo(0.0);
		}
		taxaDoMes = null;
	}

	public Double consultarValorReceberAntesDoPeriodo(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio) throws Exception {
		StringBuilder sb = new StringBuilder(" select sum(case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end) as valorReceber from contaReceber where situacao = 'AR'");
		sb.append(" and datavencimento<'").append(Uteis.getDataJDBC(dataInicio)).append(" 23:59:59'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (dadosSQL.next()) {
			return dadosSQL.getDouble("valorReceber");
		}
		return 0.0;
	}

	/**
	 * Realiza as definições gerais para a geração dos filtros de buscas
	 * 
	 * @param painelGestorVO
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @param tipoNivelEducacional
	 * @param mesAno
	 * @throws Exception
	 */
	public void executarInicializacaoDadosMapaReceitaPorTurma(PainelGestorVO painelGestorVO, String tipoMapaReceita, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, String filtrarPeriodoPor) throws Exception {
		if (!mesAno.isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		painelGestorVO.setCodigoCurso(codigoCurso);
		painelGestorVO.getPainelGestorPorNivelEducacionalVO().getPainelGestorContaReceberMesAnoTurmaVOs().clear();
		if (tipoMapaReceita.equals("COMPETENCIA")) {
			
			consultarDadosMapaReceitaPorTurma(painelGestorVO.getPainelGestorPorNivelEducacionalVO(), unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional, configuracaoFinanceiroVO, codigoCurso, filtrarPeriodoPor);
		} else {
			consultarMapaReceitaPorFluxoCaixaPainelGestorFinanceiroPorTurma(painelGestorVO.getPainelGestorPorNivelEducacionalVO(), unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional, configuracaoFinanceiroVO, codigoCurso);
		}
	}

	public void executarInicializacaoDadosMapaReceitaPorNivelEducacional(PainelGestorVO painelGestorVO, String tipoMapaReceita, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, String filtrarPeriodoPor) throws Exception {
		if (!mesAno.isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setPainelGestorPorNivelEducacionalVO(new PainelGestorVO());
		painelGestorVO.getPainelGestorPorNivelEducacionalVO().setTipoNivelEducacional(tipoNivelEducacional);
		if (tipoMapaReceita.equals("COMPETENCIA")) {
			consultarMapaReceitaPorCompetenciaPainelGestorFinanceiroPorNivelEducacional(painelGestorVO.getPainelGestorPorNivelEducacionalVO(), unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional, mesAno, configuracaoFinanceiroVO, filtrarPeriodoPor);
		} else {
			consultarMapaReceitaPorFluxoCaixaPainelGestorFinanceiroPorNivelEducacional(painelGestorVO.getPainelGestorPorNivelEducacionalVO(), unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional, mesAno, configuracaoFinanceiroVO);
		}
		// consultarDadosMapaReceitaPorNivelEducacional(painelGestorVO.getPainelGestorPorNivelEducacionalVO(),
		// unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional,
		// configuracaoFinanceiroVO);
	}

	public void consultarDadosMapaReceitaPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(" select codigoCurso, nome, nivelEducacional, sum(valor::NUMERIC(20,2)) as valorProvisaoReceber, sum(provisaoReceberNoMesPrimeiroDescontoMatriculaMensalidade::NUMERIC(20,2)) as provisaoReceberNoMesPrimeiroDescontoMatriculaMensalidade, sum(provisaoReceberNoMesPrimeiroDescontoOutras::NUMERIC(20,2)) as provisaoReceberNoMesPrimeiroDescontoOutras,");
		sb.append(" sum(valorRecebidoDoMes::NUMERIC(20,2)) as valorRecebidoDoMes,sum(valorRecebidoNoMes::NUMERIC(20,2)) as valorRecebidoNoMes, ");
		sb.append(" sum(valorRecebidoDoMesNoMes::NUMERIC(20,2)) as valorRecebidoDoMesNoMes, ");
		sb.append(" sum(valorAdiantadoMes::NUMERIC(20,2)) as valorAdiantadoMes, sum(valorAtrazadoMes::NUMERIC(20,2)) as valorAtrazadoMes,");
		sb.append(" sum(saldoReceberPrimeiroDescontoMatriculaMensalidade::NUMERIC(20,2)) as saldoReceberPrimeiroDescontoMatriculaMensalidade, sum(saldoReceberPrimeiroDescontoOutras::NUMERIC(20,2)) as saldoReceberPrimeiroDescontoOutras, ");
		sb.append(" sum(saldoReceberValorCheio::NUMERIC(20,2)) as saldoReceberValorCheio,");
		sb.append(" sum(saldoReceberValorCheioMatricula) as saldoReceberValorCheioMatricula, ");
		sb.append(" sum(saldoReceberValorCheioMaterialDidatico) as saldoReceberValorCheioMaterialDidatico, ");
		sb.append(" sum(saldoReceberValorCheioMensalidade) as saldoReceberValorCheioMensalidade, ");
		sb.append(" sum(saldoReceberValorCheioRequerimento) as saldoReceberValorCheioRequerimento, ");
		sb.append(" sum(saldoReceberValorCheioBiblioteca) as saldoReceberValorCheioBiblioteca, ");
		sb.append(" sum(saldoReceberValorCheioDevolucaoCheque) as saldoReceberValorCheioDevolucaoCheque, ");
		sb.append(" sum(saldoReceberValorCheioNegociacao) as saldoReceberValorCheioNegociacao, ");
		sb.append(" sum(saldoReceberValorCheioBolsaCusteada) as saldoReceberValorCheioBolsaCusteada, ");
		sb.append(" sum(saldoReceberValorCheioInscricao) as saldoReceberValorCheioInscricao, ");
		sb.append(" sum(saldoReceberValorCheioContratoReceita) as saldoReceberValorCheioContratoReceita, ");
		sb.append(" sum(saldoReceberValorCheioInclusaoReposicao) as saldoReceberValorCheioInclusaoReposicao, ");
		sb.append(" sum(saldoReceberValorCheioOutros)  as saldoReceberValorCheioOutros, ");

		sb.append(" sum(totalVencidoNoMes::NUMERIC(20,2)) as totalVencidoNoMes, ");
		sb.append(" sum(totalVencidoNoMesMatricula) as totalVencidoNoMesMatricula, ");
		sb.append(" sum(totalVencidoNoMesMaterialDidatico) as totalVencidoNoMesMaterialDidatico, ");
		sb.append(" sum(totalVencidoNoMesMensalidade) as totalVencidoNoMesMensalidade, ");
		sb.append(" sum(totalVencidoNoMesRequerimento) as totalVencidoNoMesRequerimento, ");
		sb.append(" sum(totalVencidoNoMesBiblioteca) as totalVencidoNoMesBiblioteca, ");
		sb.append(" sum(totalVencidoNoMesDevolucaoCheque) as totalVencidoNoMesDevolucaoCheque, ");
		sb.append(" sum(totalVencidoNoMesNegociacao) as totalVencidoNoMesNegociacao, ");
		sb.append(" sum(totalVencidoNoMesBolsaCusteada) as totalVencidoNoMesBolsaCusteada, ");
		sb.append(" sum(totalVencidoNoMesInscricao) as totalVencidoNoMesInscricao, ");
		sb.append(" sum(totalVencidoNoMesContratoReceita) as totalVencidoNoMesContratoReceita, ");
		sb.append(" sum(totalVencidoNoMesInclusaoReposicao) as totalVencidoNoMesInclusaoReposicao, ");
		sb.append(" sum(totalVencidoNoMesOutros)  as totalVencidoNoMesOutros, ");

		sb.append(" sum(case when tipoOrigem = 'MAT' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesMatricula, ");
		sb.append(" sum(case when tipoOrigem = 'MDI' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesMaterialDidatico, ");
		sb.append(" sum(case when tipoOrigem = 'MEN' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesMensalidade, ");
		sb.append(" sum(case when tipoOrigem = 'REQ' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesRequerimento, ");
		sb.append(" sum(case when tipoOrigem = 'BIB' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesBiblioteca, ");
		sb.append(" sum(case when tipoOrigem = 'DCH' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesDevolucaoCheque, ");
		sb.append(" sum(case when tipoOrigem = 'NCR' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesNegociacao, ");
		sb.append(" sum(case when tipoOrigem = 'BCC' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesBolsaCusteada, ");
		sb.append(" sum(case when tipoOrigem = 'IPS' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesInscricao, ");
		sb.append(" sum(case when tipoOrigem = 'CTR' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesContratoReceita, ");
		sb.append(" sum(case when tipoOrigem = 'IRE' then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesInclusaoReposicao, ");
		sb.append(" sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then valor::NUMERIC(20,2) else 0.0 end) as provisaoReceberMesOutros ");
		sb.append(" from (");
		sb.append(" select contareceber.tipoorigem, curso.codigo as codigoCurso, curso.nome,curso.nivelEducacional,contareceber.codigo, contareceber.valor::NUMERIC(20,2) as valor,");
		sb.append(" case when (valordescontocalculadoprimeirafaixadescontos is not null and valordescontocalculadoprimeirafaixadescontos > 0.0 and tipoorigem  in ('MAT', 'MEN')  ) ");
		sb.append(" then valordescontocalculadoprimeirafaixadescontos::NUMERIC(20,2) else 0.0 end as provisaoReceberNoMesPrimeiroDescontoMatriculaMensalidade, ");
		sb.append(" case when (valordescontocalculadoprimeirafaixadescontos is not null and valordescontocalculadoprimeirafaixadescontos > 0.0 and tipoorigem not in ('MAT', 'MEN')  ) ");
		sb.append(" then valordescontocalculadoprimeirafaixadescontos::NUMERIC(20,2) else 0.0 end as provisaoReceberNoMesPrimeiroDescontoOutras, ");

		sb.append(" case when  ( valorRecebido is not null) ");
		sb.append(" then valorRecebido::NUMERIC(20,2) else 0.0 end  as valorRecebidoDoMes,  ");
		sb.append(" case when ( extract(month from negociacaorecebimento.data) = extract(month from dataVencimento) and extract(year from negociacaorecebimento.data) = extract(year from dataVencimento))  ");
		sb.append(" then contareceber.valorRecebido::NUMERIC(20,2) else 0.0 end as valorRecebidoNoMes,  ");
		sb.append(" case when ( extract(month from negociacaorecebimento.data) = extract(month from dataVencimento) and extract(year from negociacaorecebimento.data) = extract(year from dataVencimento))  ");
		sb.append(" then contareceber.valorRecebido::NUMERIC(20,2) else 0.0 end as valorRecebidoDoMesNoMes,  0.0 as valorAdiantadoMes, 0.0 as valorAtrazadoMes,");
		sb.append(" case when (datavencimento > current_date and contaReceber.situacao = 'AR') then case when (valordescontocalculadoprimeirafaixadescontos is not null and valordescontocalculadoprimeirafaixadescontos > 0.0  and tipoorigem not in ('MAT', 'MEN')) ");
		sb.append(" then valordescontocalculadoprimeirafaixadescontos::NUMERIC(20,2) else 0.0 end else 0.0 end as saldoReceberPrimeiroDescontoMatriculaMensalidade,");
		sb.append(" case when (datavencimento > current_date and contaReceber.situacao = 'AR') then case when (valordescontocalculadoprimeirafaixadescontos is not null and valordescontocalculadoprimeirafaixadescontos > 0.0 and tipoorigem not in ('MAT', 'MEN') ) ");
		sb.append(" then valordescontocalculadoprimeirafaixadescontos::NUMERIC(20,2) else 0.0 end else 0.0 end as saldoReceberPrimeiroDescontoOutras,");
		sb.append(" case when (contareceber.situacao = 'AR') then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end  else 0.0 end as saldoReceberValorCheio, ");
		sb.append(" (case when tipoOrigem = 'MAT' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioMatricula, ");
		sb.append(" (case when contareceber.tipoOrigem = 'MDI' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioMaterialDidatico, ");
		sb.append(" (case when contareceber.tipoOrigem = 'MEN' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioMensalidade, ");
		sb.append(" (case when contareceber.tipoOrigem = 'REQ' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioRequerimento, ");
		sb.append(" (case when contareceber.tipoOrigem = 'BIB' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioBiblioteca, ");
		sb.append(" (case when contareceber.tipoOrigem = 'DCH' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioDevolucaoCheque, ");
		sb.append(" (case when contareceber.tipoOrigem = 'NCR' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioNegociacao, ");
		sb.append(" (case when contareceber.tipoOrigem = 'BCC' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioBolsaCusteada, ");
		sb.append(" (case when contareceber.tipoOrigem = 'IPS' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioInscricao, ");
		sb.append(" (case when contareceber.tipoOrigem = 'CTR' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioContratoReceita, ");
		sb.append(" (case when contareceber.tipoOrigem = 'IRE' and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as saldoReceberValorCheioInclusaoReposicao, ");
		sb.append(" (case when contareceber.tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end)  as saldoReceberValorCheioOutros, ");

		sb.append(" case when (datavencimento < current_date and contareceber.situacao = 'AR') then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end  else 0.0 end as totalVencidoNoMes, ");
		sb.append(" (case when tipoOrigem = 'MAT' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesMatricula, ");
		sb.append(" (case when tipoOrigem = 'MDI' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesMaterialDidatico, ");
		sb.append(" (case when tipoOrigem = 'MEN' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesMensalidade, ");
		sb.append(" (case when tipoOrigem = 'REQ' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesRequerimento, ");
		sb.append(" (case when tipoOrigem = 'BIB' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesBiblioteca, ");
		sb.append(" (case when tipoOrigem = 'DCH' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesDevolucaoCheque, ");
		sb.append(" (case when tipoOrigem = 'NCR' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesNegociacao, ");
		sb.append(" (case when tipoOrigem = 'BCC' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesBolsaCusteada, ");
		sb.append(" (case when tipoOrigem = 'IPS' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesInscricao, ");
		sb.append(" (case when tipoOrigem = 'CTR' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesContratoReceita, ");
		sb.append(" (case when tipoOrigem = 'IRE' and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end) as totalVencidoNoMesInclusaoReposicao, ");
		sb.append(" (case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE')  and datavencimento < current_date and contareceber.situacao = 'AR' then case when contareceber.valorReceberCalculado is not null then contareceber.valorReceberCalculado else contareceber.valor::NUMERIC(20,2) end else 0.0 end)  as totalVencidoNoMesOutros ");

		sb.append(" from contaReceber ");
		sb.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sb.append(" left join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end ");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' and ");
		sb.append(" contaReceber.situacao not in ('NE', 'CF', 'RM') and datavencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and datavencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;
		for (int y = 0; y < numeroMes; y++) {
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" union all");
			sb.append(" select contareceber.tipoorigem, curso.codigo as codigoCurso, curso.nome,curso.nivelEducacional,contareceber.codigo, 0.0, 0.0, 0.0, 0.0, contareceber.valorRecebido::NUMERIC(20,2), 0.0,  ");
			sb.append(" case when (dataVencimento::DATE > negociacaorecebimento.data::DATE) then contareceber.valorRecebido::NUMERIC(20,2) else 0.0 end as valorAdiantadoMes,");
			sb.append(" case when (dataVencimento::DATE <= negociacaorecebimento.data::DATE) then contareceber.valorRecebido::NUMERIC(20,2)  else 0.0 end as valorAtrazadoMes, 0.0, 0.0, 0.0, 0.0, ");
			sb.append(" 0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0");
			sb.append(" from contareceber ");
			sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
			sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
			sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
			sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end ");
			sb.append(" inner join curso on curso.codigo = matricula.curso");
			sb.append(" where nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' and contaReceber.situacao = 'RE' and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append(" 00:00:00' ");
			sb.append(" and negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append(" 23:59:59' ");
			sb.append(" and ( contareceber.datavencimento < '").append(Uteis.getDataJDBC(dataInicioTmp)).append(" 00:00:00'  or contareceber.datavencimento > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append(" 23:59:59'  )");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		}

		sb.append(") as t group by codigoCurso, nome, nivelEducacional  order by nome");

		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorContaReceberMesAnoTurmaVOs().clear();
		painelGestorVO.setTotalRecebidoNoPeriodo(0.0);
		painelGestorVO.setTotalRecebidoDoPeriodo(0.0);
		painelGestorVO.setTotalProvisaoPeriodo(0.0);
		painelGestorVO.setTotalProvisaoPeriodoPrimeiroDesconto(0.0);
		painelGestorVO.setTotalProvisaoPeriodoPrimeiroDescontoMatriculaMensalidade(0.0);
		painelGestorVO.setTotalProvisaoPeriodoPrimeiroDescontoOutras(0.0);
		painelGestorVO.setSaldoReceberDoPeriodoPrimeiroDesconto(0.0);
		painelGestorVO.setSaldoReceberDoPeriodoPrimeiroDescontoMatriculaMensalidade(0.0);
		painelGestorVO.setSaldoReceberDoPeriodoPrimeiroDescontoOutras(0.0);
		painelGestorVO.setSaldoReceberPeriodo(0.0);
		painelGestorVO.setTotalRecebidoNoPeriodo(0.0);
		painelGestorVO.setProvisaoReceberMesBiblioteca(0.0);
		painelGestorVO.setProvisaoReceberMesBolsaCusteada(0.0);
		painelGestorVO.setProvisaoReceberMesContratoReceita(0.0);
		painelGestorVO.setProvisaoReceberMesDevolucaoCheque(0.0);
		painelGestorVO.setProvisaoReceberMesInclusaoReposicao(0.0);
		painelGestorVO.setProvisaoReceberMesInscricao(0.0);
		painelGestorVO.setProvisaoReceberMesMatricula(0.0);
		painelGestorVO.setProvisaoReceberMesMaterialDidatico(0.0);
		painelGestorVO.setProvisaoReceberMesMensalidade(0.0);
		painelGestorVO.setProvisaoReceberMesNegociacao(0.0);
		painelGestorVO.setProvisaoReceberMesRequerimento(0.0);
		painelGestorVO.setProvisaoReceberMesOutros(0.0);
		painelGestorVO.setSaldoReceberPeriodoContratoReceita(0.0);
		painelGestorVO.setSaldoReceberPeriodoInclusaoReposicao(0.0);
		painelGestorVO.setSaldoReceberPeriodoDevolucaoCheque(0.0);
		painelGestorVO.setSaldoReceberPeriodoInscricao(0.0);
		painelGestorVO.setSaldoReceberPeriodoMatricula(0.0);
		painelGestorVO.setSaldoReceberPeriodoMaterialDidatico(0.0);
		painelGestorVO.setSaldoReceberPeriodoMensalidade(0.0);
		painelGestorVO.setSaldoReceberPeriodoNegociacao(0.0);
		painelGestorVO.setSaldoReceberPeriodoOutros(0.0);
		painelGestorVO.setSaldoReceberPeriodoRequerimento(0.0);
		painelGestorVO.setSaldoReceberPeriodoBiblioteca(0.0);
		painelGestorVO.setSaldoReceberPeriodoBolsaCusteada(0.0);

		painelGestorVO.setSaldoReceberVencidoPeriodo(0.0);
		painelGestorVO.setSaldoReceberVencidoBiblioteca(0.0);
		painelGestorVO.setSaldoReceberVencidoBolsaCusteada(0.0);
		painelGestorVO.setSaldoReceberVencidoContratoReceita(0.0);
		painelGestorVO.setSaldoReceberVencidoDevolucaoCheque(0.0);
		painelGestorVO.setSaldoReceberVencidoInclusaoReposicao(0.0);
		painelGestorVO.setSaldoReceberVencidoInscricao(0.0);
		painelGestorVO.setSaldoReceberVencidoMatricula(0.0);
		painelGestorVO.setSaldoReceberVencidoMaterialDidatico(0.0);
		painelGestorVO.setSaldoReceberVencidoMensalidade(0.0);
		painelGestorVO.setSaldoReceberVencidoNegociacao(0.0);
		painelGestorVO.setSaldoReceberVencidoOutros(0.0);
		painelGestorVO.setSaldoReceberVencidoRequerimento(0.0);

		while (dadosSQL.next()) {
			montarDadosMapaReceitaPorCompetênciaPainelGestorFinanceiroContaReceberMesAno(painelGestorVO, true, dadosSQL);
		}

		executarCalculoMediaInadimplenciaMapaReceitaPorCompetenciaPeridoPainelGestorFinanceiro(painelGestorVO);
	}

	public void consultarDadosMapaReceitaPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer codigoCurso, String filtrarPeriodoPor) throws Exception {
		
		StringBuilder sb = new StringBuilder();

		sb.append(" select * from ( ");
		sb.append("  select receitaDoMes, ");
		sb.append("  case when (acrescimo > 0 and receitaDoMes = receitaComDescontoAcrescimoDoMes) then (acrescimo::NUMERIC(20,2) + descontoDoMes::NUMERIC(20,2)) else descontoDoMes::NUMERIC(20,2) end as descontoDoMes, ");
		sb.append("  case when (descontoDoMes > 0 and receitaDoMes = receitaComDescontoAcrescimoDoMes) then (descontoDoMes + acrescimo) else acrescimo end as acrescimo, ");
		sb.append("  receitaComDescontoAcrescimoDoMes, valorRecebidoDoMes, saldoReceberDoMes, ");

		sb.append("  descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, ");

		sb.append("  receitaDoMesMatricula, receitaDoMesMaterialDidatico, receitaDoMesMensalidade, receitaDoMesRequerimento, receitaDoMesBiblioteca, receitaDoMesDevolucaoCheque, ");
		sb.append("  receitaDoMesNegociacao, receitaDoMesBolsaCusteada, receitaDoMesInscricao,  receitaDoMesContratoReceita, receitaDoMesInclusaoReposicao, receitaDoMesOutros, ");

		sb.append("  descontoDoMesMatricula, descontoDoMesMaterialDidatico, descontoDoMesMensalidade, descontoDoMesRequerimento, descontoDoMesBiblioteca, descontoDoMesDevolucaoCheque, ");
		sb.append("  descontoDoMesNegociacao, descontoDoMesBolsaCusteada, descontoDoMesInscricao, descontoDoMesContratoReceita, descontoDoMesInclusaoReposicao, descontoDoMesOutros, ");

		sb.append("  acrescimoDoMesMatricula, acrescimoDoMesMaterialDidatico, acrescimoDoMesMensalidade, acrescimoDoMesRequerimento, acrescimoDoMesBiblioteca, acrescimoDoMesDevolucaoCheque, ");
		sb.append("  acrescimoDoMesNegociacao, acrescimoDoMesBolsaCusteada, acrescimoDoMesInscricao, acrescimoDoMesContratoReceita, acrescimoDoMesInclusaoReposicao, acrescimoDoMesOutros, ");

		sb.append("  receitaComDescontoAcrescimoDoMesMatricula, receitaComDescontoAcrescimoDoMesMaterialDidatico, receitaComDescontoAcrescimoDoMesMensalidade, receitaComDescontoAcrescimoDoMesRequerimento, receitaComDescontoAcrescimoDoMesBiblioteca, ");
		sb.append("  receitaComDescontoAcrescimoDoMesDevolucaoCheque, receitaComDescontoAcrescimoDoMesNegociacao, receitaComDescontoAcrescimoDoMesBolsaCusteada, receitaComDescontoAcrescimoDoMesInscricao, ");
		sb.append("  receitaComDescontoAcrescimoDoMesContratoReceita, receitaComDescontoAcrescimoDoMesInclusaoReposicao, receitaComDescontoAcrescimoDoMesOutros, ");

		sb.append("  valorRecebidoDoMesMatricula, valorRecebidoDoMesMaterialDidatico, valorRecebidoDoMesMensalidade, valorRecebidoDoMesRequerimento, valorRecebidoDoMesBiblioteca, valorRecebidoDoMesDevolucaoCheque, ");
		sb.append("  valorRecebidoDoMesNegociacao, valorRecebidoDoMesBolsaCusteada, valorRecebidoDoMesInscricao,  valorRecebidoDoMesContratoReceita, valorRecebidoDoMesInclusaoReposicao, valorRecebidoDoMesOutros, ");

		sb.append("  saldoReceberDoMesMatricula, saldoReceberDoMesMaterialDidatico, saldoReceberDoMesMensalidade, saldoReceberDoMesRequerimento, saldoReceberDoMesBiblioteca, saldoReceberDoMesDevolucaoCheque, ");
		sb.append("  saldoReceberDoMesNegociacao, saldoReceberDoMesBolsaCusteada, saldoReceberDoMesInscricao, saldoReceberDoMesContratoReceita, saldoReceberDoMesInclusaoReposicao, saldoReceberDoMesOutros, ");

		sb.append("  totalVencidoDoMes, totalVencidoDoMesMatricula, totalVencidoDoMesMaterialDidatico, totalVencidoDoMesMensalidade, totalVencidoDoMesRequerimento, totalVencidoDoMesBiblioteca, totalVencidoDoMesDevolucaoCheque, ");
		sb.append("  totalVencidoDoMesNegociacao, totalVencidoDoMesBolsaCusteada, totalVencidoDoMesInscricao, totalVencidoDoMesContratoReceita, totalVencidoDoMesInclusaoReposicao, totalVencidoDoMesOutros, ");

		sb.append("  codigoTurma, turma, niveleducacional ");
		sb.append("  from (");
		sb.append("  select sum(receitaDoMes::NUMERIC(20,2)) as receitaDoMes, ");
		sb.append("  sum(descontoDoMes::NUMERIC(20,2)) as descontoDoMes, sum(acrescimo::NUMERIC(20,2)) as acrescimo, sum(receitaComDescontoAcrescimoDoMes) as receitaComDescontoAcrescimoDoMes, ");
		sb.append("  sum(valorRecebidoDoMes::NUMERIC(20,2)) as valorRecebidoDoMes, sum(saldoReceberDoMes::NUMERIC(20,2)) as saldoReceberDoMes, ");

		sb.append("  sum(descontoconvenio) as descontoconvenio, sum(descontoinstituicao) as descontoinstituicao, sum(valorDescontoprogressivo) as valorDescontoprogressivo, ");
		sb.append("  sum(descontoaluno) as descontoaluno, sum(descontorecebimento) as descontorecebimento, sum(descontorateio) as descontorateio, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaDoMes::NUMERIC(20,2) else 0.0 end) as receitaDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then descontoDoMes::NUMERIC(20,2) else 0.0 end) as descontoDoMesOutros,  ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then acrescimo::NUMERIC(20,2) else 0.0 end) as acrescimoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then receitaComDescontoAcrescimoDoMes::NUMERIC(20,2) else 0.0 end) as receitaComDescontoAcrescimoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then valorRecebidoDoMes::NUMERIC(20,2) else 0.0 end) as valorRecebidoDoMesOutros, ");

		sb.append("  sum(case when tipoOrigem = 'MAT' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI','MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE') then saldoReceberDoMes::NUMERIC(20,2) else 0.0 end) as saldoReceberDoMesOutros, ");

		sb.append("  sum(case when (datavencimento < current_date and situacao = 'AR') then saldoReceberDoMes else 0.0 end) as totalVencidoDoMes, ");
		sb.append("  sum(case when tipoOrigem = 'MAT' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMatricula, ");
		sb.append("  sum(case when tipoOrigem = 'MDI' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMaterialDidatico, ");
		sb.append("  sum(case when tipoOrigem = 'MEN' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesMensalidade, ");
		sb.append("  sum(case when tipoOrigem = 'REQ' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesRequerimento, ");
		sb.append("  sum(case when tipoOrigem = 'BIB' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesBiblioteca, ");
		sb.append("  sum(case when tipoOrigem = 'DCH' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesDevolucaoCheque, ");
		sb.append("  sum(case when tipoOrigem = 'NCR' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesNegociacao, ");
		sb.append("  sum(case when tipoOrigem = 'BCC' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesBolsaCusteada, ");
		sb.append("  sum(case when tipoOrigem = 'IPS' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesInscricao, ");
		sb.append("  sum(case when tipoOrigem = 'CTR' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesContratoReceita, ");
		sb.append("  sum(case when tipoOrigem = 'IRE' and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end) as totalVencidoDoMesInclusaoReposicao, ");
		sb.append("  sum(case when tipoOrigem not in ('MAT', 'MDI', 'MEN', 'REQ', 'BIB', 'DCH', 'NCR', 'BCC', 'IPS', 'CTR', 'IRE')  and datavencimento < current_date and situacao = 'AR' then saldoReceberDoMes else 0.0 end)  as totalVencidoDoMesOutros, ");

		sb.append("  codigoTurma, turma, niveleducacional ");

		sb.append("  from (");
		sb.append("  select codigo, nossonumero, situacao, datavencimento, tipoorigem, valor::NUMERIC(20,2) as receitaDoMes, ");
		sb.append("  case when (situacao = 'AR') then sum(valorDescontoCalculado) else ");
		sb.append("  (sum(descontoconvenio) + sum(descontoinstituicao) + sum(valorDescontoprogressivo) + sum(descontoaluno) + sum(descontorecebimento) + sum(descontorateio))::numeric(20,2) end as descontoDoMes, ");
		sb.append("  case when (situacao = 'AR') then sum(acrescimo + juroCalculado + multaCalculado)::numeric(20,2) else sum(acrescimo + juro + multa)::numeric(20,2) end as acrescimo, ");
		sb.append("  (sum(valorRecebidoDoMes::NUMERIC(20,2)) + sum(saldoReceberDoMes::NUMERIC(20,2))) as receitaComDescontoAcrescimoDoMes, ");
		sb.append("  valorRecebidoDoMes::NUMERIC(20,2), saldoReceberDoMes::NUMERIC(20,2), descontoconvenio, descontoinstituicao, valorDescontoprogressivo, descontoaluno, descontorecebimento, descontorateio, codigoTurma, turma, niveleducacional ");
		sb.append("  from (");
		sb.append("  select distinct contareceber.tipoOrigem, contareceber.codigo, nossonumero, "+filtrarPeriodoPor+" as  datavencimento, contareceber.situacao, ");
		sb.append("  case when (contareceber.acrescimo > 0) then contareceber.acrescimo::numeric(20,2) else 0.0 end as acrescimo, ");
		sb.append("  case when (contareceber.juro > 0) then contareceber.juro::numeric(20,2) else 0.0 end as juro, ");
		sb.append("  case when (contareceber.multa > 0) then contareceber.multa::numeric(20,2) else 0.0 end as multa, ");
		sb.append("  case when (contareceber.valorJuroCalculado > 0) then contareceber.valorJuroCalculado::numeric(20,2) else 0.0 end as juroCalculado, ");
		sb.append("  case when (contareceber.valorMultaCalculado > 0) then contareceber.valorMultaCalculado::numeric(20,2) else 0.0 end as multaCalculado, ");
		sb.append("  contareceber.valor::NUMERIC(20,2) as valor, ");
		sb.append("  case when (descontoconvenio > 0) then descontoconvenio::numeric(20,2) else 0.0 end as descontoconvenio, ");
		sb.append("  case when (descontoinstituicao > 0) then descontoinstituicao::numeric(20,2) else 0.0 end as descontoinstituicao, ");
		sb.append("  case when (valordescontoprogressivo > 0) then valordescontoprogressivo::numeric(20,2) else 0.0 end as valordescontoprogressivo, ");
		sb.append("  case when valordescontoalunojacalculado > 0 then valordescontoalunojacalculado::numeric(20,2) else 0.0 end as descontoaluno, ");
		sb.append("  case when (valorcalculadodescontolancadorecebimento > 0) then valorcalculadodescontolancadorecebimento::numeric(20,2) else 0.0 end as descontorecebimento, ");
		sb.append("  valordescontorateio as descontorateio, ");
		sb.append("  case when (contareceber.situacao = 'AR' and valorDescontoCalculado > 0) then valorDescontoCalculado::NUMERIC(20,2) else 0.0	end valorDescontoCalculado, ");
		sb.append("  case when  ( valorRecebido is not null and contareceber.situacao = 'RE')  then valorRecebido::NUMERIC(20,2) else 0.0 end  as valorRecebidoDoMes,  ");
		sb.append("  case when (contareceber.situacao = 'AR') then contareceber.valorrecebercalculado::NUMERIC(20,2) else 0.0 end as saldoReceberDoMes, ");
		sb.append("  turma.codigo AS codigoTurma, turma.identificadorTurma AS turma, curso.niveleducacional ");
		sb.append("  from contaReceber ");
		sb.append("  left join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append("  inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula  and matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sb.append("  inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append("  inner join curso on curso.codigo = matricula.curso ");
		sb.append(" where  ");
		sb.append(" contaReceber.situacao not in ('NE', 'CF', 'RM') and "+filtrarPeriodoPor+" >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and "+filtrarPeriodoPor+" <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
		sb.append("  and curso.niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and curso.codigo = ").append(codigoCurso);
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(") as t group by codigoTurma, turma, niveleducacional, valor, situacao, valorRecebidoDoMes, tipoorigem, saldoReceberDoMes, descontoconvenio, descontoinstituicao, valorDescontoprogressivo, ");
		sb.append(" descontoaluno, descontorecebimento, descontorateio, nossonumero, datavencimento, codigo ");
		sb.append(" ) as t1 group by codigoTurma, turma, niveleducacional order by turma ");
		sb.append(" ) as t2 ");
		sb.append(" ) as t3 ");
		////System.out.println(sb.toString());
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		painelGestorVO.setTotalReceitaDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalAcrescimoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalDescontoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalReceitaComDescontoAcrescimoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalSaldoAReceberDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalRecebidoDoMes(BigDecimal.ZERO);

		painelGestorVO.setReceitaDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setDescontoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setDescontoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setAcrescimoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setAcrescimoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setReceitaComDescontoAcrescimoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setValorRecebidoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setValorRecebidoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setSaldoReceberDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setSaldoReceberDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setTotalVencidoDoMes(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMatricula(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMaterialDidatico(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesMensalidade(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesRequerimento(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesBiblioteca(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesDevolucaoCheque(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesNegociacao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesBolsaCusteada(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesInscricao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesContratoReceita(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesInclusaoReposicao(BigDecimal.ZERO);
		painelGestorVO.setTotalVencidoDoMesOutros(BigDecimal.ZERO);

		painelGestorVO.setMediaInadimplenciaDoPeriodo(0.0);
		painelGestorVO.setMediaInadimplenciaNoPeriodo(0.0);

		while (dadosSQL.next()) {
			montarDadosPainelGestorFinanceiroContaReceberMesAnoTurma(painelGestorVO, true, dadosSQL);
		}
		executarCalculoMediaInadimplenciaPeridoPainelGestorFinanceiroTurma(painelGestorVO);
	}

	/**
	 * Consulta os possíveis niveis educacionais da unidades do filtro de busca;
	 * 
	 * @param unidadeEnsinoVOs
	 * @return
	 */
	public List<TipoNivelEducacional> consultarNivelEducacionalPorUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		StringBuilder sb = new StringBuilder("select nivelEducacional from Curso inner join unidadeEnsinoCurso on unidadeEnsinoCurso.curso = curso.codigo ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "unidadeEnsinoCurso.unidadeensino"));
		sb.append(" group by nivelEducacional ");
		List<TipoNivelEducacional> tipoNivelEducacionals = new ArrayList<TipoNivelEducacional>();
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (dadosSQL.next()) {
			TipoNivelEducacional tipoNivelEducacional = TipoNivelEducacional.getEnum(dadosSQL.getString("nivelEducacional"));
			tipoNivelEducacionals.add(tipoNivelEducacional);
		}
		return tipoNivelEducacionals;
	}

	/**
	 * Consulta os dados para a Geração dos dados do Mapa de Despesas do Painel
	 * Gestor Financeiro
	 * 
	 * @param painelGestorVO
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @throws Exception
	 */
	public void consultarMapaDespesaPainelGestorFinanceiro(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(" select ano::VARCHAR as ano, mes::VARCHAR as mes, contapagarmes.valor, valorpagodomes, ");
		sb.append(" valorapagar, valorvencido, ");
		sb.append(" (select sum(contapagar.valorpago::NUMERIC(20,2)) from negociacaopagamento  ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" inner join contapagar on contapagarnegociacaopagamento.contapagar = contapagar.codigo   ");
		sb.append(" where extract(year from negociacaopagamento.data) = ano and extract(month from negociacaopagamento.data) = mes ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(") as valorpagonomes,  ");

		sb.append(" (select sum(contapagar.valorpago::NUMERIC(20,2)) from negociacaopagamento ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" inner join contapagar on contapagarnegociacaopagamento.contapagar = contapagar.codigo   ");
		sb.append(" where extract(year from negociacaopagamento.data) = ano and extract(month from negociacaopagamento.data) = mes ");
		sb.append(" and (extract(year from negociacaopagamento.data) > extract(year from contapagar.datavencimento) ");
		sb.append(" or (extract(year from negociacaopagamento.data) = extract(year from contapagar.datavencimento)  ");
		sb.append(" and extract(month from negociacaopagamento.data) > extract(month from contapagar.datavencimento)) ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" )) ");
		sb.append(" as valorPagoAtrazadoMes, ");

		sb.append(" (select sum(contapagar.valorpago::NUMERIC(20,2)) from negociacaopagamento ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" inner join contapagar on contapagarnegociacaopagamento.contapagar = contapagar.codigo   ");
		sb.append(" where extract(year from negociacaopagamento.data) = ano and extract(month from negociacaopagamento.data) = mes ");
		sb.append(" and (extract(year from negociacaopagamento.data) < extract(year from contapagar.datavencimento) ");
		sb.append(" or (extract(year from negociacaopagamento.data) = extract(year from contapagar.datavencimento)  ");
		sb.append(" and extract(month from negociacaopagamento.data) < extract(month from contapagar.datavencimento)) ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" )) ");
		sb.append(" as valorPagoAdiantadoMes, ");

		sb.append(" (select sum(contapagar.valorpago::NUMERIC(20,2)) from negociacaopagamento ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" inner join contapagar on contapagarnegociacaopagamento.contapagar = contapagar.codigo   ");
		sb.append(" where extract(year from negociacaopagamento.data) = ano and extract(month from negociacaopagamento.data) = mes ");
		sb.append(" and extract(year from negociacaopagamento.data) = extract(year from contapagar.datavencimento)  ");
		sb.append(" and extract(month from negociacaopagamento.data) = extract(month from contapagar.datavencimento) ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" ) ");
		sb.append(" as valorPagoDoMesNoMes ");

		sb.append(" from ( ");
		sb.append(" select ano, mes, sum(valor) as valor, sum(valorpagodomes) as valorpagodomes, ");
		sb.append(" sum(valorapagar) as valorapagar, ");
		sb.append(" sum(valorvencido) as valorvencido ");

		sb.append(" from ( ");
		sb.append(" select sum(valor::NUMERIC(20,2)) as valor,  ");
		sb.append(" case when situacao = 'PA'  then sum(valorpago::NUMERIC(20,2)) else 0.0 end as valorpagodomes, ");
		sb.append(" case when situacao = 'AP'  then sum(valor::NUMERIC(20,2)) else 0.0 end as valorapagar, ");
		sb.append(" case when situacao = 'AP' and datavencimento::DATE < current_date  then sum(valor::NUMERIC(20,2)) else 0.0 end as valorvencido, ");
		sb.append(" extract(month from datavencimento) as mes, extract(year from datavencimento) as ano  ");
		sb.append(" from contapagar  ");
		sb.append(" where datavencimento::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and datavencimento::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" group by contapagar.codigo, contapagar.datavencimento, contapagar.situacao ");

		sb.append(" ) as contapagargeral group by ano, mes ");

		sb.append(" ) as contapagarmes ");
		sb.append(" group by ano, mes, contapagarmes.valor, valorpagodomes, valorapagar, valorvencido ");
		sb.append(" order by ano::INT, mes::INT ");

		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorContaPagarMesAnoVOs().clear();
		painelGestorVO.setTotalPagarAnteriorPeriodo(consultarValorPagarAntesDoPeriodo(unidadeEnsinoVOs, dataInicio));
		while (dadosSQL.next()) {
			montarDadosPainelGestorFinanceiroContaPagarMesAno(painelGestorVO, dadosSQL);
		}
		executarCalculoTotalFinalMapaDespesa(painelGestorVO);
	}

	/**
	 * Realiza os calculos finais do Mapa de Despesa do Painel Gestor Financeiro
	 * totalizando os seguintes dados; 1 - Saldo a Pagar do Periodo 2 - Total
	 * pago do Periodo; 3 - Total pago no Periodo; 4 - Provisão a Pagar do
	 * Periodo; 5 - Media de Inadimplencia do Periodo; 6 - Media de
	 * Inadimplencia No Periodo;
	 * 
	 * @param painelGestorVO
	 */
	public void executarCalculoTotalFinalMapaDespesa(PainelGestorVO painelGestorVO) {

		painelGestorVO.setMediaInadimplenciaDespesaDoPeriodo(0.0);
		painelGestorVO.setMediaInadimplenciaDespesaNoPeriodo(0.0);
		painelGestorVO.setSaldoPagarDoPeriodo(0.0);
		painelGestorVO.setTotalPagoDoPeriodo(0.0);
		painelGestorVO.setTotalPagoNoPeriodo(0.0);
		painelGestorVO.setTotalProvisaoContaPagarPeriodo(0.0);
		Double taxaDoMes = 0.0;
		Double taxaNoMes = 0.0;
		for (PainelGestorContaPagarMesAnoVO painelGestorContaPagarMesAnoVO : painelGestorVO.getPainelGestorContaPagarMesAnoVOs()) {
			painelGestorVO.setSaldoPagarDoPeriodo(painelGestorVO.getSaldoPagarDoPeriodo() + painelGestorContaPagarMesAnoVO.getSaldoPagarMes());
			painelGestorVO.setTotalPagoDoPeriodo(painelGestorVO.getTotalPagoDoPeriodo() + painelGestorContaPagarMesAnoVO.getPagoDoMes());
			painelGestorVO.setTotalPagoNoPeriodo(painelGestorVO.getTotalPagoNoPeriodo() + painelGestorContaPagarMesAnoVO.getPagoNoMes());
			painelGestorVO.setTotalProvisaoContaPagarPeriodo(painelGestorVO.getTotalProvisaoContaPagarPeriodo() + painelGestorContaPagarMesAnoVO.getProvisaoPagarMes());
			taxaDoMes += painelGestorContaPagarMesAnoVO.getTaxaInadimplenciaDoMes();
			taxaNoMes += painelGestorContaPagarMesAnoVO.getTaxaInadimplenciaNoMes();
		}
		if (painelGestorVO.getPainelGestorContaPagarMesAnoVOs().size() > 0) {
			painelGestorVO.setMediaInadimplenciaDespesaDoPeriodo(taxaDoMes / painelGestorVO.getPainelGestorContaPagarMesAnoVOs().size());
			painelGestorVO.setMediaInadimplenciaDespesaNoPeriodo(taxaNoMes / painelGestorVO.getPainelGestorContaPagarMesAnoVOs().size());
		}
		taxaDoMes = null;
		taxaNoMes = null;
	}

	public void montarDadosPainelGestorFinanceiroContaPagarMesAno(PainelGestorVO painelGestorVO, SqlRowSet rs) {
		PainelGestorContaPagarMesAnoVO painelGestorContaPagarMesAnoVO = new PainelGestorContaPagarMesAnoVO();
		painelGestorContaPagarMesAnoVO.setMesAno(rs.getString("mes") + "/" + rs.getString("ano"));
		painelGestorContaPagarMesAnoVO.setPagoAdiantadoDeOutroMesNoMes(rs.getDouble("valorPagoAdiantadoMes"));
		painelGestorContaPagarMesAnoVO.setPagoAtrazadoDeOutroMesNoMes(rs.getDouble("valorPagoAtrazadoMes"));
		painelGestorContaPagarMesAnoVO.setPagoDoMes(rs.getDouble("valorPagoDoMes"));
		painelGestorContaPagarMesAnoVO.setPagoNoMes(rs.getDouble("valorPagoNoMes"));
		painelGestorContaPagarMesAnoVO.setPagoDoMesNoMes(rs.getDouble("valorPagoDoMesNoMes"));
		painelGestorContaPagarMesAnoVO.setProvisaoPagarMes(rs.getDouble("valor"));
		painelGestorContaPagarMesAnoVO.setSaldoPagarMes(rs.getDouble("valorAPagar"));
		painelGestorContaPagarMesAnoVO.setTotalVencidoMes(rs.getDouble("valorVencido"));
		painelGestorVO.getPainelGestorContaPagarMesAnoVOs().add(painelGestorContaPagarMesAnoVO);

	}

	public Double consultarValorPagarAntesDoPeriodo(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio) throws Exception {
		StringBuilder sb = new StringBuilder(" select sum(valor::NUMERIC(20,2)) as valorPagar from contaPagar where situacao = 'AP'");
		sb.append(" and dataVencimento < '").append(Uteis.getDataJDBC(dataInicio)).append(" 23:59:59'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contaPagar.unidadeensino"));
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (dadosSQL.next()) {
			return dadosSQL.getDouble("valorPagar");
		}
		return 0.0;
	}

	/**
	 * Este Método realiza os calculos dos totalizadores finais do Painel Gestor
	 * Acadêmico Financeiro sendo eles: 1 - Média de Alunos no Período (Se dá
	 * atraves da soma de alunos ativos de cada mês e dividido pelo número de
	 * meses); 2 - Total de Alunos Novos; 3 - Total de Alunos Renovados; 4 -
	 * Total de Trancamentos; 5 - Total de Cancelamentos; 6 - Total de Transf.
	 * de Saída; 7 - Total de Receitas do Período (A soma de todos os valores
	 * recebidos no periodo); 8 - Total de Despesas do Período (A soma de todos
	 * os valores pagos no periodo); 9 - Média de Receitas do Período (A soma de
	 * todas as médias de receitas de cada mês e dividido pelo número de meses);
	 * 10 - Média de despesas do Período (A soma de todas as médias de despesas
	 * de cada mês e dividido pelo número de meses);
	 * 
	 * @param painelGestorVO
	 * @param grafico
	 */
	public void executarCalculoFinalPainelGestorFinanceiroAcademico(PainelGestorVO painelGestorVO, Boolean grafico) {
		for (PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO : painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs()) {
			if (!grafico) {
				for (PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorFinanceiroAcademicoNivelEducacionalVO : painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs()) {
					if (painelGestorFinanceiroAcademicoNivelEducacionalVO.getNivel().equals("") && painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() > 0) {
						painelGestorFinanceiroAcademicoNivelEducacionalVO.setMediaDespesaAluno(Uteis.arrendondarForcando2CadasDecimais(painelGestorFinanceiroAcademicoNivelEducacionalVO.getDespesa() / painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo()));
						painelGestorFinanceiroAcademicoNivelEducacionalVO.setMediaReceitaAluno(Uteis.arrendondarForcando2CadasDecimais(painelGestorFinanceiroAcademicoNivelEducacionalVO.getReceita() / painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo()));
					} else if (painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() > 0 && painelGestorFinanceiroAcademicoNivelEducacionalVO.getAlunoAtivo() > 0) {
						painelGestorFinanceiroAcademicoNivelEducacionalVO.setMediaDespesaAluno(Uteis.arrendondarForcando2CadasDecimais((painelGestorFinanceiroAcademicoMesAnoVO.getTotalOutraDespesa() / painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo()) + (painelGestorFinanceiroAcademicoNivelEducacionalVO.getDespesa() / painelGestorFinanceiroAcademicoNivelEducacionalVO.getAlunoAtivo())));

						painelGestorFinanceiroAcademicoNivelEducacionalVO.setMediaReceitaAluno(Uteis.arrendondarForcando2CadasDecimais((painelGestorFinanceiroAcademicoMesAnoVO.getTotalOutraReceita() / painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo()) + (painelGestorFinanceiroAcademicoNivelEducacionalVO.getReceita() / painelGestorFinanceiroAcademicoNivelEducacionalVO.getAlunoAtivo())));

					}

				}
				if (painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() > 0) {
					painelGestorFinanceiroAcademicoMesAnoVO.setTotalMediaDespesa(painelGestorFinanceiroAcademicoMesAnoVO.getTotalDespesa() / painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo());
					painelGestorFinanceiroAcademicoMesAnoVO.setTotalMediaReceita(painelGestorFinanceiroAcademicoMesAnoVO.getTotalReceita() / painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo());
				}
				painelGestorVO.setTotalReceita(painelGestorVO.getTotalReceita() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalReceita());
				painelGestorVO.setTotalDespesa(painelGestorVO.getTotalDespesa() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalDespesa());

				painelGestorVO.setTotalMediaDespesa(painelGestorVO.getTotalMediaDespesa() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalMediaDespesa());
				painelGestorVO.setTotalMediaReceita(painelGestorVO.getTotalMediaReceita() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalMediaReceita());
			}

			painelGestorVO.setTotalAlunoCancelado(painelGestorVO.getTotalAlunoCancelado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoCancelado());
			painelGestorVO.setTotalNovoAluno(painelGestorVO.getTotalNovoAluno() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalNovoAluno());
			painelGestorVO.setTotalAlunoRenovado(painelGestorVO.getTotalAlunoRenovado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRenovado());
			painelGestorVO.setTotalAlunoTrancado(painelGestorVO.getTotalAlunoTrancado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTrancado());
			painelGestorVO.setTotalAlunoTransferenciaSaida(painelGestorVO.getTotalAlunoTransferenciaSaida() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTransferenciaSaida());
			painelGestorVO.setTotalAlunoTransferenciaInterna(painelGestorVO.getTotalAlunoTransferenciaInterna() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTransferenciaInterna());
			painelGestorVO.setTotalAlunoAtivo(painelGestorVO.getTotalAlunoAtivo() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo());
			painelGestorVO.setTotalAlunoAptoFormar(painelGestorVO.getTotalAlunoAptoFormar() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar());
			painelGestorVO.setTotalAlunoPreMatricula(painelGestorVO.getTotalAlunoPreMatricula() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula());
			painelGestorVO.setTotalAlunoAbandonado(painelGestorVO.getTotalAlunoAbandonado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAbandanado());
			painelGestorVO.setTotalAlunoFormado(painelGestorVO.getTotalAlunoFormado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoFormado());
			painelGestorVO.setTotalAlunoRetornoEvasao(painelGestorVO.getTotalAlunoRetornoEvasao() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRetornoEvasao());

		}
		if (!painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().isEmpty()) {
			if (!grafico) {
				painelGestorVO.setTotalMediaDespesa(painelGestorVO.getTotalMediaDespesa() / painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().size());
				painelGestorVO.setTotalMediaReceita(painelGestorVO.getTotalMediaReceita() / painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().size());
			}
			painelGestorVO.setTotalAlunoAtivo(painelGestorVO.getTotalAlunoAtivo() / painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().size());
		}
	}

	public void montarDadosPainelGestorFinanceiroAcademicoMesAnoVO(PainelGestorVO painelGestorVO, Boolean grafico, SqlRowSet rs, Boolean mesmoMesAno, Boolean isFiltroPorNivelEducacional, Boolean isFiltroPorCurso) {
		PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO = null;
		if (mesmoMesAno) {
			painelGestorFinanceiroAcademicoMesAnoVO = consultarObjPainelGestorFinanceiroAcademicoMesAnoVOs(painelGestorVO, rs.getInt("mes") + "/" + rs.getInt("ano"), String.valueOf(rs.getInt("dia")));
		} else if (isFiltroPorNivelEducacional || isFiltroPorCurso) {
			if (painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().isEmpty()) {
				painelGestorFinanceiroAcademicoMesAnoVO = consultarObjPainelGestorFinanceiroAcademicoMesAnoVOs(painelGestorVO, rs.getInt("mes") + "/" + rs.getInt("ano"), null);
			} else {
				painelGestorFinanceiroAcademicoMesAnoVO = painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().get(0);
			}
		} else {
			painelGestorFinanceiroAcademicoMesAnoVO = consultarObjPainelGestorFinanceiroAcademicoMesAnoVOs(painelGestorVO, rs.getInt("mes") + "/" + rs.getInt("ano"), null);
		}
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoAptoFormar(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar() + rs.getInt("aptoFormar"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoAtivo(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() + rs.getInt("ativo"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalNovoAluno(painelGestorFinanceiroAcademicoMesAnoVO.getTotalNovoAluno() + rs.getInt("novato"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoRenovado(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRenovado() + rs.getInt("renovado"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoPreMatricula(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula() + rs.getInt("prematricula"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoCancelado(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoCancelado() + rs.getInt("cancelado"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoTrancado(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTrancado() + rs.getInt("trancado"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoAbandanado(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAbandanado() + rs.getInt("abandono"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoFormado(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoFormado() + rs.getInt("formado"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoNaoRenovado(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoNaoRenovado() + rs.getInt("naoRenovaram"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoRetornoEvasao(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRetornoEvasao() + rs.getInt("retornoEvasao"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoTransferenciaInterna(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTransferenciaInterna() + rs.getInt("transferenciaInterna"));

		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoTransferenciaSaida(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTransferenciaSaida() + rs.getInt("transferencia"));
		if (!grafico) {
			painelGestorFinanceiroAcademicoMesAnoVO.setTotalReceita(painelGestorFinanceiroAcademicoMesAnoVO.getTotalReceita() + rs.getDouble("receita"));
			painelGestorFinanceiroAcademicoMesAnoVO.setTotalDespesa(painelGestorFinanceiroAcademicoMesAnoVO.getTotalDespesa() + rs.getDouble("despesa"));
			if (rs.getString("nivelEducacional") == null || rs.getString("nivelEducacional").equals("")) {
				painelGestorFinanceiroAcademicoMesAnoVO.setTotalOutraDespesa(painelGestorFinanceiroAcademicoMesAnoVO.getTotalOutraDespesa() + rs.getDouble("despesa"));
				painelGestorFinanceiroAcademicoMesAnoVO.setTotalOutraReceita(painelGestorFinanceiroAcademicoMesAnoVO.getTotalOutraReceita() + rs.getDouble("receita"));
			}
			montarDadosPainelGestorFinanceiroAcademicoNivelEducacionalVO(painelGestorFinanceiroAcademicoMesAnoVO, rs, isFiltroPorNivelEducacional, isFiltroPorCurso);
		}
	}

	public void montarDadosPainelGestorFinanceiroAcademicoNivelEducacionalVO(PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO, SqlRowSet rs, Boolean isFiltroPorNivelEducacional, Boolean isFiltroPorCurso) {
		PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorNovo = new PainelGestorFinanceiroAcademicoNivelEducacionalVO();
		if (isFiltroPorNivelEducacional) {
			painelGestorNovo = consultarObjPainelGestorFinanceiroAcademicoNivelEducacionalVOs(painelGestorFinanceiroAcademicoMesAnoVO, rs.getString("nivelEducacional"), rs.getInt("codigoCurso"), null);
			painelGestorNovo.setCodigoCurso(rs.getInt("codigoCurso"));
			painelGestorNovo.setCurso(rs.getString("nomeCurso"));
		} else if (isFiltroPorCurso) {
			painelGestorNovo = consultarObjPainelGestorFinanceiroAcademicoNivelEducacionalVOs(painelGestorFinanceiroAcademicoMesAnoVO, rs.getString("nivelEducacional"), null, rs.getInt("codigoTurma"));
			painelGestorNovo.setCodigoTurma(rs.getInt("codigoTurma"));
			painelGestorNovo.setTurma(rs.getString("nomeTurma"));
		} else {
			painelGestorNovo = consultarObjPainelGestorFinanceiroAcademicoNivelEducacionalVOs(painelGestorFinanceiroAcademicoMesAnoVO, rs.getString("nivelEducacional"), null, null);
		}
		painelGestorNovo.setAlunoAtivo(painelGestorNovo.getAlunoAtivo() + rs.getInt("ativo"));
		painelGestorNovo.setAlunoAptoFormar(painelGestorNovo.getAlunoAptoFormar() + rs.getInt("aptoFormar"));
		painelGestorNovo.setAlunoNovo(painelGestorNovo.getAlunoNovo() + rs.getInt("novato"));
		painelGestorNovo.setAlunoRenovado(painelGestorNovo.getAlunoRenovado() + rs.getInt("renovado"));
		painelGestorNovo.setAlunoCancelado(painelGestorNovo.getAlunoCancelado() + rs.getInt("cancelado"));
		painelGestorNovo.setAlunoTrancado(painelGestorNovo.getAlunoTrancado() + rs.getInt("trancado"));
		painelGestorNovo.setAlunoAbandonado(painelGestorNovo.getAlunoAbandonado() + rs.getInt("abandono"));
		painelGestorNovo.setAlunoPreMatriculado(painelGestorNovo.getAlunoPreMatriculado() + rs.getInt("prematricula"));
		painelGestorNovo.setAlunoNaoRenovado(painelGestorNovo.getAlunoNaoRenovado() + rs.getInt("naoRenovaram"));
		painelGestorNovo.setAlunoFormado(painelGestorNovo.getAlunoFormado() + rs.getInt("formado"));
		painelGestorNovo.setAlunoTransferido(painelGestorNovo.getAlunoTransferido() + rs.getInt("transferencia"));
		painelGestorNovo.setAlunoTransferenciaInterna(painelGestorNovo.getAlunoTransferenciaInterna() + rs.getInt("transferenciaInterna"));
		painelGestorNovo.setAlunoRetornoEvasao(painelGestorNovo.getAlunoRetornoEvasao() + rs.getInt("retornoEvasao"));

		painelGestorNovo.setReceita(painelGestorNovo.getReceita() + rs.getDouble("receita"));
		painelGestorNovo.setDespesa(painelGestorNovo.getDespesa() + rs.getDouble("despesa"));

	}

	public PainelGestorFinanceiroAcademicoMesAnoVO consultarObjPainelGestorFinanceiroAcademicoMesAnoVOs(PainelGestorVO painelGestorVO, String mesAno, String dia) {
		if (dia == null) {
			for (PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO : painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs()) {
				if (painelGestorFinanceiroAcademicoMesAnoVO.getMesAno().equals(mesAno)) {
					return painelGestorFinanceiroAcademicoMesAnoVO;
				}
			}
		}
		PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO = new PainelGestorFinanceiroAcademicoMesAnoVO();
		painelGestorFinanceiroAcademicoMesAnoVO.setMesAno(mesAno);
		painelGestorFinanceiroAcademicoMesAnoVO.setDia(dia);
		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().add(painelGestorFinanceiroAcademicoMesAnoVO);
		return painelGestorFinanceiroAcademicoMesAnoVO;
	}

	public PainelGestorFinanceiroAcademicoNivelEducacionalVO consultarObjPainelGestorFinanceiroAcademicoNivelEducacionalVOs(PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO, String nivel, Integer codigoCurso, Integer codigoTurma) {
		if (nivel == null) {
			nivel = "";
		}
		if ((codigoCurso == null || codigoCurso == 0) && (codigoTurma == null || codigoTurma == 0)) {
			for (PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorFinanceiroAcademicoNivelEducacionalVO : painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs()) {
				if (painelGestorFinanceiroAcademicoNivelEducacionalVO.getNivel().equals(nivel)) {
					return painelGestorFinanceiroAcademicoNivelEducacionalVO;
				}
			}
		} else if ((codigoCurso != null && codigoCurso != 0) && (codigoTurma == null || codigoTurma == 0)) {
			for (PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorFinanceiroAcademicoNivelEducacionalVO : painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs()) {
				if (painelGestorFinanceiroAcademicoNivelEducacionalVO.getNivel().equals(nivel) && painelGestorFinanceiroAcademicoNivelEducacionalVO.getCodigoCurso().equals(codigoCurso)) {
					return painelGestorFinanceiroAcademicoNivelEducacionalVO;
				}
			}
		} else {
			for (PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorFinanceiroAcademicoNivelEducacionalVO : painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs()) {
				if (painelGestorFinanceiroAcademicoNivelEducacionalVO.getNivel().equals(nivel) && painelGestorFinanceiroAcademicoNivelEducacionalVO.getCodigoTurma().equals(codigoTurma)) {
					return painelGestorFinanceiroAcademicoNivelEducacionalVO;
				}
			}
		}

		PainelGestorFinanceiroAcademicoNivelEducacionalVO painelGestorFinanceiroAcademicoNivelEducacionalVO = new PainelGestorFinanceiroAcademicoNivelEducacionalVO();
		painelGestorFinanceiroAcademicoNivelEducacionalVO.setNivel(nivel);

		if (nivel.equals("") && painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs().size() > 0) {
			painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs().add(painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs().size(), painelGestorFinanceiroAcademicoNivelEducacionalVO);
		} else {
			painelGestorFinanceiroAcademicoMesAnoVO.getPainelGestorFinanceiroAcademicoNivelEducacionalVOs().add(0, painelGestorFinanceiroAcademicoNivelEducacionalVO);
		}

		return painelGestorFinanceiroAcademicoNivelEducacionalVO;
	}

	/**
	 * Realiza a consulta dos dados necessário para a geração do Painel Gestor
	 * Financeiro Acadêmico trazendo separado por mês e nivel educacional os
	 * seguintes dados: Mês e Ano; Nível Educacional; Total de Alunos Ativos de
	 * cada Nivel/Mês; Total de Alunos Novos de cada Nivel/Mês; Total de Alunos
	 * Renovados de cada Nivel/Mês; Total de Alunos Cancelados de cada
	 * Nivel/Mês; Total de Alunos Trancados de cada Nivel/Mês; Total de Alunos
	 * Trânsferidos de cada Nivel/Mês; Total de Receita em cada Nivel/Mês; Total
	 * de Despesa em cada Nivel/Mês; Media de Receita em cada Nivel/Mês; Media
	 * de Despesa em cada Nivel/Mês;
	 * 
	 * @param painelGestorVO
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @throws Exception
	 */
	public void consultarDadosPainelGestorFinanceiroAcademico(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {

		if (dataTermino.compareTo(Uteis.getDataUltimoDiaMes(new Date())) > 0) {
			dataTermino = Uteis.getDataUltimoDiaMes(new Date());
		}
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);

		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();
		// Junta os dados do Academico e Financeiro
		sb.append(" select nivelEducacional, sum(ativo)::INT as ativo, sum(aptoFormar)::INT as aptoFormar, sum(novato)::INT as novato, sum(renovado)::INT as renovado, sum(cancelado)::INT as cancelado, sum(trancado)::INT as trancado, sum(transferencia)::INT as transferencia, sum(prematricula)::INT as prematricula, sum(abandono) as abandono, sum(formado) as formado, sum(naoRenovaram) as naoRenovaram,  sum(retornoEvasao)::INT as retornoEvasao, sum(transferenciaInterna) as transferenciaInterna,  sum(receita::NUMERIC(20,2)) as receita, sum(despesa::NUMERIC(20,2)) as despesa, ano::INT, mes::INT from ( ");
		// Junta os dados do Academico
		sb.append(" select nivelEducacional, sum(ativo) as ativo, sum(aptoFormar) as aptoFormar, sum(novato) as novato, sum(renovado) as renovado, sum(cancelado) as cancelado, sum(trancado) as trancado, sum(transferencia) as transferencia, sum(prematricula)::INT as prematricula, sum(abandono) as abandono, sum(formado) as formado, sum(naoRenovaram) as naoRenovaram, sum(retornoEvasao) as retornoEvasao, sum(transferenciaInterna) as transferenciaInterna, 0.0 as receita, 0.0 as despesa,   ano, mes from ( ");
		sb.append(getSqlConsultaAcademicoAlunosAtivos(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNaoRenovaram(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoConcluiramDisciplinasRegulares(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoPreMatriculados(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNovato(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRenovado(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRetornoEvasao(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTrancado(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaSaida(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaInterna(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoAbandonoCurso(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoCancelamento(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoFormado(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" ) as academico group by ano, mes, nivelEducacional");
		// Traz os dados do financeiro separado por nivel edicacional
		sb.append(" union all");
		sb.append(" select nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram,  0 as retornoEvasao, 0 as transferenciaInterna,  sum(receita) as receita, sum(despesa) as despesa,  ano, mes from (");
		sb.append(" select nivelEducacional, case when (tipo = 'RECEITA') then sum(valor::NUMERIC(20,2)) else 0.0 end as receita,");
		sb.append(" case when (tipo = 'DESPESA') then sum(valor::NUMERIC(20,2)) else 0.0 end as despesa,  ano, mes from (");
		sb.append(" select sum(valor::NUMERIC(20,2)) as valor, tipo, ano, mes, nivelEducacional");
		sb.append(" from ( select distinct contareceber.codigo, valorRecebido::NUMERIC(20,2) as valor, 'RECEITA'::VARCHAR as tipo, extract(year from max(negociacaorecebimento.data))::INT as ano, ");
		sb.append(" extract(month from max(negociacaorecebimento.data))::INT as mes, nivelEducacional");
		sb.append(" from contareceber  ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" left join  matricula on matricula.matricula = contaReceber.matriculaAluno and  contaReceber.tipoPessoa = 'AL' ");
		sb.append(" left join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" left join curso on curso.codigo = matricula.curso ");
		sb.append(" where  contareceber.situacao = 'RE' and negociacaorecebimento.data ::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
		sb.append(" and negociacaorecebimento.data ::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(" group by contareceber.codigo, valorRecebido, nivelEducacional order by ano, mes, nivelEducacional) as t group by ano, mes, nivelEducacional, tipo ");
		sb.append(" union all ");
		sb.append(" select sum(valor::NUMERIC(20,2)) as valor, tipo, ano, mes, '' as nivelEducacional from ( select contapagar.codigo, valorPago::NUMERIC(20,2) as valor, 'DESPESA'::VARCHAR as tipo, ");
		sb.append(" extract(year from max(negociacaopagamento.data))::INT as ano, extract(month from max(negociacaopagamento.data))::INT as mes from contapagar  ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo   ");
		sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" left join  pessoa as aluno on aluno.codigo = contapagar.pessoa and  contapagar.tipoSacado = 'AL'");
		sb.append(" left join  matricula on matricula.aluno = aluno.codigo");
		sb.append(" left join curso on curso.codigo = matricula.curso");
		sb.append(" where contapagar.situacao = 'PA' and negociacaopagamento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sb.append(" and negociacaopagamento.data::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" group by contapagar.codigo, valorPago order by ano, mes) as t group by ano, mes, tipo  order by tipo desc, ano, mes, nivelEducacional");
		sb.append(" ) as g group by nivelEducacional, mes, ano, tipo");
		sb.append(" ) as financeiro group by nivelEducacional, mes, ano ");
		sb.append(" ) as financeiroacademico group by nivelEducacional, mes, ano order by ano, mes, nivelEducacional");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
		painelGestorVO.setTotalAlunoAtivo(0.0);
		painelGestorVO.setTotalAlunoAptoFormar(0);
		painelGestorVO.setTotalNovoAluno(0);
		painelGestorVO.setTotalAlunoCancelado(0);
		painelGestorVO.setTotalAlunoRenovado(0);
		painelGestorVO.setTotalAlunoTrancado(0);
		painelGestorVO.setTotalAlunoAbandonado(0);
		painelGestorVO.setTotalAlunoFormado(0);
		painelGestorVO.setTotalAlunoPreMatricula(0);
		painelGestorVO.setTotalAlunoTransferenciaSaida(0);
		painelGestorVO.setTotalAlunoTransferenciaInterna(0);
		painelGestorVO.setTotalMediaDespesa(0.0);
		painelGestorVO.setTotalAlunoRetornoEvasao(0);
		painelGestorVO.setTotalMediaReceita(0.0);
		painelGestorVO.setTotalDespesa(0.0);
		painelGestorVO.setTotalReceita(0.0);
		while (rs.next()) {
			montarDadosPainelGestorFinanceiroAcademicoMesAnoVO(painelGestorVO, false, rs, false, false, false);
		}
		executarCalculoFinalPainelGestorFinanceiroAcademico(painelGestorVO, false);
	}
	
	public void consultarDadosGraficoPainelGestorFinanceiroAcademico(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {
		if (dataTermino.compareTo(Uteis.getDataUltimoDiaMes(new Date())) > 0) {
			dataTermino = Uteis.getDataUltimoDiaMes(new Date());
		}
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		StringBuilder sb = new StringBuilder();

		// Junta os dados do Academico
		sb.append(" select sum(ativo)::INT as ativo,sum(aptoFormar)::INT as aptoFormar, sum(novato)::INT as novato, sum(renovado)::INT as renovado, sum(cancelado)::INT as cancelado, sum(trancado)::INT as trancado, sum(transferencia)::INT as transferencia, sum(prematricula)::INT as prematricula, sum(abandono)::INT as abandono, sum(formado)::INT as formado, sum(naoRenovaram)::INT as naoRenovaram,  sum(retornoEvasao)::INT as retornoEvasao, sum(transferenciaInterna) as transferenciaInterna, ano::INT, mes::INT from ( ");
		sb.append(getSqlConsultaAcademicoAlunosAtivos(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNaoRenovaram(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoConcluiramDisciplinasRegulares(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoPreMatriculados(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNovato(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRenovado(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRetornoEvasao(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTrancado(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaSaida(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaInterna(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoAbandonoCurso(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoCancelamento(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoFormado(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0));
		sb.append(" ) as academico group by ano, mes order by  ano, mes");
//		System.out.println(sb.toString());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
		painelGestorVO.setTotalAlunoAtivo(0.0);
		painelGestorVO.setTotalAlunoAptoFormar(0);
		painelGestorVO.setTotalNovoAluno(0);
		painelGestorVO.setTotalAlunoCancelado(0);
		painelGestorVO.setTotalAlunoRenovado(0);
		painelGestorVO.setTotalAlunoTrancado(0);
		painelGestorVO.setTotalAlunoPreMatricula(0);
		painelGestorVO.setTotalAlunoAbandonado(0);
		painelGestorVO.setTotalAlunoRetornoEvasao(0);
		painelGestorVO.setTotalAlunoFormado(0);
		painelGestorVO.setTotalAlunoTransferenciaSaida(0);
		painelGestorVO.setTotalAlunoTransferenciaInterna(0);
		painelGestorVO.setTotalMediaDespesa(0.0);
		painelGestorVO.setTotalMediaReceita(0.0);
		painelGestorVO.setTotalDespesa(0.0);
		painelGestorVO.setTotalReceita(0.0);
		while (rs.next()) {
			montarDadosPainelGestorFinanceiroAcademicoMesAnoVO(painelGestorVO, true, rs, false, false, false);
		}
		executarCalculoFinalPainelGestorFinanceiroAcademico(painelGestorVO, true);
		executarCriacaoGraficoPainelGestorAcademico(painelGestorVO, false);
	}

	/**
	 * Este Método realiza a consulta dos dados para a geração do gráfico de
	 * linha do painel gestor acadêmico
	 * 
	 * @param painelGestorVO
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @throws Exception
	 */
	/*
	 * TODO: FILTRA GERAL
	 */
	public void consultarDadosGraficoPainelGestorFinanceiroAcademicoNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String ano, String semestre,  UsuarioVO usuarioVO) throws Exception {

		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		StringBuilder sb = new StringBuilder();
		sb.append(" select case ");
		sb.append(" when niveleducacional = 'IN' THEN 1 ");
		sb.append(" when niveleducacional = 'BA' THEN 2 ");
		sb.append(" when niveleducacional = 'ME' THEN 3 ");
		sb.append(" when niveleducacional = 'SU' THEN 4 ");
		sb.append(" when niveleducacional = 'PO' THEN 5 ");
		sb.append(" when niveleducacional = 'EX' THEN 6 ");
		sb.append(" when niveleducacional = 'SE' THEN 7 ");
		sb.append(" when niveleducacional = 'PR' THEN 8 ");
		sb.append(" end AS ordem, ");
		sb.append(" periodicidade, nivelEducacional, sum(ativo)::INT as ativo,sum(aptoFormar)::INT as aptoFormar, sum(prematricula)::INT as prematricula, sum(retornoEvasao)::INT as retornoEvasao from (");
		
		// Junta os dados do Academico
		sb.append(getSqlConsultaAcademicoAlunosAtivosPorNivelEducacional(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0, ano, semestre));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoAlunosPreMatriculadoPorNivelEducacional(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0, ano, semestre));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoAlunosEvasaoPorNivelEducacional(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0, ano, semestre));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoAlunosPossiveisFormandosPorNivelEducacional(dataInicio, dataTermino, unidadeEnsinoVOs, "", 0, ano, semestre));
		sb.append(" ) as academico group by periodicidade, nivelEducacional order by ordem ");
		
		//System.out.println(sb.toString());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
		painelGestorVO.setTotalAlunoAtivo(0.0);
		painelGestorVO.setTotalAlunoAptoFormar(0);
		painelGestorVO.setTotalAlunoPreMatricula(0);
		painelGestorVO.setTotalAlunoRetornoEvasao(0);
		
		painelGestorVO.setLegendaCategoriaPainelAcademicoNivelEducacional("");
		
		while (rs.next()) {
			montarDadosPainelGestorFinanceiroAcademicoPorNivelEducacional(painelGestorVO, rs, usuarioVO);
		}
		executarCalculoFinalPainelGestorFinanceiroAcademicoPorNivelEducacional(painelGestorVO);
		executarCriacaoGraficoPainelGestorAcademicoPorNivelEducacional(painelGestorVO);
	}

	/*
	 * TODO: FILTRA POR NIVEL EDACACIONAL
	 */
	public void consultarDadosPainelGestorFinanceiroAcademicoPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional) throws Exception {

		if (dataTermino.compareTo(Uteis.getDataUltimoDiaMes(new Date())) > 0) {
			dataTermino = Uteis.getDataUltimoDiaMes(new Date());
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();

		// Junta os dados do Academico e Financeiro
		sb.append(" select codigoCurso, nomeCurso, nivelEducacional, sum(ativo)::INT as ativo, sum(aptoFormar)::INT aptoFormar , sum(novato)::INT as novato, sum(renovado)::INT as renovado, sum(cancelado)::INT as cancelado, sum(trancado)::INT as trancado, sum(transferencia)::INT as transferencia, sum(prematricula)::INT as prematricula, sum(abandono)::INT as abandono, sum(formado)::INT as formado, sum(naoRenovaram)::INT as naoRenovaram, sum(retornoEvasao)::INT as retornoEvasao, sum(transferenciaInterna) as transferenciaInterna, sum(receita::NUMERIC(20,2)) as receita, sum(despesa::NUMERIC(20,2)) as despesa, '" + Uteis.getMesData(dataTermino) + "' as mes, '" + Uteis.getAnoData(dataTermino) + "' as ano from ( ");
		// Junta os dados do Academico
		sb.append(" select codigoCurso, nomeCurso, nivelEducacional, sum(ativo) as ativo, sum(aptoFormar) as aptoFormar, sum(novato) as novato, sum(renovado) as renovado, sum(cancelado) as cancelado, sum(trancado) as trancado, sum(transferencia) as transferencia,  sum(prematricula) as prematricula, sum(abandono) as abandono, sum(formado) as formado, sum(naoRenovaram) as naoRenovaram, sum(retornoEvasao) as retornoEvasao, sum(transferenciaInterna) as transferenciaInterna, 0.0 as receita, 0.0 as despesa from ( ");
		sb.append(getSqlConsultaAcademicoAlunosAtivos(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNaoRenovaram(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoConcluiramDisciplinasRegulares(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoPreMatriculados(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNovato(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRenovado(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRetornoEvasao(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTrancado(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaSaida(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaInterna(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoAbandonoCurso(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoCancelamento(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoFormado(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), 0));
		sb.append(" ) as academico group by codigoCurso, nomeCurso, nivelEducacional");
		// Traz os dados do financeiro separado por nivel edicacional
		sb.append(" union all");
		sb.append(" select codigoCurso, nomeCurso, nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato,  0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 as retornoEvasao, 0 as transferenciaInterna,  sum(receita) as receita, sum(despesa) as despesa from (");
		sb.append(" select codigoCurso, nomeCurso, nivelEducacional, case when (tipo = 'RECEITA') then sum(valor::NUMERIC(20,2)) else 0.0 end as receita,");
		sb.append(" case when (tipo = 'DESPESA') then sum(valor::NUMERIC(20,2)) else 0.0 end as despesa from (");
		sb.append(" select codigoCurso, nomeCurso, sum(valor::NUMERIC(20,2)) as valor, tipo, nivelEducacional");
		sb.append(" from ( select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, contareceber.codigo, valorRecebido as valor, 'RECEITA'::VARCHAR as tipo,  ");
		sb.append(" nivelEducacional");
		sb.append(" from contareceber  ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join  matricula on matricula.matricula = contaReceber.matriculaAluno and  contaReceber.tipoPessoa = 'AL'");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where contareceber.situacao = 'RE' and negociacaorecebimento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
		sb.append(" and negociacaorecebimento.data ::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(" group by codigoCurso, nomeCurso, contareceber.codigo, valorRecebido, nivelEducacional order by nivelEducacional) as t group by codigoCurso, nomeCurso, nivelEducacional, tipo ");

		sb.append(" union all ");

		sb.append(" select codigoCurso,  nomeCurso, sum(valor::NUMERIC(20,2)) as valor, tipo, '' as nivelEducacional from (");
		sb.append(" select curso.codigo as codigoCurso, curso.nome as nomeCurso,  contapagar.codigo, valorPago::NUMERIC(20,2) as valor, 'DESPESA'::VARCHAR as tipo ");
		sb.append(" from contapagar  ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo   ");
		sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" inner join  pessoa as aluno on aluno.codigo = contapagar.pessoa and  contapagar.tipoSacado = 'AL'");
		sb.append(" inner join  matricula on matricula.aluno = aluno.codigo");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where contapagar.situacao = 'PA' and negociacaopagamento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sb.append(" and negociacaopagamento.data::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" group by codigoCurso,  nomeCurso, contapagar.codigo, valorPago) as t group by codigoCurso,  nomeCurso,  tipo  order by tipo desc,  nivelEducacional");

		sb.append(" ) as g group by codigoCurso, nomeCurso, nivelEducacional,  tipo");

		sb.append(" ) as financeiro group by codigoCurso, nomeCurso, nivelEducacional ");

		sb.append("  ) as financeiroacademico where nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" group by codigoCurso, nomeCurso,  nivelEducacional order by nomeCurso desc, nivelEducacional");
		////System.out.println(sb.toString());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
		painelGestorVO.setTotalAlunoAtivo(0.0);
		painelGestorVO.setTotalAlunoAptoFormar(0);
		painelGestorVO.setTotalNovoAluno(0);
		painelGestorVO.setTotalAlunoCancelado(0);
		painelGestorVO.setTotalAlunoRenovado(0);
		painelGestorVO.setTotalAlunoTrancado(0);
		painelGestorVO.setTotalAlunoPreMatricula(0);
		painelGestorVO.setTotalAlunoAbandonado(0);
		painelGestorVO.setTotalAlunoFormado(0);
		painelGestorVO.setTotalAlunoRetornoEvasao(0);
		painelGestorVO.setTotalAlunoTransferenciaSaida(0);
		painelGestorVO.setTotalAlunoTransferenciaInterna(0);
		painelGestorVO.setTotalMediaDespesa(0.0);
		painelGestorVO.setTotalMediaReceita(0.0);
		painelGestorVO.setTotalDespesa(0.0);
		painelGestorVO.setTotalReceita(0.0);
		while (rs.next()) {
			montarDadosPainelGestorFinanceiroAcademicoMesAnoVO(painelGestorVO, false, rs, false, true, false);
		}
		executarCalculoFinalPainelGestorFinanceiroAcademico(painelGestorVO, false);
	}

	/*
	 * TODO: FILTRA CURSO
	 */
	public void consultarDadosPainelGestorFinanceiroAcademicoPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, Integer codigoCurso) throws Exception {

		if (dataTermino.compareTo(Uteis.getDataUltimoDiaMes(new Date())) > 0) {
			dataTermino = Uteis.getDataUltimoDiaMes(new Date());
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();

		// Junta os dados do Academico e Financeiro
		sb.append(" select codigoCurso, codigoTurma, nomeTurma, nivelEducacional, sum(ativo)::INT as ativo,sum(aptoFormar)::INT as aptoFormar,sum(novato)::INT as novato, sum(renovado)::INT as renovado, sum(cancelado)::INT as cancelado, sum(trancado)::INT as trancado, sum(transferencia)::INT as transferencia, sum(prematricula)::INT as prematricula, sum(abandono)::INT as abandono, sum(formado)::INT as formado, sum(naoRenovaram)::INT as naoRenovaram, sum(retornoEvasao)::INT as retornoEvasao, sum(transferenciaInterna) as transferenciaInterna, sum(receita::NUMERIC(20,2)) as receita, sum(despesa::NUMERIC(20,2)) as despesa, 0 as prematricula, '" + Uteis.getMesData(dataTermino) + "' as mes, '" + Uteis.getAnoData(dataTermino) + "' as ano from ( ");
		// Junta os dados do Academico
		sb.append(" select codigoCurso, codigoTurma, nomeTurma, nivelEducacional, sum(ativo) as ativo, sum(aptoFormar) as aptoFormar, sum(novato) as novato, sum(renovado) as renovado, sum(cancelado) as cancelado, sum(trancado) as trancado, sum(transferencia) as transferencia, sum(prematricula) as prematricula, sum(abandono) as abandono, sum(formado) as formado, sum(naoRenovaram) as naoRenovaram, sum(retornoEvasao) as retornoEvasao, sum(transferenciaInterna) as transferenciaInterna, 0.0 as receita, 0.0 as despesa from ( ");
		sb.append(getSqlConsultaAcademicoAlunosAtivos(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNaoRenovaram(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoConcluiramDisciplinasRegulares(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoPreMatriculados(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoNovato(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRenovado(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoRetornoEvasao(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTrancado(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaSaida(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoTransferenciaInterna(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoAbandonoCurso(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoCancelamento(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" union all");
		sb.append(getSqlConsultaAcademicoFormado(dataInicio, dataTermino, unidadeEnsinoVOs, tipoNivelEducacional.getValor(), codigoCurso));
		sb.append(" ) as academico group by codigoCurso, codigoTurma, nomeTurma,  nivelEducacional");

		// Traz os dados do financeiro separado por nivel edicacional

		sb.append(" union all");

		sb.append(" select codigoCurso, codigoTurma, nomeTurma, nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 as retornoEvasao, 0 as transferenciaInterna,  sum(receita::NUMERIC(20,2)) as receita, sum(despesa::NUMERIC(20,2)) as despesa from (");
		sb.append(" select codigoCurso, codigoTurma, nomeTurma, nivelEducacional, case when (tipo = 'RECEITA') then sum(valor::NUMERIC(20,2)) else 0.0 end as receita,");
		sb.append(" case when (tipo = 'DESPESA') then sum(valor::NUMERIC(20,2)) else 0.0 end as despesa from (");
		sb.append(" select codigoCurso, codigoTurma, nomeTurma, sum(valor::NUMERIC(20,2)) as valor, tipo, nivelEducacional");
		sb.append(" from ( select distinct curso.codigo as codigoCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nometurma, contareceber.codigo, valorRecebido::NUMERIC(20,2) as valor, 'RECEITA'::VARCHAR as tipo,  ");
		sb.append(" nivelEducacional");
		sb.append(" from contareceber  ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join  matricula on matricula.matricula = contaReceber.matriculaAluno and  contaReceber.tipoPessoa = 'AL'");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where  contareceber.situacao = 'RE' and negociacaorecebimento.data ::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
		sb.append(" and negociacaorecebimento.data ::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and curso.codigo = ").append(codigoCurso).append(" ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(" group by codigoCurso, codigoTurma, nomeTurma, contareceber.codigo, valorRecebido, nivelEducacional order by  nivelEducacional) as t group by codigoCurso, codigoTurma, nomeTurma,  nivelEducacional, tipo ");

		sb.append(" union all ");

		sb.append(" select codigoCurso, codigoTurma, nomeTurma, sum(valor::NUMERIC(20,2)) as valor, tipo,  '' as nivelEducacional from (");
		sb.append(" select curso.codigo as codigoCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nometurma, contapagar.codigo, valorPago::NUMERIC(20,2) as valor, 'DESPESA'::VARCHAR as tipo ");
		sb.append(" from contapagar  ");
		sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo   ");
		sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo ");
		sb.append(" inner join  pessoa as aluno on aluno.codigo = contapagar.pessoa and  contapagar.tipoSacado = 'AL'");
		sb.append(" inner join  matricula on matricula.aluno = aluno.codigo");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where contapagar.situacao = 'PA' and negociacaopagamento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sb.append(" and negociacaopagamento.data::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and curso.codigo = ").append(codigoCurso).append(" ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sb.append(" group by codigoCurso, codigoTurma,  nomeTurma, contapagar.codigo, valorPago) as t group by codigoCurso, codigoTurma,  nomeTurma,  tipo  order by tipo desc, nivelEducacional");

		sb.append(" ) as g group by codigoCurso, codigoTurma,  nomeTurma, nivelEducacional, tipo");

		sb.append(" ) as financeiro group by codigoCurso, codigoTurma,  nomeTurma, nivelEducacional ");

		sb.append("  ) as financeiroacademico where nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		sb.append(" and codigoCurso = ").append(codigoCurso).append(" ");
		sb.append(" group by codigoCurso, codigoTurma,  nomeTurma, nivelEducacional order by nomeTurma desc,  nivelEducacional");
		////System.out.println(sb.toString());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
		painelGestorVO.setTotalAlunoAtivo(0.0);
		painelGestorVO.setTotalAlunoAptoFormar(0);
		painelGestorVO.setTotalNovoAluno(0);
		painelGestorVO.setTotalAlunoCancelado(0);
		painelGestorVO.setTotalAlunoRenovado(0);
		painelGestorVO.setTotalAlunoTrancado(0);
		painelGestorVO.setTotalAlunoPreMatricula(0);
		painelGestorVO.setTotalAlunoAbandonado(0);

		painelGestorVO.setTotalAlunoFormado(0);
		painelGestorVO.setTotalAlunoTransferenciaSaida(0);
		painelGestorVO.setTotalAlunoTransferenciaInterna(0);
		painelGestorVO.setTotalMediaDespesa(0.0);
		painelGestorVO.setTotalMediaReceita(0.0);
		painelGestorVO.setTotalAlunoRetornoEvasao(0);
		painelGestorVO.setTotalDespesa(0.0);
		painelGestorVO.setTotalReceita(0.0);
		while (rs.next()) {
			montarDadosPainelGestorFinanceiroAcademicoMesAnoVO(painelGestorVO, false, rs, false, false, true);
		}
		executarCalculoFinalPainelGestorFinanceiroAcademico(painelGestorVO, false);
	}

	/**
	 * Este método é responsavel em gerar o gráfico de linha do Painel Gestor
	 * Acadêmico mostrando: 1 - Total de Alunos Ativos no Mês; 2 - Total de
	 * Alunos Novos/Renovados; 3 - Total Evasão (Trancamento, Cancelamento e
	 * Transferência de Saida);
	 * 
	 * @param painelGestorVO
	 */
	public void executarCriacaoGraficoPainelGestorAcademico(PainelGestorVO painelGestorVO, Boolean mesmoMesAno) throws Exception {

		Integer dia = 0;
		Integer mes = 0;
		Integer ano = 0;
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().clear();

		painelGestorVO.setListaValoresAcademicoAtivo("");
		painelGestorVO.setListaValoresAcademicoAptoFormar("");
		painelGestorVO.setListaValoresAcademicoNovo("");
		painelGestorVO.setListaValoresAcademicoEvasao("");
		painelGestorVO.setListaValoresAcademicoPreMatricula("");
		LegendaGraficoVO legendaAtivo = new LegendaGraficoVO("Alunos Ativos", 0.0, "#3399FF");
		LegendaGraficoVO legendaPreMatricula = new LegendaGraficoVO("Alunos Pré-Matriculados", 0.0, "#999999");
		LegendaGraficoVO legendaAptoFormar = new LegendaGraficoVO("Alunos Ativos Estudaram Todas as Disciplinas", 0.0, "#3399FF");
		LegendaGraficoVO legendaNovos = new LegendaGraficoVO("Alunos Novos/Renovados", 0.0, "#00CC66");
		LegendaGraficoVO legendaEvasao = new LegendaGraficoVO("Evasao (Cancelamento, Trancamento, Transferencia de Saida)", 0.0, "#D20000");
		// Date data = null;
		for (PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO : painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs()) {
			mes = Integer.parseInt(painelGestorFinanceiroAcademicoMesAnoVO.getMesAno().substring(0, painelGestorFinanceiroAcademicoMesAnoVO.getMesAno().indexOf("/")));
			ano = Integer.parseInt(painelGestorFinanceiroAcademicoMesAnoVO.getMesAno().substring(painelGestorFinanceiroAcademicoMesAnoVO.getMesAno().length() - 4, painelGestorFinanceiroAcademicoMesAnoVO.getMesAno().length()));
			if (mesmoMesAno) {
				dia = Integer.parseInt(painelGestorFinanceiroAcademicoMesAnoVO.getDia());
			} else {
				dia = 1;
			}
			if (painelGestorVO.getListaValoresAcademicoAtivo().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoAtivo(painelGestorVO.getListaValoresAcademicoAtivo() + "[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() + "]");
			} else {
				painelGestorVO.setListaValoresAcademicoAtivo(painelGestorVO.getListaValoresAcademicoAtivo() + ",[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() + "]");
			}
			if (painelGestorVO.getListaValoresAcademicoAptoFormar().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoAptoFormar(painelGestorVO.getListaValoresAcademicoAptoFormar() + "[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar() + "]");
			} else {
				painelGestorVO.setListaValoresAcademicoAptoFormar(painelGestorVO.getListaValoresAcademicoAptoFormar() + ",[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar() + "]");
			}
			if (painelGestorVO.getListaValoresAcademicoNovo().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoNovo(painelGestorVO.getListaValoresAcademicoNovo() + "[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + (painelGestorFinanceiroAcademicoMesAnoVO.getTotalNovoAluno() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRenovado()) + "]");
			} else {
				painelGestorVO.setListaValoresAcademicoNovo(painelGestorVO.getListaValoresAcademicoNovo() + ",[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + (painelGestorFinanceiroAcademicoMesAnoVO.getTotalNovoAluno() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRenovado()) + "]");
			}
			if (painelGestorVO.getListaValoresAcademicoEvasao().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoEvasao(painelGestorVO.getListaValoresAcademicoEvasao() + "[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + (painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoCancelado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTrancado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTransferenciaSaida() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAbandanado()) + "]");
			} else {
				painelGestorVO.setListaValoresAcademicoEvasao(painelGestorVO.getListaValoresAcademicoEvasao() + ",[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + (painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoCancelado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTrancado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTransferenciaSaida() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAbandanado()) + "]");
			}
			if (painelGestorVO.getListaValoresAcademicoPreMatricula().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoPreMatricula(painelGestorVO.getListaValoresAcademicoPreMatricula() + "[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula() + "]");
			} else {
				painelGestorVO.setListaValoresAcademicoPreMatricula(painelGestorVO.getListaValoresAcademicoPreMatricula() + ",[Date.UTC(" + ano + ", " + (mes - 1) + "," + dia + "), " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula() + "]");
			}

			legendaAtivo.setValor(legendaAtivo.getValor() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo());
			legendaAptoFormar.setValor(legendaAptoFormar.getValor() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar());
			legendaNovos.setValor(legendaNovos.getValor() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalNovoAluno() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRenovado());
			legendaEvasao.setValor(legendaEvasao.getValor() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoCancelado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTrancado() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoTransferenciaSaida() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAbandanado());
			legendaPreMatricula.setValor(legendaPreMatricula.getValor() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula());
		}

		legendaAtivo.setLegenda("Alunos Ativos - " + Double.valueOf(legendaAtivo.getValor() / painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().size()).intValue() + " na Média");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaAtivo);

		legendaAptoFormar.setLegenda("Alunos Ativos Estudaram Todas as Disciplinas - " + Double.valueOf(legendaAptoFormar.getValor() / painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().size()).intValue() + " na Média");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaAptoFormar);

		legendaNovos.setLegenda("Alunos Novos/Renovados - " + legendaNovos.getValor().intValue() + " Alunos no Período");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaNovos);

		legendaEvasao.setLegenda("Evasão - " + legendaEvasao.getValor().intValue() + " Alunos no Período");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaEvasao);

		legendaPreMatricula.setLegenda("Pré-Matriculados - " + legendaPreMatricula.getValor().intValue() + " Alunos no Período");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaPreMatricula);

		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
		mes = null;
		ano = null;
	}

	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (!mesAno.isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosPainelGestorMonitoramentoDescontoPorNivelEducacional(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional, mesAno);
	}

	public void consultarDadosPainelGestorMonitoramentoDescontoPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select curso,codigoCurso, ");
		sb.append(" sum(descontoprogressivo::NUMERIC(20,2)) as descontoprogressivo, ");
		sb.append(" sum(descontoinstituicao::NUMERIC(20,2)) as descontoinstituicao, ");
		sb.append(" sum(descontoAluno::NUMERIC(20,2))  as descontoAluno,");
		sb.append(" sum(descontoConvenio::NUMERIC(20,2)) as descontoConvenio, ");
		sb.append(" sum(descontoRecebimento::NUMERIC(20,2)) as descontoRecebimento, ");
		sb.append(" sum(descontoRateio) as descontoRateio, ");
		sb.append(" sum(valor::NUMERIC(20,2))  as valor, ");
		sb.append(" sum(valorrecebido::NUMERIC(20,2)) as valorrecebido ");
		sb.append(" from (");
		sb.append(" select curso.nome as curso,curso.codigo as codigoCurso, ");
		sb.append(" case when(descontoConvenio is not null) then descontoConvenio else 0.0 end as descontoConvenio, ");
		sb.append(" case when(descontoinstituicao is not null) then descontoinstituicao else 0.0 end as descontoinstituicao,");
		sb.append(" case when(valordescontoalunojacalculado is not null) then valordescontoalunojacalculado::NUMERIC(20,2) else 0.0 end as descontoAluno, ");
		sb.append(" case when(valorcalculadodescontolancadorecebimento is not null) then valorcalculadodescontolancadorecebimento::NUMERIC(20,2) else 0.0 end as descontoRecebimento,");
		sb.append(" case when(valordescontoprogressivo is not null and descontoprogressivoutilizado != 'Nenhum') then valordescontoprogressivo::NUMERIC(20,2) else 0.0 end as descontoprogressivo, ");
		sb.append(" contareceber.valorDescontoRateio as descontoRateio, ");
		sb.append(" 0.0 as valor, valorrecebido");
		sb.append(" from contareceber");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		// sb.append(" inner join contareceberrecebimento on contareceberrecebimento.contareceber  = contareceber.codigo and situacao='RE' ");
		// sb.append(" and contareceberrecebimento.codigo =(select contareceberrecebimento.codigo from contareceberrecebimento  where  contareceberrecebimento.contareceber  = contareceber.codigo order by datarecebimento desc limit 1 )");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" where contareceber.situacao='RE' and negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}

		sb.append(" union all");
		// Esta união traz os valores faturados no período
		sb.append(" select curso.nome as curso,curso.codigo as codigoCurso,  0.0 as descontoprogressivo, 0.0 as descontoinstituicao, 0.0 as descontoAluno, ");
		sb.append(" 0.0 as descontoConvenio, 0.0 as descontoRecebimento, 0.0 as descontoRateio, valor, 0.0 as  valorrecebido");
		sb.append(" from contareceber ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" where dataVencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and dataVencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("'  and contareceber.situacao not in ('NE', 'CA') ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		sb.append(" ) as t group by codigoCurso, curso order by curso");

		painelGestorVO.setTotalFaturadoPeriodoMonitoramentoDesconto(0.0);
		painelGestorVO.setTotalRecebidoPeriodoMonitoramentoDesconto(0.0);
		painelGestorVO.setTotalDescontoAluno(0.0);
		painelGestorVO.setTotalDescontoConvenio(0.0);
		painelGestorVO.setTotalDescontoInstituicao(0.0);
		painelGestorVO.setTotalDescontoPeriodo(0.0);
		painelGestorVO.setTotalDescontoProgressivo(0.0);
		painelGestorVO.setTotalDescontoRecebimento(0.0);
		painelGestorVO.setTotalDescontoRateio(0.0);
		painelGestorVO.setTotalAlunoReceberamDesconto(0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.setPainelGestorMonitoramentoDescontoMesAnoVO(new PainelGestorMonitoramentoDescontoMesAnoVO());
		while (rs.next()) {
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setValorFaturadoMes(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getValorFaturadoMes() + rs.getDouble("valor"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setValorRecebidoMes(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getValorRecebidoMes() + rs.getDouble("valorrecebido"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoAluno(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoAluno() + rs.getDouble("descontoaluno"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoProgressivo(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoProgressivo() + rs.getDouble("descontoprogressivo"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoConvenio(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoConvenio() + rs.getDouble("descontoconvenio"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoInstituicao(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoInstituicao() + rs.getDouble("descontoinstituicao"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoRecebimento(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoRecebimento() + rs.getDouble("descontoRecebimento"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoRateio(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoRateio() + rs.getDouble("descontoRateio"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoMes(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoMes() + rs.getDouble("descontoaluno") + rs.getDouble("descontoprogressivo") + rs.getDouble("descontoconvenio") + rs.getDouble("descontoinstituicao") + rs.getDouble("descontoRecebimento") + rs.getDouble("descontoRateio"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getPainelGestorMonitoramentoDescontoNivelEducacionalVOs().add(montarDadosPainelGestorMonitoramentoDescontoCursoVO(rs, tipoNivelEducacional, mesAno, true));
		}
	}

	public PainelGestorMonitoramentoDescontoNivelEducacionalVO montarDadosPainelGestorMonitoramentoDescontoCursoVO(SqlRowSet rs, TipoNivelEducacional tipoNivelEducacional, String mesAno, Boolean isNivelEducacional) {
		PainelGestorMonitoramentoDescontoNivelEducacionalVO obj = new PainelGestorMonitoramentoDescontoNivelEducacionalVO();
		if (isNivelEducacional) {
			obj.setCurso(rs.getString("curso"));
			obj.setCodigoCurso(rs.getInt("codigoCurso"));
		} else {
			obj.setCurso(rs.getString("curso"));
			obj.setCodigoCurso(rs.getInt("codigoCurso"));
			obj.setTurma(rs.getString("turma"));
			obj.setCodigoTurma(rs.getInt("codigoTurma"));
		}
		obj.setMesAno(mesAno);
		obj.setNivel(tipoNivelEducacional.getValor());
		obj.setValorFaturado(rs.getDouble("valor"));
		obj.setValorRecebido(rs.getDouble("valorrecebido"));
		obj.setTotalDescontoAluno(rs.getDouble("descontoaluno"));
		obj.setTotalDescontoProgressivo(rs.getDouble("descontoprogressivo"));
		obj.setTotalDescontoConvenio(rs.getDouble("descontoconvenio"));
		obj.setTotalDescontoInstituicao(rs.getDouble("descontoinstituicao"));
		obj.setTotalDescontoRecebimento(rs.getDouble("descontoRecebimento"));
		obj.setTotalDescontoRateio(rs.getDouble("descontoRateio"));
		obj.setTotalDesconto(rs.getDouble("descontoaluno") + rs.getDouble("descontoprogressivo") + rs.getDouble("descontoconvenio") + rs.getDouble("descontoinstituicao") + rs.getDouble("descontoRecebimento") + rs.getDouble("descontoRateio"));
		return obj;
	}

	public void consultarDadosPainelGestorMonitoramentoConsultor(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codCurso, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select t.codunidade, t.codconsultor, t.consultor, t.codturma, t.identificadorturma, sum(t.qtdmat) as qtdmat, sum(t.qtdmatareceber) as qtdmatareceber, sum(qtdmatvencidas) as qtdmatvencidas, sum(qtdmatavencer) as qtdmatavencer, sum(qtdmatpre) as qtdmatpre, sum(qtdmatativo) as qtdmatativo, sum(qtdmatcancelado)  as qtdmatcancelado, sum(qtdmatextensao) as qtdmatextensao, sum(qtdmatdocpend) as qtdmatdocpend from (");
		// RECEBIDAS
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, ");
		sb.append(" count(matricula.matricula) as qtdmat , 0 as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, 0 as qtdmatcancelado, 0 as qtdmatextensao, 0 as qtdmatdocpend");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'RE' and parcela ilike '%MAT%' and matricula.situacao = 'AT' ");
		sb.append(" group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo , turma.identificadorturma order by pessoa.nome) ");

		sb.append(" union all ");
		// A RECEBER A VENCER
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, count(matricula.matricula) as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, 0 as qtdmatcancelado, 0 as qtdmatextensao, 0 as qtdmatdocpend from matricula  ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'AR' and parcela ilike '%MAT%' and matricula.situacao = 'AT' ");
		sb.append(" group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo, turma.identificadorturma order by pessoa.nome) ");
		sb.append(" union all ");
		// A RECEBER VENCIDAS
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, 0 as qtdmatareceber, count(matricula.matricula) as qtdmatvencidas , 0 as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, 0 as qtdmatcancelado, 0 as qtdmatextensao, 0 as qtdmatdocpend ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'AR' and parcela ilike '%MAT%' and contareceber.datavencimento < current_date ");
		sb.append(" and matricula.situacao = 'AT' group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo , turma.identificadorturma order by pessoa.nome) ");
		sb.append(" union all ");
		// A RECEBER A VENCER
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, 0 as qtdmatareceber, 0 as qtdmatvencidas , count(matricula.matricula) as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, 0 as qtdmatcancelado, 0 as qtdmatextensao, 0 as qtdmatdocpend ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'AR' and parcela ilike '%MAT%' and contareceber.datavencimento >= current_date and matricula.situacao = 'AT'  ");
		sb.append(" group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo , turma.identificadorturma order by pessoa.nome) ");
		sb.append(" union all ");
		// PRE-MATRICULA
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, 0 as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, count(matricula.matricula) as qtdmatpre , 0 as qtdmatativo, 0 as qtdmatcancelado, 0 as qtdmatextensao, 0 as qtdmatdocpend ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and matricula.situacao = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'PR' ");
		sb.append(" group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo , turma.identificadorturma order by pessoa.nome) ");
		sb.append(" union all ");
		// ATIVO
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, 0 as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, 0 as qtdmatpre, count(matricula.matricula) as qtdmatativo , 0 as qtdmatcancelado, 0 as qtdmatextensao, 0 as qtdmatdocpend ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and matricula.situacao = 'AT' and matricula.tipomatricula <> 'EX' and (matriculaperiodo.situacaomatriculaperiodo = 'AT' or matriculaperiodo.situacaomatriculaperiodo = 'CO') ");
		sb.append(" group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo , turma.identificadorturma order by pessoa.nome) ");
		sb.append(" union all ");
		// CANCELADO
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, 0 as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, count(matricula.matricula) as qtdmatcancelado ,0 as qtdmatextensao, 0 as qtdmatdocpend ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and matricula.situacao = 'CA' group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo , turma.identificadorturma order by pessoa.nome) ");
		sb.append(" union all ");
		// EXTENSAO
		// sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, 0 as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, 0 as qtdmatcancelado, count(matricula.matricula) as qtdmatextensao , 0 as qtdmatdocpend ");
		// sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma ");
		// sb.append(" where consultor is not null ");
		// sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		// sb.append(" and matricula.situacao = 'AT' and matricula.tipomatricula = 'EX'  ");
		// sb.append(" group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo , turma.identificadorturma order by pessoa.nome) ");
		// sb.append(" union all ");
		// EXCLUIDAS
		sb.append(" (select logexclusaomatricula.codunidadeensino as codunidade, logexclusaomatricula.codconsultor as codconsultor, logexclusaomatricula.nomeconsultor as consultor, logexclusaomatricula.codturma as codturma, logexclusaomatricula.turma, 0 as qtdmat, 0 as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, 0 as qtdmatcancelado, count(logexclusaomatricula.matricula) as qtdmatextensao ,0 as qtdmatdocpend   ");
		sb.append(" from logexclusaomatricula  ");
		sb.append(" where logexclusaomatricula.dataexclusao::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and logexclusaomatricula.dataexclusao::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and logexclusaomatricula.codturma is not null ");
		sb.append(" group by logexclusaomatricula.codunidadeensino, logexclusaomatricula.matricula, logexclusaomatricula.unidadeensino, logexclusaomatricula.codconsultor, logexclusaomatricula.nomeconsultor, logexclusaomatricula.codturma , logexclusaomatricula.turma order by logexclusaomatricula.nomeconsultor) ");
		sb.append(" union all ");
		// DOC PENDENTE
		sb.append(" (select matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma, 0 as qtdmat, 0 as qtdmatareceber, 0 as qtdmatvencidas, 0 as qtdmatavencer, 0 as qtdmatpre, 0 as qtdmatativo, 0 as qtdmatcancelado, 0 as qtdmatextensao, count(matricula.matricula) as qtdmatdocpend ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma inner join documetacaomatricula on documetacaomatricula.matricula = matricula.matricula ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and documetacaomatricula.situacao = 'PE' and matricula.situacao = 'AT' ");
		sb.append(" group by matricula.matricula, matricula.unidadeensino, matricula.consultor, pessoa.nome, turma.codigo, turma.codigo , turma.identificadorturma order by pessoa.nome)) as t ");
		sb.append(" inner join turma on turma.codigo = codturma ");
		sb.append(" inner join curso on curso.codigo = turma.curso ");
		sb.append(" where 1=1 ");
		if (codTurma > 0) {
			sb.append(" and codturma = ").append(codTurma);
		}
		if (codCurso > 0) {
			sb.append(" and curso.codigo = ").append(codCurso);
		}
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "turma.unidadeensino"));
		sb.append(" group by  t.identificadorturma, codconsultor, consultor, codunidade,  codturma");
		sb.append(" order by consultor, t.identificadorturma ");
		////System.out.print(sb.toString());

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		painelGestorVO.setGraficoMonitoramentoDocumentacaoConsultor("");
		painelGestorVO.setGraficoMonitoramentoFinanceiroConsultor("");
		painelGestorVO.setGraficoMonitoramentoMatriculaAtivaConsultor("");
		painelGestorVO.setGraficoMonitoramentoSituacaoAcademicaConsultor("");
		painelGestorVO.setQtdMatAReceberMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatAtivoMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatAVencerMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatCanceladoMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatExcluidoMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatExtensaoMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatPendenciaDocMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatPreMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatRecebidaMonitoramentoConsultor(0);
		painelGestorVO.setQtdMatVencidaMonitoramentoConsultor(0);
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorVO pg = new PainelGestorMonitoramentoConsultorVO();
			montarDadosPainelGestorMonitoramentoConsultorVO(painelGestorVO, pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().add(pg);
		}

		criarGraficoPainelGestorMonitoramentoConsultorSituacaoAcademica(painelGestorVO);
		criarGraficoPainelGestorMonitoramentoConsultorDocumentacaoPendente(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, codCurso, codTurma);
		criarGraficoPainelGestorMonitoramentoConsultorSituacaoFinanceira(painelGestorVO);
		criarGraficoPainelGestorMonitoramentoMatriculadoAtivoPorConsultor(painelGestorVO);

	}

	public void criarGraficoPainelGestorMonitoramentoMatriculadoAtivoPorConsultor(PainelGestorVO painelGestorVO) {
		painelGestorVO.setGraficoMonitoramentoMatriculaAtivaConsultor("");
		Integer qtdeTotal = 0;
		Integer valor = 0;
		Map<String, Integer> resultado = new HashMap<String, Integer>(0);
		for (PainelGestorMonitoramentoConsultorVO pg : painelGestorVO.getPainelGestorMonitoramentoConsultorVOs()) {
			valor = pg.getQtdMatAtivo();
			qtdeTotal += pg.getQtdMatAtivo();
			if (resultado.containsKey(pg.getNomeResumidoNomeSobrenome())) {
				valor += resultado.get(pg.getNomeResumidoNomeSobrenome());
			}
			if (valor > 0) {
				resultado.put(pg.getNomeResumidoNomeSobrenome(), valor);
			}
		}

		boolean virgula = false;
		for (String key : resultado.keySet()) {
			if (virgula) {
				painelGestorVO.setGraficoMonitoramentoMatriculaAtivaConsultor(painelGestorVO.getGraficoMonitoramentoMatriculaAtivaConsultor() + ", ");
			}
			painelGestorVO.setGraficoMonitoramentoMatriculaAtivaConsultor(painelGestorVO.getGraficoMonitoramentoMatriculaAtivaConsultor() + "['" + key + "'," + Uteis.arrendondarForcando2CadasDecimais((resultado.get(key).doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + resultado.get(key) + "'] ");
			virgula = true;
		}

	}

	public void criarGraficoPainelGestorMonitoramentoConsultorSituacaoAcademica(PainelGestorVO painelGestorVO) {
		Integer qtdeTotal = painelGestorVO.getQtdMatPreMonitoramentoConsultor() + painelGestorVO.getQtdMatAtivoMonitoramentoConsultor() + painelGestorVO.getQtdMatCanceladoMonitoramentoConsultor();
		painelGestorVO.setGraficoMonitoramentoSituacaoAcademicaConsultor("");
		if (qtdeTotal > 0) {
			painelGestorVO.setGraficoMonitoramentoSituacaoAcademicaConsultor(painelGestorVO.getGraficoMonitoramentoSituacaoAcademicaConsultor() + "['Pré-Matr.'," + Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getQtdMatPreMonitoramentoConsultor().doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + painelGestorVO.getQtdMatPreMonitoramentoConsultor() + "'], ");
			painelGestorVO.setGraficoMonitoramentoSituacaoAcademicaConsultor(painelGestorVO.getGraficoMonitoramentoSituacaoAcademicaConsultor() + "['Ativas'," + Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getQtdMatAtivoMonitoramentoConsultor().doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + painelGestorVO.getQtdMatAtivoMonitoramentoConsultor() + "'], ");
			painelGestorVO.setGraficoMonitoramentoSituacaoAcademicaConsultor(painelGestorVO.getGraficoMonitoramentoSituacaoAcademicaConsultor() + "['Canceladas'," + Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getQtdMatCanceladoMonitoramentoConsultor().doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + painelGestorVO.getQtdMatCanceladoMonitoramentoConsultor() + "'], ");
			painelGestorVO.setGraficoMonitoramentoSituacaoAcademicaConsultor(painelGestorVO.getGraficoMonitoramentoSituacaoAcademicaConsultor() + "['Extensão'," + Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getQtdMatExtensaoMonitoramentoConsultor().doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + painelGestorVO.getQtdMatExtensaoMonitoramentoConsultor() + "'] ");
		}
	}

	public void criarGraficoPainelGestorMonitoramentoConsultorDocumentacaoPendente(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codCurso, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select count(matricula.matricula) as qtdmatdocpend, tipodocumento.nome ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma inner join documetacaomatricula on documetacaomatricula.matricula = matricula.matricula ");
		sb.append(" inner join tipodocumento on documetacaomatricula.tipodedocumento = tipodocumento.codigo ");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and documetacaomatricula.situacao = 'PE' and matricula.situacao = 'AT' ");
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		if (codCurso > 0) {
			sb.append(" and curso.codigo = ").append(codCurso);
		}
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		sb.append(" group by  tipodocumento.nome order by tipodocumento.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.setGraficoMonitoramentoDocumentacaoConsultor("");
		Map<String, Integer> resultado = new HashMap<String, Integer>(0);
		Integer qtdeTotal = 0;
		while (rs.next()) {
			resultado.put(rs.getString("nome"), rs.getInt("qtdmatdocpend"));
			qtdeTotal += rs.getInt("qtdmatdocpend");

		}
		boolean virgula = false;
		for (String key : resultado.keySet()) {
			if (virgula) {
				painelGestorVO.setGraficoMonitoramentoDocumentacaoConsultor(painelGestorVO.getGraficoMonitoramentoDocumentacaoConsultor() + ", ");
			}
			painelGestorVO.setGraficoMonitoramentoDocumentacaoConsultor(painelGestorVO.getGraficoMonitoramentoDocumentacaoConsultor() + "['" + key + "'," + Uteis.arrendondarForcando2CadasDecimais((resultado.get(key).doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + resultado.get(key) + "'] ");
			virgula = true;
		}

	}

	public void criarGraficoPainelGestorMonitoramentoConsultorSituacaoFinanceira(PainelGestorVO painelGestorVO) {
		Integer qtdeTotal = painelGestorVO.getQtdMatRecebidaMonitoramentoConsultor() + painelGestorVO.getQtdMatAVencerMonitoramentoConsultor() + painelGestorVO.getQtdMatVencidaMonitoramentoConsultor();
		painelGestorVO.setGraficoMonitoramentoFinanceiroConsultor("");
		if (qtdeTotal > 0) {
			painelGestorVO.setGraficoMonitoramentoFinanceiroConsultor(painelGestorVO.getGraficoMonitoramentoFinanceiroConsultor() + "['Recebidas'," + Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getQtdMatRecebidaMonitoramentoConsultor().doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + painelGestorVO.getQtdMatRecebidaMonitoramentoConsultor() + "'], ");
			painelGestorVO.setGraficoMonitoramentoFinanceiroConsultor(painelGestorVO.getGraficoMonitoramentoFinanceiroConsultor() + "['Vencidas'," + Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getQtdMatVencidaMonitoramentoConsultor().doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + painelGestorVO.getQtdMatVencidaMonitoramentoConsultor() + "'], ");
			painelGestorVO.setGraficoMonitoramentoFinanceiroConsultor(painelGestorVO.getGraficoMonitoramentoFinanceiroConsultor() + "['A Vencer'," + Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getQtdMatAVencerMonitoramentoConsultor().doubleValue() * 100) / qtdeTotal.doubleValue()) + ", '" + painelGestorVO.getQtdMatAVencerMonitoramentoConsultor() + "'] ");
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatPendDoc(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select matricula.matricula, aluno.nome as aluno,  matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma inner join documetacaomatricula on documetacaomatricula.matricula = matricula.matricula ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and documetacaomatricula.situacao = 'PE' and matricula.situacao = 'AT' ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome ,  matricula.unidadeensino , matricula.consultor ,  pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatExt(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select logexclusaomatricula.matricula, logexclusaomatricula.aluno, logexclusaomatricula.codunidadeensino as codunidade, logexclusaomatricula.codconsultor as codconsultor, logexclusaomatricula.nomeconsultor as consultor, logexclusaomatricula.codturma as codturma, logexclusaomatricula.turma as identificadorturma ");
		sb.append(" from logexclusaomatricula  ");
		sb.append(" where logexclusaomatricula.dataexclusao::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and logexclusaomatricula.dataexclusao::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and logexclusaomatricula.codturma is not null ");
		if (codConsultor > 0) {
			sb.append(" and logexclusaomatricula.codconsultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and logexclusaomatricula.codturma = ").append(codTurma);
		}
		sb.append(" group by logexclusaomatricula.matricula, logexclusaomatricula.aluno, logexclusaomatricula.codunidadeensino , logexclusaomatricula.codconsultor , logexclusaomatricula.nomeconsultor , logexclusaomatricula.codturma , logexclusaomatricula.turma   ");
		sb.append(" order by logexclusaomatricula.aluno ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatCanc(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select matricula.matricula, aluno.nome as aluno, matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and matricula.situacao = 'CA' ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome , matricula.unidadeensino , matricula.consultor , pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatAT(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select matricula.matricula, aluno.nome as aluno, matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and matricula.situacao = 'AT' and matricula.tipomatricula <> 'EX' and (matriculaperiodo.situacaomatriculaperiodo = 'AT' or matriculaperiodo.situacaomatriculaperiodo = 'CO') ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome , matricula.unidadeensino , matricula.consultor , pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorPreMat(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select matricula.matricula, aluno.nome as aluno, matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and matricula.situacao = 'AT' and matriculaperiodo.situacaomatriculaperiodo = 'PR' ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome , matricula.unidadeensino , matricula.consultor , pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatAVencer(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select matricula.matricula, aluno.nome as aluno, matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'AR' and tipoOrigem = 'MAT' and contareceber.datavencimento >= current_date and matricula.situacao = 'AT'  ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome , matricula.unidadeensino , matricula.consultor , pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatVencida(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select matricula.matricula, aluno.nome as aluno, matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'AR' and tipoOrigem = 'MAT' and contareceber.datavencimento < current_date ");
		sb.append(" and matricula.situacao = 'AT' ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome , matricula.unidadeensino , matricula.consultor , pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatARec(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		// RECEBIDAS
		sb.append(" select matricula.matricula, aluno.nome as aluno, matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma from matricula  ");
		sb.append(" inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'AR' and tipoOrigem = 'MAT' and matricula.situacao = 'AT' ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome , matricula.unidadeensino , matricula.consultor , pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoConsultorMatRec(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codConsultor, Integer codTurma) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		// RECEBIDAS
		sb.append(" select matricula.matricula, aluno.nome as aluno, matricula.unidadeensino as codunidade, matricula.consultor as codconsultor, pessoa.nome as consultor, turma.codigo as codturma, turma.identificadorturma ");
		sb.append(" from matricula inner join funcionario on funcionario.codigo = matricula.consultor inner join pessoa on pessoa.codigo = funcionario.pessoa inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula inner join turma on turma.codigo = matriculaperiodo.turma left join contareceber on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sb.append(" where consultor is not null ");
		sb.append(" and matricula.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and matricula.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and contareceber.situacao = 'RE' and tipoOrigem = 'MAT' and matricula.situacao = 'AT' ");
		if (codConsultor > 0) {
			sb.append(" and matricula.consultor = ").append(codConsultor);
		}
		if (codTurma > 0) {
			sb.append(" and turma.codigo = ").append(codTurma);
		}
		sb.append(" group by matricula.matricula, aluno.nome , matricula.unidadeensino , matricula.consultor , pessoa.nome , turma.codigo , turma.identificadorturma   ");
		sb.append(" order by aluno.nome ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.getPainelGestorMonitoramentoConsultorVOs().clear();
		while (rs.next()) {
			PainelGestorMonitoramentoConsultorDetalhamentoVO pg = new PainelGestorMonitoramentoConsultorDetalhamentoVO();
			montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(pg, rs);
			painelGestorVO.getPainelGestorMonitoramentoConsultorDetalhamentoVOs().add(pg);
		}
	}

	public void consultarDadosPainelGestorMonitoramentoDesconto(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		// Esta uni os 2 selects abaixo
		sb.append(" select niveleducacional,ano::INT, mes::INT, ");
		sb.append(" sum(descontoprogressivo::NUMERIC(20,2)) as descontoprogressivo, ");
		sb.append(" sum(descontoinstituicao::NUMERIC(20,2)) as descontoinstituicao, ");
		sb.append(" sum(descontoAluno::NUMERIC(20,2))  as descontoAluno,");
		sb.append(" sum(descontorateio::NUMERIC(20,2))  as descontoRateio,");
		sb.append(" sum(descontoConvenio::NUMERIC(20,2)) as descontoConvenio, ");
		sb.append(" sum(descontoRecebimento::NUMERIC(20,2)) as descontoRecebimento, ");
		sb.append(" sum(valor::NUMERIC(20,2))  as valor, ");
		sb.append(" sum(valorrecebido::NUMERIC(20,2)) as valorrecebido ");
		sb.append(" from (");
		// Esta traz os valores recebidos no período
		sb.append(" select niveleducacional, ");
		sb.append(" case when(descontoConvenio is not null) then descontoConvenio::NUMERIC(20,2) else 0.0 end as descontoConvenio, ");
		sb.append(" case when(descontoinstituicao is not null) then descontoinstituicao::NUMERIC(20,2) else 0.0 end as descontoinstituicao,");
		sb.append(" case when(valordescontoalunojacalculado is not null) then valordescontoalunojacalculado::NUMERIC(20,2) else 0.0 end as descontoAluno, ");
		sb.append(" case when(valorcalculadodescontolancadorecebimento is not null) then valorcalculadodescontolancadorecebimento::NUMERIC(20,2) else 0.0 end as descontoRecebimento,");
		sb.append(" valordescontorateio as descontoRateio,");
		sb.append(" case when(valordescontoprogressivo is not null and descontoprogressivoutilizado != 'Nenhum') then valordescontoprogressivo::NUMERIC(20,2) else 0.0 end as descontoprogressivo, ");
		
		sb.append(" 0.0 as valor, valorrecebido,");
		sb.append(" extract(year from negociacaorecebimento.data) as ano, extract(month from negociacaorecebimento.data) as mes");
		sb.append(" from contareceber");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo   ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" where contareceber.situacao='RE' and negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));

		sb.append(" union all");
		// Esta união traz os valores faturados no período
		sb.append(" select niveleducacional,  0.0 as descontoConvenio, 0.0 as descontoinstituicao, 0.0 as descontoAluno, ");
		sb.append(" 0.0 as descontoRecebimento, 0.0 as descontoRateio, 0.0 as descontoprogressivo, valor,0.0 as  valorrecebido, extract(year from dataVencimento) as ano, ");
		sb.append(" extract(month from dataVencimento) as mes ");
		sb.append(" from contareceber ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" where dataVencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and dataVencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("'  and contareceber.situacao <> 'NE'");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		sb.append(" ) as t group by niveleducacional, ano, mes order by ano, mes, niveleducacional");
		painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVOs().clear();
		painelGestorVO.setTotalFaturadoPeriodoMonitoramentoDesconto(0.0);
		painelGestorVO.setTotalRecebidoPeriodoMonitoramentoDesconto(0.0);
		painelGestorVO.setTotalDescontoAluno(0.0);
		painelGestorVO.setTotalDescontoConvenio(0.0);
		painelGestorVO.setTotalDescontoInstituicao(0.0);
		painelGestorVO.setTotalDescontoPeriodo(0.0);
		painelGestorVO.setTotalDescontoProgressivo(0.0);
		painelGestorVO.setTotalDescontoRecebimento(0.0);
		painelGestorVO.setTotalDescontoRateio(0.0);
		painelGestorVO.setTotalAlunoReceberamDesconto(0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoMesAnoVO(painelGestorVO, rs);
		}
	}

	public PainelGestorMonitoramentoDescontoMesAnoVO consultarObjPainelGestorMonitoramentoDescontoMesAnoVOs(PainelGestorVO painelGestorVO, String mesAno) {
		for (PainelGestorMonitoramentoDescontoMesAnoVO painelGestorMonitoramentoDescontoMesAnoVO : painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVOs()) {
			if (painelGestorMonitoramentoDescontoMesAnoVO.getMesAno().equals(mesAno)) {
				return painelGestorMonitoramentoDescontoMesAnoVO;
			}
		}
		PainelGestorMonitoramentoDescontoMesAnoVO painelGestorMonitoramentoDescontoMesAnoVO = new PainelGestorMonitoramentoDescontoMesAnoVO();
		painelGestorMonitoramentoDescontoMesAnoVO.setMesAno(mesAno);
		painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVOs().add(painelGestorMonitoramentoDescontoMesAnoVO);
		return painelGestorMonitoramentoDescontoMesAnoVO;
	}

	public void montarDadosPainelGestorMonitoramentoDescontoMesAnoVO(PainelGestorVO painelGestorVO, SqlRowSet rs) {
		PainelGestorMonitoramentoDescontoMesAnoVO painelGestorMonitoramentoDescontoMesAnoVO = consultarObjPainelGestorMonitoramentoDescontoMesAnoVOs(painelGestorVO, rs.getInt("mes") + "/" + rs.getInt("ano"));
		painelGestorMonitoramentoDescontoMesAnoVO.setValorFaturadoMes(painelGestorMonitoramentoDescontoMesAnoVO.getValorFaturadoMes() + rs.getDouble("valor"));
		painelGestorMonitoramentoDescontoMesAnoVO.setValorRecebidoMes(painelGestorMonitoramentoDescontoMesAnoVO.getValorRecebidoMes() + rs.getDouble("valorrecebido"));
		painelGestorMonitoramentoDescontoMesAnoVO.setTotalDescontoAluno(painelGestorMonitoramentoDescontoMesAnoVO.getTotalDescontoAluno() + rs.getDouble("descontoaluno"));
		painelGestorMonitoramentoDescontoMesAnoVO.setTotalDescontoProgressivo(painelGestorMonitoramentoDescontoMesAnoVO.getTotalDescontoProgressivo() + rs.getDouble("descontoprogressivo"));
		painelGestorMonitoramentoDescontoMesAnoVO.setTotalDescontoRateio(painelGestorMonitoramentoDescontoMesAnoVO.getTotalDescontoRateio() + rs.getDouble("descontoRateio"));
		painelGestorMonitoramentoDescontoMesAnoVO.setTotalDescontoConvenio(painelGestorMonitoramentoDescontoMesAnoVO.getTotalDescontoConvenio() + rs.getDouble("descontoconvenio"));
		painelGestorMonitoramentoDescontoMesAnoVO.setTotalDescontoInstituicao(painelGestorMonitoramentoDescontoMesAnoVO.getTotalDescontoInstituicao() + rs.getDouble("descontoinstituicao"));
		painelGestorMonitoramentoDescontoMesAnoVO.setTotalDescontoRecebimento(painelGestorMonitoramentoDescontoMesAnoVO.getTotalDescontoRecebimento() + rs.getDouble("descontoRecebimento"));
		painelGestorMonitoramentoDescontoMesAnoVO.setTotalDescontoMes(painelGestorMonitoramentoDescontoMesAnoVO.getTotalDescontoMes() + rs.getDouble("descontoaluno") + rs.getDouble("descontoprogressivo") + rs.getDouble("descontoconvenio") + rs.getDouble("descontoinstituicao") + rs.getDouble("descontoRecebimento") + rs.getDouble("descontoRateio"));

		painelGestorVO.setTotalFaturadoPeriodoMonitoramentoDesconto(painelGestorVO.getTotalFaturadoPeriodoMonitoramentoDesconto() + rs.getDouble("valor"));
		painelGestorVO.setTotalRecebidoPeriodoMonitoramentoDesconto(painelGestorVO.getTotalRecebidoPeriodoMonitoramentoDesconto() + rs.getDouble("valorrecebido"));
		painelGestorVO.setTotalDescontoAluno(painelGestorVO.getTotalDescontoAluno() + rs.getDouble("descontoaluno"));
		painelGestorVO.setTotalDescontoProgressivo(painelGestorVO.getTotalDescontoProgressivo() + rs.getDouble("descontoprogressivo"));
		painelGestorVO.setTotalDescontoConvenio(painelGestorVO.getTotalDescontoConvenio() + rs.getDouble("descontoconvenio"));
		painelGestorVO.setTotalDescontoInstituicao(painelGestorVO.getTotalDescontoInstituicao() + rs.getDouble("descontoinstituicao"));
		painelGestorVO.setTotalDescontoRateio(painelGestorVO.getTotalDescontoRateio() + rs.getDouble("descontorateio"));
		painelGestorVO.setTotalDescontoRecebimento(painelGestorVO.getTotalDescontoRecebimento() + rs.getDouble("descontoRecebimento"));
		painelGestorVO.setTotalDescontoPeriodo(painelGestorVO.getTotalDescontoPeriodo() + rs.getDouble("descontoaluno") + rs.getDouble("descontoprogressivo") + rs.getDouble("descontoconvenio") + rs.getDouble("descontoinstituicao") + rs.getDouble("descontoRecebimento") + rs.getDouble("descontoRateio"));
		if (rs.getString("niveleducacional") != null && !rs.getString("niveleducacional").equals("")) {
			painelGestorMonitoramentoDescontoMesAnoVO.getPainelGestorMonitoramentoDescontoNivelEducacionalVOs().add(0, montarDadosPainelGestorMonitoramentoDescontoNivelEducacionalVO(rs));
		} else {
			painelGestorMonitoramentoDescontoMesAnoVO.getPainelGestorMonitoramentoDescontoNivelEducacionalVOs().add(painelGestorMonitoramentoDescontoMesAnoVO.getQuantidadeNiveis(), montarDadosPainelGestorMonitoramentoDescontoNivelEducacionalVO(rs));
		}
	}

	public void montarDadosPainelGestorMonitoramentoConsultorVO(PainelGestorVO painelGestorVO, PainelGestorMonitoramentoConsultorVO p, SqlRowSet rs) {
		p.setCodConsultor(rs.getInt("codConsultor"));
		p.setConsultor(rs.getString("consultor"));
		p.setTurma(rs.getString("identificadorturma"));
		p.setCodTurma(rs.getInt("codTurma"));
		p.setQtdMatRecebida(rs.getInt("qtdMat"));
		p.setQtdMatAReceber(rs.getInt("qtdMatAReceber"));
		p.setQtdMatVencida(rs.getInt("qtdMatVencidas"));
		p.setQtdMatAVencer(rs.getInt("qtdMatAVencer"));
		p.setQtdMatPre(rs.getInt("qtdMatPre"));
		p.setQtdMatAtivo(rs.getInt("qtdMatAtivo"));
		p.setQtdMatCancelado(rs.getInt("qtdMatCancelado"));
		p.setQtdMatExtensao(rs.getInt("qtdMatExtensao"));
		p.setQtdMatPendenciaDoc(rs.getInt("qtdMatDocPend"));

		painelGestorVO.setQtdMatAReceberMonitoramentoConsultor(painelGestorVO.getQtdMatAReceberMonitoramentoConsultor() + rs.getInt("qtdMatAReceber"));
		painelGestorVO.setQtdMatAtivoMonitoramentoConsultor(painelGestorVO.getQtdMatAtivoMonitoramentoConsultor() + rs.getInt("qtdMatAtivo"));
		painelGestorVO.setQtdMatAVencerMonitoramentoConsultor(painelGestorVO.getQtdMatAVencerMonitoramentoConsultor() + rs.getInt("qtdMatAVencer"));
		painelGestorVO.setQtdMatCanceladoMonitoramentoConsultor(painelGestorVO.getQtdMatCanceladoMonitoramentoConsultor() + rs.getInt("qtdMatCancelado"));
		painelGestorVO.setQtdMatExtensaoMonitoramentoConsultor(painelGestorVO.getQtdMatExtensaoMonitoramentoConsultor() + rs.getInt("qtdMatExtensao"));
		painelGestorVO.setQtdMatPendenciaDocMonitoramentoConsultor(painelGestorVO.getQtdMatPendenciaDocMonitoramentoConsultor() + rs.getInt("qtdMatDocPend"));
		painelGestorVO.setQtdMatPreMonitoramentoConsultor(painelGestorVO.getQtdMatPreMonitoramentoConsultor() + rs.getInt("qtdMatPre"));
		painelGestorVO.setQtdMatRecebidaMonitoramentoConsultor(painelGestorVO.getQtdMatRecebidaMonitoramentoConsultor() + rs.getInt("qtdMat"));
		painelGestorVO.setQtdMatVencidaMonitoramentoConsultor(painelGestorVO.getQtdMatVencidaMonitoramentoConsultor() + rs.getInt("qtdMatVencidas"));

	}

	public void montarDadosPainelGestorMonitoramentoConsultorDetalhamentoVO(PainelGestorMonitoramentoConsultorDetalhamentoVO p, SqlRowSet rs) {
		p.setMatricula(rs.getString("matricula"));
		p.setAluno(rs.getString("aluno"));
	}

	public PainelGestorMonitoramentoDescontoNivelEducacionalVO montarDadosPainelGestorMonitoramentoDescontoNivelEducacionalVO(SqlRowSet rs) {
		PainelGestorMonitoramentoDescontoNivelEducacionalVO obj = new PainelGestorMonitoramentoDescontoNivelEducacionalVO();
		obj.setNivel(rs.getString("niveleducacional"));
		obj.setMesAno(rs.getInt("mes") + "/" + rs.getInt("ano"));
		obj.setValorFaturado(rs.getDouble("valor"));
		obj.setValorRecebido(rs.getDouble("valorrecebido"));
		obj.setTotalDescontoAluno(rs.getDouble("descontoaluno"));
		obj.setTotalDescontoProgressivo(rs.getDouble("descontoprogressivo"));
		obj.setTotalDescontoConvenio(rs.getDouble("descontoconvenio"));
		obj.setTotalDescontoRateio(rs.getDouble("descontorateio"));
		obj.setTotalDescontoInstituicao(rs.getDouble("descontoinstituicao"));
		obj.setTotalDescontoRecebimento(rs.getDouble("descontoRecebimento"));
		obj.setTotalDesconto(rs.getDouble("descontoaluno") + rs.getDouble("descontoprogressivo") + rs.getDouble("descontoconvenio") + rs.getDouble("descontoinstituicao") + rs.getDouble("descontoRecebimento") + rs.getDouble("descontoRateio"));

		return obj;
	}

	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicao(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (mesAno != null && !mesAno.trim().isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosDetalhamentoDescontoInstituicao(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, cursoVO, tipoNivelEducacional);
	}

	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoInstituicaoTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (mesAno != null && !mesAno.trim().isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosDetalhamentoDescontoInstituicaoTurma(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, cursoVO, turmaVO, tipoNivelEducacional);
	}

	public void consultarDadosDetalhamentoDescontoInstituicao(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();
		sb.append(" select planodesconto,sum(valor::NUMERIC(20,2)) as valor, count(distinct matricula)::INT as qtdePessoa, sum(descontoinstituicao::NUMERIC(20,2)) as descontoinstituicao");
		sb.append(" from ( ");
		sb.append(" select distinct  planodesconto.nome as planodesconto,  matricula.matricula, 0.0 valor, ");
		sb.append(" case when(valorutilizadorecebimento is not null) then valorutilizadorecebimento else 0.0 end as descontoinstituicao,");
		sb.append(" contareceber.codigo from contareceber ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo   ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo");
		sb.append(" inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto");
		sb.append(" where contareceber.situacao='RE' and valorutilizadorecebimento is not null and valorutilizadorecebimento > 0.0 and negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("'  ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}
		sb.append(" union all");
		sb.append(" select distinct  planodesconto.nome as planodesconto,  matricula.matricula, contareceber.valor, ");
		sb.append(" 0.0 as descontoinstituicao,");
		sb.append(" contareceber.codigo from contareceber ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo");
		sb.append(" inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto");
		sb.append(" where contareceber.situacao != 'NE' and contareceber.dataVencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.dataVencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("'  ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}

		sb.append(" ) as t group by planodesconto order by descontoinstituicao desc, planodesconto");
		painelGestorVO.getPainelGestorMonitoramentoDescontoInstituicaoVOs().clear();
		painelGestorVO.setTotalFaturadoComDescontoInstituicao(0.0);
		painelGestorVO.setTotalDescontoDetalhamentoInstituicao(0.0);
		painelGestorVO.setNumeroAlunoComDescontoInstituicao(0);
		painelGestorVO.setPercentualDescontoRelacaoFaturado(0.0);
		painelGestorVO.setPercentualDescontoRelacaoTotalDesconto(0.0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoInstituicao(painelGestorVO, rs);
		}
		executarCalculoPercentualMonitoramentoDescontoInstituicao(painelGestorVO);
	}

	public void consultarDadosDetalhamentoDescontoInstituicaoTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();
		sb.append(" select planodesconto,sum(valor::NUMERIC(20,2)) as valor, count(distinct matricula)::INT as qtdePessoa, sum(descontoinstituicao::NUMERIC(20,2)) as descontoinstituicao");
		sb.append(" from ( ");
		sb.append(" select planodesconto.nome as planodesconto,  matricula.matricula, 0.0 valor, ");
		sb.append(" case when(valorutilizadorecebimento is not null) then valorutilizadorecebimento else 0.0 end as descontoinstituicao,");
		sb.append(" contareceber.codigo from contareceber ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo   ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo");
		sb.append(" inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto");
		sb.append(" where contareceber.situacao='RE' and valorutilizadorecebimento is not null and valorutilizadorecebimento > 0.0 and negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("'  ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}
		if (turmaVO != null && turmaVO.getCodigo() > 0) {
			sb.append(" and matriculaperiodo.turma = ").append(turmaVO.getCodigo());
		}

		sb.append(" union all ");
		sb.append(" select planodesconto.nome as planodesconto,  matricula.matricula, contareceber.valor, ");
		sb.append(" 0.0 as descontoinstituicao,");
		sb.append(" contareceber.codigo from contareceber ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo");
		sb.append(" inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto");
		sb.append(" where contareceber.situacao != 'NE' and contareceber.dataVencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.dataVencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("'  ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}
		if (turmaVO != null && turmaVO.getCodigo() > 0) {
			sb.append(" and matriculaperiodo.turma = ").append(turmaVO.getCodigo());
		}

		sb.append(" ) as t group by planodesconto order by descontoinstituicao desc, planodesconto");
		painelGestorVO.getPainelGestorMonitoramentoDescontoInstituicaoVOs().clear();
		painelGestorVO.setTotalFaturadoComDescontoInstituicao(0.0);
		painelGestorVO.setTotalDescontoDetalhamentoInstituicao(0.0);
		painelGestorVO.setNumeroAlunoComDescontoInstituicao(0);
		painelGestorVO.setPercentualDescontoRelacaoFaturado(0.0);
		painelGestorVO.setPercentualDescontoRelacaoTotalDesconto(0.0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoInstituicao(painelGestorVO, rs);
		}
		executarCalculoPercentualMonitoramentoDescontoInstituicao(painelGestorVO);
	}

	public void executarCalculoPercentualMonitoramentoDescontoInstituicao(PainelGestorVO painelGestorVO) {
		Double totalPercentualDesconto = 0.0;
		for (PainelGestorMonitoramentoDescontoInstituicaoVO pGestorMonitoramentoDescontoInstituicaoVO : painelGestorVO.getPainelGestorMonitoramentoDescontoInstituicaoVOs()) {
			if (painelGestorVO.getTotalDescontoDetalhamentoInstituicao() != Double.NaN && painelGestorVO.getTotalDescontoDetalhamentoInstituicao() > 0) {
				pGestorMonitoramentoDescontoInstituicaoVO.setPercentualDescontoRelacaoTotalDesconto(Uteis.arrendondarForcando2CadasDecimais((pGestorMonitoramentoDescontoInstituicaoVO.getTotalDesconto() * 100) / painelGestorVO.getTotalDescontoDetalhamentoInstituicao()));
				totalPercentualDesconto = totalPercentualDesconto + Uteis.arrendondarForcando2CadasDecimais((pGestorMonitoramentoDescontoInstituicaoVO.getTotalDesconto() * 100) / painelGestorVO.getTotalDescontoDetalhamentoInstituicao());
			}
		}
		if (painelGestorVO.getTotalFaturadoComDescontoInstituicao() != Double.NaN && painelGestorVO.getTotalFaturadoComDescontoInstituicao() > 0) {
			painelGestorVO.setPercentualDescontoRelacaoFaturado(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalDescontoDetalhamentoInstituicao() * 100) / painelGestorVO.getTotalFaturadoComDescontoInstituicao()));
		}
		if (painelGestorVO.getPainelGestorMonitoramentoDescontoInstituicaoVOs().size() > 0) {
			painelGestorVO.setPercentualDescontoRelacaoTotalDesconto(Uteis.arrendondarForcando2CadasDecimais(totalPercentualDesconto / painelGestorVO.getPainelGestorMonitoramentoDescontoInstituicaoVOs().size()));
		}
	}

	public void montarDadosPainelGestorMonitoramentoDescontoInstituicao(PainelGestorVO painelGestorVO, SqlRowSet rs) throws Exception {
		PainelGestorMonitoramentoDescontoInstituicaoVO pGestorMonitoramentoDescontoInstituicaoVO = new PainelGestorMonitoramentoDescontoInstituicaoVO();
		pGestorMonitoramentoDescontoInstituicaoVO.setNomeTipoDesconto(rs.getString("planodesconto"));
		pGestorMonitoramentoDescontoInstituicaoVO.setTotalDesconto(rs.getDouble("descontoinstituicao"));
		pGestorMonitoramentoDescontoInstituicaoVO.setNumeroAlunoComDesconto(rs.getInt("qtdePessoa"));
		pGestorMonitoramentoDescontoInstituicaoVO.setValorFaturado(rs.getDouble("valor"));
		pGestorMonitoramentoDescontoInstituicaoVO.setPercentualDescontoRelacaoFaturado((rs.getDouble("descontoinstituicao") * 100) / rs.getDouble("valor"));
		painelGestorVO.setTotalDescontoDetalhamentoInstituicao(painelGestorVO.getTotalDescontoDetalhamentoInstituicao() + rs.getDouble("descontoinstituicao"));
		painelGestorVO.setNumeroAlunoComDescontoInstituicao(painelGestorVO.getNumeroAlunoComDescontoInstituicao() + rs.getInt("qtdePessoa"));
		painelGestorVO.setTotalFaturadoComDescontoInstituicao(painelGestorVO.getTotalFaturadoComDescontoInstituicao() + rs.getDouble("valor"));
		painelGestorVO.getPainelGestorMonitoramentoDescontoInstituicaoVOs().add(pGestorMonitoramentoDescontoInstituicaoVO);
	}

	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenio(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (mesAno != null && !mesAno.trim().isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosDetalhamentoDescontoConvenio(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, cursoVO, tipoNivelEducacional);
	}

	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoConvenioTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (mesAno != null && !mesAno.trim().isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosDetalhamentoDescontoConvenioTurma(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, cursoVO, turmaVO, tipoNivelEducacional);
	}

	public void consultarDadosDetalhamentoDescontoConvenio(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();
		sb.append(" select convenio, sum(valor::NUMERIC(20,2)) as valor, ");
		sb.append(" count(distinct matricula)::INT as qtdePessoa, sum(case when valorRecebido is null then 0.0 else valorRecebido::NUMERIC(20,2) end) as valorRecebido , sum(valorAtrazado::NUMERIC(20,2)) as valorAtrazado, sum(valordesconto::NUMERIC(20,2)) as totalDescontoRecebidoConvenio, sum(valorReceber::NUMERIC(20,2)) as valorReceber ");
		sb.append(" from (  ");
		sb.append(" select convenio.descricao as convenio,  matriculaaluno as matricula, contareceber.valor::NUMERIC(20,2) as valor,  contareceber.valorRecebido::NUMERIC(20,2) as valorRecebido, ");
		sb.append(" case when (contareceber.datavencimento < current_date and contareceber.situacao = 'AR') then contareceber.valor::NUMERIC(20,2) else 0.0 end as valorAtrazado, ");
		sb.append(" case when (contareceber.situacao = 'AR') then contareceber.valor::NUMERIC(20,2) else 0.0 end as valorReceber, ");
		sb.append(" case when contareceber.valordescontorecebido is null then 0.0 else valordescontorecebido::NUMERIC(20,2) end as valordesconto ");
		sb.append(" from contareceber ");
		sb.append(" inner join convenio on convenio.codigo = contareceber.convenio");
		sb.append(" inner join matricula on contareceber.matriculaaluno = matricula.matricula");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where contareceber.tipoOrigem = 'BCC' and contareceber.datavencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.datavencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' and contareceber.situacao != 'NE'  ");

		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}

		sb.append(" ) as t group by convenio order by valor desc, convenio");

		painelGestorVO.getPainelGestorMonitoramentoDescontoConvenioVOs().clear();
		painelGestorVO.setTotalFaturadoConvenio(0.0);
		painelGestorVO.setTotalMatriculaUtilizamConvenio(0);
		painelGestorVO.setPercentualFaturadoConvenioRelacaoTotalFaturadoValorCheio(0.0);
		painelGestorVO.setPercentualInadimplenciaConvenio(0.0);
		painelGestorVO.setTotalRecebidoConvenio(0.0);
		painelGestorVO.setTotalReceberConvenio(0.0);
		painelGestorVO.setTotalAtrazadoConvenio(0.0);
		painelGestorVO.setTotalDescontoRecebidoConvenio(0.0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		// Double valorFaturadoCheio =
		// (consultarValorFaturadoCheio(unidadeEnsinoVOs, dataInicio,
		// dataTermino, cursoVO, tipoNivelEducacional));
		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoConvenio(painelGestorVO, 0.0, rs);
		}
		for (PainelGestorMonitoramentoDescontoConvenioVO pgmdc : painelGestorVO.getPainelGestorMonitoramentoDescontoConvenioVOs()) {
			pgmdc.setPercentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio((pgmdc.getTotalFaturadoConvenio() * 100) / painelGestorVO.getTotalFaturadoConvenio());
		}

	}

	public void consultarDadosDetalhamentoDescontoConvenioTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();

		sb.append(" select convenio,sum(valor::NUMERIC(20,2)) as valor, ");
		sb.append(" count(distinct matricula)::INT as qtdePessoa, sum(valorRecebido::NUMERIC(20,2)) as valorRecebido , sum(valorAtrazado::NUMERIC(20,2)) as valorAtrazado, sum(valordesconto::NUMERIC(20,2)) as totalDescontoRecebidoConvenio, sum(valorReceber::NUMERIC(20,2)) as valorReceber ");
		sb.append(" from (  ");
		sb.append(" select convenio.descricao as convenio,  matriculaaluno as matricula, contareceber.valor::NUMERIC(20,2) as valor, ");
		sb.append(" contareceber.valorRecebido::NUMERIC(20,2) as valorRecebido, case when (contareceber.datavencimento < current_date and contareceber.situacao = 'AR') then contareceber.valor::NUMERIC(20,2) else 0.0 end as valorAtrazado, ");
		sb.append(" case when (contareceber.situacao = 'AR') then contareceber.valor::NUMERIC(20,2) else 0.0 end as valorReceber, ");
		sb.append(" case when contareceber.valordescontorecebido is null then 0.0 else valordescontorecebido::NUMERIC(20,2) end as valordesconto ");
		sb.append(" from contareceber ");
		sb.append(" inner join convenio on convenio.codigo = contareceber.convenio");
		sb.append(" inner join matricula on contareceber.matriculaaluno = matricula.matricula");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on curso.codigo = matricula.curso");
		sb.append(" where contareceber.tipoOrigem = 'BCC' and contareceber.datavencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.situacao<>'NE' and contareceber.datavencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");

		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}
		if (turmaVO != null && turmaVO.getCodigo() > 0) {
			sb.append(" and matriculaperiodo.turma = ").append(turmaVO.getCodigo());
		}

		sb.append(" ) as t group by convenio order by valor desc, convenio");

		painelGestorVO.getPainelGestorMonitoramentoDescontoConvenioVOs().clear();
		painelGestorVO.setTotalFaturadoConvenio(0.0);
		painelGestorVO.setTotalMatriculaUtilizamConvenio(0);
		painelGestorVO.setPercentualFaturadoConvenioRelacaoTotalFaturadoValorCheio(0.0);
		painelGestorVO.setPercentualInadimplenciaConvenio(0.0);
		painelGestorVO.setTotalRecebidoConvenio(0.0);
		painelGestorVO.setTotalReceberConvenio(0.0);
		painelGestorVO.setTotalDescontoRecebidoConvenio(0.0);
		painelGestorVO.setTotalAtrazadoConvenio(0.0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoConvenio(painelGestorVO, 0.0, rs);
		}
		for (PainelGestorMonitoramentoDescontoConvenioVO pgmdc : painelGestorVO.getPainelGestorMonitoramentoDescontoConvenioVOs()) {
			pgmdc.setPercentualFaturadoConvenioComRelacaoTotalFaturadoValorCheio((pgmdc.getTotalFaturadoConvenio() * 100) / painelGestorVO.getTotalFaturadoConvenio());
		}

	}

	public void montarDadosPainelGestorMonitoramentoDescontoConvenio(PainelGestorVO painelGestorVO, Double valorFaturadoCheio, SqlRowSet rs) {
		PainelGestorMonitoramentoDescontoConvenioVO painelGestorMonitoramentoDescontoConvenioVO = new PainelGestorMonitoramentoDescontoConvenioVO();
		painelGestorMonitoramentoDescontoConvenioVO.setConvenio(rs.getString("convenio"));
		painelGestorMonitoramentoDescontoConvenioVO.setNumeroMatriculaConvenio(rs.getInt("qtdePessoa"));
		painelGestorMonitoramentoDescontoConvenioVO.setTotalAtrazadoConvenio(rs.getDouble("valorAtrazado"));
		painelGestorMonitoramentoDescontoConvenioVO.setTotalFaturadoConvenio(rs.getDouble("valor"));
		painelGestorMonitoramentoDescontoConvenioVO.setTotalReceberConvenio(rs.getDouble("valorReceber"));
		painelGestorMonitoramentoDescontoConvenioVO.setTotalRecebidoConvenio(rs.getDouble("valorRecebido"));
		painelGestorMonitoramentoDescontoConvenioVO.setTotalDescontoRecebidoConvenio(rs.getDouble("totalDescontoRecebidoConvenio"));
		if (rs.getDouble("valor") > 0.0) {
			painelGestorMonitoramentoDescontoConvenioVO.setPercentualAtualInadimplenciaConvenio((rs.getDouble("valorAtrazado") * 100) / rs.getDouble("valor"));
		}

		painelGestorVO.setTotalFaturadoConvenio(painelGestorVO.getTotalFaturadoConvenio() + painelGestorMonitoramentoDescontoConvenioVO.getTotalFaturadoConvenio());
		painelGestorVO.setTotalMatriculaUtilizamConvenio(painelGestorVO.getTotalMatriculaUtilizamConvenio() + painelGestorMonitoramentoDescontoConvenioVO.getNumeroMatriculaConvenio());
		painelGestorVO.setTotalRecebidoConvenio(painelGestorVO.getTotalRecebidoConvenio() + painelGestorMonitoramentoDescontoConvenioVO.getTotalRecebidoConvenio());
		painelGestorVO.setTotalReceberConvenio(painelGestorVO.getTotalReceberConvenio() + painelGestorMonitoramentoDescontoConvenioVO.getTotalReceberConvenio());
		painelGestorVO.setTotalAtrazadoConvenio(painelGestorVO.getTotalAtrazadoConvenio() + painelGestorMonitoramentoDescontoConvenioVO.getTotalAtrazadoConvenio());
		painelGestorVO.setTotalDescontoRecebidoConvenio(painelGestorVO.getTotalDescontoRecebidoConvenio() + painelGestorMonitoramentoDescontoConvenioVO.getTotalDescontoRecebidoConvenio());
		painelGestorVO.getPainelGestorMonitoramentoDescontoConvenioVOs().add(painelGestorMonitoramentoDescontoConvenioVO);
	}

	public Double consultarValorFaturadoCheio(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {

		StringBuilder sb = new StringBuilder("select sum(valor::NUMERIC(20,2)) as valor from contareceber ");
		sb.append(" left join matricula on contareceber.matriculaaluno = matricula.matricula");
		sb.append(" left join curso on curso.codigo = matricula.curso");
		sb.append(" where contareceber.datavencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.situacao <> 'NE' and contareceber.datavencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}

		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getDouble("valor");
		}
		return 0.0;
	}

	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (mesAno != null && !mesAno.trim().isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorCurso(dataTermino);
		// painelGestorVO.setDataInicioPorCurso(dataInicio);
		consultarDadosDetalhamentoDescontoProgressivo(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, cursoVO, tipoNivelEducacional);
	}

	@Override
	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivoPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoTurma, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (mesAno != null && !mesAno.trim().isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosDetalhamentoDescontoProgressivoParaTurma(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, codigoTurma, tipoNivelEducacional);
	}

	public void consultarDadosDetalhamentoDescontoProgressivoParaTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, Integer codigoTurma, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();
		sb.append(" select curso, codigoCurso, turma, codigoTurma, ");
		sb.append(" sum(usouPrimeiroDesconto) as usouPrimeiroDesconto, ");
		sb.append(" sum(usouSegundoDesconto) as usouSegundoDesconto, ");
		sb.append(" sum(usouTerceiroDesconto) as usouTerceiroDesconto, ");
		sb.append(" sum(usouQuartoDesconto) as usouQuartoDesconto, ");
		sb.append(" sum(primeiroDesconto::NUMERIC(20,2)) as primeiroDesconto,  ");
		sb.append(" sum(segundoDesconto::NUMERIC(20,2)) as segundoDesconto,  ");
		sb.append(" sum(terceiroDesconto::NUMERIC(20,2)) as terceiroDesconto, ");
		sb.append(" sum(quartoDesconto::NUMERIC(20,2)) as quartoDesconto");
		sb.append(" from ( ");
		sb.append(" select curso.nome as curso, curso.codigo as codigoCurso, ");
		sb.append(" turma.identificadorTurma as turma, turma.codigo as codigoTurma, ");
		sb.append(" case when (descontoprogressivoutilizado = 'Primeiro' ) then valordescontoprogressivo else 0.0 end primeiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Segundo' ) then valordescontoprogressivo else 0.0 end segundoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Terceiro' ) then valordescontoprogressivo else 0.0 end terceiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Quarto' ) then valordescontoprogressivo else 0.0 end quartoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Primeiro' ) then 1 else 0 end usouPrimeiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Segundo' ) then 1 else 0 end usouSegundoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Terceiro' ) then 1 else 0 end usouTerceiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Quarto' ) then 1 else 0 end usouQuartoDesconto");
		sb.append(" from contareceber ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" where negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.situacao = 'RE' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");

		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (codigoTurma != null && codigoTurma > 0) {
			sb.append(" and turma.codigo = ").append(codigoTurma);
		}
		sb.append(" and valordescontoprogressivo is not null and valordescontoprogressivo >0.0  ");

		sb.append(" ) as t group by curso, codigoCurso, turma, codigoTurma order by turma");

		painelGestorVO.setValorTotalDescontoProgressivo(0.0);
		painelGestorVO.setTotalPrimeiroDescontoProgressivo(0.0);
		painelGestorVO.setTotalSegundoDescontoProgressivo(0.0);
		painelGestorVO.setTotalTerceiroDesconto(0.0);
		painelGestorVO.setTotalQuartoDescontoProgressivo(0.0);

		painelGestorVO.setTotalPessoasUsouDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouPrimeiroDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouSegundoDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouTerceiroDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouQuartoDescontoProgressivo(0);
		painelGestorVO.getPainelGestorMonitoramentoDescontoProgressivoVOs().clear();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoProgressivo(painelGestorVO, rs, tipoNivelEducacional, false);
		}

		executarCalculoPercentualMonitoramentoDescontoProgressivo(painelGestorVO);
	}

	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoProgressivoTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional, String mesAno) throws Exception {
		if (mesAno != null && !mesAno.trim().isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosDetalhamentoDescontoProgressivoTurma(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, cursoVO, turmaVO, tipoNivelEducacional);
	}

	public void consultarDadosDetalhamentoDescontoProgressivoTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TurmaVO turmaVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();
		sb.append(" select curso, codigoCurso, codigoTurma, turma, sum(usouPrimeiroDesconto) as usouPrimeiroDesconto, ");
		sb.append(" sum(usouSegundoDesconto) as usouSegundoDesconto, ");
		sb.append(" sum(usouTerceiroDesconto) as usouTerceiroDesconto, ");
		sb.append(" sum(usouQuartoDesconto) as usouQuartoDesconto, ");
		sb.append(" sum(primeiroDesconto::NUMERIC(20,2)) as primeiroDesconto,  ");
		sb.append(" sum(segundoDesconto::NUMERIC(20,2)) as segundoDesconto,  ");
		sb.append(" sum(terceiroDesconto::NUMERIC(20,2)) as terceiroDesconto, ");
		sb.append(" sum(quartoDesconto::NUMERIC(20,2)) as quartoDesconto");
		sb.append(" from ( ");
		sb.append(" select curso.nome as curso, curso.codigo as codigoCurso, turma.codigo as codigoTurma, turma.identificadorTurma as turma, ");
		sb.append(" case when (descontoprogressivoutilizado = 'Primeiro' ) then valordescontoprogressivo else 0.0 end primeiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Segundo' ) then valordescontoprogressivo else 0.0 end segundoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Terceiro' ) then valordescontoprogressivo else 0.0 end terceiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Quarto' ) then valordescontoprogressivo else 0.0 end quartoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Primeiro' ) then 1 else 0 end usouPrimeiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Segundo' ) then 1 else 0 end usouSegundoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Terceiro' ) then 1 else 0 end usouTerceiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Quarto' ) then 1 else 0 end usouQuartoDesconto");
		sb.append(" from contareceber ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" where negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.situacao = 'RE' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");

		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}
		if (turmaVO != null && turmaVO.getCodigo() > 0) {
			sb.append(" and turma.codigo = ").append(turmaVO.getCodigo());
		}
		sb.append(" and valordescontoprogressivo is not null and valordescontoprogressivo >0.0  ");

		sb.append(" ) as t group by curso, codigoCurso, codigoTurma, turma order by curso, turma ");

		painelGestorVO.setValorTotalDescontoProgressivo(0.0);
		painelGestorVO.setTotalPrimeiroDescontoProgressivo(0.0);
		painelGestorVO.setTotalSegundoDescontoProgressivo(0.0);
		painelGestorVO.setTotalTerceiroDesconto(0.0);
		painelGestorVO.setTotalQuartoDescontoProgressivo(0.0);

		painelGestorVO.setTotalPessoasUsouDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouPrimeiroDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouSegundoDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouTerceiroDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouQuartoDescontoProgressivo(0);
		painelGestorVO.getPainelGestorMonitoramentoDescontoProgressivoVOs().clear();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoProgressivo(painelGestorVO, rs, tipoNivelEducacional, false);
		}

		executarCalculoPercentualMonitoramentoDescontoProgressivo(painelGestorVO);
	}

	public void consultarDadosDetalhamentoDescontoProgressivo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, CursoVO cursoVO, TipoNivelEducacional tipoNivelEducacional) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder();
		sb.append(" select curso, codigoCurso, sum(usouPrimeiroDesconto) as usouPrimeiroDesconto, ");
		sb.append(" sum(usouSegundoDesconto) as usouSegundoDesconto, ");
		sb.append(" sum(usouTerceiroDesconto) as usouTerceiroDesconto, ");
		sb.append(" sum(usouQuartoDesconto) as usouQuartoDesconto, ");
		sb.append(" sum(primeiroDesconto::NUMERIC(20,2)) as primeiroDesconto,  ");
		sb.append(" sum(segundoDesconto::NUMERIC(20,2)) as segundoDesconto,  ");
		sb.append(" sum(terceiroDesconto::NUMERIC(20,2)) as terceiroDesconto, ");
		sb.append(" sum(quartoDesconto::NUMERIC(20,2)) as quartoDesconto");
		sb.append(" from ( ");
		sb.append(" select curso.nome as curso, curso.codigo as codigoCurso, ");
		sb.append(" case when (descontoprogressivoutilizado is null or descontoprogressivoutilizado = 'Primeiro' or descontoprogressivoutilizado = '') then valordescontoprogressivo else 0.0 end primeiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Segundo' ) then valordescontoprogressivo else 0.0 end segundoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Terceiro' ) then valordescontoprogressivo else 0.0 end terceiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Quarto' ) then valordescontoprogressivo else 0.0 end quartoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Primeiro'  or descontoprogressivoutilizado = 'Primeiro' or descontoprogressivoutilizado = '' ) then 1 else 0 end usouPrimeiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Segundo' ) then 1 else 0 end usouSegundoDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Terceiro' ) then 1 else 0 end usouTerceiroDesconto,");
		sb.append(" case when (descontoprogressivoutilizado = 'Quarto' ) then 1 else 0 end usouQuartoDesconto");
		sb.append(" from contareceber ");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" where negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.situacao = 'RE' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");

		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and niveleducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sb.append(" and curso.codigo = ").append(cursoVO.getCodigo());
		}
		sb.append(" and valordescontoprogressivo is not null and valordescontoprogressivo >0.0  ");

		sb.append(" ) as t group by curso, codigoCurso order by curso");

		painelGestorVO.setValorTotalDescontoProgressivo(0.0);
		painelGestorVO.setTotalPrimeiroDescontoProgressivo(0.0);
		painelGestorVO.setTotalSegundoDescontoProgressivo(0.0);
		painelGestorVO.setTotalTerceiroDesconto(0.0);
		painelGestorVO.setTotalQuartoDescontoProgressivo(0.0);

		painelGestorVO.setTotalPessoasUsouDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouPrimeiroDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouSegundoDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouTerceiroDescontoProgressivo(0);
		painelGestorVO.setTotalPessoasUsouQuartoDescontoProgressivo(0);
		painelGestorVO.getPainelGestorMonitoramentoDescontoProgressivoVOs().clear();
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (rs.next()) {
			montarDadosPainelGestorMonitoramentoDescontoProgressivo(painelGestorVO, rs, tipoNivelEducacional, true);
		}

		executarCalculoPercentualMonitoramentoDescontoProgressivo(painelGestorVO);
	}

	public void executarCalculoPercentualMonitoramentoDescontoProgressivo(PainelGestorVO painelGestorVO) {
		painelGestorVO.setValorTotalDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais(painelGestorVO.getValorTotalDescontoProgressivo()));
		if (painelGestorVO.getValorTotalDescontoProgressivo() > 0) {
			painelGestorVO.setPercentualPrimeiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalPrimeiroDescontoProgressivo() * 100) / painelGestorVO.getValorTotalDescontoProgressivo()));
			painelGestorVO.setPercentualSegundoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalSegundoDescontoProgressivo() * 100) / painelGestorVO.getValorTotalDescontoProgressivo()));
			painelGestorVO.setPercentualTerceiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalTerceiroDescontoProgressivo() * 100) / painelGestorVO.getValorTotalDescontoProgressivo()));
			painelGestorVO.setPercentualQuartoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalQuartoDescontoProgressivo() * 100) / painelGestorVO.getValorTotalDescontoProgressivo()));
		}
		if (painelGestorVO.getTotalPessoasUsouDescontoProgressivo() > 0) {
			painelGestorVO.setPercentualTituloPrimeiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalPessoasUsouPrimeiroDescontoProgressivo() * 100) / painelGestorVO.getTotalPessoasUsouDescontoProgressivo()));
			painelGestorVO.setPercentualTituloSegundoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalPessoasUsouSegundoDescontoProgressivo() * 100) / painelGestorVO.getTotalPessoasUsouDescontoProgressivo()));
			painelGestorVO.setPercentualTituloTerceiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalPessoasUsouTerceiroDescontoProgressivo() * 100) / painelGestorVO.getTotalPessoasUsouDescontoProgressivo()));
			painelGestorVO.setPercentualTituloQuartoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorVO.getTotalPessoasUsouQuartoDescontoProgressivo() * 100) / painelGestorVO.getTotalPessoasUsouDescontoProgressivo()));
		}
		for (PainelGestorMonitoramentoDescontoProgressivoVO painelGestorMonitoramentoDescontoProgressivoVO : painelGestorVO.getPainelGestorMonitoramentoDescontoProgressivoVOs()) {
			if (painelGestorMonitoramentoDescontoProgressivoVO.getTotalDesconto() > 0) {
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualPrimeiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalPrimeiroDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalDesconto()));
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualSegundoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalSegundoDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalDesconto()));
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualTerceiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalTerceiroDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalDesconto()));
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualQuartoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalQuartoDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalDesconto()));
			}

			if (painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouDesconto() > 0) {
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualTituloPrimeiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouPrimeiroDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouDesconto()));
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualTituloSegundoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouSegundoDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouDesconto()));
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualTituloTerceiroDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouTerceiroDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouDesconto()));
				painelGestorMonitoramentoDescontoProgressivoVO.setPercentualTituloQuartoDescontoProgressivo(Uteis.arrendondarForcando2CadasDecimais((painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouQuartoDesconto() * 100) / painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouDesconto()));
			}
		}
	}

	public void montarDadosPainelGestorMonitoramentoDescontoProgressivo(PainelGestorVO painelGestorVO, SqlRowSet rs, TipoNivelEducacional tipoNivelEducacional, Boolean isCurso) {
		PainelGestorMonitoramentoDescontoProgressivoVO painelGestorMonitoramentoDescontoProgressivoVO = new PainelGestorMonitoramentoDescontoProgressivoVO();
		if (isCurso) {
			painelGestorMonitoramentoDescontoProgressivoVO.setCodigoCurso(rs.getInt("codigoCurso"));
			painelGestorMonitoramentoDescontoProgressivoVO.setCurso(rs.getString("curso"));
		} else {
			painelGestorMonitoramentoDescontoProgressivoVO.setCodigoCurso(rs.getInt("codigoCurso"));
			painelGestorMonitoramentoDescontoProgressivoVO.setCurso(rs.getString("curso"));
			painelGestorMonitoramentoDescontoProgressivoVO.setCodigoTurma(rs.getInt("codigoTurma"));
			painelGestorMonitoramentoDescontoProgressivoVO.setTurma(rs.getString("turma"));
		}
		if (tipoNivelEducacional != null) {

			painelGestorMonitoramentoDescontoProgressivoVO.setNivel(tipoNivelEducacional.getValor());
		}
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalDesconto(Uteis.arrendondarForcando2CadasDecimais(rs.getDouble("primeiroDesconto") + rs.getDouble("segundoDesconto") + rs.getDouble("terceiroDesconto") + rs.getDouble("quartoDesconto")));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalPrimeiroDesconto(rs.getDouble("primeiroDesconto"));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalSegundoDesconto(rs.getDouble("segundoDesconto"));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalTerceiroDesconto(rs.getDouble("terceiroDesconto"));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalQuartoDesconto(rs.getDouble("quartoDesconto"));

		painelGestorMonitoramentoDescontoProgressivoVO.setTotalPessoasUsouDesconto(rs.getInt("usouPrimeiroDesconto") + rs.getInt("usouSegundoDesconto") + rs.getInt("usouTerceiroDesconto") + rs.getInt("usouQuartoDesconto"));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalPessoasUsouPrimeiroDesconto(rs.getInt("usouPrimeiroDesconto"));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalPessoasUsouSegundoDesconto(rs.getInt("usouSegundoDesconto"));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalPessoasUsouTerceiroDesconto(rs.getInt("usouTerceiroDesconto"));
		painelGestorMonitoramentoDescontoProgressivoVO.setTotalPessoasUsouQuartoDesconto(rs.getInt("usouQuartoDesconto"));

		painelGestorVO.setValorTotalDescontoProgressivo(painelGestorVO.getValorTotalDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalDesconto());
		painelGestorVO.setTotalPrimeiroDescontoProgressivo(painelGestorVO.getTotalPrimeiroDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalPrimeiroDesconto());
		painelGestorVO.setTotalSegundoDescontoProgressivo(painelGestorVO.getTotalTerceiroDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalSegundoDesconto());
		painelGestorVO.setTotalTerceiroDesconto(painelGestorVO.getTotalTerceiroDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalTerceiroDesconto());
		painelGestorVO.setTotalQuartoDescontoProgressivo(painelGestorVO.getTotalQuartoDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalQuartoDesconto());

		painelGestorVO.setTotalPessoasUsouDescontoProgressivo(painelGestorVO.getTotalPessoasUsouDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouDesconto());
		painelGestorVO.setTotalPessoasUsouPrimeiroDescontoProgressivo(painelGestorVO.getTotalPessoasUsouPrimeiroDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouPrimeiroDesconto());
		painelGestorVO.setTotalPessoasUsouSegundoDescontoProgressivo(painelGestorVO.getTotalPessoasUsouSegundoDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouSegundoDesconto());
		painelGestorVO.setTotalPessoasUsouTerceiroDescontoProgressivo(painelGestorVO.getTotalPessoasUsouTerceiroDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouTerceiroDesconto());
		painelGestorVO.setTotalPessoasUsouQuartoDescontoProgressivo(painelGestorVO.getTotalPessoasUsouQuartoDescontoProgressivo() + painelGestorMonitoramentoDescontoProgressivoVO.getTotalPessoasUsouQuartoDesconto());

		painelGestorVO.getPainelGestorMonitoramentoDescontoProgressivoVOs().add(painelGestorMonitoramentoDescontoProgressivoVO);

	}

	@Override
	public void executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorNivelEducacional(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		if (!mesAno.isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setPainelGestorPorNivelEducacionalVO(new PainelGestorVO());
		painelGestorVO.getPainelGestorPorNivelEducacionalVO().setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.getPainelGestorPorNivelEducacionalVO().setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.getPainelGestorPorNivelEducacionalVO().setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosPainelGestorFinanceiroAcademicoPorNivelEducacional(painelGestorVO.getPainelGestorPorNivelEducacionalVO(), unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional);
	}

	@Override
	public void executarInicializacaoDadosMapaPainelGestorAcademicoXFinanceiroPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, Integer codigoCurso, String mesAno) throws Exception {
		if (!mesAno.isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		validarDadosUnidadeEnsino(unidadeEnsinoVOs);
		painelGestorVO.setPainelGestorPorTurmaVO(new PainelGestorVO());
		painelGestorVO.getPainelGestorPorTurmaVO().setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.getPainelGestorPorTurmaVO().setDataInicioPorTurma(dataTermino);
		// painelGestorVO.getPainelGestorPorTurmaVO().setDataFinalPorTurma(dataInicio);
		painelGestorVO.setCodigoCurso(codigoCurso);
		painelGestorVO.getPainelGestorPorTurmaVO().setCodigoCurso(codigoCurso);
		consultarDadosPainelGestorFinanceiroAcademicoPorTurma(painelGestorVO.getPainelGestorPorTurmaVO(), unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional, codigoCurso);
	}

	@Override
	public void executarInicializacaoDadosPainelGestorMonitoramentoDescontoPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, Integer codigoCurso) throws Exception {
		if (!mesAno.isEmpty()) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			String ano = mesAno.substring(mesAno.indexOf("/") + 1, mesAno.length());
			dataInicio = Uteis.gerarDataDiaMesAno(1, Integer.valueOf(mes), Integer.valueOf(ano));
			dataTermino = Uteis.getDataUltimoDiaMes(dataInicio);
		} else {
			dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
			dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		}
		validarDadosFiltroBusca(dataInicio, dataTermino);
		painelGestorVO.setTipoNivelEducacional(tipoNivelEducacional);
		// painelGestorVO.setDataFinalPorNivelEducacional(dataTermino);
		// painelGestorVO.setDataInicioPorNivelEducacional(dataInicio);
		consultarDadosPainelGestorMonitoramentoDescontoPorTurma(painelGestorVO, unidadeEnsinoVOs, dataInicio, dataTermino, tipoNivelEducacional, mesAno, codigoCurso);
	}

	public void consultarDadosPainelGestorMonitoramentoDescontoPorTurma(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, String mesAno, Integer codigoCurso) throws Exception {
		validarDadosFiltroBusca(dataInicio, dataTermino);
		StringBuilder sb = new StringBuilder("");
		sb.append(" select curso,codigoCurso, ");
		sb.append("  turma, codigoturma, ");
		sb.append(" sum(descontoprogressivo::NUMERIC(20,2)) as descontoprogressivo, ");
		sb.append(" sum(descontoinstituicao::NUMERIC(20,2)) as descontoinstituicao, ");
		sb.append(" sum(descontoAluno::NUMERIC(20,2))  as descontoAluno,");
		sb.append(" sum(descontoConvenio::NUMERIC(20,2)) as descontoConvenio, ");
		sb.append(" sum(descontoRecebimento::NUMERIC(20,2)) as descontoRecebimento, ");
		sb.append(" sum(descontoRateio::NUMERIC(20,2)) as descontoRateio, ");
		sb.append(" sum(valor::NUMERIC(20,2))  as valor, ");
		sb.append(" sum(valorrecebido::NUMERIC(20,2)) as valorrecebido ");
		sb.append(" from (");
		sb.append(" select curso.nome as curso,curso.codigo as codigoCurso, ");
		sb.append(" turma.identificadorTurma as turma, turma.codigo as codigoTurma, ");
		sb.append(" case when(descontoConvenio is not null) then descontoConvenio else 0.0 end as descontoConvenio, ");
		sb.append(" case when(descontoinstituicao is not null  ) then descontoinstituicao else 0.0 end as descontoinstituicao,");
		sb.append(" case when(valordescontoalunojacalculado is not null) then valordescontoalunojacalculado else 0.0 end as descontoAluno, ");
		sb.append(" case when(valorcalculadodescontolancadorecebimento is not null) then valorcalculadodescontolancadorecebimento else 0.0 end as descontoRecebimento,");
		sb.append(" case when(valordescontorateio is not null) then valordescontorateio else 0.0 end as descontoRateio, ");
		sb.append(" case when(valordescontoprogressivo is not null and descontoprogressivoutilizado != 'Nenhum') then valordescontoprogressivo else 0.0 end as descontoprogressivo, ");
		sb.append(" 0.0 as valor, valorrecebido");
		sb.append(" from contareceber");
		sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
		sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" where contareceber.situacao='RE' and negociacaorecebimento.data::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaorecebimento.data::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		sb.append(" and curso.codigo = ").append(codigoCurso).append("");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}

		sb.append(" union all");
		// Esta união traz os valores faturados no período
		sb.append(" select curso.nome as curso,curso.codigo as codigoCurso, ");
		sb.append(" turma.identificadorTurma as turma, turma.codigo as codigoTurma, ");
		sb.append(" 0.0 as descontoprogressivo, 0.0 as descontoinstituicao, 0.0 as descontoAluno, ");
		sb.append(" 0.0 as descontoConvenio, 0.0 as descontoRecebimento, 0.0 as descontoRateio, 0.0  as descontoprogressivo,  valor, 0.0 as  valorrecebido");
		sb.append(" from contareceber ");
		sb.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sb.append(" inner join curso on matricula.curso = curso.codigo ");
		sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" where dataVencimento::DATE >='").append(Uteis.getDataJDBC(dataInicio)).append("' and dataVencimento::DATE <='").append(Uteis.getDataJDBC(dataTermino)).append("'  and contareceber.situacao not in ('NE', 'CA')");
		sb.append(" and curso.codigo = ").append(codigoCurso).append("");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contareceber.unidadeensinofinanceira"));
		if (tipoNivelEducacional != null) {
			sb.append(" and nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
		}
		sb.append(" ) as t group by codigoCurso, curso,  turma, codigoturma  order by turma");

		painelGestorVO.setTotalFaturadoPeriodoMonitoramentoDesconto(0.0);
		painelGestorVO.setTotalRecebidoPeriodoMonitoramentoDesconto(0.0);
		painelGestorVO.setTotalDescontoAluno(0.0);
		painelGestorVO.setTotalDescontoConvenio(0.0);
		painelGestorVO.setTotalDescontoInstituicao(0.0);
		painelGestorVO.setTotalDescontoPeriodo(0.0);
		painelGestorVO.setTotalDescontoProgressivo(0.0);
		painelGestorVO.setTotalDescontoRecebimento(0.0);
		painelGestorVO.setTotalDescontoRateio(0.0);
		painelGestorVO.setTotalAlunoReceberamDesconto(0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		painelGestorVO.setPainelGestorMonitoramentoDescontoMesAnoVO(new PainelGestorMonitoramentoDescontoMesAnoVO());
		while (rs.next()) {
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setValorFaturadoMes(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getValorFaturadoMes() + rs.getDouble("valor"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setValorRecebidoMes(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getValorRecebidoMes() + rs.getDouble("valorrecebido"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoAluno(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoAluno() + rs.getDouble("descontoaluno"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoProgressivo(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoProgressivo() + rs.getDouble("descontoprogressivo"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoConvenio(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoConvenio() + rs.getDouble("descontoconvenio"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoInstituicao(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoInstituicao() + rs.getDouble("descontoinstituicao"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoRecebimento(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoRecebimento() + rs.getDouble("descontoRecebimento"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoRateio(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoRateio() + rs.getDouble("descontoRateio"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().setTotalDescontoMes(painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getTotalDescontoMes() + rs.getDouble("descontoaluno") + rs.getDouble("descontoprogressivo") + rs.getDouble("descontoconvenio") + rs.getDouble("descontoinstituicao") + rs.getDouble("descontoRecebimento")+ rs.getDouble("descontoRateio"));
			painelGestorVO.getPainelGestorMonitoramentoDescontoMesAnoVO().getPainelGestorMonitoramentoDescontoNivelEducacionalVOs().add(montarDadosPainelGestorMonitoramentoDescontoCursoVO(rs, tipoNivelEducacional, mesAno, false));
		}
	}
	
	
	public SqlRowSet consultarGraficoConsumoaPainelGestorFinanceiro(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(centroresultadoorigem.valor) as valorPagar,");
		sql.append(" CASE WHEN centroresultadoorigem.departamento is null then 0 else centroresultadoorigem.departamento  end as departamento, ");
		sql.append(" CASE WHEN departamento.nome is null then 'SEM DEPARTAMENTO' ELSE departamento.nome end as nome ");
		sql.append(" from contapagar  ");
		sql.append(" inner join centroresultadoorigem on centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::VARCHAR and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
		sql.append(" left join departamento on departamento.codigo = centroresultadoorigem.departamento ");
		sql.append(" WHERE 1=1 ");
		sql.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
		sql.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
		sql.append(" group by centroresultadoorigem.departamento, departamento.nome ");
		sql.append(" order by departamento.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	/**
	 * Consulta os dados para a Geração dos dados do Mapa de Despesas do Painel
	 * Gestor Financeiro
	 * 
	 * @param painelGestorVO
	 * @param unidadeEnsinoVOs
	 * @param dataInicio
	 * @param dataTermino
	 * @throws Exception
	 */
	public SqlRowSet consultarGraficoCategoriaDespesaPainelGestorFinanceiro(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String identificadorCategoriaPrincipal, Integer departamento, Boolean trazerContasPagas, Boolean trazerContasAPagar, Integer limit, Integer offset) throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(" select sum(valorPagar::NUMERIC(20,2)) as valorPagar, sum(valorPago::NUMERIC(20,2)) as valorPago, sum(valor::NUMERIC(20,2)) as valor, identificadorCategoriaDespesa, categoriaDespesa from (");

		if (trazerContasAPagar) {
			sb.append(" select sum(centroresultadoorigem.valor::NUMERIC(20,2)) as valorPagar,0 as valorPago, sum(centroresultadoorigem.valor::NUMERIC(20,2)) as valor,");
			sb.append(" cdp.identificadorCategoriaDespesa as identificadorCategoriaDespesa,");
			sb.append(" cdp.descricao as categoriaDespesa");
			sb.append(" from contapagar ");
			sb.append(" inner join centroresultadoorigem on centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::VARCHAR and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
			sb.append(" left join categoriaDespesa on categoriaDespesa.codigo = centroresultadoorigem.categoriaDespesa");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty() && !identificadorCategoriaPrincipal.equals("-1")) {
				sb.append(" left join categoriaDespesa cdp on cdp.identificadorCategoriaDespesa = case when categoriaDespesa.identificadorCategoriaDespesa like ('" + identificadorCategoriaPrincipal + ".%')");
				sb.append(" then case when SUBSTRING(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".') + " + (identificadorCategoriaPrincipal.length() + 1) + ", char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , 0, STRPOS(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".')+ " + (identificadorCategoriaPrincipal.length() + 1) + ",  char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , '.')) != '' ");
				sb.append(" then '" + identificadorCategoriaPrincipal + ".'|| SUBSTRING(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".') + " + (identificadorCategoriaPrincipal.length() + 1) + ", char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , 0, STRPOS(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".')+ " + (identificadorCategoriaPrincipal.length() + 1) + ",  char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , '.')) else   categoriaDespesa.identificadorCategoriaDespesa end ");
				sb.append(" else categoriaDespesa.identificadorCategoriaDespesa end");
			} else {
				sb.append(" left join categoriaDespesa cdp on cdp.identificadorCategoriaDespesa = case when categoriaDespesa.identificadorCategoriaDespesa like ('%.%')");
				sb.append(" then SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa ,0, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '.' ))");
				sb.append(" else categoriaDespesa.identificadorCategoriaDespesa end");
			}

			sb.append(" where contapagar.situacao = 'AP'");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty() && !identificadorCategoriaPrincipal.equals("-1")) {
				sb.append(" and  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%')");
			}
			sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
			if(departamento != null && departamento != 0){
				sb.append(" and centroresultadoorigem.departamento = ").append(departamento);
			}else if(departamento != null && departamento == 0) {
				sb.append(" and centroresultadoorigem.departamento is null ");
			}
			sb.append(" group by cdp.identificadorCategoriaDespesa, cdp.descricao ");
		}
		if (trazerContasAPagar && trazerContasPagas) {
			sb.append(" union ");
		}

		if (trazerContasPagas) {
			sb.append(" select 0 as valorPagar, sum(centroresultadoorigem.valor::NUMERIC(20,2)) as valorPago,  sum(centroresultadoorigem.valor::NUMERIC(20,2))as valor, ");
			sb.append(" cdp.identificadorCategoriaDespesa as identificadorCategoriaDespesa,");
			sb.append(" cdp.descricao as categoriaDespesa");
			sb.append(" from contapagar ");
			sb.append(" inner join centroresultadoorigem on centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::VARCHAR and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
			sb.append(" left join categoriaDespesa on categoriaDespesa.codigo = centroresultadoorigem.categoriaDespesa");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty() && !identificadorCategoriaPrincipal.equals("-1")) {
				sb.append(" left join categoriaDespesa cdp on cdp.identificadorCategoriaDespesa = case when categoriaDespesa.identificadorCategoriaDespesa like ('" + identificadorCategoriaPrincipal + ".%')");
				sb.append(" then case when SUBSTRING(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".') + " + (identificadorCategoriaPrincipal.length() + 1) + ", char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , 0, STRPOS(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".')+ " + (identificadorCategoriaPrincipal.length() + 1) + ",  char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , '.')) != '' ");
				sb.append(" then '" + identificadorCategoriaPrincipal + ".'|| SUBSTRING(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".') + " + (identificadorCategoriaPrincipal.length() + 1) + ", char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , 0, STRPOS(");
				sb.append(" SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '" + identificadorCategoriaPrincipal + ".')+ " + (identificadorCategoriaPrincipal.length() + 1) + ",  char_length(categoriaDespesa.identificadorCategoriaDespesa))");
				sb.append(" , '.')) else   categoriaDespesa.identificadorCategoriaDespesa end ");
				sb.append(" else categoriaDespesa.identificadorCategoriaDespesa end ");
			} else {
				sb.append(" left join categoriaDespesa cdp on cdp.codigo = ( ");
				sb.append(" select cd1.codigo from categoriaDespesa cd1 where cd1.identificadorCategoriaDespesa = SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa , 0, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '.' )) ");
				sb.append(" union");
				sb.append(" select cd1.codigo from categoriaDespesa cd1 where cd1.identificadorCategoriaDespesa = categoriaDespesa.identificadorCategoriaDespesa ");
				sb.append(" limit 1 ");
				sb.append(" ) ");

			}
			sb.append(" where (situacao = 'PA' or situacao = 'PP') ");
			if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty() && !identificadorCategoriaPrincipal.equals("-1")) {
				sb.append(" and  categoriaDespesa.identificadorCategoriaDespesa like ('").append(identificadorCategoriaPrincipal).append(".%')");
			}
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
			if(departamento != null && departamento != 0){
				sb.append(" and centroresultadoorigem.departamento = ").append(departamento);
			}else if(departamento != null && departamento == 0) {
				sb.append(" and centroresultadoorigem.departamento is null ");
			}
			sb.append(" and exists (select contapagar ");
			sb.append(" from contaPagarNegociacaoPagamento inner join NegociacaoPagamento on NegociacaoPagamento.codigo =  contaPagarNegociacaoPagamento.negociacaoContaPagar");
			sb.append(" and NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("  00:00:00' ");
			sb.append(" and NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("  23:59:59' ");
			sb.append(" and contaPagarNegociacaoPagamento.contapagar = contapagar.codigo)");
			sb.append(" group by cdp.identificadorCategoriaDespesa, cdp.descricao");
		}
		sb.append(" ) as t group by identificadorCategoriaDespesa, categoriaDespesa");
		sb.append(" order by valor desc, identificadorCategoriaDespesa");
		if (limit != null && limit > 0) {
			sb.append(" limit " + limit).append(" offset " + offset);
			return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}
	
	
	
	@Override
	public SqlRowSet consultarGraficoCategoriaDespesaPainelGestorFinanceiroPorDepartamento(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino,  Boolean trazerContasPagas, Boolean trazerContasAPagar,Integer departamento, Date dataCompetenciaInicial, Date dataCompetenciaFinal) throws Exception {

		StringBuilder sb = new StringBuilder();

		sb.append(" select sum(valorPagar::NUMERIC(20,2)) as valorPagar, sum(valorPago::NUMERIC(20,2)) as valorPago, sum(valor::NUMERIC(20,2)) as valor, identificadorCategoriaDespesa, categoriaDespesa from (");

		if (trazerContasAPagar) {
			sb.append(" select sum(centroresultadoorigem.valor::NUMERIC(20,2)) as valorPagar,0 as valorPago, sum(centroresultadoorigem.valor::NUMERIC(20,2)) as valor,");
			sb.append(" cdp.identificadorCategoriaDespesa as identificadorCategoriaDespesa,");
			sb.append(" cdp.descricao as categoriaDespesa");
			sb.append(" from contapagar ");
			sb.append(" inner join centroresultadoorigem on centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::VARCHAR and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
			sb.append(" left join categoriaDespesa on categoriaDespesa.codigo = centroresultadoorigem.categoriaDespesa");			
			sb.append(" left join categoriaDespesa cdp on cdp.identificadorCategoriaDespesa = case when categoriaDespesa.identificadorCategoriaDespesa like ('%.%')");
				sb.append(" then SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa ,0, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '.' ))");
				sb.append(" else categoriaDespesa.identificadorCategoriaDespesa end");
			sb.append(" where contapagar.situacao = 'AP'");
			if(departamento != null && departamento != 0){
				sb.append(" and centroresultadoorigem.departamento = ").append(departamento);
			}else if(departamento != null && departamento == 0) {
				sb.append(" and centroresultadoorigem.departamento is null ");
			}
			
			if (Uteis.isAtributoPreenchido(dataCompetenciaInicial) && Uteis.isAtributoPreenchido(dataCompetenciaFinal)) {
				sb.append(" and contapagar.datafatogerador >= '").append(Uteis.getDataJDBC(dataCompetenciaInicial)).append(" 00:00:00'");
				sb.append(" and contapagar.datafatogerador <= '").append(Uteis.getDataJDBC(dataCompetenciaFinal)).append(" 23:59:59'");
			}
			sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
			sb.append(" group by cdp.identificadorCategoriaDespesa, cdp.descricao ");
		}
		if (trazerContasAPagar && trazerContasPagas) {
			sb.append(" union ");
		}

		if (trazerContasPagas) {
			sb.append(" select 0 as valorPagar, sum(centroresultadoorigem.valor::NUMERIC(20,2)) as valorPago,  sum(centroresultadoorigem.valor::NUMERIC(20,2))as valor, ");
			sb.append(" cdp.identificadorCategoriaDespesa as identificadorCategoriaDespesa,");
			sb.append(" cdp.descricao as categoriaDespesa");
			sb.append(" from contapagar ");
			sb.append(" inner join centroresultadoorigem on centroresultadoorigem.tipoCentroResultadoOrigem = '").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR.name()).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::VARCHAR and tipoMovimentacaoCentroResultadoOrigemenum != '").append(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR.name()).append("'");
			sb.append(" left join categoriaDespesa on categoriaDespesa.codigo = centroresultadoorigem.categoriaDespesa");
				sb.append(" left join categoriaDespesa cdp on cdp.codigo = ( ");
				sb.append(" select cd1.codigo from categoriaDespesa cd1 where cd1.identificadorCategoriaDespesa = SUBSTRING(categoriaDespesa.identificadorCategoriaDespesa , 0, STRPOS(categoriaDespesa.identificadorCategoriaDespesa , '.' )) ");
				sb.append(" union");
				sb.append(" select cd1.codigo from categoriaDespesa cd1 where cd1.identificadorCategoriaDespesa = categoriaDespesa.identificadorCategoriaDespesa ");
				sb.append(" limit 1 ");
				sb.append(" ) ");

			sb.append(" where (situacao = 'PA' or situacao = 'PP') ");
			if(departamento != null && departamento != 0){
				sb.append(" and centroresultadoorigem.departamento = ").append(departamento);
			}else if(departamento != null && departamento == 0) {
				sb.append(" and centroresultadoorigem.departamento is null ");
			}
			
			if (Uteis.isAtributoPreenchido(dataCompetenciaInicial) && Uteis.isAtributoPreenchido(dataCompetenciaFinal)) {
				sb.append(" and contapagar.datafatogerador >= '").append(Uteis.getDataJDBC(dataCompetenciaInicial)).append(" 00:00:00'");
				sb.append(" and contapagar.datafatogerador <= '").append(Uteis.getDataJDBC(dataCompetenciaFinal)).append(" 23:59:59'");
			}
			sb.append(" and contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "contapagar.unidadeensino"));
//			sb.append(" and contapagar.codigo in (select contapagar ");
//			sb.append(" from contaPagarNegociacaoPagamento inner join NegociacaoPagamento on NegociacaoPagamento.codigo =  contaPagarNegociacaoPagamento.negociacaoContaPagar");
//			sb.append(" and NegociacaoPagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("  00:00:00' ");
//			sb.append(" and NegociacaoPagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("  23:59:59' ");
//			sb.append(" )");
			sb.append(" group by cdp.identificadorCategoriaDespesa, cdp.descricao");
		}
		sb.append(" ) as t group by identificadorCategoriaDespesa, categoriaDespesa");
		sb.append(" order by valor desc, identificadorCategoriaDespesa");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}

	@Override
	/**
	 * Método responsável por criar o Gráfico de Categoria de Despesa
	 * @author CarlosEugenio 24/01/2014
	 */
	public void executarCriacaoGraficoCategoriaDespesa(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino, String identificadorCategoriaPrincipal, Integer departamento, Boolean telaInicial, Integer limite, Integer offset) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		if (!painelGestorVO.getTrazerContasAPagar() && !painelGestorVO.getTrazerContasPagas()) {
			return;
		}
		int index = 0;
		SqlRowSet rs = consultarGraficoCategoriaDespesaPainelGestorFinanceiro(unidadeEnsinoVOs, dataInicio, dataTermino, identificadorCategoriaPrincipal, departamento, painelGestorVO.getTrazerContasPagas(), painelGestorVO.getTrazerContasAPagar(), limite, offset);
		Color color = Color.GREEN;
		Boolean existeRegistro = false;
		while (rs.next()) {
			if (index == 0) {
				painelGestorVO.setLegendaGraficoCategoriaDespesaVOs(null);
				painelGestorVO.setValorTotalPagarPorCategoriaDespesa(0.0);
				painelGestorVO.setValorTotalPagoPorCategoriaDespesa(0.0);
				painelGestorVO.setValorTotalPorCategoriaDespesa(0.0);
				painelGestorVO.setNivelAtualCategoriaDespesa(identificadorCategoriaPrincipal);
				painelGestorVO.setListaCoresGraficoCategoriaDespesa("");
				index++;
			}

			existeRegistro = true;
			painelGestorVO.setValorTotalPagarPorCategoriaDespesa(painelGestorVO.getValorTotalPagarPorCategoriaDespesa() + rs.getDouble("valorPagar"));
			painelGestorVO.setValorTotalPagoPorCategoriaDespesa(painelGestorVO.getValorTotalPagoPorCategoriaDespesa() + rs.getDouble("valorPago"));
			color = Uteis.gerarCorAleatoria(color);
			while (painelGestorVO.getListaCoresGraficoCategoriaDespesa().contains(HtmlColor.encodeRGB(color))) {
				color = Uteis.gerarCorAleatoria(color);
			}
			if (painelGestorVO.getListaCoresGraficoCategoriaDespesa().isEmpty()) {
				painelGestorVO.setListaCoresGraficoCategoriaDespesa("\"" + HtmlColor.encodeRGB(color) + "\"");
			} else {
				painelGestorVO.setListaCoresGraficoCategoriaDespesa(painelGestorVO.getListaCoresGraficoCategoriaDespesa() + ", \"" + HtmlColor.encodeRGB(color) + "\"");
			}
			if (rs.getString("identificadorCategoriaDespesa") == null || rs.getString("identificadorCategoriaDespesa").isEmpty()) {
				painelGestorVO.getLegendaGraficoCategoriaDespesaVOs().add(new LegendaGraficoVO(0, "0 - Não Informado", "-1", rs.getDouble("valor"), HtmlColor.encodeRGB(color), true));
			} else {
				painelGestorVO.getLegendaGraficoCategoriaDespesaVOs().add(new LegendaGraficoVO(0, rs.getString("identificadorCategoriaDespesa") + "-" + rs.getString("categoriaDespesa"), rs.getString("identificadorCategoriaDespesa"), rs.getDouble("valor"), HtmlColor.encodeRGB(color), true));
			}
		}
		painelGestorVO.setValorTotalPorCategoriaDespesa(painelGestorVO.getValorTotalPagarPorCategoriaDespesa() + painelGestorVO.getValorTotalPagoPorCategoriaDespesa());
		if (telaInicial) {
			painelGestorVO.setListaValoresCategoriaDespesaInicial(Uteis.realizarMontagemDadosGraficoPizza(painelGestorVO.getLegendaGraficoCategoriaDespesaVOs()));
		} else {
			painelGestorVO.setListaValoresCategoriaDespesa(Uteis.realizarMontagemDadosGraficoPizza(painelGestorVO.getLegendaGraficoCategoriaDespesaVOs()));
		}
		List<String> categorias = new ArrayList<String>(0);
		if (identificadorCategoriaPrincipal != null && !identificadorCategoriaPrincipal.isEmpty()) {
			for (String nivel : painelGestorVO.getListaNivelGraficoCategoriaDespesa()) {
				if (nivel.equals(identificadorCategoriaPrincipal)) {
					break;
				}
				categorias.add(nivel);
			}
			if (existeRegistro) {
				if (categorias.isEmpty()) {
					categorias.add("início");
				}
				categorias.add(identificadorCategoriaPrincipal);
			}
		}
		if (categorias.isEmpty() && departamento != null) {
			categorias.add("início");
		}
		painelGestorVO.setListaNivelGraficoCategoriaDespesa(categorias);

	}
	
	
	@Override
	public void executarCriacaoGraficoCategoriaDespesaPorDepartamento(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino,  Boolean telaInicial,Integer departamento, Date dataCompetenciaInicial,Date dataCompetenciaFinal) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		if (!painelGestorVO.getTrazerContasAPagar() && !painelGestorVO.getTrazerContasPagas()) {
			return;
		}
		int index = 0;
		SqlRowSet rs = consultarGraficoCategoriaDespesaPainelGestorFinanceiroPorDepartamento(unidadeEnsinoVOs, dataInicio, dataTermino,  painelGestorVO.getTrazerContasPagas(), painelGestorVO.getTrazerContasAPagar(),departamento,dataCompetenciaInicial,dataCompetenciaFinal);
		Color color = Color.GREEN;
		
		while (rs.next()) {
			if (index == 0) {
				painelGestorVO.setLegendaGraficoCategoriaDespesaVOs(null);
				painelGestorVO.setValorTotalPagarPorCategoriaDespesa(0.0);
				painelGestorVO.setValorTotalPagoPorCategoriaDespesa(0.0);
				painelGestorVO.setValorTotalPorCategoriaDespesa(0.0);
				painelGestorVO.setListaCoresGraficoCategoriaDespesa("");
				index++;
			}

		
			painelGestorVO.setValorTotalPagarPorCategoriaDespesa(painelGestorVO.getValorTotalPagarPorCategoriaDespesa() + rs.getDouble("valorPagar"));
			painelGestorVO.setValorTotalPagoPorCategoriaDespesa(painelGestorVO.getValorTotalPagoPorCategoriaDespesa() + rs.getDouble("valorPago"));
			color = Uteis.gerarCorAleatoria(color);
			while (painelGestorVO.getListaCoresGraficoCategoriaDespesa().contains(HtmlColor.encodeRGB(color))) {
				color = Uteis.gerarCorAleatoria(color);
			}
			if (painelGestorVO.getListaCoresGraficoCategoriaDespesa().isEmpty()) {
				painelGestorVO.setListaCoresGraficoCategoriaDespesa("\"" + HtmlColor.encodeRGB(color) + "\"");
			} else {
				painelGestorVO.setListaCoresGraficoCategoriaDespesa(painelGestorVO.getListaCoresGraficoCategoriaDespesa() + ", \"" + HtmlColor.encodeRGB(color) + "\"");
			}
			if (rs.getString("identificadorCategoriaDespesa") == null || rs.getString("identificadorCategoriaDespesa").isEmpty()) {
				painelGestorVO.getLegendaGraficoCategoriaDespesaVOs().add(new LegendaGraficoVO(0, "0 - Não Informado", "-1", rs.getDouble("valor"), HtmlColor.encodeRGB(color), true));
			} else {
				painelGestorVO.getLegendaGraficoCategoriaDespesaVOs().add(new LegendaGraficoVO(0, rs.getString("identificadorCategoriaDespesa") + "-" + rs.getString("categoriaDespesa"), rs.getString("identificadorCategoriaDespesa"), rs.getDouble("valor"), HtmlColor.encodeRGB(color), true));
			}
		}
		painelGestorVO.setValorTotalPorCategoriaDespesa(painelGestorVO.getValorTotalPagarPorCategoriaDespesa() + painelGestorVO.getValorTotalPagoPorCategoriaDespesa());
		if (telaInicial) {
			painelGestorVO.setListaValoresCategoriaDespesaInicial(Uteis.realizarMontagemDadosGraficoPizza(painelGestorVO.getLegendaGraficoCategoriaDespesaVOs()));
		} else {
			painelGestorVO.setListaValoresCategoriaDespesa(Uteis.realizarMontagemDadosGraficoPizza(painelGestorVO.getLegendaGraficoCategoriaDespesaVOs()));
		}
		painelGestorVO.getListaNivelGraficoCategoriaDespesa().clear();
		painelGestorVO.getListaNivelGraficoCategoriaDespesa().add("início");
	}
	
	@Override
	public void executarCriacaoGraficoConsumo(PainelGestorVO painelGestorVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Date dataInicio, Date dataTermino,Boolean telaInicial) throws Exception {
		dataInicio = Uteis.getDataVencimentoPadrao(1, dataInicio, 0);
		dataTermino = Uteis.getDataUltimoDiaMes(dataTermino);
		painelGestorVO.getLegendaGraficoConsumoVO().clear();
		SqlRowSet rs = consultarGraficoConsumoaPainelGestorFinanceiro(unidadeEnsinoVOs, dataInicio, dataTermino);
		Color color = Color.GREEN;
		while (rs.next()) {
			painelGestorVO.setValorTotalPagarConsumo(painelGestorVO.getValorTotalPagarConsumo() + rs.getDouble("valorPagar"));
			color = Uteis.gerarCorAleatoria(color);
			while (painelGestorVO.getListaCoresGraficoConsumo().contains(HtmlColor.encodeRGB(color))) {
				color = Uteis.gerarCorAleatoria(color);
			}
			
			if (painelGestorVO.getListaCoresGraficoConsumo().isEmpty()) {
				painelGestorVO.setListaCoresGraficoConsumo("\"" + HtmlColor.encodeRGB(color) + "\"");
			} else {
				painelGestorVO.setListaCoresGraficoConsumo(painelGestorVO.getListaCoresGraficoConsumo() + ", \"" + HtmlColor.encodeRGB(color) + "\"");
			}
			
			painelGestorVO.getLegendaGraficoConsumoVO().add(new LegendaGraficoVO(0, String.valueOf(rs.getInt("departamento")) + "-" + rs.getString("nome"), String.valueOf(rs.getInt("departamento")), rs.getDouble("valorPagar"), HtmlColor.encodeRGB(color), true));
		}
//		painelGestorVO.setValorTotalPagarConsumo(painelGestorVO.getValorTotalPagarConsumo() + painelGestorVO.getValorTotalPagarConsumo());
		if (telaInicial) {
			painelGestorVO.setListaValoresConsumoInicial(Uteis.realizarMontagemDadosGraficoPizza(painelGestorVO.getLegendaGraficoConsumoVO()));
		} else {
			painelGestorVO.setListaValoresConsumo(Uteis.realizarMontagemDadosGraficoPizza(painelGestorVO.getLegendaGraficoConsumoVO()));
		}

	}

	@Override
	public Integer consultarQtdeAlunoAtivoAtualmente(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		StringBuilder sql = new StringBuilder("select count(distinct matricula.matricula) as qtde from matricula ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" WHERE matricula.situacao in ('AT', 'CF') and matriculaperiodo.situacaoMatriculaPeriodo = 'AT' ");
		sql.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	public String getSqlConsultaAcademicoAlunosAtivos(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, count(distinct codigo)  as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;
		for (int y = 0; y < numeroMes; y++) {
			if (y > 0) {
				sb.append(" union all ");
			}
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, ");
			if ((curso == null || curso == 0) && (nivelEducacional == null || nivelEducacional.trim().isEmpty())) {
				sb.append(" extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
				sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano , ");
			}
			sb.append(" nivelEducacional from matricula ");
			sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" and (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) = ( ");
			sb.append(" select max(mp.ano||'/'||mp.semestre) from matriculaperiodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and mp.situacaomatriculaperiodo not in ('PC', 'PR') ");
			sb.append(" and mp.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE ) ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo");
			sb.append(" where  (matricula.dataAlunoConcluiuDisciplinasRegulares is null or dataAlunoConcluiuDisciplinasRegulares::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ) ");

			sb.append(" and ((matriculaperiodo.situacaomatriculaperiodo in ('TR', 'CA', 'AC', 'TS', 'TI')  and matriculaperiodo.datafechamentomatriculaperiodo::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) ");
			sb.append(" or ( matriculaperiodo.datafechamentomatriculaperiodo::DATE >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append("'::DATE and matriculaperiodo.situacaomatriculaperiodo in ('FI')) ");
			sb.append(" or (matriculaperiodo.situacaomatriculaperiodo in ('AT') and matriculaperiodo.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("')) ");

			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
			if (curso != null && curso > 0) {
				sb.append("  and curso.codigo = ").append(curso);
			} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
				sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
			}

		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoNaoRenovaram(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();

		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;

		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0  as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, count(distinct codigo) as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		for (int y = 0; y < numeroMes; y++) {
			if (y > 0) {
				sb.append(" union all ");
			}
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" mpant.matricula  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
			sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano , nivelEducacional from matricula ");
			sb.append(" inner join matriculaperiodo mpant on matricula.matricula = mpant.matricula ");
			sb.append(" and (mpant.ano||'/'||mpant.semestre) = ( select max(mp.ano||'/'||mp.semestre) as anosemeste from matriculaperiodo mp   ");
			sb.append(" where mp.matricula = matricula.matricula and mp.datafechamentomatriculaperiodo is not null ");
			sb.append(" and mp.situacaomatriculaperiodo not in ('PC', 'PR') ");
			sb.append(" and mp.datafechamentomatriculaperiodo::DATE < '").append(Uteis.getDataJDBC(dataInicioTmp)).append("'::DATE");
			sb.append(" ) ");

			sb.append(" inner join curso on matricula.curso = curso.codigo and curso.nivelEducacional not in ('PO', 'EX') ");
			sb.append(" inner join turma on mpant.turma = turma.codigo ");
			sb.append(" where mpant.situacaomatriculaperiodo = 'FI' ");
			sb.append(" and matricula.matricula not in (select mp.matricula from MatriculaPeriodo mp ");
			sb.append(" where mp.matricula = matricula.matricula  and mp.situacaomatriculaperiodo not in ('PC', 'PR')  ");
			sb.append(" and mp.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
			sb.append(" and (mp.ano||'/'||mp.semestre) > (mpant.ano||'/'||mpant.semestre)   limit 1) ");

			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
			if (curso != null && curso > 0) {
				sb.append("  and curso.codigo = ").append(curso);
			} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
				sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
			}

		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoConcluiramDisciplinasRegulares(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();

		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;

		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, count(distinct codigo) as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		for (int y = 0; y < numeroMes; y++) {
			if (y > 0) {
				sb.append(" union all ");
			}
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
			sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano , nivelEducacional from matriculaperiodo ");
			sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
			sb.append(" where  (dataAlunoConcluiuDisciplinasRegulares::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("') AND ((datafechamentomatriculaperiodo is not null and datafechamentomatriculaperiodo::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
			sb.append(" and  matriculaperiodo.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ) ");
			sb.append(" or  (datafechamentomatriculaperiodo is null and matriculaperiodo.situacaomatriculaperiodo = 'AT' and matricula.situacao = 'AT' and  matriculaperiodo.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("')) ");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
			if (curso != null && curso > 0) {
				sb.append("  and curso.codigo = ").append(curso);
			} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
				sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
			}
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoPreMatriculados(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();

		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;

		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, count(distinct codigo) as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		for (int y = 0; y < numeroMes; y++) {
			if (y > 0) {
				sb.append(" union all ");
			}
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
			sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano , nivelEducacional from matriculaperiodo ");
			sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
			sb.append(" where matriculaperiodo.situacaomatriculaperiodo = 'PR' and  matriculaperiodo.data::DATE >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append("' and matriculaperiodo.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
			if (curso != null && curso > 0) {
				sb.append("  and curso.codigo = ").append(curso);
			} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
				sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
			}
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoNovato(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();

		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;

		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, count(distinct codigo) as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		for (int y = 0; y < numeroMes; y++) {
			if (y > 0) {
				sb.append(" union all ");
			}
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
			sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano , nivelEducacional from matriculaperiodo ");
			sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
			sb.append(" where matriculaperiodo.data::DATE >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append("' and matriculaperiodo.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
			sb.append(" and 0 = (select count(mp.codigo) from matriculaperiodo as mp where mp.codigo <> matriculaperiodo.codigo and mp.situacaomatriculaperiodo not in ('PR', 'PC') and matriculaperiodo.matricula =  mp.matricula and (mp.ano||'/'||mp.semestre) <  (matriculaperiodo.ano||'/'||matriculaperiodo.semestre))");
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC')  ");
			// sb.append(" and (matriculaperiodo.datafechamentomatriculaperiodo is null or matriculaperiodo.datafechamentomatriculaperiodo::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) ");

			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
			if (curso != null && curso > 0) {
				sb.append("  and curso.codigo = ").append(curso);
			} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
				sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
			}
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoRenovado(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();

		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;

		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, count(distinct codigo) as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		for (int y = 0; y < numeroMes; y++) {
			if (y > 0) {
				sb.append(" union all ");
			}
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
			sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano , nivelEducacional from matricula ");
			sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" and matriculaperiodo.data::DATE >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append("'  ");
			sb.append(" and matriculaperiodo.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') ");
			sb.append(" and (matriculaperiodo.datafechamentomatriculaperiodo is null or matriculaperiodo.datafechamentomatriculaperiodo::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) ");
			sb.append(" inner join matriculaperiodo mp on matricula.matricula = mp.matricula   ");
			sb.append(" and (mp.ano||'/'||mp.semestre) = (select max(mp2.ano||'/'||mp2.semestre) from matriculaperiodo as mp2 where mp2.codigo <> matriculaperiodo.codigo ");
			sb.append(" and mp.situacaomatriculaperiodo not in ('PR', 'PC', 'AT') ");
			sb.append(" and matriculaperiodo.matricula =  mp2.matricula and (mp2.ano||'/'||mp2.semestre) <  (matriculaperiodo.ano||'/'||matriculaperiodo.semestre)) ");
			sb.append(" and mp.situacaomatriculaperiodo = 'FI' ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");

			sb.append(" where ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
			if (curso != null && curso > 0) {
				sb.append("  and curso.codigo = ").append(curso);
			} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
				sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
			}
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoRetornoEvasao(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();

		int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
		if (numeroMes == 0) {
			numeroMes = 1;
		}
		Date dataInicioTmp = null;
		Date dataTerminoTmp = null;

		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, count(distinct codigo) retornoEvasao, 0 as transferenciaInterna from ( ");
		for (int y = 0; y < numeroMes; y++) {
			if (y > 0) {
				sb.append(" union all ");
			}
			dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
			dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
			sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
			sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano , nivelEducacional from matriculaperiodo ");
			sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" inner join matriculaperiodo mp on matricula.matricula = mp.matricula   ");
			sb.append(" and (mp.ano||'/'||mp.semestre) = (select max(mp2.ano||'/'||mp2.semestre) from matriculaperiodo as mp2 where mp2.codigo <> matriculaperiodo.codigo ");
			sb.append(" and matriculaperiodo.matricula =  mp2.matricula and mp2.situacaomatriculaperiodo not in ('PC') and (mp2.ano||'/'||mp2.semestre) <  (matriculaperiodo.ano||'/'||matriculaperiodo.semestre)) ");

			// sb.append(" and mp.codigo = (select mp2.codigo from matriculaperiodo as mp2 where mp2.codigo <> matriculaperiodo.codigo ");
			// sb.append(" and matriculaperiodo.matricula =  mp2.matricula and mp2.data <  matriculaperiodo.data and mp2.situacaomatriculaperiodo not in ('PC') order by mp2.data desc limit 1) ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
			sb.append(" where matriculaperiodo.data::DATE >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append("'  ");
			sb.append(" and matriculaperiodo.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') ");
			sb.append(" and mp.situacaomatriculaperiodo in ('CA', 'TR', 'AC', 'TS') ");
			sb.append(" and (matriculaperiodo.datafechamentomatriculaperiodo is null or matriculaperiodo.datafechamentomatriculaperiodo::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) ");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
			if (curso != null && curso > 0) {
				sb.append("  and curso.codigo = ").append(curso);
			} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
				sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
			}
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoCancelamento(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, count(distinct codigo) as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
		sb.append(" matriculaperiodo.matricula  as codigo, extract(month from matriculaperiodo.datafechamentomatriculaperiodo) as mes,");
		sb.append(" extract(year from matriculaperiodo.datafechamentomatriculaperiodo) as ano , nivelEducacional from matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sb.append(" where matriculaperiodo.datafechamentomatriculaperiodo::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE ");
		sb.append(" and matriculaperiodo.datafechamentomatriculaperiodo::date <= '").append(Uteis.getDataJDBC(dataTermino)).append("'::DATE ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'CA' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		if (curso != null && curso > 0) {
			sb.append("  and curso.codigo = ").append(curso);
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoTransferenciaInterna(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, count(distinct codigo) as transferenciaInterna from ( ");
		sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
		sb.append(" matriculaperiodo.matricula  as codigo, extract(month from matriculaperiodo.datafechamentomatriculaperiodo) as mes,");
		sb.append(" extract(year from matriculaperiodo.datafechamentomatriculaperiodo) as ano , nivelEducacional from matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sb.append(" where matriculaperiodo.datafechamentomatriculaperiodo::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE ");
		sb.append(" and matriculaperiodo.datafechamentomatriculaperiodo::date <= '").append(Uteis.getDataJDBC(dataTermino)).append("'::DATE ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'TI' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		if (curso != null && curso > 0) {
			sb.append("  and curso.codigo = ").append(curso);
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoTrancado(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, count(distinct codigo) as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
		sb.append(" matriculaperiodo.matricula  as codigo, extract(month from matriculaperiodo.datafechamentomatriculaperiodo) as mes,");
		sb.append(" extract(year from matriculaperiodo.datafechamentomatriculaperiodo) as ano , nivelEducacional from matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sb.append(" where matriculaperiodo.datafechamentomatriculaperiodo::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE ");
		sb.append(" and matriculaperiodo.datafechamentomatriculaperiodo::date <= '").append(Uteis.getDataJDBC(dataTermino)).append("'::DATE ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'TR' ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		if (curso != null && curso > 0) {
			sb.append("  and curso.codigo = ").append(curso);
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoTransferenciaSaida(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, count(distinct codigo) as transferencia, 0 as prematricula, 0 as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
		sb.append(" matriculaperiodo.matricula  as codigo, extract(month from matriculaperiodo.datafechamentomatriculaperiodo) as mes,");
		sb.append(" extract(year from matriculaperiodo.datafechamentomatriculaperiodo) as ano , nivelEducacional from matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sb.append(" where matriculaperiodo.datafechamentomatriculaperiodo::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE ");
		sb.append(" and matriculaperiodo.datafechamentomatriculaperiodo::date <= '").append(Uteis.getDataJDBC(dataTermino)).append("'::DATE ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'TS' ");
		sb.append(" and (alunotransferidounidade = false or alunotransferidounidade is null) ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		if (curso != null && curso > 0) {
			sb.append("  and curso.codigo = ").append(curso);
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoAbandonoCurso(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, count(distinct codigo) as abandono, 0 as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
		sb.append(" matriculaperiodo.matricula  as codigo, extract(month from matriculaperiodo.datafechamentomatriculaperiodo) as mes,");
		sb.append(" extract(year from matriculaperiodo.datafechamentomatriculaperiodo) as ano , nivelEducacional from matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sb.append(" where matriculaperiodo.datafechamentomatriculaperiodo::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE ");
		sb.append(" and matriculaperiodo.datafechamentomatriculaperiodo::date <= '").append(Uteis.getDataJDBC(dataTermino)).append("'::DATE ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AC' ");

		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		if (curso != null && curso > 0) {
			sb.append("  and curso.codigo = ").append(curso);
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	public String getSqlConsultaAcademicoFormado(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, ");
		} else {
			sb.append(" ano, mes, ");
		}
		sb.append(" nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as novato, 0 as renovado, 0 as cancelado, 0 as trancado, 0 as transferencia, 0 as prematricula, 0 as abandono, count(distinct codigo) as formado, 0 as naoRenovaram, 0 retornoEvasao, 0 as transferenciaInterna from ( ");
		sb.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
		sb.append(" matriculaperiodo.matricula  as codigo, extract(month from matriculaperiodo.datafechamentomatriculaperiodo) as mes,");
		sb.append(" extract(year from matriculaperiodo.datafechamentomatriculaperiodo) as ano , nivelEducacional from matriculaperiodo ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sb.append(" inner join curso on matricula.curso = curso.codigo");
		sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sb.append(" where matriculaperiodo.datafechamentomatriculaperiodo::date >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE ");
		sb.append(" and matriculaperiodo.datafechamentomatriculaperiodo::date <= '").append(Uteis.getDataJDBC(dataTermino)).append("'::DATE ");
		sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'FO' ");
		sb.append(" and (alunotransferidounidade = false or alunotransferidounidade is null) ");
		sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));
		if (curso != null && curso > 0) {
			sb.append("  and curso.codigo = ").append(curso);
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" and nivelEducacional =  '").append(nivelEducacional).append("' ");
		}
		sb.append(" ) as t group by");
		if (curso != null && curso > 0) {
			sb.append(" codigoCurso, nomeCurso, codigoTurma, nomeTurma, nivelEducacional ");
		} else if (nivelEducacional != null && !nivelEducacional.trim().isEmpty()) {
			sb.append(" codigoCurso, nomeCurso, nivelEducacional ");
		} else {
			sb.append(" ano, mes, nivelEducacional ");
		}
		return sb.toString();
	}

	@Override
	public String consultarMatriculasSeremDesconsideradasMesAtual(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {

		Date dataInicioTmp = Uteis.getDataVencimentoPadrao(1, new Date(), 0);
		Date dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);

		StringBuilder sql = new StringBuilder("SELECT count(distinct codigo) as qtde, situacaomatriculaperiodo from ( ");
		sql.append(" select distinct curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma,  matriculaperiodo.matricula  as codigo, ");
		sql.append(" nivelEducacional, situacaomatriculaperiodo , datafechamentomatriculaperiodo ");
		sql.append(" from matricula   ");
		sql.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula  and matriculaperiodo.codigo = ( ");
		sql.append(" select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula  and mp.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE ");
		sql.append(" and (mp.datafechamentomatriculaperiodo is null or  mp.datafechamentomatriculaperiodo::DATE >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append("'::DATE)  and mp.situacaomatriculaperiodo not in ('PC')  order by mp.data desc limit 1 ) ");
		sql.append(" inner join curso on matricula.curso = curso.codigo  ");
		sql.append(" inner join turma on matriculaperiodo.turma = turma.codigo ");
		sql.append(" where  (matricula.dataAlunoConcluiuDisciplinasRegulares is null or dataAlunoConcluiuDisciplinasRegulares::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ) ");
		sql.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));

		sql.append(" and ((matriculaperiodo.datafechamentomatriculaperiodo is null and matricula.situacao = 'AT')  ");
		sql.append(" or  (matriculaperiodo.datafechamentomatriculaperiodo::DATE >= '").append(Uteis.getDataJDBC(dataInicioTmp)).append("'::DATE)) ");
		sql.append(" and matricula.matricula not in (   ");
		sql.append(" select mp2.matricula  as codigo  from matriculaperiodo mp2 ");
		sql.append(" inner join matricula as m on m.matricula = mp2.matricula   ");
		sql.append(" left join matriculaperiodo as mant on m.matricula = mant.matricula and mant.codigo = ( ");
		sql.append(" select mp.codigo from matriculaperiodo as mp where mp.codigo <> mp2.codigo   and mp2.matricula =  mp.matricula and mp.data <  mp2.data order by mp.data desc limit 1  ) ");
		sql.append(" inner join curso as c on m.curso = c.codigo  where mp2.data::DATE >= matriculaperiodo.data::DATE and m.matricula = matricula.matricula and  mp2.data::DATE <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'  ");
		sql.append(" and ((mant.codigo is null and mp2.situacaomatriculaperiodo in ('PR', 'PC'))  or (mant.codigo is not null  ");
		sql.append(" and mant.situacaomatriculaperiodo in ('CA', 'TR', 'AC', 'TS', 'TI')  ");
		sql.append(" and mp2.situacaomatriculaperiodo in ('PR', 'PC')))  ");
		sql.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "m.unidadeensino")).append(" ) ");
		sql.append(" ) as t  ");
		sql.append(" where situacaomatriculaperiodo in ('PR', 'TR', 'CA', 'AC', 'TS', 'FI', 'TI') ");
		sql.append(" group by situacaomatriculaperiodo order by qtde ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		StringBuilder msg = null;
		Integer qtdeTotal = 0;
		while (rs.next()) {
			if (msg == null) {
				msg = new StringBuilder("<div>Foram desconsiderados na contagem dos alunos ativos atualmente os seguintes casos:</div>");
			}
			if (rs.getString("situacaomatriculaperiodo").equals("PR")) {
				msg.append("<div>").append(rs.getInt("qtde")).append(" pré-matriculados de veteranos</div>");
			} else if (rs.getString("situacaomatriculaperiodo").equals("CA")) {
				msg.append("<div>").append(rs.getInt("qtde")).append(" cancelamento(s) no mês atual</div>");
			} else if (rs.getString("situacaomatriculaperiodo").equals("TR")) {
				msg.append("<div>").append(rs.getInt("qtde")).append(" trancamento(s) no mês atual</div>");
			} else if (rs.getString("situacaomatriculaperiodo").equals("TS")) {
				msg.append("<div>").append(rs.getInt("qtde")).append(" transferência(s) no mês atual</div>");
			} else if (rs.getString("situacaomatriculaperiodo").equals("TI")) {
				msg.append("<div>").append(rs.getInt("qtde")).append(" transferência(s) interna(s) no mês atual</div>");
			} else if (rs.getString("situacaomatriculaperiodo").equals("AC")) {
				msg.append("<div>").append(rs.getInt("qtde")).append(" abandono de curso no mês atual</div>");
			} else if (rs.getString("situacaomatriculaperiodo").equals("FI")) {
				msg.append("<div>").append(rs.getInt("qtde")).append(" finalização(ões) de matrículas no mês atual</div>");
			} else {
				msg.append("<div>").append(rs.getInt("qtde")).append(" de matrículas inconsistêntes no mês atual</div>");
			}
			qtdeTotal += rs.getInt("qtde");
		}
		if (msg == null) {
			msg = new StringBuilder("");
		} else {
			msg.append("<div><strong>Totalizando ").append(qtdeTotal).append(" de matrículas não consideradas</strong></div>");
		}
		return msg.toString();
	}

	
	public String getSqlConsultaAcademicoAlunosAtivosPorNivelEducacional(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso, String ano, String semestre) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		sb.append(" periodicidade, nivelEducacional, count(distinct codigo)  as ativo, 0 as aptoFormar, 0 as prematricula, 0 retornoEvasao from ( ");
			
			sb.append(" select distinct curso.periodicidade, curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, nivelEducacional ");
			sb.append(" from matricula ");
			sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo");
			sb.append(" INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo ");
			
//			FILTRO ANUAL
			sb.append(" where case when curso.periodicidade = 'AN' then matriculaperiodo.ano = '").append(ano).append("'  ");
//			FILTRO SEMESTRAL
			sb.append(" else case when curso.periodicidade = 'SE' then ");
			sb.append(" matriculaperiodo.ano = '").append(ano).append("' ");
			sb.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			sb.append(" else true end end ");
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT') ");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));

		sb.append(" ) as t group by periodicidade, nivelEducacional ");
		
		return sb.toString();
	}
	
	public String getSqlConsultaAcademicoAlunosPreMatriculadoPorNivelEducacional(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso, String ano, String semestre) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		sb.append(" periodicidade, nivelEducacional, 0 as ativo, 0 as aptoFormar, count(distinct codigo) as prematricula, 0 retornoEvasao from ( ");
			
			sb.append(" select distinct curso.periodicidade, curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, nivelEducacional ");
			sb.append(" from matricula ");
			sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo");
			sb.append(" INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo ");
			
//			FILTRO ANUAL
			sb.append(" where case when curso.periodicidade = 'AN' then matriculaperiodo.ano = '").append(ano).append("'  ");
//			FILTRO SEMESTRAL
			sb.append(" else case when curso.periodicidade = 'SE' then ");
			sb.append(" matriculaperiodo.ano = '").append(ano).append("' ");
			sb.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			sb.append(" else true end end ");
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo in ('PR') ");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));

		sb.append(" ) as t group by periodicidade, nivelEducacional ");
		
		return sb.toString();
	}
	
	public String getSqlConsultaAcademicoAlunosEvasaoPorNivelEducacional(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso, String ano, String semestre) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		sb.append(" periodicidade, nivelEducacional, 0 as ativo, 0 as aptoFormar, 0 as prematricula, count(distinct codigo) retornoEvasao from ( ");
			
			sb.append(" select distinct curso.periodicidade, curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, nivelEducacional ");
			sb.append(" from matriculaperiodo  ");
			sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" inner join matriculaperiodo mp on matricula.matricula = mp.matricula ");
			sb.append(" and (mp.ano||'/'||mp.semestre) = (");
			sb.append(" select max(mp2.ano||'/'||mp2.semestre) from matriculaperiodo as mp2 where mp2.codigo <> matriculaperiodo.codigo  ");
			sb.append(" and matriculaperiodo.matricula =  mp2.matricula and mp2.situacaomatriculaperiodo not in ('PC') ");
			sb.append(" and (mp2.ano||'/'||mp2.semestre) <  (matriculaperiodo.ano||'/'||matriculaperiodo.semestre)) ");
			
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo");
			
//			FILTRO ANUAL
			sb.append(" where case when curso.periodicidade = 'AN' then matriculaperiodo.ano = '").append(ano).append("'  ");
//			FILTRO SEMESTRAL
			sb.append(" else case when curso.periodicidade = 'SE' then ");
			sb.append(" matriculaperiodo.ano = '").append(ano).append("' ");
			sb.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			sb.append(" else true end end ");
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') ");
			sb.append(" and mp.situacaomatriculaperiodo in ('CA', 'TR', 'AC', 'TS') ");
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));

		sb.append(" ) as t group by periodicidade, nivelEducacional ");
		
		return sb.toString();
	}
	
	
	public String getSqlConsultaAcademicoAlunosPossiveisFormandosPorNivelEducacional(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String nivelEducacional, Integer curso, String ano, String semestre) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		sb.append(" periodicidade, nivelEducacional, 0 as ativo, count(distinct codigo) as aptoFormar, 0 as prematricula, 0 retornoEvasao from ( ");
			
			sb.append(" select distinct curso.periodicidade, curso.codigo as codigoCurso, curso.nome as nomeCurso, turma.codigo as codigoTurma, turma.identificadorTurma as nomeTurma, ");
			sb.append(" matriculaperiodo.matricula  as codigo, nivelEducacional, ");
			sb.append(" (select sum(cargahoraria) from gradedisciplina  ");
			sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sb.append(" where periodoletivo.gradecurricular = gradecurricular.codigo ");
			sb.append(" and gradedisciplina.tipodisciplina not in ('OP', 'LO') ");
			sb.append(" )as cargahorariaobrigatoria, ");
			sb.append(" (select sum(cargahorariadisciplina) from (select historico.cargahorariadisciplina from historico ");
			sb.append(" inner join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
			sb.append(" where historico.matricula  = matricula.matricula ");
			sb.append(" and matrizcurricular = gradecurricular.codigo ");
			sb.append(" and gradedisciplina.tipodisciplina in ('OP', 'LO') ");
			sb.append(" and situacao in ('AA', 'AP', 'IS', 'CC', 'CS', 'CH', 'AE', 'AB', 'CE')  ");
			sb.append(" union all  ");
			sb.append(" select historico.cargahorariadisciplina from historico ");
			sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina  ");
			sb.append(" where matricula  = matricula.matricula ");
			sb.append(" and historico.matrizcurricular = gradecurricular.codigo  ");
			sb.append(" and situacao in ('AA', 'AP', 'IS', 'CC', 'CS','CH', 'AE')");
			sb.append(" )as t ) as cargaHorariaOptativaCumprida , cargahoraria, totalcargahorariaestagio, totalcargahorariaatividadecomplementar ");
			
			sb.append(" from matricula ");
			sb.append(" inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
			sb.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaoMatriculaPeriodo != 'PC' ");
			sb.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
			sb.append(" inner join curso on matricula.curso = curso.codigo");
			sb.append(" inner join turma on matriculaperiodo.turma = turma.codigo");
			sb.append(" inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo ");

			sb.append(" where case when curso.periodicidade = 'AN' then matriculaperiodo.ano = '").append(ano).append("'  ");
//			FILTRO SEMESTRAL
			sb.append(" else case when curso.periodicidade = 'SE' then ");
			sb.append(" matriculaperiodo.ano = '").append(ano).append("' ");
			sb.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			sb.append(" else true end end ");
			sb.append(" and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI') ");
			
			sb.append(" and not exists (");
			sb.append(" select gradedisciplina.codigo from gradedisciplina ");
			sb.append(" inner join periodoletivo on periodoletivo.codigo = gradedisciplina.periodoletivo ");
			sb.append(" where periodoletivo.gradecurricular = matricula.gradecurricularatual ");
			sb.append(" and gradedisciplina.tipodisciplina in ('OB', 'LG') ");
			sb.append(" and gradedisciplina.codigo not in (select historico.gradedisciplina from historico ");
			sb.append(" where matricula  = matricula.matricula and matrizcurricular = matricula.gradecurricularatual ");
			sb.append(" and gradedisciplina is not null and historico.situacao in ('AA', 'AP', 'IS', 'CC', 'CH', 'CS', 'AE', 'CE', 'AB') and historico.gradedisciplina = gradedisciplina.codigo ) ) ");
			
			sb.append(" and ").append(getFiltroUnidadeEnsino(unidadeEnsinoVOs, "matricula.unidadeensino"));

		sb.append(" ) as t ");
		sb.append(" WHERE case when (cargaHorariaOptativaCumprida >= 0 or cargaHorariaOptativaCumprida IS NULL) then ");
		sb.append(" case when (CASE WHEN cargaHorariaOptativaCumprida IS NULL THEN 0 ELSE cargaHorariaOptativaCumprida END) >= cargaHoraria - totalcargahorariaestagio - totalcargahorariaatividadecomplementar - cargahorariaobrigatoria  ");
		sb.append(" then true else false end ");
		sb.append(" else true end ");
		sb.append(" group by periodicidade, nivelEducacional ");
		
		
		return sb.toString();
	}
	

	public void montarDadosPainelGestorFinanceiroAcademicoPorNivelEducacional(PainelGestorVO painelGestorVO, SqlRowSet rs, UsuarioVO usuarioVO) {
		PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO = new PainelGestorFinanceiroAcademicoMesAnoVO();
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoAptoFormar(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar() + rs.getInt("aptoFormar"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoAtivo(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() + rs.getInt("ativo"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoPreMatricula(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula() + rs.getInt("prematricula"));
		painelGestorFinanceiroAcademicoMesAnoVO.setTotalAlunoRetornoEvasao(painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRetornoEvasao() + rs.getInt("retornoEvasao"));
		painelGestorFinanceiroAcademicoMesAnoVO.setNivelEducacional(rs.getString("nivelEducacional"));
		painelGestorFinanceiroAcademicoMesAnoVO.setPeriodicidade(rs.getString("periodicidade"));
		painelGestorFinanceiroAcademicoMesAnoVO.setOrdem(rs.getInt("ordem"));
		if (painelGestorVO.getLegendaCategoriaPainelAcademicoNivelEducacional().equals("")) {
			painelGestorVO.setLegendaCategoriaPainelAcademicoNivelEducacional("'"+TipoNivelEducacional.getDescricao(rs.getString("nivelEducacional"))+"'");
		} else {
			painelGestorVO.setLegendaCategoriaPainelAcademicoNivelEducacional(painelGestorVO.getLegendaCategoriaPainelAcademicoNivelEducacional() + ", " + "'"+TipoNivelEducacional.getDescricao(rs.getString("nivelEducacional"))+"'");
		}
		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().add(painelGestorFinanceiroAcademicoMesAnoVO);
		
	}

	/**
	 * Este Método realiza os calculos dos totalizadores finais do Painel Gestor
	 * Acadêmico Financeiro sendo eles: 1 - Média de Alunos no Período (Se dá
	 * atraves da soma de alunos ativos de cada mês e dividido pelo número de
	 * meses); 2 - Total de Alunos Novos; 3 - Total de Alunos Renovados; 4 -
	 * Total de Trancamentos; 5 - Total de Cancelamentos; 6 - Total de Transf.
	 * de Saída; 7 - Total de Receitas do Período (A soma de todos os valores
	 * recebidos no periodo); 8 - Total de Despesas do Período (A soma de todos
	 * os valores pagos no periodo); 9 - Média de Receitas do Período (A soma de
	 * todas as médias de receitas de cada mês e dividido pelo número de meses);
	 * 10 - Média de despesas do Período (A soma de todas as médias de despesas
	 * de cada mês e dividido pelo número de meses);
	 * 
	 * @param painelGestorVO
	 * @param grafico
	 */
	public void executarCalculoFinalPainelGestorFinanceiroAcademicoPorNivelEducacional(PainelGestorVO painelGestorVO) {
		for (PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO : painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs()) {
			painelGestorVO.setTotalAlunoAtivo(painelGestorVO.getTotalAlunoAtivo() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo());
			painelGestorVO.setTotalAlunoAptoFormar(painelGestorVO.getTotalAlunoAptoFormar() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar());
			painelGestorVO.setTotalAlunoPreMatricula(painelGestorVO.getTotalAlunoPreMatricula() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula());
			painelGestorVO.setTotalAlunoRetornoEvasao(painelGestorVO.getTotalAlunoRetornoEvasao() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRetornoEvasao());
		}
	}
	
	/**
	 * Este método é responsavel em gerar o gráfico de linha do Painel Gestor
	 * Acadêmico mostrando: 1 - Total de Alunos Ativos no Mês; 2 - Total de
	 * Alunos Novos/Renovados; 3 - Total Evasão (Trancamento, Cancelamento e
	 * Transferência de Saida);
	 * 
	 * @param painelGestorVO
	 */
	public void executarCriacaoGraficoPainelGestorAcademicoPorNivelEducacional(PainelGestorVO painelGestorVO) throws Exception {

		
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().clear();

		painelGestorVO.setListaValoresAcademicoAtivo("");
		painelGestorVO.setListaValoresAcademicoAptoFormar("");
		painelGestorVO.setListaValoresAcademicoNovo("");
		painelGestorVO.setListaValoresAcademicoEvasao("");
		painelGestorVO.setListaValoresAcademicoPreMatricula("");
		LegendaGraficoVO legendaAtivo = new LegendaGraficoVO("Alunos Ativos", 0.0, "#3399FF");
		LegendaGraficoVO legendaPreMatricula = new LegendaGraficoVO("Alunos Pré-Matriculados", 0.0, "#999999");
		LegendaGraficoVO legendaAptoFormar = new LegendaGraficoVO("Alunos Possíveis Formandos", 0.0, "#FFCC00");
		LegendaGraficoVO legendaEvasao = new LegendaGraficoVO("Evasao (Cancelamento, Trancamento, Transferencia de Saida)", 0.0, "#3399FF");
		// Date data = null;
		for (PainelGestorFinanceiroAcademicoMesAnoVO painelGestorFinanceiroAcademicoMesAnoVO : painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs()) {
			
			if (painelGestorVO.getListaValoresAcademicoAtivo().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoAtivo(painelGestorVO.getListaValoresAcademicoAtivo() + "{id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() + "}");
			} else {
				painelGestorVO.setListaValoresAcademicoAtivo(painelGestorVO.getListaValoresAcademicoAtivo() + ", {id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo() + "}");
			}
			if (painelGestorVO.getListaValoresAcademicoAptoFormar().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoAptoFormar(painelGestorVO.getListaValoresAcademicoAptoFormar() + "{id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar() + "}");
			} else {
				painelGestorVO.setListaValoresAcademicoAptoFormar(painelGestorVO.getListaValoresAcademicoAptoFormar() + ", {id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar() + "}");
			}
			//NÃO É CONTROLADA EVASÃO PARA CURSOS INTEGRAIS
			if (!painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
				if (painelGestorVO.getListaValoresAcademicoEvasao().isEmpty()) {
					painelGestorVO.setListaValoresAcademicoEvasao(painelGestorVO.getListaValoresAcademicoEvasao() + "{id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRetornoEvasao() + "}");
				} else {
					painelGestorVO.setListaValoresAcademicoEvasao(painelGestorVO.getListaValoresAcademicoEvasao() + ", {id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRetornoEvasao() + "}");
				}
			}
			if (painelGestorVO.getListaValoresAcademicoPreMatricula().isEmpty()) {
				painelGestorVO.setListaValoresAcademicoPreMatricula(painelGestorVO.getListaValoresAcademicoPreMatricula() + "{id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula() + "}");
			} else {
				painelGestorVO.setListaValoresAcademicoPreMatricula(painelGestorVO.getListaValoresAcademicoPreMatricula() + ", {id: '"+painelGestorFinanceiroAcademicoMesAnoVO.getNivelEducacional()+"', name:'"+painelGestorFinanceiroAcademicoMesAnoVO.getPeriodicidade()+"',  y: " + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula() + "}");
			}

			legendaAtivo.setQuantidade(legendaAtivo.getQuantidade() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAtivo());
			legendaAptoFormar.setQuantidade(legendaAptoFormar.getQuantidade() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoAptoFormar());
			legendaEvasao.setQuantidade(legendaEvasao.getQuantidade() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoRetornoEvasao());
			legendaPreMatricula.setQuantidade(legendaPreMatricula.getQuantidade() + painelGestorFinanceiroAcademicoMesAnoVO.getTotalAlunoPreMatricula());
		}

		legendaAtivo.setLegenda("Alunos Ativos");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaAtivo);

		legendaAptoFormar.setLegenda("Alunos Possíveis Formandos");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaAptoFormar);

		legendaPreMatricula.setLegenda("Pré-Matriculados");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaPreMatricula);
		
		legendaEvasao.setLegenda("Evasão");
		painelGestorVO.getLegendaGraficoAcademicoFinanceiroVOs().add(legendaEvasao);

		painelGestorVO.getPainelGestorFinanceiroAcademicoMesAnoVOs().clear();
	}
}
