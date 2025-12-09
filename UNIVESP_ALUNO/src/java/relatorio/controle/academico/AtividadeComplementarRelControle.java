package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.AtividadeComplementarRelVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("AtividadeComplementarRelControle")
@Scope("viewScope")
@Lazy
public class AtividadeComplementarRelControle extends SuperControleRelatorio {

	private List listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String ano;
	private String semestre;
	private String situacao;
	private Boolean ativo;
	private Boolean trancado;
	private Boolean cancelado;
	private Boolean abandonado;
	private Boolean formado;
	private Boolean transferenciaInterna;
	private Boolean transferenciaExterna;
	private Boolean preMatricula;
	private Boolean preMatriculaCancelada;
	private Boolean concluido;
	private Integer verificaFiltro;
	private List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;

	private List listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;

	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private CursoVO cursoVO;
	
	private Boolean jubilado;

	public AtividadeComplementarRelControle() {
		montarListaSelectItemUnidadeEnsino();
		setAno(Uteis.getAnoDataAtual4Digitos());
		setSemestre(Uteis.getSemestreAtual());
		setMensagemID("msg_dados_parametroConsulta");
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public Boolean getApresentarCampoConsultaMatricula() {
		if (getControleConsulta().getCampoConsulta().equals("matricula")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getApresentarCampoConsultaCurso() {
		if (getControleConsulta().getCampoConsulta().equals("curso")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getApresentarCampoConsultaTurma() {
		if (getControleConsulta().getCampoConsulta().equals("turma")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List getTipoConsultaSituacao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("Todos", "Todos"));
		itens.add(new SelectItem("Pendencia", "Com Pendência"));
		itens.add(new SelectItem("Concluido", "Concluidos"));
		return itens;
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getValorConsultaAluno().equals("")) {
					throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
				}
				if (getCampoConsultaAluno().equals("matricula")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomePessoa")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				if (getCampoConsultaAluno().equals("nomeCurso")) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
				}
				setListaConsultaAluno(objs);
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			setCursoVO(objAluno.getCurso());
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getCampoConsultaCurso().equals("nome")) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				if (getCampoConsultaCurso().equals("codigo")) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorCodigoCursoUnidadeEnsino(Integer.parseInt(getValorConsultaCurso()), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				}
				setListaConsultaCurso(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (NumberFormatException nfe) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", "Código inválido: " + getValorConsultaCurso());
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			setMatriculaVO(objCompleto);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			setCursoVO(objCompleto.getCurso());
			setTurmaVO(new TurmaVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			setCursoVO(obj);
			setTurmaVO(new TurmaVO());
			setMatriculaVO(new MatriculaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), 0, 0, false, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			if (getTurmaVO().getSubturma()) {
				setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			} else {
				setCursoVO(getTurmaVO().getCurso());
			}
			setMatriculaVO(new MatriculaVO());
			getListaConsultaTurma().clear();
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDFAnalitico() {
		if (!getMostrarCampoAno()) {
			setAno("");
		}
		if (!getMostrarCampoSemestre()) {
			setSemestre("");
		}
		List<AtividadeComplementarRelVO> listaAtividadeComplementar = new ArrayList<AtividadeComplementarRelVO>();
		try {
			listaAtividadeComplementar = getFacadeFactory().getAtividadeComplementarRelFacade().criarObjetoLayoutAtividadeComplementarAnalitico(getCursoVO(), getTurmaVO(), getMatriculaVO().getMatricula(), getUnidadeEnsinoVO(), getAno(), getSemestre(), getSituacao(), getAtivo(), getTrancado(), getCancelado(), getAbandonado(), getFormado(), getTransferenciaInterna(), getTransferenciaExterna(), getPreMatricula(), getPreMatriculaCancelada(), getConcluido(), getJubilado(), getControleConsulta().getCampoConsulta());
			imprimirPDF(listaAtividadeComplementar, getDesignAnalitico(), "Relatório de Atividade Complementar Analítico");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaAtividadeComplementar);
		}

	}

	public void mostrarCampoAnoSemestre() {
		if (getControleConsulta().getCampoConsulta().equals("matricula")) {
			setCursoVO(null);
			getMostrarCampoAno();
			getMostrarCampoSemestre();
		}

	}

	public void imprimirPDFSintetico() {
		if (!getMostrarCampoAno()) {
			setAno("");
		}
		if (!getMostrarCampoSemestre()) {
			setSemestre("");
		}
		List<AtividadeComplementarRelVO> listaAtividadeComplementar = new ArrayList<AtividadeComplementarRelVO>();
		try {
			listaAtividadeComplementar = getFacadeFactory().getAtividadeComplementarRelFacade().criarObjetoLayoutAtividadeComplementarSintetico(getCursoVO(), getTurmaVO(), getMatriculaVO().getMatricula(), getUnidadeEnsinoVO(), getAno(), getSemestre(), getSituacao(), getAtivo(), getTrancado(), getCancelado(), getAbandonado(), getFormado(), getTransferenciaInterna(), getTransferenciaExterna(), getPreMatricula(), getPreMatriculaCancelada(), getConcluido(), getJubilado(), getControleConsulta().getCampoConsulta());
			imprimirPDF(listaAtividadeComplementar, getDesignSintetico(), "Relatório de Atividade Complementar Sintético");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaAtividadeComplementar);
		}
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public String getDesignAnalitico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "AtividadeComplementarRelAnalitico.jrxml");
	}

	public String getDesignSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "AtividadeComplementarRelSintetico.jrxml");
	}

	public String montaFiltroRelatorio() {
		String retorno = "";

		if (!getTurmaVO().getIdentificadorTurma().equals("")) {
			retorno = getTurmaVO().getIdentificadorTurma();
			this.setVerificaFiltro(new Integer(1));
		}

		if (!getCursoVO().getNome().equals("")) {
			retorno = getCursoVO().getNome();
			this.setVerificaFiltro(new Integer(2));
		}

		if (!getMatriculaVO().getMatricula().equals("")) {
			retorno = getMatriculaVO().getMatricula();
			this.setVerificaFiltro(new Integer(3));
		}

		return retorno;
	}

	public String montaFiltroSituacaoAcademicaRelatorio() {
		String retorno = "";

		if (getAtivo()) {
			retorno += "[Ativo], ";
		}

		if (getPreMatricula()) {
			retorno += " [Pré-Matrícula], ";
		}

		if (getPreMatriculaCancelada()) {
			retorno += " [Pré-Matrícula Cancelada], ";
		}

		if (getTrancado()) {
			retorno += " [Trancado], ";
		}

		if (getCancelado()) {
			retorno += " [Cancelado], ";
		}

		if (getConcluido()) {
			retorno += " [Concluído], ";
		}

		if (getTransferenciaInterna()) {
			retorno += " [Transferência Interna], ";
		}

		if (getTransferenciaExterna()) {
			retorno += " [Transferência Saida], ";
		}

		if (getFormado()) {
			retorno += " [Formado], ";
		}

		if (getAbandonado()) {
			retorno += " [Abandono de Curso], ";
		}
		
		if (getJubilado()) {
			retorno += " [Jubilado], ";
		}

		if (retorno.equals("")) {
			return retorno;
		} else {
			return retorno.substring(0, retorno.length() - 2);
		}

	}

	public void imprimirPDF(List<AtividadeComplementarRelVO> lista, String design, String tituloRelatorio) {
		getSuperParametroRelVO().setTituloRelatorio(tituloRelatorio);
		try {
			if (!lista.isEmpty()) {
				registrarAtividadeUsuario(getUsuarioLogado(), "AtividadeComplementarRelControle", "Inicializando Geração de Relatório de Atividade Complementar do  Aluno", "Emitindo Relatório");
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(lista);
				getSuperParametroRelVO().setQuantidade(lista.size());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setNomeEmpresa(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()).getNome());
				registrarAtividadeUsuario(getUsuarioLogado(), "AtividadeComplementarRelControle", "Finalizando Geração de Relatório de Atividade Complementar do  Aluno", "Finalizando Relatório");
				getSuperParametroRelVO().adicionarParametro("filtro", this.montaFiltroRelatorio());
				getSuperParametroRelVO().adicionarParametro("situacao", getSituacao());
				getSuperParametroRelVO().adicionarParametro("ano", getAno());
				getSuperParametroRelVO().adicionarParametro("semestre", getSemestre());
				getSuperParametroRelVO().adicionarParametro("situacaoAcademica", this.montaFiltroSituacaoAcademicaRelatorio());
				getSuperParametroRelVO().adicionarParametro("verificaFiltro", this.getVerificaFiltro());
				if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
					setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
				}
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			design = null;
			Uteis.liberarListaMemoria(lista);
		}
	}

