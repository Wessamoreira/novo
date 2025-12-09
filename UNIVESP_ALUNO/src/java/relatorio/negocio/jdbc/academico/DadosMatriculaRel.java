package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DadosMatriculaRelVO;
import relatorio.negocio.interfaces.academico.DadosMatriculaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DadosMatriculaRel extends SuperRelatorio implements DadosMatriculaRelInterfaceFacade {

	public DadosMatriculaRel() {
		// inicializarParametros();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DadosMatriculaRelInterfaceFacade#
	 * inicializarParametros()
	 */
	public void inicializarParametros() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.DadosMatriculaRelInterfaceFacade#criarObjeto
	 * (java.lang.String)
	 */
	public List<DadosMatriculaRelVO> criarObjeto(Integer codMatriculaPeriodo,  ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception {
		MatriculaPeriodoVO matriculaPeriodoVO = consultarUltimaMatriculaPeriodoDoAluno(codMatriculaPeriodo, usuarioVO);
		List<DadosMatriculaRelVO> listaRelatorio = new ArrayList<DadosMatriculaRelVO>(0);
		DadosMatriculaRelVO dadosMatriculaRelVO = new DadosMatriculaRelVO();
		SqlRowSet sql = consultarDadosBasicoDadosMatriculaRelVO(codMatriculaPeriodo);
		if (sql.next()) {
			dadosMatriculaRelVO.setAluno(sql.getString("nome"));
			dadosMatriculaRelVO.setCurso(sql.getString("curso"));
			dadosMatriculaRelVO.setTurno(sql.getString("turno"));
			dadosMatriculaRelVO.setCpf(Uteis.removerMascara(sql.getString("cpf")));
			dadosMatriculaRelVO.setUsuario(sql.getString("username"));
			dadosMatriculaRelVO.setMatricula(sql.getString("matricula"));
			dadosMatriculaRelVO.setNomeEmpresa(sql.getString("nomeEmpresa"));
			dadosMatriculaRelVO.getProcessoMatriculaCalendarioVO().setDataInicioInclusaoDisciplina(sql.getDate("datainicioinclusaodisciplina"));
			dadosMatriculaRelVO.getProcessoMatriculaCalendarioVO().setDataFinalInclusaoDisciplina(sql.getDate("datafinalinclusaodisciplina"));
			dadosMatriculaRelVO.getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataInicioPeriodoLetivo(sql.getDate("datainicioperiodoletivo"));
			dadosMatriculaRelVO.getProcessoMatriculaCalendarioVO().getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setDataFimPeriodoLetivo(sql.getDate("datafimperiodoletivo"));
			dadosMatriculaRelVO.setDisciplinaVOs(montarDisciplinas(matriculaPeriodoVO, usuarioVO));
			dadosMatriculaRelVO.setDocumentacaoMatriculaVOs(montarDocumentacaoMatricula(matriculaPeriodoVO, usuarioVO));
		}
		listaRelatorio.add(dadosMatriculaRelVO);
		return listaRelatorio;
	}

	public SqlRowSet consultarDadosBasicoDadosMatriculaRelVO(Integer matriculaPeriodoVO) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select matricula.matricula, pessoa.nome, pessoa.cpf, usuario.username, ");
		sqlStr.append("curso.nome as curso, turno.nome as turno, ");
		sqlStr.append("configuracoes.mensagemPlanoDeEstudo, ");
		sqlStr.append("periodoletivoativounidadeensinocurso.datainicioperiodoletivo, ");
		sqlStr.append("periodoletivoativounidadeensinocurso.datafimperiodoletivo, ");
		sqlStr.append("processomatriculacalendario.datafinalinclusaodisciplina, ");
		sqlStr.append("processomatriculacalendario.datainicioinclusaodisciplina, ");
		sqlStr.append("matriculaperiodo.codigo, unidadeensino.nome as nomeEmpresa from matricula ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sqlStr.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append("inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append("inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico ");
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.codigo = matriculaperiodo.unidadeensinocurso ");
		sqlStr.append("left join configuracoes on configuracoes.codigo = configuracaoacademico.configuracoes ");
		sqlStr.append("left join usuario on usuario.pessoa = pessoa.codigo ");		
		sqlStr.append("left join processomatriculacalendario on processomatriculacalendario.processomatricula = matriculaperiodo.processomatricula ");
		sqlStr.append("and processomatriculacalendario.curso = unidadeensinocurso.curso ");
		sqlStr.append("and processomatriculacalendario.turno = unidadeensinocurso.turno ");
		sqlStr.append("left join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sqlStr.append("where matriculaperiodo.codigo = ").append(matriculaPeriodoVO);

		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

	}

	private List<MatriculaPeriodoTurmaDisciplinaVO> montarDisciplinas(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> listaResultado = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		listaResultado = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		return listaResultado;
	}

	private List<DocumetacaoMatriculaVO> montarDocumentacaoMatricula(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		List<DocumetacaoMatriculaVO> listaResultado = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAluno(matriculaPeriodoVO.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
		listaResultado = montarSituacaoDocumentacaoMatricula(listaResultado);
		return listaResultado;
	}

	private MatriculaPeriodoVO consultarUltimaMatriculaPeriodoDoAluno(Integer codMatriculaPeriodo,  UsuarioVO usuarioVO) throws Exception {
		// List lista =
		// getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(matricula,
		// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
		// setMatriculaPeriodoVO((MatriculaPeriodoVO) lista.get(0));
		return getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(codMatriculaPeriodo.intValue(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
	}

	private ProcessoMatriculaCalendarioVO montarCalendarioProcessoMatricula(MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
		ProcessoMatriculaCalendarioVO resultado = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorMatriculaPeriodoUnidadeEnsinoCurso(matriculaPeriodoVO.getCodigo(), matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCurso().getCodigo(), matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getTurno().getCodigo(), matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		try {
			resultado.setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(resultado.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
		} catch (Exception e) {
			resultado.setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(new PeriodoLetivoAtivoUnidadeEnsinoCursoVO());
		}

		return resultado;
	}

	private List<DocumetacaoMatriculaVO> montarSituacaoDocumentacaoMatricula(List<DocumetacaoMatriculaVO> listaDocumentacaoMatricula) {
		for (DocumetacaoMatriculaVO documentacaoMatriculaVO : listaDocumentacaoMatricula) {
			if (documentacaoMatriculaVO.getSituacao().equals("OK")) {
				documentacaoMatriculaVO.setSituacao("Documento Entregue");
			} else {
				documentacaoMatriculaVO.setSituacao("Documento Pendente");
			}
		}
		return listaDocumentacaoMatricula;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DadosMatriculaRel");
	}

}
