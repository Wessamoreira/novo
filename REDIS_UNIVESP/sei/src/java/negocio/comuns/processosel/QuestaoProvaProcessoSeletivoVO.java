package negocio.comuns.processosel;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;


@XmlRootElement(name = "questaoProvaProcessoSeletivoVO")
public class QuestaoProvaProcessoSeletivoVO extends SuperVO {
    
    private Integer codigo;
    private ProvaProcessoSeletivoVO provaProcessoSeletivo;
    private QuestaoProcessoSeletivoVO questaoProcessoSeletivo;
    private Integer ordemApresentacao;
    
    public QuestaoProvaProcessoSeletivoVO clone() throws CloneNotSupportedException{
        QuestaoProvaProcessoSeletivoVO clone = (QuestaoProvaProcessoSeletivoVO) super.clone();
        clone.setNovoObj(true);
        clone.setCodigo(0);
        clone.setProvaProcessoSeletivo(new ProvaProcessoSeletivoVO());
        return clone;
    }
    
    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    
    @XmlElement(name = "ordemApresentacao")
    public Integer getOrdemApresentacao() {
        if(ordemApresentacao == null){
            ordemApresentacao = 0; 
        }
        return ordemApresentacao;
    }
    
    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }

    
    public ProvaProcessoSeletivoVO getProvaProcessoSeletivo() {
        if(provaProcessoSeletivo == null){
            provaProcessoSeletivo = new ProvaProcessoSeletivoVO();
        }
        return provaProcessoSeletivo;
    }

    
    public void setProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivo) {
        this.provaProcessoSeletivo = provaProcessoSeletivo;
    }

    @XmlElement(name = "questaoProcessoSeletivo")
    public QuestaoProcessoSeletivoVO getQuestaoProcessoSeletivo() {
        if(questaoProcessoSeletivo == null){
            questaoProcessoSeletivo = new QuestaoProcessoSeletivoVO();
        }
        return questaoProcessoSeletivo;
    }

    
    public void setQuestaoProcessoSeletivo(QuestaoProcessoSeletivoVO questaoProcessoSeletivo) {
        this.questaoProcessoSeletivo = questaoProcessoSeletivo;
    }

    
    
    
    


}
