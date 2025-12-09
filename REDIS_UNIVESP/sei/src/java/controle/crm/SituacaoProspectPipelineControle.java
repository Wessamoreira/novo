package controle.crm;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.crm.SituacaoProspectPipelineVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.crm.SituacaoProspectPipeline;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * situacaoProspectPipelineForm.jsp situacaoProspectPipelineCons.jsp) com as funcionalidades da classe <code>SituacaoProspectPipeline</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see SituacaoProspectPipeline
 * @see SituacaoProspectPipelineVO
*/

@Controller("SituacaoProspectPipelineControle")
@Scope("viewScope")
@Lazy
public class SituacaoProspectPipelineControle extends SuperControle {
    
    
    private SituacaoProspectPipelineVO situacaoProspectPipelineVO;
    
    private Boolean editarTexto_Fundo;

    public SituacaoProspectPipelineControle() throws Exception {
        
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
    * Rotina responsável por disponibilizar um novo objeto da classe <code>SituacaoProspectPipeline</code>
    * para edição pelo usuário da aplicação.
    */
    @PostConstruct
    public String novo() {
        setSituacaoProspectPipelineVO(new SituacaoProspectPipelineVO());
        setEditarTexto_Fundo(false);
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("situacaoProspectPipelineForm.xhtml");
    }

    /**
    * Rotina responsável por disponibilizar os dados de um objeto da classe <code>SituacaoProspectPipeline</code> para alteração.
    * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
    */
    public String editar() {
        SituacaoProspectPipelineVO obj = (SituacaoProspectPipelineVO)context().getExternalContext().getRequestMap().get("situacaoProspectPipelineItens");
        obj.setNovoObj(new Boolean(false));
        setSituacaoProspectPipelineVO(obj);
        setMensagemID("msg_dados_editar", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("situacaoProspectPipelineForm.xhtml");
    }

    /**
    * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>SituacaoProspectPipeline</code>.
    * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
    * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
    */
    public String persistir() {
        try {
        	getSituacaoProspectPipelineVO().setCorFundo(getSituacaoProspectPipelineVO().getCorFundo_Apresentacao());
        	getSituacaoProspectPipelineVO().setCorTexto(getSituacaoProspectPipelineVO().getCorTexto_Apresentacao());
            getFacadeFactory().getSituacaoProspectPipelineFacade().persistir(getSituacaoProspectPipelineVO(), getUsuarioLogado());
//            setEditarTexto_Fundo(true);
//            situacaoProspectEditadaCorFundo();
//            situacaoProspectEditadaCorTexto();
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("situacaoProspectPipelineForm.xhtml");
        }
    }

    /**
    * Rotina responsavel por executar as consultas disponiveis no JSP SituacaoProspectPipelineCons.jsp.
    * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
    * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
    */
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getSituacaoProspectPipelineFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuario()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("situacaoProspectPipelineCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>SituacaoProspectPipelineVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getSituacaoProspectPipelineFacade().excluir(situacaoProspectPipelineVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("situacaoProspectPipelineForm.xhtml");
        }
    }

    /**
    * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
    */
    public String getMascaraConsulta() {
        return "";
    }

    /**
    * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
    */
    public String inicializarConsultar() {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        getListaConsulta().clear();
        return Uteis.getCaminhoRedirecionamentoNavegacao("situacaoProspectPipelineCons.xhtml");
    }

    /**
    * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
    * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente 
    * quando realiza o logout.
    */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        situacaoProspectPipelineVO = null;
    }

    public SituacaoProspectPipelineVO getSituacaoProspectPipelineVO() {
        if (situacaoProspectPipelineVO == null) {
            situacaoProspectPipelineVO = new SituacaoProspectPipelineVO();
        }
        return situacaoProspectPipelineVO;
    }
     
    public void setSituacaoProspectPipelineVO(SituacaoProspectPipelineVO situacaoProspectPipelineVO) {
        this.situacaoProspectPipelineVO = situacaoProspectPipelineVO;
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }
    
    public Boolean getEditarTexto_Fundo() {
    	if (editarTexto_Fundo == null) {
    		editarTexto_Fundo = false;
    	}
    	return editarTexto_Fundo;
	}
    
    public void setEditarTexto_Fundo(Boolean editarTexto_Fundo) {
		this.editarTexto_Fundo = editarTexto_Fundo;
	}
    
    public String situacaoProspectEditadaCorFundo() {
    	setEditarTexto_Fundo(true);
    	if (getEditarTexto_Fundo()) {
    		getSituacaoProspectPipelineVO().setCorFundo(getSituacaoProspectPipelineVO().getCorFundo_Apresentacao());
    	} else {
    		getSituacaoProspectPipelineVO().setCorFundo(getSituacaoProspectPipelineVO().getCorFundo());
    	}
		return getSituacaoProspectPipelineVO().getCorFundo();
    }
    
    public String situacaoProspectEditadaCorTexto() {
    	setEditarTexto_Fundo(true);
    	if (getEditarTexto_Fundo()) {
    		getSituacaoProspectPipelineVO().setCorTexto(getSituacaoProspectPipelineVO().getCorTexto_Apresentacao());
    	} else {
    		getSituacaoProspectPipelineVO().setCorTexto(getSituacaoProspectPipelineVO().getCorTexto());
    	}
    	return getSituacaoProspectPipelineVO().getCorTexto();
    }

}
