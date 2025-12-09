package negocio.comuns.processosel;

import negocio.comuns.processosel.enumeradores.MotivoNaoProcessamentoRespostaProcessoSeletivoEnum;


public class InscricaoRespostaNaoProcessadaVO {
    
    private Integer inscricao;
    private String candidato;
    private String disciplinaIdioma;
    private MotivoNaoProcessamentoRespostaProcessoSeletivoEnum motivo;
    
    
    
   
    
    public InscricaoRespostaNaoProcessadaVO(Integer inscricao, String candidato, String disciplinaIdioma, MotivoNaoProcessamentoRespostaProcessoSeletivoEnum motivo) {
        super();
        this.inscricao = inscricao;
        this.candidato = candidato;
        this.disciplinaIdioma = disciplinaIdioma;
        this.motivo = motivo;
    }


    public Integer getInscricao() {
        if(inscricao == null){
            inscricao = 0;
        }
        return inscricao;
    }

    
    
    public String getCandidato() {
        return candidato;
    }


    
    public void setCandidato(String candidato) {
        this.candidato = candidato;
    }


    public void setInscricao(Integer inscricao) {        
        this.inscricao = inscricao;
    }

    
    public String getDisciplinaIdioma() {        
        return disciplinaIdioma;
    }

    
    public void setDisciplinaIdioma(String disciplinaIdioma) {
        this.disciplinaIdioma = disciplinaIdioma;
    }

    public MotivoNaoProcessamentoRespostaProcessoSeletivoEnum getMotivo() {
        return motivo;
    }
    
    public void setMotivo(MotivoNaoProcessamentoRespostaProcessoSeletivoEnum motivo) {
        this.motivo = motivo;
    }
    
    
    

}
