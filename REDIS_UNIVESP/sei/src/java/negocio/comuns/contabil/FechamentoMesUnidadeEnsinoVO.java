package negocio.comuns.contabil;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

public class FechamentoMesUnidadeEnsinoVO extends SuperVO {

    protected Integer codigo;
    protected FechamentoMesVO fechamentoMes;
	protected UnidadeEnsinoVO unidadeEnsino;

    public static final long serialVersionUID = 1L;
    public FechamentoMesUnidadeEnsinoVO() {
        super();
        inicializarDados();
    }

    public static void validarDados(FechamentoMesUnidadeEnsinoVO obj) throws ConsistirException {
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Fechamento da Competência) deve ser informado.");
        }
        if ((obj.getFechamentoMes() == null) || (obj.getFechamentoMes().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FECHAMENTO COMPETÊNCIA (Fechamento da Competência) deve ser informado.");
        }
    }

    public void inicializarDados() {
        setCodigo(0);
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
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
