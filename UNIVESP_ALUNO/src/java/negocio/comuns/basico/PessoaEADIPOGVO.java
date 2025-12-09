package negocio.comuns.basico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Estado. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
 public class PessoaEADIPOGVO extends SuperVO  {

    private Integer codigo;
    private Integer aluno;
    private Integer turma;
    private Integer disciplina;
    private String matricula;
    private String cpfCorrespondente;
    private String emailCorrespondente;
    private String email2Correspondente;
    private Date dataInicio;
    private Date dataAlteracao;
    private String situacao;
    private String msgErroAtualizacaoStatus;
    private Boolean erroAtualizacaoStatus;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Estado</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public PessoaEADIPOGVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>EstadoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PessoaEADIPOGVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getCpfCorrespondente().equals("")) {
            throw new ConsistirException("O campo CPF CORRESPONDENTE (PESSOA EAD IPOG) deve ser informado.");
        }
        if (obj.getEmailCorrespondente().equals("")) {
        	throw new ConsistirException("O campo EMAIL CORRESPONDENTE (PESSOA EAD IPOG) deve ser informado.");
        }
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

	public String getCpfCorrespondente() {
		if (cpfCorrespondente == null) {
			cpfCorrespondente = "";
		}
		return cpfCorrespondente;
	}

	public void setCpfCorrespondente(String cpfCorrespondente) {
		this.cpfCorrespondente = cpfCorrespondente;
	}

	public String getEmailCorrespondente() {
		if (emailCorrespondente == null) {
			emailCorrespondente = "";
		}
		return emailCorrespondente;
	}

	public void setEmailCorrespondente(String emailCorrespondente) {
		this.emailCorrespondente = emailCorrespondente;
	}

	public String getEmail2Correspondente() {
		if (email2Correspondente == null) {
			email2Correspondente = "";
		}
		return email2Correspondente;
	}

	public void setEmail2Correspondente(String email2Correspondente) {
		this.email2Correspondente = email2Correspondente;
	}

	public Integer getAluno() {
		if (aluno == null) {
			aluno = 0;
		}
		return aluno;
	}

	public void setAluno(Integer aluno) {
		this.aluno = aluno;
	}

	public Integer getTurma() {
		if (turma == null) {
			turma = 0;
		}
		return turma;
	}

	public void setTurma(Integer turma) {
		this.turma = turma;
	}

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AT";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getMsgErroAtualizacaoStatus() {
		if (msgErroAtualizacaoStatus == null) {
			msgErroAtualizacaoStatus = "";
		}
		return msgErroAtualizacaoStatus;
	}

	public void setMsgErroAtualizacaoStatus(String msgErroAtualizacaoStatus) {
		this.msgErroAtualizacaoStatus = msgErroAtualizacaoStatus;
	}

	public Boolean getErroAtualizacaoStatus() {
		if (erroAtualizacaoStatus == null) {
			erroAtualizacaoStatus = Boolean.FALSE;
		}
		return erroAtualizacaoStatus;
	}

	public void setErroAtualizacaoStatus(boolean erroAtualizacaoStatus) {
		this.erroAtualizacaoStatus = erroAtualizacaoStatus;
	}

}