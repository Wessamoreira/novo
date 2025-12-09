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
import negocio.comuns.recursoshumanos.GrupoLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateEventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTemplateFolhaPagamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.GrupoLancamentoFolhaPagamentoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>GrupoLancamentoFolhaPagamentoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>GrupoLancamentoFolhaPagamentoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class GrupoLancamentoFolhaPagamento extends ControleAcesso implements GrupoLancamentoFolhaPagamentoInterfaceFacade {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public GrupoLancamentoFolhaPagamento() throws Exception {
		super();
		setIdEntidade("GrupoLancamentoFolhaPagamento");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO, List<TemplateEventoFolhaPagamentoVO> listaAnteriorTemplateEvento) throws Exception {
		validarDados(grupoLancamentoFolhaPagamento);
		validarDadosDuplicaidadeGrupoLancamento(grupoLancamentoFolhaPagamento);

		getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().persistir(grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento(), false, usuarioVO);

		if (grupoLancamentoFolhaPagamento.getCodigo() == null || grupoLancamentoFolhaPagamento.getCodigo() == 0) {
			incluir(grupoLancamentoFolhaPagamento, validarAcesso, usuarioVO);
		} else {
			alterar(grupoLancamentoFolhaPagamento, validarAcesso, usuarioVO);
		}
		
		persistirGrupoEventos(grupoLancamentoFolhaPagamento, listaAnteriorTemplateEvento, usuarioVO);
	}
	
	/**
	 * Grava e/ou exclui todos os grupo de eventos adicionados e/ou removidos do grupo de lancamento da folha de pagamento.
	 * 
	 * @param obj
	 * @param listaAnteriorTemplateEvento - lista para validar quais faixas valores foram adicionado ou excluidos da lista.
	 * @param usuario
	 * @throws Exception
	 */
	private void persistirGrupoEventos(GrupoLancamentoFolhaPagamentoVO obj, List<TemplateEventoFolhaPagamentoVO> listaAnteriorTemplateEvento, UsuarioVO usuario) throws Exception {
		if (!obj.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate().isEmpty() || !listaAnteriorTemplateEvento.isEmpty()) {

			for (TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO : obj.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate()) {
				if (templateEventoFolhaPagamentoVO.getCodigo() == 0 || listaAnteriorTemplateEvento == null || listaAnteriorTemplateEvento.isEmpty()) {
					templateEventoFolhaPagamentoVO.setTemplateLancamentoFolhaPagamentoVO(obj.getTemplateLancamentoFolhaPagamento());
					getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().incluir(templateEventoFolhaPagamentoVO, Boolean.FALSE, usuario);
				}
			}

			if (listaAnteriorTemplateEvento != null) {
				for (TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO : listaAnteriorTemplateEvento) {
					if (!contains(obj.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate(), templateEventoFolhaPagamentoVO)) {
						getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().excluir(templateEventoFolhaPagamentoVO, true, usuario);
					}
				}
			}
		}		
	}

	private boolean contains(List<TemplateEventoFolhaPagamentoVO> listaTemplateoEventoFolhaPagamento, TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamento) {
		for (TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO : listaTemplateoEventoFolhaPagamento) {
			if (templateEventoFolhaPagamento.getCodigo().equals(templateEventoFolhaPagamentoVO.getCodigo())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Valida os campos obrigatorios referente ao grupoLancamentoFolhaPagamento.
	 * 
	 * @param grupoLancamentoFolhaPagamento
	 * @throws ConsistirException
	 */
	private void validarDados(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(grupoLancamentoFolhaPagamento.getNome())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_nome"));
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(GrupoLancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			GrupoLancamentoFolhaPagamento.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			final String sql = " INSERT INTO GrupoLancamentoFolhaPagamento( nome, templateLancamentoFolhaPagamento) VALUES ( ?, ?) returning codigo " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					if (Uteis.isAtributoPreenchido(obj.getTemplateLancamentoFolhaPagamento().getCodigo())) {
						sqlInserir.setInt(2, obj.getTemplateLancamentoFolhaPagamento().getCodigo());
					} else {
						sqlInserir.setNull(2, 0);
					}

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

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(GrupoLancamentoFolhaPagamentoVO obj, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			GrupoLancamentoFolhaPagamento.alterar(getIdEntidade(), validarAcesso, usuarioVO);
			final String sql = " UPDATE GrupoLancamentoFolhaPagamento set nome= ?, templateLancamentoFolhaPagamento = ? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					if (Uteis.isAtributoPreenchido(obj.getTemplateLancamentoFolhaPagamento().getCodigo())) {
						sqlAlterar.setInt(2, obj.getTemplateLancamentoFolhaPagamento().getCodigo());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setInt(3, obj.getCodigo().intValue());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			GrupoLancamentoFolhaPagamento.excluir(getIdEntidade(), validarAcesso, usuarioVO);
			if (!grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate().isEmpty()) {
				for (TemplateEventoFolhaPagamentoVO templateEventoFolhaPagamentoVO : grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate()) {
					getFacadeFactory().getTemplateEventoFolhaPagamentoInterfaceFacade().excluir(templateEventoFolhaPagamentoVO, false, usuarioVO);
				}
			}

			String sql = "DELETE FROM GrupoLancamentoFolhaPagamento WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { grupoLancamentoFolhaPagamento.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<GrupoLancamentoFolhaPagamentoVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSQLSelectBasico());
		sql.append(" WHERE 1 = 1");
		sql.append(" AND tipotemplatefolhapagamento = 'GRUPO_LANCAMENTO'");
		SqlRowSet tabelaResultado = null;
		switch (campoConsulta) {
		case "codigo":
			sql.append(" AND GrupoLancamentoFolhaPagamento.codigo = ?");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Integer.valueOf(valorConsulta));
			break;
		case "nome":
			sql.append(" AND upper( nome ) ilike(sem_acentos(?)) ORDER BY GrupoLancamentoFolhaPagamento.codigo DESC");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), String.format("%%%s%%", valorConsulta.toUpperCase()) );
			break;
		default:
			break;
		}
		List<GrupoLancamentoFolhaPagamentoVO> gruposLancamentosFolhaPagamento = new ArrayList<GrupoLancamentoFolhaPagamentoVO>();
		while(tabelaResultado.next()) {
			gruposLancamentosFolhaPagamento.add(montarDados(tabelaResultado));
		}
		return gruposLancamentosFolhaPagamento;
	}

	public void validarDadosDuplicaidadeGrupoLancamento(GrupoLancamentoFolhaPagamentoVO obj) throws ConsistirException{
		int totalRegistroGrupoLancamento = consultarTotalRegistros(obj);

		if (totalRegistroGrupoLancamento > 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_GrupoLancamentoFolhaPagamento_nomeDuplicado"));
		}
	}

	public int consultarTotalRegistros(GrupoLancamentoFolhaPagamentoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(GrupoLancamentoFolhaPagamento.codigo) as qtde FROM GrupoLancamentoFolhaPagamento");
		sql.append(" WHERE UPPER(nome) = sem_acentos(?)");

		if (obj.getCodigo() != null) {
			sql.append(" AND codigo != ?");
		}

		SqlRowSet rs = null;
		if (obj.getCodigo() != null) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getNome().toUpperCase(), obj.getCodigo());
		} else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getNome().toUpperCase());
		}

		if (rs.next()) {
			return rs.getInt("qtde");
		}

		return 0;
	}

	private GrupoLancamentoFolhaPagamentoVO montarDados(SqlRowSet tabelaResultado) throws Exception {
		
		GrupoLancamentoFolhaPagamentoVO obj = new GrupoLancamentoFolhaPagamentoVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setNome(tabelaResultado.getString("nome"));
		
		obj.setTemplateLancamentoFolhaPagamento(Uteis.montarDadosVO(tabelaResultado.getInt("templatelancamentofolhapagamento"), TemplateLancamentoFolhaPagamentoVO.class, p -> getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, Uteis.NIVELMONTARDADOS_DADOSBASICOS)));
		
		return obj;
		
	}

	/**
	 * Sql basico para consulta da <code>TabelaReferenciaFolhaPagamentoVO<code>
	 * 
	 * @return String Sql Basico.
	 */
	public String getSQLSelectBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM GrupoLancamentoFolhaPagamento ");
		sql.append(" INNER JOIN templatelancamentofolhapagamento on GrupoLancamentoFolhaPagamento.templatelancamentofolhapagamento = templatelancamentofolhapagamento.codigo ");

		return sql.toString();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public GrupoLancamentoFolhaPagamentoVO consultarPorChavePrimaria(Long id) throws Exception {
		String sql =  new StringBuilder().append(getSQLSelectBasico()).append(" WHERE grupolancamentofolhapagamento.codigo = ?").toString();
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, id);
        if (tabelaResultado.next()) {
            return montarDados(tabelaResultado);
        }
        throw new Exception(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
	}

	@Override
	public void validarEventoFolhaPagamento(GrupoLancamentoFolhaPagamentoVO grupoLancamentoFolhaPagamento, TemplateEventoFolhaPagamentoVO templateLancamentoFolhaPagamento, EventoFolhaPagamentoVO eventoFolhaPagamento) throws Exception {
		for (TemplateEventoFolhaPagamentoVO obj : grupoLancamentoFolhaPagamento.getTemplateLancamentoFolhaPagamento().getListaEventosDoTemplate()) {
			if (obj.getEventoFolhaPagamento().getCodigo().equals(eventoFolhaPagamento.getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_duplicidadeEventoFolha"));
			}
		}

		if (!Uteis.isAtributoPreenchido(eventoFolhaPagamento.getCodigo())){
			throw new Exception(UteisJSF.internacionalizar("msg_GrupoLancamentoFolhaPagamento_evento"));
		}
	}

	@Override
	public boolean consultarExisteGrupoLancamentoPorCodigoTipo(LancamentoFolhaPagamentoVO lancamentoFolhaPagamento) throws Exception {
		StringBuilder sql =  new StringBuilder();
		sql.append(" SELECT COUNT(codigo) FROM templatelancamentofolhapagamento");
		sql.append(" WHERE tipotemplatefolhapagamento = ? and codigo = ?");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), TipoTemplateFolhaPagamentoEnum.GRUPO_LANCAMENTO.toString(),  lancamentoFolhaPagamento.getCodigo());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		GrupoLancamentoFolhaPagamento.idEntidade = idEntidade;
	}
}
