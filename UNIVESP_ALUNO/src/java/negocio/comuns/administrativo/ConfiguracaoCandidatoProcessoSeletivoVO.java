package negocio.comuns.administrativo;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Classe Value Object referente a ConfiguracaoCandidatoProcessoSeletivo
 * Esta classe contem atributos que definem o comportamento de telas referentes ao
 * ingresso em processos seletivos tanto pela visão do candidato quanto administrador.
 * 
 * @author Usuario
 *
 */
@XmlRootElement(name = "configuracaoCandidatoProcessoSeletivoVO")
public class ConfiguracaoCandidatoProcessoSeletivoVO extends SuperVO {

	private Integer codigo;
	private Boolean apresentarCampoEndereco;
	private Boolean enderecoObrigatorio;
	private Boolean apresentarCamposTelefonicos;
	private Boolean telefoneResidencialObrigatorio;
	private Boolean telefoneComercialObrigatorio;
	private Boolean telefoneRecadoObrigatorio;
	private Boolean telefoneCelularObrigatorio;
	private Boolean apresentarCampoEmail;
	private Boolean emailObrigatorio;
	private Boolean apresentarCampoDataNascimento;
	private Boolean dataNascimentoObrigatorio;
	private Boolean apresentarCampoNaturalidade;
	private Boolean naturalidadeObrigatorio;
	private Boolean apresentarCampoNacionalidade;
	private Boolean nacionalidadeObrigatorio;
	private Boolean apresentarCampoSexo;
	private Boolean sexoObrigatorio;
	private Boolean apresentarCampoCorRaca;
	private Boolean corRacaObrigatorio;
	private Boolean apresentarCampoEstadoCivil;
	private Boolean estadoCivilObrigatorio;
	private Boolean apresentarCampoCasosEspeciais;
	private Boolean apresentarRg;
	private Boolean rgObrigatorio;
	private Boolean apresentarCampoRegistroMilitar;
	private Boolean apresentarCampoTituloEleitoral;
	private Boolean apresentarCampoFormacaoEnsinoMedio;
	private Boolean formacaoEnsinoMedioObrigatorio;
	private Boolean apresentarCampoFiliacao;
	private Boolean maeFiliacaoObrigatorio;
	private Boolean apresentarCampoCertidaoNascimento;
	private Boolean certidaoNascimentoObrigatorio;
	private Boolean apresentarCampoPaiFiliacao;	
	private Boolean apresentarTelefoneResidencial;
	private Boolean apresentarTelefoneComercial;
	private Boolean apresentarTelefoneRecado;
	private Boolean apresentarTelefoneCelular;
	private Boolean apresentarNomeBatismo;
	private Boolean nomeBatismoObrigatorio;
	private Boolean apresentarCampoEnderecoSetor;
	private Boolean apresentarCampoEnderecoNumero;
	private Boolean apresentarCampoEnderecoComplemento;
	private Boolean apresentarCampoEnderecoCidade;

	// precisa?
	public static final long serialVersionUID = 1L;

	private Integer configuracaoGeralSistema;

	public Integer getConfiguracaoGeralSistema() {
		if (configuracaoGeralSistema == null) {
			configuracaoGeralSistema = 0;
		}
		return configuracaoGeralSistema;
	}

	public void setConfiguracaoGeralSistema(Integer configuracaoGeralSistema) {
		this.configuracaoGeralSistema = configuracaoGeralSistema;
	}

