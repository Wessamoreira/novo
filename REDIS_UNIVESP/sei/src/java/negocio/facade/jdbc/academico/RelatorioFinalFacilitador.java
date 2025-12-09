package negocio.facade.jdbc.academico;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CalendarioRelatorioFinalFacilitadorVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.PerguntaRespostaOrigemVO;
import negocio.comuns.academico.RelatorioFinalFacilitadorVO;
import negocio.comuns.academico.enumeradores.SituacaoRelatorioFacilitadorEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ModalidadeBolsaEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisExcel;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RelatorioFinalFacilitadorInterfaceFacade;

@Repository
@Lazy
public class RelatorioFinalFacilitador extends ControleAcesso implements RelatorioFinalFacilitadorInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7006155065140841092L;
	protected static String idEntidade;
	private static final String nomeTabela = "RelatorioFinalFacilitador";	


	public RelatorioFinalFacilitador() throws Exception {
		super();
		setIdEntidade("RelatorioFinalFacilitador");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		if (relatorioFinalFacilitadorVO.getNovoObj()) {
			incluir(relatorioFinalFacilitadorVO, usuarioVO);
		} else {
			alterar(relatorioFinalFacilitadorVO, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, Boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdEntidade(), validarAcesso, usuarioVO);
		getConexao().getJdbcTemplate().update("DELETE FROM CalendarioRelatorioFinalFacilitador where codigo = " + relatorioFinalFacilitadorVO.getCodigo()+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		try {
			if(Uteis.isAtributoPreenchido(relatorioFinalFacilitadorVO.getQuestionarioRespostaOrigemVO().getQuestionarioVO())) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().incluir(relatorioFinalFacilitadorVO.getQuestionarioRespostaOrigemVO(), usuarioVO);
			}
		incluir(relatorioFinalFacilitadorVO, nomeTabela, new AtributoPersistencia()
				.add("matriculaperiodoturmadisciplina", relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO())
				.add("ano", relatorioFinalFacilitadorVO.getAno())
				.add("semestre", relatorioFinalFacilitadorVO.getSemestre())
				.add("mes", relatorioFinalFacilitadorVO.getMes())
				.add("questionarioRespostaOrigem", relatorioFinalFacilitadorVO.getQuestionarioRespostaOrigemVO())
				.add("situacao", relatorioFinalFacilitadorVO.getSituacao())
				.add("dataEnvioAnalise", relatorioFinalFacilitadorVO.getDataEnvioAnalise())
				.add("dataEnvioCorrecao", relatorioFinalFacilitadorVO.getDataEnvioCorrecao())
				.add("dataDeferimento", relatorioFinalFacilitadorVO.getDataDeferimento())
				.add("dataIndeferimento", relatorioFinalFacilitadorVO.getDataIndeferimento())
				.add("nota", relatorioFinalFacilitadorVO.getNota())
				.add("supervisor", relatorioFinalFacilitadorVO.getSupervisor().getCodigo())
				, usuarioVO);
		} catch (Exception e) {
			UtilReflexao.invocarMetodoSetParametroNull(relatorioFinalFacilitadorVO, "codigo");
			relatorioFinalFacilitadorVO.setNovoObj(true);
			throw e;
		}

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		try {
		alterar(relatorioFinalFacilitadorVO, nomeTabela, new AtributoPersistencia()
				.add("matriculaperiodoturmadisciplina", relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO())
				.add("ano", relatorioFinalFacilitadorVO.getAno())
				.add("semestre", relatorioFinalFacilitadorVO.getSemestre())
				.add("mes", relatorioFinalFacilitadorVO.getMes())
				.add("questionarioRespostaOrigem", relatorioFinalFacilitadorVO.getQuestionarioRespostaOrigemVO())
				.add("situacao", relatorioFinalFacilitadorVO.getSituacao())
				.add("dataEnvioAnalise", relatorioFinalFacilitadorVO.getDataEnvioAnalise())
				.add("dataEnvioCorrecao", relatorioFinalFacilitadorVO.getDataEnvioCorrecao())
				.add("dataDeferimento", relatorioFinalFacilitadorVO.getDataDeferimento())
				.add("dataIndeferimento", relatorioFinalFacilitadorVO.getDataIndeferimento())
				.add("nota", relatorioFinalFacilitadorVO.getNota())
				.add("supervisor", relatorioFinalFacilitadorVO.getSupervisor().getCodigo()),
				new AtributoPersistencia().add("codigo", relatorioFinalFacilitadorVO.getCodigo()),
				usuarioVO);
		getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().alterar(relatorioFinalFacilitadorVO.getQuestionarioRespostaOrigemVO(), usuarioVO);
		} catch (Exception e) {
			UtilReflexao.invocarMetodoSetParametroNull(relatorioFinalFacilitadorVO, "codigo");
			relatorioFinalFacilitadorVO.setNovoObj(true);
			throw e;
		}
	}

	public String getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT Relatoriofinalfacilitador.*, disciplina.nome as \"disciplina.nome\", disciplina.codigo as \"disciplina\", questionarioRespostaOrigem.codigo as \"questionarioRespostaOrigem.codigo\" ");
		sql.append(" FROM Relatoriofinalfacilitador  ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = Relatoriofinalfacilitador.matriculaperiodoturmadisciplina ");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" inner join questionariorespostaorigem on questionariorespostaorigem.codigo = Relatoriofinalfacilitador.questionariorespostaorigem ");
		return sql.toString();
	}

	@Override
	public List<RelatorioFinalFacilitadorVO> consultar(Integer disciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean validarAcesso, int nivelMontarDados, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE 1=1  ");
		if (disciplina != null && disciplina > 0) {
			sql.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO)) {
			sql.append(" and matriculaperiodoturmadisciplina.codigo = '").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append("' ");
		}
		sql.append(" order by dataEnvioAnalise desc ");
		if (limit != null && limit > 0) {
			sql.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public Integer consultarTotalRegistro(Integer disciplina, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO) throws Exception {
		StringBuilder sql = new StringBuilder(" select count(Relatoriofinalfacilitador.codigo) as qtde from Relatoriofinalfacilitador ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = Relatoriofinalfacilitador.matriculaperiodoturmadisciplina ");
		sql.append(" inner join questionariorespostaorigem on questionariorespostaorigem.codigo = Relatoriofinalfacilitador.questionariorespostaorigem ");
		sql.append(" WHERE 1=1  ");
		if (disciplina != null && disciplina > 0) {
			sql.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		}
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO)) {
			sql.append(" and matriculaperiodoturmadisciplina.codigo = '").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	
	private List<RelatorioFinalFacilitadorVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados,  UsuarioVO usuario) throws Exception {
		List<RelatorioFinalFacilitadorVO> relatorioFinalFacilitadorVO = new ArrayList<RelatorioFinalFacilitadorVO>(0);
		while (rs.next()) {
			relatorioFinalFacilitadorVO.add(montarDados(rs, nivelMontarDados, usuario));
		}
		return relatorioFinalFacilitadorVO;
	}

	private RelatorioFinalFacilitadorVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RelatorioFinalFacilitadorVO obj = new RelatorioFinalFacilitadorVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(rs.getInt("codigo"));	
		obj.getMatriculaperiodoturmadisciplinaVO().setCodigo(rs.getInt("matriculaperiodoturmadisciplina"));
		obj.getMatriculaperiodoturmadisciplinaVO().getDisciplina().setCodigo(rs.getInt("disciplina"));
		obj.getMatriculaperiodoturmadisciplinaVO().getDisciplina().setNome(rs.getString("disciplina.nome"));
		obj.getQuestionarioRespostaOrigemVO().setCodigo(rs.getInt("questionarioRespostaOrigem.codigo"));
		if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemVO())) {
		obj.setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorChavePrimaria(
				obj.getQuestionarioRespostaOrigemVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		if(Uteis.isAtributoPreenchido(rs.getString("situacao"))) {
			obj.setSituacao(SituacaoRelatorioFacilitadorEnum.getName(rs.getString("situacao")));
		}
		obj.setAno(rs.getString("ano"));
		obj.setSemestre(rs.getString("semestre"));
		obj.setMes(rs.getString("mes"));
		obj.setDataEnvioAnalise(rs.getDate("dataEnvioAnalise"));
		obj.setDataEnvioCorrecao(rs.getDate("dataenviocorrecao"));
		obj.setDataDeferimento(rs.getDate("datadeferimento"));
		obj.setDataIndeferimento(rs.getDate("dataindeferimento"));
		obj.getSupervisor().setCodigo(rs.getInt("supervisor"));
		obj.setSupervisor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getSupervisor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		if (rs.getObject("nota") == null) {
			obj.setNota((Double) rs.getObject("nota"));
		} else {
			obj.setNota(rs.getDouble("nota"));
		}
		obj.setMotivo(rs.getString("motivo"));
		if(nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.getMatriculaperiodoturmadisciplinaVO().setMatricula(rs.getString("matricula.matricula"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("pessoa.codigo"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(rs.getString("pessoa.nome"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCPF(rs.getString("pessoa.cpf"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setBanco(rs.getString("pessoa.banco"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setAgencia(rs.getString("pessoa.contacorrente"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setContaCorrente(rs.getString("pessoa.contacorrente"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setUniversidadeParceira(rs.getString("pessoa.universidadeparceira"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setModalidadeBolsa(ModalidadeBolsaEnum.valueOf(rs.getString("pessoa.modalidadebolsa")));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setValorBolsa(rs.getDouble("pessoa.valorbolsa"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setRegistroAcademico(rs.getString("pessoa.registroacademico"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getCurso().setNome(rs.getString("curso.nome"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setEmail(rs.getString("email"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setEmail2(rs.getString("pessoaemailinstitucional.email"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCelular(rs.getString("celular"));
		obj.setTotalizadorNaoEnviouRelatorio(rs.getInt("qtdNaoEnviouRel"));
		obj.setTotalizadorEmAnalise(rs.getInt("qtdAnaliseSuperior"));
		obj.setTotalizadorCorrecaoAluno(rs.getInt("qtdCorrecao"));
		obj.setTotalizadorDeferido(rs.getInt("qtdDeferido"));
		obj.setTotalizadorIndeferido(rs.getInt("qtdIndeferido"));
		obj.setTotalizadorSuspensaoBolsa(rs.getInt("qtdSuspensaoBolsa"));
		if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemVO())) {
			obj.setNomeResponsavel(rs.getString("responsavelSituacao"));
			obj.setDataResponsavel(rs.getDate("dataSituacao"));
		} else {
			obj.setNomeResponsavel("");
			obj.setDataResponsavel(null);	
		}
		return obj;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return RelatorioFinalFacilitador.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		RelatorioFinalFacilitador.idEntidade = idEntidade;
	}
	
	@Override
	public Boolean validarRelatorioFinalFacilitador(MatriculaPeriodoTurmaDisciplinaVO matriculaperiodoturmadisciplina) {
		StringBuilder sql = new StringBuilder(" select Relatoriofinalfacilitador.dataenvioanalise from Relatoriofinalfacilitador ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = Relatoriofinalfacilitador.matriculaperiodoturmadisciplina ");
		sql.append(" inner join questionariorespostaorigem on questionariorespostaorigem.codigo = Relatoriofinalfacilitador.questionariorespostaorigem ");
		sql.append(" WHERE 1=1  ");
		if (Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getDisciplina())) {
			sql.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(matriculaperiodoturmadisciplina.getDisciplina().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getMatricula())) {
			sql.append(" and matriculaperiodoturmadisciplina.matricula = '").append(matriculaperiodoturmadisciplina.getMatricula()).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}
	
	public void preencherDadosRelatorioFacilitadorQuestionarioRespostaOrigem(RelatorioFinalFacilitadorVO relatoriofinalfacilitadorVO, Integer codigoQuestionariorelatorioFacilitador, UsuarioVO usuarioVO) throws Exception{		
		Integer codigoQuestionario = 0;
		codigoQuestionario = codigoQuestionariorelatorioFacilitador;
		relatoriofinalfacilitadorVO.setQuestionarioRespostaOrigemVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorChavePrimaria(codigoQuestionario, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
		if(Uteis.isAtributoPreenchido(relatoriofinalfacilitadorVO.getQuestionarioRespostaOrigemVO())) {
			relatoriofinalfacilitadorVO.getQuestionarioRespostaOrigemVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioOrigem(codigoQuestionario, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
			 
			for (PerguntaRespostaOrigemVO perguntaRespostaOrigemPrincipalVO : relatoriofinalfacilitadorVO.getQuestionarioRespostaOrigemVO().getPerguntaRespostaOrigemVOs()) {
				getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().editarListaPerguntaItemRespostaOrigemAdicionadasVO(perguntaRespostaOrigemPrincipalVO, perguntaRespostaOrigemPrincipalVO.getPerguntaItemRespostaOrigemVOs(), usuarioVO);
			}
		}
	}
	
	@Override
	public RelatorioFinalFacilitadorVO consultarPorChamvePrimaria(Integer relatorioFinalFacilitador, Boolean validarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE relatorioFinalFacilitador.codigo = ").append(relatorioFinalFacilitador);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO = new RelatorioFinalFacilitadorVO();
		if (rs.next()) {
			relatorioFinalFacilitadorVO = montarDados(rs, nivelMontarDados, usuarioVO);
		}
		return relatorioFinalFacilitadorVO;
	}
	
	@Override
	public RelatorioFinalFacilitadorVO consultarDadosEnvioEmail(RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT pessoa.nome as \"pessoa.nome\", pessoa.registroacademico, unidadeensino.nome as \"unidadeensino.nome\", curso.nome as \"curso.nome\", ");
		sql.append(" pessoa.codigo as \"pessoa.codigo\", pessoa.email as \"pessoa.email\" ");
		sql.append(" FROM matriculaperiodoturmadisciplina ");
		sql.append(" INNER JOIN matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append(" INNER JOIN pessoa on matricula.aluno = pessoa.codigo ");
		sql.append(" INNER JOIN curso on matricula.curso = curso.codigo ");
		sql.append(" INNER JOIN unidadeensino on matricula.unidadeensino = unidadeensino.codigo ");
		sql.append(" WHERE matriculaperiodoturmadisciplina.codigo =").append(relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getCodigo());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			RelatorioFinalFacilitadorVO obj = new RelatorioFinalFacilitadorVO();
			obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(rs.getString("pessoa.nome"));
			obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("pessoa.codigo"));
			obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setEmail(rs.getString("pessoa.email"));
			obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setRegistroAcademico(rs.getString("registroacademico"));
			obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
			obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getCurso().setNome(rs.getString("curso.nome"));
			return obj;
		}
		return new RelatorioFinalFacilitadorVO();

	}
	
	@Override
	public void validarDados(RelatorioFinalFacilitadorVO obj) throws Exception {
		if (obj.getAno() == null || obj.getAno().isEmpty()) {
			throw new ConsistirException("O campo Ano deve ser informado.");
		}
		if (obj.getSemestre() == null || obj.getSemestre().isEmpty()) {
			throw new ConsistirException("O campo Semestre deve ser informado.");
		}
		if (obj.getMes() == null || obj.getMes().isEmpty()) {
			throw new ConsistirException("O campo Mês deve ser informado.");
		}
	}
	
	@Override
	public boolean realizarVerificacaoFacilitador(Integer codigoPessoa, String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) as QTDE");
		sql.append(" from salaaulablackboard");
		sql.append(" inner join salaaulablackboardpessoa as facilitador on facilitador.salaaulablackboard = salaaulablackboard.codigo");
		sql.append(" inner join pessoaemailinstitucional ef on ef.codigo = facilitador.pessoaemailinstitucional");
		sql.append(" inner join pessoa as f on f.codigo = ef.pessoa");
		sql.append(" where facilitador.tiposalaaulablackboardpessoaenum = 'FACILITADOR'");
		sql.append(" and salaaulablackboard.ano ='").append(ano).append("' ");
		sql.append(" and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" and f.codigo = '").append(codigoPessoa).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return Uteis.isAtributoPreenchido(rs, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}
	
	@Override
	public PessoaVO consultarSupervisorPorFacilitador(String matriculaFacilitador, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select f.codigo, f.nome, supervisor.tiposalaaulablackboardpessoaenum, s.codigo as codigosupervisor, s.nome as nomesupervisor");
		sql.append(" from salaaulablackboard");
		sql.append(" inner join salaaulablackboardpessoa as facilitador on facilitador.salaaulablackboard = salaaulablackboard.codigo");
		sql.append(" inner join pessoaemailinstitucional ef on ef.codigo = facilitador.pessoaemailinstitucional");
		sql.append(" inner join pessoa as f on f.codigo = ef.pessoa");
		sql.append(" inner join salaaulablackboardpessoa as supervisor on supervisor.salaaulablackboard = salaaulablackboard.codigo");
		sql.append(" inner join pessoaemailinstitucional es on es.codigo = supervisor.pessoaemailinstitucional");
		sql.append(" inner join pessoa as s on s.codigo = es.pessoa ");
		sql.append(" inner join matricula as mf on mf.aluno = f.codigo ");
		sql.append(" where facilitador.tiposalaaulablackboardpessoaenum = 'FACILITADOR'");
		sql.append(" and supervisor.tiposalaaulablackboardpessoaenum = 'ORIENTADOR'");
		sql.append(" and salaaulablackboard.ano ='").append(ano).append("' ");
		sql.append(" and salaaulablackboard.semestre = '").append(semestre).append("' ");
		sql.append(" and mf.matricula = '").append(matriculaFacilitador).append("' ");
		sql.append(" limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(rs.getInt("codigosupervisor"));
			obj.setNome(rs.getString("nomesupervisor"));
			return obj;
		}
		return new PessoaVO();

	}
	
	@Override
	public List<RelatorioFinalFacilitadorVO> consultarDashboradRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataFinal, Date dataInicial, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		StringBuilder sqlstr = new StringBuilder("");
		consultarRelatoriosNaoEnviados(relatorioFinalFacilitador, listaUnidadeEnsino, dataFinal, dataInicial, calendarioRelatorioFinalFacilitadorVO, sqlstr, false);
		sqlstr.append(" ORDER BY pessoa.nome");
		if(dataModelo != null) {
			UteisTexto.addLimitAndOffset(sqlstr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlstr.toString());
		if(dataModelo != null) {
			dataModelo.setTotalRegistrosEncontrados(0);
			if (rs.next()) {
				dataModelo.setTotalRegistrosEncontrados(rs.getInt("totalRegistroConsulta"));
			}
			rs.beforeFirst();
		}
//		System.out.println(sqlstr.toString());
		return montarDadosDashboard(rs, usuario);
	}

	private void consultarRelatoriosNaoEnviados(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataFinal, Date dataInicial, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, StringBuilder sqlstr, boolean trazerSomenteContador) {
		sqlstr.append(" WITH x AS (");
		sqlstr.append(" SELECT");
		sqlstr.append(" matriculaperiodoturmadisciplina.matricula, matriculaperiodoturmadisciplina.disciplina disciplina, array_to_string(array_agg(distinct salaaulablackboard.idsalaaulablackboard),',') as idsalaaulablackboard,");
		sqlstr.append(" matriculaperiodoturmadisciplina.codigo \"matriculaperiodoturmadisciplina.codigo\", pessoaemailinstitucional.codigo \"pessoaemailinstitucional.codigo\", pessoaemailinstitucional.email \"pessoaemailinstitucional.email\",");
		sqlstr.append(" matriculaperiodoturmadisciplina.ano \"matriculaperiodoturmadisciplina.ano\", matriculaperiodoturmadisciplina.semestre \"matriculaperiodoturmadisciplina.semestre\"");
		sqlstr.append(" FROM matriculaperiodoturmadisciplina");
		sqlstr.append(" INNER JOIN matricula ON matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		sqlstr.append(" INNER JOIN curso on curso.codigo = matricula.curso");
		sqlstr.append(" INNER JOIN configuracaoldap on configuracaoldap.codigo = curso.configuracaoldap");
		sqlstr.append(" INNER JOIN LATERAL (");
		sqlstr.append(" SELECT pessoaemailinstitucional.codigo, pessoaemailinstitucional.pessoa, pessoaemailinstitucional.email from pessoaemailinstitucional");
		sqlstr.append(" WHERE pessoaemailinstitucional.pessoa = matricula.aluno");
		sqlstr.append(" ORDER BY pessoaemailinstitucional.statusativoinativoenum,");
		sqlstr.append(" CASE WHEN pessoaemailinstitucional.email ILIKE ('%' || configuracaoldap.dominio) THEN 1 ELSE 2 END");
		sqlstr.append(" LIMIT 1 ) pessoaemailinstitucional ON pessoaemailinstitucional.codigo IS NOT NULL ");
		sqlstr.append(" INNER JOIN LATERAL (");
		sqlstr.append(" SELECT salaaulablackboard.codigo, salaaulablackboard.idsalaaulablackboard FROM salaaulablackboard ");
		sqlstr.append(" INNER JOIN salaaulablackboardpessoa on salaaulablackboardpessoa.salaaulablackboard = salaaulablackboard.codigo ");
		sqlstr.append(" WHERE salaaulablackboardpessoa.tiposalaaulablackboardpessoaenum = 'FACILITADOR' ");
		sqlstr.append(" AND pessoaemailinstitucional.codigo = salaaulablackboardpessoa.pessoaemailinstitucional ");
		sqlstr.append(" AND salaaulablackboard.ano = '").append(calendarioRelatorioFinalFacilitadorVO.getAno()).append("' ");
		sqlstr.append(" AND salaaulablackboard.semestre = '").append(calendarioRelatorioFinalFacilitadorVO.getSemestre()).append("' ");
		sqlstr.append(" ) as salaaulablackboard on salaaulablackboard.codigo is not null ");
		sqlstr.append(" INNER JOIN LATERAL (SELECT  pes.codigo, pes.nome FROM salaaulablackboardpessoa as supervisor ");
		sqlstr.append(" INNER JOIN pessoaemailinstitucional as peis on supervisor.pessoaemailinstitucional = peis.codigo ");
		sqlstr.append(" INNER JOIN pessoa as pes on pes.codigo = peis.pessoa ");
		sqlstr.append(" WHERE supervisor.salaaulablackboard = salaaulablackboard.codigo ");
		sqlstr.append(" AND supervisor.tiposalaaulablackboardpessoaenum = 'ORIENTADOR' order by pes.nome limit 1) as supervisor on supervisor.codigo is not null ");
		sqlstr.append(" WHERE matriculaperiodoturmadisciplina.ano = '").append(calendarioRelatorioFinalFacilitadorVO.getAno()).append("' ");
		sqlstr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(calendarioRelatorioFinalFacilitadorVO.getSemestre()).append("' ");;
		if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getSupervisor())) {
			sqlstr.append(" AND supervisor.codigo = ").append(relatorioFinalFacilitador.getSupervisor().getCodigo());
		}
		sqlstr.append(" AND EXISTS (");
		sqlstr.append(" SELECT FROM calendariorelatoriofinalfacilitador");
		sqlstr.append(" WHERE matriculaperiodoturmadisciplina.disciplina = calendariorelatoriofinalfacilitador.disciplina");
		sqlstr.append(" AND matriculaperiodoturmadisciplina.ano = calendariorelatoriofinalfacilitador.ano");
		sqlstr.append(" AND matriculaperiodoturmadisciplina.semestre = calendariorelatoriofinalfacilitador.semestre");
		sqlstr.append(" AND calendarioRelatorioFinalFacilitador.mes = '").append(calendarioRelatorioFinalFacilitadorVO.getMes()).append("' ");
		if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getDisciplina())) {
			sqlstr.append(" and calendariorelatoriofinalfacilitador.disciplina = ").append(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getDisciplina().getCodigo());
		}
		sqlstr.append(") ");
		if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO())) {
			sqlstr.append(" AND matricula.matricula = '").append(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getMatricula()).append("'");	
		}
		if(Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
			sqlstr.append(" AND matricula.unidadeensino in (0");
					for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
						if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
							sqlstr.append(", ").append(unidadeEnsinoVO.getCodigo());
						}
					}
			sqlstr.append(" ) ");
		}
		sqlstr.append(" AND NOT EXISTS (");
		sqlstr.append(" SELECT FROM relatoriofinalfacilitador");
		sqlstr.append(" WHERE relatoriofinalfacilitador.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlstr.append(")");
		sqlstr.append(" GROUP BY ");
		sqlstr.append(" matriculaperiodoturmadisciplina.matricula, matriculaperiodoturmadisciplina.disciplina,");
		sqlstr.append(" matriculaperiodoturmadisciplina.codigo, pessoaemailinstitucional.codigo, pessoaemailinstitucional.email,");
		sqlstr.append(" matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre");
		sqlstr.append(" ORDER BY 1, 1)");
		if(trazerSomenteContador) {
			sqlstr.append(" SELECT COUNT(*) AS totalRegistroConsulta");
		} else {
			sqlstr.append(" SELECT COUNT(*) OVER() AS totalRegistroConsulta,");
			sqlstr.append(" pessoa.cpf as \"pessoa.cpf\", pessoa.banco AS \"pessoa.banco\", pessoa.agencia AS \"pessoa.agencia\", pessoa.contacorrente AS \"pessoa.contacorrente\",");
			sqlstr.append(" pessoa.universidadeparceira AS \"pessoa.universidadeparceira\", pessoa.modalidadebolsa AS \"pessoa.modalidadebolsa\", pessoa.valorbolsa AS \"pessoa.valorbolsa\",");
			sqlstr.append(" pessoa.registroacademico AS \"pessoa.registroacademico\", m.matricula AS \"matricula.matricula\", pessoa.nome AS \"pessoa.nome\",");
			sqlstr.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.email, x.\"pessoaemailinstitucional.email\",");
			sqlstr.append(" pessoa.celular, curso.nome AS \"curso.nome\", x.\"matriculaperiodoturmadisciplina.codigo\", u.nome as \"unidadeEnsino.nome\",");
			sqlstr.append(" disciplina.nome \"disciplina.nome\", NULL::timestamp \"dataenvioanalise\", 'NH' \"situacao\",");
			sqlstr.append(" x.idsalaaulablackboard, x.\"matriculaperiodoturmadisciplina.ano\", x.\"matriculaperiodoturmadisciplina.semestre\",");
			sqlstr.append(" 0 \"supervisor\", NULL::float \"nota\", NULL::timestamp \"updated\",");
			sqlstr.append(" '' \"nomeupdated\", '' \"motivo\"");
		}
		sqlstr.append(" FROM x");
		sqlstr.append(" INNER JOIN matricula m ON  x.matricula = m.matricula");
		sqlstr.append(" INNER JOIN pessoa ON m.aluno = pessoa.codigo");
		sqlstr.append(" INNER JOIN curso ON m.curso = curso.codigo");
		sqlstr.append(" INNER JOIN unidadeensino u on m.unidadeensino = u.codigo");
		sqlstr.append(" INNER JOIN disciplina ON x.disciplina = disciplina.codigo");
	}
	
	private List<RelatorioFinalFacilitadorVO> montarDadosDashboard(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		List<RelatorioFinalFacilitadorVO> objs = new ArrayList<RelatorioFinalFacilitadorVO>(0);
		while (rs.next()) {
		RelatorioFinalFacilitadorVO obj = new RelatorioFinalFacilitadorVO();
		obj.setNovoObj(Boolean.TRUE);
		obj.getMatriculaperiodoturmadisciplinaVO().setCodigo(rs.getInt("matriculaperiodoturmadisciplina.codigo"));
		obj.getMatriculaperiodoturmadisciplinaVO().getDisciplina().setNome(rs.getString("disciplina.nome"));
		if(Uteis.isAtributoPreenchido(rs.getString("situacao"))) {
			obj.setSituacao(SituacaoRelatorioFacilitadorEnum.getEnum(rs.getString("situacao")));
		}
		obj.setDataEnvioAnalise(rs.getDate("dataEnvioAnalise"));
		obj.getMatriculaperiodoturmadisciplinaVO().setMatricula(rs.getString("matricula.matricula"));
		obj.getSupervisor().setCodigo(rs.getInt("supervisor"));
		if(!Uteis.isAtributoPreenchido(obj.getSupervisor())) {
			String ano =  rs.getString("matriculaperiodoturmadisciplina.ano");
			String semestre = rs.getString("matriculaperiodoturmadisciplina.semestre");
			obj.setSupervisor(consultarSupervisorPorFacilitador(obj.getMatriculaperiodoturmadisciplinaVO().getMatricula(), ano, semestre, usuario));
		} else {
			obj.setSupervisor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getSupervisor().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getCurso().setNome(rs.getString("curso.nome"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino.nome"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("pessoa.codigo"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(rs.getString("pessoa.nome"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCPF(rs.getString("pessoa.cpf"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setBanco(rs.getString("pessoa.banco"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setAgencia(rs.getString("pessoa.contacorrente"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setContaCorrente(rs.getString("pessoa.contacorrente"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setUniversidadeParceira(rs.getString("pessoa.universidadeparceira"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setModalidadeBolsa(ModalidadeBolsaEnum.valueOf(rs.getString("pessoa.modalidadebolsa")));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setValorBolsa(rs.getDouble("pessoa.valorbolsa"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setRegistroAcademico(rs.getString("pessoa.registroacademico"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setEmail(rs.getString("email"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setEmail2(rs.getString("pessoaemailinstitucional.email"));
		obj.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().setCelular(rs.getString("celular"));
		obj.setMotivo(rs.getString("motivo"));
		if(Uteis.isColunaExistente(rs, "idSalaAulaBlackBoard")) {
			obj.setIdSalaAulaBlackBoard(rs.getString("idSalaAulaBlackBoard"));
		}
		obj.setNomeResponsavel("");
		obj.setDataResponsavel(null);
		objs.add(obj);
		}
		return objs;
	}
	
	public List<RelatorioFinalFacilitadorVO> consultarRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicial, Date dataFinal, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, String multiplasSituacoes, int nivelMontarDados, DataModelo dataModelo, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct count(*) over() as totalRegistroConsulta, (");
		if(!Uteis.isAtributoPreenchido(dataInicial) && !Uteis.isAtributoPreenchido(dataFinal) && !Uteis.isAtributoPreenchido(multiplasSituacoes)) {
			consultarRelatoriosNaoEnviados(relatorioFinalFacilitador, listaUnidadeEnsino, dataFinal, dataInicial, calendarioRelatorioFinalFacilitadorVO, sql, true);
		} else {
			sql.append("0");
		}
		sql.append(") as qtdNaoEnviouRel,");
		sql.append(" sum(case when relatoriofinalfacilitador.situacao = '").append(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR).append("' then 1 else 0 end) over() as qtdAnaliseSuperior,");
		sql.append(" sum(case when relatoriofinalfacilitador.situacao = '").append(SituacaoRelatorioFacilitadorEnum.CORRECAO_ALUNO).append("' then 1 else 0 end) over() as qtdCorrecao,");
		sql.append(" sum(case when relatoriofinalfacilitador.situacao = '").append(SituacaoRelatorioFacilitadorEnum.DEFERIDO_SUPERVISOR).append("' then 1 else 0 end) over() as qtdDeferido,");
		sql.append(" sum(case when relatoriofinalfacilitador.situacao = '").append(SituacaoRelatorioFacilitadorEnum.INDEFERIDO_SUPERVISOR).append("' then 1 else 0 end) over() as qtdIndeferido,");
		sql.append(" sum(case when relatoriofinalfacilitador.situacao = '").append(SituacaoRelatorioFacilitadorEnum.SUSPENSAO_BOLSA).append("' then 1 else 0 end) over() as qtdSuspensaoBolsa,");
		sql.append(" pessoa.cpf as \"pessoa.cpf\", pessoa.banco AS \"pessoa.banco\", pessoa.agencia AS \"pessoa.agencia\", pessoa.contacorrente AS \"pessoa.contacorrente\",");
		sql.append(" pessoa.universidadeparceira AS \"pessoa.universidadeparceira\", pessoa.modalidadebolsa AS \"pessoa.modalidadebolsa\", pessoa.valorbolsa AS \"pessoa.valorbolsa\",");
		sql.append(" pessoa.registroacademico as \"pessoa.registroacademico\", pessoa.nome as \"pessoa.nome\", matricula.matricula as \"matricula.matricula\", disciplina.nome as \"disciplina.nome\", questionarioRespostaOrigem.codigo as \"questionarioRespostaOrigem.codigo\", ");
		sql.append(" curso.nome as \"curso.nome\", pessoa.email as \"email\", pessoaemailinstitucional.email as \"pessoaemailinstitucional.email\", pessoa.celular as \"celular\", relatoriofinalfacilitador.matriculaperiodoturmadisciplina, ");
		sql.append(" relatoriofinalfacilitador.codigo, calendariorelatoriofinalfacilitador.disciplina, relatoriofinalfacilitador.supervisor, relatoriofinalfacilitador.nota,");
		sql.append(" relatoriofinalfacilitador.situacao, relatoriofinalfacilitador.ano, relatoriofinalfacilitador.semestre, relatoriofinalfacilitador.mes, pessoa.codigo as \"pessoa.codigo\", relatoriofinalfacilitador.motivo,");
		sql.append(" relatoriofinalfacilitador.dataenvioanalise, relatoriofinalfacilitador.dataenviocorrecao, relatoriofinalfacilitador.datadeferimento, relatoriofinalfacilitador.dataindeferimento, unidadeensino.nome as \"unidadeensino.nome\", ");
		sql.append(" relatoriofinalfacilitador.nomecreated, relatoriofinalfacilitador.created, relatoriofinalfacilitador.responsavelSituacao, relatoriofinalfacilitador.dataSituacao ");
		sql.append(" from relatoriofinalfacilitador");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = relatoriofinalfacilitador.matriculaperiodoturmadisciplina");
		sql.append(" inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		sql.append(" inner join unidadeensino on matricula.unidadeensino = unidadeensino.codigo");
		sql.append(" INNER JOIN curso on curso.codigo = matricula.curso");
		sql.append(" INNER JOIN configuracaoldap on configuracaoldap.codigo = curso.configuracaoldap");
		sql.append(" INNER JOIN LATERAL (");
		sql.append(" SELECT pessoaemailinstitucional.codigo, pessoaemailinstitucional.pessoa, pessoaemailinstitucional.email from pessoaemailinstitucional");
		sql.append(" WHERE pessoaemailinstitucional.pessoa = matricula.aluno");
		sql.append(" ORDER BY pessoaemailinstitucional.statusativoinativoenum,");
		sql.append(" CASE WHEN pessoaemailinstitucional.email ILIKE ('%' || configuracaoldap.dominio) THEN 1 ELSE 2 END");
		sql.append(" LIMIT 1 ) pessoaemailinstitucional ON pessoaemailinstitucional.codigo IS NOT NULL ");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" left join questionariorespostaorigem on questionariorespostaorigem.codigo = Relatoriofinalfacilitador.questionariorespostaorigem ");
		sql.append(" inner join calendariorelatoriofinalfacilitador on matriculaperiodoturmadisciplina.disciplina = calendariorelatoriofinalfacilitador.disciplina");
		sql.append(" where 1 = 1 ");
		montarFiltrosRelatorioFacilitador(relatorioFinalFacilitador, listaUnidadeEnsino, dataInicial, dataFinal, calendarioRelatorioFinalFacilitadorVO, multiplasSituacoes, usuario, sql);
		sql.append(" order by pessoa.nome");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
//		System.out.println(sql.toString());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		montarTotalizadorConsultaBasica(dataModelo, rs);
		return montarDadosConsulta(rs, nivelMontarDados, usuario);
	}

	private void montarFiltrosRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicial, Date dataFinal, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, String multiplasSituacoes, UsuarioVO usuario, StringBuilder sql) {
		if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO())) {
			sql.append(" AND matricula.matricula = '").append(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getMatricula()).append("'");	
		}	
		
		if(Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
			sql.append(" AND matricula.unidadeensino in (0");
					for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
						if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
							sql.append(", ").append(unidadeEnsinoVO.getCodigo());
						}
					}
			sql.append(" ) ");
		}
		sql.append(" and ");
		sql.append(realizarGeracaoWherePeriodo(dataInicial, dataFinal, "relatoriofinalfacilitador.dataenvioanalise", true));
		if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getSupervisor())) {
			sql.append(" and relatoriofinalfacilitador.supervisor = ").append(relatorioFinalFacilitador.getSupervisor().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getDisciplina())) {
			sql.append(" and calendariorelatoriofinalfacilitador.disciplina = ").append(relatorioFinalFacilitador.getMatriculaperiodoturmadisciplinaVO().getDisciplina().getCodigo());
		}
		
		if(Uteis.isAtributoPreenchido(calendarioRelatorioFinalFacilitadorVO.getAno())) {
			sql.append(" and calendariorelatoriofinalfacilitador.ano = '").append(calendarioRelatorioFinalFacilitadorVO.getAno()).append("' ");
		}
		
		if(Uteis.isAtributoPreenchido(calendarioRelatorioFinalFacilitadorVO.getSemestre())) {
			sql.append(" and calendariorelatoriofinalfacilitador.semestre = '").append(calendarioRelatorioFinalFacilitadorVO.getSemestre()).append("' ");
		}
		
		if(Uteis.isAtributoPreenchido(calendarioRelatorioFinalFacilitadorVO.getMes())) {
			sql.append(" and calendariorelatoriofinalfacilitador.mes = '").append(calendarioRelatorioFinalFacilitadorVO.getMes()).append("' ");
		}
		String situacaoParaUsar = Uteis.isAtributoPreenchido(multiplasSituacoes) ? multiplasSituacoes : "";		
		if((Uteis.isAtributoPreenchido(situacaoParaUsar)) && 
				!SituacaoRelatorioFacilitadorEnum.NENHUM.toString().contentEquals(situacaoParaUsar)) {
			sql.append(" and relatoriofinalfacilitador.situacao = any ('{").append(situacaoParaUsar).append("}'::TEXT[]) ");	
		}
	}
	
	public Integer consultarTotalizadorNaoEnviouRelatorio(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicial, Date dataFinal, CalendarioRelatorioFinalFacilitadorVO calendarioRelatorioFinalFacilitadorVO, String multiplasSituacoes) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select (");
		if(!Uteis.isAtributoPreenchido(dataInicial) && !Uteis.isAtributoPreenchido(dataFinal) && !Uteis.isAtributoPreenchido(multiplasSituacoes)) {
			consultarRelatoriosNaoEnviados(relatorioFinalFacilitador, listaUnidadeEnsino, dataFinal, dataInicial, calendarioRelatorioFinalFacilitadorVO, sql, true);
		} else {
			sql.append("0");
		}
		sql.append(") as qtdNaoEnviouRel");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return new Integer(tabelaResultado.getInt("qtdNaoEnviouRel"));
		}
		return 0;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoRelatorioFacilitador(RelatorioFinalFacilitadorVO relatorioFinalFacilitador, Double valorHistoricoNota, UsuarioVO usuarioVO) {
		try {
			final StringBuilder sb = new StringBuilder(" UPDATE RelatorioFinalFacilitador set situacao = ?, nota = ?, motivo = ?, dataenviocorrecao = ?, dataindeferimento = ?, datadeferimento = ?, responsavelSituacao = ?, dataSituacao = ? ");
			sb.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
					int x = 1;
					sqlAlterar.setString(x++, relatorioFinalFacilitador.getSituacao().name());
					if(valorHistoricoNota != null){
						sqlAlterar.setDouble(x++, valorHistoricoNota);	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getMotivo())){
						sqlAlterar.setString(x++, relatorioFinalFacilitador.getMotivo());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(relatorioFinalFacilitador.getDataEnvioCorrecao() != null){
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(relatorioFinalFacilitador.getDataEnvioCorrecao()));	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(relatorioFinalFacilitador.getDataIndeferimento() != null){
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(relatorioFinalFacilitador.getDataIndeferimento()));	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(relatorioFinalFacilitador.getDataDeferimento() != null){
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(relatorioFinalFacilitador.getDataDeferimento()));	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(Uteis.isAtributoPreenchido(relatorioFinalFacilitador.getNomeResponsavel())){
						sqlAlterar.setString(x++, relatorioFinalFacilitador.getNomeResponsavel());	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					if(relatorioFinalFacilitador.getDataResponsavel() != null){
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(relatorioFinalFacilitador.getDataResponsavel()));	
					}else{
						sqlAlterar.setNull(x++, 0);	
					}
					sqlAlterar.setInt(x++, relatorioFinalFacilitador.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			setAlterarSeiLog(nomeTabela, relatorioFinalFacilitador.getCodigo().intValue(), usuarioVO);
		} catch (Exception e) {
			try {
				throw e;
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	@Override
	public File realizarGeracaoExcel(List<RelatorioFinalFacilitadorVO> listaRelatorioFinalFacilitadorVO,  String urlLogoPadraoRelatorio,UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(listaRelatorioFinalFacilitadorVO), "Não foi encontrado dados para geração do Relatório.");
		File arquivo = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Relatório Final Facilitador");
		UteisExcel uteisExcel = new UteisExcel(workbook);
		boolean trazerResponsavelSituacao = true;
		int tamanhoTitulo = 21;
		if(listaRelatorioFinalFacilitadorVO.get(0).getSituacao().equals(SituacaoRelatorioFacilitadorEnum.ANALISE_SUPERVISOR) || 
				listaRelatorioFinalFacilitadorVO.get(0).getSituacao().equals(SituacaoRelatorioFacilitadorEnum.NENHUM)) {
			trazerResponsavelSituacao = false;
			tamanhoTitulo = 18;
		}
		uteisExcel.realizarGeracaoTopoPadraoRelatorio(workbook, sheet, urlLogoPadraoRelatorio, null , tamanhoTitulo, "");
		montarCabecalhoRelatorioExcel(uteisExcel, workbook, sheet, trazerResponsavelSituacao);
		montarCorpoRelatorioExcel(listaRelatorioFinalFacilitadorVO, uteisExcel, workbook, sheet, usuario, trazerResponsavelSituacao);
		arquivo = new File(getCaminhoPastaWeb() + File.separator + "relatorio" + File.separator + String.valueOf(new Date().getTime())+".xls");
		FileOutputStream out = new FileOutputStream(arquivo);
		workbook.write(out);
		out.close();
		return arquivo;
	}
	
	public void montarCabecalhoRelatorioExcel(UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet, Boolean trazerResponsavelSituacao) {
		int cellnum = 0;	
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000, "Registro Acadêmico");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000, "Matrícula");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Aluno");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"CPF");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Email");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Email Institucional");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Celular");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 20000,"Disciplina");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000,"Data Envio");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000,"Situação");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 20000,"Id Sala de Aula");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Supervisor");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000,"Nota");
		if(trazerResponsavelSituacao) {
			uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 6000,"Data Situação");
			uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Responsável da Situação");
			uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 20000,"Motivo");
		}
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Banco");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Agência");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Conta Corrente");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Universidade Parceira");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Modalidade Bolsa");
		uteisExcel.preencherCelulaCabecalho(sheet, row, cellnum++, 10000,"Valor Bolsa");
	}
	
	public void montarCorpoRelatorioExcel(List<RelatorioFinalFacilitadorVO> listaRelatorioFinalFacilitadorVO, UteisExcel uteisExcel, HSSFWorkbook workbook, HSSFSheet sheet,  UsuarioVO usuario, Boolean trazerResponsavelSituacao) throws Exception {		
		Row row = null;
		for (RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO : listaRelatorioFinalFacilitadorVO) {			
			row = sheet.createRow(sheet.getLastRowNum() + 1);
			int cellnum = 0;
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getRegistroAcademico());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatricula());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getNome());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getCPF());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getEmail());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getEmail2());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getCelular());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getDisciplina().getNome());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getDataEnvioAnalise());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getSituacao().getDescricao());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getIdSalaAulaBlackBoard());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getSupervisor().getNome());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getNotaApresentar());
			if(trazerResponsavelSituacao) {	
				uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getDataResponsavel());
				uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getNomeResponsavel());				
				uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMotivo());
			}
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getBanco());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getAgencia());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getContaCorrente());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getUniversidadeParceira());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getModalidadeBolsa().name());
			uteisExcel.preencherCelula( row, cellnum++,relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().getAluno().getValorBolsa());
		}
	}
	
	public void preencherTodosRelatorioFacilitador(List<RelatorioFinalFacilitadorVO> listaRelatorioFinalFacilitadorVOs) {
        for (RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO : listaRelatorioFinalFacilitadorVOs) {
        	relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().setAlunoSelecionado(Boolean.TRUE);		
        }
    }

    public void desmarcarTodosRelatorioFacilitador(List<RelatorioFinalFacilitadorVO> listaRelatorioFinalFacilitadorVOs) {
    	for (RelatorioFinalFacilitadorVO relatorioFinalFacilitadorVO : listaRelatorioFinalFacilitadorVOs) {
        	relatorioFinalFacilitadorVO.getMatriculaperiodoturmadisciplinaVO().getMatriculaObjetoVO().setAlunoSelecionado(Boolean.FALSE);		
        }
    }
    
    @Override
	public List<PessoaVO> consultarSupervisorPorNome(String valorConsulta, String ano, String semestre, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select pessoa.email as emailsupervisor, pessoa.codigo as codigoSupervisor, pessoa.cpf as codigoSupervisor, pessoa.nome as nomesupervisor");
		sql.append(" from pessoa");
		sql.append(" WHERE sem_acentos((pessoa.nome)) iLIKE(trim(sem_acentos(?)))");
		montarFiltroTipoSupervisorFacilitador(ano, semestre, TipoSalaAulaBlackboardPessoaEnum.ORIENTADOR.getValorApresentar());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), PERCENT + valorConsulta + PERCENT);
		System.out.println(sql.toString());
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (rs.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(rs.getInt("codigosupervisor"));
			obj.setNome(rs.getString("nomesupervisor"));
			obj.setCPF(rs.getString("codigosupervisor"));
			obj.setEmail(rs.getString("emailsupervisor"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
    @Override
	public List<PessoaVO> consultarSupervisorPorCPF(String valorConsulta, String ano, String semestre,  UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select pessoa.email as emailsupervisor, pessoa.codigo as codigoSupervisor, pessoa.cpf as codigoSupervisor, pessoa.nome as nomesupervisor");
		sql.append(" from pessoa");
		sql.append(" WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE(?) ");
		montarFiltroTipoSupervisorFacilitador(ano, semestre, TipoSalaAulaBlackboardPessoaEnum.ORIENTADOR.getValorApresentar());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), Uteis.retirarMascaraCPF(valorConsulta) + PERCENT);
		List<PessoaVO> vetResultado = new ArrayList<PessoaVO>(0);
		while (rs.next()) {
			PessoaVO obj = new PessoaVO();
			obj.setCodigo(rs.getInt("codigosupervisor"));
			obj.setNome(rs.getString("nomesupervisor"));
			obj.setCPF(rs.getString("codigosupervisor"));
			obj.setEmail(rs.getString("emailsupervisor"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
    
    @Override
    public StringBuffer montarFiltroTipoSupervisorFacilitador(String ano, String semestre, String tiposalaaulablackboardpessoa) {
		StringBuffer str = new StringBuffer();
		str.append(" and exists (");
		str.append(" select from salaaulablackboard");
		str.append(" inner join salaaulablackboardpessoa as supervisor on supervisor.salaaulablackboard = salaaulablackboard.codigo");
		str.append(" inner join pessoaemailinstitucional on pessoaemailinstitucional.codigo = supervisor.pessoaemailinstitucional");
		str.append(" where supervisor.tiposalaaulablackboardpessoaenum = '").append(tiposalaaulablackboardpessoa).append("' ");
		str.append(" and salaaulablackboard.ano = '").append(ano).append("' ");
		str.append(" and salaaulablackboard.semestre = '").append(semestre).append("' ");
		str.append(" and pessoa.codigo = pessoaemailinstitucional.pessoa)");
		str.append(" ");
		
		return str;
    }
}
