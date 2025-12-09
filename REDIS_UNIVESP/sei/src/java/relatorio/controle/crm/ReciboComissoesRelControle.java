package relatorio.controle.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import negocio.comuns.administrativo.CargoVO;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.crm.ReciboComissoesRelVO;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("ReciboComissoesRelControle")
@Scope("viewScope")
@Lazy
public class ReciboComissoesRelControle extends SuperControleRelatorio {
    
    private FuncionarioVO funcionario;
    private CargoVO cargo;
    private static String nomeRelatorio;
    protected List listaSelectItemFuncionario;
    protected List listaConsultaFuncionario;
    protected String valorConsultaFuncionario;
    protected String campoConsultaFuncionario;
    protected List listaSelectItemCargo;
    protected List listaConsultaCargo;
    protected String valorConsultaCargo;
    protected String campoConsultaCargo;
    private String valorConsultaMes;

    public ReciboComissoesRelControle() throws Exception {
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirPDF() {
        List<ReciboComissoesRelVO> listaObjetos = null;
        try {
            listaObjetos = getFacadeFactory().getReciboComissoesRelFacade().criarObjeto(getValorConsultaMes(), getFuncionario().getPessoa().getCodigo(), getCargo().getCodigo(), getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getReciboComissoesRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getReciboComissoesRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio(UteisJSF.internacionalizar("prt_RelatorioReciboComissoes_tituloForm"));
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getReciboComissoesRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);

        }
    }

    public void consultarFuncionario() {
        try {
            setListaConsultaFuncionario(getFacadeFactory().getFuncionarioFacade().consultar(getValorConsultaFuncionario(), getCampoConsultaFuncionario(), true,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", UteisJSF.internacionalizar("prt_Funcionario_nome")));
        itens.add(new SelectItem("matricula", UteisJSF.internacionalizar("prt_Funcionario_matricula")));
        itens.add(new SelectItem("CPF", UteisJSF.internacionalizar("prt_Funcionario_CPF")));
        itens.add(new SelectItem("cargo", UteisJSF.internacionalizar("prt_Funcionario_cargo")));
        itens.add(new SelectItem("departamento", UteisJSF.internacionalizar("prt_Funcionario_departamento")));
        return itens;
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        setFuncionario(obj);
        setCampoConsultaFuncionario("");
        setValorConsultaFuncionario("");
        setListaConsultaFuncionario(new ArrayList(0));

    }

    public void limparDadosFuncionario() {
        setFuncionario(new FuncionarioVO());
    }

    public String getCampoConsultaFuncionario() {
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List getListaConsultaFuncionario() {
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public List getListaSelectItemFuncionario() {
        return listaSelectItemFuncionario;
    }

    public void setListaSelectItemFuncionario(List listaSelectItemFuncionario) {
        this.listaSelectItemFuncionario = listaSelectItemFuncionario;
    }

    public String getValorConsultaFuncionario() {
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }


    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public void setNomeRelatorio(String aNomeRelatorio) {
        nomeRelatorio = aNomeRelatorio;
    }
    
    public String getCampoConsultaCargo() {
        if (campoConsultaCargo == null) {
            campoConsultaCargo = "";
        }
        return campoConsultaCargo;
    }

    public void setCampoConsultaCargo(String campoConsultaCargo) {
        this.campoConsultaCargo = campoConsultaCargo;
    }

    public List getListaConsultaCargo() {
        if (listaConsultaCargo == null) {
            listaConsultaCargo = new ArrayList(0);
        }
        return listaConsultaCargo;
    }

    public void setListaConsultaCargo(List listaConsultaCargo) {
        this.listaConsultaCargo = listaConsultaCargo;
    }

    public List getListaSelectItemCargo() {
        if (listaSelectItemCargo == null) {
            listaSelectItemCargo = new ArrayList(0);
        }
        return listaSelectItemCargo;
    }

    public void setListaSelectItemCargo(List listaSelectItemCargo) {
        this.listaSelectItemCargo = listaSelectItemCargo;
    }

    public String getValorConsultaCargo() {
        if (valorConsultaCargo == null) {
            valorConsultaCargo = "";
        }
        return valorConsultaCargo;
    }

    public void setValorConsultaCargo(String valorConsultaCargo) {
        this.valorConsultaCargo = valorConsultaCargo;
    }

    public void consultarCargo() {
        try {
            setListaConsultaCargo(getFacadeFactory().getCargoFacade().consultar(getValorConsultaCargo(), getCampoConsultaCargo(), true,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCargo(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboCargo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", UteisJSF.internacionalizar("prt_Cargo_nome")));
        itens.add(new SelectItem("descricao", UteisJSF.internacionalizar("prt_Cargo_descricao")));
        return itens;
    }

    public void selecionarCargo() {
        CargoVO obj = (CargoVO) context().getExternalContext().getRequestMap().get("cargoItens");
        setCargo(obj);
        setCampoConsultaCargo("");
        setValorConsultaCargo("");
        setListaConsultaCargo(new ArrayList(0));

    }

    public void limparDadosCargo() {
        setCargo(new CargoVO());
    }

    public String getValorConsultaMes() {
        return valorConsultaMes;
    }

    public void setValorConsultaMes(String valorConsultaMes) {
        this.valorConsultaMes = valorConsultaMes;
    }

    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }

    public CargoVO getCargo() {
        if (cargo == null) {
            cargo = new CargoVO();
        }
        return cargo;
    }

    public void setCargo(CargoVO cargo) {
        this.cargo = cargo;
    }

}
