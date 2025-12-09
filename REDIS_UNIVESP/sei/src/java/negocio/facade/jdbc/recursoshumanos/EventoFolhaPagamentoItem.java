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

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoItemVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.EventoFolhaPagamentoItemInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>EventoFolhaPagamentoItemVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>EventoFolhaPagamentoItemVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class EventoFolhaPagamentoItem extends SuperFacade<EventoFolhaPagamentoItemVO> implements EventoFolhaPagamentoItemInterfaceFacade<EventoFolhaPagamentoItemVO> {

	private static final long serialVersionUID = 3936147293556374462L;

	protected static String idEntidade;

	public EventoFolhaPagamentoItem() throws Exception {
		super();
		setIdEntidade("EventoFolhaPagamentoItem");
	}

	@Override
	public void persistirTodos(List<EventoFolhaPagamentoItemVO> eventoFolhaPagamentoItemVOs, EventoFolhaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		excluirTodosQueNaoEstaoNaLista(obj, eventoFolhaPagamentoItemVOs, false, usuarioVO);

		for (EventoFolhaPagamentoItemVO eventoFolhaPagamentoItemVO : eventoFolhaPagamentoItemVOs) {
			eventoFolhaPagamentoItemVO.setEventoFolhaPagamento(obj);
			persistir(eventoFolhaPagamentoItemVO, false, usuarioVO);
		}
	}
	
	private void excluirTodosQueNaoEstaoNaLista(EventoFolhaPagamentoVO obj, List<EventoFolhaPagamentoItemVO> eventoFolhaPagamentoItemVOs, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoFolhaPagamentoItem.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<EventoFolhaPagamentoItemVO> i = eventoFolhaPagamentoItemVOs.iterator();
		
		StringBuilder str = new StringBuilder("DELETE FROM eventofolhapagamentoitem WHERE eventofolhapagamento = ? ");
	    while (i.hasNext()) {
	    	EventoFolhaPagamentoItemVO objeto = (EventoFolhaPagamentoItemVO)i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }
	    
		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(EventoFolhaPagamentoItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(EventoFolhaPagamentoItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			EventoFolhaPagamentoItem.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.eventofolhapagamentoitem( eventofolhapagamentoitem, eventofolhapagamento) VALUES (?, ?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamentoItem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlInserir);

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
	public void alterar(EventoFolhaPagamentoItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoFolhaPagamentoItem.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.eventofolhapagamentoitem SET eventofolhapagamentoitem=?, eventofolhapagamento=? WHERE codigo = ?");
				sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamentoItem(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(EventoFolhaPagamentoItemVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		EventoFolhaPagamentoItem.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM eventofolhapagamentoitem WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public EventoFolhaPagamentoItemVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(EventoFolhaPagamentoItemVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getEventoFolhaPagamentoItem())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}
	}

	/**
	 * Monta a lista de {@link EventoFolhaPagamentoItemVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<EventoFolhaPagamentoItemVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<EventoFolhaPagamentoItemVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public EventoFolhaPagamentoItemVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		EventoFolhaPagamentoItemVO obj = new EventoFolhaPagamentoItemVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setEventoFolhaPagamentoItem(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamentoitem"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("eventofolhapagamentoitem"), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));
		obj.setEventoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getInt("eventofolhapagamento"), null, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM eventofolhapagamentoitem ");

		return sql.toString();
	}

	@Override
	public List<EventoFolhaPagamentoItemVO> consultarPorEventoFolha(EventoFolhaPagamentoVO eventofolhapagamento, boolean validarAcesso, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE eventofolhapagamento = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), eventofolhapagamento.getCodigo());
		return (montarDadosLista(tabelaResultado));
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		EventoFolhaPagamentoItem.idEntidade = idEntidade;
	}

	
	/**
	 * Adiciona os eventos vinculados aos eventos do funcionario
	 * Pega os eventos que serao processados, verifica e adiciona os eventos vinculados a esses eventos que serao processados. tabela: eventofolhapagamentoitem
	 */
	@Override
	public void adicionarEventosVinculadosDosEventosDoContraChequeDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select e.codigo as eventofolhapagamento, cce.codigo as contraChequeEvento from eventofolhapagamentoitem  ei ");
		sql.append(" inner join eventofolhapagamento e on e.codigo = ei.eventofolhapagamentoitem and situacao like 'ATIVO' ");
		sql.append(" left join contrachequeevento cce on cce.eventoFolhaPagamento = e.codigo and cce.contracheque = ? ");
		sql.append(" where ei.eventofolhapagamento in (");
		
		List<Integer> codigoDosEventos = new ArrayList<>();
		
		for(EventoFolhaPagamentoVO evento : listaDeEventosDoFuncionario) {
			if(!codigoDosEventos.isEmpty())
				sql.append(",");
			
			codigoDosEventos.add(evento.getCodigo());
			sql.append("?");
		}
		
		sql.append(")");
		
		codigoDosEventos.add(0, contraChequeVO.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoDosEventos.toArray());
		
		while(tabelaResultado.next()) {
			try {
				EventoFolhaPagamentoVO obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().montarDadosDoEventoParaContraCheque(tabelaResultado.getInt("eventofolhapagamento"), tabelaResultado.getInt("contraChequeEvento"));
				if(Uteis.isAtributoPreenchido(obj) && !Uteis.isAtributoPreenchido(obj.getContraChequeEventoVO())) {
					listaDeEventosDoFuncionario.add(obj);
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}