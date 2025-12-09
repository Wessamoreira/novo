package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.HorarioTurmaDiaItemVO;
import negocio.comuns.academico.ItemTitulacaoCursoVO;
import negocio.comuns.academico.TitulacaoCursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.NivelFormacaoAcademica;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;
import relatorio.negocio.comuns.academico.HorarioDaTurmaPrincipalRelVO;
import relatorio.negocio.comuns.academico.HorarioDaTurmaRelVO;
import relatorio.negocio.interfaces.academico.HorarioDaTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class HorarioDaTurmaRel extends SuperRelatorio implements HorarioDaTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public List<HorarioDaTurmaPrincipalRelVO> consultarHorarioDaTurma(Integer codigoTurma, boolean apresentarProfessor, boolean apresentarSala, String ano, String semestre, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT turma.identificadorTurma as turma, Disciplina.nome as disciplina, HorarioTurma.observacao, professor.codigo as profCodigo, ");
		sb.append("  professor.nome as professor, professor.sexo as profSexo, ");
		sb.append("  min(HorarioTurmaDia.data) AS datainicio, ");
		sb.append("  max(HorarioTurmaDia.data) AS datafim , ");
		sb.append("  case when curso.codigo is not null then curso.codigo else (");
		sb.append("  select distinct curso.codigo from curso where ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada = false ");
		sb.append("  and curso.codigo in ( select distinct t.curso from turma t where t.codigo = turma.turmaprincipal)) or (turma.turmaAgrupada and curso.codigo in ( ");
		sb.append("  select distinct t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo))) limit 1");
		sb.append("  ) end as curso ");
		if (apresentarSala) {
			sb.append(",  sala.sala as sala, ");
			sb.append("  localaula.local as local ");
		}
		sb.append("  FROM HorarioTurma");
		sb.append("  INNER JOIN HorarioTurmaDia ON HorarioTurmaDia.horarioTurma = HorarioTurma.codigo ");
		sb.append("  INNER JOIN HorarioTurmaDiaItem ON HorarioTurmaDia.codigo = HorarioTurmaDiaItem.HorarioTurmaDia");
		sb.append("  INNER JOIN Turma ON HorarioTurma.turma = Turma.codigo ");
		sb.append("  INNER JOIN Disciplina  ON HorarioTurmaDiaItem.disciplina = disciplina.codigo ");
		sb.append("  INNER JOIN Pessoa professor ON  HorarioTurmaDiaItem.professor = professor.codigo ");
		sb.append("  LEFT JOIN Curso ON curso.codigo = Turma.curso ");
		if (apresentarSala) {
			sb.append("  left join salalocalaula as sala on sala.codigo = horarioturmadiaitem.sala ");
			sb.append("  left join localaula on localaula.codigo = sala.localaula ");
		}
		sb.append("  WHERE Turma.codigo = ? ");
		if (Uteis.isAtributoPreenchido(ano)) {
			sb.append("  and HorarioTurma.anovigente = '").append(ano).append("' ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sb.append("  and HorarioTurma.semestrevigente = '").append(semestre).append("' ");
		}
		sb.append("  GROUP BY turma.codigo, Disciplina.nome, HorarioTurma.observacao, professor.codigo, ");
		sb.append("  professor.nome, professor.sexo, curso.codigo");
		if (apresentarSala) {
			sb.append(",  sala.sala, localaula.local ");
		}
		sb.append("  ORDER BY datainicio, disciplina ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] { codigoTurma });
		List<HorarioDaTurmaPrincipalRelVO> listaHorarioDaTurmaPrincipalRelVO = montarDadosConsulta(tabelaResultado, apresentarProfessor, apresentarSala, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		if (!listaHorarioDaTurmaPrincipalRelVO.isEmpty() && apresentarProfessor) {
			HorarioDaTurmaPrincipalRelVO obj = listaHorarioDaTurmaPrincipalRelVO.get(0);
			obj.setTitulacaoCursoVO(getFacadeFactory().getTitulacaoCursoFacade().consultarPorCodigoCursoUnico(obj.getCurso(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
			if (!obj.getTitulacaoCursoVO().getCodigo().equals(0)) {
				preencherQtdeNivelEscolaridade(obj);
				preencherListaQtdeTitulacao(obj);
				// getFacadeFactory().getItemTitulacaoCursoFacade().calcularQtdeProfessorNivelEscolaridade(obj.getHashMapQtdeNivelEscolaridade(),
				// obj.getListaHorarioDaTurmaRelVO().size(),
				// obj.getTitulacaoCursoVO());
				calcularQtdeProfessorNivelEscolaridade(obj.getHashMapQtdeNivelEscolaridade(), obj.getListaHorarioDaTurmaRelVO().size(), obj.getTitulacaoCursoVO());
			}
		}
		return listaHorarioDaTurmaPrincipalRelVO;
	}

	public void preencherQtdeNivelEscolaridade(HorarioDaTurmaPrincipalRelVO obj) throws Exception {
		Integer qtde = 0;
		for (HorarioDaTurmaRelVO horarioDaTurmaRelVO : obj.getListaHorarioDaTurmaRelVO()) {
			Integer maiorNivel = horarioDaTurmaRelVO.getProfessor().consultarMaiorNivelDaEscolaridade();
			if (obj.getHashMapQtdeNivelEscolaridade().containsKey(maiorNivel)) {
				qtde = obj.getHashMapQtdeNivelEscolaridade().get(maiorNivel);
				qtde++;
				obj.getHashMapQtdeNivelEscolaridade().put(maiorNivel, qtde);
			} else {
				obj.getHashMapQtdeNivelEscolaridade().put(maiorNivel, 1);
			}
		}
	}

	public void preencherListaQtdeTitulacao(HorarioDaTurmaPrincipalRelVO obj) throws Exception {
		ItemTitulacaoCursoVO itemTitulacaoCursoVO = null;
		if (obj.getHashMapQtdeNivelEscolaridade().size() > 0) {
			for (int nivel = 8; nivel > 3; nivel--) {
				if (obj.getHashMapQtdeNivelEscolaridade().containsKey(nivel)) {
					itemTitulacaoCursoVO = new ItemTitulacaoCursoVO();
					itemTitulacaoCursoVO.setTitulacao(NivelFormacaoAcademica.getDescricaoPorNivel(nivel));
					itemTitulacaoCursoVO.setQuantidade(obj.getHashMapQtdeNivelEscolaridade().get(nivel));
					obj.getListaQtdeTitulacao().add(itemTitulacaoCursoVO);
				}
			}
		}
	}

	public void calcularQtdeProfessorNivelEscolaridade(HashMap<Integer, Integer> hashMapQtdeNivelEscolaridade, Integer qtdeProfessores, TitulacaoCursoVO titulacaoCursoVO) throws Exception {
		if (qtdeProfessores > 0) {
			titulacaoCursoVO.obterNivelItemTitulacaoCurso();
			Ordenacao.ordenarLista(titulacaoCursoVO.getItemTitulacaoCursoVOs(), "nivelTitulacao");
			ItemTitulacaoCursoVO obj;
			Integer qtdeProfessoresUsados = qtdeProfessores;
			Double valorQtdeProfessores = 0.0;
			int qtdeProfessoresTitulacao = 0;
			for (int i = titulacaoCursoVO.getItemTitulacaoCursoVOs().size() - 1; i >= 0; i--) {
				obj = titulacaoCursoVO.getItemTitulacaoCursoVOs().get(i);
				qtdeProfessoresTitulacao = 0;
				for (int nivel = 8; nivel >= 3; nivel--) {
					if (hashMapQtdeNivelEscolaridade.containsKey(nivel)) {
						if (nivel >= obj.getNivelTitulacao() && hashMapQtdeNivelEscolaridade.get(nivel) > 0) {
							qtdeProfessoresTitulacao += hashMapQtdeNivelEscolaridade.get(nivel);
							hashMapQtdeNivelEscolaridade.put(nivel, 0);
						} else if (obj.getNivelSegundaTitulacao() > 0 && nivel >= obj.getNivelSegundaTitulacao() && hashMapQtdeNivelEscolaridade.get(nivel) > 0) {
							qtdeProfessoresTitulacao += hashMapQtdeNivelEscolaridade.get(nivel);
							hashMapQtdeNivelEscolaridade.put(nivel, 0);
						} else {
							break;
						}
					}
				}
				if (qtdeProfessoresTitulacao > 0) {
					qtdeProfessoresUsados = qtdeProfessoresUsados - qtdeProfessoresTitulacao;
					valorQtdeProfessores = (qtdeProfessoresTitulacao * 100) / (qtdeProfessores * 1.0);
					if (Uteis.getParteDecimalDoubleDuasCasas(valorQtdeProfessores) >= 50) {
						obj.setPorcentagemTitulacao((Uteis.getParteInteiraDouble(valorQtdeProfessores)) + 1);
					} else {
						obj.setPorcentagemTitulacao(Uteis.getParteInteiraDouble(valorQtdeProfessores));
					}
				}
			}
			// if (qtdeProfessoresUsados > 0) {
			// String valorFormacaoAcademica = null;
			// for (int nivel = 8; nivel >= 3; nivel--) {
			// if (hashMapQtdeNivelEscolaridade.containsKey(nivel) &&
			// hashMapQtdeNivelEscolaridade.get(nivel) > 0) {
			// valorFormacaoAcademica =
			// NivelFormacaoAcademica.getValorPorNivel(nivel);
			// if (!valorFormacaoAcademica.equals("")) {
			// valorQtdeProfessores = 0.0;
			// ItemTitulacaoCursoVO itemTitulacaoCursoVO = new
			// ItemTitulacaoCursoVO();
			// itemTitulacaoCursoVO.setTitulacao(valorFormacaoAcademica);
			// qtdeProfessoresTitulacao =
			// hashMapQtdeNivelEscolaridade.get(nivel);
			// qtdeProfessoresUsados = qtdeProfessoresUsados -
			// qtdeProfessoresTitulacao;
			// valorQtdeProfessores = (qtdeProfessoresTitulacao * 100) /
			// (qtdeProfessores * 1.0);
			// if (Uteis.getParteDecimalDoubleDuasCasas(valorQtdeProfessores) >=
			// 50) {
			// itemTitulacaoCursoVO.setPorcentagemTitulacao((Uteis.getParteInteiraDouble(valorQtdeProfessores))
			// + 1);
			// } else {
			// itemTitulacaoCursoVO.setPorcentagemTitulacao(Uteis.getParteInteiraDouble(valorQtdeProfessores));
			// }
			// hashMapQtdeNivelEscolaridade.put(nivel, 0);
			// titulacaoCursoVO.getItemTitulacaoCursoVOs().add(itemTitulacaoCursoVO);
			// if (qtdeProfessoresUsados <= 0) {
			// break;
			// }
			// }
			// }
			// }
			// }
		}
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>HorarioTurmaDiaVO</code> resultantes da consulta.
	 */
	public static List<HorarioDaTurmaPrincipalRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, boolean apresentarProfessor, boolean apresentarSala, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<HorarioDaTurmaPrincipalRelVO> vetResultado = new ArrayList<HorarioDaTurmaPrincipalRelVO>(0);
		if (tabelaResultado.next()) {
			HorarioDaTurmaPrincipalRelVO obj = new HorarioDaTurmaPrincipalRelVO();
			obj = montarDados(tabelaResultado, apresentarProfessor, apresentarSala, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>HorarioTurmaDiaVO</code>.
	 * 
	 * @return O objeto da classe <code>HorarioTurmaDiaVO</code> com os dados devidamente montados.
	 */
	@SuppressWarnings("unchecked")
	public static HorarioDaTurmaPrincipalRelVO montarDados(SqlRowSet tabelaResultado, boolean apresentarProfessor, boolean apresentarSala, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		HorarioDaTurmaPrincipalRelVO horarioDaTurmaPrincipalRelVO = new HorarioDaTurmaPrincipalRelVO();
		horarioDaTurmaPrincipalRelVO.setTurma(tabelaResultado.getString("turma"));
		horarioDaTurmaPrincipalRelVO.setCurso(tabelaResultado.getInt("curso"));
		horarioDaTurmaPrincipalRelVO.setObservacao(tabelaResultado.getString("observacao"));
		do {
			HorarioDaTurmaRelVO obj = new HorarioDaTurmaRelVO();
			obj.setDisciplina(tabelaResultado.getString("disciplina"));
			if (apresentarProfessor) {
				obj.getProfessor().setCodigo(tabelaResultado.getInt("profCodigo"));
				obj.getProfessor().setNome(tabelaResultado.getString("professor"));
				obj.getProfessor().setSexo(tabelaResultado.getString("profSexo"));
				obj.getProfessor().setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(obj.getProfessor().getCodigo(), false, true, usuario));
				if (!obj.getProfessor().getFormacaoAcademicaVOs().isEmpty()) {
					obj.setTitulacaoProfessor(obj.getProfessor().consultarMaiorNivelEscolaridade());
				}
			}
			if (apresentarSala) {
				obj.setSala(tabelaResultado.getString("local") + " - " + tabelaResultado.getString("sala"));
			}
			obj.setDataInicio(Uteis.getData(tabelaResultado.getDate("dataInicio")));
			obj.setDataFim(Uteis.getData(tabelaResultado.getDate("dataFim")));
			horarioDaTurmaPrincipalRelVO.getListaHorarioDaTurmaRelVO().add(obj);
		} while (tabelaResultado.next());
		return horarioDaTurmaPrincipalRelVO;
	}

	@Override
	public void validarDados(TurmaVO turmaVO, String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario, String tipoLayout, Date dataBaseHorarioAula) throws Exception {
		if ((unidadeEnsinoVO == null || unidadeEnsinoVO.getCodigo() == null || unidadeEnsinoVO.getCodigo() == 0) && !usuario.getVisaoLogar().equals("coordenador")) {
			throw new Exception(UteisJSF.internacionalizar("msg_HorarioDaTurmaRel_unidadeEnsino"));
		}
		if (turmaVO == null || turmaVO.getCodigo() == null || turmaVO.getCodigo() == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_HorarioDaTurmaRel_turma"));
		}
		if ((turmaVO.getSemestral() || turmaVO.getAnual()) && (!Uteis.isAtributoPreenchido(ano))) {
			throw new Exception(UteisJSF.internacionalizar("msg_HorarioDaTurmaRel_ano"));
		}
		if ((turmaVO.getSemestral() || turmaVO.getAnual()) && (ano.trim().length() != 4)) {
			throw new Exception(UteisJSF.internacionalizar("msg_HorarioDaTurmaRel_anoInvalido"));
		}
		if ((turmaVO.getSemestral()) && semestre.trim().isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_HorarioDaTurmaRel_semestre"));
		}
		if (tipoLayout.equals("HorarioDaTurmaSemanalRel") && !Uteis.isAtributoPreenchido(dataBaseHorarioAula)) {
			throw new Exception(UteisJSF.internacionalizar("msg_HorarioTurma_periodoNaoInformado"));
		}
	}

	@Override
	public String caminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	@Override
	public String designIReportRelatorio(String tipoLayout) {
		if (tipoLayout.equals("HorarioDaTurmaRel")) {
			return caminhoBaseRelatorio() + "HorarioDaTurmaRel.jrxml";
		} else if (tipoLayout.equals("HorarioDaTurma2Rel")) {
			return caminhoBaseRelatorio() + "HorarioDaTurma2Rel.jrxml";
		}
		return caminhoBaseRelatorio() + "HorarioDaTurmaSemanalRel.jrxml";
	}

	public List<HorarioDaTurmaPrincipalRelVO> consultarHorarioDaTurmaLayout2(TurmaVO turmaVO, boolean apresentarProfessor, boolean apresentarSala, String ano, String semestre, UsuarioVO usuario, Integer unidadeEnsino) throws Exception {
		List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs = getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarParametrizada(turmaVO.getCodigo(), null, null, null, null, ano, semestre, null, null);
		List<HorarioDaTurmaPrincipalRelVO> resultado = new ArrayList<HorarioDaTurmaPrincipalRelVO>(0);
		if (!Uteis.isAtributoPreenchido(horarioTurmaDiaItemVOs)) {
			return resultado;
		}
		HorarioDaTurmaPrincipalRelVO horarioDaTurmaPrincipalRelVO = new HorarioDaTurmaPrincipalRelVO();
		horarioDaTurmaPrincipalRelVO.setTurma(turmaVO.getIdentificadorTurma());
		horarioDaTurmaPrincipalRelVO.setObservacao(turmaVO.getObservacao());
		HorarioDaTurmaRelVO obj = null;
		for (HorarioTurmaDiaItemVO item : horarioTurmaDiaItemVOs) {
			obj = new HorarioDaTurmaRelVO();
			obj.setHorario(Uteis.getData(item.getData()) + " - " + item.getNrAula() + "ª (" + item.getHorarioInicio() + " - " + item.getHorarioTermino() + ")");
			obj.setDisciplina(item.getDisciplinaVO().getNome());
			if (apresentarProfessor) {
				obj.getProfessor().setNome(item.getFuncionarioVO().getNome());
			}
			if (apresentarSala && !item.getSala().getSala().trim().isEmpty()) {
				obj.setSala(item.getSala().getLocalSala());
			}
			obj.setDiaSemana(Uteis.getDiaSemana(Uteis.getDiaSemana(item.getData())).getDescricao());
			obj.setDataInicio(Uteis.getData(item.getData(), "dd/MM/yyyy"));
			horarioDaTurmaPrincipalRelVO.getListaHorarioDaTurmaRelVO().add(obj);
		}
		resultado.add(horarioDaTurmaPrincipalRelVO);
		return resultado;
	}

	@Override
	public List<HorarioDaTurmaPrincipalRelVO> criarObjeto(TurmaVO turmaVO, boolean apresentarProfessor, boolean apresentarSala, String ano, String semestre, UsuarioVO usuario, Integer unidadeEnsino, String tipoLayout, Date dataInicio, Date dataTermino) throws Exception {
		if (tipoLayout.equals("HorarioDaTurmaRel")) {
			return consultarHorarioDaTurma(turmaVO.getCodigo(), apresentarProfessor, apresentarSala, ano, semestre, usuario);
		} else if (tipoLayout.equals("HorarioDaTurma2Rel")) {
			return consultarHorarioDaTurmaLayout2(turmaVO, apresentarProfessor, apresentarSala, ano, semestre, usuario, unidadeEnsino);
		}
		return executarGeracaoHorarioDaTurmaLayoutSemanal(turmaVO, ano, semestre, usuario, dataInicio, dataTermino);
	}

	private List<HorarioDaTurmaPrincipalRelVO> executarGeracaoHorarioDaTurmaLayoutSemanal(TurmaVO turmaVO, String ano, String semestre, UsuarioVO usuario, Date dataInicio, Date dataTermino) throws Exception {
		SqlRowSet rs = getFacadeFactory().getHorarioTurmaDiaItemFacade().consultarProgramacaoAulaHorarioTurmaRel(turmaVO.getCodigo(), ano, semestre, dataInicio, dataTermino);
		List<HorarioDaTurmaPrincipalRelVO> resultado = new ArrayList<HorarioDaTurmaPrincipalRelVO>(0);
		HorarioDaTurmaPrincipalRelVO horarioDaTurmaPrincipalRelVO = new HorarioDaTurmaPrincipalRelVO();
		horarioDaTurmaPrincipalRelVO.setTurma(turmaVO.getIdentificadorTurma());
		Map<Integer, String> diaSemanaData = new HashMap<Integer, String>(0);
		while (rs.next()) {
			HorarioDaTurmaRelVO obj = new HorarioDaTurmaRelVO();
			obj.setHorario(rs.getString("horarioInicio") + " - " + rs.getString("horarioTermino"));
			obj.setDisciplina(rs.getString("disciplina"));
			obj.getProfessor().setNome(rs.getString("professor"));
			String local = "";
			String sala = "";
			if (rs.getString("local") != null) {
				local = rs.getString("local");
			}
			if (rs.getString("sala") != null) {
				sala = rs.getString("sala");
			}
			obj.setSala(local + " - " + sala);
			obj.setOrdemApresentacaoSemanal(rs.getInt("diasemana"));
			obj.setDiaSemana(Uteis.getDiaSemana(rs.getInt("diasemana")).getDescricao());
			if (Uteis.isAtributoPreenchido(rs.getDate("data"))) {
				obj.setDataInicio(Uteis.getData(rs.getDate("data"), "dd/MM/yyyy"));
				if (!diaSemanaData.containsKey(obj.getOrdemApresentacaoSemanal())) {
					diaSemanaData.put(obj.getOrdemApresentacaoSemanal(), obj.getDataInicio());
				}
			}
			horarioDaTurmaPrincipalRelVO.getListaHorarioDaTurmaRelVO().add(obj);
		}
		if (!Uteis.isAtributoPreenchido(horarioDaTurmaPrincipalRelVO.getListaHorarioDaTurmaRelVO())) {
			return resultado;
		}
		for (HorarioDaTurmaRelVO obj : horarioDaTurmaPrincipalRelVO.getListaHorarioDaTurmaRelVO()) {
			if (!Uteis.isAtributoPreenchido(obj.getDataInicio()) && diaSemanaData.get(obj.getOrdemApresentacaoSemanal()) != null) {
				obj.setDataInicio(diaSemanaData.get(obj.getOrdemApresentacaoSemanal()));
			}
		}
		resultado.add(horarioDaTurmaPrincipalRelVO);
		return resultado;
	}

}
