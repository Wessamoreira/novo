package webservice.certisign.comuns.novaApi;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StepRSVO {

    @SerializedName("id")
    private Integer codigo;

    @SerializedName("order")
    private Integer ordem;

    @SerializedName("status")
    private Integer status;

    @SerializedName("attendees")
    private List<AtendeeRSVO> participantes;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AtendeeRSVO> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<AtendeeRSVO> participantes) {
        this.participantes = participantes;
    }
}
