/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaCompostaVO;
import negocio.comuns.academico.DisciplinaVO;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.InclusaoExclusaoDisciplinaInterfaceFacade;

/**
 * 
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class InclusaoExclusaoDisciplina extends ControleAcesso implements InclusaoExclusaoDisciplinaInterfaceFacade {

	protected static String idEntidade;

	public InclusaoExclusaoDisciplina() throws Exception {
		super();
		setIdEntidade("InclusaoExclusaoDisciplina");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<DisciplinaVO> listaExclusaoDisciplina, List<MatriculaVO> listaAluno, List<MatriculaPeriodoTurmaDisciplinaVO> listaInclusaoTurmaDisciplina, List<MatriculaPeriodoTurmaDisciplinaVO> listaExclusaoAposPersistir, Integer periodoLetivo, String tipoBusca, String ano, String semestre, Integer turmaConsulta, String nivelEducacionalCurso, UsuarioVO usuario) throws Exception {
		StringBuilder dados = new StringBuilder("Inclusão / Exclusão \n");
		dados.append(excluirDisciplinaAluno(listaExclusaoDisciplina, listaAluno, usuario));
		dados.append(incluirDisciplinaAluno(listaAluno, listaInclusaoTurmaDisciplina, listaExclusaoDisciplina, periodoLetivo, tipoBusca, ano, semestre, turmaConsulta, nivelEducacionalCurso,  usuario));
//		getFacadeFactory().getLogInclusaoExclusaoFacade().realizarRegistroLogInclusaoExclusao(dados.toString(), usuario);
	}

	/**
	 * Método responsável por excluir o Histórico e a
	 * MatriculaPeriodoTurmaDisciplina do aluno; 1º Percorre a lista de Alunos
	 * para excluir a disciplina(caso haja) do aluno; 2º Percorre a lista de
	 * disciplinas que podem ser exluidas, caso tenha sido marcada o checkBox;
	 * 3º Mando excluir o Histórico e consequentemente a
	 * MatriculaPeriodoTurmaDisciplina;
	 * 
	 * @param listaExclusaoDisciplina
	 * @param listaAluno
	 * @throws Exception
	 * @Carlos
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public String excluirDisciplinaAluno(List<DisciplinaVO> listaExclusaoDisciplina, List<MatriculaVO> listaAluno, UsuarioVO usuario) throws Exception {
		StringBuilder dadosExcluidos = new StringBuilder("");
		if (!listaExclusaoDisciplina.isEmpty()) {
			dadosExcluidos = new StringBuilder("Exclusão \n");
			excluir(getIdEntidade(), true, usuario);
		}
//		Integer index = 0;
//		for (MatriculaVO obj : listaAluno) {
//			if (!listaExclusaoDisciplina.isEmpty()) {
//				dadosExcluidos.append("Aluno -> ").append(obj.getMatricula()).append(" \n");
//			}
//			for (DisciplinaVO disciplina : listaExclusaoDisciplina) {
//				if (disciplina.getExcluirDisciplina()) {
//					Boolean possuiDisciplinaCursando = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacao(obj.getMatricula(), disciplina.getCodigo(), "CS", usuario);
//					if (possuiDisciplinaCursando) {
//						dadosExcluidos.append("Disciplina: ").append(disciplina.getNome()).append(" - Nota: ").append(getFacadeFactory().getHistoricoFacade().consultarMediaAlunoDisciplina(obj.getMatricula(), disciplina.getCodigo())).append("\n");
//						getFacadeFactory().getHistoricoFacade().excluirPorMatriculaPeriodoTurmaDisciplina(obj.getMatricula(), disciplina.getCodigo(), usuario);
//						listaExclusaoDisciplina.remove(index);
//
//						if (disciplina.getDisciplinaComposta()) {
//							List<DisciplinaCompostaVO> listaDisciplinaCompostaVOs = getFacadeFactory().getDisciplinaCompostaFacade().consultarDisciplinaComposta(disciplina.getCodigo(), false, usuario);
//							for (DisciplinaCompostaVO disciplinaCompostaVO : listaDisciplinaCompostaVOs) {
//								dadosExcluidos.append("Disciplina Composta: ").append(disciplinaCompostaVO.getCompostaVO().getNome()).append(" - Nota: ").append(getFacadeFactory().getHistoricoFacade().consultarMediaAlunoDisciplina(obj.getMatricula(), disciplinaCompostaVO.getCompostaVO().getCodigo())).append("\n");
//								getFacadeFactory().getHistoricoFacade().excluirPorMatriculaPeriodoTurmaDisciplina(obj.getMatricula(), disciplinaCompostaVO.getCompostaVO().getCodigo(), usuario);
//							}
//						}
//					}
//				}
//				
//				index++;
//			}
//		}
		return dadosExcluidos.toString();
	}

	/**
	 * Método responsável por incluir uma nova MatriculaPeriodoTurmaDisciplina e
	 * um novo historico caso seja adicionada uma disciplina. 1º percorro a
	 * lista de alunos(incluir a disciplina para todos alunos da lista) 2º
	 * verifico se já existe essa disciplina na lista de disciplinas de
	 * exclusão; 3º inicializo os dados da MatriculaPeriodoTurmaDisciplina para
	 * a inclusão; 4º Incluo a MatriculaPeriodoTurmaDisciplina e
	 * consequentemente o Historico;
	 * 
	 * @param listaHistoricoAluno
	 * @param listaInclusaoTurmaDisciplina
	 * @param listaDisciplinaExcluida
	 * @throws Exception
	 *             Caso exista a disciplina na lista de exclusão mandar uma msg
	 *             ao usuário.
	 * @Autor Carlos
	 */
	public String incluirDisciplinaAluno(List<MatriculaVO> listaAluno, List<MatriculaPeriodoTurmaDisciplinaVO> listaInclusaoTurmaDisciplina, List<DisciplinaVO> listaDisciplinaExcluida, Integer periodoLetivo, String tipoBusca, String ano, String semestre, Integer turmaConsulta, String nivelEducacionalCurso, UsuarioVO usuario) throws Exception {
		StringBuilder dadosIncluidos = new StringBuilder("");
		if (!listaInclusaoTurmaDisciplina.isEmpty()) {
			dadosIncluidos = new StringBuilder("Inclusão \n");
		}

		if (!listaInclusaoTurmaDisciplina.isEmpty()) {
			incluir(getIdEntidade(), true, usuario);
		}
		Boolean duplicidadeDisciplinaVerificada = Boolean.FALSE;
		Boolean disciplinaNoHistorico = Boolean.FALSE;
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina = new MatriculaPeriodoTurmaDisciplinaVO();
		for (MatriculaVO obj : listaAluno) {
			if (!listaInclusaoTurmaDisciplina.isEmpty()) {
				dadosIncluidos.append("Aluno -> ").append(obj.getMatricula()).append(" \n");
			}
			for (MatriculaPeriodoTurmaDisciplinaVO mptd : listaInclusaoTurmaDisciplina) {
				if (!duplicidadeDisciplinaVerificada) {
					for (DisciplinaVO disciplinaVO : listaDisciplinaExcluida) {
						if (mptd.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
							throw new Exception("Disciplina já está incluida para a Matrícula: " + obj.getMatricula());
						}
					}
				}
				duplicidadeDisciplinaVerificada = Boolean.TRUE;
				disciplinaNoHistorico = Boolean.FALSE;

				if (mptd.getDisciplina().getDisciplinaComposta()) {
					List<DisciplinaCompostaVO> listaDisciplinaCompostaVOs = getFacadeFactory().getDisciplinaCompostaFacade().consultarDisciplinaComposta(mptd.getDisciplina().getCodigo(), false, usuario);
					for (DisciplinaCompostaVO disciplinaCompostaVO : listaDisciplinaCompostaVOs) {
						disciplinaNoHistorico = getFacadeFactory().getHistoricoFacade().consultarSeDisciplinaEstaSendoCursadaOuAprovada(obj.getMatricula(), disciplinaCompostaVO.getCompostaVO().getCodigo(), usuario);
						if (!disciplinaNoHistorico) {
							matriculaPeriodoTurmaDisciplina = inicializarDadosMatriculaPeriodoTurmaDisciplina(obj, disciplinaCompostaVO.getCompostaVO().getCodigo(), mptd.getTurma().getCodigo(), periodoLetivo, tipoBusca, ano, semestre, turmaConsulta, nivelEducacionalCurso, usuario);
							if (matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo() != 0) {
								dadosIncluidos.append("Disciplina Composta: ").append(disciplinaCompostaVO.getCompostaVO().getNome()).append(" \n");
								getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluir(matriculaPeriodoTurmaDisciplina, obj.getGradeCurricularAtual(), usuario);
							}
						}

					}
				}
				disciplinaNoHistorico = getFacadeFactory().getHistoricoFacade().consultarSeDisciplinaEstaSendoCursadaOuAprovada(obj.getMatricula(), mptd.getDisciplina().getCodigo(), usuario);
				if (!disciplinaNoHistorico) {
					matriculaPeriodoTurmaDisciplina = inicializarDadosMatriculaPeriodoTurmaDisciplina(obj, mptd.getDisciplina().getCodigo(), mptd.getTurma().getCodigo(), periodoLetivo, tipoBusca, ano, semestre, turmaConsulta, nivelEducacionalCurso, usuario);
					if (matriculaPeriodoTurmaDisciplina.getCodigo() == 0 && matriculaPeriodoTurmaDisciplina.getMatriculaPeriodo() != 0) {
						dadosIncluidos.append("Disciplina: ").append(mptd.getDisciplina().getNome()).append(" \n");
						getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluir(matriculaPeriodoTurmaDisciplina, obj.getGradeCurricularAtual(), usuario);
					}
				}
			}
		}
		return dadosIncluidos.toString();
	}

	/**
	 * Método responsável por inicializar os dados da
	 * MatriculaPeriodoTurmaDisciplina para inclusão
	 * 
	 * @param obj
	 * @param mptd
	 * @Carlos
	 */
	public MatriculaPeriodoTurmaDisciplinaVO inicializarDadosMatriculaPeriodoTurmaDisciplina(MatriculaVO obj, Integer codigoDisciplina, Integer codigoTurmaIncluida, Integer periodoLetivo, String tipoBusca, String ano, String semestre, Integer turmaConsulta, String nivelEducacionalCurso,   UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVO matriculaPeriodo = new MatriculaPeriodoVO();
		if (tipoBusca.equals("BM")) {
			matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaPeriodoLetivoMatricula(obj.getMatricula(), periodoLetivo, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,  usuario);
		} else if (tipoBusca.equals("BT")) {
			matriculaPeriodo = (getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaMatriculaPeriodoPorMatriculaAnoSemestre(obj.getMatricula(), ano, semestre, turmaConsulta, nivelEducacionalCurso, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, ""));
		}

		if (matriculaPeriodo.getCodigo() == 0) {
			return new MatriculaPeriodoTurmaDisciplinaVO();
		}
		
		MatriculaPeriodoTurmaDisciplinaVO mptd = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaPeriodoDisciplina(matriculaPeriodo.getCodigo(), codigoDisciplina, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if(mptd == null || mptd.getCodigo() == null || mptd.getCodigo() == 0){
			mptd = new MatriculaPeriodoTurmaDisciplinaVO();
		}else{
			return mptd;
		}
		
		
		mptd.setMatricula(obj.getMatricula());
		mptd.getDisciplina().setCodigo(codigoDisciplina);
		mptd.setMatriculaPeriodo(matriculaPeriodo.getCodigo());
		mptd.getTurma().setCodigo(codigoTurmaIncluida);
		mptd.setAno(matriculaPeriodo.getAno());
		mptd.setSemestre(matriculaPeriodo.getSemestre());
		mptd.setDisciplinaIncluida(Boolean.TRUE);
		return mptd;
	}

	public MatriculaVO consultarAlunoPorMatricula(String matricula, Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception {
		MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matricula, codigoUnidadeEnsino, NivelMontarDados.BASICO, usuario);
		if (objAluno.getMatricula().equals("")) {
			throw new Exception("Aluno de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
		}
		return objAluno;
	}

	public List consultarAlunosPorMatricula(String matricula, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		if (matricula.equals("")) {
			throw new Exception(UteisJSF.internacionalizar("msg_InclusaoExclusaoDisciplina_Matricula"));
		}
		return getFacadeFactory().getMatriculaFacade().consultaRapidaPorUnicaMatricula(matricula, unidadeEnsino, false, usuario);
	}

	public void consultarTurmaPorChavePrimaria(TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuario) throws Exception {
		String campoConsulta = turmaVO.getIdentificadorTurma();
		if (campoConsulta.equalsIgnoreCase("")) {
			throw new Exception("Informe o identificador da turma que deseja consultar.");
		} else {
			turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(turmaVO, campoConsulta, usuario.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			ano = Uteis.getAnoDataAtual4Digitos();
			semestre = Uteis.getSemestreAtual();
		}
	}

	public List consultarAlunosPorTurma(TurmaVO turmaVO, CursoVO cursoApresentar, String ano, String semestre, Integer unidadeEnsino, String situacaoMatricula, UsuarioVO usuarioVO) throws Exception {
		if (turmaVO.getCodigo().intValue() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_InclusaoExclusaoDisciplina_Turma"));
		}
		if (turmaVO.getSubturma()) {
			turmaVO.setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurmaPrincipalSubturma(turmaVO.getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));
		}
		List<MatriculaVO> lista = getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaCursoAnoSemestre(turmaVO.getCodigo(), turmaVO.getCurso(), ano, semestre, unidadeEnsino, situacaoMatricula);
		cursoApresentar.setCodigo(turmaVO.getCurso().getCodigo());
		cursoApresentar.setNivelEducacional(turmaVO.getCurso().getNivelEducacional());
		return lista;
	}

	public List consultarAluno(String valorConsultaAluno, String campoConsultaAluno, UsuarioVO usuario) throws Exception {
		List objs = new ArrayList(0);
		if (valorConsultaAluno.equals("")) {
			throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
		}
		if (campoConsultaAluno.equals("matricula")) {
			MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(valorConsultaAluno, usuario.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, usuario);
			if (!obj.getMatricula().equals("")) {
				objs.add(obj);
				return objs;
			}
		}
		if (campoConsultaAluno.equals("nomePessoa")) {
			return getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(valorConsultaAluno, usuario.getUnidadeEnsinoLogado().getCodigo(), false, usuario);
		}
		if (campoConsultaAluno.equals("nomeCurso")) {
			return getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(valorConsultaAluno, usuario.getUnidadeEnsinoLogado().getCodigo(), false, usuario);
		}
		return new ArrayList(0);
	}

	/**
	 * Método responsável por consultar todas disciplinas ativas de todos alunos
	 * dentro da lista, caso a disciplina já esteja na lista de disciplinas ele
	 * so substiruirá a mesma.
	 * 
	 * @param listaHistoricoAlunos
	 * @throws Exception
	 * @Autor Carlos
	 */
	public List executarInsercaoDisciplinasPorListaMatricula(List<MatriculaVO> listaAlunos, Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception {
		if (listaAlunos.isEmpty()) {
			throw new Exception("Nenhum aluno encontrado!");
		}
		List<DisciplinaVO> objs = new ArrayList(0);
		consultarDisciplinasPorMatriculasESituacaoHistorico(listaAlunos, codigoUnidadeEnsino, objs, usuario);
		return objs;
	}

	public void consultarDisciplinasPorMatriculasESituacaoHistorico(List<MatriculaVO> listaAlunos, Integer codigoUnidadeEnsino, List<DisciplinaVO> objs, UsuarioVO usuario) throws Exception {
		List<DisciplinaVO> listaConsulta = getFacadeFactory().getDisciplinaFacade().consultaRapidaPorMatriculasSituacaoHistorico(listaAlunos, "CS", codigoUnidadeEnsino, false, usuario);
		for (DisciplinaVO disciplinaVO : listaConsulta) {
			adicionarObjsDisciplina(disciplinaVO, objs);
		}
	}

	public List executarInsercaoDisciplinasListaPorMatriculaPeriodoSituacaoAtiva(List<MatriculaVO> listaAlunos, Integer codigoUnidadeEnsino, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
		if (listaAlunos.isEmpty()) {
			throw new Exception("Nenhum aluno encontrado!");
		}
		List<DisciplinaVO> objs = new ArrayList(0);
		for (MatriculaVO matriculaVO : listaAlunos) {
			consultarDisciplinaPorMatriculaEMatriculaPeriodo(matriculaVO, codigoUnidadeEnsino, matriculaPeriodo, objs, usuario);
		}
		return objs;
	}

	public void consultarDisciplinaPorMatriculaEMatriculaPeriodo(MatriculaVO matriculaVO, Integer codigoUnidadeEnsino, Integer matriculaPeriodo, List<DisciplinaVO> objs, UsuarioVO usuario) throws Exception {
		List<DisciplinaVO> listaConsulta = consultarDisciplinaPorMatriculaEMatriculaPeriodo(matriculaVO.getMatricula(), codigoUnidadeEnsino, matriculaPeriodo, usuario);
		for (DisciplinaVO disciplinaVO : listaConsulta) {
			adicionarObjsDisciplina(disciplinaVO, objs);
		}
	}

	public void adicionarObjsDisciplina(DisciplinaVO disciplinaVO, List<DisciplinaVO> objs) {
		int index = 0;
		boolean adicionar = true;
		for (DisciplinaVO disciplinaVO1 : objs) {
			if (disciplinaVO1.getCodigo().equals(disciplinaVO.getCodigo())) {
				objs.set(index, disciplinaVO);
				adicionar = false;
				break;
			}
			index++;
		}
		if (adicionar) {
			objs.add(disciplinaVO);
		}

	}

	public List consultarDisciplinaPorMatriculaEMatriculaPeriodo(String matricula, Integer codigoUnidadeEnsino, Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultaRapidaPorMatriculaEMatriculaPeriodo(matricula, codigoUnidadeEnsino, matriculaPeriodo, false, usuario);
	}

	public List consultarDisciplinaIncluidaRich(HashMap<Integer, MatriculaPeriodoVO> hashMap, String campoConsultaDisciplinaIncluida, String valorConsultaDisciplinaIncluida, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		Integer[] varargs = new Integer[hashMap.keySet().size()];
		varargs = hashMap.keySet().toArray(varargs);

		if (valorConsultaDisciplinaIncluida.length() < 1) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
		}
		if (campoConsultaDisciplinaIncluida.equals("codigo")) {
			if (valorConsultaDisciplinaIncluida.equals("")) {
				valorConsultaDisciplinaIncluida = "0";
			}
			int valorInt = Integer.parseInt(valorConsultaDisciplinaIncluida);
			// return
			// getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplina(new
			// Integer(valorInt), unidadeEnsino, true,
			// Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDaGradePorCodigoOuNome(valorInt, "", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, varargs);
		}
		if (campoConsultaDisciplinaIncluida.equals("nome")) {
			// return
			// getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplina(valorConsultaDisciplinaIncluida,
			// unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDaGradePorCodigoOuNome(0, valorConsultaDisciplinaIncluida, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, varargs);
		}

		return new ArrayList(0);
	}

	public List consultarDisciplinaIncluidaRich(MatriculaPeriodoVO matriculaPeriodoVO, String campoConsultaDisciplinaIncluida, String valorConsultaDisciplinaIncluida, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		if (valorConsultaDisciplinaIncluida.length() < 1) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_informeUmParametro"));
		}
		if (campoConsultaDisciplinaIncluida.equals("codigo")) {
			if (valorConsultaDisciplinaIncluida.equals("")) {
				valorConsultaDisciplinaIncluida = "0";
			}
			int valorInt = Integer.parseInt(valorConsultaDisciplinaIncluida);
			// return
			// getFacadeFactory().getDisciplinaFacade().consultarPorCodigoDisciplina(new
			// Integer(valorInt), unidadeEnsino, true,
			// Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDaGradePorCodigoOuNome(valorInt, "", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, matriculaPeriodoVO.getGradeCurricular().getCodigo());
		}
		if (campoConsultaDisciplinaIncluida.equals("nome")) {
			// return
			// getFacadeFactory().getDisciplinaFacade().consultarPorNomeDisciplina(valorConsultaDisciplinaIncluida,
			// unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDaGradePorCodigoOuNome(0, valorConsultaDisciplinaIncluida, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, matriculaPeriodoVO.getGradeCurricular().getCodigo());
		}

		return new ArrayList(0);
	}

	public void validarDados(MatriculaPeriodoTurmaDisciplinaVO obj) throws Exception {
		if (obj.getDisciplina().getCodigo() == 0) {
			throw new Exception("Por favor, selecione uma DISCIPLINA.");
		}
		if (obj.getTurma().getCodigo() == 0) {
			throw new Exception("Turma não encontrada.");
		}
	}

	public void adicionarTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO obj, List listaTurmaDisciplinasIncluida) throws Exception {
		validarDados(obj);
		int index = 0;
		Iterator i = listaTurmaDisciplinasIncluida.iterator();

		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo()) && objExistente.getTurma().getCodigo().equals(obj.getTurma().getCodigo())) {
				listaTurmaDisciplinasIncluida.set(index, obj);
				return;
			}
			index++;
		}
		listaTurmaDisciplinasIncluida.add(obj);

	}

	/***
	 * Verifica se na programacao de aula existe algum horário de aula de outra
	 * disciplina é o mesmo horário da disciplina a ser incluída para o aluno.
	 * 
	 * @param matriculaPeriodo
	 * @param turmaInclusao
	 * @param disciplinaInclusao
	 * @throws Exception
	 */
	
	public void excluirObjsTurmaDisciplinaIncluidaVOs(MatriculaPeriodoTurmaDisciplinaVO obj, List listaTurmaDisciplinaIncluidaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> listaExclusaoAposPersistir) throws Exception {
		int index = 0;
		Iterator i = listaTurmaDisciplinaIncluidaVOs.iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO objExistente = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo()) && objExistente.getTurma().getCodigo().equals(obj.getTurma().getCodigo())) {
				listaExclusaoAposPersistir.add(obj);
				listaTurmaDisciplinaIncluidaVOs.remove(index);
				return;
			}
			index++;
		}
	}

	public List realizarMontagemListaPeriodoLetivo(MatriculaVO matriculaVO, UsuarioVO usuario, Boolean campoObrigatorio) throws Exception {
		List periodoLetivoVOs = new ArrayList(0);
		if (matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
			periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatriculaPorMatriculaPeriodo(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		} else {
			periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPorMatriculaPorMatriculaPeriodoSituacaoDiferenteAtiva(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		}
		if (periodoLetivoVOs.isEmpty()) {
			throw new Exception("Não foi encontrado nenhum período Letivo que possa permitir essa tarefa, por isso realize a Inclusão ou Exclusão de Disciplina pela tela de Matrícula!");
		}
		List<SelectItem> objs = new ArrayList<SelectItem>();
		int index = 0;
		boolean repetido = false;
		Iterator i = periodoLetivoVOs.iterator();
		if (!campoObrigatorio) {
			objs.add(new SelectItem("", ""));
		}
		while (i.hasNext()) {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
			index = 0;
			while (index < objs.size()) {
				if (objs.get(index).getValue().equals(obj.getPeriodoLetivo())) {
					repetido = true;
				}
				index++;
			}
			if (!repetido) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getPeriodoLetivo() + "°"));
			}
			repetido = false;
		}
		return objs;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return PeriodoLetivo.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.PeriodoLetivoInterfaceFacade#setIdEntidade
	 * (java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		PeriodoLetivo.idEntidade = idEntidade;
	}

	// Métodos usados pela tela de ProcessoInclusaoExclusaoDisciplinaMatricula
	/**
	 * Método responsável por consultar todas disciplinas ativas de todos alunos
	 * dentro da lista, caso a disciplina já esteja na lista de disciplinas ele
	 * so substiruirá a mesma.
	 * 
	 * @param listaHistoricoAlunos
	 * @throws Exception
	 * @Autor Carlos
	 */
	public List executarInsercaoDisciplinasListaPorMatriculaPeriodoSituacaoAtiva(List<MatriculaVO> listaAlunos, Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception {
		if (listaAlunos.isEmpty()) {
			throw new Exception("Nenhum aluno encontrado!");
		}
		List<DisciplinaVO> objs = new ArrayList(0);
		for (MatriculaVO matriculaVO : listaAlunos) {
			consultarDisciplinaPorMatriculaESituacaoMatriculaPeriodo(matriculaVO, codigoUnidadeEnsino, objs, usuario);
		}
		return objs;
	}

	public void consultarDisciplinaPorMatriculaESituacaoMatriculaPeriodo(MatriculaVO matriculaVO, Integer codigoUnidadeEnsino, List<DisciplinaVO> objs, UsuarioVO usuario) throws Exception {
		List<DisciplinaVO> listaConsulta = consultarDisciplinaPorMatriculaEMatriculaPeriodoAtiva(matriculaVO.getMatricula(), codigoUnidadeEnsino, usuario);
		for (DisciplinaVO disciplinaVO : listaConsulta) {
			adicionarObjsDisciplina(disciplinaVO, objs);
		}
	}

	public List consultarDisciplinaPorMatriculaEMatriculaPeriodoAtiva(String matricula, Integer codigoUnidadeEnsino, UsuarioVO usuario) throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultaRapidaPorMatriculaEMatriculaPeriodoAtiva(matricula, codigoUnidadeEnsino, false, usuario);

	}

	@Override
	public void executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplina,
			List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinasJaNaListaInclusao,
			List<DisciplinaVO> listaDisciplinasAtuais, TurmaVO turmaAtual, String semestre, String ano,
			UsuarioVO usuario) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
