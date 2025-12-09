package negocio.facade.jdbc.basico;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LayoutEtiquetaTagVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.LayoutEtiquetaTagInterfaceFacade;

@Repository
@Lazy
public class LayoutEtiquetaTag extends ControleAcesso implements LayoutEtiquetaTagInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783518198044447959L;
	protected static String idEntidade;

	public LayoutEtiquetaTag() throws Exception {
		super();
		setIdEntidade("LayoutEtiqueta");

	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return LayoutEtiquetaTag.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		LayoutEtiquetaTag.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>LayoutEtiquetaTagVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>LayoutEtiquetaTagVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void excluir(LayoutEtiquetaTagVO obj, UsuarioVO usuarioLogado) throws Exception {

		try {
			excluir(getIdEntidade(), usuarioLogado);
			String sql = "DELETE FROM LayoutEtiquetaTag WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {

		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>LayoutEtiquetaTagVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MetaProducaoItemVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluir(final LayoutEtiquetaTagVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		validarDados(obj);
		realizarUpperCaseDados(obj);
		if (obj.getTagEtiqueta().equals(TagEtiquetaEnum.IMAGEM_FUNDO)) {
			realizarCopiaImagem(obj, configuracaoGeralSistemaVO, usuarioLogado);
		}
		final String sql = "INSERT INTO layoutetiquetatag ( layoutetiqueta , tagetiqueta , labeltag , margemdireita , margemtopo , rotacao , tamanhofonte , cor , valortextopreview, larguraCodigoBarra, alturaCodigoBarra, imprimirNumeroAbaixo, ordem, apresentarLabelEtiquetaAposTagEtiqueta, nomeArquivo, nomeArquivoApresentar, pastaBaseArquivo, cortexto, fontNegrito, alturaFoto, larguraFoto ) VALUES ( ?, ?, ?, ?, ?, ?, ? , ? , ? , ? , ? , ? , ? , ? , ?, ?, ?, ?, ?, ?, ? ) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				int x = 1;
				sqlInserir.setInt(x++, obj.getLayoutEtiquetaVO().getCodigo().intValue());
				sqlInserir.setString(x++, obj.getTagEtiqueta().toString());
				sqlInserir.setString(x++, obj.getLabelTag());
				sqlInserir.setInt(x++, obj.getMargemDireita().intValue());
				sqlInserir.setInt(x++, obj.getMargemTopo().intValue());
				sqlInserir.setBoolean(x++, obj.getRotacao());
				sqlInserir.setInt(x++, obj.getTamanhoFonte());
				sqlInserir.setBoolean(x++, obj.getCor());
				sqlInserir.setString(x++, obj.getValorTextoPreview());
				sqlInserir.setInt(x++, obj.getLarguraCodigoBarra());
				sqlInserir.setInt(x++, obj.getAlturaCodigoBarra());
				sqlInserir.setBoolean(x++, obj.getImprimirNumeroAbaixo());
				sqlInserir.setInt(x++, obj.getOrdem());
				sqlInserir.setBoolean(x++, obj.getApresentarLabelEtiquetaAposTagEtiqueta());
				sqlInserir.setString(x++, obj.getNomeArquivo());
				sqlInserir.setString(x++, obj.getNomeArquivoApresentar());
				sqlInserir.setString(x++, obj.getPastaBaseArquivo().name());
				sqlInserir.setString(x++, obj.getCorTexto());
				sqlInserir.setBoolean(x++, obj.getFontNegrito());
				sqlInserir.setInt(x++, obj.getAlturaFoto());
				sqlInserir.setInt(x++, obj.getLarguraFoto());
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
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>LayoutEtiquetaTagVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>MetaProducaoVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterar(final LayoutEtiquetaTagVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			validarDados(obj);
			realizarUpperCaseDados(obj);
			if (obj.getTagEtiqueta().equals(TagEtiquetaEnum.IMAGEM_FUNDO)) {
				realizarCopiaImagem(obj, configuracaoGeralSistemaVO, usuarioLogado);
			}
			final String sql = "UPDATE LayoutEtiquetaTag set layoutetiqueta = ? , tagetiqueta = ? , labeltag = ? , margemdireita = ? , margemtopo = ? , rotacao = ? , tamanhofonte = ? , cor = ? , valortextopreview = ? , larguraCodigoBarra  = ?, alturaCodigoBarra  = ?, imprimirNumeroAbaixo = ?, ordem = ?, apresentarLabelEtiquetaAposTagEtiqueta = ?, nomeArquivo=?, nomeArquivoApresentar=?, pastaBaseArquivo=?, cortexto=?, fontNegrito=?, alturaFoto=?, larguraFoto=? WHERE ((codigo = ?)) ";
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int x = 1;
					sqlAlterar.setInt(x++, obj.getLayoutEtiquetaVO().getCodigo().intValue());
					sqlAlterar.setString(x++, obj.getTagEtiqueta().toString());
					sqlAlterar.setString(x++, obj.getLabelTag());
					sqlAlterar.setInt(x++, obj.getMargemDireita().intValue());
					sqlAlterar.setInt(x++, obj.getMargemTopo().intValue());
					sqlAlterar.setBoolean(x++, obj.getRotacao());
					sqlAlterar.setInt(x++, obj.getTamanhoFonte());
					sqlAlterar.setBoolean(x++, obj.getCor());
					sqlAlterar.setString(x++, obj.getValorTextoPreview());
					sqlAlterar.setInt(x++, obj.getLarguraCodigoBarra());
					sqlAlterar.setInt(x++, obj.getAlturaCodigoBarra());
					sqlAlterar.setBoolean(x++, obj.getImprimirNumeroAbaixo());
					sqlAlterar.setInt(x++, obj.getOrdem().intValue());
					sqlAlterar.setBoolean(x++, obj.getApresentarLabelEtiquetaAposTagEtiqueta());
					sqlAlterar.setString(x++, obj.getNomeArquivo());
					sqlAlterar.setString(x++, obj.getNomeArquivoApresentar());
					sqlAlterar.setString(x++, obj.getPastaBaseArquivo().name());
					sqlAlterar.setString(x++, obj.getCorTexto());
					sqlAlterar.setBoolean(x++, obj.getFontNegrito());
					sqlAlterar.setInt(x++, obj.getAlturaFoto());
					sqlAlterar.setInt(x++, obj.getLarguraFoto());
					sqlAlterar.setInt(x++, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, configuracaoGeralSistemaVO, usuarioLogado);
				return;
			}
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>LayoutEtiquetaTagVO</code> no BD. Garantindo o relacionamento com a
	 * entidade principal <code>producao.LayoutEtiqueta</code> através do
	 * atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirLayoutEtiquetaItens(Integer LayoutEtiquetaPrm, List<LayoutEtiquetaTagVO> objetos, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		Iterator<LayoutEtiquetaTagVO> e = objetos.iterator();
		while (e.hasNext()) {
			LayoutEtiquetaTagVO obj = (LayoutEtiquetaTagVO) e.next();
			obj.getLayoutEtiquetaVO().setCodigo(LayoutEtiquetaPrm);
			incluir(obj, configuracaoGeralSistemaVO, usuarioLogado);
		}
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>LayoutEtiquetaTagVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	@Override
	public LayoutEtiquetaTagVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		try {
			consultar(getIdEntidade(), false, usuarioLogado);
			String sql = "SELECT * FROM LayoutEtiquetaTag WHERE codigo = ?";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( LayoutEtiqueta ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, configuracaoGeralSistemaVO, usuarioLogado));
		} catch (Exception e) {

			throw e;
		}
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>LayoutEtiquetaTagVO</code>.
	 * 
	 * @return O objeto da classe <code>LayoutEtiquetaTagVO</code> com os dados
	 *         devidamente montados.
	 */
	public LayoutEtiquetaTagVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		LayoutEtiquetaTagVO obj = new LayoutEtiquetaTagVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getLayoutEtiquetaVO().setCodigo(dadosSQL.getInt("layoutetiqueta"));
		obj.setTagEtiqueta(TagEtiquetaEnum.valueOf(dadosSQL.getString("tagetiqueta")));
		obj.setLabelTag(dadosSQL.getString("labeltag"));
		obj.setMargemDireita(dadosSQL.getInt("margemdireita"));
		obj.setAlturaCodigoBarra(dadosSQL.getInt("alturaCodigoBarra"));
		obj.setLarguraCodigoBarra(dadosSQL.getInt("larguraCodigoBarra"));
		obj.setMargemTopo(dadosSQL.getInt("margemtopo"));
		obj.setOrdem(dadosSQL.getInt("ordem"));
		obj.setRotacao(dadosSQL.getBoolean("rotacao"));
		obj.setImprimirNumeroAbaixo(dadosSQL.getBoolean("imprimirNumeroAbaixo"));
		obj.setTamanhoFonte(dadosSQL.getInt("tamanhofonte"));
		obj.setCor(dadosSQL.getBoolean("cor"));
		obj.setValorTextoPreview(dadosSQL.getString("valortextopreview"));
		obj.setApresentarLabelEtiquetaAposTagEtiqueta(dadosSQL.getBoolean("apresentarLabelEtiquetaAposTagEtiqueta"));
		obj.setNomeArquivo(dadosSQL.getString("nomeArquivo"));
		obj.setNomeArquivoApresentar(dadosSQL.getString("nomeArquivoApresentar"));
		obj.setCorTexto(dadosSQL.getString("cortexto"));
		obj.setFontNegrito(dadosSQL.getBoolean("fontNegrito"));
		obj.setAlturaFoto(dadosSQL.getInt("alturaFoto"));
		obj.setLarguraFoto(dadosSQL.getInt("larguraFoto"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("pastaBaseArquivo"))) {
			obj.setPastaBaseArquivo(PastaBaseArquivoEnum.valueOf(dadosSQL.getString("pastaBaseArquivo")));
			obj.setUrlImagemApresentar(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + obj.getPastaBaseArquivo().getValue() + "/" + obj.getNomeArquivo());
			obj.setUrlImagem(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getPastaBaseArquivo().getValue() + File.separator + obj.getNomeArquivo());
		}
		return obj;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>LayoutEtiquetaTagVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	@Override
	public void validarDados(LayoutEtiquetaTagVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		ConsistirException consistir = new ConsistirException();
		// if (obj.getLayoutEtiquetaVO()==null) {
		// consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_LayoutEtiquetaTag_labelTag"));
		// }

		if (consistir.existeErroListaMensagemErro()) {
			throw consistir;
		}

	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(LayoutEtiquetaTagVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		obj.setLabelTag(obj.getLabelTag().toUpperCase());

	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>LayoutEtiquetaTagVO</code> relacionados a um objeto da classe
	 * <code>LayoutEtiqueta</code>.
	 * 
	 * @param cliente
	 *            Atributo de <code>LayoutEtiquetaVO</code> a ser utilizado para
	 *            localizar os objetos da classe
	 *            <code>LayoutEtiquetaTagVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>LayoutEtiquetaTagVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<LayoutEtiquetaTagVO> consultarLayoutEtiquetaTagItens(Integer layoutEtiqueta, int nivelMontarDados, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		List<LayoutEtiquetaTagVO> objetos = new ArrayList<LayoutEtiquetaTagVO>(0);
		String sql = "SELECT * FROM LayoutEtiquetaTag WHERE layoutEtiqueta = ? order by ordem ";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { layoutEtiqueta });
		while (resultado.next()) {
			objetos.add(montarDados(resultado, nivelMontarDados, configuracaoGeralSistemaVO, usuarioLogado));
		}
		return objetos;
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>LayoutEtiquetaTagVO</code> contidos em um Hashtable no BD. Faz uso
	 * da operação <code>excluirLayoutEtiquetaTag</code> e
	 * <code>incluirLayoutEtiquetaTag</code> disponíveis na classe
	 * <code>LayoutEtiquetaTag</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void alterarLayoutEtiquetaItens(Integer layoutEtiqueta, List<LayoutEtiquetaTagVO> objetos, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		String str = "DELETE FROM LayoutEtiquetaTag WHERE layoutetiqueta = " + layoutEtiqueta.intValue();
		Iterator<LayoutEtiquetaTagVO> i = objetos.iterator();
		while (i.hasNext()) {
			LayoutEtiquetaTagVO objeto = (LayoutEtiquetaTagVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str);
		Iterator<LayoutEtiquetaTagVO> e = objetos.iterator();
		while (e.hasNext()) {
			LayoutEtiquetaTagVO objeto = (LayoutEtiquetaTagVO) e.next();
			if (objeto.getCodigo().equals(0)) {
				incluir(objeto, configuracaoGeralSistemaVO, usuarioLogado);
			} else {
				alterar(objeto, configuracaoGeralSistemaVO, usuarioLogado);
			}
		}
	}

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>MetaProducaoItemVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe <code>MetaProducaoItem</code>.
	 * 
	 * @param <code>metaProducao</code> campo chave para exclusão dos objetos no
	 *        BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	@Override
	public void excluirLayoutEtiquetaTagItens(Integer LayoutEtiqueta, UsuarioVO usuarioLogado) throws Exception {
		String sql = "DELETE FROM LayoutEtiquetaTag WHERE (layoutetiqueta = ?)";
		getConexao().getJdbcTemplate().update(sql, LayoutEtiqueta);
	}

	private void realizarCopiaImagem(LayoutEtiquetaTagVO layoutEtiquetaTagVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		if (layoutEtiquetaTagVO.getPastaBaseArquivo().equals(PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL_TMP) && !layoutEtiquetaTagVO.getNomeArquivo().trim().isEmpty()) {
			File arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL_TMP.getValue() + File.separator + layoutEtiquetaTagVO.getNomeArquivo());
			ArquivoHelper.salvarArquivoNaPastaTemp(arquivo, layoutEtiquetaTagVO.getNomeArquivo(), PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL.getValue(), configuracaoGeralSistemaVO, usuario);
			ArquivoHelper.delete(arquivo);
			layoutEtiquetaTagVO.setPastaBaseArquivo(PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL);
			layoutEtiquetaTagVO.setUrlImagem(configuracaoGeralSistemaVO.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL.getValue() + "/" + layoutEtiquetaTagVO.getNomeArquivo());
			if (!layoutEtiquetaTagVO.getNomeArquivoAnt().trim().isEmpty()) {
				arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL.getValue() + File.separator + layoutEtiquetaTagVO.getNomeArquivoAnt());
				ArquivoHelper.delete(arquivo);
			}
		}
	}
	
	@Override
	public List<LayoutEtiquetaTagVO> realizarCloneListaOriginalLayout(List<LayoutEtiquetaTagVO> listaLayoutEtiquetaTagVOs) throws Exception {
		List<LayoutEtiquetaTagVO> listaClonadaLayoutVOs = new ArrayList<LayoutEtiquetaTagVO>(0);
		for (LayoutEtiquetaTagVO layoutTagVO : listaLayoutEtiquetaTagVOs) {
			LayoutEtiquetaTagVO layoutTagClonadoVO = (LayoutEtiquetaTagVO) layoutTagVO.clone();
			listaClonadaLayoutVOs.add(layoutTagClonadoVO);
		}
		return listaClonadaLayoutVOs;
	}

}
