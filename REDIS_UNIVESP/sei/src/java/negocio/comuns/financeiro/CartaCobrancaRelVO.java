package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;

public class CartaCobrancaRelVO extends SuperParametroRelVO {

	private String nomeCidade;
	private String nomeResponsavel;
	private String enderecoResponsavel;
	private String nomeAluno;	
	private String cpfAluno;
	private String numeroMatricula;
	private String nomeCurso;
	private String estadoResponsavel;
	private String cepResponsavel;
	private String turma;
	private String debitosDataInicial;
	private List<ContaReceberVO> debitosLista;
	private String dataPorExtenso;
	private String setor;
	private String nomeUnidadeEnsino;
	private String fixoUnidadeEnsino;
	private String celularUnidadeEnsino;
	private String textoPadraoCartaCobranca;
	private String nomeCidadeUnidadeEnsino;
	private boolean considerarUnidadeEnsinoFinanceira = false;
	private String tipoPessoa;
	private Integer codigoResponsavel;	
	private String cpfResponsavel;

	public String getDebitosDataInicial() {
		if (debitosDataInicial == null) {
			debitosDataInicial = "";
		}
		return debitosDataInicial;
	}

	public void setDebitosDataInicial(String debitosDataInicial) {
		this.debitosDataInicial = debitosDataInicial;
	}

	public List<ContaReceberVO> getDebitosLista() {
		if (debitosLista == null) {
			debitosLista = new ArrayList<ContaReceberVO>();
		}
		return debitosLista;
	}

	public JRDataSource getDebitosListaJrDataSouce() {
		JRDataSource jr = new JRBeanArrayDataSource(getDebitosLista().toArray());
		return jr;
	}

	public void setDebitosLista(List<ContaReceberVO> debitosLista) {
		this.debitosLista = debitosLista;
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

	public String getNomeCidade() {
		if (nomeCidade == null) {
			nomeCidade = "";
		}
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
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

	public String getEnderecoResponsavel() {
		if (enderecoResponsavel == null) {
			enderecoResponsavel = "";
		}
		return enderecoResponsavel;
	}

	public void setEnderecoResponsavel(String enderecoResponsavel) {
		this.enderecoResponsavel = enderecoResponsavel;
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

	public String getCpfAluno() {
		if (cpfAluno == null) {
			cpfAluno = "";
		}
		return cpfAluno;
	}

	public void setCpfAluno(String cpfAluno) {
		this.cpfAluno = cpfAluno;
	}

	public String getNumeroMatricula() {
		if (numeroMatricula == null) {
			numeroMatricula = "";
		}
		return numeroMatricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getEstadoResponsavel() {
		if (estadoResponsavel == null) {
			estadoResponsavel = "";
		}
		return estadoResponsavel;
	}

	public void setEstadoResponsavel(String estadoResponsavel) {
		this.estadoResponsavel = estadoResponsavel;
	}

	public String getCepResponsavel() {
		if (cepResponsavel == null) {
			cepResponsavel = "";
		}
		return cepResponsavel;
	}

	public void setCepResponsavel(String cepResponsavel) {
		this.cepResponsavel = cepResponsavel;
	}

	public String getEnderecoGeral() {

		String resp = this.getEnderecoResponsavel();

		if (!this.getSetor().equals("")) {
			resp = resp + " - " + this.getSetor();
		}

		if (!this.getNomeCidade().equals("")) {
			resp = resp + " - " + this.getNomeCidade();
		}

		if (!this.getEstadoResponsavel().equals("")) {
			resp = resp + " - " + this.getEstadoResponsavel();
		}

		if (!this.getCepResponsavel().equals("")) {
			resp = resp + " - CEP: " + this.getCepResponsavel();
		}
		return resp;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getDataPorExtenso() {
		if (dataPorExtenso == null) {
			dataPorExtenso = "";
		}
		return dataPorExtenso;
	}

	public void setDataPorExtenso(String dataPorExtenso) {
		this.dataPorExtenso = dataPorExtenso;
	}

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public String getFixoUnidadeEnsino() {
		if (fixoUnidadeEnsino == null) {
			fixoUnidadeEnsino = "";
		}
		return fixoUnidadeEnsino;
	}

	public void setFixoUnidadeEnsino(String fixoUnidadeEnsino) {
		this.fixoUnidadeEnsino = fixoUnidadeEnsino;
	}

	public String getCelularUnidadeEnsino() {
		if (celularUnidadeEnsino == null) {
			celularUnidadeEnsino = "";
		}
		return celularUnidadeEnsino;
	}

	public void setCelularUnidadeEnsino(String celularUnidadeEnsino) {
		this.celularUnidadeEnsino = celularUnidadeEnsino;
	}

	public String getTextoPadraoCartaCobranca() {
		if (textoPadraoCartaCobranca == null) {
			textoPadraoCartaCobranca = "";
		}
		return textoPadraoCartaCobranca;
	}

	public void setTextoPadraoCartaCobranca(String textoPadraoCartaCobranca) {
		this.textoPadraoCartaCobranca = textoPadraoCartaCobranca;
	}
	
	public String getNomeCidadeUnidadeEnsino() {
		if (nomeCidadeUnidadeEnsino == null) {
			nomeCidadeUnidadeEnsino = "";
		}
		return nomeCidadeUnidadeEnsino;
	}

	public void setNomeCidadeUnidadeEnsino(String nomeCidadeUnidadeEnsino) {
		this.nomeCidadeUnidadeEnsino = nomeCidadeUnidadeEnsino;
	}

	public boolean isConsiderarUnidadeEnsinoFinanceira() {
		return considerarUnidadeEnsinoFinanceira;
	}

	public void setConsiderarUnidadeEnsinoFinanceira(boolean considerarUnidadeEnsinoFinanceira) {
		this.considerarUnidadeEnsinoFinanceira = considerarUnidadeEnsinoFinanceira;
	}
	
	
	
	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Integer getCodigoResponsavel() {
		if (codigoResponsavel == null) {
			codigoResponsavel = 0;
		}
		return codigoResponsavel;
	}

	public void setCodigoResponsavel(Integer codigoResponsavel) {
		this.codigoResponsavel = codigoResponsavel;
	}

	public String getCpfResponsavel() {
		if (cpfResponsavel == null) {
			cpfResponsavel = "";
		}
		return cpfResponsavel;
	}

	public void setCpfResponsavel(String cpfResponsavel) {
		this.cpfResponsavel = cpfResponsavel;
	}
	
	

	
}
