package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.financeiro.ListagemDescontosAlunoUnidadeEnsinoVO;
import relatorio.negocio.comuns.financeiro.ListagemDescontosAlunosPorTipoDescontoRelVO;
import relatorio.negocio.comuns.financeiro.ListagemDescontosAlunosRelVO;
import relatorio.negocio.interfaces.financeiro.ListagemDescontosAlunosRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class ListagemDescontosAlunosRel extends SuperRelatorio implements ListagemDescontosAlunosRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public void validarDados(TurmaVO turmaVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, String campoFiltrarPor, List<UnidadeEnsinoVO> unidadeEnsinoVOs, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, Date dataInicio, Date dataTermino, Integer descontoProgressivo, Integer convenio, Integer planoDesconto, Boolean apresentarDescontoRateio, Boolean apresentarDescontoAluno, Boolean apresentarDescontoRecebimento) throws Exception {
		boolean excessao = true;
		for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
			if (ue.getFiltrarUnidadeEnsino()) {
				excessao = false;
			}
		}
		if (excessao) {
			throw new Exception("O campo UNIDADE DE ENSINO deve ser informado.");
		}
		if (!campoFiltrarPor.equals("unidadeEnsino")) {
			if (campoFiltrarPor.equals("curso")) {
				if (!Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO.getCurso())) {
					throw new ConsistirException("Por Favor informe um CURSO para a geração do relatório.");
				}
			} else {
				if (!Uteis.isAtributoPreenchido(turmaVO)) {
					throw new ConsistirException("Por Favor informe uma TURMA para a geração do relatório.");
				}
			}
		}
		if(!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL) && ano.trim().isEmpty()) {
			throw new ConsistirException("O campo ANO deve ser informado.");
		}
		if(!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL) && ano.trim().length() != 4) {
			throw new ConsistirException("O campo ANO deve ser informado com 4 dígitos.");
		}
		if(periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL) && semestre.trim().isEmpty()) {
			throw new ConsistirException("O campo SEMESTRE deve ser informado.");
		}
		
		if (descontoProgressivo.equals(-1) && convenio.equals(-1) && planoDesconto.equals(-1) && !apresentarDescontoRateio && !apresentarDescontoAluno && !apresentarDescontoRecebimento) {
			throw new ConsistirException("Deve ser informado um Tipo de Desconto para geração do Relatório.");
		}
	}

	@Override
	public List<ListagemDescontosAlunoUnidadeEnsinoVO> criarObjeto(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, String campoFiltroPor, String ano, String semestre, Integer descontoProgressivo, Integer planoDesconto, Integer convenio, List<UnidadeEnsinoVO> unidadeEnsinoVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer parceiro, Integer categoriaDesconto, String tipoLayout, PeriodicidadeEnum periodicidadeEnum, Date dataInicio, Date dataTermino, UsuarioVO usuarioVO, Boolean apresentarDescontoRateio, Boolean apresentarDescontoAluno, Boolean apresentarDescontoRecebimento) throws Exception {
		validarDados(turmaVO, unidadeEnsinoCursoVO, campoFiltroPor, unidadeEnsinoVOs, periodicidadeEnum, ano, semestre, dataInicio, dataTermino, descontoProgressivo, convenio, planoDesconto, apresentarDescontoRateio, apresentarDescontoAluno, apresentarDescontoRecebimento);
		if (!campoFiltroPor.equals("unidadeEnsino")) {
			if (campoFiltroPor.equals("curso")) {
				turmaVO =  null;
			}else {
				unidadeEnsinoCursoVO.setCurso(null);
			}
		}else {
			unidadeEnsinoCursoVO.setCurso(null);
			turmaVO = null;
		}
		ListagemDescontosAlunosRel.emitirRelatorio(getIdEntidade(), true, usuarioVO);
		return realizarGeracaoRelatorio(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, tipoLayout, periodicidadeEnum, dataInicio, dataTermino, usuarioVO, apresentarDescontoRateio, apresentarDescontoAluno, apresentarDescontoRecebimento);

	}

	

	public static String getDesignIReportRelatorio(String tipoLayout) throws Exception {	
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "ListagemDescontosUnidadeEnsinoRel.jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ListagemDescontosAlunosRel");
	}

	public List<ListagemDescontosAlunoUnidadeEnsinoVO> realizarGeracaoRelatorio(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, String ano, String semestre, Integer descontoProgressivo, Integer planoDesconto, Integer convenio, List<UnidadeEnsinoVO> unidadeEnsinoVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer parceiro, Integer categoriaDesconto, String tipoLayout,  PeriodicidadeEnum periodicidadeEnum, Date dataInicio, Date dataTermino, UsuarioVO usuarioVO, Boolean apresentarDescontoRateio, Boolean apresentarDescontoAluno, Boolean apresentarDescontoRecebimento) throws Exception {
		SqlRowSet rs = executarConsultaParametrizada2(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino, tipoLayout, apresentarDescontoRateio, apresentarDescontoAluno, apresentarDescontoRecebimento);
		Integer matriculaPeriodo = 0;
		Integer unidadeEnsino = 0;
		List<ListagemDescontosAlunoUnidadeEnsinoVO> listagemDescontosAlunoUnidadeEnsinoVOs = new ArrayList<ListagemDescontosAlunoUnidadeEnsinoVO>(0);
		ListagemDescontosAlunoUnidadeEnsinoVO listagemDescontosAlunoUnidadeEnsinoVO = null;		
		ListagemDescontosAlunosRelVO listagemDescontosAlunosSinteticoRel = null;
		String agrupador = "";
		while (rs.next()) {			
			if(unidadeEnsino.equals(0) || !unidadeEnsino.equals(rs.getInt("codigounidadeensino"))){
				listagemDescontosAlunoUnidadeEnsinoVO =  new ListagemDescontosAlunoUnidadeEnsinoVO();
				listagemDescontosAlunoUnidadeEnsinoVO.getUnidadeEnsinoVO().setCodigo(rs.getInt("codigounidadeensino"));
				listagemDescontosAlunoUnidadeEnsinoVO.getUnidadeEnsinoVO().setNome(rs.getString("unidadeensino"));
				listagemDescontosAlunoUnidadeEnsinoVOs.add(listagemDescontosAlunoUnidadeEnsinoVO);
				unidadeEnsino = rs.getInt("codigounidadeensino");	
				agrupador = "";
			}
			if (!tipoLayout.equals("ListagemDescontosAlunosSintetico")) {
				if(matriculaPeriodo.equals(0) || !matriculaPeriodo.equals(rs.getInt("matriculaperiodo"))) {
					listagemDescontosAlunosSinteticoRel = new ListagemDescontosAlunosRelVO();								
					listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().setCodigo(rs.getInt("matriculaperiodo"));
					listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getMatriculaVO().setMatricula(rs.getString("matricula"));
					listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino"));
					listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getMatriculaVO().getCurso().setNome(rs.getString("curso"));
					listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getMatriculaVO().getTurno().setNome(rs.getString("turno"));
					listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getTurma().setIdentificadorTurma(rs.getString("turma"));
					listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNome(rs.getString("aluno"));
					listagemDescontosAlunosSinteticoRel.setAno(ano);
					listagemDescontosAlunosSinteticoRel.setSemestre(semestre);
					listagemDescontosAlunoUnidadeEnsinoVO.getListagemDescontosAlunosRelVOs().add(listagemDescontosAlunosSinteticoRel);
					matriculaPeriodo = rs.getInt("matriculaperiodo");
				}			
			}else if(agrupador.trim().isEmpty() ||  !agrupador.equals(rs.getString("agrupador"))) {
				listagemDescontosAlunosSinteticoRel = new ListagemDescontosAlunosRelVO();
				listagemDescontosAlunosSinteticoRel.setAgrupador(rs.getString("agrupador"));
				listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setNome(rs.getString("unidadeensino"));
				listagemDescontosAlunosSinteticoRel.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().setCodigo(rs.getInt("codigounidadeensino"));
				listagemDescontosAlunoUnidadeEnsinoVO.getListagemDescontosAlunosRelVOs().add(listagemDescontosAlunosSinteticoRel);
				agrupador = rs.getString("agrupador");
			}
			String[] matriculas = rs.getString("matricula").split(",");
			for(String matricula: matriculas) {
				if(!listagemDescontosAlunoUnidadeEnsinoVO.getMatriculaComDesconto().contains(matricula)) {
					listagemDescontosAlunoUnidadeEnsinoVO.getMatriculaComDesconto().add(matricula);	
				}
			}
			adicionarTotalizadorGeral(listagemDescontosAlunoUnidadeEnsinoVO.getListagemTotalDescontosRelVOs(), rs.getString("agrupador"), rs.getDouble("valordescontomatricula"), 
					rs.getInt("qtdeContaMatricula"), rs.getDouble("valordescontoparcela"), rs.getInt("qtdeContaParcela"), !tipoLayout.equals("ListagemDescontosAlunosSintetico") ? 1 : rs.getInt("qtdeMatricula"), rs.getDouble("valorTotalDesconto"), matriculas);				

			adicionarDescontoAluno(listagemDescontosAlunosSinteticoRel, rs.getString("tipodesconto"), ((Long)rs.getObject("codigodesconto")).intValue(), rs.getString("descricaodesconto"), rs.getDouble("valordescontomatricula"), 
					rs.getInt("qtdeContaMatricula"), rs.getDouble("valordescontoparcela"), rs.getInt("qtdeContaParcela"), !tipoLayout.equals("ListagemDescontosAlunosSintetico") ? 1 : rs.getInt("qtdeMatricula"), 
					matriculas, rs.getDouble("valorTotalDesconto"), rs.getString("agrupador"));
			
		}		
		for(ListagemDescontosAlunoUnidadeEnsinoVO alunoUnidadeEnsinoVO: listagemDescontosAlunoUnidadeEnsinoVOs) {
			Ordenacao.ordenarLista(alunoUnidadeEnsinoVO.getListagemTotalDescontosRelVOs(), "agrupador");
		}
		return listagemDescontosAlunoUnidadeEnsinoVOs;
	}

	public void adicionarDescontoAluno(ListagemDescontosAlunosRelVO listagemDescontosAlunosVO, String origemDesconto, Integer codigoDesconto, String nomeDesconto, Double valorDescontoMatricula, Integer qtdeContaMatricula, Double valorDescontoParcela, Integer qtdeContaParcela, Integer qtdeMatricula, String[] matriculas, Double valorTotalDesconto, String agrupador) throws Exception {
		
		for (ListagemDescontosAlunosPorTipoDescontoRelVO desconto : listagemDescontosAlunosVO.getListagemDescontosAlunosPorTipoDescontoRelVOs()) {
			if (desconto.getCodigoDesconto().equals(codigoDesconto) && desconto.getTipoDesconto().equals(origemDesconto)) {	
				desconto.setQtdeAlunoDescontoMatricula(desconto.getQtdeAlunoDescontoMatricula() + qtdeContaMatricula);
				desconto.setValorTotalDescontoMatricula(desconto.getValorTotalDescontoMatricula() + valorDescontoMatricula);
				desconto.setQtdeAlunoDescontoMensalidade(desconto.getQtdeAlunoDescontoMensalidade() + qtdeContaParcela);
				desconto.setValorTotalDescontoMensalidade(desconto.getValorTotalDescontoMensalidade() + valorDescontoParcela);								
				desconto.setValorDesconto(desconto.getValorDesconto() + valorTotalDesconto);				
				adicionarValorTotalDesconto(listagemDescontosAlunosVO, origemDesconto, valorDescontoMatricula, qtdeContaMatricula, valorDescontoParcela, qtdeContaParcela, matriculas);				
				return;
			}
		}
		ListagemDescontosAlunosPorTipoDescontoRelVO desconto = new ListagemDescontosAlunosPorTipoDescontoRelVO(origemDesconto, codigoDesconto, nomeDesconto, valorDescontoMatricula, qtdeContaMatricula, valorDescontoParcela, qtdeContaParcela, qtdeMatricula, valorTotalDesconto, agrupador);		
		adicionarValorTotalDesconto(listagemDescontosAlunosVO, origemDesconto, valorDescontoMatricula, qtdeContaMatricula, valorDescontoParcela, qtdeContaParcela, matriculas);
		listagemDescontosAlunosVO.getListagemDescontosAlunosPorTipoDescontoRelVOs().add(desconto);
	}

	public void adicionarValorTotalDesconto(ListagemDescontosAlunosRelVO listagemDescontosAlunosVO, String origemDesconto, Double valorDescontoMatricula, Integer qtdeContaMatricula, Double valorDescontoParcela, Integer qtdeContaParcela, String[] matriculas) {
			
			if(origemDesconto.equals("Aluno")) {
				listagemDescontosAlunosVO.setValorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalPlanoFinanceiroAlunoMatriculaCalculadoAluno() + valorDescontoMatricula));
				listagemDescontosAlunosVO.setTotalAlunoPlanoFinanceiroAlunoMatricula(listagemDescontosAlunosVO.getTotalAlunoPlanoFinanceiroAlunoMatricula()+qtdeContaMatricula);
				listagemDescontosAlunosVO.setValorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalPlanoFinanceiroAlunoParcelaCalculadoAluno() + valorDescontoParcela));
				listagemDescontosAlunosVO.setTotalAlunoPlanoFinanceiroAlunoParcela(listagemDescontosAlunosVO.getTotalAlunoPlanoFinanceiroAlunoParcela()+qtdeContaParcela);
				for(String matricula: matriculas) {
					if(!listagemDescontosAlunosVO.getMatriculaComDescontoDescontoAluno().contains(matricula)) {
						listagemDescontosAlunosVO.getMatriculaComDescontoDescontoAluno().add(matricula);	
					}
				}
			}else if(origemDesconto.equals("Progressivo")) {
				listagemDescontosAlunosVO.setValorTotalDescontoProgressivoMatriculaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalDescontoProgressivoMatriculaCalculadoAluno() + valorDescontoMatricula));
				listagemDescontosAlunosVO.setTotalAlunoDescontoProgressivoMatricula(listagemDescontosAlunosVO.getTotalAlunoDescontoProgressivoMatricula()+qtdeContaMatricula);
				listagemDescontosAlunosVO.setValorTotalDescontoProgressivoParcelaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalDescontoProgressivoParcelaCalculadoAluno() + valorDescontoParcela));
				listagemDescontosAlunosVO.setTotalAlunoDescontoProgressivoParcela(listagemDescontosAlunosVO.getTotalAlunoDescontoProgressivoParcela()+qtdeContaParcela);
				for(String matricula: matriculas) {
					if(!listagemDescontosAlunosVO.getMatriculaComDescontoDescontoProgressivo().contains(matricula)) {
						listagemDescontosAlunosVO.getMatriculaComDescontoDescontoProgressivo().add(matricula);	
					}
				}
			}else if(origemDesconto.equals("Institucional")) {
				listagemDescontosAlunosVO.setValorTotalPlanoDescontoMatriculaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalPlanoDescontoMatriculaCalculadoAluno() + valorDescontoMatricula));
				listagemDescontosAlunosVO.setTotalAlunoPlanoDescontoMatricula(listagemDescontosAlunosVO.getTotalAlunoPlanoDescontoMatricula()+qtdeContaMatricula);
				listagemDescontosAlunosVO.setValorTotalPlanoDescontoParcelaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalPlanoDescontoParcelaCalculadoAluno() + valorDescontoParcela));
				listagemDescontosAlunosVO.setTotalAlunoPlanoDescontoParcela(listagemDescontosAlunosVO.getTotalAlunoPlanoDescontoParcela()+qtdeContaParcela);
				for(String matricula: matriculas) {
					if(!listagemDescontosAlunosVO.getMatriculaComDescontoPlanoDesconto().contains(matricula)) {
						listagemDescontosAlunosVO.getMatriculaComDescontoPlanoDesconto().add(matricula);	
					}
				}
			}else if(origemDesconto.equals("Convenio")) {
				listagemDescontosAlunosVO.setValorTotalConvenioMatriculaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalConvenioMatriculaCalculadoAluno() + valorDescontoMatricula));
				listagemDescontosAlunosVO.setTotalAlunoConvenioMatricula(listagemDescontosAlunosVO.getTotalAlunoConvenioMatricula()+qtdeContaMatricula);
				listagemDescontosAlunosVO.setValorTotalConvenioParcelaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalConvenioParcelaCalculadoAluno() + valorDescontoParcela));
				listagemDescontosAlunosVO.setTotalAlunoConvenioParcela(listagemDescontosAlunosVO.getTotalAlunoConvenioParcela()+qtdeContaParcela);
				for(String matricula: matriculas) {
					if(!listagemDescontosAlunosVO.getMatriculaComDescontoConvenio().contains(matricula)) {
						listagemDescontosAlunosVO.getMatriculaComDescontoConvenio().add(matricula);	
					}
				}
			}else if(origemDesconto.equals("Rateio")) {
				listagemDescontosAlunosVO.setValorTotalDescontoRateioMatriculaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalDescontoRateioMatriculaCalculadoAluno() + valorDescontoMatricula));
				listagemDescontosAlunosVO.setTotalAlunoDescontoRateioMatricula(listagemDescontosAlunosVO.getTotalAlunoDescontoRateioMatricula()+qtdeContaMatricula);
				listagemDescontosAlunosVO.setValorTotalDescontoRateioParcelaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalDescontoRateioParcelaCalculadoAluno() + valorDescontoParcela));
				listagemDescontosAlunosVO.setTotalAlunoDescontoRateioParcela(listagemDescontosAlunosVO.getTotalAlunoDescontoRateioParcela()+qtdeContaParcela);
				for(String matricula: matriculas) {
					if(!listagemDescontosAlunosVO.getMatriculaComDescontoDescontoRateio().contains(matricula)) {
						listagemDescontosAlunosVO.getMatriculaComDescontoDescontoRateio().add(matricula);	
					}
				}
			}else if(origemDesconto.equals("Recebimento")) {
				for(String matricula: matriculas) {
					if(!listagemDescontosAlunosVO.getMatriculaComDescontoDescontoRateio().contains(matricula)) {
						listagemDescontosAlunosVO.getMatriculaComDescontoDescontoRateio().add(matricula);	
					}
				}
				listagemDescontosAlunosVO.setValorTotalDescontoRecebimentoMatriculaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalDescontoRecebimentoMatriculaCalculadoAluno() + valorDescontoMatricula));
				listagemDescontosAlunosVO.setTotalAlunoDescontoRecebimentoMatricula(listagemDescontosAlunosVO.getTotalAlunoDescontoRecebimentoMatricula()+qtdeContaMatricula);
				listagemDescontosAlunosVO.setValorTotalDescontoRecebimentoParcelaCalculadoAluno(Uteis.arrendondarForcando2CadasDecimais(listagemDescontosAlunosVO.getValorTotalDescontoRecebimentoParcelaCalculadoAluno() + valorDescontoParcela));
				listagemDescontosAlunosVO.setTotalAlunoDescontoRecebimentoParcela(listagemDescontosAlunosVO.getTotalAlunoDescontoRecebimentoParcela()+qtdeContaParcela);
			}
			for(String matricula: matriculas) {
				if(!listagemDescontosAlunosVO.getMatriculaComDesconto().contains(matricula)) {
					listagemDescontosAlunosVO.getMatriculaComDesconto().add(matricula);	
				}
			}
		
	}
	
	public void adicionarTotalizadorGeral(List<ListagemDescontosAlunosPorTipoDescontoRelVO> listagemTotalDescontosRelVOs, String agrupador, Double valorDescontoMatricula, Integer qtdeContaMatricula, Double valorDescontoParcela, Integer qtdeContaParcela, Integer qtdeMatricula, Double valorTotalDesconto, String[] matriculas) {
		for(ListagemDescontosAlunosPorTipoDescontoRelVO listagemDescontosAlunosPorTipoDescontoRelVO : listagemTotalDescontosRelVOs) {
			if(listagemDescontosAlunosPorTipoDescontoRelVO.getNomeDesconto().equals(agrupador)) {				
				listagemDescontosAlunosPorTipoDescontoRelVO.setValorDesconto(valorTotalDesconto);
				listagemDescontosAlunosPorTipoDescontoRelVO.setValorTotalDescontoMatricula(Uteis.arrendondarForcando2CadasDecimais(valorDescontoMatricula + listagemDescontosAlunosPorTipoDescontoRelVO.getValorTotalDescontoMatricula()));
				listagemDescontosAlunosPorTipoDescontoRelVO.setValorTotalDescontoMensalidade(Uteis.arrendondarForcando2CadasDecimais(valorDescontoParcela + listagemDescontosAlunosPorTipoDescontoRelVO.getValorTotalDescontoMensalidade()));
				listagemDescontosAlunosPorTipoDescontoRelVO.setQtdeAlunoDescontoMatricula(qtdeContaMatricula+listagemDescontosAlunosPorTipoDescontoRelVO.getQtdeAlunoDescontoMatricula());
				listagemDescontosAlunosPorTipoDescontoRelVO.setQtdeAlunoDescontoMensalidade(qtdeContaParcela+listagemDescontosAlunosPorTipoDescontoRelVO.getQtdeAlunoDescontoMensalidade());
				for(String matricula: matriculas) {
					if(!listagemDescontosAlunosPorTipoDescontoRelVO.getMatriculaComDesconto().contains(matricula)) {
						listagemDescontosAlunosPorTipoDescontoRelVO.getMatriculaComDesconto().add(matricula);
					}
				}
				return;
			}
		}
		ListagemDescontosAlunosPorTipoDescontoRelVO listagemDescontosAlunosPorTipoDescontoRelVO =  new ListagemDescontosAlunosPorTipoDescontoRelVO();
		listagemDescontosAlunosPorTipoDescontoRelVO.setNomeDesconto(agrupador);
		listagemDescontosAlunosPorTipoDescontoRelVO.setAgrupador(agrupador);
		listagemDescontosAlunosPorTipoDescontoRelVO.setValorDesconto(valorTotalDesconto);
		listagemDescontosAlunosPorTipoDescontoRelVO.setValorTotalDescontoMatricula(valorDescontoMatricula);
		listagemDescontosAlunosPorTipoDescontoRelVO.setValorTotalDescontoMensalidade(valorDescontoParcela);
		listagemDescontosAlunosPorTipoDescontoRelVO.setQtdeAlunoDescontoMatricula(qtdeContaMatricula);
		listagemDescontosAlunosPorTipoDescontoRelVO.setQtdeAlunoDescontoMensalidade(qtdeContaParcela);
		for(String matricula: matriculas) {
			if(!listagemDescontosAlunosPorTipoDescontoRelVO.getMatriculaComDesconto().contains(matricula)) {
				listagemDescontosAlunosPorTipoDescontoRelVO.getMatriculaComDesconto().add(matricula);
			}
		}
		listagemTotalDescontosRelVOs.add(listagemDescontosAlunosPorTipoDescontoRelVO);
		
	}

	public SqlRowSet executarConsultaParametrizada2(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, String ano, String semestre, Integer descontoProgressivo, Integer planoDesconto, Integer convenio, List<UnidadeEnsinoVO> unidadeEnsinoVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer parceiro, Integer categoriaDesconto,  PeriodicidadeEnum periodicidadeEnum, Date dataInicio, Date dataTermino, String tipoLayout, Boolean apresentarDescontoRateio, Boolean apresentarDescontoAluno, Boolean apresentarDescontoRecebimento) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (tipoLayout.equals("ListagemDescontosAlunosSintetico")) {
			sqlStr.append(" select unidadeensino, codigounidadeensino, tipodesconto, codigodesconto, descricaodesconto, agrupador, sum(valordescontomatricula) as valordescontomatricula, ");
			sqlStr.append(" sum(qtdeContaMatricula) as qtdeContaMatricula, sum(valordescontoparcela) as valordescontoparcela, sum(qtdeContaParcela) as qtdeContaParcela, count(distinct matricula) as qtdeMatricula, array_to_string(array_agg(distinct matricula), ',') as matricula, sum(valorTotalDesconto) as valorTotalDesconto  ");
			sqlStr.append(" from ( ");
		}else {
			sqlStr.append(" select * from ( ");
		}
		Boolean union = false;
		if (apresentarDescontoRateio) {
			
			sqlStr.append(" select  matricula.*, 'Rateio' as tipodesconto, -1::bigint as codigodesconto, ");
			sqlStr.append("  'Desconto Rateio'::varchar as descricaodesconto, 'Desconto Rateio'::varchar as agrupador, ");		
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then contareceber.valordescontorateio else 0 end) as valordescontomatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then 1 else 0 end ) as qtdeContaMatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then contareceber.valordescontorateio else 0 end) as valordescontoparcela, ");
			sqlStr.append("  sum(contareceber.valordescontorateio) as valorTotalDesconto, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then 1 else 0 end ) as qtdeContaParcela ");	
			sqlStr.append("  from contareceber");
			sqlStr.append(realizarObtencaoJoinMatricula(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino));		
			sqlStr.append("  where (contareceber.tipoorigem = 'MAT' or  contareceber.tipoorigem = 'MEN') and contareceber.valordescontorateio > 0");
			sqlStr.append("  group by matricula.unidadeensino, matricula.curso, matricula.turno, matricula.turma, matricula.matricula, matricula.aluno, matricula.matriculaperiodo, codigounidadeensino ");
			sqlStr.append(" ");
