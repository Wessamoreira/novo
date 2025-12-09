package negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;


public class ProcessoSeletivoProvaDataVO extends SuperVO {
    
    private ItemProcSeletivoDataProvaVO itemProcSeletivoDataProva;
    private DisciplinasProcSeletivoVO disciplinaIdioma;
    private ProvaProcessoSeletivoVO provaProcessoSeletivo;
    private Integer codigo;
    
    public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProva() {
        if(itemProcSeletivoDataProva == null){
            itemProcSeletivoDataProva = new ItemProcSeletivoDataProvaVO();
        }
        return itemProcSeletivoDataProva;
    }
    
    public void setItemProcSeletivoDataProva(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProva) {
        this.itemProcSeletivoDataProva = itemProcSeletivoDataProva;
    }
    
    public DisciplinasProcSeletivoVO getDisciplinaIdioma() {
        if(disciplinaIdioma == null){
            disciplinaIdioma = new DisciplinasProcSeletivoVO();
        }
        return disciplinaIdioma;
    }
    
    public void setDisciplinaIdioma(DisciplinasProcSeletivoVO disciplinaIdioma) {
        this.disciplinaIdioma = disciplinaIdioma;
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
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    
    

}
