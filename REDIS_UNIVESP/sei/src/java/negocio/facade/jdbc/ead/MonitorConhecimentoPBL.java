package negocio.facade.jdbc.ead;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GraficoAproveitamentoAssuntoItemPBLVO;
import negocio.comuns.ead.GraficoAproveitamentoAssuntoPBLVO;
import negocio.comuns.ead.MonitorConhecimentoPBLVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.MonitorConhecimentoPBLInterfaceFacade;

/**
 * @author Pedro Andrade 09/01/2017
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class MonitorConhecimentoPBL extends ControleAcesso implements MonitorConhecimentoPBLInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3181502840308638543L;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void montarGraficoAproveitamentoAssuntoPBLVO(MonitorConhecimentoPBLVO obj, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception {
		SqlRowSet rsPizza =  consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoPizza(obj, null, isOutraTurma, usuarioLogado);
		montarDadosRapidoGraficoPizza(rsPizza, obj, "PIZZA", usuarioLogado);
		SqlRowSet rsBarra =  consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoBarra(obj, null, isOutraTurma, usuarioLogado);
		montarDadosRapidoGraficoBarra(rsBarra, obj, "BARRA", usuarioLogado);
		for (GraficoAproveitamentoAssuntoPBLVO objExistente : obj.getListaGraficoAproveitamentoAssuntoPBLVOs()) {
			objExistente.gerarGraficoBarraEmJavaScript();
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void montarGraficoAproveitamentoAssuntoOutraTurma(GraficoAproveitamentoAssuntoPBLVO obj, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception {
		SqlRowSet rsPizza =  consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoPizza(obj.getMonitorConhecimentoPBLVO(), obj.getCodigoTemaAssunto(), isOutraTurma, usuarioLogado);
		montarDadosRapidoGraficoPizzaPorGraficoAproveitamento(rsPizza, obj, "PIZZA_OUTRA_TURMA", usuarioLogado);
		SqlRowSet rsBarra =  consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoBarra(obj.getMonitorConhecimentoPBLVO(), obj.getCodigoTemaAssunto(), isOutraTurma, usuarioLogado);
		GraficoAproveitamentoAssuntoPBLVO graficoBarra = montarDadosRapidoGraficoBarraPorGraficoAproveitamento(rsBarra, "BARRA_OUTRA_TURMA", usuarioLogado);
		graficoBarra.setMonitorConhecimentoPBLVO(obj.getMonitorConhecimentoPBLVO());
		graficoBarra.setCodigoTemaAssunto(obj.getCodigoTemaAssunto());
		graficoBarra.gerarGraficoBarraOutraTurmaEmJavaScript();
		obj.setSerieGraficoBarraOutraTurmaEmJavaScript(graficoBarra.getSerieGraficoBarraOutraTurmaEmJavaScript());
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void montarGraficoAproveitamentoAssuntoPorTipoNota(GraficoAproveitamentoAssuntoPBLVO obj, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception {
		SqlRowSet rsBarra =  consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoBarraPorTipoNota(obj.getMonitorConhecimentoPBLVO(), obj.getCodigoTemaAssunto(), isOutraTurma, usuarioLogado);
		GraficoAproveitamentoAssuntoPBLVO graficoBarra =  montarDadosRapidoGraficoBarraPorGraficoAproveitamento(rsBarra, "BARRA_TIPO_NOTA", usuarioLogado);
		graficoBarra.setMonitorConhecimentoPBLVO(obj.getMonitorConhecimentoPBLVO());
		graficoBarra.setCodigoTemaAssunto(obj.getCodigoTemaAssunto());
		graficoBarra.gerarGraficoBarraPorNotaEmJavaScript();
		obj.setSerieGraficoBarraPorNotaEmJavaScript(graficoBarra.getSerieGraficoBarraPorNotaEmJavaScript());
		obj.setCategoriaGraficoBarraPorNotaEmJavaScript(graficoBarra.getCategoriaGraficoBarraPorNotaEmJavaScript());
		
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private SqlRowSet consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoPizza(MonitorConhecimentoPBLVO obj, Integer codigoAssunto, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception {

		StringBuilder sb = new StringBuilder(" select codigo_assunto, nome_assunto ,   codigoparametro ,  ");
		sb.append(" descricaoparametro,  corgrafico, corletragrafico, sum(qtdAluno) as qtdAluno  ");
		sb.append(" from (  ");	
		sb.append(" select codigo_assunto, nome_assunto , ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.codigo as codigoparametro , itemparametrosmonitoramentoavaliacaoonline.descricaoparametro as descricaoparametro,  ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.corgrafico as corgrafico, itemparametrosmonitoramentoavaliacaoonline.corletragrafico as corletragrafico, ");
		sb.append(" avaliado, ");
		sb.append(" case when itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= t.media and percentualAcertosAte >= t.media  then 1 else 0 end  as qtdAluno   ");
		sb.append(" from (   ");
		sb.append(" select turma.configuracaoead, gecta.avaliado ,   ");
		sb.append(" case when ta.codigo is not null then ta.codigo else 0 end as codigo_assunto, ta.nome as nome_assunto,  ");
		sb.append(" ( sum(case  when gecta.nota is not null then gecta.nota else 0 end) / count(gecta.gestaoeventoconteudoturma))::numeric(12,2) as media  ");
		sb.append(" from gestaoeventoconteudoturma   gect  ");
		sb.append(" inner join turma on turma.codigo = gect.turma ");
		sb.append(" inner join gestaoeventoconteudoturmaavaliacaopbl gecta on gecta.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional ");
		sb.append(" inner join conteudounidadepagina cup on cupre.conteudounidadepagina = cup.codigo  ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo  ");
		sb.append(" left join temaassunto ta on ta.codigo = uc.temaassunto ");
		sb.append(" where gecta.tipoavaliacao = 'RESULTADO_FINAL' ");
		sb.append(" and gecta.situacao = 'REALIZADO' ");
		sb.append(" and gect.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
		sb.append(" and gect.conteudo = ").append(obj.getConteudoVO().getCodigo());
		sb.append(" and gect.ano = '").append(obj.getAno()).append("' ");
		sb.append(" and gect.semestre = '").append(obj.getSemestre()).append("' ");
		if(!isOutraTurma){
			sb.append(" and gect.turma = ").append(obj.getTurmaVO().getCodigo());	
		}else{
			sb.append(" and gect.turma != ").append(obj.getTurmaVO().getCodigo());
		}
		if(codigoAssunto != null &&  codigoAssunto != 0){
			sb.append(" and ta.codigo = ").append(codigoAssunto);	
		} else if(codigoAssunto != null &&  codigoAssunto == 0){
			sb.append(" and ta.codigo is null ");
		}
		
		sb.append(" group by turma.configuracaoead, gecta.avaliado, ta.codigo, ta.nome  ");
		sb.append(" ) as t ");
		sb.append(" inner join configuracaoead on configuracaoead.codigo = t.configuracaoead ");
		sb.append(" inner join parametrosmonitoramentoavaliacaoonline on parametrosmonitoramentoavaliacaoonline.codigo = configuracaoead.parametrosmonitoramentoavaliacaoonline ");
		sb.append(" inner join itemparametrosmonitoramentoavaliacaoonline on itemparametrosmonitoramentoavaliacaoonline.parametrosmonitoramentoavaliacaoonline = parametrosmonitoramentoavaliacaoonline.codigo ");
		sb.append(" group by codigo_assunto, nome_assunto , itemparametrosmonitoramentoavaliacaoonline.codigo, itemparametrosmonitoramentoavaliacaoonline.descricaoparametro, ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.corgrafico, itemparametrosmonitoramentoavaliacaoonline.corletragrafico, avaliado , t.media ");
		
		sb.append(" ) as tt  ");
		sb.append(" group by codigo_assunto, nome_assunto , codigoparametro, descricaoparametro,    ");
		sb.append(" corgrafico, corletragrafico  ");
		sb.append(" order by codigo_assunto,  descricaoparametro  ");

		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void montarDadosRapidoGraficoPizza(SqlRowSet rs, MonitorConhecimentoPBLVO obj, String origemGrafico, UsuarioVO usuario) throws Exception {

		obj.setNovoObj(false);
		while (rs.next()) {
			GraficoAproveitamentoAssuntoPBLVO grafico = consultarGraficoAproveitamentoAssuntoPBLVO(rs.getInt("codigo_assunto"), obj);
			if (grafico.getCodigoTemaAssunto() == null) {
				grafico.setCodigoTemaAssunto(rs.getInt("codigo_assunto"));
				grafico.setAssunto(rs.getString("nome_assunto"));
				grafico.setMonitorConhecimentoPBLVO(obj);
			}
			GraficoAproveitamentoAssuntoItemPBLVO itemGrafico = new GraficoAproveitamentoAssuntoItemPBLVO();
			itemGrafico.setCodigoParametro(rs.getInt("codigoparametro"));
			itemGrafico.setDescricaoParametro(rs.getString("descricaoparametro"));
			itemGrafico.setQuantidadeAlunos(rs.getString("qtdAluno"));
			itemGrafico.setCorGrafico(rs.getString("corgrafico"));
			itemGrafico.setCorLetraGrafico(rs.getString("corletragrafico"));
			itemGrafico.setOrigemGrafico(origemGrafico);
			itemGrafico.setGraficoAproveitamentoAssuntoPBLVO(grafico);
			if (!Uteis.isAtributoPreenchido(grafico.getSerieGraficoPizzaEmJavaScript())) {
				grafico.setSerieGraficoPizzaEmJavaScript(itemGrafico.getSerieGraficoPizza());
			} else {
				grafico.setSerieGraficoPizzaEmJavaScript(grafico.getSerieGraficoPizzaEmJavaScript() + ", " + itemGrafico.getSerieGraficoPizza());
			}
			grafico.getGraficoItemPizza().add(itemGrafico);
			addGraficoAproveitamentoAssuntoPBLVO(grafico, obj);
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void montarDadosRapidoGraficoPizzaPorGraficoAproveitamento(SqlRowSet rs, GraficoAproveitamentoAssuntoPBLVO grafico, String origemGrafico, UsuarioVO usuario) throws Exception {		
		while (rs.next()) {
			GraficoAproveitamentoAssuntoItemPBLVO itemGrafico = new GraficoAproveitamentoAssuntoItemPBLVO();
			itemGrafico.setCodigoParametro(rs.getInt("codigoparametro"));
			itemGrafico.setDescricaoParametro(rs.getString("descricaoparametro"));
			itemGrafico.setQuantidadeAlunos(rs.getString("qtdAluno"));
			itemGrafico.setCorGrafico(rs.getString("corgrafico"));
			itemGrafico.setCorLetraGrafico(rs.getString("corletragrafico"));
			itemGrafico.setOrigemGrafico(origemGrafico);
			itemGrafico.setGraficoAproveitamentoAssuntoPBLVO(grafico);
			if (!Uteis.isAtributoPreenchido(grafico.getSerieGraficoPizzaOutraTurmaEmJavaScript())) {
				grafico.setSerieGraficoPizzaOutraTurmaEmJavaScript(itemGrafico.getSerieGraficoPizza());
			} else {
				grafico.setSerieGraficoPizzaOutraTurmaEmJavaScript(grafico.getSerieGraficoPizzaOutraTurmaEmJavaScript() + ", " + itemGrafico.getSerieGraficoPizza());
			}
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private SqlRowSet consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoBarra(MonitorConhecimentoPBLVO obj, Integer codigoAssunto, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception {

		StringBuilder sb = new StringBuilder(" select codigo_assunto, nome_assunto ,   codigoparametro ,  ");
		sb.append(" descricaoparametro,  corgrafico, corletragrafico,  categoria, sum(qtdAluno) as qtdAluno  ");
		sb.append(" from (  ");		
		sb.append(" select codigo_assunto, nome_assunto , ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.codigo as codigoparametro , itemparametrosmonitoramentoavaliacaoonline.descricaoparametro as descricaoparametro,  ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.corgrafico as corgrafico, itemparametrosmonitoramentoavaliacaoonline.corletragrafico as corletragrafico, ");
		sb.append(" cupre_titulo as categoria,  ");
		sb.append(" avaliado,  ");
		sb.append(" case when itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= t.nota and percentualAcertosAte >= t.nota  then 1 else 0 end  as qtdAluno     ");
		sb.append(" from (   ");
		sb.append(" select turma.configuracaoead, gecta.avaliado ,   ");
		sb.append(" case when ta.codigo is not null then ta.codigo else 0 end as codigo_assunto, ta.nome as nome_assunto,  ");
		sb.append(" cupre.titulo as cupre_titulo, ");
		sb.append(" case when gecta.nota is not null then gecta.nota else 0 end nota  ");
		sb.append(" from gestaoeventoconteudoturma   gect  ");
		sb.append(" inner join turma on turma.codigo = gect.turma ");
		sb.append(" inner join gestaoeventoconteudoturmaavaliacaopbl gecta on gecta.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional ");
		sb.append(" inner join conteudounidadepagina cup on cupre.conteudounidadepagina = cup.codigo  ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo  ");		
		sb.append(" left join temaassunto ta on ta.codigo = uc.temaassunto ");
		sb.append(" Where gecta.tipoavaliacao = 'RESULTADO_FINAL' ");	
		sb.append(" and gecta.situacao = 'REALIZADO' ");
		sb.append(" and gect.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
		sb.append(" and gect.conteudo = ").append(obj.getConteudoVO().getCodigo());
		sb.append(" and gect.ano = '").append(obj.getAno()).append("' ");
		sb.append(" and gect.semestre = '").append(obj.getSemestre()).append("' ");
		if(!isOutraTurma){
			sb.append(" and gect.turma = ").append(obj.getTurmaVO().getCodigo());	
		}else{
			sb.append(" and gect.turma != ").append(obj.getTurmaVO().getCodigo());
		}
		if(codigoAssunto != null &&  codigoAssunto != 0){
			sb.append(" and ta.codigo = ").append(codigoAssunto);	
		} else if(codigoAssunto != null &&  codigoAssunto == 0){
			sb.append(" and ta.codigo is null ");
		}
		
		sb.append(" ) as t ");
		sb.append(" inner join configuracaoead on configuracaoead.codigo = t.configuracaoead ");
		sb.append(" inner join parametrosmonitoramentoavaliacaoonline on parametrosmonitoramentoavaliacaoonline.codigo = configuracaoead.parametrosmonitoramentoavaliacaoonline ");
		sb.append(" inner join itemparametrosmonitoramentoavaliacaoonline on itemparametrosmonitoramentoavaliacaoonline.parametrosmonitoramentoavaliacaoonline = parametrosmonitoramentoavaliacaoonline.codigo ");
		
		sb.append(" group by codigo_assunto, nome_assunto , itemparametrosmonitoramentoavaliacaoonline.codigo, itemparametrosmonitoramentoavaliacaoonline.descricaoparametro, ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.corgrafico, itemparametrosmonitoramentoavaliacaoonline.corletragrafico, categoria, ");
		sb.append(" t.nota, t.avaliado ");
		sb.append(" ) as tt  ");
		sb.append(" group by codigo_assunto, nome_assunto , codigoparametro, descricaoparametro,    ");
		sb.append(" corgrafico, corletragrafico, categoria  ");
		sb.append(" order by codigo_assunto, categoria , descricaoparametro  ");

		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void montarDadosRapidoGraficoBarra(SqlRowSet rs, MonitorConhecimentoPBLVO obj, String origemGrafico, UsuarioVO usuario) throws Exception {
		
		while (rs.next()) {
			GraficoAproveitamentoAssuntoPBLVO grafico = consultarGraficoAproveitamentoAssuntoPBLVO(rs.getInt("codigo_assunto"), obj);
			if (grafico.getCodigoTemaAssunto() == null) {
				grafico.setCodigoTemaAssunto(rs.getInt("codigo_assunto"));
				grafico.setAssunto(rs.getString("nome_assunto"));
				grafico.setMonitorConhecimentoPBLVO(obj);
			}
			GraficoAproveitamentoAssuntoItemPBLVO itemGrafico = consultarGraficoBarraAproveitamentoAssuntoItemPBLVO(rs.getInt("codigoparametro"), grafico);
			if (!Uteis.isAtributoPreenchido(itemGrafico.getCodigoParametro())) {
				itemGrafico.setCodigoParametro(rs.getInt("codigoparametro"));
				itemGrafico.setDescricaoParametro(rs.getString("descricaoparametro"));
				itemGrafico.setCorGrafico(rs.getString("corgrafico"));
				itemGrafico.setCorLetraGrafico(rs.getString("corletragrafico"));
				itemGrafico.setOrigemGrafico(origemGrafico);
				itemGrafico.setGraficoAproveitamentoAssuntoPBLVO(grafico);
			}
			if(itemGrafico.getQuantidadeAlunos().isEmpty()){
				itemGrafico.setQuantidadeAlunos(rs.getString("qtdAluno"));	
			}else{
				itemGrafico.setQuantidadeAlunos(itemGrafico.getQuantidadeAlunos() +", "+ rs.getString("qtdAluno"));
			}
			if(!Uteis.isAtributoPreenchido(itemGrafico.getCategoriaGrafico())){
				itemGrafico.setCategoriaGrafico("'"+rs.getString("categoria")+"'");	
			}else if(!itemGrafico.getCategoriaGrafico().contains(rs.getString("categoria"))){
				itemGrafico.setCategoriaGrafico(itemGrafico.getCategoriaGrafico() +", '"+ rs.getString("categoria")+"'");
			}
			addGraficoAproveitamentoAssuntoItemPBLVO(itemGrafico, grafico);
			addGraficoAproveitamentoAssuntoPBLVO(grafico, obj);
		}		
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private GraficoAproveitamentoAssuntoPBLVO montarDadosRapidoGraficoBarraPorGraficoAproveitamento(SqlRowSet rs,  String origemGrafico, UsuarioVO usuario) throws Exception {
		GraficoAproveitamentoAssuntoPBLVO graficoBarra = new GraficoAproveitamentoAssuntoPBLVO();
		while (rs.next()) {
			GraficoAproveitamentoAssuntoItemPBLVO itemGrafico = consultarGraficoBarraAproveitamentoAssuntoItemPBLVO(rs.getInt("codigoparametro"), graficoBarra);
			if (!Uteis.isAtributoPreenchido(itemGrafico.getCodigoParametro())) {
				itemGrafico.setCodigoParametro(rs.getInt("codigoparametro"));
				itemGrafico.setDescricaoParametro(rs.getString("descricaoparametro"));
				itemGrafico.setCorGrafico(rs.getString("corgrafico"));
				itemGrafico.setCorLetraGrafico(rs.getString("corletragrafico"));
				itemGrafico.setOrigemGrafico(origemGrafico);
				itemGrafico.setOrigemGrafico(origemGrafico);
				itemGrafico.setGraficoAproveitamentoAssuntoPBLVO(graficoBarra);
			}
			if(itemGrafico.getQuantidadeAlunos().isEmpty()){
				itemGrafico.setQuantidadeAlunos(rs.getString("qtdAluno"));	
			}else{
				itemGrafico.setQuantidadeAlunos(itemGrafico.getQuantidadeAlunos() +", "+ rs.getString("qtdAluno"));
			}
			if(!Uteis.isAtributoPreenchido(itemGrafico.getCategoriaGrafico())){
				itemGrafico.setCategoriaGrafico("'"+rs.getString("categoria")+"'");	
			}else if(!itemGrafico.getCategoriaGrafico().contains(rs.getString("categoria"))){
				itemGrafico.setCategoriaGrafico(itemGrafico.getCategoriaGrafico() +", '"+ rs.getString("categoria")+"'");
			}
			addGraficoAproveitamentoAssuntoItemPBLVO(itemGrafico, graficoBarra);
		}
		return graficoBarra;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private GraficoAproveitamentoAssuntoPBLVO consultarGraficoAproveitamentoAssuntoPBLVO(Integer codigo, MonitorConhecimentoPBLVO monitor) {
		for (GraficoAproveitamentoAssuntoPBLVO objExistente : monitor.getListaGraficoAproveitamentoAssuntoPBLVOs()) {
			if (objExistente.getCodigoTemaAssunto().equals(codigo)) {
				return objExistente;
			}
		}
		return new GraficoAproveitamentoAssuntoPBLVO();
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void addGraficoAproveitamentoAssuntoPBLVO(GraficoAproveitamentoAssuntoPBLVO grafico, MonitorConhecimentoPBLVO monitor) {
		int index = 0;
		for (GraficoAproveitamentoAssuntoPBLVO objExistente : monitor.getListaGraficoAproveitamentoAssuntoPBLVOs()) {
			if (objExistente.getCodigoTemaAssunto().equals(grafico.getCodigoTemaAssunto())) {
				monitor.getListaGraficoAproveitamentoAssuntoPBLVOs().set(index, grafico);
				return;
			}
			index++;
		}
		monitor.getListaGraficoAproveitamentoAssuntoPBLVOs().add(grafico);
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private GraficoAproveitamentoAssuntoItemPBLVO consultarGraficoBarraAproveitamentoAssuntoItemPBLVO(Integer codigo, GraficoAproveitamentoAssuntoPBLVO grafico) {
		for (GraficoAproveitamentoAssuntoItemPBLVO objExistente : grafico.getGraficoItemBarra()) {
			if (objExistente.getCodigoParametro().equals(codigo)) {
				return objExistente;
			}
		}
		return new GraficoAproveitamentoAssuntoItemPBLVO();
	}
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void addGraficoAproveitamentoAssuntoItemPBLVO(GraficoAproveitamentoAssuntoItemPBLVO graficoBarra, GraficoAproveitamentoAssuntoPBLVO grafico) {
		int index = 0;		
		for (GraficoAproveitamentoAssuntoItemPBLVO objExistente : grafico.getGraficoItemBarra()) {
			if (objExistente.getCodigoParametro().equals(graficoBarra.getCodigoParametro())) {
				grafico.getGraficoItemBarra().set(index, graficoBarra);
				return;
			}
			index++;
		}
		grafico.getGraficoItemBarra().add(graficoBarra);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private SqlRowSet consultarGestaoEventoContudoTurmaAvaliacaoPorGraficoBarraPorTipoNota(MonitorConhecimentoPBLVO obj, Integer codigoAssunto, boolean isOutraTurma, UsuarioVO usuarioLogado) throws Exception {

		StringBuilder sb = new StringBuilder(" select codigo_assunto, nome_assunto ,   codigoparametro ,   descricaoparametro,  corgrafico, corletragrafico, categoria, sum(qtdAluno) as qtdAluno ");
		sb.append(" from (  ");		
		sb.append(" select codigo_assunto, nome_assunto ,  itemparametrosmonitoramentoavaliacaoonline.codigo as codigoparametro ,  ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.descricaoparametro as descricaoparametro,   ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.corgrafico as corgrafico,  ");
		sb.append(" itemparametrosmonitoramentoavaliacaoonline.corletragrafico as corletragrafico,    ");
		sb.append(" avaliado,    ");
		sb.append(" tipoavaliacao as categoria,  ");
		sb.append(" avaliado,  ");
		sb.append(" case when itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= tt.media and percentualAcertosAte >= tt.media  then 1 else 0 end  as qtdAluno ");
		sb.append(" from (   ");
		sb.append(" select configuracaoead , avaliado , codigo_assunto , nome_assunto , ");
		sb.append(" (case when tipoavaliacao = 'AUTO_AVALIACAO' then 'Auto Aval.' when tipoavaliacao = 'ALUNO_AVALIA_ALUNO' then 'Aluno Aval. Aluno' else 'Prof. Aval. Aluno' end) as tipoavaliacao , ");
		sb.append(" (case when tipoavaliacao in ('AUTO_AVALIACAO' , 'PROFESSOR_AVALIA_ALUNO') then sum(nota)/count(codigo_pbl) else sum(alunoAvalAluno)/count(codigo_pbl) end)::numeric(12,2)as media");
		sb.append(" from (  ");
		sb.append(" select turma.configuracaoead, gecta.avaliado ,    ");
		sb.append(" case when ta.codigo is not null then ta.codigo else 0 end as codigo_assunto,  ");
		sb.append(" ta.nome as nome_assunto,   ");
		sb.append(" gecta.tipoavaliacao , ");
		sb.append(" cupre.codigo as codigo_pbl, ");
		sb.append(" count(gecta.avaliador) as qtdAvaliador, ");
		sb.append(" sum(case when notaconceitoavaliacaopbl is not null then (  ");
		sb.append(" select notacorrespondente from notaconceitoavaliacaopbl  ");
		sb.append(" where notaconceitoavaliacaopbl.codigo = notaconceitoavaliacaopbl and notaconceitoavaliacaopbl.conteudounidadepaginarecursoeducacional = cupre.codigo  ");
		sb.append(" and notaconceitoavaliacaopbl.tipoavaliacao = gecta.tipoavaliacao) ");
		sb.append(" when gecta.nota is not null then gecta.nota else 0 end) as nota, ");
		sb.append(" (sum(case when notaconceitoavaliacaopbl is not null then (  ");
		sb.append(" select notacorrespondente from notaconceitoavaliacaopbl  ");
		sb.append(" where notaconceitoavaliacaopbl.codigo = notaconceitoavaliacaopbl and notaconceitoavaliacaopbl.conteudounidadepaginarecursoeducacional = cupre.codigo  ");
		sb.append(" and notaconceitoavaliacaopbl.tipoavaliacao = gecta.tipoavaliacao) ");
		sb.append(" when gecta.nota is not null then gecta.nota else 0 end) / (count(gecta.avaliador)))::numeric(12,2) as alunoAvalAluno ");
		sb.append(" from gestaoeventoconteudoturma   gect  ");
		sb.append(" inner join turma on turma.codigo = gect.turma ");
		sb.append(" inner join gestaoeventoconteudoturmaavaliacaopbl gecta on gecta.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional ");
		sb.append(" inner join conteudounidadepagina cup on cupre.conteudounidadepagina = cup.codigo  ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo  ");		
		sb.append(" left join temaassunto ta on ta.codigo = uc.temaassunto ");
		sb.append(" Where gecta.tipoavaliacao in ('AUTO_AVALIACAO', 'ALUNO_AVALIA_ALUNO', 'PROFESSOR_AVALIA_ALUNO') ");		
		sb.append(" and gecta.situacao = 'REALIZADO' ");		
		sb.append(" and gect.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
		sb.append(" and gect.conteudo = ").append(obj.getConteudoVO().getCodigo());
		sb.append(" and gect.ano = '").append(obj.getAno()).append("' ");
		sb.append(" and gect.semestre = '").append(obj.getSemestre()).append("' ");
		if(!isOutraTurma){
			sb.append(" and gect.turma = ").append(obj.getTurmaVO().getCodigo());	
		}else{
			sb.append(" and gect.turma != ").append(obj.getTurmaVO().getCodigo());
		}
		if(codigoAssunto != null &&  codigoAssunto != 0){
			sb.append(" and ta.codigo = ").append(codigoAssunto);	
		} else if(codigoAssunto != null &&  codigoAssunto == 0){
			sb.append(" and ta.codigo is null ");
		}
		
		sb.append(" group by turma.configuracaoead, gecta.avaliado,  ta.codigo, ta.nome, gecta.tipoavaliacao, cupre.codigo  ");
		sb.append(" ) as t ");
		sb.append(" group by configuracaoead , avaliado , codigo_assunto , nome_assunto , tipoavaliacao  ");
		sb.append(" ) as tt  ");
		sb.append(" inner join configuracaoead on configuracaoead.codigo = tt.configuracaoead ");
		sb.append(" inner join parametrosmonitoramentoavaliacaoonline on parametrosmonitoramentoavaliacaoonline.codigo = configuracaoead.parametrosmonitoramentoavaliacaoonline ");
		sb.append(" inner join itemparametrosmonitoramentoavaliacaoonline on itemparametrosmonitoramentoavaliacaoonline.parametrosmonitoramentoavaliacaoonline = parametrosmonitoramentoavaliacaoonline.codigo ");
		sb.append(" group by tt.codigo_assunto, tt.nome_assunto , itemparametrosmonitoramentoavaliacaoonline.codigo, itemparametrosmonitoramentoavaliacaoonline.descricaoparametro,  itemparametrosmonitoramentoavaliacaoonline.corgrafico, itemparametrosmonitoramentoavaliacaoonline.corletragrafico, tt.avaliado , tt.tipoavaliacao, tt.media ");		
		sb.append(" ) as ttt  ");
		sb.append(" group by codigo_assunto, nome_assunto , codigoparametro, descricaoparametro,  corgrafico, corletragrafico  , categoria  ");
		sb.append(" order by codigo_assunto,   categoria , descricaoparametro   ");

		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<MatriculaPeriodoTurmaDisciplinaVO> montarAlunosGestaoEventoContudoTurmaAvaliacao(MonitorConhecimentoPBLVO obj, String origem, String cupre, Integer codigoparametro, Integer temaAssunto) throws Exception {
		boolean isOutraTurma = false;
		SqlRowSet rs = null;
		List<MatriculaPeriodoTurmaDisciplinaVO> lista = new ArrayList<>();
		if(origem.equals("PIZZA") || origem.equals("BARRA")){
			rs = consultarAlunosGestaoEventoContudoTurmaAvaliacaoPorGraficoPizzaOrBarra(obj, codigoparametro, temaAssunto, origem, cupre, isOutraTurma);	
		}else if(origem.equals("BARRA_TIPO_NOTA")){
			rs = consultarAlunosGestaoEventoContudoTurmaAvaliacaoPorGraficoTipoNota(obj, codigoparametro, temaAssunto, cupre, isOutraTurma);
		}else if(origem.equals("PIZZA_OUTRA_TURMA") || origem.equals("BARRA_OUTRA_TURMA")){
			isOutraTurma = true;
			rs = consultarAlunosGestaoEventoContudoTurmaAvaliacaoPorGraficoPizzaOrBarra(obj, codigoparametro, temaAssunto, origem, cupre, isOutraTurma);
		}
		
		while (rs.next()) {
			MatriculaPeriodoTurmaDisciplinaVO mat = new MatriculaPeriodoTurmaDisciplinaVO();
			mat.getMatriculaObjetoVO().setMatricula(rs.getString("matricula"));
			mat.getMatriculaObjetoVO().getAluno().setNome(rs.getString("nome"));
			lista.add(mat);
		}
		return lista;
	}
	
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private SqlRowSet consultarAlunosGestaoEventoContudoTurmaAvaliacaoPorGraficoPizzaOrBarra(MonitorConhecimentoPBLVO obj, Integer codigoparametro, Integer temaAssunto, String origem, String cupre, boolean isOutraTurma) throws Exception {

		StringBuilder sb = new StringBuilder(" select matriculaperiodoturmadisciplina.matricula, pessoa.nome from ( ");
		sb.append(" select turma.configuracaoead, gecta.matriculaperiodoturmadisciplinaavaliado, uc.temaassunto, ");
		if(origem.equals("PIZZA") || origem.equals("PIZZA_OUTRA_TURMA")){
			sb.append(" (sum(case  when gecta.nota is not null then gecta.nota else 0 end) / count(gecta.gestaoeventoconteudoturma))::numeric(12,2) as media  ");	
		}else{
			sb.append(" (case when gecta.nota is not null then gecta.nota else 0 end) as media  ");	
		}
		sb.append(" from gestaoeventoconteudoturma   gect  ");
		sb.append(" inner join turma on turma.codigo = gect.turma ");
		sb.append(" inner join gestaoeventoconteudoturmaavaliacaopbl gecta on gecta.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional ");
		sb.append(" inner join conteudounidadepagina cup on cupre.conteudounidadepagina = cup.codigo  ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo  ");
		sb.append(" where gecta.tipoavaliacao = 'RESULTADO_FINAL' ");
		sb.append(" and gecta.situacao = 'REALIZADO' ");
		sb.append(" and gect.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
		sb.append(" and gect.conteudo = ").append(obj.getConteudoVO().getCodigo());
		sb.append(" and gect.ano = '").append(obj.getAno()).append("' ");
		sb.append(" and gect.semestre = '").append(obj.getSemestre()).append("' ");
		if(!isOutraTurma){
			sb.append(" and gect.turma = ").append(obj.getTurmaVO().getCodigo());	
		}else{
			sb.append(" and gect.turma != ").append(obj.getTurmaVO().getCodigo());
		}		
		if(temaAssunto != null &&  temaAssunto != 0){
			sb.append(" and uc.temaassunto = ").append(temaAssunto);	
		} else if(temaAssunto != null &&  temaAssunto == 0){
			sb.append(" and uc.temaassunto is null ");
		}
		if(Uteis.isAtributoPreenchido(cupre)){
			sb.append(" and cupre.titulo =").append(cupre).append(" ");
		}
		if(origem.equals("PIZZA") || origem.equals("PIZZA_OUTRA_TURMA")){
			sb.append(" group by turma.configuracaoead, gecta.matriculaperiodoturmadisciplinaavaliado , uc.temaassunto   ");	
		}else {
			sb.append(" group by turma.configuracaoead, gecta.matriculaperiodoturmadisciplinaavaliado , uc.temaassunto,  gecta.nota  ");	
		}
		
		sb.append(" ) as t ");
		sb.append(" inner join configuracaoead on configuracaoead.codigo = t.configuracaoead ");
		sb.append(" inner join parametrosmonitoramentoavaliacaoonline on parametrosmonitoramentoavaliacaoonline.codigo = configuracaoead.parametrosmonitoramentoavaliacaoonline ");
		sb.append(" inner join itemparametrosmonitoramentoavaliacaoonline on itemparametrosmonitoramentoavaliacaoonline.parametrosmonitoramentoavaliacaoonline = parametrosmonitoramentoavaliacaoonline.codigo ");
		sb.append(" inner join matriculaperiodoturmadisciplina on    matriculaperiodoturmadisciplina.codigo = matriculaperiodoturmadisciplinaavaliado ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno  ");
		sb.append(" where itemparametrosmonitoramentoavaliacaoonline.codigo =   ").append(codigoparametro);
		sb.append(" and itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= t.media and percentualAcertosAte >= t.media  ");
		sb.append(" group by itemparametrosmonitoramentoavaliacaoonline.codigo,  matriculaperiodoturmadisciplina.matricula , pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());		
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private SqlRowSet consultarAlunosGestaoEventoContudoTurmaAvaliacaoPorGraficoTipoNota(MonitorConhecimentoPBLVO obj, Integer codigoparametro, Integer temaAssunto, String cupre, boolean isOutraTurma) throws Exception {

		StringBuilder sb = new StringBuilder(" select matriculaperiodoturmadisciplina.matricula, pessoa.nome from ( ");
		sb.append(" select configuracaoead , matriculaperiodoturmadisciplinaavaliado , ");
		sb.append(" (case when tipoavaliacao = 'AUTO_AVALIACAO' then 'Auto Aval.' when tipoavaliacao = 'ALUNO_AVALIA_ALUNO' then 'Aluno Aval. Aluno' else 'Prof. Aval. Aluno' end) as tipoavaliacao , ");
		sb.append(" (case when tipoavaliacao in ('AUTO_AVALIACAO' , 'PROFESSOR_AVALIA_ALUNO') then sum(nota)/count(codigo_pbl) else sum(alunoAvalAluno)/count(codigo_pbl) end)::numeric(12,2)as media");
		sb.append(" from (  ");
	
		sb.append(" select turma.configuracaoead, gecta.matriculaperiodoturmadisciplinaavaliado, uc.temaassunto,  ");
		sb.append(" gecta.tipoavaliacao , ");
		sb.append(" cupre.codigo as codigo_pbl, ");
		sb.append(" count(gecta.avaliador) as qtdAvaliador, ");
		
		sb.append(" sum(case when notaconceitoavaliacaopbl is not null then (  ");
		sb.append(" select notacorrespondente from notaconceitoavaliacaopbl  ");
		sb.append(" where notaconceitoavaliacaopbl.codigo = notaconceitoavaliacaopbl and notaconceitoavaliacaopbl.conteudounidadepaginarecursoeducacional = cupre.codigo  ");
		sb.append(" and notaconceitoavaliacaopbl.tipoavaliacao = gecta.tipoavaliacao) ");
		sb.append(" when gecta.nota is not null then gecta.nota else 0 end) as nota, ");
		
		sb.append(" (sum(case when notaconceitoavaliacaopbl is not null then (  ");
		sb.append(" select notacorrespondente from notaconceitoavaliacaopbl  ");
		sb.append(" where notaconceitoavaliacaopbl.codigo = notaconceitoavaliacaopbl and notaconceitoavaliacaopbl.conteudounidadepaginarecursoeducacional = cupre.codigo  ");
		sb.append(" and notaconceitoavaliacaopbl.tipoavaliacao = gecta.tipoavaliacao) ");
		sb.append(" when gecta.nota is not null then gecta.nota else 0 end) / (count(gecta.avaliador)))::numeric(12,2) as alunoAvalAluno ");
		
		sb.append(" from gestaoeventoconteudoturma   gect  ");
		sb.append(" inner join turma on turma.codigo = gect.turma ");
		sb.append(" inner join gestaoeventoconteudoturmaavaliacaopbl gecta on gecta.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional ");
		sb.append(" inner join conteudounidadepagina cup on cupre.conteudounidadepagina = cup.codigo  ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo  ");
		sb.append(" Where gecta.tipoavaliacao in ('AUTO_AVALIACAO', 'ALUNO_AVALIA_ALUNO', 'PROFESSOR_AVALIA_ALUNO') ");		
		sb.append(" and gecta.situacao = 'REALIZADO' ");		
		sb.append(" and gect.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
		sb.append(" and gect.conteudo = ").append(obj.getConteudoVO().getCodigo());
		sb.append(" and gect.ano = '").append(obj.getAno()).append("' ");
		sb.append(" and gect.semestre = '").append(obj.getSemestre()).append("' ");
		if(!isOutraTurma){
			sb.append(" and gect.turma = ").append(obj.getTurmaVO().getCodigo());	
		}else{
			sb.append(" and gect.turma != ").append(obj.getTurmaVO().getCodigo());
		}
		if(temaAssunto != null &&  temaAssunto != 0){
			sb.append(" and uc.temaassunto = ").append(temaAssunto);	
		} else if(temaAssunto != null &&  temaAssunto == 0){
			sb.append(" and uc.temaassunto is null ");
		}		
		sb.append(" group by turma.configuracaoead, gecta.matriculaperiodoturmadisciplinaavaliado, uc.temaassunto,  gecta.tipoavaliacao, cupre.codigo  ");
		sb.append(" ) as t ");
		sb.append(" group by configuracaoead , matriculaperiodoturmadisciplinaavaliado , tipoavaliacao  ");
		sb.append(" ) as tt  ");
		sb.append(" inner join configuracaoead on configuracaoead.codigo = tt.configuracaoead ");
		sb.append(" inner join parametrosmonitoramentoavaliacaoonline on parametrosmonitoramentoavaliacaoonline.codigo = configuracaoead.parametrosmonitoramentoavaliacaoonline ");
		sb.append(" inner join itemparametrosmonitoramentoavaliacaoonline on itemparametrosmonitoramentoavaliacaoonline.parametrosmonitoramentoavaliacaoonline = parametrosmonitoramentoavaliacaoonline.codigo ");
		sb.append(" inner join matriculaperiodoturmadisciplina on    matriculaperiodoturmadisciplina.codigo = matriculaperiodoturmadisciplinaavaliado ");
		sb.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno  ");
		sb.append(" where itemparametrosmonitoramentoavaliacaoonline.codigo =   ").append(codigoparametro);
		if(Uteis.isAtributoPreenchido(cupre)){
			sb.append(" and tipoavaliacao =").append(cupre).append(" ");
		}
		sb.append(" and itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= tt.media and percentualAcertosAte >= tt.media  ");
		sb.append(" group by itemparametrosmonitoramentoavaliacaoonline.codigo,  matriculaperiodoturmadisciplina.matricula , pessoa.nome ");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());		
	}
	

}
