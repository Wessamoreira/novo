package controle.avaliacaoinst;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * respostaAvaliacaoInstitucionalDWForm.jsp respostaAvaliacaoInstitucionalDWCons.jsp) com as funcionalidades da classe
 * <code>RespostaAvaliacaoInstitucionalDW</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see RespostaAvaliacaoInstitucionalDW
 * @see RespostaAvaliacaoInstitucionalDWVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpSession;

import org.docx4j.model.datastorage.XPathEnhancerParser.pathExprNoRoot_return;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.avaliacaoinst.RespostaAvaliacaoInstitucionalDWVO;
import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("RespostaAvaliacaoInstitucionalDWControle")
@Scope("viewScope")
@Lazy
public class RespostaAvaliacaoInstitucionalDWControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8046822382305640216L;
	private RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO;        
    private AvaliacaoInstitucionalVO avaliacaoInstitucionalVO;
    private List<AvaliacaoInstitucionalVO> listaAvaliacaoInstitucional;
    private Boolean obrigatorio;
    private MatriculaVO matriculaVO;
    
    private List<SelectItem> listaSelectItemAvaliacaoInstitucional;

    public RespostaAvaliacaoInstitucionalDWControle() throws Exception {
    	getUsuarioLogado().setPermiteVisualizarRelogioAssincrono(false);
        if(context() != null) {
        	HttpSession session = (HttpSession) context().getExternalContext().getSession(false);        	
      		session.setMaxInactiveInterval(30*60000);
        }
    }

    @PostConstruct
    public void incializarListaAvaliacao() {
        try {
        	novo();
        	setListaAvaliacaoInstitucional(getUsuarioLogado().getAvaliacaoInstitucionalVOs());                       
            if (!getListaAvaliacaoInstitucional().isEmpty()) {
                AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) getListaAvaliacaoInstitucional().get(0);                
                apresentarPrimeiraAvaliacao(obj);
                validaObrigatoriedade();
                montarListaAvaliacaoInstitucional();
            }
            setMensagemDetalhada("", "");
        } catch (Exception e) {
        	e.getMessage();
        }
    }

    public void validaObrigatoriedade() {
    	if(getUsuarioLogado().getPermiteSimularNavegacaoAluno()){
    		setObrigatorio(Boolean.FALSE);
    		return;
    	}
        Iterator<AvaliacaoInstitucionalVO> i = getListaAvaliacaoInstitucional().iterator();
        while (i.hasNext()) {
            AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) i.next();
            if (obj.getAvaliacaoObrigatoria()) {
                setObrigatorio(Boolean.TRUE);
                return;
            }
        }
        setObrigatorio(Boolean.FALSE);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>RespostaAvaliacaoInstitucionalDW</code> para
     * edição pelo usuário da aplicação.
     */
    public void novo() {
        setRespostaAvaliacaoInstitucionalDWVO(new RespostaAvaliacaoInstitucionalDWVO());
        setListaAvaliacaoInstitucional(new ArrayList<AvaliacaoInstitucionalVO>(0));        
        setAvaliacaoInstitucionalVO(new AvaliacaoInstitucionalVO());
        setMatriculaVO(new MatriculaVO());        
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>RespostaAvaliacaoInstitucionalDW</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a
     * operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
     * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
        	executarValidacaoSimulacaoVisaoAluno();
        	setListaAvaliacaoInstitucional(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().persistir(getListaAvaliacaoInstitucional(), getAvaliacaoInstitucionalVO(), getUsuarioLogado()));
        	montarListaAvaliacaoInstitucional();
            setMensagemID("msg_dados_gravados");
            String retorno = getLoginControle().verificaAtualizacaoCadastral(getUsuarioLogadoClone(), Boolean.FALSE);
			if (!retorno.equals("")) {
				return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAluno/dadosPessoaisAluno");
			}
            return finalizarAvaliacao();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String getValidarOnComplete() {
    	if (Uteis.isAtributoPreenchido(getAvaliacaoInstitucionalVO().getKeyPerguntaNaoRespondida())) {
    		return "scrollTO('div." + getAvaliacaoInstitucionalVO().getKeyPerguntaNaoRespondida() + "')";
    	}
    	return "";
    }

    public String finalizarAvaliacao() throws Exception {
        if (getListaSelectItemAvaliacaoInstitucional().isEmpty()) {  
        	return responderDepois();
        }
        if(!getListaSelectItemAvaliacaoInstitucional().isEmpty()){
        	setAvaliacaoInstitucionalVO(new AvaliacaoInstitucionalVO());        	
        	validaObrigatoriedade();
        	apresentarPrimeiraAvaliacao(getListaAvaliacaoInstitucional().get(0));        	
        	return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalQuestionario.xhtml");
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("/index.xhtml");
    }

  
    public void validarDadosListaRespostaEscopoDisciplina()  {
    	try {
    		RespostaPerguntaVO respostaPerguntaVO = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaItens");
    		PerguntaQuestionarioVO perguntaQuestionarioVO = (PerguntaQuestionarioVO) getRequestMap().get("perguntaItens");
            QuestionarioVO questionarioVO = (QuestionarioVO) getRequestMap().get("questionarioItens");
            getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoRespostaQuestionario(getAvaliacaoInstitucionalVO(), questionarioVO, perguntaQuestionarioVO, respostaPerguntaVO, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public int getQtdeAvaliacao() {
        return getListaSelectItemAvaliacaoInstitucional().size();
    }

    public Boolean getExisteQuestionario() {
        if (getAvaliacaoInstitucionalVO().getCodigo().intValue() != 0) {
            return true;
        }
        return false;
    }

    public String responderDepois() {
        try {        
			HttpSession session = (HttpSession) context().getExternalContext().getSession(false);        	
    		session.setMaxInactiveInterval(60*45);
    		if (getUsuarioLogado().getVisaoLogar().equals("aluno")) {
    			String retorno = getLoginControle().verificaAtualizacaoCadastral(getUsuarioLogadoClone(), Boolean.FALSE);
				if (!retorno.equals("")) {
					return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAluno/dadosPessoaisAluno");
				}
    			return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAluno/telaInicialVisaoAluno");
    		}
    		if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
    			return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoProfessor/telaInicialVisaoProfessor");
    		}
    		if (getUsuarioLogado().getVisaoLogar().equals("administrador") || getUsuarioLogado().getVisaoLogar().equals("funcionario")) {
    			return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoAdministrativo/administrativo/homeAdministrador");
    		}
    		if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
    			return Uteis.getCaminhoRedirecionamentoNavegacao("/visaoCoordenador/telaInicialVisaoCoordenador");
    		}
    		return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
        } catch (Exception e) {
            return Uteis.getCaminhoRedirecionamentoNavegacao("index.xhtml");
        }
    }

    public void apresentarPrimeiraAvaliacao(AvaliacaoInstitucionalVO obj) {
        try {
        	
        	 if(getUsuarioLogado().getIsApresentarVisaoAluno() && getVisaoAlunoControle() !=  null && getVisaoAlunoControle().getMatricula() != null && Uteis.isAtributoPreenchido(getVisaoAlunoControle().getMatricula().getMatricula())) {
             	setMatriculaVO(getVisaoAlunoControle().getMatricula());
             }else {
             	setMatriculaVO(null);
             }
        	setAvaliacaoInstitucionalVO(getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarCarregamentoQuestionariosPorUsuarioAvaliacaoInstitucional(obj, getMatriculaVO(), getUsuarioLogado()));
        	if(getUsuarioLogado().getIsApresentarVisaoAluno()) {        	
        		if(getMatriculaVO() == null || !Uteis.isAtributoPreenchido(getMatriculaVO().getMatricula())) {
        			setMatriculaVO(getAvaliacaoInstitucionalVO().getMatriculaVO());
        		}
        	}
            setMensagemDetalhada("", "");
            setMensagemID("");
            setMensagem("");
        } catch (Exception e) {
            setAvaliacaoInstitucionalVO(new AvaliacaoInstitucionalVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void responderAvaliacao() {
        try {
        	if (Uteis.isAtributoPreenchido(getListaAvaliacaoInstitucional())) {
        		for(AvaliacaoInstitucionalVO obj: getListaAvaliacaoInstitucional()) {
        			if (obj.getCodigo().equals(getAvaliacaoInstitucionalVO().getCodigo())) {
	        			limparMensagem();
	        			apresentarPrimeiraAvaliacao(obj);
	        			return;
	        		}
	        	}
        	}
        } catch (Exception e) {
            setAvaliacaoInstitucionalVO(new AvaliacaoInstitucionalVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void escolherPeso1() {
    	PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaItens");
    	QuestionarioVO questionarioVO = (QuestionarioVO) getRequestMap().get("questionarioItens");
    	try {
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoImportanciaPerguntaSelecionado(getAvaliacaoInstitucionalVO(), questionarioVO, obj, 1, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}   
    }

    public void escolherPeso2() {
    	PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaItens");
    	QuestionarioVO questionarioVO = (QuestionarioVO) getRequestMap().get("questionarioItens");
    	try {
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoImportanciaPerguntaSelecionado(getAvaliacaoInstitucionalVO(), questionarioVO, obj, 2, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}   
    }

    public void escolherPeso3() {
    	PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaItens");
    	QuestionarioVO questionarioVO = (QuestionarioVO) getRequestMap().get("questionarioItens");
    	try {
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoImportanciaPerguntaSelecionado(getAvaliacaoInstitucionalVO(), questionarioVO, obj, 3, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}   
    }

    public void escolherPeso4() {
    	PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaItens");
    	QuestionarioVO questionarioVO = (QuestionarioVO) getRequestMap().get("questionarioItens");
    	try {
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoImportanciaPerguntaSelecionado(getAvaliacaoInstitucionalVO(), questionarioVO, obj, 4, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}   
    }

    public void escolherPeso5() {
    	PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaItens");
    	QuestionarioVO questionarioVO = (QuestionarioVO) getRequestMap().get("questionarioItens");
    	try {
			getFacadeFactory().getRespostaAvaliacaoInstitucionalDWFacade().realizarValidacaoImportanciaPerguntaSelecionado(getAvaliacaoInstitucionalVO(), questionarioVO, obj, 5, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}        
    }
    
    public void montarListaAvaliacaoInstitucional() {
    	try {
    		getListaSelectItemAvaliacaoInstitucional().clear();
    		int  contador = 1;
    		for (AvaliacaoInstitucionalVO obj : getListaAvaliacaoInstitucional()) {
    			getListaSelectItemAvaliacaoInstitucional().add(new SelectItem(obj.getCodigo(), contador + "- " + obj.getNome()));
    			contador++;
    		}
    	} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
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
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        Uteis.liberarListaMemoria(listaSelectItemAvaliacaoInstitucional);
        respostaAvaliacaoInstitucionalDWVO = null;
    }

    public Boolean getObrigatorio() {
        return obrigatorio;
    }

    public AvaliacaoInstitucionalVO getAvaliacaoInstitucionalVO() {
		if (avaliacaoInstitucionalVO == null) {
    		avaliacaoInstitucionalVO = new AvaliacaoInstitucionalVO();
    	}
        return avaliacaoInstitucionalVO;
    }

    public void setAvaliacaoInstitucionalVO(AvaliacaoInstitucionalVO avaliacaoInstitucionalVO) {
        this.avaliacaoInstitucionalVO = avaliacaoInstitucionalVO;
    }

    public void setObrigatorio(Boolean obrigatorio) {
        this.obrigatorio = obrigatorio;
    }

    public List<AvaliacaoInstitucionalVO> getListaAvaliacaoInstitucional() {
		if (listaAvaliacaoInstitucional == null) {
			listaAvaliacaoInstitucional = new ArrayList<AvaliacaoInstitucionalVO>(0);
    	}
        return listaAvaliacaoInstitucional;
    }

    public void setListaAvaliacaoInstitucional(List<AvaliacaoInstitucionalVO> listaAvaliacaoInstitucional) {
        this.listaAvaliacaoInstitucional = listaAvaliacaoInstitucional;
    }

    public RespostaAvaliacaoInstitucionalDWVO getRespostaAvaliacaoInstitucionalDWVO() {
		if (respostaAvaliacaoInstitucionalDWVO == null) {
    		respostaAvaliacaoInstitucionalDWVO = new RespostaAvaliacaoInstitucionalDWVO();
    	}
        return respostaAvaliacaoInstitucionalDWVO;
    }

    public void setRespostaAvaliacaoInstitucionalDWVO(RespostaAvaliacaoInstitucionalDWVO respostaAvaliacaoInstitucionalDWVO) {
        this.respostaAvaliacaoInstitucionalDWVO = respostaAvaliacaoInstitucionalDWVO;
    }

    public boolean getApresentarBotaoConcluir() {
        return getAvaliacaoInstitucionalVO() != null && getAvaliacaoInstitucionalVO().getCodigo() != 0  && !getUsuarioLogado().getPermiteSimularNavegacaoAluno();
    }

	public List<SelectItem> getListaSelectItemAvaliacaoInstitucional() {
		if (listaSelectItemAvaliacaoInstitucional == null) {
			listaSelectItemAvaliacaoInstitucional = new ArrayList<>();
		}
		return listaSelectItemAvaliacaoInstitucional;
	}

	public void setListaSelectItemAvaliacaoInstitucional(List<SelectItem> listaSelectItemAvaliacaoInstitucional) {
		this.listaSelectItemAvaliacaoInstitucional = listaSelectItemAvaliacaoInstitucional;
	} 
}
