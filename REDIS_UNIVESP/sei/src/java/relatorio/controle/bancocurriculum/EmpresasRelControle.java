/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.bancocurriculum;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 *
 * @author Philippe
 */
@Controller("EmpresasRelControle")
@Scope("viewScope")
@Lazy
public class EmpresasRelControle extends SuperControleRelatorio {

    private ParceiroVO parceiroVO;
    private String campoConsultaParceiro;
    private String valorConsultaParceiro;
    private List listaConsultaParceiro;
    private EstadoVO estadoVO;
    private CidadeVO cidadeVO;
    private String campoConsultaCidade;
    private String valorConsultaCidade;
    private List listaConsultaCidade;
    private List listaSelectItemEstado;
    private String ordenarPor;
    private String tipoConsulta;
    private Date dataInicio;
    private Date dataFim;
    

    public EmpresasRelControle() {
        montarListaSelectItemEstado();
    }
    
    public void imprimirPDF() {
        List empresasRelVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EmpresasRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
            empresasRelVOs = getFacadeFactory().getEmpresasRelFacade().criarObjeto(getParceiroVO(), getCidadeVO(), getEstadoVO(), getOrdenarPor(), getTipoConsulta(), getDataInicio(), getDataFim());
            if (!empresasRelVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getEmpresasRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getEmpresasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Empresas");
                getSuperParametroRelVO().setListaObjetos(empresasRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getEmpresasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setDataInicio(Uteis.getDataAno4Digitos(dataInicio));
                getSuperParametroRelVO().setDataFim(Uteis.getDataAno4Digitos(dataFim));
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                getSuperParametroRelVO().adicionarParametro("tipoConsulta", getTipoConsulta());
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "EmpresasRelControle", "Finalizando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(empresasRelVOs);
        }
    }

    public void imprimirExcel() {
        List empresasRelVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EmpresasRelControle", "Inicializando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
            empresasRelVOs = getFacadeFactory().getEmpresasRelFacade().criarObjeto(getParceiroVO(), getCidadeVO(), getEstadoVO(), getOrdenarPor(), getTipoConsulta(), getDataInicio(), getDataFim());
            if (!empresasRelVOs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getEmpresasRelFacade().designIReportRelatorioExcel());
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getEmpresasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setTituloRelatorio("Relatório Empresas");
                getSuperParametroRelVO().setListaObjetos(empresasRelVOs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getEmpresasRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setDataInicio(dataInicio.toString());
                getSuperParametroRelVO().setDataFim(dataFim.toString());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                realizarImpressaoRelatorio();
                registrarAtividadeUsuario(getUsuarioLogado(), "EmpresasRelControle", "Finalizando Geração de Relátorio Empresa Por Vagas", "Gerar Relatório");
                setMensagemID("msg_relatorio_ok");
            } else {
                setUsarTargetBlank("");
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception ex) {
            setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(empresasRelVOs);
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

    public List getSelectItemOrdenarPor() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome Parceiro"));
        itens.add(new SelectItem("dataUltimoAcesso", "Data Último Acesso"));
        return itens;
    }

    public void selecionarParceiro() {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
        setParceiroVO(obj);
        getListaConsultaParceiro().clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
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

    public List consultarEstadoPorSigla(String prm) throws Exception {
//        List lista = getFacadeFactory().getEstadoFacade().consultarPorSigla(prm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
    	List lista = getFacadeFactory().getEstadoFacade().consultarPorCodigoPaiz(getConfiguracaoGeralPadraoSistema().getPaisPadrao().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void consultarCidade() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                if (getEstadoVO().getCodigo() == 0) {
                    objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
                } else {
                    objs = getFacadeFactory().getCidadeFacade().consultarPorNomeCodigoEstado(getValorConsultaCidade(), false, getEstadoVO().getCodigo(), getUsuarioLogado());
                }
            }
            setListaConsultaCidade(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        setCidadeVO(obj);
        setEstadoVO(obj.getEstado());
        getListaConsultaCidade().clear();
        this.setValorConsultaCidade("");
        this.setCampoConsultaCidade("");
    }

    public List getTipoConsultaCidade() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public List getTipoParametroConsulta() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("TODOS", "Todos"));
        itens.add(new SelectItem("CADASTRADOS_NO_PERIODO", "Cadastrados no Período"));
        itens.add(new SelectItem("ACESSARAM_NO_PERIODO", "Acessaram no Período"));
        itens.add(new SelectItem("CADASTRARAM_VAGAS_NO_PERIODO", "Cadastraram vagas no Período"));
        return itens;
    }    

    public void limparCampoParceiro() {
        setParceiroVO(new ParceiroVO());
    }

    public void limparDadosCidade() {
        setCidadeVO(new CidadeVO());
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

    public EstadoVO getEstadoVO() {
        if (estadoVO == null) {
            estadoVO = new EstadoVO();
        }
        return estadoVO;
    }

    public void setEstadoVO(EstadoVO estadoVO) {
        this.estadoVO = estadoVO;
    }

    public CidadeVO getCidadeVO() {
        if (cidadeVO == null) {
            cidadeVO = new CidadeVO();
        }
        return cidadeVO;
    }

    public void setCidadeVO(CidadeVO cidadeVO) {
        this.cidadeVO = cidadeVO;
    }

    public String getCampoConsultaCidade() {
        if (campoConsultaCidade == null) {
            campoConsultaCidade = "";
        }
        return campoConsultaCidade;
    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    public String getValorConsultaCidade() {
        if (valorConsultaCidade == null) {
            valorConsultaCidade = "";
        }
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    public List getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList(0);
        }
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
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

    /**
     * @return the ordenarPor
     */
    public String getOrdenarPor() {
        if (ordenarPor == null) {
            ordenarPor = "nome";
        }
        return ordenarPor;
    }

    /**
     * @param ordenarPor the ordenarPor to set
     */
    public void setOrdenarPor(String ordenarPor) {
        this.ordenarPor = ordenarPor;
    }
    
    public String getTipoConsulta() {
    	
    	if(tipoConsulta == null){
    		tipoConsulta = "TODOS";
    	}
    	
		return tipoConsulta;
	}
    
    public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
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
		if(dataFim == null){
			dataFim = Uteis.getDataUltimoDiaMes(new Date());
			
		}
		
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public Boolean getIsApresentarDataPeriodo() {
		
		return !getTipoConsulta().equals("TODOS");
	}



    
}
