package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.enumerador.TipoListaProcessamentoArquivoEnum;
import negocio.comuns.utilitarias.Uteis;

public class ProcessamentoArquivoRetornoParceiroAlunoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8974034326695674415L;
	private Integer codigo;
	private ProcessamentoArquivoRetornoParceiroVO processamentoArquivoRetornoParceiroVO;
	private String cpf;
	private Double valorRepasse;
	private TipoListaProcessamentoArquivoEnum tipoListaProcessamentoArquivo;
	private List<ProcessamentoArquivoRetornoParceiroExcelVO> listaContasAReceber;
	/*
	 * transient
	 */
	private String matricula;
	private PessoaVO aluno;

	public String getCpf() {
		if (cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public ProcessamentoArquivoRetornoParceiroVO getProcessamentoArquivoRetornoParceiroVO() {
		if (processamentoArquivoRetornoParceiroVO == null) {
			processamentoArquivoRetornoParceiroVO = new ProcessamentoArquivoRetornoParceiroVO();
		}
		return processamentoArquivoRetornoParceiroVO;
	}

	public void setProcessamentoArquivoRetornoParceiroVO(ProcessamentoArquivoRetornoParceiroVO processamentoArquivoRetornoParceiroVO) {
		this.processamentoArquivoRetornoParceiroVO = processamentoArquivoRetornoParceiroVO;
	}

	public PessoaVO getAluno() {
		if (aluno == null) {
			aluno = new PessoaVO();
		}
		return aluno;
	}

	public void setAluno(PessoaVO aluno) {
		this.aluno = aluno;
	}

	public Double getValorRepasse() {
		if (valorRepasse == null) {
			valorRepasse = 0.0;
		}
		return valorRepasse;
	}

	public void setValorRepasse(Double valorRepasse) {
		this.valorRepasse = valorRepasse;
	}

	public TipoListaProcessamentoArquivoEnum getTipoListaProcessamentoArquivo() {
		if (tipoListaProcessamentoArquivo == null) {
			tipoListaProcessamentoArquivo = TipoListaProcessamentoArquivoEnum.NENHUM;
		}
		return tipoListaProcessamentoArquivo;
	}

	public void setTipoListaProcessamentoArquivo(TipoListaProcessamentoArquivoEnum tipoListaProcessamentoArquivo) {
		this.tipoListaProcessamentoArquivo = tipoListaProcessamentoArquivo;
	}

	public List<ProcessamentoArquivoRetornoParceiroExcelVO> getListaContasAReceber() {
		if (listaContasAReceber == null) {
			listaContasAReceber = new ArrayList<ProcessamentoArquivoRetornoParceiroExcelVO>();
		}
		return listaContasAReceber;
	}

	public void setListaContasAReceber(List<ProcessamentoArquivoRetornoParceiroExcelVO> listaContasAReceber) {
		this.listaContasAReceber = listaContasAReceber;
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
	
	
	public Double getValorTotalContaReceber() {
		Double valor = 0.0;
		for (ProcessamentoArquivoRetornoParceiroExcelVO obj : getListaContasAReceber()) {
			valor = valor + obj.getContaReceberVO().getValor();
		}
		return valor;
	}

	public Double getValorTotalParceiro() {
		Double valor = 0.0;
		for (ProcessamentoArquivoRetornoParceiroExcelVO obj : getListaContasAReceber()) {
			valor = valor + obj.getValorConta();
		}
		return valor;
	}

	public boolean isProcessamentoSituacaoRecebida() {
		forRecebida: for (ProcessamentoArquivoRetornoParceiroExcelVO objConta : getListaContasAReceber()) {
			if (Uteis.isAtributoPreenchido(objConta.getContaReceberVO()) && objConta.getContaReceberVO().getSituacaoEQuitada() && (getTipoListaProcessamentoArquivo().isNenhum() || getTipoListaProcessamentoArquivo().isContaRecebidas())) {
				continue forRecebida;
			}
			return false;
		}
		return true;
	}

	public boolean isProcessamentoSituacaoAReceber() {
		forReceber: for (ProcessamentoArquivoRetornoParceiroExcelVO objConta : getListaContasAReceber()) {
			if (Uteis.isAtributoPreenchido(objConta.getContaReceberVO()) && ( objConta.getContaReceberVO().getValor().equals(objConta.getValorConta()) || objConta.isValorDiferenteEntreContasAceita() || getTipoListaProcessamentoArquivo().isContaReceber())) {
				continue forReceber;
			}
			return false;
		}
		return true;
	}

	public boolean isProcessamentoSituacaoDivergenteValoresEntreConta() {
		boolean existeUmaContaPreenchida = false;
		boolean existeUmaContaNaoPreenchida = false;
		for (ProcessamentoArquivoRetornoParceiroExcelVO objConta : getListaContasAReceber()) {
			if(!Uteis.isAtributoPreenchido(objConta.getContaReceberVO())){
				existeUmaContaNaoPreenchida = true;
			}
			if(Uteis.isAtributoPreenchido(objConta.getContaReceberVO())){
				existeUmaContaPreenchida = true;
			}
			if ((existeUmaContaNaoPreenchida && Uteis.isAtributoPreenchido(objConta.getContaReceberVO())) 
					|| (existeUmaContaPreenchida && !Uteis.isAtributoPreenchido(objConta.getContaReceberVO())) 
					|| (Uteis.isAtributoPreenchido(objConta.getContaReceberVO()) && objConta.isValorDiferenteEntreContas())
				    || getTipoListaProcessamentoArquivo().isContaValoresDivergente()) {
				return true;
			}
		}
		return false;
	}

	public boolean isProcessamentoSituacaoContaNaoLocalizada() {
		forNaoLocalizada: for (ProcessamentoArquivoRetornoParceiroExcelVO objConta : getListaContasAReceber()) {
			if (!Uteis.isAtributoPreenchido(objConta.getContaReceberVO()) || getTipoListaProcessamentoArquivo().isNaoLocalizadaSistema()) {
				continue forNaoLocalizada;
			}
			return false;
		}
		return true;
	}

}
