package relatorio.negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.AdvertenciaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.NotaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class BoletimAcademicoRelVO implements Serializable {

	private static final long serialVersionUID = 2929127204444910697L;

	private String nomeAluno;
	private String matricula;
	private String nomeCurso;
	private String nomeTurma;
	private String cpf;
	private String rg;
	private String orgaoEmissor;
	private String ufOrgaoEmissor;
	private String sexo;
	private String ano;
	private String semestre;
	private String periodoLetivo;
	private String mensagemBoletimAcademico;
	private Integer configuracaoAcademico;

	private List<HistoricoVO> historico;
	private List<HistoricoVO> historicoAux;
	private List<HistoricoVO> historico1Layout2;
	private List<HistoricoVO> historico2Layout2;
	private List<NotaVO> listaNotas;
	private String dataNascimento;
	private String naturalidade;
	private String uf;
	private String nomePai;
	private String nomeMae;
	private String turno;
	private List<AdvertenciaVO> advertenciaVOs;
	private List<BoletimAcademicoEnsinoMedioRelVO> boletimAcademicoEnsinoMedioRelVOs;
	private String reconhecimento;
	private String autorizacao;
	private String documentacaoPendente;
	private String situacaoFinal;
	private String periodoAvaliacao;
	private String sistemaAvaliacao;
	private String observacaoHistorico;
	private FuncionarioVO funcionarioPrincipalVO;
	private FuncionarioVO funcionarioSecundarioVO;
	private CargoVO cargoFuncionarioPrincipal;
	private CargoVO cargoFuncionarioSecundario;
	private String dataPorExtenso;
	private Boolean apresentarCampoAssinatura;
	private Boolean apresentarQuantidadeFaltasAluno;
	private Boolean gerarUmAlunoPorPagina;
	private Boolean apresentarApenaNotaTipoMedia;
	private Integer totalFaltaPrimeiroBimestre;
	private Integer totalFaltaSegundoBimestre;
	private Integer totalFaltaTerceiroBimestre;
	private Integer totalFaltaQuartoBimestre;
	private Integer totalFalta;
	private Boolean apresentarCampoAssinaturaResponsavel;
	private Integer totalCargaHoraria;
    private String legendaSituacaoHistorico; 
    private Boolean apresentarFaltaPrimeiroBimestre;
	private Boolean apresentarFaltaSegundoBimestre;
	private Boolean apresentarFaltaTerceiroBimestre;
	private Boolean apresentarFaltaQuartoBimestre;
    

	public JRDataSource getNotaVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaNotas().toArray());
		return jr;
	}

	public BoletimAcademicoRelVO() {
	}

	public JRDataSource getHistoricoVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getHistorico().toArray());
		return jr;
	}

	public JRDataSource getAdvertenciaVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getAdvertenciaVOs().toArray());
		return jr;
	}

	public JRDataSource getBoletimAcademicoEnsinoMedioRelVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getBoletimAcademicoEnsinoMedioRelVOs().toArray());
		return jr;
	}

	public JRDataSource getHistoricoAuxVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getHistoricoAux().toArray());
		return jr;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public List<HistoricoVO> getHistorico() {
		if (historico == null) {
			historico = new ArrayList<HistoricoVO>(0);
		}
		return historico;
	}

	public void setHistorico(List<HistoricoVO> historico) {
		this.historico = historico;
	}

	public List<HistoricoVO> getHistoricoAux() {
		if (historicoAux == null) {
			historicoAux = new ArrayList<HistoricoVO>(0);
		}
		return historicoAux;
	}

	public void setHistoricoAux(List<HistoricoVO> historicoAux) {
		this.historicoAux = historicoAux;
	}

	public void setMensagemBoletimAcademico(String mensagemBoletimAcademico) {
		this.mensagemBoletimAcademico = mensagemBoletimAcademico;
	}

	public String getMensagemBoletimAcademico() {
		if (mensagemBoletimAcademico == null) {
			mensagemBoletimAcademico = "";
		}
		return mensagemBoletimAcademico;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
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

	public String getNomeTurma() {
		if (nomeTurma == null) {
			nomeTurma = "";
		}
		return nomeTurma;
	}

	public void setNomeTurma(String nomeTurma) {
		this.nomeTurma = nomeTurma;
	}

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		if (rg == null) {
			rg = "";
		}
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public List<NotaVO> getListaNotas() {
		if (listaNotas == null) {
			listaNotas = new ArrayList<NotaVO>(0);
		}
		return listaNotas;
	}

	public void setListaNotas(List<NotaVO> listaNotas) {
		this.listaNotas = listaNotas;
	}

	public String getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = "";
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(String periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public List<HistoricoVO> getHistorico1Layout2() {
		if (historico1Layout2 == null) {
			historico1Layout2 = new ArrayList<HistoricoVO>(0);
		}
		return historico1Layout2;
	}

	public void setHistorico1Layout2(List<HistoricoVO> historico1Layout2) {
		this.historico1Layout2 = historico1Layout2;
	}

	public List<HistoricoVO> getHistorico2Layout2() {
		if (historico2Layout2 == null) {
			historico2Layout2 = new ArrayList<HistoricoVO>(0);
		}
		return historico2Layout2;
	}

	public void setHistorico2Layout2(List<HistoricoVO> historico2Layout2) {
		this.historico2Layout2 = historico2Layout2;
	}

	public JRDataSource getHistorico1Layout2JR() {
		JRDataSource jr = new JRBeanArrayDataSource(getHistorico1Layout2().toArray());
		return jr;
	}

	public JRDataSource getHistorico2Layout2JR() {
		JRDataSource jr = new JRBeanArrayDataSource(getHistorico2Layout2().toArray());
		return jr;
	}

	public Integer getConfiguracaoAcademico() {
		if (configuracaoAcademico == null) {
			configuracaoAcademico = 0;
		}
		return configuracaoAcademico;
	}

	public void setConfiguracaoAcademico(Integer configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
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

	public String getNaturalidade() {
		if (naturalidade == null) {
			naturalidade = "";
		}
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		this.naturalidade = naturalidade;
	}

	public String getUf() {
		if (uf == null) {
			uf = "";
		}
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getNomePai() {
		if (nomePai == null) {
			nomePai = "";
		}
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		if (nomeMae == null) {
			nomeMae = "";
		}
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}

	public String getTurno() {
		if (turno == null) {
			turno = "";
		}
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public List<AdvertenciaVO> getAdvertenciaVOs() {
		if (advertenciaVOs == null) {
			advertenciaVOs = new ArrayList<AdvertenciaVO>(0);
		}
		return advertenciaVOs;
	}

	public void setAdvertenciaVOs(List<AdvertenciaVO> advertenciaVOs) {
		this.advertenciaVOs = advertenciaVOs;
	}

	public List<BoletimAcademicoEnsinoMedioRelVO> getBoletimAcademicoEnsinoMedioRelVOs() {
		if (boletimAcademicoEnsinoMedioRelVOs == null) {
			boletimAcademicoEnsinoMedioRelVOs = new ArrayList<BoletimAcademicoEnsinoMedioRelVO>(0);
		}
		return boletimAcademicoEnsinoMedioRelVOs;
	}

	public void setBoletimAcademicoEnsinoMedioRelVOs(
			List<BoletimAcademicoEnsinoMedioRelVO> boletimAcademicoEnsinoMedioRelVOs) {
		this.boletimAcademicoEnsinoMedioRelVOs = boletimAcademicoEnsinoMedioRelVOs;
	}

	public String getReconhecimento() {
		if (reconhecimento == null) {
			reconhecimento = "";
		}
		return reconhecimento;
	}

	public void setReconhecimento(String reconhecimento) {
		this.reconhecimento = reconhecimento;
	}

	public String getAutorizacao() {
		if (autorizacao == null) {
			autorizacao = "";
		}
		return autorizacao;
	}

	public void setAutorizacao(String autorizacao) {
		this.autorizacao = autorizacao;
	}

	public String getDocumentacaoPendente() {
		if (documentacaoPendente == null) {
			documentacaoPendente = "";
		}
		return documentacaoPendente;
	}

	public void setDocumentacaoPendente(String documentacaoPendente) {
		this.documentacaoPendente = documentacaoPendente;
	}

	public String getSituacaoFinal() {
		if (situacaoFinal == null) {
			situacaoFinal = "";
		}
		return situacaoFinal;
	}

	public void setSituacaoFinal(String situacaoFinal) {
		this.situacaoFinal = situacaoFinal;
	}

	public String getPeriodoAvaliacao() {
		if (periodoAvaliacao == null) {
			periodoAvaliacao = "";
		}
		return periodoAvaliacao;
	}

	public void setPeriodoAvaliacao(String periodoAvaliacao) {
		this.periodoAvaliacao = periodoAvaliacao;
	}

	public FuncionarioVO getFuncionarioPrincipalVO() {
		if (funcionarioPrincipalVO == null) {
			funcionarioPrincipalVO = new FuncionarioVO();
		}
		return funcionarioPrincipalVO;
	}

	public void setFuncionarioPrincipalVO(FuncionarioVO funcionarioPrincipalVO) {
		this.funcionarioPrincipalVO = funcionarioPrincipalVO;
	}

	public FuncionarioVO getFuncionarioSecundarioVO() {
		if (funcionarioSecundarioVO == null) {
			funcionarioSecundarioVO = new FuncionarioVO();
		}
		return funcionarioSecundarioVO;
	}

	public void setFuncionarioSecundarioVO(FuncionarioVO funcionarioSecundarioVO) {
		this.funcionarioSecundarioVO = funcionarioSecundarioVO;
	}

	public CargoVO getCargoFuncionarioPrincipal() {
		if (cargoFuncionarioPrincipal == null) {
			cargoFuncionarioPrincipal = new CargoVO();
		}
		return cargoFuncionarioPrincipal;
	}

	public void setCargoFuncionarioPrincipal(CargoVO cargoFuncionarioPrincipal) {
		this.cargoFuncionarioPrincipal = cargoFuncionarioPrincipal;
	}

	public CargoVO getCargoFuncionarioSecundario() {
		if (cargoFuncionarioSecundario == null) {
			cargoFuncionarioSecundario = new CargoVO();
		}
		return cargoFuncionarioSecundario;
	}

	public void setCargoFuncionarioSecundario(CargoVO cargoFuncionarioSecundario) {
		this.cargoFuncionarioSecundario = cargoFuncionarioSecundario;
	}

	public String getDataPorExtenso() {
		if (dataPorExtenso == null) {
			dataPorExtenso = "";
		}
		return dataPorExtenso;
	}

	public void setDataPorExtenso(String dataPorExtenso) {
		this.dataPorExtenso = dataPorExtenso;
	}

	public Boolean getApresentarCampoAssinatura() {
		if (apresentarCampoAssinatura == null) {
			apresentarCampoAssinatura = false;
		}
		return apresentarCampoAssinatura;
	}

	public void setApresentarCampoAssinatura(Boolean apresentarCampoAssinatura) {
		this.apresentarCampoAssinatura = apresentarCampoAssinatura;
	}

	public Boolean getApresentarQuantidadeFaltasAluno() {
		if (apresentarQuantidadeFaltasAluno == null) {
			apresentarQuantidadeFaltasAluno = false;
		}
		return apresentarQuantidadeFaltasAluno;
	}

	public void setApresentarQuantidadeFaltasAluno(Boolean apresentarQuantidadeFaltasAluno) {
		this.apresentarQuantidadeFaltasAluno = apresentarQuantidadeFaltasAluno;
	}

	public Integer getTotalFaltaPrimeiroBimestre() {
		if (totalFaltaPrimeiroBimestre == null) {
			totalFaltaPrimeiroBimestre = 0;
		}
		return totalFaltaPrimeiroBimestre;
	}

	public void setTotalFaltaPrimeiroBimestre(Integer totalFaltaPrimeiroBimestre) {
		this.totalFaltaPrimeiroBimestre = totalFaltaPrimeiroBimestre;
	}

	public Integer getTotalFaltaSegundoBimestre() {
		if (totalFaltaSegundoBimestre == null) {
			totalFaltaSegundoBimestre = 0;
		}
		return totalFaltaSegundoBimestre;
	}

	public void setTotalFaltaSegundoBimestre(Integer totalFaltaSegundoBimestre) {
		this.totalFaltaSegundoBimestre = totalFaltaSegundoBimestre;
	}

	public Integer getTotalFaltaTerceiroBimestre() {
		if (totalFaltaTerceiroBimestre == null) {
			totalFaltaTerceiroBimestre = 0;
		}
		return totalFaltaTerceiroBimestre;
	}

	public void setTotalFaltaTerceiroBimestre(Integer totalFaltaTerceiroBimestre) {
		this.totalFaltaTerceiroBimestre = totalFaltaTerceiroBimestre;
	}

	public Integer getTotalFaltaQuartoBimestre() {
		if (totalFaltaQuartoBimestre == null) {
			totalFaltaQuartoBimestre = 0;
		}
		return totalFaltaQuartoBimestre;
	}

	public void setTotalFaltaQuartoBimestre(Integer totalFaltaQuartoBimestre) {
		this.totalFaltaQuartoBimestre = totalFaltaQuartoBimestre;
	}

	public Integer getTotalFalta() {
		if (totalFalta == null) {
			totalFalta = 0;
		}
		return totalFalta;
	}

	public void setTotalFalta(Integer totalFalta) {
		this.totalFalta = totalFalta;
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

	public String getUfOrgaoEmissor() {
		if (ufOrgaoEmissor == null) {
			ufOrgaoEmissor = "";
		}
		return ufOrgaoEmissor;
	}

	public void setUfOrgaoEmissor(String ufOrgaoEmissor) {
		this.ufOrgaoEmissor = ufOrgaoEmissor;
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

	public String getSistemaAvaliacao() {
		if (sistemaAvaliacao == null) {
			sistemaAvaliacao = "";
		}
		return sistemaAvaliacao;
	}

	public void setSistemaAvaliacao(String sistemaAvaliacao) {
		this.sistemaAvaliacao = sistemaAvaliacao;
	}

	public String getObservacaoHistorico() {
		if (observacaoHistorico == null) {
			observacaoHistorico = "";
		}
		return observacaoHistorico;
	}

	public void setObservacaoHistorico(String observacaoHistorico) {
		this.observacaoHistorico = observacaoHistorico;
	}

	public Boolean getApresentarApenaNotaTipoMedia() {
		if (apresentarApenaNotaTipoMedia == null) {
			apresentarApenaNotaTipoMedia = false;
		}
		return apresentarApenaNotaTipoMedia;
	}

	public void setApresentarApenaNotaTipoMedia(Boolean apresentarApenaNotaTipoMedia) {
		this.apresentarApenaNotaTipoMedia = apresentarApenaNotaTipoMedia;
	}

	@Override
	public String toString() {
		return "BoletimAcademicoRelVO [boletimAcademicoEnsinoMedioRelVOs=" + boletimAcademicoEnsinoMedioRelVOs + "]";
	}

	/**
	 * @return the apresentarCampoAssinaturaResponsavel
	 */
	public Boolean getApresentarCampoAssinaturaResponsavel() {
		if (apresentarCampoAssinaturaResponsavel == null) {
			apresentarCampoAssinaturaResponsavel = false;
		}
		return apresentarCampoAssinaturaResponsavel;
	}

	/**
	 * @param apresentarCampoAssinaturaResponsavel
	 *            the apresentarCampoAssinaturaResponsavel to set
	 */
	public void setApresentarCampoAssinaturaResponsavel(Boolean apresentarCampoAssinaturaResponsavel) {
		this.apresentarCampoAssinaturaResponsavel = apresentarCampoAssinaturaResponsavel;
	}

	public String getOrdenacaoSemAcentuacaoNome() {
		return Uteis.removerAcentuacao(getNomeAluno());
	}

	public Boolean getGerarUmAlunoPorPagina() {
		if (gerarUmAlunoPorPagina == null) {
			gerarUmAlunoPorPagina = false;
		}
		return gerarUmAlunoPorPagina;
	}

	public void setGerarUmAlunoPorPagina(Boolean gerarUmAlunoPorPagina) {
		this.gerarUmAlunoPorPagina = gerarUmAlunoPorPagina;
	}

	public Integer getTotalCargaHoraria() {
		if (totalCargaHoraria == null) {
			totalCargaHoraria = 0;
		}
		return totalCargaHoraria;
	}

	public void setTotalCargaHoraria(Integer totalCargaHoraria) {
		this.totalCargaHoraria = totalCargaHoraria;
	}
	
	public String getLegendaSituacaoHistorico() {
		if (legendaSituacaoHistorico == null) {
			legendaSituacaoHistorico = "";
		}
		return legendaSituacaoHistorico;
	}

	public void setLegendaSituacaoHistorico(String legendaSituacaoHistorico) {
		this.legendaSituacaoHistorico = legendaSituacaoHistorico;
	}
	
	
	

	public Boolean getApresentarFaltaPrimeiroBimestre() {
		if(apresentarFaltaPrimeiroBimestre == null ) {
			apresentarFaltaPrimeiroBimestre =  Boolean.FALSE; ;
		}
		return apresentarFaltaPrimeiroBimestre;
	}

	public void setApresentarFaltaPrimeiroBimestre(Boolean apresentarFaltaPrimeiroBimestre) {
		this.apresentarFaltaPrimeiroBimestre = apresentarFaltaPrimeiroBimestre;
	}

	public Boolean getApresentarFaltaSegundoBimestre() {
		if(apresentarFaltaSegundoBimestre == null ) {
			apresentarFaltaSegundoBimestre = Boolean.FALSE;
		}
		return apresentarFaltaSegundoBimestre;
	}

	public void setApresentarFaltaSegundoBimestre(Boolean apresentarFaltaSegundoBimestre) {
		this.apresentarFaltaSegundoBimestre = apresentarFaltaSegundoBimestre;
	}

	public Boolean getApresentarFaltaTerceiroBimestre() {
		if(apresentarFaltaTerceiroBimestre == null ) {
			apresentarFaltaTerceiroBimestre = Boolean.FALSE;
		}
		return apresentarFaltaTerceiroBimestre;
	}

	public void setApresentarFaltaTerceiroBimestre(Boolean apresentarFaltaTerceiroBimestre) {
		this.apresentarFaltaTerceiroBimestre = apresentarFaltaTerceiroBimestre;
	}

	public Boolean getApresentarFaltaQuartoBimestre() {
		if(apresentarFaltaQuartoBimestre == null ) {
			apresentarFaltaQuartoBimestre = Boolean.FALSE;
		}
		return apresentarFaltaQuartoBimestre;
	}

	public void setApresentarFaltaQuartoBimestre(Boolean apresentarFaltaQuartoBimestre) {
		this.apresentarFaltaQuartoBimestre = apresentarFaltaQuartoBimestre;
	}
}