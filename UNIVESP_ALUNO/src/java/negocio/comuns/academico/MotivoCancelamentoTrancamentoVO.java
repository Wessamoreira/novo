package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.dominios.TipoJustificativaCancelamento;

/**
 * Reponsável por manter os dados da entidade Turno. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MotivoCancelamentoTrancamentoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String situacao;
    private String tipoJustificativa;
    private Boolean apresentarRequerimentoVisaoAluno;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Turno</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public MotivoCancelamentoTrancamentoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
    }

    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    /**
     * @return the situacao
     */
    public String getSituacao() {
        if (situacao == null) {
            situacao = "EM";
        }
        return situacao;
    }

    public boolean getAtivo() {
        if (getSituacao().equals("AT")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getInativo() {
        if (getSituacao().equals("IN")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getEmConstrucao() {
        if (getSituacao().equals("EM")) {
            return true;
        } else {
            return false;
        }
    }

    public String getSituacao_Apresentar() {
        if (getSituacao().equals("EM")) {
            return "Em Construção";
        }
        if (getSituacao().equals("AT")) {
            return "Ativo";
        }
        if (getSituacao().equals("IN")) {
            return "Inativo";
        }
        return getSituacao();
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTipoJustificativa() {
        if (tipoJustificativa == null) {
            tipoJustificativa = TipoJustificativaCancelamento.OUTROS.getValor();
        }
        return (tipoJustificativa);
    }
    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de
     * apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
     */
    public String getTipoJustificativa_Apresentar() {
        return Dominios.getTipoJustificativaAlteracaoMatricula().get(getTipoJustificativa()).toString();
    }
    public void setTipoJustificativa(String tipoJustificativa) {
        this.tipoJustificativa = tipoJustificativa;
    }

	public Boolean getApresentarRequerimentoVisaoAluno() {
		if(apresentarRequerimentoVisaoAluno == null) {
			apresentarRequerimentoVisaoAluno = false;
		}
		return apresentarRequerimentoVisaoAluno;
	}

	public void setApresentarRequerimentoVisaoAluno(Boolean apresentarRequerimentoVisaoAluno) {
		this.apresentarRequerimentoVisaoAluno = apresentarRequerimentoVisaoAluno;
	}
    
    
}
