package negocio.facade.jdbc.basico;

import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.LayoutEtiquetaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.basico.enumeradores.TipoConsultaComboLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.LayoutEtiquetaInterfaceFacade;

@Repository
@Lazy
@Scope(value = "singleton")
public class LayoutEtiqueta extends ControleAcesso implements LayoutEtiquetaInterfaceFacade {

	private static final long serialVersionUID = -4852884134560707211L;
	protected static String idEntidade;

	public LayoutEtiqueta() throws Exception {
		super();
		setIdEntidade("LayoutEtiqueta");
	}

	public static String getIdEntidade() {
		return LayoutEtiqueta.idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		LayoutEtiqueta.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@Override
	public void excluir(LayoutEtiquetaVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			excluir(getIdEntidade(), usuarioLogado);
			String sql = "DELETE FROM LayoutEtiqueta WHERE ((codigo = ?)) ";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			getFacadeFactory().getLayoutEtiquetaTagFacade().excluirLayoutEtiquetaTagItens(obj.getCodigo(), usuarioLogado);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@Override
	public void incluir(final LayoutEtiquetaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			validarDados(obj);
			incluir(getIdEntidade(), usuarioLogado);
			realizarUpperCaseDados(obj);
			final String sql = "INSERT INTO LayoutEtiqueta ( descricao , largurafolhaimpressao , alturafolhaimpressao , larguraetiqueta ,  alturaetiqueta , margemSuperiorEtiquetaFolha, margemEsquerdaEtiquetaFolha, margemEntreEtiquetaHorizontal, margemEntreEtiquetaVertical, numeroColunasEtiqueta, numeroLinhasEtiqueta, moduloLayoutEtiqueta) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int x = 1;
					sqlInserir.setString(x++, obj.getDescricao());
					if (obj.getLarguraFolhaImpressao().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getLarguraFolhaImpressao().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getAlturaFolhaImpressao().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getAlturaFolhaImpressao().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getLarguraEtiqueta().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getLarguraEtiqueta().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getAlturaEtiqueta().intValue() != 0) {
						sqlInserir.setInt(x++, obj.getAlturaEtiqueta().intValue());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setInt(x++, obj.getMargemSuperiorEtiquetaFolha());
					sqlInserir.setInt(x++, obj.getMargemEsquerdaEtiquetaFolha());
					sqlInserir.setInt(x++, obj.getMargemEntreEtiquetaHorizontal());
					sqlInserir.setInt(x++, obj.getMargemEntreEtiquetaVertical());
					sqlInserir.setInt(x++, obj.getNumeroColunasEtiqueta());
					sqlInserir.setInt(x++, obj.getNumeroLinhasEtiqueta());
					sqlInserir.setString(x++, obj.getModuloLayoutEtiqueta().name());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getLayoutEtiquetaTagFacade().incluirLayoutEtiquetaItens(obj.getCodigo(), obj.getLayoutEtiquetaTagVOs(), configuracaoGeralSistemaVO, usuarioLogado);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().contains("check_nome_layoutetiqueta_duplicado")) {
				throw new Exception("Já existe um Layout Etiqueta com a descrição informada.");
			}
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterar(final LayoutEtiquetaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			validarDados(obj);
			incluir(getIdEntidade(), usuarioLogado);
			realizarUpperCaseDados(obj);
			final String sql = "UPDATE LayoutEtiqueta set descricao=?, largurafolhaimpressao=?, alturafolhaimpressao=?, larguraetiqueta=?, alturaetiqueta=?, " + "  margemSuperiorEtiquetaFolha=?, margemEsquerdaEtiquetaFolha=? , margemEntreEtiquetaHorizontal=? , margemEntreEtiquetaVertical=?, numeroColunasEtiqueta=? , numeroLinhasEtiqueta=?, moduloLayoutEtiqueta = ? WHERE ((codigo=?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int x = 1;
					sqlAlterar.setString(x++, obj.getDescricao());
					if (obj.getLarguraFolhaImpressao().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getLarguraFolhaImpressao().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getAlturaFolhaImpressao().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getAlturaFolhaImpressao().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getLarguraEtiqueta().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getLarguraEtiqueta().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getAlturaEtiqueta().intValue() != 0) {
						sqlAlterar.setInt(x++, obj.getAlturaEtiqueta().intValue());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setInt(x++, obj.getMargemSuperiorEtiquetaFolha());
					sqlAlterar.setInt(x++, obj.getMargemEsquerdaEtiquetaFolha());
					sqlAlterar.setInt(x++, obj.getMargemEntreEtiquetaHorizontal());
					sqlAlterar.setInt(x++, obj.getMargemEntreEtiquetaVertical());
					sqlAlterar.setInt(x++, obj.getNumeroColunasEtiqueta());
					sqlAlterar.setInt(x++, obj.getNumeroLinhasEtiqueta());
					sqlAlterar.setString(x++, obj.getModuloLayoutEtiqueta().name());
					sqlAlterar.setInt(x++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getLayoutEtiquetaTagFacade().alterarLayoutEtiquetaItens(obj.getCodigo(), obj.getLayoutEtiquetaTagVOs(), configuracaoGeralSistemaVO, usuarioLogado);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().contains("check_nome_layoutetiqueta_duplicado")) {
				throw new Exception("Já existe um Layout Etiqueta com a descrição informada.");
			}
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param MetaProducaoVO
	 * @throws Exception
	 */

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void persistir(LayoutEtiquetaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, configuracaoGeralSistemaVO, usuarioLogado);
		} else {
			alterar(obj, configuracaoGeralSistemaVO, usuarioLogado);
		}
	}

	/**
	 * Operação responsavel por localizar um objeto da classe
	 * <code>LayoutEtiquetaVO</code> atraves de sua chave primaria.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou localizaï¿½ï¿½o do
	 *                objeto procurado.
	 */
	@Override
	public List<LayoutEtiquetaVO> consultarRapidaPorModulo(ModuloLayoutEtiquetaEnum moduloLayoutEtiquetaEnum, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM LayoutEtiqueta WHERE moduloLayoutEtiqueta = '" + moduloLayoutEtiquetaEnum.name() + "'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoGeralSistemaVO, usuarioLogado);
	}

	/**
	 * Operação responsavel por localizar um objeto da classe
	 * <code>LayoutEtiquetaVO</code> atraves de sua chave primaria.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou localizaï¿½ï¿½o do
	 *                objeto procurado.
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public LayoutEtiquetaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM LayoutEtiqueta WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, configuracaoGeralSistemaVO, usuarioLogado));
		}
		throw new Exception("Dados não encontrados (Layout Etiqueta)");
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>LayoutEtiquetaVO</code>.
	 * 
	 * @return O objeto da classe <code>LayoutEtiquetaVO</code> com os dados
	 *         devidamente montados.
	 */
	public LayoutEtiquetaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		LayoutEtiquetaVO obj = new LayoutEtiquetaVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setLarguraFolhaImpressao(dadosSQL.getInt("largurafolhaimpressao"));
		obj.setAlturaFolhaImpressao(dadosSQL.getInt("alturafolhaimpressao"));
		obj.setLarguraEtiqueta(dadosSQL.getInt("larguraetiqueta"));
		obj.setAlturaEtiqueta(dadosSQL.getInt("alturaetiqueta"));
		obj.setMargemSuperiorEtiquetaFolha(dadosSQL.getInt("margemSuperiorEtiquetaFolha"));
		obj.setMargemEsquerdaEtiquetaFolha(dadosSQL.getInt("margemEsquerdaEtiquetaFolha"));
		obj.setMargemEntreEtiquetaHorizontal(dadosSQL.getInt("margemEntreEtiquetaHorizontal"));
		obj.setMargemEntreEtiquetaVertical(dadosSQL.getInt("margemEntreEtiquetaVertical"));
		obj.setNumeroColunasEtiqueta(dadosSQL.getInt("numeroColunasEtiqueta"));
		obj.setNumeroLinhasEtiqueta(dadosSQL.getInt("numeroLinhasEtiqueta"));
		obj.setModuloLayoutEtiqueta(ModuloLayoutEtiquetaEnum.valueOf(dadosSQL.getString("moduloLayoutEtiqueta")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setLayoutEtiquetaTagVO(getFacadeFactory().getLayoutEtiquetaTagFacade().consultarLayoutEtiquetaTagItens(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoGeralSistemaVO, usuarioLogado));
		return obj;
	}

	/**
	 * Responsável por realizar uma consulta de <code>LayoutEtiqueta</code>
	 * através do valor do atributo <code>String valorConsulta</code>. Retorna
	 * os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operaçôes de busca por qual tipo de parametro, que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>LayoutEtiquetaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */

	@Override
	public List<LayoutEtiquetaVO> consultar(boolean controlarAcesso, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		return consultarPorDescricao("", controlarAcesso, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoGeralSistemaVO, usuarioLogado);
	}

	/**
	 * Responsável por realizar uma consulta de <code>LayoutEtiqueta</code>
	 * através do valor do atributo <code>String descricao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>LayoutEtiquetaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutEtiquetaVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM LayoutEtiqueta WHERE sem_acentos(upper( descricao )) like(sem_acentos(upper('%" + valorConsulta + "%'))) ";
		sqlStr += " ORDER BY descricao ";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr), nivelMontarDados, configuracaoGeralSistemaVO, usuarioLogado));
	}

	/**
	 * Responsável por realizar uma consulta de <code>LayoutEtiqueta</code>
	 * através do valor do atributo <code>Integer Codigo</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>LayoutEtiquetaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutEtiquetaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuarioLogado);
		String sqlStr = "SELECT * FROM LayoutEtiqueta WHERE codigo >= ?  ORDER BY codigo ";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, configuracaoGeralSistemaVO, usuarioLogado));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>LayoutEtiquetaVO</code> resultantes da consulta.
	 */
	public List<LayoutEtiquetaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		List<LayoutEtiquetaVO> vetResultado = new ArrayList<LayoutEtiquetaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoGeralSistemaVO, usuarioLogado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Operação responsavel por localizar um objeto da classe
	 * <code>LayoutEtiquetaVO</code> atraves de um String valor trazendo a
	 * quantidade de itens consultados, para a paginação.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexï¿½o ou localizaï¿½ï¿½o do
	 *                objeto procurado.
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalRegistrosPorCodigo(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		SqlRowSet rs = null;
		if (campoConsulta.equals(TipoConsultaComboLayoutEtiquetaEnum.DESCRICAO.toString())) {
			valorConsulta += "%";
			String sqlStr = "SELECT COUNT(1) as codigo FROM LayoutEtiqueta WHERE upper( descricao ) like(?) ";
			rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		}
		if (campoConsulta.equals(TipoConsultaComboLayoutEtiquetaEnum.CODIGO.toString())) {
			Integer valorConsultaInteiro = 0;
			if (!valorConsulta.equals("")) {
				valorConsultaInteiro = Integer.parseInt(valorConsulta);
			}
			String sqlStr = "SELECT COUNT(1) as codigo FROM LayoutEtiqueta WHERE  codigo >= ? ";
			rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsultaInteiro);
		}
		if (rs.next()) {
			return rs.getInt("codigo");
		}
		return 0;
	}

	@Override
	/**
	 * Operação responsável por adicionar um novo objeto da classe <code>LayoutEtiquetaVO</code>
	 * ao List <code>LayoutEtiquetaVOs</code>. Utiliza o atributo padrão de consulta 
	 * da classe <code>LayoutEtiquetaVO</code> - getTagEtiqueta - como identificador (key) do objeto no List.
	 * @param obj    Objeto da classe <code>LayoutEtiquetaVO</code> que serão adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjLayoutEtiquetaTagVOs(LayoutEtiquetaVO objLayoutEtiquetaVO, TagEtiquetaEnum tagEtiquetaEnum) throws Exception {
		// for (LayoutEtiquetaTagVO obj :
		// objLayoutEtiquetaVO.getLayoutEtiquetaTagVOs()) {
		// if (obj.getTagEtiqueta().equals(tagEtiquetaEnum)) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_LayoutEtiquetaTag_tagJaAdicionada"));
		// }
		// }
		LayoutEtiquetaTagVO obj = new LayoutEtiquetaTagVO();
		obj.setLayoutEtiquetaVO(objLayoutEtiquetaVO);
		obj.setTagEtiqueta(tagEtiquetaEnum);
		obj.setOrdem(objLayoutEtiquetaVO.getLayoutEtiquetaTagVOs().size() + 1);
		if (tagEtiquetaEnum.equals(TagEtiquetaEnum.IMAGEM_FUNDO)) {
			obj.setNomeArquivo(objLayoutEtiquetaVO.getNomeArquivo());
			obj.setNomeArquivoApresentar(objLayoutEtiquetaVO.getNomeArquivoApresentar());
			obj.setNomeArquivoAnt(objLayoutEtiquetaVO.getNomeArquivoAnt());
			obj.setUrlImagem(objLayoutEtiquetaVO.getUrlImagem());
			obj.setUrlImagemApresentar(objLayoutEtiquetaVO.getUrlImagemApresentar());
			obj.setPastaBaseArquivo(objLayoutEtiquetaVO.getPastaBaseArquivo());
			objLayoutEtiquetaVO.setNomeArquivo(null);
			objLayoutEtiquetaVO.setNomeArquivoApresentar(null);
			objLayoutEtiquetaVO.setNomeArquivoAnt(null);
			objLayoutEtiquetaVO.setUrlImagem(null);
			objLayoutEtiquetaVO.setUrlImagemApresentar(null);
			objLayoutEtiquetaVO.setPastaBaseArquivo(null);
		}
		objLayoutEtiquetaVO.getLayoutEtiquetaTagVOs().add(obj);
	}

	@Override
	public void realizarAlteracaoOrdemLayoutEtiquetaTag(LayoutEtiquetaVO layoutEtiquetaVO, LayoutEtiquetaTagVO layoutEtiquetaTagVO, boolean subir) {
		Integer ordemTmp = layoutEtiquetaTagVO.getOrdem();
		LayoutEtiquetaTagVO layoutEtiquetaTagVO2 = null;
		if (subir) {
			if (layoutEtiquetaTagVO.getOrdem() == 1) {
				return;
			}
			layoutEtiquetaTagVO2 = layoutEtiquetaVO.getLayoutEtiquetaTagVOs().get(layoutEtiquetaTagVO.getOrdem() - 2);
		} else {
			if (layoutEtiquetaTagVO.getOrdem() == layoutEtiquetaVO.getLayoutEtiquetaTagVOs().size()) {
				return;
			}
			layoutEtiquetaTagVO2 = layoutEtiquetaVO.getLayoutEtiquetaTagVOs().get(layoutEtiquetaTagVO.getOrdem());
		}
		layoutEtiquetaTagVO.setOrdem(layoutEtiquetaTagVO2.getOrdem());
		layoutEtiquetaTagVO2.setOrdem(ordemTmp);
		Ordenacao.ordenarLista(layoutEtiquetaVO.getLayoutEtiquetaTagVOs(), "ordem");
	}

	@Override
	/**
	 * Operação responsável por excluir um objeto da classe <code>LayoutEtiquetaVO</code>
	 * no List <code>LayoutEtiquetaVOs</code>. Utiliza o atributo padrão de consulta 
	 * da classe <code>LayoutEtiquetaVO</code> - getTagEtiqueta - como identificador (key) do objeto no List.
	 * @param obj    Objeto da classe <code>LayoutEtiquetaVO</code> que serão adiocionado ao Hashtable correspondente.
	 */
	public void excluirObjLayoutEtiquetaTagVOs(LayoutEtiquetaVO objLayoutEtiquetaVO, LayoutEtiquetaTagVO layoutEtiquetaTagVO) throws Exception {
		objLayoutEtiquetaVO.getLayoutEtiquetaTagVOs().remove(layoutEtiquetaTagVO);
		int x = 1;
		for (LayoutEtiquetaTagVO layoutEtiquetaTagVO2 : objLayoutEtiquetaVO.getLayoutEtiquetaTagVOs()) {
			layoutEtiquetaTagVO2.setOrdem(x++);
		}
		// int index = 0;
		//
		// Iterator<LayoutEtiquetaTagVO> i = objLayoutEtiquetaVO
		// .getLayoutEtiquetaTagVOs().iterator();
		//
		// while (i.hasNext()) {
		// LayoutEtiquetaTagVO objExistente = (LayoutEtiquetaTagVO) i.next();
		// if (objExistente.getTagEtiqueta().equals(tagEtiquetaEnum)) {
		// objLayoutEtiquetaVO.getLayoutEtiquetaTagVOs().remove(index);
		// return;
		// }
		// index++;
		// }

	}

	@Override
	/**
	 * Operação responsável por validar os dados de um objeto da classe <code>LayoutEtiquetaVO</code>.
	 * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
	 * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
	 * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
	 * o atributo e o erro ocorrido.
	 */
	public void validarDados(LayoutEtiquetaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		ConsistirException consistir = new ConsistirException();
		if (obj.getDescricao().equals("")) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiqueta_descricao"));
		}
		if (obj.getLarguraFolhaImpressao() == null || obj.getLarguraFolhaImpressao().equals(0)) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiqueta_larguraFolhaImpressao"));
		}
		if (obj.getAlturaFolhaImpressao() == null || obj.getLarguraFolhaImpressao().equals(0)) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiqueta_alturaFolhaImpressao"));
		}
		if (obj.getLarguraEtiqueta() == null || obj.getLarguraEtiqueta().equals(0)) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiqueta_larguraEtiqueta"));
		}
		if (obj.getAlturaEtiqueta() == null || obj.getAlturaEtiqueta().equals(0)) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiqueta_alturaEtiqueta"));
		}
		if(obj.getMargemSuperiorEtiquetaFolha() == null){
			obj.setMargemSuperiorEtiquetaFolha(0);
		}
		if(obj.getMargemEsquerdaEtiquetaFolha() == null){
			obj.setMargemEsquerdaEtiquetaFolha(0);
		}
		if(obj.getMargemEntreEtiquetaHorizontal() == null){
			obj.setMargemEntreEtiquetaHorizontal(0);
		}
		if(obj.getMargemEntreEtiquetaVertical() == null){
			obj.setMargemEntreEtiquetaVertical(0);
		}
		if(obj.getNumeroColunasEtiqueta() == null || obj.getNumeroColunasEtiqueta().equals(0)){
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiqueta_numeroColuna"));
		}
		if(obj.getNumeroLinhasEtiqueta() == null || obj.getNumeroLinhasEtiqueta().equals(0)){
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiqueta_numeroLinha"));
		}
		
		
		if (consistir.existeErroListaMensagemErro()) {
			throw consistir;
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	@Override
	public void realizarUpperCaseDados(LayoutEtiquetaVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		obj.setDescricao(obj.getDescricao().toUpperCase());
	}

	public static final float PONTO = 2.83f;

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta.
	 * Tem como passagem por parâmetros a <code>LayoutEtiquetaVO</code> e
	 * <code>OrigemImagemVO</code>
	 */
	@Override
	public void realizarMontagemPreviewEtiqueta(LayoutEtiquetaVO layoutEtiqueta, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		Rectangle tamanhoPagina = null;
		Document pdf = null;
		FileOutputStream arquivo = null;
		try {
			if (layoutEtiqueta.getAlturaEtiqueta().equals(0) || layoutEtiqueta.getAlturaFolhaImpressao().equals(0) || layoutEtiqueta.getLarguraEtiqueta().equals(0) || layoutEtiqueta.getLarguraFolhaImpressao().equals(0)) {
				throw new Exception("Insira um valor para altura ou largura");
			}
			float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
			float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
			float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemDireita = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
			float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			float margemInferior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
			StringBuffer caminhoDaImagem = new StringBuffer();
			File diretorio = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.LAYOUT_ETIQUETA.getValue());
			if (!diretorio.exists()) {
				diretorio.mkdirs();
			}
			diretorio = null;
			caminhoDaImagem.append(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + PastaBaseArquivoEnum.LAYOUT_ETIQUETA.getValue() + File.separator + layoutEtiqueta.getModuloLayoutEtiqueta().name() + ".pdf");
			tamanhoPagina = new Rectangle(larguraPagina, alturaPagina);
			pdf = new Document(tamanhoPagina, margemEsquerda, margemDireita, margemSuperior, margemInferior);
			arquivo = new FileOutputStream(caminhoDaImagem.toString());
			PdfWriter writer = PdfWriter.getInstance(pdf, arquivo);
			pdf.open();
			 
			realizarMontagemPreviewEtiquetLaserTinta(layoutEtiqueta, writer);
		 
		} catch (Exception e) {
			throw e;
		} finally {
			if (pdf != null) {
				pdf.close();
			}
			if (arquivo != null) {
				arquivo.close();
			}
		}
	}

	/**
	 * Metodo responsável por realizar a montagem do preview layout da etiqueta
	 * para o tipo de impressora Laser/Tinta. Tem como passagem por parâmetros a
	 * <code>LayoutEtiquetaVO</code>>
	 */

	public void realizarMontagemPreviewEtiquetLaserTinta(LayoutEtiquetaVO layoutEtiqueta, PdfWriter writer) throws Exception {
		float alturaPagina = layoutEtiqueta.getAlturaFolhaImpressao() * PONTO;
		float larguraPagina = layoutEtiqueta.getLarguraFolhaImpressao() * PONTO;
		float margemEsquerda = layoutEtiqueta.getMargemEsquerdaEtiquetaFolha().floatValue() * PONTO;
		float margemSuperior = layoutEtiqueta.getMargemSuperiorEtiquetaFolha().floatValue() * PONTO;
		float alturaEtiqueta = layoutEtiqueta.getAlturaEtiqueta().floatValue() * PONTO;
		float larguraEtiqueta = layoutEtiqueta.getLarguraEtiqueta().floatValue() * PONTO;
		float margemHorizontalEntreEtiquetas = (layoutEtiqueta.getMargemEntreEtiquetaHorizontal() == 0 ? 0.2f : layoutEtiqueta.getMargemEntreEtiquetaHorizontal().floatValue()) * PONTO;
		float margemVerticalEntreEtiquetas = layoutEtiqueta.getMargemEntreEtiquetaVertical() * PONTO;
		float margemSuperiorColuna = 0f;
		float margemEsquerdaColuna = 0f;
		PdfContentByte canvas = writer.getDirectContent();
		PdfGraphics2D pdfEtiqueta = (PdfGraphics2D) writer.getDirectContent().createGraphics(larguraPagina, alturaPagina);
		Integer linhas = layoutEtiqueta.getNumeroLinhasEtiqueta();
		Integer colunas = layoutEtiqueta.getNumeroColunasEtiqueta();
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				margemSuperiorColuna = alturaPagina - margemSuperior - (i * margemHorizontalEntreEtiquetas) - (i * alturaEtiqueta);
				margemEsquerdaColuna = margemEsquerda + (j * margemVerticalEntreEtiquetas) + (j * larguraEtiqueta);
				pdfEtiqueta.draw(new RoundRectangle2D.Double(margemEsquerda + (j * margemVerticalEntreEtiquetas) + (j * larguraEtiqueta), margemSuperior + (i * margemHorizontalEntreEtiquetas) + (i * alturaEtiqueta), larguraEtiqueta, alturaEtiqueta, 0, 0));		
				for (LayoutEtiquetaTagVO tag : layoutEtiqueta.getLayoutEtiquetaTagVOs()) {
					realizarMontagemPreviewLayoutEtiquetaTag(tag, canvas, alturaEtiqueta, larguraEtiqueta, margemSuperiorColuna, margemEsquerdaColuna);
				 
				}
				
			}
		}
		pdfEtiqueta.dispose();    
	}

	public void realizarMontagemPreviewLayoutEtiquetaTag(LayoutEtiquetaTagVO tag, PdfContentByte canvas, float alturaEtiqueta, float larguraEtiqueta, float margemSuperiorColuna, float margemEsquerdaColuna) throws Exception {
		float margemEsquerdaLabel = tag.getMargemDireita() * PONTO;
		float margemSuperiorLabel = tag.getMargemTopo() * PONTO;
		PdfTemplate tmp = null;
		if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.BIB_CODIGO_BARRAS)) {
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			tmp.beginText();
			Barcode128 barcode128 = new Barcode128();
			barcode128.setCodeType(Barcode128.CODE128);
			barcode128.setCode(tag.getValorTextoPreview());
			barcode128.setBarHeight(tag.getAlturaCodigoBarra());
			barcode128.setFont(BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true));
			barcode128.setSize(tag.getTamanhoFonte().floatValue());
			if (!tag.getImprimirNumeroAbaixo()) {
				barcode128.setAltText("");
				barcode128.setStartStopText(tag.getImprimirNumeroAbaixo());
			}
			tmp.addTemplate(barcode128.createTemplateWithBarcode(canvas, null, null), margemEsquerdaLabel, 0f);
			tmp.endText();
			canvas.addTemplate(tmp, margemEsquerdaColuna, (margemSuperiorColuna - (margemSuperiorLabel)));
		} else if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.IMAGEM_FUNDO)) {
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			Image image = Image.getInstance(tag.getUrlImagem());
			image.setAbsolutePosition(0, 0);
			image.setAlignment(Element.ALIGN_CENTER);
			image.scaleToFit(larguraEtiqueta, alturaEtiqueta);
			tmp.addImage(image, true);
			canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
		} else if (tag.getTagEtiqueta().equals(TagEtiquetaEnum.FOTO)) {
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			Rectangle rectangle2 = new Rectangle(margemEsquerdaLabel , alturaEtiqueta - margemSuperiorLabel, tag.getLarguraFoto() * PONTO +  margemEsquerdaLabel, alturaEtiqueta - (tag.getAlturaFoto()*PONTO)  - margemSuperiorLabel);
			rectangle2.setBorder(Rectangle.BOX);
			rectangle2.setBorderWidth(2);
			tmp.rectangle(rectangle2);
			canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
		} else {
			tmp = canvas.createTemplate(larguraEtiqueta, alturaEtiqueta);
			tmp.beginText();
			tmp.setColorFill(Color.decode(tag.getCorTexto()));
			BaseFont createFont = null;
			if(tag.getFontNegrito()){
				createFont = BaseFont.createFont(FontFactory.TIMES_BOLD, "", true);
			} else {
				createFont = BaseFont.createFont(FontFactory.TIMES_ROMAN, "", true);
			}
			float tamanhoFonte = tag.getTamanhoFonte().floatValue();
			float larguraEtiquetaUtilizar = larguraEtiqueta - margemEsquerdaLabel;
			float larguraTexto = realizarCalculoLarguraTextoConformeTamanhoFonte(tamanhoFonte, createFont, (tag.getLabelTag() + " " + tag.getValorTextoPreview()).trim());
			while (larguraTexto > larguraEtiquetaUtilizar) {
				larguraTexto = realizarCalculoLarguraTextoConformeTamanhoFonte(--tamanhoFonte, createFont, (tag.getLabelTag() + " " + tag.getValorTextoPreview()).trim());
			}
			tmp.setFontAndSize(createFont, tamanhoFonte);
			tmp.showTextAligned(Element.ALIGN_LEFT, (tag.getLabelTag() + " " + tag.getValorTextoPreview()).trim(), margemEsquerdaLabel, alturaEtiqueta - margemSuperiorLabel, tag.getRotacao() ? 90f : 0f);
			tmp.endText();
			canvas.addTemplate(tmp, margemEsquerdaColuna, margemSuperiorColuna - alturaEtiqueta);
		}
 
	}

	@Override
	public void upLoadArquivo(FileUploadEvent uploadEvent, LayoutEtiquetaVO layoutEtiquetaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		if (!layoutEtiquetaVO.getNomeArquivo().trim().isEmpty() && layoutEtiquetaVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL)) {
			layoutEtiquetaVO.setNomeArquivoAnt(layoutEtiquetaVO.getNomeArquivo());
		} else if (!layoutEtiquetaVO.getNomeArquivo().trim().isEmpty() && layoutEtiquetaVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL_TMP)) {
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + layoutEtiquetaVO.getPastaBaseArquivo().getValue() + File.separator + layoutEtiquetaVO.getNomeArquivo());
			ArquivoHelper.delete(arquivo);
			arquivo = null;
		}
		
		uploadEvent.getUploadedFile().getData();
		layoutEtiquetaVO.setNomeArquivo(ArquivoHelper.criarNomeArquivo(usuarioVO, ArquivoHelper.getExtensaoArquivo(uploadEvent.getUploadedFile().getName())));
		layoutEtiquetaVO.setNomeArquivoApresentar(uploadEvent.getUploadedFile().getName());
		layoutEtiquetaVO.setPastaBaseArquivo(PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL_TMP);
		ArquivoHelper.salvarArquivoNaPastaTemp(uploadEvent, layoutEtiquetaVO.getNomeArquivo(), layoutEtiquetaVO.getPastaBaseArquivo().getValue(), configuracaoGeralSistemaVO, usuarioVO);
		layoutEtiquetaVO.setUrlImagem(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + layoutEtiquetaVO.getPastaBaseArquivo().getValue() + File.separator + layoutEtiquetaVO.getNomeArquivo());
		layoutEtiquetaVO.setUrlImagemApresentar(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + layoutEtiquetaVO.getPastaBaseArquivo().getValue() + "/" + layoutEtiquetaVO.getNomeArquivo());
	}
	
	@Override
	public String realizarExportacaoLayout(LayoutEtiquetaVO layoutEtiquetaVO) throws Exception{
		JAXBContext context = null;
		String nomeArquivo = "LAYOUT_" + layoutEtiquetaVO.getCodigo() + ".xml";
		File layout = null;
		Marshaller marshaller = null;
		try {
			context = JAXBContext.newInstance(LayoutEtiquetaVO.class);
			layout = new File(UteisJSF.getCaminhoWeb() + File.separator + "relatorio" + File.separator + nomeArquivo);
			marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(layoutEtiquetaVO, layout);
			return nomeArquivo;
		} catch (Exception e) {
			throw e;
		} finally {
			context = null;
			layout = null;
			marshaller = null;
		}
	}
	
	@Override
	public LayoutEtiquetaVO realizarImportacaoLayout(FileUploadEvent uploadEvent) throws Exception{
		LayoutEtiquetaVO layoutEtiquetaVO = null;
		JAXBContext context = null;
		Unmarshaller unMarshaller = null;
		try{
			context = JAXBContext.newInstance(LayoutEtiquetaVO.class);
			unMarshaller = context.createUnmarshaller();
			layoutEtiquetaVO = (LayoutEtiquetaVO) unMarshaller.unmarshal(uploadEvent.getUploadedFile().getInputStream());			
		}catch(Exception e){
			throw e;
		}finally{
			uploadEvent = null;
			context = null;
			unMarshaller = null;
		}
		return layoutEtiquetaVO;
	}

	public float realizarCalculoLarguraTextoConformeTamanhoFonte(float tamanhoFonte, BaseFont baseFont, String texto) {
		float larguraLinha = 0.f;
		if (Uteis.isAtributoPreenchido(texto)) {
			for (int caracter : texto.toCharArray()) {
				larguraLinha += baseFont.getWidthPoint(caracter, tamanhoFonte);
			}
		}
		return larguraLinha;
	}
}
