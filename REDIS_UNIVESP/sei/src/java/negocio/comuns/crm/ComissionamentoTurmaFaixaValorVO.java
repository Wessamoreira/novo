/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package negocio.comuns.crm;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Otimize-04
 */
public class ComissionamentoTurmaFaixaValorVO extends SuperVO implements Serializable {
    private Integer codigo;
    private ComissionamentoTurmaVO comissionamentoTurmaVO;
    private Integer qtdeInicialAluno;
    private Integer qtdeFinalAluno;
    private Double percComissao;
    private Double valor;

    public Integer getQtdeInicialAluno() {
        if (qtdeInicialAluno == null) {
            qtdeInicialAluno = 0;
        }
        return qtdeInicialAluno;
    }

    public void setQtdeInicialAluno(Integer qtdeInicialAluno) {
        this.qtdeInicialAluno = qtdeInicialAluno;
    }

    public Integer getQtdeFinalAluno() {
        if (qtdeFinalAluno == null) {
            qtdeFinalAluno = 0;
        }
        return qtdeFinalAluno;
    }

    public void setQtdeFinalAluno(Integer qtdeFinalAluno) {
        this.qtdeFinalAluno = qtdeFinalAluno;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        //valor = ((this.getComissionamentoTurmaVO().getTicketMedio().doubleValue() * this.getComissionamentoTurmaVO().getTotalAlunosPagantes()) * this.getPercComissao().doubleValue());
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public ComissionamentoTurmaVO getComissionamentoTurmaVO() {
        if (comissionamentoTurmaVO == null) {
            comissionamentoTurmaVO = new ComissionamentoTurmaVO();
        }
        return comissionamentoTurmaVO;
    }

    public void setComissionamentoTurmaVO(ComissionamentoTurmaVO comissionamentoTurmaVO) {
        this.comissionamentoTurmaVO = comissionamentoTurmaVO;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

	public Double getPercComissao() {
		if (percComissao == null) {
			percComissao = 0.0;
		}
		return percComissao;
	}

	public void setPercComissao(Double percComissao) {
		this.percComissao = percComissao;
	}

}
