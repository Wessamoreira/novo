package negocio.comuns.faturamento.nfe;

import negocio.comuns.arquitetura.SuperVO;

public class IntegracaoGinfesAlunoItemVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private IntegracaoGinfesAlunoVO integracao;
	// Aluno
	private String numeroIdentificacao;
	private String nome;
	private Integer codigoAluno;
	private String matricula;
	private String dataNascimento;
	private String dataInicial;
	private String dataFinal;
	private Integer possuiResponsavel;
	private Integer alunoAtivo;
	private String email;
	private String parcelaContaReceber;
	//transient 
	private String numeroIdentificacaoCPF ;
	private String numeroIdentificacaoCertidaoNascimento;
	private Integer codigoContaReceber;
	// Endereco Aluno
	private String cep;
	private String numero;
	private String logradouro;
	private String bairro;
	private Integer uf;
	private Integer municipio;
	private String complemento;
	// Responsavel
	private String numeroIdentificacaoResponsavel;
	private String nomeResponsavel;
	private String emailResponsavel;
	// Endereco Responsavel
	private String cepResponsavel;
	private String numeroResponsavel;
	private String logradouroResponsavel;
	private String bairroResponsavel;
	private Integer ufResponsavel;
	private Integer municipioResponsavel;
	// Curso
	private Integer codigoUnidadeEnsinoCurso;
	private String nomeCurso;
	private String nomeTurno;
	private String serie;
	private Double valorServico;
	private Double valorDescontoCondicionado;
	private Double valorDescontoIncondicionado;
	private Double valorDesconto;
	private String dataInicioDesconto;
	private String dataFimDesconto;
	private Double valorCustos;
	private String dataInicioCustos;
	private String dataFimCustos;
	private String dataInicioCurso;
	private Integer cursoAtivo;
	private Boolean possuiErro;
	private String erroValidacao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getNomeTurno() {
		if (nomeTurno == null) {
			nomeTurno = "";
		}
		return nomeTurno;
	}

	public void setNomeTurno(String nomeTurno) {
		this.nomeTurno = nomeTurno;
	}

	public IntegracaoGinfesAlunoVO getIntegracao() {
		if (integracao == null) {
			integracao = new IntegracaoGinfesAlunoVO();
		}
		return integracao;
	}

	public void setIntegracao(IntegracaoGinfesAlunoVO integracao) {
		this.integracao = integracao;
	}

	public String getNumeroIdentificacao() {
		if (numeroIdentificacao == null) {
			numeroIdentificacao = "";
		}
		return numeroIdentificacao;
	}
	
	public void setNumeroIdentificacao(String numeroIdentificacao) {
		this.numeroIdentificacao = numeroIdentificacao;
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
	
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}
	
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getDataNascimento() {
		if (dataNascimento == null) {
			dataNascimento = "";
		}
		return dataNascimento;
	}
	
	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	public Integer getPossuiResponsavel() {
		if (possuiResponsavel == null) {
			possuiResponsavel = 0;
		}
		return possuiResponsavel;
	}
	
	public void setPossuiResponsavel(Integer possuiResponsavel) {
		this.possuiResponsavel = possuiResponsavel;
	}
	
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getLogradouro() {
		if (logradouro == null) {
			logradouro = "";
		}
		return logradouro;
	}
	
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	
	public String getBairro() {
		if (bairro == null) {
			bairro = "";
		}
		return bairro;
	}
	
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public Integer getUf() {
		if (uf == null) {
			uf = 0;
		}
		return uf;
	}
	
	public void setUf(Integer uf) {
		this.uf = uf;
	}
	
	public Integer getMunicipio() {
		if (municipio == null) {
			municipio = 0;
		}
		return municipio;
	}
	
	public void setMunicipio(Integer municipio) {
		this.municipio = municipio;
	}

	public Integer getAlunoAtivo() {
		if (alunoAtivo == null) {
			alunoAtivo = 0;
		}
		return alunoAtivo;
	}

	public void setAlunoAtivo(Integer alunoAtivo) {
		this.alunoAtivo = alunoAtivo;
	}

	public String getNumeroIdentificacaoResponsavel() {
		if (numeroIdentificacaoResponsavel == null) {
			numeroIdentificacaoResponsavel = "";
		}
		return numeroIdentificacaoResponsavel;
	}

	public void setNumeroIdentificacaoResponsavel(String numeroIdentificacaoResponsavel) {
		this.numeroIdentificacaoResponsavel = numeroIdentificacaoResponsavel;
	}

	public String getNomeResponsavel() {
		if (nomeResponsavel == null) {
			nomeResponsavel = "";
		}
		return nomeResponsavel;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getEmailResponsavel() {
		if (emailResponsavel == null) {
			emailResponsavel = "";
		}
		return emailResponsavel;
	}

	public void setEmailResponsavel(String emailResponsavel) {
		this.emailResponsavel = emailResponsavel;
	}

	public String getCepResponsavel() {
		if (cepResponsavel == null) {
			cepResponsavel = "";
		}
		return cepResponsavel;
	}

	public void setCepResponsavel(String cepResponsavel) {
		this.cepResponsavel = cepResponsavel;
	}

	public String getNumeroResponsavel() {
		if (numeroResponsavel == null) {
			numeroResponsavel = "";
		}
		return numeroResponsavel;
	}

	public void setNumeroResponsavel(String numeroResponsavel) {
		this.numeroResponsavel = numeroResponsavel;
	}

	public String getLogradouroResponsavel() {
		if (logradouroResponsavel == null) {
			logradouroResponsavel = "";
		}
		return logradouroResponsavel;
	}

	public void setLogradouroResponsavel(String logradouroResponsavel) {
		this.logradouroResponsavel = logradouroResponsavel;
	}

	public String getBairroResponsavel() {
		if (bairroResponsavel == null) {
			bairroResponsavel = "";
		}
		return bairroResponsavel;
	}

	public void setBairroResponsavel(String bairroResponsavel) {
		this.bairroResponsavel = bairroResponsavel;
	}

	public Integer getUfResponsavel() {
		if (ufResponsavel == null) {
			ufResponsavel = 0;
		}
		return ufResponsavel;
	}

	public void setUfResponsavel(Integer ufResponsavel) {
		this.ufResponsavel = ufResponsavel;
	}

	public Integer getMunicipioResponsavel() {
		if (municipioResponsavel == null) {
			municipioResponsavel = 0;
		}
		return municipioResponsavel;
	}

	public void setMunicipioResponsavel(Integer municipioResponsavel) {
		this.municipioResponsavel = municipioResponsavel;
	}
	
	public Integer getCodigoUnidadeEnsinoCurso() {
		if (codigoUnidadeEnsinoCurso == null) {
			codigoUnidadeEnsinoCurso = 0;
		}
		return codigoUnidadeEnsinoCurso;
	}

	public void setCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) {
		this.codigoUnidadeEnsinoCurso = codigoUnidadeEnsinoCurso;
	}

	public String getSerie() {
		if (serie == null) {
			serie = "";
		}
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Double getValorServico() {
		if (valorServico == null) {
			valorServico = 0.0;
		}
		return valorServico;
	}

	public void setValorServico(Double valorServico) {
		this.valorServico = valorServico;
	}

	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public String getDataInicioDesconto() {
		if (dataInicioDesconto == null) {
			dataInicioDesconto = "";
		}
		return dataInicioDesconto;
	}

	public void setDataInicioDesconto(String dataInicioDesconto) {
		this.dataInicioDesconto = dataInicioDesconto;
	}

	public String getDataFimDesconto() {
		if (dataFimDesconto == null) {
			dataFimDesconto = "";
		}
		return dataFimDesconto;
	}

	public void setDataFimDesconto(String dataFimDesconto) {
		this.dataFimDesconto = dataFimDesconto;
	}

	public Double getValorCustos() {
		if (valorCustos == null) {
			valorCustos = 0.0;
		}
		return valorCustos;
	}

	public void setValorCustos(Double valorCustos) {
		this.valorCustos = valorCustos;
	}

	public String getDataInicioCustos() {
		if (dataInicioCustos == null) {
			dataInicioCustos = "";
		}
		return dataInicioCustos;
	}

	public void setDataInicioCustos(String dataInicioCustos) {
		this.dataInicioCustos = dataInicioCustos;
	}

	public String getDataFimCustos() {
		if (dataFimCustos == null) {
			dataFimCustos = "";
		}
		return dataFimCustos;
	}

	public void setDataFimCustos(String dataFimCustos) {
		this.dataFimCustos = dataFimCustos;
	}

	public String getDataInicioCurso() {
		if (dataInicioCurso == null) {
			dataInicioCurso = "";
		}
		return dataInicioCurso;
	}

	public void setDataInicioCurso(String dataInicioCurso) {
		this.dataInicioCurso = dataInicioCurso;
	}

	public Integer getCursoAtivo() {
		if (cursoAtivo == null) {
			cursoAtivo = 0;
		}
		return cursoAtivo;
	}

	public void setCursoAtivo(Integer cursoAtivo) {
		this.cursoAtivo = cursoAtivo;
	}

	public Double getValorDescontoCondicionado() {
		if (valorDescontoCondicionado == null) {
			valorDescontoCondicionado = 0.0;
		}
		return valorDescontoCondicionado;
	}

	public void setValorDescontoCondicionado(Double valorDescontoCondicionado) {
		this.valorDescontoCondicionado = valorDescontoCondicionado;
	}

	public Double getValorDescontoIncondicionado() {
		if (valorDescontoIncondicionado == null) {
			valorDescontoIncondicionado = 0.0;
		}
		return valorDescontoIncondicionado;
	}

	public void setValorDescontoIncondicionado(Double valorDescontoIncondicionado) {
		this.valorDescontoIncondicionado = valorDescontoIncondicionado;
	}

	public String getDataInicial() {
		if (dataInicial == null) {
			dataInicial = "";
		}
		return dataInicial;
	}

	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}

	public String getDataFinal() {
		if (dataFinal == null) {
			dataFinal = "";
		}
		return dataFinal;
	}

	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Boolean getPossuiErro() {
		if(possuiErro == null) {
			possuiErro = false;
		}
		return possuiErro;
	}

	public void setPossuiErro(Boolean possuiErro) {
		this.possuiErro = possuiErro;
	}

	public String getErroValidacao() {
		if(erroValidacao == null) {
			erroValidacao = "";
		}
		return erroValidacao;
	}

	public void setErroValidacao(String erroValidacao) {
		this.erroValidacao = erroValidacao;
	}

	public Integer getCodigoAluno() {
		if(codigoAluno == null) {
			codigoAluno = 0;
		}
		return codigoAluno;
	}

	public void setCodigoAluno(Integer codigoAluno) {
		this.codigoAluno = codigoAluno;
	}

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumeroIdentificacaoCPF() {
		if(numeroIdentificacaoCPF == null) {
			numeroIdentificacaoCPF = "" ;
		}
		return numeroIdentificacaoCPF;
	}

	public void setNumeroIdentificacaoCPF(String numeroIdentificacaoCPF) {
		this.numeroIdentificacaoCPF = numeroIdentificacaoCPF;
	}

	public String getNumeroIdentificacaoCertidaoNascimento() {
		if(numeroIdentificacaoCertidaoNascimento == null ) {
			numeroIdentificacaoCertidaoNascimento = "" ;
		}
		return numeroIdentificacaoCertidaoNascimento;
	}

	public void setNumeroIdentificacaoCertidaoNascimento(String numeroIdentificacaoCertidaoNascimento) {
		this.numeroIdentificacaoCertidaoNascimento = numeroIdentificacaoCertidaoNascimento;
	}

	public Integer getCodigoContaReceber() {
		if(codigoContaReceber == null) {
			codigoContaReceber = 0;
		}
		return codigoContaReceber;
	}

	public void setCodigoContaReceber(Integer codigoContaReceber) {
		this.codigoContaReceber = codigoContaReceber;
	}
	
	public String getParcelaContaReceber() {
		if (parcelaContaReceber == null) {
			parcelaContaReceber = "";
		}
		return parcelaContaReceber;
	}

	public void setParcelaContaReceber(String parcelaContaReceber) {
		this.parcelaContaReceber = parcelaContaReceber;
	}
	
	
	
}
