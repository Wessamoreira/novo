package controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;

@Controller("MapaRecebimentoContaReceberDuplicidadeControle")
@Lazy
@Scope("viewScope")
public class MapaRecebimentoContaReceberDuplicidadeControle extends SuperControle {

    /**
     *
     */
    private static final long serialVersionUID = 2808877395975656890L;
    private List<ContaReceberVO> contaReceberVOs;
    private ContaReceberVO contaReceberVO;
    private Integer ano;
    private Integer mes;
    private String situacao;
    private Integer unidadeEnsino;
    private List<SelectItem> listaSelectItemMes;
    private List<SelectItem> listaSelectItemAno;
    private List<SelectItem> listaSelectItemUnidadeEnsino;

    @PostConstruct
    public void consultarRecebimentoContaReceberDuplicidade() {
        try {
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("nossoNumero")) {
                objs = getFacadeFactory().getContaReceberFacade().consultaContaReceberRecebidaEmDuplicidadePorNossoNumero(getControleConsulta().getValorConsulta());
            } else if (getControleConsulta().getCampoConsulta().equals("dataVcto")) {
                objs = getFacadeFactory().getContaReceberFacade().consultaContaReceberRecebidaEmDuplicidadePorPeriodoDataVcto(getControleConsulta().getDataIni(), getControleConsulta().getDataFim());
            } else if (getControleConsulta().getCampoConsulta().equals("tratada")) {
                objs = getFacadeFactory().getContaReceberFacade().consultaContaReceberRecebidaEmDuplicidadePorTratada(Boolean.TRUE);
            } else if (getControleConsulta().getCampoConsulta().equals("naoTratada")) {
                objs = getFacadeFactory().getContaReceberFacade().consultaContaReceberRecebidaEmDuplicidadePorTratada(Boolean.FALSE);
            }
            setContaReceberVOs(objs);
            //setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultaContaReceberRecebidaEmDuplicidade(getUnidadeEnsino(), getAno(), getMes(), getSituacao()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    public void visualizarRecebimentoDuplicidade() {
        try {
            setContaReceberVO(new ContaReceberVO());
            setContaReceberVO((ContaReceberVO) getRequestMap().get("contaReceberItem"));
            if (getContaReceberVO().getContaReceberHistoricoVOs().isEmpty()) {
                getContaReceberVO().setContaReceberHistoricoVOs(getFacadeFactory().getContaReceberHistoricoFacade().consultarPorCodigoContaReceber(getContaReceberVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            limparMensagem();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void registrarTradaContaReceber() {
        try {
            ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceberItem");
			getFacadeFactory().getContaReceberFacade().alterarSituacaoSuplicidadeTratada(obj.getCodigo(), obj.getDuplicidadeTratada(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public List<ContaReceberVO> getContaReceberVOs() {
        if (contaReceberVOs == null) {
            contaReceberVOs = new ArrayList<ContaReceberVO>(0);
        }
        return contaReceberVOs;
    }

    public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
        this.contaReceberVOs = contaReceberVOs;
    }

    public ContaReceberVO getContaReceberVO() {
        if (contaReceberVO == null) {
            contaReceberVO = new ContaReceberVO();
        }
        return contaReceberVO;
    }

    public void setContaReceberVO(ContaReceberVO contaReceberVO) {
        this.contaReceberVO = contaReceberVO;
    }

    public Integer getAno() {
        if (ano == null) {
            ano = Integer.valueOf(Uteis.getAnoDataAtual4Digitos());
        }
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        if (mes == null) {
            mes = Uteis.getMesDataAtual();
        }
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public void montarListaSelectItemMes() {
        listaSelectItemMes = new ArrayList<SelectItem>(0);
        int maiorMes = 12;
        if (getAno().equals(Integer.valueOf(Uteis.getAnoDataAtual4Digitos()))) {
            maiorMes = Uteis.getMesDataAtual();
        }
        setMes(Uteis.getMesDataAtual());
        for (int x = maiorMes; x >= 1; x--) {
            listaSelectItemMes.add(new SelectItem(x, MesAnoEnum.getEnum(String.valueOf(x)).getMes()));
        }
    }

    public List<SelectItem> getListaSelectItemMes() {
        if (listaSelectItemMes == null) {
            montarListaSelectItemMes();
        }
        return listaSelectItemMes;
    }

    public void setListaSelectItemMes(List<SelectItem> listaSelectItemMes) {
        this.listaSelectItemMes = listaSelectItemMes;
    }

    public List<SelectItem> getListaSelectItemSituacao() {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        lista.add(new SelectItem("TO", "Todas"));
        lista.add(new SelectItem("TR", "Tratadas"));
        lista.add(new SelectItem("NT", "Não Tratadas"));
        return lista;
    }

    public List<SelectItem> getListaSelectItemAno() {
        if (listaSelectItemAno == null) {
            listaSelectItemAno = new ArrayList<SelectItem>();
            for (int x = Integer.valueOf(Uteis.getAnoDataAtual4Digitos()); x >= 2008; x--) {
                listaSelectItemAno.add(new SelectItem(x, String.valueOf(x)));
            }
        }
        return listaSelectItemAno;
    }

    public void setListaSelectItemAno(List<SelectItem> listaSelectItemAno) {
        this.listaSelectItemAno = listaSelectItemAno;
    }

    public Integer getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
            setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
            if (getUnidadeEnsinoLogado().getCodigo() > 0) {
                listaSelectItemUnidadeEnsino.add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
            } else {
                List<UnidadeEnsinoVO> unidadeEnsinoVOs;
                try {
                    unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                    listaSelectItemUnidadeEnsino.add(new SelectItem(0, "Todas"));
                    for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                        listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
                    }
                } catch (Exception e) {
                    setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
                }

            }
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        if (situacao == null) {
            situacao = "TO";
        }
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Boolean getIsConsultaDataVcto() {
        if (getControleConsulta().getCampoConsulta().equals("dataVcto")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public List getTipoComboConsulta() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("naoTratada", "Não tratadas"));
        objs.add(new SelectItem("tratada", "Tratadas"));
        objs.add(new SelectItem("nossoNumero", "Nosso Número"));
        objs.add(new SelectItem("dataVcto", "Data de Vencimento"));
        return objs;
    }

}
