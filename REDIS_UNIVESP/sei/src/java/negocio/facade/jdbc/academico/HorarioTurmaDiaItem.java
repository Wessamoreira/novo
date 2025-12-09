/**
 * 
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import negocio.comuns.academico.ChoqueHorarioVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.HorarioTurmaDiaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.gsuite.GoogleMeetVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade;


/**
 * @author Rodrigo Wind
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class HorarioTurmaDiaItem extends ControleAcesso implements HorarioTurmaDiaItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#persistir(negocio.comuns.academico.HorarioTurmaDiaItemVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO, UsuarioVO usuarioVO) throws Exception {		
		alterar(horarioTurmaDiaItemVO, usuarioVO);
	}
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HorarioTurmaDiaItemVO horarioTurmaDiaItemVO, final UsuarioVO usuarioVO) throws Exception {
		horarioTurmaDiaItemVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql  = new StringBuilder("INSERT INTO horarioturmadiaitem ");
				sql.append(" (horarioturmadia, data, nrAula, duracaoAula, horario, horarioInicio, horarioTermino, professor, disciplina, sala, dataUltimaAlteracao, funcionarioCargo, aulaReposicao) values ");
				sql.append(" (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getCodigo());
				ps.setDate(x++, Uteis.getDataJDBC(horarioTurmaDiaItemVO.getData()));
				ps.setInt(x++, horarioTurmaDiaItemVO.getNrAula());
				ps.setInt(x++, horarioTurmaDiaItemVO.getDuracaoAula());
				ps.setString(x++, horarioTurmaDiaItemVO.getHorario());
				ps.setString(x++, horarioTurmaDiaItemVO.getHorarioInicio());
				ps.setString(x++, horarioTurmaDiaItemVO.getHorarioTermino());
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioVO())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getDisciplinaVO())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getSala())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getSala().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
				
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioCargoVO())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getFuncionarioCargoVO().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				ps.setBoolean(x++, horarioTurmaDiaItemVO.getAulaReposicao());
				return ps;
			}
		}, new ResultSetExtractor<Integer>() {
			@Override
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if(arg0.next()){
					return arg0.getInt("codigo");
				}
				return null;
			}
			
		}));
		horarioTurmaDiaItemVO.setNovoObj(false);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HorarioTurmaDiaItemVO horarioTurmaDiaItemVO, final UsuarioVO usuarioVO) throws Exception {
		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {			
			@Override
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				StringBuilder sql  = new StringBuilder("UPDATE horarioturmadiaitem set ");
				sql.append(" horarioturmadia = ?, data =?, nrAula =?, duracaoAula=?, horario=?, horarioInicio=?, horarioTermino=?, ");
				sql.append(" professor=?, disciplina=?, sala=?, dataUltimaAlteracao=?, funcionarioCargo = ?, aulaReposicao = ? ");
				
				sql.append(" where horarioturmadia = ? and nrAula = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
				PreparedStatement ps = arg0.prepareStatement(sql.toString());
				int x = 1;
				ps.setInt(x++, horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getCodigo());
				ps.setDate(x++, Uteis.getDataJDBC(horarioTurmaDiaItemVO.getData()));
				ps.setInt(x++, horarioTurmaDiaItemVO.getNrAula());
				ps.setInt(x++, horarioTurmaDiaItemVO.getDuracaoAula());
				ps.setString(x++, horarioTurmaDiaItemVO.getHorario());
				ps.setString(x++, horarioTurmaDiaItemVO.getHorarioInicio());
				ps.setString(x++, horarioTurmaDiaItemVO.getHorarioTermino());
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioVO())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getDisciplinaVO())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getSala())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getSala().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				
				ps.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioCargoVO())){
					ps.setInt(x++, horarioTurmaDiaItemVO.getFuncionarioCargoVO().getCodigo());
				}else{
					ps.setNull(x++,0);
				}
				ps.setBoolean(x++, horarioTurmaDiaItemVO.getAulaReposicao());

				ps.setInt(x++, horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getCodigo());
				ps.setInt(x++, horarioTurmaDiaItemVO.getNrAula());
				
				return ps;
			}
		}) == 0){
			horarioTurmaDiaItemVO.setNovoObj(true);
			horarioTurmaDiaItemVO.setFuncionarioCargoVO(horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getFuncionarioCargoVO());
			horarioTurmaDiaItemVO.setCodigo(0);
			incluir(horarioTurmaDiaItemVO, usuarioVO);	
			return;
		};
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#persistirHorarioTurmaDiaItem(negocio.comuns.academico.HorarioTurmaDiaVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirHorarioTurmaDiaItem(HorarioTurmaDiaVO horarioTurmaDiaVO, UsuarioVO usuarioVO) throws Exception {
		for (HorarioTurmaDiaItemVO horarioTurmaDiaItemVO : horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()) {
			if (Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO) && !Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getDisciplinaVO()) && !Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioVO())) {
				excluir(horarioTurmaDiaItemVO.getCodigo(), usuarioVO);
			} else if (Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getDisciplinaVO()) && Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioVO())) {
				horarioTurmaDiaItemVO.setData(horarioTurmaDiaVO.getData());
				horarioTurmaDiaItemVO.setHorarioTurmaDiaVO(horarioTurmaDiaVO);
				if (!Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO)) {
					horarioTurmaDiaItemVO.setAulaReposicao(horarioTurmaDiaVO.getAulaReposicao());
					horarioTurmaDiaItemVO.setFuncionarioCargoVO(horarioTurmaDiaVO.getFuncionarioCargoVO());
				}
				if (!Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getFuncionarioCargoVO())) {
					horarioTurmaDiaItemVO.setFuncionarioCargoVO(horarioTurmaDiaVO.getFuncionarioCargoVO());
				}
				persistir(horarioTurmaDiaItemVO, usuarioVO);
			}
		}
	}

	public StringBuilder getSqlConsulta(){
		StringBuilder sql = new StringBuilder("select horarioturmadiaitem.codigo, horarioturmadiaitem.horarioturmadia, horarioturmadiaitem.data, horarioturmadiaitem.nrAula, horarioturmadiaitem.funcionarioCargo, ");
		sql.append(" horarioturmadiaitem.professor, horarioturmadiaitem.disciplina, horarioturmadiaitem.sala, horarioturmadiaitem.duracaoaula, ");
		sql.append(" horarioturmadiaitem.horario, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horarioTermino, horarioturmadiaitem.googlemeet, horarioturmadiaitem.aulareposicao, ");
		sql.append(" disciplina.nome as \"disciplina.nome\", sala.sala as \"sala.sala\", localaula.local as \"localaula.local\", localaula.codigo as \"localaula.codigo\", "); 
		sql.append(" professor.nome as \"professor.nome\", professor.email as \"professor.email\", professor.celular as \"professor.celular\", "); 
		sql.append(" professor.telefoneres as \"professor.telefoneres\", professor.telefonecomer as \"professor.telefonecomer\", professor.telefonerecado as \"professor.telefonerecado\", ");
		sql.append(" turma.codigo as \"turma.codigo\", turma.identificadorTurma as \"turma.identificadorTurma\", turno.codigo as \"turno.codigo\", turno.nome as \"turno.nome\", registroaula.codigo is not null possuiAulaRegistrada, ");
		sql.append(" googlemeet.codigo as \"googlemeet.codigo\", googlemeet.horarioInicio as \"googlemeet.horarioInicio\", googlemeet.horarioTermino as \"googlemeet.horarioTermino\" ,  ");
		sql.append(" googlemeet.idEventoCalendar as \"googlemeet.idEventoCalendar\",  googlemeet.linkGoogleMeet as \"googlemeet.linkGoogleMeet\",  ");
		sql.append(" googlemeet.diaEvento as \"googlemeet.diaEvento\",  googlemeet.ano as \"googlemeet.ano\",  ");
		sql.append(" googlemeet.semestre as \"googlemeet.semestre\",  ");
		sql.append(" googlemeet.processado as \"googlemeet.processado\", ");
		
		sql.append(" classroomGoogle.codigo as \"classroomGoogle.codigo\", classroomGoogle.ano as \"classroomGoogle.ano\", ");
		sql.append(" classroomGoogle.semestre as \"classroomGoogle.semestre\",  classroomGoogle.linkClassroom as \"classroomGoogle.linkClassroom\", ");
		sql.append(" classroomGoogle.idClassRoomGoogle as \"classroomGoogle.idClassRoomGoogle\",  classroomGoogle.idCalendario as \"classroomGoogle.idCalendario\", ");
		sql.append(" classroomGoogle.idTurma as \"classroomGoogle.idTurma\",  ");
		sql.append(" classroomGoogle.emailTurma as \"classroomGoogle.emailTurma\" ");
		
		sql.append(" from horarioturmadiaitem ");
		sql.append(" inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
		sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sql.append(" inner join turno on turno.codigo = turma.turno ");
		sql.append(" left join googleMeet on googleMeet.codigo = horarioturmadiaitem.googleMeet ");
		sql.append(" left join classroomGoogle on classroomGoogle.codigo = googlemeet.classroomGoogle  ");
		sql.append(" left join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sql.append(" left join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ");
		sql.append(" left join salalocalaula as sala on sala.codigo = horarioturmadiaitem.sala ");
		sql.append(" left join localaula on localaula.codigo = sala.localaula ");
		sql.append(" left join registroaula on turma.codigo = registroaula.turma and horarioturmadiaitem.disciplina = registroaula.disciplina  and registroaula.data::DATE = horarioturmadia.data::DATE and registroaula.nrAula = horarioturmadiaitem.nrAula ");
		return sql;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#consultarPorHorarioTurmaDia(java.lang.Integer)
	 */
	@Override
	public List<HorarioTurmaDiaItemVO> consultarPorHorarioTurmaDia(Integer horarioTurmaDia) throws Exception {
		StringBuilder sql = getSqlConsulta();
		sql.append(" where horarioturmadia.codigo = ").append(horarioTurmaDia);
		sql.append(" order by nrAula");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}
	
	public List<HorarioTurmaDiaItemVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs = new ArrayList<HorarioTurmaDiaItemVO>(0);
		while(rs.next()){			
			horarioTurmaDiaItemVOs.add(montarDados(rs));
		}
		return horarioTurmaDiaItemVOs;
	}
	
	public HorarioTurmaDiaItemVO montarDados(SqlRowSet rs) throws Exception{
		HorarioTurmaDiaItemVO horarioTurmaDiaItemVO = new HorarioTurmaDiaItemVO();
		horarioTurmaDiaItemVO.setNovoObj(false);
		horarioTurmaDiaItemVO.setCodigo(rs.getInt("codigo"));
		horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().setCodigo(rs.getInt("horarioTurmaDia"));
		horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().setCodigo(rs.getInt("turma.codigo"));
		horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().getTurno().setCodigo(rs.getInt("turno.codigo"));
		horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().getTurno().setNome(rs.getString("turno.nome"));
		horarioTurmaDiaItemVO.setHorario(rs.getString("horario"));
		horarioTurmaDiaItemVO.setHorarioInicio(rs.getString("horarioInicio"));
		horarioTurmaDiaItemVO.setHorarioTermino(rs.getString("horarioTermino"));
		horarioTurmaDiaItemVO.setNrAula(rs.getInt("nrAula"));
		horarioTurmaDiaItemVO.setDuracaoAula(rs.getInt("duracaoAula"));
		horarioTurmaDiaItemVO.setData(rs.getDate("data"));
		horarioTurmaDiaItemVO.getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
		horarioTurmaDiaItemVO.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
		horarioTurmaDiaItemVO.getDisciplinaVO().setNovoObj(horarioTurmaDiaItemVO.getDisciplinaVO().getCodigo().equals(0));
		horarioTurmaDiaItemVO.getSala().setCodigo(rs.getInt("sala"));
		horarioTurmaDiaItemVO.getSala().setNovoObj(horarioTurmaDiaItemVO.getSala().getCodigo().equals(0));
		horarioTurmaDiaItemVO.getSala().setSala(rs.getString("sala.sala"));
		horarioTurmaDiaItemVO.getSala().getLocalAula().setLocal(rs.getString("localAula.local"));
		horarioTurmaDiaItemVO.getSala().getLocalAula().setCodigo(rs.getInt("localAula.codigo"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setCodigo(rs.getInt("professor"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setNovoObj(horarioTurmaDiaItemVO.getFuncionarioVO().getCodigo().equals(0));
		horarioTurmaDiaItemVO.getFuncionarioVO().setNome(rs.getString("professor.nome"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setEmail(rs.getString("professor.email"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setTelefoneComer(rs.getString("professor.telefoneComer"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setTelefoneRecado(rs.getString("professor.telefoneRecado"));
		horarioTurmaDiaItemVO.getFuncionarioVO().setTelefoneRes(rs.getString("professor.telefoneRes"));	
		horarioTurmaDiaItemVO.setPossuiAulaRegistrada(rs.getBoolean("possuiAulaRegistrada"));
		horarioTurmaDiaItemVO.setAulaReposicao(rs.getBoolean("aulareposicao"));
		
		if (Uteis.isAtributoPreenchido(rs.getInt("funcionarioCargo"))) {
			horarioTurmaDiaItemVO.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorCodigo(rs.getInt("funcionarioCargo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		}
		if (Uteis.isAtributoPreenchido(rs.getInt("googlemeet.codigo"))) {
			horarioTurmaDiaItemVO.getGoogleMeetVO().setCodigo((rs.getInt("googlemeet.codigo")));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setAno(rs.getString("googlemeet.ano"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setSemestre(rs.getString("googlemeet.semestre"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setLinkGoogleMeet(rs.getString("googlemeet.linkGoogleMeet"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setHorarioInicio(rs.getString("googlemeet.horarioInicio"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setHorarioTermino(rs.getString("googlemeet.horariotermino"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setDiaEvento(rs.getDate("googlemeet.diaevento"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setProcessado(rs.getBoolean("googlemeet.processado"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setIdEventoCalendar(rs.getString("googlemeet.idEventoCalendar"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().getProfessorVO().setCodigo(rs.getInt("professor"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().getProfessorVO().setNome(rs.getString("professor.nome"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().getDisciplinaVO().setCodigo(rs.getInt("disciplina"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
			horarioTurmaDiaItemVO.getGoogleMeetVO().setTurmaVO(horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getHorarioTurma().getTurma());
			if(Uteis.isAtributoPreenchido(rs.getInt("classroomGoogle.codigo"))) {
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setCodigo((rs.getInt("classroomGoogle.codigo")));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setAno(rs.getString("classroomGoogle.ano"));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setSemestre(rs.getString("classroomGoogle.semestre"));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setLinkClassroom(rs.getString("classroomGoogle.linkClassroom"));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setIdClassRoomGoogle(rs.getString("classroomGoogle.idClassRoomGoogle"));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setIdTurma(rs.getString("classroomGoogle.idTurma"));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setEmailTurma(rs.getString("classroomGoogle.emailTurma"));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setIdCalendario(rs.getString("classroomGoogle.idCalendario"));
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setTurmaVO(horarioTurmaDiaItemVO.getGoogleMeetVO().getTurmaVO());
				horarioTurmaDiaItemVO.getGoogleMeetVO().getClassroomGoogleVO().setDisciplinaVO(horarioTurmaDiaItemVO.getGoogleMeetVO().getDisciplinaVO());
			}
		}

		return horarioTurmaDiaItemVO;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#consultarPorTurmaDisciplinaProfessorSalaNumeroAula(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<HorarioTurmaDiaItemVO> consultarParametrizada(Integer turma, Integer disciplina, Integer professor, Integer sala, Integer numeroAula, String ano, String semestre, Date dataInicio, Date dataTermino) throws Exception {
		StringBuilder sql = getSqlConsulta();
		sql.append(" where disciplina.codigo is not null and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadiaitem.data", false));
		
		if(Uteis.isAtributoPreenchido(turma)){
			sql.append(" and turma.codigo = ").append(turma);
		}
		if(Uteis.isAtributoPreenchido(disciplina)){
			sql.append(" and disciplina.codigo = ").append(disciplina);
		}
		if(Uteis.isAtributoPreenchido(professor)){
			sql.append(" and professor.codigo = ").append(professor);
		}
		if(Uteis.isAtributoPreenchido(sala)){
			sql.append(" and sala.codigo = ").append(sala);
		}
		if(Uteis.isAtributoPreenchido(numeroAula)){
			sql.append(" and horarioturmadiaitem.numeroAula = ").append(numeroAula);
		}
		if(Uteis.isAtributoPreenchido(ano)){
			sql.append(" and horarioturma.anovigente = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)){
			sql.append(" and horarioturma.semestrevigente = '").append(semestre).append("' ");
		}
		sql.append(" order by turma.codigo, anovigente, semestrevigente, data, nraula ");		
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#realizarVerificacaoChoqueHorarioSalaAula(java.util.List, boolean)
	 */
	@Override
	public List<ChoqueHorarioVO> realizarVerificacaoChoqueHorarioSalaAula(Integer horarioTurma, List<HorarioTurmaDiaVO> horarioTurmaDiaVOs, boolean retornarExcecao) throws Exception {
				
		StringBuilder sql = getSqlConsulta();
		sql.append(" where horarioTurma.codigo != ").append(horarioTurma).append(" and ( ");				
		boolean or = false;
		for(HorarioTurmaDiaVO horarioTurmaDiaVO: horarioTurmaDiaVOs){
			for(HorarioTurmaDiaItemVO horarioTurmaDiaItemVO: horarioTurmaDiaVO.getHorarioTurmaDiaItemVOs()){
				if(Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO.getSala())){
					sql.append(or?" (":" or (");
					sql.append(" horarioturmadiaitem.data = ").append(Uteis.getDataJDBC(horarioTurmaDiaItemVO.getData()));					
					sql.append(" and '").append(horarioTurmaDiaItemVO.getHorarioInicio()).append("' <= horarioturmadiaitem.horainicio and '").append(horarioTurmaDiaItemVO.getHorarioTermino()).append(" >= horarioturmadiaitem.horarioTermino ");
					sql.append(") "); 
					or = true;
				}
			}
		}
		sql.append(") ");				
		if(retornarExcecao){
			sql.append(" limit 1");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if(rs.next()){
				throw new ChoqueHorarioVO(rs.getDate("data"), rs.getString("turma.identificadorTurma"), rs.getString("disciplina.nome"), rs.getString("professor.nome"), rs.getString("localaula.local") +" - "+rs.getString("sala.sala"));
			}	
			return null;
		}		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ChoqueHorarioVO> choqueHorarioVOs = new ArrayList<ChoqueHorarioVO>(0);
		while(rs.next()){
			choqueHorarioVOs.add(new ChoqueHorarioVO(rs.getDate("data"), rs.getString("turma.identificadorTurma"), rs.getString("disciplina.nome"), rs.getString("professor.nome"), rs.getString("localaula.local") +" - "+rs.getString("sala.sala")));
		}
		return choqueHorarioVOs;		
	}
	
	/* (non-Javadoc)
	 * @see negocio.interfaces.academico.HorarioTurmaDiaItemInterfaceFacade#realizarVerificacaoChoqueHorarioSalaAula(java.util.List, boolean)
	 */
	@Override
	public Boolean realizarVerificacaoChoqueHorarioSalaAula(Integer horarioTurma, Integer sala, Boolean controlarChoqueSala, Date data, String horaInicio, String horaTermino, boolean retornarExcecao) throws Exception {		
		if(Uteis.isAtributoPreenchido(sala) && controlarChoqueSala){
				StringBuilder sql = getSqlConsulta();
				sql.append(" where horarioTurma.codigo != ").append(horarioTurma).append(" and ");				
				sql.append(" horarioturmadiaitem.data = '").append(Uteis.getDataJDBC(data)).append("' and sala.codigo = ").append(sala);					
				sql.append(" and ( ");
				/**
				 * Obtem o choque de horário nas seguinte condições EX:
				 *      |------------------------------------------|        Horario Base (08:00 às 09:00)
				 *      |------------------------------------------------|  Horario Choque (08:00 às 10:00) caso 1
				 *      |------------------------------------------|        Horario Choque (08:00 às 09:00) caso 2
				 *  |----------------------------------------------------|  Horario Choque (07:00 às 10:00) caso 3
				 *  |----------------------------------------------|        Horario Choque (07:00 às 09:00) caso 4
				 */
				sql.append(" (horarioturmadiaitem.horarioinicio <= '").append(horaInicio).append("' and '").append(horaTermino).append("' <= horarioturmadiaitem.horarioTermino) ");
				/**
				 * Obtem o choque de horário nas seguinte condições EX:
				 *      |------------------------------------------|        Horario Base (08:00 às 09:00)
				 *      |--------------------------------|                  Horario Choque (08:00 às 08:40) caso 5
				 *   |-----------------------------------|                  Horario Choque (07:00 às 08:40) caso 8				 
				 */
				sql.append(" or (horarioturmadiaitem.horarioinicio <= '").append(horaInicio).append("' and horarioturmadiaitem.horarioTermino > '").append(horaInicio).append("' and '").append(horaTermino).append("' >= horarioturmadiaitem.horarioTermino) ");
				/**
				 * Obtem o choque de horário nas seguinte condições EX:
				 *      |------------------------------------------|        Horario Base (08:00 às 09:00)
				 *             |---------------------------|                Horario Choque (08:20 às 08:40) caso 6				 
				 */
				sql.append(" or (horarioturmadiaitem.horarioinicio >= '").append(horaInicio).append("' and '").append(horaTermino).append("' >= horarioturmadiaitem.horarioTermino) ");
				/**
				 * Obtem o choque de horário nas seguinte condições EX:
				 *      |------------------------------------------|        Horario Base (08:00 às 09:00)
				 *             |----------------------------------------|   Horario Choque (08:20 às 09:40) caso 7				 
				 */
				sql.append(" or (horarioturmadiaitem.horarioinicio >= '").append(horaInicio).append("' and '").append(horaTermino).append("' < horarioturmadiaitem.horarioTermino and '").append(horaTermino).append("' > horarioturmadiaitem.horarioInicio) ");								
				sql.append(" ) limit 1");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if(rs.next()){
				if(retornarExcecao){
					ChoqueHorarioVO choqueHorarioVO = new ChoqueHorarioVO(rs.getDate("data"), rs.getString("turma.identificadorTurma"), rs.getString("disciplina.nome"), rs.getString("professor.nome"), rs.getString("localaula.local") +" - "+rs.getString("sala.sala"));
					choqueHorarioVO.setHorario(rs.getString("horario"));
					choqueHorarioVO.setTurno(rs.getString("turno.nome"));
					choqueHorarioVO.setNrAula(rs.getInt("nrAula"));
					throw choqueHorarioVO;
				}else{
					return true;
				}
			}				
		}				
		return false;
	}
	
	@Override
	public SqlRowSet consultarProgramacaoAulaHorarioTurmaRel(Integer turma, String ano, String semestre, Date dataInicio, Date dataTermino) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("select * from (select turma.identificadorturma, horarioturmadiaitem.data, horarioturmadiaitem.horarioinicio, horarioturmadiaitem.horariotermino, ");
		sql.append("professor.nome as professor, disciplina.nome as disciplina, localaula.local, sala.sala, horarioturmadiaitem.nraula, EXTRACT(DOW FROM horarioturmadiaitem.data)::int as diaSemana ");
		sql.append("from horarioturmadiaitem ");
		sql.append("inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
		sql.append("inner join turma on turma.codigo = horarioturma.turma ");
		sql.append("inner join turno on turno.codigo = turma.turno ");
		sql.append("left join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sql.append("left join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ");
		sql.append("left join salalocalaula as sala on sala.codigo = horarioturmadiaitem.sala ");
		sql.append("left join localaula on localaula.codigo = sala.localaula ");
		sql.append("where disciplina.codigo is not null and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "horarioturmadiaitem.data", false));
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and horarioturma.anovigente = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and horarioturma.semestrevigente = '").append(semestre).append("' ");
		}
		sql.append("union ");
		sql.append("select identificadorturma, null as data, horarioInicioAula, horarioFinalAula, null as professor, null as disciplina, null as local, null as sala, numeroAula, (turnohorario.diaSemana::int - 1)::int as diasemana ");
		sql.append("from turma ");
		sql.append("inner join turno on turno.codigo = turma.turno ");
		sql.append("inner join turnohorario on turnohorario.turno = turno.codigo ");
		if (Uteis.isAtributoPreenchido(turma)) {
			sql.append(" and turma.codigo = ").append(turma);
		}
		sql.append("and not exists (");
		sql.append("	select 1 from horarioturmadiaitem htdi");
		sql.append("	inner join horarioturmadia htd on htd.codigo = htdi.horarioturmadia");
		sql.append("	inner join horarioturma ht on ht.codigo = htd.horarioturma");
		sql.append(" 	where ht.turma = ").append(turma);
		sql.append("	and htdi.disciplina is not null and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "htdi.data", false));
		sql.append("	and htdi.horarioinicio = turnohorario.horarioInicioAula");
		sql.append("	and htdi.horariotermino = turnohorario.horarioFinalAula");
		sql.append("	and EXTRACT(DOW FROM htdi.data)::int = (turnohorario.diaSemana::int - 1)::int");
		sql.append("	and htdi.nraula = turnohorario.numeroAula");
		if (Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and ht.anovigente = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and ht.semestrevigente = '").append(semestre).append("' ");
		}
		sql.append(")) as t");
		sql.append(" order by data, nraula, diasemana ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(Integer horarioTurmaDiaItem, UsuarioVO usuario) throws Exception {
		HorarioTurmaDiaItem.excluir(getIdEntidade());
		String sql = "DELETE FROM horarioTurmaDiaItem WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] {horarioTurmaDiaItem });
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarNovaDisciplinaHorarioTurmaDiaItemPorAlteracaoGradeCurricularIntegral(Integer turma, Integer disciplina, Integer novaDisiplna, UsuarioVO usuario) throws Exception {
		HorarioTurmaDiaItem.alterar(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder(" UPDATE  horarioTurmaDiaItem set disciplina = ").append(novaDisiplna);
		sqlStr.append(" WHERE codigo in( ");
		sqlStr.append(" SELECT horarioTurmaDiaItem.codigo FROM horarioTurmaDiaItem  ");
		sqlStr.append(" INNER JOIN HorarioTurmadia ON HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo  ");
		sqlStr.append(" INNER JOIN HorarioTurma ON HorarioTurmadia.HorarioTurma = HorarioTurma.codigo");
		sqlStr.append(" WHERE HorarioTurma.turma = ").append(turma).append(" ");
		sqlStr.append(" AND HorarioTurmadiaitem.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorTurmaPorDisciplina(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception {
		HorarioTurmaDiaItem.excluir(getIdEntidade());
		StringBuilder sqlStr = new StringBuilder(" DELETE FROM horarioTurmaDiaItem ");
		sqlStr.append(" WHERE codigo in( ");
		sqlStr.append(" SELECT horarioTurmaDiaItem.codigo FROM horarioTurmaDiaItem  ");
		sqlStr.append(" INNER JOIN HorarioTurmadia ON HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo  ");
		sqlStr.append(" INNER JOIN HorarioTurma ON HorarioTurmadia.HorarioTurma = HorarioTurma.codigo");
		sqlStr.append(" WHERE HorarioTurma.turma = ").append(turma).append(" ");
		sqlStr.append(" AND HorarioTurmadiaitem.disciplina = ").append(disciplina).append(" ");
		sqlStr.append(" ) ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	
	
	@Override
	public HorarioTurmaDiaItemVO consultarAulaProgramadaPorTurmaDisciplinaAnoSemestreDistribuicaoSubturma(Integer turma, Integer disciplina, String ano, String semestre, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		HorarioTurmaDiaItem.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct horarioturmadiaitem.codigo, turma.codigo as turma, turma.identificadorturma, turma.turmaagrupada from horarioturma ");
		sqlStr.append("inner join turma on turma.codigo = horarioturma.turma ");
		sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append("inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append("where anovigente = '").append(ano).append("' ");
		sqlStr.append("and semestrevigente = '").append(semestre).append("' ");
		sqlStr.append("and (turma = ").append(turma).append(" or turma.codigo in (select turmaorigem from turmaagrupada where turma = ").append(turma).append(")) ");
		sqlStr.append("and (");
		sqlStr.append("	(turma.turmaagrupada = false and disciplina = ").append(disciplina).append(")");
		sqlStr.append("	or (turma.turmaagrupada and disciplina in (");
		sqlStr.append("		select ").append(disciplina);
		sqlStr.append("		union select disciplina from disciplinaequivalente  where equivalente = ").append(disciplina);
		sqlStr.append("		union select equivalente from disciplinaequivalente  where disciplina  = ").append(disciplina);
		sqlStr.append("	)))");
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		HorarioTurmaDiaItemVO obj = new HorarioTurmaDiaItemVO();
		if (rowSet.next()) {
			obj.setCodigo(rowSet.getInt("codigo"));
			obj.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().setCodigo(rowSet.getInt("turma"));
			obj.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().setIdentificadorTurma(rowSet.getString("identificadorturma"));
			obj.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().setTurmaAgrupada(rowSet.getBoolean("turmaagrupada"));
		}
		return obj;
	}

	@Override
	public HashMap<String, String> consultarMenorHorarioMaiorHorarioProgramacaoAulaDisciplinasAluno(String matricula, String ano, String semestre, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		HorarioTurmaDiaItem.consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select min(horarioinicio) as horarioinicio, max(horariotermino) as horariotermino from horarioturmadiaitem  ");
		sqlStr.append(" inner join horarioturmadia on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo  ");
		sqlStr.append(" inner join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" inner join turma on horarioturma.turma = turma.codigo ");
		sqlStr.append(" where horarioturma.anovigente = '").append(ano).append("'").append("and horarioturma.semestrevigente = '").append(semestre).append("'");
		sqlStr.append(" and exists ( ");
		sqlStr.append(" select historico.codigo, matriculaPeriodoTurmaDisciplina.disciplina, matriculaPeriodoTurmaDisciplina.turma from historico  ");
		sqlStr.append(" inner JOIN matriculaPeriodo on matriculaPeriodo.matricula = historico.matricula and matriculaPeriodo.codigo = historico.matriculaPeriodo  ");
		sqlStr.append(" inner JOIN matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.codigo = historico.matriculaPeriodoTurmaDisciplina ");
		sqlStr.append(" where historico.matricula = '").append(matricula).append("' ");
		sqlStr.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		sqlStr.append(" and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		sqlStr.append(" and ((turma.turmaagrupada = false and (turma.subturma = false or (turma.subturma = true and turma.tiposubturma = 'GERAL')) and matriculaPeriodoTurmaDisciplina.turma = turma.codigo)");
		sqlStr.append(" or  (turma.subturma and turma.tiposubturma = 'PRATICA' and matriculaPeriodoTurmaDisciplina.turmapratica =  turma.codigo)");
		sqlStr.append(" or  (turma.subturma and turma.tiposubturma = 'TEORICA' and matriculaPeriodoTurmaDisciplina.turmateorica =  turma.codigo)");
		sqlStr.append(" or  (turma.turmaagrupada = true and turma.subturma = false and exists(select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = matriculaperiodoturmadisciplina.turma)))");
		sqlStr.append(" and ((turma.turmaagrupada = false and horarioturmadiaitem.disciplina = matriculaPeriodoTurmaDisciplina.disciplina)");
		sqlStr.append(" or (turma.turmaagrupada = true and ((horarioturmadiaitem.disciplina = matriculaPeriodoTurmaDisciplina.disciplina) or exists(select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina");
		sqlStr.append(" and disciplinaequivalente.equivalente = matriculaPeriodoTurmaDisciplina.disciplina)	or exists(select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina");
		sqlStr.append(" and disciplinaequivalente.disciplina = matriculaPeriodoTurmaDisciplina.disciplina) or exists(select turmadisciplina.codigo from turmadisciplina where turmadisciplina.turma = turma.codigo and  turmadisciplina.disciplina = horarioturmadiaitem.disciplina and turmadisciplina.disciplinaEquivalenteTurmaAgrupada = matriculaPeriodoTurmaDisciplina.disciplina))))");
		sqlStr.append(" and (historico.gradecurriculargrupooptativadisciplina is not null or historico.gradedisciplina is not null or historico.historicodisciplinaforagrade)");
		sqlStr.append(" and (historico.historicoequivalente = false or historico.historicoequivalente is null) ");
		sqlStr.append(" and (historico.historicodisciplinafazpartecomposicao = false or historico.historicodisciplinafazpartecomposicao is null))");
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		HashMap<String,String> mapHorarios = new HashMap<String, String>(0);
		if (rowSet.next()) {
			mapHorarios.put("min", rowSet.getString("horarioinicio"));
			mapHorarios.put("max", rowSet.getString("horariotermino"));
		}
		return mapHorarios;
	}
	
	@Override
	public HorarioTurmaDiaItemVO consultarFuncionarioCargoPorCodigo(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select horarioturmadiaitem.* from horarioturmadiaitem ");
		sqlStr.append(" where codigo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		HorarioTurmaDiaItemVO horarioTurmaDiaItemVO = new HorarioTurmaDiaItemVO();

		if (rs.next()) {
			horarioTurmaDiaItemVO.setCodigo(rs.getInt("codigo"));
			horarioTurmaDiaItemVO.setHorario(rs.getString("horario"));
			horarioTurmaDiaItemVO.setHorarioInicio(rs.getString("horarioInicio"));
			horarioTurmaDiaItemVO.setHorarioTermino(rs.getString("horarioTermino"));
			horarioTurmaDiaItemVO.setNrAula(rs.getInt("nrAula"));
			horarioTurmaDiaItemVO.setDuracaoAula(rs.getInt("duracaoAula"));
			horarioTurmaDiaItemVO.setData(rs.getDate("data"));
			horarioTurmaDiaItemVO.getSala().setCodigo(rs.getInt("sala"));
			horarioTurmaDiaItemVO.getSala().setNovoObj(horarioTurmaDiaItemVO.getSala().getCodigo().equals(0));
			
			if (Uteis.isAtributoPreenchido(rs.getInt("funcionarioCargo"))) {
				horarioTurmaDiaItemVO.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorCodigo(rs.getInt("funcionarioCargo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			}
		}
		return horarioTurmaDiaItemVO;
	}

	@Override
	public HorarioTurmaDiaItemVO consultarPorCodigo(Integer codigo) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select horarioturmadiaitem.* from horarioturmadiaitem ");
		sqlStr.append(" where codigo = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		HorarioTurmaDiaItemVO horarioTurmaDiaItemVO = new HorarioTurmaDiaItemVO();

		if (rs.next()) {
			horarioTurmaDiaItemVO.setCodigo(rs.getInt("codigo"));
			horarioTurmaDiaItemVO.setHorario(rs.getString("horario"));
			horarioTurmaDiaItemVO.setHorarioInicio(rs.getString("horarioInicio"));
			horarioTurmaDiaItemVO.setHorarioTermino(rs.getString("horarioTermino"));
			horarioTurmaDiaItemVO.setNrAula(rs.getInt("nrAula"));
			horarioTurmaDiaItemVO.setDuracaoAula(rs.getInt("duracaoAula"));
			horarioTurmaDiaItemVO.setData(rs.getDate("data"));
			horarioTurmaDiaItemVO.getSala().setCodigo(rs.getInt("sala"));
			horarioTurmaDiaItemVO.getSala().setNovoObj(horarioTurmaDiaItemVO.getSala().getCodigo().equals(0));

			if (Uteis.isAtributoPreenchido(rs.getInt("funcionarioCargo"))) {
				horarioTurmaDiaItemVO.setFuncionarioCargoVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorCodigo(rs.getInt("funcionarioCargo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
			}
		}
		return horarioTurmaDiaItemVO;
	}

	@Override
	public List<HorarioTurmaDiaItemVO> consultarPorGoogleMeet(GoogleMeetVO googleMeetVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT horarioturmadiaitem.codigo, nraula, duracaoaula, horario, horarioinicio, horariotermino, horarioturmadiaitem.data,");
		sql.append(" horarioturmadia, googlemeet, turma.codigo \"turma.codigo\", turma.identificadorturma FROM horarioturmadiaitem ");
		sql.append(" left join horarioturmadia on horarioturmadia.codigo = horarioturmadia");
		sql.append(" left join horarioturma on horarioturmadia.horarioturma = horarioturma.codigo");
		sql.append(" left join turma on turma.codigo = horarioturma");
		sql.append(" WHERE googleMeet = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), googleMeetVO.getCodigo());
		HorarioTurmaDiaItemVO obj;
		List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs = new ArrayList<>(0);

		while (rs.next()) {
			obj = new HorarioTurmaDiaItemVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setNrAula(rs.getInt("nrAula"));
			obj.setDuracaoAula(rs.getInt("duracaoAula"));
			obj.setHorario(rs.getString("horario"));
			obj.setHorarioInicio(rs.getString("horarioInicio"));
			obj.setHorarioTermino(rs.getString("horarioTermino"));
			obj.setData(rs.getDate("data"));
			obj.getHorarioTurmaDiaVO().setCodigo(rs.getInt("horarioturmadia"));
			obj.getGoogleMeetVO().setCodigo(rs.getInt("googlemeet"));;
			obj.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().setCodigo(rs.getInt("turma.codigo"));
			obj.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().setIdentificadorTurma(rs.getString("identificadorturma"));
			horarioTurmaDiaItemVOs.add(obj);
		}
		return horarioTurmaDiaItemVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAulaResposicao(boolean aulaResposicao, Integer codigo, UsuarioVO usuarioVO) {
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {

				StringBuilder sql = new StringBuilder("UPDATE public.horarioturmadiaitem  SET aulareposicao = ? WHERE codigo = ? ")
								.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(aulaResposicao, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(codigo, ++i, sqlAlterar);

				return sqlAlterar;
			}
		});
	}
	
	@Override
	public Integer consultarQtdeAulaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct count(distinct horarioturmadiaitem.codigo) as qtde from horarioturmadiaitem ");
		sb.append(" inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sb.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
		sb.append(" inner join turma on turma.codigo = horarioturma.turma ");
		sb.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sb.append(" where  turmadisciplina.gradedisciplina = ?");
		sb.append(" and turmadisciplina.disciplina = horarioturmadiaitem.disciplina ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] {gradeDisciplina});
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}
}
