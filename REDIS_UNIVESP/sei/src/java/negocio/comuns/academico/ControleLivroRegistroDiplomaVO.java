package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoControleLivroRegistroDiploma;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 * Reponsável por manter os dados da entidade ControleLivroRegistroDiploma.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ControleLivroRegistroDiplomaVO extends SuperVO {

	public static final long serialVersionUID = 1L;

	private Integer codigo;
	private Integer nrLivro;
	private Integer nrFolhaRecibo;
	private String situacaoFechadoAberto;
	private Integer nrMaximoFolhasLivro;
	protected CursoVO curso;
	protected UnidadeEnsinoVO unidadeEnsino;
	private Integer numeroRegistro;
	private Date dataPublicacao;
	private String via;
	private TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum;
	private String nivelEducacional;
	private Integer quantidadeRegistroPorFolha;
	private String unidadeEnsinoDescricao;
	private String unidadeConsultaApresentar;
	private List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs;

	/**
	 * Construtor padrão da classe <code>ControleLivroRegistroDiploma</code>. Cria
	 * uma nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	public ControleLivroRegistroDiplomaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ControleLivroRegistroDiplomaVO</code>. Todos os tipos de consistência
	 * de dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public static void validarDados(ControleLivroRegistroDiplomaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (!Uteis.isAtributoPreenchido(obj.getTipoLivroRegistroDiplomaEnum())) {
			throw new ConsistirException(
					"O campo TIPO LIVRO REGISTRO (Controle Livro Registro Diploma) deve ser informado.");
		}
//		if (!Uteis.isAtributoPreenchido(obj.getNivelEducacional())) {
//			if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
//				throw new ConsistirException("O campo CURSO (Controle Livro Registro Diploma) deve ser informado.");
//			}
//		}
		if (obj.getNrLivro().intValue() == 0) {
			throw new ConsistirException("O campo NR LIVRO (Controle Livro Registro Diploma) deve ser informado.");
		}
		if (obj.getNrMaximoFolhasLivro().intValue() == 0) {
			throw new ConsistirException("O NR MAXIMO FOLHAS (Controle Livro Registro Diploma) deve ser informado.");
		}
	}

	/**
	 * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
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
		setCodigo(new Integer(0));
		setNrLivro(new Integer(0));
		setNrFolhaRecibo(new Integer(0));
		setSituacaoFechadoAberto("");
		setNrMaximoFolhasLivro(new Integer(0));
		// setTipoLivroRegistroDiplomaEnum(TipoLivroRegistroDiplomaEnum.DIPLOMA);
	}

	/**
	 * Retorna o objeto da classe <code>Curso</code> relacionado com
	 * (<code>ControleLivroRegistroDiploma</code>).
	 */
	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return (curso);
	}

	/**
	 * Define o objeto da classe <code>Curso</code> relacionado com
	 * (<code>ControleLivroRegistroDiploma</code>).
	 */
	public void setCurso(CursoVO obj) {
		this.curso = obj;
	}

	public Integer getNrMaximoFolhasLivro() {
		if (nrMaximoFolhasLivro == null) {
			nrMaximoFolhasLivro = new Integer(0);
		}
		return (nrMaximoFolhasLivro);
	}

	public void setNrMaximoFolhasLivro(Integer nrMaximoFolhasLivro) {
		this.nrMaximoFolhasLivro = nrMaximoFolhasLivro;
	}

	public void setSituacaoFechadoAberto(String situacaoFechadoAberto) {
		this.situacaoFechadoAberto = situacaoFechadoAberto;
	}

	public String getSituacaoFechadoAberto() {
		if (situacaoFechadoAberto == null) {
			situacaoFechadoAberto = SituacaoControleLivroRegistroDiploma.ABERTO.getValor().toString();
		}
		return situacaoFechadoAberto;
	}

	public String getSituacaoFechadoAbertoDescricao() {
		return SituacaoControleLivroRegistroDiploma.getEnum(getSituacaoFechadoAberto()).getDescricao();
	}

	public Integer getNrFolhaRecibo() {
		if (nrFolhaRecibo == null) {
			nrFolhaRecibo = new Integer(0);
		}
		return (nrFolhaRecibo);
	}

	public void setNrFolhaRecibo(Integer nrFolhaRecibo) {
		this.nrFolhaRecibo = nrFolhaRecibo;
	}

	public Integer getNrLivro() {
		if (nrLivro == null) {
			nrLivro = new Integer(0);
		}
		return (nrLivro);
	}

	public void setNrLivro(Integer nrLivro) {
		this.nrLivro = nrLivro;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = new Integer(0);
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Integer getNumeroRegistro() {
		if (numeroRegistro == null) {
			numeroRegistro = 0;
		}
		return numeroRegistro;
	}

	public void setNumeroRegistro(Integer numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	public Date getDataPublicacao() {
		if (dataPublicacao == null) {
			dataPublicacao = new Date();
		}
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
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

	public TipoLivroRegistroDiplomaEnum getTipoLivroRegistroDiplomaEnum() {
		if (tipoLivroRegistroDiplomaEnum == null) {
			tipoLivroRegistroDiplomaEnum = TipoLivroRegistroDiplomaEnum.DIPLOMA;
		}
		return tipoLivroRegistroDiplomaEnum;
	}

	public void setTipoLivroRegistroDiplomaEnum(TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum) {
		this.tipoLivroRegistroDiplomaEnum = tipoLivroRegistroDiplomaEnum;
	}

	public String getNivelEducacional() {
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public Integer getQuantidadeRegistroPorFolha() {
		if (quantidadeRegistroPorFolha == null) {
			quantidadeRegistroPorFolha = 1;
		}
		return quantidadeRegistroPorFolha;
	}

	public void setQuantidadeRegistroPorFolha(Integer quantidadeRegistroPorFolha) {
		this.quantidadeRegistroPorFolha = quantidadeRegistroPorFolha;
	}

	public String getNivelEducacional_Apresentar() {
		return TipoNivelEducacional.getDescricao(getNivelEducacional());
	}
	
	public List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> getControleLivroRegistroDiplomaUnidadeEnsinoVOs() {
		if (controleLivroRegistroDiplomaUnidadeEnsinoVOs == null) {
			controleLivroRegistroDiplomaUnidadeEnsinoVOs = new ArrayList<ControleLivroRegistroDiplomaUnidadeEnsinoVO>(0);
		}
		return controleLivroRegistroDiplomaUnidadeEnsinoVOs;
	}
	
	public void setControleLivroRegistroDiplomaUnidadeEnsinoVOs(List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
		this.controleLivroRegistroDiplomaUnidadeEnsinoVOs = controleLivroRegistroDiplomaUnidadeEnsinoVOs;
	}

	public String getUnidadeEnsinoDescricao() {
		if (unidadeEnsinoDescricao == null) {
			unidadeEnsinoDescricao = "";
		}
		return unidadeEnsinoDescricao;
	}
	
	public void setUnidadeEnsinoDescricao(String unidadeEnsinoDescricao) {
		this.unidadeEnsinoDescricao = unidadeEnsinoDescricao;
	}
	
	public String getUnidadeConsultaApresentar() {
		if (unidadeConsultaApresentar == null) {
			unidadeConsultaApresentar = "";
		}
		return unidadeConsultaApresentar;
	}
	
	public void setUnidadeConsultaApresentar(String unidadeConsultaApresentar) {
		this.unidadeConsultaApresentar = unidadeConsultaApresentar;
	}
	
//	public UnidadeEnsinoVO getUnidadeEnsino() {
//		return unidadeEnsino;
//	}
//	
//	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
//		this.unidadeEnsino = unidadeEnsino;
//	}
	
}
