/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.sad;

import java.awt.Color;
/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ajax4jsf.util.HtmlColor;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSacado;

public class NivelGraficoDWVO implements Serializable {

    public String msg;
    public String nomeEntidade;
    private Integer unidadeEnsino;
    private Integer departamento;
    private Integer funcionario;
    private Integer funcionarioFavorecido;
    private Integer bancoFavorecido;
    private Integer fornecedorFavorecido;
    private Integer categoriaDespesa;
    private String nivelEntidade;
    private String tipoFavorecido;
    private Integer numeroNivel;
    private String corLegenda;
    private DefaultPieDataset grafico;
    private List<LegendaGraficoVO> legendaGraficoVOs;
    public static final long serialVersionUID = 1L;

    public NivelGraficoDWVO() {
        this.nomeEntidade = "Home";
        this.unidadeEnsino = 0;
        this.departamento = 0;
        this.funcionario = 0;
        this.funcionarioFavorecido = 0;
        this.bancoFavorecido = 0;
        this.fornecedorFavorecido = 0;
        this.nivelEntidade = "";
        this.numeroNivel = 1;
        this.msg = "";
        this.categoriaDespesa = 0;
        corLegenda = "";
        this.setTipoFavorecido("");
        setLegendaGraficoVOs(new ArrayList<LegendaGraficoVO>(0));
    }

    public String getCorLegenda() {
        if (corLegenda == null) {
            corLegenda = "";
        }
        return corLegenda;
    }

    public void setCorLegenda(String corLegenda) {
        this.corLegenda = corLegenda;
    }

    public void alimentarGrafico(SqlRowSet rs, String nivelConsulta) throws Exception {
        setGrafico(new DefaultPieDataset());
        setCorLegenda("");
        setLegendaGraficoVOs(new ArrayList<LegendaGraficoVO>(0));
        Color color = Color.GREEN;
        Integer index = 1;
        while (rs.next()) {
            //if (rs.getDouble("valor") > 0.0) {
            String entidade = rs.getString("entidade");
            if (getNivelEntidade().equals("FA")) {
                entidade = TipoSacado.getDescricao(entidade);
            }
            if (getNivelEntidade().equals("FU")) {
                entidade = Uteis.getNomeResumidoPessoa(entidade);
            }
            getGrafico().setValue(index.toString(), rs.getDouble("valor"));
            LegendaGraficoVO legenda = new LegendaGraficoVO(rs.getInt("codigoEntidade"), entidade, nivelConsulta, Uteis.arrendondarForcando2CadasDecimais(rs.getDouble("valor")));
            color = Uteis.gerarCorAleatoria(color);
            while (getCorLegenda().contains(HtmlColor.encodeRGB(color))) {
                color = Uteis.gerarCorAleatoria(color);
            }
            legenda.setCor(HtmlColor.encodeRGB(color));
            if (getCorLegenda().isEmpty()) {
                setCorLegenda(HtmlColor.encodeRGB(color));
            } else {
                setCorLegenda(getCorLegenda() + ", " + HtmlColor.encodeRGB(color));
            }
            getLegendaGraficoVOs().add(legenda);
            //}
            index++;
        }
        if (getLegendaGraficoVOs().isEmpty()) {
            setGrafico(null);
        }
    }

    public boolean getApresentar() {
        if (grafico != null && !grafico.getKeys().isEmpty()) {
            return true;
        }
        return false;
    }

    public String getNivelEntidade_Apresentar() {
        if (getNivelEntidade().equals("")) {
            return "Home";
        }
        if (getNivelEntidade().equals("UE")) {
            return "Unidades de Ensino";
        }
        if (getNivelEntidade().equals("DE")) {
            return "Departamentos";
        }
        if (getNivelEntidade().equals("FA")) {
            return "Favorecidos";
        }
        if (getNivelEntidade().equals("FU")) {
            return "Funcionários";
        }
        if (getNivelEntidade().equals("FUF")) {
            return "Funcionários";
        }
        if (getNivelEntidade().equals("FOF")) {
            return "Forcenedor";
        }
        if (getNivelEntidade().equals("BAF")) {
            return "Banco";
        }
        if (getNivelEntidade().equals("CD")) {
            return "Categorias de Despesas";
        }
        return "";
    }

