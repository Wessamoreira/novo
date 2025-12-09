package negocio.comuns.secretaria;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.EnadeCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.enumeradores.SituacaoMapaConvocacaoEnadeEnum;
import negocio.comuns.utilitarias.Uteis;

public class MapaConvocacaoEnadeVO extends SuperVO implements Serializable {

	private Integer codigo;
	private String unidadeEnsino;
	private EnadeCursoVO enadeCursoVO;
	private UsuarioVO responsavel;
	private Date dataAbertura;
	private Date dataFechamento;
	private Date dataPrevisaoConclusao;

	private List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaAlunosIngressantesVOs;
	private List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaAlunosDispensadosVOs;
	private List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaAlunosConcluintesVOs;

	private Integer totalAlunosIngressantes;
	private Integer totalAlunosDispensados;
	private Integer totalAlunosConcluintes;

	private SituacaoMapaConvocacaoEnadeEnum situacaoMapaConvocacaoEnade;
	private Boolean possuiErro;
	private List<MapaConvocacaoEnadeMatriculaVO> listaAlunoEnade;
	private List<EnadeCursoVO> listaCursoEnade;
	private ArquivoVO arquivoAlunoIngressante;
	private ArquivoVO arquivoAlunoConcluinte;
	private ArquivoVO arquivoAlunoExcel;
	private String NomeArquivoTXT;
	private List<MapaConvocacaoEnadeMatriculaVO> listaAlunoErroGeracaoArquivoEnade;
	private List<MapaConvocacaoEnadeVO> listaCursoErroGeracaoArquivoEnade;
	private Integer codigoSequencialArquivoEnade;
	private Integer arquivoMapaConvocacaoEnadeTxt;
	private List<MapaConvocacaoEnadeVO> listaSituacaoEnade;
	private Boolean ingressantes;
	private Boolean concluintes;
	private Integer codigoArquivoTxt;
	private String errosGeracaoArquivoCurso;
	private MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtIngressantes;
	private MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtConcluintes;	
	private boolean isMostrarDownloadArquivoIngressante;
	private boolean isMostrarDownloadArquivoConcluinte;
	private Boolean imprimirPDF;
	
	public static final String SEPARADOR = ";";

	private static final long serialVersionUID = 1L;

	public MapaConvocacaoEnadeVO() {
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

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDataAbertura() {
		if (dataAbertura == null) {
			dataAbertura = new Date();
		}
		return dataAbertura;
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public Date getDataPrevisaoConclusao() {
		return dataPrevisaoConclusao;
	}

	public void setDataPrevisaoConclusao(Date dataPrevisaoConclusao) {
		this.dataPrevisaoConclusao = dataPrevisaoConclusao;
	}

	public List<MapaConvocacaoEnadeMatriculaVO> getMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs() {
		if (mapaConvocacaoEnadeMatriculaAlunosIngressantesVOs == null) {
			mapaConvocacaoEnadeMatriculaAlunosIngressantesVOs = new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0);
		}
		return mapaConvocacaoEnadeMatriculaAlunosIngressantesVOs;
	}

	public void setMapaConvocacaoEnadeMatriculaAlunosIngressantesVOs(List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaAlunosIngressantesVOs) {
		this.mapaConvocacaoEnadeMatriculaAlunosIngressantesVOs = mapaConvocacaoEnadeMatriculaAlunosIngressantesVOs;
	}

	public List<MapaConvocacaoEnadeMatriculaVO> getMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs() {
		if (mapaConvocacaoEnadeMatriculaAlunosDispensadosVOs == null) {
			mapaConvocacaoEnadeMatriculaAlunosDispensadosVOs = new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0);
		}
		return mapaConvocacaoEnadeMatriculaAlunosDispensadosVOs;
	}

	public void setMapaConvocacaoEnadeMatriculaAlunosDispensadosVOs(List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaAlunosDispensadosVOs) {
		this.mapaConvocacaoEnadeMatriculaAlunosDispensadosVOs = mapaConvocacaoEnadeMatriculaAlunosDispensadosVOs;
	}

