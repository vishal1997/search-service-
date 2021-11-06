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


Questions covered :
# Basic Requirements:
1. Server should call the YouTube API continuously in background (async): Done 
2. A GET API which returns the stored video data in a paginated response sorted in descending order of published datetime.: Done 
3. A basic search API to search the stored videos using their title and description.: Done 
4. Dockerize the project.: Done 
5. It should be scalable and optimised. : done (Can be optimised more for vertical scaling)

# Bonus Points:
1. Add support for supplying multiple API keys so that if quota is exhausted on one, it automatically uses the next available key.: Not Done 
        a. The simple solution would be to take list of keys and iterate over it till we get a with that has quota 
        b. Maintain a pool of two list one which has quota and other with quota exceeded. Once a quota is exceeded move the key to quota exceeded list. And the key should be automatically added back to quota list once the quota of the key is reset  
2. Make a dashboard to view the stored videos with filters and sorting options (optional): Not Done 
3. Optimise search api, so that it's able to search videos containing partial match for the search query in either video title or description.: Done 

Improvements:
1. Add UT for each class 
2. Some places has TODO which is to improve the code quality 
