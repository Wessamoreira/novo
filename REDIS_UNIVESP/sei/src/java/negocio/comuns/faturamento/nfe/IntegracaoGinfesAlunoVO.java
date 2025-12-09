package negocio.comuns.faturamento.nfe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class IntegracaoGinfesAlunoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private Date dataImportacao;
	private String anoReferencia;
	private String mesReferencia;
	private Boolean importado;
	private UnidadeEnsinoVO unidadeEnsino;
	private List<IntegracaoGinfesAlunoItemVO> alunos;
	private List<IntegracaoGinfesAlunoItemVO> alunosErro;
	private Boolean descontoIncondicional;
	private Boolean descontoCondicional;	
	private Boolean trazerValorServicoContaReceber;	
	private String  situacaoContaReceber;
	private Boolean trazerContasAlunoDescontoTotal;
	


	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Date getDataImportacao() {
		return dataImportacao;
	}

	public void setDataImportacao(Date dataImportacao) {
		this.dataImportacao = dataImportacao;
	}
	
	public String getDataApresentar() {
		if (dataImportacao == null) {
			return "";
		}
		return (Uteis.getData(dataImportacao));
    }

	public Boolean getImportado() {
		if (importado == null) {
			importado = false;
		}
		return importado;
	}

	public void setImportado(Boolean importado) {
		this.importado = importado;
	}

	public List<IntegracaoGinfesAlunoItemVO> getAlunos() {
		if (alunos == null) {
			alunos = new ArrayList<IntegracaoGinfesAlunoItemVO>();
		}
		return alunos;
	}

	public void setAlunos(List<IntegracaoGinfesAlunoItemVO> alunos) {
		this.alunos = alunos;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getAnoReferencia() {
		if (anoReferencia == null) {
			anoReferencia = "";
		}
		return anoReferencia;
	}

	public void setAnoReferencia(String anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public String getMesReferencia() {
		if (mesReferencia == null) {
			mesReferencia = "";
		}
		return mesReferencia;
	}

	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}
	
	public Boolean getDescontoIncondicional() {
		if(descontoIncondicional == null) {
			descontoIncondicional = false;
		}
		return descontoIncondicional;
	}

	public String getSituacaoContaReceber() {
		if(situacaoContaReceber == null) {
			situacaoContaReceber ="";
		}
		return situacaoContaReceber ;
	}

	public void setSituacaoContaReceber(String situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
	}

	public void setDescontoIncondicional(Boolean descontoIncondicional) {
		this.descontoIncondicional = descontoIncondicional;
	}

	public Boolean getDescontoCondicional() {
		if (descontoCondicional == null) {
			descontoCondicional = false;
		}
		return descontoCondicional;
	}

	public void setDescontoCondicional(Boolean descontoCondicional) {
		this.descontoCondicional = descontoCondicional;
	}

	public List<IntegracaoGinfesAlunoItemVO> getAlunosErro() {
		if(alunosErro ==null) {
			alunosErro =  new ArrayList<IntegracaoGinfesAlunoItemVO>(0);
		}
		return alunosErro;
	}

	public void setAlunosErro(List<IntegracaoGinfesAlunoItemVO> alunosErro) {
		this.alunosErro = alunosErro;
	}

	public Boolean getTrazerValorServicoContaReceber() {
		if (trazerValorServicoContaReceber == null) {
			trazerValorServicoContaReceber = false;
		}
		return trazerValorServicoContaReceber;
	}

	public void setTrazerValorServicoContaReceber(Boolean trazerValorServicoContaReceber) {
		this.trazerValorServicoContaReceber = trazerValorServicoContaReceber;
	}

	public Boolean getTrazerContasAlunoDescontoTotal() {
		if(trazerContasAlunoDescontoTotal == null ) {
			trazerContasAlunoDescontoTotal = false ;
		}
		return trazerContasAlunoDescontoTotal;
	}

	public void setTrazerContasAlunoDescontoTotal(Boolean trazerContasAlunoDescontoTotal) {
		this.trazerContasAlunoDescontoTotal = trazerContasAlunoDescontoTotal;
	}
	
	
	

	
}
