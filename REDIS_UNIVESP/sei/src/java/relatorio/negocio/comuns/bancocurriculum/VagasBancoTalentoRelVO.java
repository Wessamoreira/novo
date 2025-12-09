package relatorio.negocio.comuns.bancocurriculum;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.bancocurriculum.VagaContatoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class VagasBancoTalentoRelVO {

    private Integer codigoVaga;
    private Integer numeroVagas;
    private String vaga;
    private Date dataInicio;
    private Date dataFim;
    private String areaProfissional;
    private String estado;
    private String cidade;
    private Integer qtdCandidatos;
    private Integer duracaoVaga;
    private Integer tempoVeiculada;
    private Integer tempoRestante;
    private List<VagaContatoVO> listaVagaContatoVOs;
    
    public JRDataSource getListaResponsavelJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaVagaContatoVOs().toArray());
        return jr;
    }

    public String getVaga() {
        if (vaga == null) {
            vaga = "";
        }
        return vaga;
    }

    public void setVaga(String vaga) {
        this.vaga = vaga;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getQtdCandidatos() {
        if (qtdCandidatos == null) {
            qtdCandidatos = 0;
        }
        return qtdCandidatos;
    }

    public void setQtdCandidatos(Integer qtdCandidatos) {
        this.qtdCandidatos = qtdCandidatos;
    }

    public Integer getDuracaoVaga() {
        if (duracaoVaga == null) {
            duracaoVaga = 0;
        }
        return duracaoVaga;
    }

    public void setDuracaoVaga(Integer duracaoVaga) {
        this.duracaoVaga = duracaoVaga;
    }

    public Integer getTempoVeiculada() {
        if (tempoVeiculada == null) {
            tempoVeiculada = 0;
        }
        return tempoVeiculada;
    }

    public void setTempoVeiculada(Integer tempoVeiculada) {
        this.tempoVeiculada = tempoVeiculada;
    }

    public Integer getTempoRestante() {
        if (tempoRestante == null) {
            tempoRestante = 0;
        }
        return tempoRestante;
    }

    public void setTempoRestante(Integer tempoRestante) {
        this.tempoRestante = tempoRestante;
    }

    public String getAreaProfissional() {
        if (areaProfissional == null) {
            areaProfissional = "";
        }
        return areaProfissional;
    }

    public void setAreaProfissional(String areaProfissional) {
        this.areaProfissional = areaProfissional;
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

    public String getCidade() {
        if (cidade == null) {
            cidade = "";
        }
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Integer getCodigoVaga() {
        if (codigoVaga == null) {
            codigoVaga = 0;
        }
        return codigoVaga;
    }

    public void setCodigoVaga(Integer codigoVaga) {
        this.codigoVaga = codigoVaga;
    }

    
    public Integer getNumeroVagas() {
        if(numeroVagas == null){
            numeroVagas = 0;
        }
        return numeroVagas;
    }

    
    public void setNumeroVagas(Integer numeroVagas) {
        this.numeroVagas = numeroVagas;
    }

	public List<VagaContatoVO> getListaVagaContatoVOs() {
		if (listaVagaContatoVOs == null) {
			listaVagaContatoVOs = new ArrayList<VagaContatoVO>(0);
		}
		return listaVagaContatoVOs;
	}

	public void setListaVagaContatoVOs(List<VagaContatoVO> listaVagaContatoVOs) {
		this.listaVagaContatoVOs = listaVagaContatoVOs;
	}
    
    

}
