package controle.processosel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * perguntaForm.jsp perguntaCons.jsp) com as funcionalidades da classe <code>Pergunta</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Pergunta
 * @see PerguntaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.processosel.PerguntaChecklistVO;
import negocio.comuns.processosel.PerguntaItemVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.processosel.RespostaPergunta;

@Controller("PerguntaControle")
@Scope("viewScope")
@Lazy
@SuppressWarnings("rawtypes")
public class PerguntaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -3385755695957915209L;

	private PerguntaVO perguntaVO;
    private List<SelectItem> listaSelectItemQuestionario;
    private RespostaPerguntaVO respostaPerguntaVO;
    private EscopoPerguntaEnum escopoPerguntaBase;
	private String valorConsultaPergunta;
	private String campoConsultaPergunta;
	private List listaConsultaPergunta;
	private PerguntaItemVO perguntaItemVO;
	private PerguntaChecklistVO perguntaChecklistVO;
	

    public PerguntaControle() throws Exception {
        //obterUsuarioLogado();
    	setControleConsulta(new ControleConsulta());
    	setMensagemID("msg_entre_prmconsulta");
		
    	
		String tipoEscopoBase = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("escopo");
		
		if (tipoEscopoBase != null && !tipoEscopoBase.trim().isEmpty()) {
			setEscopoPerguntaBase(EscopoPerguntaEnum.getEnumCorrespondenteEscopoBase(tipoEscopoBase));
		}
		
		String idControlador = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("idControlador");
		if (idControlador != null && !idControlador.trim().isEmpty()) {
			setIdControlador(idControlador);
		}
	}

	

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Pergunta</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
    	
        setPerguntaVO(new PerguntaVO());
        setRespostaPerguntaVO(new RespostaPerguntaVO());
        getPerguntaVO().setEscopoPergunta(getEscopoPerguntaBase());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perguntaForm", getIdControlador()+"&escopo="+getEscopoPerguntaBase().getEscopoBase());
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Pergunta</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
    	try{
    		PerguntaVO obj = (PerguntaVO) context().getExternalContext().getRequestMap().get("perguntaItens");
    		obj.setNovoObj(Boolean.FALSE);
    		setPerguntaVO(getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(obj.getCodigo(),Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    		setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perguntaForm", getIdControlador()+"&escopo="+getEscopoPerguntaBase().getEscopoBase());
    	 } catch (Exception e) {
             setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
             return Uteis.getCaminhoRedirecionamentoNavegacao("perguntaCons?escopo=" + getEscopoPerguntaBase().getValor());
         }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Pergunta</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (perguntaVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getPerguntaFacade().incluir(perguntaVO, getEscopoPerguntaBase(), getUsuarioLogado());
            } else {
                getFacadeFactory().getPerguntaFacade().alterar(perguntaVO, getEscopoPerguntaBase(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("perguntaForm"+ "?escopo=" + getEscopoPerguntaBase().getValor(), getIdControlador());
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP PerguntaCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List<PerguntaVO> objs = new ArrayList<>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getPerguntaFacade().consultarPorCodigo(new Integer(valorInt), getEscopoPerguntaBase(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
            	objs = getFacadeFactory().getPerguntaFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), getEscopoPerguntaBase(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoResposta")) {
                objs = getFacadeFactory().getPerguntaFacade().consultarPorTipoResposta(getControleConsulta().getValorConsulta(), getEscopoPerguntaBase(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
            return "";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>PerguntaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPerguntaFacade().excluir(perguntaVO, getEscopoPerguntaBase(), getUsuarioLogado());
            novo();
            getPerguntaVO().setEscopoPergunta(getEscopoPerguntaBase());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("perguntaForm", getIdControlador()+"&escopo="+getEscopoPerguntaBase().getEscopoBase());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("perguntaForm", getIdControlador()+"&escopo="+getEscopoPerguntaBase().getEscopoBase());
        }
        
    }

    /* Método responsável por adicionar um novo objeto da classe <code>RespostaPergunta</code>
     * para o objeto <code>perguntaVO</code> da classe <code>Pergunta</code>
     */
    public void adicionarRespostaPergunta() throws Exception {
        try {
            if (!getPerguntaVO().getCodigo().equals(0)) {
                respostaPerguntaVO.setPergunta(getPerguntaVO().getCodigo());
            }
            getPerguntaVO().adicionarObjRespostaPerguntaVOs(getRespostaPerguntaVO());
            this.setRespostaPerguntaVO(new RespostaPerguntaVO());
            setMensagemID(MSG_TELA.msg_dados_adicionados.name());

        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

        }
    }

    public void scrollerListener(DataScrollEvent dataScrollEvent) {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
        consultar();
    }


    /* Método responsável por disponibilizar dados de um objeto da classe <code>RespostaPergunta</code>
     * para edição pelo usuário.
     */
    public void editarRespostaPergunta() throws Exception {
        RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaPerguntaItem");
        setRespostaPerguntaVO(obj);

    }

    /* Método responsável por remover um novo objeto da classe <code>RespostaPergunta</code>
     * do objeto <code>perguntaVO</code> da classe <code>Pergunta</code>
     */
    public void removerRespostaPergunta() throws Exception {
        RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaPerguntaItem");
        getPerguntaVO().excluirObjRespostaPerguntaVOs(obj.getDescricao());
        setMensagemID(MSG_TELA.msg_dados_excluidos.name());

    }
    
    public void subirOpcaoRespostaQuestao() {
        try {
        	RespostaPerguntaVO opc1 = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaPerguntaItem");
            if (opc1.getOrdem() > 1) {
            	RespostaPerguntaVO opc2 = getPerguntaVO().getRespostaPerguntaVOs().get(opc1.getOrdem() - 2);
                getFacadeFactory().getPerguntaFacade().alterarOrdemOpcaoRespostaQuestao(getPerguntaVO(), opc1, opc2);
            }
            limparMensagem();

        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
        }
    }

    public void descerOpcaoRespostaQuestao() {
        try {
        	RespostaPerguntaVO opc1 = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaPerguntaItem");
            if (getPerguntaVO().getRespostaPerguntaVOs().size() >= opc1.getOrdem()) {
            	RespostaPerguntaVO opc2 = getPerguntaVO().getRespostaPerguntaVOs().get(opc1.getOrdem());
            	getFacadeFactory().getPerguntaFacade().alterarOrdemOpcaoRespostaQuestao(getPerguntaVO(), opc1, opc2);
            }
            limparMensagem();

        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void adicionarPerguntaChecklistVO()  {
        try {
            getFacadeFactory().getPerguntaFacade().adicionarPerguntaChecklistVO(getPerguntaVO(), getPerguntaChecklistVO(), getUsuarioLogadoClone());
            setPerguntaChecklistVO(new PerguntaChecklistVO());
            setMensagemID(MSG_TELA.msg_dados_adicionados.name());
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

        }
    }
    
    public void editarPerguntaChecklistVO()  {
    	try {
    		PerguntaChecklistVO pc = (PerguntaChecklistVO) context().getExternalContext().getRequestMap().get("perguntaChecklistItens");
    		setPerguntaChecklistVO(pc);
    		setMensagemID(MSG_TELA.msg_dados_editar.name());
    	} catch (Exception e) {
    		setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
    		
    	}
    }
    
    public void removerPerguntaChecklistVO()  {
    	try {
    		PerguntaChecklistVO pc = (PerguntaChecklistVO) context().getExternalContext().getRequestMap().get("perguntaChecklistItens");
    		getFacadeFactory().getPerguntaFacade().removerPerguntaChecklistVO(getPerguntaVO(), pc, getUsuarioLogadoClone());
    		setMensagemID(MSG_TELA.msg_dados_excluidos.name());
    	} catch (Exception e) {
    		setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
    		
    	}
    }

    public void irPaginaInicial() throws Exception {
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        this.consultar();
    }

    /* Método responsável por inicializar List<SelectItem> de valores do 
     * ComboBox correspondente ao atributo <code>tipoResposta</code>
     */
    @SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemTipoRespostaPergunta() {
        List<SelectItem> objs = new ArrayList<>(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoRespostaQuestionarios = (Hashtable) Dominios.getTipoRespostaQuestionario();
        Enumeration keys = tipoRespostaQuestionarios.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoRespostaQuestionarios.get(value);
            objs.add(new SelectItem(value, label));
        }
        if(getEscopoPerguntaBase().equals(EscopoPerguntaEnum.PLANO_ENSINO)||
        		getEscopoPerguntaBase().equals(EscopoPerguntaEnum.ESTAGIO) || getEscopoPerguntaBase().equals(EscopoPerguntaEnum.RELATORIO_FACILITADOR)) {
        	objs.add(new SelectItem("LS", "Lista Suspensa"));
        	objs.add(new SelectItem("NU", "Numérico"));
        	objs.add(new SelectItem("DT", "Data"));
        	objs.add(new SelectItem("HR", "Hora"));
        	objs.add(new SelectItem("VF", "Verdadeiro ou Falso"));
        }
        if(getEscopoPerguntaBase().equals(EscopoPerguntaEnum.PLANO_ENSINO)) {        	
        	objs.add(new SelectItem("LC", "Lista Campos"));
        }
        if(getEscopoPerguntaBase().equals(EscopoPerguntaEnum.ESTAGIO) || getEscopoPerguntaBase().equals(EscopoPerguntaEnum.RELATORIO_FACILITADOR)) {
        	objs.add(new SelectItem("UP", "Upload"));
        }
        
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List<SelectItem> getListaSelectItemLayoutPergunta() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("HO", "Horizontal"));
        objs.add(new SelectItem("VE", "Vertical"));
        return objs;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Questionario</code>.
     */
   public void montarListaSelectItemQuestionario(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarQuestionarioPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                QuestionarioVO obj = (QuestionarioVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
            }
            setListaSelectItemQuestionario(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Questionario</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Questionario</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemQuestionario() {
        try {
            montarListaSelectItemQuestionario("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List<QuestionarioVO> consultarQuestionarioPorDescricao(String descricaoPrm) throws Exception {
        return getFacadeFactory().getQuestionarioFacade().consultarPorDescricao(descricaoPrm, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemQuestionario();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("tipoResposta", "Tipo de Resposta"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        setListaConsulta(new ArrayList<>(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("perguntaCons"+ "?escopo=" + getEscopoPerguntaBase().getValor());
    }

    public RespostaPerguntaVO getRespostaPerguntaVO() {
    	if (respostaPerguntaVO == null) {
    		respostaPerguntaVO = new RespostaPerguntaVO();
    	}
        return respostaPerguntaVO;
    }

    public void setRespostaPerguntaVO(RespostaPerguntaVO respostaPerguntaVO) {
        this.respostaPerguntaVO = respostaPerguntaVO;
    }

    public List<SelectItem> getListaSelectItemQuestionario() {
        return (listaSelectItemQuestionario);
    }

    public void setListaSelectItemQuestionario(List<SelectItem> listaSelectItemQuestionario) {
        this.listaSelectItemQuestionario = listaSelectItemQuestionario;
    }

    public PerguntaVO getPerguntaVO() {
    	if(perguntaVO == null) {
    		perguntaVO = new PerguntaVO();
    	}
        return perguntaVO;
    }

    public void setPerguntaVO(PerguntaVO perguntaVO) {
        this.perguntaVO = perguntaVO;
    }

    public boolean getIsConsultarPorTipoResposta() {
        if (getControleConsulta().getCampoConsulta().equals("tipoResposta")) {
            return true;
        }
        return false;
    }

    public boolean getIsConsultarDiferenteTipoResposta() {
    	return !getControleConsulta().getCampoConsulta().equals("tipoResposta");
    }
    
    public String getIsMascaraCodigo() {
    	if(getControleConsulta().getCampoConsulta().equals("codigo")) {
    		return "aceitarSomenteValorNumerico(this)";
    	}
    	return "";
    }

	public EscopoPerguntaEnum getEscopoPerguntaBase() {
		if(escopoPerguntaBase == null){
			escopoPerguntaBase = EscopoPerguntaEnum.AVALIACAO_INSTITUCIONAL;
		}
		return escopoPerguntaBase;
	}

	public void setEscopoPerguntaBase(EscopoPerguntaEnum escopoPerguntaBase) {
		this.escopoPerguntaBase = escopoPerguntaBase;
	}

    public String getTituloPagina(){
    	
		switch (getEscopoPerguntaBase()) {
		case AVALIACAO_INSTITUCIONAL:
			return "Pergunta - Avaliação Institucional";
		case BANCO_CURRICULUM:
			return "Pergunta - Banco Curriculum";
		case REQUERIMENTO:
			return "Pergunta - Requerimento";
		case PROCESSO_SELETIVO:
			return "Pergunta - Processo Seletivo";
		case REQUISICAO:
			return "Pergunta - Requisição";
		case ESTAGIO:
			return "Campos Estágio";
		case PLANO_ENSINO:
			return "Campos Plano de Ensino";
		case RELATORIO_FACILITADOR:
			return "Campos Relatório Facilitador";
		default:
			return "Pergunta";
		}    	
    }

    public List<SelectItem> getQuantidadeCasasDecimaisCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem(0, "0"));
        itens.add(new SelectItem(1, "1"));
        itens.add(new SelectItem(2, "2"));
        itens.add(new SelectItem(3, "3"));
        itens.add(new SelectItem(4, "4"));
        itens.add(new SelectItem(5, "5"));
        itens.add(new SelectItem(6, "6"));
        itens.add(new SelectItem(7, "7"));
        itens.add(new SelectItem(8, "8"));
        itens.add(new SelectItem(9, "9"));
        itens.add(new SelectItem(10, "10"));
        return itens;
}
    
    public List<SelectItem> getMascaraDataCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("dd/MM/yyyy", "DD/MM/YYYY"));
        itens.add(new SelectItem("MM/yyyy", "MM/YYYY"));
        itens.add(new SelectItem("dd/MM/yyyy HH:mm:ss", "DD/MM/YYYY HH:MM:SS"));
        itens.add(new SelectItem("dd/MM/yyyy HH:mm", "DD/MM/YYYY HH:MM"));
        return itens;
    }
    
    public List<SelectItem> getMascaraHoraCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("HH:mm:ss", "HH:MM:SS"));
        itens.add(new SelectItem("HH:mm", "HH:MM"));
        itens.add(new SelectItem("HH", "HH"));
        return itens;
    }
    
	/**
	 * @return the valorConsultaPergunta
	 */
	public String getValorConsultaPergunta() {
		if(valorConsultaPergunta == null){
			valorConsultaPergunta = "";
		}
		return valorConsultaPergunta;
	}

	/**
	 * @param valorConsultaPergunta
	 *            the valorConsultaPergunta to set
	 */
	public void setValorConsultaPergunta(String valorConsultaPergunta) {
		this.valorConsultaPergunta = valorConsultaPergunta;
	}

	/**
	 * @return the campoConsultaPergunta
	 */
	public String getCampoConsultaPergunta() {
		if(campoConsultaPergunta == null){
			campoConsultaPergunta = "";
		}
		return campoConsultaPergunta;
	}

	/**
	 * @param campoConsultaPergunta
	 *            the campoConsultaPergunta to set
	 */
	public void setCampoConsultaPergunta(String campoConsultaPergunta) {
		this.campoConsultaPergunta = campoConsultaPergunta;
	}

	/**
	 * @return the listaConsultaPergunta
	 */
	public List getListaConsultaPergunta() {
		if(listaConsultaPergunta == null){
			listaConsultaPergunta = new ArrayList(0);
		}
		return listaConsultaPergunta;
	}

	/**
	 * @param listaConsultaPergunta
	 *            the listaConsultaPergunta to set
	 */
	public void setListaConsultaPergunta(List listaConsultaPergunta) {
		this.listaConsultaPergunta = listaConsultaPergunta;
	}
	
	public void consultarPergunta() {
		try {
			EscopoPerguntaEnum escopoBase = EscopoPerguntaEnum.PLANO_ENSINO;

			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaPergunta().equals("descricao")) {
				objs = getFacadeFactory().getPerguntaFacade().consultarPorDescricaoTipoResposta(getValorConsultaPergunta(), escopoBase, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaPergunta().equals("codigo")) {
				objs = getFacadeFactory().getPerguntaFacade().consultarPorCodigoTipoResposta(Integer.parseInt(getValorConsultaPergunta()), escopoBase, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaPergunta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPergunta() {
		try {
			PerguntaVO obj = (PerguntaVO) context().getExternalContext().getRequestMap().get("perguntaItens");
			obj.setRespostaPerguntaVOs(RespostaPergunta.consultarRespostaPerguntas(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getPerguntaItemVO().setPerguntaVO(obj);
			getFacadeFactory().getPerguntaFacade().adicionarObjPerguntaItemVOs(getPerguntaItemVO(), getPerguntaVO().getPerguntaItemVOs());
			getFacadeFactory().getPerguntaFacade().removerPerguntaListaPergunta(obj.getCodigo(), getListaConsultaPergunta());
			setPerguntaItemVO(new PerguntaItemVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public List<SelectItem> getTipoConsultaComboPergunta() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public PerguntaItemVO getPerguntaItemVO() {
		if(perguntaItemVO == null) {
			perguntaItemVO = new PerguntaItemVO();
		}
		return perguntaItemVO;
	}

	public void setPerguntaItemVO(PerguntaItemVO perguntaItemVO) {
		this.perguntaItemVO = perguntaItemVO;
	}	
	
	public PerguntaChecklistVO getPerguntaChecklistVO() {
		if (perguntaChecklistVO == null) {
			perguntaChecklistVO = new PerguntaChecklistVO();
		}
		return perguntaChecklistVO;
	}



	public void setPerguntaChecklistVO(PerguntaChecklistVO perguntaChecklistVO) {
		this.perguntaChecklistVO = perguntaChecklistVO;
	}



	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>PerguntaItem</code> para o objeto
	 * <code>PerguntaVO</code> da classe <code>Pergunta</code>
	 */
	public void adicionarPerguntaItem() throws Exception {
		try {
			if (getPerguntaItemVO().getPerguntaVO().getCodigo().intValue() != 0) {
				Integer campoConsulta = getPerguntaItemVO().getPerguntaVO().getCodigo();
				PerguntaVO pergunta = getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getPerguntaItemVO().setPerguntaVO(pergunta);
			}
			getFacadeFactory().getPerguntaFacade().adicionarObjPerguntaItemVOs(getPerguntaItemVO(), getPerguntaVO().getPerguntaItemVOs());
			this.setPerguntaItemVO(new PerguntaItemVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>PerguntaItem</code> do objeto <code>perguntaVO</code>
	 * da classe <code>Pergunta</code>
	 */
	public void removerPerguntaItem() throws Exception {
		PerguntaItemVO obj = (PerguntaItemVO) context().getExternalContext().getRequestMap().get("perguntaItemItens");
		getFacadeFactory().getPerguntaFacade().excluirObjPerguntaItemVOs(obj.getPerguntaVO().getCodigo(), getPerguntaVO().getPerguntaItemVOs());
		setMensagemID("msg_dados_excluidos");
	}
	
	public void subirOpcaoPergunta() {
		try {
			PerguntaItemVO opc1 = (PerguntaItemVO) context().getExternalContext().getRequestMap().get("perguntaItemItens");
			if (opc1.getOrdem() > 1) {
				PerguntaItemVO opc2 = getPerguntaVO().getPerguntaItemVOs().get(opc1.getOrdem() - 2);
				getFacadeFactory().getPerguntaFacade().alterarOrdemPergunta(getPerguntaVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerOpcaoPergunta() {
		try {
			PerguntaItemVO opc1 = (PerguntaItemVO) context().getExternalContext().getRequestMap().get("perguntaItemItens");
			if (getPerguntaVO().getPerguntaItemVOs().size() >= opc1.getOrdem()) {
				PerguntaItemVO opc2 = getPerguntaVO().getPerguntaItemVOs().get(opc1.getOrdem());
				getFacadeFactory().getPerguntaFacade().alterarOrdemPergunta(getPerguntaVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void limparFiltroConsulta() {
		getControleConsulta().setValorConsulta("");
	}
    
}
