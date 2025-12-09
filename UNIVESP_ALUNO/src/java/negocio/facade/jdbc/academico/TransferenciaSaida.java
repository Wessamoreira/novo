package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

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

import jobs.JobExecutarSincronismoComLdapAoCancelarTransferirMatricula;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaSaidaVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;

import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.TransferenciaSaidaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TransferenciaSaidaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>TransferenciaSaidaVO</code>. Encapsula toda a interação com o
 * banco de dados.
 * 
 * @see TransferenciaSaidaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TransferenciaSaida extends ControleAcesso implements TransferenciaSaidaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public TransferenciaSaida() throws Exception {
		super();
		setIdEntidade("TransferenciaSaida");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>TransferenciaSaidaVO</code>.
	 */
	public TransferenciaSaidaVO novo() throws Exception {
		TransferenciaSaida.incluir(getIdEntidade());
		TransferenciaSaidaVO obj = new TransferenciaSaidaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>TransferenciaSaidaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>TransferenciaSaidaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TransferenciaSaidaVO obj,  UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			validarDados(obj, configuracaoGeralSistema);
			incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO TransferenciaSaida( data, descricao, matricula, codigoRequerimento, instituicaoDestino, cursoDestino, justiticativa, tipoJustificativa, responsavelAutorizacao ) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(++i, obj.getDescricao());
					sqlInserir.setString(++i, obj.getMatricula().getMatricula());
					sqlInserir.setInt(++i, obj.getCodigoRequerimento().getCodigo().intValue());
					sqlInserir.setString(++i, obj.getInstituicaoDestino());
					sqlInserir.setString(++i, obj.getCursoDestino());
					sqlInserir.setString(++i, obj.getJustiticativa());
					sqlInserir.setString(++i, obj.getTipoJustificativa());
					sqlInserir.setInt(++i, obj.getResponsavelAutorizacao().getCodigo().intValue());
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
			alterarSituacaoAcademicaMatriculaTransferenciaSaida(obj, usuario);
			obj.getMatricula().setSituacao(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por executar a alteração da situação acadêmica da matrícula
	 * no ato da inclusão.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param transferenciaSaidaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoAcademicaMatriculaTransferenciaSaida(TransferenciaSaidaVO obj,  UsuarioVO usuario) throws Exception {		
		if (!obj.getMatricula().getCurso().getIntegral()) {		
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(obj.getMatricula().getMatricula(), "'AT', 'PR'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			matriculaPeriodoVO.setMatriculaVO(obj.getMatricula());
			matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor());
			getFacadeFactory().getTrancamentoFacade().alterarSituacaoAcademicaMatricula(matriculaPeriodoVO, obj.getHistoricoVOs(), obj.getCodigoRequerimento(), obj.getData(), OrigemFechamentoMatriculaPeriodoEnum.TRANSFERENCIA_SAIDA, obj.getCodigo(), false, false, true, true, usuario);
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
			getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluirTransferenciaSaidaAluno(obj, usuario, configuracaoGeralSistemaVO);
		} else {
			List listaMatriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPorMatriculaSituacoes(obj.getMatricula().getMatricula(), "'AT', 'PR'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			Iterator i = listaMatriculaPeriodoVO.iterator();
			while (i.hasNext()) {
				MatriculaPeriodoVO matriculaPeriodoVO = (MatriculaPeriodoVO)i.next();
				matriculaPeriodoVO.setMatriculaVO(obj.getMatricula());
				matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor());
				getFacadeFactory().getTrancamentoFacade().alterarSituacaoAcademicaMatricula(matriculaPeriodoVO, obj.getHistoricoVOs(), obj.getCodigoRequerimento(), obj.getData(), OrigemFechamentoMatriculaPeriodoEnum.TRANSFERENCIA_SAIDA, obj.getCodigo(), false, false, true, true ,usuario);
			}			
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>TransferenciaSaidaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto.
	 * Verifica a conexão com o banco de dados e a permissão do usuário para
	 * realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>TransferenciaSaidaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TransferenciaSaidaVO obj,  UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			validarDados(obj, configuracaoGeralSistema);
			alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE TransferenciaSaida set data=?, descricao=?, matricula=?, instituicaoDestino=?, cursoDestino=?, justiticativa=?, tipoJustificativa=?, responsavelAutorizacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			// Obtém o bloqueio para a Transferência.
			// getBloqueio().lock();
			// Matricula observador = (Matricula) ((Advised)
			// getFacadeFactory().getMatriculaFacade()).getTargetSource().getTarget();
			Date updated = null;
			// Obtém o bloqueio para a matricula.
			// observador.getBloqueio().lock();
			updated = new Date();
			try {
				// obj.getCodigoRequerimento().getMatricula().addObserver(observador);
				// obj.getCodigoRequerimento().getMatricula().setChanged();
				// obj.getCodigoRequerimento().getMatricula().notifyObservers();

				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
						int i = 0;
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getData()));
						sqlAlterar.setString(++i, obj.getDescricao());
						// sqlAlterar.setString(3, obj.getSituacao());
						sqlAlterar.setString(++i, obj.getMatricula().getMatricula());
						sqlAlterar.setString(++i, obj.getInstituicaoDestino());
						sqlAlterar.setString(++i, obj.getCursoDestino());
						sqlAlterar.setString(++i, obj.getJustiticativa());
						sqlAlterar.setString(++i, obj.getTipoJustificativa());
						sqlAlterar.setInt(++i, obj.getResponsavelAutorizacao().getCodigo().intValue());
						sqlAlterar.setInt(++i, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				});
				// Atualiza tabela de matrícula.
				// realizarAtualizacaoDoCampoUpdated(MatriculaVO.class,
				// "(matricula = ?)", updated,
				// obj.getCodigoRequerimento().getMatricula().getMatricula());
				obj.getMatricula().setUpdated(updated);
			} finally {
				// obj.getCodigoRequerimento().getMatricula().deleteObserver(observador);
				// Libera o bloqueio para a matrícula.
				// observador.getBloqueio().unlock();
				// Libera o bloqueio da transferência.
				// getBloqueio().unlock();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TransferenciaSaidaVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>TransferenciaSaidaVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(TransferenciaSaidaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM TransferenciaSaida WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>nome</code> da classe
	 * <code>Pessoa</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaSaida.* FROM TransferenciaSaida, Usuario WHERE TransferenciaSaida.responsavelAutorizacao = Usuario.codigo and sem_acentos(Usuario.nome) ilike(sem_acentos('" + valorConsulta + "%')) ORDER BY Usuario.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>String tipoJustificativa</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorTipoJustificativa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaSaida WHERE tipoJustificativa like('" + valorConsulta + "%') ORDER BY tipoJustificativa";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>String instituicaoDestino</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorInstituicaoDestino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaSaida WHERE instituicaoDestino like('" + valorConsulta + "%') ORDER BY instituicaoDestino";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>Integer codigoRequrimento</code>.
	 * Retorna os objetos com valores iguais ou superiores ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaSaida WHERE codigoRequerimento >= " + valorConsulta.intValue() + " ORDER BY codigoRequerimento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaSaida.* FROM TransferenciaSaida, Matricula WHERE TransferenciaSaida.matricula = Matricula.matricula and Matricula.matricula ilike('" + valorConsulta + "%') ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public TransferenciaSaidaVO consultarPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaSaida.* FROM TransferenciaSaida, Matricula WHERE TransferenciaSaida.matricula = Matricula.matricula and Matricula.matricula = '" + matricula + "' limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		TransferenciaSaidaVO retorno = new TransferenciaSaidaVO();
		if (tabelaResultado.next()) {
			retorno = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS,  usuario);
		}
		return retorno;
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>matricula</code> da classe
	 * <code>Matricula</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List<TransferenciaSaidaVO> consultarPorAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TransferenciaSaida.* FROM TransferenciaSaida inner join Matricula on TransferenciaSaida.matricula = Matricula.matricula inner join pessoa on matricula.aluno = pessoa.codigo where  sem_acentos(pessoa.nome) ilike(sem_acentos('" + valorConsulta + "%')) ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaSaida WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>Date data</code>. Retorna os objetos
	 * com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaSaida WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<TransferenciaSaidaVO> consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaSaida " + "LEFT JOIN matricula ON TransferenciaSaida.matricula = matricula.matricula " + "LEFT JOIN unidadeEnsino ON matricula.unidadeEnsino = unidadeEnsino.codigo " + "WHERE ((TransferenciaSaida.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (TransferenciaSaida.data <= '" + Uteis.getDataJDBC(prmFim) + "')) AND unidadeEnsino.nome = '" + unidadeEnsino + "' ORDER BY TransferenciaSaida.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TransferenciaSaida</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<TransferenciaSaidaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TransferenciaSaida WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>TransferenciaSaidaVO</code> resultantes da consulta.
	 */
	public  List<TransferenciaSaidaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		List<TransferenciaSaidaVO> vetResultado = new ArrayList<TransferenciaSaidaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TransferenciaSaidaVO</code>.
	 *
	 * @return O objeto da classe <code>TransferenciaSaidaVO</code> com os dados
	 *         devidamente montados.
	 */
	public  TransferenciaSaidaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		TransferenciaSaidaVO obj = new TransferenciaSaidaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getCodigoRequerimento().setCodigo(new Integer(dadosSQL.getInt("codigoRequerimento")));
		obj.setInstituicaoDestino(dadosSQL.getString("instituicaoDestino"));
		obj.setCursoDestino(dadosSQL.getString("cursoDestino"));
		obj.setJustiticativa(dadosSQL.getString("justiticativa"));
		obj.setTipoJustificativa(dadosSQL.getString("tipoJustificativa"));
		obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
		obj.setEstornado(dadosSQL.getBoolean("estornado"));
		if(dadosSQL.getBoolean("estornado")){
			obj.setMotivoEstorno(dadosSQL.getString("motivoEstorno"));
			obj.setDataEstorno(dadosSQL.getDate("dataEstorno"));
			obj.getResponsavelEstorno().setCodigo(dadosSQL.getInt("responsavelEstorno"));
			if(Uteis.isAtributoPreenchido(obj.getResponsavelEstorno())){
				obj.setResponsavelEstorno(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelEstorno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
			}
		}
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosRequerimento(obj, nivelMontarDados, usuario);
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public  void montarDadosRequerimento(TransferenciaSaidaVO obj, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		if (obj.getCodigoRequerimento().getCodigo().equals(0)) {
			obj.setCodigoRequerimento(new RequerimentoVO());
			return;
		}
		obj.setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>TransferenciaSaidaVO</code>. Faz uso da chave primária da classe
	 * <code>PessoaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosResponsavelAutorizacao(TransferenciaSaidaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().equals(0)) {
			obj.setResponsavelAutorizacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>TransferenciaSaidaVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosMatricula(TransferenciaSaidaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>TransferenciaSaidaVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public TransferenciaSaidaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM TransferenciaSaida WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TransferenciaSaida.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TransferenciaSaida.idEntidade = idEntidade;
	}

	/**
	 * Responsável por executar a persistência dos dados pertinentes a
	 * TransferenciaSaidaVO.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @param configuracaoGeralSistemaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void persistir(final TransferenciaSaidaVO obj,  ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, usuario, configuracaoGeralSistema);
		} else {
			alterar(obj, usuario, configuracaoGeralSistema);
		}
	}

	@Override
	public void validarTransferenciaSaida(TransferenciaSaidaVO obj) throws ConsistirException {
		if ((Uteis.isAtributoPreenchido(obj.getCodigoRequerimento().getCodigo()))) {
			if (obj.getCodigoRequerimento().getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor()) || obj.getCodigoRequerimento().getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor())) {
				throw new ConsistirException("Requerimento especificado já está finalizado.");
			}
			if (obj.getCodigoRequerimento().getSituacao().equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor())) {
				throw new ConsistirException("Requerimento especificado está aguardando pagamento.");
			}
		}
		if (obj.getMatricula().getSituacao().equals("TS")) {
			throw new ConsistirException("Matrícula especificada já está transferida.");
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>TransferenciaSaidaVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(TransferenciaSaidaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getData())) {
			throw new ConsistirException("O campo DATA (Transferência Saída) deve ser informado.");
		}
//		if (!Uteis.isAtributoPreenchido(obj.getSituacao())) {
//			throw new ConsistirException("O campo SITUAÇÃO (Transferência Saída) deve ser informado.");
//		}
		if (!Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			throw new ConsistirException("O campo MATRÍCULA (Transferência Saída) deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(configuracaoGeralSistemaVO) && !configuracaoGeralSistemaVO.getPermiteTransferenciaSaidaSemRequerimento()) {
			if (!Uteis.isAtributoPreenchido(obj.getCodigoRequerimento())) {
				throw new ConsistirException("O campo CÓDIGO REQUERIMENTO (Transferência Saída) deve ser informado.");
			}
		}
		if (!Uteis.isAtributoPreenchido(obj.getResponsavelAutorizacao())) {
			throw new ConsistirException("O campo RESPONSÁVEL AUTORIZAÇÃO (Transferência Saída) deve ser informado.");
		}
		if (obj.getEstornado() && obj.getMotivoEstorno().trim().isEmpty()) {
			throw new ConsistirException("O campo MOTIVO ESTORNO (Transferência Saída) deve ser informado.");
		}
	}

	/**
	 * Responsável por executar a validação se a matrícula está apta a realizar
	 * a transferência de saída, levando em consideração sua situação, a situação
	 * financeira e e se existe pendência em empréstimo na biblioteca.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
//	@Override
//	public void executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
//		getFacadeFactory().getMatriculaPeriodoFacade().validarExistenciaMatriculaPeriodoAptaRealizarTrancamentoCancelamentoTransferencia(matriculaVO.getMatricula());
//		boolean existePendenciaFinanceira = getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(matriculaVO.getMatricula(), usuarioVO);
//		if (existePendenciaFinanceira && !matriculaVO.getCanceladoFinanceiro()) {
//			throw new Exception(UteisJSF.internacionalizar("msg_TransferenciaSaida_matriculaPendenteFinanceira"));
//		}
//		getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().validarDadosPendenciaEmprestimoBiblioteca(matriculaVO, false, false, false, true, false, false, configuracaoGeralSistemaVO, usuarioVO);
//	}

	@Override
	public void validarDadosAntesImpressao(TransferenciaSaidaVO obj, Integer textoPadrao) throws Exception {		
		if (obj.getMatricula() == null || !Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			throw new Exception("O Aluno deve ser informado para geração do relatório.");
		}
		if(!Uteis.isAtributoPreenchido(textoPadrao)){
			throw new Exception("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}
	
	@Override
	public String imprimirDeclaracaoTransferenciaSaida(TransferenciaSaidaVO transferenciaSaidaVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO config,  UsuarioVO usuario) throws Exception {
		String caminhoRelatorio = "";
		try {
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContratoVO.setGerarNovoArquivoAssinado(true);
			impressaoContratoVO.setMatriculaVO(transferenciaSaidaVO.getMatricula());
			impressaoContratoVO.getMatriculaVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(transferenciaSaidaVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(transferenciaSaidaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.setTurmaVO(impressaoContratoVO.getMatriculaPeriodoVO().getTurma());
			impressaoContratoVO.setTextoPadraoDeclaracao(textoPadraoDeclaracaoVO.getCodigo());
			textoPadraoDeclaracaoVO.setTexto(textoPadraoDeclaracaoVO.substituirTag(textoPadraoDeclaracaoVO.getTexto(), "[(70){}Justificativa_Aluno]", transferenciaSaidaVO.getJustiticativa(), "", 70));
			textoPadraoDeclaracaoVO.substituirTag(impressaoContratoVO, usuario);
			if (textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isHtml()){
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoPadraoDeclaracaoVO.getTexto());
			} 
//			else {
//				caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracaoVO, "", true, config, usuario);
//				getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
//			}
			return caminhoRelatorio;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarEstornoTransferenciaSaida(TransferenciaSaidaVO transferenciaSaidaVO,  UsuarioVO usuarioVO) throws Exception{
		verificarPermissaoFuncionalidadeUsuario("PermiteEstornarTransferenciaSaida", usuarioVO);		
		try{ 
			if(transferenciaSaidaVO.getEstornado()){
				throw new Exception("Esta TRANFERÊNCIA DE SAIDA já esta estornada.");
			}
			transferenciaSaidaVO.setEstornado(true);
			validarDados(transferenciaSaidaVO, null);
			getFacadeFactory().getMatriculaFacade().carregarDados(transferenciaSaidaVO.getMatricula(), usuarioVO);
			if(!transferenciaSaidaVO.getMatricula().getSituacao().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor())){
				throw new Exception("Só é possível realizar o estorno da transferência de saida se a situação da ultima matrícula periodo for transferência de saida.");
			}
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaAnoSemestre(transferenciaSaidaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if(!matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getValor())){
				throw new Exception("Só é possível realizar o estorno da transferência de saida se a situação da ultima matrícula periodo for transferência de saida.");
			}
			transferenciaSaidaVO.setDataEstorno(new Date());
			transferenciaSaidaVO.getResponsavelEstorno().setCodigo(usuarioVO.getCodigo());
			transferenciaSaidaVO.getResponsavelEstorno().setNome(usuarioVO.getNome());			
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(transferenciaSaidaVO.getMatricula().getMatricula(), "AT", usuarioVO);
			matriculaPeriodoVO.setSituacaoMatriculaPeriodo("AT");
			matriculaPeriodoVO.setAlunoTransferidoUnidade(false);
			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, null, null, null);
			List<HistoricoVO> historicos = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoSituacoes(matriculaPeriodoVO.getCodigo(),"'"+SituacaoHistorico.TRANSFERIDO.getValor()+"'", usuarioVO);
			if (!historicos.isEmpty()) {
				for(HistoricoVO historicoVO: historicos){
					if(historicoVO.getSituacao().equals(SituacaoHistorico.TRANSFERIDO.getValor())){
						historicoVO.setSituacao("CS");
					}
				}
				getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(historicos, false, true, usuarioVO);
			}					
			alterarSituacaoEstorno(transferenciaSaidaVO, usuarioVO);
			
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(transferenciaSaidaVO.getMatricula().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
			if(Uteis.isAtributoPreenchido(usuario)) {
				if(Uteis.isAtributoPreenchido(usuario.getPessoa().getRegistroAcademico())) {				
					getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( usuario, usuario.getPessoa().getRegistroAcademico(),usuario.getPessoa().getRegistroAcademico(),false, usuarioVO);
					usuario.setUsername(usuario.getPessoa().getRegistroAcademico());
					getFacadeFactory().getUsuarioFacade().alterarBooleanoResetouSenhaPrimeiroAcesso(false, usuario, false, usuario);	
				}	
				JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuario, transferenciaSaidaVO.getMatricula(), true,usuarioVO);
				Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
				jobSincronizarCancelamento.start();
			}
			if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO())) {
				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorMatricula(transferenciaSaidaVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
					getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtivacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuarioVO);
					if(pessoaEmailInstitucionalVO.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.INATIVO)) {
						pessoaEmailInstitucionalVO.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
						getFacadeFactory().getPessoaEmailInstitucionalFacade().alterarSituacaoStatusAtivoInativoEnum(pessoaEmailInstitucionalVO, usuario);
					}
				}		
			}
			
		}catch(Exception e){
			transferenciaSaidaVO.setEstornado(false);
			throw e;
		}		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoEstorno(final TransferenciaSaidaVO obj, UsuarioVO usuario) throws Exception {
			alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE TransferenciaSaida set estornado = ?, dataEstorno=?, motivoEstorno=?, responsavelEstorno=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
						int i = 0;
						sqlAlterar.setBoolean(++i, true);
						sqlAlterar.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataEstorno()));
						sqlAlterar.setString(++i, obj.getMotivoEstorno());
						sqlAlterar.setInt(++i, obj.getResponsavelEstorno().getCodigo().intValue());
						sqlAlterar.setInt(++i, obj.getCodigo().intValue());
						return sqlAlterar;
					}
				});

	} 


}
