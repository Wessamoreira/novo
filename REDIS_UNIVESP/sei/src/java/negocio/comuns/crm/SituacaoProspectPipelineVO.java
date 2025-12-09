/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.crm;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;

/**
 *
 * @author edigarjr
 */
public class SituacaoProspectPipelineVO extends SuperVO {

    protected Integer codigo;
    protected String nome;
    protected String corFundo;
    protected String corTexto;
    private Boolean apresentarPipeLine;
    protected SituacaoProspectPipelineControleEnum controle;

    /**
     * Construtor padrão da classe <code>SituacaoProspectPipeline</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public SituacaoProspectPipelineVO() {
        super();
    }

    public Boolean getControleFinalizadoInsucesso() {
        if (getControle() != null && getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO)) {
            return true;
        }
        return false;
    }

    public SituacaoProspectPipelineControleEnum getControle() {
        if (controle == null) {
            controle = SituacaoProspectPipelineControleEnum.NENHUM;
        }
        return (controle);
    }

    public void setControle(SituacaoProspectPipelineControleEnum controle) {
        this.controle = controle;
    }

    public String getNome() {
        if (nome == null) {
            return "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getControle_Apresentar() {
        if (getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
            return "Inicial";
        } else if (getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)) {
            return "Finalizado com sucesso";
        } else if (getControle().equals(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO)) {
            return "Finalizado com insucesso";
        } else {
            return "";
        }
    }

    /**
     * @return the apresentarPipeLine
     */
    public Boolean getApresentarPipeLine() {
        if (apresentarPipeLine == null) {
            apresentarPipeLine = Boolean.FALSE;
        }
        return apresentarPipeLine;
    }

    /**
     * @param apresentarPipeLine the apresentarPipeLine to set
     */
    public void setApresentarPipeLine(Boolean apresentarPipeLine) {
        this.apresentarPipeLine = apresentarPipeLine;
    }

    public String getCorFundo() {
         if(corFundo == null){
            corFundo = "#b3b3b3b3";
        }
        return corFundo;
    }
    
    public String getCorFundo_Apresentacao() {
        if(corFundo == null){
           corFundo = "#b3b3b3b3";
       }
       return "#"+corFundo;
   }

    public void setCorFundo(String corFundo) {
        this.corFundo = corFundo;
    }

    public String getCorTexto() {
        if(corTexto == null){
            corTexto = "#000000";
        }
        return corTexto;
    }
    
    public String getCorTexto_Apresentacao() {
        if(corTexto == null){
            corTexto = "#000000";
        }
        return "#"+corTexto;
    }

    public void setCorTexto(String corTexto) {
        this.corTexto = corTexto;
    }
    
    
}
