package negocio.comuns.planoorcamentario;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Carlos
 */
public class ItemMensalDetalhamentoPlanoOrcamentarioVO extends SuperVO{

	private static final long serialVersionUID = -4070257204720509263L;

	private Integer codigo;
    private String mes;
    private Double valor;
    private Double valorConsumidoMes;
    private DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO;

    public String getMes() {
        if (mes == null) {
            mes = "";
        }
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public DetalhamentoPlanoOrcamentarioVO getDetalhamentoPlanoOrcamentarioVO() {
        if (detalhamentoPlanoOrcamentarioVO == null) {
            detalhamentoPlanoOrcamentarioVO = new DetalhamentoPlanoOrcamentarioVO();
        }
        return detalhamentoPlanoOrcamentarioVO;
    }

    public void setDetalhamentoPlanoOrcamentarioVO(DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO) {
        this.detalhamentoPlanoOrcamentarioVO = detalhamentoPlanoOrcamentarioVO;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Double getValorConsumidoMes() {
        if (valorConsumidoMes == null) {
            valorConsumidoMes = 0.0;
        }
        return valorConsumidoMes;
    }

    public void setValorConsumidoMes(Double valorConsumidoMes) {
        this.valorConsumidoMes = valorConsumidoMes;
    }

    public Double getSaldo() {
        return getValor() - getValorConsumidoMes();
    }
}
