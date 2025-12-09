package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.SerasaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.negocio.comuns.financeiro.SerasaRelVO; @Controller("SerasaControle")
@Scope("request")
@Lazy
public class SerasaControle extends SuperControle implements Serializable {

	private SerasaVO serasaVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	private MatriculaVO matriculaVO;

	private List listaSelectItemUnidadeEnsino;

	private String filtro;

	private Boolean filtroUnidadeEnsino;

	private Boolean filtroAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List listaConsultaAluno;

	private Boolean filtroCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;

	private Boolean filtroTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;

	private List<SerasaRelVO> serasaRelVOs;

	private List<ContaReceberVO> contaReceberVOs;

	public SerasaControle() throws Exception {
		//obterUsuarioLogado();
		novo();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {         removerObjetoMemoria(this);
		try {
			setFiltro("todos");
			montarListaSelectItemUnidadeEnsino();
			setSerasaVO(new SerasaVO());
			setSerasaRelVOs(new ArrayList<SerasaRelVO>(0));
			setarFalseNosFiltros();
			getSerasaVO().setResponsavel(getUsuarioLogadoClone());
			setMensagemID("msg_entre_dados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String gravar() {
		try {
			getFacadeFactory().getSerasaFacade().incluir(getSerasaVO());
			setMensagemID("msg_dados_gravados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void visualizarContas() {
		try {
			SerasaRelVO serasaRelVO = (SerasaRelVO) context().getExternalContext().getRequestMap().get("serasaRelVO");
//			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPadraoSistema();
			setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaParaSerasa(serasaRelVO.getMatriculaAluno(), configuracaoFinanceiroVO));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gerarArquivo() {
		try {
			if (getSerasaVO().getUnidadeEnsino().getCodigo() == 0) {
				if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() != 0) {
					getSerasaVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoCursoVO().getUnidadeEnsino());
				} else if (getTurmaVO().getCodigo() != 0) {
					getSerasaVO().getUnidadeEnsino().setCodigo(getTurmaVO().getUnidadeEnsino().getCodigo());
				} else if (!getMatriculaVO().getMatricula().equals("")) {
					getSerasaVO().getUnidadeEnsino().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
				}
			}
			getFacadeFactory().getSerasaFacade().validarDados(getSerasaVO());
			String caminhoPasta = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp();
			getFacadeFactory().getSerasaFacade().gerarDadosArquivoSerasa(getSerasaVO(), getSerasaRelVOs(), caminhoPasta, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema());
			setMensagemID("msg_arquivoGerado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFiltro() throws Exception {
		setarFalseNosFiltros();
		setarNewNosObjetosDeConsulta();
		if (getFiltro().equals("unidadeEnsino")) {
			setFiltroUnidadeEnsino(true);
		} else if (getFiltro().equals("curso")) {
			setFiltroCurso(true);
		} else if (getFiltro().equals("turma")) {
			setFiltroTurma(true);
		} else if (getFiltro().equals("aluno")) {
			setFiltroAluno(true);
		}
	}

	private void setarFalseNosFiltros() {
		setFiltroUnidadeEnsino(false);
		setFiltroCurso(false);
		setFiltroTurma(false);
		setFiltroAluno(false);
	}

	private void setarNewNosObjetosDeConsulta() {
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setTurmaVO(new TurmaVO());
		setMatriculaVO(new MatriculaVO());
		setSerasaRelVOs(new ArrayList<SerasaRelVO>(0));
	}

	public void consultarAlunosSerasa() {
		try {
			if (!getFiltro().equals("")) {
				setSerasaRelVOs(getFacadeFactory().getSerasaRelFacade().consultarAlunosSerasa(getFiltro(), getSerasaVO().getUnidadeEnsino().getCodigo(),
						getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getMatriculaVO().getMatricula(),getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			} else {
				throw new ConsistirException("Por favor, selecione um filtro para prosseguir!");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboFiltro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		itens.add(new SelectItem("aluno", "Aluno"));
		return itens;
	}

	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getSerasaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
				objs = getFacadeFactory().getSerasaFacade().consultarPorDataGeracao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(new Date(), 23, 59, 59), true,
						Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("dataGeracao", "Data da Geração"));
		return itens;
	}

	public boolean isCampoData() {
		if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
			return true;
		}
		return false;
	}

	/**
	 * Métodos do rich:modalPanel de Aluno.
	 * */

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("aluno");
		if (getMensagemDetalhada().equals("")) {
			setMatriculaVO(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultaAluno());
		this.setValorConsultaAluno(null);
		this.setCampoConsultaAluno(null);
	}

	public void limparAluno() {
		setMatriculaVO(new MatriculaVO());
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matricula"));
		return itens;
	}

	/**
	 * Métodos do rich:modalPanel de Turma.
	 * */

	public void consultarTurma() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("codigo")) {
				if (getValorConsultaTurma().equals("")) {
					setValorConsultaTurma("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaTurma());
				objs = getFacadeFactory().getTurmaFacade().consultarPorCodigo(valorInt, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
		if (getMensagemDetalhada().equals("")) {
			setTurmaVO(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultaTurma());
		this.setValorConsultaTurma(null);
		this.setCampoConsultaTurma(null);
	}

	public void limparTurma() {
		setTurmaVO(new TurmaVO());
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador Turma"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Métodos do rich:modalPanel de Curso.
	 * */

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCurso(valorInt, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCurso(getValorConsultaCurso(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCurso");
		if (getMensagemDetalhada().equals("")) {
			setUnidadeEnsinoCursoVO(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultaCurso());
		this.setValorConsultaCurso(null);
		this.setCampoConsultaCurso(null);
	}

	public void limparCurso() {
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome Curso"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public String getMascaraConsulta() {
		return "";
	}

	public void imprimirPDF() {    
		executarMetodoControle("SerasaRelControle", "imprimirPDF", getSerasaRelVOs());
	}

	public String inicializarConsultar() {         removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public Boolean getApresentarDataTableAlunos() {
		if (getSerasaRelVOs().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void selecionarArquivo() {
		SerasaVO serasaVO = (SerasaVO) context().getExternalContext().getRequestMap().get("serasa");
		setSerasaVO(serasaVO);
	}
	
	public String getDownloadArquivoTelaCons() {
		try {
			selecionarArquivo();
            HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
            //request.setAttribute("codigoArquivo", getSerasaVO().getArquivoSerasa().getCodigo());
//            request.setAttribute("urlAcessoArquivo", getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getSerasaVO().getArquivoSerasa(), PastaBaseArquivoEnum.SERASA, getConfiguracaoGeralPadraoSistema(), request.getRemoteAddr()));
            context().getExternalContext().dispatch("/DownloadSV");
            FacesContext.getCurrentInstance().responseComplete();
            return "";
		} catch (Exception e) {
			setMensagemDetalhada("Erro");
		}
		return "";
	}
	
	public String getDownloadArquivoTelaForm() {
		try {
            HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
            //request.setAttribute("codigoArquivo", getSerasaVO().getArquivoSerasa().getCodigo());
            //request.setAttribute("urlAcessoArquivo", getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getSerasaVO().getArquivoSerasa(), PastaBaseArquivoEnum.SERASA, getConfiguracaoGeralPadraoSistema(), request.getRemoteAddr()));
            context().getExternalContext().dispatch("/DownloadSV");
            FacesContext.getCurrentInstance().responseComplete();
            return "";
		} catch (Exception e) {
			setMensagemDetalhada("Erro");
		}
		return "";
	}

	public boolean isApresentarBotaoDownload() {
		return !getSerasaVO().isNovoObj();
	}

	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
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

	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
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
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public boolean getFiltroAluno() {
		if (filtroAluno == null) {
			filtroAluno = false;
		}
		return filtroAluno;
	}

	public void setFiltroAluno(Boolean filtroAluno) {
		this.filtroAluno = filtroAluno;
	}

	public boolean getFiltroCurso() {
		if (filtroCurso == null) {
			filtroCurso = false;
		}
		return filtroCurso;
	}

	public void setFiltroCurso(Boolean filtroCurso) {
		this.filtroCurso = filtroCurso;
	}

	public boolean getFiltroTurma() {
		if (filtroTurma == null) {
			filtroTurma = false;
		}
		return filtroTurma;
	}

	public void setFiltroTurma(Boolean filtroTurma) {
		this.filtroTurma = filtroTurma;
	}

	public SerasaVO getSerasaVO() {
		if (serasaVO == null) {
			serasaVO = new SerasaVO();
		}
		return serasaVO;
	}

	public void setSerasaVO(SerasaVO serasaVO) {
		this.serasaVO = serasaVO;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public Boolean getFiltroUnidadeEnsino() {
		if (filtroUnidadeEnsino == null) {
			filtroUnidadeEnsino = false;
		}
		return filtroUnidadeEnsino;
	}

	public void setFiltroUnidadeEnsino(Boolean filtroUnidadeEnsino) {
		this.filtroUnidadeEnsino = filtroUnidadeEnsino;
	}

	public String getFiltro() {
		if (filtro == null) {
			filtro = "";
		}
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
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

	public void setSerasaRelVOs(List<SerasaRelVO> serasaRelVOs) {
		this.serasaRelVOs = serasaRelVOs;
	}

	public List<SerasaRelVO> getSerasaRelVOs() {
		if (serasaRelVOs == null) {
			serasaRelVOs = new ArrayList<SerasaRelVO>(0);
		}
		return serasaRelVOs;
	}

	public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
		this.contaReceberVOs = contaReceberVOs;
	}

	public List<ContaReceberVO> getContaReceberVOs() {
		if (contaReceberVOs == null) {
			contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		}
		return contaReceberVOs;
	}

}