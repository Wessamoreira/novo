/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.crm;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.crm.PainelGestorSupervisaoVendaVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author RODRIGO
 */
@Controller(value="PainelGestorCrmVendedorControle")
@Lazy
@Scope(value="viewScope")
public class PainelGestorCrmVendedorControle extends SuperControle{
        
    private List<SelectItem> listaSelectItemCampanha;
    private Date dataInicio;
    private Date dataTermino;
    private PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO;
    private Integer campanha;
    private Integer funcionario;
    private Boolean exibirRelatorio;

    public PainelGestorCrmVendedorControle() {
        
    }   
    
    @PostConstruct
    public void inicializarDadosPainelGestorCrmVendedor(){
        try{
            if(request().getParameter("ca") != null && Integer.valueOf(request().getParameter("ca"))>0){ 
                setCampanha(Integer.valueOf(request().getParameter("ca")));            
                if(request().getParameter("us") != null && Integer.valueOf(request().getParameter("us"))>0){                
                    setFuncionario(Integer.valueOf(request().getParameter("us")));
                    getPainelGestorSupervisaoVendaVO().getVendedor().setCodigo(Integer.valueOf(request().getParameter("us")));
                    request().removeAttribute("us");                
                }
                executarGeracaoDadosEstatisticaCampanha();
            }else{
                setFuncionario(getUsuarioLogado().getCodigo());
                getPainelGestorSupervisaoVendaVO().getVendedor().setCodigo(getUsuarioLogado().getCodigo());
            }
        }catch(Exception e){
            setFuncionario(getUsuarioLogado().getCodigo());
            getPainelGestorSupervisaoVendaVO().getVendedor().setCodigo(getUsuarioLogado().getCodigo());
        }
    }
    
    public PainelGestorSupervisaoVendaVO getPainelGestorSupervisaoVendaVO() {
        if(painelGestorSupervisaoVendaVO == null){
            painelGestorSupervisaoVendaVO = new PainelGestorSupervisaoVendaVO();
        }
        return painelGestorSupervisaoVendaVO;
    }

    public void setPainelGestorSupervisaoVendaVO(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO) {
        this.painelGestorSupervisaoVendaVO = painelGestorSupervisaoVendaVO;
    }
    
    public void executarGeracaoDadosEstatisticaCampanha(){
        try{
            setPainelGestorSupervisaoVendaVO(getFacadeFactory().getPainelGestorSupervisaoVendaFacade().executarGeracaoDadosEstatisticaCampanha(getCampanha(), getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo(), getDataInicio(), getDataTermino()));
            context().getExternalContext().getRequestMap().put("urlXmlSucesso", "http://192.168.0.15/repositorio/XML/etapas.xml");
            context().getExternalContext().getRequestMap().put("time", new Date().getTime());
            
            setExibirRelatorio(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setExibirRelatorio(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    private Boolean abrirModalPerfilProspect;

    public Boolean getAbrirModalPerfilProspect() {
        if(abrirModalPerfilProspect == null){
            abrirModalPerfilProspect = false;
        }
        return abrirModalPerfilProspect;
    }
    
    

    public void setAbrirModalPerfilProspect(Boolean abrirModalPerfilProspect) {
        this.abrirModalPerfilProspect = abrirModalPerfilProspect;
    }
    
    public String getModalPerfilProspect(){
        return getAbrirModalPerfilProspect()?"RichFaces.$('modalPerfilProspect').show();":"RichFaces.$('modalPerfilProspect').hide();";
    }
    
    public void realizarApresentacaoPerfilProspectSucesso(){
        try{
            getFacadeFactory().getPainelGestorSupervisaoVendaFacade().consultarEstatisticaoPerfilProspect(getPainelGestorSupervisaoVendaVO(), getPainelGestorSupervisaoVendaVO().getCampanha(), getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo(),
                    getDataInicio(), getDataTermino(), SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO);
            setAbrirModalPerfilProspect(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setAbrirModalPerfilProspect(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
            
    public void realizarApresentacaoPerfilProspectInsucesso(){
        try{
            getFacadeFactory().getPainelGestorSupervisaoVendaFacade().consultarEstatisticaoPerfilProspect(getPainelGestorSupervisaoVendaVO(), getPainelGestorSupervisaoVendaVO().getCampanha(), getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo(),
                    getDataInicio(), getDataTermino(), SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO);
            setAbrirModalPerfilProspect(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setAbrirModalPerfilProspect(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    
    public void realizarApresentacaoPerfilProspectNaoFinalizado(){
        try{
            getFacadeFactory().getPainelGestorSupervisaoVendaFacade().consultarEstatisticaoPerfilProspect(getPainelGestorSupervisaoVendaVO(), getPainelGestorSupervisaoVendaVO().getCampanha(), getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo(),
                    getDataInicio(), getDataTermino(), SituacaoProspectPipelineControleEnum.NENHUM);
            setAbrirModalPerfilProspect(true);
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        }catch(Exception e){
            setAbrirModalPerfilProspect(false);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }
    

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }

    public List<SelectItem> getListaSelectItemCampanha() {
        if(listaSelectItemCampanha == null){
            try {
                listaSelectItemCampanha = getFacadeFactory().getCampanhaFacade().consultarListaSelectItemCampanha(getUnidadeEnsinoLogado().getCodigo(), "AT", Obrigatorio.SIM, getPainelGestorSupervisaoVendaVO().getVendedor().getCodigo());
                if(listaSelectItemCampanha != null && !listaSelectItemCampanha.isEmpty() && (getCampanha() == null || getCampanha() ==0)){
                    setCampanha((Integer)listaSelectItemCampanha.get(0).getValue());
                    executarGeracaoDadosEstatisticaCampanha();
                }
            } catch (Exception ex) {
                setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
            }
        }   
        return listaSelectItemCampanha;
    }

    public void setListaSelectItemCampanha(List<SelectItem> listaSelectItemCampanha) {
        this.listaSelectItemCampanha = listaSelectItemCampanha;
    }

    public Integer getCampanha() {
        return campanha;
    }

    public void setCampanha(Integer campanha) {
        this.campanha = campanha;
    }

    public Integer getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Integer funcionario) {
        this.funcionario = funcionario;
    }

    public Boolean getExibirRelatorio() {
        if(exibirRelatorio == null){
            exibirRelatorio = false;
        }
        return exibirRelatorio;
    }

    public void setExibirRelatorio(Boolean exibirRelatorio) {
        this.exibirRelatorio = exibirRelatorio;
    }
    
    
    
    
    
}
