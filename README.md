# myCloset
[개요]
- 설명: 카테고리별 옷을 볼 수 있고 상품 검색 및 좋아요한 옷을 모아 볼수 있는 프로그램입니다.

- 개발 배경: 안드로이드 아키텍쳐에 대한 이해와 로그인 API등을 공부하기 위해 개발하였습니다.


[주요 기능]

- 로그인 기능: 서버에 저장된 아이디와 비밀번호가 일치되는 계정을 로그인 
  - 방식 - Kakao, Google, Email
- 회원가입 기능: 사용자 정보를 입력받아 서버에 저장.
  - 이메일 중복 및 정규식 체크
  - 비밀번호 체크 및 이중 확인
  - 닉네임 중복확인 체크
- 웹뷰 기능: 연결된 웹 링크를 통해 상품의 상세정보를 확인
- 이미지 로딩: 서버에 저장된 상품에 대한 이미지 링크를 가져오고 리사이클러뷰를 이용한 뷰페이저로 이미지를 로딩
- 즐겨찾기: 상품 상단 우측에 있는 ☆ 이미지 클릭 시 서버에 회원 아이디와 상품코드를 저장
- 상품검색: 상품에 대한 정보(상품명, 브랜드명)을 입력 시 서버에서 데이터를 가져와 이미지 출력
- 위시리스트: 즐겨찾기한 상품들을 출력
- FCM: Firebase에서 제공하는 메세지 전송 플랫폼을 이용한 푸시알림

# Development environment

OS : Windows 10 64bit & Unbuntu 18.04.5 LTS

Server : AWS EC2

IDE : Android Studio 4.1

Language : Java, PHP 7.4

DB : Maria DB 10.2

DB Tool : HeidiSQL

Etc Tool: Putty, Postman

# DB Table

1. member
![member](https://user-images.githubusercontent.com/52353492/107740444-7c95c980-6d4e-11eb-8ba5-13614d1ece03.PNG)

2. dress
![dress](https://user-images.githubusercontent.com/52353492/107740446-7dc6f680-6d4e-11eb-9eef-c692f1be12b8.PNG)

3. favorite
![favorite](https://user-images.githubusercontent.com/52353492/107740441-7b649c80-6d4e-11eb-9086-29ccbd8818b2.PNG)



# ScreenShots
![Home](https://user-images.githubusercontent.com/52353492/107740008-76531d80-6d4d-11eb-9950-c884282bf58f.jpg)
![Search](https://user-images.githubusercontent.com/52353492/107739997-73f0c380-6d4d-11eb-84c6-5b722bc62b49.jpg)
![WishList](https://user-images.githubusercontent.com/52353492/107740004-75ba8700-6d4d-11eb-8173-399ee7ea0df6.jpg)
![MyInfo](https://user-images.githubusercontent.com/52353492/107740010-76ebb400-6d4d-11eb-9b7e-cdc357b4d79a.jpg)
![SignIn](https://user-images.githubusercontent.com/52353492/107740000-7521f080-6d4d-11eb-9183-eff286dbda56.jpg)
![SignUp](https://user-images.githubusercontent.com/52353492/107740002-7521f080-6d4d-11eb-8ed5-3d2b0c3be0b4.jpg)
![캡처](https://user-images.githubusercontent.com/52353492/107740317-2d4f9900-6d4e-11eb-806b-e5f8a69c94b0.PNG)
![KakaoTalk_20210214_003833011](https://user-images.githubusercontent.com/52353492/107854172-80fcd800-6e5d-11eb-94d8-bbb6a07d8e14.jpg)
