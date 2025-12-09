package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ContasRecebimentoRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO;
import relatorio.negocio.interfaces.financeiro.RecebimentoPorUnidadeCursoTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class RecebimentoPorUnidadeCursoTurmaRel extends SuperRelatorio implements RecebimentoPorUnidadeCursoTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<TipoRequerimentoVO> listaTipoReq,  Boolean dataCompetencia, Date dataInicio, Date dataFim, TurmaVO turma, CursoVO curso, String situacao, String matricula, Map<String, ContaReceberVO> hashMapCodigoContaCorrente, String filtro, Integer contaCorrente, Boolean utilizarValorCompensado, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception {
		List<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> recebimentoPorUnidadeCursoTurmaRelVOs = new ArrayList<RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO>(0);
		if (situacao.equals("receber")) {
			situacao = "AR";
		} else if (situacao.equals("recebido")) {
			situacao = "RE";
		} else {
			situacao = "";
		}
		SqlRowSet tabelaResultado = executarConsultaParametrizada(listaUnidadeEnsino, listaTipoReq, dataCompetencia, dataInicio, dataFim, turma, curso, situacao, matricula, filtro, contaCorrente, utilizarValorCompensado, filtroRelatorioFinanceiroVO);
		Map<Integer, RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> hashMapCodigoConta = new HashMap<Integer, RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO>();
		if (utilizarValorCompensado) {
			while (tabelaResultado.next()) {
				recebimentoPorUnidadeCursoTurmaRelVOs.add(montarDados(tabelaResultado, dataInicio, dataFim, situacao, hashMapCodigoContaCorrente, hashMapCodigoConta, utilizarValorCompensado));
			}
		} else {
			while (tabelaResultado.next()) {
				montarDados(tabelaResultado, dataInicio, dataFim, situacao, hashMapCodigoContaCorrente, hashMapCodigoConta, utilizarValorCompensado);
			}
			for (Integer codigoConta : hashMapCodigoConta.keySet()) {
				recebimentoPorUnidadeCursoTurmaRelVOs.add(hashMapCodigoConta.get(codigoConta));
			}
		}
		for (RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO obj : recebimentoPorUnidadeCursoTurmaRelVOs) {
			if (hashMapCodigoContaCorrente.containsKey(obj.getNumero() + " - " + obj.getDigito())) {
				ContaReceberVO cc1 = (ContaReceberVO) hashMapCodigoContaCorrente.get(obj.getNumero() + " - " + obj.getDigito());
				cc1.setValorRecebido(cc1.getValorRecebido().doubleValue() + obj.getValorRecebido());
			} else {
				ContaReceberVO cc = new ContaReceberVO();
				cc.setCodOrigem(obj.getNumero() + " - " + obj.getDigito());
				cc.setValorRecebido(obj.getValorRecebido());
				hashMapCodigoContaCorrente.put(obj.getNumero() + " - " + obj.getDigito(), cc);
			}
		}
		Ordenacao.ordenarLista(recebimentoPorUnidadeCursoTurmaRelVOs, "ordenacao");
		return recebimentoPorUnidadeCursoTurmaRelVOs;
	}

	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<TipoRequerimentoVO> listaTipoReq, Boolean dataCompetencia, Date dataInicio, Date dataFim, TurmaVO turma, CursoVO curso, String situacao, String matricula, String filtro, Integer contaCorrente, Boolean utilizarValorCompensado, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select contareceber_valorrecebido, contareceber_codigo, contareceber_parcela, contareceber_tipoOrigem, contareceber_codOrigem, contareceber_situacao, ");
		sql.append(" nomecurso, nometurma, codigocurso, codigoturma, unidadeensino , matriculaaluno, valorpagamento, datavencimento, valorrecebido, datapagamento, cpf, endereco, numero, ");
		sql.append(" complemento, setor, cidade, estado, cep, banco, forma_pagamento, forma_tipo, contacorrente_numero, contacorrente_digito, cr2_numero, cr2_digito, cr2_codigo, convenio, bolsista, pessoa_nome, parceiro_nome, parceiro_cnpj, responsavelFinanceiro, cpfResponsavelFinanceiro, legendaFormaPagamento,cr2_nomeApresentacaoSistema,contacorrente_nomeApresentacaoSistema ");
		sql.append(" from ( ");
		sql.append(" select distinct contareceber.codigo AS contareceber_codigo, contareceber.parcela AS contareceber_parcela, contareceber.tipoOrigem AS contareceber_tipoOrigem, contareceber.codOrigem AS contareceber_codOrigem, ");
		sql.append(" contareceber.situacao AS contareceber_situacao, curso.nome as \"nomecurso\", turma.identificadorturma as \"nometurma\", curso.codigo as \"codigocurso\", turma.codigo as \"codigoturma\", contareceber.unidadeensino as \"unidadeensino\"  , ");
		sql.append(" contareceber.matriculaaluno, contareceber.valor as \"valorpagamento\", contareceber.datavencimento, crr.valorrecebimento as \"valorrecebido\", pessoa.nome as pessoa_nome, parceiro.nome as parceiro_nome, parceiro.cnpj as parceiro_cnpj, ");
		sql.append(" pessoa.cpf, pessoa.endereco, pessoa.numero, pessoa.complemento, pessoa.setor, cidade.nome as \"cidade\", estado.sigla as \"estado\", pessoa.cep, banco.nrbanco as \"banco\", fp.nome AS forma_pagamento, fp.tipo AS forma_tipo, ");
		sql.append(" contacorrente.numero as contacorrente_numero, contacorrente.digito as contacorrente_digito,contacorrente.nomeApresentacaoSistema as contacorrente_nomeApresentacaoSistema, ");
		sql.append(" cr2.nomeApresentacaoSistema as  cr2_nomeApresentacaoSistema,cr2.numero as cr2_numero, cr2.digito as cr2_digito, cr2.codigo as cr2_codigo, ");
		sql.append(" case when (contareceber.convenio is not null) then 'Sim' else 'Não' end AS convenio, ");
		// sql.append(" case when (contareceber.descontoinstituicao is not null) then 'Sim' else 'Não' end AS bolsista, ");
		sql.append(" case when (contareceber.valorrecebido = 0) then 'Sim' else 'Não' end AS bolsista, responsavelFinanceiro.nome as responsavelFinanceiro, responsavelFinanceiro.cpf as cpfResponsavelFinanceiro, ");
		if (utilizarValorCompensado) {
			sql.append(" (case when fp.tipo = 'CH' and cheque.pago = true then cheque.dataprevisao::Date ");
			sql.append(" when fp.tipo = 'CA' then contareceber.datavencimento::Date ");
			sql.append(" when fp.tipo != 'CH' and fp.tipo != 'CA' then negociacaorecebimento.data::Date end ) as dataPagamento, ");			
			sql.append(" (case when fp.tipo = 'CH' and cheque.pago = true then crr.valorrecebimento ");
			sql.append(" when fp.tipo = 'CA' then crr.valorRecebimento::numeric(20,2) ");
			sql.append(" when fp.tipo != 'CH' and fp.tipo != 'CA' then contareceber.valorrecebido end ) as contareceber_valorrecebido, ");
			sql.append(" (case when fp.tipo = 'CH' and cheque.pago = true then cheque.dataprevisao::Date ");
			sql.append(" when fp.tipo = 'CA' then formapagamentonegociacaorecebimentocartaocredito.dataRecebimento::Date ");
			sql.append(" when fp.tipo != 'CH' and fp.tipo != 'CA' then ");
			if (situacao.equals("")) {
				sql.append(" contareceber.datavencimento::Date ");
			} else if (situacao.equals("RE")) {
				sql.append(" negociacaorecebimento.data::Date ");
			} else {
				sql.append(" contareceber.datavencimento::Date ");
			}
			sql.append(" end ) as dataRecebimento, ");
		} else {
			sql.append(" negociacaorecebimento.data::Date as \"datapagamento\", ");
			sql.append(" contareceber.valorrecebido as contareceber_valorrecebido, ");
			if (situacao.equals("")) {
				sql.append(" contareceber.datavencimento::Date ");
			} else if (situacao.equals("RE")) {
				sql.append(" negociacaorecebimento.data::Date ");
			} else {
				sql.append(" contareceber.datavencimento::Date ");
			}
			sql.append(" as dataRecebimento, ");
		}
		sql.append(" (select array_to_string(array(select distinct (formapagamento.tipo ||' - '|| formapagamento.nome) from formapagamento), ', ')) as legendaFormaPagamento ");
		sql.append(" from contareceber ");
		sql.append(" left join contarecebernegociacaorecebimento on contareceber.codigo = contarecebernegociacaorecebimento.contareceber  ");
		sql.append(" left join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sql.append(" left join contareceberrecebimento crr on contareceber.codigo = crr.contareceber and  crr.formapagamentonegociacaorecebimento is not null ");
		sql.append(" left join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = crr.formapagamentonegociacaorecebimento ");
		sql.append(" left join formapagamento fp on fp.codigo = formapagamentonegociacaorecebimento.formapagamento ");
		sql.append(" left join cheque on cheque.codigo = formapagamentonegociacaorecebimento.cheque ");
		sql.append(" left join formapagamentonegociacaorecebimentocartaocredito on formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo ");
		sql.append(" left join operadoracartao on operadoracartao.codigo = formapagamentonegociacaorecebimento.operadoracartao ");
		sql.append(" left join pessoa on contareceber.pessoa = pessoa.codigo ");
		sql.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contareceber.responsavelFinanceiro ");
		sql.append(" left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sql.append(" left join cidade on pessoa.cidade = cidade.codigo ");
		sql.append(" left join estado on cidade.estado = estado.codigo ");
		sql.append(" left join contacorrente on contareceber.contacorrente = contacorrente.codigo ");
		sql.append(" left join agencia on contacorrente.agencia = agencia.codigo ");
		sql.append(" left join banco on agencia.banco = banco.codigo ");
		sql.append(" left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sql.append(" left join turma on ((contareceber.turma is not null and contareceber.turma = turma.codigo) or (contareceber.turma is null and turma.codigo= matriculaperiodo.turma)) ");
		sql.append(" left join curso on curso.codigo = turma.curso ");
		sql.append(" left join contacorrente cr2 on formapagamentonegociacaorecebimento.contacorrente = cr2.codigo ");
		sql.append(" where 1=1 ");
		if (!listaUnidadeEnsino.isEmpty()) {
			sql.append(" and ").append(realizarGeracaoWhereUnidadeEnsinoSelecionada(listaUnidadeEnsino, "contareceber.unidadeensino")); 			
		}
		sql.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
		if (filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento() && Uteis.isAtributoPreenchido(listaTipoReq)) {
			sql.append(" and case when contareceber.tipoOrigem = 'REQ' then contareceber.codOrigem::integer in (");	
			sql.append(" select requerimento.codigo from requerimento inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento where tiporequerimento.codigo in (0");
			for (TipoRequerimentoVO tipoReq : listaTipoReq) {
				sql.append(", " + tipoReq.getCodigo());
			}
			sql.append(" )) else 1=1 end ");	
		}
		if (situacao.equals("")) {			
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "contareceber.datavencimento", false));
		} else if (situacao.equals("RE")) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "negociacaorecebimento.data", false));			
		} else {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "contareceber.datavencimento", false));			
		}
		if (!matricula.equals("")) {
			sql.append(" and contareceber.matriculaaluno = '").append(matricula).append("' ");
		}
		if (curso.getCodigo() != 0) {
			sql.append(" and curso.codigo = ").append(curso.getCodigo());
		}
		if (turma.getCodigo() != 0) {
			sql.append(" and turma.codigo = ").append(turma.getCodigo());
		}
		sql.append(" ) as t ");
		sql.append(" where 1=1 ");
		if (situacao.equals("")) {
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "datarecebimento", false)); 			
		} else if (situacao.equals("RE")) {
			sql.append(" and contareceber_valorrecebido > 0 ");
			sql.append(" and contareceber_situacao = '").append(situacao).append("'");
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "datarecebimento", false)); 			
			if (filtro.equals("isentos")) {
				sql.append(" and (contareceber_valorrecebido = 0)");
			} else if (!filtro.equals("pagantes")) {
				sql.append(" and (contareceber_valorrecebido > 0)");
			}
			if (contaCorrente > 0) {
				sql.append(" and cr2_codigo = ").append(contaCorrente).append("");
			}
		} else {
			sql.append(" and contareceber_situacao = '").append(situacao).append("'");
			sql.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "datarecebimento", false));
			
		}
		sql.append(" and contareceber_situacao <> 'NE'");
		sql.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "t", "contareceber_tipoOrigem"));
		if (filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento() && Uteis.isAtributoPreenchido(listaTipoReq)) {
			sql.append(" and case when contareceber_tipoOrigem = 'REQ' then contareceber_codOrigem::integer in (");	
			sql.append(" select requerimento.codigo from requerimento inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento where tiporequerimento.codigo in (0");
			for (TipoRequerimentoVO tipoReq : listaTipoReq) {
				sql.append(", " + tipoReq.getCodigo());
			}
			sql.append(" )) else 1=1 end ");	
		}
		sql.append(" group by contareceber_valorrecebido, contareceber_codigo, contareceber_parcela, contareceber_tipoOrigem, contareceber_codOrigem, contareceber_situacao, nomecurso, nometurma, codigocurso, codigoturma, ");
		sql.append(" unidadeensino , matriculaaluno, valorpagamento, datavencimento, valorrecebido, datapagamento, cpf, endereco, numero, complemento, setor, cidade, estado, ");
		sql.append(" cep, banco, forma_pagamento, forma_tipo, contacorrente_numero, contacorrente_digito,cr2_nomeApresentacaoSistema, cr2_numero, cr2_digito, cr2_codigo, convenio, bolsista, pessoa_nome, parceiro_nome, parceiro_cnpj, responsavelFinanceiro, cpfResponsavelFinanceiro, legendaFormaPagamento,contacorrente_nomeApresentacaoSistema ");
		sql.append(" order by nomecurso, nometurma, pessoa_nome ");
