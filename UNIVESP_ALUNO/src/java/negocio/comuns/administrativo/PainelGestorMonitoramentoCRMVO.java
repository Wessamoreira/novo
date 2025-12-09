package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.enumeradores.PainelGestorTipoMonitoramentoCRMEnum;


public class PainelGestorMonitoramentoCRMVO {

	private String dadosGraficoPreInscricao;
	private String dadosGraficoMonitoramentoProspect;
	private String dadosGraficoLigacaoReceptiva;
	private String dadosGraficoTaxaConversao;
	private String dadosGraficoComoFicouSabendoInstituicao;
	private List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalhePreInscricaoCRMVOs;
	private List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs;
	private List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalheTaxaConversaoCRMVOs;
	private List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalheCRMVOs;
	private Integer quantidadeTotalPreInscicao;
	private Integer quantidadeTotalLigacaoReceptiva;
	private Integer quantidadeTotalTaxaConversao;
	private PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO;
	
	private Integer quantidadeTotalProspectsCadastradosMatriculado;
	private Integer quantidadeTotalProspectsCadastradosNaoMatriculado;
	
	private Integer quantidadeTotalProspectsComAgendaMatriculado;
	private Integer quantidadeTotalProspectsComAgendaNaoMatriculado;
	
	private Integer quantidadeTotalProspectsSemAgendaMatriculado;
	private Integer quantidadeTotalProspectsSemAgendaNaoMatriculado;
	
	private Integer quantidadeTotalProspectsSemConsultorResponsavelMatriculado;
	private Integer quantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado;
	
	private Integer quantidadeTotalProspectsComConsultorResponsavelMatriculado;
	private Integer quantidadeTotalProspectsComConsultorResponsavelNaoMatriculado;
	
	private Integer quantidadeTotalProspectsNaoContactadoPeriodo1Matriculado;
	private Integer quantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado;
	
	private Integer quantidadeTotalProspectsNaoContactadoPeriodo2Matriculado;
	private Integer quantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado;
	
	private Integer quantidadeTotalProspectsNaoContactadoPeriodo3Matriculado;
	private Integer quantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado;
	private UnidadeEnsinoVO unidadeEnsino;
	private FuncionarioVO consultor;
	private CursoVO curso;
	private TipoMidiaCaptacaoVO tipoMidiaCaptacao;
	private PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum;
	private Integer periodoUltimoContato1;
	private Integer periodoUltimoContato2;
	private Integer periodoUltimoContato3;
	private String graficoComoFicouSabendoInstituicao;
	private Boolean filtrarMatriculado;

	public Integer getQuantidadeTotalProspectsCadastrados() {
		
		return getQuantidadeTotalProspectsCadastradosMatriculado()+getQuantidadeTotalProspectsCadastradosNaoMatriculado();
	}

	
	public Integer getQuantidadeTotalProspectsComAgenda() {
		return getQuantidadeTotalProspectsComAgendaMatriculado()+getQuantidadeTotalProspectsComAgendaNaoMatriculado();
	}

	
	public Integer getQuantidadeTotalProspectsSemAgenda() {
		
		return getQuantidadeTotalProspectsSemAgendaMatriculado()+getQuantidadeTotalProspectsSemAgendaNaoMatriculado();
	}

	public Integer getQuantidadeTotalProspectsSemConsultorResponsavel() {
		return getQuantidadeTotalProspectsSemConsultorResponsavelMatriculado()+getQuantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado();
	}

	public Integer getQuantidadeTotalProspectsComConsultorResponsavel() {
		return getQuantidadeTotalProspectsComConsultorResponsavelMatriculado()+getQuantidadeTotalProspectsComConsultorResponsavelNaoMatriculado();
	}

	
	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo1() {
		return getQuantidadeTotalProspectsNaoContactadoPeriodo1Matriculado()+getQuantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado();
	}

	
	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo2() {
		return getQuantidadeTotalProspectsNaoContactadoPeriodo2Matriculado()+getQuantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado();
	}

	
	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo3() {
		return getQuantidadeTotalProspectsNaoContactadoPeriodo3Matriculado()+getQuantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado();
	}
	
	public Boolean getApresentarDadosCurso() {
		return !getPainelGestorMonitoramentoDetalheCRMVOs().isEmpty() && getPainelGestorMonitoramentoDetalheCRMVOs().get(0).getCurso().getCodigo() > 0;
	}

