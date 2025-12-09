package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.SituacaoMapaEquivalenciaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class MapaEquivalenciaMatrizCurricularVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3038807944950980150L;
	private Integer codigo;
	private CursoVO curso;
	private GradeCurricularVO gradeCurricular;
	private String descricao;
	private Date data;
	private UsuarioVO responsavel;
	private SituacaoMapaEquivalenciaEnum situacao;
	private List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs;
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CursoVO getCurso() {
		if(curso == null){
			curso = new CursoVO();
		}
		return curso;
	}
	public void setCurso(CursoVO curso) {
		this.curso = curso;
	}
	public GradeCurricularVO getGradeCurricular() {
		if(gradeCurricular == null){
			gradeCurricular = new GradeCurricularVO();
		}
		return gradeCurricular;
	}
	public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
		this.gradeCurricular = gradeCurricular;
	}
	public String getDescricao() {
		if(descricao == null){
			descricao = "";
		}
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getData() {
		if(data == null){
			data = new Date();
		}
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public UsuarioVO getResponsavel() {
		if(responsavel == null){
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}
	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}
        
        public String getSituacao_Apresentar() {
            return getSituacao().getValorApresentar();
        }
	public SituacaoMapaEquivalenciaEnum getSituacao() {
		if(situacao == null){
			situacao =  SituacaoMapaEquivalenciaEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}
	public void setSituacao(SituacaoMapaEquivalenciaEnum situacao) {
		this.situacao = situacao;
	}
	public List<MapaEquivalenciaDisciplinaVO> getMapaEquivalenciaDisciplinaVOs() {
		if(mapaEquivalenciaDisciplinaVOs == null){
			mapaEquivalenciaDisciplinaVOs = new ArrayList<MapaEquivalenciaDisciplinaVO>(0);
		}
		return mapaEquivalenciaDisciplinaVOs;
	}
	public void setMapaEquivalenciaDisciplinaVOs(List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs) {
		this.mapaEquivalenciaDisciplinaVOs = mapaEquivalenciaDisciplinaVOs;
	}
	
	public Boolean getSituacaoEmConstrucao(){
		return getSituacao().equals(SituacaoMapaEquivalenciaEnum.EM_CONSTRUCAO);
	}
	public Boolean getSituacaoAtivo(){
		return getSituacao().equals(SituacaoMapaEquivalenciaEnum.ATIVO);
	}
	public Boolean getSituacaoInativo(){
		return getSituacao().equals(SituacaoMapaEquivalenciaEnum.INATIVO);
	}
	
}
