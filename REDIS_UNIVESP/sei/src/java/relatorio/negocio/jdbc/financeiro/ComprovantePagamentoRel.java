package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoPagamentoVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ComprovantePagamentoRelVO;
import relatorio.negocio.interfaces.financeiro.ComprovantePagamentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ComprovantePagamentoRel extends SuperRelatorio implements ComprovantePagamentoRelInterfaceFacade {

	public ComprovantePagamentoRel() {
	}

	public List<ComprovantePagamentoRelVO> criarObjeto(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
		ComprovantePagamentoRelVO comprovantePagamentoRelVO = new ComprovantePagamentoRelVO();
		List<ComprovantePagamentoRelVO> listaComprovantePagamentoRelVOs = new ArrayList<ComprovantePagamentoRelVO>(0);
		montarDataValorTotalPagamentoPorExtenso(comprovantePagamentoRelVO, negociacaoPagamentoVO, usuario);
		executarVerificacaoTrocoValorPagamento(negociacaoPagamentoVO, usuario);
		realizarVerificacaoTipoSacado(negociacaoPagamentoVO, comprovantePagamentoRelVO);
		comprovantePagamentoRelVO.setNomeResponsavel(negociacaoPagamentoVO.getResponsavel().getNome());
		comprovantePagamentoRelVO.setListaContaPagarNegociacaoPagamentoVO(negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs());
		comprovantePagamentoRelVO.setListaFormaPagamentoNegociacaoPagamentoVO(negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs());
		comprovantePagamentoRelVO.setCodigoNegociacaoPagamento(negociacaoPagamentoVO.getCodigo());		
		for (ContaPagarNegociacaoPagamentoVO contaVO : comprovantePagamentoRelVO.getListaContaPagarNegociacaoPagamentoVO()) {
			contaVO.setNomeResponsavel(comprovantePagamentoRelVO.getNomeResponsavel());
			contaVO.setNomeAluno(comprovantePagamentoRelVO.getNomeAluno());
			contaVO.setMatricula(comprovantePagamentoRelVO.getMatricula());
			contaVO.setTurma(comprovantePagamentoRelVO.getTurma());
			contaVO.setCodigoNegociacaoPagamento(comprovantePagamentoRelVO.getCodigoNegociacaoPagamento());
			contaVO.setValorTotalPagamentoPorExtenso(comprovantePagamentoRelVO.getValorTotalPagamentoPorExtenso());
			contaVO.setValorTotalPagamento(comprovantePagamentoRelVO.getValorTotalPagamento());
		
		}
		for (FormaPagamentoNegociacaoPagamentoVO formaPgtoVO : comprovantePagamentoRelVO.getListaFormaPagamentoNegociacaoPagamentoVO()) {
			formaPgtoVO.setCidadeDataPagamentoPorExtenso(comprovantePagamentoRelVO.getCidadeDataPagamentoPorExtenso());
			formaPgtoVO.setNomeResponsavel(comprovantePagamentoRelVO.getNomeResponsavel());
			formaPgtoVO.setNomeAluno(comprovantePagamentoRelVO.getNomeAluno());
		}
		listaComprovantePagamentoRelVOs.add(comprovantePagamentoRelVO);
		return listaComprovantePagamentoRelVOs;
	}

	public void realizarVerificacaoTipoSacado(NegociacaoPagamentoVO negociacaoPagamentoVO, ComprovantePagamentoRelVO comprovantePagamentoRelVO) {
		if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.ALUNO.getValor())) {
			comprovantePagamentoRelVO.setNomeAluno(negociacaoPagamentoVO.getAluno().getNome());
		}
		if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.BANCO.getValor())) {
			comprovantePagamentoRelVO.setNomeAluno(negociacaoPagamentoVO.getBanco().getNome());
		}
		if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor())) {
			comprovantePagamentoRelVO.setNomeAluno(negociacaoPagamentoVO.getFornecedor().getNome());
		}
		if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
			comprovantePagamentoRelVO.setNomeAluno(negociacaoPagamentoVO.getFuncionario().getPessoa().getNome());
		}
		if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.PARCEIRO.getValor())) {
			comprovantePagamentoRelVO.setNomeAluno(negociacaoPagamentoVO.getParceiro().getNome());
		}
		if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
			comprovantePagamentoRelVO.setNomeAluno(negociacaoPagamentoVO.getResponsavelFinanceiro().getNome());
		}
		if (negociacaoPagamentoVO.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			comprovantePagamentoRelVO.setNomeAluno(negociacaoPagamentoVO.getOperadoraCartao().getNome());
		}
	}

	public void executarVerificacaoTrocoValorPagamento(NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
		negociacaoPagamentoVO.setFormaPagamentoNegociacaoPagamentoVOs(getFacadeFactory().getFormaPagamentoNegociacaoPagamentoFacade().consultarPorCodigoNegociacaoPagamento(negociacaoPagamentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		for (FormaPagamentoNegociacaoPagamentoVO fpnp : negociacaoPagamentoVO.getFormaPagamentoNegociacaoPagamentoVOs()) {
			fpnp.setValor(fpnp.getValor() - negociacaoPagamentoVO.getValorTroco());
		}
	}

	public void montarDataValorTotalPagamentoPorExtenso(ComprovantePagamentoRelVO comprovantePagamentoRelVO, NegociacaoPagamentoVO negociacaoPagamentoVO, UsuarioVO usuario) throws Exception {
		Extenso ext = new Extenso();
		negociacaoPagamentoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(negociacaoPagamentoVO.getUnidadeEnsino().getCodigo(), false, usuario));
		comprovantePagamentoRelVO.setCidadeDataPagamentoPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(negociacaoPagamentoVO.getUnidadeEnsino().getCidade().getNome(), negociacaoPagamentoVO.getData(), false));
		comprovantePagamentoRelVO.setValorTotalPagamento(negociacaoPagamentoVO.getValorTotalPagamento() - negociacaoPagamentoVO.getValorTroco());
		ext.setNumber(comprovantePagamentoRelVO.getValorTotalPagamento());
		comprovantePagamentoRelVO.setValorTotalPagamentoPorExtenso("R$" + Uteis.formatarDecimalDuasCasas(comprovantePagamentoRelVO.getValorTotalPagamento()) + " ( " + ext.toString() + " ) ");
	}

	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ComprovantePagamentoRel");
	}
}
