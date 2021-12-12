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

### Compile and Run in Local Dev Mode

### build & deploy to openshift

log to openshift
```
https://api.openshift_url:6443 -u login -p password 
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
    ```
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
  copy the YAML in ../manifest/grafana-datasouce.ymlfile
  ```
    oc apply -f ../manifest/grafana-datasouce.yml
  ```

# Quarkus content based routing
https://github.com/tarilabs/quarkus-content-based-routing