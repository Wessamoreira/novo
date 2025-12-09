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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperFacade;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.TipoTransporteVO;
import negocio.comuns.recursoshumanos.TipoTransporteVO.EnumCampoConsultaTipoTransporte;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.recursoshumanos.TipoTransporteInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>TipoTransporteVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>TipoTransporteVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class TipoTransporte extends SuperFacade<TipoTransporteVO> implements TipoTransporteInterfaceFacade<TipoTransporteVO> {

	private static final long serialVersionUID = 3936147293556374462L;

	protected static String idEntidade;

	public TipoTransporte() throws Exception {
		super();
		setIdEntidade("TipoTransporte");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(TipoTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(TipoTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoEmprestimo.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder("INSERT INTO public.tipotransporte(descricao) VALUES (?)");
					sql.append(" returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

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
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	public void alterar(TipoTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		TipoEmprestimo.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.tipotransporte SET descricao=? WHERE codigo = ? ")
						.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(TipoTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		TipoEmprestimo.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM tipotransporte WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}

	@Override
	public TipoTransporteVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE tipotransporte.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
	}

	@Override
	public void validarDados(TipoTransporteVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	@Override
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		List<TipoTransporteVO> objs = new ArrayList<TipoTransporteVO>();
		dataModelo.getListaFiltros().clear();
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_TODOS);

		switch (EnumCampoConsultaTipoTransporte.valueOf(dataModelo.getCampoConsulta())) {
		case DESCRICAO:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
			objs = consultaTipoTransporte(dataModelo, "descricao");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoTransporte(dataModelo, "descricao"));
			break;
		case CODIGO:
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
			objs = consultaTipoTransporte(dataModelo, "codigo");
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTipoTransporte(dataModelo, "codigo"));
			break;
		default:
			break;
		}
		dataModelo.setListaConsulta(objs);
	}
	
	private List<TipoTransporteVO> consultaTipoTransporte(DataModelo dataModelo, String campoConsulta) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder();
        sql.append(getSqlBasico());

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoTransporte.CODIGO.name())) {
        	sql.append(" WHERE codigo = ?");
        } else {
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        	sql.append(" ORDER BY ").append(" codigo DESC");        	
        }

        UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());

        return montarDadosLista(tabelaResultado);
    }

	private Integer consultarTotalTipoTransporte(DataModelo dataModelo, String campoConsulta) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS qtde FROM tipotransporte ");

        if (dataModelo.getCampoConsulta().equals(EnumCampoConsultaTipoTransporte.CODIGO.name())) {
        	sql.append(" WHERE codigo = ?");
        } else {
        	sql.append(" WHERE LOWER(sem_acentos(").append(campoConsulta).append(") ) LIKE (LOWER(sem_acentos(?))) ");
        }

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

	/**
	 * Monta a lista de {@link TipoTransporteVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<TipoTransporteVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<TipoTransporteVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public TipoTransporteVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		TipoTransporteVO obj = new TipoTransporteVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM tipotransporte");

		return sql.toString();
	}

	@Override
	public List<TipoTransporteVO> consultarTipoTransporte(DataModelo dataModelo, String campoConsulta) throws Exception {
		dataModelo.setCampoConsulta(campoConsulta);
		dataModelo.getListaFiltros().clear();
		dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_TODOS);
		dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);

		return this.consultaTipoTransporte(dataModelo, campoConsulta);
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
	public static void setIdEntidade(String idEntidade) {
		TipoTransporte.idEntidade = idEntidade;
	}
}
