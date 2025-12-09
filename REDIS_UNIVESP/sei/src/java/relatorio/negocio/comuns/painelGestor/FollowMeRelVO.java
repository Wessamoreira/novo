package relatorio.negocio.comuns.painelGestor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import relatorio.negocio.comuns.painelGestor.enumeradores.TipoAreaFollowMeEnum;
import relatorio.negocio.comuns.painelGestor.enumeradores.TipoRelatorioAreaFollowMe;

import negocio.comuns.administrativo.FollowMeVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;


public class FollowMeRelVO extends SuperVO {
    
    private Integer codigo;
    private Date dataInicio;
    private Date dataTermino;
    private FollowMeVO followMe;
    private List<FollowMeGraficoRelVO> followMeGraficoRelVOs;
    private List<FollowMeCategoriaDespesaPadraoAlteradoVO> followMeCategoriaDespesaPadraoAlteradoVOs;
    private List<FollowMeFinanceiroAcademicoRelVO> followMeFinanceiroAcademicoRelVOs;
    private Date dataGeracao;
    private String nomeFisicoArquivo;
    private String caminhoBaseArquivo;    
    private TipoAreaFollowMeEnum tipoAreaFollowMeEnum;
    private TipoRelatorioAreaFollowMe tipoRelatorioAreaFollowMe;
    
    
    public Date getDataInicio() {
        if(dataInicio == null){                        
            try {
                dataInicio = Uteis.getDataFutura(Uteis.getDataPrimeiroDiaMes(new Date()), Calendar.MONTH, (getFollowMe().getPeriodoBaseComparativo()-1)*-1);                
            } catch (Exception ex) {
                dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
            }
            
        }
        return dataInicio;
    }
    
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }
    
    public Date getDataTermino() {
        if(dataTermino == null){
            dataTermino = Uteis.getDataUltimoDiaMes(new Date());
        }
        return dataTermino;
    }
    
    public void setDataTermino(Date dataTermino) {
        this.dataTermino = dataTermino;
    }
    
    public FollowMeVO getFollowMe() {
        if(followMe == null){
            followMe = new FollowMeVO();
        }
        return followMe;
    }
    
    public void setFollowMe(FollowMeVO followMe) {
        this.followMe = followMe;
    }
    
    public List<FollowMeGraficoRelVO> getFollowMeGraficoRelVOs() {
        if(followMeGraficoRelVOs == null){
            followMeGraficoRelVOs = new ArrayList<FollowMeGraficoRelVO>(0);
        }
        return followMeGraficoRelVOs;
    }
    
    public void setFollowMeGraficoRelVOs(List<FollowMeGraficoRelVO> followMeGraficoRelVOs) {
        this.followMeGraficoRelVOs = followMeGraficoRelVOs;
    }
    
    public Date getDataGeracao() {
        if(dataGeracao == null){
            dataGeracao = new Date();
        }
        return dataGeracao;
    }
    
    public void setDataGeracao(Date dataGeracao) {
        this.dataGeracao = dataGeracao;
    }
    
    public String getNomeFisicoArquivo() {
        if(nomeFisicoArquivo == null){
            nomeFisicoArquivo = getTipoRelatorioAreaFollowMe().name()+"_"+Uteis.getData(getDataInicio(), "dd_MM_yyyy")+"_a_"+Uteis.getData(getDataTermino(), "dd_MM_yyyy")+".pdf";
        }
        return nomeFisicoArquivo;
    }
    
    public void setNomeFisicoArquivo(String nomeFisicoArquivo) {
        this.nomeFisicoArquivo = nomeFisicoArquivo;
    }
    
    public String getCaminhoBaseArquivo() {
        if(caminhoBaseArquivo == null){
            caminhoBaseArquivo = "";
        }
        return caminhoBaseArquivo;
    }
    
    public void setCaminhoBaseArquivo(String caminhoBaseArquivo) {
        this.caminhoBaseArquivo = caminhoBaseArquivo;
    }
    

    
    public TipoAreaFollowMeEnum getTipoAreaFollowMeEnum() {
        if(tipoAreaFollowMeEnum == null){
            tipoAreaFollowMeEnum = TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA;
        }
        return tipoAreaFollowMeEnum;
    }

    
    public void setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum tipoAreaFollowMeEnum) {
        this.tipoAreaFollowMeEnum = tipoAreaFollowMeEnum;
    }

    
    public Integer getCodigo() {
        return codigo;
    }

    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getCaminhoCompletoDownload(){
        return getCaminhoBaseArquivo().replaceAll("\\\\", "/")+"/"+getNomeFisicoArquivo();
    }

    
    public TipoRelatorioAreaFollowMe getTipoRelatorioAreaFollowMe() {
        if(tipoRelatorioAreaFollowMe == null){
            tipoRelatorioAreaFollowMe = TipoRelatorioAreaFollowMe.GASTO_POR_DEPARTAMENTO;
        }
        return tipoRelatorioAreaFollowMe;
    }

    
    public void setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe tipoRelatorioAreaFollowMe) {
        this.tipoRelatorioAreaFollowMe = tipoRelatorioAreaFollowMe;
    }

    
    public List<FollowMeCategoriaDespesaPadraoAlteradoVO> getFollowMeCategoriaDespesaPadraoAlteradoVOs() {
        if(followMeCategoriaDespesaPadraoAlteradoVOs == null){
            followMeCategoriaDespesaPadraoAlteradoVOs = new ArrayList<FollowMeCategoriaDespesaPadraoAlteradoVO>(0);
        }
        return followMeCategoriaDespesaPadraoAlteradoVOs;
    }

    
    public void setFollowMeCategoriaDespesaPadraoAlteradoVOs(List<FollowMeCategoriaDespesaPadraoAlteradoVO> followMeCategoriaDespesaPadraoAlteradoVOs) {
        this.followMeCategoriaDespesaPadraoAlteradoVOs = followMeCategoriaDespesaPadraoAlteradoVOs;
    }

    
    public List<FollowMeFinanceiroAcademicoRelVO> getFollowMeFinanceiroAcademicoRelVOs() {
        if(followMeFinanceiroAcademicoRelVOs == null){
            followMeFinanceiroAcademicoRelVOs = new ArrayList<FollowMeFinanceiroAcademicoRelVO>(0);
        }
        return followMeFinanceiroAcademicoRelVOs;
    }

    
    public void setFollowMeFinanceiroAcademicoRelVOs(List<FollowMeFinanceiroAcademicoRelVO> followMeFinanceiroAcademicoRelVOs) {
        this.followMeFinanceiroAcademicoRelVOs = followMeFinanceiroAcademicoRelVOs;
    }

    
    

    
  
}
