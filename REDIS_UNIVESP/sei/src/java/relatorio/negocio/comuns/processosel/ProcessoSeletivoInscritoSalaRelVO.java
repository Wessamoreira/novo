package relatorio.negocio.comuns.processosel;

import negocio.comuns.academico.SalaLocalAulaVO;


public class ProcessoSeletivoInscritoSalaRelVO {
    
    private SalaLocalAulaVO sala;
    private Integer numeroInscrito;
    private Integer numeroInscritoNecessidadesEspeciais;
    
    public SalaLocalAulaVO getSala() {
        if(sala == null){
            sala = new SalaLocalAulaVO();
        }
        return sala;
    }
    
    public void setSala(SalaLocalAulaVO sala) {
        this.sala = sala;
    }
    
    public Integer getNumeroInscrito() {
        if(numeroInscrito == null){
            numeroInscrito = 0;
        }
        return numeroInscrito;
    }
    
    public void setNumeroInscrito(Integer numeroInscrito) {
        this.numeroInscrito = numeroInscrito;
    }
    
    public Integer getNumeroInscritoNecessidadesEspeciais() {
        if(numeroInscritoNecessidadesEspeciais == null){
            numeroInscritoNecessidadesEspeciais = 0;
        }
        return numeroInscritoNecessidadesEspeciais;
    }
    
    public void setNumeroInscritoNecessidadesEspeciais(Integer numeroInscritoNecessidadesEspeciais) {
        this.numeroInscritoNecessidadesEspeciais = numeroInscritoNecessidadesEspeciais;
    }
    
    

}
