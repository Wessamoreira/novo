package negocio.comuns.crm;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.enumerador.NivelExperienciaCargoEnum;


public class MetaItemVO extends SuperVO{
    private Integer codigo;
    private MetaVO meta;
    private Long quantidadeContatos;
    private Long quantidadeFinalizadoSucesso;
    private Long quantidadeMetaCaptacaoProspect;
    private NivelExperienciaCargoEnum nivelExperiencia;
    private Boolean padrao;

    public Long getQuantidadeContatos() {
        if (quantidadeContatos == null) {
            quantidadeContatos = 0L;
        }
        return quantidadeContatos;
    }

    public void setQuantidadeContatos(Long quantidadeContatos) {
        this.quantidadeContatos = quantidadeContatos;
    }

    public Long getQuantidadeFinalizadoSucesso() {
        if (quantidadeFinalizadoSucesso == null) {
            quantidadeFinalizadoSucesso = 0L;
        }
        return quantidadeFinalizadoSucesso;
    }

    public void setQuantidadeFinalizadoSucesso(Long quantidadeFinalizadoSucesso) {
        this.quantidadeFinalizadoSucesso = quantidadeFinalizadoSucesso;
    }

    public Long getQuantidadeMetaCaptacaoProspect() {
        if (quantidadeMetaCaptacaoProspect == null) {
            quantidadeMetaCaptacaoProspect = 0L;
        }
        return quantidadeMetaCaptacaoProspect;
    }

    public void setQuantidadeMetaCaptacaoProspect(Long quantidadeMetaCaptacaoProspect) {
        this.quantidadeMetaCaptacaoProspect = quantidadeMetaCaptacaoProspect;
    }

    public NivelExperienciaCargoEnum getNivelExperiencia() {
        if (nivelExperiencia == null) {
            nivelExperiencia = NivelExperienciaCargoEnum.NENHUM;
        }
        return nivelExperiencia;
    }

    public void setNivelExperiencia(NivelExperienciaCargoEnum nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public Boolean getPadrao() {
        if (padrao == null) {
            padrao = Boolean.FALSE;
        }
        return padrao;
    }

    public void setPadrao(Boolean padrao) {
        this.padrao = padrao;
    }

    public MetaVO getMeta() {
        if (meta == null) {
            meta = new MetaVO();
        }
        return meta;
    }

    public void setMeta(MetaVO meta) {
        this.meta = meta;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
}
