# 윈터프로그래밍 과제 [![Build Status](https://travis-ci.org/1500sheep/todolist.svg?branch=master)](https://travis-ci.org/1500sheep/todolist)

> todolist 어플리케이션



[데모 영상 확인하기](https://www.useloom.com/share/8038cb16b82d45f38ad94acf895dbb9b)



## Getting Started

### Prerequisites

- Java 1.8.x
- Lombok plugin



### Run in development

```
# build project
./gradlew build

# Run project
java -jar build/libs/todolist-0.0.1.jar
```

#### Page

[URL](http://ec2-13-209-163-242.ap-northeast-2.compute.amazonaws.com:8080/) : AWS EC2 사용



### Dependencies

|           Dependency           |    Version    |
| :----------------------------: | :-----------: |
|    ======**FrontEnd**======    |  ==========   |
|             HTML5              |               |
|              CSS               |               |
|       vanilla Javascript       |  ECMAScript6  |
|    ======**Backend**======     |  ==========   |
|          spring-boot           | 2.0.5 RELEASE |
|  spring-boot-starter-data-jpa  |               |
| spring-boot-starter-validation |               |
|    spring-boot-starter-web     |               |
|       com.h2database:h2        |               |
| handlebars-spring-boot-starter |     0.3.0     |
|    org.projectlombok:lombok    |     1.18      |
|    org.assertj:assertj-core    |    3.10.0     |
|       ======**CI**======       |  ==========   |
|           travis-ci            |               |



------



## API Document

### TODO 생성

#### Request

```
POST /api/todo
```

| Parameter   | Type      | Required | Default | Description            |
| ----------- | --------- | -------- | :-----: | ---------------------- |
| title       | String    | true     |         | 제목, 20자 이하만 가능 |
| content     | String    | true     |         | 내용                   |
| endDate     | LocalDate | false    |         | 종료 날짜              |
| priority    | int       | false    |    0    | 우선 순위              |
| isCompleted | Boolean   | true     |  false  | 완료 여부              |

#### Success Response

```
HTTP/1.1 201 Created
Location: https://localhost/todo/1
```

#### Error Response

|                type                 |                     description                     |
| :---------------------------------: | :-------------------------------------------------: |
| Invalid parameter - Bad Request 400 | 도메인 설정에 맞지 않는 parameter, validation check |
| TimeFormatException - Forbidden 403 |            시간이 포맷이 맞지 않을 경우             |

```
{
"errors":[
	{
	     "field": "title", // Invalid parameter
	     "message": "제목은 필수로 입력하여야 합니다"
	},
	{
	     "field": "content",
	    "message": "내용은 필수로 입력하여야 합니다" // Invalid parameter
    	},
	{
	   "field": "endDate", // TimeFormatException
	   "message": "날짜 형식이 맞지 않습니다"
    	}
]}
```

<hr/>

### TODO List 갖고오기

#### Request

```
Get /api/todo
```

#### Success Response

```
HTTP/1.1 200 Ok
```

| return |  description   |
| :----: | :------------: |
|  List  | TODO List 반환 |

```
{"data":[{"id":1,"title":"title","content":"content","priority":1,"endDate":"2018-11-04T21:32:08.036","isCompleted":false},{"id":2,"title":"title2","content":"content2","priority":2,"endDate":"2018-11-05T19:36:00","isCompleted":true},{"id":3,"title":"title","content":"content","priority":0,"endDate":null,"isCompleted":false}]}
```



<hr/>

### TODO 완료 누르기 

#### Request

```
Get /api/todo/complete/{id}
```

#### Success Response

```
HTTP/1.1 200 Ok
```

| return |             description              |
| :----: | :----------------------------------: |
|  ToDo  | isCompleted 완료가 된 ToDo 객체 반환 |



<hr/>

### TODO 수정

#### Request

```
PUT /api/todo/{id}
```

| Parameter | Type   | Required | Default | Description                 |
| --------- | ------ | -------- | ------- | --------------------------- |
| content   | String | true     |         | 내용                        |
| priority  | int    | false    | 0       | 우선 순위, 0 이하의 수 안됨 |

#### Success Response

```
HTTP/1.1 200 Ok
```

| return | description |
| :----: | :---------: |
|  ToDo  | 수정한 ToDo |

### Error Response

| type                                | description                                         |
| ----------------------------------- | --------------------------------------------------- |
| Invalid parameter - Bad Request 400 | 도메인 설정에 맞지 않는 parameter, validation check |
| NotAllowedException - Forbidden 403 | 우선 순위가 중복 할 경우                            |

```
{
"error":
	{
	     "message": "우선 순위가 중복됩니다"
	}
 }
```



<hr/>

### TODO 삭제

#### Request

```
DELETE /api/todo/{id}
```

#### Success Response

```
HTTP/1.1 200 Ok
```



<hr/>

### 단위테스트, 통합테스트

- 단위 테스트 ```org.junit``` 사용 , validation check를 위해 ```import javax.validation``` 사용
- 통합 테스트 ```org.springframework.boot.test.web.client.TestRestTemplate``` 사용

```
// project structure (test)
test
└───sheep.todolist
│   └───domain
│   │   │   ToDoTest // 단위 테스트
│   │   │   ToDoValidationTest // 단위 테스트
│   └───dto
│   │   │   ToDoValidationTest // 단위 테스트
│   └───web 
│       │   ApiToDoAcceptanceTest // 통합 테스트
└───support
    │   AcceptanceTest // 통합 테스트
```