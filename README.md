# 제목 1







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
