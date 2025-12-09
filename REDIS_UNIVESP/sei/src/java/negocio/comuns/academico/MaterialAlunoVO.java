package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade Curso. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MaterialAlunoVO extends SuperVO {

    private Integer codigo;
    private Integer localAula;
    private String descricao;
    private ArquivoVO arquivoVO;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getLocalAula() {
        if (localAula == null) {
            localAula = 0;
        }
        return localAula;
    }

    public void setLocalAula(Integer localAula) {
        this.localAula = localAula;
    }

    /**
     * @return the arquivoVO
     */
    public ArquivoVO getArquivoVO() {
        if (arquivoVO == null) {
            arquivoVO = new ArquivoVO();
        }
        return arquivoVO;
    }

    /**
     * @param arquivoVO the arquivoVO to set
     */
    public void setArquivoVO(ArquivoVO arquivoVO) {
        this.arquivoVO = arquivoVO;
    }

}
