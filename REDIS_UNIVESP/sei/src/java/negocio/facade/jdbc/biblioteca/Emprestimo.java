package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
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

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.BloqueioBibliotecaVO;
import negocio.comuns.biblioteca.CatalogoVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.PoolImpressaoVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.biblioteca.enumeradores.FormatoImpressaoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoExemplar;
import negocio.comuns.utilitarias.dominios.SituacaoItemEmprestimo;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.EmprestimoInterfaceFacade;
import relatorio.negocio.comuns.biblioteca.TicketRelVO;

/**
 * Classe de persistÃƒÂ¯Ã‚Â¿Ã‚Â½ncia que encapsula todas as operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½es de manipulaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o dos
 * dados da classe <code>EmprestimoVO</code>. ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por implementar
 * operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½es como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>EmprestimoVO</code>. Encapsula toda a interaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o com o banco de dados.
 * 
 * @see EmprestimoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Emprestimo extends ControleAcesso implements EmprestimoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2284542664382984761L;
	protected static String idEntidade;
	private static final long ONE_DAY = 24 * 60 * 60 * 1000;

	public Emprestimo() throws Exception {
		super();
		setIdEntidade("Emprestimo");
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o responsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por retornar um novo objeto da classe
	 * <code>EmprestimoVO</code>.
	 */
	public EmprestimoVO novo() throws Exception {
		Emprestimo.incluir(getIdEntidade());
		EmprestimoVO obj = new EmprestimoVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<String> finalizarEmprestimoDevolucao(EmprestimoVO emprestimoVO, String matricula, List<ItemEmprestimoVO> listaItensEmprestimo, ConfiguracaoBibliotecaVO confPadraoBib, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuario) throws Exception {
		List<String> mensagemSucesso = new ArrayList<String>(0);
		try {
			List<ItemEmprestimoVO> listaItensEmprestimoParaDevolucaoRenovacao = new ArrayList<ItemEmprestimoVO>();
			List<ItemEmprestimoVO> listaItensEmprestimoNovo = new ArrayList<ItemEmprestimoVO>();
			emprestimoVO.getMatricula().setMatricula(matricula);
			if (emprestimoVO.getNovoObj()) {
				emprestimoVO.setCodigo(0);
				emprestimoVO.setUnidadeEnsinoVO(unidadeEnsinoLogado);
				emprestimoVO.getItemEmprestimoVOs().clear();
			}
			listaItensEmprestimo.stream().filter(ie -> ie.getDevolverEmprestimo() || ie.getRenovarEmprestimo()).forEach(listaItensEmprestimoParaDevolucaoRenovacao::add);
			listaItensEmprestimo.stream().filter(ie -> ie.getEmprestar()).forEach(listaItensEmprestimoNovo::add);
			emprestimoVO.setValorTotalMulta(realizarCalculoMultaDevolucaoEmprestimo(listaItensEmprestimo, emprestimoVO.getPessoa().getCodigo(), TipoPessoa.getEnum(emprestimoVO.getTipoPessoa()), false, confPadraoBib, emprestimoVO.getBiblioteca().getCidade(), usuario));
			if (!listaItensEmprestimoParaDevolucaoRenovacao.isEmpty()) {
				realizarCriacaoBloqueioBiblioteca(emprestimoVO, listaItensEmprestimoParaDevolucaoRenovacao, confPadraoBib, usuario);
				getFacadeFactory().getItemEmprestimoFacade().inicializarDadosDevolucaoRenovacaoDosItensEmprestimo(emprestimoVO, listaItensEmprestimoParaDevolucaoRenovacao, confPadraoBib, configuracaoFinanceiro, mensagemSucesso, usuario);
				getFacadeFactory().getContaReceberFacade().realizarCriacaoContaReceberMultaBiblioteca(emprestimoVO, listaItensEmprestimoParaDevolucaoRenovacao, confPadraoBib, configuracaoFinanceiro, unidadeEnsinoLogado, usuario);
			}
			if (!listaItensEmprestimoNovo.isEmpty()) {
				getFacadeFactory().getItemEmprestimoFacade().inicializarDadosNovoEmprestimoDosItensEmprestimo(emprestimoVO, listaItensEmprestimoNovo, confPadraoBib, configuracaoFinanceiro, mensagemSucesso, usuario);
			}
			Thread enviarComunicadoBibliotecaEmprestimo = new Thread(new EnviarComunicadoBibliotecaEmprestimo(listaItensEmprestimo, emprestimoVO.getPessoa(), emprestimoVO.getTipoPessoa(), emprestimoVO.getBiblioteca().getNome(), emprestimoVO.getUnidadeEnsinoVO().getCodigo(), usuario));
			enviarComunicadoBibliotecaEmprestimo.start();
		} catch (Exception e) {
			if(e.getMessage() != null && e.getMessage().contains("emprestimoJaEstaDevolvido") && e.getMessage().contains("{(") && e.getMessage().contains(")}")) {
				String codigoItemEmprestimo = e.getMessage().substring(e.getMessage().indexOf("{(") + 2, e.getMessage().indexOf(")}"));
				if (NumberUtils.isNumber(codigoItemEmprestimo)) {
					Integer codigo = Integer.valueOf(codigoItemEmprestimo);
					throw new ConsistirException("O item: " + listaItensEmprestimo.stream().filter(i -> i.getCodigo().equals(codigo)).map(i -> i.getExemplar().getCatalogo().getTitulo()).findFirst().orElse("") + " j� se encontra devolvido.");
				}
			}
			throw e;
		}
		return mensagemSucesso;
	}

	public void realizarCalculoMultaDevolucaoItemEmprestimo(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		if (itemEmprestimoVO.getTipoEmprestimo().equals("HR")) {
			realizarCalculoMultaDevolucaoItemEmprestimoHora(itemEmprestimoVO, tipoPessoa, confBibVO, cidadeVO, usuarioVO);
		} else {
			realizarCalculoMultaDevolucaoItemEmprestimoDia(itemEmprestimoVO, tipoPessoa, confBibVO, cidadeVO, usuarioVO);
		}
	}
	
	public void realizarCalculoMultaDevolucaoItemEmprestimoDia(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		Double valorBase = 0.0;
		Date dataBaseCalculoMulta = new Date();
		if (itemEmprestimoVO.getSituacao().equals(SituacaoItemEmprestimo.EM_EXECUCAO.getValor())) {
			if(Uteis.getObterDiferencaDiasEntreDuasData(itemEmprestimoVO.getDataPrevisaoDevolucao(), new Date()) >= 0) {
				return;
			}
		}else if (itemEmprestimoVO.getSituacao().equals(SituacaoItemEmprestimo.DEVOLVIDO.getValor())) {
			return;
		}
		if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
			if (itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoAlunoFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaAluno();
			}
		}
		if (tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE)) {
			if (itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoVisitanteFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaVisitante();
			}
		}
		if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
			if (itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoProfessorFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaFuncionario();
			}
		}
		if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
			if (itemEmprestimoVO.getExemplar().getEmprestarSomenteFinalDeSemana()) {
				valorBase = confBibVO.getValorMultaEmprestimoFuncionarioFinalDeSemana();
			} else {
				valorBase = confBibVO.getValorMultaDiaProfessor();
			}
		}
		
		if (confBibVO.getUtilizarDiasUteisCalcularMulta()) {
			if (itemEmprestimoVO.getDataPrevisaoDevolucao().compareTo(Uteis.getDateSemHora(dataBaseCalculoMulta)) < 0 ) {
				/*if(!itemEmprestimoVO.getIsentarCobrancaMulta()){
					itemEmprestimoVO.setValorMulta(valorBase * calcularNrDiasUteisProgredir(itemEmprestimoVO.getDataPrevisaoDevolucao(), Uteis.getDateSemHora(new Date()), usuarioVO));
					itemEmprestimoVO.setValorIsencao(0.0);
				}else{
					itemEmprestimoVO.setValorIsencao(valorBase * calcularNrDiasUteisProgredir(itemEmprestimoVO.getDataPrevisaoDevolucao(), Uteis.getDateSemHora(new Date()), usuarioVO));
					itemEmprestimoVO.setValorMulta(0.0);
				}*/
				if(!itemEmprestimoVO.getIsentarCobrancaMulta()){
					itemEmprestimoVO.setValorMulta(valorBase * getFacadeFactory().getFeriadoFacade().calcularNrDiasUteis(itemEmprestimoVO.getDataPrevisaoDevolucao(), dataBaseCalculoMulta, Uteis.isAtributoPreenchido(cidadeVO.getCodigo()) ? cidadeVO.getCodigo() : 0, confBibVO.getConsiderarSabadoDiaUtil(), confBibVO.getConsiderarDomingoDiaUtil(), ConsiderarFeriadoEnum.BIBLIOTECA));
					itemEmprestimoVO.setValorIsencao(0.0);
				}else{
					itemEmprestimoVO.setValorIsencao(valorBase * getFacadeFactory().getFeriadoFacade().calcularNrDiasUteis(itemEmprestimoVO.getDataPrevisaoDevolucao(), dataBaseCalculoMulta, Uteis.isAtributoPreenchido(cidadeVO.getCodigo()) ? cidadeVO.getCodigo() : 0, confBibVO.getConsiderarSabadoDiaUtil(), confBibVO.getConsiderarDomingoDiaUtil(), ConsiderarFeriadoEnum.BIBLIOTECA));
					itemEmprestimoVO.setValorMulta(0.0);
				}
			}
		} else {
			if (itemEmprestimoVO.getDataPrevisaoDevolucao().compareTo(Uteis.getDateSemHora(dataBaseCalculoMulta)) < 0) {
				if(!itemEmprestimoVO.getIsentarCobrancaMulta()){
					itemEmprestimoVO.setValorMulta(valorBase * Uteis.nrDiasEntreDatas(Uteis.getDateHoraInicialDia(dataBaseCalculoMulta), Uteis.getDateHoraInicialDia(itemEmprestimoVO.getDataPrevisaoDevolucao())));
					itemEmprestimoVO.setValorIsencao(0.0);
				}else{
					itemEmprestimoVO.setValorIsencao(valorBase * Uteis.nrDiasEntreDatas(Uteis.getDateHoraInicialDia(dataBaseCalculoMulta), Uteis.getDateHoraInicialDia(itemEmprestimoVO.getDataPrevisaoDevolucao())));
					itemEmprestimoVO.setValorMulta(0.0);
				}
			}
		}
	}

	/*public Integer calcularNrDiasUteisProgredir(Date dataPrevisaoDevolucao, Date dataEntrega, UsuarioVO usuario) throws Exception {
		List<FeriadoVO> listaFeriadoVOs = getFacadeFactory().getFeriadoFacade().consultarPorMes(Uteis.getMesDataAtual(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		Calendar dataEntregaIncremental = new GregorianCalendar();
		dataEntregaIncremental.setTime(dataEntrega);
		Calendar dataAuxIncremental = new GregorianCalendar();
		dataAuxIncremental.setTime(dataPrevisaoDevolucao);
		// Verifico se a data da entrega é dia não útil
		while (!isWorkingDay(dataEntregaIncremental.getTime(), listaFeriadoVOs)) {
			dataEntregaIncremental.add(Calendar.DAY_OF_MONTH, 1);
		}
		while (!isWorkingDay(dataAuxIncremental.getTime(), listaFeriadoVOs)) {
			dataAuxIncremental.add(Calendar.DAY_OF_MONTH, 1);
		}
		// Calculo o nr de dias não uteis entre a data da previsão de devolução e a data da entrega.
		Integer nrDiasUteis = 0;
		while (Uteis.nrDiasEntreDatas(dataEntregaIncremental.getTime(), dataAuxIncremental.getTime()) >= 1) {
			if (!isWorkingDay(dataAuxIncremental.getTime(), listaFeriadoVOs)) {
				dataAuxIncremental.add(Calendar.DAY_OF_MONTH, 1);
			} else {
				dataAuxIncremental.add(Calendar.DAY_OF_MONTH, 1);
				nrDiasUteis++;
			}
		}
		return nrDiasUteis;
	}

	public boolean isWorkingDay(Date date, List<FeriadoVO> listaFeriadoVOs) {
		if (!Uteis.isWorkingWeekDay(date)) {
			return false;
		}

		for (FeriadoVO holiday : listaFeriadoVOs) {
			if (holiday.isHoliday(date)) {
				return false;
			}
		}

		return true;
	}*/

	public void realizarCalculoMultaDevolucaoItemEmprestimoHora(ItemEmprestimoVO itemEmprestimoVO, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		Double valorBase = 0.0;		
		
		if (tipoPessoa.equals(TipoPessoa.ALUNO)) {
			valorBase = confBibVO.getValorMultaEmprestimoPorHoraAluno();
		}
		if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO)) {
			valorBase = confBibVO.getValorMultaEmprestimoPorHoraFuncionario();
		}
		if (tipoPessoa.equals(TipoPessoa.PROFESSOR)) {
			valorBase = confBibVO.getValorMultaEmprestimoPorHoraProfessor();
		}
		
		if (confBibVO.getUtilizarDiasUteisCalcularMulta()) {
			if (itemEmprestimoVO.getDataPrevisaoDevolucao().compareTo(new Date()) < 0 ) {
				if(!itemEmprestimoVO.getIsentarCobrancaMulta()){
					double horas = UteisData.diferencaEmHoras(itemEmprestimoVO.getDataPrevisaoDevolucao(), new Date() );
					int hora = Uteis.arredondarParaMais(horas);
					itemEmprestimoVO.setValorMulta(valorBase * hora);
					itemEmprestimoVO.setValorIsencao(0.0);
				}else{
					double horas = UteisData.diferencaEmHoras(itemEmprestimoVO.getDataPrevisaoDevolucao(), new Date());
					int hora = Uteis.arredondarParaMais(horas);
					itemEmprestimoVO.setValorIsencao(valorBase * hora);
					itemEmprestimoVO.setValorMulta(0.0);
				}
			}
		} else {
			if (itemEmprestimoVO.getDataPrevisaoDevolucao().compareTo(new Date()) < 0) {
				if(!itemEmprestimoVO.getIsentarCobrancaMulta()){
					double horas = UteisData.diferencaEmHoras(itemEmprestimoVO.getDataPrevisaoDevolucao(), new Date());
					int hora = Uteis.arredondarParaMais(horas);
					itemEmprestimoVO.setValorMulta(valorBase * hora);
					itemEmprestimoVO.setValorIsencao(0.0);
				}else{
					double horas = UteisData.diferencaEmHoras(itemEmprestimoVO.getDataPrevisaoDevolucao(), new Date());
					int hora = Uteis.arredondarParaMais(horas);					
					itemEmprestimoVO.setValorIsencao(valorBase * hora);
					itemEmprestimoVO.setValorMulta(0.0);
				}
			}
		}
	}
	
	@Override
	public Double realizarCalculoMultaDevolucaoEmprestimo(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer pessoa, TipoPessoa tipoPessoa, Boolean criacaoContaReceber, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		Double valor = 0.0;
		HashMap<String, ConfiguracaoBibliotecaVO> mapConfiguracaoBiblioteca = new HashMap<String, ConfiguracaoBibliotecaVO>(0);
		ConfiguracaoBibliotecaVO confBibCalculoMulta = null;
		List<ItemEmprestimoVO> itemEmprestimoCalculoMulta = new ArrayList<ItemEmprestimoVO>(0);
		
		if (confBibVO.getValidarMultaOutraBiblioteca() && !criacaoContaReceber) {
			List<ItemEmprestimoVO> listaItensEmprestimoOutrasBiblioteca = getFacadeFactory().getItemEmprestimoFacade().consultarItensEmprestadosOutraBibliotecaPorCodigoPessoa(itemEmprestimoVOs, pessoa, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confBibVO, usuarioVO);;
			itemEmprestimoCalculoMulta.addAll(listaItensEmprestimoOutrasBiblioteca);
			itemEmprestimoCalculoMulta.addAll(itemEmprestimoVOs);
		} else {
			itemEmprestimoCalculoMulta.addAll(itemEmprestimoVOs);
		}
		
		for (ItemEmprestimoVO itemEmprestimoVO : itemEmprestimoCalculoMulta) {		
			
			String keyMapConfiguracaoBiblioteca = itemEmprestimoVO.getEmprestimo().getBiblioteca().getCodigo().toString() + itemEmprestimoVO.getEmprestimo().getUnidadeEnsinoVO().getCodigo().toString();
			if (mapConfiguracaoBiblioteca.containsKey(keyMapConfiguracaoBiblioteca)) {
				confBibCalculoMulta = mapConfiguracaoBiblioteca.get(keyMapConfiguracaoBiblioteca);
			} else {
				confBibCalculoMulta = getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(itemEmprestimoVO.getEmprestimo().getBiblioteca().getCodigo(), itemEmprestimoVO.getEmprestimo().getTipoPessoa(), itemEmprestimoVO.getEmprestimo().getMatricula().getMatricula(), usuarioVO);
				mapConfiguracaoBiblioteca.put(keyMapConfiguracaoBiblioteca, confBibCalculoMulta);
			}
			if (confBibCalculoMulta == null || confBibCalculoMulta.getCodigo().equals(0)) {
				confBibCalculoMulta = confBibVO;
			}
			
			realizarCalculoMultaDevolucaoItemEmprestimo(itemEmprestimoVO, tipoPessoa, confBibCalculoMulta, cidadeVO, usuarioVO);
			valor += itemEmprestimoVO.getValorMulta();
		}
		return valor;
	}
	
	@Override
	public Double realizarCalculoIsencaoDevolucaoEmprestimo(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer pessoa, TipoPessoa tipoPessoa, ConfiguracaoBibliotecaVO confBibVO, CidadeVO cidadeVO, UsuarioVO usuarioVO) throws Exception {
		Double valor = 0.0;
		HashMap<String, ConfiguracaoBibliotecaVO> mapConfiguracaoBiblioteca = new HashMap<String, ConfiguracaoBibliotecaVO>(0);
		ConfiguracaoBibliotecaVO confBibCalculoIsencao = null;
		
		for (ItemEmprestimoVO itemEmprestimoVO : itemEmprestimoVOs) {

			String keyMapConfiguracaoBiblioteca = itemEmprestimoVO.getEmprestimo().getBiblioteca().getCodigo().toString() + itemEmprestimoVO.getEmprestimo().getUnidadeEnsinoVO().getCodigo().toString();
			if (mapConfiguracaoBiblioteca.containsKey(keyMapConfiguracaoBiblioteca)) {
				confBibCalculoIsencao = mapConfiguracaoBiblioteca.get(keyMapConfiguracaoBiblioteca);
			} else {
				confBibCalculoIsencao = getFacadeFactory().getConfiguracaoBibliotecaFacade().executarObterConfiguracaoBibliotecaComBaseTipoPessoa(itemEmprestimoVO.getEmprestimo().getBiblioteca().getCodigo(), itemEmprestimoVO.getEmprestimo().getTipoPessoa(), itemEmprestimoVO.getEmprestimo().getMatricula().getMatricula(), usuarioVO);
				mapConfiguracaoBiblioteca.put(keyMapConfiguracaoBiblioteca, confBibCalculoIsencao);
			}
			if (confBibCalculoIsencao == null || confBibCalculoIsencao.getCodigo().equals(0)) {
				confBibCalculoIsencao = confBibVO;
			}
			
			realizarCalculoMultaDevolucaoItemEmprestimo(itemEmprestimoVO, tipoPessoa, confBibCalculoIsencao, cidadeVO, usuarioVO);
			valor += itemEmprestimoVO.getValorIsencao();
		}
		return valor;
	}

	/**
	 * Esse mÃƒÂ¯Ã‚Â¿Ã‚Â½todo recebe a lista de itens para devoluÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o e renovaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o, e
	 * verifica para cada item dessa lista se o seu emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo correspondente
	 * estÃƒÂ¯Ã‚Â¿Ã‚Â½ com todos os itens devolvidos, finalizando o emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo caso
	 * positivo.
	 * 
	 * @param listaItensEmprestimoParaDevolucaoRenovacao
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarSeTodosItensEmprestimoForamDevolvidosRenovados(List<ItemEmprestimoVO> listaItensEmprestimoParaDevolucaoRenovacao) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT CASE WHEN COUNT(ie.situacao) > 0 THEN true ELSE false END FROM emprestimo e ");
		sqlStr.append("INNER JOIN itememprestimo ie ON ie.emprestimo = e.codigo ");
		sqlStr.append("WHERE e.codigo = ? AND ie.situacao = 'EX'");
		try {
			for (ItemEmprestimoVO itemEmprestimoVO : listaItensEmprestimoParaDevolucaoRenovacao) {

				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { itemEmprestimoVO.getEmprestimo() });
				tabelaResultado.next();
				if (!tabelaResultado.getBoolean("case")) {
//					executarFinalizacaoEmprestimo(itemEmprestimoVO.getEmprestimo().getCodigo());
				}
				tabelaResultado = null;
			}
		} finally {
			sqlStr = null;
		}
	}

	/**
	 * Esse mÃƒÂ¯Ã‚Â¿Ã‚Â½todo recebe o cÃƒÂ¯Ã‚Â¿Ã‚Â½digo do emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo e verifica se ele estÃƒÂ¯Ã‚Â¿Ã‚Â½ com
	 * todos os itens devolvidos, finalizando o emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo caso positivo.
	 * 
	 * @param listaItensEmprestimoParaDevolucaoRenovacao
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarSeTodosItensEmprestimoForamDevolvidosRenovados(Integer codigoEmprestimo) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT CASE WHEN COUNT(ie.situacao) > 0 THEN true ELSE false END as case FROM emprestimo e ");
		sqlStr.append("INNER JOIN itememprestimo ie ON ie.emprestimo = e.codigo ");
		sqlStr.append("WHERE e.codigo = ? AND ie.situacao = 'EX'");
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoEmprestimo });
			tabelaResultado.next();
			if (!tabelaResultado.getBoolean("case")) {
//				executarFinalizacaoEmprestimo(codigoEmprestimo);
			}
			tabelaResultado = null;
		} finally {
			sqlStr = null;
		}
	}

	/**
	 * MÃƒÂ¯Ã‚Â¿Ã‚Â½todo que atualiza a situaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o do emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo para finalizado.
	 * 
	 * @param codigoEmprestimo
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarFinalizacaoEmprestimo(Integer codigoEmprestimo) throws Exception {
		String sqlStr = "UPDATE emprestimo SET situacao = 'FI' WHERE codigo = ?";
		try {
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { codigoEmprestimo });
		} finally {
			sqlStr = null;
		}
	}

	public void inicializarDadosEmprestimoNovo(EmprestimoVO emprestimoVO, UsuarioVO usuario) throws Exception {
		emprestimoVO.getAtendente().setCodigo(usuario.getCodigo());
		emprestimoVO.getAtendente().setNome(usuario.getNome());
		emprestimoVO.setTipoPessoa(TipoPessoa.ALUNO.getValor());
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarSituacaoMatriculaAluno(EmprestimoVO emprestimoVO, ConfiguracaoBibliotecaVO confPadraoBib, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAptaEmprestimoBiblioteca(emprestimoVO.getMatricula().getMatricula(), "'PC', 'PR'", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (!matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals("AT") && !matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals("FI") && emprestimoVO.getMatricula().getSituacao().equals("AT")) {
			if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroSituacaoMatricula").replace("{0}", emprestimoVO.getMatricula().getMatricula()).replace("{1}", emprestimoVO.getPessoa().getNome()).replace("{2}", matriculaPeriodoVO.getSituacaoMatriculaPeriodo_Apresentar()));
			}
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroSituacaoMatriculaNaoAtiva").replace("{0}", emprestimoVO.getMatricula().getMatricula()).replace("{1}", emprestimoVO.getPessoa().getNome()));
		}
	}
	
	/**
	 * Verifica o nÃºmero de exemplares que a pessoa estÃ¯Â¿Â½ pegando emprestado.
	 * Joga uma exceÃ¯Â¿Â½Ã¯Â¿Â½o se acaso o nÃºmero ultrapassar o permitido.
	 * 
	 * @param emprestimoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarNrExemplaresPorPessoa(EmprestimoVO emprestimoVO, ConfiguracaoBibliotecaVO confPadraoBib, UsuarioVO usuario) throws Exception {
		Integer tamanhoLista = emprestimoVO.getItemEmprestimoVOs().size();
		List<ItemEmprestimoVO> itensEmprestadosParaPessoa = new ArrayList<ItemEmprestimoVO>(0);
		try {
			itensEmprestadosParaPessoa = getFacadeFactory().getItemEmprestimoFacade().consultarItensEmprestadosPorCodigoPessoa(emprestimoVO.getPessoa().getCodigo(), emprestimoVO.getBiblioteca().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confPadraoBib, usuario);
			if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
				itensEmprestadosParaPessoa = getFacadeFactory().getItemEmprestimoFacade().consultarItensEmprestadosPorCodigoPessoaValidandoEmprestimosOutrasBibliotecas(emprestimoVO.getPessoa().getCodigo(), emprestimoVO.getBiblioteca().getCodigo(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confPadraoBib, usuario);
				if (tamanhoLista + itensEmprestadosParaPessoa.size() > confPadraoBib.getNumeroMaximoExemplaresAluno()) {
					String emprestimosBibliotecas = executarGeracaoMensagemLimiteEmprestimosExcedidoAluno(itensEmprestadosParaPessoa);
					throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoExemplaresConsiderandoOutrasBiblioteacas").replace("{0}", confPadraoBib.getNumeroMaximoExemplaresAluno().toString()).replace("{1}", emprestimosBibliotecas));
				}
			} else if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
				if (tamanhoLista + itensEmprestadosParaPessoa.size() > confPadraoBib.getNumeroMaximoExemplaresProfessor()) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoExemplares").replace("{0}", TipoPessoa.PROFESSOR.getDescricao()).replace("{1}", confPadraoBib.getNumeroMaximoExemplaresProfessor().toString()));
				}
			} else if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())) {
				if (tamanhoLista + itensEmprestadosParaPessoa.size() > confPadraoBib.getNumeroMaximoExemplaresVisitante()) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoExemplares").replace("{0}", TipoPessoa.REQUERENTE.getDescricao()).replace("{1}", confPadraoBib.getNumeroMaximoExemplaresVisitante().toString()));
				}
			} else if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
				if (tamanhoLista + itensEmprestadosParaPessoa.size() > confPadraoBib.getNumeroMaximoExemplaresFuncionario()) {
					throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoExemplares").replace("{0}", TipoPessoa.FUNCIONARIO.getDescricao()).replace("{1}", confPadraoBib.getNumeroMaximoExemplaresFuncionario().toString()));
				}
			}
		} finally {
			tamanhoLista = null;
		}
	}

	/**
	 * Verifica na hora da renovaÃ¯Â¿Â½Ã¯Â¿Â½o de um exemplar, se existe um nÃºmero maior
	 * de exemplares disponiveis comparado ao nÃºmero de exemplares de um
	 * catalogo que devem ficar fixos na biblioteca.
	 * 
	 * @param emprestimoVO
	 * @throws Exception
	 */
	public void verificarNrMinimoExemplaresFixosBiblioteca(EmprestimoVO emprestimoVO, ConfiguracaoBibliotecaVO confPadraoBib) throws Exception {
		Integer nrExemplaresDevemFicarDisponiveisBiblioteca;
		Integer nrExemplaresExistentesCatalogo;
		Integer nrExemplaresDisponiveisCatalogo;
		try {
			for (ItemEmprestimoVO itemEmprestimoVO : emprestimoVO.getItemEmprestimoVOs()) {
				nrExemplaresExistentesCatalogo = getFacadeFactory().getExemplarFacade().consultarNrExemplaresCatalogoGravadosDisponiveis(itemEmprestimoVO.getExemplar(), "");
				nrExemplaresDisponiveisCatalogo = getFacadeFactory().getExemplarFacade().consultarNrExemplaresCatalogoGravadosDisponiveis(itemEmprestimoVO.getExemplar(), SituacaoExemplar.DISPONIVEL.getValor());
				nrExemplaresDevemFicarDisponiveisBiblioteca = (int) ((confPadraoBib.getPercentualExemplaresParaConsulta() / 100.0) * nrExemplaresExistentesCatalogo);
				if (itemEmprestimoVO.getExemplar().getCatalogo().getNrExemplaresParaConsulta() != 0) {
					if (nrExemplaresDisponiveisCatalogo < itemEmprestimoVO.getExemplar().getCatalogo().getNrExemplaresParaConsulta()) {
						throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoExemplaresEmprestadosRenovacao"));
					}
				} else {
					if (nrExemplaresDisponiveisCatalogo < nrExemplaresDevemFicarDisponiveisBiblioteca) {
						throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoExemplaresEmprestadosRenovacao"));
					}
				}
			}
		} finally {
			nrExemplaresDevemFicarDisponiveisBiblioteca = null;
			nrExemplaresDisponiveisCatalogo = null;
			nrExemplaresExistentesCatalogo = null;
		}
	}

	/**
	 * MÃ¯Â¿Â½todo que verifica o nÃºmero maximo de renovaÃ¯Â¿Â½Ã¯Â¿Â½es de uma pessoa.
	 * 
	 * @param emprestimoVO
	 * @throws Exception
	 */
	public void verificarNrMaximoRenovacoesPessoa(EmprestimoVO emprestimoVO, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception {
		Integer nrRenovacoes = 0;
		try {
			for (ItemEmprestimoVO itemEmprestimoVO : emprestimoVO.getItemEmprestimoVOs()) {
				if (itemEmprestimoVO.getRenovarEmprestimo()) {
					nrRenovacoes = getFacadeFactory().getItensRenovadosFacade().calcularNrRenovacoesExemplar(emprestimoVO, itemEmprestimoVO);
					
					if (usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais()) {
						if(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoAluno() > 0) {
							if (nrRenovacoes != null && nrRenovacoes >= configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoAluno() ) {
								throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoRenovacoes"));
							}
						}
					} else if (usuario.getIsApresentarVisaoProfessor()) {
						if(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoProfessor() > 0) {
							if (nrRenovacoes != null && nrRenovacoes >= configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoProfessor()) {
								throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoRenovacoes"));
							}
						}						
					} else if (usuario.getIsApresentarVisaoCoordenador()) {
						if(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoCoordenador() > 0) {
							if (nrRenovacoes != null && nrRenovacoes >= configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoCoordenador()) {
								throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoRenovacoes"));
							}
						}						
					}

					if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
						if (nrRenovacoes != null && nrRenovacoes >= configuracaoBiblioteca.getNumeroRenovacoesAluno()) {
							throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoRenovacoes"));
						}
					} else if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.PROFESSOR.getValor())) {
						if (nrRenovacoes != null && nrRenovacoes >= configuracaoBiblioteca.getNumeroRenovacoesProfessor()) {
							throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoRenovacoes"));
						}
					} else if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())) {
						if (nrRenovacoes != null && nrRenovacoes >= configuracaoBiblioteca.getNumeroRenovacoesVisitante()) {
							throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoRenovacoes"));
						}
					} else if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
						if (nrRenovacoes != null && nrRenovacoes >= configuracaoBiblioteca.getNumeroRenovacoesFuncionario()) {
							throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroNumeroMaximoRenovacoes"));
						}
					}
				}
			}
		} finally {
			nrRenovacoes = null;
		}
	}
	
	
	public Boolean isNumeroMaximoRenovacoesPessoa(List<ItemEmprestimoVO> itemEmprestimoVOs, ConfiguracaoBibliotecaVO configuracaoBiblioteca, UsuarioVO usuario) throws Exception {
		Integer nrRenovacoes = 0;
		try {
			for (ItemEmprestimoVO itemEmprestimo : itemEmprestimoVOs) {				
				if (itemEmprestimo.getRenovarEmprestimo()) {
					nrRenovacoes = getFacadeFactory().getItensRenovadosFacade().calcularNrRenovacoesExemplar(itemEmprestimo.getEmprestimo(), itemEmprestimo);
					nrRenovacoes += 1;
					if (usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais()) {
						if(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoAluno() > 0) {
							if (nrRenovacoes != null && nrRenovacoes.equals(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoAluno()) ) {
								return true;
							}
						}
					} else if (usuario.getIsApresentarVisaoProfessor()) {
						if(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoProfessor() > 0) {
							if (nrRenovacoes != null && nrRenovacoes.equals(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoProfessor())) {
								return true;
							}
						}						
					} else if (usuario.getIsApresentarVisaoCoordenador()) {
						if(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoCoordenador() > 0) {
							if (nrRenovacoes != null && nrRenovacoes.equals(configuracaoBiblioteca.getQuantidadeRenovacaoPermitidaVisaoCoordenador())) {
								return true;
							}
						}						
					} else {
						Integer nrRenovacoesJaRealizadas = getFacadeFactory().getItensRenovadosFacade().calcularNrRenovacoesExemplar(itemEmprestimo.getEmprestimo(), itemEmprestimo);
						if (itemEmprestimo.getEmprestimo().isTipoPessoaAluno()) {
							if(configuracaoBiblioteca.getNumeroRenovacoesAluno() > 0) {
								if (nrRenovacoesJaRealizadas != null && nrRenovacoesJaRealizadas.equals(configuracaoBiblioteca.getNumeroRenovacoesAluno())) {
									return true;
								}
							}
						} else if (itemEmprestimo.getEmprestimo().isTipoPessoaProfessor()) {
							if(configuracaoBiblioteca.getNumeroRenovacoesProfessor() > 0) {
								if (nrRenovacoesJaRealizadas != null && nrRenovacoesJaRealizadas.equals(configuracaoBiblioteca.getNumeroRenovacoesProfessor())) {
									return true;
								}
							}						
						} else if (itemEmprestimo.getEmprestimo().isTipoPessoaFuncionario()) {
							if(configuracaoBiblioteca.getNumeroRenovacoesFuncionario() > 0) {
								if (nrRenovacoesJaRealizadas != null && nrRenovacoesJaRealizadas.equals(configuracaoBiblioteca.getNumeroRenovacoesFuncionario())) {
									return true;
								}
							}						
						} else if (itemEmprestimo.getEmprestimo().isTipoPessoaVisitante()) {
							if(configuracaoBiblioteca.getNumeroRenovacoesVisitante() > 0) {
								if (nrRenovacoesJaRealizadas != null && nrRenovacoesJaRealizadas.equals(configuracaoBiblioteca.getNumeroRenovacoesVisitante())) {
									return true;
								}
							}						
						}
					}
				}
			}
		} finally {
			nrRenovacoes = null;
		}
		
		return false;
	}

	/**
	 * Verifica se o usuÃƒÂ¯Ã‚Â¿Ã‚Â½rio possui um emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo de um exemplar de um
	 * determinado catÃƒÂ¯Ã‚Â¿Ã‚Â½logo.
	 * 
	 * @param codigoCatalogo
	 * @throws Exception
	 * @author Murillo Parreira
	 */
	public Boolean verificarExisteEmprestimoParaDeterminadoCatalogo(Integer codigoCatalogo, Integer codigoPessoa, Integer codigobiblioteca) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT COUNT(em.codigo) FROM emprestimo em ");
		sqlStr.append("INNER JOIN itememprestimo ie ON ie.emprestimo = em.codigo INNER JOIN exemplar ex ON ie.exemplar = ex.codigo ");
		sqlStr.append("WHERE ex.catalogo = ? AND ie.situacao = 'EX' ");
		if(Uteis.isAtributoPreenchido(codigobiblioteca)){
			sqlStr.append(" and ex.biblioteca  = ").append(codigobiblioteca);
		}
		if (codigoPessoa != 0) {
			sqlStr.append(" AND em.pessoa = ?");
		}
		try {
			SqlRowSet tabelaResultado = null;
			if (codigoPessoa != 0) {
				tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoCatalogo, codigoPessoa });
			} else {
				tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { codigoCatalogo});
			}
			tabelaResultado.next();
			if (tabelaResultado.getInt("count") == 0) {
				tabelaResultado = null;
				return false;
			} else {
				tabelaResultado = null;
				return true;
			}
		} finally {
			sqlStr = null;
		}
	}

	/**
	 * MÃƒÂ¯Ã‚Â¿Ã‚Â½todo que usa o Facade de MatrÃƒÂ¯Ã‚Â¿Ã‚Â½cula para verificar se uma determinada
	 * pessoa tem a sua matrÃƒÂ¯Ã‚Â¿Ã‚Â½cula ATIVA. A validaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o ocorre tambÃƒÂ¯Ã‚Â¿Ã‚Â½m no caso de
	 * uma pessoa ter mais de uma MatrÃƒÂ¯Ã‚Â¿Ã‚Â½cula.
	 * 
	 * @param emprestimoVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarMatriculaAtivaEmprestimoBiblioteca(EmprestimoVO emprestimoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		if (emprestimoVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			getFacadeFactory().getMatriculaFacade().verificarMatriculaAtivaPorCodigoPessoa(emprestimoVO.getPessoa().getCodigo(), configuracaoFinanceiro, usuario);
		}
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o que, ao incluir um emprestimo, muda a sua situaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o para
	 * <b>EM_EXECUCAO</b>, de acordo com o <b>Enum</b>
	 * <code>SituacaoEmprestimo</code>.
	 * 
	 * @param emprestimoVO
	 */
