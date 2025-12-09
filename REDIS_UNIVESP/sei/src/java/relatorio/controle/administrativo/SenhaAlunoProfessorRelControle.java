package relatorio.controle.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.administrativo.SenhaAlunoProfessorVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.administrativo.SenhaAlunoProfessorRel;

@SuppressWarnings("unchecked")
@Controller("SenhaAlunoProfessorRelControle")
@Scope("viewScope")
@Lazy
public class SenhaAlunoProfessorRelControle extends SuperControleRelatorio {

	private List listaConsultaPessoa;
	private String valorConsultaPessoa;
	private String campoConsultaPessoa;
	private String campoConsultaTipoPessoa;
	private Integer campoConsultaFiltro;
	private Integer campoConsultaUnidadeEnsino;
	private Integer campoConsultaCurso;
	private Integer campoConsultaTurno;
	private Integer campoConsultaTurma;
	private SenhaAlunoProfessorVO senhaAlunoProfessorVO;
	private List listaSenhaAlunoProfessorVO;
	private boolean filtrarPorUnidadeEnsino;
	private boolean filtrarPorCurso;
	private boolean filtrarPorTurno;
	private boolean filtrarPorTurma;
	private List<SelectItem> listaTipoConsultaComboFiltro;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemCurso;
	private List<SelectItem> listaSelectItemTurno;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private String periodicidade;
	private Date dataInicio;
	private Date dataFim;
	private String ano;
	private String semestre;
	private String identificadorTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;