//		System.out.println(sql.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return tabelaResultado;
	}

	private RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO montarDados(SqlRowSet dadosSQL, Date dataInicio, Date dataFim, String situacao, Map<String, ContaReceberVO> hashMapCodigoContaCorrente, Map<Integer, RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO> hashMapCodigoConta, Boolean utilizarValorCompensado) {
		RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO parcela = null;
		if (utilizarValorCompensado) {
			parcela = new RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO();
			parcela.setParcela(dadosSQL.getString("contareceber_parcela"));
			parcela.setOrigem(dadosSQL.getString("contareceber_tipoOrigem"));
			parcela.setMatricula(dadosSQL.getString("matriculaaluno"));
			parcela.setNomeCurso(dadosSQL.getString("nomecurso"));
			parcela.setNomeTurma(dadosSQL.getString("nometurma"));
			parcela.setNomeAluno(dadosSQL.getString("pessoa_nome"));
			parcela.setNomeParceiro(dadosSQL.getString("parceiro_nome"));
			parcela.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro"));
			parcela.setCpfResponsavelFinanceiro(dadosSQL.getString("cpfResponsavelFinanceiro"));
			parcela.setLegendaFormaPagamento(dadosSQL.getString("legendaFormaPagamento"));
			parcela.setCpf(dadosSQL.getString("cpf"));
			parcela.setCnpj(dadosSQL.getString("parceiro_cnpj"));
			String endereco = dadosSQL.getString("endereco") == null ? "" : dadosSQL.getString("endereco");
			String numero = dadosSQL.getString("numero") == null ? "" : ", " + dadosSQL.getString("numero");
			String complemento = dadosSQL.getString("complemento") == null ? "" : " - " + dadosSQL.getString("complemento");
			String setor = dadosSQL.getString("setor") == null ? "" : " - " + dadosSQL.getString("setor");
			String estado = dadosSQL.getString("estado") == null ? "" : " - " + dadosSQL.getString("estado");
			String cidade = dadosSQL.getString("cidade") == null ? "" : " / " + dadosSQL.getString("cidade");
			if (!endereco.trim().isEmpty()) {
				parcela.setEndereco(endereco + numero + complemento + setor + cidade + estado);
				parcela.setCep(dadosSQL.getString("cep"));
				parcela.setCidade(cidade);
				parcela.setEstado(estado);
			}
			parcela.setValorPagamento(dadosSQL.getDouble("valorpagamento"));
			parcela.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
			parcela.setDataPagamento(dadosSQL.getDate("datapagamento"));
			parcela.setDataVencimento(dadosSQL.getDate("datavencimento"));
			parcela.setBanco(dadosSQL.getString("banco"));
			parcela.setConvenio(dadosSQL.getString("convenio"));
			parcela.setBolsista(dadosSQL.getString("bolsista"));
			parcela.setNumero(dadosSQL.getString("cr2_numero"));
			parcela.setDigito(dadosSQL.getString("cr2_digito"));
		} else {
//			if (hashMapCodigoConta.containsKey(dadosSQL.getInt("codigo_contareceberrecebimento"))) {
//				parcela = (RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO) hashMapCodigoConta.get(dadosSQL.getInt("codigo_contareceberrecebimento"));
//			} else {
				parcela = new RecebimentoPorUnidadeCursoTurmaRel_ParcelasVO();
				parcela.setParcela(dadosSQL.getString("contareceber_parcela"));
				parcela.setOrigem(dadosSQL.getString("contareceber_tipoOrigem"));
				parcela.setMatricula(dadosSQL.getString("matriculaaluno"));
				parcela.setNomeCurso(dadosSQL.getString("nomecurso"));
				parcela.setNomeTurma(dadosSQL.getString("nometurma"));
				parcela.setNomeAluno(dadosSQL.getString("pessoa_nome"));
				parcela.setNomeParceiro(dadosSQL.getString("parceiro_nome"));
				parcela.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro"));
				parcela.setCpfResponsavelFinanceiro(dadosSQL.getString("cpfResponsavelFinanceiro"));
				parcela.setLegendaFormaPagamento(dadosSQL.getString("legendaFormaPagamento"));
				parcela.setCpf(dadosSQL.getString("cpf"));
				parcela.setCnpj(dadosSQL.getString("parceiro_cnpj"));
				String endereco = dadosSQL.getString("endereco") == null ? "" : dadosSQL.getString("endereco");
				String numero = dadosSQL.getString("numero") == null ? "" : ", " + dadosSQL.getString("numero");
				String complemento = dadosSQL.getString("complemento") == null ? "" : " - " + dadosSQL.getString("complemento");
				String setor = dadosSQL.getString("setor") == null ? "" : " - " + dadosSQL.getString("setor");
				String estado = dadosSQL.getString("estado") == null ? "" : " - " + dadosSQL.getString("estado");
				String cidade = dadosSQL.getString("cidade") == null ? "" : " / " + dadosSQL.getString("cidade");
				if (!endereco.trim().isEmpty()) {
					parcela.setEndereco(endereco + numero + complemento + setor + cidade + estado);
					parcela.setCep(dadosSQL.getString("cep"));
					parcela.setCidade(cidade);
					parcela.setEstado(estado);
				}
				parcela.setValorPagamento(dadosSQL.getDouble("valorpagamento"));
				parcela.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
				parcela.setDataPagamento(dadosSQL.getDate("datapagamento"));
				parcela.setDataVencimento(dadosSQL.getDate("datavencimento"));
				parcela.setBanco(dadosSQL.getString("banco"));
				parcela.setConvenio(dadosSQL.getString("convenio"));
				parcela.setBolsista(dadosSQL.getString("bolsista"));
				parcela.setNumero(dadosSQL.getString("cr2_numero"));
				parcela.setDigito(dadosSQL.getString("cr2_digito"));
				hashMapCodigoConta.put(dadosSQL.getInt("contareceber_codigo"), parcela);
			}
//		}
		if (dadosSQL.getString("contareceber_situacao").equals("RE")) {
			ContasRecebimentoRelVO contasRecebimentoRelVO = null;
			contasRecebimentoRelVO = new ContasRecebimentoRelVO();
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("contacorrente_nomeApresentacaoSistema"))) {
				contasRecebimentoRelVO.setContaCorrente(dadosSQL.getString("contacorrente_nomeApresentacaoSistema"));
			}else{
				contasRecebimentoRelVO.setContaCorrente(dadosSQL.getString("contacorrente_numero") + " - " + dadosSQL.getString("contacorrente_digito"));	
			}
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("cr2_nomeApresentacaoSistema"))) {
				contasRecebimentoRelVO.setContaCorrenteRecebimento(dadosSQL.getString("cr2_nomeApresentacaoSistema"));
			}else{
				contasRecebimentoRelVO.setContaCorrenteRecebimento(dadosSQL.getString("cr2_numero") + " - " + dadosSQL.getString("cr2_digito"));
			}
			contasRecebimentoRelVO.setFormaPagamento(dadosSQL.getString("forma_tipo"));
