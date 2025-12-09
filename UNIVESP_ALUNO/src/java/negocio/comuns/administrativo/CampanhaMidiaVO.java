/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Philippe
 */
public class CampanhaMidiaVO extends SuperVO {
    private Integer codigo;
    private TipoMidiaCaptacaoVO tipoMidia;
    private Integer impactoEsperado;
    private Date dataInicioVinculacao;
    private Date dataFimVinculacao;
    private Boolean apresentarPreInscricao;
    private CampanhaVO campanha;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }

        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TipoMidiaCaptacaoVO getTipoMidia() {
        if (tipoMidia == null) {
            tipoMidia = new TipoMidiaCaptacaoVO();
        }
        return tipoMidia;
    }

    public void setTipoMidia(TipoMidiaCaptacaoVO tipoMidia) {
        this.tipoMidia = tipoMidia;
    }

    public Integer getImpactoEsperado() {
        if (impactoEsperado == null) {
            impactoEsperado = 0;
        }
        return impactoEsperado;
    }

    public void setImpactoEsperado(Integer impactoEsperado) {
        this.impactoEsperado = impactoEsperado;
    }

    public Date getDataInicioVinculacao() {
        if(dataInicioVinculacao == null) {
            dataInicioVinculacao = new Date();
        }
        return dataInicioVinculacao;
    }

    public void setDataInicioVinculacao(Date dataInicioVinculacao) {
        this.dataInicioVinculacao = dataInicioVinculacao;
    }

    public Date getDataFimVinculacao() {
        if(dataFimVinculacao == null) {
            dataFimVinculacao = new Date();
        }
        return dataFimVinculacao;
    }

    public void setDataFimVinculacao(Date dataFimVinculacao) {
        this.dataFimVinculacao = dataFimVinculacao;
    }

    public Boolean getApresentarPreInscricao() {
        if (apresentarPreInscricao == null) {
            apresentarPreInscricao = Boolean.FALSE;
        }
        return apresentarPreInscricao;
    }

    public void setApresentarPreInscricao(Boolean apresentarPreInscricao) {
        this.apresentarPreInscricao = apresentarPreInscricao;
    }

    public CampanhaVO getCampanha() {
        if (campanha == null) {
            campanha = new CampanhaVO();
        }
        return campanha;
    }

    public void setCampanha(CampanhaVO campanha) {
        this.campanha = campanha;
    }

    public String getApresentarPreInscricao_Apresentar() {
        if (apresentarPreInscricao) {
            return "Sim";
        }
        return "Não";
    }

    public String getDataInicioVinculacao_Apresentar() {
        return (Uteis.getData(dataInicioVinculacao));
    }

    public String getDataFimVinculacao_Apresentar() {
        return (Uteis.getData(dataFimVinculacao));
    }
}
