/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.ConfiguracaoRankingVO;
import negocio.comuns.crm.PercentualConfiguracaoRankingVO;
import negocio.comuns.crm.enumerador.TagFormulaConfiguracaoRankingEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoConfiguracaoRankingEnum;
import negocio.comuns.crm.enumerador.tipoConsulta.TipoConsultaComboConfiguracaoRankingEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.ConfiguracaoRankingInterfaceFacade;

/**
 * 
 * @author Paulo Taucci
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoRanking extends ControleAcesso implements ConfiguracaoRankingInterfaceFacade {

	protected static String idEntidade;

	public ConfiguracaoRanking() throws Exception {
		super();
		setIdEntidade("ConfiguracaoRanking");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ConfiguracaoRankingVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoRankingVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			validarDados(obj);
			// validarUnicidadeUnidadeCursoTurma(obj);
			ConfiguracaoRanking.incluir(getIdEntidade(), true, usuarioLogado);
			realizarUpperCaseDados(obj);
			// final String sql =
			// "INSERT INTO ConfiguracaoRanking( unidadeEnsino, curso, turma, periodoInicial, periodoFinal, situacao, nome, percentualGerente ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			final String sql = "INSERT INTO ConfiguracaoRanking( situacao, nome, percentualGerente, formulaCalculoComissao, " +
					" considerarrankingcrmsomentematriculativo, desconsiderarrankingcrmalunobolsista, considerarrankingcrmprimeiramensalidade, " +
					" considerarcontratoassinadorankingcrm, qtdematriculaconsultorporturmaconsiderarrankingcrm, desconsiderarnovamatriculaaposxmoduloconcluidorankingcrm, " +
					" consideraralunoadimplentesemcontratoassinadorankingcrm, qtdeparcelaatrasadodesconsiderarsemcontratoassinadorankingcrm, descricaoregracomissionamentocrm," +
					" desconsiderarparcelaefaltaapartir3meses, desconsiderarrankingcrmsomentematriculpr, desconsiderarcontratonaoassinadoapartir4meses," +
					" unidadeensino, periodoInicial, periodoFinal" +
					" ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getSituacao().toString());
					sqlInserir.setString(2, obj.getNome().toString());
					sqlInserir.setDouble(3, obj.getPercentualGerente());
					sqlInserir.setString(4, obj.getFormulaCalculoComissao());
					sqlInserir.setBoolean(5, obj.getConsiderarRankingCrmSomenteMatriculAtivo());
					sqlInserir.setBoolean(6, obj.getDesconsiderarRankingCrmAlunoBolsista());
					sqlInserir.setBoolean(7, obj.getConsiderarRankingCrmPrimeiraMensalidade());
					sqlInserir.setBoolean(8, obj.getConsiderarContratoAssinadoRankingCrm());
					sqlInserir.setInt(9, obj.getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm());
					sqlInserir.setInt(10, obj.getDesconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm());
					sqlInserir.setBoolean(11, obj.getConsiderarAlunoAdimplenteSemContratoAssinadoRankingCrm());
					sqlInserir.setInt(12, obj.getQtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm());
					sqlInserir.setString(13, obj.getDescricaoRegraComissionamentoCRM());
					sqlInserir.setBoolean(14, obj.getDesconsiderarParcelaEFaltaApartir3Meses());
					sqlInserir.setBoolean(15, obj.getDesconsiderarrankingcrmsomentematriculpr());
					sqlInserir.setBoolean(16, obj.getDesconsiderarMatriculaContratoNaoAssinado4Meses());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(17, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlInserir.setNull(17, 0);
					}
					sqlInserir.setDate(18, Uteis.getDataJDBC(obj.getPeriodoInicial()));
					sqlInserir.setDate(19, Uteis.getDataJDBC(obj.getPeriodoFinal()));
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
			getFacadeFactory().getPercentualConfiguracaoRankingFacade().incluirPercentualConfiguracaoRankingVOs(obj.getCodigo(), obj.getPercentualVOs());

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ConfiguracaoRankingVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoRankingVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			validarDados(obj);
			ConfiguracaoRanking.alterar(getIdEntidade(), true, usuarioLogado);
			realizarUpperCaseDados(obj);
			// final String sql =
			// "UPDATE ConfiguracaoRanking set unidadeEnsino=?, curso=?, turma=?, periodoInicial=?, periodoFinal=?, situacao=?, nome=?, percentualGerente=? WHERE ((codigo = ?))";
			final String sql = "UPDATE ConfiguracaoRanking set situacao=?, nome=?, percentualGerente=?, formulaCalculoComissao = ?, " +
			
					" considerarrankingcrmsomentematriculativo=?, desconsiderarrankingcrmalunobolsista=?, considerarrankingcrmprimeiramensalidade=?, " +
					" considerarcontratoassinadorankingcrm=?, qtdematriculaconsultorporturmaconsiderarrankingcrm=?, desconsiderarnovamatriculaaposxmoduloconcluidorankingcrm=?, " +
					" consideraralunoadimplentesemcontratoassinadorankingcrm=?, qtdeparcelaatrasadodesconsiderarsemcontratoassinadorankingcrm=?, descricaoregracomissionamentocrm=?," +
					" desconsiderarparcelaefaltaapartir3meses=?, desconsiderarrankingcrmsomentematriculpr=?, desconsiderarcontratonaoassinadoapartir4meses=?, " +
					" unidadeensino=?, periodoInicial=?, periodoFinal=?" +
					" WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao().toString());
					sqlAlterar.setString(2, obj.getNome().toString());
					sqlAlterar.setDouble(3, obj.getPercentualGerente());
					sqlAlterar.setString(4, obj.getFormulaCalculoComissao());
					
					sqlAlterar.setBoolean(5, obj.getConsiderarRankingCrmSomenteMatriculAtivo());
					sqlAlterar.setBoolean(6, obj.getDesconsiderarRankingCrmAlunoBolsista());
					sqlAlterar.setBoolean(7, obj.getConsiderarRankingCrmPrimeiraMensalidade());
					
					sqlAlterar.setBoolean(8, obj.getConsiderarContratoAssinadoRankingCrm());
					sqlAlterar.setInt(9, obj.getQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm());
					sqlAlterar.setInt(10, obj.getDesconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm());
					
					sqlAlterar.setBoolean(11, obj.getConsiderarAlunoAdimplenteSemContratoAssinadoRankingCrm());
					sqlAlterar.setInt(12, obj.getQtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm());
					sqlAlterar.setString(13, obj.getDescricaoRegraComissionamentoCRM());
					
					sqlAlterar.setBoolean(14, obj.getDesconsiderarParcelaEFaltaApartir3Meses());
					sqlAlterar.setBoolean(15, obj.getDesconsiderarrankingcrmsomentematriculpr());
					sqlAlterar.setBoolean(16, obj.getDesconsiderarMatriculaContratoNaoAssinado4Meses());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(17, obj.getUnidadeEnsino().getCodigo());
					} else {
						sqlAlterar.setNull(17, 0);
					}
					sqlAlterar.setDate(18, Uteis.getDataJDBC(obj.getPeriodoInicial()));
					sqlAlterar.setDate(19, Uteis.getDataJDBC(obj.getPeriodoFinal()));
					
					sqlAlterar.setInt(20, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getPercentualConfiguracaoRankingFacade().alterarPercentualConfiguracaoRankingVOs(obj.getCodigo(), obj.getPercentualVOs(), usuarioLogado);

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ConfiguracaoRankingVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ConfiguracaoRankingVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
		try {
			ConfiguracaoRanking.excluir(getIdEntidade(), true, usuarioLogado);
			getFacadeFactory().getPercentualConfiguracaoRankingFacade().excluirPercentualConfiguracaoRankingVOs(obj.getCodigo(), usuarioLogado);
			String sql = "DELETE FROM ConfiguracaoRanking WHERE ((codigo = ?))" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param ConfiguracaoRankingVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoRankingVO obj, UsuarioVO usuarioLogado) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuarioLogado);
		} else {
			alterar(obj, usuarioLogado);
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ConfiguracaoRankingVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(ConfiguracaoRankingVO obj) throws Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}

		if (obj.getNome().trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_nome"));
		}
		if (obj.getFormulaCalculoComissao().trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_formulaCalculoComissao"));
		}

		ScriptEngineManager factory = new ScriptEngineManager();
		// create a JavaScript engine
		ScriptEngine engine = factory.getEngineByName("JavaScript");
		// evaluate JavaScript code from String
		try {
			String formula = obj.getFormulaCalculoComissao();
			for(TagFormulaConfiguracaoRankingEnum tag:TagFormulaConfiguracaoRankingEnum.values()){
	    		if(obj.getFormulaCalculoComissao().contains(tag.name())){
	    			formula = formula.replaceAll(tag.name(), "1");
	    		}
	    	}
			Object result = engine.eval(formula);	
			if (result instanceof Number) {
				result = ((Number) result).doubleValue();
			}
			if(!(result instanceof  Double)){
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_formulaCalculoInvalido"));
			}
		} catch (Exception e) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_formulaCalculoInvalido"));
		}

		// if (obj.getUnidadeEnsino() == null ||
		// obj.getUnidadeEnsino().getCodigo() == null ||
		// obj.getUnidadeEnsino().getCodigo() == 0) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_unidade"));
		// }
		//
		// if (obj.getPeriodoInicial() == null) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_periodoInicial"));
		// }
		//
		// if (obj.getPeriodoFinal() == null) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_periodoFinal"));
		// }

		if (obj.getPercentualVOs().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_percentual"));
		}

	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>ConfiguracaoRankingVO</code>.
	 */
	public void validarUnicidade(List<ConfiguracaoRankingVO> lista, ConfiguracaoRankingVO obj) throws ConsistirException {
		for (ConfiguracaoRankingVO repetido : lista) {
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados(ConfiguracaoRankingVO obj) {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis na Tela
	 * ConfiguracaoRankingCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public List<ConfiguracaoRankingVO> consultar(String valorConsulta, Date dataIni, Date dataFim, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {

		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.PERIODO.toString()))
		// {
		// return consultarPorPeriodo(dataIni, dataFim, controlarAcesso,
		// nivelMontarDados, limite, offset, usuario);
		// }
		//
		// if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
		// }
		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.UNIDADE.toString()))
		// {
		// return consultarPorUnidadeEnsino(valorConsulta, controlarAcesso,
		// nivelMontarDados, limite, offset, usuario);
		// }
		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.CURSO.toString()))
		// {
		// return consultarPorCurso(valorConsulta, controlarAcesso,
		// nivelMontarDados, limite, offset, usuario);
		// }
		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.TURMA.toString()))
		// {
		// return consultarPorTurma(valorConsulta, controlarAcesso,
		// nivelMontarDados, limite, offset, usuario);
		// }
		if (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.SITUACAO.toString())) {
			return consultarPorSituacao(valorConsulta, controlarAcesso, nivelMontarDados, limite, offset, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.NOME.toString())) {
			return consultarPorNome(valorConsulta, controlarAcesso, nivelMontarDados, limite, offset, usuario);
		}
		return new ArrayList(0);
	}

	public Integer consultarTotalRegistrosEncontrados(String valorConsulta, Date dataIni, Date dataFim, String campoConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.PERIODO.toString()))
		// {
		// return consultarTotalRegistrosEncontradosPorPeriodo(dataIni, dataFim,
		// controlarAcesso, nivelMontarDados, usuario);
		// }
		//
		// if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
		// throw new
		// Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsulta"));
		// }
		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.UNIDADE.toString()))
		// {
		// return
		// consultarTotalRegistrosEncontradosPorUnidadeEnsino(valorConsulta,
		// controlarAcesso, nivelMontarDados, usuario);
		// }
		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.CURSO.toString()))
		// {
		// return consultarTotalRegistrosEncontradosPorCurso(valorConsulta,
		// controlarAcesso, nivelMontarDados, usuario);
		// }
		// if
		// (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.TURMA.toString()))
		// {
		// return consultarTotalRegistrosEncontradosPorTurma(valorConsulta,
		// controlarAcesso, nivelMontarDados, usuario);
		// }
		if (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.SITUACAO.toString())) {
			return consultarTotalRegistrosEncontradosPorSituacao(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboConfiguracaoRankingEnum.NOME.toString())) {
			return consultarTotalRegistrosEncontradosPorNome(valorConsulta, controlarAcesso, nivelMontarDados, usuario);
		}
		return 0;
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoRanking</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoRankingVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT cr.* FROM ConfiguracaoRanking cr INNER JOIN UnidadeEnsino ue ON ue.codigo = cr.unidadeEnsino WHERE upper(ue.nome) like ? ORDER BY ue.nome";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	public Integer consultarTotalRegistrosEncontradosPorUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT COUNT(cr.codigo) FROM ConfiguracaoRanking cr INNER JOIN UnidadeEnsino ue ON ue.codigo = cr.unidadeEnsino WHERE upper(ue.nome) like ?";

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoRanking</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoRankingVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT cr.* FROM ConfiguracaoRanking cr INNER JOIN Curso c ON c.codigo = cr.curso WHERE upper(c.nome) like ? ORDER BY c.nome";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	public Integer consultarTotalRegistrosEncontradosPorCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT COUNT(cr.codigo) FROM ConfiguracaoRanking cr INNER JOIN Curso c ON c.codigo = cr.curso WHERE upper(c.nome) like ?";

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoRanking</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoRankingVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT cr.* FROM ConfiguracaoRanking cr INNER JOIN Turma t ON t.codigo = cr.turma WHERE upper(t.identificadorTurma) like ? ORDER BY t.identificadorTurma";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	public Integer consultarTotalRegistrosEncontradosPorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT COUNT(cr.*) FROM ConfiguracaoRanking cr INNER JOIN Turma t ON t.codigo = cr.turma WHERE upper(t.identificadorTurma) like ? ";

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoRanking</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoRankingVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT * FROM ConfiguracaoRanking WHERE upper( situacao ) like(?) ORDER BY situacao";
		if (limite != null && limite > 0) {
			sqlStr += " limit " + limite + " offset " + offset;
		}
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT * FROM ConfiguracaoRanking WHERE upper(sem_acentos(nome) ) like(sem_acentos( ? )) ORDER BY situacao";
		if (limite != null && limite > 0) {
			sqlStr += " limit " + limite + " offset " + offset;
		}
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	public Integer consultarTotalRegistrosEncontradosPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT COUNT(codigo) as qtde FROM ConfiguracaoRanking WHERE upper( situacao ) like (?)";

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		if (resultado.next()) {
			return resultado.getInt("qtde");
		}
		return 0;
	}

	public Integer consultarTotalRegistrosEncontradosPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT COUNT(codigo) as qtde FROM ConfiguracaoRanking WHERE upper(sem_acentos(nome) ) like(sem_acentos( ? ))";

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase());
		if (resultado.next()) {
			return resultado.getInt("qtde");
		}
		return 0;
	}

	public List consultarPorPeriodo(Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoRanking WHERE periodoInicial >= ? AND periodoFinal <= ? ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, Uteis.getDataJDBC(dataIni), Uteis.getDataJDBC(dataFim)), nivelMontarDados, usuario));
	}

	public Integer consultarTotalRegistrosEncontradosPorPeriodo(Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT COUNT(codigo) FROM ConfiguracaoRanking WHERE periodoInicial >= ? AND periodoFinal <= ?";

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, Uteis.getDataJDBC(dataIni), Uteis.getDataJDBC(dataFim));
		if (resultado.next()) {
			return resultado.getInt("count");
		}
		return 0;
	}

	/**
	 * Responsável por realizar uma consulta de <code>ConfiguracaoRanking</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoRankingVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, Integer limite, Integer offset, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ConfiguracaoRanking WHERE codigo >= ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ConfiguracaoRankingVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ConfiguracaoRankingVO</code>.
	 * 
	 * @return O objeto da classe <code>ConfiguracaoRankingVO</code> com os
	 *         dados devidamente montados.
	 */
	public static ConfiguracaoRankingVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ConfiguracaoRankingVO obj = new ConfiguracaoRankingVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setFormulaCalculoComissao(dadosSQL.getString("formulaCalculoComissao"));
		obj.setSituacao(TipoSituacaoConfiguracaoRankingEnum.valueOf(dadosSQL.getString("situacao")));
		obj.setPercentualGerente(dadosSQL.getDouble("percentualGerente"));
		obj.setConsiderarRankingCrmSomenteMatriculAtivo(dadosSQL.getBoolean("considerarRankingCrmSomenteMatriculAtivo"));
		obj.setDesconsiderarRankingCrmAlunoBolsista(dadosSQL.getBoolean("desconsiderarRankingCrmAlunoBolsista"));
		obj.setConsiderarRankingCrmPrimeiraMensalidade(dadosSQL.getBoolean("considerarRankingCrmPrimeiraMensalidade"));
		obj.setConsiderarContratoAssinadoRankingCrm(dadosSQL.getBoolean("considerarContratoAssinadoRankingCrm"));
		obj.setQtdeMatriculaConsultorPorTurmaConsiderarRankingCrm(dadosSQL.getInt("qtdeMatriculaConsultorPorTurmaConsiderarRankingCrm"));
		obj.setDesconsiderarMatriculaContratoNaoAssinado4Meses(dadosSQL.getBoolean("desconsiderarContratoNaoAssinadoApartir4Meses"));
		obj.setDesconsiderarParcelaEFaltaApartir3Meses(dadosSQL.getBoolean("desconsiderarParcelaEFaltaApartir3Meses"));
		obj.setDesconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm(dadosSQL.getInt("desconsiderarNovaMatriculaAposXModuloConcluidoRankingCrm"));
		obj.setConsiderarAlunoAdimplenteSemContratoAssinadoRankingCrm(dadosSQL.getBoolean("considerarAlunoAdimplenteSemContratoAssinadoRankingCrm"));
		obj.setDesconsiderarrankingcrmsomentematriculpr(dadosSQL.getBoolean("desconsiderarrankingcrmsomentematriculpr"));
		obj.setQtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm(dadosSQL.getInt("qtdeParcelaAtrasadoDesconsiderarSemContratoAssinadoRankingCrm"));
        obj.setDescricaoRegraComissionamentoCRM(dadosSQL.getString("descricaoRegraComissionamentoCRM"));
		
		obj.setNovoObj(new Boolean(false));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setPercentualVOs(getFacadeFactory().getPercentualConfiguracaoRankingFacade().consultarPorConfiguracaoRanking(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ConfiguracaoRankingVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public ConfiguracaoRankingVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ConfiguracaoRanking WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });

		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ConfiguracaoRankingVO> consultarUnicidade(ConfiguracaoRankingVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
		super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
		return new ArrayList(0);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoRanking.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoRanking.idEntidade = idEntidade;
	}

	public void validarUnicidadeUnidadeCursoTurma(ConfiguracaoRankingVO configuracaoRanking) throws Exception {
		if (configuracaoRanking.getTurma().getCodigo() != null && configuracaoRanking.getTurma().getCodigo() != 0) {
			if (consultarSeExisteParaTurma(configuracaoRanking.getTurma().getCodigo(), configuracaoRanking.getPeriodoInicial(), configuracaoRanking.getPeriodoFinal(), false, null)) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_rankingJaExistenteParaTurma"));
			}
		}
		if (configuracaoRanking.getCurso().getCodigo() != null && configuracaoRanking.getCurso().getCodigo() != 0) {
			if (consultarSeExisteParaCurso(configuracaoRanking.getCurso().getCodigo(), configuracaoRanking.getTurma().getCodigo(), configuracaoRanking.getPeriodoInicial(), configuracaoRanking.getPeriodoFinal(), false, null)) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_rankingJaExistenteParaCurso"));
			}
		}
		if (configuracaoRanking.getUnidadeEnsino().getCodigo() != null && configuracaoRanking.getUnidadeEnsino().getCodigo() != 0) {
			if (consultarSeExisteParaUnidadeEnsino(configuracaoRanking.getUnidadeEnsino().getCodigo(), configuracaoRanking.getCurso().getCodigo(), configuracaoRanking.getTurma().getCodigo(), configuracaoRanking.getPeriodoInicial(), configuracaoRanking.getPeriodoFinal(), false, null)) {
				throw new Exception(UteisJSF.internacionalizar("msg_ConfiguracaoRanking_rankingJaExistenteParaUnidadeEnsino"));
			}
		}
	}

	private boolean consultarSeExisteParaUnidadeEnsino(Integer unidadeEnsino, Integer curso, Integer turma, Date dataInicial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = null;
		SqlRowSet resultado = null;
		if ((turma != null && turma != 0) && (curso != null && curso != 0)) {
			sqlStr = "SELECT COUNT(codigo) FROM ConfiguracaoRanking WHERE unidadeEnsino = ? and curso = ? and turma = ? and periodoInicial <= ? and periodoFinal >= ? and situacao <> 'INATIVO'";
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, unidadeEnsino, curso, turma, Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal));
		} else if ((turma == null || turma == 0) && (curso != null && curso != 0)) {
			sqlStr = "SELECT COUNT(codigo) FROM ConfiguracaoRanking WHERE unidadeEnsino = ? and curso = ? and turma is null and periodoInicial <= ? and periodoFinal >= ? and situacao <> 'INATIVO'";
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, unidadeEnsino, curso, Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal));
		} else {
			sqlStr = "SELECT COUNT(codigo) FROM ConfiguracaoRanking WHERE unidadeEnsino = ? and curso is null and turma is null and periodoInicial <= ? and periodoFinal >= ? and situacao <> 'INATIVO'";
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, unidadeEnsino, Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal));
		}

		if (resultado.next()) {
			return resultado.getInt("count") > 0;
		}
		return false;
	}

	private boolean consultarSeExisteParaCurso(Integer curso, Integer turma, Date dataInicial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = null;
		SqlRowSet resultado = null;
		if (turma == null || turma == 0) {
			sqlStr = "SELECT COUNT(codigo) FROM ConfiguracaoRanking WHERE curso = ? and turma is null and periodoInicial <= ? and periodoFinal >= ? and situacao <> 'INATIVO'";
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, curso, Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal));
		} else {
			sqlStr = "SELECT COUNT(codigo) FROM ConfiguracaoRanking WHERE curso = ? and turma = ? and periodoInicial <= ? and periodoFinal >= ? and situacao <> 'INATIVO'";
			resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, curso, turma, Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal));
		}

		if (resultado.next()) {
			return resultado.getInt("count") > 0;
		}
		return false;
	}

	private boolean consultarSeExisteParaTurma(Integer turma, Date dataInicial, Date dataFinal, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT COUNT(codigo) FROM ConfiguracaoRanking WHERE turma = ? and periodoInicial <= ? and periodoFinal >= ? and situacao <> 'INATIVO'";

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, turma, Uteis.getDataJDBC(dataInicial), Uteis.getDataJDBC(dataFinal));
		if (resultado.next()) {
			return resultado.getInt("count") > 0;
		}
		return false;
	}

	public void removerPercentual(List<PercentualConfiguracaoRankingVO> listaPercentual, PercentualConfiguracaoRankingVO obj) throws Exception {
		int index = 0;
		Iterator i = listaPercentual.iterator();
		while (i.hasNext()) {
			PercentualConfiguracaoRankingVO objExistente = (PercentualConfiguracaoRankingVO) i.next();
			if (objExistente.getPosicao().equals(obj.getPosicao()) && objExistente.getPercentual().equals(obj.getPercentual())) {
				listaPercentual.remove(index);
				return;
			}
			index++;
		}
	}

	public void adicionarPercentual(List<PercentualConfiguracaoRankingVO> listaPercentual, PercentualConfiguracaoRankingVO obj) throws Exception {
		int index = 0;
		Iterator i = listaPercentual.iterator();
		while (i.hasNext()) {
			PercentualConfiguracaoRankingVO objExistente = (PercentualConfiguracaoRankingVO) i.next();
			if (objExistente.getPosicao().equals(obj.getPosicao()) && objExistente.getQtdePosicao().equals(obj.getQtdePosicao())) {
				listaPercentual.set(index, obj);
				return;
			}
			index++;
		}
		listaPercentual.add(obj);
	}

	public List<ConfiguracaoRankingVO> consultaRapidaNivelComboBox(Integer unidadeEnsino, String situacao, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT codigo, nome FROM configuracaoRanking ");
		sb.append("WHERE situacao = '").append(situacao.toUpperCase()).append("' ");
		if (!unidadeEnsino.equals(0)) {
			sb.append(" AND (unidadeEnsino = ");
			sb.append(unidadeEnsino);
			sb.append(" OR unidadeEnsino is null) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			ConfiguracaoRankingVO obj = new ConfiguracaoRankingVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
}
