package relatorio.negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class EmpresasRelVO {

    private Integer codigoEmpresa;
    private String nomeEmpresa;
    private String cnpj;
    private String cidade;
    private String estado;
    private String telefoneEmpresa;
    private Date dataCadastro;
    private Date dataUltimoAcesso;
    private List<ContatoEmpresasRelVO> listaContatoEmpresasRelVO;

    public String getNomeEmpresa() {
        if (nomeEmpresa == null) {
            nomeEmpresa = "";
        }
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getCnpj() {
        if (cnpj == null) {
            cnpj = "";
        }
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    public String getEstado() {
        if (estado == null) {
            estado = "";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCodigoEmpresa() {
        if (codigoEmpresa == null) {
            codigoEmpresa = 0;
        }
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(Integer codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public List<ContatoEmpresasRelVO> getListaContatoEmpresasRelVO() {
        if (listaContatoEmpresasRelVO == null) {
            listaContatoEmpresasRelVO = new ArrayList<ContatoEmpresasRelVO>(0);
        }
        return listaContatoEmpresasRelVO;
    }

    public void setListaContatoEmpresasRelVO(List<ContatoEmpresasRelVO> listaContatoEmpresasRelVO) {
        this.listaContatoEmpresasRelVO = listaContatoEmpresasRelVO;
    }

    public JRDataSource getListaContatoEmpresasRelVOJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaContatoEmpresasRelVO().toArray());
        return jr;
    }

    public String getTelefoneEmpresa() {
        if (telefoneEmpresa == null) {
            telefoneEmpresa = "";
        }
        return telefoneEmpresa;
    }

    public void setTelefoneEmpresa(String telefoneEmpresa) {
        this.telefoneEmpresa = telefoneEmpresa;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
     * @return the dataUltimoAcesso
     */
    public Date getDataUltimoAcesso() {
        return dataUltimoAcesso;
    }

    /**
     * @param dataUltimoAcesso the dataUltimoAcesso to set
     */
    public void setDataUltimoAcesso(Date dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }
}
