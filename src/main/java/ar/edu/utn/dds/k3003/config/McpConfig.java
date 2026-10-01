package ar.edu.utn.dds.k3003.config;

import ar.edu.utn.dds.k3003.tools.DonadoresTools;
import ar.edu.utn.dds.k3003.tools.EntidadesTools;
import ar.edu.utn.dds.k3003.tools.NecesidadesTools;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {

    @Bean
    public ToolCallbackProvider donadoresYEntidadesTools(
            DonadoresTools donadoresTools, EntidadesTools entidadesTools, NecesidadesTools necesidadesTools) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(donadoresTools, entidadesTools, necesidadesTools)
                .build();
    }
}
