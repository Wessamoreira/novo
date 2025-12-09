package financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.junit.Test;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import arquitetura.TestManager;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.MapaRemessaLoteContaPagarVO;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.facade.jdbc.financeiro.remessa.contapagar.CaixaEconomicaRemessaContaPagarCNAB240;

public class CaixaEconomicaRemessaContaPagarCNAB240Test extends TestManager {

	private static final long serialVersionUID = 3814020978833986547L;

	/**
	 * Construtor carrega o {@link FacadeFactory}
	 */
	public CaixaEconomicaRemessaContaPagarCNAB240Test() {
		try {
			new UnidadeEnsino(getConexao(), getFacadeFactoryTest());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void executarGeracaoArquivoRemessa() {
		try {
			TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamentoVO = new TemplateLancamentoFolhaPagamentoVO();
			templateLancamentoFolhaPagamentoVO = getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(897, Uteis.NIVELMONTARDADOS_COMBOBOX);

			List<ContraChequeVO> lista = getFacadeFactoryTest().getContraChequeInterfaceFacade().consultarContraChequePorTemplateLancamentoFolha(templateLancamentoFolhaPagamentoVO);

			for (ContraChequeVO contraChequeVO : lista) {
				//getFacadeFactory().getContraChequeInterfaceFacade().gerarContaPagar(templateLancamentoFolhaPagamentoVO, new UsuarioVO());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Erro: " + e.getMessage());
		}
	}

	/**
	 * Monta os dados {@link ControleRemessaContaPagarVO}
	 * 
	 * @param unidadesEnsinoVO
	 * @return
	 * @throws Exception
	 */
	private ControleRemessaContaPagarVO montarDadosControleRemessa(UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		ControleRemessaContaPagarVO controleRemessaContaPagarVO = new ControleRemessaContaPagarVO();
		controleRemessaContaPagarVO.setUnidadeEnsinoVO(unidadeEnsinoVO);

/*		List<ContaCorrenteVO> contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorNomeUnidadeEnsino(
				controleRemessaContaPagarVO.getUnidadeEnsinoVO().getNome(), controleRemessaContaPagarVO.getUnidadeEnsinoVO().getCodigo(), 
				false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);

		if (Uteis.isAtributoPreenchido(contaCorrente)) {
			controleRemessaContaPagarVO.setContaCorrenteVO(contaCorrente.get(0));
		} else {
			controleRemessaContaPagarVO.getContaCorrenteVO().setCodigo(1);
		}*/
		controleRemessaContaPagarVO.setSituacaoControleRemessa(SituacaoControleRemessaEnum.AGUARDANDO_PROCESSAMENTO_RETORNO_REMESSA);
		controleRemessaContaPagarVO.setDataInicio(new Date());
		controleRemessaContaPagarVO.setDataGeracao(new Date());
		controleRemessaContaPagarVO.setDataFim(new Date());
		return controleRemessaContaPagarVO;
	}

	/**
	 * Monta os dados do conta a pagar.
	 * 
	 * @param unidadeEnsinoVO
	 * @param contraChequeVO
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private ContaPagarVO montarDadosContaPagar(UnidadeEnsinoVO unidadeEnsinoVO, ContraChequeVO contraChequeVO, ContaPagarControleRemessaContaPagarVO obj, UsuarioVO usuario) throws Exception {
		ContaPagarVO contaPagar = new ContaPagarVO();
		contaPagar.setValor(obj.getValor());
		contaPagar.setDataVencimento(obj.getDataVencimento());
		contaPagar.setData(obj.getDataVencimento());
		contaPagar.setSituacao("PA"); //PA(Conta Paga) - AP(Conta à pagar)
		contaPagar.setTipoSacado("FU"); //Funcionario
		contaPagar.setPessoa(contraChequeVO.getFuncionarioCargo().getFuncionarioVO().getPessoa());
		contaPagar.setFuncionario(contraChequeVO.getFuncionarioCargo().getFuncionarioVO());
		contaPagar.setUnidadeEnsino(unidadeEnsinoVO);
		contaPagar.setTipoServicoContaPagar(TipoServicoContaPagarEnum.CAIXA_ECONOMICA_PAGAMENTO_SALARIO);
		contaPagar.setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum.CAIXA_ECONOMICA_TED);
		contaPagar.setTipoOrigem(OrigemContaPagar.FOLHA_PAGAMENTO.getValor());

		//Erro: Deve ser informado pelo menos um Centro de Resultado Movimetação.
		CentroResultadoOrigemVO centroResultadoOrigemVO = montarDadosCentroResultado(contaPagar, usuario);
		
		contaPagar.getListaCentroResultadoOrigemVOs().add(centroResultadoOrigemVO);
		contaPagar.getResponsavel().setCodigo(1);
		getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, false, usuario);
		return contaPagar;
	}

	/**
	 * Monta os dados do centro de resultado.
	 * 
	 * @param contaPagar
	 * @return
	 * @throws Exception
	 */
	private CentroResultadoOrigemVO montarDadosCentroResultado(ContaPagarVO contaPagar, UsuarioVO usuario) throws Exception {
		CentroResultadoOrigemVO centroResultadoOrigemVO = new CentroResultadoOrigemVO();

		CategoriaDespesaVO categoriaDespesaVO = getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(4, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);

		centroResultadoOrigemVO.setCategoriaDespesaVO(categoriaDespesaVO);

		centroResultadoOrigemVO.setValor(contaPagar.getValor());
		centroResultadoOrigemVO.setPorcentagem(100.00);
		centroResultadoOrigemVO.setUnidadeEnsinoVO(contaPagar.getUnidadeEnsino());
		centroResultadoOrigemVO.setCentroResultadoAdministrativo(contaPagar.getUnidadeEnsino().getCentroResultadoVO());
		centroResultadoOrigemVO.setTipoCentroResultadoOrigemEnum(TipoCentroResultadoOrigemEnum.CONTA_PAGAR);
		
		DepartamentoVO departamentoVO = (DepartamentoVO) getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(1, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		centroResultadoOrigemVO.setDepartamentoVO(departamentoVO);
		
		if(centroResultadoOrigemVO.isCategoriaDespesaInformada()){
			List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum = new ArrayList<>();
			getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(centroResultadoOrigemVO.getCategoriaDespesaVO(), listaSelectItemTipoNivelCentroResultadoEnum);
			if(!listaSelectItemTipoNivelCentroResultadoEnum.isEmpty()){
				centroResultadoOrigemVO.setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) listaSelectItemTipoNivelCentroResultadoEnum.get(0).getValue());	
			}
		}
		return centroResultadoOrigemVO;
	}
}
