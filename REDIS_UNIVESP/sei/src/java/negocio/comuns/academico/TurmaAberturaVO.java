package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
/**
 * Reponsável por manter os dados da entidade TurmaAbertura. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Turma
 */
import negocio.comuns.utilitarias.Uteis;

public class TurmaAberturaVO extends SuperVO {

    private Integer codigo;
    private TurmaVO turma;
    private UsuarioVO usuario;
    private String situacao;
    private Date data;
    private Date dataAdiada;
    private Integer qtdeAlunoMatriculado;
    private Integer qtdeAlunoPreMatriculado;
    //Atributo apenas para controle
    private Boolean abrirModalEnviarEmail;
    private String acaoTurmaBase;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TurmaAbertura</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TurmaAberturaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TurmaAberturaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TurmaAberturaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getSituacao() == null || obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Turma Abertura) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Turma Abertura) deve ser informado.");
        }
    }

    public void inicializarDados() {
        setCodigo(0);
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return (turma);
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
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

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public String getSituacao_Apresentar() {
        if (getSituacao().equals("AC")) {
            return "A CONFIRMAR";
        }
        if (getSituacao().equals("AD")) {
            return "ADIADA";
        }
        if (getSituacao().equals("AN")) {
        	return "ANTECIPADA";
        }
        if (getSituacao().equals("CO")) {
            return "CONFIRMADA";
        }
        if (getSituacao().equals("IN")) {
            return "INAUGURADA";
        }
        if (getSituacao().equals("CA")) {
            return "CANCELADA";
        }
        return (getSituacao());
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getData_Apresentar() {
        return Uteis.getData(data);
    }

    public String getDataAdiada_Apresentar() {
        if (getDataAdiada() == null) {
            return "";
        }
        if(getDataAdiada().after(getData())){
        	return "Adiada para " + Uteis.getData(dataAdiada);
        }else if(getDataAdiada().before(getData())){
        	return "Antecipada para " + Uteis.getData(dataAdiada);
        }
        return "";
    }

    public Boolean getIsSituacaoAdiadaComDataAdiada() {
        if (getSituacao().equals("AD") && getDataAdiada() != null) {
            return true;
        }
        return false;
    }

    /**
     * @return the usuario
     */
    public UsuarioVO getUsuario() {
        if (usuario == null) {
            usuario = new UsuarioVO();
        }
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(UsuarioVO usuario) {
        this.usuario = usuario;
    }

    public Integer getQtdeAlunoMatriculado() {
        if (qtdeAlunoMatriculado == null) {
            qtdeAlunoMatriculado = 0;
        }
        return qtdeAlunoMatriculado;
    }

    public void setQtdeAlunoMatriculado(Integer qtdeAlunoMatriculado) {
        this.qtdeAlunoMatriculado = qtdeAlunoMatriculado;
    }

    public Date getDataAdiada() {
        return dataAdiada;
    }

    public void setDataAdiada(Date dataAdiada) {
        this.dataAdiada = dataAdiada;
    }

    public Boolean getAbrirModalEnviarEmail() {
        if (abrirModalEnviarEmail == null) {
            abrirModalEnviarEmail = Boolean.FALSE;
        }
        return abrirModalEnviarEmail;
    }

    public void setAbrirModalEnviarEmail(Boolean abrirModalEnviarEmail) {
        this.abrirModalEnviarEmail = abrirModalEnviarEmail;
    }

    /**
     * @return the qtdeAlunoPreMatriculado
     */
    public Integer getQtdeAlunoPreMatriculado() {
        if (qtdeAlunoPreMatriculado == null) {
            qtdeAlunoPreMatriculado = 0;
        }
        return qtdeAlunoPreMatriculado;
    }

    /**
     * @param qtdeAlunoPreMatriculado the qtdeAlunoPreMatriculado to set
     */
    public void setQtdeAlunoPreMatriculado(Integer qtdeAlunoPreMatriculado) {
        this.qtdeAlunoPreMatriculado = qtdeAlunoPreMatriculado;
    }

	public String getAcaoTurmaBase() {
		if(acaoTurmaBase == null) {
			acaoTurmaBase = "";
		}
		return acaoTurmaBase;
	}

	public void setAcaoTurmaBase(String acaoTurmaBase) {
		this.acaoTurmaBase = acaoTurmaBase;
	}
    
    
}
