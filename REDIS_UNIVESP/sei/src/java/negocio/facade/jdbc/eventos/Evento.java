package negocio.facade.jdbc.eventos;

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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.EventoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>EventoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>EventoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see EventoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class Evento extends ControleAcesso implements EventoInterfaceFacade {

	protected static String idEntidade;

	public Evento() throws Exception {
		super();
		setIdEntidade("Evento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>EventoVO</code>.
	 */
	public EventoVO novo() throws Exception {
		Evento.incluir(getIdEntidade());
		EventoVO obj = new EventoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>EventoVO</code>. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>EventoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EventoVO obj) throws Exception {
		try {
			Evento.incluir(getIdEntidade());
			EventoVO.validarDados(obj);
			final String sql = "INSERT INTO Evento( nome, dataInicioRealizacao, dataFinalRealizacao, dataInicioInscricao, dataFinalInscricao, valorAluno, valorProfessor, valorFuncionario, valorComunidade, localEvento, responsavel, curso, unidadeEnsino, site, emailSuporte, telefoneSuporte, tipoInscricao, nrVagas, nrMaximoVagasExcedentes, formaInscricao, localInscricao, aceitarSubmissaoTrabalho, regrasFormatacaoTrabalho, dataInicialSubmissao, dataFinalSubmissao, tipoSubmissao, valorSubmissaoAluno, valorSubmissaoProfessor, valorSubmissaoFuncionario, valorSubmissaoComunidade, ocultarAutorComissaoAvaliadora, exigirParecerComissaoAvaliadora, pagamentoPorAutorSubmissao, situacaoFinanceira ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataInicioRealizacao()));
					sqlInserir.setDate(3, Uteis.getDataJDBC(obj.getDataFinalRealizacao()));
					sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataInicioInscricao()));
					sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataFinalInscricao()));
					sqlInserir.setDouble(6, obj.getValorAluno().doubleValue());
					sqlInserir.setDouble(7, obj.getValorProfessor().doubleValue());
					sqlInserir.setDouble(8, obj.getValorFuncionario().doubleValue());
					sqlInserir.setDouble(9, obj.getValorComunidade().doubleValue());
					sqlInserir.setString(10, obj.getLocalEvento());
					sqlInserir.setInt(11, obj.getResponsavel().getCodigo().intValue());
					sqlInserir.setInt(12, obj.getCurso().getCodigo().intValue());
					sqlInserir.setInt(13, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlInserir.setString(14, obj.getSite());
					sqlInserir.setString(15, obj.getEmailSuporte());
					sqlInserir.setString(16, obj.getTelefoneSuporte());
					sqlInserir.setString(17, obj.getTipoInscricao());
					sqlInserir.setInt(18, obj.getNrVagas().intValue());
					sqlInserir.setInt(19, obj.getNrMaximoVagasExcedentes().intValue());
					sqlInserir.setString(20, obj.getFormaInscricao());
					sqlInserir.setString(21, obj.getLocalInscricao());
					sqlInserir.setBoolean(22, obj.isAceitarSubmissaoTrabalho().booleanValue());
					sqlInserir.setString(23, obj.getRegrasFormatacaoTrabalho());
					sqlInserir.setDate(24, Uteis.getDataJDBC(obj.getDataInicialSubmissao()));
					sqlInserir.setDate(25, Uteis.getDataJDBC(obj.getDataFinalSubmissao()));
					sqlInserir.setString(26, obj.getTipoSubmissao());
					sqlInserir.setDouble(27, obj.getValorSubmissaoAluno().doubleValue());
					sqlInserir.setDouble(28, obj.getValorSubmissaoProfessor().doubleValue());
					sqlInserir.setDouble(29, obj.getValorSubmissaoFuncionario().doubleValue());
					sqlInserir.setDouble(30, obj.getValorSubmissaoComunidade().doubleValue());
					sqlInserir.setBoolean(31, obj.isOcultarAutorComissaoAvaliadora().booleanValue());
					sqlInserir.setBoolean(32, obj.isExigirParecerComissaoAvaliadora().booleanValue());
					sqlInserir.setBoolean(33, obj.isPagamentoPorAutorSubmissao().booleanValue());
					sqlInserir.setString(34, obj.getSituacaoFinanceira());
					return sqlInserir;
				}
			}, new ResultSetExtractor() {

				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>EventoVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados
	 * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>EventoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final EventoVO obj) throws Exception {
		try {
			Evento.alterar(getIdEntidade());
			EventoVO.validarDados(obj);
			final String sql = "UPDATE Evento set nome=?, dataInicioRealizacao=?, dataFinalRealizacao=?, dataInicioInscricao=?, dataFinalInscricao=?, valorAluno=?, valorProfessor=?, valorFuncionario=?, valorComunidade=?, localEvento=?, responsavel=?, curso=?, unidadeEnsino=?, site=?, emailSuporte=?, telefoneSuporte=?, tipoInscricao=?, nrVagas=?, nrMaximoVagasExcedentes=?, formaInscricao=?, localInscricao=?, aceitarSubmissaoTrabalho=?, regrasFormatacaoTrabalho=?, dataInicialSubmissao=?, dataFinalSubmissao=?, tipoSubmissao=?, valorSubmissaoAluno=?, valorSubmissaoProfessor=?, valorSubmissaoFuncionario=?, valorSubmissaoComunidade=?, ocultarAutorComissaoAvaliadora=?, exigirParecerComissaoAvaliadora=?, pagamentoPorAutorSubmissao=?, situacaoFinanceira=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataInicioRealizacao()));
					sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataFinalRealizacao()));
					sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataInicioInscricao()));
					sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataFinalInscricao()));
					sqlAlterar.setDouble(6, obj.getValorAluno().doubleValue());
					sqlAlterar.setDouble(7, obj.getValorProfessor().doubleValue());
					sqlAlterar.setDouble(8, obj.getValorFuncionario().doubleValue());
					sqlAlterar.setDouble(9, obj.getValorComunidade().doubleValue());
					sqlAlterar.setString(10, obj.getLocalEvento());
					sqlAlterar.setInt(11, obj.getResponsavel().getCodigo().intValue());
					sqlAlterar.setInt(12, obj.getCurso().getCodigo().intValue());
					sqlAlterar.setInt(13, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlAlterar.setString(14, obj.getSite());
					sqlAlterar.setString(15, obj.getEmailSuporte());
					sqlAlterar.setString(16, obj.getTelefoneSuporte());
					sqlAlterar.setString(17, obj.getTipoInscricao());
					sqlAlterar.setInt(18, obj.getNrVagas().intValue());
					sqlAlterar.setInt(19, obj.getNrMaximoVagasExcedentes().intValue());
					sqlAlterar.setString(20, obj.getFormaInscricao());
					sqlAlterar.setString(21, obj.getLocalInscricao());
					sqlAlterar.setBoolean(22, obj.isAceitarSubmissaoTrabalho().booleanValue());
					sqlAlterar.setString(23, obj.getRegrasFormatacaoTrabalho());
					sqlAlterar.setDate(24, Uteis.getDataJDBC(obj.getDataInicialSubmissao()));
					sqlAlterar.setDate(25, Uteis.getDataJDBC(obj.getDataFinalSubmissao()));
					sqlAlterar.setString(26, obj.getTipoSubmissao());
					sqlAlterar.setDouble(27, obj.getValorSubmissaoAluno().doubleValue());
					sqlAlterar.setDouble(28, obj.getValorSubmissaoProfessor().doubleValue());
					sqlAlterar.setDouble(29, obj.getValorSubmissaoFuncionario().doubleValue());
					sqlAlterar.setDouble(30, obj.getValorSubmissaoComunidade().doubleValue());
					sqlAlterar.setBoolean(31, obj.isOcultarAutorComissaoAvaliadora().booleanValue());
					sqlAlterar.setBoolean(32, obj.isExigirParecerComissaoAvaliadora().booleanValue());
					sqlAlterar.setBoolean(33, obj.isPagamentoPorAutorSubmissao().booleanValue());
					sqlAlterar.setString(34, obj.getSituacaoFinanceira());
					sqlAlterar.setInt(35, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>EventoVO</code>. Sempre localiza o registro a
	 * ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>EventoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(EventoVO obj) throws Exception {
		try {
			Evento.excluir(getIdEntidade());
			String sql = "DELETE FROM Evento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>String situacaoFinanceira</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorSituacaoFinanceira(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE situacaoFinanceira like('" + valorConsulta + "%') ORDER BY situacaoFinanceira";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Double valorSubmissaoComunidade</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorSubmissaoComunidade(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE valorSubmissaoComunidade >= " + valorConsulta.doubleValue() + " ORDER BY valorSubmissaoComunidade";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Double valorSubmissaoFuncionario</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorSubmissaoFuncionario(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE valorSubmissaoFuncionario >= " + valorConsulta.doubleValue() + " ORDER BY valorSubmissaoFuncionario";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Double valorSubmissaoProfessor</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorSubmissaoProfessor(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE valorSubmissaoProfessor >= " + valorConsulta.doubleValue() + " ORDER BY valorSubmissaoProfessor";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Double valorSubmissaoAluno</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorSubmissaoAluno(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE valorSubmissaoAluno >= " + valorConsulta.doubleValue() + " ORDER BY valorSubmissaoAluno";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>String tipoSubmissao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoSubmissao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE tipoSubmissao like('" + valorConsulta + "%') ORDER BY tipoSubmissao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Date dataFinalSubmissao</code>. Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataFinalSubmissao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE ((dataFinalSubmissao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalSubmissao <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataFinalSubmissao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Date dataInicialSubmissao</code>. Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataInicialSubmissao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE ((dataInicialSubmissao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicialSubmissao <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataInicialSubmissao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>String regrasFormatacaoTrabalho</code>. Retorna os objetos, com início do valor do atributo idêntico ao
	 * parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorRegrasFormatacaoTrabalho(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE regrasFormatacaoTrabalho like('" + valorConsulta + "%') ORDER BY regrasFormatacaoTrabalho";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Integer nrMaximoVagasExcedentes</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrMaximoVagasExcedentes(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE nrMaximoVagasExcedentes >= " + valorConsulta.intValue() + " ORDER BY nrMaximoVagasExcedentes";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Integer nrVagas</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrVagas(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE nrVagas >= " + valorConsulta.intValue() + " ORDER BY nrVagas";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>String tipoInscricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoInscricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE tipoInscricao like('" + valorConsulta + "%') ORDER BY tipoInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo <code>nome</code> da
	 * classe <code>UnidadeEnsino</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Evento.* FROM Evento, UnidadeEnsino WHERE Evento.unidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.nome like('" + valorConsulta
				+ "%') ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo <code>nome</code> da
	 * classe <code>Curso</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar
	 * o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeCurso(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Evento.* FROM Evento, Curso WHERE Evento.curso = Curso.codigo and Curso.nome like('" + valorConsulta + "%') ORDER BY Curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo <code>nome</code> da
	 * classe <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Evento.* FROM Evento, Pessoa WHERE Evento.responsavel = Pessoa.codigo and Pessoa.nome like('" + valorConsulta + "%') ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Double valorFuncionario</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorFuncionario(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE valorFuncionario >= " + valorConsulta.doubleValue() + " ORDER BY valorFuncionario";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Double valorProfessor</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorProfessor(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE valorProfessor >= " + valorConsulta.doubleValue() + " ORDER BY valorProfessor";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Double valorAluno</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorAluno(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE valorAluno >= " + valorConsulta.doubleValue() + " ORDER BY valorAluno";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Date dataFinalInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataFinalInscricao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE ((dataFinalInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalInscricao <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataFinalInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Date dataInicioInscricao</code>. Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataInicioInscricao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE ((dataInicioInscricao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicioInscricao <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataInicioInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Date dataFinalRealizacao</code>. Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataFinalRealizacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE ((dataFinalRealizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataFinalRealizacao <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataFinalRealizacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Date dataInicioRealizacao</code>. Retorna os objetos com valores pertecentes ao período informado por
	 * parâmetro. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDataInicioRealizacao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE ((dataInicioRealizacao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataInicioRealizacao <= '" + Uteis.getDataJDBC(prmFim)
				+ "')) ORDER BY dataInicioRealizacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE nome like('" + valorConsulta + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Evento</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Evento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>EventoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>EventoVO</code>.
	 * 
	 * @return O objeto da classe <code>EventoVO</code> com os dados devidamente montados.
	 */
	public static EventoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		EventoVO obj = new EventoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setDataInicioRealizacao(dadosSQL.getDate("dataInicioRealizacao"));
		obj.setDataFinalRealizacao(dadosSQL.getDate("dataFinalRealizacao"));
		obj.setDataInicioInscricao(dadosSQL.getDate("dataInicioInscricao"));
		obj.setDataFinalInscricao(dadosSQL.getDate("dataFinalInscricao"));
		obj.setValorAluno(new Double(dadosSQL.getDouble("valorAluno")));
		obj.setValorProfessor(new Double(dadosSQL.getDouble("valorProfessor")));
		obj.setValorFuncionario(new Double(dadosSQL.getDouble("valorFuncionario")));
		obj.setValorComunidade(new Double(dadosSQL.getDouble("valorComunidade")));
		obj.setLocalEvento(dadosSQL.getString("localEvento"));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.getUnidadeEnsino().setCodigo(new Integer(dadosSQL.getInt("unidadeEnsino")));
		obj.setSite(dadosSQL.getString("site"));
		obj.setEmailSuporte(dadosSQL.getString("emailSuporte"));
		obj.setTelefoneSuporte(dadosSQL.getString("telefoneSuporte"));
		obj.setTipoInscricao(dadosSQL.getString("tipoInscricao"));
		obj.setNrVagas(new Integer(dadosSQL.getInt("nrVagas")));
		obj.setNrMaximoVagasExcedentes(new Integer(dadosSQL.getInt("nrMaximoVagasExcedentes")));
		obj.setFormaInscricao(dadosSQL.getString("formaInscricao"));
		obj.setLocalInscricao(dadosSQL.getString("localInscricao"));
		obj.setAceitarSubmissaoTrabalho(new Boolean(dadosSQL.getBoolean("aceitarSubmissaoTrabalho")));
		obj.setRegrasFormatacaoTrabalho(dadosSQL.getString("regrasFormatacaoTrabalho"));
		obj.setDataInicialSubmissao(dadosSQL.getDate("dataInicialSubmissao"));
		obj.setDataFinalSubmissao(dadosSQL.getDate("dataFinalSubmissao"));
		obj.setTipoSubmissao(dadosSQL.getString("tipoSubmissao"));
		obj.setValorSubmissaoAluno(new Double(dadosSQL.getDouble("valorSubmissaoAluno")));
		obj.setValorSubmissaoProfessor(new Double(dadosSQL.getDouble("valorSubmissaoProfessor")));
		obj.setValorSubmissaoFuncionario(new Double(dadosSQL.getDouble("valorSubmissaoFuncionario")));
		obj.setValorSubmissaoComunidade(new Double(dadosSQL.getDouble("valorSubmissaoComunidade")));
		obj.setOcultarAutorComissaoAvaliadora(new Boolean(dadosSQL.getBoolean("ocultarAutorComissaoAvaliadora")));
		obj.setExigirParecerComissaoAvaliadora(new Boolean(dadosSQL.getBoolean("exigirParecerComissaoAvaliadora")));
		obj.setPagamentoPorAutorSubmissao(new Boolean(dadosSQL.getBoolean("pagamentoPorAutorSubmissao")));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCurso(obj, nivelMontarDados, usuario);
		montarDadosUnidadeEnsino(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao
	 * objeto <code>EventoVO</code>. Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(EventoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto
	 * <code>EventoVO</code>. Faz uso da chave primária da classe <code>CursoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCurso(EventoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>EventoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(EventoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new PessoaVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getResponsavel().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>EventoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public EventoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados ,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Evento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Evento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Evento.idEntidade = idEntidade;
	}
}
