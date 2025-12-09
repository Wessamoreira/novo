package relatorio.controle.bancocurriculum;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 *
 * @author rogerio.gomes
 */
@Controller("EmpresaPorVagasRelControle")
@Scope("viewScope")
@Lazy
public class EmpresaPorVagasRelControle extends SuperControleRelatorio {

    private VagasVO vagasVO;
    private ParceiroVO parceiroVO;
    private Date dataInicio;
    private Date dataFim;
    private String campoConsultaParceiro;
    private String valorConsultaParceiro;
    private List listaConsultaParceiro;
    private String campoConsultaVagas;
    private String valorConsultaVagas;
    private List listaConsultaVagas;
    private String situacaoVaga;
    private Boolean contratados = false;

    public void imprimirPDF() {
        List empresaPorVagasRelVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaPorVagasRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
            empresaPorVagasRelVOs = getFacadeFactory().getEmpresaPorVagasRelFacade().criarObjeto(getVagasVO(), getParceiroVO(), getDataInicio(), getDataFim(), getSituacaoVaga(), getContratados());
            if (!empresaPorVagasRelVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getEmpresaPorVagasRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getEmpresaPorVagasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setTituloRelatorio("Relatório Empresas x  Vagas");
                getSuperParametroRelVO().setListaObjetos(empresaPorVagasRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getEmpresaPorVagasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setDataEmissao(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaPorVagasRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(empresaPorVagasRelVOs);
        }
    }

    public void imprimirExcel() {
        List empresaPorVagasRelVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaPorVagasRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
            empresaPorVagasRelVOs = getFacadeFactory().getEmpresaPorVagasRelFacade().criarObjeto(getVagasVO(), getParceiroVO(), getDataInicio(), getDataFim(), getSituacaoVaga(), getContratados());
            if (!empresaPorVagasRelVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getEmpresaPorVagasRelFacade().designIReportRelatorioExcel());
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getEmpresaPorVagasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setTituloRelatorio("Relatório Empresas x  Vagas");
                getSuperParametroRelVO().setListaObjetos(empresaPorVagasRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getEmpresaPorVagasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setDataEmissao(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaPorVagasRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(empresaPorVagasRelVOs);
        }
    }

    public void consultarParceiro() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaParceiro().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNomeBancoCurriculumTrue(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocialBancoCurriculumTrue(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("RG")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRGBancoCurriculumTrue(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("CPF")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCPFBancoCurriculumTrue(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("CNPJ")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCNPJBancoCurriculumTrue(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaParceiro(objs);
            setMensagemID(
                    "msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaParceiro() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão social"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("CNPJ", "CNPJ"));
        return itens;
    }

    public void selecionarParceiro() {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
        setParceiroVO(obj);
        listaConsultaParceiro.clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
    }

    public void consultarVagas() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaVagas().equals("numeroVagas")) {
                int valorInt = Integer.parseInt(getValorConsultaVagas());
                objs = getFacadeFactory().getVagasFacade().consultarPorNumeroVagas(new Integer(valorInt), "", "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaVagas().equals("cargo")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorCargo(getControleConsulta().getValorConsulta(), "", "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaVagas().equals("parceiro")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorParceiro(getControleConsulta().getValorConsulta(), "", "", true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaVagas().equals("areaProfissional")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorAreaProfissional(getControleConsulta().getValorConsulta(), "", "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaVagas().equals("salario")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSalario(getControleConsulta().getValorConsulta(), "", "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaVagas().equals("situacao")) {
                objs = getFacadeFactory().getVagasFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), "", null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaVagas(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaVagas() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("parceiro", "Parceiro"));
        itens.add(new SelectItem("numeroVagas", "Número de Vagas"));
        itens.add(new SelectItem("salario", "Salário"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("situacao", "Situação"));
        return itens;
    }

    public void selecionarVagas() {
        VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
        setVagasVO(obj);
        listaConsultaParceiro.clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
    }

    public List getListaSelectitemSituacaoVagas() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("EC", "Em Construção"));
        itens.add(new SelectItem("AT", "Ativa"));
        itens.add(new SelectItem("CA", "Cancelada"));
        itens.add(new SelectItem("EN", "Encerrada"));
        itens.add(new SelectItem("EX", "Expirada"));
        return itens;
    }
    
    public void limparCampoParceiro(){
        setParceiroVO(new ParceiroVO());    
    }
    public void limparCampoVaga(){
        setVagasVO(new VagasVO());    
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getDataUltimoDiaMes(new Date());
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getSituacaoVaga() {
        if (situacaoVaga == null) {
            situacaoVaga = "";
        }
        return situacaoVaga;
    }

    public void setSituacaoVaga(String situacaoVaga) {
        this.situacaoVaga = situacaoVaga;
    }

    public Boolean getContratados() {
        return contratados;
    }

    public void setContratados(Boolean contratados) {
        this.contratados = contratados;
    }

    public VagasVO getVagasVO() {
        if (vagasVO == null) {
            vagasVO = new VagasVO();
        }
        return vagasVO;
    }

    public void setVagasVO(VagasVO vagasVO) {
        this.vagasVO = vagasVO;
    }

    public ParceiroVO getParceiroVO() {
        if (parceiroVO == null) {
            parceiroVO = new ParceiroVO();
        }
        return parceiroVO;
    }

    public void setParceiroVO(ParceiroVO parceiroVO) {
        this.parceiroVO = parceiroVO;
    }

    public String getCampoConsultaParceiro() {
        if (campoConsultaParceiro == null) {
            campoConsultaParceiro = "";
        }
        return campoConsultaParceiro;
    }

    public void setCampoConsultaParceiro(String campoConsultaParceiro) {
        this.campoConsultaParceiro = campoConsultaParceiro;
    }

    public String getValorConsultaParceiro() {
        if (valorConsultaParceiro == null) {
            valorConsultaParceiro = "";
        }
        return valorConsultaParceiro;
    }

    public void setValorConsultaParceiro(String valorConsultaParceiro) {
        this.valorConsultaParceiro = valorConsultaParceiro;
    }

    public List getListaConsultaParceiro() {
        if (listaConsultaParceiro == null) {
            listaConsultaParceiro = new ArrayList(0);
        }
        return listaConsultaParceiro;
    }

    public void setListaConsultaParceiro(List listaConsultaParceiro) {
        this.listaConsultaParceiro = listaConsultaParceiro;
    }

    public String getCampoConsultaVagas() {
        if (campoConsultaVagas == null) {
            campoConsultaVagas = "";
        }
        return campoConsultaVagas;
    }

    public void setCampoConsultaVagas(String campoConsultaVagas) {
        this.campoConsultaVagas = campoConsultaVagas;
    }

    public String getValorConsultaVagas() {
        if (valorConsultaVagas == null) {
            valorConsultaVagas = "";
        }
        return valorConsultaVagas;
    }

    public void setValorConsultaVagas(String valorConsultaVagas) {
        this.valorConsultaVagas = valorConsultaVagas;
    }

    public List getListaConsultaVagas() {
        if (listaConsultaVagas == null) {
            listaConsultaVagas = new ArrayList(0);
        }
        return listaConsultaVagas;
    }

    public void setListaConsultaVagas(List listaConsultaVagas) {
        this.listaConsultaVagas = listaConsultaVagas;
    }
}
