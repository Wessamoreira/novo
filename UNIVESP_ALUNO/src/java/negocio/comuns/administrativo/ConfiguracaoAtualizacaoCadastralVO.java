package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Cargo. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ConfiguracaoAtualizacaoCadastralVO extends SuperVO {

    private Integer codigo;
    private Integer configuracaoGeralSistema;
    private Boolean habilitarRecursoAtualizacaoCadastral;
    private Boolean apresentarCampoEndereco;
    private Boolean trazerDadoEnderecoEmBranco;
    private Boolean enderecoObrigatorio;
    private String styleEnderecoObrigatorio;
    private Boolean apresentarCampoCertidaoNasc;
    private Boolean trazerDadoCertidaoNascEmBranco;
    private Boolean certidaoNascObrigatorio;
    private String styleCertidaoNascObrigatorio;
    private Boolean apresentarCampoContato;
    private Boolean trazerDadoContatoEmBranco;
    private Boolean celularObrigatorio;
    private String styleCelularObrigatorio;
    private Boolean umTelefoneFixoObrigatorio;
    private String styleUmTelefoneFixoObrigatorio;
    private Boolean emailObrigatorio;
    private String styleEmailObrigatorio;
    private Boolean apresentarCampoDataNascimento;
    private Boolean dataNascimentoObrigatorio;
    private String styleDataNascimentoObrigatorio;
    private Boolean apresentarCampoNaturalidadeNacionalidade;
    private Boolean naturalidadeNacionalidadeObrigatorio;
    private String styleNaturalidadeNacionalidadeObrigatorio;
    private Boolean apresentarCampoCorRaca;
    private Boolean corRacaObrigatorio;
    private String styleCorRacaObrigatorio;
    private Boolean apresentarCampoEstadoCivil;
    private Boolean estadoCivilObrigatorio;
    private String styleEstadoCivilObrigatorio;
    private Boolean apresentarCampoRG;
    private Boolean rgObrigatorio;
    private String styleRgObrigatorio;
    private Boolean apresentarCampoRegistroMilitar;
    private Boolean registroMilitarObrigatorio;
    private String styleRegistroMilitarObrigatorio;
    private Boolean apresentarCampoDadoEleitoral;
    private Boolean dadoEleitoralObrigatorio;
    private String styledadoEleitoralObrigatorio;
    private Boolean apresentarCampoEnderecoFiliacao;
    private Boolean apresentarCampoCpfFiliacao;
    private Boolean apresentarCampoRgFiliacao;
    private Boolean apresentarCampoContatoFiliacao;
    private Boolean apresentarCampoDataNascFiliacao;
    private Boolean apresentarCampoNacionalidadeFiliacao;
    private Boolean apresentarCampoEstadoCivilFiliacao;
    private Boolean apresentarCampoDadoComercial;
    private Boolean apresentarCampoFormacaoAcademica;
    private Boolean apresentarCampoFormacaoExtraCurricular;
    private Boolean apresentarCampoQualificacaoComplementar;
    private Boolean apresentarCampoAreaProfissional;
    private Boolean apresentarCampoBancoCurriculo;
    private Boolean apresentarCampoUploadCurriculo;
    private Boolean apresentarCampoFiliacao;    
    private Boolean apresentarCampoCpf;
    private Boolean permitirAtualizarCpf;
    private Integer solicitarAtualizacaoACada;
    private Boolean permitirAlterarEndereco;
    private Boolean editarCampoRG;
    private Boolean editarCampoDataNascimento;
    private Boolean editarNaturalidadeNacionalidade;
    private Boolean apresentarCampoTranstornosNeurodivergentes;
	private Boolean permitirAlterarTranstornosNeurodivergentes;
    
    public static final long serialVersionUID = 1L;

    public ConfiguracaoAtualizacaoCadastralVO() {
        super();
    }

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getConfiguracaoGeralSistema() {
		if (configuracaoGeralSistema == null) {
			configuracaoGeralSistema = 0;
		}
		return configuracaoGeralSistema;
	}

	public void setConfiguracaoGeralSistema(Integer configuracaoGeralSistema) {
		this.configuracaoGeralSistema = configuracaoGeralSistema;
	}

	public Boolean getHabilitarRecursoAtualizacaoCadastral() {
		if (habilitarRecursoAtualizacaoCadastral == null) {
			habilitarRecursoAtualizacaoCadastral = Boolean.FALSE;
		}
		return habilitarRecursoAtualizacaoCadastral;
	}

	public void setHabilitarRecursoAtualizacaoCadastral(Boolean habilitarRecursoAtualizacaoCadastral) {
		this.habilitarRecursoAtualizacaoCadastral = habilitarRecursoAtualizacaoCadastral;
	}

	public Boolean getApresentarCampoEndereco() {
		if (apresentarCampoEndereco == null) {
			apresentarCampoEndereco = Boolean.TRUE;
		}
		return apresentarCampoEndereco;
	}

	public void setApresentarCampoEndereco(Boolean apresentarCampoEndereco) {
		this.apresentarCampoEndereco = apresentarCampoEndereco;
	}

	public Boolean getTrazerDadoEnderecoEmBranco() {
		if (trazerDadoEnderecoEmBranco == null) {
			trazerDadoEnderecoEmBranco = Boolean.FALSE;
		}
		return trazerDadoEnderecoEmBranco;
	}

	public void setTrazerDadoEnderecoEmBranco(Boolean trazerDadoEnderecoEmBranco) {
		this.trazerDadoEnderecoEmBranco = trazerDadoEnderecoEmBranco;
	}

	public Boolean getEnderecoObrigatorio() {
		if (enderecoObrigatorio == null) {
			enderecoObrigatorio = Boolean.FALSE;
		}
		return enderecoObrigatorio;
	}

	public void setEnderecoObrigatorio(Boolean enderecoObrigatorio) {
		this.enderecoObrigatorio = enderecoObrigatorio;
	}

	public Boolean getApresentarCampoCertidaoNasc() {
		if (apresentarCampoCertidaoNasc == null) {
			apresentarCampoCertidaoNasc = Boolean.TRUE;
		}
		return apresentarCampoCertidaoNasc;
	}
	
	public void setApresentarCampoCertidaoNasc(Boolean apresentarCampoCertidaoNasc) {
		this.apresentarCampoCertidaoNasc = apresentarCampoCertidaoNasc;
	}
	
	public Boolean getTrazerDadoCertidaoNascEmBranco() {
		if (trazerDadoCertidaoNascEmBranco == null) {
			trazerDadoCertidaoNascEmBranco = Boolean.FALSE;
		}
		return trazerDadoCertidaoNascEmBranco;
	}
	
	public void setTrazerDadoCertidaoNascEmBranco(Boolean trazerDadoCertidaoNascEmBranco) {
		this.trazerDadoCertidaoNascEmBranco = trazerDadoCertidaoNascEmBranco;
	}
	
	public Boolean getCertidaoNascObrigatorio() {
		if (certidaoNascObrigatorio == null) {
			certidaoNascObrigatorio = Boolean.FALSE;
		}
		return certidaoNascObrigatorio;
	}
	
	public void setCertidaoNascObrigatorio(Boolean certidaoNascObrigatorio) {
		this.certidaoNascObrigatorio = certidaoNascObrigatorio;
	}
	
	public Boolean getApresentarCampoContato() {
		if (apresentarCampoContato == null) {
			apresentarCampoContato = Boolean.TRUE;
		}
		return apresentarCampoContato;
	}

	public void setApresentarCampoContato(Boolean apresentarCampoContato) {
		this.apresentarCampoContato = apresentarCampoContato;
	}

	public Boolean getTrazerDadoContatoEmBranco() {
		if (trazerDadoContatoEmBranco == null) {
			trazerDadoContatoEmBranco = Boolean.FALSE;
		}
		return trazerDadoContatoEmBranco;
	}

	public void setTrazerDadoContatoEmBranco(Boolean trazerDadoContatoEmBranco) {
		this.trazerDadoContatoEmBranco = trazerDadoContatoEmBranco;
	}

	public Boolean getCelularObrigatorio() {
		if (celularObrigatorio == null) {
			celularObrigatorio = Boolean.FALSE;
		}
		return celularObrigatorio;
	}

	public void setCelularObrigatorio(Boolean celularObrigatorio) {
		this.celularObrigatorio = celularObrigatorio;
	}

	public Boolean getUmTelefoneFixoObrigatorio() {
		if (umTelefoneFixoObrigatorio == null) {
			umTelefoneFixoObrigatorio = Boolean.FALSE;
		}
		return umTelefoneFixoObrigatorio;
	}

	public void setUmTelefoneFixoObrigatorio(Boolean umTelefoneFixoObrigatorio) {
		this.umTelefoneFixoObrigatorio = umTelefoneFixoObrigatorio;
	}

	public Boolean getEmailObrigatorio() {
		if (emailObrigatorio == null) {
			emailObrigatorio = Boolean.FALSE;
		}
		return emailObrigatorio;
	}

	public void setEmailObrigatorio(Boolean emailObrigatorio) {
		this.emailObrigatorio = emailObrigatorio;
	}

	public Boolean getApresentarCampoDataNascimento() {
		if (apresentarCampoDataNascimento == null) {
			apresentarCampoDataNascimento = Boolean.TRUE;
		}
		return apresentarCampoDataNascimento;
	}

	public void setApresentarCampoDataNascimento(Boolean apresentarCampoDataNascimento) {
		this.apresentarCampoDataNascimento = apresentarCampoDataNascimento;
	}

	public Boolean getDataNascimentoObrigatorio() {
		if (dataNascimentoObrigatorio == null) {
			dataNascimentoObrigatorio = Boolean.FALSE;
		}
		return dataNascimentoObrigatorio;
	}

	public void setDataNascimentoObrigatorio(Boolean dataNascimentoObrigatorio) {
		this.dataNascimentoObrigatorio = dataNascimentoObrigatorio;
	}

	public Boolean getApresentarCampoNaturalidadeNacionalidade() {
		if (apresentarCampoNaturalidadeNacionalidade == null) {
			apresentarCampoNaturalidadeNacionalidade = Boolean.TRUE;
		}
		return apresentarCampoNaturalidadeNacionalidade;
	}

	public void setApresentarCampoNaturalidadeNacionalidade(Boolean apresentarCampoNaturalidadeNacionalidade) {
		this.apresentarCampoNaturalidadeNacionalidade = apresentarCampoNaturalidadeNacionalidade;
	}

	public Boolean getNaturalidadeNacionalidadeObrigatorio() {
		if (naturalidadeNacionalidadeObrigatorio == null) {
			naturalidadeNacionalidadeObrigatorio = Boolean.FALSE;
		}
		return naturalidadeNacionalidadeObrigatorio;
	}

	public void setNaturalidadeNacionalidadeObrigatorio(Boolean naturalidadeNacionalidadeObrigatorio) {
		this.naturalidadeNacionalidadeObrigatorio = naturalidadeNacionalidadeObrigatorio;
	}

	public Boolean getApresentarCampoCorRaca() {
		if (apresentarCampoCorRaca == null) {
			apresentarCampoCorRaca = Boolean.TRUE;
		}
		return apresentarCampoCorRaca;
	}

	public void setApresentarCampoCorRaca(Boolean apresentarCampoCorRaca) {
		this.apresentarCampoCorRaca = apresentarCampoCorRaca;
	}

	public Boolean getCorRacaObrigatorio() {
		if (corRacaObrigatorio == null) {
			corRacaObrigatorio = Boolean.FALSE;
		}
		return corRacaObrigatorio;
	}

	public void setCorRacaObrigatorio(Boolean corRacaObrigatorio) {
		this.corRacaObrigatorio = corRacaObrigatorio;
	}

	public Boolean getApresentarCampoEstadoCivil() {
		if (apresentarCampoEstadoCivil == null) {
			apresentarCampoEstadoCivil = Boolean.TRUE;
		}
		return apresentarCampoEstadoCivil;
	}

	public void setApresentarCampoEstadoCivil(Boolean apresentarCampoEstadoCivil) {
		this.apresentarCampoEstadoCivil = apresentarCampoEstadoCivil;
	}

	public Boolean getEstadoCivilObrigatorio() {
		if (estadoCivilObrigatorio == null) {
			estadoCivilObrigatorio = Boolean.FALSE;
		}
		return estadoCivilObrigatorio;
	}

	public void setEstadoCivilObrigatorio(Boolean estadoCivilObrigatorio) {
		this.estadoCivilObrigatorio = estadoCivilObrigatorio;
	}

	public Boolean getApresentarCampoRG() {
		if (apresentarCampoRG == null) {
			apresentarCampoRG = Boolean.TRUE;
		}
		return apresentarCampoRG;
	}

	public void setApresentarCampoRG(Boolean apresentarCampoRG) {
		this.apresentarCampoRG = apresentarCampoRG;
	}

	public Boolean getRgObrigatorio() {
		if (rgObrigatorio == null) {
			rgObrigatorio = Boolean.FALSE;
		}
		return rgObrigatorio;
	}

	public void setRgObrigatorio(Boolean rgObrigatorio) {
		this.rgObrigatorio = rgObrigatorio;
	}

	public Boolean getApresentarCampoRegistroMilitar() {
		if (apresentarCampoRegistroMilitar == null) {
			apresentarCampoRegistroMilitar = Boolean.TRUE;
		}
		return apresentarCampoRegistroMilitar;
	}

	public void setApresentarCampoRegistroMilitar(Boolean apresentarCampoRegistroMilitar) {
		this.apresentarCampoRegistroMilitar = apresentarCampoRegistroMilitar;
	}

	public Boolean getRegistroMilitarObrigatorio() {
		if (registroMilitarObrigatorio == null) {
			registroMilitarObrigatorio = Boolean.FALSE;
		}
		return registroMilitarObrigatorio;
	}

	public void setRegistroMilitarObrigatorio(Boolean registroMilitarObrigatorio) {
		this.registroMilitarObrigatorio = registroMilitarObrigatorio;
	}

	public Boolean getApresentarCampoDadoEleitoral() {
		if (apresentarCampoDadoEleitoral == null) {
			apresentarCampoDadoEleitoral = Boolean.TRUE;
		}
		return apresentarCampoDadoEleitoral;
	}

	public void setApresentarCampoDadoEleitoral(Boolean apresentarCampoDadoEleitoral) {
		this.apresentarCampoDadoEleitoral = apresentarCampoDadoEleitoral;
	}

	public Boolean getDadoEleitoralObrigatorio() {
		if (dadoEleitoralObrigatorio == null) {
			dadoEleitoralObrigatorio = Boolean.FALSE;
		}
		return dadoEleitoralObrigatorio;
	}

	public void setDadoEleitoralObrigatorio(Boolean dadoEleitoralObrigatorio) {
		this.dadoEleitoralObrigatorio = dadoEleitoralObrigatorio;
	}

	public Boolean getApresentarCampoEnderecoFiliacao() {
		if (apresentarCampoEnderecoFiliacao == null) {
			apresentarCampoEnderecoFiliacao = Boolean.TRUE;
		}
		return apresentarCampoEnderecoFiliacao;
	}

	public void setApresentarCampoEnderecoFiliacao(Boolean apresentarCampoEnderecoFiliacao) {
		this.apresentarCampoEnderecoFiliacao = apresentarCampoEnderecoFiliacao;
	}

	public Boolean getApresentarCampoCpfFiliacao() {
		if (apresentarCampoCpfFiliacao == null) {
			apresentarCampoCpfFiliacao = Boolean.TRUE;
		}
		return apresentarCampoCpfFiliacao;
	}

	public void setApresentarCampoCpfFiliacao(Boolean apresentarCampoCpfFiliacao) {
		this.apresentarCampoCpfFiliacao = apresentarCampoCpfFiliacao;
	}

	public Boolean getApresentarCampoRgFiliacao() {
		if (apresentarCampoRgFiliacao == null) {
			apresentarCampoRgFiliacao = Boolean.TRUE;
		}
		return apresentarCampoRgFiliacao;
	}

	public void setApresentarCampoRgFiliacao(Boolean apresentarCampoRgFiliacao) {
		this.apresentarCampoRgFiliacao = apresentarCampoRgFiliacao;
	}

	public Boolean getApresentarCampoContatoFiliacao() {
		if (apresentarCampoContatoFiliacao == null) {
			apresentarCampoContatoFiliacao = Boolean.TRUE;
		}
		return apresentarCampoContatoFiliacao;
	}

	public void setApresentarCampoContatoFiliacao(Boolean apresentarCampoContatoFiliacao) {
		this.apresentarCampoContatoFiliacao = apresentarCampoContatoFiliacao;
	}

	public Boolean getApresentarCampoDataNascFiliacao() {
		if (apresentarCampoDataNascFiliacao == null) {
			apresentarCampoDataNascFiliacao = Boolean.TRUE;
		}
		return apresentarCampoDataNascFiliacao;
	}

	public void setApresentarCampoDataNascFiliacao(Boolean apresentarCampoDataNascFiliacao) {
		this.apresentarCampoDataNascFiliacao = apresentarCampoDataNascFiliacao;
	}

	public Boolean getApresentarCampoNacionalidadeFiliacao() {
		if (apresentarCampoNacionalidadeFiliacao == null) {
			apresentarCampoNacionalidadeFiliacao = Boolean.TRUE;
		}
		return apresentarCampoNacionalidadeFiliacao;
	}

	public void setApresentarCampoNacionalidadeFiliacao(Boolean apresentarCampoNacionalidadeFiliacao) {
		this.apresentarCampoNacionalidadeFiliacao = apresentarCampoNacionalidadeFiliacao;
	}

	public Boolean getApresentarCampoEstadoCivilFiliacao() {
		if (apresentarCampoEstadoCivilFiliacao == null) {
			apresentarCampoEstadoCivilFiliacao = Boolean.TRUE;
		}
		return apresentarCampoEstadoCivilFiliacao;
	}

	public void setApresentarCampoEstadoCivilFiliacao(Boolean apresentarCampoEstadoCivilFiliacao) {
		this.apresentarCampoEstadoCivilFiliacao = apresentarCampoEstadoCivilFiliacao;
	}

	public Boolean getApresentarCampoDadoComercial() {
		if (apresentarCampoDadoComercial == null) {
			apresentarCampoDadoComercial = Boolean.TRUE;
		}
		return apresentarCampoDadoComercial;
	}

	public void setApresentarCampoDadoComercial(Boolean apresentarCampoDadoComercial) {
		this.apresentarCampoDadoComercial = apresentarCampoDadoComercial;
	}

	public Boolean getApresentarCampoFormacaoAcademica() {
		if (apresentarCampoFormacaoAcademica == null) {
			apresentarCampoFormacaoAcademica = Boolean.TRUE;
		}
		return apresentarCampoFormacaoAcademica;
	}

	public void setApresentarCampoFormacaoAcademica(Boolean apresentarCampoFormacaoAcademica) {
		this.apresentarCampoFormacaoAcademica = apresentarCampoFormacaoAcademica;
	}

	public Boolean getApresentarCampoFormacaoExtraCurricular() {
		if (apresentarCampoFormacaoExtraCurricular == null) {
			apresentarCampoFormacaoExtraCurricular = Boolean.TRUE;
		}
		return apresentarCampoFormacaoExtraCurricular;
	}

	public void setApresentarCampoFormacaoExtraCurricular(Boolean apresentarCampoFormacaoExtraCurricular) {
		this.apresentarCampoFormacaoExtraCurricular = apresentarCampoFormacaoExtraCurricular;
	}

	public Boolean getApresentarCampoQualificacaoComplementar() {
		if (apresentarCampoQualificacaoComplementar == null) {
			apresentarCampoQualificacaoComplementar = Boolean.TRUE;
		}
		return apresentarCampoQualificacaoComplementar;
	}

	public void setApresentarCampoQualificacaoComplementar(Boolean apresentarCampoQualificacaoComplementar) {
		this.apresentarCampoQualificacaoComplementar = apresentarCampoQualificacaoComplementar;
	}

	public Boolean getApresentarCampoAreaProfissional() {
		if (apresentarCampoAreaProfissional == null) {
			apresentarCampoAreaProfissional = Boolean.TRUE;
		}
		return apresentarCampoAreaProfissional;
	}

	public void setApresentarCampoAreaProfissional(Boolean apresentarCampoAreaProfissional) {
		this.apresentarCampoAreaProfissional = apresentarCampoAreaProfissional;
	}

	public Integer getSolicitarAtualizacaoACada() {
		if (solicitarAtualizacaoACada == null) {
			solicitarAtualizacaoACada = 0;
		}
		return solicitarAtualizacaoACada;
	}

	public void setSolicitarAtualizacaoACada(Integer solicitarAtualizacaoACada) {
		this.solicitarAtualizacaoACada = solicitarAtualizacaoACada;
	}

	public Boolean getApresentarCampoCpf() {
		if (apresentarCampoCpf == null) {
			apresentarCampoCpf = Boolean.TRUE;
		}
		return apresentarCampoCpf;
	}

	public void setApresentarCampoCpf(Boolean apresentarCampoCpf) {
		this.apresentarCampoCpf = apresentarCampoCpf;
	}

	public Boolean getPermitirAtualizarCpf() {
		if (permitirAtualizarCpf == null) {
			permitirAtualizarCpf = Boolean.FALSE;
		}
		return permitirAtualizarCpf;
	}

	public void setPermitirAtualizarCpf(Boolean permitirAtualizarCpf) {
		this.permitirAtualizarCpf = permitirAtualizarCpf;
	}

	public String getStyleEnderecoObrigatorio() {
		if (getEnderecoObrigatorio()) {
			styleEnderecoObrigatorio = "form-control camposObrigatorios";
		} else {
			styleEnderecoObrigatorio = "form-control campos";
		}
		return styleEnderecoObrigatorio;
	}

	public String getStyleCelularObrigatorio() {
		if (getCelularObrigatorio()) {
			styleCelularObrigatorio = "form-control camposObrigatorios";
		} else {
			styleCelularObrigatorio = "form-control campos";
		}
		return styleCelularObrigatorio;
	}
	
	public String getStyleUmTelefoneFixoObrigatorio() {
		if (getUmTelefoneFixoObrigatorio()) {
			styleUmTelefoneFixoObrigatorio = "form-control camposObrigatorios";
		} else {
			styleUmTelefoneFixoObrigatorio = "form-control campos";
		}
		return styleUmTelefoneFixoObrigatorio;
	}

	public String getStyleEmailObrigatorio() {
		if (getEmailObrigatorio()) {
			styleEmailObrigatorio = "form-control camposObrigatorios";
		} else {
			styleEmailObrigatorio = "form-control campos";
		}
		return styleEmailObrigatorio;
	}

	public String getStyleDataNascimentoObrigatorio() {
		if (getDataNascimentoObrigatorio()) {
			styleDataNascimentoObrigatorio = "form-control camposObrigatorios";
		} else {
			styleDataNascimentoObrigatorio = "form-control campos";
		}		
		return styleDataNascimentoObrigatorio;
	}

	public String getStyleNaturalidadeNacionalidadeObrigatorio() {
		if (getNaturalidadeNacionalidadeObrigatorio()) {
			styleNaturalidadeNacionalidadeObrigatorio = "form-control camposObrigatorios";
		} else {
			styleNaturalidadeNacionalidadeObrigatorio = "form-control campos";
		}
		return styleNaturalidadeNacionalidadeObrigatorio;
	}

	public String getStyleCorRacaObrigatorio() {
		if (getCorRacaObrigatorio()) {
			styleCorRacaObrigatorio = "form-control camposObrigatorios";
		} else {
			styleCorRacaObrigatorio = "form-control campos";
		}
		return styleCorRacaObrigatorio;
	}

	public String getStyleEstadoCivilObrigatorio() {
		if (getEstadoCivilObrigatorio()) {
			styleEstadoCivilObrigatorio = "form-control camposObrigatorios";
		} else {
			styleEstadoCivilObrigatorio = "form-control campos";
		}
		return styleEstadoCivilObrigatorio;
	}

	public String getStyleRgObrigatorio() {
		if (getRgObrigatorio()) {
			styleRgObrigatorio = "form-control camposObrigatorios";
		} else {
			styleRgObrigatorio = "form-control campos";
		}
		return styleRgObrigatorio;
	}

	public String getStyleRegistroMilitarObrigatorio() {
		if (getRegistroMilitarObrigatorio()) {
			styleRegistroMilitarObrigatorio = "form-control camposObrigatorios";
		} else {
			styleRegistroMilitarObrigatorio = "form-control campos";
		}
		return styleRegistroMilitarObrigatorio;
	}

	public String getStyledadoEleitoralObrigatorio() {
		if (getDadoEleitoralObrigatorio()) {
			styledadoEleitoralObrigatorio = "form-control camposObrigatorios";
		} else {
			styledadoEleitoralObrigatorio = "form-control campos";
		}
		return styledadoEleitoralObrigatorio;
	}

	public Boolean getApresentarCampoBancoCurriculo() {
		if (apresentarCampoBancoCurriculo == null) {
			apresentarCampoBancoCurriculo = Boolean.TRUE;
		}
		return apresentarCampoBancoCurriculo;
	}

	public void setApresentarCampoBancoCurriculo(Boolean apresentarCampoBancoCurriculo) {
		this.apresentarCampoBancoCurriculo = apresentarCampoBancoCurriculo;
	}

	public Boolean getApresentarCampoUploadCurriculo() {
		if (apresentarCampoUploadCurriculo == null) {
			apresentarCampoUploadCurriculo = Boolean.TRUE;
		}
		return apresentarCampoUploadCurriculo;
	}

	public void setApresentarCampoUploadCurriculo(Boolean apresentarCampoUploadCurriculo) {
		this.apresentarCampoUploadCurriculo = apresentarCampoUploadCurriculo;
	}

	public Boolean getApresentarCampoFiliacao() {
		if (apresentarCampoFiliacao == null) {
			apresentarCampoFiliacao = Boolean.TRUE;
		}
		return apresentarCampoFiliacao;
	}

	public void setApresentarCampoFiliacao(Boolean apresentarCampoFiliacao) {
		this.apresentarCampoFiliacao = apresentarCampoFiliacao;
	}

	public Boolean getEditarCampoRG() {
		if(editarCampoRG == null) {
			editarCampoRG = Boolean.FALSE;
		}
		return editarCampoRG;
	}

	public void setEditarCampoRG(Boolean editarCampoRG) {
		this.editarCampoRG = editarCampoRG;
	}

	public Boolean getEditarCampoDataNascimento() {
		if(editarCampoDataNascimento == null) {
			editarCampoDataNascimento = Boolean.FALSE;
		}
		return editarCampoDataNascimento;
	}

	public void setEditarCampoDataNascimento(Boolean editarCampoDataNascimento) {
		this.editarCampoDataNascimento = editarCampoDataNascimento;
	}

	public Boolean getEditarNaturalidadeNacionalidade() {
		if(editarNaturalidadeNacionalidade == null) {
			editarNaturalidadeNacionalidade = Boolean.FALSE;
		}
		return editarNaturalidadeNacionalidade;
	}

	public void setEditarNaturalidadeNacionalidade(Boolean editarNaturalidadeNacionalidade) {
		this.editarNaturalidadeNacionalidade = editarNaturalidadeNacionalidade;
	}

	public Boolean getPermitirAlterarEndereco() {
		if(permitirAlterarEndereco == null) {
			permitirAlterarEndereco = Boolean.FALSE;
		}
		return permitirAlterarEndereco;
	}

	public void setPermitirAlterarEndereco(Boolean permitirAlterarEndereco) {
		this.permitirAlterarEndereco = permitirAlterarEndereco;
	}

	public Boolean getApresentarCampoTranstornosNeurodivergentes() {
		if(apresentarCampoTranstornosNeurodivergentes == null) {
			apresentarCampoTranstornosNeurodivergentes = Boolean.FALSE;
		}
		return apresentarCampoTranstornosNeurodivergentes;
	}

	public void setApresentarCampoTranstornosNeurodivergentes(Boolean apresentarCampoTranstornosNeurodivergentes) {
		this.apresentarCampoTranstornosNeurodivergentes = apresentarCampoTranstornosNeurodivergentes;
	}

	public Boolean getPermitirAlterarTranstornosNeurodivergentes() {
		if(permitirAlterarTranstornosNeurodivergentes == null) {
			permitirAlterarTranstornosNeurodivergentes = Boolean.FALSE;
		}
		return permitirAlterarTranstornosNeurodivergentes;
	}

	public void setPermitirAlterarTranstornosNeurodivergentes(Boolean permitirAlterarTranstornosNeurodivergentes) {
		this.permitirAlterarTranstornosNeurodivergentes = permitirAlterarTranstornosNeurodivergentes;
	}
	
	
}
