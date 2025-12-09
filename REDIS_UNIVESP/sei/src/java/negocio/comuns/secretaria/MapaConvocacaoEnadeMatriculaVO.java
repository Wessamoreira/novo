package negocio.comuns.secretaria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.secretaria.enumeradores.SituacaoConvocadosEnadeEnum;
import negocio.comuns.utilitarias.Uteis;

public class MapaConvocacaoEnadeMatriculaVO extends SuperVO implements Serializable {

	private Integer codigo;
	private MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO;
	private MatriculaVO matriculaVO;
	private FormacaoAcademicaVO formacaoAcademicaVO;
	private PeriodoLetivoVO periodoLetivoAtual;
	private Double percentualIntegralizacaoAtual;
	private Double percentualIntegralizacaoPossivelCursar;
	private Double percentualIntegralizacaoPrevistoDataEnade;
	private String observacao;
	private SituacaoConvocadosEnadeEnum situacaoConvocadosEnade;
	private String anoIngresso;
	private String semestreIngresso;
	private String anoAtual;
	private String semestreAtual;
	private String layout;
	private Double cargaHorariaAtual;
	private String anoSemestreIngresso;
	private List<MapaConvocacaoEnadeMatriculaVO> listaAlunoEnade;
	private List<EnadeCursoVO> listaCursoEnade;
	private ArquivoVO arquivoAluno;
	private static final long serialVersionUID = 1L;
	private String errosGeracaoArquivo;
	private String unidadeEnsino;

