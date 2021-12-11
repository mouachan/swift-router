# swift-router

# generate the project on quarkus
mvn archetype:generate \
-DarchetypeGroupId=org.kie.kogito \
-DarchetypeArtifactId=kogito-quarkus-dm-archetype \
-DgroupId=com.redhat -DartifactId=swift-router-kogito-quarkus \
-DarchetypeVersion=1.5.0.redhat-00006 \
-Dversion=1.0-SNAPSHOT

# generate the project on springboot
mvn archetype:generate \
-DarchetypeGroupId=org.kie.kogito \
-DarchetypeArtifactId=kogito-springboot-dm-archetype \
-DgroupId=org.redhat -DartifactId=swift-router-kogito-springboot \
-DarchetypeVersion=1.5.0.redhat-00006 \
-Dversion=1.0-SNAPSHOT

# build & deploy to openshift
log to openshift
```
oc login 
```

Quarkus
```
~/projects/swift-router/swift-router-kogito-quarkus main* ❯ mvn clean package -Dquarkus.kubernetesdeploy=true                                                                                   
```       

Springboot
```
~/projects/swift-router/swift-router-kogito-springboot main* ❯ mvn clean fabric8:deploy -Popenshift -DskipTests
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

rest-client
```
 mvn clean package -Dquarkus.kubernetes.deploy=true  
````

Grafana
In order to connect grafana to Openshift monitoring :

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
 
# rule sample
when 
receiverAddress = 'BNPAFRPP...' and messageType/code = 'MT012’ and TRN = ‘Test…’ and document/data =
'(.*[{]4:.*103[:]EBA.*[{]5:.*|.*[{]4:.*103[:]ERP.*[{]5:.*)’
then 
codeRoutage = CHG02

# routing example
https://github.com/tarilabs/quarkus-content-based-routing