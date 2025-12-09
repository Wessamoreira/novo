package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.financeiro.DeclaracaoImpostoRendaRelVO;
import relatorio.negocio.interfaces.financeiro.DeclaracaoImpostoRendaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class DeclaracaoImpostoRendaRel extends SuperRelatorio implements DeclaracaoImpostoRendaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public List<DeclaracaoImpostoRendaRelVO> criarObjeto(DeclaracaoImpostoRendaRelVO obj, UsuarioVO usuario, String tipoPessoa, DeclaracaoImpostoRendaRelVO filtroTipoOrigem, Boolean apresentarDataPrevisaoRecebimentoVencimentoConta) throws Exception {
		return montarDados(executarConsultaParametrizada(obj, tipoPessoa, filtroTipoOrigem, usuario, apresentarDataPrevisaoRecebimentoVencimentoConta), obj, usuario);
	}

	public SqlRowSet executarConsultaParametrizada(DeclaracaoImpostoRendaRelVO obj, String tipoPessoa, DeclaracaoImpostoRendaRelVO filtroTipoOrigem, UsuarioVO usuario, Boolean apresentarDataPrevisaoRecebimentoVencimentoConta) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		Boolean considerarDataRecebimentoContaDeclaracaoImpostoRenda = verificarPermissaoFuncionalidadeUsuario("ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda", usuario);
		sqlStr.append(" SELECT sum(contareceber_valorrecebido) AS contareceber_valorrecebido, unidadeensino_setor, cidade_nome, estado_sigla, unidadeensino_nome, unidadeensino_razaosocial, unidadeensino_endereco, ");
		sqlStr.append(" unidadeensino_cep, unidadeensino_telcomercial1, unidadeensino_fax, unidadeensino_site, unidadeensino_cnpj, unidadeensino_email, unidadeensino_inscestadual, contareceber_datavencimento, ");
		sqlStr.append(" contareceber_nrdocumento, contareceber_tipoorigem, contareceber_parcela, negociacaorecebimento_datarecebimento, matriculaperiodo_ano, matriculaperiodo_semestre, unidadeensino_codigo, unidadeensinofinanceira, contareceber_responsavelfinanceiro ");
		sqlStr.append(" FROM (");
		sqlStr.append(" SELECT distinct contareceber.unidadeensinofinanceira as unidadeensinofinanceira,  unidadeensino.codigo as unidadeensino_codigo, unidadeensino.setor AS unidadeensino_setor, cidade.nome AS cidade_nome, estado.sigla AS estado_sigla, unidadeensino.nome AS unidadeensino_nome, unidadeensino.razaosocial AS unidadeensino_razaosocial,");
		sqlStr.append(" unidadeensino.cep AS unidadeensino_cep, unidadeensino.telcomercial1 AS unidadeensino_telcomercial1, unidadeensino.fax AS unidadeensino_fax, unidadeensino.endereco as unidadeensino_endereco,");
		sqlStr.append(" unidadeensino.site AS unidadeensino_site, unidadeensino.cnpj AS unidadeensino_cnpj, unidadeensino.email AS unidadeensino_email, unidadeensino.inscestadual AS unidadeensino_inscestadual, ");
		sqlStr.append(" contareceber.nrdocumento AS contareceber_nrdocumento, contareceber.tipoorigem AS contareceber_tipoorigem, contareceber.parcela contareceber_parcela, contareceber.responsavelfinanceiro AS contareceber_responsavelfinanceiro,");
		if (apresentarDataPrevisaoRecebimentoVencimentoConta) {
			sqlStr.append(" case ");
			sqlStr.append(" when formapagamento.tipo = 'CH' and cheque.pago = true and (Extract (year from cheque.dataprevisao)) = ").append(obj.getAno()).append(" then cheque.dataprevisao ");
			sqlStr.append(" when formapagamento.tipo = 'CA' and (formapagamentonegociacaorecebimentocartaocredito.codigo is null or (formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE' and extract (year from (formapagamentonegociacaorecebimentocartaocredito.datavencimento)) = ").append(obj.getAno()).append(")) THEN formapagamentonegociacaorecebimentocartaocredito.datavencimento ");
			sqlStr.append(" when formapagamento.tipo != 'CH' and formapagamento.tipo != 'CA' then contareceber.datavencimento ");
			sqlStr.append(" end AS contareceber_datavencimento, ");			
		} else {
			sqlStr.append(" contareceber.datavencimento AS contareceber_datavencimento,");
		}

		//Data
		if (considerarDataRecebimentoContaDeclaracaoImpostoRenda || !apresentarDataPrevisaoRecebimentoVencimentoConta) {
			sqlStr.append(" negociacaorecebimento.data AS negociacaorecebimento_datarecebimento, ");
		} else {
			sqlStr.append(" CASE ");
			sqlStr.append(" when formapagamento.tipo = 'CH' then case when cheque.pago and (Extract (year from cheque.databaixa)) = ").append(obj.getAno()).append(" then cheque.databaixa else cheque.dataPrevisao end ");					
			sqlStr.append(" when formapagamento.tipo = 'CD' then (case when formapagamentonegociacaorecebimento.datacredito is null then negociacaorecebimento.data else formapagamentonegociacaorecebimento.datacredito end ) ");
			sqlStr.append(" when formapagamento.tipo = 'BO' then (case when formapagamentonegociacaorecebimento.datacredito is null then negociacaorecebimento.data else formapagamentonegociacaorecebimento.datacredito end )  ");
			sqlStr.append(" when formapagamento.tipo = 'CA' and formapagamentonegociacaorecebimentocartaocredito.codigo is not null AND EXTRACT (YEAR FROM (formapagamentonegociacaorecebimentocartaocredito.datarecebimento)) = ").append(obj.getAno());
			sqlStr.append("	then case when formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE' ");
			sqlStr.append(" and formapagamentonegociacaorecebimentocartaocredito.datarecebimento is not null ");
			sqlStr.append(" then formapagamentonegociacaorecebimentocartaocredito.datarecebimento else formapagamentonegociacaorecebimentocartaocredito.datavencimento end");
			sqlStr.append(" WHEN formapagamento.tipo != 'CH' AND formapagamento.tipo != 'CA' THEN negociacaorecebimento.data END AS negociacaorecebimento_datarecebimento, ");
		}
		
		sqlStr.append(" matriculaperiodo.ano AS matriculaperiodo_ano, matriculaperiodo.semestre AS matriculaperiodo_semestre, ");
		
		// Valor Recebido
		if (considerarDataRecebimentoContaDeclaracaoImpostoRenda || !apresentarDataPrevisaoRecebimentoVencimentoConta) {
			sqlStr.append(" contareceberrecebimento.valorrecebimento AS contareceber_valorrecebido,  ");			
		} else {
			sqlStr.append(" CASE ");
			sqlStr.append(" WHEN formapagamento.tipo = 'CH' AND cheque.pago = TRUE AND (EXTRACT (YEAR FROM cheque.databaixa)) = ").append(obj.getAno()).append(" THEN contareceberrecebimento.valorrecebimento ");
			sqlStr.append(" WHEN formapagamento.tipo = 'CA' AND formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE' ");
			sqlStr.append(" AND EXTRACT (YEAR FROM (formapagamentonegociacaorecebimentocartaocredito.datarecebimento)) = ").append(obj.getAno()).append(" AND formapagamentonegociacaorecebimentocartaocredito.datarecebimento IS NOT NULL ");
			sqlStr.append(" THEN contareceberrecebimento.valorrecebimento::numeric(20, 2) ");
//			sqlStr.append(" WHEN formapagamento.tipo = 'CA' AND formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE' ");
//			sqlStr.append(" AND EXTRACT (YEAR FROM (formapagamentonegociacaorecebimentocartaocredito.datarecebimento)) = ").append(obj.getAno()).append(" AND formapagamentonegociacaorecebimentocartaocredito.datarecebimento IS NOT NULL ");
//			sqlStr.append(" AND formapagamentonegociacaorecebimentocartaocredito.valorparcela > contareceber.valorrecebido THEN contareceber.valorrecebido ");
			sqlStr.append(" WHEN formapagamento.tipo != 'CH' AND formapagamento.tipo != 'CA' THEN contareceberrecebimento.valorrecebimento END AS contareceber_valorrecebido,  ");
		}
		
		//formapagamentonegociacaorecebimentocartaocredito
		if (considerarDataRecebimentoContaDeclaracaoImpostoRenda || !apresentarDataPrevisaoRecebimentoVencimentoConta) {
			sqlStr.append(" case when formapagamento.tipo = 'CA' AND EXTRACT (YEAR FROM (negociacaorecebimento.data)) = ").append(obj.getAno()).append(" AND negociacaorecebimento.data IS NOT NULL then formapagamentonegociacaorecebimentocartaocredito.codigo ELSE NULL END  ");
		} else {
			sqlStr.append(" CASE ");
			sqlStr.append(" WHEN formapagamento.tipo = 'CA' AND formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE' ");
			sqlStr.append(" AND EXTRACT (YEAR FROM (formapagamentonegociacaorecebimentocartaocredito.datarecebimento)) = ").append(obj.getAno()).append(" AND formapagamentonegociacaorecebimentocartaocredito.datarecebimento IS NOT NULL ");
			sqlStr.append(" AND formapagamentonegociacaorecebimentocartaocredito.valorparcela <= contareceber.valorrecebido THEN formapagamentonegociacaorecebimentocartaocredito.codigo ELSE NULL END  ");
		}
		sqlStr.append("  AS formapagamentonegociacaorecebimentocartaocredito,  ");
		
		//Cheque		
		if (considerarDataRecebimentoContaDeclaracaoImpostoRenda || !apresentarDataPrevisaoRecebimentoVencimentoConta) {
			sqlStr.append("case WHEN formapagamento.tipo = 'CH' and (EXTRACT (YEAR FROM negociacaorecebimento.data)) = ").append(obj.getAno()).append(" THEN cheque.codigo ELSE NULL END AS cheque, ");
		} else {
			sqlStr.append("case WHEN formapagamento.tipo = 'CH' AND cheque.pago = TRUE AND (EXTRACT (YEAR FROM cheque.databaixa)) = ").append(obj.getAno()).append(" THEN cheque.codigo ELSE NULL END AS cheque, ");
		}
		
		//contareceberrecebimento
		sqlStr.append(" CASE ");
		sqlStr.append(" WHEN formapagamento.tipo != 'CH' AND formapagamento.tipo != 'CA' THEN contareceberrecebimento.codigo ELSE NULL END AS contareceberrecebimento ");

		sqlStr.append(" from contareceber ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = contareceber.unidadeensinofinanceira ");
		sqlStr.append(" left join matricula ON contareceber.matriculaaluno = matricula.matricula ");
		sqlStr.append(" left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sqlStr.append(" inner join contarecebernegociacaorecebimento on contareceber.codigo = contarecebernegociacaorecebimento.contareceber  ");
		sqlStr.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo  = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sqlStr.append("inner join unidadeensino unidadeensinonegociacaorecebimento on unidadeensinonegociacaorecebimento.codigo = negociacaorecebimento.unidadeensino ");
		sqlStr.append(" inner join contareceberrecebimento on contareceberrecebimento.contareceber = contareceber.codigo and contareceberrecebimento.valorrecebimento  > 0 "); 
		sqlStr.append("	and contareceberrecebimento.negociacaorecebimento = negociacaorecebimento.codigo and contareceberrecebimento.tiporecebimento = 'CR'" );
		sqlStr.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = contareceberrecebimento.formapagamentonegociacaorecebimento ");
		sqlStr.append(" left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sqlStr.append(" left join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento ");
		sqlStr.append(" left join cheque on formapagamento.tipo = 'CH' and cheque.codigo = formapagamentonegociacaorecebimento.cheque ");
		sqlStr.append(" left join operadoracartao on formapagamento.tipo = 'CA' and operadoracartao.codigo = formapagamentonegociacaorecebimento.operadoracartao ");
		sqlStr.append(" left join formapagamentonegociacaorecebimentocartaocredito on formapagamento.tipo = 'CA' and formapagamentonegociacaorecebimentocartaocredito.valorParcela > 0   AND ((formapagamentonegociacaorecebimentocartaocredito.codigo = formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito) ");
		sqlStr.append(" OR (formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is null and formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo)) ");
		sqlStr.append("left join configuracoes on configuracoes.codigo = unidadeensinonegociacaorecebimento.configuracoes ");
		sqlStr.append("left join configuracaofinanceiro on configuracoes.codigo = configuracaofinanceiro.configuracoes ");
		sqlStr.append(" left join configuracaofinanceirocartao on configuracaofinanceirocartao.configuracaofinanceiro = configuracaofinanceiro.codigo and configuracaofinanceirocartao.operadoracartao = operadoracartao.codigo");
		sqlStr.append(" left join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append(" left join estado on estado.codigo = cidade.estado ");
		sqlStr.append(" where 1=1 ");
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			if (usuario.getIsApresentarVisaoAdministrativa() && tipoPessoa.equals("RF")) {
				sqlStr.append("AND contareceber.responsavelfinanceiro = ").append(obj.getPessoa().getCodigo());
			} else if (usuario.getIsApresentarVisaoPais()) {
				sqlStr.append("AND contareceber.responsavelfinanceiro = ").append(usuario.getPessoa().getCodigo());
			} else if (tipoPessoa.equals("PA")) {
				sqlStr.append("AND contareceber.parceiro = ").append(obj.getParceiro().getCodigo());	
			}
		} 
		if (Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			sqlStr.append("AND matricula.matricula = '").append(obj.getMatricula().getMatricula()).append("' ");
			if (tipoPessoa.equals("AL") && Uteis.isAtributoPreenchido(obj.getCodigoResponsavel())) {
				sqlStr.append("AND contareceber.responsavelfinanceiro = ").append(obj.getCodigoResponsavel()).append(" ");
			} else if (tipoPessoa.equals("AL") && Uteis.isAtributoPreenchido(obj.getParceiro().getCodigo())){
				sqlStr.append("AND contareceber.parceiro = ").append(obj.getParceiro().getCodigo()).append(" ");
			} else if (tipoPessoa.equals("AL")) {
				sqlStr.append("AND contareceber.responsavelfinanceiro is null ");
			}
		}
		if (!Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula()) && Uteis.isAtributoPreenchido(tipoPessoa) && tipoPessoa.equals("RF")) {
			sqlStr.append("AND matricula.matricula is null ");
		}
		sqlStr.append("  AND contareceber.situacao = 'RE'");
		if (tipoPessoa.equals("PA")) {
			sqlStr.append("AND contaReceber.tipoOrigem in ('BCC', 'OUT', 'NCR')");
		} else {
			adicionarFiltroTipoOrigem(filtroTipoOrigem, sqlStr);
		}
		sqlStr.append(" ) AS t where contareceber_valorrecebido is not null and contareceber_valorrecebido > 0 ");
		/*if (usuario.getIsApresentarVisaoAdministrativa()) {*/
			if (!obj.getUtilizarDataVencimentoParaDataRecebimento()) {
				sqlStr.append(" and EXTRACT (YEAR FROM (negociacaorecebimento_datarecebimento)) =  ").append(obj.getAno());
			} else {
				sqlStr.append("AND (Extract (year from contareceber_datavencimento)) = ").append(obj.getAno());
			}
		/*} else {
			sqlStr.append(" and EXTRACT (YEAR FROM (negociacaorecebimento_datarecebimento)) =  ").append(obj.getAno());
		}*/
		sqlStr.append(" group by unidadeensino_setor, unidadeensino_codigo, unidadeensinofinanceira, cidade_nome, estado_sigla, unidadeensino_nome, unidadeensino_razaosocial, ");
		sqlStr.append(" unidadeensino_cep, unidadeensino_telcomercial1, unidadeensino_fax, unidadeensino_site, unidadeensino_cnpj, unidadeensino_email, unidadeensino_inscestadual, contareceber_datavencimento, ");
		sqlStr.append(" contareceber_nrdocumento, contareceber_tipoorigem, contareceber_parcela, negociacaorecebimento_datarecebimento, matriculaperiodo_ano, matriculaperiodo_semestre, contareceber_responsavelfinanceiro, unidadeensino_endereco ");
		if (!obj.getUtilizarDataVencimentoParaDataRecebimento()) {
			sqlStr.append(" order by unidadeensinofinanceira, case when contareceber_tipoorigem in ('MEN', 'MAT', 'NEG')  then 0 else case when contareceber_tipoorigem not in ('MEN', 'MAT', 'NEG', 'MDI') then 1 else 2 end end, negociacaorecebimento_datarecebimento");
		} else {
			sqlStr.append(" order by unidadeensinofinanceira, case when contareceber_tipoorigem in ('MEN', 'MAT', 'NEG')  then 0 else case when contareceber_tipoorigem not in ('MEN', 'MAT', 'NEG', 'MDI') then 1 else 2 end end, contareceber_datavencimento");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());	
	}
	
	private List<DeclaracaoImpostoRendaRelVO> montarDados(SqlRowSet dadosSQL, DeclaracaoImpostoRendaRelVO obj,UsuarioVO usuario) throws Exception {
		List<DeclaracaoImpostoRendaRelVO> listaObjs = new ArrayList<DeclaracaoImpostoRendaRelVO>(0);
		Boolean responsavelPreenchido = false;
		while (dadosSQL.next()) {
			DeclaracaoImpostoRendaRelVO objRelatorio = new DeclaracaoImpostoRendaRelVO();
			objRelatorio.setMatricula(obj.getMatricula());
			objRelatorio.getContaReceber().setConfiguracaoFinanceiro(getAplicacaoControle().getConfiguracaoFinanceiroVO(dadosSQL.getInt("unidadeensinofinanceira")));
			objRelatorio.getContaReceber().setNrDocumento(dadosSQL.getString("contareceber_nrdocumento"));
			objRelatorio.getContaReceber().setDataVencimento(dadosSQL.getDate("contareceber_datavencimento"));
			objRelatorio.getContaReceber().setTipoOrigem(dadosSQL.getString("contareceber_tipoorigem"));
			objRelatorio.getContaReceber().setParcela(dadosSQL.getString("contareceber_parcela"));
			objRelatorio.getContaReceber().setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
			objRelatorio.getContaReceber().getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("contareceber_responsavelfinanceiro"));
			if (obj.getUtilizarDataVencimentoParaDataRecebimento()) {
				objRelatorio.getContaReceber().setDataRecebimento(dadosSQL.getDate("contareceber_datavencimento"));
			} else {
				objRelatorio.getContaReceber().setDataRecebimento(dadosSQL.getDate("negociacaorecebimento_datarecebimento"));
			}
			String periodo = "";
			if ((dadosSQL.getString("matriculaperiodo_ano") != null) && (!dadosSQL.getString("matriculaperiodo_ano").equals("")) && (dadosSQL.getString("matriculaperiodo_semestre") != null) && (!dadosSQL.getString("matriculaperiodo_semestre").equals(""))) {
				periodo = dadosSQL.getString("matriculaperiodo_ano") + "-" + dadosSQL.getString("matriculaperiodo_semestre") + "";
			} else {
				periodo = dadosSQL.getString("matriculaperiodo_ano");
			}
			objRelatorio.getContaReceber().setPeriodoConta(periodo);
			objRelatorio.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino_codigo"));
			objRelatorio.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino_nome"));
			objRelatorio.getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("unidadeensino_razaosocial"));
			objRelatorio.getUnidadeEnsino().setSetor(dadosSQL.getString("unidadeensino_setor"));
			objRelatorio.getUnidadeEnsino().setEndereco(dadosSQL.getString("unidadeensino_endereco"));
			objRelatorio.getUnidadeEnsino().setFax(dadosSQL.getString("unidadeensino_fax"));
			objRelatorio.getUnidadeEnsino().setTelComercial1(dadosSQL.getString("unidadeensino_telComercial1"));
			objRelatorio.getUnidadeEnsino().getCidade().setNome(dadosSQL.getString("cidade_nome"));
			objRelatorio.getUnidadeEnsino().getCidade().getEstado().setSigla(dadosSQL.getString("estado_sigla"));
			objRelatorio.getUnidadeEnsino().setSite(dadosSQL.getString("unidadeensino_site"));
			objRelatorio.getUnidadeEnsino().setCNPJ(dadosSQL.getString("unidadeensino_cnpj"));
			objRelatorio.getUnidadeEnsino().setInscEstadual(dadosSQL.getString("unidadeensino_inscestadual"));
			objRelatorio.getUnidadeEnsino().setCEP(dadosSQL.getString("unidadeensino_cep"));
			objRelatorio.getUnidadeEnsino().setEmail(dadosSQL.getString("unidadeensino_email"));
			objRelatorio.setAno(obj.getAno());
			if (Uteis.isAtributoPreenchido(obj.getParceiro().getCodigo())) {
				objRelatorio.setParceiro(obj.getParceiro());
			}
			if (Uteis.isAtributoPreenchido(obj.getPessoa().getCodigo())) {
				objRelatorio.setPessoa(obj.getPessoa());
			}
			if (Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getResponsavelFinanceiro().getCodigo())) {
				getFacadeFactory().getPessoaFacade().carregarDados(objRelatorio.getContaReceber().getResponsavelFinanceiro(), NivelMontarDados.BASICO, usuario);
			}
			preencherServicoPorConfiguracaoFinanceira(objRelatorio);
			preencherParcelaPorConfiguracaoFinanceira(objRelatorio);
			listaObjs.add(objRelatorio);
		}
		List<FiliacaoVO> filiacoes = getFacadeFactory().getFiliacaoFacade().consultarPorCodigoPessoaTipo(obj.getMatricula().getAluno().getCodigo(), "", false, null);
		for (FiliacaoVO filiacao : filiacoes) {
			if (filiacao.getResponsavelFinanceiro()) {
				for (DeclaracaoImpostoRendaRelVO objs : listaObjs) {
					objs.setNomeResponsavel(filiacao.getNome());
					objs.setRgResponsavel(filiacao.getRG());
					objs.setCpfResponsavel(filiacao.getCPF());
					responsavelPreenchido = true;
				}
			}
		}
		if (!responsavelPreenchido) {
			for (DeclaracaoImpostoRendaRelVO objs : listaObjs) {
				objs.setNomeResponsavel(obj.getMatricula().getAluno().getNome());
				objs.setRgResponsavel(obj.getMatricula().getAluno().getRG());
				objs.setCpfResponsavel(obj.getMatricula().getAluno().getCPF());
			}
		}
		return listaObjs;
	}

	private void preencherParcelaPorConfiguracaoFinanceira(DeclaracaoImpostoRendaRelVO objRelatorio) {
		if(objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMatricula()
				&& Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getSiglaParcelaMatriculaApresentarAluno())) {
			objRelatorio.setParcela(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getSiglaParcelaMatriculaApresentarAluno());			
		}else if(objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMaterialDidatico()
				&& Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getSiglaParcelaMaterialDidaticoApresentarAluno())) {
			objRelatorio.setParcela(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getSiglaParcelaMaterialDidaticoApresentarAluno());
		}else if(objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMatricula() && !Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getSiglaParcelaMatriculaApresentarAluno())) {
			objRelatorio.setParcela("MAT");
		}else {
			objRelatorio.setParcela(objRelatorio.getContaReceber().getParcela());	
		}
	}

	private void preencherServicoPorConfiguracaoFinanceira(DeclaracaoImpostoRendaRelVO objRelatorio) {
			if(objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMatricula()
					&& Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getNomeParcelaMatriculaApresentarAluno())) {
				objRelatorio.setServico(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getNomeParcelaMatriculaApresentarAluno() + objRelatorio.getContaReceber().getPeriodoConta());			
			}else if(objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMaterialDidatico()
					&& Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getNomeParcelaMaterialDidaticoApresentarAluno())) {
				objRelatorio.setServico(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getNomeParcelaMaterialDidaticoApresentarAluno() + objRelatorio.getContaReceber().getPeriodoConta());
			}else if((objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMaterialDidatico() && !Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getNomeParcelaMaterialDidaticoApresentarAluno()))
					|| (objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMatricula() && !Uteis.isAtributoPreenchido(objRelatorio.getContaReceber().getConfiguracaoFinanceiro().getNomeParcelaMatriculaApresentarAluno()))) {
				objRelatorio.setServico(objRelatorio.getContaReceber().getTipoOrigem_apresentar() + objRelatorio.getContaReceber().getPeriodoConta());
			}else if(objRelatorio.getContaReceber().getTipoOrigemContaReceber().isMensalidade()) {
				objRelatorio.setServico("Mensalidade/Parcela "+ objRelatorio.getContaReceber().getPeriodoConta());	
			}else {
				objRelatorio.setServico("Outros");
			}
	}

	public void adicionarFiltroTipoOrigem(DeclaracaoImpostoRendaRelVO obj, StringBuilder sqlStr) {
		//conta receber de material didatico nunca poderam sair no relatorio - SEI-RE407.25
		if(!obj.getTipoOrigemMaterialDidatico()) {
			sqlStr.append(" AND contaReceber.tipoOrigem != 'MDI' ");
		}
		
		sqlStr.append(" AND (contaReceber.tipoOrigem in (''");
		if (obj.getTipoOrigemBiblioteca()) {
			sqlStr.append(", 'BIB'");
		}
		if (obj.getTipoOrigemContratoReceita()) {
			sqlStr.append(", 'CTR'");
		}
		if (obj.getTipoOrigemDevolucaoCheque()) {
			sqlStr.append(", 'DCH'");
		}
		if (obj.getTipoOrigemInclusaoReposicao()) {
			sqlStr.append(", 'IRE'");
		}
		if (obj.getTipoOrigemInscricaoProcessoSeletivo()) {
			sqlStr.append(", 'IPS'");
		}
		if (obj.getTipoOrigemMatricula()) {
			sqlStr.append(", 'MAT'");
		}
		if (obj.getTipoOrigemMensalidade()) {
			sqlStr.append(", 'MEN'");
		}
		if(obj.getTipoOrigemMaterialDidatico()) {
			sqlStr.append(", 'MDI' ");
		}
		if (obj.getTipoOrigemOutros()) {
			sqlStr.append(", 'OUT'");
		}
		if (obj.getTipoOrigemRequerimento()) {
			sqlStr.append(", 'REQ'");
		}
		sqlStr.append(" ) ");
		
		if (obj.getTipoOrigemNegociacao()) {
			sqlStr.append(" or ( contaReceber.tipoOrigem = 'NCR' and parceiro.codigo is null and contaReceber.codorigem != '' ");
			if(!obj.getTipoOrigemMaterialDidatico()) {
				sqlStr.append(" and fn_verificarcontemparcelanegociadatipoorigem(contaReceber.codorigem::int, 'MDI') = false");
			}
			sqlStr.append(" )");
			sqlStr.append(" or ( contaReceber.tipoOrigem = 'NCR' and contaReceber.codorigem = '') ");
			if(obj.getTipoOrigemMaterialDidatico()) {
				sqlStr.append(" or ( contaReceber.tipoOrigem = 'NCR'  and contaReceber.codorigem != '' and parceiro.codigo is not null and parceiro.considerarValorDescontoDeclaracaoImpostoRendaAluno ) ");
			}else {
				sqlStr.append(" or ( contaReceber.tipoOrigem = 'NCR'  and contaReceber.codorigem != '' and parceiro.codigo is not null and parceiro.considerarValorDescontoDeclaracaoImpostoRendaAluno and fn_verificarcontemparcelanegociadatipoorigem(contaReceber.codorigem::int, 'MDI') = false ) ");
			}
		
		}		
		if (obj.getTipoOrigemBolsaCusteadaConvenio()) {
			sqlStr.append(" or (  ");
			sqlStr.append(" contaReceber.tipoOrigem = 'BCC' and parceiro.considerarValorDescontoDeclaracaoImpostoRendaAluno = true ");
			sqlStr.append(" ) ");
		}
		
		sqlStr.append(" ) ");
	}

	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return "DeclaracaoImpostoRendaRel";
	}
	
	@Override
	public void consultarPermissoesImpressaoDeclaracaoVisaoAluno(DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO, UsuarioVO usuarioVO) throws Exception{
		declaracaoImpostoRendaRelVO.setTipoOrigemBiblioteca(verificarPermissaoFuncionalidadeUsuario("ApresentarContaBiblioteca", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemBolsaCusteadaConvenio(verificarPermissaoFuncionalidadeUsuario("ApresentarContaBolsaCusteadaConvenio", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemContratoReceita(verificarPermissaoFuncionalidadeUsuario("ApresentarContaContratoReceita", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemDevolucaoCheque(verificarPermissaoFuncionalidadeUsuario("ApresentarContaDevolucaoCheque", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemInclusaoReposicao(verificarPermissaoFuncionalidadeUsuario("ApresentarContaInclusaoReposicao", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemInscricaoProcessoSeletivo(verificarPermissaoFuncionalidadeUsuario("ApresentarContasInscricaoProcessoSeletivo", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemMatricula(verificarPermissaoFuncionalidadeUsuario("ApresentarContaMatricula", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemMensalidade(verificarPermissaoFuncionalidadeUsuario("ApresentarContaMensalidade", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemNegociacao(verificarPermissaoFuncionalidadeUsuario("ApresentarContaNegociacao", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemOutros(verificarPermissaoFuncionalidadeUsuario("ApresentarContaOutros", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemRequerimento(verificarPermissaoFuncionalidadeUsuario("ApresentarContaRequerimento", usuarioVO));
		declaracaoImpostoRendaRelVO.setUtilizarDataVencimentoParaDataRecebimento(verificarPermissaoFuncionalidadeUsuario("UtilizarDataVencimentoDataRecebimento", usuarioVO));
		declaracaoImpostoRendaRelVO.setTipoOrigemMaterialDidatico(verificarPermissaoFuncionalidadeUsuario("ApresentarContaMaterialDidatico", usuarioVO));
	}
	
	public List<DeclaracaoImpostoRendaRelVO> executarConsultaParametrizada(DeclaracaoImpostoRendaRelVO obj, UsuarioVO usuarioVO, String tipoPessoa, String ano) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		
		Boolean considerarDataRecebimentoContaDeclaracaoImpostoRenda = verificarPermissaoFuncionalidadeUsuario("ConsiderarDataRecebimentoContaDeclaracaoImpostoRenda", usuarioVO);
		
		if (!verificarPermissaoFuncionalidadeUsuario("PermitirGerarDeclaracaoAnoAtual", usuarioVO) && ano.equals(Uteis.getAnoDataAtual4Digitos()) && !usuarioVO.getIsApresentarVisaoAdministrativa()) {
			throw new Exception("O Usuário Não Possui Permissão Para Gerar Declarações do Ano Atual");
		}
		if (ano.length() < 4) {
			throw new Exception("O Ano Informado Deve Possuir 4 Dígitos.");
		}
		sqlStr.append(" SELECT distinct ano, matriculaaluno, codigoResponsavelFinanceiro, codigoparceiro, parceiro, nome ");
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" , responsavelFinanceiro ");	
		}
		sqlStr.append(" from ( ");
		sqlStr.append(" SELECT distinct ");
		sqlStr.append(" CAST (extract(year from ");
		if (usuarioVO.getIsApresentarVisaoAdministrativa() && obj.getUtilizarDataVencimentoParaDataRecebimento()) {
			sqlStr.append(" contareceber.datavencimento ");
		} else {
			sqlStr.append(" case when formapagamento.tipo = 'CH' and cheque.pago = true ");
			if(!usuarioVO.getIsApresentarVisaoAdministrativa() && !verificarPermissaoFuncionalidadeUsuario("PermitirGerarDeclaracaoAnoAtual", usuarioVO)) {
				if (considerarDataRecebimentoContaDeclaracaoImpostoRenda) {
					sqlStr.append("AND (Extract (YEAR FROM negociacaorecebimento.data))  < extract(year from current_date)");		
					sqlStr.append(" then negociacaorecebimento.data ");
				} else {
					sqlStr.append("AND (Extract (YEAR FROM cheque.databaixa))  < extract(year from current_date)");
					sqlStr.append(" then cheque.databaixa ");
				}
			}else {
				if (considerarDataRecebimentoContaDeclaracaoImpostoRenda) {
					sqlStr.append("AND (Extract (YEAR FROM negociacaorecebimento.data)) <= extract(year from current_date)");
					sqlStr.append(" then negociacaorecebimento.data ");
				} else {
					sqlStr.append("AND (Extract (YEAR FROM cheque.databaixa)) <= extract(year from current_date)");
					sqlStr.append(" then cheque.databaixa ");
				}
			}		
			
			sqlStr.append(" when formapagamento.tipo = 'CA' AND (formapagamentonegociacaorecebimentocartaocredito.codigo IS NULL or (1=1 ");
			if(!usuarioVO.getIsApresentarVisaoAdministrativa() && !verificarPermissaoFuncionalidadeUsuario("PermitirGerarDeclaracaoAnoAtual", usuarioVO)) {			
				if (considerarDataRecebimentoContaDeclaracaoImpostoRenda) {
					sqlStr.append(" AND EXTRACT (YEAR FROM (negociacaorecebimento.data)) < extract(year from current_date)");		
				} else {
					sqlStr.append(" AND formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE'");
					sqlStr.append(" AND EXTRACT (YEAR FROM (formapagamentonegociacaorecebimentocartaocredito.datarecebimento)) < extract(year from current_date)");
				}
			}else {
				if (considerarDataRecebimentoContaDeclaracaoImpostoRenda) {
					sqlStr.append(" AND EXTRACT (YEAR FROM (negociacaorecebimento.data)) <= extract(year from current_date)");
				} else {
					sqlStr.append(" AND formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE'");
					sqlStr.append(" AND EXTRACT (YEAR FROM (formapagamentonegociacaorecebimentocartaocredito.datarecebimento)) <= extract(year from current_date)");
				}
			}
			if (considerarDataRecebimentoContaDeclaracaoImpostoRenda) {
				sqlStr.append(")) THEN negociacaorecebimento.data ");
			} else {
				sqlStr.append(")) THEN formapagamentonegociacaorecebimentocartaocredito.datarecebimento ");
			}
			
			if (considerarDataRecebimentoContaDeclaracaoImpostoRenda) {
				sqlStr.append(" when formapagamento.tipo = 'CD' then negociacaorecebimento.data ");
				sqlStr.append(" when formapagamento.tipo = 'BO' then negociacaorecebimento.data ");			
			} else {
				sqlStr.append(" when formapagamento.tipo = 'CD' then (case when formapagamentonegociacaorecebimento.datacredito is null then negociacaorecebimento.data else formapagamentonegociacaorecebimento.datacredito end ) ");
				sqlStr.append(" when formapagamento.tipo = 'BO' then (case when formapagamentonegociacaorecebimento.datacredito is null then negociacaorecebimento.data else formapagamentonegociacaorecebimento.datacredito end )  ");
			}
			
			sqlStr.append(" when formapagamento.tipo != 'CH' and formapagamento.tipo != 'CA' then negociacaorecebimento.data end ");
		}
		sqlStr.append(" ) AS VARCHAR) AS ano, contareceber.matriculaaluno, pessoa.nome, contareceber.responsavelfinanceiro as codigoResponsavelFinanceiro, parceiro.codigo as codigoparceiro, parceiro.nome as parceiro, contareceber.datavencimento as contareceber_datavencimento, unidadefinanceira.codigo as codigoUnidadeFinanceira, unidadefinanceira.nome as nomeUnidadeFinanceira");
		if (tipoPessoa.equals("AL")) {
			sqlStr.append(" ,pessoa2.nome as responsavelfinanceiro");
		}
		sqlStr.append(" from contareceber ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = contareceber.unidadeensino ");
		sqlStr.append(" left join matricula ON contareceber.matriculaaluno = matricula.matricula ");
		sqlStr.append(" left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sqlStr.append(" inner join contarecebernegociacaorecebimento on contareceber.codigo = contarecebernegociacaorecebimento.contareceber  ");
		sqlStr.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo  = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sqlStr.append(" inner join unidadeensino unidadeensinonegociacaorecebimento on unidadeensinonegociacaorecebimento.codigo = negociacaorecebimento.unidadeensino ");
		
		sqlStr.append(" inner join unidadeensino as unidadefinanceira on unidadefinanceira.codigo = contareceber.unidadeensinofinanceira ");
		
		sqlStr.append(" inner join contareceberrecebimento on contareceberrecebimento.contareceber = contareceber.codigo ");
		sqlStr.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = contareceberrecebimento.formapagamentonegociacaorecebimento ");
		sqlStr.append(" left join parceiro on parceiro.codigo = contareceber.parceiro ");
		sqlStr.append(" left join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento ");
		sqlStr.append(" left join cheque on formapagamento.tipo = 'CH' and cheque.codigo = formapagamentonegociacaorecebimento.cheque ");
		sqlStr.append(" left join operadoracartao on formapagamento.tipo = 'CA' and operadoracartao.codigo = formapagamentonegociacaorecebimento.operadoracartao ");
		sqlStr.append(" left join formapagamentonegociacaorecebimentocartaocredito on formapagamento.tipo = 'CA'    AND ((formapagamentonegociacaorecebimentocartaocredito.codigo = formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito) ");
		sqlStr.append(" OR (formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is null and formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo)) ");
		sqlStr.append(" left join configuracoes on configuracoes.codigo = unidadeensinonegociacaorecebimento.configuracoes ");
		sqlStr.append(" left join configuracaofinanceiro on configuracoes.codigo = configuracaofinanceiro.configuracoes ");
		sqlStr.append(" left join configuracaofinanceirocartao on configuracaofinanceirocartao.configuracaofinanceiro = configuracaofinanceiro.codigo and configuracaofinanceirocartao.operadoracartao = operadoracartao.codigo");
		sqlStr.append(" left join cidade on cidade.codigo = unidadeensino.cidade ");
		sqlStr.append(" left join estado on estado.codigo = cidade.estado ");
		if (tipoPessoa.equals("RF")) {
			sqlStr.append("	inner join pessoa on pessoa.codigo = contareceber.responsavelFinanceiro");	
		} else if (tipoPessoa.equals("AL")) {
			sqlStr.append("	left join pessoa on pessoa.codigo = contareceber.pessoa");
			sqlStr.append("	left JOIN pessoa as pessoa2 ON pessoa2.codigo = contareceber.responsavelfinanceiro");
		}
		sqlStr.append(" where 1=1 ");
		if (tipoPessoa.equals("RF")) {
			sqlStr.append(" AND contareceber.tipoPessoa = 'RF'");
			sqlStr.append(" AND contareceber.responsavelFinanceiro = ").append(obj.getPessoa().getCodigo());
		} else if (tipoPessoa.equals("PA")) {
			sqlStr.append(" AND contareceber.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula()) && tipoPessoa.equals("AL")) {
			sqlStr.append(" and matricula.matricula = '").append(obj.getMatricula().getMatricula()).append("' ");
		}
		sqlStr.append(" AND contareceber.situacao = 'RE' ");
		if (!tipoPessoa.equals("PA")) {
			adicionarFiltroTipoOrigem(obj, sqlStr);
		}
				
		sqlStr.append(" order by ano desc ");
		sqlStr.append(" ) as t ");
		if(!usuarioVO.getIsApresentarVisaoAdministrativa()) {
			if (!verificarPermissaoFuncionalidadeUsuario("PermitirGerarDeclaracaoAnoAtual", usuarioVO)) {
				sqlStr.append("where ano::int < extract(year from current_date)");
			} else {
				sqlStr.append("where ano::int <= extract(year from current_date)");
			}
		} else {
			if (!obj.getUtilizarDataVencimentoParaDataRecebimento()) {
				sqlStr.append("where ano::int <= extract(year from current_date) ");
			} else {
				sqlStr.append("where extract(year from contareceber_datavencimento) <= extract(year from current_date) ");
			}
		}
		
		List<DeclaracaoImpostoRendaRelVO> declaracaoImpostoRendaRelVOs = new ArrayList<DeclaracaoImpostoRendaRelVO>(0);		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (rs.next()) {
			if (Uteis.isAtributoPreenchido(rs.getString("ano")) && rs.getString("ano").equals(ano)) {
			DeclaracaoImpostoRendaRelVO declaracaoImpostoRendaRelVO = new DeclaracaoImpostoRendaRelVO();
			declaracaoImpostoRendaRelVO.setAno(rs.getString("ano"));
			declaracaoImpostoRendaRelVO.getMatricula().setMatricula(rs.getString("matriculaaluno"));
			if (tipoPessoa.equals("AL")) {
				declaracaoImpostoRendaRelVO.setNomeResponsavel(rs.getString("responsavelFinanceiro"));	
			} else {
				declaracaoImpostoRendaRelVO.setNomeResponsavel(rs.getString("nome"));
			}
			declaracaoImpostoRendaRelVO.setCodigoResponsavel(rs.getInt("codigoResponsavelFinanceiro"));
			if (!Uteis.isAtributoPreenchido(declaracaoImpostoRendaRelVO.getAno()) ) {
				continue;
			}
			declaracaoImpostoRendaRelVO.getParceiro().setCodigo(rs.getInt("codigoParceiro"));
			declaracaoImpostoRendaRelVO.getParceiro().setNome(rs.getString("parceiro"));
			
			if (Uteis.isAtributoPreenchido(declaracaoImpostoRendaRelVO.getMatricula().getMatricula())){
				getFacadeFactory().getMatriculaFacade().carregarDados(declaracaoImpostoRendaRelVO.getMatricula(), NivelMontarDados.BASICO, usuarioVO);
			}
			declaracaoImpostoRendaRelVOs.add(declaracaoImpostoRendaRelVO);			
			}
		}
		return declaracaoImpostoRendaRelVOs;
	}
}
