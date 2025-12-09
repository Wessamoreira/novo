/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import negocio.comuns.academico.ObservacaoComplementarDiplomaVO;

/**
 * 
 * @author Otimize-TI
 */
public class DiplomaAlunoRelVO implements Cloneable {

	private String alunoNome;
	private String dataNascimentoAluno;
	private String cidadeNome;
	private String estadoSigla;
	private String matricula;
	private String cursoNome;
	private String portariaCargoPrimeiroFuncionario;
	private String portariaCargoSegundoFuncionario;
	private String portariaCargoTerceiroFuncionario;
	private String primeiroReconhecimento;
	private String leiCriacao1;
	private String leiCriacao2;	
	private String nomePrimeiroFuncionario;
	private String cargoPrimeiroFuncionario;
	private String nomeSegundoFuncionario;
	private String cargoSegundoFuncionario;
	private String nomeTerceiroFuncionario;
	private String cargoTerceiroFuncionario;
	private String dataPublicacaoDO;
	private String dataPublicacaoDOUnidade;
	private String publicacaoDO;
	private String autorizacaoNrRegistroInterno;
	private String autorizacaoNrRegistroInternoUnidade;
	private String habilitacao;
	private String nacionalidadeAluno;
	private String reconhecimento;
	private String naturalidadeAluno;
	private String rgAluno;
	private String orgaoEmissorAluno;
	private String estadoEmissorRgAluno;
	private String tituloCurso;
	private String dataColacaoGrau;
	private String nivelEducacional;
	private String dataExpedicaoDiploma;	
	private String cidadeDataExpedicaoDiploma;
	private String grauCurso;
	private String cargaHorariaTotal;
	private String cpfAluno;
	private String dataNascimentoSimples;
	private String credenciamentoPortaria;
	private String dataPublicacaoDoEmpresa;
	private List<DiplomaAlunoHistoricoRelVO> listaHistorico;
	private String mantenedora;
	private String nomeUnidadeEnsino;
	private String razaoSocial;
	private String cidadeDataAtual;
	private String numeroRegistroDiploma;
	private String numeroProcesso;
	private String via;
	private String dataConclusaoCurso;
	private String viaAnterior;
	private String dataExpedicaoViaAnterior;
	private String sexoAluno;
	private String nomeEstadoNascimento;
	private String titulacaoDoFormando;
	private String areaConhecimento;
	private String reconhecimentoCursoAutorizacaoResolucao;
	private String autorizacaoCurso;
	private String nomePai;
	private String nomeMae;
	private List<DiplomaAlunoHistoricoRelVO> listaHistoricoEstabelecimento;
	private String cursoAnterior;
	private String estabelecimentoAnterior;
	private String enderecoEstabelecimentoAnterior;
	private String cnpjUnidadeEnsino;

	private String dataAtualExtenso;

	private String tituloCursoCompleto;
	private String preposicaoNomeCurso;
	private String sexoPrimeiroFuncionario;
	private String sexoSegundoFuncionario;
	private String bairro;
	private String cep;
	private String endereco;
	private String numero;
	private String foneUnidadeEnsino;
	private String anoConclusao;
	private String estadoSiglaUnidadeEnsino;
    private String numeroRegistroDiplomaViaAnterior;
    private String numeroProcessoViaAnterior;
    private String dataRegistroDiplomaViaAnterior;
    private String reitorRegistroDiplomaViaAnterior;
    private String secretariaRegistroDiplomaViaAnterior;
    private String cargoReitorRegistroDiplomaViaAnterior;        
    
    private String nomeQuartoFuncionario;
	private String nomeQuintoFuncionario;
	private String cargoQuartoFuncionario;
	private String cargoQuintoFuncionario;
	private String portariaCargoQuartoFuncionario;
	private String portariaCargoQuintoFuncionario;
        
    private List<ObservacaoComplementarDiplomaVO> observacaoComplementarDiplomaVOs;
    private String percentualCHIntegralizacaoMatricula;

	public DiplomaAlunoRelVO() {
	}

	public DiplomaAlunoRelVO getClone() throws Exception {
		return (DiplomaAlunoRelVO) super.clone();
	}

