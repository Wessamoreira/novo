package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.ApoioSocial;
import negocio.comuns.utilitarias.dominios.AtividadeFormacaoComplementar;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.ReservasVagas;
import negocio.facade.jdbc.academico.Censo;

/**
 * Reponsável por manter os dados da entidade CensoAlunoItem. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Censo
 */
public class AlunoCensoVO extends SuperVO {

	private Integer codigo;
	// private CensoVO censo;
	private String idAlunoIES;
	private String idAlunoINEP;
	private String nome;
	private String dataNasc;
	private String sexo;
	private String corRaca;
	private String nomeMae;
	private String nomePai;
	private String nacionalidade;
	private String paisOrigem;
	private String ufDeNascimento;
	private String municipioDeNascimento;
	private String alunoComDeficiencia;
	private String cegueira;
	private String baixaVisao;
	private String surdez;
	private String auditiva;
	private String surdocegueira;
	private String fisica;
	private String mental;
	private String multipla;
	private String autismoInfantil;
	private String sindromeAsperger;
	private String sindromeRett;
	private String transtornoDesintegrativoInfancia;
	private String altasHabilidadesSuperdotacao;
	private String localizacaoZonaResidencia;
	private String escolarizacaoOutroEspaco;
	private String transportePublico;
	private String cpf;
	private String nomeFiliador;
	private String passaporte;
	private String tipoRegistro;
	private List<CursoCensoVO> listaCursoCenso;
	private String titulo;
	// Dados Auxiliares
	private String matricula;
	private String codigoAluno;
	private String etapaEnsino;
	private String nivelEducacional;
	private String tipoEscolaConcluiuEnsinoMedio;
	private Integer codigoIbgeEstado;
	private String baixaVisaoOuVisaoMonocular;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Matricula </code>.
	 */
	private MatriculaPeriodoVO matriculaPeriodo;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>CensoAlunoItem</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public AlunoCensoVO() {
		super();
	}

	public String getFormaIngresso(String formaIngressoBase) {
		String formaIngresso = "0|0|0|0|0|0|0|0|1|0";
		FormaIngresso formaIngressoEnum = FormaIngresso.getEnum(formaIngressoBase);
		if (formaIngressoEnum == null) {
			return formaIngresso;
		}
		switch (formaIngressoEnum) {
		case PROCESSO_SELETIVO:
			formaIngresso = "1|0|0|0|0|0|0|0|0|0";
			break;
		case VESTIBULAR:
			formaIngresso = "1|0|0|0|0|0|0|0|0|0";
			break;
		case ENEM:
			formaIngresso = "0|1|0|0|0|0|0|0|0|0";
			break;
		case AVALIACAO_SERIADA:
			formaIngresso = "0|0|1|0|0|0|0|0|0|0";
			break;
		case SELECAO_SIMPLIFICADA:
			formaIngresso = "0|0|0|1|0|0|0|0|0|0";
			break;
		case PECG:
			formaIngresso = "0|0|0|0|0|1|0|0|0|0";
			break;
		case TRANSFERENCIA_EXTERNA:
			formaIngresso = "0|0|0|0|0|0|0|0|1|0";
			break;
		case DECISAO_JUDICIAL:
			formaIngresso = "0|0|0|0|0|0|0|1|0|0";
			break;
		case VAGAS_REMANESCENTES:
			formaIngresso = "0|0|0|0|0|0|0|0|1|0";
			break;
		case VAGAS_PROGRAMAS_ESPECIAIS:
			formaIngresso = "0|0|0|0|0|0|0|0|0|1";
			break;
		case OUTROS_TIPOS_SELECAO:
			formaIngresso = "0|0|0|0|0|0|0|0|1|0";
			break;
		/*
		 * case PEC_G: formaIngresso = "0|0|0|1|0"; break;
		 */
		}
		return formaIngresso;
	}

