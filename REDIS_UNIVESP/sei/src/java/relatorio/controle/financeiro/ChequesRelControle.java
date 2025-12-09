package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.faces.model.SelectItem;
import negocio.comuns.administrativo.UnidadeEnsinoVO;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ChequeRelVO;

@Controller("ChequesRelControle")
@Scope("viewScope")
@Lazy
public class ChequesRelControle extends SuperControleRelatorio {

    protected List listaTipoSituacao;
    private String tipoSituacao;
    private String tipoFiltro;
    private String ordenarPor;
    private Date dataInicio;
    private Date dataFim;
    private Date dataInicioPrevisao;
    private Date dataFimPrevisao;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List listaSelectItemUnidadeEnsino;
    
    //
    public ChequesRelControle() throws Exception {
        montarListaTipoFiltro();
        montarListaSelectItemUnidadeEnsino();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {    
        List<ChequeRelVO> listaObjetos = null;
        String situacao = null;
        try {
            listaObjetos = getFacadeFactory().getChequesRelFacade().criarObjeto(getUnidadeEnsinoVO(), getTipoSituacao(), getTipoFiltro(), getOrdenarPor(), getDataInicio(), getDataFim(),
                getDataInicioPrevisao(), getDataFimPrevisao(), getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getChequesRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getChequesRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Cheques");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getChequesRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setTipoCheque(getTipoFiltro());
                
                
                if (getTipoSituacao() != null && !getTipoSituacao().equals("0")) {
                    situacao = SituacaoCheque.getDescricao(getTipoSituacao());
                    getSuperParametroRelVO().setSituacao(situacao);
                    situacao = null;
                }else {
                    getSuperParametroRelVO().setSituacao(getTipoSituacao().equals("0") ? "TODOS" : getTipoSituacao());    
                }
                
                getSuperParametroRelVO().setDataEmissao(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                getSuperParametroRelVO().setDataPrevisao(String.valueOf(Uteis.getData(getDataInicioPrevisao())) + "  a  " + String.valueOf(Uteis.getData(getDataFimPrevisao())));

                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaTipoFiltro();
                montarListaSelectItemUnidadeEnsino();

                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
            	setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            situacao = null;
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void imprimirEXCEL() {
        List<ChequeRelVO> listaObjetos = null;
        String situacao = null;
        try {
            listaObjetos = getFacadeFactory().getChequesRelFacade().criarObjeto(getUnidadeEnsinoVO(), getTipoSituacao(), getTipoFiltro(), getOrdenarPor(), getDataInicio(), getDataFim(),
                getDataInicioPrevisao(), getDataFimPrevisao(), getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getChequesRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getChequesRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Cheques");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getChequesRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setTipoCheque(getTipoFiltro());


                if (getTipoSituacao() != null && !getTipoSituacao().equals("0")) {
                    situacao = SituacaoCheque.getDescricao(getTipoSituacao());
                    getSuperParametroRelVO().setSituacao(situacao);
                    situacao = null;
                }else {
                    getSuperParametroRelVO().setSituacao(getTipoSituacao().equals("0") ? "TODOS" : getTipoSituacao());
                }

                getSuperParametroRelVO().setDataEmissao(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                getSuperParametroRelVO().setDataPrevisao(String.valueOf(Uteis.getData(getDataInicioPrevisao())) + "  a  " + String.valueOf(Uteis.getData(getDataFimPrevisao())));

                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                montarListaTipoFiltro();
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
            	setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            situacao = null;
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemOrdenacao();
    }

    public void montarListaSelectItemOrdenacao() {
        Vector opcoes = getFacadeFactory().getChequesRelFacade().getOrdenacoesRelatorio();
        Enumeration i = opcoes.elements();
        List objs = new ArrayList(0);
        int contador = 0;
        while (i.hasMoreElements()) {
            String opcao = (String) i.nextElement();
            objs.add(new SelectItem(new Integer(contador), opcao));
            contador++;
        }
        setListaSelectItemOrdenacoesRelatorio(objs);
    }

    public void montarListaTipoFiltro() throws Exception {
        if (getTipoFiltro().equals("Emitidos")) {
            List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoCheque.class);
            List<SelectItem> objs = new ArrayList<SelectItem>();
            int i = 0;
            int contador = 0;
            while (i < opcoes.size()) {
                String opcao = opcoes.get(i).getLabel();

                if ((opcao.equals("")) || (opcao.equals("Usado Em Pagamento")) || (opcao.equals("Pendente")) || (opcao.equals("Devolvido"))) {
                    objs.add(opcoes.get(i));
                    contador++;
                }
                i++;
            }
            setListaTipoSituacao(objs);
        } else {
            setListaTipoSituacao(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoCheque.class));
        }
    }

    public List getListaFiltros() throws Exception {
        List obj = new ArrayList(0);
        obj.add(new SelectItem("Recebidos", "Recebidos"));
        obj.add(new SelectItem("Emitidos", "Emitidos"));

        return obj;
    }

    public List getListaOrdenarPor() throws Exception {
        List obj = new ArrayList(0);
        obj.add(new SelectItem("PR", "Data Previsão"));
        obj.add(new SelectItem("EM", "Data Emissão"));
        return obj;
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            if (resultadoConsulta.isEmpty()) {
                resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
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

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List getListaSelectItemSituacaoCheque() throws Exception {
        return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoCheque.class);
    }

    public void setListaTipoSituacao(List listaTipoSituacao) {
        this.listaTipoSituacao = listaTipoSituacao;
    }

    public List getListaTipoSituacao() {
        return listaTipoSituacao;
    }

    public String getTipoSituacao() {
        if (tipoSituacao == null) {
            tipoSituacao = "";
        }
        return tipoSituacao;
    }

    public void setTipoSituacao(String tipoSituacao) {
        this.tipoSituacao = tipoSituacao;
    }

    public String getTipoFiltro() {
        if (tipoFiltro == null) {
            tipoFiltro = "";
        }
        return tipoFiltro;
    }

    public void setTipoFiltro(String tipoFiltro) {
        this.tipoFiltro = tipoFiltro;
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
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

    public Date getDataInicioPrevisao() {
        if (dataInicioPrevisao == null) {
            dataInicioPrevisao = new Date();
        }
        return dataInicioPrevisao;
    }

    public void setDataInicioPrevisao(Date dataInicioPrevisao) {
        this.dataInicioPrevisao = dataInicioPrevisao;
    }

    public Date getDataFimPrevisao() {
        if (dataFimPrevisao == null) {
            dataFimPrevisao = new Date();
        }
        return dataFimPrevisao;
    }

    public void setDataFimPrevisao(Date dataFimPrevisao) {
        this.dataFimPrevisao = dataFimPrevisao;
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
     * @return the ordenarPor
     */
    public String getOrdenarPor() {
        return ordenarPor;
    }

    /**
     * @param ordenarPor the ordenarPor to set
     */
    public void setOrdenarPor(String ordenarPor) {
        this.ordenarPor = ordenarPor;
    }
}
