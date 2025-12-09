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
import negocio.comuns.recursoshumanos.FaixaSalarialVO;
import negocio.comuns.recursoshumanos.FaixaSalarialVO.EnumCampoConsultaFaixaSalarial;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.FaixaSalarialInterfaceFacade;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Service
@Scope
@Lazy
public class FaixaSalarial extends SuperFacade<FaixaSalarialVO> implements FaixaSalarialInterfaceFacade<FaixaSalarialVO> {

	private static final long serialVersionUID = 1252804836663141138L;

	protected static String idEntidade;
	
	public static String getIdEntidade() {
		if (idEntidade == null)
			idEntidade = "FaixaSalarial";
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		FaixaSalarial.idEntidade = idEntidade;
	}
	
	@Override
	public void persistir(FaixaSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == null || obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@Override
	public void incluir(FaixaSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		FaixaSalarial.incluir(getIdEntidade(), validarAcesso, usuarioVO);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			
			public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder(" INSERT INTO faixasalarial ( descricao, valor )")
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
	public void alterar(FaixaSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		FaixaSalarial.alterar(getIdEntidade(), validarAcesso, usuarioVO);
		
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE faixasalarial set ")
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
	public void excluir(FaixaSalarialVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		
		FaixaSalarial.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM faixasalarial WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
		
	}

	@Override
	public FaixaSalarialVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT * FROM faixasalarial WHERE codigo = ?");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("msg_erro_dadosnaoencontrados");
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(FaixaSalarialVO obj) throws ConsistirException {
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
	public boolean consultarDescricaoJaExiste(FaixaSalarialVO obj) {
		StringBuilder sql = new StringBuilder("SELECT * FROM faixasalarial WHERE descricao = ?");
		
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
	public FaixaSalarialVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		
		FaixaSalarialVO obj = new FaixaSalarialVO();
		
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setValor(tabelaResultado.getInt("valor"));

		return obj;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		List<FaixaSalarialVO> objs = new ArrayList<>();
		dataModelo.getListaFiltros().clear();
		switch (EnumCampoConsultaFaixaSalarial.valueOf(dataModelo.getCampoConsulta())) {
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
	
	public List<FaixaSalarialVO> consultarNivelSalarialPorDescricao(DataModelo dataModelo) throws Exception {
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
        
        StringBuilder sql = new StringBuilder(" select count(codigo) as qtde from faixasalarial ");
        sql.append(" where lower(sem_acentos(descricao) ) like (lower(sem_acentos(?))) ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }
	
	public List<FaixaSalarialVO> consultarNivelSalarialPorValor(DataModelo dataModelo) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(getSQLBasico());
        sql.append(" where valor = ? ");
        sql.append(" order by descricao ");
        
        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        List<FaixaSalarialVO> listaNivelSalarial = montarDadosLista(tabelaResultado);
        return listaNivelSalarial;
    }
	
	public Integer consultarTotalNivelSalarialPorValor(DataModelo dataModelo) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
        StringBuilder sql = new StringBuilder(" select count(codigo) as qtde from faixasalarial ");
        sql.append(" where valor = ? ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	private List<FaixaSalarialVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		
		List<FaixaSalarialVO> listaNivelSalarial = new ArrayList<>();
        
        while(tabelaResultado.next()) {
        	listaNivelSalarial.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return listaNivelSalarial;
	}
	
	private String getSQLBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from faixaSalarial "); 
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

	public List<FaixaSalarialVO> consultarListaDeFaixaSalarial() throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, null);
        
        StringBuilder sql = new StringBuilder(getSQLBasico());
        sql.append(" order by valor, descricao ");
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        
        List<FaixaSalarialVO> listaFaixaSalarial = montarDadosLista(tabelaResultado);
        return listaFaixaSalarial;
    }
}