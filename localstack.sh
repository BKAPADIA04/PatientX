#!/bin/bash

set -e

aws --endpoint-url=http://localhost:4566 cloudformation deploy \
    --stack-name patientX \
    --template-file "./cdk.out/LocalStack.template.json" \

aws --endpoint-url=http://localhost:4566 elbv2 describe-load-balancers \
    --query "LoadBalancers[].DNSName" --output text