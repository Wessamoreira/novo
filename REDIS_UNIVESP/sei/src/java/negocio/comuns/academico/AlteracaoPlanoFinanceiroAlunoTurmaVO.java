package negocio.comuns.academico;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class AlteracaoPlanoFinanceiroAlunoTurmaVO extends SuperVO implements Cloneable {

	private static final long serialVersionUID = 1L;

	private TurmaVO turmaVO;
	private MatriculaVO matriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVONovo;
	private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVOAntigo;
	private String situacaoAlteracao;
	private String situacaoProcessamento;
	private UsuarioVO usuarioVO;
	private Timestamp dataAlteracao;
	private String logErro;
	
	//Nao sera salva no banco
	private String labelCondicaoPagamentoPlanoFinanceiroCursoVONovo;
	//Nao sera salva no banco
	private String labelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo;
	//Nao sera salva no banco
	private String opcoes;
	//Nao sera salva no banco
	private List<SelectItem> listaNovaCondicaoPgto;
	
	public TurmaVO getTurmaVO() {
		if (turmaVO == null)
			turmaVO = new TurmaVO();
		
		return turmaVO;
	}
	
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null)
			matriculaVO = new MatriculaVO();
		return matriculaVO;
	}
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	
	public String getSituacaoProcessamento() {
		if (situacaoProcessamento == null)
			situacaoProcessamento = "";
		return situacaoProcessamento;
	}
	public void setSituacaoProcessamento(String situacaoProcessamento) {
		this.situacaoProcessamento = situacaoProcessamento;
	}
	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null)
			usuarioVO = new UsuarioVO();
		return usuarioVO;
	}
	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}
	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVONovo() {
		if (condicaoPagamentoPlanoFinanceiroCursoVONovo == null)
			condicaoPagamentoPlanoFinanceiroCursoVONovo = new CondicaoPagamentoPlanoFinanceiroCursoVO();
		return condicaoPagamentoPlanoFinanceiroCursoVONovo;
	}
	public void setCondicaoPagamentoPlanoFinanceiroCursoVONovo(
			CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVONovo) {
		this.condicaoPagamentoPlanoFinanceiroCursoVONovo = condicaoPagamentoPlanoFinanceiroCursoVONovo;
	}
	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVOAntigo() {
		if (condicaoPagamentoPlanoFinanceiroCursoVOAntigo == null)
			condicaoPagamentoPlanoFinanceiroCursoVOAntigo = new CondicaoPagamentoPlanoFinanceiroCursoVO();
		return condicaoPagamentoPlanoFinanceiroCursoVOAntigo;
	}
	public void setCondicaoPagamentoPlanoFinanceiroCursoVOAntigo(
			CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVOAntigo) {
		this.condicaoPagamentoPlanoFinanceiroCursoVOAntigo = condicaoPagamentoPlanoFinanceiroCursoVOAntigo;
	}
	public String getLabelCondicaoPagamentoPlanoFinanceiroCursoVONovo() {
		if (labelCondicaoPagamentoPlanoFinanceiroCursoVONovo == null)
			labelCondicaoPagamentoPlanoFinanceiroCursoVONovo = "";

		return labelCondicaoPagamentoPlanoFinanceiroCursoVONovo;
	}
	public void setLabelCondicaoPagamentoPlanoFinanceiroCursoVONovo(
			String labelCondicaoPagamentoPlanoFinanceiroCursoVONovo) {
		this.labelCondicaoPagamentoPlanoFinanceiroCursoVONovo = labelCondicaoPagamentoPlanoFinanceiroCursoVONovo;
	}
	public String getLabelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo() {
		if (labelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo == null)
			labelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo = "";

		return labelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo;
	}
	public void setLabelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo(
			String labelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo) {
		this.labelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo = labelCondicaoPagamentoPlanoFinanceiroCursoVOAntigo;
	}
	
	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null)
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		return matriculaPeriodoVO;
	}
	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public String getOpcoes() {
		if (opcoes == null)
			opcoes = "";
		return opcoes;
	}

	public void setOpcoes(String opcoes) {
		this.opcoes = opcoes;
	}

	public List<SelectItem> getListaNovaCondicaoPgto() {
		if (listaNovaCondicaoPgto == null)
			listaNovaCondicaoPgto = new ArrayList<>();
		return listaNovaCondicaoPgto;
	}

	public void setListaNovaCondicaoPgto(List<SelectItem> listaNovaCondicaoPgto) {
		this.listaNovaCondicaoPgto = listaNovaCondicaoPgto;
	}

	public Timestamp getDataAlteracao() {
		if (dataAlteracao == null)
			dataAlteracao = Uteis.getDataJDBCTimestamp(new Date());
		return dataAlteracao;
	}
	
	public String getDataAlteracaoApresentar() {
	return (Uteis.getDataComHora(getDataAlteracao()));
	}

	public void setDataAlteracao(Timestamp dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getSituacaoAlteracao() {
		if (situacaoAlteracao == null)
			situacaoAlteracao = "";
		return situacaoAlteracao;
	}

	public void setSituacaoAlteracao(String situacaoAlteracao) {
		this.situacaoAlteracao = situacaoAlteracao;
	}

	public String getLogErro() {
		if (logErro == null)
			logErro = "";
		return logErro;
	}

	public void setLogErro(String logErro) {
		this.logErro = logErro;
	}
}