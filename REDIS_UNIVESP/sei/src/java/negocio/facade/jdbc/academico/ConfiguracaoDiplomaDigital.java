package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.enumeradores.CampoPeriodoDisciplinaEnum;
import negocio.comuns.academico.enumeradores.CoordenadorCursoDisciplinaAproveitadaEnum;
import negocio.comuns.academico.enumeradores.FormatoCargaHorariaXmlEnum;
import negocio.comuns.academico.enumeradores.VersaoDiplomaDigitalEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.ConfiguracaoDiplomaDigitalInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ConfiguracaoDiplomaDigitalVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ConfiguracaoDiplomaDigitalVO</code>. Encapsula toda a
 * interação com o banco de dados.
 * 
 * @see ConfiguracaoDiplomaDigitalVO
 * @see ControleAcesso
 * @author Felipi Alves
 */
@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoDiplomaDigital extends ControleAcesso implements ConfiguracaoDiplomaDigitalInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ConfiguracaoDiplomaDigital() throws Exception {
		super();
		setIdEntidade("ConfiguracaoDiplomaDigital");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ConfiguracaoDiplomaDigital.idEntidade = idEntidade;
	}

	private void verificarConfiguracaoPadraoExistente(ConfiguracaoDiplomaDigitalVO obj) throws Exception {
		if (obj.getPadrao()) {
			String sql = "SELECT codigo FROM configuracaoDiplomaDigital WHERE padrao = TRUE" + (Uteis.isAtributoPreenchido(obj) ? " AND codigo NOT IN (" + obj.getCodigo() + ")" : "");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			if (tabelaResultado.next()) {
				throw new Exception("Já existe uma configuração padrão cadastrada no sistema.");
			}
		}
	}

	@Override
	public void incluir(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		ControleAcesso.incluir(getIdEntidade(), controlarAcesso, usuario);
		obj.validarDados();
		verificarConfiguracaoPadraoExistente(obj);
		incluir(obj, "configuracaoDiplomaDigital", new AtributoPersistencia()
				.add("dataCadastro", 											Uteis.getDataJDBCTimestamp(new Date()))
				.add("descricao", 												obj.getDescricao())
				.add("padrao", 													obj.getPadrao())
				.add("utilizarCoordenadorCursoAtividadeComplementar", 			obj.getUtilizarCoordenadorCursoAtividadeComplementar())
				.add("coordenadorCursoDisciplinasAproveitadas", 				obj.getCoordenadorCursoDisciplinasAproveitadas().getValor())
				.add("apresentarTextoEnade",  									obj.getApresentarTextoEnade())
				.add("unidadeEnsinoPadrao", 									Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoPadrao()) ? obj.getUnidadeEnsinoPadrao().getCodigo() : null)
				.add("layoutGraduacaoPadrao", 									obj.getLayoutGraduacaoPadrao())
				.add("layoutGraduacaoTecnologicaPadrao", 						obj.getLayoutGraduacaoTecnologicaPadrao())
				.add("textoPadraoGraduacaoPadrao", 								Uteis.isAtributoPreenchido(obj.getTextoPadraoGraduacaoPadrao()) ? obj.getTextoPadraoGraduacaoPadrao().getCodigo() : null)
				.add("textoPadraoGraduacaoTecnologicaPadrao", 					Uteis.isAtributoPreenchido(obj.getTextoPadraoGraduacaoTecnologicaPadrao()) ? obj.getTextoPadraoGraduacaoTecnologicaPadrao().getCodigo() : null)
				.add("funcionarioPrimario", 									Uteis.isAtributoPreenchido(obj.getFuncionarioPrimario()) ? obj.getFuncionarioPrimario().getCodigo() : null)
				.add("cargoFuncionarioPrimario", 								Uteis.isAtributoPreenchido(obj.getCargoFuncionarioPrimario()) ? obj.getCargoFuncionarioPrimario().getCodigo() : null)
				.add("tituloFuncionarioPrimario", 								Uteis.isAtributoPreenchido(obj.getFuncionarioPrimario()) ? obj.getTituloFuncionarioPrimario() : Constantes.EMPTY)
				.add("funcionarioSecundario", 									Uteis.isAtributoPreenchido(obj.getFuncionarioSecundario()) ? obj.getFuncionarioSecundario().getCodigo() : null)
				.add("cargoFuncionarioSecundario",								Uteis.isAtributoPreenchido(obj.getCargoFuncionarioSecundario()) ? obj.getCargoFuncionarioSecundario().getCodigo() : null)
				.add("tituloFuncionarioSecundario", 							Uteis.isAtributoPreenchido(obj.getFuncionarioSecundario()) ? obj.getTituloFuncionarioSecundario() : Constantes.EMPTY)
				.add("funcionarioTerceiro", 									Uteis.isAtributoPreenchido(obj.getFuncionarioTerceiro()) ? obj.getFuncionarioTerceiro().getCodigo() : null)
				.add("cargoFuncionarioTerceiro", 								Uteis.isAtributoPreenchido(obj.getCargoFuncionarioTerceiro()) ? obj.getCargoFuncionarioTerceiro().getCodigo() : null)
				.add("tituloFuncionarioTerceiro", 								Uteis.isAtributoPreenchido(obj.getFuncionarioTerceiro()) ? obj.getTituloFuncionarioTerceiro() : Constantes.EMPTY)
				.add("funcionarioQuarto", 										Uteis.isAtributoPreenchido(obj.getFuncionarioQuarto()) ? obj.getFuncionarioQuarto().getCodigo() : null)
				.add("cargoFuncionarioQuarto", 									Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuarto()) ? obj.getCargoFuncionarioQuarto().getCodigo() : null)
				.add("tituloFuncionarioQuarto", 								Uteis.isAtributoPreenchido(obj.getFuncionarioQuarto()) ? obj.getTituloFuncionarioQuarto() : Constantes.EMPTY)
				.add("funcionarioQuinto", 										Uteis.isAtributoPreenchido(obj.getFuncionarioQuinto()) ? obj.getFuncionarioQuinto().getCodigo() : null)
				.add("cargoFuncionarioQuinto",									Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuinto()) ? obj.getCargoFuncionarioQuinto().getCodigo() : null)
				.add("tituloFuncionarioQuinto", 								Uteis.isAtributoPreenchido(obj.getFuncionarioQuinto()) ? obj.getTituloFuncionarioQuinto() : Constantes.EMPTY)
				.add("utilizarCampoPeriodoDisciplina", 							obj.getUtilizarCampoPeriodoDisciplina().getValor())
				.add("horaRelogio", 											obj.getHoraRelogio())
				.add("versao", 													obj.getVersao().getValor())
				.add("historicoConsiderarAprovado", 							obj.getHistoricoConsiderarAprovado())
				.add("historicoConsiderarReprovado", 							obj.getHistoricoConsiderarReprovado())
				.add("historicoConsiderarCursando", 							obj.getHistoricoConsiderarCursando())
				.add("historicoConsiderarEvasao", 								obj.getHistoricoConsiderarEvasao())
				.add("historicoConsiderarForaGrade", 							obj.getHistoricoConsiderarForaGrade())
				.add("considerarCargaHorariaForaGrade", 						obj.getConsiderarCargaHorariaForaGrade())
				.add("formatoCargaHorariaXML", 									obj.getFormatoCargaHorariaXML().getValor())
				.add("tipoLayoutHistoricoGraduacao", 							obj.getTipoLayoutHistoricoGraduacao())
				.add("tipoLayoutHistoricoGraduacaoTecnologica", 				obj.getTipoLayoutHistoricoGraduacaoTecnologica())
				.add("apresentarApenasUltimoHistoricoDisciplina", 				obj.getApresentarApenasUltimoHistoricoDisciplina())
				.add("considerarCargaHorariaCursadaIgualCargaHorariaPrevista",	obj.getConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista())
				.add("validarArquivoComprobatoriaIsPDFA", 						obj.getValidarArquivoComprobatoriaIsPDFA())
				,usuario);
	}

	@Override
	public void alterar(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		ControleAcesso.alterar(getIdEntidade(), controlarAcesso, usuario);
		obj.validarDados();
		verificarConfiguracaoPadraoExistente(obj);
		alterar(obj, "configuracaoDiplomaDigital", 
				new AtributoPersistencia()
				.add("descricao", 												obj.getDescricao())
				.add("padrao", 													obj.getPadrao())
				.add("utilizarCoordenadorCursoAtividadeComplementar", 			obj.getUtilizarCoordenadorCursoAtividadeComplementar())
				.add("coordenadorCursoDisciplinasAproveitadas", 				obj.getCoordenadorCursoDisciplinasAproveitadas().getValor())
				.add("apresentarTextoEnade",  									obj.getApresentarTextoEnade())
				.add("unidadeEnsinoPadrao", 									Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoPadrao()) ? obj.getUnidadeEnsinoPadrao().getCodigo() : null)
				.add("layoutGraduacaoPadrao", 									obj.getLayoutGraduacaoPadrao())
				.add("layoutGraduacaoTecnologicaPadrao", 						obj.getLayoutGraduacaoTecnologicaPadrao())
				.add("textoPadraoGraduacaoPadrao", 								Uteis.isAtributoPreenchido(obj.getTextoPadraoGraduacaoPadrao()) ? obj.getTextoPadraoGraduacaoPadrao().getCodigo() : null)
				.add("textoPadraoGraduacaoTecnologicaPadrao", 					Uteis.isAtributoPreenchido(obj.getTextoPadraoGraduacaoTecnologicaPadrao()) ? obj.getTextoPadraoGraduacaoTecnologicaPadrao().getCodigo() : null)
				.add("funcionarioPrimario", 									Uteis.isAtributoPreenchido(obj.getFuncionarioPrimario()) ? obj.getFuncionarioPrimario().getCodigo() : null)
				.add("cargoFuncionarioPrimario", 								Uteis.isAtributoPreenchido(obj.getCargoFuncionarioPrimario()) ? obj.getCargoFuncionarioPrimario().getCodigo() : null)
				.add("tituloFuncionarioPrimario", 								Uteis.isAtributoPreenchido(obj.getFuncionarioPrimario()) ? obj.getTituloFuncionarioPrimario() : Constantes.EMPTY)
				.add("funcionarioSecundario", 									Uteis.isAtributoPreenchido(obj.getFuncionarioSecundario()) ? obj.getFuncionarioSecundario().getCodigo() : null)
				.add("cargoFuncionarioSecundario",								Uteis.isAtributoPreenchido(obj.getCargoFuncionarioSecundario()) ? obj.getCargoFuncionarioSecundario().getCodigo() : null)
				.add("tituloFuncionarioSecundario", 							Uteis.isAtributoPreenchido(obj.getFuncionarioSecundario()) ? obj.getTituloFuncionarioSecundario() : Constantes.EMPTY)
				.add("funcionarioTerceiro", 									Uteis.isAtributoPreenchido(obj.getFuncionarioTerceiro()) ? obj.getFuncionarioTerceiro().getCodigo() : null)
				.add("cargoFuncionarioTerceiro", 								Uteis.isAtributoPreenchido(obj.getCargoFuncionarioTerceiro()) ? obj.getCargoFuncionarioTerceiro().getCodigo() : null)
				.add("tituloFuncionarioTerceiro", 								Uteis.isAtributoPreenchido(obj.getFuncionarioTerceiro()) ? obj.getTituloFuncionarioTerceiro() : Constantes.EMPTY)
				.add("funcionarioQuarto", 										Uteis.isAtributoPreenchido(obj.getFuncionarioQuarto()) ? obj.getFuncionarioQuarto().getCodigo() : null)
				.add("cargoFuncionarioQuarto", 									Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuarto()) ? obj.getCargoFuncionarioQuarto().getCodigo() : null)
				.add("tituloFuncionarioQuarto", 								Uteis.isAtributoPreenchido(obj.getFuncionarioQuarto()) ? obj.getTituloFuncionarioQuarto() : Constantes.EMPTY)
				.add("funcionarioQuinto", 										Uteis.isAtributoPreenchido(obj.getFuncionarioQuinto()) ? obj.getFuncionarioQuinto().getCodigo() : null)
				.add("cargoFuncionarioQuinto",									Uteis.isAtributoPreenchido(obj.getCargoFuncionarioQuinto()) ? obj.getCargoFuncionarioQuinto().getCodigo() : null)
				.add("tituloFuncionarioQuinto", 								Uteis.isAtributoPreenchido(obj.getFuncionarioQuinto()) ? obj.getTituloFuncionarioQuinto() : Constantes.EMPTY)
				.add("utilizarCampoPeriodoDisciplina", 							obj.getUtilizarCampoPeriodoDisciplina().getValor())
				.add("horaRelogio", 											obj.getHoraRelogio())
				.add("versao", 													obj.getVersao().getValor())
				.add("historicoConsiderarAprovado", 							obj.getHistoricoConsiderarAprovado())
				.add("historicoConsiderarReprovado", 							obj.getHistoricoConsiderarReprovado())
				.add("historicoConsiderarCursando", 							obj.getHistoricoConsiderarCursando())
				.add("historicoConsiderarEvasao", 								obj.getHistoricoConsiderarEvasao())
				.add("historicoConsiderarForaGrade", 							obj.getHistoricoConsiderarForaGrade())
				.add("considerarCargaHorariaForaGrade", 						obj.getConsiderarCargaHorariaForaGrade())
				.add("formatoCargaHorariaXML", 									obj.getFormatoCargaHorariaXML().getValor())
				.add("tipoLayoutHistoricoGraduacao", 							obj.getTipoLayoutHistoricoGraduacao())
				.add("tipoLayoutHistoricoGraduacaoTecnologica", 				obj.getTipoLayoutHistoricoGraduacaoTecnologica())
				.add("apresentarApenasUltimoHistoricoDisciplina", 				obj.getApresentarApenasUltimoHistoricoDisciplina())
				.add("considerarCargaHorariaCursadaIgualCargaHorariaPrevista",	obj.getConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista())
				.add("validarArquivoComprobatoriaIsPDFA", 						obj.getValidarArquivoComprobatoriaIsPDFA())
				,new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
	}

	@Override
	public void excluir(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		ControleAcesso.excluir(getIdEntidade(), controlarAcesso, usuario);
		excluir("configuracaoDiplomaDigital", new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
	}

	@Override
	public ConfiguracaoDiplomaDigitalVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void persistir(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		if (Uteis.isAtributoPreenchido(obj)) {
			alterar(obj, usuario, controlarAcesso);
			getAplicacaoControle().removerConfiguracaoDiploma(obj.getCodigo());
		} else {
			incluir(obj, usuario, controlarAcesso);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT configuracaodiplomadigital.codigo AS \"configuracaodiplomadigital.codigo\", configuracaodiplomadigital.descricao AS \"configuracaodiplomadigital.descricao\", configuracaodiplomadigital.padrao AS \"configuracaodiplomadigital.padrao\", unidadeensinopadrao.codigo AS \"unidadeensinopadrao.codigo\", unidadeensinopadrao.nome AS \"unidadeensinopadrao.nome\", configuracaodiplomadigital.versao AS \"configuracaodiplomadigital.versao\", configuracaodiplomadigital.formatoCargaHorariaXML AS \"configuracaodiplomadigital.formatoCargaHorariaXML\" ");
		sql.append("FROM configuracaodiplomadigital ");
		sql.append("LEFT JOIN unidadeensino unidadeensinopadrao ON unidadeensinopadrao.codigo = configuracaodiplomadigital.unidadeensinopadrao ");
		return sql;
	}

	@Override
	public List<ConfiguracaoDiplomaDigitalVO> consultar(String valorConsulta, String campoConsulta, int nivelMontarDados, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> lista = new ArrayList<>(0);
		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaBasica());
		sql.append("WHERE ");
		if (campoConsulta.equals("descricao")) {
			sql.append("sem_acentos(descricao) ILIKE (?) ");
			lista.add(valorConsulta + PERCENT);
		} else if (campoConsulta.equals("codigo")) {
			if (!Uteis.getIsValorNumerico(valorConsulta)) {
				throw new Exception("E permitido apenas valores numéricos.");
			}
			sql.append("configuracaodiplomadigital.codigo IN (?) ");
			lista.add(Integer.valueOf(valorConsulta));
		} else if (campoConsulta.equals("unidadeEnsino")) {
			sql.append("sem_acentos(unidadeensinopadrao.nome) ILIKE (?) ");
			lista.add(valorConsulta + PERCENT);
		}
		sql.append("ORDER BY configuracaodiplomadigital.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), lista.toArray());
		return (montarDadosConsultaBasica(tabelaResultado, nivelMontarDados));
	}
	
	@Override
	public void carregarDados(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario) throws Exception {
		SqlRowSet tabelaResultado = null;
		tabelaResultado = consultaRapidaCarregarDadosTodos(obj.getCodigo(), usuario);
		montarDadosTodos(obj, usuario, tabelaResultado);
	}
	
	private StringBuilder getSQLPadraoConsultaBasicaCarregarDadosTodos() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT configuracaodiplomadigital.codigo AS \"cdd.codigo\", configuracaodiplomadigital.datacadastro AS \"cdd.datacadastro\", configuracaodiplomadigital.descricao AS \"cdd.descricao\", configuracaodiplomadigital.padrao AS \"cdd.padrao\", ");
		sql.append("unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome AS \"unidadeensino.nome\", configuracaodiplomadigital.layoutgraduacaopadrao AS \"cdd.layoutgraduacaopadrao\", configuracaodiplomadigital.layoutgraduacaotecnologicapadrao AS \"cdd.layoutgraduacaotecnologicapadrao\", ");
		sql.append("textopadraograduacaopadrao.codigo AS \"textopadraograduacaopadrao.codigo\", textopadraograduacaopadrao.descricao AS \"textopadraograduacaopadrao.descricao\", textopadraograduacaotecnologicapadrao.codigo AS \"tpgtp.codigo\", textopadraograduacaotecnologicapadrao.descricao AS \"tpgtp.descricao\", ");
		sql.append("funcionarioprimario.codigo AS \"funcionarioprimario.codigo\", funcionarioprimario.matricula AS \"funcionarioprimario.matricula\", pessoaprimario.codigo AS \"pessoaprimario.codigo\", pessoaprimario.nome AS \"pessoaprimario.nome\", configuracaodiplomadigital.titulofuncionarioprimario AS \"cdd.titulofuncionarioprimario\", cargofuncionarioprimario.codigo AS \"cfp.codigo\", cargofuncionarioprimario.nome AS \"cfp.nome\", ");
		sql.append("funcionariosecundario.codigo AS \"funcionariosecundario.codigo\", funcionariosecundario.matricula AS \"funcionariosecundario.matricula\", pessoasecundario.codigo AS \"pessoasecundario.codigo\", pessoasecundario.nome AS \"pessoasecundario.nome\", configuracaodiplomadigital.titulofuncionariosecundario AS \"cdd.titulofuncionariosecundario\", cargofuncionariosecundario.codigo AS \"cfs.codigo\", cargofuncionariosecundario.nome AS \"cfs.nome\", ");
		sql.append("funcionarioterceiro.codigo AS \"funcionarioterceiro.codigo\", funcionarioterceiro.matricula AS \"funcionarioterceiro.matricula\", pessoaseterceiro.codigo AS \"pessoaseterceiro.codigo\", pessoaseterceiro.nome AS \"pessoaseterceiro.nome\", configuracaodiplomadigital.titulofuncionarioterceiro AS \"cdd.titulofuncionarioterceiro\", cargofuncionarioterceiro.codigo AS \"cft.codigo\", cargofuncionarioterceiro.nome AS \"cft.nome\", ");
		sql.append("funcionarioquarto.codigo AS \"funcionarioquarto.codigo\", funcionarioquarto.matricula AS \"funcionarioquarto.matricula\", pessoaquarto.codigo AS \"pessoaquarto.codigo\", pessoaquarto.nome AS \"pessoaquarto.nome\", configuracaodiplomadigital.titulofuncionarioquarto AS \"cdd.titulofuncionarioquarto\", cargofuncionarioquarto.codigo AS \"cfqua.codigo\", cargofuncionarioquarto.nome AS \"cfqua.nome\", ");
		sql.append("funcionarioquinto.codigo AS \"funcionarioquinto.codigo\", funcionarioquinto.matricula AS \"funcionarioquinto.matricula\", pessoaquinto.codigo AS \"pessoaquinto.codigo\", pessoaquinto.nome AS \"pessoaquinto.nome\", configuracaodiplomadigital.titulofuncionarioquinto AS \"cdd.titulofuncionarioquinto\", cargofuncionarioquinto.codigo AS \"cfqui.codigo\", cargofuncionarioquinto.nome AS \"cfqui.nome\", ");
		sql.append("configuracaodiplomadigital.utilizarcampoperiododisciplina AS \"cdd.utilizarcampoperiododisciplina\", configuracaodiplomadigital.coordenadorCursoDisciplinasAproveitadas AS \"cdd.coordenadorcursodisciplinasaproveitadas\", configuracaodiplomadigital.utilizarcoordenadorcursoatividadecomplementar AS \"cdd.utilizarcoordenadorcursoatividadecomplementar\", configuracaodiplomadigital.horaRelogio AS \"cdd.horaRelogio\", configuracaodiplomadigital.apresentarTextoEnade AS \"cdd.apresentarTextoEnade\", configuracaodiplomadigital.versao AS \"cdd.versao\", ");
		sql.append("configuracaodiplomadigital.historicoConsiderarAprovado AS \"cdd.historicoConsiderarAprovado\", configuracaodiplomadigital.historicoConsiderarReprovado AS \"cdd.historicoConsiderarReprovado\", configuracaodiplomadigital.historicoConsiderarCursando AS \"cdd.historicoConsiderarCursando\", configuracaodiplomadigital.historicoConsiderarEvasao AS \"cdd.historicoConsiderarEvasao\", configuracaodiplomadigital.historicoConsiderarForaGrade AS \"cdd.historicoConsiderarForaGrade\", configuracaodiplomadigital.considerarCargaHorariaForaGrade AS \"cdd.considerarCargaHorariaForaGrade\", ");
		sql.append("configuracaodiplomadigital.formatoCargaHorariaXML AS \"cdd.formatoCargaHorariaXML\", configuracaodiplomadigital.tipoLayoutHistoricoGraduacao AS \"cdd.tipoLayoutHistoricoGraduacao\", configuracaodiplomadigital.tipoLayoutHistoricoGraduacaoTecnologica AS \"cdd.tipoLayoutHistoricoGraduacaoTecnologica\", configuracaodiplomadigital.apresentarApenasUltimoHistoricoDisciplina \"cdd.apresentarApenasUltimoHistoricoDisciplina\", configuracaodiplomadigital.considerarCargaHorariaCursadaIgualCargaHorariaPrevista AS \"cdd.considerarCargaHorariaCursadaIgualCargaHorariaPrevista\", configuracaodiplomadigital.validarArquivoComprobatoriaIsPDFA AS \"cdd.validarArquivoComprobatoriaIsPDFA\" ");
		sql.append("FROM configuracaodiplomadigital ");
		sql.append("LEFT JOIN unidadeensino ON unidadeensino.codigo = configuracaodiplomadigital.unidadeensinopadrao ");
		sql.append("LEFT JOIN textopadraodeclaracao textopadraograduacaopadrao ON textopadraograduacaopadrao.codigo = configuracaodiplomadigital.textopadraograduacaopadrao AND configuracaodiplomadigital.layoutgraduacaopadrao IN ('TextoPadrao') ");
		sql.append("LEFT JOIN textopadraodeclaracao textopadraograduacaotecnologicapadrao ON textopadraograduacaotecnologicapadrao.codigo = configuracaodiplomadigital.textopadraograduacaotecnologicapadrao AND configuracaodiplomadigital.layoutgraduacaotecnologicapadrao IN ('TextoPadrao') ");
		sql.append("LEFT JOIN funcionario funcionarioprimario ON funcionarioprimario.codigo = configuracaodiplomadigital.funcionarioprimario ");
		sql.append("LEFT JOIN pessoa pessoaprimario ON pessoaprimario.codigo = funcionarioprimario.pessoa ");
		sql.append("LEFT JOIN cargo cargofuncionarioprimario ON cargofuncionarioprimario.codigo = configuracaodiplomadigital.cargofuncionarioprimario ");
		sql.append("LEFT JOIN funcionario funcionariosecundario ON funcionariosecundario.codigo = configuracaodiplomadigital.funcionariosecundario ");
		sql.append("LEFT JOIN pessoa pessoasecundario ON pessoasecundario.codigo = funcionariosecundario.pessoa ");
		sql.append("LEFT JOIN cargo cargofuncionariosecundario ON cargofuncionariosecundario.codigo = configuracaodiplomadigital.cargofuncionariosecundario ");
		sql.append("LEFT JOIN funcionario funcionarioterceiro ON funcionarioterceiro.codigo = configuracaodiplomadigital.funcionarioterceiro ");
		sql.append("LEFT JOIN pessoa pessoaseterceiro ON pessoaseterceiro.codigo = funcionarioterceiro.pessoa ");
		sql.append("LEFT JOIN cargo cargofuncionarioterceiro ON cargofuncionarioterceiro.codigo = configuracaodiplomadigital.cargofuncionarioterceiro ");
		sql.append("LEFT JOIN funcionario funcionarioquarto ON funcionarioquarto.codigo = configuracaodiplomadigital.funcionarioquarto ");
		sql.append("LEFT JOIN pessoa pessoaquarto ON pessoaquarto.codigo = funcionarioquarto.pessoa ");
		sql.append("LEFT JOIN cargo cargofuncionarioquarto ON cargofuncionarioquarto.codigo = configuracaodiplomadigital.cargofuncionarioquarto ");
		sql.append("LEFT JOIN funcionario funcionarioquinto ON funcionarioquinto.codigo = configuracaodiplomadigital.funcionarioquinto ");
		sql.append("LEFT JOIN pessoa pessoaquinto ON pessoaquinto.codigo = funcionarioquinto.pessoa ");
		sql.append("LEFT JOIN cargo cargofuncionarioquinto ON cargofuncionarioquinto.codigo = configuracaodiplomadigital.cargofuncionarioquinto ");
		return sql;
	}
	
	private SqlRowSet consultaRapidaCarregarDadosTodos(Integer codigo, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaBasicaCarregarDadosTodos());
		sql.append("WHERE configuracaodiplomadigital.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados. (Configuração Diploma Digital)");
		}
		return tabelaResultado;
	}

	private List<ConfiguracaoDiplomaDigitalVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados) {
		List<ConfiguracaoDiplomaDigitalVO> lista = new ArrayList<ConfiguracaoDiplomaDigitalVO>(0);
		while (tabelaResultado.next()) {
			lista.add(montarDadosBasica(tabelaResultado, nivelMontarDados));
		}
		return lista;
	}

	private ConfiguracaoDiplomaDigitalVO montarDadosBasica(SqlRowSet tabelaResultado, int nivelMontarDados) {
		ConfiguracaoDiplomaDigitalVO obj = new ConfiguracaoDiplomaDigitalVO();
		obj.setCodigo(tabelaResultado.getInt("configuracaodiplomadigital.codigo"));
		obj.setDescricao(tabelaResultado.getString("configuracaodiplomadigital.descricao"));
		obj.setPadrao(tabelaResultado.getBoolean("configuracaodiplomadigital.padrao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			obj.setNivelMontarDados(NivelMontarDados.COMBOBOX);
			return obj;
		}
		if (tabelaResultado.getString("configuracaodiplomadigital.versao") != null) {
			obj.setVersao(VersaoDiplomaDigitalEnum.getEnum(tabelaResultado.getString("configuracaodiplomadigital.versao")));
		}
		if (tabelaResultado.getString("configuracaodiplomadigital.formatoCargaHorariaXML") != null) {
			obj.setFormatoCargaHorariaXML(FormatoCargaHorariaXmlEnum.getEnum(tabelaResultado.getString("configuracaodiplomadigital.formatoCargaHorariaXML")));
		}
		obj.getUnidadeEnsinoPadrao().setCodigo(tabelaResultado.getInt("unidadeensinopadrao.codigo"));
		obj.getUnidadeEnsinoPadrao().setNome(tabelaResultado.getString("unidadeensinopadrao.nome"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		return obj;
	}
	
	private void montarDadosTodos(ConfiguracaoDiplomaDigitalVO obj, UsuarioVO usuario, SqlRowSet tabelaResultado) throws Exception {
		obj.setCodigo(tabelaResultado.getInt("cdd.codigo"));
		obj.setDataCadastro(tabelaResultado.getDate("cdd.datacadastro"));
		obj.setDescricao(tabelaResultado.getString("cdd.descricao"));
		obj.setPadrao(tabelaResultado.getBoolean("cdd.padrao"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("unidadeensino.codigo"))) {
			obj.getUnidadeEnsinoPadrao().setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
			obj.getUnidadeEnsinoPadrao().setNome(tabelaResultado.getString("unidadeensino.nome"));
		}
		if (tabelaResultado.getString("cdd.utilizarcampoperiododisciplina") != null) {
			obj.setUtilizarCampoPeriodoDisciplina(CampoPeriodoDisciplinaEnum.getEnum(tabelaResultado.getString("cdd.utilizarcampoperiododisciplina")));
		}
		if (tabelaResultado.getString("cdd.coordenadorcursodisciplinasaproveitadas") != null) {
			obj.setCoordenadorCursoDisciplinasAproveitadas(CoordenadorCursoDisciplinaAproveitadaEnum.getEnum(tabelaResultado.getString("cdd.coordenadorcursodisciplinasaproveitadas")));
		}
		obj.setHoraRelogio(tabelaResultado.getInt("cdd.horaRelogio"));
		obj.setUtilizarCoordenadorCursoAtividadeComplementar(tabelaResultado.getBoolean("cdd.utilizarcoordenadorcursoatividadecomplementar"));
		obj.setApresentarTextoEnade(tabelaResultado.getBoolean("cdd.apresentarTextoEnade"));
		obj.setLayoutGraduacaoPadrao(tabelaResultado.getString("cdd.layoutgraduacaopadrao"));
		obj.setLayoutGraduacaoTecnologicaPadrao(tabelaResultado.getString("cdd.layoutgraduacaotecnologicapadrao"));
		obj.setHistoricoConsiderarAprovado(tabelaResultado.getBoolean("cdd.historicoConsiderarAprovado"));
		obj.setHistoricoConsiderarReprovado(tabelaResultado.getBoolean("cdd.historicoConsiderarReprovado"));
		obj.setHistoricoConsiderarCursando(tabelaResultado.getBoolean("cdd.historicoConsiderarCursando"));
		obj.setHistoricoConsiderarEvasao(tabelaResultado.getBoolean("cdd.historicoConsiderarEvasao"));
		obj.setHistoricoConsiderarForaGrade(tabelaResultado.getBoolean("cdd.historicoConsiderarForaGrade"));
		obj.setConsiderarCargaHorariaForaGrade(tabelaResultado.getBoolean("cdd.considerarCargaHorariaForaGrade"));
		obj.setTipoLayoutHistoricoGraduacao(tabelaResultado.getString("cdd.tipoLayoutHistoricoGraduacao"));
		obj.setTipoLayoutHistoricoGraduacaoTecnologica(tabelaResultado.getString("cdd.tipoLayoutHistoricoGraduacaoTecnologica"));
		obj.setApresentarApenasUltimoHistoricoDisciplina(tabelaResultado.getBoolean("cdd.apresentarApenasUltimoHistoricoDisciplina"));
		obj.setConsiderarCargaHorariaCursadaIgualCargaHorariaPrevista(tabelaResultado.getBoolean("cdd.considerarCargaHorariaCursadaIgualCargaHorariaPrevista"));
		obj.setValidarArquivoComprobatoriaIsPDFA(tabelaResultado.getBoolean("cdd.validarArquivoComprobatoriaIsPDFA"));
		if (tabelaResultado.getString("cdd.versao") != null) {
			obj.setVersao(VersaoDiplomaDigitalEnum.getEnum(tabelaResultado.getString("cdd.versao")));
		}
		if (tabelaResultado.getString("cdd.formatoCargaHorariaXML") != null) {
			obj.setFormatoCargaHorariaXML(FormatoCargaHorariaXmlEnum.getEnum(tabelaResultado.getString("cdd.formatoCargaHorariaXML")));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("textopadraograduacaopadrao.codigo"))) {
			obj.getTextoPadraoGraduacaoPadrao().setCodigo(tabelaResultado.getInt("textopadraograduacaopadrao.codigo"));
			obj.getTextoPadraoGraduacaoPadrao().setDescricao(tabelaResultado.getString("textopadraograduacaopadrao.descricao"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("tpgtp.codigo"))) {
			obj.getTextoPadraoGraduacaoTecnologicaPadrao().setCodigo(tabelaResultado.getInt("tpgtp.codigo"));
			obj.getTextoPadraoGraduacaoTecnologicaPadrao().setDescricao(tabelaResultado.getString("tpgtp.descricao"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionarioprimario.codigo"))) {
			obj.getFuncionarioPrimario().setCodigo(tabelaResultado.getInt("funcionarioprimario.codigo"));
			obj.getFuncionarioPrimario().setMatricula(tabelaResultado.getString("funcionarioprimario.matricula"));
			obj.getFuncionarioPrimario().getPessoa().setCodigo(tabelaResultado.getInt("pessoaprimario.codigo"));
			obj.getFuncionarioPrimario().getPessoa().setNome(tabelaResultado.getString("pessoaprimario.nome"));
			obj.setTituloFuncionarioPrimario(tabelaResultado.getString("cdd.titulofuncionarioprimario"));
			obj.getCargoFuncionarioPrimario().setCodigo(tabelaResultado.getInt("cfp.codigo"));
			obj.getCargoFuncionarioPrimario().setNome(tabelaResultado.getString("cfp.nome"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionariosecundario.codigo"))) {
			obj.getFuncionarioSecundario().setCodigo(tabelaResultado.getInt("funcionariosecundario.codigo"));
			obj.getFuncionarioSecundario().setMatricula(tabelaResultado.getString("funcionariosecundario.matricula"));
			obj.getFuncionarioSecundario().getPessoa().setCodigo(tabelaResultado.getInt("pessoasecundario.codigo"));
			obj.getFuncionarioSecundario().getPessoa().setNome(tabelaResultado.getString("pessoasecundario.nome"));
			obj.setTituloFuncionarioSecundario(tabelaResultado.getString("cdd.titulofuncionariosecundario"));
			obj.getCargoFuncionarioSecundario().setCodigo(tabelaResultado.getInt("cfs.codigo"));
			obj.getCargoFuncionarioSecundario().setNome(tabelaResultado.getString("cfs.nome"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionarioterceiro.codigo"))) {
			obj.getFuncionarioTerceiro().setCodigo(tabelaResultado.getInt("funcionarioterceiro.codigo"));
			obj.getFuncionarioTerceiro().setMatricula(tabelaResultado.getString("funcionarioterceiro.matricula"));
			obj.getFuncionarioTerceiro().getPessoa().setCodigo(tabelaResultado.getInt("pessoaseterceiro.codigo"));
			obj.getFuncionarioTerceiro().getPessoa().setNome(tabelaResultado.getString("pessoaseterceiro.nome"));
			obj.setTituloFuncionarioTerceiro(tabelaResultado.getString("cdd.titulofuncionarioterceiro"));
			obj.getCargoFuncionarioTerceiro().setCodigo(tabelaResultado.getInt("cft.codigo"));
			obj.getCargoFuncionarioTerceiro().setNome(tabelaResultado.getString("cft.nome"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionarioquarto.codigo"))) {
			obj.getFuncionarioQuarto().setCodigo(tabelaResultado.getInt("funcionarioquarto.codigo"));
			obj.getFuncionarioQuarto().setMatricula(tabelaResultado.getString("funcionarioquarto.matricula"));
			obj.getFuncionarioQuarto().getPessoa().setCodigo(tabelaResultado.getInt("pessoaquarto.codigo"));
			obj.getFuncionarioQuarto().getPessoa().setNome(tabelaResultado.getString("pessoaquarto.nome"));
			obj.setTituloFuncionarioQuarto(tabelaResultado.getString("cdd.titulofuncionarioquarto"));
			obj.getCargoFuncionarioQuarto().setCodigo(tabelaResultado.getInt("cfqua.codigo"));
			obj.getCargoFuncionarioQuarto().setNome(tabelaResultado.getString("cfqua.nome"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("funcionarioquinto.codigo"))) {
			obj.getFuncionarioQuinto().setCodigo(tabelaResultado.getInt("funcionarioquinto.codigo"));
			obj.getFuncionarioQuinto().setMatricula(tabelaResultado.getString("funcionarioquinto.matricula"));
			obj.getFuncionarioQuinto().getPessoa().setCodigo(tabelaResultado.getInt("pessoaquinto.codigo"));
			obj.getFuncionarioQuinto().getPessoa().setNome(tabelaResultado.getString("pessoaquinto.nome"));
			obj.setTituloFuncionarioQuinto(tabelaResultado.getString("cdd.titulofuncionarioquinto"));
			obj.getCargoFuncionarioQuinto().setCodigo(tabelaResultado.getInt("cfqui.codigo"));
			obj.getCargoFuncionarioQuinto().setNome(tabelaResultado.getString("cfqui.nome"));
		}
		if (obj.isTipoLayoutGraduacaoPersonalizado()) {
			obj.setConfiguracaoLayoutHistoricoGraduacao(getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().consultarPorChavePrimaria(Integer.valueOf(obj.getTipoLayoutHistoricoGraduacao()), Boolean.FALSE, usuario));
		}
		if (obj.isTipoLayoutGraduacaoTecnologicaPersonalizado()) {
			obj.setConfiguracaoLayoutHistoricoGraduacaoTecnologica(getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().consultarPorChavePrimaria(Integer.valueOf(obj.getTipoLayoutHistoricoGraduacaoTecnologica()), Boolean.FALSE, usuario));
		}
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
	}
	
	@Override
	public List<ConfiguracaoDiplomaDigitalVO> listarTodasConfiguracoes(Boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(getSQLPadraoConsultaBasica());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados);
	}
	
	@Override
	public ConfiguracaoDiplomaDigitalVO consultarConfiguracaoExistente(Integer codigoUnidadeEnsino, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT configuracaodiplomadigital.codigo FROM configuracaodiplomadigital ");
		sql.append("LEFT JOIN unidadeensino unidadeensino2 ON unidadeensino2.configuracaodiplomadigital = configuracaodiplomadigital.codigo AND unidadeensino2.codigo IN (?) ");
		sql.append("WHERE (unidadeensino2.codigo = ? AND unidadeensino2.configuracaodiplomadigital IS NOT NULL) ");
		sql.append("OR configuracaodiplomadigital.padrao ");
		sql.append("ORDER BY CASE WHEN unidadeensino2.codigo IS NOT NULL THEN 0 ELSE 1 END LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoUnidadeEnsino, codigoUnidadeEnsino);
		if (!tabelaResultado.next()) {
			return new ConfiguracaoDiplomaDigitalVO();
		}
		return getAplicacaoControle().getConfiguracaoDiplomaDigitalVO(tabelaResultado.getInt("codigo"), usuario);
	}

}
