package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Victor Hugo de Paula Costa 23/03/2015
 *
 */
public class ParametrosMonitoramentoAvaliacaoOnlineVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String descricao;
	private List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> itemParametrosMonitoramentoAvaliacaoOnlineVOs;

	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> getItemParametrosMonitoramentoAvaliacaoOnlineVOs() {
		if (itemParametrosMonitoramentoAvaliacaoOnlineVOs == null) {
			itemParametrosMonitoramentoAvaliacaoOnlineVOs = new ArrayList<ItemParametrosMonitoramentoAvaliacaoOnlineVO>();
		}
		return itemParametrosMonitoramentoAvaliacaoOnlineVOs;
	}

	public void setItemParametrosMonitoramentoAvaliacaoOnlineVOs(List<ItemParametrosMonitoramentoAvaliacaoOnlineVO> itemParametrosMonitoramentoAvaliacaoOnlineVOs) {
		this.itemParametrosMonitoramentoAvaliacaoOnlineVOs = itemParametrosMonitoramentoAvaliacaoOnlineVOs;
	}
}
