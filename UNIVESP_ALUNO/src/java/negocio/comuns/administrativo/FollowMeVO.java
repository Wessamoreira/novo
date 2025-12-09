package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;


public class FollowMeVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = -6917889429923535298L;
    
    private Integer codigo;
    private String descricao;
    private List<FollowMeGrupoDestinatarioVO> followMeGrupoDestinatarioVOs;
    private List<FollowMeUnidadeEnsinoVO> followMeUnidadeEnsinoVOs;
    private List<FollowMeCategoriaDespesaVO> followMeCategoriaDespesaVOs;
    private List<FollowMeDepartamentoVO> followMeDepartamentoVOs;
    private Integer periodoBase;
    private Integer periodoBaseComparativo;    
    private Boolean apresentarDadosTodasUnidades;       
    /**
     * Informações Financeiras
     */
    private Boolean gastoPorDepartamento;
    private Boolean gastoPorDepartamentoAnoAnterior;
    private Boolean gerarGraficoDepartamentoMesAMes;
    private Boolean apresentarDespesaDepartamentoNaoInformado;
    
    private Boolean receitaDespesa;
    private Boolean receitaDespesaAnoAnterior;
    private Boolean receitaDespesaPorNivelEducacional;
    private Boolean receitaDespesaPorNivelEducacionalAnoAnterior;
    private Boolean receitaDespesaPorNivelEducacionalMesMes;
    private Boolean posicaoFinanceira;
    
    private Boolean inadimplencia;
    
    private Boolean gerarGraficoDespesa; 
    private Boolean gerarGraficoDespesaAnoAnterior;
    
    private Boolean gerarGraficoCategoriaDespesa;    
    private Boolean gerarGraficoCategoriaDespesaMesAMes;
    
    private Boolean gerarListaCategoriaDespesaPadraoAlterado;
    private Double percentualCategoriaDespesaPadraoAlterado;
    private Boolean gerarListaAcademicoFinanceiro;
    private Boolean apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro;
    private Boolean apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro;
    private Boolean apresentarInadimplenciaRelatorioAcademicoFinanceiro;
    private Boolean apresentarQtdeAlunoRelatorioAcademicoFinanceiro;
    private Boolean apresentarQtdeTurmaRelatorioAcademicoFinanceiro;
    private Boolean apresentarDespesaRelatorioAcademicoFinanceiro;
    
    
    
    public FollowMeVO clone() throws CloneNotSupportedException{
        FollowMeVO followMeVO = (FollowMeVO) super.clone();
        followMeVO.setCodigo(0);
        followMeVO.setNovoObj(true);
        followMeVO.setDescricao(followMeVO.getDescricao() + " - Clone");
        followMeVO.setFollowMeGrupoDestinatarioVOs(new ArrayList<FollowMeGrupoDestinatarioVO>());
        followMeVO.setFollowMeUnidadeEnsinoVOs(new ArrayList<FollowMeUnidadeEnsinoVO>());
        for(FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO:this.getFollowMeGrupoDestinatarioVOs()){
            FollowMeGrupoDestinatarioVO clone = followMeGrupoDestinatarioVO.clone();            
            followMeVO.getFollowMeGrupoDestinatarioVOs().add(clone);
        }
        for(FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO:this.getFollowMeUnidadeEnsinoVOs()){
            FollowMeUnidadeEnsinoVO clone = followMeUnidadeEnsinoVO.clone();            
            followMeVO.getFollowMeUnidadeEnsinoVOs().add(clone);
        }
        return followMeVO;
    }
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public List<FollowMeGrupoDestinatarioVO> getFollowMeGrupoDestinatarioVOs() {
        if(followMeGrupoDestinatarioVOs == null){
            followMeGrupoDestinatarioVOs = new ArrayList<FollowMeGrupoDestinatarioVO>(0);
        }
        return followMeGrupoDestinatarioVOs;
    }
    
    public void setFollowMeGrupoDestinatarioVOs(List<FollowMeGrupoDestinatarioVO> followMeGrupoDestinatarioVOs) {
        this.followMeGrupoDestinatarioVOs = followMeGrupoDestinatarioVOs;
    }
    
    public Integer getPeriodoBase() {
        if(periodoBase == null){
            periodoBase = 1;
        }
        return periodoBase;
    }
    
    public void setPeriodoBase(Integer periodoBase) {
        this.periodoBase = periodoBase;
    }
    
    public Integer getPeriodoBaseComparativo() {
        if(periodoBaseComparativo == null){
            periodoBaseComparativo = 6;
        }
        return periodoBaseComparativo;
    }
    
    public void setPeriodoBaseComparativo(Integer periodoBaseComparativo) {
        this.periodoBaseComparativo = periodoBaseComparativo;
    }

    
    public String getDescricao() {
        if(descricao == null){
            descricao ="";
        }
        return descricao;
    }

    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    public List<FollowMeUnidadeEnsinoVO> getFollowMeUnidadeEnsinoVOs() {
        if(followMeUnidadeEnsinoVOs == null){
            followMeUnidadeEnsinoVOs = new ArrayList<FollowMeUnidadeEnsinoVO>(0);
        }
        return followMeUnidadeEnsinoVOs;
    }

    
    public void setFollowMeUnidadeEnsinoVOs(List<FollowMeUnidadeEnsinoVO> followMeUnidadeEnsinoVOs) {
        this.followMeUnidadeEnsinoVOs = followMeUnidadeEnsinoVOs;
    }

    
  

    
    public Boolean getApresentarDadosTodasUnidades() {
        if(apresentarDadosTodasUnidades == null){
            apresentarDadosTodasUnidades = true;
        }
        return apresentarDadosTodasUnidades;
    }

    
    public void setApresentarDadosTodasUnidades(Boolean apresentarDadosTodasUnidades) {
        this.apresentarDadosTodasUnidades = apresentarDadosTodasUnidades;
    }

    
    public Boolean getGastoPorDepartamento() {
        if(gastoPorDepartamento == null){
            gastoPorDepartamento = true;
        }
        return gastoPorDepartamento;
    }

    
    public void setGastoPorDepartamento(Boolean gastoPorDepartamento) {
        this.gastoPorDepartamento = gastoPorDepartamento;
    }

    
    public Boolean getReceitaDespesa() {
        if(receitaDespesa == null){
            receitaDespesa = true;
        }
        return receitaDespesa;
    }

    
    public void setReceitaDespesa(Boolean receitaDespesa) {
        this.receitaDespesa = receitaDespesa;
    }

    
    public Boolean getReceitaDespesaPorNivelEducacional() {
        if(receitaDespesaPorNivelEducacional == null){
            receitaDespesaPorNivelEducacional = true;
        }
        return receitaDespesaPorNivelEducacional;
    }

    
    public void setReceitaDespesaPorNivelEducacional(Boolean receitaDespesaPorNivelEducacional) {
        this.receitaDespesaPorNivelEducacional = receitaDespesaPorNivelEducacional;
    }

    
    public Boolean getPosicaoFinanceira() {
        if(posicaoFinanceira == null){
            posicaoFinanceira = true;
        }
        return posicaoFinanceira;
    }

    
    public void setPosicaoFinanceira(Boolean posicaoFinanceira) {
        this.posicaoFinanceira = posicaoFinanceira;
    }

    
   

    
    public Boolean getInadimplencia() {
        if(inadimplencia == null){
            inadimplencia = true;
        }
        return inadimplencia;
    }

    
    public void setInadimplencia(Boolean inadimplencia) {
        this.inadimplencia = inadimplencia;
    }

    
    public List<FollowMeCategoriaDespesaVO> getFollowMeCategoriaDespesaVOs() {
        if(followMeCategoriaDespesaVOs == null){
            followMeCategoriaDespesaVOs = new ArrayList<FollowMeCategoriaDespesaVO>(0);
        }
        return followMeCategoriaDespesaVOs;
    }

    
    public void setFollowMeCategoriaDespesaVOs(List<FollowMeCategoriaDespesaVO> followMeCategoriaDespesaVOs) {
        this.followMeCategoriaDespesaVOs = followMeCategoriaDespesaVOs;
    }

    
    public List<FollowMeDepartamentoVO> getFollowMeDepartamentoVOs() {
        if(followMeDepartamentoVOs == null){
            followMeDepartamentoVOs = new ArrayList<FollowMeDepartamentoVO>(0);
        }
        return followMeDepartamentoVOs;
    }

    
    public void setFollowMeDepartamentoVOs(List<FollowMeDepartamentoVO> followMeDepartamentoVOs) {
        this.followMeDepartamentoVOs = followMeDepartamentoVOs;
    }

    
    public Boolean getGastoPorDepartamentoAnoAnterior() {
        if(gastoPorDepartamentoAnoAnterior == null){
            gastoPorDepartamentoAnoAnterior = false;
        }
        return gastoPorDepartamentoAnoAnterior;
    }

    
    public void setGastoPorDepartamentoAnoAnterior(Boolean gastoPorDepartamentoAnoAnterior) {
        this.gastoPorDepartamentoAnoAnterior = gastoPorDepartamentoAnoAnterior;
    }

    
    public Boolean getGerarGraficoDepartamentoMesAMes() {
        if(gerarGraficoDepartamentoMesAMes == null){
            gerarGraficoDepartamentoMesAMes = false;
        }
        return gerarGraficoDepartamentoMesAMes;
    }

    
    public void setGerarGraficoDepartamentoMesAMes(Boolean gerarGraficoDepartamentoMesAMes) {
        this.gerarGraficoDepartamentoMesAMes = gerarGraficoDepartamentoMesAMes;
    }

    
    public Boolean getReceitaDespesaAnoAnterior() {
        if(receitaDespesaAnoAnterior == null){
            receitaDespesaAnoAnterior = false;
        }
        return receitaDespesaAnoAnterior;
    }

    
    public void setReceitaDespesaAnoAnterior(Boolean receitaDespesaAnoAnterior) {
        this.receitaDespesaAnoAnterior = receitaDespesaAnoAnterior;
    }

    
    public Boolean getReceitaDespesaPorNivelEducacionalAnoAnterior() {
        if(receitaDespesaPorNivelEducacionalAnoAnterior == null){
            receitaDespesaPorNivelEducacionalAnoAnterior = false;
        }
        return receitaDespesaPorNivelEducacionalAnoAnterior;
    }

    
    public void setReceitaDespesaPorNivelEducacionalAnoAnterior(Boolean receitaDespesaPorNivelEducacionalAnoAnterior) {
        this.receitaDespesaPorNivelEducacionalAnoAnterior = receitaDespesaPorNivelEducacionalAnoAnterior;
    }

    
    public Boolean getGerarGraficoDespesa() {
        if(gerarGraficoDespesa == null){
            gerarGraficoDespesa = true;
        }
        return gerarGraficoDespesa;
    }

    
    public void setGerarGraficoDespesa(Boolean gerarGraficoDespesa) {
        this.gerarGraficoDespesa = gerarGraficoDespesa;
    }

    
    public Boolean getGerarGraficoCategoriaDespesa() {
        if(gerarGraficoCategoriaDespesa == null){
            gerarGraficoCategoriaDespesa = true;
        }
        return gerarGraficoCategoriaDespesa;
    }

    
    public void setGerarGraficoCategoriaDespesa(Boolean gerarGraficoCategoriaDespesa) {
        this.gerarGraficoCategoriaDespesa = gerarGraficoCategoriaDespesa;
    }

    
    public Boolean getGerarGraficoCategoriaDespesaMesAMes() {
        if(gerarGraficoCategoriaDespesaMesAMes == null){
            gerarGraficoCategoriaDespesaMesAMes = false;
        }
        return gerarGraficoCategoriaDespesaMesAMes;
    }

    
    public void setGerarGraficoCategoriaDespesaMesAMes(Boolean gerarGraficoCategoriaDespesaMesAMes) {
        this.gerarGraficoCategoriaDespesaMesAMes = gerarGraficoCategoriaDespesaMesAMes;
    }

    
    public Boolean getGerarListaCategoriaDespesaPadraoAlterado() {
        if(gerarListaCategoriaDespesaPadraoAlterado == null){
            gerarListaCategoriaDespesaPadraoAlterado = true;
        }
        return gerarListaCategoriaDespesaPadraoAlterado;
    }

    
    public void setGerarListaCategoriaDespesaPadraoAlterado(Boolean gerarListaCategoriaDespesaPadraoAlterado) {
        this.gerarListaCategoriaDespesaPadraoAlterado = gerarListaCategoriaDespesaPadraoAlterado;
    }

    
  
    
    public Boolean getGerarListaAcademicoFinanceiro() {
        if(gerarListaAcademicoFinanceiro == null){
            gerarListaAcademicoFinanceiro = true;
        }
        return gerarListaAcademicoFinanceiro;
    }

    
    public void setGerarListaAcademicoFinanceiro(Boolean gerarListaAcademicoFinanceiro) {
        this.gerarListaAcademicoFinanceiro = gerarListaAcademicoFinanceiro;
    }

    public Double getPercentualCategoriaDespesaPadraoAlterado() {
        if(percentualCategoriaDespesaPadraoAlterado == null){
            percentualCategoriaDespesaPadraoAlterado = 0.0;
        }
        return percentualCategoriaDespesaPadraoAlterado;
    }

    
    public void setPercentualCategoriaDespesaPadraoAlterado(Double percentualCategoriaDespesaPadraoAlterado) {
        this.percentualCategoriaDespesaPadraoAlterado = percentualCategoriaDespesaPadraoAlterado;
    }

    
    public Boolean getGerarGraficoDespesaAnoAnterior() {
        if(gerarGraficoDespesaAnoAnterior == null){
            gerarGraficoDespesaAnoAnterior = false;
        }
        return gerarGraficoDespesaAnoAnterior;
    }

    
    public void setGerarGraficoDespesaAnoAnterior(Boolean gerarGraficoDespesaAnoAnterior) {
        this.gerarGraficoDespesaAnoAnterior = gerarGraficoDespesaAnoAnterior;
    }

    
    public Boolean getReceitaDespesaPorNivelEducacionalMesMes() {
        if(receitaDespesaPorNivelEducacionalMesMes== null){
            receitaDespesaPorNivelEducacionalMesMes = false;
        }
        return receitaDespesaPorNivelEducacionalMesMes;
    }

    
    public void setReceitaDespesaPorNivelEducacionalMesMes(Boolean receitaDespesaPorNivelEducacionalMesMes) {
        this.receitaDespesaPorNivelEducacionalMesMes = receitaDespesaPorNivelEducacionalMesMes;
    }

    
    public Boolean getApresentarDespesaDepartamentoNaoInformado() {
        if(apresentarDespesaDepartamentoNaoInformado == null){
            apresentarDespesaDepartamentoNaoInformado = false;
        }
        return apresentarDespesaDepartamentoNaoInformado;
    }

    
    public void setApresentarDespesaDepartamentoNaoInformado(Boolean apresentarDespesaDepartamentoNaoInformado) {
        this.apresentarDespesaDepartamentoNaoInformado = apresentarDespesaDepartamentoNaoInformado;
    }

    
    public Boolean getApresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro() {
        if(apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro == null){
            apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro = true;
        }
        return apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro;
    }

    
    public void setApresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro(Boolean apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro) {
        this.apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro = apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro;
    }

    
    public Boolean getApresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro() {
        if(apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro == null){
            apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro = true;
        }
        return apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro;
    }

    
    public void setApresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro(Boolean apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro) {
        this.apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro = apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro;
    }

    
    public Boolean getApresentarInadimplenciaRelatorioAcademicoFinanceiro() {
        if(apresentarInadimplenciaRelatorioAcademicoFinanceiro == null){
            apresentarInadimplenciaRelatorioAcademicoFinanceiro = true;
        }
        return apresentarInadimplenciaRelatorioAcademicoFinanceiro;
    }

    
    public void setApresentarInadimplenciaRelatorioAcademicoFinanceiro(Boolean apresentarInadimplenciaRelatorioAcademicoFinanceiro) {
        this.apresentarInadimplenciaRelatorioAcademicoFinanceiro = apresentarInadimplenciaRelatorioAcademicoFinanceiro;
    }

    
    public Boolean getApresentarQtdeAlunoRelatorioAcademicoFinanceiro() {
        if(apresentarQtdeAlunoRelatorioAcademicoFinanceiro == null){
            apresentarQtdeAlunoRelatorioAcademicoFinanceiro = true;
        }
        return apresentarQtdeAlunoRelatorioAcademicoFinanceiro;
    }

    
    public void setApresentarQtdeAlunoRelatorioAcademicoFinanceiro(Boolean apresentarQtdeAlunoRelatorioAcademicoFinanceiro) {
        this.apresentarQtdeAlunoRelatorioAcademicoFinanceiro = apresentarQtdeAlunoRelatorioAcademicoFinanceiro;
    }

    
    public Boolean getApresentarQtdeTurmaRelatorioAcademicoFinanceiro() {
        if(apresentarQtdeTurmaRelatorioAcademicoFinanceiro == null){
            apresentarQtdeTurmaRelatorioAcademicoFinanceiro = true;
        }
        return apresentarQtdeTurmaRelatorioAcademicoFinanceiro;
    }

    
    public void setApresentarQtdeTurmaRelatorioAcademicoFinanceiro(Boolean apresentarQtdeTurmaRelatorioAcademicoFinanceiro) {
        this.apresentarQtdeTurmaRelatorioAcademicoFinanceiro = apresentarQtdeTurmaRelatorioAcademicoFinanceiro;
    }

    
    public Boolean getApresentarDespesaRelatorioAcademicoFinanceiro() {
        if(apresentarDespesaRelatorioAcademicoFinanceiro == null){
            apresentarDespesaRelatorioAcademicoFinanceiro = true;
        }
        return apresentarDespesaRelatorioAcademicoFinanceiro;
    }

    
    public void setApresentarDespesaRelatorioAcademicoFinanceiro(Boolean apresentarDespesaRelatorioAcademicoFinanceiro) {
        this.apresentarDespesaRelatorioAcademicoFinanceiro = apresentarDespesaRelatorioAcademicoFinanceiro;
    }

    
   
    
    

    
    
}
