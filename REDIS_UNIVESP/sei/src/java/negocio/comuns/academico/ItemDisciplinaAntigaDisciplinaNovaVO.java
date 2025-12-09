package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Matricula;

/**
 * Reponsável por manter os dados da entidade MatriculaPeriodo. Classe do tipo VO - Value Object composta pelos
 * atributos da entidade com visibilidade protegida e os mï¿½todos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memï¿½ria os dados desta entidade.
 * 
 * @see SuperVO
 * @see Matricula
 */
public class ItemDisciplinaAntigaDisciplinaNovaVO extends SuperVO {

    private Integer codigo;
    private List<DisciplinaVO> listaDisciplinaGradeOrigemVOs;
    private List<HistoricoVO> historicoDisciplinaEquivalenteVOs;
    private List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaGradeMigrarVOs;
    private String ano;
    private String semestre;
    private Double mediaFinal;
    private ConfiguracaoAcademicoNotaConceitoVO mediaFinalNotaConceito;
    private Double frequencia;
    private Integer cargaHorariaAproveitamentoDisciplina;
    private Integer cargaHorariaCursada;
    private String instituicao;
    private CidadeVO cidadeVO;
    private String situacao;
    private Boolean isentarMediaFinal;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrï¿½o da classe <code>MatriculaPeriodo</code>. Cria uma nova instï¿½ncia desta entidade,
     * inicializando automaticamente seus atributos (Classe VO).
     */
    public ItemDisciplinaAntigaDisciplinaNovaVO() {
        super();
    }

    public static void validarDados(ItemDisciplinaAntigaDisciplinaNovaVO obj) throws ConsistirException {
        // if ((obj.getTurma() == null) ||
        // (obj.getTurma().getCodigo().intValue() == 0)) {
        // throw new
        // ConsistirException("O campo TURMA (Matrícula Período Letivo) deve ser informado.");
        // }
    }

    public String getApresentarListaDisciplinaMigrar() {
        String valor = "<div style=\"width:300px;margin-left:8px\"><ul>";
        for (MatriculaPeriodoTurmaDisciplinaVO disc : getListaDisciplinaGradeMigrarVOs()) {
            valor += "<li>"+disc.getDisciplina().getNome()+"- CH: "+ disc.getGradeDisciplinaVO().getCargaHoraria() + " - " + disc.getGradeDisciplinaVO().getPeriodoLetivoVO().getDescricao()+ "</li> ";
        }
        valor += "</ul></div>";
        return valor;
    }

    public String getApresentarListaDisciplinaOrigem() {
        String valor = "<div style=\"width:270px;margin-left:8px\"><ul>";
//        for (DisciplinaVO disc : getListaDisciplinaGradeOrigemVOs()) {
//            valor += "<li>"+disc.getNome() + "</li> ";
//        }
        for(HistoricoVO historicoVO:getHistoricoDisciplinaEquivalenteVOs()){
        	 valor += "<li>"+historicoVO.getDisciplina().getNome() +" - CH:"+historicoVO.getGradeDisciplinaVO().getCargaHoraria()+" - MF: "+historicoVO.getMediaFinal_Apresentar()+ " - Freq: "+historicoVO.getFrequencia_Apresentar()+"- Sit:"+historicoVO.getSituacao_Apresentar()+"</li> ";
        	 
        }
        valor += "</ul></div>";
        return valor;
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the listaDisciplinaGradeOrigemVOs
     */
    public List<DisciplinaVO> getListaDisciplinaGradeOrigemVOs() {
        if (listaDisciplinaGradeOrigemVOs == null) {
            listaDisciplinaGradeOrigemVOs = new ArrayList<DisciplinaVO>();
        }
        return listaDisciplinaGradeOrigemVOs;
    }

    /**
     * @param listaDisciplinaGradeOrigemVOs the listaDisciplinaGradeOrigemVOs to set
     */
    public void setListaDisciplinaGradeOrigemVOs(List<DisciplinaVO> listaDisciplinaGradeOrigemVOs) {
        this.listaDisciplinaGradeOrigemVOs = listaDisciplinaGradeOrigemVOs;
    }

    /**
     * @return the listaDisciplinaGradeMigrarVOs
     */
    public List<MatriculaPeriodoTurmaDisciplinaVO> getListaDisciplinaGradeMigrarVOs() {
        if (listaDisciplinaGradeMigrarVOs == null) {
            listaDisciplinaGradeMigrarVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
        }
        return listaDisciplinaGradeMigrarVOs;
    }

    /**
     * @param listaDisciplinaGradeMigrarVOs the listaDisciplinaGradeMigrarVOs to set
     */
    public void setListaDisciplinaGradeMigrarVOs(List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaGradeMigrarVOs) {
        this.listaDisciplinaGradeMigrarVOs = listaDisciplinaGradeMigrarVOs;
    }

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Double getMediaFinal() {		
		return mediaFinal;
	}

	public void setMediaFinal(Double mediaFinal) {
		this.mediaFinal = mediaFinal;
	}

	public Double getFrequencia() {		
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public List<HistoricoVO> getHistoricoDisciplinaEquivalenteVOs() {
		if(historicoDisciplinaEquivalenteVOs == null){
			historicoDisciplinaEquivalenteVOs = new ArrayList<HistoricoVO>(0);
		}
		return historicoDisciplinaEquivalenteVOs;
	}

	public void setHistoricoDisciplinaEquivalenteVOs(List<HistoricoVO> historicoDisciplinaEquivalenteVOs) {
		this.historicoDisciplinaEquivalenteVOs = historicoDisciplinaEquivalenteVOs;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getMediaFinalNotaConceito() {
		if(mediaFinalNotaConceito == null){
			mediaFinalNotaConceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return mediaFinalNotaConceito;
	}

	public void setMediaFinalNotaConceito(ConfiguracaoAcademicoNotaConceitoVO mediaFinalNotaConceito) {
		this.mediaFinalNotaConceito = mediaFinalNotaConceito;
	}

	public String getSituacao() {
		if(situacao == null){
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getCargaHorariaAproveitamentoDisciplina() {
		if(cargaHorariaAproveitamentoDisciplina == null){
			cargaHorariaAproveitamentoDisciplina = 0;
		}
		return cargaHorariaAproveitamentoDisciplina;
	}

	public void setCargaHorariaAproveitamentoDisciplina(Integer cargaHorariaAproveitamentoDisciplina) {
		this.cargaHorariaAproveitamentoDisciplina = cargaHorariaAproveitamentoDisciplina;
	}

	public String getInstituicao() {
		if(instituicao == null){
			instituicao = "";
		}
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public CidadeVO getCidadeVO() {
		if(cidadeVO == null){
			cidadeVO = new CidadeVO();
		}
		return cidadeVO;
	}

	public void setCidadeVO(CidadeVO cidadeVO) {
		this.cidadeVO = cidadeVO;
	}
	
	public boolean getApresentarDadosAproveitamento(){
		return getSituacao().equals("AA") || getSituacao().equals("CC");
	}

	public Integer getCargaHorariaCursada() {
		if(cargaHorariaCursada == null){
			cargaHorariaCursada = 0;
		}
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

	public Boolean getIsentarMediaFinal() {
		if (isentarMediaFinal == null) {
			isentarMediaFinal = false;
		}
		return isentarMediaFinal;
	}

	public void setIsentarMediaFinal(Boolean isentarMediaFinal) {
		this.isentarMediaFinal = isentarMediaFinal;
	}
    
    
}
