package negocio.comuns.protocolo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class ControleCorrespondenciaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String remetente;
    private String destinatario;
    private String assunto;
    private String situacao;
    private String tipoCorrespondencia;
    private String descricao;
    private Date dataRecebProtocoloOrigem;
    private Date dataRecebProtocoloDestino;
    private Date dataRecebDptoDestino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelDptoOrigem;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelProtocoloOrigem;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelProtocoloDestino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelDptoDestino;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ControleCorrespondencia</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ControleCorrespondenciaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ControleCorrespondenciaVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ControleCorrespondenciaVO obj) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Controle de Correspondências) deve ser informado.");
        }
        if (obj.getRemetente().equals("")) {
            throw new ConsistirException("O campo REMETENTE (Controle de Correspondências) deve ser informado.");
        }
        if (obj.getDestinatario().equals("")) {
            throw new ConsistirException("O campo DESTINATÁRIO (Controle de Correspondências) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Controle de Correspondências) deve ser informado.");
        }
        if (obj.getTipoCorrespondencia().equals("")) {
            throw new ConsistirException("O campo TIPO CORRESPONDÊNCIA (Controle de Correspondências) deve ser informado.");
        }
        if ((obj.getResponsavelDptoOrigem() == null) || (obj.getResponsavelDptoOrigem().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL DPTO ORIGEM (Controle de Correspondências) deve ser informado.");
        }
        if ((obj.getResponsavelProtocoloOrigem() == null)
                || (obj.getResponsavelProtocoloOrigem().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL PROTOCOLO ORIGEM (Controle de Correspondências) deve ser informado.");
        }
        if ((obj.getResponsavelProtocoloDestino() == null)
                || (obj.getResponsavelProtocoloDestino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL PROTOCOLO DESTINO (Controle de Correspondências) deve ser informado.");
        }
        if ((obj.getResponsavelDptoDestino() == null) || (obj.getResponsavelDptoDestino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL DPTO DESTINO (Controle de Correspondências) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setRemetente("");
        setDestinatario("");
        setAssunto("");
        setSituacao("");
        setTipoCorrespondencia("");
        setDescricao("");
        setDataRecebProtocoloOrigem(new Date());
        setDataRecebProtocoloDestino(new Date());
        setDataRecebDptoDestino(new Date());
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public PessoaVO getResponsavelDptoDestino() {
        if (responsavelDptoDestino == null) {
            responsavelDptoDestino = new PessoaVO();
        }
        return (responsavelDptoDestino);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public void setResponsavelDptoDestino(PessoaVO obj) {
        this.responsavelDptoDestino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public PessoaVO getResponsavelProtocoloDestino() {
        if (responsavelProtocoloDestino == null) {
            responsavelProtocoloDestino = new PessoaVO();
        }
        return (responsavelProtocoloDestino);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public void setResponsavelProtocoloDestino(PessoaVO obj) {
        this.responsavelProtocoloDestino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public PessoaVO getResponsavelProtocoloOrigem() {
        if (responsavelProtocoloOrigem == null) {
            responsavelProtocoloOrigem = new PessoaVO();
        }
        return (responsavelProtocoloOrigem);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public void setResponsavelProtocoloOrigem(PessoaVO obj) {
        this.responsavelProtocoloOrigem = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public PessoaVO getResponsavelDptoOrigem() {
        if (responsavelDptoOrigem == null) {
            responsavelDptoOrigem = new PessoaVO();
        }
        return (responsavelDptoOrigem);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ControleCorrespondencia</code>).
     */
    public void setResponsavelDptoOrigem(PessoaVO obj) {
        this.responsavelDptoOrigem = obj;
    }

    public Date getDataRecebDptoDestino() {
        return (dataRecebDptoDestino);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataRecebDptoDestino_Apresentar() {
        return (Uteis.getData(dataRecebDptoDestino));
    }

    public void setDataRecebDptoDestino(Date dataRecebDptoDestino) {
        this.dataRecebDptoDestino = dataRecebDptoDestino;
    }

    public Date getDataRecebProtocoloDestino() {
        return (dataRecebProtocoloDestino);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataRecebProtocoloDestino_Apresentar() {
        return (Uteis.getData(dataRecebProtocoloDestino));
    }

    public void setDataRecebProtocoloDestino(Date dataRecebProtocoloDestino) {
        this.dataRecebProtocoloDestino = dataRecebProtocoloDestino;
    }

    public Date getDataRecebProtocoloOrigem() {
        return (dataRecebProtocoloOrigem);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataRecebProtocoloOrigem_Apresentar() {
        return (Uteis.getData(dataRecebProtocoloOrigem));
    }

    public void setDataRecebProtocoloOrigem(Date dataRecebProtocoloOrigem) {
        this.dataRecebProtocoloOrigem = dataRecebProtocoloOrigem;
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

    public String getTipoCorrespondencia() {
        return (tipoCorrespondencia);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoCorrespondencia_Apresentar() {
        if (tipoCorrespondencia.equals("CO")) {
            return "Convencional";
        }
        if (tipoCorrespondencia.equals("UG")) {
            return "Urgente";
        }
        if (tipoCorrespondencia.equals("UR")) {
            return "Urgente Rápido";
        }
        return (tipoCorrespondencia);
    }

    public void setTipoCorrespondencia(String tipoCorrespondencia) {
        this.tipoCorrespondencia = tipoCorrespondencia;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("RE")) {
            return "Recebido";
        }
        if (situacao.equals("TR")) {
            return "Em Tramite";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getAssunto() {
        return (assunto);
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDestinatario() {
        return (destinatario);
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getRemetente() {
        return (remetente);
    }

    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }

    public Date getData() {
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
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