	public String getProgramaReservaVagas(String programaReservaVagaBase) {
	    String programaReservaVagas = "0||||||||||||||"; // Garante 15 colunas vazias
	    ReservasVagas reservasVagas = ReservasVagas.getEnum(programaReservaVagaBase);

	    if (reservasVagas == null) {
	        return programaReservaVagas;
	    }

	    switch (reservasVagas) {
	        case ETNICO:
	            programaReservaVagas = "1|2||1|0|0|0|0|0|0|0|0|0|0|0"; 
	            break;
	        case SOCIAL_RENDA_FAMILIAR:
	            programaReservaVagas = "1|2||0|1|0|0|0|0|0|0|0|0|0|0";
	            break;
	        case DEFICIENCIA:
	            programaReservaVagas = "1|2||0|0|1|0|0|0|0|0|0|0|0|0"; 
	            break;
	        case PROCEDENTE_ENSINO_PUBLICO:
	            programaReservaVagas = "1|2||0|0|0|1|0|0|0|0|0|0|0|0";
	            break;
	        case QUILOMBOLA:
	            programaReservaVagas = "1|2||0|0|0|0|1|0|0|0|0|0|0|0";
	            break;
	        case TRANSGENERO_TRAVESTI:
	            programaReservaVagas = "1|2||0|0|0|0|0|1|0|0|0|0|0|0";
	            break;
	        case ESTRANGEIRO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|1|0|0|0|0|0";
	            break;
	        case REFUGIADO_APATRIADO_VISTO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|1|0|0|0|0";
	            break;
	        case IDOSO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|1|0|0|0";
	            break;
	        case COMUNIDADE_TRADICIONAL:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|0|1|0|0";
	            break;
	        case COMPETICOES_CONHECIMENTO:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|0|0|1|0";
	            break;
	        case OUTROS:
	            programaReservaVagas = "1|2||0|0|0|0|0|0|0|0|0|0|0|1";
	            break;

	        default:
	            return programaReservaVagas;
	    }

	    return programaReservaVagas;
	}

