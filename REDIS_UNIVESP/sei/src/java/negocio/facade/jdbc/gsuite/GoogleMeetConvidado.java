package negocio.facade.jdbc.gsuite;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.gsuite.GoogleMeetConvidadoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class GoogleMeetConvidado extends ControleAcesso implements GoogleMeetConvidadoInterfaceFacade {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -156148963858178247L;	
	
//	@Override
//	public List<GoogleMeetConvidadoVO> consultarGoogleMeetConvidado(GoogleMeetVO googleMeet, Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
//		StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorHistorico(unidadeEnsino, curso, turma, disciplina, ano, semestre, codigoProfessor, configuracaoGeralSistemaVO, usuarioVO);
//		sqlStr.append(" and  (googlemeetconvidado.googlemeet = ? or googlemeetconvidado.googlemeet is null) ");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), googleMeet.getCodigo());
//		List<GoogleMeetConvidadoVO> vetResultado = new ArrayList<GoogleMeetConvidadoVO>(0);
//		while (tabelaResultado.next()) {
//			GoogleMeetConvidadoVO obj = montarDadosPorHistorico(tabelaResultado);
//			obj.setGoogleMeetVO(googleMeet);
//			vetResultado.add(obj);
//		}
//		return vetResultado;
//		
//	}
//	
//	@Override
//	public List<GoogleMeetConvidadoVO> consultarAlunosDoHorarioTurmaDisciplinaDisponivelGsuite(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
//		StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorHistorico(unidadeEnsino, curso, turma, disciplina, ano, semestre, codigoProfessor, configuracaoGeralSistemaVO, usuarioVO);
//		sqlStr.append(" and  googlemeetconvidado.googlemeet is null  ");
//		sqlStr.append(" WHERE pessoagsuite.pessoa is not null ");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
//		return montarDadosConsultaPorHistorico(tabelaResultado);
//	}
//	
//	private StringBuilder getSQLPadraoConsultaBasicaPorHistorico(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
//		boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(unidadeEnsino, usuarioVO);
//		boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA(usuarioVO);
//		boolean permiteLancarNotaDisciplinaComposta = verificarPermiteLancarNotaDisciplinaComposta(usuarioVO);
//		boolean permitirRealizarLancamentoAlunosPreMatriculados = configuracaoGeralSistemaVO.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
//		StringBuilder sqlStr = new StringBuilder(" ");
//		sqlStr.append(" select  ");
//		sqlStr.append(" googlemeetconvidado.codigo as \"googlemeetconvidado.codigo\", ");
//		sqlStr.append(" pessoagsuite.codigo as \"pessoagsuite.codigo\", ");
//		sqlStr.append(" pessoagsuite.email as \"pessoagsuite.email\", ");
//		sqlStr.append(" pessoagsuite.statusAtivoInativoEnum as \"pessoagsuite.statusAtivoInativoEnum\", ");
//		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
//		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
//		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
//		sqlStr.append(" pessoa.email2 as \"pessoa.email2\", ");
//		sqlStr.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
//		sqlStr.append(" pessoa.aluno as \"pessoa.aluno\", ");
//		sqlStr.append(" t.\"matricula.matricula\", ");
//		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
//		sqlStr.append(" t.\"unidadeEnsino.nome\" ");
//		sqlStr.append(" from  ( ");
//		sqlStr.append(getFacadeFactory().getHistoricoFacade().getSqlCompletoConsultaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(unidadeEnsino, curso, disciplina, turma, ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, codigoProfessor, false, null, permitiVisualizarAlunoTR_CA, permiteLancarNotaDisciplinaComposta, Uteis.NIVELMONTARDADOS_TODOS, null, null, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados));
//		sqlStr.append(" ) as t ");
//		sqlStr.append(" inner join pessoa on pessoa.codigo = t.\"aluno.codigo\" ");
//		sqlStr.append(" left join pessoagsuite on pessoagsuite.pessoa = pessoa.codigo  and  pessoagsuite.unidadeensino = t.\"unidadeEnsino.codigo\" ");
//		sqlStr.append(" left join googlemeetconvidado on googlemeetconvidado.pessoagsuite = pessoagsuite.codigo ");
//		return sqlStr;
//	}
//	
//	public List<GoogleMeetConvidadoVO> montarDadosConsultaPorHistorico(SqlRowSet tabelaResultado) throws Exception {
//		List<GoogleMeetConvidadoVO> vetResultado = new ArrayList<GoogleMeetConvidadoVO>(0);
//		while (tabelaResultado.next()) {
//			vetResultado.add(montarDadosPorHistorico(tabelaResultado));
//		}
//		return vetResultado;
//	}
//
//	private GoogleMeetConvidadoVO montarDadosPorHistorico(SqlRowSet dadosSQL)  {
//		GoogleMeetConvidadoVO obj = new GoogleMeetConvidadoVO();
//		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("googlemeetconvidado.codigo"))) {
//			obj.setNovoObj(false);
//			obj.setCodigo((dadosSQL.getInt("googlemeetconvidado.codigo")));	
//		}
//		obj.getPessoaGsuiteVO().setCodigo((dadosSQL.getInt("pessoagsuite.codigo")));
//		obj.getPessoaGsuiteVO().setEmail(dadosSQL.getString("pessoagsuite.email"));
//		if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoagsuite.statusAtivoInativoEnum"))) {
//			obj.getPessoaGsuiteVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoagsuite.statusAtivoInativoEnum")));	
//		}
//		obj.getPessoaGsuiteVO().getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
//		obj.getPessoaGsuiteVO().getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
//		obj.getPessoaGsuiteVO().getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
//		obj.getPessoaGsuiteVO().getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
//		obj.getPessoaGsuiteVO().getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
//		obj.getPessoaGsuiteVO().getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
//		obj.getPessoaGsuiteVO().getUnidadeEnsinoVO().setCodigo((dadosSQL.getInt("unidadeEnsino.codigo")));
//		obj.getPessoaGsuiteVO().getUnidadeEnsinoVO().setNome((dadosSQL.getString("unidadeEnsino.nome")));
//		obj.setMatricula(dadosSQL.getString("matricula.matricula"));
//		
//		
//		
//		return obj;
//	}
//	
//	@Override
//	public void consultarPorGoogleMeet(GoogleMeetVO googleMeet, Integer nivelMontarDados)  {
//		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
//		sqlStr.append(" WHERE googlemeetconvidado.googleMeet = ? ");
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), googleMeet.getCodigo());
//		googleMeet.getGoogleMeetConvidadoVOs().clear();
//		while (tabelaResultado.next()) {
//			GoogleMeetConvidadoVO obj = montarDados(tabelaResultado, nivelMontarDados);
//			obj.setGoogleMeetVO(googleMeet);
//			googleMeet.getGoogleMeetConvidadoVOs().add(obj);
//		}
//	}
//	
//
//	private StringBuilder getSQLPadraoConsultaBasica() {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select googlemeetconvidado.codigo, googlemeetconvidado.googleMeet, ");
//		sql.append(" pessoagsuite.codigo as \"pessoagsuite.codigo\", ");
//		sql.append(" pessoagsuite.email as \"pessoagsuite.email\", ");
//		sql.append(" pessoagsuite.statusAtivoInativoEnum as \"pessoagsuite.statusAtivoInativoEnum\", ");
//		sql.append(" pessoa.codigo as \"pessoa.codigo\", ");
//		sql.append(" pessoa.nome as \"pessoa.nome\", ");
//		sql.append(" pessoa.email as \"pessoa.email\", ");
//		sql.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
//		sql.append(" pessoa.aluno as \"pessoa.aluno\", ");
//		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", ");
//		sql.append(" unidadeensino.nome as \"unidadeensino.nome\" ");
//		sql.append(" from googlemeetconvidado ");
//		sql.append(" inner join pessoagsuite  on pessoagsuite.codigo = googlemeetconvidado.pessoagsuite ");
//		sql.append(" inner join pessoa on pessoa.codigo = pessoagsuite.pessoa ");
//		sql.append(" inner join unidadeensino on unidadeensino.codigo  = pessoagsuite.unidadeensino ");
//		return sql;
//	}
//
//	public List<GoogleMeetConvidadoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) {
//		List<GoogleMeetConvidadoVO> vetResultado = new ArrayList<GoogleMeetConvidadoVO>(0);
//		while (tabelaResultado.next()) {
//			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
//		}
//		return vetResultado;
//	}
//
//	private GoogleMeetConvidadoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) {
//		GoogleMeetConvidadoVO obj = new GoogleMeetConvidadoVO();
//		obj.setNovoObj(false);
//		obj.setCodigo(dadosSQL.getInt("codigo"));
//		obj.getGoogleMeetVO().setCodigo(dadosSQL.getInt("googleMeet"));
//		obj.getPessoaGsuiteVO().setCodigo((dadosSQL.getInt("pessoagsuite.codigo")));
//		obj.getPessoaGsuiteVO().setEmail(dadosSQL.getString("pessoagsuite.email"));
//		obj.getPessoaGsuiteVO().setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoagsuite.statusAtivoInativoEnum")));
//		obj.getPessoaGsuiteVO().getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
//		obj.getPessoaGsuiteVO().getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
//		obj.getPessoaGsuiteVO().getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
//		obj.getPessoaGsuiteVO().getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
//		obj.getPessoaGsuiteVO().getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
//		obj.getPessoaGsuiteVO().getUnidadeEnsinoVO().setCodigo((dadosSQL.getInt("unidadeensino.codigo")));
//		obj.getPessoaGsuiteVO().getUnidadeEnsinoVO().setNome((dadosSQL.getString("unidadeensino.nome")));
//		return obj;
//	}
//	
	
	
	

}
