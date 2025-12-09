package relatorio.controle.academico;

import controle.arquitetura.SelectItemOrdemValor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces. model.SelectItem;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DownloadVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.interfaces.academico.DownloadRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.DownloadRel;

@Controller("DownloadRelControle")
@Scope("viewScope")
@Lazy
public class DownloadRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7229066847662118793L;
	private DownloadVO downloadVO;
	protected List<TurmaVO> listaSelectItemTurma;
	protected List<DisciplinaVO> listaSelectItemDisciplinas;
	private String ano;
	private String semestre;

	public DownloadRelControle() {
		setDownloadVO(new DownloadVO());
		setMensagemDetalhada("");
	}

	@PostConstruct
	public void init() {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemTurmaProfessor();
		} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			montarListaSelectItemTurmaVisaoCoordenador();
		}
	}

	public void imprimirPDF() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DolwnloadRelControle", "Iniciando Impressao Relatorio PDF",
					"Emitindo Relatorio");
			validarCampos();
			List listaObjetos = new ArrayList(0);
			getFacadeFactory().getTurmaFacade().carregarDados(getDownloadVO().getArquivo().getTurma(),
					NivelMontarDados.BASICO, getUsuarioLogado());
			listaObjetos = getFacadeFactory().getDownloadRelFacade().criarObjeto(getDownloadVO(), getAno(),
					getSemestre());
			if (listaObjetos.isEmpty()) {
				throw new Exception("Nenhum download encontrado para essa TURMA e para a DISCIPLINA");
			}
			getSuperParametroRelVO().setNomeDesignIreport(DownloadRel.getDesignIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir("");
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio("Relatório de Acesso ao Material");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setCaminhoBaseRelatorio("");
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaObjetos.size());
			realizarImpressaoRelatorio();
			registrarAtividadeUsuario(getUsuarioLogado(), "DolwnloadRelControle", "Finalizando Impressao Relatorio PDF",
					"Emitindo Relatorio");