//			sqlStr.append("  union all");
			union = true;
		}
		if (apresentarDescontoRecebimento) {
			if (union) {
				sqlStr.append("  union all");
			}
			sqlStr.append(" select  matricula.*, 'Recebimento' as tipodesconto, -2::bigint as codigodesconto, ");
			sqlStr.append("  'Desconto Lançado Recebimento'::varchar as descricaodesconto, 'Desconto Lançado Recebimento'::varchar as agrupador, ");		
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then contareceber.valorcalculadodescontolancadorecebimento else 0 end) as valordescontomatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then 1 else 0 end ) as qtdeContaMatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then contareceber.valorcalculadodescontolancadorecebimento else 0 end) as valordescontoparcela, ");
			sqlStr.append("  sum(contareceber.valorcalculadodescontolancadorecebimento) as valorTotalDesconto, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then 1 else 0 end ) as qtdeContaParcela ");	
			sqlStr.append("  from contareceber");
			sqlStr.append(realizarObtencaoJoinMatricula(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino));		
			sqlStr.append("  where (contareceber.tipoorigem = 'MAT' or  contareceber.tipoorigem = 'MEN') and contareceber.valorcalculadodescontolancadorecebimento > 0  and contareceber.situacao = 'RE'");
			sqlStr.append("  group by matricula.unidadeensino, matricula.curso, matricula.turno, matricula.turma, matricula.matricula, matricula.aluno, matricula.matriculaperiodo, codigounidadeensino ");
			sqlStr.append(" ");
			union = true;
		}
		if (apresentarDescontoAluno) {
			if (union) {
				sqlStr.append("  union all");
			}
//		sqlStr.append("  union all");
			sqlStr.append("  select  matricula.*, 'Aluno' as tipodesconto, 0::bigint as codigodesconto, ");
			sqlStr.append("  'Desconto Aluno'::varchar as descricaodesconto, 'Desconto Aluno'::varchar as agrupador, ");		
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then contareceber.valordescontoalunojacalculado else 0 end) as valordescontomatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then 1 else 0 end ) as qtdeContaMatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then contareceber.valordescontoalunojacalculado else 0 end) as valordescontoparcela, ");
			sqlStr.append("  sum(contareceber.valordescontoalunojacalculado) as valorTotalDesconto, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then 1 else 0 end ) as qtdeContaParcela ");	
			sqlStr.append("  from contareceber");
			sqlStr.append(realizarObtencaoJoinMatricula(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino));		
			sqlStr.append("  where (contareceber.tipoorigem = 'MAT' or  contareceber.tipoorigem = 'MEN') and contareceber.valordescontoalunojacalculado > 0");
			sqlStr.append("  group by matricula.unidadeensino, matricula.curso, matricula.turno, matricula.turma, matricula.matricula, matricula.aluno, matricula.matriculaperiodo, codigounidadeensino ");
			sqlStr.append(" ");
			union = true;
		}
		if (!descontoProgressivo.equals(-1)) {
			if (union) {
				sqlStr.append("  union all");
			}
//		sqlStr.append("  union all");
			sqlStr.append(" ");
			sqlStr.append("  select  matricula.*, 'Progressivo'::varchar as tipodesconto, descontoprogressivo.codigo::bigint as codigodesconto, ");
			sqlStr.append("  descontoprogressivo.nome as descricaodesconto, 'Desconto Progressivo'::varchar as agrupador, ");		
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then contareceber.valordescontoprogressivo else 0 end) as valordescontomatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then 1 else 0 end ) as qtdeContaMatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then contareceber.valordescontoprogressivo else 0 end) as valordescontoparcela, ");
			sqlStr.append("  sum(contareceber.valordescontoprogressivo) as valorTotalDesconto, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then 1 else 0 end ) as qtdeContaParcela ");
			sqlStr.append("  from contareceber");
			sqlStr.append(realizarObtencaoJoinMatricula(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino));
			sqlStr.append("  inner join descontoprogressivo on descontoprogressivo.codigo = contareceber.descontoprogressivo");
			sqlStr.append("  where (contareceber.tipoorigem = 'MAT' or  contareceber.tipoorigem = 'MEN') and contareceber.valordescontoprogressivo > 0");
			sqlStr.append("  group by matricula.unidadeensino, matricula.curso, matricula.turno, matricula.turma, matricula.matricula, matricula.aluno, matricula.matriculaperiodo, descontoprogressivo.codigo, descontoprogressivo.nome, codigounidadeensino ");
			sqlStr.append(" ");
			union = true;
		}
