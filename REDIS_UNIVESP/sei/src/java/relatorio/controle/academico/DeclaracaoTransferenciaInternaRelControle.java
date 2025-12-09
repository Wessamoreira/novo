package relatorio.controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;










import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.facade.jdbc.academico.TransferenciaEntrada;
import relatorio.controle.arquitetura.SuperControleRelatorio;


@Controller("DeclaracaoTransferenciaInternaRelControle")
@Lazy
@Scope("viewScope")
public class DeclaracaoTransferenciaInternaRelControle extends SuperControleRelatorio implements Serializable {
	
	private static final long serialVersionUID = 851936469734170483L;
	protected List listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private Boolean matriculado;
    private Boolean imprimirContrato;
    private Integer textoPadraoDeclaracao;
    private List<SelectItem> listaSelectItemTipoTextoPadrao;    
    private TransferenciaEntradaVO transferenciaEntradaVO;
    private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
	private String modalListaImpressaoDeclaracao;
	
	@PostConstruct
	public void inicializarDadosRequisicao() {
		try {
			String matricula = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("matricula");
			if (matricula != null && !matricula.trim().isEmpty()) {
				getTransferenciaEntradaVO().getMatricula().setMatricula(matricula);
				consultarAlunoPorMatricula();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	
	 /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TransferenciaEntradaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta,
     * disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!matriculaVO.getMatricula().isEmpty()) {
					objs.add(matriculaVO);
				} else {
					matriculaVO = null;
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarAlunoPorMatricula() throws Exception {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getTransferenciaEntradaVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			preencherDadosMatriculaAluno(objAluno);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			getListaConsultaAluno().clear();
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			preencherDadosMatriculaAluno(obj);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setCampoConsultaAluno("");
			setValorConsultaAluno("");
			setListaConsultaAluno(new ArrayList(0));
		}
	}

	public void preencherDadosMatriculaAluno(MatriculaVO matricula) throws Exception {
		if (matricula.getMatricula().equals("")) {
			throw new Exception("Aluno de matrícula " + getTransferenciaEntradaVO().getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
		}
		getTransferenciaEntradaVO().setMatricula(matricula);
		consultarListaSelectItemTipoTextoPadrao(getTransferenciaEntradaVO().getMatricula().getUnidadeEnsino().getCodigo());
	}

	public void visualizarImpressaoDeclaracaoAluno() {
		try {
			TransferenciaEntrada.validarDadosAntesImpressao(transferenciaEntradaVO, textoPadraoDeclaracao);
			setListaImpressaoDeclaracaoVOs(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarPorMatriculaPorTextoPadrao(getTransferenciaEntradaVO().getMatricula().getMatricula(), getTextoPadraoDeclaracao(), TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO, getUsuarioLogado()));
			setModalListaImpressaoDeclaracao("RichFaces.$('panelVisualizacaoImpressaoDeclaracao').show();");
		} catch (Exception e) {
			setModalListaImpressaoDeclaracao("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void impressaoDeclaracaoContratoJaGerada() {
		ImpressaoDeclaracaoVO obj = (ImpressaoDeclaracaoVO) context().getExternalContext().getRequestMap().get("impressaoDeclaracaoItens");
		try {
			limparMensagem();
			this.setCaminhoRelatorio("");
			ImpressaoContratoVO impressaoContrato = new ImpressaoContratoVO();
			impressaoContrato.setTipoTextoEnum(TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO);
			impressaoContrato.setGerarNovoArquivoAssinado(false);
			impressaoContrato.setMatriculaVO(obj.getMatricula());
			setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().executarValidacaoImpressaoEmPdf(impressaoContrato, obj.getTextoPadraoDeclaracao(), "", true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			setImprimirContrato(false);
			setFazerDownload(true);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setImprimirContrato(false);
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDF2() throws Exception {
		try {
			TransferenciaEntrada.validarDadosAntesImpressao(transferenciaEntradaVO, textoPadraoDeclaracao);
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setCaminhoRelatorio(getFacadeFactory().getTransferenciaEntradaFacade().imprimirDeclaracaoTransferenciaInterna(getTransferenciaEntradaVO(), textoPadraoDeclaracaoVO, getTransferenciaEntradaVO().getMatricula(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			if (getCaminhoRelatorio().isEmpty()) {
				setImprimirContrato(true);
				setFazerDownload(false);
			} else {
				setImprimirContrato(false);
				setFazerDownload(true);
			}
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setImprimirContrato(false);
			setFazerDownload(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../../VisualizarContrato', 'RelatorioContrato', 730, 545); RichFaces.$('panelTextoPadraoDeclaracao').hide()";
		} else if (getFazerDownload()) {
			return getDownload();
		}
		return "";
	}

	public void imprimirDeclaracao() throws Exception {
		try {
			TransferenciaEntradaVO obj = (TransferenciaEntradaVO) context().getExternalContext().getRequestMap().get("transferenciaInternaItens");
			setTransferenciaEntradaVO(getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			getTransferenciaEntradaVO().setNovoObj(Boolean.FALSE);
			consultarListaSelectItemTipoTextoPadrao(obj.getUnidadeEnsino().getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());	          
		}
	}
	
	/**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("matricula", "Matricula"));
        itens.add(new SelectItem("pessoa", "Aluno"));
        itens.add(new SelectItem("curso", "Curso"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("requerimento", "Requerimento"));
//        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }
	
	public void imprimirPDF(TransferenciaEntradaVO transferenciaEntradaVO) {
		try {
			
			String titulo = "DECLARAÇÃO DE TRANSFERÊNCIA INTERNA";
			List<TransferenciaEntradaVO> listaObjtos = new ArrayList<TransferenciaEntradaVO>(0);
			listaObjtos.add(transferenciaEntradaVO);			
			String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
			String design = "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator+"DeclaracaoTransferenciaInterna.jrxml";
			apresentarRelatorioObjetos("DeclaracaoTransferenciaInterna", titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(),
					"", listaObjtos, "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	 public Boolean getMatriculado() {
	        return matriculado;
	    }

	    public void setMatriculado(Boolean matriculado) {
	        this.matriculado = matriculado;
	    }

	public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
		try {
			getListaSelectItemTipoTextoPadrao().clear();
			List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("TI", 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			for (TextoPadraoDeclaracaoVO objeto : lista) {
				getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoTextoPadrao() {
		if (listaSelectItemTipoTextoPadrao == null) {
			listaSelectItemTipoTextoPadrao = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoTextoPadrao;
	}

	public void setListaSelectItemTipoTextoPadrao(List<SelectItem> listaSelectItemTipoTextoPadrao) {
		this.listaSelectItemTipoTextoPadrao = listaSelectItemTipoTextoPadrao;
	}

	public Integer getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = 0;
		}
		return textoPadraoDeclaracao;
	}

	public void setTextoPadraoDeclaracao(Integer textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}	

	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public TransferenciaEntradaVO getTransferenciaEntradaVO() {
		if (transferenciaEntradaVO == null) {
			transferenciaEntradaVO = new TransferenciaEntradaVO();
		}
		return transferenciaEntradaVO;
	}

	public void setTransferenciaEntradaVO(TransferenciaEntradaVO transferenciaEntradaVO) {
		this.transferenciaEntradaVO = transferenciaEntradaVO;
	}
	public void limparDadosAluno() {
		getTransferenciaEntradaVO().setMatricula(new MatriculaVO());
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Nome Curso"));
		return itens;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		if (valorConsultaAluno == null) {
			valorConsultaAluno = "";
		}
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}
	
	public List<ImpressaoDeclaracaoVO> getListaImpressaoDeclaracaoVOs() {
		if (listaImpressaoDeclaracaoVOs == null) {
			listaImpressaoDeclaracaoVOs = new ArrayList<ImpressaoDeclaracaoVO>(0);
		}
		return listaImpressaoDeclaracaoVOs;
	}

	public void setListaImpressaoDeclaracaoVOs(List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs) {
		this.listaImpressaoDeclaracaoVOs = listaImpressaoDeclaracaoVOs;
	}

	public String getModalListaImpressaoDeclaracao() {
		if (modalListaImpressaoDeclaracao == null) {
			modalListaImpressaoDeclaracao = "";
		}
		return modalListaImpressaoDeclaracao;
	}

	public void setModalListaImpressaoDeclaracao(String modalListaImpressaoDeclaracao) {
		this.modalListaImpressaoDeclaracao = modalListaImpressaoDeclaracao;
	}

	
}
