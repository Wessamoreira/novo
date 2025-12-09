package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

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
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO;
import negocio.comuns.recursoshumanos.ProgressaoSalarialVO.EnumCampoConsultaProgressaoSalarial;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.ProgressaoSalarialInterfaceFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class ProgressaoSalarial extends SuperFacade<ProgressaoSalarialVO> implements ProgressaoSalarialInterfaceFacade<ProgressaoSalarialVO> {

	private static final long serialVersionUID = 1880558427786266311L;

	protected static String idEntidade;
	
	public static String getIdEntidade() {
		if (idEntidade == null)
			idEntidade = "ProgressaoSalarial";
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ProgressaoSalarial.idEntidade = idEntidade;
	}
	
	@Override
	public void persistir(ProgressaoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
		persistirProgressaoSalarialItem(obj, usuarioVO);
	}
	
	private void persistirProgressaoSalarialItem(ProgressaoSalarialVO obj, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getProgressaoSalarialItemInterfaceFacade().persistirTodos(obj, false, usuario);
	}

	@Override
	public void incluir(ProgressaoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		ProgressaoSalarial.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder(" INSERT INTO progressaoSalarial ( descricao ) ")
				        .append(" VALUES ( ? )")
				        .append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
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
	}

	@Override
	public void alterar(ProgressaoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		ProgressaoSalarial.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE progressaoSalarial set ")
				        .append(" descricao=? ")
				        .append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProgressaoSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		getFacadeFactory().getProgressaoSalarialItemInterfaceFacade().excluirTodos(obj, validarAcesso, usuarioVO);
		
		ProgressaoSalarial.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM progressaosalarial WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
		
	}

	@Override
	public ProgressaoSalarialVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM progressaosalarial WHERE codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(ProgressaoSalarialVO obj) throws ConsistirException {
		if(obj.getDescricao().trim().isEmpty()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ProgressaoSalarial_CamposObrigatoriosNaoPreenchidos"));
		}
		
		if(consultarDescricaoJaExiste(obj)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_NivelSalarial_DescricaoJaExiste"));
		}
	}

	
	/**
	 * Valida se ja existe um registro de ProgressaoSalarial com a descricao informada
	 * 
	 * @param obj
	 * @return
	 */
	public boolean consultarDescricaoJaExiste(ProgressaoSalarialVO obj) {
		StringBuilder sql = new StringBuilder(getSQLBasico());
		sql.append(" WHERE descricao = ? ");
		
		List<Object> filtros = new ArrayList<>();
		filtros.add(obj.getDescricao());
		
		if(obj.getCodigo() > 0) {
			sql.append("and codigo <> ?");
			filtros.add(obj.getCodigo());
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		if (!tabelaResultado.next()) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public ProgressaoSalarialVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		ProgressaoSalarialVO obj = new ProgressaoSalarialVO();
		
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		
		return obj;
	}

	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		List<ProgressaoSalarialVO> objs = new ArrayList<>();
		dataModelo.getListaFiltros().clear();
		switch (EnumCampoConsultaProgressaoSalarial.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
			objs = consultarProgressaoSalarialPorDescricao(dataModelo);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalProgressaoSalarialPorDescricao(dataModelo));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}
	
	public List<ProgressaoSalarialVO> consultarProgressaoSalarialPorDescricao(DataModelo dataModelo) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(getSQLBasico());
        sql.append("where lower(sem_acentos(descricao) ) like (lower(sem_acentos(?))) ");
        sql.append(" order by descricao ");
        
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return montarDadosLista(tabelaResultado);
    }
	
	public Integer consultarTotalProgressaoSalarialPorDescricao(DataModelo dataModelo) throws Exception {
		
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(" select count(codigo) as qtde from progressaoSalarial ");
        sql.append(" where lower(sem_acentos(descricao) ) like (lower(sem_acentos(?))) ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }
	
	private List<ProgressaoSalarialVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		
		List<ProgressaoSalarialVO> listaProgressaoSalarial = new ArrayList<>();
        
        while(tabelaResultado.next()) {
        	listaProgressaoSalarial.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaProgressaoSalarial;
	}
	
	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from progressaoSalarial "); 
		return sql.toString();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalDeRegistros(String valorConsulta, DataModelo dataModelo) {

		try {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(getSQLBasico(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e.getMessage());
		}
	}

	@Override
	public List<ProgressaoSalarialVO> consultarPorFiltro(String campoConsulta, String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		
		StringJoiner sql = new StringJoiner(" ");
		sql.add("select * from ProgressaoSalarial");
		Object parametro = null;
		switch (campoConsulta) {
		case "DESCRICAO":
			sql.add(" WHERE upper(trim(sem_acentos(descricao))) ilike upper(trim(sem_acentos(?)))");
			parametro = String.format("%%%s%%", valorConsulta);
			break;
		case "CODIGO":
			sql.add(" WHERE codigo = ?");
			parametro = Integer.valueOf(valorConsulta);
			break;
		default:
			break;
		}

		sql.add(" ORDER BY codigo DESC ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametro);
		List<ProgressaoSalarialVO> listaProgressaoSalarial = new ArrayList<>();
		while (tabelaResultado.next()) {
			listaProgressaoSalarial.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}
		return listaProgressaoSalarial;
	}
}