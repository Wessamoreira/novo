package negocio.comuns.recursoshumanos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class ProgressaoSalarialVO extends SuperVO {

	private static final long serialVersionUID = -828242478759607654L;
	
	private Integer codigo;
	private String descricao;
	
	private List<ProgressaoSalarialItemVO> progressaoSalarialItens;
	
	public enum EnumCampoConsultaProgressaoSalarial {
		DESCRICAO
	}
	
	public Integer getCodigo() {
		if (codigo == null)
			codigo = 0;
		return codigo;
	}
	public String getDescricao() {
		if (descricao == null)
			descricao = "";
		return descricao;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public List<ProgressaoSalarialItemVO> getProgressaoSalarialItens() {
		if (progressaoSalarialItens == null)
			progressaoSalarialItens = new ArrayList<>();
		return progressaoSalarialItens;
	}
	public void setProgressaoSalarialItens(List<ProgressaoSalarialItemVO> progressaoSalarialItens) {
		this.progressaoSalarialItens = progressaoSalarialItens;
	}
	
    public void excluirProgressaoItemVO(ProgressaoSalarialItemVO progressaoSalarialItemVO) throws Exception {
        int index = 0;
        Iterator<ProgressaoSalarialItemVO> i = getProgressaoSalarialItens().iterator();
        while (i.hasNext()) {
        	ProgressaoSalarialItemVO objExistente = (ProgressaoSalarialItemVO) i.next();
            if (objExistente.getNivelSalarialVO().getCodigo().equals(progressaoSalarialItemVO.getNivelSalarialVO().getCodigo()) &&
            		objExistente.getFaixaSalarialVO().getCodigo().equals(progressaoSalarialItemVO.getFaixaSalarialVO().getCodigo())) {
                getProgressaoSalarialItens().remove(index);
                return;
            }
            index++;
        }
    }
}