/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Rodrigo
 */
public class HorarioTurmaLOGVO extends SuperVO {

    private Integer codigo;
    private UsuarioVO usuarioVO;
    private Date data;
    private String acao;
    private String resultadoAcao;
    private HorarioTurmaVO horarioTurma;
    public static final long serialVersionUID = 1L;

    public String getAcao() {
        if (acao == null) {
            acao = "";
        }
        return acao;
    }

    public void setAcao(String acao) {
        this.acao = acao;
    }

    public String getData_Apresentar() {
        if (data == null) {
            return "";
        }
        return (Uteis.getData(data));
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public HorarioTurmaVO getHorarioTurma() {
        if (horarioTurma == null) {
            horarioTurma = new HorarioTurmaVO();
        }
        return horarioTurma;
    }

    public void setHorarioTurma(HorarioTurmaVO horarioTurma) {
        this.horarioTurma = horarioTurma;
    }

    public String getResultadoAcao() {
        if (resultadoAcao == null) {
            resultadoAcao = "";
        }
        return resultadoAcao;
    }

    public void setResultadoAcao(String resultadoAcao) {
        this.resultadoAcao = resultadoAcao;
    }

    public UsuarioVO getUsuarioVO() {
        if (usuarioVO == null) {
            usuarioVO = null;
        }
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
