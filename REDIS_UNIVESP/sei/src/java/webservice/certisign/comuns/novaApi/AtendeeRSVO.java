package webservice.certisign.comuns.novaApi;

import com.google.gson.annotations.SerializedName;
import negocio.comuns.utilitarias.Uteis;

import java.text.ParseException;
import java.util.Date;

public class AtendeeRSVO {

    @SerializedName("id")
    private Integer codigo;

    @SerializedName("user")
    private UserRSVO usuario;

    private Integer status;

    private String link;

    @SerializedName("date")
    private String dataAssinatura;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public UserRSVO getUsuario() {
        return usuario;
    }

    public void setUsuario(UserRSVO usuario) {
        this.usuario = usuario;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getDataAssinatura() throws ParseException {
        return Uteis.getData(dataAssinatura, "yyyy-MM-dd'T'HH:mm");
    }

    public void setDataAssinatura(String dataAssinatura) {
        this.dataAssinatura = dataAssinatura;
    }

    public StatusAssinaturaCertisign getStatusAssinaturaCertisign() {
        return StatusAssinaturaCertisign.getFromCodigo(status);
    }

}
