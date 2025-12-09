package negocio.comuns.processosel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.utilitarias.Uteis;

public class ImportarCandidatoInscricaoProcessoSeletivoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ArquivoVO arquivoVO;

//	DADOS CANDIDATO
	private String corRaca;
	private String tituloEleitoral;
	private String orgaoEmissor;
	private String estadoEmissaoRG;
	private Date dataEmissaoRG;
	private String certificadoMilitar;
	private String RG;
	private String CPF;
	private String nacionalidade;
	private String nomeNaturalidade;
	private String estadoNaturalidade;
	private Date dataNasc;
	private String email;
	private String celular;
	private String telefoneRecado;
	private String telefoneRes;
	private String estadoCivil;
	private String sexo;
	private String nomeCidade;
	private String estadoCidade;
	private String complemento;
	private String cep;
	private String numero;
	private String setor;
	private String endereco;
	private String nome;
	private String nomeSocial;
	private String codigoCandidato;
	private String zonaEleitoral;
	private Date dataExpedicaoCertificadoMilitar;
	private String orgaoExpedidorCertificadoMilitar;
	private String situacaoMilitar;
	private String banco;
	private String agencia;
	private String contaCorrente;
	private String universidadeParceira;
	private String modalidadeBolsa;
	private Double valorBolsa;
	private String deficiencia;

//	DADOS INSCRIÇÃO
	private String numeroInscricao;
	private Date dataInscricao;
	private String nomeCurso;
	private Integer idPolo;
	private String nomeTurno;
	private String descricaoProcessoSeletivo;
	private Date dataProva;
	private String hora;
	private Date dataInicioInscricao;
	private Date dataTerminoInscricao;
	private String situacaoInscricao;
	private String formaIngresso;
	private String ano;
	private String semestre;
	private String sobreBolsasEAuxilios;
	private String autodeclaracaoPretoPardoOuIndigena;
	private String escolaPublica;
	private Integer classificacao;
	private Integer numeroChamada;

//	DADOS RESULTADO PROCSELETIVO
	private Date dataRegistro;
	private Double notaProcessoSeletivo;
	private String resultadoProcessoSeletivo;

