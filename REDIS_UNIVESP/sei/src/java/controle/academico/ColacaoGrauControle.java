package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas colacaoGrauForm.jsp
 * colacaoGrauCons.jsp) com as funcionalidades da classe <code>ColacaoGrau</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see ColacaoGrau
 * @see ColacaoGrauVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoColacaoGrau;
@Controller("ColacaoGrauControle")
@Scope("viewScope")
@Lazy
public class ColacaoGrauControle extends SuperControle implements Serializable {

    private ColacaoGrauVO colacaoGrauVO;
    private String campoConsultarPresidenteMesa;
    private String valorConsultarPresidenteMesa;
    private List listaConsultarPresidenteMesa;
    private String campoConsultarSecretariaAcademica;
    private String valorConsultarSecretariaAcademica;
    private List listaConsultarSecretariaAcademica;
    private Date valorConsultaData;
    private String valorConsultaSituacao;

    public ColacaoGrauControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>ColacaoGrau</code> para edição pelo usuário
     * da aplicação.
     */
    public String novo() {         
    	removerObjetoMemoria(this);
        setColacaoGrauVO(new ColacaoGrauVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ColacaoGrau</code> para alteração. O
     * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        ColacaoGrauVO obj = (ColacaoGrauVO) context().getExternalContext().getRequestMap().get("colacaoGrauItens");
        obj.setNovoObj(Boolean.FALSE);
        setColacaoGrauVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ColacaoGrau</code>. Caso
     * o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (colacaoGrauVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getColacaoGrauFacade().incluir(colacaoGrauVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getColacaoGrauFacade().alterar(colacaoGrauVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ColacaoGrauCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
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
                objs = getFacadeFactory().getColacaoGrauFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("titulo")) {
                objs = getFacadeFactory().getColacaoGrauFacade().consultarPorTitulo(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {

                objs = getFacadeFactory().getColacaoGrauFacade().consultarPorData(Uteis.getDateTime(getValorConsultaData(), 0, 0, 0), Uteis.getDateTime(getValorConsultaData(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("local")) {
                objs = getFacadeFactory().getColacaoGrauFacade().consultarPorLocal(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("ata")) {
                objs = getFacadeFactory().getColacaoGrauFacade().consultarPorAta(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getColacaoGrauFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getColacaoGrauFacade().consultarPorSituacao(getValorConsultaSituacao(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ColacaoGrauVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getColacaoGrauFacade().excluir(colacaoGrauVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauForm.xhtml");
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio dos parametros informados no
     * richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarSecretariaAcademica() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarSecretariaAcademica().equals("codigo")) {
                if (getValorConsultarSecretariaAcademica().equals("")) {
                    setValorConsultarSecretariaAcademica("0");
                }
                Integer valorInt = Integer.parseInt(getValorConsultarSecretariaAcademica());
                objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(valorInt, "FU", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarSecretariaAcademica().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultarSecretariaAcademica(), "FU", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarSecretariaAcademica().equals("CPF")) {
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultarSecretariaAcademica(), "FU", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultarSecretariaAcademica(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarSecretariaAcademica(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarSecretariaAcademica() throws Exception {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
        if (getMensagemDetalhada().equals("")) {
            this.getColacaoGrauVO().setSecretariaAcademica(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarSecretariaAcademica());
        this.setValorConsultarSecretariaAcademica(null);
        this.setCampoConsultarSecretariaAcademica(null);
    }

    public void limparCampoSecretariaAcademica() {
        this.getColacaoGrauVO().setSecretariaAcademica(new PessoaVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboSecretariaAcademica() {
        List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        // itens.add(new SelectItem("nomeCidade", "Cidade"));
        itens.add(new SelectItem("CPF", "CPF"));
        // itens.add(new SelectItem("RG", "RG"));
        // itens.add(new SelectItem("necessidadesEspeciais", "Necessidades Especiais"));
        return itens;
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio dos parametros informados no
     * richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros
     * informados no richModal montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPresidenteMesa() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultarPresidenteMesa().equals("codigo")) {
                if (getValorConsultarPresidenteMesa().equals("")) {
                    setValorConsultarPresidenteMesa("0");
                }
                Integer valorInt = Integer.parseInt(getValorConsultarPresidenteMesa());
                objs = getFacadeFactory().getPessoaFacade().consultarPorCodigo(valorInt, "FU", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarPresidenteMesa().equals("nome")) {
                if (getValorConsultarPresidenteMesa().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultarPresidenteMesa(), "FU", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarPresidenteMesa().equals("CPF")) {
                if (getValorConsultarPresidenteMesa().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultarPresidenteMesa(), "FU", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultarPresidenteMesa().equals("RG")) {
                if (getValorConsultarPresidenteMesa().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultarPresidenteMesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultarPresidenteMesa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultarPresidenteMesa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarPresidenteMesa() throws Exception {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
        if (getMensagemDetalhada().equals("")) {
            this.getColacaoGrauVO().setPresidenteMesa(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarPresidenteMesa());
        this.setValorConsultarPresidenteMesa(null);
        this.setCampoConsultarPresidenteMesa(null);
    }

    public void limparCampoPresidenteMesa() {
        this.getColacaoGrauVO().setPresidenteMesa(new PessoaVO());
    }

    /**
     * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
     */
    public List getTipoConsultarComboPresidenteMesa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("titulo", "Título"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("local", "Local"));
        itens.add(new SelectItem("ata", "ATA"));
        itens.add(new SelectItem("nomePessoa", "Presidente Mesa"));
        itens.add(new SelectItem("nomePessoa", "Secretaria Acadêmica"));
        itens.add(new SelectItem("situacao", "Situação"));
        return itens;
    }

    public List<SelectItem> getSituacaoColacaoGrau() throws Exception {
        List<SelectItem> opcoes = new ArrayList<SelectItem>();
        opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoColacaoGrau.class, false);
        return opcoes;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         
    	removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        getControleConsulta().setCampoConsulta("codigo");
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("colacaoGrauCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        colacaoGrauVO = null;
    }

    public String getCampoConsultarSecretariaAcademica() {
        return campoConsultarSecretariaAcademica;
    }

    public void setCampoConsultarSecretariaAcademica(String campoConsultarSecretariaAcademica) {
        this.campoConsultarSecretariaAcademica = campoConsultarSecretariaAcademica;
    }

    public String getValorConsultarSecretariaAcademica() {
        return valorConsultarSecretariaAcademica;
    }

    public void setValorConsultarSecretariaAcademica(String valorConsultarSecretariaAcademica) {
        this.valorConsultarSecretariaAcademica = valorConsultarSecretariaAcademica;
    }

    public List getListaConsultarSecretariaAcademica() {
        return listaConsultarSecretariaAcademica;
    }

    public void setListaConsultarSecretariaAcademica(List listaConsultarSecretariaAcademica) {
        this.listaConsultarSecretariaAcademica = listaConsultarSecretariaAcademica;
    }

    public String getCampoConsultarPresidenteMesa() {
        return campoConsultarPresidenteMesa;
    }

    public void setCampoConsultarPresidenteMesa(String campoConsultarPresidenteMesa) {
        this.campoConsultarPresidenteMesa = campoConsultarPresidenteMesa;
    }

    public String getValorConsultarPresidenteMesa() {
        return valorConsultarPresidenteMesa;
    }

    public void setValorConsultarPresidenteMesa(String valorConsultarPresidenteMesa) {
        this.valorConsultarPresidenteMesa = valorConsultarPresidenteMesa;
    }

    public List getListaConsultarPresidenteMesa() {
        return listaConsultarPresidenteMesa;
    }

    public void setListaConsultarPresidenteMesa(List listaConsultarPresidenteMesa) {
        this.listaConsultarPresidenteMesa = listaConsultarPresidenteMesa;
    }

    public ColacaoGrauVO getColacaoGrauVO() {
        return colacaoGrauVO;
    }

    public void setColacaoGrauVO(ColacaoGrauVO colacaoGrauVO) {
        this.colacaoGrauVO = colacaoGrauVO;
    }

    /**
     * @return the valorConsultaData
     */
    public Date getValorConsultaData() {
        if (valorConsultaData == null) {
            valorConsultaData = new Date();
        }
        return valorConsultaData;
    }

    /**
     * @param valorConsultaData the valorConsultaData to set
     */
    public void setValorConsultaData(Date valorConsultaData) {
        this.valorConsultaData = valorConsultaData;
    }
    
    

    public String getValorConsultaSituacao() {
    	if(valorConsultaSituacao == null){
    		valorConsultaSituacao ="";
    	}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public Boolean getApresentarCampoSituacao() {
        if (getControleConsulta().getCampoConsulta().equals("situacao")) {
            return true;
        }
        return false;
    }
    
    public Boolean getApresentarCampoData() {
    	if (getControleConsulta().getCampoConsulta().equals("data")) {
    		return true;
    	}
    	return false;
    }
}
