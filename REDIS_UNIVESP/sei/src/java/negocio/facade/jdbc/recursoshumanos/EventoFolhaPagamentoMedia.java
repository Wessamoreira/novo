package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
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

import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoMediaVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoMediaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.EventoFolhaPagamentoMediaInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>EventoFolhaPagamentoMediaVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>EventoFolhaPagamentoMediaVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class EventoFolhaPagamentoMedia extends SuperFacade<EventoFolhaPagamentoMediaVO> implements EventoFolhaPagamentoMediaInterfaceFacade<EventoFolhaPagamentoMediaVO> {

	private static final long serialVersionUID = 364096271970541168L;
	
	protected static String idEntidade;

	public EventoFolhaPagamentoMedia() throws Exception {
		super();
		setIdEntidade("EventoFolhaPagamentoMedia");
	}

	@Override
	public void persistirTodos(List<EventoFolhaPagamentoMediaVO> eventoMediaVOs, EventoFolhaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		
		excluirTodosQueNaoEstaoNaLista(obj, eventoMediaVOs, false, usuarioVO);

		for (EventoFolhaPagamentoMediaVO eventoMediaVO : eventoMediaVOs) {
			eventoMediaVO.setEventoFolhaPagamentoVO(obj);
			persistir(eventoMediaVO, false, usuarioVO);
		}
	}
	
	private void excluirTodosQueNaoEstaoNaLista(EventoFolhaPagamentoVO obj, List<EventoFolhaPagamentoMediaVO> eventoFolhaPagamentoMediaVOs, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoFolhaPagamentoMedia.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<EventoFolhaPagamentoMediaVO> i = eventoFolhaPagamentoMediaVOs.iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM EventoFolhaPagamentoMedia WHERE eventofolhapagamento = ? ");
	    while (i.hasNext()) {
	    	EventoFolhaPagamentoMediaVO objeto = (EventoFolhaPagamentoMediaVO)i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(EventoFolhaPagamentoMediaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(EventoFolhaPagamentoMediaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamentoMedia.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.EventoFolhaPagamentoMedia( grupo, tipoEventoMedia, eventoFolhaPagamento) VALUES (?, ?, ?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getGrupo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoEventoMediaEnum().getValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamentoVO(), ++i, sqlInserir);

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
	public void alterar(EventoFolhaPagamentoMediaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoFolhaPagamentoMedia.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.eventoFolhaPagamentoMedia SET grupo=?, tipoEventoMedia=?, eventoFolhaPagamento=? WHERE codigo = ?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getGrupo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoEventoMediaEnum().getValor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamentoVO(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(EventoFolhaPagamentoMediaVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoFolhaPagamentoMedia.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM eventoFolhaPagamentoMedia WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public EventoFolhaPagamentoMediaVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(EventoFolhaPagamentoMediaVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getGrupo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GrupoMedia_Grupo"));
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getTipoEventoMediaEnum().getValor())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GrupoMedia_TipoMedia"));
		}
	}

	/**
	 * Monta a lista de {@link EventoFolhaPagamentoMediaVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<EventoFolhaPagamentoMediaVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<EventoFolhaPagamentoMediaVO> eventoMedias = new ArrayList<>();

        while(tabelaResultado.next()) {
        	eventoMedias.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
		return eventoMedias;
	}

	@Override
	public EventoFolhaPagamentoMediaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		EventoFolhaPagamentoMediaVO obj = new EventoFolhaPagamentoMediaVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setEventoFolhaPagamentoVO(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("eventofolhapagamento"), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));
		obj.setGrupo(tabelaResultado.getString("grupo"));
		if(tabelaResultado.getString("tipoEventomedia") != null)
			obj.setTipoEventoMediaEnum(TipoEventoMediaEnum.valueOf(tabelaResultado.getString("tipoEventomedia")));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM eventofolhapagamentomedia ");

		return sql.toString();
	}

	@Override
	public List<EventoFolhaPagamentoMediaVO> consultarPorEventoFolha(EventoFolhaPagamentoVO eventofolhapagamento, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE eventofolhapagamento = ? order by tipoeventomedia, grupo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), eventofolhapagamento.getCodigo());
		return (montarDadosLista(tabelaResultado));
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		EventoFolhaPagamentoMedia.idEntidade = idEntidade;
	}
}