	public JRDataSource getListaDiplomaAlunoHistoricoRelVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaHistorico().toArray());
		return jr;
	}

	public JRDataSource getListaHistoricoEstabelecimentoVO() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaHistoricoEstabelecimento().toArray());
		return jr;
	}

	public static void validarDados(DiplomaAlunoRelVO obj) throws ConsistirException {
		if (obj.getMatricula().equals("")) {
			throw new ConsistirException("O campo MATRÍCULA (Aluno) deve ser informado.");
		}
		if (obj.getNomePrimeiroFuncionario().equals("")) {
			throw new ConsistirException("O campo ASSINATURA FUNCIONÁRIO 1 (Funcionário) deve ser informado.");
		}
		if (obj.getNomeSegundoFuncionario().equals("")) {
			throw new ConsistirException("O campo ASSINATURA FUNCIONÁRIO 2 (Funcionário) deve ser informado.");
		}
		if (obj.getCargoPrimeiroFuncionario().equals("")) {
			throw new ConsistirException("O campo CARGO (ASSINATURA FUNCIONÁRIO 1) deve ser informado.");
		}
		if (obj.getCargoSegundoFuncionario().equals("")) {
			throw new ConsistirException("O campo CARGO (ASSINATURA FUNCIONÁRIO 2) deve ser informado.");
		}
	}

	/**
	 * @return the alunoNome
	 */
	public String getAlunoNome() {
		if (alunoNome == null) {
			alunoNome = "";
		}
		return alunoNome;
	}

	/**
	 * @param alunoNome
	 *            the alunoNome to set
	 */
	public void setAlunoNome(String alunoNome) {
		this.alunoNome = alunoNome;
	}

	/**
	 * @return the dataNascimentoAluno
	 */
	public String getDataNascimentoAluno() {
		if (dataNascimentoAluno == null) {
			dataNascimentoAluno = "";
		}
		return dataNascimentoAluno;
	}

	/**
	 * @param dataNascimentoAluno
	 *            the dataNascimentoAluno to set
	 */
	public void setDataNascimentoAluno(String dataNascimentoAluno) {
		this.dataNascimentoAluno = dataNascimentoAluno;
	}

	/**
	 * @return the cidadeNome
	 */
	public String getCidadeNome() {
		if (cidadeNome == null) {
			cidadeNome = "";
		}
		return cidadeNome;
	}

	/**
	 * @param cidadeNome
	 *            the cidadeNome to set
	 */
	public void setCidadeNome(String cidadeNome) {
		this.cidadeNome = cidadeNome;
	}

	/**
	 * @return the estadoSigla
	 */
	public String getEstadoSigla() {
		if (estadoSigla == null) {
			estadoSigla = "";
		}
		return estadoSigla;
	}

	/**
	 * @param estadoSigla
	 *            the estadoSigla to set
	 */
	public void setEstadoSigla(String estadoSigla) {
		this.estadoSigla = estadoSigla;
	}

	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * @return the cursoNome
	 */
	public String getCursoNome() {
		if (cursoNome == null) {
			cursoNome = "";
		}
		return cursoNome;
	}

	/**
	 * @param cursoNome
	 *            the cursoNome to set
	 */
	public void setCursoNome(String cursoNome) {
		this.cursoNome = cursoNome;
	}

	/**
	 * @return the nomePrimeiroFuncionario
	 */
	public String getNomePrimeiroFuncionario() {
		if (nomePrimeiroFuncionario == null) {
			nomePrimeiroFuncionario = "";
		}
		return nomePrimeiroFuncionario;
	}

	/**
	 * @param nomePrimeiroFuncionario
	 *            the nomePrimeiroFuncionario to set
	 */
	public void setNomePrimeiroFuncionario(String nomePrimeiroFuncionario) {
		this.nomePrimeiroFuncionario = nomePrimeiroFuncionario;
	}

	/**
	 * @return the cargoPrimeiroFuncionario
	 */
	public String getCargoPrimeiroFuncionario() {
		if (cargoPrimeiroFuncionario == null) {
			cargoPrimeiroFuncionario = "";
		}
		return cargoPrimeiroFuncionario;
	}

	/**
	 * @param cargoPrimeiroFuncionario
	 *            the cargoPrimeiroFuncionario to set
	 */
	public void setCargoPrimeiroFuncionario(String cargoPrimeiroFuncionario) {
		this.cargoPrimeiroFuncionario = cargoPrimeiroFuncionario;
	}

	/**
	 * @return the nomeSegundoFuncionario
	 */
	public String getNomeSegundoFuncionario() {
		if (nomeSegundoFuncionario == null) {
			nomeSegundoFuncionario = "";
		}
		return nomeSegundoFuncionario;
	}

	/**
	 * @param nomeSegundoFuncionario
	 *            the nomeSegundoFuncionario to set
	 */
	public void setNomeSegundoFuncionario(String nomeSegundoFuncionario) {
		this.nomeSegundoFuncionario = nomeSegundoFuncionario;
	}

	/**
	 * @return the cargoSegundoFuncionario
	 */
	public String getCargoSegundoFuncionario() {
		if (cargoSegundoFuncionario == null) {
			cargoSegundoFuncionario = "";
		}
		return cargoSegundoFuncionario;
	}

	/**
	 * @param cargoSegundoFuncionario
	 *            the cargoSegundoFuncionario to set
	 */
	public void setCargoSegundoFuncionario(String cargoSegundoFuncionario) {
		this.cargoSegundoFuncionario = cargoSegundoFuncionario;
	}

	/**
	 * @return the dataPublicacaoDO
	 */
	public String getDataPublicacaoDO() {
		if (dataPublicacaoDO == null) {
			dataPublicacaoDO = "";
		}
		return dataPublicacaoDO;
	}

	/**
	 * @param dataPublicacaoDO
	 *            the dataPublicacaoDO to set
	 */
	public void setDataPublicacaoDO(String dataPublicacaoDO) {
		this.dataPublicacaoDO = dataPublicacaoDO;
	}

	public String getAutorizacaoNrRegistroInterno() {
		if (autorizacaoNrRegistroInterno == null) {
			autorizacaoNrRegistroInterno = "";
		}
		return autorizacaoNrRegistroInterno;
	}

	public void setAutorizacaoNrRegistroInterno(String autorizacaoNrRegistroInterno) {
		this.autorizacaoNrRegistroInterno = autorizacaoNrRegistroInterno;
	}

	/**
	 * @return the habilitacao
	 */
	public String getHabilitacao() {
		if (habilitacao == null) {
			habilitacao = "";
		}
		return habilitacao;
	}

	/**
	 * @param habilitacao
	 *            the habilitacao to set
	 */
	public void setHabilitacao(String habilitacao) {
		this.habilitacao = habilitacao;
	}

	/**
	 * @return the nacionalidadeAluno
	 */
	public String getNacionalidadeAluno() {
		if (nacionalidadeAluno == null) {
			nacionalidadeAluno = "";
		}
		return nacionalidadeAluno;
	}

	/**
	 * @param nacionalidadeAluno
	 *            the nacionalidadeAluno to set
	 */
	public void setNacionalidadeAluno(String nacionalidadeAluno) {
		this.nacionalidadeAluno = nacionalidadeAluno;
	}

	/**
	 * @return the naturalidadeAluno
	 */
	public String getNaturalidadeAluno() {
		if (naturalidadeAluno == null) {
			naturalidadeAluno = "";
		}
		return naturalidadeAluno;
	}

	/**
	 * @param naturalidadeAluno
	 *            the naturalidadeAluno to set
	 */
	public void setNaturalidadeAluno(String naturalidadeAluno) {
		this.naturalidadeAluno = naturalidadeAluno;
	}

	/**
	 * @return the rgAluno
	 */
	public String getRgAluno() {
		if (rgAluno == null) {
			rgAluno = "";
		}
		return rgAluno;
	}

	/**
	 * @param rgAluno
	 *            the rgAluno to set
	 */
	public void setRgAluno(String rgAluno) {
		this.rgAluno = rgAluno;
	}

	/**
	 * @return the orgaoEmissorAluno
	 */
	public String getOrgaoEmissorAluno() {
		if (orgaoEmissorAluno == null) {
			orgaoEmissorAluno = "";
		}
		return orgaoEmissorAluno;
	}

	/**
	 * @param orgaoEmissorAluno
	 *            the orgaoEmissorAluno to set
	 */
	public void setOrgaoEmissorAluno(String orgaoEmissorAluno) {
		this.orgaoEmissorAluno = orgaoEmissorAluno;
	}

	/**
	 * @return the estadoEmissorRgAluno
	 */
	public String getEstadoEmissorRgAluno() {
		if (estadoEmissorRgAluno == null) {
			estadoEmissorRgAluno = "";
		}
		return estadoEmissorRgAluno;
	}

	/**
	 * @param estadoEmissorRgAluno
	 *            the estadoEmissorRgAluno to set
	 */
	public void setEstadoEmissorRgAluno(String estadoEmissorRgAluno) {
		this.estadoEmissorRgAluno = estadoEmissorRgAluno;
	}

	/**
	 * @return the tituloCurso
	 */
	public String getTituloCurso() {
		if (tituloCurso == null) {
			tituloCurso = "";
		}
		return tituloCurso;
	}

	/**
	 * @param tituloCurso
	 *            the tituloCurso to set
	 */
	public void setTituloCurso(String tituloCurso) {
		if (tituloCurso.equals("Bacharelado")) {
			setGrauCurso("Bacharel");
		}
		if (tituloCurso.equals("Licenciatura")) {
			setGrauCurso("Licenciado");
		}
		if (getNivelEducacional().equals("GT")) {
			setGrauCurso("Tecnólogo");
		}
		this.tituloCurso = tituloCurso;
	}

	/**
	 * @return the dataColacaoGrau
	 */
	public String getDataColacaoGrau() {
		if (dataColacaoGrau == null) {
			dataColacaoGrau = "";
		}
		return dataColacaoGrau;
	}

	/**
	 * @param dataColacaoGrau
	 *            the dataColacaoGrau to set
	 */
	public void setDataColacaoGrau(String dataColacaoGrau) {
		this.dataColacaoGrau = dataColacaoGrau;
	}

	/**
	 * @return the nivelEducacional
	 */
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	/**
	 * @param nivelEducacional
	 *            the nivelEducacional to set
	 */
	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	/**
	 * @return the grauCurso
	 */
	public String getGrauCurso() {
		if (grauCurso == null) {
			grauCurso = "";
		}
		return grauCurso;
	}

	/**
	 * @param grauCurso
	 *            the grauCurso to set
	 */
	public void setGrauCurso(String grauCurso) {
		this.grauCurso = grauCurso;
	}

	/**
	 * @return the cargaHorariaTotal
	 */
	public String getCargaHorariaTotal() {
		if (cargaHorariaTotal == null) {
			cargaHorariaTotal = "";
		}
		return cargaHorariaTotal;
	}

	/**
	 * @param cargaHorariaTotal
	 *            the cargaHorariaTotal to set
	 */
	public void setCargaHorariaTotal(String cargaHorariaTotal) {
		this.cargaHorariaTotal = cargaHorariaTotal;
	}

	/**
	 * @return the listaHistorico
	 */
	public List<DiplomaAlunoHistoricoRelVO> getListaHistorico() {
		if (listaHistorico == null) {
			listaHistorico = new ArrayList(0);
		}
		return listaHistorico;
	}

	/**
	 * @param listaHistorico
	 *            the listaHistorico to set
	 */
	public void setListaHistorico(List<DiplomaAlunoHistoricoRelVO> listaHistorico) {
		this.listaHistorico = listaHistorico;
	}

	/**
	 * @return the cpfAluno
	 */
	public String getCpfAluno() {
		if (cpfAluno == null) {
			cpfAluno = "";
		}
		return cpfAluno;
	}

	/**
	 * @param cpfAluno
	 *            the cpfAluno to set
	 */
	public void setCpfAluno(String cpfAluno) {
		this.cpfAluno = cpfAluno;
	}

	/**
	 * @return the dataNascimentoSimples
	 */
	public String getDataNascimentoSimples() {
		if (dataNascimentoSimples == null) {
			dataNascimentoSimples = "";
		}
		return dataNascimentoSimples;
	}

	/**
	 * @param dataNascimentoSimples
	 *            the dataNascimentoSimples to set
	 */
	public void setDataNascimentoSimples(String dataNascimentoSimples) {
		this.dataNascimentoSimples = dataNascimentoSimples;
	}

	/**
	 * @return the credenciamentoPortaria
	 */
	public String getCredenciamentoPortaria() {
		if (credenciamentoPortaria == null) {
			credenciamentoPortaria = "";
		}
		return credenciamentoPortaria;
	}

	/**
	 * @param credenciamentoPortaria
	 *            the credenciamentoPortaria to set
	 */
	public void setCredenciamentoPortaria(String credenciamentoPortaria) {
		this.credenciamentoPortaria = credenciamentoPortaria;
	}

	/**
	 * @return the dataPublicacaoDoEmpresa
	 */
	public String getDataPublicacaoDoEmpresa() {
		if (dataPublicacaoDoEmpresa == null) {
			dataPublicacaoDoEmpresa = "";
		}
		return dataPublicacaoDoEmpresa;
	}

	/**
	 * @param dataPublicacaoDoEmpresa
	 *            the dataPublicacaoDoEmpresa to set
	 */
	public void setDataPublicacaoDoEmpresa(String dataPublicacaoDoEmpresa) {
		this.dataPublicacaoDoEmpresa = dataPublicacaoDoEmpresa;
	}

	public void setMantenedora(String mantenedora) {
		this.mantenedora = mantenedora;
	}

	public String getMantenedora() {
		if (mantenedora == null) {
			mantenedora = "";
		}
		return mantenedora;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public String getNomeUnidadeEnsino() {
		return nomeUnidadeEnsino;
	}

	public void setCidadeDataExpedicaoDiploma(String cidadeDataExpedicaoDiploma) {
		this.cidadeDataExpedicaoDiploma = cidadeDataExpedicaoDiploma;
	}

	public String getCidadeDataExpedicaoDiploma() {
		if (dataExpedicaoDiploma == null) {
			dataExpedicaoDiploma = "";
		}
		return cidadeDataExpedicaoDiploma;
	}

	public String getRazaoSocial() {
		if (razaoSocial == null) {
			razaoSocial = "";
		}
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCidadeDataAtual() {
		if (cidadeDataAtual == null) {
			cidadeDataAtual = "";
		}
		return cidadeDataAtual;
	}

	public void setCidadeDataAtual(String cidadeDataAtual) {
		this.cidadeDataAtual = cidadeDataAtual;
	}

	public String getNumeroRegistroDiploma() {
		if (numeroRegistroDiploma == null) {
			numeroRegistroDiploma = "";
		}
		return numeroRegistroDiploma;
	}

	public void setNumeroRegistroDiploma(String numeroRegistroDiploma) {
		this.numeroRegistroDiploma = numeroRegistroDiploma;
	}

	public String getNumeroProcesso() {
		if (numeroProcesso == null) {
			numeroProcesso = "";
		}
		return numeroProcesso;
	}

	public void setNumeroProcesso(String numeroProcesso) {
		this.numeroProcesso = numeroProcesso;
	}

	public String getVia() {
		if (via == null) {
			via = "";
		}
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}

	/**
	 * @return the viaAnterior
	 */
	public String getViaAnterior() {
		if (viaAnterior == null) {
			viaAnterior = "";
		}
		return viaAnterior;
	}

	/**
	 * @param viaAnterior
	 *            the viaAnterior to set
	 */
	public void setViaAnterior(String viaAnterior) {
		this.viaAnterior = viaAnterior;
	}

	/**
	 * @return the dataExpedicaoViaAnterior
	 */
	public String getDataExpedicaoViaAnterior() {
		if (dataExpedicaoViaAnterior == null) {
			dataExpedicaoViaAnterior = "";
		}
		return dataExpedicaoViaAnterior;
	}

	/**
	 * @param dataExpedicaoViaAnterior
	 *            the dataExpedicaoViaAnterior to set
	 */
	public void setDataExpedicaoViaAnterior(String dataExpedicaoViaAnterior) {
		this.dataExpedicaoViaAnterior = dataExpedicaoViaAnterior;
	}

	/**
	 * @return the dataConclusaoCurso
	 */
	public String getDataConclusaoCurso() {
		if (dataConclusaoCurso == null) {
			dataConclusaoCurso = "";
		}
		return dataConclusaoCurso;
	}

	/**
	 * @param dataConclusaoCurso
	 *            the dataConclusaoCurso to set
	 */
	public void setDataConclusaoCurso(String dataConclusaoCurso) {
		this.dataConclusaoCurso = dataConclusaoCurso;
	}

	public String getSexoAluno() {
		if (sexoAluno == null) {
			sexoAluno = "M";
		}
		return sexoAluno;
	}

	public void setSexoAluno(String sexoAluno) {
		this.sexoAluno = sexoAluno;
	}

	public String getNomeEstadoNascimento() {
		if (nomeEstadoNascimento == null) {
			nomeEstadoNascimento = "";
		}
		return nomeEstadoNascimento;
	}

	public void setNomeEstadoNascimento(String nomeEstadoNascimento) {
		this.nomeEstadoNascimento = nomeEstadoNascimento;
	}

	public String getTitulacaoDoFormando() {
		if (titulacaoDoFormando == null) {
			titulacaoDoFormando = "";
		}
		return titulacaoDoFormando;
	}

	public void setTitulacaoDoFormando(String titulacaoDoFormando) {
		this.titulacaoDoFormando = titulacaoDoFormando;
	}

	public String getAreaConhecimento() {
		if (areaConhecimento == null) {
			areaConhecimento = "";
		}
		return areaConhecimento;
	}

	public void setAreaConhecimento(String areaConhecimento) {
		this.areaConhecimento = areaConhecimento;
	}

	/**
	 * @return the reconhecimento
	 */
	public String getReconhecimento() {
		if (reconhecimento == null) {
			reconhecimento = "";
		}
		return reconhecimento;
	}

	/**
	 * @param reconhecimento
	 *            the reconhecimento to set
	 */
	public void setReconhecimento(String reconhecimento) {
		this.reconhecimento = reconhecimento;
	}

	/**
	 * @return the publicacaoDO
	 */
	public String getPublicacaoDO() {
		if (publicacaoDO == null) {
			publicacaoDO = "";
		}
		return publicacaoDO;
	}

	/**
	 * @param publicacaoDO
	 *            the publicacaoDO to set
	 */
	public void setPublicacaoDO(String publicacaoDO) {
		this.publicacaoDO = publicacaoDO;
	}

	/**
	 * @return the dataPublicacaoDOUnidade
	 */
	public String getDataPublicacaoDOUnidade() {
		if (dataPublicacaoDOUnidade == null) {
			dataPublicacaoDOUnidade = "";
		}
		return dataPublicacaoDOUnidade;
	}

	/**
	 * @param dataPublicacaoDOUnidade
	 *            the dataPublicacaoDOUnidade to set
	 */
	public void setDataPublicacaoDOUnidade(String dataPublicacaoDOUnidade) {
		this.dataPublicacaoDOUnidade = dataPublicacaoDOUnidade;
	}

	/**
	 * @return the autorizacaoNrRegistroInternoUnidade
	 */
	public String getAutorizacaoNrRegistroInternoUnidade() {
		if (autorizacaoNrRegistroInternoUnidade == null) {
			autorizacaoNrRegistroInternoUnidade = "";
		}
		return autorizacaoNrRegistroInternoUnidade;
	}

	/**
	 * @param autorizacaoNrRegistroInternoUnidade
	 *            the autorizacaoNrRegistroInternoUnidade to set
	 */
	public void setAutorizacaoNrRegistroInternoUnidade(String autorizacaoNrRegistroInternoUnidade) {
		this.autorizacaoNrRegistroInternoUnidade = autorizacaoNrRegistroInternoUnidade;
	}

	public String getReconhecimentoCursoAutorizacaoResolucao() {
		if (reconhecimentoCursoAutorizacaoResolucao == null) {
			reconhecimentoCursoAutorizacaoResolucao = "";
		}
		return reconhecimentoCursoAutorizacaoResolucao;
	}

	public void setReconhecimentoCursoAutorizacaoResolucao(String reconhecimentoCursoAutorizacaoResolucao) {
		this.reconhecimentoCursoAutorizacaoResolucao = reconhecimentoCursoAutorizacaoResolucao;
	}

	public String getAutorizacaoCurso() {
		if (autorizacaoCurso == null) {
			autorizacaoCurso = "";
		}
		return autorizacaoCurso;
	}

	public void setAutorizacaoCurso(String autorizacaoCurso) {
		this.autorizacaoCurso = autorizacaoCurso;
	}

	/**
	 * @return the nomeTerceiroFuncionario
	 */
	public String getNomeTerceiroFuncionario() {
		if (nomeTerceiroFuncionario == null) {
			nomeTerceiroFuncionario = "";
		}
		return nomeTerceiroFuncionario;
	}

	/**
	 * @param nomeTerceiroFuncionario
	 *            the nomeTerceiroFuncionario to set
	 */
	public void setNomeTerceiroFuncionario(String nomeTerceiroFuncionario) {
		this.nomeTerceiroFuncionario = nomeTerceiroFuncionario;
	}

	/**
	 * @return the cargoTerceiroFuncionario
	 */
	public String getCargoTerceiroFuncionario() {
		if (cargoTerceiroFuncionario == null) {
			cargoTerceiroFuncionario = "";
		}
		return cargoTerceiroFuncionario;
	}

	/**
	 * @param cargoTerceiroFuncionario
	 *            the cargoTerceiroFuncionario to set
	 */
	public void setCargoTerceiroFuncionario(String cargoTerceiroFuncionario) {
		this.cargoTerceiroFuncionario = cargoTerceiroFuncionario;
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

	public List<DiplomaAlunoHistoricoRelVO> getListaHistoricoEstabelecimento() {
		if (listaHistoricoEstabelecimento == null) {
			listaHistoricoEstabelecimento = new ArrayList<DiplomaAlunoHistoricoRelVO>(0);
		}
		return listaHistoricoEstabelecimento;
	}

	public void setListaHistoricoEstabelecimento(List<DiplomaAlunoHistoricoRelVO> listaHistoricoEstabelecimento) {
		this.listaHistoricoEstabelecimento = listaHistoricoEstabelecimento;
	}

	public String getCursoAnterior() {
		if (cursoAnterior == null) {
			cursoAnterior = "";
		}
		return cursoAnterior;
	}

	public void setCursoAnterior(String cursoAnterior) {
		this.cursoAnterior = cursoAnterior;
	}

	public String getEstabelecimentoAnterior() {
		if (estabelecimentoAnterior == null) {
			estabelecimentoAnterior = "";
		}
		return estabelecimentoAnterior;
	}

	public void setEstabelecimentoAnterior(String estabelecimentoAnterior) {
		this.estabelecimentoAnterior = estabelecimentoAnterior;
	}

	public String getEnderecoEstabelecimentoAnterior() {
		if (enderecoEstabelecimentoAnterior == null) {
			enderecoEstabelecimentoAnterior = "";
		}
		return enderecoEstabelecimentoAnterior;
	}

	public void setEnderecoEstabelecimentoAnterior(String enderecoEstabelecimentoAnterior) {
		this.enderecoEstabelecimentoAnterior = enderecoEstabelecimentoAnterior;
	}

	public String getDataAtualExtenso() {
		if (dataAtualExtenso == null) {
			dataAtualExtenso = "";
		}
		return dataAtualExtenso;
	}

	public void setDataAtualExtenso(String dataAtualExtenso) {
		this.dataAtualExtenso = dataAtualExtenso;
	}

	public String getTituloCursoCompleto() {
		tituloCursoCompleto = "";
		if (tituloCursoCompleto == null) {
		}
		return tituloCursoCompleto;
	}

	public void setTituloCursoCompleto(String tituloCursoCompleto) {
		this.tituloCursoCompleto = tituloCursoCompleto;
	}

	public String getPreposicaoNomeCurso() {
		if (preposicaoNomeCurso == null) {
			preposicaoNomeCurso = "";
		}
		return preposicaoNomeCurso;
	}

	public void setPreposicaoNomeCurso(String preposicaoNomeCurso) {
		this.preposicaoNomeCurso = preposicaoNomeCurso;
	}

	public String getSexoPrimeiroFuncionario() {
		if (sexoPrimeiroFuncionario == null) {
			sexoPrimeiroFuncionario = "";
		}
		return sexoPrimeiroFuncionario;
	}

	public void setSexoPrimeiroFuncionario(String sexoPrimeiroFuncionario) {
		this.sexoPrimeiroFuncionario = sexoPrimeiroFuncionario;
	}

	public String getSexoSegundoFuncionario() {
		if (sexoSegundoFuncionario == null) {
			sexoSegundoFuncionario = "";
		}
		return sexoSegundoFuncionario;
	}

	public void setSexoSegundoFuncionario(String sexoSegundoFuncionario) {
		this.sexoSegundoFuncionario = sexoSegundoFuncionario;
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

	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
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

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getFoneUnidadeEnsino() {
		if (foneUnidadeEnsino == null) {
			foneUnidadeEnsino = "";
		}
		return foneUnidadeEnsino;
	}

	public void setFoneUnidadeEnsino(String foneUnidadeEnsino) {
		this.foneUnidadeEnsino = foneUnidadeEnsino;
	}

	public String getAnoConclusao() {
		if (anoConclusao == null) {
			anoConclusao = "";
		}
		return anoConclusao;
	}

	public void setAnoConclusao(String anoConclusao) {
		this.anoConclusao = anoConclusao;
	}

	public String getEstadoSiglaUnidadeEnsino() {
		if (estadoSiglaUnidadeEnsino == null) {
			estadoSiglaUnidadeEnsino = "";
		}
		return estadoSiglaUnidadeEnsino;
	}

	public void setEstadoSiglaUnidadeEnsino(String estadoSiglaUnidadeEnsino) {
		this.estadoSiglaUnidadeEnsino = estadoSiglaUnidadeEnsino;
	}

	public String getCnpjUnidadeEnsino() {
		if (cnpjUnidadeEnsino == null) {
			cnpjUnidadeEnsino = "";
		}
		return cnpjUnidadeEnsino;
	}

	public void setCnpjUnidadeEnsino(String cnpjUnidadeEnsino) {
		this.cnpjUnidadeEnsino = cnpjUnidadeEnsino;
	}

	public String getPrimeiroReconhecimento() {
		if (primeiroReconhecimento == null) {
			primeiroReconhecimento = "";
		}
		return primeiroReconhecimento;
	}

	public void setPrimeiroReconhecimento(String primeiroReconhecimento) {
		this.primeiroReconhecimento = primeiroReconhecimento;
	}

	public String getLeiCriacao1() {
		if (leiCriacao1 == null) {
			leiCriacao1 = "";
		}
		return leiCriacao1;
	}

	public void setLeiCriacao1(String leiCriacao1) {
		this.leiCriacao1 = leiCriacao1;
	}

	public String getLeiCriacao2() {
		if (leiCriacao2 == null) {
			leiCriacao2 = "";
		}
		return leiCriacao2;
	}

	public void setLeiCriacao2(String leiCriacao2) {
		this.leiCriacao2 = leiCriacao2;
	}

	public String getPortariaCargoPrimeiroFuncionario() {
		if (portariaCargoPrimeiroFuncionario == null) {
			portariaCargoPrimeiroFuncionario = "";
		}
		return portariaCargoPrimeiroFuncionario;
	}

	public void setPortariaCargoPrimeiroFuncionario(String portariaCargoPrimeiroFuncionario) {
		this.portariaCargoPrimeiroFuncionario = portariaCargoPrimeiroFuncionario;
	}

	public String getPortariaCargoSegundoFuncionario() {
		if (portariaCargoSegundoFuncionario == null) {
			portariaCargoSegundoFuncionario = "";
		}
		return portariaCargoSegundoFuncionario;
	}

	public void setPortariaCargoSegundoFuncionario(String portariaCargoSegundoFuncionario) {
		this.portariaCargoSegundoFuncionario = portariaCargoSegundoFuncionario;
	}

	public String getPortariaCargoTerceiroFuncionario() {
		if (portariaCargoTerceiroFuncionario == null) {
			portariaCargoTerceiroFuncionario = "";
		}
		return portariaCargoTerceiroFuncionario;
	}

	public void setPortariaCargoTerceiroFuncionario(String portariaCargoTerceiroFuncionario) {
		this.portariaCargoTerceiroFuncionario = portariaCargoTerceiroFuncionario;
	}
	public String getDataExpedicaoDiploma() {
		return dataExpedicaoDiploma;
	}

	public void setDataExpedicaoDiploma(String dataExpedicaoDiploma) {
		this.dataExpedicaoDiploma = dataExpedicaoDiploma;
	}

    public List<ObservacaoComplementarDiplomaVO> getObservacaoComplementarDiplomaVOs() {
        if (observacaoComplementarDiplomaVOs == null) {
            observacaoComplementarDiplomaVOs = new ArrayList<ObservacaoComplementarDiplomaVO>();
        }
        return observacaoComplementarDiplomaVOs;
    }

    /**
     * @param observacaoComplementarVOs the observacaoComplementarVOs to set
     */
    public void setObservacaoComplementarDiplomaVOs(List<ObservacaoComplementarDiplomaVO> observacaoComplementarDiplomaVOs) {
        this.observacaoComplementarDiplomaVOs = observacaoComplementarDiplomaVOs;
    }
    
    public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma0() {
        int i = 0;
        for (ObservacaoComplementarDiplomaVO obs : this.getObservacaoComplementarDiplomaVOs()) {
            if (obs.getObservacaoComplementar().getOrdem() == 0) {
            	return obs;
            }
//        	if (i == 0) {
//                return obs;
//            }
//            i++;
//            obs.getObservacaoComplementar().getObservacao();
        }
        return new ObservacaoComplementarDiplomaVO();
    }

    public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma1() {
    	int i = 0;
    	for (ObservacaoComplementarDiplomaVO obs : this.getObservacaoComplementarDiplomaVOs()) {
            if (obs.getObservacaoComplementar().getOrdem() == 1) {
            	return obs;
            }
//    		if (i == 0) {
//    			return obs;
//    		}
//    		i++;
//    		obs.getObservacaoComplementar().getObservacao();
    	}
    	return new ObservacaoComplementarDiplomaVO();
    }

    public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma2() {
        int i = 0;
        for (ObservacaoComplementarDiplomaVO obs : this.getObservacaoComplementarDiplomaVOs()) {
            if (obs.getObservacaoComplementar().getOrdem() == 2) {
            	return obs;
            }
//        	if (i == 1) {
//                return obs;
//            }
//            i++;
        }
        return new ObservacaoComplementarDiplomaVO();
    }

    public ObservacaoComplementarDiplomaVO getObservacaoComplementarDiploma3() {
        int i = 0;
        for (ObservacaoComplementarDiplomaVO obs : this.getObservacaoComplementarDiplomaVOs()) {
            if (obs.getObservacaoComplementar().getOrdem() == 3) {
            	return obs;
            }
//        	if (i == 2) {
//                return obs;
//            }
//            i++;
        }
        return new ObservacaoComplementarDiplomaVO();
    }

    public String getNumeroRegistroDiplomaViaAnterior() {
        if (numeroRegistroDiplomaViaAnterior == null) {
            numeroRegistroDiplomaViaAnterior = "";
        }
        return numeroRegistroDiplomaViaAnterior;
    }

    public void setNumeroRegistroDiplomaViaAnterior(String numeroRegistroDiplomaViaAnterior) {
        this.numeroRegistroDiplomaViaAnterior = numeroRegistroDiplomaViaAnterior;
    }

    public String getNumeroProcessoViaAnterior() {
        if (numeroProcessoViaAnterior == null) {
            numeroProcessoViaAnterior = "";
        }
        return numeroProcessoViaAnterior;
    }

    public void setNumeroProcessoViaAnterior(String numeroProcessoViaAnterior) {
        this.numeroProcessoViaAnterior = numeroProcessoViaAnterior;
    }

	public String getDataRegistroDiplomaViaAnterior() {
		if (dataRegistroDiplomaViaAnterior == null) {
			dataRegistroDiplomaViaAnterior = "";
		}
		return dataRegistroDiplomaViaAnterior;
	}

	public void setDataRegistroDiplomaViaAnterior(String dataRegistroDiplomaViaAnterior) {
		this.dataRegistroDiplomaViaAnterior = dataRegistroDiplomaViaAnterior;
	}

	public String getReitorRegistroDiplomaViaAnterior() {
		if (reitorRegistroDiplomaViaAnterior == null) {
			reitorRegistroDiplomaViaAnterior = "";
		}
		return reitorRegistroDiplomaViaAnterior;
	}

	public void setReitorRegistroDiplomaViaAnterior(String reitorRegistroDiplomaViaAnterior) {
		this.reitorRegistroDiplomaViaAnterior = reitorRegistroDiplomaViaAnterior;
	}

	public String getSecretariaRegistroDiplomaViaAnterior() {
		if (secretariaRegistroDiplomaViaAnterior == null) {
			secretariaRegistroDiplomaViaAnterior = "";
		}
		return secretariaRegistroDiplomaViaAnterior;
	}

	public void setSecretariaRegistroDiplomaViaAnterior(String secretariaRegistroDiplomaViaAnterior) {
		this.secretariaRegistroDiplomaViaAnterior = secretariaRegistroDiplomaViaAnterior;
	}

	public String getCargoReitorRegistroDiplomaViaAnterior() {
		if (cargoReitorRegistroDiplomaViaAnterior == null) {
			cargoReitorRegistroDiplomaViaAnterior = "";
		}
		return cargoReitorRegistroDiplomaViaAnterior;
	}

	public void setCargoReitorRegistroDiplomaViaAnterior(String cargoReitorRegistroDiplomaViaAnterior) {
		this.cargoReitorRegistroDiplomaViaAnterior = cargoReitorRegistroDiplomaViaAnterior;
	}

	public String getNomeQuartoFuncionario() {
		if (nomeQuartoFuncionario == null) {
			nomeQuartoFuncionario = Constantes.EMPTY;
		}
		return nomeQuartoFuncionario;
	}

	public void setNomeQuartoFuncionario(String nomeQuartoFuncionario) {
		this.nomeQuartoFuncionario = nomeQuartoFuncionario;
	}

	public String getNomeQuintoFuncionario() {
		if (nomeQuintoFuncionario == null) {
			nomeQuintoFuncionario = Constantes.EMPTY;
		}
		return nomeQuintoFuncionario;
	}

	public void setNomeQuintoFuncionario(String nomeQuintoFuncionario) {
		this.nomeQuintoFuncionario = nomeQuintoFuncionario;
	}

	public String getCargoQuartoFuncionario() {
		if (cargoQuartoFuncionario == null) {
			cargoQuartoFuncionario = Constantes.EMPTY;
		}
		return cargoQuartoFuncionario;
	}

	public void setCargoQuartoFuncionario(String cargoQuartoFuncionario) {
		this.cargoQuartoFuncionario = cargoQuartoFuncionario;
	}

	public String getCargoQuintoFuncionario() {
		if (cargoQuintoFuncionario == null) {
			cargoQuintoFuncionario = Constantes.EMPTY;
		}
		return cargoQuintoFuncionario;
	}

	public void setCargoQuintoFuncionario(String cargoQuintoFuncionario) {
		this.cargoQuintoFuncionario = cargoQuintoFuncionario;
	}

	public String getPortariaCargoQuartoFuncionario() {
		if (portariaCargoQuartoFuncionario == null) {
			portariaCargoQuartoFuncionario = Constantes.EMPTY;
		}
		return portariaCargoQuartoFuncionario;
	}

	public void setPortariaCargoQuartoFuncionario(String portariaCargoQuartoFuncionario) {
		this.portariaCargoQuartoFuncionario = portariaCargoQuartoFuncionario;
	}

	public String getPortariaCargoQuintoFuncionario() {
		if (portariaCargoQuintoFuncionario == null) {
			portariaCargoQuintoFuncionario = Constantes.EMPTY;
		}
		return portariaCargoQuintoFuncionario;
	}

	public void setPortariaCargoQuintoFuncionario(String portariaCargoQuintoFuncionario) {
		this.portariaCargoQuintoFuncionario = portariaCargoQuintoFuncionario;
	}
	
	public String getPercentualCHIntegralizacaoMatricula() {
		if (percentualCHIntegralizacaoMatricula == null) {
			percentualCHIntegralizacaoMatricula = "";
		}
		return percentualCHIntegralizacaoMatricula;
	}

	public void setPercentualCHIntegralizacaoMatricula(String percentualCHIntegralizacaoMatricula) {
		this.percentualCHIntegralizacaoMatricula = percentualCHIntegralizacaoMatricula;
	}
}
