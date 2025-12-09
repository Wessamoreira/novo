package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Eixo de Curso. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

public class EixoCursoVO extends SuperVO {
	
	public static final long serialVersionUID = 1L;
	private Integer codigo;
	private String nome;
	
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		if(nome == null) {
			nome = "";
		}
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	
}
