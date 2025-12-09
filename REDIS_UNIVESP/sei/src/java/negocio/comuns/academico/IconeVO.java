package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.SituacaoIconeEnum;
import negocio.comuns.arquitetura.SuperVO;


public class IconeVO extends SuperVO {
    
    /**
     * 
     */
    private static final long serialVersionUID = 6069135400034236302L;
    private String nomeReal;
    private String caminhoBase;
    private Integer codigo;
    private SituacaoIconeEnum situacaoIcone;
    
    private String caminhoWebRepositorio;
    
    public IconeVO() {
        super();     
    }
    
    

    public IconeVO(String nomeReal, String caminhoBase) {
        super();
        this.nomeReal = nomeReal;
        this.caminhoBase = caminhoBase;        
    }



    public String getNomeReal() {
        if(nomeReal == null){
            nomeReal = "";
        }
        return nomeReal;
    }
    
    public void setNomeReal(String nomeReal) {
        this.nomeReal = nomeReal;
    }
    
   
    
    public String getCaminhoBase() {
        if(caminhoBase == null){
            caminhoBase = "";
        }
        return caminhoBase;
    }
    
    public void setCaminhoBase(String caminhoBase) {
        this.caminhoBase = caminhoBase;
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
    
    public SituacaoIconeEnum getSituacaoIcone() {
        if(situacaoIcone == null){
            situacaoIcone = SituacaoIconeEnum.ATIVO;
        }
        return situacaoIcone;
    }
    
    public void setSituacaoIcone(SituacaoIconeEnum situacaoIcone) {
        this.situacaoIcone = situacaoIcone;
    }



    
    public String getCaminhoWebRepositorio() {
        return caminhoWebRepositorio;
    }



    
    public void setCaminhoWebRepositorio(String caminhoWebRepositorio) {
        this.caminhoWebRepositorio = caminhoWebRepositorio;
    }
    
    public String getIcone(){
        return getCaminhoWebRepositorio()+"/"+getCaminhoBase().replaceAll("\\\\", "/")+"/"+getNomeReal();
    }
    

}
