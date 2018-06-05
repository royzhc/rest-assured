package test;

import io.restassured.response.Response;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class interfacetest {

    @Test
    public void gettest(){

        //?limit=2&offset=0&type=last_actived

       // Map<String,Object> map = new HashMap<String,Object>();
        //map.put("limit",2);
        //map.put("offset",0);
        //map.put("type","last_actived");

     //   given().params(map).get("https://testerhome.com/api/v3/topics.json").prettyPeek();
        // 三种get方法：
        // get("https://testerhome.com/{topics}/{topicid}",map).prettyPeek();
       // get("https://testerhome.com/{topics}/{topicid}","topics",12192).prettyPeek();
       // get("https://testerhome.com/topics/12192}").prettyPeek();

       // given().body("{ \"message\" : \"hello world\"}").post("https://testerhome.com/api/v3/topics.json").prettyPeek();
       // File file = new File("")
        //        given().body(file).post("https://testerhome.com/api/v3/topics.json").prettyPeek();

        //given().cookie("username","xxxxx").get("");
        //given().header().get("");
        //given().urlEncodingEnabled(false).param("user","社区").param("password","1234").get("");
        //given().multiPart(file).post("");


    }

    @Test
    public void jsonpathtest(){
        Response response = get("https://testerhome.com/api/v3/topics.json?limit=2&offset=0&type=last_actived");

        List<Object> list = response.jsonPath().getList("topics");

        System.out.println(list.size());
        System.out.println(response.jsonPath().getString("topics[1].id"));
    }

    public static void main(String[] args){
        // TODO Auto-generated method stub
    }


}