//			if (hashMapCodigoContaCorrente.containsKey(dadosSQL.getString("cr2_numero") + " - " + dadosSQL.getString("cr2_digito"))) {
//				ContaReceberVO cc1 = (ContaReceberVO) hashMapCodigoContaCorrente.get(dadosSQL.getString("cr2_numero") + " - " + dadosSQL.getString("cr2_digito"));
//				cc1.setValorRecebido(cc1.getValorRecebido().doubleValue() + dadosSQL.getDouble("contareceber_valorrecebido"));
//			} else {
//				ContaReceberVO cc = new ContaReceberVO();
//				cc.setCodOrigem(dadosSQL.getString("cr2_numero") + " - " + dadosSQL.getString("cr2_digito"));
//				cc.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
//				hashMapCodigoContaCorrente.put(dadosSQL.getString("cr2_numero") + " - " + dadosSQL.getString("cr2_digito"), cc);
//			}
			parcela.getContasRecebimentoRelVOs().add(contasRecebimentoRelVO);
		}
		return parcela;
	}

	public String caminhoBaseIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public String getDesignIReportRelatorio(String tipoLayout) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + tipoLayout + ".jrxml");
	}

	public String getDesignIReportRelatorioExcel(String tipoLayout) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + "Excel" + tipoLayout + ".jrxml");
	}

	public static String getIdEntidade() {
		return ("RecebimentoPorUnidadeCursoTurmaRel");
	}

	@Override
	public void validarDados(Integer codigoTurma, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws ConsistirException {
		if (listaUnidadeEnsino.isEmpty()) {
			throw new ConsistirException("A UNIDADE ENSINO deve ser informada para a geração do relatório.");
		}
	}

	public void validarDadosPeriodoRelatorioUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer turma, Date dataInicio, Date dataFim) throws Exception {
		if (dataFim.before(dataInicio)) {
			throw new Exception(UteisJSF.internacionalizar("msg_Recebimento_dataFimMenorDatainicio"));
		}
		if (Uteis.nrDiasEntreDatas(dataInicio, dataFim) > 30 && turma == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_Recebimento_intervaloDeDiasEntreDataFimDataInicio"));
		}
		Integer qtdeMeses = 0;
		if ((!listaUnidadeEnsino.isEmpty()) && turma == null || turma == 0) {
			qtdeMeses = Uteis.getDataQuantidadeMesesEntreData(dataInicio, dataFim);
		}
		if (qtdeMeses > 6) {
			throw new Exception(UteisJSF.internacionalizar("msg_Recebimento_possivelGerarRelatorioPeriodoSeisMeses"));
		}
	}

}
