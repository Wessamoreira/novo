package relatorio.negocio.comuns.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
/**
 * 
 * @author PedroOtimize
 *
 */
public class PosVendaRelVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6490238556111911103L;
	private TurmaVO turmaVO;
	private String matricula;
	private String nomeAluno;
	private String emailAluno;
	private boolean enviarEmail = false;
	private String celularAluno;
	private String telefoneFixoAluno;
	private List<PosVendaPresencaRelVO> listaPosVendaPresencaVOs;
	private List<DocumetacaoMatriculaVO> listaDocumetacaoMatriculaVOs;
	private String portalAlunoReposicao;
	private String portalAlunoDeclaracao;
	private Integer quantidadeParcelas;
	private Double valorPrimeiraMensalidade;
	private Double descontoPrimeiraMensalidade;
	private Double valorFinalPrimeiraMensalidade;
	private FuncionarioVO consultor;
	private String notaConsultor;

	/**
	 * filtro relatorio
	 */
	private Integer quantidadeDisciplinasConcluidas;
	private boolean trazerAlunoTransferencia=false;

	public TurmaVO getTurmaVO() {
		turmaVO = Optional.ofNullable(turmaVO).orElse(new TurmaVO());
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public String getMatricula() {
		matricula = Optional.ofNullable(matricula).orElse("");
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNomeAluno() {
		nomeAluno = Optional.ofNullable(nomeAluno).orElse("");
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public String getEmailAluno() {
		emailAluno = Optional.ofNullable(emailAluno).orElse("");
		return emailAluno;
	}

	public void setEmailAluno(String emailAluno) {
		this.emailAluno = emailAluno;
	}

	public boolean isEnviarEmail() {
		return enviarEmail;
	}

	public void setEnviarEmail(boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	public String getCelularAluno() {
		celularAluno = Optional.ofNullable(celularAluno).orElse("");
		return celularAluno;
	}

	public void setCelularAluno(String celularAluno) {
		this.celularAluno = celularAluno;
	}

	public String getTelefoneFixoAluno() {
		telefoneFixoAluno = Optional.ofNullable(telefoneFixoAluno).orElse("");
		return telefoneFixoAluno;
	}

	public void setTelefoneFixoAluno(String fixoAluno) {
		this.telefoneFixoAluno = fixoAluno;
	}

	public List<PosVendaPresencaRelVO> getListaPosVendaPresencaVOs() {
		listaPosVendaPresencaVOs = Optional.ofNullable(listaPosVendaPresencaVOs).orElse(new ArrayList<>());
		return listaPosVendaPresencaVOs;
	}

	public void setListaPosVendaPresencaVOs(List<PosVendaPresencaRelVO> listaPosVendaPresencaVOs) {
		this.listaPosVendaPresencaVOs = listaPosVendaPresencaVOs;
	}

	public List<DocumetacaoMatriculaVO> getListaDocumetacaoMatriculaVOs() {
		listaDocumetacaoMatriculaVOs = Optional.ofNullable(listaDocumetacaoMatriculaVOs).orElse(new ArrayList<>());
		return listaDocumetacaoMatriculaVOs;
	}

	public void setListaDocumetacaoMatriculaVOs(List<DocumetacaoMatriculaVO> listaDocumetacaoMatriculaVOs) {
		this.listaDocumetacaoMatriculaVOs = listaDocumetacaoMatriculaVOs;
	}

	public String getPortalAlunoReposicao() {
		portalAlunoReposicao = Optional.ofNullable(portalAlunoReposicao).orElse("");
		return portalAlunoReposicao;
	}

	public void setPortalAlunoReposicao(String portalAlunoReposicao) {
		this.portalAlunoReposicao = portalAlunoReposicao;
	}

	public String getPortalAlunoDeclaracao() {
		portalAlunoDeclaracao = Optional.ofNullable(portalAlunoDeclaracao).orElse("");
		return portalAlunoDeclaracao;
	}

	public void setPortalAlunoDeclaracao(String portalAlunoDeclaracao) {
		this.portalAlunoDeclaracao = portalAlunoDeclaracao;
	}

	public Integer getQuantidadeParcelas() {
		quantidadeParcelas = Optional.ofNullable(quantidadeParcelas).orElse(0);
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(Integer quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public Double getValorPrimeiraMensalidade() {
		valorPrimeiraMensalidade = Optional.ofNullable(valorPrimeiraMensalidade).orElse(0.0);
		return valorPrimeiraMensalidade;
	}

	public void setValorPrimeiraMensalidade(Double valorPrimeiraMensalidade) {
		this.valorPrimeiraMensalidade = valorPrimeiraMensalidade;
	}

	public Double getDescontoPrimeiraMensalidade() {
		descontoPrimeiraMensalidade = Optional.ofNullable(descontoPrimeiraMensalidade).orElse(0.0);
		return descontoPrimeiraMensalidade;
	}

	public void setDescontoPrimeiraMensalidade(Double descontoPrimeiraMensalidade) {
		this.descontoPrimeiraMensalidade = descontoPrimeiraMensalidade;
	}

	public Double getValorFinalPrimeiraMensalidade() {
		valorFinalPrimeiraMensalidade = Optional.ofNullable(valorFinalPrimeiraMensalidade).orElse(0.0);
		return valorFinalPrimeiraMensalidade;
	}

	public void setValorFinalPrimeiraMensalidade(Double valorFinalPrimeiraMensalidade) {
		this.valorFinalPrimeiraMensalidade = valorFinalPrimeiraMensalidade;
	}

	public FuncionarioVO getConsultor() {
		consultor = Optional.ofNullable(consultor).orElse(new FuncionarioVO());
		return consultor;
	}

	public void setConsultor(FuncionarioVO consultor) {
		this.consultor = consultor;
	}

	public String getNotaConsultor() {
		notaConsultor = Optional.ofNullable(notaConsultor).orElse("");
		return notaConsultor;
	}

	public void setNotaConsultor(String notaConsultor) {
		this.notaConsultor = notaConsultor;
	}

	public Integer getQuantidadeDisciplinasConcluidas() {
		quantidadeDisciplinasConcluidas = Optional.ofNullable(quantidadeDisciplinasConcluidas).orElse(0);
		return quantidadeDisciplinasConcluidas;
	}

	public void setQuantidadeDisciplinasConcluidas(Integer quantidadeDisciplinasConcluidas) {
		this.quantidadeDisciplinasConcluidas = quantidadeDisciplinasConcluidas;
	}

	public boolean isTrazerAlunoTransferencia() {
		return trazerAlunoTransferencia;
	}

	public void setTrazerAlunoTransferencia(boolean trazerAlunoTransferencia) {
		this.trazerAlunoTransferencia = trazerAlunoTransferencia;
	}
	
	

}
