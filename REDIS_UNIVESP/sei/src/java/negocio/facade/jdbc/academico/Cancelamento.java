package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.CancelamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>CancelamentoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>CancelamentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see CancelamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Cancelamento extends ControleAcesso implements CancelamentoInterfaceFacade {

	protected static String idEntidade;
	public static final long serialVersionUID = 1L;

	public Cancelamento() throws Exception {
		super();
		setIdEntidade("Cancelamento");
	}

	public CancelamentoVO novo() throws Exception {
		Cancelamento.incluir(getIdEntidade());
		CancelamentoVO obj = new CancelamentoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>CancelamentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>CancelamentoVO</code> que será gravado
	 *            no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CancelamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, Boolean verificarPermissao) throws Exception {
		try {
			if(verificarPermissao) {
				Cancelamento.incluir(getIdEntidade(), true, usuario);
			}
			validarDados(obj, configuracaoGeralSistemaVO);
			final String sql = "INSERT INTO Cancelamento( data, descricao, matricula, codigoRequerimento, justificativa, responsavelAutorizacao, situacao, motivoCancelamentoTrancamento ) " + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(2, obj.getDescricao());
					sqlInserir.setString(3, obj.getMatricula().getMatricula());
					if ((obj.getCodigoRequerimento().getCodigo() != null) && (obj.getCodigoRequerimento().getCodigo().intValue() != 0)) {
						sqlInserir.setInt(4, obj.getCodigoRequerimento().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setString(5, obj.getJustificativa());
					sqlInserir.setInt(6, obj.getResponsavelAutorizacao().getCodigo().intValue());
					sqlInserir.setString(7, obj.getSituacao());
					sqlInserir.setInt(8, obj.getMotivoCancelamentoTrancamento().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			alterarSituacaoAcademicaMatriculaCancelamento(obj, configuracaoFinanceiroVO, usuario);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemCancelamentoMatricula(obj.getMatricula().getAluno(), obj.getMatricula(), usuario);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Responsável por executar a alteração da situação acadêmica da matrícula
	 * no ato da inclusão.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param cancelamentoVO
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarSituacaoAcademicaMatriculaCancelamento(CancelamentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		obj.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
		alterarSituacaoCancelamento(obj.getCodigo(), obj.getSituacao(), usuario);
		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
		getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluirCancelamentoAluno(obj, usuario, configuracaoGeralSistemaVO);
		if (Uteis.isAtributoPreenchido(obj.getMatriculaPeriodoVO())) {
			obj.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor());
			getFacadeFactory().getTrancamentoFacade().alterarSituacaoAcademicaMatricula(obj.getMatriculaPeriodoVO(), obj.getHistoricoVOs(), obj.getCodigoRequerimento(), obj.getData(), OrigemFechamentoMatriculaPeriodoEnum.CANCELAMENTO, obj.getCodigo(), false, false, configuracaoFinanceiroVO, obj.getInativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() , obj.getInativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() ,usuario);
		} else {
			if (!obj.getMatricula().getCurso().getIntegral()) {
				MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(obj.getMatricula().getMatricula(), "'AT', 'PR', 'TR'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, configuracaoFinanceiroVO, usuario);
				matriculaPeriodoVO.setMatriculaVO(obj.getMatricula());
				matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor());
				getFacadeFactory().getTrancamentoFacade().alterarSituacaoAcademicaMatricula(matriculaPeriodoVO, obj.getHistoricoVOs(), obj.getCodigoRequerimento(), obj.getData(), OrigemFechamentoMatriculaPeriodoEnum.CANCELAMENTO, obj.getCodigo(), false, false, configuracaoFinanceiroVO, obj.getInativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() , obj.getInativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() ,usuario);
			} else {
				List listaMatriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPorMatriculaSituacoes(obj.getMatricula().getMatricula(), "'AT', 'PR', 'TR'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, configuracaoFinanceiroVO, usuario);
				Iterator i = listaMatriculaPeriodoVO.iterator();
				while (i.hasNext()) {
					MatriculaPeriodoVO matriculaPeriodoVO = (MatriculaPeriodoVO)i.next();
					matriculaPeriodoVO.setMatriculaVO(obj.getMatricula());
					matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.CANCELADA.getValor());
					getFacadeFactory().getTrancamentoFacade().alterarSituacaoAcademicaMatricula(matriculaPeriodoVO, obj.getHistoricoVOs(), obj.getCodigoRequerimento(), obj.getData(), OrigemFechamentoMatriculaPeriodoEnum.CANCELAMENTO, obj.getCodigo(), false, false, configuracaoFinanceiroVO,obj.getInativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() , obj.getInativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional() , usuario);				
				}			
			}
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>CancelamentoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>CancelamentoVO</code> que será alterada
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CancelamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception {
		try {
			validarDados(obj, configuracaoGeralSistemaVO);
			Cancelamento.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE Cancelamento set data=?, descricao=?, matricula=?, codigoRequerimento=?, justificativa=?, responsavelAutorizacao=?, situacao=?, motivoCancelamentoTrancamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(2, obj.getDescricao());
					sqlAlterar.setString(3, obj.getMatricula().getMatricula());
					if ((obj.getCodigoRequerimento().getCodigo() != null) && (obj.getCodigoRequerimento().getCodigo().intValue() != 0)) {
						sqlAlterar.setInt(4, obj.getCodigoRequerimento().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setString(5, obj.getJustificativa());
					sqlAlterar.setInt(7, obj.getResponsavelAutorizacao().getCodigo().intValue());
					sqlAlterar.setString(7, obj.getSituacao());
					sqlAlterar.setInt(8, obj.getMotivoCancelamentoTrancamento().getCodigo());
					sqlAlterar.setInt(9, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>CancelamentoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>CancelamentoVO</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CancelamentoVO obj, UsuarioVO usuario) throws Exception {
		try {
			Cancelamento.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM Cancelamento WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoCancelamento(Integer codigo, String situacao, UsuarioVO usuario) {
		try {
			String sqlStr = "UPDATE cancelamento SET situacao = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacao, codigo });
		} catch (Exception e) {
		}
	}

	public List<CancelamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (sem_acentos(p.nome)) ILIKE UPPER(sem_acentos('%" + valorConsulta.toUpperCase() + "%')) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}
	
	public List<CancelamentoVO> consultaRapidaPorNomeAlunoUltimoCancelamento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = new StringBuffer();
		sql.append(getSQLPadraoConsultaBasica());
		sql.append(" WHERE (sem_acentos(p.nome)) ILIKE UPPER(sem_acentos('%" + valorConsulta.toUpperCase() + "%')) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		sql.append(" order by c.matricula, c.codigo desc ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}
	
	
//	@Override
//	public List<CancelamentoVO> consultaRapidaPorRegistroAcademicoUltimoCancelamento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = 	getSQLPadraoConsultaBasica();
//		sql.append(" WHERE sem_acentos(p.registroAcademico) iLIKE sem_acentos(?) ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		sql.append(" order by  p.registroAcademico , m.matricula , c.codigo desc ");
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), PERCENT+ valorConsulta.toUpperCase()+PERCENT);
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<CancelamentoVO> consultaRapidaPorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" inner join turma as t on t.codigo = mp.turma ");
//		sql.append(" WHERE sem_acentos(t.identificadorturma) iLIKE sem_acentos(?) ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+PERCENT);
//		return montarDadosConsultaBasica(resultado);
//	}

	public List<CancelamentoVO> consultaRapidaPorTipoJustificativa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		
		sql.append(" WHERE sem_acentos(motivoCancelamentoTrancamento.nome) iLIKE sem_acentos(?) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+PERCENT);
		return montarDadosConsultaBasica(resultado);
	}
	
//	public List<CancelamentoVO> consultaRapidaPorTipoJustificativaUltimoCancelamento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();		
//		sql.append(" WHERE sem_acentos(motivoCancelamentoTrancamento.nome) iLIKE sem_acentos(?) ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		sql.append(" and c.codigo = (select cancelamento.codigo from cancelamento where cancelamento.matricula = m.matricula order by cancelamento.codigo desc limit 1) ");
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+PERCENT);
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<CancelamentoVO> consultaRapidaPorCodigoRequerimento(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE req.codigo >= " + valorConsulta + " ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<CancelamentoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE c.codigo >= " + valorConsulta + " ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

	public CancelamentoVO consultarPorCodigoRequerimento(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT Cancelamento.* FROM Cancelamento, Requerimento WHERE Cancelamento.codigoRequerimento = Requerimento.codigo and Requerimento.codigo = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			return new CancelamentoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	public List<CancelamentoVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER(c.matricula) LIKE '" + valorConsulta.toUpperCase() + "%' ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}
	
//	public List<CancelamentoVO> consultaRapidaPorMatriculaUltimoCancelamento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = new StringBuffer();
//		sql.append(getSQLPadraoConsultaBasica());		
//		sql.append(" WHERE UPPER(c.matricula) iLIKE (?) ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		sql.append(" order by c.matricula, c.codigo desc limit 1 ");
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+PERCENT);
//		return montarDadosConsultaBasica(resultado);
//	}

	/**
	 * Responsável por realizar uma consulta de <code>Cancelamento</code>
	 * através do valor do atributo <code>String situacao</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CancelamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<CancelamentoVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Cancelamento.* FROM Cancelamento where lower (Cancelamento.situacao) like ('" + valorConsulta.toLowerCase() + "%') ORDER BY Cancelamento.situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

//	public List<CancelamentoVO> consultaRapidaPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE UPPER(c.situacao) LIKE '" + valorConsulta.toUpperCase() + "' ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

	public List<CancelamentoVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cancelamento WHERE lower (descricao) like('" + valorConsulta.toLowerCase() + "%') ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

//	public List<CancelamentoVO> consultaRapidaPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		StringBuffer sql = getSQLPadraoConsultaBasica();
//		sql.append(" WHERE UPPER(c.descricao) LIKE '" + valorConsulta.toUpperCase() + "' ");
//		if (unidadeEnsino.intValue() != 0) {
//			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
//		}
//		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		return montarDadosConsultaBasica(resultado);
//	}

//	public List<CancelamentoVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
//		String sqlStr = "SELECT * FROM Cancelamento WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
//	}

	public List<CancelamentoVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE c.data >= '" + Uteis.getDataJDBC(prmIni) + "' AND c.data <= " + Uteis.getDataJDBC(prmFim) + "' ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(resultado);
	}

	public List<CancelamentoVO> consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cancelamento " + "LEFT JOIN matricula ON cancelamento.matricula = matricula.matricula " + "LEFT JOIN unidadeEnsino ON matricula.unidadeEnsino = unidadeEnsino.codigo " + "WHERE ((Cancelamento.data >= '" + Uteis.getDataJDBC(prmIni) + "') and (Cancelamento.data <= '" + Uteis.getDataJDBC(prmFim) + "')) AND unidadeEnsino.nome = '" + unidadeEnsino + "' ORDER BY Cancelamento.data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Cancelamento</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>CancelamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<CancelamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Cancelamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>CancelamentoVO</code> resultantes da consulta.
	 */
	public static List<CancelamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontaDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<CancelamentoVO> vetResultado = new ArrayList<CancelamentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontaDados, configuracaoFinanceiroVO, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>CancelamentoVO</code>.
	 *
	 * @return O objeto da classe <code>CancelamentoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static CancelamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		CancelamentoVO obj = new CancelamentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getCodigoRequerimento().setCodigo(new Integer(dadosSQL.getInt("codigoRequerimento")));
		obj.getResponsavelAutorizacao().setCodigo(new Integer(dadosSQL.getInt("responsavelAutorizacao")));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
		obj.setDataEstorno(dadosSQL.getTimestamp("dataEstorno"));
		obj.getResponsavelEstorno().setCodigo(new Integer(dadosSQL.getInt("responsavelEstorno")));		
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosResponsavelAutorizacao(obj, usuario);
			montarDadosResponsavelEstorno(obj, usuario);
			montarDadosMatricula(obj, nivelMontarDados, usuario);
			montarDadosCodigoRequerimento(obj, configuracaoFinanceiroVO, usuario);
			return obj;
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosMatricula(obj, nivelMontarDados, usuario);
			return obj;
		}
		montarDadosResponsavelAutorizacao(obj, usuario);
		montarDadosResponsavelEstorno(obj, usuario);
		montarDadosMatricula(obj, nivelMontarDados, usuario);
		montarDadosCodigoRequerimento(obj, configuracaoFinanceiroVO, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>CancelamentoVO</code>.
	 * Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelAutorizacao(CancelamentoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			obj.setResponsavelAutorizacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}
	
	public static void montarDadosResponsavelEstorno(CancelamentoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelEstorno().getCodigo().intValue() == 0) {
			obj.setResponsavelEstorno(new UsuarioVO());
			return;
		}
		obj.setResponsavelEstorno(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelEstorno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>RequerimentoVO</code> relacionado ao objeto
	 * <code>CancelamentoVO</code>. Faz uso da chave primária da classe
	 * <code>RequerimentoVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCodigoRequerimento(CancelamentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if (obj.getCodigoRequerimento().getCodigo().intValue() == 0) {
			obj.setCodigoRequerimento(new RequerimentoVO());
			return;
		}
		obj.setCodigoRequerimento(getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigoRequerimento().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, configuracaoFinanceiroVO));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>MatriculaVO</code> relacionado ao objeto
	 * <code>CancelamentoVO</code>. Faz uso da chave primária da classe
	 * <code>MatriculaVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMatricula(CancelamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
			obj.setMatricula(new MatriculaVO());
			return;
		}
		obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>CancelamentoVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public CancelamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Cancelamento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Cancelamento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Cancelamento.idEntidade = idEntidade;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT count(*) over() qtdeRegistro, c.codigo, c.descricao, c.situacao, c.data, m.matricula, p.nome,  p.registroAcademico, motivoCancelamentoTrancamento.nome as motivoCancelamentoTrancamento, unidadeensino.nome as unidadeensino ");
		getBodyPadraoConsultaBasica(sql);
		return sql;
	}

	private void getBodyPadraoConsultaBasica(StringBuffer sql) {
		sql.append("FROM cancelamento c ");
		sql.append("LEFT JOIN matricula m ON m.matricula = c.matricula ");
		sql.append("LEFT JOIN unidadeensino ON m.unidadeensino = unidadeensino.codigo ");
		sql.append("LEFT JOIN pessoa p ON p.codigo = m.aluno ");
		sql.append("LEFT JOIN matriculaperiodo mp ON mp.matricula = c.matricula and mp.codigo = (select mp2.codigo from matriculaperiodo mp2 where mp2.matricula = mp.matricula order by case when mp2.data <= c.data then 0 else  1 end, mp2.data desc limit 1) ");		
		sql.append("LEFT JOIN requerimento req ON req.codigo = c.codigorequerimento ");
		sql.append("LEFT JOIN motivoCancelamentoTrancamento ON motivoCancelamentoTrancamento.codigo = c.motivoCancelamentoTrancamento ");
		sql.append("LEFT JOIN turma tu ON tu.codigo = mp.turma ");
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT c.*, m.matricula AS matriculaAluno, p.codigo AS codigoPessoa, p.nome AS nomePessoa, req.codigo AS codigoRequerimento, u.codigo AS codigoResponsavel, u.nome AS nomeResponsavel ");
		sql.append("FROM cancelamento c ");
		sql.append("LEFT JOIN matricula m ON m.matricula = c.matricula ");
		sql.append("LEFT JOIN pessoa p ON p.codigo = m.aluno ");
		sql.append("LEFT JOIN requerimento req ON req.codigo = c.codigorequerimento ");
		sql.append("LEFT JOIN usuario u ON u.codigo = c.responsavelAutorizacao ");
		return sql;
	}

	public void carregarDados(CancelamentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		carregarDados(obj, NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuario);
	}

	public void carregarDados(CancelamentoVO obj, NivelMontarDados nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), Boolean.FALSE, usuario);
			montarDadosBasico(obj, resultado);
		}
		if ((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), Boolean.FALSE, usuario);
			montarDadosCompleto(obj, resultado, configuracaoFinanceiroVO, usuario);
		}
	}

	private void montarDadosBasico(CancelamentoVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("nome"));
		obj.getMatricula().getAluno().setRegistroAcademico(dadosSQL.getString("registroAcademico"));
		obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino"));
		obj.getMotivoCancelamentoTrancamento().setNome(dadosSQL.getString("motivoCancelamentoTrancamento"));
	}

	private void montarDadosCompleto(CancelamentoVO obj, SqlRowSet dadosSQL, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getCodigoRequerimento().setCodigo(dadosSQL.getInt("codigoRequerimento"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.getMotivoCancelamentoTrancamento().setCodigo(dadosSQL.getInt("motivoCancelamentoTrancamento"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("codigoPessoa"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("nomePessoa"));
		obj.getCodigoRequerimento().setCodigo(dadosSQL.getInt("codigoRequerimento"));
		obj.getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("codigoResponsavel"));
		obj.getResponsavelAutorizacao().setNome(dadosSQL.getString("nomeResponsavel"));
		obj.setDataEstorno(dadosSQL.getTimestamp("dataEstorno"));
		obj.getResponsavelEstorno().setCodigo(new Integer(dadosSQL.getInt("responsavelEstorno")));
	}

	public List<CancelamentoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<CancelamentoVO> vetResultado = new ArrayList<CancelamentoVO>(0);
		while (tabelaResultado.next()) {
			CancelamentoVO obj = new CancelamentoVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<CancelamentoVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<CancelamentoVO> vetResultado = new ArrayList<CancelamentoVO>(0);
		while (tabelaResultado.next()) {
			CancelamentoVO obj = new CancelamentoVO();
			montarDadosCompleto(obj, tabelaResultado, configuracaoFinanceiroVO, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (c.codigo = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Cancelamento ).");
		}
		tabelaResultado.beforeFirst();
		return tabelaResultado;
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaCompleta();
		sql.append(" WHERE (c.codigo = ?)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Cancelamento ).");
		}
		tabelaResultado.beforeFirst();
		return tabelaResultado;
	}
	
	@Override
	public void validarDadosAntesImpressao(CancelamentoVO cancelamentoVO, Integer textoPadrao) throws Exception {		
		if (cancelamentoVO.getMatricula() == null || !Uteis.isAtributoPreenchido(cancelamentoVO.getMatricula().getMatricula())) {
			throw new Exception("O Aluno deve ser informado para geração do relatório.");
		}
		if(!Uteis.isAtributoPreenchido(textoPadrao)){
			throw new Exception("O Texto Padrão Declaração deve ser informado para geração do relatório.");
		}
	}


	@Override
	public String imprimirDeclaracaoCancelamento(CancelamentoVO cancelamentoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception {
		String caminhoRelatorio = "";
		try {
			ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
			impressaoContratoVO.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContratoVO.setGerarNovoArquivoAssinado(true);
			impressaoContratoVO.setMatriculaVO(cancelamentoVO.getMatricula());
			impressaoContratoVO.getMatriculaVO().setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(cancelamentoVO.getMatricula().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(cancelamentoVO.getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			impressaoContratoVO.setTurmaVO(impressaoContratoVO.getMatriculaPeriodoVO().getTurma());
			impressaoContratoVO.setTextoPadraoDeclaracao(textoPadraoDeclaracaoVO.getCodigo());						
			textoPadraoDeclaracaoVO.setTexto(textoPadraoDeclaracaoVO.substituirTag(textoPadraoDeclaracaoVO.getTexto(), "[(70){}Justificativa_Aluno]", cancelamentoVO.getJustificativa(), "", 70));
			textoPadraoDeclaracaoVO.substituirTag(impressaoContratoVO, usuario);
			if (textoPadraoDeclaracaoVO.getTipoDesigneTextoEnum().isHtml()){
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("textoRelatorio", textoPadraoDeclaracaoVO.getTexto());
			} else {
				caminhoRelatorio = getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContratoVO, textoPadraoDeclaracaoVO, "", true, config, usuario);
				getFacadeFactory().getImpressaoDeclaracaoFacade().gravarImpressaoContrato(impressaoContratoVO);
			}
			return caminhoRelatorio;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por executar a persistência dos dados pertinentes a
	 * CancelamentoVO.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @param configuracaoGeralSistemaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void persistir(final CancelamentoVO cancelamentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		if (cancelamentoVO.isNovoObj()) {
			incluir(cancelamentoVO, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO, true);
		} else {
			alterar(cancelamentoVO, configuracaoGeralSistemaVO, usuarioVO);
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CancelamentoVO</code>. Todos os tipos de consistência de dados são
	 * e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarSituacaoRequerimento(RequerimentoVO obj) throws ConsistirException {
		if (obj.getSituacao().equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor())) {
			throw new ConsistirException("Requerimento especificado está aguardando pagamento.");
		}
		if (obj.getMatricula().getSituacao().equals("CA")) {
			throw new ConsistirException("Matrícula especificada já está cancelada.");
		}
		if (obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor()) || obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
			throw new ConsistirException("Requerimento especificado já está finalizado.");
		}
	}

	public void validarDados(CancelamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws ConsistirException {
		if (!configuracaoGeralSistemaVO.getPermiteCancelamentoSemRequerimento() && !obj.getCancelamentoPorOutraMatriculaOnline()) {
			if (!Uteis.isAtributoPreenchido(obj.getCodigoRequerimento())) {
				throw new ConsistirException("O campo CÓDIGO REQUERIMENTO (Cancelamento) deve ser informado.");
			}
		}
		if (!Uteis.isAtributoPreenchido(obj.getData())) {
			throw new ConsistirException("O campo DATA (Cancelamento) deve ser informado.");
		}
		if (obj.getCodigoRequerimento().getSituacao().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO (Cancelamento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getMatricula().getMatricula())) {
			throw new ConsistirException("O campo MATRÍCULA (Cancelamento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getJustificativa())) {
			throw new ConsistirException("O campo JUSTIFICATIVA (Cancelamento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getMotivoCancelamentoTrancamento())) {
			throw new ConsistirException("O campo MOTIVO CANCELAMENTO (Cancelamento) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getResponsavelAutorizacao())) {
			throw new ConsistirException("O campo RESPONSÁVEL AUTORIZACÃO (Cancelamento) deve ser informado.");
		}
	}

	/**
	 * Responsável por executar a validação se a matrícula está apta a realizar
	 * o cancelamento, levando em consideração sua situação, a situação
	 * financeira e e se existe pendência em empréstimo na biblioteca.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	public void executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().validarExistenciaMatriculaPeriodoAptaRealizarTrancamentoCancelamentoTransferencia(matriculaVO.getMatricula());
		boolean existePendenciaFinanceira = getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(matriculaVO.getMatricula(), usuarioVO);
		if (existePendenciaFinanceira && !matriculaVO.getCanceladoFinanceiro()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Cancelamento_matriculaPendenteFinanceira"));
		}
		getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().validarDadosPendenciaEmprestimoBiblioteca(matriculaVO, false, false, true, false, false, false, configuracaoGeralSistemaVO, usuarioVO);
	}
	
	/**
	 * Responsável por executar o estorno do cancelamento de matrícula
	 * 
	 * @author Alessandro - 18/01/2016
	 * @param cancelamentoVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarEstorno(CancelamentoVO cancelamentoVO, UsuarioVO usuarioVO) throws Exception {
		realizarEstornoCancelamento(cancelamentoVO, usuarioVO);
		Integer ultimaMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaCodigoUltimaMatriculaPeriodoPorMatricula(cancelamentoVO.getMatricula().getMatricula(), false, usuarioVO);
		getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaEstornoCancelamento(cancelamentoVO.getMatricula().getMatricula(), usuarioVO);
		
		if(Uteis.isAtributoPreenchido(cancelamentoVO.getMatricula().getBloqueioPorSolicitacaoLiberacaoMatricula()) && cancelamentoVO.getMatricula().getBloqueioPorSolicitacaoLiberacaoMatricula()) {
			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoEstornoCancelamentoSolicitarReconsideracaoSolicitacao(ultimaMatriculaPeriodo, usuarioVO);
		}else {
			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoEstornoCancelamento(ultimaMatriculaPeriodo, usuarioVO);			
		}
		
		List <HistoricoVO> historicos = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaCancelada(ultimaMatriculaPeriodo, usuarioVO);
		if (!historicos.isEmpty()) {
			for(HistoricoVO historicoVO: historicos){
				if(historicoVO.getSituacao().equals(SituacaoHistorico.CANCELADO.getValor())){
					historicoVO.setSituacao("CS");
				}
			}
			getFacadeFactory().getHistoricoFacade().verificarAprovacaoAlunos(historicos, false, true, usuarioVO);
			
		}
		UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(cancelamentoVO.getMatricula().getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		realizarAtivacaoUsuarioLdap(cancelamentoVO, usuario,usuarioVO);
		realizarAtivacaoBlackboardPessoaEmailInstitucional(cancelamentoVO, usuario);
		getFacadeFactory().getIntegracaoMestreGRInterfaceFacade().incluirEstornoCancelamentoAluno(cancelamentoVO, usuarioVO, getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, usuario));
	}

	private void realizarAtivacaoBlackboardPessoaEmailInstitucional(CancelamentoVO cancelamentoVO, UsuarioVO usuario) throws Exception {
		PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorMatricula(cancelamentoVO.getMatricula().getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtivacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuario);
			if(pessoaEmailInstitucionalVO.getStatusAtivoInativoEnum().equals(StatusAtivoInativoEnum.INATIVO)) {
				pessoaEmailInstitucionalVO.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
				getFacadeFactory().getPessoaEmailInstitucionalFacade().alterarSituacaoStatusAtivoInativoEnum(pessoaEmailInstitucionalVO, usuario);
			}
		}
	}

	private void realizarAtivacaoUsuarioLdap(CancelamentoVO cancelamentoVO, UsuarioVO usuario , UsuarioVO usuarioLogado) throws Exception {
		if(Uteis.isAtributoPreenchido(usuario)) {
			if(Uteis.isAtributoPreenchido(usuario.getPessoa().getRegistroAcademico())) {				
				getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( usuario, usuario.getPessoa().getRegistroAcademico(),usuario.getPessoa().getRegistroAcademico(),false, usuarioLogado);
				usuario.setUsername(usuario.getPessoa().getRegistroAcademico());
				getFacadeFactory().getUsuarioFacade().alterarBooleanoResetouSenhaPrimeiroAcesso(false, usuario, false, usuarioLogado);	

			}			
			JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuario, cancelamentoVO.getMatricula(), true,usuarioLogado);
			Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
			jobSincronizarCancelamento.start();
		}
	}
	
	private void realizarEstornoCancelamento(final CancelamentoVO cancelamentoVO, final UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sqlStr = new StringBuilder("update cancelamento as ca set situacao = 'ES', responsavelEstorno = ?, dataEstorno = ? from (");
		sqlStr.append("select c.codigo from cancelamento as c ");
		sqlStr.append("inner join matricula as m on m.matricula = c.matricula and m.situacao = 'CA' ");
		sqlStr.append("where c.matricula = ? and c.situacao = 'FD' ");
		sqlStr.append("order by c.data desc limit 1");
		sqlStr.append(") as x where ca.codigo = x.codigo");
		int alterados = getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				sqlAlterar.setInt(1, cancelamentoVO.getResponsavelEstorno().getCodigo());
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(cancelamentoVO.getDataEstorno()));
				sqlAlterar.setString(3, cancelamentoVO.getMatricula().getMatricula());
				return sqlAlterar;
			}
		});
		if (alterados < 1) {
			throw new Exception("Matrícula do cancelamento não atende condições para estorno!");
		}
	}
	
	public CancelamentoVO consultarPorMatricula(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sql = getSQLPadraoConsultaCompleta();
		sql.append(" WHERE UPPER(c.matricula) LIKE '" + valorConsulta.toUpperCase() + "' ");
		sql.append("order by (c.matricula), c.data desc, c.codigo desc limit 1");
		
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!resultado.next()) {
			return new CancelamentoVO();
		}
		return (montarDados(resultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}
	
	
	@Override
	public void realizarCancelamentoMatriculaAtivaPorOutroCursoMesmoNivelEducacional(MatriculaVO matriculaVO , ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO  , UsuarioVO usuarioVO) throws Exception {
		try {
				if (!Uteis.isAtributoPreenchido(configuracaoGeralSistema.getJustificativaCancelamentoPorOutraMatricula())) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_MatriculaOnline_MotivoPadraoCancelamentoNaoInformado"));					
				}
				if (!Uteis.isAtributoPreenchido(configuracaoGeralSistema.getMotivoPadraoCancelamentoOutraMatricula())) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_MatriculaOnline_JustificativaPadraoCancelamentoNaoInformado"));
				}		
			   	CancelamentoVO cancelamentoVO = new CancelamentoVO();
				cancelamentoVO.setJustificativa(configuracaoGeralSistema.getJustificativaCancelamentoPorOutraMatricula());
				cancelamentoVO.setData(new Date());
				cancelamentoVO.setDescricao(configuracaoGeralSistema.getJustificativaCancelamentoPorOutraMatricula());
				cancelamentoVO.setMatricula(matriculaVO);
				cancelamentoVO.setSituacao(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor());
				cancelamentoVO.setResponsavelAutorizacao(usuarioVO);
				cancelamentoVO.setMotivoCancelamentoTrancamento(configuracaoGeralSistema.getMotivoPadraoCancelamentoOutraMatricula());		
				cancelamentoVO.setInativarUsuarioLdapAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional(false);
				cancelamentoVO.setInativarUsuarioBlackBoardAoCancelarMatriculaPorOutroCursoMesmoNivelEducacional(false);
				cancelamentoVO.setCancelamentoPorOutraMatriculaOnline(true);
				getFacadeFactory().getCancelamentoFacade().incluir(cancelamentoVO, configuracaoGeralSistema, configuracaoFinanceiroVO, usuarioVO, false);
				StringBuilder sql  = new StringBuilder("UPDATE historico set situacao = '");
				sql.append(SituacaoVinculoMatricula.CANCELADA.getValor()).append("' ");
				sql.append(" where matricula = ?");
				sql.append(" and situacao in ('CS', 'CE') ");
				getConexao().getJdbcTemplate().update(sql.toString(), matriculaVO.getMatricula());
		} catch (Exception e) {
			throw e;
		}		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoCodigoRequerimentoCancelamento(final Integer codigoRequerimento) throws Exception {
		final StringBuilder sqlStr = new StringBuilder("UPDATE cancelamento SET codigorequerimento = null WHERE codigorequerimento = ? ");
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				sqlAlterar.setInt(1, codigoRequerimento);
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirCancelamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		mrecmp.setCancelamentoVO(new CancelamentoVO());
		mrecmp.getCancelamentoVO().setNovoObj(true);
		mrecmp.getCancelamentoVO().setResponsavelAutorizacao(usuarioVO);
		mrecmp.getCancelamentoVO().setJustificativa("Originado pelo Mapa Registro Evasão Curso - "+mrecmp.getMapaRegistroEvasaoCursoVO().getJustificativa());
		mrecmp.getCancelamentoVO().setCodigoRequerimento(null);
		mrecmp.getCancelamentoVO().setMatricula(mrecmp.getMatriculaPeriodoVO().getMatriculaVO());
		mrecmp.getCancelamentoVO().setData(new Date());
		mrecmp.getCancelamentoVO().setDescricao("Originado pelo Mapa Registro Evasão Curso");
		mrecmp.getCancelamentoVO().setSituacao("FD");
		mrecmp.getCancelamentoVO().setMotivoCancelamentoTrancamento(mrecmp.getMapaRegistroEvasaoCursoVO().getMotivoCancelamentoTrancamento());
		configuracaoGeralSistemaVO.setPermiteCancelamentoSemRequerimento(true);
		if (!getFacadeFactory().getMatriculaPeriodoFacade().executarVerificacaoExisteMatriculaPeriodoRenovadaPorAnoSemestreEvasao(mrecmp.getMatriculaPeriodoVO().getCodigo(), mrecmp.getMapaRegistroEvasaoCursoVO().getAnoRegistroEvasao(), mrecmp.getMapaRegistroEvasaoCursoVO().getSemestreRegistroEvasao(), false, usuarioVO)) {
			realizarMontagemMatriculaPeriodoEvasao(mrecmp, configuracaoFinanceiroVO, usuarioVO);
		} else {
			MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaSemestreAno(mrecmp.getMatriculaPeriodoVO().getMatricula(), mrecmp.getMapaRegistroEvasaoCursoVO().getSemestreRegistroEvasao(), mrecmp.getMapaRegistroEvasaoCursoVO().getAnoRegistroEvasao(), false, NivelMontarDados.BASICO.getValor(), configuracaoFinanceiroVO, usuarioVO);
			matriculaPeriodo.setFinanceiroManual(true);
			matriculaPeriodo.setDataFechamentoMatriculaPeriodo(new Date());
			matriculaPeriodo.setOrigemFechamentoMatriculaPeriodo(OrigemFechamentoMatriculaPeriodoEnum.CANCELAMENTO);
			matriculaPeriodo.setOrigemMapaRegistroTrancamentoAbandono(true);
			mrecmp.getCancelamentoVO().setMatriculaPeriodoVO(matriculaPeriodo);
		}
		persistir(mrecmp.getCancelamentoVO(), configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO);
		if (Uteis.isAtributoPreenchido(mrecmp.getMatriculaPeriodoVO())) {
			mrecmp.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(mrecmp.getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO));
		}
	}
	
	private void realizarMontagemMatriculaPeriodoEvasao(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		MatriculaPeriodoVO obj = new MatriculaPeriodoVO();
		obj.setNovoObj(true);
		obj.setTurma(mrecmp.getMatriculaPeriodoVO().getTurma());
		obj.setPeridoLetivo(mrecmp.getMatriculaPeriodoVO().getPeriodoLetivo());
		obj.setGradeCurricular(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getGradeCurricularAtual());
		obj.setMatricula(mrecmp.getMatriculaPeriodoVO().getMatricula());
		obj.setMatriculaVO(mrecmp.getMatriculaPeriodoVO().getMatriculaVO());
		obj.setSituacao("CO");
		obj.setData(new Date());
		obj.setUnidadeEnsinoCurso(mrecmp.getMatriculaPeriodoVO().getUnidadeEnsinoCurso());
		obj.setUnidadeEnsinoCursoVO(mrecmp.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO());
		obj.setAno(mrecmp.getMapaRegistroEvasaoCursoVO().getAnoRegistroEvasao());
		obj.setSemestre(mrecmp.getMapaRegistroEvasaoCursoVO().getSemestreRegistroEvasao());
		obj.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.TRANCADA.getValor());
		obj.setProcessoMatricula(obj.getProcessoMatriculaCalendarioVO().getProcessoMatricula());
		obj.getProcessoMatriculaVO().setCodigo(obj.getProcessoMatriculaCalendarioVO().getProcessoMatricula());
		obj.setFinanceiroManual(true);
		obj.setDataFechamentoMatriculaPeriodo(new Date());
		obj.setOrigemFechamentoMatriculaPeriodo(OrigemFechamentoMatriculaPeriodoEnum.CANCELAMENTO);
		obj.setOrigemMapaRegistroTrancamentoAbandono(true);
		ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorUnidadeEnsinoCursoAnoSemestre(mrecmp.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO().getCodigo(), mrecmp.getMapaRegistroEvasaoCursoVO().getAnoRegistroEvasao(), mrecmp.getMapaRegistroEvasaoCursoVO().getSemestreRegistroEvasao(), Uteis.NIVELMONTARDADOS_TODOS, false, usuarioVO, null);
		Uteis.checkState(!Uteis.isAtributoPreenchido(processoMatriculaCalendarioVO.getProcessoMatricula()), "Nenhum Calendário de Matrícula Curso (Calendário de Matrícula) definido para o ano de " + obj.getAno() + " no semestre " + obj.getSemestre() + " do trancamento.");
		obj.setProcessoMatriculaCalendarioVO(processoMatriculaCalendarioVO);
		obj.setProcessoMatricula(obj.getProcessoMatriculaCalendarioVO().getProcessoMatricula());
		obj.getProcessoMatriculaVO().setCodigo(obj.getProcessoMatriculaCalendarioVO().getProcessoMatricula());
		getFacadeFactory().getMatriculaPeriodoFacade().incluir(obj, obj.getMatriculaVO(), obj.getProcessoMatriculaCalendarioVO(), configuracaoFinanceiroVO, usuarioVO);
		mrecmp.getCancelamentoVO().setMatriculaPeriodoVO(obj);
	}
	
	@Override
	public void consultaOtimizada(DataModelo controleConsulta, String  tipoJustificativa, String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sql = getSQLPadraoConsultaBasica();
		List<Object> filtros =  new ArrayList<Object>(0);
		if (controleConsulta.getCampoConsulta().equals("matriculaMatricula")) {			
			sql.append(" WHERE sem_acentos(m.matricula) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("registroAcademico")) {			
			sql.append(" WHERE sem_acentos(p.registroAcademico) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("nomeAluno")) {			
			sql.append(" WHERE sem_acentos(p.nome) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("turma")) {			
			sql.append(" WHERE sem_acentos(tu.identificadorTurma) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("descricao")) {			
			sql.append(" WHERE sem_acentos(c.descricao) iLIKE sem_acentos(?) ");
			filtros.add(controleConsulta.getValorConsulta()+PERCENT);
		}
		if (controleConsulta.getCampoConsulta().equals("codigoRequerimento")) {
			if(!Uteis.getIsValorNumerico(controleConsulta.getValorConsulta())) {
				throw new Exception("Deve ser informado apenas valores numéricos para o filtro REQUERIMENTO.");
			}
			sql.append(" WHERE req.codigo = ? ");
			filtros.add(Integer.valueOf(controleConsulta.getValorConsulta()));
		}
		
		if (Uteis.isAtributoPreenchido(tipoJustificativa)) {			
			sql.append(" AND  sem_acentos(motivoCancelamentoTrancamento.nome) ilike sem_acentos(?) ");
			filtros.add(PERCENT+tipoJustificativa+PERCENT);
		}
		if (Uteis.isAtributoPreenchido(situacao)) {			
			sql.append(" AND  (c.situacao) = (?) ");
			filtros.add(situacao);
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND  (m.unidadeensino = " + unidadeEnsino + ") ");
		}
		sql.append(" and ").append(realizarGeracaoWhereDataInicioDataTermino(controleConsulta.getDataIni(), controleConsulta.getDataFim(), "c.data", "c.data", false));
		
		if (controleConsulta.getCampoConsulta().equals("matriculaMatricula")) {
			sql.append(" order by m.matricula, p.nome, c.data desc ");			
		}
		if (controleConsulta.getCampoConsulta().equals("registroAcademico")) {
			sql.append(" order by p.registroAcademico, p.nome, c.data desc ");			
		}
		if (controleConsulta.getCampoConsulta().equals("nomeAluno")) {						
			sql.append(" order by p.nome, c.data desc ");
		}
		if (controleConsulta.getCampoConsulta().equals("descricao")) {						
			sql.append(" order by c.descricao, p.nome, c.data desc ");
		}
		if (controleConsulta.getCampoConsulta().equals("turma")) {						
			sql.append(" order by tu.identificadorTurma, p.nome, c.data desc ");
		}
		
		if (controleConsulta.getCampoConsulta().equals("codigoRequerimento")) {						
			sql.append(" order by p.nome, c.data desc ");
		}
		sql.append(" limit ").append(controleConsulta.getLimitePorPagina());
		sql.append(" offset ").append(controleConsulta.getOffset());
		SqlRowSet resultado =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		
		if(resultado.next()) {
			controleConsulta.setTotalRegistrosEncontrados(resultado.getInt("qtdeRegistro"));
			resultado.beforeFirst();
			controleConsulta.setListaConsulta(montarDadosConsultaBasica(resultado));
		}else {
			controleConsulta.setTotalRegistrosEncontrados(0);
			controleConsulta.setListaConsulta(new ArrayList());
		}
	}

}
