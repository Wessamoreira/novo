package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

/**
 *
 * @author Carlos
 */
public class NumeroDocumentoContaReceberVO extends SuperVO{
    private Integer codigo;
    private PessoaVO pessoaVO;
    private ParceiroVO parceiroVO;
    private Integer incremental;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public PessoaVO getPessoaVO() {
        if (pessoaVO == null) {
            pessoaVO = new PessoaVO();
        }
        return pessoaVO;
    }

    public void setPessoaVO(PessoaVO pessoaVO) {
        this.pessoaVO = pessoaVO;
    }

    public ParceiroVO getParceiroVO() {
        if (parceiroVO == null) {
            parceiroVO = new ParceiroVO();
        }
        return parceiroVO;
    }

    public void setParceiroVO(ParceiroVO parceiroVO) {
        this.parceiroVO = parceiroVO;
    }

    public Integer getIncremental() {
        if (incremental == null) {
            incremental = 0;
        }
        return incremental;
    }

    public void setIncremental(Integer incremental) {
        this.incremental = incremental;
    }
}
