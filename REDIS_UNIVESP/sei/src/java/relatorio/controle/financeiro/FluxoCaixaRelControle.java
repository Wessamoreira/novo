package relatorio.controle.financeiro;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.FluxoCaixaRelVO;

@Controller("FluxoCaixaRelControle")
@Scope("viewScope")
@Lazy
public class FluxoCaixaRelControle extends SuperControleRelatorio {

    private List listaConsultaUsuario;
    private String valorConsultaUsuario;
    private String campoConsultaUsuario;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemContaCaixa;
    private Date dataInicio;
    private Date dataFim;
    private Integer codigoUnidadeEnsino;
    private Integer usuarioResponsavel;
    private String usuarioNome;
    private Integer contaCaixa;
    private String modeloRelatorio;
    private String tipoLayout;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public FluxoCaixaRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        setModeloRelatorio("retrato");
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<FluxoCaixaRelVO> listaObjetos = null;
        String retornoUsuarioNome = null;
        String retornoContaCaixa = null;
        ContaCorrenteVO obj = null;
        try {
            getFacadeFactory().getFluxoCaixaRelFacade().validarDados(getDataInicio(), getDataFim());
            listaObjetos = getFacadeFactory().getFluxoCaixaRelFacade().criarObjeto(getDataInicio(), getDataFim(), getCodigoUnidadeEnsino(), getContaCaixa(),
                    getUsuarioResponsavel(), getModeloRelatorio(), getTipoLayout());
            if (listaObjetos.isEmpty()) {
                listaObjetos = getFacadeFactory().getFluxoCaixaRelFacade().criarObjetoListaVazia(getDataInicio(), getDataFim(), getCodigoUnidadeEnsino(), getContaCaixa(),
                        getUsuarioResponsavel());
                if (listaObjetos.isEmpty()) {
                    throw new Exception("Não foi encontrado nenhum dado para impressão do relatório. Provavelmente nenhum caixa foi aberto/fechado na data informada!");
                }
            }
            getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getFluxoCaixaRelFacade().designIReportRelatorio(getModeloRelatorio(), getTipoLayout()));
            getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getFluxoCaixaRelFacade().caminhoBaseIReportRelatorio());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setTituloRelatorio("Relatório de Fluxo Caixa");
            getSuperParametroRelVO().setListaObjetos(listaObjetos);
            getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getFluxoCaixaRelFacade().caminhoBaseIReportRelatorio());
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

            getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  até  " + String.valueOf(Uteis.getData(getDataFim())));

            if (getCodigoUnidadeEnsino() != null && getCodigoUnidadeEnsino() > 0) {
                getSuperParametroRelVO().setUnidadeEnsino(
                        (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getCodigoUnidadeEnsino(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
            } else {
                getSuperParametroRelVO().setUnidadeEnsino("Todas");
            }

            retornoContaCaixa = "Todas";
            if (getContaCaixa() != null && getContaCaixa() > 0) {
                obj = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getContaCaixa(), false,Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
                	retornoContaCaixa = obj.getNomeApresentacaoSistema();
                }else{
                	retornoContaCaixa = obj.getNumero() + " - " + obj.getDigito();	
                }
            }
            getSuperParametroRelVO().setContaCorrente(retornoContaCaixa);

            retornoUsuarioNome = "Todos";

            if (getUsuarioNome() != null && !getUsuarioNome().equals("")) {
                getSuperParametroRelVO().setFuncionario(getUsuarioNome());
            } else {
                getSuperParametroRelVO().setFuncionario(retornoUsuarioNome);
            }

            realizarImpressaoRelatorio();
            removerObjetoMemoria(this);
            inicializarListasSelectItemTodosComboBox();
            setModeloRelatorio("retrato");
            setMensagemID("msg_relatorio_ok");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
            retornoUsuarioNome = null;
            retornoContaCaixa = null;
            removerObjetoMemoria(obj);
        }
    }

    public void montarObjetosFluxoCaixaParaRelatorio(Timestamp dataInicio, Date dataFim, Integer codigoUnidadeEnsino, Integer contaCaixa, Integer responsavel) {
        setDataInicio(dataInicio);
        setDataFim(dataFim);
        setCodigoUnidadeEnsino(codigoUnidadeEnsino);
        setContaCaixa(contaCaixa);
        setUsuarioResponsavel(responsavel);
        imprimirPDF();
    }

    public void limparDadosUsuario() {
        setUsuarioResponsavel(0);
        setUsuarioNome("");

    }

    public void consultarUsuario() {
        try {
            if (getValorConsultaUsuario().length() < 2) {
                throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
            }
            if (getCampoConsultaUsuario().equals("nome")) {
            	setListaConsultaUsuario(getFacadeFactory().getUsuarioFacade().consultarPorNome(getValorConsultaUsuario(), false, 
            			Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }
            if (getCampoConsultaUsuario().equals("username")) {
            	setListaConsultaUsuario(getFacadeFactory().getUsuarioFacade().consultarPorUsername(getValorConsultaUsuario(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaUsuario(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboUsuario() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("username", "Login"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboModeloRelatorio() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("retrato", "Retrato"));
        itens.add(new SelectItem("paisagem", "Paisagem"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboTipoLayout() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("layout1", "Layout 1"));
        itens.add(new SelectItem("layout2", "Layout 2"));
        return itens;
    }

    public void selecionarUsuario() {
        UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItens");
        setUsuarioResponsavel(obj.getCodigo());
        setUsuarioNome(obj.getNome());
        setCampoConsultaUsuario("");
        setValorConsultaUsuario("");
        setListaConsultaUsuario(new ArrayList<>(0));
    }

    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemOrdenacao();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemContaCaixa();
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getFluxoCaixaRelFacade().getOrdenacoesRelatorio();
        Enumeration i = opcoes.elements();
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        int contador = 0;
        while (i.hasMoreElements()) {
            String option = (String) i.nextElement();
            objs.add(new SelectItem(new Integer(contador), option));
            contador++;
        }
        setListaSelectItemOrdenacoesRelatorio(objs);
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }

    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
                objs.add(new SelectItem(0, ""));
            } else {
            	setCodigoUnidadeEnsino(super.getUnidadeEnsinoLogado().getCodigo());
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemContaCaixa() throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarContaCaixaPorNumero();
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
                if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
                	objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));	
                }else{
                	objs.add(new SelectItem(obj.getCodigo(), obj.getNumero() + "-" + obj.getDigito()));	
                }
            }
            setListaSelectItemContaCaixa(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List consultarContaCaixaPorNumero() throws Exception {
        List lista = getFacadeFactory().getContaCorrenteFacade().consultarPorContaCaixa(true, getCodigoUnidadeEnsino(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List getListaConsultaUsuario() {
        return listaConsultaUsuario;
    }

    public void setListaConsultaUsuario(List listaConsultaUsuario) {
        this.listaConsultaUsuario = listaConsultaUsuario;
    }

    public String getValorConsultaUsuario() {
        return valorConsultaUsuario;
    }

    public void setValorConsultaUsuario(String valorConsultaUsuario) {
        this.valorConsultaUsuario = valorConsultaUsuario;
    }

    public String getCampoConsultaUsuario() {
        return campoConsultaUsuario;
    }

    public void setCampoConsultaUsuario(String campoConsultaUsuario) {
        this.campoConsultaUsuario = campoConsultaUsuario;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List getListaSelectItemContaCaixa() {
        return listaSelectItemContaCaixa;
    }

    public void setListaSelectItemContaCaixa(List listaSelectItemContaCaixa) {
        this.listaSelectItemContaCaixa = listaSelectItemContaCaixa;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getNewDateComMesesAMenos(1);
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Integer getCodigoUnidadeEnsino() {
        if (codigoUnidadeEnsino == null) {
            codigoUnidadeEnsino = 0;
        }
        return codigoUnidadeEnsino;
    }

    public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
        this.codigoUnidadeEnsino = codigoUnidadeEnsino;
    }

    public Integer getUsuarioResponsavel() {
        if (usuarioResponsavel == null) {
            usuarioResponsavel = 0;
        }
        return usuarioResponsavel;
    }

    public void setUsuarioResponsavel(Integer usuarioResponsavel) {
        this.usuarioResponsavel = usuarioResponsavel;
    }

    public String getUsuarioNome() {
        if (usuarioNome == null) {
            usuarioNome = "";
        }
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public Integer getContaCaixa() {
        if (contaCaixa == null) {
            contaCaixa = 0;
        }
        return contaCaixa;
    }

    public void setContaCaixa(Integer contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public String getModeloRelatorio() {
        if (modeloRelatorio == null) {
            modeloRelatorio = "";
        }
        return modeloRelatorio;
    }

    public void setModeloRelatorio(String modeloRelatorio) {
        this.modeloRelatorio = modeloRelatorio;
    }

    public boolean getIsModeloRelatorioRetrato() {
        if (getModeloRelatorio().equals("retrato")) {
            return true;
        }
        return false;
    }

    public String getTipoLayout() {
        if(tipoLayout == null){
            tipoLayout = "layout1";
        }
        return tipoLayout;
    }

    public void setTipoLayout(String tipoLayout) {
        this.tipoLayout = tipoLayout;
    }
}
