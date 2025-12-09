package negocio.comuns.contabil;

import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Setransp. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class SpedContabilVO extends SuperVO {

    private Integer codigo;
    private byte[] arquivo;
    private Date dataInicial;
    private Date dataFinal;
    public static final long serialVersionUID = 1L;

    public SpedContabilVO() {
    }

    public static void validarUnicidade(List<SpedContabilVO> lista, SpedContabilVO obj) throws ConsistirException {
        for (SpedContabilVO repetido : lista) {
            if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
                throw new ConsistirException("Código repetido!");
            }
        }
    }

    public static void validarDados(SpedContabilVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataFinal() {
        return dataFinal;
    }
}