	public List<MapaConvocacaoEnadeMatriculaVO> getMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs() {
		if (mapaConvocacaoEnadeMatriculaAlunosConcluintesVOs == null) {
			mapaConvocacaoEnadeMatriculaAlunosConcluintesVOs = new ArrayList<MapaConvocacaoEnadeMatriculaVO>(0);
		}
		return mapaConvocacaoEnadeMatriculaAlunosConcluintesVOs;
	}

	public void setMapaConvocacaoEnadeMatriculaAlunosConcluintesVOs(List<MapaConvocacaoEnadeMatriculaVO> mapaConvocacaoEnadeMatriculaAlunosConcluintesVOs) {
		this.mapaConvocacaoEnadeMatriculaAlunosConcluintesVOs = mapaConvocacaoEnadeMatriculaAlunosConcluintesVOs;
	}

	public Integer getTotalAlunosIngressantes() {
		if (totalAlunosIngressantes == null) {
			totalAlunosIngressantes = 0;
		}
		return totalAlunosIngressantes;
	}

	public void setTotalAlunosIngressantes(Integer totalAlunosIngressantes) {
		this.totalAlunosIngressantes = totalAlunosIngressantes;
	}

	public Integer getTotalAlunosDispensados() {
		if (totalAlunosDispensados == null) {
			totalAlunosDispensados = 0;
		}
		return totalAlunosDispensados;
	}

	public void setTotalAlunosDispensados(Integer totalAlunosDispensados) {
		this.totalAlunosDispensados = totalAlunosDispensados;
	}

	public Integer getTotalAlunosConcluintes() {
		if (totalAlunosConcluintes == null) {
			totalAlunosConcluintes = 0;
		}
		return totalAlunosConcluintes;
	}

	public void setTotalAlunosConcluintes(Integer totalAlunosConcluintes) {
		this.totalAlunosConcluintes = totalAlunosConcluintes;
	}

