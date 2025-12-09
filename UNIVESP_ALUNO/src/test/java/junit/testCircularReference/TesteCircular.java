package junit.testCircularReference;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.List;

public class TesteCircular {

  
    static class ConfiguracaoGeralSistemaVO {
        private Integer codigo;
        
        private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs; 

        public void setCodigo(Integer codigo) { this.codigo = codigo; }
        
        public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> lista) {
            this.unidadeEnsinoCursoVOs = lista;
        }
    }

    // CLASSE FILHA
    static class UnidadeEnsinoCursoVO {
        private String nomeCurso;

       
        @Expose(deserialize = false, serialize = false) 
        private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

        public void setNomeCurso(String nome) { this.nomeCurso = nome; }

        public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO config) {
            this.configuracaoGeralSistemaVO = config;
        }
    }


    public static void main(String[] args) {
        System.out.println(">>> INICIANDO TESTE DE REFERÊNCIA CIRCULAR <<<");

        ConfiguracaoGeralSistemaVO configPai = new ConfiguracaoGeralSistemaVO();
        configPai.setCodigo(100);


        UnidadeEnsinoCursoVO cursoFilho = new UnidadeEnsinoCursoVO();
        cursoFilho.setNomeCurso("Engenharia de Software");


        List<UnidadeEnsinoCursoVO> lista = new ArrayList<>();
        lista.add(cursoFilho);
        configPai.setUnidadeEnsinoCursoVOs(lista);


        cursoFilho.setConfiguracaoGeralSistemaVO(configPai);


        try {

            Gson gson = new Gson(); 
            
            // Se o seu RedisService usa configurações especiais, descomente abaixo:
            // Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            System.out.println("Tentando converter para JSON...");
            long inicio = System.currentTimeMillis();
            
            String json = gson.toJson(configPai);
            
            long fim = System.currentTimeMillis();
            
            System.out.println("SUCESSO! JSON Gerado em " + (fim - inicio) + "ms");
            System.out.println("Tamanho do JSON: " + json.length() + " caracteres");
            // System.out.println(json); // Descomente para ver o JSON

        } catch (StackOverflowError e) {
            System.err.println("\n ERRO FATAL: StackOverflowError detectado!");
            System.err.println("O Gson entrou em loop infinito: Pai -> Filho -> Pai -> Filho...");
            System.err.println("SOLUÇÃO: Adicione a palavra 'transient' no atributo 'configuracaoGeralSistemaVO' na classe filha.");
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
        }
    }
}
