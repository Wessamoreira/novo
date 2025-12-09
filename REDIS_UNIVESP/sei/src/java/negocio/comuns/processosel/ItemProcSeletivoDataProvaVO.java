package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import webservice.DateAdapterMobile;

/**
 * Reponsável por manter os dados da entidade ProcSeletivo. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name="itemProcessoSeletivoDataProva")
public class ItemProcSeletivoDataProvaVO extends SuperVO {

    private Integer codigo;
    private Integer procSeletivo;
    private Date dataProva;
    private String hora;
    private Date dataInicioInscricao;
    private Date dataTerminoInscricao;
    private Date dataLiberacaoResultado;
    private String motivoAlteracaoDataProva;
    private String tipoProvaGabarito;
    private List<ProcessoSeletivoProvaDataVO> processoSeletivoProvaDataVOs;
    private List<ProcSeletivoGabaritoDataVO> procSeletivoGabaritoDataVOs;
    private Date dataLimiteAdiarVencimentoInscricao;
    private Date dataLimiteApresentarDadosVisaoCandidato;
    /**
     * TRANSIENT - utilizado para gerar várias de prova / inscricao 
     * em uma única operação do usário. Isto por que as IE via de regra
     * estão abrindo o processo seletivo para todos os dias de um determinado
     * periodo.
     */
    private Boolean replicarDatasEHorariosAteDeterminadaDataFutura;
    private Date dataFuturaReplicarDatasEHorarios;
    private Boolean ignorarFeriadosComoDataPossivelParaProva;
    private Boolean ignorarSabadosComoDataPossivelParaProva;
    private Boolean ignorarDomingosComoDataPossivelParaProva;
    
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProcSeletivo</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ItemProcSeletivoDataProvaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProcSeletivoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     * @throws Exception 
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ItemProcSeletivoDataProvaVO obj) throws Exception {
		if (obj.getDataProva() == null) {
			throw new ConsistirException("O campo DATA PROVA deve ser informado.");
		}
		if (obj.getDataInicioInscricao() == null) {
			throw new ConsistirException("O campo DATA INÍCIO INSCRIÇÃO deve ser informado.");
		}
		if (obj.getDataTerminoInscricao() == null) {
			throw new ConsistirException("O campo DATA TÉRMINO INSCRIÇÃO deve ser informado.");
		}
		if (obj.getDataLiberacaoResultado() == null) {
			throw new ConsistirException("O campo DATA LIBERAÇÃO RESULTADO deve ser informado.");
		}
		if (obj.getDataLiberacaoResultado().before(obj.getDataProva()) || Uteis.getDataComHora(obj.getDataLiberacaoResultado()).equals(Uteis.getDataComHora(obj.getDataProva()))) {
			throw new ConsistirException("O campo DATA PROVA (" + Uteis.getData(obj.getDataProva()) + ") não pode ser posterior ou igual a DATA LIBERAÇÃO RESULTADO (" + Uteis.getData(obj.getDataLiberacaoResultado()) + ").");
		}
		if (obj.getDataInicioInscricao().after(obj.getDataProva())) {
			if (obj.isNovoObj()) {
				throw new ConsistirException("O campo DATA INÍCIO INSCRIÇÃO (" + Uteis.getDataComHora(obj.getDataInicioInscricao()) + ") deve ser menor que a DATA DA PROVA (" + Uteis.getDataComHora(obj.getDataProva()) + ").");
			} else {
				throw new ConsistirException("O campo DATA DA PROVA (" + Uteis.getDataComHora(obj.getDataProva()) + ") deve ser maior que a DATA INÍCIO INSCRIÇÃO (" + Uteis.getDataComHora(obj.getDataInicioInscricao()) + ").");
			}
		}
		if (obj.getDataInicioInscricao().after(obj.getDataTerminoInscricao())) {
			throw new ConsistirException("O campo DATA INÍCIO INSCRIÇÃO (" + Uteis.getDataComHora(obj.getDataInicioInscricao()) + ") deve ser menor que a DATA DE TÉRMINO (" + Uteis.getDataComHora(obj.getDataTerminoInscricao()) + ").");
		}
		if (obj.getDataTerminoInscricao().after(obj.getDataProva())) {
			if (obj.isNovoObj()) {
				throw new ConsistirException("O campo DATA TÉRMINO INSCRIÇÃO (" + Uteis.getDataComHora(obj.getDataTerminoInscricao()) + ") deve ser deve menor que a DATA DA PROVA (" + Uteis.getDataComHora(obj.getDataProva()) + ") .");
			} else {
				throw new ConsistirException("O campo DATA DA PROVA (" + Uteis.getDataComHora(obj.getDataProva()) + ") deve ser maior que a DATA TÉRMINO INSCRIÇÃO (" + Uteis.getDataComHora(obj.getDataTerminoInscricao()) + ").");
			}
		}
		if (obj.getDataLimiteAdiarVencimentoInscricao() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ProcessoSeletivo_dataLimiteAdiarVencimentoInscricao"));
		}
		if (obj.getDataLimiteAdiarVencimentoInscricao().after(obj.getDataProva())) {
//		if (obj.getDataLimiteAdiarVencimentoInscricao().after(obj.getDataProva()) || Uteis.getData(obj.getDataLimiteAdiarVencimentoInscricao()).equals(Uteis.getData(obj.getDataProva()))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ProcessoSeletivo_dataLimiteAdiarVencimentoInscricao_maior_dataProva").replace("{0}", Uteis.getData(obj.getDataLimiteAdiarVencimentoInscricao())).replace("{1}", Uteis.getData(obj.getDataProva())));
		}
		if (Uteis.getDataJDBC(obj.getDataLimiteAdiarVencimentoInscricao()).before(Uteis.getDataJDBC(obj.getDataTerminoInscricao())) && !Uteis.getData(obj.getDataLimiteAdiarVencimentoInscricao()).equals(Uteis.getData(obj.getDataTerminoInscricao()))) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ProcessoSeletivo_dataLimiteAdiarVencimentoInscricao_menor_dataTermino").replace("{0}", Uteis.getData(obj.getDataLimiteAdiarVencimentoInscricao())).replace("{1}", Uteis.getData(obj.getDataTerminoInscricao())));
		}
		if(obj.getDataLimiteApresentarDadosVisaoCandidato() != null && (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(obj.getDataLimiteApresentarDadosVisaoCandidato(), obj.getDataLiberacaoResultado()) == false && obj.getDataLimiteApresentarDadosVisaoCandidato().before(obj.getDataLiberacaoResultado()))){
			throw new ConsistirException("O campo DATA LIMITE APRESENTAR DADOS VISÃO DO CANDIDATO (" + Uteis.getData(obj.getDataLimiteApresentarDadosVisaoCandidato())+ ") ser deve maior ou igual que a DATA LIBERAÇÃO RESULTADO (" + Uteis.getData(obj.getDataLiberacaoResultado()) + ").");
		}
	}


    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ProcSeletivoCurso</code>.
     */
    
    @XmlElement(name = "dataProva")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataProva() {
        if (dataProva == null) {
            return new Date();
        }
        return (dataProva);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    @XmlElement(name = "dataProva_Apresentar")
    public String getDataProva_Apresentar() {
        if (dataProva == null) {
            return "";
        }
//        if (hora == null || hora.equals("")) {
//        	return (Uteis.getData(dataProva, "dd/MM/yyyy"));
//        }
        return (Uteis.getData(dataProva, "dd/MM/yyyy HH:mm"));
    }
    
    @XmlElement(name = "dataInicioInscricao_Apresentar")
    public String getDataInicioInscricao_Apresentar() {
    	if (dataInicioInscricao == null) {
    		return "";
    	}

    	return (Uteis.getData(dataInicioInscricao, "dd/MM/yyyy HH:mm"));
    }
    
    @XmlElement(name = "dataTerminoInscricao_Apresentar")
    public String getDataTerminoInscricao_Apresentar() {
    	if (dataTerminoInscricao == null) {
    		return "";
    	}

    	return (Uteis.getData(dataTerminoInscricao, "dd/MM/yyyy HH:mm"));
    }
    
    @XmlElement(name = "dataLiberacaoResultado_Apresentar")
    public String getDataLiberacaoResultado_Apresentar() {
    	if (dataLiberacaoResultado == null) {
    		return "";
    	}

    	return (Uteis.getData(dataLiberacaoResultado, "dd/MM/yyyy HH:mm"));
    }

    public void setDataProva(Date dataProva) {
        this.dataProva = dataProva;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getProcSeletivo() {
        if (procSeletivo == null) {
            procSeletivo = 0;
        }
        return (procSeletivo);
    }

    public void setProcSeletivo(Integer procSeletivo) {
        this.procSeletivo = procSeletivo;
    }

    @XmlElement(name = "hora")
    public String getHora() {
        if (hora == null) {
            hora = Uteis.getHoraAtual();
        }
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    
    public List<ProcessoSeletivoProvaDataVO> getProcessoSeletivoProvaDataVOs() {
        if(processoSeletivoProvaDataVOs == null){
            processoSeletivoProvaDataVOs = new ArrayList<ProcessoSeletivoProvaDataVO>(0);
        }
        return processoSeletivoProvaDataVOs;
    }

    
    public void setProcessoSeletivoProvaDataVOs(List<ProcessoSeletivoProvaDataVO> processoSeletivoProvaDataVOs) {
        this.processoSeletivoProvaDataVOs = processoSeletivoProvaDataVOs;
    }

    @XmlElement(name = "tipoProvaGabarito")
	public String getTipoProvaGabarito() {
		if (tipoProvaGabarito == null){
			tipoProvaGabarito = "GA";
		}
		return tipoProvaGabarito;
	}
	
	public String getTipoProvaGabarito_Apresentar() {
		if (tipoProvaGabarito == null){
			tipoProvaGabarito = "GA";
		}
		if (tipoProvaGabarito.equals("GA")) {
			return "Gabarito";
		}
		if (tipoProvaGabarito.equals("PR")) {
			return "Prova";
		}
		return "Gabarito";
	}

	public void setTipoProvaGabarito(String tipoProvaGabarito) {
		this.tipoProvaGabarito = tipoProvaGabarito;
	}

	public List<ProcSeletivoGabaritoDataVO> getProcSeletivoGabaritoDataVOs() {
		if (procSeletivoGabaritoDataVOs == null) {
			procSeletivoGabaritoDataVOs = new ArrayList<ProcSeletivoGabaritoDataVO>(0);
		}
		return procSeletivoGabaritoDataVOs;
	}

	public void setProcSeletivoGabaritoDataVOs(List<ProcSeletivoGabaritoDataVO> procSeletivoGabaritoDataVOs) {
		this.procSeletivoGabaritoDataVOs = procSeletivoGabaritoDataVOs;
	}

	@XmlElement(name = "dataInicioInscricao")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataInicioInscricao() {
		return dataInicioInscricao;
	}

	public void setDataInicioInscricao(Date dataInicioInscricao) {
		this.dataInicioInscricao = dataInicioInscricao;
	}

	@XmlElement(name = "dataTerminoInscricao")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataTerminoInscricao() {
		
		return dataTerminoInscricao;
	}

	public void setDataTerminoInscricao(Date dataTerminoInscricao) {
		this.dataTerminoInscricao = dataTerminoInscricao;
	}
	
	@XmlElement(name = "dataLiberacaoResultado")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataLiberacaoResultado() {
		if (dataLiberacaoResultado == null) {
			dataLiberacaoResultado = new Date();
		}
		return dataLiberacaoResultado;
	}

	public void setDataLiberacaoResultado(Date dataLiberacaoResultado) {
		this.dataLiberacaoResultado = dataLiberacaoResultado;
	}

    /**
     * @return the replicarDatasEHorariosAteDeterminadaDataFutura
     */
    public Boolean getReplicarDatasEHorariosAteDeterminadaDataFutura() {
        if (replicarDatasEHorariosAteDeterminadaDataFutura == null) {
            replicarDatasEHorariosAteDeterminadaDataFutura = Boolean.FALSE;
        }        
        return replicarDatasEHorariosAteDeterminadaDataFutura;
    }

    /**
     * @param replicarDatasEHorariosAteDeterminadaDataFutura the replicarDatasEHorariosAteDeterminadaDataFutura to set
     */
    public void setReplicarDatasEHorariosAteDeterminadaDataFutura(Boolean replicarDatasEHorariosAteDeterminadaDataFutura) {
        this.replicarDatasEHorariosAteDeterminadaDataFutura = replicarDatasEHorariosAteDeterminadaDataFutura;
    }

    /**
     * @return the ignorarFeriadosComoDataPossivelParaProva
     */
    public Boolean getIgnorarFeriadosComoDataPossivelParaProva() {
        if (ignorarFeriadosComoDataPossivelParaProva == null) {
            ignorarFeriadosComoDataPossivelParaProva = Boolean.TRUE;
        }        
        return ignorarFeriadosComoDataPossivelParaProva;
    }

    /**
     * @param ignorarFeriadosComoDataPossivelParaProva the ignorarFeriadosComoDataPossivelParaProva to set
     */
    public void setIgnorarFeriadosComoDataPossivelParaProva(Boolean ignorarFeriadosComoDataPossivelParaProva) {
        this.ignorarFeriadosComoDataPossivelParaProva = ignorarFeriadosComoDataPossivelParaProva;
    }

    /**
     * @return the ignorarSabadosComoDataPossivelParaProva
     */
    public Boolean getIgnorarSabadosComoDataPossivelParaProva() {
        if (ignorarSabadosComoDataPossivelParaProva == null) {
            ignorarSabadosComoDataPossivelParaProva = Boolean.TRUE;
        }
        return ignorarSabadosComoDataPossivelParaProva;
    }

    /**
     * @param ignorarSabadosComoDataPossivelParaProva the ignorarSabadosComoDataPossivelParaProva to set
     */
    public void setIgnorarSabadosComoDataPossivelParaProva(Boolean ignorarSabadosComoDataPossivelParaProva) {
        this.ignorarSabadosComoDataPossivelParaProva = ignorarSabadosComoDataPossivelParaProva;
    }

    /**
     * @return the ignorarDomingosComoDataPossivelParaProva
     */
    public Boolean getIgnorarDomingosComoDataPossivelParaProva() {
        if (ignorarDomingosComoDataPossivelParaProva == null) {
            ignorarDomingosComoDataPossivelParaProva = Boolean.TRUE;
        }
        return ignorarDomingosComoDataPossivelParaProva;
    }

    /**
     * @param ignorarDomingosComoDataPossivelParaProva the ignorarDomingosComoDataPossivelParaProva to set
     */
    public void setIgnorarDomingosComoDataPossivelParaProva(Boolean ignorarDomingosComoDataPossivelParaProva) {
        this.ignorarDomingosComoDataPossivelParaProva = ignorarDomingosComoDataPossivelParaProva;
    }

    /**
     * @return the dataFuturaReplicarDatasEHorarios
     */
    public Date getDataFuturaReplicarDatasEHorarios() {
        if (dataFuturaReplicarDatasEHorarios == null) {
            dataFuturaReplicarDatasEHorarios = new Date();
        }
        return dataFuturaReplicarDatasEHorarios;
    }

    /**
     * @param dataFuturaReplicarDatasEHorarios the dataFuturaReplicarDatasEHorarios to set
     */
    public void setDataFuturaReplicarDatasEHorarios(Date dataFuturaReplicarDatasEHorarios) {
        this.dataFuturaReplicarDatasEHorarios = dataFuturaReplicarDatasEHorarios;
    }

	/**
	 * @return the dataLimiteAdiarVencimentoInscricao
	 */
	public Date getDataLimiteAdiarVencimentoInscricao() {
		if (dataLimiteAdiarVencimentoInscricao == null) {
			try {
				dataLimiteAdiarVencimentoInscricao = Uteis.getDataPassada(getDataProva(), 1);
			} catch (Exception e) {
				dataLimiteAdiarVencimentoInscricao = getDataTerminoInscricao();
			}
		}
		return dataLimiteAdiarVencimentoInscricao;
	}

	/**
	 * @param dataLimiteAdiarVencimentoInscricao the dataLimiteAdiarVencimentoInscricao to set
	 */
	public void setDataLimiteAdiarVencimentoInscricao(Date dataLimiteAdiarVencimentoInscricao) {
		this.dataLimiteAdiarVencimentoInscricao = dataLimiteAdiarVencimentoInscricao;
	}
    
	public String getMotivoAlteracaoDataProva() {
		if (motivoAlteracaoDataProva == null) {
			motivoAlteracaoDataProva = "";
		}
		return motivoAlteracaoDataProva;
	}

	public void setMotivoAlteracaoDataProva(String motivoAlteracaoDataProva) {
		this.motivoAlteracaoDataProva = motivoAlteracaoDataProva;
	}

	public Date getDataLimiteApresentarDadosVisaoCandidato() {
		return dataLimiteApresentarDadosVisaoCandidato;
	}

	public void setDataLimiteApresentarDadosVisaoCandidato(Date dataLimiteApresentarDadosVisaoCandidato) {
		this.dataLimiteApresentarDadosVisaoCandidato = dataLimiteApresentarDadosVisaoCandidato;
	}	
	
    
}
