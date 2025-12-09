package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.facade.jdbc.academico.TitulacaoCurso;

/**
 * Reponsável por manter os dados da entidade ItemTitulacaoCurso. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see TitulacaoCurso
 */
public class ItemTitulacaoCursoVO extends SuperVO {

    private Integer codigo;
    private TitulacaoCursoVO titulacaoCurso;
    private String titulacao;
    private String segundaTitulacao;
    private Integer quantidade;
    private Integer quantidadePreenchida;
    private Integer nivelTitulacao;
    private Integer nivelSegundaTitulacao;
    private Integer totalProfessores;
    private Integer porcentagemTitulacao;

    /**
     * Construtor padrão da classe <code>ItemTitulacaoCurso</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ItemTitulacaoCursoVO() {
        super();
    }

    public Integer getQuantidade() {
        if (quantidade == null) {
            quantidade = 0;
        }
        return (quantidade);
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getTitulacao() {
        if (titulacao == null) {
            return "";
        }
        return (titulacao);
    }

    public String getTitulacao_Apresentar() {
        if (!getSegundaTitulacao().equals("")) {
            return NivelFormacaoAcademica.getDescricao(getTitulacao()) + "/" + NivelFormacaoAcademica.getDescricao(getSegundaTitulacao());
        }
        return NivelFormacaoAcademica.getDescricao(getTitulacao());
    }

    public void setTitulacao(String titulacao) {
        this.titulacao = titulacao;
    }

    public TitulacaoCursoVO getTitulacaoCurso() {
        if (titulacaoCurso == null) {
            titulacaoCurso = new TitulacaoCursoVO();
        }
        return (titulacaoCurso);
    }

    public void setTitulacaoCurso(TitulacaoCursoVO titulacaoCurso) {
        this.titulacaoCurso = titulacaoCurso;
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

    public Integer getQuantidadePreenchida() {
        if (quantidadePreenchida == null) {
            quantidadePreenchida = 0;
        }
        return quantidadePreenchida;
    }

    public void setQuantidadePreenchida(Integer quantidadePreenchida) {
        this.quantidadePreenchida = quantidadePreenchida;
    }

    public String getSegundaTitulacao() {
        if (segundaTitulacao == null || segundaTitulacao.equals("0")) {
            segundaTitulacao = "";
        }
        return segundaTitulacao;
    }

    public void setSegundaTitulacao(String segundaTitulacao) {
        this.segundaTitulacao = segundaTitulacao;
    }

    public Boolean getTitulacaoPreenchida() {
        if (!getTitulacao().equals("0") && !getTitulacao().equals("")) {
            return true;
        }
        return false;
    }

    public Integer getNivelTitulacao() {
        if (nivelTitulacao == null) {
            nivelTitulacao = 0;
        }
        return nivelTitulacao;
    }

    public void setNivelTitulacao(Integer nivelTitulacao) {
        this.nivelTitulacao = nivelTitulacao;
    }

    public Integer getNivelSegundaTitulacao() {
        if (nivelSegundaTitulacao == null) {
            nivelSegundaTitulacao = 0;
        }
        return nivelSegundaTitulacao;
    }

    public void setNivelSegundaTitulacao(Integer nivelSegundaTitulacao) {
        this.nivelSegundaTitulacao = nivelSegundaTitulacao;
    }

    public Integer getTotalProfessores() {
        if (totalProfessores == null) {
            totalProfessores = 0;
        }
        return totalProfessores;
    }

    public void setTotalProfessores(Integer totalProfessores) {
        this.totalProfessores = totalProfessores;
    }

    public Integer getMenorNivelTitulacaoCurso() {
        Integer menorNivel = 0;
        menorNivel = this.getNivelTitulacao();
        if (!this.getSegundaTitulacao().equals("")) {
            if (menorNivel > this.getNivelSegundaTitulacao()) {
                menorNivel = this.getNivelSegundaTitulacao();
            }
        }
        return menorNivel;
    }

    public Integer getPorcentagemTitulacao() {
        if (porcentagemTitulacao == null) {
            porcentagemTitulacao = 0;
        }
        return porcentagemTitulacao;
    }

    public void setPorcentagemTitulacao(Integer porcentagemTitulacao) {
        this.porcentagemTitulacao = porcentagemTitulacao;
    }
}
