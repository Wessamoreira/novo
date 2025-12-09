package negocio.facade.jdbc.recursoshumanos;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoValeTransporteFuncionarioCargoVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.EventoValeTransporteFuncionarioCargoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>EventoValeTransporteFuncionarioCargoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>EventoValeTransporteFuncionarioCargoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class EventoValeTransporteFuncionarioCargo extends SuperFacade<EventoValeTransporteFuncionarioCargoVO> implements EventoValeTransporteFuncionarioCargoInterfaceFacade<EventoValeTransporteFuncionarioCargoVO> {

	private static final long serialVersionUID = 4011234606222123574L;

	protected static String idEntidade;

	public EventoValeTransporteFuncionarioCargo() throws Exception {
		super();
		setIdEntidade("EventoValeTransporteFuncionarioCargo");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistirTodos(List<EventoValeTransporteFuncionarioCargoVO> listaSalarioComposto, FuncionarioCargoVO funcionarioCargo, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluirPorFuncionarioCargo(funcionarioCargo, false, usuarioVO);

		for (EventoValeTransporteFuncionarioCargoVO salarioCompostoVO : listaSalarioComposto) {
			salarioCompostoVO.setCodigo(0);
			salarioCompostoVO.setFuncionarioCargo(funcionarioCargo);
			persistir(salarioCompostoVO, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void persistir(EventoValeTransporteFuncionarioCargoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(EventoValeTransporteFuncionarioCargoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoValeTransporteFuncionarioCargo.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.eventovaletransportefuncionariocargo(");
					sql.append(" funcionariocargo, parametrovaletransporte, numeroviagensdia, diasuteis, utilizasalarionominal, ");
					sql.append(" numeroviagensmeioexpediente, quantidadeViagensMeioExpediente)");
					sql.append(" VALUES (?, ?, ?, ?, ?, ");
					sql.append(" ?, ?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					
					Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getParametroValeTransporte(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumeroViagensDia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDiasUteisMes(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUtilizaSalarioNominal(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getNumeroViagensMeioExpediente(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidadeViagensMeioExpediente(), ++i, sqlInserir);

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

	@Override
	public void alterar(EventoValeTransporteFuncionarioCargoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoValeTransporteFuncionarioCargo.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.eventovaletransporteFuncionariocargo SET ");
				sql.append(" funcionariocargo=?, parametrovaletransporte=?, numeroviagensdia=?, diasuteis=?, utilizasalarionominal=?, "); 
				sql.append(" numeroviagensmeioexpediente=?, quantidadeViagensMeioExpediente=?");
				sql.append(" WHERE codigo=?");
				sql.append( adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getFuncionarioCargo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getParametroValeTransporte(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getNumeroViagensDia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDiasUteisMes(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getUtilizaSalarioNominal(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getNumeroViagensMeioExpediente(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getQuantidadeViagensMeioExpediente(), ++i, sqlAlterar);

				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	public void excluir(EventoValeTransporteFuncionarioCargoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoValeTransporteFuncionarioCargo.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM eventovaletransporteFuncionariocargo WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void excluirPorFuncionarioCargo(FuncionarioCargoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM eventovaletransporteFuncionariocargo WHERE (funcionariocargo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public EventoValeTransporteFuncionarioCargoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE eventovaletransporteFuncionariocargo.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	private StringBuilder getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM public.eventovaletransporteFuncionariocargo");
		return sql;
	}

	@Override
	public EventoValeTransporteFuncionarioCargoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		EventoValeTransporteFuncionarioCargoVO obj = new EventoValeTransporteFuncionarioCargoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setParametroValeTransporte(Uteis.montarDadosVO(tabelaResultado.getInt("parametrovaletransporte"), ParametroValeTransporteVO.class, 
				p -> getFacadeFactory().getParametroValeTransporteInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("parametrovaletransporte"))));
		obj.setNumeroViagensDia(tabelaResultado.getInt("numeroviagensdia"));
		obj.setDiasUteisMes(tabelaResultado.getInt("diasUteis"));
		obj.setUtilizaSalarioNominal(tabelaResultado.getBoolean("utilizaSalarioNominal"));
		
		obj.setNumeroViagensMeioExpediente(tabelaResultado.getInt("numeroViagensMeioExpediente"));
		obj.setQuantidadeViagensMeioExpediente(tabelaResultado.getInt("quantidadeViagensMeioExpediente"));
		
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS)
			return obj;
		
		obj.setFuncionarioCargo(Uteis.montarDadosVO(tabelaResultado.getInt("funcionariocargo"), FuncionarioCargoVO.class, p -> getFacadeFactory().getFuncionarioCargoFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_COMBOBOX, null)));
		
		return obj;
	}

	@Override
	public void validarDados(EventoValeTransporteFuncionarioCargoVO obj) throws ConsistirException {
		
		if (!Uteis.isAtributoPreenchido(obj.getFuncionarioCargo().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		List<FuncionarioCargoVO> objs = new ArrayList<>();

		objs = getFacadeFactory().getFuncionarioCargoFacade().consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario);
		dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getFuncionarioCargoFacade().consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario));

		dataModelo.setListaConsulta(objs);
	}

	@Override
	public List<EventoValeTransporteFuncionarioCargoVO> consultarPorFuncionarioCargo(FuncionarioCargoVO funcionarioCargoVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlBasico());
		sql.append(" WHERE eventovaletransporteFuncionariocargo.funcionariocargo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), funcionarioCargoVO.getCodigo());
		
		List<EventoValeTransporteFuncionarioCargoVO> lista = new ArrayList<>();
		while(rs.next()) {
			lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return lista;
	}

	@Override
	public void adicionarEventosDeValeTransporte(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select param.codigo as linhatransporte, e.codigo as eventofolhapagamento, cce.codigo as contraChequeEvento, vt.* from eventovaletransportefuncionariocargo vt ");
		sql.append(" inner join parametrovaletransporte param on param.codigo = vt.parametrovaletransporte ");
		sql.append(" inner join eventofolhapagamento e on e.codigo = param.eventofolhapagamento ");
		sql.append(" left join contrachequeevento cce on cce.eventoFolhaPagamento = e.codigo and cce.contracheque = ? "); 
		sql.append(" where vt.funcionarioCargo = ? ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contraChequeVO.getCodigo(), funcionarioCargo.getCodigo());

		while(tabelaResultado.next()) {
			try {
				EventoFolhaPagamentoVO obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().montarDadosDoEventoParaContraCheque(
						tabelaResultado.getInt("eventofolhapagamento"), tabelaResultado.getInt("contraChequeEvento"));
				if(Uteis.isAtributoPreenchido(obj) && !Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
					tratarEventoDeProventoValeTransporte(tabelaResultado, obj);
					listaDeEventosDoFuncionario.add(obj);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Preenche as informacoes do salario composto para ser exibido no evento
	 * @param tabelaResultado
	 * @param obj
	 * @throws Exception
	 */
	private void tratarEventoDeProventoValeTransporte(SqlRowSet tabelaResultado, EventoFolhaPagamentoVO obj) throws Exception {
		
		if(Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
			return;
		} else {
			
			EventoValeTransporteFuncionarioCargoVO eventoVT = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			
			obj.setValorTemporario(eventoVT.getValorAReceber());
			obj.setValorInformado(obj.getValorTemporario().compareTo(BigDecimal.ZERO) > 0);
			obj.setReferencia(eventoVT.getQuantidadeDeViagens().toString() + "x" + eventoVT.getParametroValeTransporte().getValor());

		}
	}

	/**
	 * Atualiza os dias uteis do mes de acordo com os dias informado.
	 */
	@Override
	public void alterarDiasValeTransporte(Integer quantidadeDiasUteis, Integer quantidadeDiasUteisMeioExpediente, UsuarioVO usuario) {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append("UPDATE public.eventovaletransporteFuncionariocargo SET diasUteis=?, quantidadeViagensMeioExpediente =?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(quantidadeDiasUteis, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(quantidadeDiasUteisMeioExpediente, ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
		
	}

	/**
	 * Atualiza a quantidade de dias uteis dos vale transporte por numero dias.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void atualizarValeTransportePorQuantidadeDias(Integer quantidadeDiasUteisDe, Integer quantidadeDiasUteisPara,
			Integer quantidadeDiasUteisMeioExpedienteDe, Integer quantidadeDiasUteisMeioExpedientePara, UsuarioVO usuarioVO) throws Exception {

		this.validarDados(quantidadeDiasUteisDe, quantidadeDiasUteisPara, quantidadeDiasUteisMeioExpedienteDe, quantidadeDiasUteisMeioExpedientePara);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append("UPDATE public.eventovaletransporteFuncionariocargo SET diasUteis=?");
				
				if (Uteis.isAtributoPreenchido(quantidadeDiasUteisMeioExpedientePara)) {
					sql.append(", quantidadeViagensMeioExpediente = ?");
				}
				sql.append(" WHERE codigo in (");
				sql.append(" SELECT codigo FROM eventovaletransportefuncionariocargo WHERE diasuteis = ").append(quantidadeDiasUteisDe);
				if (quantidadeDiasUteisMeioExpedientePara != null) {
					sql.append(" AND quantidadeviagensmeioexpediente = ").append(quantidadeDiasUteisMeioExpedienteDe);
				}
				sql.append(")");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(quantidadeDiasUteisPara, ++i, sqlAlterar);
				
				if (Uteis.isAtributoPreenchido(quantidadeDiasUteisMeioExpedientePara)) {
					Uteis.setValuePreparedStatement(quantidadeDiasUteisMeioExpedientePara, ++i, sqlAlterar);
				}

				return sqlAlterar;
			}
		});
	}

	public List<EventoValeTransporteFuncionarioCargoVO> consultarPorCargo(CargoVO cargoVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT eventovaletransporteFuncionariocargo.* FROM public.eventovaletransporteFuncionariocargo");
		sql.append(" INNER JOIN public.funcionariocargo ON funcionariocargo.codigo = eventovaletransporteFuncionariocargo.funcionariocargo");
		sql.append(" WHERE funcionariocargo.cargo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), cargoVO.getCodigo());

		List<EventoValeTransporteFuncionarioCargoVO> lista = new ArrayList<>();
		while(rs.next()) {
			lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return lista;
	}

	private void validarDados(Integer quantidadeDiasUteis, Integer quantidadeDiasUteisPara, Integer quantidadeDiasUteisMeioExpedienteDe, Integer quantidadeDiasUteisMeioExpedientePara) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(quantidadeDiasUteis)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_EventoValeTransporte_QuantidadeDiasUteisDe"));
		}

		if (!Uteis.isAtributoPreenchido(quantidadeDiasUteisPara)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_EventoValeTransporte_QuantidadeDiasUteisPara"));
		}

		if (!(quantidadeDiasUteisMeioExpedienteDe == null && quantidadeDiasUteisMeioExpedientePara == null)) {
			if (quantidadeDiasUteisMeioExpedienteDe == null || quantidadeDiasUteisMeioExpedientePara == null ) { 
				throw new ConsistirException(UteisJSF.internacionalizar("msg_EventoValeTransporte_QuantidadeDiasUteisMeioExpediente"));
			}
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		EventoValeTransporteFuncionarioCargo.idEntidade = idEntidade;
	}
}