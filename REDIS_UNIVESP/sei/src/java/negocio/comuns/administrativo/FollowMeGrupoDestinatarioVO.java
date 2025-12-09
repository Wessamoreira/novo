package negocio.comuns.administrativo;


import java.util.Date;

import negocio.comuns.administrativo.enumeradores.FrequenciaEnvioFollowMeEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;


public class FollowMeGrupoDestinatarioVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = 9075504467432614044L;
    private Integer codigo;
    private FrequenciaEnvioFollowMeEnum frequenciaEnvioFollowMeEnum;
    private DiaSemana diaSemana;
    private Integer diaDoMes;
    private Date diaMesEspecifico;
    private GrupoDestinatariosVO grupoDestinatario;
    private FollowMeVO followMe;
    private int ordem = 0;
    
    public FollowMeGrupoDestinatarioVO clone() throws CloneNotSupportedException{
        FollowMeGrupoDestinatarioVO clone = (FollowMeGrupoDestinatarioVO) super.clone();
        clone.setCodigo(0);
        clone.setFollowMe(new FollowMeVO());
        clone.setNovoObj(true);
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
    
    public FrequenciaEnvioFollowMeEnum getFrequenciaEnvioFollowMeEnum() {
        if(frequenciaEnvioFollowMeEnum == null){
            frequenciaEnvioFollowMeEnum = FrequenciaEnvioFollowMeEnum.MENSAL;
        }
        return frequenciaEnvioFollowMeEnum;
    }
    
    public void setFrequenciaEnvioFollowMeEnum(FrequenciaEnvioFollowMeEnum frequenciaEnvioFollowMeEnum) {
        this.frequenciaEnvioFollowMeEnum = frequenciaEnvioFollowMeEnum;
    }
    
    public DiaSemana getDiaSemana() {        
        return diaSemana;
    }
    
    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }
    
    public Integer getDiaDoMes() {        
        return diaDoMes;
    }
    
    public void setDiaDoMes(Integer diaDoMes) {
        this.diaDoMes = diaDoMes;
    }
    
    public Date getDiaMesEspecifico() {
        return diaMesEspecifico;
    }
    
    public void setDiaMesEspecifico(Date diaMesEspecifico) {
        this.diaMesEspecifico = diaMesEspecifico;
    }
    
    public GrupoDestinatariosVO getGrupoDestinatario() {
        if(grupoDestinatario == null){
            grupoDestinatario = new GrupoDestinatariosVO();
        }
        return grupoDestinatario;
    }
    
    public void setGrupoDestinatario(GrupoDestinatariosVO grupoDestinatariosVO) {
        this.grupoDestinatario = grupoDestinatariosVO;
    }
    
    public FollowMeVO getFollowMe() {
        if(followMe == null){
            followMe =new FollowMeVO();
        }
        return followMe;
    }
    
    public void setFollowMe(FollowMeVO followMeVO) {
        this.followMe = followMeVO;
    }

    
    public int getOrdem() {
        return ordem;
    }

    
    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }
    
    public Boolean getIsSemanal(){
        return getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.SEMANAL);
    }
    
    public Boolean getIsMensal(){
        return getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.MENSAL);
    }
    
    public Boolean getIsDataEspecifica(){
        return getFrequenciaEnvioFollowMeEnum().equals(FrequenciaEnvioFollowMeEnum.DATA_ESPECIFICA);
    }
    
    public String getDiaFrequenciaApresentar(){
        if(getIsMensal()){
            return getDiaDoMes().toString();
        }
        if(getIsSemanal()){
            return getDiaSemana().getDescricao();
        }
        
        return Uteis.getDataAplicandoFormatacao(getDiaMesEspecifico(), "dd/MM");
    }

}
