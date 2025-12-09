package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TrancamentoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TransferenciaSaidaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class OcorrenciasAlunosVO {

	private UnidadeEnsinoVO unidadeEnsino;
	private String tipoOcorrencia;
	private Date dataInicio;
	private Date dataFim;
	private MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO;
	private List<CancelamentoVO> cancelamentoVOs;
	private List<TrancamentoVO> trancamentoVOs;
	private List<TrancamentoVO> abandonoCursoVOs;
	private List<TransferenciaEntradaVO> transferenciaEntradaVOs;
	private List<TransferenciaEntradaVO> transferenciaInternaVOs;
	private List<TransferenciaSaidaVO> transferenciaSaidaVOs;
	private List<OcorrenciasAlunosVO> formadoVOs;
        private Integer qtdeCancelamento;
        private Integer qtdeTrancamento;
        private Integer qtdeTransferenciaEntrada;
        private Integer qtdeTransferenciaSaida;
        private Integer qtdeTransferenciaInterna;
        private Integer qtdeAbandonoCurso;
        private Integer qtdeFormado;
        private Integer qtdeTotal;
        private MatriculaVO matricula;
        private Date data;
        private String justificativa;
        private String turma;
        private UsuarioVO responsavelAutorizacao;

	public OcorrenciasAlunosVO() {

	}

	public JRDataSource getListaCancelamentoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getCancelamentoVOs().toArray());
		return jr;
	}
	public JRDataSource getListaAbandonoCursoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getAbandonoCursoVOs().toArray());
		return jr;
	}
	public JRDataSource getListaTransferenciaInternaVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getTransferenciaInternaVOs().toArray());
		return jr;
	}

	public JRDataSource getListaTrancamentoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getTrancamentoVOs().toArray());
		return jr;
	}

	public JRDataSource getListaTransferenciaEntradaVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getTransferenciaEntradaVOs().toArray());
		return jr;
	}

	public JRDataSource getListaTransferenciaSaidaVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getTransferenciaSaidaVOs().toArray());
		return jr;
	}
	
	public JRDataSource getListaFormadoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getFormadoVOs().toArray());
		return jr;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getTipoOcorrencia() {
		if (tipoOcorrencia == null) {
			tipoOcorrencia = "TD";
		}
		return tipoOcorrencia;
	}

	public void setTipoOcorrencia(String tipoOcorrencia) {
		this.tipoOcorrencia = tipoOcorrencia;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<CancelamentoVO> getCancelamentoVOs() {
		if (cancelamentoVOs == null) {
			cancelamentoVOs = new ArrayList<CancelamentoVO>(0);
		}
		return cancelamentoVOs;
	}

	public void setCancelamentoVOs(List<CancelamentoVO> cancelamentoVOs) {
		this.cancelamentoVOs = cancelamentoVOs;
	}

	public List<TrancamentoVO> getTrancamentoVOs() {
		if (trancamentoVOs == null) {
			trancamentoVOs = new ArrayList<TrancamentoVO>(0);
		}
		return trancamentoVOs;
	}

	public void setTrancamentoVOs(List<TrancamentoVO> trancamentoVOs) {
		this.trancamentoVOs = trancamentoVOs;
	}

	public List<TransferenciaEntradaVO> getTransferenciaEntradaVOs() {
		if (transferenciaEntradaVOs == null) {
			transferenciaEntradaVOs = new ArrayList<TransferenciaEntradaVO>(0);
		}
		return transferenciaEntradaVOs;
	}

	public void setTransferenciaEntradaVOs(List<TransferenciaEntradaVO> transferenciaEntradaVOs) {
		this.transferenciaEntradaVOs = transferenciaEntradaVOs;
	}

	public List<TransferenciaSaidaVO> getTransferenciaSaidaVOs() {
		if (transferenciaSaidaVOs == null) {
			transferenciaSaidaVOs = new ArrayList<TransferenciaSaidaVO>(0);
		}
		return transferenciaSaidaVOs;
	}

	public void setTransferenciaSaidaVOs(List<TransferenciaSaidaVO> transferenciaSaidaVOs) {
		this.transferenciaSaidaVOs = transferenciaSaidaVOs;
	}

    /**
     * @return the qtdeCancelamento
     */
    public Integer getQtdeCancelamento() {
        if(qtdeCancelamento == null) {
            qtdeCancelamento = 0;
        }
        return qtdeCancelamento;
    }

    /**
     * @param qtdeCancelamento the qtdeCancelamento to set
     */
    public void setQtdeCancelamento(Integer qtdeCancelamento) {
        this.qtdeCancelamento = qtdeCancelamento;
    }

    /**
     * @return the qtdeTrancamento
     */
    public Integer getQtdeTrancamento() {
        if(qtdeTrancamento == null) {
            qtdeTrancamento = 0;
        }
        return qtdeTrancamento;
    }

    /**
     * @param qtdeTrancamento the qtdeTrancamento to set
     */
    public void setQtdeTrancamento(Integer qtdeTrancamento) {
        this.qtdeTrancamento = qtdeTrancamento;
    }

    /**
     * @return the qtdeTransferenciaEntrada
     */
    public Integer getQtdeTransferenciaEntrada() {
        if(qtdeTransferenciaEntrada == null) {
            qtdeTransferenciaEntrada = 0;
        }
        return qtdeTransferenciaEntrada;
    }

    /**
     * @param qtdeTransferenciaEntrada the qtdeTransferenciaEntrada to set
     */
    public void setQtdeTransferenciaEntrada(Integer qtdeTransferenciaEntrada) {
        this.qtdeTransferenciaEntrada = qtdeTransferenciaEntrada;
    }

    /**
     * @return the qtdeTransferenciaSaida
     */
    public Integer getQtdeTransferenciaSaida() {
        if(qtdeTransferenciaSaida == null) {
            qtdeTransferenciaSaida = 0;
        }
        return qtdeTransferenciaSaida;
    }

    /**
     * @param qtdeTransferenciaSaida the qtdeTransferenciaSaida to set
     */
    public void setQtdeTransferenciaSaida(Integer qtdeTransferenciaSaida) {
        this.qtdeTransferenciaSaida = qtdeTransferenciaSaida;
    }

    /**
     * @return the qtdeTotal
     */
    public Integer getQtdeTotal() {
        if(qtdeTotal == null) {
            qtdeTotal = 0;
        }
        return qtdeTotal;
    }

    /**
     * @param qtdeTotal the qtdeTotal to set
     */
    public void setQtdeTotal(Integer qtdeTotal) {
        this.qtdeTotal = qtdeTotal;
    }

	public List<TrancamentoVO> getAbandonoCursoVOs() {
		if(abandonoCursoVOs == null){
			abandonoCursoVOs = new ArrayList<TrancamentoVO>(0);
		}		
		return abandonoCursoVOs;
	}

	public void setAbandonoCursoVOs(List<TrancamentoVO> abandonoCursoVOs) {
		
		this.abandonoCursoVOs = abandonoCursoVOs;
	}

	public List<TransferenciaEntradaVO> getTransferenciaInternaVOs() {
		if(transferenciaInternaVOs == null){
			transferenciaInternaVOs = new ArrayList<TransferenciaEntradaVO>(0);
		}
		return transferenciaInternaVOs;
	}

	public void setTransferenciaInternaVOs(List<TransferenciaEntradaVO> transferenciaInternaVOs) {
		this.transferenciaInternaVOs = transferenciaInternaVOs;
	}

	public Integer getQtdeTransferenciaInterna() {
		if(qtdeTransferenciaInterna == null){
			qtdeTransferenciaInterna = 0;
		}
		return qtdeTransferenciaInterna;
	}

	public void setQtdeTransferenciaInterna(Integer qtdeTransferenciaInterna) {
		this.qtdeTransferenciaInterna = qtdeTransferenciaInterna;
	}

	public Integer getQtdeAbandonoCurso() {
		if(qtdeAbandonoCurso == null){
			qtdeAbandonoCurso = 0;
		}
		return qtdeAbandonoCurso;
	}

	public void setQtdeAbandonoCurso(Integer qtdeAbandonoCurso) {
		this.qtdeAbandonoCurso = qtdeAbandonoCurso;
	}

	public List<OcorrenciasAlunosVO> getFormadoVOs() {
		if (formadoVOs == null) {
			formadoVOs = new ArrayList<OcorrenciasAlunosVO>(0);
		}
		return formadoVOs;
	}

	public void setFormadoVOs(List<OcorrenciasAlunosVO> formadoVOs) {
		this.formadoVOs = formadoVOs;
	}

	public Integer getQtdeFormado() {
		if (qtdeFormado == null) {
			qtdeFormado = 0;
		}
		return qtdeFormado;
	}

	public void setQtdeFormado(Integer qtdeFormado) {
		this.qtdeFormado = qtdeFormado;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
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

	public String getJustificativa() {
		if (justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public UsuarioVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new UsuarioVO();
		}
		return responsavelAutorizacao;
	}

	public void setResponsavelAutorizacao(UsuarioVO responsavelAutorizacao) {
		this.responsavelAutorizacao = responsavelAutorizacao;
	}


	public MotivoCancelamentoTrancamentoVO getMotivoCancelamentoTrancamentoVO() {
		if(motivoCancelamentoTrancamentoVO == null){
			motivoCancelamentoTrancamentoVO =  new MotivoCancelamentoTrancamentoVO();
		}
		return motivoCancelamentoTrancamentoVO;
	}

	public void setMotivoCancelamentoTrancamentoVO(MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO) {
		this.motivoCancelamentoTrancamentoVO = motivoCancelamentoTrancamentoVO;
	}

	public String getTurma() {
		if (turma == null) {
			turma = "";
		}
		return turma;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}
    
}
