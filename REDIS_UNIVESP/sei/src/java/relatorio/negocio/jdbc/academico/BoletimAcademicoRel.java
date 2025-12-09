package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.NotaVO;
import negocio.comuns.academico.PeriodoLetivoAtivoUnidadeEnsinoCursoVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoHistorico;
import relatorio.negocio.comuns.academico.BoletimAcademicoEnsinoMedioRelVO;
import relatorio.negocio.comuns.academico.BoletimAcademicoRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.interfaces.academico.BoletimAcademicoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class BoletimAcademicoRel extends SuperRelatorio implements BoletimAcademicoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public BoletimAcademicoRel() {
	}

	public void validarDados(MatriculaVO matriculaVO, TurmaVO turmaVO, String ano, String semestre, boolean filtrarPorAluno) throws ConsistirException {
		if (filtrarPorAluno) {
			if (matriculaVO == null || matriculaVO.getMatricula().equals("")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Boletim_matricula"));
			}
		} else {
			if (turmaVO == null || turmaVO.getCodigo().equals(0)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Boletim_turma"));
			}
			if (ano.trim().isEmpty() && turmaVO.getCurso().getPeriodicidade().equals("AN")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Boletim_matriculaPeriodoAno"));
			}
			if ((ano.trim().isEmpty() || semestre.trim().isEmpty()) && turmaVO.getCurso().getPeriodicidade().equals("SE")) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Boletim_matriculaPeriodoAnoSemestre"));
			}
		}
	}

	/**
	 * Método principal responsável por executar a criação do Relatório de
	 * Boletim Acadêmico, seguindo os parâmetros passados pelo usuário.
	 */
	@Override
	public List<BoletimAcademicoRelVO> criarObjeto(MatriculaVO matriculaVO, final String tipoLayout, final boolean filtrarPorAluno, final String anoSemestre, final TurmaVO turmaVO, final UnidadeEnsinoVO unidadeEnsinoVO, final ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, final Boolean apresentarDisciplinaComposta, final UsuarioVO usuarioVO, final boolean apresentarCampoAssinatura, final boolean apresentarQuantidadeFaltasAluno, final FuncionarioVO funcionarioPrincipalVO, final CargoVO cargoFuncionarioPrincial, final FuncionarioVO funcionarioSecundarioVO, final CargoVO cargoFuncionarioSecundario, final BimestreEnum bimestre, final SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, final boolean apresentarApenasNotaMedia, final Boolean apresentarAprovadoPorAproveitamentoComoAprovado, final boolean apresentarCampoAssinaturaResponsavel, final boolean apresentarReprovadoPorFaltaComoReprovado, FiltroRelatorioAcademicoVO filtroAcademicoVO, final GradeCurricularVO gradeCurricularVO, boolean considerarSituacaoAcademicaHistorico, boolean apresentarCargaHorariaCursada, List<String> listaNotas, Boolean trazerAlunoTransferencia) throws Exception {
		final List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		final List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = new ArrayList<BoletimAcademicoRelVO>(0);
		String ano = "";
		String semestre = "";
		/**
		 * Responsável por definir o ano e semestre selecionado
		 */
		if (!anoSemestre.trim().isEmpty()) {
			ano += anoSemestre.substring(0, 4);
			if (anoSemestre.length() > 4) {
				semestre += anoSemestre.substring(4, 5);
			}
		}
		validarDados(matriculaVO, turmaVO, ano, semestre, filtrarPorAluno);
		if (filtrarPorAluno) {
			matriculaVOs.add(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaAnoSemestrePeriodoLetivoGradeCurricularBoletim(matriculaVO.getMatricula(), ano, semestre, 0, 0, unidadeEnsinoVO.getCodigo(), usuarioVO, bimestre, situacaoRecuperacaoNota));
		} else {
			if (!turmaVO.getSubturma() && turmaVO.getTurmaAgrupada()) {
				matriculaVOs.addAll(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaAnoSemestrePeriodoLetivoBoletim(turmaVO.getTurmaAgrupadaVOs(), 0, ano, semestre, unidadeEnsinoVO.getCodigo(), usuarioVO, bimestre, situacaoRecuperacaoNota, filtroAcademicoVO));
			} else {
				matriculaVOs.addAll(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurmaAnoSemestrePeriodoLetivoGradeCurricularBoletim(turmaVO, 0, ano, semestre, 0, 0, unidadeEnsinoVO.getCodigo(), usuarioVO, bimestre, situacaoRecuperacaoNota, filtroAcademicoVO));
			}
		}
		final Map<Integer, ConfiguracaoAcademicoVO> confAcad = new HashMap<Integer, ConfiguracaoAcademicoVO>(0);
		boolean usarParalelismo = true;
		if(usarParalelismo){
			if (matriculaVOs.isEmpty()) {
				throw new Exception("Não há resultados a serem exibidos neste relatório.");
			}
		final ConsistirException consistirException = new ConsistirException();
		ProcessarParalelismo.executar(0, matriculaVOs.size(), consistirException, new ProcessarParalelismo.Processo() {			
			@Override
			public void run(int i) {
				MatriculaVO matricula = matriculaVOs.get(i);
				String ano = "";
				String semestre = "";
				/**
				 * Responsável por definir o ano e semestre selecionado
				 */
				try {
					if (!anoSemestre.trim().isEmpty()) {
						ano += anoSemestre.substring(0, 4);
						if (anoSemestre.length() > 4) {
							semestre += anoSemestre.substring(4, 5); 
						}
					}

					if (tipoLayout.equals("BoletimAcademicoEnsinoMedioRel") || tipoLayout.equals("BoletimAcademicoEnsinoMedioRel2") 
							|| tipoLayout.equals("BoletimAcademico2Rel2Alunos") || tipoLayout.equals("FichaAluno2Rel2Alunos") || tipoLayout.equals("BoletimAcademico2Rel") 
							|| tipoLayout.equals("BoletimAcademicoEnsinoMedio2Rel") || tipoLayout.equals("FichaAluno2Rel") || tipoLayout.equals("FichaIndividualRel") 
							|| tipoLayout.equals("BoletimAcademicoEnsinoFundamentalRel")) {
						boletimAcademicoRelVOs.addAll(criarObjetoEnsinoMedio(filtrarPorAluno, matricula, ano, semestre, tipoLayout, turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, apresentarDisciplinaComposta, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, situacaoRecuperacaoNota, apresentarApenasNotaMedia, confAcad, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarCampoAssinaturaResponsavel, apresentarReprovadoPorFaltaComoReprovado, gradeCurricularVO, considerarSituacaoAcademicaHistorico, apresentarCargaHorariaCursada, listaNotas, trazerAlunoTransferencia));
					} else if (tipoLayout.equals("FichaIndividualLayout2Rel") || tipoLayout.equals("FichaIndividualLayout2RetratoRel")) {
						boletimAcademicoRelVOs.addAll(criarObjetoFichaIndividualLayout2(filtrarPorAluno, matricula, ano, semestre, tipoLayout, turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, apresentarDisciplinaComposta, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, situacaoRecuperacaoNota, apresentarApenasNotaMedia, confAcad, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarCampoAssinaturaResponsavel, apresentarReprovadoPorFaltaComoReprovado, gradeCurricularVO, considerarSituacaoAcademicaHistorico, apresentarCargaHorariaCursada, listaNotas, trazerAlunoTransferencia));
					} else {
						boletimAcademicoRelVOs.addAll(criarObjeto(matricula, ano, semestre, turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, apresentarDisciplinaComposta, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, situacaoRecuperacaoNota, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel, gradeCurricularVO));
					}
				} catch (Exception e) {
					e.printStackTrace();
					consistirException.adicionarListaMensagemErro(e.getMessage());
				}
				
			}
		});
		}else{
			for (MatriculaVO matricula : matriculaVOs) {
				// if (tipoLayout.equals("BoletimAcademico2Rel")) {
				// boletimAcademicoRelVOs.addAll(criarObjetoLayout2(matricula, ano,
				// semestre, turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO,
				// apresentarDisciplinaComposta, usuarioVO,
				// apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno,
				// funcionarioPrincipalVO, cargoFuncionarioPrincial,
				// funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre,
				// situacaoRecuperacaoNota, apresentarApenasNotaMedia));
				// } else
				if (tipoLayout.equals("BoletimAcademicoEnsinoMedioRel") || tipoLayout.equals("BoletimAcademico2Rel") || tipoLayout.equals("BoletimAcademicoEnsinoMedio2Rel") || tipoLayout.equals("FichaAluno2Rel") || tipoLayout.equals("FichaIndividualRel") || tipoLayout.equals("BoletimAcademicoEnsinoFundamentalRel")) {
					boletimAcademicoRelVOs.addAll(criarObjetoEnsinoMedio(filtrarPorAluno, matricula, ano, semestre, tipoLayout, turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, apresentarDisciplinaComposta, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, situacaoRecuperacaoNota, apresentarApenasNotaMedia, confAcad, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarCampoAssinaturaResponsavel, apresentarReprovadoPorFaltaComoReprovado, gradeCurricularVO, considerarSituacaoAcademicaHistorico, apresentarCargaHorariaCursada, listaNotas, trazerAlunoTransferencia));
				} else {
					boletimAcademicoRelVOs.addAll(criarObjeto(matricula, ano, semestre, turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, apresentarDisciplinaComposta, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, situacaoRecuperacaoNota, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel, gradeCurricularVO));
				}
			}
		}
		Ordenacao.ordenarLista(boletimAcademicoRelVOs, "ordenacaoSemAcentuacaoNome");
		return boletimAcademicoRelVOs;
	}

	/**
	 * Método responsável por executar a criação do Relatório de Boletim
	 * Acadêmico do tipo Graduação respeitando os parâmetros passados pelo
	 * usuário
	 * 
	 * @param matriculaVO
	 * @param ano
	 * @param semestre
	 * @param turmaVO
	 * @param unidadeEnsinoVO
	 * @param configuracaoFinanceiroVO
	 * @param apresentarDisciplinaComposta
	 * @param usuarioVO
	 * @param apresentarCampoAssinatura
	 * @param apresentarQuantidadeFaltasAluno
	 * @param funcionarioPrincipalVO
	 * @param cargoFuncionarioPrincial
	 * @param funcionarioSecundarioVO
	 * @param cargoFuncionarioSecundario
	 * @return
	 * @throws Exception
	 */
	public List<BoletimAcademicoRelVO> criarObjeto(MatriculaVO matriculaVO, String ano, String semestre, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO, boolean apresentarCampoAssinatura, boolean apresentarQuantidadeFaltasAluno, FuncionarioVO funcionarioPrincipalVO, CargoVO cargoFuncionarioPrincial, FuncionarioVO funcionarioSecundarioVO, CargoVO cargoFuncionarioSecundario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, boolean apresentarApenasNotaMedia, boolean apresentarCampoAssinaturaResponsavel, GradeCurricularVO gradeCurricularVO) throws Exception {
		List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = new ArrayList<BoletimAcademicoRelVO>(0);
		List<HistoricoVO> anoSemestres = new ArrayList<HistoricoVO>(0);
		/**
		 * Responsável por definir o ano e semestre caso selecionado pelo
		 * usuário, senão é retornado uma consulta com todos os anos e semestres
		 * do aluno vinculados ao histórico, e assim criado o relatório
		 */
		if (!ano.trim().isEmpty()) {
			HistoricoVO obj = new HistoricoVO();
			obj.setAnoHistorico(ano);
			obj.setSemestreHistorico(semestre);
			anoSemestres.add(obj);
		} else {
			anoSemestres = getFacadeFactory().getHistoricoFacade().consultarSomenteAnoSemestreHistoricoPorMatriculaBoletimAcademicoRel(matriculaVO.getMatricula(), false, usuarioVO);
		}
		if (!anoSemestres.isEmpty()) {
			for (HistoricoVO hist : anoSemestres) {
				BoletimAcademicoRelVO obj = new BoletimAcademicoRelVO();
				montarDadosBoletimAcademicoRel(obj, matriculaVO, hist.getAnoHistorico(), hist.getSemestreHistorico(), null, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
				List<Integer> listaConfiguracoes = getFacadeFactory().getHistoricoFacade().consultarPorChavePrimariaDadosConfiguracaoAcademicaDadosMinimos(matriculaVO.getMatricula(), anoSemestres, usuarioVO);
				for (Integer configuracaoAcademica : listaConfiguracoes) {
					List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoBoletim(matriculaVO.getMatricula(), 0, hist.getAnoHistorico(), hist.getSemestreHistorico(), apresentarDisciplinaComposta, configuracaoAcademica, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO, bimestre, situacaoRecuperacaoNota, gradeCurricularVO.getCodigo());
					montarBoletimPorDisciplina(obj, historicoVOs);					
					montarDadosSituacaoFinal(obj, historicoVOs);
					boletimAcademicoRelVOs.add(obj);
					if (listaConfiguracoes.size() > 1) {
						obj = new BoletimAcademicoRelVO();
						montarDadosBoletimAcademicoRel(obj, matriculaVO, hist.getAnoHistorico(), hist.getSemestreHistorico(), turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
					}
				}
			}
		} else {
			BoletimAcademicoRelVO obj = new BoletimAcademicoRelVO();
			montarDadosBoletimAcademicoRel(obj, matriculaVO, "", "", null, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
			List<Integer> listaConfiguracoes = getFacadeFactory().getHistoricoFacade().consultarPorChavePrimariaDadosConfiguracaoAcademicaDadosMinimos(matriculaVO.getMatricula(), anoSemestres, usuarioVO);
			for (Integer configuracaoAcademica : listaConfiguracoes) {
				List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoBoletim(matriculaVO.getMatricula(), 0, "", "", apresentarDisciplinaComposta, configuracaoAcademica, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO, bimestre, situacaoRecuperacaoNota, gradeCurricularVO.getCodigo());
				montarBoletimPorDisciplina(obj, historicoVOs);							
				montarDadosSituacaoFinal(obj, historicoVOs);
				boletimAcademicoRelVOs.add(obj);
				if (listaConfiguracoes.size() > 1) {
					obj = new BoletimAcademicoRelVO();
					montarDadosBoletimAcademicoRel(obj, matriculaVO, "", "", turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
				}
			}
		}
		return boletimAcademicoRelVOs;
	}

	/**
	 * Método responsável por executar a criação do Relatório de Boletim
	 * Acadêmico do tipo Graduação respeitando os parâmetros passados pelo
	 * usuário
	 * 
	 * @param matriculaVO
	 * @param ano
	 * @param semestre
	 * @param turmaVO
	 * @param unidadeEnsinoVO
	 * @param configuracaoFinanceiroVO
	 * @param apresentarDisciplinaComposta
	 * @param usuarioVO
	 * @param apresentarCampoAssinatura
	 * @param apresentarQuantidadeFaltasAluno
	 * @param funcionarioPrincipalVO
	 * @param cargoFuncionarioPrincial
	 * @param funcionarioSecundarioVO
	 * @param cargoFuncionarioSecundario
	 * @return
	 * @throws Exception
	 */
	public List<BoletimAcademicoRelVO> criarObjetoLayout2(MatriculaVO matriculaVO, String ano, String semestre, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO, boolean apresentarCampoAssinatura, boolean apresentarQuantidadeFaltasAluno, FuncionarioVO funcionarioPrincipalVO, CargoVO cargoFuncionarioPrincial, FuncionarioVO funcionarioSecundarioVO, CargoVO cargoFuncionarioSecundario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, boolean apresentarApenasNotaMedia, boolean apresentarCampoAssinaturaResponsavel, PessoaVO responsavel, GradeCurricularVO gradeCurricularVO) throws Exception {
		List<BoletimAcademicoRelVO> lista = new ArrayList<BoletimAcademicoRelVO>(0);
		List<HistoricoVO> anoSemestres = new ArrayList<HistoricoVO>(0);
		/**
		 * Responsável por definir o ano e semestre caso selecionado pelo
		 * usuário, senão é retornado uma consulta com todos os anos e semestres
		 * do aluno vinculados ao histórico, e assim criado o relatório
		 */
		if (!ano.trim().isEmpty()) {
			HistoricoVO obj = new HistoricoVO();
			obj.setAnoHistorico(ano);
			obj.setSemestreHistorico(semestre);
			anoSemestres.add(obj);
		} else {
			anoSemestres = getFacadeFactory().getHistoricoFacade().consultarAnoSemestreHistoricoPorMatriculaBoletimAcademicoRel(matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), false, usuarioVO);
		}
		for (HistoricoVO hist : anoSemestres) {
			BoletimAcademicoRelVO obj = new BoletimAcademicoRelVO();
			montarDadosBoletimAcademicoRel(obj, matriculaVO, hist.getAnoHistorico(), hist.getSemestreHistorico(), turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
			List<Integer> listaConfiguracoes = getFacadeFactory().getHistoricoFacade().consultarPorChavePrimariaDadosConfiguracaoAcademicaDadosMinimos(matriculaVO.getMatricula(), anoSemestres, usuarioVO);
			for (Integer configuracaoAcademica : listaConfiguracoes) {
				List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoBoletim(matriculaVO.getMatricula(), turmaVO.getCodigo(), hist.getAnoHistorico(), hist.getSemestreHistorico(), apresentarDisciplinaComposta, configuracaoAcademica, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuarioVO, bimestre, situacaoRecuperacaoNota, gradeCurricularVO.getCodigo());
				if (!historicoVOs.isEmpty()) {
					montarBoletimPorDisciplina(obj, historicoVOs);
					montarHistoricoSeparadoPorCursoMatricula(obj, historicoVOs);
					lista.add(obj);
				}
			}
		}
		return lista;
	}

	/**
	 * Método responsável por executar a montagem dos historicos do boletim
	 * acadêmico separados por curso e matricula
	 * 
	 * @param obj
	 * @param historicoVOs
	 * @throws Exception
	 */
	public void montarHistoricoSeparadoPorCursoMatricula(BoletimAcademicoRelVO obj, List<HistoricoVO> historicoVOs) throws Exception {
		HashMap<String, Object> historicosSeparadosPorCursoMatricula = new HashMap<String, Object>();
		for (HistoricoVO historicoTemp : historicoVOs) {
			if (historicoTemp.getSituacao().equals("AE")) {
				historicoTemp.setSituacao("AP");
			} else if (historicoTemp.getSituacao().equals("CE")) {
				historicoTemp.setSituacao("CS");
			}
			for (NotaVO nota : historicoTemp.getListaNotas()) {
				nota.setDisciplina(historicoTemp.getDisciplina().getNome());
				nota.setFrequencia(historicoTemp.getFreguencia());
				nota.setMediaFinal(historicoTemp.getMediaFinal());
			}
			if (!historicosSeparadosPorCursoMatricula.containsKey(historicoTemp.getMatricula().getMatricula() + historicoTemp.getMatricula().getCurso().getNome())) {
				historicosSeparadosPorCursoMatricula.put(historicoTemp.getMatricula().getMatricula() + historicoTemp.getMatricula().getCurso().getNome(), historicoTemp);
			}
		}
		obj.setHistorico2Layout2(historicoVOs);
		Set<String> chaves = historicosSeparadosPorCursoMatricula.keySet();
		for (String matriculaCurso : chaves) {
			obj.getHistorico1Layout2().add((HistoricoVO) historicosSeparadosPorCursoMatricula.get(matriculaCurso));
		}
	}

	/**
	 * Método responsável por executar a criação do Relatório de Boletim
	 * Acadêmico do tipo Ensino Médio respeitando os parâmetros passados pelo
	 * usuário
	 * 
	 * @param matriculaVO
	 * @param ano
	 * @param semestre
	 * @param tipoLayout
	 * @param turmaVO
	 * @param unidadeEnsinoVO
	 * @param configuracaoFinanceiroVO
	 * @param apresentarDisciplinaComposta
	 * @param usuarioVO
	 * @param apresentarCampoAssinatura
	 * @param apresentarQuantidadeFaltasAluno
	 * @param funcionarioPrincipalVO
	 * @param cargoFuncionarioPrincial
	 * @param funcionarioSecundarioVO
	 * @param cargoFuncionarioSecundario
	 * @return
	 * @throws Exception
	 */
		public List<BoletimAcademicoRelVO> criarObjetoEnsinoMedio(Boolean filtrarPorAluno, MatriculaVO matriculaVO, String ano, String semestre, String tipoLayout, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO, boolean apresentarCampoAssinatura, boolean apresentarQuantidadeFaltasAluno, FuncionarioVO funcionarioPrincipalVO, CargoVO cargoFuncionarioPrincial, FuncionarioVO funcionarioSecundarioVO, CargoVO cargoFuncionarioSecundario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, boolean apresentarApenasNotaMedia, Map<Integer, ConfiguracaoAcademicoVO> confAcad, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarCampoAssinaturaResponsavel, boolean apresentarReprovadoPorFaltaComoReprovado, final GradeCurricularVO gradeCurricularVO, boolean considerarSituacaoAcademicaHistorico, boolean apresentarCargaHorariaCursada, List<String> listaNotas, Boolean trazerAlunoTransferencia) throws Exception {
		List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = new ArrayList<BoletimAcademicoRelVO>(0);
		List<HistoricoVO> anoSemestres = new ArrayList<HistoricoVO>(0);

		/**
		 * Responsável por definir o ano e semestre caso selecionado pelo
		 * usuário, senão é retornado uma consulta com todos os anos e semestres
		 * do aluno vinculados ao histórico, e assim criado o relatório
		 */
		if (!ano.trim().isEmpty()) {
			HistoricoVO obj = new HistoricoVO();
			obj.setAnoHistorico(ano);
			obj.setSemestreHistorico(semestre);
			anoSemestres.add(obj);
		} else {
			anoSemestres = getFacadeFactory().getHistoricoFacade().consultarSomenteAnoSemestreHistoricoPorMatriculaBoletimAcademicoRel(matriculaVO.getMatricula(), false, usuarioVO);
		}
		for (HistoricoVO hist : anoSemestres) {
			Map<String, Map<String, String>> mapMatriculaVOs = new HashMap<String, Map<String, String>>(0);
			List<Integer> listaConfiguracoes = getFacadeFactory().getHistoricoFacade().consultarPorChavePrimariaDadosConfiguracaoAcademicaDadosMinimos(matriculaVO.getMatricula(), anoSemestres, usuarioVO);
			List<BoletimAcademicoRelVO> boletimAluno = new ArrayList<BoletimAcademicoRelVO>(0);
			for (Integer configuracaoAcademica : listaConfiguracoes) {
				BoletimAcademicoRelVO obj = new BoletimAcademicoRelVO();
				montarDadosBoletimAcademicoRel(obj, matriculaVO, hist.getAnoHistorico(), hist.getSemestreHistorico(), null, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
				if (tipoLayout.equals("BoletimAcademicoEnsinoFundamentalRel")) {
					realizarConsultaRegistroPorMatriculaAnoSemestre(obj, hist.getAnoHistorico(), hist.getSemestreHistorico(), bimestre, new ArrayList<>(), usuarioVO);
				}				
				SqlRowSet rs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoBoletimEnsinoMedio(matriculaVO.getMatricula(), 0, hist.getAnoHistorico(), hist.getSemestreHistorico(), apresentarDisciplinaComposta, configuracaoAcademica, configuracaoFinanceiroVO, usuarioVO, bimestre, situacaoRecuperacaoNota, gradeCurricularVO.getCodigo(), listaNotas, trazerAlunoTransferencia);
				Integer condConfiguracaoAcademicoFechamentoPeriodoLetivo = 0;
				boolean existeDisciplina = false;
				while (rs.next()) {
					if(!Uteis.isAtributoPreenchido(condConfiguracaoAcademicoFechamentoPeriodoLetivo)) {
						condConfiguracaoAcademicoFechamentoPeriodoLetivo = rs.getInt("configuracaoAcademicoFechamentoPeriodoLetivo");
					}
					obj.setTurno(rs.getString("nometurno_matriculaperiodo"));
					try {
						existeDisciplina = true;
						if (tipoLayout.equals("BoletimAcademicoEnsinoFundamentalRel")) {
							montarBoletimPorDisciplinaEnsinoFundamentalI(rs, obj, bimestre, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
						} else if (tipoLayout.equals("FichaIndividualRel")) {
							montarBoletimPorDisciplinaIndividualAluno(rs, obj, bimestre, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado, apresentarCargaHorariaCursada);
						} else if (tipoLayout.equals("BoletimAcademico2Rel")) {
							montarBoletimAcademico2Rel(rs, obj, bimestre, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
						} else if (tipoLayout.equals("BoletimAcademicoEnsinoMedioRel") || tipoLayout.equals("BoletimAcademicoEnsinoMedioRel2")) {
							montarBoletimPorDisciplinaEnsinoMedio(rs, obj, bimestre, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
						} else if (tipoLayout.equals("FichaAluno2Rel") || tipoLayout.equals("FichaAluno2Rel2Alunos")) {
							montarBoletimPorDisciplinaFichaAluno(rs, obj, bimestre, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado, apresentarCargaHorariaCursada);
						} else {
							montarBoletimPorDisciplinaEnsinoMedioLayout2(rs, obj, bimestre, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
						}
					} catch (Exception e) {
						throw e;
					}
				}
				if (tipoLayout.equals("FichaAluno2Rel") || tipoLayout.equals("FichaAluno2Rel2Alunos")) {
					preencherCargaHorariaTotal(obj, "C.H.");
				} else if (tipoLayout.equals("FichaIndividualRel")) {
					preencherCargaHorariaTotal(obj, "Carga Horária");
				}
				if (existeDisciplina) {
					ConfiguracaoAcademicoVO conf = null;
					if (Uteis.isAtributoPreenchido(condConfiguracaoAcademicoFechamentoPeriodoLetivo) && confAcad.containsKey(condConfiguracaoAcademicoFechamentoPeriodoLetivo)) {
						conf = confAcad.get(condConfiguracaoAcademicoFechamentoPeriodoLetivo);
					} else {
						if(!Uteis.isAtributoPreenchido(condConfiguracaoAcademicoFechamentoPeriodoLetivo)) {
							condConfiguracaoAcademicoFechamentoPeriodoLetivo = matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo();
							if (condConfiguracaoAcademicoFechamentoPeriodoLetivo == 0) {
								condConfiguracaoAcademicoFechamentoPeriodoLetivo = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, usuarioVO).getConfiguracaoAcademico().getCodigo();
							}
						}
						conf = getAplicacaoControle().carregarDadosConfiguracaoAcademica(condConfiguracaoAcademicoFechamentoPeriodoLetivo);
						confAcad.put(condConfiguracaoAcademicoFechamentoPeriodoLetivo, conf);
					}
					matriculaVO.getCurso().setConfiguracaoAcademico(conf);
					
					Map<String, Map<String, String>> mapMatricula = realizarGeracaoMapMatriculaDisciplinaEnsinoMedio(obj.getBoletimAcademicoEnsinoMedioRelVOs(), considerarSituacaoAcademicaHistorico);
					if(!mapMatriculaVOs.containsKey(matriculaVO.getMatricula())){
						mapMatriculaVOs = mapMatricula;
					}else{
						for(String key : mapMatricula.get(matriculaVO.getMatricula()).keySet()){						
							mapMatriculaVOs.get(matriculaVO.getMatricula()).put(key, mapMatricula.get(matriculaVO.getMatricula()).get(key));
						}
					}
					
					
					if (!obj.getBoletimAcademicoEnsinoMedioRelVOs().isEmpty()) {
						Ordenacao.ordenarLista(obj.getBoletimAcademicoEnsinoMedioRelVOs(), "ordenacao");
						obj.setConfiguracaoAcademico(configuracaoAcademica);
						boletimAcademicoRelVOs.add(obj);
						boletimAluno.add(obj);
					}
					if (listaConfiguracoes.size() > 1) {
						obj = new BoletimAcademicoRelVO();
						montarDadosBoletimAcademicoRel(obj, matriculaVO, hist.getAnoHistorico(), hist.getSemestreHistorico(), turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
					}
				}
			}
			
			Map<String, ConfiguracaoAcademicoVO> mapConfiguracao = new HashMap<String, ConfiguracaoAcademicoVO>() ;
			mapConfiguracao.put(matriculaVO.getMatricula(), matriculaVO.getCurso().getConfiguracaoAcademico());
			Map<String, SituacaoHistorico> mapMatriculaSituacaoFinal = getFacadeFactory().getHistoricoFacade().executarCalcularResultadoFinalAluno(mapMatriculaVOs, mapConfiguracao);
			for(BoletimAcademicoRelVO obj: boletimAluno){
				if (!mapMatriculaSituacaoFinal.isEmpty() && mapMatriculaSituacaoFinal.containsKey(obj.getMatricula())) {
					obj.setSituacaoFinal(mapMatriculaSituacaoFinal.get(obj.getMatricula()).getDescricao());
				}
			}
			mapMatriculaVOs = null;
			boletimAluno =  null;
			listaConfiguracoes =  null;
		}
		return boletimAcademicoRelVOs;
	}

	public List<BoletimAcademicoRelVO> criarObjetoFichaIndividualLayout2(Boolean filtrarPorAluno, MatriculaVO matriculaVO, String ano, String semestre, String tipoLayout, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean apresentarDisciplinaComposta, UsuarioVO usuarioVO, boolean apresentarCampoAssinatura, boolean apresentarQuantidadeFaltasAluno, FuncionarioVO funcionarioPrincipalVO, CargoVO cargoFuncionarioPrincial, FuncionarioVO funcionarioSecundarioVO, CargoVO cargoFuncionarioSecundario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, boolean apresentarApenasNotaMedia, Map<Integer, ConfiguracaoAcademicoVO> confAcad, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarCampoAssinaturaResponsavel, boolean apresentarReprovadoPorFaltaComoReprovado, final GradeCurricularVO gradeCurricularVO, boolean considerarSituacaoAcademicaHistorico, boolean apresentarCargaHorariaCursada, List<String> listaNotas, Boolean trazerAlunoTransferencia) throws Exception {
		List<BoletimAcademicoRelVO> boletimAcademicoRelVOs = new ArrayList<BoletimAcademicoRelVO>(0);
		List<HistoricoVO> anoSemestres = new ArrayList<HistoricoVO>(0);
		
		/**
		 * Responsável por definir o ano e semestre caso selecionado pelo
		 * usuário, senão é retornado uma consulta com todos os anos e semestres
		 * do aluno vinculados ao histórico, e assim criado o relatório
		 */
		if (!ano.trim().isEmpty()) {
			HistoricoVO obj = new HistoricoVO();
			obj.setAnoHistorico(ano);
			obj.setSemestreHistorico(semestre);
			anoSemestres.add(obj);
		} else {
			anoSemestres.addAll(getFacadeFactory().getHistoricoFacade().consultarSomenteAnoSemestreHistoricoPorMatriculaBoletimAcademicoRel(matriculaVO.getMatricula(), false, usuarioVO));
		}
		
		for (HistoricoVO hist : anoSemestres) {
			Map<String, Map<String, String>> mapMatriculaVOs = new HashMap<String, Map<String, String>>(0);
			List<Integer> listaConfiguracoes = getFacadeFactory().getHistoricoFacade().consultarPorChavePrimariaDadosConfiguracaoAcademicaDadosMinimos(matriculaVO.getMatricula(), anoSemestres, usuarioVO);
			List<BoletimAcademicoRelVO> boletimAluno = new ArrayList<BoletimAcademicoRelVO>(0);

			for (Integer configuracaoAcademica : listaConfiguracoes) {
				BoletimAcademicoRelVO obj = new BoletimAcademicoRelVO();
				montarDadosBoletimAcademicoRel(obj, matriculaVO, hist.getAnoHistorico(), hist.getSemestreHistorico(), null, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
				
				SqlRowSet rs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoLetivoBoletimEnsinoMedio(matriculaVO.getMatricula(), 0, hist.getAnoHistorico(), hist.getSemestreHistorico(), apresentarDisciplinaComposta, configuracaoAcademica, configuracaoFinanceiroVO, usuarioVO, bimestre, situacaoRecuperacaoNota, gradeCurricularVO.getCodigo(), listaNotas, trazerAlunoTransferencia);
				boolean existeDisciplina = false;
				while (rs.next()) {
					try {
						existeDisciplina = true;
						montarBoletimPorDisciplinaFichaFinanceiraLayout2(rs, obj, bimestre, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
					} catch (Exception e) {
						throw e;
					}
				}

				if (existeDisciplina) {
					Integer curso =  matriculaVO.getCurso().getCodigo();
					
					List<GradeCurricularVO> lista = getFacadeFactory().getGradeCurricularFacade().consultarPorCurso(matriculaVO.getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
					for (GradeCurricularVO grade : lista) {
						if (!grade.getSistemaAvaliacao().isEmpty()) {
							obj.setSistemaAvaliacao(grade.getSistemaAvaliacao());							
						}
						
					}

					ConfiguracaoAcademicoVO conf = null;
					if (confAcad.containsKey(curso)) {
						conf = confAcad.get(curso);
					} else {
						Integer codConf = matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo();
						if (codConf == 0) {
							codConf = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(curso, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, usuarioVO).getConfiguracaoAcademico().getCodigo();
						}
						conf = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(codConf, usuarioVO);
						confAcad.put(curso, conf);
					}
					matriculaVO.getCurso().setConfiguracaoAcademico(conf);
					
					Map<String, Map<String, String>> mapMatricula = realizarGeracaoMapMatriculaDisciplinaEnsinoMedio(obj.getBoletimAcademicoEnsinoMedioRelVOs(), considerarSituacaoAcademicaHistorico);
					if(!mapMatriculaVOs.containsKey(matriculaVO.getMatricula())){
						mapMatriculaVOs = mapMatricula;
					}else{
						for(String key : mapMatricula.get(matriculaVO.getMatricula()).keySet()){						
							mapMatriculaVOs.get(matriculaVO.getMatricula()).put(key, mapMatricula.get(matriculaVO.getMatricula()).get(key));
						}
					}

					if (!obj.getBoletimAcademicoEnsinoMedioRelVOs().isEmpty()) {
						Ordenacao.ordenarLista(obj.getBoletimAcademicoEnsinoMedioRelVOs(), "ordenacao");
						obj.setConfiguracaoAcademico(configuracaoAcademica);
						boletimAcademicoRelVOs.add(obj);
						boletimAluno.add(obj);
					}
					if (listaConfiguracoes.size() > 1) {
						obj = new BoletimAcademicoRelVO();
						montarDadosBoletimAcademicoRel(obj, matriculaVO, hist.getAnoHistorico(), hist.getSemestreHistorico(), turmaVO, unidadeEnsinoVO, configuracaoFinanceiroVO, usuarioVO, apresentarCampoAssinatura, apresentarQuantidadeFaltasAluno, funcionarioPrincipalVO, cargoFuncionarioPrincial, funcionarioSecundarioVO, cargoFuncionarioSecundario, bimestre, apresentarApenasNotaMedia, apresentarCampoAssinaturaResponsavel);
					}
				}
			}
			Map<String, ConfiguracaoAcademicoVO> mapConfiguracao = new HashMap<String, ConfiguracaoAcademicoVO>() ;
			mapConfiguracao.put(matriculaVO.getMatricula(), matriculaVO.getCurso().getConfiguracaoAcademico());
			Map<String, SituacaoHistorico> mapMatriculaSituacaoFinal = getFacadeFactory().getHistoricoFacade().executarCalcularResultadoFinalAluno(mapMatriculaVOs, mapConfiguracao);
			for(BoletimAcademicoRelVO obj: boletimAluno){
				if (!mapMatriculaSituacaoFinal.isEmpty() && mapMatriculaSituacaoFinal.containsKey(obj.getMatricula())) {
					obj.setSituacaoFinal(mapMatriculaSituacaoFinal.get(obj.getMatricula()).getDescricao());
				}
			}
			mapMatriculaVOs = null;
			boletimAluno =  null;
			listaConfiguracoes =  null;
		}
		return boletimAcademicoRelVOs;
	}

	/**
	 * Método responsável por executar a montagem do objeto principal do boletim
	 * acadêmico de acordo com os parâmetros passados pelo usuário
	 * 
	 * @param obj
	 * @param matriculaVO
	 * @param ano
	 * @param semestre
	 * @param turmaVO
	 * @param unidadeEnsinoVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @param apresentarCampoAssinatura
	 * @param apresentarQuantidadeFaltasAluno
	 * @param funcionarioPrincipalVO
	 * @param cargoFuncionarioPrincial
	 * @param funcionarioSecundarioVO
	 * @param cargoFuncionarioSecundario
	 * @throws Exception
	 */
	public void montarDadosBoletimAcademicoRel(BoletimAcademicoRelVO obj, MatriculaVO matriculaVO, String ano, String semestre, TurmaVO turmaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, boolean apresentarCampoAssinatura, boolean apresentarQuantidadeFaltasAluno, FuncionarioVO funcionarioPrincipalVO, CargoVO cargoFuncionarioPrincial, FuncionarioVO funcionarioSecundarioVO, CargoVO cargoFuncionarioSecundario, BimestreEnum bimestre, boolean apresentarApenasNotaMedia, boolean apresentarCampoAssinaturaResponsavel) throws Exception {
		CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, usuarioVO);
		obj.setNomeAluno(matriculaVO.getAluno().getNome());
		obj.setDataNascimento(matriculaVO.getAluno().getDataNasc_Apresentar());
		obj.setNaturalidade(matriculaVO.getAluno().getNaturalidade().getNome());
		obj.setUf(matriculaVO.getAluno().getNaturalidade().getEstado().getSigla());
		obj.setTurno(matriculaVO.getTurno().getNome());
		obj.setConfiguracaoAcademico(cursoVO.getConfiguracaoAcademico().getCodigo());
		obj.setMatricula(matriculaVO.getMatricula());
		obj.setNomeCurso(cursoVO.getNome());
		obj.setApresentarApenaNotaTipoMedia(apresentarApenasNotaMedia);
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			obj.setNomeTurma(turmaVO.getIdentificadorTurma());
		} else {
			MatriculaPeriodoVO matriculaPeriodoCorrespondenteVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarCodigoUnidadeEnsinoCursoDataPorMatriculaPeriodoLetivoSemestreAno(matriculaVO.getMatricula(), 0, semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
			if (matriculaPeriodoCorrespondenteVO != null && !matriculaPeriodoCorrespondenteVO.getCodigo().equals(0)) {
				obj.setNomeTurma(matriculaPeriodoCorrespondenteVO.getTurma().getIdentificadorTurma());
			} 
		}
		PeriodoLetivoVO periodoLetivoCorrespondenteVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorAnoSemestreMatriculaPeriodoEMatricula(matriculaVO.getMatricula(), ano, semestre, cursoVO.getPeriodicidade(), usuarioVO);
		if (periodoLetivoCorrespondenteVO != null && !periodoLetivoCorrespondenteVO.getCodigo().equals(0)) {
			obj.setPeriodoLetivo(periodoLetivoCorrespondenteVO.getDescricao());
		}
		obj.setAno(ano);
		obj.setSistemaAvaliacao(matriculaVO.getGradeCurricularAtual().getSistemaAvaliacao());
		obj.setObservacaoHistorico(matriculaVO.getObservacaoComplementarHistoricoAlunoVO().getObservacao());
		obj.setSemestre(semestre);
		obj.setCpf(matriculaVO.getAluno().getCPF());
		obj.setRg(matriculaVO.getAluno().getRG());
		obj.setOrgaoEmissor(matriculaVO.getAluno().getOrgaoEmissor());
		obj.setUfOrgaoEmissor(matriculaVO.getAluno().getEstadoEmissaoRG());
		obj.setSexo(matriculaVO.getAluno().getSexo());
		obj.setAdvertenciaVOs(getFacadeFactory().getAdvertenciaFacade().consultarAdvertenciaPorMatricula(matriculaVO.getMatricula(), ano, false, unidadeEnsinoVO.getCodigo(), usuarioVO));
		obj.setAutorizacao(cursoVO.getNrRegistroInterno());
		obj.setDataPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(matriculaVO.getUnidadeEnsino().getCidade().getNome(), new Date(), false));
		obj.setMensagemBoletimAcademico(consultarMensagemBoletimAcademico(matriculaVO));
		obj.setApresentarCampoAssinatura(apresentarCampoAssinatura);
		obj.setApresentarQuantidadeFaltasAluno(apresentarQuantidadeFaltasAluno);
		obj.setFuncionarioPrincipalVO(funcionarioPrincipalVO);
		obj.setCargoFuncionarioPrincipal(cargoFuncionarioPrincial);
		obj.setFuncionarioSecundarioVO(funcionarioSecundarioVO);
		obj.setCargoFuncionarioSecundario(cargoFuncionarioSecundario);
		montarDadosFiliacao(matriculaVO.getAluno().getCodigo(), obj, usuarioVO);
		montarDadosReconhecimento(matriculaVO.getAutorizacaoCurso().getCodigo(), cursoVO.getCodigo(), matriculaVO.getData(), obj);
		montarDadosDocumentacaoPendente(obj, matriculaVO.getMatricula(), usuarioVO);
		montarDadosPeriodoAvaliacao(obj, matriculaVO, ano, semestre, configuracaoFinanceiroVO, usuarioVO, bimestre);
		obj.setApresentarCampoAssinaturaResponsavel(apresentarCampoAssinaturaResponsavel);
	}

	/**
	 * Método responsável por executar a montagem da Filiação do objeto
	 * principal do boletim acadêmico
	 * 
	 * @param codigoAluno
	 * @param obj
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void montarDadosFiliacao(Integer codigoAluno, BoletimAcademicoRelVO obj, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("select pessoa.nome, filiacao.tipo from filiacao ");
		sql.append(" inner join pessoa on pessoa.codigo = filiacao.pais ");
		sql.append(" where filiacao.aluno = ").append(codigoAluno);
		sql.append(" and filiacao.tipo in ('PA', 'MA') ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			if (rs.getString("tipo").equals("PA")) {
				obj.setNomePai(rs.getString("nome"));
			} else {
				obj.setNomeMae(rs.getString("nome"));
			}
		}
	}

	/**
	 * Método reponsável por executar a montagem dos dados de reconhecimento do
	 * curso do objeto principal
	 * 
	 * @param codigoAutorizacaoCurso
	 * @param codigoCurso
	 * @param dataMatricula
	 * @param obj
	 * @throws Exception
	 */
	public void montarDadosReconhecimento(Integer codigoAutorizacaoCurso, Integer codigoCurso, Date dataMatricula, BoletimAcademicoRelVO obj) throws Exception {
		AutorizacaoCursoVO autorizacaoCurso = new AutorizacaoCursoVO();
		if (!codigoAutorizacaoCurso.equals(0)) {
			autorizacaoCurso = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorChavePrimaria(codigoAutorizacaoCurso, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		} else {
			autorizacaoCurso = getFacadeFactory().getAutorizacaoCursoFacade().consultarPorCursoDataVigenteMatricula(codigoCurso, dataMatricula, Uteis.NIVELMONTARDADOS_COMBOBOX);
		}
		obj.setReconhecimento(autorizacaoCurso.getNome());
	}

	/**
	 * Método responsável por executar a montagem dos dados da documentação
	 * pendente do aluno para o objeto principal
	 * 
	 * @param obj
	 * @param matricula
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void montarDadosDocumentacaoPendente(BoletimAcademicoRelVO obj, String matricula, UsuarioVO usuarioVO) throws Exception {
		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarPorSituacaoMatricula("PE", matricula, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO);
		String documentacaoPendente = "";
		for (DocumetacaoMatriculaVO documetacaoMatriculaVO : documetacaoMatriculaVOs) {
			documentacaoPendente += documetacaoMatriculaVO.getTipoDeDocumentoVO().getNome() + "; ";
		}
		obj.setDocumentacaoPendente(documentacaoPendente.toUpperCase());
	}

	/**
	 * Método responsável por executar a montagem do periodo de avaliação do
	 * objeto principal
	 * 
	 * @param obj
	 * @param matriculaVO
	 * @param ano
	 * @param semestre
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	public void montarDadosPeriodoAvaliacao(BoletimAcademicoRelVO obj, MatriculaVO matriculaVO, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, BimestreEnum bimestreEnum) throws Exception {
		MatriculaPeriodoVO matriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultarCodigoUnidadeEnsinoCursoDataPorMatriculaPeriodoLetivoSemestreAno(matriculaVO.getMatricula(), 0, semestre, ano, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuarioVO);
		if (matriculaPeriodo != null && !matriculaPeriodo.getCodigo().equals(0)) {
			
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivoUnidadeEnsinoCursoVO = getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoBimestrePorMatriculaPeriodoUnidadeEnsinoCurso(matriculaPeriodo.getCodigo(), matriculaPeriodo.getUnidadeEnsinoCurso(), usuarioVO);
//			ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorMatriculaPeriodoUnidadeEnsinoCurso(matriculaPeriodo.getCodigo(), matriculaPeriodo.getUnidadeEnsinoCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if ((bimestreEnum != null && bimestreEnum.equals(BimestreEnum.BIMESTRE_01)) 
					|| (bimestreEnum == null && periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataInicioPeriodoLetivoPrimeiroBimestre() != null && periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFimPeriodoLetivoPrimeiroBimestre() != null
					&& matriculaPeriodo.getData().before(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataInicioPeriodoLetivoPrimeiroBimestre()) && matriculaPeriodo.getData().after(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFimPeriodoLetivoPrimeiroBimestre()))) {
				obj.setPeriodoAvaliacao(BimestreEnum.BIMESTRE_01.getValorApresentar());
			} else if ((bimestreEnum != null && bimestreEnum.equals(BimestreEnum.BIMESTRE_02)) 
					|| (bimestreEnum == null && periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataInicioPeriodoLetivoSegundoBimestre() != null && periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFimPeriodoLetivoSegundoBimestre() != null
					&& matriculaPeriodo.getData().before(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataInicioPeriodoLetivoSegundoBimestre()) && matriculaPeriodo.getData().after(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFimPeriodoLetivoSegundoBimestre()))) {
				obj.setPeriodoAvaliacao(BimestreEnum.BIMESTRE_02.getValorApresentar());
			} else if ((bimestreEnum != null && bimestreEnum.equals(BimestreEnum.BIMESTRE_03)) 
					|| (bimestreEnum == null && periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataInicioPeriodoLetivoTerceiroBimestre() != null && periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFimPeriodoLetivoTerceiroBimestre() != null
					&& matriculaPeriodo.getData().before(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataInicioPeriodoLetivoTerceiroBimestre()) && matriculaPeriodo.getData().after(periodoLetivoAtivoUnidadeEnsinoCursoVO.getDataFimPeriodoLetivoTerceiroBimestre()))) {
				obj.setPeriodoAvaliacao(BimestreEnum.BIMESTRE_03.getValorApresentar());
			} else {
				obj.setPeriodoAvaliacao("Final");
			}
		}
	}

	/**
	 * Método responsavel por consultar a mensagem que aparecerá no objeto
	 * principal
	 * 
	 * @param matriculaVO
	 * @return
	 * @throws Exception
	 */
	private String consultarMensagemBoletimAcademico(MatriculaVO matriculaVO) throws Exception {
		CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarCursoPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		if (Uteis.isAtributoPreenchido(cursoVO.getConfiguracaoAcademico())) {
			ConfiguracoesVO configuracoesVO = getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(cursoVO.getConfiguracaoAcademico().getConfiguracoesVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null);
			if (Uteis.isAtributoPreenchido(configuracoesVO) && configuracoesVO.getMensagemBoletimAcademico().length() > 200) {
				return configuracoesVO.getMensagemBoletimAcademico();
			}
		}
		return "";
	}

	/**
	 * Método responsavel por executar a montagem do das notas do boletim
	 * acadêmico
	 * 
	 * @param obj
	 * @param historicoVOs
	 * @throws Exception
	 */
	private void montarBoletimPorDisciplina(BoletimAcademicoRelVO obj, List<HistoricoVO> historicoVOs) throws Exception {
		int contador = 1;
		for (HistoricoVO historicoVO : historicoVOs) {
			if (historicoVO.getSituacao().equals(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor())) {
				historicoVO.setSituacao(SituacaoHistorico.APROVADO.getValor());
			}
			if (historicoVO.getSituacao().equals(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor()) || historicoVO.getSituacao().equals(SituacaoHistorico.CURSANDO_POR_CORRESPONDENCIA.getValor())) {
				historicoVO.setSituacao(SituacaoHistorico.CURSANDO.getValor());
			}
			if (contador % 2 == 0) {
				historicoVO.montaNotas(obj.getApresentarApenaNotaTipoMedia(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula());
				obj.getHistoricoAux().add(historicoVO);
			} else {
				historicoVO.montaNotas(obj.getApresentarApenaNotaTipoMedia(), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula());
				obj.getHistorico().add(historicoVO);
			}
			contador++;
		}
	}

	private int montarDadosNotasBoletimAcademico(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, String nomeDisciplina, String matricula, boolean parteDiversificada, boolean montarApenasMedias, int qtdeCasaDecimal, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = null;		
		int x = 1;		
		for (x = 1; x <= 40; x++) {			    
			if (rs.getBoolean("utilizarNota" + x) && rs.getBoolean("apresentarNota" + x + "Boletim") && (montarApenasMedias == false || (montarApenasMedias == true && rs.getBoolean("nota" + x + "MediaFinal")))) {
				BimestreEnum bimestreEnum2 = BimestreEnum.valueOf(rs.getString("bimestreNota" + x));				
				if (bimestreEnum == null || bimestreEnum.getOrdemApresentar() >= bimestreEnum2.getOrdemApresentar()) {
					boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
					boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
					boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
					boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
					boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
					boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
					boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
					boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(bimestreEnum2.getValorResumidoApresentar());
					boletimAcademicoEnsinoMedioRelVO.setNomeBimestreApresentar(bimestreEnum2.getValorApresentar());
					boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(bimestreEnum2.getOrdemApresentar());
					boletimAcademicoEnsinoMedioRelVO.setTituloNota(rs.getString("tituloNotaApresentar" + x));
					boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x);
					boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
					if (rs.getBoolean("utilizarNota" + x + "PorConceito")) {
						boletimAcademicoEnsinoMedioRelVO.setNota(rs.getString("notaConceito" + x));
						boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(0.0);
					} else {
						if (rs.getObject("nota" + x) == null) {
							boletimAcademicoEnsinoMedioRelVO.setNota("");
						} else {
							boletimAcademicoEnsinoMedioRelVO.setNota(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(rs.getDouble("nota" + x), qtdeCasaDecimal));
						}
						boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(rs.getDouble("nota" + x));
						if (boletimAcademicoEnsinoMedioRelVO.getNota().trim().isEmpty() && !rs.getBoolean("nota" + x + "Lancada")) {
							boletimAcademicoEnsinoMedioRelVO.setNota("----");
						}
					}
					obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
				}
			}
		}
		return x;
	}

	private int montarDadosFaltaBimestralBoletimAcademico(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, String nomeDisciplina, String matricula, boolean parteDiversificada, String tituloFalta1Bimestre, String tituloFalta2Bimestre, String tituloFalta3Bimestre, String tituloFalta4Bimestre, int x, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado, boolean apresentarTituloBimestreCompleto) throws Exception {
		/**
		 * Caso não exista registros na tabela frequenciaAula e utilizado os registros da tabela histórico.
		 */
		Map<BimestreEnum, Integer> faltaBimestres = montarDadosMapFaltasBimestre(obj.getMatricula(), rs, usuarioVO);
		BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
		boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
		boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
		boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
		boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
		boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
		boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
		boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
		if(apresentarTituloBimestreCompleto) {
			boletimAcademicoEnsinoMedioRelVO.setNomeBimestreApresentar(BimestreEnum.BIMESTRE_01.getValorApresentar());
		}
		boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.BIMESTRE_01.getValorResumidoApresentar());		
		boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.BIMESTRE_01.getOrdemApresentar());
		boletimAcademicoEnsinoMedioRelVO.setTituloNota(tituloFalta1Bimestre);
		boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01)));
		boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01)));
		boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
		obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		if (bimestreEnum == null || bimestreEnum.getOrdemApresentar() >= BimestreEnum.BIMESTRE_02.getOrdemApresentar()) {
			boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
			boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
			boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
			boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
			boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
			boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
			boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.BIMESTRE_02.getValorResumidoApresentar());
			if(apresentarTituloBimestreCompleto) {
				boletimAcademicoEnsinoMedioRelVO.setNomeBimestreApresentar(BimestreEnum.BIMESTRE_02.getValorApresentar());
			}
			boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.BIMESTRE_02.getOrdemApresentar());
			boletimAcademicoEnsinoMedioRelVO.setTituloNota(tituloFalta2Bimestre);
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_02)));
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_02)));
			boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);

			obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		}

		if (bimestreEnum == null || bimestreEnum.getOrdemApresentar() >= BimestreEnum.BIMESTRE_03.getOrdemApresentar()) {
			boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
			boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
			boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
			boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
			boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
			boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
			boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.BIMESTRE_03.getValorResumidoApresentar());
			boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.BIMESTRE_03.getOrdemApresentar());
			if(apresentarTituloBimestreCompleto) {
				boletimAcademicoEnsinoMedioRelVO.setNomeBimestreApresentar(BimestreEnum.BIMESTRE_03.getValorApresentar());
			}
			boletimAcademicoEnsinoMedioRelVO.setTituloNota(tituloFalta3Bimestre);
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_03)));
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_03)));
			boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
			obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		}

		if (bimestreEnum == null || bimestreEnum.getOrdemApresentar() >= BimestreEnum.BIMESTRE_04.getOrdemApresentar()) {
			boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
			boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
			boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
			boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
			boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
			boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
			boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.BIMESTRE_04.getValorResumidoApresentar());
			if(apresentarTituloBimestreCompleto) {
				boletimAcademicoEnsinoMedioRelVO.setNomeBimestreApresentar(BimestreEnum.BIMESTRE_04.getValorApresentar());
			}
			boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.BIMESTRE_04.getOrdemApresentar());
			boletimAcademicoEnsinoMedioRelVO.setTituloNota(tituloFalta4Bimestre);
			//boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(rs.getInt("faltaQuartoBimestre")));
			//boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(rs.getInt("faltaQuartoBimestre")));
			
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_04)));
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_04)));
			
			boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
			obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		}

		return x;
	}

	private int montarDadosMediaFinalBoletimAcademico(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, String nomeDisciplina, String matricula, boolean parteDiversificada, String titulo, int qtdeCasaDecimal, int x, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = null;
		if (bimestreEnum == null || bimestreEnum.getOrdemApresentar() >= BimestreEnum.BIMESTRE_04.getOrdemApresentar()) {
			boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
			boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
			boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
			boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
			boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
			boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
			boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
			boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.RESUMO_FINAL.getValorResumidoApresentar());
			boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.RESUMO_FINAL.getOrdemApresentar());
			boletimAcademicoEnsinoMedioRelVO.setTituloNota(titulo);
			boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
			if (rs.getInt("mediaFinalConceito") > 0) {
				boletimAcademicoEnsinoMedioRelVO.setNota(rs.getString("mediaConceito"));
				boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(0.0);
			} else if (rs.getBoolean("utilizaNotaFinalConceito")) {
				boletimAcademicoEnsinoMedioRelVO.setNota(rs.getString("notaFinalConceito"));
				boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(0.0);
			} else {
				if (rs.getObject("mediaFinal") == null) {
					boletimAcademicoEnsinoMedioRelVO.setNota("");
				} else {
					boletimAcademicoEnsinoMedioRelVO.setNota(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(rs.getDouble("mediaFinal"), qtdeCasaDecimal));
				}
				boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(rs.getDouble("mediaFinal"));
				if (boletimAcademicoEnsinoMedioRelVO.getNota().trim().isEmpty()) {
					boletimAcademicoEnsinoMedioRelVO.setNota("----");
				}
			}
			// boletimAcademicoEnsinoMedioRelVO.setSituacao("Sit.");
			obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		}
		return x;
	}

	private int montarDadosTotalFaltaBoletimAcademico(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, String nomeDisciplina, String matricula, boolean parteDiversificada, String titulo, int x, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
		boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
		boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
		boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
		boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
		boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
		boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
		boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
		boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.RESUMO_FINAL.getValorResumidoApresentar());
		boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.RESUMO_FINAL.getOrdemApresentar());
		boletimAcademicoEnsinoMedioRelVO.setTituloNota(titulo);
		
		/**
		 * Caso não exista registros na tabela frequenciaAula e utilizado os registros da tabela histórico.
		 */
		Integer totalFalta = getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoBoletimAcademico(obj.getMatricula(), rs.getInt("disciplina.codigo"), rs.getString("matriculaperiodo.semestre"), rs.getString("matriculaperiodo.ano"), rs.getInt("turma.codigo"), false, usuarioVO);
		if (!Uteis.isAtributoPreenchido(totalFalta)) {
			totalFalta = rs.getInt("totalFalta");
		}
		Map<BimestreEnum, Integer> faltaBimestres = montarDadosMapFaltasBimestre(obj.getMatricula(), rs, usuarioVO);
		if (bimestreEnum == null || bimestreEnum.equals(BimestreEnum.BIMESTRE_04)) {
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(totalFalta));
		} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_01)) {
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01)));
		} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_02)) {
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01) + faltaBimestres.get(BimestreEnum.BIMESTRE_02)));
		} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_03)) {
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01) + faltaBimestres.get(BimestreEnum.BIMESTRE_02) + faltaBimestres.get(BimestreEnum.BIMESTRE_03)));
		}

		if (bimestreEnum == null || bimestreEnum.equals(BimestreEnum.BIMESTRE_04)) {
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(totalFalta));
		} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_01)) {
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01)));
		} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_02)) {
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01) + faltaBimestres.get(BimestreEnum.BIMESTRE_02)));
		} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_03)) {
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(Double.valueOf(faltaBimestres.get(BimestreEnum.BIMESTRE_01) + faltaBimestres.get(BimestreEnum.BIMESTRE_02) + faltaBimestres.get(BimestreEnum.BIMESTRE_03)));
		}

		boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
		obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		return x;
	}

	private int montarDadosSituacaoHistoricoBoletimAcademico(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, String nomeDisciplina, String matricula, boolean parteDiversificada, String titulo, int x, boolean apresentarSiglaSituacao, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
		boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
		boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
		boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
		boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
		boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
		boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
		boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
		boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.RESUMO_FINAL.getValorResumidoApresentar());
		boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.RESUMO_FINAL.getOrdemApresentar());
		boletimAcademicoEnsinoMedioRelVO.setTituloNota(titulo);
		boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
		if (!apresentarSiglaSituacao) {
			boletimAcademicoEnsinoMedioRelVO.setNota(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado).getDescricao());
		} else {
			boletimAcademicoEnsinoMedioRelVO.setNota(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado).getValor());
		}
		obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		return x;
	}

	private int montarDadosFrequenciaBoletimAcademico(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, String nomeDisciplina, String matricula, boolean parteDiversificada, String titulo, int x, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		if (bimestreEnum == null || bimestreEnum.getOrdemApresentar() >= BimestreEnum.BIMESTRE_04.getOrdemApresentar()) {
			BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
			boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
			boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
			boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
			boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
			boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
			boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
			boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.RESUMO_FINAL.getValorResumidoApresentar());
			boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.RESUMO_FINAL.getOrdemApresentar());
			boletimAcademicoEnsinoMedioRelVO.setTituloNota(titulo);
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(rs.getDouble("freguencia")));
			if (boletimAcademicoEnsinoMedioRelVO.getNota().trim().isEmpty()) {
				boletimAcademicoEnsinoMedioRelVO.setNota("----");
			}
			boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(rs.getDouble("freguencia"));
			boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
			obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		}
		return x;
	}

	private int montarDadosCargaHorariaBoletimAcademico(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, String nomeDisciplina, String matricula, boolean parteDiversificada, String titulo, int x, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarReprovadoPorFaltaComoReprovado, boolean apresentarCargaHorariaCursada) throws Exception {
		if (bimestreEnum == null || bimestreEnum.getOrdemApresentar() >= BimestreEnum.BIMESTRE_04.getOrdemApresentar()) {
			BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
			boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
			boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
			boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
			boletimAcademicoEnsinoMedioRelVO.setMatricula(matricula);
			boletimAcademicoEnsinoMedioRelVO.setSituacaoHistorico(obterSituacaoHistorico(rs, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado));
			boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
			boletimAcademicoEnsinoMedioRelVO.setParteDiversificada(parteDiversificada);
			boletimAcademicoEnsinoMedioRelVO.setNomeBimestre(BimestreEnum.RESUMO_FINAL.getValorResumidoApresentar());
			boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.RESUMO_FINAL.getOrdemApresentar());
			boletimAcademicoEnsinoMedioRelVO.setTituloNota(titulo);
			if (apresentarCargaHorariaCursada) {
				boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(rs.getInt("cargaHorariaCursada")));
				boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(rs.getDouble("cargaHorariaCursada"));
			} else {
				boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(rs.getInt("cargaHorariaDisciplina")));
				boletimAcademicoEnsinoMedioRelVO.setNotaCalculada(rs.getDouble("cargaHorariaDisciplina"));
			}
			if (boletimAcademicoEnsinoMedioRelVO.getNota().trim().isEmpty()) {
				boletimAcademicoEnsinoMedioRelVO.setNota("----");
			}
			boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
			obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		}
		return x;
	}

	private String realizarCriacaoBoletimAcademico2Rel(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");
		x = montarDadosNotasBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		// x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestreEnum,
		// nomeDisciplina, matricula, parteDiversificada, "MF", qtdeCasaDecimal,
		// x);
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			x = montarDadosTotalFaltaBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
		} else {
			x = montarDadosFrequenciaBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Freq.", x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		}
		x = montarDadosSituacaoHistoricoBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Sit.", x, true, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		return "";
	}

	private void montarBoletimAcademico2Rel(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, Boolean apresentarAprovadoPorAproveitamentoComoAprovado,UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");
		x = montarDadosNotasBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		// x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestreEnum,
		// nomeDisciplina, matricula, parteDiversificada, "MF", qtdeCasaDecimal,
		// x);
		
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			x = montarDadosTotalFaltaBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
		} else {
			x = montarDadosFrequenciaBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Freq.", x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		}
		x = montarDadosSituacaoHistoricoBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Sit.", x, true, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);

	}

	private void montarBoletimPorDisciplinaEnsinoMedio(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");
		x = montarDadosNotasBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			if(!realizarBuscaExisteAgrupamentoNotaTrimestralConfiguracaoAcademico(obj)) {
			  x = montarDadosFaltaBimestralBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "F.B", "F.B", "F.B", "F.B", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado, false);
			}
		}
		x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "M.F", qtdeCasaDecimal, x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		x = montarDadosTotalFaltaBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "T.F", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
		x = montarDadosSituacaoHistoricoBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Sit.", x, true, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
	}

	private void montarBoletimPorDisciplinaEnsinoFundamentalI(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestreEnum, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");
		x = montarDadosNotasBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Total de Notas", qtdeCasaDecimal, x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		x = montarDadosSituacaoHistoricoBoletimAcademico(rs, obj, bimestreEnum, nomeDisciplina, matricula, parteDiversificada, "Sit.", x, true, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
	    realizarBuscaExisteAgrupamentoNotaTrimestralConfiguracaoAcademico(obj);
	}

	private void montarBoletimPorDisciplinaEnsinoMedioLayout2(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestre, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");
		
		x = montarDadosNotasBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			if(!realizarBuscaExisteAgrupamentoNotaTrimestralConfiguracaoAcademico(obj) ) {
			   x = montarDadosFaltaBimestralBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Faltas", "Faltas", "Faltas", "Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado, false);
			}
			x = montarDadosTotalFaltaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Total de Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
		}
		x = montarDadosFrequenciaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "% Freq.", x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Final", qtdeCasaDecimal, x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		x = montarDadosSituacaoHistoricoBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Sit.", x, true, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
	}

	private void montarBoletimPorDisciplinaFichaFinanceiraLayout2(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestre, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");

		x = montarDadosNotasBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			if(!realizarBuscaExisteAgrupamentoNotaTrimestralConfiguracaoAcademico(obj) ) {			
				x = montarDadosFaltaBimestralBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Faltas", "Faltas", "Faltas", "Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado, true);
			}
			x = montarDadosTotalFaltaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Total de Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
		}
		x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Média Final", qtdeCasaDecimal, x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);

		MatriculaPeriodoTurmaDisciplinaVO matriculaperiodoturmadisciplina = new MatriculaPeriodoTurmaDisciplinaVO();
		if (Uteis.isAtributoPreenchido(rs.getInt("matriculaperiodoturmadisciplina"))) {
			matriculaperiodoturmadisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(rs.getInt("matriculaperiodoturmadisciplina"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
			if(Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getGradeDisciplinaVO())) {
				matriculaperiodoturmadisciplina.setGradeDisciplinaVO(getFacadeFactory().getGradeDisciplinaFacade().consultarPorChavePrimariaSemExcecao(matriculaperiodoturmadisciplina.getGradeDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
			}else  if(Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getGradeCurricularGrupoOptativaDisciplinaVO())) {
				matriculaperiodoturmadisciplina.setGradeCurricularGrupoOptativaDisciplinaVO(getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorChavePrimaria(matriculaperiodoturmadisciplina.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuarioVO));
			}
		}
		montarDadosCargaHorariaPrevista(rs, obj, "Horas/Aulas Previstas", nomeDisciplina, x, matriculaperiodoturmadisciplina.getTurma());
		montarDadosCargaHorariaDadas(rs, obj, "Horas/Aulas Dadas", nomeDisciplina, x, obj.getSemestre(), obj.getAno(), matriculaperiodoturmadisciplina);
	}

	private boolean realizarBuscaExisteAgrupamentoNotaTrimestralConfiguracaoAcademico(BoletimAcademicoRelVO obj) {	 
		
		    boolean possuiAgrupamentoTrimestral  =   obj.getBoletimAcademicoEnsinoMedioRelVOs().stream().anyMatch( a -> 
					           BimestreEnum.getBimestreEnum(a.getNomeBimestre()).equals(BimestreEnum.TRIMESTRE_01)  ||
				               BimestreEnum.getBimestreEnum(a.getNomeBimestre()).equals(BimestreEnum.TRIMESTRE_02)  || 
				               BimestreEnum.getBimestreEnum(a.getNomeBimestre()).equals(BimestreEnum.TRIMESTRE_03) ); 
		     if(!possuiAgrupamentoTrimestral) {
		    	boolean naoPossuiAgrupador =  obj.getBoletimAcademicoEnsinoMedioRelVOs().stream().allMatch( a ->  !Uteis.isAtributoPreenchido(a.getNomeBimestre())); 
                 if(naoPossuiAgrupador) {
                	 possuiAgrupamentoTrimestral = true ;
                 }else {
		    	 obj.setApresentarFaltaPrimeiroBimestre(obj.getBoletimAcademicoEnsinoMedioRelVOs().stream().anyMatch( a ->  BimestreEnum.getBimestreEnum(a.getNomeBimestre()).equals(BimestreEnum.BIMESTRE_01))); 
		    	 obj.setApresentarFaltaSegundoBimestre(obj.getBoletimAcademicoEnsinoMedioRelVOs().stream().anyMatch( a ->  BimestreEnum.getBimestreEnum(a.getNomeBimestre()).equals(BimestreEnum.BIMESTRE_02)));  
		    	 obj.setApresentarFaltaTerceiroBimestre(obj.getBoletimAcademicoEnsinoMedioRelVOs().stream().anyMatch( a ->  BimestreEnum.getBimestreEnum(a.getNomeBimestre()).equals(BimestreEnum.BIMESTRE_03))); 
		    	 obj.setApresentarFaltaQuartoBimestre(obj.getBoletimAcademicoEnsinoMedioRelVOs().stream().anyMatch( a ->  BimestreEnum.getBimestreEnum(a.getNomeBimestre()).equals(BimestreEnum.BIMESTRE_04))); 
                 }
	              
		     } 
		
	       
		      
		   return possuiAgrupamentoTrimestral ;	
		
	}
	
	

	private void montarDadosCargaHorariaPrevista(SqlRowSet rs, BoletimAcademicoRelVO obj, String titulo, String nomeDisciplina, int x, TurmaVO turma) throws Exception {
		BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
		boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
		boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
		boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
		boletimAcademicoEnsinoMedioRelVO.setMatricula(obj.getMatricula());
		boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
		boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.RESUMO_FINAL.getOrdemApresentar());

		boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(rs.getInt("cargaHorariaDisciplina")));

		boletimAcademicoEnsinoMedioRelVO.setTituloNota(titulo);
		boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
		obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
	}

	private void montarDadosCargaHorariaDadas(SqlRowSet rs, BoletimAcademicoRelVO obj, String titulo, String nomeDisciplina, int x, String semestre,
			String ano, MatriculaPeriodoTurmaDisciplinaVO matriculaperiodoturmadisciplina) throws Exception {
		BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = new BoletimAcademicoEnsinoMedioRelVO();
		boletimAcademicoEnsinoMedioRelVO.setDisciplina(nomeDisciplina);
		boletimAcademicoEnsinoMedioRelVO.setOrdem(rs.getInt("ordem"));
		boletimAcademicoEnsinoMedioRelVO.setCodigoDisciplina(rs.getInt("disciplina.codigo"));
		boletimAcademicoEnsinoMedioRelVO.setMatricula(obj.getMatricula());
		boletimAcademicoEnsinoMedioRelVO.setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
		boletimAcademicoEnsinoMedioRelVO.setOrdemBimestre(BimestreEnum.RESUMO_FINAL.getOrdemApresentar());
		Integer somaCargaHorariaDisciplina = 0;
		if(!Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina)) {
			somaCargaHorariaDisciplina = rs.getInt("cargahorariacursada");
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(somaCargaHorariaDisciplina > 0 ? somaCargaHorariaDisciplina : 0));
		}else if(matriculaperiodoturmadisciplina.getDisciplinaComposta()) {						
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaHistoricoFazParteComposicaoPorMatriculaPorGradeCurricularPorMatriculaPeriodo(rs.getString("matricula"), 0, rs.getInt("matriculaperiodo"), rs.getInt("matrizcurricular"), rs.getInt("gradedisciplina"), rs.getInt("gradeCurricularGrupoOptativaDisciplina"), false, null);
			if(Uteis.isAtributoPreenchido(historicoVOs)) {
				for(HistoricoVO historicoVO : historicoVOs) {
					matriculaperiodoturmadisciplina = historicoVO.getMatriculaPeriodoTurmaDisciplina();

					if(Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina)) {
						boolean considerarTurmaTeoricaEPratica = Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaTeorica()) && Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaPratica()) ? true : false ;
						if (considerarTurmaTeoricaEPratica) {
							somaCargaHorariaDisciplina += getFacadeFactory().getRegistroAulaFacade().consultarSomaCargaHorariaDisciplinaConsiderantoTurmaTeoricaETurmaPratica(
								matriculaperiodoturmadisciplina.getTurmaPratica().getCodigo(), matriculaperiodoturmadisciplina.getTurmaTeorica().getCodigo(), semestre, ano, historicoVO.getDisciplina().getCodigo(), false, null);
						} else {
							int turmaConsiderar = matriculaperiodoturmadisciplina.getTurma().getCodigo();
							if(Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaTeorica())){
								turmaConsiderar = matriculaperiodoturmadisciplina.getTurmaTeorica().getCodigo();
							}
							if(Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaPratica())){
								turmaConsiderar = matriculaperiodoturmadisciplina.getTurmaPratica().getCodigo();
							}
							somaCargaHorariaDisciplina += getFacadeFactory().getRegistroAulaFacade().consultarSomaCargaHorarioDisciplina(
								turmaConsiderar, semestre, ano, historicoVO.getDisciplina().getCodigo(), false, null, true);
						
						}
					}else {
						somaCargaHorariaDisciplina += historicoVO.getCargaHorariaDisciplina();
					}
				}
				boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(somaCargaHorariaDisciplina > 0 ? somaCargaHorariaDisciplina/60 : 0));
			}else {
				somaCargaHorariaDisciplina = rs.getInt("cargahorariacursada");
				boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(somaCargaHorariaDisciplina > 0 ? somaCargaHorariaDisciplina : 0));
			}
				
		}else {
			boolean considerarTurmaTeoricaEPratica = Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaTeorica()) && Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaPratica()) ? true : false ;
			if (considerarTurmaTeoricaEPratica) {
				somaCargaHorariaDisciplina = getFacadeFactory().getRegistroAulaFacade().consultarSomaCargaHorariaDisciplinaConsiderantoTurmaTeoricaETurmaPratica(
					matriculaperiodoturmadisciplina.getTurmaPratica().getCodigo(), matriculaperiodoturmadisciplina.getTurmaTeorica().getCodigo(), semestre, ano, boletimAcademicoEnsinoMedioRelVO.getCodigoDisciplina(), false, null);
			} else {
				int turmaConsiderar = matriculaperiodoturmadisciplina.getTurma().getCodigo();
				if(Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaTeorica())){
					turmaConsiderar = matriculaperiodoturmadisciplina.getTurmaTeorica().getCodigo();
				}
				if(Uteis.isAtributoPreenchido(matriculaperiodoturmadisciplina.getTurmaPratica())){
					turmaConsiderar = matriculaperiodoturmadisciplina.getTurmaPratica().getCodigo();
				}
				somaCargaHorariaDisciplina = getFacadeFactory().getRegistroAulaFacade().consultarSomaCargaHorarioDisciplina(
					turmaConsiderar, semestre, ano, boletimAcademicoEnsinoMedioRelVO.getCodigoDisciplina(), false, null, true);
			}
			boletimAcademicoEnsinoMedioRelVO.setNota(String.valueOf(somaCargaHorariaDisciplina > 0 ? somaCargaHorariaDisciplina/60 : 0));
		}		

		boletimAcademicoEnsinoMedioRelVO.setTituloNota(titulo);
		boletimAcademicoEnsinoMedioRelVO.setOrdemNota(x++);
		obj.getBoletimAcademicoEnsinoMedioRelVOs().add(boletimAcademicoEnsinoMedioRelVO);
		
	}

	private void montarBoletimPorDisciplinaFichaAluno(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestre, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado, boolean apresentarCargaHorariaCursada) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");
		x = montarDadosNotasBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			if(!realizarBuscaExisteAgrupamentoNotaTrimestralConfiguracaoAcademico(obj) ) {
		  	  x = montarDadosFaltaBimestralBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "F.B", "F.B", "F.B", "F.B", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado, false);
		
			}
		}
		x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "M.F", qtdeCasaDecimal, x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			x = montarDadosTotalFaltaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "T.F", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
		}
		if (apresentarCargaHorariaCursada) {
			x = montarDadosCargaHorariaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "C.H.", x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado, apresentarCargaHorariaCursada);
		}
	}

	private void montarBoletimPorDisciplinaIndividualAluno(SqlRowSet rs, BoletimAcademicoRelVO obj, BimestreEnum bimestre, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, UsuarioVO usuarioVO, boolean apresentarReprovadoPorFaltaComoReprovado, boolean apresentarCargaHorariaCursada) throws Exception {
		String nomeDisciplina = rs.getString("disciplina.nome");
		String matricula = rs.getString("matricula");
		if (rs.getString("tipoHistorico").equals(TipoHistorico.ADAPTACAO.getValor())) {
			nomeDisciplina = nomeDisciplina + "(AD)";
		}
		if (rs.getString("tipoHistorico").equals(TipoHistorico.DEPENDENCIA.getValor())) {
			nomeDisciplina = nomeDisciplina + "(DP)";
		}
		int x = 1;
		int qtdeCasaDecimal = rs.getInt("quantidadeCasasDecimaisPermitirAposVirgula");
		boolean parteDiversificada = rs.getBoolean("parteDiversificada");
		x = montarDadosNotasBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, obj.getApresentarApenaNotaTipoMedia(), qtdeCasaDecimal, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			if(!realizarBuscaExisteAgrupamentoNotaTrimestralConfiguracaoAcademico(obj)) {
			x = montarDadosFaltaBimestralBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Faltas", "Faltas", "Faltas", "Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado, false);
		   }
		}
		x = montarDadosMediaFinalBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Média Final", qtdeCasaDecimal, x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		if (obj.getApresentarQuantidadeFaltasAluno()) {
			x = montarDadosTotalFaltaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Total de Faltas", x, apresentarAprovadoPorAproveitamentoComoAprovado, usuarioVO, apresentarReprovadoPorFaltaComoReprovado);
		}
		x = montarDadosFrequenciaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "% de Freq.", x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado);
		x = montarDadosCargaHorariaBoletimAcademico(rs, obj, bimestre, nomeDisciplina, matricula, parteDiversificada, "Carga Horária", x, apresentarAprovadoPorAproveitamentoComoAprovado, apresentarReprovadoPorFaltaComoReprovado, apresentarCargaHorariaCursada);
	}

	public SituacaoHistorico obterSituacaoHistorico(SqlRowSet rs, Boolean apresentarAprovadoPorAproveitamentoComoAprovado, boolean apresentarReprovadoPorFaltaComoReprovado) {
		SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(rs.getString("situacao"));
		if (situacaoHistorico == null) {
			situacaoHistorico = SituacaoHistorico.CURSANDO;
		}
		if (situacaoHistorico.getHistoricoAprovado()) {
			if (!apresentarAprovadoPorAproveitamentoComoAprovado && 
					(situacaoHistorico.equals(SituacaoHistorico.APROVADO_APROVEITAMENTO)
							|| situacaoHistorico.equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA)
							|| situacaoHistorico.equals(SituacaoHistorico.CONCESSAO_CREDITO)
							|| situacaoHistorico.equals(SituacaoHistorico.ISENTO))) {
			 return	situacaoHistorico;
			} else {
				situacaoHistorico = SituacaoHistorico.APROVADO;
			}
		}
		if (situacaoHistorico.getHistoricoReprovado() && apresentarReprovadoPorFaltaComoReprovado) {
			situacaoHistorico = SituacaoHistorico.REPROVADO;
		}
		if (situacaoHistorico.getHistoricoCursando()) {
			situacaoHistorico = SituacaoHistorico.CURSANDO;
		}
		return situacaoHistorico;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorio(String layout) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + layout + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("BoletimAcademicoRel");
	}

	public void montarDadosSituacaoFinalEnsinoMedio(BoletimAcademicoRelVO obj, ConfiguracaoAcademicoVO configuracaoAcademicoVO, Boolean considerarSituacaoAcademicaHistorico) throws Exception {
		Map<String, Map<String, String>> mapMatricula = realizarGeracaoMapMatriculaDisciplinaEnsinoMedio(obj.getBoletimAcademicoEnsinoMedioRelVOs(), considerarSituacaoAcademicaHistorico);
		Map<String, SituacaoHistorico> mapMatriculaSituacaoFinal = getFacadeFactory().getHistoricoFacade().executarCalcularResultadoFinalAluno(mapMatricula, realizarGeracaoMapMatriculaConfiguracaoAcademicaEnsinoMedio(obj.getBoletimAcademicoEnsinoMedioRelVOs(), obj.getAno(), obj.getSemestre()));
		if (!mapMatriculaSituacaoFinal.isEmpty() && mapMatriculaSituacaoFinal.containsKey(obj.getMatricula())) {
			obj.setSituacaoFinal(mapMatriculaSituacaoFinal.get(obj.getMatricula()).getDescricao());
		}
	}

	private Map<String, Map<String, String>> realizarGeracaoMapMatriculaDisciplinaEnsinoMedio(List<BoletimAcademicoEnsinoMedioRelVO> objs, boolean considerarSituacaoAcademicaHistorico) throws Exception {
		Map<String, Map<String, String>> mapMatricula = new HashMap<String, Map<String, String>>(0);
		for (BoletimAcademicoEnsinoMedioRelVO obj : objs) {
			if (!mapMatricula.containsKey(obj.getMatricula())) {
				mapMatricula.put(obj.getMatricula(), new HashMap<String, String>(0));
			}
			/**
			 * Deve-se desconsiderar todas as disciplina cujo codigo seja null
			 * ou 0, isso ocorre devido a regra de geração de linhas e colunas
			 * do relatório por meio da Crosstab.
			 */
			if (!mapMatricula.get(obj.getMatricula()).containsKey(obj.getCodigoDisciplina().toString()) && Uteis.isAtributoPreenchido(obj.getCodigoDisciplina())) {
				SituacaoMatriculaPeriodoEnum sit = SituacaoMatriculaPeriodoEnum.getEnumPorValor(obj.getSituacaoMatriculaPeriodo());
				if (sit != null) {
					if (sit.equals(SituacaoMatriculaPeriodoEnum.TRANCADA) || sit.equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA) || sit.equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA) || sit.equals(SituacaoMatriculaPeriodoEnum.CANCELADA) || sit.equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO) || sit.equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA)) {
						if (sit.equals(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA) && considerarSituacaoAcademicaHistorico) {
							mapMatricula.get(obj.getMatricula()).put(obj.getCodigoDisciplina().toString(), obj.getSituacaoHistorico().getValor());
						} else {
							mapMatricula.get(obj.getMatricula()).put(obj.getCodigoDisciplina().toString(), obj.getSituacaoMatriculaPeriodo());
						}
					} else {
						mapMatricula.get(obj.getMatricula()).put(obj.getCodigoDisciplina().toString(), obj.getSituacaoHistorico().getValor());
					}
				} else {
					mapMatricula.get(obj.getMatricula()).put(obj.getCodigoDisciplina().toString(), obj.getSituacaoHistorico().getValor());
				}
			}
		}
		return mapMatricula;
	}
	
	public void montarDadosSituacaoFinal(BoletimAcademicoRelVO obj, List<HistoricoVO> objs) throws Exception {
		Map<String, Map<String, String>> mapMatricula = realizarGeracaoMapMatriculaDisciplina(objs);
		Map<String, SituacaoHistorico> mapMatriculaSituacaoFinal = getFacadeFactory().getHistoricoFacade().executarCalcularResultadoFinalAluno(mapMatricula, realizarGeracaoMapMatriculaConfiguracaoAcademicoFechamentoPeriodoLetivo(objs));
		if (!mapMatriculaSituacaoFinal.isEmpty() && mapMatriculaSituacaoFinal.containsKey(obj.getMatricula())) {
			obj.setSituacaoFinal(mapMatriculaSituacaoFinal.get(obj.getMatricula()).getDescricao());
		}
	}
	
	private Map<String, Map<String, String>> realizarGeracaoMapMatriculaDisciplina(List<HistoricoVO> objs) throws Exception {
		Map<String, Map<String, String>> mapMatricula = new HashMap<String, Map<String, String>>(0);
		if (!Uteis.isAtributoPreenchido(objs)) {
			return mapMatricula;
		}
		MatriculaVO matriculaVO = (MatriculaVO) objs.get(0).getMatricula();
		getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, null);
		for (HistoricoVO obj : objs) {
			obj.setMatricula(matriculaVO);
			if (!mapMatricula.containsKey(obj.getMatricula().getMatricula())) {
				mapMatricula.put(obj.getMatricula().getMatricula(), new HashMap<String, String>(0));
			}
			/**
			 * Deve-se desconsiderar todas as disciplina cujo codigo seja null
			 * ou 0, isso ocorre devido a regra de geração de linhas e colunas
			 * do relatório por meio da Crosstab.
			 */
			if (!mapMatricula.get(obj.getMatricula().getMatricula()).containsKey(obj.getDisciplina().getCodigo().toString()) && Uteis.isAtributoPreenchido(obj.getDisciplina())) {
				SituacaoMatriculaPeriodoEnum sit = SituacaoMatriculaPeriodoEnum.getEnumPorValor(obj.getMatriculaPeriodo().getSituacaoMatriculaPeriodo());
				if (obj.getMatricula().getCurso().getPeriodicidade().equals("IN")) {
					sit = SituacaoMatriculaPeriodoEnum.getEnumPorValor(obj.getMatricula().getSituacao());
				}
				if (sit != null) {
					if (sit.equals(SituacaoMatriculaPeriodoEnum.TRANCADA) || sit.equals(SituacaoMatriculaPeriodoEnum.CANCELADA) || sit.equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO) || sit.equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA)) {
						mapMatricula.get(obj.getMatricula().getMatricula()).put(obj.getDisciplina().getCodigo().toString(), obj.getMatriculaPeriodo().getSituacaoMatriculaPeriodo());
					} else {
						mapMatricula.get(obj.getMatricula().getMatricula()).put(obj.getDisciplina().getCodigo().toString(), obj.getSituacao());
					}
				} else {
					mapMatricula.get(obj.getMatricula().getMatricula()).put(obj.getDisciplina().getCodigo().toString(), obj.getSituacao());
				}
			}
		}
		return mapMatricula;
	}	

	public void realizarConsultaRegistroPorMatriculaAnoSemestre(BoletimAcademicoRelVO obj, String ano, String semestre, BimestreEnum bimestreEnum, List<DisciplinaVO> disciplinaVOs, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT count(registrofalta.codigo) as qtde, bimestre from registrofalta ");
		sql.append(" where matricula = '").append(obj.getMatricula()).append("' ");
		if (ano != null && !ano.trim().isEmpty()) {
			sql.append(" and ano = '").append(ano).append("' ");
		}
		if (semestre != null && !semestre.trim().isEmpty()) {
			sql.append(" and semestre = '").append(semestre).append("' ");
		}
		sql.append(" group by bimestre ");
		if (bimestreEnum != null) {
			if (bimestreEnum.equals(BimestreEnum.BIMESTRE_01)) {
				sql.append(" and bimestre in ('").append(BimestreEnum.BIMESTRE_01.name()).append("') ");
			} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_02)) {
				sql.append(" and bimestre in ('").append(BimestreEnum.BIMESTRE_01.name()).append("', '").append(BimestreEnum.BIMESTRE_02.name()).append("') ");
			} else if (bimestreEnum.equals(BimestreEnum.BIMESTRE_03)) {
				sql.append(" and bimestre in ('").append(BimestreEnum.BIMESTRE_01.name()).append("', '").append(BimestreEnum.BIMESTRE_02.name()).append("', '").append(BimestreEnum.BIMESTRE_03.name()).append("') ");
			} else {
				sql.append(" and bimestre in ('").append(BimestreEnum.BIMESTRE_01.name()).append("', '").append(BimestreEnum.BIMESTRE_02.name()).append("', '").append(BimestreEnum.BIMESTRE_03.name()).append("', '").append(BimestreEnum.BIMESTRE_04.name()).append("') ");
			}
		}
		
		
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			if (rs.getString("bimestre").equals(BimestreEnum.BIMESTRE_01.name())) {
				obj.setTotalFaltaPrimeiroBimestre(rs.getInt("qtde"));
			} else if (rs.getString("bimestre").equals(BimestreEnum.BIMESTRE_02.name())) {
				obj.setTotalFaltaSegundoBimestre(rs.getInt("qtde"));
			} else if (rs.getString("bimestre").equals(BimestreEnum.BIMESTRE_03.name())) {
				obj.setTotalFaltaTerceiroBimestre(rs.getInt("qtde"));
			} else if (rs.getString("bimestre").equals(BimestreEnum.BIMESTRE_04.name())) {
				obj.setTotalFaltaQuartoBimestre(rs.getInt("qtde"));
			}
			obj.setTotalFalta(obj.getTotalFalta() + rs.getInt("qtde"));
		}
		
		Map<BimestreEnum, Integer> faltaBimestres;
		for (DisciplinaVO disciplinaVO : disciplinaVOs) {
			faltaBimestres = getFacadeFactory().getRegistroAulaFacade().consultarQuantidadeFaltasAlunoBimestre(obj.getMatricula(), disciplinaVO.getCodigo(), semestre, ano, null, false, usuarioVO);
			obj.setTotalFaltaPrimeiroBimestre(obj.getTotalFaltaPrimeiroBimestre() + (faltaBimestres.containsKey(BimestreEnum.BIMESTRE_01) ? faltaBimestres.get(BimestreEnum.BIMESTRE_01) : 0));
			obj.setTotalFaltaSegundoBimestre(obj.getTotalFaltaSegundoBimestre() + (faltaBimestres.containsKey(BimestreEnum.BIMESTRE_02) ? faltaBimestres.get(BimestreEnum.BIMESTRE_02) : 0));
			obj.setTotalFaltaTerceiroBimestre(obj.getTotalFaltaTerceiroBimestre() + (faltaBimestres.containsKey(BimestreEnum.BIMESTRE_03) ? faltaBimestres.get(BimestreEnum.BIMESTRE_03) : 0));
			obj.setTotalFaltaQuartoBimestre(obj.getTotalFaltaQuartoBimestre() + (faltaBimestres.containsKey(BimestreEnum.BIMESTRE_04) ? faltaBimestres.get(BimestreEnum.BIMESTRE_04) : 0));
		}

	}
	
	public Map<BimestreEnum, Integer> montarDadosMapFaltasBimestre(String matricula, SqlRowSet rs, UsuarioVO usuarioVO) throws Exception {
		Map<BimestreEnum, Integer> faltaBimestres = getFacadeFactory().getRegistroAulaFacade().consultarQuantidadeFaltasAlunoBimestre(matricula, rs.getInt("disciplina.codigo"), rs.getString("matriculaperiodo.semestre"), rs.getString("matriculaperiodo.ano"), rs.getInt("turma.codigo"), false, usuarioVO);
		/**
		 * Caso não exista registros na tabela frequenciaAula e utilizado os registros da tabela histórico.
		 */
		if (!Uteis.isAtributoPreenchido(faltaBimestres.get(BimestreEnum.BIMESTRE_01))) {
			faltaBimestres.put(BimestreEnum.BIMESTRE_01, rs.getInt("faltaPrimeiroBimestre"));
		}
		if (!Uteis.isAtributoPreenchido(faltaBimestres.get(BimestreEnum.BIMESTRE_02))) {
			faltaBimestres.put(BimestreEnum.BIMESTRE_02, rs.getInt("faltaSegundoBimestre"));
		}
		if (!Uteis.isAtributoPreenchido(faltaBimestres.get(BimestreEnum.BIMESTRE_03))) {
			faltaBimestres.put(BimestreEnum.BIMESTRE_03, rs.getInt("faltaTerceiroBimestre"));
		}
		if (!Uteis.isAtributoPreenchido(faltaBimestres.get(BimestreEnum.BIMESTRE_04))) {
			faltaBimestres.put(BimestreEnum.BIMESTRE_04, rs.getInt("faltaQuartoBimestre"));
		}
		return faltaBimestres;
	}
	
	public void preencherCargaHorariaTotal(BoletimAcademicoRelVO boletimAcademicoRelVO, String tituloCargaHoraria) throws Exception {
		Integer cargaHorariaTotal = boletimAcademicoRelVO.getBoletimAcademicoEnsinoMedioRelVOs().stream()
				.filter(b -> b.getTituloNota().equals(tituloCargaHoraria))
				.mapToInt(b -> b.getNotaCalculada().intValue()).sum();
		boletimAcademicoRelVO.setTotalCargaHoraria(cargaHorariaTotal);
	}
	
	
	private Map<String, ConfiguracaoAcademicoVO> realizarGeracaoMapMatriculaConfiguracaoAcademicoFechamentoPeriodoLetivo(List<HistoricoVO> objs) throws Exception {
		Map<String, ConfiguracaoAcademicoVO> mapMatricula = new HashMap<String, ConfiguracaoAcademicoVO>(0);
		if (!Uteis.isAtributoPreenchido(objs)) {
			return mapMatricula;
		}
		for (HistoricoVO obj : objs) {		
			if (!mapMatricula.containsKey(obj.getMatricula().getMatricula())) {
				mapMatricula.put(obj.getMatricula().getMatricula(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(obj.getMatricula().getMatricula(), obj.getAnoHistorico(), obj.getSemestreHistorico()));
			}		
		}
		return mapMatricula;
	}
	
	private Map<String, ConfiguracaoAcademicoVO> realizarGeracaoMapMatriculaConfiguracaoAcademicaEnsinoMedio(List<BoletimAcademicoEnsinoMedioRelVO> objs, String ano, String semestre) throws Exception {
		Map<String, ConfiguracaoAcademicoVO> mapMatricula = new HashMap<String, ConfiguracaoAcademicoVO>(0);
		for (BoletimAcademicoEnsinoMedioRelVO obj : objs) {
			if (!mapMatricula.containsKey(obj.getMatricula())) {
				mapMatricula.put(obj.getMatricula(), getFacadeFactory().getConfiguracaoAcademicoFacade().consultarConfiguracaoAcademicoFechamentoPeriodoLetivoPorMatriculaPeriodo(obj.getMatricula(), ano, semestre));
			}	
		}
		return mapMatricula;
	}
	
	public String realizarCriacaoLegendaSituacaoDisciplinaHistorico(BoletimAcademicoRelVO boletimAcademicoRelVO, MatriculaVO matriculaVO) {
		StringBuilder stringLengenda = new StringBuilder();
		HashMap<String, String> mapaSituacao = new HashMap<String, String>(0);
		if(matriculaVO.getCurso().getNivelEducacional().equals("BA") || matriculaVO.getCurso().getNivelEducacional().equals("ME") || matriculaVO.getCurso().getNivelEducacional().equals("IN")) {
			for (Iterator<BoletimAcademicoEnsinoMedioRelVO> iterator = boletimAcademicoRelVO
					.getBoletimAcademicoEnsinoMedioRelVOs().iterator(); iterator.hasNext();) {
				BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = (BoletimAcademicoEnsinoMedioRelVO) iterator
						.next();
				mapaSituacao.put(boletimAcademicoEnsinoMedioRelVO.getSituacaoHistorico().getValor(),
						boletimAcademicoEnsinoMedioRelVO.getSituacaoHistorico().getDescricao());
			}
		}else {
			for (Iterator<HistoricoVO> iterator = boletimAcademicoRelVO
					.getHistorico().iterator(); iterator.hasNext();) {
				HistoricoVO historicoVO = (HistoricoVO) iterator
						.next();
				mapaSituacao.put(historicoVO.getSituacao(),
						historicoVO.getSituacao());
			}
		}
		for (Iterator<BoletimAcademicoEnsinoMedioRelVO> iterator = boletimAcademicoRelVO
				.getBoletimAcademicoEnsinoMedioRelVOs().iterator(); iterator.hasNext();) {
			BoletimAcademicoEnsinoMedioRelVO boletimAcademicoEnsinoMedioRelVO = (BoletimAcademicoEnsinoMedioRelVO) iterator
					.next();
			mapaSituacao.put(boletimAcademicoEnsinoMedioRelVO.getSituacaoHistorico().getValor(),
					boletimAcademicoEnsinoMedioRelVO.getSituacaoHistorico().getDescricao());
		}
		for (Map.Entry<String, String> elemento : mapaSituacao.entrySet()) {
			stringLengenda.append(elemento.getKey());
			stringLengenda.append(" - ");
			stringLengenda.append(elemento.getValue());
			stringLengenda.append(" / ");
		}
		mapaSituacao = null;
		return Uteis.isAtributoPreenchido(stringLengenda) ?  stringLengenda.toString().substring(0, stringLengenda.toString().length() - 2) : "";
	}
	
}