	public Boolean getApresentarDadosConsultor() {
		return !getPainelGestorMonitoramentoDetalheCRMVOs().isEmpty() && getPainelGestorMonitoramentoDetalheCRMVOs().get(0).getConsultor().getCodigo() > 0;
	}

	public String getDadosGraficoPreInscricao() {
		if (dadosGraficoPreInscricao == null) {
			dadosGraficoPreInscricao = "";
		}
		return dadosGraficoPreInscricao;
	}

	public void setDadosGraficoPreInscricao(String dadosGraficoPreInscricao) {
		this.dadosGraficoPreInscricao = dadosGraficoPreInscricao;
	}

	public String getDadosGraficoLigacaoReceptiva() {
		if (dadosGraficoLigacaoReceptiva == null) {
			dadosGraficoLigacaoReceptiva = "";
		}
		return dadosGraficoLigacaoReceptiva;
	}

	public void setDadosGraficoLigacaoReceptiva(String dadosGraficoLigacaoReceptiva) {
		this.dadosGraficoLigacaoReceptiva = dadosGraficoLigacaoReceptiva;
	}

	public String getDadosGraficoTaxaConversao() {
		if (dadosGraficoTaxaConversao == null) {
			dadosGraficoTaxaConversao = "";
		}
		return dadosGraficoTaxaConversao;
	}

	public void setDadosGraficoTaxaConversao(String dadosGraficoTaxaConversao) {
		this.dadosGraficoTaxaConversao = dadosGraficoTaxaConversao;
	}

	public String getDadosGraficoComoFicouSabendoInstituicao() {
		if (dadosGraficoComoFicouSabendoInstituicao == null) {
			dadosGraficoComoFicouSabendoInstituicao = "";
		}
		return dadosGraficoComoFicouSabendoInstituicao;
	}

	public void setDadosGraficoComoFicouSabendoInstituicao(String dadosGraficoComoFicouSabendoInstituicao) {
		this.dadosGraficoComoFicouSabendoInstituicao = dadosGraficoComoFicouSabendoInstituicao;
	}

	public List<PainelGestorMonitoramentoDetalheCRMVO> getPainelGestorMonitoramentoDetalhePreInscricaoCRMVOs() {
		if (painelGestorMonitoramentoDetalhePreInscricaoCRMVOs == null) {
			painelGestorMonitoramentoDetalhePreInscricaoCRMVOs = new ArrayList<PainelGestorMonitoramentoDetalheCRMVO>(0);
		}
		return painelGestorMonitoramentoDetalhePreInscricaoCRMVOs;
	}

	public void setPainelGestorMonitoramentoDetalhePreInscricaoCRMVOs(List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalhePreInscricaoCRMVOs) {
		this.painelGestorMonitoramentoDetalhePreInscricaoCRMVOs = painelGestorMonitoramentoDetalhePreInscricaoCRMVOs;
	}

	public List<PainelGestorMonitoramentoDetalheCRMVO> getPainelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs() {
		if (painelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs == null) {
			painelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs = new ArrayList<PainelGestorMonitoramentoDetalheCRMVO>(0);
		}
		return painelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs;
	}

	public void setPainelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs(List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs) {
		this.painelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs = painelGestorMonitoramentoDetalheLigacaoReceptivaCRMVOs;
	}

	public List<PainelGestorMonitoramentoDetalheCRMVO> getPainelGestorMonitoramentoDetalheTaxaConversaoCRMVOs() {
		if (painelGestorMonitoramentoDetalheTaxaConversaoCRMVOs == null) {
			painelGestorMonitoramentoDetalheTaxaConversaoCRMVOs = new ArrayList<PainelGestorMonitoramentoDetalheCRMVO>(0);
		}
		return painelGestorMonitoramentoDetalheTaxaConversaoCRMVOs;
	}

	public void setPainelGestorMonitoramentoDetalheTaxaConversaoCRMVOs(List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalheTaxaConversaoCRMVOs) {
		this.painelGestorMonitoramentoDetalheTaxaConversaoCRMVOs = painelGestorMonitoramentoDetalheTaxaConversaoCRMVOs;
	}

	public Integer getQuantidadeTotalPreInscicao() {
		if (quantidadeTotalPreInscicao == null) {
			quantidadeTotalPreInscicao = 0;
		}
		return quantidadeTotalPreInscicao;
	}