//	private void alterarSituacaoEmprestimoParaEmExecucao(EmprestimoVO emprestimoVO) {
//		emprestimoVO.setSituacao("EX");
//	}

	/**
	 * Verifica se uma pessoa tem pendÃƒÂ¯Ã‚Â¿Ã‚Â½ncias financeiras para com a biblioteca,
	 * e se ÃƒÂ¯Ã‚Â¿Ã‚Â½ permitido o emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo de livros de uma pessoa com pendÃƒÂ¯Ã‚Â¿Ã‚Â½ncias.
	 * 
	 * @param emprestimoVO
	 * @throws Exception
	 */
	public void verificarPendenciasFinanceirasBiblioteca(EmprestimoVO emprestimoVO, ConfiguracaoBibliotecaVO confPadraoBib, UsuarioVO usuario) throws Exception {
		if (!confPadraoBib.getEmprestimoRenovacaoComDebitos()) {
			if (getFacadeFactory().getContaReceberFacade().verificarContaReceberPessoaTipoBiblioteca(emprestimoVO.getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_Biblioteca_ErroPendenciasFinanceirasRenovacao"));
			}
		}
	}

	/**
	 * MÃƒÂ¯Ã‚Â¿Ã‚Â½todo que ao renovar um item do emprestimo, seta a data do dia do
	 * emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo para a data atual.
	 * 
	 * @param emprestimoVO
	 * @param renovacao
	 */
	private void alterarDataEmprestimoRenovacaoParaDataDia(EmprestimoVO emprestimoVO, boolean renovacao) {
		if (renovacao) {
			emprestimoVO.setData(new Date());
		}
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o responsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por incluir no banco de dados um objeto da classe
	 * <code>EmprestimoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexÃƒÂ¯Ã‚Â¿Ã‚Â½o com o banco de
	 * dados e a permissÃƒÂ¯Ã‚Â¿Ã‚Â½o do usuÃƒÂ¯Ã‚Â¿Ã‚Â½rio para realizar esta operacÃƒÂ¯Ã‚Â¿Ã‚Â½o na entidade.
	 * Isto, atravÃƒÂ¯Ã‚Â¿Ã‚Â½s da operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>EmprestimoVO</code> que serÃƒÂ¯Ã‚Â¿Ã‚Â½ gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexÃƒÂ¯Ã‚Â¿Ã‚Â½o, restriÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de acesso ou
	 *                validaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final EmprestimoVO obj, Boolean renovacao, ConfiguracaoBibliotecaVO confPadraoBib, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		try {
			// Verifica se a pessoa tem bloqueio na biblioteca.
			getFacadeFactory().getBloqueioBibliotecaFacade().verificarBloqueioBiblioteca(obj.getPessoa().getCodigo(), obj.getBiblioteca().getCodigo(), obj.getTipoPessoa(), usuario);
			
			// Verifica se a situaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o da matrÃƒÂ¯Ã‚Â¿Ã‚Â½cula perÃƒÂ¯Ã‚Â¿Ã‚Â½odo do aluno ÃƒÂ¯Ã‚Â¿Ã‚Â½ diferente de AT
			if (obj.getTipoPessoa().equals("AL")) {
				verificarSituacaoMatriculaAluno(obj, confPadraoBib, usuario);
			}
			
			// Verificar nÃºmero de exemplares por pessoa.
			verificarNrExemplaresPorPessoa(obj, confPadraoBib, usuario);

			// verificarMatriculaAtivaEmprestimoBiblioteca(obj,
			// configuracaoFinanceiro, usuario);

			// if (renovacao) {
			verificarNrMaximoRenovacoesPessoa(obj, confPadraoBib, usuario);
			// }

			// Verificar se pode ser renovado com base no nÃºmero mÃ¯Â¿Â½nimo que deve
			// ficar na biblioteca.
			// verificarNrMinimoExemplaresFixosBiblioteca(obj, confPadraoBib);

			// Verificar se nÃƒÂ¯Ã‚Â¿Ã‚Â½o existem pendÃƒÂ¯Ã‚Â¿Ã‚Â½ncias financeiras na biblioteca que
			// impossibilitem a renovaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o
			verificarPendenciasFinanceirasBiblioteca(obj, confPadraoBib, usuario);

			// Ao gravar um emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo, a situaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o do mesmo deve passar a ficar
			// em execuÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o.
//			alterarSituacaoEmprestimoParaEmExecucao(obj);

			// Se for renovaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o, a data ÃƒÂ¯Ã‚Â¿Ã‚Â½ a data do dia.
			alterarDataEmprestimoRenovacaoParaDataDia(obj, renovacao);
			
			
			EmprestimoVO.validarDados(obj);
			Emprestimo.incluir(getIdEntidade(), true, usuario);
			obj.realizarUpperCaseDados();
//			if (obj.getCodigo().equals(0)) {
				final String sql = "INSERT INTO Emprestimo( data, atendente, biblioteca, pessoa, valorTotalMulta, tipoPessoa, matricula, unidadeEnsino ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlInserir = arg0.prepareStatement(sql);
						sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
						if (obj.getAtendente().getCodigo().intValue() != 0) {
							sqlInserir.setInt(2, obj.getAtendente().getCodigo().intValue());
						} else {
							sqlInserir.setNull(2, 0);
						}
						if (obj.getBiblioteca().getCodigo().intValue() != 0) {
							sqlInserir.setInt(3, obj.getBiblioteca().getCodigo().intValue());
						} else {
							sqlInserir.setNull(3, 0);
						}
						if (obj.getPessoa().getCodigo().intValue() != 0) {
							sqlInserir.setInt(4, obj.getPessoa().getCodigo().intValue());
						} else {
							sqlInserir.setNull(4, 0);
						}
						sqlInserir.setDouble(5, obj.getValorTotalMulta().doubleValue());
						sqlInserir.setString(6, obj.getTipoPessoa());
						if (obj.isTipoPessoaAluno() && !obj.getMatricula().getMatricula().trim().isEmpty()) {
							sqlInserir.setString(7, obj.getMatricula().getMatricula());
						} else {
							sqlInserir.setNull(7, 0);
						}
						if (!obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
							sqlInserir.setInt(8, obj.getUnidadeEnsinoVO().getCodigo());
						} else {
							sqlInserir.setNull(8, 0);
						}
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
//			}

			// Ao gravar um emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo, as seguintes situaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½es devem mudar.
			// SituaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½es dos items do EmprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo ficam em
			// execuÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o, e os itens sÃƒÂ¯Ã‚Â¿Ã‚Â½o incluÃƒÂ¯Ã‚Â¿Ã‚Â½dos no banco.
			getFacadeFactory().getItemEmprestimoFacade().alterarSituacaoItensEmprestimoParaEmExecucao(obj.getItemEmprestimoVOs());
			getFacadeFactory().getItemEmprestimoFacade().incluirItemEmprestimos(obj.getCodigo(), obj.getItemEmprestimoVOs(), usuario);

			// E situaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de cada exemplar fica emprestado.
			getFacadeFactory().getExemplarFacade().alterarSituacaoExemplaresParaEmprestado(obj.getItemEmprestimoVOs(), usuario);

			// TambÃƒÂ¯Ã‚Â¿Ã‚Â½m deve ser registrado um histÃƒÂ¯Ã‚Â¿Ã‚Â½rico do Exemplar.
			getFacadeFactory().getHistoricoExemplarFacade().registrarHistoricoExemplarParaEmprestimo(obj, renovacao);

			// Grava na Tabela ItensRenovados um novo registro se for um
			// emprÃƒÂ¯Ã‚Â¿Ã‚Â½stimo novo, ou incrementa o campo
			// nrRenovacao se for uma renovaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o.
			getFacadeFactory().getItensRenovadosFacade().montarItensRenovacao(obj, renovacao);

			obj.setNovoObj(Boolean.FALSE);

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setNovoObj(true);	
			throw e;
		}
	}

	public List<EmprestimoVO> consultarPorTipoPessoa(String valorConsulta, String tipoPessoa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM emprestimo INNER JOIN pessoa ON emprestimo.pessoa = pessoa.codigo " + "WHERE emprestimo.tipopessoa = '" + tipoPessoa + "' AND UPPER (pessoa.nome) LIKE '" + valorConsulta.toUpperCase() + "%' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<EmprestimoVO> consultarPorCodigoBarraExemplar(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT emprestimo.* FROM emprestimo LEFT JOIN itemEmprestimo ON emprestimo.codigo = itemEmprestimo.emprestimo " + "LEFT JOIN exemplar ON itemEmprestimo.exemplar = exemplar.codigo WHERE exemplar.codigoBarra = '" + valorConsulta + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por realizar uma consulta de <code>Emprestimo</code> atravÃƒÂ¯Ã‚Â¿Ã‚Â½s
	 * do valor do atributo <code>nome</code> da classe <code>Biblioteca</code>
	 * Faz uso da operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vÃƒÂ¯Ã‚Â¿Ã‚Â½rios objetos da classe <code>EmprestimoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexÃƒÂ¯Ã‚Â¿Ã‚Â½o ou restriÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de acesso.
	 */
	public List consultarPorNomeBiblioteca(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Emprestimo.* FROM Emprestimo, Biblioteca WHERE Emprestimo.biblioteca = Biblioteca.codigo and upper( Biblioteca.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Biblioteca.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por realizar uma consulta de <code>Emprestimo</code> atravÃƒÂ¯Ã‚Â¿Ã‚Â½s
	 * do valor do atributo <code>String situacao</code>. Retorna os objetos,
	 * com inÃƒÂ¯Ã‚Â¿Ã‚Â½cio do valor do atributo idÃƒÂ¯Ã‚Â¿Ã‚Â½ntico ao parÃƒÂ¯Ã‚Â¿Ã‚Â½metro fornecido. Faz uso
	 * da operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o deverÃƒÂ¯Ã‚Â¿Ã‚Â½ verificar se o usuÃƒÂ¯Ã‚Â¿Ã‚Â½rio possui
	 *            permissÃƒÂ¯Ã‚Â¿Ã‚Â½o para esta consulta ou nÃƒÂ¯Ã‚Â¿Ã‚Â½o.
	 * @return List Contendo vÃƒÂ¯Ã‚Â¿Ã‚Â½rios objetos da classe <code>EmprestimoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexÃƒÂ¯Ã‚Â¿Ã‚Â½o ou restriÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de acesso.
	 */
	public List consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Emprestimo WHERE upper( situacao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por realizar uma consulta de <code>Emprestimo</code> atravÃƒÂ¯Ã‚Â¿Ã‚Â½s
	 * do valor do atributo <code>nome</code> da classe <code>Usuario</code> Faz
	 * uso da operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 * 
	 * @return List Contendo vÃƒÂ¯Ã‚Â¿Ã‚Â½rios objetos da classe <code>EmprestimoVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexÃƒÂ¯Ã‚Â¿Ã‚Â½o ou restriÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de acesso.
	 */
	public List consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Emprestimo.* FROM Emprestimo, Usuario WHERE Emprestimo.atendente = Usuario.codigo and upper( Usuario.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Usuario.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por realizar uma consulta de <code>Emprestimo</code> atravÃƒÂ¯Ã‚Â¿Ã‚Â½s
	 * do valor do atributo <code>Date data</code>. Retorna os objetos com
	 * valores pertecentes ao perÃƒÂ¯Ã‚Â¿Ã‚Â½odo informado por parÃƒÂ¯Ã‚Â¿Ã‚Â½metro. Faz uso da
	 * operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o deverÃƒÂ¯Ã‚Â¿Ã‚Â½ verificar se o usuÃƒÂ¯Ã‚Â¿Ã‚Â½rio possui
	 *            permissÃƒÂ¯Ã‚Â¿Ã‚Â½o para esta consulta ou nÃƒÂ¯Ã‚Â¿Ã‚Â½o.
	 * @return List Contendo vÃƒÂ¯Ã‚Â¿Ã‚Â½rios objetos da classe <code>EmprestimoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexÃƒÂ¯Ã‚Â¿Ã‚Â½o ou restriÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Emprestimo WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por realizar uma consulta de <code>Emprestimo</code> atravÃƒÂ¯Ã‚Â¿Ã‚Â½s
	 * do valor do atributo <code>Integer codigo</code>. Retorna os objetos com
	 * valores iguais ou superiores ao parÃƒÂ¯Ã‚Â¿Ã‚Â½metro fornecido. Faz uso da operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o deverÃƒÂ¯Ã‚Â¿Ã‚Â½ verificar se o usuÃƒÂ¯Ã‚Â¿Ã‚Â½rio possui
	 *            permissÃƒÂ¯Ã‚Â¿Ã‚Â½o para esta consulta ou nÃƒÂ¯Ã‚Â¿Ã‚Â½o.
	 * @return List Contendo vÃƒÂ¯Ã‚Â¿Ã‚Â½rios objetos da classe <code>EmprestimoVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexÃƒÂ¯Ã‚Â¿Ã‚Â½o ou restriÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Emprestimo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<EmprestimoVO> consultarPorCodigoSituacaoEmExecucao(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Emprestimo WHERE codigo >= " + valorConsulta.intValue() + " AND situacao = '" + situacao + "' ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por montar os dados de vÃƒÂ¯Ã‚Â¿Ã‚Â½rios objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vÃƒÂ¯Ã‚Â¿Ã‚Â½rios objetos da classe <code>EmprestimoVO</code>
	 *         resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * ResponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>EmprestimoVO</code>.
	 * 
	 * @return O objeto da classe <code>EmprestimoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static EmprestimoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		EmprestimoVO obj = new EmprestimoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setData(dadosSQL.getDate("data"));
		obj.getAtendente().setCodigo(dadosSQL.getInt("atendente"));
//		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getBiblioteca().setCodigo(dadosSQL.getInt("biblioteca"));
		obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
		obj.setValorTotalMulta(dadosSQL.getDouble("valorTotalMulta"));
		obj.setTipoPessoa(dadosSQL.getString("tipoPessoa"));
		obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosAtendente(obj, nivelMontarDados, usuario);
		montarDadosBiblioteca(obj, nivelMontarDados, usuario);
		montarDadosPessoa(obj, nivelMontarDados, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setItemEmprestimoVOs(getFacadeFactory().getItemEmprestimoFacade().consultarItemEmprestimos(obj.getCodigo(), false, nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		return obj;
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o responsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>EmprestimoVO</code>.
	 * Faz uso da chave primÃƒÂ¯Ã‚Â¿Ã‚Â½ria da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual serÃƒÂ¯Ã‚Â¿Ã‚Â½ montado os dados consultados.
	 */
	public static void montarDadosPessoa(EmprestimoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPessoa().getCodigo().intValue() == 0) {
			obj.setPessoa(new PessoaVO());
			return;
		}
		obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o responsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por montar os dados de um objeto da classe
	 * <code>BibliotecaVO</code> relacionado ao objeto <code>EmprestimoVO</code>
	 * . Faz uso da chave primÃƒÂ¯Ã‚Â¿Ã‚Â½ria da classe <code>BibliotecaVO</code> para
	 * realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual serÃƒÂ¯Ã‚Â¿Ã‚Â½ montado os dados consultados.
	 */
	public static void montarDadosBiblioteca(EmprestimoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getBiblioteca().getCodigo().intValue() == 0) {
			obj.setBiblioteca(new BibliotecaVO());
			return;
		}
		obj.setBiblioteca(getFacadeFactory().getBibliotecaFacade().consultarPorChavePrimaria(obj.getBiblioteca().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o responsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por montar os dados de um objeto da classe
	 * <code>UsuarioVO</code> relacionado ao objeto <code>EmprestimoVO</code>.
	 * Faz uso da chave primÃƒÂ¯Ã‚Â¿Ã‚Â½ria da classe <code>UsuarioVO</code> para realizar
	 * a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual serÃƒÂ¯Ã‚Â¿Ã‚Â½ montado os dados consultados.
	 */
	public static void montarDadosAtendente(EmprestimoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getAtendente().getCodigo().intValue() == 0) {
			obj.setAtendente(new UsuarioVO());
			return;
		}
		obj.setAtendente(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getAtendente().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o responsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por localizar um objeto da classe
	 * <code>EmprestimoVO</code> atravÃƒÂ¯Ã‚Â¿Ã‚Â½s de sua chave primÃƒÂ¯Ã‚Â¿Ã‚Â½ria.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexÃƒÂ¯Ã‚Â¿Ã‚Â½o ou localizaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o do objeto
	 *                procurado.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public EmprestimoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Emprestimo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados NÃ£o Encontrados ( Emprestimo ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o reponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por retornar o identificador desta classe. Este
	 * identificar ÃƒÂ¯Ã‚Â¿Ã‚Â½ utilizado para verificar as permissÃƒÂ¯Ã‚Â¿Ã‚Â½es de acesso as
	 * operaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½es desta classe.
	 */
	public static String getIdEntidade() {
		return Emprestimo.idEntidade;
	}

	/**
	 * OperaÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o reponsÃƒÂ¯Ã‚Â¿Ã‚Â½vel por definir um novo valor para o identificador desta
	 * classe. Esta alteraÃƒÂ¯Ã‚Â¿Ã‚Â½ÃƒÂ¯Ã‚Â¿Ã‚Â½o deve ser possÃƒÂ¯Ã‚Â¿Ã‚Â½vel, pois, uma mesma classe de
	 * negÃƒÂ¯Ã‚Â¿Ã‚Â½cio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso ÃƒÂ¯Ã‚Â¿Ã‚Â½ realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Emprestimo.idEntidade = idEntidade;
	}

//	@SuppressWarnings("unchecked")
//	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
//	public List executarConsultaTelaBuscaEmprestimoEmAberto(String valorConsulta, UsuarioVO usuarioLogado, boolean apenasEmprestimosEmAberto) throws Exception {
//		List<ItemEmprestimoVO> lista = new ArrayList<ItemEmprestimoVO>(0);
//		try {
//
//			// if (valorConsulta.trim().length() < 3) {
//			// throw new
//			// Exception("Para efetuar a consulta deve ser informado no mÃƒÂ¯Ã‚Â¿Ã‚Â½nimo 1 Palavra. ");
//			// }
//
//			StringBuilder valor = new StringBuilder(0);
//			String[] termos = valorConsulta.split("\\s");
//
//			if (!valorConsulta.equals("")) {
//				if (termos.length == 1) {
//					valor.append(valorConsulta).append("~");
//				} else {
//					for (String str : termos) {
//						valor.append(str).append("~ ");
//					}
//				}
//			} else {
//				valor.append("*");
//			}
//			String campos[] = new String[] { "itememprestimo.codigo", "exemplar.catalogo.titulo", "exemplar.catalogo.isbn", "exemplar.catalogo.issn", "exemplar.catalogo.subtitulo", "exemplar.catalogo.assunto", "autor.nome" };
//
//			BooleanQuery booleanQuery = new BooleanQuery();
//			MultiFieldQueryParser query = new MultiFieldQueryParser(campos, new StandardAnalyzer());
//
//			query.setFuzzyMinSim(0.5f);
//			query.setAllowLeadingWildcard(true);
//
//			BooleanQuery queryUsuario = new BooleanQuery();
//			queryUsuario.add(new TermQuery(new Term("emprestimo.pessoa.codigo", usuarioLogado.getPessoa().getCodigo().toString())), BooleanClause.Occur.MUST);
//
//			if (apenasEmprestimosEmAberto) {
//				BooleanQuery queryEX = new BooleanQuery();
//				queryEX.add(new TermQuery(new Term("itememprestimo.situacao", "EX")), BooleanClause.Occur.MUST);
//				queryEX.add(query.parse(valor.toString()), BooleanClause.Occur.MUST);
//				queryEX.add(queryUsuario, BooleanClause.Occur.MUST);
////				BooleanQuery queryAT = new BooleanQuery();
////				queryAT.add(new TermQuery(new Term("situacao", "DE")), BooleanClause.Occur.MUST);
////				queryAT.add(query.parse(valor.toString()), BooleanClause.Occur.MUST);
////				queryAT.add(queryUsuario, BooleanClause.Occur.MUST);
////				BooleanQuery queryRE = new BooleanQuery();
////				queryRE.add(new TermQuery(new Term("situacao", "RE")), BooleanClause.Occur.MUST);
////				queryRE.add(query.parse(valor.toString()), BooleanClause.Occur.MUST);
////				queryRE.add(queryUsuario, BooleanClause.Occur.MUST);
//				booleanQuery.add(queryEX, BooleanClause.Occur.SHOULD);
////				booleanQuery.add(queryAT, BooleanClause.Occur.SHOULD);
////				booleanQuery.add(queryRE, BooleanClause.Occur.SHOULD);
//			} else {
//				booleanQuery.add(query.parse(valor.toString()), BooleanClause.Occur.MUST);
//				booleanQuery.add(queryUsuario, BooleanClause.Occur.MUST);
//			}
//
//			Sort sort = new Sort(new SortField("emprestimo.data", SortField.LONG, true));
//
//			FullTextQuery fullTextQuery = getFullTextSession().createFullTextQuery(booleanQuery, CatalogoVO.class, ItemEmprestimoVO.class);
//			fullTextQuery.setSort(sort);
//
//			lista = fullTextQuery.list(); // return a list of managed objects
//			return lista;
//		} catch (Exception ex) {
//			throw ex;
//		} finally {
//			lista = null;
//		}
//	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<EmprestimoVO> consultarAtivosEAtrasados(Integer unidadeEnsino) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select nome, codigo, sum(qtdAtivo) as qtdAtivo, sum(qtdAtrasado) as qtdAtrasado from(");
		sqlStr.append(" SELECT b.nome, b.codigo,  case when current_date <= dataPrevisaodevolucao then count(distinct emprestimo.codigo) end as qtdAtivo,");
		sqlStr.append(" case when current_date > dataPrevisaodevolucao then count(distinct emprestimo.codigo) end as qtdAtrasado");
		sqlStr.append(" FROM emprestimo");
		sqlStr.append(" inner join itememprestimo on itememprestimo.emprestimo = emprestimo.codigo");
		sqlStr.append(" inner join biblioteca b on emprestimo.biblioteca = b.codigo");
		sqlStr.append(" left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = b.codigo  ");
		sqlStr.append(" where itememprestimo.situacao = 'EX'");
		if (unidadeEnsino > 0) {
			sqlStr.append(" and unidadeensinobiblioteca.unidadeEnsino = ").append(unidadeEnsino);
		}
		sqlStr.append(" group by b.nome, b.codigo, dataPrevisaodevolucao");
		sqlStr.append(" ORDER BY b.nome) as t group by nome, codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			EmprestimoVO obj = new EmprestimoVO();
			obj.getBiblioteca().setCodigo(tabelaResultado.getInt("codigo"));
			obj.getBiblioteca().setNome(tabelaResultado.getString("nome"));
			obj.setQtdAtivo(tabelaResultado.getInt("qtdAtivo"));
			obj.setQtdAtrasado(tabelaResultado.getInt("qtdAtrasado"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<EmprestimoVO> consultarAtrasadosPorBibliotecaSituacaoEmExecucao(Integer codigobiblioteca, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT distinct e.*  from emprestimo e ");
		sqlStr.append("inner join itememprestimo ie on ie.emprestimo = e.codigo ");
		sqlStr.append("left join unidadeensinobiblioteca on unidadeensinobiblioteca.biblioteca = e.biblioteca ");		
		sqlStr.append("where current_date > ie.dataPrevisaodevolucao AND ie.situacao = 'EX' ");
		sqlStr.append("and e.biblioteca =").append(codigobiblioteca);
		if (unidadeEnsino > 0) {
			sqlStr.append(" and unidadeensinobiblioteca.unidadeEnsino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<EmprestimoVO> consultarAtivosEAtrasadosPorUnidadeEnsinoENivelEducacional(Integer unidadeEnsino, String nivelEducacional) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT case when c.nome is null then 'Professores e FuncionÃ¡rio' else c.nome end AS nomeCurso, c.codigo AS codigoCurso, ");
		sqlStr.append(" count(case when current_date <= dataPrevisaodevolucao then 1 else 0 end) AS qtdAtivo, ");
		sqlStr.append(" count(case when current_date > dataPrevisaodevolucao then 1 else 0 end) AS qtdAtrasado ");
		sqlStr.append(" FROM emprestimo e ");
		sqlStr.append(" INNER JOIN itemEmprestimo ie ON ie.emprestimo = e.codigo ");
		sqlStr.append(" left JOIN matricula m ON m.matricula = e.matricula ");
		sqlStr.append(" left JOIN curso c ON m.curso = c.codigo ");
		sqlStr.append(" WHERE 1=1 AND ie.situacao = 'EX' ");
		if (unidadeEnsino != 0) {
			sqlStr.append(" AND m.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (!nivelEducacional.equals("") && !nivelEducacional.equals("0")) {
			sqlStr.append(" AND c.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sqlStr.append(" group by c.nome, c.codigo");
		sqlStr.append(" ORDER BY nomeCurso ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			EmprestimoVO obj = new EmprestimoVO();
			obj.setNomeCurso(tabelaResultado.getString("nomeCurso"));
			obj.setCodigoCurso(tabelaResultado.getInt("codigoCurso"));
			obj.setQtdAtivo(tabelaResultado.getInt("qtdAtivo"));
			obj.setQtdAtrasado(tabelaResultado.getInt("qtdAtrasado"));
			vetResultado.add(obj);
		}
		return vetResultado;

	}

	public List<EmprestimoVO> consultarAtrasadosPorCursoSituacaoEmExecucao(String nomeCurso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT * from emprestimo e ");
		sqlStr.append("inner join itememprestimo ie on ie.emprestimo = e.codigo ");
		sqlStr.append("INNER JOIN pessoa p ON e.pessoa = p.codigo ");
		sqlStr.append("INNER JOIN matricula m ON m.aluno = p.codigo ");
		sqlStr.append("INNER JOIN curso c ON m.curso = c.codigo ");
		sqlStr.append("where current_date > ie.dataPrevisaodevolucao AND ie.situacao = 'EX' ");
		sqlStr.append("and c.nome = '").append(nomeCurso).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<EmprestimoVO> consultarAtrasadosParaNotificacao(Integer numeroDias, String notificacao, int nivelMontarDados) throws Exception {
		List<EmprestimoVO> objs = new ArrayList<EmprestimoVO>(0);
		if (numeroDias != null && numeroDias > 0 && notificacao != null && !notificacao.isEmpty()) {
			StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append("SELECT DISTINCT ");
			sqlStr.append("emprestimo.codigo,emprestimo.tipopessoa, emprestimo.unidadeensino, ");
			sqlStr.append("biblioteca.codigo AS bibliotecacodigo,biblioteca.nome as bibliotecanome,");
			sqlStr.append("pessoa.codigo AS pessoacodigo,pessoa.nome,pessoa.email ");
			sqlStr.append("FROM emprestimo ");
			sqlStr.append("INNER JOIN itememprestimo  on itememprestimo.emprestimo = emprestimo.codigo ");
			sqlStr.append("INNER JOIN pessoa ON emprestimo.pessoa = pessoa.codigo ");
			sqlStr.append("INNER JOIN biblioteca ON biblioteca.codigo = emprestimo.biblioteca ");
			sqlStr.append("WHERE  (itememprestimo.dataPrevisaodevolucao + INTERVAL '" + numeroDias + " DAY') ");
			sqlStr.append("< '2012-09-07' AND itememprestimo.datadevolucao is null ");
			// sqlStr.append("< '"+Uteis.getDataJDBC(new
			// Date())+"' AND itememprestimo.datadevolucao is null ");
//			sqlStr.append("AND  itememprestimo.situacao <> 'DE' AND emprestimo.situacao <> 'FI' and pessoa.codigo is not null ");
			sqlStr.append("AND  itememprestimo.situacao <> 'DE' and pessoa.codigo is not null ");
			SqlRowSet tabelaResultado;
			if (notificacao.equals("PRIMEIRA")) {
				sqlStr.append("and itememprestimo.dataprimeiranotificacao is null");
				tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
				// objs = (montarDadosConsulta(tabelaResultado,
				// nivelMontarDados,new UsuarioVO()));
				objs = montarDadosEmprestimoJob(tabelaResultado);
			} else {
				if (notificacao.equals("SEGUNDA")) {
					sqlStr.append("and itememprestimo.datasegundanotificacao is null and itememprestimo.dataprimeiranotificacao is not null");
					tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
					// objs = (montarDadosConsulta(tabelaResultado,
					// nivelMontarDados,new UsuarioVO()));
					objs = montarDadosEmprestimoJob(tabelaResultado);

				} else {
					if (notificacao.equals("TERCEIRA")) {
						sqlStr.append("and itememprestimo.dataterceiranotificacao is null and itememprestimo.datasegundanotificacao is not null");
						tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
						// objs = (montarDadosConsulta(tabelaResultado,
						// nivelMontarDados,new UsuarioVO()));
						objs = montarDadosEmprestimoJob(tabelaResultado);

					}
				}

			}
		}
		return objs;
	}

	public List<EmprestimoVO> montarDadosEmprestimoJob(SqlRowSet dadosSQL) throws Exception {
		List<EmprestimoVO> objs = new ArrayList<EmprestimoVO>(0);
		while (dadosSQL.next()) {
			EmprestimoVO obj = new EmprestimoVO();
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setTipoPessoa(dadosSQL.getString("tipopessoa"));
			obj.setBiblioteca(new BibliotecaVO());
			obj.getBiblioteca().setNome(dadosSQL.getString("bibliotecanome"));
			obj.getBiblioteca().setCodigo(dadosSQL.getInt("bibliotecacodigo"));
			obj.setPessoa(new PessoaVO());
			obj.getPessoa().setCodigo(dadosSQL.getInt("pessoacodigo"));
			obj.getPessoa().setNome(dadosSQL.getString("nome"));
			obj.getPessoa().setEmail(dadosSQL.getString("email"));
			obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensino"));
			obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
			obj.setItemEmprestimoVOs(getFacadeFactory().getItemEmprestimoFacade().consultarItensEmprestimoPorCodigoEmprestimoJob(obj.getCodigo()));
			objs.add(obj);
		}
		return objs;
	}

	public String realizarCriacaoComprovanteEmprestimo(List<TicketRelVO> listaTicketRelVOs, String unidadeEnsino, EmprestimoVO emprestimoVO, UsuarioVO usuarioVO) throws Exception {
		String textoComprovante = "";
		Integer index = 1;
		for (TicketRelVO ticketRelVO : listaTicketRelVOs) {

			if (!ticketRelVO.getNomeBiblioteca().equals("")) {
				textoComprovante += Uteis.removeCaractersEspeciais(ticketRelVO.getNomeBiblioteca().toUpperCase()) + ">";
			}
			if (!ticketRelVO.getNomePessoa().equals("")) {
				textoComprovante += "SOLICITANTE: " + Uteis.removeCaractersEspeciais(ticketRelVO.getNomePessoa().toUpperCase()) + ">";
				textoComprovante += "MATRICULA: " + Uteis.removeCaractersEspeciais(ticketRelVO.getNumeroMatricula()) + ">";
			}
			if (!unidadeEnsino.equals("")) {
				textoComprovante += "UNIDADE DE ENS.: " + Uteis.removeCaractersEspeciais(unidadeEnsino.toUpperCase()) + ">";
			}
			textoComprovante += "DATA: " + UteisData.getDataComHoraAtual() + ">";
			textoComprovante += "---------------------------------------->";
			if (!ticketRelVO.getItemEmprestimoVOs().isEmpty()) {
				textoComprovante += "EMPRESTIMO(S):>";
				for (ItemEmprestimoVO itemEmprestimoVO : ticketRelVO.getItemEmprestimoVOs()) {
					textoComprovante += +index + "- " + "DT EMP: " + itemEmprestimoVO.getEmprestimo().getData_Apresentar() + "";
					textoComprovante += " DT PREV DEV: " + itemEmprestimoVO.getDataPrevisaoDevolucao_Apresentar() + ">";					
					textoComprovante += "TITULO: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getCatalogo().getTitulo().toUpperCase())) + ">";
					textoComprovante += "TOMBO: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getCodigoBarra())) + ">";
					textoComprovante += " >";
					textoComprovante += "VOL: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getVolume())) + "    EX: "+itemEmprestimoVO.getExemplar().getNumeroExemplar()+ ">";
					index++;
				}
				textoComprovante += "---------------------------------------->";
				if (!emprestimoVO.getBiblioteca().getCodigo().equals(0)) {
					ConfiguracaoBibliotecaVO configuracaoBibliotecaVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPorBiblioteca(emprestimoVO.getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (!configuracaoBibliotecaVO.getCodigo().equals(0)) {
						if (!configuracaoBibliotecaVO.getTextoPadraoEmprestimo().equals("")) {
							textoComprovante += Uteis.removerAcentos(configuracaoBibliotecaVO.getTextoPadraoEmprestimo().toUpperCase() + ">");
						}
					}
					textoComprovante += "                                               >";
				}
			} 
			if(!ticketRelVO.getItemEmprestimoVOsDevolucao().isEmpty()) {
				textoComprovante += "DEVOLUCAO(OES):>";
				for (ItemEmprestimoVO itemEmprestimoVO : ticketRelVO.getItemEmprestimoVOsDevolucao()) {
					textoComprovante += +index + "- " + "DT EMP: " + itemEmprestimoVO.getEmprestimo().getData_Apresentar() + "";
					textoComprovante += " DATA DEV: " + itemEmprestimoVO.getDataDevolucao_Apresentar() + ">";
					textoComprovante += "TITULO: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getCatalogo().getTitulo().toUpperCase())) + ">";
					textoComprovante += "TOMBO: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getCodigoBarra())) + ">";
					textoComprovante += " >";
					textoComprovante += "VOL: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getVolume())) + "    EX: "+itemEmprestimoVO.getExemplar().getNumeroExemplar()+ ">";
					index++;
				}
				textoComprovante += "---------------------------------------->";
				if (!emprestimoVO.getBiblioteca().getCodigo().equals(0)) {
					ConfiguracaoBibliotecaVO configuracaoBibliotecaVO = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPorBiblioteca(emprestimoVO.getBiblioteca().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (!configuracaoBibliotecaVO.getCodigo().equals(0)) {
						if (!configuracaoBibliotecaVO.getTextoPadraoDevolucao().equals("")) {
							textoComprovante += Uteis.removerAcentos(configuracaoBibliotecaVO.getTextoPadraoDevolucao().toUpperCase() + ">");
						}
					}
					textoComprovante += "                                               >";
				}
			} 
			if(!ticketRelVO.getItemEmprestimoVOsRenovacao().isEmpty()){
				textoComprovante += "RENOVACAO(OES):>";
				for (ItemEmprestimoVO itemEmprestimoVO : ticketRelVO.getItemEmprestimoVOsRenovacao()) {
					textoComprovante += +index + "- " + "DT REN: " + itemEmprestimoVO.getDataDevolucao_Apresentar() + "";
					if(itemEmprestimoVO.getDataPrevistaDevolucaoTemp() != null 
							&& (itemEmprestimoVO.getDataPrevistaDevolucaoTemp().compareTo(itemEmprestimoVO.getDataPrevisaoDevolucao()) >= 0
							|| Uteis.getData(itemEmprestimoVO.getDataPrevistaDevolucaoTemp()).equals( Uteis.getData(itemEmprestimoVO.getDataPrevisaoDevolucao())))) {
						textoComprovante += " DT PREV DEV: " + itemEmprestimoVO.getDataPrevistaDevolucaoTemp_Apresentar() + ">";
					}else {						
						textoComprovante += " DT PREV DEV: " + itemEmprestimoVO.getDataPrevisaoDevolucao_Apresentar() + ">";
					}
					textoComprovante += "TITULO: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getCatalogo().getTitulo().toUpperCase())) + ">";
					textoComprovante += "TOMBO: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getCodigoBarra())) + ">";
					textoComprovante += " >";
					textoComprovante += "VOL: " + Uteis.removerAcentos(Uteis.removeCaractersEspeciais(itemEmprestimoVO.getExemplar().getVolume())) + "    EX: "+itemEmprestimoVO.getExemplar().getNumeroExemplar()+ ">";
					index++;
				}
				textoComprovante += "---------------------------------------->";
				textoComprovante += "                                               >";
			}
			
			textoComprovante += "&&&&&_______________________________&&&&&>";
			String nomePessoa = Uteis.removeCaractersEspeciais(ticketRelVO.getNomePessoa().toUpperCase());
			if(nomePessoa.length() < 42){
				int espaco = (42 - nomePessoa.length()) / 2 ;
				for(int x = 1; x <= espaco; x++){
					nomePessoa = "&"+nomePessoa+"&";
				}
			}
			textoComprovante += nomePessoa + ">";
			textoComprovante += "                                               >";
			textoComprovante += "&&&&&_______________________________&&&&&>";
			
			String nomeUsuario = Uteis.removeCaractersEspeciais(usuarioVO.getNome_Apresentar());
			if(nomeUsuario.length() < 42){
				int espaco = (42 - nomeUsuario.length()) / 2;
				for(int x = 1; x<=espaco;x++){
					nomeUsuario = "&"+nomeUsuario+"&";
				}
			}
			
			textoComprovante += nomeUsuario + ">";
		}
		incluirPoolImpressao(emprestimoVO, textoComprovante, usuarioVO);
		return textoComprovante;
	}
	
	private void incluirPoolImpressao(EmprestimoVO emprestimoVO, String textoImpressao, UsuarioVO usuario ) throws Exception{
		if(emprestimoVO.getBiblioteca().getIsImpressaoPorPool()){
			PoolImpressaoVO poolImpressaoVO = new PoolImpressaoVO();
			poolImpressaoVO.setData(new Date());
			poolImpressaoVO.setImprimir(textoImpressao);
			poolImpressaoVO.setFormatoImpressao(FormatoImpressaoEnum.TEXTO);
			poolImpressaoVO.setImpressoraVO(emprestimoVO.getImpressoraVO());
			getFacadeFactory().getPoolImpressaoFacade().incluir(poolImpressaoVO, usuario);
		}
	}
	
	@Override
	public String consultarMatriculaAlunoPorCodigoExemplar(Integer codigoBarra, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select matricula from emprestimo e ");
		sqlStr.append(" inner join pessoa p on p.codigo = e.pessoa ");
		sqlStr.append(" inner join itememprestimo ie on e.codigo = ie.emprestimo ");
		sqlStr.append(" where ie.exemplar = ").append(codigoBarra);
		sqlStr.append(" and ie.situacao <> 'DE'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		String matricula = "";
		if(tabelaResultado.next()) {
			matricula = tabelaResultado.getString("matricula");
		}
		return matricula;
	}
	
	@Override
	public String consultarMatriculaFuncionarioProfessorPorCodigoExemplar(Integer codigoBarra, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select f.matricula from emprestimo e ");
		sqlStr.append(" inner join pessoa p on p.codigo = e.pessoa ");
		sqlStr.append(" inner join funcionario f on f.pessoa = e.pessoa ");
		sqlStr.append(" inner join itememprestimo ie on e.codigo = ie.emprestimo ");
		sqlStr.append(" where ie.exemplar = ").append(codigoBarra);
		sqlStr.append(" and ie.situacao <> 'DE'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		String matricula = "";
		if(tabelaResultado.next()) {
			matricula = tabelaResultado.getString("matricula");
		}
		return matricula;
	}
	
	@Override
	public String consultarMatriculaFuncionarioProfessorPorCodigoPessoa(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select f.matricula from emprestimo e ");
		sqlStr.append(" inner join pessoa p on p.codigo = e.pessoa ");
		sqlStr.append(" inner join funcionario f on f.pessoa = e.pessoa ");
		sqlStr.append(" where p.codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		String matricula = "";
		if(tabelaResultado.next()) {
			matricula = tabelaResultado.getString("matricula");
		}
		return matricula;
	}
	
	public EmprestimoVO consultarResumoEmprestimoPorCodigoExemplar(Integer exemplar, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select emprestimo.matricula, pessoa.nome, itememprestimo.dataprevisaodevolucao from itememprestimo ");
		sqlStr.append(" inner join emprestimo on emprestimo.codigo = itememprestimo.emprestimo ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = emprestimo.pessoa ");
		sqlStr.append(" inner join exemplar on exemplar.codigo = itememprestimo.exemplar ");
		sqlStr.append(" where exemplar.codigo = ").append(exemplar);
		sqlStr.append(" and situacao = 'EX' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		EmprestimoVO emprestimoVO = new EmprestimoVO();
		if(tabelaResultado.next()) {
			emprestimoVO.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
			emprestimoVO.getPessoa().setNome(tabelaResultado.getString("nome"));
			ItemEmprestimoVO itemEmprestimoVO = new ItemEmprestimoVO();
			itemEmprestimoVO.setDataPrevisaoDevolucao(tabelaResultado.getDate("dataprevisaodevolucao"));
			emprestimoVO.getItemEmprestimoVOs().add(itemEmprestimoVO);
		}
		
		return emprestimoVO;
	}
	
	public List<EmprestimoVO> consultarEmprestimoNotificacaoPrazoDevolucao() throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT emprestimo.codigo, emprestimo.tipopessoa, emprestimo.matricula, biblioteca.codigo AS bibliotecacodigo, biblioteca.nome as bibliotecanome, emprestimo.unidadeensino, ");
		sqlStr.append(" pessoa.codigo AS pessoacodigo, pessoa.nome, pessoa.email ");
		sqlStr.append(" FROM emprestimo ");
		sqlStr.append(" INNER JOIN itememprestimo on itememprestimo.emprestimo = emprestimo.codigo ");
		sqlStr.append(" INNER JOIN pessoa ON emprestimo.pessoa = pessoa.codigo ");
		sqlStr.append(" INNER JOIN biblioteca ON biblioteca.codigo = emprestimo.biblioteca ");
		sqlStr.append(" WHERE itememprestimo.situacao = 'EX' AND itememprestimo.datadevolucao IS NULL ");
		sqlStr.append(" AND itememprestimo.dataprevisaodevolucao::DATE - ( ");
		sqlStr.append(" 	SELECT configuracaobiblioteca.quantidadediasantesnotificarprazodevolucao FROM biblioteca ");
		sqlStr.append(" 	INNER JOIN ( ");
		sqlStr.append(" 		SELECT 1 AS ordem, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca FROM matricula ");
		sqlStr.append(" 		INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" 		INNER JOIN configuracaobibliotecaniveleducacional ON configuracaobibliotecaniveleducacional.biblioteca = emprestimo.biblioteca ");
		sqlStr.append(" 		AND configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" 		AND configuracaobibliotecaniveleducacional.niveleducacional = curso.niveleducacional ");
		sqlStr.append(" 		WHERE matricula.matricula = emprestimo.matricula ");
		sqlStr.append(" 		UNION ");
		sqlStr.append(" 		SELECT 2, configuracaobibliotecaniveleducacional.configuracaobiblioteca, configuracaobibliotecaniveleducacional.biblioteca FROM matricula ");
		sqlStr.append(" 		INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" 		INNER JOIN configuracaobibliotecaniveleducacional ON configuracaobibliotecaniveleducacional.biblioteca = emprestimo.biblioteca ");
		sqlStr.append(" 		AND configuracaobibliotecaniveleducacional.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" 		AND (configuracaobibliotecaniveleducacional.niveleducacional IS NULL OR configuracaobibliotecaniveleducacional.niveleducacional = '') ");
		sqlStr.append(" 		WHERE matricula.matricula = emprestimo.matricula ");
		sqlStr.append(" 		UNION ");
		sqlStr.append(" 		SELECT 3, biblioteca.configuracaobiblioteca, biblioteca.codigo FROM biblioteca WHERE codigo = emprestimo.biblioteca ");
		sqlStr.append(" 		ORDER BY ordem LIMIT 1 ");
		sqlStr.append(" 	) AS configuracaobibliotecautilizar ON configuracaobibliotecautilizar.biblioteca = biblioteca.codigo ");
		sqlStr.append(" 	INNER JOIN configuracaobiblioteca ON configuracaobiblioteca.codigo = configuracaobibliotecautilizar.configuracaobiblioteca ");
		sqlStr.append(" 	WHERE biblioteca.codigo = emprestimo.biblioteca ");
		sqlStr.append(" ) = current_date ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosEmprestimoJob(tabelaResultado);
	}
	
	public Integer realizarCalculoQtdeEmprestimosAtrasados(List<ItemEmprestimoVO> itemEmprestimoVOs, Integer pessoa, ConfiguracaoBibliotecaVO confBibVO, UsuarioVO usuarioVO) throws Exception {
		int qtdAtrasados = 0;
		List<ItemEmprestimoVO> itemEmprestimoCalculoQtdeAtrasados = new ArrayList<ItemEmprestimoVO>(0);
		if (confBibVO.getValidarExemplarAtrasadoOutraBiblioteca()) {
			List<ItemEmprestimoVO> listaItensEmprestimoOutrasBiblioteca = getFacadeFactory().getItemEmprestimoFacade().consultarItensEmprestadosOutraBibliotecaPorCodigoPessoa(itemEmprestimoVOs, pessoa, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confBibVO, usuarioVO);;
			itemEmprestimoCalculoQtdeAtrasados.addAll(listaItensEmprestimoOutrasBiblioteca);
			itemEmprestimoCalculoQtdeAtrasados.addAll(itemEmprestimoVOs);
		} else {
			itemEmprestimoCalculoQtdeAtrasados.addAll(itemEmprestimoVOs);
		}
		
		for (ItemEmprestimoVO ie : itemEmprestimoCalculoQtdeAtrasados) {
			if (ie.getIsEmprestimoAtrasado()) {					
				qtdAtrasados++;
			}
		}
		return qtdAtrasados;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE Emprestimo set pessoa=? WHERE ((pessoa = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, pessoaAntigo });
	}
	
	private String executarGeracaoMensagemLimiteEmprestimosExcedidoAluno(List<ItemEmprestimoVO> itensEmprestadosParaPessoa) throws Exception {
		HashMap<String, Integer> qtdeExemplaresBiblioteca = new HashMap<String, Integer>(0);
		StringBuilder emprestimosBibliotecas = new StringBuilder();
		for (ItemEmprestimoVO ieVO : itensEmprestadosParaPessoa) {
			if (qtdeExemplaresBiblioteca.containsKey(ieVO.getEmprestimo().getBiblioteca().getNome())) {
				qtdeExemplaresBiblioteca.put(ieVO.getEmprestimo().getBiblioteca().getNome(), qtdeExemplaresBiblioteca.get(ieVO.getEmprestimo().getBiblioteca().getNome()) + 1);
			} else {
				qtdeExemplaresBiblioteca.put(ieVO.getEmprestimo().getBiblioteca().getNome(), 1);
			}
		}
		int cont = 1;
		for (String biblioteca : qtdeExemplaresBiblioteca.keySet()) {
			emprestimosBibliotecas.append(qtdeExemplaresBiblioteca.get(biblioteca)).append(" empr\u00e9stimo(s) na biblioteca: ").append(biblioteca);
			if (qtdeExemplaresBiblioteca.size() > cont) {
				emprestimosBibliotecas.append(", ");
			}
			cont++;
		}
		return emprestimosBibliotecas.toString();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirEmprestimosFinalizadosPorMatricula(String matricula, Integer aluno, UsuarioVO usuarioVO){
		String sqlStr = "DELETE FROM ItemEmprestimo WHERE  codigo in (select ie.codigo from itememprestimo ie inner join emprestimo e on e.codigo = ie.emprestimo  where ie.situacao != 'EX' and pessoa = ? and matricula = ? ) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sqlStr, aluno, matricula);
		sqlStr = "DELETE FROM Emprestimo WHERE pessoa = ? and matricula = ? and not exists (select ie.codigo from itememprestimo ie where ie.emprestimo = emprestimo.codigo and ie.situacao = 'EX') "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sqlStr, aluno, matricula);
	}
	
	@Override
	public boolean consultaExistenciaEmprestimosEmAbertoPorMatricula(String matricula, Integer aluno){
		String sqlStr = "select codigo FROM Emprestimo WHERE pessoa = ? and matricula = ? and exists (select ie.codigo from itememprestimo ie where ie.emprestimo = emprestimo.codigo and ie.situacao = 'EX') ";
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr, aluno, matricula).next();
		
	}
	
	
public void realizarCriacaoBloqueioBiblioteca(EmprestimoVO emprestimoVO, List<ItemEmprestimoVO> listaItensEmprestimoParaDevolucaoRenovacao, ConfiguracaoBibliotecaVO confPadraoBib, UsuarioVO usuarioVO) throws Exception{
	if(confPadraoBib.getControlaBloqueio(emprestimoVO.getTipoPessoa())){
			StringBuilder motivo = new StringBuilder("");
			Integer maiorData = 0;
			Date dataParaCalculo = new Date();
			for(ItemEmprestimoVO itemEmprestimoVO:listaItensEmprestimoParaDevolucaoRenovacao){			
				if((itemEmprestimoVO.getDevolverEmprestimo() || itemEmprestimoVO.getRenovarEmprestimo()) && itemEmprestimoVO.getEmprestimoEmAtraso()){
					if(motivo.length()>0){
						motivo.append("\n");
					}
					Integer diasAtraso = Uteis.getObterDiferencaDiasEntreDuasData(dataParaCalculo, itemEmprestimoVO.getDataPrevisaoDevolucao());
					if(maiorData < diasAtraso){
						maiorData = diasAtraso;
					}
					motivo.append("Tombo ").append(itemEmprestimoVO.getExemplar().getCodigoBarra()).append(" entregue com ").append(diasAtraso).append(" dia(s) de atraso.");
				}
			}
			if (confPadraoBib.getControlaBloqueioPorDiaEspecifico(emprestimoVO.getTipoPessoa())){
				maiorData = confPadraoBib.getQuantidadeDiaBloqueioEspecificao(emprestimoVO.getTipoPessoa());
			}
			if(motivo.length() > 0 && maiorData > 0){
				BloqueioBibliotecaVO bloqueioBibliotecaVO = new BloqueioBibliotecaVO();
				bloqueioBibliotecaVO.setTipoPessoa(emprestimoVO.getTipoPessoa());
				bloqueioBibliotecaVO.setPessoa(emprestimoVO.getPessoa());
				bloqueioBibliotecaVO.setMatricula(emprestimoVO.getMatricula());
				bloqueioBibliotecaVO.setBiblioteca(emprestimoVO.getBiblioteca());
				bloqueioBibliotecaVO.setAtendente(usuarioVO);
				bloqueioBibliotecaVO.setData(new Date());
				bloqueioBibliotecaVO.setMotivoBloqueio(motivo.toString());
				if (confPadraoBib.getUtilizarApenasDiasUteisEmprestimo()) {
					CidadeVO obj = getFacadeFactory().getCidadeFacade().consultarDadosComboBoxPorBiblioteca(emprestimoVO.getBiblioteca().getCodigo(), usuarioVO);	
					if(obj == null){
						obj = new CidadeVO();
					}
					bloqueioBibliotecaVO.setDataLimiteBloqueio(Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo, getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisProgredir(dataParaCalculo, maiorData, obj.getCodigo(), true, ConsiderarFeriadoEnum.BIBLIOTECA)));
				} else {
					bloqueioBibliotecaVO.setDataLimiteBloqueio(Uteis.getDataFuturaConsiderandoDataAtual(dataParaCalculo,maiorData));
				}
				getFacadeFactory().getBloqueioBibliotecaFacade().persistir(bloqueioBibliotecaVO, usuarioVO);	
			}
	}
}

@Override
public boolean consultaExistenciaVinculoPessoComUnidadeEnsinoBiblioteca(PessoaVO pessoa, String tipoPessoa, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception{
	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);		
	StringBuilder sqlStr = new StringBuilder(" ");
	if(tipoPessoa.equals(TipoPessoa.ALUNO.getValor())){
		sqlStr.append(" select Pessoa.codigo from matricula ");
		sqlStr.append(" INNER JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		sqlStr.append(" INNER JOIN UnidadeEnsino ON Matricula.unidadeEnsino = unidadeEnsino.codigo  ");
		sqlStr.append(" WHERE 1=1 ");
		sqlStr.append(" and pessoa.aluno = true ");
	}else if(tipoPessoa.equals(TipoPessoa.FUNCIONARIO.getValor()) || tipoPessoa.equals(TipoPessoa.PROFESSOR.getValor())){
		sqlStr.append(" select pessoa.codigo from funcionario ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa  ");
		sqlStr.append(" INNER JOIN funcionarioCargo on funcionario.codigo = funcionarioCargo.funcionario  ");
		sqlStr.append(" INNER JOIN unidadeensino on unidadeensino.codigo = funcionarioCargo.unidadeensino  ");
		sqlStr.append(" WHERE 1=1 ");
		if (tipoPessoa.equals(TipoPessoa.FUNCIONARIO.getValor())) {
			sqlStr.append(" and pessoa.funcionario = true ");
		}
		if (tipoPessoa.equals(TipoPessoa.PROFESSOR.getValor())) {
			sqlStr.append(" and pessoa.professor = true ");
		}
	}else if(tipoPessoa.equals(TipoPessoa.MEMBRO_COMUNIDADE.getValor())){
		return true;
	}
	if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
		boolean virgula = false;
		sqlStr.append(" AND unidadeEnsino.codigo in( ");
		for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
			if (!virgula) {
				sqlStr.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
			} else {
				sqlStr.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
			}
			virgula = true;
		}
		sqlStr.append(" ) ");
	}
	sqlStr.append(" and pessoa.codigo = ").append(pessoa.getCodigo());
	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	int codigo = 0;
	if(tabelaResultado.next()) {
		codigo = tabelaResultado.getInt("codigo");
	}
	return codigo > 0 ? true : false;
}


@Override
public Boolean consultarPessoaPossuiExemplarEmprestadoPorCatalogoBiblioteca(CatalogoVO catalogoVO, BibliotecaVO bibliotecaVO, PessoaVO pessoaVO) throws Exception{
	StringBuilder sql = new StringBuilder("");
	sql.append("select itememprestimo.codigo from itememprestimo ");
	sql.append("inner join emprestimo on emprestimo.codigo = itememprestimo.emprestimo ");
	sql.append("inner join exemplar on exemplar.codigo = itememprestimo.exemplar ");
	sql.append("where exemplar.catalogo = ? and exemplar.biblioteca = ? ");
	sql.append("and emprestimo.pessoa = ? and itememprestimo.situacao = 'EX' ");
	return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), catalogoVO.getCodigo(), bibliotecaVO.getCodigo(), pessoaVO.getCodigo()).next();
	
}

class EnviarComunicadoBibliotecaEmprestimo implements Runnable{
	private List<ItemEmprestimoVO> livros;
	private PessoaVO pessoa;
	private String valorTipoPessoa;
	private String biblioteca;
	private Integer unidadeEnsino;
	private UsuarioVO usuario;

	public EnviarComunicadoBibliotecaEmprestimo(List<ItemEmprestimoVO> livros, PessoaVO pessoa, String valorTipoPessoa, String biblioteca, Integer unidadeEnsino, UsuarioVO usuario) {
		super();
		this.livros = livros;
		this.pessoa = pessoa;
		this.valorTipoPessoa = valorTipoPessoa;
		this.biblioteca = biblioteca;
		this.usuario = usuario;
		this.unidadeEnsino = unidadeEnsino;
	}

	@Override
	public void run() {
		try {
			List<ItemEmprestimoVO> listaItensEmprestimoNovo = new ArrayList<ItemEmprestimoVO>();
			List<ItemEmprestimoVO> listaItensEmprestimoDevolucao = new ArrayList<ItemEmprestimoVO>();
			List<ItemEmprestimoVO> listaItensEmprestimoRenovacao = new ArrayList<ItemEmprestimoVO>();
			for (ItemEmprestimoVO ie : livros) {
				if (ie.getDevolverEmprestimo()){
					listaItensEmprestimoDevolucao.add(ie);
				}
				else if(ie.getRenovarEmprestimo()) {
					listaItensEmprestimoRenovacao.add(ie);
				} else if (ie.getEmprestar()) {
					listaItensEmprestimoNovo.add(ie);
				}
			}
			if(!listaItensEmprestimoDevolucao.isEmpty()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEmprestimoDevolucao(listaItensEmprestimoDevolucao, pessoa, valorTipoPessoa, biblioteca, unidadeEnsino, usuario);
			}
			if(!listaItensEmprestimoRenovacao.isEmpty()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executaEnvioMensagemRenovacaoEmprestimo(listaItensEmprestimoRenovacao, pessoa, valorTipoPessoa, biblioteca, unidadeEnsino, usuario);
			}
			if(!listaItensEmprestimoNovo.isEmpty()) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemEmprestimoRealizado(listaItensEmprestimoNovo, pessoa, valorTipoPessoa, biblioteca, unidadeEnsino, usuario);
			}
			
		} catch (Exception e) {				
			e.printStackTrace();
		}
	}
	
	
}
}
