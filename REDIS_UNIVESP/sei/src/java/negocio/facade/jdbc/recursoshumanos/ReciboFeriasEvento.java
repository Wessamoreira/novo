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

import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ReciboFeriasEventoVO;
import negocio.comuns.recursoshumanos.ReciboFeriasVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.ReciboFeriasEventoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>ReciboEventoFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>ReciboEventoFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class ReciboFeriasEvento extends SuperFacade<ReciboFeriasEventoVO> implements ReciboFeriasEventoInterfaceFacade<ReciboFeriasEventoVO> {

	private static final long serialVersionUID = -7671928190005676573L;
	
	protected static String idEntidade;

	public ReciboFeriasEvento() throws Exception {
		super();
		setIdEntidade("ReciboFeriasEvento");
	}

	@Override
	public void incluir(ReciboFeriasEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ContraChequeEvento.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					
					final StringBuilder sql = new StringBuilder(" INSERT INTO reciboferiasevento ")
							.append(" ( recibo, evento, provento, desconto, baseCalculo, ")
							.append(" referencia, informadoManual, valorreferencia )")
							.append(" VALUES ( ?, ?, ?, ?, ?, ")
							.append(" ?, ?, ? ) ")
							.append("returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
					
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString()); 

					int i = 0;
					
					Uteis.setValuePreparedStatement(obj.getRecibo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getEvento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getProvento(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDesconto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getBaseCalculo(), ++i, sqlInserir);
					
					Uteis.setValuePreparedStatement(obj.getReferencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInformadoManual(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorReferencia(), ++i, sqlInserir);
					
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
	public void alterar(ReciboFeriasEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			
			ReciboFeriasEvento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				
				final StringBuilder sql = new StringBuilder(" UPDATE reciboferiasevento SET ")
						.append(" recibo=?, evento=?, provento=?, desconto=?, baseCalculo=?, ")
						.append(" referencia=?, informadoManual=?, valorreferencia=? ")
						.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

				int i = 0;
				
				Uteis.setValuePreparedStatement(obj.getRecibo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getEvento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getProvento(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDesconto(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getBaseCalculo(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getReferencia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getInformadoManual(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getValorReferencia(), ++i, sqlAlterar);
				
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
				
				return sqlAlterar;
			}
		});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluir(ReciboFeriasEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ContraChequeEvento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			String sql = "DELETE FROM reciboferiasevento WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ReciboFeriasEventoVO consultarPorChavePrimaria(Long codigo) throws Exception {
		String sql = " SELECT * FROM reciboferiasevento WHERE codigo = ?";
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
        if (rs.next()) {
            return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS);
        }
        throw new Exception("Dados não encontrados.");
	}
	
	@Override
	public void validarDados(ReciboFeriasEventoVO obj) throws ConsistirException {
	}

	@Override
	public void validarDadosReciboEvento(ReciboFeriasEventoVO obj, ReciboFeriasVO reciboFeriasVO) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getEvento().getCodigo())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraChequeItem_eventoVazio"));
		}

		if(reciboFeriasVO.getListaReciboEvento().contains(obj) && !obj.getItemEmEdicao()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContraChequeItem_eventoAdicionado"));
		}
	}

	@Override
	public ReciboFeriasEventoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		ReciboFeriasEventoVO obj = new ReciboFeriasEventoVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setProvento(tabelaResultado.getBigDecimal("provento"));
		obj.setDesconto(tabelaResultado.getBigDecimal("desconto"));
		obj.setBaseCalculo(tabelaResultado.getBigDecimal("basecalculo"));
		obj.setReferencia(tabelaResultado.getString("referencia"));
		obj.setInformadoManual(tabelaResultado.getBoolean("informadoManual"));
		obj.setValorReferencia(tabelaResultado.getBigDecimal("valorreferencia"));

		obj.setEvento(Uteis.montarDadosVO(tabelaResultado.getInt("evento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p.intValue(), null, Uteis.NIVELMONTARDADOS_TODOS)));	
		obj.setRecibo(Uteis.montarDadosVO(tabelaResultado.getInt("recibo"), ReciboFeriasVO.class, p -> getFacadeFactory().getReciboFeriasInterfaceFacade().consultarPorChavePrimaria(p.longValue())));

		return obj;
	}

	@Override
	public void persistirTodos(ReciboFeriasVO obj, boolean b, UsuarioVO usuario) {
		for(ReciboFeriasEventoVO recibo : obj.getListaReciboEvento()) {
			recibo.setRecibo(obj);
			try {
				persistir(recibo, false, usuario);	
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		excluirTodosQueNaoEstaoNaListaDeEventosDoRecibo(obj, false, usuario);		
	}
	

	@Override
	public void persistir(ReciboFeriasEventoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	private void excluirTodosQueNaoEstaoNaListaDeEventosDoRecibo(ReciboFeriasVO obj, boolean validarAcesso, UsuarioVO usuario) {
		
		ArrayList<Integer> condicao = new ArrayList<>();
		condicao.add(obj.getCodigo());
		
		Iterator<ReciboFeriasEventoVO> i = obj.getListaReciboEvento().iterator();

		StringBuilder str = new StringBuilder("DELETE FROM ReciboFeriasEvento WHERE recibo = ? ");
	    while (i.hasNext()) {
	    	ReciboFeriasEventoVO objeto = (ReciboFeriasEventoVO) i.next();
	    	str.append(" AND codigo <> ? ");
	    	condicao.add(objeto.getCodigo());
	    }

		str.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(str.toString(), condicao.toArray());
	}

	
	@Override
	public List<ReciboFeriasEventoVO> consultarPorReciboFerias(Integer codigoReciboFerias, int nivelmontardadosTodos, UsuarioVO usuario) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT reciboferiasevento.* FROM reciboferiasevento ");
		sql.append(" inner join eventofolhapagamento as evento on reciboferiasevento.evento = evento.codigo ");
		sql.append(" where recibo = ? order by evento.tipolancamento desc, evento.prioridade asc, evento.ordemcalculo asc, evento.identificador desc");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoReciboFerias);
		List<ReciboFeriasEventoVO> lista = new ArrayList<ReciboFeriasEventoVO>();
        while (rs.next()) {
        	try {
        		lista.add(montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS));	
        	} catch (Exception e) {
        		e.printStackTrace();
			}
        	
        }

		return lista;
	}

	@Override
	public void excluirEventosDoRecibo(ReciboFeriasVO reciboFerias, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception{
		try {
			ReciboFeriasEvento.excluir(getIdEntidade(), validarAcesso, usuarioLogado);
		
			String sql = "DELETE FROM reciboferiasevento WHERE ((recibo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(sql, new Object[] { reciboFerias.getCodigo() });
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ReciboFeriasEvento.idEntidade = idEntidade;
	}
}