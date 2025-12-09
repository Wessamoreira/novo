package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * @author Alberto
 */
public class RequerimentoRelVO {

    private TipoRequerimentoVO tipoRequerimentoVO;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private List<RequerimentoVO> listaRequerimentoVO;
    private Integer qtdeFinalizadoDeferido;
    private Integer qtdeFinalizadoIndeferido;
    private Integer qtdeEmExecucao;
    private Integer qtdePendente;
    private Integer qtdeAguardandoPagamento;
    private Integer qtdeProntoParaRetirada;

    public JRDataSource getRequerimentos() {
        return new JRBeanArrayDataSource(getListaRequerimentoVO().toArray());
    }

    public List<RequerimentoVO> getListaRequerimentoVO() {
        if (listaRequerimentoVO == null) {
            listaRequerimentoVO = new ArrayList<RequerimentoVO>(0);
        }
        return listaRequerimentoVO;
    }

    public void setListaRequerimentoVO(List<RequerimentoVO> listaRequerimentoVO) {
        this.listaRequerimentoVO = listaRequerimentoVO;
    }

    public TipoRequerimentoVO getTipoRequerimentoVO() {
        if (tipoRequerimentoVO == null) {
            tipoRequerimentoVO = new TipoRequerimentoVO();
        }
        return tipoRequerimentoVO;
    }

    public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
        this.tipoRequerimentoVO = tipoRequerimentoVO;
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

    public Integer getQtdeAguardandoPagamento() {
        if (qtdeAguardandoPagamento == null) {
            qtdeAguardandoPagamento = 0;
        }
        return qtdeAguardandoPagamento;
    }

    public void setQtdeAguardandoPagamento(Integer qtdeAguardandoPagamento) {
        this.qtdeAguardandoPagamento = qtdeAguardandoPagamento;
    }

    public Integer getQtdeEmExecucao() {
        if (qtdeEmExecucao == null) {
            qtdeEmExecucao = 0;
        }
        return qtdeEmExecucao;
    }

    public void setQtdeEmExecucao(Integer qtdeEmExecucao) {
        this.qtdeEmExecucao = qtdeEmExecucao;
    }

    public Integer getQtdeFinalizadoDeferido() {
        if (qtdeFinalizadoDeferido == null) {
            qtdeFinalizadoDeferido = 0;
        }
        return qtdeFinalizadoDeferido;
    }

    public void setQtdeFinalizadoDeferido(Integer qtdeFinalizadoDeferido) {
        this.qtdeFinalizadoDeferido = qtdeFinalizadoDeferido;
    }

    public Integer getQtdeFinalizadoIndeferido() {
        if (qtdeFinalizadoIndeferido == null) {
            qtdeFinalizadoIndeferido = 0;
        }
        return qtdeFinalizadoIndeferido;
    }

    public void setQtdeFinalizadoIndeferido(Integer qtdeFinalizadoIndeferido) {
        this.qtdeFinalizadoIndeferido = qtdeFinalizadoIndeferido;
    }

    public Integer getQtdePendente() {
        if(qtdePendente == null){
            qtdePendente = 0;
        }
        return qtdePendente;
    }

    public void setQtdePendente(Integer qtdePendente) {
        this.qtdePendente = qtdePendente;
    }

    public Integer getQtdeProntoParaRetirada() {
        if(qtdeProntoParaRetirada == null){
            qtdeProntoParaRetirada = 0;
        }
        return qtdeProntoParaRetirada;
    }

    public void setQtdeProntoParaRetirada(Integer qtdeProntoParaRetirada) {
        this.qtdeProntoParaRetirada = qtdeProntoParaRetirada;
    }
}
