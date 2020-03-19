package org.gty.demo.config;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

import javax.annotation.Nonnull;

@Configuration
@EnableR2dbcRepositories(basePackages = "org.gty.demo.repository.r2dbc")
public class ReactiveDbConfig extends AbstractR2dbcConfiguration {

    private static final String POOL_STRING = "pool";
    private static final String PROTOCOL_STRING = "postgresql";
    private static final int PORT_INT = 5432;
    private static final String DATABASE_STRING = "db_sbrd";

    @Value("${postgresql-host}")
    private String host;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Nonnull
    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        var options = ConnectionFactoryOptions.builder()
            .option(ConnectionFactoryOptions.DRIVER, POOL_STRING)
            .option(ConnectionFactoryOptions.PROTOCOL, PROTOCOL_STRING) // driver identifier, PROTOCOL is delegated as DRIVER by the pool.
            .option(ConnectionFactoryOptions.HOST, host)
            .option(ConnectionFactoryOptions.PORT, PORT_INT)
            .option(ConnectionFactoryOptions.USER, user)
            .option(ConnectionFactoryOptions.PASSWORD, password)
            .option(ConnectionFactoryOptions.DATABASE, DATABASE_STRING)
            .build();

        return ConnectionFactories.get(options);
    }
}
