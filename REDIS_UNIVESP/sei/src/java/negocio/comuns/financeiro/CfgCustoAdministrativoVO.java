package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CfgCustoAdministrativo. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class CfgCustoAdministrativoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private String tipoCusto;
    private Double valorHoraAulaNaoGraduado;
    private Double valorHoraAulaNaoGraduadoComImpostos;
    private Double valorHoraAulaGraduado;
    private Double valorHoraAulaGraduadoComImpostos;
    private Double valorHoraAulaEspecialista;
    private Double valorHoraAulaEspecialistaComImpostos;
    private Double valorHoraAulaMestre;
    private Double valorHoraAulaMestreComImpostos;
    private Double valorHoraAulaDoutor;
    private Double valorHoraAulaDoutorComImpostos;
    private Double valorHoraAulaConvidado;
    private Double valorHoraAulaConvidadoComImpostos;
    private Double custoAdministrativoAluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private UsuarioVO responsavel;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    private ConfiguracoesVO configuracoesVO;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>CfgCustoAdministrativo</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public CfgCustoAdministrativoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CfgCustoAdministrativoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CfgCustoAdministrativoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        // if (obj.getData() == null) {
        // throw new
        // ConsistirException("O campo DATA (Configuração de Custos Administrativos) deve ser informado.");
        // }
        // if ((obj.getResponsavel() == null)
        // || (obj.getResponsavel().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo RESPONSÁVEL (Configuração de Custos Administrativos) deve ser informado.");
        // }
        // if (obj.getTipoCusto() != null && obj.getTipoCusto().equals("")) {
        // throw new
        // ConsistirException("O campo TIPO DE CUSTO (Configuração de Custos Administrativos) deve ser informado.");
        // }
        if (obj.getConfiguracoesVO() != null && obj.getConfiguracoesVO().getCodigo() != null
                && obj.getConfiguracoesVO().getCodigo().intValue() == 0) {
            throw new ConsistirException("Esta configuração não pode ser salva, (CONFIGURAÇÕES) ainda não foi salvo");
        }
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>CfgCustoAdministrativo</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>CfgCustoAdministrativo</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>CfgCustoAdministrativo</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>CfgCustoAdministrativo</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    public Double getCustoAdministrativoAluno() {
        if (custoAdministrativoAluno == null) {
            custoAdministrativoAluno = 0.0;
        }
        return (custoAdministrativoAluno);
    }

    public void setCustoAdministrativoAluno(Double custoAdministrativoAluno) {
        this.custoAdministrativoAluno = custoAdministrativoAluno;
    }

    public Double getValorHoraAulaConvidadoComImpostos() {
        if (valorHoraAulaConvidadoComImpostos == null) {
            valorHoraAulaConvidadoComImpostos = 0.0;
        }
        return (valorHoraAulaConvidadoComImpostos);
    }

    public void setValorHoraAulaConvidadoComImpostos(Double valorHoraAulaConvidadoComImpostos) {
        this.valorHoraAulaConvidadoComImpostos = valorHoraAulaConvidadoComImpostos;
    }

    public Double getValorHoraAulaConvidado() {
        if (valorHoraAulaConvidado == null) {
            valorHoraAulaConvidado = 0.0;
        }
        return (valorHoraAulaConvidado);
    }

    public void setValorHoraAulaConvidado(Double valorHoraAulaConvidado) {
        this.valorHoraAulaConvidado = valorHoraAulaConvidado;
    }

    public Double getValorHoraAulaDoutorComImpostos() {
        if (valorHoraAulaDoutorComImpostos == null) {
            valorHoraAulaDoutorComImpostos = 0.0;
        }
        return (valorHoraAulaDoutorComImpostos);
    }

    public void setValorHoraAulaDoutorComImpostos(Double valorHoraAulaDoutorComImpostos) {
        this.valorHoraAulaDoutorComImpostos = valorHoraAulaDoutorComImpostos;
    }

    public Double getValorHoraAulaDoutor() {
        if (valorHoraAulaDoutor == null) {
            valorHoraAulaDoutor = 0.0;
        }
        return (valorHoraAulaDoutor);
    }

    public void setValorHoraAulaDoutor(Double valorHoraAulaDoutor) {
        this.valorHoraAulaDoutor = valorHoraAulaDoutor;
    }

    public Double getValorHoraAulaMestreComImpostos() {
        if (valorHoraAulaMestreComImpostos == null) {
            valorHoraAulaMestreComImpostos = 0.0;
        }
        return (valorHoraAulaMestreComImpostos);
    }

    public void setValorHoraAulaMestreComImpostos(Double valorHoraAulaMestreComImpostos) {
        this.valorHoraAulaMestreComImpostos = valorHoraAulaMestreComImpostos;
    }

    public Double getValorHoraAulaMestre() {
        if (valorHoraAulaMestre == null) {
            valorHoraAulaMestre = 0.0;
        }
        return (valorHoraAulaMestre);
    }

    public void setValorHoraAulaMestre(Double valorHoraAulaMestre) {
        this.valorHoraAulaMestre = valorHoraAulaMestre;
    }

    public Double getValorHoraAulaEspecialistaComImpostos() {
        if (valorHoraAulaEspecialistaComImpostos == null) {
            valorHoraAulaEspecialistaComImpostos = 0.0;
        }
        return (valorHoraAulaEspecialistaComImpostos);
    }

    public void setValorHoraAulaEspecialistaComImpostos(Double valorHoraAulaEspecialistaComImpostos) {
        this.valorHoraAulaEspecialistaComImpostos = valorHoraAulaEspecialistaComImpostos;
    }

    public Double getValorHoraAulaEspecialista() {
        if (valorHoraAulaEspecialista == null) {
            valorHoraAulaEspecialista = 0.0;
        }
        return (valorHoraAulaEspecialista);
    }

    public void setValorHoraAulaEspecialista(Double valorHoraAulaEspecialista) {
        this.valorHoraAulaEspecialista = valorHoraAulaEspecialista;
    }

    public Double getValorHoraAulaGraduadoComImpostos() {
        if (valorHoraAulaGraduadoComImpostos == null) {
            valorHoraAulaGraduadoComImpostos = 0.0;
        }
        return (valorHoraAulaGraduadoComImpostos);
    }

    public void setValorHoraAulaGraduadoComImpostos(Double valorHoraAulaGraduadoComImpostos) {
        this.valorHoraAulaGraduadoComImpostos = valorHoraAulaGraduadoComImpostos;
    }

    public Double getValorHoraAulaGraduado() {
        if (valorHoraAulaGraduado == null) {
            valorHoraAulaGraduado = 0.0;
        }
        return (valorHoraAulaGraduado);
    }

    public void setValorHoraAulaGraduado(Double valorHoraAulaGraduado) {
        this.valorHoraAulaGraduado = valorHoraAulaGraduado;
    }

    public Double getValorHoraAulaNaoGraduadoComImpostos() {
        if (valorHoraAulaNaoGraduadoComImpostos == null) {
            valorHoraAulaNaoGraduadoComImpostos = 0.0;
        }
        return (valorHoraAulaNaoGraduadoComImpostos);
    }

    public void setValorHoraAulaNaoGraduadoComImpostos(Double valorHoraAulaNaoGraduadoComImpostos) {
        this.valorHoraAulaNaoGraduadoComImpostos = valorHoraAulaNaoGraduadoComImpostos;
    }

    public Double getValorHoraAulaNaoGraduado() {
        if (valorHoraAulaNaoGraduado == null) {
            valorHoraAulaNaoGraduado = 0.0;
        }
        return (valorHoraAulaNaoGraduado);
    }

    public void setValorHoraAulaNaoGraduado(Double valorHoraAulaNaoGraduado) {
        this.valorHoraAulaNaoGraduado = valorHoraAulaNaoGraduado;
    }

    public String getTipoCusto() {
        if (tipoCusto == null) {
            tipoCusto = "";
        }
        return (tipoCusto);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoCusto_Apresentar() {
        if (tipoCusto.equals("UE")) {
            return "Unidade de Ensino";
        }
        if (tipoCusto.equals("CE")) {
            return "Curso Específico Unidade de Ensino";
        }
        return (tipoCusto);
    }

    public void setTipoCusto(String tipoCusto) {
        this.tipoCusto = tipoCusto;
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
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the configuracoesVO
     */
    public ConfiguracoesVO getConfiguracoesVO() {
        if (configuracoesVO == null) {
            configuracoesVO = new ConfiguracoesVO();
        }
        return configuracoesVO;
    }

    /**
     * @param configuracoesVO
     *            the configuracoesVO to set
     */
    public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
        this.configuracoesVO = configuracoesVO;
    }
}
