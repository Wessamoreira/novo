/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

/**
 *
 * @author Edigar
 */
import java.io.Serializable;
import java.util.StringTokenizer;

import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;

public class CentroCustoVO implements Serializable {

    private UnidadeEnsinoVO unidadeEnsino;
    private DepartamentoVO departamento;
    private PessoaVO funcionario;
    public static final long serialVersionUID = 1L;

    public CentroCustoVO() {
    }

    public CentroCustoVO(UnidadeEnsinoVO unidadeEnsino, DepartamentoVO departamento, FuncionarioVO funcionario) {
        this.unidadeEnsino = unidadeEnsino;
        this.departamento = departamento;
        this.funcionario = funcionario.getPessoa();
    }

    public CentroCustoVO(UnidadeEnsinoVO unidadeEnsino, DepartamentoVO departamento, PessoaVO funcionario) {
        this.unidadeEnsino = unidadeEnsino;
        this.departamento = departamento;
        this.funcionario = funcionario;
    }

    public CentroCustoVO(String centroCusto) {
        StringTokenizer st = new StringTokenizer(centroCusto, ".");
        if (st.countTokens() == 3) {
            this.unidadeEnsino.setCodigo(Integer.parseInt(st.nextToken()));
            this.departamento.setCodigo(Integer.parseInt(st.nextToken()));
            this.funcionario.setCodigo(Integer.parseInt(st.nextToken()));
        }
    }

    public String getIdentificadorCentroCusto() {
        String idFinal = "";
        if (!getUnidadeEnsino().getCodigo().equals(0)) {
            idFinal = String.valueOf(getUnidadeEnsino().getCodigo());
        }
        if (!getDepartamento().getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(getDepartamento().getCodigo());
        }
        if (!getFuncionario().getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(getFuncionario().getCodigo());
        }
        return (idFinal);
    }

    public String getDescricaoCentroCusto() {
        String idFinal = "";
        if (!getUnidadeEnsino().getCodigo().equals(0)) {
            idFinal = String.valueOf(getUnidadeEnsino().getNome());
        }
        if (!getDepartamento().getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(getDepartamento().getNome());
        }
        if (!getFuncionario().getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(getFuncionario().getNome());
        }
        return (idFinal);
    }

    public static String getDescricaoCentroCusto(UnidadeEnsinoVO unidadeEnsino, DepartamentoVO departamento, FuncionarioVO funcionario) {
        String idFinal = "";
        if (!unidadeEnsino.getCodigo().equals(0)) {
            idFinal = String.valueOf(unidadeEnsino.getCodigo());
        }
        if (!departamento.getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(departamento.getCodigo());
        }
        if (!funcionario.getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(funcionario.getCodigo());
        }
        return (idFinal);
    }

    public static String getCodigoCentroCusto(UnidadeEnsinoVO unidadeEnsino, DepartamentoVO departamento, FuncionarioVO funcionario) {
        String idFinal = "";
        if (!unidadeEnsino.getCodigo().equals(0)) {
            idFinal = String.valueOf(unidadeEnsino.getCodigo());
        }
        if (!departamento.getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(departamento.getCodigo());
        }
        if (!funcionario.getCodigo().equals(0)) {
            idFinal = idFinal + "." + String.valueOf(funcionario.getPessoa().getCodigo());
        }
        return (idFinal);
    }

    public String getDescricaoCentroCusto2() {
        String idFinal = "";
        if (!unidadeEnsino.getCodigo().equals(0)) {
            idFinal = String.valueOf(unidadeEnsino.getNome());
        }
        if (!departamento.getCodigo().equals(0)) {
            idFinal = idFinal + " - " + String.valueOf(departamento.getNome());
        }
        return (idFinal);
    }

    /**
     * @return the unidadeEnsino
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    /**
     * @param unidadeEnsino the unidadeEnsino to set
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the departamento
     */
    public DepartamentoVO getDepartamento() {
        if (departamento == null) {
            departamento = new DepartamentoVO();
        }
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(DepartamentoVO departamento) {
        this.departamento = departamento;
    }

    /**
     * @return the funcionario
     */
    public PessoaVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new PessoaVO();
        }
        return funcionario;
    }

    /**
     * @param funcionario the funcionario to set
     */
    public void setFuncionario(PessoaVO funcionario) {
        this.funcionario = funcionario;
    }
}
