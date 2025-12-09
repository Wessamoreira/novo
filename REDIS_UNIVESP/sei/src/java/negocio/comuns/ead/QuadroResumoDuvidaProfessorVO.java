package negocio.comuns.ead;

import negocio.comuns.ead.enumeradores.SituacaoDuvidaProfessorEnum;


public class QuadroResumoDuvidaProfessorVO {

    private SituacaoDuvidaProfessorEnum situacaoDuvidaProfessor;
    private Integer quantidade;
    
    
    
    public QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum situacaoDuvidaProfessor, Integer quantidade) {
        super();
        this.situacaoDuvidaProfessor = situacaoDuvidaProfessor;
        this.quantidade = quantidade;
    }

    public SituacaoDuvidaProfessorEnum getSituacaoDuvidaProfessor() {
        return situacaoDuvidaProfessor;
    }
    
    public void setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum situacaoDuvidaProfessor) {
        this.situacaoDuvidaProfessor = situacaoDuvidaProfessor;
    }
    
    public Integer getQuantidade() {
        if(quantidade == null){
            quantidade = 0;
        }
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
    
    
    
}