    public NivelGraficoDWVO getVoltarNivel1(Integer categoriaDespesa) {
        NivelGraficoDWVO obj = new NivelGraficoDWVO();
        obj.unidadeEnsino = unidadeEnsino;
        obj.departamento = departamento;
        obj.categoriaDespesa = categoriaDespesa;
        obj.funcionario = funcionario;
        obj.funcionarioFavorecido = funcionarioFavorecido;
        obj.bancoFavorecido = bancoFavorecido;
        obj.fornecedorFavorecido = fornecedorFavorecido;
        obj.setNomeEntidade(nomeEntidade);
        obj.setNivelEntidade(nivelEntidade);
        obj.numeroNivel = numeroNivel;
        return obj;
    }

    public NivelGraficoDWVO getVoltarNivel2(Integer departamento, Integer funcionario, Integer funcionarioFavorecido,
            Integer bancoFavorecido, Integer fornecedorFavorecido, String tipoFavorecido) {
        NivelGraficoDWVO obj = new NivelGraficoDWVO();
        obj.unidadeEnsino = unidadeEnsino;
        obj.departamento = departamento;
        obj.categoriaDespesa = categoriaDespesa;
        obj.funcionario = funcionario;
        obj.setNomeEntidade(nomeEntidade);
        obj.setNivelEntidade(nivelEntidade);
        obj.numeroNivel = numeroNivel;
        obj.funcionarioFavorecido = funcionarioFavorecido;
        obj.bancoFavorecido = bancoFavorecido;
        obj.fornecedorFavorecido = fornecedorFavorecido;
        obj.tipoFavorecido = tipoFavorecido;
        return obj;
    }

    public String getDefinirNivelVoltar() {
        if (nivelEntidade.equals("CD") && categoriaDespesa == 0) {
            return "UE";
        } else if (nivelEntidade.equals("CD") && categoriaDespesa > 0) {
            return "CD";
        }
        if (nivelEntidade.equals("DE") && departamento == 0) {
            return "UE";
        }
        if (nivelEntidade.equals("FU") && funcionario == 0) {
            return "DE";
        }
        if (nivelEntidade.equals("FA")) {
            return "UE";
        }
//        if (nivelEntidade.equals("FUF") && fornecedorFavorecido == 0) {
//            return "FA";
//        }
//        if (nivelEntidade.equals("BAF") && bancoFavorecido == 0) {
//            return "FA";
//        }
//        if (nivelEntidade.equals("FOF") && fornecedorFavorecido == 0) {
//            return "FA";
//        }

        return "";
    }

