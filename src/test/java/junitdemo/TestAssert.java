package test;

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.ResponseAwareMatcher.*;
import static org.hamcrest.Matchers.*;

public class TestAssert {
    @BeforeClass
    public static void setup(){
        useRelaxedHTTPSValidation();
        //设置全局变量
        RestAssured.baseURI="https://testerhome.com";
        //设置代理
        //RestAssured.proxy("127.0.0.1", 8080);
    }
    @Test
    public void testHtml(){

        given()
                .queryParam("q","appium")
        .when()
                .get("https://testerhome.com/search").prettyPeek()
        .then()
                .statusCode(200)
                .body("html.head.title",equalTo("appium · 搜索结果 · TesterHome"));

    }

    @Test
    public void testTesterHomeJson(){

        given()
        .when()
                .get("https://testerhome.com/api/v3/topics.json").prettyPeek()
        .then()
                .statusCode(200)
                .body("topics.title",hasItems("优质招聘汇总"))
                //正数第二个
                .body("topics.title[1]",equalTo("优质招聘汇总"))
                //倒数第一个
                .body("topics.id[-1]",equalTo(12915))
                //findAll要用数组断言
                .body("topics.findAll { topic->topic.id == 14369 }.title",hasItems("为什么想从测试转开发"))
                //find只需找到第一个
                .body("topics.find { topic->topic.id == 14369 }.title",equalTo("为什么想从测试转开发"))
                //title有多少个
                .body("topics.title.size()",equalTo(10))
                 ;

    }

    @Test
    public void testTesterHomeJsonSingle(){

        given().when().get("https://testerhome.com/api/v3/topics/10254.json")
        .then()
                .statusCode(200)
                .body("topic.title",equalTo("优质招聘汇总"))
                ;

    }

    @Test
    public void testTesterHomeJsonGlobal(){

        RestAssured.config();
        given().proxy("127.0.0.1",8080)
                .when().get("/api/v3/topics/10254.json").prettyPeek()
                .then()
                .statusCode(200)
                .body("topic.title",equalTo("优质招聘汇总"))
        ;

    }

    @Test
    public void testTesterHomeSearch(){
        given().queryParam("q","霍格沃兹测试学院")
        .when().get("");
    }

    @Test
    public void testXML(){
        Response response = given().when().get("http://127.0.0.1:8000/hogwarts.xml").prettyPeek()
        .then()
                .statusCode(200)
                .body("shopping.category.item.name[2]",equalTo("Paper"))
                .body("shopping.category[1].item[1].name",equalTo("Pens"))
                //当前分类有多少个数量
                .body("shopping.category.size()",equalTo(3))
                .body("shopping.category[1].item.size()",equalTo(2))
                //找节点属性是present的
                .body("shopping.category.find { it.@type == 'present' } .item.name",equalTo("xxx"))
                //价格等于200的礼品
                .body("**.find { it.price == '200' }.name",equalTo("Kathryn's Birthday"))
                //名字等于pens的价格
                .body("**.find { it.name == 'Pens' }.price",equalTo("200"))
        .extract().response()
        ;
        System.out.println(response.statusCode());
        System.out.println(response.statusLine())
        ;
    }

    @Test
    public void testJsonPost(){
        HashMap<String, Object> data=new HashMap<String, Object>();
        data.put("id",6040);
        data.put("title","通过代理安装 appium");
        data.put("name","思寒");

        HashMap<String, Object> root=new HashMap<String, Object>();
        root.put("topic", data);

        given().contentType(ContentType.JSON).body(root)
                .when().post("http://www.baidu.com")
                .then().statusCode(200);

        given().contentType(ContentType.XML).body(root)
                .when().post("http://www.baidu.com")
                .then().statusCode(200).time(lessThan(1000L));

    }

    @Test
    public void multiApi(){
        //取出返回的数据
        //String name = given().get("https://testerhome.com/api/v3/topics/6040.json").prettyPeek()
                //.then().statusCode(200).extract().path("topic.user.name")
        Response response = given().get("https://testerhome.com/api/v3/topics/6040.json").prettyPeek()
                .then().statusCode(200).extract().response();
        String name = response.path("topic.user.name");
        Integer uid = response.path("topic.user.id")

        ;
        System.out.println(name);
        System.out.println(uid);

        //放到下一个接口
        given().queryParam("q",name)
                .cookie("name",name)
                .cookie("uid",uid)
         .when().get("/search")
         .then()
                .statusCode(200).body(containsString(name));
    }

    @Test
    public void testSpec(){
        ResponseSpecification rs = new ResponseSpecBuilder().build();
        rs.statusCode(200);
        rs.time(lessThan(1500L));
        rs.body(not(containsString("error")));

        given().get("/api/v3/topics/6040.json")
                .then().spec(rs);

    }

    @Test
    public void testFilter(){


    }

    public static void main(String[] args){
        // TODO Auto-generated method stub
    }
}
