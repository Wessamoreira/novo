package negocio.facade.jdbc.recursoshumanos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.recursoshumanos.ParametroValeTransporteHistoricoVO;
import negocio.comuns.recursoshumanos.ParametroValeTransporteVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoTarifaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.interfaces.recursoshumanos.ParametroValeTransporteHistoricoInterfaceFacade;

/*Classe de persistência que encapsula todas as operações de manipulação dos
* dados da classe <code>ParametroValeTransporteHistoricoVO</code>. Responsável por implementar
* operações como incluir, alterar, excluir e consultar pertinentes a classe
* <code>ParametroValeTransporteHistoricoVO</code>. Encapsula toda a interação com o banco de
* dados.
* 
* @see ControleAcesso
*/
@Service
@Scope
@Lazy
public class ParametroValeTransporteHistorico extends SuperFacade<ParametroValeTransporteHistoricoVO>
		implements ParametroValeTransporteHistoricoInterfaceFacade<ParametroValeTransporteHistoricoVO> {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public ParametroValeTransporteHistorico() throws Exception {
		super();
		setIdEntidade("ParametroValeTransporteHistorico");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(ParametroValeTransporteHistoricoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);

		if (obj.getCodigo() == 0) {
			incluir(obj, validarAcesso, usuarioVO);
		} else {
			alterar(obj, validarAcesso, usuarioVO);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void incluir(ParametroValeTransporteHistoricoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ParametroValeTransporteHistorico.incluir(getIdEntidade(), validarAcesso, usuarioVO);

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {

					StringBuilder sql = new StringBuilder();
					sql.append(" INSERT INTO public.parametrovaletransportehistorico(");
					sql.append(" valor, iniciovigencia, fimvigencia, dataalteracao, usuarioresponsavelalteracao, parametrovaletransporte,");
					sql.append(" descricao, tipotarifa, tipolinhatransporte )");
					sql.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ");
					sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

					final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());

					int i = 0;
					Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getInicioVigencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFimVigencia(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataAlteracao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUsuarioResponsavelAlteracao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getParametroValeTransporte(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoTarifa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoLinhaTransporte(), ++i, sqlInserir);

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
	public void alterar(ParametroValeTransporteHistoricoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ParametroValeTransporteHistorico.alterar(getIdEntidade(), validarAcesso, usuarioVO);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder();
				sql.append(" UPDATE public.parametrovaletransportehistorico SET");
				sql.append(" valor=?, iniciovigencia=?, fimvigencia=?, dataalteracao=?, usuarioresponsavelalteracao=?, parametrovaletransporte=?,");
				sql.append(" descricao=?, tipotarifa=?, tipolinhatransporte=?");
				sql.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(obj.getValor(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getInicioVigencia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getFimVigencia(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDataAlteracao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getUsuarioResponsavelAlteracao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getParametroValeTransporte(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getDescricao(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoTarifa(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getTipoLinhaTransporte(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(ParametroValeTransporteHistoricoVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		ParametroValeTransporteHistorico.excluir(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sql = new StringBuilder("DELETE FROM parametrovaletransportehistorico WHERE ((codigo = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });

	}
	
	@Override
	public void excluirPorParametroValeTransporte(ParametroValeTransporteVO obj, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM parametrovaletransportehistorico WHERE parametrovaletransporte = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getCodigo() });
	}

	@Override
	public ParametroValeTransporteHistoricoVO consultarPorChavePrimaria(Long id) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico()).append(" WHERE parametrovaletransportehistorico.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), id);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_dadosnaoencontrados"));
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public void validarDados(ParametroValeTransporteHistoricoVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getValor())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_TipoEmprestimo_descricao"));
		}
	}

	@Override
	public List<ParametroValeTransporteHistoricoVO> consultarPorParametroValeTransporte(Integer codigoParametro) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append(getSqlBasico());
        sql.append(" WHERE parametrovaletransporte = ?");

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoParametro);
        return montarDadosLista(tabelaResultado);
    }

	/**
	 * Monta a lista de {@link ParametroValeTransporteHistoricoVO}. 
	 * 
	 * @param tabelaResultado
	 * @return
	 * @throws Exception
	 */
	private List<ParametroValeTransporteHistoricoVO> montarDadosLista(SqlRowSet tabelaResultado) throws Exception {
		List<ParametroValeTransporteHistoricoVO> tiposEmprestimos = new ArrayList<>();

        while(tabelaResultado.next()) {
        	tiposEmprestimos.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
		return tiposEmprestimos;
	}

	@Override
	public ParametroValeTransporteHistoricoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		ParametroValeTransporteHistoricoVO obj = new ParametroValeTransporteHistoricoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setValor(tabelaResultado.getBigDecimal("valor"));
		obj.setInicioVigencia(tabelaResultado.getDate("iniciovigencia"));
		obj.setFimVigencia(tabelaResultado.getDate("fimvigencia"));
		obj.setDataAlteracao(tabelaResultado.getDate("dataalteracao"));
		obj.setDescricao(tabelaResultado.getString("descricao"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("tipolinhatransporte"))) {
			obj.setTipoLinhaTransporte(getFacadeFactory().getTipoTransporteInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("tipolinhatransporte")));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipotarifa"))) {
			obj.setTipoTarifa(TipoTarifaEnum.valueOf(tabelaResultado.getString("tipotarifa")));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("usuario.codigo"))) {
			obj.getUsuarioResponsavelAlteracao().setCodigo(tabelaResultado.getInt("usuario.codigo"));
			obj.getUsuarioResponsavelAlteracao().setNome(tabelaResultado.getString("usuario.nome"));
		}

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("parametrovaletransporte.codigo"))) {
			obj.getParametroValeTransporte().setCodigo(tabelaResultado.getInt("parametrovaletransporte.codigo"));
			obj.getParametroValeTransporte().setDescricao(tabelaResultado.getString("parametrovaletransporte.descricao"));
		}

		return obj;
	}

	private String getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT parametrohistorico.codigo, parametrohistorico.valor, parametrohistorico.iniciovigencia, parametrohistorico.fimvigencia,");
		sql.append(" parametrohistorico.dataalteracao, parametrohistorico.usuarioresponsavelalteracao, parametrohistorico.parametrovaletransporte, ");
		sql.append(" parametrohistorico.descricao, parametrohistorico.tipotarifa, parametrohistorico.tipolinhatransporte, ");
		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", parametrovaletransporte.codigo as \"parametrovaletransporte.codigo\", ");
		sql.append(" parametrovaletransporte.descricao as \"parametrovaletransporte.descricao\"");
		sql.append(" FROM public.parametrovaletransportehistorico as parametrohistorico");
		sql.append(" INNER JOIN usuario ON parametrohistorico.usuarioresponsavelalteracao = usuario.codigo");
		sql.append(" INNER JOIN parametrovaletransporte ON parametrovaletransporte.codigo = parametrohistorico.parametrovaletransporte");

		return sql.toString();
	} 

	@Override
	public ParametroValeTransporteHistoricoVO montarDados(ParametroValeTransporteVO parametroValeTransporteEdicao, UsuarioVO usuarioVO) throws Exception {
		ParametroValeTransporteHistoricoVO obj = new ParametroValeTransporteHistoricoVO();
		obj.setValor(parametroValeTransporteEdicao.getValor());
		obj.setInicioVigencia(parametroValeTransporteEdicao.getInicioVigencia());
		obj.setFimVigencia(parametroValeTransporteEdicao.getFimVigencia());
		obj.setDataAlteracao(new Date());
		obj.setUsuarioResponsavelAlteracao(usuarioVO);
		obj.setParametroValeTransporte(parametroValeTransporteEdicao);
		obj.setDescricao(parametroValeTransporteEdicao.getDescricao());
		obj.setTipoLinhaTransporte(getFacadeFactory().getTipoTransporteInterfaceFacade().consultarPorChavePrimaria(parametroValeTransporteEdicao.getTipoLinhaTransporte().getCodigo().longValue()));
		obj.setTipoTarifa(parametroValeTransporteEdicao.getTipoTarifa());
		return obj;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ParametroValeTransporteHistorico.idEntidade = idEntidade;
	}

}
