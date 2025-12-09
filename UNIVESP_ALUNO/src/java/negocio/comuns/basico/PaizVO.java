package negocio.comuns.basico;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Paiz. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

@XmlRootElement(name = "paiz")
 public class PaizVO extends SuperVO  {

	private Integer codigo;
	private String nome;
	private boolean nacao;
	private String siglaInep;
	private String nacionalidade;
        public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Paiz</code>. Cria uma nova instância
	 * desta entidade, inicializando automaticamente seus atributos (Classe VO).
	 */
	public PaizVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>PaizVO</code>. Todos os tipos de consistência de dados são e devem
	 * ser implementadas neste método. São validações típicas: verificação de
	 * campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(PaizVO obj) throws ConsistirException {
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (País) deve ser informado.");
		}
		if (obj.getNacionalidade().equals("")) {
			throw new ConsistirException("O campo NACIONALIDADE (País) deve ser informado.");
		}
	}

	@XmlElement(name ="nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	
    @XmlElement(name ="codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public boolean isNacao() {
		return nacao;
	}

	public void setNacao(boolean nacao) {
		this.nacao = nacao;
	}

	public String getSiglaInep() {
            if(siglaInep == null){
                siglaInep = "";
            }
		return siglaInep;
	}

	public String getSiglaInepTratado() {
		if (isNacao() && (siglaInep == null || siglaInep.equals(""))) {
			siglaInep = "BRA";
		}
		if (!isNacao() && (siglaInep == null) || siglaInep.equals("")) {
			siglaInep = "ARG";
		}
		return siglaInep;
	}

	public void setSiglaInep(String siglaInep) {
		this.siglaInep = siglaInep;
	}

	/**
	 * @return the nacionalidade
	 */
	public String getNacionalidade() {
		if (nacionalidade == null) {
			nacionalidade = "";
		}
		return nacionalidade;
	}

	/**
	 * @param nacionalidade
	 *            the nacionalidade to set
	 */
	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}
}