	public String getDataAbertura_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataAbertura());
	}

	public String getDataFechamento_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataFechamento());
	}

	public String getDataPrevisaoConclusao_Apresentar() {
		return Uteis.getDataAno4Digitos(getDataPrevisaoConclusao());
	}

	public SituacaoMapaConvocacaoEnadeEnum getSituacaoMapaConvocacaoEnade() {
		if (situacaoMapaConvocacaoEnade == null) {
			situacaoMapaConvocacaoEnade = SituacaoMapaConvocacaoEnadeEnum.MAPA_EM_CONSTRUCAO;
		}
		return situacaoMapaConvocacaoEnade;
	}

	public void setSituacaoMapaConvocacaoEnade(SituacaoMapaConvocacaoEnadeEnum situacaoMapaConvocacaoEnade) {
		this.situacaoMapaConvocacaoEnade = situacaoMapaConvocacaoEnade;
	}

	public String getSituacaoMapaConvocacaoEnade_Apresentar() {
		return SituacaoMapaConvocacaoEnadeEnum.getDescricao(getSituacaoMapaConvocacaoEnade());
	}

	public EnadeCursoVO getEnadeCursoVO() {
		if (enadeCursoVO == null) {
			enadeCursoVO = new EnadeCursoVO();
		}
		return enadeCursoVO;
	}

	public void setEnadeCursoVO(EnadeCursoVO enadeCursoVO) {
		this.enadeCursoVO = enadeCursoVO;
	}
	public Boolean getPossuiErro() {
		if(possuiErro == null) {
			possuiErro = Boolean.FALSE;
		}
		return possuiErro;
	}

	public void setPossuiErro(Boolean possuiErro) {
		this.possuiErro = possuiErro;
	}
	public List<EnadeCursoVO> getListaCursoEnade() {
		if (listaCursoEnade == null) {
			listaCursoEnade = new ArrayList<EnadeCursoVO>(0);
		}
		return listaCursoEnade;
	}

	public void setListaCursoCenso(List<EnadeCursoVO> listaCursoEnade) {
		this.listaCursoEnade = listaCursoEnade;
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
	public ArquivoVO getArquivoAlunoExcel() {
		if (arquivoAlunoExcel == null) {
			arquivoAlunoExcel = new ArquivoVO();
		}
		return arquivoAlunoExcel;
	}

	public void setArquivoAlunoExcel(ArquivoVO arquivoAlunoExcel) {
		this.arquivoAlunoExcel = arquivoAlunoExcel;
	}
	public ArquivoVO getArquivoAlunoIngressante() {
		if (arquivoAlunoIngressante == null) {
			arquivoAlunoIngressante = new ArquivoVO();
		}
		return arquivoAlunoIngressante;
	}

	public void setArquivoAlunoIngressante(ArquivoVO arquivoAlunoIngressante) {
		this.arquivoAlunoIngressante = arquivoAlunoIngressante;
	}

	public ArquivoVO getArquivoAlunoConcluinte() {
		if (arquivoAlunoConcluinte == null) {
			arquivoAlunoConcluinte = new ArquivoVO();
		}
		return arquivoAlunoConcluinte;
	}

	public void setArquivoAlunoConcluinte(ArquivoVO arquivoAlunoConcluinte) {
		this.arquivoAlunoConcluinte = arquivoAlunoConcluinte;
	}
	public String getNomeArquivoTXT() {
		return NomeArquivoTXT;
	}

	public void setNomeArquivoTXT(String nomeArquivoTXT) {
		NomeArquivoTXT = nomeArquivoTXT;
	}

	public List<MapaConvocacaoEnadeMatriculaVO> getListaAlunoErroGeracaoArquivoEnade() {
		if (listaAlunoErroGeracaoArquivoEnade == null) {
			listaAlunoErroGeracaoArquivoEnade = new ArrayList<MapaConvocacaoEnadeMatriculaVO>();
		}
		return listaAlunoErroGeracaoArquivoEnade;
	}

	public void setListaAlunoErroGeracaoArquivoEnade(List<MapaConvocacaoEnadeMatriculaVO> listaAlunoErroGeracaoArquivoEnade) {
		this.listaAlunoErroGeracaoArquivoEnade = listaAlunoErroGeracaoArquivoEnade;
	}

	
	public List<MapaConvocacaoEnadeVO> getlistaCursoErroGeracaoArquivoEnade() {
		if (listaCursoErroGeracaoArquivoEnade == null) {
			listaCursoErroGeracaoArquivoEnade = new ArrayList<MapaConvocacaoEnadeVO>();
		}
		return listaCursoErroGeracaoArquivoEnade;
	}

	public void setListaCursoErroGeracaoArquivoEnade(List<MapaConvocacaoEnadeVO> listaCursoErroGeracaoArquivoEnade) {
		this.listaCursoErroGeracaoArquivoEnade = listaCursoErroGeracaoArquivoEnade;
	}
	
	public Integer getCodigoSequencialArquivoEnade() {
		if (codigoSequencialArquivoEnade == null) {
			codigoSequencialArquivoEnade = 0;
		}
		return codigoSequencialArquivoEnade;
	}

	public void setCodigoSequencialArquivoEnade(Integer codigoSequencialArquivoEnade) {
		this.codigoSequencialArquivoEnade = codigoSequencialArquivoEnade;
	}

	public Integer getArquivoMapaConvocacaoEnadeTxt() {
		return arquivoMapaConvocacaoEnadeTxt;
	}

	public void setArquivoMapaConvocacaoEnadeTxt(Integer arquivoMapaConvocacaoEnadeTxt) {
		if (arquivoMapaConvocacaoEnadeTxt == null) {
			arquivoMapaConvocacaoEnadeTxt = 0;
		}
		this.arquivoMapaConvocacaoEnadeTxt = arquivoMapaConvocacaoEnadeTxt;
	}


	public List<MapaConvocacaoEnadeVO> getListaSituacaoEnade() {
		if (listaSituacaoEnade == null) {
			listaSituacaoEnade = new ArrayList<MapaConvocacaoEnadeVO>(0);
		}
		return listaSituacaoEnade;
	}

	public void setListaSituacaoEnade(List<MapaConvocacaoEnadeVO> listaSituacaoEnade) {
		this.listaSituacaoEnade = listaSituacaoEnade;
	}

	public Boolean getIngressantes() {
		if(ingressantes == null) {
			ingressantes = Boolean.FALSE;
		}
		return ingressantes;
	}

	public void setIngressantes(Boolean ingressantes) {
		this.ingressantes = ingressantes;
	}

	public Boolean getConcluintes() {
		if(concluintes == null) {
			concluintes = Boolean.FALSE;
		}
		return concluintes;
	}

	public void setConcluintes(Boolean concluintes) {
		this.concluintes = concluintes;
	}

	
	public Integer getCodigoArquivoTxt() {
		if (codigoArquivoTxt == null) {
			codigoArquivoTxt = 0;
		}
		return codigoArquivoTxt;
	}

	public void setCodigoArquivoTxt(Integer codigoArquivoTxt) {
		this.codigoArquivoTxt = codigoArquivoTxt;
	}
	public String getErrosGeracaoArquivoCurso() {
		if (errosGeracaoArquivoCurso == null) {
			errosGeracaoArquivoCurso = "";
		}
		return errosGeracaoArquivoCurso;
	}

	public void setErrosGeracaoArquivoCurso(String errosGeracaoArquivoCurso) {
		this.errosGeracaoArquivoCurso = errosGeracaoArquivoCurso;
	}
	
	public MapaConvocacaoEnadeVO getMapaConvocacaoEnadeVORelTxtIngressantes() {
		if (mapaConvocacaoEnadeVORelTxtIngressantes == null) {
			mapaConvocacaoEnadeVORelTxtIngressantes = new MapaConvocacaoEnadeVO();
		}
		return mapaConvocacaoEnadeVORelTxtIngressantes;
	}

	public void setMapaConvocacaoEnadeVORelTxtIngressantes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtIngressantes) {
		this.mapaConvocacaoEnadeVORelTxtIngressantes = mapaConvocacaoEnadeVORelTxtIngressantes;
	}
	public MapaConvocacaoEnadeVO getMapaConvocacaoEnadeVORelTxtConcluintes() {
		if (mapaConvocacaoEnadeVORelTxtConcluintes == null) {
			mapaConvocacaoEnadeVORelTxtConcluintes = new MapaConvocacaoEnadeVO();
		}
		return mapaConvocacaoEnadeVORelTxtConcluintes;
	}

	public void setMapaConvocacaoEnadeVORelTxtConcluintes(MapaConvocacaoEnadeVO mapaConvocacaoEnadeVORelTxtConcluintes) {
		this.mapaConvocacaoEnadeVORelTxtConcluintes = mapaConvocacaoEnadeVORelTxtConcluintes;
	}
	
	public boolean isIsMostrarDownloadArquivoIngressante() {
		return isMostrarDownloadArquivoIngressante;
	}

	public void setIsMostrarDownloadArquivoIngressante(boolean isMostrarDownloadArquivoIngressante) {
		this.isMostrarDownloadArquivoIngressante = isMostrarDownloadArquivoIngressante;
	}
		
	public boolean isIsMostrarDownloadArquivoConcluinte() {
		return isMostrarDownloadArquivoConcluinte;
	}

	public void setIsMostrarDownloadArquivoConcluinte(boolean isMostrarDownloadArquivoConcluinte) {
		this.isMostrarDownloadArquivoConcluinte = isMostrarDownloadArquivoConcluinte;
	}

	public Boolean getImprimirPDF() {
		if(imprimirPDF == null) {
			imprimirPDF = Boolean.FALSE;
		}
		return imprimirPDF;
	}

	public void setImprimirPDF(Boolean imprimirPDF) {
		this.imprimirPDF = imprimirPDF;
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

	
	
}
