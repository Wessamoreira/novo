package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;

public class ObservacaoComplementarDiplomaVO extends SuperVO{
    private Integer codigo;
    private ExpedicaoDiplomaVO expedicaoDiploma;
    private ObservacaoComplementarVO observacaoComplementar;

    public ObservacaoComplementarDiplomaVO(){
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
    /**
     * @return the observacaoComplementar
     */
    public ObservacaoComplementarVO getObservacaoComplementar() {
        if (observacaoComplementar == null) { 
            observacaoComplementar = new ObservacaoComplementarVO();
        }
        return observacaoComplementar;
    }

    /**
     * @param observacaoComplementar the observacaoComplementar to set
     */
    public void setObservacaoComplementar(ObservacaoComplementarVO observacaoComplementar) {
        this.observacaoComplementar = observacaoComplementar;
    }

    /**
     * @return the expedicaoDiploma
     */
    public ExpedicaoDiplomaVO getExpedicaoDiploma() {
        if (expedicaoDiploma == null) { 
            expedicaoDiploma = new ExpedicaoDiplomaVO();
        }
        return expedicaoDiploma;
    }

    /**
     * @param expedicaoDiploma the expedicaoDiploma to set
     */
    public void setExpedicaoDiploma(ExpedicaoDiplomaVO expedicaoDiploma) {
        this.expedicaoDiploma = expedicaoDiploma;
    }
    	
    public static void validarDados(ObservacaoComplementarDiplomaVO obj) throws ConsistirException {
        if (obj.getExpedicaoDiploma().getCodigo().equals(0)) {
            throw new ConsistirException("O campo EXPEDIÇÃO DE DIPLOMA (Observação Complementar Diploma) deve ser informado.");
        }
	if (obj.getObservacaoComplementar().getCodigo().equals(0)) {
            throw new ConsistirException("O campo OBSERVAÇÃO COMPLEMENTAR (Observação Complementar Diploma) deve ser informado.");
	}
    }
}
