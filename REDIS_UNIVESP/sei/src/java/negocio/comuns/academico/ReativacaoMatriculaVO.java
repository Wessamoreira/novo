package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.sad.MatriculaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;

/**
 * Reponsável por manter os dados da entidade Cancelamento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ReativacaoMatriculaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String descricao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaVO matricula;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Requerimento </code>.
     */
    private RequerimentoVO requerimento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO responsavelAutorizacao;
    private TrancamentoVO trancamento;
    private String situacao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Cancelamento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ReativacaoMatriculaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CancelamentoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public void validarSituacaoRequerimento(RequerimentoVO obj) throws ConsistirException {
        if (obj.getSituacao().equals(SituacaoRequerimento.AGUARDANDO_PAGAMENTO.getValor())) {
            throw new ConsistirException("Requerimento especificado está aguardando pagamento.");
        }
        if (obj.getMatricula().getSituacao().equals("AT")) {
            throw new ConsistirException("Matrícula especificada já está ativa.");
        }
        if (obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor())
                || obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
            throw new ConsistirException("Requerimento especificado já está finalizado.");
        }
    }

    public static void validarDados(ReativacaoMatriculaVO obj, Boolean permiteReativacaoMatriculaSemRequerimento) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Reativação) deve ser informado.");
        }
        if (obj.getRequerimento().getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Reativação) deve ser informado.");
        }
        if ((obj.getMatricula().getMatricula() == null) || (obj.getMatricula().getMatricula().equals(""))) {
            throw new ConsistirException("O campo MATRÍCULA (Reativação) deve ser informado.");
        }
        if (!permiteReativacaoMatriculaSemRequerimento && (obj.getRequerimento() == null || obj.getRequerimento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CÓDIGO REQUERIMENTO (Reativação) deve ser informado.");
        }
        if ((obj.getResponsavelAutorizacao() == null) || (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL AUTORIZACÃO (Reativação) deve ser informado.");
        }
//        if ((obj.getTrancamento() == null) || (obj.getTrancamento().getCodigo().intValue() == 0)) {
//            throw new ConsistirException("O campo TRANCAMENTO (Reativação) deve ser informado.");
//        }
    }

    public UsuarioVO getResponsavelAutorizacao() {
        if (responsavelAutorizacao == null) {
            responsavelAutorizacao = new UsuarioVO();
        }
        return responsavelAutorizacao;
    }

    public void setResponsavelAutorizacao(UsuarioVO responsavelAutorizacao) {
        this.responsavelAutorizacao = responsavelAutorizacao;
    }

    public RequerimentoVO getRequerimento() {
        if (requerimento == null) {
            requerimento = new RequerimentoVO();
        }
        return (requerimento);
    }

    public void setRequerimento(RequerimentoVO obj) {
        this.requerimento = obj;
    }

    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return (matricula);
    }

    public void setMatricula(MatriculaVO obj) {
        this.matricula = obj;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getData_Apresentar() {
        return (Uteis.getData(data));
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

    public MatriculaDWVO criarMatriculaDW(Integer processoMatricula, Integer peso) {
        MatriculaDWVO obj = new MatriculaDWVO();
        obj.setAno(Uteis.getAnoData(getData()));
        obj.setMes(Uteis.getMesData(getData()));
        obj.getCurso().setCodigo(getMatricula().getCurso().getCodigo());
        obj.getAreaConhecimento().setCodigo(getMatricula().getCurso().getAreaConhecimento().getCodigo());
        obj.setData(getData());
        obj.setNivelEducacional(obj.getCurso().getNivelEducacional());
        obj.getProcessoMatricula().setCodigo(processoMatricula);
        obj.setSituacao("RM");
        obj.setPeso(peso);
        obj.getTurno().setCodigo(getMatricula().getTurno().getCodigo());
        obj.getUnidadeEnsino().setCodigo(getMatricula().getUnidadeEnsino().getCodigo());
        return obj;
    }

    /**
     * @return the trancamento
     */
    public TrancamentoVO getTrancamento() {
        if (trancamento == null) {
            trancamento = new TrancamentoVO();
        }
        return trancamento;
    }

    /**
     * @param trancamento the trancamento to set
     */
    public void setTrancamento(TrancamentoVO trancamento) {
        this.trancamento = trancamento;
    }

    public String getSituacao_Apresentar() {
        if (getRequerimento().getCodigo().equals(0) && !getNovoObj()) {
            getRequerimento().setSituacao(getSituacao());
        }
        return getRequerimento().getSituacao_Apresentar();
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
