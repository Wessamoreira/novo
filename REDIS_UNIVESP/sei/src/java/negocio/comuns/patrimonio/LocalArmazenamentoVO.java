package negocio.comuns.patrimonio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;

public class LocalArmazenamentoVO extends SuperVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6145278171533816491L;

	private Integer codigo;
	private String localArmazenamento;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Boolean permiteReserva;	
	private Integer quantidadeDiasLimiteReserva;
	/**
	 * Mantém o local superior na qual o local que está sendo cadastro faz parte,
	 * Ex: Caso esteja sendo cadastrado o local Laboratório de Informática e este fica dentro do Bloco C
	 * então deve ser vinculado ao Local Superior o Bloco C, isto irá permitir montar a arvore dos locais.
	 */
	private LocalArmazenamentoVO localArmazenamentoSuperiorVO;
	private TreeNodeCustomizado arvoreLocalArmazenamento;
	
	List<PatrimonioUnidadeVO> patrimonioUnidadeVOs;
	//Transiente - Usando no momento de popular o grafico da tela de NOVAS Ocorrencias
	private List<OcorrenciaPatrimonioVO> listaOcorrencias;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getLocalArmazenamento() {
		if (localArmazenamento == null) {
			localArmazenamento = "";
		}
		return localArmazenamento;
	}

	public void setLocalArmazenamento(String localArmazenamento) {
		this.localArmazenamento = localArmazenamento;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public LocalArmazenamentoVO getLocalArmazenamentoSuperiorVO() {
		if (localArmazenamentoSuperiorVO == null) {
			localArmazenamentoSuperiorVO = new LocalArmazenamentoVO();
		}
		return localArmazenamentoSuperiorVO;
	}

	public void setLocalArmazenamentoSuperiorVO(LocalArmazenamentoVO localArmazenamentoSuperiorVO) {
		this.localArmazenamentoSuperiorVO = localArmazenamentoSuperiorVO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalArmazenamentoVO other = (LocalArmazenamentoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}

	public TreeNodeCustomizado getArvoreLocalArmazenamento() {
		
		return arvoreLocalArmazenamento;
	}

	public void setArvoreLocalArmazenamento(TreeNodeCustomizado arvoreLocalArmazenamento) {
		this.arvoreLocalArmazenamento = arvoreLocalArmazenamento;
	}
	
	public Boolean getIsPossuiLocalSuperior(){
		return getLocalArmazenamentoSuperiorVO().getCodigo() > 0;
	}
	
	
	/**
	 * @return the patrimonioUnidadeVOs
	 */
	public List<PatrimonioUnidadeVO> getPatrimonioUnidadeVOs() {
		if (patrimonioUnidadeVOs == null) {
			patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		}
		return patrimonioUnidadeVOs;
	}

	/**
	 * @param patrimonioUnidadeVOs the patrimonioUnidadeVOs to set
	 */
	public void setPatrimonioUnidadeVOs(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) {
		this.patrimonioUnidadeVOs = patrimonioUnidadeVOs;
	}
	
	public Integer getQuantidadePatrimonio(){
		return getPatrimonioUnidadeVOs().size();
	}
	public Integer getQuantidadeDiasLimiteReserva() {
		if(quantidadeDiasLimiteReserva == null){
			quantidadeDiasLimiteReserva = 0;
		}
		return quantidadeDiasLimiteReserva;
	}
	public void setQuantidadeDiasLimiteReserva(Integer quantidadeDiasLimiteReserva) {
		this.quantidadeDiasLimiteReserva = quantidadeDiasLimiteReserva;
	}
	
	public Boolean getPermiteReserva() {
		if (permiteReserva == null) {
			permiteReserva = false;
		}
		return permiteReserva;
	}

	public void setPermiteReserva(Boolean permiteReserva) {
		this.permiteReserva = permiteReserva;
	}
	public List<OcorrenciaPatrimonioVO> getListaOcorrencias() {
		if(listaOcorrencias == null){
			listaOcorrencias = new ArrayList<OcorrenciaPatrimonioVO>(0);
		}
		return listaOcorrencias;
	}

	public void setListaOcorrencias(List<OcorrenciaPatrimonioVO> listaOcorrencias) {
		this.listaOcorrencias = listaOcorrencias;
	}
}
