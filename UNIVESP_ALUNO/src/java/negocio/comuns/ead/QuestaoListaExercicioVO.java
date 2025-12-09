package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;


public class QuestaoListaExercicioVO extends SuperVO {
    
    private Integer codigo;
    private ListaExercicioVO listaExercicio;
    private QuestaoVO questao;
    private Integer ordemApresentacao;
    
    public QuestaoListaExercicioVO clone() throws CloneNotSupportedException{
        QuestaoListaExercicioVO clone = (QuestaoListaExercicioVO) super.clone();
        clone.setNovoObj(true);
        clone.setCodigo(0);
        clone.setListaExercicio(new ListaExercicioVO());
        return clone;
    }
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public ListaExercicioVO getListaExercicio() {
        if(listaExercicio == null){
            listaExercicio = new ListaExercicioVO();
        }
        return listaExercicio;
    }
    
    public void setListaExercicio(ListaExercicioVO listaExercicio) {
        this.listaExercicio = listaExercicio;
    }
    
    public QuestaoVO getQuestao() {
        if(questao == null){
            questao = new QuestaoVO();
        }
        return questao;
    }
    
    public void setQuestao(QuestaoVO questao) {
        this.questao = questao;
    }
    
    public Integer getOrdemApresentacao() {
        if(ordemApresentacao == null){
            ordemApresentacao = 0; 
        }
        return ordemApresentacao;
    }
    
    public void setOrdemApresentacao(Integer ordemApresentacao) {
        this.ordemApresentacao = ordemApresentacao;
    }
    
    
    

}
