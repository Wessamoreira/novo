package negocio.facade.jdbc.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.LinksUteisVO;
import negocio.comuns.basico.PaizVO;
import negocio.comuns.basico.UsuarioLinksUteisVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.LinksUteisInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>PaizVO</code>. Responsável por implementar operações
 * como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>PaizVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PaizVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class LinksUteis extends ControleAcesso implements LinksUteisInterfaceFacade {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static String idEntidade;

	public LinksUteis() throws Exception {
		super();
		setIdEntidade("LinksUteis");
	}
	
	
	/**
	 * Método para processar um arquivo excel, verificar se o mesmo está preenchido
	 * Logo em seguida, consultar os valores caso estiver preenchido
	 * apos consultado coloca o resultado da consulta em um List<>
	 */
	@Override
	public void realizarProcessamentoExcelPlanilha(FileUploadEvent uploadEvent, XSSFWorkbook xssfWorkbook, HSSFWorkbook hssfWorkbook, boolean importarPorCPF, boolean importarPorEmailInstitucional, LinksUteisVO linksUteisVO, ProgressBarVO progressBarVO, UsuarioVO usuario) throws Exception {
		String extensao = uploadEvent.getUploadedFile().getName().substring(uploadEvent.getUploadedFile().getName().lastIndexOf(".") + 1);
		linksUteisVO.getListaMensagemErroProcessamento().clear();
		int rowMax = 0;
		XSSFSheet mySheetXlsx = null;
		HSSFSheet mySheetXls = null;
		if (extensao.equals("xlsx")) {
			mySheetXlsx = xssfWorkbook.getSheetAt(0);
			rowMax = mySheetXlsx.getLastRowNum();

		} else {
			mySheetXls = hssfWorkbook.getSheetAt(0);
			rowMax = mySheetXls.getLastRowNum();
		}
		progressBarVO.setMaxValue(rowMax+1);
		int qtdeLinhaEmBranco = 0;
		int linha = 0;
		Row row = null;
		while (linha <= rowMax) {
			progressBarVO.setStatus("Carregando informações da linha "+linha+" de "+rowMax);
			if (extensao.equals("xlsx")) {
				row = mySheetXlsx.getRow(linha);
			} else {
				row = mySheetXls.getRow(linha);
			}
			if (linha == 0) {
				linha++;
				continue;
			}
			if (qtdeLinhaEmBranco == 2) {
				break;
			}
			if (getValorCelula(0, row, true) == null || getValorCelula(0, row, true).toString().equals("")) {
				qtdeLinhaEmBranco++;
				continue;
			}
			if (progressBarVO.getForcarEncerramento()) {
				break;
			}
			String paramentroPesquisa = getValorCelula(0, row, true) != null ? String.valueOf(getValorCelula(0, row, true)) : "";
			UsuarioVO usuarioVO = null;
			if (importarPorCPF) {
				// cpfString
				usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorCPF(paramentroPesquisa, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			} else {
				usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorEmailInstitucional(paramentroPesquisa, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			}
			if (Uteis.isAtributoPreenchido(usuarioVO)) {
				adicionarUsuarioLinkUteisVO(linksUteisVO, usuarioVO);
			} else {
				if (importarPorCPF) {
					linksUteisVO.getListaMensagemErroProcessamento().add("Não foi possível encontrar o usuário do cpf - " + paramentroPesquisa);
				} else {
					linksUteisVO.getListaMensagemErroProcessamento().add("Não foi possível encontrar o usuário do email - " + paramentroPesquisa);
				}

			}
			linha++;
		}

	}

	public void validarDadosPlanilha(UsuarioLinksUteisVO usuarioLinksUteisVO) {

	}

	public static LinksUteisVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		LinksUteisVO obj = new LinksUteisVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setAluno(dadosSQL.getBoolean("aluno"));
		obj.setLink(dadosSQL.getString("link"));
		obj.setIcone(dadosSQL.getString("icone"));
		obj.setAluno(dadosSQL.getBoolean("aluno"));
		obj.setProfessor(dadosSQL.getBoolean("professor"));
		obj.setCoordenador(dadosSQL.getBoolean("coordenador"));
		obj.setAdministrativo(dadosSQL.getBoolean("administrativo"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return obj;
		}
		obj.setUsuarioLinksUteisVOs(getFacadeFactory().getUsuarioLinksUteisFacade().consultarPorUsuarioLinksUteis(obj.getCodigo(), usuario));
		return obj;
	}

	@Override
	public List<LinksUteisVO> consultarPorDescricao(String valorConsulta, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controleAcesso, usuarioVO);
		String sqlStr = "SELECT * FROM LinksUteis  WHERE lower (sem_acentos(descricao)) ilike(sem_acentos(?)) ORDER BY descricao desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));

	}

	public Cell getValorCelula(int numeroCelula, Row row, Boolean isString) {
		Cell cell = row.getCell(numeroCelula);
		if (cell != null && isString) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
		}
		return cell;
	}

	public List<LinksUteisVO> consultarPorLink(String valorConsulta, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controleAcesso, usuarioVO);
		String sqlStr = "SELECT * FROM linksuteis  WHERE lower (sem_acentos(link))ilike(sem_acentos(?)) ORDER BY link desc";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toLowerCase() + "%");
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));

	}

	public static List<LinksUteisVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<LinksUteisVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return LinksUteis.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		LinksUteis.idEntidade = idEntidade;
	}

	@Override
	public LinksUteisVO novo() throws Exception {
		LinksUteis.incluir(getIdEntidade());
		LinksUteisVO obj = new LinksUteisVO();
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(LinksUteisVO obj, boolean controleAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LinksUteis.incluir(getIdEntidade(), controleAcesso, usuarioVO);
			if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
				throw new Exception("O campo Descricao dever ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getLink())) {
				throw new Exception("O campo Link dever ser informado.");
			}
			incluir(obj, "LinksUteis", new AtributoPersistencia()
					.add("descricao", obj.getDescricao())
					.add("link", obj.getLink())
					.add("icone", obj.getIcone())
					.add("aluno", obj.getAluno())
					.add("professor", obj.getProfessor())
					.add("coordenador", obj.getCoordenador())
					.add("administrativo", obj.getAdministrativo()), usuarioVO);
			getFacadeFactory().getUsuarioLinksUteisFacade().persistir(obj.getUsuarioLinksUteisVOs(), obj, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(LinksUteisVO obj, boolean controleAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LinksUteis.alterar(getIdEntidade(), controleAcesso, usuarioVO);
			alterar(obj, "LinksUteis", new AtributoPersistencia()
					.add("descricao", obj.getDescricao())
					.add("link", obj.getLink())
					.add("icone", obj.getIcone())
					.add("aluno", obj.getAluno())
					.add("professor", obj.getProfessor())
					.add("coordenador", obj.getCoordenador())
					.add("administrativo", obj.getAdministrativo()), new AtributoPersistencia()
					.add("codigo", obj.getCodigo()), usuarioVO);
			excluirListaSubordinada(obj.getUsuarioLinksUteisVOs(), "usuarioLinksUteis", new AtributoPersistencia().add("linksUteis", obj.getCodigo()), usuarioVO);
			if (!Uteis.isAtributoPreenchido(obj.getDescricao())) {
				throw new Exception("O campo Descricao dever ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getLink())) {
				throw new Exception("O campo Link dever ser informado.");
			}
			getFacadeFactory().getUsuarioLinksUteisFacade().persistir(obj.getUsuarioLinksUteisVOs(), obj, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(LinksUteisVO obj, boolean controleAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			LinksUteis.excluir(getIdEntidade(), controleAcesso, usuarioVO);
			String sql = "DELETE FROM linksuteis WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void adicionarUsuarioLinkUteisVO(LinksUteisVO linksUteisVO, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(usuarioVO)) {
			throw new Exception("O campo USUÁRIO dever ser informado.");
		}
		if (!linksUteisVO.getUsuarioLinksUteisVOs().stream().anyMatch(u -> u.getUsuarioVO().getCodigo().equals(usuarioVO.getCodigo()))) {
			UsuarioLinksUteisVO usuarioLinksUteisVO = new UsuarioLinksUteisVO();
			usuarioLinksUteisVO.setUsuario(usuarioVO);
			usuarioLinksUteisVO.setLinksUteisVO(linksUteisVO);
			linksUteisVO.getUsuarioLinksUteisVOs().add(usuarioLinksUteisVO);
		}
	}

	@Override
	public void removerUsuarioLinkUteisVO(LinksUteisVO linksUteisVO, UsuarioLinksUteisVO usuarioLinksUteisVO) throws Exception {
		linksUteisVO.getUsuarioLinksUteisVOs().removeIf(u -> u.getUsuarioVO().getCodigo().equals(usuarioLinksUteisVO.getUsuarioVO().getCodigo()));
	}

	
	
	@Override
	public List<LinksUteisVO> consultarLinksUteisUsuario(int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
		String sqlStr = "SELECT * FROM linksuteis WHERE((";
		if (usuarioVO.getIsApresentarVisaoAlunoOuPais()) {
			sqlStr += "linksuteis.aluno";
		} else if (usuarioVO.getIsApresentarVisaoAdministrativa()) {
			sqlStr += "linksuteis.administrativo";
		} else if (usuarioVO.getIsApresentarVisaoProfessor()) {
			sqlStr += "linksuteis.professor";
		} else if (usuarioVO.getIsApresentarVisaoCoordenador()) {
			sqlStr += "linksuteis.coordenador";
		}
		sqlStr = sqlStr + " AND NOT EXISTS (SELECT usuariolinksuteis.codigo FROM usuariolinksuteis WHERE linksuteis.codigo = usuariolinksuteis.linksuteis AND usuariolinksuteis.usuario = ?)) OR EXISTS (SELECT usuariolinksuteis.codigo FROM usuariolinksuteis WHERE linksuteis.codigo = usuariolinksuteis.linksuteis AND usuariolinksuteis.usuario = ?)) ORDER BY descricao ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, usuarioVO.getCodigo(), usuarioVO.getCodigo());
		return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO));

	}
	
}