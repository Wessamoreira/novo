package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.negocio.comuns.financeiro.ContaReceberDescricaoDescontosRelVO;
import relatorio.negocio.comuns.financeiro.ContaReceberQuadroFormaRecebimentoRelVO;
import relatorio.negocio.comuns.financeiro.ContaReceberRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.financeiro.ContaReceberRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
@SuppressWarnings({"rawtypes", "unchecked"})
@Scope("singleton")
@Repository
@Lazy
public class ContaReceberRel extends SuperRelatorio implements ContaReceberRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public ContaReceberRel() {
		inicializarOrdenacoesRelatorio();
	}

	@Override
	public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		boolean excessao = true;
		for (UnidadeEnsinoVO obj : unidadeEnsinoVOs) {
			if (obj.getFiltrarUnidadeEnsino()) {
				excessao = false;
				break;
			}
		}
		if (excessao) {
			throw new Exception("Ao menos uma Unidade de Ensino deve ser selecionada!");
		}
	}

	public List<ContaReceberRelVO> criarObjeto(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Date dataInicio, Date dataFim, String tipoPessoa, String alunoMatriula, String alunoNome, String funcionarioMatricula, String funcionarioNome, String candidatoCpf, String candidatoNome, String parceiroCPF, String parceiroCNPJ, String parceiroNome, String situacao, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, ContaCorrenteVO contaCorrente, Integer pessoa, Integer parceiroCodigo, Integer opcaoOrdenacao, UsuarioVO usuarioLogado, ConfiguracaoFinanceiroVO confFinanVO, Integer centroReceita, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, Integer responsavelFinanceiroCodigo, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean consideraUnidadeEnsinoFinanceira) throws Exception {
		List<ContaReceberRelVO> contaReceberRelVOs = new ArrayList<ContaReceberRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(filtroRelatorioFinanceiroVO, dataInicio, dataFim, tipoPessoa, alunoMatriula, alunoNome, funcionarioMatricula, funcionarioNome, candidatoCpf, candidatoNome, parceiroCPF, parceiroCNPJ, parceiroNome, situacao, unidadeEnsinoVOs, unidadeEnsinoCursoVO, turmaVO, contaCorrente, pessoa, parceiroCodigo, opcaoOrdenacao, centroReceita, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, responsavelFinanceiroCodigo, dataInicioCompetencia, dataFimCompetencia, consideraUnidadeEnsinoFinanceira);
		while (dadosSQL.next()) {
			contaReceberRelVOs.add(montarDados(dadosSQL, usuarioLogado, confFinanVO));
		}
		if (contaReceberRelVOs.isEmpty()) {
			return contaReceberRelVOs;
		}
		dadosSQL = realizarConsultaQuadroFormaRecebimento(filtroRelatorioFinanceiroVO, contaReceberRelVOs, dataInicio, dataFim, tipoPessoa, alunoMatriula, alunoNome, funcionarioMatricula, funcionarioNome, candidatoCpf, candidatoNome, parceiroCPF, parceiroCNPJ, parceiroNome, situacao, unidadeEnsinoVOs, unidadeEnsinoCursoVO, turmaVO, contaCorrente, pessoa, parceiroCodigo, centroReceita, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, responsavelFinanceiroCodigo, dataInicioCompetencia, dataFimCompetencia, consideraUnidadeEnsinoFinanceira);
		montarDadosQuadroFormaRecebimento(dadosSQL, contaReceberRelVOs);
		return contaReceberRelVOs;
	}

	public SqlRowSet realizarConsultaQuadroFormaRecebimento(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, List<ContaReceberRelVO> contaReceberRelVOs, Date dataInicio, Date dataFim, String tipoPessoa, String alunoMatriula, String alunoNome, String funcionarioMatricula, String funcionarioNome, String candidatoCpf, String candidatoNome, String parceiroCPF, String parceiroCNPJ, String parceiroNome, String situacao, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, ContaCorrenteVO contaCorrente, Integer pessoa, Integer parceiroCodigo, Integer centroReceita, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, Integer responsavelFinanceiroCodigo, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean consideraUnidadeEnsinoFinanceira) {
		StringBuilder sql = new StringBuilder(executarMontarCabecalhoSqlSubRelatorioQuadroFormaPagamento());
		montarRelacionamentoFormaRecebimento(sql);
		montarFiltrosRelatorio(filtroRelatorioFinanceiroVO, sql, dataInicio, dataFim, tipoPessoa, alunoMatriula, alunoNome, funcionarioMatricula, funcionarioNome, candidatoCpf, candidatoNome, parceiroCPF, parceiroCNPJ, parceiroNome, situacao, unidadeEnsinoVOs, unidadeEnsinoCursoVO, turmaVO, contaCorrente, pessoa, parceiroCodigo, centroReceita, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, responsavelFinanceiroCodigo, dataInicioCompetencia, dataFimCompetencia, consideraUnidadeEnsinoFinanceira);
		sql.append("group by contareceber_codigoFormaPagamento, contaReceber_nomeFormaPagamento order by contareceber_codigoFormaPagamento ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}

	private void montarDadosQuadroFormaRecebimento(SqlRowSet dadosSQL, List<ContaReceberRelVO> contaReceberRelVOs) {
		List<ContaReceberQuadroFormaRecebimentoRelVO> listaQuadroFormaRecebimento = new ArrayList<ContaReceberQuadroFormaRecebimentoRelVO>(0);
		while (dadosSQL.next()) {
			ContaReceberQuadroFormaRecebimentoRelVO contaReceberQuadroFormaRecebimentoRelVO;
			contaReceberQuadroFormaRecebimentoRelVO = new ContaReceberQuadroFormaRecebimentoRelVO();
			contaReceberQuadroFormaRecebimentoRelVO.setCodigoFormaPagamento(dadosSQL.getInt("contareceber_codigoFormaPagamento"));
			contaReceberQuadroFormaRecebimentoRelVO.setNomeFormaPagamento(dadosSQL.getString("contaReceber_nomeFormaPagamento"));
			contaReceberQuadroFormaRecebimentoRelVO.setValor(dadosSQL.getDouble("contareceber_valorRecebido") + dadosSQL.getDouble("contareceber_valorReceberCalculado"));
			contaReceberQuadroFormaRecebimentoRelVO.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
			contaReceberQuadroFormaRecebimentoRelVO.setJuro(dadosSQL.getDouble("contareceber_juro"));
			contaReceberQuadroFormaRecebimentoRelVO.setMulta(dadosSQL.getDouble("contareceber_multa"));
			Double valorDescontoTotal = 0.0;
			valorDescontoTotal = dadosSQL.getDouble("valordescontoalunojacalculado") + dadosSQL.getDouble("valordescontoprogressivo") + dadosSQL.getDouble("descontoconvenio") + dadosSQL.getDouble("descontoinstituicao") + dadosSQL.getDouble("valorCalculadoDescontoLancadoRecebimento") + dadosSQL.getDouble("valorDescontoRateio");
			contaReceberQuadroFormaRecebimentoRelVO.setValorDesconto(valorDescontoTotal);
			listaQuadroFormaRecebimento.add(contaReceberQuadroFormaRecebimentoRelVO);
		}
		if (!listaQuadroFormaRecebimento.isEmpty()) {
			contaReceberRelVOs.get(contaReceberRelVOs.size() - 1).setListaQuadroFormaRecebimento(listaQuadroFormaRecebimento);
		}
	}

	private ContaReceberRelVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioLogado, ConfiguracaoFinanceiroVO confFinanVO) throws Exception {
		ContaReceberRelVO contaReceberRelVO = new ContaReceberRelVO();
		contaReceberRelVO.setMatricula(dadosSQL.getString("matricula"));
		contaReceberRelVO.setSituacao(dadosSQL.getString("contareceber_situacao"));
		// if ((dadosSQL.getString("matricula").equals("1311FMAGRN074")) ||
		// (dadosSQL.getString("matricula").equals("1111FMCBBN005"))
		// || (dadosSQL.getString("matricula").equals("1311FMENAM004")) ){
		// System.out.println("...");
		// //System.out.println("...");
		// }
		contaReceberRelVO.getContaReceberVO().getMatriculaAluno().setMatricula(dadosSQL.getString("matricula"));
		contaReceberRelVO.setTipoPessoa(dadosSQL.getString("contareceber_tipopessoa"));
		contaReceberRelVO.getContaReceberVO().setTipoPessoa(dadosSQL.getString("contareceber_tipopessoa"));
		contaReceberRelVO.setData(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_data")));
		contaReceberRelVO.getContaReceberVO().setData(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_data")));
		contaReceberRelVO.setTipoOrigem(TipoOrigemContaReceber.getDescricao(dadosSQL.getString("contareceber_tipoorigem")));
		contaReceberRelVO.getContaReceberVO().setTipoOrigem(TipoOrigemContaReceber.getDescricao(dadosSQL.getString("contareceber_tipoorigem")));
		contaReceberRelVO.setSituacao(SituacaoContaReceber.getDescricao(dadosSQL.getString("contareceber_situacao")));
		contaReceberRelVO.getContaReceberVO().setSituacao(SituacaoContaReceber.getDescricao(dadosSQL.getString("contareceber_situacao")));
		contaReceberRelVO.setDescricaoPagamento(dadosSQL.getString("contareceber_descricaopagamento"));
		contaReceberRelVO.getContaReceberVO().setDescricaoPagamento(dadosSQL.getString("contareceber_descricaopagamento"));
		contaReceberRelVO.setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_datavencimento")));
		contaReceberRelVO.getContaReceberVO().setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_datavencimento")));
		contaReceberRelVO.setValor(dadosSQL.getDouble("contareceber_valor"));
		contaReceberRelVO.getContaReceberVO().setValor(dadosSQL.getDouble("contareceber_valor"));
		contaReceberRelVO.setValorBaseContaReceber(dadosSQL.getDouble("contareceber_valorBaseContaReceber"));
		contaReceberRelVO.getContaReceberVO().setValorBaseContaReceber(dadosSQL.getDouble("contareceber_valorBaseContaReceber"));
		contaReceberRelVO.setValorCusteadoContaReceber(dadosSQL.getDouble("contareceber_valorCusteadoContaReceber"));
		contaReceberRelVO.getContaReceberVO().setValorCusteadoContaReceber(dadosSQL.getDouble("contareceber_valorCusteadoContaReceber"));

		contaReceberRelVO.setNossoNumero(dadosSQL.getString("contareceber_nossoNumero"));
		contaReceberRelVO.getContaReceberVO().setNossoNumero(dadosSQL.getString("contareceber_nossoNumero"));
		contaReceberRelVO.setCodigoCentroReceita(dadosSQL.getInt("centroreceita.codigo"));
		contaReceberRelVO.getContaReceberVO().getCentroReceita().setCodigo(dadosSQL.getInt("centroreceita.codigo"));
		contaReceberRelVO.setNomeCentroReceita(dadosSQL.getString("centroreceita.descricao"));
		contaReceberRelVO.getContaReceberVO().getCentroReceita().setDescricao(dadosSQL.getString("centroreceita.descricao"));
		contaReceberRelVO.setIdentificadorCentroReceita(dadosSQL.getString("centroreceita.identificadorcentroreceita"));
		contaReceberRelVO.getContaReceberVO().getCentroReceita().setIdentificadorCentroReceita(dadosSQL.getString("centroreceita.identificadorcentroreceita"));

		contaReceberRelVO.setJuroPorcentagem(dadosSQL.getDouble("contareceber_juroporcentagem"));
		contaReceberRelVO.getContaReceberVO().setJuroPorcentagem(dadosSQL.getDouble("contareceber_juroporcentagem"));
		contaReceberRelVO.setMultaPorcentagem(dadosSQL.getDouble("contareceber_multaporcentagem"));
		contaReceberRelVO.getContaReceberVO().setMultaPorcentagem(dadosSQL.getDouble("contareceber_multaporcentagem"));
		contaReceberRelVO.setNrDocumento(dadosSQL.getString("contareceber_nrdocumento"));
		contaReceberRelVO.getContaReceberVO().setNrDocumento(dadosSQL.getString("contareceber_nrdocumento"));

		if (dadosSQL.getString("contareceber_parcela").equals("Matrícula")) {
			contaReceberRelVO.setParcela("Mat.");
		} else {
			contaReceberRelVO.setParcela(dadosSQL.getString("contareceber_parcela"));
		}
		contaReceberRelVO.setOrigemNegociacaoReceber(dadosSQL.getString("contareceber_origemnegociacaoreceber"));
		if (contaReceberRelVO.getTipoPessoa().equals("FO")) {
			contaReceberRelVO.setNomeFornecedor(dadosSQL.getString("fornecedor.nome"));
		} else if (contaReceberRelVO.getTipoPessoa().equals("PA")) {
			contaReceberRelVO.setNomeParceiro(dadosSQL.getString("parceiro2.nome"));
		} else if (contaReceberRelVO.getTipoPessoa().equals("RF")) {
			contaReceberRelVO.setNomeResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
		} else {
			contaReceberRelVO.setNomeParceiro(dadosSQL.getString("parceiro_nome"));
			contaReceberRelVO.setNomePessoa(dadosSQL.getString("pessoa_nome"));
		}

		contaReceberRelVO.setCodigo(dadosSQL.getInt("contareceber_codigo"));
		contaReceberRelVO.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber_codigo"));
		contaReceberRelVO.setLinhaDigitavelCodigoBarras(dadosSQL.getString("linhaDigitavelCodigoBarras"));
		contaReceberRelVO.getContaReceberVO().setLinhaDigitavelCodigoBarras(dadosSQL.getString("linhaDigitavelCodigoBarras"));
		contaReceberRelVO.setCodigoBarra(dadosSQL.getString("codigobarra"));
		contaReceberRelVO.getContaReceberVO().setCodigoBarra(dadosSQL.getString("codigobarra"));

		contaReceberRelVO.setRecebimentoBancario(dadosSQL.getString("contareceber_recebimentobancario"));
		contaReceberRelVO.setReconheceuDivida(dadosSQL.getString("matriculaperiodo_reconheceudivida"));
		if (dadosSQL.getString("contacorrente_numero") == null) {
			contaReceberRelVO.setNumeroContaCorrente("");
		} else {
			contaReceberRelVO.setNumeroContaCorrente(dadosSQL.getString("contacorrente_numero"));
		}
		if (dadosSQL.getString("contacorrente_digito") == null) {
			contaReceberRelVO.setDigito("");
		} else {
			contaReceberRelVO.setDigito(dadosSQL.getString("contacorrente_digito"));
		}
		contaReceberRelVO.setUsuarioResponsavel(dadosSQL.getString("usuario_responsavel"));
		contaReceberRelVO.setDataNegociacaoRecebimento(Uteis.getDataJDBC(dadosSQL.getDate("negociacaorecebimento_data")));
		contaReceberRelVO.getContaReceberVO().setOrdemConvenio(dadosSQL.getInt("contareceber.ordemConvenio"));
		contaReceberRelVO.getContaReceberVO().setOrdemConvenioValorCheio(dadosSQL.getBoolean("contareceber.OrdemConvenioValorCheio"));
		contaReceberRelVO.getContaReceberVO().setOrdemDescontoAluno(dadosSQL.getInt("contareceber.OrdemDescontoAluno"));
		contaReceberRelVO.getContaReceberVO().setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("contareceber.OrdemDescontoAlunoValorCheio"));
		contaReceberRelVO.getContaReceberVO().setOrdemDescontoProgressivo(dadosSQL.getInt("contareceber.OrdemDescontoProgressivo"));
		contaReceberRelVO.getContaReceberVO().setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("contareceber.OrdemDescontoProgressivoValorCheio"));
		contaReceberRelVO.getContaReceberVO().setOrdemPlanoDesconto(dadosSQL.getInt("contareceber.OrdemPlanoDesconto"));
		contaReceberRelVO.getContaReceberVO().setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("contareceber.OrdemPlanoDescontoValorCheio"));

		contaReceberRelVO.getContaReceberVO().setUsaDescontoCompostoPlanoDesconto(dadosSQL.getBoolean("contareceber.usaDescontoCompostoPlanoDesconto"));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setCodigo(new Integer(dadosSQL.getInt("descontoprogressivo.codigo")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setNome(dadosSQL.getString("descontoprogressivo.nome"));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setDiaLimite1(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite1")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite1(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite1")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setDiaLimite2(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite2")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite2(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite2")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setDiaLimite3(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite3")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite3(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite3")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setDiaLimite4(new Integer(dadosSQL.getInt("descontoprogressivo.diaLimite4")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite4(new Double(dadosSQL.getDouble("descontoprogressivo.percDescontoLimite4")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite1(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite1")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite2(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite2")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite3(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite3")));		
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite4(new Double(dadosSQL.getDouble("descontoprogressivo.valorDescontoLimite4")));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setAtivado(dadosSQL.getBoolean("descontoprogressivo.ativado"));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setUtilizarDiaFixo(dadosSQL.getBoolean("descontoprogressivo.utilizarDiaFixo"));
		contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setUtilizarDiaUtil(dadosSQL.getBoolean("descontoprogressivo.utilizarDiaUtil"));

		if (contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().getCodigo() > 0) {
			contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().setNovoObj(Boolean.FALSE);
		}

		// DADOS PlanoDescontoContaReceberVO
		PlanoDescontoContaReceberVO planoDescontoContaReceberVO = null;

		// HASH PARA TRATAR LISTAS DE UM MESMO OBJETO
		HashMap<Integer, PlanoDescontoContaReceberVO> hashtablePlanoDescontoContaReceber = new HashMap<Integer, PlanoDescontoContaReceberVO>(0);

		do {
			if (!contaReceberRelVO.getContaReceberVO().getCodigo().equals(dadosSQL.getInt("contareceber_codigo"))) {
				dadosSQL.previous();
				break;
			}
			planoDescontoContaReceberVO = new PlanoDescontoContaReceberVO();

			planoDescontoContaReceberVO.setCodigo(dadosSQL.getInt("pdcr.codigo"));
			planoDescontoContaReceberVO.setContaReceber(dadosSQL.getInt("pdcr.contaReceber"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setCodigo(dadosSQL.getInt("pdcr.planoDesconto"));
			planoDescontoContaReceberVO.setTipoItemPlanoFinanceiro(dadosSQL.getString("pdcr.tipoItemPlanoFinanceiro"));
			planoDescontoContaReceberVO.getConvenio().setCodigo(dadosSQL.getInt("pdcr.convenio"));
			planoDescontoContaReceberVO.setValorUtilizadoRecebimento(dadosSQL.getDouble("pdcr.valorutilizadorecebimento"));
			if (planoDescontoContaReceberVO.getCodigo() > 0) {
				planoDescontoContaReceberVO.setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getPlanoDescontoVO().setCodigo(new Integer(dadosSQL.getInt("pd.codigo")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setNome(dadosSQL.getString("pd.nome"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setPercDescontoParcela(new Double(dadosSQL.getDouble("pd.percDescontoParcela")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setPercDescontoMatricula(new Double(dadosSQL.getDouble("pd.percDescontoMatricula")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setRequisitos(dadosSQL.getString("pd.requisitos"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDescricao(dadosSQL.getString("pd.descricao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setSomente1PeriodoLetivoParcela(new Boolean(dadosSQL.getBoolean("pd.somente1PeriodoLetivoParcela")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setSomente1PeriodoLetivoMatricula(new Boolean(dadosSQL.getBoolean("pd.somente1PeriodoLetivoMatricula")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setTipoDescontoParcela(dadosSQL.getString("pd.tipoDescontoParcela"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setTipoDescontoMatricula(dadosSQL.getString("pd.tipoDescontoMatricula"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDiasValidadeVencimento(new Integer(dadosSQL.getInt("pd.diasValidadeVencimento")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setAtivo(dadosSQL.getBoolean("pd.ativo"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDataAtivacao(dadosSQL.getDate("pd.dataAtivacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDataInativacao(dadosSQL.getDate("pd.dataInativacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setCodigo(dadosSQL.getInt("pd.responsavelAtivacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDescontoValidoAteDataVencimento(dadosSQL.getBoolean("pd.descontoValidoAteDataVencimento"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setAplicarSobreValorCheio(dadosSQL.getBoolean("pd.aplicarSobreValorCheio"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setUtilizarDiaUtil(dadosSQL.getBoolean("pd.utilizarDiaUtil"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setUtilizarDiaFixo(dadosSQL.getBoolean("pd.utilizarDiaFixo"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setUtilizarAvancoDiaUtil(dadosSQL.getBoolean("pd.utilizarAvancoDiaUtil"));

			if (planoDescontoContaReceberVO.getPlanoDescontoVO().getCodigo() > 0) {
				planoDescontoContaReceberVO.getPlanoDescontoVO().setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getConvenio().setCodigo(dadosSQL.getInt("c.codigo"));
			planoDescontoContaReceberVO.getConvenio().setDescricao(dadosSQL.getString("c.descricao"));
			planoDescontoContaReceberVO.getConvenio().setDataAssinatura(dadosSQL.getDate("c.dataAssinatura"));
			planoDescontoContaReceberVO.getConvenio().getParceiro().setCodigo(dadosSQL.getInt("c.parceiro"));
			planoDescontoContaReceberVO.getConvenio().setAtivo(dadosSQL.getBoolean("c.ativo"));
			planoDescontoContaReceberVO.getConvenio().setCobertura(dadosSQL.getString("c.cobertura"));
			planoDescontoContaReceberVO.getConvenio().setPreRequisitos(dadosSQL.getString("c.preRequisitos"));
			planoDescontoContaReceberVO.getConvenio().setDataInicioVigencia(dadosSQL.getDate("c.dataInicioVigencia"));
			planoDescontoContaReceberVO.getConvenio().setDataFinalVigencia(dadosSQL.getDate("c.dataFinalVigencia"));
			planoDescontoContaReceberVO.getConvenio().setDescontoMatricula(dadosSQL.getDouble("c.descontoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setTipoDescontoMatricula(dadosSQL.getString("c.tipoDescontoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setDescontoParcela(dadosSQL.getDouble("c.descontoParcela"));
			planoDescontoContaReceberVO.getConvenio().setTipoDescontoParcela(dadosSQL.getString("c.tipoDescontoParcela"));
			planoDescontoContaReceberVO.getConvenio().setBolsaCusteadaParceiroMatricula(dadosSQL.getDouble("c.bolsaCusteadaParceiroMatricula"));
			planoDescontoContaReceberVO.getConvenio().setTipoBolsaCusteadaParceiroMatricula(dadosSQL.getString("c.tipoBolsaCusteadaParceiroMatricula"));
			planoDescontoContaReceberVO.getConvenio().setBolsaCusteadaParceiroParcela(dadosSQL.getDouble("c.bolsaCusteadaParceiroParcela"));
			planoDescontoContaReceberVO.getConvenio().setTipoBolsaCusteadaParceiroParcela(dadosSQL.getString("c.tipoBolsaCusteadaParceiroParcela"));
			planoDescontoContaReceberVO.getConvenio().getFormaRecebimentoParceiro().setCodigo(dadosSQL.getInt("c.formaRecebimentoParceiro"));
			planoDescontoContaReceberVO.getConvenio().setDiaBaseRecebimentoParceiro(dadosSQL.getInt("c.diaBaseRecebimentoParceiro"));
			planoDescontoContaReceberVO.getConvenio().getRequisitante().setCodigo(dadosSQL.getInt("c.requisitante"));
			planoDescontoContaReceberVO.getConvenio().setDataRequisicao(dadosSQL.getDate("c.dataRequisicao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("c.responsavelAutorizacao"));
			planoDescontoContaReceberVO.getConvenio().setDataAutorizacao(dadosSQL.getDate("c.dataAutorizacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelFinalizacao().setCodigo(dadosSQL.getInt("c.responsavelFinalizacao"));
			planoDescontoContaReceberVO.getConvenio().setDataFinalizacao(dadosSQL.getDate("c.dataFinalizacao"));
			planoDescontoContaReceberVO.getConvenio().setSituacao(dadosSQL.getString("c.situacao"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodoCurso(dadosSQL.getBoolean("c.validoParaTodoCurso"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodaUnidadeEnsino(dadosSQL.getBoolean("c.validoParaTodaUnidadeEnsino"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodoTurno(dadosSQL.getBoolean("c.validoParaTodoTurno"));
			planoDescontoContaReceberVO.getConvenio().setPeriodoIndeterminado(dadosSQL.getBoolean("c.periodoIndeterminado"));
			planoDescontoContaReceberVO.getConvenio().setDataAtivacao(dadosSQL.getDate("c.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().setDataInativacao(dadosSQL.getDate("c.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelAtivacao().setCodigo(dadosSQL.getInt("c.responsavelAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelInativacao().setCodigo(dadosSQL.getInt("c.responsavelInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setCodigo(dadosSQL.getInt("c.descontoProgressivoParceiro"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setCodigo(dadosSQL.getInt("c.descontoProgressivoAluno"));
			planoDescontoContaReceberVO.getConvenio().setPossuiDescontoAntecipacao(dadosSQL.getBoolean("c.possuiDescontoAntecipacao"));
			planoDescontoContaReceberVO.getConvenio().setCalculadoEmCimaValorLiquido(dadosSQL.getBoolean("c.calculadoEmCimaValorLiquido"));
			planoDescontoContaReceberVO.getConvenio().setAplicarDescontoProgressivoMatricula(dadosSQL.getBoolean("c.aplicarDescontoProgressivoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setAplicarDescontoProgressivoMatriculaParceiro(dadosSQL.getBoolean("c.aplicarDescontoProgressivoMatriculaParceiro"));
			if (planoDescontoContaReceberVO.getConvenio().getCodigo() > 0) {
				planoDescontoContaReceberVO.getConvenio().setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setCodigo(new Integer(dadosSQL.getInt("dpa.codigo")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setNome(dadosSQL.getString("dpa.nome"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite1(new Integer(dadosSQL.getInt("dpa.diaLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite1(new Double(dadosSQL.getDouble("dpa.percDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite2(new Integer(dadosSQL.getInt("dpa.diaLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite2(new Double(dadosSQL.getDouble("dpa.percDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite3(new Integer(dadosSQL.getInt("dpa.diaLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite3(new Double(dadosSQL.getDouble("dpa.percDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite4(new Integer(dadosSQL.getInt("dpa.diaLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite4(new Double(dadosSQL.getDouble("dpa.percDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite1(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite2(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite3(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite4(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setAtivado(dadosSQL.getBoolean("dpa.ativado"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDataAtivacao(dadosSQL.getDate("dpa.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("dpa.responsavelAtivacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDataInativacao(dadosSQL.getDate("dpa.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("dpa.responsavelInativacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setUtilizarDiaFixo(dadosSQL.getBoolean("dpa.utilizarDiaFixo"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setUtilizarDiaUtil(dadosSQL.getBoolean("dpa.utilizarDiaUtil"));
			if (planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getCodigo() > 0) {
				planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setNovoObj(Boolean.FALSE);
			}

			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setCodigo(new Integer(dadosSQL.getInt("dpp.codigo")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setNome(dadosSQL.getString("dpp.nome"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite1(new Integer(dadosSQL.getInt("dpp.diaLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite1(new Double(dadosSQL.getDouble("dpp.percDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite2(new Integer(dadosSQL.getInt("dpp.diaLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite2(new Double(dadosSQL.getDouble("dpp.percDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite3(new Integer(dadosSQL.getInt("dpp.diaLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite3(new Double(dadosSQL.getDouble("dpp.percDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite4(new Integer(dadosSQL.getInt("dpp.diaLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite4(new Double(dadosSQL.getDouble("dpp.percDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite1(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite2(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite3(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite4(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setAtivado(dadosSQL.getBoolean("dpp.ativado"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDataAtivacao(dadosSQL.getDate("dpp.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("dpp.responsavelAtivacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDataInativacao(dadosSQL.getDate("dpp.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("dpp.responsavelInativacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setUtilizarDiaFixo(dadosSQL.getBoolean("dpp.utilizarDiaFixo"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setUtilizarDiaUtil(dadosSQL.getBoolean("dpp.utilizarDiaUtil"));
			if (planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getCodigo() > 0) {
				planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setNovoObj(Boolean.FALSE);
			}

			if (!hashtablePlanoDescontoContaReceber.containsKey(planoDescontoContaReceberVO.getCodigo()) && planoDescontoContaReceberVO.getCodigo() != 0) {
				contaReceberRelVO.getContaReceberVO().getPlanoDescontoContaReceberVOs().add(planoDescontoContaReceberVO);
			}
			hashtablePlanoDescontoContaReceber.put(planoDescontoContaReceberVO.getCodigo(), planoDescontoContaReceberVO);
			if (dadosSQL.isLast()) {
				break;
			}
		} while (dadosSQL.next());
		contaReceberRelVO.getContaReceberVO().setValorDescontoInstituicao(dadosSQL.getDouble("contareceber_valordescontoinstituicao"));
		contaReceberRelVO.getContaReceberVO().setValorDescontoConvenio(dadosSQL.getDouble("contareceber_valordescontoconvenio"));
		contaReceberRelVO.getContaReceberVO().setValorDesconto(dadosSQL.getDouble("contareceber_desconto"));
		contaReceberRelVO.getContaReceberVO().setValorDescontoAlunoJaCalculado(dadosSQL.getDouble("valordescontoalunojacalculado"));
		contaReceberRelVO.getContaReceberVO().setValorDescontoProgressivo(dadosSQL.getDouble("contareceber_valordescontoprogressivo"));
		contaReceberRelVO.getContaReceberVO().setValorDescontoRateio(dadosSQL.getDouble("cr.valordescontorateio"));
		contaReceberRelVO.getContaReceberVO().setValorCalculadoDescontoLancadoRecebimento(dadosSQL.getDouble("contareceber_valorCalculadoDescontoLancadoRecebimento"));
		contaReceberRelVO.setJuro(dadosSQL.getDouble("contareceber_juro"));
		contaReceberRelVO.setMulta(dadosSQL.getDouble("contareceber_multa"));
		contaReceberRelVO.setAcrescimo(dadosSQL.getDouble("contareceber_acrescimo"));
		if(contaReceberRelVO.getContaReceberVO().getTipoParceiro() && dadosSQL.getBoolean("parceiro2.isentarJuro")){
			contaReceberRelVO.setJuro(0.0);			
			contaReceberRelVO.getContaReceberVO().setJuro(0.0);
		}
		if(contaReceberRelVO.getContaReceberVO().getTipoParceiro() && dadosSQL.getBoolean("parceiro2.isentarMulta")){
			contaReceberRelVO.setMulta(0.0);
			contaReceberRelVO.getContaReceberVO().setMulta(0.0);
			
		}

		if (dadosSQL.getString("contareceber_situacao").equals("AR")) {
			contaReceberRelVO.getContaReceberVO().setRealizandoRecebimento(true);
			// contaReceberRelVO.setValorRecebido(contaReceberRelVO.getContaReceberVO().getCalcularValorFinal(new
			// Date(), confFinanVO, false, new Date()));
			contaReceberRelVO.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebercalculado"));
		} else {
			contaReceberRelVO.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
			contaReceberRelVO.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
		}
		// Valor total dos descontos somados
		contaReceberRelVO.setValorDesconto(contaReceberRelVO.getContaReceberVO().getValorTotalDescontoContaReceber());

		// Lista de todos os descontos que sao institucionais (ou seja,
		// oferecidos por meio plano de desconto padrão)
		contaReceberRelVO.setDescontoPrevistoInstituicao(contaReceberRelVO.getContaReceberVO().getValorDescontoInstituicao());
		contaReceberRelVO.setValorDescontoConvenio(contaReceberRelVO.getContaReceberVO().getValorDescontoConvenio());
		// Lista de todos os outros descontos do aluno
		contaReceberRelVO.setDescontoFixo((contaReceberRelVO.getContaReceberVO().getValorTotalDescontoContaReceber() - contaReceberRelVO.getDescontoPrevistoInstituicao()));
		
		contaReceberRelVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("contareceber_valordescontocalculadoprimeirafaixadescontos"));
		contaReceberRelVO.getContaReceberVO().setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("contareceber_valordescontocalculadoprimeirafaixadescontos") + dadosSQL.getDouble("contareceber_valorCusteadoContaReceber"));
		
		contaReceberRelVO.setValorAcrescimoJuroMulta(contaReceberRelVO.getJuro() + contaReceberRelVO.getMulta() + contaReceberRelVO.getAcrescimo());
		contaReceberRelVO.setNomeTurma(dadosSQL.getString("turma.identificadorTurma"));
		
		
		if(dadosSQL.getBigDecimal("contareceber_valorindicereajusteporatraso") != null && dadosSQL.getBigDecimal("contareceber_valorindicereajusteporatraso").doubleValue() >= 0.0 ){
			contaReceberRelVO.setAcrescimo(contaReceberRelVO.getAcrescimo() + dadosSQL.getBigDecimal("contareceber_valorindicereajusteporatraso").doubleValue());
		}else if(dadosSQL.getBigDecimal("contareceber_valorindicereajusteporatraso") != null && dadosSQL.getBigDecimal("contareceber_valorindicereajusteporatraso").doubleValue() < 0.0 ){
			contaReceberRelVO.setDescontoFixo(contaReceberRelVO.getDescontoFixo() - dadosSQL.getBigDecimal("contareceber_valorindicereajusteporatraso").doubleValue());
		}

		/**
		 * Montando dados referente ao detalhamento dos descontos Layout 4
		 */
		ContaReceberDescricaoDescontosRelVO crddr = null;
		for (PlanoDescontoContaReceberVO pdcr : contaReceberRelVO.getContaReceberVO().getPlanoDescontoContaReceberVOs()) {
			crddr = new ContaReceberDescricaoDescontosRelVO();
			crddr.setTipoDesconto(pdcr.getTipoItemPlanoFinanceiro());
			if (pdcr.getTipoItemPlanoFinanceiro().equals("PD")) {
				crddr.setDescricaoDesconto(pdcr.getPlanoDescontoVO().getNome());
				crddr.setValorDescontoIntitucional(pdcr.getValorUtilizadoRecebimento());
			} else if (pdcr.getTipoItemPlanoFinanceiro().equals("CO")) {
				crddr.setDescricaoDesconto(pdcr.getConvenio().getDescricao());
				crddr.setValorDescontoConvenio(contaReceberRelVO.getValorDescontoConvenio());
			}
			contaReceberRelVO.getContaReceberDescricaoDescontosRelVOs().add(crddr);
		}
		Double valorOutrosDescontos = contaReceberRelVO.getDescontoFixo() - contaReceberRelVO.getContaReceberVO().getValorDescontoProgressivo();
		if (valorOutrosDescontos != null && valorOutrosDescontos > 0) {
			crddr = new ContaReceberDescricaoDescontosRelVO();
			crddr.setDescricaoDesconto("Outros");
			crddr.setValorOutrosDescontos(valorOutrosDescontos);
			contaReceberRelVO.getContaReceberDescricaoDescontosRelVOs().add(crddr);
		}
		if (Uteis.isAtributoPreenchido(contaReceberRelVO.getContaReceberVO().getDescontoProgressivo())) {
			crddr = new ContaReceberDescricaoDescontosRelVO();
			crddr.setDescricaoDesconto(contaReceberRelVO.getContaReceberVO().getDescontoProgressivo().getNome());
			crddr.setValorOutrosDescontos(contaReceberRelVO.getContaReceberVO().getValorDescontoProgressivo());
			contaReceberRelVO.getContaReceberDescricaoDescontosRelVOs().add(crddr);
		}
		return contaReceberRelVO;
	}

	public SqlRowSet executarConsultaParametrizada(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Date dataInicio, Date dataFim, String tipoPessoa, String alunoMatriula, String alunoNome, String funcionarioMatricula, String funcionarioNome, String candidatoCpf, String candidatoNome, String parceiroCPF, String parceiroCNPJ, String parceiroNome, String situacao, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, ContaCorrenteVO contaCorrente, Integer pessoa, Integer parceiroCodigo, Integer opcaoOrdenacao, Integer centroReceita, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, Integer responsavelFinanceiroCodigo, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean consideraUnidadeEnsinoFinanceira) throws Exception {
		StringBuilder selectStr = executarMontarCabecalhoSqlPrincipal();
		montarRelacionamentoPrincipal(selectStr, dataInicio, dataFim);
		montarFiltrosRelatorio(filtroRelatorioFinanceiroVO, selectStr, dataInicio, dataFim, tipoPessoa, alunoMatriula, alunoNome, funcionarioMatricula, funcionarioNome, candidatoCpf, candidatoNome, parceiroCPF, parceiroCNPJ, parceiroNome, situacao, unidadeEnsinoVOs, unidadeEnsinoCursoVO, turmaVO, contaCorrente, pessoa, parceiroCodigo, centroReceita, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, responsavelFinanceiroCodigo, dataInicioCompetencia, dataFimCompetencia, consideraUnidadeEnsinoFinanceira);
		montarOrdenacaoRelatorio(selectStr, opcaoOrdenacao);
		return getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
	}

	private StringBuilder executarMontarCabecalhoSqlPrincipal() {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT distinct contareceber.codigo AS contareceber_codigo, contareceber.nossonumero AS contareceber_nossonumero, contareceber.tipopessoa AS contareceber_tipopessoa, contareceber.data AS contareceber_data, contareceber.tipoorigem AS contareceber_tipoorigem, ");
		sqlStr.append(" contareceber.situacao AS contareceber_situacao, contareceber.descricaopagamento AS contareceber_descricaopagamento, contareceber.centroreceita AS contareceber_centroreceita, ");
		sqlStr.append(" contareceber.datavencimento AS contareceber_datavencimento, contareceber.valor AS contareceber_valor, contareceber.valorBaseContaReceber::NUMERIC(20,2) AS contareceber_valorBaseContaReceber, contareceber.valorCusteadoContaReceber::NUMERIC(20,2) AS contareceber_valorCusteadoContaReceber, contareceber.valordescontoalunojacalculado::NUMERIC(20,2) AS valordescontoalunojacalculado, ");
		sqlStr.append(" contareceber.descontoinstituicao AS contareceber_valordescontoinstituicao, contareceber.valorCalculadoDescontoLancadoRecebimento AS contareceber_valorCalculadoDescontoLancadoRecebimento, contareceber.descontoconvenio AS contareceber_valordescontoconvenio, ");
		sqlStr.append(" contareceber.valordescontoprogressivo AS contareceber_valordescontoprogressivo, contareceber.valorrecebido::NUMERIC(20,2) AS contareceber_valorrecebido, contareceber.valorrecebercalculado as contareceber_valorrecebercalculado, contareceber.juro::NUMERIC(20,2) AS contareceber_juro, contareceber.juroporcentagem::NUMERIC(20,2) AS contareceber_juroporcentagem, ");
		sqlStr.append(" contareceber.multa AS contareceber_multa, contareceber.acrescimo::NUMERIC(20,2) AS contareceber_acrescimo,  contareceber.valorindicereajusteporatraso::NUMERIC(20,2) AS contareceber_valorindicereajusteporatraso,  contareceber.multaporcentagem::NUMERIC(20,2) AS contareceber_multaporcentagem, contareceber.nrdocumento AS contareceber_nrdocumento, ");
		sqlStr.append(" contareceber.parcela AS contareceber_parcela, contareceber.origemnegociacaoreceber AS contareceber_origemnegociacaoreceber, contareceber.linhaDigitavelCodigoBarras, contareceber.codigobarra, ");
		sqlStr.append(" contareceber.ordemConvenio as \"contareceber.ordemConvenio\", contareceber.OrdemConvenioValorCheio as \"contareceber.OrdemConvenioValorCheio\", contareceber.OrdemDescontoAluno as \"contareceber.OrdemDescontoAluno\", ");
		sqlStr.append(" contareceber.OrdemDescontoAlunoValorCheio as \"contareceber.OrdemDescontoAlunoValorCheio\", contareceber.OrdemDescontoProgressivo as \"contareceber.OrdemDescontoProgressivo\", ");
		sqlStr.append(" contareceber.OrdemDescontoProgressivoValorCheio as \"contareceber.OrdemDescontoProgressivoValorCheio\", contareceber.OrdemPlanoDesconto as \"contareceber.OrdemPlanoDesconto\", ");
		sqlStr.append(" contareceber.OrdemPlanoDescontoValorCheio as \"contareceber.OrdemPlanoDescontoValorCheio\",  contareceber.usaDescontoCompostoPlanoDesconto as \"contareceber.usaDescontoCompostoPlanoDesconto\", ");
		sqlStr.append(" contareceber.valordesconto::NUMERIC(20,2) AS contareceber_desconto, negociacaorecebimento.data AS negociacaorecebimento_data, contareceber.valorcalculadodescontolancadorecebimento::NUMERIC(20,2) as valorcalculadodescontolancadorecebimento,  ");
		sqlStr.append(" pessoa.nome AS pessoa_nome, responsavelFinanceiro.nome as responsavelFinanceiro_nome,  parceiro.nome AS parceiro_nome,  ");
		sqlStr.append(" contacorrente.numero AS contacorrente_numero, contacorrente.digito AS contacorrente_digito, usuario.nome AS usuario_responsavel, ");
		sqlStr.append(" turma.identificadorturma AS \"turma.identificadorturma\", fornecedor.nome as \"fornecedor.nome\", parceiro2.nome as \"parceiro2.nome\", parceiro2.isentarJuro as \"parceiro2.isentarJuro\", parceiro2.isentarMulta as \"parceiro2.isentarMulta\",  ");
		sqlStr.append(" centroreceita.codigo AS \"centroreceita.codigo\", centroreceita.descricao as \"centroreceita.descricao\", centroreceita.identificadorcentroreceita as \"centroreceita.identificadorcentroreceita\", ");
		sqlStr.append(" matricula.matricula as matricula,  matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos::NUMERIC(20,2) AS valordescontocalculadoprimeirafaixadescontos, ");
		sqlStr.append("contareceber.valordescontocalculadoprimeirafaixadescontos as contareceber_valordescontocalculadoprimeirafaixadescontos, ");
		sqlStr.append(" matriculaperiodo.condicaopagamentoplanofinanceirocurso AS matriculaperiodo_condicaopagamentoplanofinanceirocurso, ");
		sqlStr.append(" CASE WHEN contareceber.recebimentobancario is true THEN 'Sim' WHEN contareceber.recebimentobancario is false THEN 'Não' else 'Não Informado' end AS contareceber_recebimentobancario, ");
		sqlStr.append(" CASE WHEN matriculaperiodo.reconheceudivida is null THEN '-' WHEN matriculaperiodo.reconheceudivida is true THEN 'Sim' else 'Não' end AS matriculaperiodo_reconheceudivida, ");
		// dados planodescontocontareceber
		sqlStr.append(" pdcr.codigo as \"pdcr.codigo\", pdcr.contaReceber as \"pdcr.contaReceber\", pdcr.planoDesconto as \"pdcr.planoDesconto\", ");
		sqlStr.append(" pdcr.tipoItemPlanoFinanceiro as \"pdcr.tipoItemPlanoFinanceiro\", pdcr.convenio as \"pdcr.convenio\", ");
		sqlStr.append(" pdcr.valorutilizadorecebimento as \"pdcr.valorutilizadorecebimento\", ");
		// dados planodesconto
		sqlStr.append(" pd.codigo as \"pd.codigo\", pd.nome as \"pd.nome\", pd.percDescontoParcela as \"pd.percDescontoParcela\", ");
		sqlStr.append(" pd.percDescontoMatricula as \"pd.percDescontoMatricula\", pd.requisitos as \"pd.requisitos\", pd.descricao as \"pd.descricao\", ");
		sqlStr.append(" pd.somente1PeriodoLetivoParcela as \"pd.somente1PeriodoLetivoParcela\", pd.somente1PeriodoLetivoMatricula as \"pd.somente1PeriodoLetivoMatricula\", ");
		sqlStr.append(" pd.tipoDescontoParcela as \"pd.tipoDescontoParcela\", pd.tipoDescontoMatricula as \"pd.tipoDescontoMatricula\", ");
		sqlStr.append(" pd.diasValidadeVencimento as \"pd.diasValidadeVencimento\", pd.ativo as \"pd.ativo\", pd.dataAtivacao as \"pd.dataAtivacao\", ");
		sqlStr.append(" pd.dataInativacao as \"pd.dataInativacao\", pd.responsavelAtivacao as \"pd.responsavelAtivacao\", ");
		sqlStr.append(" pd.responsavelInativacao as \"pd.responsavelInativacao\", pd.descontoValidoAteDataVencimento as \"pd.descontoValidoAteDataVencimento\", ");
		sqlStr.append(" pd.aplicarSobreValorCheio as \"pd.aplicarSobreValorCheio\", pd.utilizarDiaUtil as \"pd.utilizarDiaUtil\", ");
		sqlStr.append(" pd.utilizarDiaFixo as \"pd.utilizarDiaFixo\",  pd.utilizarAvancoDiaUtil as \"pd.utilizarAvancoDiaUtil\", ");
		// dados convenio
		sqlStr.append(" c.codigo as \"c.codigo\", c.descricao as \"c.descricao\", c.dataAssinatura as \"c.dataAssinatura\", c.parceiro as \"c.parceiro\", ");
		sqlStr.append(" c.ativo as \"c.ativo\", c.cobertura as \"c.cobertura\", c.preRequisitos as \"c.preRequisitos\", c.dataInicioVigencia as \"c.dataInicioVigencia\", ");
		sqlStr.append(" c.dataFinalVigencia as \"c.dataFinalVigencia\", c.descontoMatricula as \"c.descontoMatricula\", ");
		sqlStr.append(" c.tipoDescontoMatricula as \"c.tipoDescontoMatricula\", c.descontoParcela as \"c.descontoParcela\", ");
		sqlStr.append(" c.tipoDescontoParcela as \"c.tipoDescontoParcela\", c.bolsaCusteadaParceiroMatricula as \"c.bolsaCusteadaParceiroMatricula\", ");
		sqlStr.append(" c.tipoBolsaCusteadaParceiroMatricula as \"c.tipoBolsaCusteadaParceiroMatricula\", ");
		sqlStr.append(" c.bolsaCusteadaParceiroParcela as \"c.bolsaCusteadaParceiroParcela\", c.tipoBolsaCusteadaParceiroParcela as \"c.tipoBolsaCusteadaParceiroParcela\", ");
		sqlStr.append(" c.formaRecebimentoParceiro as \"c.formaRecebimentoParceiro\", c.diaBaseRecebimentoParceiro as \"c.diaBaseRecebimentoParceiro\", ");
		sqlStr.append(" c.requisitante as \"c.requisitante\", c.dataRequisicao as \"c.dataRequisicao\", c.responsavelAutorizacao as \"c.responsavelAutorizacao\", ");
		sqlStr.append(" c.dataAutorizacao as \"c.dataAutorizacao\", c.responsavelFinalizacao as \"c.responsavelFinalizacao\", ");
		sqlStr.append(" c.dataFinalizacao as \"c.dataFinalizacao\", c.situacao as \"c.situacao\", c.validoParaTodoCurso as \"c.validoParaTodoCurso\", ");
		sqlStr.append(" c.validoParaTodaUnidadeEnsino as \"c.validoParaTodaUnidadeEnsino\", c.validoParaTodoTurno as \"c.validoParaTodoTurno\", ");
		sqlStr.append(" c.periodoIndeterminado as \"c.periodoIndeterminado\", c.dataAtivacao as \"c.dataAtivacao\", c.dataInativacao as \"c.dataInativacao\", ");
		sqlStr.append(" c.responsavelAtivacao as \"c.responsavelAtivacao\", c.responsavelInativacao as \"c.responsavelInativacao\", ");
		sqlStr.append(" c.descontoProgressivoParceiro as \"c.descontoProgressivoParceiro\", c.descontoProgressivoAluno as \"c.descontoProgressivoAluno\", ");
		sqlStr.append(" c.possuiDescontoAntecipacao as \"c.possuiDescontoAntecipacao\", c.calculadoEmCimaValorLiquido as \"c.calculadoEmCimaValorLiquido\", ");
		sqlStr.append(" c.aplicarDescontoProgressivoMatricula as \"c.aplicarDescontoProgressivoMatricula\", ");
		sqlStr.append(" c.aplicarDescontoProgressivoMatriculaParceiro as \"c.aplicarDescontoProgressivoMatriculaParceiro\", ");
		// dados descontoprogressivo_contareceber
		sqlStr.append("descontoprogressivo.codigo as \"descontoprogressivo.codigo\", descontoprogressivo.nome as \"descontoprogressivo.nome\", descontoprogressivo.diaLimite1 as \"descontoprogressivo.diaLimite1\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite1 as \"descontoprogressivo.percDescontoLimite1\", descontoprogressivo.diaLimite2 as \"descontoprogressivo.diaLimite2\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite2 as \"descontoprogressivo.percDescontoLimite2\", descontoprogressivo.diaLimite3 as \"descontoprogressivo.diaLimite3\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite3 as \"descontoprogressivo.percDescontoLimite3\", descontoprogressivo.diaLimite4 as \"descontoprogressivo.diaLimite4\", ");
		sqlStr.append("descontoprogressivo.percDescontoLimite4 as \"descontoprogressivo.percDescontoLimite4\", descontoprogressivo.valorDescontoLimite1 as \"descontoprogressivo.valorDescontoLimite1\", ");
		sqlStr.append("descontoprogressivo.valorDescontoLimite2 as \"descontoprogressivo.valorDescontoLimite2\", descontoprogressivo.valorDescontoLimite3 as \"descontoprogressivo.valorDescontoLimite3\", ");
		sqlStr.append("descontoprogressivo.valorDescontoLimite4 as \"descontoprogressivo.valorDescontoLimite4\", descontoprogressivo.ativado as \"descontoprogressivo.ativado\", descontoprogressivo.dataAtivacao as \"descontoprogressivo.dataAtivacao\", ");
		sqlStr.append("descontoprogressivo.responsavelAtivacao as \"descontoprogressivo.responsavelAtivacao\", descontoprogressivo.dataInativacao as \"descontoprogressivo.dataInativacao\", ");
		sqlStr.append(" descontoprogressivo.utilizarDiaFixo as \"descontoprogressivo.utilizarDiaFixo\", descontoprogressivo.utilizarDiaUtil as \"descontoprogressivo.utilizarDiaUtil\", ");
		// dados descontoprogressivoaluno
		sqlStr.append(" dpa.codigo as \"dpa.codigo\", dpa.nome as \"dpa.nome\", dpa.diaLimite1 as \"dpa.diaLimite1\", ");
		sqlStr.append(" dpa.percDescontoLimite1 as \"dpa.percDescontoLimite1\", dpa.diaLimite2 as \"dpa.diaLimite2\", ");
		sqlStr.append(" dpa.percDescontoLimite2 as \"dpa.percDescontoLimite2\", dpa.diaLimite3 as \"dpa.diaLimite3\", ");
		sqlStr.append(" dpa.percDescontoLimite3 as \"dpa.percDescontoLimite3\", dpa.diaLimite4 as \"dpa.diaLimite4\", ");
		sqlStr.append(" dpa.percDescontoLimite4 as \"dpa.percDescontoLimite4\", dpa.valorDescontoLimite1 as \"dpa.valorDescontoLimite1\", ");
		sqlStr.append(" dpa.valorDescontoLimite2 as \"dpa.valorDescontoLimite2\", dpa.valorDescontoLimite3 as \"dpa.valorDescontoLimite3\", ");
		sqlStr.append(" dpa.valorDescontoLimite4 as \"dpa.valorDescontoLimite4\", dpa.ativado as \"dpa.ativado\", dpa.dataAtivacao as \"dpa.dataAtivacao\", ");
		sqlStr.append(" dpa.responsavelAtivacao as \"dpa.responsavelAtivacao\", dpa.dataInativacao as \"dpa.dataInativacao\", ");
		sqlStr.append(" dpa.responsavelInativacao as \"dpa.responsavelInativacao\", ");
		sqlStr.append(" dpa.utilizarDiaFixo as \"dpa.utilizarDiaFixo\", dpa.utilizarDiaUtil as \"dpa.utilizarDiaUtil\", ");
		// dados descontoprogressivoparceiro
		sqlStr.append(" dpp.codigo as \"dpp.codigo\", dpp.nome as \"dpp.nome\", dpp.diaLimite1 as \"dpp.diaLimite1\", ");
		sqlStr.append(" dpp.percDescontoLimite1 as \"dpp.percDescontoLimite1\", dpp.diaLimite2 as \"dpp.diaLimite2\", ");
		sqlStr.append(" dpp.percDescontoLimite2 as \"dpp.percDescontoLimite2\", dpp.diaLimite3 as \"dpp.diaLimite3\", ");
		sqlStr.append(" dpp.percDescontoLimite3 as \"dpp.percDescontoLimite3\", dpp.diaLimite4 as \"dpp.diaLimite4\", ");
		sqlStr.append(" dpp.percDescontoLimite4 as \"dpp.percDescontoLimite4\", dpp.valorDescontoLimite1 as \"dpp.valorDescontoLimite1\", ");
		sqlStr.append(" dpp.valorDescontoLimite2 as \"dpp.valorDescontoLimite2\", dpp.valorDescontoLimite3 as \"dpp.valorDescontoLimite3\", ");
		sqlStr.append(" dpp.valorDescontoLimite4 as \"dpp.valorDescontoLimite4\", dpp.ativado as \"dpp.ativado\", dpp.dataAtivacao as \"dpp.dataAtivacao\", ");
		sqlStr.append(" dpp.responsavelAtivacao as \"dpp.responsavelAtivacao\", dpp.dataInativacao as \"dpp.dataInativacao\", ");
		sqlStr.append(" dpp.responsavelInativacao as \"dpp.responsavelInativacao\", ");
		sqlStr.append(" dpp.utilizarDiaFixo as \"dpp.utilizarDiaFixo\", dpp.utilizarDiaUtil as \"dpp.utilizarDiaUtil\", ");
		sqlStr.append(" contareceber.dataprocessamentovalorreceber as \"cr.dataprocessamentovalorreceber\", contareceber.valorrecebercalculado as \"cr.valorrecebercalculado\", ");
		sqlStr.append(" contareceber.valorjurocalculado as \"cr.valorjurocalculado\", contareceber.valormultacalculado as \"cr.valormultacalculado\", ");
		sqlStr.append(" contareceber.valordescontocalculado as \"cr.valordescontocalculado\", ");
		sqlStr.append(" contareceber.valordescontorateio as \"cr.valordescontorateio\" ");
		return sqlStr;
	}

	private String executarMontarCabecalhoSqlSubRelatorioQuadroFormaPagamento() {
		return "SELECT formapagamento.codigo AS contareceber_codigoFormaPagamento, formapagamento.nome AS contaReceber_nomeFormaPagamento, " + "SUM(TRUNC(contareceber.valor::NUMERIC,2)) AS contareceber_valor, " + "SUM(case when (contareceber.situacao = 'AR') then contareceber.valorReceberCalculado else 0.0 end) AS contareceber_valorReceberCalculado, " + "SUM(case when (contareceber.situacao = 'RE') then contareceber.valorrecebido else 0.0 end) AS contareceber_valorrecebido, " + "SUM(TRUNC(contareceber.valordescontoalunojacalculado::NUMERIC,2)) AS valordescontoalunojacalculado,   " + "SUM(TRUNC(contareceber.valordescontoprogressivo::NUMERIC,2)) AS valordescontoprogressivo,  " + "SUM(TRUNC(contareceber.descontoconvenio::NUMERIC,2)) AS descontoconvenio, " + "SUM(TRUNC(contareceber.descontoinstituicao::NUMERIC,2)) AS descontoinstituicao,  " + "SUM(TRUNC(contareceber.valorCalculadoDescontoLancadoRecebimento::NUMERIC,2)) AS valorCalculadoDescontoLancadoRecebimento, sum(contareceber.valorDescontoRateio) as valorDescontoRateio,  "
				+ "SUM(TRUNC(contareceber.juro::NUMERIC,2)) AS contareceber_juro, " + "SUM(TRUNC(contareceber.multa::NUMERIC,2)) AS contareceber_multa ";
	}

	private void montarRelacionamentoPrincipal(StringBuilder selectStr, Date dataInicio, Date dataFim) {
		selectStr.append("FROM contareceber  LEFT JOIN convenio on (contareceber.convenio = convenio.codigo) ");
		selectStr.append("LEFT JOIN parceiro on (convenio.parceiro = parceiro.codigo) ");
		// selectStr.append("LEFT JOIN matriculaperiodo on (contareceber.matriculaperiodo = matriculaperiodo.codigo) ");
		selectStr.append("left JOIN contarecebernegociacaorecebimento on  contarecebernegociacaorecebimento.codigo =  (select contarecebernegociacaorecebimento.codigo from contarecebernegociacaorecebimento ");
		selectStr.append("INNER JOIN negociacaorecebimento as n ON (n.codigo = contarecebernegociacaorecebimento.negociacaorecebimento)  ");
		selectStr.append("where contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		if (dataInicio != null) {
			selectStr.append("and ( n.data::Date >= '");
			selectStr.append(dataInicio);
			selectStr.append("'::DATE)");
		}
		if (dataFim != null) {
			selectStr.append("AND ( n.data::Date <= '");
			selectStr.append(dataFim);
			selectStr.append("'::DATE) ");
		}
		selectStr.append("order by  contarecebernegociacaorecebimento.codigo desc limit 1) ");
		selectStr.append(" left JOIN negociacaorecebimento ON (negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento) ");
		selectStr.append("left JOIN formapagamentonegociacaorecebimento ON (negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaorecebimento) ");
		selectStr.append(" LEFT JOIN pessoa ON (contareceber.pessoa = pessoa.codigo) ");
		selectStr.append(" LEFT JOIN pessoa responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo) ");
		selectStr.append(" LEFT JOIN contacorrente ON (contareceber.contacorrente = contacorrente.codigo) ");
		selectStr.append(" LEFT JOIN usuario ON negociacaorecebimento.responsavel = usuario.codigo ");
		selectStr.append(" LEFT JOIN matricula ON contareceber.matriculaaluno = matricula.matricula ");
		selectStr.append(" LEFT join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end ");
		selectStr.append(" left join turma on turma.codigo = matriculaperiodo.turma ");
		selectStr.append(" LEFT JOIN fornecedor ON (contareceber.fornecedor = fornecedor.codigo) ");
		selectStr.append(" LEFT JOIN parceiro parceiro2 on (contaReceber.parceiro = parceiro2.codigo) ");
		selectStr.append(" LEFT JOIN matriculaperiodovencimento ON matriculaperiodovencimento.contareceber = contareceber.codigo ");
		selectStr.append(" LEFT JOIN centroreceita ON centroreceita.codigo = contareceber.centroreceita ");
		selectStr.append(" LEFT JOIN PlanoDescontoContaReceber pdcr ON pdcr.contareceber = contareceber.codigo ");
		selectStr.append(" LEFT JOIN PlanoDesconto pd ON pd.codigo = pdcr.planodesconto ");
		selectStr.append(" LEFT JOIN Convenio c ON c.codigo = pdcr.convenio ");
		selectStr.append(" LEFT JOIN DescontoProgressivo dpa ON dpa.codigo = c.descontoprogressivoaluno ");
		selectStr.append(" LEFT JOIN DescontoProgressivo dpp ON dpp.codigo = c.descontoprogressivoparceiro ");
		selectStr.append(" left join descontoprogressivo on descontoprogressivo.codigo = contareceber.descontoprogressivo ");
	}

	private void montarRelacionamentoFormaRecebimento(StringBuilder selectStr) {
		selectStr.append("FROM contareceber LEFT JOIN pessoa on (contareceber.pessoa = pessoa.codigo) LEFT JOIN convenio on (contareceber.convenio = convenio.codigo) ");
		selectStr.append("LEFT JOIN parceiro on(convenio.parceiro = parceiro.codigo) ");
		selectStr.append("LEFT JOIN contarecebernegociacaorecebimento on(contarecebernegociacaorecebimento.contareceber = contareceber.codigo) ");
		selectStr.append("LEFT JOIN negociacaorecebimento ON (negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento) ");
		selectStr.append("LEFT JOIN formapagamentonegociacaorecebimento ON (formapagamentonegociacaorecebimento.negociacaoRecebimento = negociacaorecebimento.codigo) ");
		selectStr.append("LEFT JOIN formapagamento ON (formapagamentonegociacaorecebimento.formapagamento = formapagamento.codigo) ");
		selectStr.append("LEFT JOIN contacorrente on contacorrente.codigo = contareceber.contacorrente ");
		selectStr.append("LEFT JOIN usuario ON negociacaorecebimento.responsavel = usuario.codigo ");
		selectStr.append("LEFT JOIN matricula ON contareceber.matriculaaluno = matricula.matricula ");
		selectStr.append(" LEFT join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end ");
		selectStr.append(" LEFT JOIN matriculaperiodovencimento ON matriculaperiodovencimento.contareceber = contareceber.codigo ");
		selectStr.append(" LEFT JOIN pessoa responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo) ");
		selectStr.append(" LEFT JOIN fornecedor ON (contareceber.fornecedor = fornecedor.codigo) ");
		selectStr.append(" LEFT JOIN parceiro parceiro2 ON (contareceber.parceiro = parceiro2.codigo) ");
	}

	private void montarFiltrosRelatorio(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, StringBuilder selectStr, Date dataInicio, Date dataFim, String tipoPessoa, String alunoMatriula, String alunoNome, String funcionarioMatricula, String funcionarioNome, String candidatoCpf, String candidatoNome, String parceiroCPF, String parceiroCNPJ, String parceiroNome, String situacao, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, ContaCorrenteVO contaCorrente, Integer pessoa, Integer parceiroCodigo, Integer centroReceita, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, Integer responsavelFinanceiroCodigo, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean consideraUnidadeEnsinoFinanceira) {
		String filtros = "";
		String where = " WHERE ";
		if (!situacao.equals("")) {
			if ((tipoPessoa != null) && (!tipoPessoa.equals(""))) {
				if (tipoPessoa.equals("AL")) {
					selectStr.append(where).append("( contareceber.tipoPessoa in('").append(TipoPessoa.ALUNO.getValor()).append("', '").append(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()).append("' ))");
				} else {
					selectStr.append(where).append("( contareceber.tipoPessoa = '").append(tipoPessoa).append("')");
				}
				where = " AND ";
			}
		}
		adicionarFiltroTipoOrigem(filtroRelatorioFinanceiroVO, selectStr, where);
		where = " AND ";
		if (tipoPessoa.equals(TipoPessoa.CANDIDATO.getValor()) && (!candidatoCpf.equals("")) && (pessoa != 0)) {
			selectStr.append(where).append("(contareceber.pessoa = '").append(pessoa.intValue()).append(" ')");
			where = " AND ";
		}
		if ((tipoPessoa.equals(TipoPessoa.ALUNO.getValor()) || tipoPessoa.equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) && (!alunoMatriula.equals("")) && (pessoa.intValue() != 0)) {
			selectStr.append(where).append("(contareceber.pessoa = '").append(pessoa.intValue()).append(" ')");
			where = " AND ";
		}
		if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO.getValor()) && (pessoa.intValue() != 0)) {
			selectStr.append(where).append("(contareceber.pessoa = '").append(pessoa.intValue()).append(" ')");
			where = " AND ";
		}
		if (tipoPessoa.equals(TipoPessoa.PARCEIRO.getValor()) && (parceiroCodigo != 0)) {
			selectStr.append(where).append("(( parceiro2.codigo = '").append(parceiroCodigo).append("') or ");
			selectStr.append("( parceiro.codigo = '").append(parceiroCodigo).append("'))");
			where = " AND ";
		}
		if (tipoPessoa.equals(TipoPessoa.FORNECEDOR.getValor()) && fornecedor != null && fornecedor > 0) {
			selectStr.append(where).append("( fornecedor.codigo = '").append(fornecedor).append("')");
			where = " AND ";
		}
		if (tipoPessoa.equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()) && responsavelFinanceiroCodigo != null && responsavelFinanceiroCodigo > 0) {
			selectStr.append(where).append("( responsavelFinanceiro.codigo = '").append(responsavelFinanceiroCodigo).append("')");
			where = " AND ";
		}
		if (dataInicioCompetencia != null) {
			selectStr.append(where).append("( contareceber.datacompetencia::DATE >= '").append(Uteis.getDataJDBC(dataInicioCompetencia)).append("'::DATE)");
			where = " AND ";
		}
		if (dataFimCompetencia != null) {
			selectStr.append(where).append("( contareceber.datacompetencia::DATE <= '").append(Uteis.getDataJDBC(dataFimCompetencia)).append("'::DATE)");
			where = " AND ";
		}
			if (situacao.equals("AR") || situacao.equals("REdoMes") || situacao.equals("NE") || situacao.equals("AR/REdoMes")) {
				if (dataInicio != null) {
					selectStr.append(where).append("( contareceber.datavencimento::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE)");
					where = " AND ";
				}
				if (dataFim != null) {
					selectStr.append(where).append("( contareceber.datavencimento::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("'::DATE)");
					where = " AND ";
				}
			} else if (situacao.equals("REnoMes")) {
				if (dataInicio != null) {
					selectStr.append(where).append("( negociacaorecebimento.data::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE)");
					where = " AND ";
				}
				if (dataFim != null) {
					selectStr.append(where).append("( negociacaorecebimento.data::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("'::DATE)");
					where = " AND ";
				}
			}
		if ((situacao != null) && (!situacao.equals(""))) {
			if (situacao.equals("REnoMes") || situacao.equals("REdoMes")) {
				filtros = adicionarCondicionalWhere(filtros, "( contareceber.situacao  = 'RE')", true);
			} else if (situacao.equals("AR/REdoMes")) {
				filtros = adicionarCondicionalWhere(filtros, "( contareceber.situacao in('AR', 'RE'))", true);
			} else {
				filtros = adicionarCondicionalWhere(filtros, "( contareceber.situacao  = '" + situacao + "')", true);
			}
			adicionarDescricaoFiltro("contarecebersituacao = " + situacao);
		}
		if (!unidadeEnsinoVOs.isEmpty()) {
			
			if(consideraUnidadeEnsinoFinanceira)
				selectStr.append(" and contareceber.unidadeensinofinanceira in (");
			else
				selectStr.append(" and contareceber.unidadeensino in (");
				
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (x > 0) {
						selectStr.append(", ");
					}
					selectStr.append(unidadeEnsinoVO.getCodigo());
					x++;
				}
			}
			selectStr.append(" ) ");
		}
		if ((contaCorrente != null) && (contaCorrente.getCodigo().intValue() != 0)) {
			selectStr.append(where).append("( contareceber.situacao != 'RE' and contacorrente.codigo = ").append(contaCorrente.getCodigo().intValue());
			selectStr.append(" or (contareceber.situacao = 'RE' and (formapagamentonegociacaorecebimento.contacorrente = ").append(contaCorrente.getCodigo().intValue());
			selectStr.append(" or formapagamentonegociacaorecebimento.contacorrenteoperadoracartao = ").append(contaCorrente.getCodigo().intValue());
			selectStr.append(" ))) ");
			where = " and ";
		}

		if ((centroReceita != null) && (centroReceita != 0)) {
			selectStr.append(where).append("contareceber.centroreceita = ").append(centroReceita).append(" ");
			where = " and ";
		}
		if ((condicaoPagamentoPlanoFinanceiroCurso != null) && (condicaoPagamentoPlanoFinanceiroCurso != 0)) {
			selectStr.append(where).append("matriculaperiodo.condicaopagamentoplanofinanceirocurso = ").append(condicaoPagamentoPlanoFinanceiroCurso).append(" ");
			where = " and ";
		}

		if (unidadeEnsinoCursoVO != null && unidadeEnsinoCursoVO.getCurso() != null && unidadeEnsinoCursoVO.getCurso().getCodigo() != 0) {
			selectStr.append(where).append(" matricula.curso = ").append(unidadeEnsinoCursoVO.getCurso().getCodigo()).append(" ");
		}
		if (turmaVO != null && turmaVO.getCodigo() != 0) {
			selectStr.append(where).append(" matriculaPeriodo.turma = ").append(turmaVO.getCodigo()).append(" ");
		}
		selectStr.append(filtros);
	}

	protected void montarOrdenacaoRelatorio(StringBuilder selectStr, Integer opcaoOrdenacao) {
		String ordenacao = (String) getOrdenacoesRelatorio().get(opcaoOrdenacao);
		if (ordenacao.equals("Nome")) {
			ordenacao = "pessoa.nome, parceiro2.nome,  fornecedor.nome, contareceber_datavencimento, contareceber.parcela, contareceber.codigo";
		}
		if (ordenacao.equals("Data")) {
			ordenacao = "contareceber_datavencimento, contareceber.parcela, contareceber.codigo";
		}
		if (ordenacao.equals("Tipo de Origem")) {
			ordenacao = "contareceber.tipoorigem,contareceber_datavencimento, contareceber.parcela, contareceber.codigo";
		}
		if (ordenacao.equals("Situação")) {
			ordenacao = "contareceber.situacao,contareceber_datavencimento, contareceber.parcela, contareceber.codigo";
		}
		if (ordenacao.equals("Centro de Receita")) {
			ordenacao = "centroreceita.identificadorCentroReceita, contareceber_datavencimento, contareceber.parcela, contareceber.codigo";
		}
		if (ordenacao.equals("Turma")) {
			ordenacao = "Turma.identificadorTurma, contareceber_datavencimento, pessoa.nome, parceiro2.nome,  fornecedor.nome, contareceber.parcela, contareceber.codigo";
		}
		if (!ordenacao.equals("")) {
			selectStr.append(" ORDER BY ").append(ordenacao);
		}
	}

	@Override
	public String designIReportRelatorio(String tipoLayout) {
		if (tipoLayout.equals("layout1")) {
			return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
		} else if (tipoLayout.equals("layout2")) {
			return (caminhoBaseRelatorio() + getIdEntidadeLayout2() + ".jrxml");
		} else if (tipoLayout.equals("layout4")) {
			return (caminhoBaseRelatorio() + getIdEntidadeLayout4() + ".jrxml");
		} else if (tipoLayout.equals("layout5")) {
			return (caminhoBaseRelatorio() + "ContaReceberPorTurmaRel.jrxml");
		} else {
			return (caminhoBaseRelatorio() + getIdEntidadeLayout3() + ".jrxml");
		}
	}

	public String designIReportRelatorioExcel(String tipoLayout) {
		if (tipoLayout.equals("layout1")) {
			return (caminhoBaseRelatorio() + getIdEntidadeExcel() + ".jrxml");
		} else if (tipoLayout.equals("layout2")) {
			return (caminhoBaseRelatorio() + getIdEntidadeExcelLayout2() + ".jrxml");
		} else if (tipoLayout.equals("layout5")) {
			return (caminhoBaseRelatorio() + "ContaReceberPorTurmaExcelRel.jrxml");
		} else {
			return (caminhoBaseRelatorio() + getIdEntidadeLayout3() + ".jrxml");
		}
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return "ContaReceberRel";
	}

	public static String getIdEntidadeExcel() {
		return "ContaReceberExcelRel";
	}

	public static String getIdEntidadeLayout2() {
		return "ContaReceberLayout2Rel";
	}

	public static String getIdEntidadeLayout3() {
		return "ContaReceberCentroReceitaRel";
	}

	public static String getIdEntidadeLayout4() {
		return "ContaReceberDetalhamentoDescontoRel";
	}

	public static String getIdEntidadeExcelLayout2() {
		return "ContaReceberExcelLayout2Rel";
	}

	public void inicializarOrdenacoesRelatorio() {
		Vector ordenacao = this.getOrdenacoesRelatorio();
		ordenacao.add("Nome");
		ordenacao.add("Data");
		ordenacao.add("Tipo de Origem");
		ordenacao.add("Centro de Receita");
		ordenacao.add("Turma");
	}

	public void adicionarFiltroTipoOrigem(FiltroRelatorioFinanceiroVO obj, StringBuilder sqlStr, String where) {
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
		if (str.length() > 0) {
			sqlStr.append(where).append(" contaReceber.tipoOrigem in (''").append(str);
			sqlStr.append(" ) ");
		}
	}

}