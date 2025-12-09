/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author edigarjr
 */
public class MapaControleAtividadesUsuarioVO {
    private UsuarioVO usuarioVO;
    private String tipoUsuario;
    private List<ControleAtividadeUsuarioVO> atividadesUsuario;
    private String idSessao;
    private Date dataInicioAtividade;
    private Boolean usuarioAtivo;

    /**
     * @return the usuarioVO
     */
    public UsuarioVO getUsuarioVO() {
        return usuarioVO;
    }

    /**
     * @param usuarioVO the usuarioVO to set
     */
    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    /**
     * @return the passosUsuario
     */
    public List<ControleAtividadeUsuarioVO> getAtividadesUsuario() {
        if (atividadesUsuario == null) {
            atividadesUsuario = new ArrayList<ControleAtividadeUsuarioVO>(0);
        }
        return atividadesUsuario;
    }

    /**
     * @param passosUsuario the passosUsuario to set
     */
    public void setAtividadesUsuario(List<ControleAtividadeUsuarioVO> atividadeUsuario) {
        this.atividadesUsuario = atividadeUsuario;
    }

    /**
     * @return the idSessao
     */
    public String getIdSessao() {
        return idSessao;
    }

    /**
     * @param idSessao the idSessao to set
     */
    public void setIdSessao(String idSessao) {
        this.idSessao = idSessao;
    }
    
    public void registrarNovaAtividade(String username, String entidade, String descricao, String tipo) {
        ControleAtividadeUsuarioVO novoRegistro = new ControleAtividadeUsuarioVO();
        novoRegistro.setUsername(username);
        novoRegistro.setDescricao(descricao);
        novoRegistro.setEntidade(entidade);
        novoRegistro.setTipo(tipo);
        novoRegistro.setMomento(new Date());
        getAtividadesUsuario().add(novoRegistro);
    }
    
    public String getDataInicioAtividade_Apresentar() {
        return Uteis.getData(dataInicioAtividade);
    }

    public String getHoraDataInicioAtividade_Apresentar() {
        return Uteis.getHoraMinutoComMascara(dataInicioAtividade);
    }

    /**
     * @return the dataInicioAtividade
     */
    public Date getDataInicioAtividade() {
        if (dataInicioAtividade == null) {
            dataInicioAtividade = new Date();
        }
        return dataInicioAtividade;
    }

    /**
     * @param dataInicioAtividade the dataInicioAtividade to set
     */
    public void setDataInicioAtividade(Date dataInicioAtividade) {
        this.dataInicioAtividade = dataInicioAtividade;
    }

    /**
     * @return the usuarioAtivo
     */
    public Boolean getUsuarioAtivo() {
        if (usuarioAtivo == null) {
            usuarioAtivo = Boolean.TRUE;
        }
        return usuarioAtivo;
    }

    /**
     * @param usuarioAtivo the usuarioAtivo to set
     */
    public void setUsuarioAtivo(Boolean usuarioAtivo) {
        this.usuarioAtivo = usuarioAtivo;
    }
    
}
