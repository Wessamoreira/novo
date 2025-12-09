package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.AtuacaoDocente;
import negocio.comuns.utilitarias.dominios.Escolaridade;
import negocio.facade.jdbc.academico.Censo;

/**
 * Reponsável por manter os dados da entidade CensoProfessorItem. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Censo
 */
public class ProfessorCensoVO extends SuperVO {

	private Integer codigo;
	// private CensoVO censo;
	private String tipoRegistro;
	private String codigoINEP;
	private String idDocenteIES;
	private String nome;
	private String dataNascimento;
	private String sexo;
	private String corRaca;
	private String nomeCompletoMae;
	private String nacionalidade;
	private String paisOrigem;
	private String ufDeNascimento;
	private String municipioNascimento;
	private String docenteDeficiente;
	private String cegueira;
	private String baixaVisao;
	private String surdez;
	private String auditiva;
	private String surdocegueira;
	private String fisica;
	private String mental;
	private String multipla;
	private String CPF;
	private String escolaridade;
	private String posGraduacao;
	private String situacaoDocente;
	private String documentoEstrangeiro;
	private String docenteEmExercicio31122010IES;
	private String regimeTrabalho;
	private String docenteSubstituto;
	private String docenteVisitante;
	private String tipoVinculoDocenteIES;
	private String atuacaoDoDocenteEnsinoSequencialFormacaoEspecifica;
	private String atuacaoDoDocenteGraduacaoPresencial;
	private String atuacaoDoDocenteEnsinoGraduacaoADistancia;
	private String atuacaoDoDocenteEnsinoPosGraduacaoPresencial;
	private String atuacaoDoDocenteEnsinoPosGraduacaoADistancia;
	private String atuacaoDoDocentePesquisa;
	private String atuacaoDoDocenteExtensao;
	private String atuacaoDoDocenteGestaoPlanejamentoEAvaliacao;
	private String bolsaPesquisa;
	private String codigoTurma;
	private List<CursoCensoProfessorVO> listaCursoCenso;
	private List<TurmaCensoVO> listaTurmaCensoVOs;
	private Integer codigoFormacaoAcademica;
	private String tipoInstituicaoFormacaoAcademica;

	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private PessoaVO professor;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>CensoProfessorItem</code>. Cria uma
	 * nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	public ProfessorCensoVO() {
		super();
		inicializarDados();
	}

	public String getEscolaridadeEPosGraduacaoProfessor() {
		return "2|2";
	}

	public String getEscolaridadeEPosGraduacao() {
		Escolaridade escolaridadeAtual;
		Escolaridade escolaridadeMaior = Escolaridade.PRIMARIO;
		return "2|1";
		// for (FormacaoAcademicaVO formacaoAcademica :
		// professor.getFormacaoAcademicaVOs()) {
		// escolaridadeAtual =
		// Escolaridade.getEnum(formacaoAcademica.getEscolaridade());
		// if (escolaridadeAtual != null && escolaridadeAtual.getCodigoCenso() >
		// escolaridadeMaior.getCodigoCenso()) {
		// escolaridadeMaior = escolaridadeAtual;
		// }
		// }
		// if (escolaridadeMaior == Escolaridade.PRIMARIO) {
		// return "1|0";
		// } else {
		// return "2|" + escolaridadeMaior.getCodigoCenso();
		// }
	}

	/**
	 * Informa se o docente estava trabalhando no ultimo dia do ano anterior
	 * 
	 * @return 1 - sim ou 0 - não
	 */
	public String getDocenteEmExercicioAnoAnterior(FuncionarioVO funcionarioVO) {
		GregorianCalendar calendar = new GregorianCalendar();
		int anoAtual = calendar.get(GregorianCalendar.YEAR);
		calendar = new GregorianCalendar(anoAtual - 1, 12, 31);
		if (funcionarioVO.getDataAdmissao() == null) {
			return "0";
		}
		if (funcionarioVO.getDataAdmissao().before(calendar.getTime()) && funcionarioVO.getSituacaoDocenteCenso() == 1) {
			return "1";
		} else {
			return "0";
		}
	}

