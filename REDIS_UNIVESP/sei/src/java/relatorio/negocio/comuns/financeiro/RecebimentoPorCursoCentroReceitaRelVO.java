package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;


public class RecebimentoPorCursoCentroReceitaRelVO {

        private CursoVO curso;
	private String tipoOrigem;
	private String mesCompetencia;
        private String anoCompetencia;
        private Integer nrTitulos;
	private Double totalValorBase;
	private Double totalDescontoInstitucional;
        private Double totalValorBaseDeduzidoDescontoInstitucional;
        private Double totalDescontoProgressivo;
        private Double totalDescontoConvenio;
        private Double totalValorCusteadoConvenio;
        private Double totalDescontoRecebimento;
        private Double totalJurosMultasAcrescimos;
        private Double totalValorRecebidoAteDataBaseFaturamento;
        private Double totalValorAReceberDataBaseFaturamento;
        private Double totalValorRenegociadoAteDataBaseFaturamento;
        private Double totalValorRenegociadoAposDataBaseFaturamento;
        
        private Date dataInicio;
	private Date dataFim;
	private String nomeUnidadeEnsino;
	private Boolean tipoOrigemInscricaoProcessoSeletivo;
	private Boolean tipoOrigemMatricula;
	private Boolean tipoOrigemRequerimento;
	private Boolean tipoOrigemBiblioteca;
	private Boolean tipoOrigemMensalidade;
	private Boolean tipoOrigemDevolucaoCheque;
	private Boolean tipoOrigemNegociacao;
	private Boolean tipoOrigemBolsaCusteadaConvenio;
	private Boolean tipoOrigemContratoReceita;
	private Boolean tipoOrigemOutros;
	private Boolean tipoOrigemInclusaoReposicao;
	private List<RecebimentoPorCursoCentroReceitaRelVO> listaQuadroResumo;
	private String formaPagamento;

	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = "";
		}
		return tipoOrigem;
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
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

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public Boolean getTipoOrigemInscricaoProcessoSeletivo() {
		if (tipoOrigemInscricaoProcessoSeletivo == null) {
			tipoOrigemInscricaoProcessoSeletivo = false;
		}
		return tipoOrigemInscricaoProcessoSeletivo;
	}

	public void setTipoOrigemInscricaoProcessoSeletivo(Boolean tipoOrigemInscricaoProcessoSeletivo) {
		this.tipoOrigemInscricaoProcessoSeletivo = tipoOrigemInscricaoProcessoSeletivo;
	}

	public Boolean getTipoOrigemMatricula() {
		if (tipoOrigemMatricula == null) {
			tipoOrigemMatricula = true;
		}
		return tipoOrigemMatricula;
	}

	public void setTipoOrigemMatricula(Boolean tipoOrigemMatricula) {
		this.tipoOrigemMatricula = tipoOrigemMatricula;
	}

	public Boolean getTipoOrigemRequerimento() {
		if (tipoOrigemRequerimento == null) {
			tipoOrigemRequerimento = false;
		}
		return tipoOrigemRequerimento;
	}

	public void setTipoOrigemRequerimento(Boolean tipoOrigemRequerimento) {
		this.tipoOrigemRequerimento = tipoOrigemRequerimento;
	}

	public Boolean getTipoOrigemBiblioteca() {
		if (tipoOrigemBiblioteca == null) {
			tipoOrigemBiblioteca = false;
		}
		return tipoOrigemBiblioteca;
	}

	public void setTipoOrigemBiblioteca(Boolean tipoOrigemBiblioteca) {
		this.tipoOrigemBiblioteca = tipoOrigemBiblioteca;
	}

	public Boolean getTipoOrigemMensalidade() {
		if (tipoOrigemMensalidade == null) {
			tipoOrigemMensalidade = true;
		}
		return tipoOrigemMensalidade;
	}

	public void setTipoOrigemMensalidade(Boolean tipoOrigemMensalidade) {
		this.tipoOrigemMensalidade = tipoOrigemMensalidade;
	}

	public Boolean getTipoOrigemDevolucaoCheque() {
		if (tipoOrigemDevolucaoCheque == null) {
			tipoOrigemDevolucaoCheque = false;
		}
		return tipoOrigemDevolucaoCheque;
	}

	public void setTipoOrigemDevolucaoCheque(Boolean tipoOrigemDevolucaoCheque) {
		this.tipoOrigemDevolucaoCheque = tipoOrigemDevolucaoCheque;
	}

	public Boolean getTipoOrigemNegociacao() {
		if (tipoOrigemNegociacao == null) {
			tipoOrigemNegociacao = false;
		}
		return tipoOrigemNegociacao;
	}

	public void setTipoOrigemNegociacao(Boolean tipoOrigemNegociacao) {
		this.tipoOrigemNegociacao = tipoOrigemNegociacao;
	}

	public Boolean getTipoOrigemBolsaCusteadaConvenio() {
		if (tipoOrigemBolsaCusteadaConvenio == null) {
			tipoOrigemBolsaCusteadaConvenio = false;
		}
		return tipoOrigemBolsaCusteadaConvenio;
	}

	public void setTipoOrigemBolsaCusteadaConvenio(Boolean tipoOrigemBolsaCusteadaConvenio) {
		this.tipoOrigemBolsaCusteadaConvenio = tipoOrigemBolsaCusteadaConvenio;
	}

	public Boolean getTipoOrigemContratoReceita() {
		if (tipoOrigemContratoReceita == null) {
			tipoOrigemContratoReceita = false;
		}
		return tipoOrigemContratoReceita;
	}

	public void setTipoOrigemContratoReceita(Boolean tipoOrigemContratoReceita) {
		this.tipoOrigemContratoReceita = tipoOrigemContratoReceita;
	}

	public Boolean getTipoOrigemOutros() {
		if (tipoOrigemOutros == null) {
			tipoOrigemOutros = false;
		}
		return tipoOrigemOutros;
	}

	public void setTipoOrigemOutros(Boolean tipoOrigemOutros) {
		this.tipoOrigemOutros = tipoOrigemOutros;
	}

	public Boolean getTipoOrigemInclusaoReposicao() {
		if (tipoOrigemInclusaoReposicao == null) {
			tipoOrigemInclusaoReposicao = false;
		}
		return tipoOrigemInclusaoReposicao;
	}

	public void setTipoOrigemInclusaoReposicao(Boolean tipoOrigemInclusaoReposicao) {
		this.tipoOrigemInclusaoReposicao = tipoOrigemInclusaoReposicao;
	}

    /**
     * @return the curso
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    /**
     * @return the mesCompetencia
     */
    public String getMesCompetencia() {
        if (mesCompetencia == null) {
            mesCompetencia = "";
        }
        return mesCompetencia;
    }

    /**
     * @param mesCompetencia the mesCompetencia to set
     */
    public void setMesCompetencia(String mesCompetencia) {
        this.mesCompetencia = mesCompetencia;
    }

    /**
     * @return the anoCompetencia
     */
    public String getAnoCompetencia() {
        if (anoCompetencia == null) {
            anoCompetencia = "";
        }
        return anoCompetencia;
    }

    /**
     * @param anoCompetencia the anoCompetencia to set
     */
    public void setAnoCompetencia(String anoCompetencia) {
        this.anoCompetencia = anoCompetencia;
    }

    /**
     * @return the nrTitulos
     */
    public Integer getNrTitulos() {
        if (nrTitulos == null) {
            nrTitulos = 0;
        }
        return nrTitulos;
    }

    /**
     * @param nrTitulos the nrTitulos to set
     */
    public void setNrTitulos(Integer nrTitulos) {
        this.nrTitulos = nrTitulos;
    }

    /**
     * @return the totalValorBase
     */
    public Double getTotalValorBase() {
        if (totalValorBase == null) {
            totalValorBase = 0.0;
        }
        return totalValorBase;
    }

    /**
     * @param totalValorBase the totalValorBase to set
     */
    public void setTotalValorBase(Double totalValorBase) {
        this.totalValorBase = totalValorBase;
    }

    /**
     * @return the totalDescontoInstitucional
     */
    public Double getTotalDescontoInstitucional() {
        if (totalDescontoInstitucional == null) {
            totalDescontoInstitucional = 0.0;
        }
        return totalDescontoInstitucional;
    }

    /**
     * @param totalDescontoInstitucional the totalDescontoInstitucional to set
     */
    public void setTotalDescontoInstitucional(Double totalDescontoInstitucional) {
        this.totalDescontoInstitucional = totalDescontoInstitucional;
    }

    /**
     * @return the totalValorBaseDeduzidoDescontoInstitucional
     */
    public Double getTotalValorBaseDeduzidoDescontoInstitucional() {
        if (totalValorBaseDeduzidoDescontoInstitucional == null) {
            totalValorBaseDeduzidoDescontoInstitucional = 0.0;
        }
        return totalValorBaseDeduzidoDescontoInstitucional;
    }

    /**
     * @param totalValorBaseDeduzidoDescontoInstitucional the totalValorBaseDeduzidoDescontoInstitucional to set
     */
    public void setTotalValorBaseDeduzidoDescontoInstitucional(Double totalValorBaseDeduzidoDescontoInstitucional) {
        this.totalValorBaseDeduzidoDescontoInstitucional = totalValorBaseDeduzidoDescontoInstitucional;
    }

    /**
     * @return the totalJurosMultasAcrescimos
     */
    public Double getTotalJurosMultasAcrescimos() {
        if (totalJurosMultasAcrescimos == null) {
            totalJurosMultasAcrescimos = 0.0;
        }
        return totalJurosMultasAcrescimos;
    }

    /**
     * @param totalJurosMultasAcrescimos the totalJurosMultasAcrescimos to set
     */
    public void setTotalJurosMultasAcrescimos(Double totalJurosMultasAcrescimos) {
        this.totalJurosMultasAcrescimos = totalJurosMultasAcrescimos;
    }

    /**
     * @return the totalDescontoProgressivo
     */
    public Double getTotalDescontoProgressivo() {
        if (totalDescontoProgressivo == null) {
            totalDescontoProgressivo = 0.0;
        }
        return totalDescontoProgressivo;
    }

    /**
     * @param totalDescontoProgressivo the totalDescontoProgressivo to set
     */
    public void setTotalDescontoProgressivo(Double totalDescontoProgressivo) {
        this.totalDescontoProgressivo = totalDescontoProgressivo;
    }

    /**
     * @return the totalDescontoConvenio
     */
    public Double getTotalDescontoConvenio() {
        if (totalDescontoConvenio == null) {
            totalDescontoConvenio = 0.0;
        }
        return totalDescontoConvenio;
    }

    /**
     * @param totalDescontoConvenio the totalDescontoConvenio to set
     */
    public void setTotalDescontoConvenio(Double totalDescontoConvenio) {
        this.totalDescontoConvenio = totalDescontoConvenio;
    }

    /**
     * @return the totalValorCusteadoConvenio
     */
    public Double getTotalValorCusteadoConvenio() {
        if (totalValorCusteadoConvenio == null) {
            totalValorCusteadoConvenio = 0.0;
        }
        return totalValorCusteadoConvenio;
    }

    /**
     * @param totalValorCusteadoConvenio the totalValorCusteadoConvenio to set
     */
    public void setTotalValorCusteadoConvenio(Double totalValorCusteadoConvenio) {
        this.totalValorCusteadoConvenio = totalValorCusteadoConvenio;
    }

    /**
     * @return the totalDescontoRecebimento
     */
    public Double getTotalDescontoRecebimento() {
        if (totalDescontoRecebimento == null) {
            totalDescontoRecebimento = 0.0;
        }
        return totalDescontoRecebimento;
    }

    /**
     * @param totalDescontoRecebimento the totalDescontoRecebimento to set
     */
    public void setTotalDescontoRecebimento(Double totalDescontoRecebimento) {
        this.totalDescontoRecebimento = totalDescontoRecebimento;
    }

    /**
     * @return the totalValorRecebidoAteDataBaseFaturamento
     */
    public Double getTotalValorRecebidoAteDataBaseFaturamento() {
        if (totalValorRecebidoAteDataBaseFaturamento == null) {
            totalValorRecebidoAteDataBaseFaturamento = 0.0;
        }
        return totalValorRecebidoAteDataBaseFaturamento;
    }

    /**
     * @param totalValorRecebidoAteDataBaseFaturamento the totalValorRecebidoAteDataBaseFaturamento to set
     */
    public void setTotalValorRecebidoAteDataBaseFaturamento(Double totalValorRecebidoAteDataBaseFaturamento) {
        this.totalValorRecebidoAteDataBaseFaturamento = totalValorRecebidoAteDataBaseFaturamento;
    }

    /**
     * @return the totalValorAReceberDataBaseFaturamento
     */
    public Double getTotalValorAReceberDataBaseFaturamento() {
        if (totalValorAReceberDataBaseFaturamento == null) {
            totalValorAReceberDataBaseFaturamento = 0.0;
        }
        return totalValorAReceberDataBaseFaturamento;
    }

    /**
     * @param totalValorAReceberDataBaseFaturamento the totalValorAReceberDataBaseFaturamento to set
     */
    public void setTotalValorAReceberDataBaseFaturamento(Double totalValorAReceberDataBaseFaturamento) {
        this.totalValorAReceberDataBaseFaturamento = totalValorAReceberDataBaseFaturamento;
    }

    /**
     * @return the totalValorRenegociadoAteDataBaseFaturamento
     */
    public Double getTotalValorRenegociadoAteDataBaseFaturamento() {
        if (totalValorRenegociadoAteDataBaseFaturamento == null) {
            totalValorRenegociadoAteDataBaseFaturamento = 0.0;
        }
        return totalValorRenegociadoAteDataBaseFaturamento;
    }

    /**
     * @param totalValorRenegociadoAteDataBaseFaturamento the totalValorRenegociadoAteDataBaseFaturamento to set
     */
    public void setTotalValorRenegociadoAteDataBaseFaturamento(Double totalValorRenegociadoAteDataBaseFaturamento) {
        this.totalValorRenegociadoAteDataBaseFaturamento = totalValorRenegociadoAteDataBaseFaturamento;
    }

    /**
     * @return the totalValorRenegociadoAposDataBaseFaturamento
     */
    public Double getTotalValorRenegociadoAposDataBaseFaturamento() {
        if (totalValorRenegociadoAposDataBaseFaturamento == null) {
            totalValorRenegociadoAposDataBaseFaturamento = 0.0;
        }
        return totalValorRenegociadoAposDataBaseFaturamento;
    }

    /**
     * @param totalValorRenegociadoAposDataBaseFaturamento the totalValorRenegociadoAposDataBaseFaturamento to set
     */
    public void setTotalValorRenegociadoAposDataBaseFaturamento(Double totalValorRenegociadoAposDataBaseFaturamento) {
        this.totalValorRenegociadoAposDataBaseFaturamento = totalValorRenegociadoAposDataBaseFaturamento;
    }
    
    public String getNomeCurso() {
        return this.getCurso().getNome();
    }
    
    public Integer getCodigoCurso() {
        return this.getCurso().getCodigo();
    }
	public List<RecebimentoPorCursoCentroReceitaRelVO> getListaQuadroResumo() {
		return listaQuadroResumo;
	}

	public void setListaQuadroResumo(List<RecebimentoPorCursoCentroReceitaRelVO> listaQuadroResumo) {
		this.listaQuadroResumo = listaQuadroResumo;
	}

	public JRDataSource getQuadroResumo() {
		if (getListaQuadroResumo() != null) {
			return new JRBeanArrayDataSource(getListaQuadroResumo().toArray());
		} else {
			return new JRBeanArrayDataSource(new ArrayList().toArray());
		}
    }

	public String getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = "";
		}
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

}