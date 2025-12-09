package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.ControleMarcacaoFeriasVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.formula.Formula;
import negocio.comuns.utilitarias.formula.FormulaFactory;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.FormulaFolhaPagamentoInterfaceFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class FormulaFolhaPagamento extends ControleAcesso implements FormulaFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = -1084004772804472456L;

	protected static String idEntidade;

	public FormulaFolhaPagamento() throws Exception {
		super();
		setIdEntidade("FormulaFolhaPagamento");
	}

	@Autowired
	private FormulaFactory formulaFactory;
	
	@Override
	public void persistir(FormulaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(FormulaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			FormulaFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {

					StringJoiner sql = new StringJoiner(" ");
					sql.add("INSERT INTO FormulaFolhaPagamento (identificador, descricao, formula, situacao)")
					        .add("VALUES ( ?, ?, ?, ? )")
					        .add("returning codigo");

					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;

					Uteis.setValuePreparedStatement(obj.getIdentificador(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFormula(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(FormulaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			FormulaFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

					PreparedStatement sqlAlterar = arg0.prepareStatement("UPDATE FormulaFolhaPagamento set identificador=?, descricao=?, formula=?, situacao=? WHERE codigo = ?;");
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getIdentificador(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFormula(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlAlterar);

					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FormulaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().validarDadosPorFormulaFolhaPagamento(obj, "formulaValor");
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().validarDadosPorFormulaFolhaPagamento(obj, "formulaHora");
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().validarDadosPorFormulaFolhaPagamento(obj, "formulaDia");
			getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().validarDadosPorFormulaFolhaPagamento(obj, "formulaReferencia");
			
			FormulaFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update("DELETE FROM FormulaFolhaPagamento WHERE codigo = ?", obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public void validarRestricoesInicidenciaFolhaPagamento(FormulaFolhaPagamentoVO formulaFolhaPagamentoVO) throws ConsistirException {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from FormulaFolhaPagamento where upper(trim(sem_acentos(identificador))) ilike upper(trim(sem_acentos(?))) and situacao = 'ATIVO' and codigo <> ?;");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), formulaFolhaPagamentoVO.getIdentificador(), formulaFolhaPagamentoVO.getCodigo());
		if (rs.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FormulaFolhaPagamento_duplicado"));
		}
	}

	@Override
	public List<FormulaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, String situacao) throws Exception {
		StringJoiner sql = new StringJoiner(" ");
		sql.add("select * from FormulaFolhaPagamento");
		if (situacao.equals("TODOS")) {
			situacao = "%%";
		}
		sql.add(" WHERE 1 = 1");
		sql.add(" AND situacao like ? ");
		Object parametro = null;
		switch (campoConsulta) {
		case "descricao":
			sql.add(" AND upper(trim(sem_acentos(descricao))) ilike upper(trim(sem_acentos(?)))");
			parametro = String.format("%%%s%%", valorConsulta);
			break;
		case "identificador":
			sql.add(" AND upper(trim(sem_acentos(identificador))) ilike upper(trim(sem_acentos(?)))");
			parametro = String.format("%%%s%%", valorConsulta);
			break;
		case "codigo":
			sql.add(" AND codigo = ?");
			parametro = Integer.valueOf(valorConsulta);
			break;
		default:
			break;
		}

		sql.add(" ORDER BY codigo DESC ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), situacao, parametro);
		List<FormulaFolhaPagamentoVO> incidenciasDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			incidenciasDaFolhaDePagamento.add(montarDados(tabelaResultado));
		}
		return incidenciasDaFolhaDePagamento;
	}

	public FormulaFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado) throws Exception {

		FormulaFolhaPagamentoVO obj = new FormulaFolhaPagamentoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setIdentificador(tabelaResultado.getString("identificador"));
		obj.setFormula(tabelaResultado.getString("formula"));
		obj.setSituacao(AtivoInativoEnum.valueOf(tabelaResultado.getString("situacao")));
		return obj;
	}

	private void validarDados(FormulaFolhaPagamentoVO obj) throws ConsistirException {
		try {
			obj.validarDados();
		} catch (IllegalStateException e) {
			throw new ConsistirException(UteisJSF.internacionalizar(e.getMessage()));
		}
		this.validarRestricoesInicidenciaFolhaPagamento(obj);

	}

	@Override
	public FormulaFolhaPagamentoVO consultarPorChavePrimaria(Integer codigo) throws Exception {

		StringBuilder sql = new StringBuilder("SELECT * FROM formulafolhapagamento WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado));
	}

	/**
	 * Consulta a formula pelo identificador informado.
	 * 
	 * @param identificador
	 * @throws Exception 
	 */
	@Override
	public FormulaFolhaPagamentoVO consultarFormulaPorIdentificador(String identificador) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM formulafolhapagamento WHERE identificador = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado));
	}
 
	public Object executarFormula(FormulaFolhaPagamentoVO formula, FuncionarioCargoVO funcionario, UsuarioVO usuario, ScriptEngine engine) {
		Formula formulaExecucao = formulaFactory.getInstance(formula.getFormula().trim(), funcionario, engine);

		Object valor = new Object();
		CalculoContraCheque calculoContraCheque = montarEventosCalculoContraCheque(funcionario, usuario, engine);
		calculoContraCheque.setFuncionarioCargoVO(funcionario);
		valor = formulaExecucao.execute(funcionario, parametrosDoMapa(calculoContraCheque), calculoContraCheque.getEventos());
		formula.setResultadoLog(formulaExecucao.getLog().toString());

		return valor;
	}

	@Override
	public CalculoContraCheque montarEventosCalculoContraCheque(FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario,ScriptEngine engine) {
		CalculoContraCheque calculoContraCheque = new CalculoContraCheque();
		try {
			CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO = new CompetenciaFolhaPagamentoVO();

			CompetenciaFolhaPagamentoVO competenciaAtiva = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaAtiva(true);
			ContraChequeVO contraChequeVO = getFacadeFactory().getContraChequeInterfaceFacade().consultarPorFuncionarioCargoCompetencia(funcionarioCargo, competenciaAtiva);

			if (Uteis.isAtributoPreenchido(contraChequeVO)) {
				competenciaFolhaPagamentoVO = competenciaAtiva;
			} else {
				competenciaFolhaPagamentoVO = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaFolhaPagamentoPorMesAno(UteisData.getMesData(competenciaAtiva.getDataCompetencia())-1, UteisData.getAnoData(competenciaAtiva.getDataCompetencia()), false, usuario);
				contraChequeVO = getFacadeFactory().getContraChequeInterfaceFacade().consultarPorFuncionarioCargoCompetencia(funcionarioCargo, competenciaFolhaPagamentoVO);	
			}
			
			LancamentoFolhaPagamentoVO lancamentoFolhaPagamento = getFacadeFactory().getLancamentoFolhaPagamentoInterfaceFacade().consultarPorTemplate(contraChequeVO.getTemplateLancamentoFolhaPagamentoVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
			
			calculoContraCheque.setCompetenciaFolhaPagamentoVO(competenciaFolhaPagamentoVO);
			calculoContraCheque.setFuncionarioCargoVO(funcionarioCargo);
			calculoContraCheque.calcularAvosFerias(calculoContraCheque.getCompetenciaFolhaPagamentoVO().getDataCompetencia(), null, false);
			calculoContraCheque.calcularAvos13(calculoContraCheque.getCompetenciaFolhaPagamentoVO().getDataCompetencia(), false);
			
			if (Uteis.isAtributoPreenchido(lancamentoFolhaPagamento)) {	
				calculoContraCheque.setCompetenciaPeriodoFolhaPagamentoVO(contraChequeVO.getPeriodo());
				calculoContraCheque = getFacadeFactory().getContraChequeInterfaceFacade().realizarSomaDosValoresJaCalculadosDoContraCheque(funcionarioCargo, usuario, competenciaFolhaPagamentoVO, contraChequeVO, false, engine, lancamentoFolhaPagamento);
			} else {
				calculoContraCheque.setCompetenciaPeriodoFolhaPagamentoVO(contraChequeVO.getPeriodo());
			}

			//Consulta dados da marcacao de ferias do funcionariocargo
			ControleMarcacaoFeriasVO controleMarcacaoFerias = getFacadeFactory().getControleMarcacaoFeriasInterfaceFacade().consultarControlePorFuncionarioCargoEPeriodoGozo(funcionarioCargo, competenciaFolhaPagamentoVO.getDataCompetencia());
			if(Uteis.isAtributoPreenchido(controleMarcacaoFerias)) {
				calculoContraCheque.getFuncionarioCargoVO().setInicioGozoFerias(controleMarcacaoFerias.getMarcacaoFerias().getDataInicioGozo());
				calculoContraCheque.getFuncionarioCargoVO().setFinalGozoFerias(controleMarcacaoFerias.getMarcacaoFerias().getDataFinalGozo());
				calculoContraCheque.setNumeroDiasFerias(controleMarcacaoFerias.getMarcacaoFerias().getQtdDias());
				calculoContraCheque.setNumeroDiasAbono(controleMarcacaoFerias.getMarcacaoFerias().getQtdDiasAbono());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return calculoContraCheque;
	}

	@Override
	public Object executarFormula(String formula, FuncionarioCargoVO funcionario, CalculoContraCheque calculoContraCheque, ScriptEngine engine) {
		Formula formulaExecucao = formulaFactory.getInstance(formula, funcionario, engine);
		return formulaExecucao.execute(funcionario, parametrosDoMapa(calculoContraCheque), calculoContraCheque.getEventos());
	}

	@Override
	public Object executarFormulaSemLog(String formula, FuncionarioCargoVO funcionario, ScriptEngine engine) {
		Formula formulaExecucao = formulaFactory.getInstanceSemLog(formula, engine);
		return  formulaExecucao.execute(funcionario, parametrosDoMapa());
	}
	
	public Object executarFormulaSemLog(String formula, FuncionarioCargoVO funcionario, CalculoContraCheque calculoContraCheque, ScriptEngine engine) {
		Formula formulaExecucao = formulaFactory.getInstanceSemLog(formula, engine);
		return  formulaExecucao.execute(funcionario, parametrosDoMapa(calculoContraCheque), calculoContraCheque.getEventos());
	}

	private Map<String, Object> parametrosDoMapa(CalculoContraCheque calculoContraCheque) {
		HashMap<String, Object> hashMap = new HashMap<>();

		hashMap.put("DIAS_TRABALHADOS", calculoContraCheque.getNumeroDiasTrabalhados());
		hashMap.put("VIGENCIA", new Date());
		hashMap.put("BASE_IRRF", calculoContraCheque.getBaseCalculoIRRF());
		hashMap.put("NUM_DEPENDENTE_IRRF", calculoContraCheque.getNumerosDependentesIrrf());
		hashMap.put("NUM_DEPENDENTE_SF", calculoContraCheque.getNumerosDependentesSalFamilia());
		hashMap.put("VLR_DEPENDENTE", calculoContraCheque.getValorDependente());
		hashMap.put("BASE_PREVIDENCIA", calculoContraCheque.getBaseCalculoPrevidencia());
		hashMap.put("PREV_PROPRIA", calculoContraCheque.getFuncionarioCargoVO().getPrevidencia().equals(PrevidenciaEnum.PREVIDENCIA_PROPRIA));
		hashMap.put("VLR_REFERENCIA", calculoContraCheque.getValorReferencia());

		hashMap.put("TOTAL_PROVENTOS", calculoContraCheque.getProvento());
		hashMap.put("MESES_TRABALHADOS_QUINQUENIO", calculoContraCheque.getMesesTrabalhadosQuinquenio());
		hashMap.put("MESES_TRABALHADOS", calculoContraCheque.getMesesTrabalhados());
		
		hashMap.put("INCIDE_VT", calculoContraCheque.getIncideValeTransporte());
		hashMap.put("SALARIO_FAMILIA", calculoContraCheque.getSalarioFamilia());
		hashMap.put("INCIDE_PLANO_SAUDE", calculoContraCheque.getPlanoSaude());
		hashMap.put("INCIDE_PLANO_SAUDE_FERIAS", calculoContraCheque.getIncidePlanoSaudeFerias());

		hashMap.put("ASSOCIACAO_SINDICATO", calculoContraCheque.getIncideAssociacaoSindicato());
		hashMap.put("ADICIONAL_TEMPO_SERVICO", calculoContraCheque.getIncideAdicionalTempoServico());
		hashMap.put("VALE_TRANPORTE", calculoContraCheque.getIncideValeTransporte());
		hashMap.put("BASE_PREVIDENCIA_TOTAL", calculoContraCheque.getIncidePrevidenciaPropria());
		hashMap.put("BASE_PREVIDENCIA_OBRIGATORIA", calculoContraCheque.getIncidePrevidenciaPropriaObrigatoria());
		hashMap.put("PLANO_SAUDE", calculoContraCheque.getPlanoSaude());
		hashMap.put("POSSUI_ADIANTAMENTO_FERIAS", 0);

		hashMap.put("ND_FERIAS", calculoContraCheque.getNumeroDiasFerias());
		hashMap.put("DIAS_ABONO", calculoContraCheque.getNumeroDiasAbono());
		hashMap.put("PRIMEIRO_DIA_MES", UteisData.getPrimeiroDataMes(new Date()));
		hashMap.put("ULTIMO_DIA_MES",  UteisData.getUltimoDiaMes(new Date()));
		hashMap.put("ANO_ATUAL",  UteisData.getAnoData(new Date()));
		hashMap.put("ADICIONAL_TEMPO_SERVICO_FERIAS", calculoContraCheque.getIncideAdicionalTempoServicoFerias());

		hashMap.put("ADICIONAL_TEMPO_SERVICO_RESCISAO", calculoContraCheque.getIncideAdicionalTempoServicoRescisao());
		hashMap.put("SECAO", calculoContraCheque.getFuncionarioCargoVO().getSecaoFolhaPagamento().getIdentificador());

		hashMap.put("IRRF_LANCADO", calculoContraCheque.getValorIRRFJaLancado());

		hashMap.put("BASE_IRRF_FERIAS", calculoContraCheque.getBaseCalculoIRRFFerias());
		hashMap.put("VALOR_IRRF_FERIAS", calculoContraCheque.getValorIRRFFerias());
		hashMap.put("ADICIONAL_FERIAS", calculoContraCheque.getAdicionalFerias());

		hashMap.put("INFORME_RENDIMENTO", calculoContraCheque.getInformeRendimento());
		hashMap.put("RAIS", calculoContraCheque.getRais());

		hashMap.put("BASE_INSS_FERIAS", calculoContraCheque.getIncideINSSFerias());
		hashMap.put("BASE_PREVIDENCIA_FERIAS", calculoContraCheque.getIncidePrevidenciaPropriaFerias());
		hashMap.put("BASE_PREVIDENCIA_OBRIGATORIA_FERIAS", calculoContraCheque.getIncidePrevidenciaPropriaObrigatoriaFerias());
		hashMap.put("ASSOCIACAO_SINDICATO_FERIAS", calculoContraCheque.getIncideAssociacaoSindicatoFerias());

		hashMap.put("AVO_13", calculoContraCheque.getAvos13());
		hashMap.put("AVO_FERIASP", calculoContraCheque.getAvosFeriasProporcionais());
		hashMap.put("30_JUNHO", UteisData.getData30JunhoAnoAtual());
		hashMap.put("REF_EVENTO", calculoContraCheque.getReferenciaEvento());
		hashMap.put("ND_FERIAS_MES", calculoContraCheque.getDiasFeriasMes());

		//Mapas de decimo terceiro
		hashMap.put("BASE_INSS_13", calculoContraCheque.getIncideINSSDecimoTerceiro());
		hashMap.put("BASE_IRRF_13", calculoContraCheque.getIncideIRRFDecimoTerceiro());
		hashMap.put("INCIDE_PLANO_SAUDE_13", calculoContraCheque.getIncidePlanoSaudeDecimoTerceiro());
		hashMap.put("BASE_PREVIDENCIA_13", calculoContraCheque.getIncidePrevidenciaPropriaDecimoTerceiro());
		hashMap.put("ADICIONAL_TEMPO_SERVICO_13", calculoContraCheque.getIncideAdicionalTempoServicoDecimoTerceiro());
		hashMap.put("ASSOCIACAO_SINDICATO_13", calculoContraCheque.getIncideAssociacaoSindicatoDecimoTerceiro());
		hashMap.put("BASE_PREVIDENCIA_OBRIGATORIA_13", calculoContraCheque.getIncidePrevidenciaObrigatoriaDecimoTerceiro());

		return hashMap;
	}

	private HashMap<String, Object> parametrosDoMapa() {
		return (HashMap<String, Object>) parametrosDoMapa(new CalculoContraCheque());
	}

	@Override
	public void inativar(FormulaFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					String sql = "UPDATE formulafolhapagamento set situacao=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;

					Uteis.setValuePreparedStatement(obj.getSituacao().getValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
		
	}

	public String formatarLog(String formulaLog) {
		String[] formula = formulaLog.split(";");
		StringBuilder stringBuilder = new StringBuilder();
		for (String linhaFormula : formula) {
			stringBuilder.append(linhaFormula + "\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * Inicializa a engine usando o 'nashorn'.
	 * 
	 * @return {@link ScriptEngine}
	 */
	@Override
	public ScriptEngine inicializaEngineFormula() {
		ScriptEngineManager manager = new ScriptEngineManager();
		return manager.getEngineByName("nashorn");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		FormulaFolhaPagamento.idEntidade = idEntidade;
	}
}