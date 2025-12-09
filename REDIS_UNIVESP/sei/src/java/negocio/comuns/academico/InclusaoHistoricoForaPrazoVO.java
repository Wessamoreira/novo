package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.PlanoFinanceiroReposicaoVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade MatriculaPeriodoTurmaDisciplina.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class InclusaoHistoricoForaPrazoVO extends SuperVO {

    private Integer codigo;
    private MatriculaPeriodoVO matriculaPeriodoVO;
    private Boolean reposicao;
    private TextoPadraoVO textoPadraoContrato;
    private String justificativa;
    private String observacao;
    private PlanoFinanceiroReposicaoVO planoFinanceiroReposicaoVO;
    private Integer nrParcelas;
    private Double valorTotalParcela;
    private Double desconto;
    private Date dataVencimento;
    private UsuarioVO responsavel;
    private Date dataInclusao;
    private List<InclusaoDisciplinasHistoricoForaPrazoVO> listaInclusaoDisciplinasHistoricoForaPrazoVO;
    private RequerimentoVO requerimentoVO;
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

    public Date getDataInclusao() {
        if (dataInclusao == null) {
            dataInclusao = new Date();
        }
        return dataInclusao;
    }

    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public String getJustificativa() {
        if (justificativa == null) {
            justificativa = "";
        }
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public List<InclusaoDisciplinasHistoricoForaPrazoVO> getListaInclusaoDisciplinasHistoricoForaPrazoVO() {
        if (listaInclusaoDisciplinasHistoricoForaPrazoVO == null) {
            listaInclusaoDisciplinasHistoricoForaPrazoVO = new ArrayList<InclusaoDisciplinasHistoricoForaPrazoVO>(0);
        }
        return listaInclusaoDisciplinasHistoricoForaPrazoVO;
    }

    public void setListaInclusaoDisciplinasHistoricoForaPrazoVO(List<InclusaoDisciplinasHistoricoForaPrazoVO> listaInclusaoDisciplinasHistoricoForaPrazoVO) {
        this.listaInclusaoDisciplinasHistoricoForaPrazoVO = listaInclusaoDisciplinasHistoricoForaPrazoVO;
    }

    public MatriculaPeriodoVO getMatriculaPeriodoVO() {
        if (matriculaPeriodoVO == null) {
            matriculaPeriodoVO = new MatriculaPeriodoVO();
        }
        return matriculaPeriodoVO;
    }

    public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
        this.matriculaPeriodoVO = matriculaPeriodoVO;
    }

    public String getObservacao() {
        if (observacao == null) {
            observacao = "";
        }
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public PlanoFinanceiroReposicaoVO getPlanoFinanceiroReposicaoVO() {
        if (planoFinanceiroReposicaoVO == null) {
            planoFinanceiroReposicaoVO = new PlanoFinanceiroReposicaoVO();
        }
        return planoFinanceiroReposicaoVO;
    }

    public void setPlanoFinanceiroReposicaoVO(PlanoFinanceiroReposicaoVO planoFinanceiroReposicaoVO) {
        this.planoFinanceiroReposicaoVO = planoFinanceiroReposicaoVO;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public TextoPadraoVO getTextoPadraoContrato() {
        if (textoPadraoContrato == null) {
            textoPadraoContrato = new TextoPadraoVO();
        }
        return textoPadraoContrato;
    }

    public void setTextoPadraoContrato(TextoPadraoVO textoPadraoContrato) {
        this.textoPadraoContrato = textoPadraoContrato;
    }

    public Boolean getReposicao() {
        if (reposicao == null) {
            reposicao = false;
        }
        return reposicao;
    }

    public void setReposicao(Boolean reposicao) {
        this.reposicao = reposicao;
    }

    public String getTipo_Apresentar() {
        if (getReposicao()) {
            return "Reposição";
        }
        return "Inclusão";
    }

    public Integer getNrParcelas() {
        if (nrParcelas == null) {
            nrParcelas = 0;
        }
        return nrParcelas;
    }

    public void setNrParcelas(Integer nrParcelas) {
        this.nrParcelas = nrParcelas;
    }

    public Double getValorTotalParcela() {
        if (valorTotalParcela == null) {
            valorTotalParcela = 0.0;
        }
        return valorTotalParcela;
    }

    public void setValorTotalParcela(Double valorTotalParcela) {
        this.valorTotalParcela = valorTotalParcela;
    }

    public Double getDesconto() {
        if (desconto == null) {
            desconto = 0.0;
        }
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Date getDataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = new Date();
        }
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDataInclusao_Apresentar() {
        return (Uteis.getData(getDataInclusao()));
    }
    
    public RequerimentoVO getRequerimentoVO() {
		if (requerimentoVO == null) {
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}

	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}
}
