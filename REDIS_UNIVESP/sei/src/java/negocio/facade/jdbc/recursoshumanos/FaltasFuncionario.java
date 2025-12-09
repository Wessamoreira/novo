package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FaltasFuncionarioVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoFaltaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.Conexao;
import negocio.interfaces.recursoshumanos.FaltasFuncionarioInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>FaltasFuncionarioVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>FaltasFuncionarioVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class FaltasFuncionario extends SuperFacade<FaltasFuncionarioVO> implements FaltasFuncionarioInterfaceFacade<FaltasFuncionarioVO> {

	private static final long serialVersionUID = 4011234606222123574L;

	protected static String idEntidade;

	public FaltasFuncionario() throws Exception {
		super();
		setIdEntidade("FaltasFuncionario");
	}
	
	public FaltasFuncionario(Conexao conexao) {
		super();
		setConexao(conexao);
		setIdEntidade("FaltasFuncionario");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistirTodos(List<FaltasFuncionarioVO> listaSalarioComposto, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluirPorFuncionarioCargo(funcionarioCargo, false, usuarioVO);

		for (FaltasFuncionarioVO salarioCompostoVO : listaSalarioComposto) {
			salarioCompostoVO.setCodigo(0);
			salarioCompostoVO.setFuncionarioCargo(funcionarioCargo);
			persistir(salarioCompostoVO, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void persistir(FaltasFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(FaltasFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			FaltasFuncionario.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.faltasfuncionario(funcionariocargo, datainicio, tipofalta, motivo, integral, ");
					sql.append(" debitado)");
					sql.append(" VALUES (?, ?, ?, ?, ?, ");
					sql.append(" ?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataInicio(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoFalta(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMotivo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getIntegral(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getDebitado(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(final ResultSet arg0) throws SQLException {
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

	@Override
	public void alterar(FaltasFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		FaltasFuncionario.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.faltasfuncionario SET funcionariocargo=?, datainicio=?, tipofalta=?, motivo=?, integral=?, ");
				sql.append(" debitado=? ");
				sql.append(" WHERE codigo=?");
				sql.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataInicio(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoFalta(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getMotivo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getIntegral(), ++i, sqlAlterar);

				Uteis.setValuePreparedStatement(obj.getDebitado(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	public void excluir(FaltasFuncionarioVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		FaltasFuncionario.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM faltasfuncionario WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM faltasfuncionario WHERE (funcionariocargo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public FaltasFuncionarioVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE FaltasFuncionario.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(Date dataInicio, FuncionarioCargoVO funcionarioCargo, FaltasFuncionarioVO obj) throws Exception {
		validarDados(obj);
	}

	public Integer consultarPorDataInicioEDataFim(Date dataInicio, Date dataFim, FuncionarioCargoVO funcionarioCargo, FaltasFuncionarioVO obj) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(faltasfuncionario.codigo) AS qtde FROM faltasfuncionario");
		sql.append(" INNER JOIN funcionariocargo ON funcionariocargo.codigo = faltasfuncionario.funcionariocargo");
		sql.append(" WHERE (datainicio BETWEEN ? AND ? OR datafim BETWEEN ? AND ?)");
		sql.append(" AND funcionariocargo.matriculacargo = ? AND faltasfuncionario.codigo != ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataInicio, dataFim, dataInicio,  dataFim, funcionarioCargo.getMatriculaCargo(), obj.getCodigo());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	private StringBuilder getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM public.FaltasFuncionario");
		return sql;
	}

	@Override
	public FaltasFuncionarioVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		FaltasFuncionarioVO obj = new FaltasFuncionarioVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setFuncionarioCargo(Uteis.montarDadosVO(tabelaResultado.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_COMBOBOX, null)));
		obj.setDataInicio(tabelaResultado.getDate("datainicio"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipofalta"))) {
			obj.setTipoFalta(TipoFaltaEnum.valueOf(tabelaResultado.getString("tipofalta")));
		}

		obj.setMotivo(tabelaResultado.getString("motivo"));
		obj.setIntegral(tabelaResultado.getBoolean("integral"));
		
		obj.setDebitado(tabelaResultado.getBoolean("debitado"));

		return obj;
	}

	@Override
	public void validarDados(FaltasFuncionarioVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FaltaFuncionaro_funcionarioCargo"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getDataInicio())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FaltaFuncionaro_dataInicio"));
		}

		if (!Uteis.isAtributoPreenchido(obj.getTipoFalta())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_FaltaFuncionaro_tipoFalta"));
		}
	}

	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		dataModelo.setListaConsulta(getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario));
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario));
	}

	@Override
	public List<FaltasFuncionarioVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE FaltasFuncionario.funcionariocargo = ?");
		sql.append(" order by FaltasFuncionario.datainicio desc, FaltasFuncionario.tipofalta asc ");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo());
		
		List<FaltasFuncionarioVO> lista = new ArrayList<>();
		while(rs.next()) {
			lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return lista;
	}
	
	@Override
	public List<FaltasFuncionarioVO> consultarTotalFaltasPeriodo() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT faltas.funcionariocargo, COUNT(faltas.codigo) as faltasfuncionario FROM faltasfuncionario AS faltas");
		sql.append(" INNER JOIN funcionariocargo AS fc ON faltas.funcionariocargo = fc.codigo");
		sql.append(" WHERE fc.situacaofuncionario = 'ATIVO' AND ");
		sql.append(" (SELECT count(codigo) from periodoaquisitivoferias as periodo where situacao = 'ABERTO' AND periodo.funcionariocargo = fc.codigo ");
		sql.append(" AND (faltas.datainicio >= periodo.inicioperiodo AND faltas.datainicio <= periodo.finalperiodo  )");
		sql.append(" ) != 0 ");
		sql.append(" GROUP BY faltas.funcionariocargo;");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<FaltasFuncionarioVO> lista = new ArrayList<>();
		while(rs.next()) {
			FaltasFuncionarioVO obj = new FaltasFuncionarioVO();
			obj.setTotalFaltasfuncionario(rs.getInt("faltasfuncionario"));
			obj.setFuncionarioCargo(Uteis.montarDadosVO(rs.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_COMBOBOX, null)));
			lista.add(obj);
		}
		return lista;
	}
	
	@Override
	public Integer consultarQtdFaltasDoPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo, Boolean integral) {
		
		StringBuilder sql = new StringBuilder("select count(codigo), tipofalta from faltasfuncionario ")
				.append(" where funcionariocargo = ? and tipofalta not like '").append(TipoFaltaEnum.JUSTIFICADA.getValor()).append("'")
				.append(" and datainicio between ? and ? ")
				.append(" and integral = ? ")
				.append(" group by tipofalta");
		
		List<Object> filtros = new ArrayList<>();
		filtros.add(funcionarioCargo.getCodigo());
		filtros.add(UteisData.getDataSemHora(inicioPeriodo));
		filtros.add(finalPeriodo);
		filtros.add(integral);
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		Integer qtdFalta = 0;
		while(rs.next()) {
			
			if(rs.getString("tipofalta") != null && rs.getString("tipofalta").equals(TipoFaltaEnum.INJUSTIFICADA.getValor())) {
				qtdFalta += rs.getInt("count"); 
			} else {
				qtdFalta -= rs.getInt("count");
			}
		}
		
		return qtdFalta < 0 ? 0 : qtdFalta;
	}

	@Override
	public Integer consultarQtdFaltasDoPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo) {
		return consultarQtdFaltasDoPeriodo(funcionarioCargo, inicioPeriodo, finalPeriodo, true);
	}

	/**
	 * Caso o funcionario tenha 15 dias ou mais de faltas injustificadas, o mesmo perde o direito a 1 avo
	 * Retorna a qtd de meses que o mesmo teve mais que 15 dias
	 */
	@Override
	public Integer consultarQtdPeriodoPerdidoFaltasDoFuncionarioCargoPorPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo) {
		
		StringBuilder sql = new StringBuilder(" select count(competencia) as qdtPeriodoPerdido from ");
		sql.append(" ( select to_char(datainicio, 'yyyy-MM') as competencia,  "); 
		sql.append(" sum(case when tipofalta = 'INJUSTIFICADA' then 1 else 0 end) - sum(case when tipofalta = 'ESTORNO' then 1 else 0 end) as faltas ");
		sql.append(" from faltasfuncionario where funcionariocargo = ? ");
		sql.append(" and tipofalta in ('INJUSTIFICADA', 'ESTORNO') ");
		sql.append(" and datainicio between ? and ? ");
		sql.append(" group by to_char(datainicio, 'yyyy-MM') ");
		sql.append(" ) as t where faltas >= 15 ");
				
		List<Object> filtros = new ArrayList<>();
		filtros.add(funcionarioCargo.getCodigo());
		filtros.add(UteisData.getDataSemHora(inicioPeriodo));
		filtros.add(UteisData.getDataSemHora(finalPeriodo));
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		Integer qtdFalta = 0;
		if(rs.next()) {
			qtdFalta = rs.getInt("qdtPeriodoPerdido");
		}
		
		return qtdFalta < 0 ? 0 : qtdFalta;
	}

	@Override
	public SqlRowSet consultarQuantidadeDeFaltasDoFuncionarioCargoPorPeriodo(FuncionarioCargoVO funcionarioCargo, Date inicioPeriodo, Date finalPeriodo) {
		
		StringBuilder sql = new StringBuilder(" select to_char(datainicio, 'MM') as mesFalta,  "); 
		sql.append(" sum(case when tipofalta = 'INJUSTIFICADA' then 1 else 0 end) - sum(case when tipofalta = 'ESTORNO' then 1 else 0 end) as faltas ");
		sql.append(" from faltasfuncionario where funcionariocargo = ? ");
		sql.append(" and tipofalta in ('INJUSTIFICADA', 'ESTORNO') ");
		sql.append(" and datainicio between ? and ? ");
		sql.append(" group by to_char(datainicio, 'MM') ");
		sql.append(" order by mesFalta ");
				
		List<Object> filtros = new ArrayList<>();
		filtros.add(funcionarioCargo.getCodigo());
		filtros.add(UteisData.getDataSemHora(inicioPeriodo));
		filtros.add(UteisData.getDataSemHora(finalPeriodo));
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		if(rs.next())
			return rs;
		
		return null;
	}
	
	public void adicionarEventosDeFaltasFuncionario(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoAtiva, FuncionarioCargoVO funcionarioCargo, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario) throws Exception {		
		if (funcionarioCargo.getSindicatoVO().getValidarFaltas()) {
			Date inicioPeriodo = UteisData.getPrimeiroDataMes(competenciaFolhaPagamentoAtiva.getDataCompetencia());
			Date finalPeriodo = UteisData.getUltimaDataMes(competenciaFolhaPagamentoAtiva.getDataCompetencia());
	
			adicionarEventoDeFaltaEDSR(funcionarioCargo, listaDeEventosDoFuncionario, inicioPeriodo, finalPeriodo);
		}
	}

	private void adicionarEventoDeFaltaEDSR(FuncionarioCargoVO funcionarioCargo, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, Date inicioPeriodo, Date finalPeriodo)	throws Exception {
		
		Integer quantidadeFaltas = getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarQtdFaltasDoPeriodo(funcionarioCargo, inicioPeriodo, finalPeriodo, false);
		if (quantidadeFaltas > 0) {
			if (Uteis.isAtributoPreenchido(funcionarioCargo.getSindicatoVO().getEventoLancamentoFalta().getCodigo())) {
				EventoFolhaPagamentoVO eventoLancamentoFaltas = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(funcionarioCargo.getSindicatoVO().getEventoLancamentoFalta().getCodigo(), null, Uteis.NIVELMONTARDADOS_TODOS);
				if(!listaDeEventosDoFuncionario.contains(eventoLancamentoFaltas))
					listaDeEventosDoFuncionario.add(eventoLancamentoFaltas);
			}

			adicionarEventoDeDSR(funcionarioCargo, listaDeEventosDoFuncionario, inicioPeriodo, finalPeriodo);
		}
	}

	private void adicionarEventoDeDSR(FuncionarioCargoVO funcionarioCargo, List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, Date inicioPeriodo, Date finalPeriodo) throws Exception {
		Integer quantidadeFaltas = getFacadeFactory().getFaltasFuncionarioInterfaceFacade().consultarQtdFaltasDoPeriodo(funcionarioCargo, inicioPeriodo, finalPeriodo, true);
		if (quantidadeFaltas > 0) {
			if (Uteis.isAtributoPreenchido(funcionarioCargo.getSindicatoVO().getEventoDSRPerdida().getCodigo())) {
				EventoFolhaPagamentoVO eventoDSRPerdida = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(funcionarioCargo.getSindicatoVO().getEventoDSRPerdida().getCodigo(), null, Uteis.NIVELMONTARDADOS_TODOS);
				if(!listaDeEventosDoFuncionario.contains(eventoDSRPerdida))
					listaDeEventosDoFuncionario.add(eventoDSRPerdida);
			}
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		FaltasFuncionario.idEntidade = idEntidade;
	}

}