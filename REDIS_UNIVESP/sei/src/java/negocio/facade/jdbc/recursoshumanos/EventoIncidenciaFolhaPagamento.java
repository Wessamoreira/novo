package negocio.facade.jdbc.recursoshumanos;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.EventoIncidenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.IncidenciaFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.EventoIncidenciaFolhaPagamentoInterfaceFacade;

/*Classe de persistencia que encapsula todas as operacoes de manipulacao dos
* dados da classe <code>EventoFolhaPagamentoVO</code>. Responsavel por implementar
* operacoes como incluir, alterar, excluir e consultar pertinentes a classe
* <code>EventoFolhaPagamentoVO</code>. Encapsula toda a interacao com o banco de
* dados.
* 
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class EventoIncidenciaFolhaPagamento extends ControleAcesso implements EventoIncidenciaFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = -5556587457849107369L;
	protected static String idEntidade;

	public EventoIncidenciaFolhaPagamento() throws Exception {
		super();
		setIdEntidade("EventoFolhaPagamento");
	}

	@Override
	public void incluirIncidencias(EventoFolhaPagamentoVO obj, UsuarioVO usuario) throws Exception {
		salvarIncidencias(obj, usuario);
	}
	
	@Override
	public void alterarIncidencias(EventoFolhaPagamentoVO obj, UsuarioVO usuario) throws Exception {
		excluirPorEvento(obj.getCodigo(), false, usuario);
		salvarIncidencias(obj, usuario);
	}

	@Deprecated
	private void salvarIncidencias(EventoFolhaPagamentoVO obj, UsuarioVO usuario) throws Exception {
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(EventoIncidenciaFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoIncidenciaFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder(" INSERT INTO eventoincidenciafolhapagamento ( incidencia, evento )")
					        .append(" VALUES ( ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getIncidencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEvento(), ++i, sqlInserir);
					
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorEvento(Integer codigoEvento,boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoIncidenciaFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder("DELETE FROM eventoincidenciafolhapagamento WHERE ((evento = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { codigoEvento });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Monta o objeto <code>EventoFolhaPagamentoVO<code> consultado do banco de
	 * dados.
	 * 
	 * @param tabelaResultado
	 * @return
	 */
	public EventoIncidenciaFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {

		EventoIncidenciaFolhaPagamentoVO obj = new EventoIncidenciaFolhaPagamentoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIncidencia(Uteis.montarDadosVO(tabelaResultado.getInt("incidencia"), IncidenciaFolhaPagamentoVO.class, p -> getFacadeFactory().getIncidenciaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, usuario)));
		obj.setEvento(Uteis.montarDadosVO(tabelaResultado.getInt("evento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, usuario, Uteis.NIVELMONTARDADOS_DADOSCONSULTA)));

		return obj;
	}

	/**
	 * Sql basico para consultad da <code>EventoFolhaPagamentoVO<code>
	 * 
	 * @return
	 */
	public String getSelectSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM eventoincidenciafolhapagamento ei ");
		//.append(" inner join incidenciafolhapagamento i on ei.incidencia = i.codigo ")
		//.append(" inner join eventofolhapagamento e on ei.evento = e.codigo ");

		return sql.toString();
	}


	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		FaixaValor.idEntidade = idEntidade;
	}
	
	@Override
	public List<EventoIncidenciaFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSelectSqlBasico());

		switch (campoConsulta) {
		case "evento":
			sql.append(" WHERE ei.evento = ? ");
			break;
		case "incidencia":
			sql.append(" WHERE ei.incidencia = ? ");
			break;
		default:
			break;
		}
		
		sql.append(" ORDER BY ei.").append(campoConsulta).append(" DESC ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
		List<EventoIncidenciaFolhaPagamentoVO> eventoIncidenciaDaFolhaDePagamento = new ArrayList<>();
		while (tabelaResultado.next()) {
			eventoIncidenciaDaFolhaDePagamento.add(montarDados(tabelaResultado, usuario));
		}
		return eventoIncidenciaDaFolhaDePagamento;
	}

	@Override
	public void validarDadosPorIncidenciaFolhaPagamento(IncidenciaFolhaPagamentoVO obj) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			int total = consultarTotalPorEventoFolhaPagamento(obj);
	
			if (total > 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_EventoIncidenciaFolhaPagamento_vinculado"));
			}
		}
	}

	public int consultarTotalPorEventoFolhaPagamento(IncidenciaFolhaPagamentoVO obj) throws ConsistirException {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT COUNT(codigo) as qtde FROM eventoincidenciafolhapagamento");
			sql.append(" WHERE incidencia = ?");

			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo());

	        if (rs.next()) {
	            return rs.getInt("qtde");
	        }

	    	return 0;
		} catch (Exception e) {
			throw e;
		}
	}
}