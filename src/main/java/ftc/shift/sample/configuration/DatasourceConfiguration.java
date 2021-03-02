package ftc.shift.sample.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//import javax.annotation.ParametersAreNonnullByDefault;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties
//@ParametersAreNonnullByDefault
public class DatasourceConfiguration {
    @Bean
    @ConfigurationProperties("shift.hikari")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }

    @Bean
    public DataSource dataSource(HikariConfig hikariConfig) {
        return new HikariDataSource(hikariConfig);
    }
}
