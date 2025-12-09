package negocio.comuns.financeiro;

/**
 *
 * @author Alessandro Alterado estrutura, desacoplando codigo... (Diego)
 */
import java.io.Serializable;

import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;

public class EstatisticasLancamentosFuturosVO implements Serializable {

    private TipoMapaLancamentoFuturo tipoMapaLancamento;
    private Integer qtdAnterior;
    private Integer qtdHoje;
    private Integer qtdFuturos;
    private String popup;
    private Boolean desabilitar;
    public static final long serialVersionUID = 1L;

    public Integer getQtdAnterior() {
        if (qtdAnterior == null) {
            qtdAnterior = 0;
        }
        return qtdAnterior;
    }

    public void setQtdAnterior(Integer qtdAnterior) {
        this.qtdAnterior = qtdAnterior;
    }

    public Integer getQtdHoje() {
        if (qtdHoje == null) {
            qtdHoje = 0;
        }
        return qtdHoje;
    }

    public void setQtdHoje(Integer qtdHoje) {
        this.qtdHoje = qtdHoje;
    }

    public Integer getQtdFuturos() {
        if (qtdFuturos == null) {
            qtdFuturos = 0;
        }
        return qtdFuturos;
    }

    public void setQtdFuturos(Integer qtdFuturos) {
        this.qtdFuturos = qtdFuturos;
    }

    public TipoMapaLancamentoFuturo getTipoMapaLancamento() {
        return tipoMapaLancamento;
    }

    public void setTipoMapaLancamento(TipoMapaLancamentoFuturo tipoMapaLancamento) {
        this.tipoMapaLancamento = tipoMapaLancamento;
    }

	public String getPopup() {
		if(popup == null) {
			popup =  "";
		}
		return popup;
	}

	public void setPopup(String popup) {
		this.popup = popup;
	}

	public Boolean getDesabilitar() {
		if(desabilitar == null) {
			desabilitar =  false;
		}
		return desabilitar;
	}

	public void setDesabilitar(Boolean desabilitar) {
		this.desabilitar = desabilitar;
	}
    
    
}
