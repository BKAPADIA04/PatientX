package com.systemdesign.infrastructure.stack;

import java.util.stream.Collectors;

import software.amazon.awscdk.services.ecs.CloudMapNamespaceOptions;
// import com.amazonaws.services.ecs.model.Cluster;
import software.amazon.awscdk.services.ecs.Cluster;

import software.amazon.awscdk.App;
import software.amazon.awscdk.AppProps;
import software.amazon.awscdk.BootstraplessSynthesizer;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.Token;
import software.amazon.awscdk.services.rds.DatabaseInstance;
import software.amazon.awscdk.services.ec2.ISubnet;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.rds.DatabaseInstanceEngine;
import software.amazon.awscdk.services.rds.PostgresEngineVersion;
import software.amazon.awscdk.services.rds.PostgresInstanceEngineProps;
import software.amazon.awscdk.services.route53.CfnHealthCheck;
import software.amazon.awscdk.services.rds.Credentials;
import software.amazon.awscdk.services.msk.CfnCluster;

public class LocalStack extends Stack {

    private final Vpc vpc;
    private final Cluster ecsCluster;

    public LocalStack(final App scope, final String id, final StackProps props) {
        super(scope, id, props);

        this.vpc = createVpc();

        DatabaseInstance authServiceDb = createDatabase("AuthServiceDB", "auth-service-db");
        DatabaseInstance patientServiceDb = createDatabase("PatientServiceDB", "patient-service-db");

        CfnHealthCheck authDbHealthCheck = createHealthCheck(authServiceDb, "AuthDBHealthCheck");
        CfnHealthCheck patientDbHealthCheck = createHealthCheck(patientServiceDb, "PatientDBHealthCheck");

        CfnCluster mskCluster = createMskCluster();
        this.ecsCluster = createECSCluster(); 
    }

    private Vpc createVpc() {
        return Vpc.Builder.create(this, "PatientXVpc").vpcName("PatientXVpc")
                .maxAzs(2)
                .build();
    }

    private DatabaseInstance createDatabase(String id, String dbName) {
        return DatabaseInstance.Builder
                .create(this, id)
                .engine(DatabaseInstanceEngine.postgres(
                PostgresInstanceEngineProps.builder()
                .version(PostgresEngineVersion.VER_17_2) // Use a supported version
                .build()))
                .vpc(vpc)
                .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
                .allocatedStorage(20)
                .credentials(Credentials.fromGeneratedSecret("admin_user"))
                .databaseName(dbName)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }

    private CfnHealthCheck createHealthCheck(DatabaseInstance db, String id) {
        return CfnHealthCheck.Builder.create(this, id)
                .healthCheckConfig(CfnHealthCheck.HealthCheckConfigProperty.builder()
                        .type("TCP")
                        .port(Token.asNumber(db.getDbInstanceEndpointPort()))
                        .ipAddress(db.getDbInstanceEndpointAddress())
                        .requestInterval(30)
                        .failureThreshold(3)
                        .build())
                .build();
    }

    private CfnCluster createMskCluster() {
        return CfnCluster.Builder.create(this, "MskCluster")
                .clusterName("kafka-cluster")
                .kafkaVersion("2.8.0")
                .numberOfBrokerNodes(1)
                .brokerNodeGroupInfo(CfnCluster.BrokerNodeGroupInfoProperty.builder()
                        .instanceType("kafka.m5.xlarge")
                        .clientSubnets(vpc.getPrivateSubnets().stream().map(ISubnet::getSubnetId).collect(Collectors.toList())).brokerAzDistribution("DEFAULT")
                        .build())
                .build();
    }

    private Cluster createECSCluster() {
        return Cluster.Builder.create(this, "PatientXCluster")
                .vpc(vpc)
                .defaultCloudMapNamespace(CloudMapNamespaceOptions.builder()
                .name("patient-x.local")
                .build())
                .build();
    }

    public static void main(final String[] args) {
        App app = new App(AppProps.builder().outdir("./cdk.out").build());

        StackProps prop = StackProps.builder().synthesizer(new BootstraplessSynthesizer()).build();

        new LocalStack(app, "LocalStack", prop);
        app.synth();

        System.out.println("App synthesizing in progress ...");
    }
}
