package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.SituacaoParcelaRemoverSerasaApiGeo;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoContratoAgenteNegativacaoCobrancaEnum;
import negocio.comuns.utilitarias.dominios.DiaSemanaJob;
import negocio.comuns.utilitarias.dominios.Horas;
import webservice.nfse.generic.AmbienteEnum;

public class AgenteNegativacaoCobrancaContaReceberVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nome;
	private TipoAgenteNegativacaoCobrancaContaReceberEnum tipo;
	private Boolean possuiIntegracao;
	private IntegracaoNegativacaoCobrancaContaReceberEnum integracao;
	private Boolean enviarValorTitulosUnificadosSerasa;
	private String apiKeySerasaApiGeo;
	private String senhaApiSerasaApiGeo;
	private boolean negativarNegociacoesVencidaSemParcelaRecebida=false;
	private Boolean registrarAutomaticamenteContasNegativacao;
	private Boolean validarDocumentosEntregue;
	private Boolean desconsiderarAlunoDisciplinaReprovadas;
	private Integer quantidadeMinimaDisciplinaReprovada;
	private Integer quantidadeDiasConsiderarParcelaVencida;
	private Integer quantidadeDiasConsiderarParcelaVencidaInicial;
	private List<TipoDocumentoPendenciaAgenteCobrancaVO> tipoDocumentoPendenciaAgenteCobrancaVOs;
	private Boolean tipoOrigemMatricula;
	private Boolean tipoOrigemMensalidade;
	private Boolean tipoOrigemBiblioteca;
	private Boolean tipoOrigemDevolucaoCheque;
	private Boolean tipoOrigemNegociacao;
	private Boolean tipoOrigemBolsaCusteadaConvenio;
	private Boolean tipoOrigemContratoReceita;
	private Boolean tipoOrigemOutros;
	private Boolean tipoOrigemInclusaoReposicao;
	private Boolean tipoOrigemInscricaoProcessoSeletivo;
	private Boolean tipoOrigemRequerimento;
	private Boolean tipoOrigemMaterialDidatico;
	private List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> agenteNegativacaoCobrancaUnidadeEnsinoVOs;
	private Boolean negativarTodasParcelas;
	private TipoContratoAgenteNegativacaoCobrancaEnum tipoContratoAgenteNegativacaoCobrancaEnum;
	private DiaSemanaJob diaSemanaBaseRegistrarContasNegativacao;
	private Horas horaExecucaoRotinaRegistrarContasNegativacao;
	
	private Boolean removerAutomaticamenteContasNegativacao;
	private SituacaoParcelaRemoverSerasaApiGeo situacaoParcelaRemoverSerasaApiGeo;
	private String situacaoParcelaRemoverNegociada;	
	private DiaSemanaJob diaSemanaBaseRemoverContasNegativacao;
	private Horas horaExecucaoRotinaRemoverContasNegativacao;
	private UnidadeEnsinoVO credorUnidadeEnsinoVO;
	
	private AmbienteEnum ambienteAgenteNegativacaoCobranca;
	private Boolean marcarTodosTipoOrigem;
	
	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public TipoAgenteNegativacaoCobrancaContaReceberEnum getTipo() {
		if (tipo == null) {
			tipo = TipoAgenteNegativacaoCobrancaContaReceberEnum.NENHUM;
		}
		return tipo;
	}
	
	public void setTipo(TipoAgenteNegativacaoCobrancaContaReceberEnum tipo) {
		this.tipo = tipo;
	}
	
	public Boolean getTipoAmbos() {
		return getTipo().equals(TipoAgenteNegativacaoCobrancaContaReceberEnum.AMBOS);
	}
	
	public Boolean getPossuiIntegracao() {
		if (possuiIntegracao == null) {
			possuiIntegracao = false;
		}
		return possuiIntegracao;
	}
	
	public void setPossuiIntegracao(Boolean possuiIntegracao) {
		this.possuiIntegracao = possuiIntegracao;
	}
	
	public IntegracaoNegativacaoCobrancaContaReceberEnum getIntegracao() {
		if (integracao == null) {
			integracao = IntegracaoNegativacaoCobrancaContaReceberEnum.NENHUM;
		}
		return integracao;
	}
	
	public void setIntegracao(IntegracaoNegativacaoCobrancaContaReceberEnum integracao) {
		this.integracao = integracao;
	}
	
	public Boolean getIntegracaoSerasa() {
		return getIntegracao().equals(IntegracaoNegativacaoCobrancaContaReceberEnum.SERASA);
	}

	public Boolean getIntegracaoCECAM() {
		return getIntegracao().equals(IntegracaoNegativacaoCobrancaContaReceberEnum.CECAM);
	}
	
	public Boolean getEnviarValorTitulosUnificadosSerasa() {
		if (enviarValorTitulosUnificadosSerasa == null) {
			enviarValorTitulosUnificadosSerasa = false;
		}
		return enviarValorTitulosUnificadosSerasa;
	}
	
	public void setEnviarValorTitulosUnificadosSerasa(Boolean enviarValorTitulosUnificadosSerasa) {
		this.enviarValorTitulosUnificadosSerasa = enviarValorTitulosUnificadosSerasa;
	}
	
	public Boolean getIntegracaoSerasaApiGeo() {
		return getPossuiIntegracao() && getIntegracao().equals(IntegracaoNegativacaoCobrancaContaReceberEnum.SERASA_API_GEO) ;
	}
	
	public Boolean getIntegracaoRegistroContasSerasaApiGeo() {
		return getIntegracaoSerasaApiGeo() && getRegistrarAutomaticamenteContasNegativacao();
	}
	
	public Boolean getIntegracaoRemoverContasSerasaApiGeo() {
		return getIntegracaoSerasaApiGeo() && getRemoverAutomaticamenteContasNegativacao();
	}

	public String getApiKeySerasaApiGeo() {
		if (apiKeySerasaApiGeo == null) {
			apiKeySerasaApiGeo = "";
		}
		return apiKeySerasaApiGeo;
	}

	public void setApiKeySerasaApiGeo(String apiKeySerasaApiGeo) {
		this.apiKeySerasaApiGeo = apiKeySerasaApiGeo;
	}

	public String getSenhaApiSerasaApiGeo() {
		if (senhaApiSerasaApiGeo == null) {
			senhaApiSerasaApiGeo = "";
		}
		return senhaApiSerasaApiGeo;
	}

	public void setSenhaApiSerasaApiGeo(String senhaApiSerasaApiGeo) {
		this.senhaApiSerasaApiGeo = senhaApiSerasaApiGeo;
	}

	public Boolean getRegistrarAutomaticamenteContasNegativacao() {
		if (registrarAutomaticamenteContasNegativacao == null) {
			registrarAutomaticamenteContasNegativacao = false;
		}
		return registrarAutomaticamenteContasNegativacao;
	}

	public void setRegistrarAutomaticamenteContasNegativacao(Boolean registrarAutomaticamenteContasNegativacao) {
		this.registrarAutomaticamenteContasNegativacao = registrarAutomaticamenteContasNegativacao;
	}

	

	public boolean isNegativarNegociacoesVencidaSemParcelaRecebida() {
		return negativarNegociacoesVencidaSemParcelaRecebida;
	}

	public void setNegativarNegociacoesVencidaSemParcelaRecebida(boolean negativarNegociacoesVencidaSemParcelaRecebida) {
		this.negativarNegociacoesVencidaSemParcelaRecebida = negativarNegociacoesVencidaSemParcelaRecebida;
	}

	public Boolean getValidarDocumentosEntregue() {
		if (validarDocumentosEntregue == null) {
			validarDocumentosEntregue = false;
		}
		return validarDocumentosEntregue;
	}

	public void setValidarDocumentosEntregue(Boolean validarDocumentosEntregue) {
		this.validarDocumentosEntregue = validarDocumentosEntregue;
	}

	public Boolean getDesconsiderarAlunoDisciplinaReprovadas() {
		if (desconsiderarAlunoDisciplinaReprovadas == null) {
			desconsiderarAlunoDisciplinaReprovadas = false;
		}
		return desconsiderarAlunoDisciplinaReprovadas;
	}

	public void setDesconsiderarAlunoDisciplinaReprovadas(Boolean desconsiderarAlunoDisciplinaReprovadas) {
		this.desconsiderarAlunoDisciplinaReprovadas = desconsiderarAlunoDisciplinaReprovadas;
	}

	public Integer getQuantidadeMinimaDisciplinaReprovada() {
		if (quantidadeMinimaDisciplinaReprovada == null) {
			quantidadeMinimaDisciplinaReprovada = 0;
		}
		return quantidadeMinimaDisciplinaReprovada;
	}

	public void setQuantidadeMinimaDisciplinaReprovada(Integer quantidadeMinimaDisciplinaReprovada) {
		this.quantidadeMinimaDisciplinaReprovada = quantidadeMinimaDisciplinaReprovada;
	}

	public Integer getQuantidadeDiasConsiderarParcelaVencida() {
		if (quantidadeDiasConsiderarParcelaVencida == null) {
			quantidadeDiasConsiderarParcelaVencida = 31;
		}
		return quantidadeDiasConsiderarParcelaVencida;
	}

	public void setQuantidadeDiasConsiderarParcelaVencida(Integer quantidadeDiasConsiderarParcelaVencida) {
		this.quantidadeDiasConsiderarParcelaVencida = quantidadeDiasConsiderarParcelaVencida;
	}
	public Integer getQuantidadeDiasConsiderarParcelaVencidaInicial() {
		if (quantidadeDiasConsiderarParcelaVencidaInicial == null) {
			quantidadeDiasConsiderarParcelaVencidaInicial = 365;
		}
		return quantidadeDiasConsiderarParcelaVencidaInicial;
	}
	
	public void setQuantidadeDiasConsiderarParcelaVencidaInicial(Integer quantidadeDiasConsiderarParcelaVencidaInicial) {
		this.quantidadeDiasConsiderarParcelaVencidaInicial = quantidadeDiasConsiderarParcelaVencidaInicial;
	}

	public List<TipoDocumentoPendenciaAgenteCobrancaVO> getTipoDocumentoPendenciaAgenteCobrancaVOs() {
		if (tipoDocumentoPendenciaAgenteCobrancaVOs == null) {
			tipoDocumentoPendenciaAgenteCobrancaVOs =  new ArrayList<TipoDocumentoPendenciaAgenteCobrancaVO>();
		}
		return tipoDocumentoPendenciaAgenteCobrancaVOs;
	}

	public void setTipoDocumentoPendenciaAgenteCobrancaVOs(
			List<TipoDocumentoPendenciaAgenteCobrancaVO> tipoDocumentoPendenciaAgenteCobrancaVOs) {
		this.tipoDocumentoPendenciaAgenteCobrancaVOs = tipoDocumentoPendenciaAgenteCobrancaVOs;
	}
	
	public String getTipoDocumentoPendenciaAgenteCobrancaVOApresentar() {
		StringBuilder sb = new StringBuilder("");
		if(getTipoDocumentoPendenciaAgenteCobrancaVOs().size() == 1) {
			sb.append(getTipoDocumentoPendenciaAgenteCobrancaVOs().get(0).getTipoDocumento().getNome());
		}else if(getTipoDocumentoPendenciaAgenteCobrancaVOs().size() > 1) {
			getTipoDocumentoPendenciaAgenteCobrancaVOs().stream().forEach(p->{
				sb.append(p.getTipoDocumento().getNome()).append("; ");
			});
		}
		return sb.toString();
	}

	public Boolean getTipoOrigemMatricula() {
		if (tipoOrigemMatricula == null) {
			tipoOrigemMatricula =  false;
		}
		return tipoOrigemMatricula;
	}

	public void setTipoOrigemMatricula(Boolean tipoOrigemMatricula) {
		this.tipoOrigemMatricula = tipoOrigemMatricula;
	}

	public Boolean getTipoOrigemMensalidade() {
		if (tipoOrigemMensalidade == null) {
			tipoOrigemMensalidade =  false;
		}
		return tipoOrigemMensalidade;
	}

	public void setTipoOrigemMensalidade(Boolean tipoOrigemMensalidade) {
		this.tipoOrigemMensalidade = tipoOrigemMensalidade;
	}

	public Boolean getTipoOrigemBiblioteca() {
		if (tipoOrigemBiblioteca == null) {
			tipoOrigemBiblioteca =  false;
		}
		return tipoOrigemBiblioteca;
	}

	public void setTipoOrigemBiblioteca(Boolean tipoOrigemBiblioteca) {
		this.tipoOrigemBiblioteca = tipoOrigemBiblioteca;
	}

	public Boolean getTipoOrigemDevolucaoCheque() {
		if (tipoOrigemDevolucaoCheque == null) {
			tipoOrigemDevolucaoCheque =  false;
		}
		return tipoOrigemDevolucaoCheque;
	}

	public void setTipoOrigemDevolucaoCheque(Boolean tipoOrigemDevolucaoCheque) {
		this.tipoOrigemDevolucaoCheque = tipoOrigemDevolucaoCheque;
	}

	public Boolean getTipoOrigemNegociacao() {
		if (tipoOrigemNegociacao == null) {
			tipoOrigemNegociacao =  false;
		}
		return tipoOrigemNegociacao;
	}

	public void setTipoOrigemNegociacao(Boolean tipoOrigemNegociacao) {
		this.tipoOrigemNegociacao = tipoOrigemNegociacao;
	}

	public Boolean getTipoOrigemBolsaCusteadaConvenio() {
		if (tipoOrigemBolsaCusteadaConvenio == null) {
			tipoOrigemBolsaCusteadaConvenio =  false;
		}
		return tipoOrigemBolsaCusteadaConvenio;
	}

	public void setTipoOrigemBolsaCusteadaConvenio(Boolean tipoOrigemBolsaCusteadaConvenio) {
		this.tipoOrigemBolsaCusteadaConvenio = tipoOrigemBolsaCusteadaConvenio;
	}

	public Boolean getTipoOrigemContratoReceita() {
		if (tipoOrigemContratoReceita == null) {
			tipoOrigemContratoReceita =  false;
		}
		return tipoOrigemContratoReceita;
	}

	public void setTipoOrigemContratoReceita(Boolean tipoOrigemContratoReceita) {
		this.tipoOrigemContratoReceita = tipoOrigemContratoReceita;
	}

	public Boolean getTipoOrigemOutros() {
		if (tipoOrigemOutros == null) {
			tipoOrigemOutros =  false;
		}
		return tipoOrigemOutros;
	}

	public void setTipoOrigemOutros(Boolean tipoOrigemOutros) {
		this.tipoOrigemOutros = tipoOrigemOutros;
	}

	public Boolean getTipoOrigemInclusaoReposicao() {
		if (tipoOrigemInclusaoReposicao == null) {
			tipoOrigemInclusaoReposicao =  false;
		}
		return tipoOrigemInclusaoReposicao;
	}

	public void setTipoOrigemInclusaoReposicao(Boolean tipoOrigemInclusaoReposicao) {
		this.tipoOrigemInclusaoReposicao = tipoOrigemInclusaoReposicao;
	}
	

	public Boolean getTipoOrigemInscricaoProcessoSeletivo() {
		return tipoOrigemInscricaoProcessoSeletivo;
	}

	public void setTipoOrigemInscricaoProcessoSeletivo(Boolean tipoOrigemInscricaoProcessoSeletivo) {
		this.tipoOrigemInscricaoProcessoSeletivo = tipoOrigemInscricaoProcessoSeletivo;
	}

	public Boolean getTipoOrigemRequerimento() {
		return tipoOrigemRequerimento;
	}

	public void setTipoOrigemRequerimento(Boolean tipoOrigemRequerimento) {
		this.tipoOrigemRequerimento = tipoOrigemRequerimento;
	}

	public Boolean getTipoOrigemMaterialDidatico() {
		return tipoOrigemMaterialDidatico;
	}

	public void setTipoOrigemMaterialDidatico(Boolean tipoOrigemMaterialDidatico) {
		this.tipoOrigemMaterialDidatico = tipoOrigemMaterialDidatico;
	}

	public List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> getAgenteNegativacaoCobrancaUnidadeEnsinoVOs() {
		if (agenteNegativacaoCobrancaUnidadeEnsinoVOs == null) {
			agenteNegativacaoCobrancaUnidadeEnsinoVOs =  new ArrayList<AgenteNegativacaoCobrancaUnidadeEnsinoVO>();
		}
		return agenteNegativacaoCobrancaUnidadeEnsinoVOs;
	}

	public void setAgenteNegativacaoCobrancaUnidadeEnsinoVOs(List<AgenteNegativacaoCobrancaUnidadeEnsinoVO> agenteNegativacaoCobrancaUnidadeEnsinoVOs) {
		this.agenteNegativacaoCobrancaUnidadeEnsinoVOs = agenteNegativacaoCobrancaUnidadeEnsinoVOs;
	}
	
	public String getAgenteNegativacaoCobrancaUnidadeEnsinoApresentar() {
		StringBuilder sb = new StringBuilder("");
		if(getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().size() == 1) {
			sb.append(getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().get(0).getUnidadeEnsino().getNome());
		}else if(getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().size() > 1) {
			getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().stream().forEach(p->{
				sb.append(p.getUnidadeEnsino().getNome()).append("; ");
			});
		}
		return sb.toString();
	}

	public Boolean getNegativarTodasParcelas() {
		if (negativarTodasParcelas == null) {
			negativarTodasParcelas =  false;
		}
		return negativarTodasParcelas;
	}

	public void setNegativarTodasParcelas(Boolean negativarTodasParcelas) {
		this.negativarTodasParcelas = negativarTodasParcelas;
	}

	public DiaSemanaJob getDiaSemanaBaseRegistrarContasNegativacao() {
		if (diaSemanaBaseRegistrarContasNegativacao == null) {
			diaSemanaBaseRegistrarContasNegativacao =  DiaSemanaJob.TODOS_DIAS;
		}
		return diaSemanaBaseRegistrarContasNegativacao;
	}

	public void setDiaSemanaBaseRegistrarContasNegativacao(DiaSemanaJob diaSemanaBaseRegistrarContasNegativacao) {
		this.diaSemanaBaseRegistrarContasNegativacao = diaSemanaBaseRegistrarContasNegativacao;
	}
	
	
	
	public TipoContratoAgenteNegativacaoCobrancaEnum getTipoContratoAgenteNegativacaoCobrancaEnum() {
		if (tipoContratoAgenteNegativacaoCobrancaEnum == null) {
			tipoContratoAgenteNegativacaoCobrancaEnum = TipoContratoAgenteNegativacaoCobrancaEnum.NOSSO_NUMERO;
		}
		return tipoContratoAgenteNegativacaoCobrancaEnum;
	}

	public void setTipoContratoAgenteNegativacaoCobrancaEnum(TipoContratoAgenteNegativacaoCobrancaEnum tipoContratoAgenteNegativacaoCobrancaEnum) {
		this.tipoContratoAgenteNegativacaoCobrancaEnum = tipoContratoAgenteNegativacaoCobrancaEnum;
	}

	public Horas getHoraExecucaoRotinaRegistrarContasNegativacao() {
		if (horaExecucaoRotinaRegistrarContasNegativacao == null) {
			horaExecucaoRotinaRegistrarContasNegativacao = Horas.ZERO;
		}
		return horaExecucaoRotinaRegistrarContasNegativacao;
	}

	public void setHoraExecucaoRotinaRegistrarContasNegativacao(Horas horaExecucaoRotinaRegistrarContasNegativacao) {
		this.horaExecucaoRotinaRegistrarContasNegativacao = horaExecucaoRotinaRegistrarContasNegativacao;
	}
	
	public Horas getHoraExecucaoRotinaRemoverContasNegativacao() {
		if (horaExecucaoRotinaRemoverContasNegativacao == null) {
			horaExecucaoRotinaRemoverContasNegativacao = Horas.ZERO;
		}
		return horaExecucaoRotinaRemoverContasNegativacao;
	}

	public void setHoraExecucaoRotinaRemoverContasNegativacao(Horas horaExecucaoRotinaRemoverContasNegativacao) {
		this.horaExecucaoRotinaRemoverContasNegativacao = horaExecucaoRotinaRemoverContasNegativacao;
	}
	

	public SituacaoParcelaRemoverSerasaApiGeo getSituacaoParcelaRemoverSerasaApiGeo() {
		if(situacaoParcelaRemoverSerasaApiGeo == null) {
			situacaoParcelaRemoverSerasaApiGeo = SituacaoParcelaRemoverSerasaApiGeo.NENHUM;
		}
		return situacaoParcelaRemoverSerasaApiGeo;
	}

	public void setSituacaoParcelaRemoverSerasaApiGeo(SituacaoParcelaRemoverSerasaApiGeo situacaoParcelaRemoverSerasaApiGeo) {
		this.situacaoParcelaRemoverSerasaApiGeo = situacaoParcelaRemoverSerasaApiGeo;
	}
	
	

	public String getSituacaoParcelaRemoverNegociada() {
		if(situacaoParcelaRemoverNegociada == null) {
			situacaoParcelaRemoverNegociada = "";
		}
		return situacaoParcelaRemoverNegociada;
	}

	public void setSituacaoParcelaRemoverNegociada(String situacaoParcelaRemoverNegociada) {
		this.situacaoParcelaRemoverNegociada = situacaoParcelaRemoverNegociada;
	}

	public Boolean getRemoverAutomaticamenteContasNegativacao() {
		if (removerAutomaticamenteContasNegativacao == null) {
			removerAutomaticamenteContasNegativacao = false;
		}
		return removerAutomaticamenteContasNegativacao;
	}

	public void setRemoverAutomaticamenteContasNegativacao(Boolean removerAutomaticamenteContasNegativacao) {
		this.removerAutomaticamenteContasNegativacao = removerAutomaticamenteContasNegativacao;
	}

	public DiaSemanaJob getDiaSemanaBaseRemoverContasNegativacao() {
		if (diaSemanaBaseRemoverContasNegativacao == null) {
			diaSemanaBaseRemoverContasNegativacao =  DiaSemanaJob.TODOS_DIAS;
		}
		return diaSemanaBaseRemoverContasNegativacao;
	}

	public void setDiaSemanaBaseRemoverContasNegativacao(DiaSemanaJob diaSemanaBaseRemoverContasNegativacao) {
		this.diaSemanaBaseRemoverContasNegativacao = diaSemanaBaseRemoverContasNegativacao;
	}


	public AmbienteEnum getAmbienteAgenteNegativacaoCobranca() {
		if(ambienteAgenteNegativacaoCobranca == null) {
			ambienteAgenteNegativacaoCobranca = AmbienteEnum.HOMOLOGACAO;
		}
		return ambienteAgenteNegativacaoCobranca;
	}

	public void setAmbienteAgenteNegativacaoCobranca(AmbienteEnum ambienteAgenteNegativacaoCobranca) {
		this.ambienteAgenteNegativacaoCobranca = ambienteAgenteNegativacaoCobranca;
	}
	
	public UnidadeEnsinoVO getCredorUnidadeEnsinoVO() {
		if(credorUnidadeEnsinoVO == null) {
			credorUnidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return credorUnidadeEnsinoVO;
	}

	public void setCredorUnidadeEnsinoVO(UnidadeEnsinoVO credorUnidadeEnsinoVO) {
		this.credorUnidadeEnsinoVO = credorUnidadeEnsinoVO;
	}

	public Boolean getMarcarTodosTipoOrigem() {
		if (marcarTodosTipoOrigem == null) {
			marcarTodosTipoOrigem = Boolean.TRUE;
		}
		return marcarTodosTipoOrigem;
	}

	public void setMarcarTodosTipoOrigem(Boolean marcarTodosTipoOrigem) {
		this.marcarTodosTipoOrigem = marcarTodosTipoOrigem;
	}
	
	
	public void realizarSelecaoTodasOrigens(boolean selecionado){
		setTipoOrigemBiblioteca(selecionado);
		setTipoOrigemBolsaCusteadaConvenio(selecionado);
		setTipoOrigemContratoReceita(selecionado);
		setTipoOrigemDevolucaoCheque(selecionado);
		setTipoOrigemInclusaoReposicao(selecionado);
		setTipoOrigemMatricula(selecionado);
		setTipoOrigemMensalidade(selecionado);
		setTipoOrigemNegociacao(selecionado);
		setTipoOrigemOutros(selecionado);
		setTipoOrigemInscricaoProcessoSeletivo(selecionado);
		setTipoOrigemMaterialDidatico(selecionado);
		setTipoOrigemRequerimento(selecionado);
	}
	
	public Boolean getApresentarSituacaoParcelaNegociada() {
		return getSituacaoParcelaRemoverSerasaApiGeo() != null && (getSituacaoParcelaRemoverSerasaApiGeo().equals(SituacaoParcelaRemoverSerasaApiGeo.NEGOCIADO) || getSituacaoParcelaRemoverSerasaApiGeo().equals(SituacaoParcelaRemoverSerasaApiGeo.RECEBIDO_NEGOCIADO));
	}
	
	
}