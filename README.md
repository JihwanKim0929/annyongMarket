<img src="https://github.com/user-attachments/assets/fc0c48e1-de83-423f-972b-63267db9b644">

# annyongMarket
자동 제재, 번역 채팅 적용 중고거래 웹사이트 프로젝트

# 프로젝트 기간
24.11.09 ~ 24.11.25

# 팀원소개
|김지환|김욱권|조수정|
|---|---|---|
|BackEnd,AI(이미지 검증) 모델 학습|AI Server|Publishing/design|

&nbsp;

# 프로젝트 소개
판매글 사진의 위험물(무기)자동 탐지 및 등록거부, 실시간 번역 기능을 탑재한 1대1채팅이 가능한 중고거래 웹사이트 프로젝트

# 핵심 기능
-로그인 및 회원가입
-판매글 등록 기능
-판매글 이미지 내 위험물질 탐지 및 등록 거부 기능
-다중 적발시 탈퇴 기능
-판매글 내 버튼을 통한 1대1 채팅 기능
-채팅 내부 실시간 번역 기능

# 시스템 구성도
<img src="https://github.com/user-attachments/assets/86f66a56-cac2-40bb-a363-b9a4e8458b5a">

&nbsp;

# 개인 기여 사항
## 활용 기술 스택
![Generic badge](https://img.shields.io/badge/jdk-17-orange.svg)

<img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">

<img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> 

![Generic badge](https://img.shields.io/badge/h2-1.4.200-blue.svg)

![JPA](https://img.shields.io/badge/JPA-hibernate-orange)

![Mustache](https://img.shields.io/badge/Mustache-ffdd66?style=for-the-badge&logo=mustache&logoColor=black) - 서버사이드 랜더링 템플릿엔진

![YOLOv8](https://img.shields.io/badge/YOLOv8-ff9600?style=for-the-badge&logo=ai&logoColor=white)

![Roboflow Universe](https://img.shields.io/badge/Roboflow%20Universe-black?style=for-the-badge&logo=roboflow&logoColor=white) - 무기 데이터셋 

![DeepL API](https://img.shields.io/badge/DeepL%20API-0B65C2?style=for-the-badge&logo=deepl&logoColor=white) - 번역 API (한-영 제외한 언어쌍 간의 번역)

![WebSocket](https://img.shields.io/badge/WebSocket-009688?style=for-the-badge&logo=websocket&logoColor=white)

![STOMP](https://img.shields.io/badge/STOMP%20Protocol-0033A0?style=for-the-badge)

-팀에 프론트엔드 전문 팀원이 없어 Spring Boot를 활용해 백엔드 서버 및 서버사이드 렌더링 구현(mustache 템플릿엔진 사용)

-판매글,채팅메세지,채팅방,유저 엔티티 구현(JPA)

-배포는 하지 않고 인메모리 H2 DB를 연동하여 구현

-엔티티 CRUD 관련 메소드들 작성, 회원가입시 국가 선택하도록함(채팅 번역 위해)

-스프링 시큐리티를 활용하여 유저 로그인 구현

-게시글 등록시 이미지 검증 AI모델 호출 로직 구현(RestTemplate), AI모델 서빙은 타 팀원이 FAST API로 AI 서버 구성하여 API 호출하는 방식으로 사용

-무기 감지를 위한 Yolo V8 모델 훈련(Roboflow 데이터셋 활용) - 오탐지 비율 감소를 위해 COCO Dataset에서 무기가 포함되지 않은 배경데이터셋 10%을 포함하여 학습 진행

-웹소켓(Stomp)을 활용하여 유저들간 1대1 실시간 채팅 구현(DB에 채팅 메시지 저장하도록 하여 채팅방 재 입장시, 채팅방에 현재 미입장 상태에서도 기존 메시지 볼수있도록 구현, 웹소켓은 실시간 메시지 렌더링을 위하여 사용)

-한글-영어 쌍은 FastApi의 번역 메소드를 호출하도록 하였고 이외의 경우 DeepL Api를 호출하도록 하여 자신의 말이 채팅 상대방의 언어로 자동 번역되도록 구현(서로 언어가 같은 경우 번역X)


# 구현 결과
실시간 채팅(언어가 같은 경우)

<img src="https://github.com/user-attachments/assets/500d1f38-ddd4-4b30-ab1b-55d8d2cecf39">

실시간 채팅 - 서로 언어가 다른경우

<img src="https://github.com/user-attachments/assets/aaf05501-d797-4b4a-ba12-a989d1ebfe8f">


