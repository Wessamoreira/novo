package relatorio.controle.bancocurriculum;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.financeiro.ParceiroVO;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoCheque;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.bancocurriculum.EmpresaBancoTalentoRelVO;

@Controller("EmpresaVagaBancoTalentoRelControle")
@Scope("viewScope")
@Lazy
public class EmpresaVagaBancoTalentoRelControle extends SuperControleRelatorio {

    private VagasVO vagasVO;
    private ParceiroVO parceiroVO;
    private Date dataInicio;
    private Date dataFim;
    private String situacaoVaga;
    private String campoConsultaParceiro;
    private String valorConsultaParceiro;
    private List listaConsultaParceiro;
    private String campoConsultaVagas;
    private String valorConsultaVagas;
    private List listaConsultaVagas;
    private List listaSelectItemEstado;
    private List listaSelectItemCidade;
    private List listaSelectItemAreaProfissional;
    private String ordenacao;

    //
    public EmpresaVagaBancoTalentoRelControle() throws Exception {
        //obterUsuarioLogado();
        montarListaSelectItemAreaProfissional();
        montarListaSelectItemEstado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<EmpresaBancoTalentoRelVO> listaObjetos = new ArrayList();
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaVagaBancoTalentoRelControle", "Inicializando Geração de Relatório Empresa Vaga Banco de Talentos", "Emitindo Relatório");
            listaObjetos = getFacadeFactory().getVagasBancoTalentoRelFacade().criarObjetoPDF(listaObjetos, getVagasVO(), getParceiroVO(), getDataInicio(), getDataFim(), getSituacaoVaga(), getUsuarioLogado(), getOrdenacao());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getVagasBancoTalentoRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getVagasBancoTalentoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Vagas");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getVagasBancoTalentoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setDataEmissao(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaVagaBancoTalentoRelControle", "Inicializando Geração de Relatório Empresa Vaga Banco de Talentos", "Emitindo Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void imprimirExcel() {
        List<EmpresaBancoTalentoRelVO> listaObjetos = new ArrayList();
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaVagaBancoTalentoRelControle", "Inicializando Geração de Relatório Empresa Vaga Banco de Talentos", "Emitindo Relatório");
            listaObjetos = getFacadeFactory().getVagasBancoTalentoRelFacade().criarObjetoExcel(listaObjetos, getVagasVO(), getParceiroVO(), getDataInicio(), getDataFim(), getSituacaoVaga(), getUsuarioLogado(), getOrdenacao());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getVagasBancoTalentoRelFacade().designIReportRelatorioExcel());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getVagasBancoTalentoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Vagas");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getVagasBancoTalentoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().setDataEmissao(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "EmpresaVagaBancoTalentoRelControle", "Inicializando Geração de Relatório Empresa Vaga Banco de Talentos", "Emitindo Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
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

    public List getTipoOrdenacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("data", "Data de Ativação da Vaga"));
        itens.add(new SelectItem("empresa", "Nome da Empresa"));
        itens.add(new SelectItem("areaProfissional", "Área Profissional"));
        itens.add(new SelectItem("estado", "Estado"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("codigoVaga", "Código da Vaga"));
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

    public List getListaSelectitemSituacaoVagas() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("AT", "Aberta"));
        itens.add(new SelectItem("EN", "Encerrada"));
        itens.add(new SelectItem("EX", "Expirada"));
        return itens;
    }

    public void limparCampoParceiro() {
        setParceiroVO(new ParceiroVO());
    }

    public void limparCampoVaga() {
        setVagasVO(new VagasVO());
    }

    public void montarListaSelectItemEstado() {
        try {
            montarListaSelectItemEstado("");
        } catch (Exception e) {
           // //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemEstado(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarEstadoPorSigla(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                EstadoVO obj = (EstadoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemEstado(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemAreaProfissional() {
        try {
            montarListaSelectItemAreaProfissional("");
        } catch (Exception e) {
          //  //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemAreaProfissional(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarAreaProfissionalAtiva(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                AreaProfissionalVO obj = (AreaProfissionalVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getDescricaoAreaProfissional()));
            }
            setListaSelectItemAreaProfissional(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarEstadoPorSigla(String prm) throws Exception {
        List lista = getFacadeFactory().getEstadoFacade().consultarPorCodigoPaiz(getConfiguracaoGeralPadraoSistema().getPaisPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
        return lista;
    }

    public List consultarAreaProfissionalAtiva(String prm) throws Exception {
        List lista = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissionalAtivo(prm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCidade() {
        try {
            montarListaSelectItemCidade("");
        } catch (Exception e) {
          //  //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemCidade(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCidadePorEstado(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CidadeVO obj = (CidadeVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemCidade(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarCidadePorEstado(String prm) throws Exception {
        List lista = null;
//        List lista = getFacadeFactory().getCidadeFacade().consultarPorCodigoEstado(getVagasVO().getEstado().getCodigo(), false, getUsuarioLogado());
        return lista;
    }

    public void selecionarVagas() {
        VagasVO obj = (VagasVO) context().getExternalContext().getRequestMap().get("vagasItens");
        setVagasVO(obj);
        listaConsultaParceiro.clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
    }

    public List getListaSelectItemSituacaoCheque() throws Exception {
        return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoCheque.class);
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

    public List getListaSelectItemEstado() {
        if (listaSelectItemEstado == null) {
            listaSelectItemEstado = new ArrayList(0);
        }
        return listaSelectItemEstado;
    }

    public void setListaSelectItemEstado(List listaSelectItemEstado) {
        this.listaSelectItemEstado = listaSelectItemEstado;
    }

    public List getListaSelectItemCidade() {
        if (listaSelectItemCidade == null) {
            listaSelectItemCidade = new ArrayList(0);
        }
        return listaSelectItemCidade;
    }

    public void setListaSelectItemCidade(List listaSelectItemCidade) {
        this.listaSelectItemCidade = listaSelectItemCidade;
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

    public String getOrdenacao() {
        if (ordenacao == null) {
            ordenacao = "";
        }
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }

    /**
     * @return the listaSelectItemAreaProfissional
     */
    public List getListaSelectItemAreaProfissional() {
        return listaSelectItemAreaProfissional;
    }

    /**
     * @param listaSelectItemAreaProfissional the listaSelectItemAreaProfissional to set
     */
    public void setListaSelectItemAreaProfissional(List listaSelectItemAreaProfissional) {
        this.listaSelectItemAreaProfissional = listaSelectItemAreaProfissional;
    }
}
