package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.enumerador.TipoContratoAgenteNegativacaoCobrancaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

public class RegistroNegativacaoCobrancaContaReceberItemVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer contaReceber;
	private String unidadeEnsino;
	private String unidadeEnsinoCnpj;
	private String curso;
	private Integer codigoCurso;
	private String aluno;
	private String alunoCpf;
	private Date alunoDataNascimento;
	private String alunoCep;
	private String alunoEndereco;
	private String alunoNumero;
	private String alunoBairro;
	private String alunoMunicipio;
	private String alunoUf;	
	private String matricula;
	private Integer matriculaPeriodo;
	private String parcela;
	private String nossoNumero;
	private Date dataVencimento;
	private Double valor;
	private String codigoIntegracaoApi;
	
	// campos utilizados apenas para gravar remoção do registro de cobrança da conta receber
	private Integer codigo;	
	private Integer registroNegativacaoCobrancaContaReceber;
	private Integer codigoNegociacaoContaReceber;
	private Integer codigoUsuario;
	private String nomeUsuario;
	private Date dataRegistro;
	private Integer codigoUsuarioExclusao;
	private String nomeUsuarioExclusao;
	private Date dataExclusao;
	private String motivo;
	private String situacaoContaReceber;
	private String jsonIntegracao;
	private String jsonIntegracaoExclusao;
	
	// campo utilizado apenas para visualizar situação ao consultar webservice
	private String agente;
	private Integer codigoAgente;
	// campo utilizado apenas para visualizar situação ao consultar webservice
	private String situacao;
	private Boolean selecionado;
	private Boolean bloqueio;	
	private String parceiro;
	private TipoContratoAgenteNegativacaoCobrancaEnum tipoContratoAgenteNegativacaoCobrancaEnum;
	
	
	private Boolean permiteEstornar;
	
	public String getEstilo() {
		if (!getMotivo().trim().isEmpty()) {
			return "horarioOcupado";
		} else {
			return "";
			
		}
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
	
	public Integer getContaReceber() {
		if (contaReceber == null) {
			contaReceber = 0;
		}
		return (contaReceber);
	}

	public void setContaReceber(Integer contaReceber) {
		this.contaReceber = contaReceber;
	}

	public String getAluno() {
		if (aluno == null) {
			aluno = "";
		}
		return aluno;
	}
	public void setAluno(String aluno) {
		this.aluno = aluno;
	}
	
	
	
	public String getAlunoCpf() {
		if (alunoCpf == null) {
			alunoCpf = "";
		}
		return alunoCpf;
	}

	public void setAlunoCpf(String alunoCpf) {
		this.alunoCpf = alunoCpf;
	}
	
	

	public Date getAlunoDataNascimento() {
		return alunoDataNascimento;
	}

	public void setAlunoDataNascimento(Date alunoDataNascimento) {
		this.alunoDataNascimento = alunoDataNascimento;
	}
	
	

	public String getAlunoCep() {
		if (alunoCep == null) {
			alunoCep = "";
		}
		return alunoCep;
	}

	public void setAlunoCep(String alunoCep) {
		this.alunoCep = alunoCep;
	}

	public String getAlunoEndereco() {
		if (alunoEndereco == null) {
			alunoEndereco = "";
		}
		return alunoEndereco;
	}

	public void setAlunoEndereco(String alunoEndereco) {
		this.alunoEndereco = alunoEndereco;
	}

	public String getAlunoNumero() {
		if (alunoNumero == null) {
			alunoNumero = "";
		}
		return alunoNumero;
	}

	public void setAlunoNumero(String alunoNumero) {
		this.alunoNumero = alunoNumero;
	}

	public String getAlunoBairro() {
		if (alunoBairro == null) {
			alunoBairro = "";
		}
		return alunoBairro;
	}

	public void setAlunoBairro(String alunoBairro) {
		this.alunoBairro = alunoBairro;
	}

	public String getAlunoMunicipio() {
		if (alunoMunicipio == null) {
			alunoMunicipio = "";
		}
		return alunoMunicipio;
	}

	public void setAlunoMunicipio(String alunoMunicipio) {
		this.alunoMunicipio = alunoMunicipio;
	}

	public String getAlunoUf() {
		if (alunoUf == null) {
			alunoUf = "";
		}
		return alunoUf;
	}

	public void setAlunoUf(String alunoUf) {
		this.alunoUf = alunoUf;
	}	

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	
	public Integer getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = 0;
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(Integer matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "";
		}
		return parcela;
	}
	
	public void setParcela(String parcela) {
		this.parcela = parcela;
	}
	
	public String getDataVencimentoApresentar() {
		return Uteis.getData(getDataVencimento());
	}
	
	public Date getDataVencimento() {
		return dataVencimento;
	}
	
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	
	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}
	
	public void setValor(Double valor) {
		this.valor = valor;
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	

	public String getUnidadeEnsinoCnpj() {
		if (unidadeEnsinoCnpj == null) {
			unidadeEnsinoCnpj = "";
		}
		return unidadeEnsinoCnpj;
	}

	public void setUnidadeEnsinoCnpj(String unidadeEnsinoCnpj) {
		this.unidadeEnsinoCnpj = unidadeEnsinoCnpj;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return curso;
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public Integer getCodigoUsuario() {
		if (codigoUsuario == null) {
			codigoUsuario = 0;
		}
		return codigoUsuario;
	}

	public void setCodigoUsuario(Integer codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}

	public String getNomeUsuario() {
		if (nomeUsuario == null) {
			nomeUsuario = "";
		}
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
	public String getDataExclusaoApresentar() {
		if (getDataExclusao() == null) {
			return "";
		}
		return Uteis.getDataComHora(getDataExclusao());
	}

	public Date getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(Date dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public String getMotivo() {
		if (motivo == null) {
			motivo = "";
		}
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Integer getRegistroNegativacaoCobrancaContaReceber() {
		if (registroNegativacaoCobrancaContaReceber == null) {
			registroNegativacaoCobrancaContaReceber = 0;
		}
		return registroNegativacaoCobrancaContaReceber;
	}

	public void setRegistroNegativacaoCobrancaContaReceber(Integer registroNegativacaoCobrancaContaReceber) {
		this.registroNegativacaoCobrancaContaReceber = registroNegativacaoCobrancaContaReceber;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getCodigoCurso() {
		if (codigoCurso == null) {
			codigoCurso = 0;
		}
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}


	public String getDataRegistroApresentar() {
		return Uteis.getData(getDataRegistro());
	}
	
	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Integer getCodigoUsuarioExclusao() {
		if (codigoUsuarioExclusao == null) {
			codigoUsuarioExclusao = 0;
		}
		return codigoUsuarioExclusao;
	}

	public void setCodigoUsuarioExclusao(Integer codigoUsuarioExclusao) {
		this.codigoUsuarioExclusao = codigoUsuarioExclusao;
	}

	public String getNomeUsuarioExclusao() {
		if (nomeUsuarioExclusao == null) {
			nomeUsuarioExclusao = "";
		}
		return nomeUsuarioExclusao;
	}

	public void setNomeUsuarioExclusao(String nomeUsuarioExclusao) {
		this.nomeUsuarioExclusao = nomeUsuarioExclusao;
	}

	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = Boolean.FALSE;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getSituacaoContaReceber() {
		if (situacaoContaReceber == null) {
			situacaoContaReceber = "";
		}
		return situacaoContaReceber;
	}
	
	public String getSituacaoContaReceber_Apresentar() {
		return SituacaoContaReceber.getDescricao(situacaoContaReceber);
	}	

	public void setSituacaoContaReceber(String situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public Integer getCodigoNegociacaoContaReceber() {
		if (codigoNegociacaoContaReceber == null) {
			codigoNegociacaoContaReceber = 0;
		}
		return codigoNegociacaoContaReceber;
	}

	public void setCodigoNegociacaoContaReceber(Integer codigoNegociacaoContaReceber) {
		this.codigoNegociacaoContaReceber = codigoNegociacaoContaReceber;
	}

	public String getAgente() {
		if (agente == null) {
			agente = "";
		}
		return agente;
	}

	public void setAgente(String agente) {
		this.agente = agente;
	}

	public Boolean getBloqueio() {
		if (bloqueio == null) {
			bloqueio = Boolean.FALSE;
		}
		return bloqueio;
	}

	public void setBloqueio(Boolean bloqueio) {
		this.bloqueio = bloqueio;
	}
	
	public String getSituacaoRegistro() {
		if(!Uteis.isAtributoPreenchido(getDataExclusao())  && !Uteis.isAtributoPreenchido(getMotivo())) {
			return "Registrado";		
		}else if (Uteis.isAtributoPreenchido(getDataExclusao()) && Uteis.isAtributoPreenchido(getCodigoIntegracaoApi())  && Uteis.isAtributoPreenchido(getMotivo()) && !Uteis.isAtributoPreenchido(getCodigoUsuarioExclusao())) {
			return "Rejeitado Registro";
		}else if (!Uteis.isAtributoPreenchido(getDataExclusao()) && Uteis.isAtributoPreenchido(getCodigoIntegracaoApi())  && Uteis.isAtributoPreenchido(getMotivo()) && !Uteis.isAtributoPreenchido(getCodigoUsuarioExclusao())) {
			return "Rejeitado Exclusão";
		}
		return "Retirado";		
	}

	public Boolean getPermiteEstornar() {
		if(permiteEstornar == null){
			permiteEstornar = false;
		}
		return permiteEstornar;
	}

	public void setPermiteEstornar(Boolean permiteEstornar) {
		this.permiteEstornar = permiteEstornar;
	}

	public String getParceiro() {
		if (parceiro == null) {
			parceiro = "";
		}
		return parceiro;
	}

	public void setParceiro(String parceiro) {
		this.parceiro = parceiro;
	}

	public String getCodigoIntegracaoApi() {
		if (codigoIntegracaoApi == null) {
			codigoIntegracaoApi = "";
		}
		return codigoIntegracaoApi;
	}

	public void setCodigoIntegracaoApi(String codigoIntegracaoApi) {
		this.codigoIntegracaoApi = codigoIntegracaoApi;
	}

	public Integer getCodigoAgente() {
		if(codigoAgente == null) {
			codigoAgente = 0;
		}
		return codigoAgente;
	}

	public void setCodigoAgente(Integer codigoAgente) {
		this.codigoAgente = codigoAgente;
	}

	public String getJsonIntegracao() {
		if(jsonIntegracao == null) {
			jsonIntegracao = "";
		}
		return jsonIntegracao;
	}

	public void setJsonIntegracao(String jsonIntegracao) {
		this.jsonIntegracao = jsonIntegracao;
	}

	public String getJsonIntegracaoExclusao() {
		if(jsonIntegracaoExclusao == null) {
			jsonIntegracaoExclusao = "";
		}
		return jsonIntegracaoExclusao;
	}

	public void setJsonIntegracaoExclusao(String jsonIntegracaoExclusao) {
		this.jsonIntegracaoExclusao = jsonIntegracaoExclusao;
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
	
	public String getContrato() {
		switch (getTipoContratoAgenteNegativacaoCobrancaEnum()) {
		case NOSSO_NUMERO:			
			return getNossoNumero();
		case MATRICULA:			
			return getMatricula();
		case MATRICULA_PERIODO:
			return getMatriculaPeriodo().toString();
		default:
			return "";
		}
	}
	
	
	
	

	
	
}
