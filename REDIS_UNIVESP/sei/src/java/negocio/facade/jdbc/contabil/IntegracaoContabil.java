package negocio.facade.jdbc.contabil;

import java.io.File;
import java.io.FileWriter;
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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAdministrativoEnum;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.contabil.IntegracaoContabilVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;
import negocio.comuns.contabil.enumeradores.TipoGeracaoIntegracaoContabilEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.IntegracaoContabilInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class IntegracaoContabil extends ControleAcesso implements IntegracaoContabilInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4004391609560334686L;
	protected static String idEntidade;

	public IntegracaoContabil() throws Exception {
		super();
		setIdEntidade("IntegracaoContabil");
	}
	
	
	public void validarDados(IntegracaoContabilVO obj, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoGeracaoIntegracaoContabilEnum()), "O campo Tipo Geração (Integração Contábil) não foi informado.");
		Uteis.checkState(obj.getTipoGeracaoIntegracaoContabilEnum().isUnidadeEnsino() && !Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino (Integração Contábil) não foi informado.");
		Uteis.checkState(obj.getTipoGeracaoIntegracaoContabilEnum().isCodigoIntegracao() && !Uteis.isAtributoPreenchido(obj.getCodigoIntegracaoContabil()), "O campo Código da Integração Contábil (Integração Contábil) não foi informado.");
		if (!Uteis.isAtributoPreenchido(obj.getDataInicio()) || !Uteis.isAtributoPreenchido(obj.getDataTermino())) {
			throw new Exception("O campo Período (Integração Contábil) não foi informado.");
		}		
	}
	
	private void realizarVerificacaoPermissaoIntegracaoContabilPeriodoFechamentoMes(IntegracaoContabilVO obj, UsuarioVO usuario) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAdministrativoEnum.INTEGRACAO_CONTABIL_PERMITIR_APENAS_COM_PERIODO_FECHAMENTO_MES, usuario);			
		} catch (Exception e) {
			return;
			//Caso de sucesso.
			// caso tenha a excessao entao e liberado para o mesmo fazer a integracao sem validar o periodo.
		}
		List<FechamentoMesVO> lista = getFacadeFactory().getFechamentoMesFacade().consultarCompetenciaEmAbertoPorPeriodoPorUnidadeEnsinoPorCodigoIntegracaoContabil(obj.getDataInicio(), obj.getDataTermino(), obj.getUnidadeEnsinoVO().getCodigo(), obj.getCodigoIntegracaoContabil());
		Uteis.checkState(Uteis.isAtributoPreenchido(lista), "Não foi encontrado o Fechamento da Competência para o Mês/Ano "+lista.get(0).getMes_Apresentar() + "/"+ lista.get(0).getAno_Apresentar());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		validarDados(obj, usuarioVO);
		realizarVerificacaoPermissaoIntegracaoContabilPeriodoFechamentoMes(obj, usuarioVO);
		processarIntegracaoContabil(obj, verificarAcesso, usuarioVO, configuracaoGeralSistemaVO);
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}		
		getFacadeFactory().getLancamentoContabilFacade().atualizarLancamentoContabilPorIntegracaoContabil(obj, true, usuarioVO);
		getFacadeFactory().getLancamentoContabilFacade().atualizarLancamentoContabilPorIntegracaoContabil(obj, false, usuarioVO);
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
	public void incluir(final IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoContabil.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO IntegracaoContabil (unidadeEnsino, dataInicio, dataTermino, lote, valorLote, datageracao, responsavel, arquivo, tipogeracaointegracaocontabil, codigointegracaocontabil) ");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataInicio()));
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataTermino()));
					sqlInserir.setInt(++i, obj.getLote());
					sqlInserir.setDouble(++i, obj.getValorLote());
					sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataGeracao()));
					sqlInserir.setInt(++i, obj.getResponsavel().getCodigo());
					sqlInserir.setInt(++i, obj.getArquivo().getCodigo());
					sqlInserir.setString(++i, obj.getTipoGeracaoIntegracaoContabilEnum().name());
					sqlInserir.setString(++i, obj.getCodigoIntegracaoContabil());					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
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
	public void alterar(final IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoContabil.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE IntegracaoContabil ");
			sql.append("   SET unidadeEnsino=?, dataInicio=?, dataTermino=?, lote=?, ");
			sql.append("   valorLote=?, datageracao=?, responsavel=?, arquivo=?, ");
			sql.append("   tipogeracaointegracaocontabil=?, codigointegracaocontabil=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlAlterar);
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataInicio()));
					sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataTermino()));
					sqlAlterar.setInt(++i, obj.getLote());
					sqlAlterar.setDouble(++i, obj.getValorLote());
					sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataGeracao()));
					sqlAlterar.setInt(++i, obj.getResponsavel().getCodigo());
					sqlAlterar.setInt(++i, obj.getArquivo().getCodigo());
					sqlAlterar.setString(++i, obj.getTipoGeracaoIntegracaoContabilEnum().name());
					sqlAlterar.setString(++i, obj.getCodigoIntegracaoContabil());
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
	public void excluir(IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		try {
			IntegracaoContabil.excluir(getIdEntidade(), verificarAcesso, usuario);
			getFacadeFactory().getLancamentoContabilFacade().atualizarLancamentoContabilPorIntegracaoContabil(obj, true, usuario);
			String sql = "DELETE FROM IntegracaoContabil WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
			getFacadeFactory().getArquivoFacade().excluir(obj.getArquivo(), usuario, configuracaoGeralSistemaVO);
		} catch (Exception e) {
			throw e;
		}
	}

	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void preencherLoteContabil(IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM loteunidadeensinocontabil where 1=1 ");
		if (obj.getTipoGeracaoIntegracaoContabilEnum().isUnidadeEnsino()) {
			sql.append(" and unidadeensino = ").append(obj.getUnidadeEnsinoVO().getCodigo());
		} else if (obj.getTipoGeracaoIntegracaoContabilEnum().isCodigoIntegracao()) {
			sql.append("and  codigointegracaocontabil = '").append(obj.getCodigoIntegracaoContabil()).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			obj.setLote(rs.getInt("lote") + 1);
			alterarLoteUnidadeEnsinoContabil(obj, verificarAcesso, usuario);
		} else {
			obj.setLote(1);
			incluirLoteUnidadeEnsinoContabil(obj, verificarAcesso, usuario);
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void incluirLoteUnidadeEnsinoContabil(final IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoContabil.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO loteunidadeensinocontabil (unidadeEnsino, lote, codigointegracaocontabil ) ");
			sql.append("    VALUES (?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					sqlInserir.setInt(++i, obj.getLote());
					sqlInserir.setString(++i, obj.getCodigoIntegracaoContabil());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						return rs.getInt("codigo");
					}
					return null;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void alterarLoteUnidadeEnsinoContabil(final IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			IntegracaoContabil.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE loteunidadeensinocontabil ");
			sql.append("   SET lote= ").append(obj.getLote());
			if (obj.getTipoGeracaoIntegracaoContabilEnum().isUnidadeEnsino()) {
				sql.append(" where  unidadeensino = ").append(obj.getUnidadeEnsinoVO().getCodigo());
			} else if (obj.getTipoGeracaoIntegracaoContabilEnum().isCodigoIntegracao()) {
				sql.append("where  codigointegracaocontabil = '").append(obj.getCodigoIntegracaoContabil()).append("' ");
			}
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(sql.toString());
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void processarIntegracaoContabil(IntegracaoContabilVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
		if (!obj.getListaLancamentoContabil().isEmpty()) {
			if(!Uteis.isAtributoPreenchido(obj)) {
				preencherLoteContabil(obj, verificarAcesso, usuarioVO);
			}else if(Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getArquivo())) {
				File diretorioASerIncluido = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo());
				if (!diretorioASerIncluido.exists()) {
					diretorioASerIncluido.mkdirs();
				}
				File file = new File(diretorioASerIncluido.getPath() + File.separator + obj.getArquivo().getNome());
				if(file.exists()) {
					file.delete();
				}
				
			}
			String extensao = "";
			long tempo = new Date().getTime();
			if (obj.getConfiguracaoContabilVO().getLayoutIntegracaoVO().getTipoLayoutIntegracao().isXml()) {
				getFacadeFactory().getLayoutIntegracaoFacade().gerarLayoutXmlParaIntegracaoContabil(obj, obj.getConfiguracaoContabilVO().getLayoutIntegracaoVO(), usuarioVO);
				extensao = "xml";
			} else if (obj.getConfiguracaoContabilVO().getLayoutIntegracaoVO().getTipoLayoutIntegracao().isTxt()) {
				getFacadeFactory().getLayoutIntegracaoFacade().gerarLayoutTxtParaIntegracaoContabil(obj, obj.getConfiguracaoContabilVO().getLayoutIntegracaoVO(), usuarioVO);
				extensao = "txt";
			}
			obj.getArquivo().setNome("lancamentoContabil_" + obj.getResponsavel().getCodigo() + "_" + tempo + "." + extensao);
			obj.getArquivo().setDescricao("lancamentoContabil_" + tempo);
			obj.getArquivo().setExtensao(extensao);
			obj.getArquivo().setDataUpload(new Date());
			obj.getArquivo().setDataDisponibilizacao(new Date());
			obj.getArquivo().setManterDisponibilizacao(true);
			obj.getArquivo().setSituacao(SituacaoArquivo.ATIVO.getValor());
			obj.getArquivo().setPessoaVO(obj.getResponsavel().getPessoa());
			obj.getArquivo().setPastaBaseArquivo(PastaBaseArquivoEnum.CONTABIL.getValue() + File.separator + UteisData.getAnoData(obj.getArquivo().getDataUpload()) + File.separator + UteisData.getMesData(obj.getArquivo().getDataUpload()));
			obj.getArquivo().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.CONTABIL);
			obj.getArquivo().setResponsavelUpload(obj.getResponsavel());
			obj.getArquivo().setValidarDados(false);
			File diretorioASerIncluido = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivo().getPastaBaseArquivo());
			if (!diretorioASerIncluido.exists()) {
				diretorioASerIncluido.mkdirs();
			}
			File file = new File(diretorioASerIncluido.getPath() + File.separator + obj.getArquivo().getNome());
			file.createNewFile();
			FileWriter arquivo = new FileWriter(file);
			arquivo.write(obj.getTextoArquivo());
			arquivo.close();
			getFacadeFactory().getArquivoFacade().persistir(obj.getArquivo(), verificarAcesso, usuarioVO, configuracaoGeralSistemaVO);
		} else {
			throw new Exception("Não existe lançamentos contábeis para esse período informado.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarLancamentoContabilDisponivelParaIntegracao(IntegracaoContabilVO obj, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj, usuarioVO);
		obj.setConfiguracaoContabilVO(getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorIntegracaoContabil(obj, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
		if (Uteis.isAtributoPreenchido(obj.getConfiguracaoContabilVO())) {
			obj.getConfiguracaoContabilVO().setLayoutIntegracaoVO(Uteis.montarDadosVO(obj.getConfiguracaoContabilVO().getLayoutIntegracaoVO().getCodigo(), LayoutIntegracaoVO.class, p -> getFacadeFactory().getLayoutIntegracaoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO)));
			getFacadeFactory().getLancamentoContabilFacade().consultaLoteLancamentoContabilParaProcessamento(obj, obj.getConfiguracaoContabilVO().getLayoutIntegracaoVO(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);			
			if (obj.getListaLancamentoContabil().isEmpty()) {
				throw new Exception("Não existe lançamentos contábeis para esse período informado.");
			}
		} else {
			throw new Exception("Não existe uma configuração contábil para esse tipo de Geração.");
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ic.codigo as \"ic.codigo\", ic.datageracao as \"ic.datageracao\", ic.dataTermino as \"ic.dataTermino\", ic.dataInicio as \"ic.dataInicio\", ");
		sql.append(" ic.lote as \"ic.lote\", ic.valorlote as \"ic.valorlote\",  ");
		sql.append(" ic.tipogeracaointegracaocontabil as \"ic.tipogeracaointegracaocontabil\", ");
		sql.append(" ic.codigointegracaocontabil as \"ic.codigointegracaocontabil\", ");

		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", unidadeensino.codigoIntegracaoContabil as \"unidadeensino.codigoIntegracaoContabil\", ");

		sql.append(" arquivo.codigo as \"arquivo.codigo\" , arquivo.codOrigem as \"arquivo.codOrigem\", arquivo.nome as \"arquivo.nome\", ");
		sql.append(" arquivo.descricao as \"arquivo.descricao\", arquivo.dataupload as \"arquivo.dataupload\", ");
		sql.append(" arquivo.manterDisponibilizacao as \"arquivo.manterDisponibilizacao\", arquivo.origem as \"arquivo.origem\",arquivo.situacao as \"arquivo.situacao\",  ");
		sql.append(" arquivo.responsavelupload as \"arquivo.responsavelupload\", arquivo.extensao as \"arquivo.extensao\", ");
		sql.append(" arquivo.pastabasearquivo as \"arquivo.pastabasearquivo\", arquivo.dataDisponibilizacao as \"arquivo.dataDisponibilizacao\", ");

		sql.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\",  usuario.pessoa as \"usuario.pessoa\", ");

		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.nome as \"pessoa.nome\",  pessoa.cpf as \"pessoa.cpf\", pessoa.dataNasc as \"pessoa.dataNasc\" ");

		sql.append(" FROM integracaocontabil ic ");
		sql.append(" left join unidadeensino on unidadeensino.codigo = ic.unidadeensino ");
		sql.append(" inner join usuario on usuario.codigo = ic.responsavel ");
		sql.append(" inner join arquivo on arquivo.codigo = ic.arquivo ");
		sql.append(" left join pessoa  on arquivo.pessoa = pessoa.codigo ");
		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoContabilVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ic.codigo = ").append(valorConsulta).append(" ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and ic.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" ORDER BY ic.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoContabilVO> consultaRapidaPorLote(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ic.lote = ").append(valorConsulta).append(" ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and ic.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" ORDER BY ic.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoContabilVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(unidadeensino.nome) ilike(sem_acentos(?)) ");
		sqlStr.append(" ORDER BY unidadeensino.nome desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoContabilVO> consultaRapidaPorDataGeracao(Date dataInicial, Date dataFinal, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE ic.datageracao >= '").append(Uteis.getDataBD0000(dataInicial)).append("' ");
		sqlStr.append(" AND ic.datageracao <= '").append(Uteis.getDataBD2359(dataFinal)).append("' ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and ic.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" ORDER BY ic.datageracao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoContabilVO> consultaRapidaPorPeriodoContabil(Date dataInicio, Date dataTermino, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE ic.dataInicio >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sqlStr.append(" AND ic.dataTermino <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and ic.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" ORDER BY ic.datageracao desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoContabilVO> consultaRapidaPorResponsavel(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE sem_acentos(usuario.nome) ilike(sem_acentos(?))");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and ic.unidadeensino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" ORDER BY usuario.nome desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public IntegracaoContabilVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ic.codigo = ").append(codigoPrm).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( IntegracaoContabilVO ).");
		}
		IntegracaoContabilVO obj = new IntegracaoContabilVO();
		montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
		return obj;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<IntegracaoContabilVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<IntegracaoContabilVO> vetResultado = new ArrayList<IntegracaoContabilVO>(0);
		while (tabelaResultado.next()) {
			IntegracaoContabilVO obj = new IntegracaoContabilVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarDadosBasico(IntegracaoContabilVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(new Integer(dadosSQL.getInt("ic.codigo")));
		obj.setLote(new Integer(dadosSQL.getInt("ic.lote")));
		obj.setValorLote(new Double(dadosSQL.getDouble("ic.valorlote")));
		obj.setDataGeracao((dadosSQL.getDate("ic.datageracao")));
		obj.setDataInicio((dadosSQL.getDate("ic.datainicio")));
		obj.setDataTermino((dadosSQL.getDate("ic.datatermino")));
		obj.setTipoGeracaoIntegracaoContabilEnum(TipoGeracaoIntegracaoContabilEnum.valueOf(dadosSQL.getString("ic.tipogeracaointegracaocontabil")));
		obj.setCodigoIntegracaoContabil(dadosSQL.getString("ic.codigointegracaocontabil"));

		obj.getUnidadeEnsinoVO().setCodigo(new Integer(dadosSQL.getInt("unidadeensino.codigo")));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getUnidadeEnsinoVO().setCodigoIntegracaoContabil(dadosSQL.getString("unidadeensino.codigoIntegracaoContabil"));

		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("usuario.codigo")));
		obj.getResponsavel().setNome(dadosSQL.getString("usuario.nome"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}

		obj.getArquivo().setCodigo(new Integer(dadosSQL.getInt("arquivo.codigo")));
		obj.getArquivo().setNome(dadosSQL.getString("arquivo.nome"));
		obj.getArquivo().setDescricao(dadosSQL.getString("arquivo.descricao"));
		obj.getArquivo().setDescricaoAntesAlteracao(dadosSQL.getString("arquivo.descricao"));
		obj.getArquivo().setExtensao(dadosSQL.getString("arquivo.extensao"));
		obj.getArquivo().setDataUpload(dadosSQL.getDate("arquivo.dataUpload"));
		obj.getArquivo().setPastaBaseArquivo(dadosSQL.getString("arquivo.pastaBaseArquivo"));
		obj.getArquivo().setDataDisponibilizacao(dadosSQL.getDate("arquivo.dataDisponibilizacao"));
		obj.getArquivo().setManterDisponibilizacao((dadosSQL.getBoolean("arquivo.manterDisponibilizacao")));
		obj.getArquivo().setOrigem(dadosSQL.getString("arquivo.origem"));
		obj.getArquivo().setSituacao(dadosSQL.getString("arquivo.situacao"));
		obj.getArquivo().setCodOrigem(dadosSQL.getInt("arquivo.codOrigem"));

		obj.getArquivo().getResponsavelUpload().setCodigo(dadosSQL.getInt("usuario.codigo"));
		obj.getArquivo().getResponsavelUpload().setNome(dadosSQL.getString("usuario.nome"));

		obj.getArquivo().getPessoaVO().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getArquivo().getPessoaVO().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getArquivo().getPessoaVO().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getArquivo().getPessoaVO().setDataNasc(dadosSQL.getDate("pessoa.dataNasc"));
		getFacadeFactory().getLancamentoContabilFacade().consultaLancamentoContabilPorIntegracaoContabil(obj, nivelMontarDados, usuario);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return IntegracaoContabil.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		IntegracaoContabil.idEntidade = idEntidade;
	}

}
