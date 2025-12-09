package relatorio.negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class EmpresaBancoTalentoRelVO {

    private String estado;
    private String cidade;
    private Integer codigoempresa;
    private String empresa;
    private String contato;
    private List<ContatoEmpresaBancoTalentoRelVO> listaContatos;
    private List<VagasBancoTalentoRelVO> listaVagas;

    public String getEstado() {
        if (estado == null) {
            estado = "";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        if (cidade == null) {
            cidade = "";
        }
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Integer getCodigoempresa() {
        if (codigoempresa == null) {
            codigoempresa = 0;
        }
        return codigoempresa;
    }

    public void setCodigoempresa(Integer codigoempresa) {
        this.codigoempresa = codigoempresa;
    }

    public String getEmpresa() {
        if (empresa == null) {
            empresa = "";
        }
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getContato() {
        if (contato == null) {
            contato = "";
        }
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public List<VagasBancoTalentoRelVO> getListaVagas() {
        if (listaVagas == null) {
            listaVagas = new ArrayList(0);
        }
        return listaVagas;
    }

    public void setListaVagas(List<VagasBancoTalentoRelVO> listaVagas) {
        this.listaVagas = listaVagas;
    }

    public JRDataSource getListaVagasJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaVagas().toArray());
        return jr;
    }

    public JRDataSource getListaContatosJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaContatos().toArray());
        return jr;
    }

    public List<ContatoEmpresaBancoTalentoRelVO> getListaContatos() {
        if (listaContatos == null) {
            listaContatos = new ArrayList<ContatoEmpresaBancoTalentoRelVO>(0);
        }
        return listaContatos;
    }

    public void setListaContatos(List<ContatoEmpresaBancoTalentoRelVO> listaContatos) {
        this.listaContatos = listaContatos;
    }
}
