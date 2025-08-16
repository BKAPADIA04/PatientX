package com.systemdesign.billing_service.grpc;
import billing.BillingServiceGrpc;

import billing.BillingRequest;
import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {
    // @Override
    // public void createBillingAccount(BillingRequest request, StreamObserver<BillingAccountResponse> responseObserver) {
    //     // Logic to create a billing account
    //     BillingAccountResponse response = BillingAccountResponse.newBuilder()
    //             .setAccountId("12345")
    //             .setStatus("Created")
    //             .build();
    //     responseObserver.onNext(response);
    //     responseObserver.onCompleted();
    // }

    private static final org.slf4j.Logger Logger = org.slf4j.LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(
            BillingRequest request,
            StreamObserver<BillingResponse> responseObserver) {

        Logger.info("Received Billing Request: PatientId={}, Name={}, Email={}",
                request.getPatientId(), request.getName(), request.getEmail());      

        // Example logic: generate an account ID and return success
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("12345")
                .setStatus("CREATED")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