	public void setQuantidadeTotalPreInscicao(Integer quantidadeTotalPreInscicao) {
		this.quantidadeTotalPreInscicao = quantidadeTotalPreInscicao;
	}

	public Integer getQuantidadeTotalLigacaoReceptiva() {
		if (quantidadeTotalLigacaoReceptiva == null) {
			quantidadeTotalLigacaoReceptiva = 0;
		}
		return quantidadeTotalLigacaoReceptiva;
	}

	public void setQuantidadeTotalLigacaoReceptiva(Integer quantidadeTotalLigacaoReceptiva) {
		this.quantidadeTotalLigacaoReceptiva = quantidadeTotalLigacaoReceptiva;
	}

	public Integer getQuantidadeTotalTaxaConversao() {
		if (quantidadeTotalTaxaConversao == null) {
			quantidadeTotalTaxaConversao = 0;
		}
		return quantidadeTotalTaxaConversao;
	}

	public void setQuantidadeTotalTaxaConversao(Integer quantidadeTotalTaxaConversao) {
		this.quantidadeTotalTaxaConversao = quantidadeTotalTaxaConversao;
	}


	public List<PainelGestorMonitoramentoDetalheCRMVO> getPainelGestorMonitoramentoDetalheCRMVOs() {
		if (painelGestorMonitoramentoDetalheCRMVOs == null) {
			painelGestorMonitoramentoDetalheCRMVOs = new ArrayList<PainelGestorMonitoramentoDetalheCRMVO>(0);
		}
		return painelGestorMonitoramentoDetalheCRMVOs;
	}

	public void setPainelGestorMonitoramentoDetalheCRMVOs(List<PainelGestorMonitoramentoDetalheCRMVO> painelGestorMonitoramentoDetalheCRMVOs) {
		this.painelGestorMonitoramentoDetalheCRMVOs = painelGestorMonitoramentoDetalheCRMVOs;
	}

	public PainelGestorMonitoramentoDetalheCRMVO getPainelGestorMonitoramentoDetalheCRMVO() {
		if (painelGestorMonitoramentoDetalheCRMVO == null) {
			painelGestorMonitoramentoDetalheCRMVO = new PainelGestorMonitoramentoDetalheCRMVO();
		}
		return painelGestorMonitoramentoDetalheCRMVO;
	}

	public void setPainelGestorMonitoramentoDetalheCRMVO(PainelGestorMonitoramentoDetalheCRMVO painelGestorMonitoramentoDetalheCRMVO) {
		this.painelGestorMonitoramentoDetalheCRMVO = painelGestorMonitoramentoDetalheCRMVO;
	}

	public String getDadosGraficoMonitoramentoProspect() {
		if (dadosGraficoMonitoramentoProspect == null) {
			dadosGraficoMonitoramentoProspect = "";
		}
		return dadosGraficoMonitoramentoProspect;
	}

	public void setDadosGraficoMonitoramentoProspect(String dadosGraficoMonitoramentoProspect) {
		this.dadosGraficoMonitoramentoProspect = dadosGraficoMonitoramentoProspect;
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

	public FuncionarioVO getConsultor() {
		if (consultor == null) {
			consultor = new FuncionarioVO();
		}
		return consultor;
	}

	public void setConsultor(FuncionarioVO consultor) {
		this.consultor = consultor;
	}

	public PainelGestorTipoMonitoramentoCRMEnum getPainelGestorTipoMonitoramentoCRMEnum() {
		return painelGestorTipoMonitoramentoCRMEnum;
	}

	public void setPainelGestorTipoMonitoramentoCRMEnum(PainelGestorTipoMonitoramentoCRMEnum painelGestorTipoMonitoramentoCRMEnum) {
		this.painelGestorTipoMonitoramentoCRMEnum = painelGestorTipoMonitoramentoCRMEnum;
	}

	
	public CursoVO getCurso() {
		if (curso == null) {
			curso = new CursoVO();
		}
		return curso;
	}

	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}

