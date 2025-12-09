package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.DistribuicaoSubturmaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class DistribuicaoSubturma extends ControleAcesso implements DistribuicaoSubturmaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public DistribuicaoSubturma() throws Exception {
		super();
		setIdEntidade("Turma");
	}

	@Override
	public void buscarAlunosSubturma(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, List<TurmaVO> subturmaVOs, TurmaVO turmaPrincipal, String ano, String semestre, Integer disciplina, String situacaoMatriculaPeriodo, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		matriculaPeriodoTurmaDisciplinaVOs.clear();
		subturmaVOs.clear();
		matriculaPeriodoTurmaDisciplinaVOs.addAll(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarAlunoSubturmaPorTurmaPrincipalAnoSemestreDisciplina(turmaPrincipal, false, ano, semestre, disciplina, situacaoMatriculaPeriodo, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO, tipoSubTurma));
		subturmaVOs.addAll(getFacadeFactory().getTurmaFacade().consultarPorSubTurma(turmaPrincipal, disciplina, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, tipoSubTurma, false, ano, semestre, ""));
		if (subturmaVOs.isEmpty()) {
			throw new Exception("Nenhuma Subturma encontrada conforme parâmetros acima.");
		}
		for (TurmaVO subturma : subturmaVOs) {
			subturma.setUtilizarSubturmaNaDistribuicao(subturma.getSituacao().equals("AB"));
			HorarioTurmaDiaItemVO horarioTurmaDiaItemVO = getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarAulaProgramadaPorTurmaDisciplinaAnoSemestreDistribuicaoSubturma(subturma.getCodigo(), disciplina, ano, semestre, false, usuarioVO);
			if (Uteis.isAtributoPreenchido(horarioTurmaDiaItemVO)) {
				subturma.setPossuiAulaProgramada(true);
				if (horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().getTurmaAgrupada())
					subturma.setTurmaAgrupadaProgramacaoAula(horarioTurmaDiaItemVO.getHorarioTurmaDiaVO().getHorarioTurma().getTurma().getIdentificadorTurma());
			}
			VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(subturma.getCodigo(), disciplina, ano, semestre, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(vagaTurmaDisciplinaVO))
				subturma.setNrMaximoMatricula(vagaTurmaDisciplinaVO.getNrMaximoMatricula());
			subturma.setQtdeAlunosSubturma(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarNrAlunosMatriculadosTurmaDisciplina(subturma.getCodigo(), disciplina, ano, semestre, true, tipoSubTurma, "", false, turmaPrincipal.getTurmaAgrupada()));
			subturma.getMatriculaPeriodoTurmaDisciplinaVOs().addAll(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarAlunoSubturmaPorTurmaPrincipalAnoSemestreDisciplina(subturma, true, ano, semestre, disciplina, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO, tipoSubTurma));
		}
	}

	@Override
	public void realizarDistribuicaoAlunoTurma(List<TurmaVO> subturmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
		int qtdAlunosDistribuir = matriculaPeriodoTurmaDisciplinaVOs.size();
		int qtdAlunosNaSubTurma = 0;
		int qtdAlunosMaximoSubTurma = 0;
		int qtdeMaiorJaDistribuida = 0;
		for (TurmaVO subturma : subturmaVOs) {
			if (subturma.getUtilizarSubturmaNaDistribuicao()) {
				qtdAlunosNaSubTurma += subturma.getQtdeAlunosSubturma();
				qtdAlunosMaximoSubTurma += subturma.getNrMaximoMatricula();
				subturma.setQtdeAlunosDistribuir(0);
				if (qtdeMaiorJaDistribuida < subturma.getQtdeAlunosSubturma()) {
					qtdeMaiorJaDistribuida = subturma.getQtdeAlunosSubturma();
				}
			}
		}

		/**
		 * Verifica se excedeu o limite máximo de alunos na subturma, caso isso ocorra ele apenas retorna
		 */
		if (qtdAlunosNaSubTurma >= qtdAlunosMaximoSubTurma) {
			return;
		}

		int qtdeAlunosPossiveis = qtdAlunosMaximoSubTurma - qtdAlunosNaSubTurma;
		if (qtdeAlunosPossiveis <= 0) {
			qtdeAlunosPossiveis = qtdAlunosDistribuir;
		}
		if (qtdeAlunosPossiveis < qtdAlunosDistribuir) {
			qtdAlunosDistribuir = qtdeAlunosPossiveis;
		} else {
			qtdeAlunosPossiveis = qtdAlunosDistribuir;
		}

		/**
		 * Esta regra abaixo iguala a qtde de alunos em cada subturma de forma que todas possam ter a mesma quantidade de alunos respeitando o limite
		 * mï¿½ximo de alunos na turma
		 */
		if (qtdeMaiorJaDistribuida > 0) {
			for (TurmaVO subturma : subturmaVOs) {
				if (subturma.getNrMaximoMatricula() < subturma.getQtdeAlunosSubturma() && subturma.getUtilizarSubturmaNaDistribuicao()) {
					if (subturma.getQtdeAlunosSubturma() < qtdeMaiorJaDistribuida && qtdeAlunosPossiveis > 0) {
						if (qtdeAlunosPossiveis < qtdeMaiorJaDistribuida - subturma.getQtdeAlunosSubturma()) {
							if (subturma.getNrMaximoMatricula() > 0 && qtdeAlunosPossiveis > subturma.getNrMaximoMatricula()) {
								subturma.setQtdeAlunosDistribuir(subturma.getNrMaximoMatricula() - subturma.getQtdeAlunosSubturma());
								qtdeAlunosPossiveis = qtdeAlunosPossiveis - (subturma.getNrMaximoMatricula() - subturma.getQtdeAlunosSubturma());
							} else {
								subturma.setQtdeAlunosDistribuir(qtdeAlunosPossiveis);
								qtdeAlunosPossiveis = 0;
							}
						} else {
							if (subturma.getNrMaximoMatricula() > 0 && ((qtdeMaiorJaDistribuida - subturma.getQtdeAlunosSubturma()) > subturma.getNrMaximoMatricula())) {
								subturma.setQtdeAlunosDistribuir(subturma.getNrMaximoMatricula() - subturma.getQtdeAlunosSubturma());
								qtdeAlunosPossiveis = qtdeAlunosPossiveis - (subturma.getNrMaximoMatricula() - subturma.getQtdeAlunosSubturma());
							} else {
								subturma.setQtdeAlunosDistribuir(qtdeMaiorJaDistribuida - subturma.getQtdeAlunosSubturma());
								qtdeAlunosPossiveis = qtdeAlunosPossiveis - (qtdeMaiorJaDistribuida - subturma.getQtdeAlunosSubturma());
							}
						}
					}
				}
			}
		}

		/*
		 * Este irï¿½ distribuir equitativa os restantes do alunos em todas as subturmas respeitando o limite maximo de alunos na turma
		 */
		for (int x = 1; x <= qtdeAlunosPossiveis;) {
			for (TurmaVO subturma : subturmaVOs) {
				if (subturma.getUtilizarSubturmaNaDistribuicao()) {
					if (x <= qtdeAlunosPossiveis) {
						if (subturma.getNrMaximoMatricula() > 0 && ((subturma.getQtdeAlunosDistribuir() + subturma.getQtdeAlunosSubturma()) < subturma.getNrMaximoMatricula())) {
							subturma.setQtdeAlunosDistribuir(subturma.getQtdeAlunosDistribuir() + 1);
							x++;
						} else if (subturma.getNrMaximoMatricula() == 0) {
							subturma.setQtdeAlunosDistribuir(subturma.getQtdeAlunosDistribuir() + 1);
							x++;
						}
					} else {
						break;
					}
				}
			}
		}

		/**
		 * Este irï¿½ colocar cada aluno na subturma
		 */
		for (TurmaVO subturma : subturmaVOs) {
			if (subturma.getUtilizarSubturmaNaDistribuicao()) {
				if (subturma.getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL) && subturma.getTurmaAgrupada()) {
					throw new Exception(UteisJSF.internacionalizar("msg_DistribuicaoSubturma_turmaAgrupadaTipoGeral"));
				}
				for (int x = 0; x < subturma.getQtdeAlunosDistribuir(); x++) {
					if (matriculaPeriodoTurmaDisciplinaVOs.size() > 0) {
						MatriculaPeriodoTurmaDisciplinaVO mptd = matriculaPeriodoTurmaDisciplinaVOs.get(0);
						mptd.setNovoObj(true);
						if (subturma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
							mptd.setTurmaTeorica(subturma);
						} else if (subturma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
							mptd.setTurmaPratica(subturma);
						} else {
							mptd.setTurma(subturma);
						}
						subturma.getMatriculaPeriodoTurmaDisciplinaVOs().add(mptd);
						subturma.setQtdeAlunosSubturma(subturma.getQtdeAlunosSubturma() + 1);
						matriculaPeriodoTurmaDisciplinaVOs.remove(0);
					}
				}
				subturma.setQtdeAlunosDistribuir(0);
			}
		}
	}

	@Override
	public List<String> executarAlteracaoTurmaAlunoDistribuicaoSubturma(TurmaVO turmaPrincipal, List<TurmaVO> subturmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs, boolean controlarAcesso, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma, boolean removerVinculoSubturmaTeoricaPratica) throws Exception {
		List<String> msgChoqueHorario = new ArrayList<String>(0);
		boolean choqueHorario;
		for (TurmaVO subturma : subturmaVOs) {
			for (MatriculaPeriodoTurmaDisciplinaVO obj : subturma.getMatriculaPeriodoTurmaDisciplinaVOs()) {
				if (obj.isNovoObj()) {
					if (removerVinculoSubturmaTeoricaPratica) {
						obj.setTurmaTeorica(null);
						obj.setTurmaPratica(null);
					}
					try {
						choqueHorario = false;
						executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(turmaPrincipal, obj, controlarAcesso, usuarioVO, disciplinaFazParteComposicaoSelecionada, tipoSubTurma, obj.getMatriculaObjetoVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
					} catch (Exception e) {
						if (e instanceof ConsistirException && ((ConsistirException) e).getReferenteChoqueHorario()) {
							msgChoqueHorario.add(UteisJSF.internacionalizar("msg_DistribuicaoSubTurma_choqueHorario").replace("{0}", obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getMatricula()).replace("{1}", obj.getMatriculaPeriodoObjetoVO().getMatriculaVO().getAluno().getNome()).replace("{2}", e.getMessage()));
							choqueHorario = true;
							obj.setNovoObj(true);
							continue;
						} else {
							obj.setNovoObj(true);
							throw e;
						}
					}
					if (!choqueHorario) {
						if (obj.getExisteRegistroAula()) {
							verificarRegistroAulaParaAbono(obj, turmaPrincipal, false, usuarioVO);
							if (obj.getQtdeHorasRegistrarAbono() > 0) {
								matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs.add(obj);
							} else {
								executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaValidandoRegistroAulaAbono(turmaPrincipal, obj, usuarioVO, disciplinaFazParteComposicaoSelecionada, tipoSubTurma);
							}
						}
					}
				}
			}
		}
		return msgChoqueHorario;
	}

	private void verificarRegistroAulaParaAbono(MatriculaPeriodoTurmaDisciplinaVO mptd, TurmaVO turmaOrigem, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		int qtdeHorasRegistroAulaTurmaOrigem = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma(mptd.getMatriculaPeriodoObjetoVO().getMatriculaVO().getMatricula(), turmaOrigem.getCodigo(), mptd.getDisciplina().getCodigo(), mptd.getMatriculaPeriodoObjetoVO().getAno(), mptd.getMatriculaPeriodoObjetoVO().getSemestre(), false, usuarioVO);
		int qtdeHorasRegistroAulaTurmaDestino = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma("", mptd.getTurma().getCodigo(), mptd.getDisciplina().getCodigo(), mptd.getMatriculaPeriodoObjetoVO().getAno(), mptd.getMatriculaPeriodoObjetoVO().getSemestre(), false, usuarioVO);
		if (qtdeHorasRegistroAulaTurmaOrigem < qtdeHorasRegistroAulaTurmaDestino) {
			qtdeHorasRegistroAulaTurmaDestino = qtdeHorasRegistroAulaTurmaOrigem;
		}
		if (qtdeHorasRegistroAulaTurmaDestino > 0) {
			mptd.setQtdeHorasRegistrarAbono(qtdeHorasRegistroAulaTurmaDestino);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(TurmaVO turmaPrincipal, MatriculaPeriodoTurmaDisciplinaVO mptd, List<TurmaVO> subturmaVOs, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, String ano, String semestre, Integer disciplina, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma, boolean removerVinculoSubturmaTeoricaPratica) throws Exception {
		if (removerVinculoSubturmaTeoricaPratica) {
			mptd.setTurmaTeorica(null);
			mptd.setTurmaPratica(null);
		}
		executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(turmaPrincipal, mptd, false, usuarioVO, disciplinaFazParteComposicaoSelecionada, tipoSubTurma, mptd.getMatriculaObjetoVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
		if (!mptd.getExisteRegistroAula()) {
			int index = 0;
			for (TurmaVO subturma : subturmaVOs) {
				if (subturma.getCodigo().equals(mptd.getTurma().getCodigo()) || subturma.getCodigo().equals(mptd.getTurmaTeorica().getCodigo()) || subturma.getCodigo().equals(mptd.getTurmaPratica().getCodigo())) {
					subturmaVOs.get(index).setQtdeAlunosSubturma(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarNrAlunosMatriculadosTurmaDisciplina(subturma.getCodigo(), disciplina, ano, semestre, true, tipoSubTurma, "", false, turmaPrincipal.getTurmaAgrupada()));
					subturmaVOs.get(index).getMatriculaPeriodoTurmaDisciplinaVOs().clear();
					subturmaVOs.get(index).getMatriculaPeriodoTurmaDisciplinaVOs().addAll(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarAlunoSubturmaPorTurmaPrincipalAnoSemestreDisciplina(subturma, true, ano, semestre, disciplina, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO, tipoSubTurma));
				}
				index++;
			}
			matriculaPeriodoTurmaDisciplinaVOs.remove(mptd);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(final TurmaVO turmaOrigem, final MatriculaPeriodoTurmaDisciplinaVO mptd, boolean controlarAcesso, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma,  Boolean validarChoqueHorarioOutraMatricula) throws Exception {
		montarDadosMatriculaPeriodoTurmaDisciplinaParaVerificacaoChoqueHorarioDistribuicaoSubturma(mptd, usuarioVO);
		TurmaVO turmaDestino = tipoSubTurma == null || tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)? mptd.getTurma() :  tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA)? mptd.getTurmaPratica():  mptd.getTurmaTeorica(); 
		mptd.setExisteRegistroAula(executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula(turmaOrigem, turmaDestino, mptd.getDisciplina(), mptd.getMatriculaPeriodoObjetoVO(), usuarioVO, tipoSubTurma, validarChoqueHorarioOutraMatricula));
		if (!mptd.getExisteRegistroAula()) {
			executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaValidandoRegistroAulaAbono(turmaOrigem, mptd, usuarioVO, disciplinaFazParteComposicaoSelecionada, tipoSubTurma);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaValidandoRegistroAulaAbono(TurmaVO turmaOrigem, MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		if (mptd.getExisteRegistroAula()) {
			if (mptd.getRealizarAbonoRegistroAula()) {
				int qtdeHorasRegistroAulaTurmaOrigem = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma(mptd.getMatriculaObjetoVO().getMatricula(), turmaOrigem.getCodigo(), mptd.getDisciplina().getCodigo(), mptd.getAno(), mptd.getSemestre(), false, usuarioVO);
				int qtdeHorasRegistroAulaTurmaDestino = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma("", mptd.getTurma().getCodigo(), mptd.getDisciplina().getCodigo(), mptd.getAno(), mptd.getSemestre(), false, usuarioVO);
				if (qtdeHorasRegistroAulaTurmaOrigem < qtdeHorasRegistroAulaTurmaDestino) {
					qtdeHorasRegistroAulaTurmaDestino = qtdeHorasRegistroAulaTurmaOrigem;
				}
				if (qtdeHorasRegistroAulaTurmaDestino > 0) {
					getFacadeFactory().getAbonoFaltaFacade().executarGeracaoAbonoRegistroAulaTransferenciaTurma(mptd.getMatriculaPeriodoObjetoVO(), turmaOrigem, mptd.getTurma(), mptd.getDisciplina(), qtdeHorasRegistroAulaTurmaDestino, false, usuarioVO);
				}
			}
			getFacadeFactory().getFrequenciaAulaFacade().excluirFrequenciaAulaPorMatriculaMatriculaPeriodoTurmaDisciplina(mptd.getMatriculaObjetoVO().getMatricula(), mptd.getMatriculaPeriodoObjetoVO().getCodigo(), turmaOrigem.getCodigo(), mptd.getDisciplina().getCodigo(), false, usuarioVO);
		}
		alterarTurmaPorMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubTurma(mptd, usuarioVO, tipoSubTurma);
		if (!disciplinaFazParteComposicaoSelecionada) {
			List<MatriculaPeriodoTurmaDisciplinaVO> disciplinasFazParteComposicao = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaDisciplinaFazParteComposicaoPorMatriculaPeriodoGradeDisciplina(mptd.getMatriculaPeriodoObjetoVO().getCodigo(), mptd.getGradeDisciplinaVO().getCodigo(), false, usuarioVO);
			for (MatriculaPeriodoTurmaDisciplinaVO fazParteComposicao : disciplinasFazParteComposicao) {
				alterarTurmaPorMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubTurma(fazParteComposicao, usuarioVO, tipoSubTurma);
			}
		}
		mptd.setNovoObj(false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarTurmaPorMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubTurma(MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		if (Uteis.isAtributoPreenchido(mptd.getTurmaTeorica()) && tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA)) {
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(mptd.getCodigo(), mptd.getTurmaTeorica().getCodigo(), usuarioVO);
		} else if (Uteis.isAtributoPreenchido(mptd.getTurmaPratica()) && tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA)) {
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(mptd.getCodigo(), mptd.getTurmaPratica().getCodigo(), usuarioVO);
		} else if (tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarTurmaPorMatriculaPeriodoTurmaDisciplina(mptd.getCodigo(), mptd.getTurma().getCodigo(), usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().removerTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(mptd.getCodigo(), usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().removerTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(mptd.getCodigo(), usuarioVO);
		}
	}

	@Override
	public void executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaComRegistroAulaVOs(TurmaVO turmaOrigem, List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		for (MatriculaPeriodoTurmaDisciplinaVO obj : matriculaPeriodoTurmaDisciplinaComRegistroAulaVOs) {
			executarAlteracaoTurmaPorMatriculaPeriodoTurmaDisciplinaValidandoRegistroAulaAbono(turmaOrigem, obj, usuarioVO, disciplinaFazParteComposicaoSelecionada, tipoSubTurma);
		}
	}

	public void montarDadosMatriculaPeriodoTurmaDisciplinaParaVerificacaoChoqueHorarioDistribuicaoSubturma(MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuario) throws Exception {
		mptd.getMatriculaPeriodoObjetoVO().getMatriculaPeriodoTumaDisciplinaVOs().addAll(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaPeriodo(mptd.getMatriculaPeriodoObjetoVO().getCodigo(), false, usuario));
	}

	/**
	 * Método responsavel por remover aluno da subturma validando se ele e um novo objeto, caso contrario executa a verificacao se existe registro
	 * aula, se não existir altera a turma do aluno para a turma de origem, se existir registro aula informa que e necessario realizar a Transferencia
	 * de Turma.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerAlunoSubturma(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, List<TurmaVO> subturmaVOs, TurmaVO subturma, MatriculaPeriodoTurmaDisciplinaVO mptd, Integer disciplina, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		TurmaVO turmaPrincipal =  null;
		if(tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
			turmaPrincipal =  new TurmaVO();
			turmaPrincipal.setCodigo(subturma.getTurmaPrincipal());
			getFacadeFactory().getTurmaFacade().carregarDados(turmaPrincipal, NivelMontarDados.BASICO, usuarioVO);
		}
		if (mptd.isNovoObj()) {
			for (TurmaVO obj : subturmaVOs) {
				if (obj.getCodigo().equals(subturma.getCodigo())) {					
					subturma.getMatriculaPeriodoTurmaDisciplinaVOs().remove(mptd);
					subturma.setQtdeAlunosSubturma(subturma.getQtdeAlunosSubturma() - 1);
					break;
				}
			}
		} else {
			if (tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
				removerVinculoTurmaComMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubturma(mptd, usuarioVO, tipoSubTurma, subturma);
				executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(subturma, mptd, false, usuarioVO, disciplinaFazParteComposicaoSelecionada, tipoSubTurma, mptd.getMatriculaObjetoVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
			} else {
				mptd.setExisteRegistroAula(getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma(mptd.getMatriculaObjetoVO().getMatricula(), subturma.getCodigo(), mptd.getDisciplina().getCodigo(), mptd.getAno(), mptd.getSemestre(), false, usuarioVO) > 0);
				removerVinculoTurmaComMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubturma(mptd, usuarioVO, tipoSubTurma, subturma);
			}
			if (mptd.getExisteRegistroAula()) {
				mptd.getTurma().setCodigo(subturma.getCodigo());
				throw new Exception("O Aluno " + mptd.getMatriculaObjetoVO().getAluno().getNome() + " possui aulas registradas nesta subturma, favor acessar o menu Transferência de Turma para realizar esta operação.");
			}
			if (tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA)) {
				mptd.setTurmaTeorica(null);
			} else if (tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA)) {
				mptd.setTurmaPratica(null);
			} else {
				mptd.getTurma().setCodigo(subturma.getTurmaPrincipal());
				mptd.setTurmaTeorica(null);
				mptd.setTurmaPratica(null);
			}
			subturma.getMatriculaPeriodoTurmaDisciplinaVOs().remove(mptd);
			subturma.setQtdeAlunosSubturma(subturma.getQtdeAlunosSubturma() - 1);
		}
		mptd.setNovoObj(true);
		if(tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA)) {
			mptd.setTurmaTeorica(null);
		}else if(tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA)) {
			mptd.setTurmaPratica(null);
		}else if(tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
			mptd.setTurma(new TurmaVO());
			mptd.setTurma(turmaPrincipal);			
		}
		matriculaPeriodoTurmaDisciplinaVOs.add(mptd);
	}

	/**
	 * Método responsavel por remover todos os alunos da subturma validando se ele e um novo objeto, caso contrario executa a verificacao se existe
	 * registro aula, se não existir altera a turma dos alunos para a turma de origem, se existir registro aula informa que e necessario realizar a
	 * Transferencia de Turma.
	 */
	@Override
	public void removerTodosAlunosSubturma(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, List<TurmaVO> subturmaVOs, TurmaVO subturma, Integer disciplina, UsuarioVO usuarioVO, boolean disciplinaFazParteComposicaoSelecionada, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		StringBuilder alunos = new StringBuilder("");
		TurmaVO turmaPrincipal =  null;
		if(tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
			turmaPrincipal =  new TurmaVO();
			if(subturma != null && Uteis.isAtributoPreenchido(subturma.getTurmaPrincipal())){
				turmaPrincipal.setCodigo(subturma.getTurmaPrincipal());
			}else {
				turmaPrincipal.setCodigo(subturmaVOs.get(0).getTurmaPrincipal());
			}
			getFacadeFactory().getTurmaFacade().carregarDados(turmaPrincipal, NivelMontarDados.BASICO, usuarioVO);
		}
		for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = subturma.getMatriculaPeriodoTurmaDisciplinaVOs().iterator(); iterator.hasNext();) {
			MatriculaPeriodoTurmaDisciplinaVO obj = iterator.next();
			if (obj.isNovoObj()) {
				matriculaPeriodoTurmaDisciplinaVOs.add(obj);
				for (TurmaVO turma : subturmaVOs) {
					if (turma.getCodigo().equals(subturma.getCodigo())) {
						iterator.remove();
						turma.setQtdeAlunosSubturma(turma.getQtdeAlunosSubturma() - 1);
						break;
					}
				}
			} else {
				if (tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
					removerVinculoTurmaComMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubturma(obj, usuarioVO, tipoSubTurma, subturma);
					executarAlteracaoTurmaAlunoDistribuicaoSubturmaManual(subturma, obj, false, usuarioVO, disciplinaFazParteComposicaoSelecionada, tipoSubTurma, obj.getMatriculaObjetoVO().getCurso().getConfiguracaoAcademico().getValidarChoqueHorarioOutraMatriculaAluno());
				} else {
					obj.setExisteRegistroAula(getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma(obj.getMatriculaObjetoVO().getMatricula(), subturma.getCodigo(), obj.getDisciplina().getCodigo(), obj.getAno(), obj.getSemestre(), false, usuarioVO) > 0);
					removerVinculoTurmaComMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubturma(obj, usuarioVO, tipoSubTurma, subturma);
				}
				
				if (obj.getExisteRegistroAula()) {
					obj.getTurma().setCodigo(subturma.getCodigo());
					alunos.append(obj.getMatriculaObjetoVO().getAluno().getNome()).append(", ");
				} else {
					obj.setNovoObj(true);
					matriculaPeriodoTurmaDisciplinaVOs.add(obj);
					iterator.remove();
					subturma.setQtdeAlunosSubturma(subturma.getQtdeAlunosSubturma() - 1);
				}
			}
			if(tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA)) {
				obj.setTurmaTeorica(null);
			}else if(tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA)) {
				obj.setTurmaPratica(null);
			}else if(tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
				obj.setTurma(new TurmaVO());
				obj.setTurma(turmaPrincipal);			
			}
		}
		if (Uteis.isAtributoPreenchido(alunos.toString())) {
			throw new Exception("Os Alunos " + alunos + "possuem aulas registradas nesta subturma, favor acessar o menu Transferência de Turma para realizar esta operação.");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void removerVinculoTurmaComMatriculaPeriodoTurmaDisciplinaDeAcordoComTipoSubturma(MatriculaPeriodoTurmaDisciplinaVO obj, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma, TurmaVO subturma) throws Exception {
		if (tipoSubTurma.equals(TipoSubTurmaEnum.TEORICA)) {
			obj.setTurmaTeorica(null);
 			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().removerTurmaPorMatriculaPeriodoTurmaDisciplinaTeorica(obj.getCodigo(), usuarioVO);
		} else if (tipoSubTurma.equals(TipoSubTurmaEnum.PRATICA)) {
			obj.setTurmaPratica(null);
 		    getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().removerTurmaPorMatriculaPeriodoTurmaDisciplinaPratica(obj.getCodigo(), usuarioVO);
		} else {
			obj.getTurma().setCodigo(subturma.getTurmaPrincipal());
			obj.setTurmaTeorica(null);
			obj.setTurmaPratica(null);
		}
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		DistribuicaoSubturma.idEntidade = idEntidade;
	}
	
	@Override
	public void verificarRegistroAulaParaAbono(String matricula, TurmaVO turmaOrigem, TurmaVO turmaDestino, Integer disciplina, MatriculaPeriodoVO ultimaMatriculaPeriodo, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		int qtdeHorasRegistroAulaTurmaOrigem = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma(matricula, turmaOrigem.getCodigo(), disciplina, ultimaMatriculaPeriodo.getAno(), ultimaMatriculaPeriodo.getSemestre(), controlarAcesso, usuarioVO);
		int qtdeHorasRegistroAulaTurmaDestino = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma("", turmaDestino.getCodigo(), disciplina, ultimaMatriculaPeriodo.getAno(), ultimaMatriculaPeriodo.getSemestre(), controlarAcesso, usuarioVO);
		if (qtdeHorasRegistroAulaTurmaOrigem < qtdeHorasRegistroAulaTurmaDestino) {
			qtdeHorasRegistroAulaTurmaDestino = qtdeHorasRegistroAulaTurmaOrigem;
		}
		if (qtdeHorasRegistroAulaTurmaDestino > 0) {
			turmaDestino = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaDestino.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
			throw new Exception(UteisJSF.internacionalizar("msg_TranferenciaTurma_qtdeHorasAbonarTurmaDestino").replace("{0}", String.valueOf(qtdeHorasRegistroAulaTurmaOrigem)).replace("{1}", turmaOrigem.getIdentificadorTurma()).replace("{2}", String.valueOf(qtdeHorasRegistroAulaTurmaDestino)).replace("{3}", turmaDestino.getIdentificadorTurma()));
		}
	}

	@Override
	public boolean executarVerificarQtdeMaximaAlunosTurmaChoqueHorarioRegistroAula(TurmaVO turmaOrigem, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, UsuarioVO usuarioVO, TipoSubTurmaEnum tipoSubTurma,  Boolean validarChoqueHorarioOutraMatricula) throws Exception {
		turmaDestino = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turmaDestino.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		VagaTurmaDisciplinaVO vagaTurmaDisciplinaVO = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultarPorCodigoTurmaCodigoDisciplina(turmaDestino.getCodigo(), disciplinaVO.getCodigo(), ultimaMatriculaPeriodoAtiva.getAno(), ultimaMatriculaPeriodoAtiva.getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		if (Uteis.isAtributoPreenchido(vagaTurmaDisciplinaVO)) {
			turmaDestino.setNrMaximoMatricula(vagaTurmaDisciplinaVO.getNrMaximoMatricula());
		}
		int qtdeAlunosTurma = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarNrAlunosMatriculadosTurmaDisciplina(turmaDestino.getCodigo(), disciplinaVO.getCodigo(), ultimaMatriculaPeriodoAtiva.getAno(), ultimaMatriculaPeriodoAtiva.getSemestre(), true, tipoSubTurma, "", false, turmaDestino.getTurmaAgrupada());
		if (qtdeAlunosTurma + 1 > turmaDestino.getNrMaximoMatricula()) {
			throw new Exception("Número máximo de matrícula para a turma " + turmaDestino.getIdentificadorTurma() + " excedido.");
		}
		verificarChoqueHorario(turmaOrigem, turmaDestino, disciplinaVO, ultimaMatriculaPeriodoAtiva, validarChoqueHorarioOutraMatricula, usuarioVO);
		boolean existeRegistroAula = getFacadeFactory().getAbonoFaltaFacade().verificarQtdeHorasRegistroAulaTransferenciaTurma(ultimaMatriculaPeriodoAtiva.getMatriculaVO().getMatricula(), turmaOrigem.getCodigo(), disciplinaVO.getCodigo(), ultimaMatriculaPeriodoAtiva.getAno(), ultimaMatriculaPeriodoAtiva.getSemestre(), false, usuarioVO) > 0;
		boolean registroAulaAbonado = getFacadeFactory().getAbonoFaltaFacade().verificarAbonoRegistroAulaTransferenciaTurma(ultimaMatriculaPeriodoAtiva.getMatriculaVO().getAluno().getCodigo(), ultimaMatriculaPeriodoAtiva.getMatriculaVO().getMatricula(), disciplinaVO.getCodigo(), false, usuarioVO);
		return existeRegistroAula && !registroAulaAbonado;
	}

	private void verificarChoqueHorario(TurmaVO turmaOrigem, TurmaVO turmaDestino, DisciplinaVO disciplinaVO, MatriculaPeriodoVO ultimaMatriculaPeriodoAtiva, Boolean validarChoqueHorarioOutraMatricula, UsuarioVO usuarioVO) throws Exception {
		MatriculaPeriodoTurmaDisciplinaVO objClone = null;
		for (Iterator<MatriculaPeriodoTurmaDisciplinaVO> iterator = ultimaMatriculaPeriodoAtiva.getMatriculaPeriodoTumaDisciplinaVOs().iterator(); iterator.hasNext();) {
			MatriculaPeriodoTurmaDisciplinaVO obj = iterator.next();
			if (((!turmaOrigem.getTurmaAgrupada() && obj.getTurma().getCodigo().equals(turmaOrigem.getCodigo())) || (turmaOrigem.getTurmaAgrupada())) && obj.getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
				objClone = (MatriculaPeriodoTurmaDisciplinaVO) obj.clone();
				iterator.remove();
				break;
			}
		}
		getFacadeFactory().getMatriculaPeriodoFacade().executarVerificarSeHaIncompatibilidadeHorarioDeDisciplinas(ultimaMatriculaPeriodoAtiva, turmaDestino, disciplinaVO, null, validarChoqueHorarioOutraMatricula, usuarioVO);
		if (Uteis.isAtributoPreenchido(objClone)) {
			ultimaMatriculaPeriodoAtiva.getMatriculaPeriodoTumaDisciplinaVOs().add(objClone);
		}
	}

	@Override
	public void executarVerificacaoAlunoVinculadoTipoSubturmaTeoricaOuPratica(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, TipoSubTurmaEnum tipoSubTurma) throws Exception {
		if (tipoSubTurma.equals(TipoSubTurmaEnum.GERAL)) {
			for (MatriculaPeriodoTurmaDisciplinaVO mptdVO : matriculaPeriodoTurmaDisciplinaVOs) {
				if (mptdVO.isNovoObj()) {
					if (Uteis.isAtributoPreenchido(mptdVO.getTurmaTeorica())) {
						throw new Exception(UteisJSF.internacionalizar("msg_DistribuicaoSubturma_alunoVinculadoTurmaTeoricaOuPratica").replace("{0}", TipoSubTurmaEnum.TEORICA.getValorApresentar()));
					} else if (Uteis.isAtributoPreenchido(mptdVO.getTurmaPratica())) {
						throw new Exception(UteisJSF.internacionalizar("msg_DistribuicaoSubturma_alunoVinculadoTurmaTeoricaOuPratica").replace("{0}", TipoSubTurmaEnum.PRATICA.getValorApresentar()));
					}
				}
			}
		}
	}

}
