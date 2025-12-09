package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Autor. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade private e os métodos
 * de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
 public class AutorVariacaoNomeVO extends SuperVO {

    private Integer codigo;
    private String variacaoNome;
    private AutorVO autor;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getVariacaoNome() {
        return variacaoNome;
    }

    public void setVariacaoNome(String variacaoNome) {
        this.variacaoNome = variacaoNome;
    }

    public AutorVO getAutor() {
        return autor;
    }

    public void setAutor(AutorVO autor) {
        this.autor = autor;
    }

}
