package relatorio.controle.academico;

import java.io.Serializable;
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
import relatorio.negocio.comuns.academico.DeclaracaoAbandonoCursoVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DeclaracaoAbandonoCursoRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoAbandonoCursoRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoAbandonoCursoRelControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	private MatriculaVO matriculaVO;
    private List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaAbandonoCurso;
    private Boolean imprimirContrato;
    private Integer textoPadraoDeclaracao;
    private List<SelectItem> listaSelectItemTipoTextoPadrao;
    private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
    private String modalListaImpressaoDeclaracao;

    public DeclaracaoAbandonoCursoRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }
    
    public void visualizarImpressaoDeclaracaoAluno(){
    	try {    		
    		DeclaracaoAbandonoCursoRel.validarDados(getMatriculaVO(), getTextoPadraoDeclaracao());
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

    public void imprimirPDF() {
        String titulo = null;
        String nomeEntidade = null;
        String design = null;
        String filtros = "";
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoAbandonoCursoRelControle", "Inicializando Geração de Relatório Declaração Abandono de Curso", "Emitindo Relatório");
            DeclaracaoAbandonoCursoRel.validarDados(getMatriculaVO(), getTextoPadraoDeclaracao());
            titulo = "DECLARAÇÃO DE ABANDONO DE CURSO";
            design = DeclaracaoAbandonoCursoRel.getDesignIReportRelatorio();
            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            getFacadeFactory().getDeclaracaoAbandonoCursoRelFacade().setDescricaoFiltros(filtros);

            setListaAbandonoCurso(getFacadeFactory().getDeclaracaoAbandonoCursoRelFacade().criarObjeto(
                    getMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));

            if (!getListaAbandonoCurso().isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoAbandonoCursoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoAbandonoCursoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUnidadeEnsino(nomeEntidade);
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(getListaAbandonoCurso());
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }

            //apresentarRelatorioObjetos(DeclaracaoAbandonoCursoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(),
            //		getFacadeFactory().getDeclaracaoAbandonoCursoRelFacade().getDescricaoFiltros(), getListaAbandonoCurso(), DeclaracaoAbandonoCursoRel.getCaminhoBaseRelatorio());
            //setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoAbandonoCursoRelControle", "Finalizando Geração de Relatório Declaração Abandono de Curso", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            filtros = null;
            setListaAbandonoCurso(null);
            removerObjetoMemoria(this);
        }
    }

    public void consultarAluno() {
        List objs = new ArrayList(0);
        try {
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
        } finally {
            objs = null;
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        MatriculaVO objAluno = new MatriculaVO();
        try {
            objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(),
                    this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                    getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMatriculaVO(objAluno);
            consultarListaSelectItemTipoTextoPadrao(getMatriculaVO().getUnidadeEnsino().getCodigo());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMatriculaVO(new MatriculaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            objAluno = null;
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        setMatriculaVO(obj);
        obj = null;
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        getListaConsultaAluno().clear();
        consultarListaSelectItemTipoTextoPadrao(getMatriculaVO().getUnidadeEnsino().getCodigo());
    }

    public void limparDadosAluno() {
        setMatriculaVO(new MatriculaVO());
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Nome Curso"));
        return itens;
    }

    public String getValorConsultaAluno() {
        if(valorConsultaAluno == null){
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public String getCampoConsultaAluno() {
        if(campoConsultaAluno == null){
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List getListaConsultaAluno() {
        if(listaConsultaAluno == null){
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List getListaAbandonoCurso() {
        if (listaAbandonoCurso == null) {
            listaAbandonoCurso = new ArrayList<DeclaracaoAbandonoCursoVO>(0);
        }
        return listaAbandonoCurso;
    }

    public void setListaAbandonoCurso(List listaAbandonoCurso) {
        this.listaAbandonoCurso = listaAbandonoCurso;
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
    
    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("OT", unidadeEnsino, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
	
	public void imprimirPDF2() throws Exception {
		try {
			setCaminhoRelatorio("");
			TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			setCaminhoRelatorio(getFacadeFactory().getDeclaracaoAbandonoCursoRelFacade().imprimirDeclaracaoAbandonoCurso(getMatriculaVO(), textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
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
    		   return "abrirPopup('../../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
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
