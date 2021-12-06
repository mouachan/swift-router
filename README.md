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

# rule sample
when 
receiverAddress = 'BNPAFRPP...' and messageType/code = 'MT012’ and TRN = ‘Test…’ and document/data =
'(.*[{]4:.*103[:]EBA.*[{]5:.*|.*[{]4:.*103[:]ERP.*[{]5:.*)’
then 
codeRoutage = CHG02

# routing example
https://github.com/tarilabs/quarkus-content-based-routing