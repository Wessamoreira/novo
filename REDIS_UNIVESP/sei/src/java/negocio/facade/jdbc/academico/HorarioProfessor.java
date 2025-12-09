package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
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

import negocio.comuns.academico.CalendarioHorarioAulaVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DisponibilidadeHorarioTurmaProfessorVO;
import negocio.comuns.academico.DisponibilidadeHorarioVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.QuadroHorarioVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioProfessorInterfaceFacade;
import webservice.servicos.objetos.AgendaAlunoRSVO;
import webservice.servicos.objetos.DataEventosRSVO;
import webservice.servicos.objetos.enumeradores.OrigemAgendaAlunoEnum;

/**
 *
 * @author rodrigo
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class HorarioProfessor extends ControleAcesso implements HorarioProfessorInterfaceFacade {

	protected static String idEntidade;

	public HorarioProfessor() throws Exception {
		super();
		setIdEntidade("Professor");
	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#incluir(negocio.comuns.academico.HorarioProfessorVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HorarioProfessorVO obj, UsuarioVO usuario) throws Exception {

		final String sql = "INSERT INTO HorarioProfessor (professor, turno, segunda, terca, quarta, quinta, sexta, sabado, domingo) VALUES (?,?,?,?,?,?,?,?,?)returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getProfessor().getCodigo().intValue());
				sqlInserir.setInt(2, obj.getTurno().getCodigo().intValue());
				sqlInserir.setString(3, obj.getSegunda());
				sqlInserir.setString(4, obj.getTerca());
				sqlInserir.setString(5, obj.getQuarta());
				sqlInserir.setString(6, obj.getQuinta());
				sqlInserir.setString(7, obj.getSexta());
				sqlInserir.setString(8, obj.getSabado());
				sqlInserir.setString(9, obj.getDomingo());
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

	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#alterar(negocio.comuns.academico.HorarioProfessorVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HorarioProfessorVO obj, UsuarioVO usuario) throws Exception {

		final String sql = "UPDATE HorarioProfessor SET professor=?, turno=?, segunda=?, terca=?, quarta=?, quinta=?, sexta=?, sabado=?, domingo=? WHERE ((codigo=?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getProfessor().getCodigo().intValue());
				sqlAlterar.setInt(2, obj.getTurno().getCodigo().intValue());
				sqlAlterar.setString(3, obj.getSegunda());
				sqlAlterar.setString(4, obj.getTerca());
				sqlAlterar.setString(5, obj.getQuarta());
				sqlAlterar.setString(6, obj.getQuinta());
				sqlAlterar.setString(7, obj.getSexta());
				sqlAlterar.setString(8, obj.getSabado());
				sqlAlterar.setString(9, obj.getDomingo());
				sqlAlterar.setInt(10, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
		

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSometeHorariosSemana(final HorarioProfessorVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE HorarioProfessor SET professor=?, turno=?, segunda=?, terca=?, quarta=?, quinta=?, sexta=?, sabado=?, domingo=? WHERE ((codigo=?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getProfessor().getCodigo().intValue());
				sqlAlterar.setInt(2, obj.getTurno().getCodigo().intValue());
				sqlAlterar.setString(3, obj.getSegunda());
				sqlAlterar.setString(4, obj.getTerca());
				sqlAlterar.setString(5, obj.getQuarta());
				sqlAlterar.setString(6, obj.getQuinta());
				sqlAlterar.setString(7, obj.getSexta());
				sqlAlterar.setString(8, obj.getSabado());
				sqlAlterar.setString(9, obj.getDomingo());
				sqlAlterar.setInt(10, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});		
	}

	/**
	 * Método responsável por Alterar os dados do horario do professor sem
	 * modificar os horario professor dia, que representa cada dia de aula
	 * 
	 * @param horarioProfessor
	 * @param turno
	 *            - código do turno
	 * 
	 * 
	 * @exception Exception
	 * 
	 * @author Danilo
	 * 
	 * @since 17.02.2011
	 * 
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHorarioProfessorPorTurno(final HorarioProfessorVO horarioProfessor, final Integer turno, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE HorarioProfessor SET professor=?, turno=?, segunda=?, terca=?, quarta=?, quinta=?, sexta=?, sabado=?, domingo=? WHERE (codigo=? and turno = ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, horarioProfessor.getProfessor().getCodigo().intValue());
				sqlAlterar.setInt(2, horarioProfessor.getTurno().getCodigo().intValue());
				sqlAlterar.setString(3, horarioProfessor.getSegunda());
				sqlAlterar.setString(4, horarioProfessor.getTerca());
				sqlAlterar.setString(5, horarioProfessor.getQuarta());
				sqlAlterar.setString(6, horarioProfessor.getQuinta());
				sqlAlterar.setString(7, horarioProfessor.getSexta());
				sqlAlterar.setString(8, horarioProfessor.getSabado());
				sqlAlterar.setString(9, horarioProfessor.getDomingo());
				sqlAlterar.setInt(10, horarioProfessor.getCodigo().intValue());
				sqlAlterar.setInt(11, turno.intValue());
				return sqlAlterar;
			}
		});		
	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#excluir(negocio.comuns.academico.HorarioProfessorVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(HorarioProfessorVO obj, UsuarioVO usuario) throws Exception {
		HorarioProfessor.excluir(getIdEntidade());
		String sql = "DELETE FROM HorarioProfessor WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		getFacadeFactory().getHorarioProfessorDiaFacade().excluirHorarioProfessorDias(obj.getCodigo(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarDisponibilidadeProfessorApartirDisciplinaHorarioTurma(HorarioProfessorVO horarioProfessorVO, HorarioTurmaVO horarioTurma, Boolean alterarTodasAulas, UsuarioVO usuario) throws ConsistirException, Exception {
		verificarDisponibilidadeProfessorApartirDisciplinaHorarioTurmaDiario(horarioProfessorVO, horarioTurma, alterarTodasAulas, usuario);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarDisponibilidadeProfessorApartirDisciplinaHorarioTurmaDiario(HorarioProfessorVO horarioProfessorVO, HorarioTurmaVO horarioTurma, Boolean alterarTodasAulas, UsuarioVO usuario) throws ConsistirException {
		Integer nrAula = 0;
		Integer x = 1;
		for (HorarioTurmaDiaVO dia : horarioTurma.getHorarioTurmaDiaVOs()) {
			x = 1;
			nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioTurma.getTurno(), dia.getDiaSemanaEnum());
			while (x <= nrAula) {
				if (horarioTurma.getDisciplinaAtual().getCodigo().equals(dia.getCodigoDisciplina(nrAula))) {
					if (horarioProfessorVO.getCodigoDisciplina(nrAula, dia.getDiaSemanaEnum()) != 0) {
						throw new ConsistirException("O professor " + horarioTurma.getProfessorSubstituto().getNome().toUpperCase() + " não possui disponibilidade para os horários desta disciplina.");
					}
					HorarioProfessorDiaVO horarioProfessorDiaVO = consultarHorarioProfessorPorDiaPorDia(horarioProfessorVO, dia.getData(), usuario);
					if (horarioProfessorDiaVO.getCodigo().intValue() != 0 && horarioProfessorDiaVO.getCodigoDisciplina(nrAula) != 0 && ((horarioProfessorDiaVO.getCodigoTurma(nrAula).equals(horarioTurma.getTurma().getCodigo()) && !horarioProfessorDiaVO.getCodigoDisciplina(nrAula).equals(dia.getCodigoDisciplina(nrAula))) || (!horarioProfessorDiaVO.getCodigoTurma(nrAula).equals(horarioTurma.getTurma().getCodigo())))) {
						throw new ConsistirException("O professor " + horarioTurma.getProfessorSubstituto().getNome().toUpperCase() + " não possui disponibilidade para os horários desta disciplina.");
					}
				}
				x++;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public QuadroHorarioVO atualizarDadosQuadroHorario(HorarioProfessorVO horarioProfessor, QuadroHorarioVO quadroHorarioVO, Boolean detalhado, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		Integer x = 1;
		Integer nrAula = 0;
		quadroHorarioVO.setDisciplina(new DisciplinaVO());
		quadroHorarioVO.setTurma(new TurmaVO());
		quadroHorarioVO.setHorarioProfessorVO(horarioProfessor);
		TurnoHorarioVO turnoHorarioVO = null;
		for (DiaSemana diaSemana : DiaSemana.values()) {
			if (diaSemana.equals(DiaSemana.NENHUM)) {
				continue;
			}
			nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(quadroHorarioVO.getTurno(), diaSemana);
			x = 1;
			while (x <= nrAula) {
				turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(quadroHorarioVO.getTurno(), diaSemana, x);
				DisponibilidadeHorarioVO disponibilidadeHorarioVO = montarDadosDisponibilidadeAPartiHorarioProfessor(horarioProfessor, quadroHorarioVO, x, diaSemana, turnoHorarioVO.getHorarioInicioAula(), turnoHorarioVO.getHorarioFinalAula(), detalhado, usuario);
				switch (diaSemana) {
				case DOMINGO: {
					quadroHorarioVO.atualizarListaDomingo(disponibilidadeHorarioVO);
					break;
				}
				case SEGUNGA: {
					quadroHorarioVO.atualizarListaSegunda(disponibilidadeHorarioVO);
					break;
				}
				case TERCA: {
					quadroHorarioVO.atualizarListaTerca(disponibilidadeHorarioVO);
					break;
				}
				case QUARTA: {
					quadroHorarioVO.atualizarListaQuarta(disponibilidadeHorarioVO);
					break;
				}
				case QUINTA: {
					quadroHorarioVO.atualizarListaQuinta(disponibilidadeHorarioVO);
					break;
				}
				case SEXTA: {
					quadroHorarioVO.atualizarListaSexta(disponibilidadeHorarioVO);
					break;
				}
				case SABADO: {
					quadroHorarioVO.atualizarListaSabado(disponibilidadeHorarioVO);
					break;
				}
				}
				x++;
			}
		}
		quadroHorarioVO.ordenarListas();
		return quadroHorarioVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DisponibilidadeHorarioVO montarDadosDisponibilidadeAPartiHorarioProfessor(HorarioProfessorVO obj, QuadroHorarioVO quadroHorarioVO, Integer nrAula, DiaSemana diaSemana, String horaInicio, String horaFinal, Boolean detalhado, UsuarioVO usuario) throws Exception {
		DisponibilidadeHorarioVO disponibilidade = new DisponibilidadeHorarioVO();
		disponibilidade.getTurno().setCodigo(quadroHorarioVO.getTurno().getCodigo());
		disponibilidade.setNrAula(nrAula);
		disponibilidade.setDiaSemana(diaSemana);
		disponibilidade.setDisponivelHorario(obj.getDisponibilidadeHorario(nrAula, diaSemana));
		disponibilidade.getDisciplina().setCodigo(obj.getCodigoDisciplina(nrAula, diaSemana));
		disponibilidade.getTurma().setCodigo(obj.getCodigoTurma(nrAula, diaSemana));
		disponibilidade.setAula(String.valueOf(nrAula.intValue()) + "ª Aula");
		disponibilidade.setIntervaloHora(horaInicio + " até " + horaFinal);
		if (disponibilidade.getDisciplina().getCodigo().intValue() > 0 && disponibilidade.getTurma().getCodigo().intValue() > 0) {
			if (detalhado) {
				if (!disponibilidade.getDisciplina().getCodigo().equals(quadroHorarioVO.getDisciplina().getCodigo())) {
					try {
						disponibilidade.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disponibilidade.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
					} catch (Exception e) {
						disponibilidade.setDisciplina(new DisciplinaVO());
					}
				} else {
					disponibilidade.setDisciplina(quadroHorarioVO.getDisciplina());
				}
				if (!disponibilidade.getTurma().getCodigo().equals(quadroHorarioVO.getTurma().getCodigo())) {
					disponibilidade.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(disponibilidade.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				} else {
					disponibilidade.setTurma(quadroHorarioVO.getTurma());
				}
			}
			disponibilidade.setExisteAula(true);
		} else if (disponibilidade.getDisciplina().getCodigo().intValue() == -1 && disponibilidade.getTurma().getCodigo().intValue() == -1) {
			disponibilidade.getDisciplina().setNome("Indisponivel");
			disponibilidade.getTurma().setIdentificadorTurma("Indisponivel");
		} else {
			disponibilidade.getDisciplina().setNome("Livre");
			disponibilidade.getTurma().setIdentificadorTurma("Livre");
		}
		return disponibilidade;
	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#consultarPorProfessorTurno(java.lang.Integer,
	 *      java.lang.Integer)
	 * 
	 *      Método responsável por retornar os horarios do professor objetos
	 *      <code>HorarioProfessorVO</code> baseados no professor e turno
	 * 
	 * @param codigoProfessor
	 * @param codigoTurno
	 * 
	 * @return HorarioProfessorVO
	 * 
	 */
	public HorarioProfessorVO consultarPorProfessorTurno(Integer codigoProfessor, Integer codigoTurno, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT horarioprofessor.professor, horarioprofessor.turno, horarioprofessor.segunda, ");
		sqlStr.append("horarioprofessor.terca, horarioprofessor.quarta, horarioprofessor.quinta, horarioprofessor.sexta, horarioprofessor.sabado, ");
		sqlStr.append("horarioprofessor.domingo, horarioprofessor.codigo ");
		sqlStr.append("FROM horarioprofessor ");
		sqlStr.append("INNER JOIN pessoa ON HorarioProfessor.professor = Pessoa.codigo ");
		sqlStr.append("INNER JOIN turno ON HorarioProfessor.turno = Turno.codigo ");
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoProfessor.intValue());
		sqlStr.append(" AND turno.codigo = ").append(codigoTurno.intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new HorarioProfessorVO();
		}
		return montarDados(tabelaResultado, dataInicio, dataFim, usuario);
	}

	/**
	 * 
	 * Método responsável por retornar os horarios do professor objetos
	 * <code>HorarioProfessorVO</code> baseados no professor
	 * 
	 * @param codigoProfessor
	 * 
	 * 
	 * @return HorarioProfessorVO
	 * 
	 * @throws Exception
	 */
	public List<HorarioProfessorVO> consultarPorProfessor(Integer codigoProfessor, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {

		StringBuilder sqlStr = new StringBuilder(" SELECT horarioprofessor.professor,horarioprofessor.turno,horarioprofessor.segunda, ");
		sqlStr.append(" horarioprofessor.terca,horarioprofessor.quarta,horarioprofessor.quinta,horarioprofessor.sexta, ");
		sqlStr.append(" horarioprofessor.sabado,horarioprofessor.domingo, horarioprofessor.codigo ");

		sqlStr.append(" FROM HorarioProfessor ");
		sqlStr.append(" WHERE professor = ").append(codigoProfessor);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return montarDadosListaHorarioProfessor(tabelaResultado, dataInicio, dataFim, usuario);
	}

	/***
	 * 
	 * Método responsável por montar os objetos <code>HorarioProfessorVO</code>
	 * 
	 * @param tabelaResultado
	 * @return List<HorarioProfessorVO>
	 * @throws Exception
	 */
	public List<HorarioProfessorVO> montarDadosListaHorarioProfessor(SqlRowSet tabelaResultado, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {

		List<HorarioProfessorVO> listaHorarioProfessor = new ArrayList<HorarioProfessorVO>(0);

		while (tabelaResultado.next()) {
			HorarioProfessorVO horarioProfessor = new HorarioProfessorVO();
			horarioProfessor = montarDados(tabelaResultado, dataInicio, dataFim, usuario);
			listaHorarioProfessor.add(horarioProfessor);
		}

		return listaHorarioProfessor;

	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			HorarioProfessorVO obj = new HorarioProfessorVO();
			obj = montarDados(tabelaResultado, dataInicio, dataFim, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DisponibilidadeHorarioVO</code>.
	 * 
	 * @return O objeto da classe <code>DisponibilidadeHorarioVO</code> com os
	 *         dados devidamente montados.
	 */
	public static HorarioProfessorVO montarDadosValidandoAnoAtualSemestreAtual(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		HorarioProfessorVO obj = new HorarioProfessorVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.getProfessor().setCodigo(new Integer(tabelaResultado.getInt("professor")));
		obj.setSegunda(tabelaResultado.getString("segunda"));
		obj.setTerca(tabelaResultado.getString("terca"));
		obj.setQuarta(tabelaResultado.getString("quarta"));
		obj.setQuinta(tabelaResultado.getString("quinta"));
		obj.setSexta(tabelaResultado.getString("sexta"));
		obj.setSabado(tabelaResultado.getString("sabado"));
		obj.setDomingo(tabelaResultado.getString("domingo"));
		obj.getTurno().setCodigo(new Integer(tabelaResultado.getInt("turno")));
		obj.setNovoObj(false);
		montarDadosTurno(obj, usuario);
		obj.setHorarioProfessorDiaVOs(HorarioProfessorDia.consultarHorarioProfessorDiasAnoAtualSemestreAtual(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DisponibilidadeHorarioVO</code>.
	 * 
	 * @return O objeto da classe <code>DisponibilidadeHorarioVO</code> com os
	 *         dados devidamente montados.
	 */
	public static HorarioProfessorVO montarDadosValidandoAnoSemestre(SqlRowSet tabelaResultado, String semestre, String ano, UsuarioVO usuario) throws Exception {
		HorarioProfessorVO obj = new HorarioProfessorVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.getProfessor().setCodigo(new Integer(tabelaResultado.getInt("professor")));
		obj.setSegunda(tabelaResultado.getString("segunda"));
		obj.setTerca(tabelaResultado.getString("terca"));
		obj.setQuarta(tabelaResultado.getString("quarta"));
		obj.setQuinta(tabelaResultado.getString("quinta"));
		obj.setSexta(tabelaResultado.getString("sexta"));
		obj.setSabado(tabelaResultado.getString("sabado"));
		obj.setDomingo(tabelaResultado.getString("domingo"));
		obj.getTurno().setCodigo(new Integer(tabelaResultado.getInt("turno")));
		obj.setNovoObj(false);
		montarDadosTurno(obj, usuario);
		obj.setHorarioProfessorDiaVOs(HorarioProfessorDia.consultarHorarioProfessorDiasAnoSemestre(obj.getCodigo(), semestre, ano, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DisponibilidadeHorarioVO</code>.
	 * 
	 * @return O objeto da classe <code>DisponibilidadeHorarioVO</code> com os
	 *         dados devidamente montados.
	 */
	public static HorarioProfessorVO montarDados(SqlRowSet tabelaResultado, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		HorarioProfessorVO obj = new HorarioProfessorVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.getProfessor().setCodigo(new Integer(tabelaResultado.getInt("professor")));
		obj.setSegunda(tabelaResultado.getString("segunda"));
		obj.setTerca(tabelaResultado.getString("terca"));
		obj.setQuarta(tabelaResultado.getString("quarta"));
		obj.setQuinta(tabelaResultado.getString("quinta"));
		obj.setSexta(tabelaResultado.getString("sexta"));
		obj.setSabado(tabelaResultado.getString("sabado"));
		obj.setDomingo(tabelaResultado.getString("domingo"));
		obj.getTurno().setCodigo(new Integer(tabelaResultado.getInt("turno")));
		obj.setNovoObj(false);
		montarDadosTurno(obj, usuario);
		obj.setHorarioProfessorDiaVOs(HorarioProfessorDia.consultarHorarioProfessorDias(obj.getCodigo(), dataInicio, dataFim, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	public static HorarioProfessorVO montarDadosBasico(SqlRowSet tabelaResultado, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		HorarioProfessorVO obj = new HorarioProfessorVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.getProfessor().setCodigo(new Integer(tabelaResultado.getInt("professor")));
		obj.setSegunda(tabelaResultado.getString("segunda"));
		obj.setTerca(tabelaResultado.getString("terca"));
		obj.setQuarta(tabelaResultado.getString("quarta"));
		obj.setQuinta(tabelaResultado.getString("quinta"));
		obj.setSexta(tabelaResultado.getString("sexta"));
		obj.setSabado(tabelaResultado.getString("sabado"));
		obj.setDomingo(tabelaResultado.getString("domingo"));
		obj.getTurno().setCodigo(new Integer(tabelaResultado.getInt("turno")));
		obj.setNovoObj(false);
		montarDadosTurno(obj, usuarioVO);
		if (Uteis.NIVELMONTARDADOS_DADOSBASICOS == nivelMontarDados) {
			return obj;
		}
		obj.setHorarioProfessorDiaVOs(HorarioProfessorDia.consultarHorarioProfessorDias(obj.getCodigo(), dataInicio, dataFim, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static void montarDadosHorarioProfessorDiaItem(HorarioProfessorVO obj, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		for (HorarioProfessorDiaVO dia : obj.getHorarioProfessorDiaVOs()) {
			getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOs(dia, obj, obj.getTurno(), dataInicio, dataFim, usuario);
			for (HorarioProfessorDiaItemVO item : dia.getHorarioProfessorDiaItemVOs()) {
				if (item.getDisciplinaLivre() && !item.getTurmaLivre()) {
					item.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(item.getDisciplinaVO().getCodigo(), nivelMontarDados, usuario));
					item.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(item.getTurmaVO().getCodigo(), nivelMontarDados, usuario));
				}
			}
		}
	}

	public static void montarDadosTurno(HorarioProfessorVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			obj.setTurno(new TurnoVO());
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#excluirHorarioProfessor(java.lang.Integer)
	 */
	public void excluirHorarioProfessor(Integer professor, UsuarioVO usuario) throws Exception {
		HorarioProfessor.excluir(getIdEntidade());
		String sql = "DELETE FROM HorarioProfessor WHERE (professor = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { professor });
	}

	public void excluirHorarioProfessorPorTurno(Integer professor, Integer turno, UsuarioVO usuario) throws Exception {
		HorarioProfessor.excluir(getIdEntidade());
		String sql = "DELETE FROM HorarioProfessor WHERE (professor = ? and turno = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { professor, turno });
	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#incluirHorarioProfessor(java.lang.Integer,
	 *      java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirHorarioProfessor(Integer professorPrm, List objetos, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			HorarioProfessorVO obj = (HorarioProfessorVO) e.next();
			HorarioProfessorVO horarioProfessor = consultarPorProfessorTurno(professorPrm, obj.getTurno().getCodigo(), dataInicio, dataFim, usuario);
			obj.getProfessor().setCodigo(professorPrm);
			if (horarioProfessor.getCodigo().equals(0)) {
				incluir(obj, usuario);
			} else {
				obj.setCodigo(horarioProfessor.getCodigo());
				alterar(obj, usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAtualizarHorariosProfessor(PessoaVO pessoa, Date dataInicio, Date dataFim) throws Exception {
		try {
			Iterator i = pessoa.getQuadroHorarioVOs().iterator();
			while (i.hasNext()) {
				QuadroHorarioVO quadroHorario = (QuadroHorarioVO) i.next();
				quadroHorario.atualizarValoresQuadroHorario();
				// Buscando todos horários professor pelo turno e professor
				HorarioProfessorVO horarioProfessorVO = consultarRapidaHorarioProfessorTurno(pessoa.getCodigo().intValue(), quadroHorario.getTurno().getCodigo().intValue(), null);
				horarioProfessorVO.getTurno().setCodigo(quadroHorario.getTurno().getCodigo());
				horarioProfessorVO.setSegunda(quadroHorario.getDadosHorarioSegunda());
				horarioProfessorVO.setTerca(quadroHorario.getDadosHorarioTerca());
				horarioProfessorVO.setQuarta(quadroHorario.getDadosHorarioQuarta());
				horarioProfessorVO.setQuinta(quadroHorario.getDadosHorarioQuinta());
				horarioProfessorVO.setSexta(quadroHorario.getDadosHorarioSexta());
				horarioProfessorVO.setSabado(quadroHorario.getDadosHorarioSabado());
				horarioProfessorVO.setDomingo(quadroHorario.getDadosHorarioDomingo());
				if (horarioProfessorVO.getCodigo() > 0) {
					alterarSometeHorariosSemana(horarioProfessorVO, null);
				} else {
					horarioProfessorVO.getProfessor().setCodigo(pessoa.getCodigo());
					incluir(horarioProfessorVO, null);
				}
				quadroHorario.setHorarioProfessorVO(horarioProfessorVO);

			}
		} catch (Exception e) {
			throw e;
		}
	}

	/***
	 * Método traz os horários do professor pelo professor e pelo turno
	 * 
	 * @param professor
	 * @param controlarAcesso
	 * @return
	 * @throws Exception
	 */
	public List<HorarioProfessorVO> consultarPorProfessorTurno(Integer professor, Integer codigoTurno, Date dataInicio, Date dataFim) throws Exception {
		List<HorarioProfessorVO> listaHorarioProfessorVO = new ArrayList<HorarioProfessorVO>(0);
		StringBuilder sqlStr = new StringBuilder(" SELECT professor,turno,segunda,terca,quarta,quinta,sexta,sabado,domingo, codigo FROM HorarioProfessor WHERE professor = ").append(professor);
		sqlStr.append(" AND turno = ").append(codigoTurno);
		sqlStr.append(" ORDER BY codigo");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			HorarioProfessorVO horarioProfessor = new HorarioProfessorVO();
			horarioProfessor = HorarioProfessor.montarDados(tabelaResultado, dataInicio, dataFim, null);
			listaHorarioProfessorVO.add(horarioProfessor);
		}

		return listaHorarioProfessorVO;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public HorarioProfessorVO executarCriacaoHorarioProfessorTurno(PessoaVO pessoaVO, TurnoVO turnoVO, UsuarioVO usuario) throws Exception {
		QuadroHorarioVO quadroHorarioVO = new QuadroHorarioVO();
		HorarioProfessorVO horarioProfessorVO = new HorarioProfessorVO();
		horarioProfessorVO.setTurno(turnoVO);
		horarioProfessorVO.setProfessor(pessoaVO);
		quadroHorarioVO.setHorarioProfessorVO(horarioProfessorVO);
		quadroHorarioVO.setTurno(turnoVO);
		montarDadosListaQuadroHorarioVO(quadroHorarioVO, usuario);
		pessoaVO.adicionarObjQuadroHorarioVOs(quadroHorarioVO);
		pessoaVO.montarListaHorarioProfessor();
		return quadroHorarioVO.getHorarioProfessorVO();

	}

	public void montarDadosListaQuadroHorarioVO(QuadroHorarioVO quadroHorarioVO, UsuarioVO usuario) throws Exception {
		Integer nrAula = 1;
		Integer nrAulaTurno = 0;
		for (DiaSemana diaSemana : DiaSemana.values()) {
			if (diaSemana.equals(DiaSemana.NENHUM)) {
				continue;
			}
			nrAula = 1;
			nrAulaTurno = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(quadroHorarioVO.getTurno(), diaSemana);
			while (nrAula <= nrAulaTurno) {
				DisponibilidadeHorarioVO obj = new DisponibilidadeHorarioVO();
				obj.setNrAula(nrAula);
				TurnoHorarioVO turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(quadroHorarioVO.getTurno(), diaSemana, nrAula);
				obj.setIntervaloHora(turnoHorarioVO.getHorarioInicioAula() + " até " + turnoHorarioVO.getHorarioFinalAula());
				obj.setAula(String.valueOf(nrAula.intValue()) + "ª Aula");
				obj.getTurno().setCodigo(quadroHorarioVO.getTurno().getCodigo());
				switch (diaSemana) {
				case DOMINGO: {
					quadroHorarioVO.montarListaHorarioDomingo(obj);
					break;
				}
				case SEGUNGA: {
					quadroHorarioVO.montarListaHorarioSegunda(obj);
					break;
				}
				case TERCA: {
					quadroHorarioVO.montarListaHorarioTerca(obj);
					break;
				}
				case QUARTA: {
					quadroHorarioVO.montarListaHorarioQuarta(obj);
					break;
				}
				case QUINTA: {
					quadroHorarioVO.montarListaHorarioQuinta(obj);
					break;
				}
				case SEXTA: {
					quadroHorarioVO.montarListaHorarioSexta(obj);
					break;
				}
				case SABADO: {
					quadroHorarioVO.montarListaHorarioSabado(obj);
					break;
				}
				default: {
					break;
				}
				}
				nrAula++;
			}
		}
		quadroHorarioVO.ordenarListas();
	}

	/**
	 * Método responsável por trazer os horários de aulas a ser dadas pelo
	 * professor especifico
	 * 
	 * @param professor
	 * @param controlarAcesso
	 * 
	 * @return List - {@link HorarioProfessorDiaVO}
	 * @author Rodrigo - Danilo
	 * 
	 */
	public List consultarHorarioProfessor(Integer professor, boolean controlarAcesso, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		StringBuilder sqlStr = new StringBuilder(" SELECT professor,turno,segunda,terca,quarta,quinta,sexta,sabado,domingo, codigo FROM HorarioProfessor WHERE professor = ? ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { professor });
		while (tabelaResultado.next()) {
			HorarioProfessorVO novoObj = new HorarioProfessorVO();
			novoObj = HorarioProfessor.montarDados(tabelaResultado, dataInicio, dataFim, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/***
	 * Método traz o horário do professor pelo professor e pelo turno
	 * 
	 * @param professor
	 * @param controlarAcesso
	 * @return
	 * @throws Exception
	 */
	public HorarioProfessorVO consultarHorarioProfessor(Integer professor, Integer codigoTurno, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" SELECT professor,turno,segunda,terca,quarta,quinta,sexta,sabado,domingo, codigo FROM HorarioProfessor WHERE professor = ").append(professor);
		sqlStr.append(" AND turno = ").append(codigoTurno);
		sqlStr.append(" ORDER BY codigo desc limit 1 ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		HorarioProfessorVO horarioProfessor = new HorarioProfessorVO();
		while (tabelaResultado.next()) {
			horarioProfessor = HorarioProfessor.montarDados(tabelaResultado, dataInicio, dataFim, usuario);
		}

		return horarioProfessor;
	}

	public boolean consultarHorarioProfessorPorTurno(Integer turno, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" SELECT codigo FROM HorarioProfessor WHERE turno = ").append(turno);
		sqlStr.append(" and exists (select horarioprofessordia.codigo from horarioprofessordia where horarioprofessordia.HorarioProfessor = HorarioProfessor.codigo limit 1 ) ");
		sqlStr.append(" ORDER BY codigo desc limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer)
	 */
	public HorarioProfessorVO consultarPorChavePrimaria(Integer codigoPrm, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM HorarioProfessor WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, dataInicio, dataFim, usuario));
	}

	/**
	 * @see negocio.facade.jdbc.academico.HorarioProfessorInterfaceFacade#setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		HorarioProfessor.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return HorarioProfessor.idEntidade;
	}

	public List<HorarioProfessorVO> consultarPorHorarioProfessorContendoDisciplina(Integer codigoDisciplina, Date dataInicio, Date dataFim, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT HorarioProfessor.* FROM HorarioProfessor WHERE ");
		sqlStr.append("(segunda like('%[").append(codigoDisciplina).append("]%')) OR");
		sqlStr.append("(terca like('%[").append(codigoDisciplina).append("]%')) OR");
		sqlStr.append("(quarta like('%[").append(codigoDisciplina).append("]%')) OR");
		sqlStr.append("(quinta like('%[").append(codigoDisciplina).append("]%')) OR");
		sqlStr.append("(sexta like('%[").append(codigoDisciplina).append("]%')) OR");
		sqlStr.append("(sabado like('%[").append(codigoDisciplina).append("]%')) OR");
		sqlStr.append("(domingo like('%[").append(codigoDisciplina).append("]%'))");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, dataInicio, dataFim, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoDisponibilidadeHorarioProfessor(HorarioProfessorVO horarioProfessorVO, TurnoVO turnoVO, Boolean marcarIndisponibilidade, UsuarioVO usuario) {
		int nrAulas = 0;
		for (DiaSemana diaSemana : DiaSemana.values()) {
			if (diaSemana.equals(DiaSemana.NENHUM)) {
				continue;
			}
			List<DisponibilidadeHorarioTurmaProfessorVO> disponibilidadeHorarioProfessorVOs = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
			nrAulas = (getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioProfessorVO.getTurno(), diaSemana));
			int inicio = 1;
			while (inicio <= nrAulas) {
				DisponibilidadeHorarioTurmaProfessorVO obj = new DisponibilidadeHorarioTurmaProfessorVO();
				TurnoHorarioVO turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(horarioProfessorVO.getTurno(), diaSemana, inicio);
				obj.setHorario(turnoHorarioVO.getDescricaoHorario());
				obj.setHorarioInicio(turnoHorarioVO.getHorarioInicioAula());
				obj.setHorarioTermino(turnoHorarioVO.getHorarioFinalAula());
				obj.setNrAula(inicio);
				if (marcarIndisponibilidade) {
					obj.setHorarioLivre(horarioProfessorVO.getIndisponibilidadeHorario(inicio, diaSemana));
				} else {
					obj.setHorarioLivre(true);
				}
				disponibilidadeHorarioProfessorVOs.add(obj);
				inicio++;
			}
			horarioProfessorVO.adicionarListaHorarioDisponivelTurmaProfessorVO(diaSemana, disponibilidadeHorarioProfessorVOs);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoDisponibilidadeHorarioProfessorDiaSemana(HorarioProfessorVO horarioProfessorVO, TurnoVO turnoVO, DiaSemana diaSemana, Boolean marcarIndisponibilidade, UsuarioVO usuario) {
		int nrAulas = 0;
		List<DisponibilidadeHorarioTurmaProfessorVO> disponibilidadeHorarioProfessorVOs = new ArrayList<DisponibilidadeHorarioTurmaProfessorVO>(0);
		nrAulas = (getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioProfessorVO.getTurno(), diaSemana));
		int inicio = 1;
		while (inicio <= nrAulas) {
			DisponibilidadeHorarioTurmaProfessorVO obj = new DisponibilidadeHorarioTurmaProfessorVO();
			TurnoHorarioVO turnoHorarioVO = getFacadeFactory().getTurnoFacade().consultarObjTurnoHorarioVOs(horarioProfessorVO.getTurno(), diaSemana, inicio);
			obj.setHorario(turnoHorarioVO.getDescricaoHorario());
			obj.setHorarioInicio(turnoHorarioVO.getHorarioInicioAula());
			obj.setHorarioTermino(turnoHorarioVO.getHorarioFinalAula());
			obj.setNrAula(inicio);
			if (marcarIndisponibilidade) {
				obj.setHorarioLivre(horarioProfessorVO.getIndisponibilidadeHorario(inicio, diaSemana));
				if (!obj.getHorarioLivre()) {
					obj.setProgramarAula(false);
				}
			} else {
				obj.setHorarioLivre(true);
			}
			disponibilidadeHorarioProfessorVOs.add(obj);
			inicio++;
		}
		horarioProfessorVO.adicionarListaHorarioDisponivelTurmaProfessorVO(diaSemana, disponibilidadeHorarioProfessorVOs);
	}

	public void montarDadosHorarioProfessorDiaItemVOs(HorarioProfessorVO horarioProfessorVO, Date dataInicio, Date dataFim, UsuarioVO usuario) {
		for (HorarioProfessorDiaVO horarioProfessorDia : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			horarioProfessorDia.setHorarioProfessorDiaItemVOs(new ArrayList<HorarioProfessorDiaItemVO>(0));
			try {
				getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOs(horarioProfessorDia, horarioProfessorVO, horarioProfessorVO.getTurno(), dataInicio, dataFim, usuario);
			} catch (Exception e) {

			}
		}
	}

	public void montarDadosHorarioProfessorDiaItemVOsRegistroAula(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario) throws Exception {
		for (HorarioProfessorDiaVO horarioProfessorDia : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOsRegistroAula(horarioProfessorVO, horarioProfessorDia, usuario);
			Ordenacao.ordenarLista(horarioProfessorDia.getHorarioProfessorDiaItemVOs(), "nrAula");
		}
	}

	/***
	 * Método responsável de montar as aulas (HorarioProfessorDiaItem) de um
	 * determinado dia (HorarioProfessorDia) para determinado professor
	 * 
	 * @param horarioProfessorVO
	 * @param date
	 * 
	 * @return HorarioProfessorDiaVO
	 */
	public HorarioProfessorDiaVO consultarHorarioProfessorPorDiaPorDia(HorarioProfessorVO horarioProfessorVO, Date date, UsuarioVO usuario) {
		for (HorarioProfessorDiaVO horarioProfessorDia : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			if (Uteis.getDateHoraFinalDia(horarioProfessorDia.getData()).compareTo(Uteis.getDateHoraFinalDia(date)) == 0) {
				return horarioProfessorDia;
			}
		}
		HorarioProfessorDiaVO horarioProfessorDia = new HorarioProfessorDiaVO();
		try {
			getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOs(horarioProfessorDia, horarioProfessorVO, horarioProfessorVO.getTurno(), date, date, usuario);
		} catch (Exception e) {
		}
		horarioProfessorDia.setData(date);
		return horarioProfessorDia;
	}

	/***
	 * Método responsável de montar as aulas (HorarioProfessorDiaItem) de um
	 * determinado dia (HorarioProfessorDia) para determinado professor
	 * 
	 * @param horarioProfessorVO
	 * @param date
	 * 
	 * @return HorarioProfessorDiaVO
	 */
	public HorarioProfessorDiaVO consultarHorarioProfessorPorDiaPorDiaComMontagemDiaItems(HorarioProfessorVO horarioProfessorVO, Date date, UsuarioVO usuario) {
		for (HorarioProfessorDiaVO horarioProfessorDia : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
			if (Uteis.getDateHoraFinalDia(horarioProfessorDia.getData()).compareTo(Uteis.getDateHoraFinalDia(date)) == 0) {
				try {
					getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOs(horarioProfessorDia, horarioProfessorVO, horarioProfessorVO.getTurno(), date, date, usuario);
				} catch (Exception e) {
				}
				return horarioProfessorDia;
			}
		}
		HorarioProfessorDiaVO horarioProfessorDia = new HorarioProfessorDiaVO();
		try {
			getFacadeFactory().getHorarioProfessorDiaFacade().montarDadosHorarioProfessorDiaItemVOs(horarioProfessorDia, horarioProfessorVO, horarioProfessorVO.getTurno(), date, date, usuario);
		} catch (Exception e) {
		}
		horarioProfessorDia.setData(date);
		return horarioProfessorDia;
	}

	public void adicionarDadosHorarioTurmaPorDia(HorarioProfessorVO horarioProfessorVO, Date dia, DisciplinaVO disciplina, TurmaVO turma, Integer nrAula, SalaLocalAulaVO sala, boolean alterar, UsuarioVO usuario) throws Exception {
		HorarioProfessorDiaVO item = consultarHorarioProfessorPorDiaPorDia(horarioProfessorVO, dia, usuario);
		item.adicinarTurmaEDisciplina(nrAula, disciplina, turma, sala, alterar);
		horarioProfessorVO.adicinarHorarioProfessorPorDiaPorDia(item);
	}

	public void montarListaProfessorLecionaTurma(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario) {
		horarioProfessorVO.setListaCodigoTurma(new ArrayList<Integer>());
		for (DiaSemana diaSemana : DiaSemana.values()) {
			if (diaSemana.equals(DiaSemana.NENHUM)) {
				continue;
			}
			consultarAdicionarTurmas(horarioProfessorVO, diaSemana);
		}
	}

	public void consultarAdicionarTurmas(HorarioProfessorVO horarioProfessorVO, DiaSemana diaSemana) {
		Integer cont = 1;
		Integer nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioProfessorVO.getTurno(), diaSemana);
		while (cont <= nrAula) {
			Integer codTurma = horarioProfessorVO.getCodigoTurma(cont, diaSemana);
			if (codTurma.intValue() != 0) {
				horarioProfessorVO.adicionarListaCodigoTurma(codTurma);
			}
			cont++;
		}
	}

	public void montarListaProfessorLecionaDisciplina(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario) {
		horarioProfessorVO.setListaCodigoDisciplina(new ArrayList<Integer>());
		for (DiaSemana diaSemana : DiaSemana.values()) {
			if (diaSemana.equals(DiaSemana.NENHUM)) {
				continue;
			}
			consultarAdicionarDisciplinas(horarioProfessorVO, diaSemana);
		}
	}

	public void consultarAdicionarDisciplinas(HorarioProfessorVO horarioProfessorVO, DiaSemana diaSemana) {
		Integer cont = 1;
		Integer nrAula = getFacadeFactory().getTurnoFacade().consultarNumeroAulaTurnoHorarioVOs(horarioProfessorVO.getTurno(), diaSemana);
		while (cont <= nrAula) {
			Integer codDisciplina = horarioProfessorVO.getCodigoDisciplina(cont, diaSemana);
			if (codDisciplina.intValue() != 0) {
				horarioProfessorVO.adicionarListaCodigoDisciplina(codDisciplina);
			}
			cont++;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarSubstitucaoDisciplinaTurmaHorarioProfessorSemanal(HorarioProfessorVO horarioProfessor, Integer disciplina, Integer turma, DiaSemana diaSemana, Integer nrAula, UsuarioVO usuario) throws Exception {
		horarioProfessor.setCodigoDisciplina(nrAula, diaSemana, disciplina);
		horarioProfessor.setCodigoTurma(nrAula, diaSemana, turma);
	}

	public void executarSubstituicaoDisciplinaTurmaHorarioProfessorDiarioComNrAulaDiaEspecifico(HorarioProfessorVO horarioProfessor, TurmaVO turma, DisciplinaVO disciplinaVO, Date data, int nrAula, SalaLocalAulaVO sala, UsuarioVO usuario) throws Exception {
		if (horarioProfessor != null) {
			HorarioProfessorDiaVO horarioProfessorDiaVO = getFacadeFactory().getHorarioProfessorFacade().consultarHorarioProfessorPorDiaPorDia(horarioProfessor, data, usuario);
			horarioProfessorDiaVO.adicinarTurmaEDisciplina(nrAula, disciplinaVO, turma, sala, true);
			horarioProfessor.adicinarHorarioProfessorPorDiaPorDia(horarioProfessorDiaVO);
		}
	}

	public void inicializarDadosCalendario(HorarioProfessorVO horarioProfessorVO, UsuarioVO usuario) throws Exception {
		horarioProfessorVO.setCalendarioHorarioAulaVOs(new ArrayList<CalendarioHorarioAulaVO<HorarioProfessorDiaVO>>());

		if (!horarioProfessorVO.getHorarioProfessorDiaVOs().isEmpty()) {
			Ordenacao.ordenarLista(horarioProfessorVO.getHorarioProfessorDiaVOs(), "data");
			for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorVO.getHorarioProfessorDiaVOs()) {
				adicionarHorarioTurmaDiaEmCalendarioHorarioTurma(horarioProfessorVO, horarioProfessorDiaVO);
			}
			Date menorData = horarioProfessorVO.getHorarioProfessorDiaVOs().get(0).getData();
			Date maiorData = horarioProfessorVO.getHorarioProfessorDiaVOs().get(horarioProfessorVO.getHorarioProfessorDiaVOs().size() - 1).getData();
			List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(Uteis.getDate("01/" + Uteis.getMesData(menorData) + "/" + Uteis.getAnoData(menorData)), Uteis.getDate(Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(maiorData)) + "/" + Uteis.getMesData(maiorData) + "/" + Uteis.getAnoData(maiorData)), usuario.getUnidadeEnsinoLogado().getCidade().getCodigo(), ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

			executarCriacaoDiasMesCalendario(horarioProfessorVO, feriadoVOs, usuario);

		}
	}

	public void adicionarHorarioTurmaDiaEmCalendarioHorarioTurma(HorarioProfessorVO horarioProfessorVO, HorarioProfessorDiaVO horarioProfessorDiaVO) {
		for (CalendarioHorarioAulaVO calendarioHorarioAulaVO : horarioProfessorVO.getCalendarioHorarioAulaVOs()) {
			if (Integer.parseInt(calendarioHorarioAulaVO.getAno()) == (Uteis.getAnoData(horarioProfessorDiaVO.getData())) && calendarioHorarioAulaVO.getMesAno().getMesData(horarioProfessorDiaVO.getData()).equals(calendarioHorarioAulaVO.getMesAno())) {
				adicionarDiaComAulaProgramadaCalendario(calendarioHorarioAulaVO, horarioProfessorDiaVO);
				return;
			}
		}
		CalendarioHorarioAulaVO<HorarioProfessorDiaVO> calendarioHorarioAulaVO = new CalendarioHorarioAulaVO<HorarioProfessorDiaVO>();
		calendarioHorarioAulaVO.setAno("" + (Uteis.getAnoData(horarioProfessorDiaVO.getData())));
		calendarioHorarioAulaVO.setMesAno(calendarioHorarioAulaVO.getMesAno().getMesData(horarioProfessorDiaVO.getData()));
		adicionarDiaComAulaProgramadaCalendario(calendarioHorarioAulaVO, horarioProfessorDiaVO);
		horarioProfessorVO.getCalendarioHorarioAulaVOs().add(calendarioHorarioAulaVO);
	}

	public void adicionarDiaComAulaProgramadaCalendario(CalendarioHorarioAulaVO calendarioHorarioAulaVO, HorarioProfessorDiaVO horarioProfessorDiaVO) {
		if (horarioProfessorDiaVO.getDiaSemana().equals(DiaSemana.DOMINGO.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo().add(horarioProfessorDiaVO);
		} else if (horarioProfessorDiaVO.getDiaSemana().equals(DiaSemana.SEGUNGA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda().add(horarioProfessorDiaVO);
		} else if (horarioProfessorDiaVO.getDiaSemana().equals(DiaSemana.TERCA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca().add(horarioProfessorDiaVO);
		} else if (horarioProfessorDiaVO.getDiaSemana().equals(DiaSemana.QUARTA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta().add(horarioProfessorDiaVO);
		} else if (horarioProfessorDiaVO.getDiaSemana().equals(DiaSemana.QUINTA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta().add(horarioProfessorDiaVO);
		} else if (horarioProfessorDiaVO.getDiaSemana().equals(DiaSemana.SEXTA.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta().add(horarioProfessorDiaVO);
		} else if (horarioProfessorDiaVO.getDiaSemana().equals(DiaSemana.SABADO.getValor())) {
			calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado().add(horarioProfessorDiaVO);
		}
	}

	public void executarCriacaoDiasMesCalendario(HorarioProfessorVO horarioProfessorVO, List<FeriadoVO> feriadoVOs, UsuarioVO usuario) throws Exception {
		if (!horarioProfessorVO.getHorarioProfessorDiaVOs().isEmpty()) {
			String diaString;
			DiaSemana diaSemana;
			for (CalendarioHorarioAulaVO calendarioHorarioAulaVO : horarioProfessorVO.getCalendarioHorarioAulaVOs()) {
				int dia = 1;
				int ultimoDia = Uteis.getDiaMesData(Uteis.getDataUltimoDiaMes(Uteis.getDate("01/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())));
				while (dia <= ultimoDia) {
					if (dia < 10) {
						diaString = "0" + dia;
					} else {
						diaString = "" + dia;
					}
					diaSemana = Uteis.getDiaSemanaEnum(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
					adicionarDiaCalendario(horarioProfessorVO, calendarioHorarioAulaVO, diaString, diaSemana, executarValidacaoDataFeriado(feriadoVOs, Uteis.getDate(diaString + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno())), usuario);
					dia++;
				}
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaDomingo(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSegunda(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaTerca(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuarta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaQuinta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSexta(), "dia");
				Ordenacao.ordenarLista(calendarioHorarioAulaVO.getCalendarioHorarioAulaSabado(), "dia");
			}
		}
	}

	public FeriadoVO executarValidacaoDataFeriado(List<FeriadoVO> feriadoVOs, Date data) {
		if (feriadoVOs != null) {
			for (FeriadoVO feriadoVO : feriadoVOs) {
				if (!feriadoVO.getRecorrente() && Uteis.getDateHoraFinalDia(feriadoVO.getData()).equals(Uteis.getDateHoraFinalDia(data))) {
					return feriadoVO;
				} else if (feriadoVO.getRecorrente() && Uteis.getDiaMesData(feriadoVO.getData()) == Uteis.getDiaMesData(data) && Uteis.getMesData(feriadoVO.getData()) == Uteis.getMesData(data)) {
					return feriadoVO;
				}
			}
		}
		return null;
	}

	public void adicionarDiaCalendario(HorarioProfessorVO horarioProfessorVO, CalendarioHorarioAulaVO<HorarioProfessorDiaVO> calendarioHorarioAulaVO, String dia, DiaSemana diaSemana, FeriadoVO feriadoVO, UsuarioVO usuario) throws Exception {
		if (dia.equals("01") && !diaSemana.equals(DiaSemana.DOMINGO)) {
			for (DiaSemana diaSemana1 : DiaSemana.values()) {
				if (diaSemana1.equals(DiaSemana.NENHUM)) {
					continue;
				}
				if (Integer.valueOf(diaSemana1.getValor()) < Integer.valueOf(diaSemana.getValor())) {
					adicionarDiaCalendario(horarioProfessorVO, calendarioHorarioAulaVO, "", diaSemana1, null, usuario);
				} else {
					break;
				}
			}
		}
		List<HorarioProfessorDiaVO> horarioProfessorDiaVOs = calendarioHorarioAulaVO.consultarListaCalendarioPorDiaSemana(diaSemana);
		for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorDiaVOs) {
			if (horarioProfessorDiaVO.getDia().equals(dia)) {
				return;
			}
		}
		HorarioProfessorDiaVO horarioProfessorDiaVO = new HorarioProfessorDiaVO();
		if (dia.isEmpty()) {
			horarioProfessorDiaVO.setData(null);
		} else {
			horarioProfessorDiaVO.setData(Uteis.getDate(dia + "/" + calendarioHorarioAulaVO.getMesAno().getKey() + "/" + calendarioHorarioAulaVO.getAno()));
		}
		horarioProfessorDiaVOs.add(horarioProfessorDiaVO);
		if (horarioProfessorDiaVO.getData() != null) {
			if (feriadoVO != null) {
				horarioProfessorDiaVO.setFeriado(feriadoVO);
			}
			horarioProfessorVO.adicinarHorarioProfessorPorDiaPorDia(horarioProfessorDiaVO);
		}
		calendarioHorarioAulaVO.adicionarListaCalendarioPorDiaSemana(horarioProfessorDiaVOs, diaSemana);
	}

	/**
	 * método responsável por chamar o método de alteração do horário professor
	 * 
	 * @param pessoa
	 * @param turno
	 * @param semestreVigente
	 * @param anoVigente
	 * 
	 * @author Danilo
	 * 
	 * @since 17.02.2011
	 * 
	 */
	public void alterarHorarioProfessorPorTurno(PessoaVO pessoa, Integer turno, UsuarioVO usuario) throws Exception {
		if (pessoa.getProfessor().equals(Boolean.TRUE)) {
			setIdEntidade(this.getIdEntidade());
			for (HorarioProfessorVO horarioProfessor : (List<HorarioProfessorVO>) pessoa.getHorarioProfessorVOs()) {
				alterarHorarioProfessorPorTurno(horarioProfessor, turno, usuario);
			}
		}
	}

	/***
	 * consulta todos os horarioprofessor
	 *
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 */
	public List<HorarioProfessorVO> consultarTodos(Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<HorarioProfessorVO> listaHorarioProfessor = new ArrayList<HorarioProfessorVO>(0);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT professor,turno,segunda,terca,quarta,quinta,sexta,sabado,domingo,codigo ");
		sqlStr.append(" FROM horarioprofessor ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			HorarioProfessorVO horarioProfessor = new HorarioProfessorVO();
			horarioProfessor = montarDados(tabelaResultado, dataInicio, dataFim, usuario);
			listaHorarioProfessor.add(horarioProfessor);
		}
		return listaHorarioProfessor;

	}
	
	public HorarioProfessorVO consultarPorChavePrimaria(Integer codigoPrm, Date dataInicio, Date dataFim, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		String sqlStr = "SELECT * FROM HorarioProfessor WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDadosBasico(tabelaResultado, dataInicio, dataFim, nivelMontarDados, usuarioVO));
	}

	public HorarioProfessorVO consultarPorProfessorTurnoAnoSemestre(Integer codigoProfessor, Integer codigoTurno, String semestre, String ano, Integer turma, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSqlConsultaRapidaVinculandoHorarioTurmaDetalhado(codigoProfessor, codigoTurno, semestre, ano, turma, usuario);
		sqlStr.append(" WHERE turno.codigo = ").append(codigoTurno.intValue()).append(" ");
		sqlStr.append("order by horarioprofessor.codigo desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDadosConsultaRapida(tabelaResultado, usuario);
		}
		return new HorarioProfessorVO();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void corrigeHorarioProfessorComBaseTurma() {
		StringBuilder sql = new StringBuilder("");
		/**
		 * Cria o horario do professor vinculado ao turno caso não exista
		 */
		sql.append(" insert into horarioprofessor (professor, turno) (");
		sql.append(" select distinct horarioturmadiaitem.professor, turma.turno from horarioturmadiaitem");
		sql.append(" inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sql.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma");
		sql.append(" inner join turma on horarioturma.turma = turma.codigo");
		sql.append(" left join horarioprofessor on horarioprofessor.professor = horarioturmadiaitem.professor");
		sql.append(" and horarioprofessor.turno = turma.turno");
		sql.append(" where horarioprofessor.codigo is null  and horarioturmadiaitem.professor is not null);");
		getConexao().getJdbcTemplate().update(sql.toString());
		/**
		 * Deleta os dias que não tem aula programada para o professor
		 */
		sql = new StringBuilder("");
		sql.append(" delete from horarioprofessordia where codigo not in (");
		sql.append("   select distinct horarioprofessordia.codigo");
		sql.append("   from horarioturmadiaitem");
		sql.append("   inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sql.append("   inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma");
		sql.append("   inner join turma on horarioturma.turma = turma.codigo");
		sql.append("   inner join horarioprofessor on horarioprofessor.professor = horarioturmadiaitem.professor");
		sql.append("   and horarioprofessor.turno = turma.turno");
		sql.append("   inner join horarioprofessordia on horarioprofessor.codigo = horarioprofessordia.horarioprofessor");
		sql.append("   and horarioprofessordia.data = horarioturmadia.data  ");
		sql.append(" );");
		getConexao().getJdbcTemplate().update(sql.toString());
		
		/**
		 * Insere os dias que tem aula programada para o professor e ainda não existe o registro na tabela horario professor dia
		 */
		sql = new StringBuilder("");
		sql.append(" insert into horarioprofessordia (horarioprofessor, data) (");
		sql.append(" 	select distinct horarioprofessor.codigo as horarioprofessor, horarioturmadia.data ");
		sql.append(" 	from horarioturmadiaitem");
		sql.append(" 	inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sql.append(" 	inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma");
		sql.append(" 	inner join turma on horarioturma.turma = turma.codigo");
		sql.append(" 	inner join horarioprofessor on horarioprofessor.professor = horarioturmadiaitem.professor");
		sql.append(" 	and horarioprofessor.turno = turma.turno");
		sql.append(" 	left join horarioprofessordia on horarioprofessor.codigo = horarioprofessordia.horarioprofessor");
		sql.append(" 	and horarioprofessordia.data = horarioturmadia.data");
		sql.append(" 	where horarioprofessordia.codigo is null");
		sql.append("	order by horarioprofessor.codigo, horarioturmadia.data");
		sql.append(" );");		
		getConexao().getJdbcTemplate().update(sql.toString());
		
		/**
		 * Deleta os dias item que não tem aula programada para o professor na turma
		 */
		sql = new StringBuilder("");
		sql.append(" delete from horarioprofessordiaitem where codigo not in (");
		sql.append("   select distinct horarioprofessordiaitem.codigo");
		sql.append("   from horarioturmadiaitem");
		sql.append("   inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sql.append("   inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma");
		sql.append("   inner join turma on horarioturma.turma = turma.codigo");
		sql.append("   inner join horarioprofessor on horarioprofessor.professor = horarioturmadiaitem.professor");
		sql.append("   and horarioprofessor.turno = turma.turno");
		sql.append("   inner join horarioprofessordia on horarioprofessor.codigo = horarioprofessordia.horarioprofessor");
		sql.append("   and horarioprofessordia.data = horarioturmadia.data  ");
		sql.append("   inner join horarioprofessordiaitem on horarioprofessordiaitem.horarioprofessordia = horarioprofessordia.codigo");
		sql.append("   and horarioprofessordiaitem.nraula = horarioturmadiaitem.nraula  ");
		sql.append("   and horarioprofessordiaitem.disciplina = horarioturmadiaitem.disciplina");
		sql.append("   and horarioprofessordiaitem.turma = turma.codigo  ");
		sql.append(" 	and horarioprofessordiaitem.data = horarioturmadiaitem.data ");
		sql.append(" );");		
		getConexao().getJdbcTemplate().update(sql.toString());
		/**
		 * Insere os dias item que tem aula programada para o professor na turma no dia e nr de aula especifico
		 */
		sql = new StringBuilder("");
		sql.append(" insert into horarioprofessordiaitem (horarioprofessordia, nraula, duracaoaula, horario, horarioinicio, horariotermino, turma, disciplina, sala, data) (");
		sql.append(" 	select distinct horarioprofessordia.codigo as horarioprofessordia, horarioturmadiaitem.nraula, horarioturmadiaitem.duracaoaula, ");
		sql.append(" 	horarioturmadiaitem.horario, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, turma.codigo as turma,");
		sql.append(" 	horarioturmadiaitem.disciplina, horarioturmadiaitem.sala, horarioturmadia.data");
		sql.append(" 	from horarioturmadiaitem");
		sql.append(" 	inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia");
		sql.append(" 	inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma");
		sql.append(" 	inner join turma on horarioturma.turma = turma.codigo");
		sql.append(" 	inner join horarioprofessor on horarioprofessor.professor = horarioturmadiaitem.professor");
		sql.append(" 	and horarioprofessor.turno = turma.turno");
		sql.append(" 	inner join horarioprofessordia on horarioprofessor.codigo = horarioprofessordia.horarioprofessor");
		sql.append(" 	and horarioprofessordia.data = horarioturmadia.data");
		sql.append(" 	left join horarioprofessordiaitem on horarioprofessordiaitem.horarioprofessordia = horarioprofessordia.codigo");
		sql.append(" 	and horarioprofessordiaitem.nraula = horarioturmadiaitem.nraula  ");
		sql.append(" 	and horarioprofessordiaitem.disciplina = horarioturmadiaitem.disciplina");
		sql.append(" 	and horarioprofessordiaitem.turma = turma.codigo ");
		sql.append(" 	and horarioprofessordiaitem.data = horarioturmadiaitem.data ");
		sql.append(" 	where horarioprofessordiaitem.codigo is null");
		sql.append(" 	order by horarioprofessordia.codigo, horarioturmadiaitem.nraula");
		sql.append(" );");
		getConexao().getJdbcTemplate().update(sql.toString());		

	}

	@Override
	public HorarioProfessorVO consultarRapidaHorarioProfessorTurno(Integer codigoProfessor, Integer codigoTurno, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSqlConsultaRapida();
		sqlStr.append("WHERE pessoa.codigo = ").append(codigoProfessor.intValue());
		sqlStr.append(" AND turno.codigo = ").append(codigoTurno.intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new HorarioProfessorVO();
		}
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	private StringBuilder getSqlConsultaRapida() {
		StringBuilder sqlStr = new StringBuilder("SELECT horarioprofessor.codigo, horarioprofessor.professor,  horarioprofessor.turno, horarioprofessor.segunda, ");
		sqlStr.append("horarioprofessor.terca, horarioprofessor.quarta, horarioprofessor.quinta, horarioprofessor.sexta, horarioprofessor.sabado, ");
		sqlStr.append("horarioprofessor.domingo, horarioprofessor.codigo, pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\", ");
		sqlStr.append("pessoa.telefoneRes as \"pessoa.telefoneRes\", pessoa.celular as \"pessoa.celular\" ");
		sqlStr.append("FROM horarioprofessor ");
		sqlStr.append("INNER JOIN pessoa ON HorarioProfessor.professor = Pessoa.codigo ");
		sqlStr.append("INNER JOIN turno ON HorarioProfessor.turno = Turno.codigo ");
		return sqlStr;
	}
	
	private StringBuilder getSqlConsultaRapidaVinculandoHorarioTurmaDetalhado(Integer codigoProfessor, Integer codigoTurno, String semestre, String ano, Integer turma, UsuarioVO usuario) {
		StringBuilder sqlStr = new StringBuilder("SELECT horarioprofessor.codigo, horarioprofessor.professor,  horarioprofessor.turno, horarioprofessor.segunda, ");
		sqlStr.append("horarioprofessor.terca, horarioprofessor.quarta, horarioprofessor.quinta, horarioprofessor.sexta, horarioprofessor.sabado, ");
		sqlStr.append("horarioprofessor.domingo, horarioprofessor.codigo, pessoa.nome as \"pessoa.nome\", pessoa.email as \"pessoa.email\", ");
		sqlStr.append("pessoa.telefoneRes as \"pessoa.telefoneRes\", pessoa.celular as \"pessoa.celular\" ");
		sqlStr.append("FROM horarioturmadetalhado( null, ");
		if (!turma.equals(0)) {
			sqlStr.append(turma).append(", ");
		} else {
			sqlStr.append("null, ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" '").append(ano).append("', ");
		} else {
			sqlStr.append("null, ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" '").append(semestre).append("', ");
		} else {
			sqlStr.append("null, ");
		}
		if (!codigoProfessor.equals(0)) {
			sqlStr.append(codigoProfessor).append(", ");
		} else {
			sqlStr.append("null, ");
		}
		sqlStr.append("null, null, null) AS horariodetalhado ");
		sqlStr.append(" INNER JOIN horarioprofessor ON horarioprofessor.professor = horariodetalhado.professor ");
		sqlStr.append(" INNER JOIN pessoa ON HorarioProfessor.professor = Pessoa.codigo ");
		sqlStr.append(" INNER JOIN turno ON HorarioProfessor.turno = Turno.codigo ");
		return sqlStr;
	}

	private HorarioProfessorVO montarDadosConsultaRapida(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		HorarioProfessorVO obj = new HorarioProfessorVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.getProfessor().setCodigo(new Integer(tabelaResultado.getInt("professor")));
		obj.getProfessor().setNome(tabelaResultado.getString("pessoa.nome"));
		obj.getProfessor().setEmail(tabelaResultado.getString("pessoa.email"));
		obj.getProfessor().setTelefoneRes(tabelaResultado.getString("pessoa.telefoneRes"));
		obj.getProfessor().setCelular(tabelaResultado.getString("pessoa.celular"));
		obj.setSegunda(tabelaResultado.getString("segunda"));
		obj.setTerca(tabelaResultado.getString("terca"));
		obj.setQuarta(tabelaResultado.getString("quarta"));
		obj.setQuinta(tabelaResultado.getString("quinta"));
		obj.setSexta(tabelaResultado.getString("sexta"));
		obj.setSabado(tabelaResultado.getString("sabado"));
		obj.setDomingo(tabelaResultado.getString("domingo"));
		obj.getTurno().setCodigo(new Integer(tabelaResultado.getInt("turno")));
		obj.setNovoObj(false);
		FormacaoAcademicaVO formacaoAcademicaVO = getFacadeFactory().getFormacaoAcademicaFacade().consultarFormacaoAcademicaoMaisAtual(obj.getProfessor().getCodigo(), usuario);
		if (formacaoAcademicaVO != null && !formacaoAcademicaVO.isNovoObj()) {
			obj.getProfessor().getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
		}
		montarDadosTurno(obj, usuario);
		return obj;
	}

	@Override
	public HorarioProfessorVO consultaRapidaPorChavePrimaria(Integer codigoHorarioProfessor, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSqlConsultaRapida();
		sqlStr.append("WHERE horarioprofessor.codigo = ").append(codigoHorarioProfessor.intValue());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new HorarioProfessorVO();
		}
		return montarDadosConsultaRapida(tabelaResultado, usuario);
	}

	/**
	 * Este método é responsável em excluir um horario do professor
	 * 
	 * @param horarioTurmaVO
	 * @param professor
	 * @param disciplina
	 * @param dataInicio
	 * @param dataTermino
	 * @param numeroAula
	 * @param alterarTodasAulas
	 * @param registroAulaVOs
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void realizarExclusaoHorarioProfessor(HorarioTurmaVO horarioTurmaVO, int professor, int disciplina, Date dataInicio, Date dataTermino, int numeroAula, boolean alterarTodasAulas, List<RegistroAulaVO> registroAulaVOs, UsuarioVO usuario, Boolean liberarAulaAcimaLimite) throws Exception {

		List<Date> datas = getFacadeFactory().getHorarioTurmaFacade().realizarObtencaoDataSeremAlteradoExcluido(horarioTurmaVO, dataInicio, alterarTodasAulas, professor, disciplina, registroAulaVOs);
		List<HorarioProfessorDiaVO> horarioProfessorAtualDia = getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDia(null, horarioTurmaVO.getTurno().getCodigo(), professor, null, null, dataInicio, dataTermino, null, datas, null, null, null);
		StringBuilder resultadoAcao = new StringBuilder();
		for (HorarioProfessorDiaVO horarioProfessorDiaVO : horarioProfessorAtualDia) {
			boolean existeAulaRegistrada = false;
			boolean alterouAula = false;
			for (RegistroAulaVO registroAulaVO : registroAulaVOs) {
				if (registroAulaVO.getData_Apresentar().equals(horarioProfessorDiaVO.getData_Apresentar()) && ((numeroAula == 0) || (registroAulaVO.getNrAula().equals(numeroAula)))) {
					existeAulaRegistrada = true;
					break;
				}
			}
			if (!existeAulaRegistrada) {
				HorarioTurmaDiaVO horarioTurmaDiaVO = getFacadeFactory().getHorarioTurmaFacade().realizadaObtencaoHorarioTurmaDiaPorData(horarioTurmaVO.getHorarioTurmaDiaVOs(), horarioProfessorDiaVO.getData());
				for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : horarioProfessorDiaVO.getHorarioProfessorDiaItemVOs()) {
					if (horarioProfessorDiaItemVO.getTurmaVO().getCodigo().equals(horarioTurmaVO.getTurma().getCodigo()) 
							&& (horarioProfessorDiaItemVO.getDisciplinaVO().getCodigo().equals(disciplina))
							&& horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs().stream().anyMatch(htdi -> htdi.getCodigo().equals(horarioProfessorDiaItemVO.getHorarioTurmaDiaItemVO().getCodigo()))) {
						HorarioTurmaDiaItemVO htdi = null;
						if (horarioTurmaDiaVO != null) {
							htdi = getFacadeFactory().getHorarioTurmaFacade().realizadaObtencaoHorarioTurmaDiaItemPorNrAula(horarioTurmaDiaVO, horarioProfessorDiaItemVO.getNrAula());
						}
						if (htdi == null || (!htdi.getPossuiChoqueSala() && (!htdi.getPossuiChoqueAulaExcesso() || liberarAulaAcimaLimite))) {
							if (alterarTodasAulas) {
								resultadoAcao.append("Excluiu a aula do horário do professor no dia " + horarioProfessorDiaVO.getData_Apresentar() + " de nrº " + horarioProfessorDiaItemVO.getNrAula() + "(" + horarioProfessorDiaItemVO.getHorario() + ") da disciplina " + horarioProfessorDiaItemVO.getDisciplinaVO().getNome() + " da turma " + horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma() + " \n");
								horarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(0);
								horarioProfessorDiaItemVO.getDisciplinaVO().setNome("");
								horarioProfessorDiaItemVO.getTurmaVO().setCodigo(0);
								horarioProfessorDiaItemVO.getSala().setCodigo(0);
								horarioProfessorDiaItemVO.getSala().setSala("");
								horarioProfessorDiaItemVO.getSala().getLocalAula().setLocal("");
								horarioProfessorDiaItemVO.getTurmaVO().setIdentificadorTurma("");
								alterouAula = true;
							} else if (horarioProfessorDiaItemVO.getNrAula().intValue() == numeroAula || numeroAula == 0) {
								resultadoAcao.append("Excluiu a aula do horário do professor no dia " + horarioProfessorDiaVO.getData_Apresentar() + " de nrº " + horarioProfessorDiaItemVO.getNrAula() + "(" + horarioProfessorDiaItemVO.getHorario() + ")  da disciplina " + horarioProfessorDiaItemVO.getDisciplinaVO().getNome() + " da turma " + horarioProfessorDiaItemVO.getTurmaVO().getIdentificadorTurma() + " \n");
								horarioProfessorDiaItemVO.getDisciplinaVO().setCodigo(0);
								horarioProfessorDiaItemVO.getDisciplinaVO().setNome("");
								horarioProfessorDiaItemVO.getSala().setCodigo(0);
								horarioProfessorDiaItemVO.getSala().setSala("");
								horarioProfessorDiaItemVO.getSala().getLocalAula().setLocal("");
								horarioProfessorDiaItemVO.getTurmaVO().setCodigo(0);
								horarioProfessorDiaItemVO.getTurmaVO().setIdentificadorTurma("");
								alterouAula = true;
								if (numeroAula > 0) {
									break;
								}
							}
						}
					}
				}
				if (alterouAula) {
					if (horarioProfessorDiaVO.getIsAulaProgramada()) {
						getFacadeFactory().getHorarioProfessorDiaFacade().alterar(horarioProfessorDiaVO, usuario);
					} else {
						getFacadeFactory().getHorarioProfessorDiaFacade().excluir(horarioProfessorDiaVO, usuario);
					}
					if (!alterarTodasAulas) {
						break;
					}
				}

			}
		}
		if (!resultadoAcao.toString().isEmpty()) {
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(professor, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			getFacadeFactory().getHorarioTurmaLogFacade().realizarCriacaoLogHorarioTurma(horarioTurmaVO, usuario, "Exclusão Disciplina no Horario Professor " + pessoaVO.getNome(), resultadoAcao.toString());
		}
	}
	
	@Override
	public CalendarioHorarioAulaVO<DataEventosRSVO> realizarGeracaoCalendarioProfessor(Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Boolean visaoProfessor, String ordenacao, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO, MesAnoEnum mesAno, Integer ano, UsuarioVO usuarioVO) throws Exception {
		CalendarioHorarioAulaVO<DataEventosRSVO> calendarioHorarioAula = new CalendarioHorarioAulaVO<DataEventosRSVO>();
		calendarioHorarioAula.setAno(ano.toString());
		calendarioHorarioAula.setMesAno(mesAno);
		Date data = Uteis.getData("01/" + mesAno.getKey() + "/" + ano, "dd/MM/yyyy");
		Date dataFim = Uteis.getDataUltimoDiaMes(data);
		calendarioHorarioAula.executarMontagemSemanaInicialCalendarioHorarioAulaDataEventoRSVO(calendarioHorarioAula, data);
		List<DataEventosRSVO> listaDataEventosRSVOs = getFacadeFactory().getHorarioProfessorDiaFacade().consultarRapidaHorarioProfessorDia(codigoProfessor, UnidadeEnsino, curso, turma, disciplina, data, dataFim, visaoProfessor, ordenacao, permiteInformarFuncionarioCargo, funcionarioCargoVO);
		listaDataEventosRSVOs.addAll(getFacadeFactory().getGoogleMeetInterfaceFacade().consultarCalendarioPorProfessor(codigoProfessor));
		listaDataEventosRSVOs.addAll(getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodoPorDataEventos(data, dataFim, 0, ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		DataEventosRSVO dataEventoRSVO = null;
		Integer ultimoDia = Uteis.getDiaMesData(dataFim);
		for (int dia = 1; dia <= ultimoDia; dia++) {
			dataEventoRSVO = consultaDataEventoRSVOExistente(listaDataEventosRSVOs, dia, calendarioHorarioAula.getMesAno());
			if (dataEventoRSVO.getData() != null) {
				dataEventoRSVO.setData(dataEventoRSVO.getData());
				if(dataEventoRSVO.getStyleClass().equals("horarioFeriado")) {
                	Calendar calendar = Calendar.getInstance();
                    calendar.setTime(dataEventoRSVO.getData());
                    if(listaDataEventosRSVOs.stream().anyMatch(p-> p.getDia().equals(calendar.get(Calendar.DAY_OF_MONTH)) && p.getMes().equals(calendar.get(Calendar.MONTH)) && p.getAno().equals(calendar.get(Calendar.YEAR)) && p.getStyleClass().equals("horarioRegistroLancado"))) {
                    	dataEventoRSVO.setStyleClass("horarioRegistroLancado");	
                    }
                }
			} else {
				dataEventoRSVO = calendarioHorarioAula.executarMontagemDataEventoRSVOLivre(dia, mesAno, ano);
			}
			if (UteisData.getCompararDatas(dataEventoRSVO.getData(), new Date())) {
				calendarioHorarioAula.setObjetoSelecionado(dataEventoRSVO);
			}
			calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(dataEventoRSVO, Uteis.getDiaSemanaEnum(dataEventoRSVO.getData()));
		}
		calendarioHorarioAula.executarMontagemSemanaFinalCalendarioHorarioAulaDataEventoRSVO(calendarioHorarioAula, dataFim);
		return calendarioHorarioAula;
	}

	public DataEventosRSVO consultaDataEventoRSVOExistente(List<DataEventosRSVO> listaDataEventosRSVOs, int dia, MesAnoEnum mesAno) {
    	for (DataEventosRSVO obj : listaDataEventosRSVOs) {
    		if(MesAnoEnum.getMesData(obj.getData()).equals(mesAno) && obj.getDia().equals(dia)) {
    			return obj;
    		}
		}
    	return new DataEventosRSVO();
    }
	
	@Override
	public List<TurmaDisciplinaVO> consultarChoqueHorarioCalendarioProfessor(Integer professor, Date dataBase, String horarioInicial, String horarioFinal){
		StringBuilder sql = new StringBuilder("");
		sql.append("select distinct turma.identificadorturma  as turma, disciplina.nome as disciplina from horarioprofessor ");
		sql.append("	inner join horarioprofessordia on horarioprofessordia.horarioprofessor = horarioprofessor.codigo ");
		sql.append("	inner join horarioprofessordiaitem on horarioprofessordia.codigo = horarioprofessordiaitem.horarioprofessordia");
		sql.append("	inner join horarioturmadiaitem on horarioturmadiaitem.codigo = horarioprofessordiaitem.horarioturmadiaitem");
		sql.append("	inner join turma on turma.codigo = horarioprofessordiaitem.turma");
		sql.append("	inner join disciplina on disciplina.codigo = horarioprofessordiaitem.disciplina");
		sql.append("	where horarioprofessor.professor = ").append(professor);
		sql.append("	and horarioprofessordiaitem.\"data\" = '").append(Uteis.getDataJDBC(dataBase)).append("' ");
		sql.append("	and ((horarioturmadiaitem.horarioinicio <= '").append(horarioInicial).append("' and horarioturmadiaitem.horarioinicio   >= '").append(horarioFinal).append("') ");
		sql.append("	or (horarioturmadiaitem.horariotermino   > '").append(horarioInicial).append("'	and horarioturmadiaitem.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	or (horarioturmadiaitem.horarioinicio   <= '").append(horarioInicial).append("'	and horarioturmadiaitem.horariotermino >= '").append(horarioFinal).append("') ");
		sql.append("   	or (horarioturmadiaitem.horarioinicio   >= '").append(horarioInicial).append("'	and horarioturmadiaitem.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	) ");
		sql.append("	union all 	");
		sql.append("	select distinct turma.identificadorturma  as turma, disciplina.nome as disciplina from googlemeet ");
		sql.append("	inner join turma on turma.codigo = googlemeet.turma");
		sql.append("	inner join disciplina on disciplina.codigo = googlemeet.disciplina");
		sql.append("	where googlemeet.professor = ").append(professor);
		sql.append("    and googlemeet.googlemeetavulso  ");
		sql.append("	and googlemeet.diaevento = '").append(Uteis.getDataJDBC(dataBase)).append("' ");
		sql.append("	and ((googlemeet.horarioinicio <= '").append(horarioInicial).append("' and googlemeet.horarioinicio   >= '").append(horarioFinal).append("') ");
		sql.append("	or (googlemeet.horariotermino   > '").append(horarioInicial).append("'	and googlemeet.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	or (googlemeet.horarioinicio   <= '").append(horarioInicial).append("'	and googlemeet.horariotermino >= '").append(horarioFinal).append("') ");
		sql.append("   	or (googlemeet.horarioinicio   >= '").append(horarioInicial).append("'	and googlemeet.horariotermino  < '").append(horarioFinal).append("') ");
		sql.append("	)");
		SqlRowSet rs  = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TurmaDisciplinaVO> listaTurmaDisciplina = new ArrayList<>(0);
		while (rs.next()) {
			TurmaDisciplinaVO td = new TurmaDisciplinaVO();
			td.getDisciplina().setNome(rs.getString("disciplina"));
			td.getTurmaDescricaoVO().setIdentificadorTurma(rs.getString("turma"));
			listaTurmaDisciplina.add(td);
		}
		return listaTurmaDisciplina;
	}
	
	@Override
    public void realizarGeracaoItemCalendarioProfessor(DataEventosRSVO dataEventoRSVO, Integer codigoProfessor, Integer UnidadeEnsino, Integer curso, Integer turma, Integer disciplina, Boolean visaoProfessor, boolean permiteInformarFuncionarioCargo, FuncionarioCargoVO funcionarioCargoVO, UsuarioVO usuarioVO) throws Exception {
    	dataEventoRSVO.getAgendaAlunoRSVOs().clear();
    	List<HorarioProfessorDiaVO> horarioProfessorDiaVOs = getFacadeFactory().getHorarioProfessorDiaFacade().consultarProfessorPorPeriodoTurmaAgrupada(codigoProfessor, UnidadeEnsino, curso, turma, disciplina, dataEventoRSVO.getData(), dataEventoRSVO.getData(), visaoProfessor, "", false, new FuncionarioCargoVO() );
    	for (HorarioProfessorDiaVO hpd : horarioProfessorDiaVOs) {
    		for (HorarioProfessorDiaItemVO hpdi : hpd.getHorarioProfessorDiaItemVOs()) {
    			AgendaAlunoRSVO agenda = new AgendaAlunoRSVO();
        		agenda.setOrigemAgendaAluno(OrigemAgendaAlunoEnum.HORARIO_PROFESSOR);
        		agenda.setHorarioProfessorDiaItemVO(hpdi);
        		agenda.setGoogleMeetVO(hpdi.getHorarioTurmaDiaItemVO().getGoogleMeetVO());
        		agenda.setSalaAulaBlackboardVO(hpdi.getHorarioTurmaDiaItemVO().getSalaAulaBlackboardVO());
        		dataEventoRSVO.getAgendaAlunoRSVOs().add(agenda);
			}
		}
    	List<GoogleMeetVO> listaGoogleMeet = getFacadeFactory().getGoogleMeetInterfaceFacade().consultarPorProfessorTurmaDiscipinaData(codigoProfessor, turma, disciplina, dataEventoRSVO.getData());
    	for (GoogleMeetVO googleMeet : listaGoogleMeet) {
    		AgendaAlunoRSVO agenda = new AgendaAlunoRSVO();
    		agenda.setOrigemAgendaAluno(OrigemAgendaAlunoEnum.CALENDARIO_EAD);
    		agenda.setGoogleMeetVO(googleMeet);    	
    		agenda.getGoogleMeetVO().setGoogleMeetAvulso(true);
    		if(Uteis.isAtributoPreenchido(agenda.getGoogleMeetVO().getClassroomGoogleVO())) {
    			//agenda.setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSeExisteSalaAulaBlackboard(googleMeet.getClassroomGoogleVO().getTurmaVO().getCodigo(), googleMeet.getClassroomGoogleVO().getDisciplinaVO().getCodigo(), googleMeet.getClassroomGoogleVO().getAno(), googleMeet.getClassroomGoogleVO().getSemestre(), codigoProfessor));
    		}
    		dataEventoRSVO.getAgendaAlunoRSVOs().add(agenda);
    	}
    	
    	List<FeriadoVO> feriadoVOs = getFacadeFactory().getFeriadoFacade().consultaDiasFeriadoNoPeriodo(dataEventoRSVO.getData(), dataEventoRSVO.getData(), 0, ConsiderarFeriadoEnum.ACADEMICO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		for (FeriadoVO feriadoVO : feriadoVOs) {
			AgendaAlunoRSVO agendaAlunoRSVO = new AgendaAlunoRSVO();
			agendaAlunoRSVO.setOrigemAgendaAluno(OrigemAgendaAlunoEnum.FERIADO);
			agendaAlunoRSVO.setFeriadoVO(feriadoVO);
			dataEventoRSVO.getAgendaAlunoRSVOs().add(agendaAlunoRSVO);
		}
    	Ordenacao.ordenarLista(dataEventoRSVO.getAgendaAlunoRSVOs(), "dataOrdenacao");		
    	
    }

}
