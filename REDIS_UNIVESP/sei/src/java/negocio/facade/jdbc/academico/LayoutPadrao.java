package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.OrdenadorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.LayoutPadraoInterfaceFacade;
import relatorio.negocio.comuns.academico.enumeradores.TipoFiltroPeriodoAcademicoEnum;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioContaReceberVO;

@Repository
@Scope("singleton")
@Lazy
public class LayoutPadrao extends ControleAcesso implements LayoutPadraoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6878636770892492195L;
	protected static String idEntidade;

	public LayoutPadrao() throws Exception {
		super();
		setIdEntidade("LayoutPadrao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LayoutPadraoVO obj) throws Exception {
		try {
			final String sql = "INSERT INTO LayoutPadrao (entidade, campo, valor, assinaturaFunc1, assinaturaFunc2, assinaturaFunc3, assinaturaFunc4, assinaturaFunc5, apresentarTopoRelatorio, tituloAssinaturaFunc1, tituloAssinaturaFunc2, tituloAssinaturaFunc3, tituloAssinaturaFunc4, tituloAssinaturaFunc5, observacaoComplementarIntegralizado, textoCertificadoEstudo, nomeCargo1Apresentar, nomeCargo2Apresentar, nomeCargo3Apresentar, nomeCargo4Apresentar, nomeCargo5Apresentar, usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getEntidade());
					sqlInserir.setString(2, obj.getCampo());
					sqlInserir.setString(3, obj.getValor());
					if (Uteis.isAtributoPreenchido(obj.getAssinaturaFunc1())) {
						sqlInserir.setInt(4, obj.getAssinaturaFunc1());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getAssinaturaFunc2())) {
						sqlInserir.setInt(5, obj.getAssinaturaFunc2());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getAssinaturaFunc3())) {
						sqlInserir.setInt(6, obj.getAssinaturaFunc3());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getAssinaturaFunc4())) {
						sqlInserir.setInt(7, obj.getAssinaturaFunc4());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getAssinaturaFunc5())) {
						sqlInserir.setInt(8, obj.getAssinaturaFunc5());
					} else {
						sqlInserir.setNull(8, 0);
					}					
					if (obj.getApresentarTopoRelatorio() != null) {
						sqlInserir.setBoolean(9, obj.getApresentarTopoRelatorio());
					} else {
						sqlInserir.setNull(9, 0);
					}
					sqlInserir.setString(10, obj.getTituloAssinaturaFunc1());
					sqlInserir.setString(11, obj.getTituloAssinaturaFunc2());
					sqlInserir.setString(12, obj.getTituloAssinaturaFunc3());
					sqlInserir.setString(13, obj.getTituloAssinaturaFunc4());
					sqlInserir.setString(14, obj.getTituloAssinaturaFunc5());
					sqlInserir.setString(15, obj.getObservacaoComplementarIntegralizado());
					sqlInserir.setString(16, obj.getTextoCertidaoEstudo());
					sqlInserir.setString(17, obj.getNomeCargo1Apresentar());
					sqlInserir.setString(18, obj.getNomeCargo2Apresentar());
					sqlInserir.setString(19, obj.getNomeCargo3Apresentar());
					sqlInserir.setString(20, obj.getNomeCargo4Apresentar());
					sqlInserir.setString(21, obj.getNomeCargo5Apresentar());
					sqlInserir.setInt(22, obj.getUsuario());
					return sqlInserir;
				}
			});
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LayoutPadraoVO obj) throws Exception {
		try {
			String sql = "DELETE FROM LayoutPadrao WHERE (valor = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getValor() });
		} catch (Exception e) {
			throw e;
		}
	}

	public List<LayoutPadraoVO> consultarPorEntidade(String entidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM LayoutPadrao WHERE  entidade = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { entidade });
		List<LayoutPadraoVO> layoutPadraoVOs =  new ArrayList<LayoutPadraoVO>();
		while(tabelaResultado.next()) {
			layoutPadraoVOs.add(montarDados(tabelaResultado));	
		}
		return layoutPadraoVOs;
	}

	public LayoutPadraoVO consultarPorCampo(String campo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM LayoutPadrao WHERE  campo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { campo });
		if (!tabelaResultado.next()) {
			return new LayoutPadraoVO();
		}
		return montarDados(tabelaResultado);
	}

	public LayoutPadraoVO consultarPorValor(String valor, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM LayoutPadrao WHERE  valor = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { valor });
		if (!tabelaResultado.next()) {
			return new LayoutPadraoVO();
		}
		return montarDados(tabelaResultado);
	}

	public LayoutPadraoVO consultarPorEntidadeCampoValor(String entidade, String campo, String valor, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> lista = new ArrayList<>();
		String sqlStr = "SELECT * FROM LayoutPadrao WHERE entidade = ? and campo = ? and valor = ? ";
		lista.add(entidade);
		lista.add(campo);
		lista.add(valor);
		if (Uteis.isAtributoPreenchido(usuario)) {
			lista.add(usuario.getCodigo());
			sqlStr += "AND ((usuario IS NOT NULL AND usuario = ?) OR (usuario IS NULL)) ";
			sqlStr += "ORDER BY CASE WHEN usuario IS NOT NULL THEN 0 ELSE 1 END LIMIT 1";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, lista.toArray());
		if (!tabelaResultado.next()) {
			return new LayoutPadraoVO();
		}
		return montarDados(tabelaResultado);
	}

	public LayoutPadraoVO consultarPorEntidadeCampo(String entidade, String campo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM LayoutPadrao WHERE entidade = ? and campo = ?"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { entidade, campo });
		if (!tabelaResultado.next()) {
			return new LayoutPadraoVO();
		}
		return montarDados(tabelaResultado);
	}

	public static LayoutPadraoVO montarDados(SqlRowSet dadosSQL) throws Exception {
		LayoutPadraoVO obj = new LayoutPadraoVO();
		obj.setEntidade(dadosSQL.getString("entidade"));
		obj.setCampo(dadosSQL.getString("campo"));
		obj.setValor(dadosSQL.getString("valor"));
		obj.setAssinaturaFunc1(dadosSQL.getInt("assinaturaFunc1"));
		obj.setAssinaturaFunc2(dadosSQL.getInt("assinaturaFunc2"));
		obj.setAssinaturaFunc3(dadosSQL.getInt("assinaturaFunc3"));
		obj.setAssinaturaFunc4(dadosSQL.getInt("assinaturaFunc4"));
		obj.setAssinaturaFunc5(dadosSQL.getInt("assinaturaFunc5"));
		obj.setTituloAssinaturaFunc1(dadosSQL.getString("tituloAssinaturaFunc1"));
		obj.setTituloAssinaturaFunc2(dadosSQL.getString("tituloAssinaturaFunc2"));
		obj.setTituloAssinaturaFunc3(dadosSQL.getString("tituloAssinaturaFunc3"));
		obj.setTituloAssinaturaFunc4(dadosSQL.getString("tituloAssinaturaFunc4"));
		obj.setTituloAssinaturaFunc5(dadosSQL.getString("tituloAssinaturaFunc5"));
		obj.setApresentarTopoRelatorio(dadosSQL.getBoolean("apresentarTopoRelatorio"));
		obj.setObservacaoComplementarIntegralizado(dadosSQL.getString("observacaoComplementarIntegralizado"));
		obj.setTextoCertidaoEstudo(dadosSQL.getString("textoCertificadoEstudo"));
		obj.setNomeCargo1Apresentar(dadosSQL.getString("nomeCargo1Apresentar"));
		obj.setNomeCargo2Apresentar(dadosSQL.getString("nomeCargo2Apresentar"));
		obj.setNomeCargo2Apresentar(dadosSQL.getString("nomeCargo3Apresentar"));
		obj.setNomeCargo2Apresentar(dadosSQL.getString("nomeCargo4Apresentar"));
		obj.setNomeCargo2Apresentar(dadosSQL.getString("nomeCargo5Apresentar"));
		obj.setTituloRelatorio(dadosSQL.getString("tituloRelatorio"));
		obj.setCodigo(dadosSQL.getInt("codigo"));
		return obj;
	}

	public static String getIdEntidade() {
		return LayoutPadrao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		LayoutPadrao.idEntidade = idEntidade;
	}

	/**
	 * Método que persiste um layout favorito para uma determinada entidade.
	 * Exclui e Inclui dados no banco, deixando somente um registro para cada
	 * campo de Layout Padrão.
	 * 
	 * @param valor
	 * @param entidade
	 * @param campo
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadrao(String valor, String entidade, String campo, UsuarioVO usuario) throws Exception {
		persistirLayoutPadrao(valor, entidade, campo, null, null, null, null, null, null, null, "", "", usuario, "", "");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadrao(String valor, String entidade, String campo, Integer assinaturaFunc1, Integer assinaturaFunc2, Integer assinaturaFunc3, Boolean apresentarTopoRelatorio, String tituloAssinaturaFunc1, String tituloAssinaturaFunc2, String tituloAssinaturaFunc3, String observacaoComplementarIntegralizado, String textoCertidaoEstudo, UsuarioVO usuario, String nomeCargo1Apresentar, String nomeCargo2Apresentar) throws Exception {
		persistirLayoutPadrao(valor, entidade, campo, assinaturaFunc1, assinaturaFunc2, assinaturaFunc3, null, null, apresentarTopoRelatorio, tituloAssinaturaFunc1, tituloAssinaturaFunc2, tituloAssinaturaFunc3, null, null, observacaoComplementarIntegralizado, textoCertidaoEstudo, usuario, nomeCargo1Apresentar, nomeCargo2Apresentar, null, null, null);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadrao(String valor, String entidade, String campo, Integer assinaturaFunc1, Integer assinaturaFunc2, Integer assinaturaFunc3, Integer assinaturaFunc4, Integer assinaturaFunc5, Boolean apresentarTopoRelatorio, String tituloAssinaturaFunc1, String tituloAssinaturaFunc2, String tituloAssinaturaFunc3, String tituloAssinaturaFunc4, String tituloAssinaturaFunc5, String observacaoComplementarIntegralizado, String textoCertidaoEstudo, UsuarioVO usuario, String nomeCargo1Apresentar, String nomeCargo2Apresentar, String nomeCargo3Apresentar, String nomeCargo4Apresentar, String nomeCargo5Apresentar) throws Exception {
		LayoutPadraoVO layoutPadraoVO = new LayoutPadraoVO();
		layoutPadraoVO = consultarPorEntidadeCampo(entidade, campo, false, usuario);
		if (!Uteis.isAtributoPreenchido(layoutPadraoVO)) {
			layoutPadraoVO.setCampo(campo);
			layoutPadraoVO.setEntidade(entidade);
			layoutPadraoVO.setValor(valor);
			layoutPadraoVO.setUsuario(Uteis.isAtributoPreenchido(usuario) ? usuario.getCodigo() : null);
			layoutPadraoVO.setAssinaturaFunc1(assinaturaFunc1);
			layoutPadraoVO.setAssinaturaFunc2(assinaturaFunc2);
			layoutPadraoVO.setAssinaturaFunc3(assinaturaFunc3);
			layoutPadraoVO.setAssinaturaFunc4(assinaturaFunc4);
			layoutPadraoVO.setAssinaturaFunc5(assinaturaFunc5);
			layoutPadraoVO.setTituloAssinaturaFunc1(tituloAssinaturaFunc1);
			layoutPadraoVO.setTituloAssinaturaFunc2(tituloAssinaturaFunc2);
			layoutPadraoVO.setTituloAssinaturaFunc3(tituloAssinaturaFunc3);
			layoutPadraoVO.setTituloAssinaturaFunc4(tituloAssinaturaFunc4);
			layoutPadraoVO.setTituloAssinaturaFunc5(tituloAssinaturaFunc5);
			layoutPadraoVO.setApresentarTopoRelatorio(apresentarTopoRelatorio);
			layoutPadraoVO.setObservacaoComplementarIntegralizado(observacaoComplementarIntegralizado);
			layoutPadraoVO.setTextoCertidaoEstudo(textoCertidaoEstudo);			
			layoutPadraoVO.setNomeCargo1Apresentar(nomeCargo1Apresentar);
			layoutPadraoVO.setNomeCargo2Apresentar(nomeCargo2Apresentar);
			layoutPadraoVO.setNomeCargo3Apresentar(nomeCargo3Apresentar);
			layoutPadraoVO.setNomeCargo4Apresentar(nomeCargo4Apresentar);
			layoutPadraoVO.setNomeCargo5Apresentar(nomeCargo5Apresentar);
			incluir(layoutPadraoVO);
		} else {
			excluir(layoutPadraoVO);
			layoutPadraoVO.setValor(valor);
			layoutPadraoVO.setUsuario(Uteis.isAtributoPreenchido(usuario) ? usuario.getCodigo() : null);
			layoutPadraoVO.setAssinaturaFunc1(assinaturaFunc1);
			layoutPadraoVO.setAssinaturaFunc2(assinaturaFunc2);
			layoutPadraoVO.setAssinaturaFunc3(assinaturaFunc3);
			layoutPadraoVO.setAssinaturaFunc4(assinaturaFunc4);
			layoutPadraoVO.setAssinaturaFunc5(assinaturaFunc5);
			layoutPadraoVO.setTituloAssinaturaFunc1(tituloAssinaturaFunc1);
			layoutPadraoVO.setTituloAssinaturaFunc2(tituloAssinaturaFunc2);
			layoutPadraoVO.setTituloAssinaturaFunc3(tituloAssinaturaFunc3);
			layoutPadraoVO.setTituloAssinaturaFunc4(tituloAssinaturaFunc4);
			layoutPadraoVO.setTituloAssinaturaFunc5(tituloAssinaturaFunc5);
			layoutPadraoVO.setApresentarTopoRelatorio(apresentarTopoRelatorio);
			layoutPadraoVO.setObservacaoComplementarIntegralizado(observacaoComplementarIntegralizado);
			layoutPadraoVO.setTextoCertidaoEstudo(textoCertidaoEstudo);
			layoutPadraoVO.setNomeCargo1Apresentar(nomeCargo1Apresentar);
			layoutPadraoVO.setNomeCargo2Apresentar(nomeCargo2Apresentar);
			layoutPadraoVO.setNomeCargo3Apresentar(nomeCargo3Apresentar);
			layoutPadraoVO.setNomeCargo4Apresentar(nomeCargo4Apresentar);
			layoutPadraoVO.setNomeCargo5Apresentar(nomeCargo5Apresentar);
			incluir(layoutPadraoVO);
		}
		layoutPadraoVO = null;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadrao2(final String valor,final String entidade,final String campo,final Integer assinaturaFunc1,final Integer assinaturaFunc2,final Boolean apresentarTopoRelatorio, 
			final String tituloAssinaturaFunc1,final String tituloAssinaturaFunc2,final String observacaoComplementarIntegralizado,final String textoCertidaoEstudo, UsuarioVO usuario) throws Exception {
		try {
			try {
				if(alterar(valor, entidade, campo, assinaturaFunc1, assinaturaFunc2, apresentarTopoRelatorio, tituloAssinaturaFunc1, tituloAssinaturaFunc2, observacaoComplementarIntegralizado, textoCertidaoEstudo, usuario) == 0){
					incluir(valor, entidade, campo, assinaturaFunc1, assinaturaFunc2, apresentarTopoRelatorio, tituloAssinaturaFunc1, tituloAssinaturaFunc2, observacaoComplementarIntegralizado, textoCertidaoEstudo, usuario);
				}
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final String valor,final String entidade,final String campo,final Integer assinaturaFunc1,final Integer assinaturaFunc2,final Boolean apresentarTopoRelatorio, 
			final String tituloAssinaturaFunc1,final String tituloAssinaturaFunc2,final String observacaoComplementarIntegralizado,final String textoCertidaoEstudo, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LayoutPadrao (entidade, campo, valor, assinaturaFunc1, assinaturaFunc2, apresentarTopoRelatorio, tituloAssinaturaFunc1, tituloAssinaturaFunc2, observacaoComplementarIntegralizado, textoCertificadoEstudo, usuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, entidade);
					sqlInserir.setString(2, campo);
					sqlInserir.setString(3, valor);
					if (Uteis.isAtributoPreenchido(assinaturaFunc1)) {
						sqlInserir.setInt(4, assinaturaFunc1);
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (Uteis.isAtributoPreenchido(assinaturaFunc2)) {
						sqlInserir.setInt(5, assinaturaFunc2);
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (apresentarTopoRelatorio != null) {
						sqlInserir.setBoolean(6, apresentarTopoRelatorio);
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, tituloAssinaturaFunc1);
					sqlInserir.setString(8, tituloAssinaturaFunc2);
					sqlInserir.setString(9, observacaoComplementarIntegralizado);
					sqlInserir.setString(10, textoCertidaoEstudo);
					sqlInserir.setInt(11, usuario.getCodigo());
					return sqlInserir;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private Integer alterar(final String valor,final String entidade,final String campo,final Integer assinaturaFunc1,final Integer assinaturaFunc2,final Boolean apresentarTopoRelatorio, 
			final String tituloAssinaturaFunc1,final String tituloAssinaturaFunc2,final String observacaoComplementarIntegralizado,final String textoCertidaoEstudo, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE LayoutPadrao set entidade = ?, campo= ?, valor= ?, assinaturaFunc1= ?, assinaturaFunc2= ?, apresentarTopoRelatorio= ?, tituloAssinaturaFunc1= ?, tituloAssinaturaFunc2= ?, observacaoComplementarIntegralizado= ?, textoCertificadoEstudo= ?, usuario= ? where entidade = ? and campo= ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			return getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, entidade);
					sqlInserir.setString(2, campo);
					sqlInserir.setString(3, valor);
					if (Uteis.isAtributoPreenchido(assinaturaFunc1)) {
						sqlInserir.setInt(4, assinaturaFunc1);
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (Uteis.isAtributoPreenchido(assinaturaFunc2)) {
						sqlInserir.setInt(5, assinaturaFunc2);
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (apresentarTopoRelatorio != null) {
						sqlInserir.setBoolean(6, apresentarTopoRelatorio);
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, tituloAssinaturaFunc1);
					sqlInserir.setString(8, tituloAssinaturaFunc2);
					sqlInserir.setString(9, observacaoComplementarIntegralizado);
					sqlInserir.setString(10, textoCertidaoEstudo);
					sqlInserir.setInt(11, usuario.getCodigo());
					sqlInserir.setString(12, entidade);
					sqlInserir.setString(13, campo);
					return sqlInserir;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final String valor,final String entidade,final String campo, final String agrupador, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LayoutPadrao (entidade, campo, valor, agrupador, usuario) VALUES (?, ?, ?, ?, ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, entidade);
					sqlInserir.setString(2, campo);
					sqlInserir.setString(3, valor);					
					sqlInserir.setString(4, agrupador);
					sqlInserir.setInt(5, usuario.getCodigo());
					return sqlInserir;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluirComTituloRelatorio(final String valor, final String tituloRelatorio, final String entidade,final String campo, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LayoutPadrao (entidade, campo, valor, tituloRelatorio, usuario) VALUES (?, ?, ?, ?, ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, entidade);
					sqlInserir.setString(2, campo);
					sqlInserir.setString(3, valor);					
					sqlInserir.setString(4, tituloRelatorio);
					sqlInserir.setInt(5, usuario.getCodigo());
					return sqlInserir;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private Integer alterar(final String valor,final String entidade,final String campo, final  String agrupador, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE LayoutPadrao set valor = ? where entidade = ? and campo = ? and agrupador = ? and usuario = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			return getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, valor);					
					sqlInserir.setString(2, entidade);
					sqlInserir.setString(3, campo);
					sqlInserir.setString(4, agrupador);
					sqlInserir.setInt(5, usuario.getCodigo());
					return sqlInserir;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private Integer alterarComTituloRelatorio(final String valor, final String tituloRelatorio, final String entidade,final String campo, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE LayoutPadrao set valor = ?, tituloRelatorio=? where entidade = ? and campo = ? and usuario = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			return getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, valor);					
					sqlInserir.setString(2, tituloRelatorio);
					sqlInserir.setString(3, entidade);
					sqlInserir.setString(4, campo);
					sqlInserir.setInt(5, usuario.getCodigo());
					return sqlInserir;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadrao2(String valor, String entidade, String campo, UsuarioVO usuario) throws Exception {
		try {
			if(alterar(valor, entidade, campo, "", usuario).equals(0)){
				incluir(valor, entidade, campo, "", usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
		
	@Override
	public Map<String, String> consultarValoresPadroes(String[] campos, String entidade){
		Map<String, String> resultado = new HashMap<String, String>(0);
		if(entidade != null && !entidade.trim().isEmpty()){		
			StringBuilder sql  = new StringBuilder("select entidade, campo, valor from LayoutPadrao where entidade = ? and (agrupador is null or agrupador = '') ");
			if(campos != null && campos.length > 0 ){
				sql.append(" and campo in (''");
				for(String campo: campos){
					sql.append(", '").append(campo).append("'");
				}
				sql.append(") ");
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), entidade);
			while(rs.next()){
				resultado.put(rs.getString("campo"), rs.getString("valor"));
			}
		}
		return resultado;		
	}		
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirFiltroSituacaoAcademica(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getAbandonado().toString(), entidade, "abandonado", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getAtivo().toString(), entidade, "ativo", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getCancelado().toString(), entidade, "cancelado", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getConcluido().toString(), entidade, "concluido", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getConfirmado().toString(), entidade, "confirmado", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getFormado().toString(), entidade, "formado", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getConsiderarSituacaoAtualMatricula().toString(), entidade, "considerarSituacaoAtualMatricula", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getPendenteFinanceiro().toString(), entidade, "pendenteFinanceiro", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getPreMatricula().toString(), entidade, "preMatricula", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getPreMatriculaCancelada().toString(), entidade, "preMatriculaCancelada", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getTrancado().toString(), entidade, "trancado", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getTransferenciaExterna().toString(), entidade, "transferenciaExterna", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getTransferenciaInterna().toString(), entidade, "transferenciaInterna", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getTipoFiltroPeriodoAcademico().name(), entidade, "tipoFiltroPeriodoAcademico", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getFormaIngresso().name(), entidade, "formaIngresso", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getApresentarSubtotalFormaIngresso().toString(), entidade, "apresentarSubtotalFormaIngresso", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getPeriodicidadeEnum().toString(), entidade, "periodicidadeEnum", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getAno().toString(), entidade, "ano", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getSemestre().toString(), entidade, "semestre", usuarioVO);
		persistirLayoutPadrao2(Uteis.getDataAno4Digitos(filtroRelatorioAcademicoVO.getDataInicio()), entidade, "dataInicio", usuarioVO);
		persistirLayoutPadrao2(Uteis.getDataAno4Digitos(filtroRelatorioAcademicoVO.getDataTermino()), entidade, "dataTermino", usuarioVO);
		persistirLayoutPadrao2(Uteis.getDataAno4Digitos(filtroRelatorioAcademicoVO.getDataInicioPeriodoAula()), entidade, "dataInicioPeriodoAula", usuarioVO);
		persistirLayoutPadrao2(Uteis.getDataAno4Digitos(filtroRelatorioAcademicoVO.getDataTerminoPeriodoAula()), entidade, "dataTerminoPeriodoAula", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getJubilado().toString(), entidade, "jubilado", usuarioVO);
	}
	
	
	
	@Override	
	public void consultarPadraoFiltroSituacaoAcademica(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		Map<String, String> campoValor = consultarValoresPadroes(new String[]{"abandonado", "ativo", "cancelado", "concluido", 
				"confirmado", "formado", "pendenteFinanceiro", "preMatricula", "preMatriculaCancelada", "trancado",
				"transferenciaExterna", "transferenciaInterna", "apresentarSubtotalFormaIngresso", "formaIngresso", "periodicidadeEnum", "dataInicio", "dataTermino"},  entidade);
		if(campoValor == null){
			return;
		}
		if(campoValor.containsKey("abandonado")){
			filtroRelatorioAcademicoVO.setAbandonado(Boolean.valueOf(campoValor.get("abandonado")));
		}
		if(campoValor.containsKey("ativo")){
			filtroRelatorioAcademicoVO.setAtivo(Boolean.valueOf(campoValor.get("ativo")));
		}
		if(campoValor.containsKey("cancelado")){
			filtroRelatorioAcademicoVO.setCancelado(Boolean.valueOf(campoValor.get("cancelado")));
		}
		if(campoValor.containsKey("concluido")){
			filtroRelatorioAcademicoVO.setConcluido(Boolean.valueOf(campoValor.get("concluido")));
		}
		if(campoValor.containsKey("confirmado")){
			filtroRelatorioAcademicoVO.setConfirmado(Boolean.valueOf(campoValor.get("confirmado")));
		}
		if(campoValor.containsKey("formado")){
			filtroRelatorioAcademicoVO.setFormado(Boolean.valueOf(campoValor.get("formado")));
		}
		if(campoValor.containsKey("considerarSituacaoAtualMatricula")){
			filtroRelatorioAcademicoVO.setConsiderarSituacaoAtualMatricula(Boolean.valueOf(campoValor.get("considerarSituacaoAtualMatricula")));
		}
		if(campoValor.containsKey("pendenteFinanceiro")){
			filtroRelatorioAcademicoVO.setPendenteFinanceiro(Boolean.valueOf(campoValor.get("pendenteFinanceiro")));
		}
		if(campoValor.containsKey("preMatricula")){
			filtroRelatorioAcademicoVO.setPreMatricula(Boolean.valueOf(campoValor.get("preMatricula")));
		}
		if(campoValor.containsKey("preMatriculaCancelada")){
			filtroRelatorioAcademicoVO.setPreMatriculaCancelada(Boolean.valueOf(campoValor.get("preMatriculaCancelada")));
		}
		if(campoValor.containsKey("trancado")){
			filtroRelatorioAcademicoVO.setTrancado(Boolean.valueOf(campoValor.get("trancado")));
		}
		if(campoValor.containsKey("transferenciaExterna")){
			filtroRelatorioAcademicoVO.setTransferenciaExterna(Boolean.valueOf(campoValor.get("transferenciaExterna")));
		}
		if(campoValor.containsKey("transferenciaInterna")){
			filtroRelatorioAcademicoVO.setTransferenciaInterna(Boolean.valueOf(campoValor.get("transferenciaInterna")));
		}
		if(campoValor.containsKey("tipoFiltroPeriodoAcademico") && Uteis.isAtributoPreenchido(campoValor.get("tipoFiltroPeriodoAcademico"))){
			filtroRelatorioAcademicoVO.setTipoFiltroPeriodoAcademico(TipoFiltroPeriodoAcademicoEnum.valueOf(campoValor.get("tipoFiltroPeriodoAcademico")));
		}
		if(campoValor.containsKey("apresentarSubtotalFormaIngresso")){
			filtroRelatorioAcademicoVO.setApresentarSubtotalFormaIngresso(Boolean.valueOf(campoValor.get("apresentarSubtotalFormaIngresso")));
		}
		if(campoValor.containsKey("formaIngresso") && Uteis.isAtributoPreenchido(campoValor.get("formaIngresso"))){
			filtroRelatorioAcademicoVO.setFormaIngresso(FormaIngresso.valueOf(campoValor.get("formaIngresso")));
		}
		if(campoValor.containsKey("periodicidadeEnum") && Uteis.isAtributoPreenchido(campoValor.get("periodicidadeEnum"))){
			filtroRelatorioAcademicoVO.setPeriodicidadeEnum(PeriodicidadeEnum.valueOf(campoValor.get("periodicidadeEnum")));
		}
		if(campoValor.containsKey("periodicidadeEnum") && Uteis.isAtributoPreenchido(campoValor.get("periodicidadeEnum"))){
			filtroRelatorioAcademicoVO.setPeriodicidadeEnum(PeriodicidadeEnum.valueOf(campoValor.get("periodicidadeEnum")));
		}
		if(campoValor.containsKey("dataInicio") && Uteis.isAtributoPreenchido(campoValor.get("dataInicio"))){
			filtroRelatorioAcademicoVO.setDataInicio(Uteis.getDate(campoValor.get("dataInicio")));			
		}
		if(campoValor.containsKey("dataInicioPeriodoAula") && Uteis.isAtributoPreenchido(campoValor.get("dataInicioPeriodoAula"))){			
			filtroRelatorioAcademicoVO.setDataInicioPeriodoAula(Uteis.getDate(campoValor.get("dataInicioPeriodoAula")));
		}
		if(campoValor.containsKey("dataTermino") && Uteis.isAtributoPreenchido(campoValor.get("dataTermino"))){			
			filtroRelatorioAcademicoVO.setDataTermino(Uteis.getDate(campoValor.get("dataTermino")));
		}
		if(campoValor.containsKey("dataTerminoPeriodoAula") && Uteis.isAtributoPreenchido(campoValor.get("dataTerminoPeriodoAula"))){
			filtroRelatorioAcademicoVO.setDataTerminoPeriodoAula(Uteis.getDate(campoValor.get("dataTerminoPeriodoAula")));
			
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirFiltroSituacaoHistorico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getReprovado().toString(), entidade, "reprovado", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getAprovado().toString(), entidade, "aprovado", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getNaoCursada().toString(), entidade, "naoCursada", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getCursando().toString(), entidade, "cursando", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getTrancamentoHistorico().toString(), entidade, "trancamentoHistorico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getAbandonoHistorico().toString(), entidade, "abandonoHistorico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getCanceladoHistorico().toString(), entidade, "canceladoHistorico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getTransferidoHistorico().toString(), entidade, "transferidoHistorico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getConcessaoCreditoHistorico().toString(), entidade, "concessaoCreditoHistorico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getConcessaoCargaHorariaHistorico().toString(), entidade, "concessaoCargaHorariaHistorico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioAcademicoVO.getPreMatriculaHistorico().toString(), entidade, "preMatriculaHistorico", usuarioVO);		
	}
	
	@Override	
	public void consultarPadraoFiltroSituacaoHistorico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		Map<String, String> campoValor = consultarValoresPadroes(new String[]{"reprovado", "aprovado", "naoCursada", 
				"cursando", "trancamentoHistorico", "abandonoHistorico", "canceladoHistorico", "transferidoHistorico", "concessaoCreditoHistorico",
				"concessaoCargaHorariaHistorico", "preMatriculaHistorico"},  entidade);
		if(campoValor == null){
			return;
		}
		if(campoValor.containsKey("reprovado")){
			filtroRelatorioAcademicoVO.setReprovado(Boolean.valueOf(campoValor.get("reprovado")));
		}
		if(campoValor.containsKey("aprovado")){
			filtroRelatorioAcademicoVO.setAprovado(Boolean.valueOf(campoValor.get("aprovado")));
		}
		if(campoValor.containsKey("naoCursada")){
			filtroRelatorioAcademicoVO.setNaoCursada(Boolean.valueOf(campoValor.get("naoCursada")));
		}
		if(campoValor.containsKey("cursando")){
			filtroRelatorioAcademicoVO.setCursando(Boolean.valueOf(campoValor.get("cursando")));
		}
		if(campoValor.containsKey("trancamentoHistorico")){
			filtroRelatorioAcademicoVO.setTrancamentoHistorico(Boolean.valueOf(campoValor.get("trancamentoHistorico")));
		}
		if(campoValor.containsKey("abandonoHistorico")){
			filtroRelatorioAcademicoVO.setAbandonoHistorico(Boolean.valueOf(campoValor.get("abandonoHistorico")));
		}
		if(campoValor.containsKey("canceladoHistorico")){
			filtroRelatorioAcademicoVO.setCanceladoHistorico(Boolean.valueOf(campoValor.get("canceladoHistorico")));
		}
		if(campoValor.containsKey("transferidoHistorico")){
			filtroRelatorioAcademicoVO.setTransferidoHistorico(Boolean.valueOf(campoValor.get("transferidoHistorico")));
		}
		if(campoValor.containsKey("concessaoCreditoHistorico")){
			filtroRelatorioAcademicoVO.setConcessaoCreditoHistorico(Boolean.valueOf(campoValor.get("concessaoCreditoHistorico")));
		}
		if(campoValor.containsKey("concessaoCargaHorariaHistorico")){
			filtroRelatorioAcademicoVO.setConcessaoCargaHorariaHistorico(Boolean.valueOf(campoValor.get("concessaoCargaHorariaHistorico")));
		}
		if(campoValor.containsKey("preMatriculaHistorico")){
			filtroRelatorioAcademicoVO.setPreMatriculaHistorico(Boolean.valueOf(campoValor.get("preMatriculaHistorico")));
		}		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirFiltroTipoOrigemContaReceber(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemBiblioteca().toString(), entidade, "tipoOrigemBiblioteca", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio().toString(), entidade, "tipoOrigemBolsaCusteadaConvenio", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemContratoReceita().toString(), entidade, "tipoOrigemContratoReceita", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemDevolucaoCheque().toString(), entidade, "tipoOrigemDevolucaoCheque", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemInclusaoReposicao().toString(), entidade, "tipoOrigemInclusaoReposicao", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemInscricaoProcessoSeletivo().toString(), entidade, "tipoOrigemInscricaoProcessoSeletivo", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemMatricula().toString(), entidade, "tipoOrigemMatricula", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemMensalidade().toString(), entidade, "tipoOrigemMensalidade", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemMaterialDidatico().toString(), entidade, "tipoOrigemMaterialDidatico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemNegociacao().toString(), entidade, "tipoOrigemNegociacao", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemOutros().toString(), entidade, "tipoOrigemOutros", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento().toString(), entidade, "tipoOrigemRequerimento", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getSituacaoCancelado().toString(), entidade, "situacaoCancelado", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getSituacaoPagar().toString(), entidade, "situacaoPagar", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getSituacaoPago().toString(), entidade, "situacaoPago", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getSituacaoReceber().toString(), entidade, "situacaoReceber", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getSituacaoRecebido().toString(), entidade, "situacaoRecebido", usuarioVO);					
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getSituacaoRenegociado().toString(), entidade, "situacaoRenegociado", usuarioVO);											
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getFiltrarPorDataCompetencia().toString(), entidade, "filtrarPorDataCompetencia", usuarioVO);											
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getSituacaoExcluida().toString(), entidade, "situacaoExcluida", usuarioVO);											
		persistirLayoutPadrao2(filtroRelatorioFinanceiroVO.getConsiderarUnidadeFinanceira().toString(), entidade, "considerarUnidadeFinanceira", usuarioVO);											
	}
	
	@Override	
	public void consultarPadraoFiltroTipoOrigemContaReceber(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		Map<String, String> campoValor = consultarValoresPadroes(new String[]{"tipoOrigemBiblioteca", "tipoOrigemBolsaCusteadaConvenio", "tipoOrigemContratoReceita", "tipoOrigemDevolucaoCheque", 
				"tipoOrigemInclusaoReposicao", "tipoOrigemInscricaoProcessoSeletivo", "tipoOrigemMatricula", "tipoOrigemMaterialDidatico", "tipoOrigemMensalidade", "tipoOrigemNegociacao", "tipoOrigemOutros",
				"tipoOrigemRequerimento", "situacaoCancelado", "situacaoPagar", "situacaoPago", "situacaoReceber", "situacaoRecebido", "situacaoRenegociado", "filtrarPorDataCompetencia", "situacaoExcluida", "considerarUnidadeFinanceira"},  entidade);
		if(campoValor == null){
			return;
		}
		if(campoValor.containsKey("tipoOrigemBiblioteca")){
			filtroRelatorioFinanceiroVO.setTipoOrigemBiblioteca(Boolean.valueOf(campoValor.get("tipoOrigemBiblioteca")));
		}
		if(campoValor.containsKey("tipoOrigemBolsaCusteadaConvenio")){
			filtroRelatorioFinanceiroVO.setTipoOrigemBolsaCusteadaConvenio(Boolean.valueOf(campoValor.get("tipoOrigemBolsaCusteadaConvenio")));
		}
		if(campoValor.containsKey("tipoOrigemContratoReceita")){
			filtroRelatorioFinanceiroVO.setTipoOrigemContratoReceita(Boolean.valueOf(campoValor.get("tipoOrigemContratoReceita")));
		}
		if(campoValor.containsKey("tipoOrigemDevolucaoCheque")){
			filtroRelatorioFinanceiroVO.setTipoOrigemDevolucaoCheque(Boolean.valueOf(campoValor.get("tipoOrigemDevolucaoCheque")));
		}
		if(campoValor.containsKey("tipoOrigemInclusaoReposicao")){
			filtroRelatorioFinanceiroVO.setTipoOrigemInclusaoReposicao(Boolean.valueOf(campoValor.get("tipoOrigemInclusaoReposicao")));
		}
		if(campoValor.containsKey("tipoOrigemInscricaoProcessoSeletivo")){
			filtroRelatorioFinanceiroVO.setTipoOrigemInscricaoProcessoSeletivo(Boolean.valueOf(campoValor.get("tipoOrigemInscricaoProcessoSeletivo")));
		}
		if(campoValor.containsKey("tipoOrigemMatricula")){
			filtroRelatorioFinanceiroVO.setTipoOrigemMatricula(Boolean.valueOf(campoValor.get("tipoOrigemMatricula")));
		}
		if(campoValor.containsKey("tipoOrigemMensalidade")){
			filtroRelatorioFinanceiroVO.setTipoOrigemMensalidade(Boolean.valueOf(campoValor.get("tipoOrigemMensalidade")));
		}
		if(campoValor.containsKey("tipoOrigemMaterialDidatico")){
			filtroRelatorioFinanceiroVO.setTipoOrigemMaterialDidatico(Boolean.valueOf(campoValor.get("tipoOrigemMaterialDidatico")));
		}
		if(campoValor.containsKey("tipoOrigemNegociacao")){
			filtroRelatorioFinanceiroVO.setTipoOrigemNegociacao(Boolean.valueOf(campoValor.get("tipoOrigemNegociacao")));
		}
		if(campoValor.containsKey("tipoOrigemOutros")){
			filtroRelatorioFinanceiroVO.setTipoOrigemOutros(Boolean.valueOf(campoValor.get("tipoOrigemOutros")));
		}
		if(campoValor.containsKey("tipoOrigemRequerimento")){
			filtroRelatorioFinanceiroVO.setTipoOrigemRequerimento(Boolean.valueOf(campoValor.get("tipoOrigemRequerimento")));
		}
		if(campoValor.containsKey("situacaoCancelado")){
			filtroRelatorioFinanceiroVO.setSituacaoCancelado(Boolean.valueOf(campoValor.get("situacaoCancelado")));
		}
		if(campoValor.containsKey("situacaoPagar")){
			filtroRelatorioFinanceiroVO.setSituacaoPagar(Boolean.valueOf(campoValor.get("situacaoPagar")));
		}
		if(campoValor.containsKey("situacaoPago")){
			filtroRelatorioFinanceiroVO.setSituacaoPago(Boolean.valueOf(campoValor.get("situacaoPago")));
		}
		if(campoValor.containsKey("situacaoReceber")){
			filtroRelatorioFinanceiroVO.setSituacaoReceber(Boolean.valueOf(campoValor.get("situacaoReceber")));
		}
		if(campoValor.containsKey("situacaoRecebido")){
			filtroRelatorioFinanceiroVO.setSituacaoRecebido(Boolean.valueOf(campoValor.get("situacaoRecebido")));
		}
		if(campoValor.containsKey("situacaoRenegociado")){
			filtroRelatorioFinanceiroVO.setSituacaoRenegociado(Boolean.valueOf(campoValor.get("situacaoRenegociado")));
		}
		if(campoValor.containsKey("situacaoExcluida")){
			filtroRelatorioFinanceiroVO.setSituacaoExcluida(Boolean.valueOf(campoValor.get("situacaoExcluida")));
		}
		if(campoValor.containsKey("filtrarPorDataCompetencia")){
			filtroRelatorioFinanceiroVO.setFiltrarPorDataCompetencia(Boolean.valueOf(campoValor.get("filtrarPorDataCompetencia")));
		}
		if(campoValor.containsKey("considerarUnidadeFinanceira")){
			filtroRelatorioFinanceiroVO.setConsiderarUnidadeFinanceira(Boolean.valueOf(campoValor.get("considerarUnidadeFinanceira")));
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirFiltroRelatorioContaReceberVO(FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberBiblioteca().toString(), entidade, "tipoOrigemBiblioteca", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberBolsaCusteadaConvenio().toString(), entidade, "tipoOrigemBolsaCusteadaConvenio", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberContratoReceita().toString(), entidade, "tipoOrigemContratoReceita", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberDevolucaoCheque().toString(), entidade, "tipoOrigemDevolucaoCheque", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberInclusaoReposicao().toString(), entidade, "tipoOrigemInclusaoReposicao", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberInscricaoProcessoSeletivo().toString(), entidade, "tipoOrigemInscricaoProcessoSeletivo", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberMatricula().toString(), entidade, "tipoOrigemMatricula", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberMensalidade().toString(), entidade, "tipoOrigemMensalidade", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberMaterialDidatico().toString(), entidade, "tipoOrigemMaterialDidatico", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberNegociacao().toString(), entidade, "tipoOrigemNegociacao", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberOutros().toString(), entidade, "tipoOrigemOutros", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberRequerimento().toString(), entidade, "tipoOrigemRequerimento", usuarioVO);
		persistirLayoutPadrao2(filtroRelatorioContaReceberVO.getTipoOrigemContaReceberTodos().toString(), entidade, "tipoOrigemContaReceberTodos", usuarioVO);
		
	}
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarPadraoFiltroRelatorioContaReceberVO(FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO, String entidade, UsuarioVO usuarioVO) throws Exception{
		Map<String, String> camposPadroes = consultarValoresPadroes(new String[]{"tipoOrigemBiblioteca", "tipoOrigemBolsaCusteadaConvenio", "tipoOrigemContratoReceita", "tipoOrigemDevolucaoCheque", 
				"tipoOrigemInclusaoReposicao", "tipoOrigemInscricaoProcessoSeletivo", "tipoOrigemMatricula", "tipoOrigemMaterialDidatico", "tipoOrigemMensalidade", "tipoOrigemNegociacao", "tipoOrigemOutros",
				"tipoOrigemRequerimento", "tipoOrigemContaReceberTodos"},  entidade);
		camposPadroes.entrySet().stream().forEach(p->{
			if(p.getKey().equals("tipoOrigemBiblioteca") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberBiblioteca(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemBolsaCusteadaConvenio") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberBolsaCusteadaConvenio(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemContratoReceita") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberContratoReceita(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemDevolucaoCheque") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberDevolucaoCheque(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemInclusaoReposicao") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberInclusaoReposicao(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemInscricaoProcessoSeletivo") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberInscricaoProcessoSeletivo(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemMatricula") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberMatricula(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemMensalidade") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberMensalidade(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemMaterialDidatico") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberMaterialDidatico(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemNegociacao") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberNegociacao(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemOutros") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberOutros(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemRequerimento") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberRequerimento(Boolean.parseBoolean(p.getValue()));
			}else if(p.getKey().equals("tipoOrigemContaReceberTodos") && Uteis.isAtributoPreenchido(p.getValue())){
				filtroRelatorioContaReceberVO.setTipoOrigemContaReceberTodos(Boolean.parseBoolean(p.getValue()));
			}
		});
		}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirOrdenacao(List<OrdenadorVO> ordenadorVOs, String entidade, UsuarioVO usuario) throws Exception {
		try {
			for(OrdenadorVO ordenadorVO:ordenadorVOs){
				persistirLayoutPadrao2(ordenadorVO.getOrdem().toString(), entidade, ordenadorVO.getCampoOrdenar()+"_Ordem", usuario);
				persistirLayoutPadrao2(ordenadorVO.getUtilizar().toString(), entidade, ordenadorVO.getCampoOrdenar()+"_Utilizar", usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void consultarOrdenacaoPadrao(List<OrdenadorVO> ordenadorVOs, String entidade, UsuarioVO usuario) throws Exception {
		try {
			LayoutPadraoVO layoutPadraoVO = null;
			for(OrdenadorVO ordenadorVO:ordenadorVOs){
				layoutPadraoVO  = consultarPorEntidadeCampo(entidade, ordenadorVO.getCampoOrdenar()+"_Ordem", false, usuario);
				if(Uteis.isAtributoPreenchido(layoutPadraoVO)){
					ordenadorVO.setOrdem(Integer.valueOf(layoutPadraoVO.getValor()));
				}
				layoutPadraoVO = consultarPorEntidadeCampo(entidade, ordenadorVO.getCampoOrdenar()+"_Utilizar", false, usuario);
				if(Uteis.isAtributoPreenchido(layoutPadraoVO)){
					ordenadorVO.setUtilizar(Boolean.valueOf(layoutPadraoVO.getValor()));
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadraoComAgrupador(String valor, String entidade, String campo, String agrupador, UsuarioVO usuario) throws Exception {
		try {
			if(agrupador == null || agrupador.trim().isEmpty()){
				throw new ConsistirException("O campo NOME FILTROS deve ser informado.");
			}
			if(alterar(valor, entidade, campo, agrupador, usuario) == 0){
				incluir(valor, entidade, campo, agrupador, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirLayoutPadraoComTituloRelatorio(String valor, String entidade, String campo, String tituloRelatorio, UsuarioVO usuario) throws Exception {
		try {
			if(tituloRelatorio == null || tituloRelatorio.trim().isEmpty()){
				throw new ConsistirException("O campo TÍTULO RELATÓRIO deve ser informado.");
			}
			if(alterarComTituloRelatorio(valor, tituloRelatorio, entidade, campo, usuario) == 0){
				incluirComTituloRelatorio(valor, tituloRelatorio, entidade, campo, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public List<LayoutPadraoVO> consultarAgrupadoresPorEntidade(String entidade){
		List<LayoutPadraoVO> layoutPadraoVOs = new ArrayList<LayoutPadraoVO>(0);
		if(entidade != null && !entidade.trim().isEmpty()){		
			StringBuilder sql  = new StringBuilder("select distinct agrupador, entidade, campo, valor from LayoutPadrao where entidade = ? and agrupador != ''");		
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), entidade);
			LayoutPadraoVO layoutPadraoVO = null;
			Map<String, LayoutPadraoVO> mapAgrupador = new HashMap<String, LayoutPadraoVO>(0);
			while(rs.next()){
				if(!mapAgrupador.containsKey(rs.getString("agrupador"))){
					layoutPadraoVO = new LayoutPadraoVO();
					layoutPadraoVO.setAgrupador(rs.getString("agrupador"));
					layoutPadraoVO.setEntidade(rs.getString("entidade"));
					layoutPadraoVOs.add(layoutPadraoVO);
					mapAgrupador.put(rs.getString("agrupador"), layoutPadraoVO);
				}
				layoutPadraoVO = mapAgrupador.get(rs.getString("agrupador"));
				layoutPadraoVO.getMapCampoValores().put(rs.getString("campo"), rs.getString("valor")); 
			}
		}
		return layoutPadraoVOs;		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirLayoutPorAgrupadorEntidade(String entidade, String agrupador, UsuarioVO usuarioVO){
		getConexao().getJdbcTemplate().update("delete from LayoutPadrao where entidade = ? and agrupador = ?"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), entidade, agrupador);
	}
}