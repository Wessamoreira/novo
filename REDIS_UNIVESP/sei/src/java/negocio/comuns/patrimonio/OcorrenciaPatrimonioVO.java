package negocio.comuns.patrimonio;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.enumeradores.SituacaoOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * @author Rodrigo Wind
 *
 */
public class OcorrenciaPatrimonioVO extends SuperVO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2915021335049405482L;
    private Integer codigo;
    private Date dataOcorrencia;
    private UsuarioVO responsavelOcorrencia;
    private TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonio;
    private PatrimonioUnidadeVO patrimonioUnidade;
    private FuncionarioVO solicitante;
    // Esse é utilizado para os tipos Reserva.
    private TipoPatrimonioVO tipoPatrimonioVO;
    private Date dataInicioReserva;
    private Date dataTerminoReserva;
    private LocalArmazenamentoVO localReservado;
    private Date dataReserva; // Esse não sera persistido, apenas para auxilio
			      // de tela.

    /**
     * Este é utilizado no registro de troca do local e no registro de separação
     * p/ descarte
     * 
     */
    private LocalArmazenamentoVO localArmazenamentoOrigem;
    /**
     * Este é utilizado no registro de troca do local e no registro de separação
     * p/ descarte e no emprestimo
     * 
     */
    private LocalArmazenamentoVO localArmazenamentoDestino;
    private String motivo;
    /**
     * Este é utilizado no emprestimo para registrar para que o mesmo foi
     * emprestado
     */
    private FuncionarioVO solicitanteEmprestimo;
    /**
     * Este é utilizado pelo emprestimo e pela manutenção
     */
    private Date dataPrevisaoDevolucao;
    private Date dataFinalizacao;
    private UsuarioVO responsavelFinalizacao;
    private SituacaoOcorrenciaPatrimonioEnum situacao;
    /**
     * Este é utilizado na devolução da manutenção e do emprestimo.
     */
    private String observacao;

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
     * @param codigo
     *            the codigo to set
     */
    public void setCodigo(Integer codigo) {
	this.codigo = codigo;
    }

    /**
     * @return the dataOcorrencia
     */
    public Date getDataOcorrencia() {
	if (dataOcorrencia == null) {
	    dataOcorrencia = new Date();
	}
	return dataOcorrencia;
    }

    /**
     * @param dataOcorrencia
     *            the dataOcorrencia to set
     */
    public void setDataOcorrencia(Date dataOcorrencia) {
	this.dataOcorrencia = dataOcorrencia;
    }

    /**
     * @return the responsavelOcorrencia
     */
    public UsuarioVO getResponsavelOcorrencia() {
	if (responsavelOcorrencia == null) {
	    responsavelOcorrencia = new UsuarioVO();
	}
	return responsavelOcorrencia;
    }

    /**
     * @param responsavelOcorrencia
     *            the responsavelOcorrencia to set
     */
    public void setResponsavelOcorrencia(UsuarioVO responsavelOcorrencia) {
	this.responsavelOcorrencia = responsavelOcorrencia;
    }

    /**
     * @return the tipoOcorrenciaPatrimonio
     */
    public TipoOcorrenciaPatrimonioEnum getTipoOcorrenciaPatrimonio() {
	return tipoOcorrenciaPatrimonio;
    }

    /**
     * @param tipoOcorrenciaPatrimonio
     *            the tipoOcorrenciaPatrimonio to set
     */
    public void setTipoOcorrenciaPatrimonio(TipoOcorrenciaPatrimonioEnum tipoOcorrenciaPatrimonio) {
	this.tipoOcorrenciaPatrimonio = tipoOcorrenciaPatrimonio;
    }

    /**
     * @return the patrimonioUnidade
     */
    public PatrimonioUnidadeVO getPatrimonioUnidade() {
	if (patrimonioUnidade == null) {
	    patrimonioUnidade = new PatrimonioUnidadeVO();
	}
	return patrimonioUnidade;
    }

    /**
     * @param patrimonioUnidade
     *            the patrimonioUnidade to set
     */
    public void setPatrimonioUnidade(PatrimonioUnidadeVO patrimonioUnidade) {
	this.patrimonioUnidade = patrimonioUnidade;
    }

    /**
     * @return the localArmazenamentoOrigem
     */
    public LocalArmazenamentoVO getLocalArmazenamentoOrigem() {
	if (localArmazenamentoOrigem == null) {
	    localArmazenamentoOrigem = new LocalArmazenamentoVO();
	}
	return localArmazenamentoOrigem;
    }

    /**
     * @param localArmazenamentoOrigem
     *            the localArmazenamentoOrigem to set
     */
    public void setLocalArmazenamentoOrigem(LocalArmazenamentoVO localArmazenamentoOrigem) {
	this.localArmazenamentoOrigem = localArmazenamentoOrigem;
    }

    /**
     * @return the localArmazenamentoDestino
     */
    public LocalArmazenamentoVO getLocalArmazenamentoDestino() {
	if (localArmazenamentoDestino == null) {
	    localArmazenamentoDestino = new LocalArmazenamentoVO();
	}
	return localArmazenamentoDestino;
    }

    /**
     * @param localArmazenamentoDestino
     *            the localArmazenamentoDestino to set
     */
    public void setLocalArmazenamentoDestino(LocalArmazenamentoVO localArmazenamentoDestino) {
	this.localArmazenamentoDestino = localArmazenamentoDestino;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
	if (motivo == null) {
	    motivo = "";
	}
	return motivo;
    }

    /**
     * @param motivo
     *            the motivo to set
     */
    public void setMotivo(String motivo) {
	this.motivo = motivo;
    }

    /**
     * @return the solicitanteEmprestimo
     */
    public FuncionarioVO getSolicitanteEmprestimo() {
	if (solicitanteEmprestimo == null) {
	    solicitanteEmprestimo = new FuncionarioVO();
	}
	return solicitanteEmprestimo;
    }

    /**
     * @param solicitanteEmprestimo
     *            the solicitanteEmprestimo to set
     */
    public void setSolicitanteEmprestimo(FuncionarioVO solicitanteEmprestimo) {
	this.solicitanteEmprestimo = solicitanteEmprestimo;
    }

    /**
     * @return the dataPrevisaoDevolucao
     */
    public Date getDataPrevisaoDevolucao() {
	return dataPrevisaoDevolucao;
    }

    /**
     * @param dataPrevisaoDevolucao
     *            the dataPrevisaoDevolucao to set
     */
    public void setDataPrevisaoDevolucao(Date dataPrevisaoDevolucao) {
	this.dataPrevisaoDevolucao = dataPrevisaoDevolucao;
    }

    /**
     * @return the dataFinalizacao
     */
    public Date getDataFinalizacao() {
	return dataFinalizacao;
    }

    /**
     * @param dataFinalizacao
     *            the dataFinalizacao to set
     */
    public void setDataFinalizacao(Date dataFinalizacao) {
	this.dataFinalizacao = dataFinalizacao;
    }

    /**
     * @return the responsavelFinalizacao
     */
    public UsuarioVO getResponsavelFinalizacao() {
	if (responsavelFinalizacao == null) {
	    responsavelFinalizacao = new UsuarioVO();
	}
	return responsavelFinalizacao;
    }

    /**
     * @param responsavelFinalizacao
     *            the responsavelFinalizacao to set
     */
    public void setResponsavelFinalizacao(UsuarioVO responsavelFinalizacao) {
	this.responsavelFinalizacao = responsavelFinalizacao;
    }

    /**
     * @return the situacao
     */
    public SituacaoOcorrenciaPatrimonioEnum getSituacao() {
	if (situacao == null) {
	    situacao = SituacaoOcorrenciaPatrimonioEnum.EM_ANDAMENTO;
	}
	return situacao;
    }

    /**
     * @param situacao
     *            the situacao to set
     */
    public void setSituacao(SituacaoOcorrenciaPatrimonioEnum situacao) {
	this.situacao = situacao;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
	if (observacao == null) {
	    observacao = "";
	}
	return observacao;
    }

    /**
     * @param observacao
     *            the observacao to set
     */
    public void setObservacao(String observacao) {
	this.observacao = observacao;
    }

    public boolean getIsPermiteExcluir() {
	return !isNovoObj() && Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && (getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.DESCARTE) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.MANUTENCAO) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE));
    }

    public boolean getIsApresentarDataPrevisaoDevolucao() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && (getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.MANUTENCAO));
    }

    public boolean getIsApresentarSituacao() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && (getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.MANUTENCAO));
    }

    public boolean getIsPermiteInformarDataOcorrencia() {
	return getSituacao().equals(SituacaoOcorrenciaPatrimonioEnum.EM_ANDAMENTO) && Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && (getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.DESCARTE) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.MANUTENCAO) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL));
    }

    public boolean getIsApresentarLocalArmazenamentoDestino() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && (getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.DESCARTE) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL) || getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE));
    }

    public boolean getIsApresentarDadosReservaUnidade() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE);
    }

    public boolean getIsApresentarDadosReservaLocal() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL);
    }

    public boolean getIsTipoOcorrenciaEmprestimo() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO);
    }

    public boolean getIsTipoOcorrenciaManutencao() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.MANUTENCAO);
    }

    public boolean getIsTipoOcorrenciaTrocaLocal() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL);
    }

    public boolean getIsTipoOcorrenciaSepararDescarte() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE);
    }

    public boolean getIsTipoOcorrenciaDescarte() {
	return Uteis.isAtributoPreenchido(getTipoOcorrenciaPatrimonio()) && getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.DESCARTE);
    }

    public boolean getIsEmAndamento() {
	return isNovoObj() || getSituacao().equals(SituacaoOcorrenciaPatrimonioEnum.EM_ANDAMENTO);
    }

    public boolean getIsFinalizado() {
	return !isNovoObj() && getSituacao().equals(SituacaoOcorrenciaPatrimonioEnum.FINALIZADO);
    }

    public FuncionarioVO getSolicitante() {
	if (solicitante == null) {
	    solicitante = new FuncionarioVO();
	}
	return solicitante;
    }

    public void setSolicitante(FuncionarioVO solicitante) {
	this.solicitante = solicitante;
    }

    public TipoPatrimonioVO getTipoPatrimonioVO() {
	if (tipoPatrimonioVO == null) {
	    tipoPatrimonioVO = new TipoPatrimonioVO();
	}
	return tipoPatrimonioVO;
    }

    public void setTipoPatrimonioVO(TipoPatrimonioVO tipoPatrimonioVO) {
	this.tipoPatrimonioVO = tipoPatrimonioVO;
    }

    public Date getDataInicioReserva() {
	if (dataInicioReserva == null) {
	    dataInicioReserva = new Date();
	}
	return dataInicioReserva;
    }

    public void setDataInicioReserva(Date dataInicioReserva) {
	this.dataInicioReserva = dataInicioReserva;
    }

    public Date getDataTerminoReserva() {
	if (dataTerminoReserva == null) {
	    dataTerminoReserva = UteisData.getDataAtualSomandoOuSubtraindoMinutos(60);
	}
	return dataTerminoReserva;
    }

    public void setDataTerminoReserva(Date dataTerminoReserva) {
	this.dataTerminoReserva = dataTerminoReserva;
    }

    public Date getDataReserva() {
	if (dataReserva == null) {
	    dataReserva = new Date();
	}
	return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
	this.dataReserva = dataReserva;
    }

    public LocalArmazenamentoVO getLocalReservado() {
	if (localReservado == null) {
	    localReservado = new LocalArmazenamentoVO();
	}
	return localReservado;
    }

    public void setLocalReservado(LocalArmazenamentoVO localReservado) {
	this.localReservado = localReservado;
    }

}