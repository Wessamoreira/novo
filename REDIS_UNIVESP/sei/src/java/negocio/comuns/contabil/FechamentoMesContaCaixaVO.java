package negocio.comuns.contabil;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;

public class FechamentoMesContaCaixaVO extends SuperVO {

    protected Integer codigo;
    protected FechamentoMesVO fechamentoMes;
	protected ContaCorrenteVO contaCaixa;

    public static final long serialVersionUID = 1L;
    public FechamentoMesContaCaixaVO() {
        super();
        inicializarDados();
    }

    public static void validarDados(FechamentoMesContaCaixaVO obj) throws ConsistirException {
        if ((obj.getContaCaixa() == null) || (obj.getContaCaixa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA CAIXA (Fechamento da Competência) deve ser informado.");
        }
        if ((obj.getFechamentoMes() == null) || (obj.getFechamentoMes().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FECHAMENTO COMPETÊNCIA (Fechamento da Competência) deve ser informado.");
        }
    }

    public void inicializarDados() {
        setCodigo(0);
    }

    public ContaCorrenteVO getContaCaixa() {
        if (contaCaixa == null) {
        	contaCaixa = new ContaCorrenteVO();
        }
        return (contaCaixa);
    }

    public void setContaCaixa(ContaCorrenteVO obj) {
        this.contaCaixa = obj;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public FechamentoMesVO getFechamentoMes() {
    	if (fechamentoMes == null) { 
    		fechamentoMes = new FechamentoMesVO(); 
    	}
		return fechamentoMes;
	}

	public void setFechamentoMes(FechamentoMesVO fechamentoMes) {
		this.fechamentoMes = fechamentoMes;
	}
    
}