	public String getTituloVisualizacaoMonitoramentoProspectPreInscricao() {
		if(getApresentarDadosConsultor() && getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty() && getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Consultor";
		}
		if(getApresentarDadosConsultor() && getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty() && !getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Consultor(a): "+getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome();
		}
		if(getApresentarDadosConsultor() && !getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Consultor no Curso: "+getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome();
		}
		if(getApresentarDadosConsultor() && !getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty()
				&& !getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Consultor(a) "+getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome()+" no Curso: "+getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome();
		}
		
		if(getApresentarDadosCurso() && getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty() && getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Curso ";
		}
		if(getApresentarDadosCurso() && getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty() && !getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Curso  do Consultor(a): "+getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome();
		}
		if(getApresentarDadosCurso() && !getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty() 
				&& getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Curso: "+getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome();
		}
		if(getApresentarDadosCurso() && !getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome().trim().isEmpty() 
				&& !getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome().trim().isEmpty()){
			return "Monitoramento Pré-Inscrições Por Curso "+getPainelGestorMonitoramentoDetalheCRMVO().getCurso().getNome()+" do Consultor(a): "+getPainelGestorMonitoramentoDetalheCRMVO().getConsultor().getPessoa().getNome();
		}
		return "Monitoramento Pré-Inscrições";
	}
//	public String getTituloVisualizacaoMonitoramentoProspect() {
//		String matriculado = getFiltrarMatriculado() != null && getFiltrarMatriculado() ?" Matriculados " : getFiltrarMatriculado() != null && !getFiltrarMatriculado() ? " Não Matriculados ":"";
//		if (getTipoFiltroMonitamentoCrmProspectEnum() == null) {			
//			return "Prospects"+matriculado;
//		}
//		
//		switch (getTipoFiltroMonitamentoCrmProspectEnum()) {
//		case COMO_FICOU_SABENDO_INSTITUICAO:
//			return "Como Ficou Sabendo da Instituição - Mída de Captação: "+getTipoMidiaCaptacao().getNomeMidia();
//		case MES_ANO_ABORDADO:
//			 if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				 return "Prospects"+matriculado+" Abordados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno();
//			 }
//			 if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				 return "Prospects"+matriculado+" Abordados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome();
//			 }
//			 if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				 return "Prospects"+matriculado+" Abordados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			 }
//			
//			 
//			 return "Prospects"+matriculado+" Abordados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//			 
//		case MES_ANO_FINALIZADO_SUCESSO:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Prospects"+matriculado+" Finalizados com Sucesso no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Prospects"+matriculado+" Finalizados com Sucesso no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Prospects"+matriculado+" Finalizados com Sucesso no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Prospects"+matriculado+" Finalizados com Sucesso no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case MES_ANO_MATRICULADO:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Prospects Matriculados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Prospects Matriculados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Prospects Matriculados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Prospects Matriculados no mês "+getPainelGestorMonitoramentoDetalheCRMVO().getMesAnoEnum().getMesAbreviado()+" de "+getPainelGestorMonitoramentoDetalheCRMVO().getAno()+" da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case TODOS_PROSPECTS_CADASTRADOS:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Todos os Prospects"+matriculado+" Cadastrados ";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Todos os Prospects"+matriculado+" Cadastrados na Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Todos os Prospects"+matriculado+" Cadastrados na Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Todos os Prospects"+matriculado+" Cadastrados na Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case TODOS_PROSPECTS_COM_AGENDA:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Todos os Prospects"+matriculado+" com Agenda no Período ";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Todos os Prospects"+matriculado+" com Agenda no Período da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Todos os Prospects"+matriculado+" com Agenda no Período da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Todos os Prospects"+matriculado+" com Agenda no Período da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case TODOS_PROSPECTS_SEM_AGENDA:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Todos os Prospects"+matriculado+" sem Agenda no Período ";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Todos os Prospects"+matriculado+" sem Agenda no Período da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Todos os Prospects"+matriculado+" sem Agenda no Período da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Todos os Prospects"+matriculado+" sem Agenda no Período da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case TODOS_PROSPECTS_COM_CONSULTOR:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Todos os Prospects"+matriculado+" com Consultor ";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Todos os Prospects"+matriculado+" com Consultor da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Todos os Prospects"+matriculado+" com Consultor da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Todos os Prospects"+matriculado+" com Consultor da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case TODOS_PROSPECTS_SEM_CONSULTOR:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Todos os Prospects"+matriculado+" sem Consultor ";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Todos os Prospects"+matriculado+" sem Consultor da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Todos os Prospects"+matriculado+" sem Consultor da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			
//			
//			return "Todos os Prospects"+matriculado+" sem Consultor da Unidade: "+getUnidadeEnsino().getNome()+" no Curso: "+getCurso().getNome();
//		case TODOS_PROSPECTS_SEM_CONTATO_PERIODO1:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato1()+" Meses";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato1()+" Meses da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato1()+" Meses da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato1()+" Meses da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case TODOS_PROSPECTS_SEM_CONTATO_PERIODO2:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato2()+" Meses";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato2()+" Meses da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato2()+" Meses da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato2()+" Meses da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//			
//		case TODOS_PROSPECTS_SEM_CONTATO_PERIODO3:
//			if(getPainelGestorTipoMonitoramentoCRMEnum() == null){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato3()+" Meses";
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_UNIDADE)){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato3()+" Meses da Unidade: "+getUnidadeEnsino().getNome();
//			}
//			if(getPainelGestorTipoMonitoramentoCRMEnum().equals(PainelGestorTipoMonitoramentoCRMEnum.PROSPECT_POR_CONSULTOR)){
//				return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato3()+" Meses da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome();
//			}
//			
//			
//			return "Prospect"+matriculado+" Não Contactados a Mais de "+getPeriodoUltimoContato3()+" Meses da Unidade: "+getUnidadeEnsino().getNome()+" do(a) Consultor(a): "+getConsultor().getPessoa().getNome()+" no Curso: "+getCurso().getNome();
//		case SEGMENTACAO_PROSPECT:
//			return "Prospects da segmentação "+ getSegmentacaoOpcaoVO().getSegmentacaoProspectVO().getDescricao() +" na opção "+getSegmentacaoOpcaoVO().getDescricao();			
//
//		default:
//			return "Prospects"+matriculado;
//		}
//
//	}
	
	public Integer getPeriodoUltimoContato1() {
		if (periodoUltimoContato1 == null) {
			periodoUltimoContato1 = 3;
		}
		return periodoUltimoContato1;
	}

	public void setPeriodoUltimoContato1(Integer periodoUltimoContato1) {
		this.periodoUltimoContato1 = periodoUltimoContato1;
	}

	public Integer getPeriodoUltimoContato2() {
		if (periodoUltimoContato2 == null) {
			periodoUltimoContato2 = 6;
		}
		return periodoUltimoContato2;
	}

	public void setPeriodoUltimoContato2(Integer periodoUltimoContato2) {
		this.periodoUltimoContato2 = periodoUltimoContato2;
	}

	public Integer getPeriodoUltimoContato3() {
		if (periodoUltimoContato3 == null) {
			periodoUltimoContato3 = 12;
		}
		return periodoUltimoContato3;
	}

	public void setPeriodoUltimoContato3(Integer periodoUltimoContato3) {
		this.periodoUltimoContato3 = periodoUltimoContato3;
	}

	public String getGraficoComoFicouSabendoInstituicao() {
		if(graficoComoFicouSabendoInstituicao == null){
			graficoComoFicouSabendoInstituicao = "";
		}
		return graficoComoFicouSabendoInstituicao;
	}

	public void setGraficoComoFicouSabendoInstituicao(String graficoComoFicouSabendoInstituicao) {
		this.graficoComoFicouSabendoInstituicao = graficoComoFicouSabendoInstituicao;
	}

	public TipoMidiaCaptacaoVO getTipoMidiaCaptacao() {
		if(tipoMidiaCaptacao == null){
			tipoMidiaCaptacao = new TipoMidiaCaptacaoVO();
		}
		return tipoMidiaCaptacao;
	}

	public void setTipoMidiaCaptacao(TipoMidiaCaptacaoVO tipoMidiaCaptacao) {
		this.tipoMidiaCaptacao = tipoMidiaCaptacao;
	}

	public Integer getQuantidadeTotalProspectsCadastradosMatriculado() {
		if(quantidadeTotalProspectsCadastradosMatriculado == null){
			quantidadeTotalProspectsCadastradosMatriculado = 0;
		}
		return quantidadeTotalProspectsCadastradosMatriculado;
	}

	public void setQuantidadeTotalProspectsCadastradosMatriculado(Integer quantidadeTotalProspectsCadastradosMatriculado) {
		this.quantidadeTotalProspectsCadastradosMatriculado = quantidadeTotalProspectsCadastradosMatriculado;
	}

	public Integer getQuantidadeTotalProspectsComAgendaMatriculado() {
		if(quantidadeTotalProspectsComAgendaMatriculado == null){
			quantidadeTotalProspectsComAgendaMatriculado = 0;
		}
		return quantidadeTotalProspectsComAgendaMatriculado;
	}

	public void setQuantidadeTotalProspectsComAgendaMatriculado(Integer quantidadeTotalProspectsComAgendaMatriculado) {
		this.quantidadeTotalProspectsComAgendaMatriculado = quantidadeTotalProspectsComAgendaMatriculado;
	}

	public Integer getQuantidadeTotalProspectsSemAgendaMatriculado() {
		if(quantidadeTotalProspectsSemAgendaMatriculado == null){
			quantidadeTotalProspectsSemAgendaMatriculado = 0;
		}
		return quantidadeTotalProspectsSemAgendaMatriculado;
	}

	public void setQuantidadeTotalProspectsSemAgendaMatriculado(Integer quantidadeTotalProspectsSemAgendaMatriculado) {
		this.quantidadeTotalProspectsSemAgendaMatriculado = quantidadeTotalProspectsSemAgendaMatriculado;
	}

	public Integer getQuantidadeTotalProspectsSemConsultorResponsavelMatriculado() {
		if(quantidadeTotalProspectsSemConsultorResponsavelMatriculado == null){
			quantidadeTotalProspectsSemConsultorResponsavelMatriculado = 0;
		}
		return quantidadeTotalProspectsSemConsultorResponsavelMatriculado;
	}

	public void setQuantidadeTotalProspectsSemConsultorResponsavelMatriculado(Integer quantidadeTotalProspectsSemConsultorResponsavelMatriculado) {
		this.quantidadeTotalProspectsSemConsultorResponsavelMatriculado = quantidadeTotalProspectsSemConsultorResponsavelMatriculado;
	}

	public Integer getQuantidadeTotalProspectsComConsultorResponsavelMatriculado() {
		if(quantidadeTotalProspectsComConsultorResponsavelMatriculado == null){
			quantidadeTotalProspectsComConsultorResponsavelMatriculado = 0;
		}
		return quantidadeTotalProspectsComConsultorResponsavelMatriculado;
	}

	public void setQuantidadeTotalProspectsComConsultorResponsavelMatriculado(Integer quantidadeTotalProspectsComConsultorResponsavelMatriculado) {
		this.quantidadeTotalProspectsComConsultorResponsavelMatriculado = quantidadeTotalProspectsComConsultorResponsavelMatriculado;
	}

	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo1Matriculado() {
		if(quantidadeTotalProspectsNaoContactadoPeriodo1Matriculado == null){
			quantidadeTotalProspectsNaoContactadoPeriodo1Matriculado = 0;
		}
		return quantidadeTotalProspectsNaoContactadoPeriodo1Matriculado;
	}

	public void setQuantidadeTotalProspectsNaoContactadoPeriodo1Matriculado(Integer quantidadeTotalProspectsNaoContactadoPeriodo1Matriculado) {
		this.quantidadeTotalProspectsNaoContactadoPeriodo1Matriculado = quantidadeTotalProspectsNaoContactadoPeriodo1Matriculado;
	}

	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo2Matriculado() {
		if(quantidadeTotalProspectsNaoContactadoPeriodo2Matriculado == null){
			quantidadeTotalProspectsNaoContactadoPeriodo2Matriculado = 0;
		}
		return quantidadeTotalProspectsNaoContactadoPeriodo2Matriculado;
	}

	public void setQuantidadeTotalProspectsNaoContactadoPeriodo2Matriculado(Integer quantidadeTotalProspectsNaoContactadoPeriodo2Matriculado) {
		this.quantidadeTotalProspectsNaoContactadoPeriodo2Matriculado = quantidadeTotalProspectsNaoContactadoPeriodo2Matriculado;
	}

	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo3Matriculado() {
		if(quantidadeTotalProspectsNaoContactadoPeriodo3Matriculado == null){
			quantidadeTotalProspectsNaoContactadoPeriodo3Matriculado = 0;
		}
		return quantidadeTotalProspectsNaoContactadoPeriodo3Matriculado;
	}

	public void setQuantidadeTotalProspectsNaoContactadoPeriodo3Matriculado(Integer quantidadeTotalProspectsNaoContactadoPeriodo3Matriculado) {
		this.quantidadeTotalProspectsNaoContactadoPeriodo3Matriculado = quantidadeTotalProspectsNaoContactadoPeriodo3Matriculado;
	}

	public Boolean getFiltrarMatriculado() {
		return filtrarMatriculado;
	}

	public void setFiltrarMatriculado(Boolean filtrarMatriculado) {
		this.filtrarMatriculado = filtrarMatriculado;
	}

	public Integer getQuantidadeTotalProspectsCadastradosNaoMatriculado() {
		if(quantidadeTotalProspectsCadastradosNaoMatriculado == null){
			quantidadeTotalProspectsCadastradosNaoMatriculado = 0;
		}
		return quantidadeTotalProspectsCadastradosNaoMatriculado;
	}

	public void setQuantidadeTotalProspectsCadastradosNaoMatriculado(Integer quantidadeTotalProspectsCadastradosNaoMatriculado) {
		this.quantidadeTotalProspectsCadastradosNaoMatriculado = quantidadeTotalProspectsCadastradosNaoMatriculado;
	}

	public Integer getQuantidadeTotalProspectsComAgendaNaoMatriculado() {
		if(quantidadeTotalProspectsComAgendaNaoMatriculado == null){
			quantidadeTotalProspectsComAgendaNaoMatriculado = 0;
		}
		return quantidadeTotalProspectsComAgendaNaoMatriculado;
	}

	public void setQuantidadeTotalProspectsComAgendaNaoMatriculado(Integer quantidadeTotalProspectsComAgendaNaoMatriculado) {
		this.quantidadeTotalProspectsComAgendaNaoMatriculado = quantidadeTotalProspectsComAgendaNaoMatriculado;
	}

	public Integer getQuantidadeTotalProspectsSemAgendaNaoMatriculado() {
		if(quantidadeTotalProspectsSemAgendaNaoMatriculado == null){
			quantidadeTotalProspectsSemAgendaNaoMatriculado = 0;
		}
		return quantidadeTotalProspectsSemAgendaNaoMatriculado;
	}

	public void setQuantidadeTotalProspectsSemAgendaNaoMatriculado(Integer quantidadeTotalProspectsSemAgendaNaoMatriculado) {
		this.quantidadeTotalProspectsSemAgendaNaoMatriculado = quantidadeTotalProspectsSemAgendaNaoMatriculado;
	}

	public Integer getQuantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado() {
		if(quantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado == null){
			quantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado = 0;
		}
		return quantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado;
	}

	public void setQuantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado(Integer quantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado) {
		this.quantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado = quantidadeTotalProspectsSemConsultorResponsavelNaoMatriculado;
	}

	public Integer getQuantidadeTotalProspectsComConsultorResponsavelNaoMatriculado() {
		if(quantidadeTotalProspectsComConsultorResponsavelNaoMatriculado == null){
			quantidadeTotalProspectsComConsultorResponsavelNaoMatriculado = 0;
		}
		return quantidadeTotalProspectsComConsultorResponsavelNaoMatriculado;
	}

	public void setQuantidadeTotalProspectsComConsultorResponsavelNaoMatriculado(Integer quantidadeTotalProspectsComConsultorResponsavelNaoMatriculado) {
		this.quantidadeTotalProspectsComConsultorResponsavelNaoMatriculado = quantidadeTotalProspectsComConsultorResponsavelNaoMatriculado;
	}

	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado() {
		if(quantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado == null){
			quantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado = 0;
		}
		return quantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado;
	}

	public void setQuantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado(Integer quantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado) {
		this.quantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado = quantidadeTotalProspectsNaoContactadoPeriodo1NaoMatriculado;
	}

	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado() {
		if(quantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado == null){
			quantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado = 0;
		}
		return quantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado;
	}

	public void setQuantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado(Integer quantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado) {
		this.quantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado = quantidadeTotalProspectsNaoContactadoPeriodo2NaoMatriculado;
	}

	public Integer getQuantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado() {
		if(quantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado == null){
			quantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado = 0;
		}
		return quantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado;
	}

	public void setQuantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado(Integer quantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado) {
		this.quantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado = quantidadeTotalProspectsNaoContactadoPeriodo3NaoMatriculado;
	}
	
	

}
