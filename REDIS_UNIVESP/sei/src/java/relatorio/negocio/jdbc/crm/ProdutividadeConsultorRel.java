package relatorio.negocio.jdbc.crm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.TipoSituacaoCompromissoEnum;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.crm.ProdutividadeConsultorRelVO;
import relatorio.negocio.interfaces.crm.ProdutividadeConsultorRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Pedro Andrade
 */
@Repository
@Scope("singleton")
@Lazy
public class ProdutividadeConsultorRel extends SuperRelatorio implements ProdutividadeConsultorRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2380265051524225246L;

	public void validarDados(ProdutividadeConsultorRelVO filtro) throws Exception {
		filtro.getListaUnidadeEnsinoVO().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)
			.findAny().orElseThrow(() -> new Exception("O campo UNIDADE DE ENSINO deve ser informado."));
		
		if (!Uteis.isAtributoPreenchido(filtro.getDataInicio()) || !Uteis.isAtributoPreenchido(filtro.getDataFim())) {
			throw new Exception("O campo Período deve ser informado.");
		}
	}

	@Override
	public List<ProdutividadeConsultorRelVO> criarObjeto(ProdutividadeConsultorRelVO filtro, UsuarioVO usuarioVO) throws Exception {
		validarDados(filtro);
		if (filtro.getTipoRelatorio().equals("sintetico")) {
			return executarCriacaoRelatorioSintetico(filtro, usuarioVO);
		} else {
			return executarCriacaoRelatorioAnalitico(filtro, usuarioVO);
		}
	}

	private List<ProdutividadeConsultorRelVO> executarCriacaoRelatorioSintetico(ProdutividadeConsultorRelVO filtro, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet tabelaResultado = consultarSintetica(filtro, usuarioVO);
		List<ProdutividadeConsultorRelVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			ProdutividadeConsultorRelVO obj = new ProdutividadeConsultorRelVO();
			obj.getConsultor().setNome(tabelaResultado.getString("consultor"));
			obj.setContAguardando(tabelaResultado.getInt("contAguardando"));
			obj.setContInsucesso(tabelaResultado.getInt("contInsucesso"));
			obj.setContNaoPossuiAgenda(tabelaResultado.getInt("contNaoPossuiAgenda"));
			obj.setContParalizado(tabelaResultado.getInt("contParalizado"));
			obj.setContRealizado(tabelaResultado.getInt("contRealizado"));
			obj.setContRemarcacao(tabelaResultado.getInt("contRemarcacao"));
			obj.setContCompromisso(tabelaResultado.getInt("contComprimisso"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private List<ProdutividadeConsultorRelVO> executarCriacaoRelatorioAnalitico(ProdutividadeConsultorRelVO filtro, UsuarioVO usuarioVO) throws Exception {
		SqlRowSet tabelaResultado = consultarAnalitico(filtro, usuarioVO);
		List<ProdutividadeConsultorRelVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			ProdutividadeConsultorRelVO obj = new ProdutividadeConsultorRelVO();
			obj.getConsultor().setNome(tabelaResultado.getString("consultor"));
			obj.getCampanhaVO().setDescricao(tabelaResultado.getString("campanha"));
			obj.setProspect(tabelaResultado.getString("prospect"));
			obj.setDataCompromisso(tabelaResultado.getDate("datacompromisso"));
			obj.setDataInicialcompromisso(tabelaResultado.getDate("datainicialcompromisso"));
			obj.setTipoSituacaoCompromisso(TipoSituacaoCompromissoEnum.valueOf(tabelaResultado.getString("tiposituacaocompromissoenum")).getDescricao());
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private SqlRowSet consultarSintetica(ProdutividadeConsultorRelVO filtro, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select ");
		sb.append(" sum (case when tiposituacaocompromissoenum = 'AGUARDANDO_CONTATO' then 1 else 0 end) as contAguardando ,  ");
		sb.append(" sum (case when tiposituacaocompromissoenum = 'PARALIZADO' then 1 else 0 end) as contParalizado ,   ");
		sb.append(" sum (case when tiposituacaocompromissoenum = 'REALIZADO' then 1 else 0 end) as contRealizado ,  ");
		sb.append(" sum (case when tiposituacaocompromissoenum = 'REALIZADO_COM_INSUCESSO_CONTATO' then 1 else 0 end) as contInsucesso,  ");
		sb.append(" sum (case when tiposituacaocompromissoenum = 'REALIZADO_COM_REMARCACAO' then 1 else 0 end) as contRemarcacao ,  ");
		sb.append(" sum (case when tiposituacaocompromissoenum = 'NAO_POSSUI_AGENDA' then 1 else 0 end) as contNaoPossuiAgenda ,  ");
		sb.append(" count (prospects.nome) as contComprimisso ,  ");
		sb.append(" pessoa.nome as consultor   ");
		sb.append(" from compromissoagendapessoahorario  ");
		sb.append(" inner join campanha on campanha.codigo = compromissoagendapessoahorario.campanha  ");
		sb.append(" inner join agendapessoahorario  on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario  ");
		sb.append(" inner join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa  ");
		sb.append(" inner join pessoa on pessoa.codigo = agendapessoa.pessoa  ");
		sb.append(" inner join prospects on prospects.codigo = compromissoagendapessoahorario.prospect  ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = campanha.unidadeensino  ");
		if (filtro.getListaCursoVO().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
			sb.append(" inner join curso on curso.codigo = campanha.curso  ");
		}
		sb.append(" where 1=1 ");
		montarFiltroRelatorio(sb, filtro);
		sb.append(" group by  pessoa.nome ");
		sb.append(" ORDER BY pessoa.nome");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

	}

	private SqlRowSet consultarAnalitico(ProdutividadeConsultorRelVO filtro, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select pessoa.nome as consultor, campanha.descricao as campanha, prospects.nome as prospect, datacompromisso, ");
		sb.append(" datainicialcompromisso, tiposituacaocompromissoenum  ");
		sb.append(" from compromissoagendapessoahorario  ");
		sb.append(" inner join campanha on campanha.codigo = compromissoagendapessoahorario.campanha  ");
		sb.append(" inner join agendapessoahorario  on agendapessoahorario.codigo = compromissoagendapessoahorario.agendapessoahorario  ");
		sb.append(" inner join agendapessoa on agendapessoa.codigo = agendapessoahorario.agendapessoa  ");
		sb.append(" inner join pessoa on pessoa.codigo = agendapessoa.pessoa  ");
		sb.append(" inner join prospects on prospects.codigo = compromissoagendapessoahorario.prospect  ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = campanha.unidadeensino  ");
		if (filtro.getListaCursoVO().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
			sb.append(" inner join curso on curso.codigo = campanha.curso  ");
		}
		sb.append(" where 1=1 ");
		montarFiltroRelatorio(sb, filtro);
		sb.append(" ORDER BY pessoa.nome, campanha.descricao, prospects.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());

	}

	private void montarFiltroRelatorio(StringBuilder sb, ProdutividadeConsultorRelVO filtro) {
		if (filtro.getListaUnidadeEnsinoVO().stream().anyMatch(UnidadeEnsinoVO::getFiltrarUnidadeEnsino)) {
			sb.append(filtro.getListaUnidadeEnsinoVO().stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getCodigo)
					.map(String::valueOf).collect(Collectors.joining(", ", " AND unidadeensino.codigo IN (", ") ")));
		}
		if (filtro.getListaCursoVO().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
			sb.append(filtro.getListaCursoVO().stream().filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getCodigo)
					.map(String::valueOf).collect(Collectors.joining(", ", " AND curso.codigo IN (", ") ")));
		}
		if (Uteis.isAtributoPreenchido(filtro.getCampanhaVO())) {
			sb.append(" AND campanha.codigo = ").append(filtro.getCampanhaVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(filtro.getDataInicio()) && Uteis.isAtributoPreenchido(filtro.getDataFim())) {
			sb.append(" AND datacompromisso::DATE >= '").append(Uteis.getDataJDBC(filtro.getDataInicio())).append("' ");
			sb.append(" AND datacompromisso::DATE <= '").append(Uteis.getDataJDBC(filtro.getDataFim())).append("' ");
		}

		if (Uteis.isAtributoPreenchido(filtro.getConsultor())) {
			sb.append(" AND pessoa.codigo = ").append(filtro.getConsultor().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(filtro.getSituacao())) {
			if (filtro.getSituacao().equals("realizado")) {
				sb.append(" AND tiposituacaocompromissoenum in( ");
				sb.append(" '").append(TipoSituacaoCompromissoEnum.PARALIZADO.name()).append("',");
				sb.append(" '").append(TipoSituacaoCompromissoEnum.REALIZADO.name()).append("',");
				sb.append(" '").append(TipoSituacaoCompromissoEnum.REALIZADO_COM_INSUCESSO_CONTATO.name()).append("',");
				sb.append(" '").append(TipoSituacaoCompromissoEnum.REALIZADO_COM_REMARCACAO.name()).append("')");
				sb.append(" AND (historicoReagendamentoCompromisso is null or historicoReagendamentoCompromisso = '' )");

			} else if (filtro.getSituacao().equals("naoRealizado")) {
				sb.append(" AND tiposituacaocompromissoenum in( '").append(TipoSituacaoCompromissoEnum.AGUARDANDO_CONTATO.name()).append("')");
				sb.append(" AND (historicoReagendamentoCompromisso is null or historicoReagendamentoCompromisso = '' )");
			} else if (filtro.getSituacao().equals("reagendado")) {
				sb.append(" AND tiposituacaocompromissoenum in( '").append(TipoSituacaoCompromissoEnum.REALIZADO_COM_REMARCACAO.name()).append("')");
				sb.append(" AND (historicoReagendamentoCompromisso is not null or historicoReagendamentoCompromisso != '' )");
			}
		}
	}

	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "crm" + File.separator);
	}

	public static String getDesignIReportRelatorio(ProdutividadeConsultorRelVO obj) {
		if (obj.isTipoRelatorioSintetico()) {
			return (caminhoBaseRelatorio() + getIdEntidadeSintetico() + ".jrxml");
		} else {
			return (caminhoBaseRelatorio() + getIdEntidadeAnalitico() + ".jrxml");
		}
	}

	public static String getIdEntidadeSintetico() {
		return ("ProdutividadeConsultorSinteticoRel");
	}

	public static String getIdEntidadeAnalitico() {
		return ("ProdutividadeConsultorAnaliticoRel");
	}

}
