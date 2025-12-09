package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.enumeradores.OperacaoMatriculaPeriodoContaLogEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;

public class MatriculaPeriodoContaReceberLogVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2842979895564708300L;
	private Integer codigo;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private String parcelaAntiga;
	private String parcelaNova;
	private Date dataVencimentoAntiga;
	private Date dataVencimentoNova;
	private Double valorBaseAntiga;
	private Double valorBaseNova;
	private Double valorAntiga;
	private Double valorNova;
	private Double valorCusteadoAntiga;
	private Double valorCusteadoNova;
	private Double valorDescontoAlunoAntiga;
	private Double valorDescontoAlunoNova;
	private Double valorDescontoInstituicaoAntiga;
	private Double valorDescontoInstituicaoNova;
	private Double valorDescontoConvenioAntiga;
	private Double valorDescontoConvenioNova;
	private Double valorDescontoRecebimentoAntiga;
	private Double valorDescontoRecebimentoNova;
	private Double valorDescontoRateioAntiga;
	private Double valorDescontoRateioNova;
	private Double valorReceberAntiga;
	private Double valorReceberNova;
	private Double valorRecebidoAntiga;
	private Double valorRecebidoNova;
	private Double acrescimoAntiga;
	private Double acrescimoNova;
	private Double juroAntiga;
	private Double juroNova;
	private Double multaAntiga;
	private Double multaNova;
	private Double valorDescontoProgressivoAntiga;
	private Double valorDescontoProgressivoNova;
	private DescontoProgressivoVO descontoProgressivoAntigaVO;
	private DescontoProgressivoVO descontoProgressivoNovaVO;
	private SituacaoContaReceber situacaoAntiga;
	private SituacaoContaReceber situacaoNova;
	private Double diferencaParcela;
	private List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberConvenioCusteadoAntigaVOs;
	private List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberConvenioCusteadoNovaVOs;
	private List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs;
	private List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs;
	private List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs;
	private Double valorConvenioNaoRestituirAluno;
	private Boolean valorCalculadoComDescontoAplicado;	
	private OperacaoMatriculaPeriodoContaLogEnum operacaoMatriculaPeriodoContaLog;
	private Double valorParcelaRateioDesconsiderado;
	private boolean liberadoDiferencaValorRateio=false;
	private boolean contaEditadaManualmente=false;
	private UsuarioVO responsavelLiberadoDiferencaValorRateio;
	
	
	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null){
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}
	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}
	public String getParcelaAntiga() {
		if(parcelaAntiga == null){
			parcelaAntiga = "";
		}
		return parcelaAntiga;
	}
	public void setParcelaAntiga(String parcelaAntiga) {
		this.parcelaAntiga = parcelaAntiga;
	}
	public String getParcelaNova() {
		if(parcelaNova == null){
			parcelaNova = "";
		}
		return parcelaNova;
	}
	public void setParcelaNova(String parcelaNova) {
		this.parcelaNova = parcelaNova;
	}
	public Date getDataVencimentoAntiga() {
		if(dataVencimentoAntiga == null){
			dataVencimentoAntiga = new Date();
		}
		return dataVencimentoAntiga;
	}
	public void setDataVencimentoAntiga(Date dataVencimentoAntiga) {
		this.dataVencimentoAntiga = dataVencimentoAntiga;
	}
	public Date getDataVencimentoNova() {
		if(dataVencimentoNova == null){
			dataVencimentoNova = new Date();
		}
		return dataVencimentoNova;
	}
	public void setDataVencimentoNova(Date dataVencimentoNova) {
		this.dataVencimentoNova = dataVencimentoNova;
	}
	public Double getValorBaseAntiga() {
		if(valorBaseAntiga == null){
			valorBaseAntiga = 0.0;
		}
		return valorBaseAntiga;
	}
	public void setValorBaseAntiga(Double valorBaseAntiga) {
		this.valorBaseAntiga = valorBaseAntiga;
	}
	public Double getValorBaseNova() {
		if(valorBaseNova == null){
			valorBaseNova = 0.0;
		}
		return valorBaseNova;
	}
	public void setValorBaseNova(Double valorBaseNova) {
		this.valorBaseNova = valorBaseNova;
	}
	public Double getValorAntiga() {
		if(valorAntiga == null){
			valorAntiga = 0.0;
		}
		return valorAntiga;
	}
	public void setValorAntiga(Double valorAntiga) {
		this.valorAntiga = valorAntiga;
	}
	public Double getValorNova() {
		if(valorNova == null){
			valorNova = 0.0;
		}
		return valorNova;
	}
	public void setValorNova(Double valorNova) {
		this.valorNova = valorNova;
	}
	public Double getValorCusteadoAntiga() {
		if(valorCusteadoAntiga == null){
			valorCusteadoAntiga = 0.0;
		}
		return valorCusteadoAntiga;
	}
	public void setValorCusteadoAntiga(Double valorCusteadoAntiga) {
		this.valorCusteadoAntiga = valorCusteadoAntiga;
	}
	public Double getValorCusteadoNova() {
		if(valorCusteadoNova == null){
			valorCusteadoNova = 0.0;
		}
		return valorCusteadoNova;
	}
	public void setValorCusteadoNova(Double valorCusteadoNova) {
		this.valorCusteadoNova = valorCusteadoNova;
	}
	public Double getValorDescontoAlunoAntiga() {
		if(valorDescontoAlunoAntiga == null){
			valorDescontoAlunoAntiga = 0.0;
		}
		return valorDescontoAlunoAntiga;
	}
	public void setValorDescontoAlunoAntiga(Double valorDescontoAlunoAntiga) {
		this.valorDescontoAlunoAntiga = valorDescontoAlunoAntiga;
	}
	public Double getValorDescontoAlunoNova() {
		if(valorDescontoAlunoNova == null){
			valorDescontoAlunoNova = 0.0;
		}
		return valorDescontoAlunoNova;
	}
	public void setValorDescontoAlunoNova(Double valorDescontoAlunoNova) {
		this.valorDescontoAlunoNova = valorDescontoAlunoNova;
	}
	public Double getValorDescontoInstituicaoAntiga() {
		if(valorDescontoInstituicaoAntiga == null){
			valorDescontoInstituicaoAntiga = 0.0;
		}
		return valorDescontoInstituicaoAntiga;
	}
	public void setValorDescontoInstituicaoAntiga(Double valorDescontoInstituicaoAntiga) {
		this.valorDescontoInstituicaoAntiga = valorDescontoInstituicaoAntiga;
	}
	public Double getValorDescontoInstituicaoNova() {
		if(valorDescontoInstituicaoNova == null){
			valorDescontoInstituicaoNova = 0.0;
		}
		return valorDescontoInstituicaoNova;
	}
	public void setValorDescontoInstituicaoNova(Double valorDescontoInstituicaoNova) {
		this.valorDescontoInstituicaoNova = valorDescontoInstituicaoNova;
	}
	public Double getValorDescontoConvenioAntiga() {
		if(valorDescontoConvenioAntiga == null){
			valorDescontoConvenioAntiga = 0.0;
		}
		return valorDescontoConvenioAntiga;
	}
	public void setValorDescontoConvenioAntiga(Double valorDescontoConvenioAntiga) {
		this.valorDescontoConvenioAntiga = valorDescontoConvenioAntiga;
	}
	public Double getValorDescontoConvenioNova() {
		if(valorDescontoConvenioNova == null){
			valorDescontoConvenioNova = 0.0;
		}
		return valorDescontoConvenioNova;
	}
	public void setValorDescontoConvenioNova(Double valorDescontoConvenioNova) {
		this.valorDescontoConvenioNova = valorDescontoConvenioNova;
	}
	public Double getValorDescontoRecebimentoAntiga() {
		if(valorDescontoRecebimentoAntiga == null){
			valorDescontoRecebimentoAntiga = 0.0;
		}
		return valorDescontoRecebimentoAntiga;
	}
	public void setValorDescontoRecebimentoAntiga(Double valorDescontoRecebimentoAntiga) {
		this.valorDescontoRecebimentoAntiga = valorDescontoRecebimentoAntiga;
	}
	public Double getValorDescontoRecebimentoNova() {
		if(valorDescontoRecebimentoNova == null){
			valorDescontoRecebimentoNova = 0.0;
		}
		return valorDescontoRecebimentoNova;
	}
	public void setValorDescontoRecebimentoNova(Double valorDescontoRecebimentoNova) {
		this.valorDescontoRecebimentoNova = valorDescontoRecebimentoNova;
	}
	public Double getValorDescontoRateioAntiga() {
		if(valorDescontoRateioAntiga == null){
			valorDescontoRateioAntiga = 0.0;
		}
		return valorDescontoRateioAntiga;
	}
	public void setValorDescontoRateioAntiga(Double valorDescontoRateioAntiga) {
		this.valorDescontoRateioAntiga = valorDescontoRateioAntiga;
	}
	public Double getValorDescontoRateioNova() {
		if(valorDescontoRateioNova == null){
			valorDescontoRateioNova = 0.0;
		}
		return valorDescontoRateioNova;
	}
	public void setValorDescontoRateioNova(Double valorDescontoRateioNova) {
		this.valorDescontoRateioNova = valorDescontoRateioNova;
	}
	public Double getValorReceberAntiga() {
		if(valorReceberAntiga == null){
			valorReceberAntiga = 0.0;
		}
		return valorReceberAntiga;
	}
	public void setValorReceberAntiga(Double valorReceberAntiga) {
		this.valorReceberAntiga = valorReceberAntiga;
	}
	public Double getValorReceberNova() {
		if(valorReceberNova == null){
			valorReceberNova = 0.0;
		}
		return valorReceberNova;
	}
	public void setValorReceberNova(Double valorReceberNova) {
		this.valorReceberNova = valorReceberNova;
	}
	public Double getValorRecebidoAntiga() {
		if(valorRecebidoAntiga == null){
			valorRecebidoAntiga = 0.0;
		}
		return valorRecebidoAntiga;
	}
	public void setValorRecebidoAntiga(Double valorRecebidoAntiga) {
		this.valorRecebidoAntiga = valorRecebidoAntiga;
	}
	public Double getValorRecebidoNova() {
		if(valorRecebidoNova == null){
			valorRecebidoNova = 0.0;
		}
		return valorRecebidoNova;
	}
	public void setValorRecebidoNova(Double valorRecebidoNova) {
		this.valorRecebidoNova = valorRecebidoNova;
	}
	public Double getAcrescimoAntiga() {
		if(acrescimoAntiga == null){
			acrescimoAntiga = 0.0;
		}
		return acrescimoAntiga;
	}
	public void setAcrescimoAntiga(Double acrescimoAntiga) {
		this.acrescimoAntiga = acrescimoAntiga;
	}
	public Double getAcrescimoNova() {
		if(acrescimoNova == null){
			acrescimoNova = 0.0;
		}
		return acrescimoNova;
	}
	public void setAcrescimoNova(Double acrescimoNova) {
		this.acrescimoNova = acrescimoNova;
	}
	public Double getJuroAntiga() {
		if(juroAntiga == null){
			juroAntiga = 0.0;
		}
		return juroAntiga;
	}
	public void setJuroAntiga(Double juroAntiga) {
		this.juroAntiga = juroAntiga;
	}
	public Double getJuroNova() {
		if(juroNova == null){
			juroNova = 0.0;
		}
		return juroNova;
	}
	public void setJuroNova(Double juroNova) {
		this.juroNova = juroNova;
	}
	public Double getMultaAntiga() {
		if(multaAntiga == null){
			multaAntiga = 0.0;
		}
		return multaAntiga;
	}
	public void setMultaAntiga(Double multaAntiga) {
		this.multaAntiga = multaAntiga;
	}
	public Double getMultaNova() {
		if(multaNova == null){
			multaNova = 0.0;
		}
		return multaNova;
	}
	public void setMultaNova(Double multaNova) {
		this.multaNova = multaNova;
	}
	public Double getValorDescontoProgressivoAntiga() {
		if(valorDescontoProgressivoAntiga == null){
			valorDescontoProgressivoAntiga = 0.0;
		}
		return valorDescontoProgressivoAntiga;
	}
	public void setValorDescontoProgressivoAntiga(Double valorDescontoProgressivoAntiga) {
		this.valorDescontoProgressivoAntiga = valorDescontoProgressivoAntiga;
	}
	public Double getValorDescontoProgressivoNova() {
		if(valorDescontoProgressivoNova == null){
			valorDescontoProgressivoNova = 0.0;
		}
		return valorDescontoProgressivoNova;
	}
	public void setValorDescontoProgressivoNova(Double valorDescontoProgressivoNova) {
		this.valorDescontoProgressivoNova = valorDescontoProgressivoNova;
	}
	public DescontoProgressivoVO getDescontoProgressivoAntigaVO() {
		if(descontoProgressivoAntigaVO == null){
			descontoProgressivoAntigaVO = new DescontoProgressivoVO();
		}
		return descontoProgressivoAntigaVO;
	}
	public void setDescontoProgressivoAntigaVO(DescontoProgressivoVO descontoProgressivoAntigaVO) {
		this.descontoProgressivoAntigaVO = descontoProgressivoAntigaVO;
	}
	public DescontoProgressivoVO getDescontoProgressivoNovaVO() {
		if(descontoProgressivoNovaVO == null){
			descontoProgressivoNovaVO = new DescontoProgressivoVO();
		}
		return descontoProgressivoNovaVO;
	}
	public void setDescontoProgressivoNovaVO(DescontoProgressivoVO descontoProgressivoNovaVO) {
		this.descontoProgressivoNovaVO = descontoProgressivoNovaVO;
	}
	public SituacaoContaReceber getSituacaoAntiga() {
		if(situacaoAntiga == null){
			situacaoAntiga =  SituacaoContaReceber.A_RECEBER;
		}
		return situacaoAntiga;
	}
	public void setSituacaoAntiga(SituacaoContaReceber situacaoAntiga) {
		this.situacaoAntiga = situacaoAntiga;
	}
	public SituacaoContaReceber getSituacaoNova() {
		if(situacaoNova == null){
			situacaoNova =  SituacaoContaReceber.A_RECEBER;
		}
		return situacaoNova;
	}
	public void setSituacaoNova(SituacaoContaReceber situacaoNova) {
		this.situacaoNova = situacaoNova;
	}
	public List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> getMatriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs() {
		if(matriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs == null){
			matriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs =  new ArrayList<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO>(0);
		}
		return matriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs;
	}
	public void setMatriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs(
			List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs) {
		this.matriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs = matriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs;
	}
	
	public Double getDiferencaParcela() {
		if(diferencaParcela == null){
			diferencaParcela = 0.0;
		}
		return diferencaParcela;
	}
	
	public void setDiferencaParcela(Double diferencaParcela) {
		this.diferencaParcela = diferencaParcela;
	}

	public String descricaoDescontoAntiga;
	public String getDescricaoDescontoAntiga(){
		if(descricaoDescontoAntiga == null){
			StringBuilder desc = new StringBuilder();
			if(getValorDescontoAlunoAntiga()>0.0){
				desc.append("Aluno: ").append(Uteis.getDoubleFormatado(getValorDescontoAlunoAntiga()));
			}			
			if(getValorDescontoProgressivoAntiga()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Prog: ").append(Uteis.getDoubleFormatado(getValorDescontoProgressivoAntiga()));
			}
			if(getValorDescontoInstituicaoAntiga()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Inst: ").append(Uteis.getDoubleFormatado(getValorDescontoInstituicaoAntiga()));
			}
			if(getValorDescontoConvenioAntiga()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Conv: ").append(Uteis.getDoubleFormatado(getValorDescontoConvenioAntiga()));
			}
			if(getValorDescontoRateioAntiga()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Rat: ").append(Uteis.getDoubleFormatado(getValorDescontoRateioAntiga()));
			}			
			if(desc.length() > 0){
				descricaoDescontoAntiga = "("+desc.toString()+") = "+Uteis.getDoubleFormatado(getTotalDescontoAntiga());
			}else{
				descricaoDescontoAntiga = "";
			}
		}
		return descricaoDescontoAntiga;
	}

	private Double totalDescontoAntiga;
	public Double getTotalDescontoAntiga() {
		if(totalDescontoAntiga == null){
			totalDescontoAntiga = getValorDescontoAlunoAntiga()+getValorDescontoConvenioAntiga()+getValorDescontoInstituicaoAntiga()+getValorDescontoProgressivoAntiga()+getValorDescontoRateioAntiga();
		}
		return totalDescontoAntiga;
	}
	public void setTotalDescontoAntiga(Double totalDescontoAntiga) {
		this.totalDescontoAntiga = totalDescontoAntiga;
	}
	
	private Double totalDescontoNova;
	public Double getTotalDescontoNova() {
		if(totalDescontoNova == null){
			totalDescontoNova = getValorDescontoAlunoNova()+getValorDescontoConvenioNova()+getValorDescontoInstituicaoNova()+getValorDescontoProgressivoNova()+getValorDescontoRateioNova();
		}
		return totalDescontoNova;
	}
	public void setTotalDescontoNova(Double totalDescontoNova) {
		this.totalDescontoNova = totalDescontoNova;
	}

	public String descricaoDescontoNova;
	public String getDescricaoDescontoNova(){
		if(descricaoDescontoNova == null){			
			StringBuilder desc = new StringBuilder();
			if(getValorDescontoAlunoNova()>0.0){
				desc.append("Aluno: ").append(Uteis.getDoubleFormatado(getValorDescontoAlunoNova()));
			}			
			if(getValorDescontoProgressivoNova()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Prog: ").append(Uteis.getDoubleFormatado(getValorDescontoProgressivoNova()));
			}
			if(getValorDescontoInstituicaoNova()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Inst: ").append(Uteis.getDoubleFormatado(getValorDescontoInstituicaoNova()));
			}
			if(getValorDescontoConvenioNova()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Conv: ").append(Uteis.getDoubleFormatado(getValorDescontoConvenioNova()));
			}
			if(getValorDescontoRateioNova()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Rat: ").append(Uteis.getDoubleFormatado(getValorDescontoRateioNova()));
			}
			if(getValorDescontoRecebimentoNova()>0.0){
				if(desc.length() > 0){
					desc.append(", ");
				}
				desc.append("Rec: ").append(Uteis.getDoubleFormatado(getValorDescontoRecebimentoNova()));
			}
			if(desc.length() > 0){
				descricaoDescontoNova = "("+desc.toString()+") = "+Uteis.getDoubleFormatado(getTotalDescontoNova());
			}else{
				descricaoDescontoNova = "";
			}
		}
		return descricaoDescontoNova;
	}
	
	public List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> getMatriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs() {
		if(matriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs == null){
			matriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs  = new ArrayList<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO>(0);
		}
		return matriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs;
	}
	
	public void setMatriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs(
			List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs) {
		this.matriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs = matriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs;
	}
	
	private String descricaoDescontoIntConvContaNova;
	
	public String getDescricaoDescontoIntConvContaNova() {
		if(descricaoDescontoIntConvContaNova == null){
			descricaoDescontoIntConvContaNova = "";
			for(MatriculaPeriodoContaReceberPlanoDescontoConvenioVO desc: getMatriculaPeriodoContaReceberPlanoDescontoConvenioNovaVOs()){		
				descricaoDescontoIntConvContaNova += "<div>"+desc.getDescricaoDesconto()+"</div>";
			}
		}
		return descricaoDescontoIntConvContaNova;
	}
	public void setDescricaoDescontoIntConvContaNova(String descricaoDescontoIntConvContaNova) {
		this.descricaoDescontoIntConvContaNova = descricaoDescontoIntConvContaNova;
	}
	
	private String descricaoDescontoIntConvContaAntiga;
	
	public String getDescricaoDescontoIntConvContaAntiga() {
		if(descricaoDescontoIntConvContaAntiga == null){
			descricaoDescontoIntConvContaAntiga = "";
			for(MatriculaPeriodoContaReceberPlanoDescontoConvenioVO desc: getMatriculaPeriodoContaReceberPlanoDescontoConvenioAntigaVOs()){				
				descricaoDescontoIntConvContaAntiga += "<div>"+desc.getDescricaoDesconto()+"</div>";
			}
		}
		return descricaoDescontoIntConvContaAntiga;
	}
	
	private String descricaoDescontoConvenioCusteioContaAntiga;
	
	public String getDescricaoDescontoConvenioCusteioContaAntiga() {
		if(descricaoDescontoConvenioCusteioContaAntiga == null){
			descricaoDescontoConvenioCusteioContaAntiga = "";
			for(MatriculaPeriodoContaReceberPlanoDescontoConvenioVO desc: getMatriculaPeriodoContaReceberConvenioCusteadoAntigaVOs()){				
				descricaoDescontoConvenioCusteioContaAntiga += "<div>"+desc.getDescricaoDesconto()+"</div>";
			}
		}
		return descricaoDescontoConvenioCusteioContaAntiga;
	}
	
	private String descricaoDescontoConvenioCusteioContaNova;
	
	public String getDescricaoDescontoConvenioCusteioContaNova() {
		if(descricaoDescontoConvenioCusteioContaNova == null){
			descricaoDescontoConvenioCusteioContaNova = "";
			for(MatriculaPeriodoContaReceberPlanoDescontoConvenioVO desc: getMatriculaPeriodoContaReceberConvenioCusteadoNovaVOs()){				
				descricaoDescontoConvenioCusteioContaNova += "<div>"+desc.getDescricaoDesconto()+"</div>";
			}
		}
		return descricaoDescontoConvenioCusteioContaNova;
	}
	
	public void setDescricaoDescontoIntConvContaAntiga(String descricaoDescontoIntConvContaAntiga) {
		this.descricaoDescontoIntConvContaAntiga = descricaoDescontoIntConvContaAntiga;
	}
	
	public List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> getMatriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs() {
		if(matriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs == null){
			matriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs = new ArrayList<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO>(0);
		}
		return matriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs;
	}
	
	public void setMatriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs(
			List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs) {
		this.matriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs = matriculaPeriodoContaReceberPlanoConvenioNaoRestituitAlunoVOs;
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
	
	public List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> getMatriculaPeriodoContaReceberConvenioCusteadoAntigaVOs() {
		if(matriculaPeriodoContaReceberConvenioCusteadoAntigaVOs == null){
			matriculaPeriodoContaReceberConvenioCusteadoAntigaVOs = new ArrayList<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO>(0);
		}
		return matriculaPeriodoContaReceberConvenioCusteadoAntigaVOs;
	}
	
	public void setMatriculaPeriodoContaReceberConvenioCusteadoAntigaVOs(
			List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberConvenioCusteadoAntigaVOs) {
		this.matriculaPeriodoContaReceberConvenioCusteadoAntigaVOs = matriculaPeriodoContaReceberConvenioCusteadoAntigaVOs;
	}
	
	public List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> getMatriculaPeriodoContaReceberConvenioCusteadoNovaVOs() {
		if(matriculaPeriodoContaReceberConvenioCusteadoNovaVOs == null){
			matriculaPeriodoContaReceberConvenioCusteadoNovaVOs = new ArrayList<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO>(0);
		}
		return matriculaPeriodoContaReceberConvenioCusteadoNovaVOs;
	}
	
	public void setMatriculaPeriodoContaReceberConvenioCusteadoNovaVOs(
			List<MatriculaPeriodoContaReceberPlanoDescontoConvenioVO> matriculaPeriodoContaReceberConvenioCusteadoNovaVOs) {
		this.matriculaPeriodoContaReceberConvenioCusteadoNovaVOs = matriculaPeriodoContaReceberConvenioCusteadoNovaVOs;
	}
	public Boolean getValorCalculadoComDescontoAplicado() {
		if(valorCalculadoComDescontoAplicado == null){
			valorCalculadoComDescontoAplicado = false;
		}
		return valorCalculadoComDescontoAplicado;
	}
	public void setValorCalculadoComDescontoAplicado(Boolean valorCalculadoComDescontoAplicado) {
		this.valorCalculadoComDescontoAplicado = valorCalculadoComDescontoAplicado;
	}
	

	public OperacaoMatriculaPeriodoContaLogEnum getOperacaoMatriculaPeriodoContaLog() {
		if(operacaoMatriculaPeriodoContaLog == null){
			operacaoMatriculaPeriodoContaLog = OperacaoMatriculaPeriodoContaLogEnum.MANTIDA;
		}
		return operacaoMatriculaPeriodoContaLog;
	}
	
	public void setOperacaoMatriculaPeriodoContaLog(OperacaoMatriculaPeriodoContaLogEnum operacaoMatriculaPeriodoContaLog) {
		this.operacaoMatriculaPeriodoContaLog = operacaoMatriculaPeriodoContaLog;
	}
	
	public boolean isContaEditadaManualmente() {
		return contaEditadaManualmente;
	}

	public void setContaEditadaManualmente(boolean contaEditadaManualmente) {
		this.contaEditadaManualmente = contaEditadaManualmente;
	}
	
	public boolean isLiberadoDiferencaValorRateio() {
		return liberadoDiferencaValorRateio;
	}
	
	public void setLiberadoDiferencaValorRateio(boolean liberadoDiferencaValorRateio) {
		this.liberadoDiferencaValorRateio = liberadoDiferencaValorRateio;
	}
	
	public UsuarioVO getResponsavelLiberadoDiferencaValorRateio() {
		if(responsavelLiberadoDiferencaValorRateio == null){
			responsavelLiberadoDiferencaValorRateio = new UsuarioVO();
		}
		return responsavelLiberadoDiferencaValorRateio;
	}
	
	public void setResponsavelLiberadoDiferencaValorRateio(UsuarioVO responsavelLiberadoDiferencaValorRateio) {
		this.responsavelLiberadoDiferencaValorRateio = responsavelLiberadoDiferencaValorRateio;
	}	
	
	public void setValorParcelaRateioDesconsiderado(Double valorParcelaRateioDesconsiderado) {
		this.valorParcelaRateioDesconsiderado = valorParcelaRateioDesconsiderado;		
	}

	public Double getValorParcelaRateioDesconsiderado() {
		if (valorParcelaRateioDesconsiderado == null) {
			valorParcelaRateioDesconsiderado = 0.0;
		}
		return valorParcelaRateioDesconsiderado;
	}

	private Double valorFinalRestituirAluno;


	public Double getValorFinalRestituirAluno() {
		if (valorFinalRestituirAluno == null) {
			valorFinalRestituirAluno = 0.0;
		}
		return valorFinalRestituirAluno;
	}
	
	public void setValorFinalRestituirAluno(Double valorFinalRestituirAluno) {
		this.valorFinalRestituirAluno = valorFinalRestituirAluno;
	}
	
	
}
