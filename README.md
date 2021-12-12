# swift-router



## Description

This demo is a further iteration of rule-based SWIFT message routing; using Quarkus, Springboot, Kogito Drools DMN Engine.

## Objectives

Showcase the ability to route data real time through DMN rules maintained by business users and how to enable and consume the runtime metrics monitoring

### Prerequisites
 
You will need:
  - Java 11+ installed 
  - Environment variable JAVA_HOME set accordingly
  - Maven 3.6.2+ installed
  - Docker 19+ (only if you want to run the integration tests and/or you want to use the `docker-compose` script provided in this example).
  - Openshift cli to deploy on Openshift
  - Red Hat Business Automation Operator
  - Grafana Operator
  - Red Hat Integration - AMQ Streams

### Archetype

mvn archetype to generate kogito-quarkus application
```mvn
mvn archetype:generate \
-DarchetypeGroupId=org.kie.kogito \
-DarchetypeArtifactId=kogito-quarkus-dm-archetype \
-DgroupId=com.redhat -DartifactId=swift-router-kogito-quarkus \
-DarchetypeVersion=1.5.0.redhat-00006 \
-Dversion=1.0-SNAPSHOT
```
mvn archetype to generate kogito-springboot application

```mvn
mvn archetype:generate \
-DarchetypeGroupId=org.kie.kogito \
-DarchetypeArtifactId=kogito-springboot-dm-archetype \
-DgroupId=org.redhat -DartifactId=swift-router-kogito-springboot \
-DarchetypeVersion=1.5.0.redhat-00006 \
-Dversion=1.0-SNAPSHOT
```
### Deploy Authoring/Execution envrionement

Create Authoring and Execution service in a dev environment
``` 
oc apply -f ./manifest/rhdm-authoring.yml
```

get Decision central url 
```
oc get route swift-router-svc-design-time-rhdmcentr -o json | jq '.spec.host'
```
get kie-server url
```
oc get route swift-router-svc-design-time-kieserver -o json | jq '.spec.host'
```

Log into Decision Central using dmAdmin/dmAdmin


Business Central API : 
```
echo $(oc get route swift-router-svc-design-time-rhdmcentr -o json | jq -r '.spec.host')/docs
```

Kie-server API : 
```
echo $(oc get route swift-router-svc-design-time-kieserver- -o json | jq -r '.spec.host')/docs
```



call router decision service  
Container-ID : DMNRouter_1.0.0-SNAPSHOT
payload 
```json
    {
    "model-namespace": "https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF",
    "model-name": "router",
    "event" : {
            "receiverAddress":"BNPAFRPP",
            "messageType":{
                "code":"MT012"
            },
            "TRN":"Test",
            "document":{
                "data":"r{4:5103:EBA7{5:6"
            }
        }
    }
```

```json
curl -X POST "https://swift-router-svc-design-time-kieserver-swift-router.apps.cluster-nq8h5.nq8h5.sandbox1017.opentlc.com/services/rest/server/containers/DMNRouter_1.0.0-SNAPSHOT/dmn/models/router" -H "accept: application/json" -H "content-type: application/json" -d "{\"model-namespace\": \"https://github.com/kiegroup/drools/kie-dmn/_A4BCA8B8-CF08-433F-93B2-A2598F19ECFF\",\"model-name\": \"router\",\"event\" : {\t\t\"receiverAddress\":\"BNPAFRPP\",\t\t\"messageType\":{\t\t\t\"code\":\"MT012\"\t\t},\t\t\"TRN\":\"Test\",\t\t\"document\":{\t\t\t\"data\":\"r{4:5103:EBA7{5:6\"\t\t}\t}}"
```
### Compile and Run in Local Dev Mode

### build & deploy decisions services as microservices to openshift



log to openshift
```
https://api.openshift_url:6443 -u login -p password 
```
create kafka cluster
```
oc apply -f ./manifest/kafka.yml
````
deploy kafdrop
```
oc apply -f ./manifest/kafdrop.yml
```
build Quarkus Kogito decision service
```
cd swift-router-kogito-quarkus
mvn clean package -Dquarkus.kubernetesdeploy=true                                                                                   
```       

build and deploy Springboot Kogito decision service 
```
cd ../swift-router-kogito-springboot
mvn clean fabric8:deploy -Popenshift -DskipTests
```  
get routes
```
rm ../manifest/swift-cm.properties
echo "SwiftQuarkus_URL=https://$(oc get route swift-router-kogito-quarkus --template={{.spec.host}})" >! ../manifest/swift-cm.properties
echo "SwiftSpringBoot_URL=https://$(oc get route swift-router-kogito-springboot --template={{.spec.host}})" >> ../manifest/swift-cm.properties
```
create the configmap
```
oc create configmap swift-router-cm --from-env-file=../manifest/swift-cm.properties --dry-run -o yaml | oc apply -f -   
```

build and deploy quarkus rest client to call the quarkus decision service
```
 cd ../swift-rest-quarkus-client
 mvn clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.openshift.labels.app-with-metrics=swift-rest-quarkus-client   
```
build and deploy quarkus rest client to call the springboot decision service
```
 cd ../swift-rest-springboot-client
 mvn clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.openshift.labels.app-with-metrics=swift-rest-quarkus-client   
```
Configure OpenShift Container Platform monitoring to scrape metrics from the /metrics endpoints of swift-router-kogito-quarkus and swift-router-kogito-springboot applications 
```
oc apply -f ../manifest/prometheus-service-monitor-openshift.yml
```
execute the following instructions in order to connect grafana to Openshift monitoring :

grant the grafana-serviceaccount to the cluster-monitoring-view cluster role.
```sh
oc adm policy add-cluster-role-to-user cluster-monitoring-view -z grafana-serviceaccount
```

Get the token 
```
    oc serviceaccounts get-token grafana-serviceaccount
``` 
in the below YAML, substitute ${BEARER_TOKEN} with the output of the command above 
```yaml
    apiVersion: integreatly.org/v1alpha1
    kind: GrafanaDataSource
    metadata:
    name: grafana-prometheus-datasource
    spec:
    datasources:
        - access: proxy
        editable: true
        isDefault: true
        jsonData:
            httpHeaderName1: 'Authorization'
            timeInterval: 5s
            tlsSkipVerify: true
        name: Prometheus
        secureJsonData:
            httpHeaderValue1: 'Bearer ${BEARER_TOKEN}'
        type: prometheus
        url: 'https://thanos-querier.openshift-monitoring.svc.cluster.local:9091'
    name: grafana-prometheus-datasource
```
copy the YAML in ../manifest/grafana-datasouce.yml file
```
oc apply -f ../manifest/grafana-datasouce.yml
```

# Quarkus content based routing
https://github.com/tarilabs/quarkus-content-based-routing