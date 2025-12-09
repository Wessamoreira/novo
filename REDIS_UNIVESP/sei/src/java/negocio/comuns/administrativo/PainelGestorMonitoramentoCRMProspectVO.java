package negocio.comuns.administrativo;

import negocio.comuns.crm.ProspectsVO;



public class PainelGestorMonitoramentoCRMProspectVO {
    
    private ProspectsVO prospectsVO;
    private String nomeCurso;
    private String nomeUnidadeEnsino;
    private Integer codigoPreInscricao;
    
    public ProspectsVO getProspectsVO() {
        return prospectsVO;
    }
    
    public void setProspectsVO(ProspectsVO prospectsVO) {
        this.prospectsVO = prospectsVO;
    }
    
    public String getNomeCurso() {
        return nomeCurso;
    }
    
    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
    
    public String getNomeUnidadeEnsino() {
        return nomeUnidadeEnsino;
    }
    
    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }
    
    public Integer getCodigoPreInscricao() {
        return codigoPreInscricao;
    }
    
    public void setCodigoPreInscricao(Integer codigoPreInscricao) {
        this.codigoPreInscricao = codigoPreInscricao;
    }
    
    
    

}
