package controle.crm;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.crm.ComissionamentoTurmaFaixaValorVO;
import negocio.comuns.crm.ComissionamentoTurmaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 *
 * @author Carlos
 */
@Controller("ComissionamentoTurmaControle")
@Scope("viewScope")
@Lazy
public class ComissionamentoTurmaControle extends SuperControle {

    private ComissionamentoTurmaVO comissionamentoTurmaVO;
    private ComissionamentoTurmaFaixaValorVO comissionamentoTurmaFaixaValorVO;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private List listaSelectItemConfiguracaoRanking;

    public ComissionamentoTurmaControle() {
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemConfiguracaoRanking();
    }

    public void montarListaSelectItemConfiguracaoRanking() {
        try {
            setListaSelectItemConfiguracaoRanking(getFacadeFactory().getComissionamentoTurmaFacade().montarListaSelectItemConfiguracaoRanking(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String novo() {
        setComissionamentoTurmaVO(new ComissionamentoTurmaVO());
        setComissionamentoTurmaFaixaValorVO(new ComissionamentoTurmaFaixaValorVO());
        getListaSelectItemConfiguracaoRanking().clear();
        montarListaSelectItemConfiguracaoRanking();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaForm.xhtml");
    }

    public void atualizarTicketMedio() {
    	try {
    		getFacadeFactory().getComissionamentoTurmaFacade().atualizarComissionamentoTurma(0, 0, getComissionamentoTurmaVO().getTurmaVO().getCodigo(), "", null);
    		editar();
    	} catch (Exception e) {
    		setMensagemDetalhada("msg_erro", "Não foi possível atualizar o ticket médio da comissão!");
    	}
    }
    
	public String editar() throws Exception {
        ComissionamentoTurmaVO obj = (ComissionamentoTurmaVO) context().getExternalContext().getRequestMap().get("comissionamentoItens");
        obj.setNovoObj(Boolean.FALSE);
        setComissionamentoTurmaVO(obj);
        getFacadeFactory().getComissionamentoTurmaFacade().carregarDados(getComissionamentoTurmaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
        montarListaSelectItemConfiguracaoRanking();
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Disciplina</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        try {
            getFacadeFactory().getComissionamentoTurmaFacade().persistir(getComissionamentoTurmaVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaForm.xhtml");
        }
    }

    public String excluir() {
        try {
            getFacadeFactory().getComissionamentoTurmaFacade().excluir(getComissionamentoTurmaVO(), getUsuarioLogado());
            setComissionamentoTurmaVO(new ComissionamentoTurmaVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaForm.xhtml");
        }
    }

    @Override
    public String consultar() {
        try {
            getFacadeFactory().getComissionamentoTurmaFacade().consultar(getControleConsultaOtimizado(), getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getControleConsulta().getDataIni(), getUsuarioLogado());
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaCons.xhtml");
        } catch (Exception e) {
            getControleConsultaOtimizado().getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaCons.xhtml");
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            setListaConsultaTurma(getFacadeFactory().getComissionamentoTurmaFacade().consultarTurma(getCampoConsultaTurma(), getValorConsultaTurma(), 0, 0, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        getComissionamentoTurmaVO().setTurmaVO(obj);
        montarListaSelectItemConfiguracaoRanking();
    }

    public void limparConsultaTurma() {
        getListaConsultaTurma().clear();
    }

    public void limparDadosTurma() {
        getComissionamentoTurmaVO().setTurmaVO(new TurmaVO());
        getListaSelectItemConfiguracaoRanking().clear();
    }

    public void adicionarComissaoFaixaValor() {
        try {
            getFacadeFactory().getComissionamentoTurmaFacade().adicionarObjComissionamentoFaixaValorVOs(getComissionamentoTurmaVO(), getComissionamentoTurmaFaixaValorVO());
            setComissionamentoTurmaFaixaValorVO(new ComissionamentoTurmaFaixaValorVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void removerComissionamentoFaixaValor() throws Exception {
        ComissionamentoTurmaFaixaValorVO obj = (ComissionamentoTurmaFaixaValorVO) context().getExternalContext().getRequestMap().get("comissionamentoFaixaValorItens");
        getFacadeFactory().getComissionamentoTurmaFacade().excluirObjComissionamentoFaixaValorVOs(comissionamentoTurmaVO, obj);
        setComissionamentoTurmaFaixaValorVO(new ComissionamentoTurmaFaixaValorVO());
        setMensagemID("msg_dados_excluidos");
    }

    public void realizarInicializacaoDataUltimoPagamento() {
        try {
            getFacadeFactory().getComissionamentoTurmaFacade().realizarInicializacaoDataUltimoPagamento(getComissionamentoTurmaVO());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }


    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("comissionamentoTurmaCons.xhtml");
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ContaReceberCons.jsp. Define o tipo de consulta a
     * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Turma"));
        itens.add(new SelectItem("curso", "Curso"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("dataPrimeiroPgto", "Data Primeiro Pgto"));
        return itens;
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }

    public ComissionamentoTurmaVO getComissionamentoTurmaVO() {
        if (comissionamentoTurmaVO == null) {
            comissionamentoTurmaVO = new ComissionamentoTurmaVO();
        }
        return comissionamentoTurmaVO;
    }

    public void setComissionamentoTurmaVO(ComissionamentoTurmaVO comissionamentoTurmaVO) {
        this.comissionamentoTurmaVO = comissionamentoTurmaVO;
    }

    public ComissionamentoTurmaFaixaValorVO getComissionamentoTurmaFaixaValorVO() {
        if (comissionamentoTurmaFaixaValorVO == null) {
            comissionamentoTurmaFaixaValorVO = new ComissionamentoTurmaFaixaValorVO();
        }
        return comissionamentoTurmaFaixaValorVO;
    }

    public void setComissionamentoTurmaFaixaValorVO(ComissionamentoTurmaFaixaValorVO comissionamentoTurmaFaixaValorVO) {
        this.comissionamentoTurmaFaixaValorVO = comissionamentoTurmaFaixaValorVO;
    }

    public boolean getIsApresentarCampoDataPrimeiroPagamento() {
        return getControleConsulta().getCampoConsulta().equals("dataPrimeiroPgto");
    }

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public boolean getApresentarResultadoFaixaValorVOs() {
        return !getComissionamentoTurmaVO().getListaComissionamentoPorTurmaFaixaValorVOs().isEmpty();
    }

    public List getListaSelectItemConfiguracaoRanking() {
        if (listaSelectItemConfiguracaoRanking == null) {
            listaSelectItemConfiguracaoRanking = new ArrayList(0);
        }
        return listaSelectItemConfiguracaoRanking;
    }

    public void setListaSelectItemConfiguracaoRanking(List listaSelectItemConfiguracaoRanking) {
        this.listaSelectItemConfiguracaoRanking = listaSelectItemConfiguracaoRanking;
    }
}