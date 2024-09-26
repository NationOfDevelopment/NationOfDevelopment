
⭐ **프로젝트 개요**

저희 프로젝트는 배달기능의 API를 개발하는데 초점을 맞춰 배달 어플을 만드는 아웃소싱 프로젝트입니다.  
주요 기능은 회원가입과 로그인, 가게 메뉴 등록, 장바구니와 주문, 주문에 대한 리뷰 기능을 내포하고 있습니다.

⭐ **주요 기술 스택**

![인텔리제이](https://img.shields.io/badge/IntelliJ_IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
![깃허브](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
<p>Development<p>

![자바](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![스프링](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![스프링부트](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![mysql](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white) <p>
Communication<p>
![슬랙](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)
![노션](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)


---
회원가입	

#### API:/auth/signup	

RequestBody:
```
{ 
  email:"email@gmail.com",
  username:"nickname",
  birthday:"2000-01-01",
  password:"password",
  userRole:”USER”
}
```
ResponseBody:
```
{   
"message": "회원 가입 완료",   "statusCode": 201,   
"data":
{    
}
}
```
+ 오류코드
  + _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,400,"중복된 이메일입니다.")
  + _INVALID_USER_ROLE(HttpStatus.BAD_REQUEST,400,"잘못된 유저권한 입니다.")
  + _INVALID_EMAIL_FORM(HttpStatus.BAD_REQUEST,400,"이메일 형식이 올바르지 않습니다.")
  + _INVALID_PASSWORD_FORM(HttpStatus.BAD_REQUEST,400,"비밀번호는 최소 8자 이상이어야 하며, 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다.")
  + _INVALID_BIRTHDAY(HttpStatus.BAD_REQUEST,400,"잘못된 생일 값입니다")
  + _INVALID_USER_NAME(HttpStatus.BAD_REQUEST,400 ,"유저이름은 최소 3자 이상,20자 이하여야 하며, 대소문자 포함 영문,숫자만 사용가능합니다." )

ResponseHeader:없음

RequestHeader:없음

---
