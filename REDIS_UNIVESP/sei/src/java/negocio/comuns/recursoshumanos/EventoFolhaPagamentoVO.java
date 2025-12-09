package negocio.comuns.recursoshumanos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.recursoshumanos.enumeradores.CategoriaEventoFolhaEnum;
import negocio.comuns.recursoshumanos.enumeradores.NaturezaEventoFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoEventoFolhaPagamentoEnum;
import negocio.comuns.recursoshumanos.enumeradores.TipoLancamentoFolhaPagamentoEnum;

public class EventoFolhaPagamentoVO extends SuperVO implements Comparable<EventoFolhaPagamentoVO> {

	private static final long serialVersionUID = -7653330907156185305L;

	private Integer codigo;
	private String identificador;
	private String descricao;
	private Integer prioridade;
	private AtivoInativoEnum situacao;
	private TipoLancamentoFolhaPagamentoEnum tipoLancamento;
	private TipoEventoFolhaPagamentoEnum tipoEvento;
	private FormulaFolhaPagamentoVO formulaValor;
	private FormulaFolhaPagamentoVO formulaHora;
	private FormulaFolhaPagamentoVO formulaDia;
	private FormulaFolhaPagamentoVO formulaReferencia;

	// Sprint 2
	private Integer ordemCalculo;
	private FormulaFolhaPagamentoVO formulaFixa;
	private Boolean inssFolhaNormal;
	private Boolean irrfFolhaNormal;
	private Boolean fgtsFolhaNormal;
	private Boolean dsrFolhaNormal;
	private Boolean salarioFamiliaFolhaNormal;
	private Boolean irrfFerias;
	private Boolean adicionalFerias;
	private Boolean inssDecimoTerceiro;
	private Boolean irrfDecimoTerceiro;
	private Boolean fgtsDecimoTerceiro;
	private Boolean folhaPensao;
	private Boolean feriasPensao;
	private Boolean decimoTerceiroPensao;
	private Boolean participacaoLucroPensao;
	private Boolean rais;
	private Boolean informeRendimento;
	private Boolean previdenciaPropria;
	private Boolean previdenciaObrigatoria;
	private Boolean planoSaude;
	private Boolean decimoTerceiroPrevidenciaPropria;
	private Boolean decimoTerceiroPlanoSaude;
	private Boolean valeTransporte;

	// Desconto
	private Boolean dedutivelIrrf;
	private Boolean estornaInss;
	private Boolean estornaPrevidenciaPropria;
	private Boolean estornaPrevidenciaPropriaObrigatoria;
	private Boolean estornaIrrf;
	private Boolean estornaFgts;
	private Boolean estornaValeTransporte;
	private Boolean estornaSalarioFamilia;
	private Boolean estornaIrrfFerias;
	private Boolean dedutivelIrrfFerias;
	private Boolean estornaInssDecimoTerceiro;
	private Boolean estornaFgtsDecimoTerceiro;
	private Boolean estornaIrrfDecimoTerceiro;
	private Boolean dedutivelIrrfDecimoTerceiro;
	private Boolean folhaPensaoDesconto;
	private Boolean feriasPensaoDesconto;
	private Boolean decimentoTerceiroPensaoDesconto;
	private Boolean participacaoLucroPensaoDesconto;
	private Boolean valeTransporteDesconto;

	private UsuarioVO usuarioUltimaAlteracao;
	private Date dataUltimaAlteracao;
	private CategoriaDespesaVO categoriaDespesaVO;

	// Eventos de proporcionalidade
	private Boolean admissao;
	private Boolean demissao;
	private Boolean ferias;
	private Boolean afastamento;
	// Fim Eventos de proporcionalidade

	private Boolean eventoPadrao;

	// Valor temporaria para armazenar o valor fixo escolhido para o usuario
	private BigDecimal valorTemporario;
	private Boolean valorInformado;
	private Boolean informadoManual;
	private String referencia;
	private ContraChequeEventoVO contraChequeEventoVO;
	private ReciboFeriasEventoVO reciboFeriasEventoVO;

	// Sprint 5
	private CategoriaEventoFolhaEnum categoria;
	private Boolean incideAdicionalTempoServico;
	private Boolean incideAssociacaoSindicato;

	private Boolean agrupamentoFolhaNormal;
	private Boolean agrupamentoFerias;
	private Boolean agrupamentoFeriasProporcionais;
	private Boolean agrupamentoFeriasVencidas;
	private Boolean agrupamentoDecimoTerceiro;
	private Boolean agrupamentoDecimoTerceiroProporcionais;

	private List<EventoFolhaPagamentoMediaVO> eventoMediaVOs;
	private List<EventoFolhaPagamentoItemVO> eventoFolhaPagamentoItemVOs;

