package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;

/**
 * Reponsável por manter os dados da entidade TitulacaoCurso. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class TitulacaoCursoVO extends SuperVO {

    private Integer codigo;
    /** Atributo responsável por manter os objetos da classe <code>ItemTitulacaoCurso</code>. */
    private List<ItemTitulacaoCursoVO> itemTitulacaoCursoVOs;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO curso;

    /**
     * Construtor padrão da classe <code>TitulacaoCurso</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public TitulacaoCursoVO() {
        super();
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>TitulacaoCurso</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>TitulacaoCurso</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /** Retorna Atributo responsável por manter os objetos da classe <code>ItemTitulacaoCurso</code>. */
    public List<ItemTitulacaoCursoVO> getItemTitulacaoCursoVOs() {
        if (itemTitulacaoCursoVOs == null) {
            itemTitulacaoCursoVOs = new ArrayList(0);
        }
        return (itemTitulacaoCursoVOs);
    }

    /** Define Atributo responsável por manter os objetos da classe <code>ItemTitulacaoCurso</code>. */
    public void setItemTitulacaoCursoVOs(List<ItemTitulacaoCursoVO> itemTitulacaoCursoVOs) {
        this.itemTitulacaoCursoVOs = itemTitulacaoCursoVOs;
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

    public void obterNivelItemTitulacaoCurso() {
        NivelFormacaoAcademica nivelFormacaoAcademica;
        for (ItemTitulacaoCursoVO obj : getItemTitulacaoCursoVOs()) {
            nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(obj.getTitulacao());
            obj.setNivelTitulacao(nivelFormacaoAcademica.getNivel());
            if (!obj.getSegundaTitulacao().equals("")) {
                nivelFormacaoAcademica = NivelFormacaoAcademica.getEnum(obj.getSegundaTitulacao());
                obj.setNivelSegundaTitulacao(nivelFormacaoAcademica.getNivel());
            }
        }
    }

    public Integer getPorcentagemTotalUsada() {
        Integer porcentagemTotal = 0;
        for (ItemTitulacaoCursoVO obj : getItemTitulacaoCursoVOs()) {
            porcentagemTotal += obj.getQuantidade();
        }
        return porcentagemTotal;
    }

    public Boolean getItemTitulacaoCursoVOsPreenchidasParcialmente() {
        Boolean preenchidasParcialmente = false;
        for (int i = 0; i < getItemTitulacaoCursoVOs().size(); i++) {
            ItemTitulacaoCursoVO obj = getItemTitulacaoCursoVOs().get(i);
            if (obj.getQuantidadePreenchida() < 100) {
                i = getItemTitulacaoCursoVOs().size();
                preenchidasParcialmente = true;
            }
        }
        return preenchidasParcialmente;
    }
}
