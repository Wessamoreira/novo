package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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

import com.unboundid.ldap.sdk.unboundidds.extensions.GetConnectionIDExtendedRequest;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaCursoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoDocumentoAssinadoPessoaEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.estagio.enumeradores.SituacaoAdicionalEstagioEnum;
import negocio.comuns.estagio.enumeradores.SituacaoEstagioEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoColouGrauProgramacaoFormaturaAluno;
import negocio.comuns.utilitarias.dominios.TiposRequerimento;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ProgramacaoFormaturaAlunoInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ProgramacaoFormaturaAlunoVO</code>. Responsável por implementar operações como incluir, alterar,
 * excluir e consultar pertinentes a classe <code>ProgramacaoFormaturaAlunoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ProgramacaoFormaturaAlunoVO
 * @see ControleAcesso
 * @see ProgramacaoFormatura
 */
@Repository
@Scope("singleton")
@Lazy
public class ProgramacaoFormaturaAluno extends ControleAcesso implements ProgramacaoFormaturaAlunoInterfaceFacade {

	protected static String idEntidade;

	public ProgramacaoFormaturaAluno() throws Exception {
		super();
		setIdEntidade("ProgramacaoFormatura");
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#novo()
	 */
	public ProgramacaoFormaturaAlunoVO novo() throws Exception {
		ProgramacaoFormaturaAluno.incluir(getIdEntidade());
		ProgramacaoFormaturaAlunoVO obj = new ProgramacaoFormaturaAlunoVO();
		return obj;
	}

	public void inicializarDadosProgramacaoFormaturaAlunoParaExpedicaoDiploma(MatriculaVO matriculaVO, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, ColacaoGrauVO colacaoGrauVO) {
		programacaoFormaturaAlunoVO.setColacaoGrau(colacaoGrauVO);
		programacaoFormaturaAlunoVO.setMatricula(matriculaVO);
		programacaoFormaturaAlunoVO.setSituacaoAcademica("OK");
		programacaoFormaturaAlunoVO.setSituacaoDocumentacao("OK");
		programacaoFormaturaAlunoVO.setSituacaoFinanceira("OK");
		programacaoFormaturaAlunoVO.setColouGrau("SI");
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#incluir(negocio.comuns.academico.ProgramacaoFormaturaAlunoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProgramacaoFormaturaAlunoVO obj, UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaAlunoVO.validarDados(obj);
		/**
		  * @author Leonardo Riciolle 
		  * Comentado 29/10/2014
		  *  Classe Subordinada
		*/
		// ProgramacaoFormaturaAluno.incluir(getIdEntidade());
		obj.realizarUpperCaseDados();
		final String sql = "INSERT INTO ProgramacaoFormaturaAluno( programacaoFormatura, matricula, colacaoGrau, colouGrau, requerimento ) VALUES ( ?, ?, ?, ?, ? ) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (obj.getProgramacaoFormatura().intValue() != 0) {
					sqlInserir.setInt(1, obj.getProgramacaoFormatura().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (!obj.getMatricula().getMatricula().equals("")) {
					sqlInserir.setString(2, obj.getMatricula().getMatricula());
				} else {
					sqlInserir.setNull(2, 0);
				}
				if (obj.getColacaoGrau().getCodigo().intValue() != 0) {
					sqlInserir.setInt(3, obj.getColacaoGrau().getCodigo().intValue());
				} else {
					sqlInserir.setNull(3, 0);
				}
				sqlInserir.setString(4, obj.getColouGrau());
				if (obj.getRequerimento().intValue() != 0) {
					sqlInserir.setInt(5, obj.getRequerimento().intValue());
				} else {
					sqlInserir.setNull(5, 0);
				}				
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
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#alterar(negocio.comuns.academico.ProgramacaoFormaturaAlunoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProgramacaoFormaturaAlunoVO obj,  UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaAlunoVO.validarDados(obj);
		/**
		  * @author Leonardo Riciolle 
		  * Comentado 29/10/2014
		  *  Classe Subordinada
		*/
		// ProgramacaoFormaturaAluno.alterar(getIdEntidade());
		obj.realizarUpperCaseDados();
		final String sql = "UPDATE ProgramacaoFormaturaAluno set programacaoFormatura=?, matricula=?, colacaoGrau=?, colouGrau=?, requerimento=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getProgramacaoFormatura().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getProgramacaoFormatura().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (!obj.getMatricula().getMatricula().equals("")) {
					sqlAlterar.setString(2, obj.getMatricula().getMatricula());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				if (obj.getColacaoGrau().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(3, obj.getColacaoGrau().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setString(4, obj.getColouGrau());
				if (obj.getRequerimento().intValue() != 0) {
					sqlAlterar.setInt(5, obj.getRequerimento().intValue());
				} else {
					sqlAlterar.setNull(5, 0);
				}
				
				sqlAlterar.setInt(6, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
			
		atualizarSituacaoMatricula(obj, usuario);
	}
		
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoMatricula(ProgramacaoFormaturaAlunoVO obj, UsuarioVO usuario) throws Exception{
		if(obj.getColouGrau().equals(SituacaoColouGrauProgramacaoFormaturaAluno.COLOU_GRAU.getValor())){
			if (Uteis.isAtributoPreenchido(obj.getColacaoGrau().getData())) {
				obj.getMatricula().setDataColacaoGrau(obj.getColacaoGrau().getData());
				getFacadeFactory().getMatriculaFacade().alterarDataColacaoGrauPorMatricula(obj.getMatricula(), usuario);
			}
		    obj.getMatricula().getMatriculaPeriodoVOs().addAll(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(obj.getMatricula().getMatricula(), false,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getAplicacaoControle().getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(obj.getMatricula().getUnidadeEnsino().getCodigo()), usuario));
		    MatriculaPeriodoVO matriculaPeriodoVO = obj.getMatricula().getUltimoMatriculaPeriodoVO();
		    matriculaPeriodoVO.setSituacaoMatriculaPeriodo("FO");
            getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, OrigemFechamentoMatriculaPeriodoEnum.FORMATURA, obj.getCodigo(), new Date());
            getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(obj.getMatricula().getMatricula(), "FO", usuario);
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().enviarMensagemNotificacaoRegistroFormatura(obj, usuario);
            matriculaPeriodoVO = null;
		}
	}
	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#excluir(negocio.comuns.academico.ProgramacaoFormaturaAlunoVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProgramacaoFormaturaAlunoVO obj) throws Exception {
		ProgramacaoFormaturaAluno.excluir(getIdEntidade());
		String sql = "DELETE FROM ProgramacaoFormaturaAluno WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public Boolean consultarSeExisteColacaoGrauParaMatricula(String matricula) throws Exception {
		String sqlStr = "SELECT case when (count(codigo) > 0) then true else false end as existe FROM programacaoFormaturaAluno WHERE matricula = '" + matricula + "'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("existe");
		}
		return false;
	}	

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#consultarColacaoPorMatricula(java.lang.String, int)
	 */
	public ProgramacaoFormaturaAlunoVO consultarColacaoPorMatricula(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSqlDadosCompletos();		
		sqlStr.append(" WHERE programacaoFormaturaAluno.matricula = '" + valorConsulta + "' order by programacaoFormaturaAluno.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Esse ALUNO não possui colação de grau associada a ele.");
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}



	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
	 * para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProgramacaoFormaturaAlunoVO</code> resultantes da consulta.
	 */
	public static List<ProgramacaoFormaturaAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ProgramacaoFormaturaAlunoVO> vetResultado = new ArrayList<ProgramacaoFormaturaAlunoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ProgramacaoFormaturaAlunoVO</code>.
	 * 
	 * @return O objeto da classe <code>ProgramacaoFormaturaAlunoVO</code> com os dados devidamente montados.
	 */
	public static ProgramacaoFormaturaAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaAlunoVO obj = new ProgramacaoFormaturaAlunoVO();
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setProgramacaoFormatura((dadosSQL.getInt("programacaoFormatura")));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getMatricula().getAluno().setCodigo(dadosSQL.getInt("pessoa.codigo"));
		obj.getMatricula().getAluno().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getMatricula().getAluno().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getMatricula().getAluno().setCPF(dadosSQL.getString("pessoa.cpf"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum"))){
			obj.getMatricula().getAluno().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("pessoa.tipoAssinaturaDocumentoEnum")));
		}
		obj.getMatricula().getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getMatricula().getCurso().setNome(dadosSQL.getString("curso.nome"));
		obj.getMatricula().getGradeCurricularAtual().setCodigo(dadosSQL.getInt("matricula.gradeCurricularAtual"));
		obj.getMatricula().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
		obj.getMatricula().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		obj.getMatricula().getTurno().setCodigo(dadosSQL.getInt("turno.codigo"));
		obj.getMatricula().getTurno().setNome(dadosSQL.getString("turno.nome"));
		obj.getColacaoGrau().setCodigo((dadosSQL.getInt("colacaoGrau")));
		obj.setColouGrau(dadosSQL.getString("colouGrau"));
		obj.getColacaoGrau().setTitulo(dadosSQL.getString("colacaograu.titulo"));
		obj.getColacaoGrau().setData(dadosSQL.getDate("colacaograu.data"));
		obj.setRequerimento((dadosSQL.getInt("requerimento")));		
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setSituacaoDocumentacao(dadosSQL.getString("situacaoDocumentacao"));
		obj.setNovoObj(Boolean.FALSE);
		obj.setDataConclusaoCurso(dadosSQL.getDate("matricula.dataConclusaoCurso"));
		obj.setPossuiDiploma(getFacadeFactory().getProgramacaoFormaturaAlunoFacade().consultarPossuiDiploma(obj.getMatricula().getMatricula()));
		obj.setExisteProgramacaoFormaturaDuplicada(verificarExisteProgramacaoFormaturaAlunoDuplicada(obj));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		//obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(obj.getMatricula().getMatricula(), usuario.getUnidadeEnsinoLogado().getCodigo(), false, usuario));
		obj.setCurriculoIntegralizado(getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(obj.getMatricula(), obj.getMatricula().getGradeCurricularAtual().getCodigo(), usuario, null));
		if(obj.isCurriculoIntegralizado()) {
			obj.setSituacaoAcademica("OK");
		}else {
			obj.setSituacaoAcademica("PE");
		}
		obj.setMatriculaEnadeVO(getFacadeFactory().getMatriculaEnadeFacade().consultarMatriculaEnadePorMatriculaAluno(dadosSQL.getString("matricula"), usuario));
		return obj;
	}



	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#excluirProgramacaoFormaturaAlunos(java.lang.Integer)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirProgramacaoFormaturaAlunos(Integer programacaoFormatura) throws Exception {
		ProgramacaoFormaturaAluno.excluir(getIdEntidade());
		String sql = "DELETE FROM ProgramacaoFormaturaAluno WHERE (programacaoFormatura = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { programacaoFormatura });
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#alterarProgramacaoFormaturaAlunos(java.lang.Integer, java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarProgramacaoFormaturaAlunos(Integer programacaoFormatura, List objetos,  UsuarioVO usuario) throws Exception {
		String str = "DELETE FROM ProgramacaoFormaturaAluno WHERE programacaoFormatura = " + programacaoFormatura;
		Iterator i = objetos.iterator();
		while (i.hasNext()) {
			ProgramacaoFormaturaAlunoVO objeto = (ProgramacaoFormaturaAlunoVO) i.next();
			str += " AND codigo <> " + objeto.getCodigo().intValue();
		}
		getConexao().getJdbcTemplate().update(str);
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ProgramacaoFormaturaAlunoVO objeto = (ProgramacaoFormaturaAlunoVO) e.next();
			Integer codigoProgramacaoFormaturaAlunoExistente = consultarCodigoExisteProgramacaoFormaturaAlunoSemProgramacaoFormaturaPorMatricula(objeto.getMatricula().getMatricula());
			if (Uteis.isAtributoPreenchido(codigoProgramacaoFormaturaAlunoExistente)) {
				objeto.setCodigo(codigoProgramacaoFormaturaAlunoExistente);
			}
			objeto.setProgramacaoFormatura(programacaoFormatura);
			if (Uteis.isAtributoPreenchido(objeto) && objeto.getExisteProgramacaoFormaturaDuplicada()) {
				continue;
			}
			if (objeto.getCodigo().equals(0)) { 
				incluir(objeto, usuario);
			} else {
				alterar(objeto, usuario);
			}
			getFacadeFactory().getMatriculaFacade().alterarDataConclusaoCurso(objeto.getMatricula().getMatricula(), objeto.getDataConclusaoCurso(), usuario);
			if(objeto.getColouGrau().equals("SI")){
				objeto.getMatricula().setDataColacaoGrau(objeto.getColacaoGrau().getData());			
				getFacadeFactory().getMatriculaFacade().alterarDataColacaoGrauPorMatricula(objeto.getMatricula(), usuario);
			}else {
				objeto.getMatricula().setDataColacaoGrau(null);
				getFacadeFactory().getMatriculaFacade().alterarDataColacaoGrauPorMatricula(objeto.getMatricula(), usuario);
			}
		}
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#alterarProgramacaoFormaturaAlunosPorRegistroPresencaColacaoGrau(java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarProgramacaoFormaturaAlunosPorRegistroPresencaColacaoGrau(List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaVO, UsuarioVO usuario) throws Exception {
		for (ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO : listaProgramacaoFormaturaVO) {
			if (!programacaoFormaturaAlunoVO.getCodigo().equals(0) && !programacaoFormaturaAlunoVO.getExisteProgramacaoFormaturaDuplicada()) {
				alterar(programacaoFormaturaAlunoVO, usuario);
				if (programacaoFormaturaAlunoVO.getColouGrau().equals("NI")) {
					programacaoFormaturaAlunoVO.setPermitirAlterarSituacaoColouGrau(false);
				} else {
					programacaoFormaturaAlunoVO.setPermitirAlterarSituacaoColouGrau(true);
				}
			}
		}
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#incluirProgramacaoFormaturaAlunos(java.lang.Integer, java.util.List)
	 */
	@Override
	public void incluirProgramacaoFormaturaAlunos(Integer programacaoFormaturaPrm, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ProgramacaoFormaturaAlunoVO obj = (ProgramacaoFormaturaAlunoVO) e.next();
			obj.setProgramacaoFormatura(programacaoFormaturaPrm);
			Integer codigo = consultarCodigoExisteProgramacaoFormaturaAlunoSemProgramacaoFormaturaPorMatricula(obj.getMatricula().getMatricula());
			if (Uteis.isAtributoPreenchido(codigo)) {
				obj.setCodigo(codigo);
				alterar(obj,  usuario);
			} else {
				incluir(obj,  usuario);
			}
			getFacadeFactory().getMatriculaFacade().alterarDataConclusaoCurso(obj.getMatricula().getMatricula(), obj.getDataConclusaoCurso(), usuario);				
			if(obj.getColouGrau().equals("SI")){
				obj.getMatricula().setDataColacaoGrau(obj.getColacaoGrau().getData());			
				getFacadeFactory().getMatriculaFacade().alterarDataColacaoGrauPorMatricula(obj.getMatricula(), usuario);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ProgramacaoFormaturaAlunoVO</code> relacionados a um objeto da classe <code>academico.ProgramacaoFormatura</code>.
	 * 
	 * @param programacaoFormatura
	 *            Atributo de <code>academico.ProgramacaoFormatura</code> a ser utilizado para localizar os objetos da classe <code>ProgramacaoFormaturaAlunoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ProgramacaoFormaturaAlunoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Override
	public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAlunos(Integer programacaoFormatura, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaAluno.consultar(getIdEntidade());
		List<ProgramacaoFormaturaAlunoVO> objetos = new ArrayList<>(0);
		StringBuilder sql = getSqlDadosCompletos();
		sql.append(" WHERE programacaoFormatura = ? ORDER BY pessoa.nome  ");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { programacaoFormatura });
		while (resultado.next()) {
			objetos.add(ProgramacaoFormaturaAluno.montarDados(resultado, nivelMontarDados, usuario));
		}
		return objetos;
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, int)
	 */
	public ProgramacaoFormaturaAlunoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder(getSqlDadosCompletos());
		sql.append(" WHERE codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ProgramacaoFormaturaAluno ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ProgramacaoFormaturaAluno.idEntidade;
	}

	/**
	 * @see negocio.facade.jdbc.academico.ProgramacaoFormaturaAlunoInterfaceFacade#setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		ProgramacaoFormaturaAluno.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirProgramacaoFormaturaAlunoVinculadaColacaoGrau(Integer colacaoGrau,UsuarioVO usuarioVO) throws Exception {
		ProgramacaoFormaturaAluno.excluir(getIdEntidade());
		String sql = "DELETE FROM programacaoformaturaaluno  WHERE colacaograu  = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, new Object[] { colacaoGrau });
	}
	
	public ProgramacaoFormaturaAlunoVO consultarPorMatriculaColacaoGrau(String matricula,Integer colacaoGrau, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		StringBuilder sqlStr = getSqlDadosCompletos();
		sqlStr.append("WHERE matricula.matricula = '").append(matricula).append("' ");
		sqlStr.append(" AND ProgramacaoFormaturaAluno.colacaograu = ").append(colacaoGrau).append(";");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while(tabelaResultado.next()){
			return montarDados(tabelaResultado, nivelMontarDados, usuario);
		}
		return null;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerVinculoProgramacaoFormatura(final ProgramacaoFormaturaVO programacaoFormaturaVO, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE programacaoformaturaaluno SET programacaoformatura = NULL WHERE programacaoformatura = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1,programacaoFormaturaVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	private Integer consultarCodigoExisteProgramacaoFormaturaAlunoSemProgramacaoFormaturaPorMatricula(String matricula) throws Exception{
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ProgramacaoFormaturaAluno.* FROM ProgramacaoFormaturaAluno ");
		sqlStr.append(" WHERE matricula = ? AND programacaoFormatura is null LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		while(tabelaResultado.next()){
			return new Integer(tabelaResultado.getInt("codigo"));
		}
		return 0;
	}
	
	public Boolean consultarPossuiDiploma(String matricula) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT EXISTS( ");
		sql.append(" SELECT codigo FROM expedicaodiploma e WHERE e.matricula = programacaoformaturaaluno.matricula ");		
		sql.append(" ) AS possuiDiploma ");
		sql.append(" FROM ProgramacaoFormatura ");
		sql.append(" INNER JOIN programacaoformaturaaluno ON ProgramacaoFormatura.codigo = programacaoformaturaaluno.ProgramacaoFormatura ");
		sql.append(" WHERE programacaoformaturaaluno.matricula = '").append(matricula).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());		
		while(tabelaResultado.next()) {
			return new Boolean(tabelaResultado.getBoolean("possuiDiploma"));
		}
		return Boolean.FALSE;
	}
	
	@Override
	public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAluno(Integer valorConsulta, DataModelo controleConsultaOtimize, String valorConsultaMatricula, String valorConsultaNomeAluno, String valorConsultaNomeCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaAluno.consultar(getIdEntidade());		
		StringBuilder sql = getSqlDadosCompletos();
		sql.append(" WHERE programacaoFormatura = ? ");
		List<Object> filter =  new ArrayList<Object>(0);
		filter.add(valorConsulta);
		if (Uteis.isAtributoPreenchido(valorConsultaMatricula)) {
			sql.append(" AND matricula.matricula ILIKE (?)");
			filter.add("%"+valorConsultaMatricula+"%");
		}
		if (Uteis.isAtributoPreenchido(valorConsultaNomeAluno)) {
			sql.append(" AND pessoa.nome ILIKE(sem_acentos(?))");
			filter.add("%"+valorConsultaNomeAluno+"%");
		}
		if (Uteis.isAtributoPreenchido(valorConsultaNomeCurso)) {
			sql.append(" AND curso.nome ILIKE(sem_acentos(?))");
			filter.add("%"+valorConsultaNomeCurso+"%");
		}
		sql.append(" ORDER BY pessoa.nome ");
		sql.append(" LIMIT ").append(controleConsultaOtimize.getLimitePorPagina());
		sql.append(" OFFSET ").append(controleConsultaOtimize.getOffset());
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filter.toArray());
		controleConsultaOtimize.setTotalRegistrosEncontrados(0);
		if(resultado.next()) {
        	controleConsultaOtimize.setTotalRegistrosEncontrados(resultado.getInt("qtde_total_registros"));
        	resultado.beforeFirst();
        }
		return montarDadosConsulta(resultado, nivelMontarDados, usuario);		
	}

	@Override
	public Boolean consultarExisteMatriculaEmProgramacaoFormatura(String matricula, Integer codigo) throws Exception {
		StringBuilder sql = new StringBuilder("select codigo from programacaoformaturaaluno ");
		sql.append(" WHERE matricula = '"+matricula+"' ").append(" AND programacaoformatura = "+codigo);
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (resultado.next()) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAluno(ProgramacaoFormaturaAlunoVO obj, String matricula, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("delete from programacaoformaturaaluno where programacaoformatura = ? and matricula = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), obj.getProgramacaoFormatura(), obj.getMatricula().getMatricula());
	}

	private StringBuilder getSqlDadosCompletos() {
		StringBuilder sql = new StringBuilder("SELECT  count(*) over() as qtde_total_registros, ProgramacaoFormaturaAluno.codigo, ProgramacaoFormaturaAluno.programacaoFormatura, ProgramacaoFormaturaAluno.matricula , ProgramacaoFormaturaAluno.colacaoGrau, ProgramacaoFormaturaAluno.colouGrau,  ProgramacaoFormaturaAluno.requerimento, ");
		//TODO VERSAO 8
		// sql.append(" case when exists(select codigo from contareceber where contareceber.matriculaaluno = ProgramacaoFormaturaAluno.matricula and contareceber.situacao = 'AR' and contareceber.tipoorigem not in ('REQ', 'IPS') limit 1 ) then 'PE' else 'OK' end AS situacaoFinanceira, ");
		sql.append(" 'OK' AS situacaoFinanceira, ");
		sql.append(" case when exists(select documetacaomatricula.codigo from documetacaomatricula where documetacaomatricula.matricula =  matricula.matricula and coalesce(entregue, false) = false limit 1 ) then 'PE' else 'OK' end as situacaoDocumentacao, ");  		
		sql.append(" pessoa.nome as \"pessoa.nome\",  pessoa.dataNasc as \"pessoa.dataNasc\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sql.append(" pessoa.email as \"pessoa.email\", ");
		sql.append(" pessoa.cpf as \"pessoa.cpf\", ");
		sql.append(" pessoa.tipoAssinaturaDocumentoEnum as \"pessoa.tipoAssinaturaDocumentoEnum\", ");
		sql.append(" curso.codigo as \"curso.codigo\", ");
		sql.append(" curso.nome as \"curso.nome\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", ");
		sql.append(" unidadeensino.nome as \"unidadeensino.nome\", ");
		sql.append(" turno.codigo as \"turno.codigo\", ");
		sql.append(" turno.nome as \"turno.nome\",  ");
		sql.append(" colacaograu.codigo as \"colacaograu.codigo\", ");
		sql.append(" colacaograu.titulo as \"colacaograu.titulo\",  ");
		sql.append(" colacaograu.data as \"colacaograu.data\", ");
		sql.append(" matricula.gradeCurricularAtual as \"matricula.gradeCurricularAtual\", matricula.situacao as \"matricula.situacao\", ");
		sql.append(" case when matricula.dataconclusaocurso is not null then matricula.dataconclusaocurso else ( ");
		sql.append(" (select case when gradedisciplina.bimestre = 1 then periodoletivoativounidadeensinocurso.datafimperiodoletivoprimeirobimestre else datafimperiodoletivosegundobimestre end as dataconclusaotemp ");
		sql.append(" from historico ");
		sql.append(" inner join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
		sql.append(" inner join processomatricula on processomatricula.codigo = matriculaPeriodo.processomatricula ");
		sql.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo and processomatriculacalendario.curso = matricula.curso and processomatriculacalendario.turno = matricula.turno ");
		sql.append(" inner join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sql.append(" where historico.situacao in ('AA','AP','AE','IS','CH','CC') ");
		sql.append(" and historico.matricula = matricula.matricula and historico.matrizcurricular = matricula.gradecurricularatual ");
		sql.append(" order by ");
		sql.append(" case when gradedisciplina.bimestre = 1 then periodoletivoativounidadeensinocurso.datafimperiodoletivoprimeirobimestre else datafimperiodoletivosegundobimestre end ) ");		
		sql.append(" union all ");
		sql.append(" (select max(datadeferimento) dataconclusaotemp from estagio ");
		sql.append(" inner join gradecurricularestagio on gradecurricularestagio.codigo = estagio.gradecurricularestagio ");
		sql.append(" where matricula = matricula.matricula and estagio.situacaoestagioenum = 'DEFERIDO' ");
		sql.append(" and gradecurricularestagio.gradecurricular = matricula.gradecurricularatual and estagio.datadeferimento is not null) ");
		sql.append(" order by dataconclusaotemp desc nulls last limit 1 ");
		sql.append(" ) end as \"matricula.dataConclusaoCurso\" ");
		sql.append(" FROM ProgramacaoFormaturaAluno ");
		sql.append(" INNER JOIN matricula ON matricula.matricula = ProgramacaoFormaturaAluno.matricula ");
		sql.append(" INNER JOIN curso ON matricula.curso = curso.codigo ");
		sql.append(" INNER JOIN unidadeensino ON matricula.unidadeensino = unidadeensino.codigo ");
		sql.append(" INNER JOIN turno ON matricula.turno = turno.codigo ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sql.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula and  ");
		sql.append(" matriculaPeriodo.codigo = (select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' ");
		sql.append(" order by mp.ano desc, mp.semestre desc, case when mp.situacaomatriculaperiodo in ('AT', 'FO') then 0 else 1 end, mp.data desc, mp.codigo desc limit 1 ) "); 
		sql.append(" LEFT JOIN colacaograu ON colacaograu.codigo = ProgramacaoFormaturaAluno.colacaograu ");
		return sql;
	}

	@Override
	public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAlunoPorCurso(Integer codigoProgramacao, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSqlDadosCompletos();
		sql.append(" INNER JOIN programacaoFormatura ON programacaoFormatura.codigo = ProgramacaoFormaturaAluno.programacaoFormatura ");
		sql.append(" WHERE programacaoFormatura.codigo = " + codigoProgramacao);
		adicionarFiltroValidacaoAlunoColouGrauCondicaoWhere(sql, "programacaoformaturaaluno", "matricula");
		sql.append(" AND curso.codigo = " + codigoCurso + " ORDER BY sem_acentos(pessoa.nome) " );
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<ProgramacaoFormaturaAlunoVO> consultarProgramacaoFormaturaAlunoPorSemDocumentoAssinado(Integer codigoProgramacao, Integer codigoCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSqlDadosCompletos();
		sql.append(" INNER JOIN programacaoFormatura ON programacaoFormatura.codigo = ProgramacaoFormaturaAluno.programacaoFormatura ");
		sql.append(" WHERE programacaoFormatura.codigo = " + codigoProgramacao);
		sql.append(" AND curso.codigo = " + codigoCurso );
		adicionarFiltroValidacaoAlunoColouGrauCondicaoWhere(sql, "programacaoformaturaaluno", "matricula");
//		sql.append(" AND not exists (" );
//		sql.append(" 	select documentoassinadopessoa.codigo  from documentoassinadopessoa " );
//		sql.append(" 	inner join documentoassinado on documentoassinado.codigo = documentoassinadopessoa.documentoassinado" );
//		sql.append(" 	where documentoassinado.curso = curso.codigo and documentoassinado.programacaoformatura  = programacaoFormatura.codigo " );
//		sql.append(" 	and documentoassinado.documentoAssinadoInvalido = false " );
//		sql.append(" 	and documentoassinadopessoa.tipopessoa = 'ALUNO' and documentoassinadopessoa.pessoa = pessoa.codigo ) " );
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarAlunoColouGrau(final ProgramacaoFormaturaAlunoVO obj,  UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaAlunoVO.validarDados(obj);
		obj.realizarUpperCaseDados();
		final String sql = "UPDATE ProgramacaoFormaturaAluno set colouGrau=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(4, obj.getColouGrau());
				sqlAlterar.setInt(6, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoColouGrau(DocumentoAssinadoVO documentoAssinado, Integer codigoAluno, SituacaoDocumentoAssinadoPessoaEnum situacao, UsuarioVO usuario) throws Exception {
		ProgramacaoFormaturaAlunoVO obj = null;
		try {
			obj = consultarPorDocumentoAssinadorAluno(documentoAssinado.getCodigo(), codigoAluno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if(Uteis.isAtributoPreenchido(obj) && !obj.getColouGrau().equals("SI")) {				
				if(situacao.isAssinado()) {
					obj.setMatriculaAptaInativacaoCredenciasAlunosFormados(true);
					obj.setColouGrau(SituacaoColouGrauProgramacaoFormaturaAluno.COLOU_GRAU.getValor());
				}else if(situacao.isRejeitado()) {
					obj.setColouGrau(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_COLOU.getValor());	
				}else  if(situacao.isPendente()) {
					obj.setColouGrau(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_INFORMADO.getValor());
				}
				if ((situacao.isAssinado() || situacao.isPendente()) && Uteis.isAtributoPreenchido(obj.getMatricula()) && obj.getExisteProgramacaoFormaturaDuplicada()) {
					//Antes de alterar a situação de programação de formatura aluno e matrícula deve primeiro excluir 
					//casos antigos/recentes de programação de formatura que não colaram grau, deixando apenas a que está sendo assinada no momento
					//chamado 40903
					//@Author Felipi Alves
					excluirProgramacaoFormaturaAlunoComDuplicidade(obj, usuario);
				}
				alterar(obj, usuario);	
			}	
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ProgramacaoFormaturaAlunoVO consultarPorDocumentoAssinadorAluno(Integer codigoDocumentoAssinado, Integer codigoAluno, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = getSqlDadosCompletos();
		sql.append(" INNER JOIN programacaoformatura on programacaoformatura.codigo = programacaoformaturaaluno.programacaoformatura ");
		sql.append(" INNER JOIN documentoassinado on programacaoformatura.codigo = documentoassinado.programacaoformatura ");
		sql.append(" WHERE documentoAssinado.codigo = " + codigoDocumentoAssinado);
		sql.append(" AND pessoa.codigo = " + codigoAluno);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuario);
		}
		return new ProgramacaoFormaturaAlunoVO();
	}
	
	public List<MatriculaVO> consultarProgramacaoFormaturaMatriculaEnade(String matricula, Integer programacaoFormatura, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSqlDadosCompletos();
		sb.append(" where programacaoFormatura = ").append(programacaoFormatura);
		if (!matricula.equals("")) {
			sb.append("and matricula.matricula = '").append(matricula).append("' ");
		}
		sb.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
		SqlRowSet tabelaResultado  = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());	
		return getFacadeFactory().getMatriculaEnadeFacade().montarDadosConsulta(tabelaResultado, usuarioVO);

	}
	
	/**
	 * Método com a finalidade de verificar se existe alguma outra programação de
	 * formatura aluno vinculado a matrícula do aluno que está sendo validado no
	 * momento, caso exista outra programação de formatura cuja a ("situação" ->
	 * <code>colouGrau</code>) esteja "SI" ou "NI" 
	 * chamado 40903
	 * 
	 * @param programacaoFormaturaAlunoVO
	 * @return
	 * @throws Exception
	 * @author Felipi Alves
	 */
	public static Boolean verificarExisteProgramacaoFormaturaAlunoDuplicada(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT programacaoformaturaaluno.codigo ");
		sql.append("FROM programacaoformaturaaluno ");
		sql.append("WHERE programacaoformaturaaluno.matricula = ? AND programacaoformaturaaluno.codigo <> ? AND programacaoformaturaaluno.colougrau <> 'NO' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), programacaoFormaturaAlunoVO.getMatricula().getMatricula(), programacaoFormaturaAlunoVO.getCodigo());
		return tabelaResultado.next();
	}
	
	/**
	 * Método que realizará a montagem da lista de programações de formatura aluno
	 * que estão com duplicidade, primeiro filtrando as programações que tem a
	 * duplicidade e depois apenas montar os dados dessa programação que o aluno
	 * está pendente para colar grau ou já colou grau
	 * chamado 40903
	 * 
	 * @param listaProgramacaoFormatura
	 * @param listaProgramacaoFormaturaDuplicadas
	 * @param usuario
	 * @throws Exception
	 * @author Felipi Alves
	 */
	@Override
	public void realizarMontagemProgramacaoFormaturaDuplicada(List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormatura, List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaDuplicadas, UsuarioVO usuario) throws Exception {
		listaProgramacaoFormaturaDuplicadas.clear();
		if (Uteis.isAtributoPreenchido(listaProgramacaoFormatura) && listaProgramacaoFormatura.stream().anyMatch(ProgramacaoFormaturaAlunoVO::getExisteProgramacaoFormaturaDuplicada)) {
			List<ProgramacaoFormaturaAlunoVO> programacaoFormaturaAlunoVOs = listaProgramacaoFormatura.stream().filter(ProgramacaoFormaturaAlunoVO::getExisteProgramacaoFormaturaDuplicada).collect(Collectors.toList());
			for (ProgramacaoFormaturaAlunoVO obj : programacaoFormaturaAlunoVOs) {
				if (Uteis.isAtributoPreenchido(obj)) {
					obj.setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarProgramacaoFormaturaAtivaPorProgramacaoFormaturaAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
					listaProgramacaoFormaturaDuplicadas.add(obj);
				}
			}
		}
	}

	/**
	 * Metodo de exclusão de programação de formatura aluno com filtro de
	 * <code>matricula</code> e <code>programacaoFormatura</code>
	 * chamado 40903
	 * 
	 * @param obj
	 * @throws Exception
	 * @author Felipi Alves
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorMatriculaEProgramacaoFormatura(ProgramacaoFormaturaAlunoVO obj) throws Exception {
		if (Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getMatricula()) && Uteis.isAtributoPreenchido(obj.getProgramacaoFormaturaVO()) && obj.getExisteProgramacaoFormaturaDuplicada()) {
			ProgramacaoFormaturaAluno.excluir(getIdEntidade());
			String sql = "DELETE FROM programacaoformaturaaluno WHERE matricula = ? AND programacaoFormatura = ?";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMatricula().getMatricula(), obj.getProgramacaoFormaturaVO().getCodigo() });
		}
	}
	
	/**
	 * Metodo de exclusão de programação de formatura aluno duplicadas, excluindo a
	 * programação de formatura antigas ou foram criadadas mas não foi realizado a
	 * colação de grau por via dela mesmo <code>matricula</code> e
	 * <code>codigo</code> 
	 * chamado 40903
	 * 
	 * @param obj
	 * @throws Exception
	 * @author Felipi Alves
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirProgramacaoFormaturaAlunoComDuplicidade(ProgramacaoFormaturaAlunoVO obj, UsuarioVO usuario) throws Exception {
		if (Uteis.isAtributoPreenchido(obj) && Uteis.isAtributoPreenchido(obj.getMatricula()) && obj.getExisteProgramacaoFormaturaDuplicada()) {
			ProgramacaoFormaturaAluno.excluir(getIdEntidade());
			String sql = "DELETE FROM programacaoformaturaaluno WHERE matricula = ? AND colougrau != 'NO' AND codigo != ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMatricula().getMatricula(), obj.getCodigo() });
		}
	}
	
	public ProgramacaoFormaturaAlunoVO consultarPorMatriculaProgramacaoFormaturaAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder(getSqlDadosCompletos());
		sql.append(" WHERE ProgramacaoFormaturaAluno.matricula = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { valorConsulta });
		if (!tabelaResultado.next()) {
			return new ProgramacaoFormaturaAlunoVO();
		}
		return (ProgramacaoFormaturaAluno.montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public void adicionarFiltroValidacaoAlunoColouGrauCondicaoWhere(StringBuilder sql, String aliasTabela, String aliasTabelaMatricula) {
		if (Uteis.isAtributoPreenchido(sql) && Uteis.isAtributoPreenchido(aliasTabela)) {
			sql.append(" AND ((").append(aliasTabela).append(".colougrau = 'NI') OR (").append(aliasTabela).append(".colougrau IN ('SI', 'NO') AND NOT EXISTS (");
			sql.append("SELECT dap.codigo ");
			sql.append("FROM documentoassinadopessoa dap ");
			sql.append("INNER JOIN documentoassinado da ON da.codigo = dap.documentoassinado ");
			sql.append("WHERE da.tipoorigemdocumentoassinado = 'ATA_COLACAO_GRAU' ");
			sql.append("AND da.documentoassinadoinvalido IS FALSE ");
			sql.append("AND dap.situacaodocumentoassinadopessoa != 'REJEITADO' ");
			sql.append("AND da.programacaoformatura = ").append(aliasTabela).append(".programacaoformatura ");
			sql.append("AND da.curso = ").append(aliasTabelaMatricula).append(".curso ");
			sql.append("AND dap.pessoa = ").append(aliasTabelaMatricula).append(".aluno ");
			sql.append("LIMIT 1");
			sql.append("))) ");
		}
	}
	
	@Override
	public void alterarSituacaoColacaoGrauAluno(Integer codigo, UsuarioVO usuario) {
		String sql = "UPDATE programacaoFormaturaAluno SET colougrau = 'NO' WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, codigo.intValue());
	}
	
}