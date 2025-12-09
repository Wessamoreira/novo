/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

import controle.academico.ConfiguracaoAcademicoControle;
import controle.administrativo.ConfiguracaoGeralSistemaControle;
import controle.ead.ConfiguracaoEADControle;
import controle.financeiro.CfgCustoAdministrativoControle;
import controle.financeiro.ConfiguracaoFinanceiroControle;

/**
 * 
 * @author Diego
 */
public enum ConfiguracoesDominio {

    CONFIGURACAO_GERAL_SISTEMA_CONTROLE("ConfiguracaoGeralSistemaControle", ConfiguracaoGeralSistemaControle.class),
    CFG_CUSTO_ADMINISTRATIVO_CONTROLE("CfgCustoAdministrativoControle", CfgCustoAdministrativoControle.class),
    CONFIGURACAO_FINANCEIRO_CONTROLE("ConfiguracaoFinanceiroControle", ConfiguracaoFinanceiroControle.class),
    CONFIGURACAO_ACADEMICO_CONTROLE("ConfiguracaoAcademicoControle", ConfiguracaoAcademicoControle.class);
    // Pedro: Comentei pois a configuração da biblioteca não e cadastrada junto com as outras configurações e estava gerando erro ao tentar
    // acessar o controlador da biblioteca
    // CONFIGURACAO_BIBLIOTECA_CONTROLE("ConfiguracaoBibliotecaControle", ConfiguracaoBibliotecaControle.class);

    String valor;
    private Class classe;

    ConfiguracoesDominio(String valor, Class classe) {
        this.valor = valor;
        this.classe = classe;
    }

    public static ConfiguracoesDominio getEnum(String valor) {
        ConfiguracoesDominio[] valores = values();
        for (ConfiguracoesDominio obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static Class getClasse(String valor) {
        ConfiguracoesDominio obj = getEnum(valor);
        if (obj != null) {
            return obj.getClasse();
        }
        return null;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Class getClasse() {
        return classe;
    }

    public void setClasse(Class classe) {
        this.classe = classe;
    }

}
