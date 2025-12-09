package controle.protocolo;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * controleCorrespondenciaForm.jsp controleCorrespondenciaCons.jsp) com as funcionalidades da classe <code>ControleCorrespondencia</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see ControleCorrespondencia
 * @see ControleCorrespondenciaVO
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.ControleCorrespondenciaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("ControleCorrespondenciaControle")
@Scope("viewScope")
@Lazy
public class ControleCorrespondenciaControle extends SuperControle implements Serializable {
    private ControleCorrespondenciaVO controleCorrespondenciaVO;
    private String responsavelDptoOrigem_Erro;
    private String responsavelProtocoloOrigem_Erro;
    private String responsavelProtocoloDestino_Erro;
    private String responsavelDptoDestino_Erro;
    
    public ControleCorrespondenciaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public String getLinkInicial(){
    	return " << ";
    }
    
    public String getLinkFinal(){
    	return ">>";
    }
    
    public String getLinkProximo(){
    	return ">";
    }
    
    public String getLinkAnterior(){
    	return "<";
    }

    /**
    * Rotina responsável por disponibilizar um novo objeto da classe <code>ControleCorrespondencia</code>
    * para edição pelo usuário da aplicação.
    */
    public String novo() {         
    	removerObjetoMemoria(this);
        setControleCorrespondenciaVO(new ControleCorrespondenciaVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleCorrespondenciaForm");
    }

    /**
    * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ControleCorrespondencia</code> para alteração.
    * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
    */
    public String editar() {
        ControleCorrespondenciaVO obj = (ControleCorrespondenciaVO)context().getExternalContext().getRequestMap().get("controleCorrespondenciaItem");
        obj.setNovoObj(Boolean.FALSE);
        setControleCorrespondenciaVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleCorrespondenciaForm");
    }

    /**
    * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ControleCorrespondencia</code>.
    * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
    * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
    */
    public void gravar() {
        try {
            if (controleCorrespondenciaVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getControleCorrespondenciaFacade().incluir(controleCorrespondenciaVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getControleCorrespondenciaFacade().alterar(controleCorrespondenciaVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
    * Rotina responsavel por executar as consultas disponiveis no JSP ControleCorrespondenciaCons.jsp.
    * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
    * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
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
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorData(Uteis.getDateTime(valorData,0,0,0), Uteis.getDateTime(valorData,23,59,59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("remetente")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorRemetente(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("destinatario")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorDestinatario(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipoCorrespondencia")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorTipoCorrespondencia(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataRecebProtocoloOrigem")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorDataRecebProtocoloOrigem(Uteis.getDateTime(valorData,0,0,0), Uteis.getDateTime(valorData,23,59,59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataRecebProtocoloDestino")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorDataRecebProtocoloDestino(Uteis.getDateTime(valorData,0,0,0), Uteis.getDateTime(valorData,23,59,59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataRecebDptoDestino")) {
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getControleCorrespondenciaFacade().consultarPorDataRecebDptoDestino(Uteis.getDateTime(valorData,0,0,0), Uteis.getDateTime(valorData,23,59,59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,getUsuarioLogado());
            }
            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
           
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ControleCorrespondenciaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {
            getFacadeFactory().getControleCorrespondenciaFacade().excluir(controleCorrespondenciaVO, getUsuarioLogado());
            setControleCorrespondenciaVO( new ControleCorrespondenciaVO());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void irPaginaInicial() throws Exception{
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /* Método responsável por inicializar List<SelectItem> de valores do 
     * ComboBox correspondente ao atributo <code>tipoCorrespondencia</code>
     */ 
    public List<SelectItem> getListaSelectItemTipoCorrespondenciaControleCorrespondencia() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoCorrespondencias = (Hashtable)Dominios.getTipoCorrespondencia();
        Enumeration keys = tipoCorrespondencias.keys();
        while (keys.hasMoreElements()) {
            String value = (String)keys.nextElement();
            String label = (String)tipoCorrespondencias.get(value);
            objs.add(new SelectItem( value, label));
        }
        return objs;
    }

    /* Método responsável por inicializar List<SelectItem> de valores do 
     * ComboBox correspondente ao atributo <code>situacao</code>
     */ 
    public List<SelectItem> getListaSelectItemSituacaoControleCorrespondencia() throws Exception {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("", ""));
        Hashtable situacaos = (Hashtable)Dominios.getSituacao();
        Enumeration keys = situacaos.keys();
        while (keys.hasMoreElements()) {
            String value = (String)keys.nextElement();
            String label = (String)situacaos.get(value);
            objs.add(new SelectItem( value, label));
        }
        return objs;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave primária.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade
     * montando automaticamente o resultado da consulta para apresentação.
    */
    public void consultarPessoaPorChavePrimaria() {
        try {
            Integer campoConsulta = controleCorrespondenciaVO.getResponsavelDptoOrigem().getCodigo();
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            controleCorrespondenciaVO.getResponsavelDptoOrigem().setNome(pessoa.getNome());
            this.setResponsavelDptoOrigem_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            controleCorrespondenciaVO.getResponsavelDptoOrigem().setNome("");
            controleCorrespondenciaVO.getResponsavelDptoOrigem().setCodigo(0);
            this.setResponsavelDptoOrigem_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }
    

    /**
    * Rotina responsável por preencher a combo de consulta da telas.
    */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("remetente", "Remetente"));
        itens.add(new SelectItem("destinatario", "Destinatário"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("tipoCorrespondencia", "Tipo Correspondência"));
        itens.add(new SelectItem("nomePessoa", "Responsável Dpto Origem"));
        itens.add(new SelectItem("nomePessoa", "Responsável Protocolo Origem"));
        itens.add(new SelectItem("dataRecebProtocoloOrigem", "Data Recebimento Protocolo Origem"));
        itens.add(new SelectItem("nomePessoa", "Responsável Protocolo Destino"));
        itens.add(new SelectItem("dataRecebProtocoloDestino", "Data Recebimento Protocolo Destino"));
        itens.add(new SelectItem("nomePessoa", "Responsável Dpto Destino"));
        itens.add(new SelectItem("dataRecebDptoDestino", "Data Recebimento Dpto Destino"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    /**
    * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
    */
    public String inicializarConsultar() {         
    	removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleCorrespondenciaCons");
    }
    
    public String getResponsavelDptoDestino_Erro() {
        return responsavelDptoDestino_Erro;
    }
     
    public void setResponsavelDptoDestino_Erro(String responsavelDptoDestino_Erro) {
        this.responsavelDptoDestino_Erro = responsavelDptoDestino_Erro;
    }

    public String getResponsavelProtocoloDestino_Erro() {
        return responsavelProtocoloDestino_Erro;
    }
     
    public void setResponsavelProtocoloDestino_Erro(String responsavelProtocoloDestino_Erro) {
        this.responsavelProtocoloDestino_Erro = responsavelProtocoloDestino_Erro;
    }

    public String getResponsavelProtocoloOrigem_Erro() {
        return responsavelProtocoloOrigem_Erro;
    }
     
    public void setResponsavelProtocoloOrigem_Erro(String responsavelProtocoloOrigem_Erro) {
        this.responsavelProtocoloOrigem_Erro = responsavelProtocoloOrigem_Erro;
    }

    public String getResponsavelDptoOrigem_Erro() {
        return responsavelDptoOrigem_Erro;
    }
     
    public void setResponsavelDptoOrigem_Erro(String responsavelDptoOrigem_Erro) {
        this.responsavelDptoOrigem_Erro = responsavelDptoOrigem_Erro;
    }

    public ControleCorrespondenciaVO getControleCorrespondenciaVO() {
        return controleCorrespondenciaVO;
    }
     
    public void setControleCorrespondenciaVO(ControleCorrespondenciaVO controleCorrespondenciaVO) {
        this.controleCorrespondenciaVO = controleCorrespondenciaVO;
    }
}