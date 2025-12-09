package negocio.comuns.biblioteca;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

public class ItensRenovadosVO extends SuperVO {

    private PessoaVO pessoa;
    private ExemplarVO exemplar;
    private Integer nrRenovacao;
    public static final long serialVersionUID = 1L;

    public ItensRenovadosVO() {
        super();
        inicializarDados();
    }

    public static void validarDados(ItensRenovadosVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getPessoa().getCodigo() == 0) {
            throw new ConsistirException("A PESSOA deve ser informada.");
        }
        if (obj.getExemplar().getCodigo() == 0) {
            throw new ConsistirException("O EXEMPLAR deve ser informada.");
        }
    }

    public void inicializarDados() {
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

    public ExemplarVO getExemplar() {
        if (exemplar == null) {
            exemplar = new ExemplarVO();
        }
        return exemplar;
    }

    public void setExemplar(ExemplarVO exemplar) {
        this.exemplar = exemplar;
    }

    public Integer getNrRenovacao() {
        if (nrRenovacao == null) {
            nrRenovacao = 0;
        }
        return nrRenovacao;
    }

    public void setNrRenovacao(Integer nrRenovacao) {
        this.nrRenovacao = nrRenovacao;
    }
}
