package negocio.comuns.processosel;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Inscricao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PreInscricaoVO extends SuperVO {

    private Integer codigo;
    private UnidadeEnsinoVO unidadeEnsino;
    private CursoVO curso;
    private Date data;
    /*
     * Os dados abaixo sao mantidos na pré-inscrição, pois pode ocorrer
     * de haver uma divergencia entre dados informados na pré-inscrição e dados
     * da pessoa já existente na base do SEI. Por exemplo, as vezes a pessoa
     * informa na pre outro telefone diferente do existente em seu cadastro.
     * Contudo, isto não quer dizer este novo telefone deverá ir para seu cadastro oficial
     * Estes dados serão apresentados na pré-inscrição quando forem divergentes facilitando o
     * trabalho de consultor que ligar para o prospect, no sentido de confirmar e atualizar
     * os dados de forma segura.
     */
    private String nome;
    private String nomeBatismo;
    private String telefoneResidencial;
    private String telefoneComercial;
    private String celular;
    private String email;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private ProspectsVO prospect;    
    private FuncionarioVO responsavel;
    
    //Campo não persistido no banco somente para controle de tela.
    private CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO;
    
    private Boolean reenvioPreInscricao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Inscricao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PreInscricaoVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Inscricao</code>).
     */
    public FuncionarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new FuncionarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Inscricao</code>).
     */
    public void setResponsavel(FuncionarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Inscricao</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>Inscricao</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
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

    /**
     * @return the curso
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    /**
     * @param curso the curso to set
     */
    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    /**
     * @return the prospect
     */
    public ProspectsVO getProspect() {
        if (prospect == null) {
            prospect = new ProspectsVO();
        }
        return prospect;
    }

    /**
     * @param prospect the prospect to set
     */
    public void setProspect(ProspectsVO prospect) {
        this.prospect = prospect;
    }

    public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorarioVO() {
         if (compromissoAgendaPessoaHorarioVO == null) {
            compromissoAgendaPessoaHorarioVO = new CompromissoAgendaPessoaHorarioVO();
        }
        return compromissoAgendaPessoaHorarioVO;
    }

    public void setCompromissoAgendaPessoaHorarioVO(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO) {
        this.compromissoAgendaPessoaHorarioVO = compromissoAgendaPessoaHorarioVO;
    }
    
    public String getHistoricoCriacaoProspect() {
        String historico = "Prospect cadastrado por meio de uma pré-inscrição, no dia " + this.getData_Apresentar() +
                           " com Nome: " + this.getProspect().getNome() + " E-mail: " + this.getProspect().getEmailPrincipal();
        return historico;
    }

    /**
     * @return the telefoneResidencial
     */
    public String getTelefoneResidencial() {
        if (telefoneResidencial == null) {
            telefoneResidencial = "";
        }
        return telefoneResidencial;
    }

    /**
     * @param telefoneResidencial the telefoneResidencial to set
     */
    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    /**
     * @return the telefoneComercial
     */
    public String getTelefoneComercial() {
        if (telefoneComercial == null) {
            telefoneComercial = "";
        }
        return telefoneComercial;
    }

    /**
     * @param telefoneComercial the telefoneComercial to set
     */
    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    /**
     * @return the celular
     */
    public String getCelular() {
        if (celular == null) {
            celular = "";
        }
        return celular;
    }

    /**
     * @param celular the celular to set
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * @return the nomeBatismo
     */
    public String getNomeBatismo() {
        if (nomeBatismo == null) {
        	nomeBatismo = "";
        }
        return nomeBatismo;
    }

    /**
     * @param nome the nomeBatismo to set
     */
    public void setNomeBatismo(String nomeBatismo) {
        this.nomeBatismo = nomeBatismo;
    }
    
    private Boolean matriculaOnlineExterna;

	public Boolean getMatriculaOnlineExterna() {
		if(matriculaOnlineExterna == null) {
			matriculaOnlineExterna = false;
		}
		return matriculaOnlineExterna;
	}

	public void setMatriculaOnlineExterna(Boolean matriculaOnlineExterna) {
		this.matriculaOnlineExterna = matriculaOnlineExterna;
	}
	
	public Boolean getReenvioPreInscricao() {
		if (reenvioPreInscricao == null) {
			reenvioPreInscricao = false;
		}
		return reenvioPreInscricao;
	}

	public void setReenvioPreInscricao(Boolean reenvioPreInscricao) {
		this.reenvioPreInscricao = reenvioPreInscricao;
	}
}
