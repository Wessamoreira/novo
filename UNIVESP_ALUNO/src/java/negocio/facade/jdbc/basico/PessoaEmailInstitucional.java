package negocio.facade.jdbc.basico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.Ldap.SituacaoRetornoLdapEnum;
import negocio.interfaces.basico.PessoaEmailInstitucionalInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class PessoaEmailInstitucional extends ControleAcesso implements PessoaEmailInstitucionalInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5132894999909240541L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(PessoaEmailInstitucionalVO obj, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEmail()), "O campo Email Institucional (Dados Pessoais) deve ser informado");
		Uteis.checkState(!Uteis.getValidaEmail(obj.getEmail()), "O campo Email Institucional (Dados Pessoais) não é um email valido.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getPessoaVO()), "O campo Pessoa (Dados Pessoais) deve ser informado");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final PessoaEmailInstitucionalVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(obj, "pessoaemailinstitucional", new AtributoPersistencia().add("pessoa", obj.getPessoaVO()).add("email", obj.getEmail()).add("nome", obj.getNome()).add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final PessoaEmailInstitucionalVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(obj, "pessoaemailinstitucional", new AtributoPersistencia().add("pessoa", obj.getPessoaVO()).add("email", obj.getEmail()).add("nome", obj.getNome()).add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PessoaVO pessoa, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(pessoa.getListaPessoaEmailInstitucionalVO(), "pessoaemailinstitucional", "pessoa", pessoa.getCodigo(), usuarioVO);	
		persistirListaPessoaEmailInstitucional(pessoa, pessoa.getListaPessoaEmailInstitucionalVO(), verificarAcesso, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirListaPessoaEmailInstitucional(PessoaVO pessoa, List<PessoaEmailInstitucionalVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
	  for (PessoaEmailInstitucionalVO obj : lista) {
			persistirPessoaEmailInstitucional(pessoa, obj, verificarAcesso, usuarioVO);			
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PessoaEmailInstitucionalVO incluirPessoaEmailInstitucional(ConfiguracaoLdapVO conf,PessoaEmailInstitucionalVO pessoaEmailInstitucionalExistente ,  Boolean criarNovoPessoaEmailInstitucional, UsuarioVO usuario, UsuarioVO usuarioLogado) throws Exception {
		PessoaEmailInstitucionalVO emailInstitucionalVO = new PessoaEmailInstitucionalVO(); 
		if(!criarNovoPessoaEmailInstitucional && !Uteis.isAtributoPreenchido(pessoaEmailInstitucionalExistente) ) {			
			if(Uteis.isAtributoPreenchido(conf)) {
				emailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoaPrivilegiandoRegistroAcademicoDominio(usuario.getPessoa().getCodigo(),  conf.getCodigo(), false , Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			}           
			if(!Uteis.isAtributoPreenchido(emailInstitucionalVO)) {
            	 emailInstitucionalVO = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(usuario.getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
            }
		
		}else if(!criarNovoPessoaEmailInstitucional && Uteis.isAtributoPreenchido(pessoaEmailInstitucionalExistente)){
			 emailInstitucionalVO = pessoaEmailInstitucionalExistente  ;
		}
		emailInstitucionalVO.setEmail(this.obterEmail(conf, usuario));
		emailInstitucionalVO.setPessoaVO(usuario.getPessoa());
		emailInstitucionalVO.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
		emailInstitucionalVO.setPessoaEmailInstitucional(true);
		getFacadeFactory().getPessoaEmailInstitucionalFacade().persistirPessoaEmailInstitucional(usuario.getPessoa(), emailInstitucionalVO, false, usuarioLogado);
		usuario.getPessoa().getListaPessoaEmailInstitucionalVO().add(emailInstitucionalVO);
		return emailInstitucionalVO;
	}

	private String obterEmail(ConfiguracaoLdapVO conf, UsuarioVO usuario) {
		return usuario.getUsername().concat(conf.getDominio().startsWith("@") ? conf.getDominio() : "@".concat(conf.getDominio()));
	}
	
	@Override
	public List<PessoaEmailInstitucionalVO> consultarAlunosDoHorarioTurmaDisciplinaDisponivel(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor,boolean isSomentePessoaEmailInstitucional, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorHistorico(unidadeEnsino, curso, turma, disciplina, ano, semestre, codigoProfessor, configuracaoGeralSistemaVO, usuarioVO);
		sqlStr.append(" WHERE 1=1  ");
		if(isSomentePessoaEmailInstitucional) {
			sqlStr.append(" and pessoaEmailInstitucional.pessoa is not null ");	
			sqlStr.append(" and pessoaEmailInstitucional.statusAtivoInativoEnum = '").append(StatusAtivoInativoEnum.ATIVO).append("' ");	
		}
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorHistorico(tabelaResultado);
	}

	@Override
	public List<PessoaEmailInstitucionalVO> consultarAlunosDoEadTurmaDisciplinaDisponivel(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer bimestre, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, boolean isSomentePessoaGsuite, UsuarioVO usuarioVO) throws Exception {
		if(Uteis.isAtributoPreenchido(programacaoTutoriaOnlineVO)) {
			programacaoTutoriaOnlineVO = getFacadeFactory().getProgramacaoTutoriaOnlineInterfaceFacade().consultarPorChavePrimaria(programacaoTutoriaOnlineVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}else {
			programacaoTutoriaOnlineVO.setTurmaVO(turma);
			programacaoTutoriaOnlineVO.setDisciplinaVO(disciplina);
		}
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
		if (isSomentePessoaGsuite) {
			sqlStr.append(" and pessoaemailinstitucional.pessoa is not null ");
			sqlStr.append(" and pessoaemailinstitucional.statusAtivoInativoEnum = '").append(StatusAtivoInativoEnum.ATIVO).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorHistorico(tabelaResultado);
	}
	
	private StringBuilder getSQLPadraoConsultaBasicaPorHistorico(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(unidadeEnsino, usuarioVO);
		boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA(usuarioVO);
		boolean permiteLancarNotaDisciplinaComposta = verificarPermiteLancarNotaDisciplinaComposta(usuarioVO);
		boolean permitirRealizarLancamentoAlunosPreMatriculados = configuracaoGeralSistemaVO.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select  ");
		sqlStr.append(" pessoaemailinstitucional.codigo as \"pessoaemailinstitucional.codigo\", ");
		sqlStr.append(" pessoaemailinstitucional.email as \"pessoaemailinstitucional.email\", ");
		sqlStr.append(" pessoaemailinstitucional.nome as \"pessoaemailinstitucional.nome\", ");
		sqlStr.append(" pessoaemailinstitucional.statusAtivoInativoEnum as \"pessoaemailinstitucional.statusAtivoInativoEnum\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
		sqlStr.append(" pessoa.email2 as \"pessoa.email2\", ");
		sqlStr.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sqlStr.append(" pessoa.aluno as \"pessoa.aluno\", ");
		sqlStr.append(" t.\"matricula.matricula\", ");
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

	private StringBuilder getSQLPadraoConsultaBasicaPorEad(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, Integer bimestre, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select  ");
		sqlStr.append(" pessoaemailinstitucional.codigo as \"pessoaemailinstitucional.codigo\", ");
		sqlStr.append(" pessoaemailinstitucional.email as \"pessoaemailinstitucional.email\", ");
		sqlStr.append(" pessoaemailinstitucional.nome as \"pessoaemailinstitucional.nome\", ");
		sqlStr.append(" pessoaemailinstitucional.statusAtivoInativoEnum as \"pessoaemailinstitucional.statusAtivoInativoEnum\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
		sqlStr.append(" pessoa.email2 as \"pessoa.email2\", ");
		sqlStr.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sqlStr.append(" pessoa.aluno as \"pessoa.aluno\", ");
		sqlStr.append(" t.\"matricula\" as \"matricula.matricula\", ");
		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.nome\", ");
		sqlStr.append(" t.\"salaaulablackboard.codigo\" ");
		sqlStr.append(" from  ( ");
		sqlStr.append(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().executarInicializacaoSqlConsultaMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, bimestre, false, false));
		sqlStr.append(" ) as t ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = t.codigopessoa ");
		sqlStr.append(" left join pessoaemailinstitucional on pessoaemailinstitucional.pessoa = pessoa.codigo  ");
		
		return sqlStr;
	}

	public List<PessoaEmailInstitucionalVO> montarDadosConsultaPorHistorico(SqlRowSet tabelaResultado) throws Exception {
		List<PessoaEmailInstitucionalVO> vetResultado = new ArrayList<PessoaEmailInstitucionalVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosPorHistorico(tabelaResultado));
		}
		return vetResultado;
	}

	private PessoaEmailInstitucionalVO montarDadosPorHistorico(SqlRowSet dadosSQL) {
		PessoaEmailInstitucionalVO obj = new PessoaEmailInstitucionalVO();
		obj.setCodigo((dadosSQL.getInt("pessoaEmailInstitucional.codigo")));
		obj.setEmail(dadosSQL.getString("pessoaEmailInstitucional.email"));
		obj.setEmail(dadosSQL.getString("pessoaEmailInstitucional.nome"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum"))) {
			obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum")));
		}
		obj.getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));

		return obj;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public PessoaEmailInstitucionalVO consultarPorPessoa(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  pessoaEmailInstitucional.pessoa = ").append(pessoa).append(" order by case when pessoaEmailInstitucional.statusAtivoInativoEnum = 'ATIVO' then 0 else 1 end, pessoaEmailInstitucional.codigo limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(!tabelaResultado.next()) {
			return new PessoaEmailInstitucionalVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public PessoaEmailInstitucionalVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  pessoaEmailInstitucional.codigo = ").append(codigo).append(" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(!tabelaResultado.next()) {
			return new PessoaEmailInstitucionalVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<PessoaEmailInstitucionalVO> consultarListaPessoaEmailInstitucionalPorPessoa(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  pessoaEmailInstitucional.pessoa =  ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),pessoa);
		List<PessoaEmailInstitucionalVO> listaEmail = new ArrayList<>();
		while (tabelaResultado.next()) {
			listaEmail.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return listaEmail;
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select  ");
		sql.append(" pessoaEmailInstitucional.codigo as \"pessoaEmailInstitucional.codigo\", ");
		sql.append(" pessoaEmailInstitucional.email as \"pessoaEmailInstitucional.email\", ");
		sql.append(" pessoaEmailInstitucional.nome as \"pessoaEmailInstitucional.nome\", ");
		sql.append(" pessoaEmailInstitucional.statusAtivoInativoEnum as \"pessoaEmailInstitucional.statusAtivoInativoEnum\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sql.append(" pessoa.nome as \"pessoa.nome\", ");
		sql.append(" pessoa.cpf as \"pessoa.cpf\", ");
		sql.append(" pessoa.email as \"pessoa.email\", ");
		sql.append(" pessoa.email2 as \"pessoa.email2\", ");
		sql.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sql.append(" pessoa.aluno as \"pessoa.aluno\" ");
		sql.append(" from pessoaEmailInstitucional ");
		sql.append(" left join pessoa on pessoa.codigo = pessoaEmailInstitucional.pessoa ");
		return sql;
	}
	
	private PessoaEmailInstitucionalVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		PessoaEmailInstitucionalVO obj = new PessoaEmailInstitucionalVO();
		obj.setCodigo((dadosSQL.getInt("pessoaEmailInstitucional.codigo")));
		obj.setEmail(dadosSQL.getString("pessoaEmailInstitucional.email"));
		obj.setNome(dadosSQL.getString("pessoaEmailInstitucional.nome"));
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoaEmailInstitucional.statusAtivoInativoEnum")));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			return obj;
		}
		obj.getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getPessoaVO().setCPF((dadosSQL.getString("pessoa.cpf")));
		obj.getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
		if(obj.getNome().isEmpty()) {
			obj.setNome(obj.getPessoaVO().getNome());
		}
		return obj;
	}

	@Override
	public List<PessoaEmailInstitucionalVO> consultar(String campoConsulta, String valorConsulta, int nivelMontarDados,
			UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		List<String> listaParametrosConsulta = new ArrayList<String>(0);
		sqlStr.append(" WHERE 1 = 1 ");
		if (campoConsulta.equals("nome")) {			
			sqlStr.append("AND (upper(sem_acentos(pessoaemailinstitucional.nome)) like upper(sem_acentos(?)) ");
			sqlStr.append("OR upper(sem_acentos(pessoa.nome)) like upper(sem_acentos(?))) ");
			listaParametrosConsulta.add(valorConsulta.toLowerCase() + "%");
			listaParametrosConsulta.add(valorConsulta.toLowerCase() + "%");
		} else if (campoConsulta.equals("email")) {
            sqlStr.append("AND upper(sem_acentos(pessoaemailinstitucional.email)) like upper(sem_acentos(?)) ");
            listaParametrosConsulta.add(valorConsulta.toLowerCase() + "%");
		}
		sqlStr.append(" order by pessoa.nome, pessoaemailinstitucional.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaParametrosConsulta.toArray());
		List<PessoaEmailInstitucionalVO> vetResultado = new ArrayList<PessoaEmailInstitucionalVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	@Override
	public PessoaEmailInstitucionalVO consultarPorEmail(String email, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE pessoaemailinstitucional.email = '").append(email).append("' ");
		sqlStr.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new PessoaEmailInstitucionalVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<PessoaEmailInstitucionalVO> consultarPorPessoaFuncionario(Integer pessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  pessoaEmailInstitucional.pessoa = ").append(pessoa);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorPessoa(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List<PessoaEmailInstitucionalVO> consultarPorPessoaFuncionarioteste(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  pessoa.cpf != '' and cpf not ilike('%t%') and cpf not ilike('111.111.111-11') and cpf ilike('%.%') ");
		sqlStr.append(" limit 6 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorPessoa(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List<PessoaEmailInstitucionalVO> montarDadosConsultaPorPessoa(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<PessoaEmailInstitucionalVO> vetResultado = new ArrayList<PessoaEmailInstitucionalVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoStatusAtivoInativoEnum(PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE pessoaemailinstitucional SET statusativoinativoenum = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setString(1, pessoaEmailInstitucionalVO.getStatusAtivoInativoEnum().name());
				sqlAlterar.setInt(2, pessoaEmailInstitucionalVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public PessoaEmailInstitucionalVO consultarPorMatricula(String matricula, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
		sqlStr.append(" inner join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" inner join configuracaoldap on configuracaoldap.codigo = curso.configuracaoldap and pessoaEmailInstitucional.email ilike ('%'||configuracaoldap.dominio)  ");
		sqlStr.append(" WHERE  matricula.matricula = ? order by case when pessoaEmailInstitucional.statusAtivoInativoEnum = 'ATIVO' then 0 else 1 end, pessoaEmailInstitucional.codigo limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if(!tabelaResultado.next()) {
			return new PessoaEmailInstitucionalVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public PessoaEmailInstitucionalVO consultarPorPessoaPrivilegiandoRegistroAcademicoDominio(Integer pessoa , Integer configuracaoLdap, Boolean validarEmailInstitucionalAtivo, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" inner join configuracaoldap on configuracaoldap.codigo = ? and pessoaEmailInstitucional.email ilike ('%'||configuracaoldap.dominio)  ");
		sqlStr.append(" WHERE  pessoa.codigo = ?");
		if(validarEmailInstitucionalAtivo) {			
			sqlStr.append(" and pessoaEmailInstitucional.statusAtivoInativoEnum = 'ATIVO' ");
		}
		sqlStr.append(" order by case when pessoaEmailInstitucional.email ilike ('%'||pessoa.registroAcademico)  then 0 else 1 end, pessoaEmailInstitucional.codigo limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), configuracaoLdap,pessoa);
		if(!tabelaResultado.next()) {
			return new PessoaEmailInstitucionalVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	public void preencherPessoaEmailInstitucional(PessoaVO pessoa, UsuarioVO usuarioVO) throws Exception {
		List<PessoaEmailInstitucionalVO> listaPessoaEmailInstitucionalVOs = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoaFuncionario(pessoa.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		listaPessoaEmailInstitucionalVOs.stream().forEach(p -> {
			if(pessoa.getListaPessoaEmailInstitucionalVO().stream().noneMatch(p2 -> p2.getCodigo().equals(p.getCodigo()))) {
				pessoa.getListaPessoaEmailInstitucionalVO().add(p);
			}
		});
	}

	@Override
	public void persistirPessoaEmailInstitucional(PessoaVO pessoa, PessoaEmailInstitucionalVO obj,	boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(obj, usuarioVO);
		if(Uteis.isAtributoPreenchido(pessoa)) {
			obj.setNome(pessoa.getNome());
		}
		if (!Uteis.isAtributoPreenchido(obj)) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		if(obj.isExecutarStatusAtivoInativoEnum()) {
			UsuarioVO usuarioExistente = getFacadeFactory().getUsuarioFacade().validarUsuarioExistente(obj.getPessoaVO(), null, usuarioVO);		
			if(Uteis.isAtributoPreenchido(usuarioExistente)) {				
				ConfiguracaoLdapVO conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoaEmailInstitucional(obj.getCodigo());
			    String uidConsultar =  Uteis.isAtributoPreenchido(usuarioExistente.getPessoa().getRegistroAcademico()) ? usuarioExistente.getPessoa().getRegistroAcademico() :usuarioExistente.getUsername();
				SituacaoRetornoLdapEnum  retorno =  getFacadeFactory().getLdapFacade().verificarExistenciaContaLdapPorRegistroAcademicoEmailInstitucional(conf, usuarioExistente, obj.getEmail(),uidConsultar); 			
				if(obj.getStatusAtivoInativoEnum().isAtivo() && (retorno != null && retorno.isInativo())){
					getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(conf, usuarioExistente, null, pessoa, true,null, obj,usuarioVO);
				}else if(obj.getStatusAtivoInativoEnum().isInativo()  && (retorno != null && retorno.isAtivo())) {
					getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoInativarContaPorPessoaEmailInstitucional(conf, usuarioExistente, null, pessoa, false,null, obj,usuarioVO);
				}				
			}					
		}	
	}
	
}
