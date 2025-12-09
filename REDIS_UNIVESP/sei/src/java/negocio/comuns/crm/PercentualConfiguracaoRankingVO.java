/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Paulo Taucci
 */
public class PercentualConfiguracaoRankingVO extends SuperVO {

    private Integer codigo;
    private Integer posicao;
    private Integer qtdePosicao;
    private Double percentual;
    private ConfiguracaoRankingVO configuracaoRanking;

    /**
     * Construtor padrão da classe <code>PercentualConfiguracaoRanking</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public PercentualConfiguracaoRankingVO() {
        super();
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

    public Double getPercentual() {
        if (percentual == null) {
            percentual = 0.0;
        }
        return percentual;
    }

    public void setPercentual(Double percentual) {
        this.percentual = percentual;
    }

    public Integer getPosicao() {
        if (posicao == null) {
            posicao = 0;
        }
        return posicao;
    }

    public String getPosicao_Apresentar() {
        if (!getQtdePosicao().equals(0)) {
            return getPosicao().toString() + "/" + getQtdePosicao().toString();
        }
        return getPosicao().toString();
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public ConfiguracaoRankingVO getConfiguracaoRanking() {
        if (configuracaoRanking == null) {
            configuracaoRanking = new ConfiguracaoRankingVO();
        }
        return configuracaoRanking;
    }

    public void setConfiguracaoRanking(ConfiguracaoRankingVO configuracaoRanking) {
        this.configuracaoRanking = configuracaoRanking;
    }

    public Integer getQtdePosicao() {
        if (qtdePosicao == null) {
            qtdePosicao = 0;
        }
        return qtdePosicao;
    }

    public void setQtdePosicao(Integer qtdePosicao) {
        this.qtdePosicao = qtdePosicao;
    }

}
