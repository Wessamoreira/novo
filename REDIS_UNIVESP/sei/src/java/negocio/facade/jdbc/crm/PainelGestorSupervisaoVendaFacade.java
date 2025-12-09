/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.crm;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialArray;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.crm.DadosGeraisCampanhaPorFuncionarioVO;
import negocio.comuns.crm.EstatisticaMotivoInsucessoVO;
import negocio.comuns.crm.EstatisticaPipelineVO;
import negocio.comuns.crm.EstatisticaPorEtapaCampanhaVO;
import negocio.comuns.crm.EstatisticaPorEtapaCampanhaXMLVO;
import negocio.comuns.crm.PainelGestorSupervisaoVendaVO;
import negocio.comuns.crm.enumerador.NivelExperienciaCargoEnum;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.TipoMetaEnum;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.crm.PainelGestorSupervisaoVendaInterfaceFacade;

/**
 *
 * @author RODRIGO
 */
@Service
@Lazy
public class PainelGestorSupervisaoVendaFacade extends ControleAcesso implements PainelGestorSupervisaoVendaInterfaceFacade {

    public PainelGestorSupervisaoVendaVO executarGeracaoDadosEstatisticaCampanha(Integer campanha, Integer funcionario, Date dataInicio, Date dataTermino) throws Exception {
        PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO = new PainelGestorSupervisaoVendaVO();
        painelGestorSupervisaoVendaVO.setCampanha(campanha);
        if (funcionario != null && funcionario > 0) {
            painelGestorSupervisaoVendaVO.setVendedor(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(funcionario, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
        }
        painelGestorSupervisaoVendaVO.setEstatisticaPorEtapaCampanhaSucessoVOs(executarGeracaoEstatisticaPorEtapaCampanha(campanha, funcionario, SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO, dataInicio, dataTermino));
        painelGestorSupervisaoVendaVO.setEstatisticaPorEtapaCampanhaInsucessoVOs(executarGeracaoEstatisticaPorEtapaCampanha(campanha, funcionario, SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO, dataInicio, dataTermino));
        painelGestorSupervisaoVendaVO.setEstatisticaPorEtapaCampanhaNaoFinalizadoVOs(executarGeracaoEstatisticaPorEtapaCampanha(campanha, funcionario, SituacaoProspectPipelineControleEnum.NENHUM, dataInicio, dataTermino));
        painelGestorSupervisaoVendaVO.setEstatisticaMotivoInsucessoVOs(consultarEstatisticaMotivoInsucesso(campanha, funcionario, dataInicio, dataTermino));
        consultarEstatisticaGeralCampanha(painelGestorSupervisaoVendaVO, campanha, funcionario, dataInicio, dataTermino);
        painelGestorSupervisaoVendaVO.setDadosGeraisCampanhaPorFuncionarioVOs(consultarDadosEstatisticoMetaPorVendedor(campanha, funcionario));
        executarCriacaoGraficoPipeline(painelGestorSupervisaoVendaVO, campanha, funcionario);
        return painelGestorSupervisaoVendaVO;
    }

    public void executarCriacaoGraficoPipeline(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO, Integer campanha, Integer vendedor) throws Exception {
        StringBuilder sb = new StringBuilder("");
        SqlRowSet rs = null;

        try {
            sb.append(" select count(distinct interacaoworkflow.prospect) qtdeProspect, SituacaoProspectPipeline.nome , SituacaoProspectPipeline.corFundo, SituacaoProspectPipeline.corTexto, efetivacaoVendahistorica,");
            sb.append(" (count(interacaoworkflow.prospect)*efetivacaoVendahistorica) / 100 as perspectiva");
            sb.append(" from campanha");
            sb.append(" inner join workflow  on workflow.codigo = campanha.workflow  ");
            sb.append(" inner join etapaworkflow  on etapaworkflow.workflow = workflow.codigo ");
            sb.append(" inner join SituacaoProspectWorkflow on SituacaoProspectWorkflow.codigo = etapaworkflow.situacaoDefinirProspectFinal");
            sb.append(" inner join SituacaoProspectPipeline on SituacaoProspectPipeline.codigo = SituacaoProspectWorkflow.situacaoProspectPipeline");
            sb.append(" left join interacaoworkflow  on etapaworkflow.codigo = interacaoworkflow.etapaworkflow and interacaoworkflow.campanha = campanha.codigo ");
            sb.append(" where campanha.codigo =  ").append(campanha);
            if (vendedor != null && vendedor > 0) {
                sb.append(" and  interacaoworkflow.responsavel =  ").append(vendedor);
            }
            sb.append(" and SituacaoProspectPipeline.apresentarpipeline is true ");
            sb.append(" group by SituacaoProspectPipeline.corFundo, SituacaoProspectPipeline.corTexto, SituacaoProspectPipeline.nome, efetivacaoVendahistorica");
            sb.append(" order by count(distinct interacaoworkflow.prospect) desc");
            rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            montarDadosPipeline(painelGestorSupervisaoVendaVO, rs);
            executarCriarDesignerGraficoPipeline(painelGestorSupervisaoVendaVO);
        } catch (Exception e) {
            throw e;
        } finally {
            sb = null;
            rs = null;
        }
    }

    private void executarCriarDesignerGraficoPipeline(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO) throws Exception {

        StringBuilder sb = new StringBuilder("<p><div style=\"float:left;width:413px;height: 445px; text-align:center; vertical-align:middle;\">");
        Integer tamanhoPipelineUtilizado = 0;
        Integer tamanhoPipelineUtilizar = 0;
        Integer alturaMaximaTotal = 415;
        Integer alturaMaximaLabel = 20;
        Integer tamanhoMinimoTodasLabel = 0;
        try {
            int x = 1;
            if (painelGestorSupervisaoVendaVO.getQtdeProspectPipeline() > 0) {
                tamanhoMinimoTodasLabel = painelGestorSupervisaoVendaVO.getEstatisticaPipelineVOs().size()*alturaMaximaLabel;
                alturaMaximaTotal -= tamanhoMinimoTodasLabel;
                for (EstatisticaPipelineVO estatisticaPipelineVO : painelGestorSupervisaoVendaVO.getEstatisticaPipelineVOs()) {
                    
                    if (estatisticaPipelineVO.getQuantidadeProspect() > 0) {
                        if (x == painelGestorSupervisaoVendaVO.getEstatisticaPipelineVOs().size()) {
                            tamanhoPipelineUtilizar = 415 - tamanhoPipelineUtilizado + alturaMaximaLabel;                            
                        } else {
                            tamanhoPipelineUtilizar = Integer.valueOf(alturaMaximaTotal * (estatisticaPipelineVO.getQuantidadeProspect() * 100 / painelGestorSupervisaoVendaVO.getQtdeProspectPipeline()) / 100);                            
                            tamanhoPipelineUtilizar += alturaMaximaLabel;
                        }
                        
                        if(x==1){
                            sb.append(" <div  style=\"width:413px;z-index:10;float:left;");
                            sb.append(" height: ").append(32).append("px;");
                            sb.append(" background-color:").append(estatisticaPipelineVO.getCorFundo()).append(";");                                                    
                            sb.append(" \"");
                            sb.append(" ></div>");
                            tamanhoPipelineUtilizado += 32;
                        }
                        tamanhoPipelineUtilizado += tamanhoPipelineUtilizar;
                        
                        sb.append(" <div  style=\"width:413px;float:left;");
                        sb.append(" height: ").append(tamanhoPipelineUtilizar.intValue()).append("px;");
                        sb.append(" background-color:").append(estatisticaPipelineVO.getCorFundo()).append(";");
                        //sb.append(" padding-top:").append((tamanhoPipelineUtilizar/2)-15).append("px;");   
                        sb.append(" color:").append(estatisticaPipelineVO.getCorTexto()).append(";");
                        sb.append(" text-align: center; vertical-align:middle;font: 9pt 'Trebuchet MS', verdana, arial, helvetica, sans-serif;\"");
                        sb.append(" ><span style=\"text-align: center; vertical-align:middle;font: 9pt 'Trebuchet MS', verdana, arial, helvetica, sans-serif;z-index:0; ");
                                             
                        sb.append(" color:").append(estatisticaPipelineVO.getCorTexto()).append(";\">");
                        sb.append(estatisticaPipelineVO.getQuantidadeProspect()).append(" Prospect(s)<span></div>");
                        estatisticaPipelineVO.setAltura(tamanhoPipelineUtilizar);
                        x++;
                    }
                }
            }
            sb.append("<div style=\"background-image: url('./../../resources/imagens/funil.png');background-repeat:no-repeat;width: 413px;height: 442px;position: absolute;\" ></div>");
            sb.append("</div></p>");
            painelGestorSupervisaoVendaVO.setGraficoFunilPipeline(sb.toString());
        } catch (Exception e) {
            painelGestorSupervisaoVendaVO.setGraficoFunilPipeline("");
            throw e;
        }
    }

    private void montarDadosPipeline(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO, SqlRowSet rs) throws Exception {
        painelGestorSupervisaoVendaVO.getEstatisticaPipelineVOs().clear();
        painelGestorSupervisaoVendaVO.setQtdeProspectPipeline(0);
        EstatisticaPipelineVO estatisticaPipelineVO = null;
        while (rs.next()) {
            estatisticaPipelineVO = new EstatisticaPipelineVO();
            estatisticaPipelineVO.setNomeSituacao(rs.getString("nome"));
            estatisticaPipelineVO.setEfetivacaoVendaHistorica(rs.getDouble("efetivacaoVendahistorica"));
            estatisticaPipelineVO.setQuantidadeProspect(rs.getInt("qtdeProspect"));
            estatisticaPipelineVO.setProjecaoEfetivacao(rs.getInt("perspectiva"));
            estatisticaPipelineVO.setCorFundo(rs.getString("corFundo"));
            estatisticaPipelineVO.setCorTexto(rs.getString("corTexto"));
            painelGestorSupervisaoVendaVO.setQtdeProspectPipeline(painelGestorSupervisaoVendaVO.getQtdeProspectPipeline() + rs.getInt("qtdeProspect"));
            painelGestorSupervisaoVendaVO.getEstatisticaPipelineVOs().add(estatisticaPipelineVO);
        }

    }

    public List<DadosGeraisCampanhaPorFuncionarioVO> consultarDadosEstatisticoMetaPorVendedor(Integer campanha, Integer vendedor) throws Exception {
        StringBuilder sb = new StringBuilder("");
        SqlRowSet rs = null;
        List<DadosGeraisCampanhaPorFuncionarioVO> dadosGeraisCampanhaPorFuncionarioVOs = null;
        try {
            sb.append(" select campanha.periodoInicio::DATE as periodoInicio, campanha.periodoFim::DATE as periodoFim, unidadeEnsino.cidade, Usuario.codigo as codigoUsuario, Usuario.nome nomeUsuario, funcionariocargo.nivelExperiencia, Meta.tipoMeta, Meta.considerarSabado,");
            sb.append(" MetaItem.quantidadeContatos, ");
            sb.append(" (select count(distinct prospect) from interacaoworkflow  where");
            sb.append(" campanha = campanha.codigo and usuario.codigo = interacaoworkflow.responsavel) as quantidadeTotalContatos,       ");
            sb.append(" (select count(distinct prospect) from interacaoworkflow  where");
            sb.append(" interacaoworkflow.campanha = campanha.codigo and usuario.codigo = interacaoworkflow.responsavel");
            sb.append(" and dataTermino::date = current_date) as quantidadeContatoDiaRealizado,");
            sb.append(" MetaItem.quantidadeFinalizadoSucesso,       ");
            sb.append(" (select count(distinct prospect) from interacaoworkflow");
            sb.append(" inner join etapaworkflow  on etapaworkflow.codigo = interacaoworkflow.etapaworkflow");
            sb.append(" inner join SituacaoProspectWorkflow on SituacaoProspectWorkflow.codigo = etapaworkflow.situacaoDefinirProspectFinal");
            sb.append(" inner join SituacaoProspectPipeline on SituacaoProspectPipeline.codigo = SituacaoProspectWorkflow.situacaoProspectPipeline ");
            sb.append(" where  interacaoworkflow.campanha = campanha.codigo and usuario.codigo = interacaoworkflow.responsavel       ");
            sb.append(" and controle='FINALIZADO_SUCESSO') as quantidadeTotalContatosSucesso,              ");
            sb.append(" (select count(distinct prospect) from interacaoworkflow");
            sb.append(" inner join etapaworkflow  on etapaworkflow.codigo = interacaoworkflow.etapaworkflow");
            sb.append(" inner join SituacaoProspectWorkflow on SituacaoProspectWorkflow.codigo = etapaworkflow.situacaoDefinirProspectFinal");
            sb.append(" inner join SituacaoProspectPipeline on SituacaoProspectPipeline.codigo = SituacaoProspectWorkflow.situacaoProspectPipeline ");
            sb.append(" where  interacaoworkflow.campanha = campanha.codigo and usuario.codigo = interacaoworkflow.responsavel");
            sb.append(" and dataTermino::date = current_date");
            sb.append(" and controle='FINALIZADO_SUCESSO') as quantidadeTotalContatosSucessoDiaAtual,       ");
            sb.append(" MetaItem.quantidadecaptacaoprospect,        ");
            sb.append(" (select count(registroentradaprospects.codigo) from registroentradaprospects  ");
            sb.append(" where registroentradaprospects.vendedor = usuario.codigo and registroentradaprospects.campanha = campanha.codigo)");
            sb.append(" quantidadeNovosProspectCaptadoTotalCampanha ,");
            sb.append(" (select count(registroentradaprospects.codigo) from registroentradaprospects  ");
            sb.append(" where registroentradaprospects.vendedor = usuario.codigo and registroentradaprospects.campanha = campanha.codigo");
            sb.append(" and dataindicacaoprospect::date = current_date)");
            sb.append(" quantidadeNovosProspectCaptadoDiaAtualCampanha ");
            sb.append(" from campanha");
            sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = campanha.unidadeEnsino");
            sb.append(" inner join campanhacolaborador on campanhacolaborador.campanha = campanha.codigo");
            sb.append(" inner join funcionariocargo on funcionariocargo.codigo = campanhacolaborador.funcionariocargo");
            sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo");
            sb.append(" inner join usuario on usuario.pessoa = funcionario.pessoa");
            sb.append(" inner join Meta on meta.codigo = campanha.meta");
            sb.append(" inner join MetaItem on meta.codigo = metaItem.meta and (");
            sb.append(" (funcionariocargo.nivelExperiencia is null and MetaItem.padrao is true) ");
            sb.append(" or (MetaItem.nivelExperiencia  = funcionariocargo.nivelExperiencia)");
//            sb.append(" or (MetaItem.padrao is true))");
            sb.append(" )");
            sb.append(" where campanha.codigo = ").append(campanha);
            if (vendedor != null && vendedor > 0) {
                sb.append(" and usuario.codigo = ").append(vendedor);
            }
            sb.append(" order by usuario.nome ");
            rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            dadosGeraisCampanhaPorFuncionarioVOs = new ArrayList<DadosGeraisCampanhaPorFuncionarioVO>(0);
            TipoMetaEnum tipoMetaEnum = null;
            boolean considerarSabado = false;
            Integer diasConsiderar = null;
            Integer diasUteis = null;
            Integer diasUteisDecorridos = null;
            while (rs.next()) {
                if (diasUteis == null) {
                    if (!rs.getBoolean("considerarSabado")) {
                        diasUteis = getFacadeFactory().getFeriadoFacade().consultarNrDiasUteis(rs.getDate("periodoInicio"), rs.getDate("periodoFim"), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM);
                        if (rs.getDate("periodoInicio").compareTo(new Date()) >= 0) {
                            diasUteisDecorridos = 0;
                        } else if (rs.getDate("periodoFim").compareTo(new Date()) < 0) {
                            diasUteisDecorridos = diasUteis;
                        } else {
                            diasUteisDecorridos = getFacadeFactory().getFeriadoFacade().consultarNrDiasUteis(rs.getDate("periodoInicio"), new Date(), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM);
                        }
                    } else {
                        diasUteis = getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisConsiderandoSabado(rs.getDate("periodoInicio"), rs.getDate("periodoFim"), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM);
                        if (rs.getDate("periodoInicio").compareTo(new Date()) >= 0) {
                            diasUteisDecorridos = 0;
                        } else if (rs.getDate("periodoFim").compareTo(new Date()) < 0) {
                            diasUteisDecorridos = diasUteis;
                        } else {
                            diasUteisDecorridos = getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisConsiderandoSabado(rs.getDate("periodoInicio"), new Date(), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM);
                        }
                    }
                }
                if (diasConsiderar == null) {
                    tipoMetaEnum = TipoMetaEnum.valueOf(rs.getString("tipoMeta"));
                    considerarSabado = rs.getBoolean("considerarSabado");
                    if (tipoMetaEnum.equals(TipoMetaEnum.MENSAL) && considerarSabado) {
                        diasConsiderar = 22;
                    } else if (tipoMetaEnum.equals(TipoMetaEnum.MENSAL)) {
                        diasConsiderar = 20;
                    } else if (tipoMetaEnum.equals(TipoMetaEnum.SEMANAL) && considerarSabado) {
                        diasConsiderar = 6;
                    } else if (tipoMetaEnum.equals(TipoMetaEnum.SEMANAL)) {
                        diasConsiderar = 5;
                    } else {
                        diasConsiderar = 1;
                    }
                }
                dadosGeraisCampanhaPorFuncionarioVOs.add(montarDadosDadosGeraisCampanhaPorFuncionarioVO(rs, diasUteis, diasUteisDecorridos, diasConsiderar));
            }
            return dadosGeraisCampanhaPorFuncionarioVOs;
        } catch (Exception e) {
            throw e;
        } finally {
            sb = null;
            rs = null;
        }
    }

    public DadosGeraisCampanhaPorFuncionarioVO montarDadosDadosGeraisCampanhaPorFuncionarioVO(SqlRowSet rs, Integer diasUteis, Integer diasUteisDecorridos, Integer diasConsiderar) {
        DadosGeraisCampanhaPorFuncionarioVO dadosGeraisCampanhaPorFuncionarioVO = new DadosGeraisCampanhaPorFuncionarioVO();
        dadosGeraisCampanhaPorFuncionarioVO.setCodigoUsuario(rs.getInt("codigoUsuario"));
        dadosGeraisCampanhaPorFuncionarioVO.setNomeUsuario(rs.getString("nomeUsuario"));
        if(rs.getString("nivelExperiencia") != null){
           dadosGeraisCampanhaPorFuncionarioVO.setNivelExperiencia(NivelExperienciaCargoEnum.valueOf(rs.getString("nivelExperiencia")));
        }else{
            dadosGeraisCampanhaPorFuncionarioVO.setNivelExperiencia(NivelExperienciaCargoEnum.NENHUM);
        }
        dadosGeraisCampanhaPorFuncionarioVO.setDiaCampanha(diasUteisDecorridos + "/" + diasUteis);

        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeContatoDia(rs.getInt("quantidadeContatos") / diasConsiderar);
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeContatoDiaAtual(rs.getInt("quantidadeContatoDiaRealizado"));
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeTotalContato(rs.getInt("quantidadeContatoDiaRealizado"));
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeTotalContatoEsperado((rs.getInt("quantidadeContatos") / diasConsiderar) * diasUteisDecorridos);

        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeContatoSucessoDia(rs.getInt("quantidadeFinalizadoSucesso") / diasConsiderar);
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeContatoSucessoDiaAtual(rs.getInt("quantidadeTotalContatosSucessoDiaAtual"));
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeTotalContatoSucesso(rs.getInt("quantidadeTotalContatosSucesso"));
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeTotalContatoSucessoEsperado((rs.getInt("quantidadeFinalizadoSucesso") / diasConsiderar) * diasUteisDecorridos);

        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeCaptacaoProspectDia(rs.getInt("quantidadecaptacaoprospect") / diasConsiderar);
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeCaptacaoProspectDiaAtual(rs.getInt("quantidadeNovosProspectCaptadoDiaAtualCampanha"));
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeTotalCaptacaoProspect(rs.getInt("quantidadeNovosProspectCaptadoTotalCampanha"));
        dadosGeraisCampanhaPorFuncionarioVO.setQuantidadeTotalCaptacaoProspectEsperado((rs.getInt("quantidadecaptacaoprospect") / diasConsiderar) * diasUteisDecorridos);

        return dadosGeraisCampanhaPorFuncionarioVO;
    }

    public void consultarEstatisticaoPerfilProspect(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO, Integer campanha, Integer funcionario, Date dataInicio, Date dataTermino, SituacaoProspectPipelineControleEnum situacaoProspectPipelineControleEnum) throws Exception {
        StringBuilder sb = new StringBuilder("");
        SqlRowSet rs = null;
        try {
            sb.append(" (select 'SEXO' as tipo, count(distinct prospects.codigo) as qtde, prospects.sexo as condicao from interacaoworkflow  ");
            sb.append(" inner join prospects on prospects.codigo = interacaoworkflow.prospect");
            sb.append(" where interacaoworkflow.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "interacaoworkflow.dataTermino", true));
            if (situacaoProspectPipelineControleEnum != null) {
                sb.append(" and interacaoworkflow.prospect in (");
                sb.append(" select iwf.prospect from interacaoworkflow iwf");
                sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf.etapaworkflow");
                sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal");
                sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline");
                sb.append(" where iwf.campanha = ").append(campanha);
                if (funcionario != null && funcionario > 0) {
                    sb.append(" and iwf.responsavel = ").append(funcionario);
                }
                sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf.dataTermino", true));
                if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.NENHUM)
                        || situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                    sb.append(" and iwf.prospect  not in (Select iwf2.prospect from interacaoworkflow iwf2");
                    sb.append(" inner join etapaworkflow ewf2 on ewf2.codigo = iwf2.etapaworkflow");
                    sb.append(" inner join SituacaoProspectWorkflow spwf2 on spwf2.codigo = ewf2.situacaoDefinirProspectFinal");
                    sb.append(" inner join SituacaoProspectPipeline sppl2 on sppl2.codigo = spwf2.situacaoProspectPipeline");
                    sb.append(" where iwf2.campanha = ").append(campanha);
                    if (funcionario != null && funcionario > 0) {
                        sb.append(" and iwf2.responsavel = ").append(funcionario);
                    }
                    sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf2.dataTermino", true));
                    sb.append(" and sppl2.controle in ('").append(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO).append("', ");
                    if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                        sb.append(" '").append(SituacaoProspectPipelineControleEnum.NENHUM).append("', ");
                    }
                    sb.append(" '").append(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO).append("') ");
                    sb.append(" ) ");
                } else {
                    sb.append(" and sppl.controle = '").append(situacaoProspectPipelineControleEnum.name()).append("' ");
                }
                sb.append(" )");
            }
            sb.append(" group by prospects.sexo");
            sb.append(" order by prospects.sexo)");
            sb.append(" union all");
            sb.append(" (select 'RENDA', count(distinct prospects.codigo), prospects.renda as condicao from interacaoworkflow  ");
            sb.append(" inner join prospects on prospects.codigo = interacaoworkflow.prospect");
            sb.append(" where interacaoworkflow.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "interacaoworkflow.dataTermino", true));
            if (situacaoProspectPipelineControleEnum != null) {
                sb.append(" and interacaoworkflow.prospect in (");
                sb.append(" select iwf.prospect from interacaoworkflow iwf");
                sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf.etapaworkflow");
                sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal");
                sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline");
                sb.append(" where iwf.campanha = ").append(campanha);
                if (funcionario != null && funcionario > 0) {
                    sb.append(" and iwf.responsavel = ").append(funcionario);
                }
                sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf.dataTermino", true));
                if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.NENHUM)
                        || situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                    sb.append(" and iwf.prospect  not in (Select iwf2.prospect from interacaoworkflow iwf2");
                    sb.append(" inner join etapaworkflow ewf2 on ewf2.codigo = iwf2.etapaworkflow");
                    sb.append(" inner join SituacaoProspectWorkflow spwf2 on spwf2.codigo = ewf2.situacaoDefinirProspectFinal");
                    sb.append(" inner join SituacaoProspectPipeline sppl2 on sppl2.codigo = spwf2.situacaoProspectPipeline");
                    sb.append(" where iwf2.campanha = ").append(campanha);
                    if (funcionario != null && funcionario > 0) {
                        sb.append(" and iwf2.responsavel = ").append(funcionario);
                    }
                    sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf2.dataTermino", true));
                    sb.append(" and sppl2.controle in ('").append(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO).append("', ");
                    if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                        sb.append(" '").append(SituacaoProspectPipelineControleEnum.NENHUM).append("', ");
                    }
                    sb.append(" '").append(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO).append("') ");
                    sb.append(" ) ");
                } else {
                    sb.append(" and sppl.controle = '").append(situacaoProspectPipelineControleEnum.name()).append("' ");
                }
                sb.append(" )");
            }
            sb.append(" group by prospects.renda");
            sb.append(" order by prospects.renda");
            sb.append(" )");
            sb.append(" union all");
            sb.append(" (select 'FORMACAO_ACADEMICA', count(distinct prospects.codigo), prospects.formacaoAcademica as condicao from interacaoworkflow  ");
            sb.append(" inner join prospects on prospects.codigo = interacaoworkflow.prospect");
            sb.append(" where interacaoworkflow.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "interacaoworkflow.dataTermino", true));
            if (situacaoProspectPipelineControleEnum != null) {
                sb.append(" and interacaoworkflow.prospect in (");
                sb.append(" select iwf.prospect from interacaoworkflow iwf");
                sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf.etapaworkflow");
                sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal");
                sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline");
                sb.append(" where iwf.campanha = ").append(campanha);
                if (funcionario != null && funcionario > 0) {
                    sb.append(" and iwf.responsavel = ").append(funcionario);
                }
                sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf.dataTermino", true));
                if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.NENHUM)
                        || situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                    sb.append(" and iwf.prospect  not in (Select iwf2.prospect from interacaoworkflow iwf2");
                    sb.append(" inner join etapaworkflow ewf2 on ewf2.codigo = iwf2.etapaworkflow");
                    sb.append(" inner join SituacaoProspectWorkflow spwf2 on spwf2.codigo = ewf2.situacaoDefinirProspectFinal");
                    sb.append(" inner join SituacaoProspectPipeline sppl2 on sppl2.codigo = spwf2.situacaoProspectPipeline");
                    sb.append(" where iwf2.campanha = ").append(campanha);
                    if (funcionario != null && funcionario > 0) {
                        sb.append(" and iwf2.responsavel = ").append(funcionario);
                    }
                    sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf2.dataTermino", true));
                    sb.append(" and sppl2.controle in ('").append(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO).append("', ");
                    if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                        sb.append(" '").append(SituacaoProspectPipelineControleEnum.NENHUM).append("', ");
                    }
                    sb.append(" '").append(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO).append("') ");
                    sb.append(" ) ");
                } else {
                    sb.append(" and sppl.controle = '").append(situacaoProspectPipelineControleEnum.name()).append("' ");
                }
                sb.append(" )");
            }
            sb.append(" group by prospects.formacaoAcademica");
            sb.append(" order by prospects.formacaoAcademica)");
            sb.append(" union all");
            sb.append(" (select 'TIPO_EMPRESA', count(distinct prospects.codigo), prospects.tipoempresa as condicao from interacaoworkflow  ");
            sb.append(" inner join prospects on prospects.codigo = interacaoworkflow.prospect");
            sb.append(" where interacaoworkflow.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "interacaoworkflow.dataTermino", true));
            sb.append(" group by prospects.tipoempresa");
            sb.append(" order by prospects.tipoempresa)");
            sb.append(" union all");
            sb.append(" (select 'IDADE', count(distinct prospects.codigo),");
            sb.append(" (((((current_date - prospects.datanascimento)/365)/5)+1)*5)::VARCHAR as condicao from interacaoworkflow  ");
            sb.append(" inner join prospects on prospects.codigo = interacaoworkflow.prospect");
            sb.append(" where interacaoworkflow.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "interacaoworkflow.dataTermino", true));
            if (situacaoProspectPipelineControleEnum != null) {
                sb.append(" and interacaoworkflow.prospect in (");
                sb.append(" select iwf.prospect from interacaoworkflow iwf");
                sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf.etapaworkflow");
                sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal");
                sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline");
                sb.append(" where iwf.campanha = ").append(campanha);
                if (funcionario != null && funcionario > 0) {
                    sb.append(" and iwf.responsavel = ").append(funcionario);
                }
                sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf.dataTermino", true));
                if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.NENHUM)
                        || situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                    sb.append(" and iwf.prospect  not in (Select iwf2.prospect from interacaoworkflow iwf2");
                    sb.append(" inner join etapaworkflow ewf2 on ewf2.codigo = iwf2.etapaworkflow");
                    sb.append(" inner join SituacaoProspectWorkflow spwf2 on spwf2.codigo = ewf2.situacaoDefinirProspectFinal");
                    sb.append(" inner join SituacaoProspectPipeline sppl2 on sppl2.codigo = spwf2.situacaoProspectPipeline");
                    sb.append(" where iwf2.campanha = ").append(campanha);
                    if (funcionario != null && funcionario > 0) {
                        sb.append(" and iwf2.responsavel = ").append(funcionario);
                    }
                    sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf2.dataTermino", true));
                    sb.append(" and sppl2.controle in ('").append(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO).append("', ");
                    if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                        sb.append(" '").append(SituacaoProspectPipelineControleEnum.NENHUM).append("', ");
                    }
                    sb.append(" '").append(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO).append("') ");
                    sb.append(" ) ");
                } else {
                    sb.append(" and sppl.controle = '").append(situacaoProspectPipelineControleEnum.name()).append("' ");
                }
                sb.append(" )");
            }
            sb.append(" group by ((((current_date - prospects.datanascimento)/365)/5)+1)*5");
            sb.append(" order by ((((current_date - prospects.datanascimento)/365)/5)+1)*5)");
            rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            painelGestorSupervisaoVendaVO.setEstatisticaPorFormacaoAcademica(null);
            painelGestorSupervisaoVendaVO.setEstatisticaPorIdade(null);
            painelGestorSupervisaoVendaVO.setEstatisticaPorRenda(null);
            painelGestorSupervisaoVendaVO.setEstatisticaPorSexo(null);
            painelGestorSupervisaoVendaVO.setEstatisticaPorTipoEmpresa(null);
            String condicao;
            while (rs.next()) {
                condicao = rs.getString("condicao");
                if (rs.getString("tipo").equals("SEXO")) {
                    if (condicao == null || condicao.isEmpty() || (!condicao.equals("F") && !condicao.equals("M"))) {
                        condicao = "Não Informado";
                    } else {
                        condicao = (String) Dominios.getSexo().get(condicao);
                    }
                    painelGestorSupervisaoVendaVO.getEstatisticaPorSexoData().put(condicao, rs.getInt("qtde"));
                } else if (rs.getString("tipo").equals("RENDA")) {
                    if (condicao == null || condicao.isEmpty()) {
                        condicao = "Não Informado";
                    } else {
                        condicao = UteisJSF.internacionalizar("enum_RendaProspectEnum_" + condicao);
                    }
                    painelGestorSupervisaoVendaVO.getEstatisticaPorRendaData().put(condicao, rs.getInt("qtde"));
                } else if (rs.getString("tipo").equals("TIPO_EMPRESA")) {
                    if (condicao == null || condicao.isEmpty() || condicao.equals("NENHUM")) {
                        condicao = "Não Informado";
                    } else {
                        condicao = UteisJSF.internacionalizar("enum_TipoEmpresaProspectEnum_" + condicao);
                    }
                    painelGestorSupervisaoVendaVO.getEstatisticaPorTipoEmpresaData().put(condicao, rs.getInt("qtde"));
                } else if (rs.getString("tipo").equals("FORMACAO_ACADEMICA")) {
                    if (condicao == null || condicao.isEmpty() || condicao.equals("NENHUM")) {
                        condicao = "Não Informado";
                    } else {
                        condicao = UteisJSF.internacionalizar("enum_FormacaoAcademicaProspectEnum" + condicao);
                    }
                    painelGestorSupervisaoVendaVO.getEstatisticaPorFormacaoAcademicaData().put(condicao, rs.getInt("qtde"));
                } else if (rs.getString("tipo").equals("IDADE")) {
                    if (condicao == null || condicao.isEmpty()) {
                        condicao = "De 0 a 5 anos";
                    } else {
                        condicao = "De " + (Integer.valueOf(condicao) - 5) + " a " + condicao + " anos";
                    }
                    painelGestorSupervisaoVendaVO.getEstatisticaPorIdadeData().put(condicao, rs.getInt("qtde"));
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            sb = null;
            rs = null;
        }
    }

    public void consultarEstatisticaGeralCampanha(PainelGestorSupervisaoVendaVO painelGestorSupervisaoVendaVO, Integer campanha, Integer funcionario, Date dataInicio, Date dataTermino) throws Exception {
        StringBuilder sb = new StringBuilder("");
        SqlRowSet rs = null;
        try {
            sb.append(" select count(distinct prospect) as quantidadeProspect, ");
            sb.append(" count(interacaoworkflow.codigo) as quantidadeInteracoesProspect, ");
            sb.append(" (sum(dataTermino - dataInicio)/count(interacaoworkflow.codigo))::VARCHAR as tempoConsumoMedio , ");
            sb.append(" case when(max(dataTermino)::DATE = campanha.periodoInicio ) then 1 else max(dataTermino)::DATE - campanha.periodoInicio end as numeroDiaAtualCampanha, ");
            sb.append(" campanha.periodoFim - campanha.periodoInicio as numeroDiaTotalCampanha, ");
            sb.append(" campanha.periodoInicio, ");
            sb.append(" campanha.periodoFim, ");
            sb.append(" meta.considerarSabado, ");
            sb.append(" unidadeensino.cidade, ");
            sb.append(" estatisticaSucesso.totalAtendimentoSucesso, estatisticaSucesso.tempoMedioAtendimentoSucesso::VARCHAR, ");
            sb.append(" estatisticaInsucesso.totalAtendimentoInsucesso, estatisticaInsucesso.tempoMedioAtendimentoInsucesso::VARCHAR, ");
            sb.append(" estatisticaNaoFinalizado.totalAtendimentoNaoFinalizado, ");
            sb.append(" estatisticaNaoFinalizado.tempoMedioAtendimentoNaoFinalizado::VARCHAR        ");
            sb.append(" from interacaoworkflow   ");
            sb.append(" inner join campanha on campanha.codigo =  interacaoworkflow.campanha ");
            sb.append(" inner join unidadeensino on campanha.unidadeensino =  unidadeensino.codigo ");
            sb.append(" inner join meta on campanha.meta =  meta.codigo ");
            sb.append(" left join (select iwf.campanha, count(distinct iwf.prospect) as totalAtendimentoSucesso, sum(dataTermino - dataInicio)/count(distinct iwf.prospect) as tempoMedioAtendimentoSucesso   ");
            sb.append(" from interacaoworkflow iwf  ");
            sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf.etapaworkflow  ");
            sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal  ");
            sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline ");
            sb.append(" where iwf.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and iwf.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "iwf.dataTermino", true));
            sb.append(" and sppl.controle = 'FINALIZADO_SUCESSO' group by iwf.campanha) as estatisticaSucesso on estatisticaSucesso.campanha = campanha.codigo ");
            sb.append(" left join (select iwf.campanha, count(distinct iwf.prospect) as totalAtendimentoInsucesso, sum(dataTermino - dataInicio)/count(distinct iwf.prospect) as tempoMedioAtendimentoInsucesso   ");
            sb.append(" from interacaoworkflow iwf  ");
            sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf.etapaworkflow  ");
            sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal  ");
            sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline ");
            sb.append(" where iwf.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and iwf.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "iwf.dataTermino", true));
            sb.append(" and sppl.controle = 'FINALIZADO_INSUCESSO' group by iwf.campanha) as estatisticaInsucesso on estatisticaInsucesso.campanha = campanha.codigo ");
            sb.append(" left join (select iwf.campanha,count(distinct iwf.prospect) as totalAtendimentoNaoFinalizado, sum(dataTermino - dataInicio)/count(distinct iwf.prospect) as tempoMedioAtendimentoNaoFinalizado   ");
            sb.append(" from interacaoworkflow iwf ");
            sb.append(" where iwf.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and iwf.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "iwf.dataTermino", true));
            sb.append(" and iwf.prospect not in ( ");
            sb.append(" select distinct iwf2.prospect from interacaoworkflow iwf2  ");
            sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf2.etapaworkflow  ");
            sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal  ");
            sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline  ");
            sb.append(" where iwf2.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and iwf2.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "iwf2.dataTermino", true));
            sb.append(" and sppl.controle in ('FINALIZADO_INSUCESSO','FINALIZADO_SUCESSO')  ");
            sb.append(" ) group by iwf.campanha ) as estatisticaNaoFinalizado on estatisticaNaoFinalizado.campanha = campanha.codigo ");
            sb.append(" where interacaoworkflow.campanha =  ").append(campanha);
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel =  ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "interacaoworkflow.dataTermino", true));

            sb.append(" group by campanha.periodoInicio, campanha.periodoFim, unidadeensino.cidade, meta.considerarSabado, estatisticaSucesso.totalAtendimentoSucesso, estatisticaSucesso.tempoMedioAtendimentoSucesso, ");
            sb.append(" estatisticaInsucesso.totalAtendimentoInsucesso, estatisticaInsucesso.tempoMedioAtendimentoInsucesso, ");
            sb.append(" estatisticaNaoFinalizado.totalAtendimentoNaoFinalizado, ");
            sb.append(" estatisticaNaoFinalizado.tempoMedioAtendimentoNaoFinalizado ");

            rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            if (rs.next()) {
                painelGestorSupervisaoVendaVO.setCampanha(campanha);
                painelGestorSupervisaoVendaVO.setDataInicio(rs.getDate("periodoInicio"));
                painelGestorSupervisaoVendaVO.setDataTermino(rs.getDate("periodoFim"));
                
                    if (!rs.getBoolean("considerarSabado")) {
                        painelGestorSupervisaoVendaVO.setNumeroDiaTotalCampanha(getFacadeFactory().getFeriadoFacade().consultarNrDiasUteis(rs.getDate("periodoInicio"), rs.getDate("periodoFim"), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM));
                        if (rs.getDate("periodoInicio").compareTo(new Date()) >= 0) {
                            painelGestorSupervisaoVendaVO.setNumeroDiaAtualCampanha(0);
                        } else if (rs.getDate("periodoFim").compareTo(new Date()) < 0) {
                            painelGestorSupervisaoVendaVO.setNumeroDiaAtualCampanha(painelGestorSupervisaoVendaVO.getNumeroDiaTotalCampanha());
                        } else {
                            painelGestorSupervisaoVendaVO.setNumeroDiaAtualCampanha(getFacadeFactory().getFeriadoFacade().consultarNrDiasUteis(rs.getDate("periodoInicio"), new Date(), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM));
                        }
                    } else {
                        painelGestorSupervisaoVendaVO.setNumeroDiaTotalCampanha(getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisConsiderandoSabado(rs.getDate("periodoInicio"), rs.getDate("periodoFim"), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM));
                        if (rs.getDate("periodoInicio").compareTo(new Date()) >= 0) {
                            painelGestorSupervisaoVendaVO.setNumeroDiaAtualCampanha(0);
                        } else if (rs.getDate("periodoFim").compareTo(new Date()) < 0) {
                            painelGestorSupervisaoVendaVO.setNumeroDiaAtualCampanha(painelGestorSupervisaoVendaVO.getNumeroDiaTotalCampanha());
                        } else {
                            painelGestorSupervisaoVendaVO.setNumeroDiaAtualCampanha(getFacadeFactory().getFeriadoFacade().consultarNrDiasUteisConsiderandoSabado(rs.getDate("periodoInicio"), new Date(), rs.getInt("cidade"), ConsiderarFeriadoEnum.NENHUM));
                        }
                    }
                                                
                painelGestorSupervisaoVendaVO.setQuantidadeInteracoesProspect(rs.getInt("quantidadeInteracoesProspect"));
                painelGestorSupervisaoVendaVO.setQuantidadeProspect(rs.getInt("quantidadeProspect"));
                if (rs.getString("tempoConsumoMedio") != null) {
                    painelGestorSupervisaoVendaVO.setTempoConsumoMedio(rs.getString("tempoConsumoMedio").substring(0, rs.getString("tempoConsumoMedio").indexOf(".")));
                } else {
                    painelGestorSupervisaoVendaVO.setTempoConsumoMedio("00:00:00");
                }
                if (rs.getString("tempoMedioAtendimentoInsucesso") != null) {
                    painelGestorSupervisaoVendaVO.setTempoMedioAtendimentoInsucesso(rs.getString("tempoMedioAtendimentoInsucesso").substring(0, rs.getString("tempoMedioAtendimentoInsucesso").indexOf(".")));
                } else {
                    painelGestorSupervisaoVendaVO.setTempoMedioAtendimentoInsucesso("00:00:00");
                }
                if (rs.getString("tempoMedioAtendimentoSucesso") != null) {
                    painelGestorSupervisaoVendaVO.setTempoMedioAtendimentoSucesso(rs.getString("tempoMedioAtendimentoSucesso").substring(0, rs.getString("tempoMedioAtendimentoSucesso").indexOf(".")));
                } else {
                    painelGestorSupervisaoVendaVO.setTempoMedioAtendimentoSucesso("00:00:00");
                }
                if (rs.getString("tempoMedioAtendimentoNaoFinalizado") != null) {
                    painelGestorSupervisaoVendaVO.setTempoMedioAtendimentoNaoFinalizado(rs.getString("tempoMedioAtendimentoNaoFinalizado").substring(0, rs.getString("tempoMedioAtendimentoNaoFinalizado").indexOf(".")));
                } else {
                    painelGestorSupervisaoVendaVO.setTempoMedioAtendimentoNaoFinalizado("00:00:00");
                }
                painelGestorSupervisaoVendaVO.setTotalAtendimentoSucesso(rs.getInt("totalAtendimentoSucesso"));
                painelGestorSupervisaoVendaVO.setTotalAtendimentoInsucesso(rs.getInt("totalAtendimentoInsucesso"));
                painelGestorSupervisaoVendaVO.setTotalAtendimentoNaoFinalizado(rs.getInt("totalAtendimentoNaoFinalizado"));
                if (painelGestorSupervisaoVendaVO.getTotalAtendimentoSucesso() != null && painelGestorSupervisaoVendaVO.getTotalAtendimentoSucesso() > 0) {
                    painelGestorSupervisaoVendaVO.setTaxaConversaoEmSucesso((painelGestorSupervisaoVendaVO.getTotalAtendimentoSucesso().doubleValue() * 100) / painelGestorSupervisaoVendaVO.getQuantidadeProspect().doubleValue());
                } else {
                    painelGestorSupervisaoVendaVO.setTaxaConversaoEmSucesso(0.0);
                }
                if (painelGestorSupervisaoVendaVO.getTotalAtendimentoInsucesso() != null && painelGestorSupervisaoVendaVO.getTotalAtendimentoInsucesso() > 0) {
                    painelGestorSupervisaoVendaVO.setTaxaConversaoEmInsucesso((painelGestorSupervisaoVendaVO.getTotalAtendimentoInsucesso().doubleValue() * 100) / painelGestorSupervisaoVendaVO.getQuantidadeProspect().doubleValue());
                } else {
                    painelGestorSupervisaoVendaVO.setTaxaConversaoEmInsucesso(0.0);
                }
                if (painelGestorSupervisaoVendaVO.getTotalAtendimentoNaoFinalizado() != null && painelGestorSupervisaoVendaVO.getTotalAtendimentoNaoFinalizado() > 0) {
                    painelGestorSupervisaoVendaVO.setTaxaConversaoNaoFinalizado((painelGestorSupervisaoVendaVO.getTotalAtendimentoNaoFinalizado().doubleValue() * 100)/painelGestorSupervisaoVendaVO.getQuantidadeProspect().doubleValue() );
                } else {
                    painelGestorSupervisaoVendaVO.setTaxaConversaoNaoFinalizado(0.0);
                }

            }
        } catch (Exception e) {
            throw e;
        } finally {
            sb = null;
            rs = null;
        }

    }

    public List<EstatisticaMotivoInsucessoVO> consultarEstatisticaMotivoInsucesso(Integer campanha, Integer funcionario, Date dataInicio, Date dataTermino) throws Exception {
        StringBuilder sb = new StringBuilder("");
        SqlRowSet rs = null;
        List<EstatisticaMotivoInsucessoVO> estatisticaMotivoInsucessoVOs = new ArrayList<EstatisticaMotivoInsucessoVO>(0);
        try {
            sb.append(" select count(interacaoworkflow.codigo) as qtde,MotivoInsucesso.codigo, MotivoInsucesso.descricao  from interacaoworkflow  ");
            sb.append(" inner join MotivoInsucesso on MotivoInsucesso.codigo = interacaoworkflow.MotivoInsucesso ");
            sb.append(" where 1=1 ");
            if (campanha != null && campanha > 0) {
                sb.append(" and interacaoworkflow.campanha = ").append(campanha);
            }
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel = ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "interacaoworkflow.dataTermino", true));
            sb.append(" group by MotivoInsucesso.descricao, MotivoInsucesso.codigo ");
            rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            while (rs.next()) {
                EstatisticaMotivoInsucessoVO estatisticaMotivoInsucessoVO = new EstatisticaMotivoInsucessoVO();
                estatisticaMotivoInsucessoVO.setQuantidadeInsucesso(rs.getInt("qtde"));
                estatisticaMotivoInsucessoVO.getMotivoInsucessoVO().setCodigo(rs.getInt("codigo"));
                estatisticaMotivoInsucessoVO.getMotivoInsucessoVO().setDescricao(rs.getString("descricao"));
                estatisticaMotivoInsucessoVOs.add(estatisticaMotivoInsucessoVO);
            }
            return estatisticaMotivoInsucessoVOs;
        } catch (Exception e) {
            throw e;
        } finally {
            sb = null;
            rs = null;
        }

    }

    public List<EstatisticaPorEtapaCampanhaVO> executarGeracaoEstatisticaPorEtapaCampanha(Integer campanha, Integer funcionario, SituacaoProspectPipelineControleEnum situacaoProspectPipelineControleEnum, Date dataInicio, Date dataTermino) throws Exception {

        StringBuilder sb = new StringBuilder();
        try {
            sb.append(" select etapaworkflow.codigo as codigoEtapa, etapaworkflow.nome as nomeEtapa,");
            sb.append(" etapaworkflow.cor as cor, etapaworkflow.nivelApresentacao as nivelEtapa, ");
            sb.append(" (sum(dataTermino - dataInicio))::varchar as tempoTotalAtendimento, count(distinct interacaoworkflow.prospect) as totalPessoasAtendidas, ");
            sb.append(" (sum(dataTermino - dataInicio)/ count(interacaoworkflow.codigo))::VARCHAR as tempoMedioAtendimento, ");
            sb.append(" array(select ewf.etapaworkflow from etapaworkflowantecedente ewf where ewf.etapaAntecedente = etapaworkflow.codigo ) as etapasSucessoras");
            sb.append(" from Campanha");
            sb.append(" inner join etapaworkflow on etapaworkflow.workflow =   Campanha.workflow");
            sb.append(" inner join SituacaoProspectWorkflow SituacaoProspectWorkflow on SituacaoProspectWorkflow.codigo = etapaworkflow.situacaoDefinirProspectFinal"); 
            sb.append(" inner join SituacaoProspectPipeline SituacaoProspectPipeline on SituacaoProspectPipeline.codigo = SituacaoProspectWorkflow.situacaoProspectPipeline");
            sb.append(" left join interacaoworkflow  on etapaworkflow.codigo = interacaoworkflow.etapaworkflow ");
            if (situacaoProspectPipelineControleEnum != null) {
            	
                sb.append(" and interacaoworkflow.prospect in (");
                sb.append(" select iwf.prospect from interacaoworkflow iwf");
                sb.append(" inner join etapaworkflow ewf on ewf.codigo = iwf.etapaworkflow");
                sb.append(" inner join SituacaoProspectWorkflow spwf on spwf.codigo = ewf.situacaoDefinirProspectFinal");
                sb.append(" inner join SituacaoProspectPipeline sppl on sppl.codigo = spwf.situacaoProspectPipeline");
                sb.append(" where iwf.campanha = ").append(campanha);
                if (funcionario != null && funcionario > 0) {
                    sb.append(" and iwf.responsavel = ").append(funcionario);
                }
                sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf.datatermino", true));
                if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.NENHUM)
                        || situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                    sb.append(" and iwf.prospect  not in (Select iwf2.prospect from interacaoworkflow iwf2");
                    sb.append(" inner join etapaworkflow ewf2 on ewf2.codigo = iwf2.etapaworkflow");
                    sb.append(" inner join SituacaoProspectWorkflow spwf2 on spwf2.codigo = ewf2.situacaoDefinirProspectFinal");
                    sb.append(" inner join SituacaoProspectPipeline sppl2 on sppl2.codigo = spwf2.situacaoProspectPipeline");
                    sb.append(" where iwf2.campanha = ").append(campanha);
                    if (funcionario != null && funcionario > 0) {
                        sb.append(" and iwf2.responsavel = ").append(funcionario);
                    }
                    sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "iwf2.datatermino", true));
                    sb.append(" and sppl2.controle in ('").append(SituacaoProspectPipelineControleEnum.FINALIZADO_INSUCESSO).append("', ");
                    if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                        sb.append(" '").append(SituacaoProspectPipelineControleEnum.NENHUM).append("', ");
                    }
                    sb.append(" '").append(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO).append("') ");
                    sb.append(" ) ");
                } else {
                    sb.append(" and sppl.controle = '").append(situacaoProspectPipelineControleEnum.name()).append("' ");
                }
                sb.append(" )");
                
            }
            if (campanha != null && campanha > 0) {
                sb.append(" and interacaoworkflow.campanha = ").append(campanha);
            }
            if (funcionario != null && funcionario > 0) {
                sb.append(" and interacaoworkflow.responsavel = ").append(funcionario);
            }
            sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataTermino), "interacaoworkflow.dataTermino", true));
            if (campanha != null && campanha > 0) {
                sb.append(" where Campanha.codigo = ").append(campanha);
            }
            if(situacaoProspectPipelineControleEnum != null){
            if (situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.NENHUM) || situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
            	sb.append(" and SituacaoProspectPipeline.controle not in ('FINALIZADO_SUCESSO', 'FINALIZADO_INSUCESSO') ");
            }else if(situacaoProspectPipelineControleEnum.equals(SituacaoProspectPipelineControleEnum.FINALIZADO_SUCESSO)){
            	sb.append(" and SituacaoProspectPipeline.controle <> 'FINALIZADO_INSUCESSO' ");  
            }else{
            	sb.append(" and SituacaoProspectPipeline.controle <> 'FINALIZADO_SUCESSO' ");
            }
            }
            sb.append(" group by interacaoworkflow.campanha,etapaworkflow.codigo, etapaworkflow.nome, etapaworkflow.cor, etapaworkflow.nivelApresentacao");
            sb.append(" order by nivelApresentacao, etapaworkflow.codigo");

            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            return montarDadosEstatisticaPorEtapa(rs, campanha, funcionario, situacaoProspectPipelineControleEnum);
        } catch (Exception e) {
            throw e;
        } finally {
            sb = null;
        }
    }

    public List<EstatisticaPorEtapaCampanhaVO> montarDadosEstatisticaPorEtapa(SqlRowSet rs, Integer campanha, Integer funcionario, SituacaoProspectPipelineControleEnum situacaoProspectPipelineControleEnum) throws Exception {
        List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaVOs = new ArrayList<EstatisticaPorEtapaCampanhaVO>(0);
        EstatisticaPorEtapaCampanhaVO estatisticaPorEtapaCampanhaVO = null;
        while (rs.next()) {
            estatisticaPorEtapaCampanhaVO = new EstatisticaPorEtapaCampanhaVO();
            estatisticaPorEtapaCampanhaVO.setCodigoEtapa(rs.getInt("codigoEtapa"));
            estatisticaPorEtapaCampanhaVO.setNomeEtapa(rs.getString("nomeEtapa"));
            estatisticaPorEtapaCampanhaVO.setCor(rs.getString("cor"));
            estatisticaPorEtapaCampanhaVO.setTotalPessoasAtendidas(rs.getInt("totalPessoasAtendidas"));
            if (rs.getString("tempoTotalAtendimento") != null && rs.getString("tempoTotalAtendimento").contains(".")) {
                estatisticaPorEtapaCampanhaVO.setTempoTotalAtendimento(rs.getString("tempoTotalAtendimento").substring(0, rs.getString("tempoTotalAtendimento").indexOf(".")));
            }
            if (rs.getString("tempoMedioAtendimento") != null && rs.getString("tempoMedioAtendimento").contains(".")) {
                estatisticaPorEtapaCampanhaVO.setTempoMedioAtendimento(rs.getString("tempoMedioAtendimento").substring(0, rs.getString("tempoMedioAtendimento").indexOf(".")));
            }
            estatisticaPorEtapaCampanhaVO.setNivelEtapa(rs.getInt("nivelEtapa"));
            if (rs.getObject("etapasSucessoras") != null) {
                Object[] rst = (Object[]) (((SerialArray) rs.getObject("etapasSucessoras")).getArray());
                estatisticaPorEtapaCampanhaVO.setEtapasSucessoras(new Integer[rst.length]);
                int x = 0;
                for (Object obj : rst) {
                    estatisticaPorEtapaCampanhaVO.getEtapasSucessoras()[x++] = (Integer) obj;
                }
            }
            estatisticaPorEtapaCampanhaVOs.add(estatisticaPorEtapaCampanhaVO);

        }
        //gerarXMLEstatisticaPorEtapa(estatisticaPorEtapaCampanhaVOs,campanha, funcionario, situacaoProspectPipelineControleEnum);
        return estatisticaPorEtapaCampanhaVOs;
    }

    public void gerarXMLEstatisticaPorEtapa(List<EstatisticaPorEtapaCampanhaVO> estatisticaPorEtapaCampanhaVOs,
            Integer campanha, Integer funcionario, SituacaoProspectPipelineControleEnum situacaoProspectPipelineControleEnum) throws Exception {
        FileWriter fw = null;
        JAXBContext context = null;
        Marshaller marshaller = null;
        EstatisticaPorEtapaCampanhaXMLVO estatisticaPorEtapaCampanhaXMLVO = new EstatisticaPorEtapaCampanhaXMLVO();
        try {
            context = JAXBContext.newInstance(EstatisticaPorEtapaCampanhaXMLVO.class);
            marshaller = context.createMarshaller();
            if(funcionario != null){
                fw = new FileWriter("\\\\BANCOS\\repositorio2\\XML/" + campanha + "_" + funcionario + "_" + situacaoProspectPipelineControleEnum.name() + ".xml");
            }else{
                fw = new FileWriter("\\\\BANCOS\\repositorio2\\XML/" + campanha + "_" + situacaoProspectPipelineControleEnum.name() + ".xml");
            }
            estatisticaPorEtapaCampanhaXMLVO.setEstatisticaPorEtapaCampanhaVOs(estatisticaPorEtapaCampanhaVOs);
            marshaller.marshal(estatisticaPorEtapaCampanhaXMLVO, fw);
            fw.flush();
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (fw != null) {
                fw.close();
                fw = null;
            }
            context = null;
            marshaller = null;
            estatisticaPorEtapaCampanhaXMLVO = null;
        }
    }
}
