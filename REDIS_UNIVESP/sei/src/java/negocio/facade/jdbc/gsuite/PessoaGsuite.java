package negocio.facade.jdbc.gsuite;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoSeiGsuiteVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.gsuite.PessoaGsuiteInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy
public class PessoaGsuite  extends ControleAcesso implements PessoaGsuiteInterfaceFacade{

	/**
	 * 
	 */
	private static final long serialVersionUID = -180999614282071730L;
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(PessoaGsuiteVO obj, UsuarioVO usuario) {		
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getEmail()), "O campo Email (Pessoa Gsuite) deve ser informado");
		Uteis.checkState(!Uteis.getValidaEmail(obj.getEmail()), "O campo Email (Pessoa Gsuite) não é um email valido.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getPessoaVO()), "O campo Pessoa (Pessoa Gsuite) deve ser informado");
		getFacadeFactory().getAdminSdkIntegracaoFacade().realizarVerificacaoPessoaGsuiteVOValida(obj, usuario);
	}	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final PessoaGsuiteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			incluir(obj, "PessoaGsuite", new AtributoPersistencia()
					.add("pessoa", obj.getPessoaVO())					
					.add("email", obj.getEmail())
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum()),
					usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final PessoaGsuiteVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			alterar(obj, "PessoaGsuite", new AtributoPersistencia()
					.add("pessoa", obj.getPessoaVO())					
					.add("email", obj.getEmail())
					.add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum()),
					new AtributoPersistencia()
					.add("codigo", obj.getCodigo()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PessoaVO pessoa, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarSeRegistroForamExcluidoDasListaSubordinadas(pessoa.getListaPessoaGsuite(), "pessoaGsuite", "pessoa", pessoa.getCodigo(), usuarioVO);
		persistir(pessoa.getListaPessoaGsuite(), verificarAcesso, usuarioVO);	
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<PessoaGsuiteVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		ConfiguracaoSeiGsuiteVO configuracaoSeiGsuiteVO = getFacadeFactory().getConfiguracaoSeiGsuiteFacade().consultarConfiguracaoSeiGsuitePadrao(Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(configuracaoSeiGsuiteVO)) {
		for (PessoaGsuiteVO obj : lista) {
			validarDados(obj, usuarioVO);
			if (!Uteis.isAtributoPreenchido(obj)) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarStatusPessoaGSuite(PessoaGsuiteVO obj, UsuarioVO usuarioLogado)  {		
		alterar(obj, "PessoaGsuite", 
				new AtributoPersistencia().add("statusAtivoInativoEnum", obj.getStatusAtivoInativoEnum()),
				new AtributoPersistencia().add("codigo", obj.getCodigo()),
				usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void excluir(PessoaGsuiteVO obj, UsuarioVO usuarioLogado) throws Exception {		
		excluir("PessoaGsuite", new AtributoPersistencia().add("codigo", obj.getCodigo()), usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void alterarAtualizacaoPessoaGsuiteImportada(PessoaGsuiteVO obj, UsuarioVO usuarioLogado)  {		
		alterar(obj, false, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarPessoaGsuiteImportada(DataModelo dataModelo, PessoaGsuiteVO obj) {
		List<PessoaGsuiteVO> objs = consultaPessoaGsuiteImportadasPorFiltros(obj, dataModelo);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarPessoaGsuiteTotalPorFiltros(obj, dataModelo));
		dataModelo.setListaConsulta(objs);
	}
	
	private List<PessoaGsuiteVO> consultaPessoaGsuiteImportadasPorFiltros(PessoaGsuiteVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaPessoaGsuiteImportada();
			sqlStr.append(" WHERE pessoagsuite.pessoa is null ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY pessoagsuite.email ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private Integer consultarPessoaGsuiteTotalPorFiltros(PessoaGsuiteVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" SELECT count(pessoagsuite.codigo) as QTDE FROM pessoagsuite  ");
			sqlStr.append(" WHERE pessoagsuite.pessoa is null ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, PessoaGsuiteVO obj) {
		List<PessoaGsuiteVO> objs = consultaRapidaPorFiltros(obj, dataModelo);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo));
		dataModelo.setListaConsulta(objs);
	}

	private List<PessoaGsuiteVO> consultaRapidaPorFiltros(PessoaGsuiteVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1 = 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			sqlStr.append(" ORDER BY pessoa.nome ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private Integer consultarTotalPorFiltros(PessoaGsuiteVO obj, DataModelo dataModelo) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(PessoaGsuiteVO obj, DataModelo dataModelo, StringBuilder sqlStr) {
		
		if(Uteis.isAtributoPreenchido(obj.getPessoaVO().getNome())) {			
			sqlStr.append(" and sem_acentos(pessoa.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(PERCENT+obj.getPessoaVO().getNome()+PERCENT);
		}
		if(Uteis.isAtributoPreenchido(obj.getEmail())) {
			sqlStr.append(" and sem_acentos(pessoagsuite.email) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(PERCENT + obj.getEmail() +PERCENT);
		}
		if(Uteis.isAtributoPreenchido(obj.getStatusAtivoInativoEnum())) {
			sqlStr.append(" and pessoagsuite.statusAtivoInativoEnum = ? ");
			dataModelo.getListaFiltros().add(obj.getStatusAtivoInativoEnum().name());
		}
	}

	private StringBuilder getSQLPadraoConsultaPessoaGsuiteImportada() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select  ");
		sql.append(" pessoagsuite.codigo as \"pessoagsuite.codigo\", ");
		sql.append(" pessoagsuite.email as \"pessoagsuite.email\", ");
		sql.append(" pessoagsuite.statusAtivoInativoEnum as \"pessoagsuite.statusAtivoInativoEnum\" ");
		sql.append(" from pessoagsuite ");
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" select  ");
		sql.append(" pessoagsuite.codigo as \"pessoagsuite.codigo\", ");
		sql.append(" pessoagsuite.email as \"pessoagsuite.email\", ");
		sql.append(" pessoagsuite.statusAtivoInativoEnum as \"pessoagsuite.statusAtivoInativoEnum\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sql.append(" pessoa.nome as \"pessoa.nome\", ");
		sql.append(" pessoa.email as \"pessoa.email\", ");
		sql.append(" pessoa.email2 as \"pessoa.email2\", ");
		sql.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sql.append(" pessoa.aluno as \"pessoa.aluno\" ");
		sql.append(" from pessoagsuite ");
		sql.append(" inner join pessoa on pessoa.codigo = pessoagsuite.pessoa ");
		
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(pessoagsuite.codigo) as QTDE FROM pessoagsuite  ");
		sql.append(" inner join pessoa on pessoa.codigo = pessoagsuite.pessoa ");
		return sql;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public PessoaGsuiteVO consultarPorPessoaPorUnidadeEnsino(Integer pessoa, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" inner join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.unidadeensino = ").append(unidadeEnsino);
		sqlStr.append(" and  (( pessoa.aluno and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemail ||'%' )  ");
		sqlStr.append("  or ( pessoa.funcionario and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemailfuncionario ||'%' ))   ");
		sqlStr.append(" WHERE  pessoagsuite.pessoa = ").append(pessoa);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(!tabelaResultado.next()) {
			return new PessoaGsuiteVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public PessoaGsuiteVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE pessoagsuite.codigo = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigo);
		if (!tabelaResultado.next()) {
			return new PessoaGsuiteVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean validarUnicidadePorPessoaPorUnidadeEnsino(Integer pessoa, Integer unidadeEnsino) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count (pessoagsuite.codigo) as QTDE from pessoagsuite ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = pessoagsuite.pessoa ");
		sqlStr.append(" inner join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.unidadeensino =  ").append(unidadeEnsino);
		sqlStr.append(" and  (( pessoa.aluno and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemail ||'%' )  ");
		sqlStr.append("  or ( pessoa.funcionario and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemailfuncionario ||'%' ))   ");
		sqlStr.append(" WHERE pessoagsuite.pessoa = ").append(pessoa);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public boolean validarUnicidadePorEmail(String email) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count (pessoagsuite.codigo) as QTDE from pessoagsuite ");
		sqlStr.append(" WHERE email = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), email);
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	private List<PessoaGsuiteVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<PessoaGsuiteVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	private PessoaGsuiteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		PessoaGsuiteVO obj = new PessoaGsuiteVO();
		obj.setCodigo((dadosSQL.getInt("pessoagsuite.codigo")));
		obj.setEmail(dadosSQL.getString("pessoagsuite.email"));
		obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoagsuite.statusAtivoInativoEnum")));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			return obj;
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
	public List<PessoaGsuiteVO> consultarAlunosDoEadTurmaDisciplinaDisponivelGsuite(TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, Integer codigoProfessor,boolean isSomentePessoaGsuite, UsuarioVO usuarioVO) throws Exception {
		ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO = new ProgramacaoTutoriaOnlineVO();
		programacaoTutoriaOnlineVO.setTurmaVO(turma);
		programacaoTutoriaOnlineVO.setDisciplinaVO(disciplina);
		programacaoTutoriaOnlineVO.setAno(ano);
		programacaoTutoriaOnlineVO.setSemestre(semestre);		
		ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = new ProgramacaoTutoriaOnlineProfessorVO();
		programacaoTutoriaOnlineProfessorVO.getProfessor().setCodigo(codigoProfessor);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorEad(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, usuarioVO);
		sqlStr.append(" WHERE 1=1  ");
		if(Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and t.ano = '").append(ano).append("'");	
		}
		if(Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and t.semestre = '").append(semestre).append("'");
		}
		if(isSomentePessoaGsuite) {
			sqlStr.append(" and pessoagsuite.pessoa is not null ");	
			sqlStr.append(" and pessoagsuite.statusAtivoInativoEnum = '").append(StatusAtivoInativoEnum.ATIVO).append("' ");	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorHistorico(tabelaResultado);
	}
	
	@Override
	public List<PessoaGsuiteVO> consultarAlunosDoHorarioTurmaDisciplinaDisponivelGsuite(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor,boolean isSomentePessoaGsuite, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorHistorico(unidadeEnsino, curso, turma, disciplina, ano, semestre, codigoProfessor, configuracaoGeralSistemaVO, usuarioVO);
		sqlStr.append(" WHERE 1=1  ");
		if(isSomentePessoaGsuite) {
			sqlStr.append(" and pessoagsuite.pessoa is not null ");	
			sqlStr.append(" and pessoagsuite.statusAtivoInativoEnum = '").append(StatusAtivoInativoEnum.ATIVO).append("' ");	
		}
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaPorHistorico(tabelaResultado);
	}
	
	
	
	private StringBuilder getSQLPadraoConsultaBasicaPorEad(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO,  ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, UsuarioVO usuarioVO) throws Exception {
		
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select  ");
		sqlStr.append(" pessoagsuite.codigo as \"pessoagsuite.codigo\", ");
		sqlStr.append(" pessoagsuite.email as \"pessoagsuite.email\", ");
		sqlStr.append(" pessoagsuite.statusAtivoInativoEnum as \"pessoagsuite.statusAtivoInativoEnum\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
		sqlStr.append(" pessoa.email2 as \"pessoa.email2\", ");
		sqlStr.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sqlStr.append(" pessoa.aluno as \"pessoa.aluno\", ");
		sqlStr.append(" t.\"matricula\" as \"matricula.matricula\", ");
		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.nome\" ");		
		sqlStr.append(" from  ( ");
		sqlStr.append(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().executarInicializacaoSqlConsultaMatriculaPeriodoTurmaDisciplinaPorProgramacaoTutoriaOnline(programacaoTutoriaOnlineVO, programacaoTutoriaOnlineProfessorVO, 0, false, false));
		sqlStr.append(" ) as t ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = t.codigopessoa ");
		sqlStr.append(" left join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.unidadeensino = t.\"unidadeEnsino.codigo\" ");
		sqlStr.append(" left join pessoagsuite on pessoagsuite.pessoa = pessoa.codigo  ");
		sqlStr.append(" and  (( pessoa.aluno and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemail ||'%' )  ");
		sqlStr.append("  or ( pessoa.funcionario and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemailfuncionario ||'%' ))   ");
		return sqlStr;
	}
	
	private StringBuilder getSQLPadraoConsultaBasicaPorHistorico(Integer unidadeEnsino, Integer curso, TurmaVO turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
		boolean trazerAlunoPendenteFinanceiramente = getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(unidadeEnsino, usuarioVO);
		boolean permitiVisualizarAlunoTR_CA = verificarPermissaoVisualizarAlunoTR_CA(usuarioVO);
		boolean permiteLancarNotaDisciplinaComposta = verificarPermiteLancarNotaDisciplinaComposta(usuarioVO);
		boolean permitirRealizarLancamentoAlunosPreMatriculados = configuracaoGeralSistemaVO.getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
		StringBuilder sqlStr = new StringBuilder(" ");
		sqlStr.append(" select  ");
		sqlStr.append(" pessoagsuite.codigo as \"pessoagsuite.codigo\", ");
		sqlStr.append(" pessoagsuite.email as \"pessoagsuite.email\", ");
		sqlStr.append(" pessoagsuite.statusAtivoInativoEnum as \"pessoagsuite.statusAtivoInativoEnum\", ");
		sqlStr.append(" pessoa.codigo as \"pessoa.codigo\", ");
		sqlStr.append(" pessoa.nome as \"pessoa.nome\", ");
		sqlStr.append(" pessoa.email as \"pessoa.email\", ");
		sqlStr.append(" pessoa.email2 as \"pessoa.email2\", ");
		sqlStr.append(" pessoa.funcionario as \"pessoa.funcionario\", ");
		sqlStr.append(" pessoa.aluno as \"pessoa.aluno\", ");
		sqlStr.append(" t.\"matricula.matricula\", ");
		sqlStr.append(" t.\"unidadeEnsino.codigo\", ");
		sqlStr.append(" t.\"unidadeEnsino.nome\" ");
		sqlStr.append(" from  ( ");
		sqlStr.append(getFacadeFactory().getHistoricoFacade().getSqlCompletoConsultaPorUnidadeEnsinoCursoDisciplinaTurmaAnoSemestreSituacaoMatSituacaoHistFiltroVisaoProfessorRegistroNotaLimiteOffSet(unidadeEnsino, curso, disciplina, turma, ano, semestre, trazerAlunoPendenteFinanceiramente, "'AA', 'CC', 'CH', 'IS'", false, true, codigoProfessor, false, null, permitiVisualizarAlunoTR_CA, permiteLancarNotaDisciplinaComposta, Uteis.NIVELMONTARDADOS_TODOS, null, null, usuarioVO, permitirRealizarLancamentoAlunosPreMatriculados));
		sqlStr.append(" ) as t ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = t.\"aluno.codigo\" ");
		sqlStr.append(" left join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.unidadeensino = t.\"unidadeEnsino.codigo\" ");
		sqlStr.append(" left join pessoagsuite on pessoagsuite.pessoa = pessoa.codigo  ");
		sqlStr.append(" and  (( pessoa.aluno and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemail ||'%' )  ");
		sqlStr.append("  or ( pessoa.funcionario and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemailfuncionario ||'%' ))   ");
		return sqlStr;
	}
	
	public List<PessoaGsuiteVO> montarDadosConsultaPorHistorico(SqlRowSet tabelaResultado) throws Exception {
		List<PessoaGsuiteVO> vetResultado = new ArrayList<PessoaGsuiteVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosPorHistorico(tabelaResultado));
		}
		return vetResultado;
	}

	private PessoaGsuiteVO montarDadosPorHistorico(SqlRowSet dadosSQL)  {
		PessoaGsuiteVO obj = new PessoaGsuiteVO();
		obj.setCodigo((dadosSQL.getInt("pessoagsuite.codigo")));
		obj.setEmail(dadosSQL.getString("pessoagsuite.email"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("pessoagsuite.statusAtivoInativoEnum"))) {
			obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("pessoagsuite.statusAtivoInativoEnum")));	
		}
		obj.getPessoaVO().setCodigo((dadosSQL.getInt("pessoa.codigo")));
		obj.getPessoaVO().setNome((dadosSQL.getString("pessoa.nome")));
		obj.getPessoaVO().setEmail(dadosSQL.getString("pessoa.email"));
		obj.getPessoaVO().setEmail2(dadosSQL.getString("pessoa.email2"));
		obj.getPessoaVO().setFuncionario(dadosSQL.getBoolean("pessoa.funcionario"));
		obj.getPessoaVO().setAluno(dadosSQL.getBoolean("pessoa.aluno"));
		obj.setMatricula(dadosSQL.getString("matricula.matricula"));
		
		return obj;
	}

}
