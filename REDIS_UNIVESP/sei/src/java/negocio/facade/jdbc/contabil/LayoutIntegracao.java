package negocio.facade.jdbc.contabil;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.LayoutIntegracaoTagVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.enumeradores.TipoLayoutIntegracaoEnum;
import negocio.comuns.contabil.enumeradores.TipoLayoutPlanoContaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.comuns.utilitarias.formula.FormulaFactory;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.LayoutIntegracaoInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */

@Repository
@Scope("singleton")
@Lazy
public class LayoutIntegracao extends ControleAcesso implements LayoutIntegracaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 416454532535128731L;
	protected static String idEntidade = "LayoutIntegracao";
	@Autowired
	private FormulaFactory formulaFactory;
	

	public LayoutIntegracao()  {
		super();
	}

	public void validarDados(LayoutIntegracaoVO obj) {
		if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
			throw new StreamSeiException("O campo Descrição (Layout Integração Contábil) não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoLayoutIntegracao())) {
			throw new StreamSeiException("O campo Tipo Layou Integração (Layout Integração Contábil) não foi informado.");
		}
		if (Uteis.isAtributoPreenchido(obj.getTipoLayoutIntegracao()) && obj.getTipoLayoutIntegracao().isTxt() && !Uteis.isAtributoPreenchido(obj.getDelimitadorTxt())) {
			throw new StreamSeiException("O campo Delimitador Txt (Layout Integração Contábil) não foi informado.");
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(LayoutIntegracaoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaLayoutIntegracaoTag(), "LayoutIntegracaotag", "LayoutIntegracao", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getLayoutIntegracaoTagFacade().persistir(obj.getListaLayoutIntegracaoTag(), false, usuarioVO);

	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final LayoutIntegracaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LayoutIntegracao.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO LayoutIntegracao (descricao, tipoLayoutIntegracao, delimitadorTxt, tipoLayoutPlanoContaEnum, terminaInstrucaoComDelimitador, ");
			sql.append(" valorPrefixo, valorSufixo ) ");
			sql.append(" VALUES (?,?,?,?,?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setString(++i, obj.getDescricao());
					sqlInserir.setString(++i, obj.getTipoLayoutIntegracao().name());
					sqlInserir.setString(++i, obj.getDelimitadorTxt());
					sqlInserir.setString(++i, obj.getTipoLayoutPlanoContaEnum().name());
					sqlInserir.setBoolean(++i, obj.isTerminaInstrucaoComDelimitador());
					sqlInserir.setString(++i, obj.getValorPrefixo());
					sqlInserir.setString(++i, obj.getValorSufixo());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final LayoutIntegracaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LayoutIntegracao.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE LayoutIntegracao ");
			sql.append("   SET descricao=?, tipoLayoutIntegracao=?, delimitadorTxt=?, tipoLayoutPlanoContaEnum=?, terminaInstrucaoComDelimitador=?, ");
			sql.append("   valorPrefixo=?, valorSufixo=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setString(++i, obj.getDescricao());
					sqlAlterar.setString(++i, obj.getTipoLayoutIntegracao().name());
					sqlAlterar.setString(++i, obj.getDelimitadorTxt());
					sqlAlterar.setString(++i, obj.getTipoLayoutPlanoContaEnum().name());
					sqlAlterar.setBoolean(++i, obj.isTerminaInstrucaoComDelimitador());
					sqlAlterar.setString(++i, obj.getValorPrefixo());
					sqlAlterar.setString(++i, obj.getValorSufixo());
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
	 * Operação responsável por excluir no BD um objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProcessamentoArquivoRetornoParceiroVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LayoutIntegracaoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			LayoutIntegracao.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM LayoutIntegracao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT lix.codigo as \"lix.codigo\", lix.descricao as \"lix.descricao\", ");
		sql.append(" lix.tipoLayoutIntegracao as \"lix.tipoLayoutIntegracao\", lix.delimitadorTxt as \"lix.delimitadorTxt\", ");
		sql.append(" lix.tipoLayoutPlanoContaEnum as \"lix.tipoLayoutPlanoContaEnum\", lix.terminaInstrucaoComDelimitador as \"lix.terminaInstrucaoComDelimitador\", ");
		sql.append(" lix.valorPrefixo as \"lix.valorPrefixo\", lix.valorSufixo as \"lix.valorSufixo\" ");
		sql.append(" FROM layoutintegracao lix");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutIntegracaoVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lix.codigo = ").append(valorConsulta).append(" ");
		sqlStr.append(" ORDER BY lix.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutIntegracaoVO> consultaRapidaPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE lower(lix.descricao) like('%").append(valorConsulta.toLowerCase()).append("%') ");
		sqlStr.append(" ORDER BY lix.descricao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public LayoutIntegracaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM layoutintegracao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( LayoutIntegracaoVO ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutIntegracaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<LayoutIntegracaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			LayoutIntegracaoVO obj = new LayoutIntegracaoVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	
	private void montarDadosBasico(LayoutIntegracaoVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("lix.codigo")));
		obj.setDescricao(dadosSQL.getString("lix.descricao"));
		obj.setTipoLayoutIntegracao(TipoLayoutIntegracaoEnum.valueOf(dadosSQL.getString("lix.tipoLayoutIntegracao")));
		obj.setTipoLayoutPlanoContaEnum(TipoLayoutPlanoContaEnum.valueOf(dadosSQL.getString("lix.tipoLayoutPlanoContaEnum")));
		obj.setDelimitadorTxt(dadosSQL.getString("lix.delimitadorTxt"));
		obj.setValorPrefixo(dadosSQL.getString("lix.valorPrefixo"));
		obj.setValorSufixo(dadosSQL.getString("lix.valorSufixo"));
		obj.setTerminaInstrucaoComDelimitador(dadosSQL.getBoolean("lix.terminaInstrucaoComDelimitador"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
		obj.setListaLayoutIntegracaoTag(getFacadeFactory().getLayoutIntegracaoTagFacade().consultaRapidaPorLayoutIntegracaoVO(obj, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));

	}

	public static LayoutIntegracaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		LayoutIntegracaoVO obj = new LayoutIntegracaoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setTipoLayoutIntegracao(TipoLayoutIntegracaoEnum.valueOf(dadosSQL.getString("tipoLayoutIntegracao")));
		obj.setTipoLayoutPlanoContaEnum(TipoLayoutPlanoContaEnum.valueOf(dadosSQL.getString("tipoLayoutPlanoContaEnum")));
		obj.setDelimitadorTxt(dadosSQL.getString("delimitadorTxt"));
		obj.setValorPrefixo(dadosSQL.getString("valorPrefixo"));
		obj.setValorSufixo(dadosSQL.getString("valorSufixo"));
		obj.setTerminaInstrucaoComDelimitador(dadosSQL.getBoolean("terminaInstrucaoComDelimitador"));
		obj.setListaLayoutIntegracaoTag(getFacadeFactory().getLayoutIntegracaoTagFacade().consultaRapidaPorLayoutIntegracaoVO(obj, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void validarDadosLayoutIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO tag) throws Exception {
		if (!Uteis.isAtributoPreenchido(tag.getTag())) {
			throw new Exception("O campo Tag (Layout Integração Tags) deve ser informado.");
		}

		if (obj.getTipoLayoutIntegracao().isXml() && !obj.getListaLayoutIntegracaoTag().isEmpty() && !tag.getTipoTagEnum().isTagRoot() && !Uteis.isAtributoPreenchido(tag.getTagMae().getTag())) {
			throw new Exception("O campo Tag Mãe (Layout Integração Tags) deve ser informado.");
		}

		if (!Uteis.isAtributoPreenchido(tag.getTipoTagEnum())) {
			throw new Exception("O campo Tipo Tag (Layout Integração Tags) deve ser informado.");
		}

		if ((tag.getTipoTagEnum().isCampo() || tag.getTipoTagEnum().isTagFormula()) && (tag.getCampo() == null || tag.getCampo().isEmpty())) {
			throw new Exception("O campo Valor (Layout Integração Tags) deve ser informado.");
		}

		if (tag.getTipoTagEnum().isCampo() && !Uteis.isAtributoPreenchido(tag.getTipoCampoTagEnum())) {
			throw new Exception("O campo Tipo Campo (Layout Integração Tags) deve ser informado.");
		}

		if ((tag.getTipoTagEnum().isCampo() || tag.getTipoTagEnum().isTagFormula()) && !Uteis.isAtributoPreenchido(tag.getTamanhoTag())) {
			throw new Exception("O campo Tamanho (Layout Integração Tags) deve ser informado.");
		}

		if ((tag.getTipoTagEnum().isCampo() || tag.getTipoTagEnum().isTagFormula()) && tag.getTipoCampoTagEnum().isTipoDouble() && !Uteis.isAtributoPreenchido(tag.getSeparadorMonetaria())) {
			throw new Exception("O campo Separador Monetária (Layout Integração Tags) deve ser informado.");
		}

		if (obj.getTipoLayoutIntegracao().isXml() && obj.getListaLayoutIntegracaoTag().isEmpty() && !tag.getTipoTagEnum().isTagRoot()) {
			throw new Exception("A primeira tag a ser adicionado deve ser do tipo Root.");
		}

		if (!obj.getListaLayoutIntegracaoTag().isEmpty() && tag.getTipoTagEnum().isTagRoot()) {
			throw new Exception("Não pode existir mais de uma tag Root para layout de integração");
		}

		if (tag.getTipoTagEnum().isTagRoot()) {
			tag.setTagMae(new LayoutIntegracaoTagVO());
		}
		tag.setTag(tag.getTag().replaceAll(" ", ""));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void addLayoutIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO tag, UsuarioVO usuario) throws Exception {
		validarDadosLayoutIntegracaoTag(obj, tag);
		tag.setLayoutIntegracaoVO(obj);
		int index = 0;
		for (LayoutIntegracaoTagVO objExistente : obj.getListaLayoutIntegracaoTag()) {
			if (objExistente.equalsCampoSelecaoLista(tag)) {
				obj.getListaLayoutIntegracaoTag().set(index, tag);
				validarNivelLayoutIntegracaoTag(obj);
				Collections.sort(obj.getListaLayoutIntegracaoTag());
				return;
			}
			index++;
		}
		obj.getListaLayoutIntegracaoTag().add(tag);
		validarNivelLayoutIntegracaoTag(obj);
		Collections.sort(obj.getListaLayoutIntegracaoTag());
	}

	
	private void validarNivelLayoutIntegracaoTag(LayoutIntegracaoVO obj) throws Exception {
		if (obj.getListaLayoutIntegracaoTag().stream().anyMatch(tag-> tag.getTipoTagEnum().isTagRoot())) {
			for (LayoutIntegracaoTagVO objExistente : obj.getListaLayoutIntegracaoTag()) {
				if (objExistente.getTipoTagEnum().isTagRoot()) {
					objExistente.setNivel(String.valueOf(1));
					validarNivelLayoutIntegracaoTagFilhos(obj, objExistente);
					break;
				}
			}
		} else if (obj.getTipoLayoutIntegracao().isTxt()) {
			int seq = 1;
			for (LayoutIntegracaoTagVO objExistente : obj.getListaLayoutIntegracaoTag()) {
				objExistente.setNivel(String.valueOf(seq));
				seq++;
			}
		}
	}

	
	private void validarNivelLayoutIntegracaoTagFilhos(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO objExistente) throws Exception {
		int seq = 1;
		for (LayoutIntegracaoTagVO objExistenteFilho : obj.getListaLayoutIntegracaoTag()) {
			if (Uteis.isAtributoPreenchido(objExistenteFilho.getTagMae().getTag()) && objExistenteFilho.getTagMae().getTag().equals(objExistente.getTag())) {
				objExistenteFilho.setNivel(objExistente.getNivel() + "." + seq);
				objExistenteFilho.setTagMae(objExistente);
				seq++;
				validarNivelLayoutIntegracaoTagFilhos(obj, objExistenteFilho);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removeLayoutIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO tag, UsuarioVO usuario) throws Exception {
		Iterator<LayoutIntegracaoTagVO> i = obj.getListaLayoutIntegracaoTag().iterator();
		while (i.hasNext()) {
			LayoutIntegracaoTagVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(tag) || objExistente.getTagMae().getTag().equals(tag.getTag())) {
				i.remove();
			}
		}
		validarNivelLayoutIntegracaoTag(obj);
		Collections.sort(obj.getListaLayoutIntegracaoTag());
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void alterarPosicaoLayoutIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO tag, boolean posicaoAcima, UsuarioVO usuario) throws Exception {
		Integer index = 0;
		for (LayoutIntegracaoTagVO objExistenteFilho : obj.getListaLayoutIntegracaoTag()) {
			if (objExistenteFilho.equalsCampoSelecaoLista(tag)) {
				buscarTagIrmaParaAlterarPosicaoIntegracaoTag(obj, objExistenteFilho, tag, posicaoAcima, index, index, usuario);
				break;
			}
			index++;
		}
		validarNivelLayoutIntegracaoTag(obj);
		Collections.sort(obj.getListaLayoutIntegracaoTag());
	}

	
	private void buscarTagIrmaParaAlterarPosicaoIntegracaoTag(LayoutIntegracaoVO obj, LayoutIntegracaoTagVO objExistenteFilho, LayoutIntegracaoTagVO tag, boolean posicaoAcima, Integer posicaoInicial, Integer index, UsuarioVO usuario)  {
		if (posicaoAcima) {
			index = index - 1;
		} else {
			index = index + 1;
		}
		if (index >= obj.getNumeroTags() || index < 0) {
			throw new StreamSeiException("Só é permitido a troca de tags de mesmo nível. ");
		}
		LayoutIntegracaoTagVO objExistenteIrmao = obj.getListaLayoutIntegracaoTag().get(index);
		if (objExistenteIrmao.getTagMae().getTag().equals(objExistenteFilho.getTagMae().getTag())) {
			obj.getListaLayoutIntegracaoTag().set(posicaoInicial, objExistenteIrmao);
			obj.getListaLayoutIntegracaoTag().set(index, objExistenteFilho);
			return;
		}
		buscarTagIrmaParaAlterarPosicaoIntegracaoTag(obj, objExistenteFilho, tag, posicaoAcima, posicaoInicial, index, usuario);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarLayoutTxtParaIntegracaoContabil(IntegracaoContabilVO obj, LayoutIntegracaoVO layoutIntegracaoVO, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("nashorn");
		Collections.sort(layoutIntegracaoVO.getListaLayoutIntegracaoTag());		
		if(layoutIntegracaoVO.getListaLayoutIntegracaoTag().stream().anyMatch(tag-> tag.getTipoTagEnum().isTagRoot())){
			for (LayoutIntegracaoTagVO tag : layoutIntegracaoVO.getListaLayoutIntegracaoTag()) {
				if (tag.getTipoTagEnum().isTagRoot()) {
					validarLayoutIntegracaoTagFilhosTxt(obj, layoutIntegracaoVO, tag, engine, sb);
					break;
				}
			}
		}else{
			for (LancamentoContabilVO lc : obj.getListaLancamentoContabil()) {
				int index = 1;
				for (LayoutIntegracaoTagVO tag : layoutIntegracaoVO.getListaLayoutIntegracaoTag()) {
					montarTagsTxt(tag, lc, engine, sb);
					if(layoutIntegracaoVO.isGerarDelimitador(index, null)){
						sb.append(layoutIntegracaoVO.getDelimitadorTxt());
					}
					index++;
				}
				sb.append(System.lineSeparator());
			}	
		}
		obj.setTextoArquivo(sb.toString());
	}
	
	
	private <T extends SuperVO> void validarLayoutIntegracaoTagFilhosTxt(T entidade, LayoutIntegracaoVO li, LayoutIntegracaoTagVO tagMae, ScriptEngine engine, StringBuilder sb) throws Exception {
		int index = 1;
		for (LayoutIntegracaoTagVO tagFilha : li.getListaLayoutIntegracaoTag()) {
			if (Uteis.isAtributoPreenchido(tagFilha.getTagMae().getTag()) && tagFilha.getTagMae().getTag().equals(tagMae.getTag())) {
				if (tagFilha.getTipoTagEnum().isTagList()) {
					tagFilha.setListaGenerica((List<? extends SuperVO>) UtilReflexao.realizarValidacaoCampoReflection(entidade, tagFilha.getCampo()));
					validarLayoutIntegracaoTagListTxt(entidade, li, tagFilha, engine, sb);
				} else {
					montarTagsTxt(tagFilha, entidade,engine, sb);
					if(li.isGerarDelimitador(index, tagMae)){
						sb.append(li.getDelimitadorTxt());
					}
					if(li.isGerarNovaLinha(index, tagMae)){
						sb.append(System.lineSeparator());
					}
					validarLayoutIntegracaoTagFilhosTxt(entidade, li, tagFilha, engine, sb);
					index++;
				}
			}
		}
	}
	
	private <T extends SuperVO> void validarLayoutIntegracaoTagListTxt(T entidade, LayoutIntegracaoVO li, LayoutIntegracaoTagVO tagList, ScriptEngine engine, StringBuilder sb) throws Exception {		
		for (SuperVO entidadeGenerica : tagList.getListaGenerica()) {
			validarLayoutIntegracaoTagFilhosTxt(entidadeGenerica, li, tagList, engine, sb);
		}
	}
	
	public <T extends SuperVO> void montarTagsTxt(LayoutIntegracaoTagVO tag, T entidade, ScriptEngine engine, StringBuilder sb) throws Exception {		
		if (tag.getTipoTagEnum().isFixo()) {
			sb.append(montarTagsCampoFixo(tag));
		} else if (tag.getTipoTagEnum().isTagFormula()) {					
			sb.append(montarTagsCampoFormula(tag, entidade, engine));
		} else if (tag.getTipoTagEnum().isCampo()) {
			if (tag.getTipoCampoTagEnum().isTipoString()) {
				String campo = (String) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				sb.append(montarTagsCampoString(tag, campo));
			} else if (tag.getTipoCampoTagEnum().isTipoInteiro()) {
				Integer campo = (Integer) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				sb.append(montarTagsCampoInteiro(tag, campo));
			} else if (tag.getTipoCampoTagEnum().isTipoData()) {
				Date campo = (Date) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				sb.append(montarTagsCampoData(tag, campo));
			} else if (tag.getTipoCampoTagEnum().isTipoDouble()) {
				Double campo = (Double) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				sb.append(montarTagsCampoDouble(tag, campo));
			}
		}
		
	}
	
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarLayoutXmlParaIntegracaoContabil(IntegracaoContabilVO obj, LayoutIntegracaoVO layoutIntegracaoXmlVO, UsuarioVO usuario) throws Exception {
		Collections.sort(layoutIntegracaoXmlVO.getListaLayoutIntegracaoTag());
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		Element rootElement = null;
		for (LayoutIntegracaoTagVO tag : layoutIntegracaoXmlVO.getListaLayoutIntegracaoTag()) {
			if (tag.getTipoTagEnum().isTagRoot()) {
				rootElement = doc.createElement(tag.getTag());
				Attr attr = doc.createAttribute("id");
				attr.setValue(tag.getNivel().replaceAll("\\.", ""));
				rootElement.setAttributeNode(attr);
				rootElement.setIdAttributeNode(attr, true);
				doc.appendChild(rootElement);
				validarLayoutIntegracaoTagFilhos(obj, layoutIntegracaoXmlVO, tag, doc, rootElement);
				break;
			}
		}
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		StringWriter writer = new StringWriter();
		trans.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
		trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
		trans.transform(new DOMSource(doc), new StreamResult(writer));
		obj.setTextoArquivo(writer.getBuffer().toString());
		obj.setTextoArquivo(obj.getTextoArquivo().replaceAll(" id=\".*?\"", ""));
	}

	
	private <T extends SuperVO> void validarLayoutIntegracaoTagFilhos(T entidade, LayoutIntegracaoVO li, LayoutIntegracaoTagVO tagMae, Document doc, Element rootElement) throws Exception {
		for (LayoutIntegracaoTagVO tagFilha : li.getListaLayoutIntegracaoTag()) {
			if (Uteis.isAtributoPreenchido(tagFilha.getTagMae().getTag()) && tagFilha.getTagMae().getTag().equals(tagMae.getTag())) {
				if (tagFilha.getTipoTagEnum().isTagList()) {
					tagFilha.setListaGenerica((List<? extends SuperVO>) UtilReflexao.realizarValidacaoCampoReflection(entidade, tagFilha.getCampo()));
					validarLayoutIntegracaoTagList(entidade, li, tagFilha, doc, rootElement);
				} else {
					montarTagsXml(doc, rootElement, tagFilha, entidade);
					validarLayoutIntegracaoTagFilhos(entidade, li, tagFilha, doc, rootElement);
				}
			}
		}
	}

	
	private <T extends SuperVO> void validarLayoutIntegracaoTagList(T entidade, LayoutIntegracaoVO li, LayoutIntegracaoTagVO tagList, Document doc, Element rootElement) throws Exception {
		for (SuperVO entidadeGenerica : tagList.getListaGenerica()) {
			montarTagsXml(doc, rootElement, tagList, entidade);
			validarLayoutIntegracaoTagFilhos(entidadeGenerica, li, tagList, doc, rootElement);
		}
	}
	
	private <T extends SuperVO> void montarTagsXml(Document doc, Element rootElement, LayoutIntegracaoTagVO tag, T entidade) throws Exception {
		Element element = doc.createElement(tag.getTag());
		Attr attr = doc.createAttribute("id");
		attr.setValue(tag.getNivel().replaceAll("\\.", ""));
		element.setAttributeNode(attr);
		element.setIdAttributeNode(attr, true);
		if (tag.getTipoTagEnum().isFixo()) {
			element.appendChild(doc.createTextNode(montarTagsCampoFixo(tag)));
		} else if (tag.getTipoTagEnum().isCampo()) {
			if (tag.getTipoCampoTagEnum().isTipoString()) {
				String campo = (String) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				element.appendChild(doc.createTextNode(montarTagsCampoString(tag, campo)));
			} else if (tag.getTipoCampoTagEnum().isTipoInteiro()) {
				Integer campo = (Integer) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				element.appendChild(doc.createTextNode(montarTagsCampoInteiro(tag, campo)));
			} else if (tag.getTipoCampoTagEnum().isTipoData()) {
				Date campo = (Date) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				element.appendChild(doc.createTextNode(montarTagsCampoData(tag, campo)));
			} else if (tag.getTipoCampoTagEnum().isTipoDouble()) {
				Double campo = (Double) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
				element.appendChild(doc.createTextNode(montarTagsCampoDouble(tag, campo)));
			}
		}
		if (Uteis.isAtributoPreenchido(tag.getTagMae().getTag())) {
			Element elementPai = doc.getElementById(tag.getTagMae().getNivel().replaceAll("\\.", ""));
			if (elementPai != null) {
				elementPai.appendChild(element);
			} else {
				throw new Exception("Não foi encontrado a tag mãe " + tag.getTagMae().getTag());
			}
		} else {
			rootElement.appendChild(element);
		}
	}
	
	private String montarTagsCampoFixo(LayoutIntegracaoTagVO tag){
		return  tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(UteisTexto.limitarQuantidadeCaracteres(tag.getCampo(), tag.getTamanhoTag()));
	}
	
	private <T extends SuperVO>  String montarTagsCampoFormula(LayoutIntegracaoTagVO tag, T entidade, ScriptEngine engine) throws Exception{				
		Object valor = formulaFactory.getInstanceSemLog(tag.getCampo(), engine).execute(entidade);
		try {
			if (tag.getTipoCampoTagEnum().isTipoString()) {
				return montarTagsCampoString(tag, valor.toString());
			} else if (tag.getTipoCampoTagEnum().isTipoInteiro()) {			
				return montarTagsCampoInteiro(tag, Integer.parseInt(valor.toString()));
			} else if (tag.getTipoCampoTagEnum().isTipoData() && valor instanceof Date ) {			
				return montarTagsCampoData(tag, (Date)valor);
			} else if (tag.getTipoCampoTagEnum().isTipoDouble()) {			
				return montarTagsCampoDouble(tag, Double.parseDouble(valor.toString()));
			}	
		} catch (Exception e) {
			throw new StreamSeiException("Não foi possivel realizar a conversão do campo fórmula da tag "+tag.getTag());
		}
		return "";
	}
	
	private <T extends SuperVO> String montarTagsCampoString(LayoutIntegracaoTagVO tag, String campo) throws Exception{		
		if (Uteis.isAtributoPreenchido(campo)) {
			if(Uteis.isAtributoPreenchido(tag.getRemoverMascara())){
				campo = campo.replaceAll("["+tag.getRemoverMascara()+"]", "");	
			}
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(UteisTexto.limitarQuantidadeCaracteres(campo, tag.getTamanhoTag()));
		}else{
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo("");
		}
	}
	
	private <T extends SuperVO>  String montarTagsCampoInteiro(LayoutIntegracaoTagVO tag, Integer campo) throws Exception{
		if (Uteis.isAtributoPreenchido(campo)) {					
			if(!tag.isSuprimirZeroEsquerda() && campo.toString().length() < tag.getTamanhoTag()){
				return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(StringUtils.leftPad(campo.toString(), tag.getTamanhoTag(), "0"));	
			}else{
				return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(UteisTexto.limitarQuantidadeCaracteres(campo.toString(), tag.getTamanhoTag()));
			}					
		} else {
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo("0");
		}
	}
	
	private <T extends SuperVO> String montarTagsCampoDouble(LayoutIntegracaoTagVO tag, Double campo) throws Exception{		
		if (Uteis.isAtributoPreenchido(campo)) {
			String valor = Uteis.arrendondarForcando2CadasDecimaisStrComSepador(campo, tag.getSeparadorMonetaria());
			if(!tag.isSuprimirZeroEsquerda() && valor.length() < tag.getTamanhoTag()){
				return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(StringUtils.leftPad(valor, tag.getTamanhoTag(), "0"));	
			}else{
				return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(UteisTexto.limitarQuantidadeCaracteres(valor, tag.getTamanhoTag()));
			}
		}else{
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo("0.0");
		}
	}
	
	private <T extends SuperVO> String montarTagsCampoData(LayoutIntegracaoTagVO tag, Date campo) throws Exception{
		
		if (Uteis.isAtributoPreenchido(campo)) {
			if(Uteis.isAtributoPreenchido(tag.getMascara())){
				return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(Uteis.getData(campo, tag.getMascara()));	
			}else{
				return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(Uteis.getData(campo, "dd/MM/yyyy"));
			}
		}else {
			return "";
		}
	}
	
	private <T extends SuperVO> String montarTagsCampoBoolean(LayoutIntegracaoTagVO tag, T entidade) throws Exception{
		Boolean campo = (Boolean) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
		if (Uteis.isAtributoPreenchido(campo)) {
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(UteisTexto.limitarQuantidadeCaracteres(campo.toString(), tag.getTamanhoTag()));
		}else {
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo("");
		}
	}
	
	private <T extends SuperVO> String montarTagsCampoEnum(LayoutIntegracaoTagVO tag, T entidade) throws Exception{
		Enum campo = (Enum) UtilReflexao.realizarValidacaoCampoReflection(entidade, tag.getCampo());
		if (Uteis.isAtributoPreenchido(campo)) {
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo(UteisTexto.limitarQuantidadeCaracteres(campo.name(), tag.getTamanhoTag()));
		}else {
			return tag.getLayoutIntegracaoVO().getValorCampoPrefixoAndSufixo("");
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return LayoutIntegracao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		LayoutIntegracao.idEntidade = idEntidade;
	}

}
