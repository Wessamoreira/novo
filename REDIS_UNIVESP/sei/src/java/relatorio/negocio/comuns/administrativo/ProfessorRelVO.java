package relatorio.negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.List;




public class ProfessorRelVO {

    private Integer codigo;
    private String nome;
    private String escolaridade;
    private List<DocumentacaoPendenteProfessorRelVO> documentacaoPendenteProfessorRelVOs;
    
    
    public Integer getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEscolaridade() {
        return escolaridade;
    }
    
    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    
    public List<DocumentacaoPendenteProfessorRelVO> getDocumentacaoPendenteProfessorRelVOs() {
        if(documentacaoPendenteProfessorRelVOs == null){
            documentacaoPendenteProfessorRelVOs = new ArrayList<DocumentacaoPendenteProfessorRelVO>(0);
        }
        return documentacaoPendenteProfessorRelVOs;
    }

    
    public void setDocumentacaoPendenteProfessorRelVOs(List<DocumentacaoPendenteProfessorRelVO> documentacaoPendenteProfessorRelVOs) {
        this.documentacaoPendenteProfessorRelVOs = documentacaoPendenteProfessorRelVOs;
    }
    
    
    
    
}
