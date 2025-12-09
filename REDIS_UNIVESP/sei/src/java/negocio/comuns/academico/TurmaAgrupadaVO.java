package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * 
 * @author Otimize-TI
 */
public class TurmaAgrupadaVO extends SuperVO {

    private Integer codigo;
    private Integer turmaOrigem;
    private TurmaVO turma;
    public static final long serialVersionUID = 1L;

    public TurmaAgrupadaVO() {
        inicializarDados();
    }

    public static void validarDados(TurmaAgrupadaVO obj) throws ConsistirException {
        if (obj.getTurma() == null || obj.getTurma().getCodigo() == 0) {
            throw new ConsistirException("O campo TURMA (TurmaAgrupada) deve ser informada");
        }
    }

    public void inicializarDados() {
        setCodigo(0);
        setTurmaOrigem(0);
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public Integer getTurmaOrigem() {
        return turmaOrigem;
    }

    public void setTurmaOrigem(Integer turmaOrigem) {
        this.turmaOrigem = turmaOrigem;
    }
}
