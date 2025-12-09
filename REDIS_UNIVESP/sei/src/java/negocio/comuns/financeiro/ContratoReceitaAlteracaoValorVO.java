package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class ContratoReceitaAlteracaoValorVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7267159318106353238L;
	private Integer codigo;
	private ContratosReceitasVO contratosReceitasVO;
	private Double valorAnterior;
	private Double valorNovo;
	private Date data;
	private UsuarioVO responsavel;
	private String contasAlteradas;	
	private String motivoAlteracao;	

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ContratosReceitasVO getContratosReceitasVO() {
		if(contratosReceitasVO == null){
			contratosReceitasVO = new ContratosReceitasVO();
		}
		return contratosReceitasVO;
	}

	public void setContratosReceitasVO(ContratosReceitasVO contratosReceitasVO) {
		this.contratosReceitasVO = contratosReceitasVO;
	}

	public Double getValorAnterior() {
		if(valorAnterior == null){
			valorAnterior = 0.0;
		}
		return valorAnterior;
	}

	public void setValorAnterior(Double valorAnterior) {
		this.valorAnterior = valorAnterior;
	}

	public Double getValorNovo() {
		if(valorNovo == null){
			valorNovo = 0.0;
		}
		return valorNovo;
	}

	public void setValorNovo(Double valorNovo) {
		this.valorNovo = valorNovo;
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

	public String getContasAlteradas() {
		if(contasAlteradas == null){
			contasAlteradas ="";
		}
		return contasAlteradas;
	}

	public void setContasAlteradas(String contasAlteradas) {
		this.contasAlteradas = contasAlteradas;
	}

	public String getMotivoAlteracao() {
		if(motivoAlteracao == null){
			motivoAlteracao = "";
		}
		return motivoAlteracao;
	}

	public void setMotivoAlteracao(String motivoAlteracao) {
		this.motivoAlteracao = motivoAlteracao;
	}

	
}