	public MapaConvocacaoEnadeMatriculaVO() {
		super();
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

	public MapaConvocacaoEnadeVO getMapaConvocacaoEnadeVO() {
		if (mapaConvocacaoEnadeVO == null) {
			mapaConvocacaoEnadeVO = new MapaConvocacaoEnadeVO();
		}
		return mapaConvocacaoEnadeVO;
	}

	public void setMapaConvocacaoEnadeVO(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVO) {
		this.mapaConvocacaoEnadeVO = mapaConvocacaoEnadeVO;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	// public PeriodoLetivoVO getPeriodoLetivoAcademico() {
	// if (periodoLetivoAcademico == null) {
	// periodoLetivoAcademico = new PeriodoLetivoVO();
	// }
	// return periodoLetivoAcademico;
	// }
	//
	// public void setPeriodoLetivoAcademico(PeriodoLetivoVO
	// periodoLetivoAcademico) {
	// this.periodoLetivoAcademico = periodoLetivoAcademico;
	// }

	// public Double getPercentualIntegralizacao() {
	// if (percentualIntegralizacao == null) {
	// percentualIntegralizacao = 0.0;
	// }
	// return percentualIntegralizacao;
	// }
	//
	// public void setPercentualIntegralizacao(Double percentualIntegralizacao)
	// {
	// this.percentualIntegralizacao = percentualIntegralizacao;
	// }

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public SituacaoConvocadosEnadeEnum getSituacaoConvocadosEnade() {
		if (situacaoConvocadosEnade == null) {
			situacaoConvocadosEnade = SituacaoConvocadosEnadeEnum.ALUNO_INGRESSANTE;
		}
		return situacaoConvocadosEnade;
	}

	public void setSituacaoConvocadosEnade(SituacaoConvocadosEnadeEnum situacaoConvocadosEnade) {
		this.situacaoConvocadosEnade = situacaoConvocadosEnade;
	}

	public String getNomeAluno() {
		return getMatriculaVO().getAluno().getNome();
	}

//	public String getOrdenacao1() {
//		return getMatriculaVO().getAluno().getNome();
//	}
//
//	public String getOrdenacao2() {
//		return getMatriculaVO().getMatricula();
//	}

	public PeriodoLetivoVO getPeriodoLetivoAtual() {
		if (periodoLetivoAtual == null) {
			periodoLetivoAtual = new PeriodoLetivoVO();
		}
		return periodoLetivoAtual;
	}

	public void setPeriodoLetivoAtual(PeriodoLetivoVO periodoLetivoAtual) {
		this.periodoLetivoAtual = periodoLetivoAtual;
	}

	public Double getPercentualIntegralizacaoPrevistoDataEnade() {
		if (percentualIntegralizacaoPrevistoDataEnade == null) {
			percentualIntegralizacaoPrevistoDataEnade = 0.0;
		}
		return percentualIntegralizacaoPrevistoDataEnade;
	}

	public void setPercentualIntegralizacaoPrevistoDataEnade(Double percentualIntegralizacaoPrevistoDataEnade) {
		this.percentualIntegralizacaoPrevistoDataEnade = percentualIntegralizacaoPrevistoDataEnade;
	}

	public Double getPercentualIntegralizacaoAtual() {
		if (percentualIntegralizacaoAtual == null) {
			percentualIntegralizacaoAtual = 0.0;
		}
		return percentualIntegralizacaoAtual;
	}

	public void setPercentualIntegralizacaoAtual(Double percentualIntegralizacaoAtual) {
		this.percentualIntegralizacaoAtual = percentualIntegralizacaoAtual;
	}

	public Double getPercentualIntegralizacaoPossivelCursar() {
		if (percentualIntegralizacaoPossivelCursar == null) {
			percentualIntegralizacaoPossivelCursar = 0.0;
		}
		return percentualIntegralizacaoPossivelCursar;
	}

	public void setPercentualIntegralizacaoPossivelCursar(Double percentualIntegralizacaoPossivelCursar) {
		this.percentualIntegralizacaoPossivelCursar = percentualIntegralizacaoPossivelCursar;
	}

	public String getAnoIngresso() {
		if (anoIngresso == null) {
			anoIngresso = "";
		}
		return anoIngresso;
	}

	public void setAnoIngresso(String anoIngresso) {
		this.anoIngresso = anoIngresso;
	}

	public String getSemestreIngresso() {
		if (semestreIngresso == null) {
			semestreIngresso = "";
		}
		return semestreIngresso;
	}

	public void setSemestreIngresso(String semestreIngresso) {
		this.semestreIngresso = semestreIngresso;
	}

	public String getAnoAtual() {
		if (anoAtual == null) {
			anoAtual = Uteis.getAnoDataAtual();
		}
		return anoAtual;
	}

	public void setAnoAtual(String anoAtual) {
		this.anoAtual = anoAtual;
	}

	public String getSemestreAtual() {
		if (semestreAtual == null) {
			semestreAtual = Uteis.getSemestreAtual();
		}
		return semestreAtual;
	}

	public void setSemestreAtual(String semestreAtual) {
		this.semestreAtual = semestreAtual;
	}
	
	public FormacaoAcademicaVO getFormacaoAcademicaVO() {
		if (formacaoAcademicaVO == null) {
			formacaoAcademicaVO = new FormacaoAcademicaVO();
		}
		return formacaoAcademicaVO;
	}

	public void setFormacaoAcademicaVO(FormacaoAcademicaVO formacaoAcademicaVO) {
		this.formacaoAcademicaVO = formacaoAcademicaVO;
	}

	public Double getCargaHorariaAtual() {
		if (cargaHorariaAtual == null) {
			cargaHorariaAtual = 0.0;
		}
		return cargaHorariaAtual;
	}

	public void setCargaHorariaAtual(Double cargaHorariaAtual) {
		this.cargaHorariaAtual = cargaHorariaAtual;
	}
	public void realizarUpperCaseDados() {
		if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
			return;
		}
		setAnoAtual(getAnoAtual().toUpperCase());
		setLayout(getLayout().toUpperCase());
	}
	public String getLayout() {
		if (layout == null) {
			layout = "GRADUACAO";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
	
	public boolean isLayoutGraduacao(){
		return getLayout().equals("GRADUACAO") ||  getLayout().equals("GRADUACAO_TECNOLOGICA"); 
	}
	public List<MapaConvocacaoEnadeMatriculaVO> getListaAlunoEnade() {
		if (listaAlunoEnade == null) {
			listaAlunoEnade = new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0);
		}
		return listaAlunoEnade;
	}

	public void setListaAlunoEnade(List<MapaConvocacaoEnadeMatriculaVO> listaAlunoEnade) {
		this.listaAlunoEnade = listaAlunoEnade;
	}

	public List<EnadeCursoVO> getListaCursoEnade() {
		if (listaCursoEnade == null) {
			listaCursoEnade = new ArrayList<EnadeCursoVO>(0);
		}
		return listaCursoEnade;
	}

	public void setListaCursoEnade(List<EnadeCursoVO> listaCursoEnade) {
		this.listaCursoEnade = listaCursoEnade;
	}
	public ArquivoVO getArquivoAluno() {
		if (arquivoAluno == null) {
			arquivoAluno = new ArquivoVO();
		}
		return arquivoAluno;
	}

	public void setArquivoAluno(ArquivoVO arquivoAluno) {
		this.arquivoAluno = arquivoAluno;
	}

	public String getErrosGeracaoArquivo() {
		if (errosGeracaoArquivo == null) {
			errosGeracaoArquivo = "";
		}
		return errosGeracaoArquivo;
	}

	public void setErrosGeracaoArquivo(String errosGeracaoArquivo) {
		this.errosGeracaoArquivo = errosGeracaoArquivo;
	}
	public String getAnoSemestreIngresso() {
		if (anoSemestreIngresso == null) {
			anoSemestreIngresso = "";
		}
		return anoSemestreIngresso;
	}

	public void setAnoSemestreIngresso(String anoSemestreIngresso) {
		this.anoSemestreIngresso = anoSemestreIngresso;
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getOrdenacaoNome() {
		return getMatriculaVO().getUnidadeEnsino().getNome() + getSituacaoConvocadosEnade() + getMatriculaVO().getCurso().getNome() + getMatriculaVO().getTurno().getNome() + getMatriculaVO().getAluno().getNome();
	}
	
	public String getOrdenacaoMatricula() {
		return getMatriculaVO().getUnidadeEnsino().getNome() + getSituacaoConvocadosEnade() + getMatriculaVO().getCurso().getNome() + getMatriculaVO().getTurno().getNome() + getMatriculaVO().getMatricula();
	}
	
}
