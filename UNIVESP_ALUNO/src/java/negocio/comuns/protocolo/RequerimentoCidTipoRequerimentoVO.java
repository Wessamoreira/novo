package negocio.comuns.protocolo;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CidTipoRequerimentoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;

public class RequerimentoCidTipoRequerimentoVO extends SuperVO{
	private Integer codigo;
	private Integer cidtiporequerimento;
	private Integer requerimento;
	private Integer tiporequerimento;
	private List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs;
	
	
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	
	public Integer getCidtiporequerimento() {
		if(cidtiporequerimento == null) {
			cidtiporequerimento = 0;
		}
		return cidtiporequerimento;
	}
	
	
	public void setCidtiporequerimento(Integer cidtiporequerimento) {
		this.cidtiporequerimento = cidtiporequerimento;
	}
	
	
	public Integer getRequerimento() {
		if(requerimento == null) {
			requerimento = 0;
		}
		return requerimento;
	}
	
	
	public void setRequerimento(Integer requerimento) {
		this.requerimento = requerimento;
	}
	
	
	public Integer getTiporequerimento() {
		if(tiporequerimento == null) {
			tiporequerimento = 0;
		}
		return tiporequerimento;
	}
	
	
	public void setTiporequerimento(Integer tiporequerimento) {
		this.tiporequerimento = tiporequerimento;
	}


	public List<CidTipoRequerimentoVO> getCidTipoRequerimentoVOs() {
		if (cidTipoRequerimentoVOs == null) {
			cidTipoRequerimentoVOs = new ArrayList<CidTipoRequerimentoVO>();
		}
		return cidTipoRequerimentoVOs;
	}


	public void setCidTipoRequerimentoVOs(List<CidTipoRequerimentoVO> cidTipoRequerimentoVOs) {
		this.cidTipoRequerimentoVOs = cidTipoRequerimentoVOs;
	}


	
	
}
