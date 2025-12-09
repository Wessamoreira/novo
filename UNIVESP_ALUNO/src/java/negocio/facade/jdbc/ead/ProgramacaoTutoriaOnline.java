package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoTutoriaEnum;
import negocio.comuns.ead.enumeradores.SituacaoProgramacaoTutoriaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoNivelProgramacaoTutoriaEnum;
import negocio.comuns.gsuite.ClassroomGoogleVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ProgramacaoTutoriaOnlineInterfaceFacade;

/**
 * @author Victor Hugo 11/11/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class ProgramacaoTutoriaOnline extends ControleAcesso implements ProgramacaoTutoriaOnlineInterfaceFacade, Serializable {

	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ProgramacaoTutoriaOnline.idEntidade = idEntidade;
	}

	public ProgramacaoTutoriaOnline() throws Exception {
		super();
		setIdEntidade("ProgramacaoTutoriaOnline");
	}
	
	
	public void validarDados(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		if(programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().isUnidadeEnsino() && !Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo())){
			throw new Exception("O campo Unidade Ensino deve ser informado");
		}
		if(programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().isNivelEducacional() && !Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getNivelEducacional())){
			throw new Exception("O campo Nível Educacional deve ser informado");
		}
		if(programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().isTurma() && !Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo())){
			throw new Exception("O campo Turma deve ser informado");
		}
		if(programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().isCurso() && !Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getCursoVO().getCodigo())){
			throw new Exception("O campo Curso deve ser informado");
		}		
		if(!Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo())){
			throw new Exception("O campo Disciplina deve ser informado");
		}		
//		if(!Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getQtdeAlunos())){
//			throw new Exception("O campo Qtde. Alunos por Tutor deve ser informado");
//		}
//		if(!Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getRegraDistribuicaoTutoria())){
//			throw new Exception("O campo Regra de Distribuição deve ser informado");
//		}
		if (programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_VoceDeveAdcionarAoMenosUmTutorParaEstaProgramacao"));
		}
		if(programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline()){
			if(programacaoTutoriaOnlineVO.getDataInicioAula() == null) {
				throw new Exception("A Data Inicial deve ser informada.");
			}
			else if (programacaoTutoriaOnlineVO.getDataTerminoAula() == null) {
				throw new Exception("A Data Final deve ser informada.");
			}else {
				ZoneId defaultZoneId = ZoneId.systemDefault();				

				LocalDate ldDataInicio = programacaoTutoriaOnlineVO.getDataInicioAula().toInstant().atZone(defaultZoneId).toLocalDate();
				LocalDate ldDataFim = programacaoTutoriaOnlineVO.getDataTerminoAula().toInstant().atZone(defaultZoneId).toLocalDate();
				
				if (ldDataInicio.isAfter(ldDataFim)) {
					throw new Exception("A Data Inicial não pode ser maior que a data Final.");
				}
			} 
			
			if((programacaoTutoriaOnlineVO.getTurmaVO().getAnual() || programacaoTutoriaOnlineVO.getTurmaVO().getSemestral()) && (programacaoTutoriaOnlineVO.getAno() == null || programacaoTutoriaOnlineVO.getAno().equals(""))) {
				throw new Exception("O ano deve ser informado.");
			}
			
			if(programacaoTutoriaOnlineVO.getTurmaVO().getSemestral() && (programacaoTutoriaOnlineVO.getSemestre() == null || programacaoTutoriaOnlineVO.getSemestre().equals(""))) {
				throw new Exception("O semestre deve ser informado.");
			}
			
		}
		realizarVerificacaoExistenciaProgramacaoTutoriaOnlinePersistidoNoBancoComParametrosPassados(programacaoTutoriaOnlineVO, usuarioVO);
		
	}
	  
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (programacaoTutoriaOnlineVO.getCodigo() == 0) {
			incluir(programacaoTutoriaOnlineVO, verificarAcesso, usuarioVO);
		} else {
			alterar(programacaoTutoriaOnlineVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(programacaoTutoriaOnlineVO, usuarioVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO programacaotutoriaonline(unidadeensino, niveleducacional, curso, turma, disciplina, qtdealunos, dataalteracao, regradistribuicaotutoria, situacao, tiponivelprogramacaotutoria, definirPeriodoAulaOnline, dataInicioAula, dataTerminoAula, ano, semestre, executarClassroomAutomatico, executarSalaAulaBlackboardAutomatico)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			programacaoTutoriaOnlineVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					if (programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo() != 0) {
						sqlInserir.setInt(1, programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setString(2, programacaoTutoriaOnlineVO.getNivelEducacional());
					if (programacaoTutoriaOnlineVO.getCursoVO().getCodigo() != 0) {
						sqlInserir.setInt(3, programacaoTutoriaOnlineVO.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(3, 0);
					}
					if (programacaoTutoriaOnlineVO.getTurmaVO().getCodigo() != 0) {
						sqlInserir.setInt(4, programacaoTutoriaOnlineVO.getTurmaVO().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setInt(5, programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo());
					sqlInserir.setInt(6, programacaoTutoriaOnlineVO.getQtdeAlunos());
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(programacaoTutoriaOnlineVO.getDataAlteracao()));
					sqlInserir.setString(8, programacaoTutoriaOnlineVO.getRegraDistribuicaoTutoria().name());
					sqlInserir.setString(9, programacaoTutoriaOnlineVO.getSituacaoProgramacaoTutoriaOnline().name());
					sqlInserir.setString(10, programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().name());
					sqlInserir.setBoolean(11, programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline());
					if (programacaoTutoriaOnlineVO.getDataInicioAula() != null) {
						sqlInserir.setDate(12, Uteis.getDataJDBC(programacaoTutoriaOnlineVO.getDataInicioAula()));
					} else {
						sqlInserir.setNull(12, 0);
					}
					if (programacaoTutoriaOnlineVO.getDataTerminoAula() != null) {
						sqlInserir.setDate(13, Uteis.getDataJDBC(programacaoTutoriaOnlineVO.getDataTerminoAula()));
					} else {
						sqlInserir.setNull(13, 0);
					}
					if (programacaoTutoriaOnlineVO.getAno() != null) {
						sqlInserir.setString(14, programacaoTutoriaOnlineVO.getAno());
					} else {
						sqlInserir.setNull(14, 0);
					}					
					if (programacaoTutoriaOnlineVO.getSemestre() != null) {
						sqlInserir.setString(15, programacaoTutoriaOnlineVO.getSemestre());
					} else {
						sqlInserir.setNull(15, 0);
					}
					sqlInserir.setBoolean(16, programacaoTutoriaOnlineVO.isExecutarClassroomAutomatico());
					sqlInserir.setBoolean(17, programacaoTutoriaOnlineVO.isExecutarSalaAulaBlackboardAutomatico());
					

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						programacaoTutoriaOnlineVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().persistirProgramacaoTutoriaOnlineProfessores(programacaoTutoriaOnlineVO, usuarioVO);    			
			
		} catch (Exception e) {
			programacaoTutoriaOnlineVO.setNovoObj(Boolean.TRUE);
			programacaoTutoriaOnlineVO.setCodigo(0);
			throw e;
		}
	}	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(programacaoTutoriaOnlineVO, usuarioVO);
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE programacaotutoriaonline SET unidadeensino=?, niveleducacional=?, curso=?, turma=?, disciplina=?, qtdealunos=?, dataalteracao=?, regradistribuicaotutoria=?, situacao = ?, tiponivelprogramacaotutoria = ?, ordematual = ?, definirPeriodoAulaOnline = ?, dataInicioAula = ?, dataTerminoAula = ?, ano = ?, semestre = ?, executarClassroomAutomatico=?, executarSalaAulaBlackboardAutomatico=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					if (programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo() != 0) {
						sqlAlterar.setInt(1, programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setString(2, programacaoTutoriaOnlineVO.getNivelEducacional());
					if (programacaoTutoriaOnlineVO.getCursoVO().getCodigo() != 0) {
						sqlAlterar.setInt(3, programacaoTutoriaOnlineVO.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					if (programacaoTutoriaOnlineVO.getTurmaVO().getCodigo() != 0) {
						sqlAlterar.setInt(4, programacaoTutoriaOnlineVO.getTurmaVO().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					sqlAlterar.setInt(5, programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo());
					sqlAlterar.setInt(6, programacaoTutoriaOnlineVO.getQtdeAlunos());
					sqlAlterar.setTimestamp(7, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setString(8, programacaoTutoriaOnlineVO.getRegraDistribuicaoTutoria().name());
					sqlAlterar.setString(9, programacaoTutoriaOnlineVO.getSituacaoProgramacaoTutoriaOnline().name());
					sqlAlterar.setString(10, programacaoTutoriaOnlineVO.getTipoNivelProgramacaoTutoria().name());
					sqlAlterar.setInt(11, programacaoTutoriaOnlineVO.getOrdemAtual());
					sqlAlterar.setBoolean(12, programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline());
					if (programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline() && programacaoTutoriaOnlineVO.getDataInicioAula() != null) {
						sqlAlterar.setDate(13, Uteis.getDataJDBC(programacaoTutoriaOnlineVO.getDataInicioAula()));
					} else {
						sqlAlterar.setNull(13, 0);
					}
					if (programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline() && programacaoTutoriaOnlineVO.getDataTerminoAula() != null) {
						sqlAlterar.setDate(14, Uteis.getDataJDBC(programacaoTutoriaOnlineVO.getDataTerminoAula()));
					} else {
						sqlAlterar.setNull(14, 0);
					}
					if (programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline() && programacaoTutoriaOnlineVO.getAno() != null) {
						sqlAlterar.setString(15, programacaoTutoriaOnlineVO.getAno());
					} else {
						sqlAlterar.setNull(15, 0);
					}					
					if (programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline() &&  programacaoTutoriaOnlineVO.getSemestre() != null) {
						sqlAlterar.setString(16, programacaoTutoriaOnlineVO.getSemestre());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					sqlAlterar.setBoolean(17, programacaoTutoriaOnlineVO.isExecutarClassroomAutomatico());
					sqlAlterar.setBoolean(18, programacaoTutoriaOnlineVO.isExecutarSalaAulaBlackboardAutomatico());
					sqlAlterar.setInt(19, programacaoTutoriaOnlineVO.getCodigo());

					return sqlAlterar;
				}
			});
			getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().persistirProgramacaoTutoriaOnlineProfessores(programacaoTutoriaOnlineVO, usuarioVO);
			
			if(programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline() && (programacaoTutoriaOnlineVO.getDataInicioAulaAlterada() || programacaoTutoriaOnlineVO.getDataTerminoAulaAlterada())) {
				alterarDataCalendarioAtividadeMatricula(programacaoTutoriaOnlineVO, usuarioVO);		
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atutalizarSituacaoTutoriaProgramacaoTutoriaOnlineProfessor(final ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE programacaotutoriaonline SET situacao = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, programacaoTutoriaOnlineVO.getSituacaoProgramacaoTutoriaOnline().name());
					sqlAlterar.setInt(2, programacaoTutoriaOnlineVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void atutalizarOrdemAtualProgramacaoTutoriaOnline(final ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE programacaotutoriaonline SET ordematual = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, programacaoTutoriaOnlineVO.getOrdemAtual());
					sqlAlterar.setInt(2, programacaoTutoriaOnlineVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 
	 * As programações de tutoria já unicas. Este método procura uma programação identica a que está sendo persistida no banco, caso encontre,
	 * um throws será enviado para o usuário.
	 * 
	 * @see negocio.interfaces.ead.ProgramacaoTutoriaOnlineInterfaceFacade#
	 * realizarVerificacaoExistenciaProgramacaoTutoriaOnlinePersistidoNoBancoComParametrosPassados
	 * (java.lang.Integer, java.lang.Integer, java.lang.Integer,
	 * java.lang.Integer, negocio.comuns.ead.ProgramacaoTutoriaOnlineVO,
	 * java.lang.String, java.lang.String, negocio.comuns.arquitetura.UsuarioVO)
	 * 
	 * Método responsável por realizar uma verificação no banco de dados se
	 * existe uma programação tutoria on-line com os parâmetros passados. A
	 * utilização do is null se dá pelo fato de existir varias possibilidades de
	 * gravar uma programação, A disciplina sempre é obrigatória.
	 */
	public void realizarVerificacaoExistenciaProgramacaoTutoriaOnlinePersistidoNoBancoComParametrosPassados(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {

		if (programacaoTutoriaOnlinePersistidoNoBancoComParametrosPassados(programacaoTutoriaOnlineVO, usuarioVO) > 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_ProgramacaoTutoriaOnlineJaPersistida"));	
		}
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM programacaotutoriaonline WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, programacaoTutoriaOnlineVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ProgramacaoTutoriaOnlineVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ProgramacaoTutoriaOnlineVO obj = new ProgramacaoTutoriaOnlineVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino"));
		obj.setNivelEducacional(tabelaResultado.getString("niveleducacional"));
		obj.getCursoVO().setCodigo(tabelaResultado.getInt("curso"));
		obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma"));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		obj.setQtdeAlunos(tabelaResultado.getInt("qtdealunos"));
		obj.setDataAlteracao(tabelaResultado.getTimestamp("dataalteracao"));
		obj.setRegraDistribuicaoTutoria(RegraDistribuicaoTutoriaEnum.valueOf(tabelaResultado.getString("regradistribuicaotutoria")));
		obj.setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum.valueOf(tabelaResultado.getString("situacao")));
		obj.setTipoNivelProgramacaoTutoria(TipoNivelProgramacaoTutoriaEnum.valueOf(tabelaResultado.getString("tiponivelprogramacaotutoria")));
		obj.setOrdemAtual(tabelaResultado.getInt("ordematual"));
		obj.setDefinirPeriodoAulaOnline(tabelaResultado.getBoolean("definirperiodoaulaonline"));
		obj.setDataInicioAula(tabelaResultado.getDate("datainicioaula"));
		obj.setDataTerminoAula(tabelaResultado.getDate("dataterminoaula"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.setExecutarClassroomAutomatico(tabelaResultado.getBoolean("executarClassroomAutomatico"));
		obj.setExecutarSalaAulaBlackboardAutomatico(tabelaResultado.getBoolean("executarSalaAulaBlackboardAutomatico"));
		if (obj.getTurmaVO().getCodigo() != 0) {
			obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		}
		if (obj.getUnidadeEnsinoVO().getCodigo() != 0) {
			obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		}
		if (obj.getCursoVO().getCodigo() != 0) {
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, usuarioLogado));
		}
		obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setProgramacaoTutoriaOnlineProfessorVOs(getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarPorProgramacaoTutoriaOnline(obj, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			List professoresSemProgramacaoTutoriaOnline =  getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().consultarTutorSemProgramacaoTutoriaOnline(obj, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			if (Uteis.isAtributoPreenchido(professoresSemProgramacaoTutoriaOnline)) {
				obj.getProgramacaoTutoriaOnlineProfessorVOs().addAll(professoresSemProgramacaoTutoriaOnline);
			}
			return obj;
		}
		return obj;
	}

	@Override
	public List<ProgramacaoTutoriaOnlineVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ProgramacaoTutoriaOnlineVO> vetResultado = new ArrayList<ProgramacaoTutoriaOnlineVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ProgramacaoTutoriaOnlineVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM programacaotutoriaonline WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new ProgramacaoTutoriaOnlineVO();
	}

	@Override
	public List<ProgramacaoTutoriaOnlineVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		if (!(valorConsulta.length() < 2)) {
			if (campoConsulta.equals("identificadorTurma")) {
				return consultarPorIdentificadorTurma(valorConsulta, nivelMontarDados, usuarioLogado);
			} else if (campoConsulta.equals("nomeUnidadeEnsino")) {
				return consultarPorUnidadeEnsino(valorConsulta, nivelMontarDados, usuarioLogado);
			} else if (campoConsulta.equals("nivelEducacional")) {
				return consultarPorNivelEducacional(valorConsulta, nivelMontarDados, usuarioLogado);
			} else if (campoConsulta.equals("nomeCurso")) {
				return consultarPorCurso(valorConsulta, nivelMontarDados, usuarioLogado);
			} else if (campoConsulta.equals("nomeDisciplina")) {
				return consultarPorDisciplina(valorConsulta, nivelMontarDados, usuarioLogado);
			}
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		return null;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineVO> consultarPorIdentificadorTurma(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "select programacaotutoriaonline.* from programacaotutoriaonline inner join turma on turma.codigo = programacaotutoriaonline.turma where sem_acentos(turma.identificadorturma) ilike(sem_acentos(?)) ORDER BY programacaotutoriaonline.codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, valorConsulta + "%");

		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineVO> consultarPorUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder("select programacaotutoriaonline.* from programacaotutoriaonline ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = programacaotutoriaonline.unidadeensino ");
		sql.append(" where sem_acentos(unidadeensino.nome) ilike(sem_acentos(?)) and programacaotutoriaonline.turma is null ");
		sql.append(" union all ");
		sql.append(" select programacaotutoriaonline.* from programacaotutoriaonline ");
		sql.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sql.append(" where sem_acentos(unidadeensino.nome) ilike(sem_acentos(?)) ");
		sql.append(" union all ");
		sql.append(" select distinct programacaotutoriaonline.* from programacaotutoriaonline ");
		sql.append(" inner join curso on curso.codigo = programacaotutoriaonline.curso ");
		sql.append(" inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = unidadeensinocurso.unidadeensino ");
		sql.append(" where sem_acentos(unidadeensino.nome) ilike(sem_acentos(?)) and programacaotutoriaonline.unidadeensino is null ");

		sql.append(" ORDER BY codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + "%",  valorConsulta + "%",  valorConsulta + "%");

		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineVO> consultarPorNivelEducacional(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder("select programacaotutoriaonline.* from programacaotutoriaonline where sem_acentos(niveleducacional) ilike(sem_acentos(?)) and turma is null and curso is null ");
		sql.append(" union all ");
		sql.append(" select programacaotutoriaonline.* from programacaotutoriaonline ");
		sql.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sql.append(" inner join curso on curso.codigo = turma.curso ");
		sql.append(" where sem_acentos(curso.niveleducacional) ilike(sem_acentos(?)) and programacaotutoriaonline.curso is null ");
		sql.append(" union all ");
		sql.append(" select programacaotutoriaonline.* from programacaotutoriaonline ");		
		sql.append(" inner join curso on curso.codigo = programacaotutoriaonline.curso ");
		sql.append(" where sem_acentos(curso.niveleducacional) ilike(sem_acentos(?)) ");

		sql.append(" ORDER BY codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + "%", valorConsulta + "%", valorConsulta + "%");

		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineVO> consultarPorCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder("select programacaotutoriaonline.* from programacaotutoriaonline inner join curso on curso.codigo = programacaotutoriaonline.curso ");
		sql.append(" where sem_acentos(curso.nome) ilike(sem_acentos(?)) ");
		sql.append(" union all ");
		sql.append(" select distinct programacaotutoriaonline.* from programacaotutoriaonline ");
		sql.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sql.append(" inner join curso on curso.codigo = turma.curso ");
		sql.append(" where sem_acentos(curso.nome) ilike(sem_acentos(?)) and programacaotutoriaonline.curso is null ");
		sql.append(" ORDER BY codigo ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + "%", valorConsulta + "%");

		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineVO> consultarPorDisciplina(String valorConsulta, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "select * from programacaotutoriaonline inner join disciplina on disciplina.codigo = programacaotutoriaonline.disciplina where sem_acentos(disciplina.nome) ilike(sem_acentos(?)) ORDER BY programacaotutoriaonline.codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, valorConsulta + "%");

		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
	}

	@Override
	public void alterarOrdemPrioridadeTutor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO2) {
		int ordem1 = programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade();
		programacaoTutoriaOnlineProfessorVO.setOrdemPrioridade(programacaoTutoriaOnlineProfessorVO2.getOrdemPrioridade());
		programacaoTutoriaOnlineProfessorVO2.setOrdemPrioridade(ordem1);
		Ordenacao.ordenarLista(programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs(), "ordemPrioridade");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerTutor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, UsuarioVO usuarioVO) throws Exception {
//		if (programacaoTutoriaOnlineProfessorVO.getQtdeAlunosAtivos() > 0) {
//			throw new Exception(UteisJSF.internacionalizar("msg_ProgramacaoTutoriaOnline_validarExclusaoTutor"));
//		}
		executarVerificacaoClassroomEad(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, usuarioVO);
		executarVerificacaoSalaAulaBlackboardEad(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, usuarioVO);
		programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs().remove(programacaoTutoriaOnlineProfessorVO);
		getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().excluir(programacaoTutoriaOnlineProfessorVO, false, usuarioVO);
		getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().realizarDefinicaoOrdemPrioridadeProgramacaoTutoriaOnlineProfessores(programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs());
		getFacadeFactory().getProgramacaoTutoriaOnlineProfessorInterfaceFacade().alterarOrdemPrioridadeProgramacaoTutoriaOnlineProfessores(programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs());
	}

	private void executarVerificacaoClassroomEad(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, UsuarioVO usuarioVO) throws Exception {
		ClassroomGoogleVO cg = new ClassroomGoogleVO();
		cg.setTurmaVO(programacaoTutoriaOnlineVO.getTurmaVO());
		cg.getTurmaVO().setUnidadeEnsino(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO());
		cg.getTurmaVO().setCurso(programacaoTutoriaOnlineVO.getCursoVO());
		cg.getTurmaVO().getCurso().setNivelEducacional(programacaoTutoriaOnlineVO.getNivelEducacional());
		cg.setDisciplinaVO(programacaoTutoriaOnlineVO.getDisciplinaVO());
		cg.setAno(programacaoTutoriaOnlineVO.getAno());
		cg.setSemestre(programacaoTutoriaOnlineVO.getSemestre());
		cg.setProfessorEad(programacaoTutoriaOnlineProfessorVO.getProfessor());
//		getFacadeFactory().getClassroomGoogleFacade().realizarExclusaoClassroomGoogleEad(cg, usuarioVO);
	}
	
	private void executarVerificacaoSalaAulaBlackboardEad(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, UsuarioVO usuarioVO) throws Exception {
		List<SalaAulaBlackboardVO> salaAulaBlackboardVOs = getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardEad(TipoSalaAulaBlackboardEnum.DISCIPLINA, programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo(),
				programacaoTutoriaOnlineVO.getCursoVO().getCodigo(), programacaoTutoriaOnlineVO.getNivelEducacional(), programacaoTutoriaOnlineVO.getTurmaVO().getCodigo(), 
				programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo(), programacaoTutoriaOnlineVO.getAno(), programacaoTutoriaOnlineVO.getSemestre(), null,  programacaoTutoriaOnlineVO.getCodigo(), 
				Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		if(programacaoTutoriaOnlineProfessorVO.getProfessor().getListaPessoaEmailInstitucionalVO().isEmpty()) {
			programacaoTutoriaOnlineProfessorVO.getProfessor().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		}
		Uteis.checkState(programacaoTutoriaOnlineProfessorVO.getProfessor().getListaPessoaEmailInstitucionalVO().isEmpty(), "Não foi possível realizar essa operação, pois não foi encontrado nenhum e-mail institucional para o professor.");
		for(SalaAulaBlackboardVO salaAulaBlackboardVO: salaAulaBlackboardVOs) {
			getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(salaAulaBlackboardVO.getCodigo(), programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.PROFESSOR, salaAulaBlackboardVO.getIdSalaAulaBlackboard(), programacaoTutoriaOnlineProfessorVO.getProfessor().getListaPessoaEmailInstitucionalVO().get(0).getEmail(), usuarioVO);
		}
	}

	

	/**
	 * Método responsável por localizar a programação tutoria on-line configurada priorizando a programação configurada para disciplina da turma do semestre.
	 * O mesmo será chamado no momento em que o aluno acessar o estudo on-line pela primera vez.
	 * 
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ProgramacaoTutoriaOnlineVO consultarProgramacaoTutoriaOnlinePorMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		//Consulta Por Turma e disciplina
		sqlStr.append("	select 1 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.turma = turma.codigo");
		sqlStr.append(" and programacaotutoriaonline.disciplina = disciplina.codigo");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'TURMA' ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		sqlStr.append(" union");
		//Consulta Por Unidade Ensino , curso e disciplina
		sqlStr.append(" select 2 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.curso = curso.codigo");
		sqlStr.append(" and programacaotutoriaonline.disciplina = disciplina.codigo");
		sqlStr.append(" and programacaotutoriaonline.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'CURSO' ");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		sqlStr.append(" union");
		//Consulta Por curso e disciplina
		sqlStr.append(" select 3 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.curso = curso.codigo");
		sqlStr.append(" and programacaotutoriaonline.disciplina = disciplina.codigo");
		sqlStr.append(" and programacaotutoriaonline.unidadeensino is null");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'CURSO' ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		sqlStr.append(" union");
		//Consulta Por Unidade Ensino , curso , disciplina e niveleducacional
		sqlStr.append(" select 4 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.curso = curso.codigo");
		sqlStr.append(" and programacaotutoriaonline.disciplina = disciplina.codigo");
		sqlStr.append(" and programacaotutoriaonline.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'NIVEL_EDUCACIONAL' ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		sqlStr.append(" and programacaotutoriaonline.niveleducacional = curso.niveleducacional ");
		sqlStr.append(" union");
		//Consulta Por  curso , disciplina e niveleducacional
		sqlStr.append(" select 5 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.curso = curso.codigo");
		sqlStr.append(" and programacaotutoriaonline.disciplina = disciplina.codigo");
		sqlStr.append(" and programacaotutoriaonline.unidadeensino is null");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'NIVEL_EDUCACIONAL' ");
		sqlStr.append(" and programacaotutoriaonline.niveleducacional = curso.niveleducacional ");
		sqlStr.append(" union");
		//Consulta Por Unidade Ensino , disciplina 
		sqlStr.append(" select 6 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.unidadeensino = unidadeensino.codigo");
		sqlStr.append(" and programacaotutoriaonline.disciplina = disciplina.codigo");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'UNIDADE_ENSINO' ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		sqlStr.append(" union");
		//Consulta Por disciplina 
		sqlStr.append(" select 7 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonline.disciplina = disciplina.codigo");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'DISCIPLINA' ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
		sqlStr.append(" order by ordem limit 1");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		ProgramacaoTutoriaOnlineVO obj = null;

		if (rs.next()) {
			obj = new ProgramacaoTutoriaOnlineVO();
			obj.setCodigo(rs.getInt("codigoprogramacao"));
			return consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		} else {
			return new ProgramacaoTutoriaOnlineVO();
		}
	}

	/**
	 * Método responsável por selecionar o tutor para o aluno que está acessando o estudo on-line pela primeira vez. Caso a definição seja
	 * PROGRAMACAO_DE_AULA, o mesmo pegará o professor da programação de aula daquela disciplina. Caso seja DINAMICA, deverá exister uma Programação Tutoria On-line
	 * configurada para o mesmo encontrar o professor e inseri-lo no aluno. Caso nenhum dos dois encontre um professor, um Throws será eviado para o Aluno.
	 * 
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized void realizarDefinicaoProfessorTutoriaOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO, Boolean retornarExcecao, boolean consultarDisciplinaEquivalente) throws Exception {
		if (matriculaPeriodoTurmaDisciplinaVO.getProfessor().getCodigo() == 0) {
			DefinicoesTutoriaOnlineEnum definicoes = getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
			if (definicoes == null || definicoes.isProgramacaoAula()) {
				if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica())) {
					matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(getFacadeFactory().getPessoaFacade().consultarCodigoProfessorPorHorarioTurmaDetalhadoTurmaDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), consultarDisciplinaEquivalente));
				}else if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica())) {
					matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(getFacadeFactory().getPessoaFacade().consultarCodigoProfessorPorHorarioTurmaDetalhadoTurmaDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), consultarDisciplinaEquivalente));
				}else {
					matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(getFacadeFactory().getPessoaFacade().consultarCodigoProfessorPorHorarioTurmaDetalhadoTurmaDisciplinaAnoSemestre(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), consultarDisciplinaEquivalente));
				}
				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirProfessorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO);
				if (matriculaPeriodoTurmaDisciplinaVO.getProfessor().getCodigo() == 0) {
					throw new Exception(UteisJSF.internacionalizar("msg_NenhumTutorEncontrado").replace("{0}", matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getNome()));
				}
			} else if (definicoes != null && definicoes.isDinamica()) {
				ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
				programacaoTutoriaOnlineVO = consultarProgramacaoTutoriaOnlinePorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
				if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getCodigo())) {					
//					boolean todosTutoresLotados = false;
//					for (ProgramacaoTutoriaOnlineProfessorVO tutorAtivo : programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs()) {
//						if(!tutorAtivo.isQtdAlunoTutoriaMaiorQueAlunoAtivos()) {
//							todosTutoresLotados = true;
//						}else {
//							todosTutoresLotados = false;
//						}
//					}
//					
//					if(todosTutoresLotados) {
//						consultarProfessorPorRegraDistribuicaoTutoriaSequenciada(programacaoTutoriaOnlineVO, null, matriculaPeriodoTurmaDisciplinaVO, true, usuarioVO, todosTutoresLotados);
//					}else {
//					
//						if (programacaoTutoriaOnlineVO.getRegraDistribuicaoTutoria().equals(RegraDistribuicaoTutoriaEnum.SEQUENCIADA)) {
//							consultarProfessorPorRegraDistribuicaoTutoriaSequenciada(programacaoTutoriaOnlineVO, null, matriculaPeriodoTurmaDisciplinaVO, true, usuarioVO, false);
//						} else if (programacaoTutoriaOnlineVO.getRegraDistribuicaoTutoria().equals(RegraDistribuicaoTutoriaEnum.QUANTITATIVA)) {
//							consultarProfessorPorRegraDistribuicaoTutoriaQuantitativa(programacaoTutoriaOnlineVO, null, matriculaPeriodoTurmaDisciplinaVO, true, usuarioVO);
//						} else {
//							consultarProfessorPorRegraDistribuicaoTutoriaPrioritaria(programacaoTutoriaOnlineVO, null, matriculaPeriodoTurmaDisciplinaVO, true, usuarioVO);
//						}
//					}
				} else {
					if(retornarExcecao) {
						throw new Exception(UteisJSF.internacionalizar("msg_NenhumaProgramacaoTutoriaOnlineEncontrada"));
					}
				}
			}
			
		}
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRedistruibuicaoAlunosEntreTutoresAtivos(ProgramacaoTutoriaOnlineVO obj, ProgramacaoTutoriaOnlineProfessorVO tutorInativo, UsuarioVO usuarioVO) throws Exception {
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = tutorInativo.getMatriculaPeriodoTurmaDisciplinaVOs().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			boolean realizazouDistruibuicao = false;
			if(mptd.getSelecionarMatricula()){
				if (obj.getRegraDistribuicaoTutoria().equals(RegraDistribuicaoTutoriaEnum.SEQUENCIADA)) {
					realizazouDistruibuicao = consultarProfessorPorRegraDistribuicaoTutoriaSequenciada(obj, tutorInativo, mptd, false, usuarioVO, false);
				} else if (obj.getRegraDistribuicaoTutoria().equals(RegraDistribuicaoTutoriaEnum.QUANTITATIVA)) {
					realizazouDistruibuicao =  consultarProfessorPorRegraDistribuicaoTutoriaQuantitativa(obj, tutorInativo, mptd, false, usuarioVO);
				} else {
					realizazouDistruibuicao =  consultarProfessorPorRegraDistribuicaoTutoriaPrioritaria(obj, tutorInativo, mptd, false, usuarioVO);
				}
				if(!realizazouDistruibuicao){
					throw new Exception(UteisJSF.internacionalizar("msg_ProgramacaoTutoriaOnline_validarDistribuicaoTutores"));		
				}
				i.remove();
			}
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private boolean consultarProfessorPorRegraDistribuicaoTutoriaPrioritaria(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO tutorInativo, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, boolean forcaDistribuicao, UsuarioVO usuarioVO) throws Exception {
		boolean realizouDistribuicao = false;
		for (ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO : programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs()) {
			if (programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().isSituacaoAtivo() 
					&& programacaoTutoriaOnlineProfessorVO.isQtdAlunoTutoriaMaiorQueAlunoAtivos()
					&& (!Uteis.isAtributoPreenchido(tutorInativo) || !programacaoTutoriaOnlineProfessorVO.getCodigo().equals(tutorInativo.getCodigo()))) {
				persistirDistribuicaoProgramacaoTutoriaOnlineProfessor(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, matriculaPeriodoTurmaDisciplinaVO, false, usuarioVO);
				realizouDistribuicao = true;
				break;
			}
		}
		if(!realizouDistribuicao && forcaDistribuicao){
			realizarDistribuicaoQuantitativaForcadaPoisNaoConseguiRealizarDistribuicaoPadrao(programacaoTutoriaOnlineVO, matriculaPeriodoTurmaDisciplinaVO, tutorInativo, usuarioVO);
			realizouDistribuicao = true;
		}
		return realizouDistribuicao;
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private boolean consultarProfessorPorRegraDistribuicaoTutoriaSequenciada(ProgramacaoTutoriaOnlineVO obj, ProgramacaoTutoriaOnlineProfessorVO tutorInativo, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, boolean forcaDistribuicao, UsuarioVO usuarioVO, boolean todosTutoresLotados) throws Exception {
		boolean realizouDistribuicao = false;
		int tamanhoListaTutor = obj.getProgramacaoTutoriaOnlineProfessorVOs().size();
		proximaSequencia:
			for (int i = 0; i < tamanhoListaTutor; i++) {
				obj.setOrdemAtual(obj.getOrdemAtual() + 1);
				if (obj.getOrdemAtual() > tamanhoListaTutor) {
					obj.setOrdemAtual(1);
				}
				for (ProgramacaoTutoriaOnlineProfessorVO tutorAtivo : obj.getProgramacaoTutoriaOnlineProfessorVOs()) {
					if (tutorAtivo.getOrdemPrioridade().equals(obj.getOrdemAtual())) {
						if(!todosTutoresLotados) {
							if (tutorAtivo.getSituacaoProgramacaoTutoriaOnline().isSituacaInativo() 
									|| !tutorAtivo.isQtdAlunoTutoriaMaiorQueAlunoAtivos()
									|| (Uteis.isAtributoPreenchido(tutorInativo) && tutorAtivo.getCodigo().equals(tutorInativo.getCodigo()))) {
								continue proximaSequencia;
							}
						}
						persistirDistribuicaoProgramacaoTutoriaOnlineProfessor(obj, tutorAtivo, matriculaPeriodoTurmaDisciplinaVO, true, usuarioVO);
						realizouDistribuicao = true;
						break ;
					}
				}
				if(!realizouDistribuicao && forcaDistribuicao){
					realizarDistribuicaoQuantitativaForcadaPoisNaoConseguiRealizarDistribuicaoPadrao(obj, matriculaPeriodoTurmaDisciplinaVO, tutorInativo, usuarioVO);
					realizouDistribuicao = true;
				}
				break ;
			}
		return realizouDistribuicao;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private boolean consultarProfessorPorRegraDistribuicaoTutoriaQuantitativa(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO tutorInativo, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, boolean forcaDistribuicao, UsuarioVO usuarioVO) throws Exception {
		boolean realizouDistribuicao = false;
		Ordenacao.ordenarLista(programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs(), "ordenacaoDistribuicaoQuantitativa");
		for (ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO : programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs()) {
			if (programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().isSituacaoAtivo() 
					&& programacaoTutoriaOnlineProfessorVO.isQtdAlunoTutoriaMaiorQueAlunoAtivos()
					&& (!Uteis.isAtributoPreenchido(tutorInativo) || !programacaoTutoriaOnlineProfessorVO.getCodigo().equals(tutorInativo.getCodigo()))) {
				persistirDistribuicaoProgramacaoTutoriaOnlineProfessor(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, matriculaPeriodoTurmaDisciplinaVO, false, usuarioVO);
				realizouDistribuicao = true;
				break;
			}
		}
		if(!realizouDistribuicao && forcaDistribuicao){
			realizarDistribuicaoQuantitativaForcadaPoisNaoConseguiRealizarDistribuicaoPadrao(programacaoTutoriaOnlineVO, matriculaPeriodoTurmaDisciplinaVO, tutorInativo, usuarioVO);
			realizouDistribuicao = true;
		}
		return realizouDistribuicao;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void persistirDistribuicaoProgramacaoTutoriaOnlineProfessor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, boolean isAlterarProgramacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception{
		if(isAlterarProgramacaoTutoriaOnlineVO){
			atutalizarOrdemAtualProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, false, usuarioVO);	
		}
		matriculaPeriodoTurmaDisciplinaVO.getProfessor().setCodigo(programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo());
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirProgramacaoTutoriaOnlineProfessorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO, programacaoTutoriaOnlineProfessorVO.getCodigo());
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirProfessorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO);
		programacaoTutoriaOnlineProfessorVO.setQtdeAlunosAtivos(programacaoTutoriaOnlineProfessorVO.getQtdeAlunosAtivos() + 1);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void realizarDistribuicaoQuantitativaForcadaPoisNaoConseguiRealizarDistribuicaoPadrao(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ProgramacaoTutoriaOnlineProfessorVO tutorInativo, UsuarioVO usuarioVO) throws Exception {
		Ordenacao.ordenarLista(programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs(), "ordenacaoDistribuicaoQuantitativa");
		for (ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO : programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs()) {
			if (programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().isSituacaoAtivo() && (!Uteis.isAtributoPreenchido(tutorInativo) || !programacaoTutoriaOnlineProfessorVO.getCodigo().equals(tutorInativo.getCodigo()))) {
				persistirDistribuicaoProgramacaoTutoriaOnlineProfessor(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, matriculaPeriodoTurmaDisciplinaVO, false, usuarioVO);
				break;	
			}			
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void ativarProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		if(programacaoTutoriaOnlineVO.getCodigo() == 0) {
			throw new Exception("Você Deve Gravar A Programação Primeiro");
		}
		validarDados(programacaoTutoriaOnlineVO, usuarioVO);
		programacaoTutoriaOnlineVO.setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum.ATIVO);
		atutalizarSituacaoTutoriaProgramacaoTutoriaOnlineProfessor(programacaoTutoriaOnlineVO, false, usuarioVO);		
		verificarSeExisteAlunoNaoVinculadoAoProfessor(programacaoTutoriaOnlineVO, usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarSeExisteAlunoNaoVinculadoAoProfessor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
    	List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaSemTutor(programacaoTutoriaOnlineVO, usuarioVO);
		if(!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			definirProfessorTutoriaOnlineEGerarCalendarioAtividadeMatriculaAcessoConteudoEstudo(programacaoTutoriaOnlineVO, matriculaPeriodoTurmaDisciplinaVOs, usuarioVO);				
		}
    }
    
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void definirProfessorTutoriaOnlineEGerarCalendarioAtividadeMatriculaAcessoConteudoEstudo(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {
			
			 getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().realizarDefinicaoProfessorTutoriaOnline(matriculaPeriodoTurmaDisciplinaVO, usuarioVO, false, false);
			 ConfiguracaoEADVO configuracaoEADVO = getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
			 CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
//			 getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, programacaoTutoriaOnlineVO, usuarioVO);
			 /*Date dataInicio = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, getProgramacaoTutoriaOnlineVO(),  getUsuarioLogado());
			 if(dataInicio != null) {
				 calendarioAtividadeMatriculaVO.setDataInicio(dataInicio);
			 }else {
				 calendarioAtividadeMatriculaVO.setDataInicio(new Date());
			 }*/
			 					 					 
//			 getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoEstudo(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioVO, programacaoTutoriaOnlineVO);
		}
		for(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO : programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs()) {
			programacaoTutoriaOnlineProfessorVO.setQtdeAlunosAtivos(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarQtdAlunosAtivoPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO));
		}
    }
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void desativarProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		programacaoTutoriaOnlineVO.setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum.INATIVO);
		atutalizarSituacaoTutoriaProgramacaoTutoriaOnlineProfessor(programacaoTutoriaOnlineVO, false, usuarioVO);
	}
	
	public void alterarDataCalendarioAtividadeMatricula(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		
		List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs =  new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
    	
		matriculaPeriodoTurmaDisciplinaVOs = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorCodigoTurmaDisciplinaSemestreAno(programacaoTutoriaOnlineVO.getTurmaVO(), programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo(), programacaoTutoriaOnlineVO.getAno(), programacaoTutoriaOnlineVO.getSemestre(), false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		if(!matriculaPeriodoTurmaDisciplinaVOs.isEmpty()) {
			for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : matriculaPeriodoTurmaDisciplinaVOs) {

				ConfiguracaoEADVO configuracaoEADVO = getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
				 
				CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
//				calendarioAtividadeMatriculaVO = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO,  Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);

				calendarioAtividadeMatriculaVO.setDataInicio(programacaoTutoriaOnlineVO.getDataInicioAula());
				calendarioAtividadeMatriculaVO.setDataFim(programacaoTutoriaOnlineVO.getDataTerminoAula());
//				getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().alterarDataCalendarioAtividadeMatricula(calendarioAtividadeMatriculaVO, false, usuarioVO);

			}
				 
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void executarCargaProgramacaoTutoriaOnlineClassroomAutomatico(ProgramacaoTutoriaOnlineVO obj, UsuarioVO usuario) throws Exception {		
		for (ProgramacaoTutoriaOnlineProfessorVO ptop : obj.getProgramacaoTutoriaOnlineProfessorVOs()) {
			List<MatriculaPeriodoTurmaDisciplinaVO> lista = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaGeracaoPorCargaAutomaticas(obj,  ptop, 0, true, usuario);
			for (MatriculaPeriodoTurmaDisciplinaVO mptd : lista) {
				ClassroomGoogleVO classroom = new ClassroomGoogleVO();
				classroom.setTurmaVO(mptd.getTurma());
				classroom.setDisciplinaVO(mptd.getDisciplina());
				classroom.setProfessorEad(ptop.getProfessor()); 
				classroom.setAno(mptd.getAno());
				classroom.setSemestre(mptd.getSemestre());				
//				getFacadeFactory().getClassroomGoogleFacade().realizarGeracaoClassroomGooglePorProgramacaoTutoriaOnline(obj, classroom, usuario);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void executarCargaProgramacaoTutoriaOnlineSalaAulaBlackboardAutomatico(ProgramacaoTutoriaOnlineVO obj, Integer bimestre, UsuarioVO usuario) throws Exception {		
			
			List<MatriculaPeriodoTurmaDisciplinaVO> lista = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarMatriculaPeriodoTurmaDisciplinaGeracaoPorCargaAutomaticas(obj, null, bimestre, false, usuario);
			List<SalaAulaBlackboardVO> salas =  new ArrayList<SalaAulaBlackboardVO>();
			for (MatriculaPeriodoTurmaDisciplinaVO mptd : lista) {				
				if(!salas.stream().anyMatch(s -> s.getAno().equals(mptd.getAno()) &&  s.getSemestre().equals(mptd.getSemestre()))) {
				SalaAulaBlackboardVO sab = new SalaAulaBlackboardVO();
				sab.setTipoSalaAulaBlackboardEnum(TipoSalaAulaBlackboardEnum.DISCIPLINA);				
				if(obj.getTipoNivelProgramacaoTutoria().isCurso()) {
					sab.setCursoVO(obj.getCursoVO());
				}
				if(obj.getTipoNivelProgramacaoTutoria().isTurma()) {
					sab.setTurmaVO(obj.getTurmaVO());	
				}
				sab.setDisciplinaVO(obj.getDisciplinaVO());
				sab.setBimestre(bimestre);
				for (ProgramacaoTutoriaOnlineProfessorVO ptop : obj.getProgramacaoTutoriaOnlineProfessorVOs()) {
					if(ptop.getSituacaoProgramacaoTutoriaOnline().equals(SituacaoProgramacaoTutoriaOnlineEnum.ATIVO)) {
						PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(ptop.getProfessor().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
						if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
							sab.getListaProfessores().add(new SalaAulaBlackboardPessoaVO(sab, TipoSalaAulaBlackboardPessoaEnum.PROFESSOR, pessoaEmailInstitucionalVO, null, null));
						}
					}
				}
				sab.setAno(mptd.getAno());
				sab.setSemestre(mptd.getSemestre());
				salas.add(sab);
				}				
			}				
			for (SalaAulaBlackboardVO sab : salas) {			
				getFacadeFactory().getSalaAulaBlackboardFacade().realizarGeracaoSalaAulaBlackboardPorProgramacaoTutoriaOnline(obj, sab, usuario);			
			}
			
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineVO> consultarPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, int nivelMontarDados, UsuarioVO usuarioLogado, boolean validarAulaNaoRegistrada) throws Exception {
		StringBuilder sql = new StringBuilder("");			
		for (MatriculaPeriodoTurmaDisciplinaVO mp : matriculaPeriodoTurmaDisciplinaVOs) {
			if(mp.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
				if(sql.length() > 0) {
					sql.append(" union all ");
				}
				sql.append(" select programacaotutoriaonline.dataInicioAula, dataTerminoAula, disciplina.nome as disciplina from programacaotutoriaonline ");
				sql.append(" inner join turmadisciplina on turmadisciplina.turma=programacaotutoriaonline.turma and turmadisciplina.disciplina=programacaotutoriaonline.disciplina");
				sql.append(" inner join turma on turmadisciplina.turma = turma.codigo");
				sql.append(" inner join disciplina on turmadisciplina.disciplina = disciplina.codigo");
				sql.append(" where turmadisciplina.modalidadedisciplina = 'ON_LINE'");
				sql.append(" and turmadisciplina.definicoestutoriaonline = 'DINAMICA'");
				sql.append(" and programacaotutoriaonline.definirperiodoaulaonline is true ");
				
				sql.append(" and ((turma.semestral and programacaotutoriaonline.ano = '").append(mp.getAno()).append("' ");
				sql.append(" and programacaotutoriaonline.semestre = '").append(mp.getSemestre()).append("') ");
				sql.append(" or (turma.anual and programacaotutoriaonline.ano = '").append(mp.getAno()).append("') ");
				sql.append(" or (turma.semestral = false and turma.anual = false)) ");		
				sql.append(" and turmadisciplina.turma = ").append(mp.getTurma().getCodigo());
				sql.append(" and turmadisciplina.disciplina = ").append(mp.getDisciplina().getCodigo()).append(" ");
				if(validarAulaNaoRegistrada) {
					sql.append(" and dataTerminoAula >= '").append(Uteis.getDataJDBC(new Date())).append("'");
				}
			}			
		}
		List<ProgramacaoTutoriaOnlineVO> vetResultado = new ArrayList<ProgramacaoTutoriaOnlineVO>(0);
		if(sql.length() > 0) {
			sql.append(" order by dataInicioAula, dataTerminoAula ");
			if(validarAulaNaoRegistrada) {
				sql.append(" limit 1 ");
			}
			if(validarAulaNaoRegistrada && getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next()) {
				throw new Exception("Existem aulas com registro pendente na sua turma. Por favor, entre em contato com a Secretaria Acadêmica.");
			}else {
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO = null;
				while(rs.next()) {
					programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
					programacaoTutoriaOnlineVO.setDataInicioAula(rs.getDate("dataInicioAula"));
					programacaoTutoriaOnlineVO.setDataTerminoAula(rs.getDate("dataTerminoAula"));
					programacaoTutoriaOnlineVO.getDisciplinaVO().setNome(rs.getString("disciplina"));
					vetResultado.add(programacaoTutoriaOnlineVO);
				}
			}
		}
		return vetResultado;
	}
	
	public Integer programacaoTutoriaOnlinePersistidoNoBancoComParametrosPassados(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select codigo from programacaotutoriaonline where 1=1 ");
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo())) {
			sqlStr.append(" and turma = ").append(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo());
		} else {
			sqlStr.append(" and turma is null ");
		}
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo())) {
			sqlStr.append(" and unidadeensino = ").append(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo());
		} else {
			sqlStr.append(" and unidadeensino is null");
		}
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getCursoVO().getCodigo())) {
			sqlStr.append(" and curso = ").append(programacaoTutoriaOnlineVO.getCursoVO().getCodigo());
		} else {
			sqlStr.append(" and curso is null");
		}
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getNivelEducacional())) {
			sqlStr.append(" and niveleducacional = '").append(programacaoTutoriaOnlineVO.getNivelEducacional()).append("'");
		} else {
			sqlStr.append(" and length(niveleducacional) = 0");
		}
		sqlStr.append(" and disciplina = ").append(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo());
		sqlStr.append(" and regradistribuicaotutoria = '").append(programacaoTutoriaOnlineVO.getRegraDistribuicaoTutoria()).append("'");
		sqlStr.append(" and situacao = '").append(SituacaoProgramacaoTutoriaOnlineEnum.ATIVO).append("'");
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO.getCodigo())){
			sqlStr.append(" and codigo != ").append(programacaoTutoriaOnlineVO.getCodigo());	
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		if (rs.next()) {
			return rs.getInt("codigo");
		}
		else {
			return 0;
		}
		
		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean isProgramacaoTutoriaOnlineValidoHorario(Integer professor, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Date dataInicio, Date dataTermino) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count(programacaotutoriaonline.codigo) QTDE from programacaotutoriaonline ");
		sqlStr.append(" inner join  programacaotutoriaonlineprofessor  on programacaotutoriaonlineprofessor.programacaotutoriaonline   = programacaotutoriaonline.codigo ");
		sqlStr.append(" where 1=1 ");
		sqlStr.append(" and ((programacaotutoriaonline.definirperiodoaulaonline ");
		sqlStr.append(" and programacaotutoriaonline.datainicioaula <= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
		sqlStr.append(" and programacaotutoriaonline.dataterminoaula >= '").append(Uteis.getDataJDBC(dataTermino)).append("') ");
		sqlStr.append(" or programacaotutoriaonline.definirperiodoaulaonline = false )");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and (programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" or (programacaotutoriaonline.unidadeensino is null and programacaotutoriaonline.tiponivelprogramacaotutoria != 'UNIDADE_ENSINO')) ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and (programacaotutoriaonline.curso = ").append(curso).append(" or (programacaotutoriaonline.curso is null and programacaotutoriaonline.tiponivelprogramacaotutoria != 'CURSO')) ");
		} 
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and (programacaotutoriaonline.turma = ").append(turma).append(" or (programacaotutoriaonline.turma is null and programacaotutoriaonline.tiponivelprogramacaotutoria != 'TURMA')) ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and programacaotutoriaonline.disciplina = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(professor)) {
			sqlStr.append(" and programacaotutoriaonlineprofessor.professor = ").append(professor);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	
	/**
	 * Método responsável por localizar a programação tutoria on-line configurada priorizando a programação configurada para disciplina da turma do semestre.
	 * O mesmo será chamado no momento em que o aluno acessar o estudo on-line pela primera vez.
	 * 
	 * @param matriculaPeriodoTurmaDisciplinaVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	@Override
	public ProgramacaoTutoriaOnlineVO consultarProgramacaoTutoriaOnlinePorTurmaDisciplinaAnoSemestre(TurmaVO turma, Integer disciplina, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		//Consulta Por Turma e disciplina
		sqlStr.append("	( select ordem, codigoprogramacao from ( ");
		sqlStr.append("	select 1 as ordem, programacaotutoriaonline.codigo as codigoprogramacao, programacaotutoriaonline.ano||programacaotutoriaonline.semestre as anosemestre from programacaotutoriaonline ");		
		sqlStr.append(" where programacaotutoriaonline.situacao = 'ATIVO' and  programacaotutoriaonline.turma is not null and  programacaotutoriaonline.turma = ").append(turma.getCodigo());
		sqlStr.append(" and programacaotutoriaonline.disciplina is not null and programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'TURMA' ");
		if(turma.getAnual()) {
			sqlStr.append(" and programacaotutoriaonline.ano <= ").append(ano);
}
		if(turma.getSemestral()) {
			sqlStr.append(" and programacaotutoriaonline.ano||programacaotutoriaonline.semestre <= ").append(ano).append(semestre);
		}
		sqlStr.append(" order by anosemestre desc) as t limit 1) ");
		sqlStr.append(" union");
		//Consulta Por Unidade Ensino , curso e disciplina
		sqlStr.append("	( select ordem, codigoprogramacao from (");
		sqlStr.append(" select 2 as ordem, programacaotutoriaonline.codigo as codigoprogramacao , programacaotutoriaonline.ano||programacaotutoriaonline.semestre as anosemestre from programacaotutoriaonline ");	
		sqlStr.append(" where programacaotutoriaonline.curso = ").append(turma.getCurso().getCodigo());
		sqlStr.append(" and programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" and programacaotutoriaonline.unidadeensino = ").append(turma.getUnidadeEnsino().getCodigo());
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'CURSO' ");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		if(turma.getAnual()) {
			sqlStr.append(" and programacaotutoriaonline.ano <= ").append(ano);
		}
		if(turma.getSemestral()) {
			sqlStr.append(" and programacaotutoriaonline.ano||programacaotutoriaonline.semestre <= ").append(ano).append(semestre);
		}
		sqlStr.append(" order by anosemestre desc) as t limit 1) ");
		
		sqlStr.append(" union");
		//Consulta Por curso e disciplina
		sqlStr.append("	( select ordem, codigoprogramacao from (");
		sqlStr.append(" select 3 as ordem, programacaotutoriaonline.codigo as codigoprogramacao , programacaotutoriaonline.ano||programacaotutoriaonline.semestre as anosemestre from programacaotutoriaonline");
		sqlStr.append(" where programacaotutoriaonline.curso = ").append(turma.getCurso().getCodigo());
		sqlStr.append(" and programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" and programacaotutoriaonline.unidadeensino is null");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'CURSO' ");
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO'");
		if(turma.getAnual()) {
			sqlStr.append(" and programacaotutoriaonline.ano <= ").append(ano);
		}
		if(turma.getSemestral()) {
			sqlStr.append(" and programacaotutoriaonline.ano||programacaotutoriaonline.semestre <= ").append(ano).append(semestre);
		}
		sqlStr.append(" order by anosemestre desc) as t limit 1) ");
		sqlStr.append(" union ");
		//Consulta Por Unidade Ensino , disciplina e niveleducacional
		sqlStr.append(" select 4 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from programacaotutoriaonline ");
		sqlStr.append(" where programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" and programacaotutoriaonline.unidadeensino = " ).append(turma.getUnidadeEnsino().getCodigo());
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO' ");
		sqlStr.append(" and programacaotutoriaonline.curso is null ");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'NIVEL_EDUCACIONAL' ");
		sqlStr.append(" and programacaotutoriaonline.niveleducacional = '").append(turma.getCurso().getNivelEducacional()).append("'");
		sqlStr.append(" union");
		//Consulta Por disciplina e niveleducacional
		sqlStr.append(" select 5 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from programacaotutoriaonline ");
		sqlStr.append(" where programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" and programacaotutoriaonline.unidadeensino is null " );
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO' ");
		sqlStr.append(" and programacaotutoriaonline.curso is null ");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'NIVEL_EDUCACIONAL' ");	
		sqlStr.append(" and programacaotutoriaonline.niveleducacional = '").append(turma.getCurso().getNivelEducacional()).append("'");
		sqlStr.append(" union");
		//Consulta Por Unidade Ensino , disciplina 
		sqlStr.append(" select 6 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from programacaotutoriaonline");
		sqlStr.append(" where programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" and programacaotutoriaonline.unidadeensino = " ).append(turma.getUnidadeEnsino().getCodigo());
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO' ");
		sqlStr.append(" and programacaotutoriaonline.curso is null ");		
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'UNIDADE_ENSINO' ");		
		sqlStr.append(" union");
		//Consulta Por disciplina 		
		sqlStr.append(" select 7 as ordem, programacaotutoriaonline.codigo as codigoprogramacao from programacaotutoriaonline");
		sqlStr.append(" where programacaotutoriaonline.disciplina = ").append(disciplina);
		sqlStr.append(" and programacaotutoriaonline.situacao = 'ATIVO' ");
		sqlStr.append(" and programacaotutoriaonline.tiponivelprogramacaotutoria = 'DISCIPLINA' ");		
		sqlStr.append(" order by ordem limit 1");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		ProgramacaoTutoriaOnlineVO obj = null;

		if (rs.next()) {
			obj = new ProgramacaoTutoriaOnlineVO();
			obj.setCodigo(rs.getInt("codigoprogramacao"));
			return consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		} else {
			return new ProgramacaoTutoriaOnlineVO();
		}
	}
}
