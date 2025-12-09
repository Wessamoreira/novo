package negocio.facade.jdbc.financeiro;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.enumeradores.AlinhamentoAssinaturaDigitalEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.TipoDesigneTextoEnum;
import negocio.comuns.financeiro.TextoPadraoTagVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.TextoPadraoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TextoPadraoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TextoPadraoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see TextoPadraoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TextoPadrao extends ControleAcesso implements TextoPadraoInterfaceFacade {

	protected static String idEntidade;

	public TextoPadrao() throws Exception {
		super();
		setIdEntidade("TextoPadrao");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>TextoPadraoVO</code>.
	 */
	public TextoPadraoVO novo() throws Exception {
		incluir(getIdEntidade());
		TextoPadraoVO obj = new TextoPadraoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>TextoPadraoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TextoPadraoVO</code> que será gravado
	 *            no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TextoPadraoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			TextoPadrao.incluir(getIdEntidade(), true, usuario);
			TextoPadraoVO.validarDados(obj);
			final String sql = "INSERT INTO TextoPadrao( descricao, dataDefinicao, responsavelDefinicao, texto, situacao, tipo, unidadeEnsino, orientacaoDaPagina, margemDireita, margemEsquerda, margemSuperior, margemInferior, tipodesignetextoenum, arquivoireport, assinardigitalmentetextopadrao, alinhamentoAssinaturaDigitalEnum, corAssinaturaDigitalmente, alturaassinatura, larguraassinatura,funcionarioPrimario,funcionarioSecundario,cargoFuncionarioPrimario,cargoFuncionarioSecundario,assinarDocumentoAutomaticamenteFuncionarioPrimario,assinarDocumentoAutomaticamenteFuncionarioSecundario  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ?, ? , ? , ? , ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataDefinicao()));
					if (obj.getResponsavelDefinicao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getResponsavelDefinicao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getTexto());
					sqlInserir.setString(5, obj.getSituacao());
					sqlInserir.setString(6, obj.getTipo());
					if (obj.getUnidadeEnsino().getCodigo() != 0) {
						sqlInserir.setInt(7, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setString(8, obj.getOrientacaoDaPagina());
					sqlInserir.setString(9, obj.getMargemDireita());
					sqlInserir.setString(10, obj.getMargemEsquerda());
					sqlInserir.setString(11, obj.getMargemSuperior());
					sqlInserir.setString(12, obj.getMargemInferior());
					sqlInserir.setString(13, obj.getTipoDesigneTextoEnum().name());
//					if(Uteis.isAtributoPreenchido(obj.getArquivoIreport())){
//						sqlInserir.setInt(14, obj.getArquivoIreport().getCodigo());
//					}else{
						sqlInserir.setNull(14, 0);
//					}
					sqlInserir.setBoolean(15, obj.getAssinarDigitalmenteTextoPadrao());
					sqlInserir.setString(16, obj.getAlinhamentoAssinaturaDigitalEnum().name());
					sqlInserir.setString(17, obj.getCorAssinaturaDigitalmente());
					sqlInserir.setFloat(18, obj.getAlturaAssinatura());
					sqlInserir.setFloat(19, obj.getLarguraAssinatura());
					if (obj.getFuncionarioPrimarioVO().getCodigo() != 0) {
						sqlInserir.setInt(20,obj.getFuncionarioPrimarioVO().getCodigo());
					}else {
						sqlInserir.setNull(20, 0);
					}
					if (obj.getFuncionarioSecundarioVO().getCodigo() != 0) {
						sqlInserir.setInt(21,obj.getFuncionarioSecundarioVO().getCodigo());
					}else {
						sqlInserir.setNull(21, 0);
					}
					if (obj.getCargoFuncionarioPrincipalVO().getCodigo() != 0) {
						sqlInserir.setInt(22,obj.getCargoFuncionarioPrincipalVO().getCodigo());
					}else {
						sqlInserir.setNull(22, 0);
					}
					if (obj.getCargoFuncionarioSecundarioVO().getCodigo() != 0) {
						sqlInserir.setInt(23,obj.getCargoFuncionarioSecundarioVO().getCodigo());
					}else {
						sqlInserir.setNull(23, 0);
					}
					sqlInserir.setBoolean(24, obj.getAssinarDocumentoAutomaticamenteFuncionarioPrimario());
					sqlInserir.setBoolean(25, obj.getAssinarDocumentoAutomaticamenteFuncionarioSecundario());
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
			 if(obj.getTipoDesigneTextoEnum().isPdf()){
	            	for(ArquivoVO arquivoVO :obj.getListaArquivoIreport()) {
	            		arquivoVO.setValidarDados(false);
	            		arquivoVO.setCodOrigem(obj.getCodigo());
	    				getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuario, configuracaoGeralSistemaVO);
	            	}
				}
			getFacadeFactory().getTextoPadraoTagFacade().incluirTextoPadraoTag(obj.getCodigo(), obj.getListaTagUtilizado(), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>TextoPadraoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TextoPadraoVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TextoPadraoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			TextoPadrao.alterar(getIdEntidade(), true, usuario);
			TextoPadraoVO.validarDados(obj);
			final String sql = "UPDATE TextoPadrao set descricao=?, dataDefinicao=?, responsavelDefinicao=?, texto=?, situacao=?, tipo=?, unidadeEnsino=?, orientacaoDaPagina = ?, margemDireita = ?, margemEsquerda = ?, margemSuperior = ?, margemInferior = ?, tipodesignetextoenum = ?, arquivoireport = ?, assinardigitalmentetextopadrao=?, alinhamentoAssinaturaDigitalEnum=?, corAssinaturaDigitalmente=?, alturaassinatura=?, larguraassinatura=? , funcionarioPrimario=?,funcionarioSecundario=?,cargoFuncionarioPrimario=?,cargoFuncionarioSecundario=?,assinarDocumentoAutomaticamenteFuncionarioPrimario=?,assinarDocumentoAutomaticamenteFuncionarioSecundario=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataDefinicao()));
					if (obj.getResponsavelDefinicao().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getResponsavelDefinicao().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getTexto());
					sqlAlterar.setString(5, obj.getSituacao());
					sqlAlterar.setString(6, obj.getTipo());
					if (obj.getUnidadeEnsino().getCodigo() != 0) {
						sqlAlterar.setInt(7, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setString(8, obj.getOrientacaoDaPagina());
					sqlAlterar.setString(9, obj.getMargemDireita());
					sqlAlterar.setString(10, obj.getMargemEsquerda());
					sqlAlterar.setString(11, obj.getMargemSuperior());
					sqlAlterar.setString(12, obj.getMargemInferior());
					sqlAlterar.setString(13, obj.getTipoDesigneTextoEnum().name());
//					if(Uteis.isAtributoPreenchido(obj.getArquivoIreport())){
//						sqlAlterar.setInt(14, obj.getArquivoIreport().getCodigo());
//					}else{
						sqlAlterar.setNull(14, 0);
//					}
					sqlAlterar.setBoolean(15, obj.getAssinarDigitalmenteTextoPadrao());
					sqlAlterar.setString(16, obj.getAlinhamentoAssinaturaDigitalEnum().name());
					sqlAlterar.setString(17, obj.getCorAssinaturaDigitalmente());
					sqlAlterar.setFloat(18, obj.getAlturaAssinatura());
					sqlAlterar.setFloat(19, obj.getLarguraAssinatura());
					if (obj.getFuncionarioPrimarioVO().getCodigo() != 0) {
						sqlAlterar.setInt(20,obj.getFuncionarioPrimarioVO().getCodigo());
					}else {
						sqlAlterar.setNull(20, 0);
					}
					if (obj.getFuncionarioSecundarioVO().getCodigo() != 0) {
						sqlAlterar.setInt(21,obj.getFuncionarioSecundarioVO().getCodigo());
					}else {
						sqlAlterar.setNull(21, 0);
					}
					if (obj.getCargoFuncionarioPrincipalVO().getCodigo() != 0) {
						sqlAlterar.setInt(22,obj.getCargoFuncionarioPrincipalVO().getCodigo());
					}else {
						sqlAlterar.setNull(22, 0);
					}
					if (obj.getCargoFuncionarioSecundarioVO().getCodigo() != 0) {
						sqlAlterar.setInt(23,obj.getCargoFuncionarioSecundarioVO().getCodigo());
					}else {
						sqlAlterar.setNull(23, 0);
					}
					sqlAlterar.setBoolean(24, obj.getAssinarDocumentoAutomaticamenteFuncionarioPrimario());
					sqlAlterar.setBoolean(25, obj.getAssinarDocumentoAutomaticamenteFuncionarioSecundario());
					sqlAlterar.setInt(26, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			  if(obj.getTipoDesigneTextoEnum().isPdf()){
	            	for(ArquivoVO arquivoVO : obj.getListaArquivoIreport()) {
	            		arquivoVO.setValidarDados(false);
	            		arquivoVO.setCodOrigem(obj.getCodigo());
	            		if(Uteis.isAtributoPreenchido(arquivoVO.getCodigo())) {
	            			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IREPORT);
	            		}else {
	            			arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.IREPORT_TMP);
	            		}
	            		getFacadeFactory().getArquivoFacade().persistir(arquivoVO, false, usuario, configuracaoGeralSistemaVO);
	            	}		
				} 
			getFacadeFactory().getTextoPadraoTagFacade().alterarTextoPadraoTag(obj.getCodigo(), obj.getListaTagUtilizado(), usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public List obterTagsAlteradas(String texto, Integer codigo) {
		List lista = new ArrayList(0);
		while ((texto.indexOf("[") != -1) && (texto.indexOf("]") != -1)) {
			int posicaoIni = texto.indexOf("[");
			int posicaoFim = texto.indexOf("]");
			String tag = texto.substring(posicaoIni, posicaoFim + 1);
			TextoPadraoTagVO p = new TextoPadraoTagVO();
			p.setTag(tag);
			p.setTextoPadrao(codigo);
			lista.add(p);
			texto = texto.substring(0, posicaoIni) + texto.substring(posicaoFim + 1);
		}
		return lista;
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TextoPadraoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TextoPadraoVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TextoPadraoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			TextoPadrao.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM TextoPadrao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			if(Uteis.isAtributoPreenchido(obj.getListaArquivoIreport())){
				for(ArquivoVO arquivoVO : obj.getListaArquivoIreport()) {
					getFacadeFactory().getArquivoFacade().excluirArquivoDoDiretorioEspecifico(arquivoVO, configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivoIreport().getPastaBaseArquivo());
					getFacadeFactory().getArquivoFacade().excluir(arquivoVO, usuario, configuracaoGeralSistemaVO);	
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>TextoPadrao</code> através
	 * do valor do atributo <code>situacao</code> da classe
	 * <code>Colaborador</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TextoPadraoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorResponsavelDefinicao(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT TextoPadrao.* FROM TextoPadrao, Usuario WHERE TextoPadrao.responsavelDefinicao = Usuario.codigo and upper( Usuario.nome ) like('" + valorConsulta.toUpperCase() + "%') ";
		if (unidadeEnsino > 0) {
			sqlStr += " AND unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY Usuario.nome";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TextoPadrao</code> através
	 * do valor do atributo <code>Date dataDefinicao</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TextoPadraoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataDefinicao(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TextoPadrao WHERE ((dataDefinicao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataDefinicao <= '" + Uteis.getDataJDBC(prmFim) + "')) ";
		if (unidadeEnsino > 0) {
			sqlStr += " AND unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY dataDefinicao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TextoPadrao</code> através
	 * do valor do atributo <code>String descricao</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TextoPadraoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TextoPadrao WHERE upper( descricao ) like('" + valorConsulta.toUpperCase() + "%') ";
		if (unidadeEnsino > 0) {
			sqlStr += " AND unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TextoPadrao</code> através
	 * do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TextoPadraoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TextoPadrao WHERE codigo >= " + valorConsulta.intValue() + " ";
		if (unidadeEnsino > 0) {
			sqlStr += " AND unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TextoPadrao</code> através
	 * do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TextoPadraoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoSituacao(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TextoPadrao WHERE codigo >= " + valorConsulta.intValue() + " and situacao = 'AT' ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TextoPadrao WHERE tipo = '" + valorConsulta + "' ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List consultarPorTipoEUnidadeEnsino(String valorConsulta, Integer codigoUnidade, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM TextoPadrao WHERE tipo = '");
		sql.append(valorConsulta);
		sql.append("' and (");
		if (codigoUnidade != null && codigoUnidade != 0) {
			sql.append(" unidadeensino = ").append(codigoUnidade);
			sql.append(" or unidadeensino is null ) and ");
		} else {
			sql.append("  unidadeensino is null )");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	// Método não poderá ser alterado para não ocorrer pioras na performance.
	public List<TextoPadraoVO> consultarPorTipoNivelComboBox(String valorConsulta, UnidadeEnsinoVO unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT codigo, descricao FROM TextoPadrao WHERE tipo = '" + valorConsulta + "' ";
		if (unidadeEnsino != null && unidadeEnsino.getCodigo() != 0) {
			sqlStr += "AND (unidadeEnsino = " + unidadeEnsino.getCodigo() + " OR unidadeEnsino is null) ";
		}
		if(Uteis.isAtributoPreenchido(situacao)) {
			sqlStr += "AND (situacao = '" + situacao + "') ";
		}
		sqlStr += "ORDER BY descricao ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<TextoPadraoVO> vetResultado = new ArrayList<TextoPadraoVO>(0);
		while (tabelaResultado.next()) {
			TextoPadraoVO obj = new TextoPadraoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT (textopadrao.*) FROM textopadrao ");
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.contratoMatricula = textoPadrao.codigo");
		sqlStr.append(" OR matriculaPeriodo.contratoFiador = textoPadrao.codigo");
		sqlStr.append(" WHERE matriculaPeriodo.matricula = '" + valorConsulta.toUpperCase() + "' ;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>TextoPadraoVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TextoPadraoVO</code>.
	 * 
	 * @return O objeto da classe <code>TextoPadraoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static TextoPadraoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// // System.out.println(">> Montar dados(TextoPadrao) - " + new
		// Date());
		TextoPadraoVO obj = new TextoPadraoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setTipo(dadosSQL.getString("tipo"));
		obj.setDataDefinicao(dadosSQL.getDate("dataDefinicao"));
		obj.getResponsavelDefinicao().setCodigo(new Integer(dadosSQL.getInt("responsavelDefinicao")));
		obj.setTexto(dadosSQL.getString("texto"));
		obj.setOrientacaoDaPagina(dadosSQL.getString("orientacaoDaPagina"));
		obj.setMargemDireita(dadosSQL.getString("margemDireita"));
		obj.setMargemEsquerda(dadosSQL.getString("margemEsquerda"));
		obj.setMargemSuperior(dadosSQL.getString("margemSuperior"));
		obj.setMargemInferior(dadosSQL.getString("margemInferior"));
		obj.setCorAssinaturaDigitalmente(dadosSQL.getString("corAssinaturaDigitalmente"));
		obj.setAlturaAssinatura(dadosSQL.getFloat("alturaAssinatura"));
		obj.setLarguraAssinatura(dadosSQL.getFloat("larguraAssinatura"));
		obj.setAssinarDigitalmenteTextoPadrao(dadosSQL.getBoolean("assinardigitalmentetextopadrao"));
		obj.setAlinhamentoAssinaturaDigitalEnum(AlinhamentoAssinaturaDigitalEnum.valueOf(dadosSQL.getString("alinhamentoAssinaturaDigitalEnum")));
		obj.setTipoDesigneTextoEnum(TipoDesigneTextoEnum.valueOf(dadosSQL.getString("tipodesignetextoenum")));
		obj.getFuncionarioPrimarioVO().setCodigo(dadosSQL.getInt("funcionarioPrimario"));
		obj.getFuncionarioSecundarioVO().setCodigo(dadosSQL.getInt("funcionarioSecundario"));
		obj.getCargoFuncionarioPrincipalVO().setCodigo(dadosSQL.getInt("cargoFuncionarioPrimario"));
		obj.getCargoFuncionarioSecundarioVO().setCodigo(dadosSQL.getInt("cargoFuncionarioSecundario"));
		obj.setAssinarDocumentoAutomaticamenteFuncionarioPrimario(dadosSQL.getBoolean("assinarDocumentoAutomaticamenteFuncionarioPrimario"));
		obj.setAssinarDocumentoAutomaticamenteFuncionarioSecundario(dadosSQL.getBoolean("assinarDocumentoAutomaticamenteFuncionarioSecundario"));
		if(Uteis.isAtributoPreenchido(obj.getCodigo()) && obj.getTipoDesigneTextoEnum().isPdf()){
			obj.setListaArquivoIreport(getFacadeFactory().getArquivoFacade().consultarPorCodOrigemTipoOrigem(obj.getCodigo(), OrigemArquivo.TEXTO_PADRAO.getValor(), nivelMontarDados, usuario));
		}
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setListaTagUtilizado(getFacadeFactory().getTextoPadraoTagFacade().consultarTextoPadraoTag(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));

		montarDadosResponsavelDefinicao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosFuncionarioPrimario(obj, usuario);
		montarDadosFuncionarioSecundario(obj, usuario);
		montarDadosCargoFuncionarioPrimario(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		montarDadosCargoFuncionarioSecundario(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ColaboradorVO</code> relacionado ao objeto
	 * <code>TextoPadraoVO</code>. Faz uso da chave primária da classe
	 * <code>ColaboradorVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelDefinicao(TextoPadraoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelDefinicao().getCodigo().intValue() == 0) {
			obj.setResponsavelDefinicao(new UsuarioVO());
			return;
		}
		obj.setResponsavelDefinicao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelDefinicao().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(TextoPadraoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>TextoPadraoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public TextoPadraoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM TextoPadrao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados (Texto Padrão).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TextoPadrao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TextoPadrao.idEntidade = idEntidade;
	}

	public List<SelectItem> listaSelectItemOrientacaoDaPagina;

	public List<SelectItem> getListaSelectItemOrientacaoDaPagina() {
		if (listaSelectItemOrientacaoDaPagina != null) {
			listaSelectItemOrientacaoDaPagina = new ArrayList<SelectItem>(0);
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("RE", "Retrato"));
			listaSelectItemOrientacaoDaPagina.add(new SelectItem("PA", "Paisagem"));
		}
		return listaSelectItemOrientacaoDaPagina;
	}
	
	public static void montarDadosFuncionarioPrimario(TextoPadraoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioPrimarioVO().getCodigo().intValue() == 0) {
			obj.setFuncionarioPrimarioVO(new FuncionarioVO());
			return;
		}
		obj.setFuncionarioPrimarioVO(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(obj.getFuncionarioPrimarioVO().getCodigo(), false, usuario));
	}
	public static void montarDadosFuncionarioSecundario(TextoPadraoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getFuncionarioSecundarioVO().getCodigo().intValue() == 0) {
			obj.setFuncionarioSecundarioVO(new FuncionarioVO());
			return;
		}
		obj.setFuncionarioSecundarioVO(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(obj.getFuncionarioSecundarioVO().getCodigo(), false, usuario));
	}
	
	public static void montarDadosCargoFuncionarioPrimario(TextoPadraoVO obj,int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCargoFuncionarioPrincipalVO().getCodigo().intValue() == 0) {
			obj.setCargoFuncionarioPrincipalVO(new CargoVO());
			return;
		}
		obj.setCargoFuncionarioPrincipalVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargoFuncionarioPrincipalVO().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	public static void montarDadosCargoFuncionarioSecundario(TextoPadraoVO obj,int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCargoFuncionarioSecundarioVO().getCodigo().intValue() == 0) {
			obj.setCargoFuncionarioSecundarioVO(new CargoVO());
			return;
		}
		obj.setCargoFuncionarioSecundarioVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargoFuncionarioSecundarioVO().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	@Override
	public Boolean  verificarTextoPadraoAssinaDigitalmentePorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT DISTINCT (textopadrao.codigo) FROM textopadrao ");
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.contratoMatricula = textoPadrao.codigo ");		
		sqlStr.append(" WHERE textopadrao.assinardigitalmentetextopadrao and  matriculaPeriodo.matricula = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta.toUpperCase());
		return tabelaResultado.next();
	}
	
	
	@Override
	public TextoPadraoVO consultarTextoPadraoContratoMatriculaPorMatriculaPeriodo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM textopadrao ");
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.contratoMatricula = textoPadrao.codigo ");		
		sqlStr.append(" WHERE matriculaPeriodo.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),valorConsulta);			
		if (!tabelaResultado.next()) {
			return new TextoPadraoVO();
		}			
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
		
	}
	
	
	@Override
	public TextoPadraoVO consultarTextoPadraoContratoMatriculaPorCurso(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT textopadrao.* FROM textopadrao ");
		sqlStr.append(" INNER JOIN curso ON curso.textoPadraoContratoMatriculaCalouro = textoPadrao.codigo ");		
		sqlStr.append(" WHERE curso.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),valorConsulta);			
		if (!tabelaResultado.next()) {
			return new TextoPadraoVO();
		}			
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
		
	}
	
	@Override
	public List consultaSimplesTextoPadraoFinanceiro(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT * FROM TextoPadrao" ;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

}
