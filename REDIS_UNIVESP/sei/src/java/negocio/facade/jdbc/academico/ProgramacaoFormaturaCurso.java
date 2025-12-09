package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ProgramacaoFormaturaCursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ProgramacaoFormaturaCursoInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class ProgramacaoFormaturaCurso extends ControleAcesso implements ProgramacaoFormaturaCursoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static List<ProgramacaoFormaturaCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<ProgramacaoFormaturaCursoVO> vetResultado = new ArrayList<ProgramacaoFormaturaCursoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	private static ProgramacaoFormaturaCursoVO montarDados(SqlRowSet tabelaResultado) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramacaoFormaturaCursoVO> consultarCursosPorCodigoProgramacaoFormatura(Integer codigoProgramacao, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(c.codigo) as \"qtdAlunos\", pf.codigo as \"pf.codigo\", c.codigo \"c.codigo\", c.nome \"c.nome\", c.nrregistrointerno \"c.nrregistrointerno\", ");
		sql.append(" EXISTS (SELECT codigo FROM documentoassinado WHERE (programacaoformatura = pf.codigo and curso = c.codigo and documentoassinado.documentoAssinadoInvalido = false)) AS \"documentoAssinado\", ");
		sql.append(" docAssinadoPessoa.qtdAlunoVinculados ");
		sql.append(" FROM programacaoformaturaaluno pfa ");
		sql.append(" INNER JOIN programacaoformatura pf on pf.codigo 	= pfa.programacaoformatura ");
		sql.append(" INNER JOIN matricula m on m.matricula 	= pfa.matricula ");
		sql.append(" INNER JOIN curso c on c.codigo = m.curso ");
		sql.append(" left join lateral (select count(documentoassinadopessoa.codigo) as qtdAlunoVinculados  from documentoassinadopessoa ");
		sql.append(" 		 	inner join documentoassinado on documentoassinado.codigo = documentoassinadopessoa.documentoassinado ");
		sql.append(" 		 	and documentoassinado.curso = c.codigo  and documentoassinado.programacaoformatura  = pf.codigo and documentoassinado.documentoAssinadoInvalido = false ");
		sql.append(" 		 	inner join programacaoformaturaaluno on programacaoformaturaaluno.programacaoformatura = pf.codigo ");
		sql.append(" 		 	inner join matricula  on matricula.matricula = programacaoformaturaaluno.matricula and  matricula.curso = c.codigo ");
		sql.append(" 		 	where documentoassinadopessoa.tipopessoa = 'ALUNO'  and documentoassinadopessoa.pessoa = matricula.aluno ");
		sql.append(" 		 ) as docAssinadoPessoa on 1=1");
		sql.append(" WHERE pf.codigo = " + codigoProgramacao);
		getFacadeFactory().getProgramacaoFormaturaAlunoFacade().adicionarFiltroValidacaoAlunoColouGrauCondicaoWhere(sql, "pfa", "m");
		sql.append(" GROUP BY pf.codigo, c.codigo, c.nome, c.nrregistrointerno, docAssinadoPessoa.qtdAlunoVinculados ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<ProgramacaoFormaturaCursoVO> lista = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			ProgramacaoFormaturaCursoVO programacao = new ProgramacaoFormaturaCursoVO();
			List<AutorizacaoCursoVO> listaPrimeiroRecon = getFacadeFactory().getAutorizacaoCursoFacade().consultarDataPorCurso(tabelaResultado.getInt("c.codigo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
			List<AutorizacaoCursoVO> listaRenovacaoRecon = getFacadeFactory().getAutorizacaoCursoFacade().consultarDataDescPorCurso(tabelaResultado.getInt("c.codigo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
			programacao.getListaSelectItemPrimeiroReconhecimento().add(new SelectItem("", ""));
			programacao.getListaSelectItemRenovacaoReconhecimento().add(new SelectItem("", ""));
			if (!listaPrimeiroRecon.isEmpty() && !listaRenovacaoRecon.isEmpty()) {
				for (AutorizacaoCursoVO autorizacao : listaPrimeiroRecon) {
					programacao.getListaSelectItemPrimeiroReconhecimento().add(new SelectItem(autorizacao.getNome(), autorizacao.getNome()));
				}
				for (AutorizacaoCursoVO autorizacao : listaRenovacaoRecon) {
					programacao.getListaSelectItemRenovacaoReconhecimento().add(new SelectItem(autorizacao.getNome(), autorizacao.getNome()));
				}
			}
			programacao.getProgramacaoFormaturaVO().setCodigo(tabelaResultado.getInt("pf.codigo"));
			programacao.getCursoVO().setCodigo(tabelaResultado.getInt("c.codigo"));
			programacao.getCursoVO().setNome(tabelaResultado.getString("c.nome"));
			programacao.setQuantidadeAlunosCurso(tabelaResultado.getInt("qtdAlunos"));
			programacao.setNrRegistroInternoCurso(tabelaResultado.getString("c.nrregistrointerno"));
			programacao.setCursoProgramacaoDocumentoAssinado(tabelaResultado.getBoolean("documentoAssinado"));			
			programacao.setExisteAlunoSemVinculoDocumentoAssinado(tabelaResultado.getInt("qtdAlunos") != tabelaResultado.getInt("qtdAlunoVinculados"));			
			if (programacao.getCursoProgramacaoDocumentoAssinado()) {
				programacao.setMensagemExisteDocumentoAssinado("O Curso " + programacao.getCursoVO().getNome()+ " já tem Documento Assinado no Sistema");
			} else {
				programacao.setMensagemExisteDocumentoAssinado("Nenhum Documento Assinado para esse Curso");
			}
			lista.add(programacao);
		}
		return lista;
	}

}
