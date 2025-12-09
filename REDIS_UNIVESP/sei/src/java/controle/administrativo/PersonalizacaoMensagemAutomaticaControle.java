/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package controle.administrativo;

/**
 * 
 * @author Mauro
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.ModuloTemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("PersonalizacaoMensagemAutomaticaControle")
@Scope("viewScope")
@Lazy
public class PersonalizacaoMensagemAutomaticaControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7342324749879231488L;
	private PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO;
    private List<PersonalizacaoMensagemAutomaticaVO> listaPersonalizacao;
    private TemplateMensagemAutomaticaEnum tipoTamplateSelecionado;
    private ModuloTemplateMensagemAutomaticaEnum moduloTemplateMensagemAutomaticaEnum;
    private Boolean marcarTodosNiveisEducacionais;
    private String nomeCurso;
    private String assunto;
	

    public PersonalizacaoMensagemAutomaticaControle() {
        setMensagemID("msg_entre_prmconsulta");
    }

    @PostConstruct
    public void init() throws Exception {
    	try{
        setListaPersonalizacao(new ArrayList<PersonalizacaoMensagemAutomaticaVO>());
        if(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("mensagemAutomaticaLigacaoReceptivaCampanhaCRM") != null){
        	String mensagem = ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("mensagemAutomaticaLigacaoReceptivaCampanhaCRM");
        	if (mensagem.equals("TIRE_SUAS_DUVIDAS")) {
        		PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO = (getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RECEPTIVA_TIRE_SUA_DUVIDA, false, getUsuarioLogado()));
        		if (personalizacaoMensagemAutomaticaVO != null) {
        			setPersonalizacaoMensagemAutomaticaVO(personalizacaoMensagemAutomaticaVO);
        		}
        	} else {
        		PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO = (getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_RECEPTIVA_QUERO_SER_ALUNO, false, getUsuarioLogado()));
        		if (personalizacaoMensagemAutomaticaVO != null) {
        			setPersonalizacaoMensagemAutomaticaVO(personalizacaoMensagemAutomaticaVO);
        		}
        	}
        	setMensagemID("msg_entre_prmconsulta");
        }else if(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("TEMPLATE") != null){
        	TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum = TemplateMensagemAutomaticaEnum.valueOf(((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("TEMPLATE"));
        	if(templateMensagemAutomaticaEnum != null){
        		PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO = (getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().executarGeracaoMensagemPadraoTemplateEspecifico(templateMensagemAutomaticaEnum, false, getUsuarioLogado()));
        		if (personalizacaoMensagemAutomaticaVO != null) {
        			setPersonalizacaoMensagemAutomaticaVO(personalizacaoMensagemAutomaticaVO);
        		}
        	}
        	setMensagemID("msg_dados_editar");
        }else if((context().getExternalContext().getSessionMap().get("TEMPLATE") != null)){
        	TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum = (TemplateMensagemAutomaticaEnum) context().getExternalContext().getSessionMap().get("TEMPLATE");
        	if(templateMensagemAutomaticaEnum != null){
        		PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO = (getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().executarGeracaoMensagemPadraoTemplateEspecifico(templateMensagemAutomaticaEnum, false, getUsuarioLogado()));
        		if (personalizacaoMensagemAutomaticaVO != null) {
        			setPersonalizacaoMensagemAutomaticaVO(personalizacaoMensagemAutomaticaVO);
        		}
        	}
        	setMensagemID("msg_dados_editar");
        } else {
        	novo();
        }
        
    	}catch(Exception e){
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
        
    }

    public String novo() throws Exception {
        removerObjetoMemoria(this);
        removerObjetoMemoria(getPersonalizacaoMensagemAutomaticaVO());
        setPersonalizacaoMensagemAutomaticaVO(new PersonalizacaoMensagemAutomaticaVO());
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    public String inicializarConsultar() {
//        removerObjetoMemoria(this);
        setPersonalizacaoMensagemAutomaticaVO(new PersonalizacaoMensagemAutomaticaVO());
//        setModuloTemplateMensagemAutomaticaEnum(ModuloTemplateMensagemAutomaticaEnum.TODOS);
        montarListaSelectItemTemplateMensagemAutomaticaEnum();
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("personalizacaoMensagemAutomaticaCons");
    }

    public String editar() throws Exception {
        PersonalizacaoMensagemAutomaticaVO obj = (PersonalizacaoMensagemAutomaticaVO) context().getExternalContext().getRequestMap().get("mensagemAutomatica");
        context().getExternalContext().getSessionMap();
        context().getExternalContext().getRequestMap();
        obj.setNovoObj(Boolean.FALSE);
        setPersonalizacaoMensagemAutomaticaVO(getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        getPersonalizacaoMensagemAutomaticaVO().getUsuarioUltimaAlteracao().setNome(obj.getUsuarioUltimaAlteracao().getNome());
        getPersonalizacaoMensagemAutomaticaVO().setDataUltimaAlteracao(obj.getDataUltimaAlteracao());
        if(getApresentarFiltrosUnidadeEnsinoNivelEducacional()) {
        	consultarUnidadeEnsinoFiltroRelatorio("PersonalizacaoMensagemAutomaticaControle");
        	getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().inicializarDadosUnidadeEnsinoSelecionadaEdicao(getPersonalizacaoMensagemAutomaticaVO(), getUnidadeEnsinoVOs(), getUsuarioLogado());
        
        }setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("personalizacaoMensagemAutomaticaForm");
    }

    public void consultarMensagemAutomatica() {
        try {
            getListaPersonalizacao().clear();
            setListaPersonalizacao((getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorParamentrosTemplate(getAssunto(),
                getTipoTamplateSelecionado(), getModuloTemplateMensagemAutomaticaEnum(), getNomeCurso(), false, getUsuarioLogado())));
            // if (getPersonalizacaoMensagemAutomaticaVO() != null && !getPersonalizacaoMensagemAutomaticaVO().isNovoObj()) {
            // getListaPersonalizacao().add(getPersonalizacaoMensagemAutomaticaVO());
            // }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public void gravar() {
        try {
        	        	
        	List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> listaFiltrada  = getPersonalizacaoMensagemAutomaticaVO().getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs().stream().filter(p->p.getUnidadeEnsino().getFiltrarUnidadeEnsino()).collect(Collectors.toList());
        	
        	getPersonalizacaoMensagemAutomaticaVO().getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs().clear();
        	getPersonalizacaoMensagemAutomaticaVO().setPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs(listaFiltrada);
        	getPersonalizacaoMensagemAutomaticaVO().setUsuarioUltimaAlteracao(getUsuarioLogado());
            getPersonalizacaoMensagemAutomaticaVO().setDataUltimaAlteracao(new Date());
            if (getPersonalizacaoMensagemAutomaticaVO().isNovoObj()) {
                getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().incluir(getPersonalizacaoMensagemAutomaticaVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().alterar(getPersonalizacaoMensagemAutomaticaVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<SelectItem> listaSelectItemTemplateMensagemAutomaticaEnum;
    public List<SelectItem> getListaSelectItemTemplateMensagemAutomaticaEnum() {
    	if(listaSelectItemTemplateMensagemAutomaticaEnum == null){
    		listaSelectItemTemplateMensagemAutomaticaEnum = TemplateMensagemAutomaticaEnum.getListaSelectItemTemplateMensagemAutomaticaEnum();
    	}
    	return listaSelectItemTemplateMensagemAutomaticaEnum;
    }
    
    
    public void montarListaSelectItemTemplateMensagemAutomaticaEnum(){
    	if(getModuloTemplateMensagemAutomaticaEnum().equals(ModuloTemplateMensagemAutomaticaEnum.TODOS)){
    		getListaSelectItemTemplateMensagemAutomaticaEnum().clear();
    		for(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum:TemplateMensagemAutomaticaEnum.values()){    			
    			getListaSelectItemTemplateMensagemAutomaticaEnum().add(new SelectItem(templateMensagemAutomaticaEnum, UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + templateMensagemAutomaticaEnum.toString())));
    		}
    	}else{
    		getListaSelectItemTemplateMensagemAutomaticaEnum().clear();
    		for(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum:TemplateMensagemAutomaticaEnum.values()){
    			if(templateMensagemAutomaticaEnum.getModuloTemplateMensagemAutomatica() == null || 
    					templateMensagemAutomaticaEnum.getModuloTemplateMensagemAutomatica().equals(getModuloTemplateMensagemAutomaticaEnum())){
    				listaSelectItemTemplateMensagemAutomaticaEnum.add(new SelectItem(templateMensagemAutomaticaEnum, UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + templateMensagemAutomaticaEnum.toString())));
    			}
    		}
    	}
    }
    

    public PersonalizacaoMensagemAutomaticaVO getPersonalizacaoMensagemAutomaticaVO() {
        if (personalizacaoMensagemAutomaticaVO == null) {
            personalizacaoMensagemAutomaticaVO = new PersonalizacaoMensagemAutomaticaVO();
        }
        return personalizacaoMensagemAutomaticaVO;
    }

    public void setPersonalizacaoMensagemAutomaticaVO(PersonalizacaoMensagemAutomaticaVO personalizacaoMensagemAutomaticaVO) {
        this.personalizacaoMensagemAutomaticaVO = personalizacaoMensagemAutomaticaVO;
    }

    public List<PersonalizacaoMensagemAutomaticaVO> getListaPersonalizacao() {
        if (listaPersonalizacao == null) {
            listaPersonalizacao = new ArrayList<PersonalizacaoMensagemAutomaticaVO>();
        }
        return listaPersonalizacao;
    }

    public void setListaPersonalizacao(List<PersonalizacaoMensagemAutomaticaVO> listaPersonalizacao) {
        this.listaPersonalizacao = listaPersonalizacao;
    }

    public TemplateMensagemAutomaticaEnum getTipoTamplateSelecionado() {
        if (tipoTamplateSelecionado == null) {
            tipoTamplateSelecionado = TemplateMensagemAutomaticaEnum.TODOS;
        }
        return tipoTamplateSelecionado;
    }

    public void setTipoTamplateSelecionado(TemplateMensagemAutomaticaEnum tipoTamplateSelecionado) {
        this.tipoTamplateSelecionado = tipoTamplateSelecionado;
    }
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public void gerarMensagemPadrao() {
        try {
            getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().executarGeracaoMensagemPadrao(false, getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
            getListaPersonalizacao().clear();
            setListaPersonalizacao((getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorParamentrosTemplate("",
                getTipoTamplateSelecionado(), null, "", false, getUsuarioLogado())));

        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public ModuloTemplateMensagemAutomaticaEnum getModuloTemplateMensagemAutomaticaEnum() {
		if(moduloTemplateMensagemAutomaticaEnum == null){
			moduloTemplateMensagemAutomaticaEnum = ModuloTemplateMensagemAutomaticaEnum.TODOS;
		}
		return moduloTemplateMensagemAutomaticaEnum;
	}

	public void setModuloTemplateMensagemAutomaticaEnum(ModuloTemplateMensagemAutomaticaEnum moduloTemplateMensagemAutomaticaEnum) {
		this.moduloTemplateMensagemAutomaticaEnum = moduloTemplateMensagemAutomaticaEnum;
	}
	
	private List<SelectItem> listaSelectItemModuloTemplateMensagemAutomaticaEnum;
	public List<SelectItem> getListaSelectItemModuloTemplateMensagemAutomaticaEnum() {
		if(listaSelectItemModuloTemplateMensagemAutomaticaEnum == null){
			listaSelectItemModuloTemplateMensagemAutomaticaEnum = new ArrayList<SelectItem>(0);			
			for(ModuloTemplateMensagemAutomaticaEnum mensagemAutomaticaEnum:ModuloTemplateMensagemAutomaticaEnum.values()){
				listaSelectItemModuloTemplateMensagemAutomaticaEnum.add(new SelectItem(mensagemAutomaticaEnum, mensagemAutomaticaEnum.getValorApresentar()));
			}
		}
		
        return listaSelectItemModuloTemplateMensagemAutomaticaEnum;
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
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO personalizacaoMensagemAutomaticaUnidade : getPersonalizacaoMensagemAutomaticaVO().getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				personalizacaoMensagemAutomaticaUnidade.getUnidadeEnsino().setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				personalizacaoMensagemAutomaticaUnidade.getUnidadeEnsino().setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}

	}
	

		
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosNiveisEducacionais() {
		if (getMarcarTodosNiveisEducacionais()) {
			realizarMarcarTodosNiveisEducacionais();
		} else {
			realizarDesmarcarTodosNiveisEducacionais();
		}
	}

	public Boolean getMarcarTodosNiveisEducacionais() {
		if (marcarTodosNiveisEducacionais == null) {
			marcarTodosNiveisEducacionais = false;
		}
		return marcarTodosNiveisEducacionais;
	}

	public void setMarcarTodosNiveisEducacionais(Boolean marcarTodosNiveisEducacionais) {
		this.marcarTodosNiveisEducacionais = marcarTodosNiveisEducacionais;
	}
	
	public void realizarMarcarTodosNiveisEducacionais(){
		realizarSelecaoTodosNiveisEducacionais(true);
	}
	
	public void realizarDesmarcarTodosNiveisEducacionais(){
		realizarSelecaoTodosNiveisEducacionais(false);
	}

	public void realizarSelecaoTodosNiveisEducacionais(boolean selecionado){
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalInfantil(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalBasico(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalMedio(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalExtensao(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalSequencial(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalGraduacaoTecnologica(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalSuperior(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalPosGraduacao(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalMestrado(selecionado);
		getPersonalizacaoMensagemAutomaticaVO().setNivelEducacionalProfissionalizante(selecionado);
	}
	
	public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosTiposNiveisEducacionais() {
		if (getMarcarTodosNiveisEducacionais()) {
			return UteisJSF.internacionalizar("prt_PersonalizacaoMensagemAutomatica_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_PersonalizacaoMensagemAutomatica_marcarTodos");
	}
	
	public Boolean getApresentarFiltrosUnidadeEnsinoNivelEducacional(){
		if(getModuloTemplateMensagemAutomaticaEnum()  != null && getTipoTamplateSelecionado() != null && ((getModuloTemplateMensagemAutomaticaEnum().equals(ModuloTemplateMensagemAutomaticaEnum.FINANCEIRO) && getTipoTamplateSelecionado().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_ENVIO_BOLETO_ALUNO)) || (Uteis.isAtributoPreenchido(getPersonalizacaoMensagemAutomaticaVO()) && getPersonalizacaoMensagemAutomaticaVO().getTemplateMensagemAutomaticaEnum() != null && getPersonalizacaoMensagemAutomaticaVO().getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_ENVIO_BOLETO_ALUNO)))) {
			return true;
		}
		
		return false;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getAssunto() {
		if(assunto == null) {
			assunto = "";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	
}
