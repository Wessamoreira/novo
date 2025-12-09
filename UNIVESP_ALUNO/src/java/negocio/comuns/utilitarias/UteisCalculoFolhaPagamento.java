package negocio.comuns.utilitarias;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;

import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

public class UteisCalculoFolhaPagamento extends SuperFacadeJDBC {

	private static final long serialVersionUID = 8204407640474883258L;

	/**
	 * Calculo os avos proporcionais de 13
	 * caso a data de demissao for nula o sistema considera o ultima dia do ano (calendario cheio) 
	 * @param dataInicialAno
	 * @param dataAdmissao
	 * @param dataDemissao
	 * @return
	 */
//	public static Integer realizarCalculoAvosProporcionais13(Date dataInicialAno, Date dataAdmissao, Date dataDemissao, Integer periodosPerdidos) {
//		Boolean anoAdmissaoIgualAnoDemissao = UteisData.getAnoData(dataAdmissao) == UteisData.getAnoData(dataInicialAno);
//		Integer mesesTrabalhados = 0;
//		Integer mesInicial = anoAdmissaoIgualAnoDemissao ? UteisData.getMesData(dataAdmissao) : 1;
//		Integer mesFinal = Uteis.isAtributoPreenchido(dataDemissao) == Boolean.TRUE ? UteisData.getMesData(dataDemissao) : 12;
//
//		mesesTrabalhados = mesFinal - mesInicial;
//
//		if(UteisData.getAnoData(dataAdmissao) == UteisData.getAnoData(dataInicialAno) && UteisData.getDiaMesData(dataAdmissao) > 15) {
//			mesesTrabalhados--;
//		}
//
//		if((Uteis.isAtributoPreenchido(dataDemissao) && UteisData.getDiaMesData(dataDemissao) >= 15) || !Uteis.isAtributoPreenchido(dataDemissao)) {
//
//			//Funcionario contratado e demitido no mesmo mes
//			if(anoAdmissaoIgualAnoDemissao && Uteis.isAtributoPreenchido(dataDemissao) && Uteis.getMesData(dataAdmissao) == Uteis.getMesData(dataDemissao)) {
//
//				//Funcionario trabalhou mais de 15 dias naquele mes
//				if((UteisData.getDiaMesData(dataDemissao) - UteisData.getDiaMesData(dataAdmissao) + 1) >= 15) {
//					mesesTrabalhados++;
//				}
//
//			} else {
//				mesesTrabalhados++;				
//			}
//		}
//		
//		return mesesTrabalhados - periodosPerdidos;
//	}

	/**
	 * Calculo os avos proporcionais de ferias
	 * caso a data de demissao for nula o sistema considera o ultima dia do ano (calendario cheio) 
	 * @param dataInicialAno
	 * @param ultimoPeriodoAquisitivo
	 * @param dataDemissao
	 * @return
	 */
//	public static Integer realizarCalculoAvosProporcionaisFerias(Date ultimoPeriodoAquisitivo, Date dataDemissao, Integer diasDeFalta) {
//		
//		long diasTrabalhados = UteisData.getCalculaDiferencaEmDias(ultimoPeriodoAquisitivo, dataDemissao) - diasDeFalta + 1;
//		long diasMes = 30;
//		
//		BigDecimal avosFeriasProporcional = new BigDecimal(diasTrabalhados).divide(new BigDecimal(diasMes), 0, RoundingMode.HALF_UP);
//		
//		return avosFeriasProporcional.intValue();
//	}
	
	/**
	 * Calculo os avos proporcionais de ferias
	 * 
	 * @param funcionario
	 * @param dataConsulta
	 * @param dias de falta
	 * 
	 * @return
	 */
//	public static Integer realizarCalculoAvosProporcionaisFerias(FuncionarioCargoVO funcionario, Date dataConsulta, Integer diasDeFalta) {
//		
//		long diasTrabalhados = UteisData.getCalculaDiferencaEmDias(funcionario.getDataAdmissao(), dataConsulta) - diasDeFalta + 1;
//		long diasMes = 30;
//		
//		BigDecimal avosFeriasProporcional = new BigDecimal(diasTrabalhados).divide(new BigDecimal(diasMes), 0, RoundingMode.HALF_UP);
//		
//		return avosFeriasProporcional.intValue();
//	}