	// public String getVinculoProfessorCurso(List<CursoVO> cursos) {
	// StringBuilder registro = new StringBuilder();
	//
	// for (CursoVO curso : cursos) {
	// registro.append(CensoVO.TIPO_REGISTRO_VINCULO_PROFESSOR_CURSO +
	// CensoVO.SEPARADOR);
	// registro.append(curso.getIdCursoInep() + CensoVO.SEPARADOR);
	// registro.append("\n");
	// }
	//
	// return registro.toString();
	// }
	public String getAtuacaoDoDocente(FuncionarioVO funcionarioVO) {
		String atuacao = "0|1|0|0|0|0|0|0|0";
		AtuacaoDocente docente = AtuacaoDocente.getEnum(funcionarioVO.getAtuacaoDocente());
		if (docente == null) {
			return atuacao;
		}
		switch (docente) {
		case CURSO_SEQUENCIAL_FORMACAO_ESPECIFICA:
			atuacao = "1|0|0|0|0|0|0|0|0";
			break;
		case CURSO_GRADUACAO_PRESENCIAL:
			atuacao = "0|1|0|0|0|0|0|0|0";
			break;
		case CURSO_EAD:
			atuacao = "0|0|1|0|0|0|0|0|0";
			break;
		case POSGRADUACAO_STRICTO_SENSU_PRESENCIAL:
			atuacao = "0|0|0|1|0|0|0|0|0";
			break;
		case POSGRADUACAO_STRICTO_SENSU_EAD:
			atuacao = "0|0|0|0|1|0|0|0|0";
			break;
		case PESQUISA:
			atuacao = "0|0|0|0|0|1|0|0|0";
			break;
		case EXTENSAO:
			atuacao = "0|0|0|0|0|0|1|0|0";
			break;
		case GESTAO_PLANEJAMENTO_AVALIACAO:
			atuacao = "0|0|0|0|0|0|0|1|0";
			break;
		case PESQUISA_COM_BOLSA:
			atuacao = "0|0|0|0|0|0|0|0|1";
			break;
		}
		return atuacao;
	}

