package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class GrupoDestinatariosVO extends SuperVO {

    private Integer codigo;
    private Date dataCadastro;
    private UsuarioVO responsavelCadastro;
    private String nomeGrupo;
    private List<FuncionarioGrupoDestinatariosVO> listaFuncionariosGrupoDestinatariosVOs;
    public static final long serialVersionUID = 1L;

    public GrupoDestinatariosVO() {
        super();
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

    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return dataCadastro;
    }

    public String getDataCadastro_Apresentar() {
        if (dataCadastro == null) {
            return "";
        }
        return (Uteis.getData(dataCadastro));
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public UsuarioVO getResponsavelCadastro() {
        if (responsavelCadastro == null) {
            responsavelCadastro = new UsuarioVO();
        }
        return responsavelCadastro;
    }

    public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
        this.responsavelCadastro = responsavelCadastro;
    }

    public String getNomeGrupo() {
        if (nomeGrupo == null) {
            nomeGrupo = "";
        }
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    public void setListaFuncionariosGrupoDestinatariosVOs(List<FuncionarioGrupoDestinatariosVO> listaFuncionariosGrupoDestinatariosVOs) {
        this.listaFuncionariosGrupoDestinatariosVOs = listaFuncionariosGrupoDestinatariosVOs;
    }

    public List<FuncionarioGrupoDestinatariosVO> getListaFuncionariosGrupoDestinatariosVOs() {
        if (listaFuncionariosGrupoDestinatariosVOs == null) {
            listaFuncionariosGrupoDestinatariosVOs = new ArrayList<FuncionarioGrupoDestinatariosVO>(0);
        }
        return listaFuncionariosGrupoDestinatariosVOs;
    }
}
