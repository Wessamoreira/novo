package negocio.comuns.faturamento.nfe;

import java.io.Serializable;

public class ContaReceberNfeVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String dataVencimento;
	private String valor;

    public ContaReceberNfeVO() {
        setDataVencimento("");
        setValor("");
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
