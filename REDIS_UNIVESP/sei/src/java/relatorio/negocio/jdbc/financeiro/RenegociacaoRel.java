package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.RenegociacaoContaGeradaRelVO;
import relatorio.negocio.comuns.financeiro.RenegociacaoContaNegociadaRelVO;
import relatorio.negocio.comuns.financeiro.RenegociacaoRelVO;
import relatorio.negocio.interfaces.financeiro.RenegociacaoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class RenegociacaoRel extends SuperRelatorio implements RenegociacaoRelInterfaceFacade {

	private static final long serialVersionUID = -2079782450831120323L;

	public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipo, String matricula, Integer turma, Integer responsavelFinanceiro, Integer funcionario, Integer parceiro, Integer fornecedor) throws Exception {
		boolean excessao = true;
		for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
			if (ue.getFiltrarUnidadeEnsino()) {
				excessao = false;
			}
		}
		if (excessao) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		
		if (tipo.equals("aluno") && !Uteis.isAtributoPreenchido(matricula)) {
			throw new Exception("O campo MATRÍCULA deve ser informado.");
		}
		else if (tipo.equals("responsavelFinanceiro") && !Uteis.isAtributoPreenchido(matricula)) {
			throw new Exception("O campo RESPONSÁVEL FINANCEIRO deve ser informado.");
		}
		else if (tipo.equals("turma") && !Uteis.isAtributoPreenchido(turma)) {
			throw new Exception("O campo TURMA deve ser informado.");
		}
		else if (tipo.equals("fornecedor") && !Uteis.isAtributoPreenchido(fornecedor)) {
			throw new Exception("O campo FORNECEDOR deve ser informado.");
		}
		else if (tipo.equals("parceiro") && !Uteis.isAtributoPreenchido(parceiro)) {
			throw new Exception("O campo PARCEIRO deve ser informado.");
		}
		else if (tipo.equals("funcionario") && !Uteis.isAtributoPreenchido(funcionario)) {
			throw new Exception("O campo FUNCIONÁRIO deve ser informado.");
		}
	}

	public List<RenegociacaoRelVO> realizarCriacaoObjRel(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer turma, String matricula, String tipoContaNegociada, String tipo, String tipoRelatorio, Date dataInicio, Date dataFim, String situacaoContaReceber, String ordenarPor, String tipoPeriodo, UsuarioVO usuarioVO, Integer responsavelFinanceiro, Integer funcionario, Integer parceiro, Integer fornecedor, Integer responsavelRenegociacao) throws Exception {
		validarDados(unidadeEnsinoVOs, tipo, matricula, turma, responsavelFinanceiro, funcionario, parceiro, fornecedor);
		if (!tipo.equals("responsavelFinanceiro")) {
			responsavelFinanceiro = null;
		}
		if (!tipo.equals("parceiro")) {
			parceiro = null;
		}
		if (!tipo.equals("funcionario")) {
			funcionario = null;
		}
		if (!tipo.equals("fornecedor")) {
			fornecedor = null;
		}
		if (!tipo.equals("turma")) {
			turma = null;
		}
		if (!tipo.equals("aluno")) {
			matricula = null;
		}
		return consultarRenegociacaoContaReceber(tipo, matricula, tipoContaNegociada, turma, unidadeEnsinoVOs, tipoRelatorio, dataInicio, dataFim, situacaoContaReceber, ordenarPor, tipoPeriodo, usuarioVO, responsavelFinanceiro, funcionario, parceiro, fornecedor, responsavelRenegociacao);
	}

	public List<RenegociacaoRelVO> consultarRenegociacaoContaReceber(String tipoPessoa, String matricula, String tipoContaNegociada, Integer turma, List<UnidadeEnsinoVO> unidadeEnsinoVOs, String tipoRelatorio, Date dataInicio, Date dataFim, String situacaoContaReceber, String ordenarPor, String tipoPeriodo, UsuarioVO usuarioVO, Integer responsavelFinanceiro, Integer funcionario, Integer parceiro, Integer fornecedor, Integer responsavelRenegociacao) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct negociacaocontareceber.codigo, negociacaocontareceber.tipoPessoa, negociacaocontareceber.tipoRenegociacao, negociacaocontareceber.matriculaaluno AS \"matricula\", pessoa.nome AS \"pessoa.nome\", usuario.nome AS \"usuario.nome\", ");
		sb.append("negociacaocontareceber.valorTotal AS \"negociacaocontareceber.valorTotal\", negociacaocontareceber.valor AS \"negociacaocontareceber.valor\", ");
		sb.append("negociacaocontareceber.desconto AS \"negociacaocontareceber.desconto\", negociacaocontareceber.multa AS \"negociacaocontareceber.multa\",  ");
		sb.append("negociacaocontareceber.juro AS \"negociacaocontareceber.juro\", negociacaocontareceber.data AS \"dataNegociacao\", negociacaocontareceber.nrparcela AS \"negociacaocontareceber.nrparcela\", ");
		sb.append("negociacaocontareceber.acrescimo as \"negociacaocontareceber.acrescimo\", negociacaocontareceber.tipoDesconto, ");
		sb.append("turma.codigo AS \"turma.codigo\", turma.identificadorTurma, curso.nome as nomeCurso, negociacaocontareceber.valorEntrada AS \"negociacaocontareceber.valorEntrada\", ");
		sb.append("pessoaFuncionario.nome AS \"pessoaFuncionario.nome\", fornecedor.nome as \"fornecedor.nome\", parceiro.nome AS \"parceiro.nome\" ");
		sb.append("from negociacaocontareceber ");
		sb.append("left join pessoa on pessoa.codigo = negociacaocontareceber.pessoa ");
		sb.append("left join usuario on usuario.codigo = negociacaocontareceber.responsavel ");
		sb.append("left join matriculaperiodo on matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = negociacaocontareceber.matriculaaluno order by (mp.ano ||'/' || mp.semestre) desc limit 1) ");
		sb.append("left join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append("left join curso on curso.codigo = turma.curso ");
		sb.append("left join funcionario on funcionario.codigo = negociacaocontareceber.funcionario ");
		sb.append("left join pessoa pessoaFuncionario on pessoaFuncionario.codigo = funcionario.codigo ");
		sb.append("left join parceiro on parceiro.codigo = negociacaocontareceber.parceiro ");
		sb.append("left join fornecedor on fornecedor.codigo = negociacaocontareceber.fornecedor ");
		sb.append("left join contareceber on contareceber.codigorenegociacao = negociacaocontareceber.codigo ");
		sb.append("WHERE 1 = 1 ");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sb.append(" and negociacaocontareceber.unidadeensino in (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sb.append(ue.getCodigo() + ", ");
				}
			}
			sb.append("0) ");
		}
		if (Uteis.isAtributoPreenchido(responsavelRenegociacao)) {
			sb.append(" AND usuario.codigo = ").append(responsavelRenegociacao);
		}
		if (tipoPessoa.equals("aluno")) {
			if (matricula != null && !matricula.equals("")) {
				sb.append(" AND negociacaocontareceber.matriculaaluno = '").append(matricula).append("' ");
			}
			sb.append(" AND negociacaocontareceber.tipoPessoa = 'AL' ");
		}
		if (tipoPessoa.equals("funcionario")) {
			if (funcionario != null && funcionario > 0) {
				sb.append(" AND negociacaocontareceber.funcionario = '").append(funcionario).append("' ");
			}
			sb.append(" AND negociacaocontareceber.tipoPessoa = 'FU' ");
		}
		if (tipoPessoa.equals("parceiro")) {
			if (parceiro != null && parceiro > 0) {
				sb.append(" AND negociacaocontareceber.parceiro = ").append(parceiro).append(" ");
			}
			sb.append(" AND negociacaocontareceber.tipoPessoa = 'PA' ");
		}
		if (tipoPessoa.equals("fornecedor")) {
			if (fornecedor != null && fornecedor > 0) {
				sb.append(" AND negociacaocontareceber.fornecedor = '").append(fornecedor).append("' ");
			}
			sb.append(" AND negociacaocontareceber.tipoPessoa = 'FO' ");
		}
		if (tipoPessoa.equals("responsavelFinanceiro")) {
			if (responsavelFinanceiro != null && responsavelFinanceiro > 0) {
				sb.append(" AND pessoa.codigo = ").append(responsavelFinanceiro).append(" ");
			}
			sb.append(" AND negociacaocontareceber.tipoPessoa = 'RF' ");
		}
		if (tipoPessoa.equals("turma")) {
			if (turma != null && !turma.equals(0)) {
				sb.append(" AND matriculaperiodo.turma = ").append(turma);
			}
			sb.append(" AND negociacaocontareceber.tipoPessoa in ('AL', 'RF') ");
		}
		if (dataInicio != null) {
			if (tipoPeriodo.equals("DATA_RENEGOCIACAO")) {
				sb.append(" and (negociacaocontareceber.data::Date >= '").append(Uteis.getDataJDBC(dataInicio)).append("')");
			} else if (tipoPeriodo.equals("DATA_COMPETENCIA_CONTA_RECEBER")) {
				sb.append(" and (contareceber.datacompetencia::DATE >= '").append(Uteis.getDataPrimeiroDiaMes(dataInicio)).append("'::DATE)");
			} else {
				sb.append(" and (contareceber.dataVencimento::Date >= '").append(Uteis.getDataJDBC(dataInicio)).append("')");
			}
		}
		if (dataFim != null) {
			if (tipoPeriodo.equals("DATA_RENEGOCIACAO")) {
				sb.append(" and (negociacaocontareceber.data::Date <= '").append(Uteis.getDataJDBC(dataFim)).append("')");
			} else if (tipoPeriodo.equals("DATA_COMPETENCIA_CONTA_RECEBER")) {
				sb.append(" and (contareceber.datacompetencia::DATE <= '").append(Uteis.getDataUltimoDiaMes(dataFim)).append("'::DATE)");
			} else {
				sb.append(" and (contareceber.dataVencimento::Date <= '").append(Uteis.getDataJDBC(dataFim)).append("')");
			}
		}
		if (situacaoContaReceber != null && !situacaoContaReceber.trim().isEmpty()) {
			if (situacaoContaReceber.equals("VE")) {
				sb.append(" and contareceber.situacao = 'AR' and dataVencimento::DATE <= current_date ");
			} else {
				sb.append(" and contareceber.situacao = '").append(situacaoContaReceber).append("' ");
			}
		}
		if (!tipoContaNegociada.equals("")) {
			if (tipoContaNegociada.equals("AV")) {
				sb.append(" and ( negociacaocontareceber.tipoRenegociacao = 'AV')");
			}
			if (tipoContaNegociada.equals("VE")) {
				sb.append(" and ( negociacaocontareceber.tipoRenegociacao = 'VE')");
			}
		}
		if (tipoRelatorio.equals("sinteticoPorCurso") || tipoRelatorio.equals("analiticoPorCurso")) {
			sb.append(" order by nomeCurso, turma.identificadorturma");
		} else {
			sb.append(" order by turma.identificadorturma");
		}
		if (ordenarPor.equals("NOME")) {
			if (tipoPessoa.equals("aluno") || tipoPessoa.equals("responsavelFinanceiro")) {
				sb.append(", pessoa.nome");
			} else if (tipoPessoa.equals("parceiro")) {
				sb.append(", parceiro.nome");
			} else if (tipoPessoa.equals("fornecedor")) {
				sb.append(", fornecedor.nome");
			} else if (tipoPessoa.equals("funcionario")) {
				sb.append(", pessoaFuncionario.nome");
			}
		} else if (ordenarPor.equals("DATA")) {
			sb.append(", negociacaocontareceber.data");
		} else {
			if (tipoPessoa.equals("aluno") || tipoPessoa.equals("responsavelFinanceiro")) {
				sb.append(", pessoa.nome");
			} else if (tipoPessoa.equals("parceiro")) {
				sb.append(", parceiro.nome");
			} else if (tipoPessoa.equals("fornecedor")) {
				sb.append(", fornecedor.nome");
			} else if (tipoPessoa.equals("funcionario")) {
				sb.append(", pessoaFuncionario.nome");
			} else {
				sb.append(", pessoa.nome, parceiro.nome, fornecedor.nome, pessoaFuncionario.nome");
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, tipoContaNegociada, tipoRelatorio, usuarioVO);
	}

	public List<RenegociacaoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, String tipoContaNegociada, String tipoRelatorio, UsuarioVO usuarioVO) {
		List<RenegociacaoRelVO> vetResultado = new ArrayList<RenegociacaoRelVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, tipoContaNegociada, tipoRelatorio, usuarioVO));
		}
		return vetResultado;
	}

	public RenegociacaoRelVO montarDados(SqlRowSet dadosSQL, String tipoContaNegociada, String tipoRelatorio, UsuarioVO usuarioVO) {
		RenegociacaoRelVO obj = new RenegociacaoRelVO();
		obj.setNegociacaoContaReceber(dadosSQL.getInt("codigo"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		if (obj.getTipoPessoa().equals("AL") || obj.getTipoPessoa().equals("RF")) {
			obj.setNome(dadosSQL.getString("pessoa.nome"));
		} else if (obj.getTipoPessoa().equals("FU")) {
			obj.setNome(dadosSQL.getString("pessoaFuncionario.nome"));
		} else if (obj.getTipoPessoa().equals("PA")) {
			obj.setNome(dadosSQL.getString("parceiro.nome"));
		} else if (obj.getTipoPessoa().equals("FO")) {
			obj.setNome(dadosSQL.getString("fornecedor.nome"));
		}
		obj.setResponsavel(dadosSQL.getString("usuario.nome"));
		obj.setTipoRenegociacao(dadosSQL.getString("tipoRenegociacao"));
		obj.setValorTotal(dadosSQL.getDouble("negociacaocontareceber.valorTotal"));
		obj.setValor(dadosSQL.getDouble("negociacaocontareceber.valor"));
		obj.setValorEntrada(dadosSQL.getDouble("negociacaocontareceber.valorEntrada"));
		obj.setMulta(dadosSQL.getDouble("negociacaocontareceber.multa"));
		obj.setJuro((obj.getValor() * dadosSQL.getDouble("negociacaocontareceber.juro")) / 100);
		if (dadosSQL.getString("tipoDesconto").equals("PO")) {
			obj.setDesconto(((obj.getValor() + obj.getJuro() + obj.getMulta()) * dadosSQL.getDouble("negociacaocontareceber.desconto")) / 100);
		} else {
			obj.setDesconto(dadosSQL.getDouble("negociacaocontareceber.desconto"));
		}
		obj.setAcrescimo(dadosSQL.getDouble("negociacaocontareceber.acrescimo"));
		obj.setDataNegociacao(dadosSQL.getDate("dataNegociacao"));
		obj.setNrParcela(dadosSQL.getInt("negociacaocontareceber.nrparcela"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		obj.setNomeCurso(dadosSQL.getString("nomeCurso"));
		obj.setListaRenegociacaoContaNegociadaRelVOs(consultarContaReceberNegociadaPorNegociacaoContaReceber(tipoContaNegociada, obj.getNegociacaoContaReceber(), usuarioVO));
		obj.setListaRenegociacaoContaGeradaRelVOs(consultarContaReceberGeradaPorNegociacaoContaReceber(obj.getNegociacaoContaReceber(), usuarioVO));
		if (obj.getListaRenegociacaoContaGeradaRelVOs().size() > 0) {
			obj.setValorPrimeiraParcela(obj.getListaRenegociacaoContaGeradaRelVOs().get(0).getValor());
		}
		if (obj.getListaRenegociacaoContaGeradaRelVOs().size() > 1) {
			obj.setValorSegundaParcela(obj.getListaRenegociacaoContaGeradaRelVOs().get(1).getValor());
		}
		obj.calcularValorAcrescimoCalculado();
		obj.calcularValorDescontoCalculado();
		obj.calcularValorJuroCalculado();
		obj.calcularValorMultaCalculado();
		obj.calcularValor();
		return obj;
	}

	// public void calcularMultaEJurosContaReceber(RenegociacaoRelVO obj,
	// RenegociacaoContaNegociadaRelVO rc) {
	// double multa = rc.getValor() * (rc.getMultaPorcentagem()) / 100;
	// double juros = rc.getValorNegociado() - rc.getValor() - multa;
	//
	// obj.setMulta(obj.getMulta() + multa);
	// obj.setJuro(obj.getJuro() + juros);
	// }
	public List<RenegociacaoContaNegociadaRelVO> consultarContaReceberNegociadaPorNegociacaoContaReceber(String tipoContaNegociada, Integer negociacaoContaReceber, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct contareceber.parcela, contareceber.valor, contarecebernegociado.valor AS valorNegociado, ");
		sb.append("contareceber.datavencimento, contareceber.nossonumero, contareceber.situacao, pessoa.nome, ");
		sb.append("contareceber.valorJuroCalculado, contareceber.valorMultaCalculado, ");
		sb.append("contareceber.acrescimo, contareceber.valorIndiceReajustePorAtraso, contareceber.valorDescontoCalculado, contareceber.valorReceberCalculado, ");
		sb.append("contareceber.dataCompetencia, centroReceita.descricao as centroReceita, contareceber.descontoConvenio, ");
		sb.append("contareceber.valorRecebido ");
		sb.append("from contarecebernegociado ");
		sb.append("inner join contareceber on contareceber.codigo = contarecebernegociado.contareceber ");
		sb.append("left join centroReceita on centroReceita.codigo = contaReceber.centroreceita ");
		sb.append("left join pessoa on pessoa.codigo = contareceber.pessoa ");
		sb.append("where negociacaocontareceber = ").append(negociacaoContaReceber);
		if (tipoContaNegociada.equals("AV")) {
			sb.append(" and (dataVencimento >= '").append(Uteis.getDataBD0000(new Date())).append("') ");
		}
		if (tipoContaNegociada.equals("VE")) {
			sb.append(" and (dataVencimento <= '").append(Uteis.getDataBD0000(new Date())).append("') ");
		}
		sb.append(" order by contareceber.parcela ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaContaReceberNegociada(tabelaResultado, usuarioVO);
	}

	public List<RenegociacaoContaGeradaRelVO> consultarContaReceberGeradaPorNegociacaoContaReceber(Integer negociacaoContaReceber, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct contareceber.parcela AS parcela, contareceber.valor AS valor, contaReceber.dataCompetencia, centroReceita.descricao as centroReceita, ");
		sb.append("contareceber.datavencimento AS datavencimento, contareceber.nossonumero AS nossonumero, contareceber.situacao AS situacao, ");
		sb.append("contaReceber.valorJuroCalculado, contaReceber.valorMultaCalculado, contaReceber.acrescimo, contaReceber.valorDescontoCalculado, contaReceber.valorRecebido, ");
		sb.append("contaReceber.valorReceberCalculado ");
		sb.append("from contareceber ");
		sb.append("left join centroReceita on centroReceita.codigo = contaReceber.centroreceita ");
		sb.append("where codorigem = '").append(negociacaoContaReceber).append("' and tipoorigem = 'NCR' order by parcela ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaContaReceberGerada(tabelaResultado, usuarioVO);
	}

	public List<RenegociacaoContaNegociadaRelVO> montarDadosConsultaContaReceberNegociada(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List<RenegociacaoContaNegociadaRelVO> vetResultado = new ArrayList<RenegociacaoContaNegociadaRelVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosContaReceberNegociada(tabelaResultado, usuarioVO));
		}
		return vetResultado;
	}

	public List<RenegociacaoContaGeradaRelVO> montarDadosConsultaContaReceberGerada(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) {
		List<RenegociacaoContaGeradaRelVO> vetResultado = new ArrayList<RenegociacaoContaGeradaRelVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosContaReceberGerada(tabelaResultado, usuarioVO));
		}
		return vetResultado;
	}

	public RenegociacaoContaNegociadaRelVO montarDadosContaReceberNegociada(SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		RenegociacaoContaNegociadaRelVO obj = new RenegociacaoContaNegociadaRelVO();
		obj.setParcela(dadosSQL.getString("parcela"));
		obj.setNomePessoa(dadosSQL.getString("nome"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setValorNegociado(dadosSQL.getDouble("valorNegociado"));
		obj.setMulta(dadosSQL.getDouble("valorMultaCalculado"));
		obj.setJuro(dadosSQL.getDouble("valorJuroCalculado"));
		obj.setValorDesconto(dadosSQL.getDouble("valorDescontoCalculado"));
		obj.setAcrescimo(dadosSQL.getDouble("acrescimo"));		
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setNossoNumero(dadosSQL.getString("nossonumero"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setDataCompetencia(dadosSQL.getDate("dataCompetencia"));
		obj.setCentroReceita(dadosSQL.getString("centroReceita"));
		obj.setValorReceberCalculado(dadosSQL.getDouble("valorRecebido"));
		obj.setValorDescontoConvenio(dadosSQL.getDouble("descontoConvenio"));
		if(dadosSQL.getDouble("valorIndiceReajustePorAtraso") >= 0.0){
			obj.setAcrescimo(Uteis.arrendondarForcando2CadasDecimais(obj.getAcrescimo() + dadosSQL.getDouble("valorIndiceReajustePorAtraso")));
		}else if(dadosSQL.getDouble("valorIndiceReajustePorAtraso") < 0.0){
			obj.setValorDesconto(Uteis.arrendondarForcando2CadasDecimais(obj.getValorDesconto() - dadosSQL.getDouble("valorIndiceReajustePorAtraso")));
		}
		return obj;
	}

	public RenegociacaoContaGeradaRelVO montarDadosContaReceberGerada(SqlRowSet dadosSQL, UsuarioVO usuarioVO) {
		RenegociacaoContaGeradaRelVO obj = new RenegociacaoContaGeradaRelVO();
		obj.setParcela(dadosSQL.getString("parcela"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setNossoNumero(dadosSQL.getString("nossonumero"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setCentroReceita(dadosSQL.getString("centroReceita"));
		obj.setDataCompetencia(dadosSQL.getDate("dataCompetencia"));
		obj.setMulta(dadosSQL.getDouble("valorMultaCalculado"));
		obj.setJuro(dadosSQL.getDouble("valorJuroCalculado"));
		obj.setValorDesconto(dadosSQL.getDouble("valorDescontoCalculado"));
		obj.setAcrescimo(dadosSQL.getDouble("acrescimo"));
		obj.setValorRecebido(dadosSQL.getDouble("valorReceberCalculado"));
		return obj;
	}

	public List<TurmaVO> consultarTurma(String campoConsulta, String valorConsulta, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		if (campoConsulta.equals("identificadorTurma")) {
			return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsino(valorConsulta, unidadeEnsino, false, usuarioVO);
		}
		return new ArrayList<TurmaVO>(0);
	}

	public List<MatriculaVO> consultarAluno(String campoConsultaAluno, String valorConsultaAluno, Integer turma, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
		if (valorConsultaAluno.equals("")) {
			throw new Exception("Informe os Parâmetros para a Consulta");
		}
		if (campoConsultaAluno.equals("matricula")) {
			MatriculaVO mat = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(valorConsultaAluno, unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
			if (!mat.getMatricula().equals("")) {
				objs.add(mat);
			}
		}
		if (campoConsultaAluno.equals("nomePessoa")) {
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(valorConsultaAluno, unidadeEnsino, false, usuarioVO);
		}
		if (campoConsultaAluno.equals("nomeCurso")) {
			objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(valorConsultaAluno, unidadeEnsino, false, usuarioVO);
		}
		return objs;
	}

	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getDesignIReportRelatorio() {
		return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorio(String tipoRelatorio, Boolean excel) {
		String strExcel = "";
		if (excel) {
			strExcel = "Excel";
		}
		if (tipoRelatorio.equals("sinteticoPorTurma")) {
			return (caminhoBaseRelatorio() + getIdEntidadePorTurma() + strExcel + "Rel.jrxml");
		} else if (tipoRelatorio.equals("sinteticoPorCurso")) {
			return (caminhoBaseRelatorio() + getIdEntidadePorCurso() + strExcel + "Rel.jrxml");
		} else if (tipoRelatorio.equals("analiticoPorTurma")) {
			return (caminhoBaseRelatorio() + getIdEntidadeAnaliticoPorTurma() + strExcel + "Rel.jrxml");
		} else {
			return (caminhoBaseRelatorio() + getIdEntidadeAnaliticoPorCurso() + strExcel + "Rel.jrxml");
		}
	}

	public static String getIdEntidadePorTurma() {
		return ("RenegociacaoSintetico");
	}

	public static String getIdEntidadeAnaliticoPorTurma() {
		return ("Renegociacao");
	}

	public static String getIdEntidadePorCurso() {
		return ("RenegociacaoSinteticoPorCurso");
	}

	public static String getIdEntidadeAnaliticoPorCurso() {
		return ("RenegociacaoPorCurso");
	}
}