	/**
	 * Realiza o calculo dos dias afastado do funcionario pelas datas informadas.
	 * 
	 * @param dataInicio
	 * @param dataFinal
	 * @param dataComparacao
	 * @return
	 */
//	@SuppressWarnings("deprecation")
//	public static Integer realizarCalculoDiasAfastamento(Date dataInicio, Date dataFinal, Date dataComparacao) {
//
//		if ( (UteisData.getMesData(dataInicio) == UteisData.getMesData(dataComparacao)) 
//				&& (UteisData.getMesData(dataFinal) == UteisData.getMesData(dataComparacao))) {
//			return UteisData.getDiaMesData(dataFinal) - UteisData.getDiaMesData(dataInicio) + 1;
//		}
//
//		if ( (UteisData.getMesData(dataInicio) == UteisData.getMesData(dataComparacao)) 
//				&& (UteisData.getMesData(dataFinal) != UteisData.getMesData(dataComparacao))) {
//			return UteisData.getUltimaDataMes(dataInicio).getDate() - UteisData.getDiaMesData(dataInicio) + 1;
//		}
//
//		if ( (UteisData.getMesData(dataInicio) != UteisData.getMesData(dataComparacao)) 
//				&& (UteisData.getMesData(dataFinal) == UteisData.getMesData(dataComparacao))) {
//			return UteisData.getDiaMesData(dataFinal);
//		}
//
//		if ( UteisData.getPrimeiraDataMaior(dataComparacao, dataInicio) &&  !UteisData.getPrimeiraDataMaior(dataComparacao, dataFinal)) {
//			return UteisData.getUltimaDataMes(dataComparacao).getDate();
//		}
//
//		return 0;
//	}
	
//	public static int quantidadeDiasAfastado(FuncionarioCargoVO funcionarioCargoVO, Date dataComparacao) {
//		try {
//			List<AfastamentoFuncionarioVO> afastamentos = getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().consultarUltimpoAfastamentoPorFuncionarioEDataComparacao(funcionarioCargoVO, dataComparacao);
//			int total = 0;
//			for (AfastamentoFuncionarioVO afastamentoVO : afastamentos) {
//				total += UteisCalculoFolhaPagamento.realizarCalculoDiasAfastamento(afastamentoVO.getDataInicio(), afastamentoVO.getDataFinal(), dataComparacao);
//			}
//			return total;			
//		} catch (Exception e) {
//			return 0;
//		}
//	}

	/**
	 * Consulta quantidade de dias afastado por data de competencia e motivo do afastamento.
	 * 
	 * @param funcionarioCargoVO
	 * @param dataComparacao
	 * @param motivo
	 * @return
	 */
//	public static int quantidadeDiasAfastadoPorMotivo(FuncionarioCargoVO funcionarioCargoVO, Date dataComparacao, String motivo) {
//		try {
//			List<AfastamentoFuncionarioVO> afastamentoFuncionarioVO = getFacadeFactory().getAfastamentoFuncionarioInterfaceFacade().consultarAfastamentoPorFuncionarioEDataComparacaoEMotivoAfastamento(funcionarioCargoVO, dataComparacao, motivo);
//			Integer soma = 0;
//			for (AfastamentoFuncionarioVO afastamentoFuncionarioVO2 : afastamentoFuncionarioVO) {
//				soma += UteisCalculoFolhaPagamento.realizarCalculoDiasAfastamento(afastamentoFuncionarioVO2.getDataInicio(), afastamentoFuncionarioVO2.getDataFinal(), dataComparacao);			
//			}
//			return soma;
//		} catch (Exception e) {
//			return 0;
//		}
//	}
}