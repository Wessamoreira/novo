package negocio.comuns.protocolo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.enumeradores.SituacaoRequerimentoDisciplinasAproveitadasEnum;
import negocio.comuns.utilitarias.Uteis;

public class RequerimentoDisciplinaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -788702800252619669L;
	private Integer codigo;
	private RequerimentoVO requerimento;
	private DisciplinaVO disciplina;
	private String variavelNota;
	private String tituloNota;
	private SituacaoRequerimentoDisciplinasAproveitadasEnum situacao;
	private Date dataDeferimentoIndeferimento;
	private UsuarioVO usuarioDeferimentoIndeferimento;
	private String motivoIndeferimento;
	private List<SelectItem> listaSelectItemNota;
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public RequerimentoVO getRequerimento() {
		if(requerimento == null) {
			requerimento =  new RequerimentoVO();
		}
		return requerimento;
	}
	public void setRequerimento(RequerimentoVO requerimento) {
		this.requerimento = requerimento;
	}
	public DisciplinaVO getDisciplina() {
		if(disciplina == null) {
			disciplina =  new DisciplinaVO();
		}
		return disciplina;
	}
	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}
	public String getVariavelNota() {
		return variavelNota;
	}
	public void setVariavelNota(String variavelNota) {
		this.variavelNota = variavelNota;
	}
	public SituacaoRequerimentoDisciplinasAproveitadasEnum getSituacao() {
		if(situacao == null) {
			situacao =  SituacaoRequerimentoDisciplinasAproveitadasEnum.AGUARDANDO_ANALISE;
		}
		return situacao;
	}
	public void setSituacao(SituacaoRequerimentoDisciplinasAproveitadasEnum situacao) {
		this.situacao = situacao;
	}
	@Override
	public int hashCode() {
		return Objects.hash(disciplina);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequerimentoDisciplinaVO other = (RequerimentoDisciplinaVO) obj;
		return Objects.equals(disciplina, other.disciplina);
	}
	
	public Date getDataDeferimentoIndeferimento() {
		return dataDeferimentoIndeferimento;
	}
	public void setDataDeferimentoIndeferimento(Date dataDeferimentoIndeferimento) {
		this.dataDeferimentoIndeferimento = dataDeferimentoIndeferimento;
	}
	public UsuarioVO getUsuarioDeferimentoIndeferimento() {
		if(usuarioDeferimentoIndeferimento == null) {
			usuarioDeferimentoIndeferimento =  new UsuarioVO();
		}
		return usuarioDeferimentoIndeferimento;
	}
	public void setUsuarioDeferimentoIndeferimento(UsuarioVO usuarioDeferimentoIndeferimento) {
		this.usuarioDeferimentoIndeferimento = usuarioDeferimentoIndeferimento;
	}
	public String getMotivoIndeferimento() {
		if(motivoIndeferimento == null) {
			motivoIndeferimento = "";
		}
		return motivoIndeferimento;
	}
	public void setMotivoIndeferimento(String motivoIndeferimento) {
		this.motivoIndeferimento = motivoIndeferimento;
	}
	
	public List<SelectItem> getListaSelectItemNota() {
		if(listaSelectItemNota == null){
			listaSelectItemNota =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemNota;
	}
	public void setListaSelectItemNota(List<SelectItem> listaSelectItemNota) {
		this.listaSelectItemNota = listaSelectItemNota;
	}
	public String getTituloNota() {
		if(tituloNota == null) {
			tituloNota =  "";
		}
		return tituloNota;
	}
	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}

	public boolean isSituacaoRequerimentoDisciplinaDeferido() {
		return Uteis.isAtributoPreenchido(getSituacao()) && getSituacao().isDeferido();
	}

	public boolean isSituacaoRequerimentoDisciplinaIndeferido() {
		return Uteis.isAtributoPreenchido(getSituacao()) && getSituacao().isIndeferido();
	}
}
