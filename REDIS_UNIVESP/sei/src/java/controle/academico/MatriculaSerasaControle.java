package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.SituacaoFinanceiraAlunoRelControle;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Controller("MatriculaSerasaControle")
@Scope("viewScope")
@Lazy
public class MatriculaSerasaControle extends SuperControle implements Serializable {

    private List listaConsultaContaReceber;
    private List listaMatriculaSerasaPagaram;
    private MatriculaVO matriculaVO;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private List listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List listaSelectItemUnidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private String abaAtual;
    

    /***
	 * Campos utilizados para filtro
	 */
    private String matriculaNegativaSerasaMatricula;
    private String matriculaNegativaSerasaNomeAluno;
    private String matriculaNegativaSerasaCurso;
    private String matriculaNegativaSerasaTurma;
    private String matriculaNegativaSerasaDataMatricula;
    
    private String matriculaNegativaParcelaMatricula;
    private String matriculaNegativaParcelaNomeAluno;
    private String matriculaNegativaParcelaCurso;
    private String matriculaNegativaParcelaTurma;
    private String matriculaNegativaParcelaDataMatricula;
	
    public MatriculaSerasaControle() {
        montarListaSelectItemUnidadeEnsino();
        consultarMatriculaSerasa();
    }

    @PostConstruct
    public void serasaApartirInteracaoWorkflow() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String matricula = (String) context().getExternalContext().getSessionMap().get("matriculaItem");
			if (matricula != null && !matricula.trim().equals("")) {
				setAbaAtual("tabFim");
                MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(matricula,
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                setMatriculaVO(objs);
                setUnidadeEnsinoVO(objs.getUnidadeEnsino());
				context().getExternalContext().getSessionMap().remove("matriculaItem");
			}            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void consultarMatriculaSerasa() {
        try {
            setListaConsulta(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasa(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setListaMatriculaSerasaPagaram(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasaPagaramParcela(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void visualizarDetalhes() throws Exception {
        MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
        try {
            setListaConsultaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaEUnidadeEnsino(obj.getMatricula(), "VE", obj.getMatriculaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()));
        } catch (Exception e) {
            setListaConsultaContaReceber(new ArrayList());
        }
    }

    public void preparaCancelarMatriculaSerasa() throws Exception {
        try {
    		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
    		obj.getMatriculaVO().setMatriculaSerasa(Boolean.FALSE);
    		obj.getMatriculaVO().setMatriculaVerificadaSerasa(Boolean.FALSE);
    		obj.getMatriculaVO().setGuiaAba("METODO CANCELAR MATRICULA SERASA");
    		setMatriculaPeriodoVO(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void cancelarMatriculaSerasa() throws Exception {
    	try {
        	getFacadeFactory().getMatriculaFacade().alterarMatriculaSerasa(getMatriculaPeriodoVO().getMatriculaVO(), getUsuarioLogado());
            setListaConsulta(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasa(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemDetalhada("Cancelamento de matrícula do serasa realizado com sucesso!");    		
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }

    public void preparaCancelarMatriculaSerasa2() throws Exception {
        try {
            MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
            obj.getMatriculaVO().setMatriculaSerasa(Boolean.FALSE);
            obj.getMatriculaVO().setMatriculaVerificadaSerasa(Boolean.FALSE);
            obj.getMatriculaVO().setGuiaAba("METODO CANCELAR MATRICULA SERASA 2");
            setMatriculaPeriodoVO(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void cancelarMatriculaSerasa2() throws Exception {
    	try {
    		getFacadeFactory().getMatriculaFacade().alterarMatriculaSerasa(getMatriculaPeriodoVO().getMatriculaVO(), getUsuarioLogado());
    		setListaMatriculaSerasaPagaram(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasaPagaramParcela(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    		setMensagemDetalhada("Cancelamento de matrícula do serasa realizado com sucesso!");
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void marcarMatriculaVerificada() throws Exception {
    	try {
    		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
    		obj.getMatriculaVO().setMatriculaSerasa(Boolean.TRUE);
    		obj.getMatriculaVO().setMatriculaVerificadaSerasa(Boolean.TRUE);
    		obj.getMatriculaVO().setDataAlteracaoMatriculaSerasa(new Date());
    		obj.getMatriculaVO().setGuiaAba("METODO MARCAR MATRICULA VERIFICADA");
    		getFacadeFactory().getMatriculaFacade().alterarMatriculaSerasa(obj.getMatriculaVO(), getUsuarioLogado());
    		setListaMatriculaSerasaPagaram(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasaPagaramParcela(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    		setListaConsulta(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasa(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    		setMensagemDetalhada("Marcação da matricula como verificada, realizado com sucesso!");
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void desmarcarMatriculaVerificada() throws Exception {
    	try {
    		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
    		obj.getMatriculaVO().setMatriculaSerasa(Boolean.TRUE);
    		obj.getMatriculaVO().setMatriculaVerificadaSerasa(Boolean.FALSE);
    		obj.getMatriculaVO().setDataAlteracaoMatriculaSerasa(obj.getMatriculaVO().getData());
    		obj.getMatriculaVO().setGuiaAba("METODO DESMARCAR MATRICULA VERIFICADA");
    		getFacadeFactory().getMatriculaFacade().alterarMatriculaSerasa(obj.getMatriculaVO(), getUsuarioLogado());
    		setListaMatriculaSerasaPagaram(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasaPagaramParcela(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    		setListaConsulta(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSerasa(getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    		setMensagemDetalhada("Demarcação da matricula como verificada, realizado com sucesso!");
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }
    
    public void imprimirSituacaoFinanceira(){
        try {
            MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
            getFacadeFactory().getMatriculaFacade().carregarDados(obj.getMatriculaVO(), getUsuarioLogado());
            SituacaoFinanceiraAlunoRelControle sit = (SituacaoFinanceiraAlunoRelControle)getControlador("SituacaoFinanceiraAlunoRelControle");
            sit.setMatriculaVO(obj.getMatriculaVO());
            sit.imprimirPDF();
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {            
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarRegistroNaoEnviarMensagemCobranca(){
        try {
            MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItem");
            getFacadeFactory().getMatriculaFacade().alterarNaoEnviarMensagemCobranca(obj.getMatricula(), obj.getMatriculaVO().getNaoEnviarMensagemCobranca());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void alterarMatriculaSerasa() throws Exception {
        try {
        	if (getMatriculaVO().getMatricula().equals("")) {
        		throw new Exception("Selecione uma matricula para realização da operação!");
        	}
        	getMatriculaVO().setMatriculaVerificadaSerasa(Boolean.FALSE);
        	getMatriculaVO().setGuiaAba("METODO ALTERAR MATRICULA SERADA");
            getFacadeFactory().getMatriculaFacade().alterarMatriculaSerasa(getMatriculaVO(), getUsuarioLogado());
            setMensagemDetalhada("Alteração da situação da matrícula junto serasa foi realizada com sucesso!");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
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
            //System.out.println(e.getMessage());
        }
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
    
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
                objs.add(new SelectItem(0, ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
                removerObjetoMemoria(obj);
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (this.getUnidadeEnsinoVO().getCodigo() != 0) {
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

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItem");
        obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
        setMatriculaVO(obj);
        valorConsultaAluno = "";
        campoConsultaAluno = "";
        getListaConsultaAluno().clear();
    }

    public void limparDadosAluno() throws Exception {
        removerObjetoMemoria(getMatriculaVO());
        getListaConsultaAluno().clear();
        limparMensagem();
    }

    public List getTipoConsultaComboAluno() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    /**
     * @return the listaConsultaDocumentos
     */
    public List getListaConsultaContaReceber() {
        if (listaConsultaContaReceber == null) {
            listaConsultaContaReceber = new ArrayList();
        }
        return listaConsultaContaReceber;
    }

    /**
     * @param listaConsultaDocumentos the listaConsultaDocumentos to set
     */
    public void setListaConsultaContaReceber(List listaConsultaContaReceber) {
        this.listaConsultaContaReceber = listaConsultaContaReceber;
    }

    /**
     * @return the matriculaVO
     */
    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    /**
     * @param matriculaVO the matriculaVO to set
     */
    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    /**
     * @return the listaMatriculaSerasaPagaram
     */
    public List getListaMatriculaSerasaPagaram() {
        if (listaMatriculaSerasaPagaram == null) {
            listaMatriculaSerasaPagaram = new ArrayList();
        }
        return listaMatriculaSerasaPagaram;
    }

    /**
     * @param listaMatriculaSerasaPagaram the listaMatriculaSerasaPagaram to set
     */
    public void setListaMatriculaSerasaPagaram(List listaMatriculaSerasaPagaram) {
        this.listaMatriculaSerasaPagaram = listaMatriculaSerasaPagaram;
    }

	public String getAbaAtual() {
		if (abaAtual == null) {
			abaAtual = "tabInicio";
		}
		return abaAtual;
	}

	public void setAbaAtual(String abaAtual) {
		this.abaAtual = abaAtual;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public String getMatriculaNegativaSerasaMatricula() {
		if(matriculaNegativaSerasaMatricula == null){
			matriculaNegativaSerasaMatricula = "";
		}
		return matriculaNegativaSerasaMatricula;
	}

	public void setMatriculaNegativaSerasaMatricula(String matriculaNegativaSerasaMatricula) {
		this.matriculaNegativaSerasaMatricula = matriculaNegativaSerasaMatricula;
	}

	public String getMatriculaNegativaSerasaNomeAluno() {
		if(matriculaNegativaSerasaNomeAluno == null){
			matriculaNegativaSerasaNomeAluno = "";
		}
		return matriculaNegativaSerasaNomeAluno;
	}

	public void setMatriculaNegativaSerasaNomeAluno(String matriculaNegativaSerasaNomeAluno) {
		this.matriculaNegativaSerasaNomeAluno = matriculaNegativaSerasaNomeAluno;
	}

	public String getMatriculaNegativaSerasaCurso() {
		if(matriculaNegativaSerasaCurso == null){
			matriculaNegativaSerasaCurso = "";
		}
		return matriculaNegativaSerasaCurso;
	}

	public void setMatriculaNegativaSerasaCurso(String matriculaNegativaSerasaCurso) {
		this.matriculaNegativaSerasaCurso = matriculaNegativaSerasaCurso;
	}

	public String getMatriculaNegativaSerasaTurma() {
		if(matriculaNegativaSerasaTurma == null){
			matriculaNegativaSerasaTurma = "";
		}
		return matriculaNegativaSerasaTurma;
	}

	public void setMatriculaNegativaSerasaTurma(String matriculaNegativaSerasaTurma) {
		this.matriculaNegativaSerasaTurma = matriculaNegativaSerasaTurma;
	}

	public String getMatriculaNegativaParcelaMatricula() {
		if(matriculaNegativaParcelaMatricula == null){
			matriculaNegativaParcelaMatricula = "";
		}
		return matriculaNegativaParcelaMatricula;
	}

	public void setMatriculaNegativaParcelaMatricula(String matriculaNegativaParcelaMatricula) {
		this.matriculaNegativaParcelaMatricula = matriculaNegativaParcelaMatricula;
	}

	public String getMatriculaNegativaParcelaNomeAluno() {
		if(matriculaNegativaParcelaNomeAluno == null){
			matriculaNegativaParcelaNomeAluno = "";
		}
		return matriculaNegativaParcelaNomeAluno;
	}

	public void setMatriculaNegativaParcelaNomeAluno(String matriculaNegativaParcelaNomeAluno) {
		this.matriculaNegativaParcelaNomeAluno = matriculaNegativaParcelaNomeAluno;
	}

	public String getMatriculaNegativaParcelaCurso() {
		if(matriculaNegativaParcelaCurso == null){
			matriculaNegativaParcelaCurso = "";
		}
		return matriculaNegativaParcelaCurso;
	}

	public void setMatriculaNegativaParcelaCurso(String matriculaNegativaParcelaCurso) {
		this.matriculaNegativaParcelaCurso = matriculaNegativaParcelaCurso;
	}

	public String getMatriculaNegativaParcelaTurma() {
		if(matriculaNegativaParcelaTurma == null){
			matriculaNegativaParcelaTurma = "";
		}
		return matriculaNegativaParcelaTurma;
	}

	public void setMatriculaNegativaParcelaTurma(String matriculaNegativaParcelaTurma) {
		this.matriculaNegativaParcelaTurma = matriculaNegativaParcelaTurma;
	}

	public String getMatriculaNegativaSerasaDataMatricula() {
		if(matriculaNegativaSerasaDataMatricula == null){
			matriculaNegativaSerasaDataMatricula = "";
		}
		return matriculaNegativaSerasaDataMatricula;
	}

	public void setMatriculaNegativaSerasaDataMatricula(String matriculaNegativaSerasaDataMatricula) {
		this.matriculaNegativaSerasaDataMatricula = matriculaNegativaSerasaDataMatricula;
	}

	public String getMatriculaNegativaParcelaDataMatricula() {
		if(matriculaNegativaSerasaDataMatricula == null){
			matriculaNegativaSerasaDataMatricula = "";
		}
		return matriculaNegativaParcelaDataMatricula;
	}

	public void setMatriculaNegativaParcelaDataMatricula(String matriculaNegativaParcelaDataMatricula) {
		this.matriculaNegativaParcelaDataMatricula = matriculaNegativaParcelaDataMatricula;
	}
	
}