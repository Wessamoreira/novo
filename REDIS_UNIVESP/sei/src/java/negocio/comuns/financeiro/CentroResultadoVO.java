package negocio.comuns.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.enumerador.RestricaoUsoCentroResultadoEnum;

/**
 * 
 * @author PedroOtimize
 *
 */
public class CentroResultadoVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1393838151050579833L;
	private Integer codigo;
	private CentroResultadoVO centroResultadoPrincipal;
	private String identificadorCentroResultado;
	private String descricao;
	private SituacaoEnum situacaoEnum;
	private RestricaoUsoCentroResultadoEnum restricaoUsoCentroResultadoEnum;
	private List<CentroResultadoRestricaoUsoVO> listaCentroResultadoRestricaoUsoVOs;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	
	private TreeNodeCustomizado arvoreCentroResultado;
	
	private Boolean filtrarCentroResultado = false;
	
	/*atributos transientes*/
	private String tipoNivelCentroResultado;
	private Integer codigoOrigem;
	private String nomeOrigem;
	
	public enum enumCampoConsultaCentroResultado {
		IDENTIFICADOR_CENTRO_RESULTADO, 
		DESCRICAO_CENTRO_RESULTADO,
		DESCRICAO_CENTRO_RESULTADO_SUPERIOR;
	}

	public Integer getCodigo() {
		codigo = Optional.ofNullable(codigo).orElse(0);
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public SituacaoEnum getSituacaoEnum() {
		situacaoEnum = Optional.ofNullable(situacaoEnum).orElse(SituacaoEnum.EM_CONSTRUCAO);
		return situacaoEnum;
	}

	public void setSituacaoEnum(SituacaoEnum situacaoEnum) {
		this.situacaoEnum = situacaoEnum;
	}

	public CentroResultadoVO getCentroResultadoPrincipal() {
		centroResultadoPrincipal = Optional.ofNullable(centroResultadoPrincipal).orElse(new CentroResultadoVO());
		return centroResultadoPrincipal;
	}

	public void setCentroResultadoPrincipal(CentroResultadoVO centroResultadoPrincipal) {
		this.centroResultadoPrincipal = centroResultadoPrincipal;
	}

	public String getIdentificadorCentroResultado() {
		identificadorCentroResultado = Optional.ofNullable(identificadorCentroResultado).orElse("");
		return identificadorCentroResultado;
	}

	public void setIdentificadorCentroResultado(String identificadorCentroResultado) {
		this.identificadorCentroResultado = identificadorCentroResultado;
	}

	public String getDescricao() {
		descricao = Optional.ofNullable(descricao).orElse("");
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	

	public RestricaoUsoCentroResultadoEnum getRestricaoUsoCentroResultadoEnum() {
		restricaoUsoCentroResultadoEnum = Optional.ofNullable(restricaoUsoCentroResultadoEnum).orElse(RestricaoUsoCentroResultadoEnum.NENHUM);
		return restricaoUsoCentroResultadoEnum;
	}

	public void setRestricaoUsoCentroResultadoEnum(RestricaoUsoCentroResultadoEnum restricaoUsoCentroResultadoEnum) {
		this.restricaoUsoCentroResultadoEnum = restricaoUsoCentroResultadoEnum;
	}	

	public TreeNodeCustomizado getArvoreCentroResultado() {
		if (arvoreCentroResultado == null) {
			arvoreCentroResultado = new TreeNodeCustomizado();
		}
		return arvoreCentroResultado;
	}

	public void setArvoreCentroResultado(TreeNodeCustomizado arvoreCentroResultado) {
		this.arvoreCentroResultado = arvoreCentroResultado;
	}

	public List<CentroResultadoRestricaoUsoVO> getListaCentroResultadoRestricaoUsoVOs() {
		listaCentroResultadoRestricaoUsoVOs = Optional.ofNullable(listaCentroResultadoRestricaoUsoVOs).orElse(new ArrayList<>());
		return listaCentroResultadoRestricaoUsoVOs;
	}

	public void setListaCentroResultadoRestricaoUsoVOs(List<CentroResultadoRestricaoUsoVO> listaCentroResultadoRestricaoUsoVOs) {
		this.listaCentroResultadoRestricaoUsoVOs = listaCentroResultadoRestricaoUsoVOs;
	}
	
	public boolean isApresentarListaCentroResultadoRestricaoUso(){
		return getRestricaoUsoCentroResultadoEnum().isUsuarioEspecificos() || getRestricaoUsoCentroResultadoEnum().isUsuarioPerfilAcesso();
	}

	public Boolean getFiltrarCentroResultado() {
		if (filtrarCentroResultado == null) {
			filtrarCentroResultado = Boolean.FALSE;
		}

		return filtrarCentroResultado;
	}

	public void setFiltrarCentroResultado(Boolean filtrarCentroResultado) {
		this.filtrarCentroResultado = filtrarCentroResultado;
	}

	public String getTipoNivelCentroResultado() {
		if(tipoNivelCentroResultado == null) {
			tipoNivelCentroResultado = "";
		}
		return tipoNivelCentroResultado;
	}

	public void setTipoNivelCentroResultado(String tipoNivelCentroResultado) {
		this.tipoNivelCentroResultado = tipoNivelCentroResultado;
	}

	public Integer getCodigoOrigem() {
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public String getNomeOrigem() {
		if(nomeOrigem == null) {
			nomeOrigem = "";
		}
		return nomeOrigem;
	}

	public void setNomeOrigem(String nomeOrigem) {
		this.nomeOrigem = nomeOrigem;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if(unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurnoVO getTurnoVO() {
		if(turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}
	
	
	public String descricao_Apresentar;
	public String getDescricao_Apresentar() {
		if(descricao_Apresentar == null) {
			descricao_Apresentar = getIdentificadorCentroResultado()+" - "+getDescricao();
		}
		return descricao_Apresentar; 
	}
	
	public String getIdentificadorDescricao() {
		return getIdentificadorCentroResultado() + "-"+ getDescricao(); 
	}
	
}
