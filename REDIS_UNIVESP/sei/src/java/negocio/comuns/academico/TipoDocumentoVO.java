package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.TipoDocumentacaoEnum;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.secretaria.enumeradores.TipoUploadArquivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.administrativo.enumeradores.TipoIdadeExigidaEnum;

/**
 * Reponsável por manter os dados da entidade TipoDocumento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "tipoDocumentoVO")
public class TipoDocumentoVO extends SuperVO {

	private Integer codigo;
	private String nome;
	private boolean utilizaFuncionario;
	private Boolean contrato;
	private String escolaridade;
	private TipoExigenciaDocumentoEnum tipoExigenciaDocumento;
	private List<TipoDocumentoEquivalenteVO> tipoDocumentoEquivalenteVOs;
	private Boolean documentoFrenteVerso;
	private Boolean permitirPostagemPortalAluno;
	public static final long serialVersionUID = 1L;
	private String sexo;
	private Integer idade;
	private String estrangeiro;
	private String estadoCivil;
	private Boolean transferencia;
	private Boolean portadorDiploma;
	private Boolean inscricaoProcessoSeletivo;
	private Boolean reabertura;
	private Boolean renovacao;
	private Boolean enem;
	private String identificadorGED;
	private CategoriaGEDVO categoriaGED;
	private TipoUploadArquivoEnum tipoUploadArquivo;
    private String extensaoArquivo;
	
	private Boolean filtrarTipoDocumento;
	private Boolean documentoObrigatorioFuncionario;
	private Boolean assinarDigitalmente;
	private String tipoIdadeExigida;
	private TipoDocumentacaoEnum tipoDocumentacaoEnum;
	private Boolean enviarDocumentoXml;

	/**
	 * Construtor padrão da classe <code>TipoDocumento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public TipoDocumentoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar a unicidade dos dados de um objeto da classe
	 * <code>TipoDocumentoVO</code>.
	 */
	public static void validarUnicidade(List<TipoDocumentoVO> lista, TipoDocumentoVO obj) throws ConsistirException {
		for (TipoDocumentoVO repetido : lista) {
			if (repetido.getNome().equals(obj.getNome())) {
				throw new ConsistirException("Já existe um documento com o mesmo nome.");
			}
		}
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>TipoDocumentoVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(TipoDocumentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoDocumentacaoEnum())) {
			throw new ConsistirException("O campo TIPO DOCUMENTAÇÃO deve ser informado.");
		}
		if (!obj.getUtilizaFuncionario() && obj.getTipoExigenciaDocumento() == null) {
			throw new ConsistirException("O campo TIPO EXIGÊNCIA deve ser informado.");
		}
		if (obj.getUtilizaFuncionario()) {
			obj.setTipoExigenciaDocumento(null);
		}else {
			obj.setDocumentoObrigatorioFuncionario(false);
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
	 */
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setNome(getNome().toUpperCase());
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setNome("");
		setUtilizaFuncionario(Boolean.FALSE);
	}

	public void adicionarObjTipoDocumentoEquivalenteVOs(TipoDocumentoEquivalenteVO obj) throws Exception {
		int index = 0;
		Iterator i = getTipoDocumentoEquivalenteVOs().iterator();
		while (i.hasNext()) {
			TipoDocumentoEquivalenteVO objExistente = (TipoDocumentoEquivalenteVO) i.next();
			if (objExistente.getTipoDocumentoEquivalente().getCodigo().intValue() == obj.getTipoDocumentoEquivalente()
					.getCodigo().intValue()) {
				getTipoDocumentoEquivalenteVOs().set(index, obj);
				return;
			}
			index++;
		}
		getTipoDocumentoEquivalenteVOs().add(obj);
	}

	public void excluirObjTipoDocumentoEquivalenteVOs(Integer tipoDocumento) throws Exception {
		int index = 0;
		Iterator i = getTipoDocumentoEquivalenteVOs().iterator();
		while (i.hasNext()) {
			TipoDocumentoEquivalenteVO objExistente = (TipoDocumentoEquivalenteVO) i.next();
			if (objExistente.getTipoDocumentoEquivalente().getCodigo().intValue() == tipoDocumento) {
				getTipoDocumentoEquivalenteVOs().remove(index);
				return;
			}
			index++;
		}
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
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

	/**
	 * @return the utilizaFuncionario
	 */
	@XmlElement(name = "utilizaFuncionario")
	public boolean getUtilizaFuncionario() {
		return utilizaFuncionario;
	}

	/**
	 * @param utilizaFuncionario
	 *            the utilizaFuncionario to set
	 */
	public void setUtilizaFuncionario(boolean utilizaFuncionario) {
		this.utilizaFuncionario = utilizaFuncionario;
	}

	@XmlElement(name = "escolaridade")
	public String getEscolaridade() {
		if (escolaridade == null) {
			escolaridade = "";
		}
		return escolaridade;
	}

	public void setEscolaridade(String escolaridade) {
		this.escolaridade = escolaridade;
	}

	@XmlElement(name = "tipoExigenciaDocumento")
	public TipoExigenciaDocumentoEnum getTipoExigenciaDocumento() {
		if (tipoExigenciaDocumento == null) {
			tipoExigenciaDocumento = TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO;
		}
		return tipoExigenciaDocumento;
	}

	public void setTipoExigenciaDocumento(TipoExigenciaDocumentoEnum tipoExigenciaDocumento) {
		this.tipoExigenciaDocumento = tipoExigenciaDocumento;
	}

	/**
	 * @return the tipoDocumentoEquivalenteVOs
	 */
	
	@XmlElement(name = "tipoDocumentoEquivalenteVOs")
	public List<TipoDocumentoEquivalenteVO> getTipoDocumentoEquivalenteVOs() {
		if (tipoDocumentoEquivalenteVOs == null) {
			tipoDocumentoEquivalenteVOs = new ArrayList();
		}
		return tipoDocumentoEquivalenteVOs;
	}

	/**
	 * @param tipoDocumentoEquivalenteVOs the tipoDocumentoEquivalenteVOs to set
	 */
	public void setTipoDocumentoEquivalenteVOs(List<TipoDocumentoEquivalenteVO> tipoDocumentoEquivalenteVOs) {
		this.tipoDocumentoEquivalenteVOs = tipoDocumentoEquivalenteVOs;
	}

	/**
	 * @return the documentoFrenteVerso
	 */
	@XmlElement(name = "documentoFrenteVerso")
	public Boolean getDocumentoFrenteVerso() {
		if (documentoFrenteVerso == null) {
			documentoFrenteVerso = Boolean.FALSE;
		}
		return documentoFrenteVerso;
	}

	/**
	 * @param documentoFrenteVerso the documentoFrenteVerso to set
	 */
	public void setDocumentoFrenteVerso(Boolean documentoFrenteVerso) {
		this.documentoFrenteVerso = documentoFrenteVerso;
	}

	/**
	 * @return the permitirPostagemPortalAluno
	 */
	@XmlElement(name = "permitirPostagemPortalAluno")
	public Boolean getPermitirPostagemPortalAluno() {
		if (permitirPostagemPortalAluno == null) {
			permitirPostagemPortalAluno = Boolean.FALSE;
		}
		return permitirPostagemPortalAluno;
	}

	/**
	 * @param permitirPostagemPortalAluno the permitirPostagemPortalAluno to set
	 */
	public void setPermitirPostagemPortalAluno(Boolean permitirPostagemPortalAluno) {
		this.permitirPostagemPortalAluno = permitirPostagemPortalAluno;
	}

	@XmlElement(name = "sexo")
	public String getSexo() {
		if (sexo == null) {
			sexo = "";
		}
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	@XmlElement(name = "portadorDiploma")
	public Boolean getPortadorDiploma() {
		if (portadorDiploma == null) {
			portadorDiploma = false;
		}
		return portadorDiploma;
	}

	public void setPortadorDiploma(Boolean portadorDiploma) {
		this.portadorDiploma = portadorDiploma;
	}

	@XmlElement(name = "idadeMinima")
	public Integer getIdade() {
		if (idade == null) {
			idade = 0;
		}
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	@XmlElement(name = "estrangeiro")
	public String getEstrangeiro() {
		if (estrangeiro == null) {
			estrangeiro = "";
		}
		return estrangeiro;
	}

	public void setEstrangeiro(String estrangeiro) {
		this.estrangeiro = estrangeiro;
	}

	@XmlElement(name = "estadoCivil")
	public String getEstadoCivil() {
		if (estadoCivil == null) {
			estadoCivil = "";
		}
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	@XmlElement(name = "transferencia")
	public Boolean getTransferencia() {
		if (transferencia == null) {
			transferencia = false;
		}
		return transferencia;
	}

	public void setTransferencia(Boolean transferencia) {
		this.transferencia = transferencia;
	}

	@XmlElement(name = "inscricaoProcessoSeletivo")
	public Boolean getInscricaoProcessoSeletivo() {
		if (inscricaoProcessoSeletivo == null) {
			inscricaoProcessoSeletivo = false;
		}
		return inscricaoProcessoSeletivo;
	}

	public void setInscricaoProcessoSeletivo(Boolean inscricaoProcessoSeletivo) {
		this.inscricaoProcessoSeletivo = inscricaoProcessoSeletivo;
	}

	@XmlElement(name = "reabertura")
	public Boolean getReabertura() {
		if (reabertura == null) {
			reabertura = false;
		}
		return reabertura;
	}

	public void setReabertura(Boolean reabertura) {
		this.reabertura = reabertura;
	}

	@XmlElement(name = "renovacao")
	public Boolean getRenovacao() {
		if (renovacao == null) {
			renovacao = false;
		}
		return renovacao;
	}

	public void setRenovacao(Boolean renovacao) {
		this.renovacao = renovacao;
	}

	@XmlElement(name = "enem")
	public Boolean getEnem() {
		if (enem == null) {
			enem = false;
		}
		return enem;
	}

	public void setEnem(Boolean enem) {
		this.enem = enem;
	}

	@XmlElement(name = "contrato")
	public Boolean getContrato() {
		if (contrato == null) {
			contrato = Boolean.FALSE;
		}
		return contrato;
	}

	public void setContrato(Boolean contrato) {
		this.contrato = contrato;
	}

	@XmlElement(name = "identificadorGED")
	public String getIdentificadorGED() {
		if (identificadorGED == null) {
			identificadorGED = "";
		}
		return identificadorGED;
	}

	public void setIdentificadorGED(String identificadorGED) {
		this.identificadorGED = identificadorGED;
	}

	public CategoriaGEDVO getCategoriaGED() {
		if (categoriaGED == null) {
			categoriaGED = new CategoriaGEDVO();
		}
		return categoriaGED;
	}

	public void setCategoriaGED(CategoriaGEDVO categoriaGED) {
		this.categoriaGED = categoriaGED;
	}
	
	public Boolean getFiltrarTipoDocumento() {
		if(filtrarTipoDocumento == null) {
			filtrarTipoDocumento = Boolean.FALSE;
		}
		return filtrarTipoDocumento;
	}

	public void setFiltrarTipoDocumento(Boolean filtrarTipoDocumento) {
		this.filtrarTipoDocumento = filtrarTipoDocumento;
	}
	
	@XmlElement(name = "tipoUploadArquivo")
	public TipoUploadArquivoEnum getTipoUploadArquivo() {
		if (tipoUploadArquivo == null) {
			tipoUploadArquivo = TipoUploadArquivoEnum.IMAGEM;
		}
		return tipoUploadArquivo;
	}

	public void setTipoUploadArquivo(TipoUploadArquivoEnum tipoUploadArquivo) {
		this.tipoUploadArquivo = tipoUploadArquivo;
	}

	@XmlElement(name = "extensaoArquivo")
	public String getExtensaoArquivo() {
		if (extensaoArquivo == null) {
			extensaoArquivo = getTipoUploadArquivo().getExtensao();
		}
		return extensaoArquivo;
	}

	public void setExtensaoArquivo(String extensaoArquivo) {
		this.extensaoArquivo = extensaoArquivo;
	}
	
	public Boolean getDocumentoObrigatorioFuncionario() {
		if(documentoObrigatorioFuncionario == null) {
			documentoObrigatorioFuncionario = false;
		}else if(!this.utilizaFuncionario){
			documentoObrigatorioFuncionario = false;
		}
		return documentoObrigatorioFuncionario;
	}

	public void setDocumentoObrigatorioFuncionario(Boolean documentoObrigatorioFuncionario) {
		this.documentoObrigatorioFuncionario = documentoObrigatorioFuncionario;
	}
	

	public Boolean getAssinarDigitalmente() {
		if(assinarDigitalmente == null) {
			assinarDigitalmente = false;
		}
		return assinarDigitalmente;
	}

	public void setAssinarDigitalmente(Boolean assinarDigitalmente) {
		this.assinarDigitalmente = assinarDigitalmente;
	}

	public String getTipoIdadeExigida() {
		if(tipoIdadeExigida == null) {
			tipoIdadeExigida = TipoIdadeExigidaEnum.MINIMA.getValor();
		}
		return tipoIdadeExigida;
	}

	public void setTipoIdadeExigida(String tipoIdadeExigida) {
		this.tipoIdadeExigida = tipoIdadeExigida;
	}	

	public TipoDocumentacaoEnum getTipoDocumentacaoEnum() {
		if (tipoDocumentacaoEnum == null) {
			tipoDocumentacaoEnum = TipoDocumentacaoEnum.OUTROS;
		}
		return tipoDocumentacaoEnum;
	}

	public void setTipoDocumentacaoEnum(TipoDocumentacaoEnum tipoDocumentacaoEnum) {
		this.tipoDocumentacaoEnum = tipoDocumentacaoEnum;
	}
	
	public Boolean getEnviarDocumentoXml() {
		if (enviarDocumentoXml == null) {
			enviarDocumentoXml = Boolean.TRUE;
		}
		return enviarDocumentoXml;
	}
	
	public void setEnviarDocumentoXml(Boolean enviarDocumentoXml) {
		this.enviarDocumentoXml = enviarDocumentoXml;
	}
}
