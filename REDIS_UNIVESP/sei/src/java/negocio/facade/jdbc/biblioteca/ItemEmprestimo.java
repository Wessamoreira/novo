package negocio.facade.jdbc.biblioteca;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TurnoHorarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.ReservaVO;
import negocio.comuns.biblioteca.enumeradores.SituacaoReservaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.SituacaoItemEmprestimo;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ItemEmprestimoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ItemEmprestimoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>ItemEmprestimoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ItemEmprestimoVO
 * @see ControleAcesso
 * @see Emprestimo
 */
@Repository
@Scope("singleton")
@Lazy
public class ItemEmprestimo extends ControleAcesso implements ItemEmprestimoInterfaceFacade {

	protected static String idEntidade;

	public ItemEmprestimo() throws Exception {
		super();
		setIdEntidade("Emprestimo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>ItemEmprestimoVO</code>.
	 */
	public ItemEmprestimoVO novo() throws Exception {
		ItemEmprestimo.incluir(getIdEntidade());
		ItemEmprestimoVO obj = new ItemEmprestimoVO();
		return obj;
	}

	public Integer calcularNrExemplaresEmprestadosParaUmaCatalogo(ItemEmprestimoVO itemEmprestimoVO) throws Exception {
		String sqlStr = "SELECT COUNT (*) FROM itememprestimo INNER JOIN exemplar ON itememprestimo.exemplar = exemplar.codigo WHERE itememprestimo.situacao <> 'DE' AND exemplar.obra = " + itemEmprestimoVO.getExemplar().getCatalogo().getCodigo() + " ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (new Integer(tabelaResultado.getInt(1)));
	}

	/**
	 * Esse método inicializa dos dados de um novo empréstimo com uma lista de
	 * exemplares em mão, depois inclui no banco esse novo registro de
	 * empréstimo.
	 * 
	 * @param emprestimoVO
	 * @param listaExemplares
	 * @param configuracaoBibliotecaVO
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializarDadosNovoEmprestimoDosItensEmprestimo(EmprestimoVO emprestimoVO, List<ItemEmprestimoVO> listaItemEmprestimo, ConfiguracaoBibliotecaVO confPadraoBib, ConfiguracaoFinanceiroVO configuracaoFinanceiro, List<String> listaMensagem, UsuarioVO usuario) throws Exception {
		String mensagemSucesso = "";
		emprestimoVO.getItemEmprestimoVOs().clear();
		for (ItemEmprestimoVO itemEmprestimoVO : listaItemEmprestimo) {
			Date dataEmprestimo = itemEmprestimoVO.getEmprestimo().getData(); 
			itemEmprestimoVO.setEmprestimo(emprestimoVO);
			if (itemEmprestimoVO.getTipoEmprestimo().equals("HR")) {
				itemEmprestimoVO.getEmprestimo().setData(dataEmprestimo);
			}

			if (getFacadeFactory().getExemplarFacade().verificarExisteEmprestimoParaDeterminadoExemplar(itemEmprestimoVO.getExemplar().getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplarEmprestado"));
			}
			if (itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
				Integer diaDaSemana = Uteis.getDiaSemana(new Date());
				if (diaDaSemana != 6 && diaDaSemana != 7 && diaDaSemana != 1 && !getFacadeFactory().getFeriadoFacade().validarDataSeVesperaFimDeSemana(new Date(), itemEmprestimoVO.getExemplar().getBiblioteca().getCidade().getCodigo(), confPadraoBib.getConsiderarSabadoDiaUtil(), confPadraoBib.getConsiderarDomingoDiaUtil(), ConsiderarFeriadoEnum.BIBLIOTECA)) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplarEmprestadoSomenteVesperaFeriadoOuFinalSemana").replace("{0}", itemEmprestimoVO.getExemplar().getCodigoBarra()).replace("{1}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo()));
				}
			}
			
			if(!confPadraoBib.getEmprestimoRenovacaoComDebitos()) {
				if (itemEmprestimoVO.getEmprestar()) {
					if (emprestimoVO.getValorTotalMulta() > 0) {
						emprestimoVO.setValorTotalMulta(0.0);
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroPendenciasFinanceirasEmprestimo"));
					}
				}
			}
			
			Integer numeroExemplaresDisponiveis = getFacadeFactory().getReservaFacade().consultarNumeroDeExemplaresDisponiveisPorCatalogo(itemEmprestimoVO.getExemplar().getCatalogo(), itemEmprestimoVO.getExemplar().getBiblioteca(), confPadraoBib, true);
			Integer numeroReservasValidas = getFacadeFactory().getReservaFacade().consultarQuantidadeDeReservasValidasPorCatalogo(itemEmprestimoVO.getExemplar().getCatalogo(), itemEmprestimoVO.getExemplar().getBiblioteca());
			
			if (numeroExemplaresDisponiveis <= numeroReservasValidas && numeroReservasValidas > 0) {
//				if (!getFacadeFactory().getReservaFacade().verificarExistenciaReservaCatalogoParaDeterminadaPessoa(itemEmprestimoVO.getExemplar().getCatalogo().getCodigo(), emprestimoVO.getPessoa().getCodigo(), itemEmprestimoVO.getExemplar().getBiblioteca().getCodigo())) {
//					throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplaresReservados").replace("{0}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo()));
//				}			
//				if (numeroReservasValidas > 0) {
					List<ReservaVO> listaReservaCatalogo = getFacadeFactory().getReservaFacade().consultarReservasPorCatalogoPessoa(itemEmprestimoVO.getExemplar().getCatalogo(), itemEmprestimoVO.getExemplar().getBiblioteca(), null, usuario, numeroExemplaresDisponiveis);
					if(!listaReservaCatalogo.isEmpty()){
					//List<ReservaVO> listaReservaPessoa = getFacadeFactory().getReservaFacade().consultarReservasPorCatalogoPessoa(null, itemEmprestimoVO.getExemplar().getBiblioteca(), emprestimoVO.getPessoa(), usuario);			
					//for (ReservaVO reservaPessoa : listaReservaPessoa) {
						boolean reservaDisponivel = false;
						for (ReservaVO reservaCatalogo : listaReservaCatalogo) {
							if (emprestimoVO.getPessoa().getCodigo().equals(reservaCatalogo.getPessoa().getCodigo())) {
								reservaDisponivel = true;
								break;
							}
						}
						if (!reservaDisponivel) {
							throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroCatalogoReservado").replace("{0}", listaReservaCatalogo.get(0).getPessoa().getNome()).replace("{1}", Uteis.getData(listaReservaCatalogo.get(0).getDataTerminoReserva())));
						}
					}
					//}
			}
			CidadeVO cidadeBibliotecaVO = consultarCidadeBiblioteca(emprestimoVO.getBiblioteca().getCodigo(), usuario);
			if (!itemEmprestimoVO.getTipoEmprestimo().equals("HR")) {
				realizarCalculoDataPrevisaoDevolucaoExemplar(itemEmprestimoVO, TipoPessoa.getEnum(emprestimoVO.getTipoPessoa()), emprestimoVO.getBiblioteca(), confPadraoBib, false, cidadeBibliotecaVO, usuario);
			}
			emprestimoVO.getItemEmprestimoVOs().add(itemEmprestimoVO);
			itemEmprestimoVO = null;
		}
		emprestimoVO.getAtendente().setCodigo(usuario.getCodigo());
		emprestimoVO.getAtendente().setNome(usuario.getNome());
		getFacadeFactory().getEmprestimoFacade().incluir(emprestimoVO, false, confPadraoBib, configuracaoFinanceiro, usuario);
		for (ItemEmprestimoVO itemEmprestimoVO : listaItemEmprestimo) {
			mensagemSucesso = UteisJSF.internacionalizar("msg_Biblioteca_EmprestadoSucesso").replace("{0}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo()).replace("{1}", itemEmprestimoVO.getExemplar().getCodigoBarra());
			listaMensagem.add(mensagemSucesso);
			if (itemEmprestimoVO.getExemplar().getExemplarSelecionadoDeUmaReserva()) {
				getFacadeFactory().getReservaFacade().executarVinculoDaReservaParaUmEmprestimo(emprestimoVO.getCodigo(), emprestimoVO.getPessoa().getCodigo(), itemEmprestimoVO.getExemplar().getCatalogo().getCodigo());
			}
		}
		// return mensagemSucesso;
	}

	/*@Override
	public void realizarCalculoDataPrevisaoDevolucaoExemplar(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO confPadraoBib, Boolean utilizarDataDevolucaoTemp, CidadeVO cidadeBibliotecaVO, UsuarioVO usuarioVO) throws Exception {
		Date dataPrevisaoDevolucao = null;
		Date dataParaCalculo = Uteis.getDateSemHora(new Date());
		if (itemEmprestimoVO.getEmprestar() || itemEmprestimoVO.getRenovarEmprestimo()) {
			if (confPadraoBib.getUtilizarApenasDiasUteisEmprestimo()) {
				if (!itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
					if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataParaCalculo, confPadraoBib.getPrazoEmpresAluno(), cidadeBibliotecaVO.getCodigo(), true));
					} else if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataParaCalculo, confPadraoBib.getPrazoEmpresProfessor(), cidadeBibliotecaVO.getCodigo(), true));
					} else if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataParaCalculo, confPadraoBib.getPrazoEmpresFuncionario(), cidadeBibliotecaVO.getCodigo(), true));
					}
				} else {
					if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataParaCalculo, confPadraoBib.getPrazoEmprestimoAlunoFinalDeSemana(), cidadeBibliotecaVO.getCodigo(), true));
					} else if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataParaCalculo, confPadraoBib.getPrazoEmprestimoProfessorFinalDeSemana(), cidadeBibliotecaVO.getCodigo(), true));
					} else if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataParaCalculo, confPadraoBib.getPrazoEmprestimoFuncionarioFinalDeSemana(), cidadeBibliotecaVO.getCodigo(), true));
					}
				}
			} else {
				if (!itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
					if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, confPadraoBib.getPrazoEmpresAluno());
					} else if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, confPadraoBib.getPrazoEmpresProfessor());
					} else if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, confPadraoBib.getPrazoEmpresFuncionario());
					}
				} else {
					if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, confPadraoBib.getPrazoEmprestimoAlunoFinalDeSemana());
					} else if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, confPadraoBib.getPrazoEmprestimoProfessorFinalDeSemana());
					} else if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
						dataPrevisaoDevolucao = Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, confPadraoBib.getPrazoEmprestimoFuncionarioFinalDeSemana());
					}
				}
			}
			if (utilizarDataDevolucaoTemp) {
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(dataPrevisaoDevolucao);
			} else {
				itemEmprestimoVO.setDataPrevisaoDevolucao(dataPrevisaoDevolucao);
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(dataPrevisaoDevolucao);				
			}
		}
	}*/
	
	
	@Override
	public void realizarCalculoDataPrevisaoDevolucaoExemplar(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO confPadraoBib, Boolean utilizarDataDevolucaoTemp, CidadeVO cidadeBibliotecaVO, UsuarioVO usuarioVO) throws Exception {
		Date dataParaCalculo = null;
		Date dataPrevisaoDevolucao = null;
//		boolean teste = false;
//		if (teste) {
//			dataParaCalculo = Uteis.getDataPrimeiroDiaMes(Uteis.getDateSemHora(Uteis.getData("30/01/2018", "dd/MM/yyyy")));
//			int nrdias = 7;
//			for (int x = 0; x <= 60; x++) {
//				dataPrevisaoDevolucao = getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(dataParaCalculo, nrdias, cidadeBibliotecaVO.getCodigo(), confPadraoBib.getConsiderarSabadoDiaUtil(), confPadraoBib.getConsiderarDomingoDiaUtil(), ConsiderarFeriadoEnum.BIBLIOTECA);
//				System.out.println("Emprestimo dia " + Uteis.getData(dataParaCalculo) + "(" + Uteis.getDiaSemana(dataParaCalculo) + ") - previsao dia " + Uteis.getData(dataPrevisaoDevolucao) + "(" + Uteis.getDiaSemana(dataPrevisaoDevolucao) + ")");
//				dataParaCalculo = UteisData.obterDataFuturaUsandoCalendar(dataParaCalculo, 1);
//			}
//		}			
		dataParaCalculo = Uteis.getDateSemHora(new Date());
		Integer nrDias=0;
		if (itemEmprestimoVO.getEmprestar() || itemEmprestimoVO.getRenovarEmprestimo()) {
			if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
				if (!itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
					nrDias = confPadraoBib.getPrazoEmpresAluno();
				}else{
					nrDias = confPadraoBib.getPrazoEmprestimoAlunoFinalDeSemana();
				}
			} else if (tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE)) {
				if (!itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
					nrDias = confPadraoBib.getPrazoEmpresVisitante();
				}else{
					nrDias = confPadraoBib.getPrazoEmprestimoVisitanteFinalDeSemana();
				}
			} else if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
				if (!itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
					nrDias = confPadraoBib.getPrazoEmpresProfessor();
				}else{
					nrDias = confPadraoBib.getPrazoEmprestimoProfessorFinalDeSemana();
				}
			} else if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
				if (!itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
					nrDias = confPadraoBib.getPrazoEmpresFuncionario();
				}else{
					nrDias = confPadraoBib.getPrazoEmprestimoFuncionarioFinalDeSemana();
				}
			}
			if (confPadraoBib.getUtilizarApenasDiasUteisEmprestimo()) {
				dataPrevisaoDevolucao = getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(dataParaCalculo, nrDias, cidadeBibliotecaVO.getCodigo(), confPadraoBib.getConsiderarSabadoDiaUtil(), confPadraoBib.getConsiderarDomingoDiaUtil(), ConsiderarFeriadoEnum.BIBLIOTECA);
			} else {
				dataPrevisaoDevolucao =  UteisData.obterDataFuturaUsandoCalendar(dataParaCalculo, nrDias);
			}
			if (utilizarDataDevolucaoTemp) {
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(dataPrevisaoDevolucao);
			} else {
				itemEmprestimoVO.setDataPrevisaoDevolucao(dataPrevisaoDevolucao);
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(dataPrevisaoDevolucao);				
			}
		}
	}


	public void realizarCalculoDataPrevisaoDevolucaoExemplarHora(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, BibliotecaVO bibliotecaVO, ConfiguracaoBibliotecaVO confPadraoBib, Boolean utilizarDataDevolucaoTemp, CidadeVO cidadeBibliotecaVO, UsuarioVO usuarioVO) throws Exception {
		Date dataPrevisaoDevolucao = null;
		Date dataParaCalculo = itemEmprestimoVO.getEmprestimo().getData();
		if (itemEmprestimoVO.getEmprestar() || itemEmprestimoVO.getRenovarEmprestimo()) {
			if (confPadraoBib.getUtilizarApenasDiasUteisEmprestimo()) {
				dataPrevisaoDevolucao = calcularDataPrevisaoDevHora(dataParaCalculo, bibliotecaVO, true, cidadeBibliotecaVO, itemEmprestimoVO);

			} else {				
				dataPrevisaoDevolucao = calcularDataPrevisaoDevHora(dataParaCalculo, bibliotecaVO, false, cidadeBibliotecaVO, itemEmprestimoVO);
			}
			if (utilizarDataDevolucaoTemp) {
				itemEmprestimoVO.setDataPrevistaDevolucaoTemp(dataPrevisaoDevolucao);
				itemEmprestimoVO.setDataPrevisaoDevolucao(dataPrevisaoDevolucao);
			} else {
				itemEmprestimoVO.setDataPrevisaoDevolucao(dataPrevisaoDevolucao);
			}
		}
	}

	public Date calcularDataPrevisaoDevHora(Date dataPrevisaoDevolucao, BibliotecaVO bibliotecaVO, Boolean diasUteis, CidadeVO cidadeBiblioteca, ItemEmprestimoVO itemEmprestimoVO) throws Exception {
		Date dataPrevisaoDevolucao2 = null;
		if (Uteis.getObterDiferencaDiasEntreDuasData(Uteis.getDateSemHora(dataPrevisaoDevolucao), Uteis.getDateSemHora(new Date())) > 0) {
			dataPrevisaoDevolucao2 = dataPrevisaoDevolucao;
			Date dataFutura = UteisData.getDataFuturaAvancandoHora(dataPrevisaoDevolucao, itemEmprestimoVO.getHorasEmprestimo());
			DiaSemana diaSemanaTemp = Uteis.getDiaSemanaEnum(dataPrevisaoDevolucao2);
			TurnoHorarioVO turnoHorarioTemp = getFacadeFactory().getTurnoHorarioFacade().consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca(bibliotecaVO.getTurno().getCodigo(), "", diaSemanaTemp.getValor());
			if (!turnoHorarioTemp.getHorarioInicioAula().equals("") && Uteis.obterDataComHora(Uteis.getDateSemHora(dataFutura), turnoHorarioTemp.getHorarioInicioAula()).before(dataFutura)) {
				dataPrevisaoDevolucao2 = dataFutura; 
			} else {
				if (!turnoHorarioTemp.getHorarioInicioAula().equals("")) {
					dataPrevisaoDevolucao2 = Uteis.obterDataComHora(Uteis.getDateSemHora(dataFutura), turnoHorarioTemp.getHorarioInicioAula());
					if (dataFutura.before(dataPrevisaoDevolucao2)) {
						dataPrevisaoDevolucao2 = dataFutura; 
					}
				} else {
					dataPrevisaoDevolucao2 = dataFutura;
				}
			}
		} else {
			dataPrevisaoDevolucao2 = Uteis.getDataAdicionadaEmHoras(dataPrevisaoDevolucao, itemEmprestimoVO.getHorasEmprestimo());
		}
		if (bibliotecaVO.getTurno().getCodigo().intValue() > 0) {
			DiaSemana diaSemana = Uteis.getDiaSemanaEnum(dataPrevisaoDevolucao2);
			String horario = Uteis.gethoraHHMM(dataPrevisaoDevolucao2);
			TurnoHorarioVO turnoHorario = null;
			// verifica se esta aberto a biblioteca.
			turnoHorario = getFacadeFactory().getTurnoHorarioFacade().consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca(bibliotecaVO.getTurno().getCodigo(), horario, diaSemana.getValor());
			if (turnoHorario.getCodigo().intValue() == 0) {
				// se nao esta aberto verifica se estará aberto a mais tarde no
				// mesmo dia.
				turnoHorario = getFacadeFactory().getTurnoHorarioFacade().consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxHorario(bibliotecaVO.getTurno().getCodigo(), horario, diaSemana.getValor());
				if (turnoHorario.getCodigo().intValue() == 0) {
					// se nao estará aberto no mesmo dia, verifica proximo dia
					// aberto considerando minutos restantes
					// setar horario inicial abertura para dataprevisao.

					// obter ultima horario aberto.
					turnoHorario = getFacadeFactory().getTurnoHorarioFacade().consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_UltimoHorarioDia(bibliotecaVO.getTurno().getCodigo(), horario, diaSemana.getValor());
					if (turnoHorario.getCodigo().intValue() == 0) {
						Date dataFutura = UteisData.getDataFuturaAvancandoHora(dataPrevisaoDevolucao, itemEmprestimoVO.getHorasEmprestimo());
						Date dataFuturaAvancada = Uteis.obterDataAvancada(dataFutura, 1);
						//Date dataFuturaAvancada = Uteis.obterDataAvancada(dataPrevisaoDevolucao, 1);
						dataFuturaAvancada = Uteis.obterDataComHora(dataFuturaAvancada, "01:00");
						int valorHoras = itemEmprestimoVO.getHorasEmprestimo();
						itemEmprestimoVO.setHorasEmprestimo(0);
						if (Uteis.getDateSemHora(dataFutura).before(Uteis.getDateSemHora(new Date()))) {
							if (dataFutura.before(dataFuturaAvancada)) {
								dataPrevisaoDevolucao2 = dataFutura;
							} else {
								dataPrevisaoDevolucao2 = dataFuturaAvancada;
							}
						} else {
							dataPrevisaoDevolucao2 = dataFuturaAvancada;							
						}
						Date dataReturn = this.calcularDataPrevisaoDevHora(dataPrevisaoDevolucao2, bibliotecaVO, diasUteis, cidadeBiblioteca, itemEmprestimoVO);
						itemEmprestimoVO.setHorasEmprestimo(valorHoras);
						return dataReturn;
						//throw new Exception("Não foi localizado um turno definido para a biblioteca que trate o dia do emprestimo. Verifique o cadastro de turno!");
					}
					String horarioFinal = turnoHorario.getHorarioFinalAula(); // 18:00

					Date dataPrevisaoInicial = Uteis.getDateSemHora(dataPrevisaoDevolucao2);
					dataPrevisaoInicial = Uteis.obterDataComHora(dataPrevisaoInicial, horarioFinal);

					turnoHorario = getFacadeFactory().getTurnoHorarioFacade().consultaRapidaPorDiaSemanaPeriodoFuncionamentoBiblioteca_ProxDiaHorario(bibliotecaVO.getTurno().getCodigo(), diaSemana.getValor());
					if (turnoHorario.getCodigo().intValue() != 0) {
						String horaInicio = turnoHorario.getHorarioInicioAula();
						dataPrevisaoDevolucao2 = Uteis.obterDataAvancada(dataPrevisaoDevolucao2, 1);
						dataPrevisaoDevolucao2 = Uteis.getDateSemHora(dataPrevisaoDevolucao2);
						dataPrevisaoDevolucao2 = Uteis.obterDataComHora(dataPrevisaoDevolucao2, horaInicio);

						dataPrevisaoDevolucao2 = Uteis.obterDiferencaHorasDuasDatasMilisegundos_AcrescentandoEmDataFutura(dataPrevisaoInicial, dataPrevisaoDevolucao2, dataPrevisaoDevolucao2);
					}
				} 
				
				// setar horario inicial abertura para dataprevisao.
				dataPrevisaoDevolucao2 = Uteis.getDateSemHora(dataPrevisaoDevolucao2);
				dataPrevisaoDevolucao2 = Uteis.obterDataComHora(dataPrevisaoDevolucao2, turnoHorario.getHorarioInicioAula());
			}
			// Verifica feriado e defini data util.
			if (diasUteis) {
				Integer qtdDias = getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataPrevisaoDevolucao2, 0, cidadeBiblioteca.getCodigo(), true, ConsiderarFeriadoEnum.BIBLIOTECA);
				if (qtdDias > 0) {
					dataPrevisaoDevolucao2 = Uteis.obterDataAvancada(dataPrevisaoDevolucao2, qtdDias);
					return this.calcularDataPrevisaoDevHora(dataPrevisaoDevolucao2, bibliotecaVO, diasUteis, cidadeBiblioteca, itemEmprestimoVO);
				}
			}			
			if (Uteis.getObterDiferencaDiasEntreDuasData(Uteis.getDateSemHora(dataPrevisaoDevolucao2), Uteis.getDateSemHora(new Date())) > 0) {
				String horarioInicial = turnoHorario.getHorarioInicioAula();
				Date dataPrevisaoInicial = Uteis.getDateSemHora(dataPrevisaoDevolucao2);
				if (Uteis.obterDataComHora(dataPrevisaoInicial, horarioInicial).before(dataPrevisaoDevolucao2)) {
					dataPrevisaoDevolucao2 = dataPrevisaoDevolucao2;
				} else {
					dataPrevisaoDevolucao2 = Uteis.obterDataComHora(dataPrevisaoInicial, horarioInicial);					
				}					
			}
		}
		return dataPrevisaoDevolucao2;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void inicializarDadosDevolucaoRenovacaoDosItensEmprestimo(EmprestimoVO emprestimo, List<ItemEmprestimoVO> listaItensEmprestimo, ConfiguracaoBibliotecaVO confPadraoBib, ConfiguracaoFinanceiroVO configuracaoFinanceiro, List<String> listaMensagem, UsuarioVO usuario) throws Exception {
		EmprestimoVO emprestimoParaDevolucao = new EmprestimoVO();
		EmprestimoVO emprestimoParaRenovacao = (EmprestimoVO)emprestimo.clone();
		emprestimoParaRenovacao.setNovoObj(true);
		emprestimoParaRenovacao.setCodigo(0);
		emprestimoParaRenovacao.setItemEmprestimoVOs(new ArrayList<ItemEmprestimoVO>(0));
		emprestimoParaRenovacao.getAtendente().setCodigo(usuario.getCodigo());
		emprestimoParaRenovacao.getAtendente().setNome(usuario.getNome());
		emprestimoParaRenovacao.setData(new Date());
		emprestimoParaRenovacao.setValorTotalMulta(0.0);
		String mensagemSucesso = "";
		for (ItemEmprestimoVO itemEmprestimoVO : listaItensEmprestimo) {
			if (itemEmprestimoVO.getRenovarEmprestimo() && itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroRenovarExemplarEmprestimoEspecial").replace("{0}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo()).replace("{1}", itemEmprestimoVO.getExemplar().getCodigoBarra()));
			} 
			
			if(!confPadraoBib.getEmprestimoRenovacaoComDebitos()) {
				if (itemEmprestimoVO.getRenovarEmprestimo()) {
					getFacadeFactory().getEmprestimoFacade().realizarCalculoMultaDevolucaoItemEmprestimo(itemEmprestimoVO, TipoPessoa.getEnum(emprestimoParaRenovacao.getTipoPessoa()), confPadraoBib, emprestimo.getBiblioteca().getCidade(), usuario);
					if (itemEmprestimoVO.getValorMulta() > 0) {
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroRenovarExemplarExistenciaDebitos").replace("{0}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo()).replace("{1}", itemEmprestimoVO.getExemplar().getCodigoBarra()));
					}
					if (emprestimo.getValorTotalMulta() > 0) {
						emprestimo.setValorTotalMulta(0.0);
						throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroPendenciasFinanceirasRenovacao"));
					}
				}
			}
			
			itemEmprestimoVO.setDataDevolucao(new Date());
			itemEmprestimoVO.getResponsavelDevolucao().setCodigo(usuario.getCodigo());
			itemEmprestimoVO.getResponsavelDevolucao().setNome(usuario.getNome());
			itemEmprestimoVO.setSituacao(SituacaoItemEmprestimo.DEVOLVIDO.getValor());
			getFacadeFactory().getExemplarFacade().executarAlteracaoSituacaoExemplares(itemEmprestimoVO.getExemplar(), SituacaoExemplar.DISPONIVEL.getValor(), usuario);
			
			ReservaVO reservaVO = getFacadeFactory().getReservaFacade().consultarReservaPorEmprestimoCatalogoPessoa(itemEmprestimoVO.getEmprestimo().getCodigo(), itemEmprestimoVO.getExemplar().getCatalogo().getCodigo(), emprestimoParaRenovacao.getPessoa().getCodigo(), usuario);
			if(reservaVO != null && !reservaVO.getCodigo().equals(0)) {
				getFacadeFactory().getReservaFacade().alterarSituacaoReserva(reservaVO, SituacaoReservaEnum.FINALIZADO.getKey(), usuario);
			}
			
			emprestimoParaDevolucao = getFacadeFactory().getEmprestimoFacade().consultarPorChavePrimaria(itemEmprestimoVO.getEmprestimo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			itemEmprestimoVO.setEmprestimo(emprestimoParaDevolucao);
			getFacadeFactory().getExemplarFacade().alterarEstadoExemplares(itemEmprestimoVO.getExemplar(), usuario);
			if (itemEmprestimoVO.getDevolverEmprestimo()) {
				getFacadeFactory().getHistoricoExemplarFacade().registrarHistoricoExemplarDevolucao(emprestimoParaDevolucao, itemEmprestimoVO);
				getFacadeFactory().getItensRenovadosFacade().excluirItensRenovados(emprestimoParaDevolucao, itemEmprestimoVO);
			}
			executarAlteracaoSituacaoItensEmprestimo(itemEmprestimoVO, itemEmprestimoVO.getSituacao());
			getFacadeFactory().getEmprestimoFacade().verificarSeTodosItensEmprestimoForamDevolvidosRenovados(itemEmprestimoVO.getEmprestimo().getCodigo());
			if (itemEmprestimoVO.getDevolverEmprestimo()) {
				mensagemSucesso = UteisJSF.internacionalizar("msg_Biblioteca_DevolvidoSucesso").replace("{0}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo()).replace("{1}", itemEmprestimoVO.getExemplar().getCodigoBarra());
				listaMensagem.add(mensagemSucesso);
				
				if (getFacadeFactory().getReservaFacade().consultarNumeroDeExemplaresDisponiveisPorCatalogo(itemEmprestimoVO.getExemplar().getCatalogo(), itemEmprestimoVO.getExemplar().getBiblioteca(), confPadraoBib, false) > 0) {
					ReservaVO reservaAnterior = new ReservaVO();
					reservaAnterior.setCatalogo(itemEmprestimoVO.getExemplar().getCatalogo());
					reservaAnterior.setPessoa(emprestimoParaRenovacao.getPessoa());
					reservaAnterior.setTipoPessoa(emprestimoParaRenovacao.getTipoPessoa());
					reservaAnterior.setBibliotecaVO(itemEmprestimoVO.getExemplar().getBiblioteca());
					getFacadeFactory().getReservaFacade().executarAlterarDataTerminoReservaDataReservaMaisAntigaPorCatalogoEEnviaMensagemReservaDisponivel(itemEmprestimoVO.getExemplar().getCatalogo(), reservaAnterior, confPadraoBib, usuario);
				}
			}
			if (itemEmprestimoVO.getRenovarEmprestimo()) {				
				ItemEmprestimoVO itemEmprestimoRenovacao = null;
				itemEmprestimoRenovacao = montarItemEmprestimoRenovacao(itemEmprestimoVO);
				itemEmprestimoRenovacao.setCodigo(0);
				CidadeVO cidadeBibliotecaVO = consultarCidadeBiblioteca(emprestimoParaRenovacao.getBiblioteca().getCodigo(), usuario);
				if (!itemEmprestimoRenovacao.getTipoEmprestimo().equals("HR")) {
					realizarCalculoDataPrevisaoDevolucaoExemplar(itemEmprestimoRenovacao, TipoPessoa.getEnum(emprestimoParaRenovacao.getTipoPessoa()), emprestimoParaRenovacao.getBiblioteca(), confPadraoBib, false, cidadeBibliotecaVO, usuario);
				}
				emprestimoParaRenovacao.getItemEmprestimoVOs().add(itemEmprestimoRenovacao);
				itemEmprestimoRenovacao.setEmprestimo(emprestimoParaRenovacao);
				itemEmprestimoRenovacao.setAlterado(true);
				//getFacadeFactory().getExemplarFacade().executarAlteracaoSituacaoExemplares(itemEmprestimoRenovacao.getExemplar(), SituacaoExemplar.EMPRESTADO.getValor(), usuario);
				mensagemSucesso = UteisJSF.internacionalizar("msg_Biblioteca_RenovadoSucesso").replace("{0}", itemEmprestimoVO.getExemplar().getCatalogo().getTitulo()).replace("{1}", itemEmprestimoVO.getExemplar().getCodigoBarra());
				listaMensagem.add(mensagemSucesso);
			}
		}
		if (!emprestimoParaRenovacao.getItemEmprestimoVOs().isEmpty()) {
			getFacadeFactory().getEmprestimoFacade().incluir(emprestimoParaRenovacao, true, confPadraoBib, configuracaoFinanceiro, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ItemEmprestimoVO montarItemEmprestimoRenovacao(ItemEmprestimoVO itemEmprestimoAntigo) throws CloneNotSupportedException {
		ItemEmprestimoVO itemEmprestimoNovo = (ItemEmprestimoVO) itemEmprestimoAntigo.clone();
		itemEmprestimoNovo.setCodigo(0);
		itemEmprestimoNovo.setValorMulta(0.0);
		itemEmprestimoNovo.setValorIsencao(0.0);
		itemEmprestimoNovo.setIsentarCobrancaMulta(false);
		itemEmprestimoNovo.setResponsavelDevolucao(null);
		itemEmprestimoNovo.setDataDevolucao(null);
		itemEmprestimoNovo.setSituacao(SituacaoItemEmprestimo.EM_EXECUCAO.getValor());
		return itemEmprestimoNovo;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ItemEmprestimoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemEmprestimoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemEmprestimoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			ItemEmprestimoVO.validarDados(obj);
			/**
			 * @author Leonardo Riciolle 
			 * Comentado 28/10/2014
			 *  Classe Subordinada
			 */
			// ItemEmprestimo.incluir(getIdEntidade());
			final String sql = "INSERT INTO ItemEmprestimo( emprestimo, exemplar, situacao, dataDevolucao, dataPrevisaoDevolucao, valorMulta, responsavelDevolucao, renovadoPeloSolicitante, bibliotecadevolvida, tipoEmprestimo, horasEmprestimo ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				// getConexao().getJdbcTemplate().update(new
				// PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getEmprestimo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getEmprestimo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getExemplar().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getExemplar().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getSituacao());
					if(obj.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())){
						sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(obj.getDataDevolucao()));
					}else{
						sqlInserir.setNull(4, 0);
					}
					sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(obj.getDataPrevisaoDevolucao()));
					if(obj.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())){
						sqlInserir.setDouble(6, obj.getValorMulta().doubleValue());
					}else{
						sqlInserir.setDouble(6, 0.0);
					}
					if (obj.getResponsavelDevolucao().getCodigo() != 0) {
						sqlInserir.setInt(7, obj.getResponsavelDevolucao().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					sqlInserir.setBoolean(8, obj.getRenovadoPeloSolicitante());
					sqlInserir.setInt(9, obj.getBibliotecaDevolvida());
					sqlInserir.setString(10, obj.getTipoEmprestimo());
					sqlInserir.setInt(11, obj.getHorasEmprestimo());					
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));

			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			if (e.getMessage().contains("check_exemplar_disponivel_emprestimo")) {
				throw new Exception(UteisJSF.internacionalizar("msg_Biblioteca_ErroExemplarIndisponivelEmprestimo").replace("{0}", obj.getExemplar().getCodigoBarra())); 
			}
			obj.setNovoObj(true);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ItemEmprestimoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemEmprestimoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemEmprestimoVO obj) throws Exception {
		try {
			ItemEmprestimoVO.validarDados(obj);
			/**
			 * @author Leonardo Riciolle 
			 * Comentado 28/10/2014
			 *  Classe Subordinada
			 */
			// ItemEmprestimo.alterar(getIdEntidade());
			final String sql = " UPDATE ItemEmprestimo set situacao=?, dataDevolucao=?, dataPrevisaoDevolucao=?, valorMulta=?, " 
							 + " emprestimo = ?, exemplar = ?, responsaveldevolucao=?, dataprimeiranotificacao=?, datasegundanotificacao=?, dataterceiranotificacao=?, "
							 + " isentarCobrancaMulta = ?, valorIsencao = ?, renovadoPeloSolicitante=?, bibliotecadevolvida=?, tipoEmprestimo=?, horasEmprestimo=? " 
							 + " WHERE codigo = ?";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao());
					if(obj.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())){
						sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataDevolucao()));
					}else{
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataPrevisaoDevolucao()));
					if(obj.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())){
						sqlAlterar.setDouble(4, obj.getValorMulta().doubleValue());
					}else{
						sqlAlterar.setDouble(4, 0.0);
					}
					if (obj.getEmprestimo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getEmprestimo().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getExemplar().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getExemplar().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor()) && obj.getResponsavelDevolucao().getCodigo() != 0) {
						sqlAlterar.setInt(7, obj.getResponsavelDevolucao().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (obj.getDataPrimeiraNotificacao() != null) {
						sqlAlterar.setDate(8, Uteis.getDataJDBC(obj.getDataPrimeiraNotificacao()));
					} else {
						sqlAlterar.setNull(8, 0);
					}
					if (obj.getDataSegundaNotificacao() != null) {
						sqlAlterar.setDate(9, Uteis.getDataJDBC(obj.getDataSegundaNotificacao()));
					} else {
						sqlAlterar.setNull(9, 0);
					}
					if (obj.getDataTerceiraNotificacao() != null) {
						sqlAlterar.setDate(10, Uteis.getDataJDBC(obj.getDataTerceiraNotificacao()));
					} else {
						sqlAlterar.setNull(10, 0);
					}
					if(obj.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())){
						sqlAlterar.setBoolean(11, obj.getIsentarCobrancaMulta());
						sqlAlterar.setDouble(12, obj.getValorIsencao().doubleValue());
					}else{
						sqlAlterar.setBoolean(11, false);
						sqlAlterar.setDouble(12, 0.0);
					}
					sqlAlterar.setBoolean(13, obj.getRenovadoPeloSolicitante());
					sqlAlterar.setInt(14, obj.getBibliotecaDevolvida());
					sqlAlterar.setString(15, obj.getTipoEmprestimo());
					sqlAlterar.setInt(16, obj.getHorasEmprestimo());
					sqlAlterar.setInt(17, obj.getCodigo());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ItemEmprestimoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemEmprestimoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ItemEmprestimoVO obj) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle 
			 * Comentado 28/10/2014
			 *  Classe Subordinada
			 */
			// ItemEmprestimo.excluir(getIdEntidade());
			String sql = "DELETE FROM ItemEmprestimo WHERE ((emprestimo = ?) and (exemplar = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getEmprestimo(), obj.getExemplar().getCodigo() });

		} catch (Exception e) {
			throw e;
		}
	}
	
	
	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemEmprestimoVO</code> resultantes da consulta.
	 */
	public static List<ItemEmprestimoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ItemEmprestimoVO> vetResultado = new ArrayList<ItemEmprestimoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ItemEmprestimoVO</code>.
	 * 
	 * @return O objeto da classe <code>ItemEmprestimoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static ItemEmprestimoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemEmprestimoVO obj = new ItemEmprestimoVO();
		
		obj.getEmprestimo().setCodigo(dadosSQL.getInt("emprestimo"));
		obj.getEmprestimo().setData(dadosSQL.getTimestamp("emprestimo.data"));		
		obj.getEmprestimo().setTipoPessoa(dadosSQL.getString("emprestimo.tipopessoa"));
		obj.getEmprestimo().getPessoa().setCodigo(dadosSQL.getInt("emprestimo.pessoa"));
		obj.getEmprestimo().getPessoa().setNome(dadosSQL.getString("pessoa.nome"));
		obj.getEmprestimo().getAtendente().setCodigo(dadosSQL.getInt("emprestimo.atendente"));
		obj.getEmprestimo().getAtendente().setNome(dadosSQL.getString("atendente.nome"));
		obj.getEmprestimo().getMatricula().setMatricula(dadosSQL.getString("emprestimo.matricula"));
		obj.getEmprestimo().getBiblioteca().setCodigo(dadosSQL.getInt("emprestimo.biblioteca"));	
		obj.getEmprestimo().getBiblioteca().setNome(dadosSQL.getString("biblioteca.nome"));	
		obj.getEmprestimo().getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("emprestimo.unidadeensino"));
		obj.getEmprestimo().getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensino.nome"));
		
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getExemplar().setCodigo(dadosSQL.getInt("exemplar"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setDataDevolucao(dadosSQL.getDate("dataDevolucao"));
		obj.setDataPrevisaoDevolucao(dadosSQL.getTimestamp("dataPrevisaoDevolucao"));
		obj.setDataPrevistaDevolucaoTemp(dadosSQL.getTimestamp("dataPrevisaoDevolucao"));		
		obj.setValorMulta(dadosSQL.getDouble("valorMulta"));
		obj.setValorIsencao(dadosSQL.getDouble("valorIsencao"));
		obj.setIsentarCobrancaMulta(dadosSQL.getBoolean("isentarCobrancaMulta"));
		obj.getResponsavelDevolucao().setCodigo(dadosSQL.getInt("responsavelDevolucao"));
		obj.setRenovadoPeloSolicitante(dadosSQL.getBoolean("renovadoPeloSolicitante"));
		obj.setTipoEmprestimo(dadosSQL.getString("tipoEmprestimo"));
		obj.setHorasEmprestimo(dadosSQL.getInt("horasEmprestimo"));
		obj.setNovoObj(Boolean.FALSE);
		
		montarDadosExemplar(obj, nivelMontarDados, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosResponsavelDevolucao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public static void montarDadosResponsavelDevolucao(ItemEmprestimoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelDevolucao().getCodigo().intValue() == 0) {
			obj.setResponsavelDevolucao(new UsuarioVO());
			return;
		}
		obj.setResponsavelDevolucao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelDevolucao().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>ExemplarVO</code> relacionado ao objeto
	 * <code>ItemEmprestimoVO</code>. Faz uso da chave primária da classe
	 * <code>ExemplarVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosExemplar(ItemEmprestimoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getExemplar().getCodigo().intValue() == 0) {
			obj.setExemplar(new ExemplarVO());
			return;
		}
		obj.setExemplar(getFacadeFactory().getExemplarFacade().consultarPorChavePrimaria(obj.getExemplar().getCodigo(), nivelMontarDados, usuario));
	}	

	/**
	 * Operação que, ao incluir um <code>EmprestimoVO</code> altera a situação
	 * de todos os <code>ItemEmprestimoVO</code> para <b>EM_EXECUCAO</b>.
	 * 
	 * @param itemEmprestimoVOs
	 * @param devolucao
	 * @throws Exception
	 */
	public void alterarSituacaoItensEmprestimoParaEmExecucao(List<ItemEmprestimoVO> itemEmprestimoVOs) throws Exception {
		Iterator e = itemEmprestimoVOs.iterator();
		while (e.hasNext()) {
			ItemEmprestimoVO obj = (ItemEmprestimoVO) e.next();
			obj.setSituacao(SituacaoItemEmprestimo.EM_EXECUCAO.getValor());
		}
	}

	/**
	 * Operação que, ao realizar uma <b>Devolução</b> altera a situação de todos
	 * os <code>ItemEmprestimoVO</code> para <b>DEVOLVIDO</b>.
	 * 
	 * @param itemEmprestimoVOs
	 * @param emprestimoVO
	 * @param devolucao
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoItensEmprestimoParaDevolvido(ItemEmprestimoVO itemEmprestimoVO) throws Exception {
		executarAlteracaoSituacaoItensEmprestimo(itemEmprestimoVO, SituacaoItemEmprestimo.DEVOLVIDO.getValor());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarAlteracaoSituacaoItensEmprestimo(final ItemEmprestimoVO itemEmprestimoVO, final String situacao) throws Exception {

		try {
			final String sql = "UPDATE ItemEmprestimo set situacao=?, valorMulta = ?, isentarCobrancaMulta = ?, dataDevolucao = ?, responsaveldevolucao = ?, valorIsencao=?, renovadoPeloSolicitante=?, bibliotecaDevolvida=?, tipoEmprestimo=?, horasEmprestimo=? , motivoIsencao=? WHERE (codigo = ? )";

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, situacao);
					sqlAlterar.setDouble(2, itemEmprestimoVO.getValorMulta().doubleValue());
					sqlAlterar.setBoolean(3, itemEmprestimoVO.getIsentarCobrancaMulta());
					if (situacao.equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())) {
						sqlAlterar.setDate(4, Uteis.getDataJDBC(itemEmprestimoVO.getDataDevolucao()));
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (itemEmprestimoVO.getResponsavelDevolucao().getCodigo() != 0) {
						sqlAlterar.setInt(5, itemEmprestimoVO.getResponsavelDevolucao().getCodigo());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					sqlAlterar.setDouble(6, itemEmprestimoVO.getValorIsencao().doubleValue());
					sqlAlterar.setBoolean(7, itemEmprestimoVO.getRenovadoPeloSolicitante());
					sqlAlterar.setInt(8, itemEmprestimoVO.getBibliotecaDevolvida());
					sqlAlterar.setString(9, itemEmprestimoVO.getTipoEmprestimo());
					sqlAlterar.setInt(10, itemEmprestimoVO.getHorasEmprestimo());
					sqlAlterar.setString(11, itemEmprestimoVO.getMotivoIsencao());
					sqlAlterar.setInt(12, itemEmprestimoVO.getCodigo());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por incluir objetos da <code>ItemEmprestimoVO</code>
	 * no BD. Garantindo o relacionamento com a entidade principal
	 * <code>biblioteca.Emprestimo</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirItemEmprestimos(Integer emprestimoPrm, List objetos, UsuarioVO usuarioVO) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ItemEmprestimoVO obj = (ItemEmprestimoVO) e.next();
			obj.getEmprestimo().setCodigo(emprestimoPrm);
			if (obj.getCodigo().equals(0)) {
				incluir(obj, usuarioVO);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ItemEmprestimoVO</code>
	 * relacionados a um objeto da classe <code>biblioteca.Emprestimo</code>.
	 * 
	 * @param emprestimo
	 *            Atributo de <code>biblioteca.Emprestimo</code> a ser utilizado
	 *            para localizar os objetos da classe
	 *            <code>ItemEmprestimoVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>ItemEmprestimoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List consultarItemEmprestimos(Integer emprestimo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemEmprestimo.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		String sqlStr = "SELECT * FROM ItemEmprestimo  WHERE emprestimo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { emprestimo });
		while (tabelaResultado.next()) {
			objetos.add(ItemEmprestimo.montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return objetos;
	}
	
	@Override
	public Boolean consultarPessoaPossuePendenciaBiblioteca(Integer pessoa, String matricula, Boolean somenteEmAtraso) throws Exception {
		SqlRowSet tabelaResultado = null;
		String sqlStr = "select itememprestimo.codigo from itememprestimo inner join emprestimo on emprestimo.codigo = itememprestimo.emprestimo where emprestimo.pessoa = ? and itememprestimo.situacao =  'EX'  ";
		if(somenteEmAtraso != null && somenteEmAtraso){
			sqlStr += " and itememprestimo.dataprevisaodevolucao < current_date ";
		}
		if(!matricula.equals("")){
			sqlStr += " and emprestimo.matricula = ? ";
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, pessoa, matricula);
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, pessoa);
		}
		return tabelaResultado.next();
	}

	

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ItemEmprestimo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ItemEmprestimo.idEntidade = idEntidade;
	}

	/**
	 * Método que consulta os exemplares de um empréstimo não finalizado com
	 * base no código da pessoa.
	 * 
	 * @param codigoPessoa
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	public List<ItemEmprestimoVO> consultarItensEmprestadosPorCodigoPessoa(Integer codigoPessoa, Integer biblioteca, String tipoPessoa, int nivelMontarDados, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());
		sqlStr.append(" WHERE emprestimo.pessoa = ").append(codigoPessoa).append(" AND itememprestimo.situacao = 'EX' ");
		if (Uteis.isAtributoPreenchido(configuracaoBiblioteca) && !configuracaoBiblioteca.getLiberaDevolucaoExemplarOutraBiblioteca() && (biblioteca != null && !biblioteca.equals(0))) {
			sqlStr.append(" AND emprestimo.biblioteca = ").append(biblioteca);
		}
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			sqlStr.append(" AND emprestimo.tipopessoa = '").append(tipoPessoa).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<ItemEmprestimoVO> consultarItensEmprestimoPorCodigoEmprestimoJob(Integer codigoEmprestimo) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT DISTINCT ");
		sql.append("itememprestimo.codigo,itememprestimo.dataprevisaodevolucao,itememprestimo.emprestimo,");
		sql.append("catalogo.titulo ");
		sql.append("FROM itememprestimo ");
		sql.append("INNER JOIN exemplar ON exemplar.codigo = itememprestimo.exemplar ");
		sql.append("INNER JOIN catalogo ON catalogo.codigo = exemplar.catalogo ");
		sql.append("WHERE itememprestimo.situacao <> 'DE' AND itememprestimo.datadevolucao is null ");
		sql.append("AND emprestimo = ").append(codigoEmprestimo);
		sql.append(" ORDER BY titulo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosItensJob(tabelaResultado);
	}

	private List<ItemEmprestimoVO> montarDadosItensJob(SqlRowSet dadosSQL) throws Exception {
		List<ItemEmprestimoVO> objs = new ArrayList<ItemEmprestimoVO>(0);
		while (dadosSQL.next()) {
			ItemEmprestimoVO obj = new ItemEmprestimoVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setDataPrevisaoDevolucao(dadosSQL.getDate("dataprevisaodevolucao"));
			obj.setEmprestimo(new EmprestimoVO());
			obj.getEmprestimo().setCodigo(dadosSQL.getInt("emprestimo"));
			obj.setExemplar(new ExemplarVO());
			obj.getExemplar().setTituloExemplar(dadosSQL.getString("titulo"));
			objs.add(obj);
		}
		return objs;
	}

	@Override
	public void registarRenovacaoEmprestimo(String tipoPessoa, ItemEmprestimoVO itemEmprestimoVO, ConfiguracaoBibliotecaVO configuracaoBibliotecaVO, Boolean liberarValidacaoBloqueioBiblioteca, UsuarioVO usuario) throws Exception {
		if(configuracaoBibliotecaVO.getControlaBloqueio(tipoPessoa) && itemEmprestimoVO.getIsEmprestimoAtrasado() && !liberarValidacaoBloqueioBiblioteca){
			throw new Exception(UteisJSF.internacionalizar("msg_Emprestimo_renovarExemplarBloqueioBiblioteca"));
		}
		if (configuracaoBibliotecaVO.getNaoRenovarExemplarIndisponivel() && getFacadeFactory().getExemplarFacade().consultarNrExemplaresCatalogoGravadosDisponiveisParaEmprestimo(itemEmprestimoVO.getExemplar(), "DI") == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_Emprestimo_renovarExemplarIndisponivel"));
		}else if(getFacadeFactory().getReservaFacade().consultarQuantidadeDeReservasValidasPorCatalogo(itemEmprestimoVO.getExemplar().getCatalogo(), itemEmprestimoVO.getExemplar().getBiblioteca())> 0){
			throw new Exception("Existe uma reserva para o catalogo selecionado! Selecione o mesmo utilizando o recurso de adicionar catalogo a partir do quadro de reserva!");
		} else {
			itemEmprestimoVO.setDevolverEmprestimo(false);
			itemEmprestimoVO.setRenovarEmprestimo(true);
		}

	}
	
	@Override
	public List<ItemEmprestimoVO> consultarItemEmprestimoVisaoAlunoProfessor(String valorConsulta, boolean apenasEmprestimosEmAberto, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select itememprestimo.codigo, catalogo.titulo, catalogo.codigo as \"catalogo.codigo\", emprestimo.codigo as \"emprestimo.codigo\", exemplar.codigo as \"exemplar.codigo\", exemplar.emprestarsomentefinaldesemana as \"exemplar.emprestarsomentefinaldesemana\", ");
		sqlStr.append(" exemplar.codigobarra, emprestimo.pessoa, emprestimo.matricula as \"matriculaaluno\", funcionario.matricula as \"matriculafuncionario\", itememprestimo.dataprevisaodevolucao, itememprestimo.datadevolucao, emprestimo.tipopessoa, itememprestimo.valormulta, itememprestimo.situacao, itememprestimo.tipoemprestimo, emprestimo.biblioteca, exemplar.bibliotecaAtual, itememprestimo.contareceber ");
		sqlStr.append(" from itememprestimo ");
		sqlStr.append(" inner join emprestimo on emprestimo.codigo= itememprestimo.emprestimo ");
		sqlStr.append(" inner join exemplar on exemplar.codigo = itememprestimo.exemplar ");
		sqlStr.append(" inner join catalogo on catalogo.codigo = exemplar.catalogo ");
		sqlStr.append(" left join funcionario on funcionario.pessoa = emprestimo.pessoa ");
		sqlStr.append(" left join contareceber on itememprestimo.contareceber = contareceber.codigo ");
		sqlStr.append(" where emprestimo.pessoa = ").append(usuario.getPessoa().getCodigo());
		if(!valorConsulta.equals("")) {
			sqlStr.append(" and catalogo.titulo ilike '%").append(valorConsulta).append("%' ");
		}
		if(apenasEmprestimosEmAberto) {
			sqlStr.append(" and itememprestimo.situacao <> 'DE' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ItemEmprestimoVO> lista = new ArrayList<ItemEmprestimoVO>(0);
		while(tabelaResultado.next()) {
			ItemEmprestimoVO obj = new ItemEmprestimoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNovoObj(false);
			obj.setTipoEmprestimo(tabelaResultado.getString("tipoemprestimo"));			
			obj.getExemplar().setCodigo(tabelaResultado.getInt("exemplar.codigo"));
			obj.getExemplar().getCatalogo().setTitulo(tabelaResultado.getString("titulo"));
			obj.getExemplar().getCatalogo().setCodigo(tabelaResultado.getInt("catalogo.codigo"));
			obj.getExemplar().setCodigoBarra(tabelaResultado.getString("codigobarra"));
			obj.getExemplar().setBibliotecaAtual(tabelaResultado.getInt("bibliotecaAtual"));
			obj.getExemplar().getBiblioteca().setCodigo(tabelaResultado.getInt("biblioteca"));
			obj.getEmprestimo().setCodigo(tabelaResultado.getInt("emprestimo.codigo"));
			obj.getEmprestimo().getPessoa().setCodigo(tabelaResultado.getInt("pessoa"));
			obj.getEmprestimo().setTipoPessoa(tabelaResultado.getString("tipopessoa"));
			obj.getEmprestimo().getBiblioteca().setCodigo(tabelaResultado.getInt("biblioteca"));
			obj.getEmprestimo().getMatricula().setMatricula(tabelaResultado.getString("matriculaaluno"));
			obj.getExemplar().setEmprestarSomenteFinalDeSemana(tabelaResultado.getBoolean("exemplar.emprestarsomentefinaldesemana"));
		
			if(obj.getEmprestimo().getMatricula().getMatricula().equals("")) {
				obj.getEmprestimo().getMatricula().setMatricula(tabelaResultado.getString("matriculafuncionario"));
			}
			
			if(obj.getEmprestimo().getCodigo() > 0){
				obj.getEmprestimo().setNovoObj(false);
			}
			
			obj.setDataPrevisaoDevolucao(tabelaResultado.getTimestamp("dataprevisaodevolucao"));
			obj.setDataDevolucao(tabelaResultado.getDate("datadevolucao"));
			obj.setValorMulta(tabelaResultado.getDouble("valormulta"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			Integer contaReceber = tabelaResultado.getInt("contareceber");
			if (Uteis.isAtributoPreenchido(contaReceber)) {
				calcularValorMultaComJurosMultaContaReceber(contaReceber , obj , usuario);
			}
			lista.add(obj);
		}
		return lista; 
	}
	
	public String consultarPorSituacaoExecucaoAtrasadoRenovadoEPorPessoa(Integer pessoa, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select itememprestimo.codigo, emprestimo.matricula from emprestimo ");
		sb.append(" inner join itememprestimo on itememprestimo.emprestimo = emprestimo.codigo ");
		sb.append(" where pessoa = ").append(pessoa);
		sb.append(" and itememprestimo.situacao in('EX', 'AT', 'RE') limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			String matricula = tabelaResultado.getString("matricula");
			return matricula;
		}
		return "";
	}
	
	public StringBuilder getSqlConsultaCompleta(){
		StringBuilder sql  = new StringBuilder(" select itememprestimo.*, ");
		sql.append(" emprestimo.data as \"emprestimo.data\", emprestimo.tipoPessoa as \"emprestimo.tipoPessoa\", emprestimo.unidadeEnsino as \"emprestimo.unidadeEnsino\", ");
		sql.append(" emprestimo.atendente as \"emprestimo.atendente\", emprestimo.biblioteca as \"emprestimo.biblioteca\", emprestimo.pessoa as \"emprestimo.pessoa\", emprestimo.matricula as \"emprestimo.matricula\", ");
		sql.append(" pessoa.nome as \"pessoa.nome\", biblioteca.nome as \"biblioteca.nome\", atendente.nome as \"atendente.nome\", unidadeensino.nome as \"unidadeensino.nome\" ");						
		sql.append(" from itememprestimo ");				
		sql.append(" inner join emprestimo on emprestimo.codigo = itememprestimo.emprestimo ");
		sql.append(" inner join biblioteca on emprestimo.biblioteca = biblioteca.codigo ");
		sql.append(" inner join pessoa on emprestimo.pessoa = pessoa.codigo ");
		sql.append(" left join unidadeensino on emprestimo.unidadeensino = unidadeensino.codigo ");
		sql.append(" left join usuario as atendente on emprestimo.atendente = atendente.codigo ");
		return sql;
	}
	
	public List<ItemEmprestimoVO> consultarItensEmprestadosOutraBibliotecaPorCodigoPessoa(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer codigoPessoa, Integer biblioteca, int nivelMontarDados, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());		
		sqlStr.append(" WHERE emprestimo.pessoa = ").append(codigoPessoa).append(" AND itememprestimo.situacao = 'EX' ");
		if(!configuracaoBiblioteca.getLiberaDevolucaoExemplarOutraBiblioteca() && (biblioteca != null && !biblioteca.equals(0))) {
			sqlStr.append(" AND emprestimo.biblioteca = ").append(biblioteca);
		}
		sqlStr.append(" AND itememprestimo.codigo NOT IN (0");
		for (ItemEmprestimoVO item : itemEmprestimoVOs) {
			sqlStr.append(", ").append(item.getCodigo());
		}
		sqlStr.append(") ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	/**
	 * @author Wendel Rodrigues
	 * @version 5.0.3.1
	 * @since 20/03/2015
	 * Cria o vínculo com chave estrangeira de ContaReceber para ItemEmprestimo.
	 */
	public void executarVinculoDaContaReceberParaItemEmprestimo(List<ItemEmprestimoVO> listaItensEmprestimo, Integer contaReceber, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" UPDATE itememprestimo SET contareceber = ").append(contaReceber);
		sqlStr.append(" WHERE itememprestimo.codigo IN (0");
		for (ItemEmprestimoVO item : listaItensEmprestimo) {
			sqlStr.append(", ").append(item.getCodigo());
		}
		sqlStr.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	/**
	 * @author Wendel Rodrigues
	 * @version 5.0.3.1
	 * @since 20/03/2015
	 * Caso exclua a ContaReceber será removido a chave estrangeira e concedido a insenção da multa do ItemEmprestimo.
	 * Caso cancele a ContaReceber o vínculo com ItemEmprestimo não é removido e será concedido a insenção da multa.
	 * Caso reativar ContaReceber que foi cancelada o vínculo com ItemEmprestimo não é removido e será cobrado a multa do ItemEmprestimo.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarIsencaoEOuRemoverVinculoDaContaReceberParaItemEmprestimo(Integer contaReceber, Boolean isentarCobrancaMulta, Boolean removerVinculoContaReceber, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" UPDATE itememprestimo SET responsavelisencao = ").append(usuarioVO.getCodigo()).append(", ");
		if (removerVinculoContaReceber) {
			sqlStr.append(" contareceber = null, ");
		}
		if (isentarCobrancaMulta) {
			sqlStr.append(" isentarcobrancamulta = true, valorisencao = valormulta ");
		} else {
			sqlStr.append(" isentarcobrancamulta = false, valorisencao = 0 ");
		}
		sqlStr.append(" WHERE itememprestimo.contareceber = ").append(contaReceber).append(adicionarUsuarioLogadoComoComentarioParaLogTriggerComIgnoreValidacao(usuarioVO));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	@Override
	public List<ItemEmprestimoVO> consultarItensEmprestadosPorCodigoPessoaValidandoEmprestimosOutrasBibliotecas(Integer codigoPessoa, Integer biblioteca, String tipoPessoa, int nivelMontarDados, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());
		sqlStr.append(" WHERE emprestimo.pessoa = ").append(codigoPessoa);
		sqlStr.append(" AND itememprestimo.situacao = 'EX' ");
		if (!TipoPessoa.MEMBRO_COMUNIDADE.name().equals(tipoPessoa) && !configuracaoBiblioteca.getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestado() && Uteis.isAtributoPreenchido(biblioteca)) {
			sqlStr.append(" AND emprestimo.biblioteca = ").append(biblioteca);
		}		
		if (TipoPessoa.MEMBRO_COMUNIDADE.name().equals(tipoPessoa) && !configuracaoBiblioteca.getConsiderarEmprestimoOutraBibliotecaNrMaximoExemplaresEmprestadoVisitante() && Uteis.isAtributoPreenchido(biblioteca)) {
			sqlStr.append(" AND emprestimo.biblioteca = ").append(biblioteca);
		}
		if (Uteis.isAtributoPreenchido(tipoPessoa)) {
			sqlStr.append(" AND emprestimo.tipopessoa = '").append(tipoPessoa).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public CidadeVO consultarCidadeBiblioteca(Integer biblioteca, UsuarioVO usuarioVO) {
		CidadeVO obj = getFacadeFactory().getCidadeFacade().consultarDadosComboBoxPorBiblioteca(biblioteca, usuarioVO);
		if (obj != null && !obj.getCodigo().equals(0)) {
			return obj;
		}
		return new CidadeVO();
	}
	
	@Override
	public List<ItemEmprestimoVO> consultarPorMatriculaFichaAluno(String matricula, String situacao, String mesAno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select emprestimo.data AS dataEmprestimo, itememprestimo.dataprevisaodevolucao, itememprestimo.situacao, itememprestimo.datadevolucao,  catalogo.titulo, ");
		sb.append(" itemEmprestimo.codigo, emprestimo.codigo AS \"emprestimo.codigo\" ");
		sb.append(" from itememprestimo ");
		sb.append(" inner join emprestimo on emprestimo.codigo = itememprestimo.emprestimo ");
		sb.append(" inner join exemplar on exemplar.codigo = itememprestimo.exemplar ");
		sb.append(" inner join catalogo on catalogo.codigo = exemplar.catalogo ");
		sb.append(" inner join matricula on matricula.matricula = emprestimo.matricula ");
		sb.append(" where matricula.matricula = '").append(matricula).append("' ");
		if (situacao != null && !situacao.equals("")) {
			if (situacao.equals("EX")) {
				sb.append(" and itemEmprestimo.situacao = '").append(situacao).append("' and itemEmprestimo.dataPrevisaoDevolucao >= current_date ");
			} else if (situacao.equals("AT")) {
				sb.append(" and (itemEmprestimo.situacao = '").append(situacao).append("' or (itemEmprestimo.situacao = 'EX' and itemEmprestimo.dataPrevisaoDevolucao < current_date )) ");
			} else {
				sb.append(" and itemEmprestimo.situacao = '").append(situacao).append("' ");
			}
		}
		if (mesAno != null && !mesAno.equals("")) {
			sb.append(" and extract(month from emprestimo.data) = ").append(getMesDataEmprestimo(mesAno));
			sb.append(" and extract(year from emprestimo.data) = ").append(getAnoDataEmprestimo(mesAno));
		}
		sb.append(" order by emprestimo.data, catalogo.titulo, itememprestimo.codigo desc ");
		List<ItemEmprestimoVO> listaItemEmprestimoVOs = new ArrayList<ItemEmprestimoVO>(0);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (tabelaResultado.next()) {
			ItemEmprestimoVO obj = new ItemEmprestimoVO();
			
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDataPrevisaoDevolucao(tabelaResultado.getDate("dataprevisaodevolucao"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.setDataDevolucao(tabelaResultado.getDate("dataDevolucao"));
			obj.getExemplar().getCatalogo().setTitulo(tabelaResultado.getString("titulo"));
			
			obj.getEmprestimo().setCodigo(tabelaResultado.getInt("emprestimo.codigo"));
			obj.getEmprestimo().setData(tabelaResultado.getDate("dataEmprestimo"));
			listaItemEmprestimoVOs.add(obj);
			
		}
		return listaItemEmprestimoVOs;
	}
	
	public Integer getMesDataEmprestimo(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String mes = mesAno.substring(0, mesAno.indexOf("/"));
			return Uteis.getMesConcatenadoReferencia(mes);
		}
		return 0;
	}
	
	public Integer getAnoDataEmprestimo(String mesAno) {
		if (mesAno != null && !mesAno.equals("")) {
			String ano = mesAno.substring(mesAno.indexOf("/") + 1);
			return Integer.parseInt(ano);
		}
		return 0;
	}
	
	@Override
	public List<SelectItem> consultarMesAnoItemEmprestimoPorAlunoFichaAluno(Integer aluno, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select mes ||'/'|| ano AS mesAno from (");
		sb.append(" select distinct case ");
		sb.append(" when extract(month from emprestimo.data) = 1 then 'JAN' ");
		sb.append(" when extract(month from emprestimo.data) = 2 then 'FEV' ");
		sb.append(" when extract(month from emprestimo.data) = 3 then 'MAR' ");
		sb.append(" when extract(month from emprestimo.data) = 4 then 'ABR' ");
		sb.append(" when extract(month from emprestimo.data) = 5 then 'MAI' ");
		sb.append(" when extract(month from emprestimo.data) = 6 then 'JUN' ");
		sb.append(" when extract(month from emprestimo.data) = 7 then 'JUL' ");
		sb.append(" when extract(month from emprestimo.data) = 8 then 'AGO' ");
		sb.append(" when extract(month from emprestimo.data) = 9 then 'SET' ");
		sb.append(" when extract(month from emprestimo.data) = 10 then 'OUT' ");
		sb.append(" when extract(month from emprestimo.data) = 11 then 'NOV' ");
		sb.append(" when extract(month from emprestimo.data) = 12 then 'DEZ' ");
		sb.append(" end AS mes, extract(year from emprestimo.data) AS ano, emprestimo.data ");
		sb.append(" from emprestimo ");
		sb.append(" inner join matricula on matricula.matricula = emprestimo.matricula ");
		sb.append(" where matricula.aluno = ").append(aluno);
		sb.append(" order by emprestimo.data desc) as t ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		HashMap<String, String> mapMesAnoVOs = new HashMap<String, String>(0);
		List<SelectItem> listaSelectItemMesAnoItemEmprestimoVOs = new ArrayList<SelectItem>(0);
		listaSelectItemMesAnoItemEmprestimoVOs.add(new SelectItem("", ""));
		while (tabelaResultado.next()) {
			if (!mapMesAnoVOs.containsKey(tabelaResultado.getString("mesAno"))) {
				listaSelectItemMesAnoItemEmprestimoVOs.add(new SelectItem(tabelaResultado.getString("mesAno"), tabelaResultado.getString("mesAno")));
				mapMesAnoVOs.put(tabelaResultado.getString("mesAno"), tabelaResultado.getString("mesAno"));
			}
		}
		return listaSelectItemMesAnoItemEmprestimoVOs;
	}
	
	private static String adicionarUsuarioLogadoComoComentarioParaLogTriggerComIgnoreValidacao(UsuarioVO usuarioVO) {
		return new StringBuilder().append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO)).append("--ignorevalidacaosituacao").toString();
	}
	
	private ItemEmprestimoVO calcularValorMultaComJurosMultaContaReceber(Integer codigoContaReceber , ItemEmprestimoVO itemEmprestimoVO , UsuarioVO usuario) throws Exception {
		ContaReceberVO contaReceberVO = new ContaReceberVO();
		contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(codigoContaReceber, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, new ConfiguracaoFinanceiroVO(), usuario);
		itemEmprestimoVO.setSituacao_Multa(contaReceberVO.getSituacao_Apresentar());
		if (Uteis.isAtributoPreenchido(contaReceberVO) && Uteis.isAtributoPreenchido(contaReceberVO.getValorMultaCalculado())) {
			BigDecimal porcentagemParcelaContaReceber =	Uteis.converterValorDoubleParaBigDecimal(itemEmprestimoVO.getValorMulta()).multiply(new BigDecimal("100")).divide(Uteis.converterValorDoubleParaBigDecimal(contaReceberVO.getValor()) , MathContext.DECIMAL128);			
			BigDecimal valorMultaJurosParcelaContaReceber = Uteis.porcentagemComBigDecimal(Uteis.converterValorDoubleParaBigDecimal(contaReceberVO.getValorMultaCalculado()).add(Uteis.converterValorDoubleParaBigDecimal(contaReceberVO.getValorJuroCalculado())), porcentagemParcelaContaReceber);
			itemEmprestimoVO.setValorMulta(Uteis.arrendondarForcando2CasasDecimais(itemEmprestimoVO.getValorMulta() + valorMultaJurosParcelaContaReceber.doubleValue()));
		}
		return itemEmprestimoVO;
	}
}
