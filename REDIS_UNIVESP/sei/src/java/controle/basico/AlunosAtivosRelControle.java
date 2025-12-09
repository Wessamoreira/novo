package controle.basico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.basico.AlunosAtivosRelVO; 
@Controller("AlunosAtivosRelControle")
@Scope("viewScope")
@Lazy
public class AlunosAtivosRelControle extends SuperControleRelatorio implements Serializable {

	private Date data;
    private Integer quantidadeAlunos;
    private Boolean isApresentarBotaoRelatorioDetalhado;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private String tipoRelatorio;

    public AlunosAtivosRelControle() throws Exception {
        //obterUsuarioLogado();
        inicializarDados();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void inicializarDados() {
        montarListaSelectItemUnidadeEnsino();
        setIsApresentarBotaoRelatorioDetalhado(null);
        setData(null);
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void realizarImpressaoPDF() {
        List<AlunosAtivosRelVO> lista;
        try {
            getFacadeFactory().getAlunosAtivosRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo());
            if (this.getTipoRelatorio().equals("analitico")) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosAtivosRelFacade().designIReportRelatorio());
            } else {
                if (getUnidadeEnsinoVO().getCodigo() == null || getUnidadeEnsinoVO().getCodigo() == 0) {
                    throw new Exception("Para Emissão do Relatório Sintético é necessário informar a Unidade de Ensino.");
                }
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosAtivosRelFacade().designIReportRelatorioSintetico());
            }
            lista = getFacadeFactory().getAlunosAtivosRelFacade().criarObjeto(getData(), tipoRelatorio, getUnidadeEnsinoVO().getCodigo());
            if (!lista.isEmpty()) {
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosAtivosRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio(" Relatório de Alunos Ativos");
                getSuperParametroRelVO().setListaObjetos(lista);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosAtivosRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                String tipoRelatorio;
                if (getTipoRelatorio().equals("sintetico")) {
                    tipoRelatorio = "Sintético";
                } else {
                    tipoRelatorio = "Analítico";
                }
                getSuperParametroRelVO().setTipoRelatorio(tipoRelatorio);
                getSuperParametroRelVO().setUnidadeEnsino(
                    (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                realizarImpressaoRelatorio();
//                removerObjetoMemoria(this);                
//                inicializarDados();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            lista = null;
        }
    }

    public void realizarImpressaoExcel() {    
        List<AlunosAtivosRelVO> lista;
        try {
            getFacadeFactory().getAlunosAtivosRelFacade().validarDados(getUnidadeEnsinoVO().getCodigo());
            lista = getFacadeFactory().getAlunosAtivosRelFacade().criarObjeto(getData(), this.getTipoRelatorio(), getUnidadeEnsinoVO().getCodigo());
            if (!lista.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAlunosAtivosRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getAlunosAtivosRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Alunos Ativos");
                getSuperParametroRelVO().setListaObjetos(lista);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAlunosAtivosRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                String tipoRelatorio;
                if (getTipoRelatorio().equals("sintetico")) {
                    tipoRelatorio = "Sintético";
                } else {
                    tipoRelatorio = "Analítico";
                }
                getSuperParametroRelVO().setTipoRelatorio(tipoRelatorio);
                getSuperParametroRelVO().setUnidadeEnsino(
                    (getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                realizarImpressaoRelatorio();
//                removerObjetoMemoria(this);
//                inicializarDados();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            lista = null;
            
        }
    }

    public List<SelectItem> getListaTipoRelatorio() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("sintetico", "Sintético"));
        itens.add(new SelectItem("analitico", "Analítico"));
        return itens;
    }

    public String realizarImpressaoEmTela() {
        try {
            setQuantidadeAlunos(getFacadeFactory().getAlunosAtivosRelFacade().executarConsultaQuantidadeAlunosAtivos(getData()));
            setIsApresentarBotaoRelatorioDetalhado(true);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public void selecionarTipoAluno() {
        if (this.getTipoRelatorio().equals("analitico")) {
            setIsApresentarBotaoRelatorioDetalhado(true);
        } else {
            setIsApresentarBotaoRelatorioDetalhado(false);
        }
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public Integer getQuantidadeAlunos() {
    	if(this.quantidadeAlunos == null){
    		this.quantidadeAlunos = 0;
    	}
        return this.quantidadeAlunos;
    }

    public void setQuantidadeAlunos(Integer quantidadeAlunos) {
        this.quantidadeAlunos = quantidadeAlunos;
    }

    public boolean getIsApresentarBotaoRelatorioPDF() {
        if (this.getTipoRelatorio().equals("sintetico") && getUnidadeEnsinoVO().getCodigo() != null && getUnidadeEnsinoVO().getCodigo() != 0) {
            return true;
        } else if (this.getTipoRelatorio().equals("sintetico") && (getUnidadeEnsinoVO().getCodigo() == null || getUnidadeEnsinoVO().getCodigo() == 0)) {
            return false;
        } else if (this.getTipoRelatorio().equals("analitico")) {
            return true;
        }
        return false;
    }

    public Boolean getIsApresentarBotaoRelatorioDetalhado() {
        if(isApresentarBotaoRelatorioDetalhado == null){
            return false;
        }
        return isApresentarBotaoRelatorioDetalhado;
    }

    public void setIsApresentarBotaoRelatorioDetalhado(Boolean isApresentarBotaoRelatorioDetalhado) {
        this.isApresentarBotaoRelatorioDetalhado = isApresentarBotaoRelatorioDetalhado;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    /**
     * @return the unidadeEnsinoVO
     */
    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    /**
     * @param unidadeEnsinoVO the unidadeEnsinoVO to set
     */
    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
    	if(this.unidadeEnsinoVO == null){
    		this.unidadeEnsinoVO = new UnidadeEnsinoVO();
    	}
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    /**
     * @return the tipoRelatorio
     */
    public String getTipoRelatorio() {
        if (tipoRelatorio == null) {
            tipoRelatorio = "sintetico";
        }
        return tipoRelatorio;
    }

    /**
     * @param tipoRelatorio the tipoRelatorio to set
     */
    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
    }
}