	// Sprint 6
	private NaturezaEventoFolhaPagamentoEnum naturezaEvento;

	private Boolean incideAdicionalTempoServicoFerias;
	private Boolean incideAdicionalTempoServico13;
	private Boolean incideAdicionalTempoServicoRescisao;

	// Sprint 7
	private Boolean inssFerias;
	private Boolean previdenciaPropriaFerias;
	private Boolean previdenciaObrigatoriaFerias;
	private Boolean incideAssociacaoSindicatoFerias;
	private Boolean incidePlanoSaudeFerias;

	private Boolean permiteDuplicarContraCheque;

	private Boolean incidePrevidenciaObrigatoriaDecimoTerceiro;
	private Boolean incideAssociacaoSindicatoDecimoTerceiro;

	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}

	public String getIdentificador() {
		if (identificador == null)
			identificador = "";
		return identificador;
	}

	public String getDescricao() {
		if (descricao == null)
			descricao = "";
		return descricao;
	}

	public Integer getPrioridade() {
		if (prioridade == null)
			prioridade = 99;
		return prioridade;
	}

	public AtivoInativoEnum getSituacao() {
		if (situacao == null) {
			situacao = AtivoInativoEnum.ATIVO;
		}
		return situacao;
	}

	public String getSituacao_Apresentar() {
		if (getSituacao() == null)
			return "";

		return getSituacao().getDescricao();
	}

	public FormulaFolhaPagamentoVO getFormulaValor() {
		if (formulaValor == null)
			formulaValor = new FormulaFolhaPagamentoVO();
		return formulaValor;
	}

	public FormulaFolhaPagamentoVO getFormulaHora() {
		if (formulaHora == null)
			formulaHora = new FormulaFolhaPagamentoVO();
		return formulaHora;
	}

	public FormulaFolhaPagamentoVO getFormulaDia() {
		if (formulaDia == null)
			formulaDia = new FormulaFolhaPagamentoVO();
		return formulaDia;
	}

	public FormulaFolhaPagamentoVO getFormulaReferencia() {
		if (formulaReferencia == null)
			formulaReferencia = new FormulaFolhaPagamentoVO();
		return formulaReferencia;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}

	public void setSituacao(AtivoInativoEnum situacao) {
		this.situacao = situacao;
	}

	public void setFormulaValor(FormulaFolhaPagamentoVO formulaValor) {
		this.formulaValor = formulaValor;
	}

	public void setFormulaHora(FormulaFolhaPagamentoVO formulaHora) {
		this.formulaHora = formulaHora;
	}

	public void setFormulaDia(FormulaFolhaPagamentoVO formulaDia) {
		this.formulaDia = formulaDia;
	}

	public void setFormulaReferencia(FormulaFolhaPagamentoVO formulaReferencia) {
		this.formulaReferencia = formulaReferencia;
	}

	public TipoLancamentoFolhaPagamentoEnum getTipoLancamento() {
		return tipoLancamento;
	}

	public String getTipoLancamento_Apresentar() {
		if (getTipoLancamento() == null)
			return "";

		return getTipoLancamento().getDescricao();
	}

	public TipoEventoFolhaPagamentoEnum getTipoEvento() {
		return tipoEvento;
	}

	public String getTipoEvento_Apresentar() {
		if (getTipoEvento() == null)
			return "";

		return getTipoEvento().getDescricao();
	}

	public void setTipoLancamento(TipoLancamentoFolhaPagamentoEnum tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public void setTipoEvento(TipoEventoFolhaPagamentoEnum tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Integer getOrdemCalculo() {
		if (ordemCalculo == null)
			ordemCalculo = 99;
		return ordemCalculo;
	}

	public FormulaFolhaPagamentoVO getFormulaFixa() {
		if (formulaFixa == null)
			formulaFixa = new FormulaFolhaPagamentoVO();
		return formulaFixa;
	}

	public void setOrdemCalculo(Integer ordemCalculo) {
		this.ordemCalculo = ordemCalculo;
	}

	public void setFormulaFixa(FormulaFolhaPagamentoVO formulaFixa) {
		this.formulaFixa = formulaFixa;
	}

	public Boolean getInssFolhaNormal() {
		if (inssFolhaNormal == null)
			inssFolhaNormal = false;
		return inssFolhaNormal;
	}

	public void setInssFolhaNormal(Boolean inssFolhaNormal) {
		this.inssFolhaNormal = inssFolhaNormal;
	}

	public Boolean getIrrfFolhaNormal() {
		if (irrfFolhaNormal == null)
			irrfFolhaNormal = false;
		return irrfFolhaNormal;
	}

	public void setIrrfFolhaNormal(Boolean irrfFolhaNormal) {
		this.irrfFolhaNormal = irrfFolhaNormal;
	}

	public Boolean getFgtsFolhaNormal() {
		if (fgtsFolhaNormal == null)
			fgtsFolhaNormal = false;
		return fgtsFolhaNormal;
	}

	public void setFgtsFolhaNormal(Boolean fgtsFolhaNormal) {
		this.fgtsFolhaNormal = fgtsFolhaNormal;
	}

	public Boolean getDsrFolhaNormal() {
		if (dsrFolhaNormal == null)
			dsrFolhaNormal = false;
		return dsrFolhaNormal;
	}

	public void setDsrFolhaNormal(Boolean dsrFolhaNormal) {
		this.dsrFolhaNormal = dsrFolhaNormal;
	}

	public Boolean getSalarioFamiliaFolhaNormal() {
		if (salarioFamiliaFolhaNormal == null)
			salarioFamiliaFolhaNormal = false;
		return salarioFamiliaFolhaNormal;
	}

	public void setSalarioFamiliaFolhaNormal(Boolean salarioFamiliaFolhaNormal) {
		this.salarioFamiliaFolhaNormal = salarioFamiliaFolhaNormal;
	}

	public Boolean getIrrfFerias() {
		if (irrfFerias == null)
			irrfFerias = false;
		return irrfFerias;
	}

	public void setIrrfFerias(Boolean irrfFerias) {
		this.irrfFerias = irrfFerias;
	}

	public Boolean getAdicionalFerias() {
		if (adicionalFerias == null)
			adicionalFerias = false;
		return adicionalFerias;
	}

	public void setAdicionalFerias(Boolean adicionalFerias) {
		this.adicionalFerias = adicionalFerias;
	}

	public Boolean getInssDecimoTerceiro() {
		if (inssDecimoTerceiro == null)
			inssDecimoTerceiro = false;
		return inssDecimoTerceiro;
	}

	public void setInssDecimoTerceiro(Boolean inssDecimoTerceiro) {
		this.inssDecimoTerceiro = inssDecimoTerceiro;
	}

	public Boolean getIrrfDecimoTerceiro() {
		if (irrfDecimoTerceiro == null)
			irrfDecimoTerceiro = false;
		return irrfDecimoTerceiro;
	}

	public void setIrrfDecimoTerceiro(Boolean irrfDecimoTerceiro) {
		this.irrfDecimoTerceiro = irrfDecimoTerceiro;
	}

	public Boolean getFgtsDecimoTerceiro() {
		if (fgtsDecimoTerceiro == null)
			fgtsDecimoTerceiro = false;
		return fgtsDecimoTerceiro;
	}

	public void setFgtsDecimoTerceiro(Boolean fgtsDecimoTerceiro) {
		this.fgtsDecimoTerceiro = fgtsDecimoTerceiro;
	}

	public Boolean getFolhaPensao() {
		if (folhaPensao == null)
			folhaPensao = false;
		return folhaPensao;
	}

	public void setFolhaPensao(Boolean folhaPensao) {
		this.folhaPensao = folhaPensao;
	}

	public Boolean getFeriasPensao() {
		if (feriasPensao == null)
			feriasPensao = false;
		return feriasPensao;
	}

	public void setFeriasPensao(Boolean feriasPensao) {
		this.feriasPensao = feriasPensao;
	}

	public Boolean getDecimoTerceiroPensao() {
		if (decimoTerceiroPensao == null)
			decimoTerceiroPensao = false;
		return decimoTerceiroPensao;
	}

	public void setDecimoTerceiroPensao(Boolean decimoTerceiroPensao) {
		this.decimoTerceiroPensao = decimoTerceiroPensao;
	}

	public Boolean getParticipacaoLucroPensao() {
		if (participacaoLucroPensao == null)
			participacaoLucroPensao = false;
		return participacaoLucroPensao;
	}

	public void setParticipacaoLucroPensao(Boolean participacaoLucroPensao) {
		this.participacaoLucroPensao = participacaoLucroPensao;
	}

	public Boolean getRais() {
		if (rais == null)
			rais = false;
		return rais;
	}

	public void setRais(Boolean rais) {
		this.rais = rais;
	}

	public Boolean getInformeRendimento() {
		if (informeRendimento == null)
			informeRendimento = false;
		return informeRendimento;
	}

	public void setInformeRendimento(Boolean informeRendimento) {
		this.informeRendimento = informeRendimento;
	}

	public Boolean getPrevidenciaPropria() {
		if (previdenciaPropria == null)
			previdenciaPropria = false;
		return previdenciaPropria;
	}

	public void setPrevidenciaPropria(Boolean previdenciaPropria) {
		this.previdenciaPropria = previdenciaPropria;
	}

	public Boolean getPlanoSaude() {
		if (planoSaude == null)
			planoSaude = false;
		return planoSaude;
	}

	public void setPlanoSaude(Boolean planoSaude) {
		this.planoSaude = planoSaude;
	}

	public Boolean getDecimoTerceiroPrevidenciaPropria() {
		if (decimoTerceiroPrevidenciaPropria == null)
			decimoTerceiroPrevidenciaPropria = false;
		return decimoTerceiroPrevidenciaPropria;
	}

	public void setDecimoTerceiroPrevidenciaPropria(Boolean decimoTerceiroPrevidenciaPropria) {
		this.decimoTerceiroPrevidenciaPropria = decimoTerceiroPrevidenciaPropria;
	}

	public Boolean getDecimoTerceiroPlanoSaude() {
		if (decimoTerceiroPlanoSaude == null)
			decimoTerceiroPlanoSaude = false;
		return decimoTerceiroPlanoSaude;
	}

	public void setDecimoTerceiroPlanoSaude(Boolean decimoTerceiroPlanoSaude) {
		this.decimoTerceiroPlanoSaude = decimoTerceiroPlanoSaude;
	}

	public Boolean getDedutivelIrrf() {
		if (dedutivelIrrf == null) {
			dedutivelIrrf = false;
		}
		return dedutivelIrrf;
	}

	public void setDedutivelIrrf(Boolean dedutivelIrrf) {
		this.dedutivelIrrf = dedutivelIrrf;
	}

	public Boolean getEstornaInss() {
		if (estornaInss == null) {
			estornaInss = false;
		}
		return estornaInss;
	}

	public void setEstornaInss(Boolean estornaInss) {
		this.estornaInss = estornaInss;
	}

	public Boolean getEstornaIrrf() {
		if (estornaIrrf == null) {
			estornaIrrf = false;
		}
		return estornaIrrf;
	}

	public void setEstornaIrrf(Boolean estornaIrrf) {
		this.estornaIrrf = estornaIrrf;
	}

	public Boolean getEstornaFgts() {
		if (estornaFgts == null) {
			estornaFgts = false;
		}
		return estornaFgts;
	}

	public void setEstornaFgts(Boolean estornaFgts) {
		this.estornaFgts = estornaFgts;
	}

	public Boolean getEstornaValeTransporte() {
		if (estornaValeTransporte == null) {
			estornaValeTransporte = false;
		}
		return estornaValeTransporte;
	}

	public void setEstornaValeTransporte(Boolean estornaValeTransporte) {
		this.estornaValeTransporte = estornaValeTransporte;
	}

	public Boolean getEstornaSalarioFamilia() {
		if (estornaSalarioFamilia == null) {
			estornaSalarioFamilia = false;
		}
		return estornaSalarioFamilia;
	}

	public void setEstornaSalarioFamilia(Boolean estornaSalarioFamilia) {
		this.estornaSalarioFamilia = estornaSalarioFamilia;
	}

	public Boolean getEstornaIrrfFerias() {
		if (estornaIrrfFerias == null) {
			estornaIrrfFerias = false;
		}
		return estornaIrrfFerias;
	}

	public void setEstornaIrrfFerias(Boolean estornaIrrfFerias) {
		this.estornaIrrfFerias = estornaIrrfFerias;
	}

	public Boolean getDedutivelIrrfFerias() {
		if (dedutivelIrrfFerias == null) {
			dedutivelIrrfFerias = false;
		}
		return dedutivelIrrfFerias;
	}

	public void setDedutivelIrrfFerias(Boolean dedutivelIrrfFerias) {
		this.dedutivelIrrfFerias = dedutivelIrrfFerias;
	}

	public Boolean getEstornaInssDecimoTerceiro() {
		if (estornaInssDecimoTerceiro == null) {
			estornaInssDecimoTerceiro = false;
		}
		return estornaInssDecimoTerceiro;
	}

	public void setEstornaInssDecimoTerceiro(Boolean estornaInssDecimoTerceiro) {
		this.estornaInssDecimoTerceiro = estornaInssDecimoTerceiro;
	}

	public Boolean getEstornaFgtsDecimoTerceiro() {
		if (estornaFgtsDecimoTerceiro == null) {
			estornaFgtsDecimoTerceiro = false;
		}
		return estornaFgtsDecimoTerceiro;
	}

	public void setEstornaFgtsDecimoTerceiro(Boolean estornaFgtsDecimoTerceiro) {
		this.estornaFgtsDecimoTerceiro = estornaFgtsDecimoTerceiro;
	}

	public Boolean getEstornaIrrfDecimoTerceiro() {
		if (estornaIrrfDecimoTerceiro == null) {
			estornaIrrfDecimoTerceiro = false;
		}
		return estornaIrrfDecimoTerceiro;
	}

	public void setEstornaIrrfDecimoTerceiro(Boolean estornaIrrfDecimoTerceiro) {
		this.estornaIrrfDecimoTerceiro = estornaIrrfDecimoTerceiro;
	}

	public Boolean getDedutivelIrrfDecimoTerceiro() {
		if (dedutivelIrrfDecimoTerceiro == null) {
			dedutivelIrrfDecimoTerceiro = false;
		}
		return dedutivelIrrfDecimoTerceiro;
	}

	public void setDedutivelIrrfDecimoTerceiro(Boolean dedutivelIrrfDecimoTerceiro) {
		this.dedutivelIrrfDecimoTerceiro = dedutivelIrrfDecimoTerceiro;
	}

	public Boolean getFolhaPensaoDesconto() {
		if (folhaPensaoDesconto == null) {
			folhaPensaoDesconto = false;
		}
		return folhaPensaoDesconto;
	}

	public void setFolhaPensaoDesconto(Boolean folhaPensaoDesconto) {
		this.folhaPensaoDesconto = folhaPensaoDesconto;
	}

	public Boolean getFeriasPensaoDesconto() {
		if (feriasPensaoDesconto == null) {
			feriasPensaoDesconto = false;
		}
		return feriasPensaoDesconto;
	}

	public void setFeriasPensaoDesconto(Boolean feriasPensaoDesconto) {
		this.feriasPensaoDesconto = feriasPensaoDesconto;
	}

	public Boolean getDecimentoTerceiroPensaoDesconto() {
		if (decimentoTerceiroPensaoDesconto == null) {
			decimentoTerceiroPensaoDesconto = false;
		}
		return decimentoTerceiroPensaoDesconto;
	}

	public void setDecimentoTerceiroPensaoDesconto(Boolean decimentoTerceiroPensaoDesconto) {
		this.decimentoTerceiroPensaoDesconto = decimentoTerceiroPensaoDesconto;
	}

	public Boolean getParticipacaoLucroPensaoDesconto() {
		if (participacaoLucroPensaoDesconto == null) {
			participacaoLucroPensaoDesconto = false;
		}
		return participacaoLucroPensaoDesconto;
	}

	public void setParticipacaoLucroPensaoDesconto(Boolean participacaoLucroPensaoDesconto) {
		this.participacaoLucroPensaoDesconto = participacaoLucroPensaoDesconto;
	}

	/**
	 * A opcao de incidir obrigatoriamente na previdencia ou nao <br>
	 * so pode ser obrigatoria caso a previdencia for propria
	 * 
	 * @return
	 */
	public Boolean getPrevidenciaObrigatoria() {
		if (previdenciaObrigatoria == null) {
			previdenciaObrigatoria = false;
		} else if (!getPrevidenciaPropria()) {
			setPrevidenciaObrigatoria(false);
		}
		return previdenciaObrigatoria;
	}

	public void setPrevidenciaObrigatoria(Boolean previdenciaObrigatoria) {
		this.previdenciaObrigatoria = previdenciaObrigatoria;
	}

	public int compareTo(EventoFolhaPagamentoVO evento) {
		if (this.getPrioridade() < evento.getPrioridade()) {
			return -1;
		}
		if (this.getPrioridade() > evento.getPrioridade()) {
			return 1;
		}

		if (this.getOrdemCalculo() < evento.getOrdemCalculo()) {
			return -1;
		}
		if (this.getOrdemCalculo() > evento.getOrdemCalculo()) {
			return 1;
		}
		return 0;
	}

	public Boolean getEstornaPrevidenciaPropria() {
		if (estornaPrevidenciaPropria == null)
			estornaPrevidenciaPropria = false;
		return estornaPrevidenciaPropria;
	}

	public Boolean getEstornaPrevidenciaPropriaObrigatoria() {
		if (estornaPrevidenciaPropriaObrigatoria == null)
			estornaPrevidenciaPropriaObrigatoria = false;
		return estornaPrevidenciaPropriaObrigatoria;
	}

	public void setEstornaPrevidenciaPropria(Boolean estornaPrevidenciaPropria) {
		this.estornaPrevidenciaPropria = estornaPrevidenciaPropria;
	}

	public void setEstornaPrevidenciaPropriaObrigatoria(Boolean estornaPrevidenciaPropriaObrigatoria) {
		this.estornaPrevidenciaPropriaObrigatoria = estornaPrevidenciaPropriaObrigatoria;
	}

	public BigDecimal getValorTemporario() {
		if (valorTemporario == null)
			valorTemporario = BigDecimal.ZERO;
		return valorTemporario;
	}

	public void setValorTemporario(BigDecimal valorTemporario) {
		this.valorTemporario = valorTemporario;
	}

	public Boolean getValorInformado() {
		if (valorInformado == null) {
			valorInformado = false;
		}
		return valorInformado;
	}

	public void setValorInformado(Boolean informadoManual) {
		this.valorInformado = informadoManual;
	}

	public UsuarioVO getUsuarioUltimaAlteracao() {
		if (usuarioUltimaAlteracao == null) {
			usuarioUltimaAlteracao = new UsuarioVO();
		}
		return usuarioUltimaAlteracao;
	}

	public void setUsuarioUltimaAlteracao(UsuarioVO usuarioUltimaAlteracao) {
		this.usuarioUltimaAlteracao = usuarioUltimaAlteracao;
	}

	public Date getDataUltimaAlteracao() {
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public String getReferencia() {
		if (referencia == null)
			referencia = "";
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public ContraChequeEventoVO getContraChequeEventoVO() {
		if (contraChequeEventoVO == null)
			contraChequeEventoVO = new ContraChequeEventoVO();
		return contraChequeEventoVO;
	}

	public void setContraChequeEventoVO(ContraChequeEventoVO contraChequeEventoVO) {
		this.contraChequeEventoVO = contraChequeEventoVO;
	}

	public Boolean getValeTransporte() {
		if (valeTransporte == null) {
			valeTransporte = Boolean.FALSE;
		}
		return valeTransporte;
	}

	public void setValeTransporte(Boolean valeTransporte) {
		this.valeTransporte = valeTransporte;
	}

	public Boolean getValeTransporteDesconto() {
		if (valeTransporteDesconto == null) {
			valeTransporteDesconto = Boolean.FALSE;
		}
		return valeTransporteDesconto;
	}

	public void setValeTransporteDesconto(Boolean valeTransporteDesconto) {
		this.valeTransporteDesconto = valeTransporteDesconto;
	}

	public Boolean getAdmissao() {
		if (admissao == null) {
			admissao = Boolean.FALSE;
		}
		return admissao;
	}

	public void setAdmissao(Boolean admissao) {
		this.admissao = admissao;
	}

	public Boolean getDemissao() {
		if (demissao == null) {
			demissao = Boolean.FALSE;
		}
		return demissao;
	}

	public void setDemissao(Boolean demissao) {
		this.demissao = demissao;
	}

	public Boolean getFerias() {
		if (ferias == null) {
			ferias = Boolean.FALSE;
		}
		return ferias;
	}

	public void setFerias(Boolean ferias) {
		this.ferias = ferias;
	}

	public Boolean getAfastamento() {
		if (afastamento == null) {
			afastamento = Boolean.FALSE;
		}
		return afastamento;
	}

	public void setAfastamento(Boolean afastamento) {
		this.afastamento = afastamento;
	}

	public Boolean getEventoPadrao() {
		if (eventoPadrao == null) {
			eventoPadrao = Boolean.FALSE;
		}
		return eventoPadrao;
	}

	public void setEventoPadrao(Boolean eventoPadrao) {
		this.eventoPadrao = eventoPadrao;
	}

	public List<EventoFolhaPagamentoItemVO> getEventoFolhaPagamentoItemVOs() {
		if (eventoFolhaPagamentoItemVOs == null) {
			eventoFolhaPagamentoItemVOs = new ArrayList<>();
		}
		return eventoFolhaPagamentoItemVOs;
	}

	public void setEventoFolhaPagamentoItemVOs(List<EventoFolhaPagamentoItemVO> eventoFolhaPagamentoItemVOs) {
		this.eventoFolhaPagamentoItemVOs = eventoFolhaPagamentoItemVOs;
	}

	public CategoriaEventoFolhaEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEventoFolhaEnum categoria) {
		this.categoria = categoria;
	}

	public Boolean getIncideAdicionalTempoServico() {
		if (incideAdicionalTempoServico == null)
			incideAdicionalTempoServico = false;
		return incideAdicionalTempoServico;
	}

	public void setIncideAdicionalTempoServico(Boolean incideAdicionalTempoServico) {
		this.incideAdicionalTempoServico = incideAdicionalTempoServico;
	}

	public Boolean getIncideAssociacaoSindicato() {
		if (incideAssociacaoSindicato == null)
			incideAssociacaoSindicato = false;
		return incideAssociacaoSindicato;
	}

	public void setIncideAssociacaoSindicato(Boolean incideAssociacaoSindicato) {
		this.incideAssociacaoSindicato = incideAssociacaoSindicato;
	}

	public Boolean getAgrupamentoFolhaNormal() {
		if (agrupamentoFolhaNormal == null)
			agrupamentoFolhaNormal = false;
		return agrupamentoFolhaNormal;
	}

	public void setAgrupamentoFolhaNormal(Boolean agrupamentoFolhaNormal) {
		this.agrupamentoFolhaNormal = agrupamentoFolhaNormal;
	}

	public Boolean getAgrupamentoFerias() {
		if (agrupamentoFerias == null) {
			agrupamentoFerias = false;
		}
		return agrupamentoFerias;
	}

	public void setAgrupamentoFerias(Boolean agrupamentoFerias) {
		this.agrupamentoFerias = agrupamentoFerias;
	}

	public Boolean getAgrupamentoFeriasProporcionais() {
		if (agrupamentoFeriasProporcionais == null)
			agrupamentoFeriasProporcionais = false;
		return agrupamentoFeriasProporcionais;
	}

	public void setAgrupamentoFeriasProporcionais(Boolean agrupamentoFeriasProporcionais) {
		this.agrupamentoFeriasProporcionais = agrupamentoFeriasProporcionais;
	}

	public Boolean getAgrupamentoFeriasVencidas() {
		if (agrupamentoFeriasVencidas == null)
			agrupamentoFeriasVencidas = false;

		return agrupamentoFeriasVencidas;
	}

	public void setAgrupamentoFeriasVencidas(Boolean agrupamentoFeriasVencidas) {
		this.agrupamentoFeriasVencidas = agrupamentoFeriasVencidas;
	}

	public Boolean getAgrupamentoDecimoTerceiro() {
		if (agrupamentoDecimoTerceiro == null)
			agrupamentoDecimoTerceiro = false;
		return agrupamentoDecimoTerceiro;
	}

	public void setAgrupamentoDecimoTerceiro(Boolean agrupamentoDecimoTerceiro) {
		this.agrupamentoDecimoTerceiro = agrupamentoDecimoTerceiro;
	}

	public Boolean getAgrupamentoDecimoTerceiroProporcionais() {
		if (agrupamentoDecimoTerceiroProporcionais == null)
			agrupamentoDecimoTerceiroProporcionais = false;
		return agrupamentoDecimoTerceiroProporcionais;
	}

	public void setAgrupamentoDecimoTerceiroProporcionais(Boolean agrupamentoDecimoTerceiroProporcionais) {
		this.agrupamentoDecimoTerceiroProporcionais = agrupamentoDecimoTerceiroProporcionais;
	}

	public List<EventoFolhaPagamentoMediaVO> getEventoMediaVOs() {
		if (eventoMediaVOs == null)
			eventoMediaVOs = new ArrayList<>();
		return eventoMediaVOs;
	}

	public void setEventoMediaVOs(List<EventoFolhaPagamentoMediaVO> eventoMediaVOs) {
		this.eventoMediaVOs = eventoMediaVOs;
	}

	public ReciboFeriasEventoVO getReciboFeriasEventoVO() {
		if (reciboFeriasEventoVO == null)
			reciboFeriasEventoVO = new ReciboFeriasEventoVO();
		return reciboFeriasEventoVO;
	}

	public void setReciboFeriasEventoVO(ReciboFeriasEventoVO reciboFeriasEventoVO) {
		this.reciboFeriasEventoVO = reciboFeriasEventoVO;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof EventoFolhaPagamentoVO) {

			if (((EventoFolhaPagamentoVO) obj).getCodigo().equals(getCodigo()))
				return true;

		}
		return false;
	}

	public NaturezaEventoFolhaPagamentoEnum getNaturezaEvento() {
		if (naturezaEvento == null)
			naturezaEvento = NaturezaEventoFolhaPagamentoEnum.OUTROS;
		return naturezaEvento;
	}

	public void setNaturezaEvento(NaturezaEventoFolhaPagamentoEnum naturezaEvento) {
		this.naturezaEvento = naturezaEvento;
	}

	public Boolean getIncideAdicionalTempoServicoFerias() {
		if (incideAdicionalTempoServicoFerias == null)
			incideAdicionalTempoServicoFerias = false;
		return incideAdicionalTempoServicoFerias;
	}

	public void setIncideAdicionalTempoServicoFerias(Boolean incideAdicionalTempoServicoFerias) {
		this.incideAdicionalTempoServicoFerias = incideAdicionalTempoServicoFerias;
	}

	public Boolean getIncideAdicionalTempoServico13() {
		if (incideAdicionalTempoServico13 == null)
			incideAdicionalTempoServico13 = false;
		return incideAdicionalTempoServico13;
	}

	public void setIncideAdicionalTempoServico13(Boolean incideAdicionalTempoServico13) {
		this.incideAdicionalTempoServico13 = incideAdicionalTempoServico13;
	}

	public Boolean getIncideAdicionalTempoServicoRescisao() {
		if (incideAdicionalTempoServicoRescisao == null)
			incideAdicionalTempoServicoRescisao = false;
		return incideAdicionalTempoServicoRescisao;
	}

	public void setIncideAdicionalTempoServicoRescisao(Boolean incideAdicionalTempoServicoRescisao) {
		this.incideAdicionalTempoServicoRescisao = incideAdicionalTempoServicoRescisao;
	}

	public Boolean getInssFerias() {
		if (inssFerias == null)
			inssFerias = false;
		return inssFerias;
	}

	public void setInssFerias(Boolean inssFerias) {
		this.inssFerias = inssFerias;
	}

	public Boolean getPrevidenciaObrigatoriaFerias() {
		if (previdenciaObrigatoriaFerias == null) {
			previdenciaObrigatoriaFerias = false;
		}
		return previdenciaObrigatoriaFerias;
	}

	public void setPrevidenciaObrigatoriaFerias(Boolean previdenciaObrigatoriaFerias) {
		this.previdenciaObrigatoriaFerias = previdenciaObrigatoriaFerias;
	}

	public Boolean getIncideAssociacaoSindicatoFerias() {
		if (incideAssociacaoSindicatoFerias == null) {
			incideAssociacaoSindicatoFerias = false;
		}
		return incideAssociacaoSindicatoFerias;
	}

	public void setIncideAssociacaoSindicatoFerias(Boolean incideAssociacaoSindicatoFerias) {
		this.incideAssociacaoSindicatoFerias = incideAssociacaoSindicatoFerias;
	}

	public Boolean getPrevidenciaPropriaFerias() {
		if (previdenciaPropriaFerias == null) {
			previdenciaPropriaFerias = false;
		}
		return previdenciaPropriaFerias;
	}

	public void setPrevidenciaPropriaFerias(Boolean previdenciaPropriaFerias) {
		this.previdenciaPropriaFerias = previdenciaPropriaFerias;
	}

	public Boolean getIncidePlanoSaudeFerias() {
		if (incidePlanoSaudeFerias == null) {
			incidePlanoSaudeFerias = Boolean.FALSE;
		}
		return incidePlanoSaudeFerias;
	}

	public void setIncidePlanoSaudeFerias(Boolean incidePlanoSaudeFerias) {
		this.incidePlanoSaudeFerias = incidePlanoSaudeFerias;
	}

	public Boolean getPermiteDuplicarContraCheque() {
		if (permiteDuplicarContraCheque == null)
			permiteDuplicarContraCheque = false;
		return permiteDuplicarContraCheque;
	}

	public void setPermiteDuplicarContraCheque(Boolean permiteDuplicarContraCheque) {
		this.permiteDuplicarContraCheque = permiteDuplicarContraCheque;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public Boolean getIncidePrevidenciaObrigatoriaDecimoTerceiro() {
		if (incidePrevidenciaObrigatoriaDecimoTerceiro == null) {
			incidePrevidenciaObrigatoriaDecimoTerceiro = Boolean.FALSE;
		}
		return incidePrevidenciaObrigatoriaDecimoTerceiro;
	}

	public void setIncidePrevidenciaObrigatoriaDecimoTerceiro(Boolean incidePrevidenciaObrigatoriaDecimoTerceiro) {
		this.incidePrevidenciaObrigatoriaDecimoTerceiro = incidePrevidenciaObrigatoriaDecimoTerceiro;
	}

	public Boolean getIncideAssociacaoSindicatoDecimoTerceiro() {
		if (incideAssociacaoSindicatoDecimoTerceiro == null) {
			incideAssociacaoSindicatoDecimoTerceiro = Boolean.FALSE;
		}
		return incideAssociacaoSindicatoDecimoTerceiro;
	}

	public void setIncideAssociacaoSindicatoDecimoTerceiro(Boolean incideAssociacaoSindicatoDecimoTerceiro) {
		this.incideAssociacaoSindicatoDecimoTerceiro = incideAssociacaoSindicatoDecimoTerceiro;
	}

	public Boolean getInformadoManual() {
		if (informadoManual == null) {
			informadoManual = Boolean.FALSE;
		}
		return informadoManual;
	}

	public void setInformadoManual(Boolean informadoManual) {
		this.informadoManual = informadoManual;
	}
}