package relatorio.negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ProcessoSeletivoRedacaoRelVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer inscricao;
	private Integer qtdInscricoes;
	private SalaLocalAulaVO sala;
	private ItemProcSeletivoDataProvaVO dataProva;
	private String textoOrientacaoRodape;
	private List<ProcessoSeletivoRedacao_linhasRelVO> linhas;
	
    public JRDataSource getListaProcessoSeletivoRedacao_linhas() {
        JRDataSource jr = new JRBeanArrayDataSource(getLinhas().toArray());
        return jr;
    }
	
	public ProcessoSeletivoRedacaoRelVO(Integer qtdLinhas, Boolean ocultarLinha) {
		Integer totalLinhas = qtdLinhas;
		for (int x = 0; x < totalLinhas; x++) {
			ProcessoSeletivoRedacao_linhasRelVO linha = new ProcessoSeletivoRedacao_linhasRelVO();
			if (ocultarLinha) {
				linha.setNumero(0);
			} else {
				linha.setNumero(x+1);
			}
			getLinhas().add(linha);
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
	
	public List<ProcessoSeletivoRedacao_linhasRelVO> getLinhas() {
		if (linhas == null) {
			linhas = new ArrayList<ProcessoSeletivoRedacao_linhasRelVO>();
		}
		return linhas;
	}
	
	public void setLinhas(List<ProcessoSeletivoRedacao_linhasRelVO> linhas) {
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

	public String getTextoOrientacaoRodape() {
		if (textoOrientacaoRodape == null) {
			textoOrientacaoRodape = "";
		}
		return textoOrientacaoRodape;
	}

	public void setTextoOrientacaoRodape(String textoOrientacaoRodape) {
		this.textoOrientacaoRodape = textoOrientacaoRodape;
	}

	public Integer getInscricao() {
		return inscricao;
	}

	public void setInscricao(Integer inscricao) {
		this.inscricao = inscricao;
	}

}
