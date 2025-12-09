package relatorio.controle.academico;

//import flex.messaging.io.ArrayCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DeclaracaoPasseEstudantilVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.interfaces.academico.DeclaracaoPasseEstudantilRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.DeclaracaoPasseEstudantilRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoPasseEstudantilRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoPasseEstudantilRelControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	protected DeclaracaoPasseEstudantilRel declaracaoPasseEstudantilRel;
    protected List listaConsultaAluno;
    private String observacao;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private DeclaracaoPasseEstudantilVO declaracaoPasseEstudantilVO;
    private List listaDeclaracao;
    private Boolean imprimirContrato;
    private Integer textoPadraoDeclaracao;
    private List<SelectItem> listaSelectItemTipoTextoPadrao;
    private MatriculaVO matriculaVO;
    private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
    private String modalListaImpressaoDeclaracao;

    public DeclaracaoPasseEstudantilRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }
    
    public void visualizarImpressaoDeclaracaoAluno(){
    	try {
    		getDeclaracaoPasseEstudantilVO().setTextoPadrao(getTextoPadraoDeclaracao());
			DeclaracaoPasseEstudantilRel.validarDados(getDeclaracaoPasseEstudantilVO());
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
    
    public void imprimirPDF2() throws Exception {
		try {
			getDeclaracaoPasseEstudantilVO().setObservacao(getObservacao());
			getDeclaracaoPasseEstudantilVO().setTextoPadrao(getTextoPadraoDeclaracao());
			DeclaracaoPasseEstudantilRel.validarDados(getDeclaracaoPasseEstudantilVO());
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setCaminhoRelatorio(getFacadeFactory().getDeclaracaoPasseEstudantilRelFacade().imprimirDeclaracaoPasseEstudantil(getDeclaracaoPasseEstudantilVO(), textoPadraoDeclaracaoVO, getMatriculaVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
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
            return "abrirPopup('../../../VisualizarContrato', 'RelatorioContrato', 730, 545); ";
        }else if(getFazerDownload()){
        	return getDownload();
        }
        return "";
    }

    public void imprimirPDF() {
        String titulo = "Declaração para Passe Estudantil";
        Iterator i = getListaDeclaracao().iterator();
        while (i.hasNext()) {
            DeclaracaoPasseEstudantilVO de = (DeclaracaoPasseEstudantilVO)i.next();
            de.setObservacao(getObservacao());
        }
        String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
        String design = DeclaracaoPasseEstudantilRel.getDesignIReportRelatorio();
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoPasseEstudantilRelControle", "Inicializando Geração de Relatório Declaração Passe Estudantil", "Emitindo Relatório");
            DeclaracaoPasseEstudantilRel.validarDados(getDeclaracaoPasseEstudantilVO());
            if (!getListaDeclaracao().isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoPasseEstudantilRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoPasseEstudantilRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(getListaDeclaracao());
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoPasseEstudantilRelControle", "Finalizando Geração de Relatório Declaração Passe Estudantil", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            Uteis.liberarListaMemoria(listaDeclaracao);
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
                if (!matriculaVO.getMatricula().isEmpty()) {
                    objs.add(matriculaVO);
                } else {
                    matriculaVO = null;
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
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDeclaracaoPasseEstudantilVO().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getDeclaracaoPasseEstudantilVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            List<MatriculaPeriodoVO> lista = new ArrayList<MatriculaPeriodoVO>(0);
            lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(objAluno.getMatricula(),
                    false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            boolean alunoFormado = true;
            for (MatriculaPeriodoVO matPd : lista) {
                if (matPd.getSituacaoMatriculaPeriodo().equals("AT")) {
                    alunoFormado = false;
                }
            }
            if (alunoFormado) {
                setDeclaracaoPasseEstudantilVO(new DeclaracaoPasseEstudantilVO());
                if (getListaDeclaracao().size() == 0) {
                    getListaDeclaracao().add(getDeclaracaoPasseEstudantilVO());
                } else {
                    getListaDeclaracao().set(0, getDeclaracaoPasseEstudantilVO());
                }
                throw new Exception("Aluno de matrícula " + getDeclaracaoPasseEstudantilVO().getMatricula() + " está inativo.");
            }
            setDeclaracaoPasseEstudantilVO(getFacadeFactory().getDeclaracaoPasseEstudantilRelFacade().consultarPorCodigoAluno(objAluno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getObservacao(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            if (getListaDeclaracao().size() == 0) {
                getListaDeclaracao().add(getDeclaracaoPasseEstudantilVO());
            } else {
                getListaDeclaracao().set(0, getDeclaracaoPasseEstudantilVO());
            }
            setMatriculaVO(objAluno);
            consultarListaSelectItemTipoTextoPadrao(objAluno.getUnidadeEnsino().getCodigo());
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
            List<MatriculaPeriodoVO> lista = new ArrayList<MatriculaPeriodoVO>(0);
            lista = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            boolean alunoFormado = true;
            for (MatriculaPeriodoVO matPd : lista) {
                if (matPd.getSituacaoMatriculaPeriodo().equals("AT")) {
                    alunoFormado = false;
                }
            }
            if (alunoFormado) {
                setDeclaracaoPasseEstudantilVO(new DeclaracaoPasseEstudantilVO());
                if (getListaDeclaracao().size() == 0) {
                    getListaDeclaracao().add(getDeclaracaoPasseEstudantilVO());
                } else {
                    getListaDeclaracao().set(0, getDeclaracaoPasseEstudantilVO());
                }
                throw new Exception("Aluno de matrícula " + getDeclaracaoPasseEstudantilVO().getMatricula() + " está inativo.");
            }
            setDeclaracaoPasseEstudantilVO(getFacadeFactory().getDeclaracaoPasseEstudantilRelFacade().consultarPorCodigoAluno(obj,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getObservacao(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            if (getListaDeclaracao().size() == 0) {
                getListaDeclaracao().add(getDeclaracaoPasseEstudantilVO());
            } else {
                getListaDeclaracao().set(0, getDeclaracaoPasseEstudantilVO());
            }
            setMatriculaVO(obj);
            consultarListaSelectItemTipoTextoPadrao(obj.getUnidadeEnsino().getCodigo());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setCampoConsultaAluno("");
            setValorConsultaAluno("");
            setListaConsultaAluno(new ArrayList(0));
        }
    }

    public void limparDadosAluno() {
        getDeclaracaoPasseEstudantilVO().setMatricula("");
        getDeclaracaoPasseEstudantilVO().setNome("");
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

    protected void setDeclaracaoPasseEstudantilRel(DeclaracaoPasseEstudantilRel declaracaoPasseEstudantilRel) {
        this.declaracaoPasseEstudantilRel = declaracaoPasseEstudantilRel;
    }

    protected DeclaracaoPasseEstudantilRelInterfaceFacade getDeclaracaoPasseEstudantilRel() {
        if (declaracaoPasseEstudantilRel == null) {
            declaracaoPasseEstudantilRel = new DeclaracaoPasseEstudantilRel();
        }
        return declaracaoPasseEstudantilRel;
    }

    /**
     * @param declaracaoSetranspVO
     *            the declaracaoSetranspVO to set
     */
    public void setDeclaracaoPasseEstudantilVO(DeclaracaoPasseEstudantilVO declaracaoPasseEstudantilVO) {
        this.declaracaoPasseEstudantilVO = declaracaoPasseEstudantilVO;
    }

    /**
     * @return the declaracaoSetranspVO
     */
    public DeclaracaoPasseEstudantilVO getDeclaracaoPasseEstudantilVO() {
        if (declaracaoPasseEstudantilVO == null) {
            declaracaoPasseEstudantilVO = new DeclaracaoPasseEstudantilVO();
        }
        return declaracaoPasseEstudantilVO;
    }

    /**
     * @param listaDeclaracao
     *            the listaDeclaracao to set
     */
    public void setListaDeclaracao(List listaDeclaracao) {
        this.listaDeclaracao = listaDeclaracao;
    }

    /**
     * @return the listaDeclaracao
     */
    public List getListaDeclaracao() {
        if (listaDeclaracao == null) {
            listaDeclaracao = new ArrayList<>(0);
        }
        return listaDeclaracao;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    
    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("PE", 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
