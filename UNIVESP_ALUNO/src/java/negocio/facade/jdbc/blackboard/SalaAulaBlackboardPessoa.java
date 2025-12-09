package negocio.facade.jdbc.blackboard;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.blackboard.enumeradores.SituacaoHistoricoNotaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.blackboard.SalaAulaBlackboardPessoaInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Repository
@Scope("singleton")
@Lazy
public class SalaAulaBlackboardPessoa extends ControleAcesso implements SalaAulaBlackboardPessoaInterfaceFacade {

	/**
	 *
	 */
	private static final long serialVersionUID = 6775425103475495367L;

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarTransferenciaInternaMatriculaEnsalada(MatriculaVO matricula, List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO mptd : listaMatriculaPeriodoTurmaDisciplinaVO) {
			SalaAulaBlackboardPessoaVO sap = consultarSalaAulaBlackboardPessoaPorMatriculaPorDisciplinaPorTipoSalaAulaBlackboardEnum(matricula.getTransferenciaEntrada().getMatricula().getMatricula(), mptd.getDisciplina().getCodigo(), TipoSalaAulaBlackboardEnum.DISCIPLINA, usuarioVO);
			if(Uteis.isAtributoPreenchido(sap)) {
				sap.setCodigo(0);
				sap.setMatricula(matricula.getMatricula());
				sap.setMatriculaPeriodoTurmaDisciplina(mptd.getCodigo());
				incluir(sap, usuarioVO);
			}
		}
	}
	
	public Integer consultarQuantidadeMatriculaPorSalaAulaBlackboard(SalaAulaBlackboardVO obj, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(distinct salaaulablackboardpessoa.codigo) AS QTDE FROM salaaulablackboardpessoa ");
		sql.append("inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
		sql.append(" WHERE salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = ? ");
		sql.append(" AND salaaulablackboard.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {tipoSalaAulaBlackboardPessoaEnum.name(), obj.getCodigo()});
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public SalaAulaBlackboardPessoaVO consultarSalaAulaBlackboardPessoaPorMatriculaPorDisciplinaPorTipoSalaAulaBlackboardEnum(String matricula, Integer disciplina, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, UsuarioVO usuarioVO) {
		StringBuilder sqlStr = getSQLPadraoConsulta();
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = salaaulablackboardpessoa.matriculaperiodoturmadisciplina ");		
		sqlStr.append(" WHERE salaaulablackboardpessoa.matricula = ? ");
		sqlStr.append(" and salaaulablackboard.tiposalaaulablackboardenum = ? ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {matricula, tipoSalaAulaBlackboardEnum.name(), disciplina});
		SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
		if (tabelaResultado.next()) {
			montarDados(sabp, tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		}
		return sabp;
	}

	@Override
	public Boolean consultarSeExisteInscricaoSalaAulaBlackboardGrupo(MatriculaVO matricula, Integer disciplina, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, TipoSalaAulaBlackboardEnum tipoSalaAulaBlackboardEnum, String ano, String semestre, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(salaaulablackboardpessoa.codigo) AS QTDE from salaaulablackboardpessoa ");
		sql.append(" inner join salaaulablackboard on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo  ");
		sql.append(" WHERE salaaulablackboardpessoa.matricula = ? ");
		sql.append(" and salaaulablackboardpessoa.tipoSalaAulaBlackboardPessoaEnum = ? ");
		sql.append("and salaaulablackboard.tiposalaaulablackboardenum = ? ");
		if(tipoSalaAulaBlackboardEnum.isProjetoIntegradorGrupo()) {
			sql.append(" and salaaulablackboard.disciplina = ").append(disciplina).append(" ");
			sql.append(" and exists ( ");
			sql.append(" 	select agrupamentounidadeensinoitem.codigo  from agrupamentounidadeensinoitem");
			sql.append(" 	where agrupamentounidadeensinoitem.unidadeensino =  ").append(matricula.getUnidadeEnsino().getCodigo());
			sql.append(" 	and agrupamentounidadeensinoitem.agrupamentounidadeensino = salaaulablackboard.agrupamentounidadeensino");
			sql.append(" )");	
		}
		if(Uteis.isAtributoPreenchido(ano)) {
			sql.append(" and salaaulablackboard.ano ='").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sql.append(" and salaaulablackboard.semestre ='").append(semestre).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {matricula.getMatricula(), tipoSalaAulaBlackboardPessoaEnum.name(), tipoSalaAulaBlackboardEnum.name()});
		return Uteis.isAtributoPreenchido(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}

	@Override
	public List<SalaAulaBlackboardPessoaVO> consultarAlunosDoHorarioTurmaDisciplinaDisponivel(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor,boolean isSomentePessoaEmailInstitucional, Integer salaAulaBlackboard, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorHistorico(unidadeEnsino, curso, turma, disciplina, ano, semestre, codigoProfessor, configuracaoGeralSistemaVO, usuarioVO);
		sqlStr.append(" WHERE 1=1  ");
		if(isSomentePessoaEmailInstitucional) {
			sqlStr.append(" and pessoaEmailInstitucional.pessoa is not null ");
			sqlStr.append(" and pessoaEmailInstitucional.statusAtivoInativoEnum = '").append(StatusAtivoInativoEnum.ATIVO).append("' ");
		}
		if(Uteis.isAtributoPreenchido(salaAulaBlackboard)) {
			sqlStr.append(" and (salaaulablackboardpessoa.salaaulablackboard is null or salaaulablackboardpessoa.salaaulablackboard =  ").append(salaAulaBlackboard).append(") ");
		}else {
			sqlStr.append(" and (salaaulablackboardpessoa.salaaulablackboard is null) ");
		}
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorHistorico(tabelaResultado);
	}

	private StringBuilder getSQLPadraoConsultaBasicaPorHistorico(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(unidadeEnsino, usuarioVO);
		boolean permitiVisualizarAlunoTR_CA = Uteis.isAtributoPreenchido(usuarioVO) ? verificarPermissaoVisualizarAlunoTR_CA(usuarioVO) : true;
		boolean permiteLancarNotaDisciplinaComposta = Uteis.isAtributoPreenchido(usuarioVO) ? verificarPermiteLancarNotaDisciplinaComposta(usuarioVO) : true;
		boolean permitirRealizarLancamentoAlunosPreMatriculados = configuracaoGeralSistemaVO.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select  ");
		sqlStr.append(" pessoaemailinstitucional.codigo as \"pessoaemailinstitucional.codigo\", ");
		sqlStr.append(" pessoaemailinstitucional.email as \"pessoaemailinstitucional.email\", ");
		sqlStr.append(" pessoaemailinstitucional.statusAtivoInativoEnum as \"pessoaemailinstitucional.statusAtivoInativoEnum\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
		sqlStr.append(" pessoa.email2 as \"pessoa.email2\", ");
		sqlStr.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sqlStr.append(" pessoa.aluno as \"pessoa.aluno\", ");
		sqlStr.append(" t.\"matricula.matricula\", ");
		sqlStr.append(" t.\"matriculaPeriodoTurmaDisciplina.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.nome\", ");
		sqlStr.append(" salaaulablackboardpessoa.salaaulablackboard ");
		sqlStr.append(" from  ( ");
		sqlStr.append(getFacadeFactory().getHistoricoFacade().getSqlCompletoConsultaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(unidadeEnsino, curso, disciplina, turma, ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, codigoProfessor, false, null, permitiVisualizarAlunoTR_CA, permiteLancarNotaDisciplinaComposta, Uteis.NIVELMONTARDADOS_TODOS, null, null, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados));
		sqlStr.append(" ) as t ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = t.\"aluno.codigo\" ");
		sqlStr.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo  ");
		sqlStr.append(" left join salaaulablackboardpessoa on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional and t.\"matriculaPeriodoTurmaDisciplina.codigo\" = salaaulablackboardpessoa.matriculaperiodoturmadisciplina ");
		
		return sqlStr;
	}

	@Override
	public List<SalaAulaBlackboardPessoaVO> consultarAlunosDoEadTurmaDisciplinaDisponivel(CursoVO curso, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer bimestre, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean isSomentePessoaGsuite, Integer salaaulablackboard, UsuarioVO usuarioVO) throws Exception {
		if(programacaoTutoriaOnlineVO== null) {
			programacaoTutoriaOnlineVO =  new ProgramacaoTutoriaOnlineVO();
		}
		programacaoTutoriaOnlineVO.setCursoVO(curso);
		programacaoTutoriaOnlineVO.setTurmaVO(turma);
		programacaoTutoriaOnlineVO.setDisciplinaVO(disciplina);		
		programacaoTutoriaOnlineVO.setAno(ano);
		programacaoTutoriaOnlineVO.setSemestre(semestre);		
		
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorEad(programacaoTutoriaOnlineVO, null, bimestre, usuarioVO);
		sqlStr.append(" WHERE 1=1  ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and t.ano = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and t.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(bimestre)) {
			sqlStr.append(" and t.bimestre = ").append(bimestre).append(" ");
		}
		if (isSomentePessoaGsuite) {
			sqlStr.append(" and pessoaemailinstitucional.pessoa is not null ");
			sqlStr.append(" and pessoaemailinstitucional.statusAtivoInativoEnum = '").append(StatusAtivoInativoEnum.ATIVO).append("' ");
		}
		if(Uteis.isAtributoPreenchido(salaaulablackboard)) {
			sqlStr.append(" and (salaaulablackboardpessoa.salaaulablackboard is null or salaaulablackboardpessoa.salaaulablackboard =  ").append(salaaulablackboard).append(") ");	
		}else {
			sqlStr.append(" and salaaulablackboardpessoa.salaaulablackboard is null ");
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorHistorico(tabelaResultado);
	}

	private StringBuilder getSQLPadraoConsultaBasicaPorEad(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select distinct ");
		sqlStr.append(" pessoaemailinstitucional.codigo as \"pessoaemailinstitucional.codigo\", ");
		sqlStr.append(" pessoaemailinstitucional.email as \"pessoaemailinstitucional.email\", ");
		sqlStr.append(" pessoaemailinstitucional.statusAtivoInativoEnum as \"pessoaemailinstitucional.statusAtivoInativoEnum\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
		sqlStr.append(" pessoa.email2 as \"pessoa.email2\", ");
		sqlStr.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sqlStr.append(" pessoa.aluno as \"pessoa.aluno\", ");
		sqlStr.append(" t.\"matricula\" as \"matricula.matricula\", ");
		sqlStr.append(" t.\"matriculaperiodoturmadisciplina.codigo\" as \"matriculaperiodoturmadisciplina.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.nome\", ");
		sqlStr.append(" salaaulablackboardpessoa.salaaulablackboard,  ");
		sqlStr.append(" t.ano, ");
		sqlStr.append(" t.semestre, ");
		sqlStr.append(" t.bimestre ");
		sqlStr.append(" from  ( ");
		sqlStr.append(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().executarInicializacaoSqlConsultaMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, bimestre, false, false));
		sqlStr.append(" ) as t ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = t.codigopessoa ");
		sqlStr.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo  ");
		sqlStr.append(" left join salaaulablackboardpessoa on pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional and t.\"matriculaperiodoturmadisciplina.codigo\" = salaaulablackboardpessoa.matriculaperiodoturmadisciplina ");
		return sqlStr;
	}

	public List<SalaAulaBlackboardPessoaVO> montarDadosConsultaPorHistorico(SqlRowSet tabelaResultado) throws Exception {
		List<SalaAulaBlackboardPessoaVO> vetResultado = new ArrayList<SalaAulaBlackboardPessoaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosPorHistorico(tabelaResultado));
		}
		return vetResultado;
	}

	private SalaAulaBlackboardPessoaVO montarDadosPorHistorico(SqlRowSet dadosSQL) {
		SalaAulaBlackboardPessoaVO obj = new SalaAulaBlackboardPessoaVO();
		obj.getPessoaEmailInstitucionalVO().setCodigo((dadosSQL.getInt("pessoaEmailInstitucional.codigo")));
		obj.getPessoaEmailInstitucionalVO().setEmail(dadosSQL.getString("pessoaEmailInstitucional.email"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum"))) {
			obj.getPessoaEmailInstitucionalVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum")));
		}
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
		obj.setMatricula(dadosSQL.getString("matricula.matricula"));
		obj.setMatriculaPeriodoTurmaDisciplina(dadosSQL.getInt("matriculaperiodoturmadisciplina.codigo"));
		obj.getSalaAulaBlackboardVO().setCodigo(dadosSQL.getInt("salaAulaBlackboard"));		
		return obj;
	}


	private StringBuilder getSQLPadraoConsulta()  {

		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select count(*) over() as totalRegistroConsulta, ");
		sqlStr.append(" salaAulaBlackboardPessoa.codigo as \"salaAulaBlackboardPessoa.codigo\", ");
		sqlStr.append(" salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum as \"salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum\", ");
		sqlStr.append(" salaAulaBlackboardPessoa.matricula as \"salaAulaBlackboardPessoa.matricula\", ");
		sqlStr.append(" salaaulablackboardpessoa.userId as \"salaaulablackboardpessoa.userId\", ");
		sqlStr.append(" salaaulablackboardpessoa.memberId as \"salaaulablackboardpessoa.memberId\", ");

		sqlStr.append(" salaaulablackboard.codigo as \"salaaulablackboard.codigo\", ");
		sqlStr.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\", ");
		
		sqlStr.append(" salaAulaBlackboardPessoa.matriculaperiodoturmadisciplina as \"salaAulaBlackboardPessoa.matriculaperiodoturmadisciplina\", ");
		sqlStr.append(" pessoaemailinstitucional.codigo as \"pessoaemailinstitucional.codigo\", ");
		sqlStr.append(" pessoaemailinstitucional.email as \"pessoaemailinstitucional.email\", ");
		sqlStr.append(" pessoaemailinstitucional.nome as \"pessoaemailinstitucional.nome\", ");
		sqlStr.append(" pessoaemailinstitucional.statusAtivoInativoEnum as \"pessoaemailinstitucional.statusAtivoInativoEnum\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
		sqlStr.append(" pessoa.cpf as \"pessoa.cpf\", ");
		sqlStr.append(" pessoa.registroacademico as \"pessoa.registroacademico\" ");
		sqlStr.append(" from salaAulaBlackboardPessoa  ");
		sqlStr.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaAulaBlackboardPessoa.pessoaemailinstitucional  ");
		sqlStr.append(" left join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa ");
		sqlStr.append(" inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
		return sqlStr;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<SalaAulaBlackboardPessoaVO> consultarPorSalaAulaBlackboard(SalaAulaBlackboardVO obj, int nivelMontarDados, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsulta();
			sqlStr.append(" where salaAulaBlackboardPessoa.salaAulaBlackboard = ? order by pessoa.nome, pessoaemailinstitucional.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			montarTotalizadorConsultaBasica(obj.getDadosConsultaAlunos(), tabelaResultado);
			List<SalaAulaBlackboardPessoaVO> lista = new ArrayList<SalaAulaBlackboardPessoaVO>();
			while (tabelaResultado.next()) {
				SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
				montarDados(sabp, tabelaResultado, nivelMontarDados, usuario);
				sabp.setSalaAulaBlackboardVO(obj);
				lista.add(sabp);
			}
			return lista;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	public void consultarAlunosPorSalaAulaBlackboardOtimizado(SalaAulaBlackboardVO obj, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, int nivelMontarDados, UsuarioVO usuarioVO) {
		try {
			StringBuilder sqlStr = new StringBuilder(" ");
			sqlStr.append(" select count(*) over() as totalRegistroConsulta, ");
			sqlStr.append(" salaAulaBlackboardPessoa.codigo as \"salaAulaBlackboardPessoa.codigo\", ");
			sqlStr.append(" salaAulaBlackboardPessoa.salaAulaBlackboard as \"salaAulaBlackboardPessoa.salaAulaBlackboard\", ");
			sqlStr.append(" salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum as \"salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum\", ");
			sqlStr.append(" salaAulaBlackboardPessoa.matricula as \"salaAulaBlackboardPessoa.matricula\", ");
			sqlStr.append(" salaaulablackboardpessoa.userId as \"salaaulablackboardpessoa.userId\", ");
			sqlStr.append(" salaaulablackboardpessoa.memberId as \"salaaulablackboardpessoa.memberId\", ");
			sqlStr.append(" salaaulablackboard.codigo as \"salaaulablackboard.codigo\", ");
			sqlStr.append(" salaaulablackboard.idSalaAulaBlackboard as \"salaaulablackboard.idSalaAulaBlackboard\", ");
			sqlStr.append(" salaAulaBlackboardPessoa.matriculaperiodoturmadisciplina as \"salaAulaBlackboardPessoa.matriculaperiodoturmadisciplina\", ");
			sqlStr.append(" pessoaemailinstitucional.codigo as \"pessoaemailinstitucional.codigo\", ");
			sqlStr.append(" pessoaemailinstitucional.email as \"pessoaemailinstitucional.email\", ");
			sqlStr.append(" pessoaemailinstitucional.nome as \"pessoaemailinstitucional.nome\", ");
			sqlStr.append(" pessoaemailinstitucional.statusAtivoInativoEnum as \"pessoaemailinstitucional.statusAtivoInativoEnum\", ");
			sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
			sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
			sqlStr.append(" pessoa.email as \"pessoa.email\", ");
			sqlStr.append(" pessoa.cpf as \"pessoa.cpf\", ");
			sqlStr.append(" pessoa.registroacademico as \"pessoa.registroacademico\", ");
			sqlStr.append(" t.codigo as \"historiconotablackboard.codigo\", ");
			sqlStr.append(" t.nota as \"historiconotablackboard.nota\", ");
			sqlStr.append(" t.situacaohistoriconotablackboardenum as \"historiconotablackboard.situacaohistoriconotablackboardenum\", ");
			sqlStr.append(" t.notaanterior as \"historiconotablackboard.notaanterior\", ");
			sqlStr.append(" t.motivo as \"historiconotablackboard.motivo\", ");
			sqlStr.append(" historico.codigo as \"historico.codigo\", ");
			sqlStr.append(" historico.mediafinal as \"historico.mediafinal\" ");
			sqlStr.append(" from salaAulaBlackboardPessoa ");
			sqlStr.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = salaAulaBlackboardPessoa.pessoaemailinstitucional  ");
			sqlStr.append(" inner join salaaulablackboard on salaaulablackboard.codigo = salaaulablackboardpessoa.salaaulablackboard ");
			sqlStr.append(" inner join matricula on matricula.matricula = salaaulablackboardpessoa.matricula ");
			sqlStr.append(" left join pessoa on pessoa.codigo = pessoaemailinstitucional.pessoa ");
			sqlStr.append(" left join lateral (select * from historiconotablackboard where historiconotablackboard.salaAulaBlackboardPessoa = salaAulaBlackboardPessoa.codigo ");
			sqlStr.append(" and historiconotablackboard.situacaohistoriconotablackboardenum in ('");
			sqlStr.append(SituacaoHistoricoNotaBlackboardEnum.APURADO).append("','");
			sqlStr.append(SituacaoHistoricoNotaBlackboardEnum.EM_AUDITORIA).append("','");
		  	sqlStr.append(SituacaoHistoricoNotaBlackboardEnum.DEFERIDO).append("') order by historiconotablackboard.codigo desc limit 1 ) as t on true ");
			sqlStr.append(" left join historico on historico.codigo = t.historico ");
			sqlStr.append(" where salaAulaBlackboardPessoa.salaAulaBlackboard = ? ");
			sqlStr.append(" and salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum = '").append(TipoSalaAulaBlackboardPessoaEnum.ALUNO).append("' ");
			if(!filtroRelatorioAcademicoVO.isTodasSituacoesMatriculaDesmarcadas()) {
				sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
		    }
			if (Uteis.isAtributoPreenchido(obj.getFiltroAluno())) {
				sqlStr.append("AND ( lower (sem_acentos(pessoaemailinstitucional.nome)) like (sem_acentos('");
				sqlStr.append(obj.getFiltroAluno().toLowerCase());
				sqlStr.append("%')) ");
								
				sqlStr.append("OR ( lower (sem_acentos(pessoa.nome)) like (sem_acentos('");
				sqlStr.append(obj.getFiltroAluno().toLowerCase());
				sqlStr.append("%')) ");
				sqlStr.append(") ");
				
				sqlStr.append("OR ( lower (sem_acentos(pessoaemailinstitucional.email)) like (sem_acentos('");
				sqlStr.append(obj.getFiltroAluno().toLowerCase());
				sqlStr.append("%')) ");
				sqlStr.append(")) ");
			}
			
			sqlStr.append(" order by pessoa.nome, pessoaemailinstitucional.nome ");
			if(Uteis.isAtributoPreenchido(obj.getDadosConsultaAlunos().getLimitePorPagina())) {
				sqlStr.append(" limit ").append(obj.getDadosConsultaAlunos().getLimitePorPagina());
				sqlStr.append(" offset ").append(obj.getDadosConsultaAlunos().getOffset());	
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
			montarTotalizadorConsultaBasica(obj.getDadosConsultaAlunos(), tabelaResultado);
			List<SalaAulaBlackboardPessoaVO> lista = new ArrayList<SalaAulaBlackboardPessoaVO>();
			while (tabelaResultado.next()) {
				SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
				montarDadosHistoricoNota(sabp, tabelaResultado, nivelMontarDados, usuarioVO);
				sabp.setSalaAulaBlackboardVO(obj);
				sabp.setExisteUsuarioLogadoNaSala(sabp.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo().equals(usuarioVO.getPessoa().getCodigo()));
				lista.add(sabp);
			}
			obj.getDadosConsultaAlunos().setListaConsulta(lista);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

    @Override
    public void consultarProfessoresFacilitadoresESupervisoresPorSalaAulaBlackboardOtimizado(SalaAulaBlackboardVO obj, int nivelMontarDados, UsuarioVO usuarioVO) {
        try {
            StringBuilder sqlStr = getSQLPadraoConsulta();
            sqlStr.append(" where salaAulaBlackboardPessoa.salaAulaBlackboard = ? ");
            sqlStr.append(" and salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum in ( ");
            sqlStr.append("'").append(TipoSalaAulaBlackboardPessoaEnum.PROFESSOR).append("', ");
            sqlStr.append("'").append(TipoSalaAulaBlackboardPessoaEnum.FACILITADOR).append("', ");
            sqlStr.append("'").append(TipoSalaAulaBlackboardPessoaEnum.ORIENTADOR).append("' ");
            sqlStr.append(") ");
            
            if (Uteis.isAtributoPreenchido(obj.getFiltroSupervisor())) {
				sqlStr.append("AND ( lower (sem_acentos(pessoaemailinstitucional.nome)) like (sem_acentos('");
				sqlStr.append(obj.getFiltroSupervisor().toLowerCase());
				sqlStr.append("%')) ");
				
				sqlStr.append("OR ( lower (sem_acentos(pessoaemailinstitucional.email)) like (sem_acentos('");
				sqlStr.append(obj.getFiltroSupervisor().toLowerCase());
				sqlStr.append("%')) ");
				sqlStr.append(")) ");
			}

            sqlStr.append(" order by pessoa.nome, pessoaemailinstitucional.nome ");
            sqlStr.append(" limit ").append(obj.getDadosConsultaFacilitadores().getLimitePorPagina());
            sqlStr.append(" offset ").append(obj.getDadosConsultaFacilitadores().getOffset());
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
            montarTotalizadorConsultaBasica(obj.getDadosConsultaFacilitadores(), tabelaResultado);
            List<SalaAulaBlackboardPessoaVO> lista = new ArrayList<SalaAulaBlackboardPessoaVO>();
            while (tabelaResultado.next()) {
                SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
                montarDados(sabp, tabelaResultado, nivelMontarDados, usuarioVO);
                sabp.setSalaAulaBlackboardVO(obj);
                lista.add(sabp);
            }
            obj.getDadosConsultaFacilitadores().setListaConsulta(lista);
        } catch (Exception e) {
            throw new StreamSeiException(e);
        }
    }

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(SalaAulaBlackboardPessoaVO obj, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			alterar(obj, usuarioVO);
		} else {
			incluir(obj, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void excluir(SalaAulaBlackboardPessoaVO obj, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder("delete from salaaulablackboardpessoa where codigo = ? ");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(SalaAulaBlackboardPessoaVO obj, UsuarioVO usuarioVO) throws Exception {
		incluir(obj, "salaaulablackboardpessoa", new AtributoPersistencia()
						.add("salaaulablackboard", obj.getSalaAulaBlackboardVO().getCodigo())
						.add("pessoaemailinstitucional", obj.getPessoaEmailInstitucionalVO().getCodigo())
						.add("matricula", obj.getMatricula())
						.add("matriculaperiodoturmadisciplina", obj.getMatriculaPeriodoTurmaDisciplina().intValue() != 0 ? obj.getMatriculaPeriodoTurmaDisciplina() : null)
						.add("tiposalaaulablackboardpessoaenum", obj.getTipoSalaAulaBlackboardPessoaEnum().name())
						.add("memberid", obj.getMemberId())
						.add("userid", obj.getUserId())
				, usuarioVO);
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(SalaAulaBlackboardPessoaVO obj, UsuarioVO usuarioVO) throws Exception {
		alterar(obj, "salaaulablackboardpessoa", new AtributoPersistencia()
						.add("salaaulablackboard", obj.getSalaAulaBlackboardVO().getCodigo())
						.add("pessoaemailinstitucional", obj.getPessoaEmailInstitucionalVO().getCodigo())
						.add("matricula", obj.getMatricula())
						.add("matriculaperiodoturmadisciplina", obj.getMatriculaPeriodoTurmaDisciplina())
						.add("tiposalaaulablackboardpessoaenum", obj.getTipoSalaAulaBlackboardPessoaEnum().name())
						.add("memberid", obj.getMemberId())
						.add("userid", obj.getUserId())
				,new AtributoPersistencia().add("codigo", obj.getCodigo()),
				usuarioVO);
	}

	public List<SalaAulaBlackboardPessoaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) {
		List<SalaAulaBlackboardPessoaVO> vetResultado = new ArrayList<SalaAulaBlackboardPessoaVO>(0);
		while (tabelaResultado.next()) {
			SalaAulaBlackboardPessoaVO obj = new SalaAulaBlackboardPessoaVO();
			montarDados(obj, tabelaResultado, nivelMontarDados, usuarioVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDados(SalaAulaBlackboardPessoaVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("salaAulaBlackboardPessoa.codigo"));
		obj.getSalaAulaBlackboardVO().setCodigo(dadosSQL.getInt("salaaulablackboard.codigo"));
		obj.getSalaAulaBlackboardVO().setIdSalaAulaBlackboard(dadosSQL.getString("salaaulablackboard.idSalaAulaBlackboard"));
		obj.setMatricula(dadosSQL.getString("salaAulaBlackboardPessoa.matricula"));
		obj.setUserId(dadosSQL.getString("salaaulablackboardpessoa.userId"));
		obj.setMemberId(dadosSQL.getString("salaaulablackboardpessoa.memberId"));
		obj.setMatriculaPeriodoTurmaDisciplina(dadosSQL.getInt("salaAulaBlackboardPessoa.matriculaperiodoturmadisciplina"));
		obj.setTipoSalaAulaBlackboardPessoaEnum(TipoSalaAulaBlackboardPessoaEnum.valueOf(dadosSQL.getString("salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum")));
		obj.getPessoaEmailInstitucionalVO().setCodigo((dadosSQL.getInt("pessoaEmailInstitucional.codigo")));
		obj.getPessoaEmailInstitucionalVO().setEmail(dadosSQL.getString("pessoaEmailInstitucional.email"));
		obj.getPessoaEmailInstitucionalVO().setNome(dadosSQL.getString("pessoaEmailInstitucional.nome"));
		obj.getPessoaEmailInstitucionalVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum")));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setCPF(dadosSQL.getString("pessoa.cpf"));
		obj.getPessoaEmailInstitucionalVO().getPessoaVO().setRegistroAcademico(dadosSQL.getString("pessoa.registroacademico"));
		if(Uteis.isAtributoPreenchido(obj.getPessoaEmailInstitucionalVO().getPessoaVO())) {
			obj.getPessoaEmailInstitucionalVO().setNome(obj.getPessoaEmailInstitucionalVO().getPessoaVO().getNome());
		}
	}

	private void montarDadosHistoricoNota(SalaAulaBlackboardPessoaVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		montarDados(obj, dadosSQL, nivelMontarDados, usuario);
		obj.getHistoricoNotaBlackboardVO().setCodigo(dadosSQL.getInt("historiconotablackboard.codigo"));
		if (dadosSQL.getObject("historiconotablackboard.nota") == null) {
			obj.getHistoricoNotaBlackboardVO().setNota((Double) dadosSQL.getObject("historiconotablackboard.nota"));
		} else {
			obj.getHistoricoNotaBlackboardVO().setNota(dadosSQL.getDouble("historiconotablackboard.nota"));
		}
		if (dadosSQL.getObject("historiconotablackboard.notaanterior") == null) {
			obj.getHistoricoNotaBlackboardVO().setNotaAnterior((Double) dadosSQL.getObject("historiconotablackboard.notaanterior"));
		} else {
			obj.getHistoricoNotaBlackboardVO().setNotaAnterior(dadosSQL.getDouble("historiconotablackboard.notaanterior"));
		}
		obj.getHistoricoNotaBlackboardVO().setMotivo(dadosSQL.getString("historiconotablackboard.motivo"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("historiconotablackboard.situacaohistoriconotablackboardenum"))) {
			obj.getHistoricoNotaBlackboardVO().setSituacaoHistoricoNotaBlackboardEnum(SituacaoHistoricoNotaBlackboardEnum.valueOf(dadosSQL.getString("historiconotablackboard.situacaohistoriconotablackboardenum")));
		}
		obj.getHistoricoNotaBlackboardVO().getHistoricoVO().setCodigo(dadosSQL.getInt("historico.codigo"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getDouble("historico.mediafinal"))) {
			obj.getHistoricoNotaBlackboardVO().getHistoricoVO().setMediaFinal(dadosSQL.getDouble("historico.mediafinal"));
		}
		obj.getHistoricoNotaBlackboardVO().setSalaAulaBlackboardPessoaVO(obj);
	}

	@Override
	public SalaAulaBlackboardPessoaVO consultarPorSalaAulaBlackboardEmail(SalaAulaBlackboardVO obj, String email,
			int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsulta();
        sqlStr.append(" where salaAulaBlackboardPessoa.salaAulaBlackboard = ? ");
        sqlStr.append(" and pessoaemailinstitucional.email = '").append(email).append("' ");
        sqlStr.append(" limit 1 ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
        SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
        if (!tabelaResultado.next()) {
			return sabp;
		}
        montarDados(sabp, tabelaResultado, nivelMontarDados, usuario);
        return sabp;
	}

	@Override
	public SalaAulaBlackboardPessoaVO consultarPorIdSalaAulaBlackboardEmailTipoPessoa(String idSalaAulaBlackboard, String email, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsulta();
		sqlStr.append(" where salaAulaBlackboard.idSalaAulaBlackboard = ? ");
		sqlStr.append(" and pessoaemailinstitucional.email = ? ");
		sqlStr.append(" and salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum = ? ");
		sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {idSalaAulaBlackboard, email, tipoSalaAulaBlackboardPessoaEnum.name()});
		SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
		if (!tabelaResultado.next()) {
			return sabp;
		}
		montarDados(sabp, tabelaResultado, nivelMontarDados, usuario);
		return sabp;
	}

	@Override
	public SalaAulaBlackboardPessoaVO consultarPorCodigo(SalaAulaBlackboardPessoaVO obj, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsulta();
		sqlStr.append(" where salaAulaBlackboardPessoa.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getCodigo());
		SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
		if (!tabelaResultado.next()) {
			return sabp;
		}
		montarDados(sabp, tabelaResultado, nivelMontarDados, usuario);
		return sabp;
	}

	@Override
	public List<SalaAulaBlackboardPessoaVO> consultarPorIdSalaAulaBlackboardTipoPessoa(String idSalaAulaBlackboard, TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsulta();
		sqlStr.append(" where salaaulablackboard.idSalaAulaBlackboard = ? ");
		sqlStr.append(" and salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum = ? ");
		sqlStr.append(" order by pessoa.nome, pessoaemailinstitucional.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), idSalaAulaBlackboard, tipoSalaAulaBlackboardPessoaEnum.name());
		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
	}
	
	@Override
	public SalaAulaBlackboardPessoaVO consultarPorIdSalaAulaBlackboardMatricula(String idSalaAulaBlackboard, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsulta();
		sqlStr.append(" where salaAulaBlackboard.idSalaAulaBlackboard = ? ");
		sqlStr.append(" and salaAulaBlackboardPessoa.matricula = ? ");
		sqlStr.append(" and salaAulaBlackboardPessoa.tipoSalaAulaBlackboardPessoaEnum = 'ALUNO' ");
		sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] {idSalaAulaBlackboard, matricula});
		SalaAulaBlackboardPessoaVO sabp = new SalaAulaBlackboardPessoaVO();
		if (!tabelaResultado.next()) {
			return sabp;
		}
		montarDados(sabp, tabelaResultado, nivelMontarDados, usuario);
		return sabp;
	}

}