	public void montarTurma() throws Exception {
		try {
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				if (getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				} else {
					throw new Exception("Por Favor Informe a Unidade de Ensino.");
				}
				if (getTurmaVO().getSubturma()) {
					setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
				} else {
					setCursoVO(getTurmaVO().getCurso());
				}
			} else {
				throw new Exception("Informe a Turma.");
			}
			if (getTurmaVO().getCodigo() != 0) {
				setMensagemID("msg_dados_consultados");
			} else {
				setMensagemID("Dados não encontratos");
			}
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
			////System.out.println(e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));

			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Boolean getAtivo() {
		if (ativo == null) {
			ativo = Boolean.TRUE;
		}
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getTrancado() {
		if (trancado == null) {
			trancado = Boolean.FALSE;
		}
		return trancado;
	}

	public void setTrancado(Boolean trancado) {
		this.trancado = trancado;
	}

	public Boolean getCancelado() {
		if (cancelado == null) {
			cancelado = Boolean.FALSE;
		}
		return cancelado;
	}

	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
	}

	public Boolean getAbandonado() {
		if (abandonado == null) {
			abandonado = Boolean.FALSE;
		}
		return abandonado;
	}

	public void setAbandonado(Boolean abandonado) {
		this.abandonado = abandonado;
	}

	public Boolean getFormado() {
		if (formado == null) {
			formado = Boolean.TRUE;
		}
		return formado;
	}

	public void setFormado(Boolean formado) {
		this.formado = formado;
	}

	public Boolean getTransferenciaInterna() {
		if (transferenciaInterna == null) {
			transferenciaInterna = Boolean.FALSE;
		}
		return transferenciaInterna;
	}

	public void setTransferenciaInterna(Boolean transferenciaInterna) {
		this.transferenciaInterna = transferenciaInterna;
	}

	public Boolean getTransferenciaExterna() {
		if (transferenciaExterna == null) {
			transferenciaExterna = Boolean.FALSE;
		}
		return transferenciaExterna;
	}

	public void setTransferenciaExterna(Boolean transferenciaExterna) {
		this.transferenciaExterna = transferenciaExterna;
	}

	public Boolean getPreMatricula() {
		if (preMatricula == null) {
			preMatricula = Boolean.FALSE;
		}
		return preMatricula;
	}

	public void setPreMatricula(Boolean preMatricula) {
		this.preMatricula = preMatricula;
	}

	public Boolean getPreMatriculaCancelada() {
		if (preMatriculaCancelada == null) {
			preMatriculaCancelada = Boolean.FALSE;
		}
		return preMatriculaCancelada;
	}

	public void setPreMatriculaCancelada(Boolean preMatriculaCancelada) {
		this.preMatriculaCancelada = preMatriculaCancelada;
	}

	public Boolean getConcluido() {
		if (concluido == null) {
			concluido = Boolean.TRUE;
		}
		return concluido;
	}

	public void setConcluido(Boolean concluido) {
		this.concluido = concluido;
	}

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public Boolean getMostrarCampoAno() {
		if (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0) {
			if (getCursoVO().getPeriodicidade().equals("SE") || getCursoVO().getPeriodicidade().equals("AN")) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
		if (getTurmaVO().getCodigo() != null && getTurmaVO().getCodigo() != 0) {
			if (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getCurso().getPeriodicidade().equals("AN")) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}

	public void limparDadosAluno() {
		setMatriculaVO(null);
	}

	public Boolean getMostrarCampoSemestre() {
		if (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0) {
			if (getCursoVO().getPeriodicidade().equals("SE")) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
		if (getTurmaVO().getCodigo() != null && getTurmaVO().getCodigo() != 0) {
			if (getTurmaVO().getCurso().getPeriodicidade().equals("SE")) {
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}

	public Integer getVerificaFiltro() {
		return verificaFiltro;
	}

	public void setVerificaFiltro(Integer verificaFiltro) {
		this.verificaFiltro = verificaFiltro;
	}

	public Boolean getJubilado() {
		if(jubilado == null) {
			return false;
		}
		return jubilado;
	}

	public void setJubilado(Boolean jubilado) {
		this.jubilado = jubilado;
	}
	
	

}