//		sqlStr.append("  union all");
		if (!convenio.equals(-1)) {
			if (union) {
				sqlStr.append("  union all");
			}
			sqlStr.append(" ");
			sqlStr.append("  select matricula.*, 'Convenio'::varchar  as tipodesconto, ");
			sqlStr.append("  convenio.codigo::bigint as codigodesconto, ");
			sqlStr.append("  convenio.descricao as descricaodesconto, parceiro.nome as agrupador, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then planodescontocontareceber.valorutilizadorecebimento else 0 end) as valordescontomatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then 1 else 0 end ) as qtdeContaMatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then planodescontocontareceber.valorutilizadorecebimento else 0 end) as valordescontoparcela, ");
			sqlStr.append("  sum(planodescontocontareceber.valorutilizadorecebimento) as valorTotalDesconto, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then 1 else 0 end ) as qtdeContaParcela ");		
			sqlStr.append("  from contareceber");
			sqlStr.append(realizarObtencaoJoinMatricula(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino));
			sqlStr.append("  inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo");
			sqlStr.append("  inner join convenio on planodescontocontareceber.convenio = convenio.codigo");
			sqlStr.append("  inner join parceiro on parceiro.codigo = convenio.parceiro");
			sqlStr.append("  left join planodesconto on planodescontocontareceber.planodesconto = planodesconto.codigo");
			sqlStr.append("  where (contareceber.tipoorigem = 'MAT' or  contareceber.tipoorigem = 'MEN') and planodescontocontareceber.valorutilizadorecebimento > 0");
			if (Uteis.isAtributoPreenchido(parceiro)) {
				sqlStr.append("  and 	parceiro.codigo = ").append(parceiro);
			}
			if (convenio > 0 && Uteis.isAtributoPreenchido(convenio)) {
				sqlStr.append("  and 	convenio.codigo = ").append(convenio);
			}
			sqlStr.append("  group by matricula.unidadeensino, matricula.curso, matricula.turno, matricula.turma, matricula.matricula, matricula.aluno, matricula.matriculaperiodo, convenio.codigo, convenio.descricao, codigounidadeensino, parceiro.nome "); 
			sqlStr.append(" ");
			union = true;
		}
		if (!planoDesconto.equals(-1)) {
			if (union) {
				sqlStr.append("  union all");
			}
//		sqlStr.append("  union all ");
			sqlStr.append(" ");
			sqlStr.append("  select matricula.*, 'Institucional'::varchar as tipodesconto, ");
			sqlStr.append("  planodesconto.codigo::bigint as codigodesconto,");
			sqlStr.append("  planodesconto.nome as descricaodesconto, case when categoriaDesconto.codigo is null then 'Desconto Institucional'::varchar else categoriaDesconto.nome end as agrupador, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then planodescontocontareceber.valorutilizadorecebimento else 0 end) as valordescontomatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then 1 else 0 end ) as qtdeContaMatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then planodescontocontareceber.valorutilizadorecebimento else 0 end) as valordescontoparcela, ");
			sqlStr.append("  sum(planodescontocontareceber.valorutilizadorecebimento) as valorTotalDesconto, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then 1 else 0 end ) as qtdeContaParcela ");		
			sqlStr.append("  from contareceber");	
			sqlStr.append("  inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo");
			sqlStr.append("  inner join planodesconto on planodescontocontareceber.planodesconto = planodesconto.codigo");
			sqlStr.append("  left join categoriaDesconto on categoriaDesconto.codigo = planodesconto.categoriaDesconto");
			sqlStr.append(realizarObtencaoJoinMatricula(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino));
			sqlStr.append("  where (contareceber.tipoorigem = 'MAT' or  contareceber.tipoorigem = 'MEN') and planodescontocontareceber.valorutilizadorecebimento > 0");
			if (Uteis.isAtributoPreenchido(categoriaDesconto)) {
				sqlStr.append("  and 	categoriaDesconto.codigo = ").append(categoriaDesconto);
			}
			if (planoDesconto > 0 && Uteis.isAtributoPreenchido(planoDesconto)) {
				sqlStr.append("  and 	planodesconto.codigo = ").append(planoDesconto);
			}
			sqlStr.append("  group by matricula.unidadeensino, matricula.curso, matricula.turno, matricula.turma, matricula.matricula, matricula.aluno, matricula.matriculaperiodo, planodesconto.codigo, planodesconto.nome, codigounidadeensino, case when categoriaDesconto.codigo is null then 'Desconto Institucional' else categoriaDesconto.nome end ");

			if (!Uteis.isAtributoPreenchido(categoriaDesconto) && !(planoDesconto > 0)) {
			sqlStr.append("  union all ");
			sqlStr.append(" ");
			sqlStr.append("  select matricula.*, 'Institucional'::varchar as tipodesconto, ");
			sqlStr.append("  ('x'||substr(md5(planodescontocontareceber.nome),1,8))::bit(32)::bigint as codigodesconto,");
			sqlStr.append("  planodescontocontareceber.nome as descricaodesconto, 'Desconto Institucional Manual'::varchar as agrupador, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then planodescontocontareceber.valorutilizadorecebimento else 0 end) as valordescontomatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MAT' then 1 else 0 end ) as qtdeContaMatricula, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then planodescontocontareceber.valorutilizadorecebimento else 0 end) as valordescontoparcela, ");
			sqlStr.append("  sum(planodescontocontareceber.valorutilizadorecebimento) as valorTotalDesconto, ");
			sqlStr.append("  sum(case when contareceber.tipoorigem = 'MEN' then 1 else 0 end ) as qtdeContaParcela ");		
			sqlStr.append("  from contareceber");	
			sqlStr.append("  inner join planodescontocontareceber on planodescontocontareceber.contareceber = contareceber.codigo and planodescontocontareceber.tipoitemplanofinanceiro = 'DM' and planodescontocontareceber.planodesconto is null ");
			sqlStr.append(realizarObtencaoJoinMatricula(unidadeEnsinoCursoVO, turmaVO, ano, semestre, descontoProgressivo, planoDesconto, convenio, unidadeEnsinoVOs, filtroRelatorioAcademicoVO, parceiro, categoriaDesconto, periodicidadeEnum, dataInicio, dataTermino));
			sqlStr.append("  where (contareceber.tipoorigem = 'MAT' or  contareceber.tipoorigem = 'MEN') and planodescontocontareceber.valorutilizadorecebimento > 0");
			
			sqlStr.append("  group by matricula.unidadeensino, matricula.curso, matricula.turno, matricula.turma, matricula.matricula, matricula.aluno, matricula.matriculaperiodo, planodescontocontareceber.codigo, planodescontocontareceber.nome, codigounidadeensino ");
			}
		}
		sqlStr.append("  ) as conta ");
		if (tipoLayout.equals("ListagemDescontosAlunosSintetico")) {
			sqlStr.append("  group by unidadeensino, codigounidadeensino, tipodesconto, codigodesconto,  descricaodesconto, agrupador "); 
			sqlStr.append("  order by unidadeensino, codigounidadeensino, agrupador, descricaodesconto ");
		}else {
			sqlStr.append("  order by unidadeensino, curso, turno, turma, matricula, aluno,  agrupador, descricaodesconto ");
		}	
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}
	
 
	
	private StringBuilder realizarObtencaoJoinMatricula(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, TurmaVO turmaVO, String ano, String semestre, Integer descontoProgressivo, Integer planoDesconto, Integer convenio, List<UnidadeEnsinoVO> unidadeEnsinoVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer parceiro, Integer categoriaDesconto,  PeriodicidadeEnum periodicidadeEnum, Date dataInicio, Date dataTermino) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" inner join (select distinct unidadeensino.nome as unidadeensino, curso.nome as curso, turno.nome as turno, turma.identificadorturma as turma, ");
		sqlStr.append(" matricula.matricula as matricula, pessoa.nome as aluno, matriculaperiodo.codigo as matriculaperiodo, unidadeensino.codigo as codigounidadeensino ");
		sqlStr.append(" from matricula ");		
		sqlStr.append(" inner join matriculaPeriodo on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" and matriculaPeriodo.codigo = (select codigo from matriculaPeriodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' ");
		if (periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)){
			sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "mp.data", false));
		}
		if (!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL) && Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and mp.ano = '").append(ano).append("'");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and mp.semestre = '").append(semestre).append("'");
		}
		sqlStr.append(" order by (mp.ano||mp.semestre) desc, case when mp.situacaomatriculaperiodo = 'AT' or  mp.situacaomatriculaperiodo = 'PR' or  mp.situacaomatriculaperiodo = 'FI' then 1 else 2 end limit 1) "); 
		sqlStr.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");		
		sqlStr.append("inner join pessoa on matricula.aluno = pessoa.codigo ");
		sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append("inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append("inner join turma on turma.codigo = matriculaperiodo.turma ");	
		sqlStr.append("where 1= 1 ");
		if(!descontoProgressivo.equals(-1) 
				&& !Uteis.isAtributoPreenchido(categoriaDesconto) 
				&& !planoDesconto.equals(-1)
				&& !convenio.equals(-1)
				&& !Uteis.isAtributoPreenchido(parceiro)) {
			sqlStr.append(" and exists (select codigo from contareceber as cr where cr.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" and (cr.tipoorigem = 'MAT' or cr.tipoorigem = 'MEN') ");
			sqlStr.append(" and ((cr.valordescontoprogressivo is not null and cr.valordescontoprogressivo > 0) or (cr.valordescontoalunojacalculado is not null and cr.valordescontoalunojacalculado > 0) ");
			sqlStr.append(" or (cr.valordescontorateio is not null and cr.valordescontorateio > 0) ");
			sqlStr.append(" or (cr.valorcalculadodescontolancadorecebimento is not null and cr.valorcalculadodescontolancadorecebimento > 0 and cr.situacao = 'RE') ");
			sqlStr.append(" or exists (select pd.codigo from planodescontocontareceber pd where cr.codigo = pd.contareceber ");
			sqlStr.append(" and pd.valorutilizadorecebimento is not null and pd.valorutilizadorecebimento > 0 limit 1)) ");
			sqlStr.append("  limit 1) ");
		}
		if (!unidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and unidadeensino.codigo in (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		sqlStr.append(" and curso.periodicidade = '").append(periodicidadeEnum.getValor()).append("' ");		
		if (periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)){
			sqlStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "matriculaperiodo.data", false));
		}
		if (!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL) && Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and matriculaperiodo.ano = '").append(ano).append("'");
		}
		if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL) && Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and matriculaperiodo.semestre = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO.getCurso())) {
			sqlStr.append(" AND curso.codigo = ").append(unidadeEnsinoCursoVO.getCurso().getCodigo());
			if (Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO.getTurno())) {
				sqlStr.append(" AND matricula.turno = ").append(unidadeEnsinoCursoVO.getTurno().getCodigo());
			}
		}
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(" AND matriculaPeriodo.turma = ").append(turmaVO.getCodigo());
		}
		String andOr = " and ( ";
		if (!descontoProgressivo.equals(-1)) {
			sqlStr.append(andOr).append(" exists(select cr.codigo from contareceber as cr where cr.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" and (cr.tipoorigem = 'MAT' or cr.tipoorigem = 'MEN') ");
			if (!descontoProgressivo.equals(0)) {
				sqlStr.append(" and cr.descontoprogressivo = ").append(descontoProgressivo);
			}
			sqlStr.append(" and cr.valordescontoprogressivo > 0) ");
			andOr = " or ";
		}
		if (!planoDesconto.equals(-1) && Uteis.isAtributoPreenchido(categoriaDesconto)) {
			sqlStr.append(andOr).append(" exists(select pdcr.codigo from planodescontocontareceber pdcr ");
			sqlStr.append(" inner join contareceber as cr on cr.codigo = pdcr.contareceber ");
			sqlStr.append(" inner join planodesconto as pd on pd.codigo = pdcr.planodesconto ");
			sqlStr.append(" where cr.matriculaperiodo = matriculaperiodo.codigo and (cr.tipoorigem = 'MAT' or cr.tipoorigem = 'MEN') ");
			sqlStr.append(" AND pd.categoriaDesconto = ").append(categoriaDesconto);
			if (planoDesconto > 0) {
				sqlStr.append(" AND pd.codigo = ").append(planoDesconto);
			}
			sqlStr.append(" and pdcr.valorutilizadorecebimento > 0 ");
			sqlStr.append(" limit 1) ");
			andOr = " or ";
		}
		if ((!planoDesconto.equals(-1) && !Uteis.isAtributoPreenchido(categoriaDesconto))) {
			sqlStr.append(andOr).append(" exists(select pdcr.codigo from planodescontocontareceber pdcr ");
			sqlStr.append(" inner join contareceber as cr on cr.codigo = pdcr.contareceber ");
			sqlStr.append(" inner join planodesconto as pd on pd.codigo = pdcr.planodesconto ");
			sqlStr.append(" where cr.matriculaperiodo = matriculaperiodo.codigo and (cr.tipoorigem = 'MAT' or cr.tipoorigem = 'MEN') ");
			sqlStr.append(" and pdcr.valorutilizadorecebimento > 0 ");
			if (planoDesconto > 0) {
				sqlStr.append(" AND pd.codigo = ").append(planoDesconto);
			}
			sqlStr.append(" limit 1) ");
			andOr = " or ";
		}
		if (!convenio.equals(-1) && !Uteis.isAtributoPreenchido(parceiro)) {
			sqlStr.append(andOr).append(" exists(select  pdcr.codigo from planodescontocontareceber as pdcr ");
			sqlStr.append(" inner join contareceber cr on cr.codigo = pdcr.contareceber ");			
			sqlStr.append(" where cr.matriculaperiodo = matriculaperiodo.codigo and (cr.tipoorigem = 'MAT' or cr.tipoorigem = 'MEN') ");
			sqlStr.append(" and pdcr.valorutilizadorecebimento > 0 ");
			if (convenio > 0) {
				sqlStr.append(" AND pdcr.convenio = ").append(convenio);
			}else {
				sqlStr.append(" AND pdcr.convenio is not null ");
			}
			sqlStr.append(" limit 1) ");
			andOr = " or ";
		}
		if (!convenio.equals(-1) && Uteis.isAtributoPreenchido(parceiro)) {
			sqlStr.append(andOr).append(" exists(select  pdcr.codigo from planodescontocontareceber as pdcr ");
			sqlStr.append(" inner join contareceber as cr on cr.codigo = pdcr.contareceber ");
			sqlStr.append(" inner join convenio as c on c.codigo = pdcr.convenio ");
			sqlStr.append(" where cr.matriculaperiodo = matriculaperiodo.codigo and (cr.tipoorigem = 'MAT' or cr.tipoorigem = 'MEN') ");
			sqlStr.append(" and pdcr.valorutilizadorecebimento > 0 ");
			sqlStr.append(" AND c.parceiro = ").append(parceiro);
			if (convenio > 0) {
				sqlStr.append(" AND pdcr.convenio = ").append(convenio);
			}
			sqlStr.append(" limit 1) ");
			andOr = " or ";
		}
		if(andOr.equals(" or ")) {
			sqlStr.append(") ");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" ) as matricula on matricula.matriculaperiodo = contareceber.matriculaperiodo ");
		return sqlStr;
	} 

}
