package relatorio.negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ProcessoSeletivoAtaProvaRelVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer qtdInscricoes;
	private SalaLocalAulaVO sala;
	private ItemProcSeletivoDataProvaVO dataProva;
	private List<String> linhas;
	
    public JRDataSource getListaProcessoSeletivoAtaProva_linhas() {
        JRDataSource jr = new JRBeanArrayDataSource(getLinhas().toArray());
        return jr;
    }
	
	public ProcessoSeletivoAtaProvaRelVO() throws Exception {
		Integer totalLinhas = 35;
		for (int x = 0; x < totalLinhas; x++) {
			getLinhas().add("");
		}
	}
	
	public SalaLocalAulaVO getSala() {
		if (sala == null) {
			sala = new SalaLocalAulaVO();
		}
		return sala;
	}
	
	public void setSala(SalaLocalAulaVO sala) {
		this.sala = sala;
	}
	
	public ItemProcSeletivoDataProvaVO getDataProva() {
		if (dataProva == null) {
			dataProva = new ItemProcSeletivoDataProvaVO();
		}
		return dataProva;
	}
	
	public void setDataProva(ItemProcSeletivoDataProvaVO dataProva) {
		this.dataProva = dataProva;
	}
	
	public List<String> getLinhas() {
		if (linhas == null) {
			linhas = new ArrayList<String>();
		}
		return linhas;
	}
	
	public void setLinhas(List<String> linhas) {
		this.linhas = linhas;
	}

	public Integer getQtdInscricoes() {
		if (qtdInscricoes == null) {
			qtdInscricoes = 0; 
		}
		return qtdInscricoes;
	}

	public void setQtdInscricoes(Integer qtdInscricoes) {
		this.qtdInscricoes = qtdInscricoes;
	}

}
