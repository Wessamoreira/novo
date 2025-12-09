package webservice.servicos.objetos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.InteracaoRequerimentoHistoricoVO;
import negocio.comuns.academico.MaterialRequerimentoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;

@XmlRootElement(name = "requerimentoTurmaReposicaoRSVO")
public class RequerimentoTurmaReposicaoRSVO {
	
private RequerimentoVO requerimentoVO;
private TurmaVO turmaVO;
private String tipoArquivo;
private RequerimentoHistoricoVO requerimentoHistoricoVO;
private String interacao;
private InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistorico;
private MaterialRequerimentoVO materialRequerimento;

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	/**
	 * @param turmaVO the turmaVO to set
	 */
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	
	public RequerimentoVO getRequerimentoVO() {
		if (requerimentoVO == null) {
			requerimentoVO = new RequerimentoVO();
		}
		return requerimentoVO;
	}
	
	public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
		this.requerimentoVO = requerimentoVO;
	}
	
	public String getTipoArquivo() {
		if (tipoArquivo == null) {
			tipoArquivo = "";
		}
		return tipoArquivo;
	}
	
	public void setTipoArquivo(String tipoArquivo) {
		this.tipoArquivo = tipoArquivo;
	}
	
	public RequerimentoHistoricoVO getRequerimentoHistoricoVO() {
		if (requerimentoHistoricoVO == null) {
			requerimentoHistoricoVO = new RequerimentoHistoricoVO();
		}
		return requerimentoHistoricoVO;
	}
	
	public void setRequerimentoHistoricoVO(RequerimentoHistoricoVO requerimentoHistoricoVO) {
		this.requerimentoHistoricoVO = requerimentoHistoricoVO;
	}
	
	public String getInteracao() {
		if (interacao == null) {
			interacao = "";
		}
		return interacao;
	}
	
	public void setInteracao(String interacao) {
		this.interacao = interacao;
	}
	
	public InteracaoRequerimentoHistoricoVO getInteracaoRequerimentoHistorico() {
		if (interacaoRequerimentoHistorico == null) {
			interacaoRequerimentoHistorico = new InteracaoRequerimentoHistoricoVO();
		}
		return interacaoRequerimentoHistorico;
	}
	
	public void setInteracaoRequerimentoHistorico(InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistorico) {
		this.interacaoRequerimentoHistorico = interacaoRequerimentoHistorico;
	}
	
	public MaterialRequerimentoVO getMaterialRequerimento() {
		if (materialRequerimento == null) {
			materialRequerimento = new MaterialRequerimentoVO();
		}
		return materialRequerimento;
	}
	
	public void setMaterialRequerimento(MaterialRequerimentoVO materialRequerimento) {
		this.materialRequerimento = materialRequerimento;
	}
	
	
	
	

}