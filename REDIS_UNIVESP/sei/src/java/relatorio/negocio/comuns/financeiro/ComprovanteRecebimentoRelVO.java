package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ComprovanteRecebimentoRelVO {

    private String nomeResponsavel;
    private String nomeAluno;
    private String matricula;
    private String turma;
    private Integer codigoNegociacaoRecebimento;
    private List<ContaReceberNegociacaoRecebimentoVO> listaContaReceberNegociacaoRecebimentoVO;
   
    private List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoNegociacaoRecebimentoVO;
    
    private List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoCartao;
    
    
    private String parcelasPagasNrDocumentoDataVencimento;
    private String valorTotalRecebimentoPorExtenso;
    private String cidadeDataRecebimentoPorExtenso;
    private Double valorTotalRecebimento;
    private Double valorCalculadoDescontoLancadoRecebimento;

    public ComprovanteRecebimentoRelVO() {
    }

    public Integer getCodigoNegociacaoRecebimento() {
        if (codigoNegociacaoRecebimento == null) {
            codigoNegociacaoRecebimento = 0;
        }
        return codigoNegociacaoRecebimento;
    }

    public void setCodigoNegociacaoRecebimento(Integer codigoNegociacaoRecebimento) {
        this.codigoNegociacaoRecebimento = codigoNegociacaoRecebimento;
    }

    public JRDataSource getListaContaReceberNegociacaoRecebimentoDataSource() {
        return new JRBeanArrayDataSource(getListaContaReceberNegociacaoRecebimentoVO().toArray());
    }

    public JRDataSource getListaContaReceberNegociacaoRecebimentoDataSource1() {
        return new JRBeanArrayDataSource(getListaContaReceberNegociacaoRecebimentoVO().toArray());
    }

    public JRDataSource getListaFormaPagamentoNegociacaoRecebimentoDataSource() {
        return new JRBeanArrayDataSource(getListaFormaPagamentoNegociacaoRecebimentoVO().toArray());
    }
    public JRDataSource getListaFormaPagamentoNegociacaoRecebimentoCartaoDataSource() {
    	return new JRBeanArrayDataSource(getListaFormaPagamentoNegociacaoRecebimentoVO().toArray());
    }

    public JRDataSource getListaFormaPagamentoNegociacaoRecebimentoDataSource1() {
        return new JRBeanArrayDataSource(getListaFormaPagamentoNegociacaoRecebimentoVO().toArray());
    }

    public List<ContaReceberNegociacaoRecebimentoVO> getListaContaReceberNegociacaoRecebimentoVO() {
        if (listaContaReceberNegociacaoRecebimentoVO == null) {
            listaContaReceberNegociacaoRecebimentoVO = new ArrayList<ContaReceberNegociacaoRecebimentoVO>(0);
        }
        return listaContaReceberNegociacaoRecebimentoVO;
    }

    public void setListaContaReceberNegociacaoRecebimentoVO(List<ContaReceberNegociacaoRecebimentoVO> listaContaReceberNegociacaoRecebimentoVO) {
        this.listaContaReceberNegociacaoRecebimentoVO = listaContaReceberNegociacaoRecebimentoVO;
    }

    public String getMatricula() {
        if (matricula == null) {
            matricula = "";
        }
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNomeAluno() {
        if (nomeAluno == null) {
            nomeAluno = "";
        }
        return nomeAluno;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public String getNomeResponsavel() {
        if (nomeResponsavel == null) {
            nomeResponsavel = "";
        }
        return nomeResponsavel;
    }

    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getCidadeDataRecebimentoPorExtenso() {
        if (cidadeDataRecebimentoPorExtenso == null) {
            cidadeDataRecebimentoPorExtenso = "";
        }
        return cidadeDataRecebimentoPorExtenso;
    }

    public void setCidadeDataRecebimentoPorExtenso(String cidadeDataRecebimentoPorExtenso) {
        this.cidadeDataRecebimentoPorExtenso = cidadeDataRecebimentoPorExtenso;
    }

    public String getParcelasPagasNrDocumentoDataVencimento() {
        if (parcelasPagasNrDocumentoDataVencimento == null) {
            parcelasPagasNrDocumentoDataVencimento = "";
        }
        return parcelasPagasNrDocumentoDataVencimento;
    }

    public void setParcelasPagasNrDocumentoDataVencimento(String parcelasPagasNrDocumentoDataVencimento) {
        this.parcelasPagasNrDocumentoDataVencimento = parcelasPagasNrDocumentoDataVencimento;
    }

    public String getValorTotalRecebimentoPorExtenso() {
        if (valorTotalRecebimentoPorExtenso == null) {
            valorTotalRecebimentoPorExtenso = "";
        }
        return valorTotalRecebimentoPorExtenso;
    }

    public void setValorTotalRecebimentoPorExtenso(String valorTotalRecebimentoPorExtenso) {
        this.valorTotalRecebimentoPorExtenso = valorTotalRecebimentoPorExtenso;
    }

    public Double getValorTotalRecebimento() {
        if (valorTotalRecebimento == null) {
            valorTotalRecebimento = 0.0;
        }
        return valorTotalRecebimento;
    }

    public void setValorTotalRecebimento(Double valorTotalRecebimento) {
        this.valorTotalRecebimento = valorTotalRecebimento;
    }

    public List<FormaPagamentoNegociacaoRecebimentoVO> getListaFormaPagamentoNegociacaoRecebimentoVO() {
        if (listaFormaPagamentoNegociacaoRecebimentoVO == null) {
            listaFormaPagamentoNegociacaoRecebimentoVO = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
        }
        return listaFormaPagamentoNegociacaoRecebimentoVO;
    }

    public void setListaFormaPagamentoNegociacaoRecebimentoVO(List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoNegociacaoRecebimentoVO) {
        this.listaFormaPagamentoNegociacaoRecebimentoVO = listaFormaPagamentoNegociacaoRecebimentoVO;
    }

	public List<FormaPagamentoNegociacaoRecebimentoVO> getListaFormaPagamentoCartao() {
		if(listaFormaPagamentoCartao == null){
			listaFormaPagamentoCartao = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
		}
		return listaFormaPagamentoCartao;
	}

	public void setListaFormaPagamentoCartao(
			List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoCartao) {
		this.listaFormaPagamentoCartao = listaFormaPagamentoCartao;
	}

	public Double getValorCalculadoDescontoLancadoRecebimento() {
		if (valorCalculadoDescontoLancadoRecebimento == null) {
			valorCalculadoDescontoLancadoRecebimento = 0.0;
		}
		return valorCalculadoDescontoLancadoRecebimento;
	}

	public void setValorCalculadoDescontoLancadoRecebimento(Double valorCalculadoDescontoLancadoRecebimento) {
		this.valorCalculadoDescontoLancadoRecebimento = valorCalculadoDescontoLancadoRecebimento;
	}
    
    
    
}
