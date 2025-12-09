package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LayoutIntegracaoTagVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.enumeradores.TipoCampoTagEnum;
import negocio.comuns.contabil.enumeradores.TipoTagEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.LayoutIntegracaoTagInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */

@Repository
@Scope("singleton")
@Lazy
public class LayoutIntegracaoTag extends ControleAcesso implements LayoutIntegracaoTagInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8433930267005702806L;
	protected static String idEntidade = "LayoutIntegracaoTag";

	public LayoutIntegracaoTag() {
		super();
	}

	public void validarDados(LayoutIntegracaoTagVO obj) throws Exception {

		if (!Uteis.isAtributoPreenchido(obj.getTag())) {
			throw new Exception("O campo Tag (Layout Integração Tags) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(obj.getNivel())) {
			throw new Exception("O campo Nível (Layout Integração Tags) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(obj.getTipoTagEnum())) {
			throw new Exception("O campo Tipo Tag (Layout Integração Tags) deve ser informado.");
		}

		if ((obj.getTipoTagEnum().isCampo()) && !Uteis.isAtributoPreenchido(obj.getCampo())) {
			throw new Exception("O campo Valor (Layout Integração Tags) deve ser informado.");
		}

		if ((obj.getTipoTagEnum().isCampo()) && !Uteis.isAtributoPreenchido(obj.getTamanhoTag())) {
			throw new Exception("O campo Tamanho tag (Layout Integração Tags) deve ser informado.");
		}

		if (obj.getTipoTagEnum().isCampo() && !Uteis.isAtributoPreenchido(obj.getTipoCampoTagEnum())) {
			throw new Exception("O campo Tipo Campo (Layout Integração Tags) deve ser informado.");
		}
		
		if (obj.getTipoTagEnum().isCampo() && obj.getTipoCampoTagEnum().isTipoDouble() && !Uteis.isAtributoPreenchido(obj.getSeparadorMonetaria())) {
			throw new Exception("O campo Separador Monetário (Layout Integração Tags) deve ser informado.");
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<LayoutIntegracaoTagVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (LayoutIntegracaoTagVO layoutIntegracaoXMLTagVO : lista) {
			persistir(layoutIntegracaoXMLTagVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(LayoutIntegracaoTagVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LayoutIntegracaoTagVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LayoutIntegracaoTag.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO LayoutIntegracaoTag ( layoutIntegracao, tagMae, tag, tipoTag, campo, ");
			sql.append("    tipoCampoTag, nivel, tamanhoTag, mascara, separadorMonetaria, removermascara, suprimirZeroEsquerda ) ");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getLayoutIntegracaoVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getTagMae())) {
						sqlInserir.setInt(++i, obj.getTagMae().getCodigo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getTag());
					sqlInserir.setString(++i, obj.getTipoTagEnum().name());
					if (Uteis.isAtributoPreenchido(obj.getCampo())) {
						sqlInserir.setString(++i, obj.getCampo());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoCampoTagEnum())) {
						sqlInserir.setString(++i, obj.getTipoCampoTagEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getNivel());
					if (Uteis.isAtributoPreenchido(obj.getTamanhoTag())) {
						sqlInserir.setInt(++i, obj.getTamanhoTag());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getMascara());
					sqlInserir.setString(++i, obj.getSeparadorMonetaria());
					sqlInserir.setString(++i, obj.getRemoverMascara());
					sqlInserir.setBoolean(++i, obj.isSuprimirZeroEsquerda());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final LayoutIntegracaoTagVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LayoutIntegracaoTag.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE LayoutIntegracaoTag ");
			sql.append("   SET layoutIntegracao=?, tagMae=?, tag=?, tipoTag=?, campo=?,  tipoCampoTag=?, ");
			sql.append("   nivel=?, tamanhoTag =?, mascara=?, separadorMonetaria=?, removerMascara=?, suprimirZeroEsquerda=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getLayoutIntegracaoVO().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getTagMae())) {
						sqlAlterar.setInt(++i, obj.getTagMae().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getTag());
					sqlAlterar.setString(++i, obj.getTipoTagEnum().name());
					if (Uteis.isAtributoPreenchido(obj.getCampo())) {
						sqlAlterar.setString(++i, obj.getCampo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getTipoCampoTagEnum())) {
						sqlAlterar.setString(++i, obj.getTipoCampoTagEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getNivel());
					if (Uteis.isAtributoPreenchido(obj.getTamanhoTag())) {
						sqlAlterar.setInt(++i, obj.getTamanhoTag());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getMascara());
					sqlAlterar.setString(++i, obj.getSeparadorMonetaria());
					sqlAlterar.setString(++i, obj.getRemoverMascara());
					sqlAlterar.setBoolean(++i, obj.isSuprimirZeroEsquerda());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LayoutIntegracaoTagVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LayoutIntegracaoTag.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM LayoutIntegracaoTag WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT lixt.codigo as \"lixt.codigo\", lixt.tag as \"lixt.tag\", ");
		sql.append(" lixt.tipoTag as \"lixt.tipoTag\", lixt.campo as \"lixt.campo\", ");
		sql.append(" lixt.tipoCampoTag as \"lixt.tipoCampoTag\",  lixt.nivel as \"lixt.nivel\", ");
		sql.append(" lixt.tamanhoTag as \"lixt.tamanhoTag\", lixt.mascara as \"lixt.mascara\", lixt.tagMae as \"lixt.tagMae\", ");
		sql.append(" lixt.layoutIntegracao as \"lixt.layoutIntegracao\", lixt.separadorMonetaria as \"lixt.separadorMonetaria\",  ");
		sql.append(" lixt.removerMascara as \"lixt.removerMascara\", lixt.suprimirZeroEsquerda as \"lixt.suprimirZeroEsquerda\",  ");
		sql.append(" tagMae.tag as \"tagMae.tag\", ");
		sql.append(" tagMae.tipoTag as \"tagMae.tipoTag\", tagMae.campo as \"tagMae.campo\", ");
		sql.append(" tagMae.tipoCampoTag as \"tagMae.tipoCampoTag\",  tagMae.nivel as \"tagMae.nivel\", ");
		sql.append(" tagMae.tamanhoTag as \"tagMae.tamanhoTag\", tagMae.mascara as \"tagMae.mascara\", tagMae.tagMae as \"tagMae.tagMae\", ");
		sql.append(" tagMae.layoutIntegracao as \"tagMae.layoutIntegracao\" , tagMae.separadorMonetaria as \"tagMae.separadorMonetaria\",   ");
		sql.append(" tagMae.removermascara as \"tagMae.removermascara\", tagMae.suprimirZeroEsquerda as \"tagMae.suprimirZeroEsquerda\"  ");
		sql.append(" FROM LayoutIntegracaoTag lixt ");
		sql.append(" left join  LayoutIntegracaoTag tagMae on tagMae.codigo = lixt.tagMae ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutIntegracaoTagVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lixt.codigo = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY lixt.nivel asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutIntegracaoTagVO> consultaRapidaPorTag(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lower(lixt.tag) like('%").append(valorConsulta.toLowerCase()).append("%') ");
		sqlStr.append(" ORDER BY lixt.nivel asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutIntegracaoTagVO> consultaRapidaPorLayoutIntegracaoVO(LayoutIntegracaoVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lixt.layoutIntegracao = ").append(obj.getCodigo()).append(" ");
		sqlStr.append(" ORDER BY lixt.nivel asc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<LayoutIntegracaoTagVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			LayoutIntegracaoTagVO lit = new LayoutIntegracaoTagVO();
			montarDadosBasico(lit, tabelaResultado, nivelMontarDados, usuario);
			lit.setLayoutIntegracaoVO(obj);
			vetResultado.add(lit);
		}
		return vetResultado;
		
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutIntegracaoTagVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<LayoutIntegracaoTagVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			LayoutIntegracaoTagVO obj = new LayoutIntegracaoTagVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	
	private void montarDadosBasico(LayoutIntegracaoTagVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("lixt.codigo")));
		obj.getLayoutIntegracaoVO().setCodigo((dadosSQL.getInt("lixt.layoutIntegracao")));		
		obj.setTag(dadosSQL.getString("lixt.tag"));
		obj.setTipoTagEnum(TipoTagEnum.valueOf(dadosSQL.getString("lixt.tipoTag")));
		obj.setCampo(dadosSQL.getString("lixt.campo"));
		obj.setMascara(dadosSQL.getString("lixt.mascara"));
		obj.setRemoverMascara(dadosSQL.getString("lixt.removermascara"));
		obj.setSeparadorMonetaria(dadosSQL.getString("lixt.separadorMonetaria"));
		obj.setSuprimirZeroEsquerda(dadosSQL.getBoolean("lixt.SuprimirZeroEsquerda"));
		obj.setNivel(dadosSQL.getString("lixt.nivel"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("lixt.tipoCampoTag"))) {
			obj.setTipoCampoTagEnum(TipoCampoTagEnum.valueOf(dadosSQL.getString("lixt.tipoCampoTag")));
		}
		obj.setTamanhoTag((dadosSQL.getInt("lixt.tamanhoTag")));
		if(Uteis.isAtributoPreenchido((dadosSQL.getInt("lixt.tagMae")))){
			obj.getTagMae().setNovoObj(Boolean.FALSE);
			obj.getTagMae().setCodigo((dadosSQL.getInt("lixt.tagMae")));
			obj.getTagMae().getLayoutIntegracaoVO().setCodigo((dadosSQL.getInt("tagMae.layoutIntegracao")));
			obj.getTagMae().setTag(dadosSQL.getString("tagMae.tag"));
			obj.getTagMae().setTipoTagEnum(TipoTagEnum.valueOf(dadosSQL.getString("tagMae.tipoTag")));
			obj.getTagMae().setCampo(dadosSQL.getString("tagMae.campo"));
			obj.getTagMae().setMascara(dadosSQL.getString("tagMae.mascara"));
			obj.getTagMae().setRemoverMascara(dadosSQL.getString("tagMae.removermascara"));
			obj.getTagMae().setSeparadorMonetaria(dadosSQL.getString("tagMae.separadorMonetaria"));
			obj.getTagMae().setSuprimirZeroEsquerda(dadosSQL.getBoolean("tagMae.SuprimirZeroEsquerda"));
			obj.getTagMae().setNivel(dadosSQL.getString("tagMae.nivel"));
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("tagMae.tipoCampoTag"))) {
				obj.getTagMae().setTipoCampoTagEnum(TipoCampoTagEnum.valueOf(dadosSQL.getString("tagMae.tipoCampoTag")));
			}
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}

	}

	

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return LayoutIntegracaoTag.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		LayoutIntegracaoTag.idEntidade = idEntidade;
	}

}
