package negocio.comuns.academico;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Turma. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TextoPadraoDeclaracaoFuncionarioVO extends SuperVO {

    protected Integer codigo;
    protected TextoPadraoDeclaracaoVO textoPadraoDeclaracao;
    protected FuncionarioVO funcionario;
    public static final long serialVersionUID = 1L;


    /**
     * Construtor padrão da classe <code>Turma</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public TextoPadraoDeclaracaoFuncionarioVO() {
        super();
    }


    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    /**
     * @param codigo
     *            the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
        	funcionario = new FuncionarioVO();
        }
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }


	public TextoPadraoDeclaracaoVO getTextoPadraoDeclaracao() {
		if (textoPadraoDeclaracao == null) {
			textoPadraoDeclaracao = new TextoPadraoDeclaracaoVO();
		}
		return textoPadraoDeclaracao;
	}


	public void setTextoPadraoDeclaracao(TextoPadraoDeclaracaoVO textoPadraoDeclaracao) {
		this.textoPadraoDeclaracao = textoPadraoDeclaracao;
	}
}
