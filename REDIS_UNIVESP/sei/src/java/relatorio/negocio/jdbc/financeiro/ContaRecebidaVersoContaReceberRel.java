package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import relatorio.negocio.comuns.financeiro.ContaRecebidaVersoContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.financeiro.ContaRecebidaVersoContaReceberRelInterfaceFacade;

@Service
@Lazy
@Scope
public class ContaRecebidaVersoContaReceberRel extends ControleAcesso implements ContaRecebidaVersoContaReceberRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5093756638167189437L;

	@Override
	public List<ContaRecebidaVersoContaReceberRelVO> realizarGeracaoRelatorio(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataTermino, Integer curso, Integer turma, String tipoPessoa, String matricula, Integer candidato, Integer funcionario, Integer parceiro, Integer fornecedor, Integer requerente, Integer responsavelFinanceiro, Integer contaCorrente, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPlanoFinanceiroCurso, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Boolean filtrarContasRecebidasDataRecebimento, Boolean filtrarContasRegistroCobranca, String ordenarPor, Integer tipoRequerimento, UsuarioVO usuarioVO, Boolean considerarUnidadeFinanceira) throws Exception {
	    	consultar(getIdEntidade(), true, usuarioVO);
		validarDados(dataInicio, dataTermino, listaUnidadeEnsino);
		SqlRowSet rs = consultarDadosRelatorio(listaUnidadeEnsino, dataInicio, dataTermino, curso, turma, tipoPessoa, matricula, candidato, funcionario, parceiro, fornecedor, requerente, responsavelFinanceiro, contaCorrente, centroReceita, planoFinanceiroCurso, condicaoPlanoFinanceiroCurso, filtroRelatorioFinanceiroVO, filtrarContasRecebidasDataRecebimento, filtrarContasRegistroCobranca, ordenarPor, tipoRequerimento, considerarUnidadeFinanceira);
		return montarDadosConsulta(rs);
	}

	private void validarDados(Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws ConsistirException {
		if (dataInicio == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataInicio"));
		}
		if (dataTermino == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTermino"));
		}
		if (dataTermino.before(dataInicio)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTerminoMaiorDataInicio"));
		}
		boolean excessao = true;
		for (UnidadeEnsinoVO obj : listaUnidadeEnsino) {
			if (obj.getFiltrarUnidadeEnsino()) {
				excessao = false;
				break;
			}
		}
		if (excessao) {
			throw new ConsistirException("Ao menos uma Unidade de Ensino deve ser selecionada!");
		}
	}

	private SqlRowSet consultarDadosRelatorio(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataTermino, Integer curso, Integer turma, String tipoPessoa, String matricula, Integer candidato, Integer funcionario, Integer parceiro, Integer fornecedor, Integer requerente, Integer responsavelFinanceiro, Integer contaCorrente, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPlanoFinanceiroCurso, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Boolean filtrarContasRecebidasDataRecebimento, Boolean filtrarContasRegistroCobranca, String ordenarPor, Integer tipoRequerimento, Boolean considerarUnidadeFinanceira) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select case contareceber.tipopessoa when 'RF' then responsavelFinanceiro.nome when 'PA' then parceiro.nome when 'FO' then fornecedor.nome else pessoa.nome end as sacado, ");
		sql.append(" matricula.matricula, contaReceber.dataVencimento, contaReceber.parcela, contaReceber.nossonumero, contaReceber.tipoOrigem, contaReceber.valordescontocalculadoprimeirafaixadescontos, contareceber.valor::NUMERIC(20,2) as valor, valorrecebercalculado::NUMERIC(20,2) as valorRecebido, contareceber.situacao, ");
		sql.append(" contaReceber.tipopessoa, contareceber.codigo as contaReceber, contareceber.acrescimo, contareceber.valorindicereajusteporatraso, null as dataRecebimento, parceiro.isentarJuro as \"parceiro.isentarJuro\", parceiro.isentarMulta as \"parceiro.isentarMulta\", ");
		sql.append(" turma.identificadorTurma as turma, contareceber.valormultacalculado::NUMERIC(20,2) as multa, contareceber.valorjurocalculado::NUMERIC(20,2) as juro, ");
		sql.append(" contareceber.valordescontocalculado AS desconto, ");
//		sql.append(" case when (valordescontoalunojacalculado is null) then 0.0 else valordescontoalunojacalculado::NUMERIC(20,2) end +");
//		sql.append(" case when (descontoconvenio is null) then 0.0 else descontoconvenio::NUMERIC(20,2) end +");
//		sql.append(" case when (descontoinstituicao is null) then 0.0 else descontoinstituicao::NUMERIC(20,2) end +");
//		sql.append(" case when (valordescontoprogressivo is null) then 0.0 else valordescontoprogressivo::NUMERIC(20,2) end +");
//		sql.append(" case when (valordescontorateio is null) then 0.0 else valordescontorateio::NUMERIC(20,2) end +");
//		sql.append(" case when (valorCalculadoDescontoLancadoRecebimento is null) then 0.0 else valorCalculadoDescontoLancadoRecebimento::NUMERIC(20,2) end as desconto,  ");
		sql.append(" multaPorcentagem, juroPorcentagem, configuracaofinanceiro.tipoCalculoJuro, configuracaofinanceiro.percentualJuroPadrao, configuracaofinanceiro.percentualMultaPadrao ");
		sql.append(" from contareceber");
		sql.append(" LEFT join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sql.append(" LEFT join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sql.append(" LEFT JOIN formapagamentonegociacaorecebimento ON (negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaorecebimento) ");
		sql.append(" LEFT JOIN contacorrente ON (contareceber.contacorrente = contacorrente.codigo) ");
		sql.append(" left join matricula on matricula.matricula = matriculaaluno");
		sql.append(" left join matriculaperiodo on contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sql.append(" left join turma on turma.codigo = matriculaperiodo.turma");
		sql.append(" left join pessoa on pessoa.codigo = contareceber.pessoa");
		sql.append(" LEFT JOIN pessoa responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo)");
		sql.append(" LEFT JOIN parceiro on (contareceber.parceiro = parceiro.codigo)");
		sql.append(" LEFT JOIN fornecedor ON (contareceber.fornecedor = fornecedor.codigo)");
		sql.append(" left join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino");
		sql.append(" left join unidadeensino unidadeEnsinoFinanceira on unidadeEnsinoFinanceira.codigo = contareceber.unidadeensinofinanceira");
		sql.append(" left join configuracoes on configuracoes.codigo = unidadeensino.configuracoes");
		sql.append(" left join configuracaofinanceiro on configuracoes.codigo = configuracaofinanceiro.configuracoes");
		sql.append(" left join requerimento requerimento on requerimento.contareceber = contareceber.codigo ");
		sql.append(" left join tiporequerimento tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
		sql.append(" where contareceber.situacao = 'AR' and contareceber.valor > 0 ");
		if (filtrarContasRegistroCobranca != null && filtrarContasRegistroCobranca) {
			sql.append(" and (contareceber.registroCobrancaContaReceber is not null)");
		}
		if (!filtroRelatorioFinanceiroVO.getFiltrarPorDataCompetencia()) {
			sql.append(" and (contareceber.dataVencimento::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.dataVencimento::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("')");
		} else {
			sql.append(" and (contareceber.dataCompetencia::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.dataCompetencia::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("')");
		}
		adicionarFiltroTipoOrigem(filtroRelatorioFinanceiroVO, sql);
		
		if (!listaUnidadeEnsino.isEmpty()) {
			if(considerarUnidadeFinanceira) {
				sql.append(" and contareceber.unidadeensinofinanceira in (");
			}else {
				sql.append(" and contareceber.unidadeensino in (");				
			}
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sql.append(ue.getCodigo() + ", ");
				}
			}
			sql.append("0) ");
		}
		if (contaCorrente != null && contaCorrente > 0) {
			sql.append(" and (contacorrente.codigo = ").append(contaCorrente).append(") ");
		}
		if (centroReceita != null && centroReceita > 0) {
			sql.append(" and contareceber.centroReceita = ").append(centroReceita);
		}
		if (curso != null && curso > 0) {
			sql.append(" and matricula.curso = ").append(curso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}

		if (tipoPessoa != null && tipoPessoa.equals("AL")) {
			if (planoFinanceiroCurso != null && planoFinanceiroCurso > 0) {
				sql.append(" and matriculaPeriodo.planofinanceirocurso = ").append(planoFinanceiroCurso);
			}
			if (condicaoPlanoFinanceiroCurso != null && condicaoPlanoFinanceiroCurso > 0) {
				sql.append(" and matriculaPeriodo.condicaopagamentoplanofinanceirocurso = ").append(condicaoPlanoFinanceiroCurso);
			}
		}
		if (tipoPessoa != null && !tipoPessoa.trim().isEmpty()) {
			if (tipoPessoa.equals(TipoPessoa.ALUNO.getValor())) {
				sql.append(" and contareceber.tipoPessoa in ('").append(TipoPessoa.ALUNO.getValor()).append("', '").append(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()).append("') ");
			} else {
				sql.append(" and contareceber.tipoPessoa = '").append(tipoPessoa).append("' ");
			}
		}
		if (tipoPessoa != null && tipoPessoa.equals("AL") && matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (tipoPessoa != null && tipoPessoa.equals("FU") && funcionario != null && funcionario > 0) {
			sql.append(" and contareceber.funcionario = ").append(funcionario);
		}
		if (tipoPessoa != null && tipoPessoa.equals("RE") && requerente != null && requerente > 0) {
			sql.append(" and contareceber.pessoa = ").append(requerente);
		}
		if (tipoPessoa != null && tipoPessoa.equals("CA") && candidato != null && candidato > 0) {
			sql.append(" and contareceber.pessoa = ").append(candidato);
		}
		if (tipoPessoa != null && tipoPessoa.equals("RF") && responsavelFinanceiro != null && responsavelFinanceiro > 0) {
			sql.append(" and responsavelFinanceiro.codigo = ").append(responsavelFinanceiro);
		}
		if (tipoPessoa != null && tipoPessoa.equals("PA") && parceiro != null && parceiro > 0) {
			sql.append(" and parceiro.codigo = ").append(parceiro);
		}
		if (tipoPessoa != null && tipoPessoa.equals("FO") && fornecedor != null && fornecedor > 0) {
			sql.append(" and fornecedor.codigo = ").append(fornecedor);
		}
		if(filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento() && Uteis.isAtributoPreenchido(tipoRequerimento) ){
			sql.append(" and (tiporequerimento.codigo = ").append(tipoRequerimento).append(" OR tiporequerimento.codigo IS NULL ) ");
		}

		sql.append(" union all ");

		sql.append(" select distinct case contareceber.tipopessoa when 'RF' then responsavelFinanceiro.nome when 'PA' then parceiro.nome when 'FO' then fornecedor.nome else pessoa.nome end as sacado, ");
		sql.append(" matricula.matricula, contareceber.dataVencimento, contareceber.parcela, contareceber.nossonumero, contareceber.tipoOrigem, contareceber.valordescontocalculadoprimeirafaixadescontos, contareceber.valor::NUMERIC(20,2) as valor, valorRecebido::NUMERIC(20,2) as valorRecebido, contareceber.situacao, ");
		sql.append(" contaReceber.tipopessoa, contareceber.codigo as contaReceber, contareceber.acrescimo, contareceber.valorindicereajusteporatraso, negociacaorecebimento.data as dataRecebimento, parceiro.isentarJuro as \"parceiro.isentarJuro\", parceiro.isentarMulta as \"parceiro.isentarMulta\", ");
		sql.append(" turma.identificadorTurma as turma, contareceber.multa::NUMERIC(20,2) as multa, contareceber.juro::NUMERIC(20,2) as juro, ");
		sql.append(" case when (valordescontoalunojacalculado is null) then 0.0 else valordescontoalunojacalculado::NUMERIC(20,2) end +");
		sql.append(" case when (descontoconvenio is null) then 0.0 else descontoconvenio::NUMERIC(20,2) end +");
		sql.append(" case when (descontoinstituicao is null) then 0.0 else descontoinstituicao::NUMERIC(20,2) end +");
		sql.append(" case when (valordescontoprogressivo is null) then 0.0 else valordescontoprogressivo::NUMERIC(20,2) end +");
		sql.append(" case when (valordescontorateio is null) then 0.0 else valordescontorateio::NUMERIC(20,2) end +");
		sql.append(" case when (valorCalculadoDescontoLancadoRecebimento is null) then 0.0 else valorCalculadoDescontoLancadoRecebimento::NUMERIC(20,2) end as desconto,  ");
		sql.append(" multaPorcentagem, juroPorcentagem, configuracaofinanceiro.tipoCalculoJuro, configuracaofinanceiro.percentualJuroPadrao, configuracaofinanceiro.percentualMultaPadrao ");
		sql.append(" from contareceber");
		sql.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sql.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sql.append(" left JOIN formapagamentonegociacaorecebimento ON (negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaorecebimento) ");
		sql.append(" LEFT JOIN contacorrente ON (contareceber.contacorrente = contacorrente.codigo) ");
		sql.append(" left join matricula on matricula.matricula = matriculaaluno");
		sql.append(" left join matriculaperiodo on contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sql.append(" left join turma on turma.codigo = matriculaperiodo.turma");
		sql.append(" left join pessoa on pessoa.codigo = contareceber.pessoa");
		sql.append(" LEFT JOIN pessoa responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo)");
		sql.append(" LEFT JOIN parceiro on (contareceber.parceiro = parceiro.codigo)");
		sql.append(" LEFT JOIN fornecedor ON (contareceber.fornecedor = fornecedor.codigo)");
		sql.append(" left join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino");
		sql.append(" left join configuracoes on configuracoes.codigo = unidadeensino.configuracoes");
		sql.append(" left join configuracaofinanceiro on configuracoes.codigo = configuracaofinanceiro.configuracoes");
		sql.append(" left join requerimento requerimento on requerimento.contareceber = contareceber.codigo ");
		sql.append(" left join tiporequerimento tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento ");
		sql.append(" where contareceber.situacao = 'RE' and contareceber.valor > 0 ");
		if (filtrarContasRegistroCobranca != null && filtrarContasRegistroCobranca) {
			sql.append(" and (contareceber.registroCobrancaContaReceber is not null)");
		}
		if (filtrarContasRecebidasDataRecebimento != null && filtrarContasRecebidasDataRecebimento) {
			sql.append(" and negociacaorecebimento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and negociacaorecebimento.data::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		} else if (!filtroRelatorioFinanceiroVO.getFiltrarPorDataCompetencia()) {
			sql.append(" and (contareceber.dataVencimento::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.dataVencimento::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("')");
		} else {
			sql.append(" and (contareceber.dataCompetencia::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and contareceber.dataCompetencia::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("')");
		}
		adicionarFiltroTipoOrigem(filtroRelatorioFinanceiroVO, sql);
		if (!listaUnidadeEnsino.isEmpty()) {
			sql.append("and contareceber.unidadeensino in (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sql.append(ue.getCodigo() + ", ");
				}
			}
			sql.append("0) ");
		}
		if (contaCorrente != null && contaCorrente > 0) {
			sql.append(" and (formapagamentonegociacaorecebimento.contacorrente = ").append(contaCorrente).append(" or formapagamentonegociacaorecebimento.contacorrenteoperadoracartao = ").append(contaCorrente).append(" ) ");
		}
		if (centroReceita != null && centroReceita > 0) {
			sql.append(" and contareceber.centroReceita = ").append(centroReceita);
		}
		if (curso != null && curso > 0) {
			sql.append(" and matricula.curso = ").append(curso);
		}
		if (turma != null && turma > 0) {
			sql.append(" and turma.codigo = ").append(turma);
		}

		if (tipoPessoa != null && tipoPessoa.equals("AL")) {
			if (planoFinanceiroCurso != null && planoFinanceiroCurso > 0) {
				sql.append(" and matriculaPeriodo.planofinanceirocurso = ").append(planoFinanceiroCurso);
			}
			if (condicaoPlanoFinanceiroCurso != null && condicaoPlanoFinanceiroCurso > 0) {
				sql.append(" and matriculaPeriodo.condicaopagamentoplanofinanceirocurso = ").append(condicaoPlanoFinanceiroCurso);
			}
		}
		if (tipoPessoa != null && !tipoPessoa.trim().isEmpty()) {
			if (tipoPessoa.equals(TipoPessoa.ALUNO.getValor())) {
				sql.append(" and contareceber.tipoPessoa in ('").append(TipoPessoa.ALUNO.getValor()).append("', '").append(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()).append("') ");
			} else {
				sql.append(" and contareceber.tipoPessoa = '").append(tipoPessoa).append("' ");
			}
		}
		if (tipoPessoa != null && tipoPessoa.equals("AL") && matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		}
		if (tipoPessoa != null && tipoPessoa.equals("FU") && funcionario != null && funcionario > 0) {
			sql.append(" and contareceber.funcionario = ").append(funcionario);
		}
		if (tipoPessoa != null && tipoPessoa.equals("RE") && requerente != null && requerente > 0) {
			sql.append(" and contareceber.pessoa = ").append(requerente);
		}
		if (tipoPessoa != null && tipoPessoa.equals("CA") && candidato != null && candidato > 0) {
			sql.append(" and contareceber.pessoa = ").append(candidato);
		}
		if (tipoPessoa != null && tipoPessoa.equals("RF") && responsavelFinanceiro != null && responsavelFinanceiro > 0) {
			sql.append(" and responsavelFinanceiro.codigo = ").append(responsavelFinanceiro);
		}
		if (tipoPessoa != null && tipoPessoa.equals("PA") && parceiro != null && parceiro > 0) {
			sql.append(" and parceiro.codigo = ").append(parceiro);
		}
		if (tipoPessoa != null && tipoPessoa.equals("FO") && fornecedor != null && fornecedor > 0) {
			sql.append(" and fornecedor.codigo = ").append(fornecedor);
		}
		if(filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento() && Uteis.isAtributoPreenchido(tipoRequerimento) ){
			sql.append(" and (tiporequerimento.codigo = ").append(tipoRequerimento).append(" OR tiporequerimento.codigo IS NULL ) ");
		}
		sql.append(" order by ").append(ordenarPor);
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	private List<ContaRecebidaVersoContaReceberRelVO> montarDadosConsulta(SqlRowSet rs) throws Exception {
		List<ContaRecebidaVersoContaReceberRelVO> contaRecebidaVersoContaReceberRelVOs = new ArrayList<ContaRecebidaVersoContaReceberRelVO>(0);
		ContaRecebidaVersoContaReceberRelVO contaRecebidaVersoContaReceberRelVO = null;
		while (rs.next()) {
			contaRecebidaVersoContaReceberRelVO = new ContaRecebidaVersoContaReceberRelVO();
			contaRecebidaVersoContaReceberRelVO.setContaReceber(rs.getInt("contaReceber"));
			contaRecebidaVersoContaReceberRelVO.setDataRecebimento(rs.getDate("dataRecebimento"));
			contaRecebidaVersoContaReceberRelVO.setDataVencimento(rs.getDate("dataVencimento"));
			contaRecebidaVersoContaReceberRelVO.setMatricula(rs.getString("matricula"));
			contaRecebidaVersoContaReceberRelVO.setNossoNumero(rs.getString("nossoNumero"));
			contaRecebidaVersoContaReceberRelVO.setParcela(rs.getString("parcela"));
			contaRecebidaVersoContaReceberRelVO.setSacado(rs.getString("sacado"));
			contaRecebidaVersoContaReceberRelVO.setSituacao(rs.getString("situacao"));
			contaRecebidaVersoContaReceberRelVO.setTipoOrigem(rs.getString("tipoOrigem"));
			contaRecebidaVersoContaReceberRelVO.setTipoPessoa(rs.getString("tipoPessoa"));
			contaRecebidaVersoContaReceberRelVO.setTurma(rs.getString("turma"));
			contaRecebidaVersoContaReceberRelVO.setValor(rs.getDouble("valor"));
			contaRecebidaVersoContaReceberRelVO.setAcrescimo(rs.getDouble("acrescimo"));
			if (rs.getDouble("valorDescontoCalculadoPrimeiraFaixaDescontos") == 0.0 && rs.getDouble("desconto") == 0.0) {
				contaRecebidaVersoContaReceberRelVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(Uteis.arrendondarForcando2CadasDecimais(rs.getDouble("valor")));
			} else {
				contaRecebidaVersoContaReceberRelVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(rs.getDouble("valorDescontoCalculadoPrimeiraFaixaDescontos"));
			}
			contaRecebidaVersoContaReceberRelVO.setValorRecebido(rs.getDouble("valorRecebido"));
			contaRecebidaVersoContaReceberRelVO.setJuro(rs.getDouble("juro"));
			contaRecebidaVersoContaReceberRelVO.setMulta(rs.getDouble("multa"));
			contaRecebidaVersoContaReceberRelVO.setDesconto(rs.getDouble("desconto"));
			if (rs.getString("situacao").equals("AR") && rs.getString("tipoPessoa").equals("PA") && rs.getBoolean("parceiro.isentarJuro")) {
				contaRecebidaVersoContaReceberRelVO.setJuro(0.0);
			}
			if (rs.getString("situacao").equals("AR") && rs.getString("tipoPessoa").equals("PA") && rs.getBoolean("parceiro.isentarMulta")) {
				contaRecebidaVersoContaReceberRelVO.setMulta(0.0);
			}
			
			if(rs.getBigDecimal("valorindicereajusteporatraso") != null && rs.getBigDecimal("valorindicereajusteporatraso").doubleValue() >= 0.0 ){
				contaRecebidaVersoContaReceberRelVO.setAcrescimo(contaRecebidaVersoContaReceberRelVO.getAcrescimo() + rs.getBigDecimal("valorindicereajusteporatraso").doubleValue());
			}else if(rs.getBigDecimal("valorindicereajusteporatraso") != null && rs.getBigDecimal("valorindicereajusteporatraso").doubleValue() < 0.0 ){
				contaRecebidaVersoContaReceberRelVO.setDesconto(contaRecebidaVersoContaReceberRelVO.getDesconto() - rs.getBigDecimal("valorindicereajusteporatraso").doubleValue());
			}
			contaRecebidaVersoContaReceberRelVOs.add(contaRecebidaVersoContaReceberRelVO);
		}
		return contaRecebidaVersoContaReceberRelVOs;
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return "ContaRecebidaVersoContaReceberRel";
	}

	public static String getDesignIReportRelatorio() {
		return (getCaminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
	}

	public void adicionarFiltroTipoOrigem(FiltroRelatorioFinanceiroVO obj, StringBuilder sqlStr) {
		StringBuilder str = new StringBuilder();
		if (obj.getTipoOrigemBiblioteca()) {
			str.append(", 'BIB'");
		}
		if (obj.getTipoOrigemBolsaCusteadaConvenio()) {
			str.append(", 'BCC'");
		}
		if (obj.getTipoOrigemContratoReceita()) {
			str.append(", 'CTR'");
		}
		if (obj.getTipoOrigemDevolucaoCheque()) {
			str.append(", 'DCH'");
		}
		if (obj.getTipoOrigemInclusaoReposicao()) {
			str.append(", 'IRE'");
		}
		if (obj.getTipoOrigemInscricaoProcessoSeletivo()) {
			str.append(", 'IPS'");
		}
		if (obj.getTipoOrigemMatricula()) {
			str.append(", 'MAT'");
		}
		if (obj.getTipoOrigemMensalidade()) {
			str.append(", 'MEN'");
		}
		if (obj.getTipoOrigemNegociacao()) {
			str.append(", 'NCR'");
		}
		if (obj.getTipoOrigemOutros()) {
			str.append(", 'OUT'");
		}
		if (obj.getTipoOrigemRequerimento()) {
			str.append(", 'REQ'");
		}
		if (obj.getTipoOrigemMaterialDidatico()) {
			str.append(", 'MDI'");
		}
		if (str.toString().length() > 0) {
			sqlStr.append(" and contaReceber.tipoOrigem in (''");
			sqlStr.append(str);
			sqlStr.append(" ) ");
		}
	}

}