	public ConfiguracaoCandidatoProcessoSeletivoVO() {
		super();
	}
    
	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "apresentarCampoEndereco")
	public Boolean getApresentarCampoEndereco() {
		if (apresentarCampoEndereco == null) {
			apresentarCampoEndereco = Boolean.TRUE;
		}
		return apresentarCampoEndereco;
	}

	public void setApresentarCampoEndereco(Boolean apresentarCampoEndereco) {
		this.apresentarCampoEndereco = apresentarCampoEndereco;
	}

	@XmlElement(name = "enderecoObrigatorio",type = Boolean.class)
	public Boolean getEnderecoObrigatorio() {
		if (enderecoObrigatorio == null) {
			enderecoObrigatorio = Boolean.TRUE;
		}
		return enderecoObrigatorio;
	}

	public void setEnderecoObrigatorio(Boolean enderecoObrigatorio) {
		this.enderecoObrigatorio = enderecoObrigatorio;
	}

	@XmlElement(name = "apresentarCamposTelefonicos")
	public Boolean getApresentarCamposTelefonicos() {
		if (apresentarCamposTelefonicos == null) {
			apresentarCamposTelefonicos = Boolean.TRUE;
		}
		return apresentarCamposTelefonicos;
	}

	public void setApresentarCamposTelefonicos(Boolean apresentarCamposTelefonicos) {
		this.apresentarCamposTelefonicos = apresentarCamposTelefonicos;
	}

	@XmlElement(name = "telefoneResidencialObrigatorio")
	public Boolean getTelefoneResidencialObrigatorio() {
		if (telefoneResidencialObrigatorio == null) {
			telefoneResidencialObrigatorio = Boolean.FALSE;
		}
		return telefoneResidencialObrigatorio;
	}

	public void setTelefoneResidencialObrigatorio(Boolean telefoneResidencialObrigatorio) {
		this.telefoneResidencialObrigatorio = telefoneResidencialObrigatorio;
	}

	@XmlElement(name = "telefoneComercialObrigatorio")
	public Boolean getTelefoneComercialObrigatorio() {
		if (telefoneComercialObrigatorio == null) {
			telefoneComercialObrigatorio = Boolean.FALSE;
		}
		return telefoneComercialObrigatorio;
	}

	public void setTelefoneComercialObrigatorio(Boolean telefoneComercialObrigatorio) {
		this.telefoneComercialObrigatorio = telefoneComercialObrigatorio;
	}

	@XmlElement(name = "telefoneRecadoObrigatorio")
	public Boolean getTelefoneRecadoObrigatorio() {
		if (telefoneRecadoObrigatorio == null) {
			telefoneRecadoObrigatorio = Boolean.FALSE;
		}
		return telefoneRecadoObrigatorio;
	}

	public void setTelefoneRecadoObrigatorio(Boolean telefoneRecadoObrigatorio) {
		this.telefoneRecadoObrigatorio = telefoneRecadoObrigatorio;
	}

	@XmlElement(name = "telefoneCelularObrigatorio")
	public Boolean getTelefoneCelularObrigatorio() {
		if (telefoneCelularObrigatorio == null) {
			telefoneCelularObrigatorio = Boolean.FALSE;
		}
		return telefoneCelularObrigatorio;
	}

	public void setTelefoneCelularObrigatorio(Boolean telefoneCelularObrigatorio) {
		this.telefoneCelularObrigatorio = telefoneCelularObrigatorio;
	}

	@XmlElement(name = "apresentarCampoEmail")
	public Boolean getApresentarCampoEmail() {
		if (apresentarCampoEmail == null) {
			apresentarCampoEmail = Boolean.TRUE;
		}
		return apresentarCampoEmail;
	}

	public void setApresentarCampoEmail(Boolean apresentarCampoEmail) {
		this.apresentarCampoEmail = apresentarCampoEmail;
	}

	@XmlElement(name = "emailObrigatorio")
	public Boolean getEmailObrigatorio() {
		if (emailObrigatorio == null) {
			emailObrigatorio = Boolean.FALSE;
		}
		return emailObrigatorio;
	}

	public void setEmailObrigatorio(Boolean emailObrigatorio) {
		this.emailObrigatorio = emailObrigatorio;
	}

	@XmlElement(name = "apresentarCampoDataNascimento")
	public Boolean getApresentarCampoDataNascimento() {
		if (apresentarCampoDataNascimento == null) {
			apresentarCampoDataNascimento = Boolean.TRUE;
		}
		return apresentarCampoDataNascimento;
	}

	public void setApresentarCampoDataNascimento(Boolean apresentarCampoDataNascimento) {
		this.apresentarCampoDataNascimento = apresentarCampoDataNascimento;
	}

	@XmlElement(name = "dataNascimentoObrigatorio")
	public Boolean getDataNascimentoObrigatorio() {
		if (dataNascimentoObrigatorio == null) {
			dataNascimentoObrigatorio = Boolean.FALSE;
		}
		return dataNascimentoObrigatorio;
	}

	public void setDataNascimentoObrigatorio(Boolean dataNascimentoObrigatorio) {
		this.dataNascimentoObrigatorio = dataNascimentoObrigatorio;
	}

	@XmlElement(name = "apresentarCampoNaturalidade")
	public Boolean getApresentarCampoNaturalidade() {
		if (apresentarCampoNaturalidade == null) {
			apresentarCampoNaturalidade = Boolean.TRUE;
		}
		return apresentarCampoNaturalidade;
	}

	public void setApresentarCampoNaturalidade(Boolean apresentarCampoNaturalidade) {
		this.apresentarCampoNaturalidade = apresentarCampoNaturalidade;
	}

	@XmlElement(name = "naturalidadeObrigatorio")
	public Boolean getNaturalidadeObrigatorio() {
		if (naturalidadeObrigatorio == null) {
			naturalidadeObrigatorio = Boolean.FALSE;
		}
		return naturalidadeObrigatorio;
	}

	public void setNaturalidadeObrigatorio(Boolean naturalidadeObrigatorio) {
		this.naturalidadeObrigatorio = naturalidadeObrigatorio;
	}

	@XmlElement(name = "apresentarCampoNacionalidade")
	public Boolean getApresentarCampoNacionalidade() {
		if (apresentarCampoNacionalidade == null) {
			apresentarCampoNacionalidade = Boolean.TRUE;
		}
		return apresentarCampoNacionalidade;
	}

	public void setApresentarCampoNacionalidade(Boolean apresentarCampoNacionalidade) {
		this.apresentarCampoNacionalidade = apresentarCampoNacionalidade;
	}

	@XmlElement(name = "nacionalidadeObrigatorio")
	public Boolean getNacionalidadeObrigatorio() {
		if (nacionalidadeObrigatorio == null) {
			nacionalidadeObrigatorio = Boolean.FALSE;
		}
		return nacionalidadeObrigatorio;
	}

	public void setNacionalidadeObrigatorio(Boolean nacionalidadeObrigatorio) {
		this.nacionalidadeObrigatorio = nacionalidadeObrigatorio;
	}

	@XmlElement(name = "apresentarCampoSexo")
	public Boolean getApresentarCampoSexo() {
		if (apresentarCampoSexo == null) {
			apresentarCampoSexo = Boolean.TRUE;
		}
		return apresentarCampoSexo;
	}

	public void setApresentarCampoSexo(Boolean apresentarCampoSexo) {
		this.apresentarCampoSexo = apresentarCampoSexo;
	}

	@XmlElement(name = "sexoObrigatorio")
	public Boolean getSexoObrigatorio() {
		if(sexoObrigatorio == null) {
			sexoObrigatorio = Boolean.FALSE;
		}
		return sexoObrigatorio;
	}

	public void setSexoObrigatorio(Boolean sexoObrigatorio) {
		this.sexoObrigatorio = sexoObrigatorio;
	}

	@XmlElement(name = "apresentarCampoCorRaca")
	public Boolean getApresentarCampoCorRaca() {
		if (apresentarCampoCorRaca == null) {
			apresentarCampoCorRaca = Boolean.TRUE;
		}
		return apresentarCampoCorRaca;
	}

	public void setApresentarCampoCorRaca(Boolean apresentarCampoCorRaca) {
		this.apresentarCampoCorRaca = apresentarCampoCorRaca;
	}

	@XmlElement(name = "corRacaObrigatorio")
	public Boolean getCorRacaObrigatorio() {
		if(corRacaObrigatorio == null) {
			corRacaObrigatorio = Boolean.FALSE;
		}
		return corRacaObrigatorio;
	}

	public void setCorRacaObrigatorio(Boolean corRacaObrigatorio) {
		this.corRacaObrigatorio = corRacaObrigatorio;
	}

	@XmlElement(name = "apresentarCampoEstadoCivil")
	public Boolean getApresentarCampoEstadoCivil() {
		if (apresentarCampoEstadoCivil == null) {
			apresentarCampoEstadoCivil = Boolean.TRUE;
		}
		return apresentarCampoEstadoCivil;
	}

	public void setApresentarCampoEstadoCivil(Boolean apresentarCampoEstadoCivil) {
		this.apresentarCampoEstadoCivil = apresentarCampoEstadoCivil;
	}

	@XmlElement(name = "estadoCivilObrigatorio")
	public Boolean getEstadoCivilObrigatorio() {
		if(estadoCivilObrigatorio == null) {
			estadoCivilObrigatorio = Boolean.FALSE;
		}
		return estadoCivilObrigatorio;
	}

	public void setEstadoCivilObrigatorio(Boolean estadoCivilObrigatorio) {
		this.estadoCivilObrigatorio = estadoCivilObrigatorio;
	}

	@XmlElement(name = "apresentarCampoCasosEspeciais")
	public Boolean getApresentarCampoCasosEspeciais() {
		if (apresentarCampoCasosEspeciais == null) {
			apresentarCampoCasosEspeciais = Boolean.TRUE;
		}
		return apresentarCampoCasosEspeciais;
	}

	public void setApresentarCampoCasosEspeciais(Boolean apresentarCampoCasosEspeciais) {
		this.apresentarCampoCasosEspeciais = apresentarCampoCasosEspeciais;
	}

	@XmlElement(name = "apresentarRg")
	public Boolean getApresentarRg() {
		if (apresentarRg == null) {
			apresentarRg = Boolean.TRUE;
		}
		return apresentarRg;
	}

	public void setApresentarRg(Boolean apresentarRg) {
		this.apresentarRg = apresentarRg;
	}

	@XmlElement(name = "rgObrigatorio")
	public Boolean getRgObrigatorio() {
		if(rgObrigatorio == null) {
			rgObrigatorio = Boolean.FALSE;
		}
		return rgObrigatorio;
	}

	public void setRgObrigatorio(Boolean rgObrigatorio) {
		this.rgObrigatorio = rgObrigatorio;
	}

	@XmlElement(name = "apresentarCampoRegistroMilitar")
	public Boolean getApresentarCampoRegistroMilitar() {
		if (apresentarCampoRegistroMilitar == null) {
			apresentarCampoRegistroMilitar = Boolean.TRUE;
		}
		return apresentarCampoRegistroMilitar;
	}

	public void setApresentarCampoRegistroMilitar(Boolean apresentarCampoRegistroMilitar) {
		this.apresentarCampoRegistroMilitar = apresentarCampoRegistroMilitar;
	}

	@XmlElement(name = "apresentarCampoTituloEleitoral")
	public Boolean getApresentarCampoTituloEleitoral() {
		if (apresentarCampoTituloEleitoral == null) {
			apresentarCampoTituloEleitoral = Boolean.TRUE;
		}
		return apresentarCampoTituloEleitoral;
	}

	public void setApresentarCampoTituloEleitoral(Boolean apresentarCampoTituloEleitoral) {
		this.apresentarCampoTituloEleitoral = apresentarCampoTituloEleitoral;
	}

	@XmlElement(name = "apresentarCampoFormacaoEnsinoMedio")
	public Boolean getApresentarCampoFormacaoEnsinoMedio() {
		if (apresentarCampoFormacaoEnsinoMedio == null) {
			apresentarCampoFormacaoEnsinoMedio = Boolean.TRUE;
		}
		return apresentarCampoFormacaoEnsinoMedio;
	}

	public void setApresentarCampoFormacaoEnsinoMedio(Boolean apresentarCampoFormacaoEnsinoMedio) {
		this.apresentarCampoFormacaoEnsinoMedio = apresentarCampoFormacaoEnsinoMedio;
	}
	
	@XmlElement(name = "formacaoEnsinoMedioObrigatorio", type = Boolean.class)
	public Boolean getFormacaoEnsinoMedioObrigatorio() {
		if(formacaoEnsinoMedioObrigatorio == null) {
			formacaoEnsinoMedioObrigatorio = Boolean.FALSE;
		}
		return formacaoEnsinoMedioObrigatorio;
	}
	
	public void setFormacaoEnsinoMedioObrigatorio(Boolean formacaoEnsinoMedioObrigatorio) {
		this.formacaoEnsinoMedioObrigatorio = formacaoEnsinoMedioObrigatorio;
	}

	@XmlElement(name = "apresentarCampoFiliacao")
	public Boolean getApresentarCampoFiliacao() {
		if (apresentarCampoFiliacao == null) {
			apresentarCampoFiliacao = Boolean.TRUE;
		}
		return apresentarCampoFiliacao;
	}

	public void setApresentarCampoFiliacao(Boolean apresentarCampoFiliacao) {
		this.apresentarCampoFiliacao = apresentarCampoFiliacao;
	}

	@XmlElement(name = "maeFiliacaoObrigatorio")
	public Boolean getMaeFiliacaoObrigatorio() {
		if(maeFiliacaoObrigatorio == null) {
			maeFiliacaoObrigatorio = Boolean.FALSE;
		}
		return maeFiliacaoObrigatorio;
	}

	public void setMaeFiliacaoObrigatorio(Boolean maeFiliacaoObrigatorio) {
		this.maeFiliacaoObrigatorio = maeFiliacaoObrigatorio;
	}

	@XmlElement(name = "apresentarCampoCertidaoNascimento")
	public Boolean getApresentarCampoCertidaoNascimento() {
		if(apresentarCampoCertidaoNascimento== null) {
			apresentarCampoCertidaoNascimento = Boolean.FALSE;
		}
		return apresentarCampoCertidaoNascimento;
	}

	public void setApresentarCampoCertidaoNascimento(Boolean apresentarCampoCertidaoNascimento) {
		this.apresentarCampoCertidaoNascimento = apresentarCampoCertidaoNascimento;
	}

	@XmlElement(name = "certidaoNascimentoObrigatorio")
	public Boolean getCertidaoNascimentoObrigatorio() {
		if(certidaoNascimentoObrigatorio == null) {
			certidaoNascimentoObrigatorio = Boolean.FALSE;
		}
		return certidaoNascimentoObrigatorio;
	}

	public void setCertidaoNascimentoObrigatorio(Boolean certidaoNascimentoObrigatorio) {
		this.certidaoNascimentoObrigatorio = certidaoNascimentoObrigatorio;
	}

	@XmlElement(name = "apresentarCampoPaiFiliacao")
	public Boolean getApresentarCampoPaiFiliacao() {
		if(apresentarCampoPaiFiliacao == null) {
			apresentarCampoPaiFiliacao = Boolean.FALSE;
		}
		return apresentarCampoPaiFiliacao;
	}

	public void setApresentarCampoPaiFiliacao(Boolean apresentarCampoPaiFiliacao) {
		this.apresentarCampoPaiFiliacao = apresentarCampoPaiFiliacao;
	}



	@XmlElement(name = "apresentarTelefoneResidencial")
	public Boolean getApresentarTelefoneResidencial() {
		if(apresentarTelefoneResidencial  ==null) {
			apresentarTelefoneResidencial = Boolean.FALSE;	
		}
		return apresentarTelefoneResidencial;
	}

	public void setApresentarTelefoneResidencial(Boolean apresentarTelefoneResidencial) {
		this.apresentarTelefoneResidencial = apresentarTelefoneResidencial;
	}

	@XmlElement(name = "apresentarTelefoneComercial")
	public Boolean getApresentarTelefoneComercial() {
		if(apresentarTelefoneComercial== null) {
			apresentarTelefoneComercial = Boolean.FALSE;
		}
		return apresentarTelefoneComercial;
	}

	public void setApresentarTelefoneComercial(Boolean apresentarTelefoneComercial) {
		this.apresentarTelefoneComercial = apresentarTelefoneComercial;
	}

	@XmlElement(name = "apresentarTelefoneRecado")
	public Boolean getApresentarTelefoneRecado() {
		if(apresentarTelefoneRecado == null) {
			apresentarTelefoneRecado = Boolean.FALSE;
		}
		return apresentarTelefoneRecado;
	}

	public void setApresentarTelefoneRecado(Boolean apresentarTelefoneRecado) {
		this.apresentarTelefoneRecado = apresentarTelefoneRecado;
	}

	@XmlElement(name = "apresentarTelefoneCelular")
	public Boolean getApresentarTelefoneCelular() {
		if(apresentarTelefoneCelular == null ) {
			apresentarTelefoneCelular = Boolean.FALSE;
		}
		return apresentarTelefoneCelular;
	}

	public void setApresentarTelefoneCelular(Boolean apresentarTelefoneCelular) {
		this.apresentarTelefoneCelular = apresentarTelefoneCelular;
	}

	@XmlElement(name = "apresentarNomeBatismo")
	public Boolean getApresentarNomeBatismo() {
		if(apresentarNomeBatismo == null) {
			apresentarNomeBatismo = Boolean.FALSE;
		}
		return apresentarNomeBatismo;
	}

	public void setApresentarNomeBatismo(Boolean apresentarNomeBatismo) {
		this.apresentarNomeBatismo = apresentarNomeBatismo;
	}

	@XmlElement(name = "nomeBatismoObrigatorio")
	public Boolean getNomeBatismoObrigatorio() {
		if(nomeBatismoObrigatorio == null) {
			nomeBatismoObrigatorio = Boolean.FALSE;
		}
		return nomeBatismoObrigatorio;
	}

	public void setNomeBatismoObrigatorio(Boolean nomeBatismoObrigatorio) {
		this.nomeBatismoObrigatorio = nomeBatismoObrigatorio;
	}

	@XmlElement(name = "apresentarCampoEnderecoSetor")
	public Boolean getApresentarCampoEnderecoSetor() {
		if(apresentarCampoEnderecoSetor == null) {
			apresentarCampoEnderecoSetor = Boolean.FALSE;
		}
		return apresentarCampoEnderecoSetor;
	}

	public void setApresentarCampoEnderecoSetor(Boolean apresentarCampoEnderecoSetor) {
		this.apresentarCampoEnderecoSetor = apresentarCampoEnderecoSetor;
	}

	@XmlElement(name = "apresentarCampoEnderecoNumero")
	public Boolean getApresentarCampoEnderecoNumero() {
		if(apresentarCampoEnderecoNumero == null) {
			apresentarCampoEnderecoNumero = Boolean.FALSE;
		}
		return apresentarCampoEnderecoNumero;
	}

	public void setApresentarCampoEnderecoNumero(Boolean apresentarCampoEnderecoNumero) {
		this.apresentarCampoEnderecoNumero = apresentarCampoEnderecoNumero;
	}

	@XmlElement(name = "apresentarCampoEnderecoComplemento")
	public Boolean getApresentarCampoEnderecoComplemento() {
		if(apresentarCampoEnderecoComplemento == null) {
			apresentarCampoEnderecoComplemento = Boolean.FALSE;
		}
		return apresentarCampoEnderecoComplemento;
	}

	public void setApresentarCampoEnderecoComplemento(Boolean apresentarCampoEnderecoComplemento) {
		this.apresentarCampoEnderecoComplemento = apresentarCampoEnderecoComplemento;
	}

	@XmlElement(name = "apresentarCampoEnderecoCidade")
	public Boolean getApresentarCampoEnderecoCidade() {
		if(apresentarCampoEnderecoCidade == null) {
			apresentarCampoEnderecoCidade = Boolean.FALSE;
		}
		return apresentarCampoEnderecoCidade;
	}

	public void setApresentarCampoEnderecoCidade(Boolean apresentarCampoEnderecoCidade) {
		this.apresentarCampoEnderecoCidade = apresentarCampoEnderecoCidade;
	}

	
}
