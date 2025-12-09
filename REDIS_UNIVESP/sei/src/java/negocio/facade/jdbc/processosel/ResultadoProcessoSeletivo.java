package negocio.facade.jdbc.processosel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.richfaces.event.FileUploadEvent;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.PoliticaGerarAgendaEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.processosel.ColunaGabaritoVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.ImportarCandidatoInscricaoProcessoSeletivoVO;
import negocio.comuns.processosel.InscricaoRespostaNaoProcessadaVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.processosel.ResultadoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessamentoArquivoRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoGabaritoRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoProvaRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.MotivoNaoProcessamentoRespostaProcessoSeletivoEnum;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.ResultadoProcessoSeletivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ResultadoProcessoSeletivoVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ResultadoProcessoSeletivoVO</code>. Encapsula toda
 * a interação com o banco de dados.
 * 
 * @see ResultadoProcessoSeletivoVO
 * @see ControleAcesso
 */
@Repository
public class ResultadoProcessoSeletivo extends ControleAcesso implements ResultadoProcessoSeletivoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ResultadoProcessoSeletivo() throws Exception {
		super();
		setIdEntidade("ResultadoProcessoSeletivo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ResultadoProcessoSeletivoVO</code>.
	 */
	public ResultadoProcessoSeletivoVO novo(UsuarioVO usuarioVO) throws Exception {
		ResultadoProcessoSeletivo.incluir(getIdEntidade());
		ResultadoProcessoSeletivoVO obj = new ResultadoProcessoSeletivoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ResultadoProcessoSeletivoVO</code>. Primeiramente valida os dados
	 * ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoProcessoSeletivoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ResultadoProcessoSeletivoVO.validarDados(obj);
			ResultadoProcessoSeletivo.incluir(getIdEntidade(), true, usuarioVO);
			final String sql = "INSERT INTO ResultadoProcessoSeletivo( inscricao, resultadoPrimeiraOpcao, resultadoSegundaOpcao, resultadoTerceiraOpcao, mediaNotasProcSeletivo, " + "mediaPonderadaNotasProcSeletivo, responsavel, dataRegistro, somatorioAcertos, respostaProvaProcessoSeletivo, notaRedacao, redacao , navegadorAcesso  ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getInscricao().getCodigo().intValue());
					sqlInserir.setString(2, obj.getResultadoPrimeiraOpcao());
					sqlInserir.setString(3, obj.getResultadoSegundaOpcao());
					sqlInserir.setString(4, obj.getResultadoTerceiraOpcao());
					sqlInserir.setDouble(5, obj.getMediaNotasProcSeletivo().doubleValue());
					sqlInserir.setDouble(6, obj.getMediaPonderadaNotasProcSeletivo().doubleValue());
					sqlInserir.setInt(7, obj.getResponsavel().getCodigo().intValue());
					sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataRegistro()));
					sqlInserir.setDouble(9, obj.getSomatorioAcertos());
					sqlInserir.setString(10, obj.getRespostaProvaProcessoSeletivo());
					if (obj.getNotaRedacao() != null) {
						sqlInserir.setDouble(11, obj.getNotaRedacao());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setString(12, obj.getRedacao());
					sqlInserir.setString(13, obj.getNavegadorAcesso());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			// sempre que se registra um resultado para uma inscrição então ela precisa ser refefinida como ativa. Pois,
			// a mesma poderia estar anteriormente como cancelada e/ou marcada como NAO_COMPARECEU
			getFacadeFactory().getInscricaoFacade().atualizarSituacaoInscricao(obj.getInscricao(), SituacaoInscricaoEnum.ATIVO, usuarioVO);
			getFacadeFactory().getResultadoDisciplinaProcSeletivoFacade().incluirResultadoDisciplinaProcSeletivos(obj.getCodigo(), obj.getResultadoDisciplinaProcSeletivoVOs(), usuarioVO);
			if (obj.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA") && Uteis.isAtributoPreenchido(obj.getInscricao().getGabaritoVO())) {
				getFacadeFactory().getInscricaoFacade().executarCriacaoVinculoInscricaoComGabarito(obj.getInscricao(), usuarioVO);
				getFacadeFactory().getResultadoProcessoSeletivoGabaritoRespostaFacade().incluirResultadoProcessoSeletivoGabaritoRespostaVOs(obj.getCodigo(), obj.getResultadoProcessoSeletivoGabaritoRespostaVOs(), usuarioVO);
			} else if (obj.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("PR") && Uteis.isAtributoPreenchido(obj.getInscricao().getProvaProcessoSeletivoVO())) {
				getFacadeFactory().getInscricaoFacade().executarCriacaoVinculoInscricaoComProvaProcessoSeletivo(obj.getInscricao(), usuarioVO);
				getFacadeFactory().getResultadoProcessoSeletivoProvaRespostaFacade().executarGeracaoResultadoProcessoSeletivoProvaRespostaVO(obj);
				getFacadeFactory().getResultadoProcessoSeletivoProvaRespostaFacade().incluirResultadoProcessoSeletivoProvaRespostaVOs(obj, usuarioVO);
			}
			if (!obj.getResultadoPrimeiraOpcao().equals("AG")) { 
				enviaMensagemResultadoProcessoSeletivo(obj);
				enviaMensagemAprovacaoProcessoSeletivo(obj);
				gerarCampanhaCRMInscricaoProcessoSeletivoLancamentoResultado(obj, usuarioVO);
			}
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void enviaMensagemResultadoProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivo) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select enviamensagemresultadoprocessoseletivo, itemprocseletivodataprova.dataliberacaoresultado, inscricao.unidadeensino as unidadeensino ");
		sql.append(" from resultadoprocessoseletivo ");
		sql.append(" inner join inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sql.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sql.append(" where resultadoprocessoseletivo.codigo = ").append(resultadoProcessoSeletivo.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			if (!tabelaResultado.getBoolean("enviamensagemresultadoprocessoseletivo") && tabelaResultado.getDate("dataliberacaoresultado").before(new Date())) {
				Integer unidadeEnsino = tabelaResultado.getInt("unidadeensino");
				unidadeEnsino = Uteis.isAtributoPreenchido(unidadeEnsino) ? unidadeEnsino : 0;
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemResultadoProcessoSeletivo(resultadoProcessoSeletivo, unidadeEnsino);
				alterarEnviaMensagemResultadoProcessoSeletivo(resultadoProcessoSeletivo.getCodigo());
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarEnviaMensagemResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" update resultadoprocessoseletivo set enviamensagemresultadoprocessoseletivo = true where codigo = ").append(resultadoProcessoSeletivo);
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void enviaMensagemAprovacaoProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivo) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select enviamensagemaprovacaoprocessoseletivo, itemprocseletivodataprova.dataliberacaoresultado ");
		sql.append(" from resultadoprocessoseletivo ");
		sql.append(" inner join inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sql.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sql.append(" where resultadoprocessoseletivo.codigo = ").append(resultadoProcessoSeletivo.getCodigo());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			if (!tabelaResultado.getBoolean("enviamensagemaprovacaoprocessoseletivo") && tabelaResultado.getDate("dataliberacaoresultado").before(new Date())) {
				if (resultadoProcessoSeletivo.getResultadoPrimeiraOpcao().equals("AP") || resultadoProcessoSeletivo.getResultadoSegundaOpcao().equals("AP") || resultadoProcessoSeletivo.getResultadoTerceiraOpcao().equals("AP")) {
					getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAprovacaoProcessoSeletivo(resultadoProcessoSeletivo, new UsuarioVO());
					alterarEnviaMensagemAprovacaoProcessoSeletivo(resultadoProcessoSeletivo.getCodigo());
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarEnviaMensagemAprovacaoProcessoSeletivo(Integer resultadoProcessoSeletivo) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" update resultadoprocessoseletivo set enviamensagemaprovacaoprocessoseletivo = true where codigo = ").append(resultadoProcessoSeletivo);
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	public List<ResultadoProcessoSeletivoVO> consultarResultadoProcessoSeletivoEnvioMensagemAprovacaoResultado() throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select inscricao.unidadeensino as \"inscricao.unidadeensino\", inscricao.codigo as \"inscricao.codigo\", resultadoprocessoseletivo.*, itemprocseletivodataprova.dataliberacaoresultado ");
		sql.append(" from resultadoprocessoseletivo ");
		sql.append(" inner join inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sql.append(" inner join itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sql.append(" where (itemprocseletivodataprova.dataliberacaoresultado::DATE <= current_date) ");
		sql.append(" and (enviamensagemaprovacaoprocessoseletivo = false or enviamensagemresultadoprocessoseletivo = false) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		List<ResultadoProcessoSeletivoVO> vetResultado = new ArrayList<ResultadoProcessoSeletivoVO>(0);
		ResultadoProcessoSeletivoVO obj = null;
		while (tabelaResultado.next()) {
			obj = new ResultadoProcessoSeletivoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getInscricao().setCodigo(tabelaResultado.getInt("inscricao.codigo"));
			obj.getInscricao().getUnidadeEnsino().setCodigo(tabelaResultado.getInt("inscricao.unidadeensino"));
			obj.getInscricao().getItemProcessoSeletivoDataProva().setDataLiberacaoResultado(tabelaResultado.getDate("dataliberacaoresultado"));
			obj.setResultadoPrimeiraOpcao(tabelaResultado.getString("resultadoPrimeiraOpcao"));
			obj.setResultadoSegundaOpcao(tabelaResultado.getString("resultadoSegundaOpcao"));
			obj.setResultadoTerceiraOpcao(tabelaResultado.getString("resultadoTerceiraOpcao"));
			obj.setRespostaProvaProcessoSeletivo(tabelaResultado.getString("respostaProvaProcessoSeletivo"));
			obj.setMediaNotasProcSeletivo(new Double(tabelaResultado.getDouble("mediaNotasProcSeletivo")));
			obj.setMediaPonderadaNotasProcSeletivo(new Double(tabelaResultado.getDouble("mediaPonderadaNotasProcSeletivo")));
			obj.setSomatorioAcertos(new Double(tabelaResultado.getDouble("somatorioAcertos")));
			obj.getResponsavel().setCodigo(new Integer(tabelaResultado.getInt("responsavel")));
			obj.setDataRegistro(tabelaResultado.getDate("dataRegistro"));
			obj.setNotaRedacao(tabelaResultado.getDouble("notaRedacao"));
			obj.setRedacao(tabelaResultado.getString("redacao"));
			obj.setEnviaMensagemAprovacaoProcessoseletivo(tabelaResultado.getBoolean("enviamensagemaprovacaoprocessoseletivo"));
			obj.setEnviaMensagemResultadoProcessoSeletivo(tabelaResultado.getBoolean("enviamensagemresultadoprocessoseletivo"));
			obj.setNovoObj(Boolean.FALSE);
			vetResultado.add(obj);
			obj = null;
		}

		return vetResultado;
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ResultadoProcessoSeletivoVO</code>. Sempre utiliza a chave
	 * primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoProcessoSeletivoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO, Boolean alterarResultadoProcessoSeletivoProvaResposta) throws Exception {
		try {
			ResultadoProcessoSeletivoVO.validarDados(obj);
			ResultadoProcessoSeletivo.alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE ResultadoProcessoSeletivo set inscricao=?, resultadoPrimeiraOpcao=?, resultadoSegundaOpcao=?, resultadoTerceiraOpcao=?, mediaNotasProcSeletivo=?, mediaPonderadaNotasProcSeletivo=?, responsavel=?, dataRegistro=?, somatorioAcertos=?, respostaProvaProcessoSeletivo = ?, notaRedacao=?, redacao=?  , navegadorAcesso=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getInscricao().getCodigo().intValue());
					sqlAlterar.setString(2, obj.getResultadoPrimeiraOpcao());
					sqlAlterar.setString(3, obj.getResultadoSegundaOpcao());
					sqlAlterar.setString(4, obj.getResultadoTerceiraOpcao());
					sqlAlterar.setDouble(5, obj.getMediaNotasProcSeletivo().doubleValue());
					sqlAlterar.setDouble(6, obj.getMediaPonderadaNotasProcSeletivo().doubleValue());
					sqlAlterar.setInt(7, obj.getResponsavel().getCodigo().intValue());
					sqlAlterar.setDate(8, Uteis.getDataJDBC(obj.getDataRegistro()));
					sqlAlterar.setDouble(9, obj.getSomatorioAcertos());
					sqlAlterar.setString(10, obj.getRespostaProvaProcessoSeletivo());
					if (obj.getNotaRedacao() != null) {
						sqlAlterar.setDouble(11, obj.getNotaRedacao());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setString(12, obj.getRedacao());
					sqlAlterar.setString(13, obj.getNavegadorAcesso());					
					sqlAlterar.setInt(14, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			})==0){
				incluir(obj, usuarioVO);
				return;
			}
			// sempre que se registra um resultado para uma inscrição então ela precisa ser refefinida como ativa. Pois,
			// a mesma poderia estar anteriormente como cancelada e/ou marcada como NAO_COMPARECEU
			getFacadeFactory().getInscricaoFacade().atualizarSituacaoInscricao(obj.getInscricao(), SituacaoInscricaoEnum.ATIVO, usuarioVO);

			getFacadeFactory().getResultadoDisciplinaProcSeletivoFacade().alterarResultadoDisciplinaProcSeletivos(obj.getCodigo(), obj.getResultadoDisciplinaProcSeletivoVOs(), usuarioVO);
			if (obj.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA") && Uteis.isAtributoPreenchido(obj.getInscricao().getGabaritoVO())) {
				getFacadeFactory().getInscricaoFacade().executarCriacaoVinculoInscricaoComGabarito(obj.getInscricao(), usuarioVO);
				getFacadeFactory().getResultadoProcessoSeletivoGabaritoRespostaFacade().alterarResultadoGabaritoRespostaVOs(obj.getCodigo(), obj.getResultadoProcessoSeletivoGabaritoRespostaVOs(), usuarioVO);
			} else if (obj.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("PR") && Uteis.isAtributoPreenchido(obj.getInscricao().getProvaProcessoSeletivoVO())) {
				getFacadeFactory().getInscricaoFacade().executarCriacaoVinculoInscricaoComProvaProcessoSeletivo(obj.getInscricao(), usuarioVO);
				if (alterarResultadoProcessoSeletivoProvaResposta) {
					getFacadeFactory().getResultadoProcessoSeletivoProvaRespostaFacade().alterarResultadoProcessoSeletivoProvaRespostaVOs(obj, usuarioVO);
				}
			}
			if (!obj.getResultadoPrimeiraOpcao().equals("AG")) { 
				enviaMensagemResultadoProcessoSeletivo(obj);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ResultadoProcessoSeletivoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ResultadoProcessoSeletivoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ResultadoProcessoSeletivo.excluir(getIdEntidade(), true, usuarioVO);
			getFacadeFactory().getResultadoDisciplinaProcSeletivoFacade().excluirResultadoDisciplinaProcSeletivos(obj.getCodigo(), usuarioVO);
			String sql = "DELETE FROM ResultadoProcessoSeletivo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
			sql = "update inscricao set gabarito =  null, provaprocessoseletivo = null, candidatoconvocadomatricula =  false, chamada =  null, classificacao = null WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getInscricao().getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ResultadoProcessoSeletivo</code> através do valor do atributo <code>Date dataRegistro</code>.
	 * Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza
	 * o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ResultadoProcessoSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ResultadoProcessoSeletivoVO> consultarPorDataRegistroComUnidadeEnsino(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT distinct ResultadoProcessoSeletivo.* FROM ResultadoProcessoSeletivo ");
		sb.append(" inner join Inscricao on ResultadoProcessoSeletivo.inscricao = Inscricao.codigo ");
		sb.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
		sb.append(" left  join procseletivounidadeensino on procseletivo.codigo = procseletivounidadeensino.procseletivo ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sb.append(" and  procseletivounidadeensino.unidadeensino =  ").append(unidadeEnsino);
		}
		sb.append(" WHERE ((dataRegistro >= '").append(Uteis.getDataJDBC(prmIni)).append("') and (dataRegistro <= '").append(Uteis.getDataJDBC(prmFim)).append("')) ");
		sb.append(" ORDER BY ResultadoProcessoSeletivo.dataRegistro");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ResultadoProcessoSeletivo</code> através do valor do atributo <code>codigo</code> da classe
	 * <code>Inscricao</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ResultadoProcessoSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ResultadoProcessoSeletivoVO> consultarPorCodigoInscricaoComUnidadeEnsino(Integer valorConsulta, Integer unidadeEnsino,boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT distinct ResultadoProcessoSeletivo.*, Inscricao.codigo FROM ResultadoProcessoSeletivo ");
		sb.append(" inner join Inscricao on ResultadoProcessoSeletivo.inscricao = Inscricao.codigo ");
		sb.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
		sb.append(" where  Inscricao.codigo = ").append(valorConsulta);
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sb.append(" and  inscricao.unidadeensino = ").append(unidadeEnsino);
		}
		sb.append(" ORDER BY Inscricao.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ResultadoProcessoSeletivoVO> consultarPorNomeCandidatoComUnidadeEnsino(String nomeCandidato, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT distinct ResultadoProcessoSeletivo.*, pessoa.nome FROM ResultadoProcessoSeletivo ");
		sb.append(" inner join Inscricao on ResultadoProcessoSeletivo.inscricao = Inscricao.codigo ");
		sb.append(" inner join procseletivo on procseletivo.codigo = Inscricao.procseletivo ");
		sb.append(" inner join pessoa on pessoa.codigo = inscricao.candidato "); 		
		sb.append(" where  sem_acentos(pessoa.nome) ilike sem_acentos(?) ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sb.append(" and  inscricao.unidadeensino = ").append(unidadeEnsino);
		}
		sb.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), nomeCandidato + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ResultadoProcessoSeletivo</code> através do valor do atributo <code>Integer codigo</code>.
	 * Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ResultadoProcessoSeletivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<ResultadoProcessoSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ResultadoProcessoSeletivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<ResultadoProcessoSeletivoVO> consultarPorCodigoECurso(Integer codigoProcesso, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Distinct ResultadoProcessoSeletivo.* FROM ResultadoProcessoSeletivo, Inscricao WHERE Inscricao.procSeletivo = " + codigoProcesso.intValue() + " AND " + "( (ResultadoProcessoSeletivo.resultadoPrimeiraOpcao = 'AP' AND Inscricao.cursoOpcao1 = " + codigoCurso.intValue() + ") OR" + "(ResultadoProcessoSeletivo.resultadoSegundaOpcao = 'AP' AND Inscricao.cursoOpcao2 = " + codigoCurso.intValue() + ") OR" + "(ResultadoProcessoSeletivo.resultadoTerceiraOpcao = 'AP' AND Inscricao.cursoOpcao3 = " + codigoCurso.intValue() + ") ) ORDER BY ResultadoProcessoSeletivo.mediaNotasProcSeletivo DESC";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ResultadoProcessoSeletivoVO</code> resultantes da consulta.
	 */
	public static List<ResultadoProcessoSeletivoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ResultadoProcessoSeletivoVO> vetResultado = new ArrayList<ResultadoProcessoSeletivoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ResultadoProcessoSeletivoVO</code>.
	 * 
	 * @return O objeto da classe <code>ResultadoProcessoSeletivoVO</code> com os dados devidamente montados.
	 */
	public static ResultadoProcessoSeletivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ResultadoProcessoSeletivoVO obj = new ResultadoProcessoSeletivoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getInscricao().setCodigo(new Integer(dadosSQL.getInt("inscricao")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosInscricao(obj, nivelMontarDados, usuario);
			return obj;
		}
		obj.setResultadoPrimeiraOpcao(dadosSQL.getString("resultadoPrimeiraOpcao"));
		obj.setResultadoSegundaOpcao(dadosSQL.getString("resultadoSegundaOpcao"));
		obj.setResultadoTerceiraOpcao(dadosSQL.getString("resultadoTerceiraOpcao"));
		obj.setRespostaProvaProcessoSeletivo(dadosSQL.getString("respostaProvaProcessoSeletivo"));
		obj.setMediaNotasProcSeletivo(new Double(dadosSQL.getDouble("mediaNotasProcSeletivo")));
		obj.setMediaPonderadaNotasProcSeletivo(new Double(dadosSQL.getDouble("mediaPonderadaNotasProcSeletivo")));
		obj.setSomatorioAcertos(new Double(dadosSQL.getDouble("somatorioAcertos")));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.setDataRegistro(dadosSQL.getDate("dataRegistro"));
		if (dadosSQL.getObject("notaRedacao") != null) {
			BigDecimal notaTemp;
			notaTemp = ((BigDecimal) dadosSQL.getObject("notaRedacao"));
			obj.setNotaRedacao(notaTemp.doubleValue());
		} else {
			obj.setNotaRedacao(null);
		}
		obj.setRedacao(dadosSQL.getString("redacao"));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosInscricao(obj, nivelMontarDados, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setResultadoDisciplinaProcSeletivoVOs(getFacadeFactory().getResultadoDisciplinaProcSeletivoFacade().consultarResultadoDisciplinaProcSeletivos(obj.getCodigo(), usuario));
		obj.setResultadoProcessoSeletivoGabaritoRespostaVOs(getFacadeFactory().getResultadoProcessoSeletivoGabaritoRespostaFacade().consultaRapidaPorResultadoProcessoSeletivo(obj.getCodigo(), usuario));
		obj.setResultadoProcessoSeletivoProvaRespostaVOs(getFacadeFactory().getResultadoProcessoSeletivoProvaRespostaFacade().consultarPorResultadoProcessoSeletivo(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>ResultadoProcessoSeletivoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(ResultadoProcessoSeletivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>InscricaoVO</code> relacionado ao objeto
	 * <code>ResultadoProcessoSeletivoVO</code>. Faz uso da chave primária da classe <code>InscricaoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosInscricao(ResultadoProcessoSeletivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getInscricao().getCodigo().intValue() == 0) {
			obj.setInscricao(new InscricaoVO());
			return;
		}
		obj.setInscricao(getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(obj.getInscricao().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ResultadoProcessoSeletivoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ResultadoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ResultadoProcessoSeletivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ResultadoProcessoSeletivoVO</code> através do código de sua inscrição
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ResultadoProcessoSeletivoVO consultarPorCodigoInscricao_ResultadoUnico(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM ResultadoProcessoSeletivo WHERE inscricao = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, valorConsulta.intValue());
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
			// throw new ConsistirException("Candidato não realizou prova ou ainda está em fase de apuração.");
		}
		return new ResultadoProcessoSeletivoVO();
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ResultadoProcessoSeletivoVO</code> através do código de sua inscrição
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ResultadoProcessoSeletivoVO consultarPorCPFCandidato_ResultadoUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT ResultadoProcessoSeletivo.* FROM ResultadoProcessoSeletivo , Inscricao, Pessoa WHERE  ResultadoProcessoSeletivo.inscricao =inscricao.codigo and inscricao.candidato = pessoa.codigo and upper (pessoa.cpf) like('" + valorConsulta.toLowerCase() + "%') ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return ResultadoProcessoSeletivo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ResultadoProcessoSeletivo.idEntidade = idEntidade;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public ResultadoProcessamentoArquivoRespostaVO realizarProcessamentoArquivoResposta(FileUploadEvent uploadEvent, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala, UsuarioVO usuarioVO) throws Exception {
		ResultadoProcessamentoArquivoRespostaVO resultado = new ResultadoProcessamentoArquivoRespostaVO();
		Map<Integer, String> inscricoes = null;
		Map<Integer, ProvaProcessoSeletivoVO> provas = new HashMap<Integer, ProvaProcessoSeletivoVO>(0);
		List<Integer> listaCodigoInscricaoArquivoVOs = new ArrayList<Integer>(0);
		try {
			inscricoes = realizarSeparacaoRespostaPorInscricao(uploadEvent);
			for (Integer inscricao : inscricoes.keySet()) {
				realizarProcessamentoRespostaPorInscricao(inscricao, inscricoes.get(inscricao), provas, resultado, procSeletivo, itemProcSeletivoDataProva, sala, usuarioVO);
				listaCodigoInscricaoArquivoVOs.add(inscricao);
			}
			for (ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO : resultado.getResultadoProcessoSeletivoVOs()) {
				if (resultadoProcessoSeletivoVO.isNovoObj()) {
					incluir(resultadoProcessoSeletivoVO, usuarioVO);
				} else {
					alterar(resultadoProcessoSeletivoVO, usuarioVO, true);
				}
			}
			resultado.setListaInscricaoNaoComparecidosVOs(getFacadeFactory().getInscricaoFacade().consultarInscricaoNaoCompareceuProcessamentoResultadoProcessoSeletivo(listaCodigoInscricaoArquivoVOs, procSeletivo, itemProcSeletivoDataProva, sala));
		} catch (Exception e) {
			throw e;
		} finally {
			uploadEvent = null;
			listaCodigoInscricaoArquivoVOs = null;
		}
		return resultado;
	}

	private void realizarProcessamentoRespostaPorInscricao(Integer inscricao, String respostas, Map<Integer, ProvaProcessoSeletivoVO> provas, ResultadoProcessamentoArquivoRespostaVO resultado, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala, UsuarioVO usuarioVO) throws Exception {
		ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = realizarInicializacaoDadosResultadoProcessoSeletivoPorInscricao(inscricao, procSeletivo, itemProcSeletivoDataProva, sala, usuarioVO);
		if (resultadoProcessoSeletivoVO != null) {
			// Usa Prova ou Gabarito			
			if (resultadoProcessoSeletivoVO.getApresentarAbaProvaProcessoSeletivo()) {
				executarCalculoAprovacaoCandidatoTipoProva(inscricao, respostas, provas, resultado, resultadoProcessoSeletivoVO, usuarioVO);
			}else if (resultadoProcessoSeletivoVO.getApresentarAbaGabarito()) {
				executarCalculoAprovacaoCandidatoTipoGabarito(inscricao, respostas, provas, resultado, resultadoProcessoSeletivoVO, usuarioVO);
			}else{
				resultado.getInscricaoRespostaNaoProcessadaVOs().add(new InscricaoRespostaNaoProcessadaVO(inscricao, "", "", MotivoNaoProcessamentoRespostaProcessoSeletivoEnum.GABARITO_NAO_LOCALIZADO));
			}
		} else {
			resultado.getInscricaoRespostaNaoProcessadaVOs().add(new InscricaoRespostaNaoProcessadaVO(inscricao, "", "", MotivoNaoProcessamentoRespostaProcessoSeletivoEnum.INSCRICAO_NAO_LOCALIZADA));
		}
	}

	public Map<Integer, Integer> realizarCorrecaoProvaPorGabarito(GabaritoVO gabaritoVO, ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, String respostas) {
		for (ColunaGabaritoVO colunaGabaritoVO : resultadoProcessoSeletivoVO.getColunaGabaritoVOs()) {
			for (ResultadoProcessoSeletivoGabaritoRespostaVO gabaritoRespostaVO : colunaGabaritoVO.getResultadoProcessoSeletivoGabaritoRespostaVOs()) {			
				gabaritoRespostaVO.setRespostaCorreta(respostas.substring(gabaritoRespostaVO.getNrQuestao() - 1, gabaritoRespostaVO.getNrQuestao()));
				gabaritoRespostaVO.setResultadoProcessoSeletivoVO(resultadoProcessoSeletivoVO);				
			}
		}				
		return null;
	}

	public void realizarCalculoAprovacaoCandidatoPorGabarito(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, Integer qtdeQuestao) {
		if(!Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getGrupoDisciplinaProcSeletivoVO())){			
			resultadoProcessoSeletivoVO.setMediaNotasProcSeletivo(Uteis.arrendondarForcando2CadasDecimais((resultadoProcessoSeletivoVO.getSomatorioAcertos() / resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getValorPorAcerto()) * resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getValorPorAcerto()));
		}
		resultadoProcessoSeletivoVO.setResultadoSegundaOpcao("RE");
		resultadoProcessoSeletivoVO.setResultadoTerceiraOpcao("RE");
		if (resultadoProcessoSeletivoVO.isAlgumaNotaAbaixoMinimo()) {
			resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
		} else {
			if (resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina")) {
				if (resultadoProcessoSeletivoVO.getMediaNotasProcSeletivo() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getMediaMinimaAprovacao()) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			} else if (resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertos")) {
				if (resultadoProcessoSeletivoVO.getSomatorioAcertos() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao()) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			} else {
				if (resultadoProcessoSeletivoVO.getSomatorioAcertos() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao() 
						&& (Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getNotaRedacao()) && resultadoProcessoSeletivoVO.getNotaRedacao() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getNotaMinimaRedacao())) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			}
		}
	}

	public void realizarCalculoAprovacaoCandidatoPorProva(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, Integer qtdeQuestao) {
		Double mediaFinal = 0.0;
		for (ResultadoDisciplinaProcSeletivoVO disciplina : resultadoProcessoSeletivoVO.getResultadoDisciplinaProcSeletivoVOs()) {
			mediaFinal = mediaFinal + disciplina.getNota();
		}
		mediaFinal = mediaFinal / resultadoProcessoSeletivoVO.getResultadoDisciplinaProcSeletivoVOs().size();
		resultadoProcessoSeletivoVO.setMediaNotasProcSeletivo(Uteis.arrendondarForcando2CadasDecimais(mediaFinal));
		resultadoProcessoSeletivoVO.setResultadoSegundaOpcao("RE");
		resultadoProcessoSeletivoVO.setResultadoTerceiraOpcao("RE");		
		if (resultadoProcessoSeletivoVO.isAlgumaNotaAbaixoMinimo()) {
			resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
		} else {
			if (resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina")) {
				if (resultadoProcessoSeletivoVO.getMediaNotasProcSeletivo() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getMediaMinimaAprovacao()) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			} else if (resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertos")) {
				if (resultadoProcessoSeletivoVO.getSomatorioAcertos() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao()) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			} else {
				if ((resultadoProcessoSeletivoVO.getSomatorioAcertos() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao()) && (Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getNotaRedacao()) && resultadoProcessoSeletivoVO.getNotaRedacao() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getNotaMinimaRedacao())) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			}
			if ((resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertosRedacao")) && resultadoProcessoSeletivoVO.getNotaRedacao() == null) {
				resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AG");
				resultadoProcessoSeletivoVO.setResultadoSegundaOpcao("AG");
				resultadoProcessoSeletivoVO.setResultadoTerceiraOpcao("AG");
			}
		}
	}

	private void realizarCalculoAprovacaoCandidato(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception {
		Double notaPorQuestao = 10.0 / (provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs().size());
		if(Double.isInfinite(notaPorQuestao)) {
			notaPorQuestao = 0.0;
		}
		for (ResultadoDisciplinaProcSeletivoVO disciplina : resultadoProcessoSeletivoVO.getResultadoDisciplinaProcSeletivoVOs()) {
			if(!disciplina.isEditavel()){
				disciplina.setNota(Uteis.arrendondarForcando2CadasDecimais(disciplina.getQuantidadeAcertos() * notaPorQuestao));
			}
		}
		resultadoProcessoSeletivoVO.setResultadoSegundaOpcao("RE");
		resultadoProcessoSeletivoVO.setResultadoTerceiraOpcao("RE");
		resultadoProcessoSeletivoVO.calcularMedias();
		if (resultadoProcessoSeletivoVO.isAlgumaNotaAbaixoMinimo()) {
			resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
		} else {
			if (resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina")) {
				if (resultadoProcessoSeletivoVO.getSomatorioAcertos() * notaPorQuestao >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getMediaMinimaAprovacao()) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			} else {
				if (resultadoProcessoSeletivoVO.getSomatorioAcertos() >= resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getQuantidadeAcertosMinimosAprovacao()) {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AP");
				} else {
					resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("RE");
				}
			}
		}
		if ((resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("quantidadeAcertosRedacao")) && resultadoProcessoSeletivoVO.getNotaRedacao() == null) {
			resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao("AG");
			resultadoProcessoSeletivoVO.setResultadoSegundaOpcao("AG");
			resultadoProcessoSeletivoVO.setResultadoTerceiraOpcao("AG");
		}

	}

	private void realizarDistribuicaoRespostaNasDisciplinas(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, Map<Integer, Integer> acertosPorDisciplina) {
		if (!resultadoProcessoSeletivoVO.getResultadoDisciplinaProcSeletivoVOs().isEmpty()) {
			resultadoProcessoSeletivoVO.setSomatorioAcertos(0.0);
			for (ResultadoDisciplinaProcSeletivoVO disciplina : resultadoProcessoSeletivoVO.getResultadoDisciplinaProcSeletivoVOs()) {
				if (acertosPorDisciplina.containsKey(disciplina.getDisciplinaProcSeletivo().getCodigo())) {
					disciplina.setQuantidadeAcertos(acertosPorDisciplina.get(disciplina.getDisciplinaProcSeletivo().getCodigo()));
					disciplina.setNota(disciplina.getQuantidadeAcertos()*resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getValorPorAcerto());
					resultadoProcessoSeletivoVO.setSomatorioAcertos(resultadoProcessoSeletivoVO.getSomatorioAcertos() + disciplina.getQuantidadeAcertos());
				} else if (disciplina.isEditavel()) {
					resultadoProcessoSeletivoVO.setSomatorioAcertos(resultadoProcessoSeletivoVO.getSomatorioAcertos() + disciplina.getQuantidadeAcertos());
					if(!resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina")){
						disciplina.setNota(disciplina.getQuantidadeAcertos()*resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getValorPorAcerto());
					}
				} else {
					disciplina.setQuantidadeAcertos(0);
					disciplina.setNota(0.0);
				}
			}
		}
	}

	private Map<Integer, Integer> realizarMarcacaoRespostaProvaPorDisciplina(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, String respostas) throws Exception {
		int numeroQuestao = 1;
		String opcaoMarcado = null;
		Map<Integer, Integer> acertosPorDisciplina = new HashMap<Integer, Integer>(0);
		for (QuestaoProvaProcessoSeletivoVO questao : resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
			if (respostas.length() >= numeroQuestao) {
				opcaoMarcado = respostas.substring(numeroQuestao - 1, numeroQuestao);
				questao.getQuestaoProcessoSeletivo().setAcertouQuestao(false);
				if (questao.getQuestaoProcessoSeletivo().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.CANCELADA)) {
					questao.getQuestaoProcessoSeletivo().setAcertouQuestao(true);
				} else {
					for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoResposta : questao.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
						if (opcaoResposta.getLetraCorrespondente().equalsIgnoreCase(opcaoMarcado)) {
							questao.getQuestaoProcessoSeletivo().setAcertouQuestao(opcaoResposta.getCorreta());
							/**
							 * Preenchido atributo setMarcada para que seja possível executar a geração do
							 * <code>ResultadoProcessoSeletivoProvaRespostaVO</code> no ato de inclusão de ResultadoProcessoSeletivo, no qual, é
							 * responsável por armazenar as respostas para uma possível alteração por meio da tela de Resultado do Processo Seletivo.
							 */
							opcaoResposta.setMarcada(true);
							break;
						}
					}
				}
				if (questao.getQuestaoProcessoSeletivo().getAcertouQuestao()) {
					if (acertosPorDisciplina.containsKey(questao.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo())) {
						acertosPorDisciplina.put(questao.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo(), acertosPorDisciplina.get(questao.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo()) + 1);
					} else {
						acertosPorDisciplina.put(questao.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo(), 1);
					}
				}
				numeroQuestao++;
			}
		}
		return acertosPorDisciplina;
	}

	private ResultadoProcessoSeletivoVO realizarInicializacaoDadosResultadoProcessoSeletivoPorInscricao(Integer inscricao, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala, UsuarioVO usuarioVO) throws Exception {
		ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
		resultadoProcessoSeletivoVO.setInscricao(getFacadeFactory().getInscricaoFacade().consultarDadosParaResultadoProcessoSeletivo(inscricao, procSeletivo, itemProcSeletivoDataProva, sala));
		if (resultadoProcessoSeletivoVO.getInscricao().getCodigo() == 0) {
			return null;
		}
		resultadoProcessoSeletivoVO = executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(resultadoProcessoSeletivoVO.getInscricao(), usuarioVO).getResultadoProcessoSeletivoVO();
		if (!Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO)) {			
			resultadoProcessoSeletivoVO.setDataRegistro(new Date());
			resultadoProcessoSeletivoVO.getResponsavel().setCodigo(usuarioVO.getCodigo());
			resultadoProcessoSeletivoVO.getResponsavel().setNome(usuarioVO.getNome());
		}
		return resultadoProcessoSeletivoVO;
	}

	@Override
	public ResultadoProcessoSeletivoVO consultarResultadoProcessoSeletivoPorCodigoInscricao(Integer valorConsulta, boolean controlarAcesso, Integer procSeletivo, Integer itemProcSeletivoDataProva, Integer sala, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM resultadoprocessoseletivo ");
		sqlStr.append(" INNER JOIN inscricao on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sqlStr.append(" INNER JOIN procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sqlStr.append(" left JOIN salalocalaula on salalocalaula.codigo = inscricao.sala ");
		sqlStr.append(" INNER JOIN itemprocseletivodataprova on itemprocseletivodataprova.codigo = inscricao.itemprocessoseletivodataprova ");
		sqlStr.append(" WHERE inscricao = ").append(valorConsulta);
		if (procSeletivo != null && !procSeletivo.equals(0)) {
			sqlStr.append(" AND procseletivo.codigo = ").append(procSeletivo);
		}
		if (itemProcSeletivoDataProva != null && !itemProcSeletivoDataProva.equals(0)) {
			sqlStr.append(" AND itemprocseletivodataprova.codigo = ").append(itemProcSeletivoDataProva);
		}
		if (sala != null && !sala.equals(0)) {
			sqlStr.append(" AND salalocalaula.codigo = ").append(sala);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	private Map<Integer, String> realizarSeparacaoRespostaPorInscricao(FileUploadEvent uploadEvent) throws Exception {
		Map<Integer, String> inscricoes = new HashMap<Integer, String>();
		BufferedReader reader = null;
		InputStreamReader fr = null;
		try {
			fr = new InputStreamReader(uploadEvent.getUploadedFile().getInputStream());
			reader = new BufferedReader(fr);
			String str;
			while (reader.ready()) {
				str = reader.readLine();
				if (str != null && !str.trim().isEmpty() && str.trim().length() >= 9) {
					inscricoes.put(Integer.valueOf(str.substring(0, 9)), str.substring(9, str.length()));
				}
			}
		} catch (Exception e) {
			throw new Exception("Falha na leitura do arquivo");
		} finally {
			if (fr != null) {
				fr.close();
			}

			if (reader != null) {
				reader.close();
			}
			uploadEvent = null;
			reader = null;
			fr = null;
		}
		return inscricoes;
	}

	/**
	 * Realiza o calculo da média do gabarito respondido manualmente
	 * 
	 * @param resultadoProcessoSeletivoVO
	 * @param gabaritoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void realizarCalculoMediaPorGabaritoLancadoManualmente(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, boolean validarDados, UsuarioVO usuarioVO) throws Exception {
		iniciarlizarDadosListaGabaritoResposta(resultadoProcessoSeletivoVO, usuarioVO);
		resultadoProcessoSeletivoVO.setSomatorioAcertos(0.0);
		resultadoProcessoSeletivoVO.setMediaNotasProcSeletivo(0.0);
		Map<Integer, Integer> acertosPorDisciplina = new HashMap<Integer, Integer>(0);
		if(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getGabaritoRespostaVOs().isEmpty() && Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO())){
			resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().setGabaritoRespostaVOs(getFacadeFactory().getGabaritoRespostaFacade().consultaRapidaPorGabarito(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getCodigo(), usuarioVO));
		}		
		for (GabaritoRespostaVO gabaritoRespostaVO : resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getGabaritoRespostaVOs()) {
			for (ResultadoProcessoSeletivoGabaritoRespostaVO respostaManualVO : resultadoProcessoSeletivoVO.getResultadoProcessoSeletivoGabaritoRespostaVOs()) {
				if(validarDados){
					getFacadeFactory().getResultadoProcessoSeletivoGabaritoRespostaFacade().validarDados(respostaManualVO);
				}
				if (!acertosPorDisciplina.containsKey(gabaritoRespostaVO.getDisciplinasProcSeletivoVO().getCodigo())) {
					acertosPorDisciplina.put(gabaritoRespostaVO.getDisciplinasProcSeletivoVO().getCodigo(), 0);
				}
				if (gabaritoRespostaVO.getNrQuestao().equals(respostaManualVO.getNrQuestao())) {
					if (gabaritoRespostaVO.getRespostaCorreta().equalsIgnoreCase(respostaManualVO.getRespostaCorreta()) || gabaritoRespostaVO.getAnulado()) {
						resultadoProcessoSeletivoVO.setSomatorioAcertos(resultadoProcessoSeletivoVO.getSomatorioAcertos() + 1);
						if (acertosPorDisciplina.containsKey(gabaritoRespostaVO.getDisciplinasProcSeletivoVO().getCodigo()) ) {
							acertosPorDisciplina.put(gabaritoRespostaVO.getDisciplinasProcSeletivoVO().getCodigo(), acertosPorDisciplina.get(gabaritoRespostaVO.getDisciplinasProcSeletivoVO().getCodigo()) + 1);
						} else {
							acertosPorDisciplina.put(gabaritoRespostaVO.getDisciplinasProcSeletivoVO().getCodigo(), 1);
						}
					}
					break;
				}
			}
		}

		/**
		 * Adicionada regra para que seja possível realizar o cálculo da nota por disciplina.
		 */
		if (resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getControlarGabaritoPorDisciplina()) {
			if(Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo()) && resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs().isEmpty()){
				resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			}
			if(!Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getGrupoDisciplinaProcSeletivoVO())){
				resultadoProcessoSeletivoVO.setGrupoDisciplinaProcSeletivoVO(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO().getGrupoDisciplinaProcSeletivoVO());
			}			
			realizarDistribuicaoRespostaNasDisciplinas(resultadoProcessoSeletivoVO, acertosPorDisciplina);			
			resultadoProcessoSeletivoVO.calcularMedias();
		}
		realizarCalculoAprovacaoCandidatoPorGabarito(resultadoProcessoSeletivoVO, resultadoProcessoSeletivoVO.getQuantidadeQuestao());
	}

	public void inicializarDadosGabaritoResposta(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getResultadoProcessoSeletivoGabaritoRespostaVOs().isEmpty()) {
			if (obj.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA")) {
				if (!Uteis.isAtributoPreenchido(obj.getInscricao().getGabaritoVO())) {
					obj.getInscricao().getGabaritoVO().setCodigo(getFacadeFactory().getGabaritoFacade().consultarCodigoGabaritoPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(obj.getInscricao().getCodigo()));
					obj.getInscricao().setGabaritoVO(getFacadeFactory().getGabaritoFacade().consultaRapidaPorChavePrimaria(obj.getInscricao().getGabaritoVO().getCodigo(), usuarioVO));
				}
				obj.setQuantidadeQuestao(obj.getInscricao().getGabaritoVO().getQuantidadeQuestao());
				// } else {
				// if (!Uteis.isAtributoPreenchido(obj.getInscricao().getGabaritoVO())) {
				// Integer prova =
				// getFacadeFactory().getProvaProcessoSeletivoFacade().consultarCodigoProvaPorInscricao(obj.getInscricao().getCodigo());
				// obj.getInscricao().setProvaProcessoSeletivoVO(getFacadeFactory().getProvaProcessoSeletivoFacade().consultarPorChavePrimaria(prova,
				// NivelMontarDados.TODOS));
				// }
				// obj.setQuantidadeQuestao(obj.getInscricao().getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs().size());
			}
			adicionarGabaritoResposta(obj, usuarioVO);
		} else {
			obj.setQuantidadeQuestao(obj.getInscricao().getGabaritoVO().getQuantidadeQuestao());
			editarColunaResultadoProcessoSeletivoGabaritoResposta(obj, usuarioVO);
		}
	}

	public void adicionarGabaritoResposta(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO) {
		obj.getResultadoProcessoSeletivoGabaritoRespostaVOs().clear();
		obj.getColunaGabaritoVOs().clear();
		int cont = 1;
		obj.setQtdeColuna(1);
		ColunaGabaritoVO colunaGabaritoVO = null;
		int qtdeColuna  = Uteis.arredondarParaMais(obj.getQuantidadeQuestao()/4);		
		for (int i = 1; i <= obj.getQuantidadeQuestao(); i++) {
			if (cont == 1) {
				colunaGabaritoVO = new ColunaGabaritoVO();
				colunaGabaritoVO.setColuna(obj.getQtdeColuna());
			}
			ResultadoProcessoSeletivoGabaritoRespostaVO gabaritoRespostaVO = new ResultadoProcessoSeletivoGabaritoRespostaVO();
			gabaritoRespostaVO.setNrQuestao(i);
			gabaritoRespostaVO.setRespostaCorreta("");
			colunaGabaritoVO.getResultadoProcessoSeletivoGabaritoRespostaVOs().add(gabaritoRespostaVO);
			if (cont == qtdeColuna || (i == obj.getQuantidadeQuestao())) {
				obj.setQtdeColuna(obj.getQtdeColuna() + 1);
				cont = 1;
				obj.getColunaGabaritoVOs().add(colunaGabaritoVO);
			} else {
				cont++;
			}
		}
	}

	public void iniciarlizarDadosListaGabaritoResposta(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO) {
		obj.getResultadoProcessoSeletivoGabaritoRespostaVOs().clear();
		for (ColunaGabaritoVO colunaGabaritoVO : obj.getColunaGabaritoVOs()) {
			for (ResultadoProcessoSeletivoGabaritoRespostaVO gabaritoRespostaVO : colunaGabaritoVO.getResultadoProcessoSeletivoGabaritoRespostaVOs()) {
				obj.getResultadoProcessoSeletivoGabaritoRespostaVOs().add(gabaritoRespostaVO);
			}
		}
		obj.setQuantidadeQuestao(obj.getResultadoProcessoSeletivoGabaritoRespostaVOs().size());
	}

	public void editarColunaResultadoProcessoSeletivoGabaritoResposta(ResultadoProcessoSeletivoVO obj, UsuarioVO usuarioVO) {
		obj.getColunaGabaritoVOs().clear();
		int cont = 1;		
		int qtdeQuestao = 1;
		obj.setQtdeColuna(1);
		ColunaGabaritoVO colunaGabaritoVO = null;
		int nrColuna =  Uteis.arredondarParaMais(obj.getQuantidadeQuestao()/4);
		for (ResultadoProcessoSeletivoGabaritoRespostaVO gabaritoRespostaVO : obj.getResultadoProcessoSeletivoGabaritoRespostaVOs()) {
			if (cont == 1) {
				colunaGabaritoVO = new ColunaGabaritoVO();
				colunaGabaritoVO.setColuna(obj.getQtdeColuna());
			}
			if (cont == nrColuna || qtdeQuestao == obj.getQuantidadeQuestao()) {
				obj.setQtdeColuna(obj.getQtdeColuna() + 1);
			
				colunaGabaritoVO.getResultadoProcessoSeletivoGabaritoRespostaVOs().add(gabaritoRespostaVO);
				obj.getColunaGabaritoVOs().add(colunaGabaritoVO);
				cont = 1;
			} else {
				colunaGabaritoVO.getResultadoProcessoSeletivoGabaritoRespostaVOs().add(gabaritoRespostaVO);
				cont++;
			}
			qtdeQuestao++;
		}
		if (obj.getColunaGabaritoVOs().isEmpty()) {
			obj.getColunaGabaritoVOs().add(colunaGabaritoVO);
		}
	}

	@Override
	public String consultarResultadoProcessoSeletivoDescritivoPorMatricula(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder(" select array_to_string(array( ");
		sql.append(" select disciplinasprocseletivo.nome||': '||quantidadeacertos::VARCHAR from resultadodisciplinaprocseletivo  ");
		sql.append(" inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = resultadodisciplinaprocseletivo.disciplinaprocseletivo  ");
		sql.append(" where resultadodisciplinaprocseletivo.resultadoprocessoseletivo = resultadoprocessoseletivo.codigo  ");
		sql.append(" ), ', ') || ' - Total Pontos:' ||  ");
		sql.append(" (case when procseletivo.regimeaprovacao = 'notaPorDisciplina' then medianotasprocseletivo else somatorioacertos end)::numeric(20,2) as disciplinasprocseletivo  ");
		sql.append(" from matricula   ");
		sql.append(" inner join inscricao on inscricao.codigo = matricula.inscricao ");
		sql.append(" inner join resultadoprocessoseletivo on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sql.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sql.append(" where formaingresso =  'PS' ");
		sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getString("disciplinasprocseletivo");
		}
		return "";
	}

	@Override
	public String consultarResultadoProcessoSeletivoDescritivoPorInscricao(Integer inscricao) throws Exception {
		StringBuilder sql = new StringBuilder(" select array_to_string(array( ");
		sql.append(" select disciplinasprocseletivo.nome || case when quantidadeacertos > 0 then ': '||quantidadeacertos::VARCHAR else '' end from resultadodisciplinaprocseletivo   ");
		sql.append(" inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = resultadodisciplinaprocseletivo.disciplinaprocseletivo  ");
		sql.append(" where resultadodisciplinaprocseletivo.resultadoprocessoseletivo = resultadoprocessoseletivo.codigo  ");
		sql.append(" ), ', ') || ' - Total Pontos:' || (case when procseletivo.regimeaprovacao = 'notaPorDisciplina' then medianotasprocseletivo else somatorioacertos end)::numeric(20,2) as disciplinasprocseletivo  ");
		sql.append(" from inscricao   ");
		sql.append(" inner join resultadoprocessoseletivo on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sql.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sql.append(" where inscricao.codigo = ").append(inscricao).append(" ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getString("disciplinasprocseletivo");
		}
		return "";
	}

	@Override
	public Double consultarPontosResultadoProcessoSeletivoPorInscricao(Integer inscricao) throws Exception {
		StringBuilder sql = new StringBuilder("select (case when procseletivo.regimeaprovacao = 'notaPorDisciplina' then medianotasprocseletivo when procseletivo.regimeaprovacao = 'quantidadeAcertosRedacao' then notaredacao else somatorioacertos end)::numeric(20,2) as nota ");
		sql.append("from inscricao ");
		sql.append("inner join resultadoprocessoseletivo on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sql.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sql.append("where inscricao.codigo = ").append(inscricao);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getDouble("nota");
		}
		return 0.0;
	}

	@Override
	public String consultarResultadoProcessoSeletivoDescritivoSemQtdeAcertosPorInscricao(Integer inscricao) throws Exception {
		StringBuilder sql = new StringBuilder(" select array_to_string(array( ");
		sql.append(" select disciplinasprocseletivo.nome from resultadodisciplinaprocseletivo ");
		sql.append(" inner join disciplinasprocseletivo on disciplinasprocseletivo.codigo = resultadodisciplinaprocseletivo.disciplinaprocseletivo ");
		sql.append(" where resultadodisciplinaprocseletivo.resultadoprocessoseletivo = resultadoprocessoseletivo.codigo ");
		sql.append(" ), ', ')|| ' - Total Pontos: '|| (case when procseletivo.regimeaprovacao = 'notaPorDisciplina' then medianotasprocseletivo else somatorioacertos end)::numeric(20,2) as disciplinasprocseletivo  ");
		sql.append(" from inscricao   ");
		sql.append(" inner join resultadoprocessoseletivo on inscricao.codigo = resultadoprocessoseletivo.inscricao ");
		sql.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sql.append(" where inscricao.codigo = ").append(inscricao).append(" ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getString("disciplinasprocseletivo");
		}
		return "";
	}

	public void gerarCampanhaCRMInscricaoProcessoSeletivoLancamentoResultado(final ResultadoProcessoSeletivoVO obj, UsuarioVO usuario) {
		try {
			getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().gerarAgendaCampanhaCRMInscricaoProcessoSeletivo(obj.getInscricao().getProcSeletivo().getCodigo(), obj.getInscricao(), PoliticaGerarAgendaEnum.GERAR_AO_LANCAR_RESULTADO_INSCRICAO, false, usuario);
		} catch (Exception e) {
			// Nao gera-se excecao pois este processo nao pode impedir o lancamento do resultado
		}
	}

	private void executarCalculoAprovacaoCandidatoTipoProva(Integer inscricao, String respostas, Map<Integer, ProvaProcessoSeletivoVO> provas, ResultadoProcessamentoArquivoRespostaVO resultado, ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception {
//		Integer codigoProva = getFacadeFactory().getProvaProcessoSeletivoFacade().consultarCodigoProvaPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(inscricao);
//		if (codigoProva != null && codigoProva > 0) {
//			ProvaProcessoSeletivoVO provaProcessoSeletivoVO = null;
//			if (provas.containsKey(codigoProva)) {
//				provaProcessoSeletivoVO = provas.get(codigoProva);
//			} else {
//				provaProcessoSeletivoVO = getFacadeFactory().getProvaProcessoSeletivoFacade().consultarPorChavePrimaria(codigoProva, NivelMontarDados.TODOS);
//				for (QuestaoProvaProcessoSeletivoVO questao : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
//					questao.getQuestaoProcessoSeletivo().setOpcaoRespostaQuestaoProcessoSeletivoVOs(getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().consultarPorQuestao(questao.getQuestaoProcessoSeletivo().getCodigo()));
//				}
//				provas.put(codigoProva, provaProcessoSeletivoVO);
//			}
//			resultadoProcessoSeletivoVO.getInscricao().setProvaProcessoSeletivoVO(provaProcessoSeletivoVO);
//			resultadoProcessoSeletivoVO.setGrupoDisciplinaProcSeletivoVO(provaProcessoSeletivoVO.getGrupoDisciplinaProcSeletivoVO());
			realizarMarcacaoRespostaProvaPorDisciplina(resultadoProcessoSeletivoVO, respostas);
			realizarCalculoMediaNotaLancadaManualmente(resultadoProcessoSeletivoVO, usuarioVO);			
			resultado.getResultadoProcessoSeletivoVOs().add(resultadoProcessoSeletivoVO);
//		} else {
//			resultado.getInscricaoRespostaNaoProcessadaVOs().add(new InscricaoRespostaNaoProcessadaVO(inscricao, resultadoProcessoSeletivoVO.getInscricao().getCandidato().getNome(), resultadoProcessoSeletivoVO.getInscricao().getOpcaoLinguaEstrangeira().getNome(), MotivoNaoProcessamentoRespostaProcessoSeletivoEnum.PROVA_NAO_LOCALIZADA));
//		}
	}

	private void executarCalculoAprovacaoCandidatoTipoGabarito(Integer inscricao, String respostas, Map<Integer, ProvaProcessoSeletivoVO> provas, ResultadoProcessamentoArquivoRespostaVO resultado, ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception {				
			realizarCorrecaoProvaPorGabarito(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO(), resultadoProcessoSeletivoVO, respostas);
			realizarCalculoMediaNotaLancadaManualmente(resultadoProcessoSeletivoVO, usuarioVO);
			resultado.getResultadoProcessoSeletivoVOs().add(resultadoProcessoSeletivoVO);		
	}

	/**
	 * Responsável por executar o cálculo de aprovação do candidato do tipo prova lançada manual.
	 * 
	 * @author Wellington - 16 de dez de 2015
	 * @param resultadoProcessoSeletivoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void executarCalculoAprovacaoCandidatoTipoProva(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception {
		realizarDistribuicaoRespostaNasDisciplinas(resultadoProcessoSeletivoVO, realizarMarcacaoRespostaProvaPorDisciplinaManual(resultadoProcessoSeletivoVO));
		resultadoProcessoSeletivoVO.calcularMedias();	  
		///realizarCalculoAprovacaoCandidato(resultadoProcessoSeletivoVO, resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO());
	}

	private Map<Integer, Integer> realizarMarcacaoRespostaProvaPorDisciplinaManual(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) throws Exception {
		Map<Integer, Integer> acertosPorDisciplina = new HashMap<Integer, Integer>(0);
		for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
			for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
				if (opcaoRespostaQuestaoProcessoSeletivoVO.getMarcada()) {
					boolean acertou = opcaoRespostaQuestaoProcessoSeletivoVO.getCorreta() || (questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getSituacaoQuestaoEnum() != null && questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.CANCELADA));
					questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setAcertouQuestao(acertou);
				}
			}
			if (questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getAcertouQuestao()) {
				if (acertosPorDisciplina.containsKey(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo())) {
					acertosPorDisciplina.put(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo(), acertosPorDisciplina.get(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo()) + 1);
				} else {
					acertosPorDisciplina.put(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo(), 1);
				}
			}
		}
		return acertosPorDisciplina;
	}

	/**
	 * Responsável por executar o carregamento dos dados do resultado do processo seletivo de acordo com as respostas marcadas.
	 * 
	 * @author Wellington - 21 de dez de 2015
	 * @param resultadoProcessoSeletivoVO
	 * @throws Exception
	 */
	@Override
	public void editarResultadoProcessoSeletivoProvaResposta(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) throws Exception {
		resultadoProcessoSeletivoVO.getInscricao().setProvaProcessoSeletivoVO(getFacadeFactory().getProvaProcessoSeletivoFacade().consultarPorChavePrimaria(resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getCodigo(), NivelMontarDados.TODOS));
		resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().setQuestaoProvaProcessoSeletivoVOs(getFacadeFactory().getQuestaoProvaProcessoSeletivoFacade().consultarPorProvaProcessoSeletivo(resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getCodigo(), NivelMontarDados.TODOS));
		resultadoProcessoSeletivoVO.setGrupoDisciplinaProcSeletivoVO(resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO());

		for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
			for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
				for (ResultadoProcessoSeletivoProvaRespostaVO provaRespostaVO : resultadoProcessoSeletivoVO.getResultadoProcessoSeletivoProvaRespostaVOs()) {
					if (opcaoRespostaQuestaoProcessoSeletivoVO.getCodigo().equals(provaRespostaVO.getOpcaoRespostaQuestaoProcessoSeletivoMarcadaVO().getCodigo())) {
						opcaoRespostaQuestaoProcessoSeletivoVO.setMarcada(true);
						break;
					}
				}
			}
		}
	}
	
	@Override
	public void executarVerificacaoHabilitarCampoNota(InscricaoVO inscricaoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(inscricaoVO.getGabaritoVO())) {
			q: for (ResultadoDisciplinaProcSeletivoVO rdpsVO : inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoDisciplinaProcSeletivoVOs()) {
				for (GabaritoRespostaVO grVO : inscricaoVO.getGabaritoVO().getGabaritoRespostaVOs()) {
					if (rdpsVO.getDisciplinaProcSeletivo().getCodigo().equals(grVO.getDisciplinasProcSeletivoVO().getCodigo())) {
						continue q;
					}
				}
				rdpsVO.setEditavel(true);
			}
		} else if (Uteis.isAtributoPreenchido(inscricaoVO.getProvaProcessoSeletivoVO())) {
			q: for (ResultadoDisciplinaProcSeletivoVO rdpsVO : inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoDisciplinaProcSeletivoVOs()) {
				for (QuestaoProvaProcessoSeletivoVO qppsVO : inscricaoVO.getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
					if (rdpsVO.getDisciplinaProcSeletivo().getCodigo().equals(qppsVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().getCodigo())) {
						continue q;
					}
				}
				rdpsVO.setEditavel(true);
			}
		}else{
			for (ResultadoDisciplinaProcSeletivoVO rdpsVO : inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoDisciplinaProcSeletivoVOs()) {
				rdpsVO.setEditavel(true);
			}
		}
	}
	
	@Override
	public List<ResultadoProcessoSeletivoVO> realizarGeracaoResultadoProcessoSeletivoLancamentoNotaPorDisciplina(final List<InscricaoVO> inscricaoVOs, final DisciplinasProcSeletivoVO disciplina, final UsuarioVO usuarioVO) throws Exception{
//		if(!Uteis.isAtributoPreenchido(disciplina.getCodigo())){
//			throw new Exception("O campo DISCIPLINA deve ser informado.");
//		}
		if(inscricaoVOs.isEmpty()){
			throw new Exception("Nenhuma inscrição possui esta disciplina para lançamento de nota, escolha outra disciplina.");
		}
		final List<ResultadoProcessoSeletivoVO> listaLancarNota =  new ArrayList<ResultadoProcessoSeletivoVO>(0);
		final ConsistirException consistirException = new ConsistirException(); 
		ProcessarParalelismo.executar(0, inscricaoVOs.size(), consistirException, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {
				try{
				InscricaoVO inscricaoVO = inscricaoVOs.get(i);
				if(inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO) &&
						inscricaoVO.getSituacao().equals("CO")){				
					List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs = inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs();
					if(disciplinasGrupoDisciplinaProcSeletivoVOs.isEmpty()){
						disciplinasGrupoDisciplinaProcSeletivoVOs = getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().consultarPorInscricaoCandidato(inscricaoVO.getCodigo(), false, usuarioVO);
						inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().setDisciplinasGrupoDisciplinaProcSeletivoVOs(disciplinasGrupoDisciplinaProcSeletivoVOs);
					}
						if (!Uteis.isAtributoPreenchido(disciplina.getCodigo())) {
							inscricaoVO = executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(inscricaoVO, usuarioVO);
							inscricaoVO.getResultadoProcessoSeletivoVO().setInscricao(inscricaoVO);
							inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoDisciplinaProcSeletivoVO(new ResultadoDisciplinaProcSeletivoVO());
						} else {
							for (DisciplinasGrupoDisciplinaProcSeletivoVO disciplinasGrupoDisciplinaProcSeletivoVO : disciplinasGrupoDisciplinaProcSeletivoVOs) {
								if (disciplinasGrupoDisciplinaProcSeletivoVO.getDisciplinasProcSeletivo().getCodigo().equals(disciplina.getCodigo())) {
									if (Uteis.isAtributoPreenchido(disciplina.getCodigo())) {
										realizarSelecaoResultadoPorDisciplina(inscricaoVO.getResultadoProcessoSeletivoVO(), disciplina, usuarioVO);
									} else {
										inscricaoVO.getResultadoProcessoSeletivoVO().setResultadoDisciplinaProcSeletivoVO(new ResultadoDisciplinaProcSeletivoVO());
									}
									break;
								}
							}
						}
				}
				}catch(Exception e){
					consistirException.adicionarListaMensagemErro(e.getMessage());
				}
				
			}
		});	
		for(InscricaoVO inscricaoVO: inscricaoVOs){
			if(inscricaoVO.getSituacaoInscricao().equals(SituacaoInscricaoEnum.ATIVO) &&
					inscricaoVO.getSituacao().equals("CO") && inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoDisciplinaProcSeletivoVO().getDisciplinaProcSeletivo().getCodigo().equals(disciplina.getCodigo())){
				listaLancarNota.add(inscricaoVO.getResultadoProcessoSeletivoVO());
			}
		}
		if(!consistirException.getListaMensagemErro().isEmpty()){
			throw consistirException;
		}
		if(listaLancarNota.isEmpty()){
			throw new Exception("Nenhuma inscrição possui esta disciplina para lançamento de nota, escolha outra disciplina.");
		}
		return listaLancarNota;
	}
	
	public void realizarSelecaoResultadoPorDisciplina(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, DisciplinasProcSeletivoVO disciplinasProcSeletivoVO, UsuarioVO usuarioVO){
		for(ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO: resultadoProcessoSeletivoVO.getResultadoDisciplinaProcSeletivoVOs()){
			if(resultadoDisciplinaProcSeletivoVO.getDisciplinaProcSeletivo().getCodigo().equals(disciplinasProcSeletivoVO.getCodigo())){
				resultadoProcessoSeletivoVO.setResultadoDisciplinaProcSeletivoVO(new ResultadoDisciplinaProcSeletivoVO());
				resultadoProcessoSeletivoVO.setResultadoDisciplinaProcSeletivoVO(resultadoDisciplinaProcSeletivoVO);
				return;
			}
		}
	}
	
	@Override
	public InscricaoVO executarMontagemDadosResultadoProcessoSeletivoPorInscricaoVO(InscricaoVO inscricaoVO, UsuarioVO usuarioVO) throws Exception{		
			if(!inscricaoVO.getNivelMontarDados().equals(NivelMontarDados.TODOS)){
				getFacadeFactory().getInscricaoFacade().carregarDados(inscricaoVO, usuarioVO);
				inscricaoVO.setResultadoProcessoSeletivoVO(consultarPorCodigoInscricao_ResultadoUnico(inscricaoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				inscricaoVO.setResultadoProcessoSeletivo(inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo());
				inscricaoVO.setNivelMontarDados(NivelMontarDados.TODOS);
			}
			
			
			if (!Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo())&& Uteis.isAtributoPreenchido(inscricaoVO.getProvaProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo())) {
				inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().setCodigo(inscricaoVO.getProvaProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo());
				inscricaoVO.getResultadoProcessoSeletivoVO().setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			}
			
			if (!Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO()) || inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs().isEmpty()) {
				inscricaoVO.getResultadoProcessoSeletivoVO().setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarGrupoDisciplinaPorProcessoSeletivoEUnidadeEnsinoCurso(inscricaoVO.getProcSeletivo().getCodigo(), inscricaoVO.getCursoOpcao1().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			}			
			if(Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getCodigo())){
			if (!inscricaoVO.getResultadoProcessoSeletivoVO().getResultadoDisciplinaProcSeletivoVOs().isEmpty()) {
				for (ResultadoDisciplinaProcSeletivoVO d : inscricaoVO.getResultadoProcessoSeletivoVO()
						.getResultadoDisciplinaProcSeletivoVOs()) {
					realizarPreenchimentoResultadoDeAcordoComGrupoDisciplinaConfigurado(inscricaoVO, d);
				}
			}else{
				inicializarDadosResultadoDisciplinaProcessoSeletivo(inscricaoVO, usuarioVO);
			}
			}else{
				inscricaoVO.getResultadoProcessoSeletivoVO().setDataRegistro(new Date());
				inscricaoVO.getResultadoProcessoSeletivoVO().getResponsavel().setCodigo(usuarioVO.getCodigo());
				inscricaoVO.getResultadoProcessoSeletivoVO().getResponsavel().setNome(usuarioVO.getNome());
				inicializarDadosResultadoDisciplinaProcessoSeletivo(inscricaoVO, usuarioVO);
			}
			executarMontagemDadosGabaritoOuProvaProcessoSeletivoInscricao(inscricaoVO, usuarioVO);
			getFacadeFactory().getResultadoProcessoSeletivoFacade().executarVerificacaoHabilitarCampoNota(inscricaoVO);
			ProvaProcessoSeletivoVO provaProcessoSeletivoVO = inscricaoVO.getResultadoProcessoSeletivoVO().getInscricao().getProvaProcessoSeletivoVO();
			inscricaoVO.getResultadoProcessoSeletivoVO().setInscricao(inscricaoVO);
			if (Uteis.isAtributoPreenchido(provaProcessoSeletivoVO)) {
				inscricaoVO.setProvaProcessoSeletivoVO(provaProcessoSeletivoVO);
			}
			return inscricaoVO;
	}
	
	@Override
	public void executarMontagemDadosGabaritoOuProvaProcessoSeletivoInscricao(InscricaoVO inscricaoVO, UsuarioVO usuarioVO) throws Exception {
		if (inscricaoVO.getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA")) {
			inscricaoVO.getGabaritoVO().setCodigo(getFacadeFactory().getGabaritoFacade().consultarCodigoGabaritoPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(inscricaoVO.getCodigo()));
			inscricaoVO.setGabaritoVO(getFacadeFactory().getGabaritoFacade().consultaRapidaPorChavePrimaria(inscricaoVO.getGabaritoVO().getCodigo(), usuarioVO));
			inscricaoVO.getResultadoProcessoSeletivoVO().getInscricao().setGabaritoVO(inscricaoVO.getGabaritoVO());
			inicializarDadosGabaritoResposta(inscricaoVO.getResultadoProcessoSeletivoVO(), usuarioVO);
		} else {
			if (!Uteis.isAtributoPreenchido(inscricaoVO.getProvaProcessoSeletivoVO()) && inscricaoVO.getFormaIngresso().equals(FormaIngresso.PROCESSO_SELETIVO.getValor())) {
				inscricaoVO.getProvaProcessoSeletivoVO().setCodigo(getFacadeFactory().getProvaProcessoSeletivoFacade().consultarCodigoProvaPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(inscricaoVO.getCodigo()));
			} else {
				if (Uteis.isAtributoPreenchido(inscricaoVO.getProvaProcessoSeletivoVO())) {
				inscricaoVO.setProvaProcessoSeletivoVO(getFacadeFactory().getProvaProcessoSeletivoFacade().consultarPorChavePrimaria(inscricaoVO.getProvaProcessoSeletivoVO().getCodigo(), NivelMontarDados.TODOS));
				inscricaoVO.getProvaProcessoSeletivoVO().setQuestaoProvaProcessoSeletivoVOs(getFacadeFactory().getQuestaoProvaProcessoSeletivoFacade().consultarPorProvaProcessoSeletivo(inscricaoVO.getProvaProcessoSeletivoVO().getCodigo(), NivelMontarDados.TODOS));
				inscricaoVO.getResultadoProcessoSeletivoVO().getInscricao().setProvaProcessoSeletivoVO(inscricaoVO.getProvaProcessoSeletivoVO());
				editarResultadoProcessoSeletivoProvaResposta(inscricaoVO.getResultadoProcessoSeletivoVO());
				}
			}
		}
	}
	
	public void inicializarDadosResultadoDisciplinaProcessoSeletivo(InscricaoVO inscricaoVO, UsuarioVO usuarioVO) throws Exception{
		List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs = inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs();
		if(disciplinasGrupoDisciplinaProcSeletivoVOs.isEmpty()){
			disciplinasGrupoDisciplinaProcSeletivoVOs = getFacadeFactory().getDisciplinasGrupoDisciplinaProcSeletivoFacade().consultarPorInscricaoCandidato(inscricaoVO.getCodigo(), false, usuarioVO);
			inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO().setDisciplinasGrupoDisciplinaProcSeletivoVOs(disciplinasGrupoDisciplinaProcSeletivoVOs);
		}
		for (DisciplinasGrupoDisciplinaProcSeletivoVO d : disciplinasGrupoDisciplinaProcSeletivoVOs) {
			if (!Uteis.isAtributoPreenchido(inscricaoVO.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO())) {
				inscricaoVO.getResultadoProcessoSeletivoVO().setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(d.getGrupoDisciplinaProcSeletivo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			}
			ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO = new ResultadoDisciplinaProcSeletivoVO();
			resultadoDisciplinaProcSeletivoVO.setDisciplinaProcSeletivo(d.getDisciplinasProcSeletivo());
			resultadoDisciplinaProcSeletivoVO.setVariavelNota(d.getVariavelNota());
			resultadoDisciplinaProcSeletivoVO.setNotaMinimaReprovadoImediato(d.getNotaMinimaReprovadoImediato());
			resultadoDisciplinaProcSeletivoVO.setOrdemCriterioDesempate(d.getOrdemCriterioDesempate());			
			realizarPreenchimentoResultadoDeAcordoComGrupoDisciplinaConfigurado(inscricaoVO, resultadoDisciplinaProcSeletivoVO);
			inscricaoVO.getResultadoProcessoSeletivoVO().adicionarObjResultadoDisciplinaProcSeletivoVOs(resultadoDisciplinaProcSeletivoVO, inscricaoVO);
		}
		
	}
	
	public void realizarPreenchimentoResultadoDeAcordoComGrupoDisciplinaConfigurado(InscricaoVO insc, ResultadoDisciplinaProcSeletivoVO resultado) {
		GrupoDisciplinaProcSeletivoVO grupo = insc.getResultadoProcessoSeletivoVO().getGrupoDisciplinaProcSeletivoVO();
		if (!Uteis.isAtributoPreenchido(grupo)) {
			for (ProcSeletivoUnidadeEnsinoVO pro : insc.getProcSeletivo().getProcSeletivoUnidadeEnsinoVOs()) {
				if (grupo == null) {
					for (ProcSeletivoCursoVO curso : pro.getProcSeletivoCursoVOs()) {
						if (curso.getUnidadeEnsinoCurso().getCodigo().equals(insc.getCursoOpcao1().getCodigo())) {
							grupo = curso.getGrupoDisciplinaProcSeletivo();
							break;
						}
					}
				}
			}
		}
		if (grupo != null) {
			insc.getResultadoProcessoSeletivoVO().setGrupoDisciplinaProcSeletivoVO(grupo);
			for (DisciplinasGrupoDisciplinaProcSeletivoVO discGrupo : grupo.getDisciplinasGrupoDisciplinaProcSeletivoVOs()) {
				if (discGrupo.getDisciplinasProcSeletivo().getCodigo().equals(resultado.getDisciplinaProcSeletivo().getCodigo())) {
					resultado.setVariavelNota(discGrupo.getVariavelNota());
					resultado.setNotaMinimaReprovadoImediato(discGrupo.getNotaMinimaReprovadoImediato());
					resultado.setOrdemCriterioDesempate(discGrupo.getOrdemCriterioDesempate());
				}
			}
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gravarLancamentoNotaPorDisciplina(List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs, UsuarioVO usuarioVO) throws Exception{
		for(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO: resultadoProcessoSeletivoVOs){
			realizarCalculoMediaNotaLancadaManualmente(resultadoProcessoSeletivoVO, usuarioVO);
			if(resultadoProcessoSeletivoVO.getApresentarAbaGabarito()){
				for(ResultadoProcessoSeletivoGabaritoRespostaVO resultadoProcessoSeletivoGabaritoRespostaVO: resultadoProcessoSeletivoVO.getResultadoProcessoSeletivoGabaritoRespostaVOs()){
					if(resultadoProcessoSeletivoGabaritoRespostaVO.getRespostaCorreta().trim().isEmpty()){
						resultadoProcessoSeletivoGabaritoRespostaVO.setRespostaCorreta("W");
					}
				}
			}
			persistir(resultadoProcessoSeletivoVO, usuarioVO, true);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuarioVO, Boolean alterarResultadoProcessoSeletivoProvaResposta) throws Exception{
		if(Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getCodigo())){
			alterar(resultadoProcessoSeletivoVO, usuarioVO, alterarResultadoProcessoSeletivoProvaResposta);
		}else{
			incluir(resultadoProcessoSeletivoVO, usuarioVO);
		}
	}

	public void realizarCalculoResultadoLancamentoNotaPorDisciplina(List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs, UsuarioVO usuarioVO) throws Exception{
		for(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO: resultadoProcessoSeletivoVOs){
			realizarCalculoMediaNotaLancadaManualmente(resultadoProcessoSeletivoVO, usuarioVO);
		}
	}

	/**
	 * Responsável por executar o cálculo da média, seja ela do tipo Gabarito, seja do tipo Prova, ao ser lançado a nota individualmente.
	 * 
	 * @author Rodrigo 06/10/2016 - migrado do controle para facade
	 */
	@Override
	public void realizarCalculoMediaNotaLancadaManualmente(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception {
		    boolean apresentarGabarito = resultadoProcessoSeletivoVO.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("GA") && Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getInscricao().getGabaritoVO());
		    boolean apresentarProva = resultadoProcessoSeletivoVO.getInscricao().getItemProcessoSeletivoDataProva().getTipoProvaGabarito().equals("PR") && Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO());
		    boolean apresentarDisciplina = !resultadoProcessoSeletivoVO.getResultadoDisciplinaProcSeletivoVOs().isEmpty();
			if (apresentarGabarito) {
				realizarCalculoMediaPorGabaritoLancadoManualmente(resultadoProcessoSeletivoVO, false, usuarioVO); 
			} else if (apresentarProva) {
				executarCalculoAprovacaoCandidatoTipoProva(resultadoProcessoSeletivoVO, usuarioVO);
			} else if(apresentarDisciplina){
				resultadoProcessoSeletivoVO.setSomatorioAcertos(0.0);
				resultadoProcessoSeletivoVO.calcularMedias();
			}else{
				if(!resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getRegimeAprovacao().equals("notaPorDisciplina")
						&&  Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO.getInscricao().getItemProcessoSeletivoDataProva().getCodigo())
						&&  !apresentarDisciplina
						&&  !apresentarProva
						&&  !apresentarGabarito){
					resultadoProcessoSeletivoVO.setMediaNotasProcSeletivo(resultadoProcessoSeletivoVO.getSomatorioAcertos()*resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getValorPorAcerto());
					resultadoProcessoSeletivoVO.setMediaPonderadaNotasProcSeletivo(resultadoProcessoSeletivoVO.getSomatorioAcertos()*resultadoProcessoSeletivoVO.getInscricao().getProcSeletivo().getValorPorAcerto());
				}
				resultadoProcessoSeletivoVO.realizarCalculoAprovacaoCandidatoPorProva();
			}						
	}
	
	@Override
	public void realizarSelecaoLancamentoNotaResultadoProcessoSeletivoPorDisciplina(final List<ResultadoProcessoSeletivoVO> resultadoProcessoSeletivoVOs, final DisciplinasProcSeletivoVO disciplina, final UsuarioVO usuarioVO) throws Exception{
	    for(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO: resultadoProcessoSeletivoVOs ){
	    	if(Uteis.isAtributoPreenchido(disciplina.getCodigo())){
	    		realizarSelecaoResultadoPorDisciplina(resultadoProcessoSeletivoVO, disciplina, usuarioVO);
	    	}else{
	    		resultadoProcessoSeletivoVO.setResultadoDisciplinaProcSeletivoVO(new ResultadoDisciplinaProcSeletivoVO());
	    	}
	    }	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarRedacaoProvaProcessoSeletivo(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO , UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE resultadoprocessoseletivo set redacao=? , navegadorAcesso=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, resultadoProcessoSeletivoVO.getRedacao());
				sqlAlterar.setString(2, resultadoProcessoSeletivoVO.getNavegadorAcesso());
				sqlAlterar.setInt(3, resultadoProcessoSeletivoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ResultadoProcessoSeletivoVO inicializarDadosResultadoImportarCandidatoInscricao(ImportarCandidatoInscricaoProcessoSeletivoVO importarCandidatoVO, InscricaoVO inscricaoVO, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(importarCandidatoVO.getProcSeletivoVO().getCodigo()) && Uteis.isAtributoPreenchido(inscricaoVO.getCodigo())) {
			ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = consultarResultadoProcessoSeletivoPorCodigoInscricao(inscricaoVO.getCodigo(), false, importarCandidatoVO.getProcSeletivoVO().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			if (Uteis.isAtributoPreenchido(resultadoProcessoSeletivoVO)) {
				return resultadoProcessoSeletivoVO;
			}
		}
		ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
		resultadoProcessoSeletivoVO.setInscricao(inscricaoVO);
		resultadoProcessoSeletivoVO.setResponsavel(usuario);
		resultadoProcessoSeletivoVO.setDataRegistro(importarCandidatoVO.getDataRegistro() != null ? importarCandidatoVO.getDataRegistro() : UteisData.obterDataFutura(importarCandidatoVO.getDataProva(), 2));
		resultadoProcessoSeletivoVO.setMediaNotasProcSeletivo(importarCandidatoVO.getNotaProcessoSeletivo());
		resultadoProcessoSeletivoVO.setResultadoPrimeiraOpcao(importarCandidatoVO.getResultadoProcessoSeletivo());
		return resultadoProcessoSeletivoVO;
	}
}
