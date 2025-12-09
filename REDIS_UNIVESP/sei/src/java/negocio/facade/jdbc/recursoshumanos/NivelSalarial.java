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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.NivelSalarialVO;
import negocio.comuns.recursoshumanos.NivelSalarialVO.EnumCampoConsultaNivelSalarial;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.NivelSalarialInterfaceFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class NivelSalarial extends SuperFacade<NivelSalarialVO> implements NivelSalarialInterfaceFacade<NivelSalarialVO> {

	private static final long serialVersionUID = -3255227380607420750L;

	protected static String idEntidade;
	
	public static String getIdEntidade() {
		if (idEntidade == null)
			idEntidade = "NivelSalarial";
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NivelSalarial.idEntidade = idEntidade;
	}
	
	@Override
	public void persistir(NivelSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(NivelSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		NivelSalarial.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder(" INSERT INTO nivelsalarial ( descricao, valor )")
				        .append(" VALUES ( ?, ? )")
				        .append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
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
	public void alterar(NivelSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		NivelSalarial.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE nivelsalarial set ")
				        .append(" descricao=?, valor=? ")
				        .append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	public void excluir(NivelSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		NivelSalarial.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM nivelsalarial WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
		
	}

	@Override
	public NivelSalarialVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM nivelsalarial WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(NivelSalarialVO obj) throws ConsistirException {
		if(obj.getDescricao().trim().isEmpty() || obj.getValor() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_NivelSalarial_CamposObrigatoriosNaoPreenchidos"));
		}
		
		if(obj.getValor() <= 0) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_NivelSalarial_CampoValorDeveSerMaiorQueZero"));
		}
		
		if(consultarDescricaoJaExiste(obj)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_NivelSalarial_DescricaoJaExiste"));
		}
	}

	
	/**
	 * Valida se ja existe um registro de NivelSalario com a descricao informada
	 * 
	 * @param obj
	 * @return
	 */
	public boolean consultarDescricaoJaExiste(NivelSalarialVO obj) {
		StringBuilder sql = new StringBuilder("SELECT * FROM nivelsalarial WHERE descricao = ?");
		
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
	public NivelSalarialVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		NivelSalarialVO obj = new NivelSalarialVO();
		
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setValor(tabelaResultado.getInt("valor"));

		return obj;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		List<NivelSalarialVO> objs = new ArrayList<>();
		dataModelo.getListaFiltros().clear();
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_TODOS);
		
		switch (EnumCampoConsultaNivelSalarial.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
			objs = consultarNivelSalarialPorDescricao(dataModelo);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalNivelSalarialPorDescricao(dataModelo));
			break;
		case VALOR:
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
			objs = consultarNivelSalarialPorValor(dataModelo);
			dataModelo.setTotalRegistrosEncontrados(consultarTotalNivelSalarialPorValor(dataModelo));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}
	
	public List<NivelSalarialVO> consultarNivelSalarialPorDescricao(DataModelo dataModelo) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(getSQLBasico());
        sql.append("where lower(sem_acentos(descricao) ) like (lower(sem_acentos(?))) ");
        sql.append(" order by descricao ");
        
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return montarDadosLista(tabelaResultado);
    }
	
	public Integer consultarTotalNivelSalarialPorDescricao(DataModelo dataModelo) throws Exception {
		
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(" select count(codigo) as qtde from nivelsalarial ");
        sql.append(" where lower(sem_acentos(descricao) ) like (lower(sem_acentos(?))) ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }
	
	public List<NivelSalarialVO> consultarNivelSalarialPorValor(DataModelo dataModelo) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(getSQLBasico());
        sql.append(" where valor = ? ");
        sql.append(" order by descricao ");
        
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        List<NivelSalarialVO> listaNivelSalarial = montarDadosLista(tabelaResultado);
        return listaNivelSalarial;
    }
	
	public Integer consultarTotalNivelSalarialPorValor(DataModelo dataModelo) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(" select count(codigo) as qtde from nivelsalarial ");
        sql.append(" where valor = ? ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	private List<NivelSalarialVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		
		List<NivelSalarialVO> listaNivelSalarial = new ArrayList<>();
        
        while(tabelaResultado.next()) {
        	listaNivelSalarial.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaNivelSalarial;
	}
	
	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from nivelSalarial "); 
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

	public List<NivelSalarialVO> consultarListaDeNivelSalarial() throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, null);
        
        StringBuilder sql = new StringBuilder(getSQLBasico());
        sql.append(" order by valor, descricao ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
        List<NivelSalarialVO> listaNivelSalarial = montarDadosLista(tabelaResultado);
        return listaNivelSalarial;
    }
}