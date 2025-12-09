package negocio.comuns.basico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DisciplinasInteresseVO;
import negocio.comuns.academico.DisponibilidadeHorarioVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.QuadroHorarioVO;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.administrativo.ConfiguracaoAtualizacaoCadastralVO;
import negocio.comuns.administrativo.ConfiguracaoCandidatoProcessoSeletivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.FormacaoExtraCurricularVO;
import negocio.comuns.administrativo.PessoaPreInscricaoCursoVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.JsonDateDeserializer;
import negocio.comuns.arquitetura.JsonDateSerializer;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.bancocurriculum.CurriculumPessoaVO;
import negocio.comuns.basico.enumeradores.ModalidadeBolsaEnum;
import negocio.comuns.basico.enumeradores.SituacaoMilitarEnum;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.dominios.CorRaca;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.comuns.utilitarias.dominios.TipoDeficiencia;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.AtributoComparacao;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import webservice.DateAdapterMobile;
import webservice.servicos.IntegracaoFiliacaoVO;
import webservice.servicos.IntegracaoFormacaoAcademicaVO;
import webservice.servicos.IntegracaoPessoaVO;

/**
 * Reponsável por manter os dados da entidade Pessoa. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
//@Entity
//@Table(name = "Pessoa")
//@Analyzer
@XmlRootElement(name = "pessoa")
public class PessoaVO extends SuperVO implements PossuiEndereco, Cloneable {

//    @Id
//    @DocumentId
//    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
	private Integer codigo;
	private String nome;
	private String endereco;
	private String setor;
	private String numero;
	private String CEP;
	private String complemento;
	private String sexo;
	private String estadoCivil;
	private String telefoneComer;
	private String telefoneRes;
	private String telefoneRecado;
	private String celular;
	private String email;
	private String email2;
	private String paginaPessoal;

	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dataNasc;
	private String CPF;
	private String RG;
	private String certificadoMilitar;
	private String pispasep;

	@JsonDeserialize(using = JsonDateDeserializer.class)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dataEmissaoRG;
	private String estadoEmissaoRG;
	private String orgaoEmissor;
	private String tituloEleitoral;
	private String necessidadesEspeciais;
	private String tipoNecessidadesEspeciais;
	private Boolean funcionario;
	private Boolean professor;
	private Boolean gerenciaPreInscricao;
	private Boolean aluno;
	private Boolean requisitante;
	private Boolean candidato;
	private Boolean membroComunidade;
	private Boolean sabatista;
	private String atuaComoDocente;
	private Boolean gravida;
	private Boolean canhoto;
	private Boolean portadorNecessidadeEspecial;
	// / CAMPOS DEVERAM SER UTILIZADO APENAS NO RELATÓRIOS///
	private String nomeEmpresa;
	private String enderecoEmpresa;
	private String cargoPessoaEmpresa;
	private String cepEmpresa;
	private String complementoEmpresa;
	private String setorEmpresa;
	private String certidaoNascimento;
	private Boolean isentarTaxaBoleto;
	private boolean permiteEnviarRemessa = false;
	private String secaoZonaEleitoral;
//    @Transient
	private CidadeVO cidadeEmpresa;
	// /////////////// TERMINA CAMPOS DO RELATORIO AQUI /////////////////
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>FormacaoAcademica</code>.
	 */
//    @Transient
	private List<PessoaPreInscricaoCursoVO> pessoaPreInscricaoCursoVOs;
//    @Transient
	private List<FormacaoAcademicaVO> formacaoAcademicaVOs;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>DisciplinasInteresse</code>.
	 */
//    @Transient
	private List disciplinasInteresseVOs;
//    @Transient
	private List quadroHorarioVOs;
//    @Transient
	private List<HorarioProfessorVO> horarioProfessorVOs;
//    @Transient
	private List documetacaoPessoaVOs;
	/**
	 * Atributo responsável por manter os objetos da classe <code>Filiacao</code>.
	 */
//    @Transient
	private List<FiliacaoVO> filiacaoVOs;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>DisponibilidadeHorario</code>.
	 */
//    @Transient
	private List disponibilidadeHorarioVOs;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Cidade </code>.
	 */
//    @Transient
	private CidadeVO cidade;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Cidade </code>.
	 */
//    @Transient
	private CidadeVO naturalidade;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Paiz </code>.
	 */
//    @Transient
	private PaizVO nacionalidade;
	// private String cssPadrao;
	private String valorCssTopoLogo;
	private String valorCssBackground;
	private String valorCssMenu;
	// private byte foto[];
//    @Transient
	private PerfilEconomicoVO perfilEconomico;
	private Boolean ativo;
	private String idAlunoInep;
	private String corRaca;
	private String deficiencia;
	private String passaporte;
//    @Transient
	private Boolean selecionado;
	private String nomeFiador;
	private String enderecoFiador;
	private String telefoneFiador;
	private String celularFiador;
	private String cpfFiador;
	private String cepFiador;
	private String setorFiador;
	private String numeroEndFiador;
	private String complementoFiador;
	private String estadoCivilFiador;
	private String rgFiador;
	private String profissaoFiador;
	private Date dataNascimentoFiador;
	private PaizVO paisFiador;
//    @Transient
	private CidadeVO cidadeFiador;
//    @Transient
	private ArquivoVO arquivoImagem;
//    @Transient
	private UsuarioVO reponsavelUltimaAlteracao;
//    @Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimaAlteracao;
	private Boolean coordenador;
	// Atributo para controle na tel de recados
	private Boolean enviarComunicadoPessoa;
	/**
	 * Atributo responsável por manter os objetos da classe
	 * <code>DadosComerciaisVO</code>.
	 */
//    @Transient
	private List<DadosComerciaisVO> dadosComerciaisVOs;
//    @Transient
	private List<FormacaoExtraCurricularVO> formacaoExtraCurricularVOs;
//    @Transient
	private List<AreaProfissionalInteresseContratacaoVO> areaProfissionalInteresseContratacaoVOs;
	private Boolean ingles;
	private Boolean espanhol;
	private Boolean frances;
	private String inglesNivel;
	private String espanholNivel;
	private String francesNivel;
	private String outrosIdiomas;
	private String outrosIdiomasNivel;
	private Boolean windows;
	private Boolean word;
	private Boolean excel;
	private Boolean access;
	private Boolean powerPoint;
	private Boolean internet;
	private Boolean sap;
	private Boolean corelDraw;
	private Boolean autoCad;
	private Boolean photoshop;
	private Boolean microsiga;
	private String outrosSoftwares;
	private Integer qtdFilhos;
	private Boolean possuiFilho;
	private Boolean participaBancoCurriculum;
	private Boolean informacoesVerdadeiras;
	private Boolean divulgarMeusDados;
	private String informacoesAdicionais;
	private Boolean curriculoAtualizado;
//    @Transient
	private Boolean gerarNumeroCPF;
	private Integer codProspect;
	private Integer qtdVagasCandidatou;
	private Boolean possuiAcessoVisaoPais;
	private Boolean ocultarDadosCRM;
//    @Transient
	private List<CurriculumPessoaVO> curriculumPessoaVOs;
//    @Transient
	private List<CurriculumPessoaVO> curriculumPessoaExcluir;

	private Date dataExpedicaoTituloEleitoral;
	private String zonaEleitoral;
	private Date dataExpedicaoCertificadoMilitar;
	private String orgaoExpedidorCertificadoMilitar;
	private SituacaoMilitarEnum situacaoMilitar;
	private Boolean dadosPessoaisAtualizado;
	private Boolean renovacaoAutomatica;
