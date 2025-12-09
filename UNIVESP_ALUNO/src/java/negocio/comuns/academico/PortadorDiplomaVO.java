package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade PortadorDiploma. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PortadorDiplomaVO extends SuperVO {

	private Integer codigo;
	private Date data;
	private String descricao;
	private RequerimentoVO codigoRequerimento;
	private String curso;
	private String instituicaoEnsino;
	private TipoMidiaCaptacaoVO tipoMidiaCaptacao;
	private CidadeVO cidade;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Matricula </code>.
	 */
	private MatriculaVO matricula;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
	 */
	private PessoaVO responsavelAutorizacao;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>PortadorDiploma</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public PortadorDiplomaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setData(new Date());
		setDescricao("");
		setCurso("");
		setInstituicaoEnsino("");
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>PortadorDiploma</code>).
	 */
	public PessoaVO getResponsavelAutorizacao() {
		if (responsavelAutorizacao == null) {
			responsavelAutorizacao = new PessoaVO();
		}
		return (responsavelAutorizacao);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>PortadorDiploma</code>).
	 */
	public void setResponsavelAutorizacao(PessoaVO obj) {
		this.responsavelAutorizacao = obj;
	}

	/**
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com ( <code>PortadorDiploma</code>).
	 */
	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return (matricula);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com ( <code>PortadorDiploma</code>).
	 */
	public void setMatricula(MatriculaVO obj) {
		this.matricula = obj;
	}

	public TipoMidiaCaptacaoVO getTipoMidiaCaptacao() {
		if (tipoMidiaCaptacao == null) {
			tipoMidiaCaptacao = new TipoMidiaCaptacaoVO();
		}
		return tipoMidiaCaptacao;
	}

	public void setTipoMidiaCaptacao(TipoMidiaCaptacaoVO tipoMidiaCaptacao) {
		this.tipoMidiaCaptacao = tipoMidiaCaptacao;
	}

	public String getInstituicaoEnsino() {
		if (instituicaoEnsino == null) {
			instituicaoEnsino = "";
		}
		return (instituicaoEnsino);
	}

	public void setInstituicaoEnsino(String instituicaoEnsino) {
		this.instituicaoEnsino = instituicaoEnsino;
	}

	public String getCurso() {
		if (curso == null) {
			curso = "";
		}
		return (curso);
	}

	public void setCurso(String curso) {
		this.curso = curso;
	}

	public RequerimentoVO getCodigoRequerimento() {
		if (codigoRequerimento == null) {
			codigoRequerimento = new RequerimentoVO();
		}
		return codigoRequerimento;
	}

	public void setCodigoRequerimento(RequerimentoVO codigoRequerimento) {
		this.codigoRequerimento = codigoRequerimento;
	}

	public String getDescricao() {
		return (descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return (data);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		return (Uteis.getData(data));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}
}
