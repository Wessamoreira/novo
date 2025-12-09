package controle.arquitetura;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Configuration
public class GsonConfig {

    private static final List<String> CAMPOS_IGNORADOS = Arrays.asList(
        "serialVersionUID", "version", "created", "codigoCreated", "nomeCreated",
        "updated", "codigoUpdated", "nomeUpdated", "novoObj", "validarDados",
        "nivelMontarDados", "maximixado", "minimizar", "selecionado",
        "controlarConcorrencia", "edicaoManual", "verificouBloqueioPorFechamentoMes",
        "bloqueioPorFechamentoMesLiberado", "descricaoBloqueio", "dataBloqueioVerificada",
        "mensagemAvisoUsuario", "entityManager", "handler", "hibernateLazyInitializer"
    );

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")

                // --- ADAPTADORES DE DATA ---
                .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, type, ctx) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, ctx) -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .registerTypeAdapter(OffsetDateTime.class, (JsonSerializer<OffsetDateTime>) (src, type, ctx) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .registerTypeAdapter(OffsetDateTime.class, (JsonDeserializer<OffsetDateTime>) (json, type, ctx) -> OffsetDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .registerTypeAdapter(ZonedDateTime.class, (JsonSerializer<ZonedDateTime>) (src, type, ctx) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)))
                .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, type, ctx) -> ZonedDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_ZONED_DATE_TIME))

                // --- VACINA CONTRA O ERRO DE ENUM ---
                // Isso impede o Gson de tentar instanciar 'java.lang.Enum' diretamente
                .registerTypeHierarchyAdapter(Enum.class, new TypeAdapter<Enum<?>>() {
                    @Override
                    public void write(JsonWriter out, Enum<?> value) throws IOException {
                        if (value == null) {
                            out.nullValue();
                        } else {
                            out.value(value.name());
                        }
                    }

                    @Override
                    public Enum<?> read(JsonReader in) throws IOException {
                        // Se cair num Enum genérico que o Gson não sabe o tipo, ele pula
                        in.skipValue(); 
                        return null; 
                    }
                })

                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        if (f.getAnnotation(ExcluirJsonAnnotation.class) != null) return true;
                        if (CAMPOS_IGNORADOS.contains(f.getName())) return true;
                        
                        String classeNome = f.getDeclaredClass().getName();
                        if (classeNome.startsWith("java.util.logging") ||
                            classeNome.startsWith("org.slf4j") ||
                            classeNome.startsWith("org.hibernate") || 
                            classeNome.startsWith("org.springframework")) {
                            return true;
                        }
                        
                        // Ignora campos declarados como Enum puro
                        if (f.getDeclaredClass() == Enum.class) {
                            return true;
                        }

                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return clazz.getName().contains("CGLIB") || 
                               clazz.getName().contains("HibernateProxy");
                    }
                })
                .create();
    }
}





