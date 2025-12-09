package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ComunicadoInternoRegistroLeitura.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ComunicadoInternoRegistroLeituraVO extends SuperVO {

    private Integer codigo;
    private Integer comunicadoInterno;
    private Date dataLeitura;
    private Integer destinatario;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ComunicadoInternoRegistroLeitura</code>
     * . Cria uma nova instância desta entidade, inicializando automaticamente
     * seus atributos (Classe VO).
     */
    public ComunicadoInternoRegistroLeituraVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ComunicadoInternoRegistroLeituraVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ComunicadoInternoRegistroLeituraVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getComunicadoInterno().intValue() == 0) {
            throw new ConsistirException("O campo COMUNICADO INTERNO (Comunicado Interno Registro Leitura) deve ser informado.");
        }
        if (obj.getDestinatario().intValue() == 0) {
            throw new ConsistirException("O campo DESTINATÁRIO (Comunicado Interno Registro Leitura) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setDataLeitura(new Date());
    }

    public Integer getDestinatario() {
        if (destinatario == null) {
            destinatario = 0;
        }
        return (destinatario);
    }

    public void setDestinatario(Integer destinatario) {
        this.destinatario = destinatario;
    }

    public Date getDataLeitura() {
        return (dataLeitura);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataLeitura_Apresentar() {
        return (Uteis.getData(getDataLeitura()));
    }

    public void setDataLeitura(Date dataLeitura) {
        this.dataLeitura = dataLeitura;
    }

    public Integer getComunicadoInterno() {
        if (comunicadoInterno == null) {
            comunicadoInterno = 0;
        }
        return (comunicadoInterno);
    }

    public void setComunicadoInterno(Integer comunicadoInterno) {
        this.comunicadoInterno = comunicadoInterno;
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
}