//	DADOS FORMAÇÃO ACADÊMICA
	private String formacaoAcademicaCurso;
	private String formacaoAcademicaTipoIes;
	private String formacaoAcademicaIes;
	private String formacaoAcademicaEscolaridade;
	private String formacaoAcademicaAno;
	private String formacaoAcademicaSemestre;
	private String formacaoAcademicaCidade;
	private String formacaoAcademicaEstado;
	
	private PaizVO nacionalidadeOriginal;
	private CidadeVO naturalidadeOriginal;
	private CidadeVO cidadeOriginal;
	private ProcSeletivoVO procSeletivoVO;
	private CursoVO cursoVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private TurnoVO turnoVO;
	private String erro;
	private String observacao;
	private Boolean possuiErro;
	private Boolean possuiObservacao;
	private Boolean selecionado;
	private Boolean inscricaoExistente;
	private Integer idPolo2;
	private Integer idPolo3;
	private Integer idPolo4;
	private Integer idPolo5;
	private Map<Integer, UnidadeEnsinoVO> mapPolos;
	private List<String>  listaMotivoErrosImportacao;


	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getTituloEleitoral() {
		if (tituloEleitoral == null) {
			tituloEleitoral = "";
		}
		return tituloEleitoral;
	}

	public void setTituloEleitoral(String tituloEleitoral) {
		this.tituloEleitoral = tituloEleitoral;
	}

	public String getOrgaoEmissor() {
		if (orgaoEmissor == null) {
			orgaoEmissor = "";
		}
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getEstadoEmissaoRG() {
		if (estadoEmissaoRG == null) {
			estadoEmissaoRG = "";
		}
		return estadoEmissaoRG;
	}

	public void setEstadoEmissaoRG(String estadoEmissaoRG) {
		this.estadoEmissaoRG = estadoEmissaoRG;
	}

	public Date getDataEmissaoRG() {
		return dataEmissaoRG;
	}

	public void setDataEmissaoRG(Date dataEmissaoRG) {
		this.dataEmissaoRG = dataEmissaoRG;
	}

	public String getCertificadoMilitar() {
		if (certificadoMilitar == null) {
			certificadoMilitar = "";
		}
		return certificadoMilitar;
	}

	public void setCertificadoMilitar(String certificadoMilitar) {
		this.certificadoMilitar = certificadoMilitar;
	}

	public String getRG() {
		if (RG == null) {
			RG = "";
		}
		return RG;
	}

	public void setRG(String rG) {
		RG = rG;
	}

	public String getCPF() {
		if (CPF == null) {
			CPF = "";
		}
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getNacionalidade() {
		if (nacionalidade == null) {
			nacionalidade = "";
		}
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getNomeNaturalidade() {
		if (nomeNaturalidade == null) {
			nomeNaturalidade = "";
		}
		return nomeNaturalidade;
	}

	public void setNomeNaturalidade(String nomeNaturalidade) {
		this.nomeNaturalidade = nomeNaturalidade;
	}

	public String getEstadoNaturalidade() {
		if (estadoNaturalidade == null) {
			estadoNaturalidade = "";
		}
		return estadoNaturalidade;
	}

	public void setEstadoNaturalidade(String estadoNaturalidade) {
		this.estadoNaturalidade = estadoNaturalidade;
	}

	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
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

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTelefoneRecado() {
		if (telefoneRecado == null) {
			telefoneRecado = "";
		}
		return telefoneRecado;
	}

	public void setTelefoneRecado(String telefoneRecado) {
		this.telefoneRecado = telefoneRecado;
	}

	public String getTelefoneRes() {
		if (telefoneRes == null) {
			telefoneRes = "";
		}
		return telefoneRes;
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	public String getEstadoCivil() {
		if (estadoCivil == null) {
			estadoCivil = "";
		}
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getSexo() {
		if (sexo == null) {
			sexo = "";
		}
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getNomeCidade() {
		if (nomeCidade == null) {
			nomeCidade = "";
		}
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public String getEstadoCidade() {
		if (estadoCidade == null) {
			estadoCidade = "";
		}
		return estadoCidade;
	}

	public void setEstadoCidade(String estadoCidade) {
		this.estadoCidade = estadoCidade;
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

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
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
	
	public String getNomeSocial() {
		if (nomeSocial == null) {
			nomeSocial = "";
		}
		return nomeSocial;
	}

	public void setNomeSocial(String nomeSocial) {
		this.nomeSocial = nomeSocial;
	}
	
	public String getCodigoCandidato() {
		if (codigoCandidato == null) {
			codigoCandidato = "";
		}
		return codigoCandidato;
	}

	public void setCodigoCandidato(String codigoCandidato) {
		this.codigoCandidato = codigoCandidato;
	}

	public String getZonaEleitoral() {
		if (zonaEleitoral == null) {
			zonaEleitoral = "";
		}
		return zonaEleitoral;
	}

	public void setZonaEleitoral(String zonaEleitoral) {
		this.zonaEleitoral = zonaEleitoral;
	}

	public Date getDataExpedicaoCertificadoMilitar() {
		return dataExpedicaoCertificadoMilitar;
	}

	public void setDataExpedicaoCertificadoMilitar(Date dataExpedicaoCertificadoMilitar) {
		this.dataExpedicaoCertificadoMilitar = dataExpedicaoCertificadoMilitar;
	}

	public String getOrgaoExpedidorCertificadoMilitar() {
		if (orgaoExpedidorCertificadoMilitar == null) {
			orgaoExpedidorCertificadoMilitar = "";
		}
		return orgaoExpedidorCertificadoMilitar;
	}

	public void setOrgaoExpedidorCertificadoMilitar(String orgaoExpedidorCertificadoMilitar) {
		this.orgaoExpedidorCertificadoMilitar = orgaoExpedidorCertificadoMilitar;
	}

	public String getSituacaoMilitar() {
		if (situacaoMilitar == null) {
			situacaoMilitar = "";
		}
		return situacaoMilitar;
	}

	public void setSituacaoMilitar(String situacaoMilitar) {
		this.situacaoMilitar = situacaoMilitar;
	}

	public String getNumeroInscricao() {
		if (numeroInscricao == null) {
			numeroInscricao = "";
		}
		return numeroInscricao;
	}

	public void setNumeroInscricao(String numeroInscricao) {
		this.numeroInscricao = numeroInscricao;
	}

	public Date getDataInscricao() {
		return dataInscricao;
	}

	public void setDataInscricao(Date dataInscricao) {
		this.dataInscricao = dataInscricao;
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

	public String getDescricaoProcessoSeletivo() {
		if (descricaoProcessoSeletivo == null) {
			descricaoProcessoSeletivo = "";
		}
		return descricaoProcessoSeletivo;
	}

	public void setDescricaoProcessoSeletivo(String descricaoProcessoSeletivo) {
		this.descricaoProcessoSeletivo = descricaoProcessoSeletivo;
	}

	public Date getDataProva() {
		return dataProva;
	}

	public void setDataProva(Date dataProva) {
		this.dataProva = dataProva;
	}

	public String getHora() {
		if (hora == null) {
			hora = "";
		}
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Date getDataInicioInscricao() {
		return dataInicioInscricao;
	}

	public void setDataInicioInscricao(Date dataInicioInscricao) {
		this.dataInicioInscricao = dataInicioInscricao;
	}

	public Date getDataTerminoInscricao() {
		return dataTerminoInscricao;
	}

	public void setDataTerminoInscricao(Date dataTerminoInscricao) {
		this.dataTerminoInscricao = dataTerminoInscricao;
	}

	public String getSituacaoInscricao() {
		if (situacaoInscricao == null) {
			situacaoInscricao = "";
		}
		return situacaoInscricao;
	}

	public void setSituacaoInscricao(String situacaoInscricao) {
		this.situacaoInscricao = situacaoInscricao;
	}

	public String getFormaIngresso() {
		if (formaIngresso == null) {
			formaIngresso = "";
		}
		return formaIngresso;
	}

	public void setFormaIngresso(String formaIngresso) {
		this.formaIngresso = formaIngresso;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Date getDataRegistro() {
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}

	public Double getNotaProcessoSeletivo() {
		if (notaProcessoSeletivo == null) {
			notaProcessoSeletivo = 0.0;
		}
		return notaProcessoSeletivo;
	}

	public void setNotaProcessoSeletivo(Double notaProcessoSeletivo) {
		this.notaProcessoSeletivo = notaProcessoSeletivo;
	}

	public String getResultadoProcessoSeletivo() {
		if (resultadoProcessoSeletivo == null) {
			resultadoProcessoSeletivo = "";
		}
		return resultadoProcessoSeletivo;
	}

	public void setResultadoProcessoSeletivo(String resultadoProcessoSeletivo) {
		this.resultadoProcessoSeletivo = resultadoProcessoSeletivo;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Boolean getPossuiErro() {
		if (possuiErro == null) {
			possuiErro = false;
		}
		return possuiErro;
	}

	public void setPossuiErro(Boolean possuiErro) {
		this.possuiErro = possuiErro;
	}

	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = true;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getErro() {
		if (erro == null) {
			erro = "";
		}
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public Boolean getPossuiObservacao() {
		if (possuiObservacao == null) {
			possuiObservacao = false;
		}
		return possuiObservacao;
	}

	public void setPossuiObservacao(Boolean possuiObservacao) {
		this.possuiObservacao = possuiObservacao;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public PaizVO getNacionalidadeOriginal() {
		if (nacionalidadeOriginal == null) {
			nacionalidadeOriginal = new PaizVO();
		}
		return nacionalidadeOriginal;
	}

	public void setNacionalidadeOriginal(PaizVO nacionalidadeOriginal) {
		this.nacionalidadeOriginal = nacionalidadeOriginal;
	}

	public CidadeVO getNaturalidadeOriginal() {
		if (naturalidadeOriginal == null) {
			naturalidadeOriginal = new CidadeVO();
		}
		return naturalidadeOriginal;
	}

	public void setNaturalidadeOriginal(CidadeVO naturalidadeOriginal) {
		this.naturalidadeOriginal = naturalidadeOriginal;
	}

	public CidadeVO getCidadeOriginal() {
		if (cidadeOriginal == null) {
			cidadeOriginal = new CidadeVO();
		}
		return cidadeOriginal;
	}

	public void setCidadeOriginal(CidadeVO cidadeOriginal) {
		this.cidadeOriginal = cidadeOriginal;
	}

	public ProcSeletivoVO getProcSeletivoVO() {
		if (procSeletivoVO == null) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}

	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public Integer getIdPolo() {
		if (idPolo == null) {
			idPolo = 0;
		}
		return idPolo;
	}

	public void setIdPolo(Integer idPolo) {
		this.idPolo = idPolo;
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

	public String getSobreBolsasEAuxilios() {
		if (sobreBolsasEAuxilios == null) {
			sobreBolsasEAuxilios = "";
		}
		return sobreBolsasEAuxilios;
	}

	public void setSobreBolsasEAuxilios(String sobreBolsasEAuxilios) {
		this.sobreBolsasEAuxilios = sobreBolsasEAuxilios;
	}

	public String getAutodeclaracaoPretoPardoOuIndigena() {
		if (autodeclaracaoPretoPardoOuIndigena == null) {
			autodeclaracaoPretoPardoOuIndigena = "";
		}
		return autodeclaracaoPretoPardoOuIndigena;
	}

	public void setAutodeclaracaoPretoPardoOuIndigena(String autodeclaracaoPretoPardoOuIndigena) {
		this.autodeclaracaoPretoPardoOuIndigena = autodeclaracaoPretoPardoOuIndigena;
	}

	public String getDataCreated_Apresentar() {
		return Uteis.getDataComHora(getCreated());
	}

	public Integer getClassificacao() {
		if (classificacao == null) {
			classificacao = 0;
		}
		return classificacao;
	}

	public void setClassificacao(Integer classificacao) {
		this.classificacao = classificacao;
	}

	public Integer getNumeroChamada() {
		if (numeroChamada == null) {
			numeroChamada = 0;
		}
		return numeroChamada;
	}

	public void setNumeroChamada(Integer numeroChamada) {
		this.numeroChamada = numeroChamada;
	}

	public Boolean getInscricaoExistente() {
		if (inscricaoExistente == null) {
			inscricaoExistente = false;
		}
		return inscricaoExistente;
	}

	public void setInscricaoExistente(Boolean inscricaoExistente) {
		this.inscricaoExistente = inscricaoExistente;
	}

	public String getEscolaPublica() {
		if (escolaPublica == null) {
			escolaPublica = "";
		}
		return escolaPublica;
	}

	public void setEscolaPublica(String escolaPublica) {
		this.escolaPublica = escolaPublica;
	}

	public String getBanco() {
		if (banco == null) {
			banco = "";
		}
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		if (agencia == null) {
			agencia = "";
		}
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getContaCorrente() {
		if (agencia == null) {
			agencia = "";
		}
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public String getUniversidadeParceira() {
		if (universidadeParceira == null) {
			universidadeParceira = "";
		}
		return universidadeParceira;
	}

	public void setUniversidadeParceira(String universidadeParceira) {
		this.universidadeParceira = universidadeParceira;
	}

	public String getModalidadeBolsa() {
		if (modalidadeBolsa == null) {
			modalidadeBolsa = "";
		}
		return modalidadeBolsa;
	}

	public void setModalidadeBolsa(String modalidadeBolsa) {
		this.modalidadeBolsa = modalidadeBolsa;
	}

	public Double getValorBolsa() {
		if (valorBolsa == null) {
			valorBolsa = 0.0;
		}
		return valorBolsa;
	}

	public void setValorBolsa(Double valorBolsa) {
		this.valorBolsa = valorBolsa;
	}

	public Integer getIdPolo2() {
		if (idPolo2 == null) {
			idPolo2 = 0;
		}
		return idPolo2;
	}

	public void setIdPolo2(Integer idPolo2) {
		this.idPolo2 = idPolo2;
	}

	public Integer getIdPolo3() {
		if (idPolo3 == null) {
			idPolo3 = 0;
		}
		return idPolo3;
	}

	public void setIdPolo3(Integer idPolo3) {
		this.idPolo3 = idPolo3;
	}

	public Integer getIdPolo4() {
		if (idPolo4 == null) {
			idPolo4 = 0;
		}
		return idPolo4;
	}

	public void setIdPolo4(Integer idPolo4) {
		this.idPolo4 = idPolo4;
	}

	public Integer getIdPolo5() {
		if (idPolo5 == null) {
			idPolo5 = 0;
		}
		return idPolo5;
	}

	public void setIdPolo5(Integer idPolo5) {
		this.idPolo5 = idPolo5;
	}

	public String getDeficiencia() {
		if(deficiencia == null ) {
			deficiencia ="";
		}
		return deficiencia;
	}

	public void setDeficiencia(String deficiencia) {
		this.deficiencia = deficiencia;
	}

	public Map<Integer, UnidadeEnsinoVO> getMapPolos() {
		if(mapPolos == null ) {
			mapPolos = new HashMap<Integer, UnidadeEnsinoVO>();
		}
		return mapPolos;
	}

	public void setMapPolos(Map<Integer, UnidadeEnsinoVO> mapPolos) {
		this.mapPolos = mapPolos;
	}

	public List<String> getListaMotivoErrosImportacao() {
		if(listaMotivoErrosImportacao == null ) {
			listaMotivoErrosImportacao = new ArrayList<String>(0);
		}
		return listaMotivoErrosImportacao;
	}

	public void setListaMotivoErrosImportacao(List<String> listaMotivoErrosImportacao) {
		this.listaMotivoErrosImportacao = listaMotivoErrosImportacao;
	}

	public String getCorRaca() {
		if(corRaca == null) {
			corRaca =  "";
		}
		return corRaca;
	}

	public void setCorRaca(String corRaca) {
		this.corRaca = corRaca;
	}

	public String getFormacaoAcademicaCurso() {
		if(formacaoAcademicaCurso == null) {
			formacaoAcademicaCurso = "";
		}
		return formacaoAcademicaCurso;
	}

	public void setFormacaoAcademicaCurso(String formacaoAcademicaCurso) {
		this.formacaoAcademicaCurso = formacaoAcademicaCurso;
	}

	public String getFormacaoAcademicaTipoIes() {
		if(formacaoAcademicaTipoIes == null) {
			formacaoAcademicaTipoIes = "";
		}
		return formacaoAcademicaTipoIes;
	}

	public void setFormacaoAcademicaTipoIes(String formacaoAcademicaTipoIes) {
		this.formacaoAcademicaTipoIes = formacaoAcademicaTipoIes;
	}

	public String getFormacaoAcademicaIes() {
		if(formacaoAcademicaTipoIes == null) {
			formacaoAcademicaTipoIes = "";
		}
		return formacaoAcademicaIes;
	}

	public void setFormacaoAcademicaIes(String formacaoAcademicaIes) {
		this.formacaoAcademicaIes = formacaoAcademicaIes;
	}

	public String getFormacaoAcademicaEscolaridade() {
		if(formacaoAcademicaEscolaridade == null) {
			formacaoAcademicaEscolaridade = "";
		}
		return formacaoAcademicaEscolaridade;
	}

	public void setFormacaoAcademicaEscolaridade(String formacaoAcademicaEscolaridade) {
		this.formacaoAcademicaEscolaridade = formacaoAcademicaEscolaridade;
	}

	public String getFormacaoAcademicaAno() {
		if(formacaoAcademicaAno == null) {
			formacaoAcademicaAno = "";
		}
		return formacaoAcademicaAno;
	}

	public void setFormacaoAcademicaAno(String formacaoAcademicaAno) {
		this.formacaoAcademicaAno = formacaoAcademicaAno;
	}

	public String getFormacaoAcademicaSemestre() {
		if(formacaoAcademicaSemestre == null) {
			formacaoAcademicaSemestre = "";
		}
		return formacaoAcademicaSemestre;
	}

	public void setFormacaoAcademicaSemestre(String formacaoAcademicaSemestre) {
		this.formacaoAcademicaSemestre = formacaoAcademicaSemestre;
	}

	public String getFormacaoAcademicaCidade() {
		if(formacaoAcademicaCidade == null) {
			formacaoAcademicaCidade = "";
		}
		return formacaoAcademicaCidade;
	}

	public void setFormacaoAcademicaCidade(String formacaoAcademicaCidade) {
		this.formacaoAcademicaCidade = formacaoAcademicaCidade;
	}

	public String getFormacaoAcademicaEstado() {
		if(formacaoAcademicaEstado == null) {
			formacaoAcademicaEstado= "";
		}
		return formacaoAcademicaEstado;
	}

	public void setFormacaoAcademicaEstado(String formacaoAcademicaEstado) {
		this.formacaoAcademicaEstado = formacaoAcademicaEstado;
	}

}