//  @TRansient
	private Boolean incluirProspect;

	// Transiente
	private String urlFotoAluno;
	private Boolean gerarProspectInativo;

	private String nomeCenso;
	private Date dataUltimaAtualizacaoCadatral;
	private Boolean registrarLogAtualizacaoCadastral;
	private String grauParentesco;
	private TipoAssinaturaDocumentoEnum tipoAssinaturaDocumentoEnum;
	private String senhaCertificadoParaDocumento;

	private TipoMidiaCaptacaoVO tipoMidiaCaptacao;

	// Atributo Transient
	private List<PessoaGsuiteVO> listaPessoaGsuite;
	private List<PessoaEmailInstitucionalVO> listaPessoaEmailInstitucionalVO;

	private String nomeBatismo;
	private String registroAcademico;

	// Atributo Transient
	private Integer quantidadeContaReceberPessoaComoAluno;
	private Integer quantidadeContaReceberPessoaComoResponsavelFinanceiro;

	// Atributo Transient
	private Integer codigoCurso1DadosUnicidadeCandidatoCurso;
	private Integer codigoCurso2DadosUnicidadeCandidatoCurso;
	private Integer codigoCurso3DadosUnicidadeCandidatoCurso;

	/*
	 * Colunas Dados Bancarios Aluno
	 * 
	 */

	private String banco;
	private String agencia;
	private String contaCorrente;
	private String universidadeParceira;
	private ModalidadeBolsaEnum modalidadeBolsa;
	private Double valorBolsa;

	private String dataEmissaoRGAnonimizada;
	private Boolean tempoEstendidoProva;
	private String transtornosNeurodivergentes;
	
	public static final long serialVersionUID = 1L;

	public Date getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null) {
			dataUltimaAlteracao = new Date();
		}
		return dataUltimaAlteracao;
	}

	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public UsuarioVO getReponsavelUltimaAlteracao() {
		if (reponsavelUltimaAlteracao == null) {
			reponsavelUltimaAlteracao = new UsuarioVO();
		}
		return reponsavelUltimaAlteracao;
	}

	public void setReponsavelUltimaAlteracao(UsuarioVO reponsavelUltimaAlteracao) {
		this.reponsavelUltimaAlteracao = reponsavelUltimaAlteracao;
	}

	/**
	 * Construtor padrão da classe <code>Pessoa</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public PessoaVO() {
		super();
	}

	public static void validarDadosFiliacaoResponsavelFinanceiro(PessoaVO obj, FiliacaoVO filiacao)
			throws ConsistirException {
		if (!obj.getFiliacaoVOs().isEmpty()) {
			Iterator i = obj.getFiliacaoVOs().iterator();
			while (i.hasNext()) {
				FiliacaoVO objExistente = (FiliacaoVO) i.next();
				if (!objExistente.getNome().equals(filiacao.getNome()) && objExistente.getResponsavelFinanceiro()) {
					filiacao.setResponsavelFinanceiro(false);
					throw new ConsistirException("Já existe um Responsável Financeiro para este Aluno!");
				}
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.NESTED)
	public static void validarDados(PessoaVO obj, Boolean validaCpf, Boolean validaCamposEnadeCenso,
			Boolean validaEndereco) throws ConsistirException {
		if (obj.getNome().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo NOME (Dados Pessoais) deve ser informado.");
		}
		if ((obj.getAluno().booleanValue() || obj.getCandidato().booleanValue()) && obj.getDataNasc() == null) {
			throw new ConsistirException("O campo DATA NASCIMENTO (Dados Pessoais) deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(obj.getDataNasc())
				&& (String.valueOf(Uteis.getAnoData(obj.getDataNasc())).length() != 4)) {
			throw new ConsistirException("O campo DATA NASCIMENTO (Dados Pessoais) foi informado com valor errado.");
		}
		if (Uteis.isAtributoPreenchido(obj.getDataNasc()) && obj.getDataNasc().after(new Date())) {
			throw new ConsistirException(obj.getGrauParentesco().isEmpty()
					? "O campo DATA NASCIMENTO (Dados Pessoais) informado é inválido. Data futura."
					: "O campo DATA NASCIMENTO (Dados Filiação) informado é inválido. Data futura.");
		}
		if (obj.getPossuiFilho()) {
			if (obj.getQtdFilhos().equals(0)) {
				throw new ConsistirException("O campo FILHOS (Dados Pessoais) deve ser informado.");
			}
		}
		if (obj.getGerarNumeroCPF()) {
			Double cpf = Math.ceil(Math.random() * Math.pow(10, 7));
			obj.setCPF(cpf.toString());
		}

		if (obj.getCPF().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo CPF (Dados Funcionais)  deve ser informado.");
		} else {
			if (Uteis.isAtributoPreenchido(validaCpf) && validaCpf && !obj.getCPF().contains("T")
					&& ((obj.getDataNasc() != null && obj.getIdade() > 18) || obj.getGerarNumeroCPF() == null)) {
				if (!Uteis.verificaCPF(obj.getCPF())) {
					throw new ConsistirException("O CPF não é VÁLIDO.");
				}
			}
		}
		if (obj.getIngles()) {
			if (obj.getInglesNivel() == null || obj.getInglesNivel().trim().equals(Constantes.EMPTY)) {
				throw new ConsistirException("O campo NÍVEL - IDIOMA INGLÊS (etapa 2) deve ser informado.");
			}
		} else {
			obj.setInglesNivel(Constantes.EMPTY);
		}
		if (obj.getFrances()) {
			if (obj.getFrancesNivel() == null || obj.getFrancesNivel().trim().equals(Constantes.EMPTY)) {
				throw new ConsistirException("O campo NÍVEL - IDIOMA FRANCÊS (etapa 2) deve ser informado.");
			}
		} else {
			obj.setFrancesNivel(Constantes.EMPTY);
		}
		if (obj.getEspanhol()) {
			if (obj.getEspanholNivel() == null || obj.getEspanholNivel().trim().equals(Constantes.EMPTY)) {
				throw new ConsistirException("O campo NÍVEL - IDIOMA ESPANHOL (etapa 2) deve ser informado.");
			}
		} else {
			obj.setEspanholNivel(Constantes.EMPTY);
		}
		if (obj.getTelefoneComer().length() > 15) {
			throw new ConsistirException("O campo TEL.COMERCIAL não pode ter mais do que 15 caracteres.");
		}
		if (obj.getTelefoneRes().length() > 15) {
			throw new ConsistirException("O campo TEL.RESIDENCIAL não pode ter mais do que 15 caracteres.");
		}
		if (obj.getTelefoneRecado().length() > 15) {
			throw new ConsistirException("O campo TEL.RECADO não pode ter mais do que 15 caracteres.");
		}

		if (validaCamposEnadeCenso && obj.getRG().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo RG (Documentos Pessoais)  deve ser informado.");
		}

		if (validaCamposEnadeCenso && obj.getOrgaoEmissor().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo Orgão Emissor (Documentos Pessoais)  deve ser informado.");
		}

		if ((validaCamposEnadeCenso || validaEndereco) && obj.getEndereco().trim().equals(Constantes.EMPTY) && (obj.getAluno()
				|| obj.getFuncionario() || obj.getProfessor() || obj.getCoordenador() || obj.getCandidato())) {
			throw new ConsistirException("O campo Endereço (Dados Pessoais)  deve ser informado.");
		}

		if ((validaCamposEnadeCenso || validaEndereco) && obj.getSetor().trim().equals(Constantes.EMPTY) && (obj.getAluno()
				|| obj.getFuncionario() || obj.getProfessor() || obj.getCoordenador() || obj.getCandidato())) {
			throw new ConsistirException("O campo Bairro/Setor (Dados Pessoais)  deve ser informado.");
		}

		if ((validaCamposEnadeCenso || validaEndereco) && obj.getCEP().trim().equals(Constantes.EMPTY) && (obj.getAluno()
				|| obj.getFuncionario() || obj.getProfessor() || obj.getCoordenador() || obj.getCandidato())) {
			throw new ConsistirException("O campo CEP (Dados Pessoais)  deve ser informado.");
		}

		if ((validaCamposEnadeCenso || validaEndereco) && obj.getCidade().getNome().trim().equals(Constantes.EMPTY) && (obj.getAluno()
				|| obj.getFuncionario() || obj.getProfessor() || obj.getCoordenador() || obj.getCandidato())) {
			throw new ConsistirException("O campo Cidade (Dados Pessoais)  deve ser informado.");
		}

//        if((validaCamposEnadeCenso || validaEndereco) && obj.getEmail().trim().equals(Constantes.EMPTY)) {
//            throw new ConsistirException("O campo Email (Dados Pessoais)  deve ser informado.");
//        } 

		if ((validaCamposEnadeCenso || validaEndereco) && obj.getTelefoneComer().trim().equals(Constantes.EMPTY)
				&& obj.getTelefoneRes().trim().equals(Constantes.EMPTY) && obj.getTelefoneRecado().trim().equals(Constantes.EMPTY)
				&& obj.getCelular().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("Ao menos um telefone (Dados Pessoais)  deve ser informado.");
		}

		if (validaCamposEnadeCenso && obj.getCorRaca().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo Cor/Raça (Dados Pessoais)  deve ser informado.");
		}

		if (validaCamposEnadeCenso && obj.getIdAlunoInep().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo ID Aluno Inep (Documentos Pessoais)  deve ser informado.");
		}

		if (validaCamposEnadeCenso && obj.getSexo().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo Sexo (Dados Pessoais)  deve ser informado.");
		}

		if (validaCamposEnadeCenso
				&& (obj.getNacionalidade().getCodigo() == null || obj.getNacionalidade().getCodigo() == 0)) {
			throw new ConsistirException("O campo Nacionalidade (Dados Pessoais)  deve ser informado.");
		}

		if (validaCamposEnadeCenso && obj.getNaturalidade().getNome().trim().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo Naturalidade (Dados Pessoais)  deve ser informado.");
		}
//        if (!obj.getEmail().equals(Constantes.EMPTY) && !Uteis.getValidaEmail(obj.getEmail())) {
//        	throw new ConsistirException("O campo Email (Dados Pessoais) informado é inválido.");
//        }
//        if (!obj.getEmail2().equals(Constantes.EMPTY) && !Uteis.getValidaEmail(obj.getEmail2())) {
//        	throw new ConsistirException("O campo Email 2 (Dados Pessoais) informado é inválido.");
//        }
		if (((obj.getCEP().trim().isEmpty() && validaEndereco)
				|| (!obj.getCEP().equals(Constantes.EMPTY) && !Uteis.validarMascaraCEP(obj.getCEP())))
				&& (obj.getAluno() || obj.getFuncionario() || obj.getProfessor() || obj.getCoordenador()
						|| obj.getCandidato())) {
			throw new ConsistirException("O campo CEP (Dados Pessoais) informado é inválido.");
		}
		if (Uteis.isAtributoPreenchido(obj.getDataEmissaoRG()) && obj.getDataEmissaoRG().after(new Date())) {
			throw new ConsistirException("O campo DATA EMISSÃO RG (Dados Pessoais) informado é inválido. Data futura.");
		}
		if (Uteis.isAtributoPreenchido(obj.getDataExpedicaoCertificadoMilitar())
				&& obj.getDataExpedicaoCertificadoMilitar().after(new Date())) {
			throw new ConsistirException(
					"O campo DATA EXPEDIÇÃO REGISTRO MILITAR (Dados Pessoais) informado é inválido. Data futura.");
		}
		if (Uteis.isAtributoPreenchido(obj.getDataExpedicaoTituloEleitoral())
				&& obj.getDataExpedicaoTituloEleitoral().after(new Date())) {
			throw new ConsistirException(
					"O campo DATA EXPEDIÇÃO TITULO ELEITORAL (Dados Pessoais) informado é inválido. Data futura.");
		}
		if (!obj.getTelefoneComer().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getTelefoneComer()).length() < 8) {
			throw new ConsistirException(obj.getGrauParentesco().isEmpty()
					? "O campo TEL.COMERCIAL deve conter 8 ou mais números informados."
					: "O campo TEL.COMERCIAL (Dados Filiação) deve conter 8 ou mais números informados.");
		}
		if (!obj.getTelefoneRes().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getTelefoneRes()).length() < 8) {
			throw new ConsistirException(obj.getGrauParentesco().isEmpty()
					? "O campo TEL.RESIDENCIAL deve conter 8 ou mais números informados."
					: "O campo TEL.RESIDENCIAL (Dados Filiação) deve conter 8 ou mais números informados.");
		}
		if (!obj.getTelefoneRecado().isEmpty()
				&& Uteis.removeCaractersEspeciais2(obj.getTelefoneRecado()).length() < 8) {
			throw new ConsistirException(
					obj.getGrauParentesco().isEmpty() ? "O campo TEL.RECADO deve conter 8 ou mais números informados."
							: "O campo TEL.RECADO (Dados Filiação) deve conter 8 ou mais números informados.");
		}
		if (!obj.getCelular().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getCelular()).length() < 8) {
			throw new ConsistirException(
					obj.getGrauParentesco().isEmpty() ? "O campo CELULAR deve conter 8 ou mais números informados."
							: "O campo CELULAR (Dados Filiação) deve conter 8 ou mais números informados.");
		}
		if (validaCamposEnadeCenso) {
//			PessoaVO.validarFiliacaoMaeObrigatorio(obj);
			PessoaVO.validarFormacaoEnsinoMedioObrigatorio(obj);
		}
	}

	public String validarPreenchimentoCurriculo(PessoaVO obj) {
		String msg = Constantes.EMPTY;
		String abasmsg = Constantes.EMPTY;
		if (obj.getParticipaBancoCurriculum()) {
			if (obj.getPorcentagemPreenchidaCurriculo().doubleValue() < 100.0) {
				msg += "Para obter 100% de preenchimento do seu currículo, complete os dados da(s) aba(s) a seguir: ";
				if (obj.getPorcentagemPreenchidaDadosBasico() < 25.0) {
					abasmsg += Constantes.EMPTY;
				}
				if (obj.getPorcentagemPreenchidaFormacaoAcademica() < 25.0) {
					if (abasmsg.equals(Constantes.EMPTY)) {
						abasmsg += "Formação Acadêmica";
					} else {
						abasmsg += ", Formação Acadêmica";
					}
				}
				if (obj.getPorcentagemPreenchidaDadosComerciais() < 25.0) {
					if (abasmsg.equals(Constantes.EMPTY)) {
						abasmsg += "Experiências Profissionais";
					} else {
						abasmsg += ", Experiências Profissionais";
					}
				}
				if (obj.getPorcentagemPreenchidaFormacaoExtraCurricular() < 25.0) {
					if (abasmsg.equals(Constantes.EMPTY)) {
						abasmsg += "Formação Extra Curricular";
					} else {
						abasmsg += ", Formação Extra Curricular";
					}
				}
				abasmsg += ";";
			}
		}
		return msg + abasmsg;
	}

	public String getTipoPessoa() {
		if (this.getAluno()) {
			return TipoPessoa.ALUNO.getValor();
		} else if (this.getFuncionario()) {
			return TipoPessoa.FUNCIONARIO.getValor();
		} else if (this.getProfessor()) {
			return TipoPessoa.PROFESSOR.getValor();
		} else if (this.getCandidato()) {
			return TipoPessoa.CANDIDATO.getValor();
		} else if (this.getPossuiAcessoVisaoPais()) {
			return TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor();
		} else if (this.getMembroComunidade()) {
			return TipoPessoa.MEMBRO_COMUNIDADE.getValor();
		} else if (this.getRequisitante()) {
			return TipoPessoa.REQUERENTE.getValor();
		}
		return Constantes.EMPTY;
	}

	public String getTipoPessoaApresentar() {
		StringBuilder tipoPessoa = new StringBuilder();
		if (this.getAluno()) {
			tipoPessoa.append("Alu.");
		}
		if (this.getFuncionario()) {
			tipoPessoa.append(tipoPessoa.toString().isEmpty() ? "Fun." : "/Fun.");
		}
		if (this.getProfessor()) {
			tipoPessoa.append(tipoPessoa.toString().isEmpty() ? "Prof." : "/Prof.");
		}
		if (this.getCoordenador()) {
			tipoPessoa.append(tipoPessoa.toString().isEmpty() ? "Coord." : "/Coord.");
		}
		if (this.getCandidato()) {
			tipoPessoa.append(tipoPessoa.toString().isEmpty() ? "Cand." : "/Cand.");
		}
		if (this.getPossuiAcessoVisaoPais()) {
			tipoPessoa.append(tipoPessoa.toString().isEmpty() ? "Resp. Fin." : "/Resp. Fin.");
		}
		if (this.getMembroComunidade()) {
			tipoPessoa.append(tipoPessoa.toString().isEmpty() ? "Mem. Comu." : "/Me. Comu.");
		}
		if (this.getRequisitante()) {
			tipoPessoa.append(tipoPessoa.toString().isEmpty() ? "Requi." : "/Requi.");
		}
		return tipoPessoa.toString();
	}

	public static void validarDadosFiliacao(PessoaVO pessoa) throws ConsistirException {
		Iterator i = pessoa.getFiliacaoVOs().iterator();
		while (i.hasNext()) {
			FiliacaoVO obj = (FiliacaoVO) i.next();
			if (obj.getTipo().equals("MA")) {
				return;
			}
		}
		throw new ConsistirException("Deve ser informado os dados da Mãe em Filiação.");
	}

	// public Image getVisualizarImagem() {
	// Image image = new ImageIcon(getFoto()).getImage();
	// return image;
	// }
	public Boolean getExisteImagem() {
		return !getArquivoImagem().getNome().equals(Constantes.EMPTY);
	}

	public FormacaoAcademicaVO getFormacaoAcademicaNivelMedio() {
		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			if (formacaoAcademicaVO.getEscolaridade().equals("EM")) {
				return formacaoAcademicaVO;
			}
		}
		return null;
	}

	public FormacaoAcademicaVO getFormacaoAcademicaNivelFundamental() {
		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			if (formacaoAcademicaVO.getEscolaridade().equals("EF")) {
				return formacaoAcademicaVO;
			}
		}
		return null;
	}

	public FormacaoAcademicaVO getFormacaoAcademicaNivelGraduacao() {
		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			if (formacaoAcademicaVO.getEscolaridade().equals("GR")) {
				return formacaoAcademicaVO;
			}
		}
		return null;
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>DisponibilidadeHorarioVO</code> ao List
	 * <code>disponibilidadeHorarioVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>DisponibilidadeHorario</code> - getTurno().getCodigo() - como
	 * identificador (key) do objeto no List.
	 *
	 * @param obj Objeto da classe <code>DisponibilidadeHorarioVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjDisponibilidadeHorarioVOs(DisponibilidadeHorarioVO obj) throws Exception {
		if (this.getProfessor().equals(Boolean.TRUE)) {
			int index = 0;
			Iterator i = getDisponibilidadeHorarioVOs().iterator();
			while (i.hasNext()) {
				DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
				if (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())
						&& objExistente.getNrAula().equals(obj.getNrAula())
						&& objExistente.getDiaSemana().equals(obj.getDiaSemana())) {
					getDisponibilidadeHorarioVOs().set(index, obj);
					return;
				}
				index++;
			}
			getDisponibilidadeHorarioVOs().add(obj);
		}

	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>DisponibilidadeHorarioVO</code> no List
	 * <code>disponibilidadeHorarioVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>DisponibilidadeHorario</code> - getTurno().getCodigo() - como
	 * identificador (key) do objeto no List.
	 *
	 * @param turno Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjDisponibilidadeHorarioVOs(DisponibilidadeHorarioVO obj) throws Exception {
		int index = 0;
		Iterator i = getDisponibilidadeHorarioVOs().iterator();
		while (i.hasNext()) {
			DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
			if (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())
					&& objExistente.getNrAula().equals(obj.getNrAula())
					&& objExistente.getDiaSemana().equals(obj.getDiaSemana())) {
				getDisponibilidadeHorarioVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>DisponibilidadeHorarioVO</code> no List
	 * <code>disponibilidadeHorarioVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>DisponibilidadeHorario</code> - getTurno().getCodigo() - como
	 * identificador (key) do objeto no List.
	 *
	 * @param turno Parâmetro para localizar o objeto do List.
	 */
	public DisponibilidadeHorarioVO consultarObjDisponibilidadeHorarioVO(DisponibilidadeHorarioVO obj)
			throws Exception {
		Iterator i = getDisponibilidadeHorarioVOs().iterator();
		while (i.hasNext()) {
			DisponibilidadeHorarioVO objExistente = (DisponibilidadeHorarioVO) i.next();
			if (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())
					&& objExistente.getNrAula().equals(obj.getNrAula())
					&& objExistente.getDiaSemana().equals(obj.getDiaSemana())) {
				return objExistente;
			}
		}
		return null;
	}

	public void adicionarObjHorarioProfessorVOs(HorarioProfessorVO obj) throws Exception {
		if (this.getProfessor().equals(Boolean.TRUE)) {
			int index = 0;
			Iterator i = getHorarioProfessorVOs().iterator();
			while (i.hasNext()) {
				HorarioProfessorVO objExistente = (HorarioProfessorVO) i.next();
				if (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())) {
					getHorarioProfessorVOs().set(index, obj);
					return;
				}
				index++;
			}
			getHorarioProfessorVOs().add(obj);
		}
	}

	public List<HorarioProfessorVO> montarNovaListaHorarioProfessor() throws Exception {
		Iterator i = getQuadroHorarioVOs().iterator();
		while (i.hasNext()) {
			HorarioProfessorVO horarioProfessor = new HorarioProfessorVO();
			QuadroHorarioVO obj = (QuadroHorarioVO) i.next();
			obj.atualizarValoresQuadroHorario();
			horarioProfessor.setCodigo(obj.getHorarioProfessorVO().getCodigo());
			horarioProfessor.getTurno().setCodigo(obj.getTurno().getCodigo());
			horarioProfessor.setProfessor(obj.getHorarioProfessorVO().getProfessor());
			horarioProfessor.setSegunda(obj.getDadosHorarioSegunda());
			horarioProfessor.setTerca(obj.getDadosHorarioTerca());
			horarioProfessor.setQuarta(obj.getDadosHorarioQuarta());
			horarioProfessor.setQuinta(obj.getDadosHorarioQuinta());
			horarioProfessor.setSexta(obj.getDadosHorarioSexta());
			horarioProfessor.setSabado(obj.getDadosHorarioSabado());
			horarioProfessor.setDomingo(obj.getDadosHorarioDomingo());
			adicionarObjHorarioProfessorVOs(horarioProfessor);
		}

		return getHorarioProfessorVOs();
	}

	public void montarListaHorarioProfessor() throws Exception {
		Iterator i = getQuadroHorarioVOs().iterator();
		while (i.hasNext()) {
			HorarioProfessorVO horarioProfessor = new HorarioProfessorVO();
			QuadroHorarioVO obj = (QuadroHorarioVO) i.next();
			obj.atualizarValoresQuadroHorario();
			horarioProfessor.getTurno().setCodigo(obj.getTurno().getCodigo());
			horarioProfessor.setSegunda(obj.getDadosHorarioSegunda());
			horarioProfessor.setTerca(obj.getDadosHorarioTerca());
			horarioProfessor.setQuarta(obj.getDadosHorarioQuarta());
			horarioProfessor.setQuinta(obj.getDadosHorarioQuinta());
			horarioProfessor.setSexta(obj.getDadosHorarioSexta());
			horarioProfessor.setSabado(obj.getDadosHorarioSabado());
			horarioProfessor.setDomingo(obj.getDadosHorarioDomingo());

			adicionarObjHorarioProfessorVOs(horarioProfessor);
		}

	}

	public void adicionarObjQuadroHorarioVOs(QuadroHorarioVO quadroHorario) throws Exception {
		if (this.getProfessor().equals(true)) {
			int index = 0;
			quadroHorario.atualizarValoresQuadroHorario();
			Iterator i = getQuadroHorarioVOs().iterator();
			while (i.hasNext()) {
				QuadroHorarioVO objExistente = (QuadroHorarioVO) i.next();
				if (objExistente.getTurno().getCodigo().equals(quadroHorario.getTurno().getCodigo())) {
					getQuadroHorarioVOs().set(index, quadroHorario);
					return;
				}
				index++;
			}
			getQuadroHorarioVOs().add(quadroHorario);
		}

	}

	public void excluirObjQuadroHorarioVOs(QuadroHorarioVO obj) throws Exception {
		int index = 0;
		Iterator i = getQuadroHorarioVOs().iterator();
		while (i.hasNext()) {
			QuadroHorarioVO objExistente = (QuadroHorarioVO) i.next();
			if (objExistente.getTurno().getCodigo().equals(obj.getTurno().getCodigo())) {
				getQuadroHorarioVOs().remove(index);
				return;
			}
			index++;
		}
	}

	public QuadroHorarioVO consultarObjQuadroHorarioVO(Integer codigoTurno) throws Exception {
		Iterator i = getQuadroHorarioVOs().iterator();
		while (i.hasNext()) {
			QuadroHorarioVO objExistente = (QuadroHorarioVO) i.next();
			if (objExistente.getTurno().getCodigo().equals(codigoTurno)) {
				return objExistente;
			}
		}
		return new QuadroHorarioVO();
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>FiliacaoVO</code> ao List <code>filiacaoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe <code>Filiacao</code> - getNome() - como
	 * identificador (key) do objeto no List.
	 *
	 * @param obj Objeto da classe <code>FiliacaoVO</code> que será adiocionado ao
	 *            Hashtable correspondente.
	 */
	public void adicionarObjFiliacaoVOs(FiliacaoVO obj) throws Exception {
		verificarCpfFiliacaoAntesAdicionar(obj);
		FiliacaoVO.validarDados(obj, false);
		Uteis.checkState(obj.getCPF().equals(getCPF()),
				"Não é possível adicionar essa filiação, pois o CPF do aluno não pode ser igual ao dos responsáveis.");
		if (obj.getTipo().equals("PA")) {
			obj.getPais().setGrauParentesco("Pai");
		} else if (obj.getTipo().equals("MA")) {
			obj.getPais().setGrauParentesco("Mãe");
		}
		int index = 0;
		Iterator<FiliacaoVO> i = getFiliacaoVOs().iterator();
		while (i.hasNext()) {
			FiliacaoVO objExistente = (FiliacaoVO) i.next();
			if (objExistente.getTipo().equals(obj.getTipo())
					&& (objExistente.getTipo().equals("MA") || objExistente.getTipo().equals("PA"))
					&& (obj.getTipo().equals("MA") || obj.getTipo().equals("PA"))) {
				getFiliacaoVOs().set(index, obj);
				return;
			} else if (objExistente.getNome().equals(obj.getNome())) {
				getFiliacaoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getFiliacaoVOs().add(obj);
	}

	private void verificarCpfFiliacaoAntesAdicionar(FiliacaoVO obj) {
		if (obj.getCPF().equals(Constantes.EMPTY) && !obj.getNome().trim().isEmpty()) {
			obj.setCPF(Uteis.gerarCPFValido());
			obj.setCPF(Uteis.adicionarMascaraCPF(obj.getCPF()));
			String digitosInvalidos = obj.getCPF().substring(obj.getCPF().lastIndexOf("-"));
			Integer x = Integer.parseInt(digitosInvalidos.substring(1, 2));
			if (x > 0) {
				x = x - 1;
			} else {
				x = 9;
			}
			obj.setCPF(obj.getCPF().replace(obj.getCPF().substring(obj.getCPF().lastIndexOf("-")), "-" + x + "T"));
		}
	}

	/**
	 * Operação responsável por excluir um objeto da classe <code>FiliacaoVO</code>
	 * no List <code>filiacaoVOs</code>. Utiliza o atributo padrão de consulta da
	 * classe <code>Filiacao</code> - getNome() - como identificador (key) do objeto
	 * no List.
	 *
	 * @param nome Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjFiliacaoVOs(String nome) throws Exception {
		int index = 0;
		Iterator i = getFiliacaoVOs().iterator();
		while (i.hasNext()) {
			FiliacaoVO objExistente = (FiliacaoVO) i.next();
			if (objExistente.getNome().equals(nome)) {
				getFiliacaoVOs().remove(index);
				return;
			}
			index++;
		}
		// excluirObjSubordinadoOC
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>FiliacaoVO</code> no List <code>filiacaoVOs</code>. Utiliza o atributo
	 * padrão de consulta da classe <code>Filiacao</code> - getNome() - como
	 * identificador (key) do objeto no List.
	 *
	 * @param nome Parâmetro para localizar o objeto do List.
	 */
	public FiliacaoVO consultarObjFiliacaoVO(String nome) throws Exception {
		Iterator i = getFiliacaoVOs().iterator();
		while (i.hasNext()) {
			FiliacaoVO objExistente = (FiliacaoVO) i.next();
			if (objExistente.getNome().equals(nome)) {
				return objExistente;
			}
		}
		return null;
		// consultarObjSubordinadoOC
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>DisciplinasInteresseVO</code> ao List
	 * <code>disciplinasInteresseVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>DisciplinasInteresse</code> - getDisciplina().getCodigo() -
	 * como identificador (key) do objeto no List.
	 *
	 * @param obj Objeto da classe <code>DisciplinasInteresseVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjDisciplinasInteresseVOs(DisciplinasInteresseVO obj) throws Exception {
		DisciplinasInteresseVO.validarDados(obj);
		int index = 0;
		Iterator i = getDisciplinasInteresseVOs().iterator();
		while (i.hasNext()) {
			DisciplinasInteresseVO objExistente = (DisciplinasInteresseVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
				getDisciplinasInteresseVOs().set(index, obj);
				return;
			}
			index++;
		}
		getDisciplinasInteresseVOs().add(obj);
		// adicionarObjSubordinadoOC
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>DisciplinasInteresseVO</code> no List
	 * <code>disciplinasInteresseVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>DisciplinasInteresse</code> - getDisciplina().getCodigo() -
	 * como identificador (key) do objeto no List.
	 *
	 * @param disciplina Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjDisciplinasInteresseVOs(Integer disciplina) throws Exception {
		int index = 0;
		Iterator i = getDisciplinasInteresseVOs().iterator();
		while (i.hasNext()) {
			DisciplinasInteresseVO objExistente = (DisciplinasInteresseVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
				getDisciplinasInteresseVOs().remove(index);
				return;
			}
			index++;
		}
		// excluirObjSubordinadoOC
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>DisciplinasInteresseVO</code> no List
	 * <code>disciplinasInteresseVOs</code>. Utiliza o atributo padrão de consulta
	 * da classe <code>DisciplinasInteresse</code> - getDisciplina().getCodigo() -
	 * como identificador (key) do objeto no List.
	 *
	 * @param disciplina Parâmetro para localizar o objeto do List.
	 */
	public DisciplinasInteresseVO consultarObjDisciplinasInteresseVO(Integer disciplina) throws Exception {
		Iterator i = getDisciplinasInteresseVOs().iterator();
		while (i.hasNext()) {
			DisciplinasInteresseVO objExistente = (DisciplinasInteresseVO) i.next();
			if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
				return objExistente;
			}
		}
		return null;
		// consultarObjSubordinadoOC
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>FormacaoAcademicaVO</code> ao List <code>formacaoAcademicaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do
	 * objeto no List.
	 *
	 * @param obj Objeto da classe <code>FormacaoAcademicaVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjFormacaoAcademicaVOs(FormacaoAcademicaVO obj) throws Exception {
		FormacaoAcademicaVO.validarDados(obj);
		UtilReflexao.adicionarObjetoLista(getFormacaoAcademicaVOs(), obj, new AtributoComparacao().add("curso", obj.getCurso()).add("escolaridade", obj.getEscolaridade()));
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>FormacaoAcademicaVO</code> no List <code>formacaoAcademicaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do
	 * objeto no List.
	 *
	 * @param curso Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjFormacaoAcademicaVOs(String curso) throws Exception {
		int index = 0;
		Iterator i = getFormacaoAcademicaVOs().iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO objExistente = (FormacaoAcademicaVO) i.next();
			if (objExistente.getCurso().equals(curso)) {
				getFormacaoAcademicaVOs().remove(index);
				return;
			}
			index++;
		}
		// excluirObjSubordinadoOC
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>FormacaoAcademicaVO</code> no List <code>formacaoAcademicaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do
	 * objeto no List.
	 *
	 * @param curso Parâmetro para localizar o objeto do List.
	 */
	public FormacaoAcademicaVO consultarObjFormacaoAcademicaVO(String curso) throws Exception {
		Iterator i = getFormacaoAcademicaVOs().iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO objExistente = (FormacaoAcademicaVO) i.next();
			if (objExistente.getCurso().equals(curso)) {
				return objExistente;
			}
		}
		return null;
		// consultarObjSubordinadoOC
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>FormacaoAcademicaVO</code> ao List <code>formacaoAcademicaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do
	 * objeto no List.
	 *
	 * @param obj Objeto da classe <code>FormacaoAcademicaVO</code> que será
	 *            adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjPessoaPreInscricaoCursoVOs(PessoaPreInscricaoCursoVO obj) throws Exception {
		PessoaPreInscricaoCursoVO.validarDados(obj);
		int index = 0;
		Iterator i = getPessoaPreInscricaoCursoVOs().iterator();
		while (i.hasNext()) {
			PessoaPreInscricaoCursoVO objExistente = (PessoaPreInscricaoCursoVO) i.next();
			if (objExistente.getCurso().getCodigo().equals(obj.getCurso().getCodigo())) {
				getPessoaPreInscricaoCursoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getPessoaPreInscricaoCursoVOs().add(obj);
	}

	/**
	 * Operação responsável por excluir um objeto da classe
	 * <code>FormacaoAcademicaVO</code> no List <code>formacaoAcademicaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do
	 * objeto no List.
	 *
	 * @param curso Parâmetro para localizar e remover o objeto do List.
	 */
	public void excluirObjPessoaPreInscricaoCursoVOs(Integer curso) throws Exception {
		int index = 0;
		Iterator i = getPessoaPreInscricaoCursoVOs().iterator();
		while (i.hasNext()) {
			PessoaPreInscricaoCursoVO objExistente = (PessoaPreInscricaoCursoVO) i.next();
			if (objExistente.getCurso().getCodigo().equals(curso)) {
				getPessoaPreInscricaoCursoVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por consultar um objeto da classe
	 * <code>FormacaoAcademicaVO</code> no List <code>formacaoAcademicaVOs</code>.
	 * Utiliza o atributo padrão de consulta da classe
	 * <code>FormacaoAcademica</code> - getCurso() - como identificador (key) do
	 * objeto no List.
	 *
	 * @param curso Parâmetro para localizar o objeto do List.
	 */
	public PessoaPreInscricaoCursoVO consultarObjPessoaPreInscricaoCursoVO(String curso) throws Exception {
		Iterator i = getPessoaPreInscricaoCursoVOs().iterator();
		while (i.hasNext()) {
			PessoaPreInscricaoCursoVO objExistente = (PessoaPreInscricaoCursoVO) i.next();
			if (objExistente.getCurso().equals(curso)) {
				return objExistente;
			}
		}
		return null;
	}

	public Integer getIdadeAluno() {
		return Uteis.calcularIdadePessoa(new Date(), getDataNasc());
	}

	public String getDeficienciasCenso() {
        String deficiencia = "0||||||||||";
        TipoDeficiencia tipoDeficiencia = TipoDeficiencia.getEnum(getDeficiencia().trim());
        if (tipoDeficiencia == null) {
            return "0||||||||||";
        }
        switch (tipoDeficiencia) {
            case CEGUEIRA:
                deficiencia = "1|1|0|0|0|0|0|0|0|0|0";
                break;
            case BAIXA_VISAO:
                deficiencia = "1|0|1|0|0|0|0|0|0|0|0";
                break;
            case VISAO_MONOCULAR:
                deficiencia = "1|0|0|1|0|0|0|0|0|0|0";
                break;
            case SURDEZ:
                deficiencia = "1|0|0|0|1|0|0|0|0|0|0";
                break;
            case AUDITIVA:
                deficiencia = "1|0|0|0|0|1|0|0|0|0|0";
                break;
            case FISICA:
                deficiencia = "1|0|0|0|0|0|1|0|0|0|0";
                break;
            case SURDOCEGUEIRA:
                deficiencia = "1|0|0|0|0|0|0|1|0|0|0";
                break;
            case INTELECTUAL:
                deficiencia = "1|0|0|0|0|0|0|0|1|0|0";
                break;
            case TGDTEA:
                deficiencia = "1|0|0|0|0|0|0|0|0|1|0";
                break;     
            case SUPERDOTACAO:
                deficiencia = "1|0|0|0|0|0|0|0|0|0|1";
                break;    
            case NAO_DECLARADO:
                deficiencia = "0||||||||||";
                break;
            case NENHUMA:
                deficiencia = "0||||||||||";
                break;
        }
        return deficiencia;
    }
	public String consultarMaiorNivelEscolaridade() {
		String nivelEscolaridade = Constantes.EMPTY;
		int maiorNivel = 0;
		NivelFormacaoAcademica nivelFormacaoAcademica;
		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(formacaoAcademicaVO.getEscolaridade());
			if (nivelFormacaoAcademica != null && nivelFormacaoAcademica.getNivel() > 0
					&& formacaoAcademicaVO.getSituacao().equals("CO")) {
				if (maiorNivel < nivelFormacaoAcademica.getNivel()) {
					maiorNivel = nivelFormacaoAcademica.getNivel();
					nivelEscolaridade = nivelFormacaoAcademica.getSigla();
					if ((nivelFormacaoAcademica.getValor().equals("DR")
							|| nivelFormacaoAcademica.getValor().equals("GR")) && getSexo().equals("F")) {
						nivelEscolaridade += "a";
					}
				}
			}
		}
		return nivelEscolaridade;
	}

	public Integer consultarMaiorNivelDaEscolaridade() {
		int maiorNivel = 0;
		NivelFormacaoAcademica nivelFormacaoAcademica;
		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(formacaoAcademicaVO.getEscolaridade());
			if (nivelFormacaoAcademica != null && nivelFormacaoAcademica.getNivel() > 0
					&& formacaoAcademicaVO.getSituacao().equals("CO")) {
				if (maiorNivel < nivelFormacaoAcademica.getNivel()) {
					maiorNivel = nivelFormacaoAcademica.getNivel();
				}
			}
		}
		return maiorNivel;
	}

	/**
	 * Recupera a Matior titula de nivel de escolaridade da {@link PessoaVO} pela
	 * escoladridade da formação academica.
	 * 
	 * @return - Maior Titulação Escolar
	 */
	public String getMaiorTitulacaoNivelEscolaridade() {
		NivelFormacaoAcademica nivelFormacaoAcademica;
		NivelFormacaoAcademica nivelFormacaoAcademicaMaior = NivelFormacaoAcademica.FUNDAMENTAL;

		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(formacaoAcademicaVO.getEscolaridade());

			if (Uteis.isAtributoPreenchido(nivelFormacaoAcademica)
					&& nivelFormacaoAcademica.getNivel() > nivelFormacaoAcademicaMaior.getNivel()) {
				nivelFormacaoAcademicaMaior = nivelFormacaoAcademica;
			}
		}

		if (Uteis.isAtributoPreenchido(nivelFormacaoAcademicaMaior)
				|| (nivelFormacaoAcademicaMaior.getValor() != NivelFormacaoAcademica.FUNDAMENTAL.getValor())) {
			return nivelFormacaoAcademicaMaior.getDescricao();
		} else {
			return Constantes.EMPTY;
		}
	}

	public String getSiglaMaiorTitulacaoNivelEscolaridade() {
		NivelFormacaoAcademica nivelFormacaoAcademica;
		NivelFormacaoAcademica nivelFormacaoAcademicaMaior = NivelFormacaoAcademica.FUNDAMENTAL;

		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			if (formacaoAcademicaVO.isSituacaoFormacaoAcademicaConcluida()) {
				nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(formacaoAcademicaVO.getEscolaridade());
				if (Uteis.isAtributoPreenchido(nivelFormacaoAcademica)) {
					if (nivelFormacaoAcademica.getNivel() > nivelFormacaoAcademicaMaior.getNivel()) {
						nivelFormacaoAcademicaMaior = nivelFormacaoAcademica;
					}
				}
			}
		}

		if (Uteis.isAtributoPreenchido(nivelFormacaoAcademicaMaior)
				|| (nivelFormacaoAcademicaMaior.getValor() != NivelFormacaoAcademica.FUNDAMENTAL.getValor())) {
			return nivelFormacaoAcademicaMaior.getValor();
		} else {
			return Constantes.EMPTY;
		}
	}

	public String getTitulacaoNivelEscolaridade() {
		String nivelEscolaridade = Constantes.EMPTY;
		String titulacoes = Constantes.EMPTY;
		NivelFormacaoAcademica nivelFormacaoAcademica;
		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(formacaoAcademicaVO.getEscolaridade());
			if (nivelFormacaoAcademica != null && nivelFormacaoAcademica.getNivel() > 0) {
				nivelEscolaridade = nivelFormacaoAcademica.getSigla();
				if (!nivelFormacaoAcademica.getValor().equals(Constantes.EMPTY)) {
					if (!titulacoes.equals(Constantes.EMPTY)) {
						titulacoes += " / ";
					}
					if ((nivelFormacaoAcademica.getValor().equals("DR")
							|| nivelFormacaoAcademica.getValor().equals("GR")) && getSexo().equals("F")) {
						nivelEscolaridade += "a";
					}
					titulacoes += getTitulacaoProfessor(nivelEscolaridade);
				}
			}
		}
		return titulacoes;
	}

	public String getNivelEscolaridade() {
		String nivelEscolaridade = Constantes.EMPTY;
		NivelFormacaoAcademica nivelFormacaoAcademica;
		for (FormacaoAcademicaVO formacaoAcademicaVO : getFormacaoAcademicaVOs()) {
			nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(formacaoAcademicaVO.getEscolaridade());
			if (nivelFormacaoAcademica != null && nivelFormacaoAcademica.getNivel() > 0) {
				nivelEscolaridade = nivelFormacaoAcademica.getSigla();
				if (!nivelFormacaoAcademica.getValor().equals(Constantes.EMPTY)) {
					if ((nivelFormacaoAcademica.getValor().equals("DR")
							|| nivelFormacaoAcademica.getValor().equals("GR")) && getSexo().equals("F")) {
						nivelEscolaridade += "a";
					}
				}
			}
		}
		return nivelEscolaridade;
	}

	public String getTitulacaoProfessor(String nivelEscolaridade) {
		if (nivelEscolaridade.equalsIgnoreCase("dra")) {
			return "Doutora";
		} else if (nivelEscolaridade.equalsIgnoreCase("dr")) {
			return "Doutor";
		} else if (nivelEscolaridade.equalsIgnoreCase("ms")) {
			return "Mestre";
		} else if (nivelEscolaridade.equalsIgnoreCase("esp")) {
			return "Especialista";
		} else if (nivelEscolaridade.equalsIgnoreCase("phd")) {
			return "PhD";
		} else if (nivelEscolaridade.equalsIgnoreCase("gr")) {
			return "Graduado";
		} else if (nivelEscolaridade.equalsIgnoreCase("gra")) {
			return "Graduada";
		}
		return Constantes.EMPTY;
	}

	public Integer getNacionalidadeCenso() {
		if (getNacionalidade() == null) {
			return 1;
		} else {

			if (getNacionalidade().isNacao()) {
				return 1;
			} else {
				return 3;
			}
		}
	}

	public List getQuadroHorarioVOs() {
		if (quadroHorarioVOs == null) {
			quadroHorarioVOs = new ArrayList();
		}
		return quadroHorarioVOs;
	}

	public void setQuadroHorarioVOs(List quadroHorarioVOs) {
		this.quadroHorarioVOs = quadroHorarioVOs;
	}

	@XmlElement(name = "horarioProfessorVOs")
	public List<HorarioProfessorVO> getHorarioProfessorVOs() {
		if (horarioProfessorVOs == null) {
			horarioProfessorVOs = new ArrayList<HorarioProfessorVO>();
		}
		return horarioProfessorVOs;
	}

	public void setHorarioProfessorVOs(List<HorarioProfessorVO> horarioProfessorVOs) {
		this.horarioProfessorVOs = horarioProfessorVOs;
	}

	/**
	 * Retorna o objeto da classe <code>Paiz</code> relacionado com (
	 * <code>Pessoa</code>).
	 */

	@XmlElement(name = "nacionalidade")
	public PaizVO getNacionalidade() {
		if (nacionalidade == null) {
			nacionalidade = new PaizVO();
		}
		return (nacionalidade);
	}

	/**
	 * Define o objeto da classe <code>Paiz</code> relacionado com (
	 * <code>Pessoa</code>).
	 */
	public void setNacionalidade(PaizVO obj) {
		this.nacionalidade = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Cidade</code> relacionado com (
	 * <code>Pessoa</code>).
	 */
	@XmlElement(name = "naturalidade")
	public CidadeVO getNaturalidade() {
		if (naturalidade == null) {
			naturalidade = new CidadeVO();
		}
		return (naturalidade);
	}

	/**
	 * Define o objeto da classe <code>Cidade</code> relacionado com (
	 * <code>Pessoa</code>).
	 */
	public void setNaturalidade(CidadeVO obj) {
		this.naturalidade = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Cidade</code> relacionado com (
	 * <code>Pessoa</code>).
	 */
	@XmlElement(name = "cidade")
	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return (cidade);
	}

	/**
	 * Define o objeto da classe <code>Cidade</code> relacionado com (
	 * <code>Pessoa</code>).
	 */
	public void setCidade(CidadeVO obj) {
		this.cidade = obj;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>DisponibilidadeHorario</code>.
	 */

	@XmlElement(name = "disponibilidadeHorarioVOs")
	public List getDisponibilidadeHorarioVOs() {
		if (disponibilidadeHorarioVOs == null) {
			disponibilidadeHorarioVOs = new ArrayList();
		}
		return (disponibilidadeHorarioVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>DisponibilidadeHorario</code>.
	 */
	public void setDisponibilidadeHorarioVOs(List disponibilidadeHorarioVOs) {
		this.disponibilidadeHorarioVOs = disponibilidadeHorarioVOs;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>Filiacao</code>.
	 */
	@XmlElement(name = "filiacaoVOs")
	public List<FiliacaoVO> getFiliacaoVOs() {
		if (filiacaoVOs == null) {
			filiacaoVOs = new ArrayList<FiliacaoVO>(0);
		}
		return (filiacaoVOs);
	}

	public FiliacaoVO getFiliacaoVO_Tipo(String tipoFiliacao) {
		for (FiliacaoVO filiacao : getFiliacaoVOs()) {
			if (filiacao.getTipo().equals(tipoFiliacao)) {
				return filiacao;
			}
		}
		return new FiliacaoVO();
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>Filiacao</code>.
	 */
	public void setFiliacaoVOs(List<FiliacaoVO> filiacaoVOs) {
		this.filiacaoVOs = filiacaoVOs;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>DisciplinasInteresse</code>.
	 */

	public List<DisciplinasInteresseVO> getDisciplinasInteresseVOs() {
		if (disciplinasInteresseVOs == null) {
			disciplinasInteresseVOs = new ArrayList<DisciplinasInteresseVO>(0);
		}
		return (disciplinasInteresseVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>DisciplinasInteresse</code>.
	 */
	public void setDisciplinasInteresseVOs(List disciplinasInteresseVOs) {
		this.disciplinasInteresseVOs = disciplinasInteresseVOs;
	}

	/**
	 * Retorna Atributo responsável por manter os objetos da classe
	 * <code>FormacaoAcademica</code>.
	 */
	@XmlElement(name = "formacaoAcademicaVOs")
	public List<FormacaoAcademicaVO> getFormacaoAcademicaVOs() {
		if (formacaoAcademicaVOs == null) {
			formacaoAcademicaVOs = new ArrayList<FormacaoAcademicaVO>(0);
		}
		return (formacaoAcademicaVOs);
	}

	/**
	 * Define Atributo responsável por manter os objetos da classe
	 * <code>FormacaoAcademica</code>.
	 */
	public void setFormacaoAcademicaVOs(List<FormacaoAcademicaVO> formacaoAcademicaVOs) {
		this.formacaoAcademicaVOs = formacaoAcademicaVOs;
	}

	public Boolean getMostrarFormacaoAcademica() {
		if (getFormacaoAcademicaVOs().isEmpty()) {
			return false;
		}
		return true;
	}

	// public String getTipoPessoa() {
	// return (tipoPessoa);
	// }
	//
	// /**
	// * Operação responsável por retornar o valor de apresentação de um
	// atributo com um domínio específico.
	// * Com base no valor de armazenamento do atributo esta função é capaz de
	// retornar o
	// * de apresentação correspondente. Útil para campos como sexo,
	// escolaridade, etc.
	// */
	// public String getTipoPessoa_Apresentar() {
	// if (tipoPessoa.equals("PR")) {
	// return "Professor";
	// }
	// if (tipoPessoa.equals("AL")) {
	// return "Aluno";
	// }
	// if (tipoPessoa.equals("FU")) {
	// return "Funcionário";
	// }
	// if (tipoPessoa.equals("CO")) {
	// return "Comunidade";
	// }
	// return (tipoPessoa);
	// }
	//
	// public void setTipoPessoa(String tipoPessoa) {
	// this.tipoPessoa = tipoPessoa;
	// }
	@XmlElement(name = "necessidadesEspeciais")
	public String getNecessidadesEspeciais() {
		if (necessidadesEspeciais == null) {
			necessidadesEspeciais = Constantes.EMPTY;
		}
		return (necessidadesEspeciais);
	}

	public void setNecessidadesEspeciais(String necessidadesEspeciais) {
		this.necessidadesEspeciais = necessidadesEspeciais;
	}

	@XmlElement(name = "tipoNecessidadesEspeciais")
	public String getTipoNecessidadesEspeciais() {
		if (tipoNecessidadesEspeciais == null) {
			tipoNecessidadesEspeciais = "Nenhum";
		}
		return (tipoNecessidadesEspeciais);
	}

	public void setTipoNecessidadesEspeciais(String tipoNecessidadesEspeciais) {
		this.tipoNecessidadesEspeciais = tipoNecessidadesEspeciais;
	}

	@XmlElement(name = "tituloEleitoral")
	public String getTituloEleitoral() {
		if (tituloEleitoral == null) {
			tituloEleitoral = Constantes.EMPTY;
		}
		return (tituloEleitoral);
	}

	public void setTituloEleitoral(String tituloEleitoral) {
		this.tituloEleitoral = tituloEleitoral;
	}

	@XmlElement(name = "orgaoEmissor")
	public String getOrgaoEmissor() {
		if (orgaoEmissor == null) {
			orgaoEmissor = Constantes.EMPTY;
		}
		return (orgaoEmissor);
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	@XmlElement(name = "estadoEmissaoRG")
	public String getEstadoEmissaoRG() {
		if (estadoEmissaoRG == null) {
			estadoEmissaoRG = Constantes.EMPTY;
		}
		return (estadoEmissaoRG);
	}

	public String getNomeMae() {
		for (FiliacaoVO filiacao : getFiliacaoVOs()) {
			if ("MA".equals(filiacao.getTipo())) {
				return filiacao.getNome().trim();
			}
		}
		return Constantes.EMPTY;
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com
	 * um domínio específico. Com base no valor de armazenamento do atributo esta
	 * função é capaz de retornar o de apresentação correspondente. Útil para campos
	 * como sexo, escolaridade, etc.
	 */
	public String getEstadoEmissaoRG_Apresentar() {
		if (estadoEmissaoRG == null) {
			return Constantes.EMPTY;
		}
		if (estadoEmissaoRG.equals("BA")) {
			return "BA";
		}
		if (estadoEmissaoRG.equals("RS")) {
			return "RS";
		}
		if (estadoEmissaoRG.equals("RR")) {
			return "RR";
		}
		if (estadoEmissaoRG.equals("RO")) {
			return "RO";
		}
		if (estadoEmissaoRG.equals("RN")) {
			return "RN";
		}
		if (estadoEmissaoRG.equals("RJ")) {
			return "RJ";
		}
		if (estadoEmissaoRG.equals("CE")) {
			return "CE";
		}
		if (estadoEmissaoRG.equals("AP")) {
			return "AP";
		}
		if (estadoEmissaoRG.equals("MT")) {
			return "MT";
		}
		if (estadoEmissaoRG.equals("MS")) {
			return "MS";
		}
		if (estadoEmissaoRG.equals("PR")) {
			return "PR";
		}
		if (estadoEmissaoRG.equals("GO")) {
			return "GO";
		}
		if (estadoEmissaoRG.equals("AM")) {
			return "AM";
		}
		if (estadoEmissaoRG.equals("AL")) {
			return "AL";
		}
		if (estadoEmissaoRG.equals("SP")) {
			return "SP";
		}
		if (estadoEmissaoRG.equals("DF")) {
			return "DF";
		}
		if (estadoEmissaoRG.equals("PI")) {
			return "PI";
		}
		if (estadoEmissaoRG.equals("AC")) {
			return "AC";
		}
		if (estadoEmissaoRG.equals("MG")) {
			return "MG";
		}
		if (estadoEmissaoRG.equals("ES")) {
			return "ES";
		}
		if (estadoEmissaoRG.equals("PE")) {
			return "PE";
		}
		if (estadoEmissaoRG.equals("SE")) {
			return "SE";
		}
		if (estadoEmissaoRG.equals("SC")) {
			return "SC";
		}
		if (estadoEmissaoRG.equals("MA")) {
			return "MA";
		}
		if (estadoEmissaoRG.equals("PB")) {
			return "PB";
		}
		if (estadoEmissaoRG.equals("PA")) {
			return "PA";
		}
		if (estadoEmissaoRG.equals("TO")) {
			return "TO";
		}
		return (estadoEmissaoRG);
	}

	public void setEstadoEmissaoRG(String estadoEmissaoRG) {
		this.estadoEmissaoRG = estadoEmissaoRG;
	}

	@XmlElement(name = "dataEmissaoRG")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataEmissaoRG() {
		return (dataEmissaoRG);
	}

	@XmlElement(name = "aluno")
	public Boolean getAluno() {
		if (aluno == null) {
			aluno = false;
		}
		return aluno;
	}

	public void setAluno(Boolean aluno) {
		this.aluno = aluno;
	}

	@XmlElement(name = "candidato")
	public Boolean getCandidato() {
		if (candidato == null) {
			candidato = false;
		}
		return candidato;
	}

	public void setCandidato(Boolean candidato) {
		this.candidato = candidato;
	}

	@XmlElement(name = "funcionario")
	public Boolean getFuncionario() {
		if (funcionario == null) {
			funcionario = false;
		}
		return funcionario;
	}

	public void setFuncionario(Boolean funcionario) {
		this.funcionario = funcionario;
	}

	@XmlElement(name = "professor")
	public Boolean getProfessor() {
		if (professor == null) {
			professor = false;
		}
		return professor;
	}

	public void setProfessor(Boolean professor) {
		this.professor = professor;
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	public String getDataEmissaoRG_Apresentar() {
		return (Uteis.getData(dataEmissaoRG));
	}

	public void setDataEmissaoRG(Date dataEmissaoRG) {
		this.dataEmissaoRG = dataEmissaoRG;
	}

	@XmlElement(name = "certificadoMilitar")
	public String getCertificadoMilitar() {
		if (certificadoMilitar == null) {
			certificadoMilitar = Constantes.EMPTY;
		}
		return (certificadoMilitar);
	}

	public void setCertificadoMilitar(String certificadoMilitar) {
		this.certificadoMilitar = certificadoMilitar;
	}

	@XmlElement(name = "RG")
	public String getRG() {
		if (RG == null) {
			RG = Constantes.EMPTY;
		}
		return (RG);
	}

	public void setRG(String RG) {
		this.RG = RG;
	}

	public String getCPFMascarado() {
		return Uteis.isAtributoPreenchido(getCPF()) && getCPF().length() >= 14
				? "xxx." + getCPF().substring(4, 11) + "-xx"
				: Constantes.EMPTY;
	}

	@XmlElement(name = "CPF")
	public String getCPF() {
		if (CPF == null) {
			CPF = Constantes.EMPTY;
		}
		return (CPF);
	}

	public void setCPF(String CPF) {
		this.CPF = CPF;
	}

	@XmlElement(name = "dataNasc")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataNasc() {
		return (dataNasc);
	}

	@XmlElement(name = "membroComunidade")
	public Boolean getMembroComunidade() {
		if (membroComunidade == null) {
			membroComunidade = false;
		}
		return membroComunidade;
	}

	public void setMembroComunidade(Boolean membroComunidade) {
		this.membroComunidade = membroComunidade;
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão
	 * dd/mm/aaaa.
	 */
	public String getDataNasc_Apresentar() {
		return (Uteis.getData(dataNasc));
	}

	public Integer getIdade() {
		return (Uteis.calcularIdadePessoa(new Date(), getDataNasc()));
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	@XmlElement(name = "email")
	public String getEmail() {
		if (email == null) {
			email = Constantes.EMPTY;
		}
		return (email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name = "email2")
	public String getEmail2() {
		if (email2 == null) {
			email2 = Constantes.EMPTY;
		}
		return (email2);
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	@XmlElement(name = "celular")
	public String getCelular() {
		if (celular == null) {
			celular = Constantes.EMPTY;
		}
		return (celular);
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@XmlElement(name = "telefoneRecado")
	public String getTelefoneRecado() {
		if (telefoneRecado == null) {
			telefoneRecado = Constantes.EMPTY;
		}
		return (telefoneRecado);
	}

	public void setTelefoneRecado(String telefoneRecado) {
		this.telefoneRecado = telefoneRecado;
	}

	@XmlElement(name = "telefoneRes")
	public String getTelefoneRes() {
		if (telefoneRes == null) {
			telefoneRes = Constantes.EMPTY;
		}
		return (telefoneRes);
	}

	public void setTelefoneRes(String telefoneRes) {
		this.telefoneRes = telefoneRes;
	}

	@XmlElement(name = "telefoneComer")
	public String getTelefoneComer() {
		if (telefoneComer == null) {
			telefoneComer = Constantes.EMPTY;
		}
		return (telefoneComer);
	}

	public void setTelefoneComer(String telefoneComer) {
		this.telefoneComer = telefoneComer;
	}

	@XmlElement(name = "estadoCivil")
	public String getEstadoCivil() {
		if (estadoCivil == null) {
			estadoCivil = Constantes.EMPTY;
		}
		return (estadoCivil);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com
	 * um domínio específico. Com base no valor de armazenamento do atributo esta
	 * função é capaz de retornar o de apresentação correspondente. Útil para campos
	 * como sexo, escolaridade, etc.
	 */
	public String getEstadoCivil_Apresentar() {
		if (getEstadoCivil().equals("S")) {
			return "Solteiro(a)";
		}
		if (getEstadoCivil().equals("C")) {
			return "Casado(a)";
		}
		if (getEstadoCivil().equals("V")) {
			return "Viúvo(a)";
		}
		if (getEstadoCivil().equals("D")) {
			return "Divorciado(a)";
		}
		if (getEstadoCivil().equals("A")) {
			return "Amasiado(a)";
		}
		if (getEstadoCivil().equals("U")) {
			return "União Estável";
		}
		if (getEstadoCivil().equals("E")) {
			return "Separado(a)";
		}
		if (getEstadoCivil().equals("Q")) {
			return "Desquitado(a)";
		}
		return (getEstadoCivil());
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	@XmlElement(name = "sexo")
	public String getSexo() {
		if (sexo == null) {
			sexo = Constantes.EMPTY;
		}
		return (sexo);
	}

	public int getSexoCenso() {
		if (sexo != null && sexo.equals("F")) {
			return 1;
		}
		return 0;
	}

	public int getSexoCensoTecnico() {
		if (sexo != null && sexo.equals("F")) {
			return 2;
		}
		return 1;
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com
	 * um domínio específico. Com base no valor de armazenamento do atributo esta
	 * função é capaz de retornar o de apresentação correspondente. Útil para campos
	 * como sexo, escolaridade, etc.
	 */
	public String getSexo_Apresentar() {
		if (getSexo().equals("F")) {
			return "Feminino";
		}
		if (getSexo().equals("M")) {
			return "Masculino";
		}
		return (sexo);
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@XmlElement(name = "complemento")
	public String getComplemento() {
		if (complemento == null) {
			complemento = Constantes.EMPTY;
		}
		return (complemento);
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@XmlElement(name = "cep")
	public String getCEP() {
		if (CEP == null) {
			CEP = Constantes.EMPTY;
		}
		return (CEP);
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
	}

	@XmlElement(name = "numero")
	public String getNumero() {
		if (numero == null) {
			numero = Constantes.EMPTY;
		}
		return (numero);
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	@XmlElement(name = "setor")
	public String getSetor() {
		if (setor == null) {
			setor = Constantes.EMPTY;
		}
		return (setor);
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	@XmlElement(name = "endereco")
	public String getEndereco() {
		if (endereco == null) {
			endereco = Constantes.EMPTY;
		}
		return (endereco);
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = Constantes.EMPTY;

		} else {
			if (nome.contains("''")) {
				nome = nome.replaceAll("''", "'");
			}
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "paginaPessoal")
	public String getPaginaPessoal() {
		if (paginaPessoal == null) {
			paginaPessoal = Constantes.EMPTY;
		}
		return paginaPessoal;
	}

	public void setPaginaPessoal(String paginaPessoal) {
		this.paginaPessoal = paginaPessoal;
	}

	@XmlElement(name = "perfilEconomico")
	public PerfilEconomicoVO getPerfilEconomico() {
		if (perfilEconomico == null) {
			perfilEconomico = new PerfilEconomicoVO();
		}
		return perfilEconomico;
	}

	public void setPerfilEconomico(PerfilEconomicoVO perfilEconomico) {
		this.perfilEconomico = perfilEconomico;
	}

	// public byte[] getFoto() {
	// return foto;
	// }
	//
	// public void setFoto(byte[] foto) {
	// this.foto = foto;
	// }
	/**
	 * @return the tipoFuncionario
	 */
	@XmlElement(name = "atuaComoDocente")
	public String getAtuaComoDocente() {
		if (atuaComoDocente == null) {
			atuaComoDocente = "FU";
		}
		return atuaComoDocente;
	}

	/**
	 * @param tipoFuncionario the tipoFuncionario to set
	 */
	public void setAtuaComoDocente(String atuaComoDocente) {
		this.atuaComoDocente = atuaComoDocente;
	}

	/**
	 * @return the ativo
	 */
	@XmlElement(name = "ativo")
	public Boolean getAtivo() {
		if (ativo == null) {
			ativo = true;
		}
		return ativo;
	}

	/**
	 * @param ativo the ativo to set
	 */
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@XmlElement(name = "idAlunoInep")
	public String getIdAlunoInep() {
		if (idAlunoInep == null) {
			idAlunoInep = Constantes.EMPTY;
		}
		return idAlunoInep;
	}

	public void setIdAlunoInep(String idAlunoInep) {
		this.idAlunoInep = idAlunoInep;
	}

	@XmlElement(name = "passaporte")
	public String getPassaporte() {
		if (passaporte == null) {
			passaporte = Constantes.EMPTY;
		}
		return passaporte;
	}

	public void setPassaporte(String passaporte) {
		this.passaporte = passaporte;
	}

	@XmlElement(name = "corRaca")
	public String getCorRaca() {
		if (corRaca == null) {
			corRaca = Constantes.EMPTY;
		}
		return corRaca;
	}

	public int getCorRacaCenso() {
		return CorRaca.getCodigoCenso(corRaca);
	}

	public void setCorRaca(String corRaca) {
		this.corRaca = corRaca;
	}

	@XmlElement(name = "deficiencia")
	public String getDeficiencia() {
		if (deficiencia == null) {
			deficiencia = Constantes.EMPTY;
		}
		return deficiencia;
	}

	public void setDeficiencia(String deficiencia) {
		this.deficiencia = deficiencia;
	}

	@XmlElement(name = "selecionado")
	public Boolean getSelecionado() {
		if (selecionado == null) {
			selecionado = false;
		}
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	@Override
	public String toString() {
		return "PessoaVO [codigo=" + codigo + ", nome=" + nome + ", funcionario=" + funcionario + ", professor="
				+ professor + ", aluno=" + aluno + "]";
	}

	/**
	 * @return the nomeFiador
	 */
	@XmlElement(name = "nomeFiador")
	public String getNomeFiador() {
		if (nomeFiador == null) {
			nomeFiador = Constantes.EMPTY;
		}
		return nomeFiador;
	}

	/**
	 * @param nomeFiador the nomeFiador to set
	 */
	public void setNomeFiador(String nomeFiador) {
		this.nomeFiador = nomeFiador;
	}

	/**
	 * @return the enderecoFiador
	 */
	@XmlElement(name = "enderecoFiador")
	public String getEnderecoFiador() {
		if (enderecoFiador == null) {
			enderecoFiador = Constantes.EMPTY;
		}
		return enderecoFiador;
	}

	/**
	 * @param enderecoFiador the enderecoFiador to set
	 */
	public void setEnderecoFiador(String enderecoFiador) {
		this.enderecoFiador = enderecoFiador;
	}

	/**
	 * @return the telefoneFiador
	 */
	@XmlElement(name = "telefoneFiador")
	public String getTelefoneFiador() {
		if (telefoneFiador == null) {
			telefoneFiador = Constantes.EMPTY;
		}
		return telefoneFiador;
	}

	/**
	 * @param telefoneFiador the telefoneFiador to set
	 */
	public void setTelefoneFiador(String telefoneFiador) {
		this.telefoneFiador = telefoneFiador;
	}

	/**
	 * @return the cpfFiador
	 */
	@XmlElement(name = "cpfFiador")
	public String getCpfFiador() {
		if (cpfFiador == null) {
			cpfFiador = Constantes.EMPTY;
		}
		return cpfFiador;
	}

	/**
	 * @param cpfFiador the cpfFiador to set
	 */
	public void setCpfFiador(String cpfFiador) {
		this.cpfFiador = cpfFiador;
	}

	/**
	 * @return the celularFiador
	 */
	@XmlElement(name = "celularFiador")
	public String getCelularFiador() {
		if (celularFiador == null) {
			celularFiador = Constantes.EMPTY;
		}
		return celularFiador;
	}

	/**
	 * @param celularFiador the celularFiador to set
	 */
	public void setCelularFiador(String celularFiador) {
		this.celularFiador = celularFiador;
	}

	/**
	 * @return the valorCssTopoLogo
	 */
	@XmlElement(name = "valorCssTopoLogo")
	public String getValorCssTopoLogo() {
		if (valorCssTopoLogo == null) {
			valorCssTopoLogo = Constantes.EMPTY;
		}
		return valorCssTopoLogo;
	}

	/**
	 * @param valorCssTopoLogo the valorCssTopoLogo to set
	 */
	public void setValorCssTopoLogo(String valorCssTopoLogo) {
		this.valorCssTopoLogo = valorCssTopoLogo;
	}

	/**
	 * @return the valorCssBackground
	 */
	@XmlElement(name = "valorCssBackground")
	public String getValorCssBackground() {
		if (valorCssBackground == null) {
			valorCssBackground = Constantes.EMPTY;
		}
		return valorCssBackground;
	}

	/**
	 * @param valorCssBackground the valorCssBackground to set
	 */
	public void setValorCssBackground(String valorCssBackground) {
		this.valorCssBackground = valorCssBackground;
	}

	/**
	 * @return the valorCssMenu
	 */
	@XmlElement(name = "valorCssMenu")
	public String getValorCssMenu() {
		if (valorCssMenu == null) {
			valorCssMenu = Constantes.EMPTY;
		}
		return valorCssMenu;
	}

	/**
	 * @param valorCssMenu the valorCssMenu to set
	 */
	public void setValorCssMenu(String valorCssMenu) {
		this.valorCssMenu = valorCssMenu;
	}

	public void setArquivoImagem(ArquivoVO arquivoImagem) {
		this.arquivoImagem = arquivoImagem;
	}

	public ArquivoVO getArquivoImagem() {
		if (arquivoImagem == null) {
			arquivoImagem = new ArquivoVO();
		}
		return arquivoImagem;
	}

	public String getPrimeiroNome() {
		String nome = getNome().replaceAll("^\\s+", Constantes.EMPTY);
		String[] nomes = nome.split(" ");
		if (nomes.length > 0) {
			return nomes[0];
		} else {
			return Constantes.EMPTY;
		}
	}

	public String getUltimoNome() {
		String[] nomes = getNome().split(" ");
		if (nomes.length > 1) {
			return nomes[nomes.length - 1];
		} else {
			return Constantes.EMPTY;
		}
	}

	public String getNomeResumido() {
		List<String> preposicoes2 = new ArrayList(0);
		preposicoes2.add("dos");
		preposicoes2.add("das");
		preposicoes2.add("de");
		preposicoes2.add("da");
		preposicoes2.add("e");
		preposicoes2.add("a");
		preposicoes2.add("i");
		preposicoes2.add("o");
		preposicoes2.add("DOS");
		preposicoes2.add("DAS");
		preposicoes2.add("DE");
		preposicoes2.add("DA");
		preposicoes2.add("E");
		preposicoes2.add("A");
		preposicoes2.add("I");
		preposicoes2.add("O");
		String[] nomes = getNome().trim().split(" ");
		if (nomes.length == 1) {
			return getNome();
		}
		String nomeResumido = Constantes.EMPTY;
		Integer indice = 1;
		nomeResumido += nomes[0] + " ";
		while (indice < nomes.length - 1) {
			if (preposicoes2.contains(nomes[indice])) {
				nomes[indice] = Constantes.EMPTY;
			} else if (nomes[indice].length() > 2) {
				nomes[indice] = nomes[indice].substring(0, 1).concat(".");
			}
			nomeResumido += nomes[indice] + " ";
			indice = indice + 1;
		}
		preposicoes2 = null;
		nomeResumido += nomes[nomes.length - 1] + " ";
		return nomeResumido;
	}

	public String getNomeResumidoNomeSobrenome() {
		String[] nomes = getNomeResumido().replace("  ", " ").split(" ");
		String nomeResumido = Constantes.EMPTY;
		for (int i = 0; i < nomes.length; i++) {
			if (i == 0) {
				nomeResumido = nomes[i];
			} else if (i == 1) {
				nomeResumido += " " + nomes[i];
			} else if (i == 2) {
				nomeResumido += " " + nomes[i];
			}
		}
		return nomeResumido;
	}

	@XmlElement(name = "coordenador")
	public Boolean getCoordenador() {
		if (coordenador == null) {
			coordenador = false;
		}
		return coordenador;
	}

	public void setCoordenador(Boolean coordenador) {
		this.coordenador = coordenador;
	}

	/**
	 * @return the enviarComunicadoPessoa
	 */
	@XmlElement(name = "enviarComunicadoPessoa")
	public Boolean getEnviarComunicadoPessoa() {
		if (enviarComunicadoPessoa == null) {
			enviarComunicadoPessoa = Boolean.FALSE;
		}
		return enviarComunicadoPessoa;
	}

	/**
	 * @param enviarComunicadoPessoa the enviarComunicadoPessoa to set
	 */
	public void setEnviarComunicadoPessoa(Boolean enviarComunicadoPessoa) {
		this.enviarComunicadoPessoa = enviarComunicadoPessoa;
	}

	public List<DadosComerciaisVO> getDadosComerciaisVOs() {
		if (dadosComerciaisVOs == null) {
			dadosComerciaisVOs = new ArrayList<DadosComerciaisVO>(0);
		}
		return dadosComerciaisVOs;
	}

	public void setDadosComerciaisVOs(List<DadosComerciaisVO> dadosComerciaisVOs) {
		this.dadosComerciaisVOs = dadosComerciaisVOs;
	}

	public Boolean getMostrarExperienciaProfissional() {
		if (getDadosComerciaisVOs().isEmpty()) {
			return false;
		}
		return true;
	}

	public List<FormacaoExtraCurricularVO> getFormacaoExtraCurricularVOs() {
		if (formacaoExtraCurricularVOs == null) {
			formacaoExtraCurricularVOs = new ArrayList<FormacaoExtraCurricularVO>(0);
		}
		return formacaoExtraCurricularVOs;
	}

	public void setFormacaoExtraCurricularVOs(List<FormacaoExtraCurricularVO> formacaoExtraCurricularVOs) {
		this.formacaoExtraCurricularVOs = formacaoExtraCurricularVOs;
	}

	public Boolean getMostrarFormacaoExtraCurricular() {
		if (getFormacaoExtraCurricularVOs().isEmpty()) {
			return false;
		}
		return true;
	}

	@XmlElement(name = "espanhol")
	public Boolean getEspanhol() {
		if (espanhol == null) {
			espanhol = false;
		}
		return espanhol;
	}

	public void setEspanhol(Boolean espanhol) {
		this.espanhol = espanhol;
	}

	@XmlElement(name = "espanholNivel")
	public String getEspanholNivel() {
		if (espanholNivel == null) {
			espanholNivel = Constantes.EMPTY;
		}
		return espanholNivel;
	}

	public void setEspanholNivel(String espanholNivel) {
		this.espanholNivel = espanholNivel;
	}

	@XmlElement(name = "frances")
	public Boolean getFrances() {
		if (frances == null) {
			frances = false;
		}
		return frances;
	}

	public void setFrances(Boolean frances) {
		this.frances = frances;
	}

	@XmlElement(name = "francesNivel")
	public String getFrancesNivel() {
		if (francesNivel == null) {
			francesNivel = Constantes.EMPTY;
		}
		return francesNivel;
	}

	public void setFrancesNivel(String francesNivel) {
		this.francesNivel = francesNivel;
	}

	@XmlElement(name = "ingles")
	public Boolean getIngles() {
		if (ingles == null) {
			ingles = false;
		}
		return ingles;
	}

	public void setIngles(Boolean ingles) {
		this.ingles = ingles;
	}

	@XmlElement(name = "inglesNivel")
	public String getInglesNivel() {
		if (inglesNivel == null) {
			inglesNivel = Constantes.EMPTY;
		}
		return inglesNivel;
	}

	public String getInglesNivel_Apresentar() {
		if (inglesNivel == null) {
			inglesNivel = Constantes.EMPTY;
		} else if (inglesNivel.equals("avancado")) {
			return "Avançado";
		} else if (inglesNivel.equals("inicial")) {
			return "Inicial";
		} else {
			return "Intermediário";
		}
		return inglesNivel;
	}

	public String getEspanholNivel_Apresentar() {
		if (espanholNivel == null) {
			espanholNivel = Constantes.EMPTY;
		} else if (espanholNivel.equals("avancado")) {
			return "Avançado";
		} else if (espanholNivel.equals("inicial")) {
			return "Inicial";
		} else {
			return "Intermediário";
		}
		return espanholNivel;
	}

	public String getFrancesNivel_Apresentar() {
		if (francesNivel == null) {
			francesNivel = Constantes.EMPTY;
		} else if (francesNivel.equals("avancado")) {
			return "Avançado";
		} else if (francesNivel.equals("inicial")) {
			return "Inicial";
		} else {
			return "Intermediário";
		}
		return francesNivel;
	}

	public void setInglesNivel(String inglesNivel) {
		this.inglesNivel = inglesNivel;
	}

	@XmlElement(name = "outrosIdiomas")
	public String getOutrosIdiomas() {
		if (outrosIdiomas == null) {
			outrosIdiomas = Constantes.EMPTY;
		}
		return outrosIdiomas;
	}

	public void setOutrosIdiomas(String outrosIdiomas) {
		this.outrosIdiomas = outrosIdiomas;
	}

	@XmlElement(name = "outrosIdiomasNivel")
	public String getOutrosIdiomasNivel() {
		if (outrosIdiomasNivel == null) {
			outrosIdiomasNivel = Constantes.EMPTY;
		}
		return outrosIdiomasNivel;
	}

	public void setOutrosIdiomasNivel(String outrosIdiomasNivel) {
		this.outrosIdiomasNivel = outrosIdiomasNivel;
	}

	@XmlElement(name = "access")
	public Boolean getAccess() {
		if (access == null) {
			access = false;
		}
		return access;
	}

	public void setAccess(Boolean access) {
		this.access = access;
	}

	@XmlElement(name = "autoCad")
	public Boolean getAutoCad() {
		if (autoCad == null) {
			autoCad = false;
		}
		return autoCad;
	}

	public void setAutoCad(Boolean autoCad) {
		this.autoCad = autoCad;
	}

	@XmlElement(name = "corelDraw")
	public Boolean getCorelDraw() {
		if (corelDraw == null) {
			corelDraw = false;
		}
		return corelDraw;
	}

	public void setCorelDraw(Boolean corelDraw) {
		this.corelDraw = corelDraw;
	}

	@XmlElement(name = "excel")
	public Boolean getExcel() {
		if (excel == null) {
			excel = false;
		}
		return excel;
	}

	public void setExcel(Boolean excel) {
		this.excel = excel;
	}

	@XmlElement(name = "internet")
	public Boolean getInternet() {
		if (internet == null) {
			internet = false;
		}
		return internet;
	}

	public void setInternet(Boolean internet) {
		this.internet = internet;
	}

	@XmlElement(name = "microsiga")
	public Boolean getMicrosiga() {
		if (microsiga == null) {
			microsiga = false;
		}
		return microsiga;
	}

	public void setMicrosiga(Boolean microsiga) {
		this.microsiga = microsiga;
	}

	@XmlElement(name = "outrosSoftwares")
	public String getOutrosSoftwares() {
		if (outrosSoftwares == null) {
			outrosSoftwares = Constantes.EMPTY;
		}
		return outrosSoftwares;
	}

	public void setOutrosSoftwares(String outrosSoftwares) {
		this.outrosSoftwares = outrosSoftwares;
	}

	@XmlElement(name = "photoshop")
	public Boolean getPhotoshop() {
		if (photoshop == null) {
			photoshop = false;
		}
		return photoshop;
	}

	public void setPhotoshop(Boolean photoshop) {
		this.photoshop = photoshop;
	}

	@XmlElement(name = "powerPoint")
	public Boolean getPowerPoint() {
		if (powerPoint == null) {
			powerPoint = false;
		}
		return powerPoint;
	}

	public void setPowerPoint(Boolean powerPoint) {
		this.powerPoint = powerPoint;
	}

	@XmlElement(name = "sap")
	public Boolean getSap() {
		if (sap == null) {
			sap = false;
		}
		return sap;
	}

	public void setSap(Boolean sap) {
		this.sap = sap;
	}

	@XmlElement(name = "windows")
	public Boolean getWindows() {
		if (windows == null) {
			windows = false;
		}
		return windows;
	}

	public void setWindows(Boolean windows) {
		this.windows = windows;
	}

	@XmlElement(name = "word")
	public Boolean getWord() {
		if (word == null) {
			word = false;
		}
		return word;
	}

	public void setWord(Boolean word) {
		this.word = word;
	}

	public String getSoftwares() {
		String softwares = Constantes.EMPTY;
		Boolean primeiro = Boolean.TRUE;
		if (getWindows()) {
			softwares = "Windows, ";
			primeiro = Boolean.FALSE;
		}
		if (getWord() && primeiro) {
			softwares = "Word, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getWord()) {
			softwares = softwares + " Word, ";
		}
		if (getExcel() && primeiro) {
			softwares = "Excel, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getExcel()) {
			softwares = softwares + " Excel, ";
		}
		if (getAccess() && primeiro) {
			softwares = "Acess, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getAccess()) {
			softwares = softwares + " Acess, ";
		}
		if (getPowerPoint() && primeiro) {
			softwares = "PowerPoint ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getPowerPoint()) {
			softwares = softwares + " PowerPoint, ";
		}
		if (getInternet() && primeiro) {
			softwares = "Internet, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getInternet()) {
			softwares = softwares + " Internet, ";
		}
		if (getSap() && primeiro) {
			softwares = "Sap, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getSap()) {
			softwares = softwares + " Sap, ";
		}
		if (getCorelDraw() && primeiro) {
			softwares = "CorelDraw, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getCorelDraw()) {
			softwares = softwares + " CorelDraw, ";
		}
		if (getAutoCad() && primeiro) {
			softwares = "AutoCad, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getAutoCad()) {
			softwares = softwares + " AutoCad, ";
		}
		if (getPhotoshop() && primeiro) {
			softwares = "Photoshop, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getPhotoshop()) {
			softwares = softwares + " Photoshop, ";
		}
		if (getMicrosiga() && primeiro) {
			softwares = "Microsiga, ";
			primeiro = Boolean.FALSE;
		} else if (!primeiro && getMicrosiga()) {
			softwares = softwares + " Microsiga, ";
		}
		if (!getOutrosSoftwares().equals(Constantes.EMPTY)) {
			softwares = getOutrosSoftwares() + ", ";
		}
		if (softwares.equals(Constantes.EMPTY) && softwares.length() >= 2) {
			softwares = softwares.substring(0, softwares.length() - 2);
			softwares = softwares + ".";
		}
		return softwares;
	}

	public List<AreaProfissionalInteresseContratacaoVO> getAreaProfissionalInteresseContratacaoVOs() {
		if (areaProfissionalInteresseContratacaoVOs == null) {
			areaProfissionalInteresseContratacaoVOs = new ArrayList<AreaProfissionalInteresseContratacaoVO>(0);
		}
		return areaProfissionalInteresseContratacaoVOs;
	}

	public void setAreaProfissionalInteresseContratacaoVOs(
			List<AreaProfissionalInteresseContratacaoVO> areaProfissionalInteresseContratacaoVOs) {
		this.areaProfissionalInteresseContratacaoVOs = areaProfissionalInteresseContratacaoVOs;
	}

	public JRDataSource getListaAreaProfissionalInteresseContratacaoVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getAreaProfissionalInteresseContratacaoVOs().toArray());
		return jr;
	}

	public JRDataSource getListaDadosComerciaisVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getDadosComerciaisVOs().toArray());
		return jr;
	}

	public JRDataSource getListaFormacaoAcademicaVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getFormacaoAcademicaVOs().toArray());
		return jr;
	}

	public JRDataSource getListaFormacaoExtraCurricularVOs() {
		JRDataSource jr = new JRBeanArrayDataSource(getFormacaoExtraCurricularVOs().toArray());
		return jr;
	}

	public String getCargoInteresseContratacaoCurriculum() {
		Iterator i = getAreaProfissionalInteresseContratacaoVOs().iterator();
		String cargoInteresse = Constantes.EMPTY;
		int tamanho = getAreaProfissionalInteresseContratacaoVOs().size();
		int aux = 1;
		while (i.hasNext()) {
			AreaProfissionalInteresseContratacaoVO obj = (AreaProfissionalInteresseContratacaoVO) i.next();
			if (aux < tamanho) {
				cargoInteresse += obj.getAreaProfissional().getDescricaoAreaProfissional() + ", ";
			} else {
				cargoInteresse += obj.getAreaProfissional().getDescricaoAreaProfissional() + ".";
			}
			aux++;
		}
		return cargoInteresse;
	}

	@XmlElement(name = "qtdFilhos")
	public Integer getQtdFilhos() {
		if (qtdFilhos == null) {
			qtdFilhos = 0;
		}
		return qtdFilhos;
	}

	public void setQtdFilhos(Integer qtdFilhos) {
		this.qtdFilhos = qtdFilhos;
	}

	@XmlElement(name = "possuiFilho")
	public Boolean getPossuiFilho() {
		if (possuiFilho == null) {
			possuiFilho = false;
		}
		return possuiFilho;
	}

	public void setPossuiFilho(Boolean possuiFilho) {
		this.possuiFilho = possuiFilho;
	}

	/**
	 * @return the nomeEmpresa
	 */
	@XmlElement(name = "nomeEmpresa")
	public String getNomeEmpresa() {
		if (nomeEmpresa == null) {
			nomeEmpresa = Constantes.EMPTY;
		}
		return nomeEmpresa;
	}

	/**
	 * @param nomeEmpresa the nomeEmpresa to set
	 */
	public void setNomeEmpresa(String nomeEmpresa) {
		this.nomeEmpresa = nomeEmpresa;
	}

	/**
	 * @return the enderecoEmpresa
	 */
	@XmlElement(name = "enderecoEmpresa")
	public String getEnderecoEmpresa() {
		if (enderecoEmpresa == null) {
			enderecoEmpresa = Constantes.EMPTY;
		}
		return enderecoEmpresa;
	}

	/**
	 * @param enderecoEmpresa the enderecoEmpresa to set
	 */
	public void setEnderecoEmpresa(String enderecoEmpresa) {
		this.enderecoEmpresa = enderecoEmpresa;
	}

	/**
	 * @return the cargoPessoaEmpresa
	 */
	@XmlElement(name = "cargoPessoaEmpresa")
	public String getCargoPessoaEmpresa() {
		if (cargoPessoaEmpresa == null) {
			cargoPessoaEmpresa = Constantes.EMPTY;
		}
		return cargoPessoaEmpresa;
	}

	/**
	 * @param cargoPessoaEmpresa the cargoPessoaEmpresa to set
	 */
	public void setCargoPessoaEmpresa(String cargoPessoaEmpresa) {
		this.cargoPessoaEmpresa = cargoPessoaEmpresa;
	}

	/**
	 * @return the cepEmpresa
	 */
	@XmlElement(name = "cepEmpresa")
	public String getCepEmpresa() {
		if (cepEmpresa == null) {
			cepEmpresa = Constantes.EMPTY;
		}
		return cepEmpresa;
	}

	/**
	 * @param cepEmpresa the cepEmpresa to set
	 */
	public void setCepEmpresa(String cepEmpresa) {
		this.cepEmpresa = cepEmpresa;
	}

	/**
	 * @return the complementoEmpresa
	 */
	@XmlElement(name = "complementoEmpresa")
	public String getComplementoEmpresa() {
		if (complementoEmpresa == null) {
			complementoEmpresa = Constantes.EMPTY;
		}
		return complementoEmpresa;
	}

	/**
	 * @param complementoEmpresa the complementoEmpresa to set
	 */
	public void setComplementoEmpresa(String complementoEmpresa) {
		this.complementoEmpresa = complementoEmpresa;
	}

	/**
	 * @return the setorEmpresa
	 */
	@XmlElement(name = "setorEmpresa")
	public String getSetorEmpresa() {
		if (setorEmpresa == null) {
			setorEmpresa = Constantes.EMPTY;
		}
		return setorEmpresa;
	}

	/**
	 * @param setorEmpresa the setorEmpresa to set
	 */
	public void setSetorEmpresa(String setorEmpresa) {
		this.setorEmpresa = setorEmpresa;
	}

	/**
	 * @return the cidadeEmpresa
	 */

	@XmlElement(name = "cidadeEmpresa")
	public CidadeVO getCidadeEmpresa() {
		if (cidadeEmpresa == null) {
			cidadeEmpresa = new CidadeVO();
		}
		return cidadeEmpresa;
	}

	/**
	 * @param cidadeEmpresa the cidadeEmpresa to set
	 */
	public void setCidadeEmpresa(CidadeVO cidadeEmpresa) {
		this.cidadeEmpresa = cidadeEmpresa;
	}

	@XmlElement(name = "participaBancoCurriculum")
	public Boolean getParticipaBancoCurriculum() {
		if (participaBancoCurriculum == null) {
			participaBancoCurriculum = Boolean.TRUE;
		}
		return participaBancoCurriculum;
	}

	public void setParticipaBancoCurriculum(Boolean participaBancoCurriculum) {
		this.participaBancoCurriculum = participaBancoCurriculum;
	}

	@XmlElement(name = "informacoesVerdadeiras")
	public Boolean getInformacoesVerdadeiras() {
		if (informacoesVerdadeiras == null) {
			informacoesVerdadeiras = Boolean.TRUE;
		}
		return informacoesVerdadeiras;
	}

	public void setInformacoesVerdadeiras(Boolean informacoesVerdadeiras) {
		this.informacoesVerdadeiras = informacoesVerdadeiras;
	}

	@XmlElement(name = "divulgarMeusDados")
	public Boolean getDivulgarMeusDados() {
		if (divulgarMeusDados == null) {
			divulgarMeusDados = Boolean.TRUE;
		}
		return divulgarMeusDados;
	}

	public void setDivulgarMeusDados(Boolean divulgarMeusDados) {
		this.divulgarMeusDados = divulgarMeusDados;
	}

	@XmlElement(name = "certidaoNascimento")
	public String getCertidaoNascimento() {
		if (certidaoNascimento == null) {
			certidaoNascimento = Constantes.EMPTY;
		}
		return certidaoNascimento;
	}

	public void setCertidaoNascimento(String certidaoNascimento) {
		this.certidaoNascimento = certidaoNascimento;
	}

	public Boolean getGerarNumeroCPF() {
		if (gerarNumeroCPF == null) {
			gerarNumeroCPF = Boolean.FALSE;
		}
		return gerarNumeroCPF;
	}

	public void setGerarNumeroCPF(Boolean gerarNumeroCPF) {
		this.gerarNumeroCPF = gerarNumeroCPF;
	}

	/**
	 * @return the informacoesAdicionais
	 */
	@XmlElement(name = "informacoesAdicionais")
	public String getInformacoesAdicionais() {
		if (informacoesAdicionais == null) {
			informacoesAdicionais = Constantes.EMPTY;
		}
		return informacoesAdicionais;
	}

	/**
	 * @param informacoesAdicionais the informacoesAdicionais to set
	 */
	public void setInformacoesAdicionais(String informacoesAdicionais) {
		this.informacoesAdicionais = informacoesAdicionais;
	}

	/**
	 * @return the codProspect
	 */
	@XmlElement(name = "codProspect")
	public Integer getCodProspect() {
		if (codProspect == null) {
			codProspect = 0;
		}
		return codProspect;
	}

	public ProspectsVO gerarProspectsVOAPartirPessoaVO() {
		ProspectsVO obj = new ProspectsVO();
		obj.setPessoa(this);
		obj.setNome(this.getNome());
		obj.setCEP(this.getCEP());
		obj.setCpf(this.getCPF());
		obj.setComplemento(this.getComplemento());
		obj.setEndereco(this.getEndereco());
		obj.setEmailPrincipal(this.getEmail());
		obj.setSetor(this.getSetor());
		obj.setTelefoneComercial(this.getTelefoneComer());
		obj.setCidade(this.getCidade());
		obj.setCelular(this.getCelular());
		obj.setRg(this.getRG());
		obj.setTelefoneRecado(this.getTelefoneRecado());
		obj.setTelefoneResidencial(this.getTelefoneRes());
		obj.setArquivoFoto(this.getArquivoImagem());
		obj.setSexo(this.getSexo());
		obj.setDataNascimento(this.getDataNasc());

		obj.setCargo(this.getCargoPessoaEmpresa());
		obj.setDataExpedicao(this.getDataEmissaoRG());
		obj.setEmailSecundario(this.getEmail2());
		obj.setEstadoCivil(this.getEstadoCivil());
		obj.setEstadoEmissor(this.getEstadoEmissaoRG());
		obj.setNumero(this.getNumero());
		obj.setOrgaoEmissor(this.getOrgaoEmissor());
		obj.setNacionalidade(this.getNacionalidade());
		obj.setNaturalidade(this.getNaturalidade());
		obj.setNomeEmpresa(this.getNomeEmpresa());
		for (FiliacaoVO filiacao : this.getFiliacaoVOs()) {
			if (filiacao.getTipo().equals("PA")) {
				obj.setNomePai(filiacao.getNome());
			}
			if (filiacao.getTipo().equals("MA")) {
				obj.setNomeMae(filiacao.getNome());
			}
		}
		for (FormacaoAcademicaVO formacaoAcademicaVO : this.getFormacaoAcademicaVOs()) {
			formacaoAcademicaVO.setCodigo(0);
			formacaoAcademicaVO.setNovoObj(true);
			formacaoAcademicaVO.setProspectsVO(obj);
			obj.getFormacaoAcademicaVOs().add(formacaoAcademicaVO);
		}
		return obj;
	}

	/**
	 * @param codProspect the codProspect to set
	 */
	public void setCodProspect(Integer codProspect) {
		this.codProspect = codProspect;
	}

	/**
	 * @return the gerenciaPreInscricao
	 */
	@XmlElement(name = "gerenciaPreInscricao")
	public Boolean getGerenciaPreInscricao() {
		if (gerenciaPreInscricao == null) {
			gerenciaPreInscricao = Boolean.FALSE;
		}
		return gerenciaPreInscricao;
	}

	/**
	 * @param gerenciaPreInscricao the gerenciaPreInscricao to set
	 */
	public void setGerenciaPreInscricao(Boolean gerenciaPreInscricao) {
		this.gerenciaPreInscricao = gerenciaPreInscricao;
	}

	/**
	 * @return the pessoaPreInscricaoCursoVOs
	 */

	public List<PessoaPreInscricaoCursoVO> getPessoaPreInscricaoCursoVOs() {
		if (pessoaPreInscricaoCursoVOs == null) {
			pessoaPreInscricaoCursoVOs = new ArrayList();
		}
		return pessoaPreInscricaoCursoVOs;
	}

	/**
	 * @param pessoaPreInscricaoCursoVOs the pessoaPreInscricaoCursoVOs to set
	 */
	public void setPessoaPreInscricaoCursoVOs(List<PessoaPreInscricaoCursoVO> pessoaPreInscricaoCursoVOs) {
		this.pessoaPreInscricaoCursoVOs = pessoaPreInscricaoCursoVOs;
	}

	@XmlElement(name = "curriculoAtualizado")
	public Boolean getCurriculoAtualizado() {
		if (curriculoAtualizado == null) {
			curriculoAtualizado = Boolean.FALSE;
		}
		return curriculoAtualizado;
	}

	public void setCurriculoAtualizado(Boolean curriculoAtualizado) {
		this.curriculoAtualizado = curriculoAtualizado;
	}

	/**
	 * @return the documetacaoPessoaVOs
	 */

	public List getDocumetacaoPessoaVOs() {
		if (documetacaoPessoaVOs == null) {
			documetacaoPessoaVOs = new ArrayList();
		}
		return documetacaoPessoaVOs;
	}

	/**
	 * @param documetacaoPessoaVOs the documetacaoPessoaVOs to set
	 */
	public void setDocumetacaoPessoaVOs(List documetacaoPessoaVOs) {
		this.documetacaoPessoaVOs = documetacaoPessoaVOs;
	}

	/**
	 * @return the pispasep
	 */
	@XmlElement(name = "pispasep")
	public String getPispasep() {
		if (pispasep == null) {
			pispasep = Constantes.EMPTY;
		}
		return pispasep;
	}

	/**
	 * @param pispasep the pispasep to set
	 */
	public void setPispasep(String pispasep) {
		this.pispasep = pispasep;
	}

	@XmlElement(name = "possuiAcessoVisaoPais")
	public Boolean getPossuiAcessoVisaoPais() {
		if (possuiAcessoVisaoPais == null) {
			possuiAcessoVisaoPais = Boolean.FALSE;
		}
		return possuiAcessoVisaoPais;
	}

	public void setPossuiAcessoVisaoPais(Boolean possuiAcessoVisaoPais) {
		this.possuiAcessoVisaoPais = possuiAcessoVisaoPais;
	}

	@XmlElement(name = "cepFiador")
	public String getCepFiador() {
		if (cepFiador == null) {
			cepFiador = Constantes.EMPTY;
		}
		return cepFiador;
	}

	public void setCepFiador(String cepFiador) {
		this.cepFiador = cepFiador;
	}

	@XmlElement(name = "setorFiador")
	public String getSetorFiador() {
		if (setorFiador == null) {
			setorFiador = Constantes.EMPTY;
		}
		return setorFiador;
	}

	public void setSetorFiador(String setorFiador) {
		this.setorFiador = setorFiador;
	}

	@XmlElement(name = "cidadeFiador")
	public CidadeVO getCidadeFiador() {
		if (cidadeFiador == null) {
			cidadeFiador = new CidadeVO();
		}
		return cidadeFiador;
	}

	public void setCidadeFiador(CidadeVO cidadeFiador) {
		this.cidadeFiador = cidadeFiador;
	}

	@XmlElement(name = "numeroEndFiador")
	public String getNumeroEndFiador() {
		if (numeroEndFiador == null) {
			numeroEndFiador = Constantes.EMPTY;
		}
		return numeroEndFiador;
	}

	public void setNumeroEndFiador(String numeroEndFiador) {
		this.numeroEndFiador = numeroEndFiador;
	}

	@XmlElement(name = "complementoFiador")
	public String getComplementoFiador() {
		if (complementoFiador == null) {
			complementoFiador = Constantes.EMPTY;
		}
		return complementoFiador;
	}

	public void setComplementoFiador(String complementoFiador) {
		this.complementoFiador = complementoFiador;
	}

	public PessoaVO getClone() throws Exception {
		PessoaVO obj = (PessoaVO) super.clone();
		return obj;
	}

	public Double getPorcentagemPreenchidaFormacaoAcademica() {
		if (!this.getFormacaoAcademicaVOs().isEmpty()) {
			return 25.0;
		}
		return 0.0;
	}

	public Double getPorcentagemPreenchidaDadosComerciais() {
		if (!this.getDadosComerciaisVOs().isEmpty()) {
			return 25.0;
		}
		return 0.0;
	}

	public Double getPorcentagemPreenchidaFormacaoExtraCurricular() {
		if (!this.getFormacaoExtraCurricularVOs().isEmpty()) {
			return 25.0;
		}
		return 0.0;
	}

	public Double getPorcentagemPreenchidaIdioma() {
		// idioma
		if (this.getIngles().booleanValue() || this.getEspanhol().booleanValue() || this.getFrances().booleanValue()
				|| !this.getOutrosIdiomas().equals(Constantes.EMPTY)) {
			return 12.5;
		}
		return 0.0;
	}

	public Double getPorcentagemPreenchidaSoftware() {
		// software
		if (this.getWindows().booleanValue() || this.getWord().booleanValue() || this.getExcel().booleanValue()
				|| this.getAccess().booleanValue() || this.getPowerPoint().booleanValue()
				|| this.getInternet().booleanValue() || this.getSap().booleanValue()
				|| this.getCorelDraw().booleanValue() || this.getAutoCad().booleanValue()
				|| this.getPhotoshop().booleanValue() || this.getMicrosiga().booleanValue()
				|| !this.getOutrosSoftwares().equals(Constantes.EMPTY)) {
			return 12.5;
		}
		return 0.0;
	}

	public Double getPorcentagemPreenchidaAreaProfissionalInteresse() {
		if (!this.getAreaProfissionalInteresseContratacaoVOs().isEmpty()) {
			return 12.5;
		}
		return 0.0;
	}

	public Double getPorcentagemPreenchidaQualificacoesAtividadeComplementar() {
		if (!this.getInformacoesAdicionais().equals(Constantes.EMPTY)) {
			return 12.5;
		}
		return 0.0;
	}

	public Double getPorcentagemPreenchidaDadosBasico() {
		return 25.0;
	}

	public Double getPorcentagemPreenchidaCurriculo() {
		double porcentagem = 0.0;
		porcentagem = porcentagem + getPorcentagemPreenchidaDadosBasico();
		porcentagem = porcentagem + getPorcentagemPreenchidaFormacaoAcademica();
		porcentagem = porcentagem + getPorcentagemPreenchidaDadosComerciais();
		porcentagem = porcentagem + getPorcentagemPreenchidaFormacaoExtraCurricular();
//        porcentagem = porcentagem + getPorcentagemPreenchidaIdioma();
//        porcentagem = porcentagem + getPorcentagemPreenchidaSoftware();
//        porcentagem = porcentagem + getPorcentagemPreenchidaAreaProfissionalInteresse();
//        porcentagem = porcentagem + getPorcentagemPreenchidaQualificacoesAtividadeComplementar();
		return porcentagem;
	}

	@XmlElement(name = "requisitante")
	public Boolean getRequisitante() {
		if (requisitante == null) {
			requisitante = Boolean.FALSE;
		}
		return requisitante;
	}

	public void setRequisitante(Boolean requisitante) {
		this.requisitante = requisitante;
	}

	@XmlElement(name = "isentarTaxaBoleto")
	public Boolean getIsentarTaxaBoleto() {
		if (isentarTaxaBoleto == null) {
			isentarTaxaBoleto = false;
		}
		return isentarTaxaBoleto;
	}

	public void setIsentarTaxaBoleto(Boolean isentarTaxaBoleto) {
		this.isentarTaxaBoleto = isentarTaxaBoleto;
	}

	@XmlElement(name = "ocultarDadosCRM")
	public Boolean getOcultarDadosCRM() {
		if (ocultarDadosCRM == null) {
			ocultarDadosCRM = true;
		}
		return ocultarDadosCRM;
	}

	public void setOcultarDadosCRM(Boolean ocultarDadosCRM) {
		this.ocultarDadosCRM = ocultarDadosCRM;
	}

	@XmlElement(name = "gravida")
	public Boolean getGravida() {
		if (gravida == null) {
			gravida = Boolean.FALSE;
		}
		return gravida;
	}

	public void setGravida(Boolean gravida) {
		this.gravida = gravida;
	}

	@XmlElement(name = "canhoto")
	public Boolean getCanhoto() {
		if (canhoto == null) {
			canhoto = Boolean.FALSE;
		}
		return canhoto;
	}

	public void setCanhoto(Boolean canhoto) {
		this.canhoto = canhoto;
	}

	@XmlElement(name = "portadorNecessidadeEspecial")
	public Boolean getPortadorNecessidadeEspecial() {
		if (portadorNecessidadeEspecial == null) {
			portadorNecessidadeEspecial = Boolean.FALSE;
		}
		return portadorNecessidadeEspecial;
	}

	public void setPortadorNecessidadeEspecial(Boolean portadorNecessidadeEspecial) {
		this.portadorNecessidadeEspecial = portadorNecessidadeEspecial;
	}

	public List<CurriculumPessoaVO> getCurriculumPessoaVOs() {
		if (curriculumPessoaVOs == null) {
			curriculumPessoaVOs = new ArrayList<CurriculumPessoaVO>(0);
		}
		return curriculumPessoaVOs;
	}

	public void setCurriculumPessoaVOs(List<CurriculumPessoaVO> curriculumPessoaVOs) {
		this.curriculumPessoaVOs = curriculumPessoaVOs;
	}

	public List<CurriculumPessoaVO> getCurriculumPessoaExcluir() {
		if (curriculumPessoaExcluir == null) {
			curriculumPessoaExcluir = new ArrayList<CurriculumPessoaVO>(0);
		}
		return curriculumPessoaExcluir;
	}

	public void setCurriculumPessoaExcluir(List<CurriculumPessoaVO> curriculumPessoaExcluir) {
		this.curriculumPessoaExcluir = curriculumPessoaExcluir;
	}

	/***
	 * Utilizado somente para notificacao de mensagem de reposicao de aula
	 */
	private String disciplinas;

	@XmlElement(name = "disciplinas")
	public String getDisciplinas() {
		if (disciplinas == null) {
			disciplinas = Constantes.EMPTY;
		}
		return disciplinas;
	}

	public void setDisciplinas(String disciplinas) {
		this.disciplinas = disciplinas;
	}

	/**
	 * @return the qtdVagasCandidatou
	 */
	@XmlElement(name = "qtdVagasCandidatou")
	public Integer getQtdVagasCandidatou() {
		if (qtdVagasCandidatou == null) {
			qtdVagasCandidatou = 0;
		}
		return qtdVagasCandidatou;
	}

	/**
	 * @param qtdVagasCandidatou the qtdVagasCandidatou to set
	 */
	public void setQtdVagasCandidatou(Integer qtdVagasCandidatou) {
		this.qtdVagasCandidatou = qtdVagasCandidatou;
	}

	public String getResponsavelFinanceiro() {
		if (!this.getFiliacaoVOs().isEmpty()) {
			Iterator i = this.getFiliacaoVOs().iterator();
			while (i.hasNext()) {
				FiliacaoVO objExistente = (FiliacaoVO) i.next();
				if (objExistente.getResponsavelFinanceiro()) {
					return objExistente.getPais().getNome();
				}
			}
		}
		return Constantes.EMPTY;
	}

	public String getDataExpedicaoTituloEleitoral_Apresentar() {
		return (Uteis.getData(dataExpedicaoTituloEleitoral));
	}

	@XmlElement(name = "dataExpedicaoTituloEleitoral")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataExpedicaoTituloEleitoral() {
		return dataExpedicaoTituloEleitoral;
	}

	public void setDataExpedicaoTituloEleitoral(Date dataExpedicaoTituloEleitoral) {
		this.dataExpedicaoTituloEleitoral = dataExpedicaoTituloEleitoral;
	}

	@XmlElement(name = "zonaEleitoral")
	public String getZonaEleitoral() {
		if (zonaEleitoral == null) {
			zonaEleitoral = Constantes.EMPTY;
		}
		return zonaEleitoral;
	}

	public void setZonaEleitoral(String zonaEleitoral) {
		this.zonaEleitoral = zonaEleitoral;
	}

	public String getDataExpedicaoCertificadoMilitar_Apresentar() {
		return Uteis.getData(dataExpedicaoCertificadoMilitar);
	}

	@XmlElement(name = "dataExpedicaoCertificadoMilitar")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataExpedicaoCertificadoMilitar() {
		return dataExpedicaoCertificadoMilitar;
	}

	public void setDataExpedicaoCertificadoMilitar(Date dataExpedicaoCertificadoMilitar) {
		this.dataExpedicaoCertificadoMilitar = dataExpedicaoCertificadoMilitar;
	}

	@XmlElement(name = "orgaoExpedidorCertificadoMilitar")
	public String getOrgaoExpedidorCertificadoMilitar() {
		if (orgaoExpedidorCertificadoMilitar == null) {
			orgaoExpedidorCertificadoMilitar = Constantes.EMPTY;
		}
		return orgaoExpedidorCertificadoMilitar;
	}

	public void setOrgaoExpedidorCertificadoMilitar(String orgaoExpedidorCertificadoMilitar) {
		this.orgaoExpedidorCertificadoMilitar = orgaoExpedidorCertificadoMilitar;
	}

	@XmlElement(name = "urlFoto")
	public String getUrlFotoAluno() {
		return urlFotoAluno;
	}

	public void setUrlFotoAluno(String urlFotoAluno) {
		this.urlFotoAluno = urlFotoAluno;
	}

	@XmlElement(name = "situacaoMilitar")
	public SituacaoMilitarEnum getSituacaoMilitar() {
		if (situacaoMilitar == null) {
			return SituacaoMilitarEnum.ISENTO;
		}
		return situacaoMilitar;
	}

	public void setSituacaoMilitar(SituacaoMilitarEnum situacaoMilitar) {
		this.situacaoMilitar = situacaoMilitar;
	}

	@XmlElement(name = "dadosPessoaisAtualizado")
	public Boolean getDadosPessoaisAtualizado() {
		if (dadosPessoaisAtualizado == null) {
			dadosPessoaisAtualizado = Boolean.FALSE;
		}
		return dadosPessoaisAtualizado;
	}

	public void setDadosPessoaisAtualizado(Boolean dadosPessoaisAtualizado) {
		this.dadosPessoaisAtualizado = dadosPessoaisAtualizado;
	}

	public Boolean getRenovacaoAutomatica() {
		if (renovacaoAutomatica == null) {
			renovacaoAutomatica = Boolean.FALSE;
		}
		return renovacaoAutomatica;
	}

	public void setRenovacaoAutomatica(Boolean renovacaoAutomatica) {
		this.renovacaoAutomatica = renovacaoAutomatica;
	}

	public PessoaVO getPessoaVO(IntegracaoPessoaVO intP) {
		PessoaVO p = new PessoaVO();
		p.setNome(intP.getNome());
		p.setEndereco(intP.getEndereco());
		p.setSetor(intP.getSetor());
		p.setNumero(intP.getNumero());
		p.setCEP(intP.getCEP());
		p.setComplemento(intP.getComplemento());
		p.setSexo(intP.getSexo());
		p.setEstadoCivil(intP.getEstadoCivil());
		p.setTelefoneComer(intP.getTelefoneComer());
		p.setTelefoneRes(intP.getTelefoneRes());
		p.setTelefoneRecado(intP.getTelefoneRecado());
		p.setCelular(intP.getCelular());
		p.setEmail(intP.getEmail());
		p.setEmail2(intP.getEmail2());
		p.setCorRaca(intP.getCorraca());
		p.setPaginaPessoal(intP.getPaginaPessoal());
//		p.setDataNasc(intP.getDataNasc());
		String dataNasc = intP.getDataNasc();
		if (dataNasc.indexOf("T") != -1) {
			dataNasc = dataNasc.substring(0, dataNasc.indexOf("T"));
			p.setDataNasc(Uteis.getDataYYYMMDD(dataNasc));
		}
		p.setCPF(intP.getCPF());
		p.setRG(intP.getRG());
		p.setCertificadoMilitar(intP.getCertificadoMilitar());
		p.setPispasep(intP.getPispasep());
		// p.setDataEmissaoRG(intP.getDataEmissaoRG());
		String dataEmissao = intP.getDataEmissaoRG();
		if (dataEmissao.indexOf("T") != -1) {
			dataEmissao = dataEmissao.substring(0, dataEmissao.indexOf("T"));
			p.setDataEmissaoRG(Uteis.getDataYYYMMDD(dataEmissao));
		}
		p.setEstadoEmissaoRG(intP.getEstadoEmissaoRG());
		p.setOrgaoEmissor(intP.getOrgaoEmissor());
		p.setTituloEleitoral(intP.getTituloEleitoral());
		p.setNecessidadesEspeciais(intP.getNecessidadesEspeciais());
//	    private List<IntegracaoFormacaoAcademicaVO> formacaoAcademicaVOs;
		// p.getCidade().setCodigo(intP.getCodigoCidade());
		p.getNacionalidade().setNome(intP.getNomeNacionalidade());
		p.getCidade().setNome(intP.getNomeCidade());
		p.getCidade().setCodigoIBGE(intP.getCodigoIBGECidade());
		p.getCidade().getEstado().setSigla(intP.getSiglaEstado());
		p.getNaturalidade().setCodigoIBGE(intP.getCodigoIBGENaturalidade());
		p.getNaturalidade().setNome(intP.getNomeNaturalidade());
		if (!intP.getFiliacaoVOs().isEmpty()) {
			for (IntegracaoFiliacaoVO intFil : intP.getFiliacaoVOs()) {
				FiliacaoVO filiacao = new FiliacaoVO();
				filiacao.getPais().setCelular(intFil.getCelular());
				String cep = Uteis
						.adicionarMascaraCEPConformeTamanhoCampo(Uteis.removerCaracteresEspeciais3(intFil.getCEP()));
				filiacao.getPais().setCEP(cep);

				filiacao.getPais().getCidade().setCodigoIBGE(intFil.getCodigoCidadeIBGE());

				filiacao.getPais().setComplemento(intFil.getComplemento());
				filiacao.getPais().setCPF(intFil.getCPF());
				String dataInicio = intFil.getDataNasc();
				if (dataInicio.indexOf("T") != -1) {
					dataInicio = dataInicio.substring(0, dataInicio.indexOf("T"));
					filiacao.getPais().setDataNasc(Uteis.getDataYYYMMDD(dataInicio));
				}
				filiacao.getPais().setEndereco(intFil.getEndereco());
				filiacao.getPais().setNome(intFil.getNome());
				filiacao.getPais().setRG(intFil.getRG());
				filiacao.getPais().setSetor(intFil.getSetor());
				filiacao.getPais().setTelefoneComer(intFil.getTelComercial());
				filiacao.getPais().setTelefoneRes(intFil.getTelResidencial());
				filiacao.getPais().setTelefoneRecado(intFil.getTelRecado());
				filiacao.setTipo(intFil.getTipo());
				if (intFil.getCEP().equals(Constantes.EMPTY)) {
					String cep2 = Uteis
							.adicionarMascaraCEPConformeTamanhoCampo(Uteis.removerCaracteresEspeciais3(p.getCEP()));
					filiacao.getPais().setCEP(cep2);
				}
				try {
					p.adicionarObjFiliacaoVOs(filiacao);
				} catch (Exception e) {

				}
			}
		}
		if (!intP.getFormacaoAcademicaVOs().isEmpty()) {
			for (IntegracaoFormacaoAcademicaVO intFor : intP.getFormacaoAcademicaVOs()) {
				FormacaoAcademicaVO formacao = new FormacaoAcademicaVO();
				formacao.getCidade().setCodigoIBGE(intFor.getCodigoCidadeIBGE());
				formacao.setCurso(intFor.getCurso());
				String dataInicio = intFor.getDataInicio();
				if (dataInicio.indexOf("T") != -1) {
					dataInicio = dataInicio.substring(0, dataInicio.indexOf("T"));
					formacao.setDataInicio(Uteis.getDataYYYMMDD(dataInicio));
				} else if (!dataInicio.equals(Constantes.EMPTY)) {
					formacao.setDataInicio(Uteis.getDataYYYMMDD(dataInicio));
				}

				String dataFim = intFor.getDataFim();
				if (dataFim.indexOf("T") != -1) {
					dataFim = dataFim.substring(0, dataFim.indexOf("T"));
					formacao.setDataFim(Uteis.getDataYYYMMDD(dataFim));
					formacao.setAnoDataFim(Uteis.getAno(formacao.getDataFim()));
				} else if (!dataFim.equals(Constantes.EMPTY)) {
					formacao.setDataFim(Uteis.getDataYYYMMDD(dataFim));
					formacao.setAnoDataFim(Uteis.getAno(formacao.getDataFim()));
				}
				String anoDataFim = intFor.getAnoDataFim();
				if (!anoDataFim.equals(Constantes.EMPTY)) {
					formacao.setAnoDataFim(anoDataFim);
				}
				formacao.setDescricaoAreaConhecimento(intFor.getDescricaoAreaConhecimento());
				formacao.setEscolaridade(intFor.getEscolaridade());
				formacao.setInstituicao(intFor.getInstituicao());
				formacao.setSituacao(intFor.getSituacao());
				formacao.setTipoInst(intFor.getTipoInst());
				try {
					p.adicionarObjFormacaoAcademicaVOs(formacao);
				} catch (Exception e) {

				}
			}
		}
		return p;
	}

	public PessoaVO getResponsavelFinanceiroAluno() {
		Iterator i = getFiliacaoVOs().iterator();
		while (i.hasNext()) {
			FiliacaoVO f = (FiliacaoVO) i.next();
			if (f.getResponsavelFinanceiro().booleanValue()) {
				return f.getPais();
			}
		}
		return new PessoaVO();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaVO other = (PessoaVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public String getNomePai() {
		for (FiliacaoVO filiacao : getFiliacaoVOs()) {
			if ("PA".equals(filiacao.getTipo())) {
				return filiacao.getNome().trim();
			}
		}
		return Constantes.EMPTY;
	}

	@XmlElement(name = "incluirProspect")
	public Boolean getIncluirProspect() {
		if (incluirProspect == null) {
			incluirProspect = Boolean.TRUE;
		}
		return incluirProspect;
	}

	public void setIncluirProspect(Boolean incluirProspect) {
		this.incluirProspect = incluirProspect;
	}

	public void inicializarDadosNecessidadeGerarProspectPelaConfiguracaoGeralSistemaTipoOrigem(
			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String tipoOrigem) {
		if (tipoOrigem.equals("ALUNO")) {
			if (configuracaoGeralSistemaVO.getCriarProspectAluno()) {
				setIncluirProspect(true);
			} else {
				setIncluirProspect(false);
			}
		} else if (tipoOrigem.equals("FILIACAO")) {
			if (configuracaoGeralSistemaVO.getCriarProspectFiliacao()) {
				setIncluirProspect(true);
			} else {
				setIncluirProspect(false);
			}
		} else if (tipoOrigem.equals("CANDIDATO")) {
			if (configuracaoGeralSistemaVO.getCriarProspectCandidato()) {
				setIncluirProspect(true);
			} else {
				setIncluirProspect(false);
			}
		} else if (tipoOrigem.equals("FUNCIONARIO")) {
			if (configuracaoGeralSistemaVO.getCriarProspectFuncionario()) {
				setIncluirProspect(true);
			} else {
				setIncluirProspect(false);
			}
		}
	}
	
	

	public TipoAssinaturaDocumentoEnum getTipoAssinaturaDocumentoEnum() {
		if (tipoAssinaturaDocumentoEnum == null) {
			tipoAssinaturaDocumentoEnum = TipoAssinaturaDocumentoEnum.ELETRONICA;
		}
		return tipoAssinaturaDocumentoEnum;
	}

	public void setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum tipoAssinaturaDocumentoEnum) {
		this.tipoAssinaturaDocumentoEnum = tipoAssinaturaDocumentoEnum;
	}

	@XmlElement(name = "senhaCertificadoParaDocumento")
	public String getSenhaCertificadoParaDocumento() {
		return senhaCertificadoParaDocumento;
	}

	public void setSenhaCertificadoParaDocumento(String senhaCertificadoParaDocumento) {
		this.senhaCertificadoParaDocumento = senhaCertificadoParaDocumento;
	}

	@XmlElement(name = "gerarProspectInativo")
	public Boolean getGerarProspectInativo() {
		if (gerarProspectInativo == null) {
			gerarProspectInativo = Boolean.FALSE;
		}
		return gerarProspectInativo;
	}

	public void setGerarProspectInativo(Boolean gerarProspectInativo) {
		this.gerarProspectInativo = gerarProspectInativo;
	}

	@XmlElement(name = "nomeCenso")
	public String getNomeCenso() {
		if (nomeCenso == null) {
			nomeCenso = Constantes.EMPTY;
		}
		return nomeCenso;
	}

	public void setNomeCenso(String nomeCenso) {
		this.nomeCenso = nomeCenso;
	}

	@XmlElement(name = "dataUltimaAtualizacaoCadatral")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataUltimaAtualizacaoCadatral() {
		return dataUltimaAtualizacaoCadatral;
	}

	public void setDataUltimaAtualizacaoCadatral(Date dataUltimaAtualizacaoCadatral) {
		this.dataUltimaAtualizacaoCadatral = dataUltimaAtualizacaoCadatral;
	}

	@XmlElement(name = "registrarLogAtualizacaoCadastral")
	public Boolean getRegistrarLogAtualizacaoCadastral() {
		if (registrarLogAtualizacaoCadastral == null) {
			registrarLogAtualizacaoCadastral = Boolean.FALSE;
		}
		return registrarLogAtualizacaoCadastral;
	}

	public void setRegistrarLogAtualizacaoCadastral(Boolean registrarLogAtualizacaoCadastral) {
		this.registrarLogAtualizacaoCadastral = registrarLogAtualizacaoCadastral;
	}

	@XmlElement(name = "permiteEnviarRemessa")
	public boolean isPermiteEnviarRemessa() {
		return permiteEnviarRemessa;
	}

	public void setPermiteEnviarRemessa(boolean permiteEnviarRemessa) {
		this.permiteEnviarRemessa = permiteEnviarRemessa;
	}

	public void limparDadosSegundoRegraAtualizacaoCadastral(ConfiguracaoAtualizacaoCadastralVO config) {
		if (config.getHabilitarRecursoAtualizacaoCadastral()) {
			if (config.getTrazerDadoContatoEmBranco()) {
				setCelular(Constantes.EMPTY);
				setTelefoneComer(Constantes.EMPTY);
				setTelefoneRes(Constantes.EMPTY);
				setTelefoneRecado(Constantes.EMPTY);
				setEmail(Constantes.EMPTY);
			}
			if (config.getTrazerDadoEnderecoEmBranco()) {
				setCEP(Constantes.EMPTY);
				setEndereco(Constantes.EMPTY);
				setNumero(Constantes.EMPTY);
				setSetor(Constantes.EMPTY);
				setComplemento(Constantes.EMPTY);
				setCidade(null);
			}
			if (config.getTrazerDadoCertidaoNascEmBranco() && config.getApresentarCampoCertidaoNasc()) {
				setCertidaoNascimento(Constantes.EMPTY);
			}
		}
	}

	public String obterStringLogAlteracaoCadastral(PessoaVO obj) {
		String log = Constantes.EMPTY;
		if (!obj.getNome().equals(this.getNome())) {
			log += " -> Nome: " + this.getNome();
		}
		if (!obj.getEndereco().equals(this.getEndereco())) {
			log += " -> Endereço: " + this.getEndereco();
		}
		if (!obj.getSetor().equals(this.getSetor())) {
			log += " -> Setor: " + this.getSetor();
		}
		if (!obj.getNumero().equals(this.getNumero())) {
			log += " -> Número: " + this.getSetor();
		}
		if (!obj.getCEP().equals(this.getCEP())) {
			log += " -> CEP: " + this.getCEP();
		}
		if (!obj.getComplemento().equals(this.getComplemento())) {
			log += " -> Complemento: " + this.getComplemento();
		}
		if (!obj.getSexo().equals(this.getSexo())) {
			log += " -> Sexo: " + this.getSexo();
		}
		if (!obj.getEstadoCivil().equals(this.getEstadoCivil())) {
			log += " -> Estado Civil: " + this.getEstadoCivil();
		}
		if (!obj.getTelefoneComer().equals(this.getTelefoneComer())) {
			log += " -> Telefone Comer: " + this.getTelefoneComer();
		}
		if (!obj.getTelefoneRes().equals(this.getTelefoneRes())) {
			log += " -> Telefone Res: " + this.getTelefoneRes();
		}
		if (!obj.getTelefoneRecado().equals(this.getTelefoneRecado())) {
			log += " -> Telefone Recado: " + this.getTelefoneRecado();
		}
		if (!obj.getCelular().equals(this.getCelular())) {
			log += " -> Celular: " + this.getCelular();
		}
		if (!obj.getEmail().equals(this.getEmail())) {
			log += " -> Email: " + this.getEmail();
		}
		if (!obj.getEmail2().equals(this.getEmail2())) {
			log += " -> Email2: " + this.getEmail2();
		}
		if (!obj.getPaginaPessoal().equals(this.getPaginaPessoal())) {
			log += " -> Pagina Pessoal: " + this.getPaginaPessoal();
		}
		if (!obj.getDataNasc_Apresentar().equals(this.getDataNasc_Apresentar())) {
			log += " -> Data Nasc: " + this.getDataNasc_Apresentar();
		}
		if (!obj.getCPF().equals(this.getCPF())) {
			log += " -> CPF: " + this.getCPF();
		}
		if (!obj.getRG().equals(this.getRG())) {
			log += " -> RG: " + this.getRG();
		}
		if (!obj.getCertificadoMilitar().equals(this.getCertificadoMilitar())) {
			log += " -> Certificado Militar: " + this.getCertificadoMilitar();
		}
		if (!obj.getPispasep().equals(this.getPispasep())) {
			log += " -> Pispasep: " + this.getPispasep();
		}
		if (!obj.getDataEmissaoRG_Apresentar().equals(this.getDataEmissaoRG_Apresentar())) {
			log += " -> Data Emissão RG: " + this.getDataEmissaoRG_Apresentar();
		}
		if (!obj.getEstadoEmissaoRG().equals(this.getEstadoEmissaoRG())) {
			log += " -> Estado Emissão RG: " + this.getEstadoEmissaoRG();
		}
		if (!obj.getOrgaoEmissor().equals(this.getOrgaoEmissor())) {
			log += " -> Orgão Emissor RG: " + this.getOrgaoEmissor();
		}
		if (!obj.getTituloEleitoral().equals(this.getTituloEleitoral())) {
			log += " -> Titulo Eleitoral: " + this.getTituloEleitoral();
		}
		if (!obj.getNecessidadesEspeciais().equals(this.getNecessidadesEspeciais())) {
			log += " -> Necessidades Esp.: " + this.getNecessidadesEspeciais();
		}
		if (!obj.getNecessidadesEspeciais().equals(this.getNecessidadesEspeciais())) {
			log += " -> Necessidades Esp.: " + this.getNecessidadesEspeciais();
		}
		if (!obj.getTipoNecessidadesEspeciais().equals(this.getTipoNecessidadesEspeciais())) {
			log += " -> Tipo Necessidades Esp.: " + this.getTipoNecessidadesEspeciais();
		}
		if (!obj.getNomeEmpresa().equals(this.getNomeEmpresa())) {
			log += " -> Nome Empresa: " + this.getNomeEmpresa();
		}
		if (!obj.getEnderecoEmpresa().equals(this.getEnderecoEmpresa())) {
			log += " -> Endereço Empresa: " + this.getEnderecoEmpresa();
		}
		if (!obj.getCargoPessoaEmpresa().equals(this.getCargoPessoaEmpresa())) {
			log += " -> Cargo Empresa: " + this.getCargoPessoaEmpresa();
		}
		if (!obj.getCepEmpresa().equals(this.getCepEmpresa())) {
			log += " -> Cep Empresa: " + this.getCepEmpresa();
		}
		if (!obj.getComplementoEmpresa().equals(this.getComplementoEmpresa())) {
			log += " -> Complemento Empresa: " + this.getComplementoEmpresa();
		}
		if (!obj.getSetorEmpresa().equals(this.getSetorEmpresa())) {
			log += " -> Setor Empresa: " + this.getSetorEmpresa();
		}
		if (obj.getCidadeEmpresa().getCodigo().intValue() != this.getCidadeEmpresa().getCodigo().intValue()) {
			log += " -> Cidade Empresa: " + this.getCidadeEmpresa().getCodigo();
		}
		if (!obj.getCertidaoNascimento().equals(this.getCertidaoNascimento())) {
			log += " -> Certidão Nasc: " + this.getCertidaoNascimento();
		}
		if (!obj.getDataExpedicaoTituloEleitoral_Apresentar()
				.equals(this.getDataExpedicaoTituloEleitoral_Apresentar())) {
			log += " -> Data Exp. Titulo Eleitoral: " + this.getDataExpedicaoTituloEleitoral_Apresentar();
		}
		if (!obj.getZonaEleitoral().equals(this.getZonaEleitoral())) {
			log += " -> Zona Eleitoral: " + this.getZonaEleitoral();
		}
		if (!obj.getDataExpedicaoCertificadoMilitar_Apresentar()
				.equals(this.getDataExpedicaoCertificadoMilitar_Apresentar())) {
			log += " -> Data Exp. Certificado Militar: " + this.getDataExpedicaoCertificadoMilitar_Apresentar();
		}
		if (!obj.getOrgaoExpedidorCertificadoMilitar().equals(this.getOrgaoExpedidorCertificadoMilitar())) {
			log += " -> Orgão Exp. Certificado Militar: " + this.getOrgaoExpedidorCertificadoMilitar();
		}
		if (!obj.getSituacaoMilitar().getValorApresentar().equals(this.getSituacaoMilitar().getValorApresentar())) {
			log += " -> Situação Militar: " + this.getSituacaoMilitar().getValorApresentar();
		}
		if (obj.getCidade().getCodigo().intValue() != this.getCidade().getCodigo().intValue()) {
			log += " -> Cidade: " + this.getCidade().getCodigo();
		}
		if (obj.getNaturalidade().getCodigo().intValue() != this.getNaturalidade().getCodigo().intValue()) {
			log += " -> Naturalidade: " + this.getNaturalidade().getCodigo();
		}
		if (obj.getNacionalidade().getCodigo().intValue() != this.getNacionalidade().getCodigo().intValue()) {
			log += " -> Nacionalidade: " + this.getNacionalidade().getCodigo();
		}
		if (!obj.getIdAlunoInep().equals(this.getIdAlunoInep())) {
			log += " -> IdAlunoInep: " + this.getIdAlunoInep();
		}
		if (!obj.getCorRaca().equals(this.getCorRaca())) {
			log += " -> Cor/Raça: " + this.getCorRaca();
		}
		if (!obj.getNomeFiador().equals(this.getNomeFiador())) {
			log += " -> Nome Fiador: " + this.getNomeFiador();
		}
		if (!obj.getEnderecoFiador().equals(this.getEnderecoFiador())) {
			log += " -> Endereço Fiador: " + this.getEnderecoFiador();
		}
		if (!obj.getTelefoneFiador().equals(this.getTelefoneFiador())) {
			log += " -> Telefone Fiador: " + this.getTelefoneFiador();
		}
		if (!obj.getCelularFiador().equals(this.getCelularFiador())) {
			log += " -> Celular Fiador: " + this.getCelularFiador();
		}
		if (!obj.getCpfFiador().equals(this.getCpfFiador())) {
			log += " -> CPF Fiador: " + this.getCpfFiador();
		}
		if (!obj.getCepFiador().equals(this.getCepFiador())) {
			log += " -> CEP Fiador: " + this.getCepFiador();
		}
		if (!obj.getSetorFiador().equals(this.getSetorFiador())) {
			log += " -> Setor Fiador: " + this.getSetorFiador();
		}
		if (!obj.getNumeroEndFiador().equals(this.getNumeroEndFiador())) {
			log += " -> Nr. End. Fiador: " + this.getNumeroEndFiador();
		}
		if (!obj.getComplementoFiador().equals(this.getComplementoFiador())) {
			log += " -> Complemento End. Fiador: " + this.getComplementoFiador();
		}
		if (obj.getCidadeFiador().getCodigo().intValue() != this.getCidadeFiador().getCodigo().intValue()) {
			log += " -> Cidade Fiador: " + this.getCidadeFiador().getCodigo();
		}
		String logFormacao = " -> Formação Acadêmica (";
		boolean achouFormacaoAlterada = false;
		Iterator i = this.getFormacaoAcademicaVOs().iterator();
		while (i.hasNext()) {
			FormacaoAcademicaVO formacao = (FormacaoAcademicaVO) i.next();
			Iterator j = obj.getFormacaoAcademicaVOs().iterator();
			while (j.hasNext()) {
				FormacaoAcademicaVO form = (FormacaoAcademicaVO) j.next();
				if (form.getCodigo().intValue() == formacao.getCodigo().intValue()) {
					if (!form.getInstituicao().equals(formacao.getInstituicao())) {
						logFormacao += " -> Instituição: " + formacao.getInstituicao();
						achouFormacaoAlterada = true;
					}
					if (!form.getEscolaridade().equals(formacao.getEscolaridade())) {
						logFormacao += " -> Escolaridade: " + formacao.getEscolaridade();
						achouFormacaoAlterada = true;
					}
					if (!form.getCurso().equals(formacao.getCurso())) {
						logFormacao += " -> Curso: " + formacao.getCurso();
						achouFormacaoAlterada = true;
					}
					if (!form.getSituacao().equals(formacao.getSituacao())) {
						logFormacao += " -> Situação: " + formacao.getSituacao();
						achouFormacaoAlterada = true;
					}
					if (!form.getTipoInst().equals(formacao.getTipoInst())) {
						logFormacao += " -> Tipo Instituição: " + formacao.getTipoInst();
						achouFormacaoAlterada = true;
					}
					if (!form.getDataInicio_Apresentar().equals(formacao.getDataInicio_Apresentar())) {
						logFormacao += " -> Data Inicio: " + formacao.getDataInicio_Apresentar();
						achouFormacaoAlterada = true;
					}
					if (!form.getDataFim_Apresentar().equals(formacao.getDataFim_Apresentar())) {
						logFormacao += " -> Data Fim: " + formacao.getDataFim_Apresentar();
						achouFormacaoAlterada = true;
					}
					if (!form.getAnoDataFim().equals(formacao.getAnoDataFim())) {
						logFormacao += " -> Ano Data Fim: " + formacao.getAnoDataFim();
						achouFormacaoAlterada = true;
					}
					if (form.getAreaConhecimento().getCodigo().intValue() != formacao.getAreaConhecimento().getCodigo()
							.intValue()) {
						logFormacao += " -> Área Conhecimento: " + formacao.getAreaConhecimento().getCodigo();
						achouFormacaoAlterada = true;
					}
					if (form.getCidade().getCodigo().intValue() != formacao.getCidade().getCodigo().intValue()) {
						logFormacao += " -> Cidade: " + formacao.getCidade().getCodigo();
						achouFormacaoAlterada = true;
					}
				}
			}
		}
		logFormacao += ") ";
		if (achouFormacaoAlterada) {
			log += logFormacao;
		}
//	    private List documetacaoPessoaVOs;
//	    private List<FiliacaoVO> filiacaoVOs;
//	    private List disponibilidadeHorarioVOs;
//	    private List<DadosComerciaisVO> dadosComerciaisVOs;
//	    private List<FormacaoExtraCurricularVO> formacaoExtraCurricularVOs;
//	    private List<AreaProfissionalInteresseContratacaoVO> areaProfissionalInteresseContratacaoVOs;
		return log;
	}

	@XmlElement(name = "grauParentesco")
	public String getGrauParentesco() {
		if (grauParentesco == null) {
			grauParentesco = Constantes.EMPTY;
		}
		return grauParentesco;
	}

	public void setGrauParentesco(String grauParentesco) {
		this.grauParentesco = grauParentesco;
	}

	/**
	 * 
	 * @param obj
	 * @throws ConsistirException
	 */
	public static void validarFiliacaoMaeObrigatorio(PessoaVO obj) throws ConsistirException {
		int cont = 0;
		if (!obj.getFiliacaoVOs().isEmpty()) {
			Iterator itens = obj.getFiliacaoVOs().iterator();
			while (itens.hasNext()) {
				FiliacaoVO objFiliacao = (FiliacaoVO) itens.next();
				if (objFiliacao.getTipo().equals("MA")) {
					cont++;
				}
			}
			if (cont == 0) {
				throw new ConsistirException("É obrigatório o cadastro da mãe.");
			}
		} else {
			throw new ConsistirException("É obrigatório o cadastro da mãe.");
		}
	}

	/**
	 * 
	 * @param obj
	 * @throws ConsistirException
	 */
	public static void validarFormacaoEnsinoMedioObrigatorio(PessoaVO obj) throws ConsistirException {
		int cont = 0;
		if (!obj.getFormacaoAcademicaVOs().isEmpty()) {
			Iterator itens = obj.getFormacaoAcademicaVOs().iterator();
			while (itens.hasNext()) {
				FormacaoAcademicaVO objFormacao = (FormacaoAcademicaVO) itens.next();
				if (objFormacao.getEscolaridade().equals("EM")) {
					if (objFormacao.getInstituicao().trim().isEmpty()) {
						throw new ConsistirException("O campo INSTITUIÇÃO ENSINO MÉDIO deve ser informado.");
					}
					if (objFormacao.getAnoDataFim().trim().isEmpty()) {
						throw new ConsistirException("O campo ANO CONCLUSÃO ENSINO MÉDIO deve ser informado.");
					}
					if (objFormacao.getTipoInst().trim().isEmpty()) {
						throw new ConsistirException("O campo TIPO INSTITUIÇÃO ENSINO MÉDIO deve ser informado.");
					}
					cont++;
				}
			}
			if (cont == 0) {
				throw new ConsistirException("É obrigatório o cadastro da formação do Ensino Médio.");
			}
		} else {
			throw new ConsistirException("É obrigatório o cadastro da formação do Ensino Médio.");
		}
	}

	/**
	 * // * Método para validação dos dados de um Candidato (PessoaVO) de acordo com
	 * as configurações (ConfiguracaoCandidatoProcessoSeletivoVO) definidas para a
	 * exibição e obrigatoriedade dos campos referentes à inscrição em um processo
	 * seletivo.
	 * 
	 * @param obj
	 * @param regras
	 * @throws ConsistirException
	 */
	public static void validarDadosCandidato(PessoaVO obj, boolean deveValidarCPF,
			ConfiguracaoCandidatoProcessoSeletivoVO regras) throws ConsistirException {
		if (obj.getNome().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo NOME SOCIAL deve ser informado.");
		}
		if (obj.getNomeBatismo().equals(Constantes.EMPTY) && regras.getNomeBatismoObrigatorio()) {
			throw new ConsistirException("O campo NOME BATISMO deve ser informado.");
		}
		if (obj.getCEP().equals(Constantes.EMPTY) && regras.getEnderecoObrigatorio()) {
			throw new ConsistirException("O campo CEP deve ser informado.");
		}
		if (obj.getEndereco().trim().equals(Constantes.EMPTY) && regras.getEnderecoObrigatorio()) {
			throw new ConsistirException("O campo ENDEREÇO deve ser informado.");
		}
		if (obj.getSetor().trim().equals(Constantes.EMPTY) && regras.getEnderecoObrigatorio()) {
			throw new ConsistirException("O campo SETOR deve ser informado.");
		}
		if (obj.getNumero().trim().equals(Constantes.EMPTY) && regras.getEnderecoObrigatorio()) {
			throw new ConsistirException("O campo NÚMERO deve ser informado.");
		}
		if (((obj.getCidade() == null) || (obj.getCidade().getCodigo().intValue() == 0))
				&& regras.getEnderecoObrigatorio()) {
			throw new ConsistirException("O campo CIDADE deve ser informado.");
		}
		if (obj.getTelefoneRes().equals(Constantes.EMPTY) && regras.getTelefoneResidencialObrigatorio()) {
			throw new ConsistirException("O campo TELEFONE RESIDENCIAL deve ser informado.");
		}
		if (obj.getTelefoneComer().equals(Constantes.EMPTY) && regras.getTelefoneComercialObrigatorio()) {
			throw new ConsistirException("O campo TELEFONE COMERCIAL deve ser informado.");
		}
		if (obj.getTelefoneRecado().equals(Constantes.EMPTY) && regras.getTelefoneRecadoObrigatorio()) {
			throw new ConsistirException("O campo TELEFONE RECADO deve ser informado.");
		}
		if (obj.getCelular().equals(Constantes.EMPTY) && regras.getTelefoneCelularObrigatorio()) {
			throw new ConsistirException("O campo CELULAR deve ser informado.");
		}
		if (obj.getEmail().equals(Constantes.EMPTY) && regras.getEmailObrigatorio()) {

			throw new ConsistirException("O campo EMAIL deve ser informado.");
		}
		if ((!Uteis.isAtributoPreenchido(obj.getDataNasc())) && regras.getDataNascimentoObrigatorio()) {
			throw new ConsistirException("O campo DATA NASCIMENTO deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(obj.getDataNasc()) && obj.getDataNasc().after(new Date())) {
			throw new ConsistirException("O campo DATA NASCIMENTO informado é inválido. Data futura.");
		}
		if (((obj.getSexo() == null) || (!Uteis.isAtributoPreenchido(obj.getSexo()))) && regras.getSexoObrigatorio()) {
			throw new ConsistirException("O campo SEXO deve ser informado.");
		}
		if (obj.getEstadoCivil().equals(Constantes.EMPTY) && regras.getEstadoCivilObrigatorio()) {
			throw new ConsistirException("O campo ESTADO CIVIL deve ser informado.");
		}
		if ((!Uteis.isAtributoPreenchido(obj.getNacionalidade())) && regras.getNacionalidadeObrigatorio()) {
			throw new ConsistirException("O campo NACIONALIDADE deve ser informado.");
		}
		if (obj.getNaturalidade().getNome().equals(Constantes.EMPTY) && regras.getNaturalidadeObrigatorio()) {
			throw new ConsistirException("O campo NATURALIDADE deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(obj.getPortadorNecessidadeEspecial()) && obj.getPortadorNecessidadeEspecial()
				&& !Uteis.isAtributoPreenchido(obj.getNecessidadesEspeciais())) {
			throw new ConsistirException("O campo NECESSIDADES ATENDIMENTO ESPECIALIZADO deve ser informado.");
		}
		if (obj.getCPF().equals(Constantes.EMPTY)) {
			throw new ConsistirException("O campo CPF  deve ser informado.");
		} else {
			if (deveValidarCPF) {
				Boolean validacpf = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
						.get("validarCPF");
				if (Uteis.isAtributoPreenchido(validacpf) && validacpf) {
					if (!Uteis.verificaCPF(obj.getCPF())) {
						throw new ConsistirException("O CPF não é VÁLIDO.");
					}
				}
			}
		}
		if (obj.getRG().equals(Constantes.EMPTY) && regras.getRgObrigatorio()) {
			throw new ConsistirException("O campo RG deve ser informado.");
		}
		if (obj.getDataEmissaoRG() == null && regras.getRgObrigatorio()) {
			throw new ConsistirException("O campo DATA EMISSAO DO RG deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(obj.getDataEmissaoRG()) && obj.getDataEmissaoRG().after(new Date())) {
			throw new ConsistirException("O campo DATA EMISSAO DO RG informado é inválido. Data futura.");
		}
		if (obj.getOrgaoEmissor().equals(Constantes.EMPTY) && regras.getRgObrigatorio()) {
			throw new ConsistirException("O campo ORGÃO EMISSOR DO RG deve ser informado.");
		}
		if (((!Uteis.isAtributoPreenchido(obj.getEstadoEmissaoRG())) || (obj.getEstadoEmissaoRG().equals(Constantes.EMPTY)))
				&& regras.getRgObrigatorio()) {
			throw new ConsistirException("O campo ESTADO EMISSOR DO RG deve ser informado.");
		}
		if (obj.getCertidaoNascimento().equals(Constantes.EMPTY) && regras.getCertidaoNascimentoObrigatorio()) {
			throw new ConsistirException("O campo Certidão Nascimento  deve ser informado.");
		}

		if (regras.getFormacaoEnsinoMedioObrigatorio()) {
			validarFormacaoEnsinoMedioObrigatorio(obj);
		}
		if (regras.getMaeFiliacaoObrigatorio()) {
			validarFiliacaoMaeObrigatorio(obj);
			validarDadosFiliacao(obj);
		}
	}

	@XmlElement(name = "listaPessoaGsuite")
	public List<PessoaGsuiteVO> getListaPessoaGsuite() {
		if (listaPessoaGsuite == null) {
			listaPessoaGsuite = new ArrayList<>();
		}
		return listaPessoaGsuite;
	}

	public void setListaPessoaGsuite(List<PessoaGsuiteVO> listaPessoaGsuite) {
		this.listaPessoaGsuite = listaPessoaGsuite;
	}

	public String listaPessoaGsuiteVO_ApresentarHtml;

	public void setListaPessoaGsuiteVO_ApresentarHtml(String listaPessoaGsuiteVO_ApresentarHtml) {
		this.listaPessoaGsuiteVO_ApresentarHtml = listaPessoaGsuiteVO_ApresentarHtml;
	}

	public String getListaPessoaGsuiteVO_ApresentarHtml() {
		if (listaPessoaGsuiteVO_ApresentarHtml == null) {
			listaPessoaGsuiteVO_ApresentarHtml = Constantes.EMPTY;
			StringBuilder sb = new StringBuilder(Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(getListaPessoaGsuite())) {
				for (PessoaGsuiteVO pessoaGsuite : getListaPessoaGsuite()) {
					sb.append(Constantes.EMPTY).append(pessoaGsuite.getEmail()).append(";");
				}
				listaPessoaGsuiteVO_ApresentarHtml = sb.toString().substring(0, sb.toString().length() - 1);
			}
		}
		return listaPessoaGsuiteVO_ApresentarHtml;
	}

	public List<PessoaEmailInstitucionalVO> getListaPessoaEmailInstitucionalVO() {
		if (listaPessoaEmailInstitucionalVO == null) {
			listaPessoaEmailInstitucionalVO = new ArrayList<PessoaEmailInstitucionalVO>(0);
		}
		return listaPessoaEmailInstitucionalVO;
	}

	public void setListaPessoaEmailInstitucionalVO(List<PessoaEmailInstitucionalVO> listaPessoaEmailInstitucionalVO) {
		this.listaPessoaEmailInstitucionalVO = listaPessoaEmailInstitucionalVO;
	}

	public String listaPessoaEmailInstitucionalVO_ApresentarHtml;
	public String listaPessoaEmailInstitucionalVOAtivo_ApresentarHtml;

	public void setListaPessoaEmailInstitucionalVO_ApresentarHtml(
			String listaPessoaEmailInstitucionalVO_ApresentarHtml) {
		this.listaPessoaEmailInstitucionalVO_ApresentarHtml = listaPessoaEmailInstitucionalVO_ApresentarHtml;
	}

	public String getListaPessoaEmailInstitucionalVO_ApresentarHtml() {
		if (listaPessoaEmailInstitucionalVO_ApresentarHtml == null) {
			listaPessoaEmailInstitucionalVO_ApresentarHtml = Constantes.EMPTY;
			StringBuilder sb = new StringBuilder(Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(getListaPessoaEmailInstitucionalVO())) {
				for (PessoaEmailInstitucionalVO pessoaEmail : getListaPessoaEmailInstitucionalVO()) {
					sb.append(Constantes.EMPTY).append(pessoaEmail.getEmail()).append(";");
				}
				listaPessoaEmailInstitucionalVO_ApresentarHtml = sb.toString().substring(0, sb.toString().length() - 1);
			}
		}
		return listaPessoaEmailInstitucionalVO_ApresentarHtml;
	}
	public String getListaPessoaEmailInstitucionalVOAtivo_ApresentarHtml() {
		if (listaPessoaEmailInstitucionalVOAtivo_ApresentarHtml == null) {
			listaPessoaEmailInstitucionalVOAtivo_ApresentarHtml = Constantes.EMPTY;
			StringBuilder sb = new StringBuilder(Constantes.EMPTY);
			if (Uteis.isAtributoPreenchido(getListaPessoaEmailInstitucionalVO())) {
				for (PessoaEmailInstitucionalVO pessoaEmail : getListaPessoaEmailInstitucionalVO()) {
					sb.append(Constantes.EMPTY).append(pessoaEmail.getEmail()).append(";");
				}
				listaPessoaEmailInstitucionalVOAtivo_ApresentarHtml = sb.toString().substring(0, sb.toString().length() - 1);
			}
		}
		return listaPessoaEmailInstitucionalVOAtivo_ApresentarHtml;
	}

	/**
	 * Transiente
	 */
	private UnidadeEnsinoVO unidadeEnsinoVO;

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	@XmlElement(name = "estadoCivilFiador")
	public String getEstadoCivilFiador() {
		if (estadoCivilFiador == null) {
			estadoCivilFiador = Constantes.EMPTY;
		}
		return estadoCivilFiador;
	}

	public void setEstadoCivilFiador(String estadoCivilFiador) {
		this.estadoCivilFiador = estadoCivilFiador;
	}

	@XmlElement(name = "rgFiador")
	public String getRgFiador() {
		if (rgFiador == null) {
			rgFiador = Constantes.EMPTY;
		}
		return rgFiador;
	}

	public void setRgFiador(String rgFiador) {
		this.rgFiador = rgFiador;
	}

	@XmlElement(name = "profissaoFiador")
	public String getProfissaoFiador() {
		if (profissaoFiador == null) {
			profissaoFiador = Constantes.EMPTY;
		}
		return profissaoFiador;
	}

	public void setProfissaoFiador(String profissaoFiador) {
		this.profissaoFiador = profissaoFiador;
	}

	@XmlElement(name = "dataNascimentoFiador")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataNascimentoFiador() {
		return dataNascimentoFiador;
	}

	public void setDataNascimentoFiador(Date dataNascimentoFiador) {
		this.dataNascimentoFiador = dataNascimentoFiador;
	}

	@XmlElement(name = "paisFiador")
	public PaizVO getPaisFiador() {
		if (paisFiador == null) {
			paisFiador = new PaizVO();
		}
		return paisFiador;
	}

	public void setPaisFiador(PaizVO paisFiador) {
		this.paisFiador = paisFiador;
	}

	public String getEstadoCivilFiadorApresentar() {
		String estadoCivilApresentar = null;
		switch (getEstadoCivilFiador()) {
		case "S":
			estadoCivilApresentar = "Solteiro(a)";
			break;
		case "C":
			estadoCivilApresentar = "Casado(a)";
			break;
		case "V":
			estadoCivilApresentar = "Viúvo(a)";
			break;
		case "D":
			estadoCivilApresentar = "Divorciado(a)";
			break;
		case "A":
			estadoCivilApresentar = "Amasiado(a)";
			break;
		case "U":
			estadoCivilApresentar = "União Estável";
			break;
		case "E":
			estadoCivilApresentar = "Separado(a)";
			break;
		case "Q":
			estadoCivilApresentar = "Desquitado(a)";
			break;
		default:
			estadoCivilApresentar = getEstadoCivilFiador();
			break;
		}
		return estadoCivilApresentar.toUpperCase();
	}

	@XmlElement(name = "secaoZonaEleitoral")
	public String getSecaoZonaEleitoral() {
		if (secaoZonaEleitoral == null) {
			secaoZonaEleitoral = Constantes.EMPTY;
		}
		return secaoZonaEleitoral;
	}

	public void setSecaoZonaEleitoral(String secaoZonaEleitoral) {
		this.secaoZonaEleitoral = secaoZonaEleitoral;
	}

	@XmlElement(name = "tipoMidiaCaptacao")
	public TipoMidiaCaptacaoVO getTipoMidiaCaptacao() {
		if (tipoMidiaCaptacao == null) {
			tipoMidiaCaptacao = new TipoMidiaCaptacaoVO();
		}

		return tipoMidiaCaptacao;
	}

	public void setTipoMidiaCaptacao(TipoMidiaCaptacaoVO tipoMidiaCaptacao) {
		this.tipoMidiaCaptacao = tipoMidiaCaptacao;
	}

	@XmlElement(name = "nomeBatismo")
	public String getNomeBatismo() {
		if (nomeBatismo == null) {
			nomeBatismo = Constantes.EMPTY;
		} else {
			if (nomeBatismo.contains("''")) {
				nomeBatismo = nomeBatismo.replaceAll("''", "'");
			}
		}
		return nomeBatismo;
	}

	public void setNomeBatismo(String nomeBatismo) {
		this.nomeBatismo = nomeBatismo;
	}

	@XmlElement(name = "quantidadeContaReceberPessoaComoAluno")
	public Integer getQuantidadeContaReceberPessoaComoAluno() {
		if (quantidadeContaReceberPessoaComoAluno == null) {
			quantidadeContaReceberPessoaComoAluno = 0;
		}
		return quantidadeContaReceberPessoaComoAluno;
	}

	public void setQuantidadeContaReceberPessoaComoAluno(Integer quantidadeContaReceberPessoaComoAluno) {
		this.quantidadeContaReceberPessoaComoAluno = quantidadeContaReceberPessoaComoAluno;
	}

	@XmlElement(name = "quantidadeContaReceberPessoaComoResponsavelFinanceiro")
	public Integer getQuantidadeContaReceberPessoaComoResponsavelFinanceiro() {
		if (quantidadeContaReceberPessoaComoResponsavelFinanceiro == null) {
			quantidadeContaReceberPessoaComoResponsavelFinanceiro = 0;
		}
		return quantidadeContaReceberPessoaComoResponsavelFinanceiro;
	}

	public void setQuantidadeContaReceberPessoaComoResponsavelFinanceiro(
			Integer quantidadeContaReceberPessoaComoResponsavelFinanceiro) {
		this.quantidadeContaReceberPessoaComoResponsavelFinanceiro = quantidadeContaReceberPessoaComoResponsavelFinanceiro;
	}

	public String getisAluno() {
		return getAluno() ? "Sim" : "Não";
	}

	public String getisFuncionario() {
		return getFuncionario() ? "Sim" : "Não";
	}

	@XmlElement(name = "codigoCurso1DadosUnicidadeCandidatoCurso")
	public Integer getCodigoCurso1DadosUnicidadeCandidatoCurso() {
		return codigoCurso1DadosUnicidadeCandidatoCurso;
	}

	public void setCodigoCurso1DadosUnicidadeCandidatoCurso(Integer codigoCurso1DadosUnicidadeCandidatoCurso) {
		this.codigoCurso1DadosUnicidadeCandidatoCurso = codigoCurso1DadosUnicidadeCandidatoCurso;
	}

	@XmlElement(name = "codigoCurso2DadosUnicidadeCandidatoCurso")
	public Integer getCodigoCurso2DadosUnicidadeCandidatoCurso() {
		return codigoCurso2DadosUnicidadeCandidatoCurso;
	}

	public void setCodigoCurso2DadosUnicidadeCandidatoCurso(Integer codigoCurso2DadosUnicidadeCandidatoCurso) {
		this.codigoCurso2DadosUnicidadeCandidatoCurso = codigoCurso2DadosUnicidadeCandidatoCurso;
	}

	@XmlElement(name = "codigoCurso3DadosUnicidadeCandidatoCurso")
	public Integer getCodigoCurso3DadosUnicidadeCandidatoCurso() {
		return codigoCurso3DadosUnicidadeCandidatoCurso;
	}

	public void setCodigoCurso3DadosUnicidadeCandidatoCurso(Integer codigoCurso3DadosUnicidadeCandidatoCurso) {
		this.codigoCurso3DadosUnicidadeCandidatoCurso = codigoCurso3DadosUnicidadeCandidatoCurso;
	}

	public void adicionarCpfTemporarioPais(FiliacaoVO obj) throws Exception {
		verificarCpfFiliacaoAntesAdicionar(obj);
	}

	private Integer zoomFonte;
	private ConfiguracaoAparenciaSistemaVO configuracaoAparenciaSistemaVO;

	public String getBanco() {
		if (banco == null) {
			banco = Constantes.EMPTY;
		}
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		if (agencia == null) {
			agencia = Constantes.EMPTY;
		}
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = Constantes.EMPTY;
		}
		return contaCorrente;
	}

	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}
	
	@ExcluirJsonAnnotation
	public String nomeOriginal;
	@ExcluirJsonAnnotation
	public String emailOriginal;
	public String getNomeOriginal() {
		if(nomeOriginal == null) {
			nomeOriginal =  getNome();
		}
		return (nomeOriginal);
	}
	
	public String getEmailOriginal() {
		if(emailOriginal == null) {
			emailOriginal =  getEmail();
		}
		return (emailOriginal);
	}
	
	
	public String getRegistroAcademico() {
		if(registroAcademico == null ) {
			registroAcademico = Constantes.EMPTY;
		}
		return registroAcademico;
	}

	public void setNomeOriginal(String nomeOriginal) {
		this.nomeOriginal = nomeOriginal;
	}

	public void setEmailOriginal(String emailOriginal) {
		this.emailOriginal = emailOriginal;
	}
	
	

	public void setRegistroAcademico(String registroAcademico) {
		this.registroAcademico = registroAcademico;
	}

	public Boolean getSabatista() {
		if(sabatista == null) {
			sabatista = Boolean.FALSE;
		}
		
		return sabatista;
	}

	public void setSabatista(Boolean sabatista) {
		this.sabatista = sabatista;
	}

	public String getUniversidadeParceira() {
		if(universidadeParceira == null) {
			universidadeParceira = Constantes.EMPTY;
		}
		return universidadeParceira;
	}

	public void setUniversidadeParceira(String universidadeParceira) {
		this.universidadeParceira = universidadeParceira;
	}
	
	public ModalidadeBolsaEnum getModalidadeBolsa() {
		if(modalidadeBolsa == null) {
			return ModalidadeBolsaEnum.NENHUM;
		}
		return modalidadeBolsa;
	}
	
	public void setModalidadeBolsa(ModalidadeBolsaEnum modalidadeBolsa) {
		this.modalidadeBolsa = modalidadeBolsa;
	}

	public Double getValorBolsa() {
		if(valorBolsa == null) {
			valorBolsa = 0.0;
		}
		return valorBolsa;
	}

	public void setValorBolsa(Double valorBolsa) {
		this.valorBolsa = valorBolsa;
	}

	public String getDataEmissaoRGAnonimizada() {
		if(dataEmissaoRGAnonimizada == null) {
			dataEmissaoRGAnonimizada = Constantes.EMPTY;
		}
		dataEmissaoRGAnonimizada =  Uteis.realizarAnonimizacaoIgnorandoTagNegrito(Uteis.getData(getDataEmissaoRG()),0,0,2); 
		
		return dataEmissaoRGAnonimizada;
	}

	public void setDataEmissaoRGAnonimizada(String dataEmissaoRGAnonimizada) {
		this.dataEmissaoRGAnonimizada = dataEmissaoRGAnonimizada;
	}
	
	public Boolean getTempoEstendidoProva() {
		if (tempoEstendidoProva == null) {
			tempoEstendidoProva = Boolean.FALSE;
		}
		return tempoEstendidoProva;
	}

	public void setTempoEstendidoProva(Boolean tempoEstendidoProva) {
		this.tempoEstendidoProva = tempoEstendidoProva;
	}
	
	public String getCPFormatoLGPD() {
        String mask = "XXX.XXX";
        if(getCPF().length() == 14) {
            String cpfOcultado = getCPF().substring(0, 4) + mask + getCPF().substring(11);
            return cpfOcultado;
        }
        return getCPF();
	}
	
	@XmlElement(name = "transtornosNeurodivergentes")
	public String getTranstornosNeurodivergentes() {
		if (transtornosNeurodivergentes == null) {
			transtornosNeurodivergentes = Constantes.EMPTY;
		}
		return transtornosNeurodivergentes;
	}

	public void setTranstornosNeurodivergentes(String transtornosNeurodivergentes) {
		this.transtornosNeurodivergentes = transtornosNeurodivergentes;
	}

}