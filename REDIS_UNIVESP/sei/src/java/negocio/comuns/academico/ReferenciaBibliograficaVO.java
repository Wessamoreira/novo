package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Disciplina;

/**
 * Reponsável por manter os dados da entidade ReferenciaBibliografica. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Disciplina
 */
public class ReferenciaBibliograficaVO extends SuperVO {

	private Integer codigo;
	private String titulo;
	private String anoPublicacao;
	private String edicao;
	private String localPublicacao;
	private String ISBN;
	private String autores;
	private String tipoPublicacao;
	private String tipoReferencia;
	private Integer disciplina;
	private Boolean publicacaoExistenteBiblioteca;
	private PlanoEnsinoVO planoEnsino;
	private String subtitulo;
	private String justificativa;

	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Exemplar </code>.
	 */
	private CatalogoVO catalogo;

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>ReferenciaBibliografica</code>. Cria
	 * uma nova instância desta entidade, inicializando automaticamente seus
	 * atributos (Classe VO).
	 */
	public ReferenciaBibliograficaVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ReferenciaBibliograficaVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(ReferenciaBibliograficaVO obj) throws ConsistirException {
		if (obj.getPublicacaoExistenteBiblioteca()) {
			if (obj.getPublicacaoExistenteBiblioteca() && (obj.getCatalogo() == null || obj.getCatalogo().isNovoObj())) {
				throw new ConsistirException("O campo PUBLICACÃO BIBLIOGRAFICA (Referência Bibliografica) deve ser informado.");
			}
			if ((obj.getCatalogo() != null && !obj.getCatalogo().isNovoObj()) && obj.getCatalogo().getTitulo().equals("")) {
				throw new ConsistirException("O campo TÍTULO (Referência Bibliografica) deve ser informado.");
			}
			if ((obj.getCatalogo() != null && !obj.getCatalogo().isNovoObj()) && obj.getCatalogo().getAnoPublicacao().equals("")) {
				throw new ConsistirException("O campo ANO DE PUBLICAÇÃO (Referência Bibliografica) deve ser informado.");
			}
			obj.setTitulo("");
			obj.setAnoPublicacao("");
			obj.setEdicao("");
			obj.setLocalPublicacao("");
			obj.setISBN("");
			obj.setAutores("");
			obj.setTipoPublicacao("");
		}else{
			if(obj.getTitulo().trim().isEmpty()){
				throw new ConsistirException("O campo TÍTULO (Referência Bibliografica) deve ser informado.");
			}
			if(obj.getAnoPublicacao().trim().isEmpty()){
				throw new ConsistirException("O campo ANO PUBLICAÇÃO (Referência Bibliografica) deve ser informado.");
			}
			if (obj.getAnoPublicacao().trim().length() < 4) {
				throw new ConsistirException("O campo ANO DE PUBLICAÇÃO (Referência Bibliografica) deve possuir 4 dígitos.");
			}
			obj.setCatalogo(null);
		}
		// if ((obj.getCatalogo()!=null && !obj.getCatalogo().isNovoObj()) &&
		// obj.getCatalogo().getEdicao().equals("")) {
		// throw new
		// ConsistirException("O campo EDIÇÃO (Referência Bibliografica) deve ser informado.");
		// }
		// if ((obj.getCatalogo()!=null && !obj.getCatalogo().isNovoObj()) &&
		// obj.getCatalogo().getCidadePublicacaoCatalogo()!= null &&
		// !obj.getCatalogo().getCidadePublicacaoCatalogo().isNovoObj() &&
		// obj.getCatalogo().getCidadePublicacaoCatalogo().getNome().equals(""))
		// {
		// throw new
		// ConsistirException("O campo LOCAL DE PUBLICAÇÂO (Referência Bibliografica) deve ser informado.");
		// }
		// if ((obj.getCatalogo()!=null && !obj.getCatalogo().isNovoObj()) &&
		// obj.getCatalogo().getIsbn().equals("")) {
		// throw new
		// ConsistirException("O campo ISBN (Referência Bibliografica) deve ser informado.");
		// }
		// if ((obj.getCatalogo()!=null && !obj.getCatalogo().isNovoObj()) &&
		// obj.getCatalogo().getCatalogoAutorVOs().equals("")) {
		// throw new
		// ConsistirException("O campo AUTORES (Referência Bibliografica) deve ser informado.");
		// }
		//
		// if ((obj.getCatalogo()!=null && !obj.getCatalogo().isNovoObj()) &&
		// obj.getCatalogo().getTipoCatalogo() == null) {
		// throw new
		// ConsistirException("O campo TIPO PUBLICAÇÃO (Referência Bibliografica) deve ser informado.");
		// }
		//
		 if (obj.getTipoReferencia() == null || obj.getTipoReferencia().trim().isEmpty()) {
			 throw new ConsistirException("O campo TIPO REFERÊNCIA (Referência Bibliografica) deve ser informado.");
		 }

	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setTitulo("");
		setAnoPublicacao("");
		setEdicao("");
		setLocalPublicacao("");
		setISBN("");
		setAutores("");
		setTipoPublicacao("");
		setTipoReferencia("");
		setPublicacaoExistenteBiblioteca(Boolean.FALSE);
	}

