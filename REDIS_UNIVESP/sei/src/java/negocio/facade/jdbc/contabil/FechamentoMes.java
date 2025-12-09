package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.FechamentoMesContaCaixaVO;
import negocio.comuns.contabil.FechamentoMesHistoricoModificacaoVO;
import negocio.comuns.contabil.FechamentoMesUnidadeEnsinoVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.FechamentoMesInterfaceFacade;

/**
 * @see fechamentoMesVO
 * @see SuperEntidade
 */
@SuppressWarnings("unchecked")
@Repository
@Lazy
public class FechamentoMes extends ControleAcesso implements FechamentoMesInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public FechamentoMes() throws Exception {
		super();
		setIdEntidade("FechamentoMes");
	}

	public FechamentoMesVO novo() throws Exception {
		ControleAcesso.incluir(getIdEntidade());
		FechamentoMesVO obj = new FechamentoMesVO();
		return obj;
	}
	
    /**
     * Operação responsável por validar os dados de um objeto da classe <code>fechamentoMesVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public void validarDados(FechamentoMesVO obj) throws ConsistirException {

		if (!obj.getPoliticaFechamentoMesPadrao()) {
			obj.setGerarPrevisaoReceitaCompetenciaUltimoDiaMes(Boolean.FALSE);
			obj.setGerarPrevisaoReceitaCompetenciaDiaEspecifico(Boolean.FALSE);
			obj.setGerarPrevisaoReceitaCompetenciaAutomatimente(Boolean.FALSE);
			obj.setFecharCompetenciaUltimoDiaMes(Boolean.FALSE);
			obj.setFecharCompetenciaDiaEspecifico(Boolean.FALSE);
		}
    	if (!obj.getGerarFechamentoMesTodasUnidades()) {
    		if ((obj.getListaUnidadesEnsinoVOs() == null)
                || (obj.getListaUnidadesEnsinoVOs().isEmpty())) {
    			throw new ConsistirException("O campo UNIDADE(S) ENSINO(s) (Fechamento da Competência) deve ser informado.");
    		}
    	}
        if (obj.getMes().intValue() == 0) {
            throw new ConsistirException("O campo MÊS (Fechamento da Competência) deve ser informado.");
        }
        if (obj.getAno().intValue() == 0) {
            throw new ConsistirException("O campo ANO (Fechamento da Competência) deve ser informado.");
        }
        
        if ((obj.getGerarPrevisaoReceitaCompetenciaDiaEspecifico()) &&
            (obj.getDiaEspecificoGerarPrevisaoReceitaCompetencia().intValue() <= 0)) {
            throw new ConsistirException("O campo DIA ESPECÍFICO GERAR PREVISÃO (Fechamento da Competência) deve ser informado.");
        }
        
        if ((obj.getFecharCompetenciaDiaEspecifico()) &&
            (obj.getDiaEspecificoFechaCompetencia().intValue() <= 0)) {
                throw new ConsistirException("O campo DIA ESPECÍFICO FECHAR COMPETÊNCIA (Fechamento da Competência) deve ser informado.");
        }
        
        verificarJaExisteFechamentoMesUnidadeEnsino(obj);
    }
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FechamentoMesVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			ControleAcesso.incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO fechamentoMes( "
					+ "fechado, mes, ano, usuario, dataFechamento, observacao, "
					+ "politicaFechamentoMesPadrao, dataInicioFechamentoMes, dataFimFechamentoMes, fecharParcialmenteMes, "
					+ "fecharCompetenciaUltimoDiaMes, fecharCompetenciaDiaEspecifico, "
					+ "diaEspecificoFechaCompetencia, nrMesesManterAbertoAntesFechamentoAutomatico, gerarPrevisaoReceitaCompetenciaAutomatimente, "
					+ "gerarPrevisaoReceitaCompetenciaUltimoDiaMes, gerarPrevisaoReceitaCompetenciaDiaEspecifico, diaEspecificoGerarPrevisaoReceitaCompetencia, "
					+ "bloquearInclusaoAlteracaoContaReceber, bloquearRecebimentoContaReceber, bloquearInclusaoAlteracaoContaPagar, "
					+ "bloquearPagamentoContaPagar, bloquearInclusaoAlteracaoNotaFiscalEntrada, bloquearInclusaoAlteracaoNotaFiscalSaida, "
					+ "bloquearMovimentacoesFinanceiras, bloquearAberturaTodasContaCaixa, bloquearAberturaContasCaixasEspecificas, gerarFechamentoMesTodasUnidades,"
					+ "dataUtilizarVerificarBloqueioContaPagar, dataUtilizarVerificarBloqueioContaReceber "
					+ ") VALUES ( "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
					+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  "
					+ ") returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 1;
					sqlInserir.setBoolean(i++, obj.isFechado().booleanValue());
					sqlInserir.setInt(i++, obj.getMes().intValue());
					sqlInserir.setInt(i++, obj.getAno().intValue());
					if (Uteis.isAtributoPreenchido(obj.getUsuario())) {
						sqlInserir.setInt(i++, obj.getUsuario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(i++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataFechamento())) {
						sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataFechamento()));
					} else {
						sqlInserir.setNull(i++, 0);
					}					
					sqlInserir.setString(i++, obj.getObservacao());
					sqlInserir.setBoolean(i++, obj.getPoliticaFechamentoMesPadrao().booleanValue());
					if (Uteis.isAtributoPreenchido(obj.getDataInicioFechamentoMes())) {
						sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataInicioFechamentoMes()));
					} else {
						sqlInserir.setNull(i++, 0);
					}					
					if (Uteis.isAtributoPreenchido(obj.getDataFimFechamentoMes())) {
						sqlInserir.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataFimFechamentoMes()));
					} else {
						sqlInserir.setNull(i++, 0);
					}					
					sqlInserir.setBoolean(i++, obj.getFecharParcialmenteMes().booleanValue());
					sqlInserir.setBoolean(i++, obj.getFecharCompetenciaUltimoDiaMes().booleanValue());
					sqlInserir.setBoolean(i++, obj.getFecharCompetenciaDiaEspecifico().booleanValue());
					sqlInserir.setInt(i++, obj.getDiaEspecificoFechaCompetencia());
					sqlInserir.setInt(i++, obj.getNrMesesManterAbertoAntesFechamentoAutomatico());
					sqlInserir.setBoolean(i++, obj.getGerarPrevisaoReceitaCompetenciaAutomatimente().booleanValue());
					sqlInserir.setBoolean(i++, obj.getGerarPrevisaoReceitaCompetenciaUltimoDiaMes().booleanValue());
					sqlInserir.setBoolean(i++, obj.getGerarPrevisaoReceitaCompetenciaDiaEspecifico().booleanValue());
					sqlInserir.setInt(i++, obj.getDiaEspecificoGerarPrevisaoReceitaCompetencia());
					sqlInserir.setBoolean(i++, obj.getBloquearInclusaoAlteracaoContaReceber().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearRecebimentoContaReceber().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearInclusaoAlteracaoContaPagar().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearPagamentoContaPagar().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearInclusaoAlteracaoNotaFiscalEntrada().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearInclusaoAlteracaoNotaFiscalSaida().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearMovimentacoesFinanceiras().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearAberturaTodasContaCaixa().booleanValue());
					sqlInserir.setBoolean(i++, obj.getBloquearAberturaContasCaixasEspecificas().booleanValue());
					sqlInserir.setBoolean(i++, obj.getGerarFechamentoMesTodasUnidades().booleanValue());
					sqlInserir.setString(i++, obj.getDataUtilizarVerificarBloqueioContaPagar());
					sqlInserir.setString(i++, obj.getDataUtilizarVerificarBloqueioContaReceber());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
			getFacadeFactory().getFechamentoMesUnidadeEnsinoFacade().incluirListaFechamentoMesUnidadeEnsino(obj.getCodigo(), obj.getListaUnidadesEnsinoVOs(), usuarioVO);
			getFacadeFactory().getFechamentoMesContaCaixaFacade().incluirListaFechamentoMesContaCaixa(obj.getCodigo(), obj.getListaContaCaixaVOs(), usuarioVO);
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().incluirListaFechamentoMesHistoricoModificacao(obj.getCodigo(), obj.getListaHistoricoModificacaoVOs(), usuarioVO);			
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FechamentoMesVO obj, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			ControleAcesso.alterar(getIdEntidade(), controlarAcesso, usuarioVO);
			final String sql = "UPDATE fechamentoMes set "
					+ " fechado=?, mes=?, ano=?, usuario=?, dataFechamento=?, observacao=?, "
					+ " politicaFechamentoMesPadrao=?, dataInicioFechamentoMes=?, dataFimFechamentoMes=?, fecharParcialmenteMes=?, "
					+ " fecharCompetenciaUltimoDiaMes=?, fecharCompetenciaDiaEspecifico=?, "
					+ " diaEspecificoFechaCompetencia=?, nrMesesManterAbertoAntesFechamentoAutomatico=?, gerarPrevisaoReceitaCompetenciaAutomatimente=?, "
					+ " gerarPrevisaoReceitaCompetenciaUltimoDiaMes=?, gerarPrevisaoReceitaCompetenciaDiaEspecifico=?, diaEspecificoGerarPrevisaoReceitaCompetencia=?, "
					+ " bloquearInclusaoAlteracaoContaReceber=?, bloquearRecebimentoContaReceber=?, bloquearInclusaoAlteracaoContaPagar=?, "
					+ " bloquearPagamentoContaPagar=?, bloquearInclusaoAlteracaoNotaFiscalEntrada=?, bloquearInclusaoAlteracaoNotaFiscalSaida=?, "
					+ " bloquearMovimentacoesFinanceiras=?, bloquearAberturaTodasContaCaixa=?, bloquearAberturaContasCaixasEspecificas=?, "
					+ " gerarFechamentoMesTodasUnidades=?, dataUtilizarVerificarBloqueioContaPagar=?, dataUtilizarVerificarBloqueioContaReceber=? "
					+ " WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 1;
					sqlAlterar.setBoolean(i++, obj.isFechado().booleanValue());
					sqlAlterar.setInt(i++, obj.getMes().intValue());
					sqlAlterar.setInt(i++, obj.getAno().intValue());
					if (Uteis.isAtributoPreenchido(obj.getUsuario().getCodigo())) {
						sqlAlterar.setInt(i++, obj.getUsuario().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(i++, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataFechamento())) {
						sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataFechamento()));
					} else {
						sqlAlterar.setNull(i++, 0);
					}					
					sqlAlterar.setString(i++, obj.getObservacao());
					sqlAlterar.setBoolean(i++, obj.getPoliticaFechamentoMesPadrao().booleanValue());
					if (Uteis.isAtributoPreenchido(obj.getDataInicioFechamentoMes())) {
						sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataInicioFechamentoMes()));
					} else {
						sqlAlterar.setNull(i++, 0);
					}					
					if (Uteis.isAtributoPreenchido(obj.getDataFimFechamentoMes())) {
						sqlAlterar.setTimestamp(i++, Uteis.getDataJDBCTimestamp(obj.getDataFimFechamentoMes()));
					} else {
						sqlAlterar.setNull(i++, 0);
					}					
					sqlAlterar.setBoolean(i++, obj.getFecharParcialmenteMes().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getFecharCompetenciaUltimoDiaMes().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getFecharCompetenciaDiaEspecifico().booleanValue());
					sqlAlterar.setInt(i++, obj.getDiaEspecificoFechaCompetencia());
					sqlAlterar.setInt(i++, obj.getNrMesesManterAbertoAntesFechamentoAutomatico());
					sqlAlterar.setBoolean(i++, obj.getGerarPrevisaoReceitaCompetenciaAutomatimente().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getGerarPrevisaoReceitaCompetenciaUltimoDiaMes().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getGerarPrevisaoReceitaCompetenciaDiaEspecifico().booleanValue());
					sqlAlterar.setInt(i++, obj.getDiaEspecificoGerarPrevisaoReceitaCompetencia());
					sqlAlterar.setBoolean(i++, obj.getBloquearInclusaoAlteracaoContaReceber().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearRecebimentoContaReceber().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearInclusaoAlteracaoContaPagar().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearPagamentoContaPagar().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearInclusaoAlteracaoNotaFiscalEntrada().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearInclusaoAlteracaoNotaFiscalSaida().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearMovimentacoesFinanceiras().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearAberturaTodasContaCaixa().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getBloquearAberturaContasCaixasEspecificas().booleanValue());
					sqlAlterar.setBoolean(i++, obj.getGerarFechamentoMesTodasUnidades().booleanValue());
					sqlAlterar.setString(i++, obj.getDataUtilizarVerificarBloqueioContaPagar());
					sqlAlterar.setString(i++, obj.getDataUtilizarVerificarBloqueioContaReceber());					
					sqlAlterar.setInt(i++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getFechamentoMesUnidadeEnsinoFacade().alterarListaFechamentoMesUnidadeEnsino(obj.getCodigo(), obj.getListaUnidadesEnsinoVOs(), usuarioVO);
			getFacadeFactory().getFechamentoMesContaCaixaFacade().alterarListaFechamentoMesContaCaixa(obj.getCodigo(), obj.getListaContaCaixaVOs(), usuarioVO);
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().alterarListaFechamentoMesHistoricoModificacao(obj.getCodigo(), obj.getListaHistoricoModificacaoVOs(), usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void fecharMes(FechamentoMesVO obj) throws Exception {
		try {
			validarDados(obj);
			alterar(getIdEntidade());
			String sql = "UPDATE fechamentoMes set fechado=?, dataFechamento=?, usuario=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.isFechado(), obj.getDataFechamento(), obj.getUsuario().getCodigo(), obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FechamentoMesVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade());
			ControleAcesso.excluir(getIdEntidade(), true, usuarioVO);
			
			if (!obj.getListaHistoricoModificacaoVOs().isEmpty()) {
				throw new Exception("Não é possível EXCLUIR este fechamento, pois o mesmo está FECHADO (ou já existem históricos de modificação de bloqueios registrados no mesmo).");
			}

			getFacadeFactory().getFechamentoMesUnidadeEnsinoFacade().excluir(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getFechamentoMesContaCaixaFacade().excluir(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().excluir(obj.getCodigo(), usuarioVO);
			
			String sql = "DELETE FROM fechamentoMes WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<FechamentoMesVO> consultarPorCompetencia(Integer mes, Integer ano, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM fechamentoMes WHERE (mes = " + mes.intValue() + ") and (ano = " + ano.intValue() + ") ORDER BY mes, ano";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}	

	public List<FechamentoMesVO> consultarPorAno(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM fechamentoMes WHERE ano = " + valorConsulta.intValue() + " ORDER BY ano, mes";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<FechamentoMesVO> consultarPorMes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM fechamentoMes WHERE mes = " + valorConsulta.intValue() + " ORDER BY mes, ano";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public List<FechamentoMesVO> consultarCompetenciaEmAbertoPorPeriodoPorUnidadeEnsinoPorCodigoIntegracaoContabil(Date dataInicio, Date dataFim, Integer unidadeEnsino, String codigoIntegracaoContabil) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select mes::int, ano::int from ( ");
		sqlStr.append(" select extract( month from periodo ) as mes, extract( year from periodo ) as ano ");
		sqlStr.append(" from generate_series('").append(Uteis.getDataJDBCTimestamp(dataInicio)).append("'::timestamp, '").append(Uteis.getDataJDBCTimestamp(dataFim)).append("', '1 month') as periodo");
		sqlStr.append(" ) as t ");
		sqlStr.append(" where not exists ( ");
		sqlStr.append(" select fechamentomes.codigo from fechamentomes ");
		sqlStr.append(" where t.mes=fechamentomes.mes and t.ano = fechamentomes.ano ");
		sqlStr.append(" and fechamentomes.fechado ");
		sqlStr.append(" and ( gerarfechamentomestodasunidades = true or ");
		sqlStr.append(" (gerarfechamentomestodasunidades = false ");
		sqlStr.append(" and exists(select fechamentomesunidadeensino.unidadeensino from fechamentomesunidadeensino ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = fechamentomesunidadeensino.unidadeensino	");
		sqlStr.append(" where fechamentomesunidadeensino.fechamentomes = fechamentomes.codigo  ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(codigoIntegracaoContabil)){
			sqlStr.append(" and unidadeensino.codigointegracaocontabil = '").append(codigoIntegracaoContabil).append("' ");
		}
		sqlStr.append(" )))) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<FechamentoMesVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			FechamentoMesVO fchMes = new FechamentoMesVO();
			fchMes.setMes(new Integer(tabelaResultado.getInt("mes")));
			fchMes.setAno(new Integer(tabelaResultado.getInt("ano")));
			lista.add(fchMes);
		}
		return lista;
	}
	
	public FechamentoMesVO verificarCompetenciaFechada(Date dataVerificar, Date dataCompVerificar, TipoOrigemHistoricoBloqueioEnum tipoOrigem, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		return verificarCompetenciaFechada(dataVerificar, dataCompVerificar, tipoOrigem, unidadeEnsino, null, usuario);
	}
	
	public FechamentoMesVO verificarCompetenciaFechada(Date dataVerificar, Date dataCompVerificar, TipoOrigemHistoricoBloqueioEnum tipoOrigem, Integer unidadeEnsino, Integer contaCaixa, UsuarioVO usuario) throws Exception {
		List<Integer> listaCodigosUnidadesEnsino = new ArrayList<Integer>();
		listaCodigosUnidadesEnsino.add(unidadeEnsino);
		return verificarCompetenciaFechada(dataVerificar, dataCompVerificar, tipoOrigem, listaCodigosUnidadesEnsino, null, usuario);
	}	

	public FechamentoMesVO verificarCompetenciaFechada(Date dataVerificar, Date dataCompVerificar, TipoOrigemHistoricoBloqueioEnum tipoOrigem, List<Integer> listaUnidadesEnsino, Integer contaCaixa, UsuarioVO usuario) throws Exception {
		int mes = Uteis.getMesData(dataVerificar);
		int ano = Uteis.getAnoData(dataVerificar);
		
		int mesComp = mes;
		int anoComp = ano;		
		boolean validarDataComp = false;
		if ((dataCompVerificar != null) &&
		    (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.APAGAR)) ||
		    (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.ARECEBER))){
			mesComp = Uteis.getMesData(dataCompVerificar);
			anoComp = Uteis.getAnoData(dataCompVerificar);
			validarDataComp = true;
		}
		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT fechamentoMes.codigo, fechamentoMes.mes, fechamentoMes.ano, fechamentoMes.dataUtilizarVerificarBloqueioContaPagar, fechamentoMes.dataUtilizarVerificarBloqueioContaReceber FROM fechamentoMes ");
		sqlStr.append("LEFT JOIN FechamentoMesUnidadeEnsino ON FechamentoMesUnidadeEnsino.fechamentoMes = fechamentoMes.codigo ");
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.CAIXA)) {
			sqlStr.append("LEFT JOIN FechamentoMesContaCaixa ON FechamentoMesContaCaixa.fechamentoMes = fechamentoMes.codigo ");
		}
		sqlStr.append("WHERE (1=1) ");
		if (!validarDataComp) {
			sqlStr.append(" AND (fechamentoMes.mes = ").append(mes).append(") ");
			sqlStr.append(" AND (fechamentoMes.ano = ").append(ano).append(") ");
		} else {
			sqlStr.append(" AND (( ");
			sqlStr.append("     ((fechamentoMes.mes = ").append(mes).append(") ");
			sqlStr.append("  AND (fechamentoMes.ano = ").append(ano).append(") ");
			if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.APAGAR)) {
				sqlStr.append("  AND (fechamentoMes.dataUtilizarVerificarBloqueioContaPagar = 'DV')").append(") ");
			}
			if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.ARECEBER)) {
				sqlStr.append("  AND (fechamentoMes.dataUtilizarVerificarBloqueioContaReceber = 'DV')").append(") ");
			}			
			sqlStr.append(" ) OR ( ");
			sqlStr.append("     ((fechamentoMes.mes = ").append(mesComp).append(") ");
			sqlStr.append("  AND (fechamentoMes.ano = ").append(anoComp).append(") ");
			if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.APAGAR)) {
				sqlStr.append("  AND (fechamentoMes.dataUtilizarVerificarBloqueioContaPagar = 'DC')").append(") ");
			}
			if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.ARECEBER)) {
				sqlStr.append("  AND (fechamentoMes.dataUtilizarVerificarBloqueioContaReceber = 'DC')").append(") ");
			}			
			sqlStr.append(" )) ");
		}
		
		sqlStr.append(" AND (fechamentoMes.politicaFechamentoMesPadrao = false) ");
		sqlStr.append(" AND (fechamentoMes.fechado = true) ");
		
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.ARECEBER)) {
			sqlStr.append(" AND (fechamentoMes.bloquearInclusaoAlteracaoContaReceber = true) ");
		}
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO)) {
			sqlStr.append(" AND (fechamentoMes.bloquearRecebimentoContaReceber = true) ");
		}
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.APAGAR)) {
			sqlStr.append(" AND (fechamentoMes.bloquearInclusaoAlteracaoContaPagar = true) ");
		}
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.PAGAMENTO)) {
			sqlStr.append(" AND (fechamentoMes.bloquearPagamentoContaPagar = true) ");
		}
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.NFENTRADA)) {
			sqlStr.append(" AND (fechamentoMes.bloquearInclusaoAlteracaoNotaFiscalEntrada = true) ");
		}
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.NFSAIDA)) {
			sqlStr.append(" AND (fechamentoMes.bloquearInclusaoAlteracaoNotaFiscalSaida = true) ");
		}
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.MOVIMENTACAOFINANCEIRA)) {
			sqlStr.append(" AND (fechamentoMes.bloquearMovimentacoesFinanceiras = true) ");
		}
		if (tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.CAIXA)) {
			sqlStr.append(" AND ((fechamentoMes.bloquearAberturaTodasContaCaixa is true) OR ");
			sqlStr.append("    	 (FechamentoMesContaCaixa.contaCaixa = ").append(contaCaixa).append("))");
		}
		
		if ((listaUnidadesEnsino == null) || (listaUnidadesEnsino.isEmpty())) {
			throw new Exception("Deve ser informada uma unidade de ensino para verificação de bloqueio / fechamento de uma competência.");
		}
		sqlStr.append(" AND ((fechamentoMes.gerarFechamentoMesTodasUnidades is true) OR ");
		sqlStr.append("      (");
		String condicional = "";
		for (Integer unidadeEnsino : listaUnidadesEnsino) {
			sqlStr.append(condicional);
			sqlStr.append(" (FechamentoMesUnidadeEnsino.unidadeEnsino = ").append(unidadeEnsino).append(")");
			condicional = " OR ";
		}
		sqlStr.append("))");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			Integer codigoFechamentoMesConflito = tabelaResultado.getInt("codigo");
			if (codigoFechamentoMesConflito != null) {
				FechamentoMesVO fchMes = new FechamentoMesVO();
				fchMes.setCodigo(codigoFechamentoMesConflito);
				fchMes.setMes(new Integer(tabelaResultado.getInt("mes")));
				fchMes.setAno(new Integer(tabelaResultado.getInt("ano")));	
				fchMes.setDataUtilizarVerificarBloqueioContaPagar(tabelaResultado.getString("dataUtilizarVerificarBloqueioContaPagar"));
				fchMes.setDataUtilizarVerificarBloqueioContaReceber(tabelaResultado.getString("dataUtilizarVerificarBloqueioContaReceber"));
				fchMes.setDataBloqueioVerificada(dataVerificar);
				if ((tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.APAGAR)) &&
				    (fchMes.getDataUtilizarVerificarBloqueioContaPagar().equals("DC"))) {
					fchMes.setDataBloqueioVerificada(dataCompVerificar);
				}
				if ((tipoOrigem.equals(TipoOrigemHistoricoBloqueioEnum.ARECEBER)) &&
					(fchMes.getDataUtilizarVerificarBloqueioContaReceber().equals("DC"))) {
						fchMes.setDataBloqueioVerificada(dataCompVerificar);
					}
				return fchMes;
			}
		}
		return null;
	}
	
	public void verificarJaExisteFechamentoMesUnidadeEnsino(FechamentoMesVO obj) throws ConsistirException {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT fechamentoMes.codigo FROM fechamentoMes ");
		sqlStr.append("LEFT JOIN FechamentoMesUnidadeEnsino ON FechamentoMesUnidadeEnsino.fechamentoMes = fechamentoMes.codigo ");
		sqlStr.append("LEFT JOIN UnidadeEnsino ON UnidadeEnsino.codigo = FechamentoMesUnidadeEnsino.unidadeEnsino ");
		sqlStr.append("WHERE (1=1) ");
		if (!obj.getPoliticaFechamentoMesPadrao()) {
			sqlStr.append(" AND (fechamentoMes.mes = ").append(obj.getMes()).append(") ");
			sqlStr.append(" AND (fechamentoMes.ano = ").append(obj.getAno()).append(") ");
			sqlStr.append(" AND (fechamentoMes.politicaFechamentoMesPadrao = false) ");
		} else {
			sqlStr.append(" AND (fechamentoMes.politicaFechamentoMesPadrao = true) ");
		}
		sqlStr.append("  AND (fechamentoMes.codigo != ").append(obj.getCodigo()).append(") ");
		//sqlStr.append("  AND (fechamentoMes.fechado = true) ");
		if (!obj.getGerarFechamentoMesTodasUnidades()) {
			// neste caso temos que verificar se existe outro fechamento de competencia que choque
			// na unidade e competencia. Caso contrario, a basta fazer o validacao acima da competencia
			sqlStr.append(" AND ((fechamentoMes.gerarFechamentoMesTodasUnidades is true) OR ");
			sqlStr.append("    	 (unidadeEnsino.codigo in (");
			String virgula = "";
			for (FechamentoMesUnidadeEnsinoVO unidade : obj.getListaUnidadesEnsinoVOs()) {
				sqlStr.append(virgula).append(unidade.getUnidadeEnsino().getCodigo().toString());
				virgula = ", ";
			}
			sqlStr.append("       )))");	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			Integer codigoFechamentoMesConflito = tabelaResultado.getInt("codigo");
			if (codigoFechamentoMesConflito != null) {
				throw new ConsistirException("Já existe um Fechamento de Mês para esta competência e para a(s) mesmas(s) unidade(s) de ensino (Código Fechamento Mês em Conflito: " + codigoFechamentoMesConflito + ")");
			}
		}
	}	

	public List<FechamentoMesVO> consultarPorNomeFantasiaUnidadeEnsino(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT DISTINCT fechamentoMes.* FROM fechamentoMes "
				+ "LEFT JOIN FechamentoMesUnidadeEnsino ON FechamentoMesUnidadeEnsino.fechamentoMes = fechamentoMes.codigo "
				+ "LEFT JOIN UnidadeEnsino ON UnidadeEnsino.codigo = FechamentoMesUnidadeEnsino.unidadeEnsino "
				+ "WHERE (gerarFechamentoMesTodasUnidades is true) OR (upper( UnidadeEnsino.nome ) LIKE('" + valorConsulta.toUpperCase()
				+ "%')) ORDER BY fechamentoMes.ano, fechamentoMes.mes ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<FechamentoMesVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM fechamentoMes WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY ano, mes desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public static List<FechamentoMesVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<FechamentoMesVO> vetResultado = new ArrayList<FechamentoMesVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static FechamentoMesVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FechamentoMesVO obj = new FechamentoMesVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setMes(new Integer(dadosSQL.getInt("mes")));
		obj.setAno(new Integer(dadosSQL.getInt("ano")));
		obj.setFechado(new Boolean(dadosSQL.getBoolean("fechado")));
		obj.getUsuario().setCodigo(new Integer(dadosSQL.getInt("usuario")));
		obj.setDataFechamento(dadosSQL.getDate("dataFechamento"));
		obj.setDataInicioFechamentoMes(dadosSQL.getDate("dataInicioFechamentoMes"));
		obj.setDataFimFechamentoMes(dadosSQL.getDate("dataFimFechamentoMes"));
		obj.setObservacao(dadosSQL.getString("observacao"));
		obj.setPoliticaFechamentoMesPadrao(dadosSQL.getBoolean("politicaFechamentoMesPadrao"));
		obj.setGerarFechamentoMesTodasUnidades(dadosSQL.getBoolean("gerarFechamentoMesTodasUnidades"));
		obj.setFecharParcialmenteMes(dadosSQL.getBoolean("fecharParcialmenteMes"));
		obj.setFecharCompetenciaUltimoDiaMes(dadosSQL.getBoolean("fecharCompetenciaUltimoDiaMes"));
		obj.setFecharCompetenciaDiaEspecifico(dadosSQL.getBoolean("fecharCompetenciaDiaEspecifico"));
		obj.setDiaEspecificoFechaCompetencia(dadosSQL.getInt("diaEspecificoFechaCompetencia"));
		obj.setNrMesesManterAbertoAntesFechamentoAutomatico(dadosSQL.getInt("nrMesesManterAbertoAntesFechamentoAutomatico"));
		obj.setGerarPrevisaoReceitaCompetenciaAutomatimente(dadosSQL.getBoolean("gerarPrevisaoReceitaCompetenciaAutomatimente"));
		obj.setGerarPrevisaoReceitaCompetenciaUltimoDiaMes(dadosSQL.getBoolean("gerarPrevisaoReceitaCompetenciaUltimoDiaMes"));
		obj.setGerarPrevisaoReceitaCompetenciaDiaEspecifico(dadosSQL.getBoolean("gerarPrevisaoReceitaCompetenciaDiaEspecifico"));
		obj.setDiaEspecificoGerarPrevisaoReceitaCompetencia(dadosSQL.getInt("diaEspecificoGerarPrevisaoReceitaCompetencia"));
		obj.setBloquearInclusaoAlteracaoContaReceber(dadosSQL.getBoolean("bloquearInclusaoAlteracaoContaReceber"));
		obj.setBloquearRecebimentoContaReceber(dadosSQL.getBoolean("bloquearRecebimentoContaReceber"));
		obj.setBloquearInclusaoAlteracaoContaPagar(dadosSQL.getBoolean("bloquearInclusaoAlteracaoContaPagar"));
		obj.setBloquearPagamentoContaPagar(dadosSQL.getBoolean("bloquearPagamentoContaPagar"));
		obj.setBloquearInclusaoAlteracaoNotaFiscalEntrada(dadosSQL.getBoolean("bloquearInclusaoAlteracaoNotaFiscalEntrada"));		
		obj.setBloquearInclusaoAlteracaoNotaFiscalSaida(dadosSQL.getBoolean("bloquearInclusaoAlteracaoNotaFiscalSaida"));
		obj.setBloquearMovimentacoesFinanceiras(dadosSQL.getBoolean("bloquearMovimentacoesFinanceiras"));
		obj.setBloquearAberturaTodasContaCaixa(dadosSQL.getBoolean("bloquearAberturaTodasContaCaixa"));
		obj.setBloquearAberturaContasCaixasEspecificas(dadosSQL.getBoolean("bloquearAberturaContasCaixasEspecificas"));
		obj.setDataUtilizarVerificarBloqueioContaPagar(dadosSQL.getString("dataUtilizarVerificarBloqueioContaPagar"));
		obj.setDataUtilizarVerificarBloqueioContaReceber(dadosSQL.getString("dataUtilizarVerificarBloqueioContaReceber"));
		obj.setListaUnidadesEnsinoVOs(getFacadeFactory().getFechamentoMesUnidadeEnsinoFacade().consultarPorFechamentoMes(obj.getCodigo(), false, usuario));
		montarDadosUsuario(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setListaHistoricoModificacaoVOs(getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().consultarPorFechamentoMes(obj.getCodigo(), false, usuario));
		obj.setListaContaCaixaVOs(getFacadeFactory().getFechamentoMesContaCaixaFacade().consultarPorFechamentoMes(obj.getCodigo(), false, usuario));		
		return obj;
	}
	
	public static void montarDadosUsuario(FechamentoMesVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getUsuario().getCodigo().intValue() == 0) {
			return;
		}
		obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
	}	

	public FechamentoMesVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM fechamentoMes WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( fechamentoMes ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public FechamentoMesVO consultarMesFechado(Integer mes, Integer ano, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM fechamentoMes WHERE mes = ? AND ano = ? AND fechado is TRUE";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { mes, ano });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public FechamentoMesVO consultarExisteItemMesAno(Integer mes, Integer ano, Integer nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM fechamentoMes WHERE mes = ? AND ano = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { mes, ano });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public Boolean verificarMesFechado(Integer mes, Integer ano,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT (CASE WHEN (select COUNT(codigo) from fechamentomes where mes = ? AND ano = ? AND fechado) > 0 THEN true ELSE false END)";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { mes, ano });
		if (!tabelaResultado.next()) {
			return null;
		}
		return (new Boolean(tabelaResultado.getBoolean(1)));
	}

	public static String getIdEntidade() {
		return FechamentoMes.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		FechamentoMes.idEntidade = idEntidade;
	}
	
	public void verificarEGerarHistoricoModificacoesFechamentoMes(FechamentoMesVO fechamentoMesAlterado, FechamentoMesVO fechamentoMesAnterior, UsuarioVO usuario) throws Exception {
		if (!fechamentoMesAnterior.getFechado()) {
			// como o objeto anterior ainda nao estava fechado, nao temos por que controlar
			// a questao do historico de modificacao. Pois o mesmo ainda nao estava sendo aplicado.
			return;
		}
		StringBuilder histModCampos = new StringBuilder(); 
		if (!fechamentoMesAlterado.getFechado().equals(fechamentoMesAnterior.getFechado())) {
			histModCampos.append("Campo FECHADO alterado de ").append(fechamentoMesAnterior.getFechado().toString()).append(" para ").append(fechamentoMesAlterado.getFechado().toString());
		}
		if (!fechamentoMesAlterado.getGerarFechamentoMesTodasUnidades().equals(fechamentoMesAnterior.getGerarFechamentoMesTodasUnidades())) {
			histModCampos.append("Campo GERAR FECHAMENTO MÊS TODAS UNIDADES alterado de ").append(fechamentoMesAnterior.getGerarFechamentoMesTodasUnidades().toString()).append(" para ").append(fechamentoMesAlterado.getGerarFechamentoMesTodasUnidades().toString());
		}
		if (!fechamentoMesAlterado.getFecharParcialmenteMes().equals(fechamentoMesAnterior.getFecharParcialmenteMes())) {
			histModCampos.append("Campo FECHAR PARCEIALMENTE MÊS alterado de ").append(fechamentoMesAnterior.getFecharParcialmenteMes().toString()).append(" para ").append(fechamentoMesAlterado.getFecharParcialmenteMes().toString());
		}
		if (!Uteis.getData(fechamentoMesAlterado.getDataInicioFechamentoMes()).equals(Uteis.getData(fechamentoMesAnterior.getDataInicioFechamentoMes()))) {
			histModCampos.append("Campo DATA INÍCIO FECHAMENTO alterado de ").append(Uteis.getData(fechamentoMesAnterior.getDataInicioFechamentoMes())).append(" para ").append(Uteis.getData(fechamentoMesAlterado.getDataInicioFechamentoMes()));
		}		
		if (!Uteis.getData(fechamentoMesAlterado.getDataFimFechamentoMes()).equals(Uteis.getData(fechamentoMesAnterior.getDataFimFechamentoMes()))) {
			histModCampos.append("Campo DATA FIM FECHAMENTO alterado de ").append(Uteis.getData(fechamentoMesAnterior.getDataFimFechamentoMes())).append(" para ").append(Uteis.getData(fechamentoMesAlterado.getDataFimFechamentoMes()));
		}		
		if (!fechamentoMesAlterado.getFecharCompetenciaUltimoDiaMes().equals(fechamentoMesAnterior.getFecharCompetenciaUltimoDiaMes())) {
			histModCampos.append("Campo FECHAR COMPETENCIA ÚLTIMO DIA MÊS alterado de ").append(fechamentoMesAnterior.getFecharCompetenciaUltimoDiaMes().toString()).append(" para ").append(fechamentoMesAlterado.getFecharCompetenciaUltimoDiaMes().toString());
		}
		if (!fechamentoMesAlterado.getFecharCompetenciaDiaEspecifico().equals(fechamentoMesAnterior.getFecharCompetenciaDiaEspecifico())) {
			histModCampos.append("Campo FECHAR COMPETENCIA DIA ESPECÍFICO alterado de ").append(fechamentoMesAnterior.getFecharCompetenciaDiaEspecifico().toString()).append(" para ").append(fechamentoMesAlterado.getFecharCompetenciaDiaEspecifico().toString());
		}
		if (!fechamentoMesAlterado.getDiaEspecificoFechaCompetencia().equals(fechamentoMesAnterior.getDiaEspecificoFechaCompetencia())) {
			histModCampos.append("Campo DIA ESPECÍFICO FECHAR COMPETÊNCIA alterado de ").append(fechamentoMesAnterior.getDiaEspecificoFechaCompetencia().toString()).append(" para ").append(fechamentoMesAlterado.getDiaEspecificoFechaCompetencia().toString());
		}		
		if (!fechamentoMesAlterado.getNrMesesManterAbertoAntesFechamentoAutomatico().equals(fechamentoMesAnterior.getNrMesesManterAbertoAntesFechamentoAutomatico())) {
			histModCampos.append("Campo NR. MESES MANTER ABERTO alterado de ").append(fechamentoMesAnterior.getNrMesesManterAbertoAntesFechamentoAutomatico().toString()).append(" para ").append(fechamentoMesAlterado.getNrMesesManterAbertoAntesFechamentoAutomatico().toString());
		}		
		if (!fechamentoMesAlterado.getGerarPrevisaoReceitaCompetenciaAutomatimente().equals(fechamentoMesAnterior.getGerarPrevisaoReceitaCompetenciaAutomatimente())) {
			histModCampos.append("Campo GERAR PREVISÃO RECEITA COMPET. AUTOMATICAMENTE alterado de ").append(fechamentoMesAnterior.getGerarPrevisaoReceitaCompetenciaAutomatimente().toString()).append(" para ").append(fechamentoMesAlterado.getGerarPrevisaoReceitaCompetenciaAutomatimente().toString());
		}
		if (!fechamentoMesAlterado.getGerarPrevisaoReceitaCompetenciaUltimoDiaMes().equals(fechamentoMesAnterior.getGerarPrevisaoReceitaCompetenciaUltimoDiaMes())) {
			histModCampos.append("Campo GERAR PREVISÃO RECEITA ÚLTIMO DIA MÊS alterado de ").append(fechamentoMesAnterior.getGerarPrevisaoReceitaCompetenciaUltimoDiaMes().toString()).append(" para ").append(fechamentoMesAlterado.getGerarPrevisaoReceitaCompetenciaUltimoDiaMes().toString());
		}
		if (!fechamentoMesAlterado.getGerarPrevisaoReceitaCompetenciaDiaEspecifico().equals(fechamentoMesAnterior.getGerarPrevisaoReceitaCompetenciaDiaEspecifico())) {
			histModCampos.append("Campo GERAR PREVISÃO RECEITA DIA ESPECÍFICO alterado de ").append(fechamentoMesAnterior.getGerarPrevisaoReceitaCompetenciaDiaEspecifico().toString()).append(" para ").append(fechamentoMesAlterado.getGerarPrevisaoReceitaCompetenciaDiaEspecifico().toString());
		}
		if (!fechamentoMesAlterado.getDiaEspecificoGerarPrevisaoReceitaCompetencia().equals(fechamentoMesAnterior.getDiaEspecificoGerarPrevisaoReceitaCompetencia())) {
			histModCampos.append("Campo DIA ESPECÍFICO GERAR PREVISÃO RECEITA alterado de ").append(fechamentoMesAnterior.getDiaEspecificoGerarPrevisaoReceitaCompetencia().toString()).append(" para ").append(fechamentoMesAlterado.getDiaEspecificoGerarPrevisaoReceitaCompetencia().toString());
		}
		if (!fechamentoMesAlterado.getBloquearInclusaoAlteracaoContaReceber().equals(fechamentoMesAnterior.getBloquearInclusaoAlteracaoContaReceber())) {
			histModCampos.append("Campo BLOQUEAR INCLUSÃO / ALTERAÇÃO CONTA A RECEBER alterado de ").append(fechamentoMesAnterior.getBloquearInclusaoAlteracaoContaReceber().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearInclusaoAlteracaoContaReceber().toString());
		}
		if (!fechamentoMesAlterado.getBloquearRecebimentoContaReceber().equals(fechamentoMesAnterior.getBloquearRecebimentoContaReceber())) {
			histModCampos.append("Campo BLOQUEAR RECEBIMENTO DE UMA CONTA A RECEBER alterado de ").append(fechamentoMesAnterior.getBloquearRecebimentoContaReceber().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearRecebimentoContaReceber().toString());
		}
		if (!fechamentoMesAlterado.getBloquearInclusaoAlteracaoContaPagar().equals(fechamentoMesAnterior.getBloquearInclusaoAlteracaoContaPagar())) {
			histModCampos.append("Campo BLOQUEAR INCLUSÃO / ALTERAÇÃO CONTA A PAGAR alterado de ").append(fechamentoMesAnterior.getBloquearInclusaoAlteracaoContaPagar().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearInclusaoAlteracaoContaPagar().toString());
		}		
		if (!fechamentoMesAlterado.getBloquearPagamentoContaPagar().equals(fechamentoMesAnterior.getBloquearPagamentoContaPagar())) {
			histModCampos.append("Campo BLOQUEAR PAGAMENTO DE UMA CONTA A PAGAR alterado de ").append(fechamentoMesAnterior.getBloquearPagamentoContaPagar().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearPagamentoContaPagar().toString());
		}		
		if (!fechamentoMesAlterado.getBloquearInclusaoAlteracaoNotaFiscalEntrada().equals(fechamentoMesAnterior.getBloquearInclusaoAlteracaoNotaFiscalEntrada())) {
			histModCampos.append("Campo BLOQUEAR INCLUSÃO / ALTERAÇÃO DE NF DE ENTRADA alterado de ").append(fechamentoMesAnterior.getBloquearInclusaoAlteracaoNotaFiscalEntrada().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearInclusaoAlteracaoNotaFiscalEntrada().toString());
		}	
		if (!fechamentoMesAlterado.getBloquearInclusaoAlteracaoNotaFiscalSaida().equals(fechamentoMesAnterior.getBloquearInclusaoAlteracaoNotaFiscalSaida())) {
			histModCampos.append("Campo BLOQUEAR INCLUSÃO / ALTERAÇÃO DE NF DE SAÍDA alterado de ").append(fechamentoMesAnterior.getBloquearInclusaoAlteracaoNotaFiscalSaida().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearInclusaoAlteracaoNotaFiscalSaida().toString());
		}
		if (!fechamentoMesAlterado.getBloquearMovimentacoesFinanceiras().equals(fechamentoMesAnterior.getBloquearMovimentacoesFinanceiras())) {
			histModCampos.append("Campo BLOQUEAR MOVIMENTAÇÕES FINANCEIRAS alterado de ").append(fechamentoMesAnterior.getBloquearMovimentacoesFinanceiras().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearMovimentacoesFinanceiras().toString());
		}
		if (!fechamentoMesAlterado.getBloquearAberturaTodasContaCaixa().equals(fechamentoMesAnterior.getBloquearAberturaTodasContaCaixa())) {
			histModCampos.append("Campo BLOQUEAR ABERTURA TODAS CONTAS CAIXA alterado de ").append(fechamentoMesAnterior.getBloquearAberturaTodasContaCaixa().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearAberturaTodasContaCaixa().toString());
		}		
		if (!fechamentoMesAlterado.getBloquearAberturaContasCaixasEspecificas().equals(fechamentoMesAnterior.getBloquearAberturaContasCaixasEspecificas())) {
			histModCampos.append("Campo BLOQUEAR ABERTURA CONTAS CAIXAS ESPECÍFICAS alterado de ").append(fechamentoMesAnterior.getBloquearAberturaContasCaixasEspecificas().toString()).append(" para ").append(fechamentoMesAlterado.getBloquearAberturaContasCaixasEspecificas().toString());
		}		
		if (!fechamentoMesAlterado.getDataUtilizarVerificarBloqueioContaPagar().equals(fechamentoMesAnterior.getDataUtilizarVerificarBloqueioContaPagar())) {
			histModCampos.append("Campo DATA UTILIZAR VERIFICAR BLOQUEIO CONTAS A PAGAR alterado de ").append(fechamentoMesAnterior.getDataUtilizarVerificarBloqueioContaPagar_Apresentar()).append(" para ").append(fechamentoMesAlterado.getDataUtilizarVerificarBloqueioContaPagar_Apresentar());
		}	
		if (!fechamentoMesAlterado.getDataUtilizarVerificarBloqueioContaReceber().equals(fechamentoMesAnterior.getDataUtilizarVerificarBloqueioContaReceber())) {
			histModCampos.append("Campo DATA UTILIZAR VERIFICAR BLOQUEIO CONTAS A RECEBER alterado de ").append(fechamentoMesAnterior.getDataUtilizarVerificarBloqueioContaReceber_Apresentar()).append(" para ").append(fechamentoMesAlterado.getDataUtilizarVerificarBloqueioContaReceber_Apresentar());
		}			
		if (!histModCampos.toString().equals("")) {
			registrarNovoHistoricoModificacao(fechamentoMesAlterado, usuario, TipoOrigemHistoricoBloqueioEnum.FECHAMENTOMES,
					"Foram Alterados Parametros Importantes do Fechamento do Mês", 
					histModCampos.toString() );
		}
		
		// verificando se as unidades de ensino foram alteradas
		StringBuilder unidadesAposAlteracao = new StringBuilder(); 
		for (FechamentoMesUnidadeEnsinoVO obj1 : fechamentoMesAlterado.getListaUnidadesEnsinoVOs()) {
			unidadesAposAlteracao.append(obj1.getUnidadeEnsino().getCodigo()).append(";");
		}
		StringBuilder unidadesAnterior = new StringBuilder();
		for (FechamentoMesUnidadeEnsinoVO obj2 : fechamentoMesAnterior.getListaUnidadesEnsinoVOs()) {
			unidadesAnterior.append(obj2.getUnidadeEnsino().getCodigo()).append(";");
		}
		if (!unidadesAposAlteracao.toString().equals(unidadesAnterior.toString())) {
			// se entrarmos aqui é por que houve alteracoes na lista de unidade. Logo, temos que gerar
			// um registro desta situação.
			registrarNovoHistoricoModificacao(fechamentoMesAlterado, usuario, TipoOrigemHistoricoBloqueioEnum.FECHAMENTOMES,
					"Foram Alteradas as Unidades de Ensino Selecionadas no Fechamento Mês", 
					"Unidades de Ensino Selecionadas Alteradas de [" + unidadesAnterior.toString() + "] para [" + unidadesAposAlteracao.toString() + "]. " );
		}
		
		// verificando se as contas caixas foram alteradas
		StringBuilder caixasAposAlteracao = new StringBuilder(); 
		for (FechamentoMesContaCaixaVO obj1 : fechamentoMesAlterado.getListaContaCaixaVOs()) {
			caixasAposAlteracao.append(obj1.getContaCaixa().getCodigo()).append(";");
		}
		StringBuilder caixasAnterior = new StringBuilder();
		for (FechamentoMesContaCaixaVO obj2 : fechamentoMesAnterior.getListaContaCaixaVOs()) {
			caixasAnterior.append(obj2.getContaCaixa().getCodigo()).append(";");
		}
		if (!caixasAposAlteracao.toString().equals(caixasAnterior.toString())) {
			// se entrarmos aqui é por que houve alteracoes na lista de unidade. Logo, temos que gerar
			// um registro desta situação.
			registrarNovoHistoricoModificacao(fechamentoMesAlterado, usuario, TipoOrigemHistoricoBloqueioEnum.FECHAMENTOMES,
					"Foram Alteradas as Contas Caixas Selecionadas no Fechamento Mês", 
					"Unidades de Ensino Selecionadas Alteradas de [" + caixasAnterior.toString() + "] para [" + caixasAposAlteracao.toString() + "]. " );
		}		
		
	}
	
	public void registrarNovoHistoricoModificacao(FechamentoMesVO fechamentoMes, UsuarioVO usuario, TipoOrigemHistoricoBloqueioEnum tipoOrigem, String descricao, String detalhe) {
		FechamentoMesHistoricoModificacaoVO historico = getFacadeFactory().getFechamentoMesHistoricoModificacaoFacade().gerarNovoHistoricoModificacao(fechamentoMes, usuario, tipoOrigem, descricao, detalhe);
		fechamentoMes.getListaHistoricoModificacaoVOs().add(historico);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarPrevisaoFaturamentoCompetencia(Date data, FechamentoMesVO obj, UsuarioVO usuario) throws Exception {
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerPrevisaoFaturamentoCompetencia(Date data, FechamentoMesVO obj, UsuarioVO usuario) throws Exception {
		
	}	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void fecharCompetencia(FechamentoMesVO obj, Boolean gerarPrevisaoFaturamento, UsuarioVO usuario) throws Exception {
		obj.setFechado(Boolean.TRUE);
		obj.setDataFechamento(new Date());
		registrarNovoHistoricoModificacao(obj, usuario, TipoOrigemHistoricoBloqueioEnum.FECHAMENTOMES, "Fechamento da Competência Realizado com Sucesso!", "");
		alterar(obj, false, usuario);
		getFacadeFactory().getFechamentoFinanceiroFacade().processarDadosParaFechamentoFinanceiro(obj, usuario);
		if (gerarPrevisaoFaturamento) {
			gerarPrevisaoFaturamentoCompetencia(obj.getDataFechamento(), obj, usuario);
		}
	}	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void reabrirCompetencia(FechamentoMesVO obj, Boolean removerPrevisaoFaturamento, UsuarioVO usuario) throws Exception {
		obj.setFechado(Boolean.FALSE);
		registrarNovoHistoricoModificacao(obj, usuario, TipoOrigemHistoricoBloqueioEnum.FECHAMENTOMES, "Competência REABERTA com sucesso em " + Uteis.getDataComHora(new Date()), "Competência reaberta havia sido fechada em " + Uteis.getDataComHora(obj.getDataFechamento()));
		obj.setDataFechamento(null);
		alterar(obj, false, usuario);
			
		if (removerPrevisaoFaturamento) {
			removerPrevisaoFaturamentoCompetencia(obj.getDataFechamento(), obj, usuario);
		}
	}	
}