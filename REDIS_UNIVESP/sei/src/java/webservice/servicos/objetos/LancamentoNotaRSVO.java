package webservice.servicos.objetos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.TurmaVO;

@XmlRootElement(name = "lancamentoNotaRSVO")
public class LancamentoNotaRSVO {
	
	private List<HistoricoVO> listaHistoricos;
	private ConfiguracaoAcademicoVO configuracaoAcademico;
	private Integer codigoPerfilAcesso;
	private TurmaVO turmaVO;
	private HistoricoVO historico;
	private Integer numeroNota;
	/**
	 * @return the listaHistoricos
	 */
	public List<HistoricoVO> getListaHistoricos() {
		if (listaHistoricos == null) {
			listaHistoricos = new ArrayList<>();
		}
		return listaHistoricos;
	}
	/**
	 * @param listaHistoricos the listaHistoricos to set
	 */
	public void setListaHistoricos(List<HistoricoVO> listaHistoricos) {
		this.listaHistoricos = listaHistoricos;
	}
	/**
	 * @return the configuracaoAcademico
	 */
	public ConfiguracaoAcademicoVO getConfiguracaoAcademico() {
		return configuracaoAcademico;
	}
	/**
	 * @param configuracaoAcademico the configuracaoAcademico to set
	 */
	public void setConfiguracaoAcademico(ConfiguracaoAcademicoVO configuracaoAcademico) {
		this.configuracaoAcademico = configuracaoAcademico;
	}
	/**
	 * @return the codigoPerfilAcesso
	 */
	public Integer getCodigoPerfilAcesso() {
		return codigoPerfilAcesso;
	}
	/**
	 * @param codigoPerfilAcesso the codigoPerfilAcesso to set
	 */
	public void setCodigoPerfilAcesso(Integer codigoPerfilAcesso) {
		this.codigoPerfilAcesso = codigoPerfilAcesso;
	}
	/**
	 * @return the turmaVO
	 */
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

	public HistoricoVO getHistorico() {
		if (historico == null) {
			historico = new HistoricoVO();
		}
		return historico;
	}
	/**
	 * @param historico the historico to set
	 */
	public void setHistorico(HistoricoVO historico) {
		this.historico = historico;
	}
	/**
	 * @return the numeroNota
	 */
	public Integer getNumeroNota() {
		if (numeroNota == null) {
			numeroNota = 0;
		}
		return numeroNota;
	}
	/**
	 * @param numeroNota the numeroNota to set
	 */
	public void setNumeroNota(Integer numeroNota) {
		this.numeroNota = numeroNota;
	}

	
	
	
	
}