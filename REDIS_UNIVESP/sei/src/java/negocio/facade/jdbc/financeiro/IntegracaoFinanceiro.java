package negocio.facade.jdbc.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.AplicacaoControle;
import jobs.JobEnvioEmail;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.IntegracaoFinanceiroVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.ProcessamentoIntegracaoFinanceiraDetalheVO;
import negocio.comuns.financeiro.enumerador.LayoutArquivoIntegracaoFinanceiraEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.IntegracaoFinanceiroInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class IntegracaoFinanceiro extends ControleAcesso implements IntegracaoFinanceiroInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public IntegracaoFinanceiro() throws Exception {
		super();
		setIdEntidade("IntegracaoFinanceiro");
	}

	public IntegracaoFinanceiroVO novo() throws Exception {
		IntegracaoFinanceiro.incluir(getIdEntidade());
		IntegracaoFinanceiroVO obj = new IntegracaoFinanceiroVO();
		return obj;
	}

	public List<ProcessamentoIntegracaoFinanceiraDetalheVO> consultaContaSerProcessada(Integer codigoProcessamentoIntegracaoFinanceiro, int limit, Integer offset) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(getSqlConsultaBasica());
		sqlStr.append(" LEFT JOIN matriculaperiodo ON contareceber.matriculaperiodo = matriculaperiodo.codigo  ");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso   ");
		sqlStr.append(" LEFT JOIN matriculaperiodo mptrocar ON mptrocar.matricula = matricula.matricula "); 
		sqlStr.append(" and mptrocar.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sqlStr.append(" and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'SE' then ( ");
		sqlStr.append(" mp.ano = processamentointegracaofinanceiradetalhe.ano ");		
		sqlStr.append(" and mp.semestre = case when processamentointegracaofinanceiradetalhe.mes in ('01','02','03','04','05','06') then '1' else '2' end) ");
		sqlStr.append(" else case when curso.periodicidade = 'AN' then (mp.ano = processamentointegracaofinanceiradetalhe.ano	) ");
		sqlStr.append(" else true end end ");
		sqlStr.append(" and (mp.ano||'/'||mp.semestre)  != (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) limit 1) ");
		sqlStr.append(" WHERE processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ").append(codigoProcessamentoIntegracaoFinanceiro.intValue()).append(" and processamentointegracaofinanceiradetalhe.contareceberprocessada = false order by processamentointegracaofinanceiradetalhe.codigo LIMIT "+limit );
		if(offset != null){
			sqlStr.append(" offset ").append(offset);	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado));
	}

	public static List<ProcessamentoIntegracaoFinanceiraDetalheVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<ProcessamentoIntegracaoFinanceiraDetalheVO> vetResultado = new ArrayList<ProcessamentoIntegracaoFinanceiraDetalheVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static ProcessamentoIntegracaoFinanceiraDetalheVO montarDados(SqlRowSet dadosSQL) throws Exception {
		ProcessamentoIntegracaoFinanceiraDetalheVO obj = new ProcessamentoIntegracaoFinanceiraDetalheVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setCodigoPessoaFinanceiro(dadosSQL.getString("codigoPessoaFinanceiro"));
		obj.getIntegracaoFinanceiro().setCodigo(dadosSQL.getInt("processamentointegracaofinanceira"));
		obj.setCodigoSicredi(dadosSQL.getString("codigoSicredi"));
		obj.setValor(dadosSQL.getDouble("valor"));
		obj.setDesconto(dadosSQL.getDouble("desconto"));
		obj.setBolsa(dadosSQL.getDouble("bolsa"));
		obj.setAcrescimo(dadosSQL.getDouble("acrescimo"));
		obj.setJuro(dadosSQL.getDouble("juro"));
		obj.setMulta(dadosSQL.getDouble("multa"));
		obj.setValorReceber(dadosSQL.getDouble("valorReceber"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setBairro(dadosSQL.getString("bairro"));
		obj.setCep(dadosSQL.getString("cep"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setCidade(dadosSQL.getString("cidade"));
		obj.setComplemento(dadosSQL.getString("complemento"));
		obj.setConteudoLinhaProcessada(dadosSQL.getString("conteudoLinhaProcessada"));
		obj.setCurso(dadosSQL.getString("curso"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setMes(dadosSQL.getString("mes"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setNossoNumero(dadosSQL.getString("nossoNumero"));
		obj.setNumero(dadosSQL.getString("numero"));
		obj.setTipoOrigem(dadosSQL.getString("tipoOrigem"));
		obj.setUf(dadosSQL.getString("uf"));
		obj.setContaReceberProcessada(dadosSQL.getBoolean("contaReceberProcessada"));
		obj.setDataCompetencia(dadosSQL.getDate("dataCompetencia"));
		obj.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		obj.setDataMaximaPagamento(dadosSQL.getDate("dataMaximaPagamento"));
		obj.setPossuiPendenciasFinanceirasExternas(dadosSQL.getBoolean("possuipendenciasfinanceirasexternas"));
		obj.setMudouCodigoFinanceiro(dadosSQL.getBoolean("mudouCodigoFinanceiro"));
		obj.setContaReceber(dadosSQL.getInt("contaReceber"));
		obj.setControleCliente(dadosSQL.getString("controleCliente"));
		obj.setDataVencimentoBolsa(dadosSQL.getDate("dataVencimentoBolsa"));
		obj.setDescontoPontualidade1(dadosSQL.getDouble("descontoPontualidade1"));
		obj.setDataVencimentoDescPontualidade1(dadosSQL.getDate("dataVencimentoDescPontualidade1"));
		obj.setDescontoPontualidade1(dadosSQL.getDouble("descontoPontualidade2"));
		obj.setDataVencimentoDescPontualidade1(dadosSQL.getDate("dataVencimentoDescPontualidade2"));
		obj.setDescontoPontualidade1(dadosSQL.getDouble("descontoPontualidade3"));
		obj.setDataVencimentoDescPontualidade1(dadosSQL.getDate("dataVencimentoDescPontualidade3"));
		obj.setDescontoPontualidade1(dadosSQL.getDouble("descontoPontualidade4"));
		obj.setDataVencimentoDescPontualidade1(dadosSQL.getDate("dataVencimentoDescPontualidade4"));
		obj.setJurosApresentar(dadosSQL.getString("jurosApresentar"));
		obj.setMultaApresentar(dadosSQL.getString("multaApresentar"));
		obj.setPossuiFies(dadosSQL.getBoolean("possuiFies"));
		if(dadosSQL.getString("tipoLayoutArquivo") != null){
			obj.setTipoLayoutArquivo(LayoutArquivoIntegracaoFinanceiraEnum.valueOf(dadosSQL.getString("tipoLayoutArquivo")));
		}
		obj.setNovoObj(false);
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarProcessamentoContaReceber(ProcessamentoIntegracaoFinanceiraDetalheVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean executarRotinaAlteracao, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(obj.getContaReceber()) && executarRotinaAlteracao && obj.getMudouCodigoFinanceiro()){
			excluirTodasMatriculasPeriodoVencimento(usuario, executarRotinaAlteracao, obj.getIntegracaoFinanceiro().getCodigo(), obj.getContaReceber());
			excluirTodasContasReceberIntegracaoFinanceira(usuario, executarRotinaAlteracao, obj.getIntegracaoFinanceiro().getCodigo(), obj.getContaReceber());
			obj.setContaReceber(0);			
		}
		if(Uteis.isAtributoPreenchido(obj.getContaReceber()) && executarRotinaAlteracao && !obj.getMudouCodigoFinanceiro()){
			realizarAlteracaoContaReceber(obj, configuracaoFinanceiroVO, usuario);
		}else{
			executarProcessamentoContaReceberInclusao(obj, configuracaoFinanceiroVO, usuario);
		}
	}
	
	private void executarProcessamentoContaReceberInclusao(ProcessamentoIntegracaoFinanceiraDetalheVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaVO matriculaVO = null;
		try {
			matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorCodigoFinanceiroMatricula(obj.getCodigoPessoaFinanceiro().toString(), usuario);
			if (matriculaVO == null) {
				realizarRegistroProcessamento(obj.getCodigo(), true, "MATRICULA NÃO LOCALIZADA PARA O CÓDIGO INTEGRADOR FINANCEIRO (" + obj.getCodigoPessoaFinanceiro() + ").");
				return;
			}

		} catch (Exception e) {
			matriculaVO = null;
			realizarRegistroProcessamento(obj.getCodigo(), true, "MATRICULA NÃO LOCALIZADA PARA O CÓDIGO INTEGRADOR FINANCEIRO (" + obj.getCodigoPessoaFinanceiro() + ").");
			return;
		}
		MatriculaPeriodoVO matriculaPeriodoVO = null;
		try {
			if (matriculaVO.getCurso().getPeriodicidade().equals("IN")) {
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(matriculaVO.getMatricula(), "", "", true,  false, Optional.ofNullable(null), Optional.ofNullable(null), usuario);
			} else if (matriculaVO.getCurso().getPeriodicidade().equals("AN")) {
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(matriculaVO.getMatricula(), "", obj.getAno(), true, false, Optional.ofNullable(null), Optional.ofNullable(null), usuario);
			} else if (matriculaVO.getCurso().getPeriodicidade().equals("SE")) {
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(matriculaVO.getMatricula(), obj.getSemestre(), obj.getAno(), true, false, Optional.ofNullable(null), Optional.ofNullable(null), usuario);
			}
		} catch (Exception e) {
			matriculaPeriodoVO = null;
			try {
				matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(matriculaVO.getMatricula(), "", "", true, false, Optional.ofNullable(null), Optional.ofNullable(null), usuario);
			} catch (Exception ex) {
				matriculaPeriodoVO = null;
				realizarRegistroProcessamento(obj.getCodigo(), true, "MATRICULA PERIODO NÃO LOCALIZADA PARA O CÓDIGO INTEGRADOR FINANCEIRO (" + obj.getCodigoPessoaFinanceiro() + ").");
				return;
			}
		}
		matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().setUsarDataVencimentoDataMatricula(false);
		matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().setDataVencimentoMatricula(obj.getDataMaximaPagamento());
		matriculaPeriodoVO.setConfiguracaoFinanceiro(configuracaoFinanceiroVO);
		matriculaPeriodoVO.setMatriculaVO(matriculaVO);
		matriculaVO.getPlanoFinanceiroAluno().setTipoDescontoMatricula("VA");
		matriculaVO.getPlanoFinanceiroAluno().setTipoDescontoParcela("VA");
		matriculaVO.getPlanoFinanceiroAluno().setValorDescontoMatricula(obj.getBolsa() + obj.getDesconto());
		matriculaVO.getPlanoFinanceiroAluno().setValorDescontoParcela(obj.getBolsa() + obj.getDesconto());
		matriculaVO.getPlanoFinanceiroAluno().setDescontoProgressivo(null);
		matriculaVO.getPlanoFinanceiroAluno().setDescontoProgressivoMatricula(null);
		matriculaVO.getPlanoFinanceiroAluno().setDescontoProgressivoPrimeiraParcela(null);
		matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs().clear();
		matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs().clear();
		try {
			realizarCriacaoContaReceber(matriculaVO, matriculaPeriodoVO, obj, configuracaoFinanceiroVO, usuario);
		} catch (Exception ex) {
			realizarRegistroProcessamento(obj.getCodigo(), true, ex.getMessage());
		}
		matriculaPeriodoVO = null;
		matriculaVO = null;
	}
	
		public MatriculaPeriodoVencimentoVO realizarCriacaoMatriculaPeriodoVencimento(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessamentoIntegracaoFinanceiraDetalheVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) {
//		String parcela = "";
//		if (matriculaVO.getCurso().getPeriodicidade().equals("IN")) {
//			parcela = obj.getMes() + "/" + obj.getAno();
//		} else if (matriculaVO.getCurso().getPeriodicidade().equals("AN")) {
//			parcela = obj.getMes() + "/12";
//		} else if (matriculaVO.getCurso().getPeriodicidade().equals("SE")) {
//			parcela = ("07,08,09,10,11,12".contains(obj.getMes()) ? "0" + (Integer.valueOf(obj.getMes()) - 6) : obj.getMes()) + "/6";
//		}
		MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO = new MatriculaPeriodoVencimentoVO();
		StringBuilder sql = new StringBuilder("select codigo from matriculaPeriodoVencimento where matriculaperiodo = ? and parcela = ? ");
		SqlRowSet rs= getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matriculaPeriodoVO.getCodigo(), obj.getMes()+"/"+obj.getAno());
		if(rs.next()){			
			matriculaPeriodoVencimentoVO.setCodigo(rs.getInt("codigo"));
		}
		matriculaPeriodoVencimentoVO.setData(new Date());
		matriculaPeriodoVencimentoVO.setDataCompetencia(obj.getDataCompetencia());
		matriculaPeriodoVencimentoVO.setDataVencimento(obj.getDataVencimento());
		matriculaPeriodoVencimentoVO.setMatriculaPeriodoVO(matriculaPeriodoVO);
		matriculaPeriodoVencimentoVO.setDiasVariacaoDataVencimento(0);
		matriculaPeriodoVencimentoVO.setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
		matriculaPeriodoVencimentoVO.setParcela(obj.getMes()+"/"+obj.getAno());
		matriculaPeriodoVencimentoVO.setSituacao(SituacaoVencimentoMatriculaPeriodo.CONTARECEBER_GERADA);
		matriculaPeriodoVencimentoVO.setValor(obj.getValor());
		matriculaPeriodoVencimentoVO.setTipoDesconto("VA");
		if(obj.getTipoOrigem().equals("MAT")){
			matriculaPeriodoVencimentoVO.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MATRICULA);	
		}else if(obj.getTipoOrigem().equals("MEN")){
			matriculaPeriodoVencimentoVO.setTipoOrigemMatriculaPeriodoVencimento(TipoOrigemContaReceber.MENSALIDADE);
		}
		matriculaPeriodoVencimentoVO.setValorDesconto(0.0);
		matriculaPeriodoVencimentoVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(obj.getValorReceber());
		matriculaPeriodoVencimentoVO.setValorDescontoCalculadoSegundaFaixaDescontos(obj.getValorReceber());
		matriculaPeriodoVencimentoVO.setValorDescontoCalculadoTerceiraFaixaDescontos(obj.getValorReceber());
		matriculaPeriodoVencimentoVO.setValorDescontoCalculadoQuartaFaixaDescontos(obj.getValorReceber());
		return matriculaPeriodoVencimentoVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoContaReceber(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ProcessamentoIntegracaoFinanceiraDetalheVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO = realizarCriacaoMatriculaPeriodoVencimento(matriculaVO, matriculaPeriodoVO, obj, configuracaoFinanceiroVO, usuario);
		ContaReceberVO contaReceberVO = null;
		if (!matriculaPeriodoVencimentoVO.getVencimentoReferenteMatricula()) {
			contaReceberVO = matriculaPeriodoVO.criarContaReceberMensalidadeBaseadoMatriculaPeriodoVencimento(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, matriculaPeriodoVencimentoVO, configuracaoFinanceiroVO, false, usuario);
		} else {
			contaReceberVO = matriculaPeriodoVO.criarContaReceberMatriculaBaseadoMatriculaPeriodoVencimento(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, matriculaPeriodoVencimentoVO, false, configuracaoFinanceiroVO, false, usuario);
			contaReceberVO.setParcela("MAT_"+matriculaPeriodoVencimentoVO.getParcela());
		}		
		adicionarInformacoesIntegracaoContaReceber(obj, contaReceberVO);
		getFacadeFactory().getContaReceberFacade().incluir(contaReceberVO, false, configuracaoFinanceiroVO, usuario);
		gravarValorFinalContaReceberAtualizadoComAcrescimosEDescontos(obj, contaReceberVO, usuario);
		matriculaPeriodoVencimentoVO.setContaReceber(contaReceberVO);
		if(!Uteis.isAtributoPreenchido(matriculaPeriodoVencimentoVO.getCodigo())){
			getFacadeFactory().getMatriculaPeriodoVencimentoFacade().incluir(matriculaPeriodoVencimentoVO, usuario);
		}else{
			getFacadeFactory().getMatriculaPeriodoVencimentoFacade().alterar(matriculaPeriodoVencimentoVO, false, usuario);
		}
		realizarRegistroProcessamento(obj.getCodigo(), false, "");
		matriculaPeriodoVencimentoVO =null;
		contaReceberVO = null;
	}
	
	public void gravarValorFinalContaReceberAtualizadoComAcrescimosEDescontos(ProcessamentoIntegracaoFinanceiraDetalheVO obj, ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception{
		contaReceberVO.setValorRecebido(obj.getValorReceber());
		contaReceberVO.setJuro(obj.getJuro());
		contaReceberVO.setValorJuroCalculado(obj.getJuro());
		contaReceberVO.setMulta(obj.getMulta());
		contaReceberVO.setValorMultaCalculado(obj.getMulta());
		contaReceberVO.setAcrescimo(obj.getAcrescimo());
		contaReceberVO.setValorDesconto(obj.getDesconto() + obj.getBolsa());
		contaReceberVO.setValorDescontoAlunoJaCalculado(obj.getDesconto() + obj.getBolsa());
		contaReceberVO.setValorReceberCalculado(obj.getValorReceber());
		contaReceberVO.setValorDescontoCalculado(obj.getDesconto() + obj.getBolsa());
		getFacadeFactory().getContaReceberFacade().gravarValorFinalContaReceberAtualizadoComAcrescimosEDescontos(contaReceberVO, usuario, false, true);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarAlteracaoContaReceber(ProcessamentoIntegracaoFinanceiraDetalheVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ContaReceberVO contaReceberVO = new ContaReceberVO();
		contaReceberVO.setCodigo(obj.getContaReceber());
		try{
			getFacadeFactory().getContaReceberFacade().carregarDados(contaReceberVO, NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuario);
			adicionarInformacoesIntegracaoContaReceber(obj, contaReceberVO);			
			getFacadeFactory().getContaReceberFacade().gerarDadosBoleto(contaReceberVO, configuracaoFinanceiroVO, false, usuario);
			gravarValorFinalContaReceberAtualizadoComAcrescimosEDescontos(obj, contaReceberVO, usuario);
			realizarRegistroProcessamento(obj.getCodigo(), false, "");
		}catch(Exception e){
			realizarRegistroProcessamento(obj.getCodigo(), true, e.getMessage());
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void adicionarInformacoesIntegracaoContaReceber(ProcessamentoIntegracaoFinanceiraDetalheVO obj, ContaReceberVO contaReceberVO){
		contaReceberVO.setProcessamentoIntegracaoFinanceiraDetalheVO(obj);
		if (contaReceberVO.getContaCorrenteVO().getAgencia().getBanco().getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
			if (obj.getNossoNumero().length() < 11) {
				String zeros = "";
				for (int i = 0; i + obj.getNossoNumero().length() != obj.getNossoNumero().length(); i++) {
					zeros = zeros + "0";
				}
				contaReceberVO.setNossoNumero(zeros+obj.getNossoNumero());				
			}else {
				contaReceberVO.setNossoNumero(obj.getNossoNumero());
			}			
		} else {
			contaReceberVO.setNossoNumero(obj.getNossoNumero());
		}		
		contaReceberVO.setDataVencimento(obj.getDataVencimento());
		contaReceberVO.setDataCompetencia(obj.getDataCompetencia());		
		contaReceberVO.setValor(obj.getValor());
		contaReceberVO.setValorBaseContaReceber(obj.getValor());
		contaReceberVO.setJuro(obj.getJuro());
		contaReceberVO.setMulta(obj.getMulta());
		contaReceberVO.setAcrescimo(obj.getAcrescimo());
		contaReceberVO.setValorRecebido(0.0);		
		contaReceberVO.setTipoDesconto("VA");
		contaReceberVO.setValorDesconto(obj.getDesconto() + obj.getBolsa());
		contaReceberVO.setValorDescontoAlunoJaCalculado(obj.getDesconto() + obj.getBolsa());
		contaReceberVO.setValorReceberCalculado(obj.getValorReceber());
		contaReceberVO.setValorDescontoCalculado(obj.getDesconto() + obj.getBolsa());
		contaReceberVO.setNrDocumento(obj.getMes()+obj.getAno()+obj.getCodigoPessoaFinanceiro());
		contaReceberVO.setJustificativaDesconto((obj.getDesconto() > 0 ? "Desconto Aluno: " + Uteis.getDoubleFormatado(obj.getDesconto()) + "    " : "") + (obj.getBolsa() > 0 ? "Bolsa: " + Uteis.getDoubleFormatado(obj.getBolsa()) : ""));
		contaReceberVO.setTipoOrigem(obj.getTipoOrigem());
		contaReceberVO.setObservacao(contaReceberVO.getJustificativaDesconto());
		contaReceberVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(obj.getValorReceber());
		contaReceberVO.setValorDescontoCalculadoSegundaFaixaDescontos(obj.getValorReceber());
		contaReceberVO.setValorDescontoCalculadoTerceiraFaixaDescontos(obj.getValorReceber());
		contaReceberVO.setValorDescontoCalculadoQuartaFaixaDescontos(obj.getValorReceber());
		contaReceberVO.setDescricaoPagamento(contaReceberVO.getDescricaoPagamento());
		contaReceberVO.setSituacao("AR");
		contaReceberVO.setPossuiPendenciasFinanceirasExternas(obj.getPossuiPendenciasFinanceirasExternas());
		contaReceberVO.setPossuiFiesIntegracaoFinanceiras(obj.getPossuiFies());
	}

	public static String getIdEntidade() {
		return IntegracaoFinanceiro.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		IntegracaoFinanceiro.idEntidade = idEntidade;
	}

	public String removerZeroEsquerdaString(String codigoFinanceiroMatricula) {
		return codigoFinanceiroMatricula.replaceFirst("0*", "");
	}

	@Override
	public void executarIntegracaoFinanceiro(final Integer codigoProcessamentoIntegracaoFinanceiro) {
		Stopwatch watch = new Stopwatch();
		watch.start();
		try {			
			if(!Uteis.isAtributoPreenchido(codigoProcessamentoIntegracaoFinanceiro) || !validaCodigoIntegracaoFinanceiraProcessarValidoEPossuiContaProcessar(codigoProcessamentoIntegracaoFinanceiro)){
				JobEnvioEmail.enviarSMSNotificacaoEquipeOtimize("CODIGO EXECUCAO INTEGRACAO FINANCEIRA INVALIDA/JA EXECUTADA ("+codigoProcessamentoIntegracaoFinanceiro+") ");
				return;
			}
		//	JobEnvioEmail.enviarSMSNotificacaoEquipeOtimize("INTEGRACAO FINANCEIRA EM EXECUCAO ("+codigoProcessamentoIntegracaoFinanceiro+") ");
			final Boolean executarRotinaAlteracao = !realizarVerificacaoExecucaoRotinaExclusao(codigoProcessamentoIntegracaoFinanceiro);
			final ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, 0);
			final ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, 0, null);
			configuracaoFinanceiroVO.setNivelMontarDados(NivelMontarDados.TODOS);
			final UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(configuracaoGeralSistemaVO.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, null);
			realizarRegistroContaProcessadaErroPorCodigoIntegracaoFinanceiraAluno(codigoProcessamentoIntegracaoFinanceiro);
			if(!executarRotinaAlteracao){
				excluirTodasMatriculasPeriodoVencimento(usuarioVO, executarRotinaAlteracao, codigoProcessamentoIntegracaoFinanceiro, 0);
				excluirTodasContasReceberIntegracaoFinanceira(usuarioVO, executarRotinaAlteracao, codigoProcessamentoIntegracaoFinanceiro, 0);
			}else{				
				realizarRegistroContaProcessadaSemAlteracao(codigoProcessamentoIntegracaoFinanceiro);				
				excluirTodasMatriculasPeriodoVencimento(usuarioVO, executarRotinaAlteracao, codigoProcessamentoIntegracaoFinanceiro, 0);
				excluirTodasContasReceberIntegracaoFinanceira(usuarioVO, executarRotinaAlteracao, codigoProcessamentoIntegracaoFinanceiro, 0);
			}
			List<ProcessamentoIntegracaoFinanceiraDetalheVO> processamentoIntegracaoFinanceiraDetalheVOs = consultaContaSerProcessada(codigoProcessamentoIntegracaoFinanceiro, 1000, null);
			do {
				executarProcessamentoIntegracaoFinanceiro(processamentoIntegracaoFinanceiraDetalheVOs, configuracaoFinanceiroVO, executarRotinaAlteracao, usuarioVO);
				Uteis.liberarListaMemoria(processamentoIntegracaoFinanceiraDetalheVOs);
				processamentoIntegracaoFinanceiraDetalheVOs = consultaContaSerProcessada(codigoProcessamentoIntegracaoFinanceiro, 1000, null);
			} while (!processamentoIntegracaoFinanceiraDetalheVOs.isEmpty());
			//realizarSuspensacaoMatricula(codigoProcessamentoIntegracaoFinanceiro);			
			realizarAtivacaoMatriculaPeriodo(codigoProcessamentoIntegracaoFinanceiro, configuracaoFinanceiroVO);
			watch.stop();			
			JobEnvioEmail.enviarSMSNotificacaoEquipeUNIRV("INTEGRACAO FINANCEIRA EXECUTADA - EM "+watch.getElapsed()+" ");					
			Uteis.liberarListaMemoria(processamentoIntegracaoFinanceiraDetalheVOs);
		} catch (Exception e) {
			watch.stop();
			JobEnvioEmail.enviarSMSNotificacaoEquipeOtimize("INTEGRACAO FINANCEIRA NAO EXECUTADA, VERIFICAR LOG : ("+codigoProcessamentoIntegracaoFinanceiro+") ");
			realizarErroGenericoProcessamentoIntegracaoFinanceiro(codigoProcessamentoIntegracaoFinanceiro, e.getMessage());
		}
				
		watch = null;
	}
	
	public Boolean realizarVerificacaoExecucaoRotinaExclusao(Integer codigoProcessamentoIntegracaoFinanceiro){
		return getConexao().getJdbcTemplate().queryForRowSet("select realizarexclusao from  processamentointegracaofinanceira where codigo = ? and realizarexclusao = true ", codigoProcessamentoIntegracaoFinanceiro).next();
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtivacaoMatriculaPeriodo(Integer codigoProcessamentoIntegracaoFinanceiro, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO){
		StringBuilder sql  = new StringBuilder("");
		sql.append(" update matriculaperiodo set ").append(configuracaoFinanceiroVO.getAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula()?"situacaomatriculaperiodo  = (case when situacaomatriculaperiodo = 'PR' then 'AT' else situacaomatriculaperiodo end), ":"").append("  situacao  = 'CO'  where (situacaomatriculaperiodo = 'PR' or situacao  != 'CO') and codigo not in (");
		sql.append(" 	select contareceber.matriculaperiodo from contareceber");
		sql.append(" 	inner JOIN processamentointegracaofinanceiradetalhe ON contareceber.processamentointegracaofinanceiradetalhe = processamentointegracaofinanceiradetalhe.codigo ");
		sql.append(" 	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
		sql.append(" 	and contareceber.tipoorigem = 'MAT' and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ").append(codigoProcessamentoIntegracaoFinanceiro);
		sql.append(" );");
		getConexao().getJdbcTemplate().update(sql.toString());		
//		sql  = new StringBuilder("");
//		sql.append(" update matriculaperiodo set ").append(configuracaoFinanceiroVO.getAtivarPreMatriculaAutomaticamenteAposPagamentoTaxaMatricula()?"situacaomatriculaperiodo  = (case when situacaomatriculaperiodo = 'AT' then 'PR' else situacaomatriculaperiodo end), ":"").append(" situacao  = 'PF'  where (situacaomatriculaperiodo = 'AT' or situacao  != 'PF') and codigo in (");
//		sql.append(" 	select contareceber.matriculaperiodo from contareceber");
//		sql.append(" 	inner JOIN processamentointegracaofinanceiradetalhe ON contareceber.processamentointegracaofinanceiradetalhe = processamentointegracaofinanceiradetalhe.codigo ");
//		sql.append(" 	where contareceber.matriculaperiodo = matriculaperiodo.codigo");
//		sql.append(" 	and contareceber.tipoorigem = 'MAT' and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ").append(codigoProcessamentoIntegracaoFinanceiro);
//		sql.append(" );");
//		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarSuspensacaoMatricula(Integer codigoProcessamentoIntegracaoFinanceiro){
		StringBuilder sql  = new StringBuilder("");
		sql.append("  update matricula set matriculasuspensa  = true from processamentointegracaofinanceiradetalhe");
		sql.append("  where processamentointegracaofinanceiradetalhe.codigoPessoaFinanceiro = matricula.codigofinanceiromatricula");
		sql.append("  and processamentointegracaofinanceiradetalhe.suspenderMatricula");
		sql.append("  and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ? ");
		getConexao().getJdbcTemplate().update(sql.toString(), codigoProcessamentoIntegracaoFinanceiro);
		sql  = new StringBuilder("");
		sql.append("  update matricula set matriculasuspensa  = false where matricula.codigofinanceiromatricula not in ");
		sql.append("  (select processamentointegracaofinanceiradetalhe.codigoPessoaFinanceiro from processamentointegracaofinanceiradetalhe ");
		sql.append("  where processamentointegracaofinanceiradetalhe.codigoPessoaFinanceiro = matricula.codigofinanceiromatricula");
		sql.append("  and processamentointegracaofinanceiradetalhe.suspenderMatricula");
		sql.append("  and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ? ) ");
		getConexao().getJdbcTemplate().update(sql.toString(), codigoProcessamentoIntegracaoFinanceiro);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTodasMatriculasPeriodoVencimento(UsuarioVO usuario, Boolean executarRotinaAlteracao, Integer codigoProcessamentoIntegracaoFinanceiro, Integer contaEspecifica) throws Exception {
		realizarRemocaoVinculoRemessaTodasContasReceberExcluidaIntegracaoFinanceira(usuario, executarRotinaAlteracao, codigoProcessamentoIntegracaoFinanceiro, contaEspecifica);
		StringBuilder sql = new StringBuilder(" ");
		if(executarRotinaAlteracao){
			sql.append(" delete from MatriculaPeriodoVencimento where contareceber in (");
			sql.append(" 	select contareceber.codigo from contareceber ");
			sql.append(" 	inner join processamentointegracaofinanceiradetalhe on processamentointegracaofinanceiradetalhe.codigo =  contareceber.processamentointegracaofinanceiradetalhe");
			sql.append(" 	where contareceber.situacao = 'AR' and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira != ").append(codigoProcessamentoIntegracaoFinanceiro);
			if(Uteis.isAtributoPreenchido(contaEspecifica)){
				sql.append(" and  contareceber.codigo =  ").append(contaEspecifica); 
			}
			sql.append(" ) --ignoreaudit ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}else{
			sql.append("DELETE FROM MatriculaPeriodoVencimento ");
			if(Uteis.isAtributoPreenchido(contaEspecifica)){
				sql.append(" where  contareceber.codigo =  ").append(contaEspecifica); 
			}
			sql.append("--ignoreaudit");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRemocaoVinculoRemessaTodasContasReceberExcluidaIntegracaoFinanceira(UsuarioVO usuario, Boolean executarRotinaAlteracao, Integer codigoProcessamentoIntegracaoFinanceiro, Integer contaEspecifica) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		if (executarRotinaAlteracao) {
			sql.append(" update controleremessacontareceber set contareceber = null where contareceber in (");
			sql.append("  select contareceber.codigo from contareceber ");
			sql.append("  inner join processamentointegracaofinanceiradetalhe on processamentointegracaofinanceiradetalhe.codigo =  contareceber.processamentointegracaofinanceiradetalhe");
			sql.append("  where contareceber.situacao = 'AR' and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira != ").append(codigoProcessamentoIntegracaoFinanceiro);
			if (Uteis.isAtributoPreenchido(contaEspecifica)) {
				sql.append(" and  contareceber.codigo =  ").append(contaEspecifica);
			}
			sql.append(" ) --ignoreaudit ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		} else {
			sql.append(" update controleremessacontareceber set contareceber = null where contareceber in (");
			sql.append("select codigo FROM ContaReceber where contareceber.situacao = 'AR' and processamentointegracaofinanceiradetalhe is not null and processamentointegracaofinanceiradetalhe > 0 ");
			if (Uteis.isAtributoPreenchido(contaEspecifica)) {
				sql.append(" and  contareceber.codigo =  ").append(contaEspecifica);
			}
			sql.append(") ");
			sql.append("--ignoreaudit");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTodasContasReceberIntegracaoFinanceira(UsuarioVO usuario, Boolean executarRotinaAlteracao, Integer codigoProcessamentoIntegracaoFinanceiro, Integer contaEspecifica) throws Exception {
		StringBuilder sql = new StringBuilder(" ");
		if(executarRotinaAlteracao){
			sql.append(" delete from contareceber where codigo in (");
			sql.append(" 	select contareceber.codigo from contareceber ");
			sql.append(" 	inner join processamentointegracaofinanceiradetalhe on processamentointegracaofinanceiradetalhe.codigo =  contareceber.processamentointegracaofinanceiradetalhe");
			sql.append(" 	where contareceber.situacao = 'AR' and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira != ").append(codigoProcessamentoIntegracaoFinanceiro);
			if(Uteis.isAtributoPreenchido(contaEspecifica)) {
				sql.append(" and  contareceber.codigo =  ").append(contaEspecifica); 
			}
			sql.append(" ) --ignoreaudit ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}else{
			sql.append("DELETE FROM ContaReceber where contareceber.situacao = 'AR' and processamentointegracaofinanceiradetalhe is not null and processamentointegracaofinanceiradetalhe > 0 ");
			if(Uteis.isAtributoPreenchido(contaEspecifica)) {
				sql.append(" and  contareceber.codigo =  ").append(contaEspecifica); 
			}
			sql.append("--ignoreaudit");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		}
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	public void executarProcessamentoIntegracaoFinanceiro(final List<ProcessamentoIntegracaoFinanceiraDetalheVO> processamentoIntegracaoFinanceiraDetalheVOs, final ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, final Boolean executarRotinaAlteracao, final UsuarioVO usuarioVO) throws Exception {		
		final ConsistirException consistirException = new ConsistirException();		
			ProcessarParalelismo.Processo processo = new ProcessarParalelismo.Processo() {
				@Override
				public void run(int i) {
					ProcessamentoIntegracaoFinanceiraDetalheVO obj = processamentoIntegracaoFinanceiraDetalheVOs.get(i);
					try {
						executarProcessamentoContaReceber(obj, configuracaoFinanceiroVO, executarRotinaAlteracao, usuarioVO);

					} catch (Exception e) {
						consistirException.adicionarListaMensagemErro(e.getMessage());
					} finally {
						obj = null;
					}
				}
			};
			ProcessarParalelismo.executar(0, processamentoIntegracaoFinanceiraDetalheVOs.size(), consistirException, processo);
			processo = null;		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroProcessamento(Integer codigoDetalhe, Boolean erro, String motivo) throws Exception {
		getConexao().getJdbcTemplate().update("update processamentointegracaofinanceiradetalhe set dataprocessamento = now(),contaReceberProcessada = ?, contaReceberProcessadaErro = ?,  motivoErroProcessamento = ? where codigo = ? ", true, erro, motivo, codigoDetalhe);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarErroGenericoProcessamentoIntegracaoFinanceiro(Integer codigoProcessamentoIntegracaoFinanceiro, String motivo) {
		getConexao().getJdbcTemplate().update("update processamentointegracaofinanceira set  motivoErroProcessamento = ? where codigo = ? ", motivo, codigoProcessamentoIntegracaoFinanceiro);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroContaProcessadaErroPorCodigoIntegracaoFinanceiraAluno(Integer codigoProcessamentoIntegracaoFinanceiro){
		StringBuilder sql  = new StringBuilder("");
		sql.append(" update processamentointegracaofinanceiradetalhe set  contareceberprocessada = true , contareceberprocessadaerro = true, ");
		sql.append(" motivoerroprocessamento =  'MATRICULA NÃO LOCALIZADA PARA O CÓDIGO INTEGRADOR FINANCEIRO ('||processamentointegracaofinanceiradetalhe.codigopessoafinanceiro||').'");
		sql.append(" where codigopessoafinanceiro not in (select matricula.codigofinanceiromatricula from matricula  where matricula.codigofinanceiromatricula = processamentointegracaofinanceiradetalhe.codigopessoafinanceiro )");
		sql.append(" and  processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ").append(codigoProcessamentoIntegracaoFinanceiro).append(" and processamentointegracaofinanceiradetalhe.contareceberprocessada = false;");
		
		getConexao().getJdbcTemplate().update(sql.toString());
	}
	

	private StringBuilder getSqlConsultaBasica() {
		StringBuilder sqlConsultaBasica = new StringBuilder("");
			sqlConsultaBasica.append(
					"SELECT processamentointegracaofinanceiradetalhe.codigo,processamentointegracaofinanceiradetalhe.conteudolinhaprocessada,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.contareceberprocessada,processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.codigopessoafinanceiro,processamentointegracaofinanceiradetalhe.nossonumero,processamentointegracaofinanceiradetalhe.tipoorigem,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.nome,processamentointegracaofinanceiradetalhe.curso,processamentointegracaofinanceiradetalhe.endereco,processamentointegracaofinanceiradetalhe.numero,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.complemento,processamentointegracaofinanceiradetalhe.bairro,processamentointegracaofinanceiradetalhe.cidade,processamentointegracaofinanceiradetalhe.uf,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.cep,processamentointegracaofinanceiradetalhe.cpf,processamentointegracaofinanceiradetalhe.mes,processamentointegracaofinanceiradetalhe.ano,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.bolsa,processamentointegracaofinanceiradetalhe.desconto,processamentointegracaofinanceiradetalhe.juro,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.multa,processamentointegracaofinanceiradetalhe.acrescimo,processamentointegracaofinanceiradetalhe.valorreceber, processamentointegracaofinanceiradetalhe.valor,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.datamaximapagamento,processamentointegracaofinanceiradetalhe.datavencimento,processamentointegracaofinanceiradetalhe.datacompetencia,");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.controleCliente, processamentointegracaofinanceiradetalhe.dataVencimentoBolsa, processamentointegracaofinanceiradetalhe.descontoPontualidade1, ");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade1, processamentointegracaofinanceiradetalhe.descontoPontualidade2, processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade2, ");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.descontoPontualidade3, processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade3, processamentointegracaofinanceiradetalhe.descontoPontualidade4, ");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade4, processamentointegracaofinanceiradetalhe.jurosApresentar, processamentointegracaofinanceiradetalhe.multaApresentar, ");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.tipoLayoutArquivo, processamentointegracaofinanceiradetalhe.codigosicredi, ");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.possuipendenciasfinanceirasexternas, contareceber.codigo as contareceber, (contareceber.codigo is not null and matricula.codigofinanceiromatricula !=  processamentointegracaofinanceiradetalhe.codigopessoafinanceiro) as mudouCodigoFinanceiro, ");
			sqlConsultaBasica.append(
					" processamentointegracaofinanceiradetalhe.possuiFies");
			sqlConsultaBasica.append(
					" FROM processamentointegracaofinanceira INNER JOIN processamentointegracaofinanceiradetalhe");
			sqlConsultaBasica.append(
					" ON processamentointegracaofinanceira.codigo = processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira");
			sqlConsultaBasica.append(
					" LEFT JOIN contareceber ON contareceber.nossonumero = processamentointegracaofinanceiradetalhe.nossonumero ");
			sqlConsultaBasica.append(" LEFT JOIN matricula ON contareceber.matriculaaluno = matricula.matricula  ");
		
		return sqlConsultaBasica;
	}
	
	@Override
	public ProcessamentoIntegracaoFinanceiraDetalheVO consultaPorCodigoProcessamentoIntegracaoFinanceiro(Integer codigoProcessamentoIntegracaoFinanceiro) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(getSqlConsultaBasica());
		sqlStr.append(" WHERE processamentointegracaofinanceiradetalhe.codigo = ").append(codigoProcessamentoIntegracaoFinanceiro.intValue()).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(tabelaResultado.next()){
			return (montarDados(tabelaResultado));
		}
		return null;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRegistroContaProcessadaSemAlteracao(Integer codigoProcessamentoIntegracaoFinanceiro){
		
		StringBuilder sql = new StringBuilder("");
		sql.append("  update contareceber set processamentointegracaofinanceiradetalhe = t.processamentointegracaofinanceiradetalhe, 		");
		sql.append("  possuiPendenciasFinanceirasExternas = t.possuiPendenciasFinanceirasExternas		");
		sql.append("  from ( ");
		sql.append("  select contareceber.codigo, processamentointegracaofinanceiradetalhe.codigo as processamentointegracaofinanceiradetalhe, ");
		sql.append("  processamentointegracaofinanceiradetalhe.possuiPendenciasFinanceirasExternas ");
		sql.append("  FROM contareceber	");
		sql.append("  inner join matricula on contareceber.matriculaaluno = matricula.matricula	");
		sql.append("  inner JOIN matriculaperiodo ON contareceber.matriculaperiodo = matriculaperiodo.codigo	");
		sql.append("  inner JOIN curso ON curso.codigo = matricula.curso ");
		sql.append("  inner join processamentointegracaofinanceiradetalhe on contareceber.nossonumero = processamentointegracaofinanceiradetalhe.nossonumero	");
		sql.append("  and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = 	").append(codigoProcessamentoIntegracaoFinanceiro);
		sql.append("  LEFT JOIN matriculaperiodo mptrocar ON mptrocar.matricula = matricula.matricula "); 
		sql.append("  and mptrocar.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sql.append("  and mp.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'SE' then ( ");
		sql.append("  mp.ano = processamentointegracaofinanceiradetalhe.ano ");		
		sql.append("  and mp.semestre = case when processamentointegracaofinanceiradetalhe.mes in ('01','02','03','04','05','06') then '1' else '2' end) ");
		sql.append("  else case when curso.periodicidade = 'AN' then (mp.ano = processamentointegracaofinanceiradetalhe.ano	) ");
		sql.append("  else true end end ");
		sql.append("  and (mp.ano||'/'||mp.semestre)  != (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) limit 1) ");
		sql.append("  inner join processamentointegracaofinanceiradetalhe as detalhe on contareceber.processamentointegracaofinanceiradetalhe = detalhe.codigo ");	
		sql.append("  WHERE ");
		sql.append("  contareceber.datavencimento = processamentointegracaofinanceiradetalhe.datavencimento 		");
		sql.append("  and contareceber.valor = processamentointegracaofinanceiradetalhe.valor		");
		sql.append("  and contareceber.datacompetencia = processamentointegracaofinanceiradetalhe.datacompetencia		");			
		sql.append("  and processamentointegracaofinanceiradetalhe.contareceberprocessada = false		");
		sql.append("  and processamentointegracaofinanceiradetalhe.codigopessoafinanceiro = matricula.codigofinanceiromatricula");			
		sql.append("  and ((detalhe.tipolayoutarquivo = 'LAYOUT_DESC_DETALHADO' and processamentointegracaofinanceiradetalhe.tipolayoutarquivo = 'LAYOUT_DESC_DETALHADO' ");
		sql.append("  and detalhe.valor = processamentointegracaofinanceiradetalhe.valor ");
		sql.append("  and detalhe.bolsa = processamentointegracaofinanceiradetalhe.bolsa ");
		sql.append("  and ((detalhe.datavencimentobolsa = processamentointegracaofinanceiradetalhe.datavencimentobolsa) "); 
		sql.append("  or (detalhe.datavencimentobolsa is null and processamentointegracaofinanceiradetalhe.datavencimentobolsa is null)) ");
		sql.append("  and detalhe.descontopontualidade1 = processamentointegracaofinanceiradetalhe.descontopontualidade1 ");
		sql.append("  and ((detalhe.datavencimentodescpontualidade1 = processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade1) ");
		sql.append("  or (detalhe.datavencimentodescpontualidade1 is null and processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade1 is null)) ");
		sql.append("  and detalhe.descontopontualidade2 = processamentointegracaofinanceiradetalhe.descontopontualidade2 ");
		sql.append("  and ((detalhe.datavencimentodescpontualidade2 = processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade2) ");
		sql.append("  or (detalhe.datavencimentodescpontualidade2 is null and processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade2 is null)) ");
		sql.append("  and detalhe.descontopontualidade2 = processamentointegracaofinanceiradetalhe.descontopontualidade2 ");
		sql.append("  and ((detalhe.datavencimentodescpontualidade3 = processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade3) ");
		sql.append("  or (detalhe.datavencimentodescpontualidade3 is null and processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade3 is null)) ");
		sql.append("  and ((detalhe.datavencimentodescpontualidade4 = processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade4) ");
		sql.append("  or (detalhe.datavencimentodescpontualidade4 is null and processamentointegracaofinanceiradetalhe.datavencimentodescpontualidade4 is null)) ");						
		sql.append("  ) or (detalhe.tipolayoutarquivo in ('LAYOUT_DESC_GERAL', 'LAYOUT_DESC_GERAL_2') and processamentointegracaofinanceiradetalhe.tipolayoutarquivo in ('LAYOUT_DESC_GERAL', 'LAYOUT_DESC_GERAL_2') ");			
		sql.append("  and case when contareceber.juro is null then 0.0 else contareceber.juro end = processamentointegracaofinanceiradetalhe.juro		");
		sql.append("  and case when contareceber.multa is null then 0.0 else contareceber.multa end = processamentointegracaofinanceiradetalhe.multa 		");
		sql.append("  and case when contareceber.acrescimo is null then 0.0 else contareceber.acrescimo end = processamentointegracaofinanceiradetalhe.acrescimo 		");
		sql.append("  and contareceber.valordesconto = (processamentointegracaofinanceiradetalhe.desconto + processamentointegracaofinanceiradetalhe.bolsa )::NUMERIC(20,2)		");
		sql.append("  )) ");
		
		// este verifica se não existe outra matricula periodo para vincular a conta a receber
		sql.append("   and mptrocar.codigo is null ");
		sql.append(" ) as t where t.codigo = contareceber.codigo --ignoreaudit	");
		getConexao().getJdbcTemplate().update(sql.toString());
		
		sql = new StringBuilder("");
		sql.append("  update processamentointegracaofinanceiradetalhe set contareceberprocessada  = true from contareceber");
		sql.append("  where contareceber.processamentointegracaofinanceiradetalhe = processamentointegracaofinanceiradetalhe.codigo");
		sql.append("  and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ").append(codigoProcessamentoIntegracaoFinanceiro);			
		getConexao().getJdbcTemplate().update(sql.toString());

		//** Vincula Contas Alterar ao Processamento Integracao para não serem excluidas  
		sql = new StringBuilder("");
		sql.append("  update contareceber set processamentointegracaofinanceiradetalhe = processamentointegracaofinanceiradetalhe.codigo from processamentointegracaofinanceiradetalhe ");
		sql.append("  where contareceber.nossonumero = processamentointegracaofinanceiradetalhe.nossonumero ");
		sql.append("  and contareceberprocessada  = false and processamentointegracaofinanceiradetalhe.processamentointegracaofinanceira = ").append(codigoProcessamentoIntegracaoFinanceiro).append(" --ignoreaudit ");			
		getConexao().getJdbcTemplate().update(sql.toString());	
	}
	
	public Boolean validaCodigoIntegracaoFinanceiraProcessarValidoEPossuiContaProcessar(Integer codigoProcessamentoIntegracaoFinanceiro){
		return getConexao().getJdbcTemplate().queryForRowSet("select codigo from processamentointegracaofinanceiradetalhe where contareceberprocessada = false and processamentointegracaofinanceira= "+codigoProcessamentoIntegracaoFinanceiro+" limit 1").next();
	}
	
    /**
     * Método responsável por verificar se existe um arquivo de integração financeira sendo processado. 
     * Caso o retorno seja verdadeiro o recurso de consultar as conta a receber será bloqueado até que 
     * o processamento do arquivo seja finalizado. 
     */
	public  Boolean realizarVerificacaoProcessamentoIntegracaoFinanceira() {
		if (AplicacaoControle.getIntegracaoFinanceiraProcessando() > 0) {
			return true;
		}
		return false;
	}
	

	@Override
	public List<PlanoFinanceiroAlunoDescricaoDescontosVO> realizarGeracaoPlanoDescontoDescricaoAluno(ProcessamentoIntegracaoFinanceiraDetalheVO processamentoIntegracaoFinanceiraDetalheVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception{
		new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		Map<Long, PlanoFinanceiroAlunoDescricaoDescontosVO> mpPlanoDesconto = new HashMap<Long, PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		if(processamentoIntegracaoFinanceiraDetalheVO.getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_DETALHADO)){
		adicionarPlanoDescontoAlunoIntegracaoFinanceiro(processamentoIntegracaoFinanceiraDetalheVO, 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoBolsa(), 
				0.0, 
				processamentoIntegracaoFinanceiraDetalheVO.getBolsa(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoBolsa(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade1(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade1(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade2(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade2(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade3(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade3(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade4(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade4(),
				mpPlanoDesconto, configuracaoFinanceiroVO);
		adicionarPlanoDescontoAlunoIntegracaoFinanceiro(processamentoIntegracaoFinanceiraDetalheVO, 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade1(), 
				0.0, 
				processamentoIntegracaoFinanceiraDetalheVO.getBolsa(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoBolsa(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade1(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade1(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade2(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade2(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade3(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade3(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade4(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade4(),
				mpPlanoDesconto, configuracaoFinanceiroVO);
		adicionarPlanoDescontoAlunoIntegracaoFinanceiro(processamentoIntegracaoFinanceiraDetalheVO, 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade2(), 
				0.0, 
				processamentoIntegracaoFinanceiraDetalheVO.getBolsa(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoBolsa(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade1(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade1(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade2(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade2(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade3(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade3(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade4(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade4(),
				mpPlanoDesconto, configuracaoFinanceiroVO);
		adicionarPlanoDescontoAlunoIntegracaoFinanceiro(processamentoIntegracaoFinanceiraDetalheVO, 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade3(), 
				0.0, 
				processamentoIntegracaoFinanceiraDetalheVO.getBolsa(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoBolsa(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade1(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade1(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade2(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade2(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade3(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade3(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade4(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade4(),
				mpPlanoDesconto, configuracaoFinanceiroVO);
		adicionarPlanoDescontoAlunoIntegracaoFinanceiro(processamentoIntegracaoFinanceiraDetalheVO, 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade4(), 
				0.0, 
				processamentoIntegracaoFinanceiraDetalheVO.getBolsa(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoBolsa(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade1(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade1(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade2(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade2(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade3(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade3(),
				processamentoIntegracaoFinanceiraDetalheVO.getDescontoPontualidade4(), 
				processamentoIntegracaoFinanceiraDetalheVO.getDataVencimentoDescPontualidade4(),
				mpPlanoDesconto, configuracaoFinanceiroVO);
		}else{
			adicionarPlanoDescontoAlunoIntegracaoFinanceiro(processamentoIntegracaoFinanceiraDetalheVO, 
					processamentoIntegracaoFinanceiraDetalheVO.getDataMaximaPagamento(), 
					0.0, processamentoIntegracaoFinanceiraDetalheVO.getBolsa(), 
					null, processamentoIntegracaoFinanceiraDetalheVO.getDesconto(), 
					null, 0.0, null, 0.0, null, 0.0, null, mpPlanoDesconto, configuracaoFinanceiroVO);
		}
		List<PlanoFinanceiroAlunoDescricaoDescontosVO>  listaPlanoFinanceiroAlunoDescricaoDescontos =  new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		listaPlanoFinanceiroAlunoDescricaoDescontos.addAll(mpPlanoDesconto.values());
		Ordenacao.ordenarListaDecrescente(listaPlanoFinanceiroAlunoDescricaoDescontos, "diaNrAntesVencimento");
		if(mpPlanoDesconto.containsKey(-1l) && mpPlanoDesconto.containsKey(0l)){
			mpPlanoDesconto.get(-1l).setReferentePlanoFinanceiroAposVcto(true);
		}else if(mpPlanoDesconto.containsKey(-1l) && !mpPlanoDesconto.containsKey(0l) && mpPlanoDesconto.size() > 1){
			mpPlanoDesconto.get(-1l).setTipoOrigemDesconto(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO);
			mpPlanoDesconto.get(-1l).setDataInicioAplicacaoDesconto(Uteis.getDateHoraFinalDia(Uteis.obterDataAvancada(listaPlanoFinanceiroAlunoDescricaoDescontos.get(mpPlanoDesconto.size()-2).getDataLimiteAplicacaoDesconto(), 1)));
		}
		return listaPlanoFinanceiroAlunoDescricaoDescontos;
	}
	
	private void adicionarPlanoDescontoAlunoIntegracaoFinanceiro(ProcessamentoIntegracaoFinanceiraDetalheVO processamentoIntegracaoFinanceiraDetalheVO, 
    		Date dataBaseVencimentoDesconto,
    		Double valorDescontoAluno,
    		Double valorDescontoCovenio, Date dataVencimentoDescontoConvenio,
    		Double valorDescontoProgressivo1, Date dataVencimentoDescontoProgressivo1,
    		Double valorDescontoProgressivo2, Date dataVencimentoDescontoProgressivo2,
    		Double valorDescontoProgressivo3, Date dataVencimentoDescontoProgressivo3,
    		Double valorDescontoProgressivo4, Date dataVencimentoDescontoProgressivo4,
    		Map<Long, PlanoFinanceiroAlunoDescricaoDescontosVO> mpPlanoDesconto, 
    		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception{
    	if( ( (valorDescontoAluno != null && valorDescontoAluno > 0) 
    			|| (valorDescontoCovenio != null && valorDescontoCovenio > 0.0)
    			|| (valorDescontoProgressivo1 != null && valorDescontoProgressivo1 > 0.0)
    			|| (valorDescontoProgressivo2 != null && valorDescontoProgressivo2 > 0.0)
    			|| (valorDescontoProgressivo3 != null && valorDescontoProgressivo3 > 0.0)
    			|| (valorDescontoProgressivo4 != null && valorDescontoProgressivo4 > 0.0))  
    			&& ((configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade() && dataBaseVencimentoDesconto != null) 
    			|| !configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade())){
    		Long numeroDesconto = -1l;
    		if(dataBaseVencimentoDesconto != null && Uteis.getData(processamentoIntegracaoFinanceiraDetalheVO.getDataVencimento()).equals(Uteis.getData(dataBaseVencimentoDesconto))){
    			numeroDesconto = 0l;
    		}else if(dataBaseVencimentoDesconto != null){
    			numeroDesconto = Uteis.nrDiasEntreDatas(processamentoIntegracaoFinanceiraDetalheVO.getDataVencimento(), dataBaseVencimentoDesconto);
    		}
    		if(!mpPlanoDesconto.containsKey(numeroDesconto)){
    			mpPlanoDesconto.put(numeroDesconto, new PlanoFinanceiroAlunoDescricaoDescontosVO());    			
    		}    		    	
    		PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO = mpPlanoDesconto.get(numeroDesconto.longValue());
    		planoFinanceiroAlunoDescricaoDescontosVO.setCodigoContaReceber(processamentoIntegracaoFinanceiraDetalheVO.getContaReceber());
    		if(processamentoIntegracaoFinanceiraDetalheVO.getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_DETALHADO)){
    			planoFinanceiroAlunoDescricaoDescontosVO.setValorBase(processamentoIntegracaoFinanceiraDetalheVO.getValor());    		    		    	
    		}else{
    			planoFinanceiroAlunoDescricaoDescontosVO.setValorBase(processamentoIntegracaoFinanceiraDetalheVO.getValor()+processamentoIntegracaoFinanceiraDetalheVO.getJuro()+processamentoIntegracaoFinanceiraDetalheVO.getMulta()+processamentoIntegracaoFinanceiraDetalheVO.getAcrescimo());
    		}    		    		    	    	
    		planoFinanceiroAlunoDescricaoDescontosVO.setDataInicioAplicacaoDesconto(Uteis.getData("01/01/1970", "dd/MM/yyyy"));
    		planoFinanceiroAlunoDescricaoDescontosVO.setDataLimiteAplicacaoDesconto(dataBaseVencimentoDesconto);    		
    		planoFinanceiroAlunoDescricaoDescontosVO.setReferentePlanoFinanceiroAteVencimento(numeroDesconto.equals(0l));
    		planoFinanceiroAlunoDescricaoDescontosVO.setDiaNrAntesVencimento(numeroDesconto.intValue());
    		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoAluno(valorDescontoAluno);
    		if( (valorDescontoCovenio != null && valorDescontoCovenio > 0.0) &&
    			(dataVencimentoDescontoConvenio == null || (dataBaseVencimentoDesconto != null && dataVencimentoDescontoConvenio != null &&
    			dataVencimentoDescontoConvenio.compareTo(dataBaseVencimentoDesconto) <= 0))){
    			planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoConvenio(valorDescontoCovenio);
    		}    		
    		if( (valorDescontoProgressivo1 != null && valorDescontoProgressivo1 > 0.0) &&
        			(dataVencimentoDescontoProgressivo1 == null || (dataBaseVencimentoDesconto != null && dataVencimentoDescontoProgressivo1 != null &&
        					dataVencimentoDescontoProgressivo1.compareTo(dataBaseVencimentoDesconto) >= 0))){
        			planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoProgressivo(valorDescontoProgressivo1);
        			planoFinanceiroAlunoDescricaoDescontosVO.setPercentualDescontoProgressivo(Uteis.arrendondarForcando4CadasDecimais(valorDescontoProgressivo1*100/processamentoIntegracaoFinanceiraDetalheVO.getValor()));

        	}else if( (valorDescontoProgressivo2 != null && valorDescontoProgressivo2 > 0.0) &&
        			(dataVencimentoDescontoProgressivo2 == null || (dataBaseVencimentoDesconto != null && dataVencimentoDescontoProgressivo2 != null &&
					dataVencimentoDescontoProgressivo2.compareTo(dataBaseVencimentoDesconto) >= 0))){
        		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoProgressivo(valorDescontoProgressivo2);
        		planoFinanceiroAlunoDescricaoDescontosVO.setPercentualDescontoProgressivo(Uteis.arrendondarForcando4CadasDecimais(valorDescontoProgressivo2*100/processamentoIntegracaoFinanceiraDetalheVO.getValor()));
        		

        	}else if( (valorDescontoProgressivo3 != null && valorDescontoProgressivo3 > 0.0) &&
        			(dataVencimentoDescontoProgressivo3 == null || (dataBaseVencimentoDesconto != null && dataVencimentoDescontoProgressivo3 != null &&
        			dataVencimentoDescontoProgressivo3.compareTo(dataBaseVencimentoDesconto) >= 0))){
        		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoProgressivo(valorDescontoProgressivo3);
        		planoFinanceiroAlunoDescricaoDescontosVO.setPercentualDescontoProgressivo(Uteis.arrendondarForcando4CadasDecimais(valorDescontoProgressivo3*100/processamentoIntegracaoFinanceiraDetalheVO.getValor()));

        	}else if( (valorDescontoProgressivo4 != null && valorDescontoProgressivo4 > 0.0) &&
        			(dataVencimentoDescontoProgressivo4 == null || (dataBaseVencimentoDesconto != null && dataVencimentoDescontoProgressivo4 != null &&
        			dataVencimentoDescontoProgressivo4.compareTo(dataBaseVencimentoDesconto) >= 0))){
        		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoProgressivo(valorDescontoProgressivo4);
        		planoFinanceiroAlunoDescricaoDescontosVO.setPercentualDescontoProgressivo(Uteis.arrendondarForcando4CadasDecimais(valorDescontoProgressivo4*100/processamentoIntegracaoFinanceiraDetalheVO.getValor()));

        	}
    		planoFinanceiroAlunoDescricaoDescontosVO.setValorBaseComDescontosJaCalculadosAplicados(
    				planoFinanceiroAlunoDescricaoDescontosVO.getValorBase()-planoFinanceiroAlunoDescricaoDescontosVO.getValorDescontoAluno()
    				-planoFinanceiroAlunoDescricaoDescontosVO.getValorDescontoConvenio()
    				-planoFinanceiroAlunoDescricaoDescontosVO.getValorDescontoProgressivo()-planoFinanceiroAlunoDescricaoDescontosVO.getValorDescontoRateio());
    		if(planoFinanceiroAlunoDescricaoDescontosVO.getValorBaseComDescontosJaCalculadosAplicados().equals(planoFinanceiroAlunoDescricaoDescontosVO.getValorBase())){
    			mpPlanoDesconto.remove(numeroDesconto);
    		}
    		    		
    	}
    	
    }


}