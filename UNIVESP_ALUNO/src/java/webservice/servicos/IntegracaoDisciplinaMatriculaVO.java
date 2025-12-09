package webservice.servicos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;

@XmlRootElement
public class IntegracaoDisciplinaMatriculaVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 732349415249204507L;
	private Integer disciplina;
    private String nomeDisciplina;

    /**
     * Construtor padrão da classe <code>FormacaoAcademica</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public IntegracaoDisciplinaMatriculaVO() {
        super();
    }

    @XmlElement(name = "disciplina")
	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	@XmlElement(name = "nomeDisciplina")
	public String getNomeDisciplina() {
		if (nomeDisciplina == null) {
			nomeDisciplina = "";
		}
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}


}
