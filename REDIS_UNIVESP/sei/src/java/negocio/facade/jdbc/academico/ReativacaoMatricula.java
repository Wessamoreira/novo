package negocio.facade.jdbc.academico;

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

import controle.arquitetura.DataModelo;
import jobs.JobExecutarSincronismoComLdapAoCancelarTransferirMatricula;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.ReativacaoMatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoMatriculaPeriodo;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ReativacaoMatriculaInterfaceFacade;


/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ReativacaoMatriculaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ReativacaoMatriculaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ReativacaoMatriculaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class ReativacaoMatricula extends ControleAcesso implements ReativacaoMatriculaInterfaceFacade {

	protected static String idEntidade;

	public ReativacaoMatricula() throws Exception {
		super();
		setIdEntidade("ReativacaoMatricula");
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#novo()
	 */
	public ReativacaoMatriculaVO novo() throws Exception {
		ReativacaoMatricula.incluir(getIdEntidade());
		ReativacaoMatriculaVO obj = new ReativacaoMatriculaVO();
		return obj;
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#alterarSituacaoAcademicaMatriculaPeriodo(negocio.comuns.academico.ReativacaoMatriculaVO)
       * Método comentado aguarando definir regra de negocio pra renovar - trancar - reativar.
       * Responsável: Rodrigo Jayme
	 */
	public void alterarSituacaoAcademicaMatriculaPeriodo(ReativacaoMatriculaVO reativacaoMatriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaOuTrancadaPorMatriculaSemExcecao(reativacaoMatriculaVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiro, usuario);
		if (matriculaPeriodoVO != null) {
			matriculaPeriodoVO.setSituacao(SituacaoMatriculaPeriodo.CONCLUIDA.getValor());
//			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoComUsuarioResponsavel(null, matriculaPeriodoVO, usuario)MatriculaPeriodo(matriculaPeriodoVO,);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().retirarReservaTurmaDisciplina(matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(),null);
			reativacaoMatriculaVO.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(reativacaoMatriculaVO.getMatricula().getMatricula(), 0, NivelMontarDados.BASICO, usuario));
			getFacadeFactory().getMatriculaDWFacade().incluir(reativacaoMatriculaVO.criarMatriculaDW(matriculaPeriodoVO.getProcessoMatricula(), 1),usuario);

		}
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#incluir(negocio.comuns.academico.ReativacaoMatriculaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ReativacaoMatriculaVO obj, UsuarioVO usuarioVO, Boolean permiteReativacaoMatriculaSemRequerimento) throws Exception {
		try {
			ReativacaoMatricula.incluir(getIdEntidade(), true, usuarioVO);
			ReativacaoMatriculaVO.validarDados(obj, permiteReativacaoMatriculaSemRequerimento);
			final String sql = "INSERT INTO ReativacaoMatricula( data, descricao, matricula, requerimento, trancamento, responsavelAutorizacao, situacao ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
        			sqlInserir.setString(2, obj.getDescricao());		
        			sqlInserir.setString(3, obj.getMatricula().getMatricula());
                                if ((obj.getRequerimento().getCodigo() != null) &&
                                    (obj.getRequerimento().getCodigo().intValue() != 0)) {
                                    sqlInserir.setInt(4, obj.getRequerimento().getCodigo().intValue());
                                } else {
                                    sqlInserir.setNull(4, 0);
                                }
                                if ((obj.getTrancamento().getCodigo() != null) &&
                                    (obj.getTrancamento().getCodigo().intValue() != 0)) {
                                    sqlInserir.setInt(5, obj.getTrancamento().getCodigo().intValue());
                                } else {
                                    sqlInserir.setNull(5, 0);
                                }
        			sqlInserir.setInt(6, obj.getResponsavelAutorizacao().getCodigo().intValue());
                                sqlInserir.setString(7, obj.getSituacao());
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
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#alterar(negocio.comuns.academico.ReativacaoMatriculaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ReativacaoMatriculaVO obj, UsuarioVO usuarioVO, Boolean permiteReativacaoMatriculaSemRequerimento) throws Exception {
		try {
			ReativacaoMatriculaVO.validarDados(obj, permiteReativacaoMatriculaSemRequerimento);
			ReativacaoMatricula.alterar(getIdEntidade(), true, usuarioVO);
			final String sql = "UPDATE ReativacaoMatricula set data=?, descricao=?, matricula=?, requerimento=?, responsavelAutorizacao=?, situacao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
        			sqlAlterar.setString(2, obj.getDescricao());			
        			sqlAlterar.setString(3, obj.getMatricula().getMatricula());
                                if ((obj.getRequerimento().getCodigo() != null) &&
                                    (obj.getRequerimento().getCodigo().intValue() != 0)) {
                                    sqlAlterar.setInt(4, obj.getRequerimento().getCodigo().intValue());
                                } else {
                                    sqlAlterar.setNull(4, 0);
                                }
        			sqlAlterar.setInt(5, obj.getResponsavelAutorizacao().getCodigo().intValue());
                                sqlAlterar.setString(6, obj.getSituacao());
        			sqlAlterar.setInt(7, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
			
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#excluir(negocio.comuns.academico.ReativacaoMatriculaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ReativacaoMatriculaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ReativacaoMatricula.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM ReativacaoMatricula WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#deferirRequerimento(negocio.comuns.academico.ReativacaoMatriculaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void deferirRequerimento(ReativacaoMatriculaVO reativacaoMatriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		try {
			if(reativacaoMatriculaVO.getRequerimento().getCodigo() > 0) {
				reativacaoMatriculaVO.getRequerimento().setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
				getFacadeFactory().getRequerimentoFacade().alterarSituacao(reativacaoMatriculaVO.getRequerimento().getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), "", reativacaoMatriculaVO.getRequerimento().getMotivoDeferimento(),reativacaoMatriculaVO.getRequerimento(), usuario);
				
			}
			reativacaoMatriculaVO.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
			alterarSituacaoReativacaoMatricula(reativacaoMatriculaVO.getCodigo(), reativacaoMatriculaVO.getSituacao());
			getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(reativacaoMatriculaVO.getMatricula().getMatricula(), "AT", usuario);
			getFacadeFactory().getMatriculaFacade().alterarMatriculaCanceladoFinanceiro(reativacaoMatriculaVO.getMatricula().getMatricula(), false);
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(reativacaoMatriculaVO.getMatricula().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			if(Uteis.isAtributoPreenchido(usuarioVO)) {
				JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuarioVO, reativacaoMatriculaVO.getMatricula(), true,usuario);
				Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
				jobSincronizarCancelamento.start();
			}			
			if ( Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO())) {
				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(reativacaoMatriculaVO.getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
				if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
					getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtivacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuario);
				}			
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#indeferirRequerimento(negocio.comuns.academico.ReativacaoMatriculaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void indeferirRequerimento(ReativacaoMatriculaVO reativacaoMatriculaVO) throws Exception {
		try {
			if(reativacaoMatriculaVO.getRequerimento().getCodigo() > 0){
				reativacaoMatriculaVO.getRequerimento().setSituacao(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor());
				getFacadeFactory().getRequerimentoFacade().alterarSituacao(reativacaoMatriculaVO.getRequerimento().getCodigo(), SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor(), reativacaoMatriculaVO.getRequerimento().getMotivoIndeferimento(), "",reativacaoMatriculaVO.getRequerimento(),  null);
	                        
			}
			reativacaoMatriculaVO.setSituacao(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor());
			alterarSituacaoReativacaoMatricula(reativacaoMatriculaVO.getCodigo(), reativacaoMatriculaVO.getSituacao());
                } catch (Exception e) {
			throw e;
		}
	}

        @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoReativacaoMatricula(Integer codigo, String situacao) {
		String sqlStr = "UPDATE reativacaomatricula SET situacao = ? WHERE codigo = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacao, codigo });
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorNomePessoa(java.lang.String, boolean, int)
	 */
	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ReativacaoMatricula.* FROM ReativacaoMatricula, Usuario WHERE ReativacaoMatricula.responsavelAutorizacao = usuario.codigo and lower (Usuario.nome) like('"
				+ valorConsulta.toLowerCase() + "%') ORDER BY Usuario.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO);
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorCodigoRequerimento(java.lang.Integer, boolean, int)
	 */
	public List consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ReativacaoMatricula.* FROM ReativacaoMatricula, Requerimento WHERE ReativacaoMatricula.Requerimento = Requerimento.codigo and Requerimento.codigo >= "
				+ valorConsulta.intValue() + " ORDER BY Requerimento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO);
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorMatriculaMatricula(java.lang.String, boolean, int)
	 */
	public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ReativacaoMatricula.* FROM ReativacaoMatricula, Matricula WHERE ReativacaoMatricula.matricula = Matricula.matricula and lower (Matricula.matricula) like('"
				+ valorConsulta.toLowerCase() + "%') ORDER BY Matricula.matricula";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO);
	}

	public List consultarPorNomeAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ReativacaoMatricula.* FROM ReativacaoMatricula inner join matricula on matricula.matricula = ReativacaoMatricula.matricula inner join pessoa on pessoa.codigo = matricula.aluno WHERE upper(pessoa.nome) like('"
				+ valorConsulta.toUpperCase() + "%') ORDER BY pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO);
	}
	
	@Override
	public List consultarPorRegistroAcademicoAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT ReativacaoMatricula.* FROM ReativacaoMatricula inner join matricula on matricula.matricula = ReativacaoMatricula.matricula inner join pessoa on pessoa.codigo = matricula.aluno WHERE upper(pessoa.registroAcademico) like('"
				+ valorConsulta.toUpperCase() + "%') ORDER BY pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO);
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorSituacao(java.lang.String, boolean, int)
	 */
	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ReativacaoMatricula.* FROM ReativacaoMatricula, Requerimento WHERE ReativacaoMatricula.Requerimento = Requerimento.codigo and lower (Requerimento.situacao) = ('"
				+ valorConsulta.toLowerCase() + "') ORDER BY Requerimento.situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO));
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorDescricao(java.lang.String, boolean, int)
	 */
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ReativacaoMatricula WHERE lower (descricao) like('" + valorConsulta.toLowerCase()
				+ "%') ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO));
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorData(java.util.Date, java.util.Date, boolean, int)
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ReativacaoMatricula WHERE ((data >= '" + Uteis.getDataJDBC(prmIni)
				+ "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO));
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorDataUnidadeEnsino(java.util.Date, java.util.Date, java.lang.String, boolean, int)
	 */
	public List consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ReativacaoMatricula "
				+ "LEFT JOIN matricula ON ReativacaoMatricula.matricula = matricula.matricula "
				+ "LEFT JOIN unidadeEnsino ON matricula.unidadeEnsino = unidadeEnsino.codigo "
				+ "WHERE ((ReativacaoMatricula.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (ReativacaoMatricula.data <= '"
				+ Uteis.getDataJDBC(prmFim) + "')) AND unidadeEnsino.nome = '" + unidadeEnsino
				+ "' ORDER BY ReativacaoMatricula.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO));
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean, int)
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ReativacaoMatricula WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ReativacaoMatriculaVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontaDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontaDados, usuario,configuracaoFinanceiroVO));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ReativacaoMatriculaVO</code>.
	 * 
	 * @return O objeto da classe <code>ReativacaoMatriculaVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ReativacaoMatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ReativacaoMatriculaVO obj = new ReativacaoMatriculaVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
                obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getRequerimento().setCodigo(new Integer(dadosSQL.getInt("requerimento")));
                obj.getTrancamento().setCodigo(new Integer(dadosSQL.getInt("trancamento")));
                obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosMatricula(obj, nivelMontarDados, usuario);
			montarDadosRequerimento(obj, usuario,configuracaoFinanceiroVO);
			return obj;
		}
		obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosRequerimento(obj, usuario,configuracaoFinanceiroVO);
		montarDadosResponsavelAutorizacao(obj, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>ReativacaoMatriculaVO</code>.
	 * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelAutorizacao(ReativacaoMatriculaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>RequerimentoVO</code> relacionado ao objeto
	 * <code>ReativacaoMatriculaVO</code>. Faz uso da chave primária da classe
	 * <code>RequerimentoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosRequerimento(ReativacaoMatriculaVO obj, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		if (obj.getRequerimento().getCodigo().intValue() == 0) {
			return;
		}
		obj.setRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario,configuracaoFinanceiroVO));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>ReativacaoMatriculaVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatricula(ReativacaoMatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
	 */
	public ReativacaoMatriculaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ReativacaoMatricula WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ReativacaoMatricula.idEntidade;
	}

	/**
	 * @see negocio.facade.jdbc.academico.ReativacaoMatriculaInterfaceFacade#setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		ReativacaoMatricula.idEntidade = idEntidade;
	}
	
	public Integer codigoUltimaReativacaoPorMatricula (String matricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT codigo from reativacaomatricula WHERE matricula = '").append(matricula).append("' AND situacao = 'FD' ORDER BY codigo desc limit 1");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (resultado.next()) {
			Integer codigo = new Integer(resultado.getInt("codigo"));
			if (Uteis.isAtributoPreenchido(codigo)) {
				return codigo;
			}
		}
		return 0;
	}
	
	@Override
	public List consultarReativacaoMatricula(String valorConsulta, Date prmIni, Date prmFim, DataModelo dataModelo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(*) over() as qtde_total_registros, ReativacaoMatricula.* FROM ReativacaoMatricula ");
		if (dataModelo.getCampoConsulta().equals("nomeAluno") || dataModelo.getCampoConsulta().equals("matriculaMatricula")) {
			sql.append(" INNER JOIN matricula    ON matricula.matricula    = ReativacaoMatricula.matricula ");
		}
		if (dataModelo.getCampoConsulta().equals("nomeAluno")) {
			sql.append(" INNER JOIN pessoa       ON pessoa.codigo          = matricula.aluno ");
		} else if (dataModelo.getCampoConsulta().equals("codigoRequerimento")) {
			sql.append(" INNER JOIN requerimento ON requerimento.codigo = ReativacaoMatricula.requerimento ");
		} else if (dataModelo.getCampoConsulta().equals("nomePessoa")) {
			sql.append(" INNER JOIN usuario      ON usuario.codigo = ReativacaoMatricula.responsavelautorizacao ");
		}
		sql.append(" WHERE ");
		if (dataModelo.getCampoConsulta().equals("codigo")) {
			sql.append(" codigo >= ? ");
		} else if (dataModelo.getCampoConsulta().equals("data")) {
			sql.append(" ((data >= ? ) and (data <= ? )) ");
		} else if (dataModelo.getCampoConsulta().equals("descricao")) {
			sql.append(" lower (descricao) ilike(?) ");
		} else if (dataModelo.getCampoConsulta().equals("nomeAluno")) {
			sql.append(" upper(pessoa.nome) ilike(?) ");
		} else if (dataModelo.getCampoConsulta().equals("matriculaMatricula")) {
			sql.append(" ReativacaoMatricula.matricula = Matricula.matricula AND lower (Matricula.matricula) ilike(?) ");
		} else if (dataModelo.getCampoConsulta().equals("codigoRequerimento")) {
			sql.append(" ReativacaoMatricula.Requerimento = Requerimento.codigo and Requerimento.codigo >= ? ");
		} else if (dataModelo.getCampoConsulta().equals("nomePessoa")) {
			sql.append(" ReativacaoMatricula.responsavelAutorizacao = usuario.codigo and lower (Usuario.nome) ilike(?) ");
		} else if (dataModelo.getCampoConsulta().equals("situacao")) {
			sql.append(" ReativacaoMatricula.situacao in (?)");
		}
		if (dataModelo.getCampoConsulta().equals("codigo")) {
			sql.append(" ORDER BY codigo");
		} else if (dataModelo.getCampoConsulta().equals("data")) {
			sql.append(" ORDER BY data");
		} else if (dataModelo.getCampoConsulta().equals("descricao")) {
			sql.append(" ORDER BY descricao");
		} else if (dataModelo.getCampoConsulta().equals("situacao")) {
			sql.append( "ORDER BY ReativacaoMatricula.situacao");
		} else if (dataModelo.getCampoConsulta().equals("nomeAluno")) {
			sql.append(" ORDER BY pessoa.nome");
		} else if (dataModelo.getCampoConsulta().equals("matriculaMatricula")) {
			sql.append(" ORDER BY Matricula.matricula");
		} else if (dataModelo.getCampoConsulta().equals("codigoRequerimento")) {
			sql.append(" ORDER BY Requerimento.codigo");
		} else if (dataModelo.getCampoConsulta().equals("nomePessoa")) {
			sql.append(" ORDER BY Usuario.nome ");
		}
		sql.append(" LIMIT ").append(dataModelo.getLimitePorPagina());
		sql.append(" OFFSET ").append(dataModelo.getOffset());
		dataModelo.setTotalRegistrosEncontrados(0);
		SqlRowSet tabelaResultado = null;
		if (dataModelo.getCampoConsulta().equals("data")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Uteis.getDataJDBC(prmIni), Uteis.getDataJDBC(prmFim));
		} else if (dataModelo.getCampoConsulta().equals("situacao") || dataModelo.getCampoConsulta().equals("codigoRequerimento") || dataModelo.getCampoConsulta().equals("codigo")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), ((dataModelo.getCampoConsulta().equals("codigoRequerimento") || dataModelo.getCampoConsulta().equals("codigo")) ? Integer.valueOf(valorConsulta) : valorConsulta));
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		}
		if (tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario,configuracaoFinanceiroVO));
	}
}
