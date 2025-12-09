package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Carlos
 */
public class DisciplinaCompostaVO extends SuperVO {
    private DisciplinaVO disciplinaVO;
    private DisciplinaVO compostaVO;
    private Integer ordem;
    
    public DisciplinaCompostaVO() {

    }

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public DisciplinaVO getCompostaVO() {
        if (compostaVO == null) {
            compostaVO = new DisciplinaVO();
        }
        return compostaVO;
    }

    public void setCompostaVO(DisciplinaVO compostaVO) {
        this.compostaVO = compostaVO;
    }

    public Integer getOrdem() {
        if (ordem == null) {
            ordem = 0;
        }
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
}
