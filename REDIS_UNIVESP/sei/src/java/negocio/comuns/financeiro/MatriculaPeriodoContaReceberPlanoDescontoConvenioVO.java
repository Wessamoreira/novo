package negocio.comuns.financeiro;

import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class MatriculaPeriodoContaReceberPlanoDescontoConvenioVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7282409934745404092L;
	private Integer codigo;
	private MatriculaPeriodoContaReceberLogVO matriculaPeriodoContaReceberLogVO;
	private PlanoDescontoVO planoDescontoVO;
	private Boolean utilizado;
	private String tipoItemPlanoFinanceiro; //"PD" - plano de desconto "CO" - convenio
	private ConvenioVO convenio;
	    /**
	     * Atributo utilizado para armazenar o valor de um convenio/plano desconto
	     * já calculado para uma determinada conta a receber. O mesmo é preenchido 
	     * com este valor, mesmo antes da conta a receber ser recebida. Isto é importante
	     * para os relatórios e cálculo de informações relativas à descontos no painel gestor.
	     */
	private Double valorUtilizadoRecebimento;
	private Double valorConvenioNaoRestituirAluno;
	
		public Integer getCodigo() {
			if(codigo == null){
				codigo = 0;
			}
			return codigo;
		}
		public void setCodigo(Integer codigo) {
			this.codigo = codigo;
		}
		public MatriculaPeriodoContaReceberLogVO getMatriculaPeriodoContaReceberLogVO() {
			if(matriculaPeriodoContaReceberLogVO == null){
				matriculaPeriodoContaReceberLogVO = new MatriculaPeriodoContaReceberLogVO();
			}
			return matriculaPeriodoContaReceberLogVO;
		}
		public void setMatriculaPeriodoContaReceberLogVO(MatriculaPeriodoContaReceberLogVO matriculaPeriodoContaReceberLogVO) {
			this.matriculaPeriodoContaReceberLogVO = matriculaPeriodoContaReceberLogVO;
		}
		public PlanoDescontoVO getPlanoDescontoVO() {
			if(planoDescontoVO == null){
				planoDescontoVO = new PlanoDescontoVO();
			}
			return planoDescontoVO;
		}
		public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
			this.planoDescontoVO = planoDescontoVO;
		}
		public Boolean getUtilizado() {
			if(utilizado == null){
				utilizado = true;
			}
			return utilizado;
		}
		public void setUtilizado(Boolean utilizado) {
			this.utilizado = utilizado;
		}
		public String getTipoItemPlanoFinanceiro() {
			if(tipoItemPlanoFinanceiro == null){
				tipoItemPlanoFinanceiro = "PD";
			}
			return tipoItemPlanoFinanceiro;
		}
		public void setTipoItemPlanoFinanceiro(String tipoItemPlanoFinanceiro) {
			this.tipoItemPlanoFinanceiro = tipoItemPlanoFinanceiro;
		}
		public ConvenioVO getConvenio() {
			if(convenio == null){
				convenio = new ConvenioVO();
			}
			return convenio;
		}
		public void setConvenio(ConvenioVO convenio) {
			this.convenio = convenio;
		}
		public Double getValorUtilizadoRecebimento() {
			if(valorUtilizadoRecebimento == null){
				valorUtilizadoRecebimento = 0.0;
			}
			return valorUtilizadoRecebimento;
		}
		public void setValorUtilizadoRecebimento(Double valorUtilizadoRecebimento) {
			this.valorUtilizadoRecebimento = valorUtilizadoRecebimento;
		}
	 
		public String descricaoDesconto;
		public String getDescricaoDesconto(){
			if(descricaoDesconto == null){
				if(getTipoItemPlanoFinanceiro().equals("PD")){
					descricaoDesconto = "Plano Desconto: "+getPlanoDescontoVO().getNome()+"(R$ "+Uteis.getDoubleFormatado(getValorUtilizadoRecebimento())+")";
				}else{
					descricaoDesconto = "Convênio: "+getConvenio().getDescricao()+"(R$ "+Uteis.getDoubleFormatado(getValorUtilizadoRecebimento())+")";
				}
			}
			return descricaoDesconto;
		}
		
		public Double getValorConvenioNaoRestituirAluno() {
			if(valorConvenioNaoRestituirAluno == null){
				valorConvenioNaoRestituirAluno = 0.0;
			}
			return valorConvenioNaoRestituirAluno;
		}
		
		public void setValorConvenioNaoRestituirAluno(Double valorConvenioNaoRestituirAluno) {
			this.valorConvenioNaoRestituirAluno = valorConvenioNaoRestituirAluno;
		}
		
		

}
