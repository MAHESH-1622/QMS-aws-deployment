package com.sm.qms.config;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.ProtocolVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.DropKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//@Configuration
//@EnableCassandraRepositories
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${spring.cassandra.keyspace-name: qms_crud}")
    private String keyspace;

    @Value("${spring.cassandra.contact-points: localhost}")
    private String contactPoint;

    @Value("${spring.cassandra.port: 9042}")
    private int port;

    @Value("${spring.cassandra.basePackages}")
    private String basePackages;

    @Value("${spring.data.cassandra.username}")
    private String username;

    @Value("${spring.data.cassandra.password}")
    private String password;

    public CassandraConfig() {
    }

    @Override
    protected String getKeyspaceName() {
        return keyspace;
    }

    @Override
    public String getContactPoints() {
        return contactPoint;
    }

    @Override
    protected int getPort() {
        return port;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

//    @Override
//    protected String getLocalDataCenter() {
//        return "datacenter1";
//    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{basePackages};
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(keyspace)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication(3L));
    }

    @Override
    protected List<DropKeyspaceSpecification> getKeyspaceDrops() {
//        return Arrays.asList(DropKeyspaceSpecification.dropKeyspace(keyspace)); // For testing
        return super.getKeyspaceDrops();
    }

    @Override
    protected CqlSession getRequiredSession() {
        return super.getRequiredSession();
    }
}
