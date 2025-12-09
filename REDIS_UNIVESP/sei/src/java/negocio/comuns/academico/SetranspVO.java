package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Setransp. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class SetranspVO extends SuperVO {

    private Integer codigo;
    private Date dataGeracao;
    private UsuarioVO responsavel;
    private UnidadeEnsinoVO unidadeEnsino;
    private ArquivoVO arquivo;
    public static final String SEPARADOR_COLUNA = ";";
    private List<SetranspAlunoVO> setranspAlunoVOs;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Setransp</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public SetranspVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>SetranspVO</code>.
     */
    public static void validarUnicidade(List<SetranspVO> lista, SetranspVO obj) throws ConsistirException {
        for (SetranspVO repetido : lista) {
            if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
                throw new ConsistirException("Código repetido!");
            }
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>SetranspVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(SetranspVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDataGeracao() == null) {
            throw new ConsistirException("Data nula!");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(null);
        setDataGeracao(new Date());
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public Date getDataGeracao() {
        if (dataGeracao == null) {
            dataGeracao = new Date();
        }
        return (dataGeracao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataGeracao_Apresentar() {
        return (Uteis.getData(dataGeracao));
    }

    public void setDataGeracao(Date dataGeracao) {
        this.dataGeracao = dataGeracao;
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public List<SetranspAlunoVO> getSetranspAlunoVOs() {
        if (setranspAlunoVOs == null) {
            setranspAlunoVOs = new ArrayList<SetranspAlunoVO>(0);
        }
        return setranspAlunoVOs;
    }

    public void setSetranspAlunoVOs(List<SetranspAlunoVO> setranspAlunoVOs) {
        this.setranspAlunoVOs = setranspAlunoVOs;
    }

    public ArquivoVO getArquivo() {
        if (arquivo == null) {
            arquivo = new ArquivoVO();
        }
        return arquivo;
    }

    public void setArquivo(ArquivoVO arquivo) {
        this.arquivo = arquivo;
    }
}
