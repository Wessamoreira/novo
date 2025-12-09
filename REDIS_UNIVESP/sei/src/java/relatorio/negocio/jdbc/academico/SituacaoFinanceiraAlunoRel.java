package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.RegistroNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DadosSituacaoContaPagarVO;
import relatorio.negocio.comuns.academico.DadosSituacaoContaReceberVO;
import relatorio.negocio.comuns.academico.SituacaoFinanceiraAlunoRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.academico.SituacaoFinanceiraAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class SituacaoFinanceiraAlunoRel extends SuperRelatorio implements SituacaoFinanceiraAlunoRelInterfaceFacade {

	public static void validarDados(MatriculaVO obj, String tipoPessoa, PessoaVO responsavelFinanceiro) throws ConsistirException {
		if (!tipoPessoa.equals("responsavelFinanceiro") && obj.getMatricula().equals("")) {			
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SituacaoFinanceiraAluno_matricula"));
		}
		if (!tipoPessoa.equals("responsavelFinanceiro") && obj.getMatricula().equals("")) {
			throw new ConsistirException("A Matrícula deve ser informada para a geração do relatório.");
		}
		if (tipoPessoa.equals("responsavelFinanceiro") && (responsavelFinanceiro == null || responsavelFinanceiro.getCodigo().intValue() == 0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_SituacaoFinanceiraAluno_responsavelFinanceiro"));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.SituacaoFinanceiraAlunoRelInterfaceFacade
	 * #criarObjeto(negocio.comuns.academico .MatriculaVO)
	 */
	public List<SituacaoFinanceiraAlunoRelVO> criarObjeto(MatriculaVO matriculaVO, SituacaoFinanceiraAlunoRelVO situacaoFinanceiraAlunoRelVO, String tipoPessoa, ParceiroVO parceiroVO, UsuarioVO usuarioVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PessoaVO responsavelFinanceiro, String ano, String semestre, Date dataInicio, Date dataFim, Boolean filtrarPorDataCompetencia, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, CentroReceitaVO centroReceitaVO, String tipoLayout) throws Exception {
		List<SituacaoFinanceiraAlunoRelVO> listaResultado = new ArrayList<SituacaoFinanceiraAlunoRelVO>(0);          
		if (filtrarPorDataCompetencia) {
			if(dataInicio != null) {
				dataInicio = Uteis.getDataPrimeiroDiaMes(dataInicio);
			}
			if(dataFim != null) {
				dataFim = Uteis.getDataUltimoDiaMes(dataFim);
			}
		}
		situacaoFinanceiraAlunoRelVO.getContaReceberVOs().clear();
		situacaoFinanceiraAlunoRelVO.getContaPagarVOs().clear();
		situacaoFinanceiraAlunoRelVO.setContaReceberVOs(montarListaContaReceber(matriculaVO, listaUnidadeEnsinoVOs, tipoPessoa, parceiroVO, usuarioVO, configuracaoFinanceiroVO, responsavelFinanceiro, ano, semestre, dataInicio, dataFim, filtrarPorDataCompetencia, filtroRelatorioFinanceiroVO, centroReceitaVO, tipoLayout));
		situacaoFinanceiraAlunoRelVO.setContaPagarVOs(montarListaContaPagar(matriculaVO.getMatricula(), listaUnidadeEnsinoVOs, usuarioVO, configuracaoFinanceiroVO));
		situacaoFinanceiraAlunoRelVO.setNomesAgentesNegativacaoCobrancaContaReceberVO(montarNomesAgentesNegativacaoCobrancaContaReceberVO(matriculaVO.getMatricula() , dataInicio, dataFim,usuarioVO));
		situacaoFinanceiraAlunoRelVO.setApresentarContaPagar(!situacaoFinanceiraAlunoRelVO.getContaPagarVOs().isEmpty());
		if (!tipoPessoa.equals("responsavelFinanceiro")) {
			situacaoFinanceiraAlunoRelVO.setAluno(matriculaVO.getAluno().getNome());
			situacaoFinanceiraAlunoRelVO.setCurso(matriculaVO.getCurso().getNome());
			situacaoFinanceiraAlunoRelVO.setTelefone(matriculaVO.getAluno().getTelefoneRes());
			situacaoFinanceiraAlunoRelVO.setCelular(matriculaVO.getAluno().getCelular());
			situacaoFinanceiraAlunoRelVO.setCpf(matriculaVO.getAluno().getCPF());
			situacaoFinanceiraAlunoRelVO.setMatricula(matriculaVO.getMatricula());
			situacaoFinanceiraAlunoRelVO.setSituacaoMatricula(matriculaVO.getSituacao_Apresentar());
			if (!ano.equals("") && !semestre.equals("")) {
				situacaoFinanceiraAlunoRelVO.setAnoSemestre(ano + "/" + semestre);
			}
			MatriculaPeriodoVO mat = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatriculaConsultaBasica(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
			getFacadeFactory().getTurmaFacade().carregarDados(mat.getTurma(), usuarioVO);
			situacaoFinanceiraAlunoRelVO.setTurma(mat.getTurma().getIdentificadorTurma());
			situacaoFinanceiraAlunoRelVO.setCnpjEmpresa(matriculaVO.getUnidadeEnsino().getCNPJ());
			situacaoFinanceiraAlunoRelVO.setNomeEmpresa(matriculaVO.getUnidadeEnsino().getRazaoSocial());
			PessoaVO responsavelFinanceiroAluno = getFacadeFactory().getPessoaFacade().consultarResponsavelFinanceiroAluno(matriculaVO.getAluno().getCodigo(), usuarioVO);
			if (Uteis.isAtributoPreenchido(responsavelFinanceiroAluno)) {
				situacaoFinanceiraAlunoRelVO.setNomeResponsavelFinanceiro(responsavelFinanceiroAluno.getNome());
			}
		} else {
			if (Uteis.isAtributoPreenchido(responsavelFinanceiro)) {
				responsavelFinanceiro.setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getPessoaFacade().carregarDados(responsavelFinanceiro, NivelMontarDados.BASICO, usuarioVO);
				List<MatriculaVO> matriculasVinculadasResponsavelFinanceiro = getFacadeFactory().getMatriculaFacade().consultaRapidaBasicaPorCodigoResponsavelFinanceiro(responsavelFinanceiro.getCodigo(), false, usuarioVO);
				situacaoFinanceiraAlunoRelVO.setNomeEmpresa(matriculasVinculadasResponsavelFinanceiro.stream().map(MatriculaVO::getUnidadeEnsino).map(UnidadeEnsinoVO::getRazaoSocial).findFirst().orElse(""));
				situacaoFinanceiraAlunoRelVO.setCnpjEmpresa(matriculasVinculadasResponsavelFinanceiro.stream().map(MatriculaVO::getUnidadeEnsino).map(UnidadeEnsinoVO::getCNPJ).findFirst().orElse(""));
			}
			situacaoFinanceiraAlunoRelVO.setAluno(responsavelFinanceiro.getNome());
			situacaoFinanceiraAlunoRelVO.setCurso("");
			situacaoFinanceiraAlunoRelVO.setTelefone(responsavelFinanceiro.getTelefoneRes());
			situacaoFinanceiraAlunoRelVO.setCelular(responsavelFinanceiro.getCelular());
			situacaoFinanceiraAlunoRelVO.setCpf(responsavelFinanceiro.getCPF());
			situacaoFinanceiraAlunoRelVO.setMatricula("");
			situacaoFinanceiraAlunoRelVO.setSituacaoMatricula("");
		}
		situacaoFinanceiraAlunoRelVO.setValorDevidoTotal(somarValorDevidoTotal(situacaoFinanceiraAlunoRelVO.getContaReceberVOs()));

		for (DadosSituacaoContaReceberVO obj  : situacaoFinanceiraAlunoRelVO.getContaReceberVOs()) {
			if (obj.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
				situacaoFinanceiraAlunoRelVO.setQuite(true);
			} else if (obj.getDataVencimento().after(new Date())) {
				situacaoFinanceiraAlunoRelVO.setParcelaAVencer(true);
				situacaoFinanceiraAlunoRelVO.setQuite(false);
			} else if (obj.getDataVencimento().before(new Date()) && (!obj.getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor()) || obj.getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor()))) {
				situacaoFinanceiraAlunoRelVO.setParcelaEmAberto(true);
				situacaoFinanceiraAlunoRelVO.setQuite(false);
			}
		}
		
		listaResultado.add(situacaoFinanceiraAlunoRelVO);
		return listaResultado;
	}

	private String montarNomesAgentesNegativacaoCobrancaContaReceberVO(String matricula ,Date dataInicio, Date dataFim, UsuarioVO usuarioVO) throws Exception {	
		String nomesAgentes = getFacadeFactory().getRegistroNegativacaoCobrancaContaReceberFacade()
				.consultarPorMatricula(dataInicio, dataFim, 0, matricula,true, false, usuarioVO).stream()
				.map(RegistroNegativacaoCobrancaContaReceberVO::getAgente)
				.map(AgenteNegativacaoCobrancaContaReceberVO::getNome).distinct().collect(Collectors.joining(" , "));
		 if(!Uteis.isAtributoPreenchido(nomesAgentes)){
			 return  "" ;
		 }		
		return nomesAgentes;
		
		
	}

	private double somarValorDevidoTotal(List<DadosSituacaoContaReceberVO> lista) {
		double total = 0;
		for (DadosSituacaoContaReceberVO dadosSituacaoContaReceberVO : lista) {
			if (dadosSituacaoContaReceberVO.getDataVencimento().before(new Date()) && dadosSituacaoContaReceberVO.getSituacao().equals("A Receber")) {
				total += dadosSituacaoContaReceberVO.getValorReceberCalculado();
			}
		}
		return Uteis.arredondar(total, 2, 0);
	}

	private List<DadosSituacaoContaReceberVO> montarListaContaReceber(MatriculaVO matriculaVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, String tipoPessoa, ParceiroVO parceiroVO, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, PessoaVO responsavelFinanceiro, String ano, String semestre, Date dataInicio, Date dataFim, Boolean filtrarPorDataCompetencia, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, CentroReceitaVO centroReceitaVO, String tipoLayout) throws Exception {
		StringBuilder sql = new StringBuilder("select DISTINCT contareceber.codigo, contareceber.tipoorigem,contareceber.nossonumero, ");
		sql.append(" contareceber.datavencimento, matriculaPeriodo.ano as matriculaPeriodo_ano, matriculaPeriodo.semestre as matriculaPeriodo_semestre, ");
		sql.append(" contareceber.datavencimento, ");
		sql.append(" case when contareceber.situacao = 'AR' then 0.00 else contareceber.valorrecebido end as valorrecebido, ");
		sql.append(" case when contareceber.situacao = 'AR' then contareceber.valorrecebercalculado else 0.0 end as valorreceber, ");
		sql.append(" contareceber.valor as valor, ");
		sql.append(" contareceber.juro as juro, ");
		sql.append(" contareceber.valorjurocalculado as valorjurocalculado, ");
		sql.append(" contareceber.valormultacalculado as valormultacalculado, ");
		sql.append(" contareceber.multa, ");
		sql.append(" contareceber.acrescimo as acrescimo, ");
		sql.append(" contareceber.valorIndiceReajustePorAtraso as valorIndiceReajustePorAtraso, ");
		sql.append(" contareceber.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa as valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa, ");
		sql.append(" contareceber.valorDescontoAlunoJaCalculado, ");
		sql.append(" contareceber.descontoconvenio as valorDescontoConvenio, ");
		sql.append(" contareceber.descontoinstituicao as valorDescontoInstituicao, ");
		sql.append(" contareceber.valorDescontoProgressivo, ");
		sql.append(" contareceber.valorDescontoRateio, ");
		sql.append(" contareceber.valorCalculadoDescontoLancadoRecebimento, ");
		sql.append(" contareceber.valorDescontoCalculadoPrimeiraFaixaDescontos, ");
		sql.append(" contareceber.valorDescontoCalculadoSegundaFaixaDescontos, ");
		sql.append(" contareceber.valorDescontoCalculadoTerceiraFaixaDescontos, ");
		sql.append(" contareceber.valorDescontoCalculadoQuartaFaixaDescontos, ");
		sql.append(" contareceber.situacao, ");
		sql.append(" contareceber.parcela, ");
		sql.append(" centroReceita.descricao AS descricaoCentroReceita, ");
		sql.append(" contareceber.dataprocessamentovalorreceber AS dataprocessamentovalorreceber, ");
		sql.append(" case when contareceber.situacao = 'AR' then null else negociacaorecebimento.data end as datapagamento ");
		sql.append(" from contareceber ");
		sql.append(" left join contarecebernegociacaorecebimento on (contareceber.codigo = contarecebernegociacaorecebimento.contareceber) ");
		sql.append(" and contarecebernegociacaorecebimento.codigo = (select contarecebernegociacaorecebimento.codigo from contarecebernegociacaorecebimento where contarecebernegociacaorecebimento.contareceber = contareceber.codigo order by contarecebernegociacaorecebimento.codigo desc limit 1)");
		sql.append(" left join negociacaorecebimento on (contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo) ");
		sql.append(" left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		sql.append(" left join centroReceita on centroReceita.codigo = contaReceber.centroReceita ");
		if (tipoPessoa.equals("parceiro")) {
			sql.append(" left join convenio on convenio.codigo = contareceber.convenio ");
		}
		if (tipoPessoa.equals("responsavelFinanceiro")) {
			sql.append(" left join Pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contareceber.responsavelFinanceiro ");
		}
		
		adicionarFiltro(sql, tipoPessoa, matriculaVO, listaUnidadeEnsinoVOs, parceiroVO, responsavelFinanceiro, ano, semestre, filtrarPorDataCompetencia, dataInicio, dataFim, filtroRelatorioFinanceiroVO, centroReceitaVO);
		if(tipoLayout.equals("SituacaoFinanceiraAlunoRelLayout1")){
			sql.append(" ORDER BY centroReceita.descricao, contareceber.datavencimento");	
		}else{
			sql.append(" ORDER BY contareceber.datavencimento");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarLista(tabelaResultado, usuarioVO, configuracaoFinanceiroVO);
	}

	private List<DadosSituacaoContaPagarVO> montarListaContaPagar(String matricula, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT contapagar.codigo, contapagar.dataVencimento, contapagar.parcela, contapagar.nrDocumento,  ");
		sql.append("CASE WHEN (contapagar.situacao = 'AP') THEN 'A pagar' ELSE  ");
		sql.append("CASE WHEN (contapagar.situacao = 'PP') THEN 'Pago Parcial' ELSE ");
		sql.append("CASE WHEN (contapagar.situacao = 'NE') THEN 'Negociado' ELSE ");
		sql.append("CASE WHEN (contapagar.situacao = 'PA') THEN 'Pago' END END END END AS situacao,  ");
		sql.append("TRUNC(contapagar.valor::NUMERIC, 2) AS valor, TRUNC(contapagar.juro::NUMERIC, 2) AS juro, TRUNC(contapagar.multa::NUMERIC, 2) AS multa, ");
		sql.append("TRUNC(contapagar.desconto::NUMERIC, 2) AS desconto, TRUNC(contapagar.valorPago::NUMERIC, 2) AS valorPago,  ");
		sql.append("negociacaopagamento.data AS dataPagamento ");
		sql.append("from contapagar  ");
		sql.append("left join contapagarnegociacaopagamento on (contapagarnegociacaopagamento.contapagar = contapagar.codigo) ");
		sql.append("and contapagarnegociacaopagamento.codigo = (select contapagarnegociacaopagamento.codigo from contapagarnegociacaopagamento where contapagarnegociacaopagamento.contapagar = contapagar.codigo  ");
		sql.append("order by contapagarnegociacaopagamento.codigo desc limit 1) ");
		sql.append("left join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaoContaPagar  ");
		sql.append("inner join pessoa on pessoa.codigo = contapagar.pessoa ");
		sql.append("inner join matricula on matricula.aluno = pessoa.codigo ");
		sql.append("where matricula.matricula = '").append(matricula).append("' ");
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			boolean virgula = false;
			sql.append("and contapagar.unidadeEnsino in(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sql.append(unidadeEnsinoVO.getCodigo());
					} else {
						sql.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sql.append(") ");
		}
		sql.append(" ORDER BY contapagar.dataVencimento");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarListaContaPagar(tabelaResultado, usuarioVO, configuracaoFinanceiroVO);
	}

	private List<DadosSituacaoContaReceberVO> montarLista(SqlRowSet tabelaResultado, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List<DadosSituacaoContaReceberVO> dadosSituacaoContaReceberVOs = new ArrayList<DadosSituacaoContaReceberVO>(0);
		List<ContaReceberVO> contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		while (tabelaResultado.next()) {
			dadosSituacaoContaReceberVOs.add(montarDados(tabelaResultado, usuarioVO, configuracaoFinanceiroVO));
			if((tabelaResultado.getDate("dataprocessamentovalorreceber") == null 
					|| !Uteis.getData(tabelaResultado.getDate("dataprocessamentovalorreceber")).equals(Uteis.getData(new Date())))
					&& tabelaResultado.getString("situacao").equals("AR")){
				ContaReceberVO contaReceberVO = new ContaReceberVO();
				contaReceberVO.setCodigo(tabelaResultado.getInt("codigo"));
				contaReceberVOs.add(contaReceberVO);		
			}
		}
		if(!contaReceberVOs.isEmpty()){
			getFacadeFactory().getContaReceberFacade().realizarProcessamentoValorFinalContaReceberAtualizadoComAcrescimosEDescontos(null, contaReceberVOs, true, "", true, usuarioVO, true, false , null , null, true);
			for(ContaReceberVO contaReceberVO: contaReceberVOs){
				for(DadosSituacaoContaReceberVO dadosSituacaoContaReceberVO: dadosSituacaoContaReceberVOs){
					if(dadosSituacaoContaReceberVO.getContaReceber().equals(contaReceberVO.getCodigo())){
						dadosSituacaoContaReceberVO.setValorIndiceReajustePorAtraso(contaReceberVO.getValorIndiceReajustePorAtraso());
						dadosSituacaoContaReceberVO.setAcrescimo(contaReceberVO.getAcrescimo() + contaReceberVO.getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue() + contaReceberVO.getValorIndiceReajustePorAtraso().doubleValue());
						dadosSituacaoContaReceberVO.setJuro(contaReceberVO.getJuro());
						dadosSituacaoContaReceberVO.setMulta(contaReceberVO.getMulta());
						dadosSituacaoContaReceberVO.setValorDescontoInstituicao(contaReceberVO.getValorDescontoInstituicao());
						dadosSituacaoContaReceberVO.setValorDescontoConvenio(contaReceberVO.getValorDescontoConvenio());
						dadosSituacaoContaReceberVO.setValorDescontoProgressivo(contaReceberVO.getValorDescontoProgressivo());
						dadosSituacaoContaReceberVO.setValorDescontoAlunoJaCalculado(contaReceberVO.getValorDescontoAlunoJaCalculado());
						dadosSituacaoContaReceberVO.setValorCalculadoDescontoLancadoRecebimento(contaReceberVO.getValorCalculadoDescontoLancadoRecebimento());
						dadosSituacaoContaReceberVO.setValorReceberCalculado(contaReceberVO.getValorReceberCalculado());
						break;
					}
				}
			}
			Uteis.liberarListaMemoria(contaReceberVOs);
		}
		return dadosSituacaoContaReceberVOs;
	}

	private List<DadosSituacaoContaPagarVO> montarListaContaPagar(SqlRowSet tabelaResultado, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List<DadosSituacaoContaPagarVO> dadosSituacaoContaPagarVOs = new ArrayList<DadosSituacaoContaPagarVO>(0);
		while (tabelaResultado.next()) {
			dadosSituacaoContaPagarVOs.add(montarDadosContaPagar(tabelaResultado, usuarioVO, configuracaoFinanceiroVO));
		}
		return dadosSituacaoContaPagarVOs;
	}

	private DadosSituacaoContaReceberVO montarDados(SqlRowSet tabelaResultado, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		DadosSituacaoContaReceberVO dadosSituacaoContaReceberVO = new DadosSituacaoContaReceberVO();
		dadosSituacaoContaReceberVO.setContaReceber(tabelaResultado.getInt("codigo"));
		dadosSituacaoContaReceberVO.setDataVencimento(tabelaResultado.getDate("datavencimento"));
		dadosSituacaoContaReceberVO.setDataPagamento(tabelaResultado.getDate("datapagamento"));
		dadosSituacaoContaReceberVO.setTipoOrigem(tabelaResultado.getString("tipoorigem"));
		dadosSituacaoContaReceberVO.setNossoNumero(tabelaResultado.getString("nossonumero"));
		dadosSituacaoContaReceberVO.setSituacao(tabelaResultado.getString("situacao"));
		dadosSituacaoContaReceberVO.setParcela(tabelaResultado.getString("parcela"));
		dadosSituacaoContaReceberVO.setValorDescontoRateio(tabelaResultado.getDouble("valorDescontoRateio"));
		dadosSituacaoContaReceberVO.setValorIndiceReajustePorAtraso(tabelaResultado.getBigDecimal("valorIndiceReajustePorAtraso"));
		dadosSituacaoContaReceberVO.setValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa(tabelaResultado.getBigDecimal("valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa"));
		if (!dadosSituacaoContaReceberVO.getSituacao().equals(SituacaoContaReceber.getDescricao("NE"))) {
			dadosSituacaoContaReceberVO.setValorDescontoInstituicao(tabelaResultado.getDouble("valorDescontoInstituicao"));
			dadosSituacaoContaReceberVO.setValorDescontoConvenio(tabelaResultado.getDouble("valorDescontoConvenio"));
			dadosSituacaoContaReceberVO.setValorDescontoProgressivo(tabelaResultado.getDouble("valorDescontoProgressivo"));
			dadosSituacaoContaReceberVO.setValorDescontoAlunoJaCalculado(tabelaResultado.getDouble("valorDescontoAlunoJaCalculado"));
			dadosSituacaoContaReceberVO.setValorCalculadoDescontoLancadoRecebimento(tabelaResultado.getDouble("valorCalculadoDescontoLancadoRecebimento"));
			if(!dadosSituacaoContaReceberVO.getSituacao().equals(SituacaoContaReceber.getDescricao("RE"))){
				dadosSituacaoContaReceberVO.setJuro(tabelaResultado.getDouble("valorjurocalculado"));
				dadosSituacaoContaReceberVO.setMulta(tabelaResultado.getDouble("valormultacalculado"));
			}else{
				dadosSituacaoContaReceberVO.setJuro(tabelaResultado.getDouble("juro"));
				dadosSituacaoContaReceberVO.setMulta(tabelaResultado.getDouble("multa"));
			}
			dadosSituacaoContaReceberVO.setAcrescimo(tabelaResultado.getDouble("acrescimo"));
			dadosSituacaoContaReceberVO.setValorRecebido(tabelaResultado.getDouble("valorrecebido"));
			dadosSituacaoContaReceberVO.setValor(tabelaResultado.getDouble("valor"));
			dadosSituacaoContaReceberVO.setValorReceberCalculado(tabelaResultado.getDouble("valorreceber"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(tabelaResultado.getDouble("valorDescontoCalculadoPrimeiraFaixaDescontos"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoSegundaFaixaDescontos(tabelaResultado.getDouble("valorDescontoCalculadoSegundaFaixaDescontos"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoTerceiraFaixaDescontos(tabelaResultado.getDouble("valorDescontoCalculadoTerceiraFaixaDescontos"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoQuartaFaixaDescontos(tabelaResultado.getDouble("valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa"));
		} else {
			dadosSituacaoContaReceberVO.setValorDescontoInstituicao(tabelaResultado.getDouble("valorDescontoInstituicao"));
			dadosSituacaoContaReceberVO.setValorDescontoConvenio(tabelaResultado.getDouble("valorDescontoConvenio"));
			dadosSituacaoContaReceberVO.setValorDescontoProgressivo(tabelaResultado.getDouble("valorDescontoProgressivo"));
			dadosSituacaoContaReceberVO.setValorDescontoAlunoJaCalculado(tabelaResultado.getDouble("valorDescontoAlunoJaCalculado"));
			dadosSituacaoContaReceberVO.setValorCalculadoDescontoLancadoRecebimento(tabelaResultado.getDouble("valorCalculadoDescontoLancadoRecebimento"));
			dadosSituacaoContaReceberVO.setJuro(tabelaResultado.getDouble("juro"));
			dadosSituacaoContaReceberVO.setMulta(tabelaResultado.getDouble("multa"));
			dadosSituacaoContaReceberVO.setAcrescimo(tabelaResultado.getDouble("acrescimo"));
			dadosSituacaoContaReceberVO.setValorRecebido(tabelaResultado.getDouble("valorrecebido"));
			dadosSituacaoContaReceberVO.setValor(tabelaResultado.getDouble("valor"));
			dadosSituacaoContaReceberVO.setValorReceberCalculado(tabelaResultado.getDouble("valorreceber"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(tabelaResultado.getDouble("valorDescontoCalculadoPrimeiraFaixaDescontos"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoSegundaFaixaDescontos(tabelaResultado.getDouble("valorDescontoCalculadoSegundaFaixaDescontos"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoTerceiraFaixaDescontos(tabelaResultado.getDouble("valorDescontoCalculadoTerceiraFaixaDescontos"));
			dadosSituacaoContaReceberVO.setValorDescontoCalculadoQuartaFaixaDescontos(tabelaResultado.getDouble("valorDescontoCalculadoQuartaFaixaDescontos"));
		}
		dadosSituacaoContaReceberVO.setAcrescimo(dadosSituacaoContaReceberVO.getAcrescimo() + dadosSituacaoContaReceberVO.getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue() + dadosSituacaoContaReceberVO.getValorIndiceReajustePorAtraso().doubleValue());
		String periodo = "";
		if ((tabelaResultado.getString("matriculaperiodo_ano") != null) && (!tabelaResultado.getString("matriculaperiodo_ano").equals("")) && (tabelaResultado.getString("matriculaperiodo_semestre") != null) && (!tabelaResultado.getString("matriculaperiodo_semestre").equals(""))) {
			periodo = tabelaResultado.getString("matriculaperiodo_ano") + "-" + tabelaResultado.getString("matriculaperiodo_semestre") + "°";
		} else {
			periodo = tabelaResultado.getString("matriculaperiodo_ano");
		}
		dadosSituacaoContaReceberVO.setPeriodoConta(periodo);
		dadosSituacaoContaReceberVO.setDescricaoCentroReceita(tabelaResultado.getString("descricaoCentroReceita"));
		return dadosSituacaoContaReceberVO;
	}

	private DadosSituacaoContaPagarVO montarDadosContaPagar(SqlRowSet tabelaResultado, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		DadosSituacaoContaPagarVO dadosSituacaoContaPagarVO = new DadosSituacaoContaPagarVO();
		dadosSituacaoContaPagarVO.setDataVencimento(tabelaResultado.getDate("datavencimento"));
		dadosSituacaoContaPagarVO.setDataPagamento(tabelaResultado.getDate("datapagamento"));
		dadosSituacaoContaPagarVO.setParcela(tabelaResultado.getString("parcela"));
		dadosSituacaoContaPagarVO.setNrDocumento(tabelaResultado.getString("nrDocumento"));
		dadosSituacaoContaPagarVO.setSituacao(tabelaResultado.getString("situacao"));
		dadosSituacaoContaPagarVO.setValor(tabelaResultado.getDouble("valor"));
		dadosSituacaoContaPagarVO.setJuro(tabelaResultado.getDouble("juro"));
		dadosSituacaoContaPagarVO.setMulta(tabelaResultado.getDouble("multa"));
		dadosSituacaoContaPagarVO.setDesconto(tabelaResultado.getDouble("desconto"));
		dadosSituacaoContaPagarVO.setValorPago(tabelaResultado.getDouble("valorPago"));
		return dadosSituacaoContaPagarVO;
	}

	public static String getDesignIReportRelatorio(String layout, String tipoLayout) {
		if (tipoLayout.equals("SituacaoFinanceiraAlunoRel")) {
			if (layout.equals("retrato")) {
				return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
			} else {
				return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadePaisagem() + ".jrxml");
			}
		} else {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + tipoLayout + ".jrxml");
		}
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("SituacaoFinanceiraAlunoRel");
	}

	public static String getIdEntidadePaisagem() {
		return ("SituacaoFinanceiraAlunoPaisagemRel");
	}

	@Override
	public PessoaVO consultarPessoaResponsavelFinanceiroPorMatriculaAluno(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct pessoa.codigo, pessoa.nome from filiacao ");
		sql.append(" inner join pessoa on pessoa.codigo  = filiacao.pais ");
		sql.append(" inner join matricula on matricula.aluno = filiacao.aluno ");
		sql.append(" where responsavelfinanceiro = true and matricula.matricula = ' ").append(matricula).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			PessoaVO pessoaVO = new PessoaVO();
			pessoaVO.setCodigo(rs.getInt("codigo"));
			pessoaVO.setNome(rs.getString("nome"));
			return pessoaVO;
		}
		return null;
	}

	@Override
	public List<MatriculaVO> consultarAlunoResponsavelFinanceiro(Integer respFinan) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select aluno.codigo, aluno.nome, matricula.matricula from filiacao  ");
		sql.append(" inner join pessoa on pessoa.codigo  = filiacao.pais ");
		sql.append(" inner join pessoa aluno on aluno.codigo = filiacao.aluno ");
		sql.append(" inner join matricula on matricula.aluno = filiacao.aluno ");
		sql.append(" where responsavelfinanceiro = true and filiacao.pais  = ").append(respFinan);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		if (rs.next()) {
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setMatricula(rs.getString("codigo"));
			matriculaVO.getAluno().setCodigo(rs.getInt("codigo"));
			matriculaVO.getAluno().setNome(rs.getString("nome"));
			matriculaVOs.add(matriculaVO);
		}
		return matriculaVOs;
	}

	private void adicionarFiltro(StringBuilder sql, String tipoPessoa, MatriculaVO matriculaVO, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, ParceiroVO parceiroVO, PessoaVO responsavelFinanceiro, String ano, String semestre, Boolean filtrarPorDataCompetencia, Date dataInicio, Date dataFim, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, CentroReceitaVO centroReceitaVO) throws Exception {
		if (tipoPessoa.equals("responsavelFinanceiro")) {
			sql.append(" where contareceber.tipoPessoa = 'RF' ");
			if (Uteis.isAtributoPreenchido(responsavelFinanceiro)) {
				sql.append(" and responsavelFinanceiro.codigo = ").append(responsavelFinanceiro.getCodigo());
			}
			if (dataInicio != null || dataFim != null) {
				sql.append(" and ");
			}
		} else {
			sql.append(" where matriculaaluno = '").append(matriculaVO.getMatricula()).append("' ");
			if (tipoPessoa.equals("aluno")) {
				sql.append(" and contareceber.tipopessoa in ('AL', 'RF', 'PA') ");
			} else if (tipoPessoa.equals("parceiro")) {
				sql.append(" and contareceber.tipopessoa = 'PA' ");
				if (Uteis.isAtributoPreenchido(parceiroVO)) {
					sql.append(" and contareceber.parceiro = ").append(parceiroVO.getCodigo());
				}
			} else if (tipoPessoa.equals("responsavelFinanceiroAlunoEspecifico")) {
				sql.append(" and contareceber.pessoa = ").append(matriculaVO.getAluno().getCodigo());
				sql.append(" and contareceber.tipopessoa in ('AL', 'RF', 'PA') ");
			}
			if (!ano.equals("") || !semestre.equals("")) {
				sql.append(" and ((");
			} 
			if (ano.equals("") && semestre.equals("") && (dataInicio != null || dataFim != null)) {
				sql.append(" and ");
			}
			if (!ano.equals("")) {
				sql.append(" matriculaperiodo.ano = '").append(ano).append("' ");
			}
			if (!semestre.equals("")) {
				if (ano.equals("")) {
					sql.append(" matriculaperiodo.semestre = '").append(semestre).append("' ");
				} else {
					sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
				}
			}
			if (!ano.equals("") || !semestre.equals("")) {
				sql.append(")");
			}
			
			if ((!ano.equals("") || !semestre.equals("")) && (dataInicio != null || dataFim != null)) {
				sql.append(" or (");
			}
		}
		if (filtrarPorDataCompetencia) {
			if (dataInicio != null) {
				sql.append(" contareceber.datacompetencia::DATE >= '").append(Uteis.getDataPrimeiroDiaMes(dataInicio)).append("'::DATE");
			}
			if (dataFim != null) {
				if (dataInicio == null) {
					sql.append(" contareceber.datacompetencia::DATE <= '").append(Uteis.getDataUltimoDiaMes(Uteis.getDataJDBC(dataFim))).append("'::DATE");
				} else {
					sql.append(" and contareceber.datacompetencia::DATE <= '").append(Uteis.getDataUltimoDiaMes(Uteis.getDataJDBC(dataFim))).append("'::DATE");
				}
			}
		} else {
			if (dataInicio != null) {
				sql.append(" contareceber.datavencimento::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("'::DATE");
			}
			if (dataFim != null) {
				if (dataInicio == null) {
					sql.append(" contareceber.datavencimento::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("'::DATE");
				} else {
					sql.append(" and contareceber.datavencimento::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("'::DATE");
				}
			}
		}
		if ((!ano.equals("") || !semestre.equals("")) && (dataInicio != null || dataFim != null)) {
			sql.append(")");
		}
		if (!ano.equals("") || !semestre.equals("")) {
			sql.append(")");
		}
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			boolean virgula = false;
			sql.append(" and contareceber.unidadeEnsino in(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sql.append(unidadeEnsinoVO.getCodigo());
					} else {
						sql.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sql.append(") ");
		}
		
		if (centroReceitaVO != null && !centroReceitaVO.getCodigo().equals(0)) {
			sql.append(" and contaReceber.centroReceita = ").append(centroReceitaVO.getCodigo());
		}
		if (filtroRelatorioFinanceiroVO.getSituacaoReceber() || filtroRelatorioFinanceiroVO.getSituacaoRecebido() || filtroRelatorioFinanceiroVO.getSituacaoCancelado() || filtroRelatorioFinanceiroVO.getSituacaoRenegociado()) {
			sql.append(" and contaReceber.situacao in( ");
			boolean virgula = false;
			if (filtroRelatorioFinanceiroVO.getSituacaoReceber()) {
				sql.append(virgula ?  ", " : "" ).append(" 'AR' ");
				virgula = true;
			}
			if (filtroRelatorioFinanceiroVO.getSituacaoRecebido()) {
				sql.append(virgula ?  ", " : "" ).append(" 'RE' ");
				virgula = true;
			}
			if (filtroRelatorioFinanceiroVO.getSituacaoCancelado()) {
				sql.append(virgula ?  ", " : "" ).append(" 'CF' ");
				virgula = true;
			}
			if (filtroRelatorioFinanceiroVO.getSituacaoRenegociado()) {
				sql.append(virgula ?  ", " : "" ).append(" 'NE' ");
				virgula = true;
			}
			sql.append(") ");
		} 
		if(!filtroRelatorioFinanceiroVO.adicionarFiltroTipoOrigem().equals("('')")) {
			sql.append(" and contaReceber.tipoOrigem in").append(filtroRelatorioFinanceiroVO.adicionarFiltroTipoOrigem());
		}
	}
}
