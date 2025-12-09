package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas programacaoFormaturaForm.jsp
 * programacaoFormaturaCons.jsp) com as funcionalidades da classe <code>ProgramacaoFormatura</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ProgramacaoFormatura
 * @see ProgramacaoFormaturaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.basico.enumeradores.ProvedorDeAssinaturaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoColouGrauProgramacaoFormaturaAluno;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.academico.ProgramacaoFormatura; @Controller("RegistroPresencaColacaoGrauControle")
@Scope("viewScope")
@Lazy
public class RegistroPresencaColacaoGrauControle extends SuperControle implements Serializable {

    private ProgramacaoFormaturaVO programacaoFormaturaVO;
    private Date valorConsultaDataInicio;
    private Date valorConsultaDataFinal;
    protected List listaColouGrau;
    private ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO;
    private Boolean mostrarSegundoCampo;
    private String aluno_Erro;
    private List<DocumentoAssinadoVO> documentoAssinadoVOs;
    private Boolean apresentarBotaoAtualizarPresenca;
    private String alunosColouGrau;
    protected List listaAlunoColouGrau;
    private Boolean apresentarModalProgramacaoFormatura;
    private List<ProgramacaoFormaturaAlunoVO> programacaoFormaturaAlunoVOs;
    private Integer qtdProgramacaoFormaturaAlunoDuplicados;
    private Integer qtdProgramacaoFormaturaAlunoNaoDuplicados;

    public RegistroPresencaColacaoGrauControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ProgramacaoFormatura</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() throws Exception {         removerObjetoMemoria(this);
        setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
        getProgramacaoFormaturaVO().setResponsavelCadastro(getUsuarioLogadoClone());
        setProgramacaoFormaturaAlunoVO(new ProgramacaoFormaturaAlunoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroPresencaColacaoGrauForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>ProgramacaoFormatura</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
    		getFacadeFactory().getProgramacaoFormaturaAlunoFacade().alterarProgramacaoFormaturaAlunosPorRegistroPresencaColacaoGrau(getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs(), getUsuarioLogado());
    		setApresentarModalProgramacaoFormatura(Boolean.FALSE);
    		setOncompleteModal("RichFaces.$('panelAvisoDiplicidadeProgramacaoFormaturaAluno').hide();");
    		setMensagemID("msg_dados_gravados");
        	return Uteis.getCaminhoRedirecionamentoNavegacao("registroPresencaColacaoGrauForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("registroPresencaColacaoGrauForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ProgramacaoFormaturaCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
                    Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorProgramacaoUnidadeEnsino(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeTurno")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeTurno(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("identificadorTurmaTurma")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorIdentificadorTurmaTurma(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeAluno")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeAluno(getControleConsulta().getValorConsulta(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorMatriculaMatricula(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUsuario")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorNomeUsuario(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("periodoRequerimento") || getControleConsulta().getCampoConsulta().equals("periodoColacaoGrau") || getControleConsulta().getCampoConsulta().equals("periodoCadastro")) {
                objs = validarDataConsulta(objs);
            }
            if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorRegistroAcademico(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
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

    public List validarDataConsulta(List objs) throws Exception {
        if (getValorConsultaDataFinal() != null && getValorConsultaDataInicio() != null) {
            if (getControleConsulta().getCampoConsulta().equals("periodoRequerimento")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataRequerimento(Uteis.getDateTime(getValorConsultaDataInicio(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinal(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("periodoColacaoGrau")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataColacaoGrau(Uteis.getDateTime(getValorConsultaDataInicio(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinal(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("periodoCadastro")) {
                objs = getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorDataCadastro(Uteis.getDateTime(getValorConsultaDataInicio(), 0, 0, 0),
                        Uteis.getDateTime(getValorConsultaDataFinal(), 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
        } else {
            throw new ConsistirException("Por favor digite uma data válida.");
        }

        return objs;
    }

    public void selecionarProgramacaoFormatura() throws Exception {
        try {
        	ProgramacaoFormaturaVO obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoFormaturaItens");
        	obj.setNovoObj(Boolean.FALSE);
        	getDocumentoAssinadoVOs().clear();
        	setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
        	setDocumentoAssinadoVOs(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorCodigoProgramacaoFormatura(getProgramacaoFormaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        	setApresentarBotaoAtualizarPresenca(Uteis.isAtributoPreenchido(getDocumentoAssinadoVOs()));
        	montarListaSelectItemColouGrau();
        	getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream().forEach( p -> { if(!p.getColouGrau().equals(SituacaoColouGrauProgramacaoFormaturaAluno.COLOU_GRAU.getValor())) { p.setMatriculaAptaInativacaoCredenciasAlunosFormados(true);}});
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public void montarPresencaColacaoGrau() {
        List<ProgramacaoFormaturaAlunoVO> listaProgramacaoFormaturaAluno = getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs();
        for (ProgramacaoFormaturaAlunoVO programacaoFormaturaAluno : listaProgramacaoFormaturaAluno) {
            if (programacaoFormaturaAluno.getColouGrau().equals("NI")) {
                programacaoFormaturaAluno.setColouGrau("SI");
                // programacaoFormaturaAluno.setPermitirAlterarSituacaoColouGrau(false);
            } else {
                // programacaoFormaturaAluno.setPermitirAlterarSituacaoColouGrau(true);
            }
        }
    }

    public void montarListaSelectItemColouGrau() {
        setListaColouGrau(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoColouGrauProgramacaoFormaturaAluno.class, false));
        montarListaSelectItemAlunosColouGrau();
    }

    public boolean getApresentarAlunos() {
        if (getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs() == null || getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().size() == 0) {
            return false;
        }
        return true;
    }

    public void mostrarSegundoCampo() {
        if (getControleConsulta().getCampoConsulta().equals("periodoRequerimento") || getControleConsulta().getCampoConsulta().equals("periodoColacaoGrau")
                || getControleConsulta().getCampoConsulta().equals("periodoCadastro")) {
            setMostrarSegundoCampo(true);
        } else {
            setMostrarSegundoCampo(false);
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
        itens.add(new SelectItem("matricula", "Matricula"));
        itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
        itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
        itens.add(new SelectItem("periodoRequerimento", "Período Requerimento"));
        itens.add(new SelectItem("periodoColacaoGrau", "Período Colação Grau"));
        itens.add(new SelectItem("periodoCadastro", "Período Cadastro"));
        itens.add(new SelectItem("nomeUsuario", "Responsável Cadastro"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroPresencaColacaoGrauForm.xhtml");
    }

    public String getAluno_Erro() {
        return aluno_Erro;
    }

    public void setAluno_Erro(String aluno_Erro) {
        this.aluno_Erro = aluno_Erro;
    }

    public ProgramacaoFormaturaAlunoVO getProgramacaoFormaturaAlunoVO() {
        return programacaoFormaturaAlunoVO;
    }

    public void setProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) {
        this.programacaoFormaturaAlunoVO = programacaoFormaturaAlunoVO;
    }

    public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
        return programacaoFormaturaVO;
    }

    public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
        this.programacaoFormaturaVO = programacaoFormaturaVO;
    }

    /**
     * @return the mostrarSegundoCampo
     */
    public Boolean getMostrarSegundoCampo() {
        if (mostrarSegundoCampo == null) {
            mostrarSegundoCampo = false;
        }
        return mostrarSegundoCampo;
    }

    /**
     * @param mostrarSegundoCampo
     *            the mostrarSegundoCampo to set
     */
    public void setMostrarSegundoCampo(Boolean mostrarSegundoCampo) {
        this.mostrarSegundoCampo = mostrarSegundoCampo;
    }

    /**
     * @return the valorConsultaDataInicio
     */
    public Date getValorConsultaDataInicio() {
        return valorConsultaDataInicio;
    }

    /**
     * @param valorConsultaDataInicio
     *            the valorConsultaDataInicio to set
     */
    public void setValorConsultaDataInicio(Date valorConsultaDataInicio) {
        this.valorConsultaDataInicio = valorConsultaDataInicio;
    }

    /**
     * @return the valorConsultaDataFinal
     */
    public Date getValorConsultaDataFinal() {
        return valorConsultaDataFinal;
    }

    /**
     * @param valorConsultaDataFinal
     *            the valorConsultaDataFinal to set
     */
    public void setValorConsultaDataFinal(Date valorConsultaDataFinal) {
        this.valorConsultaDataFinal = valorConsultaDataFinal;
    }

    /**
     * @return the listaColouGrau
     */
    public List getListaColouGrau() {
        if (listaColouGrau == null) {
            listaColouGrau = new ArrayList(0);
        }
        return listaColouGrau;
    }

    /**
     * @param listaColouGrau
     *            the listaColouGrau to set
     */
    public void setListaColouGrau(List listaColouGrau) {
        this.listaColouGrau = listaColouGrau;
    }
    
    public void realizarVerificacaoDocumentoAssinatura() {
		try {
			List<DocumentoAssinadoVO> listaDocumentosCerti = getDocumentoAssinadoVOs().stream().filter(da -> da.getProvedorDeAssinaturaEnum().isProvedorCertisign() || da.getProvedorDeAssinaturaEnum().isProvedorImprensaOficial()).collect(Collectors.toList());
			List<DocumentoAssinadoVO> listaDocumentosTechCert = getDocumentoAssinadoVOs().stream().filter(da -> da.getProvedorDeAssinaturaEnum().isProvedorTechCert()).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(listaDocumentosCerti)) {
				getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorCertiSign(listaDocumentosCerti, getUsuarioLogado());
			}
            if (Uteis.isAtributoPreenchido(listaDocumentosTechCert)){
                getFacadeFactory().getDocumentoAssinadoFacade().executarProcessamentoDocumentosAssinadoEletronicaMenteValidandoSituacaoAssinaturaPorProvedorTechCert(listaDocumentosCerti, getUsuarioLogado());
            }
			consultarProgramacaoFormaturaAluno();
			setMensagemID("msg_dados_validados", Uteis.SUCESSO);
		}catch (ConsistirException e) {
			setMensagemDetalhada("msg_erro", e.getToStringMensagemErro(), Uteis.ERRO);	
	    } catch (Exception e) {
	    	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);	
		}
    }
    
    private void consultarProgramacaoFormaturaAluno() {
    	getDocumentoAssinadoVOs().clear();
    	try {
    		setProgramacaoFormaturaVO(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorChavePrimaria(getProgramacaoFormaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    		setDocumentoAssinadoVOs(getFacadeFactory().getDocumentoAssinadoFacade().consultarPorCodigoProgramacaoFormatura(getProgramacaoFormaturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    		setApresentarBotaoAtualizarPresenca(getDocumentoAssinadoVOs().stream().map(dav -> dav.getProvedorDeAssinaturaEnum()).anyMatch(c -> c.equals(ProvedorDeAssinaturaEnum.CERTISIGN) || c.equals(ProvedorDeAssinaturaEnum.TECHCERT)));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<DocumentoAssinadoVO> getDocumentoAssinadoVOs() {
		if (documentoAssinadoVOs == null) {
			documentoAssinadoVOs = new ArrayList<DocumentoAssinadoVO>(0);
		}
    	return documentoAssinadoVOs;
	}
    
    public void setDocumentoAssinadoVOs(List<DocumentoAssinadoVO> documentoAssinadoVOs) {
		this.documentoAssinadoVOs = documentoAssinadoVOs;
	}
    
    public Boolean getApresentarBotaoAtualizarPresenca() {
		if (apresentarBotaoAtualizarPresenca == null) {
			apresentarBotaoAtualizarPresenca = false;
		}
    	return apresentarBotaoAtualizarPresenca;
	}
    
    public void setApresentarBotaoAtualizarPresenca(Boolean apresentarBotaoAtualizarPresenca) {
		this.apresentarBotaoAtualizarPresenca = apresentarBotaoAtualizarPresenca;
	}
    
    public String getAlunosColouGrau() {
		if (alunosColouGrau == null) {
			alunosColouGrau = "";
		}
    	return alunosColouGrau;
	}
    
    public void setAlunosColouGrau(String alunosColouGrau) {
		this.alunosColouGrau = alunosColouGrau;
	}
    
	public void montarListaSelectItemAlunosColouGrau() {
    	setListaAlunoColouGrau(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoColouGrauProgramacaoFormaturaAluno.class, true));
    }
    
    public List getListaAlunoColouGrau() {
    	if (listaAlunoColouGrau == null) {
    		listaAlunoColouGrau = new ArrayList<>(0);
    	}
		return listaAlunoColouGrau;
	}
    
    public void setListaAlunoColouGrau(List listaAlunoColouGrau) {
		this.listaAlunoColouGrau = listaAlunoColouGrau;
	}
    
    public void executarListarAlunosColouGrau() {
    	if (getAlunosColouGrau() != "") {
    		getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream().forEach(
    				programacaoFormaAlu-> {
    					if (!programacaoFormaAlu.isPermitirAlterarSituacaoColouGrau() && !programacaoFormaAlu.getExisteProgramacaoFormaturaDuplicada()) {
    						programacaoFormaAlu.setColouGrau(getAlunosColouGrau());;
    					}});
    	}
    }
    
    public void incialiazarVerificacaoProgramacaoParaGravar() {
    	setOncompleteModal(Constantes.EMPTY);
    	setApresentarModalProgramacaoFormatura(Boolean.FALSE);
    	setQtdProgramacaoFormaturaAlunoDuplicados(0);
		setQtdProgramacaoFormaturaAlunoNaoDuplicados(0);
    	try {
    		getFacadeFactory().getProgramacaoFormaturaAlunoFacade().realizarMontagemProgramacaoFormaturaDuplicada(getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs(), getProgramacaoFormaturaAlunoVOs(), getUsuario());
    		if (Uteis.isAtributoPreenchido(getProgramacaoFormaturaAlunoVOs())) {
    			setQtdProgramacaoFormaturaAlunoDuplicados(getProgramacaoFormaturaAlunoVOs().size());
    			setQtdProgramacaoFormaturaAlunoNaoDuplicados(getProgramacaoFormaturaVO().getProgramacaoFormaturaAlunoVOs().stream().filter(p -> !p.getExisteProgramacaoFormaturaDuplicada()).collect(Collectors.toList()).size());
        		setApresentarModalProgramacaoFormatura(Boolean.TRUE);
        		setOncompleteModal("RichFaces.$('panelAvisoDiplicidadeProgramacaoFormaturaAluno').show();");
        		setMensagemID(Constantes.EMPTY);
        	} else {
        		gravar();
        	}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    public Boolean getApresentarModalProgramacaoFormatura() {
		if (apresentarModalProgramacaoFormatura == null) {
			apresentarModalProgramacaoFormatura = Boolean.FALSE;
		}
    	return apresentarModalProgramacaoFormatura;
	}
    
    public void setApresentarModalProgramacaoFormatura(Boolean apresentarModalProgramacaoFormatura) {
		this.apresentarModalProgramacaoFormatura = apresentarModalProgramacaoFormatura;
	}
    
    public List<ProgramacaoFormaturaAlunoVO> getProgramacaoFormaturaAlunoVOs() {
		if (programacaoFormaturaAlunoVOs == null) {
			programacaoFormaturaAlunoVOs = new ArrayList<>(0);
		}
    	return programacaoFormaturaAlunoVOs;
	}
    
    public void setProgramacaoFormaturaAlunoVOs(List<ProgramacaoFormaturaAlunoVO> programacaoFormaturaAlunoVOs) {
		this.programacaoFormaturaAlunoVOs = programacaoFormaturaAlunoVOs;
	}
    
    public void excluirProgramacaoFormaturaAlunoDuplicada() {
    	ProgramacaoFormaturaAlunoVO obj = (ProgramacaoFormaturaAlunoVO) context().getExternalContext().getRequestMap().get("programacaoAlunoItens");
    	try {
			getFacadeFactory().getProgramacaoFormaturaAlunoFacade().excluirPorMatriculaEProgramacaoFormatura(obj);
			obj.setExisteProgramacaoFormaturaDuplicada(Boolean.FALSE);
			obj.setProgramacaoFormaturaVO(null);
			getProgramacaoFormaturaAlunoVOs().removeIf(p -> p.getCodigo().equals(obj.getCodigo()));
			verificarFecharModalProgramacaoFormaturaDuplicada();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    private void verificarFecharModalProgramacaoFormaturaDuplicada() {
    	if (!Uteis.isAtributoPreenchido(getProgramacaoFormaturaAlunoVOs())) {
    		setApresentarModalProgramacaoFormatura(Boolean.FALSE);
    		setOncompleteModal("RichFaces.$('panelAvisoDiplicidadeProgramacaoFormaturaAluno').hide();");
    	}
    }

    public Integer getQtdProgramacaoFormaturaAlunoDuplicados() {
		if (qtdProgramacaoFormaturaAlunoDuplicados == null) {
			qtdProgramacaoFormaturaAlunoDuplicados = 0;
		}
    	return qtdProgramacaoFormaturaAlunoDuplicados;
	}
    
    public void setQtdProgramacaoFormaturaAlunoDuplicados(Integer qtdProgramacaoFormaturaAlunoDuplicados) {
		this.qtdProgramacaoFormaturaAlunoDuplicados = qtdProgramacaoFormaturaAlunoDuplicados;
	}
    
    public Integer getQtdProgramacaoFormaturaAlunoNaoDuplicados() {
		if (qtdProgramacaoFormaturaAlunoNaoDuplicados == null) {
			qtdProgramacaoFormaturaAlunoNaoDuplicados = 0;
		}
    	return qtdProgramacaoFormaturaAlunoNaoDuplicados;
	}
    
    public void setQtdProgramacaoFormaturaAlunoNaoDuplicados(Integer qtdProgramacaoFormaturaAlunoNaoDuplicados) {
		this.qtdProgramacaoFormaturaAlunoNaoDuplicados = qtdProgramacaoFormaturaAlunoNaoDuplicados;
	}
}
