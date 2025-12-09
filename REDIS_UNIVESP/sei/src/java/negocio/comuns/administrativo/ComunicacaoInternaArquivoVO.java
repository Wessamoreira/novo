package negocio.comuns.administrativo;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.arquitetura.SuperVO;

/**
 * Reponsável por manter os dados da entidade ComunicacaoInterna. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ComunicacaoInternaArquivoVO extends SuperVO {

    private Integer codigo;
    private ArquivoVO arquivoAnexo;
    private ComunicacaoInternaVO comunicacaoInterna;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ComunicacaoInterna</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ComunicacaoInternaArquivoVO() {
        super();
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

    public void setArquivoAnexo(ArquivoVO arquivoAnexo) {
        this.arquivoAnexo = arquivoAnexo;
    }

    public ArquivoVO getArquivoAnexo() {
        if (arquivoAnexo == null) {
            arquivoAnexo = new ArquivoVO();
        }
        return arquivoAnexo;
    }

    /**
     * @return the comunicacaoInterna
     */
    public ComunicacaoInternaVO getComunicacaoInterna() {
        if (comunicacaoInterna == null) {
            comunicacaoInterna = new ComunicacaoInternaVO();
        }
        return comunicacaoInterna;
    }

    /**
     * @param comunicacaoInterna the comunicacaoInterna to set
     */
    public void setComunicacaoInterna(ComunicacaoInternaVO comunicacaoInterna) {
        this.comunicacaoInterna = comunicacaoInterna;
    }
}
