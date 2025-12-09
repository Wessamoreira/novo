package negocio.comuns.academico;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.TipoNotaConceitoEnum;
import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement(name = "configuracaoAcademicoNotaConceitoVO")
public class ConfiguracaoAcademicoNotaConceitoVO extends SuperVO {

    private Integer codigo;
    private Integer configuracaoAcademico;
    private TipoNotaConceitoEnum tipoNotaConceito;
    private String conceitoNota;
    private String situacao;
    private String abreviaturaConceitoNota;
    private Double faixaNota1;
    private Double faixaNota2;
    
    public ConfiguracaoAcademicoNotaConceitoVO clone(){
        try {
            return (ConfiguracaoAcademicoNotaConceitoVO) super.clone();
        } catch (CloneNotSupportedException e) {
           return new ConfiguracaoAcademicoNotaConceitoVO();
        }
    }
    
    @XmlElement(name = "configuracaoAcademico")
    public Integer getConfiguracaoAcademico() {
        if(configuracaoAcademico == null){
            configuracaoAcademico = 0;
        }
        return configuracaoAcademico;
    }
    
    public void setConfiguracaoAcademico(Integer configuracaoAcademico) {
        this.configuracaoAcademico = configuracaoAcademico;
    }
    
    @XmlElement(name = "tipoNotaConceito")
    public TipoNotaConceitoEnum getTipoNotaConceito() {
        if(tipoNotaConceito == null){
            tipoNotaConceito = TipoNotaConceitoEnum.NOTA_1;
        }
        return tipoNotaConceito;
    }
    
    public void setTipoNotaConceito(TipoNotaConceitoEnum tipoNotaConceito) {
        this.tipoNotaConceito = tipoNotaConceito;
    }
    
    @XmlElement(name = "conceitoNota")
    public String getConceitoNota() {
        if(conceitoNota == null){
            conceitoNota = "";
        }
        return conceitoNota;
    }
    
    public void setConceitoNota(String conceitoNota) {
        this.conceitoNota = conceitoNota;
    }
    
    @XmlElement(name = "abreviaturaConceitoNota")
    public String getAbreviaturaConceitoNota() {
        if(abreviaturaConceitoNota == null){
            abreviaturaConceitoNota = "";
        }
        return abreviaturaConceitoNota;
    }
    
    public void setAbreviaturaConceitoNota(String abreviaturaConceitoNota) {
        this.abreviaturaConceitoNota = abreviaturaConceitoNota;
    }
    
    

    
    @XmlElement(name = "faixaNota1")
    public Double getFaixaNota1() {
        if(faixaNota1 == null){
            faixaNota1 = 0.0;
        }
        return faixaNota1;
    }

    
    public void setFaixaNota1(Double faixaNota1) {
        this.faixaNota1 = faixaNota1;
    }

    @XmlElement(name = "faixaNota2")
    public Double getFaixaNota2() {
        if(faixaNota2 == null){
            faixaNota2 = 0.0;
        }
        return faixaNota2;
    }

    
    public void setFaixaNota2(Double faixaNota2) {
        this.faixaNota2 = faixaNota2;
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

    
    @XmlElement(name = "situacao")
    public String getSituacao() {
        if(situacao == null){
            situacao = "";
        }
        return situacao;
    }

    
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
    
    
    
}
