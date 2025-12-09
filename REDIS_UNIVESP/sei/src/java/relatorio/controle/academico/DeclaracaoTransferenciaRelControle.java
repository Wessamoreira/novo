package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DeclaracaoTransferenciaRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.interfaces.academico.DeclaracaoTransferenciaRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.DeclaracaoAprovacaoVestRel;
import relatorio.negocio.jdbc.academico.DeclaracaoTransferenciaRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoTransferenciaRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoTransferenciaRelControle extends SuperControleRelatorio {

    protected List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private DeclaracaoTransferenciaRelVO declaracaoTransferenciaRelVO;
    private List listaDeclaracaoTransferencia;
    private Boolean imprimirContrato;
    private Integer textoPadraoDeclaracao;
    private List<SelectItem> listaSelectItemTipoTextoPadrao;
    private MatriculaVO matriculaVO;
    private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
    private String modalListaImpressaoDeclaracao;

    public DeclaracaoTransferenciaRelControle() throws Exception {

        setMensagemID("msg_entre_prmrelatorio");
    }
    
    public void visualizarImpressaoDeclaracaoAluno(){
    	try {    		
    		getDeclaracaoTransferenciaRelVO().setTextoPadraoDeclaracao(getTextoPadraoDeclaracao());
    		DeclaracaoTransferenciaRel.validarDados(getDeclaracaoTransferenciaRelVO());
			setListaImpressaoDeclaracaoVOs(getFacadeFactory().getImpressaoDeclaracaoFacade().consultarPorMatriculaPorTextoPadrao(getMatriculaVO().getMatricula(), getTextoPadraoDeclaracao(), TipoDoTextoImpressaoContratoEnum.TEXTO_PADRAO_DECLARACAO, getUsuarioLogado()));
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

    public void imprimirDeclaracaoPDF() {
        String titulo = null;
        String nomeEntidade = null;
        String design = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoTransferenciaRelControle", "Inicializando Geração de Relatório Declaração Transferência", "Emitindo Relatório");
            DeclaracaoTransferenciaRel.validarDados(getDeclaracaoTransferenciaRelVO());
            titulo = "Declaração de Transferência";
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = DeclaracaoTransferenciaRel.getDesignIReportRelatorio();

            if (!getListaDeclaracaoTransferencia().isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoTransferenciaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoTransferenciaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(getListaDeclaracaoTransferencia());
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }

            //apresentarRelatorioObjetos(DeclaracaoTransferenciaRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", 
            //design, getUsuarioLogado().getNome(), "", getListaDeclaracaoTransferencia(), "");
            //setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoTransferenciaRelControle", "Finalizando Geração de Relatório Declaração Transferência", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            Uteis.liberarListaMemoria(getListaDeclaracaoTransferencia());
        }
    }

    public void imprimirGuiaPDF() {
        String titulo = null;
        String nomeEntidade = null;
        String design = null;
        try {
            DeclaracaoTransferenciaRel.validarDados(getDeclaracaoTransferenciaRelVO());
            titulo = "Guia de Transferência";
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = DeclaracaoTransferenciaRel.getDesignIReportRelatorioGuia();

            if (!getListaDeclaracaoTransferencia().isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoTransferenciaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoTransferenciaRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(getListaDeclaracaoTransferencia());
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            //apresentarRelatorioObjetos(DeclaracaoTransferenciaRel.getIdEntidadeGuia(), titulo, nomeEntidade, "", "PDF", "", 
            //		design, getUsuarioLogado().getNome(), "", getListaDeclaracaoTransferencia(), "");
            //setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            Uteis.liberarListaMemoria(getListaDeclaracaoTransferencia());
        }
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO matriculaVO = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
                        getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                if (!matriculaVO.getMatricula().equals("")) {
                    objs.add(matriculaVO);
                } else {
                    removerObjetoMemoria(matriculaVO);
                }

            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
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
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDeclaracaoTransferenciaRelVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(),
                    true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getDeclaracaoTransferenciaRelVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setDeclaracaoTransferenciaRelVO(getFacadeFactory().getDeclaracaoTransferenciaRelFacade().consultarPorCodigoAluno(objAluno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            if (getListaDeclaracaoTransferencia().size() == 0) {
                getListaDeclaracaoTransferencia().add(getDeclaracaoTransferenciaRelVO());
            } else {
                getListaDeclaracaoTransferencia().set(0, getDeclaracaoTransferenciaRelVO());
            }
            setMatriculaVO(objAluno);
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
            consultarListaSelectItemTipoTextoPadrao(objAluno.getUnidadeEnsino().getCodigo());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        setDeclaracaoTransferenciaRelVO(getFacadeFactory().getDeclaracaoTransferenciaRelFacade().consultarPorCodigoAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
        if (getListaDeclaracaoTransferencia().size() == 0) {
            getListaDeclaracaoTransferencia().add(getDeclaracaoTransferenciaRelVO());
        } else {
            getListaDeclaracaoTransferencia().set(0, getDeclaracaoTransferenciaRelVO());
        }
        setCampoConsultaAluno("");
        setValorConsultaAluno("");
        setListaConsultaAluno(new ArrayList(0));
        setMatriculaVO(obj);
        consultarListaSelectItemTipoTextoPadrao(obj.getUnidadeEnsino().getCodigo());
    }

    public void limparDadosAluno() {
        setListaDeclaracaoTransferencia(new ArrayList(0));
        getDeclaracaoTransferenciaRelVO().setMatricula("");
        getDeclaracaoTransferenciaRelVO().setNome("");
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();        
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }

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
     * @return the declaracaoTransferenciaRelVO
     */
    public DeclaracaoTransferenciaRelVO getDeclaracaoTransferenciaRelVO() {
        if (declaracaoTransferenciaRelVO == null) {
            declaracaoTransferenciaRelVO = new DeclaracaoTransferenciaRelVO();
        }
        return declaracaoTransferenciaRelVO;
    }

    /**
     * @param declaracaoTransferenciaRelVO
     *            the declaracaoTransferenciaRelVO to set
     */
    public void setDeclaracaoTransferenciaRelVO(DeclaracaoTransferenciaRelVO declaracaoTransferenciaRelVO) {
        this.declaracaoTransferenciaRelVO = declaracaoTransferenciaRelVO;
    }

    /**
     * @return the listaDeclaracaoTransferencia
     */
    public List getListaDeclaracaoTransferencia() {
        if (listaDeclaracaoTransferencia == null) {
            listaDeclaracaoTransferencia = new ArrayList(0);
        }
        return listaDeclaracaoTransferencia;
    }

    /**
     * @param listaDeclaracaoTransferencia
     *            the listaDeclaracaoTransferencia to set
     */
    public void setListaDeclaracaoTransferencia(List listaDeclaracaoTransferencia) {
        this.listaDeclaracaoTransferencia = listaDeclaracaoTransferencia;
    }
    
    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("TS", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            for (TextoPadraoDeclaracaoVO objeto : lista) {
                getListaSelectItemTipoTextoPadrao().add(new SelectItem(objeto.getCodigo(), objeto.getDescricao()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
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
	
	public void imprimirPDF2() throws Exception {
		try {
			setCaminhoRelatorio("");
			getDeclaracaoTransferenciaRelVO().setTextoPadraoDeclaracao(getTextoPadraoDeclaracao());
			DeclaracaoTransferenciaRel.validarDados(getDeclaracaoTransferenciaRelVO());
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setCaminhoRelatorio(getFacadeFactory().getDeclaracaoTransferenciaRelFacade().imprimirDeclaracaoTransferencia(getDeclaracaoTransferenciaRelVO(), textoPadraoDeclaracaoVO, getMatriculaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
			if(getCaminhoRelatorio().isEmpty()){
				setImprimirContrato(true);
				setFazerDownload(false);
			}else {
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
        }else if(getFazerDownload()){
        	return getDownload();
        }
        return "";
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
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
