# 나만의 작은 시니또
<div align='center'>
  
  <img src='https://github.com/user-attachments/assets/555957c8-b2aa-4797-8660-8e404962da01'>

  ### "디지털시대?! 나도 이제 두렵지 않아!"
  👦🏻 세대와 👨🏻‍🦳세대를 이어주는 따뜻한 전화 📞

  <b>나만의 작은 시니또</b>
</div>

## 목차
- [🤔 시니또가 무엇인가요?](#시니또가-무엇인가요)
- [🧑🏻‍💻 어떤 서비스인가요?](#어떤-서비스인가요)
- [💬 핵심 기능 간략한 설명](#핵심-기능-간략한-설명)
  - [콜백 서비스](#콜백-서비스)
  - [안부 전화 서비스](#안부-전화-서비스)
- [💡 핵심 기능 상세 설명](#핵심-기능-상세-설명)
  - [1. 공통](#1-공통)
  - [2. 포인트](#2-포인트)
  - [3. 콜백 서비스](#3-콜백-서비스)
  - [4. 안부 전화 서비스](#4-안부-전화-서비스)
- [📂 디렉토리 구조](#디렉토리-구조)
- [👩‍👩‍👧‍👧 협업 과정](#협업-과정)
## 🚀 배포 주소
### ✔️ Client
[https://sinitto.life/](https://sinitto.life/)

### ✔️ Dummy Data Login
[https://sinitto.site/dummy](https://sinitto.site/dummy)

[주의: 더미데이터 유저는 카카오메시지가 오지 않습니다]

**비밀번호** `1234`

### ✔️ Server
[https://sinitto.site/](https://sinitto.site/)
### ✔️ Swagger
https://sinitto.site/swagger-ui/index.html
### ✔️ Admin Page
https://sinitto.site/admin/login

**이메일** `admin@kakao.com`

**비밀번호** `admin123`
### ✔️ Slack
[https://join.slack.com/t/sinitto/shared_invite](https://join.slack.com/t/sinitto/shared_invite/zt-2uqws45gg-bsPq8cuH2iQV6Jk1Y3Zglw)


## ✨ **프로젝트 실행 가이드**

아래의 단계를 따라 프로젝트를 실행할 수 있습니다.
> 우분투 기준으로 작성되었습니다.

### **필수 소프트웨어**

- **Java 21**
- **Gradle**
- **Redis**
- **Git**

### **1. 프로젝트 클론**

GitHub에서 프로젝트를 클론합니다

```
git clone https://github.com/kakao-tech-campus-2nd-step3/Team8_BE.git
cd Team8_BE
```

### **2. 환경 설정**

**`application-dev.properties`** 파일에 아래와 같은 설정을 추가해야 합니다:

- **Kakao API 설정**: Kakao 로그인 및 결제 URL 관련 정보
- **Redis 설정**: Redis 서버 연결 정보
- **MySQL 설정**: 데이터베이스 연결 정보
- **JWT 설정**: 비밀 키 등 인증 관련 정보
- **SSL 설정**: 서버 SSL 인증서 경로 및 설정
- **Slack 설정**: 알림 관련 설정
- **Admin 설정**: 어드민 계정 설정

**`application-dev.properties`** 예시:

```
# Kakao API 설정
kakao.clientId=YOUR_KAKAO_CLIENT_ID
kakao.devRedirectUri=YOUR_LOCAL_REDIRECT_URI
kakao.redirectUri=YOUR_PROD_REDIRECT_URI
kakao.frontUri=YOUR_FRONT_URI
kakao.Pay-url=YOUR_KAKAO_PAY_URL

# JWT 설정
jwt.secret=YOUR_JWT_SECRET_KEY

# Redis 설정
spring.data.redis.host=YOUR_REDIS_HOST
spring.data.redis.port=YOUR_REDIS_PORT
spring.data.redis.password=YOUR_REDIS_PASSWORD

# MySQL 설정
spring.datasource.url=YOUR_MYSQL_URL
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update

# SSL 설정
server.ssl.key-store=YOUR_SSL_KEYSTORE_PATH
server.ssl.key-store-password=YOUR_KEYSTORE_PASSWORD
server.ssl.key-store-type=PKCS12

# Slack 설정
slack.notice.webhook.url=YOUR_SLACK_WEBHOOK_URL
slack.charge.request.url=YOUR_SLACK_CHARGE_REQUEST_URL
slack.withdraw.request.url=YOUR_SLACK_WITHDRAW_REQUEST_URL

# Admin 설정
admin.adminEmail=YOUR_ADMIN_EMAIL
admin.adminPassword=YOUR_ADMIN_PASSWORD

# 기타 설정
kakao.bank-name=YOUR_BANK_NAME
kakao.account-number=YOUR_ACCOUNT_NUMBER
kakao.name=YOUR_KAKAO_NAME
kakao.front-uri-without-https=YOUR_FRONT_URI_WITHOUT_HTTPS
```

### **3. 의존성 설치**

프로젝트 의존성을 설치합니다.

```
./gradlew build
```

### **4. Redis 서버 실행**

Redis 서버를 실행합니다.

```
redis-server
```

### **5. 애플리케이션 실행**

애플리케이션을 실행하는 방법에는 두 가지가 있습니다.

1. **Gradle로 애플리케이션 실행**
    ```
    ./gradlew bootRun
    ```

2. **JAR 파일을 실행**

    ```
    ./gradlew bootJar
    java -jar build/libs/Team8_BE-0.0.1-SNAPSHOT.jar
    ```




## 👥 팀원

|Frontend|Frontend|Frontend|
|:------:|:------:|:------:|
|[<img src="https://github.com/Dobbymin.png" width="100px">](https://github.com/Dobbymin)|[<img src="https://github.com/Diwoni.png" width="100px">](https://github.com/Diwoni)|[<img src="https://github.com/JYN523.png" width="100px">](https://github.com/JYN523)|
|[김강민](https://github.com/Dobbymin)|[정지원](https://github.com/Diwoni)|[제유나](https://github.com/JYN523)|

|Backend|Backend|Backend|Backend|
|:------:|:------:|:------:|:------:|
|[<img src="https://github.com/zzoe2346.png" width="100px">](https://github.com/zzoe2346)|[<img src="https://github.com/GitJIHO.png" width="100px">](https://github.com/GitJIHO)|[<img src="https://github.com/2iedo.png" width="100px">](https://github.com/2iedo)|[<img src="https://github.com/eunsoni.png" width="100px">](https://github.com/eunsoni)|
|[정성훈](https://github.com/zzoe2346)|[이지호](https://github.com/GitJIHO)|[이도훈](https://github.com/2iedo)|[김은선](https://github.com/eunsoni)|

## ⚒️ 기술 스텍

### Frontend
<img width="697" alt="image" src="https://github.com/user-attachments/assets/6387235c-1ddf-4ebf-8eca-d3e838559d4a">


### Backend
<img width="705" alt="image" src="https://github.com/user-attachments/assets/c1f11983-4563-4322-9a29-e512e14927d5">

> [!NOTE]  
> 백엔드 내부 쉘스크립트 모음 https://curse-plastic-d90.notion.site/3ce05a49e06046469bde14c12b8b9c00

## 서비스 아키텍처

![Service Architecture](https://github.com/user-attachments/assets/a9ad53f3-9b63-433d-b4a7-1afbbfd21e90)

## ERD
![image](https://github.com/user-attachments/assets/d638bf78-4adc-4a43-96f2-e30cd04ea7b3)

## Use Case Diagram
![image](https://github.com/user-attachments/assets/0259f679-b13f-496a-8261-6e6df0abed50)
![image](https://github.com/user-attachments/assets/71999d87-3d83-4019-b46b-1c2abc54ccc3)
![image](https://github.com/user-attachments/assets/03da2737-f945-4803-b325-3ca831720b56)
![image](https://github.com/user-attachments/assets/bc12fbc3-4111-4072-a62d-682360e5a186)


## 🤔 시니또가 무엇인가요?

‘시니또’는 어르신을 뜻하는 ‘시니어’와 비밀 친구를 뜻하는 ‘마니또’를 합친 단어로,
디지털 서비스를 이용하기 어려운 어르신들과 디지털 서비스를 이용하는데 익숙한
젊은 세대를 이어주기 위한 ‘시니또’라는 명칭을 사용해보았습니다.

또한 ‘시니또’는 세대 간의 소통을 강조하며, 서로의 필요를 이해하고 지원하는
관계를 형성하고자 하는 취지를 담고 있습니다.

## 🧑🏻‍💻 어떤 서비스인가요?

과거에는 시니어들이 전화를 통해 배달 주문이나 서비스 이용을 비교적 쉽게 할 수
있었습니다. 그러나 스마트폰이 발달한 현재, 대부분의 서비스가 스마트폰
애플리케이션 기반으로 제공되고있어, 시니어들이 이러한 디지털 서비스를 이용하는
데 많은 어려움을 겪고 있습니다.

실제로 저희 조원 중 한 분의 할아버지께서는 스마트폰 사용에 어려움을 느끼셨고,
주로 조원분들에게 전화를 하여 도움을 요청하셨습니다. 이러한 문제점을 바탕으로
저희는 ‘어르신들이 예전에 자주 사용하시던 **전화**를 통해 디지털 서비스를 쉽게
이용할 수는 없을까?’ 라는 생각을 하게 되었고, 이를 ‘시니또’ 서비스의 핵심
기능으로 자리 잡게 되었습니다.

이 서비스는 어르신들이 보다 쉽게 디지털 서비스를 이용할 수 있도록 돕는 동시에,
젊은 세대가 그들과 소통하고 지원할 수 있는 기회를 제공하고자 합니다.

## 💬 핵심 기능 간략한 설명

> ‘시니또‘는 크게 두 가지의 핵심 기능이 있습니다.

### 콜백 서비스

어르신들이 **전화 한 통**으로 디지털 서비스를 이용할 수 있게 합니다.

이 서비스를 원활하게 이용할 수 있도록 보호자는 사전에 시니또 서비스에 시니어를
등록하고, **시니어가 요청할 내용이 담긴 가이드라인**을 제공합니다.

### 안부 전화 서비스

평소 업무 등의 활동으로 인해 시니어분들과 연락할 시간이 부족한 보호자들을 위한
서비스입니다.

보호자는 사전에 웹사이트에 안부전화 서비스를 이용할 기간, 시간대, 전화 시간 및
전화로 확인했으면 하는 내용을 작성하고, 시니또는 해당 요청사항을 확인한 후 수락
버튼을 눌러 안부 전화 서비스를 수행하게 됩니다.

## 💡 핵심 기능 상세 설명

### 1. 공통

> **로그인**

<img width="194" alt="image-1" src="https://github.com/user-attachments/assets/a1c1c560-8ad4-4d1b-88a8-50a4dcedfc87">

’카카오톡 로그인’ 버튼을 눌러 로그인을 진행합니다. 이전에 로그인을 한 적이 있을
경우 바로 로그인이 완료되며, 처음 이용할 경우 회원가입 창으로 이동하게 됩니다.

> **회원가입**

<img width="194" alt="image-2" src="https://github.com/user-attachments/assets/46e50cd6-aa3e-4615-be09-edb2a7e6917b">

사용자는 가입 유형인 ‘**시니또**‘와 ’**보호자**‘ 중 한 가지 유형을 선택합니다.

여기서 ’시니또‘는 시니어의 디지털 서비스를 수행하는 사람이며, ’보호자‘는 디지털
서비스를 필요로 하는 시니어를 등록하는 사람입니다.

사용자는 가입 유형을 선택한 후 이름 및 연락처를 입력하고, 아래의 서약서를 읽은
후 ’서약서의 내용에 동의합니다‘를 체크한 후에 ’가입하기‘ 버튼을 눌러 회원가입을
완료합니다.

### **2. 포인트**

> **포인트 충전 (보호자)**

<img width="194" alt="image-4" src="https://github.com/user-attachments/assets/847a5f99-c365-43b9-abde-c2ff6e084819">

‘보호자’는 포인트를 충전하여 시니어가 제공받는 서비스의 값을 제공합니다.

‘충전하기’ 버튼을 누른 후, 충전 금액을 입력하면 충전 요청이 완료되며, 이러한
요청은 관리자에게 전달됩니다.

그 후 카카오톡의 ‘나에게 보낸 메시지’를 확인하여 금액을 입금할 은행계좌와
입금자명을 확인하고, 해당 양식에 맞추어 입금을 진행합니다.

관리자는 입금 내역을 확인한 후 포인트를 지급합니다.

> **포인트 출금 (시니또)**

<img width="194" alt="image-3" src="https://github.com/user-attachments/assets/cdadb11e-c257-41d4-978a-b3c0eac93766">

‘시니또’는 콜백 서비스 및 안부전화 서비스를 통해 받은 포인트를 현금으로 환전할
수 있습니다.

최소 5000포인트 이상이 있어야 출금이 가능하며, 출금할 포인트를 입력한 후, ‘출금
신청’ 버튼을 눌러 출금 요청을 관리자에게 전송합니다.

관리자는 이를 확인한 후, 등록된 계좌번호로 수수료를 제외한 금액을 송금합니다.

### 3. 콜백 서비스

> **시니어 등록 (보호자)**

<img width="194" alt="image-5" src="https://github.com/user-attachments/assets/0637ec32-07c5-4ea9-aebd-f8335e7e2bbc">

‘보호자’는 디지털 서비스를 이용할 시니어를 등록합니다. 마이페이지의 ‘시니어
등록하기’버튼을 눌러 시니어를 등록합니다.

시니어를 등록하기 위해 시니어의 성함 및 전화번호를 입력해야 합니다.

> **가이드라인 등록 (보호자)**

<img width="194" alt="image-6" src="https://github.com/user-attachments/assets/a666bb59-ef40-4c66-b464-ad4b9f0a647d">

‘보호자’는 시니어를 등록한 후, 각 목록에 대한 가이드라인을 등록하여야 합니다.
`택시 호출하기` , `음식 배달 주문하기` , `병원 접수 대행` ,
`여행 및 문화 생활 예약 대행` 에 대한 가이드라인을 추가할 수 있으며, 가이드라인
제목 및 가이드라인 내용을 입력하여야 합니다.

‘시니또’가 이 내용을 확인한 후 디지털 서비스를 수행하기 때문에, 가능한 내용을
상세히 적어야 원할한 서비스 진행이 가능합니다.

> **전화를 통한 등록 (시니어)**

<img width="194" alt="image-7" src="https://github.com/user-attachments/assets/d5a0022f-5c20-48b6-abdf-76228707645a">

‘시니어’는 메인 페이지에 적혀있는 번호로 전화를 하여 콜백 서비스를 요청합니다.
등록이 성공했을 경우 ‘감사합니다. 잠시만 기다려주세요.’ 라는 기계음이 들리며,
‘시니또’가 요청을 수락할 때까지 기다리고 있으면 됩니다.

> **콜백 요청 확인 (시니또)**

<img width="194" alt="image-8" src="https://github.com/user-attachments/assets/a3e29d88-bcd8-4226-bcfd-f4cfa3c7ac03">

‘시니또’는 시니어가 요청한 콜백 서비스의 리스트를 확인할 수 있습니다. 여러
요청들 중 하나를 선택하여 상세한 내용을 확인할 수 있습니다.

> **콜백 요청 수락 & 수행 (시니또)**

<img width="194" alt="image-9" src="https://github.com/user-attachments/assets/ed421ef2-0e8c-405e-8334-3e18002ddbdc">

‘시니또’는 시니어가 요청한 콜백 서비스의 리스트 중 하나를 선택하여 ‘요청
상세페이지’로 이동할 수 있습니다.

각 목록마다 버튼을 클릭하여 상세한 내용을 확인할 수 있으며, 내용을 확인하고
서비스를 수행할 수 있을 경우 ‘전화걸기 및 수락하기’ 버튼을 클릭하여 서비스를
시작합니다.

> **콜백 요청 완료 대기 (시니또)**

<img width="194" alt="image-10" src="https://github.com/user-attachments/assets/b94f88ba-d4f8-4b5a-8ccc-864b3dcbfc05">

‘시니또’가 ‘전화걸기 및 수락하기’ 버튼을 누르면 아래에 ‘시니어 전화번호’가
뜨게됩니다.

‘시니또’는 해당 전화번호로 전화를 걸어 시니어가 어떤 요청을 하는지 확인하고,
해당 서비스를 수행합니다.

이 때, 가이드라인과 벗어나는 내용을 부탁하거나, ‘시니또’가 수행할 수 없는 부탁을
할 경우 ‘도움 포기’ 버튼을 눌러 해당 요청을 거부할 수 있습니다.

요청을 완료한 후 ‘도움 완료’버튼을 눌러 완료 대기 상태로 전환할 수 있습니다.

> **콜백 요청 완료 (보호자)**

<img width="194" alt="image-12" src="https://github.com/user-attachments/assets/3e0714ad-c880-4f92-907b-e7d98cef11de">

'보호자'는 '시니또'가 완료 버튼을 누른 후, 시니어에게 해당 요청이 제대로
완료되었는지 확인합니다. 그 다음 '서비스 이용내역' 화면에서 완료 대기 상태인
콜백 서비스를 완료 상태로 변경할 수 있습니다.

만약 보호자가 이틀 동안 이 버튼을 누르지 않으면, 서비스는 자동으로 완료 상태로
변경됩니다.

### 4. 안부 전화 서비스

> **시니어 등록 (보호자)**

<img width="194" alt="image-13" src="https://github.com/user-attachments/assets/48c180bc-709c-4a09-bf07-1b3d01d6d2d0">

‘보호자’는 디지털 서비스를 이용할 시니어를 등록합니다.

마이페이지의 ‘시니어 등록하기’버튼을 눌러 시니어를 등록합니다.

시니어를 등록하기 위해 시니어의 성함 및 전화번호를 입력해야 합니다.

> **안부전화 서비스 등록 (보호자)**

<img width="194" alt="image-14" src="https://github.com/user-attachments/assets/87c4526c-5f0b-4f74-ba52-3541d1d3dbe0">

‘보호자’는 안부전화 서비스를 받을 시니어를 선택하고, 서비스 이용 시간대 및 이용
시간을 선택합니다.

그 후 서비스 이용 기간을 선택하고, 아래의 ‘포인트 계산하기’ 버튼을 클릭하여
비용을 확인합니다.

그 후 ‘시니또’ 가 시니어와 나눌 대화의 주제를 입력하고, ‘신청하기’ 버튼을
클릭하여 안부전화 서비스 신청을 완료합니다.

> **안부전화 서비스 확인 (시니또)**

<img width="194" alt="image-15" src="https://github.com/user-attachments/assets/3af93ab2-e279-4936-962b-2b337d43ff15">

‘시니또’는 ‘안부전화 서비스’에서 현재 대기 중인 안부전화 서비스 목록을 확인할 수
있습니다.

여러 목록들 중 하나를 선택하여 안부전화 서비스를 진행할 시간대를 확인하며,
아래의 ‘서비스 상세 확인하기’버튼을 클릭하여 시니어의 번호를 확인합니다.

> **안부전화 서비스 수락 & 수행 (시니또)**

<img width="194" alt="image-16" src="https://github.com/user-attachments/assets/4a56880b-99c2-4300-8d79-8d7c140c67de">

‘시니또’는 시니어의 전화번호 및 요청사항을 확인한 후, ‘서비스 수락하기’ 버튼을
클릭하여 안부전화 서비스를 수행합니다.

> **안부전화 서비스 완료 대기 (시니또)**

<img width="194" alt="image-17" src="https://github.com/user-attachments/assets/6d42ef15-2b8c-44ad-ad51-e8101281851a">
<img width="194" alt="image-18" src="https://github.com/user-attachments/assets/53ea5384-1ffe-4ebc-8621-9e71ca9895ab">

‘시니또’는 지정된 기간 동안 안부전화 서비스를 수행하고, 서비스 수행기간이 끝나는
날짜 이후에 ‘안부전화 서비스 보고서‘를 작성할 수 있습니다.

보고서에는 안부전화 서비스 기간 동안 시니어와 나누었던 대화를 작성해야 하며,
내용을 작성한 후 ’보고서 제출하기‘ 버튼을 클릭하여 보고서를 제출합니다.

> **안부전화 서비스 완료 (보호자)**

<img width="194" alt="image-18" src="https://github.com/user-attachments/assets/616fe99a-4b97-49b6-ab4b-cf75956473cf">

’보호자‘는 ’서비스 이용내역‘ 페이지에서 완료 대기 상태인 안부전화 서비스를
완료상태로 변경할 수 있습니다.

## 디렉토리 구조

```c
Team8_BE
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── example
    │   │           └── sinitto
    │   │               ├── auth
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── callback
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   ├── service
    │   │               │   └── util
    │   │               ├── common
    │   │               │   ├── config
    │   │               │   ├── dummy
    │   │               │   ├── exception
    │   │               │   ├── interceptor
    │   │               │   ├── properties
    │   │               │   └── service
    │   │               ├── guard
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── guardGuideline
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── helloCall
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── member
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── point
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               ├── review
    │   │               │   ├── controller
    │   │               │   ├── dto
    │   │               │   ├── entity
    │   │               │   ├── repository
    │   │               │   └── service
    │   │               └── sinitto
    │   │                   ├── controller
    │   │                   ├── dto
    │   │                   ├── entity
    │   │                   ├── repository
    │   │                   └── service
    │   └── resources
    │       ├── application-dev.properties
    │       ├── application.properties
    │       ├── keystore.p12
    │       ├── static
    │       │   └── css
    │       └── templates
    │           ├── dummy
    │           └── point
    └── test
        └── java
            └── com
                └── example
                    └── sinitto
                        ├── auth
                        │   ├── entity
                        │   ├── repository
                        │   └── service
                        ├── callback
                        │   ├── entity
                        │   ├── repository
                        │   ├── service
                        │   └── util
                        ├── guard
                        │   ├── entity
                        │   ├── repository
                        │   └── service
                        ├── guardGuideline
                        │   ├── entity
                        │   ├── repository
                        │   └── service
                        ├── hellocall
                        │   ├── entity
                        │   ├── repository
                        │   └── service
                        ├── member
                        │   ├── entity
                        │   ├── repository
                        │   └── service
                        ├── point
                        │   ├── entity
                        │   └── service
                        ├── review
                        │   ├── entity
                        │   └── service
                        └── sinitto
                            ├── entity
                            ├── repository
                            └── service

```
## 관리자 및 개발자 편의기능

### 1. 서버 업데이트(무중단 배포)시 Slack 채널을 통한 알림기능
> Github Action을 통해 서버 업데이트 트리거가 발생하면, 서버 무중단배포 쉘스크립트가 작동하며
슬랙 페이지 서버-로그 채널에서 백엔드 서버 재부팅 로그 (하단 50줄), 무중단 배포 관련 결과를 제공합니다.
> #### 슬랙 초대링크 : [https://join.slack.com/t/sinitto/shared_invite](https://join.slack.com/t/sinitto/shared_invite/zt-2uqws45gg-bsPq8cuH2iQV6Jk1Y3Zglw)

![image](https://github.com/user-attachments/assets/c5202037-a313-443a-b0fa-700e372344b6)

### 2. 사용자의 포인트 충전 및 출금 요청시, Slack 채널을 통한 알림기능
> 보호자의 충전, 시니또의 출금 요청이 들어오면 관리자의 원활한 확인을 위해 슬랙에 메시지를 전송하고, 버튼을 통해 어드민페이지로 접속할 수 있습니다.
> #### 슬랙 초대링크 : [https://join.slack.com/t/sinitto/shared_invite](https://join.slack.com/t/sinitto/shared_invite/zt-2uqws45gg-bsPq8cuH2iQV6Jk1Y3Zglw)

![image](https://github.com/user-attachments/assets/b5927b13-08a8-4345-b31a-cb0e42896075)

### 3. 더미데이터 로그인기능
> 개발 과정에서의 개발자들의 실제 더미데이터 기반 웹 사용 및 테스트를 위해, Server Side Rendering 방식의 더미데이터 로그인 페이지를 구현하였습니다.
서버에 저장된 더미데이터로만 로그인이 가능하며, 개발환경(로컬환경에서 프론트 서버가 열려있을 시) 및 배포서버로 로그인이 가능하고,
로그인시 해당 더미데이터 유저의 JWT(AccessToken 및 RefreshToken)을 발급하여 함께 전송합니다
> #### 더미데이터 로그인 페이지 : https://sinitto.site/dummy [비밀번호 : 1234]
> 
<img width="480" alt="image-3" src="https://github.com/user-attachments/assets/45fa7e58-9451-4d3b-857b-93c78f3b0794">
<img width="500" alt="image-3" src="https://github.com/user-attachments/assets/c0cac5df-2214-4004-b7dd-043971fe4274">


## 협업 효율

프론트엔드-백엔드 간 협업 과정 중 제공된 api를 연결하는 과정에서 소통에 어려움이 있었습니다.

학업과 병행할 수 있는 카카오테크캠퍼스의 특성 상 조원들마다 개발을 진행하는 시간이 달라 실시간으로
문제를 해결하기 어려운 문제점이 있었습니다. 또한 프론트엔드에서 백엔드 서버로 api를 통해 요청을 보냈을 때, 
에러가 발생했을 경우 자세한 오류 메시지가 응답을 통해 나타나지 않았으며, 실제 오류 내용은
백엔드 서버에 기록되는 문제가 있었습니다.

또한 백엔드 담당 조원에게 질문을 하더라도 답변을 받기 전까지는 그동안 하던 작업을
진행하지 못하는 문제가 생겨, 전체적인 개발 속도가 느려지는 문제가 있었습니다.

이러한 방법을 어떻게 해결할 수 있을까 고민해보던 중, 가장 먼저 떠오른 방법은 '에러 코드가 생기면
Slack의 채널에 전송을 하는 것' 이었습니다.

백엔드 서버에서 자바 파일을 실행할 경우 nohup 명렬어를 통해 실행하는데, 이 때 자바 파일을 실행한 기록이
out 확장자 파일에 남게 됩니다. 이 중에서 에러가 발생한 경우 'ERROR' 라는 단어가 포함이 되는데,
이러한 패턴을 확인하여 'ERROR'이 포함된 단어가 out 확장자 파일에 기록되면 Slack의 '서버-로그' 채널에
전송되도록 하였습니다. 이를 통해 프론트엔드 개발자분들이 어떠한 오류인지 확인할 수 있게 하였습니다.

그러나 한 가지 문제가 더 있었습니다. 개발 언어가 다르기 때문에 프론트엔드의 경우 자바에서 발생하는
에러코드를 읽는데 어려움이 있다는 것이었습니다. 실제로 프론트엔드 개발자분들께 해당 에러코드에 대해
이해가 되는지 물어봤을 때, 이해하기가 어려워 ChatGPT와 같은 LLM 모델에 에러코드를 복사하여 질문을
한다는 답변을 받았습니다.

이러한 불편함을 해결하기 위해, 현재 무료로 이용할 수 있는 LLM 모델인 Upstage의 'Solar-pro' 모델과 
연결을 하여 이를 해결하였습니다. 기존의 에러코드를 Slack에 바로 전송하는 대신, 해당 메시지를
rest 요청을 통해 LLM 모델에 답변을 받고, 해당 답변을 포함하여 에러코드와 같이 보내도록 변경하였습니다.

이러한 과정을 통하여 개발 과정에서의 소통의 어려움을 해결할 수 있었습니다.

아래는 실제 개발 과정에서 사용된 내역입니다. 해당 내용과 관련된 쉘 스크립트는 백엔드 내부 쉘스크립트 모음
(https://www.notion.so/3ce05a49e06046469bde14c12b8b9c00) 중 '서버에러 자동 공지 관련코드' 에서
확인할 수 있습니다.
![image](https://github.com/user-attachments/assets/0f7ee6d3-f468-4f48-9e5f-91beaaeefb8a)