//            removerObjetoMemoria(this);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
//            if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
//                montarListaSelectItemTurmaVisaoCoordenador();
//            }
//            executarMetodoControle(DownloadRelControle.class.getSimpleName(), "montarListaSelectItemTurmaProfessor");
		}
	}

	public void validarCampos() throws Exception {
		if ((getDownloadVO().getArquivo() == null)) {
			throw new Exception("Não há dados a serem exibidos!");
		}
		if ((getDownloadVO().getArquivo().getDisciplina().getCodigo() == null
				|| getDownloadVO().getArquivo().getDisciplina().getCodigo() == 0)) {
			throw new Exception("O campo DISCIPLINA deve ser informado!");
		}
	}

	public List<ArquivoVO> consultarArquivosPorDisciplina(String valorConsulta) throws Exception {
		List<ArquivoVO> listaArquivos = getFacadeFactory().getArquivoFacade().consultarPorNomeDisciplina(valorConsulta,
				Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return listaArquivos;
	}

	public void montarListaSelectItemTurmaVisaoCoordenador() {
		List listaResultado = null;
		Iterator i = null;
		try {
			List obj = new ArrayList(0);
			listaResultado = consultarTurmaPorCoordenador();
			getListaSelectItemTurma().clear();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();

				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno "
							+ turma.getTurno().getNome();
				}
				obj.add(new SelectItem(turma.getCodigo(), value));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) obj, ordenador);
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}

	public List consultarTurmaPorCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(
				getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(),
				getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			getDownloadVO().getArquivo().getDisciplina().setCodigo(0);
			if (getDownloadVO().getArquivo().getTurma().getCodigo() != 0) {
				getFacadeFactory().getTurmaFacade().carregarDados(getDownloadVO().getArquivo().getTurma(),
						NivelMontarDados.TODOS, getUsuarioLogado());
				List objs = new ArrayList(0);
				List resultado = consultarDisciplinaTurmaVisaoCoordenador();
				Iterator i = resultado.iterator();
				objs.add(new SelectItem(0, ""));
				while (i.hasNext()) {
					DisciplinaVO obj = (DisciplinaVO) i.next();
					objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
				setListaSelectItemDisciplinas(objs);
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinas(null);
		}
	}

	public List consultarDisciplinaTurmaVisaoCoordenador() throws Exception {
		List listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaCoordenadorPorTurma(
				getUsuarioLogado().getPessoa().getCodigo(), getDownloadVO().getArquivo().getTurma().getCodigo(),
				getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return listaConsultas;
	}

	public void montarListaSelectItemTurmaProfessor() {
		List listaResultado = null;
		Iterator i = null;
		try {
			List obj = new ArrayList(0);
			listaResultado = consultarTurmaPorProfessor();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();

				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno "
							+ turma.getTurno().getNome();
				}
				obj.add(new SelectItem(turma.getCodigo(), value));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) obj, ordenador);
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}

	}

	public List consultarTurmaPorProfessor() throws Exception {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
			if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), false, "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
			} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), false, "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false, true);
			} else {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), false, "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false, true);
			}
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessor(
					getUsuarioLogado().getPessoa().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
					getUsuarioLogado());
		}
	}

	public void montarListaSelectItemDisciplinaTurma() {
		List resultado = null;
		Iterator i = null;
		try {
			List objs = new ArrayList(0);
			if (getDownloadVO().getArquivo().getTurma().getCodigo() > 0) {
				getFacadeFactory().getTurmaFacade().carregarDados(getDownloadVO().getArquivo().getTurma(),
						NivelMontarDados.BASICO, getUsuarioLogado());
			}
			resultado = consultarDisciplinaProfessorTurma();
			i = resultado.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			// setListaSelectItemDisciplinas(objs);
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemDisciplinas(objs);
		} catch (Exception e) {
			setListaSelectItemDisciplinas(new ArrayList(0));
		} finally {
			Uteis.liberarListaMemoria(resultado);
			i = null;
		}
	}

	public List consultarDisciplinaProfessorTurma() throws Exception {
		List listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaAgrupada(
				getUsuarioLogado().getPessoa().getCodigo(), getDownloadVO().getArquivo().getTurma().getCodigo(),
				getSemestre(), getAno(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), true);
		return listaConsultas;
	}

	public List<TurmaVO> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<TurmaVO>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<TurmaVO> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List<DisciplinaVO> getListaSelectItemDisciplinas() {
		if (listaSelectItemDisciplinas == null) {
			listaSelectItemDisciplinas = new ArrayList<DisciplinaVO>(0);
		}
		return listaSelectItemDisciplinas;
	}

	public void setListaSelectItemDisciplinas(List<DisciplinaVO> listaSelectItemDisciplinas) {
		this.listaSelectItemDisciplinas = listaSelectItemDisciplinas;
	}

	/**
	 * @return the downloadRelVO
	 */
	public DownloadVO getDownloadVO() {
		if (downloadVO == null) {
			downloadVO = new DownloadVO();
		}
		return downloadVO;
	}

	/**
	 * @param downloadRelVO the downloadRelVO to set
	 */
	public void setDownloadVO(DownloadVO downloadVO) {
		this.downloadVO = downloadVO;
	}

	public boolean getIsApresentarDadosAposSelecionarTurma() {
		return getDownloadVO().getArquivo().getTurma().getCodigo() != 0;
	}

	public String getAno() {
//		if (ano == null) {
//			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
//					&& getUsuarioLogado().getIsApresentarVisaoProfessor()) {
//				ano = getVisaoProfessorControle().getAno();
//			} else if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
//					&& getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
//				ano = getVisaoCoordenadorControle().getAno();
//			} else {
//				ano = Uteis.getAnoDataAtual4Digitos();
//			}
//		}
		return "";
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
//		if (semestre == null) {
//			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
//					&& getUsuarioLogado().getIsApresentarVisaoProfessor()) {
//				semestre = getVisaoProfessorControle().getSemestre();
//			} else if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
//					&& getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
//				semestre = getVisaoCoordenadorControle().getSemestre();
//			} else {
//				semestre = Uteis.getSemestreAtual();
//			}
//		}
		return "";
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")
				|| getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getDownloadVO().getArquivo().getTurma().getCodigo().equals(0)) {
					if (getDownloadVO().getArquivo().getTurma().getSemestral()) {
						setAno(getAno());
						return true;
					} else if (getDownloadVO().getArquivo().getTurma().getAnual()) {
						setAno(getAno());
						return true;
					} else {
						setAno("");
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")
				|| getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getDownloadVO().getArquivo().getTurma().getCodigo().equals(0)) {
					if (getDownloadVO().getArquivo().getTurma().getSemestral()) {
						setSemestre(getSemestre());
						return true;
					} else {
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public List<SelectItem> listaSelectSemestre;

	public List<SelectItem> getListaSelectSemestre() {
		if (listaSelectSemestre == null) {
			listaSelectSemestre = new ArrayList<SelectItem>(0);
			listaSelectSemestre.add(new SelectItem("", ""));
			listaSelectSemestre.add(new SelectItem("1", "1º"));
			listaSelectSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectSemestre;
	}

}
