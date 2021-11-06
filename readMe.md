Tech Used: Elasticsearch, Java, Dropwizard, docker

Elastic search setup : https://www.elastic.co/start?elektra=organic&storm=CLP&rogue=free-and-open-gic

create es index using file present in: searchapi/src/main/resources/es_index_mapping.json

Created the ES index with correct mapping before executing the below commands.<br><br> 
Execute Application:
1. Using docker:<br>
    a. mvn clean install<br>
    b. docker build -t search-service .<br>
    c. docker run -p 8088:8088 --network host search-service:latest<br>
2. Directly run jar file <br>
    a. mvn clean install <br>
    b. sh start-jar<br>
    
    
API :
1. get: http://localhost:8088/youtube, http://localhost:8088/youtube?pageId=string
2. search: http://localhost:8088/youtube/search?q=India, http://localhost:8088/youtube/search?q=India&pageId=string