    public NivelGraficoDWVO getNovoNivel1(LegendaGraficoVO legenda, Integer categoriaDespesa, boolean incrementar, String origem) {
        NivelGraficoDWVO obj = new NivelGraficoDWVO();
        if (legenda.getNivel().equals("")) {
            obj.unidadeEnsino = 0;
            obj.departamento = 0;
            obj.categoriaDespesa = 0;
            obj.funcionario = 0;
            obj.funcionarioFavorecido = 0;
            obj.bancoFavorecido = 0;
            obj.fornecedorFavorecido = 0;
            obj.setTipoFavorecido("");
            obj.setNivelEntidade("UE");
        } else if (legenda.getNivel().equals("UE")) {
            obj.unidadeEnsino = legenda.getCodigo();
            obj.departamento = 0;
            obj.categoriaDespesa = 0;
            obj.funcionario = 0;
            obj.funcionarioFavorecido = 0;
            obj.bancoFavorecido = 0;
            obj.fornecedorFavorecido = 0;
            obj.setTipoFavorecido("");
            if (origem.equals("RE")) {
                obj.setNivelEntidade("DE");
            }
            if (origem.equals("PA")) {
                obj.setNivelEntidade("FA");
            }
        } else if (legenda.getNivel().equals("DE")) {
            obj.unidadeEnsino = unidadeEnsino;
            obj.departamento = legenda.getCodigo();
            obj.categoriaDespesa = categoriaDespesa;
            obj.funcionario = 0;
            obj.funcionarioFavorecido = 0;
            obj.bancoFavorecido = 0;
            obj.fornecedorFavorecido = 0;
            obj.setTipoFavorecido("");
            obj.setNivelEntidade("FU");
        } else if (legenda.getNivel().equals("FU")) {
            obj.unidadeEnsino = unidadeEnsino;
            obj.departamento = departamento;
            obj.categoriaDespesa = categoriaDespesa;
            obj.funcionario = legenda.getCodigo();
            obj.funcionarioFavorecido = 0;
            obj.bancoFavorecido = 0;
            obj.fornecedorFavorecido = 0;
            obj.setTipoFavorecido("");
            obj.setNivelEntidade("FU");
        } else if (legenda.getNivel().equals("CD")) {
            obj.setNivelEntidade(nivelEntidade);
            obj.unidadeEnsino = unidadeEnsino;
            obj.departamento = departamento;
            obj.categoriaDespesa = categoriaDespesa;
            if (origem.equals("RE")) {
                obj.funcionario = funcionario;
            } else if (origem.equals("PA")) {
                obj.funcionario = 0;
            }
            obj.funcionarioFavorecido = funcionarioFavorecido;
            obj.bancoFavorecido = bancoFavorecido;
            obj.fornecedorFavorecido = fornecedorFavorecido;
            obj.tipoFavorecido = tipoFavorecido;
        } else if (legenda.getNivel().equals("FA")) {
            obj.unidadeEnsino = unidadeEnsino;
            obj.departamento = departamento;
            obj.categoriaDespesa = categoriaDespesa;
            obj.funcionario = 0;
            obj.funcionarioFavorecido = 0;
            obj.bancoFavorecido = 0;
            obj.fornecedorFavorecido = 0;
            obj.setTipoFavorecido("");
            if (TipoSacado.BANCO.getDescricao().equals(legenda.getNome())) {
                obj.setTipoFavorecido(TipoSacado.BANCO.getValor());
            }
            if (TipoSacado.FORNECEDOR.getDescricao().equals(legenda.getNome())) {
                obj.setTipoFavorecido(TipoSacado.FORNECEDOR.getValor());
            }
            if (TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao().equals(legenda.getNome())) {
                obj.setTipoFavorecido(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
            }
            obj.setNivelEntidade("FA");
        }
//        }else if (legenda.getNivel().equals("BAF")) {
//            obj.unidadeEnsino = unidadeEnsino;
//            obj.departamento = departamento;
//            obj.categoriaDespesa = categoriaDespesa;
//            obj.funcionario = 0;
//            obj.funcionarioFavorecido = 0;
//            obj.bancoFavorecido = legenda.getCodigo();
//            obj.fornecedorFavorecido = 0;
//            obj.tipoFavorecido = tipoFavorecido;
//            obj.setNivelEntidade("BAF");
//        }else if (legenda.getNivel().equals("FOF")) {
//            obj.unidadeEnsino = unidadeEnsino;
//            obj.departamento = departamento;
//            obj.categoriaDespesa = categoriaDespesa;
//            obj.funcionario = 0;
//            obj.funcionarioFavorecido = 0;
//            obj.bancoFavorecido = 0;
//            obj.fornecedorFavorecido = legenda.getCodigo();
//            obj.tipoFavorecido = tipoFavorecido;
//            obj.setNivelEntidade("FOF");
//        }else if (legenda.getNivel().equals("FUF")) {
//            obj.unidadeEnsino = unidadeEnsino;
//            obj.departamento = departamento;
//            obj.categoriaDespesa = categoriaDespesa;
//            obj.funcionario = 0;
//            obj.funcionarioFavorecido = legenda.getCodigo();
//            obj.bancoFavorecido = 0;
//            obj.fornecedorFavorecido = 0;
//            obj.tipoFavorecido = tipoFavorecido;
//            obj.setNivelEntidade("FUF");
//        }
        if (incrementar) {
            obj.setNomeEntidade(legenda.getNome());
            obj.numeroNivel = numeroNivel + 1;
        } else {
            obj.setNomeEntidade(nomeEntidade);
            obj.numeroNivel = numeroNivel;
        }
        return obj;
    }

    public NivelGraficoDWVO getNovoNivel2(LegendaGraficoVO legenda, Integer departamento,
            Integer funcionario, Integer funcionarioFavorecido,
            Integer bancoFavorecido, Integer fornecedorFavorecido, String tipoFavorecido,
            boolean incrementar, String origem) {
        NivelGraficoDWVO obj = new NivelGraficoDWVO();
        if (legenda.getNivel().equals("")) {
            obj.unidadeEnsino = 0;
            obj.departamento = 0;
            obj.categoriaDespesa = 0;
            obj.funcionario = 0;
            obj.funcionarioFavorecido = 0;
            obj.bancoFavorecido = 0;
            obj.fornecedorFavorecido = 0;
            obj.tipoFavorecido = "";
            obj.setNivelEntidade("UE");
        } else if (legenda.getNivel().equals("UE")) {
            obj.unidadeEnsino = legenda.getCodigo();
            obj.departamento = 0;
            obj.categoriaDespesa = 0;
            obj.funcionario = 0;
            obj.funcionarioFavorecido = 0;
            obj.bancoFavorecido = 0;
            obj.fornecedorFavorecido = 0;
            obj.tipoFavorecido = "";
            obj.setNivelEntidade("CD");
        } else if (legenda.getNivel().equals("CD")) {
            obj.unidadeEnsino = unidadeEnsino;
            obj.departamento = departamento;
            obj.funcionario = funcionario;
            obj.funcionarioFavorecido = funcionarioFavorecido;
            obj.bancoFavorecido = bancoFavorecido;
            obj.fornecedorFavorecido = fornecedorFavorecido;
            obj.tipoFavorecido = tipoFavorecido;
            obj.categoriaDespesa = legenda.getCodigo();
            obj.setNivelEntidade("CD");
        } else {
            obj.unidadeEnsino = unidadeEnsino;
            obj.departamento = departamento;
            obj.funcionario = funcionario;
            obj.funcionarioFavorecido = funcionarioFavorecido;
            obj.bancoFavorecido = bancoFavorecido;
            obj.fornecedorFavorecido = fornecedorFavorecido;
            obj.tipoFavorecido = tipoFavorecido;
            obj.categoriaDespesa = categoriaDespesa;
            obj.setNivelEntidade("CD");
        }

        if (incrementar) {
            obj.setNomeEntidade(legenda.getNome());
            obj.numeroNivel = numeroNivel + 1;
        } else {
            obj.setNomeEntidade(nomeEntidade);
            obj.numeroNivel = numeroNivel;
        }
        return obj;
    }

    public Integer getElement() {
        return getLegendaGraficoVOs().size();
    }

    public Integer getColumn() {
        if (getLegendaGraficoVOs().size() > 2) {
            return 2;
        }
        return getLegendaGraficoVOs().size();
    }

    public List<LegendaGraficoVO> getLegendaGraficoVOs() {
        if(legendaGraficoVOs == null){
            legendaGraficoVOs = new ArrayList<LegendaGraficoVO>(0);
        }
        return legendaGraficoVOs;
    }

    public void setLegendaGraficoVOs(List<LegendaGraficoVO> legendaGraficoVOs) {
        this.legendaGraficoVOs = legendaGraficoVOs;
    }

    public DefaultPieDataset getGrafico() {
        return grafico;
    }

    public void setGrafico(DefaultPieDataset grafico) {
        this.grafico = grafico;
    }

    public Integer getCategoriaDespesa() {
        return categoriaDespesa;
    }

    public void setCategoriaDespesa(Integer categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public Integer getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Integer funcionario) {
        this.funcionario = funcionario;
    }

    public String getNivelEntidade() {
        return nivelEntidade;
    }

    public void setNivelEntidade(String nivelEntidade) {
        this.nivelEntidade = nivelEntidade;
    }

    public Integer getNumeroNivel() {
        return numeroNivel;
    }

    public void setNumeroNivel(Integer numeroNivel) {
        this.numeroNivel = numeroNivel;
    }

    public String getNomeEntidade() {
        return nomeEntidade;
    }

    public void setNomeEntidade(String nomeEntidade) {
        this.nomeEntidade = nomeEntidade;
    }

    public Integer getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public String getMsg() {
        if (msg == null) {
            msg = "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getBancoFavorecido() {
        return bancoFavorecido;
    }

    public void setBancoFavorecido(Integer bancoFavorecido) {
        this.bancoFavorecido = bancoFavorecido;
    }

    public Integer getFornecedorFavorecido() {
        return fornecedorFavorecido;
    }

    public void setFornecedorFavorecido(Integer fornecedorFavorecido) {
        this.fornecedorFavorecido = fornecedorFavorecido;
    }

    public Integer getFuncionarioFavorecido() {
        return funcionarioFavorecido;
    }

    public void setFuncionarioFavorecido(Integer funcionarioFavorecido) {
        this.funcionarioFavorecido = funcionarioFavorecido;
    }

    public String getTipoFavorecido() {
        if (tipoFavorecido == null) {
            tipoFavorecido = "";
        }
        return tipoFavorecido;
    }

    public void setTipoFavorecido(String tipoFavorecido) {
        this.tipoFavorecido = tipoFavorecido;
    }
}
