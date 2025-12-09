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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.TemplateEventoFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>GrupoEventoFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>GrupoEventoFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see FaixaValorGEDVO
* @see ControleAcesso
*/
@SuppressWarnings({"unchecked", "rawtypes"})
@Service
@Scope
@Lazy
public class TemplateEventoFolhaPagamento  extends ControleAcesso implements TemplateEventoFolhaPagamentoInterfaceFacade<TemplateEventoFolhaPagamentoVO> {

	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public TemplateEventoFolhaPagamento() throws Exception {
		super();
		setIdEntidade("TemplateEventoFolhaPagamento");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TemplateEventoFolhaPagamento.idEntidade = idEntidade;
	}

	@Override
	public void persistir(TemplateEventoFolhaPagamentoVO t, boolean validarAcesso, UsuarioVO usuarioVO)
			throws Exception {
	}

	@Override
	public void incluir(TemplateEventoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {

		try {
			
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
				
				final StringBuilder sql = new StringBuilder(" INSERT INTO templateeventofolhapagamento ")
						.append(" ( valor, templatelancamentofolhapagamento, eventoFolhaPagamento) ")
						.append(" VALUES ( ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				
				final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getTemplateLancamentoFolhaPagamentoVO(), ++i, sqlInserir);
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
	public void alterar(TemplateEventoFolhaPagamentoVO t, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
	}

	@Override
	public void excluir(TemplateEventoFolhaPagamentoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("DELETE FROM TemplateEventoFolhaPagamento ")
					.append(" WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public TemplateEventoFolhaPagamentoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM TemplateLancamentoFolhaPagamentoVO WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}
	
	@Override
	public void validarDados(TemplateEventoFolhaPagamentoVO t) throws ConsistirException {
	}

	@Override
	public TemplateEventoFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {

		TemplateEventoFolhaPagamentoVO obj = new TemplateEventoFolhaPagamentoVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setValor(tabelaResultado.getBigDecimal("valor"));
		
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS)
			return obj;
			
		obj.setEventoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("eventofolhapagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, null, Uteis.NIVELMONTARDADOS_TODOS)));
		obj.getTemplateLancamentoFolhaPagamentoVO().setCodigo(tabelaResultado.getInt("templatelancamentofolhapagamento"));
		
		return obj;
	}
	
	@Override
	public void validarEventoFolhaPagamento(List<TemplateEventoFolhaPagamentoVO> listaDeEventos, TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO) throws Exception {

		if (!Uteis.isAtributoPreenchido(templateEventoFolhaPagamentoVO.getEventoFolhaPagamento().getCodigo())){
			throw new Exception(UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_evento"));
		}
		
		if (!Uteis.isAtributoPreenchido(templateEventoFolhaPagamentoVO.getValor())){
			throw new Exception(UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_valor"));
		}
		
		for (TemplateEventoFolhaPagamentoVO obj : listaDeEventos) {
			if (obj.getEventoFolhaPagamento().getCodigo().equals(templateEventoFolhaPagamentoVO.getEventoFolhaPagamento().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_duplicidadeEventoFolha"));
			}
		}
	}

	@Override
	public List<TemplateEventoFolhaPagamentoVO> consultarPorTemplateEventoFolhaPagamento(Integer codigo, int nivelmontardadosTodos, UsuarioVO usuarioLogado) {
		
		StringBuilder sql = new StringBuilder(" SELECT templateeventofolhapagamento.*, eventofolhapagamento.descricao, eventofolhapagamento.tipolancamento FROM templateeventofolhapagamento ");
		sql.append(" INNER JOIN eventofolhapagamento on templateeventofolhapagamento.eventofolhapagamento = eventofolhapagamento.codigo ");
		sql.append(" WHERE templatelancamentofolhapagamento = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		List<TemplateEventoFolhaPagamentoVO> lista = new ArrayList<>(0);
		while(tabelaResultado.next()) {
			try {
				lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return lista;
		
	}

	/**
	 * Adiciona os eventos da folha de pagamento que estao nos Eventos do Grupo e que nao estao no contracheque como valor inserido manualmente
	 * 
	 */
	@Override
	public void adicionarEventosDoGrupoLancamentoQueNaoEstaoNoContraCheque(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, TemplateLancamentoFolhaPagamentoVO template, ContraChequeVO contraChequeVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select te.codigo as codigo, te.valor as valor, e.codigo as eventofolhapagamento, cce.codigo as contraChequeEvento, cce.periodo as periodo from templateeventofolhapagamento te ");
		sql.append(" inner join eventofolhapagamento e on e.codigo = te.eventoFolhaPagamento ");
		sql.append(" left join contrachequeevento cce on cce.eventoFolhaPagamento = e.codigo and cce.contracheque = ? ");
		sql.append(" where te.templatelancamentofolhapagamento = ? ");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contraChequeVO.getCodigo(), template.getCodigo());
		
		while(tabelaResultado.next()) {
			try {
				Integer eventoFolhaPgto = tabelaResultado.getInt("eventofolhapagamento");
				//Caso evento esteja em outro periodo ele e adicionado assim mesmo. Por isso passo contraChequeEvento = null
				EventoFolhaPagamentoVO obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().montarDadosDoEventoParaContraCheque(eventoFolhaPgto, null);
				if(Uteis.isAtributoPreenchido(obj)) {
					
					tratarEventosCadastradosNoGrupo(tabelaResultado, obj);
					//Valida se o evento ja nao se encontra na lista
					if(!listaDeEventosDoFuncionario.contains(obj)) {
						listaDeEventosDoFuncionario.add(obj);
					}
						
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Trata os eventos que foram lancados no grupo
	 * Valores que foram escolhidos manualmente
	 * 
	 * @param tabelaResultado
	 * @param obj
	 */
	private void tratarEventosCadastradosNoGrupo(SqlRowSet tabelaResultado, EventoFolhaPagamentoVO obj) throws Exception {
		
		TemplateEventoFolhaPagamentoVO eventoDoTemplate = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);

		obj.setValorTemporario(eventoDoTemplate.getValor());
		obj.setValorInformado(obj.getValorTemporario().compareTo(BigDecimal.ZERO) > 0);
			
	}
}