	public Boolean getPublicacaoExistenteBiblioteca() {
		return publicacaoExistenteBiblioteca;
	}

	public void setPublicacaoExistenteBiblioteca(Boolean publicacaoExistenteBiblioteca) {
		this.publicacaoExistenteBiblioteca = publicacaoExistenteBiblioteca;
	}

	public CatalogoVO getCatalogo() {
		if (catalogo == null) {
			catalogo = new CatalogoVO();
		}
		return catalogo;
	}

	public void setCatalogo(CatalogoVO catalogo) {
		this.catalogo = catalogo;
	}

	public Integer getDisciplina() {
		return (disciplina);
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public String getTipoReferencia() {
		if (tipoReferencia == null) {
			tipoReferencia = "";
		}
		return (tipoReferencia);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	public String getTipoReferencia_Apresentar() {
		if (tipoReferencia.equals("BA")) {
			return "Básica";
		}
		if (tipoReferencia.equals("CO")) {
			return "Complementar";
		}
		return (tipoReferencia);
	}

	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}

	public String getTipoPublicacao() {
		if (tipoPublicacao == null) {
			tipoPublicacao = "";
		}
		return (tipoPublicacao);
	}

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo
	 * com um domínio específico. Com base no valor de armazenamento do atributo
	 * esta função é capaz de retornar o de apresentação correspondente. Útil
	 * para campos como sexo, escolaridade, etc.
	 */
	public String getTipoPublicacao_Apresentar() {
		if(tipoPublicacao == null){
			tipoPublicacao = "LI";
		}
		if (tipoPublicacao.equals("LI")) {
			return "Livro";
		}
		if (tipoPublicacao.equals("AR")) {
			return "Artigo";
		}
		if (tipoPublicacao.equals("RE")) {
			return "Revista";
		}
		if (tipoPublicacao.equals("MO")) {
			return "Monografia";
		}
		if (tipoPublicacao.equals("VI")) {
			return "Video";
		}
		return (tipoPublicacao);
	}

	public void setTipoPublicacao(String tipoPublicacao) {
		this.tipoPublicacao = tipoPublicacao;
	}

	public String getAutores() {
		if (autores == null) {
			autores = "";
		}
		return (autores);
	}

	public void setAutores(String autores) {
		this.autores = autores;
	}

	public String getISBN() {
		if (ISBN == null) {
			ISBN = "";
		}
		return (ISBN);
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	public String getLocalPublicacao() {
		if (localPublicacao == null) {
			localPublicacao = "";
		}
		return (localPublicacao);
	}

	public void setLocalPublicacao(String localPublicacao) {
		this.localPublicacao = localPublicacao;
	}

	public String getEdicao() {
		if (edicao == null) {
			edicao = "";
		}
		return (edicao);
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public String getAnoPublicacao() {
		if (anoPublicacao == null) {
			anoPublicacao = "";
		}
		return anoPublicacao;
	}

	public void setAnoPublicacao(String anoPublicacao) {
		this.anoPublicacao = anoPublicacao;
	}

	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return (titulo);
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public PlanoEnsinoVO getPlanoEnsino() {
		if (planoEnsino == null) {
			planoEnsino = new PlanoEnsinoVO();
		}
		return planoEnsino;
	}

	public void setPlanoEnsino(PlanoEnsinoVO planoEnsino) {
		this.planoEnsino = planoEnsino;
	}
	
	public String getSubtitulo() {
		if (subtitulo == null) {
			subtitulo = "";
		}
		return subtitulo;
	}
	
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}

	public String getJustificativa() {
		if(justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
	
}
