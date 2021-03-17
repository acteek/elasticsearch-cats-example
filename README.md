## Elasticsearch-cats-example

### local elastic docker 

`docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:7.11.1`

### test run

```
$ sbt
 runMain com.github.acteek.example.CatsExample
```
output:
```
Cat(id00002,Iriska,10,List(pineapple, fish),Owner(Bob,Doll))
Cat(id00003,Bob,1,List(fish),Owner(Bob,Doll))
There were 2 total hits
```