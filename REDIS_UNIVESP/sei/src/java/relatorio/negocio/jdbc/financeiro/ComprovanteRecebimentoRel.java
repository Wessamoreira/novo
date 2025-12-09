package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ChequeVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.financeiro.ComprovanteRecebimentoRelVO;
import relatorio.negocio.interfaces.financeiro.ComprovanteRecebimentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ComprovanteRecebimentoRel extends SuperRelatorio implements ComprovanteRecebimentoRelInterfaceFacade {

	public ComprovanteRecebimentoRel() {
	}

	public List<ComprovanteRecebimentoRelVO> criarObjeto(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		ComprovanteRecebimentoRelVO comprovanteRecebimentoRelVO = new ComprovanteRecebimentoRelVO();
		List<ComprovanteRecebimentoRelVO> listaComprovanteRecebimentoRelVOs = new ArrayList<ComprovanteRecebimentoRelVO>(0);
		montarParcelasPagasNrDocumentoDataVencimento(comprovanteRecebimentoRelVO, negociacaoRecebimentoVO);
		montarDataValorTotalRecebimentoPorExtenso(comprovanteRecebimentoRelVO, negociacaoRecebimentoVO, usuario);
		executarVerificacaoTrocoValorRecebimento(negociacaoRecebimentoVO, usuario);
		if (negociacaoRecebimentoVO.getTipoParceiro()) {
			comprovanteRecebimentoRelVO.setNomeAluno(negociacaoRecebimentoVO.getParceiroVO().getNome());
		} else if (negociacaoRecebimentoVO.getTipoFornecedor()) {
			comprovanteRecebimentoRelVO.setNomeAluno(negociacaoRecebimentoVO.getFornecedor().getNome());
		} else {
			StringBuilder nomeAlunos = new StringBuilder();
			Set<String> alunos = new HashSet<String>();
			for (ContaReceberVO contaReceber : negociacaoRecebimentoVO.getContaReceberVOs()) {
				if (contaReceber.getMatriculaAluno().getAluno().getCodigo().intValue() != 0 &&
						contaReceber.getMatriculaAluno().getAluno().getCodigo().intValue() != negociacaoRecebimentoVO.getPessoa().getCodigo().intValue()) {
					alunos.add(contaReceber.getMatriculaAluno().getAluno().getNome());
				}
			}
			if (!alunos.isEmpty()) {
				nomeAlunos.append(" (");
				String aux = "";
				for (String aluno : alunos) {
					nomeAlunos.append(aux).append("Aluno(a): ").append(aluno);
					aux = ", ";
				}
				nomeAlunos.append(")");
			}
			comprovanteRecebimentoRelVO.setNomeAluno(negociacaoRecebimentoVO.getPessoa().getNome() + nomeAlunos);
			alunos = null;
		}
		comprovanteRecebimentoRelVO.setNomeResponsavel(negociacaoRecebimentoVO.getResponsavel().getNome());
		comprovanteRecebimentoRelVO.setListaContaReceberNegociacaoRecebimentoVO(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs());
		comprovanteRecebimentoRelVO.setListaFormaPagamentoNegociacaoRecebimentoVO(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs());
		comprovanteRecebimentoRelVO.setCodigoNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
		// Abaixo dados da entidade principal, são replicados para as entidades
		// subordinadas, para que também possam ser
		// apresentados nos subrelatórios, haja vista, que isto é necessário,
		// para customização do relatório para comportar
		// diversas contas a receber (10 ou mais).
		for (ContaReceberNegociacaoRecebimentoVO contaVO : comprovanteRecebimentoRelVO.getListaContaReceberNegociacaoRecebimentoVO()) {
			contaVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
			contaVO.setNomeAluno(comprovanteRecebimentoRelVO.getNomeAluno());
			contaVO.setMatricula(comprovanteRecebimentoRelVO.getMatricula());
			contaVO.setTurma(comprovanteRecebimentoRelVO.getTurma());
			contaVO.setCodigoNegociacaoRecebimento(comprovanteRecebimentoRelVO.getCodigoNegociacaoRecebimento());
			contaVO.setParcelasPagasNrDocumentoDataVencimento(comprovanteRecebimentoRelVO.getParcelasPagasNrDocumentoDataVencimento());
			contaVO.setValorTotalRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getValorTotalRecebimentoPorExtenso());
			contaVO.setValorTotalRecebimento(comprovanteRecebimentoRelVO.getValorTotalRecebimento());
			if (contaVO.getContaReceber().getMatriculaPeriodo() == null || contaVO.getContaReceber().getMatriculaPeriodo().equals(0)) {
				contaVO.setAno(String.valueOf(Uteis.getAnoData(contaVO.getContaReceber().getDataVencimento())));
				contaVO.setSemestre(Uteis.getSemestreData(contaVO.getContaReceber().getDataVencimento()));
			} else {
				MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnoSemestreMatriculaPeriodoPorCodigo(contaVO.getContaReceber().getMatriculaPeriodo(), usuario);
				if (matriculaPeriodoVO != null && !matriculaPeriodoVO.getCodigo().equals(0)) {
					contaVO.setAno(matriculaPeriodoVO.getAno());
					contaVO.setSemestre(matriculaPeriodoVO.getSemestre());
				}
			}
			if(contaVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue() >= 0.0){
				contaVO.getContaReceber().setAcrescimo(contaVO.getContaReceber().getAcrescimo() + contaVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());
			}else if(contaVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue() < 0.0){
				contaVO.getContaReceber().setValorDescontoRecebido(contaVO.getContaReceber().getValorDescontoRecebido() - contaVO.getContaReceber().getValorIndiceReajustePorAtraso().doubleValue());	
			}
			
			if(contaVO.getContaReceber().getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())){		  				 
				 ChequeVO cheque = new ChequeVO();
 				 cheque = getFacadeFactory().getChequeFacade().consultarChequeDevolvidoPorContaReceberCodOrigemTipoOrigem(contaVO.getContaReceber().getCodOrigem(),contaVO.getContaReceber().getTipoOrigem());                
                if(Uteis.isAtributoPreenchido(cheque)) {
                	 contaVO.setChequeDevolvido(cheque); 
                	 
                }
				     
			}

		}

		for (FormaPagamentoNegociacaoRecebimentoVO formaPgtoVO : comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO()) {
			
			if (!formaPgtoVO.getCodigo().equals(0) && (formaPgtoVO.getFormaPagamento().getTipo().equals("CA") || formaPgtoVO.getFormaPagamento().getTipo().equals("CD"))) {
				if (formaPgtoVO.getFormaPagamento().getTipo().equals("CA")) {
					comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao().addAll(getFacadeFactory().getRecebimentoFacade().consultarFormaPagamentoNegociacaoRecebimentoCartaoRelatorio(formaPgtoVO, comprovanteRecebimentoRelVO));
				} else {
					formaPgtoVO.setDataPrevisaCartao(negociacaoRecebimentoVO.getData());
					formaPgtoVO.setDataEmissaoCartao(negociacaoRecebimentoVO.getData());
					formaPgtoVO.setCidadeDataRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getCidadeDataRecebimentoPorExtenso());
					formaPgtoVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
					formaPgtoVO.setValorRecebimento(Uteis.arrendondarForcando2CadasDecimais(formaPgtoVO.getValorRecebimento()));
					comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao().add(formaPgtoVO);
				}

			} else if (!formaPgtoVO.getCodigo().equals(0) && (formaPgtoVO.getFormaPagamento().getTipo().equals("DI"))) {
				formaPgtoVO.setDataPrevisaCartao(negociacaoRecebimentoVO.getData());
				formaPgtoVO.setDataEmissaoCartao(negociacaoRecebimentoVO.getData());
				formaPgtoVO.setCidadeDataRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getCidadeDataRecebimentoPorExtenso());
				formaPgtoVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
				formaPgtoVO.setValorRecebimento(Uteis.arrendondarForcando2CadasDecimais(formaPgtoVO.getValorRecebimento()));
				comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao().add(formaPgtoVO);

			} else if (!formaPgtoVO.getCodigo().equals(0) && (formaPgtoVO.getFormaPagamento().getTipo().equals("IS"))) {
				formaPgtoVO.setValorRecebimento(comprovanteRecebimentoRelVO.getValorCalculadoDescontoLancadoRecebimento());
				formaPgtoVO.setCidadeDataRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getCidadeDataRecebimentoPorExtenso());
				formaPgtoVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
				comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao().add(formaPgtoVO);
			}
			else {
				formaPgtoVO.setCidadeDataRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getCidadeDataRecebimentoPorExtenso());
				formaPgtoVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
				comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao().add(formaPgtoVO);
			}

		}
		comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO().clear();
		comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO().addAll(comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao());
		listaComprovanteRecebimentoRelVOs.add(comprovanteRecebimentoRelVO);
		return listaComprovanteRecebimentoRelVOs;
	}

	public void executarVerificacaoTrocoValorRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		negociacaoRecebimentoVO.setFormaPagamentoNegociacaoRecebimentoVOs(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().consultarPorCodigoNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		for (FormaPagamentoNegociacaoRecebimentoVO fpnr : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			if (fpnr.getFormaPagamento().getTipo().equals("DI")) {
				fpnr.setValorRecebimento(fpnr.getValorRecebimento() - negociacaoRecebimentoVO.getValorTroco());
			}
		}
	}

	public void montarParcelasPagasNrDocumentoDataVencimento(ComprovanteRecebimentoRelVO comprovanteRecebimentoRelVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO) throws Exception {
		String parcelasPagasNrDocumentoDataVencimento = "";
		for (ContaReceberNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs()) {
			if (!parcelasPagasNrDocumentoDataVencimento.equals("")) {
				parcelasPagasNrDocumentoDataVencimento += ", ";
			}
			parcelasPagasNrDocumentoDataVencimento += "nº Doc. " + obj.getContaReceber().getNrDocumento() + " - data vcto: " + obj.getContaReceber().getDataVencimento_Apresentar();
		}
		comprovanteRecebimentoRelVO.setParcelasPagasNrDocumentoDataVencimento(parcelasPagasNrDocumentoDataVencimento);
	}

	public void montarDataValorTotalRecebimentoPorExtenso(ComprovanteRecebimentoRelVO comprovanteRecebimentoRelVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		Extenso ext = new Extenso();
		negociacaoRecebimentoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(negociacaoRecebimentoVO.getUnidadeEnsino().getCodigo(), false, usuario));
		comprovanteRecebimentoRelVO.setCidadeDataRecebimentoPorExtenso(Uteis.getDataCidadeDiaMesPorExtensoEAno(negociacaoRecebimentoVO.getUnidadeEnsino().getCidade().getNome(), negociacaoRecebimentoVO.getData(), false));
		if(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().stream().allMatch(p-> p.getFormaPagamento().getTipoFormaPagamentoEnum().isIsencao())) {
			comprovanteRecebimentoRelVO.setValorCalculadoDescontoLancadoRecebimento(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().stream().mapToDouble(p->p.getContaReceber().getValorCalculadoDescontoLancadoRecebimento()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b)));
		}else {
			comprovanteRecebimentoRelVO.setValorTotalRecebimento(negociacaoRecebimentoVO.getValorTotalRecebimento() - negociacaoRecebimentoVO.getValorTroco());
		}
		
		
		ext.setNumber(comprovanteRecebimentoRelVO.getValorTotalRecebimento());
		comprovanteRecebimentoRelVO.setValorTotalRecebimentoPorExtenso("R$ " + Uteis.formatarDecimalDuasCasas(comprovanteRecebimentoRelVO.getValorTotalRecebimento()) + " (" + ext.toString() + ")");
	}

	/*
	 * Esta condição verifica se os atributos que armazena o caminho da logo e o
	 * nome da mesma estão ou não preenchidos. Se preenchido será considerado a
	 * logo inserida pelo usuário, caso contrário será atribuida a logo padrão.
	 */
	public void validaLogoComprovanteRecibo(String caminhoFisicoLogo, String nomeLogo, SuperParametroRelVO superParametro, String urlExternoDownloadArquivo) {

		if ((!caminhoFisicoLogo.equals("") && (!nomeLogo.equals("")))) {
			superParametro.adicionarParametro("logoUsuario", urlExternoDownloadArquivo + "/" + caminhoFisicoLogo.replaceAll("\\\\", "/") + "/" + nomeLogo);
		} else {
			superParametro.adicionarParametro("logoUsuario", "");

		}

	}

	public String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ComprovanteRecebimentoRel");
	}

	public String consultarIdentificadorTurmaContaReceber(List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs) throws Exception {
		if (contaReceberNegociacaoRecebimentoVOs.size() == 1) {
			return contaReceberNegociacaoRecebimentoVOs.get(0).getTurma();
		} else {
			return "";
		}
	}
	
	@Override
	public List<ComprovanteRecebimentoRelVO> montarObjetoComprovanteReciboCartaoCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		setIdEntidade("ComprovanteRecebimentoCartaoCreditoRel");
		ComprovanteRecebimentoRelVO comprovanteRecebimentoRelVO = new ComprovanteRecebimentoRelVO();
		List<ComprovanteRecebimentoRelVO> listaComprovanteRecebimentoRelVOs = new ArrayList<ComprovanteRecebimentoRelVO>(0);
		montarParcelasPagasNrDocumentoDataVencimento(comprovanteRecebimentoRelVO, negociacaoRecebimentoVO);
		montarDataValorTotalRecebimentoPorExtenso(comprovanteRecebimentoRelVO, negociacaoRecebimentoVO, usuario);
		StringBuilder nomeAlunos = new StringBuilder();
		Set<String> alunos = new HashSet<String>();
		for (ContaReceberVO contaReceber : negociacaoRecebimentoVO.getContaReceberVOs()) {
			if (contaReceber.getMatriculaAluno().getAluno().getCodigo().intValue() != 0 && contaReceber.getMatriculaAluno().getAluno().getCodigo().intValue() != negociacaoRecebimentoVO.getPessoa().getCodigo().intValue()) {
				alunos.add(contaReceber.getMatriculaAluno().getAluno().getNome());
			}
		}
		if (!alunos.isEmpty()) {
			nomeAlunos.append(" (");
			String aux = "";
			for (String aluno : alunos) {
				nomeAlunos.append(aux).append("Aluno(a): ").append(aluno);
				aux = ", ";
			}
			nomeAlunos.append(")");
		}
		comprovanteRecebimentoRelVO.setNomeAluno(negociacaoRecebimentoVO.getPessoa().getNome() + nomeAlunos);
		alunos = null;
		comprovanteRecebimentoRelVO.setNomeResponsavel(negociacaoRecebimentoVO.getResponsavel().getNome());
		comprovanteRecebimentoRelVO.setListaContaReceberNegociacaoRecebimentoVO(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs());
		comprovanteRecebimentoRelVO.setListaFormaPagamentoNegociacaoRecebimentoVO(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs());
		comprovanteRecebimentoRelVO.setCodigoNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
		// Abaixo dados da entidade principal, são replicados para as entidades
		// subordinadas, para que também possam ser
		// apresentados nos subrelatórios, haja vista, que isto é necessário,
		// para customização do relatório para comportar
		// diversas contas a receber (10 ou mais).
		for (ContaReceberNegociacaoRecebimentoVO contaVO : comprovanteRecebimentoRelVO.getListaContaReceberNegociacaoRecebimentoVO()) {
			contaVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
			contaVO.setNomeAluno(comprovanteRecebimentoRelVO.getNomeAluno());
			contaVO.setMatricula(comprovanteRecebimentoRelVO.getMatricula());
			contaVO.setTurma(comprovanteRecebimentoRelVO.getTurma());
			contaVO.setCodigoNegociacaoRecebimento(comprovanteRecebimentoRelVO.getCodigoNegociacaoRecebimento());
			contaVO.setParcelasPagasNrDocumentoDataVencimento(comprovanteRecebimentoRelVO.getParcelasPagasNrDocumentoDataVencimento());
			contaVO.setValorTotalRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getValorTotalRecebimentoPorExtenso());
			contaVO.setValorTotalRecebimento(comprovanteRecebimentoRelVO.getValorTotalRecebimento());
			if (contaVO.getContaReceber().getMatriculaPeriodo() == null || contaVO.getContaReceber().getMatriculaPeriodo().equals(0)) {
				contaVO.setAno(String.valueOf(Uteis.getAnoData(contaVO.getContaReceber().getDataVencimento())));
				contaVO.setSemestre(Uteis.getSemestreData(contaVO.getContaReceber().getDataVencimento()));
			} else {
				MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnoSemestreMatriculaPeriodoPorCodigo(contaVO.getContaReceber().getMatriculaPeriodo(), usuario);
				if (matriculaPeriodoVO != null && !matriculaPeriodoVO.getCodigo().equals(0)) {
					contaVO.setAno(matriculaPeriodoVO.getAno());
					contaVO.setSemestre(matriculaPeriodoVO.getSemestre());
				}
			}
		}
		String numeroCartao = "";
		Boolean primeiro = true;
		for (FormaPagamentoNegociacaoRecebimentoVO formaPgtoVO : comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO()) {
//			if(primeiro) {
//			numeroCartao = formaPgtoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao();
//			primeiro = false;
//		} else if(numeroCartao.equals(formaPgtoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao())){
//			continue;
//		} else {
//			numeroCartao = formaPgtoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getNumeroCartao();
//		}
			formaPgtoVO.setDataEmissaoCartao(negociacaoRecebimentoVO.getData());
			formaPgtoVO.setCidadeDataRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getCidadeDataRecebimentoPorExtenso());
			formaPgtoVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
			comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao().add(formaPgtoVO);
		}
		comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO().clear();
		comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO().addAll(comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao());
		listaComprovanteRecebimentoRelVOs.add(comprovanteRecebimentoRelVO);
		return listaComprovanteRecebimentoRelVOs;
	}
	
	protected static String idEntidade;
	
	public static void setIdEntidade(String idEntidade) {
		ComprovanteRecebimentoRel.idEntidade = idEntidade;
	}
	
	@Override
	public List<ComprovanteRecebimentoRelVO> criarObjetoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		ComprovanteRecebimentoRelVO comprovanteRecebimentoRelVO = new ComprovanteRecebimentoRelVO();
		List<ComprovanteRecebimentoRelVO> listaComprovanteRecebimentoRelVOs = new ArrayList<ComprovanteRecebimentoRelVO>(0);
		montarParcelasPagasNrDocumentoDataVencimento(comprovanteRecebimentoRelVO, negociacaoRecebimentoVO);
		montarDataValorTotalRecebimentoPorExtenso(comprovanteRecebimentoRelVO, negociacaoRecebimentoVO, usuario);
		executarVerificacaoValorRecebimentoDCC(negociacaoRecebimentoVO, usuario);
		if (negociacaoRecebimentoVO.getTipoParceiro()) {
			comprovanteRecebimentoRelVO.setNomeAluno(negociacaoRecebimentoVO.getParceiroVO().getNome());
		} else if (negociacaoRecebimentoVO.getTipoFornecedor()) {
			comprovanteRecebimentoRelVO.setNomeAluno(negociacaoRecebimentoVO.getFornecedor().getNome());
		} else {
			StringBuilder nomeAlunos = new StringBuilder();
			Set<String> alunos = new HashSet<String>();
			for (ContaReceberVO contaReceber : negociacaoRecebimentoVO.getContaReceberVOs()) {
				if (contaReceber.getMatriculaAluno().getAluno().getCodigo().intValue() != 0 &&
						contaReceber.getMatriculaAluno().getAluno().getCodigo().intValue() != negociacaoRecebimentoVO.getPessoa().getCodigo().intValue()) {
					alunos.add(contaReceber.getMatriculaAluno().getAluno().getNome());
				}
			}
			if (!alunos.isEmpty()) {
				nomeAlunos.append(" (");
				String aux = "";
				for (String aluno : alunos) {
					nomeAlunos.append(aux).append("Aluno(a): ").append(aluno);
					aux = ", ";
				}
				nomeAlunos.append(")");
			}
			comprovanteRecebimentoRelVO.setNomeAluno(negociacaoRecebimentoVO.getPessoa().getNome() + nomeAlunos);
			alunos = null;
		}
		comprovanteRecebimentoRelVO.setNomeResponsavel(negociacaoRecebimentoVO.getResponsavel().getNome());
		comprovanteRecebimentoRelVO.setListaContaReceberNegociacaoRecebimentoVO(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs());
		comprovanteRecebimentoRelVO.setListaFormaPagamentoNegociacaoRecebimentoVO(negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs());
		comprovanteRecebimentoRelVO.setCodigoNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo());
		// Abaixo dados da entidade principal, são replicados para as entidades
		// subordinadas, para que também possam ser
		// apresentados nos subrelatórios, haja vista, que isto é necessário,
		// para customização do relatório para comportar
		// diversas contas a receber (10 ou mais).
		for (ContaReceberNegociacaoRecebimentoVO contaVO : comprovanteRecebimentoRelVO.getListaContaReceberNegociacaoRecebimentoVO()) {
			contaVO.setNomeResponsavel(comprovanteRecebimentoRelVO.getNomeResponsavel());
			contaVO.setNomeAluno(comprovanteRecebimentoRelVO.getNomeAluno());
			contaVO.setMatricula(comprovanteRecebimentoRelVO.getMatricula());
			contaVO.setTurma(comprovanteRecebimentoRelVO.getTurma());
			contaVO.setCodigoNegociacaoRecebimento(comprovanteRecebimentoRelVO.getCodigoNegociacaoRecebimento());
			contaVO.setParcelasPagasNrDocumentoDataVencimento(comprovanteRecebimentoRelVO.getParcelasPagasNrDocumentoDataVencimento());
			contaVO.setValorTotalRecebimentoPorExtenso(comprovanteRecebimentoRelVO.getValorTotalRecebimentoPorExtenso());
			contaVO.setValorTotalRecebimento(comprovanteRecebimentoRelVO.getValorTotalRecebimento());
			if (contaVO.getContaReceber().getMatriculaPeriodo() == null || contaVO.getContaReceber().getMatriculaPeriodo().equals(0)) {
				contaVO.setAno(String.valueOf(Uteis.getAnoData(contaVO.getContaReceber().getDataVencimento())));
				contaVO.setSemestre(Uteis.getSemestreData(contaVO.getContaReceber().getDataVencimento()));
			} else {
				MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarAnoSemestreMatriculaPeriodoPorCodigo(contaVO.getContaReceber().getMatriculaPeriodo(), usuario);
				if (matriculaPeriodoVO != null && !matriculaPeriodoVO.getCodigo().equals(0)) {
					contaVO.setAno(matriculaPeriodoVO.getAno());
					contaVO.setSemestre(matriculaPeriodoVO.getSemestre());
				}
			}
		}
		for (FormaPagamentoNegociacaoRecebimentoVO formaPgtoVO : comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO()) {
			comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao().addAll(getFacadeFactory().getRecebimentoFacade().consultarFormaPagamentoNegociacaoRecebimentoCartaoRelatorioDCC(formaPgtoVO, comprovanteRecebimentoRelVO));
		}
		comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO().clear();
		comprovanteRecebimentoRelVO.getListaFormaPagamentoNegociacaoRecebimentoVO().addAll(comprovanteRecebimentoRelVO.getListaFormaPagamentoCartao());
		listaComprovanteRecebimentoRelVOs.add(comprovanteRecebimentoRelVO);
		return listaComprovanteRecebimentoRelVOs;
	}
	
	public void executarVerificacaoValorRecebimentoDCC(NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		negociacaoRecebimentoVO.setFormaPagamentoNegociacaoRecebimentoVOs(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().consultarPorCodigoNegociacaoRecebimento(negociacaoRecebimentoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		for (FormaPagamentoNegociacaoRecebimentoVO fpnr : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			fpnr.setValorRecebimento(fpnr.getValorRecebimento() - negociacaoRecebimentoVO.getValorTroco());
		}
	}
}
