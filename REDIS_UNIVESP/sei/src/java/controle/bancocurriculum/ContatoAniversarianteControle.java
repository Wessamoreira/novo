/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.bancocurriculum;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 *
 * @author Philippe
 */
@Controller("ContatoAniversarianteControle")
@Scope("viewScope")
@Lazy
public class ContatoAniversarianteControle extends SuperControleRelatorio {

    private ParceiroVO parceiro;
    private Integer mes;
    private String campoConsultaParceiro;
    private String valorConsultaParceiro;
    private List listaConsultaParceiro;

    public void consultarContatos() {
        getListaConsulta().clear();
        try {
            setListaConsulta(getFacadeFactory().getContatoParceiroFacade().consultarAniversariantesPorMes(getMes(), getParceiro().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
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
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void imprimirPDF() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ContatoAniversarianteControle", "Inicializando Geração de Relatório Contato Aniversariante", "Emitindo Relatório");
            if (!getListaConsulta().isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Contato Aniversariante");
                getSuperParametroRelVO().setListaObjetos(getListaConsulta());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa(getNomeEmpresa());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros(getMes_Apresentar());
//                getSuperParametroRelVO().setDataEmissao(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "ContatoAniversarianteControle", "Inicializando Geração de Relatório Contato Aniversariante", "Emitindo Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(getListaConsulta());
        }
    }

    public void imprimirExcel() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ContatoAniversarianteControle", "Inicializando Geração de Relatório Contato Aniversariante", "Emitindo Relatório");
            if (!getListaConsulta().isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(designIReportRelatorioExcel());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Contato Aniversariante");
                getSuperParametroRelVO().setListaObjetos(getListaConsulta());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa(getNomeEmpresa());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros(getMes_Apresentar());
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "ContatoAniversarianteControle", "Inicializando Geração de Relatório Contato Aniversariante", "Emitindo Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(getListaConsulta());
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

    public List getTipoMes() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("1", "Janeiro"));
        itens.add(new SelectItem("2", "Fevereiro"));
        itens.add(new SelectItem("3", "Março"));
        itens.add(new SelectItem("4", "Abril"));
        itens.add(new SelectItem("5", "Maio"));
        itens.add(new SelectItem("6", "Junho"));
        itens.add(new SelectItem("7", "Julho"));
        itens.add(new SelectItem("8", "Agosto"));
        itens.add(new SelectItem("9", "Setembro"));
        itens.add(new SelectItem("10", "Outubro"));
        itens.add(new SelectItem("11", "Novembro"));
        itens.add(new SelectItem("12", "Dezembro"));
        return itens;
    }

    public String getMes_Apresentar() {
        if (getMes().equals(1)) {
            return "Janeiro";
        }
        if (getMes().equals(2)) {
            return "Fevereiro";
        }
        if (getMes().equals(3)) {
            return "Março";
        }
        if (getMes().equals(4)) {
            return "Abril";
        }
        if (getMes().equals(5)) {
            return "Maio";
        }
        if (getMes().equals(6)) {
            return "Junho";
        }
        if (getMes().equals(7)) {
            return "Julho";
        }
        if (getMes().equals(8)) {
            return "Agosto";
        }
        if (getMes().equals(9)) {
            return "Setembrr";
        }
        if (getMes().equals(10)) {
            return "Outubro";
        }
        if (getMes().equals(11)) {
            return "Novembro";
        }
        if (getMes().equals(12)) {
            return "Dezembro";
        }
        return "";
    }

    public void selecionarParceiro() {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
        setParceiro(obj);
        getListaConsultaParceiro().clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
    }

    public void limparCampoParceiro() {
        setParceiro(new ParceiroVO());
    }

    public Integer getMes() {
        if (mes == null) {
            mes = (new Date().getMonth() + 1);
        }
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
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

    public ParceiroVO getParceiro() {
        if (parceiro == null) {
            parceiro = new ParceiroVO();
        }
        return parceiro;
    }

    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
    }

    public String getNomeEmpresa() {
        if (getParceiro().getNome().equals("")) {
            return "Todos";
        }
        return getParceiro().getNome();
    }

    public String designIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidade() + ".jrxml");
    }

    public String designIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    public String caminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "bancocurriculum" + File.separator);
    }

    public static String getIdEntidade() {
        return "ContatoAniversarianteRel";
    }

    public static String getIdEntidadeExcel() {
        return "ContatoAniversarianteRelExcel";
    }
}
