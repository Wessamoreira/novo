package relatorio.controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.ImpressaoDeclaracaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.enumeradores.TipoDoTextoImpressaoContratoEnum;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DeclaracaoConclusaoCursoVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.DeclaracaoConclusaoCursoRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoConclusaoCursoRelControle")
@Scope("viewScope")
@Lazy
public class DeclaracaoConclusaoCursoRelControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;
	protected MatriculaVO matriculaVO;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private List listaConsultaAluno;
    private DeclaracaoConclusaoCursoVO declaracaoConclusaoCursoVO;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List<FuncionarioVO> listaConsultaFuncionario;
    private Boolean imprimirContrato;
    private Integer textoPadraoDeclaracao;
    private List<SelectItem> listaSelectItemTipoTextoPadrao;
    private List<ImpressaoDeclaracaoVO> listaImpressaoDeclaracaoVOs;
    private String modalListaImpressaoDeclaracao;


    public DeclaracaoConclusaoCursoRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }
    
    public void visualizarImpressaoDeclaracaoAluno(){
    	try {    		
    		getDeclaracaoConclusaoCursoVO().setTextoPadraoDeclaracao(getTextoPadraoDeclaracao());
    		DeclaracaoConclusaoCursoRel.validarDados(getMatriculaVO(),getDeclaracaoConclusaoCursoVO());
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
			obj.setTextoPadraoDeclaracao(getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(obj.getTextoPadraoDeclaracao().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));  		
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
        List listaResultado = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoConclusaoCursoRelControle", "Inicializando Geração de Relatório Declaração Conclusão de Curso", "Emitindo Relatório");
            DeclaracaoConclusaoCursoRel.validarDados(getMatriculaVO(),getDeclaracaoConclusaoCursoVO());
            executarVerificacaoDeConclusaoCurso(getMatriculaVO());
            getFacadeFactory().getDeclaracaoConclusaoCursoRelFacade().setDescricaoFiltros("");
            titulo = "RELATÓRIO DE CONCLUSÃO DE CURSO";

            nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
            design = DeclaracaoConclusaoCursoRel.getDesignIReportRelatorio(getDeclaracaoConclusaoCursoVO().getTipoLayout());

            listaResultado = getFacadeFactory().getDeclaracaoConclusaoCursoRelFacade().criarObjeto( getDeclaracaoConclusaoCursoVO(),
                    getMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());

            if (!listaResultado.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(DeclaracaoConclusaoCursoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(DeclaracaoConclusaoCursoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setUsuarioVO(getUsuarioLogadoClone());
                getSuperParametroRelVO().setListaObjetos(listaResultado);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }

            //apresentarRelatorioObjetos(DeclaracaoConclusaoCursoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(),
            //        getFacadeFactory().getDeclaracaoConclusaoCursoRelFacade().getDescricaoFiltros(), listaResultado, DeclaracaoConclusaoCursoRel.getCaminhoBaseRelatorio());
            //setMensagemID("msg_relatorio_ok");
            registrarAtividadeUsuario(getUsuarioLogado(), "DeclaracaoConclusaoCursoRelControle", "Finalizando Geração de Relatório Declaração Conclusão de Curso", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            design = null;
            nomeEntidade = null;
            Uteis.liberarListaMemoria(listaResultado);
            
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see relatorio.negocio.jdbc.academico.DeclaracaoConclusaoCursoRelInterfaceFacade#getApresentarCampos()
     */
    public Boolean getApresentarCampos() {
        if ((getMatriculaVO().getAluno() != null) && (getMatriculaVO().getAluno().getCodigo() != 0)) {
            return true;
        }
        return false;
    }


    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, true, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", 0, true, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, true, true, false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFuncionarioPrincipal() throws Exception {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioPrincipalItens");
        getDeclaracaoConclusaoCursoVO().setFuncionarioPrincipalVO(obj);
    }


    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        return itens;
    }


    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);

            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
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

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        setMatriculaVO(obj);
        consultarAlunoDadosCompletos();
        obj = null;
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        getListaConsultaAluno().clear();
        consultarListaSelectItemTipoTextoPadrao(getMatriculaVO().getUnidadeEnsino().getCodigo());
    }

    public void consultarAlunoDadosCompletos() throws Exception{
        try {
            setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(),this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            consultarListaSelectItemTipoTextoPadrao(getMatriculaVO().getUnidadeEnsino().getCodigo());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(),
                    this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            if (objAluno.getMatricula().equals("")) {
                throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            this.setMatriculaVO(objAluno);
            consultarListaSelectItemTipoTextoPadrao(getMatriculaVO().getUnidadeEnsino().getCodigo());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(null);
        setMensagemID("msg_entre_prmrelatorio");
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List getListaSelectTipoLayout() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem(0, "Declaração de Conclusão de Curso"));
        lista.add(new SelectItem(1, "Declaração de Conclusão de Curso(Layout 2)"));
        lista.add(new SelectItem(2, "Declaração de Conclusão de Curso (Disciplinas)"));
        return lista;
    }


    public void consultarFuncionarioPrincipal() throws Exception {
        try {
            getDeclaracaoConclusaoCursoVO().setFuncionarioPrincipalVO(consultarFuncionarioPorMatricula(getDeclaracaoConclusaoCursoVO().getFuncionarioPrincipalVO().getMatricula()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public FuncionarioVO consultarFuncionarioPorMatricula(String matricula) throws Exception {
        FuncionarioVO funcionarioVO = null;
        try {
            funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, 0, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            if (Uteis.isAtributoPreenchido(funcionarioVO)) {
                return funcionarioVO;
            } else {
                setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
            }
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new FuncionarioVO();
    }


    public void executarVerificacaoDeConclusaoCurso(MatriculaVO matricula) throws Exception {
        getFacadeFactory().getDeclaracaoConclusaoCursoRelFacade().executarVerificacaoDeConclusaoCurso(matricula);
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

    public DeclaracaoConclusaoCursoVO getDeclaracaoConclusaoCursoVO() {
        if (declaracaoConclusaoCursoVO == null){
            declaracaoConclusaoCursoVO = new DeclaracaoConclusaoCursoVO();
        }
        return declaracaoConclusaoCursoVO;
    }

    public void setDeclaracaoConclusaoCursoVO(DeclaracaoConclusaoCursoVO declaracaoConclusaoCursoVO) {
        this.declaracaoConclusaoCursoVO = declaracaoConclusaoCursoVO;
    }

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List<FuncionarioVO> getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = null;
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }


    public void limparDadosFuncionarioPrincipal() {
        removerObjetoMemoria(getDeclaracaoConclusaoCursoVO().getFuncionarioPrincipalVO());
        getDeclaracaoConclusaoCursoVO().setFuncionarioPrincipalVO(new FuncionarioVO());
    }

    public void consultarListaSelectItemTipoTextoPadrao(Integer unidadeEnsino) {
        try {
            getListaSelectItemTipoTextoPadrao().clear();
            List<TextoPadraoDeclaracaoVO> lista = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("CC", 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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
			getDeclaracaoConclusaoCursoVO().setTextoPadraoDeclaracao(getTextoPadraoDeclaracao());
			DeclaracaoConclusaoCursoRel.validarDados(getMatriculaVO(),getDeclaracaoConclusaoCursoVO());
            executarVerificacaoDeConclusaoCurso(getMatriculaVO());
            TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorChavePrimaria(textoPadraoDeclaracao, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            setCaminhoRelatorio(getFacadeFactory().getDeclaracaoConclusaoCursoRelFacade().imprimirDeclaracaoConclusaoCurso(getMatriculaVO(), getDeclaracaoConclusaoCursoVO(), textoPadraoDeclaracaoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado()));
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
	
	public String getUrlDonloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			if(getNomeTelaAtual().contains("/relatorio/")) {
				return "location.href='../../../DownloadSV'";
			}
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	public void realizarDownloadArquivoAssinado() {
		ImpressaoDeclaracaoVO obj = (ImpressaoDeclaracaoVO) context().getExternalContext().getRequestMap().get("impressaoDeclaracaoItens");
		try {
			ArquivoVO arquivoVO = getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getDocumentoAssinadoVO().getArquivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOS_CAMINHO_ARQUIVO_MINIMO, getUsuarioLogado());
			realizarDownloadArquivo(arquivoVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

}