	public String getAtuacaoDoDocentePesquisa(FuncionarioVO funcionarioVO) {
		String atuacao = "0|1|0|";
		AtuacaoDocente docente = AtuacaoDocente.getEnum(funcionarioVO.getAtuacaoDocente());
		if (docente == null) {
			return atuacao;
		}
		switch (docente) {
		case PESQUISA:
			atuacao = "1|0|0|0";
			break;
		case EXTENSAO:
			atuacao = "0|1|0|";
			break;
		case GESTAO_PLANEJAMENTO_AVALIACAO:
			atuacao = "0|0|1|";
			break;
		case PESQUISA_COM_BOLSA:
			atuacao = "1|0|0|1";
			break;
		}
		return atuacao;
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>CensoProfessorItemVO</code>.
	 */
	// public static void validarUnicidade(List<CensoProfessorItemVO> lista,
	// CensoProfessorItemVO obj) throws ConsistirException {
	// for (CensoProfessorItemVO repetido : lista) {
	// if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
	// throw new ConsistirException("O campo CODIGO já esta cadastrado!");
	// }
	// }
	// }
	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CensoProfessorItemVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(ProfessorCensoVO professorCenso) throws ConsistirException {
		if (!professorCenso.isValidarDados().booleanValue()) {
			return;
		}
		if ((professorCenso.getProfessor() == null) || (professorCenso.getProfessor().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo PROFESSOR (professorCenso) deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(null);
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>CensoProfessorItem</code>).
	 */
	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return (professor);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>CensoProfessorItem</code>).
	 */
	public void setProfessor(PessoaVO obj) {
		this.professor = obj;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String CPF) {
		this.CPF = CPF;
	}

	public String getAtuacaoDoDocenteEnsinoGraduacaoADistancia() {
		return atuacaoDoDocenteEnsinoGraduacaoADistancia;
	}

	public void setAtuacaoDoDocenteEnsinoGraduacaoADistancia(String atuacaoDoDocenteEnsinoGraduacaoADistancia) {
		this.atuacaoDoDocenteEnsinoGraduacaoADistancia = atuacaoDoDocenteEnsinoGraduacaoADistancia;
	}

	public String getAtuacaoDoDocenteEnsinoPosGraduacaoADistancia() {
		return atuacaoDoDocenteEnsinoPosGraduacaoADistancia;
	}

	public void setAtuacaoDoDocenteEnsinoPosGraduacaoADistancia(String atuacaoDoDocenteEnsinoPosGraduacaoADistancia) {
		this.atuacaoDoDocenteEnsinoPosGraduacaoADistancia = atuacaoDoDocenteEnsinoPosGraduacaoADistancia;
	}

	public String getAtuacaoDoDocenteEnsinoPosGraduacaoPresencial() {
		return atuacaoDoDocenteEnsinoPosGraduacaoPresencial;
	}

	public void setAtuacaoDoDocenteEnsinoPosGraduacaoPresencial(String atuacaoDoDocenteEnsinoPosGraduacaoPresencial) {
		this.atuacaoDoDocenteEnsinoPosGraduacaoPresencial = atuacaoDoDocenteEnsinoPosGraduacaoPresencial;
	}

	public String getAtuacaoDoDocenteEnsinoSequencialFormacaoEspecifica() {
		return atuacaoDoDocenteEnsinoSequencialFormacaoEspecifica;
	}

	public void setAtuacaoDoDocenteEnsinoSequencialFormacaoEspecifica(String atuacaoDoDocenteEnsinoSequencialFormacaoEspecifica) {
		this.atuacaoDoDocenteEnsinoSequencialFormacaoEspecifica = atuacaoDoDocenteEnsinoSequencialFormacaoEspecifica;
	}

	public String getAtuacaoDoDocenteExtensao() {
		return atuacaoDoDocenteExtensao;
	}

	public void setAtuacaoDoDocenteExtensao(String atuacaoDoDocenteExtensao) {
		this.atuacaoDoDocenteExtensao = atuacaoDoDocenteExtensao;
	}

	public String getAtuacaoDoDocenteGestaoPlanejamentoEAvaliacao() {
		return atuacaoDoDocenteGestaoPlanejamentoEAvaliacao;
	}

	public void setAtuacaoDoDocenteGestaoPlanejamentoEAvaliacao(String atuacaoDoDocenteGestaoPlanejamentoEAvaliacao) {
		this.atuacaoDoDocenteGestaoPlanejamentoEAvaliacao = atuacaoDoDocenteGestaoPlanejamentoEAvaliacao;
	}

	public String getAtuacaoDoDocenteGraduacaoPresencial() {
		return atuacaoDoDocenteGraduacaoPresencial;
	}

	public void setAtuacaoDoDocenteGraduacaoPresencial(String atuacaoDoDocenteGraduacaoPresencial) {
		this.atuacaoDoDocenteGraduacaoPresencial = atuacaoDoDocenteGraduacaoPresencial;
	}

	public String getAtuacaoDoDocentePesquisa() {
		return atuacaoDoDocentePesquisa;
	}

	public void setAtuacaoDoDocentePesquisa(String atuacaoDoDocentePesquisa) {
		this.atuacaoDoDocentePesquisa = atuacaoDoDocentePesquisa;
	}

	public String getAuditiva() {
		return auditiva;
	}

	public void setAuditiva(String auditiva) {
		this.auditiva = auditiva;
	}

	public String getBaixaVisao() {
		return baixaVisao;
	}

	public void setBaixaVisao(String baixaVisao) {
		this.baixaVisao = baixaVisao;
	}

	public String getBolsaPesquisa() {
		return bolsaPesquisa;
	}

	public void setBolsaPesquisa(String bolsaPesquisa) {
		this.bolsaPesquisa = bolsaPesquisa;
	}

	public String getCegueira() {
		return cegueira;
	}

	public void setCegueira(String cegueira) {
		this.cegueira = cegueira;
	}

	public String getCorRaca() {
		return corRaca;
	}

	public void setCorRaca(String corRaca) {
		this.corRaca = corRaca;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getDocenteDeficiente() {
		return docenteDeficiente;
	}

	public void setDocenteDeficiente(String docenteDeficiente) {
		this.docenteDeficiente = docenteDeficiente;
	}

	public String getDocenteEmExercicio31122010IES() {
		return docenteEmExercicio31122010IES;
	}

	public void setDocenteEmExercicio31122010IES(String docenteEmExercicio31122010IES) {
		this.docenteEmExercicio31122010IES = docenteEmExercicio31122010IES;
	}

	public String getDocenteSubstituto() {
		if (docenteSubstituto == null) {
			docenteSubstituto = "";
		}
		return docenteSubstituto;
	}

	public void setDocenteSubstituto(String docenteSubstituto) {
		this.docenteSubstituto = docenteSubstituto;
	}

	public String getDocenteVisitante() {
		if (docenteVisitante == null) {
			if (posGraduacao.equals("3")) {
				docenteVisitante = "0";
			} else {
				docenteVisitante = "";
			}
		}
		return docenteVisitante;
	}

	public void setDocenteVisitante(String docenteVisitante) {
		this.docenteVisitante = docenteVisitante;
	}

	public String getDocumentoEstrangeiro() {
		return documentoEstrangeiro;
	}

	public void setDocumentoEstrangeiro(String documentoEstrangeiro) {
		this.documentoEstrangeiro = documentoEstrangeiro;
	}

	public String getEscolaridade() {
		if (escolaridade == null) {
			escolaridade = "1";
		}
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	public String getFisica() {
		return fisica;
	}

	public void setFisica(String fisica) {
		this.fisica = fisica;
	}

	public String getIdDocenteIES() {
		return idDocenteIES;
	}

	public void setIdDocenteIES(String idDocenteIES) {
		this.idDocenteIES = idDocenteIES;
	}

	public String getMental() {
		return mental;
	}

	public void setMental(String mental) {
		this.mental = mental;
	}

	public String getMultipla() {
		return multipla;
	}

	public void setMultipla(String multipla) {
		this.multipla = multipla;
	}

	public String getMunicipioNascimento() {
		return municipioNascimento;
	}

	public void setMunicipioNascimento(String municipioNascimento) {
		this.municipioNascimento = municipioNascimento;
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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeCompletoMae() {
		return nomeCompletoMae;
	}

	public void setNomeCompletoMae(String nomeCompletoMae) {
		this.nomeCompletoMae = nomeCompletoMae;
	}

	public String getPaisOrigem() {
		if (paisOrigem == null) {
			return "BRA";
		}
		if (paisOrigem.equals("")) {
			return "BRA";
		}
		if (paisOrigem.equals("0")) {
			return "BRA";
		}
		if (paisOrigem.equals("3")) {
			return "ARG";
		}
		if (paisOrigem.equals("1")) {
			return "BRA";
		}
		return paisOrigem;
	}

	public void setPaisOrigem(String paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public String getPosGraduacao() {
		if (posGraduacao == null) {
			if (getEscolaridade().equals("2")) {
				posGraduacao = "1";
			} else if (getEscolaridade().equals("1")) {
				posGraduacao = "";
			} else {
				posGraduacao = "";
			}

		}
		return posGraduacao;
	}

	public void setPosGraduacao(String posGraduacao) {
		this.posGraduacao = posGraduacao;
	}

	public String getRegimeTrabalho() {
		return regimeTrabalho;
	}

	public void setRegimeTrabalho(String regimeTrabalho) {
		this.regimeTrabalho = regimeTrabalho;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSituacaoDocente() {
		return situacaoDocente;
	}

	public void setSituacaoDocente(String situacaoDocente) {
		this.situacaoDocente = situacaoDocente;
	}

	public String getSurdez() {
		return surdez;
	}

	public void setSurdez(String surdez) {
		this.surdez = surdez;
	}

	public String getSurdocegueira() {
		return surdocegueira;
	}

	public void setSurdocegueira(String surdocegueira) {
		this.surdocegueira = surdocegueira;
	}

	public String getTipoRegistro() {
		return tipoRegistro;
	}

	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}

	public String getTipoVinculoDocenteIES() {
		if (tipoVinculoDocenteIES == null) {
			tipoVinculoDocenteIES = "";
		}
		return tipoVinculoDocenteIES;
	}

	public void setTipoVinculoDocenteIES(String tipoVinculoDocenteIES) {
		this.tipoVinculoDocenteIES = tipoVinculoDocenteIES;
	}

	public String getUfDeNascimento() {
		return ufDeNascimento;
	}

	public void setUfDeNascimento(String ufDeNascimento) {
		this.ufDeNascimento = ufDeNascimento;
	}

	public List<CursoCensoProfessorVO> getListaCursoCenso() {
		if (listaCursoCenso == null) {
			listaCursoCenso = new ArrayList<CursoCensoProfessorVO>(0);
		}
		return listaCursoCenso;
	}

	public void setListaCursoCenso(List<CursoCensoProfessorVO> listaCursoCenso) {
		this.listaCursoCenso = listaCursoCenso;
	}

	public String getCodigoINEP() {
		return codigoINEP;
	}

	public void setCodigoINEP(String codigoINEP) {
		this.codigoINEP = codigoINEP;
	}

	public String getCodigoTurma() {
		return codigoTurma;
	}

	public void setCodigoTurma(String codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	public List<TurmaCensoVO> getListaTurmaCensoVOs() {
		if (listaTurmaCensoVOs == null) {
			listaTurmaCensoVOs = new ArrayList<TurmaCensoVO>(0);
		}
		return listaTurmaCensoVOs;
	}

	public void setListaTurmaCensoVOs(List<TurmaCensoVO> listaTurmaCensoVOs) {
		this.listaTurmaCensoVOs = listaTurmaCensoVOs;
	}

	public Integer getCodigoFormacaoAcademica() {
		if (codigoFormacaoAcademica == null) {
			codigoFormacaoAcademica = 0;
		}
		return codigoFormacaoAcademica;
	}

	public void setCodigoFormacaoAcademica(Integer codigoFormacaoAcademica) {
		this.codigoFormacaoAcademica = codigoFormacaoAcademica;
	}

	public String getTipoInstituicaoFormacaoAcademica() {
		if (tipoInstituicaoFormacaoAcademica == null) {
			tipoInstituicaoFormacaoAcademica = "";
		}
		return tipoInstituicaoFormacaoAcademica;
	}

	public void setTipoInstituicaoFormacaoAcademica(String tipoInstituicaoFormacaoAcademica) {
		this.tipoInstituicaoFormacaoAcademica = tipoInstituicaoFormacaoAcademica;
	}
}