	public String getFinanciamentoEstudantil(List<String> listaFinanciamentoEstudantilVOs) {
		String financiamentoEstudantil = "1|FI|GE|GM|IE|EE|PI|PP|ER|EN|MN|IN";
		for (String financiamentoEstudantilBase : listaFinanciamentoEstudantilVOs) {

			FinanciamentoEstudantil finan = FinanciamentoEstudantil.getEnum(financiamentoEstudantilBase);
			if (finan == null) {
				return financiamentoEstudantil;
			}
			switch (finan) {
			case FIES:
//				financiamentoEstudantil = "1|1|0|0|0|0|0|0|0|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("FI", "1");
				break;
			case GOVERNO_ESTADUAL:
//				financiamentoEstudantil = "1|0|1|0|0|0|0|0|0|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("GE", "1");
				break;
			case GOVERNO_MUNICIPAL:
//				financiamentoEstudantil = "1|0|0|1|0|0|0|0|0|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("GM", "1");
				break;
			case IES:
//				financiamentoEstudantil = "1|0|0|0|1|0|0|0|0|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("IE", "1");
				break;
			case ENTIDADES_EXTERNAS:
//				financiamentoEstudantil = "1|0|0|0|0|1|0|0|0|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("EE", "1");
				break;
			case PROUNI_INTEGRAL:
//				financiamentoEstudantil = "1|0|0|0|0|0|0|1|0|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("PI", "1");
				break;
			case PROUNI_PARCIAL:
//				financiamentoEstudantil = "1|0|0|0|0|0|0|0|1|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("PP", "1");
				break;
			case ENTIDADES_EXTERNAS_NAO_REEMBOLSAVEL:
//				financiamentoEstudantil = "1|0|0|0|0|0|0|0|0|1|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("ER", "1");
				break;
			case GOVERNO_ESTADUAL_NAO_REEMBOLSAVEL:
//				financiamentoEstudantil = "1|0|0|0|0|0|0|0|0|0|1|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("EN", "1");
				break;
			case GOVERNO_MUNICIPAL_NAO_REEMBOLSAVEL:
//				financiamentoEstudantil = "1|0|0|0|0|0|0|0|0|0|0|1";
				financiamentoEstudantil = financiamentoEstudantil.replace("MN", "1");
				break;
			case IES_NAO_REEMBOLSAVEL:
//				financiamentoEstudantil = "1|0|0|0|0|0|0|0|0|0|0|0";
				financiamentoEstudantil = financiamentoEstudantil.replace("IN", "1");
				break;
			}
		}
		
		if (listaFinanciamentoEstudantilVOs.isEmpty()) {
			financiamentoEstudantil = "0|||||||||||";
		} else {
			financiamentoEstudantil = financiamentoEstudantil.replace("FI", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("GE", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("GM", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("IE", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("EE", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("PI", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("PP", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("ER", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("EN", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("MN", "0");
			financiamentoEstudantil = financiamentoEstudantil.replace("IN", "0");
		}
		return financiamentoEstudantil;
	}

	public String getApoioSocial(String apoioSocialBase) {
		String apoio = "0||||||";
		ApoioSocial apoioSocial = ApoioSocial.getEnum(apoioSocialBase);
		if (apoioSocial == null) {
			return apoio;
		}
		switch (apoioSocial) {
		case ALIMENTACAO:
			apoio = "1|1|0|0|0|0|0";
			break;
		case MORADIA:
			apoio = "1|0|1|0|0|0|0";
			break;
		case TRANSPORTE:
			apoio = "1|0|0|1|0|0|0";
			break;
		case MATERIAL_DIDATICO:
			apoio = "1|0|0|0|1|0|0";
			break;
		case BOLSA_TRABALHO:
			apoio = "1|0|0|0|0|1|0";
			break;
		case BOLSA_PERMANENCIA:
			apoio = "1|0|0|0|0|0|1";
			break;
		}
		return apoio;
	}

	public String getAtividadeFormacaoComplementar(String atividadeFormacaoComplementarBase) {
		String atividade = "0||||||||";
		AtividadeFormacaoComplementar formacao = AtividadeFormacaoComplementar.getEnum(atividadeFormacaoComplementarBase);
		if (formacao == null) {
			return atividade;
		}
		switch (formacao) {
		case PESQUISA:
			atividade = "1|1|0|0|0|0|0|0|0";
			break;
		case PESQUISA_REMUNERADA:
			atividade = "1|0|1|0|0|0|0|0|0";
			break;
		case EXTENSAO:
			atividade = "1|0|0|1|0|0|0|0|0";
			break;
		case EXTENSAO_REMUNERADA:
			atividade = "1|0|0|0|1|0|0|0|0";
			break;
		case MONITORIA:
			atividade = "1|0|0|0|0|1|0|0|0";
			break;
		case MONITORIA_REMUNERADA:
			atividade = "1|0|0|0|0|0|1|0|0";
			break;
		case ESTAGIO_NAO_OBRIGATORIO:
			atividade = "1|0|0|0|0|0|0|1|0";
			break;
		case ESTAGIO_NAO_OBRIGATORIO_REMUNERADO:
			atividade = "1|0|0|0|0|0|0|0|1";
			break;
		}
		return atividade;
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da
	 * classe <code>CensoAlunoItemVO</code>.
	 */
	// public static void validarUnicidade(List<CensoAlunoItemVO> lista,
	// CensoAlunoItemVO obj) throws ConsistirException {
	// for (CensoAlunoItemVO repetido : lista) {
	// if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
	// throw new ConsistirException("O campo CODIGO já esta cadastrado!");
	// }
	// }
	// }
	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>CensoAlunoItemVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(AlunoCensoVO alunoCenso) throws ConsistirException {
		if (!alunoCenso.isValidarDados().booleanValue()) {
			return;
		}
		if ((alunoCenso.getMatriculaPeriodo().getMatricula() == null) || (alunoCenso.getMatriculaPeriodo().getMatricula().equals(""))) {
			throw new ConsistirException("O campo MATRICULA (AlunoCenso) deve ser informado.");
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
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>CensoAlunoItem</code>).
	 */
	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return (matriculaPeriodo);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>CensoAlunoItem</code>).
	 */
	public void setMatriculaPeriodo(MatriculaPeriodoVO obj) {
		this.matriculaPeriodo = obj;
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

	public String getAlunoComDeficiencia() {
		if (alunoComDeficiencia == null) {
			alunoComDeficiencia = "0";
		}
		return alunoComDeficiencia;
	}

	public void setAlunoComDeficiencia(String alunoComDeficiencia) {
		this.alunoComDeficiencia = alunoComDeficiencia;
	}

	public String getAuditiva() {
		if (auditiva == null) {
			auditiva = "";
		}
		return auditiva;
	}

	public void setAuditiva(String auditiva) {
		this.auditiva = auditiva;
	}

	public String getBaixaVisao() {
		if (baixaVisao == null) {
			baixaVisao = "";
		}
		return baixaVisao;
	}

	public void setBaixaVisao(String baixaVisao) {
		this.baixaVisao = baixaVisao;
	}

	public String getCegueira() {
		if (cegueira == null) {
			cegueira = "";
		}
		return cegueira;
	}

	public void setCegueira(String cegueira) {
		this.cegueira = cegueira;
	}

	public String getCorRaca() {
		if (corRaca == null) {
			corRaca = "0";
		}
		return corRaca;
	}

	public void setCorRaca(String corRaca) {
		this.corRaca = corRaca;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getDataNasc() {
		if (dataNasc == null) {
			dataNasc = "";
		}
		return dataNasc;
	}

	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}

	public String getFisica() {
		if (fisica == null) {
			fisica = "";
		}
		return fisica;
	}

	public void setFisica(String fisica) {
		this.fisica = fisica;
	}

	public String getIdAlunoIES() {
		if (idAlunoIES == null) {
			idAlunoIES = "";
		}
		return idAlunoIES;
	}

	public void setIdAlunoIES(String idAlunoIES) {
		this.idAlunoIES = idAlunoIES;
	}

	public String getIdAlunoINEP() {
		if (idAlunoINEP == null) {
			idAlunoINEP = "";
		}
		return idAlunoINEP;
	}

	public void setIdAlunoINEP(String idAlunoINEP) {
		this.idAlunoINEP = idAlunoINEP;
	}

	public List<CursoCensoVO> getListaCursoCenso() {
		if (listaCursoCenso == null) {
			listaCursoCenso = new ArrayList<CursoCensoVO>(0);
		}
		return listaCursoCenso;
	}

	public void setListaCursoCenso(List<CursoCensoVO> listaCursoCenso) {
		this.listaCursoCenso = listaCursoCenso;
	}

	public String getMental() {
		if (mental == null) {
			mental = "";
		}
		return mental;
	}

	public void setMental(String mental) {
		this.mental = mental;
	}

	public String getMultipla() {
		if (multipla == null) {
			multipla = "";
		}
		return multipla;
	}

	public void setMultipla(String multipla) {
		this.multipla = multipla;
	}

	public String getNomeNaturalidade() {
		if (municipioDeNascimento == null) {
			municipioDeNascimento = "";
		}
		return municipioDeNascimento;
	}

	public String getMunicipioDeNascimento() {
		if (municipioDeNascimento == null) {
			municipioDeNascimento = "";
		}
		return municipioDeNascimento;
	}

	public void setMunicipioDeNascimento(String municipioDeNascimento) {
		this.municipioDeNascimento = municipioDeNascimento;
	}

	public String getNacionalidade() {
		if (nacionalidade == null) {
			nacionalidade = "";
		}
		return nacionalidade;
	}

	public String getNomeNacionalidade() {
		if (nacionalidade == null || nacionalidade.equals("1")) {
			return "Brasileira";
		}
		return "Estrangeira";
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getNome() {
		if (nome == null) {
			nome = "NAO FORNECIDO";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeFiliador() {
		if (nomeFiliador == null) {
			nomeFiliador = "NAO FORNECIDO";
		}
		return nomeFiliador;
	}

	public void setNomeFiliador(String nomeFiliador) {
		this.nomeFiliador = nomeFiliador;
	}

	public String getPaisOrigem() {
		if (paisOrigem == null) {
			if (getNacionalidade().equals("1")) {
				paisOrigem = "BRA";
			} else {
				paisOrigem = "";
			}
		} else if (paisOrigem.equals("0") || paisOrigem.equals("")) {
			paisOrigem = "BRA";
		}
		return paisOrigem;
	}

	public void setPaisOrigem(String paisOrigem) {
		this.paisOrigem = paisOrigem;
	}

	public String getPassaporte() {
		if (passaporte == null) {
			passaporte = "";
		}
		return passaporte;
	}

	public void setPassaporte(String passaporte) {
		this.passaporte = passaporte;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSurdez() {
		if (surdez == null) {
			surdez = "";
		}
		return surdez;
	}

	public void setSurdez(String surdez) {
		this.surdez = surdez;
	}

	public String getSurdocegueira() {
		if (surdocegueira == null) {
			surdocegueira = "";
		}
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

	public String getUfDeNascimento() {
		if (ufDeNascimento == null) {
			ufDeNascimento = "";
		}
		return ufDeNascimento;
	}

	public void setUfDeNascimento(String ufDeNascimento) {
		this.ufDeNascimento = ufDeNascimento;
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

	public String getCodigoAluno() {
		return codigoAluno;
	}

	public void setCodigoAluno(String codigoAluno) {
		this.codigoAluno = codigoAluno;
	}

	public String getAltasHabilidadesSuperdotacao() {
		if (altasHabilidadesSuperdotacao == null) {
			altasHabilidadesSuperdotacao = "";
		}
		return altasHabilidadesSuperdotacao;
	}

	public void setAltasHabilidadesSuperdotacao(String altasHabilidadesSuperdotacao) {
		this.altasHabilidadesSuperdotacao = altasHabilidadesSuperdotacao;
	}

	public String getAutismoInfantil() {
		if (autismoInfantil == null) {
			autismoInfantil = "";
		}
		return autismoInfantil;
	}

	public void setAutismoInfantil(String autismoInfantil) {
		this.autismoInfantil = autismoInfantil;
	}

	public String getSindromeAsperger() {
		if (sindromeAsperger == null) {
			sindromeAsperger = "";
		}
		return sindromeAsperger;
	}

	public void setSindromeAsperger(String sindromeAsperger) {
		this.sindromeAsperger = sindromeAsperger;
	}

	public String getSindromeRett() {
		if (sindromeRett == null) {
			sindromeRett = "";
		}
		return sindromeRett;
	}

	public void setSindromeRett(String sindromeRett) {
		this.sindromeRett = sindromeRett;
	}

	public String getTranstornoDesintegrativoInfancia() {
		if (transtornoDesintegrativoInfancia == null) {
			transtornoDesintegrativoInfancia = "";
		}
		return transtornoDesintegrativoInfancia;
	}

	public void setTranstornoDesintegrativoInfancia(String transtornoDesintegrativoInfancia) {
		this.transtornoDesintegrativoInfancia = transtornoDesintegrativoInfancia;
	}

	public String getDeficiencia() {
		if (getCegueira().equals("1")) {
			return "Cegueira";
		}
		if (getBaixaVisao().equals("1")) {
			return "Baixa Visão";
		}
		if (getSurdez().equals("1")) {
			return "Surdez";
		}
		if (getAuditiva().equals("1")) {
			return "Auditiva";
		}
		if (getSurdocegueira().equals("1")) {
			return "Surdez e Cegueira";
		}
		if (getMultipla().equals("1")) {
			return "Multipla";
		}
		if (getMental().equals("1")) {
			return "Mental";
		}
		if (getAutismoInfantil().equals("1")) {
			return "Autismo Infantil";
		}
		if (getSindromeAsperger().equals("1")) {
			return "Sindrome Asperge";
		}
		if (getSindromeRett().equals("1")) {
			return "Sindrome Rett";
		}
		if (getTranstornoDesintegrativoInfancia().equals("1")) {
			return "Transtorno Desintegrativo Infancia";
		}
		if (getAltasHabilidadesSuperdotacao().equals("1")) {
			return "Altas Habilidades Superdotacao";
		}
		if (getBaixaVisaoOuVisaoMonocular().equals("1")) {
			return "Visão monocular";
		}
		return "";
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

	public String getLocalizacaoZonaResidencia() {
		return localizacaoZonaResidencia;
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

	public String getEscolarizacaoOutroEspaco() {
		return escolarizacaoOutroEspaco;
	}

	public String getTransportePublico() {
		return transportePublico;
	}

	public void setLocalizacaoZonaResidencia(String localizacaoZonaResidencia) {
		this.localizacaoZonaResidencia = localizacaoZonaResidencia;
	}

	public void setEscolarizacaoOutroEspaco(String escolarizacaoOutroEspaco) {
		this.escolarizacaoOutroEspaco = escolarizacaoOutroEspaco;
	}

	public void setTransportePublico(String transportePublico) {
		this.transportePublico = transportePublico;
	}
	
	public int getSexoCensoTecnico() {
    	if (sexo != null && sexo.equals("F")) {
    		return 2;
    	}
    	return 1;
    }

	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getEtapaEnsino() {
		return etapaEnsino;
	}
	
	public void setEtapaEnsino(String etapaEnsino) {
		this.etapaEnsino = etapaEnsino;
	}

	public String getNivelEducacional() {
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public String getTipoEscolaConcluiuEnsinoMedio() {
		if(tipoEscolaConcluiuEnsinoMedio == null) {
			tipoEscolaConcluiuEnsinoMedio = "";
		}
		return tipoEscolaConcluiuEnsinoMedio;
	}

	public void setTipoEscolaConcluiuEnsinoMedio(String tipoEscolaConcluiuEnsinoMedio) {
		this.tipoEscolaConcluiuEnsinoMedio = tipoEscolaConcluiuEnsinoMedio;
	}	
	
	public Integer getCodigoIbgeEstado() {
		if (codigoIbgeEstado == null) {
			codigoIbgeEstado = 0;
		}
		return codigoIbgeEstado;
	}

	public void setCodigoIbgeEstado(Integer codigoIbgeEstado) {
		this.codigoIbgeEstado = codigoIbgeEstado;
	}

	public String getBaixaVisaoOuVisaoMonocular() {
		if (baixaVisaoOuVisaoMonocular == null) {
			baixaVisaoOuVisaoMonocular = "";
		}
		return baixaVisaoOuVisaoMonocular;
	}

	public void setBaixaVisaoOuVisaoMonocular(String baixaVisaoOuVisaoMonocular) {
		this.baixaVisaoOuVisaoMonocular = baixaVisaoOuVisaoMonocular;
	}

}
