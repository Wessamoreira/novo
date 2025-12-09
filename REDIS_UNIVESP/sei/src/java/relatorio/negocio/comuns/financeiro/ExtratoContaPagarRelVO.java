package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import negocio.comuns.financeiro.ContaPagarVO;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ExtratoContaPagarRelVO {

    private String favorecido;
    private String tipoData;
    private Double totalPago;
    private Double totalPagar;
    private String nomeBanco;
    private String numeroBancoRecebimento;
    private String numeroAgenciaRecebimento;
    private String contaCorrenteRecebimento;
    private Boolean tipoFavorecidoFornecedorFuncionario;
    private List<ContaPagarVO> listaContaPagarVOs;

    public JRDataSource getContasPagar() {
        return new JRBeanArrayDataSource(getListaContaPagarVOs().toArray());
    }

    public String getFavorecido() {
        if (favorecido == null) {
            favorecido = "";
        }
        return favorecido;
    }

    public void setFavorecido(String favorecido) {
        this.favorecido = favorecido;
    }

    public List<ContaPagarVO> getListaContaPagarVOs() {
        if (listaContaPagarVOs == null) {
            listaContaPagarVOs = new ArrayList<ContaPagarVO>(0);
        }
        return listaContaPagarVOs;
    }

    public void setListaContaPagarVOs(List<ContaPagarVO> listaContaPagarVOs) {
        this.listaContaPagarVOs = listaContaPagarVOs;
    }

    public String getTipoData() {
        if (tipoData == null) {
            tipoData = "";
        }
        return tipoData;
    }

    public void setTipoData(String tipoData) {
        this.tipoData = tipoData;
    }

    public Double getTotalPagar() {
        if (totalPagar == null) {
            totalPagar = 0.0;
        }
        return totalPagar;
    }

    public void setTotalPagar(Double totalPagar) {
        this.totalPagar = totalPagar;
    }

    public Double getTotalPago() {
        if (totalPago == null) {
            totalPago = 0.0;
        }
        return totalPago;
    }

    public void setTotalPago(Double totalPago) {
        this.totalPago = totalPago;
    }

    public String getNomeBanco() {
        if (nomeBanco == null) {
            nomeBanco = "";
        }
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    public String getNumeroBancoRecebimento() {
        if (numeroBancoRecebimento == null) {
            numeroBancoRecebimento = "";
        }
        return numeroBancoRecebimento;
    }

    public void setNumeroBancoRecebimento(String numeroBancoRecebimento) {
        this.numeroBancoRecebimento = numeroBancoRecebimento;
    }

    public String getNumeroAgenciaRecebimento() {
        if (numeroAgenciaRecebimento == null) {
            numeroAgenciaRecebimento = "";
        }
        return numeroAgenciaRecebimento;
    }

    public void setNumeroAgenciaRecebimento(String numeroAgenciaRecebimento) {
        this.numeroAgenciaRecebimento = numeroAgenciaRecebimento;
    }

    public String getContaCorrenteRecebimento() {
        if (contaCorrenteRecebimento == null) {
            contaCorrenteRecebimento = "";
        }
        return contaCorrenteRecebimento;
    }

    public void setContaCorrenteRecebimento(String contaCorrenteRecebimento) {
        this.contaCorrenteRecebimento = contaCorrenteRecebimento;
    }

    public Boolean getTipoFavorecidoFornecedorFuncionario() {
        if(tipoFavorecidoFornecedorFuncionario == null){
            tipoFavorecidoFornecedorFuncionario = false;
        }
        return tipoFavorecidoFornecedorFuncionario;
    }

    public void setTipoFavorecidoFornecedorFuncionario(Boolean tipoFavorecidoFornecedorFuncionario) {
        this.tipoFavorecidoFornecedorFuncionario = tipoFavorecidoFornecedorFuncionario;
    }
}
