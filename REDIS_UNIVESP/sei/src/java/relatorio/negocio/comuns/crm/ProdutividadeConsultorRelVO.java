package relatorio.negocio.comuns.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

public class ProdutividadeConsultorRelVO {

	private CampanhaVO campanhaVO;
	private PessoaVO consultor;
	private String prospect;
	private Date dataCompromisso;
	private Date dataInicialcompromisso;
	private String tipoSituacaoCompromisso;

	private Date dataInicio;
	private Date dataFim;

	private List<UnidadeEnsinoVO> listaUnidadeEnsinoVO;
	private String unidadeEnsinoApresentar;
	private List<CursoVO> listaCursoVO;
	private String cursoApresentar;
	private String situacao;
	private String tipoRelatorio;
	private Integer contAguardando;
	private Integer contParalizado;
	private Integer contRealizado;
	private Integer contInsucesso;
	private Integer contRemarcacao;
	private Integer contNaoPossuiAgenda;
	private Integer contCompromisso;

	public String getProspect() {
		if (prospect == null) {
			prospect = "";
		}
		return prospect;
	}

	public void setProspect(String prospect) {
		this.prospect = prospect;
	}

	public Date getDataCompromisso() {
		return dataCompromisso;
	}

	public void setDataCompromisso(Date dataCompromisso) {
		this.dataCompromisso = dataCompromisso;
	}

	public Date getDataInicialcompromisso() {
		return dataInicialcompromisso;
	}

	public void setDataInicialcompromisso(Date dataInicialcompromisso) {
		this.dataInicialcompromisso = dataInicialcompromisso;
	}

	public String getTipoSituacaoCompromisso() {
		if (tipoSituacaoCompromisso == null) {
			tipoSituacaoCompromisso = "";
		}
		return tipoSituacaoCompromisso;
	}

	public void setTipoSituacaoCompromisso(String tipoSituacaoCompromisso) {
		this.tipoSituacaoCompromisso = tipoSituacaoCompromisso;
	}

	public CampanhaVO getCampanhaVO() {
		if (campanhaVO == null) {
			campanhaVO = new CampanhaVO();
		}
		return campanhaVO;
	}

	public void setCampanhaVO(CampanhaVO campanhaVO) {
		this.campanhaVO = campanhaVO;
	}

	public PessoaVO getConsultor() {
		if (consultor == null) {
			consultor = new PessoaVO();
		}
		return consultor;
	}

	public void setConsultor(PessoaVO consultor) {
		this.consultor = consultor;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = UteisData.getPrimeiroDataMes(new Date());
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = UteisData.getUltimaDataMes(new Date());
		}
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public List<UnidadeEnsinoVO> getListaUnidadeEnsinoVO() {
		if (listaUnidadeEnsinoVO == null) {
			listaUnidadeEnsinoVO = new ArrayList<UnidadeEnsinoVO>();
		}
		return listaUnidadeEnsinoVO;
	}

	public void setListaUnidadeEnsinoVO(List<UnidadeEnsinoVO> listaUnidadeEnsinoVO) {
		this.listaUnidadeEnsinoVO = listaUnidadeEnsinoVO;
	}

	public List<CursoVO> getListaCursoVO() {
		if (listaCursoVO == null) {
			listaCursoVO = new ArrayList<CursoVO>();
		}
		return listaCursoVO;
	}

	public void setListaCursoVO(List<CursoVO> listaCursoVO) {
		this.listaCursoVO = listaCursoVO;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getTipoRelatorio() {
		if (tipoRelatorio == null) {
			tipoRelatorio = "";
		}
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}
	
	public boolean isTipoRelatorioSintetico() {
		return getTipoRelatorio().equals("sintetico");
	}

	public Integer getContAguardando() {
		if(contAguardando == null){
			contAguardando = 0;
		}
		return contAguardando;
	}

	public void setContAguardando(Integer contAguardando) {
		this.contAguardando = contAguardando;
	}

	public Integer getContParalizado() {
		if(contParalizado == null){
			contParalizado = 0;
		}
		return contParalizado;
	}

	public void setContParalizado(Integer contParalizado) {
		this.contParalizado = contParalizado;
	}

	public Integer getContRealizado() {
		if(contRealizado == null){
			contRealizado = 0;
		}
		return contRealizado;
	}

	public void setContRealizado(Integer contRealizado) {
		this.contRealizado = contRealizado;
	}

	public Integer getContInsucesso() {
		if(contInsucesso == null){
			contInsucesso = 0;
		}
		return contInsucesso;
	}

	public void setContInsucesso(Integer contInsucesso) {
		this.contInsucesso = contInsucesso;
	}

	public Integer getContRemarcacao() {
		if(contRemarcacao == null){
			contRemarcacao = 0;
		}
		return contRemarcacao;
	}

	public void setContRemarcacao(Integer contRemarcacao) {
		this.contRemarcacao = contRemarcacao;
	}

	public Integer getContNaoPossuiAgenda() {
		if(contNaoPossuiAgenda == null){
			contNaoPossuiAgenda = 0;
		}
		return contNaoPossuiAgenda;
	}

	public void setContNaoPossuiAgenda(Integer contNaoPossuiAgenda) {
		this.contNaoPossuiAgenda = contNaoPossuiAgenda;
	}

	public Integer getContCompromisso() {
		if(contCompromisso == null){
			contCompromisso = 0;
		}
		return contCompromisso;
	}

	public void setContCompromisso(Integer contCompromisso) {
		this.contCompromisso = contCompromisso;
	}
	
	
	
	

}
