rm ./finalbuild -R
mkdir ./finalbuild
mvn clean package -DskipTests
cp target/*.jar finalbuild/
cp ./src/main/resources/application.properties ./finalbuild/
cp ./src/main/resources/commands.json ./finalbuild/


$root=$PSScriptRoot
$root= [regex]::escape($PSScriptRoot)
new-item -path './finalbuild/run.ps1'
write-output "java -jar command-center-client-0.0.1-SNAPSHOT.jar -spring.config.location=$root`\\application.properties" | add-content ./finalbuild/run.ps1
