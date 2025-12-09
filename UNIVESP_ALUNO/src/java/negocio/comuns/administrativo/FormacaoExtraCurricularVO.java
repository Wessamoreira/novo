package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.facade.jdbc.basico.Pessoa;

/**
 * Reponsável por manter os dados da entidade FormacaoAcademica. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pessoa
 */
public class FormacaoExtraCurricularVO extends SuperVO {

    private Integer codigo;
    private String instituicao;
    private String curso;
    private Integer anoRealizacaoConclusao;
    private String cargaHoraria;
    private PessoaVO pessoa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>FormacaoAcademica</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public FormacaoExtraCurricularVO() {
        super();
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public String getCurso() {
        return (curso);
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getInstituicao() {
        if (instituicao == null) {
            instituicao = "";
        }
        return (instituicao);
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
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

    public Integer getAnoRealizacaoConclusao() {
    	if (anoRealizacaoConclusao == null) {
    		anoRealizacaoConclusao = 0;
    	}
        return anoRealizacaoConclusao;
    }

    public void setAnoRealizacaoConclusao(Integer anoRealizacaoConclusao) {
        this.anoRealizacaoConclusao = anoRealizacaoConclusao;
    }

    public String getCargaHoraria() {
        if (cargaHoraria == null) {
            cargaHoraria = "";
        }
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }
}