	public SenhaAlunoProfessorRelControle() throws Exception {

		// obterUsuarioLogado();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF() {
		try {
			String titulo = "Senha Primeiro Acesso";
			criarObjetos();
			String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
			String design = getFacadeFactory().getSenhaAlunoProfessorRelFacade().getDesignIReportRelatorio();
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(SenhaAlunoProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setListaObjetos(getListaSenhaAlunoProfessorVO());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(SenhaAlunoProfessorRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			// apresentarRelatorioObjetos(SenhaAlunoProfessorRel.getIdEntidade(), titulo,
			// nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), "",
			// getListaSenhaAlunoProfessorVO(), "");
			setMensagemID("msg_relatorio_ok");
			limparDadosFiltro();
		} catch (Exception e) {
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(getListaSenhaAlunoProfessorVO());
		}
	}

	public void criarObjetos() throws Exception {
		setListaSenhaAlunoProfessorVO(getFacadeFactory().getSenhaAlunoProfessorRelFacade().criarObjetos(
				getCampoConsultaFiltro(), getSenhaAlunoProfessorVO(), getListaSenhaAlunoProfessorVO(),
				getCampoConsultaTipoPessoa(), getCaminhoWebImagem(), getCampoConsultaUnidadeEnsino(),
				getCampoConsultaCurso(), getCampoConsultaTurno(), getCampoConsultaTurma(),
				getDataInicio(), getDataFim(), getAno(), getSemestre(), getPeriodicidade(), getFiltroRelatorioAcademicoVO()));
		if (getListaSenhaAlunoProfessorVO().isEmpty()) {
			throw new Exception("Não há dados a serem exibidos com esses parâmetros.");
		}
	}

	public void consultarPessoa() {
		if (getCampoConsultaTipoPessoa().equals("aluno")) {
			consultarAluno();
		} else {
			consultarProfessor();
		}
	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaPessoa().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaPessoa().equals("matricula")) {
				objs = (getFacadeFactory().getPessoaFacade().consultarPorMatriculaTipoPessoa(getValorConsultaPessoa(),
						true, null, null, this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaPessoa().equals("nomePessoa")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeTipoPessoa(getValorConsultaPessoa(), true,
						null, null, this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("nomeCurso")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeCurso(getValorConsultaPessoa(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("nomeUnidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeUnidadeEnsinoTipoPessoa(
						getValorConsultaPessoa(), true, null, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("nomeTurma")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorIdentificadorTurma(getValorConsultaPessoa(),
						this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPessoa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarProfessor() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaPessoa().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaPessoa().equals("matricula")) {
				objs = (getFacadeFactory().getPessoaFacade().consultarPorMatriculaTipoPessoa(getValorConsultaPessoa(),
						null, true, null, this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getCampoConsultaPessoa().equals("nomePessoa")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeTipoPessoa(getValorConsultaPessoa(), null,
						true, null, this.getUnidadeEnsinoLogado().getCodigo(), false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("nomeUnidade")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNomeUnidadeEnsinoTipoPessoa(
						getValorConsultaPessoa(), null, true, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
						getUsuarioLogado());
			}
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaPessoa(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPessoa() throws Exception {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
			setSenhaAlunoProfessorVO(getFacadeFactory().getSenhaAlunoProfessorRelFacade().consultarPorNomeCpf(obj,
					getCampoConsultaTipoPessoa(), getCaminhoWebImagem(), this.getUnidadeEnsinoLogado().getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		setCampoConsultaUnidadeEnsino(0);
		getListaSelectItemUnidadeEnsino().clear();
		getListaSelectItemCurso().clear();
		getListaSelectItemTurno().clear();
		getListaSelectItemTurma().clear();
		List<UnidadeEnsinoVO> listaUnidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(
				codigoUnidadeEnsino, codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				getUsuarioLogado());
		setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(listaUnidadeEnsinoVO, "codigo", "nome"));
	}

	public void montarListaSelectItemCurso() throws Exception {
		limparConsultaTurma();
		getListaConsultaTurma().clear();
		if (getIsFiltrarPorCurso()) {
			getListaSelectItemCurso().clear();
			getListaSelectItemTurno().clear();
			getListaSelectItemTurma().clear();
			List<CursoVO> listaCursoVO = getFacadeFactory().getCursoFacade().consultarPorCodigoUnidadeEnsino(getCampoConsultaUnidadeEnsino(), getPeriodicidade(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(listaCursoVO, "codigo", "nome"));
		}
	}

	public void montarListaSelectItemTurno() throws Exception {
		if (getIsFiltrarPorTurno()) {
			getListaSelectItemTurno().clear();
			getListaSelectItemTurma().clear();
			List<TurnoVO> listaTurnoVO = getFacadeFactory().getTurnoFacade().consultarPorUnidadeEnsinoCurso(getCampoConsultaUnidadeEnsino(), getCampoConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(listaTurnoVO, "codigo", "nome"));
		}
	}

	public void habilitarCamposFiltro() throws Exception {
		limparDados();
		switch (getCampoConsultaFiltro()) {
		case 1:
			setFiltrarPorUnidadeEnsino(true);
			setFiltrarPorCurso(false);
			setFiltrarPorTurno(false);
			setFiltrarPorTurma(false);
			break;
		case 2:
			setFiltrarPorUnidadeEnsino(true);
			setFiltrarPorCurso(true);
			setFiltrarPorTurno(false);
			setFiltrarPorTurma(false);
			break;
		case 3:
			setFiltrarPorUnidadeEnsino(true);
			setFiltrarPorCurso(true);
			setFiltrarPorTurno(true);
			setFiltrarPorTurma(false);
			break;
		case 4:
			setFiltrarPorUnidadeEnsino(true);
			setFiltrarPorCurso(false);
			setFiltrarPorTurno(false);
			setFiltrarPorTurma(true);
			break;
		default:
			setSenhaAlunoProfessorVO(null);
			setFiltrarPorUnidadeEnsino(false);
			setFiltrarPorCurso(false);
			setFiltrarPorTurno(false);
			setFiltrarPorTurma(false);
			break;
		}
		if (getIsFiltrarPorUnidadeEnsino()) {
			montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
		} else {
			setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
		}
	}

	public List<SelectItem> getTipoConsultaComboTipoPessoa() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("professor", "Professor"));
		return itens;
	}

	public void montarTipoConsultaComboFiltro() {
		setListaTipoConsultaComboFiltro(new ArrayList<SelectItem>());
		getListaTipoConsultaComboFiltro().add(new SelectItem("", "Matrícula"));
		if (!getCampoConsultaTipoPessoa().equals("")) {
			getListaTipoConsultaComboFiltro().add(new SelectItem(1, "Unidade Ensino"));
			if (getCampoConsultaTipoPessoa().equals("aluno")) {
				getListaTipoConsultaComboFiltro().add(new SelectItem(2, "Curso"));
				getListaTipoConsultaComboFiltro().add(new SelectItem(3, "Turno"));
				getListaTipoConsultaComboFiltro().add(new SelectItem(4, "Turma"));
			}
		}
		limparDadosFiltro();
		limparDadosPessoa();
		if (getCampoConsultaTipoPessoa().equals("aluno")) {
			montarListaSelectItemPeriodicidade();
		}
	}

	public List<SelectItem> getTipoConsultaComboPessoa() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomePessoa", "Nome"));
		if (!getCampoConsultaTipoPessoa().equals("professor")) {
			itens.add(new SelectItem("nomeCurso", "Nome Curso"));
			itens.add(new SelectItem("nomeTurma", "Nome Turma"));
		}
		return itens;
	}

	public void limparDadosFiltro() {
		setFiltrarPorUnidadeEnsino(false);
		setFiltrarPorCurso(false);
		setFiltrarPorTurno(false);
		setFiltrarPorTurma(false);
		setCampoConsultaFiltro(0);
		getListaSelectItemUnidadeEnsino().clear();
		limparDados();
	}

	public void limparDadosConsultaPessoa() {
		getListaConsultaPessoa().clear();
		setValorConsultaPessoa("");
	}

	public void limparDadosPessoa() {
		setSenhaAlunoProfessorVO(null);
	}

	public boolean getIsVerificarPossuiUnidadeEnsinoLogado() throws Exception {
		if (this.getUnidadeEnsinoLogado().getCodigo() != 0) {
			setValorConsultaPessoa(this.getUnidadeEnsinoLogado().getNome());
			return true;
		} else {
			setValorConsultaPessoa("");
			return false;
		}
	}

	public List getListaSenhaAlunoProfessorVO() {
		if (listaSenhaAlunoProfessorVO == null) {
			listaSenhaAlunoProfessorVO = new ArrayList(0);
		}
		return listaSenhaAlunoProfessorVO;
	}

	public void setListaSenhaAlunoProfessorVO(List listaSenhaAlunoProfessorVO) {
		this.listaSenhaAlunoProfessorVO = listaSenhaAlunoProfessorVO;
	}

	public SenhaAlunoProfessorVO getSenhaAlunoProfessorVO() {
		if (senhaAlunoProfessorVO == null) {
			senhaAlunoProfessorVO = new SenhaAlunoProfessorVO();
		}
		return senhaAlunoProfessorVO;
	}

	public void setSenhaAlunoProfessorVO(SenhaAlunoProfessorVO senhaAlunoProfessorVO) {
		this.senhaAlunoProfessorVO = senhaAlunoProfessorVO;
	}

	/**
	 * @return the listaConsultaPessoa
	 */
	public List getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList(0);
		}
		return listaConsultaPessoa;
	}

	/**
	 * @param listaConsultaPessoa
	 *            the listaConsultaPessoa to set
	 */
	public void setListaConsultaPessoa(List listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	/**
	 * @return the valorConsultaPessoa
	 */
	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	/**
	 * @param valorConsultaPessoa
	 *            the valorConsultaPessoa to set
	 */
	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	/**
	 * @return the campoConsultaPessoa
	 */
	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	/**
	 * @param campoConsultaPessoa
	 *            the campoConsultaPessoa to set
	 */
	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	/**
	 * @return the campoConsultaTipoPessoa
	 */
	public String getCampoConsultaTipoPessoa() {
		if (campoConsultaTipoPessoa == null) {
			campoConsultaTipoPessoa = "";
		}
		return campoConsultaTipoPessoa;
	}

	/**
	 * @param campoConsultaTipoPessoa
	 *            the campoConsultaTipoPessoa to set
	 */
	public void setCampoConsultaTipoPessoa(String campoConsultaTipoPessoa) {
		this.campoConsultaTipoPessoa = campoConsultaTipoPessoa;
	}

	/**
	 * @return the campoConsultaFiltro
	 */
	public Integer getCampoConsultaFiltro() {
		if (campoConsultaFiltro == null) {
			campoConsultaFiltro = 0;
		}
		return campoConsultaFiltro;
	}

	/**
	 * @param campoConsultaFiltro
	 *            the campoConsultaFiltro to set
	 */
	public void setCampoConsultaFiltro(Integer campoConsultaFiltro) {
		this.campoConsultaFiltro = campoConsultaFiltro;
	}

	/**
	 * @return the filtrarPorUnidadeEnsino
	 */
	public boolean getIsFiltrarPorUnidadeEnsino() {
		return filtrarPorUnidadeEnsino;
	}

	/**
	 * @param filtrarPorUnidadeEnsino
	 *            the filtrarPorUnidadeEnsino to set
	 */
	public void setFiltrarPorUnidadeEnsino(boolean filtrarPorUnidadeEnsino) {
		this.filtrarPorUnidadeEnsino = filtrarPorUnidadeEnsino;
	}

	/**
	 * @return the filtrarPorCurso
	 */
	public boolean getIsFiltrarPorCurso() {
		return filtrarPorCurso;
	}

	/**
	 * @param filtrarPorCurso
	 *            the filtrarPorCurso to set
	 */
	public void setFiltrarPorCurso(boolean filtrarPorCurso) {
		this.filtrarPorCurso = filtrarPorCurso;
	}

	/**
	 * @return the filtrarPorTurno
	 */
	public boolean getIsFiltrarPorTurno() {
		return filtrarPorTurno;
	}

	/**
	 * @param filtrarPorTurno
	 *            the filtrarPorTurno to set
	 */
	public void setFiltrarPorTurno(boolean filtrarPorTurno) {
		this.filtrarPorTurno = filtrarPorTurno;
	}

	/**
	 * @return the filtrarPorTurma
	 */
	public boolean getIsFiltrarPorTurma() {
		return filtrarPorTurma;
	}

	/**
	 * @param filtrarPorTurma
	 *            the filtrarPorTurma to set
	 */
	public void setFiltrarPorTurma(boolean filtrarPorTurma) {
		this.filtrarPorTurma = filtrarPorTurma;
	}

	/**
	 * @return the listaTipoConsultaComboFiltro
	 */
	public List<SelectItem> getListaTipoConsultaComboFiltro() {
		if (listaTipoConsultaComboFiltro == null) {
			listaTipoConsultaComboFiltro = new ArrayList<SelectItem>(0);
		}
		return listaTipoConsultaComboFiltro;
	}

	/**
	 * @param listaTipoConsultaComboFiltro
	 *            the listaTipoConsultaComboFiltro to set
	 */
	public void setListaTipoConsultaComboFiltro(List<SelectItem> listaTipoConsultaComboFiltro) {
		this.listaTipoConsultaComboFiltro = listaTipoConsultaComboFiltro;
	}

	/**
	 * @return the listaSelectItemUnidadeEnsino
	 */
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	/**
	 * @param listaSelectItemUnidadeEnsino
	 *            the listaSelectItemUnidadeEnsino to set
	 */
	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	/**
	 * @return the listaSelectItemCurso
	 */
	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<SelectItem>();
		}
		return listaSelectItemCurso;
	}

	/**
	 * @param listaSelectItemCurso
	 *            the listaSelectItemCurso to set
	 */
	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	/**
	 * @return the listaSelectItemTurno
	 */
	public List<SelectItem> getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurno;
	}

	/**
	 * @param listaSelectItemTurno
	 *            the listaSelectItemTurno to set
	 */
	public void setListaSelectItemTurno(List<SelectItem> listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	/**
	 * @return the listaSelectItemTurma
	 */
	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	/**
	 * @param listaSelectItemTurma
	 *            the listaSelectItemTurma to set
	 */
	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * @return the campoConsultaUnidadeEnsino
	 */
	public Integer getCampoConsultaUnidadeEnsino() {
		if (campoConsultaUnidadeEnsino == null) {
			campoConsultaUnidadeEnsino = 0;
		}
		return campoConsultaUnidadeEnsino;
	}

	/**
	 * @param campoConsultaUnidadeEnsino
	 *            the campoConsultaUnidadeEnsino to set
	 */
	public void setCampoConsultaUnidadeEnsino(Integer campoConsultaUnidadeEnsino) {
		this.campoConsultaUnidadeEnsino = campoConsultaUnidadeEnsino;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public Integer getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = 0;
		}
		return campoConsultaCurso;
	}

	/**
	 * @param campoConsultaCurso
	 *            the campoConsultaCurso to set
	 */
	public void setCampoConsultaCurso(Integer campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	/**
	 * @return the campoConsultaTurno
	 */
	public Integer getCampoConsultaTurno() {
		if (campoConsultaTurno == null) {
			campoConsultaTurno = 0;
		}
		return campoConsultaTurno;
	}

	/**
	 * @param campoConsultaTurno
	 *            the campoConsultaTurno to set
	 */
	public void setCampoConsultaTurno(Integer campoConsultaTurno) {
		this.campoConsultaTurno = campoConsultaTurno;
	}

	/**
	 * @return the campoConsultaTurma
	 */
	public Integer getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = 0;
		}
		return campoConsultaTurma;
	}

	/**
	 * @param campoConsultaTurma
	 *            the campoConsultaTurma to set
	 */
	public void setCampoConsultaTurma(Integer campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getConsultarTipoPessoa() {
		if (getCampoConsultaTipoPessoa().equals("aluno")) {
			return "Aluno";
		} else {
			return "Professor";
		}
	}

	public boolean getIsConsultarAlunoProfessorEspecifico() {
		if (getCampoConsultaFiltro() == 0 && !getCampoConsultaTipoPessoa().equals("")) {
			return true;
		}
		return false;
	}

	public String getCaminhoWebImagem() throws Exception {
		return (obterCaminhoWebImagem() + File.separator + "cadeado.png");
	}

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodicidade;
	}

	public void setListaSelectItemPeriodicidade(List<SelectItem> listaSelectItemPeriodicidade) {
		this.listaSelectItemPeriodicidade = listaSelectItemPeriodicidade;
	}

	public void montarListaSelectItemPeriodicidade() {
		getListaSelectItemPeriodicidade().clear();
		getListaSelectItemPeriodicidade().add(new SelectItem("", ""));
		for (PeriodicidadeEnum periodicidadeEnum : PeriodicidadeEnum.values()) {
			getListaSelectItemPeriodicidade().add(new SelectItem(periodicidadeEnum.getValor(), periodicidadeEnum.getDescricao()));
		}
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = "";
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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

	public Boolean getFiltrarPeriodicidadeIntegral() {
		if (getCampoConsultaTipoPessoa().equals("aluno") && getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getFiltrarPeriodicidadeSemestral() {
		if (getCampoConsultaTipoPessoa().equals("aluno") && getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getFiltrarPeriodicidadeAnual() {
		if (getCampoConsultaTipoPessoa().equals("aluno") && getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List<SelectItem> getListaSelectItemSemestres() {
		List<SelectItem> semestres = new ArrayList<>(0);
		semestres.add(new SelectItem("", ""));
		semestres.add(new SelectItem("1", "1"));
		semestres.add(new SelectItem("2", "2"));
		return semestres;
	}

	public void selecionarTurma() {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
		setCampoConsultaTurma(obj.getCodigo());
		setIdentificadorTurma(obj.getIdentificadorTurma());
	}
	
	public String getIdentificadorTurma() {
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}

	public void limparConsultaTurma() {
		setCampoConsultaTurma(0);
		setIdentificadorTurma("");
		setValorConsultaTurma("");
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
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

	public void consultarTurma() {
		try {
			if (getPeriodicidade().equals("")) {
				throw new ConsistirException("Selecione a Periodicidade.");
			}
			if (getCampoConsultaUnidadeEnsino().equals(0)) {
				throw new ConsistirException("Selecione uma Unidade de Ensino.");
			}
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoPeriodicidadeCurso(getValorConsultaTurma(), getCampoConsultaUnidadeEnsino(), getPeriodicidade(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		if (filtroRelatorioAcademicoVO == null) {
			filtroRelatorioAcademicoVO = new FiltroRelatorioAcademicoVO();
		}
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}
	
	public void limparDados() {
		setCampoConsultaUnidadeEnsino(0);
		setCampoConsultaCurso(0);
		setCampoConsultaTurma(0);
		setCampoConsultaTurno(0);
		getListaSelectItemCurso().clear();
		getListaSelectItemTurma().clear();
		getListaSelectItemTurno().clear();
		setDataFim(null);
		setDataInicio(null);
		setAno("");
		setSemestre("");
		setIdentificadorTurma("");
	}
	
	public Boolean getExibirFiltrosAcademicosOuObrigatoriedadePeriodicidade() {
		return getIsFiltrarPorCurso() || getIsFiltrarPorTurma() || getIsFiltrarPorTurno() || getIsFiltrarPorUnidadeEnsino();
	}
	
	public void consultarAlunoProfessorPorMatricula() throws Exception{
		try {
			Boolean aluno = null, professor = null;
			if (getCampoConsultaTipoPessoa().equals("aluno")) {
				aluno = Boolean.TRUE;
			} else {
				professor = Boolean.TRUE;
			}
			PessoaVO pessoaVO = getFacadeFactory().getPessoaFacade().consultarPorMatriculaUnicaTipoPessoa(getSenhaAlunoProfessorVO().getMatricula(), aluno, professor, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuario());
			setSenhaAlunoProfessorVO(getFacadeFactory().getSenhaAlunoProfessorRelFacade().consultarPorNomeCpf(pessoaVO, getCampoConsultaTipoPessoa(), getCaminhoWebImagem(), this.getUnidadeEnsinoLogado().getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
}
